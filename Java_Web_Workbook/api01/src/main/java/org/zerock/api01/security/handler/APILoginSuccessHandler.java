package org.zerock.api01.security.handler;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.zerock.api01.util.JWTUtil;

import java.io.IOException;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class APILoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        log.info("Login Success Handler................");

        //응답의 컨텐츠 타입을 JSON으로 설정
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        log.info(authentication);   //현재 인증된 사용자의 정보
        log.info(authentication.getName()); //username 확인

        //JWT 토큰에 포함될 클레임을 생성 - <key = mid, value = username>
        Map<String, Object> claim = Map.of("mid", authentication.getName());
        //Access Token의 유효기간은 1일
        String accessToken = jwtUtil.generateToken(claim,1);
        //Refresh Token의 유효기간은 30일
        String refreshToken = jwtUtil.generateToken(claim,30);

        Gson gson = new Gson(); // Java 객체를 JSON으로 변환하는 Gson객체 생성

        //액세스 토큰, 리프레시 토큰 포함하는 맵 객체 생성
        Map<String, Object> keyMap = Map.of("accessToken", accessToken, "refreshToken", refreshToken);

        //생성한 맵 객체를 JSON으로 변환
        String jsonStr = gson.toJson(keyMap);

        //생성된 JSON 문자열 응답 본문에 출력 - 클라이언트가 생성된 JSON 문자열 사용가능
        response.getWriter().println(jsonStr);

    }
}
