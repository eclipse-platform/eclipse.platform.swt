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

import org.eclipse.swt.internal.Platform;

public class NativesGenerator extends JNIGenerator {
	
boolean nativeMacro, enterExitMacro;

public NativesGenerator() {
	enterExitMacro = true;
	nativeMacro = true;
}

public void generate(Class clazz, String methodName) {
	Method[] methods = clazz.getDeclaredMethods();
	int count = 0;
	for (int i = 0; i < methods.length; i++) {
		if (methods[i].getName().startsWith(methodName)) count++;
	}
	Method[] result = new Method[count];
	count = 0;
	for (int i = 0; i < methods.length; i++) {
		if (methods[i].getName().startsWith(methodName)) result[count++] = methods[i];
	}
	generate(result);
}

public void generate(Class clazz) {
	ClassData classData = getMetaData().getMetaData(clazz);
	if (classData.getFlag("no_gen")) return;
	generateMetaData("swt_copyright");
	generateMetaData("swt_includes");
	generateNativeMacro(clazz);
	Method[] methods = clazz.getDeclaredMethods();
	generateExcludes(methods);
	generate(methods);
}

public void generateExcludes(Method[] methods) {
	sort(methods);
	HashSet excludes = new HashSet();
	for (int i = 0; i < methods.length; i++) {
		Method method = methods[i];
		if ((method.getModifiers() & Modifier.NATIVE) == 0) continue;
		MethodData methodData = getMetaData().getMetaData(method);
		String exclude = methodData.getExclude();
		if (exclude.length() != 0) {
			excludes.add(exclude);
		}
	}
	for (Iterator iter = excludes.iterator(); iter.hasNext();) {
		String exclude = (String)iter.next();
		output(exclude);
		outputDelimiter();
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			if ((method.getModifiers() & Modifier.NATIVE) == 0) continue;
			MethodData methodData = getMetaData().getMetaData(method);
			String methodExclude = methodData.getExclude();
			if (exclude.equals(methodExclude)) {
				output("#define NO_");
				output(getFunctionName(method));
				outputDelimiter();
			}
		}
		output("#endif");
		outputDelimiter();
		outputDelimiter();
	}
}

public void generate(Method[] methods) {
	sort(methods);	
	for (int i = 0; i < methods.length; i++) {
		Method method = methods[i];
		if ((method.getModifiers() & Modifier.NATIVE) == 0) continue;
		generate(method);
	}
}

public void generate(Method method) {
	MethodData methodData = getMetaData().getMetaData(method);
	if (methodData.getFlag("no_gen")) return;
	Class returnType = method.getReturnType();
	Class[] paramTypes = method.getParameterTypes();
	String function = getFunctionName(method);
	
	if (!(returnType == Void.TYPE || returnType.isPrimitive())) {
		output("Warnning: bad return type. :" + method);
		outputDelimiter();
		return;
	}
	
	generateSourceStart(function);
	generateFunctionPrototype(method, function, paramTypes, returnType);
	generateFunctionBody(method, methodData, function, paramTypes, returnType);
	generateSourceEnd(function);
	outputDelimiter();
}

public void setEnterExitMacro(boolean enterExitMacro) {
	this.enterExitMacro = enterExitMacro;
}

public void setNativeMacro(boolean nativeMacro) {
	this.nativeMacro = nativeMacro;
}

void generateNativeMacro(Class clazz) {
	output("#define ");
	output(getClassName(clazz));
	output("_NATIVE(func) Java_");
	output(toC(clazz.getName()));
	output("_##func");
	outputDelimiter();
	outputDelimiter();
}

void generateGetParameter(int i, Class paramType, ParameterData paramData, boolean critical) {
	if (paramType.isPrimitive()) return;
	output("\tif (arg" + i);
	output(") lparg" + i);
	output(" = ");
	if (paramType.isArray()) {
		Class componentType = paramType.getComponentType();
		if (componentType.isPrimitive()) {
			if (critical) {
				output("(*env)->GetPrimitiveArrayCritical(env, arg" + i);
				output(", NULL);");
			} else {
				output("(*env)->Get");
				output(getTypeSignature1(componentType));
				output("ArrayElements(env, arg" + i);
				output(", NULL);");
			}
		} else {
			throw new Error("not done");
		}
	} else if (paramType == String.class) {
		if (paramData.getFlag("unicode")) {
			output("(*env)->GetStringChars(env, arg" + i);
			output(", NULL);");
		} else {
			output("(*env)->GetStringUTFChars(env, arg" + i);
			output(", NULL);");
		}
	} else {
		if (paramData.getFlag("no_in")) {
			output("&_arg" + i);
			output(";");
		} else {
			output("get");
			output(getClassName(paramType));
			output("Fields(env, arg" + i);
			output(", &_arg" + i);
			output(");");
		}
	}
	outputDelimiter();
}

void generateSetParameter(int i, Class paramType, ParameterData paramData, boolean critical) {
	if (paramType.isPrimitive()) return;
	if (paramType.isArray()) {
		output("\tif (arg" + i);
		output(") ");
		Class componentType = paramType.getComponentType();
		if (componentType.isPrimitive()) {
			if (critical) {
				output("(*env)->ReleasePrimitiveArrayCritical(env, arg" + i);
			} else {
				output("(*env)->Release");
				output(getTypeSignature1(componentType));
				output("ArrayElements(env, arg" + i);
			}
			output(", lparg" + i);
			output(", ");
			if (paramData.getFlag("no_out")) {
				output("JNI_ABORT");
			} else {				
				output("0");
			}
			output(");");
		} else {
			throw new Error("not done");
		}
		outputDelimiter();
	} else if (paramType == String.class) {
		output("\tif (arg" + i);
		output(") ");
		if (paramData.getFlag("unicode")) {
			output("(*env)->ReleaseStringChars(env, arg" + i);
		} else {
			output("(*env)->ReleaseStringUTFChars(env, arg" + i);
		}
		output(", lparg" + i);
		output(");");
		outputDelimiter();
	} else {
		if (!paramData.getFlag("no_out")) {
			output("\tif (arg" + i);
			output(") ");
			output("set");
			output(getClassName(paramType));
			output("Fields(env, arg" + i);
			output(", lparg" + i);
			output(");");
			outputDelimiter();
		}
	}
}

void generateExitMacro(Method method, String function) {
	if (!enterExitMacro) return;
	output("\t");
	output(getClassName(method.getDeclaringClass()));
	output("_NATIVE_EXIT(env, that, ");
	output(function);
	output("_FUNC);");
	outputDelimiter();
}

void generateEnterMacro(Method method, String function) {
	if (!enterExitMacro) return;
	output("\t");
	output(getClassName(method.getDeclaringClass()));
	output("_NATIVE_ENTER(env, that, ");
	output(function);
	output("_FUNC);");
	outputDelimiter();
}

boolean generateLocalVars(Method method, Class[] paramTypes, Class returnType) {
	boolean needsReturn = enterExitMacro;
	for (int i = 0; i < paramTypes.length; i++) {
		Class paramType = paramTypes[i];
		if (paramType.isPrimitive()) continue;
		ParameterData paramData = getMetaData().getMetaData(method, i);
		output("\t");
		if (paramType.isArray()) {
			Class componentType = paramType.getComponentType();
			if (componentType.isPrimitive()) {
				output(getTypeSignature2(componentType));
				output(" *lparg" + i);
				output("=NULL;");
			} else {
				throw new Error("not done");
			}
		} else if (paramType == String.class) {
			if (paramData.getFlag("unicode")) {
				output("const jchar *lparg" + i);				
			} else {
				output("const jbyte *lparg" + i);
			}
			output("= NULL;");
		} else {
			output(getClassName(paramType));
			output(" _arg" + i);
			if (paramData.getFlag("init")) output("={0}");
			output(", *lparg" + i);
			output("=NULL;");
		}
		outputDelimiter();
		needsReturn = true;
	}
	if (needsReturn) {
		if (returnType != Void.TYPE) {
			output("\t");
			output(getTypeSignature2(returnType));
			output(" rc;");
			outputDelimiter();
		}
	}
	return needsReturn;
}

void generateGetters(Method method, Class[] paramTypes) {
	int criticalCount = 0;
	for (int i = 0; i < paramTypes.length; i++) {
		Class paramType = paramTypes[i];
		ParameterData paramData = getMetaData().getMetaData(method, i);
		if (!isCritical(paramType, paramData)) {
			generateGetParameter(i, paramType, paramData, false);
		} else {
			criticalCount++;
		}
	}
	if (criticalCount != 0) {
		output("#ifdef JNI_VERSION_1_2");
		outputDelimiter();
		output("\tif (IS_JNI_1_2) {");
		outputDelimiter();
		for (int i = 0; i < paramTypes.length; i++) {
			Class paramType = paramTypes[i];
			ParameterData paramData = getMetaData().getMetaData(method, i);
			if (isCritical(paramType, paramData)) {
				output("\t");
				generateGetParameter(i, paramType, paramData, true);
			}
		}
		output("\t} else");
		outputDelimiter();
		output("#endif");
		outputDelimiter();
		output("\t{");
		outputDelimiter();
		for (int i = 0; i < paramTypes.length; i++) {
			Class paramType = paramTypes[i];
			ParameterData paramData = getMetaData().getMetaData(method, i);
			if (isCritical(paramType, paramData)) {
				output("\t");
				generateGetParameter(i, paramType, paramData, false);
			}
		}
		output("\t}");
		outputDelimiter();	
	}
}

void generateSetters(Method method, Class[] paramTypes) {
	int criticalCount = 0;
	for (int i = paramTypes.length - 1; i >= 0; i--) {
		Class paramType = paramTypes[i];
		ParameterData paramData = getMetaData().getMetaData(method, i);
		if (isCritical(paramType, paramData)) {
			criticalCount++;
		}
	}
	if (criticalCount != 0) {
		output("#ifdef JNI_VERSION_1_2");
		outputDelimiter();
		output("\tif (IS_JNI_1_2) {");
		outputDelimiter();
		for (int i = paramTypes.length - 1; i >= 0; i--) {
			Class paramType = paramTypes[i];
			ParameterData paramData = getMetaData().getMetaData(method, i);
			if (isCritical(paramType, paramData)) {
				output("\t");
				generateSetParameter(i, paramType, paramData, true);
			}
		}
		output("\t} else");
		outputDelimiter();
		output("#endif");
		outputDelimiter();
		output("\t{");
		outputDelimiter();
		for (int i = paramTypes.length - 1; i >= 0; i--) {
			Class paramType = paramTypes[i];
			ParameterData paramData = getMetaData().getMetaData(method, i);
			if (isCritical(paramType, paramData)) {
				output("\t");
				generateSetParameter(i, paramType, paramData, false);
			}
		}
		output("\t}");
		outputDelimiter();
	}
	for (int i = paramTypes.length - 1; i >= 0; i--) {
		Class paramType = paramTypes[i];
		ParameterData paramData = getMetaData().getMetaData(method, i);
		if (!isCritical(paramType, paramData)) {
			generateSetParameter(i, paramType, paramData, false);
		}
	}
}

void generateDynamicFunctionCall(Method method, MethodData methodData, Class[] paramTypes, Class returnType, boolean needsReturn) {
	output("/*");
	outputDelimiter();
	generateFunctionCall(method, methodData, paramTypes, returnType, needsReturn);
	output("*/");
	outputDelimiter();
	output("\t");
	output("{");
	outputDelimiter();
	
	if (Platform.PLATFORM.equals("win32")) {
		output("\t\tstatic int initialized = 0;");
		outputDelimiter();
		output("\t\tstatic HMODULE hm = NULL;");
		outputDelimiter();
		output("\t\tstatic FARPROC fp = NULL;");
		outputDelimiter();
		if (returnType != Void.TYPE) {
			if (needsReturn) {
				output("\t\trc = 0;");
				outputDelimiter();
			}
		}
		output("\t\tif (!initialized) {");
		outputDelimiter();
		output("\t\t\tif (!(hm = GetModuleHandle(");
		output(method.getName());
		output("_LIB))) hm = LoadLibrary(");
		output(method.getName());
		output("_LIB);");
		outputDelimiter();
		output("\t\t\tif (hm) fp = GetProcAddress(hm, \"");
		output(method.getName());
		output("\");");
		outputDelimiter();
		output("\t\t\tinitialized = 1;");
		outputDelimiter();
		output("\t\t}");
		outputDelimiter();
		output("\t\tif (fp) {");
		outputDelimiter();
		output("\t\t");
		generateFunctionCallLeftSide(method, methodData, returnType, needsReturn);
		output("fp");
		generateFunctionCallRightSide(method, methodData, paramTypes, 0);
		outputDelimiter();
		output("\t\t}");
		outputDelimiter();
	} else {
		output("\t\tstatic int initialized = 0;");
		outputDelimiter();
		output("\t\tstatic void *handle = NULL;");
		outputDelimiter();
		output("\t\tstatic ");
		output(getTypeSignature2(returnType));
		output(" (*fptr)(");
		for (int i = 0; i < paramTypes.length; i++) {
			if (i != 0) output(", ");
			Class paramType = paramTypes[i];
			ParameterData paramData = getMetaData().getMetaData(method, i);
			String cast = paramData.getCast();
			if (cast.length() > 2) {
				output(cast.substring(1, cast.length() - 1));
			} else {
				output(getTypeSignature4(paramType));
			}
		}
		output(");");
		outputDelimiter();
		if (returnType != Void.TYPE) {
			if (needsReturn) {
				output("\t\trc = 0;");
				outputDelimiter();
			}
		}
		output("\t\tif (!initialized) {");
		outputDelimiter();
		output("\t\t\tif (!handle) handle = dlopen(");
		output(method.getName());
		output("_LIB, RTLD_LAZY);");
		outputDelimiter();
		output("\t\t\tif (handle) fptr = dlsym(handle, \"");
		output(method.getName());
		output("\");");
		outputDelimiter();
		output("\t\t\tinitialized = 1;");
		outputDelimiter();
		output("\t\t}");
		outputDelimiter();
		output("\t\tif (fptr) {");
		outputDelimiter();
		output("\t\t");
		generateFunctionCallLeftSide(method, methodData, returnType, needsReturn);
		output("(*fptr)");
		generateFunctionCallRightSide(method, methodData, paramTypes, 0);
		outputDelimiter();
		output("\t\t}");
		outputDelimiter();
	}

	output("\t");
	output("}");
	outputDelimiter();
}

void generateFunctionCallLeftSide(Method method, MethodData methodData, Class returnType, boolean needsReturn) {
	output("\t");
	if (returnType != Void.TYPE) {
		if (needsReturn) {
			output("rc = ");
		} else {
			output("return ");
		}
		output("(");
		output(getTypeSignature2(returnType));
		output(")");
	}
	if (methodData.getFlag("address")) {
		output("&");
	}	
}

void generateFunctionCallRightSide(Method method, MethodData methodData, Class[] paramTypes, int paramStart) {
	if (!methodData.getFlag("const")) {
		output("(");
		for (int i = paramStart; i < paramTypes.length; i++) {
			Class paramType = paramTypes[i];
			ParameterData paramData = getMetaData().getMetaData(method, i);
			if (i != paramStart) output(", ");
			if (paramData.getFlag("struct")) output("*");
			output(paramData.getCast());
			if (!paramType.isPrimitive()) output("lp");
			output("arg" + i);
		}
		output(")");
	}
	output(";");
}

void generateFunctionCall(Method method, MethodData methodData, Class[] paramTypes, Class returnType, boolean needsReturn) {
	generateFunctionCallLeftSide(method, methodData, returnType, needsReturn);
	/*
	* 
	*/
	int paramStart = 0;
	if (method.getName().equalsIgnoreCase("call")) {
		output("(");
		ParameterData paramData = getMetaData().getMetaData(method, 0);
		String cast = paramData.getCast(); 
		if (cast.length() != 0 && !cast.equals("()")) {
			output(cast);
		} else {
			output("(");
			output(getTypeSignature2(returnType));
			output(" (*)())");
		}
		output("arg0)");
		paramStart = 1;
	} else if (method.getName().equals("VtblCall")) {
		output("((");
		output(getTypeSignature2(returnType));
		output(" (STDMETHODCALLTYPE *)())(*(int **)arg1)[arg0])");
		paramStart = 1;
	} else {
		output(method.getName());
	}
	generateFunctionCallRightSide(method, methodData, paramTypes, paramStart);
	outputDelimiter();
}

void generateReturn(Method method, Class returnType, boolean needsReturn) {
	if (needsReturn && returnType != Void.TYPE) {
		output("\treturn rc;");
		outputDelimiter();
	}
}

void generateGTKmemmove(Method method, String function, Class[] paramTypes) {
	generateEnterMacro(method, function);
	output("\t");
	boolean get = paramTypes[0].isPrimitive();
	String className = getClassName(paramTypes[get ? 1 : 0]);
	output(get ? "if (arg1) get" : "if (arg0) set");
	output(className);
	output(get ? "Fields(env, arg1, (" : "Fields(env, arg0, (");
	output(className);
	output(get ? " *)arg0)" : " *)arg1)");
	output(";");
	outputDelimiter();
	generateExitMacro(method, function);	
}

void generateFunctionBody(Method method, MethodData methodData, String function, Class[] paramTypes, Class returnType) {
	output("{");
	outputDelimiter();
	
	/*
	* 
	*/
	boolean isGTKmemove = method.getName().equals("memmove") && paramTypes.length == 2 && returnType == Void.TYPE;
	if (isGTKmemove) {
		generateGTKmemmove(method, function, paramTypes);
	} else {
		boolean needsReturn = generateLocalVars(method, paramTypes, returnType);
		generateEnterMacro(method, function);
		generateGetters(method, paramTypes);
		if (methodData.getFlag("dynamic")) {
			generateDynamicFunctionCall(method, methodData, paramTypes, returnType, needsReturn);
		} else {
			generateFunctionCall(method, methodData, paramTypes, returnType, needsReturn);
		}
		generateSetters(method, paramTypes);
		generateExitMacro(method, function);
		generateReturn(method, returnType, needsReturn);
	}
	
	output("}");
	outputDelimiter();
}

void generateFunctionPrototype(Method method, String function, Class[] paramTypes, Class returnType) {
	output("JNIEXPORT ");
	output(getTypeSignature2(returnType));
	output(" JNICALL ");
	if (nativeMacro) {
		output(getClassName(method.getDeclaringClass()));
		output("_NATIVE(");
	} else {
		output("Java_");
		output(toC(method.getDeclaringClass().getName()));
		output("_");
	}
	output(function);
	if (nativeMacro) {
		output(")");
	}
	outputDelimiter();
	output("\t(JNIEnv *env, ");
	if ((method.getModifiers() & Modifier.STATIC) != 0) {
		output("jclass");
	} else {
		output("jobject");
	}
	output(" that");
	for (int i = 0; i < paramTypes.length; i++) {
		Class paramType = paramTypes[i];
		output(", ");
		output(getTypeSignature2(paramType));
		output(" arg" + i);
	}
	output(")");
	outputDelimiter();
}

void generateSourceStart(String function) {
	output("#ifndef NO_");
	output(function);
	outputDelimiter();	
}

void generateSourceEnd(String function) {
	output("#endif");
	outputDelimiter();
}

boolean isCritical(Class paramType, ParameterData paramData) {
	return paramType.isArray() && paramType.getComponentType().isPrimitive() && paramData.getFlag("critical");
}

boolean isUnique(Method method) {
	return isUnique(method, Modifier.NATIVE);
}

public static void main(String[] args) {
//	args = new String[]{"org.eclipse.swt.internal.win32.OS"};
	if (args.length < 1) {
		System.out.println("Usage: java NativesGenerator <className1> <className2>");
		return;
	}
	try {
		NativesGenerator gen = new NativesGenerator();
		for (int i = 0; i < args.length; i++) {
			String clazzName = args[i];
			Class clazz = Class.forName(clazzName);
			gen.generate(clazz);
//			gen.generate(clazz, "CommandBar_Destroy");
		}
	} catch (Exception e) {
		System.out.println("Problem");
		e.printStackTrace(System.out);
	}
}

}
