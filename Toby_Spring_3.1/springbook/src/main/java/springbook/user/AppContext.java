package springbook.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springbook.user.dao.UserDao;
import springbook.user.domain.User;
import springbook.user.service.UserLevelOrdinary;
import springbook.user.service.UserLevelUpgradePolicy;
import springbook.user.service.UserService;
import springbook.user.service.UserServiceImpl;
import springbook.user.sqlservice.OxmSqlService;
import springbook.user.sqlservice.SqlRegistry;
import springbook.user.sqlservice.SqlService;

//import springbook.user.service.UserServiceTest.TestUserService;
import springbook.user.sqlservice.updatable.EmbeddedDbSqlRegistry;

import javax.sql.DataSource;

import java.sql.Driver;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

@Configuration
//@ImportResource("/test_applicationContext.xml")
@Import({SqlServiceContext.class})
//@Import({SqlServiceContext.class, AppContext.TestAppContext.class, AppContext.ProductionAppContext.class})
@EnableTransactionManagement
@ComponentScan(basePackages = "springbook.user")    //이 패키지 아래서 찾기
@PropertySource("/database.properties")     //Environment 타입의 환경 오브젝트에 저장됨
@EnableSqlService   //sql서비스를 사용하겠다는 사용자 정의 애노테이션
public class AppContext implements SqlmapConfig{
    @Value("${db.driverClass}") Class<? extends Driver> driverClass;
    @Value("${db.url}") String url;
    @Value("${db.username}") String username;
    @Value("${db.password}") String password;

//    @Autowired
//    SqlService sqlService;

    @Autowired
    UserDao userDao;

    @Autowired
    Environment env;

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer(){
        return new PropertySourcesPlaceholderConfigurer();
    }

//   DB연결과 트랜잭션
    @Bean
    public DataSource dataSource(){
//        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
//
//        dataSource.setDriverClass(Driver.class);
//        dataSource.setUrl("jdbc:mysql://localhost/test_tobyspring3_1");
//        dataSource.setUsername("root");
//        dataSource.setPassword("jher235");
//
//        return dataSource;

        SimpleDriverDataSource ds = new SimpleDriverDataSource();

//        try{
//            ds.setDriverClass((Class<? extends java.sql.Driver>)Class.forName(env.getProperty("db.driverClass")) );
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        ds.setUrl(env.getProperty("db.url"));
//        ds.setUsername(env.getProperty("db.username"));
//        ds.setPassword(env.getProperty("db.password"));


        ds.setDriverClass(this.driverClass);

        ds.setUrl(this.url);
        ds.setUsername(this.username);
        ds.setPassword(this.password);

        return ds;
    }

    @Bean
    public PlatformTransactionManager transactionManager(){
            DataSourceTransactionManager tm = new DataSourceTransactionManager();
            tm.setDataSource(dataSource());
            return tm;
    }

    @Bean
    public SqlmapConfig sqlmapConfig(){
        return new UserSqlMapConfig();
    }

    @Override
    public Resource getSqlMapResource() {
        return new ClassPathResource("sqlmap.xml", UserDao.class);
    }

//    @Configuration
//    @Profile("production")
//    public static class ProductionAppContext {
//        @Bean
//        public MailSender mailSender() {
//            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//            mailSender.setHost("localhost");
//            return mailSender;
//        }
//    }

//    @Configuration
//    @Profile("test")
//    public static class TestAppContext {
//        @Bean
//        public UserService testUserService() {
//            return new TestUserService();
//        }
//        @Bean
//        public UserLevelUpgradePolicy userLevelUpgradePolicy(){
//            return new UserLevelOrdinary();
//        }
//
//        @Bean
//        public MailSender mailSender() {
//            return new DummyMailSender();
//        }
//    }

//    애플리케이션 로직 & 테스트용 빈
//    @Bean
//    public UserDao userDao(){
//        UserDaoJdbc dao = new UserDaoJdbc();
////        dao.setDataSource(dataSource());
////        dao.setSqlService(this.sqlService);
//        return dao;
//    }

//    @Bean
//    public UserService userService(){
//        UserServiceImpl service = new UserServiceImpl();
//        service.setUserDao(this.userDao);
//        service.setUserLevelUpgradePolicy(userLevelUpgradePolicy());
//        return service;
//    }

//    @Bean
//    public UserService testUserService() {
//        TestUserService testService = new TestUserService();
//        testService.setUserDao(this.userDao);
//        testService.setUserLevelUpgradePolicy(userLevelUpgradePolicy());
////        testService.setMailSender(mailSender());
//        return testService;
//    }

//    @Bean
//    public UserLevelUpgradePolicy userLevelUpgradePolicy(){
//        return new UserLevelOrdinary();
//    }

//    Sql 서비스

//    @Bean
//    public SqlService sqlService() {
//        OxmSqlService sqlService = new OxmSqlService();
//        sqlService.setUnmarshaller(unmarshaller());
//        sqlService.setSqlRegistry(sqlRegistry());
//        return sqlService;
//    }
//
//    @Bean
//    public SqlRegistry sqlRegistry() {
//        EmbeddedDbSqlRegistry sqlRegistry = new EmbeddedDbSqlRegistry();
//        sqlRegistry.setDataSource(embeddedDatabase());
//        return sqlRegistry;
//    }
//
//    @Bean
//    public Unmarshaller unmarshaller() {
//        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
//        marshaller.setContextPath("springbook.user.sqlservice.jaxb");
//        return marshaller;
//    }
//
//    @Bean
//    public DataSource embeddedDatabase() {
//        return new EmbeddedDatabaseBuilder()
//                .setName("embeddedDatabase")
//                .setType(HSQL)
//                .addScript("classpath:/etc/sqlRegistrySchema.sql")
//                .build();
//    }


}
