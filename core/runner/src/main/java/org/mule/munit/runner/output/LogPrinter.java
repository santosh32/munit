package org.mule.munit.runner.output;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * <p>Prints the output in a log file</p>
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class LogPrinter implements OutputPrinter{

    private Log log;

    public LogPrinter() {
        this.log = LogFactory.getLog(LogPrinter.class);

    }

    @Override
    public void print(String message) {
        log.info(message);
    }
}
