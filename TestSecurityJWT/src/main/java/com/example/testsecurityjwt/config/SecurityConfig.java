package com.example.testsecurityjwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http.
                csrf((auth)->auth.disable()); //JWT를 사용하면 stateless 방식이므로 csrf무효화

        //From 로그인 방식 disable
        http
                .formLogin((auth)->auth.disable());

        //http basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());

        http
                .authorizeHttpRequests((auth)-> auth
                        .requestMatchers("/login", "/", "/join").permitAll() //모든 권한 허용
                        .requestMatchers("/admin").hasRole("ADMIN") //ADMIN권한만 접근 허용
                        .anyRequest().authenticated()); //로그인한 사용자만 접근이 가능하도록 설정

        //가장 중료!
        //세션 설정 JWT에서는 세션을 stateless로 관리함. 따라서 sessionManagement 메서드에서 세션을 STATELESS로 설정해줘야 함.
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        return http.build();
    }
}
