package com.dglbc.transport;

import com.dglbc.core.RpcRequest;
import com.dglbc.core.RpcResponse;

import java.io.IOException;

public class Client {
    private static String DEFAULT_HOST = "127.0.0.1";
    private static int DEFAULT_PORT = 12345;
    private static AsyncClientHandler clientHandle;

    public static void start() throws IOException {
        start(DEFAULT_HOST, DEFAULT_PORT);
    }

    public static synchronized void start(String ip, int port) throws IOException {
        if (clientHandle != null)
            return;
        clientHandle = new AsyncClientHandler(ip, port);
        new Thread(clientHandle, "Client").start();
    }

    //向服务器发送消息1
    public RpcResponse sendrpc(RpcRequest request) throws Exception {
        Client.start();
        return clientHandle.sendrpc(request);
    }

}
