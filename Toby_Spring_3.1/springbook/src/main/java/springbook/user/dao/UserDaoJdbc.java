package springbook.user.dao;

import org.springframework.dao.DuplicateKeyException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class UserDaoJdbc implements UserDao{

    private DataSource dataSource;      //스프링에서 제공하는 인터페이스 객체

    private JdbcContext jdbcContext;
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {

        this.jdbcTemplate = new JdbcTemplate(dataSource);   //jdbcTemplate 방식

    }


    public void add(final User user) throws DuplicateUserIdException {
        this.jdbcTemplate.update("insert into users(id, name, password,level,login,recommend) values(?,?,?,?,?,?)", user.getId(), user.getName(), user.getPassword(), user.getLevel().intValue(),user.getLogin(),user.getRecommend());
    }

    public class DuplicateUserIdException extends RuntimeException{
        public DuplicateUserIdException(Throwable cause){
            super(cause);
        }
    }

    public User get(String id){
        return this.jdbcTemplate.queryForObject("select * from users where id = ?",new Object[]{id},this.userMapper);
    }


    public void deleteAll() {
        this.jdbcTemplate.update("delete from users");  //JdbcTemplate의 내장 콜백을 사용함.
    }


    public int getCount()  {
        return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }

    public List<User> getAll(){
        return this.jdbcTemplate.query("select * from users order by id", this.userMapper);
    }

    private RowMapper<User> userMapper =
            new RowMapper<User>() {
                @Override
                public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                    User user = new User();
                    user.setId(rs.getString("id"));
                    user.setName(rs.getString("name"));
                    user.setPassword(rs.getString("password"));
                    user.setLevel(Level.valueOf(rs.getInt("level")));
                    user.setLogin(rs.getInt("login"));
                    user.setRecommend(rs.getInt("recommend"));
                    return user;
                }
            };

}
