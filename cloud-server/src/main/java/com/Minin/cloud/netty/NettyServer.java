package com.Minin.cloud.netty;

import com.Minin.cloud.netty.serialization.SerializationPipeLine;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServer {

    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        EventLoopGroup light = new NioEventLoopGroup( 1);
        EventLoopGroup hard = new NioEventLoopGroup();


        ChannelFuture channelFuture = null;
        try {
            serverBootstrap.group(light, hard).channel(NioServerSocketChannel.class).childHandler(new SerializationPipeLine());
            channelFuture = serverBootstrap.bind(8189).sync();
            log.info("Server started:");
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("", e);
        } finally {
            light.shutdownGracefully();
            hard.shutdownGracefully();
        }

    }
}
