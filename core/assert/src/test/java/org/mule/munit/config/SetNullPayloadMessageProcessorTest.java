package org.mule.munit.config;


import org.junit.Before;
import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.transport.NullPayload;

import static org.mockito.Mockito.*;

public class SetNullPayloadMessageProcessorTest extends AbstractMessageProcessorTest{

    MuleMessage muleMessage;

    @Before
    public void setUp(){
        muleMessage = mock(MuleMessage.class);
    }

    @Test
    public void alwaysSetNullPayload(){
        MunitMessageProcessor mp = buildMp();
        mp.doProcess(muleMessage, null);

        verify(muleMessage, times(1)).setPayload(NullPayload.getInstance());
    }

    @Override
    protected MunitMessageProcessor doBuildMp() {
        return new SetNullPayloadMessageProcessor();
    }

    protected String getExpectedName() {
        return "SetNull";
    }
}
