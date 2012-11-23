
package org.mule.munit.adapters;

import javax.annotation.Generated;
import org.mule.api.MuleEvent;
import org.mule.api.process.ProcessAdapter;
import org.mule.api.process.ProcessCallback;
import org.mule.api.process.ProcessTemplate;
import org.mule.api.process.ProcessTemplate;
import org.mule.api.processor.MessageProcessor;
import org.mule.munit.DBServerModule;


/**
 * A <code>DBServerModuleProcessAdapter</code> is a wrapper around {@link DBServerModule } that enables custom processing strategies.
 * 
 */
@Generated(value = "Mule DevKit Version 3.3.1", date = "2012-11-23T12:55:32-03:00", comments = "Build 3.3.1.1298.3ae82a7")
public class DBServerModuleProcessAdapter
    extends DBServerModuleLifecycleAdapter
    implements ProcessAdapter<DBServerModuleCapabilitiesAdapter>
{


    public<P >ProcessTemplate<P, DBServerModuleCapabilitiesAdapter> getProcessTemplate() {
        final DBServerModuleCapabilitiesAdapter object = this;
        return new ProcessTemplate<P,DBServerModuleCapabilitiesAdapter>() {


            @Override
            public P execute(ProcessCallback<P, DBServerModuleCapabilitiesAdapter> processCallback, MessageProcessor messageProcessor, MuleEvent event)
                throws Exception
            {
                return processCallback.process(object);
            }

        }
        ;
    }

}
