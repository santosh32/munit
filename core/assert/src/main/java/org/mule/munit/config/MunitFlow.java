package org.mule.munit.config;

import org.mule.api.MuleContext;
import org.mule.construct.Flow;


/**
 * <p>
 *     Generic Munit Flow
 * </p>
 *
 * @author Federico, Fernando
 * @since 3.3.2
 */
public class MunitFlow extends Flow {

    /**
     * <p>The munit test description</p>
     */
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
