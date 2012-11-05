package org.mule.munit.common.connectors;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;


public class ConnectorCallBack implements MethodInterceptor {

    public static String ID = "__munitConnectorCallback";

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        String methodName = method.getName();
        if ( methodName.equals("connect")){
           return null;
        }
        return proxy.invokeSuper(obj,args);
    }
}
