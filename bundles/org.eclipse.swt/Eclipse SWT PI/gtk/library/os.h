/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others. All rights reserved.
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

#include <stdlib.h>
#include <gtk/gtk.h>
#include <gdk/gdk.h>
#include <pango/pango.h>
#include <pango/pango-font.h>
#include <string.h>
#include <dlfcn.h>
#include <locale.h>

#ifndef GDK_WINDOWING_X11

/* X Structures */
#define NO_XClientMessageEvent
#define NO_XCrossingEvent
#define NO_XExposeEvent
#define NO_XFocusChangeEvent
#define NO_XVisibilityEvent
#define NO_XWindowChanges

/* X functions */
#define NO__1XCheckMaskEvent
#define NO__1XCheckWindowEvent
#define NO__1XCheckIfEvent
#define NO__1XDefaultScreen
#define NO__1XDefaultRootWindow
#define NO__1XFlush
#define NO__1XGetSelectionOwner
#define NO__1XQueryTree
#define NO__1XQueryPointer
#define NO__1XKeysymToKeycode
#define NO__1XReconfigureWMWindow
#define NO__1XSendEvent
#define NO__1XSetInputFocus
#define NO__1XSynchronize
#define NO__1XSetErrorHandler
#define NO__1XSetIOErrorHandler
#define NO__1XSetTransientForHint
#define NO__1XTestFakeButtonEvent
#define NO__1XTestFakeKeyEvent
#define NO__1XTestFakeMotionEvent
#define NO__1XWarpPointer
#define NO__1gdk_x11_atom_to_xatom
#define NO__1gdk_1x11_1drawable_1get_1xdisplay
#define NO__1gdk_1x11_1drawable_1get_1xid
#define NO__1gdk_window_lookup
#define NO__1gdk_window_add_filter
#define NO_memmove__ILorg_eclipse_swt_internal_gtk_XClientMessageEvent_2I
#define NO_memmove__ILorg_eclipse_swt_internal_gtk_XCrossingEvent_2I
#define NO_memmove__ILorg_eclipse_swt_internal_gtk_XExposeEvent_2I
#define NO_memmove__ILorg_eclipse_swt_internal_gtk_XFocusChangeEvent_2I
#define NO_memmove__Lorg_eclipse_swt_internal_gtk_XCrossingEvent_2II
#define NO_memmove__Lorg_eclipse_swt_internal_gtk_XExposeEvent_2II
#define NO_memmove__Lorg_eclipse_swt_internal_gtk_XFocusChangeEvent_2II
#define NO_memmove__Lorg_eclipse_swt_internal_gtk_XVisibilityEvent_2II

#else
#include <gdk/gdkx.h>
#include <X11/extensions/XTest.h>
#include <X11/extensions/Xrender.h>
#endif

#include "os_custom.h"

#endif /* INC_os_H */
