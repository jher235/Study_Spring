package org.zerock.api01.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.zerock.api01.security.APIUserDetailsService;
import org.zerock.api01.security.filter.APILoginFilter;
import org.zerock.api01.security.filter.TokenCheckFilter;
import org.zerock.api01.security.handler.APILoginSuccessHandler;
import org.zerock.api01.util.JWTUtil;

import java.security.Security;

@Configuration
@Log4j2
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class CustomSecurityConfig {

    //DI
    private final APIUserDetailsService apiUserDetailsService;

    private final JWTUtil jwtUtil;


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();     //패스워드 인코더는 해시값에 솔트를 포함하는 인코더
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){

        log.info("------------web configure--------------");

        return web -> web.ignoring()
                .requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations()
                );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {

        //AuthenticationManager 설정. HttpSecurity에서 AuthenticationManagerBuilder 인스턴스
        AuthenticationManagerBuilder authenticationManagerBuilder
                = http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(apiUserDetailsService)  //사용자 정보 가져올 서비스 클래스 설정
                .passwordEncoder(passwordEncoder());    // 패스워드 인코더 설정

        //설정된 AuthenticationManagerBuilder를 사용하여 AuthenticationManager인스턴스 생성
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        //반드시 필요 - HttpSecurity에 AuthenticationManager를 설정, 인증시 필요
        http.authenticationManager(authenticationManager);

        //APILoginFilter - 특정 경로에 대한 요청을 처리하기 위한 커스텀 필터 설정
        APILoginFilter apiLoginFilter = new APILoginFilter("/generateToken");
        apiLoginFilter.setAuthenticationManager(authenticationManager); //생성된 필터에 AuthenticationManager를 설정 -로그인 시도시 인증과정 처리

        //APILoginSuccessHandler
        APILoginSuccessHandler successHandler = new APILoginSuccessHandler(jwtUtil);
        //SuccessHandler 세팅
        apiLoginFilter.setAuthenticationSuccessHandler(successHandler);


        //APILoginFilter의 위치 조정 -> UsernamePasswordAuthenticationFilter 전에 추가하여 처리 순서 정의
        http.addFilterBefore(apiLoginFilter, UsernamePasswordAuthenticationFilter.class);

        //api로 시작하는 모든 경로는 TokenCheckFilter 동작
        http.addFilterBefore(
                tokenCheckFilter(jwtUtil),
                UsernamePasswordAuthenticationFilter.class
        );


        log.info("------------ configure ------------");

        http.csrf(config-> config.disable());   //csrf토큰 비활성화

        http.sessionManagement(config-> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); //세션을 사용하지 않음


        return http.build();
    }

    private TokenCheckFilter tokenCheckFilter(JWTUtil jwtUtil){
        return new TokenCheckFilter(jwtUtil);
    }

}
