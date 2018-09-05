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
package com.zhengyuan.service.ReceiverHandle;


import com.zhengyuan.service.MessageService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@ChannelHandler.Sharable
public class ReceiverServerHandler extends ChannelInboundHandlerAdapter {
    @Autowired
    private MessageService messageService;
    private static final Logger logger= Logger.getLogger("netty");
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //String stringMessage = (String) msg;
//        super.channelRead(ctx, msg);
//        ((ByteBuf) msg).release();
        //tcp请求返回ACK字符串
        ByteBuf out = Unpooled.directBuffer(16);
        out.writeCharSequence("ACK", CharsetUtil.UTF_8);
        ctx.write(out); // (1)
        ctx.flush(); // (2)

        //System.out.println("client channelRead..");
        ByteBuf bytebufmsg = (ByteBuf)msg;
        ByteBuf buf1 = bytebufmsg.readBytes(bytebufmsg.readableBytes());
        //控制台打印收到的消息
        // System.out.println("Client received:" + ByteBufUtil.hexDump(buf1));

        String strTosend = ByteBufUtil.hexDump(buf1);
       // System.out.println(strTosend);
        //netty推送得到kafka produce
        logger.info("nettyMsg:"+strTosend);
        //messageService.send(strTosend);
    }
}
