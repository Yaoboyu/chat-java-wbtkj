package com.wbtkj.chat.pojo.dto.openai.chat;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wbtkj.chat.utils.TikTokensUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 描述： chat
 *
 * @author https:www.unfbx.com
 * @since 2023-03-02
 */
@Data
@Builder
@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class ChatCompletion implements Serializable {

    @NonNull
    @Builder.Default
    private String model = Model.DEFAULT_3_5.getName();
    /**
     * 问题描述
     */
    @NonNull
    private List<Message> messages;
    /**
     * 使用什么取样温度，0到2之间。较高的值(如0.8)将使输出更加随机，而较低的值(如0.2)将使输出更加集中和确定。
     * <p>
     * We generally recommend altering this or but not both.top_p
     */
    @Builder.Default
    private double temperature = 0.2;

    /**
     * 使用温度采样的替代方法称为核心采样，其中模型考虑具有top_p概率质量的令牌的结果。因此，0.1 意味着只考虑包含前 10% 概率质量的代币。
     * <p>
     * 我们通常建议更改此设置，但不要同时更改两者。temperature
     */
    @JsonProperty("top_p")
    @Builder.Default
    private Double topP = 1d;


    /**
     * 为每个提示生成的完成次数。
     */
    @Builder.Default
    private Integer n = 1;


    /**
     * 是否流式输出.
     * default:false
     *
     * @see com.unfbx.chatgpt.OpenAiStreamClient
     */
    @Builder.Default
    private boolean stream = false;
    /**
     * 停止输出标识
     */
    private List<String> stop;
    /**
     * 最大支持4096
     */
    @JsonProperty("max_tokens")
    @Builder.Default
    private Integer maxTokens = 2048;


    @JsonProperty("presence_penalty")
    @Builder.Default
    private double presencePenalty = 0;

    /**
     * -2.0 ~~ 2.0
     */
    @JsonProperty("frequency_penalty")
    @Builder.Default
    private double frequencyPenalty = 0;

    @JsonProperty("logit_bias")
    private Map logitBias;
    /**
     * 用户唯一值，确保接口不被重复调用
     */
    private String user;

    /**
     * 获取当前参数的tokens数
     */
    public long tokens() {
        if (CollectionUtil.isEmpty(this.messages) || StrUtil.isBlank(this.model)) {
            log.warn("参数异常model：{}，prompt：{}", this.model, this.messages);
            return 0;
        }
        return TikTokensUtil.tokens(this.model, this.messages);
    }

    public int maxTokenLen() {
        if (model.equals("gpt-3.5-turbo")) {
            return 4096;
        } else if (model.equals("gpt-3.5-turbo-16k")) {
            return 16384;
        } else if (model.equals("gpt-4")) {
            return 8192;
        } else if (model.equals("gpt-4-32k")) {
            return 32768;
        } else {
            return -1;
        }
    }


    @Getter
    @AllArgsConstructor
    public enum Model {
        DEFAULT_3_5("gpt-3.5-turbo-16k"),
        DEFAULT_4("gpt-4"),
        /**
         * gpt-3.5-turbo
         */
        GPT_3_5_TURBO("gpt-3.5-turbo"),
        /**
         * gpt-3.5-turbo-16k
         */
        GPT_3_5_TURBO_16K("gpt-3.5-turbo-16k"),
        /**
         * GPT4.0
         */
        GPT_4("gpt-4"),
        /**
         * GPT4.0 超长上下文
         */
        GPT_4_32K("gpt-4-32k"),
        ;
        private String name;
    }

}


