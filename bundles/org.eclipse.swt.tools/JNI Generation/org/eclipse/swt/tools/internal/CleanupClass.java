/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
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
import java.util.*;

public abstract class CleanupClass extends JNIGenerator {

String classSourcePath;
String[] sourcePath;
String classSource;
Hashtable files;
int usedCount, unusedCount;

String[] getArgNames(JNIMethod method) {
	int n_args = method.getParameters().length;
	if (n_args == 0) return new String[0];
	String name = method.getName();
	String params = "";
	int index = 0;
	while (true) {
		index = classSource.indexOf(name, index + 1);
		if (!Character.isWhitespace(classSource.charAt(index - 1))) continue;
		if (index == -1) return null;
		int parantesesStart = classSource.indexOf("(", index);
		if (classSource.substring(index + name.length(), parantesesStart).trim().length() == 0) {
			int parantesesEnd = classSource.indexOf(")", parantesesStart);
 			params = classSource.substring(parantesesStart + 1, parantesesEnd);
 			break;
		}
	}
	String[] names = new String[n_args];
	StringTokenizer tk = new StringTokenizer(params, ",");
	for (int i = 0; i < names.length; i++) {
		String s = tk.nextToken().trim();
		StringTokenizer tk1 = new StringTokenizer(s, " ");
		String s1 = null;
		while (tk1.hasMoreTokens()) {
			s1 = tk1.nextToken();
		}
		names[i] = s1.trim();
	}
	return names;	
}


void loadClassSource() {
	if (classSourcePath == null) return;
	File f = new File(classSourcePath);
	classSource = loadFile(f);
}

void loadFiles () {
	// BAD - holds on to a lot of memory
	if (sourcePath == null) return;
	files = new Hashtable ();
	for (int i = 0; i < sourcePath.length; i++) {
		File file = new File(sourcePath[i]);
		if (file.exists()) {
			if (!file.isDirectory()) {
				if (file.getAbsolutePath().endsWith(".java")) {
					files.put(file, loadFile(file));
				}
			} else {
				loadDirectory(file);
			}		
		}
	}
}

String loadFile (File file) {
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
		e.printStackTrace(System.out);
	}
	return "";
}

void loadDirectory(File file) {
	String[] entries = file.list();
	for (int i = 0; i < entries.length; i++) {
		String entry = entries[i];
		File f = new File(file, entry);
		if (!f.isDirectory()) {
			if (f.getAbsolutePath().endsWith(".java")) {
				files.put(f, loadFile(f));
			}
		} else {
			loadDirectory(f);
		}					
	}
}

public void generate(JNIClass clazz) {
	loadFiles ();
	loadClassSource();
}

public void setSourcePath(String[] sourcePath) {
	this.sourcePath = sourcePath;
	files = null;
}

public void setClassSourcePath(String classSourcePath) {
	this.classSourcePath = classSourcePath;
}

}
