package org.zerock.jdbcex.dao;

import lombok.Cleanup;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import org.zerock.jdbcex.domain.TodoVO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TodoDAO {

    //TodoDAO의 기능에 문제가 없는지 확인용 시간 출력
    public String getTime(){
        String now = null;
        try(    // try-with-resources 문
            Connection connection = ConnectionUtil.INSTANCE.getConnection();    //ConnectionUtil을 통해 데이터베이스 연결을 가져옴
            PreparedStatement preparedStatement = connection.prepareStatement("select now()");//쿼리 준비

            ResultSet resultSet = preparedStatement.executeQuery();//준비된 쿼리를 실행하고, 그 결과를 가져옵니다. 결과는 ResultSet 객체에 저장
        ){  //try-with-resources 문을 닫기
            resultSet.next();// ResultSet에서 다음 행 가져옴

            now = resultSet.getString(1);   // ResultSet의 첫 번째 열의 값을 문자열로 가져와 now 변수에 저장, 이 값은 "select now()" 쿼리의 결과, 즉 현재 시간임
        } catch (Exception e) {
            e.printStackTrace();    //예외 발생 시 발생한 예외의 스택 트레이스를 출력
        }
        return now;
    }

    //lombok의 @Cleanup을 통하여 close를 함.
    public String getTime2() throws Exception {
        @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement("select now()");
        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        resultSet.next();

        String now = resultSet.getString(1);

        return now;
    }

    public void insert(TodoVO vo) throws Exception {
        String sql = "insert into tbl_todo(title, dueDate, finished) values (?,?,?)";

        @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, vo.getTitle());
        preparedStatement.setDate(2, Date.valueOf(vo.getDueDate()));
        preparedStatement.setBoolean(3, vo.isFinished());

        preparedStatement.executeUpdate();

    }

    public List<TodoVO> selectAll() throws Exception {
        String sql = "select * from tbl_todo";

        @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        List<TodoVO> list = new ArrayList<>();

        while (resultSet.next()){
            TodoVO vo = TodoVO.builder()
                    .tno(resultSet.getLong("tno"))
                    .title(resultSet.getString("title"))
                    .dueDate(resultSet.getDate("dueDate").toLocalDate())
                    .finished(resultSet.getBoolean("finished"))
                    .build();

            list.add(vo);
        }

        return  list;
    }

    public TodoVO selectOne(Long tno) throws Exception {
        String sql = "select * from tbl_todo where tno = ?";

//        try (
//                Connection connection = ConnectionUtil.INSTANCE.getConnection();
//                PreparedStatement preparedStatement = connection.prepareStatement(sql);
//
//                ResultSet resultSet = preparedStatement.executeQuery();
//        ){
//            resultSet.next();
//            TodoVO vo = TodoVO.builder()
//                    .tno(resultSet.getLong("tno"))
//                    .title(resultSet.getString("title"))
//                    .dueDate(resultSet.getDate("dueDate").toLocalDate())
//                    .finished(resultSet.getBoolean("finished"))
//                    .build();
//            return vo;
//
//        }

        @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setLong(1, tno);

        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        resultSet.next();

        TodoVO vo = TodoVO.builder()
                .tno(resultSet.getLong("tno"))
                .title(resultSet.getString("title"))
                .dueDate(resultSet.getDate("dueDate").toLocalDate())
                .finished(resultSet.getBoolean("finished"))
                .build();

        return vo;
    }

    public void deleteOne(Long tno) throws Exception {
        String sql = "delete from tbl_todo where tno = ?";

        @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setLong(1, tno);

        preparedStatement.executeUpdate();

    }

    public void updateOne(TodoVO vo) throws Exception {
        String sql = "update tbl_todo set title=?, dueDate =?, finished=? where tno=?";

        @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1,vo.getTitle());
        preparedStatement.setDate(2, Date.valueOf(vo.getDueDate()));
        preparedStatement.setBoolean(3, vo.isFinished());
        preparedStatement.setLong(4, vo.getTno());

        preparedStatement.executeUpdate();

    }
}
