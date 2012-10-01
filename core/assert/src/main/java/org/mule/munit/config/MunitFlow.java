package org.mule.munit.config;

import org.mule.api.MuleContext;
import org.mule.construct.Flow;

/**
 * Created by IntelliJ IDEA.
 * User: fernandofederico
 * Date: 3/27/12
 * Time: 12:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class MunitFlow extends Flow {
    private String description;

    public MunitFlow(String name, MuleContext muleContext) {
        super(name, muleContext);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
