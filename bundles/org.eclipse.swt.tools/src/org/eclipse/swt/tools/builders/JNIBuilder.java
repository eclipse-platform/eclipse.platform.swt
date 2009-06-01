/*******************************************************************************
 * Copyright (c) 2008, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tools.builders;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.tools.internal.JNIGeneratorApp;
import org.eclipse.swt.tools.internal.MetaData;

public class JNIBuilder extends IncrementalProjectBuilder {

	MetaData metaData;
	MainClass[] classes;
	
	static class MainClass {
		public String mainClassName;
		public String outputDir;
		public String sourceDir;
		public boolean build;
		
		public String toString() {
			return mainClassName + "->" + outputDir;
		}
	}
	
	public JNIBuilder() {
		metaData = new MetaData("org.eclipse.swt.internal");
		String mainClasses = metaData.getMetaData("swt_main_classes", null);
		if (mainClasses != null) {
			String[] list = mainClasses.split(",");
			classes = new MainClass[list.length / 2];
			for (int i = 0; i < list.length; i += 2) {
				MainClass clazz = classes[i/2] = new MainClass();
				clazz.mainClassName = list[i];
				clazz.outputDir = list[i+1].substring(2, list[i+1].length());
				clazz.sourceDir = clazz.outputDir.substring(0, clazz.outputDir.length() - "library/".length());
			}
		}
	}
	
	protected IProject[] build(int kind, Map args, final IProgressMonitor monitor) throws CoreException {
		IResourceDelta delta = getDelta(getProject());
		if (delta == null) return null;
		delta.accept(new IResourceDeltaVisitor() {
			public boolean visit(IResourceDelta delta) throws CoreException {
				IPath ipath = delta.getFullPath();
				if (!"java".equals(ipath.getFileExtension())) return true;
				String path = ipath.toPortableString();
				for (int i = 0; i < classes.length; i++) {
					if (classes[i].build) continue;
					if (path.startsWith(classes[i].sourceDir)) {
						classes[i].build = true;
					}
				}
				return true;
			}
		});
		final IWorkspaceRoot root = getProject().getWorkspace().getRoot();
		for (int i = 0; i < classes.length; i++) {
			MainClass mainClass = classes[i];
			if (mainClass.build) {
				mainClass.build = false;
				IResource library = root.findMember(mainClass.outputDir);
				JNIGeneratorApp gen = new JNIGeneratorApp();
				gen.setMainClassName(mainClass.mainClassName, library.getLocation().toPortableString());
				gen.generate();
				library.refreshLocal(IResource.DEPTH_INFINITE, null);
			}
		}
		return null;
	}

}
