package com.Minin.cloud.netty;

import com.Minin.cloud.netty.handlers.FirstInHandler;
import com.Minin.cloud.netty.handlers.OutHandler;
import com.Minin.cloud.netty.handlers.SecondInHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class EchoPipeLine extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(
                new OutHandler(),
                new FirstInHandler(),
                new SecondInHandler()
        );
    }
}
