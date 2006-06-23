/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Mozilla Communicator client code, released March 31, 1998.
 *
 * The Initial Developer of the Original Code is
 * Netscape Communications Corporation.
 * Portions created by Netscape are Copyright (C) 1998-1999
 * Netscape Communications Corporation.  All Rights Reserved.
 *
 * Contributor(s):
 *
 * IBM
 * -  Binding to permit interfacing between Mozilla and SWT
 * -  Copyright (C) 2004, 2006 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */

#include "swt.h"
#include "xpcom_profile_structs.h"
#include "xpcom_profile_stats.h"

extern "C" {

#define XPCOM_PROFILE_NATIVE(func) Java_org_eclipse_swt_internal_mozilla_XPCOM_1PROFILE_##func

#ifndef NO_NS_1NewProfileDirServiceProvider
JNIEXPORT jint JNICALL XPCOM_PROFILE_NATIVE(NS_1NewProfileDirServiceProvider)
	(JNIEnv *env, jclass that, jboolean arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	XPCOM_PROFILE_NATIVE_ENTER(env, that, NS_1NewProfileDirServiceProvider_FUNC);
	if (arg1) if ((lparg1 = env->GetIntArrayElements(arg1, NULL)) == NULL) goto fail;
	rc = (jint)NS_NewProfileDirServiceProvider(arg0, (nsProfileDirServiceProvider**)lparg1);
fail:
	if (arg1 && lparg1) env->ReleaseIntArrayElements(arg1, lparg1, 0);
	XPCOM_PROFILE_NATIVE_EXIT(env, that, NS_1NewProfileDirServiceProvider_FUNC);
	return rc;
}
#endif

#ifndef NO_ProfileDirServiceProvider_1Register
JNIEXPORT jint JNICALL XPCOM_PROFILE_NATIVE(ProfileDirServiceProvider_1Register)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	XPCOM_PROFILE_NATIVE_ENTER(env, that, ProfileDirServiceProvider_1Register_FUNC);
	rc = (jint)((nsProfileDirServiceProvider *)arg0)->Register();
	XPCOM_PROFILE_NATIVE_EXIT(env, that, ProfileDirServiceProvider_1Register_FUNC);
	return rc;
}
#endif

#ifndef NO_ProfileDirServiceProvider_1SetProfileDir
JNIEXPORT jint JNICALL XPCOM_PROFILE_NATIVE(ProfileDirServiceProvider_1SetProfileDir)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	XPCOM_PROFILE_NATIVE_ENTER(env, that, ProfileDirServiceProvider_1SetProfileDir_FUNC);
	rc = (jint)((nsProfileDirServiceProvider *)arg0)->SetProfileDir((nsIFile *)arg1);
	XPCOM_PROFILE_NATIVE_EXIT(env, that, ProfileDirServiceProvider_1SetProfileDir_FUNC);
	return rc;
}
#endif

#ifndef NO_ProfileDirServiceProvider_1Shutdown
JNIEXPORT jint JNICALL XPCOM_PROFILE_NATIVE(ProfileDirServiceProvider_1Shutdown)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	XPCOM_PROFILE_NATIVE_ENTER(env, that, ProfileDirServiceProvider_1Shutdown_FUNC);
	rc = (jint)((nsProfileDirServiceProvider *)arg0)->Shutdown();
	XPCOM_PROFILE_NATIVE_EXIT(env, that, ProfileDirServiceProvider_1Shutdown_FUNC);
	return rc;
}
#endif

}
