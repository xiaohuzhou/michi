package com.xiaohuzhou.base.loader;

import java.util.Set;

/**
 * @Auther: ZhouXiaoHu
 * @Date: 2019/5/26
 * @Description:
 */
public interface Loader {

    Set<Class<?>> loadClass(String basePackage);
}
