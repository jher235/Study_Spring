package com.example.testsecurity.service;

import com.example.testsecurity.dto.request.JoinDto;
import com.example.testsecurity.dto.response.UserData;
import com.example.testsecurity.entity.User;
import com.example.testsecurity.entity.vo.Role;
import com.example.testsecurity.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Iterator;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    public void joinProcess(JoinDto joinDto){

        //db에 동일한 username을 지닌 회원 여부 검증.
        if(userRepository.existsByUsername(joinDto.getUsername())){
            return; //원래는 예외처리가 필요
        }

        User user = new User();

        user.setUsername(joinDto.getUsername());
        user.setPassword(passwordEncoder.encode(joinDto.getPassword())); //해쉬화 후 저장.
        user.setRole(Role.ROLE_USER);

        userRepository.save(user);
    }

    public UserData getUserData(){
        //만약 닉네임 등의 다른 정보를 보여주고 싶다면 getAuthentication().getPrincipal()로 객체를 찾아와서 get으로 원하는 필드를 사용한다.
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        Collection<? extends GrantedAuthority> roleCollection = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = roleCollection.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        return UserData.userData(userName, role);
    }
}
