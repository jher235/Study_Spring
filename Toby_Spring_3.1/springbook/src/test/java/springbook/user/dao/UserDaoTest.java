package springbook.user.dao;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import springbook.user.domain.User;

import java.sql.SQLException;


import static org.junit.jupiter.api.Assertions.*;   //JUnit
import static  org.assertj.core.api.Assertions.*;   //AssertJ


//@ExtendWith(SpringExtension.class)        //JUnit 5에서 사용하는 확장 모델 지정
//@ContextConfiguration(classes = DaoFactory.class)
class UserDaoTest {


    @Test
    void addAndGet() throws SQLException, ClassNotFoundException {
//        ConnectionMaker cm = new NConnectionMaker();
//        UserDao userDao = new UserDao(cm);
//        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(DaoFactory.class);     //이건 daofactory를 받아옴

        ApplicationContext applicationContext = new GenericXmlApplicationContext("applicationContext.xml");   //이건 xml 사용

        UserDao userDao = applicationContext.getBean("userDao",UserDao.class);



        userDao.deleteAll();
        assertThat(userDao.getCount()).isEqualTo(0);

        User user1 = new User("0","test0","test1");
        User user2 = new User("1","test2","test2");

        userDao.add(user1);
        userDao.add(user2);

        assertThat(userDao.getCount()).isEqualTo(2);




        System.out.println("현재 users 수 : "+userDao.getCount());




        User result = userDao.get(user1.getId());
        assertThat(result.getName()).isEqualTo(user1.getName());
        assertThat(result.getPassword()).isEqualTo(user1.getPassword());
        assertEquals(result.getName(),user1.getName());

        User result2 = userDao.get(user2.getId());
        assertThat(result2.getName()).isEqualTo(user2.getName());
        assertThat(result2.getPassword()).isEqualTo(user2.getPassword());
        assertEquals(result2.getName(),user2.getName());


        userDao.deleteAll();
        System.out.println("현재 users 수 : "+userDao.getCount());

    }

    @Test
    public void getUserFailure(){
        EmptyResultDataAccessException e = assertThrows(
                EmptyResultDataAccessException.class,
                ()->{
                    ApplicationContext applicationContext = new GenericXmlApplicationContext("applicationContext.xml");

                    UserDao userDao = applicationContext.getBean("userDao", UserDao.class);
                    userDao.deleteAll();
                    assertThat(userDao.getCount()).isEqualTo(0);

                    userDao.get("unknown_id");
                }
        );
    }
}
