package org.mule.munit.config.spring;

import org.apache.commons.lang.StringUtils;
import org.mule.config.spring.MuleHierarchicalBeanDefinitionParserDelegate;
import org.mule.munit.config.AssertThatMessageProcessor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * <p>Assert That  Definition Parser</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class AssertThatDefinitionParser
        implements BeanDefinitionParser {

    public BeanDefinition parse(Element element, ParserContext parserContent) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(AssertThatMessageProcessor.class.getName());
        if ((element.getAttribute("message") != null) && (!StringUtils.isBlank(element.getAttribute("message")))) {
            builder.addPropertyValue("message", element.getAttribute("message"));
        }
        if ((element.getAttribute("payloadIs-ref") != null) && (!StringUtils.isBlank(element.getAttribute("payloadIs-ref")))) {
            if (element.getAttribute("payloadIs-ref").startsWith("#")) {
                builder.addPropertyValue("payloadIs", element.getAttribute("payloadIs-ref"));
            } else {
                builder.addPropertyValue("payloadIs", (("#[registry:" + element.getAttribute("payloadIs-ref")) + "]"));
            }
        }
        if ((element.getAttribute("payload-ref") != null) && (!StringUtils.isBlank(element.getAttribute("payload-ref")))) {
            if (element.getAttribute("payload-ref").startsWith("#")) {
                builder.addPropertyValue("payload", element.getAttribute("payload-ref"));
            } else {
                builder.addPropertyValue("payload", (("#[registry:" + element.getAttribute("payload-ref")) + "]"));
            }
        }
        BeanDefinition definition = builder.getBeanDefinition();
        definition.setAttribute(MuleHierarchicalBeanDefinitionParserDelegate.MULE_NO_RECURSE, Boolean.TRUE);
        return definition;
    }

}
