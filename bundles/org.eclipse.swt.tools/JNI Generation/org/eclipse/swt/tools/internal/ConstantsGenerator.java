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

public class ConstantsGenerator extends JNIGenerator {

public void generate(JNIClass clazz) {
	JNIField[] fields = clazz.getDeclaredFields();
	generate(fields);
}

public void generate(JNIField[] fields) {
	sort(fields);
	outputln("int main() {");
	for (int i = 0; i < fields.length; i++) {
		JNIField field = fields[i];
		if ((field.getModifiers() & Modifier.FINAL) == 0) continue;
		generate(field);
	}
	outputln("}");
}

public void generate(JNIField field) {
	JNIType type = field.getType();
	output("\tprintf(\"public static final ");
	output(field.getType().getTypeSignature3(false));
	output(" ");
	output(field.getName());
	output(" = ");
	if (type.isType("java.lang.String") || type.isType("[B")) output("\"%s\"");
	else output("0x%x");
	output(";\\n\", ");
	output(field.getName());
	outputln(");");
}

public static void main(String[] args) {
	if (args.length < 1) {
		System.out.println("Usage: java ConstantsGenerator <className1> <className2>");
		return;
	}
	try {
		ConstantsGenerator gen = new ConstantsGenerator();
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
