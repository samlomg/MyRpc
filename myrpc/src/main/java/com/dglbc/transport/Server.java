package com.dglbc.transport;

import java.io.IOException;

public class Server {
    public static int DEFAULT_PORT = 12345;
    public static int DEFAULT_BUFF_SIZE = 2048;
    private static AsyncServerHandler serverHandle;
    public volatile static long clientCount = 0;

    public static void start() throws IOException {
        start(DEFAULT_PORT);
    }

    public static synchronized void start(int port) throws IOException {
        if (serverHandle != null)
            return;
        serverHandle = new AsyncServerHandler(port);
        new Thread(serverHandle, "Server").start();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Server.start(DEFAULT_PORT);
        while(true){
//            System.out.println(System.currentTimeMillis());
            Thread.sleep(1000);
        }
    }
}
