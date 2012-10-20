package org.mule.munit.runner.mule.context;

import org.apache.commons.lang.StringUtils;
import org.mule.api.processor.MessageProcessor;
import org.mule.config.spring.MuleHierarchicalBeanDefinitionParserDelegate;
import org.mule.construct.Flow;
import org.mule.munit.common.mp.MunitMessageProcessor;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.NamespaceHandler;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.List;
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
            if (isMessageProcessor(beanType)){
                String tagName = element.getTagName();
                
                if ( !StringUtils.isEmpty(tagName) ){
                    BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(MunitMessageProcessor.class.getName());
                    builder.setScope(beanDefinition.getScope());
                    builder.addPropertyValue("realMp", beanDefinition);
                    builder.addPropertyValue("namespace", getNamespaceFrom(tagName));
                    builder.addPropertyValue("name", getNameFrom(tagName));
                    builder.addPropertyValue("attributes", getAttributes(element));
                    AbstractBeanDefinition mocked = builder.getBeanDefinition();
                    setNoRecurseOnDefinition(mocked);
                    removeProcessorDefinition(parserContext, beanDefinition);
                    attachProcessorDefinition(parserContext, mocked);
                    return mocked;  
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
        return MessageProcessor.class.isAssignableFrom(beanType) && !Flow.class.isAssignableFrom(beanType);
    }
    protected void setNoRecurseOnDefinition(BeanDefinition definition) {
        definition.setAttribute(MuleHierarchicalBeanDefinitionParserDelegate.MULE_NO_RECURSE, Boolean.TRUE);
    }

    protected void attachProcessorDefinition(ParserContext parserContext, BeanDefinition definition) {
        BeanDefinition containingBeanDefinition = parserContext.getContainingBeanDefinition();
        if ( containingBeanDefinition == null ){
            return;
        }
        MutablePropertyValues propertyValues = containingBeanDefinition.getPropertyValues();
        if (containingBeanDefinition.getBeanClassName().equals("org.mule.config.spring.factories.PollingMessageSourceFactoryBean")) {
            propertyValues.addPropertyValue("messageProcessor", definition);
        } else {
            if (containingBeanDefinition.getBeanClassName().equals("org.mule.enricher.MessageEnricher")) {
                propertyValues.addPropertyValue("enrichmentMessageProcessor", definition);
            } else {
                PropertyValue messageProcessors = propertyValues.getPropertyValue("messageProcessors");
                if ((messageProcessors == null)||(messageProcessors.getValue() == null)) {
                    propertyValues.addPropertyValue("messageProcessors", new ManagedList());
                }
                List listMessageProcessors = ((List) propertyValues.getPropertyValue("messageProcessors").getValue());
                listMessageProcessors.add(definition);
            }
        }
    }

    // TODO: fix this for enrichers
    protected void removeProcessorDefinition(ParserContext parserContext, BeanDefinition definition) {
        BeanDefinition containingBeanDefinition = parserContext.getContainingBeanDefinition();
        if ( containingBeanDefinition == null ){
            return;
        }
        MutablePropertyValues propertyValues = containingBeanDefinition.getPropertyValues();
        if (containingBeanDefinition.getBeanClassName().equals("org.mule.config.spring.factories.PollingMessageSourceFactoryBean")) {
            propertyValues.addPropertyValue("messageProcessor", definition);
        } else {
            if (containingBeanDefinition.getBeanClassName().equals("org.mule.enricher.MessageEnricher")) {
                propertyValues.addPropertyValue("enrichmentMessageProcessor", definition);
            } else {
                PropertyValue messageProcessors = propertyValues.getPropertyValue("messageProcessors");
                if ((messageProcessors == null)||(messageProcessors.getValue() == null)) {
                    return;
                }
                List listMessageProcessors = ((List) propertyValues.getPropertyValue("messageProcessors").getValue());
                listMessageProcessors.remove(definition);
            }
        }
    }

    @Override
    public BeanDefinitionHolder decorate(Node source, BeanDefinitionHolder definition, ParserContext parserContext) {
        return definition;
    }
}
