package com.wbtkj.chat.config;

import com.wbtkj.chat.websocket.ChatHandler;
import com.wbtkj.chat.filter.WebSocketInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.annotation.Resource;


/**
 * 描述：
 *
 * @author https:www.unfbx.com
 * @since 2023-03-23
 */
@Configuration
@EnableWebMvc
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Resource
    private ChatHandler chatHandler;
    @Resource
    private WebSocketInterceptor webSocketInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(chatHandler, "/chat")
                .addInterceptors(webSocketInterceptor)
                .setAllowedOrigins("*");
    }

//    @Bean
//    public ServerEndpointExporter serverEndpointExporter() {
//        return new ServerEndpointExporter();
//    }
}
