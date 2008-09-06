package org.eclipse.swt.tools.jni.builder;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.tools.internal.JNIGeneratorApp;
import org.eclipse.swt.tools.internal.MetaData;

public class JNIBuilder extends IncrementalProjectBuilder {

	MetaData metaData;
	MainClass[] classes;
	
	static class MainClass {
		public String mainClassName;
		public String outputDir;
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
			}
		}
	}
	
	protected IProject[] build(int kind, Map args, final IProgressMonitor monitor) throws CoreException {
		IResourceDelta delta = getDelta(getProject());
		delta.accept(new IResourceDeltaVisitor() {
			public boolean visit(IResourceDelta delta) throws CoreException {
				String path = delta.getFullPath().toPortableString();
				for (int i = 0; i < classes.length; i++) {
					if (classes[i].build) continue;
					String outputDir = classes[i].outputDir;
					if (path.startsWith(outputDir.substring(0, outputDir.length() - "library/".length()))) {
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
				System.out.println(mainClass.mainClassName);
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
