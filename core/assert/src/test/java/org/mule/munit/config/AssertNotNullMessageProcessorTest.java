package org.mule.munit.config;


import org.junit.Test;
import org.mule.transport.NullPayload;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class AssertNotNullMessageProcessorTest extends AbstractMessageProcessorTest{

    public static final String TEST_MESSAGE = "TEST MESSAGE";


    @Test
    public void testSendCorrectParams(){
        MunitMessageProcessor mp = buildMp();

        when(muleMessage.getPayload()).thenReturn(NullPayload.getInstance());

        mp.doProcess(muleMessage, module);

        verify(module, times(1)).assertNotNull(TEST_MESSAGE, NullPayload.getInstance());
        verify(muleMessage, times(1)).getPayload();
    }
    @Override
    protected MunitMessageProcessor doBuildMp() {
        AssertNotNullMessageProcessor mp = new AssertNotNullMessageProcessor();
        mp.setMessage(TEST_MESSAGE);
        return mp;
    }

    @Override
    protected String getExpectedName() {
        return "assertNotNull";
    }
}
