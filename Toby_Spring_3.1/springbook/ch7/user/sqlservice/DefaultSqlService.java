package springbook.user.sqlservice;

public class DefaultSqlService extends BaseSqlService{  //BaseSqlService를 상속해서 원래 기능을 쓰며 의존성을 이곳에서 주입함.
    public DefaultSqlService(){
        setSqlReader(new JaxbXmlSqlReader());
        setSqlRegistry(new HashMapSqlRegister());
    }
}
