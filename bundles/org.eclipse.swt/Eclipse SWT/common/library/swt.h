/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
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

extern int IS_JNI_1_2;

/* 64 bit support */
#ifndef SWT_PTR_SIZE_64

#define GetSWT_PTRField GetIntField
#define SetSWT_PTRField SetIntField
#define NewSWT_PTRArray NewIntArray
#define CallStaticSWT_PTRMethodV CallStaticIntMethodV
#define CallSWT_PTRMethodV CallIntMethodV
#define SWT_PTRArray jintArray
#define SWT_PTR jint
#define SWT_PTR_SIGNATURE "I"

#else

#define GetSWT_PTRField GetLongField
#define SetSWT_PTRField SetLongField
#define NewSWT_PTRArray NewLongArray
#define CallStaticSWT_PTRMethodV CallStaticLongMethodV
#define CallSWT_PTRMethodV CallLongMethodV
#define SWT_PTRArray jlongArray
#define SWT_PTR jlong
#define SWT_PTR_SIGNATURE "J"

#endif

/* For debugging */
#define DEBUG_PRINTF(x)
/*#define DEBUG_PRINTF(x) printf x; */

/* define this to print out debug statements */
/* #define DEBUG_CALL_PRINTS */
/* #define DEBUG_CHECK_NULL_EXCEPTIONS */

#ifdef DEBUG_CALL_PRINTS
#define DEBUG_CALL(func) fprintf(stderr, func);
#else
#define DEBUG_CALL(func)
#endif

#ifdef DEBUG_CHECK_NULL_EXCEPTIONS
#define DEBUG_CHECK_NULL(env, address) \
	if (address == 0) { \
		jclass clazz = (*env)->FindClass(env, "org/eclipse/swt/SWTError"); \
		if (clazz != NULL) { \
			(*env)->ThrowNew(env, clazz, "Argument cannot be NULL"); \
		} \
		return; \
	}
#else
#define DEBUG_CHECK_NULL(env, address)
#endif

#endif /* ifndef INC_swt_H */
