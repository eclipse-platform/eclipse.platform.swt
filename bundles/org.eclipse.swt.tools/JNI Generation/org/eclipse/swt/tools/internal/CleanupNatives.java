/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tools.internal;

import java.util.*;
import java.lang.reflect.*;

public class CleanupNatives extends CleanupClass {
	
public CleanupNatives() {
}

String[] getArgNames(Method method) {
	int n_args = method.getParameterTypes().length;
	if (n_args == 0) return new String[0];
	String name = method.getName();
	String params = "";
	int index = 0;
	while (true) {
		index = classSource.indexOf(name, index + 1);
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

public void generate(Class clazz) {
	unusedCount = usedCount = 0;
	super.generate(clazz);
	Method[] methods = clazz.getDeclaredMethods();
	generate(methods);
	output("used=" + usedCount + " unused=" + unusedCount + " total=" + (unusedCount + usedCount));
}

public void generate(Method[] methods) {
	sort(methods);	
	for (int i = 0; i < methods.length; i++) {
		Method method = methods[i];
		if ((method.getModifiers() & Modifier.NATIVE) == 0) continue;
		generate(method);
	}
}

public void generate(Method method) {
	String name = method.getName();
	Enumeration keys = files.keys();
	while (keys.hasMoreElements()) {
		Object key = keys.nextElement();
		String str = (String)files.get(key);
		if (str.indexOf(name) != -1) {
			int modifiers = method.getModifiers();
			Class clazz = method.getDeclaringClass();
			String modifiersStr = Modifier.toString(modifiers);
			output(modifiersStr);
			if (modifiersStr.length() > 0) output(" ");
			output(getTypeSignature3(method.getReturnType()));
			output(" " );
			output(method.getName());
			output("(");
			Class[] paramTypes = method.getParameterTypes();
			String[] paramNames = getArgNames(method);
			for (int i = 0; i < paramTypes.length; i++) {
				Class paramType = paramTypes[i];
				if (i != 0) output(", ");
				String sig = getTypeSignature3(paramType);
				if (clazz.getPackage().equals(paramType.getPackage())) sig = getClassName(paramType);
				output(sig);
				output(" ");
				output(paramNames[i]);
			}
			outputln(");");
			usedCount++;
			return;
		}
	}
	unusedCount++;
//	output("NOT USED=" + method.toString() + "\n");
}

public static void main(String[] args) {
	if (args.length < 2) {
		System.out.println("Usage: java CleanupNatives <OS className> <src path> <class source>");
		return;
	}
	try {
		CleanupNatives gen = new CleanupNatives();
		String clazzName = args[0];
		String[] sourcePath = new String[]{args[1]};
		String classSource = args[2]; 
		Class clazz = Class.forName(clazzName);
		gen.setSourcePath(sourcePath);
		gen.setClassSourcePath(classSource);
		gen.generate(clazz);
	} catch (Exception e) {
		System.out.println("Problem");
		e.printStackTrace(System.out);
	}
}

}
