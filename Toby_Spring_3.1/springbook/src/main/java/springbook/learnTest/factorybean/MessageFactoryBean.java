package springbook.learnTest.factorybean;

import org.springframework.beans.factory.FactoryBean;

public class MessageFactoryBean implements FactoryBean<Message> {
    String text;

    public void setText(String text) {
        this.text = text;
    }

    public Message getObject() throws Exception{
        return Message.newMessage(this.text);
    }

    public Class<? extends Message> getObjectType(){
        return Message.class;
    }

    public boolean isSingleton(){
        return false;       //getObject가 매번 같은 오브젝트를 리턴하진 않음.
    }

}
