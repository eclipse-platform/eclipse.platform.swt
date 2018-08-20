/*******************************************************************************
 * Copyright (c) 2004, 2018 IBM Corporation and others.
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
import java.lang.reflect.*;
import java.util.*;

public abstract class JNIGenerator implements Flags {

	JNIClass mainClass;
	JNIClass[] classes;
	MetaData metaData;
	String delimiter;
	PrintStream output;
	ProgressMonitor progress;
	
	static final String JNI64 = "JNI64";

public JNIGenerator() {
	delimiter = System.getProperty("line.separator");
	output = System.out;
	metaData = new MetaData(new Properties());
}

public static String skipCopyrights(InputStream is) throws IOException {
	int state = 0;
	StringBuilder copyrights = new StringBuilder();
	while (state != 5) {
		int c = is.read();
		if (c == -1) return null;
		switch (state) {
			case 0:
				if (!Character.isWhitespace((char)c)) state = 1;
			case 1:
				if (c == '/') state = 2;
				else return null;
				break;
			case 2:
				if (c == '*') state = 3;
				else return null;
				break;
			case 3:
				if (c == '*') state = 4;
				break;
			case 4:
				if (c == '/') state = 5;
				else state = 3;
				break;
		}
		if (state > 0) copyrights.append((char)c);
	}
	return copyrights.toString();
}

public static boolean compare(InputStream is1, InputStream is2) throws IOException {
	skipCopyrights(is1);
	skipCopyrights(is2);
	while (true) {
		int c1 = is1.read();
		int c2 = is2.read();
		if (c1 != c2) return false;
		if (c1 == -1) break;
	}
	return true;
}

public static void output(byte[] bytes, String fileName) throws IOException {
	try (FileInputStream is = new FileInputStream(fileName)){
		if (compare(new ByteArrayInputStream(bytes), new BufferedInputStream(is))) return;
	} catch (FileNotFoundException e) {
	}
	try (FileOutputStream out = new FileOutputStream(fileName)) {
		out.write(bytes);
	}
}

public static String getDelimiter(String fileName) {
	
	try (InputStream is = new BufferedInputStream(new FileInputStream(fileName))){
		int c;
		while ((c = is.read()) != -1) {
			if (c == '\n') return "\n";
			if (c == '\r') {
				int c1 = is.read();
				if (c1 == '\n') {
					return "\r\n";
				}
				return "\r";
			}
		}
	} catch (IOException e) {
	}
	return System.getProperty("line.separator");
}

String fixDelimiter(String str) {
	if (delimiter.equals("\n")) return str;
	int index = 0, length = str.length();
	StringBuilder buffer = new StringBuilder();
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
		StringBuilder buffer = new StringBuilder();
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
	try (FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr)){
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

static void sort(JNIMethod[] methods) {
	Arrays.sort(methods, (mth1, mth2) -> {
		int result = mth1.getName().compareTo(mth2.getName());
		return result != 0 ? result : getFunctionName(mth1).compareTo(getFunctionName(mth2));
	});
}

static void sort(JNIField[] fields) {
	Arrays.sort(fields, (a, b) -> a.getName().compareTo(b.getName()));
}

static void sort(JNIClass[] classes) {
	Arrays.sort(classes, (a, b) -> a.getName().compareTo(b.getName()));	
}

static String[] split(String str, String separator) {
	StringTokenizer tk = new StringTokenizer(str, separator);
	List<String> result = new ArrayList<>();
	while (tk.hasMoreTokens()) {
		result.add(tk.nextToken());
	}
	return result.toArray(new String[result.size()]);
}

static String toC(String str) {
	int length = str.length();
	StringBuilder buffer = new StringBuilder(length * 2);
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

public void generateAutoGenNote() {
	outputln("/* Note: This file was auto-generated by " + JNIGenerator.class.getName() + " */");
	outputln("/* DO NOT EDIT - your changes will be lost. */");
	outputln();
}

public void generateIncludes() {
}

public void generate() {
	if (classes == null) return;
	generateCopyright();
	generateAutoGenNote();
	generateIncludes();
	sort(classes);
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
	for (int i = 0; i < classes.length; i++) {
		JNIClass clazz = classes[i];
		if (clazz.getFlag(FLAG_CPP)) {
			return true;
		}
	}
	return false;
}

public String getDelimiter() {
	return delimiter;
}

public String getExtension() {
	return getCPP() ? ".cpp" : getM() ? ".m" : ".c";
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

public boolean getM() {
	for (int i = 0; i < classes.length; i++) {
		JNIClass clazz = classes[i];
		if (clazz.getFlag(FLAG_M)) {
			return true;
		}
	}
	return false;
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
