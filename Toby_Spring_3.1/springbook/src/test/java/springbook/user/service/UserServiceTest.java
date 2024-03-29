package springbook.user.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import springbook.user.AppContext;
import springbook.user.dao.MockUserDao;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


import static org.mockito.Mockito.*;
import static springbook.user.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")     //test프로파일을 활성 프로파일로 사용하게 함
//@ContextConfiguration(locations ="/test_applicationContext.xml")
@ContextConfiguration(classes = {AppContext.class, TestAppContext.class})
@Transactional
public class UserServiceTest {
    @Autowired
    UserService userService;

    @Autowired
    UserService testUserService;

//    @Autowired
//    UserServiceImpl userServiceImpl;

    @Autowired
    UserDao userDao;

    @Autowired
    DataSource dataSource;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    UserLevelUpgradePolicy userLevelUpgradePolicy;

//    @Autowired
//    MailSender mailSender;

    @Autowired
    ApplicationContext context;



    List<User> users;

    @BeforeEach
    public void setUp(){
        users = Arrays.asList(
                new User("test1","test1","t1","test1@tt.com", Level.BASIC,MIN_LOGCOUNT_FOR_SILVER-1,0),
                new User("test2","test2","t2","test2@tt.com", Level.BASIC,MIN_LOGCOUNT_FOR_SILVER,0),
                new User("test3","test3","t3","test3@tt.com", Level.SILVER,60,MIN_RECOMMEND_FOR_GOLD-1),
                new User("test4","test4","t4","test4@tt.com", Level.SILVER,60,MIN_RECOMMEND_FOR_GOLD),
                new User("test5","test5","t5","test5@tt.com", Level.GOLD,100,Integer.MAX_VALUE)
        );
    }

    @AfterEach
    public void clear(){
        userDao.deleteAll();
    }

//    public static class TestUserService extends UserServiceImpl{
//        private String id;
//
//        private TestUserService(String id){
//            this.id = id;
//        }
//
//        @Override
//        protected void upgradeLevel(User user) {
//            if(user.getId().equals(this.id)) throw new TestUserServiceException();
//            super.upgradeLevel(user);
//        }
//    }

    static class TestUserServiceException extends RuntimeException{
    }

    static class MockMailSender implements MailSender{
        private List<String> request = new ArrayList<String>();
        public List<String> getRequest(){
            return request;
        }

        @Override
        public void send(SimpleMailMessage simpleMessage) throws MailException {
            request.add(simpleMessage.getTo()[0]);
        }

        @Override
        public void send(SimpleMailMessage... simpleMessages) throws MailException {

        }
    }

    @Test
    public void mockUpgradeLevels() throws Exception {
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        UserDao mockUserDao = mock(UserDao.class);  //유저다오 목 오브젝트 생성
        when(mockUserDao.getAll()).thenReturn(this.users);  //리턴값 설정
        userServiceImpl.setUserDao(mockUserDao);    //di

        userServiceImpl.setUserLevelUpgradePolicy(this.userLevelUpgradePolicy);

        MailSender mockMailSender = mock(MailSender.class);
        userServiceImpl.setMailSender(mockMailSender);

        userServiceImpl.upgradeLevels();

        verify(mockUserDao, times(2)).update(any(User.class));
        verify(mockUserDao, times(2)).update(any(User.class));
        verify(mockUserDao).update(users.get(1));
        assertThat(users.get(1).getLevel()).isEqualTo(Level.SILVER);
        verify(mockUserDao).update(users.get(3));
        assertThat(users.get(3).getLevel()).isEqualTo(Level.GOLD);

//        ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
//        verify(mockMailSender, times(2)).send(mailMessageArg.capture());
//        List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
//        assertThat(mailMessages.get(0).getTo()[0]).isEqualTo(users.get(1).getEmail());
//        assertThat(mailMessages.get(1).getTo()[0]).isEqualTo(users.get(3).getEmail());


    }

        @Test
//    @DirtiesContext
    public void upgradeLevels테스트() throws Exception {   //목 오브젝트를 통해 db를 연동하지 않는 테스트
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        MockUserDao mockUserDao = new MockUserDao(this.users);
        userServiceImpl.setUserDao(mockUserDao);
        userServiceImpl.setUserLevelUpgradePolicy(userLevelUpgradePolicy);

        userDao.deleteAll();
        for(User user: users) userDao.add(user);

//        MockMailSender mockMailSender = new MockMailSender();
//        userServiceImpl.setMailSender(mockMailSender);
//        userService.setMailSender(mockMailSender);

        userServiceImpl.upgradeLevels();



//        checkLevel업글(users.get(0), false);
//        checkLevel업글(users.get(1), true);
//        checkLevel업글(users.get(2), false);
//        checkLevel업글(users.get(3), true);
//        checkLevel업글(users.get(4), false);

//        List<String> request = mockMailSender.getRequest();
//        assertThat(request.size()).isEqualTo(2);      //2번째 4번째 업글되어 사이즈는 2
//        assertThat(request.get(0)).isEqualTo(users.get(1).getEmail());    //이메일 주소 확인
//        assertThat(request.get(1)).isEqualTo(users.get(3).getEmail());

        List<User> updated = mockUserDao.getUpdated();
        assertThat(updated.size()).isEqualTo(2);
        checkUserAndLevel(updated.get(0),"test2",Level.SILVER);
        checkUserAndLevel(updated.get(1),"test4",Level.GOLD);


    }

    private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel){
        assertThat(updated.getId()).isEqualTo(expectedId);
        assertThat(updated.getLevel()).isEqualTo(expectedLevel);
    }

    private void checkLevelUpgraded(User user, boolean upgraded){
        User userUpdate = userDao.get(user.getId());
        if (upgraded){
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel().getNextLevel());
        }else{
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel());
        }

    }

    @Test
    public void add(){
        userDao.deleteAll();

        User userWithLevel = users.get(4);  //골드레벨
        User userWithoutLevel = users.get(0); //레벨 없음
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        assertThat(userWithLevelRead.getLevel()).isEqualTo(userWithLevel.getLevel());
        assertThat(userWithoutLevelRead.getLevel()).isEqualTo(Level.BASIC);

    }

    @Test
    @DirtiesContext
    public void upgradeAllOrNot() throws Exception{

        userDao.deleteAll();
        for(User user : users) userDao.add(user);

        try{
//            txUserService.upgradeLevels();
            this.testUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        }catch (TestUserServiceException e){

        }

        checkLevelUpgraded(users.get(1),false);
    }

    @Test
    @Transactional
    @Rollback(false)    //롤백되지 않도록 함 기본설정은 true로 롤백됨.
//    @Transactional(propagation = Propagation.NEVER)     //트랜잭션 전파 속성을 NEVER로 주어서 트랜잭션이 시작되지 않도록 함.
    public void transactionSync(){

        userService.deleteAll();
        assertThat(userDao.getCount()).isEqualTo(0);

        DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition(); //트랜잭션 정의 기본값 사용
        TransactionStatus txStatus = transactionManager.getTransaction(txDefinition);   //트랜잭션 매니저에게 트랜잭션 요구

        userService.add(users.get(0));
        userService.add(users.get(1));

        assertThat(userDao.getCount()).isEqualTo(2);

        transactionManager.rollback(txStatus);
        assertThat(userDao.getCount()).isEqualTo(0);
//        transactionManager.commit(txStatus);
    }

    public static class TestUserService extends UserServiceImpl {
        private String id = "test4";

        protected void upgradeLevel(User user) {
            if (user.getId().equals(this.id)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        }

        public List<User> getAll() {
            for(User user : super.getAll()) {
                super.update(user);
            }
            return null;
        }

    }


}