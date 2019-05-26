package com.xiaohuzhou.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Auther: ZhouXiaoHu
 * @Date: 2019/5/3
 * @Description: 路由映射
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {

    String value() default ""; //请求路径

    String method() default ""; //请求方法
}
