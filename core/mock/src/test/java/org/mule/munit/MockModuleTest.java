package org.mule.munit;

import org.junit.Test;
import org.mule.api.MuleContext;
import org.mule.api.MuleMessage;
import org.mule.munit.common.mocking.MessageProcessorMocker;

import java.util.*;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.*;

/**
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MockModuleTest {

    public static final String NAMESPACE = "namespace";
    public static final String MESSAGE_PROCESSOR = "mp";
    public static final String INBOUND_KEY = "inboundKey";
    public static final String INBOUND_VALUE = "inboundValue";
    public static final String OUTBOUND_KEY = "outboundKey";
    public static final String OUTBOUND_VALUE = "outboundValue";
    public static final String INVOCATION_KEY = "invocationKey";
    public static final String INVOCATION_VALUE = "invocationValue";
    public static final String SESSION_VALUE = "sessionValue";
    public static final String SESSION_KEY = "sessionKey";
    public static final String PAYLOAD = "payload";
    public static final Exception EXCEPTION = new Exception("error");
    private MessageProcessorMocker mocker = mock(MessageProcessorMocker.class);
    private MuleContext muleContext = mock(MuleContext.class);

    @Test
    public void whenMethodCanHandlerNullOptionals(){
        defineMockerBehavior();
        
        module().when(NAMESPACE+":"+ MESSAGE_PROCESSOR, null,null);

        verify(mocker,times(1)).when(MESSAGE_PROCESSOR);
        verify(mocker,times(1)).ofNamespace(NAMESPACE);
        verify(mocker,times(1)).withAttributes((Map<String, Object>) notNull());
        verify(mocker,times(1)).thenReturn(any(MuleMessage.class));
    }
    
    @Test
    public void whenMethodCanHandleNotNullProperties(){
        defineMockerBehavior();

        module().when(NAMESPACE+":"+ MESSAGE_PROCESSOR, createAttributes(),null);

        verify(mocker,times(1)).when(MESSAGE_PROCESSOR);
        verify(mocker,times(1)).ofNamespace(NAMESPACE);
        verify(mocker,times(1)).withAttributes((Map<String, Object>) notNull());
        verify(mocker,times(1)).thenReturn(any(MuleMessage.class));
    }


    @Test
    public void whenMethodCanHandleNotNullReturnValue(){
        defineMockerBehavior();

        module().when(NAMESPACE+":"+ MESSAGE_PROCESSOR, createAttributes(),createMuleMessage());

        verify(mocker,times(1)).when(MESSAGE_PROCESSOR);
        verify(mocker,times(1)).ofNamespace(NAMESPACE);
        verify(mocker,times(1)).withAttributes((Map<String, Object>) notNull());
        verify(mocker,times(1)).thenReturn(any(MuleMessage.class));
    }

    @Test
    public void throwExceptionMustSupportNullOptionals(){
        defineMockerBehavior();
        module().throwAn(EXCEPTION, NAMESPACE + ":" + MESSAGE_PROCESSOR, null);

        verify(mocker,times(1)).when(MESSAGE_PROCESSOR);
        verify(mocker,times(1)).ofNamespace(NAMESPACE);
        verify(mocker,times(1)).withAttributes((Map<String, Object>) notNull());
        verify(mocker,times(1)).thenThrow(EXCEPTION);
    }

    @Test
    public void throwExceptionMustSupportAttributes(){
        defineMockerBehavior();
        module().throwAn(EXCEPTION, NAMESPACE + ":" + MESSAGE_PROCESSOR, createAttributes());

        verify(mocker,times(1)).when(MESSAGE_PROCESSOR);
        verify(mocker,times(1)).ofNamespace(NAMESPACE);
        verify(mocker,times(1)).withAttributes((Map<String, Object>) notNull());
        verify(mocker,times(1)).thenThrow(EXCEPTION);
    }

    private MunitMuleMessage createMuleMessage() {
        MunitMuleMessage munitMuleMessage = new MunitMuleMessage();
        munitMuleMessage.setInboundProperties(props(entry(INBOUND_KEY, INBOUND_VALUE)));
        munitMuleMessage.setOutboundProperties(props(entry(OUTBOUND_KEY, OUTBOUND_VALUE)));
        munitMuleMessage.setInvocationProperties(props(entry(INVOCATION_KEY, INVOCATION_VALUE)));
//        munitMuleMessage.setSessionProperties(props(entry(SESSION_KEY, SESSION_VALUE)));
        munitMuleMessage.setPayload(PAYLOAD);
        return munitMuleMessage;
    }

    private HashMap<String, Object> props(Map.Entry<String,Object> ... entries) {
        HashMap<String, Object> map = new HashMap<String, Object>();

        for ( Map.Entry<String,Object> entry : entries ){
            map.put(entry.getKey(),entry.getValue());
        }
        return map;
    }

    private Map.Entry<String, Object> entry(String key, Object value){
        return new HashMap.SimpleEntry<String,Object>(key,value);
    }

    private List<Attribute> createAttributes() {
        List<Attribute> attributes = new ArrayList<Attribute>();
        attributes.add(Attribute.create("attribute", "attributeValue"));
        return attributes;
    }

    private void defineMockerBehavior() {
        when(mocker.when(anyString())).thenReturn(mocker);
        when(mocker.withAttributes(anyMap())).thenReturn(mocker);
        when(mocker.ofNamespace(anyString())).thenReturn(mocker);
    }



    private MockMockModule module() {
        MockMockModule mockMockModule = new MockMockModule(mocker);
        mockMockModule.setMuleContext(muleContext);
        return mockMockModule;
    }
}
