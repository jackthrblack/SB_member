package com.icia.member;

import com.icia.member.dto.MemberDetailDTO;
import com.icia.member.dto.MemberLoginDTO;
import com.icia.member.dto.MemberMapperDTO;
import com.icia.member.dto.MemberSaveDTO;
import com.icia.member.repository.MemberMapperRepository;
import com.icia.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Member;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

@SpringBootTest
public class MemberTest {

    @Autowired
    private MemberService ms;

    @Autowired
    private MemberMapperRepository mmr;

    // unit => 가장 작은 단위
    @Test
    @DisplayName("회원 데이터 생성")
    public void newMembers() {
        IntStream.rangeClosed(1, 15).forEach(i -> {
            ms.save(new MemberSaveDTO("email" + i, "pw" + 1, "name" + i));
        });
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("삭제 테스트")
    public void deleteMember() {
        // given
        MemberSaveDTO memberSaveDTO = new MemberSaveDTO("deleteEamil", "deletePassword", "deleteName");
        Long memberId = ms.save(memberSaveDTO);
        System.out.println(memberSaveDTO);
        System.out.println(memberId);
        // when
        ms.deleteById(memberId);
        // then
        boolean result = ms.login(new MemberLoginDTO(memberSaveDTO.getMemberEmail(), memberSaveDTO.getMemberPassword()));
        assertThat(result).isEqualTo(false);

        // MemberDetailDTO memberDetailDTO = ms.findById(memberId);
        assertThrows(NoSuchElementException.class, () -> {
            assertThat(ms.findById(memberId)).isNull();
        });
    }

  /*  @Test
    @Transactional
    @Rollback
    @DisplayName("회원수정 테스트")
    public void memberUpdate() {

        MemberSaveDTO memberSaveDTO = new MemberSaveDTO("email", "password", "name");
        Long memberId = ms.save(memberSaveDTO);


        MemberDetailDTO memberDetailDTO = ms.findById(memberId);
        MemberDetailDTO memberUpdate = ms.findById(memberId);
        memberUpdate.setMemberName("수정후이름");

        // 수정
        memberId = ms.update(memberUpdate);
        memberUpdate = ms.findById(memberId);

        // 수정 후 비교
        System.out.println("수정 전 : " + memberDetailDTO.getMemberName());
        System.out.println("수정 후 : " + memberUpdate.getMemberName());
        assertThat(memberDetailDTO.getMemberName()).isNotEqualTo(memberUpdate.getMemberName());
*/

    @Test
    @Transactional
    @Rollback
    @DisplayName("회원수정 테스트")
    public void memberUpdateTest(){
        /*
        * 1. 신규회원등록
        * 2. 신규등록한 회원에 대한 이름 수정
        * 3. 신규등록시 사용한 이름과 DB에 저장된 이름이 일치하는지 판단
        * 4. 일치하지 않아야 테스트 통과과
       * */

        //1.
        String memberEmail = "수정회원1";
        String memberPassword = "수정비번1";
        String memberName="수정이름1";

        MemberSaveDTO memberSaveDTO = new MemberSaveDTO(memberEmail, memberPassword, memberName);
        Long saveMemberId= ms.save(memberSaveDTO);

        // 가입 후 DB에서 이름 조회
        String saveMemberName = ms.findById(saveMemberId).getMemberName();

        //2.
        String updateName="수정용이름";
        MemberDetailDTO updateMember = new MemberDetailDTO(saveMemberId, memberEmail, memberPassword, updateName);
        ms.update(updateMember);

        // 수정 후 DB에서 이름 조회
        String updateMemberName= ms.findById(saveMemberId).getMemberName();

        //3,4
        assertThat(saveMemberName).isNotEqualTo(updateMemberName);

    }

    @Test
    @Transactional
    @DisplayName("mybatis 목록 출력 테스트")
    public void memberListTest(){
        List<MemberMapperDTO> memberList = mmr.memberList();
        for(MemberMapperDTO m:memberList){
            System.out.println("m.toString = " + m.toString());
        }

        List<MemberMapperDTO> memberList2 = mmr.memberList2();
        for(MemberMapperDTO m:memberList2){
            System.out.println("m.toString = " + m.toString());
        }
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("mybatis 회원")
    public void memberSaveTest(){
        MemberMapperDTO memberMapperDTO = new MemberMapperDTO("회원이메일1", "회원비번1", "회원이름1");
        mmr.save(memberMapperDTO);

        MemberMapperDTO memberMapperDTO2 = new MemberMapperDTO("회원이메일2", "회원비번2", "회원이름2");
        mmr.save2(memberMapperDTO2);
    }

}



