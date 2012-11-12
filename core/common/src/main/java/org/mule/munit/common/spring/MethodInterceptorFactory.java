package org.mule.munit.common.spring;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import java.lang.reflect.Constructor;

/**
 * <p>Abstract definition that creates an interceptor</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public abstract class MethodInterceptorFactory {

    /**
     * <p>The factory method to create connector/message processors beans</p>
     *
     * @param realMpClass
     *          <p>The class of the message processor/ connector</p>
     *
     * @param objects
     *          <p>Constructor arguments</p>
     *
     * @return
     *          <p>An @see #Enhancer of the message processor/connector</p>
     */
    public Object create( Class realMpClass, Object ... objects){
        try {

            Enhancer e = new Enhancer();
            e.setSuperclass(realMpClass);

            MethodInterceptor callback = createInterceptor();

            e.setCallback(callback);
            if ( objects != null ){

                Constructor[] constructors = realMpClass.getConstructors();
                for ( Constructor constructor : constructors ){
                    boolean matchConstructor = true;
                    Class[] parameterTypes = constructor.getParameterTypes();
                    for ( int j=0;j< parameterTypes.length;j++){
                        if ( j<objects.length) {
                            matchConstructor = matchConstructor && parameterTypes[j].isAssignableFrom(objects[j].getClass());
                        }
                        else{
                            matchConstructor=false;
                        }
                    }

                    if  ( matchConstructor ){
                        return e.create(constructor.getParameterTypes(), objects);
                    }
                }
                throw new Error("Could not mock the connectors");
            }
            return e.create();

        } catch (Throwable e) {
            throw new Error("Could not mock the connectors", e);
        }
    }

    protected abstract MethodInterceptor createInterceptor();
}
