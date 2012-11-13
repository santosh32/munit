package org.mule.munit.common.mp;

import org.junit.Before;
import org.junit.Test;
import org.mule.api.MuleMessage;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MockedMessageProcessorBehaviorTest {
    public static final MessageProcessorCall MESSAGE_PROCESSOR_CALL = new MessageProcessorCall(new MessageProcessorId("test", "any"));
    private MuleMessage muleMessage;

    @Before
    public void createMock(){
        muleMessage = mock(MuleMessage.class);
    }

    @Test
    public void gettersMustReturnTheOriginalValue(){
        MockedMessageProcessorBehavior behavior = new MockedMessageProcessorBehavior(MESSAGE_PROCESSOR_CALL, muleMessage);

        assertEquals(MESSAGE_PROCESSOR_CALL, behavior.getMessageProcessorCall());
        assertEquals(muleMessage, behavior.getReturnMuleMessage());
    }

}
