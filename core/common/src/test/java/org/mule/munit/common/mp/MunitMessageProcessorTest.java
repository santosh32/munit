package org.mule.munit.common.mp;


import org.junit.Test;
import org.mule.api.MuleException;

import static org.junit.Assert.assertTrue;

public class MunitMessageProcessorTest {

    @Test
    public void testDispose(){
        MockedMessageProcessor realMp = new MockedMessageProcessor();
         ;

        munitMessageProcessor(realMp).dispose();
        assertTrue(realMp.calledDispose);
    }

    @Test
    public void testStop() throws MuleException {
        MockedMessageProcessor realMp = new MockedMessageProcessor();
        munitMessageProcessor(realMp).stop();
        assertTrue(realMp.calledStop);
    }

    @Test
    public void testStart() throws MuleException {
        MockedMessageProcessor realMp = new MockedMessageProcessor();
        munitMessageProcessor(realMp).start();
        assertTrue(realMp.calledStart);
    }

    @Test
    public void testInitialise() throws MuleException {
        MockedMessageProcessor realMp = new MockedMessageProcessor();
        munitMessageProcessor(realMp).initialise();
        assertTrue(realMp.calledInitialise);
    }

    @Test
    public void testSetMuleContext() throws MuleException {
        MockedMessageProcessor realMp = new MockedMessageProcessor();
        munitMessageProcessor(realMp).setMuleContext(null);
        assertTrue(realMp.calledSetMuleContext);
    }

    @Test
    public void testSetFlowConstruct() throws MuleException {
        MockedMessageProcessor realMp = new MockedMessageProcessor();
        munitMessageProcessor(realMp).setFlowConstruct(null);
        assertTrue(realMp.calledsetFlowConstruct);
    }

    private MunitMessageProcessor munitMessageProcessor(MockedMessageProcessor realMp) {
        MunitMessageProcessor munitMessageProcessor = new MunitMessageProcessor();
        munitMessageProcessor.setRealMp(realMp);
        return munitMessageProcessor;
    }



}
