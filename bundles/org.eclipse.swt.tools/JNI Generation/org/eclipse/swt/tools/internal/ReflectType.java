/*******************************************************************************
 * Copyright (c) 2008, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tools.internal;

public class ReflectType implements JNIType {
	Class clazz;

public ReflectType(Class clazz) {
	this.clazz = clazz;
}

public boolean equals(Object obj) {
	if (obj == this) return true;
	if (!(obj instanceof ReflectType)) return false;
	return ((ReflectType)obj).clazz == clazz;
}

public JNIType getComponentType() {
	return new ReflectType(clazz.getComponentType());
}

public String getName() {
	return clazz.getName();
}

public String getSimpleName() {
	String name = clazz.getName();
	int index = name.lastIndexOf('.') + 1;
	return name.substring(index, name.length());
}

public String getTypeSignature(boolean define) {
	if (clazz == Void.TYPE) return "V";
	if (clazz == Integer.TYPE) return define ? "I_J" : "I";
	if (clazz == Boolean.TYPE) return "Z";
	if (clazz == Long.TYPE) return define ? "J_J" : "J";
	if (clazz == Short.TYPE) return "S";
	if (clazz == Character.TYPE) return "C";
	if (clazz == Byte.TYPE) return "B";
	if (clazz == Float.TYPE) return define ? "F_D" : "F";
	if (clazz == Double.TYPE) return define ? "F_D" : "D";
	if (clazz == String.class) return "Ljava/lang/String;";
	if (clazz.isArray()) {
		if (define) return getComponentType().getTypeSignature(define) + "Array";
		return "[" + getComponentType().getTypeSignature(define);
	}
	return "L" + clazz.getName().replace('.', '/') + ";";
}

public String getTypeSignature1(boolean define) {
	if (clazz == Void.TYPE) return "Void";
	if (clazz == Integer.TYPE) return define ? "IntLong" : "Int";
	if (clazz == Boolean.TYPE) return "Boolean";
	if (clazz == Long.TYPE) return define ? "IntLong" : "Long";
	if (clazz == Short.TYPE) return "Short";
	if (clazz == Character.TYPE) return "Char";
	if (clazz == Byte.TYPE) return "Byte";
	if (clazz == Float.TYPE) return define ? "FloatDouble" : "Float";
	if (clazz == Double.TYPE) return define ? "FloatDouble" : "Double";
	if (clazz == String.class) return "String";
	return "Object";
}

public String getTypeSignature2(boolean define) {
	if (clazz == Void.TYPE) return "void";
	if (clazz == Integer.TYPE) return define ? "jintLong" : "jint";
	if (clazz == Boolean.TYPE) return "jboolean";
	if (clazz == Long.TYPE) return define ? "jintLong" : "jlong";
	if (clazz == Short.TYPE) return "jshort";
	if (clazz == Character.TYPE) return "jchar";
	if (clazz == Byte.TYPE) return "jbyte";
	if (clazz == Float.TYPE) return define ? "jfloatDouble" : "jfloat";
	if (clazz == Double.TYPE) return define ? "jfloatDouble" : "jdouble";
	if (clazz == String.class) return "jstring";
	if (clazz == Class.class) return "jclass";
	if (clazz.isArray()) {
		return getComponentType().getTypeSignature2(define) + "Array";
	}
	return "jobject";
}

public String getTypeSignature3(boolean define) {
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
		return getComponentType().getTypeSignature3(define) + "[]";
	}
	return clazz.getName();
}

public String getTypeSignature4(boolean define, boolean struct) {
	if (clazz == Void.TYPE) return "void";
	if (clazz == Integer.TYPE) return define ? "jintLong" : "jint";
	if (clazz == Boolean.TYPE) return "jboolean";
	if (clazz == Long.TYPE) return define ? "jintLong" : "jlong";
	if (clazz == Short.TYPE) return "jshort";
	if (clazz == Character.TYPE) return "jchar";
	if (clazz == Byte.TYPE) return "jbyte";
	if (clazz == Float.TYPE) return define ? "jfloatDouble" : "jfloat";
	if (clazz == Double.TYPE) return define ? "jfloatDouble" : "jdouble";
	if (clazz == String.class) return "jstring";
	if (clazz.isArray()) {
		String sig = getComponentType().getTypeSignature4(define, struct);
		return struct ? sig : sig + " *";
	}
	String sig = getSimpleName(); 
	return struct ? sig : sig + " *";
}

public int hashCode() {
	return clazz.hashCode();
}

public boolean isArray() {
	return clazz.isArray();
}

public boolean isPrimitive() {
	return clazz.isPrimitive();
}

public boolean isType(String type) {
	return clazz.getName().equals(type);
}

}
