package org.mule.munit.config.spring;

import org.apache.commons.lang.StringUtils;
import org.mule.config.spring.MuleHierarchicalBeanDefinitionParserDelegate;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *     Munit Bean Definition Parser
 * </p>
 *
 * @author Federico, Fernando
 * @since 3.3.2
 */
public class MunitDefinitionParser implements BeanDefinitionParser {

    /**
     * <p>
     *     The message processor class
     * </p>
     */
    private Class mpClass;

    /**
     * <p>
     *     The message processor attributes to parse
     * </p>
     */
    private List<String> attributes;

    /**
     * <p>
     *     The ref attributes to parse
     * </p>
     */
    private List<String> refAttributes;

    /**
     * <p>
     *     Constructor for parsers that parse message processors with simple attributes
     * </p>
     *
     * @param mpClass
     *      <p>The message processor class</p>
     *
     * @param attributes
     *      <p>The message processor attributes to parse</p>
     */
    public MunitDefinitionParser(Class mpClass, List<String> attributes) {
        this(mpClass, attributes, new ArrayList<String>());
    }

    /**
     * <p>
     *     Constructor for parsers that parse message processors with simple attributes and complex ref-attributes
     * </p>
     *
     * @param mpClass
     *      <p>The message processor class</p>
     *
     * @param attributes
     *      <p>The message processor attributes to parse</p>
     *
     * @param refAttributes
     *      <p>The ref attributes to parse</p>
     */
    public MunitDefinitionParser(Class mpClass, List<String> attributes, List<String> refAttributes) {
        this.mpClass = mpClass;
        this.attributes = attributes;
        this.refAttributes = refAttributes;

    }

    /**
     * <p>
     *     Constructor for parsers that parse message processors with no attributes
     * </p>
     *
     * @param mpClass
     *      <p>The message processor class</p>
     */
    public MunitDefinitionParser(Class mpClass) {
        this(mpClass, new ArrayList<String>(), new ArrayList<String>());
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

    public BeanDefinition parse(Element element, ParserContext parserContent) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(mpClass.getName());
        builder.setScope(BeanDefinition.SCOPE_PROTOTYPE);
        for ( String attribute : attributes ){
            if ((element.getAttribute(attribute) != null) && (!StringUtils.isBlank(element.getAttribute(attribute)))) {
                builder.addPropertyValue(attribute, element.getAttribute(attribute));
            }
        }

        for ( String refAttribute : refAttributes ){
            String attrWithRefAttached = refAttribute + "-ref";
            if ((element.getAttribute(attrWithRefAttached) != null) && (!StringUtils.isBlank(element.getAttribute(attrWithRefAttached)))) {
                if (element.getAttribute(attrWithRefAttached).startsWith("#")) {
                    builder.addPropertyValue(refAttribute, element.getAttribute(attrWithRefAttached));
                } else {
                    builder.addPropertyValue(refAttribute, new RuntimeBeanReference(element.getAttribute(attrWithRefAttached)));
                }
            }
        }


        BeanDefinition definition = builder.getBeanDefinition();
        setNoRecurseOnDefinition(definition);
        attachProcessorDefinition(parserContent, definition);

        return definition;
    }
    
}
