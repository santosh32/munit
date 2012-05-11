
package org.mule.munit.config.spring;

import org.mule.munit.config.*;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;


/**
 * Registers bean definitions parsers for handling elements in <code>http://www.mulesoft.org/schema/mule/munit</code>.
 * 
 */
public class AssertModuleNamespaceHandler
    extends NamespaceHandlerSupport
{


    /**
     * Invoked by the {@link org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader} after construction but before any custom elements are parsed.
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
        registerBeanDefinitionParser("add-expected", new AddExpectedDefinitionParser());
        registerBeanDefinitionParser("reset-calls", new ResetCallsDefinitionParser());
        registerBeanDefinitionParser("validate-calls", new ResetCallsDefinitionParser());
        registerBeanDefinitionParser("test", new MUnitFlowDefinitionParser(MunitTest.class));
        registerBeanDefinitionParser("before-test", new MUnitFlowDefinitionParser(MunitBeforeTest.class));
        registerBeanDefinitionParser("after-test", new MUnitFlowDefinitionParser(MunitAfterTest.class));
        registerBeanDefinitionParser("before-suite", new MUnitFlowDefinitionParser(MunitBeforeSuite.class));
        registerBeanDefinitionParser("after-suite", new MUnitFlowDefinitionParser(MunitAfterSuite.class));

    }

}
