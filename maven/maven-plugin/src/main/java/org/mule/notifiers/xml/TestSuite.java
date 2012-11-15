package org.mule.notifiers.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.mule.notifiers.xml.Property;
import org.mule.notifiers.xml.TestCase;

import java.util.ArrayList;
import java.util.List;


@XStreamAlias(value = "testsuite")
public class TestSuite {

    @XStreamAsAttribute private String name;
    @XStreamAsAttribute private float time;
    @XStreamAsAttribute private int failures;
    @XStreamAsAttribute private int errors;
    @XStreamAsAttribute private int skipped;
    @XStreamAsAttribute private int tests;
    private List<Property> properties;
    @XStreamImplicit private List<TestCase> testcases = new ArrayList<TestCase>();

    public TestSuite(List<Property> properties, String name) {
        this.properties = properties;
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public int getFailures() {
        return failures;
    }

    public void setFailures(int failures) {
        this.failures = failures;
    }

    public int getErrors() {
        return errors;
    }

    public void setErrors(int errors) {
        this.errors = errors;
    }

    public int getSkipped() {
        return skipped;
    }

    public void setSkipped(int skipped) {
        this.skipped = skipped;
    }

    public int getTests() {
        return tests;
    }

    public void setTests(int tests) {
        this.tests = tests;
    }

    public List<TestCase> getTestcases() {
        return testcases;
    }

    public void setTestcases(List<TestCase> testcases) {
        this.testcases = testcases;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void add(TestCase testCase) {
        this.testcases.add(testCase);
    }
}
