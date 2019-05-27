package com.xiaohuzhou.base.loader;

import com.xiaohuzhou.base.annotations.MichiMapping;
import com.xiaohuzhou.base.annotations.MichiRoute;
import com.xiaohuzhou.base.enums.RequestMethod;
import com.xiaohuzhou.base.filter.Filter;
import com.xiaohuzhou.base.loader.entity.InstanceMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @Auther: ZhouXiaoHu
 * @Date: 2019/5/26
 * @Description: 分类  路由 - 类+方法
 */
public class ComponentScanner implements Scanner {

    private String basePackage;

    private static Logger LOG = LoggerFactory.getLogger(ComponentScanner.class);

    private static Map<String, InstanceMethod> routeReferences = new HashMap<>();

    private static ArrayList<Filter> filters = new ArrayList<>();

    public static void startScanner(String basePackage) {
        new ComponentScanner(basePackage);
    }

    private ComponentScanner(String basePackage) {
        this.basePackage = basePackage;
        doScan();
    }


    public static Map<String, InstanceMethod> routeReferences() {
        return routeReferences;
    }

    public static ArrayList<Filter> filters() {
        return filters;
    }


    @Override
    public void doScan() {
        ComponentLoader componentLoader = new ComponentLoader();
        Set<Class<?>> routeClasses = componentLoader.getRouteClasses();
        Set<Class<? extends Filter>> filterClasses = componentLoader.getFilterClasses();

        Iterator<Class<?>> routeClassIterator = routeClasses.iterator();
        try {
            while (routeClassIterator.hasNext()) {
                Class clazz = routeClassIterator.next();
                Annotation annotation = clazz.getAnnotation(MichiRoute.class);
                if (annotation != null) {
                    String baseRoute = ((MichiRoute) annotation).value();
                    Method[] methods = clazz.getMethods();

                    for (int i = 0; i < methods.length; i++) {
                        Method method = methods[i];
                        MichiMapping requestMapping = method.getAnnotation(MichiMapping.class);
                        if (requestMapping != null) {
                            RequestMethod enumMethod = requestMapping.method();
                            String requestMethod = enumMethod.name();
                            String route = requestMapping.value();

                            String key = requestMethod + baseRoute + route;
                            if (routeReferences.get(key) != null) {
                                throw new RuntimeException("route: " + requestMethod + "  " + baseRoute + route + " is already existed!");
                            }

                            LOG.info("add route: {}   {}", requestMethod, route);
                            Object o = clazz.newInstance();
                            routeReferences.put(key, new InstanceMethod(method, o));
                        }
                    }
                }
            }

            //TODO 这里过滤器需要默认的 以及后面加的 还有按照order排序
            filters.ensureCapacity(filterClasses.size());
            Iterator<Class<? extends Filter>> iterator = filterClasses.iterator();
            while (iterator.hasNext()) {
                Filter filter = iterator.next().newInstance();
                filters.add(filter);
                filter.init();
                LOG.info("add filter: {}", filter.getClass().getSimpleName());
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private class ComponentLoader implements Loader {

        private Set<Class<?>> routeClasses;

        private Set<Class<? extends Filter>> filterClasses;

        Reflections reflections = new Reflections(basePackage);

        private ComponentLoader() {
            this.routeClasses = getRoutes();
            this.filterClasses = getFilters();
        }

        private Set<Class<?>> getRouteClasses() {
            return routeClasses;
        }

        private Set<Class<? extends Filter>> getFilterClasses() {
            return filterClasses;
        }


        @Override
        public Set<Class<?>> getRoutes() {
            return reflections.getTypesAnnotatedWith(MichiRoute.class);
        }

        @Override
        public Set<Class<? extends Filter>> getFilters() {
            return reflections.getSubTypesOf(Filter.class);
        }
    }
}

