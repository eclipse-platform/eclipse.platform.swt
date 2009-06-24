/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others. All rights reserved.
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

#include "gnome.h"

#ifndef NO_GnomeVFSMimeApplication
void cacheGnomeVFSMimeApplicationFields(JNIEnv *env, jobject lpObject);
GnomeVFSMimeApplication *getGnomeVFSMimeApplicationFields(JNIEnv *env, jobject lpObject, GnomeVFSMimeApplication *lpStruct);
void setGnomeVFSMimeApplicationFields(JNIEnv *env, jobject lpObject, GnomeVFSMimeApplication *lpStruct);
#define GnomeVFSMimeApplication_sizeof() sizeof(GnomeVFSMimeApplication)
#else
#define cacheGnomeVFSMimeApplicationFields(a,b)
#define getGnomeVFSMimeApplicationFields(a,b,c) NULL
#define setGnomeVFSMimeApplicationFields(a,b,c)
#define GnomeVFSMimeApplication_sizeof() 0
#endif

