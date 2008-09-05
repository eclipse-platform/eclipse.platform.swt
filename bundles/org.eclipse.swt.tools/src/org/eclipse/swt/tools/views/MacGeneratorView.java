package org.eclipse.swt.tools.views;

import org.eclipse.swt.tools.internal.MacGenerator;
import org.eclipse.swt.tools.internal.MacGeneratorUI;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.*;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.*;
import org.eclipse.ui.*;


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
	private Job job;
	private String mainClassName = "org.eclipse.swt.internal.cocoa.OS";
	
	class GenJob extends Job {
		public GenJob() {
			super("Mac Genarator");
		}
		protected IStatus run(IProgressMonitor monitor) {
			ui.generate();
			refresh();
			MacGeneratorView.this.job = null;
			return Status.OK_STATUS;
		}
	}
	
	/**
	 * The constructor.
	 */
	public MacGeneratorView() {
		IWorkspaceRoot workspace = ResourcesPlugin.getWorkspace().getRoot();
		root = workspace.findMember(new Path("org.eclipse.swt/Eclipse SWT PI/cocoa"));
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		String[] xmls = null;
//		xmls = new String[]{
//			"AppKitFull.bridgesupport",
//			"FoundationFull.bridgesupport",
//			"WebKitFull.bridgesupport",
//		};
		MacGenerator gen = new MacGenerator();
		gen.setXmls(xmls);
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