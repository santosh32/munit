
package org.mule.munit.config;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.mule.config.spring.MuleHierarchicalBeanDefinitionParserDelegate;
import org.mule.config.spring.factories.MessageProcessorChainFactoryBean;
import org.mule.config.spring.util.SpringXMLUtils;
import org.mule.munit.processors.OutboundEndpointMessageProcessor;
import org.mule.util.TemplateParser;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class OutboundEndpointDefinitionParser
    implements BeanDefinitionParser
{

    /**
     * Mule Pattern Info
     * 
     */
    private TemplateParser.PatternInfo patternInfo;

    public OutboundEndpointDefinitionParser() {
        patternInfo = TemplateParser.createMuleStyleParser().getStyle();
    }

    public BeanDefinition parse(Element element, ParserContext parserContent) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(OutboundEndpointMessageProcessor.class.getName());
        String configRef = element.getAttribute("config-ref");
        if ((configRef!= null)&&(!StringUtils.isBlank(configRef))) {
            builder.addPropertyValue("moduleObject", configRef);
        }
        if ((element.getAttribute("address")!= null)&&(!StringUtils.isBlank(element.getAttribute("address")))) {
            builder.addPropertyValue("address", element.getAttribute("address"));
        }
        if ((element.getAttribute("returnValue-ref")!= null)&&(!StringUtils.isBlank(element.getAttribute("returnValue-ref")))) {
            if (element.getAttribute("returnValue-ref").startsWith("#")) {
                builder.addPropertyValue("returnValue", element.getAttribute("returnValue-ref"));
            } else {
                builder.addPropertyValue("returnValue", (("#[registry:"+ element.getAttribute("returnValue-ref"))+"]"));
            }
        }
        if (element!= null) {
            String text = element.getAttribute("text");
            if ((text!= null)&&(!StringUtils.isBlank(text))) {
                builder.addPropertyValue("assertions", text);
            } else {
                BeanDefinitionBuilder assertionsBeanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(MessageProcessorChainFactoryBean.class);
                BeanDefinition assertionsBeanDefinition = assertionsBeanDefinitionBuilder.getBeanDefinition();
                parserContent.getRegistry().registerBeanDefinition(generateChildBeanName(element), assertionsBeanDefinition);
                element.setAttribute("name", generateChildBeanName(element));
                assertionsBeanDefinitionBuilder.setSource(parserContent.extractSource(element));
                assertionsBeanDefinitionBuilder.setScope(BeanDefinition.SCOPE_SINGLETON);
                List assertionsList = parserContent.getDelegate().parseListElement(element, assertionsBeanDefinitionBuilder.getBeanDefinition());
                parserContent.getRegistry().removeBeanDefinition(generateChildBeanName(element));
                builder.addPropertyValue("assertions", assertionsList);
            }
        }
        BeanDefinition definition = builder.getBeanDefinition();
        definition.setAttribute(MuleHierarchicalBeanDefinitionParserDelegate.MULE_NO_RECURSE, Boolean.TRUE);
        MutablePropertyValues propertyValues = parserContent.getContainingBeanDefinition().getPropertyValues();
        if (parserContent.getContainingBeanDefinition().getBeanClassName().equals("org.mule.config.spring.factories.PollingMessageSourceFactoryBean")) {
            propertyValues.addPropertyValue("messageProcessor", definition);
        } else {
            if (parserContent.getContainingBeanDefinition().getBeanClassName().equals("org.mule.enricher.MessageEnricher")) {
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
        return definition;
    }

    protected String getAttributeValue(Element element, String attributeName) {
        if (!StringUtils.isEmpty(element.getAttribute(attributeName))) {
            return element.getAttribute(attributeName);
        }
        return null;
    }

    private String generateChildBeanName(Element element) {
        String id = SpringXMLUtils.getNameOrId(element);
        if (StringUtils.isBlank(id)) {
            String parentId = SpringXMLUtils.getNameOrId(((Element) element.getParentNode()));
            return ((("."+ parentId)+":")+ element.getLocalName());
        } else {
            return id;
        }
    }

}
