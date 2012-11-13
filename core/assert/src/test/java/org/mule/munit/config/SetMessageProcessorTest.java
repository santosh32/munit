package org.mule.munit.config;

import org.junit.Test;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleContext;
import org.mule.api.MuleMessage;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class SetMessageProcessorTest extends AbstractMessageProcessorTest{


    public static final String EXP = "#[exp]";
    public static final String PAYLOAD = "r1";
    public static final String INBOUND_VALUE = "inboundValue";
    public static final String OUTBOUND_VALUE = "outboundValue";
    public static final String INVOCATION_VALUE = "invocationValue";
    public static final String INBOUND_KEY = "inboundKey";
    public static final String OUTBOUND_KEY = "outboundKey";
    public static final String INVOCATION_KEY = "invocationKey";
    private MuleContext muleContext = mock(MuleContext.class);

    @Test
    public void calledCorrectly(){
        SetMessageProcessor mp = (SetMessageProcessor) buildMp();
        mp.setPayload(EXP);
        mp.setInboundProperties(properties(INBOUND_KEY, INBOUND_VALUE));
        mp.setOutboundProperties(properties(OUTBOUND_KEY, OUTBOUND_VALUE));
        mp.setInvocationProperties(properties(INVOCATION_KEY, INVOCATION_VALUE));
        MuleMessage mm = new DefaultMuleMessage("aMessage", muleContext);

        when(expressionManager.evaluate(EXP, mm)).thenReturn(PAYLOAD);
        when(expressionManager.parse( OUTBOUND_VALUE, mm)).thenReturn(OUTBOUND_VALUE);
        when(expressionManager.parse(INBOUND_VALUE, mm)).thenReturn(INBOUND_VALUE);
        when(expressionManager.parse(INVOCATION_VALUE, mm)).thenReturn(INVOCATION_VALUE);

        mp.doProcess(mm, module);

        assertEquals(PAYLOAD, mm.getPayload());
        
        assertEquals(INBOUND_VALUE, mm.getInboundProperty(INBOUND_KEY));
        assertEquals(INVOCATION_VALUE, mm.getInvocationProperty(INVOCATION_KEY));
        assertEquals(OUTBOUND_VALUE, mm.getOutboundProperty(OUTBOUND_KEY));
    }

    private Map<String, Object> properties(String key, Object value) {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put(key, value);
        return properties;
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
