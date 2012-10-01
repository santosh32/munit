package org.mule.munit.config.spring;

import org.mule.munit.config.*;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;


/**
 * <p>Assert Module Namespace Handler</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class AssertModuleNamespaceHandler
        extends NamespaceHandlerSupport {


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
        registerBeanDefinitionParser("test", new MunitTestDefinitionParser(MunitTest.class));
        registerBeanDefinitionParser("before-test", new MUnitFlowDefinitionParser(MunitBeforeTest.class));
        registerBeanDefinitionParser("after-test", new MUnitFlowDefinitionParser(MunitAfterTest.class));
        registerBeanDefinitionParser("before-suite", new MUnitFlowDefinitionParser(MunitBeforeSuite.class));
        registerBeanDefinitionParser("after-suite", new MUnitFlowDefinitionParser(MunitAfterSuite.class));

    }

}
