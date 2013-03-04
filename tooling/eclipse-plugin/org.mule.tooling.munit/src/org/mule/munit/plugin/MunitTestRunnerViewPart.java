package org.mule.munit.plugin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.eclipse.core.internal.resources.File;
import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.junit.BasicElementLabels;
import org.eclipse.jdt.internal.junit.JUnitCorePlugin;
import org.eclipse.jdt.internal.junit.JUnitPreferencesConstants;
import org.eclipse.jdt.internal.junit.Messages;
import org.eclipse.jdt.internal.junit.model.TestRunSession;
import org.eclipse.jdt.internal.junit.ui.CounterPanel;
import org.eclipse.jdt.internal.junit.ui.JUnitMessages;
import org.eclipse.jdt.internal.junit.ui.JUnitPlugin;
import org.eclipse.jdt.internal.junit.ui.JUnitProgressBar;
import org.eclipse.jdt.internal.junit.ui.TestViewer;
import org.eclipse.jdt.internal.ui.viewsupport.ViewHistory;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.registry.EditorDescriptor;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.progress.UIJob;
import org.eclipse.wst.xml.ui.internal.tabletree.XMLMultiPageEditorPart;

public class MunitTestRunnerViewPart extends ViewPart {
	static final int REFRESH_INTERVAL = 200;
	public static final String NAME = "org.eclipse.jdt.munit.ResultView";
	static final int LAYOUT_FLAT = 0;
	static final int LAYOUT_HIERARCHICAL = 1;

	UpdateUIJob updateUIJob;
	SuiteStatus suiteStatus;
	CounterPanel counterPanel;
	private Composite fCounterComposite;
	private Composite fViewerComposite;
	private int processedTests;
	private Composite fParent;
	private Text errorViewer;

	final ImageDescriptor fSuiteIconDescriptor = JUnitPlugin
			.getImageDescriptor("obj16/tsuite.gif"); //$NON-NLS-1$
	final ImageDescriptor fSuiteOkIconDescriptor = JUnitPlugin
			.getImageDescriptor("obj16/tsuiteok.gif"); //$NON-NLS-1$
	final ImageDescriptor fSuiteErrorIconDescriptor = JUnitPlugin
			.getImageDescriptor("obj16/tsuiteerror.gif"); //$NON-NLS-1$
	final ImageDescriptor fSuiteFailIconDescriptor = JUnitPlugin
			.getImageDescriptor("obj16/tsuitefail.gif"); //$NON-NLS-1$
	final ImageDescriptor fSuiteRunningIconDescriptor = JUnitPlugin
			.getImageDescriptor("obj16/tsuiterun.gif"); //$NON-NLS-1$

	protected JUnitProgressBar fProgressBar;
	private TreeViewer fTreeViewer;
	private boolean fIsDisposed;

	public MunitTestRunnerViewPart() {

	}

	private class UpdateUIJob extends UIJob {
		private boolean fRunning = true;

		public UpdateUIJob(String name) {
			super(name);
			setSystem(true);
		}

		public IStatus runInUIThread(IProgressMonitor monitor) {
			if (!fIsDisposed && MunitEclipseUpdater.getInstance().isRunning()) {
				if (suiteStatus != null) {
					fProgressBar.setMaximum(suiteStatus.getNumberOfTests());

					if (processedTests < suiteStatus.getProcessedTests()) {
						int steps = (suiteStatus.getProcessedTests() - processedTests); 
						for (int i=0; i<steps; i++){
						fProgressBar.step(suiteStatus.getErrors()
								+ suiteStatus.getFailures());
						}
						processedTests+=steps;
					}

					counterPanel.setTotal(suiteStatus.getNumberOfTests());
					counterPanel
							.setRunValue(suiteStatus.getProcessedTests(), 0);
					counterPanel.setErrorValue(suiteStatus.getErrors());
					counterPanel.setFailureValue(suiteStatus.getFailures());

					fTreeViewer.setInput(suiteStatus);
				}

			}
	
			schedule(REFRESH_INTERVAL);

			return Status.OK_STATUS;
		}

		public void stop() {
			fProgressBar.step(suiteStatus.getErrors()
					+ suiteStatus.getFailures());
			fRunning = false;
		}

		public boolean shouldSchedule() {
			return fRunning;
		}
	}

	public void setSuiteStatus(SuiteStatus suiteStatus) {
		this.suiteStatus = suiteStatus;

	}

	public void clear() {
		fIsDisposed = false;
		processedTests = 0;
		counterPanel.reset();
		fProgressBar.reset();
		errorViewer.setText("");
		fTreeViewer.setInput(new SuiteStatus());
	}

	protected Composite createProgressCountPanel(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);

		counterPanel = new CounterPanel(composite);
		counterPanel.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
				| GridData.HORIZONTAL_ALIGN_FILL));
		fProgressBar = new JUnitProgressBar(composite);
		fProgressBar.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
				| GridData.HORIZONTAL_ALIGN_FILL));
		return composite;
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout();
		parent.setLayout(layout);

		fCounterComposite = createProgressCountPanel(parent);
		fCounterComposite.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
				| GridData.HORIZONTAL_ALIGN_FILL));

		fViewerComposite = createViewerComposite(parent);
		fViewerComposite.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
				| GridData.HORIZONTAL_ALIGN_FILL | GridData.FILL_VERTICAL
				| GridData.GRAB_VERTICAL));

		parent.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
				| GridData.HORIZONTAL_ALIGN_FILL));

		updateUIJob = new UpdateUIJob(JUnitMessages.TestRunnerViewPart_jobName);
		updateUIJob.schedule(REFRESH_INTERVAL);
	}

	private Composite createViewerComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		FillLayout layout = new FillLayout();
		layout.type = SWT.HORIZONTAL;
		layout.spacing = 20;
		composite.setLayout(layout);

		errorViewer = new Text(composite, SWT.MULTI | SWT.V_SCROLL
				| SWT.H_SCROLL | SWT.READ_ONLY);

		fTreeViewer = buildTreeViewer(composite);

		return composite;
	}

	private TreeViewer buildTreeViewer(Composite composite) {
		TreeViewer tree = new TreeViewer(composite, SWT.V_SCROLL | SWT.SINGLE);
		tree.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {

				IPath path = new Path(suiteStatus.getSuitePath());
				
				Workspace w = (Workspace) ResourcesPlugin.getWorkspace();

				File file = (File) w.newResource(path, IResource.FILE);

				FileEditorInput input = new FileEditorInput(file);

				try {
					int myFile = fefe(file.getContents(), getSelectedStatus(event).getTestName());
					IWorkbenchPage activePage = PlatformUI
							.getWorkbench()
							.getActiveWorkbenchWindow()
							.getActivePage();
					
					HashMap map = new HashMap();
					   map.put(IMarker.LINE_NUMBER, new Integer(myFile));
					   map.put(IWorkbenchPage.EDITOR_ID_ATTR, 
							   "org.eclipse.wst.xml.ui.internal.tabletree.XMLMultiPageEditorPart");
					   IMarker marker = file.createMarker(IMarker.TEXT);
					   marker.setAttributes(map);
					   //page.openEditor(marker); //2.1 API
					   IDE.openEditor(activePage, marker);

				} catch (PartInitException e) {

					MessageDialog
							.openError(PlatformUI.getWorkbench()
									.getActiveWorkbenchWindow().getShell(), "",
									"Error");
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		tree.setLabelProvider(new ILabelProvider() {

			@Override
			public void addListener(ILabelProviderListener listener) {
				// TODO Auto-generated method stub

			}

			@Override
			public void dispose() {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean isLabelProperty(Object element, String property) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void removeListener(ILabelProviderListener listener) {
				// TODO Auto-generated method stub

			}

			@Override
			public Image getImage(Object element) {

				if (element instanceof TestStatus) {
					TestStatus testStatus = (TestStatus) element;
					if (testStatus.isFinished()) {
						if (testStatus.hasError()) {
							return MunitPlugin.getImageDescriptor(
									"/studio16x16-error.png").createImage();
						} else if (testStatus.hasFailed()) {
							return MunitPlugin.getImageDescriptor(
									"/studio16x16-failed.png").createImage();
						}

						return MunitPlugin.getImageDescriptor(
								"/studio16x16-ok.png").createImage();
					}

				}

				return MunitPlugin.getImageDescriptor("/studio16x16.png")
						.createImage();
			}

			@Override
			public String getText(Object element) {
				if (element instanceof TestStatus) {
					return ((TestStatus) element).getTestName();
				} else if (element instanceof SuiteStatus) {
					return ((SuiteStatus) element).getName();
				}
				return null;
			}

		});
		tree.setContentProvider(new ITreeContentProvider() {

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
				// TODO Auto-generated method stub

			}

			@Override
			public void dispose() {
				// TODO Auto-generated method stub

			}

			@Override
			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof SuiteStatus) {
					return ((SuiteStatus) inputElement).getTests().toArray();
				}
				return null;
			}

			@Override
			public Object[] getChildren(Object parentElement) {
				return getElements(parentElement);
			}

			@Override
			public Object getParent(Object element) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean hasChildren(Object element) {
				// TODO Auto-generated method stub
				return false;
			}
		});

		tree.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				// if the selection is empty clear the label
				if (event.getSelection().isEmpty()) {
					return;
				}
				if (event.getSelection() instanceof TreeSelection) {
					Object firstElement = ((TreeSelection) event.getSelection())
							.getFirstElement();
					if (firstElement instanceof TestStatus) {
						TestStatus testStatus = (TestStatus) firstElement;
						String cause = testStatus.getCause();
						if (cause != null) {
							errorViewer.setText(cause);
						} else {
							errorViewer.setText("");
						}

					}

				}
			}

		});
		return tree;
	}
	
	public int fefe(InputStream is, String testName)
	{
    	 try{
    		 
    	
    	BufferedReader br
        	= new BufferedReader(
        		new InputStreamReader(is));
 
    	StringBuilder sb = new StringBuilder();
    	int i=1;
    	String line;
    	while ((line = br.readLine()) != null) {
    		if ( line.contains("name=\"" +  testName + "\""))
    		{
    			
    			br.close();
    			return i; 
    		}
    		i++;
    	} 
    	
    	

    	br.close();
    	return i;
    	 }
    	 catch(Exception e)
    	 {
    		 return 1;
    	 }
    	 
	}
	
	public TestStatus getSelectedStatus(DoubleClickEvent event)
	{
		Object firstElement = ((TreeSelection) event.getSelection())
				.getFirstElement();
		if (firstElement instanceof TestStatus) {
			return (TestStatus) firstElement;
		}
		
		return null;
	}

	@Override
	public void setFocus() {
		counterPanel.setFocus();
	}

	public synchronized void dispose() {
		fIsDisposed = true;
		

	}

}
