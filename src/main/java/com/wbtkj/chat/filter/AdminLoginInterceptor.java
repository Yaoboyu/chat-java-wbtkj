package com.wbtkj.chat.filter;

import com.alibaba.fastjson.JSONObject;
import com.wbtkj.chat.config.ThreadLocalConfig;
import com.wbtkj.chat.exception.MyServiceException;
import com.wbtkj.chat.pojo.vo.Result;
import com.wbtkj.chat.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class AdminLoginInterceptor implements HandlerInterceptor {
    @Resource
    AdminService adminService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        log.info("请求源: {}, 方法: {}, uri: {}", request.getRemoteUser(), request.getMethod(),request.getRequestURI());

        //3.获取请求头中的令牌（token）。
        String token = request.getHeader("Authorization");

        //4.解析token
        try{
            adminService.checkToken(token);
        } catch (MyServiceException e) {
            log.info(e.getMessage());
            Result error = Result.error(e.getMessage());
            response.setHeader("Content-Type", "text/html;charset=UTF-8");
            response.getWriter().write(JSONObject.toJSONString(error));
            return false;
        }

        //5.放行。
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        ThreadLocalConfig.remove();
    }
}