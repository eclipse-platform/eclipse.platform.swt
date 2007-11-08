/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

#include "swt.h"
#include "os_structs.h"
#include "os_stats.h"

#define OS_NATIVE(func) Java_org_eclipse_swt_internal_cocoa_OS_##func

#ifndef NO_JNIGetObject
JNIEXPORT jobject JNICALL OS_NATIVE(JNIGetObject)
	(JNIEnv *env, jclass that, jint arg0)
{
	jobject rc = 0;
	OS_NATIVE_ENTER(env, that, JNIGetObject_FUNC);
	rc = (jobject)arg0;
	OS_NATIVE_EXIT(env, that, JNIGetObject_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend_1size
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend_1size)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	NSSize _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1size_FUNC);
	if (arg2) if ((lparg2 = getNSSizeFields(env, arg2, &_arg2)) == NULL) goto fail;
	//rc = (jint)objc_msgSend_size(arg0, arg1, lparg2);
	*lparg2 = [(NSAttributedString *)arg0 size];
fail:
	if (arg2 && lparg2) setNSSizeFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1size_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend_1mouseLocationOutsideOfEventStream
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend_1mouseLocationOutsideOfEventStream)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	NSPoint _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1mouseLocationOutsideOfEventStream_FUNC);
	if (arg2) if ((lparg2 = getNSPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	//rc = (jint)objc_msgSend_mouseLocationOutsideOfEventStream(arg0, arg1, lparg2);
	*lparg2 = [(NSWindow *)arg0 mouseLocationOutsideOfEventStream];
fail:
	if (arg2 && lparg2) setNSPointFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1mouseLocationOutsideOfEventStream_FUNC);
	return rc;
}
#endif


#ifndef NO_objc_1msgSend_1selectedRange
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend_1selectedRange)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	NSRange _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1selectedRange_FUNC);
	if (arg2) if ((lparg2 = getNSRangeFields(env, arg2, &_arg2)) == NULL) goto fail;
	//rc = (jint)objc_msgSend_selectedRange(arg0, arg1, lparg2);
	*lparg2 = [(NSText *)arg0 selectedRange];
fail:
	if (arg2 && lparg2) setNSRangeFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1selectedRange_FUNC);
	return rc;
}
#endif