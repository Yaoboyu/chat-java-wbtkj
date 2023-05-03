package com.wbtkj.chat.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName="contextfilter",urlPatterns="/*")
public class CrossFilter implements Filter {

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*"); //允许所有的域名访问
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, PATCH, DELETE"); //允许的提交方式
        response.setHeader("Access-Control-Max-Age", "3600"); //最大有效时间
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with, Content-Type, Accept, Origin"); //允许那些请求头
        response.setHeader("Access-Control-Allow-Credentials", "true"); //是否支持ajax提交cookie
        chain.doFilter(req, res);
    }

    public void init(FilterConfig filterConfig) {}

    public void destroy() {}

}

