package com.xiaohuzhou.base.loader.entity;

import java.lang.reflect.Method;

/**
 * @Auther: ZhouXiaoHu
 * @Date: 2019/5/27
 * @Description:
 */
public class InstanceMethod {

    private Method method;

    private Object object;

    public InstanceMethod() {
    }

    public InstanceMethod(Method method, Object object) {
        this.method = method;
        this.object = object;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
