package org.mule.munit.config;


import org.junit.Test;
import org.mule.transport.NullPayload;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AssertNullMessageProcessorTest extends AbstractMessageProcessorTest{

    public static final String TEST_MESSAGE = "testMessage";


    @Test
    public void testCallCorrectly(){
        MunitMessageProcessor mp = buildMp();

        when(muleMessage.getPayload()).thenReturn(NullPayload.getInstance());

        mp.doProcess(muleMessage, module);

        verify(module).assertNull(TEST_MESSAGE, NullPayload.getInstance());
    }


    @Override
    protected MunitMessageProcessor doBuildMp() {
        AssertNullMessageProcessor mp = new AssertNullMessageProcessor();
        mp.setMessage(TEST_MESSAGE);
        return mp;
    }

    @Override
    protected String getExpectedName() {
        return "assertNull";
    }
}
