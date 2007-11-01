/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
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

#ifndef NO_objc_class
typedef struct objc_class_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID isa, super_class, name, version, info, instance_size, ivars, methodLists, cache, protocols;
} objc_class_FID_CACHE;

objc_class_FID_CACHE objc_classFc;

void cacheobjc_classFields(JNIEnv *env, jobject lpObject)
{
	if (objc_classFc.cached) return;
	objc_classFc.clazz = (*env)->GetObjectClass(env, lpObject);
	objc_classFc.isa = (*env)->GetFieldID(env, objc_classFc.clazz, "isa", "I");
	objc_classFc.super_class = (*env)->GetFieldID(env, objc_classFc.clazz, "super_class", "I");
	objc_classFc.name = (*env)->GetFieldID(env, objc_classFc.clazz, "name", "I");
	objc_classFc.version = (*env)->GetFieldID(env, objc_classFc.clazz, "version", "I");
	objc_classFc.info = (*env)->GetFieldID(env, objc_classFc.clazz, "info", "I");
	objc_classFc.instance_size = (*env)->GetFieldID(env, objc_classFc.clazz, "instance_size", "I");
	objc_classFc.ivars = (*env)->GetFieldID(env, objc_classFc.clazz, "ivars", "I");
	objc_classFc.methodLists = (*env)->GetFieldID(env, objc_classFc.clazz, "methodLists", "I");
	objc_classFc.cache = (*env)->GetFieldID(env, objc_classFc.clazz, "cache", "I");
	objc_classFc.protocols = (*env)->GetFieldID(env, objc_classFc.clazz, "protocols", "I");
	objc_classFc.cached = 1;
}

struct objc_class *getobjc_classFields(JNIEnv *env, jobject lpObject, struct objc_class *lpStruct)
{
	if (!objc_classFc.cached) cacheobjc_classFields(env, lpObject);
	lpStruct->isa = (*env)->GetIntField(env, lpObject, objc_classFc.isa);
	lpStruct->super_class = (*env)->GetIntField(env, lpObject, objc_classFc.super_class);
	lpStruct->name = (*env)->GetIntField(env, lpObject, objc_classFc.name);
	lpStruct->version = (*env)->GetIntField(env, lpObject, objc_classFc.version);
	lpStruct->info = (*env)->GetIntField(env, lpObject, objc_classFc.info);
	lpStruct->instance_size = (*env)->GetIntField(env, lpObject, objc_classFc.instance_size);
	lpStruct->ivars = (*env)->GetIntField(env, lpObject, objc_classFc.ivars);
	lpStruct->methodLists = (*env)->GetIntField(env, lpObject, objc_classFc.methodLists);
	lpStruct->cache = (*env)->GetIntField(env, lpObject, objc_classFc.cache);
	lpStruct->protocols = (*env)->GetIntField(env, lpObject, objc_classFc.protocols);
	return lpStruct;
}

void setobjc_classFields(JNIEnv *env, jobject lpObject, struct objc_class *lpStruct)
{
	if (!objc_classFc.cached) cacheobjc_classFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, objc_classFc.isa, (jint)lpStruct->isa);
	(*env)->SetIntField(env, lpObject, objc_classFc.super_class, (jint)lpStruct->super_class);
	(*env)->SetIntField(env, lpObject, objc_classFc.name, (jint)lpStruct->name);
	(*env)->SetIntField(env, lpObject, objc_classFc.version, (jint)lpStruct->version);
	(*env)->SetIntField(env, lpObject, objc_classFc.info, (jint)lpStruct->info);
	(*env)->SetIntField(env, lpObject, objc_classFc.instance_size, (jint)lpStruct->instance_size);
	(*env)->SetIntField(env, lpObject, objc_classFc.ivars, (jint)lpStruct->ivars);
	(*env)->SetIntField(env, lpObject, objc_classFc.methodLists, (jint)lpStruct->methodLists);
	(*env)->SetIntField(env, lpObject, objc_classFc.cache, (jint)lpStruct->cache);
	(*env)->SetIntField(env, lpObject, objc_classFc.protocols, (jint)lpStruct->protocols);
}
#endif

#ifndef NO_objc_method
typedef struct objc_method_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID method_name, method_types, method_imp;
} objc_method_FID_CACHE;

objc_method_FID_CACHE objc_methodFc;

void cacheobjc_methodFields(JNIEnv *env, jobject lpObject)
{
	if (objc_methodFc.cached) return;
	objc_methodFc.clazz = (*env)->GetObjectClass(env, lpObject);
	objc_methodFc.method_name = (*env)->GetFieldID(env, objc_methodFc.clazz, "method_name", "I");
	objc_methodFc.method_types = (*env)->GetFieldID(env, objc_methodFc.clazz, "method_types", "I");
	objc_methodFc.method_imp = (*env)->GetFieldID(env, objc_methodFc.clazz, "method_imp", "I");
	objc_methodFc.cached = 1;
}

struct objc_method *getobjc_methodFields(JNIEnv *env, jobject lpObject, struct objc_method *lpStruct)
{
	if (!objc_methodFc.cached) cacheobjc_methodFields(env, lpObject);
	lpStruct->method_name = (*env)->GetIntField(env, lpObject, objc_methodFc.method_name);
	lpStruct->method_types = (*env)->GetIntField(env, lpObject, objc_methodFc.method_types);
	lpStruct->method_imp = (*env)->GetIntField(env, lpObject, objc_methodFc.method_imp);
	return lpStruct;
}

void setobjc_methodFields(JNIEnv *env, jobject lpObject, struct objc_method *lpStruct)
{
	if (!objc_methodFc.cached) cacheobjc_methodFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, objc_methodFc.method_name, (jint)lpStruct->method_name);
	(*env)->SetIntField(env, lpObject, objc_methodFc.method_types, (jint)lpStruct->method_types);
	(*env)->SetIntField(env, lpObject, objc_methodFc.method_imp, (jint)lpStruct->method_imp);
}
#endif

#ifndef NO_objc_method_list
typedef struct objc_method_list_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID obsolete, method_count;
} objc_method_list_FID_CACHE;

objc_method_list_FID_CACHE objc_method_listFc;

void cacheobjc_method_listFields(JNIEnv *env, jobject lpObject)
{
	if (objc_method_listFc.cached) return;
	objc_method_listFc.clazz = (*env)->GetObjectClass(env, lpObject);
	objc_method_listFc.obsolete = (*env)->GetFieldID(env, objc_method_listFc.clazz, "obsolete", "I");
	objc_method_listFc.method_count = (*env)->GetFieldID(env, objc_method_listFc.clazz, "method_count", "I");
	objc_method_listFc.cached = 1;
}

struct objc_method_list *getobjc_method_listFields(JNIEnv *env, jobject lpObject, struct objc_method_list *lpStruct)
{
	if (!objc_method_listFc.cached) cacheobjc_method_listFields(env, lpObject);
	lpStruct->obsolete = (*env)->GetIntField(env, lpObject, objc_method_listFc.obsolete);
	lpStruct->method_count = (*env)->GetIntField(env, lpObject, objc_method_listFc.method_count);
	return lpStruct;
}

void setobjc_method_listFields(JNIEnv *env, jobject lpObject, struct objc_method_list *lpStruct)
{
	if (!objc_method_listFc.cached) cacheobjc_method_listFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, objc_method_listFc.obsolete, (jint)lpStruct->obsolete);
	(*env)->SetIntField(env, lpObject, objc_method_listFc.method_count, (jint)lpStruct->method_count);
}
#endif

