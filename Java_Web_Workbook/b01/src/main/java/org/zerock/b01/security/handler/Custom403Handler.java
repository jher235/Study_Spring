package org.zerock.b01.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;


//AccessDeniedHandler 인터페이스를 구현하는 Custom403Handler 클래스를 정의.
// 이 클래스는 접근 거부(403) 상황을 사용자 정의 방식으로 처리함.
@Log4j2
public class Custom403Handler implements AccessDeniedHandler {

    @Override   //접근 거부 상황이 발생했을 때 호출됨
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        log.info("----------------ACCESS DENIED-----------------");

        response.setStatus(HttpStatus.FORBIDDEN.value());   // HTTP 응답 상태 코드를 403(Forbidden)으로 설정

        //json 요청이었는지 확인. 요청의 Content-Type 헤더 값을 가져와 contentType 변수에 저장
        String contentType = request.getHeader("Content-Type");

//      contentType이 "application/json"으로 시작하는지 확인, JSON 요청인지 여부를 jsonRequest 변수에 저장
        boolean jsonRequest = contentType.startsWith("application/json");

        log.info("isJSON: "+ jsonRequest);

        //요청이 JSON 요청이 아닌 경우, 즉 일반 웹 페이지 요청인 경우
        if(!jsonRequest){
//          사용자를 로그인 페이지로 리다이렉트하고, 쿼리 파라미터로 error=ACCESS_DENIED를 추가하여 접근 거부 상황을 알림
            response.sendRedirect("/member/login?error=ACCESS_DENIED");
        }

    }
}
