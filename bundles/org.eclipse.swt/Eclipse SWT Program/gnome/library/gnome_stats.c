/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others. All rights reserved.
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
#include "gnome_stats.h"

#ifdef NATIVE_STATS

int GNOME_nativeFunctionCount = 25;
int GNOME_nativeFunctionCallCount[25];
char * GNOME_nativeFunctionNames[] = {
	"GnomeVFSMimeApplication_1sizeof",
	"_1g_1free",
	"_1g_1list_1append",
	"_1g_1list_1free",
	"_1g_1list_1next",
	"_1g_1object_1unref",
	"_1gnome_1icon_1lookup",
	"_1gnome_1icon_1theme_1lookup_1icon",
	"_1gnome_1icon_1theme_1new",
	"_1gnome_1vfs_1get_1mime_1type",
	"_1gnome_1vfs_1get_1registered_1mime_1types",
	"_1gnome_1vfs_1init",
	"_1gnome_1vfs_1is_1executable_1command_1string",
	"_1gnome_1vfs_1make_1uri_1from_1input",
	"_1gnome_1vfs_1make_1uri_1from_1input_1with_1dirs",
	"_1gnome_1vfs_1mime_1application_1free",
	"_1gnome_1vfs_1mime_1application_1launch",
	"_1gnome_1vfs_1mime_1extensions_1list_1free",
	"_1gnome_1vfs_1mime_1get_1default_1application",
	"_1gnome_1vfs_1mime_1get_1extensions_1list",
	"_1gnome_1vfs_1mime_1registered_1mime_1type_1list_1free",
	"_1gnome_1vfs_1mime_1type_1from_1name",
	"_1gnome_1vfs_1mime_1type_1get_1equivalence",
	"_1gnome_1vfs_1url_1show",
	"memmove",
};

#define STATS_NATIVE(func) Java_org_eclipse_swt_tools_internal_NativeStats_##func

JNIEXPORT jint JNICALL STATS_NATIVE(GNOME_1GetFunctionCount)
	(JNIEnv *env, jclass that)
{
	return GNOME_nativeFunctionCount;
}

JNIEXPORT jstring JNICALL STATS_NATIVE(GNOME_1GetFunctionName)
	(JNIEnv *env, jclass that, jint index)
{
	return (*env)->NewStringUTF(env, GNOME_nativeFunctionNames[index]);
}

JNIEXPORT jint JNICALL STATS_NATIVE(GNOME_1GetFunctionCallCount)
	(JNIEnv *env, jclass that, jint index)
{
	return GNOME_nativeFunctionCallCount[index];
}

#endif
