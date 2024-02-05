package springbook.learnTest.factorybean;

public class Message {
    String text;
    private Message(String text){   //생성자가 private여서 외부에서 접근 불가.
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static Message newMessage(String text){      //생성자 대신 사용할 수 있는 스태틱 팩토리 메소드 제공, 스태틱 메소드여서 스프링 빈 등록 불가
        return new Message(text);
    }
}
