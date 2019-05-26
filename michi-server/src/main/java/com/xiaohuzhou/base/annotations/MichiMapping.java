package com.xiaohuzhou.base.annotations;

import com.xiaohuzhou.base.enums.RequestMethod;

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
public @interface MichiMapping {

    String value() default ""; //请求路径

    RequestMethod method() default RequestMethod.GET; //请求方法
}
