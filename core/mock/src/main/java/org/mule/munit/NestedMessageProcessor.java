package org.mule.munit;

import org.mule.api.DefaultMuleException;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.NestedProcessor;
import org.mule.api.processor.MessageProcessor;

/**
 * <p>
 *     The NestedMessageProcessor is a wrapper of the devkit Nested processor.
 *
 *     The devkit nested processor lives inside Devkit jars, we don't want that dependency in the common modules. That's
 *     why it is wrapped.
 *
 *     This class should die in future versions of Devkit.
 * </p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class NestedMessageProcessor implements MessageProcessor {

    /**
     * <p>
     *     Wrapped nested processor
     * </p>
     */
    private NestedProcessor nestedProcessor;

    public NestedMessageProcessor(NestedProcessor nestedProcessor) {
        this.nestedProcessor = nestedProcessor;
    }

    /**
     * <p>
     *     The NestedMessageProcessor does not change the mule event. The nested processors that it wraps are only for
     *     munit assertions. it is not valid to modify the event. Munit is a testing framework, not aspect framework.
     * </p>
     *
     * @param event
     *      <p>The mule event to be processed</p>
     * @return
     *      <p>The same mule event as input</p>
     * @throws MuleException
     *      <p>If the mule nested processor fails</p>
     */
    @Override
    public MuleEvent process(MuleEvent event) throws MuleException {

        try {
            nestedProcessor.process();
        } catch (Exception e) {
            throw new DefaultMuleException(e);
        }

        return event;
    }
}
