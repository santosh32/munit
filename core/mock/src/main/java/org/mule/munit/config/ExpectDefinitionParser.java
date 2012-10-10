package org.mule.munit.config;

import org.mule.munit.processors.ExpectMessageProcessor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import javax.annotation.Generated;

@Generated(value = "Mule DevKit Version 3.3.1", date = "2012-09-24T08:53:54-03:00", comments = "Build UNNAMED.1297.150f2c9")
public class ExpectDefinitionParser
    extends AbstractDefinitionParser
{


    public BeanDefinition parse(Element element, ParserContext parserContext) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(ExpectMessageProcessor.class.getName());
        builder.setScope(BeanDefinition.SCOPE_PROTOTYPE);
        parseConfigRef(element, builder);
        parseProperty(builder, element, "when", "when");
        parseMapAndSetProperty(element, builder, "parameters", "parameters", "parameter", new ParseDelegate<String>() {


            public String parse(Element element) {
                return element.getTextContent();
            }

        }
        );
        if (hasAttribute(element, "mustReturn-ref")) {
            if (element.getAttribute("mustReturn-ref").startsWith("#")) {
                builder.addPropertyValue("mustReturn", element.getAttribute("mustReturn-ref"));
            } else {
                builder.addPropertyValue("mustReturn", (("#[registry:"+ element.getAttribute("mustReturn-ref"))+"]"));
            }
        }
        parseProperty(builder, element, "mustReturnResponseFrom", "mustReturnResponseFrom");
        BeanDefinition definition = builder.getBeanDefinition();
        setNoRecurseOnDefinition(definition);
        attachProcessorDefinition(parserContext, definition);
        return definition;
    }

}
