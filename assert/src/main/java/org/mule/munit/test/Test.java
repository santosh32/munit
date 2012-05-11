package org.mule.munit.test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.mule.MessageExchangePattern;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.munit.config.MunitFlow;
import org.mule.munit.test.result.TestResult;
import org.mule.munit.test.result.MunitResult;
import org.mule.munit.test.result.notification.Notification;
import org.mule.tck.MuleTestUtils;

public class Test {

	private List<MunitFlow> before;
	private List<MunitFlow> after;
	private MunitFlow flow;

	private static String stack2string(Throwable e) {
		try {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			return sw.toString();
		} catch (Exception e2) {
			return "";
		}
	}
	
	public Test(List<MunitFlow> before, MunitFlow flowConstruct,
			List<MunitFlow> after) {
		this.before = before;
		this.after = after;
		this.flow = flowConstruct;

	}

	public String getName() {
		return flow.getName();
	}

	public TestResult run() {

		TestResult result = new TestResult(getName());
		MuleEvent event = muleEvent();

		try {
			run(event, before);
			showDescription();

			flow.process(event);
		} catch (AssertionError t) {
			result.setFailure(buildNotifcationFrom(t));
		} catch (MuleException e) {
			result.setError(buildNotifcationFrom(e));
		} finally {
			runAfter(result, event);
		}

		return result;

	}



	private Notification buildNotifcationFrom(Throwable t) {
		return new Notification(t.getMessage(), stack2string(t));
	}

	private void runAfter(TestResult result, MuleEvent event) {
		try {
			run(event, after);
		} catch (MuleException e) {
			result.setError(buildNotifcationFrom(e));
		}
	}

	private void run(MuleEvent event, List<MunitFlow> flows)
			throws MuleException {
		if (flows != null) {
			for (MunitFlow flow : flows) {
				System.out.printf(flow.getDescription() + "%n");
				flow.process(event);
			}
		}
	}

	private void showDescription() {
		System.out.printf("%nDescription:%n************%n"
				+ flow.getDescription().replaceAll("\\.", "\\.%n") + "%n");
	}

	private MuleEvent muleEvent() {
		try {
			return MuleTestUtils.getTestEvent(null,
					MessageExchangePattern.REQUEST_RESPONSE,
					flow.getMuleContext());
		} catch (Exception e) {
			return null;
		}
	}

}
