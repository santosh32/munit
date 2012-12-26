package org.mule.munit.runner;

import org.mule.MessageExchangePattern;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.munit.config.MunitAfterSuite;
import org.mule.munit.config.MunitBeforeSuite;
import org.mule.munit.config.MunitFlow;
import org.mule.munit.runner.output.*;
import org.mule.tck.MuleTestUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>Runs the Munit Suite</p>
 *
 *  <p>T is the expected result from the suite run</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public abstract class MunitRunner<T> {

    private TestOutputHandler handler = new DefaultOutputHandler();
    private MuleContextManager muleContextManager = new MuleContextManager(null);

    private MuleContext muleContext;

    public MunitRunner(TestOutputHandler handler, MuleContextManager muleContextManager, MuleContext muleContext) {
        this.handler = handler;
        this.muleContextManager = muleContextManager;
        this.muleContext = muleContext;
    }

    /**
     * <p>Runs all the tests of the suite</p>
     * @return The suite result
     * @throws Exception If the suite fails
     */
    protected abstract T runSuite() throws Exception;

    /**
     * <p>Get the name of the suite</p>
     * @return The suite name
     */
    protected abstract String getSuiteName();

    /**
     * <p>Runs the suite based on the constructor arguments </p>
     * @return The suite result
     */
    public T run() {
        try {
            handler.printTestName(getSuiteName());
            process(lookupFlows(MunitBeforeSuite.class), muleEvent());

            T result = runSuite();

            return result;

        } catch (Exception e) {
            muleContextManager.killMule(muleContext);
            throw new RuntimeException("Could not Run the suite", e);
        }
        finally {
            try {
                process(lookupFlows(MunitAfterSuite.class), muleEvent());
            } catch (MuleException e) {
                throw new RuntimeException("After Suite process could not be executed", e);
            }

            muleContextManager.killMule(muleContext);
        }

    }

    private MuleEvent muleEvent() {
        try {
            return MuleTestUtils.getTestEvent(null,
                    MessageExchangePattern.REQUEST_RESPONSE, muleContext);
        } catch (Exception e) {
            return null;
        }
    }

    private void process(Collection<MunitFlow> flowConstructs, MuleEvent event)
            throws MuleException {
        for (MunitFlow flowConstruct : flowConstructs) {
            handler.printDescription(flowConstruct.getName(), flowConstruct.getDescription());
            (flowConstruct).process(event);
        }
    }

    private List<MunitFlow> lookupFlows(Class munitClass) {
        return new ArrayList<MunitFlow>(muleContext.getRegistry()
                .lookupObjects(munitClass));
    }
}
