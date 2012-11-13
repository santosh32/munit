package org.mule.munit.common.connectors;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;


/**
 * <p>Every time a method of a @see #org.mule.api.transport.Connector implementation is called this class is called. @see{MethodInterceptor}</p>
 *
 * <p>This class only acts when the method is connect</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class ConnectorMethodInterceptor implements MethodInterceptor {

    /**
     * <p>The method of the <code>Connector</code> implementation that is wanted to be mocked</p>
     */
    private static final String METHOD_TO_INTERCEPT = "connect";

    /**
     * @see MethodInterceptor#intercept(Object, java.lang.reflect.Method, Object[], net.sf.cglib.proxy.MethodProxy)
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        String methodName = method.getName();
        if ( methodName.equals(METHOD_TO_INTERCEPT)){
            return null;
        }
        return proxy.invokeSuper(obj,args);
    }
}
