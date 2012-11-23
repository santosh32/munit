
package org.mule.munit.adapters;

import javax.annotation.Generated;
import org.mule.api.MetadataAware;
import org.mule.munit.FTPServerModule;


/**
 * A <code>FTPServerModuleMetadataAdapater</code> is a wrapper around {@link FTPServerModule } that adds support for querying metadata about the extension.
 * 
 */
@Generated(value = "Mule DevKit Version 3.3.1", date = "2012-11-23T03:25:05-03:00", comments = "Build 3.3.1.1298.3ae82a7")
public class FTPServerModuleMetadataAdapater
    extends FTPServerModuleCapabilitiesAdapter
    implements MetadataAware
{

    private final static String MODULE_NAME = "ftpserver";
    private final static String MODULE_VERSION = "3.3.2-M2-SNAPSHOT";
    private final static String DEVKIT_VERSION = "3.3.1";
    private final static String DEVKIT_BUILD = "3.3.1.1298.3ae82a7";

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
