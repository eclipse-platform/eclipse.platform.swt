/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
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

extern JavaVM *JVM;

/* #define DEBUG */

/* if DEBUG is defined print messages from dlerror */
#ifdef DEBUG
#define CHECK_DLERROR \
    char* error = dlerror(); \
    if (error != NULL) { \
        fprintf(stderr, "dlerror: %s\n", error); \
    }
#else
#define CHECK_DLERROR
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
			HMODULE hm = LoadLibraryA(name##_LIB); \
			if (hm) var = GetProcAddress(hm, #name); \
			initialized = 1; \
		}
/*
 * Java's boolean[] may not be used to interface to C on Windows, because:
 * WINAPI 'BOOL'     is 4 bytes
 * Java's 'jboolean' is 1 byte
 * Trying to pass Java's boolean[] will pass wrong data on read and corrupt memory on write.
 * Use java's 'int[]' as a correct interface to 'BOOL'.
 */
#define jbooleanArray DONT_USE_jbooleanArray_USE_intArray_INSTEAD
#else
#define CALLING_CONVENTION
#define LOAD_FLAGS RTLD_LAZY
#define LOAD_FUNCTION(var, name) \
		static int initialized = 0; \
		static void *var = NULL; \
		if (!initialized) { \
			void* handle = dlopen(name##_LIB, LOAD_FLAGS); \
			if (handle) var = dlsym(handle, #name); \
			initialized = 1; \
	                CHECK_DLERROR \
		}
/*
 * Java's boolean[] may not be used to interface to C on Linux, because:
 * GLib's 'gboolean' is 4 bytes
 * Java's 'jboolean' is 1 byte
 * Trying to pass Java's boolean[] will pass wrong data on read and corrupt memory on write.
 * Use java's 'int[]' as a correct interface to 'gboolean'.
 */
#define jbooleanArray DONT_USE_jbooleanArray_USE_intArray_INSTEAD
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
