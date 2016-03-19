package com.rekkeb.netty.client.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 *
 * Created by rekkeb on 2/3/16.
 */
public class HttpClientHandler extends SimpleChannelInboundHandler<HttpObject> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1,
                HttpMethod.GET,
                "/");

        request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
        request.headers().set(HttpHeaders.Names.HOST, "localhost");

        ctx.writeAndFlush(request)
                .addListener(future -> { if (!future.isSuccess()) future.cause().printStackTrace(); });

        System.out.println("Canal Activo: " + ctx.name());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        System.out.println("channelRead0: " + msg.toString());

        if (msg instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) msg;

            System.out.println("Response0: " + response.getDecoderResult().toString());
        }
        else if (msg instanceof HttpContent){
            HttpContent response = (HttpContent) msg;

            System.out.println("Content0: " + response.content().toString(CharsetUtil.UTF_8));
        }
        else{
            System.out.println(msg.getClass());
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead: " + msg.toString());
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
