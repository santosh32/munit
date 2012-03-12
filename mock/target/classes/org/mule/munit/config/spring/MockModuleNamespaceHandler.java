
package org.mule.munit.config.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;


/**
 * Registers bean definitions parsers for handling elements in <code>http://www.mulesoft.org/schema/mule/mock</code>.
 * 
 */
public class MockModuleNamespaceHandler
    extends NamespaceHandlerSupport
{


    /**
     * Invoked by the {@link DefaultBeanDefinitionDocumentReader} after construction but before any custom elements are parsed. 
     * @see NamespaceHandlerSupport#registerBeanDefinitionParser(String, BeanDefinitionParser)
     * 
     */
    public void init() {
        registerBeanDefinitionParser("config", new MockModuleConfigDefinitionParser());
        registerBeanDefinitionParser("expect", new ExpectDefinitionParser());
        registerBeanDefinitionParser("expect-fail", new ExpectFailDefinitionParser());
        registerBeanDefinitionParser("verify-call", new VerifyCallDefinitionParser());
        registerBeanDefinitionParser("fail-on-connect", new FailOnConnectDefinitionParser());
        registerBeanDefinitionParser("reset", new ResetDefinitionParser());
    }

}
