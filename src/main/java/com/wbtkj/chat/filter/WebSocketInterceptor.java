package com.wbtkj.chat.filter;

import com.wbtkj.chat.config.ThreadLocalConfig;
import com.wbtkj.chat.exception.MyServiceException;
import com.wbtkj.chat.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * webSocket握手拦截器
 * @since 2022年9月16日16:57:52
 */
@Slf4j
@Component
public class WebSocketInterceptor implements HandshakeInterceptor {
    @Resource
    private UserService userService;


    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        log.debug("HandshakeInterceptor beforeHandshake start...");
        if (request instanceof ServletServerHttpRequest) {
            HttpServletRequest req = ((ServletServerHttpRequest) request).getServletRequest();
            String token = req.getHeader("token");

            try {
                userService.checkToken(token);
            } catch (MyServiceException e) {
                log.info("【beforeHandshake】 authorization Parse failure. authorization = {}", token);
                return false;
            }
        }
        log.debug("HandshakeInterceptor beforeHandshake end...");
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        log.debug("HandshakeInterceptor afterHandshake start...");
        HttpServletRequest httpRequest = ((ServletServerHttpRequest) request).getServletRequest();
        HttpServletResponse httpResponse = ((ServletServerHttpResponse) response).getServletResponse();
        if (StringUtils.isNotEmpty(httpRequest.getHeader("Sec-WebSocket-Protocol"))) {
            httpResponse.addHeader("Sec-WebSocket-Protocol", httpRequest.getHeader("Sec-WebSocket-Protocol"));
        }
//        ThreadLocalConfig.remove();
        log.debug("HandshakeInterceptor afterHandshake end...");
    }
}