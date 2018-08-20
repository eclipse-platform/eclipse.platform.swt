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

import java.io.*;

public class ReflectParameter extends ReflectItem implements JNIParameter {
	ReflectMethod method;
	int parameter;

public ReflectParameter(ReflectMethod method, int parameter) {
	this.method = method;
	this.parameter = parameter;
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
public String getMetaData() {
	String className = method.getDeclaringClass().getSimpleName();
	String key = className + "_" + JNIGenerator.getFunctionName(method) + "_" + parameter;
	MetaData metaData = method.declaringClass.metaData;
	String value = metaData.getMetaData(key, null);
	if (value == null) {
		key = className + "_" + method.getName() + "_" + parameter;
		value = metaData.getMetaData(key, null);
	}
	/*
	* Support for 64 bit port.
	*/
	if (value == null) {
		JNIType[] paramTypes = method.getParameterTypes();
		if (ReflectItem.convertTo32Bit(paramTypes, true)) {
			key = className + "_" + JNIGenerator.getFunctionName(method, paramTypes) + "_" + parameter;
			value = metaData.getMetaData(key, null);
		}
		if (value == null) {
			paramTypes = method.getParameterTypes();
			if (ReflectItem.convertTo32Bit(paramTypes, false)) {
				key = className + "_" + JNIGenerator.getFunctionName(method, paramTypes) + "_" + parameter;
				value = metaData.getMetaData(key, null);
			}
		}
	}
	/*
	* Support for lock.
	*/
	if (value == null && method.getName().startsWith("_")) {
		key = className + "_" + JNIGenerator.getFunctionName(method).substring(2) + "_" + parameter;
		value = metaData.getMetaData(key, null);
		if (value == null) {
			key = className + "_" + method.getName().substring(1) + "_" + parameter;
			value = metaData.getMetaData(key, null);
		}
	}
	if (value == null) value = "";	
	return value;
}

@Override
public JNIMethod getMethod() {
	return method;
}

@Override
public JNIClass getTypeClass() {
	ReflectType type = (ReflectType)getType();
	ReflectClass declaringClass = method.declaringClass;
	String sourcePath  = declaringClass.sourcePath;
	sourcePath = new File(sourcePath).getParent() + "/" + type.getSimpleName() + ".java";
	return new ReflectClass(type.clazz, declaringClass.metaData, sourcePath);
}

@Override
public JNIType getType() {
	return method.getParameterTypes()[parameter];
}

@Override
public JNIType getType64() {
	return method.getParameterTypes64()[parameter];
}

@Override
public int getParameter() {
	return parameter;
}

@Override
public void setCast(String str) {
	setParam("cast", str);
}

@Override
public void setMetaData(String value) {
	String key;
	String className = method.getDeclaringClass().getSimpleName();
	if (method.isNativeUnique()) {
		key = className + "_" + method.getName () + "_" + parameter;
	} else {
		key = className + "_" + JNIGenerator.getFunctionName(method) + "_" + parameter;
	}
	method.declaringClass.metaData.setMetaData(key, value);
}
}
