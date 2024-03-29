package org.zerock.api01.security.filter;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;

@Log4j2
public class APILoginFilter extends AbstractAuthenticationProcessingFilter {

    //AbstractAuthenticationProcessingFilter생성자에 defaultFilterProcessUtl를 파라미터로 제공하며 생성
    public APILoginFilter(String defaultFilterProcessUtl){
        super(defaultFilterProcessUtl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        log.info("APILoginFilter -----------------------");

        return null;
    }


}
