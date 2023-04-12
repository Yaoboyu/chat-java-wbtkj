package com.wbtkj.wbt.Mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

@Mapper
public interface UserMapper {
    @Select("select count(*) from wbt.userinfo")
    int UserNumber();
    @Insert("INSERT INTO wbt.userinfo(email, pwd, salt, RegDate) VALUES(#{email},#{pwd},#{salt},#{date})")
    void InsertUser(String email, String pwd, String salt, Date date);
    @Select("select count(*) from wbt.userinfo where email = #{email}")
    int Count(String email);
}
