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

import java.lang.reflect.*;
import java.util.*;

public class CleanupConstants extends CleanupClass {

String getFieldValue(Field field) {
	String name = field.getName();
	int index = 0;
	while (true) {
		index = classSource.indexOf(name, index + 1);
		if (index == -1) return null;
		int equalsIndex = classSource.indexOf("=", index);
		if (classSource.substring(index + name.length(), equalsIndex).trim().length() == 0) {
			int semiIndex = classSource.indexOf(";", equalsIndex);
 			return classSource.substring(equalsIndex + 1, semiIndex).trim();
		}
	}
}

public void generate(Class clazz) {
	unusedCount = usedCount = 0;
	super.generate(clazz);
	Field[] fields = clazz.getDeclaredFields();
	generate(fields);
	output("used=" + usedCount + " unused=" + unusedCount + " total=" + (unusedCount + usedCount));
}

public void generate(Field[] fields) {
	sort(fields);
	for (int i = 0; i < fields.length; i++) {
		Field field = fields[i];
		if ((field.getModifiers() & Modifier.FINAL) == 0) continue;
		generate(field);
	}
}

public void generate(Field field) {
	String name = field.getName();
	Enumeration keys = files.keys();
	while (keys.hasMoreElements()) {
		Object key = keys.nextElement();
		String str = (String)files.get(key);
		if (str.indexOf(name) != -1) {
			int modifiers = field.getModifiers();
			String modifiersStr = Modifier.toString(modifiers);
			output("\t");
			output(modifiersStr);
			if (modifiersStr.length() > 0) output(" ");
			output(getTypeSignature3(field.getType()));
			output(" " );
			output(field.getName());
			output(" = ");
			output(getFieldValue(field));
			outputln(";");
			usedCount++;
			return;
		}
	}
	unusedCount++;
//	output("NOT USED=" + field.toString() + " \n");
}

public static void main(String[] args) {
	if (args.length < 2) {
		System.out.println("Usage: java CleanupConstants <OS className> <src path> <class source>");
		return;
	}
	try {
		CleanupConstants gen = new CleanupConstants();
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
