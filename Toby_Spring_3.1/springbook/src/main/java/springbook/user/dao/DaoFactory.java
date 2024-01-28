package springbook.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

@Configuration
public class DaoFactory {

    @Bean
    public UserDao userDao(){
        UserDao userDao = new UserDao();        //메소드를 통한 주입
//        userDao.setConnectionMaker(connectionMaker());
        userDao.setDataSource(dataSource());
        return userDao;

//        return new UserDao(connectionMaker());       //생성자를 통한 주입 시
    }


    public AccountDao accountDao(){
        return new AccountDao(connectionMaker());
    }

    @Bean
    public ConnectionMaker connectionMaker() {
        return new MySqlConnectionMaker();
    }

    @Bean
    public DataSource dataSource(){
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost/tobyspring3_1?");
        dataSource.setUsername("root");
        dataSource.setPassword("jher235");

        return dataSource;
    }
}
