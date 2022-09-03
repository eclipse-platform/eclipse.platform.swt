/*******************************************************************************
 * Copyright (c) 2004, 2019 IBM Corporation and others.
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
 *     Red Hat Inc. - stop generating pre 1.2 JNI code
 *******************************************************************************/
package org.eclipse.swt.tools.internal;

import java.lang.reflect.*;
import java.util.*;

public class NativesGenerator extends JNIGenerator {

boolean enterExitMacro;

public NativesGenerator() {
	enterExitMacro = true;
}

@Override
public void generateCopyright() {
	outputln(fixDelimiter(getMetaData().getCopyright()));
}

@Override
public void generateIncludes() {
	String outputName = getOutputName();
	outputln("#include \"swt.h\"");
	output("#include \"");
	output(outputName);
	outputln("_structs.h\"");
	output("#include \"");
	output(outputName);
	outputln("_stats.h\"");
	
	/* 
	 * Note: Only applies to Linux versions of SWT.
	 * Include common structs shared between multiple GTK versions.
	 * Reference Bug 570533, the initial work for separation between 
	 * functions in different GTK versions.
	 */
	if (outputName.equals("gtk3") || outputName.equals("gtk4")) outputln("#include \"os_structs.h\"");
	outputln();
}

public void generate(JNIClass clazz, String methodName) {
	JNIMethod[] methods = clazz.getDeclaredMethods();
	int count = 0;
	for (JNIMethod method : methods) {
		if (method.getName().startsWith(methodName)) count++;
	}
	JNIMethod[] result = new JNIMethod[count];
	count = 0;
	for (JNIMethod method : methods) {
		if (method.getName().startsWith(methodName)) result[count++] = method;
	}
	generate(result);
}

@Override
public void generate(JNIClass clazz) {
	JNIMethod[] methods = clazz.getDeclaredMethods();
	int i = 0;
	for (; i < methods.length; i++) {
		JNIMethod method = methods[i];
		if ((method.getModifiers() & Modifier.NATIVE) != 0) break;
	}
	if (i == methods.length) return;
	sort(methods);
	generateNativeMacro(clazz);
	generateWarningSettings();
	generateExcludes(methods);
	generate(methods);
}

public void generate(JNIMethod[] methods) {
	sort(methods);
	for (JNIMethod method : methods) {
		if ((method.getModifiers() & Modifier.NATIVE) == 0) continue;
		generate(method);
		if (progress != null) progress.step();
	}
}

boolean isStruct(String flagsStr) {
	for (String flag : split(flagsStr, " ")) {
		if (flag.equals(Flags.FLAG_STRUCT)) return true;
	}
	return false;
}

boolean isFloatingPoint(String type) {
	return type.equals("float") || type.equals("double");
}

void generateCallback(JNIMethod method, String function) {
	output("static jlong ");
	output(function);
	outputln(";");
	output("static ");
	String[] types = split((String)method.getParam("callback_types"), ";");
	String[] flags = split((String)method.getParam("callback_flags"), ";");
	output(types[0]);
	output(" ");
	output("proc_");
	output(function);
	output("(");
	boolean first = true;
	for (int i = 1; i < types.length; i++) {
		if (!first) output(", ");
		output(types[i]);
		output(" ");
		output("arg");
		output(String.valueOf(i - 1));
		first = false;
	}
	outputln(") {");

	output("\t");
	if (isStruct(flags[0]) || isFloatingPoint(types[0])) {
		output(types[0]);
		output("* lprc = ");
	} else if (!types[0].equals("void")) {
		output("return ");
	}
	output("((");
	output(types[0]);
	if (isStruct(flags[0]) || isFloatingPoint(types[0])) output("*");
	output(" (*)(");
	first = true;
	for (int i = 1; i < types.length; i++) {
		if (!first) output(", ");
		first = false;
		output(types[i]);
		if (isStruct(flags[i]) || isFloatingPoint(types[i])) output("*");
	}
	output("))");
	output(function);
	output(")(");
	first = true;
	for (int i = 1; i < types.length; i++) {
		if (!first) output(", ");
		first = false;
		if (isStruct(flags[i]) || isFloatingPoint(types[i])) output("&");
		output("arg");
		output(String.valueOf(i -1));
	}
	outputln(");");
	if (isStruct(flags[0]) || isFloatingPoint(types[0])) {
		output("\t");
		output(types[0]);
		outputln(" rc;");
		outputln("\tif (lprc) {");
		outputln("\t\trc = *lprc;");
		outputln("\t\tfree(lprc);");
		outputln("\t} else {");
		output("\t\tmemset(&rc, 0, sizeof(");
		output(types[0]);
		outputln("));");
		outputln("\t}");
		outputln("\treturn rc;");
	}
	outputln("}");

	output("static jlong ");
	output(method.getName());
	outputln("(jlong func) {");
	output("\t");
	output(function);
	outputln(" = func;");
	output("\treturn (jlong)proc_");
	output(function);
	outputln(";");
	outputln("}");
}

public void generate(JNIMethod method) {
	if (method.getFlag(FLAG_NO_GEN)) return;
	JNIType returnType = method.getReturnType();
	if (!(returnType.isType("void") || returnType.isPrimitive() || isSystemClass(returnType) || returnType.isType("java.lang.String"))) {
		output("Warning: bad return type. :");
		outputln(method.toString());
		return;
	}
	JNIParameter[] params = method.getParameters();
	String function = getFunctionName(method);
	generateSourceStart(function);
	generateIgnoreDeprecationsStart(method);
	boolean isCPP = getCPP();
	if (isCPP) {
		output("extern \"C\" ");
		generateFunctionPrototype(method, function, params, returnType, true);
		outputln(";");
	}
	if (function.startsWith("CALLBACK_")) {
		generateCallback(method, function);
	}
	generateFunctionPrototype(method, function, params, returnType, false);
	generateFunctionBody(method, function, params, returnType);
	generateIgnoreDeprecationsEnd(method);
	generateSourceEnd();
	outputln();
}

public void setEnterExitMacro(boolean enterExitMacro) {
	this.enterExitMacro = enterExitMacro;
}

void generateExcludes(JNIMethod[] methods) {
	HashSet<String> excludes = new HashSet<>();
	for (JNIMethod method : methods) {
		if ((method.getModifiers() & Modifier.NATIVE) == 0) continue;
		String exclude = method.getExclude();
		if (exclude.length() != 0) {
			excludes.add(exclude);
		}
	}
	for (String exclude: excludes) {
		outputln(exclude);
		for (JNIMethod method : methods) {
			if ((method.getModifiers() & Modifier.NATIVE) == 0) continue;
			String methodExclude = method.getExclude();
			if (exclude.equals(methodExclude)) {
				output("#define NO_");
				outputln(getFunctionName(method));
			}
		}
		outputln("#endif");
		outputln();
	}
}

void generateNativeMacro(JNIClass clazz) {
	output("#ifndef ");
	output(clazz.getSimpleName());
	outputln("_NATIVE");
	output("#define ");
	output(clazz.getSimpleName());
	output("_NATIVE(func) Java_");
	output(toC(clazz.getName()));
	outputln("_##func");
	outputln("#endif");
	outputln();
}

void generateWarningSettings() {
	outputln("#ifdef _WIN32");
	outputln("  /* Many methods don't use their 'env' and 'that' arguments */");
	outputln("  #pragma warning (disable: 4100)");
	outputln("#endif");
	outputln();
}

boolean generateGetParameter(JNIParameter param, boolean critical, int indent) {
	JNIType paramType = param.getType();
	if (paramType.isPrimitive() || isSystemClass(paramType)) return false;
	String iStr = String.valueOf(param.getParameter());
	for (int j = 0; j < indent; j++) output("\t");
	output("if (arg");
	output(iStr);
	output(") if ((lparg");
	output(iStr);
	output(" = ");
	boolean isCPP = getCPP();
	if (paramType.isArray()) {
		JNIType componentType = paramType.getComponentType();
		if (componentType.isPrimitive()) {
			if (critical) {
				if (isCPP) {
					output("(");
					output(componentType.getTypeSignature2());
					output("*)");
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
				output(componentType.getTypeSignature1());
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
	} else if (paramType.isType("java.lang.String")) {
		if (param.getFlag(FLAG_UNICODE)) {
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
		if (param.getFlag(FLAG_NO_IN)) {
			output("&_arg");
			output(iStr);
		} else {
			output("get");
			output(paramType.getSimpleName());
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

void generateSetParameter(JNIParameter param, boolean critical) {
	JNIType paramType = param.getType();
	if (paramType.isPrimitive() || isSystemClass(paramType)) return;
	String iStr = String.valueOf(param.getParameter());
	boolean isCPP = getCPP();
	if (paramType.isArray()) {
		output("\tif (arg");
		output(iStr);
		output(" && lparg");
		output(iStr);
		output(") ");
		JNIType componentType = paramType.getComponentType();
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
				output(componentType.getTypeSignature1());
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
			if (param.getFlag(FLAG_NO_OUT)) {
				output("JNI_ABORT");
			} else {				
				output("0");
			}
			output(");");
		} else {
			throw new Error("not done");
		}
		outputln();
	} else if (paramType.isType("java.lang.String")) {
		output("\tif (arg");
		output(iStr);
		output(" && lparg");
		output(iStr);
		output(") ");
		if (param.getFlag(FLAG_UNICODE)) {
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
		if (!param.getFlag(FLAG_NO_OUT)) {
			output("\tif (arg");
			output(iStr);
			output(" && lparg");
			output(iStr);
			output(") ");
			output("set");
			output(paramType.getSimpleName());
			output("Fields(env, arg");
			output(iStr);
			output(", lparg");
			output(iStr);
			outputln(");");
		}
	}
}

void generateEnterExitMacro(JNIMethod method, String function, boolean enter) {
	if (!enterExitMacro) return;
	boolean tryCatch = method.getFlag(FLAG_TRYCATCH);
	output("\t");
	output(method.getDeclaringClass().getSimpleName());
	output("_NATIVE_");
	output(enter ? "ENTER" : "EXIT");
	if (tryCatch) output(enter ? "_TRY" : "_CATCH");
	output("(env, that, ");
	output(function);
	outputln("_FUNC);");
}

boolean generateLocalVars(JNIParameter[] params, JNIType returnType) {
	boolean needsReturn = enterExitMacro;
	for (int i = 0; i < params.length; i++) {
		JNIParameter param = params[i];
		JNIType paramType = param.getType();
		if (paramType.isPrimitive() || isSystemClass(paramType)) continue;
		output("\t");
		if (paramType.isArray()) {
			JNIType componentType = paramType.getComponentType();
			if (componentType.isPrimitive()) {
				output(componentType.getTypeSignature2());
				output(" *lparg" + i);
				output("=NULL;");
			} else {
				throw new Error("not done");
			}
		} else if (paramType.isType("java.lang.String")) {
			if (param.getFlag(FLAG_UNICODE)) {
				output("const jchar *lparg" + i);				
			} else {
				output("const char *lparg" + i);
			}
			output("= NULL;");
		} else {
			if (param.getTypeClass().getFlag(Flags.FLAG_STRUCT)) {
				output("struct ");
			}
			output(paramType.getSimpleName());
			output(" _arg" + i);
			if (param.getFlag(FLAG_INIT)) output("={0}");
			output(", *lparg" + i);
			output("=NULL;");
		}
		outputln();
		needsReturn = true;
	}
	if (needsReturn) {
		if (!returnType.isType("void")) {
			output("\t");
			output(returnType.getTypeSignature2());
			outputln(" rc = 0;");
		}
	}
	return needsReturn;
}

boolean generateGetters(JNIParameter[] params) {
	boolean genFailTag = false;
	int criticalCount = 0;
	for (JNIParameter param : params) {
		if (!isCritical(param)) {
			genFailTag |= generateGetParameter(param, false, 1);
		} else {
			criticalCount++;
		}
	}
	if (criticalCount != 0) {
		for (JNIParameter param : params) {
			if (isCritical(param)) {
				genFailTag |= generateGetParameter(param, true, 2);
			}
		}
	}
	return genFailTag;
}

void generateSetters(JNIParameter[] params) {
	int criticalCount = 0;
	for (int i = params.length - 1; i >= 0; i--) {
		JNIParameter param = params[i];
		if (isCritical(param)) {
			criticalCount++;
		}
	}
	if (criticalCount != 0) {
		for (int i = params.length - 1; i >= 0; i--) {
			JNIParameter param = params[i];
			if (isCritical(param)) {
				output("\t");
				generateSetParameter(param, true);
			}
		}
	}
	for (int i = params.length - 1; i >= 0; i--) {
		JNIParameter param = params[i];
		if (!isCritical(param)) {
			generateSetParameter(param, false);
		}
	}
}

void generateDynamicFunctionCall(JNIMethod method, JNIParameter[] params, JNIType returnType, boolean needsReturn) {
	outputln("/*");
	generateFunctionCall(method, params, returnType, needsReturn);
	outputln("*/");
	outputln("\t{");

	String name = method.getName();
	if (name.startsWith("_")) name = name.substring(1);
	output("\t\t");
	output(method.getDeclaringClass().getSimpleName());
	output("_LOAD_FUNCTION(fp, ");
	output(name);
	outputln(")");
	outputln("\t\tif (fp) {");
	output("\t\t");
	generateFunctionCallLeftSide(method, returnType, needsReturn);
	output("((");
	output(returnType.getTypeSignature2());
	output(" (CALLING_CONVENTION*)(");
	for (int i = 0; i < params.length; i++) {
		if (i != 0) output(", ");
		JNIParameter param = params[i];
		String cast = param.getCast();
		boolean isStruct = param.getFlag(FLAG_STRUCT);
		if (cast.length() > 2) {
			cast = cast.substring(1, cast.length() - 1);
			if (isStruct) {
				int index = cast.lastIndexOf('*');
				if (index != -1) cast = cast.substring(0, index).trim();
			}
			output(cast);
		} else {
			JNIType paramType = param.getType();
			output(paramType.getTypeSignature4(isStruct));
		}
	}
	output("))");
	output("fp");
	output(")");
	generateFunctionCallRightSide(method, params, 0);
	output(";");
	outputln();
	outputln("\t\t}");
	outputln("\t}");
}

void generateFunctionCallLeftSide(JNIMethod method, JNIType returnType, boolean needsReturn) {
	output("\t");
	if (!returnType.isType("void")) {
		if (needsReturn) {
			output("rc = ");
		} else {
			output("return ");
		}
		output("(");
		output(returnType.getTypeSignature2());
		output(")");
	}
	if (method.getFlag(FLAG_ADDRESS)) {
		output("&");
	}	
	if (method.getFlag(FLAG_JNI)) {
		boolean isCPP = getCPP();
		output(isCPP ? "env->" : "(*env)->");
	}
}

void generateFunctionCallRightSide(JNIMethod method, JNIParameter[] params, int paramStart) {
	if (!method.getFlag(FLAG_CONST)) {
		output("(");
		if (method.getFlag(FLAG_JNI)) {
			boolean isCPP = getCPP();
			if (!isCPP) output("env, ");
		}
		for (int i = paramStart; i < params.length; i++) {
			JNIParameter param = params[i];
			if (i != paramStart) output(", ");
			if (param.getFlag(FLAG_STRUCT)) output("*");
			output(param.getCast());
			if (param.getFlag(FLAG_OBJECT)) output("TO_OBJECT(");
			if (i == params.length - 1 && param.getFlag(FLAG_SENTINEL)) {
				output("NULL");
			} else {
				JNIType paramType = param.getType();
				if (!paramType.isPrimitive() && !isSystemClass(paramType)) output("lp");
				output("arg" + i);
			}
			if (param.getFlag(FLAG_OBJECT)) output(")");
		}
		output(")");
	}
}

void generateFunctionCall(JNIMethod method, JNIParameter[] params, JNIType returnType, boolean needsReturn) {
	String name = method.getName();
	String copy = (String)method.getParam("copy");
	boolean isCPP = getCPP();
	boolean makeCopy = copy.length() != 0 && isCPP && !returnType.isType("void");
	if (makeCopy) {
		output("\t");
		output(copy);
		output(" temp = ");
	} else {
		generateFunctionCallLeftSide(method, returnType, needsReturn);
	}
	int paramStart = 0;
	if (name.startsWith("_")) name = name.substring(1);

	boolean objc_struct = false;
	if (name.equals("objc_msgSend_stret") || name.equals("objc_msgSendSuper_stret")) objc_struct = true;
	if (objc_struct) {
		outputln("if (STRUCT_SIZE_LIMIT == 0) {");
		output("\t\t");
	}
	if (name.equalsIgnoreCase("call") || name.startsWith("callFunc")) {
		output("(");
		String cast = params[0].getCast(); 
		if (cast.length() != 0 && !cast.equals("()")) {
			output(cast);
		} else {
			output("(");
			output(returnType.getTypeSignature2());
			output(" (");
			output((String)method.getParam("convention"));
			output("*)())");
		}
		output("arg0)");
		paramStart = 1;
	} else if (name.startsWith("VtblCall") || name.startsWith("_VtblCall")) {
		output("((");
		output(returnType.getTypeSignature2());
		output(" (STDMETHODCALLTYPE *)(");
		for (int i = 1; i < params.length; i++) {
			if (i != 1) output(", ");
			JNIParameter param = params[i];
			JNIType paramType = param.getType();
			output(paramType.getTypeSignature4(param.getFlag(FLAG_STRUCT)));
		}
		output("))(*(");
		JNIType paramType = params[1].getType();
		output(paramType.getTypeSignature4(false));
		output(" **)arg1)[arg0])");
		paramStart = 1;
	} else if (method.getFlag(FLAG_CPP) || method.getFlag(FLAG_SETTER) || method.getFlag(FLAG_GETTER) || method.getFlag(FLAG_ADDER)) {
		if (method.getFlag(FLAG_OBJECT)) {
			output("TO_HANDLE(");
		}
		output("(");
		JNIParameter param = params[0];
		if (param.getFlag(FLAG_STRUCT)) output("*");
		String cast = param.getCast(); 
		if (cast.length() != 0 && !cast.equals("()")) {
			output(cast);
		}
		if (param.getFlag(FLAG_OBJECT)) {
			output("TO_OBJECT(");
		}
		output("arg0");
		if (param.getFlag(FLAG_OBJECT)) {
			output(")");
		}
		output(")->");
		String accessor = method.getAccessor();
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
	} else if (method.getFlag(FLAG_GCNEW)) {
		output("TO_HANDLE(gcnew ");
		String accessor = method.getAccessor();
		if (accessor.length() != 0) {
			output(accessor);
		} else {
			int index = -1;
			if ((index = name.indexOf('_')) != -1) {
				output(name.substring(index + 1));
			} else {
				output(name);
			}
		}
	} else if (method.getFlag(FLAG_NEW)) {
		if (method.getFlag(FLAG_OBJECT)) {
			output("TO_HANDLE(");
		}
		output("new ");
		String accessor = method.getAccessor();
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
	} else if (method.getFlag(FLAG_DELETE)) {
		output("delete ");
		JNIParameter param = params[0];
		String cast = param.getCast(); 
		if (cast.length() != 0 && !cast.equals("()")) {
			output(cast);
		} else {
			output("(");
			output(name.substring(0, name.indexOf('_')));
			output(" *)");
		}
		outputln("arg0;");
		return;
	} else {
		if (method.getFlag(FLAG_OBJECT)) {
			output("TO_HANDLE(");				
		}
		if (method.getFlag(Flags.FLAG_CAST)) {
			output("((");
			String returnCast = returnType.getTypeSignature2();
			if (name.equals("objc_msgSend_bool") && returnCast.equals("jboolean")) {
				returnCast = "BOOL";
			}
			output(returnCast);
			output(" (*)(");
			for (int i = 0; i < params.length; i++) {
				if (i != 0) output(", ");
				JNIParameter param = params[i];
				String cast = param.getCast();
				if (cast != null && cast.length() != 0) {
					if (cast.startsWith("(")) cast = cast.substring(1);
					if (cast.endsWith(")")) cast = cast.substring(0, cast.length() - 1);
					output(cast);
				} else {
					JNIType paramType = param.getType();
					if (!(paramType.isPrimitive() || paramType.isArray())) {
						if (param.getTypeClass().getFlag(FLAG_STRUCT)) {
							output("struct ");
						}
					}
					output(paramType.getTypeSignature4(param.getFlag(FLAG_STRUCT)));
				}
			}
			output("))");
		}
		String accessor = method.getAccessor();
		if (accessor.length() != 0) {
			output(accessor);
		} else {
			output(name);
		}
		if (method.getFlag(Flags.FLAG_CAST)) {
			output(")");
		}
	}
	if ((method.getFlag(FLAG_SETTER) && params.length == 3) || (method.getFlag(FLAG_GETTER) && params.length == 2)) {
		output("[arg1]");
		paramStart++;
	}
	if (method.getFlag(FLAG_SETTER)) output(" = ");
	if (method.getFlag(FLAG_ADDER)) output(" += ");
	if (!method.getFlag(FLAG_GETTER)) {
		generateFunctionCallRightSide(method, params, paramStart);
	}
	if (method.getFlag(FLAG_GCNEW) || method.getFlag(FLAG_OBJECT)) {
		output(")");
	}
	output(";");
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
		output(returnType.getTypeSignature2());
		output(")");
		outputln("copy;");
		outputln("\t}");
	}
	if (objc_struct) {
		outputln("\t} else if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {");
		generate_objc_msgSend_stret (params, name);
		generateFunctionCallRightSide(method, params, 1);
		outputln(";");
		outputln("\t} else {");
		generate_objc_msgSend_stret (params, name.substring(0, name.length() - "_stret".length()));
		generateFunctionCallRightSide(method, params, 1);
		outputln(";");
		outputln("\t}");
	}
}

void generate_objc_msgSend_stret (JNIParameter[] params, String func) {
	output("\t\t*lparg0 = (*(");
	JNIType paramType = params[0].getType();
	output(paramType.getTypeSignature4(true));
	output(" (*)(");
	for (int i = 1; i < params.length; i++) {
		if (i != 1) output(", ");
		JNIParameter param = params[i];
		String cast = param.getCast();
		if (cast != null && cast.length() != 0) {
			if (cast.startsWith("(")) cast = cast.substring(1);
			if (cast.endsWith(")")) cast = cast.substring(0, cast.length() - 1);
			output(cast);
		} else {
			paramType = param.getType();
			if (!(paramType.isPrimitive() || paramType.isArray())) {
				if (param.getTypeClass().getFlag(FLAG_STRUCT)) {
					output("struct ");
				}
			}
			output(paramType.getTypeSignature4(param.getFlag(FLAG_STRUCT)));
		}
	}
	output("))");
	output(func);
	output(")");
}

void generateReturn(JNIType returnType, boolean needsReturn) {
	if (needsReturn && !returnType.isType("void")) {
		outputln("\treturn rc;");
	}
}

void generateMemmove(JNIMethod method, String function, JNIParameter[] params) {
	generateEnterExitMacro(method, function, true);
	output("\t");
	boolean get = params[0].getType().isPrimitive();
	String className = params[get ? 1 : 0].getType().getSimpleName();
	output(get ? "if (arg1) get" : "if (arg0) set");
	output(className);
	output(get ? "Fields(env, arg1, (" : "Fields(env, arg0, (");
	output(className);
	output(get ? " *)arg0)" : " *)arg1)");
	outputln(";");
	generateEnterExitMacro(method, function, false);	
}

void generateFunctionBody(JNIMethod method, String function, JNIParameter[] params, JNIType returnType) {
	outputln("{");
	
	/* Custom GTK memmoves. */
	String name = method.getName();
	if (name.startsWith("_")) name = name.substring(1);
	boolean isMemove = (name.equals("memmove") || name.equals("MoveMemory")) && params.length == 2 && returnType.isType("void");
	if (isMemove) {
		generateMemmove(method, function, params);
	} else {
		boolean needsReturn = generateLocalVars(params, returnType);
		generateEnterExitMacro(method, function, true);
		boolean genFailTag = generateGetters(params);
		if (method.getFlag(FLAG_DYNAMIC)) {
			generateDynamicFunctionCall(method, params, returnType, needsReturn);
		} else {
			generateFunctionCall(method, params, returnType, needsReturn);
		}
		if (genFailTag) outputln("fail:");
		generateSetters(params);
		generateEnterExitMacro(method, function, false);
		generateReturn(returnType, needsReturn);
	}
	
	outputln("}");
}

void generateFunctionPrototype(JNIMethod method, String function, JNIParameter[] params, JNIType returnType, boolean singleLine) {
	output("JNIEXPORT ");
	output(returnType.getTypeSignature2());
	output(" JNICALL ");
	output(method.getDeclaringClass().getSimpleName());
	output("_NATIVE(");
	output(function);
	if (singleLine) {
		output(")");
		output("(JNIEnv *env, ");
	} else {
		outputln(")");
		output("\t(JNIEnv *env, ");
	}
	if ((method.getModifiers() & Modifier.STATIC) != 0) {
		output("jclass");
	} else {
		output("jobject");
	}
	output(" that");
	for (int i = 0; i < params.length; i++) {
		output(", ");
		JNIType paramType = params[i].getType();
		output(paramType.getTypeSignature2());
		output(" arg" + i);
	}
	output(")");
	if (!singleLine) outputln();
}

void generateSourceStart(String function) {
	output("#ifndef NO_");
	outputln(function);
}

void generateSourceEnd() {
	outputln("#endif");
}

void generateIgnoreDeprecationsStart(JNIMethod method) {
	if (method.getFlag(Flags.FLAG_IGNORE_DEPRECATIONS))
		outputln("G_GNUC_BEGIN_IGNORE_DEPRECATIONS");
}

void generateIgnoreDeprecationsEnd(JNIMethod method) {
	if (method.getFlag(Flags.FLAG_IGNORE_DEPRECATIONS))
		outputln("G_GNUC_END_IGNORE_DEPRECATIONS");
}

boolean isCritical(JNIParameter param) {
	JNIType paramType = param.getType();
	return paramType.isArray() && paramType.getComponentType().isPrimitive() && param.getFlag(FLAG_CRITICAL);
}

boolean isSystemClass(JNIType type) {
	return type.isType("java.lang.Object") || type.isType("java.lang.Class") ;
}

}
