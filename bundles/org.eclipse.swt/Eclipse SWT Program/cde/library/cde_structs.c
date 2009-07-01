/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

#include "swt.h"
#include "cde_structs.h"

#ifndef NO_DtActionArg
typedef struct DtActionArg_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID argClass, name;
} DtActionArg_FID_CACHE;

DtActionArg_FID_CACHE DtActionArgFc;

void cacheDtActionArgFields(JNIEnv *env, jobject lpObject)
{
	if (DtActionArgFc.cached) return;
	DtActionArgFc.clazz = (*env)->GetObjectClass(env, lpObject);
	DtActionArgFc.argClass = (*env)->GetFieldID(env, DtActionArgFc.clazz, "argClass", "I");
	DtActionArgFc.name = (*env)->GetFieldID(env, DtActionArgFc.clazz, "name", I_J);
	DtActionArgFc.cached = 1;
}

DtActionArg *getDtActionArgFields(JNIEnv *env, jobject lpObject, DtActionArg *lpStruct)
{
	if (!DtActionArgFc.cached) cacheDtActionArgFields(env, lpObject);
	lpStruct->argClass = (*env)->GetIntField(env, lpObject, DtActionArgFc.argClass);
	lpStruct->u.file.name = (char *)(*env)->GetIntLongField(env, lpObject, DtActionArgFc.name);
	return lpStruct;
}

void setDtActionArgFields(JNIEnv *env, jobject lpObject, DtActionArg *lpStruct)
{
	if (!DtActionArgFc.cached) cacheDtActionArgFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, DtActionArgFc.argClass, (jint)lpStruct->argClass);
	(*env)->SetIntLongField(env, lpObject, DtActionArgFc.name, (jintLong)lpStruct->u.file.name);
}
#endif

