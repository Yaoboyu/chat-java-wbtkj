package com.wbtkj.wbt.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LoginMapper {
    @Select("select salt from wbt.userinfo where email = #{email}")
    String Salt(String email);
    @Select("SELECT pwd from wbt.userinfo where email = #{email}")
    String Pwd(String email);

}
