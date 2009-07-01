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

#include "win32.h"

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

#ifndef NO_PROCESS_INFORMATION
void cachePROCESS_INFORMATIONFields(JNIEnv *env, jobject lpObject);
PROCESS_INFORMATION *getPROCESS_INFORMATIONFields(JNIEnv *env, jobject lpObject, PROCESS_INFORMATION *lpStruct);
void setPROCESS_INFORMATIONFields(JNIEnv *env, jobject lpObject, PROCESS_INFORMATION *lpStruct);
#define PROCESS_INFORMATION_sizeof() sizeof(PROCESS_INFORMATION)
#else
#define cachePROCESS_INFORMATIONFields(a,b)
#define getPROCESS_INFORMATIONFields(a,b,c) NULL
#define setPROCESS_INFORMATIONFields(a,b,c)
#define PROCESS_INFORMATION_sizeof() 0
#endif

#ifndef NO_SHELLEXECUTEINFOW
void cacheSHELLEXECUTEINFOWFields(JNIEnv *env, jobject lpObject);
SHELLEXECUTEINFOW *getSHELLEXECUTEINFOWFields(JNIEnv *env, jobject lpObject, SHELLEXECUTEINFOW *lpStruct);
void setSHELLEXECUTEINFOWFields(JNIEnv *env, jobject lpObject, SHELLEXECUTEINFOW *lpStruct);
#define SHELLEXECUTEINFOW_sizeof() sizeof(SHELLEXECUTEINFOW)
#else
#define cacheSHELLEXECUTEINFOWFields(a,b)
#define getSHELLEXECUTEINFOWFields(a,b,c) NULL
#define setSHELLEXECUTEINFOWFields(a,b,c)
#define SHELLEXECUTEINFOW_sizeof() 0
#endif

#ifndef NO_STARTUPINFOW
void cacheSTARTUPINFOWFields(JNIEnv *env, jobject lpObject);
STARTUPINFOW *getSTARTUPINFOWFields(JNIEnv *env, jobject lpObject, STARTUPINFOW *lpStruct);
void setSTARTUPINFOWFields(JNIEnv *env, jobject lpObject, STARTUPINFOW *lpStruct);
#define STARTUPINFOW_sizeof() sizeof(STARTUPINFOW)
#else
#define cacheSTARTUPINFOWFields(a,b)
#define getSTARTUPINFOWFields(a,b,c) NULL
#define setSTARTUPINFOWFields(a,b,c)
#define STARTUPINFOW_sizeof() 0
#endif

