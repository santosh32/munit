package org.mule.munit.config;

import org.mule.api.MuleContext;

import static junit.framework.Assert.assertEquals;


/**
 * <p>The Test Flow</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MunitTestFlow extends MunitFlow{
    private boolean ignore;
    private String expected;

    public MunitTestFlow(String name, MuleContext muleContext) {
        super(name, muleContext);
    }

    public String getExpected() {
        return expected;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    public boolean isIgnore() {
        return ignore;
    }
    
    public boolean expectException(Throwable t){
        if ( expected != null ){
            assertEquals(expected, t.getClass().getName());
            return true;
        }
        return false;
    }


}
