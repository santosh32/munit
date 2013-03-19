package org.mule.munit.config;

import org.mule.api.MuleContext;

/**
 * <p>
 *     After test flow
 * </p>
 *
 * @author Federico, Fernando
 * @since 3.3.2
 */
public class MunitAfterTest extends MunitFlow{
    public MunitAfterTest(String name, MuleContext muleContext) {
        super(name, muleContext);
    }
}
