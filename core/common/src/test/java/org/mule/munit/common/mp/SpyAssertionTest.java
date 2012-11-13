package org.mule.munit.common.mp;

import org.junit.Test;
import org.mule.api.processor.MessageProcessor;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

/**
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class SpyAssertionTest {

    @Test
    public void gettersMustReturnTheOriginalValues(){
        ArrayList<MessageProcessor> beforeMessageProcessors = new ArrayList<MessageProcessor>();
        ArrayList<MessageProcessor> afterMessageProcessors = new ArrayList<MessageProcessor>();
        SpyAssertion spyAssertion = new SpyAssertion(beforeMessageProcessors, afterMessageProcessors);

        assertEquals(beforeMessageProcessors, spyAssertion.getBeforeMessageProcessors());
        assertEquals(afterMessageProcessors, spyAssertion.getAfterMessageProcessors());
    }
}
