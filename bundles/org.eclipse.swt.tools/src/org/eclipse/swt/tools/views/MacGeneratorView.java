package org.eclipse.swt.tools.views;

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.*;
import org.eclipse.jface.action.*;
import org.eclipse.swt.tools.internal.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;
import org.eclipse.ui.part.*;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class MacGeneratorView extends ViewPart {
	private Action generateAction;
	private MacGeneratorUI ui;
	private IResource root;
	IResourceChangeListener listener;
	private Job job;
	private String mainClassName = "org.eclipse.swt.internal.cocoa.OS";
	
	class GenJob extends Job {
		public GenJob() {
			super("Mac Generator");
		}
		protected IStatus run(final IProgressMonitor monitor) {
			try {
				ui.generate(new ProgressMonitor() {
					public void setMessage(String message) {
						monitor.subTask(message);
					}
					public void setTotal(int total) {
						monitor.beginTask("Generating", total);
					}
					public void step() {
						monitor.worked(1);
					}
				});
				refresh();
			} finally {
				monitor.done();
				MacGeneratorView.this.job = null;
			}
			return Status.OK_STATUS;
		}
	}
	
	/**
	 * The constructor.
	 */
	public MacGeneratorView() {
		MacGenerator.BUILD_C_SOURCE = false;
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		root = workspaceRoot.findMember(new Path("org.eclipse.swt/Eclipse SWT PI/cocoa"));		
		listener = new IResourceChangeListener() {
			public void resourceChanged(IResourceChangeEvent event) {
				if (job != null) return;
				if (event.getType() != IResourceChangeEvent.POST_CHANGE) return;
				IResourceDelta rootDelta = event.getDelta();
				IResourceDelta piDelta = rootDelta.findMember(root.getFullPath());
				if (piDelta == null) return;
				final ArrayList changed = new ArrayList();
				IResourceDeltaVisitor visitor = new IResourceDeltaVisitor() {
					public boolean visit(IResourceDelta delta) {
						if (delta.getKind() != IResourceDelta.CHANGED) return true;
						if ((delta.getFlags() & IResourceDelta.CONTENT) == 0) return true;
						IResource resource = delta.getResource();
						if (resource.getType() == IResource.FILE && "extras".equalsIgnoreCase(resource.getFileExtension())) {
							changed.add(resource);
						}
						return true;
					}
				};
				try {
					piDelta.accept(visitor);
				} catch (CoreException e) {}
				if (changed.size() > 0) {
					ui.refresh();
				}
			}
		};
		workspace.addResourceChangeListener(listener);
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		MacGenerator gen = new MacGenerator();
		gen.setOutputDir(root.getLocation().toPortableString());
		gen.setMainClass(mainClassName);
		ui = new MacGeneratorUI(gen);
		ui.setActionsVisible(false);
		ui.open(parent);

		makeActions();
		contributeToActionBars();
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}
	
	public void dispose() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		workspace.removeResourceChangeListener(listener);
		super.dispose();
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(generateAction);
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(generateAction);
	}
	
	void refresh() {
		try {
			root.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
//			e.printStackTrace();
		}
	}
	
	void generate() {
		if (job != null) return;
		job = new GenJob();
		job.schedule();
	}

	private void makeActions() {
		generateAction = new Action() {
			public void run() {
				generate();
			}
		};
		generateAction.setText("Generate");
		generateAction.setToolTipText("Generate");
		generateAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_ETOOL_SAVE_EDIT));
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		ui.setFocus();
	}
}