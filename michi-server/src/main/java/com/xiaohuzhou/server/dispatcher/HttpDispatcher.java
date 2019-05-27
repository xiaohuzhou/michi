package com.xiaohuzhou.server.dispatcher;

import com.xiaohuzhou.base.filter.Filter;
import com.xiaohuzhou.base.loader.ComponentScanner;
import com.xiaohuzhou.base.loader.entity.InstanceMethod;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;

/**
 * @Auther: ZhouXiaoHu
 * @Date: 2019/5/27
 * @Description:
 */
public class HttpDispatcher implements Dispatcher {

    private static Map<String, InstanceMethod> routeReferences = ComponentScanner.routeReferences();

    public static ArrayList<Filter> filters = ComponentScanner.filters();

    private static volatile HttpDispatcher httpDispatcher;

    private HttpDispatcher() {
    }

    public static HttpDispatcher builder() {
        if (httpDispatcher == null) {
            synchronized (HttpDispatcher.class) {
                if (httpDispatcher == null) {
                    httpDispatcher = new HttpDispatcher();
                }
            }
        }
        return httpDispatcher;
    }

    @Override
    public Object route(String uri, String method) {
        //TODO 方法执行前调用filter
        Object o = null;
        InstanceMethod instanceMethod = routeReferences.get(method + uri);
        try {
            o = instanceMethod.getMethod().invoke(instanceMethod.getObject(), null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return o;
    }
}
