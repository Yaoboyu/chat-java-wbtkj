package com.wbtkj.chat.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wbtkj.chat.config.StaticContextAccessor;
import com.wbtkj.chat.exception.MyException;
import com.wbtkj.chat.exception.MyServiceException;
import com.wbtkj.chat.filter.OpenAiAuthInterceptor;
import com.wbtkj.chat.pojo.dto.openai.billing.BillingUsage;
import com.wbtkj.chat.pojo.dto.openai.billing.Subscription;
import com.wbtkj.chat.pojo.dto.openai.chat.ChatCompletion;
import com.wbtkj.chat.pojo.dto.openai.chat.Message;
import com.wbtkj.chat.pojo.dto.openai.completions.Completion;
import com.wbtkj.chat.service.OpenAiApi;
import com.wbtkj.chat.service.OpenAiStreamService;
import com.wbtkj.chat.service.ThirdPartyModelKeyService;
import io.reactivex.Single;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Resource;
import java.time.LocalDate;
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
@Service
public class OpenAiStreamServiceImpl implements OpenAiStreamService {

//    @Value("${chatgpt.apiHost}")
    private String apiHost;
    /**
     * 自定义的okHttpClient
     */
    private OkHttpClient okHttpClient;

    @Getter
    private OpenAiApi openAiApi;

    /**
     * 自定义鉴权处理拦截器
     */
    private OpenAiAuthInterceptor openAiAuthInterceptor;

    @Resource
    ThirdPartyModelKeyService thirdPartyModelKeyService;


    /**
     * 构造实例对象
     */
    public OpenAiStreamServiceImpl() {
        openAiAuthInterceptor = StaticContextAccessor.getBean(OpenAiAuthInterceptor.class);
        apiHost = StaticContextAccessor.getBean(Environment.class).getProperty("chatgpt.apiHost");

        okHttpClient = new OkHttpClient
                .Builder()
                .addInterceptor(openAiAuthInterceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(50, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS)
                .build();


        openAiApi = new Retrofit.Builder()
                .baseUrl(apiHost)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build().create(OpenAiApi.class);
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

//    /**
//     * ## 官方已经禁止使用此api
//     * OpenAi账户余额查询
//     *
//     * @return 余额信息
//     */
//    @SneakyThrows
//    @Deprecated
//    public CreditGrantsResponse creditGrants() {
//        Request request = new Request.Builder()
//                .url(this.apiHost + "dashboard/billing/credit_grants")
//                .get()
//                .build();
//        Response response = this.okHttpClient.newCall(request).execute();
//        ResponseBody body = response.body();
//        String bodyStr = body.string();
////        log.info("调用查询余额请求返回值：{}", bodyStr);
//        if (!response.isSuccessful()) {
//            if (response.code() == CommonError.OPENAI_AUTHENTICATION_ERROR.getCode()
//                    || response.code() == CommonError.OPENAI_LIMIT_ERROR.getCode()
//                    || response.code() == CommonError.OPENAI_SERVER_ERROR.getCode()) {
//                OpenAiResponse openAiResponse = JSONUtil.toBean(bodyStr, OpenAiResponse.class);
//                log.error(openAiResponse.getError().getMessage());
//                throw new BaseException(openAiResponse.getError().getMessage());
//            }
//            String errorMsg = bodyStr;
//            log.error("询余额请求异常：{}", errorMsg);
//            OpenAiResponse openAiResponse = JSONUtil.toBean(errorMsg, OpenAiResponse.class);
//            if (Objects.nonNull(openAiResponse.getError())) {
//                log.error(openAiResponse.getError().getMessage());
//                throw new BaseException(openAiResponse.getError().getMessage());
//            }
//            throw new BaseException(CommonError.RETRY_ERROR);
//        }
//        ObjectMapper mapper = new ObjectMapper();
//        // 读取Json 返回值
//        CreditGrantsResponse completionResponse = mapper.readValue(bodyStr, CreditGrantsResponse.class);
//        return completionResponse;
//    }


    public Subscription subscription() {
        Single<Subscription> subscription = this.openAiApi.subscription();
        return subscription.blockingGet();
    }

    public BillingUsage billingUsage(@NotNull LocalDate starDate, @NotNull LocalDate endDate) {
        Single<BillingUsage> billingUsage = this.openAiApi.billingUsage(starDate, endDate);
        return billingUsage.blockingGet();
    }

}
