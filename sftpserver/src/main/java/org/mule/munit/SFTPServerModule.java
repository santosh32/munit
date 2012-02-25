
package org.mule.munit;

import org.apache.sshd.SshServer;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.common.keyprovider.FileKeyPairProvider;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.PasswordAuthenticator;
import org.apache.sshd.server.command.ScpCommandFactory;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.sftp.SftpSubsystem;
import org.apache.sshd.server.shell.ProcessShellFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.Module;
import org.mule.api.annotations.Processor;
import org.mule.api.lifecycle.Initialisable;
import org.mule.api.lifecycle.InitialisationException;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.security.Security;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;

/**
 * SFTP server for Integration tests
 *
 * @author Federico, Fernando
 */
@Module(name="sftpserver", schemaVersion="1.0")
public class SFTPServerModule
{
    /**
     * port
     */
    @Configurable
    private int port;

    /**
     * hostKey
     */
    @Configurable
    private String hostKey;

    private SshServer sshd;


    /**
     * start the server
     *
     * {@sample.xml ../../../doc/SFTPServer-connector.xml.sample sftpserver:startServer}
     *
     */
    @Processor
    public void startServer()
    {
        try {
            sshd.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * check if file exists
     *
     * {@sample.xml ../../../doc/SFTPServer-connector.xml.sample sftpserver:containsFiles}
     *
     * @param file file
     * @param path path
     */
    @Processor
    public void containsFiles(String file, String path) {
        assertTrue(getNoodlesFilesAfterSftp(file, path).length > 0);
    }

    private File[] getNoodlesFilesAfterSftp(final String fileName,String path) {
        return new File(path).listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File file, String name) {
                return name.startsWith(fileName);
            }
        });
    }

    /**
     * stop
     *
     * {@sample.xml ../../../doc/SFTPServer-connector.xml.sample sftpserver:stopServer}
     *
     */
    @Processor
    public void stopServer()
    {
        try {
            sshd.stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sshd = null;
    }


    public void setPort(int port) {
        this.port = port;
    }

    public void setHostKey(String hostKey) {
        this.hostKey = hostKey;
    }

    @PostConstruct
    public void initialise() throws InitialisationException {
        Security.addProvider(new BouncyCastleProvider());
        sshd = SshServer.setUpDefaultServer();
        sshd.setPort(port);
        FileKeyPairProvider fileKeyPairProvider = new FileKeyPairProvider(new String[]{hostKey});

        sshd.setKeyPairProvider(fileKeyPairProvider);
        SftpSubsystem.Factory factory = new SftpSubsystem.Factory();

        sshd.setSubsystemFactories(Arrays.<NamedFactory<Command>>asList(factory));
        sshd.setCommandFactory(new ScpCommandFactory());
        sshd.setShellFactory(new ProcessShellFactory());

        sshd.setPasswordAuthenticator(PasswordAuthenticator());
    }

    private PasswordAuthenticator PasswordAuthenticator() {
        return new MyPasswordAuthenticator();
    }

    public class MyPasswordAuthenticator implements PasswordAuthenticator
    {

        @Override
        public boolean authenticate(String s, String s1, ServerSession serverSession) {
            return true;
        }
    }
}
