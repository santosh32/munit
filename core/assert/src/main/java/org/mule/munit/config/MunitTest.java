package org.mule.munit.config;

import org.mule.api.MuleContext;


public class MunitTest extends MunitFlow{
    private boolean ignore;

    public MunitTest(String name, MuleContext muleContext) {
        super(name, muleContext);
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    public boolean isIgnore() {
        return ignore;
    }
}
