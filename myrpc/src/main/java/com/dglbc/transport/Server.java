package com.dglbc.transport;

public class Server {
    private static int DEFAULT_PORT = 12345;
    public static int DEFAULT_BUFF_SIZE = 2;
    private static AsyncServerHandler serverHandle;
    public volatile static long clientCount = 0;

    public static void start() {
        start(DEFAULT_PORT);
    }

    public static synchronized void start(int port) {
        if (serverHandle != null)
            return;
        serverHandle = new AsyncServerHandler(port);
        new Thread(serverHandle, "Server").start();
    }

    public static void main(String[] args) {
        Server.start();
    }
}
