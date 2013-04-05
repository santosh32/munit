package org.mule.munit.runner.mule.context;

import org.mule.config.spring.MuleBeanDefinitionDocumentReader;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.w3c.dom.Element;

public class MunitBeanDefinitionDocumentReader extends MuleBeanDefinitionDocumentReader{

    @Override
    protected BeanDefinitionParserDelegate createHelper(XmlReaderContext readerContext, Element root, BeanDefinitionParserDelegate parentDelegate)
    {
        BeanDefinitionParserDelegate delegate = new MunitBeanDefinitionParserDelegate(readerContext, this);
        delegate.initDefaults(root, parentDelegate);
        return delegate;
    }
}
