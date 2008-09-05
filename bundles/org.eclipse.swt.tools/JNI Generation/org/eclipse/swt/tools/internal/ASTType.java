/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tools.internal;

import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.Type;

public class ASTType implements JNIType {
	
	static interface TypeResolver {
		public String findPath(String simpleName);
		public String resolve(String simpleName);
	}	
	
	String name;

public ASTType(String name) {
	this.name = name;
}

public ASTType(TypeResolver resolver, Type type, int extraDimensions) {
	name = from(resolver, type, extraDimensions);
}

String from(TypeResolver resolver, Type type, int extraDimensions) {
	String name = "";
	String str = type.toString();
	if (type.isPrimitiveType()) {
		if (str.equals("void")) name = "V";
		else if (str.equals("int")) name = "I";
		else if (str.equals("boolean")) name = "Z";
		else if (str.equals("long")) name = "J";
		else if (str.equals("short")) name = "S";
		else if (str.equals("char")) name = "C";
		else if (str.equals("byte")) name = "B";
		else if (str.equals("float")) name = "F";
		else if (str.equals("double")) name = "D";
	} else if (type.isArrayType()) {
		ArrayType arrayType = (ArrayType)type;
		name = from(resolver, arrayType.getElementType(), arrayType.getDimensions());
	} else if (str.indexOf('.') != -1) {
		name = "L" + type.toString().replace('.', '/') + ";";
	} else if (str.equals("String")){
		name = "Ljava/lang/String;";
	} else if (str.equals("Class")){
		name = "Ljava/lang/Class;";
	} else if (str.equals("Object")){
		name = "Ljava/lang/Object;";
	} else {
		String qualifiedName = resolver != null  ? resolver.resolve(str) : str;
		name = "L" + qualifiedName.replace('.', '/') + ";";
	}
	for (int i = 0; i < extraDimensions; i++) {
		name = "[" + name;
	}
	return name;
}

public boolean equals(Object obj) {
	if (obj == this) return true;
	if (!(obj instanceof ASTType)) return false;
	return ((ASTType)obj).name.equals(name);
}

public JNIType getComponentType() {
	if (!name.startsWith("[")) throw new RuntimeException();
	return new ASTType(name.substring(1));
}

public String getName() {
	if (isPrimitive()) {
		if (name.equals("V")) return "void";
		if (name.equals("I")) return "int";
		if (name.equals("Z")) return "boolean";
		if (name.equals("J")) return "long";
		if (name.equals("S")) return "short";
		if (name.equals("C")) return "char";
		if (name.equals("B")) return "byte";
		if (name.equals("F")) return "float";
		if (name.equals("D")) return "double";
	}
	if (isArray()) return name;
	return name.substring(1, name.length() - 1).replace('/', '.');
}

public String getSimpleName() {
	String name = getName();
	if (isArray() || isPrimitive()) return name;
	int index = name.lastIndexOf('.') + 1;
	return name.substring(index, name.length());
}

public String getTypeSignature(boolean define) {
	if (isPrimitive()) {
		if (define) {
			if (name.equals("I")) return "I_J";
			if (name.equals("J")) return "I_J";
			if (name.equals("F")) return "F_D";
			if (name.equals("D")) return "F_D";
		}
		return name;
	}
	if (isArray()) {
		if (define) return getComponentType().getTypeSignature(define) + "Array";
		return "[" + getComponentType().getTypeSignature(define);
	}
	return name;
}

public String getTypeSignature1(boolean define) {
	if (isPrimitive()) {
		if (name.equals("V")) return "Void";
		if (name.equals("I")) return define ? "IntLong" : "Int";
		if (name.equals("Z")) return "Boolean";
		if (name.equals("J")) return define ? "IntLong" : "Long";
		if (name.equals("S")) return "Short";
		if (name.equals("C")) return "Char";
		if (name.equals("B")) return "Byte";
		if (name.equals("F")) return define ? "FloatDouble" : "Float";
		if (name.equals("D")) return define ? "FloatDouble" : "Double";
	}
	if (name.equals("Ljava/lang/String;")) return "String";
	return "Object";
}

public String getTypeSignature2(boolean define) {
	if (isPrimitive()) {
		if (name.equals("V")) return "void";
		if (name.equals("I")) return define ? "jintLong" : "jint";
		if (name.equals("Z")) return "jboolean";
		if (name.equals("J")) return define ? "jintLong" : "jlong";
		if (name.equals("S")) return "jshort";
		if (name.equals("C")) return "jchar";
		if (name.equals("B")) return "jbyte";
		if (name.equals("F")) return define ? "jfloatDouble" : "jfloat";
		if (name.equals("D")) return define ? "jfloatDouble" : "jdouble";
	}
	if (name.equals("Ljava/lang/String;")) return "jstring";
	if (name.equals("Ljava/lang/Class;")) return "jclass";
	if (isArray()) {
		return getComponentType().getTypeSignature2(define) + "Array";
	}
	return "jobject";
}

public String getTypeSignature3(boolean define) {
	if (isPrimitive()) {
		if (name.equals("V")) return "void";
		if (name.equals("I")) return "int";
		if (name.equals("Z")) return "boolean";
		if (name.equals("J")) return "long";
		if (name.equals("S")) return "short";
		if (name.equals("C")) return "char";
		if (name.equals("B")) return "byte";
		if (name.equals("F")) return "float";
		if (name.equals("D")) return "double";
	}
	if (name.equals("Ljava/lang/String;")) return "String";
	if (isArray()) {
		return getComponentType().getTypeSignature3(define) + "[]";
	}
	return getName();
}

public String getTypeSignature4(boolean define, boolean struct) {
	if (isPrimitive()) {
		if (name.equals("V")) return "void";
		if (name.equals("I")) return define ? "jintLong" : "jint";
		if (name.equals("Z")) return "jboolean";
		if (name.equals("J")) return define ? "jintLong" : "jlong";
		if (name.equals("S")) return "jshort";
		if (name.equals("C")) return "jchar";
		if (name.equals("B")) return "jbyte";
		if (name.equals("F")) return define ? "jfloatDouble" : "jfloat";
		if (name.equals("D")) return define ? "jfloatDouble" : "jdouble";
	}
	if (name.equals("Ljava/lang/String;")) return "jstring";
	if (isArray()) {
		String sig = getComponentType().getTypeSignature4(define, struct);
		return struct ? sig : sig + " *";
	}
	String sig = getSimpleName(); 
	return struct ? sig : sig + " *";
}

public int hashCode() {
	return name.hashCode();
}

public boolean isArray() {
	return name.startsWith("[");
}

public boolean isPrimitive() {
	return !name.startsWith("L") && !name.startsWith("[");
}

public boolean isType(String type) {
	return getName().equals(type);
}

public String toString() {
	return name;
}

}
