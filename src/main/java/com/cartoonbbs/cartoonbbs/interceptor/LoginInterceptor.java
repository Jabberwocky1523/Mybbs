package com.cartoonbbs.cartoonbbs.interceptor;
/*拦截器，如果没有登录的话，是不可以发表东西
* 重定向到登录页面*/
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        if(request.getSession().getAttribute("user")==null){
            response.sendRedirect("/admin");
            return false;
        }

        return true;
    }
}
