package springbook.learnTest.embeddeddb;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;
import static org.assertj.core.api.Assertions.*;

public class EmbeddedDbTest {
    EmbeddedDatabase db;
    JdbcTemplate template;

    @BeforeEach
    public void setUp(){
        db = new EmbeddedDatabaseBuilder()
                .setType(HSQL)      //내장형 db종류
//                .addScript("classpath:/springbok/learningtest/spring/embeddeddb/schema.sql")    //초기화에 사용할 db스크립트 리소스
//                .addScript("classpath:/springbook/learningtest/spring/embeddeddb/data.sql")     //초기 데이터
                .addScript("classpath:/etc/schema.sql")    //초기화에 사용할 db스크립트 리소스
                .addScript("classpath:/etc/data.sql")     //초기 데이터
                .build();

        template = new JdbcTemplate(db);
    }

    @AfterEach
    public void tearDown(){
        db.shutdown();      //매 테스트가 끝날때마다 db종료
    }

    @Test
    public void initData(){
        assertThat(template.queryForObject("select count(*) from sqlmap",Integer.class)).isEqualTo(2);
        List<Map<String, Object>> list = template.queryForList("select * from sqlmap order by key_");

        assertThat((String)list.get(0).get("key_")).isEqualTo("KEY1");
        assertThat((String)list.get(0).get("sql_")).isEqualTo("SQL1");
        assertThat((String)list.get(1).get("key_")).isEqualTo("KEY2");
        assertThat((String)list.get(1).get("sql_")).isEqualTo("SQL2");
    }

    @Test
    public void insert(){
        template.update("insert into sqlmap(key_, sql_) values (?,?);", "KEY3", "SQL3");

        assertThat(template.queryForObject("select count(*) from sqlmap", Integer.class)).isEqualTo(3);
    }


}
