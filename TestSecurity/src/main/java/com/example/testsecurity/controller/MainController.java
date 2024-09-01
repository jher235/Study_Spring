package com.example.testsecurity.controller;

import com.example.testsecurity.dto.response.UserData;
import com.example.testsecurity.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;
import java.util.Iterator;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MainController {

    private final AuthService authService;

    @GetMapping("/")
    public String mainP(Model model){
        UserData userData = authService.getUserData();

        // Model 객체를 사용하여 컨트롤러에서 처리한 데이터를 뷰로 전달
        model.addAttribute("id", userData.getUserName());
        model.addAttribute("role", userData.getRole());
        return "main";
    }
}
