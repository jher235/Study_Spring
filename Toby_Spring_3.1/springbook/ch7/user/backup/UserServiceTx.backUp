package springbook.user.service;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import springbook.user.domain.User;

//비지니스로직X, 트랜젝션 경계 설정만 함.
public class UserServiceTx implements UserService{

    UserService userService;
    PlatformTransactionManager transactionManager;

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void setUserService(UserService userService){
        this.userService = userService;     //UserService를 구현한 다른 오브젝트를 di받는다.
    }

    @Override
    public void add(User user) {
        userService.add(user);  //di받은 다른 오브젝트에게 모든 기능을 위임한다.
    }

    @Override
    public void upgradeLevels() {
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
        try{
            userService.upgradeLevels();

            this.transactionManager.commit(status);
        }catch (RuntimeException e){
            this.transactionManager.rollback(status);
            throw e;
        }

    }

}
