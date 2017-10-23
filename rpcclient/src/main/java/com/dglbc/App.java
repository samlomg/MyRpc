package com.dglbc;

import com.dglbc.api.HelloService;
import com.dglbc.core.RpcProxy;

import java.net.SocketException;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws SocketException {
        RpcProxy rpcProxy = new RpcProxy("192.168.22.223", 10002);
        HelloService helloService = rpcProxy.create(HelloService.class);
        helloService.hello("LBC");
    }
}
