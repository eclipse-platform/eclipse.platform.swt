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

import java.lang.reflect.*;

import org.eclipse.jdt.core.dom.Modifier;

public class ReflectMethod extends ReflectItem implements JNIMethod {
	Method method;
	ReflectType returnType;
	ReflectType[] paramTypes;
	ReflectClass declaringClass;
	Boolean unique;
	
public ReflectMethod(ReflectClass declaringClass, Method method) {
	this.method = method;
	this.declaringClass = declaringClass;	
	Class<?> returnType = method.getReturnType();
	Class<?>[] paramTypes = method.getParameterTypes();
	this.returnType = new ReflectType(returnType);
	this.paramTypes = new ReflectType[paramTypes.length];
	for (int i = 0; i < this.paramTypes.length; i++) {
		this.paramTypes[i] = new ReflectType(paramTypes[i]);
	}
}

@Override
public int hashCode() {
	return method.hashCode();
}

@Override
public boolean equals(Object obj) {
	if (!(obj instanceof ReflectMethod)) return false;
	return ((ReflectMethod)obj).method.equals(method);
}

@Override
public JNIClass getDeclaringClass() {
	return declaringClass;
}

@Override
public int getModifiers() {
	return method.getModifiers();
}

@Override
public String getName() {
	return method.getName();
}

@Override
public boolean isNativeUnique() {
	if (unique != null) return unique.booleanValue();
	boolean result = true;
	String name = getName();
	for (JNIMethod mth : declaringClass.getDeclaredMethods()) {
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
public JNIParameter[] getParameters() {
	Class<?>[] paramTypes = method.getParameterTypes();
	ReflectParameter[] result = new ReflectParameter[paramTypes.length];
	for (int i = 0; i < paramTypes.length; i++) {
		result[i] = new ReflectParameter(this, i);
	}
	return result;
}

@Override
public JNIType getReturnType() {
	return returnType;
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
	String key;
	String className = declaringClass.getSimpleName();
	if (isNativeUnique()) {
		key = className + "_" + method.getName ();
	} else {
		key = className + "_" + JNIGenerator.getFunctionName(this);
	}
	declaringClass.metaData.setMetaData(key, value);
}

@Override
public String toString() {
	return method.toString();
}

}
