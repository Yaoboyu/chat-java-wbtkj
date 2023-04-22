package com.wbtkj.chat.mapper;

import cn.hutool.core.date.DateTime;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CDKEYMapper {
    @Insert("insert into chat.cdkey(user_id, value, date, used,code) VALUES (#{id},#{value},#{datetime},true,#{code})")
    void activate(Long id, int value, DateTime datetime,String code);
    @Select("select count(*) from chat.cdkey where code = #{code}")
    int count(String code);
}
