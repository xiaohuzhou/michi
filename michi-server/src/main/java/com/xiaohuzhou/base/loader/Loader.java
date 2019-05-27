package com.xiaohuzhou.base.loader;

import com.xiaohuzhou.base.filter.Filter;

import java.util.Set;

/**
 * @Auther: ZhouXiaoHu
 * @Date: 2019/5/26
 * @Description:
 */
public interface Loader {

    Set<Class<?>> getRoutes();

    Set<Class<? extends Filter>> getFilters();


}
