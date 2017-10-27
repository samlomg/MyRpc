package com.dglbc.transport;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Arrays;

public class ReadHandler implements CompletionHandler<Integer, ByteBuffer> {
    //用于读取半包消息和发送应答
    private AsynchronousSocketChannel channel;
    byte[] messages;


    public ReadHandler(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

    public ReadHandler(AsynchronousSocketChannel channel, byte[] messages) {
        this.channel = channel;
        this.messages = messages;
    }

    //读取到消息后的处理
    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        System.out.println(result);
        if (result < Server.DEFAULT_BUFF_SIZE){
            try {
                String expression = new String(messages, "UTF-8");
                System.out.println("服务器收到消息: " + expression);
                String calrResult = null;
                try {
                    calrResult = expression;
                } catch (Exception e) {
                    calrResult = "计算错误：" + e.getMessage();
                }
                //向客户端发送消息
                doWrite(calrResult);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        //flip操作
        attachment.flip();
        //根据
        byte[] message = new byte[attachment.remaining()];
        attachment.get(message);
        messages = concat(messages, message);
        ByteBuffer buffer = ByteBuffer.allocate(Server.DEFAULT_BUFF_SIZE);
        channel.read(buffer, buffer, this);
        attachment.compact();
    }

    //发送消息
    private void doWrite(String result) {
        byte[] bytes = result.getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        writeBuffer.put(bytes);
        writeBuffer.flip();
        //异步写数据 参数与前面的read一样
        channel.write(writeBuffer, writeBuffer,new WriteHandler(channel));
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try {
            this.channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public byte[] concat(byte[] first, byte[] second) {
        byte[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}