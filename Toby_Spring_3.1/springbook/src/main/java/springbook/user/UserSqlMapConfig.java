package springbook.user;

import org.springframework.core.io.ClassPathResource;
import springbook.user.dao.UserDao;

import javax.annotation.Resource;

public class UserSqlMapConfig implements SqlmapConfig{
    @Override
    public org.springframework.core.io.Resource getSqlMapResource() {
        return new ClassPathResource("sqlmap.xml", UserDao.class);
    }
}
