package com.cartoonbbs.cartoonbbs.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class LogAspect {
    private final Logger logger=LoggerFactory.getLogger(this.getClass());
    @Pointcut("execution(*  com.cartoonbbs.cartoonbbs.web.*.*(..))")
    //拦截所有的权限，以机web下的所有控制器
    public void log(){

    }
    @Before("log()")
    public  void doBefore(JoinPoint joinPoint){
        ServletRequestAttributes attributes= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request=attributes.getRequest();
        String url=request.getRequestURI().toString();
        String ip=request.getRemoteAddr();
        String classMethod=joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName();
        Object[] args=joinPoint.getArgs();
        RequestLog requestLog=new RequestLog(url,ip,classMethod,args);

        logger.info("request:{}",requestLog);
    }
    @After("log()")
    public void doAfter(){
        //logger.info("*************doAfter******************");
    }
    @AfterReturning(returning = "result",pointcut = "log()")
    public void doAfterRuturn(Object result){
        logger.info("result:{}",result);

    }
    private class   RequestLog{
        private String url;
        private String ip;
        private String classMethod;
        private Object[] ars;

        public RequestLog(String url, String ip, String classMethod, Object[] ars) {
            this.url = url;
            this.ip = ip;
            this.classMethod = classMethod;
            this.ars = ars;
        }

        @Override
        public String toString() {
            return "RequestLog{" +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", classMethod='" + classMethod + '\'' +
                    ", ars=" + Arrays.toString(ars) +
                    '}';
        }
    }

}
