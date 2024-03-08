package org.zerock.w1.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;

public class ConnectTests {

    @Test
    public void testConnection() throws Exception{
        Class.forName("org.mariadb.jdbc.Driver");

        Connection connection = DriverManager.getConnection(
                "jdbc:mariadb://localhost:3307/webdb",
                "webuser",
                "webuser");

        Assertions.assertNotNull(connection);

        connection.close();
    }

    @Test
    public void test1(){
        int v1=10;
        int v2=10;

        Assertions.assertEquals(v1,v2);
    }
}
