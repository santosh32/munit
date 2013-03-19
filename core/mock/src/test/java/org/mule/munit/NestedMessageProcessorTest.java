package org.mule.munit;


import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.NestedProcessor;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class NestedMessageProcessorTest {

    private NestedProcessor nestedProcessor = mock(NestedProcessor.class);
    private MuleEvent inMuleEvent = mock(MuleEvent.class);
    private MuleEvent outMuleEvent = mock(MuleEvent.class);



    @Test
    public void happyPath() throws Exception {
        when(nestedProcessor.process()).thenReturn(outMuleEvent);
        assertEquals(inMuleEvent, mp().process(inMuleEvent));

        verify(nestedProcessor, times(1)).process();
    }

    @Test(expected = MuleException.class)
    public void exceptionHandling() throws Exception {
        when(nestedProcessor.process()).thenThrow(new Exception());
        mp().process(inMuleEvent);

        verify(nestedProcessor, times(1)).process();
    }

    private NestedMessageProcessor mp() {
        return new NestedMessageProcessor(nestedProcessor);
    }

}
