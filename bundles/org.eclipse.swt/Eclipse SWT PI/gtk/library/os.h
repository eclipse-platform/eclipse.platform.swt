/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others. All rights reserved.
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
  
#ifndef INC_os_H
#define INC_os_H

#define NDEBUG

/*
#define G_DISABLE_DEPRECATED
#define GTK_DISABLE_DEPRECATED
*/

#include <gtk/gtk.h>
#include <gdk/gdk.h>
#include <pango/pango.h>
#include <pango/pango-font.h>
#include <string.h>
#include <dlfcn.h>

#ifndef GDK_WINDOWING_X11
#define NO_XWindowChanges
#define NO_XDefaultScreen
#define NO_XReconfigureWMWindow
#define NO_XSetInputFocus
#define NO_gdk_1x11_1drawable_1get_1xdisplay
#define NO_gdk_1x11_1drawable_1get_1xid
#else
#include <gdk/gdkx.h>
#include <X11/extensions/XTest.h>
#endif

/*
* Defined this prototype to avoid warnings, 
* because it is a deprecated function in GTK.
*/
#ifdef GTK_DISABLE_DEPRECATED
void gtk_progress_bar_set_bar_style(void*, int);
#endif

#define NATIVE_ENTER(env,clazz,func)
#define NATIVE_EXIT(env,clazz,func)

#include "os_custom.h"

#endif /* INC_os_H */
