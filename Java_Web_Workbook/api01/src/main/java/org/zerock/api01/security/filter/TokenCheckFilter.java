package org.zerock.api01.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zerock.api01.util.JWTUtil;

import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class TokenCheckFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();  //현재 요청의 URI 가져옴

        if(!path.startsWith("/api/")){  //URI가 /api/로 시작하지 않는 경우 = API요청이 아닌 경우
            filterChain.doFilter(request, response);    //필터링 과정 계속 진행
            return;
        }

        log.info("Token Check Filter ...........................");
        log.info("JWTUtil: " + jwtUtil);

        filterChain.doFilter(request, response);


    }
}
