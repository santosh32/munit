package org.mule.munit;

import org.junit.Test;
import org.mule.api.*;
import org.mule.api.processor.MessageProcessor;
import org.mule.munit.common.mocking.EndpointMocker;
import org.mule.munit.common.mocking.MessageProcessorMocker;
import org.mule.munit.common.mocking.MunitSpy;
import org.mule.munit.common.mocking.SpyProcess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public static final String FULL_NAME = NAMESPACE + ":" + MESSAGE_PROCESSOR;
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
    public static final String ADDRESS = "address";
    private MessageProcessorMocker mocker = mock(MessageProcessorMocker.class);
    private MuleContext muleContext = mock(MuleContext.class);
    private EndpointMocker endpointMocker = mock(EndpointMocker.class);
    private MunitSpy spy = mock(MunitSpy.class);
    private MessageProcessor messageProcessor = mock(MessageProcessor.class);

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
    public void noNamespaceMeansMuleNamespace(){
        defineMockerMuleNamespaceBehavior();

        module().when(MESSAGE_PROCESSOR, createAttributes(),null);

        verify(mocker,times(1)).when(MESSAGE_PROCESSOR);
        verify(mocker,times(1)).ofNamespace("mule");
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
        module().throwAn(EXCEPTION, FULL_NAME, null);

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

    @Test
    public void endpointMockingMustSupportNullOptionals(){
        endpointMockerBehavior();

        module().outboundEndpoint(ADDRESS, null,null,null,null,null,null);

        verify(endpointMocker,times(1)).expectEndpointWithAddress(ADDRESS);
        verify(endpointMocker, times(1)).withIncomingMessageSatisfying((List<SpyProcess>) notNull());
        verify(endpointMocker, times(1)).toReturn((MuleMessage) notNull());
    }


    @Test
    public void endpointMockingMustSupportOptionals(){
        endpointMockerBehavior();

        module().outboundEndpoint(ADDRESS, PAYLOAD,
                props(entry(INVOCATION_KEY,INVOCATION_VALUE)),
                props(entry(INBOUND_KEY,INBOUND_VALUE)),
                props(entry(SESSION_KEY, SESSION_VALUE)),
                props(entry(OUTBOUND_KEY,OUTBOUND_VALUE)),
                createAssertions());

        verify(endpointMocker,times(1)).expectEndpointWithAddress(ADDRESS);
        verify(endpointMocker, times(1)).withIncomingMessageSatisfying((List<SpyProcess>) notNull());
        verify(endpointMocker, times(1)).toReturn((MuleMessage) notNull());
    }

    @Test
    public void spyMustSupportNullOptionals(){
        spyBehavior();

        module().spy(FULL_NAME, null,null);

        verify(spy, times(1)).spyMessageProcessor(MESSAGE_PROCESSOR);
        verify(spy, times(1)).ofNamespace(NAMESPACE);
        verify(spy, times(1)).running((List<SpyProcess>) notNull(), (List<SpyProcess>) notNull());
    }

    @Test
    public void spyMustSupportOptionals(){
        spyBehavior();

        module().spy(FULL_NAME, createAssertions(),createAssertions());

        verify(spy, times(1)).spyMessageProcessor(MESSAGE_PROCESSOR);
        verify(spy, times(1)).ofNamespace(NAMESPACE);
        verify(spy, times(1)).running((List<SpyProcess>) notNull(), (List<SpyProcess>) notNull());
    }

    @Test
    public void createSpyIsCorrect() throws MuleException {
        SpyProcess spyProcess = module().createSpy(createMessageProcessors());

        MuleEvent event = mock(MuleEvent.class);
        spyProcess.spy(event);

        verify(messageProcessor, times(1)).process(event);
    }

    private List<MessageProcessor> createMessageProcessors() {
        ArrayList<MessageProcessor> messageProcessors = new ArrayList<MessageProcessor>();

        messageProcessors.add(messageProcessor);
        return messageProcessors;
    }

    private void spyBehavior() {
        when(spy.ofNamespace(NAMESPACE)).thenReturn(spy);
        when(spy.spyMessageProcessor(MESSAGE_PROCESSOR)).thenReturn(spy);
    }

    private List<NestedProcessor> createAssertions() {
        ArrayList<NestedProcessor> nestedProcessors = new ArrayList<NestedProcessor>();
        nestedProcessors.add(mock(NestedProcessor.class));
        return nestedProcessors;
    }

    private MunitMuleMessage createMuleMessage() {
        MunitMuleMessage munitMuleMessage = new MunitMuleMessage();
        munitMuleMessage.setInboundProperties(props(entry(INBOUND_KEY, INBOUND_VALUE)));
        munitMuleMessage.setOutboundProperties(props(entry(OUTBOUND_KEY, OUTBOUND_VALUE)));
        munitMuleMessage.setInvocationProperties(props(entry(INVOCATION_KEY, INVOCATION_VALUE)));
        // TODO: add this line and make test work
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
        when(mocker.when(MESSAGE_PROCESSOR)).thenReturn(mocker);
        when(mocker.withAttributes(anyMap())).thenReturn(mocker);
        when(mocker.ofNamespace(NAMESPACE)).thenReturn(mocker);
    }

    private void defineMockerMuleNamespaceBehavior() {
        when(mocker.when(MESSAGE_PROCESSOR)).thenReturn(mocker);
        when(mocker.withAttributes(anyMap())).thenReturn(mocker);
        when(mocker.ofNamespace("mule")).thenReturn(mocker);
    }

    private MockMockModule module() {
        MockMockModule mockMockModule = new MockMockModule(mocker, endpointMocker, spy);
        mockMockModule.setMuleContext(muleContext);
        return mockMockModule;
    }

    private void endpointMockerBehavior() {
        when(endpointMocker.expectEndpointWithAddress(anyString())).thenReturn(endpointMocker);
        when(endpointMocker.withIncomingMessageSatisfying(anyList())).thenReturn(endpointMocker);
    }
}
