package org.zerock.api01.security.filter;

import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zerock.api01.util.JWTUtil;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
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


}
