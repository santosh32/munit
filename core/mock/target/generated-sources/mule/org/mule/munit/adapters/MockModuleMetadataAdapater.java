
package org.mule.munit.adapters;

import javax.annotation.Generated;
import org.mule.api.MetadataAware;
import org.mule.munit.MockModule;


/**
 * A <code>MockModuleMetadataAdapater</code> is a wrapper around {@link MockModule } that adds support for querying metadata about the extension.
 * 
 */
@Generated(value = "Mule DevKit Version 3.3.1", date = "2012-10-20T12:55:18-03:00", comments = "Build UNNAMED.1297.150f2c9")
public class MockModuleMetadataAdapater
    extends MockModuleCapabilitiesAdapter
    implements MetadataAware
{

    private final static String MODULE_NAME = "mock";
    private final static String MODULE_VERSION = "3.3.2-SNAPSHOT";
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
