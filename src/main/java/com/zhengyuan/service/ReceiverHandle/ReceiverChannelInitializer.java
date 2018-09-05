package com.zhengyuan.service.ReceiverHandle;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
@Qualifier("ReceiverChannelInitializer")
public class ReceiverChannelInitializer extends ChannelInitializer<SocketChannel> {
    //@Autowired
    @Qualifier("ReceiverServerHandler")
    private ReceiverServerHandler receiverServerHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();

        pipeline.addLast(new LineBasedFrameDecoder(1024,false,false));
        //pipeline.addLast(new DelimiterBasedFrameDecoder(1024*1024, Delimiters.lineDelimiter()));

        //pipeline.addLast(new LengthFieldBasedFrameDecoder(1024*1024, 0, 2, 0, 2));
        //pipeline.addLast(new LengthFieldPrepender(2));
        //pipeline.addLast(new ObjectCodec());
        pipeline.addLast(receiverServerHandler);
    }
}
