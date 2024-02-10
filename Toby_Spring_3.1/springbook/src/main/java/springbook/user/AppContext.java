package springbook.user;

import com.mysql.cj.jdbc.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springbook.user.dao.UserDao;
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

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

@Configuration
//@ImportResource("/test_applicationContext.xml")
@Import(SqlServiceContext.class)
@EnableTransactionManagement
@ComponentScan(basePackages = "springbook.user")    //이 패키지 아래서 찾기
public class AppContext {

//    @Autowired
//    SqlService sqlService;

    @Autowired
    UserDao userDao;


//   DB연결과 트랜잭션
    @Bean
    public DataSource dataSource(){
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost/test_tobyspring3_1");
        dataSource.setUsername("root");
        dataSource.setPassword("jher235");

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(){
            DataSourceTransactionManager tm = new DataSourceTransactionManager();
            tm.setDataSource(dataSource());
            return tm;
    }

//    애플리케이션 로직 & 테스트용 빈
//    @Bean
//    public UserDao userDao(){
//        UserDaoJdbc dao = new UserDaoJdbc();
////        dao.setDataSource(dataSource());
////        dao.setSqlService(this.sqlService);
//        return dao;
//    }

    @Bean
    public UserService userService(){
        UserServiceImpl service = new UserServiceImpl();
        service.setUserDao(this.userDao);
        service.setUserLevelUpgradePolicy(userLevelUpgradePolicy());
        return service;
    }

//    @Bean
//    public UserService testUserService() {
//        TestUserService testService = new TestUserService();
//        testService.setUserDao(this.userDao);
//        testService.setUserLevelUpgradePolicy(userLevelUpgradePolicy());
////        testService.setMailSender(mailSender());
//        return testService;
//    }

    @Bean
    public UserLevelUpgradePolicy userLevelUpgradePolicy(){
        return new UserLevelOrdinary();
    }

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
