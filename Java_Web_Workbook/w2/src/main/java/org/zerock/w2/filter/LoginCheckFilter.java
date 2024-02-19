package org.zerock.w2.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;


import java.io.IOException;
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

        if(session.getAttribute("loginInfo") == null){
            resp.sendRedirect("/login");
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);  //다음 필터나 목적지(JSP, servlet)로 갈 수 있도록 함.

    }
}
