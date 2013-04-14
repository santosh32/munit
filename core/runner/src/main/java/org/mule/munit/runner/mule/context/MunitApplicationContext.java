package org.mule.munit.runner.mule.context;

import org.mule.api.MuleContext;
import org.mule.config.ConfigResource;
import org.mule.config.spring.MissingParserProblemReporter;
import org.mule.config.spring.MuleApplicationContext;
import org.mule.munit.common.connectors.ConnectorMethodInterceptorFactory;
import org.mule.munit.common.endpoint.MunitSpringFactoryPostProcessor;
import org.mule.munit.common.mp.MunitMessageProcessorInterceptorFactory;

import java.io.IOException;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.Resource;


public class MunitApplicationContext extends MuleApplicationContext{
    private MunitConfiguration configuration;

    public MunitApplicationContext(MuleContext muleContext, ConfigResource[] configResources, MunitConfiguration configuration) throws BeansException {
        super(muleContext, configResources);
        this.configuration = configuration;
    }

    public MunitApplicationContext(MuleContext muleContext, Resource[] springResources, MunitConfiguration configuration) throws BeansException {
        super(muleContext, springResources);
        this.configuration = configuration;
    }

    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws IOException {
        XmlBeanDefinitionReader beanDefinitionReader = new MunitXmlBeanDefinitionReader(beanFactory);
        //hook in our custom hierarchical reader
        beanDefinitionReader.setDocumentReaderClass(MunitBeanDefinitionDocumentReader.class);
        //add error reporting

        beanFactory.registerBeanDefinition(MunitMessageProcessorInterceptorFactory.ID, new RootBeanDefinition(MunitMessageProcessorInterceptorFactory.class));
        beanFactory.registerBeanDefinition(ConnectorMethodInterceptorFactory.ID, new RootBeanDefinition(ConnectorMethodInterceptorFactory.class));
        beanDefinitionReader.setProblemReporter(new MissingParserProblemReporter());

        if ( configuration != null ){
            RootBeanDefinition beanDefinition = new RootBeanDefinition();
            beanDefinition.setBeanClass(MunitSpringFactoryPostProcessor.class);
            MutablePropertyValues propertyValues = new MutablePropertyValues();
            propertyValues.add("mockInbounds", configuration.isMockInbounds());
            propertyValues.add("mockConnectors", configuration.isMockConnectors());
            propertyValues.add("mockingExcludedFlows", configuration.getMockingExcludedFlows());
            beanDefinition.setPropertyValues(propertyValues);
            beanFactory.registerBeanDefinition("___MunitSpringFactoryPostProcessor", beanDefinition);
        }
        // Communicate mule context to parsers
        try
        {
            getCurrentMuleContext().set(this.getMuleContext());
            beanDefinitionReader.loadBeanDefinitions(getConfigResources());
        }
        finally
        {

            getCurrentMuleContext().remove();
        }
    }


    @Override
    protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        super.prepareBeanFactory(beanFactory);
        MunitSpringFactoryPostProcessor bean = beanFactory.getBean(MunitSpringFactoryPostProcessor.class);
        bean.postProcessBeanFactory(beanFactory);
    }
}
