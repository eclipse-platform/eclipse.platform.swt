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

#include <dlfcn.h>
#include <gtk/gtkunixprint.h>

#define OS_LOAD_FUNCTION LOAD_FUNCTION

// Hard-link code generated from GTK.java to LIB_GTK
#define GTK_LOAD_FUNCTION(var, name) LOAD_FUNCTION_LIB(var, LIB_GTK, name)
// Hard-link code generated from GDK.java to LIB_GDK
#define GDK_LOAD_FUNCTION(var, name) LOAD_FUNCTION_LIB(var, LIB_GDK, name)

#define LOAD_FUNCTION_LIB(var, libname, name) \
		static int initialized = 0; \
		static void *var = NULL; \
		if (!initialized) { \
			void* handle = dlopen(libname, LOAD_FLAGS); \
			if (handle) var = dlsym(handle, #name); \
			initialized = 1; \
	                CHECK_DLERROR \
		}

#if defined(GDK_WINDOWING_X11)
#if !GTK_CHECK_VERSION(4,0,0)
#include <gtk/gtkx.h>
#define NO_gdk_1x11_1surface_1get_1xid
#define NO_gdk_1x11_1surface_1lookup_1for_1display
#else
#define NO_gdk_1x11_1get_1default_1xdisplay
#define NO_gdk_1x11_1window_1get_1xid
#define NO_gdk_1x11_1window_1lookup_1for_1display
#define NO_GTK_1IS_1PLUG
#define NO_gtk_1plug_1new
#define NO_gtk_1socket_1get_1id
#define NO_gtk_1socket_1new
#endif
#if !GTK_CHECK_VERSION(4,0,0)
#include <gdk/gdkx.h>
#else
#include <gdk/x11/gdkx.h>
#endif
#else

#define NO_GDK_1IS_1X11_1DISPLAY

/* X Structures */
#define NO_XExposeEvent
#define NO_XEvent
#define NO_XFocusChangeEvent
#define NO_X_1EVENT_1TYPE
#define NO_X_1EVENT_1WINDOW

/* X functions */
#define NO_XCheckIfEvent
#define NO_XDefaultScreen
#define NO_XDefaultRootWindow
#define NO_XFree
#define NO_XGetWindowProperty
#define NO_XQueryPointer
#define NO_XKeysymToKeycode
#define NO_XSendEvent
#define NO_XSetInputFocus
#define NO_XSetErrorHandler
#define NO_XSetIOErrorHandler
#define NO_XSetTransientForHint
#define NO_XSynchronize
#define NO_XWarpPointer
#define NO_GDK_1PIXMAP_1XID
#define NO_gdk_1x11_1display_1error_1trap_1pop_1ignored
#define NO_gdk_1x11_1display_1error_1trap_1push
#define NO_gdk_1x11_1display_1get_1xdisplay
#define NO_gdk_1x11_1display_1utf8_1to_1compound_1text
#define NO_gdk_1x11_1drawable_1get_1xdisplay
#define NO_gdk_1x11_1drawable_1get_1xid
#define NO_gdk_1x11_1get_1default_1xdisplay
#define NO_gdk_1x11_1screen_1get_1window_1manager_1name
#define NO_gdk_1x11_1screen_1lookup_1visual
#define NO_gdk_1x11_1visual_1get_1xvisual
#define NO_gdk_1x11_1window_1get_1xid
#define NO_gdk_1x11_1window_1lookup_1for_1display
#define NO_gdk_window_lookup
#define NO_gdk_window_add_filter
#define NO_GTK_1IS_1PLUG
#define NO_gtk_1plug_1new
#define NO_gtk_1socket_1get_1id
#define NO_gtk_1socket_1new
#define NO_memmove__JLorg_eclipse_swt_internal_gtk_XExposeEvent_2J
#define NO_memmove__ILorg_eclipse_swt_internal_gtk_XExposeEvent_2I
#define NO_memmove__ILorg_eclipse_swt_internal_gtk_XFocusChangeEvent_2I
#define NO_memmove__Lorg_eclipse_swt_internal_gtk_XExposeEvent_2JJ
#define NO_memmove__Lorg_eclipse_swt_internal_gtk_XExposeEvent_2II
#define NO_memmove__Lorg_eclipse_swt_internal_gtk_XFocusChangeEvent_2JJ
#define NO_memmove__Lorg_eclipse_swt_internal_gtk_XFocusChangeEvent_2II

#endif

#if GTK_CHECK_VERSION(4,0,0)
#define GTK4 1

// No GtkStatusIcon on GTK4
#define NO_gtk_1status_1icon_1position_1menu_1func

// No GdkScreen on GTK4, including parameter casts
#define NO_gdk_1screen_1get_1default
#define NO_gdk_1screen_1get_1monitor_1at_1point
#define NO_gdk_1screen_1get_1monitor_1at_1window
#define NO_gdk_1screen_1get_1monitor_1geometry
#define NO_gdk_1screen_1get_1monitor_1scale_1factor
#define NO_gdk_1screen_1get_1monitor_1workarea
#define NO_gdk_1screen_1get_1n_1monitors
#define NO_gdk_1screen_1get_1primary_1monitor
#define NO_gdk_1screen_1get_1resolution
#define NO_gdk_1screen_1get_1system_1visual
#define NO_gdk_1screen_1get_1window_1stack
#define NO_gdk_1screen_1is_1composited

// No GDK lock
#define NO_gdk_1threads_1add_1idle
#define NO_gdk_1threads_1add_1timeout
#define NO_gdk_1threads_1enter
#define NO_gdk_1threads_1init
#define NO_gdk_1threads_1leave

// Miscellaneous functions removed from GTK4
#define NO_gtk_1misc_1set_1alignment
#define NO_gdk_1test_1simulate_1button
#define NO_gdk_1test_1simulate_1key
#define NO_gdk_1atom_1name
#define NO_gdk_1cairo_1create
#define NO_gdk_1selection_1owner_1set
#define NO_gdk_1selection_1owner_1get
#define NO_gdk_1visual_1get_1depth
#define NO_gdk_1pixbuf_1get_1from_1window

// No GdkWindow on GTK4, this includes parameter casting
#define NO_gtk_1im_1context_1set_1client_1window
#define NO_gdk_1cairo_1set_1source_1window
#define NO_gdk_1device_1grab
#define NO_gdk_1device_1warp
#define NO_gdk_1display_1get_1monitor_1at_1window
#define NO_gdk_1get_1default_1root_1window
#define NO_gdk_1property_1get
#define NO_gdk_1window_1begin_1draw_1frame
#define NO_gdk_1window_1create_1similar_1surface
#define NO_gdk_1window_1destroy
#define NO_gdk_1window_1end_1draw_1frame
#define NO_gdk_1window_1focus
#define NO_gdk_1window_1get_1children
#define NO_gdk_1window_1get_1device_1position
#define NO_gdk_1window_1get_1display
#define NO_gdk_1window_1set_1events
#define NO_gdk_1window_1get_1events
#define NO_gdk_1window_1get_1frame_1extents
#define NO_gdk_1window_1get_1height
#define NO_gdk_1window_1get_1width
#define NO_gdk_1window_1get_1origin
#define NO_gdk_1window_1get_1parent
#define NO_gdk_1window_1get_1root_1origin
#define NO_gdk_1window_1get_1state
#define NO_gdk_1window_1get_1user_1data
#define NO_gdk_1window_1get_1visible_1region
#define NO_gdk_1window_1show
#define NO_gdk_1window_1hide
#define NO_gdk_1window_1invalidate_1rect
#define NO_gdk_1window_1invalidate_1region
#define NO_gdk_1window_1raise
#define NO_gdk_1window_1lower
#define NO_gdk_1window_1move
#define NO_gdk_1window_1move_1resize
#define NO_gdk_1window_1process_1updates
#define NO_gdk_1window_1resize
#define NO_gdk_1window_1restack
#define NO_gdk_1window_1set_1cursor
#define NO_gdk_1window_1set_1decorations
#define NO_gdk_1window_1set_1functions
#define NO_gdk_1window_1set_1override_1redirect
#define NO_gdk_1window_1set_1user_1data
#define NO_gdk_1window_1show_1unraised
#define NO_gdk_1window_1set_1background_1pattern
#define NO_gdk_1event_1get_1window

// GdkDragContext removals
#define NO_gdk_1drag_1context_1get_1dest_1window
#define NO_gdk_1drag_1context_1list_1targets
#define NO_gdk_1drag_1status

// Event related functions removed in GTK4
#define NO_gdk_1event_1copy
#define NO_gdk_1event_1free
#define NO_gdk_1event_1get
#define NO_gdk_1event_1peek
#define NO_gdk_1event_1put
#define NO_gdk_1event_1get_1button
#define NO_gdk_1event_1get_1coords
#define NO_gdk_1event_1get_1root_1coords
#define NO_gdk_1event_1get_1keycode
#define NO_gdk_1event_1new
#define NO_gdk_1event_1set_1device
#define NO_gdk_1event_1get_1keyval
#define NO_gdk_1event_1get_1scroll_1deltas
#define NO_gdk_1event_1get_1scroll_1direction
#define NO_gdk_1event_1get_1state
#define NO_GDK_1EVENT_1TYPE
#define NO_GDK_1EVENT_1WINDOW

// GdkCursor removals
#define NO_gdk_1cursor_1new_1for_1display
#define NO_gdk_1cursor_1new_1from_1pixbuf
#define NO_gdk_1cursor_1new_1from_1name__JLjava_lang_String_2

// GdkKeymap removals
#define NO_gdk_1keymap_1translate_1keyboard_1state
#define NO_gdk_1keymap_1get_1entries_1for_1keyval

// GdkAtom removed
#define NO_gtk_1drag_1get_1data__JJJ
#define NO_gdk_1text_1property_1to_1utf8_1list_1for_1display
#define NO_gdk_1x11_1display_1utf8_1to_1compound_1text

// Accessibility interface changes
#define NO_GTK_1TYPE_1TEXT_1VIEW_1ACCESSIBLE
#define NO_swt_1fixed_1accessible_1register_1accessible

// GtkCellRenderer changes
#define NO_GtkCellRendererPixbufClass_1sizeof
#define NO_GtkCellRendererPixbuf_1sizeof
#define NO_GtkCellRendererToggleClass_1sizeof
#define NO_GtkCellRendererToggle_1sizeof

// GTK3 only macros
#define NO_GTK_1IS_1ACCEL_1LABEL
#define NO_GTK_1IS_1CONTAINER

#else

// No GdkSurface on GTK3
#define NO_gdk_1surface_1create_1similar_1surface
#define NO_gdk_1surface_1destroy
#define NO_gdk_1surface_1get_1origin
#define NO_gdk_1surface_1invalidate_1rect
#define NO_gdk_1surface_1get_1display
#define NO_gdk_1surface_1set_1cursor
#define NO_gdk_1surface_1new_1popup
#define NO_gdk_1surface_1get_1device_1position
#define NO_gdk_1display_1get_1monitor_1at_1surface
#define NO_gdk_1surface_1get_1width
#define NO_gdk_1surface_1get_1height
#define NO_gdk_1surface_1hide
#define NO_gdk_1surface_1get_1root_1origin
#define NO_gdk_1surface_1invalidate_1region
#define NO_gdk_1event_1get_1surface
#define NO_gdk_1surface_1set_1input_1region
#define NO_gdk_1surface_1set_1opaque_1region

// No GdkToplevel on GTK3
#define NO_gdk_1toplevel_1present

// No GdkClipboard on GTK3
#define NO_gdk_1clipboard_1set_1content

// No Graphene library on GTK3
#define NO_graphene_1rect_1alloc
#define NO_graphene_1rect_1free
#define NO_graphene_1rect_1init

// GdkCursor API changes from GTK3 -> GTK4
#define NO_gdk_1cursor_1new_1from_1name___3BI
#define NO_gdk_1cursor_1new_1from_1name___3BJ
#define NO_gdk_1cursor_1new_1from_1texture

// No GdkTexture on GTK3
#define NO_gdk_1texture_1new_1for_1pixbuf
#define NO_gdk_1texture_1new_1from_1file
#define NO_gdk_1pixbuf_1get_1from_1texture
#define NO_gdk_1texture_1new_1from_1file__J_3J
#define NO_gdk_1texture_1new_1from_1file___3B_3J

// GdkEvent functions which do not exist on GTK3
#define NO_gdk_1focus_1event_1get_1in
#define NO_gdk_1crossing_1event_1get_1mode
#define NO_gdk_1button_1event_1get_1button
#define NO_gdk_1event_1get_1position
#define NO_gdk_1key_1event_1get_1keyval
#define NO_gdk_1key_1event_1get_1keycode
#define NO_gdk_1scroll_1event_1get_1deltas
#define NO_gdk_1scroll_1event_1get_1direction
#define NO_gdk_1event_1get_1modifier_1state
#define NO_gdk_1key_1event_1get_1layout
#define NO_gdk_1event_1ref
#define NO_gdk_1event_1unref

// GdkCursor API changes
#define NO_gdk_1cursor_1new_1from_1name__Ljava_lang_String_2J

// No GdkPopup on GTK3
#define NO_gdk_1popup_1get_1parent
#define NO_gdk_1popup_1layout_1new
#define NO_gdk_1popup_1present

// SWTFixed changes
#define NO_swt_1fixed_1add
#define NO_swt_1fixed_1remove

#endif

#include "os_custom.h"

#endif /* INC_os_H */
