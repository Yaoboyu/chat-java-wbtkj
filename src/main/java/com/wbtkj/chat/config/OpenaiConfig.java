package com.wbtkj.chat.config;

import org.springframework.context.annotation.Configuration;

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
//    public OpenAIAPI myOpenAiApi() {
//        return new Retrofit.Builder()
//                .baseUrl(apiHost)
//                .client(okHttpClient)
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addConverterFactory(JacksonConverterFactory.create())
//                .build().create(OpenAIAPI.class);
//    }
}
