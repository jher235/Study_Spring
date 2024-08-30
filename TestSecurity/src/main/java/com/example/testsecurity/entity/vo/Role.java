package com.example.testsecurity.entity.vo;

public enum Role {
    ROLE_USER, ROLE_ADMIN;  //스프링 시큐리티에서는 'ROLE_' 접두사를 붙여줘야 함.

    public String getRoleName(){
        return this.name().substring(5);
    }
}
