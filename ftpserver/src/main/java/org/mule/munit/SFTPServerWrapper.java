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

import java.io.IOException;
import java.security.Security;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: fernandofederico
 * Date: 3/9/12
 * Time: 4:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class SFTPServerWrapper extends FTPServer{

    private SshServer sshd;


    @Override
    public void initialize(int port) {
        Security.addProvider(new BouncyCastleProvider());
        sshd = SshServer.setUpDefaultServer();
        sshd.setPort(port);
        FileKeyPairProvider fileKeyPairProvider = new FileKeyPairProvider(new String[]{getClass().getResource("/hostkey.pem").toString().replaceAll("file:","") });
        sshd.setKeyPairProvider(fileKeyPairProvider);
        SftpSubsystem.Factory factory = new SftpSubsystem.Factory();

        sshd.setSubsystemFactories(Arrays.<NamedFactory<Command>>asList(factory));
        sshd.setCommandFactory(new ScpCommandFactory());
        sshd.setShellFactory(new ProcessShellFactory());

        sshd.setPasswordAuthenticator(new MockPasswordAuthenticator());
    }

    @Override
    public void start() {
        try {
            sshd.start();
        } catch (IOException e) {
            throw new RuntimeException("Could not start the server", e);
        }
    }

    @Override
    public void stop() {
        try {
            sshd.stop();
        } catch (InterruptedException e) {
            throw new RuntimeException("Could not stop the server", e);
        }
        sshd = null;
    }


    public class MockPasswordAuthenticator implements PasswordAuthenticator
    {

        @Override
        public boolean authenticate(String s, String s1, ServerSession serverSession) {
            return true;
        }
    }


}
