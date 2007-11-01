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

#include "os.h"

#ifndef NO_NSPoint
void cacheNSPointFields(JNIEnv *env, jobject lpObject);
NSPoint *getNSPointFields(JNIEnv *env, jobject lpObject, NSPoint *lpStruct);
void setNSPointFields(JNIEnv *env, jobject lpObject, NSPoint *lpStruct);
#define NSPoint_sizeof() sizeof(NSPoint)
#else
#define cacheNSPointFields(a,b)
#define getNSPointFields(a,b,c) NULL
#define setNSPointFields(a,b,c)
#define NSPoint_sizeof() 0
#endif

#ifndef NO_NSRect
void cacheNSRectFields(JNIEnv *env, jobject lpObject);
NSRect *getNSRectFields(JNIEnv *env, jobject lpObject, NSRect *lpStruct);
void setNSRectFields(JNIEnv *env, jobject lpObject, NSRect *lpStruct);
#define NSRect_sizeof() sizeof(NSRect)
#else
#define cacheNSRectFields(a,b)
#define getNSRectFields(a,b,c) NULL
#define setNSRectFields(a,b,c)
#define NSRect_sizeof() 0
#endif

#ifndef NO_NSSize
void cacheNSSizeFields(JNIEnv *env, jobject lpObject);
NSSize *getNSSizeFields(JNIEnv *env, jobject lpObject, NSSize *lpStruct);
void setNSSizeFields(JNIEnv *env, jobject lpObject, NSSize *lpStruct);
#define NSSize_sizeof() sizeof(NSSize)
#else
#define cacheNSSizeFields(a,b)
#define getNSSizeFields(a,b,c) NULL
#define setNSSizeFields(a,b,c)
#define NSSize_sizeof() 0
#endif

#ifndef NO_objc_class
void cacheobjc_classFields(JNIEnv *env, jobject lpObject);
struct objc_class *getobjc_classFields(JNIEnv *env, jobject lpObject, struct objc_class *lpStruct);
void setobjc_classFields(JNIEnv *env, jobject lpObject, struct objc_class *lpStruct);
#define objc_class_sizeof() sizeof(objc_class)
#else
#define cacheobjc_classFields(a,b)
#define getobjc_classFields(a,b,c) NULL
#define setobjc_classFields(a,b,c)
#define objc_class_sizeof() 0
#endif

#ifndef NO_objc_method
void cacheobjc_methodFields(JNIEnv *env, jobject lpObject);
struct objc_method *getobjc_methodFields(JNIEnv *env, jobject lpObject, struct objc_method *lpStruct);
void setobjc_methodFields(JNIEnv *env, jobject lpObject, struct objc_method *lpStruct);
#define objc_method_sizeof() sizeof(objc_method)
#else
#define cacheobjc_methodFields(a,b)
#define getobjc_methodFields(a,b,c) NULL
#define setobjc_methodFields(a,b,c)
#define objc_method_sizeof() 0
#endif

#ifndef NO_objc_method_list
void cacheobjc_method_listFields(JNIEnv *env, jobject lpObject);
struct objc_method_list *getobjc_method_listFields(JNIEnv *env, jobject lpObject, struct objc_method_list *lpStruct);
void setobjc_method_listFields(JNIEnv *env, jobject lpObject, struct objc_method_list *lpStruct);
#define objc_method_list_sizeof() sizeof(objc_method_list)
#else
#define cacheobjc_method_listFields(a,b)
#define getobjc_method_listFields(a,b,c) NULL
#define setobjc_method_listFields(a,b,c)
#define objc_method_list_sizeof() 0
#endif

