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
	
	boolean isCPP;
	
public boolean getCPP() {
	return isCPP;
}

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
		outputln();
		for (int i = 0; i < classes.length; i++) {
			Class clazz = classes[i];
			ClassData classData = getMetaData().getMetaData(clazz);
			String classExclude = classData.getExclude();
			if (exclude.equals(classExclude)) {
				output("#define NO_");
				output(getClassName(clazz));
				outputln();
			}
		}
		output("#endif");
		outputln();
		outputln();
	}
}

public void generateHeaderFile(Class clazz) {
	generateSourceStart(clazz);
	generatePrototypes(clazz);
	generateBlankMacros(clazz);
	generateSourceEnd(clazz);	
	outputln();
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
	outputln();
	generateGlobalVar(clazz);
	outputln();
	generateFunctions(clazz);
	generateSourceEnd(clazz);
	outputln();
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
		if (classData.getFlag("cpp")) {
			isCPP = true;
			break;
		}
	}
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
	outputln();
}

void generateSourceEnd(Class clazz) {
	output("#endif");
	outputln();
}

void generateGlobalVar(Class clazz) {
	String clazzName = getClassName(clazz);
	output(clazzName);
	output("_FID_CACHE ");
	output(clazzName);
	output("Fc;");
	outputln();
}

void generateBlankMacros(Class clazz) {
	String clazzName = getClassName(clazz);
	output("#else");
	outputln();
	output("#define cache");
	output(clazzName);
	output("Fields(a,b)");
	outputln();
	output("#define get");
	output(clazzName);
	output("Fields(a,b,c) NULL");
	outputln();
	output("#define set");
	output(clazzName);
	output("Fields(a,b,c)");
	outputln();
	output("#define ");
	output(clazzName);
	output("_sizeof() 0");
	outputln();
}

void generatePrototypes(Class clazz) {
	String clazzName = getClassName(clazz);
	output("void cache");
	output(clazzName);
	output("Fields(JNIEnv *env, jobject lpObject);");
	outputln();
	output(clazzName);
	output(" *get");
	output(clazzName);
	output("Fields(JNIEnv *env, jobject lpObject, ");
	output(clazzName);
	output(" *lpStruct);");
	outputln();
	output("void set");
	output(clazzName);
	output("Fields(JNIEnv *env, jobject lpObject, ");
	output(clazzName);
	output(" *lpStruct);");
	outputln();
	output("#define ");
	output(clazzName);
	output("_sizeof() sizeof(");
	output(clazzName);
	output(")");
	outputln();
}

void generateFIDsStructure(Class clazz) {
	String clazzName = getClassName(clazz);
	output("typedef struct ");
	output(clazzName);
	output("_FID_CACHE {");
	outputln();
	output("\tint cached;");
	outputln();
	output("\tjclass clazz;");
	outputln();
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
	outputln();
	output("} ");
	output(clazzName);
	output("_FID_CACHE;");
	outputln();
}

void generateCacheFunction(Class clazz) {
	String clazzName = getClassName(clazz);
	output("void cache");
	output(clazzName);
	output("Fields(JNIEnv *env, jobject lpObject)");
	outputln();
	output("{");
	outputln();
	output("\tif (");
	output(clazzName);
	output("Fc.cached) return;");
	outputln();
	Class superclazz = clazz.getSuperclass();
	if (superclazz != Object.class) {
		String superName = getClassName(superclazz);
		output("\tcache");
		output(superName);
		output("Fields(env, lpObject);");
		outputln();
	}
	output("\t");
	output(clazzName);
	if (isCPP) {
		output("Fc.clazz = env->GetObjectClass(lpObject);");
	} else {
		output("Fc.clazz = (*env)->GetObjectClass(env, lpObject);");
	}
	outputln();
	Field[] fields = clazz.getDeclaredFields();
	for (int i = 0; i < fields.length; i++) {
		Field field = fields[i];
		if (ignoreField(field)) continue;
		output("\t");
		output(clazzName);
		output("Fc.");
		output(field.getName());
		if (isCPP) {
			output(" = env->GetFieldID(");
		} else {
			output(" = (*env)->GetFieldID(env, ");
		}
		output(clazzName);
		output("Fc.clazz, \"");
		output(field.getName());
		output("\", \"");
		output(getTypeSignature(field.getType()));
		output("\");");
		outputln();
	}
	output("\t");
	output(clazzName);
	output("Fc.cached = 1;");
	outputln();
	output("}");
	outputln();
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
			outputln();
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
			outputln();
		}
		boolean noWinCE = fieldData.getFlag("no_wince");
		if (noWinCE) {
			output("#ifndef _WIN32_WCE");
			outputln();
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
			if (isCPP) {
				output("env->Get");
			} else {
				output("(*env)->Get");
			}
			output(getTypeSignature1(field.getType()));
			if (isCPP) {
				output("Field(lpObject, ");
			} else {
				output("Field(env, lpObject, ");
			}
			output(getClassName(field.getDeclaringClass()));
			output("Fc.");
			output(field.getName());
			output(");");
		} else if (type.isArray()) {
			Class componentType = type.getComponentType();
			if (componentType.isPrimitive()) {
				output("\t{");
				outputln();
				output("\t");				
				output(getTypeSignature2(field.getType()));
				output(" lpObject1 = (");
				output(getTypeSignature2(field.getType()));
				if (isCPP) {
					output(")env->GetObjectField(lpObject, ");
				} else {
					output(")(*env)->GetObjectField(env, lpObject, ");
				}
				output(getClassName(field.getDeclaringClass()));
				output("Fc.");
				output(field.getName());
				output(");");
				outputln();
				if (isCPP) {
					output("\tenv->Get");
				} else {
					output("\t(*env)->Get");
				}
				output(getTypeSignature1(componentType));
				if (isCPP) {
					output("ArrayRegion(lpObject1, 0, sizeof(lpStruct->");
				} else {
					output("ArrayRegion(env, lpObject1, 0, sizeof(lpStruct->");
				}
				output(accessor);
				output(")");
				int byteCount = getByteCount(componentType);
				if (byteCount > 1) {
					output(" / ");
					output(String.valueOf(byteCount));
				}
				output(", (");
				output(getTypeSignature4(type));				
				output(")lpStruct->");
				output(accessor);
				output(");");
				outputln();
				output("\t}");
			} else {
				throw new Error("not done");
			}
		} else {
			output("\t{");
			outputln();
			if (isCPP) {
				output("\tjobject lpObject1 = env->GetObjectField(lpObject, ");
			} else {
				output("\tjobject lpObject1 = (*env)->GetObjectField(env, lpObject, ");
			}
			output(getClassName(field.getDeclaringClass()));
			output("Fc.");
			output(field.getName());
			output(");");
			outputln();
			output("\tget");
			output(typeName);
			output("Fields(env, lpObject1, &lpStruct->");
			output(accessor);
			output(");");
			outputln();
			output("\t}");
		}
		outputln();
		if (noWinCE) {
			output("#endif");
			outputln();
		}
		if (exclude.length() != 0) {
			output("#endif");
			outputln();
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
	outputln();
	output("{");
	outputln();
	output("\tif (!");
	output(clazzName);
	output("Fc.cached) cache");
	output(clazzName);
	output("Fields(env, lpObject);");
	outputln();
	generateGetFields(clazz);
	output("\treturn lpStruct;");
	outputln();
	output("}");
	outputln();
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
			outputln();
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
			outputln();
		}
		boolean noWinCE = fieldData.getFlag("no_wince");
		if (noWinCE) {
			output("#ifndef _WIN32_WCE");
			outputln();
		}
		Class type = field.getType();
		String typeName = getClassName(type);
		String accessor = fieldData.getAccessor();
		if (accessor == null || accessor.length() == 0) accessor = field.getName();
		if (type.isPrimitive()) {
			if (isCPP) {
				output("\tenv->Set");
			} else {
				output("\t(*env)->Set");
			}
			output(getTypeSignature1(field.getType()));
			if (isCPP) {
				output("Field(lpObject, ");
			} else {
				output("Field(env, lpObject, ");
			}
			output(getClassName(field.getDeclaringClass()));
			output("Fc.");
			output(field.getName());
			output(", (");
			output(getTypeSignature2(field.getType()));
			output(")lpStruct->");
			output(accessor);
			output(");");
		} else if (type.isArray()) {
			Class componentType = type.getComponentType();
			if (componentType.isPrimitive()) {
				output("\t{");
				outputln();
				output("\t");				
				output(getTypeSignature2(field.getType()));
				output(" lpObject1 = (");
				output(getTypeSignature2(field.getType()));
				if (isCPP) {
					output(")env->GetObjectField(lpObject, ");
				} else {
					output(")(*env)->GetObjectField(env, lpObject, ");
				}
				output(getClassName(field.getDeclaringClass()));
				output("Fc.");
				output(field.getName());
				output(");");
				outputln();
				if (isCPP) {
					output("\tenv->Set");
				} else {
					output("\t(*env)->Set");
				}
				output(getTypeSignature1(componentType));
				if (isCPP) {
					output("ArrayRegion(lpObject1, 0, sizeof(lpStruct->");
				} else {
					output("ArrayRegion(env, lpObject1, 0, sizeof(lpStruct->");
				}
				output(accessor);
				output(")");
				int byteCount = getByteCount(componentType);
				if (byteCount > 1) {
					output(" / ");
					output(String.valueOf(byteCount));
				}
				output(", (");
				output(getTypeSignature4(type));				
				output(")lpStruct->");
				output(accessor);
				output(");");
				outputln();
				output("\t}");
			} else {
				throw new Error("not done");
			}
		} else {
			output("\t{");
			outputln();
			output("\tjobject lpObject1 = (*env)->GetObjectField(env, lpObject, ");
			output(getClassName(field.getDeclaringClass()));
			output("Fc.");
			output(field.getName());
			output(");");
			outputln();
			output("\tset");
			output(typeName);
			output("Fields(env, lpObject1, &lpStruct->");
			output(accessor);
			output(");");
			outputln();
			output("\t}");
		}
		outputln();
		if (noWinCE) {
			output("#endif");
			outputln();
		}
		if (exclude.length() != 0) {
			output("#endif");
			outputln();
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
	outputln();
	output("{");
	outputln();
	output("\tif (!");
	output(clazzName);
	output("Fc.cached) cache");
	output(clazzName);
	output("Fields(env, lpObject);");
	outputln();
	generateSetFields(clazz);
	output("}");
	outputln();
}

void generateFunctions(Class clazz) {
	generateCacheFunction(clazz);
	outputln();
	generateGetFunction(clazz);
	outputln();
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
