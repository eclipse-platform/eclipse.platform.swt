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

#include "swt.h"
#include "os_structs.h"

#ifdef NATIVE_STATS

int GNOME_nativeFunctionCount = 15;
int GNOME_nativeFunctionCallCount[15];
char * GNOME_nativeFunctionNames[] = {
	"GnomeVFSMimeApplication_1sizeof", 
	"g_1free", 
	"g_1list_1next", 
	"g_1object_1unref", 
	"gnome_1icon_1lookup", 
	"gnome_1icon_1theme_1lookup_1icon", 
	"gnome_1icon_1theme_1new", 
	"gnome_1vfs_1get_1registered_1mime_1types", 
	"gnome_1vfs_1init", 
	"gnome_1vfs_1mime_1application_1free", 
	"gnome_1vfs_1mime_1extensions_1list_1free", 
	"gnome_1vfs_1mime_1get_1default_1application", 
	"gnome_1vfs_1mime_1get_1extensions_1list", 
	"gnome_1vfs_1mime_1registered_1mime_1type_1list_1free", 
	"memmove", 
};

#endif
