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

/**
 * SWT GNOME natives implementation.
 */ 

/* #define PRINT_FAILED_RCODES */
#define NDEBUG 

#include "swt.h"
#include "structs.h"

#include <stdio.h>
#include <assert.h>
#include <libgnomevfs/gnome-vfs.h>
#include <libgnomevfs/gnome-vfs-mime-handlers.h>

#ifndef NO_GnomeVFSMimeApplication
typedef struct GnomeVFSMimeApplication_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID requires_terminal, supported_uri_schemes, expects_uris, can_open_multiple_files, command, name, id;
} GnomeVFSMimeApplication_FID_CACHE;

GnomeVFSMimeApplication_FID_CACHE GnomeVFSMimeApplicationFc;

void cacheGnomeVFSMimeApplicationFids(JNIEnv *env, jobject lpObject)
{
	if (GnomeVFSMimeApplicationFc.cached) return;
	GnomeVFSMimeApplicationFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GnomeVFSMimeApplicationFc.requires_terminal = (*env)->GetFieldID(env, GnomeVFSMimeApplicationFc.clazz, "requires_terminal", "Z");
	GnomeVFSMimeApplicationFc.supported_uri_schemes = (*env)->GetFieldID(env, GnomeVFSMimeApplicationFc.clazz, "supported_uri_schemes", "I");
	GnomeVFSMimeApplicationFc.expects_uris = (*env)->GetFieldID(env, GnomeVFSMimeApplicationFc.clazz, "expects_uris", "I");
	GnomeVFSMimeApplicationFc.can_open_multiple_files = (*env)->GetFieldID(env, GnomeVFSMimeApplicationFc.clazz, "can_open_multiple_files", "Z");
	GnomeVFSMimeApplicationFc.command = (*env)->GetFieldID(env, GnomeVFSMimeApplicationFc.clazz, "command", "I");
	GnomeVFSMimeApplicationFc.name = (*env)->GetFieldID(env, GnomeVFSMimeApplicationFc.clazz, "name", "I");
	GnomeVFSMimeApplicationFc.id = (*env)->GetFieldID(env, GnomeVFSMimeApplicationFc.clazz, "id", "I");
	GnomeVFSMimeApplicationFc.cached = 1;
}

GnomeVFSMimeApplication *getGnomeVFSMimeApplicationFields(JNIEnv *env, jobject lpObject, GnomeVFSMimeApplication *lpStruct)
{
	if (!GnomeVFSMimeApplicationFc.cached) cacheGnomeVFSMimeApplicationFids(env, lpObject);
	lpStruct->requires_terminal = (gboolean)(*env)->GetBooleanField(env, lpObject, GnomeVFSMimeApplicationFc.requires_terminal);
	lpStruct->supported_uri_schemes = (GList *)(*env)->GetIntField(env, lpObject, GnomeVFSMimeApplicationFc.supported_uri_schemes);
	lpStruct->expects_uris = (GnomeVFSMimeApplicationArgumentType)(*env)->GetIntField(env, lpObject, GnomeVFSMimeApplicationFc.expects_uris);
	lpStruct->can_open_multiple_files = (gboolean)(*env)->GetBooleanField(env, lpObject, GnomeVFSMimeApplicationFc.can_open_multiple_files);
	lpStruct->command = (char *)(*env)->GetIntField(env, lpObject, GnomeVFSMimeApplicationFc.command);
	lpStruct->name = (char *)(*env)->GetIntField(env, lpObject, GnomeVFSMimeApplicationFc.name);
	lpStruct->id = (char *)(*env)->GetIntField(env, lpObject, GnomeVFSMimeApplicationFc.id);
	return lpStruct;
}

void setGnomeVFSMimeApplicationFields(JNIEnv *env, jobject lpObject, GnomeVFSMimeApplication *lpStruct)
{
	if (!GnomeVFSMimeApplicationFc.cached) cacheGnomeVFSMimeApplicationFids(env, lpObject);
	(*env)->SetBooleanField(env, lpObject, GnomeVFSMimeApplicationFc.requires_terminal, (jboolean)lpStruct->requires_terminal);
	(*env)->SetIntField(env, lpObject, GnomeVFSMimeApplicationFc.supported_uri_schemes, (jint)lpStruct->supported_uri_schemes);
	(*env)->SetIntField(env, lpObject, GnomeVFSMimeApplicationFc.expects_uris, (jint)lpStruct->expects_uris);
	(*env)->SetBooleanField(env, lpObject, GnomeVFSMimeApplicationFc.can_open_multiple_files, (jboolean)lpStruct->can_open_multiple_files);
	(*env)->SetIntField(env, lpObject, GnomeVFSMimeApplicationFc.command, (jint)lpStruct->command);
	(*env)->SetIntField(env, lpObject, GnomeVFSMimeApplicationFc.name, (jint)lpStruct->name);
	(*env)->SetIntField(env, lpObject, GnomeVFSMimeApplicationFc.id, (jint)lpStruct->id);
}
#endif /* NO_GnomeVFSMimeApplication */

#ifndef NO_g_1list_1next__I
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_GNOME_g_1list_1next__I
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("g_1list_1next__I\n")

	return (jint)g_list_next(arg0);
}
#endif

#ifndef NO_gnome_1vfs_1get_1registered_1mime_1types
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_GNOME_gnome_1vfs_1get_1registered_1mime_1types
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gnome_1vfs_1get_1registered_1mime_1types\n")

	return (jint)gnome_vfs_get_registered_mime_types();
}
#endif

#ifndef NO_gnome_1vfs_1init
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_motif_GNOME_gnome_1vfs_1init
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gnome_1vfs_1init\n")

	return (jboolean)gnome_vfs_init();
}
#endif

#ifndef NO_gnome_1vfs_1mime_1application_1free
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_GNOME_gnome_1vfs_1mime_1application_1free
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gnome_1vfs_1mime_1application_1free\n")

	gnome_vfs_mime_application_free((GnomeVFSMimeApplication *)arg0);
}
#endif

#ifndef NO_gnome_1vfs_1mime_1extensions_1list_1free
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_GNOME_gnome_1vfs_1mime_1extensions_1list_1free
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gnome_1vfs_1mime_1extensions_1list_1free\n")

	gnome_vfs_mime_extensions_list_free(arg0);
}
#endif

#ifndef NO_gnome_1vfs_1mime_1registered_1mime_1type_1list_1free
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_GNOME_gnome_1vfs_1mime_1registered_1mime_1type_1list_1free
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gnome_1vfs_1mime_1registered_1mime_1type_1list_1free\n")

	gnome_vfs_mime_registered_mime_type_list_free(arg0);
}
#endif

#ifndef NO_gnome_1vfs_1mime_1get_1default_1application
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_GNOME_gnome_1vfs_1mime_1get_1default_1application
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc;

	DEBUG_CALL("gnome_1vfs_1mime_1get_1default_1application\n")

	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jint)gnome_vfs_mime_get_default_application(lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	return rc;
}
#endif

#ifndef NO_gnome_1vfs_1mime_1get_1extensions_1list
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_GNOME_gnome_1vfs_1mime_1get_1extensions_1list
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gnome_1vfs_1mime_1get_1extensions_1list\n")

	return (jint)gnome_vfs_mime_get_extensions_list((const char *)arg0);
}
#endif

#ifndef NO_memmove
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_GNOME_memmove
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	GnomeVFSMimeApplication _arg0, *lparg0=NULL;

	DEBUG_CALL("memmove\n")

	if (arg0) lparg0 = getGnomeVFSMimeApplicationFields(env, arg0, &_arg0);
	memmove((void *)lparg0, (void *)arg1, arg2);
	if (arg0) setGnomeVFSMimeApplicationFields(env, arg0, lparg0);
}
#endif


