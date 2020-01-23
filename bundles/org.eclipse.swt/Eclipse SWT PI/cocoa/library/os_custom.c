/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
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

#include "swt.h"
#include "os_structs.h"
#include "os_stats.h"

#define OS_NATIVE(func) Java_org_eclipse_swt_internal_cocoa_OS_##func

#define LOAD_CFSTR(var, name) \
		static int initialized = 0; \
		static CFStringRef *var = NULL; \
		if (!initialized) { \
			CFBundleRef bundle = CFBundleGetBundleWithIdentifier(CFSTR(name##_LIB)); \
			if (bundle) var = CFBundleGetDataPointerForName(bundle, CFSTR(#name)); \
			initialized = 1; \
		} 

#ifndef NO_JNIGetObject
JNIEXPORT jobject JNICALL OS_NATIVE(JNIGetObject)
	(JNIEnv *env, jclass that, jlong arg0)
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

#ifndef NO_class_1getName
JNIEXPORT jstring JNICALL OS_NATIVE(class_1getName)
(JNIEnv *env, jclass that, jlong arg0)
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
JNIEXPORT jlong JNICALL OS_NATIVE(isFlipped_1CALLBACK)
(JNIEnv *env, jclass that)
{
	return (jlong)isFlippedProc;
}
#endif

#ifndef NO_kTISPropertyUnicodeKeyLayoutData
JNIEXPORT jlong JNICALL OS_NATIVE(kTISPropertyUnicodeKeyLayoutData)
(JNIEnv *env, jclass that)
{
	jlong rc = 0;
	OS_NATIVE_ENTER(env, that, kTISPropertyUnicodeKeyLayoutData_FUNC);
/*
	rc = (jlong) kTISPropertyUnicodeKeyLayoutData;
*/
	{
		LOAD_CFSTR(data, kTISPropertyUnicodeKeyLayoutData)
		if (data) {
			rc = (jlong)(*data);
		}
	}
	OS_NATIVE_EXIT(env, that, kTISPropertyUnicodeKeyLayoutData_FUNC);
	return rc;
}
#endif

typedef void (*FunctionPointer)(jlong result);
typedef void (^ObjcBlock)(jlong result);

/*
Method that takes a function pointer as input and returns a objective-c block
which calls the function pointed to by the function pointer.
*/
ObjcBlock functionToBlock(FunctionPointer func) {
    return [[^(jlong result) {
                 func(result);
             } copy] autorelease];
}

/*
Wrapper function which receives a function pointer from Java and calls NSSavePanel.beginSheetModalForWindow
with objective-C block (with block syntax) as the last parameter.
*/ 
#ifndef NO_beginSheetModalForWindow
JNIEXPORT jlong JNICALL OS_NATIVE(beginSheetModalForWindow)
	(JNIEnv *env, jclass that, jlong arg0, jlong arg1, jlong arg2, FunctionPointer arg3)
{
	jlong rc = 0;
	
	OS_NATIVE_ENTER(env, that, beginSheetModalForWindow_FUNC);
	
	rc = (jlong)((jlong (*)(jlong, jlong, jlong, void (^)(jlong)))objc_msgSend)(arg0, arg1, arg2, functionToBlock(arg3));
	
	OS_NATIVE_EXIT(env, that, beginSheetModalForWindow_FUNC);
	return rc;
}
#endif

