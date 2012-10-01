package org.mule;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.mule.munit.test.MunitTestRunner;
import org.mule.munit.test.result.MunitResult;
import org.mule.munit.test.result.SuiteResult;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * Runs tests
 *
 * @goal test
 * @requiresDependencyResolution test
 * @goal test
 * @phase test
 */
public class MUnitMojo
    extends AbstractMojo
{
    /**
     * @parameter expression="${project}"
     * @required
     */
    protected MavenProject project;

    /**
     * @parameter expression="${munit.test}"
     */
    protected String munittest;

    /**
     * The classpath elements of the project being tested.
     *
     * @parameter expression="${project.testClasspathElements}"
     * @required
     * @readonly
     */
    protected List<String> classpathElements;


    public void execute()
        throws MojoExecutionException
    {
        
        if ( !"true".equals(System.getProperty("skipTests")) )
        {
            List testResources = project.getTestResources();
            for ( Object o : testResources )
            {
                Resource testResource = (Resource) o;
                testResource.getTargetPath();
            }

            Thread t = Thread.currentThread();
            ClassLoader old = t.getContextClassLoader();
            try {
                List<SuiteResult> results = new ArrayList<SuiteResult>();
                t.setContextClassLoader(getClassPath(makeClassPath()));

                File testFolder = new File(project.getBasedir(), "src/test/munit");

                for ( File file : testFolder.listFiles() )
                {

                    String fileName = file.getName();
                    if (fileName.endsWith(".xml") && validateFilter(fileName))
                    {
                    	System.out.println();
                        System.out.println("===========  Running " + fileName +" test ===========");
                        System.out.println();
                        results.add(buildRunnerFor(fileName).run());
                    }

                }

                show(results);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } finally {
                t.setContextClassLoader(old);
            }
        }

    }

    private void show(List<SuiteResult> results) throws MojoExecutionException {
		boolean success = true;
		
		System.out.println();
		System.out.println("\t=====================================");
		System.out.println("\t  Munit Summary                      ");
		System.out.println("\t=====================================");
		
		for ( SuiteResult run : results )
		{
			List<MunitResult> failingTests = run.getFailingTests();
			List<MunitResult> errorTests = run.getErrorTests();
			System.out.println("\t >> " + run.getTestName() + " test result: Errors: " + errorTests.size() + ", Failures:" + failingTests.size());
			
			showFailures(failingTests);
			showError(errorTests);

		    if ( !failingTests.isEmpty() || !errorTests.isEmpty() )
		    {
		    	success = false;
		    }
		}
		
		if ( !success )
		{
		  	throw new MojoExecutionException("MUnit Tests Failed!!!");
		}
	}

	private void showFailures(List<MunitResult> failingTests) {
		if ( !failingTests.isEmpty() )
		{
			for ( MunitResult result : failingTests )
			{
				System.out.println("\t\t ---" + result.getTestName() + " <<< FAILED");
			}
		}
	}

	private void showError(List<MunitResult> errorTests) {
		if ( !errorTests.isEmpty() )
		{
			for ( MunitResult result : errorTests )
			{
				System.out.println("\t\t ---" + result.getTestName() + " <<< ERROR");
			}
		}
	}

	private MunitTestRunner buildRunnerFor(String fileName) {
		MunitTestRunner runner = new MunitTestRunner(fileName);
		runner.setNotificationListener(new StreamNotificationListener(System.out));
		return runner;
	}

    private boolean validateFilter(String fileName) {
        if ( munittest == null )
        {
            return true;
        }

        return fileName.matches(munittest);
    }

    public URLClassLoader getClassPath(List<URL> classpath) {
       return new URLClassLoader(classpath.toArray(new URL[classpath.size()]), getClass().getClassLoader());
    }

    /**
     * Creates a classloader for loading tests.
     *
     * <p>
     * We need to be able to see the same JUnit classes between this code and the mtest code,
     * but everything else should be isolated.
     */
    private List<URL> makeClassPath() throws MalformedURLException {

        List<URL> urls = new ArrayList<URL>(classpathElements.size());

        for (String e : classpathElements)
            urls.add(new File(e).toURL());
        return urls;
    }
}
