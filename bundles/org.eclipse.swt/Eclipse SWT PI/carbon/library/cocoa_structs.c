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
#include "cocoa_structs.h"

#ifndef NO_NSPoint
typedef struct NSPoint_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID x, y;
} NSPoint_FID_CACHE;

NSPoint_FID_CACHE NSPointFc;

void cacheNSPointFields(JNIEnv *env, jobject lpObject)
{
	if (NSPointFc.cached) return;
	NSPointFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NSPointFc.x = (*env)->GetFieldID(env, NSPointFc.clazz, "x", "F");
	NSPointFc.y = (*env)->GetFieldID(env, NSPointFc.clazz, "y", "F");
	NSPointFc.cached = 1;
}

NSPoint *getNSPointFields(JNIEnv *env, jobject lpObject, NSPoint *lpStruct)
{
	if (!NSPointFc.cached) cacheNSPointFields(env, lpObject);
	lpStruct->x = (*env)->GetFloatField(env, lpObject, NSPointFc.x);
	lpStruct->y = (*env)->GetFloatField(env, lpObject, NSPointFc.y);
	return lpStruct;
}

void setNSPointFields(JNIEnv *env, jobject lpObject, NSPoint *lpStruct)
{
	if (!NSPointFc.cached) cacheNSPointFields(env, lpObject);
	(*env)->SetFloatField(env, lpObject, NSPointFc.x, (jfloat)lpStruct->x);
	(*env)->SetFloatField(env, lpObject, NSPointFc.y, (jfloat)lpStruct->y);
}
#endif

#ifndef NO_NSRect
typedef struct NSRect_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID x, y, width, height;
} NSRect_FID_CACHE;

NSRect_FID_CACHE NSRectFc;

void cacheNSRectFields(JNIEnv *env, jobject lpObject)
{
	if (NSRectFc.cached) return;
	NSRectFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NSRectFc.x = (*env)->GetFieldID(env, NSRectFc.clazz, "x", "F");
	NSRectFc.y = (*env)->GetFieldID(env, NSRectFc.clazz, "y", "F");
	NSRectFc.width = (*env)->GetFieldID(env, NSRectFc.clazz, "width", "F");
	NSRectFc.height = (*env)->GetFieldID(env, NSRectFc.clazz, "height", "F");
	NSRectFc.cached = 1;
}

NSRect *getNSRectFields(JNIEnv *env, jobject lpObject, NSRect *lpStruct)
{
	if (!NSRectFc.cached) cacheNSRectFields(env, lpObject);
	lpStruct->origin.x = (*env)->GetFloatField(env, lpObject, NSRectFc.x);
	lpStruct->origin.y = (*env)->GetFloatField(env, lpObject, NSRectFc.y);
	lpStruct->size.width = (*env)->GetFloatField(env, lpObject, NSRectFc.width);
	lpStruct->size.height = (*env)->GetFloatField(env, lpObject, NSRectFc.height);
	return lpStruct;
}

void setNSRectFields(JNIEnv *env, jobject lpObject, NSRect *lpStruct)
{
	if (!NSRectFc.cached) cacheNSRectFields(env, lpObject);
	(*env)->SetFloatField(env, lpObject, NSRectFc.x, (jfloat)lpStruct->origin.x);
	(*env)->SetFloatField(env, lpObject, NSRectFc.y, (jfloat)lpStruct->origin.y);
	(*env)->SetFloatField(env, lpObject, NSRectFc.width, (jfloat)lpStruct->size.width);
	(*env)->SetFloatField(env, lpObject, NSRectFc.height, (jfloat)lpStruct->size.height);
}
#endif

#ifndef NO_NSSize
typedef struct NSSize_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID width, height;
} NSSize_FID_CACHE;

NSSize_FID_CACHE NSSizeFc;

void cacheNSSizeFields(JNIEnv *env, jobject lpObject)
{
	if (NSSizeFc.cached) return;
	NSSizeFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NSSizeFc.width = (*env)->GetFieldID(env, NSSizeFc.clazz, "width", "F");
	NSSizeFc.height = (*env)->GetFieldID(env, NSSizeFc.clazz, "height", "F");
	NSSizeFc.cached = 1;
}

NSSize *getNSSizeFields(JNIEnv *env, jobject lpObject, NSSize *lpStruct)
{
	if (!NSSizeFc.cached) cacheNSSizeFields(env, lpObject);
	lpStruct->width = (*env)->GetFloatField(env, lpObject, NSSizeFc.width);
	lpStruct->height = (*env)->GetFloatField(env, lpObject, NSSizeFc.height);
	return lpStruct;
}

void setNSSizeFields(JNIEnv *env, jobject lpObject, NSSize *lpStruct)
{
	if (!NSSizeFc.cached) cacheNSSizeFields(env, lpObject);
	(*env)->SetFloatField(env, lpObject, NSSizeFc.width, (jfloat)lpStruct->width);
	(*env)->SetFloatField(env, lpObject, NSSizeFc.height, (jfloat)lpStruct->height);
}
#endif

