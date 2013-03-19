package org.mule.munit.config.spring;

import org.mule.munit.config.*;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * <p>
 *     Assert Module Namespace Handler
 * </p>
 *
 * @author Federico, Fernando
 * @since 3.3.2
 */
public class AssertModuleNamespaceHandler
        extends NamespaceHandlerSupport {


    public void init() {
        registerBeanDefinitionParser("config", new AssertModuleConfigDefinitionParser());
        registerBeanDefinitionParser("assert-that", new MunitDefinitionParser(AssertThatMessageProcessor.class, asList("message"), asList("payloadIs")));
        registerBeanDefinitionParser("assert-true", new MunitDefinitionParser(AssertTrueMessageProcessor.class, asList("message", "condition")));
        registerBeanDefinitionParser("assert-on-equals", new MunitDefinitionParser(AssertOnEqualsMessageProcessor.class, asList("message"), asList("expected", "value")));
        registerBeanDefinitionParser("assert-not-same", new MunitDefinitionParser(AssertNotSameMessageProcessor.class, asList("message"), asList("expected", "value")));
        registerBeanDefinitionParser("assert-false", new MunitDefinitionParser(AssertFalseMessageProcessor.class, asList("message", "condition")));
        registerBeanDefinitionParser("assert-not-null", new MunitDefinitionParser(AssertNotNullMessageProcessor.class, asList("message")));
        registerBeanDefinitionParser("assert-null", new MunitDefinitionParser(AssertNullMessageProcessor.class, asList("message")));
        registerBeanDefinitionParser("set", new SetDefinitionParser());
        registerBeanDefinitionParser("run-custom", new MunitDefinitionParser(RunAssertionMessageProcessor.class, new ArrayList<String>(), asList("assertion")));
        registerBeanDefinitionParser("set-null-payload",  new MunitDefinitionParser(SetNullPayloadMessageProcessor.class));
        registerBeanDefinitionParser("fail", new MunitDefinitionParser(FailMessageProcessor.class, asList("message")));
        registerBeanDefinitionParser("test", new MunitTestDefinitionParser(MunitTestFlow.class));
        registerBeanDefinitionParser("before-test", new MUnitFlowDefinitionParser(MunitBeforeTest.class));
        registerBeanDefinitionParser("after-test", new MUnitFlowDefinitionParser(MunitAfterTest.class));
        registerBeanDefinitionParser("before-suite", new MUnitFlowDefinitionParser(MunitBeforeSuite.class));
        registerBeanDefinitionParser("after-suite", new MUnitFlowDefinitionParser(MunitAfterSuite.class));

    }
    
    
    private static List<String> asList(String ... attrs){
       return Arrays.asList(attrs);
    }

}
