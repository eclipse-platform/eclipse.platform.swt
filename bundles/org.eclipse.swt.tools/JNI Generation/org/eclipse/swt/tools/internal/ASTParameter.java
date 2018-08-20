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

public class ASTParameter extends ASTItem implements JNIParameter {
	ASTMethod method;
	int parameter;
	String name;
	String data;

public ASTParameter(ASTMethod method, int parameter, String name) {
	this.method = method;
	this.parameter = parameter;
	this.name = name;
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
	if (data != null) return data;
	String className = method.getDeclaringClass().getSimpleName();
	String key = className + "_" + JNIGenerator.getFunctionName(method) + "_" + parameter;
	MetaData metaData = method.declaringClass.metaData;
	String value = metaData.getMetaData(key, null);
	if (value == null) {
		key = className + "_" + method.getName() + "_" + parameter;
		value = metaData.getMetaData(key, null);
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

public String getName() {
	return name;
}

@Override
public JNIClass getTypeClass() {
	ASTType type = (ASTType)getType();
	ASTClass declaringClass = method.declaringClass;
	String sourcePath = declaringClass.resolver.findPath(type.getSimpleName());
	return new ASTClass(sourcePath, declaringClass.metaData);
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
	data = value;
}
}
