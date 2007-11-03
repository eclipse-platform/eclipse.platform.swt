/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

#include "swt.h"
#include "os_stats.h"

#ifdef NATIVE_STATS

int OS_nativeFunctionCount = 45;
int OS_nativeFunctionCallCount[45];
char * OS_nativeFunctionNames[] = {
	"DeleteGlobalRef",
	"GetCurrentProcess",
	"JNIGetObject",
	"NSBitsPerPixelFromDepth",
	"NSDeviceRGBColorSpace",
	"NewGlobalRef",
	"SetFrontProcess",
	"TransformProcessType",
	"class_1addIvar",
	"class_1addMethod",
	"objc_1allocateClassPair",
	"objc_1getClass",
	"objc_1lookUpClass",
	"objc_1msgSend__II",
	"objc_1msgSend__IIF",
	"objc_1msgSend__IIFF",
	"objc_1msgSend__IIFFFF",
	"objc_1msgSend__III",
	"objc_1msgSend__IIIF",
	"objc_1msgSend__IIII",
	"objc_1msgSend__IIIII",
	"objc_1msgSend__IIIIII",
	"objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2",
	"objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2I",
	"objc_1msgSend__IILjava_lang_String_2",
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2",
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2I",
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2",
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2I",
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IIZ",
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSSize_2",
	"objc_1msgSend__II_3C",
	"objc_1msgSend__II_3CI",
	"objc_1msgSend__II_3F",
	"objc_1msgSend__II_3IIIIIIIIIII",
	"objc_1msgSend_1fpret",
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSPoint_2",
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2II",
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2II",
	"objc_1msgSend_1stret___3FII",
	"objc_1registerClassPair",
	"object_1getClassName",
	"object_1getInstanceVariable",
	"object_1setInstanceVariable",
	"sel_1registerName",
};

#define STATS_NATIVE(func) Java_org_eclipse_swt_tools_internal_NativeStats_##func

JNIEXPORT jint JNICALL STATS_NATIVE(OS_1GetFunctionCount)
	(JNIEnv *env, jclass that)
{
	return OS_nativeFunctionCount;
}

JNIEXPORT jstring JNICALL STATS_NATIVE(OS_1GetFunctionName)
	(JNIEnv *env, jclass that, jint index)
{
	return (*env)->NewStringUTF(env, OS_nativeFunctionNames[index]);
}

JNIEXPORT jint JNICALL STATS_NATIVE(OS_1GetFunctionCallCount)
	(JNIEnv *env, jclass that, jint index)
{
	return OS_nativeFunctionCallCount[index];
}

#endif
