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

public class ReflectField extends ReflectItem implements JNIField {
	Field field;
	ReflectType type;
	ReflectClass declaringClass;
	
public ReflectField(ReflectClass declaringClass, Field field) {
	this.declaringClass = declaringClass;
	this.field = field;
	Class<?> clazz = field.getType();
	type = new ReflectType(clazz);
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
