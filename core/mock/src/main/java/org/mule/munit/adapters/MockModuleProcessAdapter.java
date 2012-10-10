package org.mule.munit.adapters;

import org.mule.api.MuleEvent;
import org.mule.api.process.ProcessAdapter;
import org.mule.api.process.ProcessCallback;
import org.mule.api.process.ProcessTemplate;
import org.mule.api.processor.MessageProcessor;

import javax.annotation.Generated;


/**
 * A <code>MockModuleProcessAdapter</code> is a wrapper around {@link org.mule.munit.MockModule } that enables custom processing strategies.
 * 
 */
@Generated(value = "Mule DevKit Version 3.3.1", date = "2012-09-24T08:53:54-03:00", comments = "Build UNNAMED.1297.150f2c9")
public class MockModuleProcessAdapter
    extends MockModuleLifecycleAdapter
    implements ProcessAdapter<MockModuleCapabilitiesAdapter>
{


    public<P >ProcessTemplate<P, MockModuleCapabilitiesAdapter> getProcessTemplate() {
        final MockModuleCapabilitiesAdapter object = this;
        return new ProcessTemplate<P,MockModuleCapabilitiesAdapter>() {


            @Override
            public P execute(ProcessCallback<P, MockModuleCapabilitiesAdapter> processCallback, MessageProcessor messageProcessor, MuleEvent event)
                throws Exception
            {
                return processCallback.process(object);
            }

        }
        ;
    }

}
