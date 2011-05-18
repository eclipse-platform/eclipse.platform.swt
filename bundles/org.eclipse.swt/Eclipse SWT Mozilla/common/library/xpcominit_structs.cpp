/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

#include "swt.h"
#include "xpcominit_structs.h"

#ifndef NO_GREProperty
typedef struct GREProperty_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID property, value;
} GREProperty_FID_CACHE;

GREProperty_FID_CACHE GREPropertyFc;

void cacheGREPropertyFields(JNIEnv *env, jobject lpObject)
{
	if (GREPropertyFc.cached) return;
	GREPropertyFc.clazz = env->GetObjectClass(lpObject);
	GREPropertyFc.property = env->GetFieldID(GREPropertyFc.clazz, "property", I_J);
	GREPropertyFc.value = env->GetFieldID(GREPropertyFc.clazz, "value", I_J);
	GREPropertyFc.cached = 1;
}

GREProperty *getGREPropertyFields(JNIEnv *env, jobject lpObject, GREProperty *lpStruct)
{
	if (!GREPropertyFc.cached) cacheGREPropertyFields(env, lpObject);
	lpStruct->property = (const char *)env->GetIntLongField(lpObject, GREPropertyFc.property);
	lpStruct->value = (const char *)env->GetIntLongField(lpObject, GREPropertyFc.value);
	return lpStruct;
}

void setGREPropertyFields(JNIEnv *env, jobject lpObject, GREProperty *lpStruct)
{
	if (!GREPropertyFc.cached) cacheGREPropertyFields(env, lpObject);
	env->SetIntLongField(lpObject, GREPropertyFc.property, (jintLong)lpStruct->property);
	env->SetIntLongField(lpObject, GREPropertyFc.value, (jintLong)lpStruct->value);
}
#endif

#ifndef NO_GREVersionRange
typedef struct GREVersionRange_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lower, lowerInclusive, upper, upperInclusive;
} GREVersionRange_FID_CACHE;

GREVersionRange_FID_CACHE GREVersionRangeFc;

void cacheGREVersionRangeFields(JNIEnv *env, jobject lpObject)
{
	if (GREVersionRangeFc.cached) return;
	GREVersionRangeFc.clazz = env->GetObjectClass(lpObject);
	GREVersionRangeFc.lower = env->GetFieldID(GREVersionRangeFc.clazz, "lower", I_J);
	GREVersionRangeFc.lowerInclusive = env->GetFieldID(GREVersionRangeFc.clazz, "lowerInclusive", "Z");
	GREVersionRangeFc.upper = env->GetFieldID(GREVersionRangeFc.clazz, "upper", I_J);
	GREVersionRangeFc.upperInclusive = env->GetFieldID(GREVersionRangeFc.clazz, "upperInclusive", "Z");
	GREVersionRangeFc.cached = 1;
}

GREVersionRange *getGREVersionRangeFields(JNIEnv *env, jobject lpObject, GREVersionRange *lpStruct)
{
	if (!GREVersionRangeFc.cached) cacheGREVersionRangeFields(env, lpObject);
	lpStruct->lower = (const char *)env->GetIntLongField(lpObject, GREVersionRangeFc.lower);
	lpStruct->lowerInclusive = env->GetBooleanField(lpObject, GREVersionRangeFc.lowerInclusive);
	lpStruct->upper = (const char *)env->GetIntLongField(lpObject, GREVersionRangeFc.upper);
	lpStruct->upperInclusive = env->GetBooleanField(lpObject, GREVersionRangeFc.upperInclusive);
	return lpStruct;
}

void setGREVersionRangeFields(JNIEnv *env, jobject lpObject, GREVersionRange *lpStruct)
{
	if (!GREVersionRangeFc.cached) cacheGREVersionRangeFields(env, lpObject);
	env->SetIntLongField(lpObject, GREVersionRangeFc.lower, (jintLong)lpStruct->lower);
	env->SetBooleanField(lpObject, GREVersionRangeFc.lowerInclusive, (jboolean)lpStruct->lowerInclusive);
	env->SetIntLongField(lpObject, GREVersionRangeFc.upper, (jintLong)lpStruct->upper);
	env->SetBooleanField(lpObject, GREVersionRangeFc.upperInclusive, (jboolean)lpStruct->upperInclusive);
}
#endif

