package org.mule.munit.config;

import org.junit.Before;
import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.api.expression.ExpressionManager;
import org.mule.munit.AssertModule;
import org.mule.util.TemplateParser;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public abstract class AbstractMessageProcessorTest {
    protected MuleMessage muleMessage;
    protected AssertModule module;
    protected ExpressionManager expressionManager;

    @Before
    public void setUp(){
        muleMessage = mock(MuleMessage.class);
        module = mock(AssertModule.class);
        expressionManager = mock((ExpressionManager.class));
    }

    @Test
    public void checkProcessorName(){
        MunitMessageProcessor mp = buildMp();
        assertEquals(getExpectedName(), mp.getProcessor());
    }

    protected MunitMessageProcessor buildMp(){
        MunitMessageProcessor mp = doBuildMp();
        mp.expressionManager = this.expressionManager;
        mp.patternInfo = TemplateParser.createMuleStyleParser().getStyle();

        return mp;
    }

    protected abstract MunitMessageProcessor doBuildMp();
    protected abstract String getExpectedName();
}
