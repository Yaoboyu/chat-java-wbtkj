package com.wbtkj.chat.config;

import com.wbtkj.chat.filter.OpenAiAuthInterceptor;
import com.wbtkj.chat.service.OpenAiApi;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Configuration
//@DependsOn("OpenAiAuthInterceptor")
public class OpenaiConfig {
//    @Value("${chatgpt.apiHost}")
//    private String apiHost;
//    @Resource
//    private OpenAiAuthInterceptor authInterceptor;
//    @Resource
//    private OkHttpClient okHttpClient;
//
//    @Bean
//    public OkHttpClient myOkHttpClient() {
//        return new OkHttpClient
//                .Builder()
//                .addInterceptor(authInterceptor)
//                .connectTimeout(10, TimeUnit.SECONDS)
//                .writeTimeout(50, TimeUnit.SECONDS)
//                .readTimeout(50, TimeUnit.SECONDS)
//                .build();
//    }
//
//    @Bean
//    public OpenAiApi myOpenAiApi() {
//        return new Retrofit.Builder()
//                .baseUrl(apiHost)
//                .client(okHttpClient)
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addConverterFactory(JacksonConverterFactory.create())
//                .build().create(OpenAiApi.class);
//    }
}
