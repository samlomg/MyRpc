package com.dglbc;

import com.dglbc.api.HelloService;
import com.dglbc.core.RpcProxy;

import java.net.SocketException;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws SocketException {
        RpcProxy rpcProxy = new RpcProxy("192.168.22.223", 12345);
        HelloService helloService = rpcProxy.create(HelloService.class);
        System.err.println(helloService.hello("LBC"));
    }
}
