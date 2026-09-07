/*******************************************************************************
 * Copyright (c) 2008, 2026 IBM Corporation and others.
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
import org.eclipse.e4.core.di.annotations.*;
import org.eclipse.e4.ui.di.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.eclipse.e4.ui.model.application.ui.menu.*;
import org.eclipse.swt.*;
import org.eclipse.swt.tools.internal.*;
import org.eclipse.swt.widgets.*;

import jakarta.annotation.*;


public class MacGeneratorView {
	private static final String GENERATE_ICON = "platform:/plugin/org.eclipse.swt.tools/icons/save_edit.svg";
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
		Path rootPath = new Path("Eclipse SWT PI/cocoa");
		root = swtProject.findMember(rootPath);
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
		if (root != null) workspace.addResourceChangeListener(listener);
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	@PostConstruct
	public void createPartControl(Composite parent, MPart part) {
		if (root == null) {
			Label label = new Label(parent, SWT.WRAP);
			label.setText("Project org.eclipse.swt with folder \"Eclipse SWT PI/cocoa\" was not found in the workspace.");
			return;
		}
		MacGenerator gen = new MacGenerator();
		gen.setOutputDir(root.getLocation().toPortableString());
		gen.setMainClass(mainClassName);
		gen.setSelectorEnum(selectorEnumName);
		ui = new MacGeneratorUI(gen);
		ui.setActionsVisible(false);
		ui.open(parent);

		contributeToPart(part);
	}

	private void contributeToPart(MPart part) {
		MToolBar toolBar = part.getToolbar();
		if (toolBar == null) {
			toolBar = MMenuFactory.INSTANCE.createToolBar();
			MDirectToolItem item = MMenuFactory.INSTANCE.createDirectToolItem();
			item.setLabel("Generate");
			item.setTooltip("Generate");
			item.setIconURI(GENERATE_ICON);
			toolBar.getChildren().add(item);
			part.setToolbar(toolBar);
		}
		MMenu menu = part.getMenus().stream().filter(m -> m.getTags().contains("ViewMenu")).findFirst().orElse(null);
		if (menu == null) {
			menu = MMenuFactory.INSTANCE.createMenu();
			menu.getTags().add("ViewMenu");
			MDirectMenuItem item = MMenuFactory.INSTANCE.createDirectMenuItem();
			item.setLabel("Generate");
			item.setIconURI(GENERATE_ICON);
			menu.getChildren().add(item);
			part.getMenus().add(menu);
		}
		// The items are persisted with the workbench model, the object is not
		for (MToolBarElement element : toolBar.getChildren()) {
			if (element instanceof MDirectToolItem item) item.setObject(this);
		}
		for (MMenuElement element : menu.getChildren()) {
			if (element instanceof MDirectMenuItem item) item.setObject(this);
		}
	}
	
	@PreDestroy
	public void dispose() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		workspace.removeResourceChangeListener(listener);
	}
	
	void refresh() {
		try {
			root.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
//			e.printStackTrace();
		}
	}
	
	@Execute
	void generate() {
		if (job != null) return;
		job = new GenJob();
		job.schedule();
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Focus
	public void setFocus() {
		if (ui != null) ui.setFocus();
	}
}