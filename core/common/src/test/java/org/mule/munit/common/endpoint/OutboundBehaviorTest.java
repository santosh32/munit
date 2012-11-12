package org.mule.munit.common.endpoint;

import org.junit.Before;
import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.api.processor.MessageProcessor;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class OutboundBehaviorTest {

    public static final ArrayList<MessageProcessor> ASSERTIONS = new ArrayList<MessageProcessor>();
    private MuleMessage message;

    @Before
    public void setUp(){
        message = mock(MuleMessage.class);
    }

    @Test
    public void testGetters(){
        OutboundBehavior behavior = new OutboundBehavior(message, ASSERTIONS);


        assertEquals(ASSERTIONS, behavior.getAssertions());
        assertEquals(message, behavior.getMessage());
    }
}
