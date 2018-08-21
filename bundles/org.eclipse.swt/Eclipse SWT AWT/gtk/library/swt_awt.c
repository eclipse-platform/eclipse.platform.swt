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
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

#include "swt.h"
#include "jawt_md.h"

#define SWT_AWT_NATIVE(func) Java_org_eclipse_swt_awt_SWT_1AWT_##func

#ifndef NO_getAWTHandle
JNIEXPORT jintLong JNICALL SWT_AWT_NATIVE(getAWTHandle)
	(JNIEnv *env, jclass that, jobject canvas)
{
	JAWT awt;
	JAWT_DrawingSurface* ds;
	JAWT_DrawingSurfaceInfo* dsi;
	JAWT_X11DrawingSurfaceInfo* dsi_x11;
	jintLong result = 0;
	jint lock;

	awt.version = JAWT_VERSION_1_3;
	if (JAWT_GetAWT(env, &awt) != 0) {
		ds = awt.GetDrawingSurface(env, canvas);
		if (ds != NULL) {
			lock = ds->Lock(ds);
		 	if ((lock & JAWT_LOCK_ERROR) == 0) {
			 	dsi = ds->GetDrawingSurfaceInfo(ds);
				dsi_x11 = (JAWT_X11DrawingSurfaceInfo*)dsi->platformInfo;
				result = (jintLong)dsi_x11->drawable;
				ds->FreeDrawingSurfaceInfo(dsi);
				ds->Unlock(ds);
			}
		}
		awt.FreeDrawingSurface(ds);
	}
	return result;
}
#endif

#ifndef NO_setDebug
JNIEXPORT void JNICALL SWT_AWT_NATIVE(setDebug)
	(JNIEnv *env, jclass that, jobject frame, jboolean debug)
{
	JAWT awt;
	JAWT_DrawingSurface* ds;
	JAWT_DrawingSurfaceInfo* dsi;
	JAWT_X11DrawingSurfaceInfo* dsi_x11;
	jint lock;

	awt.version = JAWT_VERSION_1_3;
	if (JAWT_GetAWT(env, &awt) != 0) {
		ds = awt.GetDrawingSurface(env, frame);
		if (ds != NULL) {
			lock = ds->Lock(ds);
		 	if ((lock & JAWT_LOCK_ERROR) == 0) {
			 	dsi = ds->GetDrawingSurfaceInfo(ds);
				dsi_x11 = (JAWT_X11DrawingSurfaceInfo*)dsi->platformInfo;
				XSynchronize(dsi_x11->display, debug);
				ds->FreeDrawingSurfaceInfo(dsi);
				ds->Unlock(ds);
			}
		}
		awt.FreeDrawingSurface(ds);
	}
}
#endif

#ifndef NO_initFrame
JNIEXPORT jobject JNICALL Java_org_eclipse_swt_awt_SWT_1AWT_initFrame
	(JNIEnv *env, jclass that, jintLong handle)
{
	jobject object;
	jmethodID constructor;
	
	jclass cls = (*env)->FindClass(env, "sun/awt/X11/XEmbeddedFrame");
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
    jclass cls = (*env)->FindClass(env, "sun/awt/X11/XEmbeddedFrame");
    if (NULL == cls) return;
    jmethodID midInit = (*env)->GetMethodID(env, cls, "validateWithBounds", "(IIII)V");
    (*env)->CallVoidMethod(env, frame, midInit, x,y,w,h);
}
#endif

#ifndef NO_synthesizeWindowActivation
JNIEXPORT void JNICALL Java_org_eclipse_swt_awt_SWT_1AWT_synthesizeWindowActivation
(JNIEnv *env, jclass that, jobject frame, jboolean doActivate)
{
    jclass cls = (*env)->FindClass(env, "sun/awt/X11/XEmbeddedFrame");
    if (NULL == cls) return;
    jmethodID midInit = (*env)->GetMethodID(env, cls, "synthesizeWindowActivation", "(Z)V");
    (*env)->CallVoidMethod(env, frame, midInit, doActivate);
}
#endif

#ifndef NO_registerListeners
JNIEXPORT void JNICALL Java_org_eclipse_swt_awt_SWT_1AWT_registerListeners
(JNIEnv *env, jclass that, jobject frame)
{
    jclass cls = (*env)->FindClass(env, "sun/awt/X11/XEmbeddedFrame");
    if (NULL == cls) return;
    jmethodID midInit = (*env)->GetMethodID(env, cls, "registerListeners", "()V");
    (*env)->CallVoidMethod(env, frame, midInit);
}
#endif



