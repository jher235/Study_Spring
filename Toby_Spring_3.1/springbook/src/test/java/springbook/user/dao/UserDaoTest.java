package springbook.user.dao;


import com.sun.jdi.request.DuplicateRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


//@ExtendWith(SpringExtension.class)        //JUnit 5에서 사용하는 확장 모델 지정   ContextConfiguration이 설정파일을 알려주면 그걸 가져와서 적용해줌
//@ContextConfiguration(classes = DaoFactory.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations ="/test_applicationContext.xml")      //방법2 xml파일을 새로 만들어서 db커넥션 설정
//@ContextConfiguration(locations ="/applicationContext.xml")
//@DirtiesContext     //applicationContext의 구성, 상태를 변경할 것이라는 것을 알려주는 것.
class UserDaoTest {

//    @Autowired
//    private ApplicationContext applicationContext;

    @Autowired
    private UserDao userDao;
    @Autowired
    DataSource dataSource;

    private User user1;
    private User user2;
    private User user3;


    @BeforeEach     //메소드 추출을 대신 JUnit5에서 제공하는 기능을 사용함
    public void setUp(){

        this.user1 = new User("0","test0","test1","test1@tt.com",Level.BASIC,1,0);
        this.user2 = new User("1","test2","test2","test2@tt.com",Level.SILVER,55,10);
        this.user3 = new User("2","test3","test3","test3@tt.com",Level.GOLD,100,40);

    }

    @Test
    void addAndGet() throws SQLException, ClassNotFoundException {
//        ConnectionMaker cm = new NConnectionMaker();
//        UserDao userDao = new UserDao(cm);
//        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(DaoFactory.class);     //이건 daofactory를 받아옴

//        ApplicationContext applicationContext = new GenericXmlApplicationContext("applicationContext.xml");   //이건 xml 사용

//        UserDao userDao = applicationContext.getBean("userDao",UserDao.class);

        userDao.deleteAll();
        assertThat(userDao.getCount()).isEqualTo(0);



        userDao.add(user1);
        userDao.add(user2);

        assertThat(userDao.getCount()).isEqualTo(2);

        System.out.println("현재 users 수 : "+userDao.getCount());

        User result1 = userDao.get(user1.getId());
        checkSameUser(result1,user1);


        User result2 = userDao.get(user2.getId());
        checkSameUser(result2,user2);

        userDao.deleteAll();
        System.out.println("현재 users 수 : "+userDao.getCount());

    }

    @Test
    public void getUserFailure(){
        EmptyResultDataAccessException e = assertThrows(
                EmptyResultDataAccessException.class,
                ()->{

                    userDao.deleteAll();
                    assertThat(userDao.getCount()).isEqualTo(0);

                    userDao.get("unknown_id");
                }
        );
    }

    @Test
    public void getAll테스트() throws SQLException, ClassNotFoundException {
        userDao.deleteAll();

        List<User> users0 = userDao.getAll();
        assertThat(users0.size()).isEqualTo(0);

        userDao.add(user1);
        List<User> users1 = userDao.getAll();
        assertThat(users1.size()).isEqualTo(1);
        checkSameUser(user1,users1.get(0));

        userDao.add(user2);
        List<User> users2 = userDao.getAll();
        assertThat(users2.size()).isEqualTo(2);
        checkSameUser(user1,users2.get(0));
        checkSameUser(user2,users2.get(1));



        userDao.add(user3);
        List<User> users3 = userDao.getAll();
        assertThat(users3.size()).isEqualTo(3);
        checkSameUser(user1,users3.get(0));
        checkSameUser(user2,users3.get(1));
        checkSameUser(user3,users3.get(2));



    }

    private void checkSameUser(User user1, User user2){
        assertThat(user1.getId()).isEqualTo(user2.getId());
        assertThat(user1.getName()).isEqualTo(user2.getName());
        assertThat(user1.getPassword()).isEqualTo(user2.getPassword());
        assertThat(user1.getLevel()).isEqualTo(user2.getLevel());
        assertThat(user1.getLogin()).isEqualTo(user2.getLogin());
        assertThat(user1.getRecommend()).isEqualTo(user2.getRecommend());
    }

    @Test
    public void duplicateKey(){
        userDao.deleteAll();
        userDao.add(user1);

        assertThrows(DataAccessException.class, ()->userDao.add(user1));

    }

    //dataSource를 사용해 SQLException에서 직접 DuplicateKeyException으로 전환하는 기능을 확인해보는 학습 테스트.
    @Test
    public void sqlExceptionTranslate(){
        userDao.deleteAll();

        try {
            userDao.add(user1);
            userDao.add(user1);
        }catch (DuplicateKeyException e){
            SQLException sqlEx = (SQLException)e.getCause();
            SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);

//            assertThat(set.translate(null,null,sqlEx)).isEqualTo(DuplicateKeyException.class);
        }
    }

    @Test
    public void update(){
        userDao.deleteAll();

        userDao.add(user1);
        userDao.add(user2);

        user1.setName("김재헌");
        user1.setPassword("wogjs");
        user1.setLevel(Level.GOLD);
        user1.setLogin(1000);
        user1.setRecommend(100);
        userDao.update(user1);

        User user1update = userDao.get(user1.getId());
        checkSameUser(user1,user1update);

        User user2same = userDao.get(user2.getId());
        checkSameUser(user2,user2same);

    }



}
