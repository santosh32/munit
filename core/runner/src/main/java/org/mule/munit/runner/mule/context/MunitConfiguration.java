package org.mule.munit.runner.mule.context;

import java.util.List;
import java.util.Properties;

public class MunitConfiguration {
    private boolean mockInbounds;
    private List<String> mockingExcludedFlows;
    private boolean mockConnectors;
    private Properties startupProperties;

    public MunitConfiguration(boolean mockInbounds, List<String> mockingExcludedFlows, boolean mockConnectors, Properties startupProperties) {
        this.mockInbounds = mockInbounds;
        this.mockingExcludedFlows = mockingExcludedFlows;
        this.mockConnectors = mockConnectors;
        this.startupProperties = startupProperties;
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

    public Properties getStartupProperties() {
        return startupProperties;
    }

}
