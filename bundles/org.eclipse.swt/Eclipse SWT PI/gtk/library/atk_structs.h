/*******************************************************************************
* Copyright (c) 2000, 2003 IBM Corporation and others. All rights reserved.
* The contents of this file are made available under the terms
* of the GNU Lesser General Public License (LGPL) Version 2.1 that
* accompanies this distribution (lgpl-v21.txt).  The LGPL is also
* available at http://www.gnu.org/licenses/lgpl.html.  If the version
* of the LGPL at http://www.gnu.org is different to the version of
* the LGPL accompanying this distribution and there is any conflict
* between the two license versions, the terms of the LGPL accompanying
* this distribution shall govern.
* 
* Contributors:
*     IBM Corporation - initial API and implementation
*******************************************************************************/

#include "atk.h"

#ifndef NO_AtkActionIface
AtkActionIface *getAtkActionIfaceFields(JNIEnv *env, jobject lpObject, AtkActionIface *lpStruct);
void setAtkActionIfaceFields(JNIEnv *env, jobject lpObject, AtkActionIface *lpStruct);
#define AtkActionIface_sizeof() sizeof(AtkActionIface)
#else
#define getAtkActionIfaceFields(a,b,c) NULL
#define setAtkActionIfaceFields(a,b,c)
#define AtkActionIface_sizeof() 0
#endif

#ifndef NO_AtkComponentIface
AtkComponentIface *getAtkComponentIfaceFields(JNIEnv *env, jobject lpObject, AtkComponentIface *lpStruct);
void setAtkComponentIfaceFields(JNIEnv *env, jobject lpObject, AtkComponentIface *lpStruct);
#define AtkComponentIface_sizeof() sizeof(AtkComponentIface)
#else
#define getAtkComponentIfaceFields(a,b,c) NULL
#define setAtkComponentIfaceFields(a,b,c)
#define AtkComponentIface_sizeof() 0
#endif

#ifndef NO_AtkHypertextIface
AtkHypertextIface *getAtkHypertextIfaceFields(JNIEnv *env, jobject lpObject, AtkHypertextIface *lpStruct);
void setAtkHypertextIfaceFields(JNIEnv *env, jobject lpObject, AtkHypertextIface *lpStruct);
#define AtkHypertextIface_sizeof() sizeof(AtkHypertextIface)
#else
#define getAtkHypertextIfaceFields(a,b,c) NULL
#define setAtkHypertextIfaceFields(a,b,c)
#define AtkHypertextIface_sizeof() 0
#endif

#ifndef NO_AtkObjectClass
AtkObjectClass *getAtkObjectClassFields(JNIEnv *env, jobject lpObject, AtkObjectClass *lpStruct);
void setAtkObjectClassFields(JNIEnv *env, jobject lpObject, AtkObjectClass *lpStruct);
#define AtkObjectClass_sizeof() sizeof(AtkObjectClass)
#else
#define getAtkObjectClassFields(a,b,c) NULL
#define setAtkObjectClassFields(a,b,c)
#define AtkObjectClass_sizeof() 0
#endif

#ifndef NO_AtkObjectFactoryClass
AtkObjectFactoryClass *getAtkObjectFactoryClassFields(JNIEnv *env, jobject lpObject, AtkObjectFactoryClass *lpStruct);
void setAtkObjectFactoryClassFields(JNIEnv *env, jobject lpObject, AtkObjectFactoryClass *lpStruct);
#define AtkObjectFactoryClass_sizeof() sizeof(AtkObjectFactoryClass)
#else
#define getAtkObjectFactoryClassFields(a,b,c) NULL
#define setAtkObjectFactoryClassFields(a,b,c)
#define AtkObjectFactoryClass_sizeof() 0
#endif

#ifndef NO_AtkSelectionIface
AtkSelectionIface *getAtkSelectionIfaceFields(JNIEnv *env, jobject lpObject, AtkSelectionIface *lpStruct);
void setAtkSelectionIfaceFields(JNIEnv *env, jobject lpObject, AtkSelectionIface *lpStruct);
#define AtkSelectionIface_sizeof() sizeof(AtkSelectionIface)
#else
#define getAtkSelectionIfaceFields(a,b,c) NULL
#define setAtkSelectionIfaceFields(a,b,c)
#define AtkSelectionIface_sizeof() 0
#endif

#ifndef NO_AtkTextIface
AtkTextIface *getAtkTextIfaceFields(JNIEnv *env, jobject lpObject, AtkTextIface *lpStruct);
void setAtkTextIfaceFields(JNIEnv *env, jobject lpObject, AtkTextIface *lpStruct);
#define AtkTextIface_sizeof() sizeof(AtkTextIface)
#else
#define getAtkTextIfaceFields(a,b,c) NULL
#define setAtkTextIfaceFields(a,b,c)
#define AtkTextIface_sizeof() 0
#endif

#ifndef NO_GInterfaceInfo
GInterfaceInfo *getGInterfaceInfoFields(JNIEnv *env, jobject lpObject, GInterfaceInfo *lpStruct);
void setGInterfaceInfoFields(JNIEnv *env, jobject lpObject, GInterfaceInfo *lpStruct);
#define GInterfaceInfo_sizeof() sizeof(GInterfaceInfo)
#else
#define getGInterfaceInfoFields(a,b,c) NULL
#define setGInterfaceInfoFields(a,b,c)
#define GInterfaceInfo_sizeof() 0
#endif

#ifndef NO_GObjectClass
GObjectClass *getGObjectClassFields(JNIEnv *env, jobject lpObject, GObjectClass *lpStruct);
void setGObjectClassFields(JNIEnv *env, jobject lpObject, GObjectClass *lpStruct);
#define GObjectClass_sizeof() sizeof(GObjectClass)
#else
#define getGObjectClassFields(a,b,c) NULL
#define setGObjectClassFields(a,b,c)
#define GObjectClass_sizeof() 0
#endif

#ifndef NO_GTypeInfo
GTypeInfo *getGTypeInfoFields(JNIEnv *env, jobject lpObject, GTypeInfo *lpStruct);
void setGTypeInfoFields(JNIEnv *env, jobject lpObject, GTypeInfo *lpStruct);
#define GTypeInfo_sizeof() sizeof(GTypeInfo)
#else
#define getGTypeInfoFields(a,b,c) NULL
#define setGTypeInfoFields(a,b,c)
#define GTypeInfo_sizeof() 0
#endif

#ifndef NO_GTypeQuery
GTypeQuery *getGTypeQueryFields(JNIEnv *env, jobject lpObject, GTypeQuery *lpStruct);
void setGTypeQueryFields(JNIEnv *env, jobject lpObject, GTypeQuery *lpStruct);
#define GTypeQuery_sizeof() sizeof(GTypeQuery)
#else
#define getGTypeQueryFields(a,b,c) NULL
#define setGTypeQueryFields(a,b,c)
#define GTypeQuery_sizeof() 0
#endif

#ifndef NO_GtkAccessible
GtkAccessible *getGtkAccessibleFields(JNIEnv *env, jobject lpObject, GtkAccessible *lpStruct);
void setGtkAccessibleFields(JNIEnv *env, jobject lpObject, GtkAccessible *lpStruct);
#define GtkAccessible_sizeof() sizeof(GtkAccessible)
#else
#define getGtkAccessibleFields(a,b,c) NULL
#define setGtkAccessibleFields(a,b,c)
#define GtkAccessible_sizeof() 0
#endif

