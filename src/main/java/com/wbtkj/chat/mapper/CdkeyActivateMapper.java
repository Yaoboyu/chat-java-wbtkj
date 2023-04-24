package com.wbtkj.chat.mapper;

import com.wbtkj.chat.pojo.model.CdkeyActivate;
import com.wbtkj.chat.pojo.model.CdkeyActivateExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CdkeyActivateMapper {
    int countByExample(CdkeyActivateExample example);

    int deleteByExample(CdkeyActivateExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CdkeyActivate record);

    int insertSelective(CdkeyActivate record);

    List<CdkeyActivate> selectByExample(CdkeyActivateExample example);

    CdkeyActivate selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CdkeyActivate record, @Param("example") CdkeyActivateExample example);

    int updateByExample(@Param("record") CdkeyActivate record, @Param("example") CdkeyActivateExample example);

    int updateByPrimaryKeySelective(CdkeyActivate record);

    int updateByPrimaryKey(CdkeyActivate record);
}