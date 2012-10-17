package org.mule.munit.mp;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class MockMpManager {
    public static String ID = "_muleMockMpManager";

    private List<MpBehavior> behaviors = new ArrayList<MpBehavior>();
    
    
    public List<MpBehavior> getBehaviorsFor(String namespace, String name){
        List<MpBehavior> expected = new ArrayList<MpBehavior>();
        for ( MpBehavior behavior : behaviors){

            if (isExpected(namespace, name, behavior)){
                expected.add(behavior);
            }
        }
        return expected;
    }

    private boolean isExpected(String namespace, String name, MpBehavior behavior) {
        return behavior.getName().equals(name) && ( !StringUtils.isEmpty(namespace) && namespace.equals(behavior.getNamespace()) );
    }

    public void resetBehaviors(){
        behaviors.clear();
    }

    public synchronized void addBehavior(MpBehavior behavior) {
        behaviors.add(behavior);
    }
}
