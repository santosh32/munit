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
import org.mule.api.context.MuleContextAware;
import org.mule.api.registry.MuleRegistry;
import org.mule.api.registry.RegistrationException;

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
 * Module for Mocking
 *
 * @author Federico, Fernando
 */
@Module(name="mock", schemaVersion="1.0")
public class MockModule  implements MuleContextAware
{
    /**
     * Bean that we want to mock.
     */
    @Configurable
    private String of;

    private MuleContext muleContext;


    private Object mock;
    private ConnectionManager connectionManagerMock;
    private Class<? extends Object> connectionKeyClass;


    public static String getStackTrace(Throwable throwable) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        throwable.printStackTrace(printWriter);
        return writer.toString();
    }

    /**
     * Set The name of the bean to be mocked
     *
     * @param of The name of the bean to be mocked
     */
    public void setOf(String of)
    {
        this.of = of;
    }


    /**
     * Expected behaviour of the mock.
     *
     * {@sample.xml ../../../doc/mock-connector.xml.sample mock:expect}
     *
     * @param when Message processor id
     * @param mustReturn Expected return value
     */
    @Processor
    public void expect(String when, final Object mustReturn)
    {
        try {

            Method method = getMockedMethod(when);

            if ( method != null  ){
                when(method.invoke(mock,getAnyParametersOf(method))).thenAnswer(new Answer<Object>() {
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
     * Expect to fail when message processor is called
     *
     * {@sample.xml ../../../doc/mock-connector.xml.sample mock:expectFail}
     *
     * @param exceptionType Java Exception full name
     * @param when Message processor Id
     *
     */
    @Processor
    public void expectFail(String when, String exceptionType)
    {
        try {

            Method method = getMockedMethod(when);

            if ( method != null  ){
                when(method.invoke(mock, getAnyParametersOf(method)))
                        .thenThrow((Throwable) Class.forName(exceptionType).newInstance());
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
     */
    @Processor
    public void verifyCall(String messageProcessor,  List<Object> parameters )
    {
        try {

            Method method = getMockedMethod(messageProcessor);

            if ( method != null  ){
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
            if ( method.getName().equals(messageProcessor) )
            {
                return method;
            }
        }

        throw new Exception("Method that want to be mocked does not exist");
    }

    @Override
    public void setMuleContext(MuleContext muleContext) {
        this.muleContext = muleContext;

        MuleRegistry registry = muleContext.getRegistry();

        registerMock( buildMock(registry), registry);
    }


    private Object[] getAnyParametersOf(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        int parameterNumber = parameterTypes.length;
        Object[] parameters = new Object[parameterNumber];
        for ( int i=0; i<parameterNumber; i++)
        {
            parameters[i] = any(parameterTypes[i]);
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
