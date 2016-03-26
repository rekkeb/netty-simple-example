package com.rekkeb.netty.client;

import com.rekkeb.netty.client.handlers.ByteBufHandler;
import com.rekkeb.netty.client.handlers.HttpClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.logging.LoggingHandler;

/**
 *
 * Created by rekkeb on 29/2/16.
 */
public class HttpClient {
    public static void main(String[] args) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap(); // (1)
            b.group(workerGroup) // (2)
                .channel(NioSocketChannel.class) // (3)
                //.option(ChannelOption.SO_KEEPALIVE, true) // (4)
                .remoteAddress("localhost", 8080)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new HttpClientCodec())
                                .addLast(new HttpClientHandler())
//                                    .addLast(new ByteBufHandler())
//                                  .addLast(new ObjectHandler())
                        ;
                    }
                });

            // Start the client.
            Channel channel = b.connect().sync().channel(); // (5)

            // Wait until the connection is closed.
            channel.closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}