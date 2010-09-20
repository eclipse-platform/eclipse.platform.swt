/*******************************************************************************
* Copyright (c) 2000, 2005 IBM Corporation and others. All rights reserved.
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

#ifndef INC_gnome_H
#define INC_gnome_H

#include <string.h>
#include <stdio.h>
#include <assert.h>
#include <dlfcn.h>
#include <libgnome/libgnome.h>
#include <libgnome/gnome-program.h>
#include <libgnomeui/libgnomeui.h>
#include <libgnomevfs/gnome-vfs.h>
#include <libgnomevfs/gnome-vfs-mime.h>
#include <libgnomevfs/gnome-vfs-mime-handlers.h>
#include <libgnomevfs/gnome-vfs-mime-info.h>

#ifdef AIX
#define LIB_VFS "libgnomevfs-2.a(libgnomevfs-2.so.0)"
#else
#define LIB_VFS "libgnomevfs-2.so.0"
#endif

#define gnome_vfs_url_show_LIB LIB_VFS
#define gnome_vfs_make_uri_from_input_with_dirs_LIB LIB_VFS
#define gnome_vfs_mime_application_launch_LIB LIB_VFS

#endif
