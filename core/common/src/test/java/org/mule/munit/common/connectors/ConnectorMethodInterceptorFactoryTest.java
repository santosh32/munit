package org.mule.munit.common.connectors;

import org.junit.Test;
import org.springframework.beans.factory.support.RootBeanDefinition;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class ConnectorMethodInterceptorFactoryTest {
    
    @Test
    public void checkCorrectId(){
        assertEquals("__munitConnectorInterceptorFactory", ConnectorMethodInterceptorFactory.ID);
    }

     @Test
    public void checkCorrectFactoryNames(){
         RootBeanDefinition beanDefinition = new RootBeanDefinition();
         ConnectorMethodInterceptorFactory.addFactoryDefinitionTo(beanDefinition);

         assertEquals("create", beanDefinition.getFactoryMethodName());
         assertEquals(ConnectorMethodInterceptorFactory.ID, beanDefinition.getFactoryBeanName());
     }

    @Test
    public void testCreateInterceptor(){
          assertTrue(new ConnectorMethodInterceptorFactory().createInterceptor() instanceof ConnectorMethodInterceptor);
    }

}
