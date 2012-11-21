package org.mule.munit.runner.mule;

import org.mule.MessageExchangePattern;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.munit.config.MunitFlow;
import org.mule.munit.config.MunitTestFlow;
import org.mule.munit.runner.mule.result.TestResult;
import org.mule.munit.runner.mule.result.notification.Notification;
import org.mule.munit.runner.mule.result.output.TestOutputHandler;
import org.mule.tck.MuleTestUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

/**
 * <p>MUnit Test</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MunitTest {
    /**
     * <p>The MUnit flows that have to be run before the MUnit test.</p>
     */
    private List<MunitFlow> before;

    /**
     * <p>The MUnit flows that have to be run after the MUnit test.</p>
     */
    private List<MunitFlow> after;

    /**
     * <p>The MUnit test.</p>
     */
    private MunitTestFlow test;

    /**
     * <p>The Output handler to be use.</p>
     */
    private TestOutputHandler outputHandler;

    private static String stack2string(Throwable e) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return sw.toString();
        } catch (Exception e2) {
            return "";
        }
    }

    public MunitTest(List<MunitFlow> before,
                     MunitTestFlow test,
                     List<MunitFlow> after,
                     TestOutputHandler outputHandler) {
        this.before = before;
        this.after = after;
        this.test = test;
        this.outputHandler = outputHandler;
    }

    public String getName() {
        return test.getName();
    }

    public TestResult run() {
        TestResult result = new TestResult(getName());
        if ( test.isIgnore() ){
            result.setSkipped(true);
            return result;
        }

        long start = System.currentTimeMillis();
        MuleEvent event = muleEvent();

        try {
            run(event, before);
            showDescription();

            test.process(event);
        } catch (AssertionError t) {
            result.setFailure(buildNotifcationFrom(t));
        } catch (MuleException e) {
            try {
                if ( !test.expectException(e,event) ){
                    result.setError(buildNotifcationFrom(e));
                }
            }catch (AssertionError t) {
                result.setFailure(buildNotifcationFrom(t));
            }
        } finally {
            runAfter(result, event);
        }

        long end = System.currentTimeMillis();
        result.setTime(new Float(end - start)/1000);
        return result;

    }


    private Notification buildNotifcationFrom(Throwable t) {
        return new Notification(t.getMessage(), stack2string(t));
    }

    private void runAfter(TestResult result, MuleEvent event) {
        try {
            run(event, after);
        } catch (MuleException e) {
            result.setError(buildNotifcationFrom(e));
        }
    }

    private void run(MuleEvent event, List<MunitFlow> flows)
            throws MuleException {
        if (flows != null) {
            for (MunitFlow flow : flows) {
                outputHandler.print(flow.getName(), flow.getDescription());
                flow.process(event);
            }
        }
    }

    private void showDescription() {
       outputHandler.print(test.getName(), test.getDescription());
    }

    protected MuleEvent muleEvent() {
        try {
            return MuleTestUtils.getTestEvent(null,
                    MessageExchangePattern.REQUEST_RESPONSE,
                    test.getMuleContext());
        } catch (Exception e) {
            return null;
        }
    }

}
