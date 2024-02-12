package springbook.user.sqlservice;

//sqlregistry오브젝트를 메소드 파라미터로 di받아서 읽어들인 sql을 등록하는데 사용하도록 함
public interface SqlReader {        //sql을 읽어서 sqlregistry에 전달하는 역할
    void read(SqlRegistry sqlRegistry);
}
