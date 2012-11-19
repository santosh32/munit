package org.mule.munit.common.endpoint;

import org.junit.Before;
import org.junit.Test;
import org.mule.api.config.MuleProperties;
import org.mule.api.transport.Connector;
import org.mule.construct.Flow;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MunitSpringFactoryPostProcessorTest {

    private ConfigurableListableBeanFactory beanFactory;
    
    @Before
    public void setUp(){
        beanFactory = mock(ConfigurableListableBeanFactory.class);
                
    }

    @Test
    public void testPostProcessBeanFactoryWithoutMocking(){
        MunitSpringFactoryPostProcessor pp = new MunitSpringFactoryPostProcessor();
        pp.setMockInbounds(false);
        pp.setMockingExcludedFlows(new ArrayList<String>());
        when(beanFactory.getBeanDefinition(MuleProperties.OBJECT_MULE_ENDPOINT_FACTORY)).thenReturn(new GenericBeanDefinition());
        
        pp.postProcessBeanFactory(beanFactory);
        
        verify(beanFactory, times(1)).getBeanDefinition(any(String.class));
    }

    @Test
    public void testPostProcessBeanFactoryWithMockingExcludedFlows(){
        MunitSpringFactoryPostProcessor pp = new MunitSpringFactoryPostProcessor();
        pp.setMockInbounds(true);
        pp.setMockingExcludedFlows(new ArrayList<String>());

        when(beanFactory.getBeanDefinitionNames()).thenReturn(new String[]{"beanName"});
        when(beanFactory.getBeanDefinition("beanName")).thenReturn(createBeanDefinition());
        when(beanFactory.getBeanDefinition(MuleProperties.OBJECT_MULE_ENDPOINT_FACTORY)).thenReturn(new GenericBeanDefinition());
        pp.postProcessBeanFactory(beanFactory);

    }

    @Test
    public void testMockConnectors(){
        MunitSpringFactoryPostProcessor pp = new MunitSpringFactoryPostProcessor();
        pp.setMockConnectors(true);
        pp.setMockingExcludedFlows(new ArrayList<String>());

        when(beanFactory.getBeanDefinitionNames()).thenReturn(new String[]{"beanName"});
        when(beanFactory.getBeanNamesForType(Connector.class)).thenReturn(new String[]{"beanName"});
        when(beanFactory.getBeanDefinition("beanName")).thenReturn(createConnectionDefinition());
        when(beanFactory.getBeanDefinition(MuleProperties.OBJECT_MULE_ENDPOINT_FACTORY)).thenReturn(new GenericBeanDefinition());

        pp.postProcessBeanFactory(beanFactory);
    }

    private BeanDefinition createConnectionDefinition() {
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition();

        rootBeanDefinition.setBeanClass(Connector.class);
        return rootBeanDefinition;

    }

    private RootBeanDefinition createBeanDefinition() {
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition();
        rootBeanDefinition.setBeanClass(Flow.class);
        rootBeanDefinition.setBeanClassName(Flow.class.getName());

        return rootBeanDefinition;
    }
}
