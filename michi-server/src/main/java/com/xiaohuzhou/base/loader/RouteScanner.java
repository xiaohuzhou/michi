package com.xiaohuzhou.base.loader;

import com.xiaohuzhou.base.annotations.MichiMapping;
import com.xiaohuzhou.base.annotations.MichiRoute;
import com.xiaohuzhou.base.enums.RequestMethod;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @Auther: ZhouXiaoHu
 * @Date: 2019/5/26
 * @Description: 分类  路由 - 类+方法
 */
public class RouteScanner implements Scanner {

    private Map<String, Method> routeReferences = new HashMap<>();

    public RouteScanner(String basePackage) {
        doScan(basePackage);
    }

    public Map<String, Method> getRouteReferences() {
        return routeReferences;
    }

    public void doScan(String basePackage) {
        Set<Class<?>> classes = new ComponentLoader(basePackage).getClasses();

        classes.forEach(clazz -> {
            Annotation annotation = clazz.getAnnotation(MichiRoute.class);
            if (annotation != null) {
                String baseRoute = ((MichiRoute) annotation).value();
                Method[] methods = clazz.getMethods();

                Arrays.stream(methods).forEach(method -> {
                    MichiMapping requestMapping = method.getAnnotation(MichiMapping.class);
                    if (requestMapping != null) {
                        RequestMethod enumMethod = requestMapping.method();
                        String requestMethod = enumMethod.name();
                        String route = requestMapping.value();

                        String key = requestMethod + baseRoute + route;
                        if (routeReferences.get(key) != null) {
                            throw new RuntimeException("route: " + requestMethod + "  " + baseRoute + route + " is already existed!");
                        }

                        routeReferences.put(key, method);
                    }
                });
            }
        });
    }

    private class ComponentLoader implements Loader {

        private Set<Class<?>> classes;

        private ComponentLoader(String basePackage) {
            this.classes = loadClass(basePackage);
        }

        private Set<Class<?>> getClasses() {
            return classes;
        }

        public Set<Class<?>> loadClass(String basePackage) {
            Reflections reflections = new Reflections(basePackage);
            return reflections.getTypesAnnotatedWith(MichiRoute.class);
        }
    }
}
