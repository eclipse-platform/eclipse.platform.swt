/*******************************************************************************
 * Copyright (c) 2008, 2015 IBM Corporation and others.
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

public class ReflectType implements JNIType {
	Class<?> clazz;

public ReflectType(Class<?> clazz) {
	this.clazz = clazz;
}

@Override
public boolean equals(Object obj) {
	if (obj == this) return true;
	if (!(obj instanceof ReflectType)) return false;
	return ((ReflectType)obj).clazz == clazz;
}

@Override
public JNIType getComponentType() {
	return new ReflectType(clazz.getComponentType());
}

@Override
public String getName() {
	return clazz.getName();
}

@Override
public String getSimpleName() {
	String name = clazz.getName();
	int index = name.lastIndexOf('.') + 1;
	return name.substring(index, name.length());
}

@Override
public String getTypeSignature() {
	if (clazz == Void.TYPE) return "V";
	if (clazz == Integer.TYPE) return "I";
	if (clazz == Boolean.TYPE) return "Z";
	if (clazz == Long.TYPE) return "J";
	if (clazz == Short.TYPE) return "S";
	if (clazz == Character.TYPE) return "C";
	if (clazz == Byte.TYPE) return "B";
	if (clazz == Float.TYPE) return "F";
	if (clazz == Double.TYPE) return "D";
	if (clazz == String.class) return "Ljava/lang/String;";
	if (clazz.isArray()) {
		return "[" + getComponentType().getTypeSignature();
	}
	return "L" + clazz.getName().replace('.', '/') + ";";
}

@Override
public String getTypeSignature1() {
	if (clazz == Void.TYPE) return "Void";
	if (clazz == Integer.TYPE) return "Int";
	if (clazz == Boolean.TYPE) return "Boolean";
	if (clazz == Long.TYPE) return "Long";
	if (clazz == Short.TYPE) return "Short";
	if (clazz == Character.TYPE) return "Char";
	if (clazz == Byte.TYPE) return "Byte";
	if (clazz == Float.TYPE) return "Float";
	if (clazz == Double.TYPE) return "Double";
	if (clazz == String.class) return "String";
	return "Object";
}

@Override
public String getTypeSignature2() {
	if (clazz == Void.TYPE) return "void";
	if (clazz == Integer.TYPE) return "jint";
	if (clazz == Boolean.TYPE) return "jboolean";
	if (clazz == Long.TYPE) return "jlong";
	if (clazz == Short.TYPE) return "jshort";
	if (clazz == Character.TYPE) return "jchar";
	if (clazz == Byte.TYPE) return "jbyte";
	if (clazz == Float.TYPE) return "jfloat";
	if (clazz == Double.TYPE) return "jdouble";
	if (clazz == String.class) return "jstring";
	if (clazz == Class.class) return "jclass";
	if (clazz.isArray()) {
		return getComponentType().getTypeSignature2() + "Array";
	}
	return "jobject";
}

@Override
public String getTypeSignature3() {
	if (clazz == Void.TYPE) return "void";
	if (clazz == Integer.TYPE) return "int";
	if (clazz == Boolean.TYPE) return "boolean";
	if (clazz == Long.TYPE) return "long";
	if (clazz == Short.TYPE) return "short";
	if (clazz == Character.TYPE) return "char";
	if (clazz == Byte.TYPE) return "byte";
	if (clazz == Float.TYPE) return "float";
	if (clazz == Double.TYPE) return "double";
	if (clazz == String.class) return "String";
	if (clazz.isArray()) {
		return getComponentType().getTypeSignature3() + "[]";
	}
	return clazz.getName();
}

@Override
public String getTypeSignature4(boolean struct) {
	if (clazz == Void.TYPE) return "void";
	if (clazz == Integer.TYPE) return "jint";
	if (clazz == Boolean.TYPE) return "jboolean";
	if (clazz == Long.TYPE) return "jlong";
	if (clazz == Short.TYPE) return "jshort";
	if (clazz == Character.TYPE) return "jchar";
	if (clazz == Byte.TYPE) return "jbyte";
	if (clazz == Float.TYPE) return "jfloat";
	if (clazz == Double.TYPE) return "jdouble";
	if (clazz == String.class) return "jstring";
	if (clazz.isArray()) {
		String sig = getComponentType().getTypeSignature4(struct);
		return struct ? sig : sig + " *";
	}
	String sig = getSimpleName(); 
	return struct ? sig : sig + " *";
}

@Override
public int hashCode() {
	return clazz.hashCode();
}

@Override
public boolean isArray() {
	return clazz.isArray();
}

@Override
public boolean isPrimitive() {
	return clazz.isPrimitive();
}

@Override
public boolean isType(String type) {
	return clazz.getName().equals(type);
}

}
