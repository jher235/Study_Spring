package com.example.testsecurity.dto;

import com.example.testsecurity.entity.User;
import com.example.testsecurity.entity.vo.Role;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Setter
@Getter
@EqualsAndHashCode //다중 로그인 설정을 할 때 정상적인 비교를 통해 중복 유저를 찾기 위해 이게 필요했다. UserDetails구현체와 User엔티티 이 두 클래스에 적용.
public class CustomUserDetails implements UserDetails {
    private User user;

    public CustomUserDetails(User user){
        this.user = user;
    }

    //사용자의 roles 값에 따라 사용자의 권한을 반환하는 메서드
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //사용자의 권한 정보(GrantedAuthority 객체)를 담기 위한 리스트 생성. 유저 한명이 여러 개의 권한을 갖는 것을 가정으로 Collection을 사용함.
        Collection<GrantedAuthority> collection = new ArrayList<>();

        //지금은 유저가 한개의 권한만 가진다고 생각하고 작성한 것이고 여러개의 권한이라면 유저가 role Collection을 지니고 순회해야 할 것.
        //GrantedAuthority인터페이스가 구현해야하는 getAuthority()가 있음.
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole().name();
            }
        });

        return collection;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }
}
