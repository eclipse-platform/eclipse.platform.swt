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

#ifdef NATIVE_STATS
extern int GNOME_nativeFunctionCount;
extern int GNOME_nativeFunctionCallCount[];
extern char* GNOME_nativeFunctionNames[];
#define GNOME_NATIVE_ENTER(env, that, func) GNOME_nativeFunctionCallCount[func]++;
#define GNOME_NATIVE_EXIT(env, that, func) 
#else
#ifndef GNOME_NATIVE_ENTER
#define GNOME_NATIVE_ENTER(env, that, func) 
#endif
#ifndef GNOME_NATIVE_EXIT
#define GNOME_NATIVE_EXIT(env, that, func) 
#endif
#endif

typedef enum {
	GnomeVFSMimeApplication_1sizeof_FUNC,
	_1g_1free_FUNC,
	_1g_1list_1append_FUNC,
	_1g_1list_1free_FUNC,
	_1g_1list_1next_FUNC,
	_1g_1object_1unref_FUNC,
	_1gnome_1icon_1lookup_FUNC,
	_1gnome_1icon_1theme_1lookup_1icon_FUNC,
	_1gnome_1icon_1theme_1new_FUNC,
	_1gnome_1vfs_1get_1mime_1type_FUNC,
	_1gnome_1vfs_1get_1registered_1mime_1types_FUNC,
	_1gnome_1vfs_1init_FUNC,
	_1gnome_1vfs_1is_1executable_1command_1string_FUNC,
	_1gnome_1vfs_1make_1uri_1from_1input_FUNC,
	_1gnome_1vfs_1make_1uri_1from_1input_1with_1dirs_FUNC,
	_1gnome_1vfs_1mime_1application_1free_FUNC,
	_1gnome_1vfs_1mime_1application_1launch_FUNC,
	_1gnome_1vfs_1mime_1can_1be_1executable_FUNC,
	_1gnome_1vfs_1mime_1extensions_1list_1free_FUNC,
	_1gnome_1vfs_1mime_1get_1default_1application_FUNC,
	_1gnome_1vfs_1mime_1get_1extensions_1list_FUNC,
	_1gnome_1vfs_1mime_1registered_1mime_1type_1list_1free_FUNC,
	_1gnome_1vfs_1mime_1type_1from_1name_FUNC,
	_1gnome_1vfs_1url_1show_FUNC,
	memmove_FUNC,
} GNOME_FUNCS;
