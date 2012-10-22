
package org.mule.munit.config;

import javax.annotation.Generated;
import org.mule.munit.config.AbstractDefinitionParser.ParseDelegate;
import org.mule.munit.config.AbstractDefinitionParser.ParseDelegate;
import org.mule.munit.holders.AttributeExpressionHolder;
import org.mule.munit.holders.MunitMuleMessageExpressionHolder;
import org.mule.munit.processors.ExpectMessageProcessor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

@Generated(value = "Mule DevKit Version 3.3.1", date = "2012-10-22T05:47:49-03:00", comments = "Build UNNAMED.1297.150f2c9")
public class ExpectDefinitionParser
    extends AbstractDefinitionParser
{


    public BeanDefinition parse(Element element, ParserContext parserContext) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(ExpectMessageProcessor.class.getName());
        builder.setScope(BeanDefinition.SCOPE_PROTOTYPE);
        parseConfigRef(element, builder);
        parseProperty(builder, element, "messageProcessor", "messageProcessor");
        if (!parseObjectRef(element, builder, "to-return", "toReturn")) {
            BeanDefinitionBuilder toReturnBuilder = BeanDefinitionBuilder.rootBeanDefinition(MunitMuleMessageExpressionHolder.class.getName());
            Element toReturnChildElement = DomUtils.getChildElementByTagName(element, "to-return");
            if (toReturnChildElement!= null) {
                if (hasAttribute(toReturnChildElement, "payload-ref")) {
                    if (toReturnChildElement.getAttribute("payload-ref").startsWith("#")) {
                        toReturnBuilder.addPropertyValue("payload", toReturnChildElement.getAttribute("payload-ref"));
                    } else {
                        toReturnBuilder.addPropertyValue("payload", (("#[registry:"+ toReturnChildElement.getAttribute("payload-ref"))+"]"));
                    }
                }
                parseMapAndSetProperty(toReturnChildElement, toReturnBuilder, "invocationProperties", "invocation-properties", "invocation-property", new ParseDelegate<String>() {


                    public String parse(Element element) {
                        return element.getTextContent();
                    }

                }
                );
                parseMapAndSetProperty(toReturnChildElement, toReturnBuilder, "inboundProperties", "inbound-properties", "inbound-property", new ParseDelegate<String>() {


                    public String parse(Element element) {
                        return element.getTextContent();
                    }

                }
                );
                parseMapAndSetProperty(toReturnChildElement, toReturnBuilder, "sessionProperties", "session-properties", "session-property", new ParseDelegate<String>() {


                    public String parse(Element element) {
                        return element.getTextContent();
                    }

                }
                );
                parseMapAndSetProperty(toReturnChildElement, toReturnBuilder, "outboundProperties", "outbound-properties", "outbound-property", new ParseDelegate<String>() {


                    public String parse(Element element) {
                        return element.getTextContent();
                    }

                }
                );
                builder.addPropertyValue("toReturn", toReturnBuilder.getBeanDefinition());
            }
        }
        parseListAndSetProperty(element, builder, "attributes", "attributes", "attribute", new ParseDelegate<BeanDefinition>() {


            public BeanDefinition parse(Element element) {
                BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(AttributeExpressionHolder.class);
                parseProperty(builder, element, "name", "name");
                if (hasAttribute(element, "whereValue-ref")) {
                    if (element.getAttribute("whereValue-ref").startsWith("#")) {
                        builder.addPropertyValue("whereValue", element.getAttribute("whereValue-ref"));
                    } else {
                        builder.addPropertyValue("whereValue", (("#[registry:"+ element.getAttribute("whereValue-ref"))+"]"));
                    }
                }
                return builder.getBeanDefinition();
            }

        }
        );
        BeanDefinition definition = builder.getBeanDefinition();
        setNoRecurseOnDefinition(definition);
        attachProcessorDefinition(parserContext, definition);
        return definition;
    }

}
