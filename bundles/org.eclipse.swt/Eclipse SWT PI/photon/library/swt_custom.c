/*******************************************************************************
* Copyright (c) 2000, 2003 IBM Corporation and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Common Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/cpl-v10.html
* 
* Contributors:
*     IBM Corporation - initial API and implementation
*******************************************************************************/

#include "swt.h"
#include "structs.h"

#define OS_NATIVE(func) Java_org_eclipse_swt_internal_photon_OS_##func

#ifdef PR_20268
#include "clip.c"
#include "pt_blit.c"
#include "pt_draw_widget.c"
#define PtBlit PtBlit_
#define PtClippedBlit PtClippedBlit_
#endif

#ifndef NO_PfGenerateFontName
JNIEXPORT jbyteArray JNICALL OS_NATIVE(PfGenerateFontName)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jint arg2, jbyteArray arg3)
{
	jbyte *lparg0=NULL;
	jbyte *lparg3=NULL;
	jbyteArray rc;
	NATIVE_ENTER(env, that, "PfGenerateFontName\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	rc = (jbyteArray)PfGenerateFontName((char const *)lparg0, arg1, arg2, (char *)lparg3);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "PfGenerateFontName\n")
	return rc == NULL ? NULL : arg3;
}
#endif

#ifndef NO_PhGetTile
JNIEXPORT jint JNICALL OS_NATIVE(PhGetTile)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "PhGetTile\n")
	rc = (jint)PhGetTile();
	memset((void *)rc, 0, sizeof(PhTile_t));
	NATIVE_EXIT(env, that, "PhGetTile\n")
	return rc;
}
#endif
