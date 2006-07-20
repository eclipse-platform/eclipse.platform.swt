/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tools.internal;

import java.lang.reflect.*;
import java.util.HashSet;
import java.util.Iterator;

public class StructsGenerator extends JNIGenerator {

boolean header;

public StructsGenerator(boolean header) {
	this.header = header;
}

public void generateCopyright() {
	generateMetaData("swt_copyright");
}

public void generateIncludes() {
	if (header) {
		output("#include \"");
		output(getOutputName());
		outputln(".h\"");
	} else {
		outputln("#include \"swt.h\"");
		output("#include \"");
		output(getOutputName());
		outputln("_structs.h\"");
	}
	outputln();
}

public void generate(Class clazz) {
	int j = 0;
	Field[] fields = clazz.getDeclaredFields();
	for (; j < fields.length; j++) {
		Field field = fields[j];
		int mods = field.getModifiers();
		if ((mods & Modifier.PUBLIC) != 0 && (mods & Modifier.STATIC) == 0) {
			break;
		}
	}
	if (j == fields.length) return;
	if (header) {
		generateHeaderFile(clazz);
	} else {
		generateSourceFile(clazz);
	}
}

public void generate() {
	if (!header && getClasses().length == 0) return;
	super.generate();
}

public String getExtension() {
	return header ? ".h" : super.getExtension();
}

public String getSuffix() {
	return "_structs";
}

void generateExcludes(Class[] classes) {
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
		outputln(exclude);
		for (int i = 0; i < classes.length; i++) {
			Class clazz = classes[i];
			ClassData classData = getMetaData().getMetaData(clazz);
			String classExclude = classData.getExclude();
			if (exclude.equals(classExclude)) {
				output("#define NO_");
				outputln(getClassName(clazz));
			}
		}
		outputln("#endif");
		outputln();
	}
}

void generateHeaderFile(Class clazz) {
	generateSourceStart(clazz);
	generatePrototypes(clazz);
	generateBlankMacros(clazz);
	generateSourceEnd(clazz);	
	outputln();
}

void generateSourceFile(Class clazz) {
	generateSourceStart(clazz);
	generateFIDsStructure(clazz);
	outputln();
	generateGlobalVar(clazz);
	outputln();
	generateFunctions(clazz);
	generateSourceEnd(clazz);
	outputln();
}

void generateSourceStart(Class clazz) {
	String clazzName = getClassName(clazz);
	output("#ifndef NO_");
	outputln(clazzName);
}

void generateSourceEnd(Class clazz) {
	outputln("#endif");
}

void generateGlobalVar(Class clazz) {
	String clazzName = getClassName(clazz);
	output(clazzName);
	output("_FID_CACHE ");
	output(clazzName);
	outputln("Fc;");
}

void generateBlankMacros(Class clazz) {
	String clazzName = getClassName(clazz);
	outputln("#else");
	output("#define cache");
	output(clazzName);
	outputln("Fields(a,b)");
	output("#define get");
	output(clazzName);
	outputln("Fields(a,b,c) NULL");
	output("#define set");
	output(clazzName);
	outputln("Fields(a,b,c)");
	output("#define ");
	output(clazzName);
	outputln("_sizeof() 0");
}

void generatePrototypes(Class clazz) {
	String clazzName = getClassName(clazz);
	output("void cache");
	output(clazzName);
	outputln("Fields(JNIEnv *env, jobject lpObject);");
	output(clazzName);
	output(" *get");
	output(clazzName);
	output("Fields(JNIEnv *env, jobject lpObject, ");
	output(clazzName);
	outputln(" *lpStruct);");
	output("void set");
	output(clazzName);
	output("Fields(JNIEnv *env, jobject lpObject, ");
	output(clazzName);
	outputln(" *lpStruct);");
	output("#define ");
	output(clazzName);
	output("_sizeof() sizeof(");
	output(clazzName);
	outputln(")");
}

void generateFIDsStructure(Class clazz) {
	String clazzName = getClassName(clazz);
	output("typedef struct ");
	output(clazzName);
	outputln("_FID_CACHE {");
	outputln("\tint cached;");
	outputln("\tjclass clazz;");
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
	outputln(";");
	output("} ");
	output(clazzName);
	outputln("_FID_CACHE;");
}

void generateCacheFunction(Class clazz) {
	String clazzName = getClassName(clazz);
	output("void cache");
	output(clazzName);
	outputln("Fields(JNIEnv *env, jobject lpObject)");
	outputln("{");
	output("\tif (");
	output(clazzName);
	outputln("Fc.cached) return;");
	Class superclazz = clazz.getSuperclass();
	if (superclazz != Object.class) {
		String superName = getClassName(superclazz);
		output("\tcache");
		output(superName);
		outputln("Fields(env, lpObject);");
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
		outputln("\");");
	}
	output("\t");
	output(clazzName);
	outputln("Fc.cached = 1;");
	outputln("}");
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
			outputln(" *)lpStruct);");
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
			outputln(exclude);
		}
		boolean noWinCE = fieldData.getFlag(FLAG_NO_WINCE);
		if (noWinCE) {
			outputln("#ifndef _WIN32_WCE");
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
				outputln("\t{");
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
				outputln(");");
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
				outputln(");");
				output("\t}");
			} else {
				throw new Error("not done");
			}
		} else {
			outputln("\t{");
			if (isCPP) {
				output("\tjobject lpObject1 = env->GetObjectField(lpObject, ");
			} else {
				output("\tjobject lpObject1 = (*env)->GetObjectField(env, lpObject, ");
			}
			output(getClassName(field.getDeclaringClass()));
			output("Fc.");
			output(field.getName());
			outputln(");");
			output("\tif (lpObject1 != NULL) get");
			output(typeName);
			output("Fields(env, lpObject1, &lpStruct->");
			output(accessor);
			outputln(");");
			output("\t}");
		}
		outputln();
		if (noWinCE) {
			outputln("#endif");
		}
		if (exclude.length() != 0) {
			outputln("#endif");
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
	outputln(" *lpStruct)");
	outputln("{");
	output("\tif (!");
	output(clazzName);
	output("Fc.cached) cache");
	output(clazzName);
	outputln("Fields(env, lpObject);");
	generateGetFields(clazz);
	outputln("\treturn lpStruct;");
	outputln("}");
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
			outputln(" *)lpStruct);");
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
			outputln(exclude);
		}
		boolean noWinCE = fieldData.getFlag(FLAG_NO_WINCE);
		if (noWinCE) {
			outputln("#ifndef _WIN32_WCE");
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
				outputln("\t{");
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
				outputln(");");
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
				outputln(");");
				output("\t}");
			} else {
				throw new Error("not done");
			}
		} else {
			outputln("\t{");
			output("\tjobject lpObject1 = (*env)->GetObjectField(env, lpObject, ");
			output(getClassName(field.getDeclaringClass()));
			output("Fc.");
			output(field.getName());
			outputln(");");
			output("\tif (lpObject1 != NULL) set");
			output(typeName);
			output("Fields(env, lpObject1, &lpStruct->");
			output(accessor);
			outputln(");");
			output("\t}");
		}
		outputln();
		if (noWinCE) {
			outputln("#endif");
		}
		if (exclude.length() != 0) {
			outputln("#endif");
		}
	}
}

void generateSetFunction(Class clazz) {
	String clazzName = getClassName(clazz);
	output("void set");
	output(clazzName);
	output("Fields(JNIEnv *env, jobject lpObject, ");
	output(clazzName);
	outputln(" *lpStruct)");
	outputln("{");
	output("\tif (!");
	output(clazzName);
	output("Fc.cached) cache");
	output(clazzName);
	outputln("Fields(env, lpObject);");
	generateSetFields(clazz);
	outputln("}");
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

}
