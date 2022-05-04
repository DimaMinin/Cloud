package com.Minin.cloud.netty.serialization;

import com.Minin.cloud.netty.handlers.FileHandler;
import com.Minin.cloud.netty.handlers.SecondInHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class SerializationPipeLine extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(
                new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                new ObjectEncoder(),
                new FileHandler()
        );

    }
}
