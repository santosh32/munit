
package org.mule.munit.adapters;

import javax.annotation.Generated;
import org.mule.api.MetadataAware;
import org.mule.munit.MClient;


/**
 * A <code>MClientMetadataAdapater</code> is a wrapper around {@link MClient } that adds support for querying metadata about the extension.
 * 
 */
@Generated(value = "Mule DevKit Version 3.3.1", date = "2012-09-24T08:54:01-03:00", comments = "Build UNNAMED.1297.150f2c9")
public class MClientMetadataAdapater
    extends MClientCapabilitiesAdapter
    implements MetadataAware
{

    private final static String MODULE_NAME = "mclient";
    private final static String MODULE_VERSION = "1.2-SNAPSHOT";
    private final static String DEVKIT_VERSION = "3.3.1";
    private final static String DEVKIT_BUILD = "UNNAMED.1297.150f2c9";

    public String getModuleName() {
        return MODULE_NAME;
    }

    public String getModuleVersion() {
        return MODULE_VERSION;
    }

    public String getDevkitVersion() {
        return DEVKIT_VERSION;
    }

    public String getDevkitBuild() {
        return DEVKIT_BUILD;
    }

}
