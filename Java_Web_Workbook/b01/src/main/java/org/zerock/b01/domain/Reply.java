package org.zerock.b01.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Reply", indexes = {
        @Index(name = "idx_reply_board_bno", columnList = "board_bno")
})
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
//@ToString(exclude = "board")    //toString을 할 때 참조하는 객체를 사용하지 않도록 exclude 속성값 지정
public class Reply extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //JPA에서 엔티티의 기본 키(Primary Key) 값을 자동으로 생성
    private Long rno;

    @ManyToOne(fetch = FetchType.LAZY)  //연관 관계를 나타낼 때는 반드시 fetch속성은 LAZY로 지정
//    @JoinColumn(name = "bno")   //특정 엔티티를 참조하는 외래 키를 매핑할 수 있습니다.
    private Board board;

    private String replyText;

    private String replyer;

    public void changeText(String text){
        this.replyText = text;
    }

}
