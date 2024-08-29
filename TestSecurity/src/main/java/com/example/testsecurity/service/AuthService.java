package com.example.testsecurity.service;

import com.example.testsecurity.dto.JoinDto;
import com.example.testsecurity.entity.User;
import com.example.testsecurity.entity.vo.Role;
import com.example.testsecurity.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void joinProcess(JoinDto joinDto){

        //db에 동일한 username을 지닌 회원 여부 검증.
        if(userRepository.existsByUsername(joinDto.getUsername())){
            return;
        }

        User user = new User();

        user.setUsername(joinDto.getUsername());
        user.setPassword(passwordEncoder.encode(joinDto.getPassword())); //해쉬화 후 저장.
        user.setRole(Role.USER);

        userRepository.save(user);
    }
}
