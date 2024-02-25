package org.zerock.b01.controller;


import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class SampleJSONController {

    //배열을 출력
    @GetMapping("/helloArr")
    public String[] helloArr(){
        log.info("helloArr.....................");
        return new String[]{"AAA","BBB","CCC"};
    }

}
