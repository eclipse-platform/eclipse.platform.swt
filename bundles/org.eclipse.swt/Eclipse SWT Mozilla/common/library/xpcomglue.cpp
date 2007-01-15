/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

#include "swt.h"
#include "xpcomglue_structs.h"
#include "xpcomglue_stats.h"

extern "C" {

#define XPCOMGlue_NATIVE(func) Java_org_eclipse_swt_internal_mozilla_XPCOMGlue_##func

#ifndef NO_XPCOMGlueShutdown
JNIEXPORT jint JNICALL XPCOMGlue_NATIVE(XPCOMGlueShutdown)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	XPCOMGlue_NATIVE_ENTER(env, that, XPCOMGlueShutdown_FUNC);
	rc = (jint)XPCOMGlueShutdown();
	XPCOMGlue_NATIVE_EXIT(env, that, XPCOMGlueShutdown_FUNC);
	return rc;
}
#endif

#ifndef NO_XPCOMGlueStartup
JNIEXPORT jint JNICALL XPCOMGlue_NATIVE(XPCOMGlueStartup)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	XPCOMGlue_NATIVE_ENTER(env, that, XPCOMGlueStartup_FUNC);
	if (arg0) if ((lparg0 = env->GetByteArrayElements(arg0, NULL)) == NULL) goto fail;
	rc = (jint)XPCOMGlueStartup((const char *)lparg0);
fail:
	if (arg0 && lparg0) env->ReleaseByteArrayElements(arg0, lparg0, 0);
	XPCOMGlue_NATIVE_EXIT(env, that, XPCOMGlueStartup_FUNC);
	return rc;
}
#endif

}
