package org.zerock.b01.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.zerock.b01.security.CustomUserDetailsService;
import org.zerock.b01.security.handler.Custom403Handler;

import javax.sql.DataSource;

//스프링 시큐리티 설정
@Log4j2
@Configuration
@RequiredArgsConstructor    //DataSource, userDetailsService 주입
// @EnableMethodSecurity(prePostEnabled = true)  // 6.1 이후로 deprecated
@EnableMethodSecurity(prePostEnabled = true)
public class CustomSecurityConfig {

    //주입 필요
    private final DataSource dataSource;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("------------configure---------------");

        //커스텀 로그인 페이지
        http.formLogin(fLogin -> {
            fLogin.loginPage("/member/login");
//            fLogin.loginPage("/login");
        });

        http.csrf(config->{
            config.disable();   //CSFR토큰을 비활성화
        });

        http.rememberMe(config->{
            config.key("12345678");
            config.tokenRepository(persistentTokenRepository());
            config.userDetailsService(userDetailsService);
            config.tokenValiditySeconds(60*60*24*30);   //한달 유지
        });

        //스프링 시큐리티의 예외 처리를 구성하기 위한 메서드. 이 메서드를 통해 예외 처리 방식을 사용자 정의
        http.exceptionHandling(config->{
            config.accessDeniedHandler(accessDeniedHandler());//접근 거부 상황이 발생했을 때의 처리 로직을 사용자 정의
        });

        http.oauth2Login(config -> {
            config.loginPage("/member/login");  //사용자가 인증되지 않은 상태에서 보호된 리소스에 접근 시도할 때 리다이렉트될 로그인 페이지의 URL을 설정
        });

        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new Custom403Handler();
    }

//     정적 리소스에 대한 보안 필터 체인의 적용 무시
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){

        log.info("----------------web configure-----------------");

        return (web -> web.ignoring().requestMatchers(PathRequest.
                toStaticResources().atCommonLocations()));
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;
    }


}
