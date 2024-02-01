package springbook.user.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


import static springbook.user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.UserService.MIN_RECOMMEND_FOR_GOLD;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations ="/test_applicationContext.xml")
class UserServiceTest {
    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    @Autowired
    DataSource dataSource;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    UserLevelUpgradePolicy userLevelUpgradePolicy;

    List<User> users;

    @BeforeEach
    public void setUp(){
        users = Arrays.asList(
                new User("test1","test1","t1", Level.BASIC,MIN_LOGCOUNT_FOR_SILVER-1,0),
                new User("test2","test2","t2", Level.BASIC,MIN_LOGCOUNT_FOR_SILVER,0),
                new User("test3","test3","t3", Level.SILVER,60,MIN_RECOMMEND_FOR_GOLD-1),
                new User("test4","test4","t4", Level.SILVER,60,MIN_RECOMMEND_FOR_GOLD),
                new User("test5","test5","t5", Level.GOLD,100,Integer.MAX_VALUE)
        );
    }

    static class TestUserService extends UserService{
        private String id;

        private TestUserService(String id){
            this.id = id;
        }

        @Override
        protected void upgradeLevel(User user) {
            if(user.getId().equals(this.id)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        }
    }

    static class TestUserServiceException extends RuntimeException{
    }

    @Test
    public void upgradeLevels테스트() throws Exception {
        userDao.deleteAll();
        for(User user: users) userDao.add(user);

        userService.upgradeLevels();


        checkLevel업글(users.get(0), false);
        checkLevel업글(users.get(1), true);
        checkLevel업글(users.get(2), false);
        checkLevel업글(users.get(3), true);
        checkLevel업글(users.get(4), false);

    }

    private void checkLevel업글(User user, boolean upgraded){
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
    public void upgradeAllOrNot() throws Exception{
        UserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(this.userDao);
//        testUserService.setDataSource(this.dataSource);
        testUserService.setTransactionManager(transactionManager);
        testUserService.setUserLevelUpgradePolicy(this.userLevelUpgradePolicy);
        userDao.deleteAll();
        for(User user : users) userDao.add(user);

        try{
            testUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        }catch (TestUserServiceException e){

        }

        checkLevel업글(users.get(1),false);
    }


}