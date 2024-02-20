package org.zerock.springex.mapper;

import org.apache.ibatis.annotations.Select;

//현재 시간을 처리하는 TimeMapper 인터페이스
public interface TimeMapper {

    @Select("select now()")
    String getTime();
}
