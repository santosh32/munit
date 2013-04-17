package org.mule.munit.runner.functional;

import org.junit.After;
import org.junit.Before;
import org.mule.DefaultMuleMessage;
import org.mule.MessageExchangePattern;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.construct.Flow;
import org.mule.modules.interceptor.matchers.*;
import org.mule.munit.common.MunitCore;
import org.mule.munit.common.mocking.EndpointMocker;
import org.mule.munit.common.mocking.MessageProcessorMocker;
import org.mule.munit.common.mocking.MunitSpy;
import org.mule.munit.common.mocking.MunitVerifier;
import org.mule.munit.runner.MuleContextManager;
import org.mule.munit.runner.mule.context.MockingConfiguration;
import org.mule.tck.MuleTestUtils;

import java.util.*;

public abstract class FunctionalMunitSuite {

    protected MuleContext muleContext;

    public FunctionalMunitSuite() {
        MuleContextManager muleContextManager = new MuleContextManager(createConfiguration());
        try {
            String resources = getConfigResources();

            muleContext = muleContextManager.startMule(resources);

        } catch (Exception e) {
            muleContextManager.killMule(muleContext);
            throw new RuntimeException(e);
        }
    }

    private MockingConfiguration createConfiguration()
    {
        return new MockingConfiguration(mockInboundEndpoints(), mockingExcludedFlows(), mockConnectors(), getStartUpProperties());
    }

    protected List<String> mockingExcludedFlows() {
        return new ArrayList<String>();
    }

    protected boolean mockInboundEndpoints() {
        return true;
    }

    protected boolean mockConnectors(){
        return true;
    }

    @Before
    public final void __setUpMunit(){
        MunitCore.registerManager(muleContext);
    }
    
    @After
    public final void __restartMunit(){
        MunitCore.reset(muleContext);
    }

    protected abstract String getConfigResources();

    protected final MuleEvent testEvent(Object payload) throws Exception {
        return MuleTestUtils.getTestEvent(payload, MessageExchangePattern.REQUEST_RESPONSE, muleContext);
    }

    protected final MuleMessage muleMessageWithPayload(Object payload){
        return  new DefaultMuleMessage(payload, muleContext);
    }

    protected final MessageProcessorMocker whenMessageProcessor(String name){
        return new MessageProcessorMocker(muleContext).when(name);
    }
    
    protected final MunitVerifier verifyCallOfMessageProcessor(String name){
        return new MunitVerifier(muleContext).verifyCallOfMessageProcessor(name);
    }
    
    protected final MunitSpy spyMessageProcessor(String name){
        return new MunitSpy(muleContext).spyMessageProcessor(name);
    }
    
    protected final MuleEvent runFlow(String name, MuleEvent event) throws MuleException {
        Flow flow = (Flow) muleContext.getRegistry().lookupFlowConstruct(name);
        if ( flow == null ){
            throw new IllegalArgumentException("Flow " + name + " does not exist");
        }
        return flow.process(event);
    }

    protected final EndpointMocker expectOutboundEndpointWithAddress(String address) {
        EndpointMocker endpointMocker = new EndpointMocker(muleContext);
        return endpointMocker.expectEndpointWithAddress(address);
    }

    protected final Matcher any(){
        return new AnyClassMatcher(Object.class);
    }

    protected final Matcher isNotNull(){
        return new NotNullMatcher();
    }

    protected final Matcher isNull(){
       return new NullMatcher();
    }

    protected final  Matcher anyCollection(){
        return new AnyClassMatcher(Collection.class);
    }

    protected final Matcher anyMap(){
        return new AnyClassMatcher(Map.class);
    }

    protected final Matcher anySet(){
        return new AnyClassMatcher(Set.class);
    }

    protected final Matcher anyList(){
        return new AnyClassMatcher(List.class);
    }
    
    protected final Matcher anyString(){
        return new AnyClassMatcher(String.class);
    }
    
    protected final Matcher anyObject(){
        return new AnyClassMatcher(Object.class);
    }

    protected final Matcher anyShort(){
        return new AnyClassMatcher(Short.class);
    }
    
    protected final Matcher anyFloat(){
        return new AnyClassMatcher(Float.class);
    }
    
    protected Matcher anyDouble(){
        return new AnyClassMatcher(Double.class);
    }
    
    protected final Matcher eq(Object o){
        return new EqMatcher(o);
    }

    protected final Matcher anyBoolean(){
        return new AnyClassMatcher(Boolean.class);
    }
    
    protected final Matcher anyByte(){
        return new AnyClassMatcher(Byte.class); 
    }
    
    protected final Matcher anyInt(){
        return new AnyClassMatcher(Integer.class);
    }

    protected Properties getStartUpProperties()
    {
        return null;
    }
}
