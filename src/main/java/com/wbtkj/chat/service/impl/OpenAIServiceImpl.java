package com.wbtkj.chat.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.knuddels.jtokkit.api.EncodingType;
import com.wbtkj.chat.api.OpenAIAPI;
import com.wbtkj.chat.config.StaticContextAccessor;
import com.wbtkj.chat.exception.MyException;
import com.wbtkj.chat.exception.MyServiceException;
import com.wbtkj.chat.filter.OpenAiAuthInterceptor;
import com.wbtkj.chat.pojo.dto.openai.billing.BillingUsage;
import com.wbtkj.chat.pojo.dto.openai.billing.Subscription;
import com.wbtkj.chat.pojo.dto.openai.chat.ChatCompletion;
import com.wbtkj.chat.pojo.dto.openai.chat.Message;
import com.wbtkj.chat.pojo.dto.openai.completions.Completion;
import com.wbtkj.chat.pojo.dto.openai.embeddings.Embedding;
import com.wbtkj.chat.pojo.dto.openai.embeddings.EmbeddingResponse;
import com.wbtkj.chat.pojo.dto.openai.embeddings.TextAndEmbedding;
import com.wbtkj.chat.service.OpenAIService;
import com.wbtkj.chat.service.ThirdPartyModelKeyService;
import com.wbtkj.chat.utils.TikTokensUtil;
import io.reactivex.Single;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 * 描述： open ai 客户端
 *
 * @author https:www.unfbx.com
 * 2023-02-28
 */

@Slf4j
@Getter
@Service
public class OpenAIServiceImpl implements OpenAIService {

    private String apiHost;
    /**
     * 自定义的okHttpClient
     */
    private OkHttpClient okHttpClient;

    private OpenAIAPI openAIAPI;

    /**
     * 自定义鉴权处理拦截器
     */
    private OpenAiAuthInterceptor openAiAuthInterceptor;

    @Resource
    ThirdPartyModelKeyService thirdPartyModelKeyService;


    /**
     * 构造实例对象
     */
    public OpenAIServiceImpl(@Value("${chatgpt.apiHost}") String apiHost) {
        this.openAiAuthInterceptor = StaticContextAccessor.getBean(OpenAiAuthInterceptor.class);

        this.apiHost = apiHost;

        this.okHttpClient = new OkHttpClient
                .Builder()
                .addInterceptor(openAiAuthInterceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(50, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS)
                .build();


        this.openAIAPI = new Retrofit.Builder()
                .baseUrl(apiHost)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build().create(OpenAIAPI.class);
    }


    public void streamCompletions(Completion completion, EventSourceListener eventSourceListener) {
        if (Objects.isNull(eventSourceListener)) {
            log.error("参数异常：EventSourceListener不能为空，可以参考：com.unfbx.chatgpt.sse.ConsoleEventSourceListener");
            throw new MyException();
        }
        if (StrUtil.isBlank(completion.getPrompt())) {
            log.error("参数异常：Prompt不能为空");
            throw new MyServiceException("参数异常：Prompt不能为空");
        }
        if (!completion.isStream()) {
            completion.setStream(true);
        }
        try {
            EventSource.Factory factory = EventSources.createFactory(this.okHttpClient);
            ObjectMapper mapper = new ObjectMapper();
            String requestBody = mapper.writeValueAsString(completion);
            Request request = new Request.Builder()
                    .url(this.apiHost + "v1/completions")
                    .post(RequestBody.create(requestBody, MediaType.parse(ContentType.JSON.getValue())))
                    .build();
            //创建事件
            EventSource eventSource = factory.newEventSource(request, eventSourceListener);
        } catch (JsonProcessingException e) {
            log.error("请求参数解析异常：{}", e);
            e.printStackTrace();
        } catch (Exception e) {
            log.error("请求参数解析异常：{}", e);
            e.printStackTrace();
        }
    }


    public void streamCompletions(String question, EventSourceListener eventSourceListener) {
        Completion q = Completion.builder()
                .prompt(question)
                .stream(true)
                .build();
        this.streamCompletions(q, eventSourceListener);
    }


    public void streamChatCompletion(ChatCompletion chatCompletion, EventSourceListener eventSourceListener) {
        if (Objects.isNull(eventSourceListener)) {
            log.error("参数异常：EventSourceListener不能为空，可以参考：com.unfbx.chatgpt.sse.ConsoleEventSourceListener");
            throw new MyException();
        }
        try {
            EventSource.Factory factory = EventSources.createFactory(this.okHttpClient);
            ObjectMapper mapper = new ObjectMapper();
            String requestBody = mapper.writeValueAsString(chatCompletion);
            Request request = new Request.Builder()
                    .url(this.apiHost + "v1/chat/completions")
                    .post(RequestBody.create(requestBody, MediaType.parse(ContentType.JSON.getValue())))
                    .addHeader("model", chatCompletion.getModel())
                    .build();
            //创建事件
            EventSource eventSource = factory.newEventSource(request, eventSourceListener);
        } catch (Exception e) {
            log.error("请求参数解析异常：{}", e);
            e.printStackTrace();
            throw new MyException();
        }
    }


    public void streamChatCompletion(List<Message> messages, EventSourceListener eventSourceListener) {
        ChatCompletion chatCompletion = ChatCompletion.builder()
                .messages(messages)
                .stream(true)
                .build();
        this.streamChatCompletion(chatCompletion, eventSourceListener);
    }

    public Subscription subscription() {
        Single<Subscription> subscription = this.openAIAPI.subscription();
        return subscription.blockingGet();
    }

    public BillingUsage billingUsage(@NotNull LocalDate starDate, @NotNull LocalDate endDate) {
        Single<BillingUsage> billingUsage = this.openAIAPI.billingUsage(starDate, endDate);
        return billingUsage.blockingGet();
    }

    public EmbeddingResponse embeddings(String input) {
        List<String> inputs = new ArrayList<>(1);
        inputs.add(input);
        Embedding embedding = Embedding.builder().input(inputs).build();
        return this.embeddings(embedding);
    }

    public List<TextAndEmbedding> embeddings(List<String> input) {
        List<TextAndEmbedding> result = new ArrayList<>();
        int queryLen = 0;
        int startIndex = 0;
        int tokens = 0;

        for (int index = 0; index < input.size(); index++) {
            queryLen += TikTokensUtil.tokens(EncodingType.CL100K_BASE, input.get(index));
            if (queryLen > 8192 - 1024) {
                Embedding embedding = Embedding.builder().input(input.subList(startIndex, index + 1)).build();
                EmbeddingResponse embeddingResponse = this.embeddings(embedding);
                for (int i = startIndex; i < index + 1; i++) {
                    result.add(TextAndEmbedding.builder()
                            .text(input.get(i))
                            .embedding(embeddingResponse.getData().get(i-startIndex).getEmbedding())
                            .build());
                }
                queryLen = 0;
                startIndex = index + 1;
                tokens += embeddingResponse.getUsage().getTotalTokens();
            }
        }
        if (queryLen > 0) {
            Embedding embedding = Embedding.builder().input(input.subList(startIndex, input.size())).build();
            EmbeddingResponse embeddingResponse = this.embeddings(embedding);
            for (int i = startIndex; i < input.size(); i++) {
                result.add(TextAndEmbedding.builder()
                        .text(input.get(i))
                        .embedding(embeddingResponse.getData().get(i-startIndex).getEmbedding())
                        .build());
            }
            tokens += embeddingResponse.getUsage().getTotalTokens();
        }

        return result;
    }

    public EmbeddingResponse embeddings(Embedding embedding) {
        Single<EmbeddingResponse> embeddings = this.openAIAPI.embeddings(embedding);
        return embeddings.blockingGet();
    }
}
