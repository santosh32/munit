package org.mule.munit.config;

import org.junit.Test;
import org.mule.api.MuleContext;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MunitFlowTest {

    MuleContext muleContext = mock(MuleContext.class);

    @Test
    public void testFlowDescription(){
        MunitFlow flow = new MunitFlow("name", muleContext);
        flow.setDescription("my Description");

        assertEquals("my Description", flow.getDescription());
    }
}
