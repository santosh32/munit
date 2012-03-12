
package org.mule.munit.config;

import org.mule.api.Capabilities;
import org.mule.api.Capability;


/**
 * A <code>MClientCapabilitiesAdapter</code> is a wrapper around {@link org.mule.munit.MClient } that implements {@link org.mule.api.Capabilities} interface.
 * 
 */
public class MClientCapabilitiesAdapter
    extends org.mule.munit.MClient
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
