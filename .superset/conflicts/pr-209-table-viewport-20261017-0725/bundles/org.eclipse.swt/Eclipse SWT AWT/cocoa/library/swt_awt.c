/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API
 *    Adobe Systems, Inc. - initial implementation
 *******************************************************************************/

#include "swt.h"
#include "jawt_md.h"

#define SWT_AWT_NATIVE(func) Java_org_eclipse_swt_awt_SWT_1AWT_##func

#ifndef NO_initFrame
JNIEXPORT jobject JNICALL Java_org_eclipse_swt_awt_SWT_1AWT_initFrame
	(JNIEnv *env, jclass that, jlong handle, const char *className)
{
	jobject object;
	jmethodID constructor;
	
	jclass cls = (*env)->FindClass(env, "sun/lwawt/macosx/CViewEmbeddedFrame");
	if (NULL == cls) return NULL;
	constructor = (*env)->GetMethodID(env, cls, "<init>", "(J)V");
	object = (*env)->NewObject(env, cls, constructor, handle);
	return object;
}
#endif

#ifndef NO_validateWithBounds
JNIEXPORT void JNICALL Java_org_eclipse_swt_awt_SWT_1AWT_validateWithBounds
(JNIEnv *env, jclass that, jobject frame, jint x,jint y,jint w,jint h)
{
    jclass cls = (*env)->FindClass(env, "sun/lwawt/macosx/CViewEmbeddedFrame");
    if (NULL == cls) return;
    jmethodID midInit = (*env)->GetMethodID(env, cls, "validateWithBounds", "(IIII)V");
    (*env)->CallVoidMethod(env, frame, midInit, x,y,w,h);
}
#endif

#ifndef NO_synthesizeWindowActivation
JNIEXPORT void JNICALL Java_org_eclipse_swt_awt_SWT_1AWT_synthesizeWindowActivation
(JNIEnv *env, jclass that, jobject frame, jboolean doActivate)
{
    jclass cls = (*env)->FindClass(env, "sun/lwawt/macosx/CViewEmbeddedFrame");
    if (NULL == cls) return;
    jmethodID midInit = (*env)->GetMethodID(env, cls, "synthesizeWindowActivation", "(Z)V");
    (*env)->CallVoidMethod(env, frame, midInit, doActivate);
}
#endif

