
package org.mule.munit.config.spring;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.mule.config.spring.MuleHierarchicalBeanDefinitionParserDelegate;
import org.mule.config.spring.factories.MessageProcessorChainFactoryBean;
import org.mule.config.spring.util.SpringXMLUtils;
import org.mule.munit.config.CallMessageProcessor;
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

public class CallDefinitionParser
    implements BeanDefinitionParser
{

    /**
     * Mule Pattern Info
     * 
     */
    private TemplateParser.PatternInfo patternInfo;

    public CallDefinitionParser() {
        patternInfo = TemplateParser.createMuleStyleParser().getStyle();
    }

    public BeanDefinition parse(Element element, ParserContext parserContent) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(CallMessageProcessor.class.getName());
        String configRef = element.getAttribute("config-ref");
        if ((configRef!= null)&&(!StringUtils.isBlank(configRef))) {
            builder.addPropertyValue("moduleObject", configRef);
        }
        if ((element.getAttribute("path")!= null)&&(!StringUtils.isBlank(element.getAttribute("path")))) {
            builder.addPropertyValue("path", element.getAttribute("path"));
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
                ManagedMap parameters = new ManagedMap();
                parametersListChilds = DomUtils.getChildElementsByTagName(parametersListElement, "parameter");
                if (parametersListChilds!= null) {
                    if (parametersListChilds.size() == 0) {
                        parametersListChilds = DomUtils.getChildElements(parametersListElement);
                    }
                    for (Element parametersChild: parametersListChilds) {
                        String parametersValueRef = parametersChild.getAttribute("value-ref");
                        String parametersKeyRef = parametersChild.getAttribute("key-ref");
                        Object valueObject = null;
                        Object keyObject = null;
                        if ((parametersValueRef!= null)&&(!StringUtils.isBlank(parametersValueRef))) {
                            valueObject = new RuntimeBeanReference(parametersValueRef);
                        } else {
                            valueObject = parametersChild.getTextContent();
                        }
                        if ((parametersKeyRef!= null)&&(!StringUtils.isBlank(parametersKeyRef))) {
                            keyObject = new RuntimeBeanReference(parametersKeyRef);
                        } else {
                            keyObject = parametersChild.getAttribute("key");
                        }
                        if ((keyObject == null)||((keyObject instanceof String)&&StringUtils.isBlank(((String) keyObject)))) {
                            keyObject = parametersChild.getTagName();
                        }
                        parameters.put(keyObject, valueObject);
                    }
                }
                builder.addPropertyValue("parameters", parameters);
            }
        }
        if ((element.getAttribute("payload-ref")!= null)&&(!StringUtils.isBlank(element.getAttribute("payload-ref")))) {
            if (element.getAttribute("payload-ref").startsWith("#")) {
                builder.addPropertyValue("payload", element.getAttribute("payload-ref"));
            } else {
                builder.addPropertyValue("payload", (("#[registry:"+ element.getAttribute("payload-ref"))+"]"));
            }
        }
        Element responseProcessingElement = DomUtils.getChildElementByTagName(element, "response-processing");
        if (responseProcessingElement!= null) {
            String text = responseProcessingElement.getAttribute("text");
            if ((text!= null)&&(!StringUtils.isBlank(text))) {
                builder.addPropertyValue("responseProcessing", text);
            } else {
                BeanDefinitionBuilder responseProcessingBeanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(MessageProcessorChainFactoryBean.class);
                BeanDefinition responseProcessingBeanDefinition = responseProcessingBeanDefinitionBuilder.getBeanDefinition();
                parserContent.getRegistry().registerBeanDefinition(generateChildBeanName(responseProcessingElement), responseProcessingBeanDefinition);
                responseProcessingElement.setAttribute("name", generateChildBeanName(responseProcessingElement));
                responseProcessingBeanDefinitionBuilder.setSource(parserContent.extractSource(responseProcessingElement));
                responseProcessingBeanDefinitionBuilder.setScope(BeanDefinition.SCOPE_SINGLETON);
                List responseProcessingList = parserContent.getDelegate().parseListElement(responseProcessingElement, responseProcessingBeanDefinitionBuilder.getBeanDefinition());
                parserContent.getRegistry().removeBeanDefinition(generateChildBeanName(responseProcessingElement));
                builder.addPropertyValue("responseProcessing", responseProcessingList);
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
