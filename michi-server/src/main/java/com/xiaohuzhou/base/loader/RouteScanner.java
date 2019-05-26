package com.xiaohuzhou.base.loader;

import com.xiaohuzhou.annotations.RequestMapping;
import com.xiaohuzhou.annotations.Route;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
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
        Class[] classes = new ComponentLoader(basePackage).getClasses();

        for (int i = 0; i < classes.length; i++) {
            Class clazz = classes[i];
            Annotation annotation = clazz.getAnnotation(Route.class);

            if (annotation != null) {
                String baseRoute = ((Route) annotation).value();
                Method[] methods = clazz.getMethods();
                for (int j = 0; j < methods.length; j++) {
                    Method method = methods[i];
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    if (requestMapping != null) {
                        String requestMethod = requestMapping.method();
                        String route = requestMapping.value();

                        String key = requestMethod + baseRoute + route;

                        if (routeReferences.get(key) != null) {
                            throw new RuntimeException("route: " + requestMethod + "  " + baseRoute + route + " is already existed!");
                        }

                        routeReferences.put(requestMethod + baseRoute + route, method);
                    }
                }
            }

        }

    }

    private class ComponentLoader implements Loader {

        private Class[] classes;

        private ComponentLoader(String basePackage) {
            this.classes = loadClass(basePackage);
        }

        private Class[] getClasses() {
            return classes;
        }

        public Class[] loadClass(String basePackage) {
            try {
                String absolutePath = getClass().getClassLoader().getResource("").getPath();
                List<String> classFileNames = getAllFileName(new File(absolutePath), absolutePath);

                Class[] classes = new Class[classFileNames.size()];

                for (int i = 0; i < classFileNames.size(); i++) {
                    classes[i] = Class.forName(classFileNames.get(i));
                }

                return classes;
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        private List<String> getAllFileName(File file, String absolutePath) throws IOException {
            LinkedList<String> fileNameList = new LinkedList<>();
            File[] files = file.listFiles();

            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                if (f.isDirectory()) {
                    fileNameList.addAll(getAllFileName(f, absolutePath));
                } else {
                    if (f.getName().endsWith(".class")) {
                        String classPath = f.getAbsolutePath()
                                .replace(absolutePath, "")
                                .replaceAll("/", ".")
                                .replace(".class", "");
                        fileNameList.addLast(classPath);
                    }
                }
            }
            return fileNameList;
        }

    }
}
