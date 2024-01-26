package springbook.user.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import springbook.user.domain.User;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DaoFactory.class)
class UserDaoTest {


    @Test
    void addAndGet() throws SQLException, ClassNotFoundException {
//        ConnectionMaker cm = new NConnectionMaker();
//        UserDao userDao = new UserDao(cm);
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(DaoFactory.class);

        UserDao userDao = applicationContext.getBean("userDao",UserDao.class);

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
