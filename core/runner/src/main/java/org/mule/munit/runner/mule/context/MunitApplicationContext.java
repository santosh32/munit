package org.mule.munit.runner.mule.context;

import org.mule.api.MuleContext;
import org.mule.config.ConfigResource;
import org.mule.config.spring.MissingParserProblemReporter;
import org.mule.config.spring.MuleApplicationContext;
import org.mule.munit.AssertModule;
import org.mule.munit.common.connectors.ConnectorMethodInterceptorFactory;
import org.mule.munit.common.endpoint.MunitSpringFactoryPostProcessor;
import org.mule.munit.common.mp.MunitMessageProcessorInterceptorFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.Resource;

import java.io.IOException;


public class MunitApplicationContext extends MuleApplicationContext{
    private MockingConfiguration configuration;

    public MunitApplicationContext(MuleContext muleContext, ConfigResource[] configResources, MockingConfiguration configuration) throws BeansException {
        super(muleContext, configResources);
        this.configuration = configuration;
    }

    public MunitApplicationContext(MuleContext muleContext, Resource[] springResources, MockingConfiguration configuration) throws BeansException {
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
            MutablePropertyValues mutablePropertyValues = new MutablePropertyValues();
            mutablePropertyValues.add("mockInbounds", Boolean.valueOf(configuration.isMockInbounds()));
            mutablePropertyValues.add("mockConnectors", configuration.isMockConnectors());
            mutablePropertyValues.add("mockingExcludedFlows", configuration.getMockingExcludedFlows());
            beanDefinition.setPropertyValues(mutablePropertyValues);
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
            MunitSpringFactoryPostProcessor bean = beanFactory.getBean(MunitSpringFactoryPostProcessor.class);
            bean.postProcessBeanFactory(beanFactory);

            getCurrentMuleContext().remove();
        }
    }


}
