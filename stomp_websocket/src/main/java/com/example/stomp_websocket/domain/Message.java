package com.example.stomp_websocket.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Message {  //수신자(혹은 수신 단체), 발신자도 작성해줄 수 있으면 좋음.
    String content;
}