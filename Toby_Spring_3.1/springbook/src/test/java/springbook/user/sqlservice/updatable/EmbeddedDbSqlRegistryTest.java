package springbook.user.sqlservice.updatable;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import springbook.user.sqlservice.SqlUpdateFailureException;
import springbook.user.sqlservice.UpdatableSqlRegistry;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

class EmbeddedDbSqlRegistryTest extends AbstractUpdatableSqlRegistryTest{
    EmbeddedDatabase db;

    @Override
    protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
        db = new EmbeddedDatabaseBuilder()
                .setType(HSQL)
                .addScript("classpath:/etc/sqlRegistrySchema.sql")
                .build();

        EmbeddedDbSqlRegistry embeddedDbSqlRegistry = new EmbeddedDbSqlRegistry();
        embeddedDbSqlRegistry.setDataSource(db);

        return embeddedDbSqlRegistry;
    }

    @AfterEach
    public void tearDown() {
        db.shutdown();
    }

    @Test
    public void transactionalUpdate() {
        checkFindResult("SQL1", "SQL2", "SQL3");

        Map<String, String> sqlmap = new HashMap<String, String>();
        sqlmap.put("KEY1", "Modified1");
        sqlmap.put("KEY9999!@#$", "Modified9999");      //두번째 키를 존재하지 않는 것으로 두어 테스트 실패를 유도

        try {
            sqlRegistry.updateSql(sqlmap);
            fail();
        }
        catch(SqlUpdateFailureException e) {}

        checkFindResult("SQL1", "SQL2", "SQL3");
    }

}