package org.mule.munit.common.connectors;

import net.sf.cglib.proxy.MethodInterceptor;
import org.mule.munit.common.spring.BeanFactoryMethodBuilder;
import org.mule.munit.common.spring.MethodInterceptorFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;

/**
 *  <p>
 *      Factory to create the method interceptor for Mule Connector
 *  </p>
 *
 * @author Federico, Fernando
 * @since 3.3.2
 */
public class ConnectorMethodInterceptorFactory extends MethodInterceptorFactory {

    /**
     * <p>
     *     The bean definition ID in the mule registry
     * </p>
     */
    public static String ID = "__munitConnectorInterceptorFactory";

    /**
     * <p>
     *     Util method that creates a @see #BeanFactoryMethodBuilder based on an abstract bean definition
     * </p>
     *
     * <p>
     *     The usage:
     * </p>
     *
     * <code>
     *      addFactoryDefinitionTo(beanDefinition).withConstructorArguments(beanDefinition.getBeanClass());
     * </code>
     * @param beanDefinition
     *      <p>
     *          The bean definition that we want to modify
     *      </p>
     * @return
     *      <p>
     *          The {@link BeanFactoryMethodBuilder} that will do the job of adding constructor params to the bean definition
     *      </p>
     */
    public static BeanFactoryMethodBuilder addFactoryDefinitionTo(AbstractBeanDefinition beanDefinition){
        return new BeanFactoryMethodBuilder(beanDefinition,"create", ID);
    }

    /**
     * <p>
     *     Actual implementation of the interceptor creation
     * </p>
     *
     * @return
     *      <p>
     *          A {@link ConnectorMethodInterceptor} object
     *      </p>
     */
    @Override
    protected MethodInterceptor createInterceptor() {
        return new ConnectorMethodInterceptor();
    }
}
