/*******************************************************************************
 * Copyright (c) 2008, 2016 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
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


public class MacGeneratorView extends ViewPart {
	private Action generateAction;
	private MacGeneratorUI ui;
	private IResource root;
	IResourceChangeListener listener;
	private Job job;
	private String mainClassName = "org.eclipse.swt.internal.cocoa.OS";
	private String selectorEnumName = "org.eclipse.swt.internal.cocoa.Selector";
	
	class GenJob extends Job {
		public GenJob() {
			super("Mac Generator");
		}
		@Override
		protected IStatus run(final IProgressMonitor monitor) {
			try {
				ui.generate(new ProgressMonitor() {
					@Override
					public void setMessage(String message) {
						monitor.subTask(message);
					}
					@Override
					public void setTotal(int total) {
						monitor.beginTask("Generating", total);
					}
					@Override
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
		IProject swtProject = workspaceRoot.getProject("org.eclipse.swt");
		if (swtProject == null) {
			throw new IllegalStateException("Project org.eclipse.swt not found in the workspace.");
		}
		Path rootPath = new Path("Eclipse SWT PI/cocoa");
		root = swtProject.findMember(rootPath);
		if (root == null) {
			throw new IllegalStateException("Path "+rootPath+" not found in the workspace.");
		}
		listener = event -> {
			if (job != null) return;
			if (event.getType() != IResourceChangeEvent.POST_CHANGE) return;
			IResourceDelta rootDelta = event.getDelta();
			IResourceDelta piDelta = rootDelta.findMember(root.getFullPath());
			if (piDelta == null) return;
			final ArrayList<IResource> changed = new ArrayList<>();
			IResourceDeltaVisitor visitor = delta -> {
				if (delta.getKind() != IResourceDelta.CHANGED) return true;
				if ((delta.getFlags() & IResourceDelta.CONTENT) == 0) return true;
				IResource resource = delta.getResource();
				if (resource.getType() == IResource.FILE && "extras".equalsIgnoreCase(resource.getFileExtension())) {
					changed.add(resource);
				}
				return true;
			};
			try {
				piDelta.accept(visitor);
			} catch (CoreException e) {}
			if (changed.size() > 0) {
				ui.refresh();
			}
		};
		workspace.addResourceChangeListener(listener);
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	@Override
	public void createPartControl(Composite parent) {
		MacGenerator gen = new MacGenerator();
		gen.setOutputDir(root.getLocation().toPortableString());
		gen.setMainClass(mainClassName);
		gen.setSelectorEnum(selectorEnumName);
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
	
	@Override
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
			@Override
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
	@Override
	public void setFocus() {
		ui.setFocus();
	}
}