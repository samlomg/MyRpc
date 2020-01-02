package com.dglbc.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.EventExecutor;
import lombok.extern.java.Log;

import java.util.logging.Logger;

@Log
public class UdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    //打印日志
//    public static final Logger log = Logger.getLogger(UdpServerHandler.class);
    //在事件循环中执行
    private EventExecutor executor;

    //客户端与服务器端连接成功的时候触发
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("UDP通道已经连接");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info(ctx.channel().remoteAddress()+ "UDP通道已经断开");
        super.channelInactive(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info(ctx.channel().remoteAddress()+ "UDP通道已经连接");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.info(ctx.channel().remoteAddress()+ "UDP通道已经断开");
        super.channelUnregistered(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        // 读取收到的数据
        ByteBuf buf = (ByteBuf) packet.copy().content();
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "GB2312");
        System.out.println("【NOTE】>>>>>> 收到客户端的数据：" + body);
        String re= "Hello，我是Server，我的时间戳是" + System.currentTimeMillis();
        System.out.println(re);
        // 回复一条信息给客户端
        ctx.writeAndFlush(new DatagramPacket(
                Unpooled.copiedBuffer(re.getBytes("GB2312"))
                , packet.sender()));
    }

    //消息没有结束的时候触发
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    //捕获异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //logger.log(Level.INFO, "AuthServerInitHandler exceptionCaught");
        log.info("UdpServerHandler exceptionCaught" + cause.getMessage());
        cause.printStackTrace();
        ctx.close();
    }
}
