package com.example.stomp_websocket.controller;

import com.example.stomp_websocket.domain.Greeting;
import com.example.stomp_websocket.domain.HelloMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class GreetingController {

    @MessageMapping("/hello") //'/hello' 경로로 보내는 STOMP 메시지를 처리할 메서드 지정
    @SendTo("/topic/greetings") //이 반환값(greeting)을 '/topic/greetings' 경로로 구독하고 있는 모든 클라이언트에게 전송
    public Greeting greeting(HelloMessage message) throws Exception{
        return new Greeting("안녕, "+ HtmlUtils.htmlEscape(message.getName())+ "~");//HTML 특수 문자를 이스케이프 처리함.
    }

}
