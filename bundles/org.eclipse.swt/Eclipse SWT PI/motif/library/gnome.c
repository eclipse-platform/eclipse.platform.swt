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

#include <stdio.h>
#include <assert.h>
#include <libgnome/libgnome.h>
#include <libgnome/gnome-program.h>
#include <libgnomeui/libgnomeui.h>
#include <libgnomevfs/gnome-vfs.h>
#include <libgnomevfs/gnome-vfs-mime-handlers.h>
#include <libgnomevfs/gnome-vfs-mime-info.h>

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

#ifndef NO_LIBGNOME_1MODULE
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_GNOME_LIBGNOME_1MODULE
	(JNIEnv *env, jclass that)
{
	jint rc;
	/* custom */
	rc = (jint)LIBGNOME_MODULE;
	return rc;
}
#endif

#ifndef NO_g_1free
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_GNOME_g_1free
	(JNIEnv *env, jclass that, jint arg0)
{
	g_free((gpointer)arg0);
}
#endif

#ifndef NO_g_1list_1next__I
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_GNOME_g_1list_1next__I
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("g_1list_1next__I\n")

	return (jint)g_list_next(arg0);
}
#endif

#ifndef NO_g_1malloc
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_GNOME_g_1malloc
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	rc = (jint)g_malloc((gulong)arg0);
	return rc;
}
#endif

#ifndef NO_g_1object_1unref
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_GNOME_g_1object_1unref
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("g_1object_1unref\n")
	g_object_unref((gpointer)arg0);
}
#endif

#ifndef NO_gnome_1icon_1lookup
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_GNOME_gnome_1icon_1lookup
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jbyteArray arg3, jint arg4, jbyteArray arg5, jint arg6, jintArray arg7)
{
	jbyte *lparg2=NULL;
	jbyte *lparg3=NULL;
	jbyte *lparg5=NULL;
	jint *lparg7=NULL;
	jint rc;
	DEBUG_CALL("gnome_1icon_1lookup\n")
	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	if (arg5) lparg5 = (*env)->GetByteArrayElements(env, arg5, NULL);
	if (arg7) lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL);
	rc = (jint)gnome_icon_lookup((GnomeIconTheme *)arg0, (GnomeThumbnailFactory *)arg1, (const char *)lparg2, (const char *)lparg3, (GnomeVFSFileInfo *)arg4, (const char *)lparg5, arg6, (GnomeIconLookupResultFlags *)lparg7);
	if (arg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg5) (*env)->ReleaseByteArrayElements(env, arg5, lparg5, 0);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	return rc;
}
#endif

#ifndef NO_gnome_1icon_1theme_1lookup_1icon
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_GNOME_gnome_1icon_1theme_1lookup_1icon
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jintArray arg4)
{
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc;
	DEBUG_CALL("gnome_1icon_1theme_1lookup_1icon\n")
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)gnome_icon_theme_lookup_icon((GnomeIconTheme *)arg0, (const char *)arg1, arg2, (const GnomeIconData **)lparg3, lparg4);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	return rc;
}
#endif

#ifndef NO_gnome_1icon_1theme_1new
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_GNOME_gnome_1icon_1theme_1new
	(JNIEnv *env, jclass that)
{
	DEBUG_CALL("gnome_1icon_1theme_1new\n")

	return (jint)gnome_icon_theme_new();
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

	gnome_vfs_mime_extensions_list_free((GList*)arg0);
}
#endif

#ifndef NO_gnome_1vfs_1mime_1registered_1mime_1type_1list_1free
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_GNOME_gnome_1vfs_1mime_1registered_1mime_1type_1list_1free
	(JNIEnv *env, jclass that, jint arg0)
{
	DEBUG_CALL("gnome_1vfs_1mime_1registered_1mime_1type_1list_1free\n")

	gnome_vfs_mime_registered_mime_type_list_free((GList*)arg0);
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

#ifndef NO_gnome_1vfs_1mime_1get_1icon
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_GNOME_gnome_1vfs_1mime_1get_1icon
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc;

	DEBUG_CALL("gnome_1vfs_1mime_1get_1icon\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jint)gnome_vfs_mime_get_icon(lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	return rc;
}
#endif

#ifndef NO_gnome_1program_1init
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_GNOME_gnome_1program_1init
	(JNIEnv *env, jclass that, jbyteArray arg0, jbyteArray arg1, jint arg2, jint arg3, jintArray arg4, jint arg5)
{
	jbyte *lparg0=NULL;
	jbyte *lparg1=NULL;
	jint *lparg4=NULL;
	jint rc;
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)gnome_program_init((const char *)lparg0, (const char *)lparg1, (const GnomeModuleInfo *)arg2, arg3, (char **)lparg4, (void *)arg5);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	return rc;
}
#endif

#ifndef NO_gnome_1program_1locate_1file
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_GNOME_gnome_1program_1locate_1file
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jboolean arg3, jint arg4)
{
	jint rc;
	rc = (jint)gnome_program_locate_file((GnomeProgram *)arg0, arg1, (const gchar *)arg2, (gboolean)arg3,  (GSList **)arg4);
	return rc;
}
#endif

