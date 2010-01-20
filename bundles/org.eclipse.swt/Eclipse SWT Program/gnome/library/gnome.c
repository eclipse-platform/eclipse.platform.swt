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

#include "swt.h"
#include "gnome_structs.h"
#include "gnome_stats.h"

#define GNOME_NATIVE(func) Java_org_eclipse_swt_internal_gnome_GNOME_##func

#ifndef NO_GnomeVFSMimeApplication_1sizeof
JNIEXPORT jint JNICALL GNOME_NATIVE(GnomeVFSMimeApplication_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	GNOME_NATIVE_ENTER(env, that, GnomeVFSMimeApplication_1sizeof_FUNC);
	rc = (jint)GnomeVFSMimeApplication_sizeof();
	GNOME_NATIVE_EXIT(env, that, GnomeVFSMimeApplication_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1free
JNIEXPORT void JNICALL GNOME_NATIVE(_1g_1free)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	GNOME_NATIVE_ENTER(env, that, _1g_1free_FUNC);
	g_free((gpointer)arg0);
	GNOME_NATIVE_EXIT(env, that, _1g_1free_FUNC);
}
#endif

#ifndef NO__1g_1list_1append
JNIEXPORT jintLong JNICALL GNOME_NATIVE(_1g_1list_1append)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	GNOME_NATIVE_ENTER(env, that, _1g_1list_1append_FUNC);
	rc = (jintLong)g_list_append((GList *)arg0, (gpointer)arg1);
	GNOME_NATIVE_EXIT(env, that, _1g_1list_1append_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1list_1free
JNIEXPORT void JNICALL GNOME_NATIVE(_1g_1list_1free)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	GNOME_NATIVE_ENTER(env, that, _1g_1list_1free_FUNC);
	g_list_free((GList *)arg0);
	GNOME_NATIVE_EXIT(env, that, _1g_1list_1free_FUNC);
}
#endif

#ifndef NO__1g_1list_1next
JNIEXPORT jintLong JNICALL GNOME_NATIVE(_1g_1list_1next)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	GNOME_NATIVE_ENTER(env, that, _1g_1list_1next_FUNC);
	rc = (jintLong)g_list_next(arg0);
	GNOME_NATIVE_EXIT(env, that, _1g_1list_1next_FUNC);
	return rc;
}
#endif

#ifndef NO__1g_1object_1unref
JNIEXPORT void JNICALL GNOME_NATIVE(_1g_1object_1unref)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	GNOME_NATIVE_ENTER(env, that, _1g_1object_1unref_FUNC);
	g_object_unref((gpointer)arg0);
	GNOME_NATIVE_EXIT(env, that, _1g_1object_1unref_FUNC);
}
#endif

#ifndef NO__1gnome_1icon_1lookup
JNIEXPORT jintLong JNICALL GNOME_NATIVE(_1gnome_1icon_1lookup)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jbyteArray arg2, jbyteArray arg3, jintLong arg4, jbyteArray arg5, jint arg6, jintArray arg7)
{
	jbyte *lparg2=NULL;
	jbyte *lparg3=NULL;
	jbyte *lparg5=NULL;
	jint *lparg7=NULL;
	jintLong rc = 0;
	GNOME_NATIVE_ENTER(env, that, _1gnome_1icon_1lookup_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetByteArrayElements(env, arg5, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL)) == NULL) goto fail;
	rc = (jintLong)gnome_icon_lookup((GnomeIconTheme *)arg0, (GnomeThumbnailFactory *)arg1, (const char *)lparg2, (const char *)lparg3, (GnomeVFSFileInfo *)arg4, (const char *)lparg5, (GnomeIconLookupFlags)arg6, (GnomeIconLookupResultFlags *)lparg7);
fail:
	if (arg7 && lparg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg5 && lparg5) (*env)->ReleaseByteArrayElements(env, arg5, lparg5, 0);
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	GNOME_NATIVE_EXIT(env, that, _1gnome_1icon_1lookup_FUNC);
	return rc;
}
#endif

#ifndef NO__1gnome_1icon_1theme_1lookup_1icon
JNIEXPORT jintLong JNICALL GNOME_NATIVE(_1gnome_1icon_1theme_1lookup_1icon)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jintLongArray arg3, jintArray arg4)
{
	jintLong *lparg3=NULL;
	jint *lparg4=NULL;
	jintLong rc = 0;
	GNOME_NATIVE_ENTER(env, that, _1gnome_1icon_1theme_1lookup_1icon_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntLongArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jintLong)gnome_icon_theme_lookup_icon((GnomeIconTheme *)arg0, (const char *)arg1, arg2, (const GnomeIconData **)lparg3, lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntLongArrayElements(env, arg3, lparg3, 0);
	GNOME_NATIVE_EXIT(env, that, _1gnome_1icon_1theme_1lookup_1icon_FUNC);
	return rc;
}
#endif

#ifndef NO__1gnome_1icon_1theme_1new
JNIEXPORT jintLong JNICALL GNOME_NATIVE(_1gnome_1icon_1theme_1new)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	GNOME_NATIVE_ENTER(env, that, _1gnome_1icon_1theme_1new_FUNC);
	rc = (jintLong)gnome_icon_theme_new();
	GNOME_NATIVE_EXIT(env, that, _1gnome_1icon_1theme_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1gnome_1vfs_1get_1mime_1type
JNIEXPORT jintLong JNICALL GNOME_NATIVE(_1gnome_1vfs_1get_1mime_1type)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	GNOME_NATIVE_ENTER(env, that, _1gnome_1vfs_1get_1mime_1type_FUNC);
	rc = (jintLong)gnome_vfs_get_mime_type((const char *)arg0);
	GNOME_NATIVE_EXIT(env, that, _1gnome_1vfs_1get_1mime_1type_FUNC);
	return rc;
}
#endif

#ifndef NO__1gnome_1vfs_1get_1registered_1mime_1types
JNIEXPORT jintLong JNICALL GNOME_NATIVE(_1gnome_1vfs_1get_1registered_1mime_1types)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	GNOME_NATIVE_ENTER(env, that, _1gnome_1vfs_1get_1registered_1mime_1types_FUNC);
	rc = (jintLong)gnome_vfs_get_registered_mime_types();
	GNOME_NATIVE_EXIT(env, that, _1gnome_1vfs_1get_1registered_1mime_1types_FUNC);
	return rc;
}
#endif

#ifndef NO__1gnome_1vfs_1init
JNIEXPORT jboolean JNICALL GNOME_NATIVE(_1gnome_1vfs_1init)
	(JNIEnv *env, jclass that)
{
	jboolean rc = 0;
	GNOME_NATIVE_ENTER(env, that, _1gnome_1vfs_1init_FUNC);
	rc = (jboolean)gnome_vfs_init();
	GNOME_NATIVE_EXIT(env, that, _1gnome_1vfs_1init_FUNC);
	return rc;
}
#endif

#ifndef NO__1gnome_1vfs_1is_1executable_1command_1string
JNIEXPORT jboolean JNICALL GNOME_NATIVE(_1gnome_1vfs_1is_1executable_1command_1string)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jboolean rc = 0;
	GNOME_NATIVE_ENTER(env, that, _1gnome_1vfs_1is_1executable_1command_1string_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jboolean)gnome_vfs_is_executable_command_string((const char *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	GNOME_NATIVE_EXIT(env, that, _1gnome_1vfs_1is_1executable_1command_1string_FUNC);
	return rc;
}
#endif

#ifndef NO__1gnome_1vfs_1make_1uri_1from_1input
JNIEXPORT jintLong JNICALL GNOME_NATIVE(_1gnome_1vfs_1make_1uri_1from_1input)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	GNOME_NATIVE_ENTER(env, that, _1gnome_1vfs_1make_1uri_1from_1input_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)gnome_vfs_make_uri_from_input((const char *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	GNOME_NATIVE_EXIT(env, that, _1gnome_1vfs_1make_1uri_1from_1input_FUNC);
	return rc;
}
#endif

#ifndef NO__1gnome_1vfs_1make_1uri_1from_1input_1with_1dirs
JNIEXPORT jintLong JNICALL GNOME_NATIVE(_1gnome_1vfs_1make_1uri_1from_1input_1with_1dirs)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	GNOME_NATIVE_ENTER(env, that, _1gnome_1vfs_1make_1uri_1from_1input_1with_1dirs_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
/*
	rc = (jintLong)gnome_vfs_make_uri_from_input_with_dirs((const char *)lparg0, arg1);
*/
	{
		LOAD_FUNCTION(fp, gnome_vfs_make_uri_from_input_with_dirs)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(const char *, jint))fp)((const char *)lparg0, arg1);
		}
	}
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	GNOME_NATIVE_EXIT(env, that, _1gnome_1vfs_1make_1uri_1from_1input_1with_1dirs_FUNC);
	return rc;
}
#endif

#ifndef NO__1gnome_1vfs_1mime_1application_1free
JNIEXPORT void JNICALL GNOME_NATIVE(_1gnome_1vfs_1mime_1application_1free)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	GNOME_NATIVE_ENTER(env, that, _1gnome_1vfs_1mime_1application_1free_FUNC);
	gnome_vfs_mime_application_free((GnomeVFSMimeApplication *)arg0);
	GNOME_NATIVE_EXIT(env, that, _1gnome_1vfs_1mime_1application_1free_FUNC);
}
#endif

#ifndef NO__1gnome_1vfs_1mime_1application_1launch
JNIEXPORT jint JNICALL GNOME_NATIVE(_1gnome_1vfs_1mime_1application_1launch)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jint rc = 0;
	GNOME_NATIVE_ENTER(env, that, _1gnome_1vfs_1mime_1application_1launch_FUNC);
/*
	rc = (jint)gnome_vfs_mime_application_launch((GnomeVFSMimeApplication *)arg0, (GList *)arg1);
*/
	{
		LOAD_FUNCTION(fp, gnome_vfs_mime_application_launch)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(GnomeVFSMimeApplication *, GList *))fp)((GnomeVFSMimeApplication *)arg0, (GList *)arg1);
		}
	}
	GNOME_NATIVE_EXIT(env, that, _1gnome_1vfs_1mime_1application_1launch_FUNC);
	return rc;
}
#endif

#ifndef NO__1gnome_1vfs_1mime_1can_1be_1executable
JNIEXPORT jboolean JNICALL GNOME_NATIVE(_1gnome_1vfs_1mime_1can_1be_1executable)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jboolean rc = 0;
	GNOME_NATIVE_ENTER(env, that, _1gnome_1vfs_1mime_1can_1be_1executable_FUNC);
	rc = (jboolean)gnome_vfs_mime_can_be_executable((const char *)arg0);
	GNOME_NATIVE_EXIT(env, that, _1gnome_1vfs_1mime_1can_1be_1executable_FUNC);
	return rc;
}
#endif

#ifndef NO__1gnome_1vfs_1mime_1extensions_1list_1free
JNIEXPORT void JNICALL GNOME_NATIVE(_1gnome_1vfs_1mime_1extensions_1list_1free)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	GNOME_NATIVE_ENTER(env, that, _1gnome_1vfs_1mime_1extensions_1list_1free_FUNC);
	gnome_vfs_mime_extensions_list_free((GList *)arg0);
	GNOME_NATIVE_EXIT(env, that, _1gnome_1vfs_1mime_1extensions_1list_1free_FUNC);
}
#endif

#ifndef NO__1gnome_1vfs_1mime_1get_1default_1application
JNIEXPORT jintLong JNICALL GNOME_NATIVE(_1gnome_1vfs_1mime_1get_1default_1application)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	GNOME_NATIVE_ENTER(env, that, _1gnome_1vfs_1mime_1get_1default_1application_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)gnome_vfs_mime_get_default_application((const char *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	GNOME_NATIVE_EXIT(env, that, _1gnome_1vfs_1mime_1get_1default_1application_FUNC);
	return rc;
}
#endif

#ifndef NO__1gnome_1vfs_1mime_1get_1extensions_1list
JNIEXPORT jintLong JNICALL GNOME_NATIVE(_1gnome_1vfs_1mime_1get_1extensions_1list)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jintLong rc = 0;
	GNOME_NATIVE_ENTER(env, that, _1gnome_1vfs_1mime_1get_1extensions_1list_FUNC);
	rc = (jintLong)gnome_vfs_mime_get_extensions_list((const char *)arg0);
	GNOME_NATIVE_EXIT(env, that, _1gnome_1vfs_1mime_1get_1extensions_1list_FUNC);
	return rc;
}
#endif

#ifndef NO__1gnome_1vfs_1mime_1registered_1mime_1type_1list_1free
JNIEXPORT void JNICALL GNOME_NATIVE(_1gnome_1vfs_1mime_1registered_1mime_1type_1list_1free)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	GNOME_NATIVE_ENTER(env, that, _1gnome_1vfs_1mime_1registered_1mime_1type_1list_1free_FUNC);
	gnome_vfs_mime_registered_mime_type_list_free((GList *)arg0);
	GNOME_NATIVE_EXIT(env, that, _1gnome_1vfs_1mime_1registered_1mime_1type_1list_1free_FUNC);
}
#endif

#ifndef NO__1gnome_1vfs_1mime_1type_1from_1name
JNIEXPORT jintLong JNICALL GNOME_NATIVE(_1gnome_1vfs_1mime_1type_1from_1name)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	GNOME_NATIVE_ENTER(env, that, _1gnome_1vfs_1mime_1type_1from_1name_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)gnome_vfs_mime_type_from_name((const char *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	GNOME_NATIVE_EXIT(env, that, _1gnome_1vfs_1mime_1type_1from_1name_FUNC);
	return rc;
}
#endif

#ifndef NO__1gnome_1vfs_1url_1show
JNIEXPORT jint JNICALL GNOME_NATIVE(_1gnome_1vfs_1url_1show)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	GNOME_NATIVE_ENTER(env, that, _1gnome_1vfs_1url_1show_FUNC);
/*
	rc = (jint)gnome_vfs_url_show((const char *)arg0);
*/
	{
		LOAD_FUNCTION(fp, gnome_vfs_url_show)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(const char *))fp)((const char *)arg0);
		}
	}
	GNOME_NATIVE_EXIT(env, that, _1gnome_1vfs_1url_1show_FUNC);
	return rc;
}
#endif

#ifndef NO_memmove
JNIEXPORT void JNICALL GNOME_NATIVE(memmove)
	(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
{
	GnomeVFSMimeApplication _arg0, *lparg0=NULL;
	GNOME_NATIVE_ENTER(env, that, memmove_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setGnomeVFSMimeApplicationFields(env, arg0, lparg0);
	GNOME_NATIVE_EXIT(env, that, memmove_FUNC);
}
#endif

