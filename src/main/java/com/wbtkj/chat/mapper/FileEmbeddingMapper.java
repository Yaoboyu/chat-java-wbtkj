package com.wbtkj.chat.mapper;

import com.wbtkj.chat.pojo.model.FileEmbedding;
import com.wbtkj.chat.pojo.model.FileEmbeddingExample;

import java.math.BigDecimal;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;

public interface FileEmbeddingMapper {

    List<String> getTextsByCosineDistance(@Param("fileNames") List<String> fileNames, @Param("embedding") List<BigDecimal> embedding, @Param("limit") Integer limit);

    int countByExample(FileEmbeddingExample example);

    int deleteByExample(FileEmbeddingExample example);

    int deleteByPrimaryKey(Long id);

    int insert(FileEmbedding record);

    int insertSelective(FileEmbedding record);

    List<FileEmbedding> selectByExampleWithRowbounds(FileEmbeddingExample example, RowBounds rowBounds);

    List<FileEmbedding> selectByExample(FileEmbeddingExample example);

    FileEmbedding selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") FileEmbedding record, @Param("example") FileEmbeddingExample example);

    int updateByExample(@Param("record") FileEmbedding record, @Param("example") FileEmbeddingExample example);

    int updateByPrimaryKeySelective(FileEmbedding record);

    int updateByPrimaryKey(FileEmbedding record);
}