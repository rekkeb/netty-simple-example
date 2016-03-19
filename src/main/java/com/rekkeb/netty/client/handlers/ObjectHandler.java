package com.rekkeb.netty.client.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

/**
 *
 * Created by rekkeb on 2/3/16.
 */
public class ObjectHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        HttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1,
                HttpMethod.GET,
                "/");

        request.headers().set(HttpHeaders.Names.ACCEPT, "application/json");
        request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);

        ctx.writeAndFlush(request)
                .addListener(future -> { if (!future.isSuccess()) future.cause().printStackTrace(); });

        System.out.println("Canal Activo: " + ctx.name());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Received: " + msg.toString());
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
