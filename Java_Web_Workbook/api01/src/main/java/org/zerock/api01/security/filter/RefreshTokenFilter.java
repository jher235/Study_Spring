package org.zerock.api01.security.filter;

import com.google.gson.Gson;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zerock.api01.security.exception.RefreshTokenException;
import org.zerock.api01.util.JWTUtil;

import java.awt.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class RefreshTokenFilter extends OncePerRequestFilter {


    private final String refreshPath;

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();  //getRequestURI()로 요청 경로 받아옴

        if(!path.equals(refreshPath)){  //요청 경로가 토큰 갱신을 위한 특정 경로가 아니라면
            log.info("skip refresh token filter......");
            filterChain.doFilter(request,response);
            return;
        }

       log.info("Refresh Token Filter ... run....................1");

        //전송된 JSON에서 accessToken과 refreshToken을 얻어온다.
        Map<String, String> tokens = parseRequestJSON(request);     //요청 본문에 포함된 JSON 데이터를 파싱하여 Map객체로 변환 ->

        String accessToken = tokens.get("accessToken");
        String refreshToken = tokens.get("refreshToken");

        log.info("accessToken: "+accessToken);
        log.info("refreshToken: "+ refreshToken);

        try{
            checkAccessToken(accessToken);
        }catch (RefreshTokenException refreshTokenException){
            refreshTokenException.sendResponseError(response);
        }

        Map<String, Object> refreshClaims = null;

        try {
            refreshClaims = checkRefreshToken(refreshToken); // refreshToken의 유효성을 검사 ->토큰이 유효한지, 변조되지 않았는지 등을 확인하여 저장
            log.info(refreshClaims);

            //Refresh Token의 유효 시간이 얼마 남지 않은 경우
            Integer exp = (Integer)refreshClaims.get("exp");//'exp'(만료 시간)를 가져와 exp 변수에 저장

            // exp 값을 밀리초 단위로 변환하여 expTime 변수에 Date 객체로 저장
            Date expTime = new Date(Instant.ofEpochMilli(exp).toEpochMilli()*1000);

            //new Date(System.currentTimeMillis());: 현재 시간을 current 변수에 Date 객체로 저장
            Date current = new Date(System.currentTimeMillis());

            //만료 시간과 현재 시간의 간격 계산
            //만일 3일 미만인 경우에는 Refresh Token도 다시 생성
            long gapTime = (expTime.getTime() - current.getTime());

            log.info("------------------------------------");
            log.info("current: "+current);
            log.info("expTime: "+ expTime);
            log.info("gap: " + gapTime);

            //refreshClaims에서 사용자 식별자를 나타내는 'mid'를 가져와 mid 변수에 저장
            String mid = (String)refreshClaims.get("mid");

            //사용자 식별자 mid를 포함하는 새로운 액세스 토큰을 생성합니다. 유효 기간은 1일
            //이 상태까지 오면 무조건 AccessToken은 새로 생성
            String accessTokenValue = jwtUtil.generateToken(Map.of("mid",mid),1);

            String refreshTokenValue = tokens.get("refreshToken"); // 기존에 저장된 tokens 컬렉션에서 리프레시 토큰 값을 가져옴

            //만일 RefreshToken이 3일도 안남았다면...
            if(gapTime < (1000 * 60 * 60 * 24 * 3)){
                log.info("new Refresh Token required... ");
                refreshTokenValue = jwtUtil.generateToken(Map.of("mid", mid),30);
            }

            log.info("Refresh Token result.................");
            log.info("accessToken: "+accessTokenValue);
            log.info("refreshToken: "+ refreshTokenValue);

            sendTokens(accessTokenValue,refreshTokenValue,response);

        }catch (RefreshTokenException refreshTokenException){
            refreshTokenException.sendResponseError(response);
        }
    }

    private Map<String, String> parseRequestJSON(HttpServletRequest request){

        //JSON 데이터를 분석해서 mid, mpw 전달 값을 Map으로 처리
        //request.getInputStream를 통해 요청 본문에 접근하고 InputStreamReader로 Reader객체로 변환
        try (Reader reader = new InputStreamReader(request.getInputStream())){
            Gson gson = new Gson();     //JSON파싱을 위한 Gson 객체 선언

            return gson.fromJson(reader, Map.class);    //accessToken, refreshToken과 같은 키-값 쌍을 받아옴

        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return null;
    }

    private void checkAccessToken(String accessToken) throws RefreshTokenException{
        try{
            jwtUtil.validateToken(accessToken);//토큰 유효성 검사
        }catch (ExpiredJwtException expiredJwtException){
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.NO_ACCESS);
        }
    }

    private Map<String, Object> checkRefreshToken(String refreshToken) throws RefreshTokenException{
        try {
            Map<String, Object> values = jwtUtil.validateToken(refreshToken);
            return values;
        }catch (ExpiredJwtException expiredJwtException){
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.OLD_REFRESH);
        }catch (MalformedJwtException malformedJwtException){
            log.error("MalformedJwtException-----------------------");
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.NO_REFRESH);
        }catch (Exception exception){
            new RefreshTokenException(RefreshTokenException.ErrorCase.NO_REFRESH);
        }
        return null;
    }

    private void sendTokens(String accessTokenValue, String refreshTokenValue, HttpServletResponse response){

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Gson gson = new Gson();

        String jsonStr = gson.toJson(Map.of("accessToken",accessTokenValue,"refreshToken",refreshTokenValue));

        try {
            response.getWriter().println(jsonStr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
