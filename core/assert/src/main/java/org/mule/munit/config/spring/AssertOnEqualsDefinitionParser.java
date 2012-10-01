package org.mule.munit.config.spring;

import org.apache.commons.lang.StringUtils;
import org.mule.config.spring.MuleHierarchicalBeanDefinitionParserDelegate;
import org.mule.munit.config.AssertOnEqualsMessageProcessor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * <p>Assert on equals Definition Parser</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class AssertOnEqualsDefinitionParser
        implements BeanDefinitionParser {

    public BeanDefinition parse(Element element, ParserContext parserContent) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(AssertOnEqualsMessageProcessor.class.getName());
        String configRef = element.getAttribute("org.mule.munit.config-ref");
        if ((configRef != null) && (!StringUtils.isBlank(configRef))) {
            builder.addPropertyValue("moduleObject", configRef);
        }
        if ((element.getAttribute("message") != null) && (!StringUtils.isBlank(element.getAttribute("message")))) {
            builder.addPropertyValue("message", element.getAttribute("message"));
        }
        if ((element.getAttribute("expected-ref") != null) && (!StringUtils.isBlank(element.getAttribute("expected-ref")))) {
            if (element.getAttribute("expected-ref").startsWith("#")) {
                builder.addPropertyValue("expected", element.getAttribute("expected-ref"));
            } else {
                builder.addPropertyValue("expected", (("#[registry:" + element.getAttribute("expected-ref")) + "]"));
            }
        }
        if ((element.getAttribute("value-ref") != null) && (!StringUtils.isBlank(element.getAttribute("value-ref")))) {
            if (element.getAttribute("value-ref").startsWith("#")) {
                builder.addPropertyValue("value", element.getAttribute("value-ref"));
            } else {
                builder.addPropertyValue("value", (("#[registry:" + element.getAttribute("value-ref")) + "]"));
            }
        }
        BeanDefinition definition = builder.getBeanDefinition();
        definition.setAttribute(MuleHierarchicalBeanDefinitionParserDelegate.MULE_NO_RECURSE, Boolean.TRUE);
        return definition;
    }
}
