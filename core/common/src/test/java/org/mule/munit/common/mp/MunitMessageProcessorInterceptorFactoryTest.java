package org.mule.munit.common.mp;

import net.sf.cglib.proxy.MethodInterceptor;
import org.junit.Test;
import org.mule.component.simple.EchoComponent;
import org.springframework.beans.factory.support.RootBeanDefinition;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MunitMessageProcessorInterceptorFactoryTest {
    
    @Test
    public void testIdIsCorrect(){
        assertEquals("__messageProcessorEnhancerFactory", MunitMessageProcessorInterceptorFactory.ID);
    }
    
    @Test
    public void testFactoryBeanNameAndMethod(){
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        MunitMessageProcessorInterceptorFactory.addFactoryDefinitionTo(beanDefinition);
        
        assertEquals("create", beanDefinition.getFactoryMethodName());
        assertEquals(MunitMessageProcessorInterceptorFactory.ID, beanDefinition.getFactoryBeanName());
    }

    @Test
    public void createCorrectInterceptor(){
        MethodInterceptor interceptor = new MunitMessageProcessorInterceptorFactory().createInterceptor();
        assertTrue(interceptor instanceof MunitMessageProcessorInterceptor);
    }


    @Test
    public void testCreate(){
        MunitMessageProcessorInterceptorFactory factory = new MunitMessageProcessorInterceptorFactory();

        factory.create(EchoComponent.class, new MessageProcessorId("name","namespace"), new HashMap<String, String>(), "fileName", "2");
    }
}
