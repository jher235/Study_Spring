package springbook.user.dao;

import org.springframework.dao.DuplicateKeyException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import springbook.user.domain.Level;
import springbook.user.domain.User;
import springbook.user.sqlservice.SqlService;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Map;

public class UserDaoJdbc implements UserDao{

    private DataSource dataSource;      //스프링에서 제공하는 인터페이스 객체

    private JdbcContext jdbcContext;
    private JdbcTemplate jdbcTemplate;

//    private String sqlAdd;

    private Map<String, String > sqlMap;

    private SqlService sqlService;

    public void setSqlService(SqlService sqlService) {
        this.sqlService = sqlService;
    }

    public void setSqlMap(Map<String, String> sqlMap) {
        this.sqlMap = sqlMap;
    }

//    public void setSqlAdd(String sqlAdd) {
//        this.sqlAdd = sqlAdd;
//    }

    public void setDataSource(DataSource dataSource) {

        this.jdbcTemplate = new JdbcTemplate(dataSource);   //jdbcTemplate 방식

    }


    public void add(final User user) throws DuplicateUserIdException {
//        this.jdbcTemplate.update("insert into users(id, name, password,email,level,login,recommend) values(?,?,?,?,?,?,?)", user.getId(), user.getName(), user.getPassword(),user.getEmail(), user.getLevel().intValue(),user.getLogin(),user.getRecommend());
        this.jdbcTemplate.update(
                this.sqlService.getSql("userAdd"),
                user.getId(), user.getName(), user.getPassword(),user.getEmail(),
                user.getLevel().intValue(),user.getLogin(),user.getRecommend());
    }

    public class DuplicateUserIdException extends RuntimeException{ //아이디 중복 예외
        public DuplicateUserIdException(Throwable cause){
            super(cause);
        }
    }

    public User get(String id){
        return this.jdbcTemplate.queryForObject(this.sqlService.getSql("userGet"),new Object[]{id},this.userMapper);
    }


    public void deleteAll() {
        this.jdbcTemplate.update(this.sqlService.getSql("userDeleteAll"));  //JdbcTemplate의 내장 콜백을 사용함.
    }


    public int getCount()  {
        return this.jdbcTemplate.queryForObject(this.sqlService.getSql("userGetCount"), Integer.class);
    }

    @Override
    public void update(User user) {
        this.jdbcTemplate.update(
                this.sqlService.getSql("userUpdate"), user.getName(), user.getPassword(),user.getEmail(),
                        user.getLevel().intValue(),user.getLogin(), user.getRecommend(), user.getId()
        );

    }

    public List<User> getAll(){
        return this.jdbcTemplate.query(this.sqlService.getSql("userGetAll"), this.userMapper);
    }

    private RowMapper<User> userMapper =
            new RowMapper<User>() {
                @Override
                public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                    User user = new User();
                    user.setId(rs.getString("id"));
                    user.setName(rs.getString("name"));
                    user.setPassword(rs.getString("password"));
                    user.setEmail(rs.getString("email"));
                    user.setLevel(Level.valueOf(rs.getInt("level")));
                    user.setLogin(rs.getInt("login"));
                    user.setRecommend(rs.getInt("recommend"));
                    return user;
                }
            };

}
