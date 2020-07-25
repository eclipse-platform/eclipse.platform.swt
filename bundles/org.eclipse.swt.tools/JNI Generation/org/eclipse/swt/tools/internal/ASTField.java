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
	ASTType type;
	String data;
	int start;
	
public ASTField(ASTClass declaringClass, FieldDeclaration field, VariableDeclarationFragment fragment) {
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
