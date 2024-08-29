package com.example.testsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 이 클래스가 스프링 시큐리티에서도 관리가 됨.
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{

        //이 경로에 대해 여러 권한 설정이 있는데 몇 개만 보면
        // permitAll(모든 사용자가 접근 가능),
        // authenticated(로그인을 진행하면 모두 접근할 수 있음),
        // hasRole(특정 룰이 있어야 로그인 한 후 사용가능),
        // denyAll(로그인을 진행해도 모든 사용자가 접근할 수 없음.
        httpSecurity.authorizeHttpRequests((auth)->auth //특정 경로에 요청을 허용, 거부할 수 있음. 람다식으로 작성.
                        .requestMatchers("/","/login").permitAll() //requestMatchers는 특정한 경로의 요청에 대해 작업을 진행하려 할 때 사용.
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/my/**").hasAnyRole("ADMIN, USER") //경로에는 와일드카드를 통해 하위페이지를 포함. 룰은 명시된 룰 중 하나라면 접근 가능.
                        .anyRequest().authenticated() //이 외 모든 요청에 대하여 설정.
//                        .anyRequest().denyAll() //사용하지 않는 모든 경로는 아무도 접근하지 못하도록 할 수도 있다.
        );

        //동작 순서가 상단부터 동작되므로 순서에 유의해야 한다.
        return httpSecurity.build();//SecurityFilterChain를 설정한 후 빌드해서 리턴해주는 것.
    }
}
