/*******************************************************************************
* Copyright (c) 2000, 2005 IBM Corporation and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*     IBM Corporation - initial API and implementation
*******************************************************************************/

#include "gdip.h"

#ifndef NO_GdiplusStartupInput
void cacheGdiplusStartupInputFields(JNIEnv *env, jobject lpObject);
GdiplusStartupInput *getGdiplusStartupInputFields(JNIEnv *env, jobject lpObject, GdiplusStartupInput *lpStruct);
void setGdiplusStartupInputFields(JNIEnv *env, jobject lpObject, GdiplusStartupInput *lpStruct);
#define GdiplusStartupInput_sizeof() sizeof(GdiplusStartupInput)
#else
#define cacheGdiplusStartupInputFields(a,b)
#define getGdiplusStartupInputFields(a,b,c) NULL
#define setGdiplusStartupInputFields(a,b,c)
#define GdiplusStartupInput_sizeof() 0
#endif

#ifndef NO_PointF
void cachePointFFields(JNIEnv *env, jobject lpObject);
PointF *getPointFFields(JNIEnv *env, jobject lpObject, PointF *lpStruct);
void setPointFFields(JNIEnv *env, jobject lpObject, PointF *lpStruct);
#define PointF_sizeof() sizeof(PointF)
#else
#define cachePointFFields(a,b)
#define getPointFFields(a,b,c) NULL
#define setPointFFields(a,b,c)
#define PointF_sizeof() 0
#endif

#ifndef NO_Rect
void cacheRectFields(JNIEnv *env, jobject lpObject);
Rect *getRectFields(JNIEnv *env, jobject lpObject, Rect *lpStruct);
void setRectFields(JNIEnv *env, jobject lpObject, Rect *lpStruct);
#define Rect_sizeof() sizeof(Rect)
#else
#define cacheRectFields(a,b)
#define getRectFields(a,b,c) NULL
#define setRectFields(a,b,c)
#define Rect_sizeof() 0
#endif

#ifndef NO_RectF
void cacheRectFFields(JNIEnv *env, jobject lpObject);
RectF *getRectFFields(JNIEnv *env, jobject lpObject, RectF *lpStruct);
void setRectFFields(JNIEnv *env, jobject lpObject, RectF *lpStruct);
#define RectF_sizeof() sizeof(RectF)
#else
#define cacheRectFFields(a,b)
#define getRectFFields(a,b,c) NULL
#define setRectFFields(a,b,c)
#define RectF_sizeof() 0
#endif

