package springbook.user.dao;

public class AccountDao {
    private ConnectionMaker connectionMaker;

    public AccountDao(ConnectionMaker connectionMaker) {
//        simpleConnectionMaker =  new  SimpleConnectionMaker();
        this.connectionMaker = connectionMaker;

    }
}
