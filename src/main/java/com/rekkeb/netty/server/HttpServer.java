package com.rekkeb.netty.server;

import com.rekkeb.netty.server.handlers.ByteBufServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 *
 * Created by rekkeb on 29/2/16.
 */
public class HttpServer {
    public static void main(String[] args) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(workerGroup) // (2)
                    .channel(NioServerSocketChannel.class) // (3)
                    .localAddress(8080)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                //.addLast(new HttpServerCodec())
                                //.addLast(new HttpServerHandler())
                                .addLast(new ByteBufServerHandler())
                            ;
                        }
                    });

            // Start the server
            ChannelFuture f = b.bind().sync();

            f.addListener(future -> {
                if (future.isSuccess()) {
                    System.out.println("Server up");
                }
                else{
                    future.cause().printStackTrace();
                }
            });

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}