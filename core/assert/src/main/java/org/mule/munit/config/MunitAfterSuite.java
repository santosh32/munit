package org.mule.munit.config;

import org.mule.api.MuleContext;

/**
 * <p>
 *     After Suite Flow
 * </p>
 *
 * @author Federico, Fernando
 * @since 3.3.2
 */
public class MunitAfterSuite extends MunitFlow{
    public MunitAfterSuite(String name, MuleContext muleContext) {
        super(name, muleContext);
    }
}
