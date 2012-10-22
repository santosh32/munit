package org.mule.munit.common.mocking;

import org.junit.Before;
import org.junit.Test;
import org.mule.api.MuleContext;
import org.mule.api.registry.MuleRegistry;
import org.mule.munit.common.mp.MockedMessageProcessorManager;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MunitToolTest {

    private MuleContext muleContext;
    private MuleRegistry muleRegistry;
    private MockedMessageProcessorManager manager;

    @Before
    public void setUp(){
        muleContext = mock(MuleContext.class);
        muleRegistry = mock(MuleRegistry.class);
        manager = mock(MockedMessageProcessorManager.class);

        when(muleContext.getRegistry()).thenReturn(muleRegistry);
        when(muleRegistry.lookupObject(MockedMessageProcessorManager.ID)).thenReturn(manager);

    }


    @Test
    public void getFullNameWithEmptyNamespace(){
        MunitTool munitTool = new MunitTool(muleContext);
        munitTool.messageProcessorName = "testName";
        
        assertEquals("mule:testName", munitTool.getFullName());
    }

    @Test
    public void getManager(){
        MunitTool munitTool = new MunitTool(muleContext);

        assertEquals(manager, munitTool.getManager());
    }


    @Test(expected = IllegalArgumentException.class)
    public void validNameWithNull(){
        MunitTool munitTool = new MunitTool(muleContext);

        munitTool.checkValidQuery();
    }


    @Test
    public void validName(){
        MunitTool munitTool = new MunitTool(muleContext);
        munitTool.messageProcessorName = "testName";

        munitTool.checkValidQuery();
    }

    @Test
    public void getFullNameWithNamespace(){
        MunitTool munitTool = new MunitTool(muleContext);
        munitTool.messageProcessorName = "testName";
        munitTool.messageProcessorNamespace = "testNamespace";

        assertEquals("testNamespace:testName", munitTool.getFullName());
    }
}
