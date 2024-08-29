package com.example.testsecurity.controller;

import com.example.testsecurity.dto.JoinDto;
import com.example.testsecurity.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/join")
    public String joinPage(){
        return "join";
    }

    @PostMapping("/joinProc")
    public String joinProcess(JoinDto joinDto){

        authService.joinProcess(joinDto);
        return "redirect:/login";
    }

}
