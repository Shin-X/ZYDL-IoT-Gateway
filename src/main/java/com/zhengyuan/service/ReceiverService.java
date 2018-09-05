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
package com.zhengyuan.service;

import com.zhengyuan.domain.NettyProperties;
import com.zhengyuan.service.ReceiverHandle.ReceiverChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@Component

public class ReceiverService {

    private  ServerBootstrap serverBootstrap;

    private   InetSocketAddress tcpPort;

    private  Channel serverChannel;


    private NettyProperties nettyProperties;

    private ReceiverChannelInitializer receiverChannelInitializer;




    public void start() throws Exception {
         serverChannel =  serverBootstrap.bind(tcpPort).sync().channel().closeFuture().sync().channel();
    }

    @PreDestroy
    public void stop() {
        if ( serverChannel != null ) {
            serverChannel.close();
            serverChannel.parent().close();
        }
    }
}
