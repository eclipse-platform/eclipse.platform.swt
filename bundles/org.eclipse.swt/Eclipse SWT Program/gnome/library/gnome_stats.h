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

#ifdef NATIVE_STATS
int GNOME_nativeFunctionCallCount[];
char* GNOME_nativeFunctionNames[];
#define GNOME_NATIVE_ENTER(env, that, func) GNOME_nativeFunctionCallCount[func]++;
#define GNOME_NATIVE_EXIT(env, that, func) 
#else
#define GNOME_NATIVE_ENTER(env, that, func) 
#define GNOME_NATIVE_EXIT(env, that, func) 
#endif

#define GnomeVFSMimeApplication_1sizeof_FUNC 0
#define g_1free_FUNC 1
#define g_1list_1next_FUNC 2
#define g_1object_1unref_FUNC 3
#define gnome_1icon_1lookup_FUNC 4
#define gnome_1icon_1theme_1lookup_1icon_FUNC 5
#define gnome_1icon_1theme_1new_FUNC 6
#define gnome_1vfs_1get_1registered_1mime_1types_FUNC 7
#define gnome_1vfs_1init_FUNC 8
#define gnome_1vfs_1mime_1application_1free_FUNC 9
#define gnome_1vfs_1mime_1extensions_1list_1free_FUNC 10
#define gnome_1vfs_1mime_1get_1default_1application_FUNC 11
#define gnome_1vfs_1mime_1get_1extensions_1list_FUNC 12
#define gnome_1vfs_1mime_1registered_1mime_1type_1list_1free_FUNC 13
#define memmove_FUNC 14
