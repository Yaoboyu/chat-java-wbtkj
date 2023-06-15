package com.wbtkj.chat.config;

import com.wbtkj.chat.filter.AdminLoginInterceptor;
import com.wbtkj.chat.filter.UserLoginInterceptor;
import com.wbtkj.chat.filter.WebSocketInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Resource
    private UserLoginInterceptor userLoginInterceptor;
    @Resource
    private AdminLoginInterceptor adminLoginInterceptor;
    @Value("${cros.allow-host}")
    private String allowHost;

    /**
     * 注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminLoginInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login");

        registry.addInterceptor(userLoginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/admin/**")
                .excludePathPatterns("/login")
                .excludePathPatterns("/register")
                .excludePathPatterns("/changePwd")
                .excludePathPatterns("/utils/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")      // 设置映射
                .allowedOriginPatterns(allowHost)        // 设置域
                .allowedMethods("*")// 设置请求的方式GET、POST等
                .allowCredentials(true)            // 设置是否携带cookie
                .maxAge(3600)                      // 设置设置的有效期 秒单位
                .allowedHeaders("*");              // 设置头
    }
}