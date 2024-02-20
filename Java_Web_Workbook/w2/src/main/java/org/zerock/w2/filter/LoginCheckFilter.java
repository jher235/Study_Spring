package org.zerock.w2.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.zerock.w2.dto.MemberDTO;
import org.zerock.w2.service.MemberService;


import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.LogRecord;

@WebFilter(urlPatterns = {"/todo/*"})   // /todo/...로 시작하는 모든 경로에 대해서 필터링 시도
@Log4j2
public class LoginCheckFilter implements Filter {

    //필터가 필터링이 필요한 로직을 구현하는 부분
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        log.info("Login check fitler....");

        HttpServletRequest req = (HttpServletRequest)servletRequest;
        HttpServletResponse resp = (HttpServletResponse)servletResponse;

        HttpSession session = req.getSession();

//        if(session.getAttribute("loginInfo") == null){
//            resp.sendRedirect("/login");
//            return;
//        }


        if(session.getAttribute("loginInfo") != null){
            filterChain.doFilter(servletRequest, servletResponse);  //다음 필터나 목적지(JSP, servlet)로 갈 수 있도록 함.
            return;
        }

        //session에 loginInfo가 없다면 쿠키를 체크
        Cookie cookie = findCookie(req.getCookies(), "remember-me");

        //만약 세션에도 없고 쿠키도 없다면 그냥 로그인 화면으로 이동
        if(cookie==null){
            resp.sendRedirect("/login");
            return;
        }

        log.info("cookie는 존재하는 상황");
        //쿠키가 있다면 uuid값 받아와서 저장
        String uuid = cookie.getValue();

        try {
            //uuid를 통해 db확인
            MemberDTO memberDTO = MemberService.INSTANCE.getByUuid(uuid);

            log.info("쿠키의 값으로 조회한 사용자 정보 : " + memberDTO);
            if(memberDTO == null){
                throw new Exception("Cookie value is not valid");
            }

            session.setAttribute("loginInfo", memberDTO);
            filterChain.doFilter(servletRequest, servletResponse);

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("/login");
        }
    }

    private Cookie findCookie(Cookie[] cookies, String cookieName){
        if (cookies == null || cookies.length==0){
            return null;
        }

        //null일 수 있는 객체 Optinal 사용
        Optional<Cookie> result = Arrays.stream(cookies)
                .filter(ck -> ck.getName().equals(cookieName))
                .findFirst();

        //삼항 연산자
        return result.isPresent() ? result.get():null;
    }

}
