/*******************************************************************************
* Copyright (c) 2000, 2005 IBM Corporation and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
* 
* Contributors:
*     IBM Corporation - initial API and implementation
*******************************************************************************/

#include "jawt_md.h"

#define SWT_AWT_NATIVE(func) Java_org_eclipse_swt_awt_SWT_1AWT_##func

#ifndef NO_getAWTHandle
JNIEXPORT jint JNICALL SWT_AWT_NATIVE(getAWTHandle)
	(JNIEnv *env, jclass that, jobject canvas)
{
	JAWT awt;
	JAWT_DrawingSurface* ds;
	JAWT_DrawingSurfaceInfo* dsi;
	JAWT_X11DrawingSurfaceInfo* dsi_x11;
	jint result = 0;
	jint lock;

	awt.version = JAWT_VERSION_1_3;
	if (JAWT_GetAWT(env, &awt) != 0) {
		ds = awt.GetDrawingSurface(env, canvas);
		if (ds != NULL) {
			lock = ds->Lock(ds);
		 	if ((lock & JAWT_LOCK_ERROR) == 0) {
			 	dsi = ds->GetDrawingSurfaceInfo(ds);
				dsi_x11 = (JAWT_X11DrawingSurfaceInfo*)dsi->platformInfo;
				result = (jint)dsi_x11->drawable;
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
