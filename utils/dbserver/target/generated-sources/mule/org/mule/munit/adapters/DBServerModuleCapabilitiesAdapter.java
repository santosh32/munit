
package org.mule.munit.adapters;

import javax.annotation.Generated;
import org.mule.api.Capabilities;
import org.mule.api.Capability;
import org.mule.munit.DBServerModule;


/**
 * A <code>DBServerModuleCapabilitiesAdapter</code> is a wrapper around {@link DBServerModule } that implements {@link org.mule.api.Capabilities} interface.
 * 
 */
@Generated(value = "Mule DevKit Version 3.3.1", date = "2012-11-23T12:55:32-03:00", comments = "Build 3.3.1.1298.3ae82a7")
public class DBServerModuleCapabilitiesAdapter
    extends DBServerModule
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
