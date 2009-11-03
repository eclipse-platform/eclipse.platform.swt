/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
 
/**
 * swt.h
 *
 * This file contains the global macro declarations for the
 * SWT library.
 *
 */

#ifndef INC_swt_H
#define INC_swt_H

#include "jni.h"

#ifdef __cplusplus
extern "C" {
#endif

extern int IS_JNI_1_2;

/* 64 bit support */
#ifndef SWT_PTR_SIZE_64

#define GetSWT_PTRField GetIntField
#define SetSWT_PTRField SetIntField
#define NewSWT_PTRArray NewIntArray
#define CallStaticSWT_PTRMethodV CallStaticIntMethodV
#define CallSWT_PTRMethodV CallIntMethodV
#define CallStaticSWT_PTRMethod CallStaticIntMethod
#define CallSWT_PTRMethod CallIntMethod
#define GetSWT_PTRArrayElements GetIntArrayElements
#define ReleaseSWT_PTRArrayElements ReleaseIntArrayElements
#define SWT_PTRArray jintArray
#define SWT_PTR jint
#define SWT_PTR_SIGNATURE "I"

#else

#define GetSWT_PTRField GetLongField
#define SetSWT_PTRField SetLongField
#define NewSWT_PTRArray NewLongArray
#define CallStaticSWT_PTRMethodV CallStaticLongMethodV
#define CallSWT_PTRMethodV CallLongMethodV
#define CallStaticSWT_PTRMethod CallStaticLongMethod
#define CallSWT_PTRMethod CallLongMethod
#define GetSWT_PTRArrayElements GetLongArrayElements
#define ReleaseSWT_PTRArrayElements ReleaseLongArrayElements
#define SWT_PTRArray jlongArray
#define SWT_PTR jlong
#define SWT_PTR_SIGNATURE "J"

#endif

#ifdef __APPLE__
#define CALLING_CONVENTION
#define LOAD_FUNCTION(var, name) \
		static int initialized = 0; \
		static void *var = NULL; \
		if (!initialized) { \
			CFBundleRef bundle = CFBundleGetBundleWithIdentifier(CFSTR(name##_LIB)); \
			if (bundle) var = CFBundleGetFunctionPointerForName(bundle, CFSTR(#name)); \
			initialized = 1; \
		} 
#elif defined (_WIN32) || defined (_WIN32_WCE)
#define CALLING_CONVENTION CALLBACK
#define LOAD_FUNCTION(var, name) \
		static int initialized = 0; \
		static FARPROC var = NULL; \
		if (!initialized) { \
			HMODULE hm = LoadLibrary(name##_LIB); \
			if (hm) var = GetProcAddress(hm, #name); \
			initialized = 1; \
		}
#else
#define CALLING_CONVENTION
#define LOAD_FUNCTION(var, name) \
		static int initialized = 0; \
		static void *var = NULL; \
		if (!initialized) { \
			void* handle = dlopen(name##_LIB, RTLD_LAZY); \
			if (handle) var = dlsym(handle, #name); \
			initialized = 1; \
		}
#endif

void throwOutOfMemory(JNIEnv *env);

#define CHECK_NULL_VOID(ptr) \
	if ((ptr) == NULL) { \
		throwOutOfMemory(env); \
		return; \
	}

#define CHECK_NULL(ptr) \
	if ((ptr) == NULL) { \
		throwOutOfMemory(env); \
		return 0; \
	}

#ifdef __cplusplus
}
#endif 

#endif /* ifndef INC_swt_H */
