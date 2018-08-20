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

import java.util.*;

import org.eclipse.jdt.core.dom.*;

public class ASTField extends ASTItem implements JNIField {
	ASTClass declaringClass;
	String name;
	int modifiers;
	ASTType type, type64;
	String data;
	int start;
	
public ASTField(ASTClass declaringClass, String source, FieldDeclaration field, VariableDeclarationFragment fragment) {
	this.declaringClass = declaringClass;	
	name = fragment.getName().getIdentifier();
	modifiers = field.getModifiers();
	start = field.getStartPosition();
	
	Javadoc doc = field.getJavadoc();
	List<TagElement> tags = null;
	if (doc != null) {
		tags = doc.tags();
		for (TagElement tag : tags) {
			if ("@field".equals(tag.getTagName())) {
				String data = tag.fragments().get(0).toString();
				setMetaData(data);
				break;
			}
		}
	}
	type = new ASTType(declaringClass.resolver, field.getType(), fragment.getExtraDimensions());
	type64 =  this.type;
	if (GEN64) {
		String s = source.substring(field.getStartPosition(), field.getStartPosition() + field.getLength());
		if (type.isType("int") && s.indexOf("int /*long*/") != -1) type64 = new ASTType("J");
		else if (type.isType("float") && s.indexOf("float /*double*/") != -1) type64 = new ASTType("D");
		else if (type.isType("[I") && (s.indexOf("int /*long*/") != -1 || s.indexOf("int[] /*long[]*/") != -1)) type64 = new ASTType("[J");
		else if (type.isType("[F") && (s.indexOf("float /*double*/") != -1|| s.indexOf("float[] /*double[]*/") != -1)) type64 = new ASTType("[D");
		else if (type.isType("long") && s.indexOf("long /*int*/") != -1) type = new ASTType("I");
		else if (type.isType("double") && s.indexOf("double /*float*/") != -1) type = new ASTType("F");
		else if (type.isType("[J") && (s.indexOf("long /*int*/") != -1|| s.indexOf("long[] /*int[]*/") != -1)) type = new ASTType("[I");
		else if (type.isType("[D") && (s.indexOf("double /*float*/") != -1|| s.indexOf("double[] /*float[]*/") != -1)) type = new ASTType("[F");
	}
}

@Override
public int hashCode() {
	return getName().hashCode();
}

@Override
public boolean equals(Object obj) {
	if (this == obj) return true;
	if (!(obj instanceof ASTField)) return false;
	return ((ASTField)obj).getName().equals(getName());
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
	if (data != null) return data;
	String className = getDeclaringClass().getSimpleName();
	String key = className + "_" + getName();
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
	data = value;
}

@Override
public String toString() {
	return getName();
}

}
