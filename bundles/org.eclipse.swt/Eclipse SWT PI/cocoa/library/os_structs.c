/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

#include "swt.h"
#include "os_structs.h"

#ifndef NO_CFRange
typedef struct CFRange_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID location, length;
} CFRange_FID_CACHE;

CFRange_FID_CACHE CFRangeFc;

void cacheCFRangeFields(JNIEnv *env, jobject lpObject)
{
	if (CFRangeFc.cached) return;
	CFRangeFc.clazz = (*env)->GetObjectClass(env, lpObject);
	CFRangeFc.location = (*env)->GetFieldID(env, CFRangeFc.clazz, "location", I_J);
	CFRangeFc.length = (*env)->GetFieldID(env, CFRangeFc.clazz, "length", I_J);
	CFRangeFc.cached = 1;
}

CFRange *getCFRangeFields(JNIEnv *env, jobject lpObject, CFRange *lpStruct)
{
	if (!CFRangeFc.cached) cacheCFRangeFields(env, lpObject);
	lpStruct->location = (*env)->GetIntLongField(env, lpObject, CFRangeFc.location);
	lpStruct->length = (*env)->GetIntLongField(env, lpObject, CFRangeFc.length);
	return lpStruct;
}

void setCFRangeFields(JNIEnv *env, jobject lpObject, CFRange *lpStruct)
{
	if (!CFRangeFc.cached) cacheCFRangeFields(env, lpObject);
	(*env)->SetIntLongField(env, lpObject, CFRangeFc.location, (jintLong)lpStruct->location);
	(*env)->SetIntLongField(env, lpObject, CFRangeFc.length, (jintLong)lpStruct->length);
}
#endif

#ifndef NO_CGAffineTransform
typedef struct CGAffineTransform_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID a, b, c, d, tx, ty;
} CGAffineTransform_FID_CACHE;

CGAffineTransform_FID_CACHE CGAffineTransformFc;

void cacheCGAffineTransformFields(JNIEnv *env, jobject lpObject)
{
	if (CGAffineTransformFc.cached) return;
	CGAffineTransformFc.clazz = (*env)->GetObjectClass(env, lpObject);
	CGAffineTransformFc.a = (*env)->GetFieldID(env, CGAffineTransformFc.clazz, "a", F_D);
	CGAffineTransformFc.b = (*env)->GetFieldID(env, CGAffineTransformFc.clazz, "b", F_D);
	CGAffineTransformFc.c = (*env)->GetFieldID(env, CGAffineTransformFc.clazz, "c", F_D);
	CGAffineTransformFc.d = (*env)->GetFieldID(env, CGAffineTransformFc.clazz, "d", F_D);
	CGAffineTransformFc.tx = (*env)->GetFieldID(env, CGAffineTransformFc.clazz, "tx", F_D);
	CGAffineTransformFc.ty = (*env)->GetFieldID(env, CGAffineTransformFc.clazz, "ty", F_D);
	CGAffineTransformFc.cached = 1;
}

CGAffineTransform *getCGAffineTransformFields(JNIEnv *env, jobject lpObject, CGAffineTransform *lpStruct)
{
	if (!CGAffineTransformFc.cached) cacheCGAffineTransformFields(env, lpObject);
	lpStruct->a = (*env)->GetFloatDoubleField(env, lpObject, CGAffineTransformFc.a);
	lpStruct->b = (*env)->GetFloatDoubleField(env, lpObject, CGAffineTransformFc.b);
	lpStruct->c = (*env)->GetFloatDoubleField(env, lpObject, CGAffineTransformFc.c);
	lpStruct->d = (*env)->GetFloatDoubleField(env, lpObject, CGAffineTransformFc.d);
	lpStruct->tx = (*env)->GetFloatDoubleField(env, lpObject, CGAffineTransformFc.tx);
	lpStruct->ty = (*env)->GetFloatDoubleField(env, lpObject, CGAffineTransformFc.ty);
	return lpStruct;
}

void setCGAffineTransformFields(JNIEnv *env, jobject lpObject, CGAffineTransform *lpStruct)
{
	if (!CGAffineTransformFc.cached) cacheCGAffineTransformFields(env, lpObject);
	(*env)->SetFloatDoubleField(env, lpObject, CGAffineTransformFc.a, (jfloatDouble)lpStruct->a);
	(*env)->SetFloatDoubleField(env, lpObject, CGAffineTransformFc.b, (jfloatDouble)lpStruct->b);
	(*env)->SetFloatDoubleField(env, lpObject, CGAffineTransformFc.c, (jfloatDouble)lpStruct->c);
	(*env)->SetFloatDoubleField(env, lpObject, CGAffineTransformFc.d, (jfloatDouble)lpStruct->d);
	(*env)->SetFloatDoubleField(env, lpObject, CGAffineTransformFc.tx, (jfloatDouble)lpStruct->tx);
	(*env)->SetFloatDoubleField(env, lpObject, CGAffineTransformFc.ty, (jfloatDouble)lpStruct->ty);
}
#endif

#ifndef NO_CGPathElement
typedef struct CGPathElement_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID type, points;
} CGPathElement_FID_CACHE;

CGPathElement_FID_CACHE CGPathElementFc;

void cacheCGPathElementFields(JNIEnv *env, jobject lpObject)
{
	if (CGPathElementFc.cached) return;
	CGPathElementFc.clazz = (*env)->GetObjectClass(env, lpObject);
	CGPathElementFc.type = (*env)->GetFieldID(env, CGPathElementFc.clazz, "type", "I");
	CGPathElementFc.points = (*env)->GetFieldID(env, CGPathElementFc.clazz, "points", I_J);
	CGPathElementFc.cached = 1;
}

CGPathElement *getCGPathElementFields(JNIEnv *env, jobject lpObject, CGPathElement *lpStruct)
{
	if (!CGPathElementFc.cached) cacheCGPathElementFields(env, lpObject);
	lpStruct->type = (CGPathElementType)(*env)->GetIntField(env, lpObject, CGPathElementFc.type);
	lpStruct->points = (CGPoint *)(*env)->GetIntLongField(env, lpObject, CGPathElementFc.points);
	return lpStruct;
}

void setCGPathElementFields(JNIEnv *env, jobject lpObject, CGPathElement *lpStruct)
{
	if (!CGPathElementFc.cached) cacheCGPathElementFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, CGPathElementFc.type, (jint)lpStruct->type);
	(*env)->SetIntLongField(env, lpObject, CGPathElementFc.points, (jintLong)lpStruct->points);
}
#endif

#ifndef NO_CGPoint
typedef struct CGPoint_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID x, y;
} CGPoint_FID_CACHE;

CGPoint_FID_CACHE CGPointFc;

void cacheCGPointFields(JNIEnv *env, jobject lpObject)
{
	if (CGPointFc.cached) return;
	CGPointFc.clazz = (*env)->GetObjectClass(env, lpObject);
	CGPointFc.x = (*env)->GetFieldID(env, CGPointFc.clazz, "x", F_D);
	CGPointFc.y = (*env)->GetFieldID(env, CGPointFc.clazz, "y", F_D);
	CGPointFc.cached = 1;
}

CGPoint *getCGPointFields(JNIEnv *env, jobject lpObject, CGPoint *lpStruct)
{
	if (!CGPointFc.cached) cacheCGPointFields(env, lpObject);
	lpStruct->x = (*env)->GetFloatDoubleField(env, lpObject, CGPointFc.x);
	lpStruct->y = (*env)->GetFloatDoubleField(env, lpObject, CGPointFc.y);
	return lpStruct;
}

void setCGPointFields(JNIEnv *env, jobject lpObject, CGPoint *lpStruct)
{
	if (!CGPointFc.cached) cacheCGPointFields(env, lpObject);
	(*env)->SetFloatDoubleField(env, lpObject, CGPointFc.x, (jfloatDouble)lpStruct->x);
	(*env)->SetFloatDoubleField(env, lpObject, CGPointFc.y, (jfloatDouble)lpStruct->y);
}
#endif

#ifndef NO_CGRect
typedef struct CGRect_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID origin, size;
} CGRect_FID_CACHE;

CGRect_FID_CACHE CGRectFc;

void cacheCGRectFields(JNIEnv *env, jobject lpObject)
{
	if (CGRectFc.cached) return;
	CGRectFc.clazz = (*env)->GetObjectClass(env, lpObject);
	CGRectFc.origin = (*env)->GetFieldID(env, CGRectFc.clazz, "origin", "Lorg/eclipse/swt/internal/cocoa/CGPoint;");
	CGRectFc.size = (*env)->GetFieldID(env, CGRectFc.clazz, "size", "Lorg/eclipse/swt/internal/cocoa/CGSize;");
	CGRectFc.cached = 1;
}

CGRect *getCGRectFields(JNIEnv *env, jobject lpObject, CGRect *lpStruct)
{
	if (!CGRectFc.cached) cacheCGRectFields(env, lpObject);
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, CGRectFc.origin);
	if (lpObject1 != NULL) getCGPointFields(env, lpObject1, &lpStruct->origin);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, CGRectFc.size);
	if (lpObject1 != NULL) getCGSizeFields(env, lpObject1, &lpStruct->size);
	}
	return lpStruct;
}

void setCGRectFields(JNIEnv *env, jobject lpObject, CGRect *lpStruct)
{
	if (!CGRectFc.cached) cacheCGRectFields(env, lpObject);
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, CGRectFc.origin);
	if (lpObject1 != NULL) setCGPointFields(env, lpObject1, &lpStruct->origin);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, CGRectFc.size);
	if (lpObject1 != NULL) setCGSizeFields(env, lpObject1, &lpStruct->size);
	}
}
#endif

#ifndef NO_CGSize
typedef struct CGSize_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID width, height;
} CGSize_FID_CACHE;

CGSize_FID_CACHE CGSizeFc;

void cacheCGSizeFields(JNIEnv *env, jobject lpObject)
{
	if (CGSizeFc.cached) return;
	CGSizeFc.clazz = (*env)->GetObjectClass(env, lpObject);
	CGSizeFc.width = (*env)->GetFieldID(env, CGSizeFc.clazz, "width", F_D);
	CGSizeFc.height = (*env)->GetFieldID(env, CGSizeFc.clazz, "height", F_D);
	CGSizeFc.cached = 1;
}

CGSize *getCGSizeFields(JNIEnv *env, jobject lpObject, CGSize *lpStruct)
{
	if (!CGSizeFc.cached) cacheCGSizeFields(env, lpObject);
	lpStruct->width = (*env)->GetFloatDoubleField(env, lpObject, CGSizeFc.width);
	lpStruct->height = (*env)->GetFloatDoubleField(env, lpObject, CGSizeFc.height);
	return lpStruct;
}

void setCGSizeFields(JNIEnv *env, jobject lpObject, CGSize *lpStruct)
{
	if (!CGSizeFc.cached) cacheCGSizeFields(env, lpObject);
	(*env)->SetFloatDoubleField(env, lpObject, CGSizeFc.width, (jfloatDouble)lpStruct->width);
	(*env)->SetFloatDoubleField(env, lpObject, CGSizeFc.height, (jfloatDouble)lpStruct->height);
}
#endif

#ifndef NO_CTParagraphStyleSetting
typedef struct CTParagraphStyleSetting_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID spec, valueSize, value;
} CTParagraphStyleSetting_FID_CACHE;

CTParagraphStyleSetting_FID_CACHE CTParagraphStyleSettingFc;

void cacheCTParagraphStyleSettingFields(JNIEnv *env, jobject lpObject)
{
	if (CTParagraphStyleSettingFc.cached) return;
	CTParagraphStyleSettingFc.clazz = (*env)->GetObjectClass(env, lpObject);
	CTParagraphStyleSettingFc.spec = (*env)->GetFieldID(env, CTParagraphStyleSettingFc.clazz, "spec", "I");
	CTParagraphStyleSettingFc.valueSize = (*env)->GetFieldID(env, CTParagraphStyleSettingFc.clazz, "valueSize", I_J);
	CTParagraphStyleSettingFc.value = (*env)->GetFieldID(env, CTParagraphStyleSettingFc.clazz, "value", I_J);
	CTParagraphStyleSettingFc.cached = 1;
}

CTParagraphStyleSetting *getCTParagraphStyleSettingFields(JNIEnv *env, jobject lpObject, CTParagraphStyleSetting *lpStruct)
{
	if (!CTParagraphStyleSettingFc.cached) cacheCTParagraphStyleSettingFields(env, lpObject);
	lpStruct->spec = (CTParagraphStyleSpecifier)(*env)->GetIntField(env, lpObject, CTParagraphStyleSettingFc.spec);
	lpStruct->valueSize = (*env)->GetIntLongField(env, lpObject, CTParagraphStyleSettingFc.valueSize);
	lpStruct->value = (void *)(*env)->GetIntLongField(env, lpObject, CTParagraphStyleSettingFc.value);
	return lpStruct;
}

void setCTParagraphStyleSettingFields(JNIEnv *env, jobject lpObject, CTParagraphStyleSetting *lpStruct)
{
	if (!CTParagraphStyleSettingFc.cached) cacheCTParagraphStyleSettingFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, CTParagraphStyleSettingFc.spec, (jint)lpStruct->spec);
	(*env)->SetIntLongField(env, lpObject, CTParagraphStyleSettingFc.valueSize, (jintLong)lpStruct->valueSize);
	(*env)->SetIntLongField(env, lpObject, CTParagraphStyleSettingFc.value, (jintLong)lpStruct->value);
}
#endif

#ifndef NO_NSAffineTransformStruct
typedef struct NSAffineTransformStruct_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID m11, m12, m21, m22, tX, tY;
} NSAffineTransformStruct_FID_CACHE;

NSAffineTransformStruct_FID_CACHE NSAffineTransformStructFc;

void cacheNSAffineTransformStructFields(JNIEnv *env, jobject lpObject)
{
	if (NSAffineTransformStructFc.cached) return;
	NSAffineTransformStructFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NSAffineTransformStructFc.m11 = (*env)->GetFieldID(env, NSAffineTransformStructFc.clazz, "m11", F_D);
	NSAffineTransformStructFc.m12 = (*env)->GetFieldID(env, NSAffineTransformStructFc.clazz, "m12", F_D);
	NSAffineTransformStructFc.m21 = (*env)->GetFieldID(env, NSAffineTransformStructFc.clazz, "m21", F_D);
	NSAffineTransformStructFc.m22 = (*env)->GetFieldID(env, NSAffineTransformStructFc.clazz, "m22", F_D);
	NSAffineTransformStructFc.tX = (*env)->GetFieldID(env, NSAffineTransformStructFc.clazz, "tX", F_D);
	NSAffineTransformStructFc.tY = (*env)->GetFieldID(env, NSAffineTransformStructFc.clazz, "tY", F_D);
	NSAffineTransformStructFc.cached = 1;
}

NSAffineTransformStruct *getNSAffineTransformStructFields(JNIEnv *env, jobject lpObject, NSAffineTransformStruct *lpStruct)
{
	if (!NSAffineTransformStructFc.cached) cacheNSAffineTransformStructFields(env, lpObject);
	lpStruct->m11 = (*env)->GetFloatDoubleField(env, lpObject, NSAffineTransformStructFc.m11);
	lpStruct->m12 = (*env)->GetFloatDoubleField(env, lpObject, NSAffineTransformStructFc.m12);
	lpStruct->m21 = (*env)->GetFloatDoubleField(env, lpObject, NSAffineTransformStructFc.m21);
	lpStruct->m22 = (*env)->GetFloatDoubleField(env, lpObject, NSAffineTransformStructFc.m22);
	lpStruct->tX = (*env)->GetFloatDoubleField(env, lpObject, NSAffineTransformStructFc.tX);
	lpStruct->tY = (*env)->GetFloatDoubleField(env, lpObject, NSAffineTransformStructFc.tY);
	return lpStruct;
}

void setNSAffineTransformStructFields(JNIEnv *env, jobject lpObject, NSAffineTransformStruct *lpStruct)
{
	if (!NSAffineTransformStructFc.cached) cacheNSAffineTransformStructFields(env, lpObject);
	(*env)->SetFloatDoubleField(env, lpObject, NSAffineTransformStructFc.m11, (jfloatDouble)lpStruct->m11);
	(*env)->SetFloatDoubleField(env, lpObject, NSAffineTransformStructFc.m12, (jfloatDouble)lpStruct->m12);
	(*env)->SetFloatDoubleField(env, lpObject, NSAffineTransformStructFc.m21, (jfloatDouble)lpStruct->m21);
	(*env)->SetFloatDoubleField(env, lpObject, NSAffineTransformStructFc.m22, (jfloatDouble)lpStruct->m22);
	(*env)->SetFloatDoubleField(env, lpObject, NSAffineTransformStructFc.tX, (jfloatDouble)lpStruct->tX);
	(*env)->SetFloatDoubleField(env, lpObject, NSAffineTransformStructFc.tY, (jfloatDouble)lpStruct->tY);
}
#endif

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
	NSPointFc.x = (*env)->GetFieldID(env, NSPointFc.clazz, "x", F_D);
	NSPointFc.y = (*env)->GetFieldID(env, NSPointFc.clazz, "y", F_D);
	NSPointFc.cached = 1;
}

NSPoint *getNSPointFields(JNIEnv *env, jobject lpObject, NSPoint *lpStruct)
{
	if (!NSPointFc.cached) cacheNSPointFields(env, lpObject);
	lpStruct->x = (*env)->GetFloatDoubleField(env, lpObject, NSPointFc.x);
	lpStruct->y = (*env)->GetFloatDoubleField(env, lpObject, NSPointFc.y);
	return lpStruct;
}

void setNSPointFields(JNIEnv *env, jobject lpObject, NSPoint *lpStruct)
{
	if (!NSPointFc.cached) cacheNSPointFields(env, lpObject);
	(*env)->SetFloatDoubleField(env, lpObject, NSPointFc.x, (jfloatDouble)lpStruct->x);
	(*env)->SetFloatDoubleField(env, lpObject, NSPointFc.y, (jfloatDouble)lpStruct->y);
}
#endif

#ifndef NO_NSRange
typedef struct NSRange_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID location, length;
} NSRange_FID_CACHE;

NSRange_FID_CACHE NSRangeFc;

void cacheNSRangeFields(JNIEnv *env, jobject lpObject)
{
	if (NSRangeFc.cached) return;
	NSRangeFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NSRangeFc.location = (*env)->GetFieldID(env, NSRangeFc.clazz, "location", I_J);
	NSRangeFc.length = (*env)->GetFieldID(env, NSRangeFc.clazz, "length", I_J);
	NSRangeFc.cached = 1;
}

NSRange *getNSRangeFields(JNIEnv *env, jobject lpObject, NSRange *lpStruct)
{
	if (!NSRangeFc.cached) cacheNSRangeFields(env, lpObject);
	lpStruct->location = (*env)->GetIntLongField(env, lpObject, NSRangeFc.location);
	lpStruct->length = (*env)->GetIntLongField(env, lpObject, NSRangeFc.length);
	return lpStruct;
}

void setNSRangeFields(JNIEnv *env, jobject lpObject, NSRange *lpStruct)
{
	if (!NSRangeFc.cached) cacheNSRangeFields(env, lpObject);
	(*env)->SetIntLongField(env, lpObject, NSRangeFc.location, (jintLong)lpStruct->location);
	(*env)->SetIntLongField(env, lpObject, NSRangeFc.length, (jintLong)lpStruct->length);
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
	NSRectFc.x = (*env)->GetFieldID(env, NSRectFc.clazz, "x", F_D);
	NSRectFc.y = (*env)->GetFieldID(env, NSRectFc.clazz, "y", F_D);
	NSRectFc.width = (*env)->GetFieldID(env, NSRectFc.clazz, "width", F_D);
	NSRectFc.height = (*env)->GetFieldID(env, NSRectFc.clazz, "height", F_D);
	NSRectFc.cached = 1;
}

NSRect *getNSRectFields(JNIEnv *env, jobject lpObject, NSRect *lpStruct)
{
	if (!NSRectFc.cached) cacheNSRectFields(env, lpObject);
	lpStruct->origin.x = (*env)->GetFloatDoubleField(env, lpObject, NSRectFc.x);
	lpStruct->origin.y = (*env)->GetFloatDoubleField(env, lpObject, NSRectFc.y);
	lpStruct->size.width = (*env)->GetFloatDoubleField(env, lpObject, NSRectFc.width);
	lpStruct->size.height = (*env)->GetFloatDoubleField(env, lpObject, NSRectFc.height);
	return lpStruct;
}

void setNSRectFields(JNIEnv *env, jobject lpObject, NSRect *lpStruct)
{
	if (!NSRectFc.cached) cacheNSRectFields(env, lpObject);
	(*env)->SetFloatDoubleField(env, lpObject, NSRectFc.x, (jfloatDouble)lpStruct->origin.x);
	(*env)->SetFloatDoubleField(env, lpObject, NSRectFc.y, (jfloatDouble)lpStruct->origin.y);
	(*env)->SetFloatDoubleField(env, lpObject, NSRectFc.width, (jfloatDouble)lpStruct->size.width);
	(*env)->SetFloatDoubleField(env, lpObject, NSRectFc.height, (jfloatDouble)lpStruct->size.height);
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
	NSSizeFc.width = (*env)->GetFieldID(env, NSSizeFc.clazz, "width", F_D);
	NSSizeFc.height = (*env)->GetFieldID(env, NSSizeFc.clazz, "height", F_D);
	NSSizeFc.cached = 1;
}

NSSize *getNSSizeFields(JNIEnv *env, jobject lpObject, NSSize *lpStruct)
{
	if (!NSSizeFc.cached) cacheNSSizeFields(env, lpObject);
	lpStruct->width = (*env)->GetFloatDoubleField(env, lpObject, NSSizeFc.width);
	lpStruct->height = (*env)->GetFloatDoubleField(env, lpObject, NSSizeFc.height);
	return lpStruct;
}

void setNSSizeFields(JNIEnv *env, jobject lpObject, NSSize *lpStruct)
{
	if (!NSSizeFc.cached) cacheNSSizeFields(env, lpObject);
	(*env)->SetFloatDoubleField(env, lpObject, NSSizeFc.width, (jfloatDouble)lpStruct->width);
	(*env)->SetFloatDoubleField(env, lpObject, NSSizeFc.height, (jfloatDouble)lpStruct->height);
}
#endif

#ifndef NO_objc_super
typedef struct objc_super_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID receiver, super_class;
} objc_super_FID_CACHE;

objc_super_FID_CACHE objc_superFc;

void cacheobjc_superFields(JNIEnv *env, jobject lpObject)
{
	if (objc_superFc.cached) return;
	objc_superFc.clazz = (*env)->GetObjectClass(env, lpObject);
	objc_superFc.receiver = (*env)->GetFieldID(env, objc_superFc.clazz, "receiver", I_J);
	objc_superFc.super_class = (*env)->GetFieldID(env, objc_superFc.clazz, "super_class", I_J);
	objc_superFc.cached = 1;
}

struct objc_super *getobjc_superFields(JNIEnv *env, jobject lpObject, struct objc_super *lpStruct)
{
	if (!objc_superFc.cached) cacheobjc_superFields(env, lpObject);
	lpStruct->receiver = (id)(*env)->GetIntLongField(env, lpObject, objc_superFc.receiver);
	lpStruct->swt_super_class = (Class)(*env)->GetIntLongField(env, lpObject, objc_superFc.super_class);
	return lpStruct;
}

void setobjc_superFields(JNIEnv *env, jobject lpObject, struct objc_super *lpStruct)
{
	if (!objc_superFc.cached) cacheobjc_superFields(env, lpObject);
	(*env)->SetIntLongField(env, lpObject, objc_superFc.receiver, (jintLong)lpStruct->receiver);
	(*env)->SetIntLongField(env, lpObject, objc_superFc.super_class, (jintLong)lpStruct->swt_super_class);
}
#endif

