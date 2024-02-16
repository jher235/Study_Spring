package org.zerock.jdbcex.dao;

import lombok.Cleanup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TodoDAO {

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
}
