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
import java.util.*;

import org.eclipse.jdt.core.dom.*;

public class ReflectField extends ReflectItem implements JNIField {
	Field field;
	ReflectType type, type64;
	ReflectClass declaringClass;
	
public ReflectField(ReflectClass declaringClass, Field field, String source, CompilationUnit unit) {
	this.declaringClass = declaringClass;
	this.field = field;
	Class<?> clazz = field.getType();
	type = new ReflectType(clazz);
	type64 = type;
	boolean changes = canChange64(clazz);
	if (changes && new File(declaringClass.sourcePath).exists()) {
		TypeDeclaration type1 = (TypeDeclaration)unit.types().get(0);
		Class<?> result = null;
		FieldDeclaration[] fields = type1.getFields();
		for (int i = 0; i < fields.length && result == null; i++) {
			FieldDeclaration node = fields[i];
			for (Iterator<?> iterator = node.fragments().iterator(); iterator.hasNext();) {
				VariableDeclarationFragment decl = (VariableDeclarationFragment) iterator.next();
				if (decl.getName().getIdentifier().equals(field.getName())) {
					String s = source.substring(node.getStartPosition(), node.getStartPosition() + node.getLength());
					if (clazz == int.class && s.indexOf("int /*long*/") != -1) type64 = new ReflectType(long.class);
					else if (clazz == float.class && s.indexOf("float /*double*/") != -1) type64 = new ReflectType(double.class);
					else if (clazz == int[].class && (s.indexOf("int /*long*/") != -1 || s.indexOf("int[] /*long[]*/") != -1)) type64 = new ReflectType(long[].class);
					else if (clazz == float[].class && (s.indexOf("float /*double*/") != -1|| s.indexOf("float[] /*double[]*/") != -1)) type = new ReflectType(double[].class);
					else if (clazz == long.class && s.indexOf("long /*int*/") != -1) type = new ReflectType(int.class);
					else if (clazz == double.class && s.indexOf("double /*float*/") != -1) type = new ReflectType(float.class);
					else if (clazz == long[].class && (s.indexOf("long /*int*/") != -1|| s.indexOf("long[] /*int[]*/") != -1)) type = new ReflectType(int[].class);
					else if (clazz == double[].class && (s.indexOf("double /*float*/") != -1|| s.indexOf("double[] /*float[]*/") != -1)) type = new ReflectType(float[].class);
					break;
				}
			}
		}
	}	
}

@Override
public int hashCode() {
	return field.hashCode();
}

@Override
public boolean equals(Object obj) {
	if (!(obj instanceof ReflectField)) return false;
	return ((ReflectField)obj).field.equals(field);
}

@Override
public JNIClass getDeclaringClass() {
	return declaringClass;
}

@Override
public int getModifiers() {
	return field.getModifiers();
}

@Override
public String getName() {
	return field.getName();
}

@Override
public JNIType getType() {
	return type;
}

@Override
public JNIType getType64() {
	return type64;
}

@Override
public String getAccessor() {
	return (String)getParam("accessor");
}

@Override
public String getCast() {
	String cast = ((String)getParam("cast")).trim();
	if (cast.length() > 0) {
		if (!cast.startsWith("(")) cast = "(" + cast;
		if (!cast.endsWith(")")) cast = cast + ")";
	}
	return cast;
}

@Override
public String getExclude() {
	return (String)getParam("exclude");
}

@Override
public String getMetaData() {
	String className = getDeclaringClass().getSimpleName();
	String key = className + "_" + field.getName();
	return declaringClass.metaData.getMetaData(key, "");
}

@Override
public void setAccessor(String str) { 
	setParam("accessor", str);
}

@Override
public void setCast(String str) {
	setParam("cast", str);
}

@Override
public void setExclude(String str) { 
	setParam("exclude", str);
}

@Override
public void setMetaData(String value) {
	String className = declaringClass.getSimpleName();
	String key = className + "_" + field.getName();
	declaringClass.metaData.setMetaData(key, value);
}

@Override
public String toString() {
	return field.toString();
}

}
