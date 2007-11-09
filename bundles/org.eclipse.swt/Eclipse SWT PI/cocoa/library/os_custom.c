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

static SEL cascadeTopLeftFromPoint = NULL;
#ifndef NO_objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSPoint_2
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSPoint_2)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jobject arg3)
{
	NSPoint _arg0, *lparg0=NULL;
	NSPoint _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	if (arg0) if ((lparg0 = getNSPointFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSPointFields(env, arg3, &_arg3)) == NULL) goto fail;
	
	//rc = (jint)objc_msgSend_struct(lparg0, arg1, arg2, lparg3);
	
	if (cascadeTopLeftFromPoint == NULL) cascadeTopLeftFromPoint = sel_registerName("cascadeTopLeftFromPoint:");
	if ((SEL)arg2 == cascadeTopLeftFromPoint) {
		*lparg0 = [(NSWindow *)arg1 cascadeTopLeftFromPoint: _arg3];
	}
fail:
	if (arg3 && lparg3) setNSPointFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSPointFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	return rc;
}
#endif

static SEL mouseLocationOutsideOfEventStream;
#ifndef NO_objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSPoint_2II
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSPoint_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NSPoint _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSPoint_2II_FUNC);
	if (arg0) if ((lparg0 = getNSPointFields(env, arg0, &_arg0)) == NULL) goto fail;
	
	//rc = (jint)objc_msgSend_struct(lparg0, arg1, arg2);
	
	if (mouseLocationOutsideOfEventStream == 0) mouseLocationOutsideOfEventStream = sel_registerName("mouseLocationOutsideOfEventStream");
	if ((SEL)arg2 == mouseLocationOutsideOfEventStream) {
		*lparg0 = [(NSWindow *)arg1 mouseLocationOutsideOfEventStream];
	}
	
fail:
	if (arg0 && lparg0) setNSPointFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSPoint_2II_FUNC);
	return rc;
}
#endif

static SEL selectedRange;
#ifndef NO_objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSRange_2II
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSRange_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NSRange _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSRange_2II_FUNC);
	if (arg0) if ((lparg0 = getNSRangeFields(env, arg0, &_arg0)) == NULL) goto fail;
	
	//rc = (jint)objc_msgSend_struct(lparg0, arg1, arg2);
	if (selectedRange == 0) selectedRange = sel_registerName("selectedRange");
	if ((SEL)arg2 == selectedRange) {
		*lparg0 = [(NSText *)arg1 selectedRange];
	}
		
fail:
	if (arg0 && lparg0) setNSRangeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSRange_2II_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSSize_2II
static SEL size;
static SEL minimumSize;
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSSize_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NSSize _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSSize_2II_FUNC);
	if (arg0) if ((lparg0 = getNSSizeFields(env, arg0, &_arg0)) == NULL) goto fail;
	
	//rc = (jint)objc_msgSend_struct(lparg0, arg1, arg2);
	if (size == 0) size = sel_registerName("size");
	if ((SEL)arg2 == size) {
		*lparg0 = [(NSAttributedString *)arg1 size];
	} else {
		if (minimumSize == 0) minimumSize = sel_registerName("minimumSize");
		if ((SEL)arg2 == minimumSize) {
			*lparg0 = [(NSTabView *)arg1 minimumSize];
		}
	}
	
fail:
	if (arg0 && lparg0) setNSSizeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSSize_2II_FUNC);
	return rc;
}
#endif


#ifndef NO_drawRect_1CALLBACK
static SWT_PTR drawRect_1CALLBACK;
static void drawRect(id obj, SEL sel, NSRect rect)
{
	return ((void (*)(id, SEL, NSRect*))drawRect_1CALLBACK)(obj, sel, &rect);
}
JNIEXPORT SWT_PTR JNICALL OS_NATIVE(drawRect_1CALLBACK)
	(JNIEnv *env, jclass that, SWT_PTR func)
{
	drawRect_1CALLBACK = func;
	return (SWT_PTR)drawRect;
}
#endif