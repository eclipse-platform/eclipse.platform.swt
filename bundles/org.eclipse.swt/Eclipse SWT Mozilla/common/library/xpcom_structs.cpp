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

