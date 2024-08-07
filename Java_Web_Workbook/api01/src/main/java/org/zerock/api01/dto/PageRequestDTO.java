package org.zerock.api01.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {
    private static int PAGE = 1;
    private static int SIZE = 10;

    @Builder.Default
    private int page = PAGE;
    @Builder.Default
    private int size = SIZE;
    private String type; // 검색의 종류 t,c, w, tc, tw, twc
    private String keyword;

    //추가된 내용들
    private LocalDate from;
    private LocalDate to;
    private Boolean completed;

    public String[] getTypes(){
        if(type == null || type.isEmpty()){
            return null;
        }
        return type.split("");
    }
    public Pageable getPageable(String ...props){
        return PageRequest.of(this.page -1, this.size, Sort.by(props).descending());
    }

    private String link;

    public String getLink(){

        if(link == null){
            StringBuilder builder = new StringBuilder();
            builder.append("pages=" + this.page);
            builder.append("&size=" + this.size);

            if(type != null && type.length() >0){
                builder.append("&type=" + this.type);
            }

            if(keyword != null){
                builder.append("&keyword="+ URLEncoder.encode(keyword, StandardCharsets.UTF_8));
            }
            link = builder.toString();
        }
        return link;
    }
}
