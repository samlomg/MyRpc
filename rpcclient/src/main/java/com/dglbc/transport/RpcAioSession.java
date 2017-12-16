package com.dglbc.transport;

import com.dglbc.core.FastjsonSerializer;
import com.dglbc.core.RpcRequest;
import com.dglbc.core.RpcResponse;
import lombok.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

/**
 * Created by LBC on 2017/11/3
 **/

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RpcAioSession {

    public static int DEFAULT_BUFF_SIZE = 2048;

    private AsynchronousSocketChannel asynchronousSocketChannel;
    private CountDownLatch countDownLatch;
    private RpcResponse rpcResponse;
    private boolean status = true;
    byte[] messages;
    FastjsonSerializer fastjsonSerializer = new FastjsonSerializer();

    public RpcAioSession(AsynchronousSocketChannel asynchronousSocketChannel, CountDownLatch countDownLatch) {
        this.asynchronousSocketChannel = asynchronousSocketChannel;
        this.countDownLatch = countDownLatch;
        this.messages = new byte[]{};
    }

    public RpcAioSession sumbit(RpcRequest request) throws IOException {
        byte[] req = fastjsonSerializer.serialize(request);
        //直接把buffer size计算出来！
        ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
        writeBuffer.put(req);
        writeBuffer.flip();
        //异步写
        asynchronousSocketChannel.write(writeBuffer, writeBuffer, new WriteHandler());
        return this;
    }


    public class WriteHandler implements CompletionHandler<Integer, ByteBuffer> {
        @Override
        public void completed(Integer result, ByteBuffer buffer) {
            //完成全部数据的写入
            if (buffer.hasRemaining()) {
                asynchronousSocketChannel.write(buffer, buffer, this);
            } else {
                //读取数据
                ByteBuffer readBuffer = ByteBuffer.allocate(DEFAULT_BUFF_SIZE);
                asynchronousSocketChannel.read(readBuffer, readBuffer, new ReadHandler());
                countDownLatch.countDown();

            }
        }

        @Override
        public void failed(Throwable exc, ByteBuffer attachment) {
            System.out.println("RPCSESSION.Write:" + exc.toString());
            try {
                asynchronousSocketChannel.close();
                countDownLatch.countDown();
            } catch (IOException e) {
                status = false;
                e.printStackTrace();
            }
        }

    }

    public class ReadHandler implements CompletionHandler<Integer, ByteBuffer> {

        @Override
        public void completed(Integer result, ByteBuffer attachment) {

            tobyteArray(attachment);

            if (result < DEFAULT_BUFF_SIZE) {
                attachment.clear();
                try {
                    asynchronousSocketChannel.close();
                    rpcResponse = fastjsonSerializer.deserialize(messages, RpcResponse.class);
                    countDownLatch.countDown();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                attachment.compact();
                asynchronousSocketChannel.read(attachment, attachment, new ReadHandler());
            }

        }

        @Override
        public void failed(Throwable exc, ByteBuffer attachment) {
            System.out.println("RPCSESSION.Read:" + attachment.toString());
            exc.printStackTrace();
            System.err.println("数据读取失败...");
            try {
                asynchronousSocketChannel.close();
                countDownLatch.countDown();
            } catch (IOException e) {
                e.printStackTrace();
                status = false;
            }
        }

        //由buffer重组byte数组
        private void tobyteArray(ByteBuffer attachment) {
            //flip操作
            attachment.flip();
            //根据
            byte[] message = new byte[attachment.remaining()];
            attachment.get(message);
            messages = concat(messages, message);
        }

        public byte[] concat(byte[] first, byte[] second) {
            byte[] result = Arrays.copyOf(first, first.length + second.length);
            System.arraycopy(second, 0, result, first.length, second.length);
            return result;
        }

    }
}
