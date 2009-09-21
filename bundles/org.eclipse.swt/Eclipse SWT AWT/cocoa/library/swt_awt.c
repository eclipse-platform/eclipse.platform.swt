/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * Copyright (c) 2009 Adobe Systems Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API
 *    Adobe Systems, Inc. - initial implementation
 *******************************************************************************/

#include "swt.h"
#include "jawt_md.h"

#define SWT_AWT_NATIVE(func) Java_org_eclipse_swt_awt_SWT_1AWT_##func

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

	awt.version = JAWT_VERSION_1_4;
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
