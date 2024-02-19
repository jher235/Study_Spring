package org.zerock.w2.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@WebFilter(urlPatterns = {"/*"})
@Log4j2
public class UTF8filter implements Filter {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        log.info("UTF8 filter.....");

        HttpServletRequest req = (HttpServletRequest)servletRequest;

        req.setCharacterEncoding("UTF-8");

        filterChain.doFilter(servletRequest, servletResponse);


    }
}
