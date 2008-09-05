/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tools.internal;

import java.io.*;
import java.lang.reflect.Modifier;
import java.util.*;

public abstract class JNIGenerator implements Flags {

	JNIClass mainClass;
	JNIClass[] classes;
	MetaData metaData;
	boolean isCPP;
	String delimiter;
	PrintStream output;
	ProgressMonitor progress;
	
	static final String JNI64 = "JNI64";

public JNIGenerator() {
	delimiter = System.getProperty("line.separator");
	output = System.out;
	metaData = new MetaData(new Properties());
}

String fixDelimiter(String str) {
	if (delimiter.equals("\n")) return str;
	int index = 0, length = str.length();
	StringBuffer buffer = new StringBuffer();
	while (index != -1) {
		int start = index;
		index = str.indexOf('\n', start);
		if (index == -1) {
			buffer.append(str.substring(start, length));
		} else {
			buffer.append(str.substring(start, index));
			buffer.append(delimiter);
			index++;
		}
	}
	return buffer.toString();
}

static String getFunctionName(JNIMethod method) {
	return getFunctionName(method, method.getParameterTypes());
}

static String getFunctionName(JNIMethod method, JNIType[] paramTypes) {
	if ((method.getModifiers() & Modifier.NATIVE) == 0) return method.getName();
	String function = toC(method.getName());
	if (!method.isNativeUnique()) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(function);
		buffer.append("__");
		for (int i = 0; i < paramTypes.length; i++) {
			JNIType paramType = paramTypes[i];
			buffer.append(toC(paramType.getTypeSignature(false)));
		}
		return buffer.toString();
	}
	return function;
}

static String loadFile (String file) {
	try {
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		StringBuffer str = new StringBuffer();
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

static void sort(JNIMethod[] methods) {
	Arrays.sort(methods, new Comparator() {
		public int compare(Object a, Object b) {
			JNIMethod mth1 = (JNIMethod)a;
			JNIMethod mth2 = (JNIMethod)b;
			int result = mth1.getName().compareTo(mth2.getName());
			return result != 0 ? result : getFunctionName(mth1).compareTo(getFunctionName(mth2));
		}
	});
}

static void sort(JNIField[] fields) {
	Arrays.sort(fields, new Comparator() {
		public int compare(Object a, Object b) {
			return ((JNIField)a).getName().compareTo(((JNIField)b).getName());
		}
	});
}

static void sort(JNIClass[] classes) {
	Arrays.sort(classes, new Comparator() {
		public int compare(Object a, Object b) {
			return ((JNIClass)a).getName().compareTo(((JNIClass)b).getName());
		}
	});	
}

static String[] split(String str, String separator) {
	StringTokenizer tk = new StringTokenizer(str, separator);
	ArrayList result = new ArrayList();
	while (tk.hasMoreElements()) {
		result.add(tk.nextElement());
	}
	return (String[])result.toArray(new String[result.size()]);
}

static String toC(String str) {
	int length = str.length();
	StringBuffer buffer = new StringBuffer(length * 2);
	for (int i = 0; i < length; i++) {
		char c = str.charAt(i);
		switch (c) {
			case '_': buffer.append("_1"); break;
			case ';': buffer.append("_2"); break;
			case '[': buffer.append("_3"); break;
			case '.': buffer.append("_"); break;
			case '/': buffer.append("_"); break;
			default: buffer.append(c);
		}
	}
	return buffer.toString();
}

public abstract void generate(JNIClass clazz);

public void generateCopyright() {
}

public void generateIncludes() {
}

public void generate() {
	if (classes == null) return;
	generateCopyright();
	generateIncludes();
	sort(classes);
	for (int i = 0; i < classes.length; i++) {
		JNIClass clazz = classes[i];
		if (clazz.getFlag(FLAG_CPP)) {
			isCPP = true;
			break;
		}
	}
	for (int i = 0; i < classes.length; i++) {
		JNIClass clazz = classes[i];
		if (getGenerate(clazz)) generate(clazz);
		if (progress != null) progress.step();
	}
	output.flush();
}

public void generateMetaData(String key) {
	MetaData mt = getMetaData();
	String data = mt.getMetaData(key, null);
	if (data == null) return;
	if (data.length() == 0) return;
	outputln(fixDelimiter(data));
}

public JNIClass[] getClasses() {
	return classes;
}

public boolean getCPP() {
	return isCPP;
}

public String getDelimiter() {
	return delimiter;
}

public String getExtension() {
	return getCPP() ? ".cpp" : ".c";
}

public String getFileName() {
	return getOutputName() + getSuffix() + getExtension();
}

protected boolean getGenerate(JNIItem item) {
	return item.getGenerate();
}

public PrintStream getOutput() {
	return output;
}

public String getOutputName() {
	return getMainClass().getSimpleName().toLowerCase();
}

public JNIClass getMainClass() {
	return mainClass;
}

public MetaData getMetaData() {
	return metaData;
}

public ProgressMonitor getProgressMonitor() {
	return progress;
}

public String getSuffix() {
	return "";
}

public void output(String str) {
	output.print(str);
}

public void outputln() {
	output(getDelimiter());
}

public void outputln(String str) {
	output(str);
	output(getDelimiter());
}

public void setClasses(JNIClass[] classes) {
	this.classes = classes;
}

public void setDelimiter(String delimiter) {
	this.delimiter = delimiter;
}

public void setMainClass(JNIClass mainClass) {
	this.mainClass = mainClass;
}

public void setMetaData(MetaData data) {
	metaData = data;
}

public void setOutput(PrintStream output) {
	this.output = output;
}

public void setProgressMonitor(ProgressMonitor progress) {
	this.progress = progress;
}

}
