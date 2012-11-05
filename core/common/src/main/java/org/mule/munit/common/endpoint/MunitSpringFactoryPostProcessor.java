package org.mule.munit.common.endpoint;


import org.mule.api.config.MuleProperties;
import org.mule.api.transport.Connector;
import org.mule.construct.Flow;
import org.mule.munit.common.connectors.ConnectorCallBack;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

import java.util.List;
import java.util.Map;


/**
 * <p>This class changes the endpoint factory and inject the mock manager</p>
 *
 * <p>This is a piece part of the endpoint mocking. By overriding the endpoint factory we can mock all the outbound/inbound
 * endpoints of a mule application</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MunitSpringFactoryPostProcessor implements BeanFactoryPostProcessor {

    /**
     * <p>Defines if the inbounds must be mocked or not. This is pure Munit configuration</p>
     */
    protected boolean mockInbounds;

    /**
     * <p>List of flows which we don't want to mock the inbound message sources</p>
     */
    protected List<String> mockingExcludedFlows;


    /**
     * <p>Implementation of the BeanFactoryPostProcessor. It removes the message sources of all the flows except
     * for the ones specified in mockingExcludedFlows. Only if mockInbounds is true.</p>
     *
     * @param beanFactory
     *          <p>The spring bean factory</p>
     * @throws BeansException
     *          <p>When post processing fails. Never thrown for this implementation</p>
     */
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

            swapFactory(beanFactory);

            String[] beanNamesForType = beanFactory.getBeanNamesForType(Connector.class);
            for (String beanName : beanNamesForType) {
                RootBeanDefinition beanDefinition = RootBeanDefinition.class.cast(beanFactory.getBeanDefinition(beanName));

                if (beanDefinition.getFactoryMethodName() == null) {
                    beanDefinition.setFactoryBeanName(ConnectorCallBack.ID);

                    ConstructorArgumentValues constructorArgumentValues = beanDefinition.getConstructorArgumentValues();
                    if (constructorArgumentValues != null && !constructorArgumentValues.isEmpty()) {
                        ConstructorArgumentValues values = new ConstructorArgumentValues();
                        Map<Integer, ConstructorArgumentValues.ValueHolder> indexedArgumentValues = constructorArgumentValues.getIndexedArgumentValues();

                        for (Integer i :  indexedArgumentValues.keySet()){
                            values.addIndexedArgumentValue(i+1,indexedArgumentValues.get(i));
                        }
                        values.addIndexedArgumentValue(0, beanDefinition.getBeanClass());
                        beanDefinition.setConstructorArgumentValues(values);
                    }


                    beanDefinition.setFactoryMethodName("create");
                } else {
                    // TODO: Log message
                }

            }
        }
    }

    /**
     * <p>Changes the default EndpointFactory of mule with a Wrapper of it. This wrapper creates mocks of the Outbound
     * Endpoints</p>
     *
     * @param beanFactory
     *           <p>The spring bean factory</p>
     */
    private void swapFactory(ConfigurableListableBeanFactory beanFactory) {
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

}
