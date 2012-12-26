package org.mule.munit.runner.mule.context;

import java.util.List;

public class MockingConfiguration {
    private boolean mockInbounds;
    private List<String> mockingExcludedFlows;
    private boolean mockConnectors;

    public MockingConfiguration(boolean mockInbounds, List<String> mockingExcludedFlows, boolean mockConnectors) {
        this.mockInbounds = mockInbounds;
        this.mockingExcludedFlows = mockingExcludedFlows;
        this.mockConnectors = mockConnectors;
    }

    public List<String> getMockingExcludedFlows() {
        return mockingExcludedFlows;
    }

    public boolean isMockInbounds() {
        return mockInbounds;
    }

    public boolean isMockConnectors() {
        return mockConnectors;
    }

}
