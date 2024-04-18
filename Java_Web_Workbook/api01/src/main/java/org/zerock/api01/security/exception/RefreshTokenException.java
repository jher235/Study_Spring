package org.zerock.api01.security.exception;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

public class RefreshTokenException extends RuntimeException{
    private ErrorCase errorCase;    //맴버변수로 이넘 선언

    public enum ErrorCase{  //예외의 종류를 나타내는 enum
        NO_ACCESS, BAD_ACCESS, NO_REFRESH, OLD_REFRESH, BAD_REFRESH
    }

    public RefreshTokenException(ErrorCase errorCase){  //생성자 ->  ErrorCase를 받아와서 할당
        super(errorCase.name());
        this.errorCase = errorCase;
    }

    public void sendResponseError(HttpServletResponse response){

        response.setStatus(HttpStatus.UNAUTHORIZED.value());   //HTTP 응답의 상태 코드를 401 Unauthorized로 설정, HttpStatus.UNAUTHORIZED.value()는 401의미
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);  //응답 본문이 JSON 형식임을 의미

        Gson gson = new Gson();

        String responseStr = gson.toJson(Map.of("msg", errorCase.name(), "time", new Date()));  //파라미터들을 포함하는 json 반환

        try{
            response.getWriter().println(responseStr);  //getWriter() 메소드를 호출하여 응답 본문에 쓰기 위한 PrintWriter 객체를 얻고 이 객체의 println 메소드를 사용하여 앞서 작성한 json을 응답 본문에 반환함
        }catch (IOException e){
            throw new RuntimeException(e); //오류 던지기
        }

    }

}
