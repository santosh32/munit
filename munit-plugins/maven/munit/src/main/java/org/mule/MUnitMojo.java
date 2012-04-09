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
import org.junit.internal.RealSystem;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.mule.munit.MTest;
import org.mule.munit.MuleSuiteRunner;

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
                List<Result> results = new ArrayList<Result>();
                t.setContextClassLoader(getClassPath(makeClassPath()));

                File testFolder = new File(project.getBasedir(), "src/test/munit");

                for ( File file : testFolder.listFiles() )
                {

                    String fileName = file.getName();
                    if (fileName.endsWith(".xml"))
                    {
                        System.out.println("Running " + fileName +" test");
                        System.setProperty("munit.resource", fileName);

                        MuleSuiteRunner muleSuiteRunner = new MuleSuiteRunner(MTest.class);
                        JUnitCore core = new JUnitCore();
                        core.addListener(new TextListener(new RealSystem()));
                        results.add(core.run(muleSuiteRunner));
                    }

                }

                for ( Result run : results )
                {
                    if ( !run.wasSuccessful() )
                        throw new MojoExecutionException("MUnit Tests Failed");
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } finally {
                t.setContextClassLoader(old);
            }
        }

    }

    public URLClassLoader getClassPath(List<URL> classpath) {
       return new URLClassLoader(classpath.toArray(new URL[classpath.size()]), getClass().getClassLoader());
    }

    /**
     * Creates a classloader for loading tests.
     *
     * <p>
     * We need to be able to see the same JUnit classes between this code and the test code,
     * but everything else should be isolated.
     */
    private List<URL> makeClassPath() throws MalformedURLException {

        List<URL> urls = new ArrayList<URL>(classpathElements.size());

        for (String e : classpathElements)
            urls.add(new File(e).toURL());
        return urls;
    }
}
