package com.example.stomp_websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker //WebSocket을 사용하여 메시지 브로커(STOMP 프로토콜 사용)를 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메모리 기반의 심플 메시지 브로커 활성화, '/topic' 경로로 시작하는 메시지 구독하는 클라이언트에게 메시지 전송할 수 있음.
        // 다시 말해 클라이언트는 '/topic' 경로를 통해 메세지 수신이 가능함.
        registry.enableSimpleBroker("/topic");
        //애플리케이션 메세지의 목적지를 설정. '/app'으로 시작하는 경로로 메세지를 보내면 애플리케이션 내 메세지 핸들러로 라우팅됨
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {// STOMP 프로토콜을 위한 WebSocket 엔드포인트 등록
        //클라이언트가 WebSocket에 연결할 수 있는 엔드포인트에 '/gs-guide-websocket' 경로 추가.
        registry.addEndpoint("/gs-guide-websocket");
    }
}
