package com.dglbc.core;

import com.dglbc.transport.Client;
import net.sf.cglib.proxy.InvocationHandler;
import net.sf.cglib.proxy.Proxy;

import java.lang.reflect.Method;
import java.util.UUID;

public class RpcProxy {
    private String ip;
    private Integer port;
    private SendWithoutRunable send = new SendWithoutRunable();
    private Client client=new Client();
    public RpcProxy(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

    @SuppressWarnings("unchecked")
    public <T> T create(Class<?> interfaceClass) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        RpcRequest request = new RpcRequest(); // 创建并初始化 RPC 请求
                        request.setRequestId(UUID.randomUUID().toString());
                        request.setClassName(method.getDeclaringClass().getName());
                        request.setMethodName(method.getName());
                        request.setParameterTypes(method.getParameterTypes());
                        request.setParameters(args);
//                        RpcResponse response=send.send(new DatagramSocket(), request, ip, port);
                        RpcResponse response = client.sendrpc(request);
                        if (response.getError() != null) {
                            throw response.getError();
                        } else {
                            return response.getResult();
                        }
                    }
                }
        );
    }
}
