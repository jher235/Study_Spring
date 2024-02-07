package springbook.user.service;

import org.springframework.transaction.annotation.Transactional;
import springbook.user.domain.User;

import java.util.List;

@Transactional  //<tx:method name="*/>와 같은 설정 효과
public interface UserService {
    void add(User user);
    void upgradeLevels();
    void deleteAll();
    void upgrade(User user);

    @Transactional(readOnly = true)
    User get(String id);
    @Transactional(readOnly = true)
    List<User> getAll();

}
