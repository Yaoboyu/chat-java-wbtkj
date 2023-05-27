package com.wbtkj.chat.filter;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.json.JSONUtil;
import com.wbtkj.chat.config.StaticContextAccessor;
import com.wbtkj.chat.exception.MyException;
import com.wbtkj.chat.pojo.dto.openai.CommonError;
import com.wbtkj.chat.pojo.dto.openai.chat.ChatCompletion;
import com.wbtkj.chat.pojo.dto.openai.common.OpenAiResponse;
import com.wbtkj.chat.pojo.dto.thirdPartyModelKey.ThirdPartyModelKeyStatus;
import com.wbtkj.chat.service.ThirdPartyModelKeyService;
import com.wbtkj.chat.utils.MailUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;


@Slf4j
@Component
public class OpenAiAuthInterceptor implements Interceptor {

    /**
     * key 集合
     */
    private CopyOnWriteArrayList<String> gpt3Key;
    private CopyOnWriteArrayList<String> gpt4Key;
    private int gpt3KeyIndex;
    private int gpt4KeyIndex;

    /**
     * 账号被封了
     */
    private static final String ACCOUNT_DEACTIVATED = "account_deactivated";
    /**
     * key不正确
     */
    private static final String INVALID_API_KEY = "invalid_api_key";
    /**
     *
     */
//    private static final String EXCEEDED_QUOTA = "quota";

    private ThirdPartyModelKeyService thirdPartyModelKeyService;


    public OpenAiAuthInterceptor() {
        thirdPartyModelKeyService = StaticContextAccessor.getBean(ThirdPartyModelKeyService.class);

        gpt3Key = new CopyOnWriteArrayList<>(thirdPartyModelKeyService.getEnableGpt3Keys());
        gpt4Key = new CopyOnWriteArrayList<>(thirdPartyModelKeyService.getEnableGpt4Keys());
        gpt3KeyIndex = 0;
        gpt4KeyIndex = 0;
    }

    public boolean addKey(String key, String model) {
        if(model.equals(ChatCompletion.Model.GPT_3_5_TURBO.getName())) {
            return gpt3Key.addIfAbsent(key);
        } else if(model.equals(ChatCompletion.Model.GPT_4.getName())) {
            return gpt4Key.addIfAbsent(key);
        } else {
            return false;
        }
    }

    public boolean delKey(String key, String model) {
        if(model.equals(ChatCompletion.Model.GPT_3_5_TURBO.getName())) {
            return gpt3Key.removeIf(Predicate.isEqual(key));
        } else if(model.equals(ChatCompletion.Model.GPT_4.getName())) {
            return gpt4Key.removeIf(Predicate.isEqual(key));
        } else {
            return false;
        }
    }



    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = intercept(chain, 0);
        if(response == null) {
            throw new MyException();
        }

        return response;
    }

    private Response intercept(Chain chain, int depth) throws IOException {
        if(depth >= 6) return null;

        // 获取key
        Request original = chain.request();
        String model = original.header("model");
        String key = null;
        if(model.equals(ChatCompletion.Model.GPT_3_5_TURBO.getName())) {
            key = getGpt3Key();
        } else if (model.equals(ChatCompletion.Model.GPT_4.getName())) {
            key = getGpt4Key();
        }
        Request request = this.auth(key, original);
        // 请求
        Response response = chain.proceed(request);
        if (!response.isSuccessful()) {
            String errorMsg = response.body().string();
            OpenAiResponse openAiResponse = JSONUtil.toBean(errorMsg, OpenAiResponse.class);
            String errorCode = openAiResponse.getError().getCode();
            // apikey失效
            if (response.code() == CommonError.OPENAI_AUTHENTICATION_ERROR.getCode()
                    || response.code() == CommonError.OPENAI_LIMIT_ERROR.getCode()) {

                log.error("--------> 请求openai异常，错误code：{}", errorCode);
                log.error("--------> 请求异常：{}", errorMsg);
                //账号被封或者key不正确就移除掉
                if (ACCOUNT_DEACTIVATED.equals(errorCode) || INVALID_API_KEY.equals(errorCode)) {
                    thirdPartyModelKeyService.changeStatus(key, ThirdPartyModelKeyStatus.INVALID.getStatus());
                }
                // 其他情况尝试其他key
                return intercept(chain, depth + 1);
            }

            // 服务器出错
            if (response.code() == CommonError.OPENAI_SERVER_ERROR.getCode()) {
                return intercept(chain, depth + 1);
            }


            //非官方定义的错误code
            log.error("--------> 请求异常：{}", errorMsg);
            if (Objects.nonNull(openAiResponse.getError())) {
                log.error(openAiResponse.getError().getMessage());
                throw new MyException();
            }
            throw new MyException();
        }

        return response;
    }

//    /**
//     * 自定义apiKeys的处理逻辑
//     *
//     * @param model
//     * @param errorKey 错误的key
//     */
//    private void onErrorDealApiKeys(String model, String errorKey) {
//        if(model.equals(ChatCompletion.Model.GPT_3_5_TURBO.getName())) {
//            gpt3Key.removeIf(Predicate.isEqual(errorKey));
//        } else if (model.equals(ChatCompletion.Model.GPT_4.getName())) {
//            gpt4Key.removeIf(Predicate.isEqual(errorKey));
//        }
//
//
//        log.info("--------> 当前ApiKey：[{}] 失效了，移除！", errorKey);
//    }

    /**
     * 获取请求key
     *
     * @return key
     */
    private String getGpt3Key() {
        if (CollectionUtil.isEmpty(gpt3Key)) {
            MailUtils.SendMail("773508803@qq.com",
                    "[告警] 没有可用的gpt3 key！！",
                    "[告警] 没有可用的gpt3 key！！");
            log.error(CommonError.NO_ACTIVE_API_KEYS.getMsg());
            throw new MyException("gpt-3.5-turbo暂不可用");
        }
        return gpt3Key.get(getGpt3KeyIndex());
    }

    private String getGpt4Key() {
        if (CollectionUtil.isEmpty(gpt4Key)) {
            MailUtils.SendMail("773508803@qq.com",
                    "[告警] 没有可用的gpt4 key！！",
                    "[告警] 没有可用的gpt4 key！！");
            log.error(CommonError.NO_ACTIVE_API_KEYS.getMsg());
            throw new MyException("gpt-4暂不可用");
        }
        return gpt4Key.get(getGpt4KeyIndex());
    }

    private synchronized int getGpt3KeyIndex(){
        this.gpt3KeyIndex = (this.gpt3KeyIndex + 1) % gpt3Key.size();
        return this.gpt3KeyIndex;
    }

    private synchronized int getGpt4KeyIndex(){
        this.gpt4KeyIndex = (this.gpt4KeyIndex + 1) % gpt4Key.size();
        return this.gpt4KeyIndex;
    }

    /**
     * 默认的鉴权处理方法
     *
     * @param key      api key
     * @param original 源请求体
     * @return 请求体
     */
    private Request auth(String key, Request original) {
        Request request = original.newBuilder()
                .header(Header.AUTHORIZATION.getValue(), "Bearer " + key)
                .header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue())
                .method(original.method(), original.body())
                .build();
        return request;
    }
}
