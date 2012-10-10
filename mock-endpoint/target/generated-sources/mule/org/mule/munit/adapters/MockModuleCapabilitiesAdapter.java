
package org.mule.munit.adapters;

import org.mule.api.Capabilities;
import org.mule.api.Capability;


/**
 * A <code>MockModuleCapabilitiesAdapter</code> is a wrapper around {@link org.mule.munit.MockModule } that implements {@link org.mule.api.Capabilities} interface.
 * 
 */
public class MockModuleCapabilitiesAdapter
    extends org.mule.munit.MockModule
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
