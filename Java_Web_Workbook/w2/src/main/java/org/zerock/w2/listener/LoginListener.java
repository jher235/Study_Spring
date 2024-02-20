package org.zerock.w2.listener;


import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionAttributeListener;
import jakarta.servlet.http.HttpSessionBindingEvent;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;

@WebListener
@Log4j2
public class LoginListener implements HttpSessionAttributeListener {
    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {

        String name = event.getName();
        Object object = event.getValue();

        if(name.equals("loginInfo")){
            log.info("A user logined........");
            log.info(object);
        }

    }
}
