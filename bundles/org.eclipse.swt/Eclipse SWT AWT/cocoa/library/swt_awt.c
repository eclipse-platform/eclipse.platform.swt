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

#ifdef __OBJC__
#import <AppKit/NSView.h>
#endif

#define SWT_AWT_NATIVE(func) Java_org_eclipse_swt_awt_SWT_1AWT_##func

/*
 * JAWT version 1.7 does not define the type JAWT_MacOSXDrawingSurfaceInfo.
 */
#ifdef JAWT_VERSION_1_7
// Legacy NSView-based rendering
typedef struct JAWT_MacOSXDrawingSurfaceInfo {
    NSView *cocoaViewRef; // the view is guaranteed to be valid only for the duration of Component.paint method
}
JAWT_MacOSXDrawingSurfaceInfo;
#endif /* #ifdef JAWT_VERSION_1_7 */


#ifndef NO_getAWTHandle
JNIEXPORT jintLong JNICALL SWT_AWT_NATIVE(getAWTHandle)
	(JNIEnv *env, jclass that, jobject canvas)
{
	jintLong result = 0;
	JAWT awt;
	JAWT_DrawingSurface* ds;
	JAWT_DrawingSurfaceInfo* dsi;
	JAWT_MacOSXDrawingSurfaceInfo* dsi_cocoa;
	jint lock;

	awt.version = JAWT_VERSION_1_4 | JAWT_MACOSX_USE_CALAYER;
	if (JAWT_GetAWT(env, &awt) != 0) {
		ds = awt.GetDrawingSurface(env, canvas);
		if (ds != NULL) {
			lock = ds->Lock(ds);
		 	if ((lock & JAWT_LOCK_ERROR) == 0) {
			 	dsi = ds->GetDrawingSurfaceInfo(ds);
				dsi_cocoa = (JAWT_MacOSXDrawingSurfaceInfo*)dsi->platformInfo;
				result = (jintLong)dsi_cocoa->cocoaViewRef;
				ds->FreeDrawingSurfaceInfo(dsi);
				ds->Unlock(ds);
			}
		}
		awt.FreeDrawingSurface(ds);
	}
	return result;
}
#endif

#ifndef NO_initFrame
JNIEXPORT jobject JNICALL Java_org_eclipse_swt_awt_SWT_1AWT_initFrame
	(JNIEnv *env, jclass that, jintLong handle, const char *className)
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

