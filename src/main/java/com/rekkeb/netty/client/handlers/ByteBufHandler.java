package com.rekkeb.netty.client.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

/**
 *
 * Created by rekkeb on 2/3/16.
 */
public class ByteBufHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        ByteBuf byteBuf = Unpooled.copiedBuffer("Sending data from client...", Charset.forName("UTF-8"));

        ctx.writeAndFlush(byteBuf)
                .addListener(future -> { if (!future.isSuccess()) future.cause().printStackTrace(); });

        System.out.println("Channel Active: " + ctx.name());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println("channelRead0: " + msg.toString());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf)msg;

        System.out.println("channelRead: " + byteBuf.toString(Charset.forName("UTF-8")));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("ReadComplete: " + ctx.name());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.println("Exception");
        cause.printStackTrace();
    }
}
