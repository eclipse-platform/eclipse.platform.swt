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

