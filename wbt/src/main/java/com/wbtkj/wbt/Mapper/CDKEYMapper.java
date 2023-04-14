package com.wbtkj.wbt.Mapper;

import cn.hutool.core.date.DateTime;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CDKEYMapper {
    @Insert("insert into wbt.cdkey(user_id, value, date, used,code) VALUES (#{id},#{value},#{datetime},true,#{code})")
    void activate(Long id, int value, DateTime datetime,String code);
    @Select("select count(*) from wbt.cdkey where code = #{code}")
    int count(String code);
}
