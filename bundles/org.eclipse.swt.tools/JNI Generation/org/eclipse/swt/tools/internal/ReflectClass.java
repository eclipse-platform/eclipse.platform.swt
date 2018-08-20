/*******************************************************************************
 * Copyright (c) 2004, 2015 IBM Corporation and others.
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

import org.eclipse.jdt.core.dom.*;

public class ReflectClass extends ReflectItem implements JNIClass {
	Class<?> clazz;
	ReflectField[] fields;
	ReflectMethod[] methods;
	MetaData metaData;
	String sourcePath;

public ReflectClass(Class<?> clazz) {
	this(clazz, null, null);
}

public ReflectClass(Class<?> clazz, MetaData data, String sourcePath) {
	this.clazz = clazz;
	this.metaData = data;
	this.sourcePath = sourcePath;
}

void checkMembers() {
	if (fields != null) return;
	String source = null;
	CompilationUnit unit = null;
	if (JNIItem.GEN64) {
		source = JNIGenerator.loadFile(sourcePath);
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(source.toCharArray());
		unit = (CompilationUnit)parser.createAST(null);
	}
	Field[] fields = clazz.getDeclaredFields();
	this.fields = new ReflectField[fields.length];
	for (int i = 0; i < fields.length; i++) {
		this.fields[i] = new ReflectField(this, fields[i], source, unit);
	}
	Method[] methods = clazz.getDeclaredMethods();
	this.methods = new ReflectMethod[methods.length];
	for (int i = 0; i < methods.length; i++) {
		this.methods[i] = new ReflectMethod(this, methods[i], source, unit);
	}
}

@Override
public int hashCode() {
	return clazz.hashCode();
}

@Override
public boolean equals(Object obj) {
	if (!(obj instanceof ReflectClass)) return false;
	return ((ReflectClass)obj).clazz.equals(clazz);
}

@Override
public JNIField[] getDeclaredFields() {
	checkMembers();
	JNIField[] result = new JNIField[fields.length];
	System.arraycopy(fields, 0, result, 0, result.length);
	return result;
}

@Override
public JNIMethod[] getDeclaredMethods() {
	checkMembers();
	JNIMethod[] result = new JNIMethod[methods.length];
	System.arraycopy(methods, 0, result, 0, result.length);
	return result;
}

@Override
public String getName() {
	return clazz.getName();
}

@Override
public JNIClass getSuperclass() {
	Class<?> superclazz = clazz.getSuperclass();
	String path = new File(sourcePath).getParent() + "/" + getSimpleName(superclazz) + ".java";
	return new ReflectClass(superclazz, metaData, path);
}

String getSimpleName(Class<?> type) {
	String name = type.getName();
	int index = name.lastIndexOf('.') + 1;
	return name.substring(index, name.length());
}

@Override
public String getSimpleName() {
	return getSimpleName(clazz);
}

@Override
public String getExclude() {
	return (String)getParam("exclude");
}

@Override
public String getMetaData() {
	String key = JNIGenerator.toC(clazz.getName());
	return metaData.getMetaData(key, "");
}

@Override
public void setExclude(String str) { 
	setParam("exclude", str);
}

@Override
public void setMetaData(String value) {
	String key = JNIGenerator.toC(clazz.getName());
	metaData.setMetaData(key, value);
}

@Override
public String toString() {
	return clazz.toString();
}

}
