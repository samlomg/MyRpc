package com.dglbc.transport;

import com.dglbc.core.InvokeReflect;
import com.dglbc.core.RpcRequest;
import com.dglbc.core.RpcResponse;
import com.dglbc.serializer.FastjsonSerializer;

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
    FastjsonSerializer fastjsonSerializer = new FastjsonSerializer();


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

        tobyteArray(attachment);

        ByteBuffer buffer = ByteBuffer.allocate(Server.DEFAULT_BUFF_SIZE);
        channel.read(buffer, buffer, new ReadHandler(channel,messages));
        attachment.compact();

        if (result < Server.DEFAULT_BUFF_SIZE){
            try {
                RpcRequest request= (RpcRequest) fastjsonSerializer.deserialize(messages,RpcRequest.class);

                RpcResponse response = new RpcResponse();
                response.setRequestId(request.getRequestId());
                try {
                    Object resultf = InvokeReflect.handle(request);
                    response.setResult(resultf);
                } catch (Throwable t) {
                    response.setError(t);
                }

                //向客户端发送消息
                doWrite(fastjsonSerializer.serialize(response));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try {
            this.channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //由buffer重组byte数组
    private void tobyteArray(ByteBuffer attachment){
        //flip操作
        attachment.flip();
        //根据
        byte[] message = new byte[attachment.remaining()];
        attachment.get(message);
        messages = concat(messages, message);
    }

    //发送消息
    private void doWrite(byte[] bytes) {
//        byte[] bytes = result.getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        writeBuffer.put(bytes);
        writeBuffer.flip();
        //异步写数据 参数与前面的read一样
        channel.write(writeBuffer, writeBuffer,new WriteHandler(channel));
    }

    public byte[] concat(byte[] first, byte[] second) {
        byte[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}