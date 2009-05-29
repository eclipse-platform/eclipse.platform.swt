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

#ifndef INC_os_H
#define INC_os_H

/*#define NDEBUG*/
/*#define DEBUG_EXCEPTIONS*/

#include <Cocoa/Cocoa.h>
#include <WebKit/WebView.h>
#import <objc/objc-runtime.h>

#include "os_custom.h"

extern jint CPSSetProcessName(void *, jintLong);

#define objc_msgSend_bool objc_msgSend
#define objc_msgSendSuper_bool objc_msgSendSuper

#ifndef __i386__
#define objc_msgSend_fpret objc_msgSend
#endif

/* The structure objc_super defines "class" in i386/ppc and "super_class" in x86_64 */
#ifdef __i386__
#define swt_super_class class
#elif __ppc__
#define swt_super_class class
#elif __x86_64__
#define swt_super_class super_class
#endif

#ifdef __i386__
#define STRUCT_SIZE_LIMIT 8
#elif __ppc__
#define STRUCT_SIZE_LIMIT 4
#elif __x86_64__
#define STRUCT_SIZE_LIMIT 16
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
	} \
	@catch (NSException *nsx) { \
		DUMP_EXCEPTION \
	}
#endif

#endif /* INC_os_H */

