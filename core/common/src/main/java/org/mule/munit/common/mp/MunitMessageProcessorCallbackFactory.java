package org.mule.munit.common.mp;

import net.sf.cglib.proxy.Enhancer;

import java.util.Map;


/**
 * <p>This is the Message processor Wrapper.</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MunitMessageProcessorCallbackFactory {



    public Object create(Class realMpClass, MessageProcessorId id, Map<String,String> attributes){
        try {

            Enhancer e = new Enhancer();
            e.setSuperclass(realMpClass);

            MunitMessageProcessorCallback callback = new MunitMessageProcessorCallback();
            callback.setId(id);
            callback.setAttributes(attributes);
            e.setCallback(callback);
            return e.create();

        } catch (Throwable e) {
            throw new Error("The message processor " + id.getFullName() + " could not be mocked", e);
        }
    }

}
