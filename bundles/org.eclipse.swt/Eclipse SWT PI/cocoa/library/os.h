/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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

//TODO this is needed for objc_super, is it correct?
#define __OBJC2__ 1

#include <Cocoa/Cocoa.h>
#include <WebKit/WebView.h>
#import <objc/objc-runtime.h>

#include "os_custom.h"

extern jint CPSSetProcessName(void *, jintLong);

#ifndef __i386__
#define objc_msgSend_fpret objc_msgSend
#endif

#ifdef __i386__
#define STRUCT_SIZE_LIMIT 8
#elif __ppc__
#define STRUCT_SIZE_LIMIT 4
#elif __x86_64__
#define STRUCT_SIZE_LIMIT 16
#endif

#ifndef NATIVE_STATS
#define OS_NATIVE_ENTER(env, that, func) \
	@try {  
#define OS_NATIVE_EXIT(env, that, func) \
	} \
	@catch (NSException *nsx) { \
		jclass threadClass = (*env)->FindClass(env, "java/lang/Thread"); \
		jmethodID dumpStackID = (*env)->GetStaticMethodID(env, threadClass, "dumpStack", "()V"); \
		if (dumpStackID != NULL) (*env)->CallStaticVoidMethod(env, threadClass, dumpStackID, 0); \
		@throw; \
	}
#endif

#endif /* INC_os_H */