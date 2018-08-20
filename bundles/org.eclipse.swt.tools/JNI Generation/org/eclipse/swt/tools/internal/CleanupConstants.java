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
 *     Red Hat Inc. - generification
 *******************************************************************************/
package org.eclipse.swt.tools.internal;

import java.lang.reflect.*;

public class CleanupConstants extends CleanupClass {

String getFieldValue(JNIField field) {
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

@Override
public void generate(JNIClass clazz) {
	unusedCount = usedCount = 0;
	super.generate(clazz);
	JNIField[] fields = clazz.getDeclaredFields();
	generate(fields);
	output("used=" + usedCount + " unused=" + unusedCount + " total=" + (unusedCount + usedCount));
}

public void generate(JNIField[] fields) {
	sort(fields);
	for (int i = 0; i < fields.length; i++) {
		JNIField field = fields[i];
		if ((field.getModifiers() & Modifier.FINAL) == 0) continue;
		generate(field);
	}
}

public void generate(JNIField field) {
	String name = field.getName();
	for (String str : files.values()) {
		if (str.indexOf(name) != -1) {
			int modifiers = field.getModifiers();
			String modifiersStr = Modifier.toString(modifiers);
			output("\t");
			output(modifiersStr);
			if (modifiersStr.length() > 0) output(" ");
			output(field.getType().getTypeSignature3(false));
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
	//output("NOT USED=" + field.toString() + " \n");
}

public static void main(String[] args) {
	if (args.length < 3) {
		System.out.println("Usage: java CleanupConstants <OS className> <class source> <src path1> <src path2>");
		return;
	}
	try {
		CleanupConstants gen = new CleanupConstants();
		String clazzName = args[0];
		String classSource = args[1];
		String[] sourcePath = new String[args.length - 2];
		System.arraycopy(args, 2, sourcePath, 0, sourcePath.length);
		Class<?> clazz = Class.forName(clazzName);
		gen.setSourcePath(sourcePath);
		gen.setClassSourcePath(classSource);
		gen.generate(new ReflectClass(clazz));
	} catch (Exception e) {
		System.out.println("Problem");
		e.printStackTrace(System.out);
	}
}

}
