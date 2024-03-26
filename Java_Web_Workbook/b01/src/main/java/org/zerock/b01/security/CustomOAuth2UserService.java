package org.zerock.b01.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.zerock.b01.domain.Member;
import org.zerock.b01.domain.MemberRole;
import org.zerock.b01.repository.MemberRepository;
import org.zerock.b01.security.dto.MemberSecurityDTO;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    //카카오 서비스와 연동된 결과를 OAuth2UserRequest로 받아옴
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("userRequest....");
        log.info(userRequest);

        log.info("oauth2 user............................");

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName();

        log.info("Name: "+clientName);
        OAuth2User oAuth2User = super.loadUser(userRequest);    //기본 사용자 정보 로드
        Map<String, Object> paramMap = oAuth2User.getAttributes(); //사용자 속성 가져옴

        String email = null;

        switch (clientName){
            case "kakao":
                email = getKakaoEmail(paramMap);
                break;
        }

        log.info("==========================");
        log.info(email);
        log.info("==========================");

//        paramMap.forEach((k,v)->{
//            log.info("---------------------------");
//            log.info(k+": "+v);
//        });

        return oAuth2User;
    }

    private MemberSecurityDTO generateDTO(String email, Map<String, Object> params){

        Optional<Member> result =  memberRepository.findByEmail(email);

        //데이터베이스에 해당 이메일 사용자가 없을 경우
        if(result.isEmpty()){
            //회원 추가 -- mid는 이메일, mpw는 1111
            Member member = Member.builder()
                    .mid(email)
                    .mpw(passwordEncoder.encode("1111"))
                    .email(email)
                    .social(true)
                    .build();
            member.addRole(MemberRole.USER);
            memberRepository.save(member);

            //SimpleGrantedAuthority는 Spring Security에서 권한(Authority)을 표현하는 가장 기본적인 구현체
            MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(email, "1111", email,
                    false,true, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));

            memberSecurityDTO.setProps(params);

            return memberSecurityDTO;
        }else{//데이터베이스에 해당 이메일 사용자가 있는 경우

            //Optional 객체 내부에 저장된 Member 객체를 반환
            Member member = result.get();
            MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                    member.getMid(),
                    member.getMpw(),
                    member.getEmail(),
                    member.isDel(),
                    member.isSocial(),
                    member.getRoleSet().stream().map(memberRole -> new SimpleGrantedAuthority(
                            "ROLE_"+memberRole.name())).collect(Collectors.toList()) //RoleSet에 맞춰 권한 생성
            );

            return memberSecurityDTO;

        }

    }

    private String getKakaoEmail(Map<String, Object> paramMap){

        log.info("KAKAO---------------------------");

        Object value = paramMap.get("kakao_account");

        log.info(value);

        LinkedHashMap accountMap = (LinkedHashMap) value;

        String email = (String) accountMap.get("email");

        log.info("email....."+ email);

        return email;
    }

}
