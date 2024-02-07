package springbook.learnTest.factorybean;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations ="/learnTest-context.xml")
class FactoryBeanTest {
    @Autowired
    ApplicationContext context;

    @Test
    public void getMessageFromFactoryBean(){
        Object message = context.getBean("message");
        assertThat(message).isInstanceOf(Message.class);
        assertThat(((Message)message).getText()).isEqualTo("Factory Bean");
    }


}