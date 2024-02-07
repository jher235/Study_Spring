package springbook.user.dao;

import springbook.user.domain.User;

import java.util.ArrayList;
import java.util.List;

public class MockUserDao implements UserDao{    //getAll에 대해서는 스텁으로서, update에 대해서는 목 오브젝트로서 동작하는 UserDao 타입의 테스트 대역
    private List<User> users;   //레벨 업그레이드 후보 User목록
    private List<User> updated = new ArrayList();   //업그레이드 대상 오브젝트를 저장해둘 곳

    public MockUserDao(List<User> users){
        this.users = users;
    }

    public List<User> getUpdated(){
        return this.updated;
    }

    @Override
    public List<User> getAll() {    //스텁 기능 제공
        return this.users;
    }

    @Override
    public void update(User user) { //목 오브젝트 기능 제공
        updated.add(user);
    }

    //아래는 테스트에 사용하지 않을 것이므로 throw new UnsupportedOperationException();을 던지게 해서 지원하지 않는 기능이라는 예외를 발생시킴
    @Override
    public void add(User user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public User get(String id) {
        throw new UnsupportedOperationException();
    }



    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getCount() {
        throw new UnsupportedOperationException();
    }


}
