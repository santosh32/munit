package org.mule.munit.config;

import org.apache.commons.lang.StringUtils;
import org.mule.api.MessagingException;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.expression.ExpressionManager;
import org.mule.munit.common.MunitCore;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;


/**
 * <p>
 *     The Test Flow
 * </p>
 *
 * @author Federico, Fernando
 * @since 3.3.2
 */
public class MunitTestFlow extends MunitFlow {
    /**
     * <p>Determines if the test has to be ignored</p>
     */
    private boolean ignore;

    /**
     * <p>The name of the exception that is expected</p>
     */
    private String expectExceptionThatSatisfies;

    public MunitTestFlow(String name, MuleContext muleContext) {
        super(name, muleContext);

        registerMpManager(muleContext);
    }

    public String getExpectExceptionThatSatisfies() {
        return expectExceptionThatSatisfies;
    }

    public void setExpectExceptionThatSatisfies(String expectExceptionThatSatisfies) {
        this.expectExceptionThatSatisfies = expectExceptionThatSatisfies;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    public boolean isIgnore() {
        return ignore;
    }

    private boolean expectException(Throwable t) {

        String className = t.getClass().getName();
        if (t instanceof MessagingException) {
            Exception causeException = ((MessagingException) t).getCauseException();
            if (causeException != null) {
                className = causeException.getClass().getName();
            }
        }
        assertEquals(expectExceptionThatSatisfies, className);
        return true;
    }

    public boolean expectException(Throwable t, MuleEvent event) {

        if (!StringUtils.isEmpty(expectExceptionThatSatisfies)) {
            ExpressionManager expressionManager = muleContext.getExpressionManager();
            if ( expressionManager.isExpression(expectExceptionThatSatisfies)) {
                Boolean expressionResult = (Boolean) expressionManager.evaluate(expectExceptionThatSatisfies, event);
                if ( !expressionResult ){
                    fail("The exception does not match your MEL expression");
                }
                return true;
            }
            else{
                return expectException(t);
            }
            
        }

        return false;
    }

    @Override
    public MuleEvent process(MuleEvent event) throws MuleException {

            MuleEvent process = super.process(event);
            return process;


    }

    private void registerMpManager(MuleContext muleContext) {
        MunitCore.registerManager(muleContext);
    }


}
