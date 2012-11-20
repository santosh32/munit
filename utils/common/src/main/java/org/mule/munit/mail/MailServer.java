package org.mule.munit.mail;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;

import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.List;

/**
 * <p>Representation of a mail server for Munit tests</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MailServer {
    /**
     * <p>This class is just a wrapper of Green Mail, so here is the instance of GreenMail server</p>
     */
    private GreenMail mailServer;

    public MailServer() {
        mailServer = new GreenMail(ServerSetupTest.ALL);
    }

    public void start(){
        mailServer.start();
    }

    public void stop(){
        try{
            mailServer.stop();
        }
        catch (Throwable e){
            // Do nothing
        }
    }

    public List<MimeMessage> getReceivedMessages(){
        return Arrays.asList(mailServer.getReceivedMessages());
    }

}
