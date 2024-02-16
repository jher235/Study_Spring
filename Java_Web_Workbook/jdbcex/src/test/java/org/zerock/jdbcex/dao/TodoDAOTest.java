package org.zerock.jdbcex.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zerock.jdbcex.domain.TodoVO;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class TodoDAOTest {
    private TodoDAO todoDAO;

    @BeforeEach
    public void ready(){
        todoDAO = new TodoDAO();
    }

    @Test
    public void testTime() throws Exception {
        System.out.println(todoDAO.getTime());
        System.out.println(todoDAO.getTime2());
    }

    @Test
    public void testInsert() throws Exception {
        TodoVO vo = TodoVO.builder()
                .title("Sample test...")
                .dueDate(LocalDate.of(2024,9,4))
                .build();

        todoDAO.insert(vo);
    }

    @Test
    public void testSelectAll() throws Exception {
        List<TodoVO> list = todoDAO.selectAll();
        list.forEach(vo -> System.out.println(vo));
    }

    @Test
    public void testSelectOne() throws Exception{
        Long tno = 1L;
        TodoVO vo = todoDAO.selectOne(tno);
        System.out.println(vo);

    }

//    @Test
//    public void testDeleteOne() throws Exception {
//        Long tno = 1L;
//
//        todoDAO.deleteOne(tno);
//        System.out.println(todoDAO.selectOne(tno));
//        assertThrows(SQLDataException.class, () ->{
//            todoDAO.selectOne(tno);
//        } );
//    //        assertEquals(todoDAO.selectOne(tno), null );
//    }

    @Test
    public void testUpgradeOne() throws Exception {
        TodoVO vo = TodoVO.builder()
                .tno(1L)
                .title("Sample test...")
                .dueDate(LocalDate.of(2001,9,4))
                .finished(true)
                .build();

        todoDAO.updateOne(vo);
    }

}