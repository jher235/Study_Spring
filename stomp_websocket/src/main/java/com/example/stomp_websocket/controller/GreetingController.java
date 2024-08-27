package com.example.stomp_websocket.controller;

import com.example.stomp_websocket.domain.Greeting;
import com.example.stomp_websocket.domain.HelloMessage;
import com.example.stomp_websocket.domain.Message;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import javax.swing.plaf.SpinnerUI;

@Controller
@AllArgsConstructor
public class GreetingController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/hello") //'/hello' 경로로 보내는 STOMP 메시지를 처리할 메서드 지정
    @SendTo("/topic/greetings") //이 반환값(greeting)을 '/topic/greetings' 경로로 구독하고 있는 모든 클라이언트에게 전송
    public Greeting greeting(HelloMessage message) throws Exception{
        return new Greeting("안녕, "+ HtmlUtils.htmlEscape(message.getName())+ "~");//HTML 특수 문자를 이스케이프 처리함.
    }

    @MessageMapping("/message/{roomId}")
    public void messaging(Message message, @DestinationVariable String roomId) throws Exception{
        messagingTemplate.convertAndSend("/topic/"+roomId, message.getContent());
    }
}
