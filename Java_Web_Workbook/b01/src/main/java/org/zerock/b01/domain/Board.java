package org.zerock.b01.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Board extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    @Column(length = 500, nullable = false)//칼럼의 길이, null허용 여부
    private String title;

    @Column(length = 2000, nullable = false)
    private String content;

    @Column(length = 50, nullable = false)
    private String writer;

    @OneToMany(mappedBy = "board",  //BoardImage의 board변수
            cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private Set<BoardImage> imageSet = new HashSet<>();

    public void change(String title, String content){
        this.title = title;
        this.content = content;
    }

    public void addImage(String uuid, String fileName){ //BoardImage 객체들을 관리

        BoardImage boardImage = BoardImage.builder()
                .uuid(uuid)
                .fileName(fileName)
                .board(this)
                .ord(imageSet.size())
                .build();

        imageSet.add(boardImage);
    }

    public void clearImages(){  //BoardImage 객체들을 관리
        imageSet.forEach(boardImage -> boardImage.changeBoard(null));

        this.imageSet.clear();
    }


}
