package com.example.testsecurity.service;

import com.example.testsecurity.dto.CustomUserDetails;
import com.example.testsecurity.entity.User;
import com.example.testsecurity.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // 시큐리티는 로그인을 컨트롤러에서 처리하지 않고 필터에서 처리함. 따로 컨트롤러단을 작성해줄 필요없이 자동으로 이곳에서 처리한다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userData = userRepository.findByUsername(username); //원래는 Optional, 예외처리를 해주는 게 좋음
        if(userData != null){
            return new CustomUserDetails(userData);
        }

        return null;
    }
}
