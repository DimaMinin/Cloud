package com.Minin.cloud.netty.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
public class SecondInHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String message = (String) msg;
        log.info("received: {}", message);
        LocalDateTime time = LocalDateTime.now();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd hh:mm:ss");
        String date = dateFormat.format(new Date());
        message = date + " " + message;
        ctx.write(message);
    }
}
