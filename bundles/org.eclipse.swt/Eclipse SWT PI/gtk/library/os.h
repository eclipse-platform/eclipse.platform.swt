/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others. All rights reserved.
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

#define G_DISABLE_DEPRECATED
#define GTK_DISABLE_SINGLE_INCLUDES
/*
#define GTK_DISABLE_DEPRECATED
#define GDK_DISABLE_DEPRECATED
#define GSEAL_ENABLE
*/

#include <stdlib.h>
#include <gtk/gtk.h>
#include <gdk/gdk.h>
#if GTK_CHECK_VERSION(3,0,0)
#include <gtk/gtkx.h>
#include <gdk/gdkx.h>
#endif
#include <pango/pango.h>
#include <pango/pango-font.h>
#include <string.h>
#include <dlfcn.h>
#include <locale.h>
#include <unistd.h>

#define OS_LOAD_FUNCTION LOAD_FUNCTION

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

/* Disable access to sealed structs in GTK3 */
#if GTK_CHECK_VERSION(3,0,0)
#define NO_GdkDragContext
#define NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkDragContext_2II
#define NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkDragContext_2JJ
#define NO_GtkSelectionData
#define NO_memmove__Lorg_eclipse_swt_internal_gtk_GtkSelectionData_2II
#define NO_memmove__Lorg_eclipse_swt_internal_gtk_GtkSelectionData_2JJ
#define NO__1gtk_1style_1get_1bg_1gc
#define NO__1gtk_1style_1get_1black_1gc
#define NO__1gtk_1style_1get_1dark_1gc
#define NO__1gtk_1style_1get_1fg_1gc
#define NO__1gtk_1style_1get_1light_1gc
#define NO__1gtk_1style_1get_1mid_1gc
#define NO__1gtk_1style_1get_1text_1aa_1gc
#define NO__1gtk_1style_1get_1text_1gc
#define NO__1gtk_1style_1get_1white_1gc
#define NO__1gdk_1gc_1get_1values
#define NO__1gdk_1gc_1new
#define NO__1gdk_1draw_1rectangle
#define NO__1gdk_1gc_1set_1clip_1region
#define NO__1gdk_1gc_1set_1foreground
#define NO__1gdk_1gc_1set_1function
#define NO__1gdk_1gc_1set_1subwindow
#define NO__1gdk_1gc_1set_1values
#define NO__1gdk_1draw_1drawable
#define NO_GdkVisual
#define NO_GtkColorSelectionDialog
#define NO_GdkGCValues
#define NO_GtkAccessible
#endif

#include "os_custom.h"

#endif /* INC_os_H */
