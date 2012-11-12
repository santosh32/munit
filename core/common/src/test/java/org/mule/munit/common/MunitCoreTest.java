package org.mule.munit.common;

import org.junit.Before;
import org.junit.Test;
import org.mule.api.MuleContext;
import org.mule.api.config.MuleProperties;
import org.mule.api.registry.MuleRegistry;
import org.mule.api.registry.RegistrationException;
import org.mule.munit.common.endpoint.MockEndpointManager;
import org.mule.munit.common.mp.MockedMessageProcessorManager;

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

    @Before
    public void setUp(){
        muleContext = mock(MuleContext.class);
        muleRegistry = mock(MuleRegistry.class);
        manager = mock(MockedMessageProcessorManager.class);
        endpointManager = mock(MockEndpointManager.class);

        when(muleContext.getRegistry()).thenReturn(muleRegistry);
        when(muleRegistry.lookupObject(MockedMessageProcessorManager.ID)).thenReturn(manager);
        when(muleRegistry.lookupObject(MuleProperties.OBJECT_MULE_ENDPOINT_FACTORY)).thenReturn(endpointManager);

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


}
