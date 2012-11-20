
package org.mule.munit.adapters;

import javax.annotation.Generated;
import org.mule.api.Capabilities;
import org.mule.api.Capability;
import org.mule.munit.MailServerModule;


/**
 * A <code>MailServerModuleCapabilitiesAdapter</code> is a wrapper around {@link MailServerModule } that implements {@link org.mule.api.Capabilities} interface.
 * 
 */
@Generated(value = "Mule DevKit Version 3.3.1", date = "2012-11-20T12:38:06-03:00", comments = "Build UNNAMED.1297.150f2c9")
public class MailServerModuleCapabilitiesAdapter
    extends MailServerModule
    implements Capabilities
{


    /**
     * Returns true if this module implements such capability
     * 
     */
    public boolean isCapableOf(Capability capability) {
        if (capability == Capability.LIFECYCLE_CAPABLE) {
            return true;
        }
        return false;
    }

}
