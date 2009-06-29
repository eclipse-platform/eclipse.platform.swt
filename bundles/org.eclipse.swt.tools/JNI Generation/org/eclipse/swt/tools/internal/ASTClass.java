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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.swt.tools.internal.ASTType.TypeResolver;

public class ASTClass extends ASTItem implements JNIClass {
	String sourcePath;
	MetaData metaData;

	ASTClass superclass;
	ASTField[] fields;
	ASTMethod[] methods;
	String name, simpleName, superclassName, packageName;
	String[] imports;
	String data;
	int start;
	
	TypeResolver resolver = new TypeResolver() {
		public String findPath(String simpleName) {
			if (simpleName.equals(ASTClass.this.simpleName)) return sourcePath;
			String basePath = sourcePath.substring(0, sourcePath.length() - name.length() - ".java".length());
			File file = new File(basePath + packageName.replace('.', '/') + "/" + simpleName + ".java");
			if (file.exists()) {
				return file.getAbsolutePath();
			}
			for (int i = 0; i < imports.length; i++) {
				file = new File(basePath + imports[i].replace('.', '/') + "/" + simpleName + ".java");
				if (file.exists()) {
					return file.getAbsolutePath();				
				}
			}
			return "";
		}
		public String resolve(String simpleName) {
			if (simpleName.equals(ASTClass.this.simpleName)) return packageName + "." + simpleName;
			String basePath = sourcePath.substring(0, sourcePath.length() - name.length() - ".java".length());
			File file = new File(basePath + packageName.replace('.', '/') + "/" + simpleName + ".java");
			if (file.exists()) {
				return packageName + "." + simpleName;				
			}
			for (int i = 0; i < imports.length; i++) {
				file = new File(basePath + imports[i].replace('.', '/') + "/" + simpleName + ".java");
				if (file.exists()) {
					return imports[i] + "." + simpleName;				
				}
			}
			return simpleName;
		}
	};

public ASTClass(String sourcePath, MetaData metaData) {
	this.sourcePath = sourcePath;
	this.metaData = metaData;
	
	String source = JNIGenerator.loadFile(sourcePath);
	ASTParser parser = ASTParser.newParser(AST.JLS3);
	parser.setSource(source.toCharArray());
	CompilationUnit unit = (CompilationUnit)parser.createAST(null);
	TypeDeclaration type = (TypeDeclaration)unit.types().get(0);
	simpleName = type.getName().getIdentifier();
	packageName = unit.getPackage().getName().getFullyQualifiedName();
	name = packageName + "." + simpleName;
	superclassName = type.getSuperclassType() != null ? type.getSuperclassType().toString() : null;
	List imports = unit.imports();
	this.imports = new String[imports.size()];
	int count = 0;
	for (Iterator iterator = imports.iterator(); iterator.hasNext();) {
		ImportDeclaration imp = (ImportDeclaration) iterator.next();
		this.imports[count++] = imp.getName().getFullyQualifiedName();
	}
	start = type.getStartPosition();
	
	Javadoc doc = type.getJavadoc();
	List tags = null;
	if (doc != null) {
		tags = doc.tags();
		for (Iterator iterator = tags.iterator(); iterator.hasNext();) {
			TagElement tag = (TagElement) iterator.next();
			if ("@jniclass".equals(tag.getTagName())) {
				String data = tag.fragments().get(0).toString();
				setMetaData(data);
				break;
			}
		}
	}

	FieldDeclaration[] fields = type.getFields();
	ArrayList fid = new ArrayList();
	for (int i = 0; i < fields.length; i++) {
		FieldDeclaration field = fields[i];
		List fragments = field.fragments();
		for (Iterator iterator = fragments.iterator(); iterator.hasNext();) {
			VariableDeclarationFragment fragment = (VariableDeclarationFragment) iterator.next();
			fid.add(new ASTField(this, source, field, fragment));
		}
	}
	this.fields = (ASTField[])fid.toArray(new ASTField[fid.size()]);
	MethodDeclaration[] methods = type.getMethods();
	ArrayList mid = new ArrayList();
	for (int i = 0; i < methods.length; i++) {
		if (methods[i].getReturnType2() == null) continue;
		mid.add(new ASTMethod(this, source, methods[i]));
	}
	this.methods = (ASTMethod[])mid.toArray(new ASTMethod[mid.size()]);
}

public int hashCode() {
	return getName().hashCode();
}

public boolean equals(Object obj) {
	if (this == obj) return true;
	if (!(obj instanceof ASTClass)) return false;
	return ((ASTClass)obj).getName().equals(getName());
}

public JNIField[] getDeclaredFields() {
	JNIField[] result = new JNIField[fields.length];
	System.arraycopy(fields, 0, result, 0, result.length);
	return result;
}

public JNIMethod[] getDeclaredMethods() {
	JNIMethod[] result = new JNIMethod[methods.length];
	System.arraycopy(methods, 0, result, 0, result.length);
	return result;
}

public String getName() {
	return name;
}

public JNIClass getSuperclass() {
	if (superclassName == null) return new ReflectClass(Object.class);
	if (superclass != null) return superclass;
	String sourcePath = resolver.findPath(superclassName);
	return superclass = new ASTClass(sourcePath, metaData);
}

public String getSimpleName() {
	return simpleName;
}

public String getExclude() {
	return (String)getParam("exclude");
}

public String getMetaData() {
	if (data != null) return data;
	String key = JNIGenerator.toC(getName());
	return metaData.getMetaData(key, "");
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
