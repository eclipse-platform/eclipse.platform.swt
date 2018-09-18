/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others. All rights reserved.
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
#include <pango/pango.h>
#include <pango/pango-font.h>
#include <string.h>
#include <locale.h>
#include <unistd.h>

#ifndef _WIN32
#include <dlfcn.h>
#include <gtk/gtkunixprint.h>
#else
#include <windows.h>
//#define NO_realpath // TODO [win32] use GetFullPathName instead; 
#define NO_RTLD_1GLOBAL
#define NO_RTLD_1LAZY
#define NO_RTLD_1NOW
#define NO__1dlclose
#define NO__1dlopen
#define NO__1dlsym

#define NO__1gtk_1enumerate_1printers
#define NO__1gtk_1printer_1get_1name
#define NO__1gtk_1printer_1is_1default
#define NO__1gtk_1print_1job_1get_1surface
#define NO__1gtk_1print_1unix_1dialog_1get_1current_1page
#define NO__1gtk_1print_1unix_1dialog_1get_1selected_1printer
#define NO__1gtk_1print_1unix_1dialog_1get_1settings
#define NO__1gtk_1print_1unix_1dialog_1set_1settings
#define NO__1gtk_1print_1unix_1dialog_1get_1page_1setup
#define NO__1gtk_1print_1unix_1dialog_1set_1page_1setup
#define NO__1gtk_1printer_1get_1backend
#define NO__1gtk_1print_1unix_1dialog_1new
#define NO__1gtk_1print_1job_1new
#define NO__1gtk_1print_1job_1send
#define NO__1gtk_1print_1unix_1dialog_1set_1current_1page
#define NO__1gtk_1print_1unix_1dialog_1set_1embed_1page_1setup
#define NO__1gtk_1print_1unix_1dialog_1set_1manual_1capabilities

// map realpath to a similar function in win32
#define realpath(N,R) _fullpath((R),(N),_MAX_PATH)
#endif


#define OS_LOAD_FUNCTION LOAD_FUNCTION

// Hard-link code generated from GTK.java to LIB_GTK
#define GTK_LOAD_FUNCTION(var, name) LOAD_FUNCTION_LIB(var, LIB_GTK, name)
// Hard-link code generated from GTK.java to LIB_GDK
#define GDK_LOAD_FUNCTION(var, name) LOAD_FUNCTION_LIB(var, LIB_GDK, name)

#ifdef _WIN32
#define LOAD_FUNCTION_LIB(var, libname, name) \
		static int initialized = 0; \
		static FARPROC var = NULL; \
		if (!initialized) { \
			HMODULE hm = LoadLibrary(libname); \
			if (hm) var = GetProcAddress(hm, #name); \
			initialized = 1; \
		}
#else
#define LOAD_FUNCTION_LIB(var, libname, name) \
		static int initialized = 0; \
		static void *var = NULL; \
		if (!initialized) { \
			void* handle = dlopen(libname, LOAD_FLAGS); \
			if (handle) var = dlsym(handle, #name); \
			initialized = 1; \
	                CHECK_DLERROR \
		}
#endif


#ifdef GDK_WINDOWING_X11

#include <gdk/gdkx.h>
#include <gtk/gtkx.h>
#else

#define NO_GDK_1IS_1X11_1DISPLAY

/* X Structures */
#define NO_XAnyEvent
#define NO_XExposeEvent
#define NO_XEvent
#define NO_XFocusChangeEvent
#define NO_X_1EVENT_1TYPE
#define NO_X_1EVENT_1WINDOW

/* X functions */
#define NO__1XCheckIfEvent
#define NO__1XDefaultScreen
#define NO__1XDefaultRootWindow
#define NO__1XFree
#define NO__1XGetWindowProperty
#define NO__1XQueryPointer
#define NO__1XKeysymToKeycode
#define NO__1XSendEvent
#define NO__1XSetInputFocus
#define NO__1XSetErrorHandler
#define NO__1XSetIOErrorHandler
#define NO__1XSetTransientForHint
#define NO__1XSynchronize
X#define NO__1XWarpPointer
#define NO__1GDK_1PIXMAP_1XID
#define NO__1gdk_1x11_1display_1get_1xdisplay
#define NO__1gdk_1x11_1display_1utf8_1to_1compound_1text
#define NO__1gdk_1x11_1drawable_1get_1xdisplay
#define NO__1gdk_1x11_1drawable_1get_1xid
#define NO__1gdk_1x11_1get_1default_1xdisplay
#define NO__1gdk_1x11_1screen_1get_1window_1manager_1name
#define NO__1gdk_1x11_1screen_1lookup_1visual
#define NO__1gdk_1x11_1visual_1get_1xvisual
#define NO__1gdk_1x11_1window_1get_1xid
#define NO__1gdk_1x11_1window_1lookup_1for_1display
#define NO__1gdk_window_lookup
#define NO__1gdk_window_add_filter
#define NO__1GTK_1IS_1PLUG
#define NO__1gtk_1plug_1new
#define NO__1gtk_1socket_1get_1id
#define NO__1gtk_1socket_1new
#define NO_memmove__JLorg_eclipse_swt_internal_gtk_XExposeEvent_2J
#define NO_memmove__ILorg_eclipse_swt_internal_gtk_XExposeEvent_2I
#define NO_memmove__ILorg_eclipse_swt_internal_gtk_XFocusChangeEvent_2I
#define NO_memmove__Lorg_eclipse_swt_internal_gtk_XExposeEvent_2JJ
#define NO_memmove__Lorg_eclipse_swt_internal_gtk_XExposeEvent_2II
#define NO_memmove__Lorg_eclipse_swt_internal_gtk_XFocusChangeEvent_2JJ
#define NO_memmove__Lorg_eclipse_swt_internal_gtk_XFocusChangeEvent_2II

#endif

/* Disable access to sealed structs in GTK3 */
#define GdkRegion cairo_region_t
#define gdk_region_point_in cairo_region_contains_point
#define gdk_region_new cairo_region_create
#define gdk_region_offset cairo_region_translate
#define gdk_region_subtract cairo_region_subtract
#define gdk_region_union cairo_region_union
#define gdk_region_intersect cairo_region_intersect
#define gdk_region_destroy cairo_region_destroy
#define gdk_region_empty cairo_region_is_empty
#define gdk_region_get_clipbox cairo_region_get_extents
#define gdk_region_rectangle cairo_region_create_rectangle
#define gdk_region_union_with_rect cairo_region_union_rectangle
#define gdk_region_rect_in cairo_region_contains_rectangle

// TODO: the following are still called in code even on GTK3.
#define NO__1GDK_1PIXMAP_1XID
#define NO_GdkImage
#define NO_GTK_1ENTRY_1IM_1CONTEXT
#define NO_GTK_1TEXTVIEW_1IM_1CONTEXT

#include "os_custom.h"

#endif /* INC_os_H */
