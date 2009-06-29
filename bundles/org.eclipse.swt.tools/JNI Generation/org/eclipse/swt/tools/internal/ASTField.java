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

import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

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
	List tags = null;
	if (doc != null) {
		tags = doc.tags();
		for (Iterator iterator = tags.iterator(); iterator.hasNext();) {
			TagElement tag = (TagElement) iterator.next();
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

public int hashCode() {
	return getName().hashCode();
}

public boolean equals(Object obj) {
	if (this == obj) return true;
	if (!(obj instanceof ASTField)) return false;
	return ((ASTField)obj).getName().equals(getName());
}

public JNIClass getDeclaringClass() {
	return declaringClass;
}

public int getModifiers() {
	return modifiers;
}

public String getName() {
	return name;
}

public JNIType getType() {
	return type;
}

public JNIType getType64() {
	return type64;
}

public String getAccessor() {
	return (String)getParam("accessor");
}

public String getCast() {
	String cast = ((String)getParam("cast")).trim();
	if (cast.length() > 0) {
		if (!cast.startsWith("(")) cast = "(" + cast;
		if (!cast.endsWith(")")) cast = cast + ")";
	}
	return cast;
}

public String getExclude() {
	return (String)getParam("exclude");
}

public String getMetaData() {
	if (data != null) return data;
	String className = getDeclaringClass().getSimpleName();
	String key = className + "_" + getName();
	return declaringClass.metaData.getMetaData(key, "");
}

public void setAccessor(String str) { 
	setParam("accessor", str);
}

public void setCast(String str) {
	setParam("cast", str);
}

public void setExclude(String str) { 
	setParam("exclude", str);
}

public void setMetaData(String value) {
	data = value;
}

public String toString() {
	return getName();
}

}
