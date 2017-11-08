package com.dglbc.transport;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class WriteHandler implements CompletionHandler<Integer, ByteBuffer> {
    private AsynchronousSocketChannel channel=null;


    public WriteHandler(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        //如果没有发送完，就继续发送直到完成
        if (result < Server.DEFAULT_BUFF_SIZE){
            try {
                channel.shutdownInput();
                channel.shutdownOutput();
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            channel.write(buffer, buffer, this);
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