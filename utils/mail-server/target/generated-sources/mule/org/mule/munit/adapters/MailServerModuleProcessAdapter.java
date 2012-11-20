
package org.mule.munit.adapters;

import javax.annotation.Generated;
import org.mule.api.MuleEvent;
import org.mule.api.process.ProcessAdapter;
import org.mule.api.process.ProcessCallback;
import org.mule.api.process.ProcessTemplate;
import org.mule.api.process.ProcessTemplate;
import org.mule.api.processor.MessageProcessor;
import org.mule.munit.MailServerModule;


/**
 * A <code>MailServerModuleProcessAdapter</code> is a wrapper around {@link MailServerModule } that enables custom processing strategies.
 * 
 */
@Generated(value = "Mule DevKit Version 3.3.1", date = "2012-11-20T12:38:06-03:00", comments = "Build UNNAMED.1297.150f2c9")
public class MailServerModuleProcessAdapter
    extends MailServerModuleLifecycleAdapter
    implements ProcessAdapter<MailServerModuleCapabilitiesAdapter>
{


    public<P >ProcessTemplate<P, MailServerModuleCapabilitiesAdapter> getProcessTemplate() {
        final MailServerModuleCapabilitiesAdapter object = this;
        return new ProcessTemplate<P,MailServerModuleCapabilitiesAdapter>() {


            @Override
            public P execute(ProcessCallback<P, MailServerModuleCapabilitiesAdapter> processCallback, MessageProcessor messageProcessor, MuleEvent event)
                throws Exception
            {
                return processCallback.process(object);
            }

        }
        ;
    }

}
