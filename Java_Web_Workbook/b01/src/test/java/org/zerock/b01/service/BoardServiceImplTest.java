package org.zerock.b01.service;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.b01.dto.BoardDTO;
import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class BoardServiceImplTest {

    @Autowired
    private BoardService boardService;

    @Test
    public void testRegister(){

        log.info(boardService.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()
                .title("Sample title....")
                .content("Sample content....")
                .writer("user00")
                .build();

        Long bno =  boardService.register(boardDTO);

        log.info(bno);


    }

    @Test
    public void testModify(){

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(102L)
                .title("modify test...")
                .content("modify test....")
                .build();

        boardService.modify(boardDTO);
    }

    @Test
    public void testList(){

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("tcw")
                .keyword("1")
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);

        log.info(responseDTO);
    }

    @Test
    public void testRegisterWithImages(){

        log.info(boardService.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()
                .title("File...Sample Title...")
                .content("Sample Content...")
                .writer("user00")
                .build();

        boardDTO.setFileNames(
                Arrays.asList(
                        UUID.randomUUID() + "_aaa.jpg",
                        UUID.randomUUID()+"_bbb.jpg",
                        UUID.randomUUID()+"_ccc.jpg"
                )
        );

        Long bno = boardService.register(boardDTO);

        log.info("bno: "+bno);
    }

    @Test
    public void testReadAll(){
        Long bno = 101L;

        BoardDTO boardDTO = boardService.readOne(bno);

        log.info(boardDTO);

        for (String fileName : boardDTO.getFileNames()){
            log.info(fileName);
        }
    }

}