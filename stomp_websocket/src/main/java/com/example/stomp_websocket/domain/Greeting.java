package com.example.stomp_websocket.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class Greeting {
    private String content;

    public String getContent(){
        return content;
    }

}
