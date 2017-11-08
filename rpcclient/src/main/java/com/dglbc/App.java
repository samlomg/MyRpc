package com.dglbc;

import com.dglbc.api.HelloService;
import com.dglbc.core.RpcProxy;

import java.net.SocketException;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws SocketException {
        RpcProxy rpcProxy = new RpcProxy("127.0.0.1", 12345);
        HelloService helloService = rpcProxy.create(HelloService.class);
        System.err.println(helloService.hello("Improve"));
    }
}
