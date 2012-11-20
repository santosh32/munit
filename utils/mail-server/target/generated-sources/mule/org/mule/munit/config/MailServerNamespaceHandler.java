
package org.mule.munit.config;

import javax.annotation.Generated;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;


/**
 * Registers bean definitions parsers for handling elements in <code>http://www.mulesoft.org/schema/mule/mail-server</code>.
 * 
 */
@Generated(value = "Mule DevKit Version 3.3.1", date = "2012-11-20T12:38:06-03:00", comments = "Build UNNAMED.1297.150f2c9")
public class MailServerNamespaceHandler
    extends NamespaceHandlerSupport
{


    /**
     * Invoked by the {@link DefaultBeanDefinitionDocumentReader} after construction but before any custom elements are parsed. 
     * @see NamespaceHandlerSupport#registerBeanDefinitionParser(String, BeanDefinitionParser)
     * 
     */
    public void init() {
        registerBeanDefinitionParser("config", new MailServerModuleConfigDefinitionParser());
        registerBeanDefinitionParser("start-server", new StartServerDefinitionParser());
        registerBeanDefinitionParser("stop-server", new StopServerDefinitionParser());
        registerBeanDefinitionParser("get-received-messages", new GetReceivedMessagesDefinitionParser());
    }

}
