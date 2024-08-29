package com.example.testsecurity.entity;

import com.example.testsecurity.entity.vo.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //int값이 자동으로 증가하는 방식
    private int id;

    private String username;
    private String password;

    private Role role;
}
