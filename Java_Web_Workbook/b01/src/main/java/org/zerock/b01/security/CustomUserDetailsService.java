package org.zerock.b01.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerock.b01.domain.Member;
import org.zerock.b01.repository.MemberRepository;
import org.zerock.b01.security.dto.MemberSecurityDTO;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

//    private PasswordEncoder passwordEncoder;

    private final MemberRepository memberRepository;

//    public  CustomUserDetailsService(){
//        this.passwordEncoder = new BCryptPasswordEncoder();
//    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//        log.info("loadUserByUsername: "+ username);
//
//        // 사용자 정보를 기반으로 UserDetails 객체 생성
//        UserDetails userDetails = User.builder()
//                .username("user1") // 사용자 이름
////                .password("{noop}password") // 사용자 비밀번호, {noop}은 패스워드 인코딩을 사용하지 않음을 의미
//                .password(passwordEncoder.encode("1111"))//패스워드 인코딩 필요
////                .roles("ROLE_USER") // 사용자 권한, 여기서는 "USER" 권한을 부여
//                .roles("USER")
//                .build();
//
//        return userDetails;
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("loadUserByUsername: "+ username);

        Optional<Member> result = memberRepository.getWithRoles(username);

        if(result.isEmpty()){   //해당 아이디를 가진 사용자가 없다면
            throw new UsernameNotFoundException("username not found...");
        }

        Member member = result.get();

        MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                member.getMid(),
                member.getMpw(),
                member.getEmail(),
                member.isDel(),
                false,
                member.getRoleSet().stream().map(memberRole -> new SimpleGrantedAuthority(
                        "ROLE_"+memberRole.name())).collect(Collectors.toList())
        );

        log.info("memberSecurityDTO");
        log.info(memberSecurityDTO);

        return memberSecurityDTO;

    }
}
