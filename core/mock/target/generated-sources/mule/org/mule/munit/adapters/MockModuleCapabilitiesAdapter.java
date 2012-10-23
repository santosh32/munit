
package org.mule.munit.adapters;

import javax.annotation.Generated;
import org.mule.api.Capabilities;
import org.mule.api.Capability;
import org.mule.munit.MockModule;


/**
 * A <code>MockModuleCapabilitiesAdapter</code> is a wrapper around {@link MockModule } that implements {@link org.mule.api.Capabilities} interface.
 * 
 */
@Generated(value = "Mule DevKit Version 3.3.1", date = "2012-10-23T01:00:50-03:00", comments = "Build UNNAMED.1297.150f2c9")
public class MockModuleCapabilitiesAdapter
    extends MockModule
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
