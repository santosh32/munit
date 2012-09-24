package org.mule.munit;

import org.mockito.asm.tree.LocalVariableNode;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.*;

/**
 * Created by IntelliJ IDEA.
 * User: fernandofederico
 * Date: 9/23/12
 * Time: 3:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class MockedMethod{
    Method method;
    List<LocalVariableNode> variableNodes;

    public MockedMethod(Method method, List<LocalVariableNode> variableNodes) {
        this.method = method;
        this.variableNodes = variableNodes;
    }


    public Method getMethod() {
        return method;
    }

    public Map<Integer, Object> getParameters(Map<String, Object> expectedParams) {
        Map<Integer, Object> map = new HashMap<Integer, Object>();
        if ( expectedParams == null ){
            return map;
        }

        for (String param : expectedParams.keySet() ){
            boolean found = false;
            for ( LocalVariableNode variableNode : variableNodes){
                if ( param.equalsIgnoreCase(variableNode.name)){
                    map.put(variableNode.index-1, expectedParams.get(variableNode.name));
                    found = true;
                }
            }
            
            if ( !found ){
                throw new RuntimeException("Attribute "+param+" does not exists");
            }
        }
        

        return map;
    }

    public Object[] getAnyParameters(final Map<Integer, Object> parametersIndex) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        int parameterNumber = parameterTypes.length;
        Object[] parameters = new Object[parameterNumber];
        for ( int i=0; i<parameterNumber; i++)
        {
            if ( parametersIndex.get(i) != null ){
                parameters[i] = eq(parametersIndex.get(i));
            }
            else{
                if ( parameterTypes[i].isPrimitive() )
                {
                    if (parameterTypes[i].getName().equals("int") )
                        parameters[i] = anyInt();
                    if (parameterTypes[i].getName().equals("short") )
                        parameters[i] = anyShort();
                    if (parameterTypes[i].getName().equals("boolean") )
                        parameters[i] = anyBoolean();
                    if (parameterTypes[i].getName().equals("byte") )
                        parameters[i] = anyByte();
                    if (parameterTypes[i].getName().equals("long") )
                        parameters[i] = anyLong();
                    if (parameterTypes[i].getName().equals("float") )
                        parameters[i] = anyFloat();
                    if (parameterTypes[i].getName().equals("double") )
                        parameters[i] = anyDouble();
                    if (parameterTypes[i].getName().equals("char") )
                        parameters[i] = anyChar();
                }
                else{
                    parameters[i] = any(parameterTypes[i]);
                }
            }

        }
        return parameters;
    }

}
