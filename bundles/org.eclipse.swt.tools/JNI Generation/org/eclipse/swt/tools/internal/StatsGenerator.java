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
	outputln();
	output("extern int ");
	output(className);
	output("_nativeFunctionCount;");
	outputln();
	output("extern int ");
	output(className);
	output("_nativeFunctionCallCount[];");
	outputln();
	output("extern char* ");
	output(className);
	output("_nativeFunctionNames[];");
	outputln();
	output("#define ");
	output(className);
	output("_NATIVE_ENTER(env, that, func) ");
	output(className);
	output("_nativeFunctionCallCount[func]++;");
	outputln();
	output("#define ");
	output(className);
	output("_NATIVE_EXIT(env, that, func) ");
	outputln();
	output("#else");
	outputln();
	output("#define ");
	output(className);
	output("_NATIVE_ENTER(env, that, func) ");
	outputln();
	output("#define ");
	output(className);
	output("_NATIVE_EXIT(env, that, func) ");
	outputln();
	output("#endif");
	outputln();
	outputln();	
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
	outputln();
	outputln();
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
	outputln();
	output("#endif");
	outputln();
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
	outputln();
	output("int ");
	output(className);
	output("_nativeFunctionCallCount[");
	output(String.valueOf(methodCount));
	output("];");
	outputln();
	output("char * ");
	output(className);
	output("_nativeFunctionNames[] = {");
	outputln();
	generate(methods);
	output("};");
	outputln();
	outputln();
	generateStatsNatives(className);
}

void generateStatsNatives(String className) {
	output("#define STATS_NATIVE(func) Java_org_eclipse_swt_tools_internal_NativeStats_##func");
	outputln();
	outputln();

	output("JNIEXPORT jint JNICALL STATS_NATIVE(");
	output(toC(className + "_GetFunctionCount"));
	output(")");
	outputln();
	output("\t(JNIEnv *env, jclass that)");
	outputln();
	output("{");
	outputln();
	output("\treturn ");
	output(className);
	output("_nativeFunctionCount;");
	outputln();
	output("}");
	outputln();
	outputln();

	output("JNIEXPORT jstring JNICALL STATS_NATIVE(");
	output(toC(className + "_GetFunctionName"));
	output(")");
	outputln();
	output("\t(JNIEnv *env, jclass that, jint index)");
	outputln();
	output("{");
	outputln();
	output("\treturn ");
	if (isCPP) {
		output("env->NewStringUTF(");
	} else {
		output("(*env)->NewStringUTF(env, ");
	}
	output(className);
	output("_nativeFunctionNames[index]);");
	outputln();
	output("}");
	outputln();
	outputln();

	output("JNIEXPORT jint JNICALL STATS_NATIVE(");
	output(toC(className + "_GetFunctionCallCount"));
	output(")");
	outputln();
	output("\t(JNIEnv *env, jclass that, jint index)");
	outputln();
	output("{");
	outputln();
	output("\treturn ");
	output(className);
	output("_nativeFunctionCallCount[index];");
	outputln();
	output("}");
	outputln();
}

void generateStringArray(Method method) {	
	output("\t\"");
	output(getFunctionName(method));
	output("\", ");
	outputln();
}

void generateDefines(Method[] methods) {
	if (methods.length == 0) return;
	output("typedef enum {");
	outputln();
	for (int i = 0; i < methods.length; i++) {
		Method method = methods[i];
		if ((method.getModifiers() & Modifier.NATIVE) == 0) continue;
		output("\t");
		output(getFunctionName(method));
		output("_FUNC,");
		outputln();
	}
	Class clazz = methods[0].getDeclaringClass();
	output("} ");
	output(getClassName(clazz));
	output("_FUNCS;");
	outputln();
}

}
