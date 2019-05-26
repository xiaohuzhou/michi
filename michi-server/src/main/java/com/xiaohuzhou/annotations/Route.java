package com.xiaohuzhou.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Auther: ZhouXiaoHu
 * @Date: 2019/5/3
 * @Description: 路由扫描层
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Route {

    String value() default "";
}
