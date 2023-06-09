/*******************************************************************************
 * Copyright (c) 2004, 2017 IBM Corporation and others.
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
package org.eclipse.swt.tools.internal;

import java.io.*;
import java.util.*;
import java.util.zip.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.swt.*;

public class JNIGeneratorApp {

	JNIClass mainClass;
	JNIClass[] classes;
	ProgressMonitor progress;
	String mainClassName, classesDir, outputDir, classpath;
	MetaData metaData;
	
	static boolean USE_AST = true;

public JNIGeneratorApp() {
}

public String getClasspath() {
	return classpath;
}

public String getClassesDir() {
	return classesDir;
}

public JNIClass getMainClass() {
	return mainClass;
}

public String getMainClassName() {
	return mainClassName;
}

public MetaData getMetaData() {
	return metaData;
}

String getMetaDataDir() {
	return "./JNI Generation/org/eclipse/swt/tools/internal/";
}

public String getOutputDir() {
	return outputDir;
}

public void generateAll() {
	String mainClasses = new MetaData(getDefaultMainClass()).getMetaData("swt_main_classes", null);
	if (mainClasses != null) {
		String[] list = JNIGenerator.split(mainClasses, ",");
		for (int i = 0; i < list.length; i += 2) {
			String className = list[i].trim();
			if (!USE_AST) {
				try {
					Class.forName(className, false, getClass().getClassLoader());
				} catch (Throwable e) {
					continue;
				}
			}
			setMainClassName(className);
			generate();
		}
	}
}

void generateSTATS_C(JNIClass[] classes) {
	try {
		StatsGenerator gen = new StatsGenerator(false);
		gen.setMainClass(mainClass);
		gen.setClasses(classes);
		gen.setMetaData(metaData);
		gen.setProgressMonitor(progress);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		gen.setOutput(new PrintStream(out));
		String fileName = outputDir + gen.getFileName();
		gen.setDelimiter(JNIGenerator.getDelimiter(fileName));
		gen.generate();
		if (out.size() > 0) JNIGenerator.output(out.toByteArray(), fileName);
	} catch (Exception e) {
		System.out.println("Problem");
		e.printStackTrace(System.out);
	}
}

void generateSTATS_H(JNIClass[] classes) {
	try {
		StatsGenerator gen = new StatsGenerator(true);
		gen.setMainClass(mainClass);
		gen.setClasses(classes);
		gen.setMetaData(metaData);
		gen.setProgressMonitor(progress);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		gen.setOutput(new PrintStream(out));
		String fileName = outputDir + gen.getFileName();
		gen.setDelimiter(JNIGenerator.getDelimiter(fileName));
		gen.generate();
		if (out.size() > 0) JNIGenerator.output(out.toByteArray(), fileName);
	} catch (Exception e) {
		System.out.println("Problem");
		e.printStackTrace(System.out);
	}
}

void generateSTRUCTS_H(JNIClass[] classes) {
	try {
		StructsGenerator gen = new StructsGenerator(true);
		gen.setMainClass(mainClass);
		gen.setClasses(classes);
		gen.setMetaData(metaData);
		gen.setProgressMonitor(progress);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		gen.setOutput(new PrintStream(out));
		String fileName = outputDir + gen.getFileName();
		gen.setDelimiter(JNIGenerator.getDelimiter(fileName));
		gen.generate();
		if (out.size() > 0) JNIGenerator.output(out.toByteArray(), fileName);
	} catch (Exception e) {
		System.out.println("Problem");
		e.printStackTrace(System.out);
	}
}

void generateSTRUCTS_C(JNIClass[] classes) {
	try {
		StructsGenerator gen = new StructsGenerator(false);
		gen.setMainClass(mainClass);
		gen.setClasses(classes);
		gen.setMetaData(metaData);
		gen.setProgressMonitor(progress);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		gen.setOutput(new PrintStream(out));
		String fileName = outputDir + gen.getFileName();
		gen.setDelimiter(JNIGenerator.getDelimiter(fileName));
		gen.generate();
		if (out.size() > 0) JNIGenerator.output(out.toByteArray(), fileName);
	} catch (Exception e) {
		System.out.println("Problem");
		e.printStackTrace(System.out);
	}

}

void generateSWT_C(JNIClass[] classes) {
	try {
		NativesGenerator gen = new NativesGenerator();
		gen.setMainClass(mainClass);
		gen.setClasses(classes);
		gen.setMetaData(metaData);
		gen.setProgressMonitor(progress);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		gen.setOutput(new PrintStream(out));
		String fileName = outputDir + gen.getFileName();
		gen.setDelimiter(JNIGenerator.getDelimiter(fileName));
		gen.generate();
		if (out.size() > 0) JNIGenerator.output(out.toByteArray(), fileName);
	} catch (Exception e) {
		System.out.println("Problem");
		e.printStackTrace(System.out);
	}
}


void generateMetaData(JNIClass[] classes) {
	try {
		MetaDataGenerator gen = new MetaDataGenerator();
		gen.setMainClass(mainClass);
		gen.setClasses(classes);
		gen.setMetaData(metaData);
		gen.setProgressMonitor(progress);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		gen.setOutput(new PrintStream(out));
		String fileName = getMetaDataDir() + gen.getFileName();
		if (new File(fileName).exists()) {
			gen.setDelimiter(JNIGenerator.getDelimiter(fileName));
			gen.generate();
			if (!new File(getMetaDataDir()).exists()) {
				System.out.println("Warning: Meta data output dir does not exist");
				return;
			}
			if (out.size() > 0) JNIGenerator.output(out.toByteArray(), fileName);
		}
	} catch (Exception e) {
		System.out.println("Problem");
		e.printStackTrace(System.out);
	}
}

void generateEmbededMetaData(JNIClass[] classes) {
	try {
		EmbedMetaData gen = new EmbedMetaData();
		gen.setMainClass(mainClass);
		gen.setClasses(classes);
		gen.setMetaData(metaData);
		gen.generate();
	} catch (Exception e) {
		System.out.println("Problem");
		e.printStackTrace(System.out);
	}
}

public void generate() {
	System.out.println("Generating \"" + getMainClassName() + "\"");
	generate(null);
}

public void generate(ProgressMonitor progress) {
	if (mainClass == null) return;
	if (progress != null) progress.setMessage("Initializing...");
	JNIClass[] classes = getClasses();
	JNIClass[] natives = getNativesClasses(classes);
	JNIClass[] structs = getStructureClasses(classes);
	this.progress = progress;
	if (progress != null) {
		int nativeCount = 0;
		for (JNIClass clazz : natives) {
			for (JNIMethod method : clazz.getDeclaredMethods()) {
				if ((method.getModifiers() & Modifier.NATIVE) == 0) continue;
				nativeCount++;
			}
		}
		int total = nativeCount * 4;
		total += classes.length;
		total += natives.length * (3);
		total += structs.length * 2;
		progress.setTotal(total);
		progress.setMessage("Generating structs.h ...");
	}
	generateSTRUCTS_H(structs);
	if (progress != null) progress.setMessage("Generating structs.c ...");
	generateSTRUCTS_C(structs);
	if (progress != null) progress.setMessage("Generating natives ...");
	generateSWT_C(natives);
	if (progress != null) progress.setMessage("Generating stats.h ...");
	generateSTATS_H(natives);
	if (progress != null) progress.setMessage("Generating stats.c ...");
	generateSTATS_C(natives);
	if (progress != null) progress.setMessage("Generating meta data ...");
	generateMetaData(classes);
//	if (progress != null) progress.setMessage("Generating embeded meta data ...");
//	generateEmbededMetaData(classes);
	if (progress != null) progress.setMessage("Done.");
	this.progress = null;
}

String getPackageName() {
	int dot = mainClassName.lastIndexOf('.');
	if (dot == -1) return "";
	return mainClassName.substring(0, dot);
}

String[] getClassNames() {
	String pkgName = getPackageName();
	String classpath = getClasspath();
	if (classpath == null) classpath = System.getProperty("java.class.path");
	String pkgPath = pkgName.replace('.', File.separatorChar);
	String pkgZipPath = pkgName.replace('.', '/');
	List<String> classes = new ArrayList<>();	
	int start = 0;
	int index = 0;
	while (index < classpath.length()) {
		index = classpath.indexOf(File.pathSeparatorChar, start);
		if (index == -1) index = classpath.length();
		String path = classpath.substring(start, index);
		if (path.toLowerCase().endsWith(".jar")) {
			try (ZipFile zipFile = new ZipFile(path)){
				Enumeration<? extends ZipEntry> entries = zipFile.entries();
				while (entries.hasMoreElements()) {
					ZipEntry entry = entries.nextElement();
					String name = entry.getName();
					if (name.startsWith(pkgZipPath) && name.indexOf('/', pkgZipPath.length() + 1) == -1 && name.endsWith(".class")) {
						String className = name.substring(pkgZipPath.length() + 1, name.length() - 6);
						className= className.replace('/', '.');
						classes.add(className);
					}
				}
			} catch (IOException e) {
			}
		} else {
			File file = new File(path + File.separator + pkgPath);
			if (file.exists()) {
				String[] entries = file.list();
				if(entries == null) {
					entries = new String[0];
				}
				for (String entry : entries) {
					File f = new File(file, entry);
					if (!f.isDirectory()) {
						if (f.getAbsolutePath().endsWith(".class")) {
							String className = entry.substring(0, entry.length() - 6);
							classes.add(className);
						}
					}					
				}
			}
		}
		start = index + 1;
	}
	return classes.toArray(new String[classes.size()]);
}

public JNIClass[] getClasses() {
	if (classes != null) return classes;
	if (mainClassName == null) return new JNIClass[0];
	if (USE_AST) return getASTClasses();
	String[] classNames = getClassNames();
	Arrays.sort(classNames);
	String packageName = getPackageName();
	JNIClass[] classes = new JNIClass[classNames.length];
	for (int i = 0; i < classNames.length; i++) {
		String className = classNames[i];
		try {
			String qualifiedName = packageName + "." + className;
			if (qualifiedName.equals(mainClassName)) {
				classes[i] = mainClass;
			} else {
				String root = classesDir != null ? classesDir : new File(outputDir).getParent() + "/";
				String sourcePath = root + qualifiedName.replace('.', '/') + ".java";
				classes[i] = new ReflectClass(Class.forName(qualifiedName, false, getClass().getClassLoader()), metaData, sourcePath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	return classes;
}

JNIClass[] getASTClasses() {
	if (classes != null) return classes;
	if (mainClassName == null) return new JNIClass[0];
	String root = classesDir != null ? classesDir : new File(outputDir).getParent() + "/";
	String mainPath = new File(root + mainClassName.replace('.', '/') + ".java").getAbsolutePath();
	List<JNIClass> classes = new ArrayList<>();
	String packageName = getPackageName();
	File dir = new File(root + "/" + packageName.replace('.', '/'));
	File[] files = dir.listFiles();
	if (files == null) {
		files = new File[0];
	}
	for (File file : files) {
		try {
			String path = file.getAbsolutePath().replace('\\', '/');
			if (path.endsWith(".java")) {
				if (mainPath.equals(path)){
					classes.add(mainClass);
				} else {
					try {
						classes.add(new ASTClass(path, metaData));
					} catch (ClassCastException cce) {
						if (cce.getMessage().startsWith(EnumDeclaration.class.getName())) {
							// this can be ignored since enums don't affect native files
						}
					} catch (IndexOutOfBoundsException e) {
						// ignore, can also happen because of enums
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	return classes.toArray(new JNIClass[classes.size()]);
}

public JNIClass[] getNativesClasses(JNIClass[] classes) {
	if (mainClass == null) return new JNIClass[0];
	List<JNIClass> result = new ArrayList<>();
	for (JNIClass clazz : classes) {
		for (JNIMethod method : clazz.getDeclaredMethods()) {
			int mods = method.getModifiers();
			if ((mods & Modifier.NATIVE) != 0) {
				result.add(clazz);
				break;
			}
		}
	}
	return result.toArray(new JNIClass[result.size()]);
}

public JNIClass[] getStructureClasses(JNIClass[] classes) {
	if (mainClass == null) return new JNIClass[0];
	List<JNIClass> result = new ArrayList<>();
	outer:
	for (JNIClass clazz : classes) {
		for (JNIMethod method : clazz.getDeclaredMethods()) {
			int mods = method.getModifiers();
			if ((mods & Modifier.NATIVE) != 0) continue outer;
		}
		boolean hasPublicFields = false;
		for (JNIField field : clazz.getDeclaredFields()) {
			int mods = field.getModifiers();
			if ((mods & Modifier.PUBLIC) != 0 && (mods & Modifier.STATIC) == 0) {
				hasPublicFields = true;
				break;
			}
		}
		if (!hasPublicFields) continue;
		result.add(clazz);
	}
	return result.toArray(new JNIClass[result.size()]);
}

public void setClasspath(String classpath) {
	this.classpath = classpath;
}

public void setMainClass(JNIClass mainClass) {
	this.mainClass = mainClass;
}

public void setMetaData(MetaData data) {
	this.metaData = data;
}

public void setClasses(JNIClass[] classes) {
	this.classes = classes;
}

public void setMainClassName(String str) {
	mainClassName = str;
	metaData = new MetaData(mainClassName);
	String mainClasses = getMetaData().getMetaData("swt_main_classes", null);
	if (mainClasses != null) {
		String[] list = JNIGenerator.split(mainClasses, ",");
		for (int i = 0; i < list.length; i += 2) {
			if (mainClassName.equals(list[i].trim())) {
				setOutputDir(list[i + 1].trim());
			}
		}
	}
	if (mainClassName != null) {
		try {
			String root = classesDir != null ? classesDir : new File(outputDir).getParent() + "/";
			String sourcePath = root + mainClassName.replace('.', '/') + ".java";
			if (USE_AST) {
				mainClass = new ASTClass(sourcePath, metaData);
			} else {
				mainClass = new ReflectClass(Class.forName(mainClassName, false, getClass().getClassLoader()), metaData, sourcePath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
public void setMainClassName(String str, String outputDir) {
	setMainClassName(str, outputDir, null);
}

public void setMainClassName(String str, String outputDir, String classesDir) {
	mainClassName = str;
	setClassesDir(classesDir);
	setOutputDir(outputDir);
	metaData = new MetaData(mainClassName);
	try {
		String root = classesDir != null ? classesDir : new File(outputDir).getParent() + "/";
		String sourcePath = root + mainClassName.replace('.', '/') + ".java";
		if (USE_AST) {
			mainClass = new ASTClass(sourcePath, metaData);
		} else {
			mainClass = new ReflectClass(Class.forName(mainClassName, false, getClass().getClassLoader()), metaData, sourcePath);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
}

public void setClassesDir(String str) {
	if (str != null) {
		if (!str.endsWith("\\") && !str.endsWith("/") ) {
			str += File.separator;
		}
		str = str.replace('\\', '/');
	}
	classesDir = str;
}

public void setOutputDir(String str) {
	if (str != null) {
		if (!str.endsWith("\\") && !str.endsWith("/") ) {
			str += File.separator;
		}
		str = str.replace('\\', '/');
	}
	outputDir = str;
}

public static String getDefaultMainClass() {
	return "org.eclipse.swt.internal." + getDefaultPlatform() + ".OS";
}

public static String getDefaultPlatform() {
	return SWT.getPlatform();
}

public static void main(String[] args) {
	JNIGeneratorApp gen = new JNIGeneratorApp ();
	if (args.length == 1 && (args[0].equals("*") || args[0].equals("all"))) {
		gen.generateAll();
		return;
	}
	if (args.length > 0) {
		gen.setMainClassName(args[0]);
		if (args.length > 1) gen.setOutputDir(args[1]);
		if (args.length > 2) gen.setClasspath(args[2]);
	} else {
		gen.setMainClassName(getDefaultMainClass());
	}
	gen.generate();
}

}
