package com.xiaohuzhou.base.filter;

import com.xiaohuzhou.base.annotations.Order;
import io.netty.handler.codec.http.HttpRequest;

/**
 * @Auther: ZhouXiaoHu
 * @Date: 2019/5/26
 * @Description:
 */
@Order(1)
public class LoginFilter implements Filter {

    @Override
    public void init() {

    }

    @Override
    public void doFilter(HttpRequest request) {

    }

    @Override
    public void destroy() {

    }
}
