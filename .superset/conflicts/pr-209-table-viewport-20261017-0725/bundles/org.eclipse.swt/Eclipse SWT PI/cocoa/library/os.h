/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
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

#ifndef INC_os_H
#define INC_os_H

/*#define NDEBUG*/
/*#define DEBUG_EXCEPTIONS*/

#import <objc/objc-runtime.h>
#include <Cocoa/Cocoa.h>
#include <Carbon/Carbon.h>
#include <WebKit/WebKit.h>
#include <JavaScriptCore/JavaScriptCore.h>
#include "mach-o/dyld.h"

#include "os_custom.h"

extern jint CPSSetProcessName(void *, jlong);

#define OS_LOAD_FUNCTION LOAD_FUNCTION

#define objc_msgSend_bool objc_msgSend
#define objc_msgSendSuper_bool objc_msgSendSuper

#define objc_msgSend_floatret objc_msgSend_fpret
#ifndef __i386__
#define objc_msgSend_fpret objc_msgSend
#endif

/* The structure objc_super defines "class" in i386/ppc and "super_class" in x86_64 */
#ifdef __x86_64__
#define swt_super_class super_class
#elif __arm64__
#define swt_super_class super_class
#endif

/* STRUCT_SIZE_LIMIT is the maximum size of struct that can be returned using registers */
/* When sizeof(struct) is greater than this limit, objc_msgSend*_stret call is used */
/* objc_msgSend*_stret methods are not available on arm64 architecture, so objc_msgSend* calls are always used */
#ifdef __x86_64__
#define STRUCT_SIZE_LIMIT 16
#elif __arm64__
#define STRUCT_SIZE_LIMIT 64
#define objc_msgSendSuper_stret objc_msgSendSuper
#define objc_msgSend_stret objc_msgSend
#endif

#ifdef DEBUG_EXCEPTIONS
#define DUMP_EXCEPTION \
	if (![[nsx name] isEqualToString:NSAccessibilityException])  { \
		NSLog(@"Exception thrown: %@ %@", [nsx name], [nsx reason]); \
		jclass threadClass = (*env)->FindClass(env, "java/lang/Thread"); \
		jmethodID dumpStackID = (*env)->GetStaticMethodID(env, threadClass, "dumpStack", "()V"); \
		if (dumpStackID != NULL) (*env)->CallStaticVoidMethod(env, threadClass, dumpStackID, 0); \
	}
#else
#define DUMP_EXCEPTION
#endif

#ifndef NATIVE_STATS
#define OS_NATIVE_ENTER(env, that, func) \
	@try {  
#define OS_NATIVE_EXIT(env, that, func) \
	; } \
	@catch (NSException *nsx) { \
		DUMP_EXCEPTION \
	}
#endif

#endif /* INC_os_H */

