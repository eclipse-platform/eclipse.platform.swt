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

#include "cde.h"

#ifndef NO_DtActionArg
void cacheDtActionArgFields(JNIEnv *env, jobject lpObject);
DtActionArg *getDtActionArgFields(JNIEnv *env, jobject lpObject, DtActionArg *lpStruct);
void setDtActionArgFields(JNIEnv *env, jobject lpObject, DtActionArg *lpStruct);
#define DtActionArg_sizeof() sizeof(DtActionArg)
#else
#define cacheDtActionArgFields(a,b)
#define getDtActionArgFields(a,b,c) NULL
#define setDtActionArgFields(a,b,c)
#define DtActionArg_sizeof() 0
#endif

