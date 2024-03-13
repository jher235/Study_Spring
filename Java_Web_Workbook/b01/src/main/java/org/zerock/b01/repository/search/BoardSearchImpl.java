package org.zerock.b01.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.QBoard;
import org.zerock.b01.domain.QReply;
import org.zerock.b01.dto.BoardListAllDTO;
import org.zerock.b01.dto.BoardListReplyCountDTO;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
//구현 클래스는 반드시 '인터페이스 이름 + Impl'로 작성한다. 파일의 이름이 틀린 경우 제대로 동작하지 않으니 주의해야 함
public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {

    public BoardSearchImpl() {
        super(Board.class);
    }

    @Override
    public Page<Board> search1(Pageable pageable) {
        QBoard qBoard = QBoard.board;   //Q도메인 객체
        JPQLQuery<Board> query = from(qBoard);  //select... from board
        query.where(qBoard.title.contains("1")); //where title like...

        //paging
        this.getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch();
        long count = query.fetchCount();

        return null;
    }

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {
        QBoard qBoard = QBoard.board;
        JPQLQuery<Board> query = from(qBoard);
        log.info("qBoard : " + qBoard);

        log.info("query : " +query);

        //검색조건 & 키워드가 있다면
        if((types!=null && types.length >0) && keyword != null){
            BooleanBuilder booleanBuilder =  new BooleanBuilder();

            for (String type :types){

                switch (type){
                    case "t":
                        booleanBuilder.or(qBoard.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(qBoard.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(qBoard.writer.contains(keyword));
                        break;
                }

            }//end for
            query.where(booleanBuilder);
        }//end if

        //bno>0
        query.where(qBoard.bno.gt(0L));

        log.info("query.where(qBoard.bno.gt(0L)); : " +query.where(qBoard.bno.gt(0L)));

        //paging
        this.getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch();

        log.info("list : " +list);

        long count = query.fetchCount();

        log.info("count : " +count);

        return new PageImpl<>(list,pageable,count);

    }

    @Override
    public Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable) {

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        JPQLQuery<Board> query = from(board);

//        이 코드는 Board와 Reply를 결합하는데, Reply의 board 필드가 Board 엔티티와 같은 Reply만 결합하라는 의미입니다.
        query.leftJoin(reply).on(reply.board.eq(board));    //left join을 사용할 때는 on()을 이용해 조인 조건을 지정한다.

        query.groupBy(board);     //조인 처리 후에 게시물당 처리가 필요하므로 groupBy()를 적용


        if((types != null && types.length>0)&& keyword !=null){
            BooleanBuilder booleanBuilder = new BooleanBuilder();

            for(String type: types){

                switch (type){
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;

                }
            }
            query.where(booleanBuilder);
        }

        //bno>0
        query.where(board.bno.gt(0L));



        JPQLQuery<BoardListReplyCountDTO> dtojpqlQuery = query.select(Projections.
                bean(BoardListReplyCountDTO.class,
                        board.bno,
                        board.title,
                        board.writer,
                        board.regDate,
                        reply.count().as("replyCount")
                        ));

        this.getQuerydsl().applyPagination(pageable, dtojpqlQuery);

        List<BoardListReplyCountDTO> dtoList = dtojpqlQuery.fetch();

        long count = dtojpqlQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, count);
    }


//    검색 유형(types), 검색 키워드(keyword), 페이지네이션 정보(pageable)를 기반으로 게시판 목록을 조회하고,
//    Page<BoardListReplyCountDTO> 타입으로 결과를 반환함.
//    여기서 BoardListReplyCountDTO는 게시글 정보와 해당 게시글의 댓글 수를 담는 DTO를 의미
    @Override
    public Page<BoardListAllDTO> searchWithAll(String[] types, String keyword, Pageable pageable) {
//        Querydsl 쿼리에서 사용될 엔티티의 메타 모델
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

//        from(board)를 통해 Board 엔티티를 조회하는 JPQL 쿼리를 생성
        JPQLQuery<Board> boardJPQLQuery = from(board);

//        생성된 쿼리에 대해 Reply 엔티티와의 왼쪽 조인을 추가
//        왼쪽에 있는 테이블(boardJPQLQuery가 대표하는 Board 엔티티)의 모든 레코드와
//        오른쪽 테이블(여기서는 Reply 엔티티)에서 조건에 맞는 레코드를 함께 가져오는 조인 방식
//        .on은 조인을 수행할 때 사용할 조건을 지정. 그 뒤에가 조건임
        boardJPQLQuery.leftJoin(reply).on(reply.board.eq(board));  //left join

        boardJPQLQuery.groupBy(board);

//        페이지네이션 정보(pageable)를 쿼리에 적용함.
        getQuerydsl().applyPagination(pageable, boardJPQLQuery);    //paging

////        결과를  List<Board> 형태로 저장 //DTO를 BoardListAllDTO로 바꾸며 형태가 바뀜
//        List<Board> boardList = boardJPQLQuery.fetch();

//        댓글 수를 튜플값으로 저장
        JPQLQuery<Tuple> tupleJPQLQuery = boardJPQLQuery.select(board, reply.countDistinct());

        List<Tuple> tupleList = tupleJPQLQuery.fetch();

//        지금 Board에는 reply 정보가 없고 reply에만 board정보가 있는 구조 형태이므로 tuple을 로드해서 게시물의 나머지 정보를 가져옴
        List<BoardListAllDTO> dtoList = tupleList.stream().map(tuple -> {

            Board board1 = (Board) tuple.get(board);
            long replyCount = tuple.get(1,Long.class);

            BoardListAllDTO dto = BoardListAllDTO.builder()
                    .bno(board1.getBno())
                    .title(board1.getTitle())
                    .writer(board1.getWriter())
                    .regDate(board1.getRegDate())
                    .replyCount(replyCount)
                    .build();

            //boardImage를 BoardImageDTO 처리할 부분

            return dto;

        }).collect(Collectors.toList());


        long totalCount = boardJPQLQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, totalCount);
    }
}
