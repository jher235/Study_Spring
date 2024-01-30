package springbook.user.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import springbook.user.dao.statement.DeleteAllStatement;
import springbook.user.dao.statement.StatementStrategy;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.io.FileReader;
import java.sql.*;
import java.util.List;

public class UserDao {

//    private SimpleConnectionMaker simpleConnectionMaker;
//    private ConnectionMaker connectionMaker;

    private DataSource dataSource;      //스프링에서 제공하는 인터페이스 객체

    private JdbcContext jdbcContext;
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
//        this.dataSource = dataSource;
//        this.jdbcContext = new JdbcContext();       //jdbcContext 생성 (ioc)
//        this.jdbcContext.setDataSource(dataSource);     //의존 오브젝트 주입(di)
        this.jdbcTemplate = new JdbcTemplate(dataSource);   //jdbcTemplate 방식

    }

//    //직접 스프링 빈으로 등록하는 방식
//    public void setJdbcTemplate(DataSource dataSource){
//        this.jdbcTemplate = new JdbcTemplate(dataSource);
//    }


//    public void setJdbcContext(JdbcContext jdbcContext){
//        this.jdbcContext = jdbcContext;
//    }

//    public UserDao(ConnectionMaker connectionMaker) {     //생성자를 통한 주입
////        simpleConnectionMaker =  new  SimpleConnectionMaker();
//        this.connectionMaker = connectionMaker;
//
//    }

//    public void setConnectionMaker(ConnectionMaker connectionMaker) {       //수정자(메소드)를 통한 개별 주입
//        this.connectionMaker = connectionMaker;
//    }

//    private static PreparedStatement makeStatement(Connection c) throws SQLException {      //메소드 추출
//        PreparedStatement ps;
//        ps = c.prepareStatement("delete from users");
//        return ps;
////        return c.prepareStatement("delete from users");
//    }
//    abstract protected PreparedStatement makeStatement(Connection c) throws SQLException;       //템플릿 메소드 패턴을 위함

    public void add(final User user) throws ClassNotFoundException, SQLException {
//        this.jdbcContext.WithStatementStrategy(       //메소드 파라미터로 이전한 익명 내부 클래스를 사용
//        new StatementStrategy() {
//            @Override
//            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
//                PreparedStatement ps = c.prepareStatement(
//                        "insert into users(id, name, password) values(?,?,?)");
//                ps.setString(1, user.getId());
//                ps.setString(2, user.getName());
//                ps.setString(3, user.getPassword());
//
//                return ps;
//            }
//        });

        //JdbcTemplate을 사용하여 간단하게 변경, 내장 콜백
        this.jdbcTemplate.update("insert into users(id, name, password) values(?,?,?)", user.getId(), user.getName(), user.getPassword());

    }


    public User get(String id) throws ClassNotFoundException, SQLException {
//        return this.jdbcTemplate.queryForObject("select * from users where id = ? ",
//                new Object[]{id},
//                new RowMapper<User>() {
//                    @Override
//                    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
//                        User user = new User();
//                        user.setId(rs.getString("id"));
//                        user.setName(rs.getString("name"));
//                        user.setPassword(rs.getString("password"));
//                        return user;
//                    }
//                }
//        );
        //중복되는 부분 제거
        return this.jdbcTemplate.queryForObject("select * from users where id = ?",new Object[]{id},this.userMapper);



//
////        Connection c = getConnection();
////        Connection c = connectionMaker.makeConnection();
//        Connection c = dataSource.getConnection();
//
//        PreparedStatement ps = c.prepareStatement("select * from users where id = ? ");
//        ps.setString(1, id);
////        ps.execute();
//
//        ResultSet rs = ps.executeQuery();
//
//        User user = null;
//        if (rs.next()) {
//            user = new User();
//            user.setId(rs.getString("id"));
//            user.setName(rs.getString("name"));
//            user.setPassword(rs.getString("password"));
//        }
//
//        rs.close();
//        ps.close();
//        c.close();
//
//        if (user == null) throw new EmptyResultDataAccessException(1);
//
//        return user;
    }


    public void deleteAll() throws SQLException {
//        this.jdbcContext.executeSql("delete from users");

//        this.jdbcTemplate.update(   //JdbcTemplate적용. 템플릿으로부터 커넥션을 제공받아서 PreparedStatement를 만들어 돌려준다.
//                new PreparedStatementCreator() {    //PreparedStatementCreator()의 콜백을 받아서 update()가 사용함.
//                    @Override
//                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
//                        return con.prepareStatement("delete from users");
//                    }
//                }
//        );
        this.jdbcTemplate.update("delete from users");  //JdbcTemplate의 내장 콜백을 사용함.
    }


    public int getCount() throws SQLException {
//        return this.jdbcTemplate.query(new PreparedStatementCreator() {
//               @Override
//               public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
//                   return con.prepareStatement("select count(*) from users");
//               }
//           }, new ResultSetExtractor<Integer>() {
//               @Override
//               public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
//                   rs.next();
//                   return rs.getInt(1);
//               }
//           }
//        );
        //위 코드를 아래와 같이 변경 가능
        return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);



//        Connection c = null;
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//
//        try {
//            c = dataSource.getConnection();
//
//            ps = c.prepareStatement("select count(*) from users");
//            rs = ps.executeQuery();
//            rs.next();
//            return rs.getInt(1);
//        }catch (SQLException e){
//            throw e;
//        }finally {      //close()는 만들어진 순서의 반대로 하는 것이 원칙이다.
//            if(rs!=null){
//                try{
//                    rs.close();
//                }catch (SQLException e){
//                }
//            }
//            if(ps!=null){
//                try {
//                    ps.close();
//                }catch (SQLException e){
//                }
//            }
//            if(c!=null){
//                try {
//                    c.close();
//                }catch (SQLException e){
//                }
//            }
//
//        }
    }

    public List<User> getAll(){
//        return this.jdbcTemplate.query("select * from users order by id",
//                new RowMapper<User>() {     //ResultSet의 모든 로우를 열람하면서 로우마다 RowMapper 콜백을 호출한다.
//                    @Override
//                    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
//                        User user = new User();
//                        user.setId(rs.getString("id"));
//                        user.setName(rs.getString("name"));
//                        user.setPassword(rs.getString("password"));
//                        return user;
//                    }
//                }
//        );

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
                    return user;
                }
            };

//    private static Connection getConnection() throws ClassNotFoundException, SQLException {
//        Class.forName("com.mysql.cj.jdbc.Driver");
//        Connection c = DriverManager.getConnection(
//                "jdbc:mysql://localhost:3306/tobyspring3_1?serverTimezone=UTC","root","jher235");
//        return c;
//    }

//    public abstract Connection getConnection() throws ClassNotFoundException, SQLException;


}
