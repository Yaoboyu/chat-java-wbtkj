package com.wbtkj.chat.pojo.dto.openai.embeddings;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class TextAndEmbedding {
    private String text;
    private List<BigDecimal> embedding;
}
