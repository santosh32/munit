package org.mule.munit.runner.java;

import junit.framework.TestCase;
import org.mule.MessageExchangePattern;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.munit.config.MunitFlow;
import org.mule.munit.config.MunitTestFlow;
import org.mule.tck.MuleTestUtils;

import java.util.List;


public class MunitTest extends TestCase
{

    private List<MunitFlow> before;
    MunitTestFlow flow;
    private List<MunitFlow> after;
    private MuleContext muleContext;


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
            if ( !flow.expectException(t) ){
                throw t;
            }
        }
        finally {
            run(event, after);
        }
    }

    private void run(MuleEvent event, List<MunitFlow> flows) throws MuleException {
        if (flows != null)
        {
            for ( MunitFlow flow : flows )
            {
                System.out.printf(flow.getDescription() + "%n");
                flow.process(event);
            }
        }
    }

    private void showDescription() {
        System.out.printf("%nDescription:%n************%n" + flow.getDescription().replaceAll("\\.", "\\.%n") + "%n");
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

