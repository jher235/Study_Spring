package hello.hellospring.controller;

import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MemberController {
    private final MemberService memberService;

    @Autowired  //자동으로 스프링 빈 등록되어있는 MemberService객체를 찾아서 연결해 줌 -> 의존관계 주입 DI
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
}
