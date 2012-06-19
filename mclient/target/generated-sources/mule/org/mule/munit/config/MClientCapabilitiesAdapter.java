
package org.mule.munit.config;

import org.mule.api.Capabilities;
import org.mule.api.Capability;
import org.mule.munit.MClient;


/**
 * A <code>MClientCapabilitiesAdapter</code> is a wrapper around {@link MClient } that implements {@link org.mule.api.Capabilities} interface.
 * 
 */
public class MClientCapabilitiesAdapter
    extends MClient
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
