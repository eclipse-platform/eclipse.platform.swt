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

public class NativesGenerator extends JNIGenerator {

boolean enterExitMacro;

public NativesGenerator() {
	enterExitMacro = true;
}

public void generateCopyright() {
	generateMetaData("swt_copyright");
}

public void generateIncludes() {
	String outputName = getOutputName();
	outputln("#include \"swt.h\"");
	output("#include \"");
	output(outputName);
	outputln("_structs.h\"");
	output("#include \"");
	output(outputName);
	outputln("_stats.h\"");
	outputln();
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
	Method[] methods = clazz.getDeclaredMethods();
	int i = 0;
	for (; i < methods.length; i++) {
		Method method = methods[i];
		if ((method.getModifiers() & Modifier.NATIVE) != 0) break;
	}
	if (i == methods.length) return;
	sort(methods);
	if (isCPP) {
		outputln("extern \"C\" {");
		outputln();
	}
	generateNativeMacro(clazz);
	generateExcludes(methods);
	generate(methods);
	if (isCPP) {
		outputln("}");
	}
}

public void generate(Method[] methods) {
	sort(methods);	
	for (int i = 0; i < methods.length; i++) {
		Method method = methods[i];
		if ((method.getModifiers() & Modifier.NATIVE) == 0) continue;
		generate(method);
		if (progress != null) progress.step();
	}
}

public void generate(Method method) {
	MethodData methodData = getMetaData().getMetaData(method);
	if (methodData.getFlag(FLAG_NO_GEN)) return;
	Class returnType = method.getReturnType();
	Class[] paramTypes = method.getParameterTypes();
	String function = getFunctionName(method);
	
	if (!(returnType == Void.TYPE || returnType.isPrimitive() || isSystemClass(returnType) || returnType == String.class)) {
		output("Warning: bad return type. :");
		outputln(method.toString());
		return;
	}
	
	generateSourceStart(function);
	generateFunctionPrototype(method, function, paramTypes, returnType);
	generateFunctionBody(method, methodData, function, paramTypes, returnType);
	generateSourceEnd(function);
	outputln();
}

public void setEnterExitMacro(boolean enterExitMacro) {
	this.enterExitMacro = enterExitMacro;
}

void generateExcludes(Method[] methods) {
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
		outputln(exclude);
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			if ((method.getModifiers() & Modifier.NATIVE) == 0) continue;
			MethodData methodData = getMetaData().getMetaData(method);
			String methodExclude = methodData.getExclude();
			if (exclude.equals(methodExclude)) {
				output("#define NO_");
				outputln(getFunctionName(method));
			}
		}
		outputln("#endif");
		outputln();
	}
}

void generateNativeMacro(Class clazz) {
	output("#define ");
	output(getClassName(clazz));
	output("_NATIVE(func) Java_");
	output(toC(clazz.getName()));
	outputln("_##func");
	outputln();
}

boolean generateGetParameter(Method method, int i, Class paramType, ParameterData paramData, boolean critical, int indent) {
	if (paramType.isPrimitive() || isSystemClass(paramType)) return false;
	String iStr = String.valueOf(i);
	for (int j = 0; j < indent; j++) output("\t");
	output("if (arg");
	output(iStr);
	output(") if ((lparg");
	output(iStr);
	output(" = ");
	if (paramType.isArray()) {
		Class componentType = paramType.getComponentType();
		if (componentType.isPrimitive()) {
			if (critical) {
				if (isCPP) {
					output("env->GetPrimitiveArrayCritical(arg");
				} else {
					output("(*env)->GetPrimitiveArrayCritical(env, arg");
				}
				output(iStr);
				output(", NULL)");
			} else {
				if (isCPP) {
					output("env->Get");
				} else {
					output("(*env)->Get");
				}
				output(getTypeSignature1(componentType));
				if (isCPP) {
					output("ArrayElements(arg");
				} else {
					output("ArrayElements(env, arg");
				}
				output(iStr);
				output(", NULL)");
			}
		} else {
			throw new Error("not done");
		}
	} else if (paramType == String.class) {
		if (paramData.getFlag(FLAG_UNICODE)) {
			if (isCPP) {
				output("env->GetStringChars(arg");
			} else {
				output("(*env)->GetStringChars(env, arg");
			}
			output(iStr);
			output(", NULL)");
		} else {
			if (isCPP) {
				output("env->GetStringUTFChars(arg");
			} else {
				output("(*env)->GetStringUTFChars(env, arg");
			}
			output(iStr);
			output(", NULL)");
		}
	} else {
		if (paramData.getFlag(FLAG_NO_IN)) {
			output("&_arg");
			output(iStr);
		} else {
			output("get");
			output(getClassName(paramType));
			output("Fields(env, arg");
			output(iStr);
			output(", &_arg");
			output(iStr);
			output(")");
		}
	}	
	outputln(") == NULL) goto fail;");
	return true;
}

void generateSetParameter(int i, Class paramType, ParameterData paramData, boolean critical) {
	if (paramType.isPrimitive() || isSystemClass(paramType)) return;
	String iStr = String.valueOf(i);
	if (paramType.isArray()) {
		output("\tif (arg");
		output(iStr);
		output(" && lparg");
		output(iStr);
		output(") ");
		Class componentType = paramType.getComponentType();
		if (componentType.isPrimitive()) {
			if (critical) {
				if (isCPP) {
					output("env->ReleasePrimitiveArrayCritical(arg");
				} else {
					output("(*env)->ReleasePrimitiveArrayCritical(env, arg");
				}
				output(iStr);
			} else {
				if (isCPP) {
					output("env->Release");
				} else {
					output("(*env)->Release");
				}
				output(getTypeSignature1(componentType));
				if (isCPP) {
					output("ArrayElements(arg");
				} else {
					output("ArrayElements(env, arg");
				}
				output(iStr);
			}
			output(", lparg");
			output(iStr);
			output(", ");
			if (paramData.getFlag(FLAG_NO_OUT)) {
				output("JNI_ABORT");
			} else {				
				output("0");
			}
			output(");");
		} else {
			throw new Error("not done");
		}
		outputln();
	} else if (paramType == String.class) {
		output("\tif (arg");
		output(iStr);
		output(" && lparg");
		output(iStr);
		output(") ");
		if (paramData.getFlag(FLAG_UNICODE)) {
			if (isCPP) {
				output("env->ReleaseStringChars(arg");
			} else {
				output("(*env)->ReleaseStringChars(env, arg");
			}
		} else {
			if (isCPP) {
				output("env->ReleaseStringUTFChars(arg");
			} else {
				output("(*env)->ReleaseStringUTFChars(env, arg");
			}
		}
		output(iStr);
		output(", lparg");
		output(iStr);
		outputln(");");
	} else {
		if (!paramData.getFlag(FLAG_NO_OUT)) {
			output("\tif (arg");
			output(iStr);
			output(" && lparg");
			output(iStr);
			output(") ");
			output("set");
			output(getClassName(paramType));
			output("Fields(env, arg");
			output(iStr);
			output(", lparg");
			output(iStr);
			outputln(");");
		}
	}
}

void generateExitMacro(Method method, String function) {
	if (!enterExitMacro) return;
	output("\t");
	output(getClassName(method.getDeclaringClass()));
	output("_NATIVE_EXIT(env, that, ");
	output(function);
	outputln("_FUNC);");
}

void generateEnterMacro(Method method, String function) {
	if (!enterExitMacro) return;
	output("\t");
	output(getClassName(method.getDeclaringClass()));
	output("_NATIVE_ENTER(env, that, ");
	output(function);
	outputln("_FUNC);");
}

boolean generateLocalVars(Method method, Class[] paramTypes, Class returnType) {
	boolean needsReturn = enterExitMacro;
	for (int i = 0; i < paramTypes.length; i++) {
		Class paramType = paramTypes[i];
		if (paramType.isPrimitive() || isSystemClass(paramType)) continue;
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
			if (paramData.getFlag(FLAG_UNICODE)) {
				output("const jchar *lparg" + i);				
			} else {
				output("const char *lparg" + i);
			}
			output("= NULL;");
		} else {
			output(getClassName(paramType));
			output(" _arg" + i);
			if (paramData.getFlag(FLAG_INIT)) output("={0}");
			output(", *lparg" + i);
			output("=NULL;");
		}
		outputln();
		needsReturn = true;
	}
	if (needsReturn) {
		if (returnType != Void.TYPE) {
			output("\t");
			output(getTypeSignature2(returnType));
			outputln(" rc = 0;");
		}
	}
	return needsReturn;
}

boolean generateGetters(Method method, Class[] paramTypes) {
	boolean genFailTag = false;
	int criticalCount = 0;
	for (int i = 0; i < paramTypes.length; i++) {
		Class paramType = paramTypes[i];
		ParameterData paramData = getMetaData().getMetaData(method, i);
		if (!isCritical(paramType, paramData)) {
			genFailTag |= generateGetParameter(method, i, paramType, paramData, false, 1);
		} else {
			criticalCount++;
		}
	}
	if (criticalCount != 0) {
		outputln("#ifdef JNI_VERSION_1_2");
		outputln("\tif (IS_JNI_1_2) {");
		for (int i = 0; i < paramTypes.length; i++) {
			Class paramType = paramTypes[i];
			ParameterData paramData = getMetaData().getMetaData(method, i);
			if (isCritical(paramType, paramData)) {
				genFailTag |= generateGetParameter(method, i, paramType, paramData, true, 2);
			}
		}
		outputln("\t} else");
		outputln("#endif");
		outputln("\t{");
		for (int i = 0; i < paramTypes.length; i++) {
			Class paramType = paramTypes[i];
			ParameterData paramData = getMetaData().getMetaData(method, i);
			if (isCritical(paramType, paramData)) {
				genFailTag |= generateGetParameter(method, i, paramType, paramData, false, 2);
			}
		}
		outputln("\t}");
	}
	return genFailTag;
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
		outputln("#ifdef JNI_VERSION_1_2");
		outputln("\tif (IS_JNI_1_2) {");
		for (int i = paramTypes.length - 1; i >= 0; i--) {
			Class paramType = paramTypes[i];
			ParameterData paramData = getMetaData().getMetaData(method, i);
			if (isCritical(paramType, paramData)) {
				output("\t");
				generateSetParameter(i, paramType, paramData, true);
			}
		}
		outputln("\t} else");
		outputln("#endif");
		outputln("\t{");
		for (int i = paramTypes.length - 1; i >= 0; i--) {
			Class paramType = paramTypes[i];
			ParameterData paramData = getMetaData().getMetaData(method, i);
			if (isCritical(paramType, paramData)) {
				output("\t");
				generateSetParameter(i, paramType, paramData, false);
			}
		}
		outputln("\t}");
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
	outputln("/*");
	generateFunctionCall(method, methodData, paramTypes, returnType, needsReturn);
	outputln("*/");
	outputln("\t{");

	String name = method.getName();
	if (name.startsWith("_")) name = name.substring(1);
	if (getPlatform().equals("win32")) {
		outputln("\t\tstatic int initialized = 0;");
		outputln("\t\tstatic HMODULE hm = NULL;");
		outputln("\t\tstatic FARPROC fp = NULL;");
		if (returnType != Void.TYPE) {
			if (needsReturn) {
				outputln("\t\trc = 0;");
			}
		}
		outputln("\t\tif (!initialized) {");
		output("\t\t\tif (!hm) hm = LoadLibrary(");
		output(name);
		outputln("_LIB);");
		output("\t\t\tif (hm) fp = GetProcAddress(hm, \"");
		output(name);
		outputln("\");");
		outputln("\t\t\tinitialized = 1;");
		outputln("\t\t}");
		outputln("\t\tif (fp) {");
		output("\t\t");
		generateFunctionCallLeftSide(method, methodData, returnType, needsReturn);
		output("fp");
		generateFunctionCallRightSide(method, methodData, paramTypes, 0);
		outputln();
		outputln("\t\t}");
	} else if (getPlatform().equals("carbon")) {
		outputln("\t\tstatic int initialized = 0;");
		outputln("\t\tstatic CFBundleRef bundle = NULL;");
		output("\t\ttypedef ");
		output(getTypeSignature2(returnType));
		output(" (*FPTR)(");
		for (int i = 0; i < paramTypes.length; i++) {
			if (i != 0) output(", ");
			Class paramType = paramTypes[i];
			ParameterData paramData = getMetaData().getMetaData(method, i);
			String cast = paramData.getCast();
			if (cast.length() > 2) {
				output(cast.substring(1, cast.length() - 1));
			} else {
				output(getTypeSignature4(paramType, paramData.getFlag(FLAG_STRUCT)));
			}
		}
		outputln(");");
		outputln("\t\tstatic FPTR fptr;");
		if (returnType != Void.TYPE) {
			if (needsReturn) {
				outputln("\t\trc = 0;");
			}
		}
		outputln("\t\tif (!initialized) {");
		output("\t\t\tif (!bundle) bundle = CFBundleGetBundleWithIdentifier(CFSTR(");
		output(name);
		outputln("_LIB));");
		output("\t\t\tif (bundle) fptr = (FPTR)CFBundleGetFunctionPointerForName(bundle, CFSTR(\"");
		output(name);
		outputln("\"));");
		outputln("\t\t\tinitialized = 1;");
		outputln("\t\t}");
		outputln("\t\tif (fptr) {");
		output("\t\t");
		generateFunctionCallLeftSide(method, methodData, returnType, needsReturn);
		output("(*fptr)");
		generateFunctionCallRightSide(method, methodData, paramTypes, 0);
		outputln();
		outputln("\t\t}");
	} else {
		outputln("\t\tstatic int initialized = 0;");
		outputln("\t\tstatic void *handle = NULL;");
		output("\t\ttypedef ");
		output(getTypeSignature2(returnType));
		output(" (*FPTR)(");
		for (int i = 0; i < paramTypes.length; i++) {
			if (i != 0) output(", ");
			Class paramType = paramTypes[i];
			ParameterData paramData = getMetaData().getMetaData(method, i);
			String cast = paramData.getCast();
			if (cast.length() > 2) {
				output(cast.substring(1, cast.length() - 1));
			} else {
				output(getTypeSignature4(paramType, paramData.getFlag(FLAG_STRUCT)));
			}
		}
		outputln(");");
		outputln("\t\tstatic FPTR fptr;");
		if (returnType != Void.TYPE) {
			if (needsReturn) {
				outputln("\t\trc = 0;");
			}
		}
		outputln("\t\tif (!initialized) {");
		output("\t\t\tif (!handle) handle = dlopen(");
		output(name);
		outputln("_LIB, RTLD_LAZY);");
		output("\t\t\tif (handle) fptr = (FPTR)dlsym(handle, \"");
		output(name);
		outputln("\");");
		outputln("\t\t\tinitialized = 1;");
		outputln("\t\t}");
		outputln("\t\tif (fptr) {");
		output("\t\t");
		generateFunctionCallLeftSide(method, methodData, returnType, needsReturn);
		output("(*fptr)");
		generateFunctionCallRightSide(method, methodData, paramTypes, 0);
		outputln();
		outputln("\t\t}");
	}

	outputln("\t}");
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
	if (methodData.getFlag(FLAG_ADDRESS)) {
		output("&");
	}	
	if (methodData.getFlag(FLAG_JNI)) {
		output(isCPP ? "env->" : "(*env)->");
	}
}

void generateFunctionCallRightSide(Method method, MethodData methodData, Class[] paramTypes, int paramStart) {
	if (!methodData.getFlag(FLAG_CONST)) {
		output("(");
		if (methodData.getFlag(FLAG_JNI)) {
			if (!isCPP) output("env, ");
		}
		for (int i = paramStart; i < paramTypes.length; i++) {
			Class paramType = paramTypes[i];
			ParameterData paramData = getMetaData().getMetaData(method, i);
			if (i != paramStart) output(", ");
			if (paramData.getFlag(FLAG_STRUCT)) output("*");
			output(paramData.getCast());
			if (i == paramTypes.length - 1 && paramData.getFlag(FLAG_SENTINEL)) {
				output("NULL");
			} else {
				if (!paramType.isPrimitive() && !isSystemClass(paramType)) output("lp");
				output("arg" + i);
			}
		}
		output(")");
	}
	output(";");
}

void generateFunctionCall(Method method, MethodData methodData, Class[] paramTypes, Class returnType, boolean needsReturn) {
	String copy = (String)methodData.getParam("copy");
	boolean makeCopy = copy.length() != 0 && isCPP && returnType != Void.TYPE;
	if (makeCopy) {
		output("\t");
		output(copy);
		output(" temp = ");
	} else {
		generateFunctionCallLeftSide(method, methodData, returnType, needsReturn);
	}
	int paramStart = 0;
	String name = method.getName();
	if (name.startsWith("_")) name = name.substring(1);
	if (name.equalsIgnoreCase("call")) {
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
	} else if (name.startsWith("VtblCall")) {
		output("((");
		output(getTypeSignature2(returnType));
		output(" (STDMETHODCALLTYPE *)(");
		for (int i = 1; i < paramTypes.length; i++) {
			if (i != 1) output(", ");
			Class paramType = paramTypes[i];
			output(getTypeSignature4(paramType));
		}
		output("))(*(");
		output(getTypeSignature4(paramTypes[1]));
		output(" **)arg1)[arg0])");
		paramStart = 1;
	} else if (methodData.getFlag(FLAG_CPP)) {
		output("(");
		ParameterData paramData = getMetaData().getMetaData(method, 0);
		if (paramData.getFlag(FLAG_STRUCT)) output("*");
		String cast = paramData.getCast(); 
		if (cast.length() != 0 && !cast.equals("()")) {
			output(cast);
		}
		output("arg0)->");
		String accessor = methodData.getAccessor();
		if (accessor.length() != 0) {
			output(accessor);
		} else {
			int index = -1;
			if ((index = name.indexOf('_')) != -1) {
				output(name.substring(index + 1, name.length()));
			} else {
				output(name);
			}
		}
		paramStart = 1;
	} else if (methodData.getFlag(FLAG_NEW)) {
		output("new ");
		String accessor = methodData.getAccessor();
		if (accessor.length() != 0) {
			output(accessor);
		} else {
			int index = -1;
			if ((index = name.indexOf('_')) != -1) {
				output(name.substring(0, index));
			} else {
				output(name);
			}
		}
	} else if (methodData.getFlag(FLAG_DELETE)) {
		output("delete ");
		ParameterData paramData = getMetaData().getMetaData(method, 0);
		String cast = paramData.getCast(); 
		if (cast.length() != 0 && !cast.equals("()")) {
			output(cast);
		} else {
			output("(");
			output(name.substring(0, name.indexOf("_")));
			output(" *)");
		}
		outputln("arg0;");
		return;
	} else {
		String accessor = methodData.getAccessor();
		if (accessor.length() != 0) {
			output(accessor);
		} else {
			output(name);
		}
	}
	generateFunctionCallRightSide(method, methodData, paramTypes, paramStart);
	outputln();
	if (makeCopy) {
		outputln("\t{");
		output("\t\t");
		output(copy);
		output("* copy = new ");
		output(copy);
		outputln("();");
		outputln("\t\t*copy = temp;");
		output("\t\trc = ");
		output("(");
		output(getTypeSignature2(returnType));
		output(")");
		outputln("copy;");
		outputln("\t}");
	}
}

void generateReturn(Method method, Class returnType, boolean needsReturn) {
	if (needsReturn && returnType != Void.TYPE) {
		outputln("\treturn rc;");
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
	outputln(";");
	generateExitMacro(method, function);	
}

void generateFunctionBody(Method method, MethodData methodData, String function, Class[] paramTypes, Class returnType) {
	outputln("{");
	
	/* Custom GTK memmoves. */
	String name = method.getName();
	if (name.startsWith("_")) name = name.substring(1);
	boolean isGTKmemove = name.equals("memmove") && paramTypes.length == 2 && returnType == Void.TYPE;
	if (isGTKmemove) {
		generateGTKmemmove(method, function, paramTypes);
	} else {
		boolean needsReturn = generateLocalVars(method, paramTypes, returnType);
		generateEnterMacro(method, function);
		boolean genFailTag = generateGetters(method, paramTypes);
		if (methodData.getFlag(FLAG_DYNAMIC)) {
			generateDynamicFunctionCall(method, methodData, paramTypes, returnType, needsReturn);
		} else {
			generateFunctionCall(method, methodData, paramTypes, returnType, needsReturn);
		}
		if (genFailTag) outputln("fail:");
		generateSetters(method, paramTypes);
		generateExitMacro(method, function);
		generateReturn(method, returnType, needsReturn);
	}
	
	outputln("}");
}

void generateFunctionPrototype(Method method, String function, Class[] paramTypes, Class returnType) {
	output("JNIEXPORT ");
	output(getTypeSignature2(returnType));
	output(" JNICALL ");
	output(getClassName(method.getDeclaringClass()));
	output("_NATIVE(");
	output(function);
	outputln(")");
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
	outputln(")");
}

void generateSourceStart(String function) {
	output("#ifndef NO_");
	outputln(function);
}

void generateSourceEnd(String function) {
	outputln("#endif");
}

boolean isCritical(Class paramType, ParameterData paramData) {
	return paramType.isArray() && paramType.getComponentType().isPrimitive() && paramData.getFlag(FLAG_CRITICAL);
}

boolean isSystemClass(Class type) {
	return type == Object.class || type == Class.class;
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
