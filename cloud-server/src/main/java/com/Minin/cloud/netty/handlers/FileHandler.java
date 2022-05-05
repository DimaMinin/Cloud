package com.Minin.cloud.netty.handlers;

import com.Minin.cloud.model.AbstractMessage;
import com.Minin.cloud.model.FileMessage;
import com.Minin.cloud.model.ListMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class FileHandler extends SimpleChannelInboundHandler<AbstractMessage> {

    private Path serverDir = Path.of("ServerFiles");

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(new ListMessage(serverDir));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AbstractMessage msg) throws Exception {
        log.info("received {} message", msg.getMessageType().getName());
        if (msg instanceof FileMessage fileMessage) {
            Files.write(serverDir.resolve(fileMessage.getName()), fileMessage.getBytes());
            ctx.writeAndFlush(new ListMessage(serverDir));
        }
    }

}
