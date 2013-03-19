package org.mule.munit.mel.utils;

import org.junit.Test;
import org.mule.api.MuleContext;
import org.mule.api.registry.MuleRegistry;
import org.mule.module.scripting.component.Scriptable;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class FlowResultFunctionTest {

    public static final String SCRIPT_NAME = "scriptName";
    public static final String SCRIPT_RESULT = "scriptResult";
    private MuleContext muleContext = mock(MuleContext.class);
    private MuleRegistry muleRegistry = mock(MuleRegistry.class);
    private Scriptable script = mock(Scriptable.class);
    private ScriptEngine scriptEngine = mock(ScriptEngine.class);
    private Bindings bindings = mock(Bindings.class);

    @Test(expected = IllegalArgumentException.class)
    public void callWithNull(){
        new FlowResultFunction(muleContext).call(null,null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void callWithEmpty(){
        new FlowResultFunction(muleContext).call(new Object[]{},null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void callWithNotString(){
        new FlowResultFunction(muleContext).call(new Object[]{new Object()},null);
    }

    @Test(expected = NullPointerException.class)
    public void callWithScriptNameThatDoesNotExist(){
        when(muleContext.getRegistry()).thenReturn(muleRegistry);
        when(muleRegistry.lookupObject(SCRIPT_NAME)).thenReturn(null);
        new FlowResultFunction(muleContext).call(new Object[]{SCRIPT_NAME}, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void callWithScriptNameThatExistsButIsNotAnScript(){
        when(muleContext.getRegistry()).thenReturn(muleRegistry);
        when(muleRegistry.lookupObject(SCRIPT_NAME)).thenReturn(new Object());
        new FlowResultFunction(muleContext).call(new Object[]{SCRIPT_NAME}, null);
    }


    @Test
    public void callWithScriptNameThatExists() throws ScriptException {
        when(muleContext.getRegistry()).thenReturn(muleRegistry);
        when(muleRegistry.lookupObject(SCRIPT_NAME)).thenReturn(script);
        when(script.getScriptEngine()).thenReturn(scriptEngine);
        when(scriptEngine.createBindings()).thenReturn(bindings);
        when(script.runScript(bindings)).thenReturn(SCRIPT_RESULT);
       
        assertEquals(SCRIPT_RESULT, new FlowResultFunction(muleContext).call(new Object[]{SCRIPT_NAME}, null));
    }

    @Test(expected = RuntimeException.class)
    public void callAndScriptFails() throws ScriptException {
        when(muleContext.getRegistry()).thenReturn(muleRegistry);
        when(muleRegistry.lookupObject(SCRIPT_NAME)).thenReturn(script);
        when(script.getScriptEngine()).thenReturn(scriptEngine);
        when(scriptEngine.createBindings()).thenReturn(bindings);
        when(script.runScript(bindings)).thenThrow(new ScriptException("error"));

        new FlowResultFunction(muleContext).call(new Object[]{SCRIPT_NAME}, null);
    }



}
