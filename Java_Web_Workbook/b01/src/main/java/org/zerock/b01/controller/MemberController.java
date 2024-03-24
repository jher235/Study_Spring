package org.zerock.b01.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.b01.dto.MemberJoinDTO;
import org.zerock.b01.service.MemberService;

@Controller
@RequestMapping("/member")
@Log4j2
@RequiredArgsConstructor
public class MemberController {

    //의존성 주입
    private final MemberService memberService;

    @GetMapping("/login")
    public void loginGet(String error, String logout){
        log.info("Login Get...............");
        log.info("logout: "+ logout);

        if(logout != null) log.info("user logout............");
    }

    @GetMapping("/join")
    public void joinGet(){

        log.info("join get...");
    }

    @PostMapping("/join")
    public String joinPost(MemberJoinDTO memberJoinDTO, RedirectAttributes redirectAttributes){

        log.info("join post...");
        log.info(memberJoinDTO);

        try {
            memberService.join(memberJoinDTO);
        } catch (MemberService.MidExistException e) {

            redirectAttributes.addFlashAttribute("error", "mid");
            return "redirect:/member/join";
        }

        redirectAttributes.addFlashAttribute("result", "success");

        return "redirect:/member/login"; //회원가입 성공 후 로그인 하도록 이동

    }



}
