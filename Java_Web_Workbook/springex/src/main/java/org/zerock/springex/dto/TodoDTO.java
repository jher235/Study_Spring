package org.zerock.springex.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDate;

@ToString
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodoDTO {

    private Long tno;

    @NotEmpty
    private String title;

    @Future
    private LocalDate dueDate;

    private boolean finished;

    @NotEmpty
    private String writer; //새로 추가

}
