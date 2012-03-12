
package org.mule.munit.config.spring;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.mule.config.spring.MuleHierarchicalBeanDefinitionParserDelegate;
import org.mule.config.spring.util.SpringXMLUtils;
import org.mule.munit.config.VerifyCallMessageProcessor;
import org.mule.util.TemplateParser;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

public class VerifyCallDefinitionParser
    implements BeanDefinitionParser
{

    /**
     * Mule Pattern Info
     * 
     */
    private TemplateParser.PatternInfo patternInfo;

    public VerifyCallDefinitionParser() {
        patternInfo = TemplateParser.createMuleStyleParser().getStyle();
    }

    public BeanDefinition parse(Element element, ParserContext parserContent) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(VerifyCallMessageProcessor.class.getName());
        String configRef = element.getAttribute("config-ref");
        if ((configRef!= null)&&(!StringUtils.isBlank(configRef))) {
            builder.addPropertyValue("moduleObject", configRef);
        }
        if ((element.getAttribute("messageProcessor")!= null)&&(!StringUtils.isBlank(element.getAttribute("messageProcessor")))) {
            builder.addPropertyValue("messageProcessor", element.getAttribute("messageProcessor"));
        }
        Element parametersListElement = null;
        parametersListElement = DomUtils.getChildElementByTagName(element, "parameters");
        List<Element> parametersListChilds = null;
        if (parametersListElement!= null) {
            String parametersRef = parametersListElement.getAttribute("ref");
            if ((parametersRef!= null)&&(!StringUtils.isBlank(parametersRef))) {
                if ((!parametersRef.startsWith(patternInfo.getPrefix()))&&(!parametersRef.endsWith(patternInfo.getSuffix()))) {
                    builder.addPropertyValue("parameters", new RuntimeBeanReference(parametersRef));
                } else {
                    builder.addPropertyValue("parameters", parametersRef);
                }
            } else {
                ManagedList parameters = new ManagedList();
                parametersListChilds = DomUtils.getChildElementsByTagName(parametersListElement, "parameter");
                if (parametersListChilds!= null) {
                    for (Element parametersChild: parametersListChilds) {
                        String valueRef = parametersChild.getAttribute("value-ref");
                        if ((valueRef!= null)&&(!StringUtils.isBlank(valueRef))) {
                            parameters.add(new RuntimeBeanReference(valueRef));
                        } else {
                            parameters.add(parametersChild.getTextContent());
                        }
                    }
                }
                builder.addPropertyValue("parameters", parameters);
            }
        }
        if ((element.getAttribute("times")!= null)&&(!StringUtils.isBlank(element.getAttribute("times")))) {
            builder.addPropertyValue("times", element.getAttribute("times"));
        }
        if ((element.getAttribute("atLeast")!= null)&&(!StringUtils.isBlank(element.getAttribute("atLeast")))) {
            builder.addPropertyValue("atLeast", element.getAttribute("atLeast"));
        }
        if ((element.getAttribute("atMost")!= null)&&(!StringUtils.isBlank(element.getAttribute("atMost")))) {
            builder.addPropertyValue("atMost", element.getAttribute("atMost"));
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
