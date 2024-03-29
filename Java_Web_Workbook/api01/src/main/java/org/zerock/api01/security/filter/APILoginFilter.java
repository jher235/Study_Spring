package org.zerock.api01.security.filter;


import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

@Log4j2
public class APILoginFilter extends AbstractAuthenticationProcessingFilter {

    //AbstractAuthenticationProcessingFilter생성자에 defaultFilterProcessUtl를 파라미터로 제공하며 생성
    public APILoginFilter(String defaultFilterProcessUrl){
        super(defaultFilterProcessUrl);
    }

    //이 메소드는 인터페이스에서 상속 받은 메소드로, 요청이 들어올 때마다 호출
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        log.info("APILoginFilter -----------------------");

        if(request.getMethod().equalsIgnoreCase("GET")){
            log.info("GET METHOD NOT SUPPORT");
            return null;
        }

        //GET방식이 아니라면 JSON 데이터를 파싱하여 사용자 아이디와 비밀번호를 포함한 맵 객체로 변환
        Map<String, String> jsonData = parseRequestJSON(request);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                jsonData.get("mid"),
                jsonData.get("mpw"));

        return getAuthenticationManager().authenticate(authenticationToken);

//        log.info(jsonData);
//        return null;
    }

    private Map<String, String> parseRequestJSON(HttpServletRequest request){

        //JSON 데이터를 분석해서 mid, mpw 전달값을 Map으로 처리
        try(Reader reader = new InputStreamReader(request.getInputStream())){

            Gson gson = new Gson();

            return gson.fromJson(reader, Map.class);

        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return null;
    }

}
