
package org.mule.munit.config.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;


/**
 * Registers bean definitions parsers for handling elements in <code>http://www.mulesoft.org/schema/mule/ftpserver</code>.
 * 
 */
public class FTPServerModuleNamespaceHandler
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
