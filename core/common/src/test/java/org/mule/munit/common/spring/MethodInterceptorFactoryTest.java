package org.mule.munit.common.spring;

import net.sf.cglib.proxy.MethodInterceptor;
import org.junit.Test;
import org.mule.munit.common.connectors.ConnectorMethodInterceptor;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

/**
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MethodInterceptorFactoryTest {

    @Test
    public void testCreate(){
        MethodInterceptorFactory factory = createFactory();
        TestObject object = (TestObject) factory.create(TestObject.class, "myString", 3);

        assertEquals("myString", object.getString1());
        assertEquals(new Integer(3), object.getInteger1());
    }
    
    @Test(expected = Error.class)
     public void testFailWhenIncorrectParameters(){
        MethodInterceptorFactory factory = createFactory();
        factory.create(TestObject.class, "myString");
    }

    @Test
    public void testWithNoParameters(){
        MethodInterceptorFactory factory = createFactory();
        TestObject object = (TestObject) factory.create(TestObject.class);
        assertNull(object.getInteger1());
        assertNull(object.getString1());
    }

    private MethodInterceptorFactory createFactory() {
        return new MethodInterceptorFactory() {

            @Override
            protected MethodInterceptor createInterceptor() {
                return new ConnectorMethodInterceptor();
            }
        };
    }
}
