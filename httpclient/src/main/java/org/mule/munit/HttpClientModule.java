package org.mule.munit;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.mule.api.MuleContext;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.Module;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.param.Optional;
import org.mule.api.context.MuleContextAware;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Http Client for integration Testing
 *
 * @author Federico, Fernando
 */
@Module(name="httpclient", schemaVersion="1.0")
public class HttpClientModule
{
    /**
     * Http Method
     */
    @Configurable
    @Optional
    private String method;


    /**
     * Execute a HTTP call.
     *
     * {@sample.xml ../../../doc/HttpClient-connector.xml.sample httpclient:execute}
     *
     * @param parameters Http parameters
     * @param address http address
     * @return http response
     */
    @Processor
    public String execute(String address, Map<String,String> parameters)
    {
        org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient();

        GetMethod request = new GetMethod(address);
        NameValuePair params[] = createParameters(parameters);
        request.setQueryString(params);

        try {
            int status = client.executeMethod(request);

            if (status != HttpStatus.SC_OK) {
                System.err.println("Error\t" + request.getStatusCode() + "\t" +
                        request.getStatusText() + "\t" + request.getStatusLine());
            } else {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(request.getResponseBodyAsStream(), request.getResponseCharSet()));
                String line   = reader.readLine();
                StringBuffer buffer = new StringBuffer();
                while (line != null) {


                    buffer.append(line);
                    line = reader.readLine();
                }

                return buffer.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private NameValuePair[] createParameters(Map<String, String> parameters) {

        NameValuePair[] params = new NameValuePair[parameters.size()];
        int i=0;
        for ( String key : parameters.keySet())
        {
            params[i] = new NameValuePair(key, parameters.get(key));
            i++;
        }

        return params;
    }


    public void setMethod(String method) {
        this.method = method;
    }
}
