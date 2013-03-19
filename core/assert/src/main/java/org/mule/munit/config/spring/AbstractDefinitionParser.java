package org.mule.munit.config.spring;

import org.apache.commons.lang.StringUtils;
import org.mule.config.spring.MuleHierarchicalBeanDefinitionParserDelegate;
import org.mule.util.TemplateParser;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import java.util.List;

/**
 * <p>
 *     Assert Module Definition Parser
 * </p>
 *
 * @author Federico, Fernando
 * @since 3.3.2
 */
public abstract class AbstractDefinitionParser implements BeanDefinitionParser
{

    private TemplateParser.PatternInfo patternInfo;

    public AbstractDefinitionParser() {
        patternInfo = TemplateParser.createMuleStyleParser().getStyle();
    }

    protected boolean hasAttribute(Element element, String attributeName) {
        String value = element.getAttribute(attributeName);
        if ((value!= null)&&(!StringUtils.isBlank(value))) {
            return true;
        }
        return false;
    }

    protected void setRef(BeanDefinitionBuilder builder, String propertyName, String ref) {
        if (!isMuleExpression(ref)) {
            builder.addPropertyValue(propertyName, new RuntimeBeanReference(ref));
        } else {
            builder.addPropertyValue(propertyName, ref);
        }
    }

    protected boolean isMuleExpression(String value) {
        if ((!value.startsWith(patternInfo.getPrefix()))&&(!value.endsWith(patternInfo.getSuffix()))) {
            return false;
        } else {
            return true;
        }
    }

    protected ManagedMap parseMap(Element element, String childElementName, AbstractDefinitionParser.ParseDelegate parserDelegate) {
        ManagedMap managedMap = new ManagedMap();
        List<Element> childDomElements = DomUtils.getChildElementsByTagName(element, childElementName);
        if (childDomElements.size() == 0) {
            childDomElements = DomUtils.getChildElements(element);
        }
        for (Element childDomElement: childDomElements) {
            Object key = null;
            if (hasAttribute(childDomElement, "key-ref")) {
                key = new RuntimeBeanReference(childDomElement.getAttribute("key-ref"));
            } else {
                if (hasAttribute(childDomElement, "key")) {
                    key = childDomElement.getAttribute("key");
                } else {
                    key = childDomElement.getTagName();
                }
            }
            if (hasAttribute(childDomElement, "value-ref")) {
                if (!isMuleExpression(childDomElement.getAttribute("value-ref"))) {
                    managedMap.put(key, new RuntimeBeanReference(childDomElement.getAttribute("value-ref")));
                } else {
                    managedMap.put(key, childDomElement.getAttribute("value-ref"));
                }
            } else {
                managedMap.put(key, parserDelegate.parse(childDomElement));
            }
        }
        return managedMap;
    }

    protected void parseMapAndSetProperty(Element element, BeanDefinitionBuilder builder, String fieldName, String parentElementName, String childElementName, AbstractDefinitionParser.ParseDelegate parserDelegate) {
        Element domElement = DomUtils.getChildElementByTagName(element, parentElementName);
        if (domElement!= null) {
            if (hasAttribute(domElement, "ref")) {
                setRef(builder, fieldName, domElement.getAttribute("ref"));
            } else {
                ManagedMap managedMap = parseMap(domElement, childElementName, parserDelegate);
                builder.addPropertyValue(fieldName, managedMap);
            }
        }
    }

    protected void attachProcessorDefinition(ParserContext parserContext, BeanDefinition definition) {
        MutablePropertyValues propertyValues = parserContext.getContainingBeanDefinition().getPropertyValues();
        if (parserContext.getContainingBeanDefinition().getBeanClassName().equals("org.mule.config.spring.factories.PollingMessageSourceFactoryBean")) {
            propertyValues.addPropertyValue("messageProcessor", definition);
        } else {
            if (parserContext.getContainingBeanDefinition().getBeanClassName().equals("org.mule.enricher.MessageEnricher")) {
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



    protected void setNoRecurseOnDefinition(BeanDefinition definition) {
        definition.setAttribute(MuleHierarchicalBeanDefinitionParserDelegate.MULE_NO_RECURSE, Boolean.TRUE);
    }

    public interface ParseDelegate<T >{
        public T parse(Element element);
    }

}
