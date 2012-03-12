
package org.mule.munit.config.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;


/**
 * Registers bean definitions parsers for handling elements in <code>http://www.mulesoft.org/schema/mule/mpmock</code>.
 * 
 */
public class MPMockModuleNamespaceHandler
    extends NamespaceHandlerSupport
{


    /**
     * Invoked by the {@link DefaultBeanDefinitionDocumentReader} after construction but before any custom elements are parsed. 
     * @see NamespaceHandlerSupport#registerBeanDefinitionParser(String, BeanDefinitionParser)
     * 
     */
    public void init() {
        registerBeanDefinitionParser("config", new MPMockModuleConfigDefinitionParser());
        registerBeanDefinitionParser("assert-payload", new AssertPayloadDefinitionParser());
        registerBeanDefinitionParser("respond", new RespondDefinitionParser());
    }

}
