package org.mule.munit.common.endpoint;


import org.mule.api.MuleContext;
import org.mule.api.MuleMessage;
import org.mule.api.config.MuleProperties;
import org.mule.api.context.MuleContextAware;
import org.mule.api.el.ExpressionLanguageContext;
import org.mule.api.el.ExpressionLanguageExtension;
import org.mule.api.transport.Connector;
import org.mule.construct.Flow;
import org.mule.munit.common.mel.assertions.*;
import org.mule.munit.common.mel.matchers.*;
import org.mule.munit.common.mel.utils.FlowResultFunction;
import org.mule.munit.common.mel.utils.GetResourceFunction;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

import java.util.*;
import java.util.logging.Logger;

import static org.mule.module.hamcrest.message.AttachmentMatcher.hasInboundAttachment;
import static org.mule.module.hamcrest.message.PropertyMatcher.*;
import static org.mule.munit.common.connectors.ConnectorMethodInterceptorFactory.addFactoryDefinitionTo;


/**
 * <p>
 *     This class changes the endpoint factory and inject the mock manager
 * </p>
 *
 * <p>
 *     This is a piece part of the endpoint mocking. By overriding the endpoint factory we can mock all the outbound/inbound
 * endpoints of a mule application
 * </p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MunitSpringFactoryPostProcessor  implements ExpressionLanguageExtension, MuleContextAware{

    private static Logger logger = Logger.getLogger("Bean definition Processor");

    /**
     * <p>
     *     Defines if the inbounds must be mocked or not. This is pure Munit configuration
     * </p>
     */
    protected boolean mockInbounds = true;

    /**
     * <p>
     *     Defines if the app connectors for outbound/inbound endpoints have to be mocked. If they are then all
     * outbound endpoints/inbound endpoints must be mocked.
     * </p>
     */
    protected boolean mockConnectors = true;

    /**
     * <p>
     *     List of flows which we don't want to mock the inbound message sources
     * </p>
     */
    protected List<String> mockingExcludedFlows = new ArrayList<String>();

    /**
     * <p>
     *     Mule context for MEL expressions
     * </p>
     */
    private MuleContext muleContext;


    /**
     * <p>
     *     Implementation of the BeanFactoryPostProcessor. It removes the message sources of all the flows except
     * for the ones specified in mockingExcludedFlows. Only if mockInbounds is true.
     * </p>
     *
     * @param beanFactory
     *          <p>
     *              The spring bean factory
     *          </p>
     * @throws BeansException
     *          <p>
     *              When post processing fails. Never thrown for this implementation
     *          </p>
     */
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        if ( isMockInbounds() || isMockConnectors() ){
            String[] names = beanFactory.getBeanDefinitionNames();
            String[] beanDefinitionNames = names == null ? new String[0] : names;
            for ( String name : beanDefinitionNames ){
                BeanDefinition beanDefinition = beanFactory.getBeanDefinition(name);
                if ( Flow.class.getName().equals(beanDefinition.getBeanClassName()) ){
                    if ( !mockingExcludedFlows.contains(name) ){
                        beanDefinition.getPropertyValues().removePropertyValue("messageSource");
                    }
                }
            }


        }

        changeEndpointFactory(beanFactory);

        mockConnectors(beanFactory);

    }

    /**
     * <p>
     *     Changes the default EndpointFactory of mule with a Wrapper of it. This wrapper creates mocks of the Outbound
     * Endpoints
     * </p>
     *
     * @param beanFactory
     *           <p>
     *               The spring bean factory
     *           </p>
     */
    private void changeEndpointFactory(ConfigurableListableBeanFactory beanFactory) {
        GenericBeanDefinition endpointFactory = (GenericBeanDefinition) beanFactory.getBeanDefinition(MuleProperties.OBJECT_MULE_ENDPOINT_FACTORY);

        AbstractBeanDefinition abstractBeanDefinition = endpointFactory.cloneBeanDefinition();

        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.add("defaultFactory", abstractBeanDefinition);
        endpointFactory.setPropertyValues(propertyValues);
        endpointFactory.setBeanClassName(MockEndpointManager.class.getCanonicalName());
    }

    /**
     * <p>
     *     Changes the {@link Connector} bean definition so they are created as mocks of connectors that do not connect
     * </p>
     *
     * <p>
     *     This action is done only if {@link #isMockConnectors()} is true
     * </p>

     * @param beanFactory
     *          <p>
     *              The bean factory that contains the bean definition
     *          </p>
     */
    private void mockConnectors(ConfigurableListableBeanFactory beanFactory) {
        if (isMockConnectors()) {
            String[] beanNamesForType = beanFactory.getBeanNamesForType(Connector.class);
            if (beanNamesForType != null) {
                for (String beanName : beanNamesForType) {
                    RootBeanDefinition beanDefinition = RootBeanDefinition.class.cast(beanFactory.getBeanDefinition(beanName));

                    if (beanDefinition.getFactoryMethodName() == null) {
                        addFactoryDefinitionTo(beanDefinition)
                                .withConstructorArguments(beanDefinition.getBeanClass());
                    } else {
                        logger.info("The connector " + beanName + " cannot be mocked as it already has a factory method");
                    }
                }
            }

        }
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

    public boolean isMockConnectors() {
        return mockConnectors;
    }

    public void setMockConnectors(boolean mockConnectors) {
        this.mockConnectors = mockConnectors;
    }

    @Override
    public void configureContext(ExpressionLanguageContext context) {
        context.declareFunction("messageHasPropertyInAnyScopeCalled",  new MessageHasElementAssertionMelFunction(new MessageHasElementAssertionCommand(){
            @Override
            public boolean messageHas(String param, MuleMessage muleMessage) {
                return hasPropertyInAnyScope(param).matches(muleMessage);
            }
        }));

        context.declareFunction("messageHasInboundPropertyCalled",  new MessageHasElementAssertionMelFunction(new MessageHasElementAssertionCommand(){
            @Override
            public boolean messageHas(String param, MuleMessage muleMessage) {
                return hasInboundProperty(param).matches(muleMessage);
            }
        }));

        context.declareFunction("messageHasOutboundPropertyCalled",  new MessageHasElementAssertionMelFunction(new MessageHasElementAssertionCommand(){
            @Override
            public boolean messageHas(String param, MuleMessage muleMessage) {
                return hasOutboundProperty(param).matches(muleMessage);
            }
        }));

        context.declareFunction("messageHasSessionPropertyCalled",  new MessageHasElementAssertionMelFunction(new MessageHasElementAssertionCommand(){
            @Override
            public boolean messageHas(String param, MuleMessage muleMessage) {
                return hasSessionProperty(param).matches(muleMessage);
            }
        }));

        context.declareFunction("messageHasInvocationPropertyCalled",  new MessageHasElementAssertionMelFunction(new MessageHasElementAssertionCommand(){
            @Override
            public boolean messageHas(String param, MuleMessage muleMessage) {
                return hasInvocationProperty(param).matches(muleMessage);
            }
        }));

        context.declareFunction("messageHasInboundAttachmentCalled",  new MessageHasElementAssertionMelFunction(new MessageHasElementAssertionCommand(){
            @Override
            public boolean messageHas(String param, MuleMessage muleMessage) {
                return hasInboundAttachment(param).matches(muleMessage);
            }
        }));

        context.declareFunction("messageHasOutboundAttachmentCalled",  new MessageHasElementAssertionMelFunction(new MessageHasElementAssertionCommand(){
            @Override
            public boolean messageHas(String param, MuleMessage muleMessage) {
                return hasInboundAttachment(param).matches(muleMessage);
            }
        }));

        context.declareFunction("messageInboundProperty",  new MassageMatchingAssertionMelFunction(new ElementMatcherBuilder(){
            @Override
            public ElementMatcher build(String elementName,  MuleMessage muleMessage) {
                return new ElementMatcher(muleMessage.getInboundProperty(elementName));
            }
        }));

        context.declareFunction("messageOutboundProperty",  new MassageMatchingAssertionMelFunction(new ElementMatcherBuilder(){
            @Override
            public ElementMatcher build(String elementName, MuleMessage muleMessage) {
                return new ElementMatcher(muleMessage.getOutboundProperty(elementName));
            }
        }));

        context.declareFunction("messageInboundProperty",  new MassageMatchingAssertionMelFunction(new ElementMatcherBuilder(){
            @Override
            public ElementMatcher build(String elementName,  MuleMessage muleMessage) {
                return new ElementMatcher(muleMessage.getInboundProperty(elementName));
            }
        }));

        context.declareFunction("messageInvocationProperty",  new MassageMatchingAssertionMelFunction(new ElementMatcherBuilder(){
            @Override
            public ElementMatcher build(String elementName,  MuleMessage muleMessage) {
                return new ElementMatcher(muleMessage.getInvocationProperty(elementName));
            }
        }));

        context.declareFunction("messageInboundAttachment",  new MassageMatchingAssertionMelFunction(new ElementMatcherBuilder(){
            @Override
            public ElementMatcher build(String elementName,  MuleMessage muleMessage) {
                return new ElementMatcher(muleMessage.getInboundAttachment(elementName));
            }
        }));

        context.declareFunction("messageOutboundAttachment",  new MassageMatchingAssertionMelFunction(new ElementMatcherBuilder(){
            @Override
            public ElementMatcher build(String elementName,  MuleMessage muleMessage) {
                return new ElementMatcher(muleMessage.getOutboundAttachment(elementName));
            }
        }));

        context.declareFunction("eq", new EqMatcherFunction());
        context.declareFunction("anyBoolean", new AnyMatcherFunction(Boolean.class));
        context.declareFunction("anyByte", new AnyMatcherFunction(Byte.class));
        context.declareFunction("anyInt", new AnyMatcherFunction(Integer.class));
        context.declareFunction("anyDouble", new AnyMatcherFunction(Double.class));
        context.declareFunction("anyFloat", new AnyMatcherFunction(Float.class));
        context.declareFunction("anyShort", new AnyMatcherFunction(Short.class));
        context.declareFunction("anyObject", new AnyMatcherFunction(Object.class));
        context.declareFunction("anyString", new AnyMatcherFunction(String.class));
        context.declareFunction("anyList", new AnyMatcherFunction(List.class));
        context.declareFunction("anySet", new AnyMatcherFunction(Set.class));
        context.declareFunction("anyMap", new AnyMatcherFunction(Map.class));
        context.declareFunction("anyCollection", new AnyMatcherFunction(Collection.class));
        context.declareFunction("isNull", new NullMatcherFunction());
        context.declareFunction("isNotNull", new NotNullMatcherFunction());
        context.declareFunction("any", new AnyClassMatcherFunction());
        context.declareFunction("resultOfScript", new FlowResultFunction(muleContext));
        context.declareFunction("getResource", new GetResourceFunction());

    }

    @Override
    public void setMuleContext(MuleContext context) {
        this.muleContext = context;
    }
}
