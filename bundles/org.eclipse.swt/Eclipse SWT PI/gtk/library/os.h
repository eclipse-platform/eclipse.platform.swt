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

#define NO_gtk_1enumerate_1printers
#define NO_gtk_1printer_1get_1name
#define NO_gtk_1printer_1is_1default
#define NO_gtk_1print_1job_1get_1surface
#define NO_gtk_1print_1unix_1dialog_1get_1current_1page
#define NO_gtk_1print_1unix_1dialog_1get_1selected_1printer
#define NO_gtk_1print_1unix_1dialog_1get_1settings
#define NO_gtk_1print_1unix_1dialog_1set_1settings
#define NO_gtk_1print_1unix_1dialog_1get_1page_1setup
#define NO_gtk_1print_1unix_1dialog_1set_1page_1setup
#define NO_gtk_1printer_1get_1backend
#define NO_gtk_1print_1unix_1dialog_1new
#define NO_gtk_1print_1job_1new
#define NO_gtk_1print_1job_1send
#define NO_gtk_1print_1unix_1dialog_1set_1current_1page
#define NO_gtk_1print_1unix_1dialog_1set_1embed_1page_1setup
#define NO_gtk_1print_1unix_1dialog_1set_1manual_1capabilities

// map realpath to a similar function in win32
#define realpath(N,R) _fullpath((R),(N),_MAX_PATH)
#endif


#define OS_LOAD_FUNCTION LOAD_FUNCTION

// Hard-link code generated from GTK.java to LIB_GTK
#define GTK_LOAD_FUNCTION(var, name) LOAD_FUNCTION_LIB(var, LIB_GTK, name)
// Hard-link code generated from GDK.java to LIB_GDK
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


#if defined(GDK_WINDOWING_X11)
#if !GTK_CHECK_VERSION(3,96,0)
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
#if !GTK_CHECK_VERSION(3,96,0)
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

#if GTK_CHECK_VERSION(3,96,0)
#define GTK4 1

// Structs which do not exist on GTK4
#define NO_GtkTargetEntry
#define NO_GdkEventWindowState
#define NO_GdkWindowAttr
#define NO_GdkEvent
#define NO_GdkEventButton
#define NO_GdkEventCrossing
#define NO_GdkEventFocus
#define NO_GdkEventKey
#define NO_GdkEventMotion
#define NO_GdkEventScroll

// Memmoves for those structs which were removed in GTK4
#define NO_memmove__ILorg_eclipse_swt_internal_gtk_GtkTargetEntry_2I
#define NO_memmove__JLorg_eclipse_swt_internal_gtk_GtkTargetEntry_2J
#define NO_memmove__LLorg_eclipse_swt_internal_gtk_GtkTargetEntry_2II
#define NO_memmove__LLorg_eclipse_swt_internal_gtk_GtkTargetEntry_2JJ
#define NO_memmove__Iorg_eclipse_swt_internal_gtk_GdkEvent_2I
#define NO_memmove__Jorg_eclipse_swt_internal_gtk_GdkEvent_2J
#define NO_memmove__ILorg_eclipse_swt_internal_gtk_GdkEventButton_2I
#define NO_memmove__JLorg_eclipse_swt_internal_gtk_GdkEventButton_2J
#define NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventButton_2II
#define NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventButton_2JJ
#define NO_memmove__Iorg_eclipse_swt_internal_gtk_GdkEventCrossing_2I
#define NO_memmove__Jorg_eclipse_swt_internal_gtk_GdkEventCrossing_2J
#define NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventCrossing_2II
#define NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventCrossing_2JJ
#define NO_memmove__Iorg_eclipse_swt_internal_gtk_GdkEventFocus_2I
#define NO_memmove__Jorg_eclipse_swt_internal_gtk_GdkEventFocus_2J
#define NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventFocus_2II
#define NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventFocus_2JJ
#define NO_memmove__ILorg_eclipse_swt_internal_gtk_GdkEventKey_2I
#define NO_memmove__JLorg_eclipse_swt_internal_gtk_GdkEventKey_2J
#define NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventKey_2II
#define NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventKey_2JJ
#define NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventMotion_2II
#define NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventMotion_2JJ
#define NO_memmove__Iorg_eclipse_swt_internal_gtk_GdkEventWindowState_2I
#define NO_memmove__Jorg_eclipse_swt_internal_gtk_GdkEventWindowState_2J
#define NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventWindowState_2II
#define NO_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventWindowState_2JJ

// No GtkClipboard on GTK4
#define NO_gtk_1clipboard_1clear
#define NO_gtk_1clipboard_1get
#define NO_gtk_1clipboard_1set_1can_1store
#define NO_gtk_1clipboard_1set_1with_1owner
#define NO_gtk_1clipboard_1store
#define NO_gtk_1clipboard_1wait_1for_1contents

// No GtkStatusIcon on GTK4
#define NO_gtk_1status_1icon_1get_1geometry
#define NO_gtk_1status_1icon_1get_1visible
#define NO_gtk_1status_1icon_1new
#define NO_gtk_1status_1icon_1position_1menu_1func
#define NO_gtk_1status_1icon_1set_1from_1pixbuf
#define NO_gtk_1status_1icon_1set_1tooltip_1text
#define NO_gtk_1status_1icon_1set_1visible

// No GtkTargetList on GTK4
#define NO_gtk_1target_1list_1new
#define NO_gtk_1target_1list_1unref

// No GdkScreen on GTK4, including parameter casts
#define NO_gtk_1widget_1get_1screen
#define NO_gtk_1style_1context_1add_1provider_1for_1screen
#define NO_gdk_1device_1warp__IIII
#define NO_gdk_1device_1warp__JJII
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

// Miscellaneous functions removed from GTK4
#define NO_gtk_1misc_1set_1alignment
#define NO_gdk_1test_1simulate_1button
#define NO_gdk_1test_1simulate_1key
#define NO_gdk_1atom_1name
#define NO_gdk_1cairo_1create
#define NO_gdk_1threads_1leave
#define NO_gdk_1selection_1owner_1set
#define NO_gdk_1selection_1owner_1get
#define NO_gdk_1visual_1get_1depth
#define NO_gtk_1widget_1style_1get__I_3B_3II
#define NO_gtk_1widget_1style_1get__J_3B_3IJ
#define NO_gtk_1widget_1style_1get__I_3B_3JI
#define NO_gtk_1widget_1style_1get__J_3B_3JJ
#define NO_gtk_1css_1provider_1load_1from_1data__I_3BI_3I
#define NO_gtk_1css_1provider_1load_1from_1data__J_3BJ_3J
#define NO_gdk_1pixbuf_1get_1from_1window

// Some GtkContainer functions don't exist on GTK4
#define NO_gtk_1container_1propagate_1draw
#define NO_gtk_1container_1set_1border_1width
#define NO_gtk_1container_1get_1border_1width

/**
 * Some gtk_drag_* functions exist on both versions,
 * but with different signatures. Define them in both GTK4
 * and GTK3.
 */
#define NO_gtk_1drag_1begin_1with_1coordinates__IIIIIII
#define NO_gtk_1drag_1begin_1with_1coordinates__JJIIJII
#define NO_gtk_1drag_1get_1data__IIII
#define NO_gtk_1drag_1get_1data__JJJI

// Some gtk_drag_* functions were removed in GTK4
#define NO_gtk_1drag_1begin
#define NO_gtk_1drag_1dest_1set
#define NO_gtk_1drag_1dest_1find_1target
#define NO_gtk_1drag_1finish
#define NO_gtk_1drag_1set_1icon_1surface

// Some sizing functions are not available on GTK4
#define NO_gtk_1window_1set_1geometry_1hints

// No GdkWindow on GTK4, this includes parameter casting
#define NO_gtk_1widget_1set_1has_1window
#define NO_gtk_1widget_1get_1has_1window
#define NO_gtk_1im_1context_1set_1client_1window
#define NO_gtk_1widget_1set_1parent_1window
#define NO_gdk_1cairo_1set_1source_1window
#define NO_gdk_1device_1grab
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
#define NO_gdk_1window_1new
#define NO_gdk_1window_1process_1updates
#define NO_gdk_1window_1resize
#define NO_gdk_1window_1restack
#define NO_gdk_1window_1set_1cursor
#define NO_gdk_1window_1set_1decorations
#define NO_gdk_1window_1set_1functions
#define NO_gdk_1window_1set_1override_1redirect
#define NO_gdk_1window_1set_1user_1data
#define NO_gdk_1window_1show_1unraised
#define NO_gdk_1device_1get_1window_1at_1position
#define NO_gdk_1window_1set_1background_1pattern
#define NO_gdk_1event_1get_1window

// GdkDragContext removals
#define NO_gdk_1drag_1context_1get_1dest_1window
#define NO_gdk_1drag_1context_1list_1targets
#define NO_gdk_1drag_1status

// Some widgets have removed access to their GdkWindows in GTK4
#define NO_gtk_1text_1view_1get_1window
#define NO_gtk_1tree_1view_1get_1bin_1window

// Event related functions removed in GTK4
#define NO_gdk_1event_1free
#define NO_gdk_1event_1get
#define NO_gdk_1event_1peek
#define NO_gdk_1event_1put
#define NO_GDK_1EVENT_1TYPE
#define NO_GDK_1EVENT_1WINDOW

// GdkCursor removals
#define NO_gdk_1cursor_1new_1for_1display
#define NO_gdk_1cursor_1new_1from_1pixbuf
#define NO_gdk_1display_1supports_1cursor_1color
#define NO_gdk_1cursor_1new_1from_1name__JLjava_lang_String_2

#else

/**
 * Some gtk_drag_* functions exist on both versions,
 * but with different signatures. Define them in both GTK4
 * and GTK3.
 */
#define NO_gtk_1drag_1begin_1with_1coordinates__IIIIII
#define NO_gtk_1drag_1begin_1with_1coordinates__JJJIII
#define NO_gtk_1drag_1get_1data__III
#define NO_gtk_1drag_1get_1data__JJJ

// No GdkSurface on GTK3
#define NO_gtk_1widget_1set_1has_1surface
#define NO_gtk_1widget_1get_1has_1surface
#define NO_gtk_1widget_1set_1parent_1surface
#define NO_gdk_1surface_1create_1similar_1surface
#define NO_gdk_1surface_1destroy
#define NO_gdk_1surface_1focus
#define NO_gdk_1device_1get_1surface_1at_1position
#define NO_gdk_1surface_1lower
#define NO_gdk_1surface_1raise
#define NO_gdk_1surface_1get_1children
#define NO_gdk_1surface_1get_1user_1data
#define NO_gdk_1surface_1get_1origin
#define NO_gdk_1surface_1invalidate_1rect
#define NO_gdk_1surface_1get_1display
#define NO_gdk_1surface_1set_1cursor
#define NO_gdk_1surface_1new_1child
#define NO_gdk_1surface_1set_1user_1data
#define NO_gdk_1surface_1restack
#define NO_gdk_1surface_1show_1unraised
#define NO_gdk_1surface_1show
#define NO_gdk_1surface_1get_1device_1position
#define NO_gdk_1display_1get_1monitor_1at_1surface
#define NO_gdk_1surface_1get_1frame_1extents
#define NO_gdk_1surface_1get_1width
#define NO_gdk_1surface_1get_1height
#define NO_gdk_1surface_1get_1state
#define NO_gdk_1surface_1resize
#define NO_gdk_1surface_1move
#define NO_gdk_1surface_1move_1resize
#define NO_gdk_1surface_1hide
#define NO_gdk_1surface_1set_1decorations
#define NO_gdk_1surface_1set_1functions
#define NO_gdk_1surface_1get_1root_1origin
#define NO_gdk_1surface_1invalidate_1region
#define NO_gdk_1event_1get_1surface

// No GdkClipboard on GTK3
#define NO_gdk_1clipboard_1set_1content
#define NO_gdk_1display_1get_1clipboard
#define NO_gdk_1display_1get_1primary_1clipboard

// No GtkSnapshot on GTK3
#define NO_gtk_1snapshot_1append_1cairo
#define NO_gtk_1widget_1snapshot_1child

// No Graphene library on GTK3
#define NO_graphene_1rect_1alloc
#define NO_graphene_1rect_1free
#define NO_graphene_1rect_1init

// Miscellaneous functions not present on GTK3
#define NO_gdk_1device_1warp__III
#define NO_gdk_1device_1warp__JII
#define NO_gdk_1display_1get_1keymap
#define NO_gtk_1widget_1measure
#define NO_gtk_1style_1context_1add_1provider_1for_1display
#define NO_gtk_1widget_1get_1first_1child
#define NO_gtk_1widget_1get_1next_1sibling
#define NO_gtk_1css_1provider_1load_1from_1data__I_3BI
#define NO_gtk_1css_1provider_1load_1from_1data__J_3BJ
#define NO_gdk_1display_1is_1composited
#define NO_gtk_1gesture_1multi_1press_1new
#define NO_gtk_1style_1context_1get_1margin

// GdkCursor API changes from GTK3 -> GTK4
#define NO_gdk_1cursor_1new_1from_1name___3BI
#define NO_gdk_1cursor_1new_1from_1name___3BJ
#define NO_gdk_1cursor_1new_1from_1texture
#define NO_gtk_1widget_1set_1cursor

// No GdkTexture on GTK3
#define NO_gdk_1texture_1new_1for_1pixbuf

// GtkEventController related functions for GTK4
#define NO_gtk_1event_1controller_1key_1new
#define NO_gtk_1event_1controller_1motion_1new
#define NO_gtk_1event_1controller_1scroll_1new
#define NO_gtk_1widget_1add_1controller

// GdkEvent functions which do not exist on GTK3
#define NO_gdk_1event_1get_1focus_1in
#define NO_gdk_1event_1get_1string
#define NO_gdk_1event_1get_1key_1group
#define NO_gdk_1event_1get_1crossing_1mode

// GdkCursor API changes
#define NO_gdk_1cursor_1new_1from_1name__Ljava_lang_String_2J

#endif

#include "os_custom.h"

#endif /* INC_os_H */
