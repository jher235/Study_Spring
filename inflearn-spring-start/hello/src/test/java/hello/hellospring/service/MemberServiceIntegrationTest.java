package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest     //스프링 컨테이너와 테스트를 함께 실행함
@Transactional    //이걸 테스트에 달면 테스트 실행할 때 트랜젝션을 먼저 실행하고 db에 데이터를 insert하고 테스트를 돌린다음에 테스트가 끝나면 db를 롤백하여 원래 상태로 만들어준다.(테스트 하나하나마다)
class MemberServiceIntegrationTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository ;

    @Test
    void 회원가입() {
        //given
        Member member = new Member();
        member.setName("hello");

        //when
        Long saveId = memberService.join(member);

        //then
        Member findMember = memberService.findOne(saveId).get();
        assertThat(member.getName()).isEqualTo(findMember.getName());

        Optional<Member> fm = memberRepository.findByNameAndId(member.getName(),member.getId());
        assertThat(member.getId()).isEqualTo(fm.get().getId());
    }

    @Test
    public void 중복_회원_예외(){
        //given
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");


        //when
        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class, ()->memberService.join(member2));

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");


    }


}