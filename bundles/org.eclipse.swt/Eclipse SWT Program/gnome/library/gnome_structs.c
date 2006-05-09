/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others. All rights reserved.
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

#include "swt.h"
#include "gnome_structs.h"

#ifndef NO_GnomeVFSMimeApplication
typedef struct GnomeVFSMimeApplication_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID id, name, command, can_open_multiple_files, expects_uris, supported_uri_schemes, requires_terminal;
} GnomeVFSMimeApplication_FID_CACHE;

GnomeVFSMimeApplication_FID_CACHE GnomeVFSMimeApplicationFc;

void cacheGnomeVFSMimeApplicationFields(JNIEnv *env, jobject lpObject)
{
	if (GnomeVFSMimeApplicationFc.cached) return;
	GnomeVFSMimeApplicationFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GnomeVFSMimeApplicationFc.id = (*env)->GetFieldID(env, GnomeVFSMimeApplicationFc.clazz, "id", "I");
	GnomeVFSMimeApplicationFc.name = (*env)->GetFieldID(env, GnomeVFSMimeApplicationFc.clazz, "name", "I");
	GnomeVFSMimeApplicationFc.command = (*env)->GetFieldID(env, GnomeVFSMimeApplicationFc.clazz, "command", "I");
	GnomeVFSMimeApplicationFc.can_open_multiple_files = (*env)->GetFieldID(env, GnomeVFSMimeApplicationFc.clazz, "can_open_multiple_files", "Z");
	GnomeVFSMimeApplicationFc.expects_uris = (*env)->GetFieldID(env, GnomeVFSMimeApplicationFc.clazz, "expects_uris", "I");
	GnomeVFSMimeApplicationFc.supported_uri_schemes = (*env)->GetFieldID(env, GnomeVFSMimeApplicationFc.clazz, "supported_uri_schemes", "I");
	GnomeVFSMimeApplicationFc.requires_terminal = (*env)->GetFieldID(env, GnomeVFSMimeApplicationFc.clazz, "requires_terminal", "Z");
	GnomeVFSMimeApplicationFc.cached = 1;
}

GnomeVFSMimeApplication *getGnomeVFSMimeApplicationFields(JNIEnv *env, jobject lpObject, GnomeVFSMimeApplication *lpStruct)
{
	if (!GnomeVFSMimeApplicationFc.cached) cacheGnomeVFSMimeApplicationFields(env, lpObject);
	lpStruct->id = (char *)(*env)->GetIntField(env, lpObject, GnomeVFSMimeApplicationFc.id);
	lpStruct->name = (char *)(*env)->GetIntField(env, lpObject, GnomeVFSMimeApplicationFc.name);
	lpStruct->command = (char *)(*env)->GetIntField(env, lpObject, GnomeVFSMimeApplicationFc.command);
	lpStruct->can_open_multiple_files = (gboolean)(*env)->GetBooleanField(env, lpObject, GnomeVFSMimeApplicationFc.can_open_multiple_files);
	lpStruct->expects_uris = (GnomeVFSMimeApplicationArgumentType)(*env)->GetIntField(env, lpObject, GnomeVFSMimeApplicationFc.expects_uris);
	lpStruct->supported_uri_schemes = (GList *)(*env)->GetIntField(env, lpObject, GnomeVFSMimeApplicationFc.supported_uri_schemes);
	lpStruct->requires_terminal = (gboolean)(*env)->GetBooleanField(env, lpObject, GnomeVFSMimeApplicationFc.requires_terminal);
	return lpStruct;
}

void setGnomeVFSMimeApplicationFields(JNIEnv *env, jobject lpObject, GnomeVFSMimeApplication *lpStruct)
{
	if (!GnomeVFSMimeApplicationFc.cached) cacheGnomeVFSMimeApplicationFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GnomeVFSMimeApplicationFc.id, (jint)lpStruct->id);
	(*env)->SetIntField(env, lpObject, GnomeVFSMimeApplicationFc.name, (jint)lpStruct->name);
	(*env)->SetIntField(env, lpObject, GnomeVFSMimeApplicationFc.command, (jint)lpStruct->command);
	(*env)->SetBooleanField(env, lpObject, GnomeVFSMimeApplicationFc.can_open_multiple_files, (jboolean)lpStruct->can_open_multiple_files);
	(*env)->SetIntField(env, lpObject, GnomeVFSMimeApplicationFc.expects_uris, (jint)lpStruct->expects_uris);
	(*env)->SetIntField(env, lpObject, GnomeVFSMimeApplicationFc.supported_uri_schemes, (jint)lpStruct->supported_uri_schemes);
	(*env)->SetBooleanField(env, lpObject, GnomeVFSMimeApplicationFc.requires_terminal, (jboolean)lpStruct->requires_terminal);
}
#endif

