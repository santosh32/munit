package org.mule.munit;


import org.mule.api.config.MuleProperties;
import org.mule.construct.Flow;
import org.mule.transport.NullPayload;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * <p>Module to assert payload's results</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class AssertModule  implements BeanFactoryPostProcessor
{
    private boolean mockInbounds;
    private List<String> mockingExcludedFlows;

    private static String wrapMessage(String message)
    {
        return message == null ? "" : message;
    }

    /**
     * <p>Assert that the payload is equal to an expected value.</p>
     *
     * <p>The payloadIs-ref can be any Object/expression. </p>
     * <p>The assertion Fails if the payload is not equal to the payloadIs-ref</p>
     *
     * @param payloadIs Expected Value
     * @param payload payload
     * @param message Description message to be shown in case of failure.
     */
    public void assertThat(String message, Object payloadIs, Object payload)
    {
        assertEquals(wrapMessage(message), payloadIs, payload);
    }

    /**
     * <p>Assert for a true expression.</p>
     *
     * @param condition Boolean expression
     * @param message Description message to be shown in case of failure.
     */
    public void assertTrue(String message, Boolean condition)
    {
        junit.framework.Assert.assertTrue(wrapMessage(message), condition);
    }


    /**
     * <p>Check that two objects are equal.</p>
     *
     * @param expected Expected value.  If not provided the expected value is taken from the expected value Queue.
     * @param value Real value
     * @param message Description message to be shown in case of failure.
     */
    public void assertOnEquals(String message, Object expected, Object value)
    {
        assertEquals(wrapMessage(message), expected, value);
    }


    /**
     * Assert two objects are not equal
     *
     * @param expected expected value. If not provided the expected value is taken from the expected value Queue.
     * @param value real value
     * @param message description message
     */
    public void assertNotSame(String message, Object expected, Object value)
    {
        junit.framework.Assert.assertNotSame(wrapMessage(message), expected, value);
    }

    /**
     * <p>Check if an expression is false.</p>
     *
     * @param condition Boolean expression
     * @param message Description message to be shown in case of failure.
     */
    public void assertFalse(String message, Boolean condition)
    {
        junit.framework.Assert.assertFalse(wrapMessage(message), condition);
    }

    /**
     * <p>Assert for a Not Null payload. </p>
     *
     * @param payload payload
     * @param message Description message to be shown in case of failure.
     */
    public void assertNotNull(String message, Object payload)
    {
        junit.framework.Assert.assertFalse(wrapMessage(message), payload instanceof NullPayload);
    }

    /**
     * <p>Assert Null Payload. </p>
     *
     * @param payload payload
     * @param message Description message to be shown in case of failure.
     */
    public void assertNull(String message, Object payload)
    {
        junit.framework.Assert.assertTrue(wrapMessage(message), payload instanceof NullPayload);
    }



    /**
     * <p>Fail assertion.</p>
     *
     * @param message  Description message to be shown in case of failure.
     */
    public void fail(String message)
    {
        junit.framework.Assert.fail(wrapMessage(message));
    }


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

            BeanDefinition endpointFactory = beanFactory.getBeanDefinition(MuleProperties.OBJECT_MULE_ENDPOINT_FACTORY);
//            endpointFactory.setBeanClassName(MockEndpointFactory.class.getCanonicalName());

        }
    }

    public boolean isMockInbounds() {
        return mockInbounds;
    }

    public void setMockInbounds(boolean mockInbounds) {
        this.mockInbounds = mockInbounds;
    }

    public List<String> getMockingExcludedFlows() {
        return mockingExcludedFlows;
    }

    public void setMockingExcludedFlows(List<String> mockingExcludedFlows) {
        this.mockingExcludedFlows = mockingExcludedFlows;
    }
}
