package org.mule.munit.common.mocking;

import junit.framework.AssertionFailedError;
import org.junit.Before;
import org.junit.Test;
import org.mule.api.MuleContext;
import org.mule.api.registry.MuleRegistry;
import org.mule.munit.common.mp.MessageProcessorCall;
import org.mule.munit.common.mp.MessageProcessorId;
import org.mule.munit.common.mp.MockedMessageProcessorManager;

import java.util.ArrayList;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MunitVerifierTest {

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
    @Test(expected = AssertionFailedError.class)
    public void withNoCallFailAtLeast(){

        when(manager.findCallsFor(any(MessageProcessorId.class), any(Map.class)))
                .thenReturn(new ArrayList<MessageProcessorCall>());

        new MunitVerifier(muleContext).verifyCallOfMessageProcessor("testName")
                .ofNamespace("testNamespace")
                .atLeast(1);
        
    }

    @Test(expected = AssertionFailedError.class)
    public void withNoCallFailAtLeastOne(){

        when(manager.findCallsFor(any(MessageProcessorId.class), any(Map.class)))
                .thenReturn(new ArrayList<MessageProcessorCall>());

        new MunitVerifier(muleContext).verifyCallOfMessageProcessor("testName")
                .ofNamespace("testNamespace")
                .atLeastOnce();

    }

    @Test(expected = AssertionFailedError.class)
    public void withNoCallFailTimes(){

        when(manager.findCallsFor(any(MessageProcessorId.class), any(Map.class)))
                .thenReturn(new ArrayList<MessageProcessorCall>());

        new MunitVerifier(muleContext).verifyCallOfMessageProcessor("testName")
                .ofNamespace("testNamespace")
                .times(2);

    }

    @Test(expected = AssertionFailedError.class)
    public void withCallsFailAtMost(){

        when(manager.findCallsFor(any(MessageProcessorId.class), any(Map.class)))
                .thenReturn(createCalls());

        new MunitVerifier(muleContext).verifyCallOfMessageProcessor("testName")
                .ofNamespace("testNamespace")
                .times(2);

    }


    @Test
    public void withCallsOkTimes(){

        when(manager.findCallsFor(any(MessageProcessorId.class), any(Map.class)))
                .thenReturn(createCalls());

        new MunitVerifier(muleContext).verifyCallOfMessageProcessor("testName")
                .ofNamespace("testNamespace")
                .times(3);

    }

    @Test
    public void withCallsOkAtLeastOnce(){

        when(manager.findCallsFor(any(MessageProcessorId.class), any(Map.class)))
                .thenReturn(createCalls());

        new MunitVerifier(muleContext).verifyCallOfMessageProcessor("testName")
                .ofNamespace("testNamespace")
                .atLeastOnce();

    }


    @Test
    public void withCallsOkAtLeast(){

        when(manager.findCallsFor(any(MessageProcessorId.class), any(Map.class)))
                .thenReturn(createCalls());

        new MunitVerifier(muleContext).verifyCallOfMessageProcessor("testName")
                .ofNamespace("testNamespace")
                .atLeast(1);

    }

    @Test
    public void withCallsOkAtMost(){

        when(manager.findCallsFor(any(MessageProcessorId.class), any(Map.class)))
                .thenReturn(createCalls());

        new MunitVerifier(muleContext).verifyCallOfMessageProcessor("testName")
                .ofNamespace("testNamespace")
                .atMost(4);

    }

    private ArrayList<MessageProcessorCall> createCalls() {
        ArrayList<MessageProcessorCall> calls = new ArrayList<MessageProcessorCall>();
        calls.add(new MessageProcessorCall(null));
        calls.add(new MessageProcessorCall(null));
        calls.add(new MessageProcessorCall(null));
        return calls;
    }
}
