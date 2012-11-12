package org.mule.munit.config;

import org.junit.Test;
import org.mule.transport.NullPayload;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class AssertThatMessageProcessorTest extends AbstractMessageProcessorTest{

    public static final String TEST_MESSAGE = "testMessage";
    public static final String EXP = "#[exp]";
    public static final String RETURN_VALUE = "r1";

    @Test
    public void calledCorrectly(){
        AssertThatMessageProcessor mp = (AssertThatMessageProcessor) buildMp();
        mp.setPayloadIs(EXP);

        when(expressionManager.evaluate(EXP, muleMessage)).thenReturn(RETURN_VALUE);
        when(muleMessage.getPayload()).thenReturn(NullPayload.getInstance());

        mp.doProcess(muleMessage, module);

        verify(module).assertThat(TEST_MESSAGE, RETURN_VALUE, NullPayload.getInstance());
    }

    @Override
    protected MunitMessageProcessor doBuildMp() {
        AssertThatMessageProcessor mp = new AssertThatMessageProcessor();
        mp.setMessage(TEST_MESSAGE);
        return mp;
    }

    @Override
    protected String getExpectedName() {
        return "assertThat";
    }
}
