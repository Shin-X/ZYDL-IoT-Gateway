/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zhengyuan;
import com.zhengyuan.domain.NettyProperties;
import com.zhengyuan.service.ReceiverHandle.ReceiverChannelInitializer;
import com.zhengyuan.service.ReceiverService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SpringBootApplication
@EnableConfigurationProperties(NettyProperties.class)
public class Application {

    public static void main(String[] args) throws Exception{
        ConfigurableApplicationContext context = SpringApplication.run(Application.class);
        ReceiverService receiverService = context.getBean(ReceiverService.class);
        receiverService.start();
    }


    @Autowired
    private NettyProperties nettyProperties;
    @Autowired
    private ReceiverChannelInitializer receiverChannelInitializer;
    @Bean(name = "serverBootstrap")
    public ServerBootstrap bootstrap() {
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup(), workerGroup())
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .childHandler(receiverChannelInitializer)
                .option(ChannelOption.SO_BACKLOG, 256)          // (5)服务端可连接队列
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        ChannelFuture f; // (7)
        try {
            f = b.bind(nettyProperties.getTcpPort()).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return b;
    }
    @Bean(destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup bossGroup() {
        return new NioEventLoopGroup(nettyProperties.getBossCount());
    }

    @Bean(destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup workerGroup() {
        return new NioEventLoopGroup(nettyProperties.getWorkerCount());
    }

    @Bean
    public InetSocketAddress tcpSocketAddress() {
        return new InetSocketAddress(nettyProperties.getTcpPort());
    }

}