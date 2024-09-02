package com.example.testsecurity.entity;

import com.example.testsecurity.entity.vo.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@EqualsAndHashCode //다중 로그인 설정을 할 때 정상적인 비교를 통해 중복 유저를 찾기 위해 이게 필요했다. UserDetails구현체와 User엔티티 이 두 클래스에 적용.
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //int값이 자동으로 증가하는 방식
    private int id;

    private String username;
    private String password;

    private Role role;
}
