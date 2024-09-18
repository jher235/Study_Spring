package com.example.testsecurity.controller;

import com.example.testsecurity.dto.request.JoinDto;
import com.example.testsecurity.dto.response.UserData;
import com.example.testsecurity.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.Session;
import org.apache.catalina.session.StandardSession;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.WebSessionServerLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Objects;

@Controller
@AllArgsConstructor
@Log4j2
public class AuthController {

    private final AuthService authService;
    private final SessionRegistry sessionRegistry;

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

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            log.info(request.getSession(false).getId());
            HttpSession session = request.getSession(false); //false는 기존 세션이 없는 경우 새로 생성하지 않고 null을 반환

            //loginform 방식에서는 로그아웃 안되는 문제를 sessionRegistry 세션에서 직접 삭제해서 해결할 수 있었다.
            sessionRegistry.removeSessionInformation(request.getSession(false).getId());
            //SecurityContextLogoutHandler().logout에서 request의 session에 invalidate를 해줌. 근데 sessionRegistry에서 관리하는 세션에는 적용이 안되는 것 같다.
            new SecurityContextLogoutHandler().logout(request, response, authentication);

        }

        // 로그아웃 후 SecurityContext 상태 확인
        SecurityContext context = SecurityContextHolder.getContext();
        log.info("SecurityContext after logout: " + context.getAuthentication());


        //현재 세션의 유저 목록 - 세션 별로 관리하는데 유저 목록???
        List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
        for (Object o : allPrincipals){
            log.info(o.toString());
        }

        return "redirect:/";
    }

}
