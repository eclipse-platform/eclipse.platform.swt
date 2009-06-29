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

import java.lang.reflect.Modifier;

public class SizeofGenerator extends JNIGenerator {


public void generate(JNIClass clazz) {
	String className = clazz.getSimpleName();
	output("\tprintf(\"");
	output(className);
	output("=%d\\n\", sizeof(");
	output(className);
	outputln("));");
//	Field[] fields = clazz.getDeclaredFields();
//	generate(fields);	
}
	
public void generate() {
	outputln("int main() {");
	super.generate();
	outputln("}");
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
	output("\tprintf(\"");
	output(field.getName());
	output("=%d\\n\", sizeof(");
	output(field.getName());
	outputln("));");
}

public static void main(String[] args) {
	if (args.length < 1) {
		System.out.println("Usage: java SizeofGenerator <className1> <className2>");
		return;
	}
	try {
		SizeofGenerator gen = new SizeofGenerator();
		for (int i = 0; i < args.length; i++) {
			String clazzName = args[i];
			Class clazz = Class.forName(clazzName);
			gen.generate(new ReflectClass(clazz));
		}
	} catch (Exception e) {
		System.out.println("Problem");
		e.printStackTrace(System.out);
	}
}

}
