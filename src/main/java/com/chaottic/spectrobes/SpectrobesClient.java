package com.chaottic.spectrobes;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.jetbrains.annotations.NotNull;

public final class SpectrobesClient {

    public void connect(String host, int port) {
        var group = new NioEventLoopGroup();

        try {
            var bootstrap = new Bootstrap();
            bootstrap
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.AUTO_CLOSE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(@NotNull SocketChannel ch) throws Exception {

                        }
                    });

            var future = bootstrap.connect(host, port).await();

            future.channel().closeFuture().await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            group.shutdownGracefully();
        }
    }
}
