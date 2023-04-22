package com.wbtkj.chat.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LoginMapper {
    @Select("select salt from chat.userinfo where email = #{email}")
    String Salt(String email);
    @Select("SELECT pwd from chat.userinfo where email = #{email}")
    String Pwd(String email);

}
