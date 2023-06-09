package com.wbtkj.chat.mapper;

import com.wbtkj.chat.pojo.model.UserFile;
import com.wbtkj.chat.pojo.model.UserFileExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface UserFileMapper {
    int countByExample(UserFileExample example);

    int deleteByExample(UserFileExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UserFile record);

    int insertSelective(UserFile record);

    List<UserFile> selectByExampleWithRowbounds(UserFileExample example, RowBounds rowBounds);

    List<UserFile> selectByExample(UserFileExample example);

    UserFile selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UserFile record, @Param("example") UserFileExample example);

    int updateByExample(@Param("record") UserFile record, @Param("example") UserFileExample example);

    int updateByPrimaryKeySelective(UserFile record);

    int updateByPrimaryKey(UserFile record);
}