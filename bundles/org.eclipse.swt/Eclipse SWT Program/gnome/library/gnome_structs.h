/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
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

