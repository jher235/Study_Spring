package org.zerock.api01.util;


import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component  //스프링 컨테이너가 해당 클래스의 인스턴스(빈)를 생성하고 관리해야 함을 명시
@Log4j2
public class JWTUtil {

    @Value("${org.zerock.jwt.secret}")// application.properties 또는 application.yml 파일에 정의된 'org.zerock.jwt.secret' 프로퍼티 값을 이 필드에 주입
    private String key;// // JWT 토큰을 생성하거나 검증할 때 사용할 비밀키를 저장하는 필드

    public String generateToken(Map<String, Object> valueMap, int days){//// Map 형태의 정보와 토큰의 유효 기간

        log.info("generateKey...." + key);

        //헤더 부분 설정: JWT의 타입과 사용할 알고리즘을 지정합니다
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg","HS256");

        //payload 부분 설정: 사용자가 전달한 valueMap의 내용을 페이로드에 추가합니다.
        Map<String, Object> payloads = new HashMap<>();
        payloads.putAll(valueMap);

        //테스트 시에는 짧은 유효 기간
        int time = (60 * 24) * days;  //테스트는 분단위로 나중에 60*24 (일)단위 변경할 것

        // JWT 토큰 생성: 설정한 헤더, 페이로드, 발행 시간, 만료 시간, 서명 알고리즘, 비밀키를 사용하여 JWT 토큰을 생성
        String jwtStr = Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))    // 현재 시간을 발행 시간으로 설정
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(time).toInstant())) //현재 시간으로부터 'time'분 후를 만료 시간으로 설정
                .signWith(SignatureAlgorithm.HS256, key.getBytes()) // HS256 알고리즘과 비밀키를 사용하여 JWT 토큰에 서명
                .compact(); // 위의 설정으로 JWT 토큰 문자열을 생성

        return jwtStr;  // 생성된 JWT 토큰 문자열 반환
    }

    //검증하려는 JWT 토큰 문자열을 파라미터로 받아옴
    public Map<String, Object> validateToken(String token) throws JwtException{

//      Map<String, Object>: JWT의 클레임 - 클레임은 토큰에 대한 속성이나 권한 등을 나타내는 키-값 쌍
        Map<String, Object> claim = null;

        claim = Jwts.parser()   //JWT 파싱을 위한 JwtParser 인스턴스를 생성
                .setSigningKey(key.getBytes())  //JWT가 서명된 방식과 일치하는 키 설정
                .parseClaimsJws(token) //JWT서명 검증 - 주어진 토큰 파싱, 유효성 검증, 실패 시 에러
                .getBody(); // 클레임 세트 추출 Map<String, Object> 타입으로 JWT 내부 정보를 담고있음

        return claim;
    }

}
