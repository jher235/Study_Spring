package org.zerock.api01.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.expression.spel.ast.Projection;
import org.zerock.api01.domain.QTodo;
import org.zerock.api01.domain.Todo;
import org.zerock.api01.dto.PageRequestDTO;
import org.zerock.api01.dto.TodoDTO;

import java.util.List;

public class TodoSearchImpl extends QuerydslRepositorySupport implements TodoSearch {
    public TodoSearchImpl() {
        super(Todo.class);
    }

    @Override
    public Page<TodoDTO> list(PageRequestDTO pageRequestDTO) {
        QTodo todo = QTodo.todo;

        JPQLQuery<Todo> query = from(todo);

        if(pageRequestDTO.getFrom() != null && pageRequestDTO.getTo() != null){
            BooleanBuilder fromToBuilder = new BooleanBuilder(); //조건을 담을 수 있는 BooleanBuilder, 아래 2줄이 조건을 생성
            fromToBuilder.and(todo.dueDate.goe(pageRequestDTO.getFrom())); //todo.dueDate가 pageRequestDTO.getFrom() 보다 작거나 같을 때
            fromToBuilder.and(todo.dueDate.loe(pageRequestDTO.getTo()));//todo.dueDate가 pageRequestDTO.getTo() 보다 크거나 같을 때
            query.where(fromToBuilder); //조건을 적용
        }

        if (pageRequestDTO.getCompleted() != null){
            query.where(todo.complete.eq(pageRequestDTO.getCompleted()));
        }

        if (pageRequestDTO.getKeyword() != null){
            query.where(todo.title.contains(pageRequestDTO.getKeyword()));
        }

        //QuerydslRepositorySupport 클래스에서 제공하는 메서드로 Querydsl 객체를 반환
        this.getQuerydsl().applyPagination(pageRequestDTO.getPageable("tno"), query); //Pageable 객체와 query를 인자로 받아 페이징 처리를 쿼리에 적용, 정렬 기준은 "tno"

        //query.select로 JPQLQuery<Todo> 객체에서 JPQLQuery<TodoDTO> 객체로 변환
        JPQLQuery<TodoDTO> dtojpqlQuery = query.select(Projections.bean(TodoDTO.class, //TodoDTO 클래스 프로젝션을 생성 -> 아래 필드를 선택하여 TodoDTO 객체 생성
                todo.tno,
                todo.title,
                todo.dueDate,
                todo.complete,
                todo.writer
        ));

        List<TodoDTO> list = dtojpqlQuery.fetch(); //쿼리를 실행하여 결과를 TodoDTO리스트로 반환

        long count = dtojpqlQuery.fetchCount(); //쿼리의 결과 개수 저장

        //PageImpl 객체는 Spring Data에서 제공하는 Page 인터페이스의 구현체
        return new PageImpl<>(list,pageRequestDTO.getPageable("tno"),count); //TodoDTO 리스트와 페이징 정보를 포함한 Pageable 객체, 결과의 개수를 포함한 PageImpl객체 반환
    }
}
