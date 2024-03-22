package org.zerock.b01.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
//@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private PasswordEncoder passwordEncoder;

    public  CustomUserDetailsService(){
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("loadUserByUsername: "+ username);

        // 사용자 정보를 기반으로 UserDetails 객체 생성
        UserDetails userDetails = User.builder()
                .username("user1") // 사용자 이름
//                .password("{noop}password") // 사용자 비밀번호, {noop}은 패스워드 인코딩을 사용하지 않음을 의미
                .password(passwordEncoder.encode("1111"))//패스워드 인코딩 필요
//                .roles("ROLE_USER") // 사용자 권한, 여기서는 "USER" 권한을 부여
                .roles("USER")
                .build();

        return userDetails;
    }
}
