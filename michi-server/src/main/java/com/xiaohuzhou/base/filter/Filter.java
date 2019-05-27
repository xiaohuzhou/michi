package com.xiaohuzhou.base.filter;

import io.netty.handler.codec.http.HttpRequest;

/**
 * @Auther: ZhouXiaoHu
 * @Date: 2019/5/26
 * @Description:
 */
public interface Filter {

    void init();

    void doFilter(HttpRequest request);

    void destroy();
}
