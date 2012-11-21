package org.mule.munit.config;

import org.apache.commons.lang.StringUtils;
import org.mule.api.MessagingException;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.munit.common.MunitCore;

import static junit.framework.Assert.assertEquals;


/**
 * <p>The Test Flow</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MunitTestFlow extends MunitFlow {
    /**
     * <p>Determines if the test has to be ignored</p>
     */
    private boolean ignore;

    /**
     * <p>The name of the exception that is expected</p>
     */
    private String expected;

    public MunitTestFlow(String name, MuleContext muleContext) {
        super(name, muleContext);

        registerMpManager(muleContext);
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

    public boolean expectException(Throwable t) {

        if (!StringUtils.isEmpty(expected)) {
            String className = t.getClass().getName();
            if (t instanceof MessagingException) {
                Exception causeException = ((MessagingException) t).getCauseException();
                if ( causeException != null ){
                    className = causeException.getClass().getName();
                }
            }
            assertEquals(expected, className);
            return true;
        }


        return false;
    }

    @Override
    public MuleEvent process(MuleEvent event) throws MuleException {
        try{
            MuleEvent process = super.process(event);
            return process;
        }
        finally {
            MunitCore.reset(muleContext);
        }
    }

    private void registerMpManager(MuleContext muleContext) {
        MunitCore.registerManager(muleContext);
    }


}
