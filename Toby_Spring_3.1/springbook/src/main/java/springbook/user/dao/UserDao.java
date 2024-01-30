package springbook.user.dao;

import org.springframework.dao.EmptyResultDataAccessException;

import springbook.user.dao.statement.DeleteAllStatement;
import springbook.user.dao.statement.StatementStrategy;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.*;

public class UserDao {

//    private SimpleConnectionMaker simpleConnectionMaker;
//    private ConnectionMaker connectionMaker;

    private DataSource dataSource;      //스프링에서 제공하는 인터페이스 객체

    private JdbcContext jdbcContext;

    public void setDataSource(DataSource dataSource){
        this.dataSource = dataSource;
        this.jdbcContext = new JdbcContext();       //jdbcContext 생성 (ioc)
        this.jdbcContext.setDataSource(dataSource);     //의존 오브젝트 주입(di)

    }



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
        this.jdbcContext.WithStatementStrategy(       //메소드 파라미터로 이전한 익명 내부 클래스를 사용
        new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                PreparedStatement ps = c.prepareStatement(
                        "insert into users(id, name, password) values(?,?,?)");
                ps.setString(1, user.getId());
                ps.setString(2, user.getName());
                ps.setString(3, user.getPassword());

                return ps;
            }
        });


    }



    public User get(String id) throws ClassNotFoundException, SQLException{

//        Connection c = getConnection();
//        Connection c = connectionMaker.makeConnection();
        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement(
                "select * from users where id = ? ");
        ps.setString(1,id);
//        ps.execute();

        ResultSet rs = ps.executeQuery();

        User user = null;
        if (rs.next()){
            user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
        }

        rs.close();
        ps.close();
        c.close();

        if (user==null)throw new EmptyResultDataAccessException(1);

        return user;
    }


    public void deleteAll() throws SQLException {
        this.jdbcContext.WithStatementStrategy(
            new StatementStrategy() {
                @Override
                public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                    return c.prepareStatement("delete from users");
                }
            }
        );
    }


    public int getCount() throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            c = dataSource.getConnection();

            ps = c.prepareStatement("select count(*) from users");
            rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        }catch (SQLException e){
            throw e;
        }finally {      //close()는 만들어진 순서의 반대로 하는 것이 원칙이다.
            if(rs!=null){
                try{
                    rs.close();
                }catch (SQLException e){
                }
            }
            if(ps!=null){
                try {
                    ps.close();
                }catch (SQLException e){
                }
            }
            if(c!=null){
                try {
                    c.close();
                }catch (SQLException e){
                }
            }

        }
    }

//    private static Connection getConnection() throws ClassNotFoundException, SQLException {
//        Class.forName("com.mysql.cj.jdbc.Driver");
//        Connection c = DriverManager.getConnection(
//                "jdbc:mysql://localhost:3306/tobyspring3_1?serverTimezone=UTC","root","jher235");
//        return c;
//    }

//    public abstract Connection getConnection() throws ClassNotFoundException, SQLException;


}
