package org.zerock.b01.repository;

import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.b01.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    @EntityGraph(attributePaths = "roleSet")
    @Query("select m from Member m where m.mid = :mid and m.social = false")
    Optional<Member> getWithRoles(String mid);

    @EntityGraph(attributePaths = "roleSet")//지연로딩이지만 필요시 바로 호출
    Optional<Member> findByEmail(String email);

    @Modifying  //쿼리가 데이터를 수정하는 작업을 처리할 때 붙여줘야 함 - 기본적으로 @Query는 조회이기 때문
    @Transactional  //트랜잭션의 범위 안에서 성공되거나 실패시 모두 롤백
    @Query("update Member m set m.mpw =:mpw where m.mid = :mid")
    void updatePassword(@Param("mpw") String password, @Param("mid") String mid);

}
