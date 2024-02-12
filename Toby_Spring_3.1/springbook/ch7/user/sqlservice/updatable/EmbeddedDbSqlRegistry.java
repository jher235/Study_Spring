package springbook.user.sqlservice.updatable;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import springbook.user.sqlservice.SqlNotFoundException;
import springbook.user.sqlservice.SqlUpdateFailureException;
import springbook.user.sqlservice.UpdatableSqlRegistry;

import javax.sql.DataSource;
import java.util.Map;

public class EmbeddedDbSqlRegistry implements UpdatableSqlRegistry {
    JdbcTemplate jdbc;
    TransactionTemplate transactionTemplate;

    public void setDataSource(DataSource dataSource){
        jdbc = new JdbcTemplate(dataSource);
        transactionTemplate = new TransactionTemplate(new DataSourceTransactionManager(dataSource));
    }


    @Override
    public void registerSql(String key, String sql) {
        jdbc.update("insert into sqlmap(key_, sql_) values(?,?)",key, sql);
    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        try {
            return jdbc.queryForObject("select sql_ from sqlmap where key_=?",String.class,key);
        }catch (EmptyResultDataAccessException e){
            throw new SqlNotFoundException(key + "에 해당하는 SQL을 찾을 수 없습니다.", e);
        }
    }

    //하나 업데이트
    @Override
    public void updateSql(String key, String sql) throws SqlUpdateFailureException {
        int affected = jdbc.update("update sqlmap set sql_ = ? where key_ = ?" , sql, key); //영행을 받은 레코드 개수 리턴
        if (affected == 0) {
            throw new SqlUpdateFailureException(key + "에 해당하는 SQL을 찾을 수 없습니다");
        }
    }

    //여러개 업데이트
    @Override
    public void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                for(Map.Entry<String, String> entry : sqlmap.entrySet()){
                    updateSql(entry.getKey(), entry.getValue());
                }
            }
        });
    }
}
