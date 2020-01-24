package com.example.demo.annotation;

import java.lang.annotation.*;

/**
 * @author M
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)  // 保留到运行时，可通过注解获取
@Documented
public @interface MyMethod {

    Class toClass() ;

}
