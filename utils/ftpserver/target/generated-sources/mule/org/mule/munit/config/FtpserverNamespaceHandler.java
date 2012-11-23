
package org.mule.munit.config;

import javax.annotation.Generated;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;


/**
 * Registers bean definitions parsers for handling elements in <code>http://www.mulesoft.org/schema/mule/ftpserver</code>.
 * 
 */
@Generated(value = "Mule DevKit Version 3.3.1", date = "2012-11-23T03:25:05-03:00", comments = "Build 3.3.1.1298.3ae82a7")
public class FtpserverNamespaceHandler
    extends NamespaceHandlerSupport
{


    /**
     * Invoked by the {@link DefaultBeanDefinitionDocumentReader} after construction but before any custom elements are parsed. 
     * @see NamespaceHandlerSupport#registerBeanDefinitionParser(String, BeanDefinitionParser)
     * 
     */
    public void init() {
        registerBeanDefinitionParser("config", new FTPServerModuleConfigDefinitionParser());
        registerBeanDefinitionParser("start-server", new StartServerDefinitionParser());
        registerBeanDefinitionParser("contains-files", new ContainsFilesDefinitionParser());
        registerBeanDefinitionParser("stop-server", new StopServerDefinitionParser());
        registerBeanDefinitionParser("remove", new RemoveDefinitionParser());
    }

}
