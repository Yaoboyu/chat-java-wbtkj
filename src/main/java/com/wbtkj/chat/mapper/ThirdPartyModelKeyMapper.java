package com.wbtkj.chat.mapper;

import com.wbtkj.chat.pojo.model.ThirdPartyModelKey;
import com.wbtkj.chat.pojo.model.ThirdPartyModelKeyExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface ThirdPartyModelKeyMapper {
    int countByExample(ThirdPartyModelKeyExample example);

    int deleteByExample(ThirdPartyModelKeyExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ThirdPartyModelKey record);

    int insertSelective(ThirdPartyModelKey record);

    List<ThirdPartyModelKey> selectByExampleWithRowbounds(ThirdPartyModelKeyExample example, RowBounds rowBounds);

    List<ThirdPartyModelKey> selectByExample(ThirdPartyModelKeyExample example);

    ThirdPartyModelKey selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ThirdPartyModelKey record, @Param("example") ThirdPartyModelKeyExample example);

    int updateByExample(@Param("record") ThirdPartyModelKey record, @Param("example") ThirdPartyModelKeyExample example);

    int updateByPrimaryKeySelective(ThirdPartyModelKey record);

    int updateByPrimaryKey(ThirdPartyModelKey record);
}