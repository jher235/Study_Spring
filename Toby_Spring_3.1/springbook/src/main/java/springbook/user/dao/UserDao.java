package springbook.user.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.*;

public class UserDao {

//    private SimpleConnectionMaker simpleConnectionMaker;
//    private ConnectionMaker connectionMaker;

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource){
        this.dataSource = dataSource;
    }


//    public UserDao(ConnectionMaker connectionMaker) {     //생성자를 통한 주입
////        simpleConnectionMaker =  new  SimpleConnectionMaker();
//        this.connectionMaker = connectionMaker;
//
//    }

//    public void setConnectionMaker(ConnectionMaker connectionMaker) {       //수정자(메소드)를 통한 개별 주입
//        this.connectionMaker = connectionMaker;
//    }

    public void add(User user) throws ClassNotFoundException, SQLException {
//        Connection c = getConnection();
//        Connection c = simpleConnectionMaker.makeNewConnection();
//        Connection c = connectionMaker.makeConnection();
        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement(
                "insert into users(id, name, password) values(?,?,?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();
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
        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement("delete from users");
        ps.executeUpdate();

        ps.close();
        c.close();
    }


    public int getCount() throws SQLException {
        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement("select count(*) from users");

        ResultSet rs = ps.executeQuery();
        rs.next();
        int count = rs.getInt(1);

        rs.close();
        c.close();
        ps.close();

        return count;
    }

//    private static Connection getConnection() throws ClassNotFoundException, SQLException {
//        Class.forName("com.mysql.cj.jdbc.Driver");
//        Connection c = DriverManager.getConnection(
//                "jdbc:mysql://localhost:3306/tobyspring3_1?serverTimezone=UTC","root","jher235");
//        return c;
//    }

//    public abstract Connection getConnection() throws ClassNotFoundException, SQLException;


}
