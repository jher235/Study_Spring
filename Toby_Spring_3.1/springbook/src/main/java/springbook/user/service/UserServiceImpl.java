package springbook.user.service;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import java.util.List;


public class UserServiceImpl implements UserService {
    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

//    private DataSource dataSource;

    UserDao userDao;
    UserLevelUpgradePolicy userLevelUpgradePolicy;

//    private PlatformTransactionManager transactionManager;

    private MailSender mailSender;

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

//    public void setTransactionManager(PlatformTransactionManager transactionManager){
//        this.transactionManager = transactionManager;
//    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setUserLevelUpgradePolicy(UserLevelUpgradePolicy userLevelUpgradePolicy) {
        this.userLevelUpgradePolicy = userLevelUpgradePolicy;
    }

//    public void setDataSource(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }

    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        for (User user : users) {
            if (canUpgradeLevel(user)) upgradeLevel(user);
        }

//        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
//
//        try {
//            List<User> users = userDao.getAll();
//            for(User user : users){
//                if(canUpgradeLevel(user)) upgradeLevel(user);
//            }
//            this.transactionManager.commit(status);
//        }catch (RuntimeException e){
//            this.transactionManager.rollback(status);
//            throw e;
//        }

    }


    private boolean canUpgradeLevel(User user) {
        return userLevelUpgradePolicy.canUpgradeLevel(user);
//        Level currentLevel = user.getLevel();
//        switch (currentLevel){
//            case BASIC ->{return user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER;}
//            case SILVER -> {return user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD;}
//            case GOLD -> {return false;}
//            default -> throw new IllegalArgumentException("Unknown Level : "+ currentLevel);
//            }
    }

    protected void upgradeLevel(User user) {
//        userLevelUpgradePolicy.upgradeLevel(user);
        user.upgradeLevel();
        userDao.update(user);
//        sendUpgradeEmail(user);
    }

    private void sendUpgradeEmail(User user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("jher235@gmail.com");
        mailMessage.setSubject("Upgrade 안내");
        mailMessage.setText("사용자님의 등급이 " + user.getLevel().name() + "로 업그레이드되었습니다.");

        this.mailSender.send(mailMessage);


    }

    public void add(User user) {
        if (user.getLevel() == null) user.setLevel(Level.BASIC);
        userDao.add(user);
    }

}
