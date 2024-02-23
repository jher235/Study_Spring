package org.zerock.springex.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.N;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.Arrays;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {

    @Builder.Default
    @Min(value = 1)
    @Positive
    private int page=1;

    @Builder.Default
    @Min(value = 1)
    @Positive
    @Max(value = 100)
    private int size=10;

    private String link;

    //필터링을 위한 변수

    private String[] types;
    private String keyword;
    private boolean finished;
    private LocalDate from;
    private LocalDate to;


    public int getSkip(){
        return (page-1) *10;
    }

//    public String getLink(){
//        if(link==null){
//            StringBuilder builder = new StringBuilder();
//            builder.append("page="+this.page);
//            builder.append("&size="+this.size);
//            link = builder.toString();
//        }
//        return link;
//    }

    public String getLink(){
       StringBuilder builder = new StringBuilder();
        builder.append("page="+this.page);
        builder.append("&size="+this.size);

        if(finished){
            builder.append("&finished=on");
        }

        if(types!=null && types.length>0){
            for(int i=0;i<types.length;i++){
                builder.append("&types="+types[i]);
            }
        }

        if(keyword!=null){
            try{
                //한글이 가능하도록 인코딩함
                builder.append("&keyword="+ URLEncoder.encode(keyword,"UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        if (from != null){
            builder.append("&from="+from.toString());
        }

        if(to!=null){
            builder.append("&to="+to.toString());
        }

        return builder().toString();

    }


    public boolean checkType(String type){
        if(types == null || types.length==0){
            return false;
        }
        return Arrays.stream(types).anyMatch(type::equals);
    }
}
