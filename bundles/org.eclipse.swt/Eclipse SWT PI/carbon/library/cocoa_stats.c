/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

#include "swt.h"
#include "cocoa_stats.h"

#ifdef NATIVE_STATS

char * Cocoa_nativeFunctionNames[] = {
	"HICocoaViewCreate",
	"HIJavaViewCreateWithCocoaView",
	"HIWebViewCreate",
	"HIWebViewGetWebView",
	"NSDeviceRGBColorSpace",
	"NSPointInRect",
	"NSSearchPathForDirectoriesInDomains",
	"WebInitForCarbon",
	"class_1getClassMethod",
	"memcpy",
	"memmove",
	"objc_1getClass",
	"objc_1getMetaClass",
	"objc_1msgSend__II",
	"objc_1msgSend__IID",
	"objc_1msgSend__IIF",
	"objc_1msgSend__IIFF",
	"objc_1msgSend__III",
	"objc_1msgSend__IIII",
	"objc_1msgSend__IIIII",
	"objc_1msgSend__IIIIII",
	"objc_1msgSend__IIIIIIII",
	"objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2",
	"objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2I",
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2",
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2I",
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2",
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2",
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2I",
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2II",
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSSize_2",
	"objc_1msgSend__II_3BI",
	"objc_1msgSend__II_3C",
	"objc_1msgSend__II_3IIIIIIIIIII",
	"objc_1msgSend_1fpret",
#ifndef JNI64
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2II",
#else
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2JJ",
#endif
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2II",
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRect_2I",
	"sel_1registerName",
};
#define NATIVE_FUNCTION_COUNT sizeof(Cocoa_nativeFunctionNames) / sizeof(char*)
int Cocoa_nativeFunctionCount = NATIVE_FUNCTION_COUNT;
int Cocoa_nativeFunctionCallCount[NATIVE_FUNCTION_COUNT];

#define STATS_NATIVE(func) Java_org_eclipse_swt_tools_internal_NativeStats_##func

JNIEXPORT jint JNICALL STATS_NATIVE(Cocoa_1GetFunctionCount)
	(JNIEnv *env, jclass that)
{
	return Cocoa_nativeFunctionCount;
}

JNIEXPORT jstring JNICALL STATS_NATIVE(Cocoa_1GetFunctionName)
	(JNIEnv *env, jclass that, jint index)
{
	return (*env)->NewStringUTF(env, Cocoa_nativeFunctionNames[index]);
}

JNIEXPORT jint JNICALL STATS_NATIVE(Cocoa_1GetFunctionCallCount)
	(JNIEnv *env, jclass that, jint index)
{
	return Cocoa_nativeFunctionCallCount[index];
}

#endif
