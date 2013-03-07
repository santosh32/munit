package org.mule.munit;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.*;
import org.apache.ftpserver.listener.ListenerFactory;

/**
 * <p>Wrapper of the FTP Server</p>
 *
 * @author Federico, Fernando
 */
public class FTPServer extends Server {
    private FtpServer server;

    public static Server instance(int port){
        FTPServer ftpServer = new FTPServer();
        ftpServer.initialize(port);
        return ftpServer;
    }

    @Override
    void initialize(int port) {
        FtpServerFactory serverFactory = new FtpServerFactory();

        ListenerFactory factory = new ListenerFactory();

        factory.setPort(port);
        serverFactory.setUserManager(new MockUserManager());
        serverFactory.addListener("default", factory.createListener());

        server = serverFactory.createServer();
    }

    @Override
    void start() {
        try {
            server.start();
        } catch (FtpException e) {
            throw new RuntimeException("Could not start FTP server", e);
        }
    }

    @Override
    void stop() {
        server.stop();
    }
}
