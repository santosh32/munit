package org.mule.munit.common.connectors;

import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Constructor;

/**
 * Created by IntelliJ IDEA.
 * User: fernandofederico
 * Date: 11/4/12
 * Time: 6:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConnectorCallBackFactory {

    public static Class createClass( Class realMpClass, Object ... objects){
        try {

            Enhancer e = new Enhancer();
            e.setSuperclass(realMpClass);

            ConnectorCallBack callback = new ConnectorCallBack();

            e.setCallbackType(ConnectorCallBack.class);
            return e.createClass();

        } catch (Throwable e) {
            throw new Error("Could not mock the connectors", e);
        }
    }
    public Object create( Class realMpClass, Object ... objects){
        try {

            Enhancer e = new Enhancer();
            e.setSuperclass(realMpClass);

            ConnectorCallBack callback = new ConnectorCallBack();

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
}
