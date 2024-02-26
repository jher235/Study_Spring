package org.zerock.b01.reporsitory.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.QBoard;

import java.util.List;

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
}
