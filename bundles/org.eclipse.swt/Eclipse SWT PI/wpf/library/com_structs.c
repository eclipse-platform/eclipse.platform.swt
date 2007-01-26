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
#include "com_structs.h"

#ifndef NO_POINT
typedef struct POINT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID x, y;
} POINT_FID_CACHE;

POINT_FID_CACHE POINTFc;

void cachePOINTFields(JNIEnv *env, jobject lpObject)
{
	if (POINTFc.cached) return;
	POINTFc.clazz = (*env)->GetObjectClass(env, lpObject);
	POINTFc.x = (*env)->GetFieldID(env, POINTFc.clazz, "x", "I");
	POINTFc.y = (*env)->GetFieldID(env, POINTFc.clazz, "y", "I");
	POINTFc.cached = 1;
}

POINT *getPOINTFields(JNIEnv *env, jobject lpObject, POINT *lpStruct)
{
	if (!POINTFc.cached) cachePOINTFields(env, lpObject);
	lpStruct->x = (*env)->GetIntField(env, lpObject, POINTFc.x);
	lpStruct->y = (*env)->GetIntField(env, lpObject, POINTFc.y);
	return lpStruct;
}

void setPOINTFields(JNIEnv *env, jobject lpObject, POINT *lpStruct)
{
	if (!POINTFc.cached) cachePOINTFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, POINTFc.x, (jint)lpStruct->x);
	(*env)->SetIntField(env, lpObject, POINTFc.y, (jint)lpStruct->y);
}
#endif

