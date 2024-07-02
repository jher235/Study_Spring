package org.zerock.api01.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodoDTO {
    private Long tno;
    private String title;

    //Jackson 라이브러리를 사용하여 JSON 직렬화 및 역직렬화할 때 날짜나 시간 형식을 지정하기 위해 사용
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate dueDate;

    private String writer;
    private boolean complete;
}
