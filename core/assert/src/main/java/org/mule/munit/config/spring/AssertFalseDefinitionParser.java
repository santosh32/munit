package org.mule.munit.config.spring;

import org.apache.commons.lang.StringUtils;
import org.mule.config.spring.MuleHierarchicalBeanDefinitionParserDelegate;
import org.mule.munit.config.AssertFalseMessageProcessor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * <p>Assert false Definition Parser</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class AssertFalseDefinitionParser
        implements BeanDefinitionParser {


    public BeanDefinition parse(Element element, ParserContext parserContent) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(AssertFalseMessageProcessor.class.getName());
        if ((element.getAttribute("message") != null) && (!StringUtils.isBlank(element.getAttribute("message")))) {
            builder.addPropertyValue("message", element.getAttribute("message"));
        }
        if ((element.getAttribute("condition") != null) && (!StringUtils.isBlank(element.getAttribute("condition")))) {
            builder.addPropertyValue("condition", element.getAttribute("condition"));
        }
        BeanDefinition definition = builder.getBeanDefinition();
        definition.setAttribute(MuleHierarchicalBeanDefinitionParserDelegate.MULE_NO_RECURSE, Boolean.TRUE);

        return definition;
    }


}
