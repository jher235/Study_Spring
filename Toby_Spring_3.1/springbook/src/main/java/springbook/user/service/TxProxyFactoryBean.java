package springbook.user.service;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Proxy;

public class TxProxyFactoryBean implements FactoryBean<Object> {
    Object target;
    PlatformTransactionManager transactionManager;
    String pattern;
    Class<?> serviceInterface;

    public void setTarget(Object target) {
        this.target = target;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void setServiceInterface(Class<?> serviceInterface) {
        this.serviceInterface = serviceInterface;
    }




    //FactoryBean 인터페이스 구현 메소드
    @Override
    public Object getObject() throws Exception {    //di받은걸 이용해서 TransactionHandler를 사용하는 다이내믹 프록시 생성
        TransactionHandler txHandler = new TransactionHandler();
        txHandler.setTarget(target);
        txHandler.setTransactionManager(transactionManager);
        txHandler.setPattern(pattern);
        return Proxy.newProxyInstance(
                getClass().getClassLoader(), new Class[]{serviceInterface},txHandler );

    }

    @Override
    public Class<?> getObjectType() {   //팩터리 빈이 생성하는 오브젝트의 타입은 di받은 인터페이스 타입에 따라 달라진다. 재사용 가능.
        return serviceInterface;
    }

    @Override
    public boolean isSingleton() {
        return false;   //getObject가 매번 같은 오브젝트를 리턴하진 않음.
    }
}
