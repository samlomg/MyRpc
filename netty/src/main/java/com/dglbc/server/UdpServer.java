package com.dglbc.server;

import com.dglbc.Constants;
import com.dglbc.server.handler.UdpServerHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.extern.java.Log;

@Log
public class UdpServer {
    //打印日志
//    public static final Logger log = Logger.getLogger(UdpServer.class);

    //给管道抽象出接口，给Channel更多的能力和配置，例如Channel的状态，参数，IO操作
    //使用ChannelPipeline实现自定义IO
    public static Channel channel;



    public static void main(String[] args) throws InterruptedException {
        //启动服务
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        //优化使用的线程
        final EventExecutorGroup group = new DefaultEventExecutorGroup(16);

        try {
            Bootstrap b = new Bootstrap();//udp不能使用ServerBootstrap
            b.group(workerGroup).channel(NioDatagramChannel.class)//设置UDP通道
                    //设置udp的管道工厂
                    .handler(new ChannelInitializer<NioDatagramChannel>() {
                        //NioDatagramChannel标志着是UDP格式的
                        @Override
                        protected void initChannel(NioDatagramChannel ch)
                                throws Exception {
                            // TODO Auto-generated method stub
                            //创建一个执行Handler的容器
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            //执行具体的处理器
                            pipeline.addLast(group, "handler", new UdpServerHandler());//消息处理器
                        }

                    })//初始化处理器
                    //true / false 多播模式(UDP适用),可以向多个主机发送消息
                    .option(ChannelOption.SO_BROADCAST, true)// 支持广播
                    .option(ChannelOption.SO_RCVBUF, 2048 * 1024)// 设置UDP读缓冲区为2M
                    .option(ChannelOption.SO_SNDBUF, 1024 * 1024);// 设置UDP写缓冲区为1M

            // 绑定端口，开始接收进来的连接  ，绑定的端口9999
            ChannelFuture f = b.bind(Constants.PORT).sync();
            //获取channel通道
            channel = f.channel();
            System.out.println("UDP Server 启动！");
            // 等待服务器 socket 关闭 。
            // 这不会发生，可以优雅地关闭服务器。
            f.channel().closeFuture().sync();
        } finally {
            // 优雅退出 释放线程池资源
            group.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
