package springbook.user.service;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;




public class UserService {
    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    private DataSource dataSource;

    UserDao userDao;
    UserLevelUpgradePolicy userLevelUpgradePolicy;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setUserLevelUpgradePolicy(UserLevelUpgradePolicy userLevelUpgradePolicy) {
        this.userLevelUpgradePolicy = userLevelUpgradePolicy;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void upgradeLevels() throws Exception{
//        TransactionSynchronizationManager.initSynchronization();    //트랜잭션 동기화 관리자를 이용해 동기화 작업을 초기화한다.
//        Connection c = DataSourceUtils.getConnection(dataSource);
//        c.setAutoCommit(false);
//        try{
//            List<User> users = userDao.getAll();
//            for(User user:users){
//                if(canUpgradeLevel(user)) upgradeLevel(user);
//            }
//            c.commit(); //정상적으로 마치면 커밋
//        }catch (Exception e){
//            c.rollback();   //예외발생시 롤백
//            throw e;
//        }finally {
//            DataSourceUtils.releaseConnection(c, dataSource);   //스프링 유틸리티 메소드를 이용해 db커넥션 닫기
//            TransactionSynchronizationManager.unbindResource(this.dataSource);  //동기화 작업 종료 및 정리
//            TransactionSynchronizationManager.clearSynchronization();
//        }

        PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            List<User> users = userDao.getAll();
            for(User user : users){
                if(canUpgradeLevel(user)) upgradeLevel(user);
            }
            transactionManager.commit(status);
        }catch (RuntimeException e){
            transactionManager.rollback(status);
            throw e;
        }



    }

    private boolean canUpgradeLevel (User user){
        return userLevelUpgradePolicy.canUpgradeLevel(user);
//        Level currentLevel = user.getLevel();
//        switch (currentLevel){
//            case BASIC ->{return user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER;}
//            case SILVER -> {return user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD;}
//            case GOLD -> {return false;}
//            default -> throw new IllegalArgumentException("Unknown Level : "+ currentLevel);
//            }
    }

    protected void upgradeLevel(User user){
//        userLevelUpgradePolicy.upgradeLevel(user);
        user.upgradeLevel();
        userDao.update(user);
    }

    public void add(User user){
        if(user.getLevel() == null) user.setLevel(Level.BASIC);
        userDao.add(user);
    }

}
