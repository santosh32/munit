package org.mule.munit;

import org.mule.munit.common.mocking.MessageProcessorMocker;

/**
 * <p>
 *     This name might sound incorrect but it is a mock of the mock module.
 * </p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MockMockModule  extends MockModule{

    MessageProcessorMocker mocker;

    public MockMockModule(MessageProcessorMocker mocker) {
        this.mocker = mocker;
    }

    @Override
    protected MessageProcessorMocker mocker() {
        return mocker;
    }
}
