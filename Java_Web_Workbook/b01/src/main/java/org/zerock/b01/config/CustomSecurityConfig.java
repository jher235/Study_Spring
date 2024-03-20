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

@Log4j2
@Configuration
@RequiredArgsConstructor
// @EnableMethodSecurity(prePostEnabled = true)  // 6.1 이후로 deprecated
@EnableMethodSecurity(prePostEnabled = true)
public class CustomSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("------------configure---------------");

        http.formLogin(fLogin -> {
            fLogin.loginPage("/member/login");
//            fLogin.loginPage("/login");


        });

        return http.build();
    }

//     정적 리소스에 대한 보안 필터 체인의 적용 무시
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){

        log.info("----------------web configure-----------------");

        return (web -> web.ignoring().requestMatchers(PathRequest.
                toStaticResources().atCommonLocations()));
    }

}
