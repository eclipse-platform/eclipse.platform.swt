/*******************************************************************************
 * Copyright (c) 2009, 2010 IBM Corporation and others. All rights reserved.
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

#include "webkit_win32.h"

#ifndef NO_JSClassDefinition
void cacheJSClassDefinitionFields(JNIEnv *env, jobject lpObject);
JSClassDefinition *getJSClassDefinitionFields(JNIEnv *env, jobject lpObject, JSClassDefinition *lpStruct);
void setJSClassDefinitionFields(JNIEnv *env, jobject lpObject, JSClassDefinition *lpStruct);
#define JSClassDefinition_sizeof() sizeof(JSClassDefinition)
#else
#define cacheJSClassDefinitionFields(a,b)
#define getJSClassDefinitionFields(a,b,c) NULL
#define setJSClassDefinitionFields(a,b,c)
#define JSClassDefinition_sizeof() 0
#endif

