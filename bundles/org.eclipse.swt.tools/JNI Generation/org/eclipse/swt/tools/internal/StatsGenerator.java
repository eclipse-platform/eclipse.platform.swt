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

public class StatsGenerator extends JNIGenerator {
	
	boolean isCPP;

public StatsGenerator() {
}

public boolean getCPP() {
	return isCPP;
}

public void generate(Class clazz) {
	generateHeaderFile(clazz);
	generateSourceFile(clazz);
}

public void generate(Method[] methods) {
	sort(methods);
	for (int i = 0; i < methods.length; i++) {
		Method method = methods[i];
		if ((method.getModifiers() & Modifier.NATIVE) == 0) continue;
		generateStringArray(method);
	}
}

public void generateHeaderFile(Class clazz){
	generateNATIVEMacros(clazz);
	Method[] methods = clazz.getDeclaredMethods();	
	generateHeaderFile(methods);
}

public void generateHeaderFile(Class[] classes) {
	if (classes.length == 0) return;
	sort(classes);
	generateMetaData("swt_copyright");
	for (int i = 0; i < classes.length; i++) {
		Class clazz = classes[i];
		ClassData classData = getMetaData().getMetaData(clazz);
		if (classData.getFlag("no_gen")) continue;
		generateHeaderFile(clazz);
	}
}

void generateNATIVEMacros(Class clazz) {
	String className = getClassName(clazz);
	output("#ifdef NATIVE_STATS");
	outputDelimiter();
	output("extern int ");
	output(className);
	output("_nativeFunctionCount;");
	outputDelimiter();
	output("extern int ");
	output(className);
	output("_nativeFunctionCallCount[];");
	outputDelimiter();
	output("extern char* ");
	output(className);
	output("_nativeFunctionNames[];");
	outputDelimiter();
	output("#define ");
	output(className);
	output("_NATIVE_ENTER(env, that, func) ");
	output(className);
	output("_nativeFunctionCallCount[func]++;");
	outputDelimiter();
	output("#define ");
	output(className);
	output("_NATIVE_EXIT(env, that, func) ");
	outputDelimiter();
	output("#else");
	outputDelimiter();
	output("#define ");
	output(className);
	output("_NATIVE_ENTER(env, that, func) ");
	outputDelimiter();
	output("#define ");
	output(className);
	output("_NATIVE_EXIT(env, that, func) ");
	outputDelimiter();
	output("#endif");
	outputDelimiter();
	outputDelimiter();	
}

public void generateHeaderFile(Method[] methods) {
	sort(methods);
	generateDefines(methods);	
}

public void generateSourceFile(Class[] classes) {
	if (classes.length == 0) return;
	sort(classes);
	generateMetaData("swt_copyright");
	generateMetaData("swt_includes");
	output("#ifdef NATIVE_STATS");
	outputDelimiter();
	outputDelimiter();
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
	outputDelimiter();
	output("#endif");
	outputDelimiter();
}

public void generateSourceFile(Class clazz) {
	Method[] methods = clazz.getDeclaredMethods();
	int methodCount = 0;
	for (int i = 0; i < methods.length; i++) {
		Method method = methods[i];
		if ((method.getModifiers() & Modifier.NATIVE) == 0) continue;
		methodCount++;
	}
	String className = getClassName(clazz);
	output("int ");
	output(className);
	output("_nativeFunctionCount = ");
	output(String.valueOf(methodCount));
	output(";");
	outputDelimiter();
	output("int ");
	output(className);
	output("_nativeFunctionCallCount[");
	output(String.valueOf(methodCount));
	output("];");
	outputDelimiter();
	output("char * ");
	output(className);
	output("_nativeFunctionNames[] = {");
	outputDelimiter();
	generate(methods);
	output("};");
	outputDelimiter();
	outputDelimiter();
	generateStatsNatives(className);
}

void generateStatsNatives(String className) {
	output("#define STATS_NATIVE(func) Java_org_eclipse_swt_tools_internal_NativeStats_##func");
	outputDelimiter();
	outputDelimiter();

	output("JNIEXPORT jint JNICALL STATS_NATIVE(");
	output(toC(className + "_GetFunctionCount"));
	output(")");
	outputDelimiter();
	output("\t(JNIEnv *env, jclass that)");
	outputDelimiter();
	output("{");
	outputDelimiter();
	output("\treturn ");
	output(className);
	output("_nativeFunctionCount;");
	outputDelimiter();
	output("}");
	outputDelimiter();
	outputDelimiter();

	output("JNIEXPORT jstring JNICALL STATS_NATIVE(");
	output(toC(className + "_GetFunctionName"));
	output(")");
	outputDelimiter();
	output("\t(JNIEnv *env, jclass that, jint index)");
	outputDelimiter();
	output("{");
	outputDelimiter();
	output("\treturn ");
	if (isCPP) {
		output("env->NewStringUTF(");
	} else {
		output("(*env)->NewStringUTF(env, ");
	}
	output(className);
	output("_nativeFunctionNames[index]);");
	outputDelimiter();
	output("}");
	outputDelimiter();
	outputDelimiter();

	output("JNIEXPORT jint JNICALL STATS_NATIVE(");
	output(toC(className + "_GetFunctionCallCount"));
	output(")");
	outputDelimiter();
	output("\t(JNIEnv *env, jclass that, jint index)");
	outputDelimiter();
	output("{");
	outputDelimiter();
	output("\treturn ");
	output(className);
	output("_nativeFunctionCallCount[index];");
	outputDelimiter();
	output("}");
	outputDelimiter();
}

void generateStringArray(Method method) {	
	output("\t\"");
	output(getFunctionName(method));
	output("\", ");
	outputDelimiter();
}

void generateDefines(Method[] methods) {
	if (methods.length == 0) return;
	output("typedef enum {");
	outputDelimiter();
	for (int i = 0; i < methods.length; i++) {
		Method method = methods[i];
		if ((method.getModifiers() & Modifier.NATIVE) == 0) continue;
		output("\t");
		output(getFunctionName(method));
		output("_FUNC,");
		outputDelimiter();
	}
	Class clazz = methods[0].getDeclaringClass();
	output("} ");
	output(getClassName(clazz));
	output("_FUNCS;");
	outputDelimiter();
}

}
