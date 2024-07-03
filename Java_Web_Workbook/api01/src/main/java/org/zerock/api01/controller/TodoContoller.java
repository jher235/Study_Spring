package org.zerock.api01.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.zerock.api01.domain.Todo;
import org.zerock.api01.dto.PageRequestDTO;
import org.zerock.api01.dto.PageResponseDTO;
import org.zerock.api01.dto.TodoDTO;
import org.zerock.api01.service.TodoService;

import java.util.Map;

@RestController
@RequestMapping("/api/todo")
@Log4j2
@RequiredArgsConstructor
public class TodoContoller {
    private final TodoService todoService;

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)// Content-Type이 application/json인 POST 요청만 처리하도록 설정
    public Map<String, Long> register(@RequestBody TodoDTO todoDTO){
        log.info(todoDTO);

        Long tno = todoService.register(todoDTO);

        return Map.of("tno", tno);
    }

    @GetMapping("/{tno}")
    public TodoDTO read(@PathVariable("tno") Long tno){
        return todoService.read(tno);
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO){
        return todoService.list(pageRequestDTO);
    }

    @DeleteMapping("/{tno}")
    public Map<String, String> delete(@PathVariable Long tno){
        todoService.remove(tno);
        return Map.of("result", "success");
    }

    @PatchMapping(value = "/{tno}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String > modify(@PathVariable Long tno, @RequestBody TodoDTO todoDTO){
        todoDTO.setTno(tno); // 잘못된 tno가 발생하지 않도록 함.

        todoService.modify(todoDTO);
        return Map.of("result", "success");
    }

}
