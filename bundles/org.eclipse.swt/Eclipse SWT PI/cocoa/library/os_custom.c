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

extern id objc_msgSend(id, SEL, ...);


#ifndef NO_objc_1msgSend__IID
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IID)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jdouble arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IID_FUNC);
	rc = ((jint (*) (id, SEL, double))objc_msgSend)((id)arg0, (SEL)arg1, arg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IID_FUNC);
	return rc;
}
#endif


#ifndef NO_objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2FF
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2FF)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jfloat arg3, jfloat arg4)
{
	NSRect _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2FF_FUNC);
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = ((jint (*) (id, SEL, NSRect, float, float))objc_msgSend)((id)arg0, (SEL)arg1, *lparg2, arg3, arg4);
fail:
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2FF_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIF
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIF)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloat arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIF_FUNC);	
	rc = ((jint (*) (id, SEL, float))objc_msgSend)((id)arg0, (SEL)arg1, arg2);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIF_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIFFFF
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIFFFF)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloat arg2, jfloat arg3, jfloat arg4, jfloat arg5)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIFFFF_FUNC);
	rc = ((jint (*) (id, SEL, float, float, float, float))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3, arg4, arg5);
	//rc = (jint)objc_msgSend((id)arg0, (SEL)arg1, arg2, arg3, arg4, arg5);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIFFFF_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIIF
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIIF)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jfloat arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIIF_FUNC);
	rc = ((jint (*) (id, SEL, int, float))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIIF_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend__IIFF
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend__IIFF)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloat arg2, jfloat arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend__IIFF_FUNC);
	rc = ((jint (*) (id, SEL, float, float))objc_msgSend)((id)arg0, (SEL)arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, objc_1msgSend__IIFF_FUNC);
	return rc;
}
#endif

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
