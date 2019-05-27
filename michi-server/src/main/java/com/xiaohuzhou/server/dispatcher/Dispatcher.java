package com.xiaohuzhou.server.dispatcher;

/**
 * @Auther: ZhouXiaoHu
 * @Date: 2019/5/27
 * @Description:
 */
public interface Dispatcher {

    Object route(String uri, String method);

}
