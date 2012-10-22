package org.mule.munit.common.endpoint;

import org.junit.Before;
import org.junit.Test;
import org.mule.api.config.MuleProperties;
import org.mule.construct.Flow;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class EndpointFactorySwapperPostProcessorTest {

    private ConfigurableListableBeanFactory beanFactory;
    
    @Before
    public void setUp(){
        beanFactory = mock(ConfigurableListableBeanFactory.class);
                
    }

    @Test
    public void testPostProcessBeanFactoryWithoutMocking(){
        EndpointFactorySwapperPostProcessor pp = new EndpointFactorySwapperPostProcessor();
        pp.setMockInbounds(false);
        pp.setMockingExcludedFlows(new ArrayList<String>());
        
        pp.postProcessBeanFactory(beanFactory);
        
        verify(beanFactory, times(0)).getBeanDefinition(any(String.class));
    }

    @Test
    public void testPostProcessBeanFactoryWithMockingExcludedFlows(){
        EndpointFactorySwapperPostProcessor pp = new EndpointFactorySwapperPostProcessor();
        pp.setMockInbounds(true);
        pp.setMockingExcludedFlows(new ArrayList<String>());

        when(beanFactory.getBeanDefinitionNames()).thenReturn(new String[]{"beanName"});
        when(beanFactory.getBeanDefinition("beanName")).thenReturn(createBeanDefinition());
        when(beanFactory.getBeanDefinition(MuleProperties.OBJECT_MULE_ENDPOINT_FACTORY)).thenReturn(new GenericBeanDefinition());
        pp.postProcessBeanFactory(beanFactory);

    }

    private RootBeanDefinition createBeanDefinition() {
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition();
        rootBeanDefinition.setBeanClass(Flow.class);
        rootBeanDefinition.setBeanClassName(Flow.class.getName());

        return rootBeanDefinition;
    }
}
