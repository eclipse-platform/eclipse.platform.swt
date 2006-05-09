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
#include "xpcom_structs.h"

#ifndef NO_nsID
typedef struct nsID_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID m0, m1, m2, m3;
} nsID_FID_CACHE;

nsID_FID_CACHE nsIDFc;

void cachensIDFields(JNIEnv *env, jobject lpObject)
{
	if (nsIDFc.cached) return;
	nsIDFc.clazz = env->GetObjectClass(lpObject);
	nsIDFc.m0 = env->GetFieldID(nsIDFc.clazz, "m0", "I");
	nsIDFc.m1 = env->GetFieldID(nsIDFc.clazz, "m1", "S");
	nsIDFc.m2 = env->GetFieldID(nsIDFc.clazz, "m2", "S");
	nsIDFc.m3 = env->GetFieldID(nsIDFc.clazz, "m3", "[B");
	nsIDFc.cached = 1;
}

nsID *getnsIDFields(JNIEnv *env, jobject lpObject, nsID *lpStruct)
{
	if (!nsIDFc.cached) cachensIDFields(env, lpObject);
	lpStruct->m0 = env->GetIntField(lpObject, nsIDFc.m0);
	lpStruct->m1 = env->GetShortField(lpObject, nsIDFc.m1);
	lpStruct->m2 = env->GetShortField(lpObject, nsIDFc.m2);
	{
	jbyteArray lpObject1 = (jbyteArray)env->GetObjectField(lpObject, nsIDFc.m3);
	env->GetByteArrayRegion(lpObject1, 0, sizeof(lpStruct->m3), (jbyte *)lpStruct->m3);
	}
	return lpStruct;
}

void setnsIDFields(JNIEnv *env, jobject lpObject, nsID *lpStruct)
{
	if (!nsIDFc.cached) cachensIDFields(env, lpObject);
	env->SetIntField(lpObject, nsIDFc.m0, (jint)lpStruct->m0);
	env->SetShortField(lpObject, nsIDFc.m1, (jshort)lpStruct->m1);
	env->SetShortField(lpObject, nsIDFc.m2, (jshort)lpStruct->m2);
	{
	jbyteArray lpObject1 = (jbyteArray)env->GetObjectField(lpObject, nsIDFc.m3);
	env->SetByteArrayRegion(lpObject1, 0, sizeof(lpStruct->m3), (jbyte *)lpStruct->m3);
	}
}
#endif

