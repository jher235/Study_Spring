package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//JpaRepository를 통하여 기본적인 clud, 단순조회들이 제공됨
public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository {

    @Override
    Optional<Member> findByName(String name);   //규칙이 있음 findBy다음에 오는 것을 통해 JPQL을 짜줌 select m from Member m where m.name = ?


    @Override
    Optional<Member> findByNameAndId(String name, Long id);
}
