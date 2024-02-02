package springbook.user.service;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.sql.DataSource;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;


public class UserService {
    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

//    private DataSource dataSource;

    UserDao userDao;
    UserLevelUpgradePolicy userLevelUpgradePolicy;

    private PlatformTransactionManager transactionManager;

    private MailSender mailSender;

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager){
        this.transactionManager = transactionManager;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setUserLevelUpgradePolicy(UserLevelUpgradePolicy userLevelUpgradePolicy) {
        this.userLevelUpgradePolicy = userLevelUpgradePolicy;
    }

//    public void setDataSource(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }

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

//        PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            List<User> users = userDao.getAll();
            for(User user : users){
                if(canUpgradeLevel(user)) upgradeLevel(user);
            }
            this.transactionManager.commit(status);
        }catch (RuntimeException e){
            this.transactionManager.rollback(status);
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
//        sendUpgradeEmail(user);
    }

    private void sendUpgradeEmail(User user){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("jher235@gmail.com");
        mailMessage.setSubject("Upgrade 안내");
        mailMessage.setText("사용자님의 등급이 " + user.getLevel().name() + "로 업그레이드되었습니다.");

        this.mailSender.send(mailMessage);

//        Properties props = new Properties();
//        props.put("mail.smtp.host", "smtp.gmail.com");
//        Session s = javax.mail.Session.getInstance(props, null);
//
//        MimeMessage message = new MimeMessage(s);
//        try{
//            message.setFrom(new InternetAddress("tim668666@gmail.com"));
//            message.addRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(user.getEmail())));
//            message.setSubject("Upgrade 안내");
//            message.setText("사용자님의 등급이 " + user.getLevel().name() + "로 업그레이드되었습니다.");
//
//            Transport.send(message);
//        }catch (AddressException e ){
//            throw new RuntimeException(e);
//        }catch (MessagingException e){
//            throw new RuntimeException(e);
//        }
////        catch (UnsupportedEncodingException e){
////            throw new RuntimeException(e);
////        }
    }

    public void add(User user){
        if(user.getLevel() == null) user.setLevel(Level.BASIC);
        userDao.add(user);
    }

}
