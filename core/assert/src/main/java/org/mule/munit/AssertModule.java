package org.mule.munit;


import org.mule.api.annotations.Processor;
import org.mule.api.annotations.param.Optional;
import org.mule.api.annotations.param.Payload;
import org.mule.construct.Flow;
import org.mule.transport.NullPayload;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static junit.framework.Assert.assertEquals;

/**
 * <p>Module for asserting in Munit tests.</p>
 *
 * @author Federico, Fernando
 */
public class AssertModule  implements BeanFactoryPostProcessor
{
    private boolean mockInbounds;
    private List<String> mockingExcludedFlows;

    private Queue<Object> expectedPayload = new LinkedList<Object>();

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
    @Processor
    public void assertThat(@Optional String message, Object payloadIs, @Payload Object payload)
    {
        assertEquals(wrapMessage(message), payloadIs, payload);
    }

    /**
     * <p>Assert for a true expression.</p>
     *
     * {@sample.xml ../../../doc/Assert-connector.xml.sample assert:assertTrue}
     *
     * @param condition Boolean expression
     * @param message Description message to be shown in case of failure.
     */
    @Processor
    public void assertTrue(@Optional String message, Boolean condition)
    {
        junit.framework.Assert.assertTrue(wrapMessage(message), condition);
    }


    /**
     * <p>Check that two objects are equal.</p>
     *
     * {@sample.xml ../../../doc/Assert-connector.xml.sample assert:assertOnEquals}
     *
     * @param expected Expected value.  If not provided the expected value is taken from the expected value Queue.
     * @param value Real value
     * @param message Description message to be shown in case of failure.
     */
    @Processor
    public void assertOnEquals(@Optional String message, @Optional Object expected, Object value)
    {
        if ( expected == null )
        {
            expected = expectedPayload.poll();
        }
        
        assertEquals(wrapMessage(message), expected, value);
    }

    

    /**
     * Assert two objects are not equal
     *
     * {@sample.xml ../../../doc/Assert-connector.xml.sample assert:assertNotSame}
     *
     * @param expected expected value. If not provided the expected value is taken from the expected value Queue.
     * @param value real value
     * @param message description message
     */
    @Processor
    public void assertNotSame(@Optional String message, @Optional Object expected, Object value)
    {
        if ( expected == null )
        {
            expected = expectedPayload.peek();
        }
        
        junit.framework.Assert.assertNotSame(wrapMessage(message), expected, value);
    }

    /**
     * <p>Check if an expression is false.</p>
     *
     * {@sample.xml ../../../doc/Assert-connector.xml.sample assert:assertFalse}
     *
     * @param condition Boolean expression
     * @param message Description message to be shown in case of failure.
     */
    @Processor
    public void assertFalse(@Optional String message, Boolean condition)
    {
        junit.framework.Assert.assertFalse(wrapMessage(message), condition);
    }

    /**
     * <p>Assert for a Not Null payload. </p>
     *
     * {@sample.xml ../../../doc/Assert-connector.xml.sample assert:assertNotNull}
     *
     * @param payload payload
     * @param message Description message to be shown in case of failure.
     */
    @Processor
    public void assertNotNull(@Optional String message, @Payload Object payload)
    {
        junit.framework.Assert.assertFalse(wrapMessage(message), payload instanceof NullPayload);
    }

    /**
     * <p>Assert Null Payload. </p>
     *
     * {@sample.xml ../../../doc/Assert-connector.xml.sample assert:assertNull}
     *
     * @param payload payload
     * @param message Description message to be shown in case of failure.
     */
    @Processor
    public void assertNull(@Optional String message, @Payload Object payload)
    {
        junit.framework.Assert.assertTrue(wrapMessage(message), payload instanceof NullPayload);
    }

    /**
     * <p>Defines the payload for testing.</p>
     *
     * {@sample.xml ../../../doc/Assert-connector.xml.sample assert:set}
     *
     * @param payload payload
     * @return The testing payload
     */
    @Processor
    public Object set(Object payload)
    {
        return payload;
    }

    /**
     * <p>Defines a Null payload for testing.</p>
     *
     * {@sample.xml ../../../doc/Assert-connector.xml.sample assert:setNullPayload}
     * @return Null payload
     */
    @Processor
    public NullPayload setNullPayload()
    {
        return NullPayload.getInstance();
    }


    /**
     * <p>Fail assertion.</p>
     *
     * {@sample.xml ../../../doc/Assert-connector.xml.sample assert:fail}
     *
     * @param message  Description message to be shown in case of failure.
     */
    @Processor
    public void fail(@Optional String message)
    {
        junit.framework.Assert.fail(wrapMessage(message));
    }


    /**
     * <p>Assert module keeps a queue of expected payloads, so you can call assertNotSame and assertOnEquals</p>
     * <p>inside a inbound flow you created for your tests. The expected payload is taken from the peek of the
     * queue</p>
     *
     * {@sample.xml ../../../doc/Assert-connector.xml.sample assert:addExpectedValue}
     *
     * @param value  Description message to be shown in case of failure.
     */

    @Processor
    public void addExpected(Object value)
    {
        expectedPayload.add(value);
    }

    /**
     * <p>Resets the module</p>
     *
     * {@sample.xml ../../../doc/Assert-connector.xml.sample assert:reset}
     */
    @Processor
    public void resetCalls()
    {
        expectedPayload = new LinkedList<Object>();
    }

    /**
     * <p>Checks that all the expected calls to assert has been done</p>
     *
     * {@sample.xml ../../../doc/Assert-connector.xml.sample assert:reset}
     */
    @Processor
    public void validateCalls()
    {
        junit.framework.Assert.assertTrue(expectedPayload.isEmpty());
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

//            BeanDefinition endpointFactory = beanFactory.getBeanDefinition(MuleProperties.OBJECT_MULE_ENDPOINT_FACTORY);
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
