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
import com.wbtkj.chat.mapper.FileEmbeddingMapper;
import com.wbtkj.chat.pojo.dto.openai.billing.BillingUsage;
import com.wbtkj.chat.pojo.dto.openai.billing.Subscription;
import com.wbtkj.chat.pojo.dto.openai.chat.ChatCompletion;
import com.wbtkj.chat.pojo.dto.openai.chat.ChatCompletionResponse;
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
import lombok.NonNull;
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
import java.net.SocketTimeoutException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
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
    @Resource
    FileEmbeddingMapper fileEmbeddingMapper;


    /**
     * 构造实例对象
     */
    public OpenAIServiceImpl(@Value("${chatgpt.apiHost}") String apiHost) {
        this.openAiAuthInterceptor = StaticContextAccessor.getBean(OpenAiAuthInterceptor.class);

        this.apiHost = apiHost;

        this.okHttpClient = new OkHttpClient
                .Builder()
                .addInterceptor(openAiAuthInterceptor)
                .connectTimeout(300, TimeUnit.SECONDS)
                .writeTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .build();


        this.openAIAPI = new Retrofit.Builder()
                .baseUrl(apiHost)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build().create(OpenAIAPI.class);
    }


    public void streamChatCompletion(ChatCompletion chatCompletion, EventSourceListener eventSourceListener) {
        if (chatCompletion.tokens() > 4096) {
            throw new MyServiceException("超出最大单词限制，请减少上下文数或减少最大回复数");
        }
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
        log.info("开始embeddings，输入长度：{}", input.size());
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

        log.info("embeddings结束，花费token：{}", tokens);
        return result;
    }

    public EmbeddingResponse embeddings(Embedding embedding) {
        Single<EmbeddingResponse> embeddings = this.openAIAPI.embeddings(embedding);
        EmbeddingResponse embeddingResponse = null;
        for (int i = 0; i < 5; i++) {
            try {
                embeddingResponse = embeddings.blockingGet();
            } catch (Exception e) {
                continue;
            }
            if (embeddingResponse != null) {
                break;
            }
        }
        if (embeddingResponse == null) {
            throw new MyException();
        }

        return embeddingResponse;
    }

    @Override
    public ChatCompletionResponse chatCompletion(ChatCompletion chatCompletion) {
        Single<ChatCompletionResponse> chatCompletionResponse = this.openAIAPI.chatCompletion(chatCompletion);
        return chatCompletionResponse.blockingGet();
    }

    @Override
    public ChatCompletionResponse chatCompletion(List<Message> messages) {
        ChatCompletion chatCompletion = ChatCompletion.builder().messages(messages).build();
        return this.chatCompletion(chatCompletion);
    }

    @Override
    public void streamChatCompletionWithFile(List<String> fileNames, ChatCompletion chatCompletion, EventSourceListener eventSourceListener) {
        List<Message> originMessage = chatCompletion.getMessages();

        String lang = "zh";
        if (fileNames.size() == 1) {
            lang = fileNames.get(0).split("&")[1];
        }

        // 将提问转换为关键词序列
        String getKeyWordMessage = "You need to extract keywords from the statement or question and "
                + "return a series of keywords separated by commas.\ncontent: "
                + originMessage.get(originMessage.size()-1).getContent() + "\nkeywords: ";
        List<Message> messages = new ArrayList<>();
        messages.add(Message.builder().role(Message.Role.USER).content(getKeyWordMessage).build());
        ChatCompletionResponse getKeyWordRes = this.chatCompletion(messages);
        String keywords = getKeyWordRes.getChoices().get(0).getMessage().getContent();

        // 获取关键词序列的embedding
        EmbeddingResponse embeddings = this.embeddings(keywords);

        // 从数据库用余弦距离查找最相似的一组text
        List<String> context = fileEmbeddingMapper.getTextsByCosineDistance(
                fileNames, embeddings.getData().get(0).getEmbedding(), 100);

        // 组合system
        context = this.cutTexts(context);
        StringBuilder text = new StringBuilder();
        for (int index = 0; index < context.size(); index++) {
            String line = String.format("%d. %s", index, context.get(index));
            text.append(line).append("\n");
        }

        String system = String.format("You are a helpful AI article assistant. " +
                "The following are the relevant article content fragments found from the article. " +
                "The relevance is sorted from high to low. " +
                "You can answer according to the following content:\n\n%s\n\n" +
                "You need to carefully consider your answer to ensure that it is based on the context. ",
//                "If the context does not mention the content or it is uncertain whether it is correct, " +
//                "please answer \"Current context cannot provide effective information.\" " +
//                "You must use %s to respond.",
                text);
        originMessage.remove(0);
        originMessage.add(0, Message.builder().role(Message.Role.SYSTEM).content(system).build());

        if (chatCompletion.tokens() > 4096) {
            throw new MyServiceException("超出最大单词限制，请减少上下文数或减少最大回复数");
        }

        this.streamChatCompletion(chatCompletion, eventSourceListener);
    }

    private List<String> cutTexts(List<String> context) {
        int maximum = 4096 - 1024;
        for (int index = 0; index < context.size(); index++) {
            String text = context.get(index);
            maximum -= TikTokensUtil.tokens(EncodingType.CL100K_BASE, text);
            if (maximum < 0) {
                context = context.subList(0, index + 1);
                log.debug(String.format("Exceeded maximum length, cut the first %d fragments", index + 1));
                break;
            }
        }
        return context;
    }
}
