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

import java.lang.reflect.*;
import java.util.*;

public class SizeofGenerator extends JNIGenerator {

public void generate(Class[] classes) {
	output("int main() {");
	outputDelimiter();
	super.generate(classes);
	output("}");
	outputDelimiter();
}

public void generate(Class clazz) {
	String className = getClassName(clazz);
	output("\tprintf(\"");
	output(className);
	output("=%d\\n\", sizeof(");
	output(className);
	output("));");
	outputDelimiter();
//	Field[] fields = clazz.getDeclaredFields();
//	generate(fields);	
}

public void generate(Field[] fields) {
	Arrays.sort(fields, new Comparator() {
		public int compare(Object a, Object b) {
			return ((Field)a).getName().compareTo(((Field)b).getName());
		}
	});
	
	for (int i = 0; i < fields.length; i++) {
		Field field = fields[i];
		if ((field.getModifiers() & Modifier.FINAL) == 0) continue;
		generate(field);
	}
}

public void generate(Field field) {
	output("\tprintf(\"");
	output(field.getName());
	output("=%d\\n\", sizeof(");
	output(field.getName());
	output("));");
	outputDelimiter();
}

public static void main(String[] args) {
	if (args.length < 1) {
		System.out.println("Usage: java SizeofGenerator <className1> <className2>");
		return;
	}
	try {
		ConstantsGenerator gen = new ConstantsGenerator();
		for (int i = 0; i < args.length; i++) {
			String clazzName = args[i];
			Class clazz = Class.forName(clazzName);
			gen.generate(clazz);
		}
	} catch (Exception e) {
		System.out.println("Problem");
		e.printStackTrace(System.out);
	}
}

}
