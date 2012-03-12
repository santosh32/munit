
package org.mule.munit.config;

import org.mule.api.Capabilities;
import org.mule.api.Capability;


/**
 * A <code>MPMockModuleCapabilitiesAdapter</code> is a wrapper around {@link org.mule.munit.MPMockModule } that implements {@link org.mule.api.Capabilities} interface.
 * 
 */
public class MPMockModuleCapabilitiesAdapter
    extends org.mule.munit.MPMockModule
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
