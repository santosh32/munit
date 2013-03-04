package org.mule.munit.examples.jira;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.module.google.task.model.TaskList;
import org.mule.munit.runner.functional.FunctionalMunitSuite;

public class JiraMockingTest extends FunctionalMunitSuite{

	@Override
	protected String getConfigResources() {
		
		return "jira-mock-example.xml";
	}
	
	@Test
	public void testThatIfJiraDoesntFailOKIsLogged() throws MuleException, Exception{
		whenMessageProcessor("create-issue").ofNamespace("jira").thenReturn(muleMessageWithPayload("OK"));
		
		MuleEvent resultEvent = runFlow("jira-mock-exampleFlow", testEvent(createTaskList()));
		
		verifyCallOfMessageProcessor("create-issue").ofNamespace("jira")
		.withAttributes(expectedParameters("task2")).times(1);
		
		verifyCallOfMessageProcessor("create-issue").ofNamespace("jira")
		.withAttributes(expectedParameters("task1")).times(1);
		
		verifyCallOfMessageProcessor("logger")
		.withAttributes(expectedLoggerParameters("Everything OK")).times(2);
	}

	private HashMap<String, Object> expectedLoggerParameters(String value) {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("message", value);
		return hashMap;
	}
	
	private HashMap<String, Object> expectedParameters(String value) {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("summary", value);
		return hashMap;
	}
	
	
	public List<TaskList> createTaskList(){
        List<TaskList> lists = new ArrayList<TaskList>();
        TaskList task1 = new TaskList();
        task1.setTitle("task1");
        lists.add(task1);

        TaskList task2 = new TaskList();
        task2.setTitle("task2");
        lists.add(task2);

		return lists;
		
	}

}
