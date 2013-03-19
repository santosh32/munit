package org.mule.munit.common.mocking;

import org.mule.api.MuleEvent;
import org.mule.api.MuleException;

/**
 * <p>
 *     if you want to spy something this is the method that you need to implement
 * </p>
 *
 * @author Federico, Fernando
 * @since 3.3.2
 */
public interface SpyProcess {

    /**
     * <p>
     *     Executes code in the spying process
     * </p>
     * @param event
     *      <p>
     *          The {@link MuleEvent}
     *      </p>
     * @throws MuleException
     *      <p>
     *          In case of spy failure
     *      </p>
     */
    void spy(MuleEvent event) throws MuleException;
}
