package springbook.user.sqlservice;

import org.springframework.beans.factory.annotation.Autowired;
import springbook.user.dao.UserDao;
import springbook.user.sqlservice.jaxb.SqlType;
import springbook.user.sqlservice.jaxb.Sqlmap;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class XmlSqlService implements SqlService, SqlRegistry, SqlReader{
    private Map<String, String> sqlMap = new HashMap<String, String >();

    private String sqlmapFile;

    private SqlReader sqlReader;
    private SqlRegistry sqlRegistry;

    public void setSqlReader(SqlReader sqlReader) {
        this.sqlReader = sqlReader;
    }

    public void setSqlRegistry(SqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }

    public void setSqlmapFile(String sqlmapFile) {
        this.sqlmapFile = sqlmapFile;
    }

    @PostConstruct
    public void loadSql(){
        this.sqlReader.read(this.sqlRegistry);
//        String contextPath = Sqlmap.class.getPackage().getName();
//        try {
//            JAXBContext context = JAXBContext.newInstance(contextPath);
//            Unmarshaller unmarshaller = context.createUnmarshaller();
//            System.out.println("*********+++++++++++"+this.sqlmapFile);
////            InputStream is = UserDao.class.getResourceAsStream("sqlmap.xml");
//            InputStream is = UserDao.class.getResourceAsStream(this.sqlmapFile);
//            System.out.println("*********+++++++++++"+is);
//            System.out.println("*********+++++++++++"+UserDao.class.getResourceAsStream("UserDao.java"));
////            InputStream is = this.getClass().getResourceAsStream(this.sqlmapFile);
//            Sqlmap sqlmap = (Sqlmap)unmarshaller.unmarshal(is);
//
//            for (SqlType sql: sqlmap.getSql()){
//                sqlMap.put(sql.getKey(), sql.getValue());
//            }
//
//        } catch (JAXBException e) {
//            System.out.println("예외!!!!!!!!!!!!!!!!!!!");
//            throw new RuntimeException(e);
//        }

    }

//    public XmlSqlService(){
//        String contextPath = Sqlmap.class.getPackage().getName();
//        try {
//            JAXBContext context = JAXBContext.newInstance(contextPath);
//            Unmarshaller unmarshaller = context.createUnmarshaller();
//            InputStream is = UserDao.class.getResourceAsStream("sqlmap.xml");
//            Sqlmap sqlmap = (Sqlmap)unmarshaller.unmarshal(is);
//
//            for (SqlType sql: sqlmap.getSql()){
//                sqlMap.put(sql.getKey(), sql.getValue());
//            }
//
//        } catch (JAXBException e) {
//            throw new RuntimeException(e);
//        }
//
//    }


    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        try {
            return this.sqlRegistry.findSql(key);
        }catch (SqlNotFoundException e){
            throw new SqlRetrievalFailureException(e);
        }
//        String sql = sqlMap.get(key);
//        if (sql==null) throw new SqlRetrievalFailureException(key+"에 대한 SQL을 찾을 수 없습니다.");
//        else return sql;

    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        String sql = sqlMap.get(key);
        if(sql==null) throw new SqlNotFoundException(key + "에 대한 정보를 찾을 수 없습니다");
        else  return sql;
    }

    @Override
    public void registerSql(String key, String sql) {
        sqlMap.put(key, sql);
    }


    @Override
    public void read(SqlRegistry sqlRegistry) {
        String contextPath = Sqlmap.class.getPackage().getName();
        try {
            JAXBContext context = JAXBContext.newInstance(contextPath);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            System.out.println("*********+++++++++++"+this.sqlmapFile);
            InputStream is = UserDao.class.getResourceAsStream(this.sqlmapFile);
//            InputStream is = this.getClass().getResourceAsStream("/resources/sqlmap.xml");
            System.out.println("*********+++++++++++"+is);
            System.out.println("*********+++++++++++"+this.getClass().getResourceAsStream("/resources/main/sqlmap.xml"));
            System.out.println("*********+++++++++++"+UserDao.class.getResourceAsStream("UserDao.class"));
//            Sqlmap sqlmap = (Sqlmap)unmarshaller.unmarshal(is);
            Sqlmap sqlmap = (Sqlmap)unmarshaller.unmarshal(is);

            for (SqlType sql: sqlmap.getSql()){
                sqlMap.put(sql.getKey(), sql.getValue());
            }

        } catch (JAXBException e) {
            System.out.println("예외!!!!!!!!!!!!!!!!!!!");
            throw new RuntimeException(e);
        }

    }

}
