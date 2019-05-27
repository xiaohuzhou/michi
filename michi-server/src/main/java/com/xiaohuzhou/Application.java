package com.xiaohuzhou;

import com.xiaohuzhou.base.annotations.MichiMapping;
import com.xiaohuzhou.base.annotations.MichiRoute;
import com.xiaohuzhou.base.enums.RequestMethod;
import com.xiaohuzhou.base.loader.ComponentScanner;
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
@MichiRoute
public class Application {

    private static Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            LOG.info("initializing configures");
            //在进入事件循环监听之前还要加载路由，配置等
            ComponentScanner.startScanner("com.xiaohuzhou");

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

//    @MichiMapping(value = "/test", method = RequestMethod.GET)
//    public void test() {
//        System.out.println(1);
//    }
}
