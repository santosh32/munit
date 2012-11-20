package org.mule.munit;


import org.mule.api.annotations.Configurable;

import java.util.Map;

/**
 * <p>Pojo Simplified Representation of a Mime Message</p>
 *
 * @author Federico, Fernando
 */
public class MimeMessage {


    /**
     * <p>Message content</p>
     */
    @Configurable
    public Object withContent;

    /**
     * <p>Mime message headers</p>
     */
    @Configurable
    public Map<String, String> withHeaders;

    public Object getWithContent() {
        return withContent;
    }

    public void setWithContent(Object withContent) {
        this.withContent = withContent;
    }

    public Map<String, String> getWithHeaders() {
        return withHeaders;
    }

    public void setWithHeaders(Map<String, String> withHeaders) {
        this.withHeaders = withHeaders;
    }
}
