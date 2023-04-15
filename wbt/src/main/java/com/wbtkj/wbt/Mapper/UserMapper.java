package com.wbtkj.wbt.Mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;

@Mapper
public interface UserMapper {
    @Select("select count(*) from wbt.user_info")
    int UserNumber();
    @Insert("INSERT INTO wbt.user_info(email, pwd, salt, RegDate) VALUES(#{email},#{pwd},#{salt},#{date})")
    void InsertUser(String email, String pwd, String salt, Date date);
    @Select("select count(*) from wbt.user_info where email = #{email}")
    int Count(String email);
    @Select("select salt from wbt.user_info where email = #{email}")
    String Salt(String email);
    @Select("SELECT pwd from wbt.user_info where email = #{email}")
    String Pwd(String email);
    @Update("UPDATE wbt.user_info SET wbt.user_info.pwd = #{pwd} WHERE email = #{email}")
    void UpdatePwd(String pwd,String email);
    @Update("UPDATE wbt.user_info set wbt.user_info.status = #{status} where email = #{email}")
    void updateStatus(String email,int status);
    @Update("update wbt.user_info set wbt.user_info.quota = quota + #{value} and id = #{id}")
    void addQuota(int value,int id);
}
