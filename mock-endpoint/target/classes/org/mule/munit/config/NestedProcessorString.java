
package org.mule.munit.config;

import java.util.Map;
import org.mule.api.NestedProcessor;

public class NestedProcessorString
    implements NestedProcessor
{

    /**
     * Output string to be returned on process
     * 
     */
    private String output;

    public NestedProcessorString(String output) {
        this.output = output;
    }

    /**
     * Sets output
     * 
     * @param value Value to set
     */
    public void setOutput(String value) {
        this.output = value;
    }

    public Object process()
        throws Exception
    {
        return output;
    }

    public Object process(Object payload)
        throws Exception
    {
        return output;
    }

    public Object processWithExtraProperties(Map<String, Object> properties)
        throws Exception
    {
        return output;
    }

    public Object process(Object payload, Map<String, Object> properties)
        throws Exception
    {
        return output;
    }

}
