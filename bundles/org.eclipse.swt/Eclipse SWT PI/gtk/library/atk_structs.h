/*******************************************************************************
* Copyright (c) 2000, 2003 IBM Corporation and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Common Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/cpl-v10.html
* 
* Contributors:
*     IBM Corporation - initial API and implementation
*******************************************************************************/

#include "atk.h"

#ifndef NO_AtkActionIface
AtkActionIface *getAtkActionIfaceFields(JNIEnv *env, jobject lpObject, AtkActionIface *lpStruct);
void setAtkActionIfaceFields(JNIEnv *env, jobject lpObject, AtkActionIface *lpStruct);
#else
#define getAtkActionIfaceFields(a,b,c) NULL
#define setAtkActionIfaceFields(a,b,c)
#endif

#ifndef NO_AtkComponentIface
AtkComponentIface *getAtkComponentIfaceFields(JNIEnv *env, jobject lpObject, AtkComponentIface *lpStruct);
void setAtkComponentIfaceFields(JNIEnv *env, jobject lpObject, AtkComponentIface *lpStruct);
#else
#define getAtkComponentIfaceFields(a,b,c) NULL
#define setAtkComponentIfaceFields(a,b,c)
#endif

#ifndef NO_AtkObjectClass
AtkObjectClass *getAtkObjectClassFields(JNIEnv *env, jobject lpObject, AtkObjectClass *lpStruct);
void setAtkObjectClassFields(JNIEnv *env, jobject lpObject, AtkObjectClass *lpStruct);
#else
#define getAtkObjectClassFields(a,b,c) NULL
#define setAtkObjectClassFields(a,b,c)
#endif

#ifndef NO_AtkObjectFactoryClass
AtkObjectFactoryClass *getAtkObjectFactoryClassFields(JNIEnv *env, jobject lpObject, AtkObjectFactoryClass *lpStruct);
void setAtkObjectFactoryClassFields(JNIEnv *env, jobject lpObject, AtkObjectFactoryClass *lpStruct);
#else
#define getAtkObjectFactoryClassFields(a,b,c) NULL
#define setAtkObjectFactoryClassFields(a,b,c)
#endif

#ifndef NO_AtkSelectionIface
AtkSelectionIface *getAtkSelectionIfaceFields(JNIEnv *env, jobject lpObject, AtkSelectionIface *lpStruct);
void setAtkSelectionIfaceFields(JNIEnv *env, jobject lpObject, AtkSelectionIface *lpStruct);
#else
#define getAtkSelectionIfaceFields(a,b,c) NULL
#define setAtkSelectionIfaceFields(a,b,c)
#endif

#ifndef NO_AtkTextIface
AtkTextIface *getAtkTextIfaceFields(JNIEnv *env, jobject lpObject, AtkTextIface *lpStruct);
void setAtkTextIfaceFields(JNIEnv *env, jobject lpObject, AtkTextIface *lpStruct);
#else
#define getAtkTextIfaceFields(a,b,c) NULL
#define setAtkTextIfaceFields(a,b,c)
#endif

#ifndef NO_GInterfaceInfo
GInterfaceInfo *getGInterfaceInfoFields(JNIEnv *env, jobject lpObject, GInterfaceInfo *lpStruct);
void setGInterfaceInfoFields(JNIEnv *env, jobject lpObject, GInterfaceInfo *lpStruct);
#else
#define getGInterfaceInfoFields(a,b,c) NULL
#define setGInterfaceInfoFields(a,b,c)
#endif

#ifndef NO_GObjectClass
GObjectClass *getGObjectClassFields(JNIEnv *env, jobject lpObject, GObjectClass *lpStruct);
void setGObjectClassFields(JNIEnv *env, jobject lpObject, GObjectClass *lpStruct);
#else
#define getGObjectClassFields(a,b,c) NULL
#define setGObjectClassFields(a,b,c)
#endif

#ifndef NO_GTypeInfo
GTypeInfo *getGTypeInfoFields(JNIEnv *env, jobject lpObject, GTypeInfo *lpStruct);
void setGTypeInfoFields(JNIEnv *env, jobject lpObject, GTypeInfo *lpStruct);
#else
#define getGTypeInfoFields(a,b,c) NULL
#define setGTypeInfoFields(a,b,c)
#endif

#ifndef NO_GTypeQuery
GTypeQuery *getGTypeQueryFields(JNIEnv *env, jobject lpObject, GTypeQuery *lpStruct);
void setGTypeQueryFields(JNIEnv *env, jobject lpObject, GTypeQuery *lpStruct);
#else
#define getGTypeQueryFields(a,b,c) NULL
#define setGTypeQueryFields(a,b,c)
#endif

#ifndef NO_GtkAccessible
GtkAccessible *getGtkAccessibleFields(JNIEnv *env, jobject lpObject, GtkAccessible *lpStruct);
void setGtkAccessibleFields(JNIEnv *env, jobject lpObject, GtkAccessible *lpStruct);
#else
#define getGtkAccessibleFields(a,b,c) NULL
#define setGtkAccessibleFields(a,b,c)
#endif

