package com.dglbc.transport;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class WriteHandler implements CompletionHandler<Integer, ByteBuffer> {
    private AsynchronousSocketChannel channel;


    public WriteHandler(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        //如果没有发送完，就继续发送直到完成
        if (buffer.hasRemaining())
            channel.write(buffer, buffer, this);
        else{
            //创建新的Buffer
            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
            //异步读  第三个参数为接收消息回调的业务Handler
            channel.read(readBuffer, readBuffer, new ReadHandler(channel));
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        System.err.println("数据发送失败...");
        try {
            channel.close();

        } catch (IOException e) {
        }
    }
}