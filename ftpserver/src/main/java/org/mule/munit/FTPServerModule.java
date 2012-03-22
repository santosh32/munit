
package org.mule.munit;

import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.Module;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.Optional;
import org.mule.api.lifecycle.InitialisationException;

import javax.annotation.PostConstruct;

/**
 * <p>FTP server for Integration tests.</p>
 *
 * <p>With this module you can start a FTP server on your local machine </p>
 *
 * @author Federico, Fernando
 */
@Module(name="ftpserver", schemaVersion="1.0")
public class FTPServerModule
{
    /**
     * <p>FTP server port.</p>
     */
    @Configurable
    private int port;

    /**
     * <p>Defines if is FTP over ssh.</p>
     */
    @Configurable
    @Optional
    @Default("false")
    private boolean secure;

    private FTPServer server;


    /**
     * <p>Starts the server</p>
     *
     * {@sample.xml ../../../doc/SFTPServer-connector.xml.sample sftpserver:startServer}
     *
     */
    @Processor
    public void startServer()
    {
        server.start();
    }

    /**
     * <p>check if a file exists.</p
     *
     * {@sample.xml ../../../doc/SFTPServer-connector.xml.sample sftpserver:containsFiles}
     *
     * @param file file
     * @param path path
     */
    @Processor
    public void containsFiles(String file, String path) {
        server.containsFiles(file, path);
    }

    /**
     * <p>Stops the server</p>
     *
     * {@sample.xml ../../../doc/SFTPServer-connector.xml.sample sftpserver:stopServer}
     *
     */
    @Processor
    public void stopServer()
    {
       try
       {
        server.stop();
       }catch(Throwable t)
       {

       }
    }


    /**
     * <p>Remove created files</p>
     *
     * {@sample.xml ../../../doc/SFTPServer-connector.xml.sample sftpserver:remove}
     *
     * @param path Path to be removed.
     *
     */
    @Processor
    public void remove(String path)
    {
        server.remove(path);
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    @PostConstruct
    public void initialise() throws InitialisationException {
        if ( secure )
            server = new SFTPServerWrapper();
        else
            server = new FTPServerWrapper();


        server.initialize(port);

    }



}
