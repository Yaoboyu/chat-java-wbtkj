package com.wbtkj.chat.filter;

import com.wbtkj.chat.config.ThreadLocalConfig;
import com.wbtkj.chat.pojo.DTO.UserDTO;
import com.wbtkj.chat.utils.JwtUtils;
import com.wbtkj.chat.pojo.VO.Result;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(urlPatterns = "/*")
public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        //1.获取请求url。
        String uri = req.getRequestURI().toString();
        log.info("请求的uri: {}", uri);

        //2.判断请求url中是否包含login，如果包含，说明是登录操作，放行。
        if(uri.startsWith("/login") || uri.startsWith("/register")){
            log.info("登录注册操作, 放行...");
            chain.doFilter(request,response);
            return;
        }

        //3.获取请求头中的令牌（token）。
        String jwt = req.getHeader("token");

        //4.判断令牌是否存在，如果不存在，返回错误结果（未登录）。
        if(!StringUtils.hasLength(jwt)){
            log.info("请求头token为空,返回未登录的信息");
            Result error = Result.error("NOT_LOGIN");
            //手动转换 对象--json --------> 阿里巴巴fastJSON
            String notLogin = JSONObject.toJSONString(error);
            resp.getWriter().write(notLogin);
            return;
        }

        //5.解析token，如果解析失败，返回错误结果（未登录）。
        try {
            Claims claims = JwtUtils.parseJWT(jwt);
            UserDTO userDTO = new UserDTO();
            userDTO.setId((Long) claims.get("id"));
            userDTO.setEmail((String) claims.get("email"));
            ThreadLocalConfig.setgetUser(userDTO);
            log.info("登录信息：" + JSONObject.toJSONString(userDTO));
        } catch (Exception e) {//jwt解析失败
            e.printStackTrace();
            log.info("解析令牌失败, 返回未登录错误信息");
            Result error = Result.error("NOT_LOGIN");
            //手动转换 对象--json --------> 阿里巴巴fastJSON
            String notLogin = JSONObject.toJSONString(error);
            resp.getWriter().write(notLogin);
            return;
        }

        //6.放行。
        chain.doFilter(request, response);
    }
}