package org.zerock.api01.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PageResponseDTO<E> {
    private int page;
    private int size;
    private int total;

    private int start;
    private int end;

    private boolean prev;
    private boolean next;

    private List<E> dtoList;

    @Builder(builderMethodName = "withAll")// builder() 대신 withAll()이라는 이름의 빌더 메서드가 생성됨
    public PageResponseDTO(PageRequestDTO pageRequestDTO, List<E> dtoList, int total){
        if (total<=0){
            return;
        }
        this.page = pageRequestDTO.getPage();
        this.size = pageRequestDTO.getSize();

        this.total = total;
        this.dtoList = dtoList;

        this.end = (int) (Math.ceil(this.page/10) *10);
        this.start = this.end -9;
        int last = (int)(Math.ceil(total/(double)size));

        this.end = end > last ? last : end;
        this.prev = this.start > 1;
        this.next = total > this.end * this.size;

    }
}
