package org.eclipse.swt.tools.builders;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaModelMarker;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.compiler.BuildContext;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.core.compiler.CompilationParticipant;
import org.eclipse.jdt.core.compiler.batch.BatchCompiler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class Check64CompilationParticipant extends CompilationParticipant {
	HashSet sources;

	static final char[] INT_LONG = "int /*long*/".toCharArray();
	static final char[] INT_LONG_ARRAY = "int[] /*long[]*/".toCharArray();
	static final char[] FLOAT_DOUBLE = "float /*double*/".toCharArray();
	static final char[] FLOAT_DOUBLE_ARRAY = "float[] /*double[]*/".toCharArray();
	static final char[] LONG_INT = "long /*int*/".toCharArray();
	static final char[] LONG_INT_ARRAY = "long[] /*int[]*/".toCharArray();
	static final char[] DOUBLE_FLOAT = "double /*float*/".toCharArray();
	static final char[] DOUBLE_FLOAT_ARRAY = "double[] /*float[]*/".toCharArray();
	static final String buildDir = "/.build64/";
	static final String pluginDir = "/org.eclipse.swt/";
	static final String SOURCE_ID = "JNI";/*JavaBuilder.SOURCE_ID*/
	
void create(IContainer file) throws CoreException {
	if (file.exists()) return;
	switch (file.getType()) {
		case IResource.FOLDER:
			create(file.getParent());
			((IFolder)file).create(true, true, null);
	}
}

void createProblemFor(IResource resource, int start, int end, String message) {
	try {
		IMarker marker = resource.createMarker(IJavaModelMarker.JAVA_MODEL_PROBLEM_MARKER);
		int severity = IMarker.SEVERITY_ERROR;
		marker.setAttributes(
			new String[] {IMarker.MESSAGE, IMarker.SEVERITY, IMarker.CHAR_START, IMarker.CHAR_END, IMarker.SOURCE_ID},
			new Object[] {message, new Integer(severity), new Integer(start), new Integer(end), SOURCE_ID});
	} catch (CoreException e) {
		e.printStackTrace();
	}
}

void replace(char[] source, char[] src, char[] dest) {
	int start = 0;
	while (start < source.length) {
		int index = CharOperation.indexOf(src, source, true, start);
		if (index == -1) break;
		System.arraycopy(dest, 0, source, index, dest.length);
		start = index + 1;
	}
}

void replace64(char[] source) {
	if (CharOperation.indexOf(INT_LONG, source, true, 0) != -1 || CharOperation.indexOf(INT_LONG_ARRAY, source, true, 0) != -1 ||
		CharOperation.indexOf(FLOAT_DOUBLE, source, true, 0) != -1 || CharOperation.indexOf(FLOAT_DOUBLE_ARRAY, source, true, 0) != -1) {
		replace(source, INT_LONG, LONG_INT);
		replace(source, INT_LONG_ARRAY, LONG_INT_ARRAY);
		replace(source, FLOAT_DOUBLE, DOUBLE_FLOAT);
		replace(source, FLOAT_DOUBLE_ARRAY, DOUBLE_FLOAT_ARRAY);
	} else {
		replace(source, LONG_INT, INT_LONG);
		replace(source, LONG_INT_ARRAY, INT_LONG_ARRAY);
		replace(source, DOUBLE_FLOAT, FLOAT_DOUBLE);
		replace(source, DOUBLE_FLOAT_ARRAY, FLOAT_DOUBLE_ARRAY);
	}
}
	
public void buildStarting(BuildContext[] files, boolean isBatch) {
	if (sources == null) sources = new HashSet();
	for (int i = 0; i < files.length; i++) {
		BuildContext context = files[i];
		IFile file = context.getFile();
		IProject project = file.getProject();
		Path path = new Path(buildDir + file.getProjectRelativePath().toPortableString());
		IFile newFile = project.getFile(path);
		sources.add(newFile.getLocation().toPortableString());
		try {
			if (newFile.exists()) {
				newFile.delete(true, null);
			}
			create(newFile.getParent());
			char[] source = context.getContents();
			replace64(source);
			//TODO encoding
//			System.out.println(i + "-" + file);
			newFile.create(new ByteArrayInputStream(new String(source).getBytes()), true, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
}

public void buildFinished(IJavaProject project) {
	try {
		if (sources == null) return;
//		long time = System.currentTimeMillis();
		IProject proj = project.getProject();
		boolean hasProblems = false;
		IMarker[] markers = proj.findMarkers(IJavaModelMarker.JAVA_MODEL_PROBLEM_MARKER, true, IResource.DEPTH_INFINITE);
		for (int i = 0; i < markers.length; i++) {
			IMarker marker = markers[i];
			if (SOURCE_ID.equals(marker.getAttribute(IMarker.SOURCE_ID))) {
				marker.delete();
			} else {
				hasProblems = true;
			}
		}
		if (hasProblems) return;
		String root = proj.getLocation().toPortableString() + buildDir;
		StringBuffer sourcePath = new StringBuffer(), cp = new StringBuffer();
		IClasspathEntry[] entries = project.getResolvedClasspath(true);
		for (int i = 0; i < entries.length; i++) {
			IClasspathEntry entry = entries[i]; 
			String path = entry.getPath().toPortableString();
			if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
				if (path.startsWith(pluginDir)) {
					if (sourcePath.length() > 0) sourcePath.append(File.pathSeparatorChar);
					String dir = root + path.substring(pluginDir.length());
					sourcePath.append(dir);
				}
			} else {
				if (cp.length() > 0) cp.append(File.pathSeparator);
				cp.append(path);
			}
		}
		String log; 
		ArrayList args = new ArrayList();
		args.addAll(Arrays.asList(new String[]{
			"-nowarn",
//			"-d", "none",
			"-cp", cp.toString(),
			"-log", log = (root + "/log.xml"),
			"-sourcepath", sourcePath.toString(),
		}));
		args.addAll(sources);
//		System.out.println("start=" + sources.size());
		PrintWriter writer = new PrintWriter(new ByteArrayOutputStream());
		BatchCompiler.compile((String[])args.toArray(new String[args.size()]), writer, writer, null);
//		System.out.println("time0=" +( System.currentTimeMillis() - time));
		InputStream is = new BufferedInputStream(new FileInputStream(log));
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(is));
		is.close();
		proj.findMember(new Path(buildDir)).refreshLocal(IResource.DEPTH_INFINITE, null);		
		String projPath = proj.getLocation().toPortableString();
		NodeList stats = doc.getDocumentElement().getElementsByTagName("stats");
		if (stats.getLength() > 0) {
			NodeList summary = ((Element)stats.item(0)).getElementsByTagName("problem_summary");
			if (summary.getLength() > 0) {
				String errors = ((Element)summary.item(0)).getAttribute("errors");
				if (Integer.parseInt(errors) > 0) {
					NodeList sources = doc.getDocumentElement().getElementsByTagName("sources");
					for (int i = 0; i < sources.getLength(); i++) {
						NodeList src = ((Element)sources.item(i)).getElementsByTagName("source");
						for (int j = 0; j < src.getLength(); j++) {
							Element source = (Element)src.item(j);
							NodeList problems = source.getElementsByTagName("problems");
							for (int k = 0; k < problems.getLength(); k++) {
								NodeList problem = ((Element)problems.item(k)).getElementsByTagName("problem");
								for (int l = 0; l < problem.getLength(); l++) {
									Element node = (Element)problem.item(l);
									String path = source.getAttribute("path");
									path = path.replaceAll(buildDir, "/");
									if (path.startsWith(projPath)) {
										path = path.substring(projPath.length());
									}
									IResource resource = proj.findMember(new Path(path));
									if (resource != null) {
										int start = Integer.parseInt(node.getAttribute("charStart"));
										int end = Integer.parseInt(node.getAttribute("charEnd"));
										String message = "[64] " + ((Element)node.getElementsByTagName("message").item(0)).getAttribute("value");
										createProblemFor(resource, start, end, message);
									}
								}
							}
						}
					}
				}
			}
		}
//		System.out.println("copiling time=" +( System.currentTimeMillis() - time));
	} catch (Exception e) {
		e.printStackTrace();
	}
}

public void cleanStarting(IJavaProject project) {
	if (!isActive(project)) return;
//	System.out.println("clean");
	sources = null;
	IResource resource = project.getProject().findMember(new Path(buildDir));
	if (resource != null) {
		try {
			resource.delete(true, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
}

public boolean isActive(IJavaProject project) {
	if (project.getProject().getName().equals("org.eclipse.swt")) {
		return true;
	}
	return super.isActive(project);
}

}
