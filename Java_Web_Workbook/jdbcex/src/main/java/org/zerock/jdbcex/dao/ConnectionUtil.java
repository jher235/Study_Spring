package org.zerock.jdbcex.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;

//TodoDAO에서 필요한 작업을 수행할 때 HikariDataSource를 이용하므로 이에 대한 처리를 쉽게 사용할 수 있도록 ConnectionUtil클래스를 Enum으로 구성해서 사용한다.
public enum ConnectionUtil {
    INSTANCE;

    private HikariDataSource ds;

    ConnectionUtil(){

        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.mariadb.jdbc.Driver");
        config.setJdbcUrl("jdbc:mariadb://localhost:3307/webdb");
        config.setUsername("webuser");
        config.setPassword("webuser");
        config.addDataSourceProperty("cachePrepStmt", true);
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        ds = new HikariDataSource(config);

//        Connection connection = ds.getConnection();
//
//        System.out.println(connection);
//
//        connection.close();
    }

    public Connection getConnection()  throws  Exception{
        return ds.getConnection();
    }


}
