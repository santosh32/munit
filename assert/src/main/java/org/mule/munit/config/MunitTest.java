package org.mule.munit.config;

import org.mule.api.MuleContext;

/**
 * Created by IntelliJ IDEA.
 * User: fernandofederico
 * Date: 4/9/12
 * Time: 11:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class MunitTest extends MunitFlow{
    public MunitTest(String name, MuleContext muleContext) {
        super(name, muleContext);
    }
}
