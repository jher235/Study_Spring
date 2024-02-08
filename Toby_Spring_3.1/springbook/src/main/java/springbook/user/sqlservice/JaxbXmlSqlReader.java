package springbook.user.sqlservice;

import springbook.user.dao.UserDao;
import springbook.user.sqlservice.jaxb.SqlType;
import springbook.user.sqlservice.jaxb.Sqlmap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

public class JaxbXmlSqlReader implements SqlReader{
    private static final String DEFAULT_SQLMAP_FILE = "sqlmap.xml";
    private String sqlmapFile = DEFAULT_SQLMAP_FILE;

//    public void setSqlmapFile(String sqlmapFile) { this.sqlmapFile = sqlmapFile; }

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
                sqlRegistry.registerSql(sql.getKey(), sql.getValue());
            }

        } catch (JAXBException e) {
            System.out.println("예외!!!!!!!!!!!!!!!!!!!");
            throw new RuntimeException(e);
        }

    }
}
