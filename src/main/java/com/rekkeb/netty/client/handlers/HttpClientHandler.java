package com.rekkeb.netty.client.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;
import java.nio.charset.Charset;

/**
 *
 * Created by rekkeb on 2/3/16.
 */
public class HttpClientHandler extends SimpleChannelInboundHandler<HttpObject> {

    private ByteBuf content = Unpooled.buffer();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        HttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1,
                HttpMethod.GET,
                "/");

//        request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
        request.headers().set(HttpHeaders.Names.HOST, "localhost");

        ctx.writeAndFlush(request)
                .addListener(future -> { if (!future.isSuccess()) future.cause().printStackTrace(); });

        System.out.println("Channel Active: " + ctx.name());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpResponse){
            HttpResponse response = (HttpResponse)msg;
            System.out.println("Response0 Status: " + response.getStatus());
            System.out.println("Response0 Headers: " + response.headers().entries());
            System.out.println("Response0 Begin Content { ");
        }
        if (msg instanceof HttpContent){
            HttpContent content = (HttpContent)msg;
            //System.out.println(content.content().toString(CharsetUtil.UTF_8));
            this.content.writeBytes(content.content());
            if (msg instanceof LastHttpContent){
                System.out.println(this.content.toString(CharsetUtil.UTF_8));
                System.out.println("} Response0 End Content");
                System.out.println(((LastHttpContent)msg).trailingHeaders().entries().toString());
                ctx.close();
            }
        }
    }

//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        if (msg instanceof HttpResponse){
//            HttpResponse response = (HttpResponse)msg;
//            System.out.println("Response Status: " + response.getStatus());
//            System.out.println("Response Headers: " + response.headers().entries());
//        }
//        else if (msg instanceof DefaultHttpContent){
//            DefaultHttpContent content = (DefaultHttpContent)msg;
//            System.out.println("Response Content: " + content.content().toString(Charset.forName("UTF-8")));
//        }
//        else {
//            System.out.println("channelRead: " + msg.toString());
//        }
//    }
//
//    @Override
//    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("ReadComplete: " + ctx.name());
//        ctx.close();
//    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.println("Exception");
        cause.printStackTrace();
    }
}
