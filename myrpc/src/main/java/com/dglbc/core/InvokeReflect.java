package com.dglbc.core;

import com.dglbc.impl.HelloServiceImpl;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LBC on 2017/10/26
 **/
@SuppressWarnings("Duplicates")
public class InvokeReflect {
    public static Map<String, Object> handlerMap = new HashMap<String, Object>() {{
        put("com.dglbc.api.HelloService", new HelloServiceImpl());
    }};

    private static Object handle(RpcRequest request) throws Throwable {
        String className = request.getClassName();
        Object serviceBean = handlerMap.get(className);

        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        FastClass serviceFastClass = FastClass.create(serviceClass);
        FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
        return serviceFastMethod.invoke(serviceBean, parameters);
    }
}
