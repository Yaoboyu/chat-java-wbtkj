package com.wbtkj.chat.mapper;

import com.wbtkj.chat.pojo.model.User;
import com.wbtkj.chat.pojo.model.UserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface UserMapper {
    int countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    @Update("update user_info set quota = quota + #{val} where id = #{userId}")
    int addBanlance(long val, long userId);

    @Select("SELECT * from user_info where email like '%${email}%'")
    List<User> getUsers(String email);
}