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

public class MetaDataGenerator extends JNIGenerator {

public void generate(Class clazz) {
	output(toC(clazz.getName()));
	output("=");
	ClassData data = getMetaData().getMetaData(clazz);
	if (data != null) output(data.toString());
	outputln();
	Field[] fields = clazz.getDeclaredFields();
	generate(fields);
	Method[] methods = clazz.getDeclaredMethods();
	generate(methods);
	outputln();
}

public void generate(Class[] classes) {
	generateMetaData("swt_properties_copyright");
	super.generate(classes);
}

public void generate(Field[] fields) {
	for (int i = 0; i < fields.length; i++) {
		Field field = fields[i];
		int mods = field.getModifiers();
		if ((mods & Modifier.PUBLIC) == 0) continue;
		if ((mods & Modifier.FINAL) != 0) continue;
		if ((mods & Modifier.STATIC) != 0) continue;
		generate(field);
		outputln();
		if (progress != null) progress.step();
	}
}

public void generate(Field field) {
	output(getClassName(field.getDeclaringClass()));
	output("_");
	output(field.getName());
	output("=");
	FieldData data = getMetaData().getMetaData(field);
	if (data != null) output(data.toString());
}

public void generate(Method[] methods) {
	sort(methods);
	for (int i = 0; i < methods.length; i++) {
		Method method = methods[i];
		if ((method.getModifiers() & Modifier.NATIVE) == 0) continue;
		generate(method);
		outputln();
		if (progress != null) progress.step();
	}
}

public void generate(Method method) {
	StringBuffer buffer = new StringBuffer();
	buffer.append(getClassName(method.getDeclaringClass()));
	buffer.append("_");
	if (isNativeUnique(method)) {
		buffer.append(method.getName());
	} else {
		buffer.append(getFunctionName(method));
	}
	String key = buffer.toString();
	output(key);
	output("=");
	MethodData methodData = getMetaData().getMetaData(method);
	if (methodData != null) output(methodData.toString());
	outputln();
	int length = method.getParameterTypes().length;
	for (int i = 0; i < length; i++) {
		output(key);
		output("_");
		output(i + "=");
		ParameterData paramData = getMetaData().getMetaData(method, i);
		if (paramData != null) output(paramData.toString());
		outputln();		
	}
}

public static void main(String[] args) {
	if (args.length < 1) {
		System.out.println("Usage: java CastGenerator <className1> <className2>");
		return;
	}
	try {
		MetaDataGenerator gen = new MetaDataGenerator();
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
