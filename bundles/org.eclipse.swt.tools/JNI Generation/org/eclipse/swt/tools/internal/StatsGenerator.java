/*******************************************************************************
 * Copyright (c) 2004, 2013 IBM Corporation and others.
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

import java.lang.reflect.*;

public class StatsGenerator extends JNIGenerator {

	boolean header;
	
public StatsGenerator(boolean header) {
	this.header = header;
}

@Override
public void generateCopyright() {
	outputln(fixDelimiter(getMetaData().getCopyright()));
}

@Override
public void generateIncludes() {
	if (!header) {
		outputln("#include \"swt.h\"");
		output("#include \"");
		output(getOutputName());
		outputln("_stats.h\"");
		outputln();
	}
}

@Override
public void generate(JNIClass clazz) {
	if (header) {
		generateHeaderFile(clazz);
	}
}

@Override
public String getExtension() {
	return header ? ".h" : super.getExtension();
}

@Override
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
	outputln();	
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
	for (JNIMethod method : methods) {
		if ((method.getModifiers() & Modifier.NATIVE) == 0) continue;
		String function = getFunctionName(method);
		output("\t");
		output(function);
		outputln("_FUNC,");
		if (progress != null) progress.step();
	}
	JNIClass clazz = methods[0].getDeclaringClass();
	output("} ");
	output(clazz.getSimpleName());
	outputln("_FUNCS;");
}

}
