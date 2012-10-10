package org.mule.munit.config.spring;

import org.apache.commons.lang.StringUtils;
import org.mule.config.spring.MuleHierarchicalBeanDefinitionParserDelegate;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Munit Bean Definition Parser</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MunitDefinitionParser implements BeanDefinitionParser {

    private Class mpClass;
    private List<String> attributes;
    private List<String> refAttributes;

    public MunitDefinitionParser(Class mpClass, List<String> attributes) {
        this(mpClass, attributes, new ArrayList<String>());
    }

    public MunitDefinitionParser(Class mpClass, List<String> attributes, List<String> refAttributes) {
        this.mpClass = mpClass;
        this.attributes = attributes;
        this.refAttributes = refAttributes;

    }

    public MunitDefinitionParser(Class mpClass) {
        this(mpClass, new ArrayList<String>(), new ArrayList<String>());
    }

    public BeanDefinition parse(Element element, ParserContext parserContent) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(mpClass.getName());
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
        definition.setAttribute(MuleHierarchicalBeanDefinitionParserDelegate.MULE_NO_RECURSE, Boolean.TRUE);

        return definition;
    }
    
}
