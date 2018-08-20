/*******************************************************************************
 * Copyright (c) 2008, 2018 IBM Corporation and others.
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
package org.eclipse.swt.tools.builders;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.compiler.*;
import org.eclipse.jdt.core.compiler.batch.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.swt.tools.*;
import org.w3c.dom.*;
import org.xml.sax.*;

public class Check64CompilationParticipant extends CompilationParticipant {
	HashSet<String> sources;

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
	static final String plugin = "org.eclipse.swt";
	static final String SOURCE_ID = "JNI";
	static final String CHECK_64_ENABLED = Activator.PLUGIN_ID + "CHECK_64_ENABLED";

static String loadFile (String file) {
	if (file == null) return null;
	try (FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr)) {
		StringBuilder str = new StringBuilder();
		char[] buffer = new char[1024];
		int read;
		while ((read = br.read(buffer)) != -1) {
			str.append(buffer, 0, read);
		}
		fr.close();
		return str.toString();
	} catch (IOException e) {
		throw new RuntimeException("File not found:" + file, e);
	}
}

void build(IJavaProject project, String root) throws CoreException {
	try {
		StringBuilder sourcePath = new StringBuilder(), cp = new StringBuilder();
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
		String bin = root + "/bin";
		if (cp.length() > 0) cp.append(File.pathSeparator);
		cp.append(bin);
		ArrayList<String> args = new ArrayList<>();
		args.addAll(Arrays.asList(new String[]{
			"-nowarn",
			"-1.8",
//			"-verbose",
			"-d", bin,
			"-cp", cp.toString(),
			"-log", root + "/log.xml",
			"-sourcepath", sourcePath.toString(),
		}));
		args.addAll(sources);
		try (PrintWriter writer = new PrintWriter(new BufferedOutputStream(new FileOutputStream(root + "/out.txt")))) {
			BatchCompiler.compile(args.toArray(new String[args.size()]), writer, writer, null);
		}
		project.getProject().findMember(new Path(buildDir)).refreshLocal(IResource.DEPTH_INFINITE, null);
	} catch (Exception e) {
		throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Problem building 64-bit code", e));
	}
}

void create(IContainer file) throws CoreException {
	if (file.exists()) return;
	switch (file.getType()) {
		case IResource.FOLDER:
			create(file.getParent());
			((IFolder)file).create(true, true, null);
	}
}

IResource getResourceWithoutErrors(IProject proj, String path, boolean deleteJNI) throws CoreException {
	path = path.replaceAll(buildDir, "/");
	String projPath = proj.getLocation().toPortableString();
	if (path.startsWith(projPath)) {
		path = path.substring(projPath.length());
	}
	IResource resource = proj.findMember(new Path(path));
	boolean hasProblems = false;
	IMarker[] markers = resource.findMarkers(IJavaModelMarker.JAVA_MODEL_PROBLEM_MARKER, true, IResource.DEPTH_INFINITE);
	for (int m = 0; m < markers.length; m++) {
		IMarker marker = markers[m];
		if (SOURCE_ID.equals(marker.getAttribute(IMarker.SOURCE_ID))) {
			if (deleteJNI) marker.delete();
		} else {
			Object severity = marker.getAttribute(IMarker.SEVERITY);
			hasProblems |= severity != null && ((Integer)severity).intValue() == IMarker.SEVERITY_ERROR;
		}
	}
	return hasProblems ? null : resource;
}

void createProblem(IResource resource, String message, int start, int end) throws CoreException {
	IMarker marker = resource.createMarker(IJavaModelMarker.JAVA_MODEL_PROBLEM_MARKER);
	int severity = IMarker.SEVERITY_ERROR;
	marker.setAttributes(
		new String[] {IMarker.MESSAGE, IMarker.SEVERITY, IMarker.CHAR_START, IMarker.CHAR_END, IMarker.SOURCE_ID},
		new Object[] {"[32/64] " + message, Integer.valueOf(severity), Integer.valueOf(start), Integer.valueOf(end), SOURCE_ID});
}

void createProblems(IJavaProject project, String root) throws CoreException {
	try (InputStream is = new BufferedInputStream(new FileInputStream(root + "/log.xml"))) {
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(is));
		is.close();
		IProject proj = project.getProject();	
		NodeList sources = doc.getDocumentElement().getElementsByTagName("sources");
		for (int i = 0; i < sources.getLength(); i++) {
			NodeList src = ((Element)sources.item(i)).getElementsByTagName("source");
			for (int j = 0; j < src.getLength(); j++) {
				Element source = (Element)src.item(j);
				String path = source.getAttribute("path").replace('\\', '/');
				IResource resource = getResourceWithoutErrors(proj, path, true);
				if (resource != null) {
					NodeList problems = source.getElementsByTagName("problems");
					for (int k = 0; k < problems.getLength(); k++) {
						NodeList problem = ((Element)problems.item(k)).getElementsByTagName("problem");
						for (int l = 0; l < problem.getLength(); l++) {
							Element node = (Element)problem.item(l);
							if (resource != null) {
								int start = Integer.parseInt(node.getAttribute("charStart"));
								int end = Integer.parseInt(node.getAttribute("charEnd"));
								String message = ((Element)node.getElementsByTagName("message").item(0)).getAttribute("value");
								createProblem(resource, message, start, end);
							}
						}
					}
				}
			}
		}
	} catch (Exception e) {
		throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Problem creating 64-bit problems", e));
	}
}

String resolvePath(String sourcePath, String simpleName) {
	String basePath = sourcePath.substring(0, sourcePath.lastIndexOf("/"));
	File file = new File(basePath + "/" + simpleName + ".java");
	if (file.exists()) {
		return file.getAbsolutePath();
	}
//	System.out.println("failed=" + simpleName + " " + sourcePath);
	return null;
}

TypeDeclaration loadType(HashMap<String, TypeDeclaration> cache, String path) {
	if (path == null) return null;
	Object value = cache.get(path);
	if (value != null) return (TypeDeclaration)value;
	ASTParser parser = ASTParser.newParser(AST.JLS8);
	parser.setSource(loadFile(path).toCharArray());
	CompilationUnit unit = (CompilationUnit)parser.createAST(null);
	TypeDeclaration type = (TypeDeclaration)unit.types().get(0);
	cache.put(path, type);
	return type;
}

boolean is64Type(String type) {
	return type.equals("int") || type.equals("long") || type.equals("float") || type.equals("double") || 
			type.equals("int[]") || type.equals("long[]") || type.equals("float[]") || type.equals("double[]");
}

void createBadOverwrittenMethodProblems(IJavaProject project) throws CoreException {
	if (sources == null) return;
	IProject proj = project.getProject();
	HashMap<String, TypeDeclaration> cache = new HashMap<>();
	for (String path: sources) {
		IResource resource = getResourceWithoutErrors(proj, path, false);
		if (resource == null) continue;
		TypeDeclaration type = loadType(cache, path);
		MethodDeclaration[] methods = type.getMethods();
		List<TypeDeclaration> superclasses = new ArrayList<>();
		TypeDeclaration temp = type;
		while (true) {
			Type supertype = temp.getSuperclassType();
			if (supertype == null) break;
			TypeDeclaration stype = loadType(cache, resolvePath(path, supertype.toString()));
			if (stype == null) break;
			superclasses.add(stype);
			temp = stype;
		}
		for (int i = 0; i < methods.length; i++) {
			MethodDeclaration method = methods[i];
			for (TypeDeclaration supertype : superclasses) {
				MethodDeclaration[] supermethods = supertype.getMethods();
				for (int j = 0; j < supermethods.length; j++) {
					MethodDeclaration supermethod = supermethods[j];
					if (method.getName().getIdentifier().equals(supermethod.getName().getIdentifier()) && method.parameters().size() == supermethod.parameters().size()) {
						List<SingleVariableDeclaration> mParams = method.parameters();
						List<SingleVariableDeclaration> sParams = supermethod.parameters();
						for (int k=0; k<sParams.size(); k++) {
							SingleVariableDeclaration p1 = mParams.get(k);
							SingleVariableDeclaration p2 = sParams.get(k);
							String r1 = p1.getType().toString();
							String r2 = p2.getType().toString();
							if (is64Type(r1) && is64Type(r2)) {
								if (!r1.equals(r2) && p1.getName().getIdentifier().equals(p2.getName().getIdentifier())) {
									String message = "\"" + p1.getName().getIdentifier() + "\" parameter type does not match super declaration";
									createProblem(resource, message, p1.getStartPosition(), p1.getStartPosition() + p1.toString().length());
								}
							}
						}
					}
				}
			}
		}
	}
}

boolean replace(char[] source, char[] src, char[] dest) {
	boolean changed = false;
	int start = 0;
	while (start < source.length) {
		int index = CharOperation.indexOf(src, source, true, start);
		if (index == -1) break;
		changed |= true;
		System.arraycopy(dest, 0, source, index, dest.length);
		start = index + 1;
	}
	return changed;
}

boolean replace(char[] source) {
	boolean changed = false;
	changed |= replace(source, LONG_INT, INT_LONG);
	changed |= replace(source, LONG_INT_ARRAY, INT_LONG_ARRAY);
	changed |= replace(source, DOUBLE_FLOAT, FLOAT_DOUBLE);
	changed |= replace(source, DOUBLE_FLOAT_ARRAY, FLOAT_DOUBLE_ARRAY);
	if (!changed) {
		changed |= replace(source, INT_LONG, LONG_INT);
		changed |= replace(source, INT_LONG_ARRAY, LONG_INT_ARRAY);
		changed |= replace(source, FLOAT_DOUBLE, DOUBLE_FLOAT);
		changed |= replace(source, FLOAT_DOUBLE_ARRAY, DOUBLE_FLOAT_ARRAY);
	}
	return changed;
}

public static boolean getEnabled() {
	return Activator.getDefault().getPreferenceStore().getBoolean(CHECK_64_ENABLED);
}

public static void setEnabled(boolean enabled) {
	Activator.getDefault().getPreferenceStore().setValue(CHECK_64_ENABLED, enabled);
}

boolean is64bit(IJavaProject project) {
	try {
		IClasspathEntry[] entries = project.getResolvedClasspath(true);
		for (int i = 0; i < entries.length; i++) {
			IClasspathEntry entry = entries[i];
			if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
				String path = entry.getPath().toPortableString();
				if (path.equals(pluginDir + "Eclipse SWT PI/win32") || 
					path.equals(pluginDir + "Eclipse SWT PI/cocoa") || 
					path.equals(pluginDir + "Eclipse SWT PI/gtk")
				) 
				{
					return true;
				}
			}
		}
	} catch (JavaModelException e) {}
	return false;
}

@Override
public void buildFinished(IJavaProject project) {
	try {
		if (sources == null) return;
		if (!getEnabled() || !is64bit(project)) return;
//		long time = System.currentTimeMillis();
		String root = project.getProject().getLocation().toPortableString() + buildDir;
		build(project, root);		
		createProblems(project, root);
		createBadOverwrittenMethodProblems(project);
		sources = null;
//		System.out.println("compiling time=" + (System.currentTimeMillis() - time));
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	
@Override
public void buildStarting(BuildContext[] files, boolean isBatch) {
	if (sources == null) sources = new HashSet<>();
//	long time = System.currentTimeMillis();
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
			replace(source);
			newFile.create(new ByteArrayInputStream(new String(source).getBytes()), true, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
//	System.out.println("copying time=" + (System.currentTimeMillis() - time));	
}

@Override
public void cleanStarting(IJavaProject project) {
	if (!isActive(project)) return;
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

@Override
public boolean isActive(IJavaProject project) {
	if (project.getProject().getName().equals(plugin)) {
		return true;
	}
	return super.isActive(project);
}

}
