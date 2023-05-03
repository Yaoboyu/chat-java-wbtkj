package com.wbtkj.chat.mapper;

import com.wbtkj.chat.pojo.model.Openaikey;
import com.wbtkj.chat.pojo.model.OpenaikeyExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OpenaikeyMapper {
    int countByExample(OpenaikeyExample example);

    int deleteByExample(OpenaikeyExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Openaikey record);

    int insertSelective(Openaikey record);

    List<Openaikey> selectByExample(OpenaikeyExample example);

    Openaikey selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Openaikey record, @Param("example") OpenaikeyExample example);

    int updateByExample(@Param("record") Openaikey record, @Param("example") OpenaikeyExample example);

    int updateByPrimaryKeySelective(Openaikey record);

    int updateByPrimaryKey(Openaikey record);
}