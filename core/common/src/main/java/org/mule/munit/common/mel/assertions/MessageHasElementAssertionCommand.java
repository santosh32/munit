package org.mule.munit.common.mel.assertions;

import org.mule.api.MuleMessage;


public interface MessageHasElementAssertionCommand {
    boolean messageHas(String param, MuleMessage muleMessage);
}
