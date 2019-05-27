package com.xiaohuzhou.server.initializer;

import com.xiaohuzhou.server.handler.MichiHttpServerHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Auther: ZhouXiaoHu
 * @Date: 2019/5/26
 * @Description: 定义各类http的处理器，包括netty提供封装好的以及自己定义的
 */
public class MichiServerInitializer extends ChannelInitializer<SocketChannel> {

    private static Logger LOG = LoggerFactory.getLogger(MichiServerInitializer.class);

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline channelPipeline = ch.pipeline();

        //绑定一个netty提供的默认的http处理器，主要是解决编码问题
        channelPipeline.addLast("HttpServerCodec", new HttpServerCodec());
        channelPipeline.addLast("MichiHttpServerHandler", new MichiHttpServerHandler());


        channelPipeline.addLast("protobufVarint32FrameDecoder", new ProtobufVarint32FrameDecoder());
        //pipeline.addLast(new ProtobufDecoder(Animal.Data.getDefaultInstance()));
        //pipeline.addLast(new ProtobufDecoder(Animal2.Data2.getDefaultInstance()));
        channelPipeline.addLast("ProtobufEncoder", new ProtobufEncoder());
        channelPipeline.addLast("ProtobufVarint32LengthFieldPrepender", new ProtobufVarint32LengthFieldPrepender());

        LOG.info("finished initialize handlers");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        LOG.error("exception occurred by cause: {}", cause.getMessage());
    }
}
