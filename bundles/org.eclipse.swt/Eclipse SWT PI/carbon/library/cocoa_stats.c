/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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

int Cocoa_nativeFunctionCount = 18;
int Cocoa_nativeFunctionCallCount[18];
char * Cocoa_nativeFunctionNames[] = {
	"HIWebViewCreate",
	"HIWebViewGetWebView",
	"WebInitForCarbon",
	"memcpy",
	"objc_1getClass",
	"objc_1msgSend__II",
	"objc_1msgSend__IIF",
	"objc_1msgSend__III",
	"objc_1msgSend__IIII",
	"objc_1msgSend__IIIII",
	"objc_1msgSend__IIIIII",
	"objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2I",
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2",
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2I",
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2",
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2I",
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSSize_2",
	"sel_1registerName",
};

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
