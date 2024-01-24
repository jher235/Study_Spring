package springbook.user.dao;

import springbook.user.domain.User;

import java.sql.*;

public class UserDao {

//    private SimpleConnectionMaker simpleConnectionMaker;
    private ConnectionMaker connectionMaker;

    public UserDao(ConnectionMaker connectionMaker) {
//        simpleConnectionMaker =  new  SimpleConnectionMaker();
        this.connectionMaker = connectionMaker;

    }



    public void add(User user) throws ClassNotFoundException, SQLException {
//        Connection c = getConnection();
//        Connection c = simpleConnectionMaker.makeNewConnection();
        Connection c = connectionMaker.makeConnection();

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
        Connection c = connectionMaker.makeConnection();

        PreparedStatement ps = c.prepareStatement(
                "select * from users where id = ? ");
        ps.setString(1,id);
//        ps.execute();

        ResultSet rs = ps.executeQuery();
        rs.next();

        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();
        c.close();


        return user;
    }

//    private static Connection getConnection() throws ClassNotFoundException, SQLException {
//        Class.forName("com.mysql.cj.jdbc.Driver");
//        Connection c = DriverManager.getConnection(
//                "jdbc:mysql://localhost:3306/tobyspring3_1?serverTimezone=UTC","root","jher235");
//        return c;
//    }

//    public abstract Connection getConnection() throws ClassNotFoundException, SQLException;


}
