package org.mule.munit.runner.mule.context;

import org.mule.api.MuleContext;
import org.mule.config.ConfigResource;
import org.mule.config.spring.MissingParserProblemReporter;
import org.mule.config.spring.MuleApplicationContext;
import org.mule.munit.common.connectors.ConnectorCallBack;
import org.mule.munit.common.connectors.ConnectorCallBackFactory;
import org.mule.munit.common.endpoint.MunitSpringFactoryPostProcessor;
import org.mule.munit.common.mp.MunitMessageProcessorCallback;
import org.mule.munit.common.mp.MunitMessageProcessorCallbackFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
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

        beanFactory.registerBeanDefinition(MunitMessageProcessorCallback.ID, new RootBeanDefinition(MunitMessageProcessorCallbackFactory.class));
        beanFactory.registerBeanDefinition(ConnectorCallBack.ID, new RootBeanDefinition(ConnectorCallBackFactory.class));
        beanDefinitionReader.setProblemReporter(new MissingParserProblemReporter());

        if ( configuration != null ){
            this.addBeanFactoryPostProcessor(createPostProcessorFromConfiguration());
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

    private BeanFactoryPostProcessor createPostProcessorFromConfiguration() {
        MunitSpringFactoryPostProcessor postProcessor = new MunitSpringFactoryPostProcessor();
        postProcessor.setMockInbounds(configuration.isMockInbounds());
        postProcessor.setMockingExcludedFlows(configuration.getMockingExcludedFlows());
        return postProcessor;
    }
}
