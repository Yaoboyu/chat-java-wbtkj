package com.wbtkj.chat.mapper;

import com.wbtkj.chat.pojo.model.FileEmbedding;
import com.wbtkj.chat.pojo.model.FileEmbeddingExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;

import java.math.BigDecimal;
import java.util.List;

public interface FileEmbeddingMapperCopy {

    List<String> getTextsByCosineDistance(List<String> fileNames, List<BigDecimal> embedding, int limit);

//    <select id="getTextsByCosineDistance" resultType="java.lang.String">
//    SELECT text
//    FROM file_embedding
//    WHERE name IN
//            <foreach item="fileName" collection="fileNames" open="(" separator="," close=")">
//            #{fileName}
//    </foreach>
//    ORDER BY embedding &lt;-&gt; #{embedding,jdbcType=OTHER,typeHandler=com.wbtkj.chat.mapper.typeHandler.PGVectorTypeHandler}
//    LIMIT #{limit}
//  </select>
}