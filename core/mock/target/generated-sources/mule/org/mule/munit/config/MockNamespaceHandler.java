
package org.mule.munit.config;

import javax.annotation.Generated;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;


/**
 * Registers bean definitions parsers for handling elements in <code>http://www.mulesoft.org/schema/mule/mock</code>.
 * 
 */
@Generated(value = "Mule DevKit Version 3.3.1", date = "2012-10-20T04:33:25-03:00", comments = "Build UNNAMED.1297.150f2c9")
public class MockNamespaceHandler
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
        registerBeanDefinitionParser("spy", new SpyDefinitionParser());
        registerBeanDefinitionParser("verify-call", new VerifyCallDefinitionParser());
        registerBeanDefinitionParser("outbound-endpoint", new OutboundEndpointDefinitionParser());
    }

}
