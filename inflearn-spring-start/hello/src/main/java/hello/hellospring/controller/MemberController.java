package hello.hellospring.controller;

import hello.hellospring.domain.Member;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

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


    @GetMapping("/members/new")
    public String createForm(){
        return "members/createMemberForm.html";
    }

    @PostMapping("/members/new")
    public String create(MemberForm form){
        Member member = new Member();
        member.setName(form.getName());

        memberService.join(member);

        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model){
        List<Member> members = memberService.findMembers();
        model.addAttribute("members",members);
        return "members/memberList";
    }
//jdbc:h2:mem:test
}
