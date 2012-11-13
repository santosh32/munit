package org.mule.munit.common.connectors;

import net.sf.cglib.proxy.MethodProxy;
import org.junit.Before;
import org.junit.Test;
import org.mule.api.transport.Connector;
import org.mule.munit.common.MunitCore;

import static junit.framework.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class ConnectorMethodInterceptorTest {

    MethodProxy proxy;
    
    @Before
    public void setUp(){
        proxy = mock(MethodProxy.class);
    }
            
    @Test
    public void whenConnectReturnNull() throws Throwable {

        ConnectorMethodInterceptor connectorMethodInterceptor = new ConnectorMethodInterceptor();

        Object interceptedResult = connectorMethodInterceptor.intercept(null, Connector.class.getMethod("connect"), null, proxy);

        
        verify(proxy, never()).invokeSuper(any(Object.class),any(Object[].class));

        assertNull(interceptedResult);
    }
    
    @Test
    public void whenNotConnectThenCallSuper() throws Throwable {
        ConnectorMethodInterceptor connectorMethodInterceptor = new ConnectorMethodInterceptor();
        connectorMethodInterceptor.intercept(null, Connector.class.getMethod("isConnected"), null, proxy);

        verify(proxy, times(1)).invokeSuper(any(Object.class), any(Object[].class));
        
    }



}
