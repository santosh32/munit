package org.mule.munit.config.spring;

import org.mule.munit.config.SetMessageProcessor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class SetDefinitionParser
    extends AbstractDefinitionParser
{
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(SetMessageProcessor.class.getName());
        builder.setScope(BeanDefinition.SCOPE_PROTOTYPE);
        if (hasAttribute(element, "payload-ref")) {
            if (element.getAttribute("payload-ref").startsWith("#")) {
                builder.addPropertyValue("payload", element.getAttribute("payload-ref"));
            } else {
                builder.addPropertyValue("payload", (("#[registry:"+ element.getAttribute("payload-ref"))+"]"));
            }
        }
        parseMapAndSetProperty(element, builder, "invocationProperties", "invocation-properties", "invocation-property", new ParseDelegate<String>() {


            public String parse(Element element) {
                return element.getTextContent();
            }

        }
        );
        parseMapAndSetProperty(element, builder, "inboundProperties", "inbound-properties", "inbound-property", new ParseDelegate<String>() {


            public String parse(Element element) {
                return element.getTextContent();
            }

        }
        );
        parseMapAndSetProperty(element, builder, "sessionProperties", "session-properties", "session-property", new ParseDelegate<String>() {


            public String parse(Element element) {
                return element.getTextContent();
            }

        }
        );
        parseMapAndSetProperty(element, builder, "outboundProperties", "outbound-properties", "outbound-property", new ParseDelegate<String>() {


            public String parse(Element element) {
                return element.getTextContent();
            }

        }
        );
        BeanDefinition definition = builder.getBeanDefinition();
        setNoRecurseOnDefinition(definition);
        attachProcessorDefinition(parserContext, definition);
        return definition;
    }


}
