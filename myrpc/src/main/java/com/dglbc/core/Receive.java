package com.dglbc.core;

import com.dglbc.api.HelloServiceImpl;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class Receive implements  Runnable {
    public static Map<String, Object> handlerMap = new HashMap<String,Object>(){{
        put("com.dglbc.api.HelloService", new HelloServiceImpl());
    }};
    private DatagramSocket ds;

    private FastjsonSerializer fastjsonSerializer = new FastjsonSerializer();

    public Receive(DatagramSocket ds) {
        this.ds = ds;
    }

    public void run() {
        try {
            while (true) {
                // 2,创建数据包
                byte[] buf = new byte[1024];
                DatagramPacket dp = new DatagramPacket(buf, buf.length);

                // 3,使用接收方法将数据存储到数据包中。
                ds.receive(dp);// 阻塞式的。
                RpcRequest request= (RpcRequest) fastjsonSerializer.deserialize(dp.getData(),RpcRequest.class);

                // 4，通过数据包对象的方法，解析其中的数据,比如，地址，端口，数据内容。
                String ip = dp.getAddress().getHostAddress();
                int port =dp.getPort();

                RpcResponse response = new RpcResponse();
                response.setRequestId(request.getRequestId());
                try {
                    Object result = handle(request);
                    response.setResult(result);
                } catch (Throwable t) {
                    response.setError(t);
                }
                byte[] bufs = fastjsonSerializer.serialize(response);
                DatagramPacket dp2 =new DatagramPacket(bufs, bufs.length, InetAddress.getByName(ip), port);
                ds.send(dp2);
                System.out.println("OK");
//                String data = new String(dp.getData(), 0, dp.getLength());
//                if ("886".equals(data)) {
//                    System.out.println(ip + "....离开聊天室");
//                    break;
//                }
//                System.out.println(ip + ":" + data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("接收端失败");

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private Object handle(RpcRequest request) throws Throwable {
        String className = request.getClassName();
        System.out.println("className:"+className);
        Object serviceBean = handlerMap.get(className);

        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

//         Method method = serviceClass.getMethod(methodName, parameterTypes);
//         method.setAccessible(true);
//         return method.invoke(serviceBean, parameters);

        FastClass serviceFastClass = FastClass.create(serviceClass);
        FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
        return serviceFastMethod.invoke(serviceBean, parameters);
    }
}
