
package org.mule.munit.config.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;


/**
 * Registers bean definitions parsers for handling elements in <code>http://www.mulesoft.org/schema/mule/munit</code>.
 * 
 */
public class AssertModuleNamespaceHandler
    extends NamespaceHandlerSupport
{


    /**
     * Invoked by the {@link DefaultBeanDefinitionDocumentReader} after construction but before any custom elements are parsed. 
     * @see NamespaceHandlerSupport#registerBeanDefinitionParser(String, BeanDefinitionParser)
     * 
     */
    public void init() {
        registerBeanDefinitionParser("config", new AssertModuleConfigDefinitionParser());
        registerBeanDefinitionParser("assert-that", new AssertThatDefinitionParser());
        registerBeanDefinitionParser("assert-true", new AssertTrueDefinitionParser());
        registerBeanDefinitionParser("assert-on-equals", new AssertOnEqualsDefinitionParser());
        registerBeanDefinitionParser("assert-not-same", new AssertNotSameDefinitionParser());
        registerBeanDefinitionParser("assert-false", new AssertFalseDefinitionParser());
        registerBeanDefinitionParser("assert-not-null", new AssertNotNullDefinitionParser());
        registerBeanDefinitionParser("assert-null", new AssertNullDefinitionParser());
        registerBeanDefinitionParser("set", new SetDefinitionParser());
        registerBeanDefinitionParser("set-null-payload", new SetNullPayloadDefinitionParser());
        registerBeanDefinitionParser("fail", new FailDefinitionParser());
    }

}
