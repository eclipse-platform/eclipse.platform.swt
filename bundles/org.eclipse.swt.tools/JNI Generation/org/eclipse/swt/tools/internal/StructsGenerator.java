/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tools.internal;

import java.lang.reflect.*;
import java.util.HashSet;
import java.util.Iterator;

public class StructsGenerator extends JNIGenerator {

public void generate(Class clazz) {
	generateHeaderFile(clazz);
	generateSourceFile(clazz);
}

public void generateExcludes(Class[] classes) {
	HashSet excludes = new HashSet();
	for (int i = 0; i < classes.length; i++) {
		Class clazz = classes[i];
		ClassData classData = getMetaData().getMetaData(clazz);
		String exclude = classData.getExclude();
		if (exclude.length() != 0) {
			excludes.add(exclude);
		}
	}
	for (Iterator iter = excludes.iterator(); iter.hasNext();) {
		String exclude = (String)iter.next();
		output(exclude);
		outputDelimiter();
		for (int i = 0; i < classes.length; i++) {
			Class clazz = classes[i];
			ClassData classData = getMetaData().getMetaData(clazz);
			String classExclude = classData.getExclude();
			if (exclude.equals(classExclude)) {
				output("#define NO_");
				output(getClassName(clazz));
				outputDelimiter();
			}
		}
		output("#endif");
		outputDelimiter();
		outputDelimiter();
	}
}

public void generateHeaderFile(Class clazz) {
	generateSourceStart(clazz);
	generatePrototypes(clazz);
	generateBlankMacros(clazz);
	generateSourceEnd(clazz);	
	outputDelimiter();
}

public void generateHeaderFile(Class[] classes) {
	sort(classes);
	generateMetaData("swt_copyright");
	generateMetaData("swt_includes");
	generateExcludes(classes);
	for (int i = 0; i < classes.length; i++) {
		Class clazz = classes[i];
		ClassData classData = getMetaData().getMetaData(clazz);
		if (classData.getFlag("no_gen")) continue;
		generateHeaderFile(clazz);
	}
}

public void generateSourceFile(Class clazz) {
	generateSourceStart(clazz);
	generateFIDsStructure(clazz);
	outputDelimiter();
	generateGlobalVar(clazz);
	outputDelimiter();
	generateFunctions(clazz);
	generateSourceEnd(clazz);
	outputDelimiter();
}

public void generateSourceFile(Class[] classes) {
	if (classes.length == 0) return;
	sort(classes);
	generateMetaData("swt_copyright");
	generateMetaData("swt_includes");
	for (int i = 0; i < classes.length; i++) {
		Class clazz = classes[i];
		ClassData classData = getMetaData().getMetaData(clazz);
		if (classData.getFlag("no_gen")) continue;
		generateSourceFile(clazz);
	}
}

void generateSourceStart(Class clazz) {
	String clazzName = getClassName(clazz);
	output("#ifndef NO_");
	output(clazzName);
	outputDelimiter();
}

void generateSourceEnd(Class clazz) {
	output("#endif");
	outputDelimiter();
}

void generateGlobalVar(Class clazz) {
	String clazzName = getClassName(clazz);
	output(clazzName);
	output("_FID_CACHE ");
	output(clazzName);
	output("Fc;");
	outputDelimiter();
}

void generateBlankMacros(Class clazz) {
	String clazzName = getClassName(clazz);
	output("#else");
	outputDelimiter();
	output("#define cache");
	output(clazzName);
	output("Fields(a,b)");
	outputDelimiter();
	output("#define get");
	output(clazzName);
	output("Fields(a,b,c) NULL");
	outputDelimiter();
	output("#define set");
	output(clazzName);
	output("Fields(a,b,c)");
	outputDelimiter();
	output("#define ");
	output(clazzName);
	output("_sizeof() 0");
	outputDelimiter();
}

void generatePrototypes(Class clazz) {
	String clazzName = getClassName(clazz);
	output("void cache");
	output(clazzName);
	output("Fields(JNIEnv *env, jobject lpObject);");
	outputDelimiter();
	output(clazzName);
	output(" *get");
	output(clazzName);
	output("Fields(JNIEnv *env, jobject lpObject, ");
	output(clazzName);
	output(" *lpStruct);");
	outputDelimiter();
	output("void set");
	output(clazzName);
	output("Fields(JNIEnv *env, jobject lpObject, ");
	output(clazzName);
	output(" *lpStruct);");
	outputDelimiter();
	output("#define ");
	output(clazzName);
	output("_sizeof() sizeof(");
	output(clazzName);
	output(")");
	outputDelimiter();
}

void generateFIDsStructure(Class clazz) {
	String clazzName = getClassName(clazz);
	output("typedef struct ");
	output(clazzName);
	output("_FID_CACHE {");
	outputDelimiter();
	output("\tint cached;");
	outputDelimiter();
	output("\tjclass clazz;");
	outputDelimiter();
	output("\tjfieldID ");
	Field[] fields = clazz.getDeclaredFields();
	boolean first = true;
	for (int i = 0; i < fields.length; i++) {
		Field field = fields[i];
		if (ignoreField(field)) continue;
		if (!first) output(", ");
		output(field.getName());
		first = false;
	}
	output(";");
	outputDelimiter();
	output("} ");
	output(clazzName);
	output("_FID_CACHE;");
	outputDelimiter();
}

void generateCacheFunction(Class clazz) {
	String clazzName = getClassName(clazz);
	output("void cache");
	output(clazzName);
	output("Fields(JNIEnv *env, jobject lpObject)");
	outputDelimiter();
	output("{");
	outputDelimiter();
	output("\tif (");
	output(clazzName);
	output("Fc.cached) return;");
	outputDelimiter();
	Class superclazz = clazz.getSuperclass();
	if (superclazz != Object.class) {
		String superName = getClassName(superclazz);
		output("\tcache");
		output(superName);
		output("Fields(env, lpObject);");
		outputDelimiter();
	}
	output("\t");
	output(clazzName);
	output("Fc.clazz = (*env)->GetObjectClass(env, lpObject);");
	outputDelimiter();
	Field[] fields = clazz.getDeclaredFields();
	for (int i = 0; i < fields.length; i++) {
		Field field = fields[i];
		if (ignoreField(field)) continue;
		output("\t");
		output(clazzName);
		output("Fc.");
		output(field.getName());
		output(" = (*env)->GetFieldID(env, ");
		output(clazzName);
		output("Fc.clazz, \"");
		output(field.getName());
		output("\", \"");
		output(getTypeSignature(field));
		output("\");");
		outputDelimiter();
	}
	output("\t");
	output(clazzName);
	output("Fc.cached = 1;");
	outputDelimiter();
	output("}");
	outputDelimiter();
}

void generateGetFields(Class clazz) {
	Class superclazz = clazz.getSuperclass();
	String clazzName = getClassName(clazz);
	String superName = getClassName(superclazz);
	if (superclazz != Object.class) {
		/* Windows exception - cannot call get/set function of super class in this case */
		if (!(clazzName.equals(superName + "A") || clazzName.equals(superName + "W"))) {
			output("\tget");
			output(superName);
			output("Fields(env, lpObject, (");
			output(superName);
			output(" *)lpStruct);");
			outputDelimiter();
		} else {
			generateGetFields(superclazz);
		}
	}
	Field[] fields = clazz.getDeclaredFields();
	for (int i = 0; i < fields.length; i++) {
		Field field = fields[i];
		if (ignoreField(field)) continue;
		FieldData fieldData = getMetaData().getMetaData(field);
		String exclude = fieldData.getExclude();
		if (exclude.length() != 0) {
			output(exclude);
			outputDelimiter();
		}
		boolean noWinCE = fieldData.getFlag("no_wince");
		if (noWinCE) {
			output("#ifndef _WIN32_WCE");
			outputDelimiter();
		}
		Class type = field.getType();
		String typeName = getClassName(type);
		String accessor = fieldData.getAccessor();
		if (accessor == null || accessor.length() == 0) accessor = field.getName();
		if (type.isPrimitive()) {
			output("\tlpStruct->");
			output(accessor);
			output(" = ");
			output(fieldData.getCast());
			output("(*env)->Get");
			output(getTypeSignature1(field));
			output("Field(env, lpObject, ");
			output(getClassName(field.getDeclaringClass()));
			output("Fc.");
			output(field.getName());
			output(");");
		} else if (type.isArray()) {
			Class componentType = type.getComponentType();
			if (componentType.isPrimitive()) {
				output("\t{");
				outputDelimiter();
				output("\t");				
				output(getTypeSignature2(field));
				output(" lpObject1 = (*env)->GetObjectField(env, lpObject, ");
				output(getClassName(field.getDeclaringClass()));
				output("Fc.");
				output(field.getName());
				output(");");
				outputDelimiter();
				output("\t(*env)->Get");
				output(getTypeSignature1(componentType));
				output("ArrayRegion(env, lpObject1, 0, sizeof(lpStruct->");
				output(accessor);
				output(")");
				int byteCount = getByteCount(componentType);
				if (byteCount > 1) {
					output(" / ");
					output(String.valueOf(byteCount));
				}
				output(", (void *)lpStruct->");
				output(accessor);
				output(");");
				outputDelimiter();
				output("\t}");
			} else {
				throw new Error("not done");
			}
		} else {
			output("\t{");
			outputDelimiter();
			output("\tjobject lpObject1 = (*env)->GetObjectField(env, lpObject, ");
			output(getClassName(field.getDeclaringClass()));
			output("Fc.");
			output(field.getName());
			output(");");
			outputDelimiter();
			output("\tget");
			output(typeName);
			output("Fields(env, lpObject1, &lpStruct->");
			output(accessor);
			output(");");
			outputDelimiter();
			output("\t}");
		}
		outputDelimiter();
		if (noWinCE) {
			output("#endif");
			outputDelimiter();
		}
		if (exclude.length() != 0) {
			output("#endif");
			outputDelimiter();
		}
	}
}
void generateGetFunction(Class clazz) {
	String clazzName = getClassName(clazz);
	output(clazzName);
	output(" *get");
	output(clazzName);
	output("Fields(JNIEnv *env, jobject lpObject, ");
	output(clazzName);
	output(" *lpStruct)");
	outputDelimiter();
	output("{");
	outputDelimiter();
	output("\tif (!");
	output(clazzName);
	output("Fc.cached) cache");
	output(clazzName);
	output("Fields(env, lpObject);");
	outputDelimiter();
	generateGetFields(clazz);
	output("\treturn lpStruct;");
	outputDelimiter();
	output("}");
	outputDelimiter();
}

void generateSetFields(Class clazz) {
	Class superclazz = clazz.getSuperclass();
	String clazzName = getClassName(clazz);
	String superName = getClassName(superclazz);
	if (superclazz != Object.class) {
		/* Windows exception - cannot call get/set function of super class in this case */
		if (!(clazzName.equals(superName + "A") || clazzName.equals(superName + "W"))) {
			output("\tset");
			output(superName);
			output("Fields(env, lpObject, (");
			output(superName);
			output(" *)lpStruct);");
			outputDelimiter();
		} else {
			generateSetFields(superclazz);
		}
	}
	Field[] fields = clazz.getDeclaredFields();
	for (int i = 0; i < fields.length; i++) {
		Field field = fields[i];
		if (ignoreField(field)) continue;
		FieldData fieldData = getMetaData().getMetaData(field);
		String exclude = fieldData.getExclude();
		if (exclude.length() != 0) {
			output(exclude);
			outputDelimiter();
		}
		boolean noWinCE = fieldData.getFlag("no_wince");
		if (noWinCE) {
			output("#ifndef _WIN32_WCE");
			outputDelimiter();
		}
		Class type = field.getType();
		String typeName = getClassName(type);
		String accessor = fieldData.getAccessor();
		if (accessor == null || accessor.length() == 0) accessor = field.getName();
		if (type.isPrimitive()) {
			output("\t(*env)->Set");
			output(getTypeSignature1(field));
			output("Field(env, lpObject, ");
			output(getClassName(field.getDeclaringClass()));
			output("Fc.");
			output(field.getName());
			output(", (");
			output(getTypeSignature2(field));
			output(")lpStruct->");
			output(accessor);
			output(");");
		} else if (type.isArray()) {
			Class componentType = type.getComponentType();
			if (componentType.isPrimitive()) {
				output("\t{");
				outputDelimiter();
				output("\t");				
				output(getTypeSignature2(field));
				output(" lpObject1 = (*env)->GetObjectField(env, lpObject, ");
				output(getClassName(field.getDeclaringClass()));
				output("Fc.");
				output(field.getName());
				output(");");
				outputDelimiter();
				output("\t(*env)->Set");
				output(getTypeSignature1(componentType));
				output("ArrayRegion(env, lpObject1, 0, sizeof(lpStruct->");
				output(accessor);
				output(")");
				int byteCount = getByteCount(componentType);
				if (byteCount > 1) {
					output(" / ");
					output(String.valueOf(byteCount));
				}
				output(", (void *)lpStruct->");
				output(accessor);
				output(");");
				outputDelimiter();
				output("\t}");
			} else {
				throw new Error("not done");
			}
		} else {
			output("\t{");
			outputDelimiter();
			output("\tjobject lpObject1 = (*env)->GetObjectField(env, lpObject, ");
			output(getClassName(field.getDeclaringClass()));
			output("Fc.");
			output(field.getName());
			output(");");
			outputDelimiter();
			output("\tset");
			output(typeName);
			output("Fields(env, lpObject1, &lpStruct->");
			output(accessor);
			output(");");
			outputDelimiter();
			output("\t}");
		}
		outputDelimiter();
		if (noWinCE) {
			output("#endif");
			outputDelimiter();
		}
		if (exclude.length() != 0) {
			output("#endif");
			outputDelimiter();
		}
	}
}

void generateSetFunction(Class clazz) {
	String clazzName = getClassName(clazz);
	output("void set");
	output(clazzName);
	output("Fields(JNIEnv *env, jobject lpObject, ");
	output(clazzName);
	output(" *lpStruct)");
	outputDelimiter();
	output("{");
	outputDelimiter();
	output("\tif (!");
	output(clazzName);
	output("Fc.cached) cache");
	output(clazzName);
	output("Fields(env, lpObject);");
	outputDelimiter();
	generateSetFields(clazz);
	output("}");
	outputDelimiter();
}

void generateFunctions(Class clazz) {
	generateCacheFunction(clazz);
	outputDelimiter();
	generateGetFunction(clazz);
	outputDelimiter();
	generateSetFunction(clazz);
}

boolean ignoreField(Field field) {
	int mods = field.getModifiers();
	return
		((mods & Modifier.PUBLIC) == 0) ||
		((mods & Modifier.FINAL) != 0) ||
		((mods & Modifier.STATIC) != 0);
}

public static void main(String[] args) {
	if (args.length < 1) {
		System.out.println("Usage: java StructsGenerator <className1> <className2>");
		return;
	}
	try {
		StructsGenerator gen = new StructsGenerator();
		for (int i = 0; i < args.length; i++) {
			String clazzName = args[i];
			Class clazz = Class.forName(clazzName);
			gen.generate(clazz);
		}
	} catch (Exception e) {
		System.out.println("Problem");
		e.printStackTrace(System.out);
	}
}

}
