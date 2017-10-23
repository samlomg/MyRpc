package com.dglbc.core;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

@SuppressWarnings("Duplicates")
public class SendWithoutRunable {
    FastjsonSerializer fastjsonSerializer = new FastjsonSerializer();
    public RpcResponse send(DatagramSocket ds, RpcRequest request, String ip, int port){
        RpcResponse returnCode =null;
        try {
            byte[] buf = fastjsonSerializer.serialize(request);
            //封装发送的数据包，其中255为广播地址
            DatagramPacket dp = new DatagramPacket(buf, buf.length, InetAddress.getByName(ip), port);
            ds.send(dp);

            //接收
            byte[] buf1 = new byte[1024];
            DatagramPacket dp1 = new DatagramPacket(buf1, buf1.length);

            // 3,使用接收方法将数据存储到数据包中。
            ds.receive(dp1);// 阻塞式的。
            returnCode= fastjsonSerializer.deserialize(dp1.getData(),RpcResponse.class);

        } catch (Exception e) {
            throw new RuntimeException("发送端失败");
        }
        return returnCode;
    }
}
