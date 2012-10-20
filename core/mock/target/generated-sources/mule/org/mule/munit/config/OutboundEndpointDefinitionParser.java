
package org.mule.munit.config;

import javax.annotation.Generated;
import org.mule.config.spring.factories.MessageProcessorChainFactoryBean;
import org.mule.munit.config.AbstractDefinitionParser.ParseDelegate;
import org.mule.munit.processors.OutboundEndpointMessageProcessor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

@Generated(value = "Mule DevKit Version 3.3.1", date = "2012-10-20T12:55:18-03:00", comments = "Build UNNAMED.1297.150f2c9")
public class OutboundEndpointDefinitionParser
    extends AbstractDefinitionParser
{


    public BeanDefinition parse(Element element, ParserContext parserContext) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(OutboundEndpointMessageProcessor.class.getName());
        builder.setScope(BeanDefinition.SCOPE_PROTOTYPE);
        parseConfigRef(element, builder);
        parseProperty(builder, element, "address", "address");
        if (hasAttribute(element, "returnPayload-ref")) {
            if (element.getAttribute("returnPayload-ref").startsWith("#")) {
                builder.addPropertyValue("returnPayload", element.getAttribute("returnPayload-ref"));
            } else {
                builder.addPropertyValue("returnPayload", (("#[registry:"+ element.getAttribute("returnPayload-ref"))+"]"));
            }
        }
        parseMapAndSetProperty(element, builder, "returnInvocationProperties", "return-invocation-properties", "return-invocation-property", new ParseDelegate<String>() {


            public String parse(Element element) {
                return element.getTextContent();
            }

        }
        );
        parseMapAndSetProperty(element, builder, "returnInboundProperties", "return-inbound-properties", "return-inbound-property", new ParseDelegate<String>() {


            public String parse(Element element) {
                return element.getTextContent();
            }

        }
        );
        parseMapAndSetProperty(element, builder, "returnSessionProperties", "return-session-properties", "return-session-property", new ParseDelegate<String>() {


            public String parse(Element element) {
                return element.getTextContent();
            }

        }
        );
        parseMapAndSetProperty(element, builder, "returnOutboundProperties", "return-outbound-properties", "return-outbound-property", new ParseDelegate<String>() {


            public String parse(Element element) {
                return element.getTextContent();
            }

        }
        );
        parseNestedProcessorAsListAndSetProperty(element, "assertions", parserContext, MessageProcessorChainFactoryBean.class, builder, "assertions");
        BeanDefinition definition = builder.getBeanDefinition();
        setNoRecurseOnDefinition(definition);
        attachProcessorDefinition(parserContext, definition);
        return definition;
    }

}
