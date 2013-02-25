package org.mule.munit.runner.java;

import junit.framework.TestCase;
import org.mule.MessageExchangePattern;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.munit.common.MunitCore;
import org.mule.munit.config.MunitFlow;
import org.mule.munit.config.MunitTestFlow;
import org.mule.munit.runner.output.*;
import org.mule.tck.MuleTestUtils;

import java.util.List;

import static org.mule.munit.common.MunitCore.getStackTraceElements;


public class MunitTest extends TestCase
{

    private List<MunitFlow> before;
    private MunitTestFlow flow;
    private List<MunitFlow> after;
    private MuleContext muleContext;
    private TestOutputHandler outputHandler = new DefaultOutputHandler();


    public MunitTest(List<MunitFlow> before, MunitTestFlow flow, List<MunitFlow> after) {
        this.before = before;
        this.flow = flow;
        this.after = after;
        this.muleContext = flow.getMuleContext();
    }

    public String getName()
    {
        return flow.getName();
    }

    @Override
    public int countTestCases() {
        return 1;
    }

    @Override
    protected void runTest() throws Throwable {
        if ( flow.isIgnore() ){
            return;
        }

        MuleEvent event = muleEvent();
        run(event, before);

        showDescription();

        try{
            flow.process(event);
        }
        catch(Throwable t)
        {
            if ( !flow.expectException(t,event) ){
                List<StackTraceElement> stackTraceElements = getStackTraceElements(event.getMuleContext());
                t.setStackTrace(stackTraceElements.toArray(new StackTraceElement[]{}));
                throw t;
            }

        }
        finally {
            MunitCore.reset(muleContext);
            run(event, after);
        }
    }

    private void run(MuleEvent event, List<MunitFlow> flows) throws MuleException {
        if (flows != null)
        {
            for ( MunitFlow flow : flows )
            {
                outputHandler.printDescription(flow.getName(), flow.getDescription());
                flow.process(event);
            }
        }
    }

    private void showDescription() {
        outputHandler.printDescription(flow.getName(), flow.getDescription().replaceAll("\\.", "\\.%n"));
    }


    protected MuleEvent muleEvent() {
        try {
            return MuleTestUtils.getTestEvent(null,
                    MessageExchangePattern.REQUEST_RESPONSE,
                    muleContext);
        } catch (Exception e) {
            return null;
        }
    }

}

