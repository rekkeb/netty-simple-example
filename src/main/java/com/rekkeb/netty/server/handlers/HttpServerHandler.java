package com.rekkeb.netty.server.handlers;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

/**
 *
 * Created by rekkeb on 2/3/16.
 */
public class HttpServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Channel Registered: " + ctx.name());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
          System.out.println("Canal Activo: " + ctx.name());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof HttpRequest) {
            HttpRequest in = (HttpRequest) msg;
            System.out.println("Server received: \n-------------------------------");
            System.out.println(in.toString());
            System.out.println("-------------------------------");

            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    Unpooled.wrappedBuffer("Responserrrrrl".getBytes()),
                    false
            );
            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, response.content().readableBytes());

            ctx.write(response)
                    .addListener(future -> {
                        if (!future.isSuccess()) future.cause().printStackTrace();
                    });
        }
        else if (msg instanceof LastHttpContent){
            //ctx.close();
            LastHttpContent content = (LastHttpContent)msg;
            System.out.println(content.toString());
        }
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
        System.out.println("Canal Inactivo: " + ctx.name());
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
