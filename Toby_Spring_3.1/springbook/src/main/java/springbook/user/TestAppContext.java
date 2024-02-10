package springbook.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;

import springbook.user.service.DummyMailSender;
import springbook.user.service.UserLevelOrdinary;
import springbook.user.service.UserLevelUpgradePolicy;
import springbook.user.service.UserService;
//import springbook.user.service.UserServiceTest.TestUserService;

@Configuration
public class TestAppContext {   //테스트용 빈 정보 분리
//    @Bean
//    public UserService testUserService() {
//        return new TestUserService();
//    }
    @Bean
    public UserLevelUpgradePolicy userLevelUpgradePolicy(){
        return new UserLevelOrdinary();
    }

    @Bean
    public MailSender mailSender() {
        return new DummyMailSender();
    }
}