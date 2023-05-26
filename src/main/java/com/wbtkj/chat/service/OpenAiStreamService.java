package com.wbtkj.chat.service;

import com.wbtkj.chat.pojo.dto.openai.billing.BillingUsage;
import com.wbtkj.chat.pojo.dto.openai.billing.Subscription;
import com.wbtkj.chat.pojo.dto.openai.chat.ChatCompletion;
import com.wbtkj.chat.pojo.dto.openai.chat.Message;
import com.wbtkj.chat.pojo.dto.openai.completions.Completion;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.List;

public interface OpenAiStreamService {

    /**
     * 问答接口 stream 形式
     *
     * @param completion          open ai 参数
     * @param eventSourceListener sse监听器
     */
    void streamCompletions(Completion completion, EventSourceListener eventSourceListener);

    /**
     * 问答接口-简易版
     *
     * @param question            请求参数
     * @param eventSourceListener sse监听器
     */
    void streamCompletions(String question, EventSourceListener eventSourceListener);

    /**
     * 流式输出，最新版的GPT-3.5 chat completion 更加贴近官方网站的问答模型
     *
     * @param chatCompletion      问答参数
     * @param eventSourceListener sse监听器
     */
    void streamChatCompletion(ChatCompletion chatCompletion, EventSourceListener eventSourceListener);

    /**
     * 流式输出，最新版的GPT-3.5 chat completion 更加贴近官方网站的问答模型
     *
     * @param messages            问答列表
     * @param eventSourceListener sse监听器
     */
    void streamChatCompletion(List<Message> messages, EventSourceListener eventSourceListener);

    /**
     * 账户信息查询：里面包含总金额等信息
     *
     * @return 个人账户信息
     */
    Subscription subscription();

    /**
     * 账户调用接口消耗金额信息查询
     * 最多查询100天
     *
     * @param starDate 开始时间
     * @param endDate  结束时间
     * @return
     */
    public BillingUsage billingUsage(@NotNull LocalDate starDate, @NotNull LocalDate endDate);
}