package org.mule.munit.common.spring;

import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;

import java.util.Map;


/**
 * <p>Sets the constructor information to a bean definition</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class BeanFactoryMethodBuilder {

    /**
     * <p>The bean definition that has to be modified</p>
     */
    private AbstractBeanDefinition beanDefinition;


    /**
     * <p>Constructor. Sets the factory method name and the bean factory.</p>
     *
     * @param beanDefinition
     *        <p>The bean definition that has to be modified</p>
     * @param factoryMethodName
     *        <p>The factory method name</p>
     * @param beanFactoryName
     *        <p>The bean factory name</p>
     */
    public BeanFactoryMethodBuilder(AbstractBeanDefinition beanDefinition,
                                    String factoryMethodName,
                                    String beanFactoryName) {
        this.beanDefinition = beanDefinition;

        beanDefinition.setFactoryBeanName(beanFactoryName);
        beanDefinition.setFactoryMethodName(factoryMethodName);
    }

    /**
     * <p>Sets the constructor arguments to the bean definition</p>
     *
     * @param constructorArguments
     *      <p>The constructor arguments for the bean definition</p>
     *
     * @return
     *      <p>The bean definition with the one it was created.</p>
     */
    public AbstractBeanDefinition withConstructorArguments(Object... constructorArguments) {
        int argumentsSize = constructorArguments.length;
        ConstructorArgumentValues constructorArgumentValues = beanDefinition.getConstructorArgumentValues();

        ConstructorArgumentValues values = new ConstructorArgumentValues();
        Map<Integer, ConstructorArgumentValues.ValueHolder> indexedArgumentValues = constructorArgumentValues.getIndexedArgumentValues();

        for (Integer i : indexedArgumentValues.keySet()) {
            values.addIndexedArgumentValue(i + argumentsSize, indexedArgumentValues.get(i));
        }

        for (int i = 0; i < argumentsSize; i++) {
            values.addIndexedArgumentValue(i, constructorArguments[i]);
        }
        beanDefinition.setConstructorArgumentValues(values);

        return beanDefinition;
    }

}
