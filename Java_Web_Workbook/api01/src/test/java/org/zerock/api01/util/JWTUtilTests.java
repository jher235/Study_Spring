package org.zerock.api01.util;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class JWTUtilTests {

    @Autowired
    private JWTUtil jwtUtil;

    @Test
    public void testGenerate(){

        Map<String, Object> claimMap = Map.of("mid", "ABCDE");

        String jwtStr = jwtUtil.generateToken(claimMap,1);

        log.info(jwtStr);
    }

    @Test
    public void testValidate(){
        String jwtStr = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MTE3MTM0MDMs" +
                "Im1pZCI6IkFCQ0RFIiwiaWF0IjoxNzExNzEzMzQzfQ.v2Ue76Gn6lRECISZ3JyKoAgapF-oAiYOVS0_UOifn58";

        Map<String, Object> claim = jwtUtil.validateToken(jwtStr);

        log.info(claim);
    }

}