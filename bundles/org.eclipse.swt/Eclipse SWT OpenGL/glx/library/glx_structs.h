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

#include "glx.h"

#ifndef NO_XVisualInfo
void cacheXVisualInfoFields(JNIEnv *env, jobject lpObject);
XVisualInfo *getXVisualInfoFields(JNIEnv *env, jobject lpObject, XVisualInfo *lpStruct);
void setXVisualInfoFields(JNIEnv *env, jobject lpObject, XVisualInfo *lpStruct);
#define XVisualInfo_sizeof() sizeof(XVisualInfo)
#else
#define cacheXVisualInfoFields(a,b)
#define getXVisualInfoFields(a,b,c) NULL
#define setXVisualInfoFields(a,b,c)
#define XVisualInfo_sizeof() 0
#endif

