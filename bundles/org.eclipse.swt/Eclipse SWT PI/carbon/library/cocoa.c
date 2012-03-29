/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

#include "swt.h"
#include "cocoa_structs.h"
#include "cocoa_stats.h"

#ifndef Cocoa_NATIVE
#define Cocoa_NATIVE(func) Java_org_eclipse_swt_internal_cocoa_Cocoa_##func
#endif

#ifndef NO_HICocoaViewCreate
JNIEXPORT jint JNICALL Cocoa_NATIVE(HICocoaViewCreate)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	Cocoa_NATIVE_ENTER(env, that, HICocoaViewCreate_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
/*
	rc = (jint)HICocoaViewCreate(arg0, arg1, lparg2);
*/
	{
		Cocoa_LOAD_FUNCTION(fp, HICocoaViewCreate)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jint, jint, jint *))fp)(arg0, arg1, lparg2);
		}
	}
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	Cocoa_NATIVE_EXIT(env, that, HICocoaViewCreate_FUNC);
	return rc;
}
#endif

#ifndef NO_HIWebViewCreate
JNIEXPORT jint JNICALL Cocoa_NATIVE(HIWebViewCreate)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc = 0;
	Cocoa_NATIVE_ENTER(env, that, HIWebViewCreate_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)HIWebViewCreate((HIViewRef *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	Cocoa_NATIVE_EXIT(env, that, HIWebViewCreate_FUNC);
	return rc;
}
#endif

#ifndef NO_HIWebViewGetWebView
JNIEXPORT jint JNICALL Cocoa_NATIVE(HIWebViewGetWebView)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	Cocoa_NATIVE_ENTER(env, that, HIWebViewGetWebView_FUNC);
	rc = (jint)HIWebViewGetWebView((HIViewRef)arg0);
	Cocoa_NATIVE_EXIT(env, that, HIWebViewGetWebView_FUNC);
	return rc;
}
#endif

#ifndef NO_NSDeviceRGBColorSpace
JNIEXPORT jint JNICALL Cocoa_NATIVE(NSDeviceRGBColorSpace)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	Cocoa_NATIVE_ENTER(env, that, NSDeviceRGBColorSpace_FUNC);
	rc = (jint)NSDeviceRGBColorSpace;
	Cocoa_NATIVE_EXIT(env, that, NSDeviceRGBColorSpace_FUNC);
	return rc;
}
#endif

#ifndef NO_NSPointInRect
JNIEXPORT jboolean JNICALL Cocoa_NATIVE(NSPointInRect)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1)
{
	NSPoint _arg0, *lparg0=NULL;
	NSRect _arg1, *lparg1=NULL;
	jboolean rc = 0;
	Cocoa_NATIVE_ENTER(env, that, NSPointInRect_FUNC);
	if (arg0) if ((lparg0 = getNSPointFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getNSRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jboolean)NSPointInRect(*lparg0, *lparg1);
fail:
	if (arg1 && lparg1) setNSRectFields(env, arg1, lparg1);
	if (arg0 && lparg0) setNSPointFields(env, arg0, lparg0);
	Cocoa_NATIVE_EXIT(env, that, NSPointInRect_FUNC);
	return rc;
}
#endif

#ifndef NO_NSSearchPathForDirectoriesInDomains
JNIEXPORT jint JNICALL Cocoa_NATIVE(NSSearchPathForDirectoriesInDomains)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	jint rc = 0;
	Cocoa_NATIVE_ENTER(env, that, NSSearchPathForDirectoriesInDomains_FUNC);
	rc = (jint)NSSearchPathForDirectoriesInDomains((NSSearchPathDirectory)arg0, (NSSearchPathDomainMask)arg1, (BOOL)arg2);
	Cocoa_NATIVE_EXIT(env, that, NSSearchPathForDirectoriesInDomains_FUNC);
	return rc;
}
#endif

#ifndef NO_WebInitForCarbon
JNIEXPORT void JNICALL Cocoa_NATIVE(WebInitForCarbon)
	(JNIEnv *env, jclass that)
{
	Cocoa_NATIVE_ENTER(env, that, WebInitForCarbon_FUNC);
	WebInitForCarbon();
	Cocoa_NATIVE_EXIT(env, that, WebInitForCarbon_FUNC);
}
#endif

#ifndef NO_class_1getClassMethod
JNIEXPORT jintLong JNICALL Cocoa_NATIVE(class_1getClassMethod)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	Cocoa_NATIVE_ENTER(env, that, class_1getClassMethod_FUNC);
	rc = (jintLong)class_getClassMethod((Class)arg0, (SEL)arg1);
	Cocoa_NATIVE_EXIT(env, that, class_1getClassMethod_FUNC);
	return rc;
}
#endif

#ifndef NO_memcpy
JNIEXPORT void JNICALL Cocoa_NATIVE(memcpy)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NSRect _arg0, *lparg0=NULL;
	Cocoa_NATIVE_ENTER(env, that, memcpy_FUNC);
	if (arg0) if ((lparg0 = getNSRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	memcpy(lparg0, (void *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
	Cocoa_NATIVE_EXIT(env, that, memcpy_FUNC);
}
#endif

#ifndef NO_memmove
JNIEXPORT void JNICALL Cocoa_NATIVE(memmove)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NSPoint _arg0, *lparg0=NULL;
	Cocoa_NATIVE_ENTER(env, that, memmove_FUNC);
	if (arg0) if ((lparg0 = getNSPointFields(env, arg0, &_arg0)) == NULL) goto fail;
	memmove(lparg0, (void *)arg1, arg2);
fail:
	if (arg0 && lparg0) setNSPointFields(env, arg0, lparg0);
	Cocoa_NATIVE_EXIT(env, that, memmove_FUNC);
}
#endif

#ifndef NO_objc_1getClass
JNIEXPORT jint JNICALL Cocoa_NATIVE(objc_1getClass)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	Cocoa_NATIVE_ENTER(env, that, objc_1getClass_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)objc_getClass((const char *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	Cocoa_NATIVE_EXIT(env, that, objc_1getClass_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1getMetaClass
JNIEXPORT jint JNICALL Cocoa_NATIVE(objc_1getMetaClass)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	Cocoa_NATIVE_ENTER(env, that, objc_1getMetaClass_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)objc_getMetaClass((const char *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	Cocoa_NATIVE_EXIT(env, that, objc_1getMetaClass_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__II
JNIEXPORT jint JNICALL Cocoa_NATIVE(objc_1msgSend__II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	Cocoa_NATIVE_ENTER(env, that, objc_1msgSend__II_FUNC);
	rc = (jint)objc_msgSend((id)arg0, (SEL)arg1);
	Cocoa_NATIVE_EXIT(env, that, objc_1msgSend__II_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IID
JNIEXPORT jint JNICALL Cocoa_NATIVE(objc_1msgSend__IID)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jdouble arg2)
{
	jint rc = 0;
	Cocoa_NATIVE_ENTER(env, that, objc_1msgSend__IID_FUNC);
	rc = (jint)((jint (*)(id, SEL, jdouble))objc_msgSend)((id)arg0, (SEL)arg1, arg2);
	Cocoa_NATIVE_EXIT(env, that, objc_1msgSend__IID_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIF
JNIEXPORT jint JNICALL Cocoa_NATIVE(objc_1msgSend__IIF)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloat arg2)
{
	jint rc = 0;
	Cocoa_NATIVE_ENTER(env, that, objc_1msgSend__IIF_FUNC);
	rc = (jint)((jint (*)(id, SEL, jfloat))objc_msgSend)((id)arg0, (SEL)arg1, arg2);
	Cocoa_NATIVE_EXIT(env, that, objc_1msgSend__IIF_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIFF
JNIEXPORT jint JNICALL Cocoa_NATIVE(objc_1msgSend__IIFF)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloat arg2, jfloat arg3)
{
	jint rc = 0;
	Cocoa_NATIVE_ENTER(env, that, objc_1msgSend__IIFF_FUNC);
	rc = (jint)((jint (*)(id, SEL, jfloat, jfloat))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3);
	Cocoa_NATIVE_EXIT(env, that, objc_1msgSend__IIFF_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__III
JNIEXPORT jint JNICALL Cocoa_NATIVE(objc_1msgSend__III)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	Cocoa_NATIVE_ENTER(env, that, objc_1msgSend__III_FUNC);
	rc = (jint)objc_msgSend((id)arg0, (SEL)arg1, arg2);
	Cocoa_NATIVE_EXIT(env, that, objc_1msgSend__III_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIII
JNIEXPORT jint JNICALL Cocoa_NATIVE(objc_1msgSend__IIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	Cocoa_NATIVE_ENTER(env, that, objc_1msgSend__IIII_FUNC);
	rc = (jint)objc_msgSend((id)arg0, (SEL)arg1, arg2, arg3);
	Cocoa_NATIVE_EXIT(env, that, objc_1msgSend__IIII_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIII
JNIEXPORT jint JNICALL Cocoa_NATIVE(objc_1msgSend__IIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	Cocoa_NATIVE_ENTER(env, that, objc_1msgSend__IIIII_FUNC);
	rc = (jint)objc_msgSend((id)arg0, (SEL)arg1, arg2, arg3, arg4);
	Cocoa_NATIVE_EXIT(env, that, objc_1msgSend__IIIII_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIIII
JNIEXPORT jint JNICALL Cocoa_NATIVE(objc_1msgSend__IIIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jint rc = 0;
	Cocoa_NATIVE_ENTER(env, that, objc_1msgSend__IIIIII_FUNC);
	rc = (jint)objc_msgSend((id)arg0, (SEL)arg1, arg2, arg3, arg4, arg5);
	Cocoa_NATIVE_EXIT(env, that, objc_1msgSend__IIIIII_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIIIIII
JNIEXPORT jint JNICALL Cocoa_NATIVE(objc_1msgSend__IIIIIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7)
{
	jint rc = 0;
	Cocoa_NATIVE_ENTER(env, that, objc_1msgSend__IIIIIIII_FUNC);
	rc = (jint)objc_msgSend((id)arg0, (SEL)arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	Cocoa_NATIVE_EXIT(env, that, objc_1msgSend__IIIIIIII_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2
JNIEXPORT jint JNICALL Cocoa_NATIVE(objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	NSPoint _arg3, *lparg3=NULL;
	jint rc = 0;
	Cocoa_NATIVE_ENTER(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	if (arg3) if ((lparg3 = getNSPointFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)objc_msgSend((id)arg0, (SEL)arg1, arg2, *lparg3);
fail:
	if (arg3 && lparg3) setNSPointFields(env, arg3, lparg3);
	Cocoa_NATIVE_EXIT(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2I
JNIEXPORT jint JNICALL Cocoa_NATIVE(objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jint arg4)
{
	NSRect _arg3, *lparg3=NULL;
	jint rc = 0;
	Cocoa_NATIVE_ENTER(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC);
	if (arg3) if ((lparg3 = getNSRectFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)objc_msgSend((id)arg0, (SEL)arg1, arg2, *lparg3, arg4);
fail:
	if (arg3 && lparg3) setNSRectFields(env, arg3, lparg3);
	Cocoa_NATIVE_EXIT(env, that, objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2
JNIEXPORT jint JNICALL Cocoa_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	NSPoint _arg2, *lparg2=NULL;
	jint rc = 0;
	Cocoa_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	if (arg2) if ((lparg2 = getNSPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)objc_msgSend((id)arg0, (SEL)arg1, *lparg2);
fail:
	if (arg2 && lparg2) setNSPointFields(env, arg2, lparg2);
	Cocoa_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2I
JNIEXPORT jint JNICALL Cocoa_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3)
{
	NSPoint _arg2, *lparg2=NULL;
	jint rc = 0;
	Cocoa_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2I_FUNC);
	if (arg2) if ((lparg2 = getNSPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)objc_msgSend((id)arg0, (SEL)arg1, *lparg2, arg3);
fail:
	if (arg2 && lparg2) setNSPointFields(env, arg2, lparg2);
	Cocoa_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2I_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2
JNIEXPORT jint JNICALL Cocoa_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3, jobject arg4)
{
	NSPoint _arg2, *lparg2=NULL;
	NSPoint _arg3, *lparg3=NULL;
	NSPoint _arg4, *lparg4=NULL;
	jint rc = 0;
	Cocoa_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	if (arg2) if ((lparg2 = getNSPointFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSPointFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getNSPointFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)objc_msgSend((id)arg0, (SEL)arg1, *lparg2, *lparg3, *lparg4);
fail:
	if (arg4 && lparg4) setNSPointFields(env, arg4, lparg4);
	if (arg3 && lparg3) setNSPointFields(env, arg3, lparg3);
	if (arg2 && lparg2) setNSPointFields(env, arg2, lparg2);
	Cocoa_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2
JNIEXPORT jint JNICALL Cocoa_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	NSRect _arg2, *lparg2=NULL;
	jint rc = 0;
	Cocoa_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)objc_msgSend((id)arg0, (SEL)arg1, *lparg2);
fail:
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
	Cocoa_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2I
JNIEXPORT jint JNICALL Cocoa_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3)
{
	NSRect _arg2, *lparg2=NULL;
	jint rc = 0;
	Cocoa_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC);
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)objc_msgSend((id)arg0, (SEL)arg1, *lparg2, arg3);
fail:
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
	Cocoa_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2II
JNIEXPORT jint JNICALL Cocoa_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3, jint arg4)
{
	NSRect _arg2, *lparg2=NULL;
	jint rc = 0;
	Cocoa_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2II_FUNC);
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)objc_msgSend((id)arg0, (SEL)arg1, *lparg2, arg3, arg4);
fail:
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
	Cocoa_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2II_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSSize_2
JNIEXPORT jint JNICALL Cocoa_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSSize_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	NSSize _arg2, *lparg2=NULL;
	jint rc = 0;
	Cocoa_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSSize_2_FUNC);
	if (arg2) if ((lparg2 = getNSSizeFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)objc_msgSend((id)arg0, (SEL)arg1, *lparg2);
fail:
	if (arg2 && lparg2) setNSSizeFields(env, arg2, lparg2);
	Cocoa_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSSize_2_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__II_3BI
JNIEXPORT jint JNICALL Cocoa_NATIVE(objc_1msgSend__II_3BI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3)
{
	jbyte *lparg2=NULL;
	jint rc = 0;
	Cocoa_NATIVE_ENTER(env, that, objc_1msgSend__II_3BI_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (*)(jint, jint, jbyte *, jint))objc_msgSend)(arg0, arg1, lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	Cocoa_NATIVE_EXIT(env, that, objc_1msgSend__II_3BI_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__II_3C
JNIEXPORT jint JNICALL Cocoa_NATIVE(objc_1msgSend__II_3C)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2)
{
	jchar *lparg2=NULL;
	jint rc = 0;
	Cocoa_NATIVE_ENTER(env, that, objc_1msgSend__II_3C_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (*)(jint, jint, jchar *))objc_msgSend)(arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	Cocoa_NATIVE_EXIT(env, that, objc_1msgSend__II_3C_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__II_3IIIIIIIIIII
JNIEXPORT jint JNICALL Cocoa_NATIVE(objc_1msgSend__II_3IIIIIIIIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11, jint arg12)
{
	jint *lparg2=NULL;
	jint rc = 0;
	Cocoa_NATIVE_ENTER(env, that, objc_1msgSend__II_3IIIIIIIIIII_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)objc_msgSend((id)arg0, (SEL)arg1, lparg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	Cocoa_NATIVE_EXIT(env, that, objc_1msgSend__II_3IIIIIIIIIII_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend_1fpret
JNIEXPORT jdouble JNICALL Cocoa_NATIVE(objc_1msgSend_1fpret)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jdouble rc = 0;
	Cocoa_NATIVE_ENTER(env, that, objc_1msgSend_1fpret_FUNC);
	rc = (jdouble)((jdouble (*)(jintLong, jintLong))objc_msgSend_fpret)(arg0, arg1);
	Cocoa_NATIVE_EXIT(env, that, objc_1msgSend_1fpret_FUNC);
	return rc;
}
#endif

#if (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2II) && !defined(JNI64)) || (!defined(NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL Cocoa_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#else
JNIEXPORT void JNICALL Cocoa_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2JJ)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
#endif
{
	NSPoint _arg0, *lparg0=NULL;
#ifndef JNI64
	Cocoa_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2II_FUNC);
#else
	Cocoa_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2JJ_FUNC);
#endif
	if (arg0) if ((lparg0 = getNSPointFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (STRUCT_SIZE_LIMIT == 0) {
		((void (*)(NSPoint *, jintLong, jintLong))objc_msgSend_stret)(lparg0, arg1, arg2);
	} else if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		*lparg0 = (*(NSPoint (*)(jintLong, jintLong))objc_msgSend_stret)(arg1, arg2);
	} else {
		*lparg0 = (*(NSPoint (*)(jintLong, jintLong))objc_msgSend)(arg1, arg2);
	}
fail:
	if (arg0 && lparg0) setNSPointFields(env, arg0, lparg0);
#ifndef JNI64
	Cocoa_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2II_FUNC);
#else
	Cocoa_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2JJ_FUNC);
#endif
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2II
JNIEXPORT void JNICALL Cocoa_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NSRect _arg0, *lparg0=NULL;
	Cocoa_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2II_FUNC);
	if (arg0) if ((lparg0 = getNSRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (STRUCT_SIZE_LIMIT == 0) {
		objc_msgSend_stret((void *)lparg0, (void *)arg1, (SEL)arg2);
	} else if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		*lparg0 = (*(NSRect (*)(void *, SEL))objc_msgSend_stret)((void *)arg1, (SEL)arg2);
	} else {
		*lparg0 = (*(NSRect (*)(void *, SEL))objc_msgSend)((void *)arg1, (SEL)arg2);
	}
fail:
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
	Cocoa_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2II_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRect_2I
JNIEXPORT void JNICALL Cocoa_NATIVE(objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRect_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jobject arg3, jint arg4)
{
	NSRect _arg0, *lparg0=NULL;
	NSRect _arg3, *lparg3=NULL;
	Cocoa_NATIVE_ENTER(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC);
	if (arg0) if ((lparg0 = getNSRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSRectFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (STRUCT_SIZE_LIMIT == 0) {
		objc_msgSend_stret((void *)lparg0, (void *)arg1, (SEL)arg2, *lparg3, arg4);
	} else if (sizeof(_arg0) > STRUCT_SIZE_LIMIT) {
		*lparg0 = (*(NSRect (*)(void *, SEL, NSRect, jint))objc_msgSend_stret)((void *)arg1, (SEL)arg2, *lparg3, arg4);
	} else {
		*lparg0 = (*(NSRect (*)(void *, SEL, NSRect, jint))objc_msgSend)((void *)arg1, (SEL)arg2, *lparg3, arg4);
	}
fail:
	if (arg3 && lparg3) setNSRectFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
	Cocoa_NATIVE_EXIT(env, that, objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC);
}
#endif

#ifndef NO_sel_1registerName
JNIEXPORT jint JNICALL Cocoa_NATIVE(sel_1registerName)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	Cocoa_NATIVE_ENTER(env, that, sel_1registerName_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)sel_registerName((const char *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	Cocoa_NATIVE_EXIT(env, that, sel_1registerName_FUNC);
	return rc;
}
#endif

