package org.zerock.b01.repository;


import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.BoardImage;
import org.zerock.b01.dto.BoardListReplyCountDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class BoardRepositoryTests {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;    //게시글 삭제 시 댓글 삭제를 위함

    @Test
    public void testInsert(){
        IntStream.rangeClosed(1,100).forEach(i->{
            Board board = Board.builder()
                    .title("title...." + i)
                    .content("content..." + i)
                    .writer("user" + (i%10))
                    .build();

            Board result = boardRepository.save(board);
            log.info("BNO : "+ result.getBno());
        });
    }

    @Test
    public void testSelect(){
        Long bno= 100L;
        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElseThrow();
        log.info(board);
    }

    @Test
    public void testUpdate(){
        long bno = 100L;

        Optional<Board> result = boardRepository.findById(bno);

        Board board = result.orElseThrow();

        board.change("update... title 100", "update content... 100");

        boardRepository.save(board);

    }

    @Test
    public void testDelete(){
        Long bno = 3L;

        boardRepository.deleteById(bno);
    }

    @Test
    public void testPaging(){
        //1 page order by bno desc
        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());
        Page<Board> result = boardRepository.findAll(pageable);

        log.info("total count : " + result.getTotalElements());
        log.info("total pages : " + result.getTotalPages());
        log.info("page number : " + result.getNumber());
        log.info("page size : " + result.getSize());

        List<Board> todoList = result.getContent();

        todoList.forEach(b -> log.info(b));

    }

    //Q도메인을 이용한 쿼리 작성, 테스트
    @Test
    public void testSearch1(){
        //2 page order by tno desc
        Pageable pageable = PageRequest.of(1,20,Sort.by("bno").descending());

        boardRepository.search1(pageable);
    }

    @Test
    public void testSearchAll(){
        String[] types = {"t","c","w"};

        String keyword = "1";
        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());

        Page<Board> result = boardRepository.searchAll(types,keyword, pageable);

    }

    @Test
    public void testSearchAll2(){
        String[] types = {"t","c","w"};

        String keyword = "1";
        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());

        Page<Board> result = boardRepository.searchAll(types,keyword, pageable);

        //total pages
        log.info(result.getTotalPages());

        //page size
        log.info(result.getSize());

        //pageNumber
        log.info(result.getNumber());

        //prev next
        log.info(result.hasPrevious() + ": "+result. hasNext());

        result.getContent().forEach(board -> log.info(board));

    }

    @Test
    public void testSearchReplyCount(){

        String[] types = {"t","c","w"};

        String keyword = "1";

        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());

        Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types, keyword,pageable);

        //total pages
        log.info(result.getTotalPages());

        //page size
        log.info(result.getSize());

        //pageNumber
        log.info(result.getNumber());

        log.info(result.hasPrevious() + " : " + result.hasNext());

        result.getContent().forEach(b->log.info(b));
    }

    @Test
    public void testInsertWithImages(){

        Board board = Board.builder()
                .title("Image test")
                .content("첨부파일 테스트")
                .writer("tester")
                .build();

        for(int i=0;i<3;i++){
            board.addImage(UUID.randomUUID().toString(), "file"+i+".jpg");
        }

        boardRepository.save(board);

    }

    @Test
    public void testReadWithImages(){

        //반드시 존재하는 bno를 사용해야함.
        Optional<Board> result = boardRepository.findByIdWithImages(1L);

        Board board = result.orElseThrow();

        log.info(board);
        log.info("----------------");
//        log.info(board.getImageSet());
        for(BoardImage boardImage : board.getImageSet()){
            log.info(boardImage);
        }

    }

    @Transactional
    @Commit //작업이 성공적으로 완료되면 변경사항을 커밋(저장)하도록 설정
    @Test
    public void testModifyImages(){
        Optional<Board> result = boardRepository.findByIdWithImages(1L);

        Board board = result.orElseThrow();

//        기존 첨부 파일 삭제
        board.clearImages();

        //새로운 첨부파일 추가
        for (int i=0;i<2;i++){
            board.addImage(UUID.randomUUID().toString(), "updatefile"+i+".jpg");
        }

        boardRepository.save(board);

    }

    @Test
    @Transactional
    @Commit
    public void testRemoveAll(){

        Long bno = 1L;

        replyRepository.deleteByBoard_Bno(bno);

        boardRepository.deleteById(bno);

    }



}

