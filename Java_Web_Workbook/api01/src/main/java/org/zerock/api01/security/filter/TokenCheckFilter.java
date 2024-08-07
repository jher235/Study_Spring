package org.zerock.api01.security.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zerock.api01.security.APIUserDetailsService;
import org.zerock.api01.security.exception.AccessTokenException;
import org.zerock.api01.util.JWTUtil;

import java.io.IOException;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class TokenCheckFilter extends OncePerRequestFilter {//OncePerRequestFilter는 하나의 요청에 대해 한번씩 동작하는 필터

    private final APIUserDetailsService apiUserDetailsService;
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

        try {
            Map<String, Object> payload =  validateAccessToken(request);

            //mid
            String mid = (String) payload.get("mid");

            log.info("mid: "+ mid);

            UserDetails userDetails = apiUserDetailsService.loadUserByUsername(mid); //mid에 해당하는 UserDetails 가져옴

            // UserDetails 객체를 기반으로 UsernamePasswordAuthenticationToken을 생성, 사용자의 인증 정보를 포함하는 토큰
            // 비밀번호는 이미 검증되었기 때문에 null. userDetails.getAuthorities()로 사용자의 권한 정보도 포함함
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());

            // authenticationToken을 SecurityContextHolder에 설정. Spring Security는 SecurityContextHolder에 저장된 인증 정보를 기반으로 접근을 제어함
            //이를 통해 컨트롤러나 서비스에서 @PreAuthorize 어노테이션을 사용할 수 있음
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);
        }catch (AccessTokenException accessTokenException){
            accessTokenException.sendResponseError(response);
        }


    }

    private Map<String, Object> validateAccessToken(HttpServletRequest request)throws AccessTokenException{

        String headerStr = request.getHeader("Authorization");  //토큰 획득

        if(headerStr == null || headerStr.length() <0){
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.UNACCEPT);  //토큰이 없거나 길이가 짧을 때
        }

        //Bearer 생략
        String tokenType = headerStr.substring(0,6);//토큰 유형을 확인하기 위해 헤더 문자열에서 첫 6글자를 추출하여 "Bearer"와 비교
        String tokenStr = headerStr.substring(7);

        if(tokenType.equalsIgnoreCase("Bearer")==false){
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADTYPE);
        }

        try {
            Map<String, Object> values = jwtUtil.validateToken(tokenStr);   //유효성 검사,  이 메서드는 토큰이 유효하다면 토큰에 포함된 정보(claims)를 Map 형태로 반환

            return values;
        }catch (MalformedJwtException malformedJwtException){   //잘못된 형식일 때
            log.error("MalformedJwtException-------------------------");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.MARFORM);
        }catch (SignatureException signatureException){ //JWT의 서명 검증에 실패한 경우
            log.error("SignatureException--------------------------");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADSIGN);
        }catch (ExpiredJwtException expiredJwtException){   //JWT가 만료된 경우
            log.error("ExpiredJwtException--------------------------");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.EXPIRED);
        }
    }

}
