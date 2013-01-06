package org.mule.munit;

import org.mule.munit.common.mocking.EndpointMocker;
import org.mule.munit.common.mocking.MessageProcessorMocker;
import org.mule.munit.common.mocking.MunitSpy;

/**
 * <p>
 *     This name might sound incorrect but it is a mock of the mock module.
 * </p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MockMockModule  extends MockModule{

    private MessageProcessorMocker mocker;
    private EndpointMocker endpointMocker;
    private MunitSpy spy;

    public MockMockModule(MessageProcessorMocker mocker, EndpointMocker endpointMocker, MunitSpy spy) {
        this.mocker = mocker;
        this.endpointMocker = endpointMocker;
        this.spy = spy;
    }

    @Override
    protected MessageProcessorMocker mocker() {
        return mocker;
    }

    @Override
    protected EndpointMocker endpointMocker() {
        return endpointMocker;
    }

    @Override
    protected MunitSpy spy() {
        return spy;
    }
}
