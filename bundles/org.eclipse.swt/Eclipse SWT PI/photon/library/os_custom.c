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

#include "swt.h"
#include "os_structs.h"
#include "os_stats.h"

#define OS_NATIVE(func) Java_org_eclipse_swt_internal_photon_OS_##func

#ifdef PR_20268
#include "clip.c"
#include "pt_blit.c"
#include "pt_draw_widget.c"
#define PtBlit PtBlit_
#define PtClippedBlit PtClippedBlit_
#endif

#ifndef NO_PhGetTile
JNIEXPORT jint JNICALL OS_NATIVE(PhGetTile)
	(JNIEnv *env, jclass that)
{
	jint rc;
	OS_NATIVE_ENTER(env, that, PhGetTile_FUNC)
	rc = (jint)PhGetTile();
	memset((void *)rc, 0, sizeof(PhTile_t));
	OS_NATIVE_EXIT(env, that, PhGetTile_FUNC)
	return rc;
}
#endif
