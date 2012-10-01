package org.mule.munit.config;

import org.mule.api.MuleContext;


/**
 * <p>The Test Flow</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
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
