/*******************************************************************************
 * Copyright (c) 2004, 2017 IBM Corporation and others.
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

import java.lang.reflect.Modifier;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

public class ASTMethod extends ASTItem implements JNIMethod {
	String name, qualifiedName;
	int modifiers;
	ASTClass declaringClass;
	ASTType[] paramTypes, paramTypes64;
	ASTType returnType, returnType64;
	ASTParameter[] parameters;
	Boolean unique;
	String data;
	int start;
	
public ASTMethod(ASTClass declaringClass, String source, MethodDeclaration method) {
	this.declaringClass = declaringClass;
	
	name = method.getName().getIdentifier();
	modifiers = method.getModifiers();
	start = method.getStartPosition();
	
	Javadoc doc = method.getJavadoc();
	List<TagElement> tags = null;
	if (doc != null) {
		tags = doc.tags();
		for (TagElement tag : tags) {
			if ("@method".equals(tag.getTagName())) {
				String data = tag.fragments().get(0).toString();
				setMetaData(data);
				break;
			}
		}
	}
	returnType = new ASTType(declaringClass.resolver, method.getReturnType2(), method.getExtraDimensions());
	returnType64 = returnType;
	if (GEN64) {
		String s = source.substring(method.getReturnType2().getStartPosition(), method.getName().getStartPosition());
		if (returnType.isType("int") && s.indexOf("int /*long*/") != -1) returnType64 = new ASTType("J");
		else if (returnType.isType("float") && s.indexOf("float /*double*/") != -1) returnType64 = new ASTType("D");
		else if (returnType.isType("[I") && (s.indexOf("int /*long*/") != -1 || s.indexOf("int[] /*long[]*/") != -1)) returnType64 = new ASTType("[J");
		else if (returnType.isType("[F") && (s.indexOf("float /*double*/") != -1|| s.indexOf("float[] /*double[]*/") != -1)) returnType64 = new ASTType("[D");
		else if (returnType.isType("long") && s.indexOf("long /*int*/") != -1) returnType = new ASTType("I");
		else if (returnType.isType("double") && s.indexOf("double /*float*/") != -1) returnType = new ASTType("F");
		else if (returnType.isType("[J") && (s.indexOf("long /*int*/") != -1|| s.indexOf("long[] /*int[]*/") != -1)) returnType = new ASTType("[I");
		else if (returnType.isType("[D") && (s.indexOf("double /*float*/") != -1|| s.indexOf("double[] /*float[]*/") != -1)) returnType = new ASTType("[F");
	}
	
	List<SingleVariableDeclaration> parameters = method.parameters();
	paramTypes = new ASTType[parameters.size()];
	paramTypes64 = new ASTType[parameters.size()];
	this.parameters = new ASTParameter[paramTypes.length];
	int i = 0;
	for (Iterator<SingleVariableDeclaration> iterator = parameters.iterator(); iterator.hasNext(); i++) {
		SingleVariableDeclaration param = iterator.next();
		paramTypes[i] = new ASTType(declaringClass.resolver, param.getType(), param.getExtraDimensions());
		paramTypes64[i] = paramTypes[i];
		this.parameters[i] = new ASTParameter(this, i, param.getName().getIdentifier());
		if (GEN64) {
			String s = source.substring(param.getStartPosition(), param.getStartPosition() + param.getLength());
			if (paramTypes[i].isType("int") && s.indexOf("int /*long*/") != -1) paramTypes64[i] = new ASTType("J");
			else if (paramTypes[i].isType("float") && s.indexOf("float /*double*/") != -1) paramTypes64[i] = new ASTType("D");
			else if (paramTypes[i].isType("[I") && (s.indexOf("int /*long*/") != -1 || s.indexOf("int[] /*long[]*/") != -1)) paramTypes64[i] = new ASTType("[J");
			else if (paramTypes[i].isType("[F") && (s.indexOf("float /*double*/") != -1|| s.indexOf("float[] /*double[]*/") != -1)) paramTypes64[i] = new ASTType("[D");
			else if (paramTypes[i].isType("long") && s.indexOf("long /*int*/") != -1) paramTypes[i] = new ASTType("I");
			else if (paramTypes[i].isType("double") && s.indexOf("double /*float*/") != -1) paramTypes[i] = new ASTType("F");
			else if (paramTypes[i].isType("[J") && (s.indexOf("long /*int*/") != -1|| s.indexOf("long[] /*int[]*/") != -1)) paramTypes[i] = new ASTType("[I");
			else if (paramTypes[i].isType("[D") && (s.indexOf("double /*float*/") != -1|| s.indexOf("double[] /*float[]*/") != -1)) paramTypes[i] = new ASTType("[F");
		}
		if (tags != null) {
			String name = param.getName().getIdentifier();
			for (TagElement tag : tags) {
				if ("@param".equals(tag.getTagName())) {
					List<?> fragments = tag.fragments();
					if (name.equals(fragments.get(0).toString())) {
						String data = fragments.get(1).toString();
						this.parameters[i].setMetaData(data);
					}
				}
			}
		}
	}
}

@Override
public JNIClass getDeclaringClass() {
	return declaringClass;
}

@Override
public int getModifiers() {
	return modifiers;
}

@Override
public String getName() {
	return name;
}

@Override
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
	unique = Boolean.valueOf(result);
	return result;
}

@Override
public JNIType[] getParameterTypes() {
	return paramTypes;
}

@Override
public JNIType[] getParameterTypes64() {
	return paramTypes64;
}

@Override
public JNIParameter[] getParameters() {
	return this.parameters;
}

@Override
public JNIType getReturnType() {
	return returnType;
}

@Override
public JNIType getReturnType64() {
	return returnType64;
}

@Override
public String getAccessor() {
	return (String)getParam("accessor");
}

@Override
public String getExclude() {
	return (String)getParam("exclude");
}

@Override
public String getMetaData() {
	if (data != null) return data;
	String className = getDeclaringClass().getSimpleName();
	String key = className + "_" + JNIGenerator.getFunctionName(this);
	MetaData metaData = declaringClass.metaData;
	String value = metaData.getMetaData(key, null);
	if (value == null) {
		key = className + "_" + getName();
		value = metaData.getMetaData(key, null);
	}
	/*
	* Support for lock.
	*/
	if (value == null && getName().startsWith("_")) {
		key = className + "_" + JNIGenerator.getFunctionName(this).substring(2);
		value = metaData.getMetaData(key, null);
		if (value == null) {
			key = className + "_" + getName().substring(1);
			value = metaData.getMetaData(key, null);
		}
	}
	if (value == null) value = "";	
	return value;
}

@Override
public void setAccessor(String str) { 
	setParam("accessor", str);
}

@Override
public void setExclude(String str) { 
	setParam("exclude", str);
}

@Override
public void setMetaData(String value) {
	data = value;
}

@Override
public String toString() {
	return getName();
}

}
