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
#include "os_structs.h"

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
	CGPointFc.x = (*env)->GetFieldID(env, CGPointFc.clazz, "x", "F");
	CGPointFc.y = (*env)->GetFieldID(env, CGPointFc.clazz, "y", "F");
	CGPointFc.cached = 1;
}

CGPoint *getCGPointFields(JNIEnv *env, jobject lpObject, CGPoint *lpStruct)
{
	if (!CGPointFc.cached) cacheCGPointFields(env, lpObject);
	lpStruct->x = (*env)->GetFloatField(env, lpObject, CGPointFc.x);
	lpStruct->y = (*env)->GetFloatField(env, lpObject, CGPointFc.y);
	return lpStruct;
}

void setCGPointFields(JNIEnv *env, jobject lpObject, CGPoint *lpStruct)
{
	if (!CGPointFc.cached) cacheCGPointFields(env, lpObject);
	(*env)->SetFloatField(env, lpObject, CGPointFc.x, (jfloat)lpStruct->x);
	(*env)->SetFloatField(env, lpObject, CGPointFc.y, (jfloat)lpStruct->y);
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
	CGSizeFc.width = (*env)->GetFieldID(env, CGSizeFc.clazz, "width", "F");
	CGSizeFc.height = (*env)->GetFieldID(env, CGSizeFc.clazz, "height", "F");
	CGSizeFc.cached = 1;
}

CGSize *getCGSizeFields(JNIEnv *env, jobject lpObject, CGSize *lpStruct)
{
	if (!CGSizeFc.cached) cacheCGSizeFields(env, lpObject);
	lpStruct->width = (*env)->GetFloatField(env, lpObject, CGSizeFc.width);
	lpStruct->height = (*env)->GetFloatField(env, lpObject, CGSizeFc.height);
	return lpStruct;
}

void setCGSizeFields(JNIEnv *env, jobject lpObject, CGSize *lpStruct)
{
	if (!CGSizeFc.cached) cacheCGSizeFields(env, lpObject);
	(*env)->SetFloatField(env, lpObject, CGSizeFc.width, (jfloat)lpStruct->width);
	(*env)->SetFloatField(env, lpObject, CGSizeFc.height, (jfloat)lpStruct->height);
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
	NSAffineTransformStructFc.m11 = (*env)->GetFieldID(env, NSAffineTransformStructFc.clazz, "m11", "F");
	NSAffineTransformStructFc.m12 = (*env)->GetFieldID(env, NSAffineTransformStructFc.clazz, "m12", "F");
	NSAffineTransformStructFc.m21 = (*env)->GetFieldID(env, NSAffineTransformStructFc.clazz, "m21", "F");
	NSAffineTransformStructFc.m22 = (*env)->GetFieldID(env, NSAffineTransformStructFc.clazz, "m22", "F");
	NSAffineTransformStructFc.tX = (*env)->GetFieldID(env, NSAffineTransformStructFc.clazz, "tX", "F");
	NSAffineTransformStructFc.tY = (*env)->GetFieldID(env, NSAffineTransformStructFc.clazz, "tY", "F");
	NSAffineTransformStructFc.cached = 1;
}

NSAffineTransformStruct *getNSAffineTransformStructFields(JNIEnv *env, jobject lpObject, NSAffineTransformStruct *lpStruct)
{
	if (!NSAffineTransformStructFc.cached) cacheNSAffineTransformStructFields(env, lpObject);
	lpStruct->m11 = (*env)->GetFloatField(env, lpObject, NSAffineTransformStructFc.m11);
	lpStruct->m12 = (*env)->GetFloatField(env, lpObject, NSAffineTransformStructFc.m12);
	lpStruct->m21 = (*env)->GetFloatField(env, lpObject, NSAffineTransformStructFc.m21);
	lpStruct->m22 = (*env)->GetFloatField(env, lpObject, NSAffineTransformStructFc.m22);
	lpStruct->tX = (*env)->GetFloatField(env, lpObject, NSAffineTransformStructFc.tX);
	lpStruct->tY = (*env)->GetFloatField(env, lpObject, NSAffineTransformStructFc.tY);
	return lpStruct;
}

void setNSAffineTransformStructFields(JNIEnv *env, jobject lpObject, NSAffineTransformStruct *lpStruct)
{
	if (!NSAffineTransformStructFc.cached) cacheNSAffineTransformStructFields(env, lpObject);
	(*env)->SetFloatField(env, lpObject, NSAffineTransformStructFc.m11, (jfloat)lpStruct->m11);
	(*env)->SetFloatField(env, lpObject, NSAffineTransformStructFc.m12, (jfloat)lpStruct->m12);
	(*env)->SetFloatField(env, lpObject, NSAffineTransformStructFc.m21, (jfloat)lpStruct->m21);
	(*env)->SetFloatField(env, lpObject, NSAffineTransformStructFc.m22, (jfloat)lpStruct->m22);
	(*env)->SetFloatField(env, lpObject, NSAffineTransformStructFc.tX, (jfloat)lpStruct->tX);
	(*env)->SetFloatField(env, lpObject, NSAffineTransformStructFc.tY, (jfloat)lpStruct->tY);
}
#endif

#ifndef NO_NSDecimal
typedef struct NSDecimal_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID _exponent, _length, _isNegative, _isCompact, _reserved, _mantissa;
} NSDecimal_FID_CACHE;

NSDecimal_FID_CACHE NSDecimalFc;

void cacheNSDecimalFields(JNIEnv *env, jobject lpObject)
{
	if (NSDecimalFc.cached) return;
	NSDecimalFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NSDecimalFc._exponent = (*env)->GetFieldID(env, NSDecimalFc.clazz, "_exponent", "I");
	NSDecimalFc._length = (*env)->GetFieldID(env, NSDecimalFc.clazz, "_length", "I");
	NSDecimalFc._isNegative = (*env)->GetFieldID(env, NSDecimalFc.clazz, "_isNegative", "I");
	NSDecimalFc._isCompact = (*env)->GetFieldID(env, NSDecimalFc.clazz, "_isCompact", "I");
	NSDecimalFc._reserved = (*env)->GetFieldID(env, NSDecimalFc.clazz, "_reserved", "I");
	NSDecimalFc._mantissa = (*env)->GetFieldID(env, NSDecimalFc.clazz, "_mantissa", "[S");
	NSDecimalFc.cached = 1;
}

NSDecimal *getNSDecimalFields(JNIEnv *env, jobject lpObject, NSDecimal *lpStruct)
{
	if (!NSDecimalFc.cached) cacheNSDecimalFields(env, lpObject);
	lpStruct->_exponent = (*env)->GetIntField(env, lpObject, NSDecimalFc._exponent);
	lpStruct->_length = (*env)->GetIntField(env, lpObject, NSDecimalFc._length);
	lpStruct->_isNegative = (*env)->GetIntField(env, lpObject, NSDecimalFc._isNegative);
	lpStruct->_isCompact = (*env)->GetIntField(env, lpObject, NSDecimalFc._isCompact);
	lpStruct->_reserved = (*env)->GetIntField(env, lpObject, NSDecimalFc._reserved);
	{
	jshortArray lpObject1 = (jshortArray)(*env)->GetObjectField(env, lpObject, NSDecimalFc._mantissa);
	(*env)->GetShortArrayRegion(env, lpObject1, 0, sizeof(lpStruct->_mantissa) / 2, (jshort *)lpStruct->_mantissa);
	}
	return lpStruct;
}

void setNSDecimalFields(JNIEnv *env, jobject lpObject, NSDecimal *lpStruct)
{
	if (!NSDecimalFc.cached) cacheNSDecimalFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, NSDecimalFc._exponent, (jint)lpStruct->_exponent);
	(*env)->SetIntField(env, lpObject, NSDecimalFc._length, (jint)lpStruct->_length);
	(*env)->SetIntField(env, lpObject, NSDecimalFc._isNegative, (jint)lpStruct->_isNegative);
	(*env)->SetIntField(env, lpObject, NSDecimalFc._isCompact, (jint)lpStruct->_isCompact);
	(*env)->SetIntField(env, lpObject, NSDecimalFc._reserved, (jint)lpStruct->_reserved);
	{
	jshortArray lpObject1 = (jshortArray)(*env)->GetObjectField(env, lpObject, NSDecimalFc._mantissa);
	(*env)->SetShortArrayRegion(env, lpObject1, 0, sizeof(lpStruct->_mantissa) / 2, (jshort *)lpStruct->_mantissa);
	}
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
	NSRangeFc.location = (*env)->GetFieldID(env, NSRangeFc.clazz, "location", "I");
	NSRangeFc.length = (*env)->GetFieldID(env, NSRangeFc.clazz, "length", "I");
	NSRangeFc.cached = 1;
}

NSRange *getNSRangeFields(JNIEnv *env, jobject lpObject, NSRange *lpStruct)
{
	if (!NSRangeFc.cached) cacheNSRangeFields(env, lpObject);
	lpStruct->location = (*env)->GetIntField(env, lpObject, NSRangeFc.location);
	lpStruct->length = (*env)->GetIntField(env, lpObject, NSRangeFc.length);
	return lpStruct;
}

void setNSRangeFields(JNIEnv *env, jobject lpObject, NSRange *lpStruct)
{
	if (!NSRangeFc.cached) cacheNSRangeFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, NSRangeFc.location, (jint)lpStruct->location);
	(*env)->SetIntField(env, lpObject, NSRangeFc.length, (jint)lpStruct->length);
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

#ifndef NO_NSSwappedDouble
typedef struct NSSwappedDouble_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID v;
} NSSwappedDouble_FID_CACHE;

NSSwappedDouble_FID_CACHE NSSwappedDoubleFc;

void cacheNSSwappedDoubleFields(JNIEnv *env, jobject lpObject)
{
	if (NSSwappedDoubleFc.cached) return;
	NSSwappedDoubleFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NSSwappedDoubleFc.v = (*env)->GetFieldID(env, NSSwappedDoubleFc.clazz, "v", "J");
	NSSwappedDoubleFc.cached = 1;
}

NSSwappedDouble *getNSSwappedDoubleFields(JNIEnv *env, jobject lpObject, NSSwappedDouble *lpStruct)
{
	if (!NSSwappedDoubleFc.cached) cacheNSSwappedDoubleFields(env, lpObject);
	lpStruct->v = (*env)->GetLongField(env, lpObject, NSSwappedDoubleFc.v);
	return lpStruct;
}

void setNSSwappedDoubleFields(JNIEnv *env, jobject lpObject, NSSwappedDouble *lpStruct)
{
	if (!NSSwappedDoubleFc.cached) cacheNSSwappedDoubleFields(env, lpObject);
	(*env)->SetLongField(env, lpObject, NSSwappedDoubleFc.v, (jlong)lpStruct->v);
}
#endif

#ifndef NO_NSSwappedFloat
typedef struct NSSwappedFloat_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID v;
} NSSwappedFloat_FID_CACHE;

NSSwappedFloat_FID_CACHE NSSwappedFloatFc;

void cacheNSSwappedFloatFields(JNIEnv *env, jobject lpObject)
{
	if (NSSwappedFloatFc.cached) return;
	NSSwappedFloatFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NSSwappedFloatFc.v = (*env)->GetFieldID(env, NSSwappedFloatFc.clazz, "v", "I");
	NSSwappedFloatFc.cached = 1;
}

NSSwappedFloat *getNSSwappedFloatFields(JNIEnv *env, jobject lpObject, NSSwappedFloat *lpStruct)
{
	if (!NSSwappedFloatFc.cached) cacheNSSwappedFloatFields(env, lpObject);
	lpStruct->v = (*env)->GetIntField(env, lpObject, NSSwappedFloatFc.v);
	return lpStruct;
}

void setNSSwappedFloatFields(JNIEnv *env, jobject lpObject, NSSwappedFloat *lpStruct)
{
	if (!NSSwappedFloatFc.cached) cacheNSSwappedFloatFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, NSSwappedFloatFc.v, (jint)lpStruct->v);
}
#endif

#ifndef NO_objc_super
typedef struct objc_super_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID receiver, cls;
} objc_super_FID_CACHE;

objc_super_FID_CACHE objc_superFc;

void cacheobjc_superFields(JNIEnv *env, jobject lpObject)
{
	if (objc_superFc.cached) return;
	objc_superFc.clazz = (*env)->GetObjectClass(env, lpObject);
	objc_superFc.receiver = (*env)->GetFieldID(env, objc_superFc.clazz, "receiver", "I");
	objc_superFc.cls = (*env)->GetFieldID(env, objc_superFc.clazz, "cls", "I");
	objc_superFc.cached = 1;
}

struct objc_super *getobjc_superFields(JNIEnv *env, jobject lpObject, struct objc_super *lpStruct)
{
	if (!objc_superFc.cached) cacheobjc_superFields(env, lpObject);
	lpStruct->receiver = (id)(*env)->GetIntField(env, lpObject, objc_superFc.receiver);
	lpStruct->class = (Class)(*env)->GetIntField(env, lpObject, objc_superFc.cls);
	return lpStruct;
}

void setobjc_superFields(JNIEnv *env, jobject lpObject, struct objc_super *lpStruct)
{
	if (!objc_superFc.cached) cacheobjc_superFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, objc_superFc.receiver, (jint)lpStruct->receiver);
	(*env)->SetIntField(env, lpObject, objc_superFc.cls, (jint)lpStruct->class);
}
#endif

