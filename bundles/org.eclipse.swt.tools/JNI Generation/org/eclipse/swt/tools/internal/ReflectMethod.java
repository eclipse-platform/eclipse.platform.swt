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

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ReflectMethod extends ReflectItem implements JNIMethod {
	Method method;
	ReflectType returnType, returnType64;
	ReflectType[] paramTypes, paramTypes64;
	ReflectClass declaringClass;
	Boolean unique;
	
public ReflectMethod(ReflectClass declaringClass, Method method, String source, CompilationUnit unit) {
	this.method = method;
	this.declaringClass = declaringClass;	
	Class returnType = method.getReturnType();
	Class[] paramTypes = method.getParameterTypes();
	this.returnType = new ReflectType(returnType);
	this.returnType64 = this.returnType;
	this.paramTypes = new ReflectType[paramTypes.length];
	this.paramTypes64 = new ReflectType[paramTypes.length];
	for (int i = 0; i < this.paramTypes.length; i++) {
		this.paramTypes[i] = this.paramTypes64[i] = new ReflectType(paramTypes[i]);
	}
	boolean changes = false;
	if ((method.getModifiers() & Modifier.NATIVE) != 0) {
		changes = canChange64(returnType);
		if (!changes) {
			for (int i = 0; i < paramTypes.length && !changes; i++) {
				changes |= canChange64(paramTypes[i]);
			}
		}
	}
	if (changes && new File(declaringClass.sourcePath).exists()) {
		String name = method.getName();
		TypeDeclaration type = (TypeDeclaration)unit.types().get(0);
		MethodDeclaration decl = null;
		MethodDeclaration[] methods = type.getMethods();
		for (int i = 0; i < methods.length && decl == null; i++) {
			MethodDeclaration node = methods[i];
			if (node.getName().getIdentifier().equals(name)) {
				if (!declaringClass.getSimpleName(returnType).equals(node.getReturnType2().toString())) continue;
				List parameters = node.parameters();
				if (parameters.size() != paramTypes.length) continue;
				decl = node;
				for (int j = 0; j < paramTypes.length; j++) {
					if (!declaringClass.getSimpleName(paramTypes[j]).equals(((SingleVariableDeclaration)parameters.get(j)).getType().toString())) {
						decl = null;
						break;
					}
				}
			}
		}
		for (int i = 0; i < paramTypes.length; i++) {
			if (canChange64(paramTypes[i])) {
				Class clazz = paramTypes[i];
				SingleVariableDeclaration node = (SingleVariableDeclaration)decl.parameters().get(i);
				String s = source.substring(node.getStartPosition(), node.getStartPosition() + node.getLength());
				if (clazz == int.class && s.indexOf("int /*long*/") != -1) this.paramTypes64[i] = new ReflectType(long.class);
				else if (clazz == int[].class && (s.indexOf("int /*long*/") != -1 || s.indexOf("int[] /*long[]*/") != -1)) this.paramTypes64[i] = new ReflectType(long[].class);
				else if (clazz == float.class && s.indexOf("float /*double*/") != -1) this.paramTypes64[i] = new ReflectType(double.class);
				else if (clazz == float[].class && (s.indexOf("float /*double*/") != -1|| s.indexOf("float[] /*double[]*/") != -1)) this.paramTypes64[i] = new ReflectType(double[].class);
				else if (clazz == long.class && s.indexOf("long /*int*/") != -1) this.paramTypes[i] = new ReflectType(int.class);
				else if (clazz == long[].class && (s.indexOf("long /*int*/") != -1|| s.indexOf("long[] /*int[]*/") != -1)) this.paramTypes[i] = new ReflectType(int[].class);
				else if (clazz == double.class && s.indexOf("double /*float*/") != -1) this.paramTypes[i] = new ReflectType(float.class);
				else if (clazz == double[].class && (s.indexOf("double /*float*/") != -1|| s.indexOf("double[] /*float[]*/") != -1)) this.paramTypes[i] = new ReflectType(float[].class);
			}
		}
		if (canChange64(returnType)) {
			Class clazz = returnType;
			ASTNode node = decl.getReturnType2();
			String s = source.substring(node.getStartPosition(), decl.getName().getStartPosition());
			if (clazz == int.class && s.indexOf("int /*long*/") != -1) this.returnType64 = new ReflectType(long.class);
			else if (clazz == int[].class && (s.indexOf("int /*long*/") != -1 || s.indexOf("int[] /*long[]*/") != -1)) this.returnType64 = new ReflectType(long[].class);
			else if (clazz == float.class && s.indexOf("float /*double*/") != -1) this.returnType64 = new ReflectType(double.class);
			else if (clazz == float[].class && (s.indexOf("float /*double*/") != -1|| s.indexOf("float[] /*double[]*/") != -1)) this.returnType64 = new ReflectType(double[].class);
			else if (clazz == long.class && s.indexOf("long /*int*/") != -1) this.returnType = new ReflectType(int.class);
			else if (clazz == long[].class && (s.indexOf("long /*int*/") != -1|| s.indexOf("long[] /*int[]*/") != -1)) this.returnType = new ReflectType(int[].class);
			else if (clazz == double.class && s.indexOf("double /*float*/") != -1) this.returnType = new ReflectType(float.class);
			else if (clazz == double[].class && (s.indexOf("double /*float*/") != -1|| s.indexOf("double[] /*float[]*/") != -1)) this.returnType = new ReflectType(float[].class);
		}		
	}
}

public int hashCode() {
	return method.hashCode();
}

public boolean equals(Object obj) {
	if (!(obj instanceof ReflectMethod)) return false;
	return ((ReflectMethod)obj).method.equals(method);
}

public JNIClass getDeclaringClass() {
	return declaringClass;
}

public int getModifiers() {
	return method.getModifiers();
}

public String getName() {
	return method.getName();
}

public boolean isNativeUnique() {
	if (unique != null) return unique.booleanValue();
	boolean result = true;
	String name = getName();
	JNIMethod[] methods = declaringClass.getDeclaredMethods();
	for (int i = 0; i < methods.length; i++) {
		JNIMethod mth = methods[i];
		if ((mth.getModifiers() & Modifier.NATIVE) != 0 &&
			this != mth && !this.equals(mth) &&
			name.equals(mth.getName()))
			{
				result = false;
				break;
			}
	}
	unique = new Boolean(result);
	return result;
}

public JNIType[] getParameterTypes() {
	return paramTypes;
}

public JNIType[] getParameterTypes64() {
	return paramTypes64;
}

public JNIParameter[] getParameters() {
	Class[] paramTypes = method.getParameterTypes();
	ReflectParameter[] result = new ReflectParameter[paramTypes.length];
	for (int i = 0; i < paramTypes.length; i++) {
		result[i] = new ReflectParameter(this, i);
	}
	return result;
}

public JNIType getReturnType() {
	return returnType;
}

public JNIType getReturnType64() {
	return returnType64;
}

public String getAccessor() {
	return (String)getParam("accessor");
}

public String getExclude() {
	return (String)getParam("exclude");
}

public String getMetaData() {
	String className = getDeclaringClass().getSimpleName();
	String key = className + "_" + JNIGenerator.getFunctionName(this);
	MetaData metaData = declaringClass.metaData;
	String value = metaData.getMetaData(key, null);
	if (value == null) {
		key = className + "_" + method.getName();
		value = metaData.getMetaData(key, null);
	}
	/*
	* Support for 64 bit port.
	*/
	if (value == null) {
		JNIType[] paramTypes = getParameterTypes();
		if (convertTo32Bit(paramTypes, true)) {
			key = className + "_" + JNIGenerator.getFunctionName(this, paramTypes);
			value = metaData.getMetaData(key, null);
		}
		if (value == null) {
			paramTypes = getParameterTypes();
			if (convertTo32Bit(paramTypes, false)) {
				key = className + "_" + JNIGenerator.getFunctionName(this, paramTypes);
				value = metaData.getMetaData(key, null);
			}
		}
	}
	/*
	* Support for lock.
	*/
	if (value == null && method.getName().startsWith("_")) {
		key = className + "_" + JNIGenerator.getFunctionName(this).substring(2);
		value = metaData.getMetaData(key, null);
		if (value == null) {
			key = className + "_" + method.getName().substring(1);
			value = metaData.getMetaData(key, null);
		}
	}
	if (value == null) value = "";	
	return value;
}

public void setAccessor(String str) { 
	setParam("accessor", str);
}

public void setExclude(String str) { 
	setParam("exclude", str);
}

public void setMetaData(String value) {
	String key;
	String className = declaringClass.getSimpleName();
	if (isNativeUnique()) {
		key = className + "_" + method.getName ();
	} else {
		key = className + "_" + JNIGenerator.getFunctionName(this);
	}
	declaringClass.metaData.setMetaData(key, value);
}

public String toString() {
	return method.toString();
}

}
