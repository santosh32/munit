package org.mule.munit.runner.mule.context;

import org.apache.commons.lang.StringUtils;
import org.mule.api.processor.MessageProcessor;
import org.mule.construct.Flow;
import org.mule.munit.common.mp.MessageProcessorId;
import org.mule.munit.common.mp.MunitMessageProcessorInterceptorFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.xml.NamespaceHandler;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.Map;

public class MunitHandlerWrapper implements NamespaceHandler {

    private NamespaceHandler realHandler;

    public MunitHandlerWrapper(NamespaceHandler realHandler) {
        this.realHandler = realHandler;
    }

    @Override
    public void init() {
    }

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        BeanDefinition beanDefinition = realHandler.parse(element, parserContext);
        if ( beanDefinition == null || element == null ){
            return beanDefinition;
        }

        try {
            Class<?> beanType = Class.forName(beanDefinition.getBeanClassName());
            if (isMessageProcessor(beanType) && AbstractBeanDefinition.class.isAssignableFrom(beanDefinition.getClass())){
                String tagName = element.getTagName();
                
                if ( !StringUtils.isEmpty(tagName) ){
                    String filename = parserContext.getReaderContext().getResource().getFilename();
                    MunitMessageProcessorInterceptorFactory.addFactoryDefinitionTo((AbstractBeanDefinition) beanDefinition)
                            .withConstructorArguments(beanType,new MessageProcessorId(getNameFrom(tagName), getNamespaceFrom(tagName)),
                                    getAttributes(element), filename, element.getAttribute("location"));
                    return beanDefinition;
                }
            }


        } catch (ClassNotFoundException e) {
            return beanDefinition;
        }

        return beanDefinition;
    }

    private Map<String, String> getAttributes(Element element) {
        Map<String,String> attrs = new HashMap<String, String>();
        NamedNodeMap attributes = element.getAttributes();
        for (int i=0 ; i<attributes.getLength(); i++){
            Node attr = attributes.item(i);
            attrs.put(attr.getNodeName(), attr.getNodeValue());
        }
        return attrs;
    }

    private String getNameFrom(String tagName) {
        String[] splitedName = tagName.split(":");
        if ( splitedName.length == 1 ){
            return splitedName[0];
        }
        else if (splitedName.length > 1) {
            return splitedName[1];
        }

        return "";
    }

    private String getNamespaceFrom(String tagName) {
        String[] splitedName = tagName.split(":");
        if ( splitedName.length > 1 ){
            return splitedName[0];
        }

        return "mule";
    }

    private boolean isMessageProcessor(Class<?> beanType) {
        return MessageProcessor.class.isAssignableFrom(beanType)
                && !Flow.class.isAssignableFrom(beanType);
    }

    @Override
    public BeanDefinitionHolder decorate(Node source, BeanDefinitionHolder definition, ParserContext parserContext) {
        return definition;
    }
}
