package org.mule.munit.common.endpoint;


import org.mule.api.config.MuleProperties;
import org.mule.construct.Flow;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;

import java.util.List;


/**
 * <p>This class changes the endpoint factory and inject the mock manager</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class EndpointFactorySwapperPostProcessor implements BeanFactoryPostProcessor {

    protected boolean mockInbounds;
    protected List<String> mockingExcludedFlows;

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        if (isMockInbounds() ){
            String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
            for ( String name : beanDefinitionNames ){
                BeanDefinition beanDefinition = beanFactory.getBeanDefinition(name);
                if ( Flow.class.getName().equals(beanDefinition.getBeanClassName()) ){
                    if ( !mockingExcludedFlows.contains(name) ){
                        beanDefinition.getPropertyValues().removePropertyValue("messageSource");
                    }
                }
            }

            wrapFactory(beanFactory);
        }
    }

    private void wrapFactory(ConfigurableListableBeanFactory beanFactory) {
        GenericBeanDefinition endpointFactory = (GenericBeanDefinition) beanFactory.getBeanDefinition(MuleProperties.OBJECT_MULE_ENDPOINT_FACTORY);

        AbstractBeanDefinition abstractBeanDefinition = endpointFactory.cloneBeanDefinition();

        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.add("defaultFactory", abstractBeanDefinition);
        endpointFactory.setPropertyValues(propertyValues);
        endpointFactory.setBeanClassName(MockEndpointManager.class.getCanonicalName());
    }

    public void setMockInbounds(boolean mockInbounds) {
        this.mockInbounds = mockInbounds;
    }

    public void setMockingExcludedFlows(List<String> mockingExcludedFlows) {
        this.mockingExcludedFlows = mockingExcludedFlows;
    }

    public boolean isMockInbounds() {
        return mockInbounds;
    }


    public List<String> getMockingExcludedFlows() {
        return mockingExcludedFlows;
    }

}
