package com.rekkeb.netty.server.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

import java.nio.charset.Charset;

/**
 *
 * Created by rekkeb on 2/3/16.
 */
public class ByteBufServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Channel Registered: " + ctx.name());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
          System.out.println("Channel Active: " + ctx.name());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf byteBuf = (ByteBuf) msg;

        System.out.println("channelRead: " + byteBuf.toString(Charset.forName("UTF-8")));

        byteBuf.writeBytes(" - Server".getBytes());

        ctx.write(byteBuf)
                .addListener(future -> {
                    if (!future.isSuccess()) future.cause().printStackTrace();
                });

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(future -> {if (!future.isSuccess()) future.cause().printStackTrace();})
                .addListener(ChannelFutureListener.CLOSE);

        System.out.println("Read Complete: " + ctx.name());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Channel Inactive: " + ctx.name());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Channel Unregistered: " + ctx.name());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.println("Exception: " + ctx.name());
        cause.printStackTrace();
    }
}
