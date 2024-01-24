package springbook.user.dao;

public class DaoFactory {
    public UserDao userDao(){

        return new UserDao(connectionMaker());
    }


    public AccountDao accountDao(){
        return new AccountDao(connectionMaker());
    }

    public ConnectionMaker connectionMaker() {
        return new NConnectionMaker();
    }
}
