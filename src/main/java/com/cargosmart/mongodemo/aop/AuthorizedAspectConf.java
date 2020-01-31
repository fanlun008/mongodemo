package com.cargosmart.mongodemo.aop;

import com.cargosmart.mongodemo.annotation.Authorized;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Aspect
@Order(0)
@Component
public class AuthorizedAspectConf {
    /**
     * 请求头认证字段
     */
    private static final String HEAD_AUTHORIZATION="Authorization";

    /**
     * 请求切点方法(已提供@RequestMapping,@GetMapping,@PostMapping注解，需要其它请增加)
     */
    @Pointcut(" @annotation(org.springframework.web.bind.annotation.RequestMapping) || " +
            "   @annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "   @annotation(org.springframework.web.bind.annotation.PostMapping)")
    void requestMapping() {
    }

    /**
     * 范围切点方法
     */
    @Pointcut("execution(* com.cargosmart.mongodemo.controller.*.*(..))")
    public void methodPointCut() {
    }

    @Before("requestMapping() && methodPointCut()")
    void doBefore(JoinPoint joinPoint) throws Exception {
        System.out.println("AOP before");
        Authorized authorized = null;
        MethodSignature signMethod = (MethodSignature) joinPoint.getSignature();
        Method method = signMethod.getMethod();
        //获取方法注解
        authorized = method.getAnnotation(Authorized.class);
        if (authorized == null) {
            System.out.println("AOP 方法上未检测到 Authorized 注解");
            //获取类上的注解
            authorized = joinPoint.getTarget().getClass().getAnnotation(Authorized.class);
            if (authorized == null) {
                System.out.println("AOP 类上未检测到 Authorized 注解\nPass Authorized-------------------");
                return;
            }
        }
        authLogic(joinPoint);

    }


    /**
     * 认证逻辑
     * @param joinPoint
     * @throws Exception
     */
    private void authLogic(JoinPoint joinPoint) throws Exception {
        System.out.println("认证开始...");
        String classType = joinPoint.getTarget().getClass().getName();
        Class<?> clazz = Class.forName(classType);
        String clazzName = clazz.getName();
        String methodName = joinPoint.getSignature().getName();
        System.out.println("ClassName: "+clazzName);
        System.out.println("MethodName:"+methodName);
        //获取当前http请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader(HEAD_AUTHORIZATION);
        //此处的TOKEN验证业务逻辑自行编写
        if(StringUtils.isNotEmpty(token)){
            System.out.println("****token: " + token);
            System.out.println("请求认证通过！");
        }else {
            throw new Exception("请求被拒绝！");
        }
    }
}
