package org.mule.munit.config;

import org.junit.Test;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SetMessageProcessorTest extends AbstractMessageProcessorTest{


    public static final String EXP = "#[exp]";
    public static final String PAYLOAD = "r1";

    @Test
    public void calledCorrectly(){
        SetMessageProcessor mp = (SetMessageProcessor) buildMp();
        mp.setPayload(EXP);

        when(expressionManager.evaluate(EXP, muleMessage)).thenReturn(PAYLOAD);

        mp.doProcess(muleMessage, module);

        verify(muleMessage).setPayload(PAYLOAD);
    }

    @Override
    protected MunitMessageProcessor doBuildMp() {
        return new SetMessageProcessor();
    }

    @Override
    protected String getExpectedName() {
        return "set";
    }
}
