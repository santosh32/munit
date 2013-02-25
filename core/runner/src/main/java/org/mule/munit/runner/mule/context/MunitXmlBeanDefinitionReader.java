package org.mule.munit.runner.mule.context;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

public class MunitXmlBeanDefinitionReader extends XmlBeanDefinitionReader{

    /**
     * Create new XmlBeanDefinitionReader for the given bean factory.
     *
     * @param registry the BeanFactory to load bean definitions into,
     *                 in the form of a BeanDefinitionRegistry
     */
    public MunitXmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
        setDocumentLoader(new MunitDocumentLoader());
    }
}
