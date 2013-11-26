/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
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
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.ArrayList;

public class EmbedMetaData extends JNIGenerator {
	TreeMap<Integer, String> inserts;

@Override
public void generate(JNIClass clazz) {
	inserts = new TreeMap<Integer, String>();
	String data = ((AbstractItem)clazz).flatten();
	if (data != null && data.length() != 0) {
		String doc = "/** @jniclass " + data + " */" + delimiter;
		inserts.put(new Integer(((ASTClass)clazz).start), doc);
	}
	JNIField[] fields = clazz.getDeclaredFields();
	generate(fields);
	JNIMethod[] methods = clazz.getDeclaredMethods();
	generate(methods);
	if (inserts.size() == 0) return;
	String sourcePath = ((ASTClass)clazz).sourcePath;
	String source = JNIGenerator.loadFile(sourcePath);
	Set<Integer> set = inserts.keySet();
	ArrayList<Integer> keys = new ArrayList<Integer>();
	keys.addAll(set);
	Collections.reverse(keys);
	StringBuffer buffer = new StringBuffer(source);
	for (Iterator<Integer> iterator = keys.iterator(); iterator.hasNext();) {
		Integer index = iterator.next();
		String doc = inserts.get(index);
		buffer.insert(index.intValue(), doc);
	}
	try {
		output(buffer.toString().getBytes(), sourcePath);
	} catch (Exception e) {
		e.printStackTrace();
	}
	inserts = null;
}

public void generate(JNIField[] fields) {
	for (int i = 0; i < fields.length; i++) {
		JNIField field = fields[i];
		int mods = field.getModifiers();
		if ((mods & Modifier.PUBLIC) == 0) continue;
		if ((mods & Modifier.FINAL) != 0) continue;
		if ((mods & Modifier.STATIC) != 0) continue;
		generate(field);
	}
}

public void generate(JNIField field) {
	//wrap cast with parentheses
	field.setCast(field.getCast());
	String data = ((AbstractItem)field).flatten();
	if (data != null && data.length() != 0) {
		String doc = "/** @field " + data + " */" + delimiter + "\t";
		inserts.put(new Integer(((ASTField)field).start), doc);
	}
}

public void generate(JNIMethod[] methods) {
	for (int i = 0; i < methods.length; i++) {
		JNIMethod method = methods[i];
		if ((method.getModifiers() & Modifier.NATIVE) == 0) continue;
		generate(method);
	}
}

public void generate(JNIMethod method) {
	ArrayList<String> tags = new ArrayList<String>();
	String data = ((AbstractItem)method).flatten();
	if (data != null && data.length() != 0) {
		tags.add("@method " + data);
	}
	JNIParameter[] params = method.getParameters();
	for (int i = 0; i < params.length; i++) {
		ASTParameter param = (ASTParameter)params[i];
		//wrap cast with parentheses
		param.setCast(param.getCast());
		data = ((AbstractItem)param).flatten();
		if (data != null && data.length() != 0) {
			tags.add("@param " + param.getName() + " " + data);
		}
	}
	if (tags.size() == 0) return;
	if (tags.size() == 1) {
		String doc = "/** " + tags.get(0) + " */" + delimiter;
		inserts.put(new Integer(((ASTMethod)method).start), doc);
	} else {
		StringBuffer buffer = new StringBuffer();
		buffer.append("/**");
		buffer.append(delimiter);
		for (Iterator<String> iterator = tags.iterator(); iterator.hasNext();) {
			String tag = iterator.next();
			buffer.append(" * ");
			buffer.append(tag);
			buffer.append(delimiter);
		}
		buffer.append(" */");
		buffer.append(delimiter);
		inserts.put(new Integer(((ASTMethod)method).start), buffer.toString());
	}
}
}
