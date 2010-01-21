/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others. All rights reserved.
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
void cacheAtkActionIfaceFields(JNIEnv *env, jobject lpObject);
AtkActionIface *getAtkActionIfaceFields(JNIEnv *env, jobject lpObject, AtkActionIface *lpStruct);
void setAtkActionIfaceFields(JNIEnv *env, jobject lpObject, AtkActionIface *lpStruct);
#define AtkActionIface_sizeof() sizeof(AtkActionIface)
#else
#define cacheAtkActionIfaceFields(a,b)
#define getAtkActionIfaceFields(a,b,c) NULL
#define setAtkActionIfaceFields(a,b,c)
#define AtkActionIface_sizeof() 0
#endif

#ifndef NO_AtkAttribute
void cacheAtkAttributeFields(JNIEnv *env, jobject lpObject);
AtkAttribute *getAtkAttributeFields(JNIEnv *env, jobject lpObject, AtkAttribute *lpStruct);
void setAtkAttributeFields(JNIEnv *env, jobject lpObject, AtkAttribute *lpStruct);
#define AtkAttribute_sizeof() sizeof(AtkAttribute)
#else
#define cacheAtkAttributeFields(a,b)
#define getAtkAttributeFields(a,b,c) NULL
#define setAtkAttributeFields(a,b,c)
#define AtkAttribute_sizeof() 0
#endif

#ifndef NO_AtkComponentIface
void cacheAtkComponentIfaceFields(JNIEnv *env, jobject lpObject);
AtkComponentIface *getAtkComponentIfaceFields(JNIEnv *env, jobject lpObject, AtkComponentIface *lpStruct);
void setAtkComponentIfaceFields(JNIEnv *env, jobject lpObject, AtkComponentIface *lpStruct);
#define AtkComponentIface_sizeof() sizeof(AtkComponentIface)
#else
#define cacheAtkComponentIfaceFields(a,b)
#define getAtkComponentIfaceFields(a,b,c) NULL
#define setAtkComponentIfaceFields(a,b,c)
#define AtkComponentIface_sizeof() 0
#endif

#ifndef NO_AtkHypertextIface
void cacheAtkHypertextIfaceFields(JNIEnv *env, jobject lpObject);
AtkHypertextIface *getAtkHypertextIfaceFields(JNIEnv *env, jobject lpObject, AtkHypertextIface *lpStruct);
void setAtkHypertextIfaceFields(JNIEnv *env, jobject lpObject, AtkHypertextIface *lpStruct);
#define AtkHypertextIface_sizeof() sizeof(AtkHypertextIface)
#else
#define cacheAtkHypertextIfaceFields(a,b)
#define getAtkHypertextIfaceFields(a,b,c) NULL
#define setAtkHypertextIfaceFields(a,b,c)
#define AtkHypertextIface_sizeof() 0
#endif

#ifndef NO_AtkObjectClass
void cacheAtkObjectClassFields(JNIEnv *env, jobject lpObject);
AtkObjectClass *getAtkObjectClassFields(JNIEnv *env, jobject lpObject, AtkObjectClass *lpStruct);
void setAtkObjectClassFields(JNIEnv *env, jobject lpObject, AtkObjectClass *lpStruct);
#define AtkObjectClass_sizeof() sizeof(AtkObjectClass)
#else
#define cacheAtkObjectClassFields(a,b)
#define getAtkObjectClassFields(a,b,c) NULL
#define setAtkObjectClassFields(a,b,c)
#define AtkObjectClass_sizeof() 0
#endif

#ifndef NO_AtkObjectFactoryClass
void cacheAtkObjectFactoryClassFields(JNIEnv *env, jobject lpObject);
AtkObjectFactoryClass *getAtkObjectFactoryClassFields(JNIEnv *env, jobject lpObject, AtkObjectFactoryClass *lpStruct);
void setAtkObjectFactoryClassFields(JNIEnv *env, jobject lpObject, AtkObjectFactoryClass *lpStruct);
#define AtkObjectFactoryClass_sizeof() sizeof(AtkObjectFactoryClass)
#else
#define cacheAtkObjectFactoryClassFields(a,b)
#define getAtkObjectFactoryClassFields(a,b,c) NULL
#define setAtkObjectFactoryClassFields(a,b,c)
#define AtkObjectFactoryClass_sizeof() 0
#endif

#ifndef NO_AtkSelectionIface
void cacheAtkSelectionIfaceFields(JNIEnv *env, jobject lpObject);
AtkSelectionIface *getAtkSelectionIfaceFields(JNIEnv *env, jobject lpObject, AtkSelectionIface *lpStruct);
void setAtkSelectionIfaceFields(JNIEnv *env, jobject lpObject, AtkSelectionIface *lpStruct);
#define AtkSelectionIface_sizeof() sizeof(AtkSelectionIface)
#else
#define cacheAtkSelectionIfaceFields(a,b)
#define getAtkSelectionIfaceFields(a,b,c) NULL
#define setAtkSelectionIfaceFields(a,b,c)
#define AtkSelectionIface_sizeof() 0
#endif

#ifndef NO_AtkTableIface
void cacheAtkTableIfaceFields(JNIEnv *env, jobject lpObject);
AtkTableIface *getAtkTableIfaceFields(JNIEnv *env, jobject lpObject, AtkTableIface *lpStruct);
void setAtkTableIfaceFields(JNIEnv *env, jobject lpObject, AtkTableIface *lpStruct);
#define AtkTableIface_sizeof() sizeof(AtkTableIface)
#else
#define cacheAtkTableIfaceFields(a,b)
#define getAtkTableIfaceFields(a,b,c) NULL
#define setAtkTableIfaceFields(a,b,c)
#define AtkTableIface_sizeof() 0
#endif

#ifndef NO_AtkTextIface
void cacheAtkTextIfaceFields(JNIEnv *env, jobject lpObject);
AtkTextIface *getAtkTextIfaceFields(JNIEnv *env, jobject lpObject, AtkTextIface *lpStruct);
void setAtkTextIfaceFields(JNIEnv *env, jobject lpObject, AtkTextIface *lpStruct);
#define AtkTextIface_sizeof() sizeof(AtkTextIface)
#else
#define cacheAtkTextIfaceFields(a,b)
#define getAtkTextIfaceFields(a,b,c) NULL
#define setAtkTextIfaceFields(a,b,c)
#define AtkTextIface_sizeof() 0
#endif

#ifndef NO_AtkTextRange
void cacheAtkTextRangeFields(JNIEnv *env, jobject lpObject);
AtkTextRange *getAtkTextRangeFields(JNIEnv *env, jobject lpObject, AtkTextRange *lpStruct);
void setAtkTextRangeFields(JNIEnv *env, jobject lpObject, AtkTextRange *lpStruct);
#define AtkTextRange_sizeof() sizeof(AtkTextRange)
#else
#define cacheAtkTextRangeFields(a,b)
#define getAtkTextRangeFields(a,b,c) NULL
#define setAtkTextRangeFields(a,b,c)
#define AtkTextRange_sizeof() 0
#endif

#ifndef NO_AtkTextRectangle
void cacheAtkTextRectangleFields(JNIEnv *env, jobject lpObject);
AtkTextRectangle *getAtkTextRectangleFields(JNIEnv *env, jobject lpObject, AtkTextRectangle *lpStruct);
void setAtkTextRectangleFields(JNIEnv *env, jobject lpObject, AtkTextRectangle *lpStruct);
#define AtkTextRectangle_sizeof() sizeof(AtkTextRectangle)
#else
#define cacheAtkTextRectangleFields(a,b)
#define getAtkTextRectangleFields(a,b,c) NULL
#define setAtkTextRectangleFields(a,b,c)
#define AtkTextRectangle_sizeof() 0
#endif

#ifndef NO_AtkValueIface
void cacheAtkValueIfaceFields(JNIEnv *env, jobject lpObject);
AtkValueIface *getAtkValueIfaceFields(JNIEnv *env, jobject lpObject, AtkValueIface *lpStruct);
void setAtkValueIfaceFields(JNIEnv *env, jobject lpObject, AtkValueIface *lpStruct);
#define AtkValueIface_sizeof() sizeof(AtkValueIface)
#else
#define cacheAtkValueIfaceFields(a,b)
#define getAtkValueIfaceFields(a,b,c) NULL
#define setAtkValueIfaceFields(a,b,c)
#define AtkValueIface_sizeof() 0
#endif

#ifndef NO_GtkAccessible
void cacheGtkAccessibleFields(JNIEnv *env, jobject lpObject);
GtkAccessible *getGtkAccessibleFields(JNIEnv *env, jobject lpObject, GtkAccessible *lpStruct);
void setGtkAccessibleFields(JNIEnv *env, jobject lpObject, GtkAccessible *lpStruct);
#define GtkAccessible_sizeof() sizeof(GtkAccessible)
#else
#define cacheGtkAccessibleFields(a,b)
#define getGtkAccessibleFields(a,b,c) NULL
#define setGtkAccessibleFields(a,b,c)
#define GtkAccessible_sizeof() 0
#endif

