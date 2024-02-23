package org.zerock.springex.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.N;

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

    public int getSkip(){
        return (page-1) *10;
    }

    public String getLink(){
        if(link==null){
            StringBuilder builder = new StringBuilder();
            builder.append("page="+this.page);
            builder.append("&size="+this.size);
            link = builder.toString();
        }
        return link;
    }
}
