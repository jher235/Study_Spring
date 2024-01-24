package springbook.user.dao;

import org.junit.jupiter.api.Test;
import springbook.user.domain.User;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest {


    @Test
    void addAndGet() throws SQLException, ClassNotFoundException {
        ConnectionMaker cm = new NConnectionMaker();

        UserDao userDao = new UserDao(cm);
        User user = new User();
        user.setId("0");
        user.setName("test1");
        user.setPassword("test1");

        try {
            userDao.add(user);
        } catch (ClassNotFoundException e) {
            System.out.println("problem: "+ e);
            throw new RuntimeException(e);
        } catch (SQLException e) {
            System.out.println("problem: "+ e);
            throw new RuntimeException(e);
        }

        User result = userDao.get("0");
        assertEquals(result.getId(),"0");

    }
}
