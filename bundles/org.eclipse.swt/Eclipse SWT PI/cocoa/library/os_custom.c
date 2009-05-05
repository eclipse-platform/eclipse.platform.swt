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
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jobject rc = 0;
	OS_NATIVE_ENTER(env, that, JNIGetObject_FUNC);
	rc = (jobject)arg0;
	OS_NATIVE_EXIT(env, that, JNIGetObject_FUNC);
	return rc;
}
#endif

#ifndef NO_NSIntersectionRect
JNIEXPORT void JNICALL OS_NATIVE(NSIntersectionRect)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jobject arg2)
{
	NSRect _arg0, *lparg0=NULL;
	NSRect _arg1, *lparg1=NULL;
	NSRect _arg2, *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, NSIntersectionRect_FUNC);
	if (arg0) if ((lparg0 = getNSRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getNSRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	*lparg0 = NSIntersectionRect(*lparg1, *lparg2);
fail:
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
	if (arg1 && lparg1) setNSRectFields(env, arg1, lparg1);
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, NSIntersectionRect_FUNC);
}
#endif

#ifndef NO_CGDisplayBounds
JNIEXPORT void JNICALL OS_NATIVE(CGDisplayBounds)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	CGRect _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, CGDisplayBounds_FUNC);
	if (arg1) if ((lparg1 = getCGRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	*lparg1 = CGDisplayBounds((CGDirectDisplayID)arg0);
fail:
	if (arg1 && lparg1) setCGRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, CGDisplayBounds_FUNC);
}
#endif

#ifndef NO__1_1BIG_1ENDIAN_1_1
JNIEXPORT jboolean JNICALL OS_NATIVE(_1_1BIG_1ENDIAN_1_1)
	(JNIEnv *env, jclass that)
{
	jboolean rc;
	OS_NATIVE_ENTER(env, that, _1_1BIG_1ENDIAN_1_1_FUNC)
#ifdef __BIG_ENDIAN__
	rc = (jboolean)TRUE;
#else
	rc = (jboolean)FALSE;
#endif
	OS_NATIVE_EXIT(env, that, _1_1BIG_1ENDIAN_1_1_FUNC)
	return rc;
}
#endif

#ifndef NO_class_1getName
JNIEXPORT jstring JNICALL OS_NATIVE(class_1getName)
(JNIEnv *env, jclass that, jintLong arg0)
{
	jstring rc = 0;
	OS_NATIVE_ENTER(env, that, class_1getName_FUNC);
	const char *className = class_getName((Class)arg0);
	if (className != NULL) rc = (*env)->NewStringUTF(env, className);
	OS_NATIVE_EXIT(env, that, class_1getName_FUNC);
	return rc;
}
#endif

#ifndef NO_isFlipped_1CALLBACK
static BOOL isFlippedProc(id obj, SEL sel)
{
	return YES;
}
JNIEXPORT jintLong JNICALL OS_NATIVE(isFlipped_1CALLBACK)
(JNIEnv *env, jclass that)
{
	return (jintLong)isFlippedProc;
}
#endif

#ifndef NO_kTISPropertyUnicodeKeyLayoutData
JNIEXPORT jintLong JNICALL OS_NATIVE(kTISPropertyUnicodeKeyLayoutData)
(JNIEnv *env, jclass that)
{
	// Technically this CFStringRef should be CFRetain'ed but we have no opportunity to release it.
	// The pointer won't disappear unless the Carbon framework bundle is somehow unloaded, which is unlikely to happen.
	static int initialized = 0;
	static CFStringRef *var = NULL;
	if (!initialized) {
		CFBundleRef bundle = CFBundleGetBundleWithIdentifier(CFSTR("com.apple.Carbon"));
		if (bundle) var = (CFStringRef *)CFBundleGetDataPointerForName(bundle, CFSTR("kTISPropertyUnicodeKeyLayoutData"));
		initialized = 1;
	} 
	
	return (jintLong)(*var);
}
#endif
