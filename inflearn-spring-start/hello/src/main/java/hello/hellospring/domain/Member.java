package hello.hellospring.domain;

import jakarta.persistence.*;

@Entity //jpa가 관리하는 엔티티임
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(name = "username")    // @Column(name = "username")는 name 필드를 데이터베이스 테이블의 username 컬럼에 매핑한다는 것을 의미함
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
