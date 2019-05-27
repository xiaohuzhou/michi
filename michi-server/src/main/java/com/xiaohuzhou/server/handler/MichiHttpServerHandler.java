package com.xiaohuzhou.server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.xiaohuzhou.server.dispatcher.HttpDispatcher;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * @Auther: ZhouXiaoHu
 * @Date: 2019/5/26
 * @Description: 这个主要是路由页面的http请求的处理器，相当于DispatcherServeLet
 */
public class MichiHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject o) throws Exception {
        if (o instanceof HttpRequest) {
            //o 已经是http请求了 在此处进去url判断  请求方法判断等等
            HttpRequest request = (HttpRequest) o;

            String uri = request.uri();

            Object result = null;
            if (!uri.equals("/favicon.ico")) {
                //这里需要路由判定，给过去http的请求 得到响应，顺便还要定制过滤器
                result = HttpDispatcher.builder().route(request.uri(), request.method().name());
            }

            //响应的内容，为字节码，使用netty的工具读取字节码
            ByteBuf byteBuf = Unpooled.copiedBuffer(objectMapper.writeValueAsString(result), CharsetUtil.UTF_8);

            //构建一个默认的http响应
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);

            //给予响应响应头
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain")
                    .set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());

            //响应  并  flush
            ctx.writeAndFlush(response);
            //channelHandlerContext.close(); //主动关闭，而不等待超时获取浏览器来关闭请求
        }
    }
}
