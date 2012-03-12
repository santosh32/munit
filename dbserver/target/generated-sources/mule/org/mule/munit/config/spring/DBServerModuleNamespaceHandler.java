
package org.mule.munit.config.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;


/**
 * Registers bean definitions parsers for handling elements in <code>http://www.mulesoft.org/schema/mule/dbserver</code>.
 * 
 */
public class DBServerModuleNamespaceHandler
    extends NamespaceHandlerSupport
{


    /**
     * Invoked by the {@link DefaultBeanDefinitionDocumentReader} after construction but before any custom elements are parsed. 
     * @see NamespaceHandlerSupport#registerBeanDefinitionParser(String, BeanDefinitionParser)
     * 
     */
    public void init() {
        registerBeanDefinitionParser("config", new DBServerModuleConfigDefinitionParser());
        registerBeanDefinitionParser("start-db-server", new StartDbServerDefinitionParser());
        registerBeanDefinitionParser("execute", new ExecuteDefinitionParser());
        registerBeanDefinitionParser("stop-db-server", new StopDbServerDefinitionParser());
    }

}
