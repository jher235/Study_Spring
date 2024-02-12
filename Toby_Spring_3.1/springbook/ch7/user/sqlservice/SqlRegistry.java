package springbook.user.sqlservice;

public interface SqlRegistry {
    void registerSql(String key, String sql);

    String findSql(String key) throws SqlNotFoundException; //키로 sql 검색
}
