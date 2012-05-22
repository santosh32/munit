package org.mule.munit;

import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mule.api.ConnectionManager;
import org.mule.api.MuleContext;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.Module;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.param.Optional;
import org.mule.api.context.MuleContextAware;
import org.mule.api.registry.MuleRegistry;
import org.mule.api.registry.RegistrationException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

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
    
{
    /**
     * <p>Component that we want to mock.</p>
     */
    @Configurable
    private String of;

    private MuleContext muleContext;


    private Object mock;
    private ConnectionManager connectionManagerMock;
    private Class<? extends Object> connectionKeyClass;


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
     * <p>Set The name of the bean to be mocked.</p>
     *
     * @param of The name of the bean to be mocked
     */
    public void setOf(String of)
    {
        this.of = of;
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
     * @param when Message processor name.
     * @param mustReturn Expected return value.
     * @param parameters Message processor parameters.
     */
    @Processor
    public void expect(String when, @Optional List<Object> parameters, final Object mustReturn)
    {
        try {

            Method method = getMockedMethod(when);

            if ( method != null  ){
                Object[] expectedParams = parameters != null ? parameters.toArray() : getAnyParametersOf(method);
                when(method.invoke(mock, expectedParams)).thenAnswer(new Answer<Object>() {
                    @Override
                    public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                        return mustReturn;
                    }
                });
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

            Method method = getMockedMethod(when);

            if ( method != null  ){
                when(method.invoke(mock, getAnyParametersOf(method)))
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
    public void verifyCall(String messageProcessor,  List<Object> parameters, @Optional Integer times, 
                           @Optional Integer atLeast, @Optional Integer atMost )
    {
        try {

            Method method = getMockedMethod(messageProcessor);

            if ( method != null  ){
                if ( times != null  )
                    verify(mock, times(times));
                else if ( atLeast != null )
                    verify(mock, atLeast(atLeast));
                else if ( atMost != null )
                    verify(mock, atMost(atMost));
                else
                    verify(mock);

                method.invoke(mock, buildEquals(parameters));
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

    private Method getMockedMethod(String messageProcessor) throws Exception {
        Method[] declaredMethods = mock.getClass().getDeclaredMethods();
        for ( Method method : declaredMethods )
        {
            if (method.getName().equalsIgnoreCase(removeSlashes(messageProcessor)) )
            {
                return method;
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


    private Object[] getAnyParametersOf(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        int parameterNumber = parameterTypes.length;
        Object[] parameters = new Object[parameterNumber];
        for ( int i=0; i<parameterNumber; i++)
        {
            if ( parameterTypes[i].isPrimitive() )
            {
                if (parameterTypes[i].getName().equals("int") )
                    parameters[i] = anyInt();
                if (parameterTypes[i].getName().equals("short") )
                    parameters[i] = anyShort();
                if (parameterTypes[i].getName().equals("boolean") )
                    parameters[i] = anyBoolean();
                if (parameterTypes[i].getName().equals("byte") )
                    parameters[i] = anyByte();
                if (parameterTypes[i].getName().equals("long") )
                    parameters[i] = anyLong();
                if (parameterTypes[i].getName().equals("float") )
                    parameters[i] = anyFloat();
                if (parameterTypes[i].getName().equals("double") )
                    parameters[i] = anyDouble();
                if (parameterTypes[i].getName().equals("char") )
                    parameters[i] = anyChar();
            }
            else{
                parameters[i] = any(parameterTypes[i]);
            }
        }
        return parameters;
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

    private Object[] buildEquals(List<Object> parameters) {
        Object[] matchers = new Object[parameters.size()];
        int i=0;

        for ( Object parameter : parameters )
        {
            matchers[i] = eq(parameter);
            i++       ;
        }
        return matchers;
    }
}
