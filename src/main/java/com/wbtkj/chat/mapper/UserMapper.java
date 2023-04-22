package com.wbtkj.chat.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;

@Mapper
public interface UserMapper {
    @Select("select id from user where email = #{email}")
    Long id(String email);;
    @Select("select count(*) from user")
    Integer userCount();
    @Insert("INSERT INTO user(email, pwd, salt, create_time, update_time, status) VALUES(#{email},#{pwd},#{salt},#{createTime},#{updateTime},#{status})")
    void insertUser(String email, String pwd, String salt, Date createTime, Date updateTime, int status);
    @Select("select count(*) from user where email = #{email}")
    Integer count(String email);
    @Select("select salt from user where email = #{email}")
    String salt(String email);
    @Select("SELECT pwd from user where email = #{email}")
    String pwd(String email);
    @Update("UPDATE user SET user.pwd = #{pwd} WHERE email = #{email}")
    void UpdatePwd(String pwd,String email);
    @Update("UPDATE user set user.status = #{status} where email = #{email}")
    void updateStatus(String email,int status);
    @Update("update user set user.quota = quota + #{value} and id = #{id}")
    void addQuota(int value,int id);
}
