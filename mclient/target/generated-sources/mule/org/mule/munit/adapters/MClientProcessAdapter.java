
package org.mule.munit.adapters;

import javax.annotation.Generated;
import org.mule.api.MuleEvent;
import org.mule.api.process.ProcessAdapter;
import org.mule.api.process.ProcessCallback;
import org.mule.api.process.ProcessTemplate;
import org.mule.api.process.ProcessTemplate;
import org.mule.api.processor.MessageProcessor;
import org.mule.munit.MClient;


/**
 * A <code>MClientProcessAdapter</code> is a wrapper around {@link MClient } that enables custom processing strategies.
 * 
 */
@Generated(value = "Mule DevKit Version 3.3.1", date = "2012-09-24T08:54:01-03:00", comments = "Build UNNAMED.1297.150f2c9")
public class MClientProcessAdapter
    extends MClientLifecycleAdapter
    implements ProcessAdapter<MClientCapabilitiesAdapter>
{


    public<P >ProcessTemplate<P, MClientCapabilitiesAdapter> getProcessTemplate() {
        final MClientCapabilitiesAdapter object = this;
        return new ProcessTemplate<P,MClientCapabilitiesAdapter>() {


            @Override
            public P execute(ProcessCallback<P, MClientCapabilitiesAdapter> processCallback, MessageProcessor messageProcessor, MuleEvent event)
                throws Exception
            {
                return processCallback.process(object);
            }

        }
        ;
    }

}
