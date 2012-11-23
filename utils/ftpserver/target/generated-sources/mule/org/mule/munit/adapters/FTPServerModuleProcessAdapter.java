
package org.mule.munit.adapters;

import javax.annotation.Generated;
import org.mule.api.MuleEvent;
import org.mule.api.process.ProcessAdapter;
import org.mule.api.process.ProcessCallback;
import org.mule.api.process.ProcessTemplate;
import org.mule.api.process.ProcessTemplate;
import org.mule.api.processor.MessageProcessor;
import org.mule.munit.FTPServerModule;


/**
 * A <code>FTPServerModuleProcessAdapter</code> is a wrapper around {@link FTPServerModule } that enables custom processing strategies.
 * 
 */
@Generated(value = "Mule DevKit Version 3.3.1", date = "2012-11-23T03:25:05-03:00", comments = "Build 3.3.1.1298.3ae82a7")
public class FTPServerModuleProcessAdapter
    extends FTPServerModuleLifecycleAdapter
    implements ProcessAdapter<FTPServerModuleCapabilitiesAdapter>
{


    public<P >ProcessTemplate<P, FTPServerModuleCapabilitiesAdapter> getProcessTemplate() {
        final FTPServerModuleCapabilitiesAdapter object = this;
        return new ProcessTemplate<P,FTPServerModuleCapabilitiesAdapter>() {


            @Override
            public P execute(ProcessCallback<P, FTPServerModuleCapabilitiesAdapter> processCallback, MessageProcessor messageProcessor, MuleEvent event)
                throws Exception
            {
                return processCallback.process(object);
            }

        }
        ;
    }

}
