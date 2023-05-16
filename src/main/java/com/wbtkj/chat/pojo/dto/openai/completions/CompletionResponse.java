package com.wbtkj.chat.pojo.dto.openai.completions;

import com.wbtkj.chat.pojo.dto.openai.common.Choice;
import com.wbtkj.chat.pojo.dto.openai.common.OpenAiResponse;
import com.wbtkj.chat.pojo.dto.openai.common.Usage;
import lombok.Data;

import java.io.Serializable;

/**
 * 描述： 答案类
 *
 * @author https:www.unfbx.com
 *  2023-02-11
 */
@Data
public class CompletionResponse extends OpenAiResponse implements Serializable {
    private String id;
    private String object;
    private long created;
    private String model;
    private Choice[] choices;
    private Usage usage;
}
