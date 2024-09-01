package com.example.testsecurity.dto.response;

import com.example.testsecurity.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class UserData {
    private String userName;
    private String role;

    public static UserData userData(String userName, String role){
        return new UserData(userName, role);
    }
}
