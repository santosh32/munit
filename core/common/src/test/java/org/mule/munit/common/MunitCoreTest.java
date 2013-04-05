package org.mule.munit.common;

import org.junit.Before;
import org.junit.Test;
import org.mule.api.MuleContext;
import org.mule.api.config.MuleProperties;
import org.mule.api.construct.FlowConstruct;
import org.mule.api.registry.MuleRegistry;
import org.mule.api.registry.RegistrationException;
import org.mule.modules.interceptor.processors.MessageProcessorCall;
import org.mule.modules.interceptor.processors.MessageProcessorId;
import org.mule.munit.common.endpoint.MockEndpointManager;
import org.mule.munit.common.mp.MockedMessageProcessorManager;
import org.mule.munit.common.mp.MunitMessageProcessorCall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MunitCoreTest {

    private MuleContext muleContext;
    private MuleRegistry muleRegistry;
    private MockedMessageProcessorManager manager;
    private MockEndpointManager endpointManager;
    private FlowConstruct flowConstruct = mock(FlowConstruct.class);

    @Before
    public void setUp(){
        muleContext = mock(MuleContext.class);
        muleRegistry = mock(MuleRegistry.class);
        manager = mock(MockedMessageProcessorManager.class);
        endpointManager = mock(MockEndpointManager.class);

        when(muleContext.getRegistry()).thenReturn(muleRegistry);
        when(muleRegistry.lookupObject(MockedMessageProcessorManager.ID)).thenReturn(manager);
        when(muleRegistry.lookupObject(MuleProperties.OBJECT_MULE_ENDPOINT_FACTORY)).thenReturn(endpointManager);
        when(flowConstruct.getName()).thenReturn("flowName");

    }

    @Test
    public void testReset(){
        MunitCore.reset(muleContext);

        verify(endpointManager).resetBehaviors();
        verify(manager).reset();

    }

    @Test
    public void testResetWhenNullManager(){

        when(muleRegistry.lookupObject(MockedMessageProcessorManager.ID)).thenReturn(null);

        MunitCore.reset(muleContext);

        verify(endpointManager).resetBehaviors();
        verify(manager,times(0)).reset();
    }

    @Test
    public void testRegisterManager() throws RegistrationException {

        MunitCore.registerManager(muleContext);

        verify(muleRegistry, times(0)).registerObject(eq(MockedMessageProcessorManager.ID), isA(MockedMessageProcessorManager.class));
    }

    @Test
    public void testRegisterManagerWithNull() throws RegistrationException {

        when(muleRegistry.lookupObject(MockedMessageProcessorManager.ID)).thenReturn(null);

        MunitCore.registerManager(muleContext);

        verify(muleRegistry).registerObject(eq(MockedMessageProcessorManager.ID), isA(MockedMessageProcessorManager.class));
    }

    @Test
    public void testSetContext(){
        MunitCore.setMuleContext(muleContext);

        assertEquals(muleContext, MunitCore.getMuleContext());
    }


    @Test
    public void buildStackTrace(){

        when(manager.getCalls()).thenReturn(executedCalls());
        List<StackTraceElement> stackTraceElements = MunitCore.buildMuleStackTrace(muleContext);

        assertEquals(1, stackTraceElements.size());
        assertEquals("nsp1:mp1{key=value}", stackTraceElements.get(0).getMethodName());
        assertEquals("mule-config.xml", stackTraceElements.get(0).getFileName());
        assertEquals("flowName", stackTraceElements.get(0).getClassName());
        assertEquals(20, stackTraceElements.get(0).getLineNumber());
    }

    private List<MunitMessageProcessorCall> executedCalls() {
        ArrayList<MunitMessageProcessorCall> calls = new ArrayList<MunitMessageProcessorCall>();
        MunitMessageProcessorCall call1 = new MunitMessageProcessorCall(new MessageProcessorId("mp1", "nsp1"));
        call1.setFileName("mule-config.xml");
        call1.setLineNumber("20");
        call1.setFlowConstruct(flowConstruct);

        HashMap<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("key", "value");
        attributes.put("name", "any");
        attributes.put(MunitCore.LINE_NUMBER_ELEMENT_ATTRIBUTE, "anyLocation");
        call1.setAttributes(attributes);
        calls.add(call1);
        return calls;
    }

}
