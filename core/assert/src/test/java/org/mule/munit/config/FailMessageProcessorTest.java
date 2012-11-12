package org.mule.munit.config;

import org.junit.Test;

import static org.mockito.Mockito.verify;

/**
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class FailMessageProcessorTest extends AbstractMessageProcessorTest{

    public static final String TEST_MESSAGE = "testMessage";


    @Test
    public void calledCorrectly(){
        MunitMessageProcessor mp = buildMp();


        mp.doProcess(muleMessage, module);

        verify(module).fail(TEST_MESSAGE);
    }

    @Override
    protected MunitMessageProcessor doBuildMp() {
        FailMessageProcessor mp = new FailMessageProcessor();
        mp.setMessage(TEST_MESSAGE);
        return mp;
    }

    @Override
    protected String getExpectedName() {
        return "Fail";
    }
}
