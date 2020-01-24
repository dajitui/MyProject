package com.example.demo.aspect;

import com.alibaba.fastjson.JSON;
import com.example.demo.annotation.MyField;
import com.example.demo.annotation.MyMethod;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author M
 */
@Aspect
@Component
public class TestAspect {

    @Autowired
    private StringRedisTemplate template;

    private static ThreadLocal<String> cacheKey = new ThreadLocal<>();

    @Pointcut("@annotation(com.example.demo.annotation.MyMethod)")

    public void annotationPoinCut() {

    }

    @Before(value = "annotationPoinCut()")
    public void beforeTest(JoinPoint point) throws IllegalAccessException {
        //获取方法签名
        MethodSignature signature = (MethodSignature) point.getSignature();
        //获取切入方法的对象
        Method method = signature.getMethod();
        //获取方法上的Aop注解
        MyMethod myMethod = method.getAnnotation(MyMethod.class);
        String args = JSON.toJSONString(point.getArgs());
        if (args.length() > 1) {
            Object dto = JSON.parseObject(args.substring(1, args.length() - 1), myMethod.toClass());
            xx(dto, myMethod.toClass());
        }


    }

    private void xx(Object dto, Class class1) throws IllegalAccessException {
        // 获取所有字段
        Object lastDto = null;
        for (Field f : class1.getDeclaredFields()) {
            f.setAccessible(true);
            //System.out.println("f:" + f.getName());
            // 判断这个字段是否有MyField注解
            if ("key".equals(f.getName()) && !StringUtils.isEmpty(f.get(dto))) {
                lastDto = JSON.parseObject(template.opsForValue().get(f.get(dto)), class1);
                cacheKey.set(String.valueOf(f.get(dto)));
            }
            System.out.println(lastDto);
            if (f.isAnnotationPresent(MyField.class) && lastDto != null) {
                Object value = f.get(dto);
                Object value1 = f.get(lastDto);
                if (value != value1) {
                    System.out.println("存在不同,之前的值为:" + value1 + ",后面的值:" + value);
                }
            }
        }
    }

    @After(value = "annotationPoinCut()")
    public void afterTest(JoinPoint point) {
        System.out.println(cacheKey.get());
        template.delete(cacheKey.get());
        cacheKey.remove();
    }

}
