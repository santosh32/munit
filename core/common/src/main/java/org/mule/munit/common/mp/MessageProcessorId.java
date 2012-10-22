package org.mule.munit.common.mp;

/**
 * <p>The representation of a Message Processor Id</p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
public class MessageProcessorId {

    /**
     * <p>The Name of the message processor. For example, the name of jira:create-group is create-group</p>
     */
    private String name;

    /**
     * <p>The namespace of the message processor. For example, the namespace of jira:create-group is jira</p>
     */
    private String namespace;

    public MessageProcessorId(String name, String namespace) {
        this.name = name;
        this.namespace = namespace;
    }
    
    public String getFullName(){
        return namespace + ":" + name;
    }

    public String getName() {
        return name;
    }

    public String getNamespace() {
        return namespace;
    }

    @Override
    public boolean equals(Object o) {
        if ( o instanceof MessageProcessorId){
            MessageProcessorId mpId = (MessageProcessorId) o;
            
            return mpId.getName().equals(name) && mpId.getNamespace().equals(namespace);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return getFullName().hashCode();
    }
}
