/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tools.internal;

import java.io.*;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;
import java.util.zip.*;
import java.util.Arrays;

import org.eclipse.swt.SWT;

public class JNIGeneratorApp {

	String mainClass, outputDir, classpath;
	MetaData metaData;

public JNIGeneratorApp() {
}

public String getClasspath() {
	return classpath;
}

public String getMainClass() {
	return mainClass;
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

void generateSTATS_C() {
	try {
		String outputName = getClassName(mainClass).toLowerCase();
		String inc = 
			"#include \"swt.h\"\n" +
			"#include \"" + "os_structs.h\"\n";
		metaData.setMetaData("swt_includes", inc);
		StatsGenerator gen = new StatsGenerator();
		gen.setMetaData(metaData);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream print = new PrintStream(out);
		gen.setOutput(print);
		gen.generateSourceFile(getNativesClasses());
		print.flush();
		if (out.size() > 0) output(out.toByteArray(), outputDir + outputName + "_stats.c");
	} catch (Exception e) {
		System.out.println("Problem");
		e.printStackTrace(System.out);
	}
}

void generateSTATS_H() {
	try {
		String outputName = getClassName(mainClass).toLowerCase();
		String inc = "";
		metaData.setMetaData("swt_includes", inc);
		StatsGenerator gen = new StatsGenerator();
		gen.setMetaData(metaData);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream print = new PrintStream(out);
		gen.setOutput(print);
		gen.generateHeaderFile(getNativesClasses());
		print.flush();
		if (out.size() > 0) output(out.toByteArray(), outputDir + outputName + "_stats.h");
	} catch (Exception e) {
		System.out.println("Problem");
		e.printStackTrace(System.out);
	}
}

void generateSTRUCTS_H() {
	try {
		String outputName = getClassName(mainClass).toLowerCase();
		metaData.setMetaData("swt_includes", "#include \"" + outputName + ".h\"\n");
		StructsGenerator gen = new StructsGenerator();
		gen.setMetaData(metaData);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream print = new PrintStream(out);
		gen.setOutput(print);
		gen.generateHeaderFile(getStructureClasses());
		print.flush();
		if (out.size() > 0) output(out.toByteArray(), outputDir + outputName + "_structs.h");
	} catch (Exception e) {
		System.out.println("Problem");
		e.printStackTrace(System.out);
	}

}

void generateSTRUCTS_C() {
	try {
		String outputName = getClassName(mainClass).toLowerCase();
		String inc = 
			"#include \"swt.h\"\n" +
			"#include \"" + outputName + "_structs.h\"\n";
		metaData.setMetaData("swt_includes", inc);
		StructsGenerator gen = new StructsGenerator();
		gen.setMetaData(metaData);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream print = new PrintStream(out);
		gen.setOutput(print);
		gen.generateSourceFile(getStructureClasses());
		print.flush();
		if (out.size() > 0) output(out.toByteArray(), outputDir + outputName + "_structs.c");
	} catch (Exception e) {
		System.out.println("Problem");
		e.printStackTrace(System.out);
	}

}

void generateSWT_C() {
	try {
		String outputName = getClassName(mainClass).toLowerCase();
		String inc = 
			"#include \"swt.h\"\n" +
			"#include \"" + outputName + "_structs.h\"\n";
		metaData.setMetaData("swt_includes", inc);
		NativesGenerator gen = new NativesGenerator();
		gen.setMetaData(metaData);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream print = new PrintStream(out);
		gen.setOutput(print);
		gen.generate(getNativesClasses());
		print.flush();
		if (out.size() > 0) output(out.toByteArray(), outputDir + outputName + ".c");
	} catch (Exception e) {
		System.out.println("Problem");
		e.printStackTrace(System.out);
	}
}


void generateAllMetaData() {
	try {
		MetaDataGenerator gen = new MetaDataGenerator();
		gen.setMetaData(metaData);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream print = new PrintStream(out);
		gen.setOutput(print);
		gen.generate(getClasses());
		print.flush();
		if (!new File(getMetaDataDir()).exists()) {
			System.out.println("Warning: Meta data output dir does not exist");
			return;
		}
		if (out.size() > 0) output(out.toByteArray(), getMetaDataDir() + mainClass + ".properties");
	} catch (Exception e) {
		System.out.println("Problem");
		e.printStackTrace(System.out);
	}
}

public void generate() {
	if (mainClass == null) return;
	generateSTRUCTS_H();
	generateSTRUCTS_C();
	generateSWT_C();
	generateSTATS_H();
	generateSTATS_C();
	generateAllMetaData();
}

boolean compare(InputStream is1, InputStream is2) throws IOException {
	while (true) {
		int c1 = is1.read();
		int c2 = is2.read();
		if (c1 != c2) return false;
		if (c1 == -1) break;
	}
	return true;
}

void output(byte[] bytes, String fileName) throws IOException {
	FileInputStream is = null;
	try {
		is = new FileInputStream(fileName);
		if (compare(new ByteArrayInputStream(bytes), new BufferedInputStream(is))) return;
	} catch (FileNotFoundException e) {
	} finally {
		try {
			if (is != null) is.close();
		} catch (IOException e) {}
	}
	FileOutputStream out = new FileOutputStream(fileName);
	out.write(bytes);
	out.close();
}

String getClassName(String className) {
	int dot = mainClass.lastIndexOf('.');
	if (dot == -1) return className;
	return mainClass.substring(dot + 1);
}

String getPackageName(String className) {
	int dot = mainClass.lastIndexOf('.');
	if (dot == -1) return "";
	return mainClass.substring(0, dot);
}

String[] getClassNames(String mainClassName) {
	String pkgName = getPackageName(mainClassName);
	String classpath = getClasspath();
	if (classpath == null) classpath = System.getProperty("java.class.path");
	String pkgPath = pkgName.replace('.', File.separatorChar);
	String pkgZipPath = pkgName.replace('.', '/');
	ArrayList classes = new ArrayList();	
	int start = 0;
	int index = 0;
	while (index < classpath.length()) {
		index = classpath.indexOf(File.pathSeparatorChar, start);
		if (index == -1) index = classpath.length();
		String path = classpath.substring(start, index);
		if (path.toLowerCase().endsWith(".jar")) {
			ZipFile zipFile = null;
			try {
				zipFile = new ZipFile(path);
				Enumeration entries = zipFile.entries();
				while (entries.hasMoreElements()) {
					ZipEntry entry = (ZipEntry)entries.nextElement();
					String name = entry.getName();
					if (name.startsWith(pkgZipPath) && name.endsWith(".class")) {
						String className = name.substring(pkgZipPath.length() + 1, name.length() - 6);
						className.replace('/', '.');
						classes.add(className);
					}
				}
			} catch (IOException e) {
			} finally {
				try {
					if (zipFile != null) zipFile.close();
				} catch (IOException ex) {}
			}
		} else {
			File file = new File(path + File.separator + pkgPath);
			if (file.exists()) {
				String[] entries = file.list();
				for (int i = 0; i < entries.length; i++) {
					String entry = entries[i];
					File f = new File(file, entry);
					if (!f.isDirectory()) {
						if (f.getAbsolutePath().endsWith(".class")) {
							String className = entry.substring(0, entry.length() - 6);
							classes.add(className);
						}
					} else {
						System.out.println("SUBDIR NOT DONE=" + f);
					}					
				}
			}
		}
		start = index + 1;
	}
	return (String[])classes.toArray(new String[classes.size()]);
}

public Class[] getClasses() {
	if (mainClass == null) return new Class[0];
	String[] classNames = getClassNames(mainClass);
	Arrays.sort(classNames);
	String packageName = getPackageName(mainClass);
	Class[] classes = new Class[classNames.length];
	for (int i = 0; i < classNames.length; i++) {
		String className = classNames[i];
		try {
			classes[i] = Class.forName(packageName + "." + className, false, getClass().getClassLoader());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	return classes;
}

public Class[] getNativesClasses() {
	if (mainClass == null) return new Class[0];
	ArrayList result = new ArrayList();
	Class[] classes = getClasses();
	for (int i = 0; i < classes.length; i++) {
		Class clazz = classes[i];
		Method[] methods = clazz.getDeclaredMethods();
		for (int j = 0; j < methods.length; j++) {
			Method method = methods[j];
			int mods = method.getModifiers();
			if ((mods & Modifier.NATIVE) != 0) {
				result.add(clazz);
				break;
			}
		}
	}
	return (Class[])result.toArray(new Class[result.size()]);
}

public Class[] getStructureClasses() {
	if (mainClass == null) return new Class[0];
	ArrayList result = new ArrayList();
	Class[] classes = getClasses();
	outer:
	for (int i = 0; i < classes.length; i++) {
		Class clazz = classes[i];
		Method[] methods = clazz.getDeclaredMethods();
		for (int j = 0; j < methods.length; j++) {
			Method method = methods[j];
			int mods = method.getModifiers();
			if ((mods & Modifier.NATIVE) != 0) continue outer;
		}
		result.add(clazz);
	}
	return (Class[])result.toArray(new Class[result.size()]);
}

MetaData loadMetaData() {
	int index = 0;
	Properties propeties = new Properties();
	int length = mainClass.length();
	while (index < length) {
		index = mainClass.indexOf('.', index);
		if (index == -1) index = length;
		try {
			InputStream is = getClass().getResourceAsStream(mainClass.substring(0, index) + ".properties");
			propeties.load(is);
			is.close();
		} catch (Exception e) {
		}
		index++;
	}
	return new MetaData(propeties);
}

public void setClasspath(String classpath) {
	this.classpath = classpath;
}

public void setMainClass(String str) {
	mainClass = str;
	metaData = loadMetaData();
}

public void setOutputDir(String str) {
	outputDir = str;
}

public static String getDefaultMainClass() {
	return "org.eclipse.swt.internal." + getDefaultPlatform() + ".OS";
}

public static String getDefaultOutputDir() {
	return "../org.eclipse.swt/Eclipse SWT PI/" + getDefaultPlatform() + "/library/";
}

public static String getDefaultPlatform() {
	return SWT.getPlatform();
}

public static void main(String[] args) {
	JNIGeneratorApp gen = new JNIGeneratorApp ();
	if (args.length > 0) {
		gen.setMainClass(args[0]);
		if (args.length > 1) gen.setOutputDir(args[1]);
		if (args.length > 2) gen.setClasspath(args[2]);
	} else {
		gen.setMainClass(getDefaultMainClass());
		gen.setOutputDir(getDefaultOutputDir());
	}
	gen.generate();
}

}
