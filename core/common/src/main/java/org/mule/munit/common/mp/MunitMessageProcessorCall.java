package org.mule.munit.common.mp;

import org.mule.modules.interceptor.processors.MessageProcessorCall;
import org.mule.modules.interceptor.processors.MessageProcessorId;

/**
 * <p>
 *     Representation of the message processor call for Munit
 * </p>
 *
 * @author Federico, Fernando
 * @since 3.4
 */
public class MunitMessageProcessorCall extends MessageProcessorCall {

    private String fileName;
    private String lineNumber;

    public MunitMessageProcessorCall(MessageProcessorId messageProcessorId) {
        super(messageProcessorId);
    }


    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getLineNumber() {
        return lineNumber;
    }
}
