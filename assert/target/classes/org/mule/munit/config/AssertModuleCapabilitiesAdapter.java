
package org.mule.munit.config;

import org.mule.api.Capabilities;
import org.mule.api.Capability;


/**
 * A <code>AssertModuleCapabilitiesAdapter</code> is a wrapper around {@link org.mule.munit.AssertModule } that implements {@link org.mule.api.Capabilities} interface.
 * 
 */
public class AssertModuleCapabilitiesAdapter
    extends org.mule.munit.AssertModule
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
