package org.mule.munit.runner.mule.context;

import java.util.List;

public class MockingConfiguration {
    private boolean mockInbounds;
    private List<String> mockingExcludedFlows;

    public MockingConfiguration(boolean mockInbounds, List<String> mockingExcludedFlows) {
        this.mockInbounds = mockInbounds;
        this.mockingExcludedFlows = mockingExcludedFlows;
    }

    public List<String> getMockingExcludedFlows() {
        return mockingExcludedFlows;
    }

    public boolean isMockInbounds() {
        return mockInbounds;
    }
}
