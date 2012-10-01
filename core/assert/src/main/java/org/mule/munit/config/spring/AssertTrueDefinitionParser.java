package org.mule.munit.config.spring;

import org.apache.commons.lang.StringUtils;
import org.mule.config.spring.MuleHierarchicalBeanDefinitionParserDelegate;
import org.mule.munit.config.AssertTrueMessageProcessor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * <p>Assert true Definition Parser</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class AssertTrueDefinitionParser
        implements BeanDefinitionParser {

    public BeanDefinition parse(Element element, ParserContext parserContent) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(AssertTrueMessageProcessor.class.getName());
        String configRef = element.getAttribute("org.mule.munit.config-ref");
        if ((configRef != null) && (!StringUtils.isBlank(configRef))) {
            builder.addPropertyValue("moduleObject", configRef);
        }
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
