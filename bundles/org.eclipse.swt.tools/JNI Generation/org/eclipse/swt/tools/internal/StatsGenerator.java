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

public class StatsGenerator extends JNIGenerator {

	boolean header;
	
public StatsGenerator(boolean header) {
	this.header = header;
}

public void generateCopyright() {
	outputln(fixDelimiter(getMetaData().getCopyright()));
}

public void generateIncludes() {
	if (!header) {
		outputln("#include \"swt.h\"");
		output("#include \"");
		output(getOutputName());
		outputln("_stats.h\"");
		outputln();
	}
}

public void generate(JNIClass clazz) {
	if (header) {
		generateHeaderFile(clazz);
	} else {
		generateSourceFile(clazz);
	}
}

public String getExtension() {
	return header ? ".h" : super.getExtension();
}

public String getSuffix() {
	return "_stats";
}

void generateHeaderFile(JNIClass clazz){
	generateNATIVEMacros(clazz);
	JNIMethod[] methods = clazz.getDeclaredMethods();
	sort(methods);
	generateFunctionEnum(methods);	
}

void generateNATIVEMacros(JNIClass clazz) {
	String className = clazz.getSimpleName();
	outputln("#ifdef NATIVE_STATS");
	output("extern int ");
	output(className);
	outputln("_nativeFunctionCount;");
	output("extern int ");
	output(className);
	outputln("_nativeFunctionCallCount[];");
	output("extern char* ");
	output(className);
	outputln("_nativeFunctionNames[];");
	output("#define ");
	output(className);
	output("_NATIVE_ENTER(env, that, func) ");
	output(className);
	outputln("_nativeFunctionCallCount[func]++;");
	output("#define ");
	output(className);
	outputln("_NATIVE_EXIT(env, that, func) ");
	outputln("#else");
	output("#ifndef ");
	output(className);
	outputln("_NATIVE_ENTER");
	output("#define ");
	output(className);
	outputln("_NATIVE_ENTER(env, that, func) ");
	outputln("#endif");
	output("#ifndef ");
	output(className);
	outputln("_NATIVE_EXIT");
	output("#define ");
	output(className);
	outputln("_NATIVE_EXIT(env, that, func) ");
	outputln("#endif");
	outputln("#endif");
	outputln();	
}

void generateSourceFile(JNIClass clazz) {
	outputln("#ifdef NATIVE_STATS");
	outputln();
	JNIMethod[] methods = clazz.getDeclaredMethods();
	int methodCount = 0;
	for (int i = 0; i < methods.length; i++) {
		JNIMethod method = methods[i];
		if ((method.getModifiers() & Modifier.NATIVE) == 0) continue;
		methodCount++;
	}
	String className = clazz.getSimpleName();
	output("int ");
	output(className);
	output("_nativeFunctionCount = ");
	output(String.valueOf(methodCount));
	outputln(";");
	output("int ");
	output(className);
	output("_nativeFunctionCallCount[");
	output(String.valueOf(methodCount));
	outputln("];");
	output("char * ");
	output(className);
	outputln("_nativeFunctionNames[] = {");
	sort(methods);
	for (int i = 0; i < methods.length; i++) {
		JNIMethod method = methods[i];
		if ((method.getModifiers() & Modifier.NATIVE) == 0) continue;
		String function = getFunctionName(method), function64 = getFunctionName(method, method.getParameterTypes64());
		if (!function.equals(function64)) {
			output("#ifndef ");
			output(JNI64);
			outputln();
		}
		output("\t\"");
		output(function);
		outputln("\",");
		if (!function.equals(function64)) {
			outputln("#else");
			output("\t\"");
			output(function64);
			outputln("\",");
			outputln("#endif");
		}
		if (progress != null) progress.step();
	}
	outputln("};");
	outputln();
	generateStatsNatives(className);
	outputln();
	outputln("#endif");
}

void generateStatsNatives(String className) {
	outputln("#define STATS_NATIVE(func) Java_org_eclipse_swt_tools_internal_NativeStats_##func");
	outputln();

	output("JNIEXPORT jint JNICALL STATS_NATIVE(");
	output(toC(className + "_GetFunctionCount"));
	outputln(")");
	outputln("\t(JNIEnv *env, jclass that)");
	outputln("{");
	output("\treturn ");
	output(className);
	outputln("_nativeFunctionCount;");
	outputln("}");
	outputln();

	output("JNIEXPORT jstring JNICALL STATS_NATIVE(");
	output(toC(className + "_GetFunctionName"));
	outputln(")");
	outputln("\t(JNIEnv *env, jclass that, jint index)");
	outputln("{");
	output("\treturn ");
	boolean isCPP = getCPP();
	if (isCPP) {
		output("env->NewStringUTF(");
	} else {
		output("(*env)->NewStringUTF(env, ");
	}
	output(className);
	outputln("_nativeFunctionNames[index]);");
	outputln("}");
	outputln();

	output("JNIEXPORT jint JNICALL STATS_NATIVE(");
	output(toC(className + "_GetFunctionCallCount"));
	outputln(")");
	outputln("\t(JNIEnv *env, jclass that, jint index)");
	outputln("{");
	output("\treturn ");
	output(className);
	outputln("_nativeFunctionCallCount[index];");
	outputln("}");
}

void generateFunctionEnum(JNIMethod[] methods) {
	if (methods.length == 0) return;
	outputln("typedef enum {");
	for (int i = 0; i < methods.length; i++) {
		JNIMethod method = methods[i];
		if ((method.getModifiers() & Modifier.NATIVE) == 0) continue;
		String function = getFunctionName(method), function64 = getFunctionName(method, method.getParameterTypes64());
		if (!function.equals(function64)) {
			output("#ifndef ");
			output(JNI64);
			outputln();
		}
		output("\t");
		output(function);
		outputln("_FUNC,");
		if (!function.equals(function64)) {
			outputln("#else");
			output("\t");
			output(function64);
			outputln("_FUNC,");
			outputln("#endif");
		}
		if (progress != null) progress.step();
	}
	JNIClass clazz = methods[0].getDeclaringClass();
	output("} ");
	output(clazz.getSimpleName());
	outputln("_FUNCS;");
}

}
