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

#include "wgl.h"

#ifndef NO_LAYERPLANEDESCRIPTOR
void cacheLAYERPLANEDESCRIPTORFields(JNIEnv *env, jobject lpObject);
LAYERPLANEDESCRIPTOR *getLAYERPLANEDESCRIPTORFields(JNIEnv *env, jobject lpObject, LAYERPLANEDESCRIPTOR *lpStruct);
void setLAYERPLANEDESCRIPTORFields(JNIEnv *env, jobject lpObject, LAYERPLANEDESCRIPTOR *lpStruct);
#define LAYERPLANEDESCRIPTOR_sizeof() sizeof(LAYERPLANEDESCRIPTOR)
#else
#define cacheLAYERPLANEDESCRIPTORFields(a,b)
#define getLAYERPLANEDESCRIPTORFields(a,b,c) NULL
#define setLAYERPLANEDESCRIPTORFields(a,b,c)
#define LAYERPLANEDESCRIPTOR_sizeof() 0
#endif

#ifndef NO_PIXELFORMATDESCRIPTOR
void cachePIXELFORMATDESCRIPTORFields(JNIEnv *env, jobject lpObject);
PIXELFORMATDESCRIPTOR *getPIXELFORMATDESCRIPTORFields(JNIEnv *env, jobject lpObject, PIXELFORMATDESCRIPTOR *lpStruct);
void setPIXELFORMATDESCRIPTORFields(JNIEnv *env, jobject lpObject, PIXELFORMATDESCRIPTOR *lpStruct);
#define PIXELFORMATDESCRIPTOR_sizeof() sizeof(PIXELFORMATDESCRIPTOR)
#else
#define cachePIXELFORMATDESCRIPTORFields(a,b)
#define getPIXELFORMATDESCRIPTORFields(a,b,c) NULL
#define setPIXELFORMATDESCRIPTORFields(a,b,c)
#define PIXELFORMATDESCRIPTOR_sizeof() 0
#endif

