package org.mule.munit.config;

import org.mule.api.MuleContext;

/**
 * <p>
 *     Before Test flow
 * </p>
 *
 * @author Federico, Fernando
 * @since 3.3.2
 */
public class MunitBeforeTest extends MunitFlow{
    public MunitBeforeTest(String name, MuleContext muleContext) {
        super(name, muleContext);
    }
}
