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

#include "os.h"

#ifndef NO_ICONINFO
void cacheICONINFOFields(JNIEnv *env, jobject lpObject);
ICONINFO *getICONINFOFields(JNIEnv *env, jobject lpObject, ICONINFO *lpStruct);
void setICONINFOFields(JNIEnv *env, jobject lpObject, ICONINFO *lpStruct);
#define ICONINFO_sizeof() sizeof(ICONINFO)
#else
#define cacheICONINFOFields(a,b)
#define getICONINFOFields(a,b,c) NULL
#define setICONINFOFields(a,b,c)
#define ICONINFO_sizeof() 0
#endif

#ifndef NO_POINT
void cachePOINTFields(JNIEnv *env, jobject lpObject);
POINT *getPOINTFields(JNIEnv *env, jobject lpObject, POINT *lpStruct);
void setPOINTFields(JNIEnv *env, jobject lpObject, POINT *lpStruct);
#define POINT_sizeof() sizeof(POINT)
#else
#define cachePOINTFields(a,b)
#define getPOINTFields(a,b,c) NULL
#define setPOINTFields(a,b,c)
#define POINT_sizeof() 0
#endif

