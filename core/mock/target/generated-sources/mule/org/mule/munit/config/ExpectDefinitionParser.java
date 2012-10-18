
package org.mule.munit.config;

import javax.annotation.Generated;
import org.mule.munit.config.AbstractDefinitionParser.ParseDelegate;
import org.mule.munit.processors.ExpectMessageProcessor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

@Generated(value = "Mule DevKit Version 3.3.1", date = "2012-10-18T01:17:40-03:00", comments = "Build UNNAMED.1297.150f2c9")
public class ExpectDefinitionParser
    extends AbstractDefinitionParser
{


    public BeanDefinition parse(Element element, ParserContext parserContext) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(ExpectMessageProcessor.class.getName());
        builder.setScope(BeanDefinition.SCOPE_PROTOTYPE);
        parseConfigRef(element, builder);
        parseProperty(builder, element, "thatMessageProcessor", "thatMessageProcessor");
        parseMapAndSetProperty(element, builder, "parameters", "parameters", "parameter", new ParseDelegate<String>() {


            public String parse(Element element) {
                return element.getTextContent();
            }

        }
        );
        if (hasAttribute(element, "toReturn-ref")) {
            if (element.getAttribute("toReturn-ref").startsWith("#")) {
                builder.addPropertyValue("toReturn", element.getAttribute("toReturn-ref"));
            } else {
                builder.addPropertyValue("toReturn", (("#[registry:"+ element.getAttribute("toReturn-ref"))+"]"));
            }
        }
        parseProperty(builder, element, "toReturnResponseFrom", "toReturnResponseFrom");
        BeanDefinition definition = builder.getBeanDefinition();
        setNoRecurseOnDefinition(definition);
        attachProcessorDefinition(parserContext, definition);
        return definition;
    }

}
