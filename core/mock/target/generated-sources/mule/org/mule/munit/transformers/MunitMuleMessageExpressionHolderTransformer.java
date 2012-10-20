
package org.mule.munit.transformers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.endpoint.ImmutableEndpoint;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.transformer.DataType;
import org.mule.api.transformer.DiscoverableTransformer;
import org.mule.api.transformer.MessageTransformer;
import org.mule.api.transformer.TransformerException;
import org.mule.api.transformer.TransformerMessagingException;
import org.mule.config.i18n.CoreMessages;
import org.mule.munit.MunitMuleMessage;
import org.mule.munit.holders.MunitMuleMessageExpressionHolder;
import org.mule.munit.processors.AbstractExpressionEvaluator;
import org.mule.transformer.types.DataTypeFactory;

@Generated(value = "Mule DevKit Version 3.3.1", date = "2012-10-20T04:33:25-03:00", comments = "Build UNNAMED.1297.150f2c9")
public class MunitMuleMessageExpressionHolderTransformer
    extends AbstractExpressionEvaluator
    implements DiscoverableTransformer, MessageTransformer
{

    private int weighting = DiscoverableTransformer.DEFAULT_PRIORITY_WEIGHTING;
    private ImmutableEndpoint endpoint;
    private MuleContext muleContext;
    private String name;

    public int getPriorityWeighting() {
        return weighting;
    }

    public void setPriorityWeighting(int weighting) {
        this.weighting = weighting;
    }

    public boolean isSourceTypeSupported(Class<?> aClass) {
        return (aClass == MunitMuleMessageExpressionHolder.class);
    }

    public boolean isSourceDataTypeSupported(DataType<?> dataType) {
        return (dataType.getType() == MunitMuleMessageExpressionHolder.class);
    }

    public List<Class<?>> getSourceTypes() {
        return Arrays.asList(new Class<?> [] {MunitMuleMessageExpressionHolder.class });
    }

    public List<DataType<?>> getSourceDataTypes() {
        return Arrays.asList(new DataType<?> [] {DataTypeFactory.create(MunitMuleMessageExpressionHolder.class)});
    }

    public boolean isAcceptNull() {
        return false;
    }

    public boolean isIgnoreBadInput() {
        return false;
    }

    public Object transform(Object src)
        throws TransformerException
    {
        throw new UnsupportedOperationException();
    }

    public Object transform(Object src, String encoding)
        throws TransformerException
    {
        throw new UnsupportedOperationException();
    }

    public void setReturnClass(Class<?> theClass) {
        throw new UnsupportedOperationException();
    }

    public Class<?> getReturnClass() {
        return MunitMuleMessage.class;
    }

    public void setReturnDataType(DataType<?> type) {
        throw new UnsupportedOperationException();
    }

    public DataType<?> getReturnDataType() {
        return DataTypeFactory.create(MunitMuleMessage.class);
    }

    public void setEndpoint(ImmutableEndpoint ep) {
        endpoint = ep;
    }

    public ImmutableEndpoint getEndpoint() {
        return endpoint;
    }

    public void dispose() {
    }

    public void initialise()
        throws InitialisationException
    {
    }

    public void setMuleContext(MuleContext context) {
        muleContext = context;
    }

    public void setName(String newName) {
        name = newName;
    }

    public String getName() {
        return name;
    }

    public Object transform(Object src, MuleEvent event)
        throws TransformerMessagingException
    {
        return transform(src, null, event);
    }

    public Object transform(Object src, String encoding, MuleEvent event)
        throws TransformerMessagingException
    {
        if (src == null) {
            return null;
        }
        MunitMuleMessageExpressionHolder holder = ((MunitMuleMessageExpressionHolder) src);
        MunitMuleMessage result = new MunitMuleMessage();
        try {
            final Object _transformedPayload = ((Object) evaluateAndTransform(this.muleContext, event, MunitMuleMessageExpressionHolder.class.getDeclaredField("_payloadType").getGenericType(), null, holder.getPayload()));
            result.setPayload(_transformedPayload);
            final Map<String, Object> _transformedInvocationProperties = ((Map<String, Object> ) evaluateAndTransform(this.muleContext, event, MunitMuleMessageExpressionHolder.class.getDeclaredField("_invocationPropertiesType").getGenericType(), null, holder.getInvocationProperties()));
            result.setInvocationProperties(_transformedInvocationProperties);
            final Map<String, Object> _transformedInboundProperties = ((Map<String, Object> ) evaluateAndTransform(this.muleContext, event, MunitMuleMessageExpressionHolder.class.getDeclaredField("_inboundPropertiesType").getGenericType(), null, holder.getInboundProperties()));
            result.setInboundProperties(_transformedInboundProperties);
            final Map<String, Object> _transformedSessionProperties = ((Map<String, Object> ) evaluateAndTransform(this.muleContext, event, MunitMuleMessageExpressionHolder.class.getDeclaredField("_sessionPropertiesType").getGenericType(), null, holder.getSessionProperties()));
            result.setSessionProperties(_transformedSessionProperties);
            final Map<String, Object> _transformedOutboundProperties = ((Map<String, Object> ) evaluateAndTransform(this.muleContext, event, MunitMuleMessageExpressionHolder.class.getDeclaredField("_outboundPropertiesType").getGenericType(), null, holder.getOutboundProperties()));
            result.setOutboundProperties(_transformedOutboundProperties);
        } catch (NoSuchFieldException e) {
            throw new TransformerMessagingException(CoreMessages.createStaticMessage("internal error"), event, this, e);
        } catch (TransformerException e) {
            throw new TransformerMessagingException(e.getI18nMessage(), event, this, e);
        }
        return result;
    }

    public MuleEvent process(MuleEvent event) {
        return null;
    }

    public String getMimeType() {
        return null;
    }

    public String getEncoding() {
        return null;
    }

    public MuleContext getMuleContext() {
        return muleContext;
    }

}
