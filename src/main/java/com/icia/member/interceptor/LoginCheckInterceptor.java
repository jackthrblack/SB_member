package com.icia.member.interceptor;

import com.icia.member.common.SessionConst;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    // 쓰고 안쓰고를 떠나서 부모가 이 매개변수를 다 갖고있다면 무조건 다 써줘야 한다.
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        // 사용자가 요청한 주소
        String requestURI = request.getRequestURI();
        System.out.println("requestURI = " + requestURI);

        //세션 가져옴
        HttpSession session = request.getSession();
        // 세션에 로그인 정보가 있는 지확인
        if(session.getAttribute(SessionConst.LOGIN_EMAIL)==null){
            // 미로그인 상태
            // 로그인을 하지 않은 경우 바로 로그인페이지로 보내고 로그인을 하면 요청한 화면을 보여훔.
            response.sendRedirect("/member/login?redirectURL="+requestURI); // 로그인 주소를 요청하면서 파라미터를 들고간다.
            return false;
        }else{
            // 로그인 상태
            return true;
        }

    }

}
