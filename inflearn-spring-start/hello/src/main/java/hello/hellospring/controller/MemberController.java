package hello.hellospring.controller;

import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MemberController {
    private  MemberService memberService;

//    @Autowired        //방법3 setter주입
//    public void setMemberService(MemberService memberService) {
//        this.memberService = memberService;
//    }

//    @Autowired private  MemberService memberService;  //방법2 필드주입-중간에 바꿀 수 없음


    @Autowired  //자동으로 스프링 빈 등록되어있는 MemberService객체를 찾아서 연결해 줌 -> 의존관계 주입 DI    //방법1 생성자 주입 - 가장 좋음
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
}
