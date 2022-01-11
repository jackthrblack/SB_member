package com.icia.member.controller;

import com.icia.member.dto.MemberDetailDTO;
import com.icia.member.dto.MemberLoginDTO;
import com.icia.member.dto.MemberSaveDTO;
import com.icia.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

import static com.icia.member.common.SessionConst.LOGIN_EMAIL;

@Controller
@RequestMapping("/member/*")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService ms;

    //회원가입폼
    @GetMapping("save")
    public String saveForm(){
        return "member/save";
    }

    //회원가입
    @PostMapping("save")
    public String save(@ModelAttribute MemberSaveDTO memberSaveDTO){
        Long memberId = ms.save(memberSaveDTO);
        return "member/login";
    }

    //로그인폼
    @GetMapping("login")
    public String loginForm(){

        return "member/login";
    }

    //로그인처리
    @PostMapping("login")
    public String login(@ModelAttribute MemberLoginDTO memberLoginDTO, HttpSession session,
                        @RequestParam(defaultValue = "/") String redirectURL){

        System.out.println("MemberController.login");
        System.out.println("redirectURL = " + redirectURL);

        boolean loginResult = ms.login(memberLoginDTO);

        if (loginResult){
//            session.setAttribute("loginEmail", memberLoginDTO.getMemberEmail());
            session.setAttribute(LOGIN_EMAIL, memberLoginDTO.getMemberEmail());
           // return "redirect:/member/"; //다시 컨트롤러로 목록 요청해야 하는까 목록은 리다이렉트~
           // return "member/mypage";
            return "redirect:"+redirectURL; // 사용자가 요청한 주소로 보내주기 위해
        }else {
            return "member/login";

        }
    }

    //로그아웃
    @GetMapping("logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "index";
    }

    //회원목록
    @GetMapping
    public String findAll(Model model){
        List<MemberDetailDTO> memberList = ms.findAll();
        model.addAttribute("memberList",memberList);
        return "member/findAll";
    }


    //회원조회(/member/5)
    @GetMapping("{memberId}")
    public String findById(@PathVariable("memberId") Long memberId, Model model){
        MemberDetailDTO member = ms.findById(memberId);
        model.addAttribute("member", member);
        return "member/findById";
    }


    //회원조회(ajax)
    @PostMapping("{memberId}")
    public @ResponseBody MemberDetailDTO detail(@PathVariable("memberId") Long memberId) {
        MemberDetailDTO member = ms.findById(memberId);
        return member;
    }

    //회원삭제(/member/delete/5)
    @GetMapping("delete/{memberId}")
    public String deleteById(@PathVariable("memberId") Long memberId){
        ms.deleteById(memberId);
        return "redirect:/member/";
    }

    //회원삭제(/member/5)
    @DeleteMapping("{memberId}")
    public ResponseEntity deleteById2(@PathVariable Long memberId){
            ms.deleteById(memberId);
            /*
              단순 화면 출력이 아닌 데이터를 리턴하고자할 때 사용하는 리턴방식
            * ResponesEntity: 데이터 & 상태코드를 함께 리턴 할 수 있음 => 둘다 보내거나 하나만 보내거나
            http 상태코드(200,400(bad request),404(주소가 틀렸거나 없거나),405,500(자바문법오류) 등)
            * ReponseBody: 데이터를 리턴 할 수 있음.
            * */
        // 200 코드를 리턴
        return new ResponseEntity(HttpStatus.OK);
    }

    //정보수정 요청
    @GetMapping("update")
    public String update_form(Model model, HttpSession session){
        // 세션값은 오브젝트의 값인데 스트링으로 타입을 정해놓아서 오류가난다
        // -> 오브젝트는 최상위의 타입이라고 생가각하면 된다 => 그래서 강제형변환을 해줘야한다.
        String memberEmail = (String) session.getAttribute(LOGIN_EMAIL);

        MemberDetailDTO member = ms.findByEmail(memberEmail);
        model.addAttribute("member",member);

    return "member/update";
    }

    //정보수정(post)
    @PostMapping("update")
    public String update(@ModelAttribute MemberDetailDTO memberDetailDTO){
        Long memberId = ms.update(memberDetailDTO);
        // 수정완료 후 해당회원의 상세페이지 출력
        return "redirect:/member/"+memberDetailDTO.getMemberId();
   }

   //수정처리(PUT)
    @PutMapping("{memberId}")
    // json으로 데이터가 전달되면 @RequstBody로 받아줘야함.
    public ResponseEntity update2(@RequestBody MemberDetailDTO memberDetailDTO){
        Long memberId = ms.update(memberDetailDTO);
        return new ResponseEntity(HttpStatus.OK);
    }


}
