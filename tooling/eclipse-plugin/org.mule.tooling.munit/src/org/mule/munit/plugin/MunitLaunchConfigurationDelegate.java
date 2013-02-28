package org.mule.munit.plugin;

/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     David Saff (saff@mit.edu) - bug 102632: [JUnit] Support for JUnit 4.
 *     Robert Konigsberg <konigsberg@google.com> - [JUnit] Leverage AbstractJavaLaunchConfigurationDelegate.getMainTypeName in JUnitLaunchConfigurationDelegate - https://bugs.eclipse.org/bugs/show_bug.cgi?id=280114
 *     Achim Demelt <a.demelt@exxcellent.de> - [junit] Separate UI from non-UI code - https://bugs.eclipse.org/bugs/show_bug.cgi?id=278844
 *******************************************************************************/

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.mule.munit.runner.MuleContextManager;
import org.osgi.framework.Bundle;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;

import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;

import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;

import org.eclipse.jdt.internal.junit.JUnitCorePlugin;
import org.eclipse.jdt.internal.junit.JUnitMessages;
import org.eclipse.jdt.internal.junit.Messages;
import org.eclipse.jdt.internal.junit.launcher.ITestKind;
import org.eclipse.jdt.internal.junit.launcher.JUnitLaunchConfigurationConstants;
import org.eclipse.jdt.internal.junit.launcher.JUnitRuntimeClasspathEntry;
import org.eclipse.jdt.internal.junit.launcher.TestKindRegistry;
import org.eclipse.jdt.internal.junit.util.CoreTestSearchEngine;
import org.eclipse.jdt.internal.junit.util.IJUnitStatusConstants;


import org.eclipse.jdt.launching.AbstractJavaLaunchConfigurationDelegate;
import org.eclipse.jdt.launching.ExecutionArguments;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.SocketUtil;
import org.eclipse.jdt.launching.VMRunnerConfiguration;


/**
 * Launch configuration delegate for a JUnit test as a Java application.
 *
 * <p>
 * Clients can instantiate and extend this class.
 * </p>
 * @since 3.3
 */
public class MunitLaunchConfigurationDelegate extends AbstractJavaLaunchConfigurationDelegate {

	private boolean fKeepAlive= false;
	private int fPort;
	private IMember[] fTestElements;

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.ILaunchConfigurationDelegate#launch(org.eclipse.debug.core.ILaunchConfiguration, java.lang.String, org.eclipse.debug.core.ILaunch, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public synchronized void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}
		monitor.beginTask(MessageFormat.format("{0}...", new String[]{configuration.getName()}), 5); //$NON-NLS-1$
		// check for cancellation
		if (monitor.isCanceled()) {
			return;
		}

		try {

			try {
				preLaunchCheck(configuration, launch, new SubProgressMonitor(monitor, 2));
			} catch (CoreException e) {
				if (e.getStatus().getSeverity() == IStatus.CANCEL) {
					monitor.setCanceled(true);
					return;
				}
				throw e;
			}
			// check for cancellation
			if (monitor.isCanceled()) {
				return;
			}
			
			IProject project = getJavaProject(configuration).getProject();
			project.build(IncrementalProjectBuilder.FULL_BUILD, monitor);

			Map<String, IFolder> sourceFolders = new HashMap<String, IFolder>();
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IJavaProject javaProject = getJavaProject(configuration);
			IPath munitOutputFolder = null;
			IClasspathEntry[] entries = javaProject .getResolvedClasspath(true);
			for (int i = 0; i < entries.length; i++) {
				IClasspathEntry entry = entries[i];
				if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
					IPath path = entry.getPath();
					IFolder sourceFolder = root.getFolder(path);
					if ( sourceFolder.getLocation().toString().contains("test/munit") ){
						munitOutputFolder = entry.getOutputLocation();
					}
					
				}
			}
			
			
			
			MunitEclipseUpdater.launch();

			String mainTypeName= verifyMainTypeName(configuration);
			IVMRunner runner= getVMRunner(configuration, mode);

			File workingDir = verifyWorkingDirectory(configuration);
			String workingDirName = null;
			if (workingDir != null) {
				workingDirName= workingDir.getAbsolutePath();
			}

			String[] envp= getEnvironment(configuration);

			ArrayList vmArguments= new ArrayList();
			ArrayList programArguments= new ArrayList();
			programArguments.add("-resource");
			programArguments.add(configuration.getAttribute("resource", ""));
			programArguments.add("-path");
			programArguments.add(configuration.getAttribute("Mpath", ""));
			programArguments.add("-port");
			programArguments.add(String.valueOf(MunitEclipseUpdater.getInstance().getPort()));

			// VM-specific attributes
			Map vmAttributesMap= getVMSpecificAttributesMap(configuration);
				
			for (int i = 0; i < entries.length; i++) {
				IClasspathEntry entry = entries[i];
				if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
					IPath path = entry.getPath();
					IFolder sourceFolder = root.getFolder(path);
					if ( !sourceFolder.getLocation().toString().contains("test/munit") ){
						try {
							IFolder folder = root.getFolder(entry.getOutputLocation());
							for ( IResource resource : folder.members() ){
								try{
									resource.copy(munitOutputFolder, IFolder.SHALLOW,  monitor);
								}
								catch(Throwable e){
									
								}
							}
							
						}
						catch(Throwable y){
							
						}
						
					}
					
				}
			}
			String[] classpath = getClasspath(configuration);
			// ClasspathgetC
			List<String> classPathAsList = new ArrayList<String>(Arrays.asList(classpath));
			try {
				URL[] urlClasspath = new ClasspathProvider().getClassPath(getJavaProject(configuration));
				for ( URL url : urlClasspath ){
					classPathAsList.add(url.getFile());
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			

			// Create VM config
			VMRunnerConfiguration runConfig= new VMRunnerConfiguration("org.mule.munit.runner.remote.MunitRemoteRunner", classPathAsList.toArray(new String[]{}));
			runConfig.setVMArguments((String[]) vmArguments.toArray(new String[vmArguments.size()]));
			runConfig.setProgramArguments((String[]) programArguments.toArray(new String[programArguments.size()]));
			runConfig.setEnvironment(envp);
			runConfig.setWorkingDirectory(workingDirName);
			runConfig.setVMSpecificAttributesMap(vmAttributesMap);

			// Bootpath
			runConfig.setBootClassPath(getBootpath(configuration));
			
			
			// check for cancellation
			if (monitor.isCanceled()) {
				return;
			}

			// done the verification phase
			monitor.worked(1);

			setDefaultSourceLocator(launch, configuration);
			monitor.worked(1);

			runner.run(runConfig, launch, monitor);
			
			
			if (monitor.isCanceled()) {
				return;
			}
		} finally {
			fTestElements= null;
			monitor.done();
		}
	}

	private int evaluatePort() throws CoreException {
		int port= SocketUtil.findFreePort();
		if (port == -1) {
			abort(JUnitMessages.JUnitLaunchConfigurationDelegate_error_no_socket, null, IJavaLaunchConfigurationConstants.ERR_NO_SOCKET_AVAILABLE);
		}
		return port;
	}

	/**
	 * Performs a check on the launch configuration's attributes. If an attribute contains an invalid value, a {@link CoreException}
	 * with the error is thrown.
	 *
	 * @param configuration the launch configuration to verify
	 * @param launch the launch to verify
	 * @param monitor the progress monitor to use
	 * @throws CoreException an exception is thrown when the verification fails
	 */
	protected void preLaunchCheck(ILaunchConfiguration configuration, ILaunch launch, IProgressMonitor monitor) throws CoreException {
		try {
			IJavaProject javaProject= getJavaProject(configuration);
			if ((javaProject == null) || !javaProject.exists()) {
				abort(JUnitMessages.JUnitLaunchConfigurationDelegate_error_invalidproject, null, IJavaLaunchConfigurationConstants.ERR_NOT_A_JAVA_PROJECT);
			}
			if (!CoreTestSearchEngine.hasTestCaseType(javaProject)) {
				abort(JUnitMessages.JUnitLaunchConfigurationDelegate_error_junitnotonpath, null, IJUnitStatusConstants.ERR_JUNIT_NOT_ON_PATH);
			}

			ITestKind testKind= getTestRunnerKind(configuration);
			boolean isJUnit4Configuration= TestKindRegistry.JUNIT4_TEST_KIND_ID.equals(testKind.getId());
			if (isJUnit4Configuration && ! CoreTestSearchEngine.hasTestAnnotation(javaProject)) {
				abort(JUnitMessages.JUnitLaunchConfigurationDelegate_error_junit4notonpath, null, IJUnitStatusConstants.ERR_JUNIT_NOT_ON_PATH);
			}
		} finally {
			monitor.done();
		}
	}

	private ITestKind getTestRunnerKind(ILaunchConfiguration configuration) {
		ITestKind testKind= JUnitLaunchConfigurationConstants.getTestRunnerKind(configuration);
		if (testKind.isNull()) {
			testKind= TestKindRegistry.getDefault().getKind(TestKindRegistry.JUNIT3_TEST_KIND_ID); // backward compatible for launch configurations with no runner
		}
		return testKind;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.launching.AbstractJavaLaunchConfigurationDelegate#verifyMainTypeName(org.eclipse.debug.core.ILaunchConfiguration)
	 */
	public String verifyMainTypeName(ILaunchConfiguration configuration) throws CoreException {
		return "org.eclipse.jdt.internal.junit.runner.RemoteTestRunner"; //$NON-NLS-1$
	}

	/**
	 * Evaluates all test elements selected by the given launch configuration. The elements are of type
	 * {@link IType} or {@link IMethod}. At the moment it is only possible to run a single method or a set of types, but not
	 * mixed or more than one method at a time.
	 *
	 * @param configuration the launch configuration to inspect
	 * @param monitor the progress monitor
	 * @return returns all types or methods that should be ran
	 * @throws CoreException an exception is thrown when the search for tests failed
	 */
	protected IMember[] evaluateTests(ILaunchConfiguration configuration, IProgressMonitor monitor) throws CoreException {
		IJavaProject javaProject= getJavaProject(configuration);

		IJavaElement testTarget= getTestTarget(configuration, javaProject);
		String testMethodName= configuration.getAttribute(JUnitLaunchConfigurationConstants.ATTR_TEST_METHOD_NAME, ""); //$NON-NLS-1$
		if (testMethodName.length() > 0) {
			if (testTarget instanceof IType) {
				return new IMember[] { ((IType) testTarget).getMethod(testMethodName, new String[0]) };
			}
		}
		HashSet result= new HashSet();
		ITestKind testKind= getTestRunnerKind(configuration);
		testKind.getFinder().findTestsInContainer(testTarget, result, monitor);
		if (result.isEmpty()) {
			String msg= Messages.format(JUnitMessages.JUnitLaunchConfigurationDelegate_error_notests_kind, testKind.getDisplayName());
			abort(msg, null, IJavaLaunchConfigurationConstants.ERR_UNSPECIFIED_MAIN_TYPE);
		}
		return (IMember[]) result.toArray(new IMember[result.size()]);
	}

	/**
	 * Collects all VM and program arguments. Implementors can modify and add arguments.
	 *
	 * @param configuration the configuration to collect the arguments for
	 * @param vmArguments a {@link List} of {@link String} representing the resulting VM arguments
	 * @param programArguments a {@link List} of {@link String} representing the resulting program arguments
	 * @exception CoreException if unable to collect the execution arguments
	 */
	protected void collectExecutionArguments(ILaunchConfiguration configuration, List/*String*/ vmArguments, List/*String*/ programArguments) throws CoreException {

		// add program & VM arguments provided by getProgramArguments and getVMArguments
		String pgmArgs= getProgramArguments(configuration);
		String vmArgs= getVMArguments(configuration);
		ExecutionArguments execArgs= new ExecutionArguments(vmArgs, pgmArgs);
		vmArguments.addAll(Arrays.asList(execArgs.getVMArgumentsArray()));
		programArguments.addAll(Arrays.asList(execArgs.getProgramArgumentsArray()));

		String testFailureNames= configuration.getAttribute(JUnitLaunchConfigurationConstants.ATTR_FAILURES_NAMES, ""); //$NON-NLS-1$

		programArguments.add("-version"); //$NON-NLS-1$
		programArguments.add("3"); //$NON-NLS-1$

		programArguments.add("-port"); //$NON-NLS-1$
		programArguments.add(String.valueOf(fPort));

		if (fKeepAlive)
			programArguments.add(0, "-keepalive"); //$NON-NLS-1$

		ITestKind testRunnerKind= getTestRunnerKind(configuration);
		getTestTarget(configuration, getJavaProject(configuration));
		programArguments.add("-testLoaderClass"); //$NON-NLS-1$
		programArguments.add(testRunnerKind.getLoaderClassName());
		programArguments.add("-loaderpluginname"); //$NON-NLS-1$
		programArguments.add(testRunnerKind.getLoaderPluginId());

		IMember[] testElements = fTestElements;

		// a test name was specified just run the single test
		if (testElements.length == 1) {
			if (testElements[0] instanceof IMethod) {
				IMethod method= (IMethod) testElements[0];
				programArguments.add("-test"); //$NON-NLS-1$
				programArguments.add(method.getDeclaringType().getFullyQualifiedName()+':'+method.getElementName());
			} else if (testElements[0] instanceof IType) {
				IType type= (IType) testElements[0];
				programArguments.add("-classNames"); //$NON-NLS-1$
				programArguments.add(type.getFullyQualifiedName());
			} else {
				abort(JUnitMessages.JUnitLaunchConfigurationDelegate_error_wrong_input, null, IJavaLaunchConfigurationConstants.ERR_UNSPECIFIED_MAIN_TYPE);
			}
		} else if (testElements.length > 1) {
			String fileName= createTestNamesFile(testElements);
			programArguments.add("-testNameFile"); //$NON-NLS-1$
			programArguments.add(fileName);
		}
		if (testFailureNames.length() > 0) {
			programArguments.add("-testfailures"); //$NON-NLS-1$
			programArguments.add(testFailureNames);
		}
	}

	private String createTestNamesFile(IMember[] testElements) throws CoreException {
		try {
			File file= File.createTempFile("testNames", ".txt"); //$NON-NLS-1$ //$NON-NLS-2$
			file.deleteOnExit();
			BufferedWriter bw= null;
			try {
				bw= new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8")); //$NON-NLS-1$
				for (int i= 0; i < testElements.length; i++) {
					if (testElements[i] instanceof IType) {
						IType type= (IType) testElements[i];
						String testName= type.getFullyQualifiedName();
						bw.write(testName);
						bw.newLine();
					} else {
						abort(JUnitMessages.JUnitLaunchConfigurationDelegate_error_wrong_input, null, IJavaLaunchConfigurationConstants.ERR_UNSPECIFIED_MAIN_TYPE);
					}
				}
			} finally {
				if (bw != null) {
					bw.close();
				}
			}
			return file.getAbsolutePath();
		} catch (IOException e) {
			throw new CoreException(new Status(IStatus.ERROR, JUnitCorePlugin.CORE_PLUGIN_ID, IStatus.ERROR, "", e)); //$NON-NLS-1$
		}
	}


	private final IJavaElement getTestTarget(ILaunchConfiguration configuration, IJavaProject javaProject) throws CoreException {
		String containerHandle = configuration.getAttribute(JUnitLaunchConfigurationConstants.ATTR_TEST_CONTAINER, ""); //$NON-NLS-1$
		if (containerHandle.length() != 0) {
			 IJavaElement element= JavaCore.create(containerHandle);
			 if (element == null || !element.exists()) {
				 abort(JUnitMessages.JUnitLaunchConfigurationDelegate_error_input_element_deosn_not_exist, null, IJavaLaunchConfigurationConstants.ERR_UNSPECIFIED_MAIN_TYPE);
			 }
			 return element;
		}
		String testTypeName= getMainTypeName(configuration);
		if (testTypeName != null && testTypeName.length() != 0) {
			IType type= javaProject.findType(testTypeName);
			if (type != null && type.exists()) {
				return type;
			}
		}
		abort(JUnitMessages.JUnitLaunchConfigurationDelegate_input_type_does_not_exist, null, IJavaLaunchConfigurationConstants.ERR_UNSPECIFIED_MAIN_TYPE);
		return null; // not reachable
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.junit.launcher.ITestFindingAbortHandler#abort(java.lang.String, java.lang.Throwable, int)
	 */
	protected void abort(String message, Throwable exception, int code) throws CoreException {
		throw new CoreException(new Status(IStatus.ERROR, JUnitCorePlugin.CORE_PLUGIN_ID, code, message, exception));
	}

	
}

