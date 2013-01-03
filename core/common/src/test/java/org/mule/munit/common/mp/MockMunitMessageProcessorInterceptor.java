package org.mule.munit.common.mp;

import net.sf.cglib.proxy.MethodProxy;
import org.mule.api.MuleContext;

/**
 * <p>
 *     A class for test only. This just overrides protected methods to simplify class testing
 * </p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MockMunitMessageProcessorInterceptor extends MunitMessageProcessorInterceptor{

    MockedMessageProcessorManager manager;
    boolean mockProcess;
    MuleContext context;

    public MockMunitMessageProcessorInterceptor(MockedMessageProcessorManager manager) {
        this.manager = manager;
    }

    @Override
    public Object process(Object obj, Object[] args, MethodProxy proxy) throws Throwable {
        if ( mockProcess ){
            return  obj;
        }
        return super.process(obj, args, proxy);
    }

    @Override
    protected MockedMessageProcessorManager getMockedMessageProcessorManager() {
        return manager;
    }

    @Override
    public MuleContext getMuleContext() {
        return context;
    }

    public void setMockProcess(boolean mockProcess) {
        this.mockProcess = mockProcess;
    }

    public void setContext(MuleContext context) {
        this.context = context;
    }
}
