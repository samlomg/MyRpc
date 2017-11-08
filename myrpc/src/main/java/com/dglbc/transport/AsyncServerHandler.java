package com.dglbc.transport;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

public class AsyncServerHandler implements Runnable {
    public CountDownLatch latch;
    public AsynchronousServerSocketChannel asynchronousServerSocketChannel;
    private AsynchronousChannelGroup asynchronousChannelGroup;

    public AsyncServerHandler(int port) {
        try {
            this.asynchronousChannelGroup = AsynchronousChannelGroup.withCachedThreadPool(
                    Executors.newCachedThreadPool(Executors.defaultThreadFactory()), 5);
            //创建服务端通道
            asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open(asynchronousChannelGroup);
            asynchronousServerSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
            //重用端口
            asynchronousServerSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            //绑定端口并设置连接请求队列长度
            asynchronousServerSocketChannel.bind(new InetSocketAddress(port), 1000);
            System.out.println("服务器已启动，端口号：" + port);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void run() {
        //CountDownLatch初始化
        //它的作用：在完成一组正在执行的操作之前，允许当前的现场一直阻塞
        //此处，让现场在此阻塞，防止服务端执行完成后退出
        //也可以使用while(true)+sleep
        //生成环境就不需要担心这个问题，以为服务端是不会退出的

        //用于接收客户端的连接
        asynchronousServerSocketChannel.accept(this, new AcceptHandler());
    }
}