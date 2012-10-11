package org.mule.munit.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import javax.annotation.Generated;


/**
 * Registers bean definitions parsers for handling elements in <code>http://www.mulesoft.org/schema/mule/mock</code>.
 * 
 */
@Generated(value = "Mule DevKit Version 3.3.1", date = "2012-09-24T08:53:54-03:00", comments = "Build UNNAMED.1297.150f2c9")
public class MockNamespaceHandler
    extends NamespaceHandlerSupport
{


    /**
     * Invoked by the {@link DefaultBeanDefinitionDocumentReader} after construction but before any custom elements are parsed. 
     * @see org.springframework.beans.factory.xml.NamespaceHandlerSupport#registerBeanDefinitionParser(String, BeanDefinitionParser)
     * 
     */
    public void init() {
        registerBeanDefinitionParser("config", new MockModuleConfigDefinitionParser());
        registerBeanDefinitionParser("expect", new ExpectDefinitionParser());
        registerBeanDefinitionParser("expect-fail", new ExpectFailDefinitionParser());
        registerBeanDefinitionParser("verify-call", new VerifyCallDefinitionParser());
        registerBeanDefinitionParser("fail-on-connect", new FailOnConnectDefinitionParser());
        registerBeanDefinitionParser("reset", new ResetDefinitionParser());
        registerBeanDefinitionParser("outbound-endpoint-behavior", new OutboundEndpointDefinitionParser());
    }

}
