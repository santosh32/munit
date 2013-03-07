package org.mule.munit;

import org.mule.api.annotations.Module;
import org.mule.api.annotations.Processor;
import org.mule.munit.MailServer;

import javax.annotation.PostConstruct;
import javax.mail.internet.MimeMessage;
import java.util.List;

/**
 * <p>Module for mail integration testing.</p>
 *
 * @author Federico, Fernando
 */
@Module(name="mail-server", schemaVersion="1.0")
public class MailServerModule
{

    private MailServer mailServer;


    @PostConstruct
    public void createServer(){
        mailServer = new MailServer();
    }

    /**
     * <p>Starts the mail server</p>
     *
     * {@sample.xml ../../../doc/MailServer-connector.xml.sample mail-server:start}
     */
    @Processor
    public void startServer()  {
        mailServer.start();
    }

    /**
     * <p>Stops the mail server</p>
     *
     * {@sample.xml ../../../doc/MailServer-connector.xml.sample mail-server:stop}
     */
    @Processor
    public void stopServer()  {
        try{
            mailServer.stop();
        }
        catch (Throwable e){
            // Do nothing
        }
    }

    /**
     * <p>Gets the messages from the server</p>
     *
     * {@sample.xml ../../../doc/MailServer-connector.xml.sample mail-server:getMessages}
     *
     * @return The list of MimeMessages
     */
    @Processor
    public List<MimeMessage> getReceivedMessages()  {
        return mailServer.getReceivedMessages();
    }

}
