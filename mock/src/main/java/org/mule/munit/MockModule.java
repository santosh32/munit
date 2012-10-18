package org.mule.munit;

import org.apache.commons.lang.StringUtils;
import org.mockito.Mockito;
import org.mockito.asm.ClassReader;
import org.mockito.asm.tree.ClassNode;
import org.mockito.asm.tree.LocalVariableNode;
import org.mockito.asm.tree.MethodNode;
import org.mockito.internal.stubbing.answers.Returns;
import org.mule.MessageExchangePattern;
import org.mule.api.ConnectionManager;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.NestedProcessor;
import org.mule.api.annotations.Module;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.param.Optional;
import org.mule.api.config.MuleProperties;
import org.mule.api.context.MuleContextAware;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.registry.MuleRegistry;
import org.mule.api.registry.RegistrationException;
import org.mule.construct.Flow;
import org.mule.munit.endpoint.MockEndpointManager;
import org.mule.munit.endpoint.OutboundBehavior;
import org.mule.munit.mp.MockMpManager;
import org.mule.munit.mp.MpBehavior;
import org.mule.tck.MuleTestUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


/**
 * <p>Module for Mocking devkit Modules.</p>
 * 
 * <p>With this module you can mock using mockito framework all the modules that are written with Mule Devkit.</p>
 * 
 * <p> In order to be able to mock a module it has to be declare with a reference name. For example:</p>
 * 
 * <p> <sfdc:config name="Salesforce" username="" password="" securityToken="" /></p>
 * 
 * <p> Otherwise the module/connector will not be accessible by a reference name and will not be able to be mocked. </p>
 *
 * @author Federico, Fernando
 */
@Module(name="mock", schemaVersion="1.0")
public class MockModule  implements MuleContextAware, BeanFactoryPostProcessor
    
    private MuleContext muleContext;

    private static String getStackTrace(Throwable throwable) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        throwable.printStackTrace(printWriter);
        return writer.toString();
    }
    
    private static String removeSlashes(String messageProcessor)
    {
        return messageProcessor.replaceAll("-", "");
    }


    /**
     * <p>Define what the mock must return on a message processor call.</p>
     * 
     * <p>If the message processor doesn't return any value then there is no need to define an expect.</p>
     *
     * <p>You can define the message processor parameters in the same order they appear in the API documentation. In
     * order to define the behaviour on that particular case.</p>
     *
     * {@sample.xml ../../../doc/mock-connector.xml.sample mock:expect}
     *
     * @param thatMessageProcessor Message processor name.
     * @param toReturn Expected return value.
     * @param toReturnResponseFrom The flow name that creates the expected result
     * @param parameters Message processor parameters.
     */
    @Processor
    public void expect(String thatMessageProcessor,
                       @Optional Map<String,Object> parameters,
                       @Optional final Object toReturn,
                       @Optional String toReturnResponseFrom)
    {
        try {
            final Object expectedObject = toReturn != null ? toReturn : getResultOf(toReturnResponseFrom);
            MockMpManager manager = (MockMpManager) muleContext.getRegistry().lookupObject(MockMpManager.ID);
            manager.addBehavior(new MpBehavior(getName(thatMessageProcessor),getNamespace(thatMessageProcessor), parameters, expectedObject ));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

        private String getNamespace(String when) {
            String[] split = when.split(":");
            if ( split.length > 1 ) {
                return split[0];
            }

            return "mule";
        }

        private String getName(String when) {
            String[] split = when.split(":");
            if ( split.length > 1 ) {
                return split[1];
            }

            return split[0];
        }


    /**
     * <p>Expect to throw an exception when message processor is called. </p>
     *
     * {@sample.xml ../../../doc/mock-connector.xml.sample mock:expectFail}
     *
     * @param throwA Java Exception full qualified name.
     * @param when Message processor name.
     *
     */
    @Processor
    public void expectFail(String when, String throwA)
    {
        try {

            MockedMethod mockedMethod = getMockedMethod(when);
            Method method = mockedMethod.getMethod();

            if ( method != null  ){
                when(method.invoke(mock, mockedMethod.getAnyParameters(new HashMap<Integer, Object>())))
                        .thenThrow((Throwable) Class.forName(throwA).newInstance());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Check that the message processor was called with some specified parameters
     *
     * {@sample.xml ../../../doc/mock-connector.xml.sample mock:verifyCall}
     *
     * @param messageProcessor Message processor Id
     * @param parameters Message processor parameters.
     * @param times Number of times the message processor has to be called
     * @param atLeast Number of time the message processor has to be called at least.
     * @param atMost Number of times the message processor has to be called at most.
     */
    @Processor
    public void verifyCall(String messageProcessor,  Map<String,Object> parameters, @Optional Integer times,
                           @Optional Integer atLeast, @Optional Integer atMost )
    {
        try {

            MockedMethod mockedMethod = getMockedMethod(messageProcessor);
            Method method = mockedMethod.getMethod();

            if ( method != null  ){
                if ( times != null  )
                    verify(mock, times(times));
                else if ( atLeast != null )
                    verify(mock, atLeast(atLeast));
                else if ( atMost != null )
                    verify(mock, atMost(atMost));
                else
                    verify(mock);


                Map<Integer, Object> parameterIndex = mockedMethod.getParameters(parameters);

                method.invoke(mock, mockedMethod.getAnyParameters(parameterIndex));
            }

        } catch (InvocationTargetException e) {
            fail("Verification Error:" + getStackTrace(e));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Throw an Exception when a connector tries to connect.
     *
     * {@sample.xml ../../../doc/mock-connector.xml.sample mock:failOnConnect}
     *
     */
    @Processor
    public void failOnConnect()
    {
        try {
            doThrow(new Exception()).when(connectionManagerMock).acquireConnection(any());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reset mock behaviour
     *
     * {@sample.xml ../../../doc/mock-connector.xml.sample mock:reset}
     *
     */
    @Processor
    public void reset()
    {
        if (connectionManagerMock != null && connectionKeyClass !=null){
            setConnectionManagerDefaultBehaviour();
        }

        Mockito.reset(mock);
    }
    
    private boolean matchFriendlyName(Method method, String messageProcessor){
        Processor annotation = method.getAnnotation(Processor.class);
        return (annotation != null && !StringUtils.isEmpty(annotation.friendlyName()) && annotation.friendlyName().equalsIgnoreCase(messageProcessor));
        
    }

    private MockedMethod getMockedMethod(String messageProcessor) throws Exception {
        Method[] declaredMethods = mockedClass.getMethods();
        for ( Method method : declaredMethods )
        {
                if (method.getName().equalsIgnoreCase(removeSlashes(messageProcessor)) || matchFriendlyName(method, messageProcessor) )
                {
                    ClassNode classNode = null;
                    try {
                        classNode = new ClassNode();
                        Class<?> declaringClass = method.getDeclaringClass();
                        ClassReader classReader = new ClassReader(declaringClass.getClassLoader().getResourceAsStream(org.mockito.asm.Type.getInternalName(declaringClass) + ".class"));
                        classReader.accept(classNode, 0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                    }

                    List<MethodNode> methods = classNode.methods;
                    List<LocalVariableNode> localVariableNodes = null;
                    for ( MethodNode methodNode : methods ){
                        if ( methodNode.name.equals(method.getName())){
                            localVariableNodes = methodNode.localVariables;
                            break;
                        }
                    }
                    return new MockedMethod(method, localVariableNodes);
                }

        }

        throw new Exception("Method that want to be mocked does not exist");
    }

    @Override
    public void setMuleContext(MuleContext muleContext)  {
        this.muleContext = muleContext;
    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        MuleRegistry registry = muleContext.getRegistry();
        registerMock( buildMock(registry), registry);
    }


   


    private Object buildMock(MuleRegistry registry) {
        Object registerObject = registry.get(of);

        if ( registerObject instanceof ConnectionManager)
        {
            return createConnectionManagerMock(registry);
        }
        else
        {
            mock = mock(registerObject.getClass());
            return mock;
        }
    }

    private void registerMock(Object mocked, MuleRegistry registry) {
        try {
            registry.unregisterObject(of);
            registry.registerObject(of,mocked);
        } catch (RegistrationException e) {
            throw new RuntimeException(e);
        }
    }

    private Object createConnectionManagerMock(MuleRegistry registry) {

        Class<? extends Object> connectionManagerClass = registry.get(of).getClass();
        Type[] genericInterfaces = connectionManagerClass.getGenericInterfaces();

        for ( Object inteface : genericInterfaces )
        {
            if(inteface instanceof ParameterizedType){
                ParameterizedType type = (ParameterizedType) inteface;
                Type[] typeArguments = type.getActualTypeArguments();

                buildConnectionMocks(connectionManagerClass, typeArguments);

                return connectionManagerMock;
            }
        }

        throw new RuntimeException("Could not create mock for the connector");
    }

    private void buildConnectionMocks(Class<? extends Object> connectionManagerClass, Type[] typeArguments) {
        mockedClass = (Class<Object>) typeArguments[1];
        mock = mock((Class<Object>) typeArguments[1]);
        connectionKeyClass =(Class<Object>) typeArguments[0];
        connectionManagerMock = (ConnectionManager) mock(connectionManagerClass, new Returns(""));





        setConnectionManagerDefaultBehaviour();
    }

    private void setConnectionManagerDefaultBehaviour() {
        try {
            doReturn(mock).when(connectionManagerMock).acquireConnection(any(connectionKeyClass));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

        public void outboundEndpoint(String address, Object returnPayload,
                                     Map<String, Object> returnInvocationProperties,
                                     Map<String, Object> returnInboundProperties,
                                     Map<String, Object> returnSessionProperties,
                                     Map<String, Object> returnOutboundProperties,
                                     List<NestedProcessor> assertions)
        {

            MockEndpointManager factory = (MockEndpointManager) muleContext.getRegistry().lookupObject(MuleProperties.OBJECT_MULE_ENDPOINT_FACTORY);

            OutboundBehavior behavior = new OutboundBehavior(returnPayload, createMessageProcessorsFrom(assertions));

            behavior.setInboundProperties(returnInboundProperties);
            behavior.setInvocationProperties(returnInvocationProperties);
            behavior.setOutboundProperties(returnOutboundProperties);
            behavior.setSessionProperties(returnSessionProperties);

            factory.addExpect(address, behavior);
        }

        private List<MessageProcessor> createMessageProcessorsFrom(List<NestedProcessor> assertions) {
            if  ( assertions == null ){
                return null;
            }


            List<MessageProcessor> mps = new ArrayList<MessageProcessor>();
            for ( NestedProcessor nestedProcessor : assertions ){
                mps.add(new NestedMessageProcessor(nestedProcessor));
            }

            return mps;
        }


    private Object getResultOf(String mustReturnResponseFrom) {

        try {
            Flow flow = (Flow) muleContext.getRegistry().lookupFlowConstruct(mustReturnResponseFrom);
            if ( flow == null ){
                throw new RuntimeException("Flow " + mustReturnResponseFrom + " does not exist");
            }

            MuleEvent process = flow.process(testEvent());
            return process.getMessage().getPayload();
        } catch (Exception e) {
            throw new RuntimeException("Could not call flow " + mustReturnResponseFrom + " to get the expected result");
        }
    }

    private MuleEvent testEvent() throws Exception {
        return MuleTestUtils.getTestEvent(null, MessageExchangePattern.REQUEST_RESPONSE, muleContext);
    }

    public String getOf() {
        return of;
    }
}


