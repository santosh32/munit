package org.mule.assertions;

import org.mule.api.MuleEvent;
import org.mule.munit.MunitAssertion;


public class CustomAssertion implements MunitAssertion{
    @Override
    public MuleEvent execute(MuleEvent muleEvent) throws AssertionError {
        if ( !muleEvent.getMessage().getPayload().equals("Hello World") ){
            throw new AssertionError("Error the payload is incorrect");
        }

        return muleEvent;
    }
}
