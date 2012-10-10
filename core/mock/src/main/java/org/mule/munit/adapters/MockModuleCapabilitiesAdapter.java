package org.mule.munit.adapters;

import org.mule.api.Capabilities;
import org.mule.api.Capability;
import org.mule.munit.MockModule;

import javax.annotation.Generated;


/**
 * A <code>MockModuleCapabilitiesAdapter</code> is a wrapper around {@link org.mule.munit.MockModule } that implements {@link org.mule.api.Capabilities} interface.
 * 
 */
@Generated(value = "Mule DevKit Version 3.3.1", date = "2012-09-24T08:53:54-03:00", comments = "Build UNNAMED.1297.150f2c9")
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
