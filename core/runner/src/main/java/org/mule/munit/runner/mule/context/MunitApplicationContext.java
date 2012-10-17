package org.mule.munit.runner.mule.context;

import org.mule.api.MuleContext;
import org.mule.config.ConfigResource;
import org.mule.config.spring.MissingParserProblemReporter;
import org.mule.config.spring.MuleApplicationContext;
import org.mule.config.spring.MuleBeanDefinitionDocumentReader;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.Resource;

import java.io.IOException;


public class MunitApplicationContext extends MuleApplicationContext{
    public MunitApplicationContext(MuleContext muleContext, ConfigResource[] configResources) throws BeansException {
        super(muleContext, configResources);
    }

    public MunitApplicationContext(MuleContext muleContext, Resource[] springResources) throws BeansException {
        super(muleContext, springResources);
    }

    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws IOException {
        XmlBeanDefinitionReader beanDefinitionReader = new MunitXmlBeanDefinitionReader(beanFactory);
        //hook in our custom hierarchical reader
        beanDefinitionReader.setDocumentReaderClass(MunitBeanDefinitionDocumentReader.class);
        //add error reporting
        beanDefinitionReader.setProblemReporter(new MissingParserProblemReporter());

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
}
