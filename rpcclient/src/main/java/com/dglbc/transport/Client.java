package com.dglbc.transport;

import com.dglbc.core.RpcRequest;
import com.dglbc.core.RpcResponse;

import java.util.Scanner;

public class Client {
    private static String DEFAULT_HOST = "127.0.0.1";
    private static int DEFAULT_PORT = 12345;
    private static AsyncClientHandler clientHandle;

    public static void start() {
        start(DEFAULT_HOST, DEFAULT_PORT);
    }

    public static synchronized void start(String ip, int port) {
        if (clientHandle != null)
            return;
        clientHandle = new AsyncClientHandler(ip, port);
        new Thread(clientHandle, "Client").start();
    }

    //向服务器发送消息
    public static boolean sendMsg(String msg) throws Exception {
        if (msg.equals("q")) return false;
        clientHandle.sendMsg(msg);
        return true;
    }

    //向服务器发送消息1
    public RpcResponse sendrpc(RpcRequest request) throws Exception {

        return clientHandle.sendrpc(request);
    }

    @SuppressWarnings("resource")
    public static void main(String[] args) throws Exception {
        Client.start();
        System.out.println("请输入请求消息：");
        Scanner scanner = new Scanner(System.in);
        Client.sendMsg(scanner.nextLine());
    }
}
