package com.cartoonbbs.cartoonbbs.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

//TODO 拦截器设置
@Configuration
public class WebConfig  extends WebMvcConfigurerAdapter {
    //拦截除了登录和登录成功页面
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin")
                .excludePathPatterns("/admin/login")
                .excludePathPatterns("/admin/register")
                .excludePathPatterns("/admin/register/**")
                .excludePathPatterns("/admin/verify")
                .excludePathPatterns("/admin/background");
    }
}
