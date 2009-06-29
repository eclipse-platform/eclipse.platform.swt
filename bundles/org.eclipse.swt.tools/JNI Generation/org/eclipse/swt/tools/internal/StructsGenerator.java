/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tools.internal;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Iterator;

public class StructsGenerator extends JNIGenerator {

boolean header;

static final boolean GLOBAL_REF = false;

public StructsGenerator(boolean header) {
	this.header = header;
}

public void generateCopyright() {
	outputln(fixDelimiter(getMetaData().getCopyright()));
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

public void generate(JNIClass clazz) {
	int j = 0;
	JNIField[] fields = clazz.getDeclaredFields();
	for (; j < fields.length; j++) {
		JNIField field = fields[j];
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

void generateExcludes(JNIClass[] classes) {
	HashSet excludes = new HashSet();
	for (int i = 0; i < classes.length; i++) {
		JNIClass clazz = classes[i];
		String exclude = clazz.getExclude();
		if (exclude.length() != 0) {
			excludes.add(exclude);
		}
	}
	for (Iterator iter = excludes.iterator(); iter.hasNext();) {
		String exclude = (String)iter.next();
		outputln(exclude);
		for (int i = 0; i < classes.length; i++) {
			JNIClass clazz = classes[i];
			String classExclude = clazz.getExclude();
			if (exclude.equals(classExclude)) {
				output("#define NO_");
				outputln(clazz.getSimpleName());
			}
		}
		outputln("#endif");
		outputln();
	}
}

void generateHeaderFile(JNIClass clazz) {
	generateSourceStart(clazz);
	generatePrototypes(clazz);
	generateBlankMacros(clazz);
	generateSourceEnd(clazz);	
	outputln();
}

void generateSourceFile(JNIClass clazz) {
	generateSourceStart(clazz);
	generateFIDsStructure(clazz);
	outputln();
	generateGlobalVar(clazz);
	outputln();
	generateFunctions(clazz);
	generateSourceEnd(clazz);
	outputln();
}

void generateSourceStart(JNIClass clazz) {
	String clazzName = clazz.getSimpleName();
	output("#ifndef NO_");
	outputln(clazzName);
}

void generateSourceEnd(JNIClass clazz) {
	outputln("#endif");
}

void generateGlobalVar(JNIClass clazz) {
	String clazzName = clazz.getSimpleName();
	output(clazzName);
	output("_FID_CACHE ");
	output(clazzName);
	outputln("Fc;");
}

void generateBlankMacros(JNIClass clazz) {
	String clazzName = clazz.getSimpleName();
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

void generatePrototypes(JNIClass clazz) {
	String clazzName = clazz.getSimpleName();
	output("void cache");
	output(clazzName);
	outputln("Fields(JNIEnv *env, jobject lpObject);");
	if (clazz.getFlag(Flags.FLAG_STRUCT)) {
		output("struct ");
	}
	output(clazzName);
	output(" *get");
	output(clazzName);
	output("Fields(JNIEnv *env, jobject lpObject, ");
	if (clazz.getFlag(Flags.FLAG_STRUCT)) {
		output("struct ");
	}
	output(clazzName);
	outputln(" *lpStruct);");
	output("void set");
	output(clazzName);
	output("Fields(JNIEnv *env, jobject lpObject, ");
	if (clazz.getFlag(Flags.FLAG_STRUCT)) {
		output("struct ");
	}
	output(clazzName);
	outputln(" *lpStruct);");
	output("#define ");
	output(clazzName);
	output("_sizeof() sizeof(");
	if (clazz.getFlag(Flags.FLAG_STRUCT)) {
		output("struct ");
	}
	output(clazzName);
	outputln(")");
}

void generateFIDsStructure(JNIClass clazz) {
	String clazzName = clazz.getSimpleName();
	output("typedef struct ");
	output(clazzName);
	outputln("_FID_CACHE {");
	outputln("\tint cached;");
	outputln("\tjclass clazz;");
	output("\tjfieldID ");
	JNIField[] fields = clazz.getDeclaredFields();
	boolean first = true;
	for (int i = 0; i < fields.length; i++) {
		JNIField field = fields[i];
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

void generateCacheFunction(JNIClass clazz) {
	String clazzName = clazz.getSimpleName();
	output("void cache");
	output(clazzName);
	outputln("Fields(JNIEnv *env, jobject lpObject)");
	outputln("{");
	output("\tif (");
	output(clazzName);
	outputln("Fc.cached) return;");
	JNIClass superclazz = clazz.getSuperclass();
	if (!superclazz.getName().equals("java.lang.Object")) {
		String superName = superclazz.getSimpleName();
		output("\tcache");
		output(superName);
		outputln("Fields(env, lpObject);");
	}
	output("\t");
	output(clazzName);
	if (isCPP) {
		if (GLOBAL_REF) {
			output("Fc.clazz = (jclass)env->NewGlobalRef(env->GetObjectClass(lpObject));");
		} else {
			output("Fc.clazz = env->GetObjectClass(lpObject);");
		}
	} else {
		if (GLOBAL_REF) {
			output("Fc.clazz = (*env)->NewGlobalRef(env, (*env)->GetObjectClass(env, lpObject));");
		} else {
			output("Fc.clazz = (*env)->GetObjectClass(env, lpObject);");
		}
	}
	outputln();
	JNIField[] fields = clazz.getDeclaredFields();
	for (int i = 0; i < fields.length; i++) {
		JNIField field = fields[i];
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
		JNIType type = field.getType(), type64 = field.getType64();
		output("\", ");
		if (type.equals(type64)) output("\"");
		output(type.getTypeSignature(!type.equals(type64)));
		if (type.equals(type64)) output("\"");
		outputln(");");
	}
	output("\t");
	output(clazzName);
	outputln("Fc.cached = 1;");
	outputln("}");
}

void generateGetFields(JNIClass clazz) {
	JNIClass superclazz = clazz.getSuperclass();
	String clazzName = clazz.getSimpleName();
	String superName = superclazz.getSimpleName();
	if (!superclazz.getName().equals("java.lang.Object")) {
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
	JNIField[] fields = clazz.getDeclaredFields();
	for (int i = 0; i < fields.length; i++) {
		JNIField field = fields[i];
		if (ignoreField(field)) continue;
		String exclude = field.getExclude();
		if (exclude.length() != 0) {
			outputln(exclude);
		}
		boolean noWinCE = field.getFlag(FLAG_NO_WINCE);
		if (noWinCE) {
			outputln("#ifndef _WIN32_WCE");
		}
		JNIType type = field.getType(), type64 = field.getType64();
		String typeName = type.getSimpleName();
		String accessor = field.getAccessor();
		if (accessor == null || accessor.length() == 0) accessor = field.getName();
		if (type.isPrimitive()) {
			output("\tlpStruct->");
			output(accessor);
			output(" = ");
			output(field.getCast());
			if (isCPP) {
				output("env->Get");
			} else {
				output("(*env)->Get");
			}
			output(type.getTypeSignature1(!type.equals(type64)));
			if (isCPP) {
				output("Field(lpObject, ");
			} else {
				output("Field(env, lpObject, ");
			}
			output(field.getDeclaringClass().getSimpleName());
			output("Fc.");
			output(field.getName());
			output(");");
		} else if (type.isArray()) {
			JNIType componentType = type.getComponentType(), componentType64 = type64.getComponentType();
			if (componentType.isPrimitive()) {
				outputln("\t{");
				output("\t");				
				output(type.getTypeSignature2(!type.equals(type64)));
				output(" lpObject1 = (");
				output(type.getTypeSignature2(!type.equals(type64)));
				if (isCPP) {
					output(")env->GetObjectField(lpObject, ");
				} else {
					output(")(*env)->GetObjectField(env, lpObject, ");
				}
				output(field.getDeclaringClass().getSimpleName());
				output("Fc.");
				output(field.getName());
				outputln(");");
				if (isCPP) {
					output("\tenv->Get");
				} else {
					output("\t(*env)->Get");
				}
				output(componentType.getTypeSignature1(!componentType.equals(componentType64)));
				if (isCPP) {
					output("ArrayRegion(lpObject1, 0, sizeof(lpStruct->");
				} else {
					output("ArrayRegion(env, lpObject1, 0, sizeof(lpStruct->");
				}
				output(accessor);
				output(")");
				if (!componentType.isType("byte")) {
					output(" / sizeof(");
					output(componentType.getTypeSignature2(!componentType.equals(componentType64)));
					output(")");
				}
				output(", (");
				output(type.getTypeSignature4(!type.equals(type64), false));				
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
			output(field.getDeclaringClass().getSimpleName());
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

void generateGetFunction(JNIClass clazz) {
	String clazzName = clazz.getSimpleName();
	if (clazz.getFlag(Flags.FLAG_STRUCT)) {
		output("struct ");
	}
	output(clazzName);
	output(" *get");
	output(clazzName);
	output("Fields(JNIEnv *env, jobject lpObject, ");
	if (clazz.getFlag(Flags.FLAG_STRUCT)) {
		output("struct ");
	}
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

void generateSetFields(JNIClass clazz) {
	JNIClass superclazz = clazz.getSuperclass();
	String clazzName = clazz.getSimpleName();
	String superName = superclazz.getSimpleName();
	if (!superclazz.getName().equals("java.lang.Object")) {
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
	JNIField[] fields = clazz.getDeclaredFields();
	for (int i = 0; i < fields.length; i++) {
		JNIField field = fields[i];
		if (ignoreField(field)) continue;
		String exclude = field.getExclude();
		if (exclude.length() != 0) {
			outputln(exclude);
		}
		boolean noWinCE = field.getFlag(FLAG_NO_WINCE);
		if (noWinCE) {
			outputln("#ifndef _WIN32_WCE");
		}
		JNIType type = field.getType(), type64 = field.getType64();
		String typeName = type.getSimpleName();
		String accessor = field.getAccessor();
		if (accessor == null || accessor.length() == 0) accessor = field.getName();
		if (type.isPrimitive()) {
			if (isCPP) {
				output("\tenv->Set");
			} else {
				output("\t(*env)->Set");
			}
			output(type.getTypeSignature1(!type.equals(type64)));
			if (isCPP) {
				output("Field(lpObject, ");
			} else {
				output("Field(env, lpObject, ");
			}
			output(field.getDeclaringClass().getSimpleName());
			output("Fc.");
			output(field.getName());
			output(", (");
			output(type.getTypeSignature2(!type.equals(type64)));
			output(")lpStruct->");
			output(accessor);
			output(");");
		} else if (type.isArray()) {
			JNIType componentType = type.getComponentType(), componentType64 = type64.getComponentType();
			if (componentType.isPrimitive()) {
				outputln("\t{");
				output("\t");				
				output(type.getTypeSignature2(!type.equals(type64)));
				output(" lpObject1 = (");
				output(type.getTypeSignature2(!type.equals(type64)));
				if (isCPP) {
					output(")env->GetObjectField(lpObject, ");
				} else {
					output(")(*env)->GetObjectField(env, lpObject, ");
				}
				output(field.getDeclaringClass().getSimpleName());
				output("Fc.");
				output(field.getName());
				outputln(");");
				if (isCPP) {
					output("\tenv->Set");
				} else {
					output("\t(*env)->Set");
				}
				output(componentType.getTypeSignature1(!componentType.equals(componentType64)));
				if (isCPP) {
					output("ArrayRegion(lpObject1, 0, sizeof(lpStruct->");
				} else {
					output("ArrayRegion(env, lpObject1, 0, sizeof(lpStruct->");
				}
				output(accessor);
				output(")");
				if (!componentType.isType("byte")) {
					output(" / sizeof(");
					output(componentType.getTypeSignature2(!componentType.equals(componentType64)));
					output(")");
				}
				output(", (");
				output(type.getTypeSignature4(!type.equals(type64), false));				
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
			output(field.getDeclaringClass().getSimpleName());
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

void generateSetFunction(JNIClass clazz) {
	String clazzName = clazz.getSimpleName();
	output("void set");
	output(clazzName);
	output("Fields(JNIEnv *env, jobject lpObject, ");
	if (clazz.getFlag(Flags.FLAG_STRUCT)) {
		output("struct ");
	}
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

void generateFunctions(JNIClass clazz) {
	generateCacheFunction(clazz);
	outputln();
	generateGetFunction(clazz);
	outputln();
	generateSetFunction(clazz);
}

boolean ignoreField(JNIField field) {
	int mods = field.getModifiers();
	return
		((mods & Modifier.PUBLIC) == 0) ||
		((mods & Modifier.FINAL) != 0) ||
		((mods & Modifier.STATIC) != 0);
}

}
