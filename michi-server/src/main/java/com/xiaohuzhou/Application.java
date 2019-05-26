package com.xiaohuzhou;

import com.xiaohuzhou.annotations.RequestMapping;
import com.xiaohuzhou.annotations.Route;
import com.xiaohuzhou.base.loader.RouteScanner;
import com.xiaohuzhou.base.loader.Scanner;
import com.xiaohuzhou.server.initializer.MichiServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @Auther: ZhouXiaoHu
 * @Date: 2019/5/3
 * @Description:
 */
@RequestMapping
@Route
public class Application {

    private static Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            LOG.info("initializing configures");
            //TODO 在进入时间循环监听之前还要加载路由，配置等
            new RouteScanner("");

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new MichiServerInitializer());

            int port = 8080;
            LOG.info("Application start listen on port {}", port);
            ChannelFuture channelFuture = serverBootstrap.bind(8080);
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            LOG.error("server initializer failed! error:{}", e.getMessage());
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

}
