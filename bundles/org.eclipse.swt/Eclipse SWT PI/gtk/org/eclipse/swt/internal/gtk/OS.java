/*******************************************************************************
 * Copyright (c) 2000, 2022 IBM Corporation and others. All rights reserved.
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
package org.eclipse.swt.internal.gtk;

import java.util.*;

import org.eclipse.swt.internal.*;

// Common type translation table:
// C            ->  Java
// --------------------
// Primitives:
// int          -> int
// gint*        -> int[]
//
// Unsigned integer:
// * Note that java's int is signed, which introduces difficulties
// * for values > 0x7FFFFFFF. Java's long can fit such values, but
// * java's long is 8 bytes, while guint is 4 bytes. For that reason,
// * java's long CAN'T be used for pointers or arrays.
// guint        -> int/long
// guint*       -> int[]
//
// Boolean:
// * Java's boolean is handy, but it's 1 byte, while gboolean is 4
// * bytes. For that reason, it CAN'T be used for pointers or arrays.
// gboolean     -> int/boolean
// gboolean*    -> int
//
// Pointers:
// gpointer     -> long
// void *       -> long
//
// Strings:
// gchar *      -> long    // You're responsible for allocating/deallocating memory buffer.
// const char * -> byte[]  // Example: setenv()
// const gchar* -> byte[]  // Example: g_log_remove_handler()
//
// Special types:
// GQuark       -> int
// GError **    -> long[]  // Example: g_filename_to_uri()


/**
 * This class contains native functions for various libraries.
 *
 * Any dynamic functions must be manually linked to their corresponding library. See os_cutom.h  #define FUNC_LIB_* LIB_*
 */
public class OS extends C {
	/** OS Constants */
	public static final boolean IsLinux, IsWin32, BIG_ENDIAN;
	static {

		/* Initialize the OS flags and locale constants */
		String osName = System.getProperty ("os.name");
		boolean isLinux = false, isWin32 = false;
		if (osName.equals ("Linux")) isLinux = true;
		if (osName.startsWith("Windows")) isWin32 = true;
		IsLinux = isLinux;  IsWin32 = isWin32;

		byte[] buffer = new byte[4];
		long ptr = C.malloc(4);
		C.memmove(ptr, new int[]{1}, 4);
		C.memmove(buffer, ptr, 1);
		C.free(ptr);
		BIG_ENDIAN = buffer[0] == 0;
	}

	/** Initialization; load native libraries */
	static {
		String propertyName = "SWT_GTK4";
		String gtk4 = getEnvironmentalVariable(propertyName);
		if (gtk4 != null && gtk4.equals("1")) {
			try {
				Library.loadLibrary("swt-pi4");
			} catch (Throwable e) {
				System.err.println("SWT OS.java Error: Failed to load swt-pi4, loading swt-pi3 as fallback.");
				Library.loadLibrary("swt-pi3");
			}
		} else {
			try {
				Library.loadLibrary("swt-pi3");
			} catch (Throwable e) {
				System.err.println("SWT OS.java Error: Failed to load swt-pi3, loading swt-pi4 as fallback.");
				Library.loadLibrary("swt-pi4");
			}
		}
	}

	//Add ability to debug gtk warnings for SWT snippets via SWT_FATAL_WARNINGS=1
	// env variable. Please see Eclipse bug 471477
	static {
		String propertyName = "SWT_FATAL_WARNINGS";
		String swt_fatal_warnings = getEnvironmentalVariable (propertyName);

		if (swt_fatal_warnings != null && swt_fatal_warnings.equals("1")) {
			String gtk4PropertyName = "SWT_GTK4";
			String gtk4 = getEnvironmentalVariable (gtk4PropertyName);
			if (gtk4 != null && gtk4.equals("1")) {
				System.err.println("SWT warning: SWT_FATAL_WARNINGS only available on GTK3.");
			} else {
				OS.swt_debug_on_fatal_warnings ();
			}
		}
	}

	// Bug 519124
	static {
		String swt_lib_versions = getEnvironmentalVariable (OS.SWT_LIB_VERSIONS); // Note, this is read in multiple places.
		if (swt_lib_versions != null && swt_lib_versions.equals("1")) {
			System.out.print("SWT_LIB_Gtk:"+GTK.gtk_get_major_version()+"."+GTK.gtk_get_minor_version()+"."+GTK.gtk_get_micro_version());
			System.out.print(" (Dynamic gdbus)");
			System.out.println("");
		}
	}

	public static final String SWT_LIB_VERSIONS = "SWT_LIB_VERSIONS";

	public static String getEnvironmentalVariable (String envVarName) {
		String envVarValue = null;
		long ptr = C.getenv(ascii(envVarName));
		if (ptr != 0) {
			int length = C.strlen(ptr);
			byte[] buffer = new byte[length];
			C.memmove(buffer, ptr, length);
			char[] convertedChar = new char[buffer.length];
			for (int i = 0; i < buffer.length; i++) {
				convertedChar[i]=(char)buffer[i];
			}
			envVarValue = new String(convertedChar);
		}
		return envVarValue;
	}

	/** Constants */
	public static final int G_FILE_ERROR_IO = 21;
	public static final int G_FILE_TEST_IS_DIR = 1 << 2;
	public static final int G_FILE_TEST_IS_EXECUTABLE = 1 << 3;
	public static final int G_SIGNAL_MATCH_DATA = 1 << 4;
	public static final int G_SIGNAL_MATCH_ID = 1 << 0;
	public static final int G_LOG_FLAG_FATAL = 0x2;
	public static final int G_LOG_FLAG_RECURSION = 0x1;
	public static final int G_LOG_LEVEL_MASK = 0xfffffffc;
	public static final int G_APP_INFO_CREATE_NONE = 0;
	public static final int G_APP_INFO_CREATE_SUPPORTS_URIS  = (1 << 1);
	public static final int GTK_TYPE_TEXT_BUFFER = 21;
	public static final int PANGO_ALIGN_LEFT = 0;
	public static final int PANGO_ALIGN_CENTER = 1;
	public static final int PANGO_ALIGN_RIGHT = 2;
	public static final int PANGO_ATTR_FOREGROUND = 9;
	public static final int PANGO_ATTR_BACKGROUND = 10;
	public static final int PANGO_ATTR_UNDERLINE = 11;
	public static final int PANGO_ATTR_UNDERLINE_COLOR = 18;
	public static final int PANGO_DIRECTION_LTR = 0;
	public static final int PANGO_DIRECTION_RTL = 1;
	public static final int PANGO_SCALE = 1024;
	public static final int PANGO_STRETCH_ULTRA_CONDENSED = 0x0;
	public static final int PANGO_STRETCH_EXTRA_CONDENSED = 0x1;
	public static final int PANGO_STRETCH_CONDENSED = 0x2;
	public static final int PANGO_STRETCH_SEMI_CONDENSED = 0x3;
	public static final int PANGO_STRETCH_NORMAL = 0x4;
	public static final int PANGO_STRETCH_SEMI_EXPANDED = 0x5;
	public static final int PANGO_STRETCH_EXPANDED = 0x6;
	public static final int PANGO_STRETCH_EXTRA_EXPANDED = 0x7;
	public static final int PANGO_STRETCH_ULTRA_EXPANDED = 0x8;
	public static final int PANGO_STYLE_ITALIC = 0x2;
	public static final int PANGO_STYLE_NORMAL = 0x0;
	public static final int PANGO_STYLE_OBLIQUE = 0x1;
	public static final int PANGO_TAB_LEFT = 0;
	public static final int PANGO_UNDERLINE_NONE = 0;
	public static final int PANGO_UNDERLINE_SINGLE = 1;
	public static final int PANGO_UNDERLINE_DOUBLE = 2;
	public static final int PANGO_UNDERLINE_LOW = 3;
	public static final int PANGO_UNDERLINE_ERROR = 4;
	public static final int PANGO_VARIANT_NORMAL = 0;
	public static final int PANGO_VARIANT_SMALL_CAPS = 1;
	public static final int PANGO_WEIGHT_BOLD = 0x2bc;
	public static final int PANGO_WEIGHT_NORMAL = 0x190;
	public static final int PANGO_WRAP_WORD_CHAR = 2;
	public static final int PANGO_FONT_MASK_FAMILY = 1 << 0;
	public static final int PANGO_FONT_MASK_STYLE = 1 << 1;
	public static final int PANGO_FONT_MASK_WEIGHT = 1 << 3;
	public static final int PANGO_FONT_MASK_SIZE = 1 << 5;

	/**
	 * GDBus Session types.
	 * @category gdbus */
	public static final int G_BUS_TYPE_SESSION = 2; //The login session message bus.
	/** @category gdbus */
	public static final int G_BUS_NAME_OWNER_FLAGS_ALLOW_REPLACEMENT = (1<<0); //Allow another message bus connection to claim the name.
	/**
	 * If another message bus connection owns the name and have
	 * specified #G_BUS_NAME_OWNER_FLAGS_ALLOW_REPLACEMENT, then take the name from the other connection.
	 * @category gdbus */
	public static final int G_BUS_NAME_OWNER_FLAGS_REPLACE = (1<<1);

	// Proxy flags found here: https://developer.gnome.org/gio/stable/GDBusProxy.html#GDBusProxyFlags
	public static final int G_DBUS_PROXY_FLAGS_DO_NOT_LOAD_PROPERTIES = 1;
	public static final int G_DBUS_PROXY_FLAGS_DO_NOT_CONNECT_SIGNALS = 2;
	public static final int G_DBUS_PROXY_FLAGS_DO_NOT_AUTO_START = 3;

	public static final int G_DBUS_CALL_FLAGS_NONE = 0;

	/**
	 * DBus Data types as defined by:
	 * https://dbus.freedesktop.org/doc/dbus-specification.html#idm423
	 * If using these, make sure they're properly handled in all GDBus code. Only some of these are supported by some GDBus classes.
	 * @category gdbus */
	public static final String DBUS_TYPE_BYTE = "y"; // 8 bit, unsigned int.
	/** @category gdbus */
	public static final String DBUS_TYPE_BOOLEAN = "b";
	/** @category gdbus */
	public static final String DBUS_TYPE_ARRAY = "a";
	/** @category gdbus */
	public static final String DBUS_TYPE_STRING = "s";
	/** @category gdbus */
	public static final String DBUS_TYPE_STRING_ARRAY = "as";
	/** @category gdbus */
	public static final String DBUS_TYPE_STRUCT_ARRAY_BROWSER_FUNCS = "a(tss)";
	/** @category gdbus */
	public static final String DBUS_TYPE_INT32 = "i";
	/** @category gdbus */
	public static final String DBUS_TYPE_UINT64 = "t";
	/** @category gdbus */
	public static final String DBUS_TYPE_DOUBLE = "d";
	/** @category gdbus */
	public static final String DBUS_TYPE_STRUCT = "r"; // Not used by Dbus, but implemented by GDBus.
	/** @category gdbus */
	public static final String DBUS_TYPE_SINGLE_COMPLETE = "*";

	/**
	 * GVariant Types
	 * These are for the most part quite similar to DBus types with a few differences. Read:
	 * https://developer.gnome.org/glib/stable/glib-GVariantType.html
	 *
	 * @category gdbus
	 */
	public static final byte[] G_VARIANT_TYPE_BYTE = ascii(DBUS_TYPE_BYTE);
	/** @category gdbus */
	public static final byte[] G_VARIANT_TYPE_BOOLEAN = ascii(DBUS_TYPE_BOOLEAN);
	/** @category gdbus */
	public static final byte[] G_VARIANT_TYPE_STRING_ARRAY = ascii(DBUS_TYPE_STRING_ARRAY);
	/** @category gdbus */
	public static final byte[] G_VARIANT_TYPE_STRING = ascii(DBUS_TYPE_STRING);
	/** @category gdbus */
	public static final byte[] G_VARIANT_TYPE_IN32 = ascii(DBUS_TYPE_INT32);
	/** @category gdbus */
	public static final byte[] G_VARIANT_TYPE_UINT64 = ascii(DBUS_TYPE_UINT64);
	/** @category gdbus */
	public static final byte[] G_VARIANT_TYPE_DOUBLE = ascii(DBUS_TYPE_DOUBLE);
	/** @category gdbus */
	public static final byte[] G_VARIANT_TYPE_TUPLE = ascii(DBUS_TYPE_STRUCT);
	/** @category gdbus */
	public static final byte[] G_VARIANT_TYPE_ARRAY_BROWSER_FUNCS = ascii(DBUS_TYPE_STRUCT_ARRAY_BROWSER_FUNCS);


	/** Signals */
	public static final byte[] accel_closures_changed = ascii("accel-closures-changed");		// Gtk3,4
	public static final byte[] activate = ascii("activate");	// ?
	public static final byte[] angle_changed = ascii("angle-changed");	// Gtk3/4, Guesture related.
	public static final byte[] backspace = ascii("backspace");
	public static final byte[] begin = ascii("begin");
	public static final byte[] button_press_event = ascii("button-press-event");
	public static final byte[] button_release_event = ascii("button-release-event");
	public static final byte[] changed = ascii("changed");
	public static final byte[] change_value = ascii("change-value");
	public static final byte[] clicked = ascii("clicked");
	public static final byte[] close_request = ascii("close-request");
	public static final byte[] commit = ascii("commit");
	public static final byte[] configure_event = ascii("configure-event");
	public static final byte[] copy_clipboard = ascii("copy-clipboard");
	public static final byte[] cut_clipboard = ascii("cut-clipboard");
	public static final byte[] create_menu_proxy = ascii("create-menu-proxy");
	public static final byte[] delete_event = ascii("delete-event");
	public static final byte[] delete_from_cursor = ascii("delete-from-cursor");
	public static final byte[] day_selected = ascii("day-selected");
	public static final byte[] day_selected_double_click = ascii("day-selected-double-click");
	public static final byte[] delete_range = ascii("delete-range");
	public static final byte[] delete_text = ascii("delete-text");
	public static final byte[] direction_changed = ascii("direction-changed");
	public static final byte[] dpi_changed = ascii("notify::scale-factor");
	public static final byte[] drag_begin = ascii("drag-begin");
	public static final byte[] drag_data_delete = ascii("drag-data-delete");
	public static final byte[] drag_data_get = ascii("drag-data-get");
	public static final byte[] drag_data_received = ascii("drag-data-received");
	public static final byte[] drag_drop = ascii("drag-drop");
	public static final byte[] drag_end = ascii("drag-end");
	public static final byte[] drag_leave = ascii("drag-leave");
	public static final byte[] drag_motion = ascii("drag-motion");
	public static final byte[] prepare = ascii("prepare");
	public static final byte[] draw = ascii("draw");
	public static final byte[] end = ascii("end");
	public static final byte[] enter_notify_event = ascii("enter-notify-event");
	public static final byte[] enter = ascii("enter");
	public static final byte[] event = ascii("event");
	public static final byte[] event_after = ascii("event-after");
	public static final byte[] expand_collapse_cursor_row = ascii("expand-collapse-cursor-row");
	public static final byte[] focus = ascii("focus");
	public static final byte[] focus_in_event = ascii("focus-in-event");
	public static final byte[] focus_in = ascii("focus-in");
	public static final byte[] focus_out_event = ascii("focus-out-event");
	public static final byte[] focus_out = ascii("focus-out");
	public static final byte[] grab_focus = ascii("grab-focus");
	public static final byte[] hide = ascii("hide");
	public static final byte[] icon_release = ascii("icon-release");
	public static final byte[] insert_text = ascii("insert-text");
	public static final byte[] key_press_event = ascii("key-press-event");
	public static final byte[] key_release_event = ascii("key-release-event");
	public static final byte[] key_pressed = ascii("key-pressed");
	public static final byte[] key_released = ascii("key-released");
	public static final byte[] keys_changed = ascii("keys-changed");
	public static final byte[] leave_notify_event = ascii("leave-notify-event");
	public static final byte[] leave = ascii("leave");
	public static final byte[] map = ascii("map");
	public static final byte[] map_event = ascii("map-event");
	public static final byte[] mnemonic_activate = ascii("mnemonic-activate");
	public static final byte[] month_changed = ascii("month-changed");
	public static final byte[] next_month = ascii("next-month");
	public static final byte[] prev_month = ascii("prev-month");
	public static final byte[] next_year = ascii("next-year");
	public static final byte[] prev_year = ascii("prev-year");
	public static final byte[] motion_notify_event = ascii("motion-notify-event");
	public static final byte[] motion = ascii("motion");
	public static final byte[] move_cursor = ascii("move-cursor");
	public static final byte[] move_focus = ascii("move-focus");
	public static final byte[] output = ascii("output");
	public static final byte[] paste_clipboard = ascii("paste-clipboard");
	public static final byte[] pressed = ascii("pressed");
	public static final byte[] released = ascii("released");
	public static final byte[] popped_up = ascii("popped-up");
	public static final byte[] popup_menu = ascii("popup-menu");
	public static final byte[] populate_popup = ascii("populate-popup");
	public static final byte[] preedit_changed = ascii("preedit-changed");
	public static final byte[] realize = ascii("realize");
	public static final byte[] row_activated = ascii("row-activated");
	public static final byte[] row_changed = ascii("row-changed");
	public static final byte[] row_has_child_toggled = ascii("row-has-child-toggled");
	public static final byte[] scale_changed = ascii("scale-changed");
	public static final byte[] scroll_child = ascii("scroll-child");
	public static final byte[] scroll_event = ascii("scroll-event");
	public static final byte[] scroll = ascii("scroll");
	public static final byte[] select = ascii("select");
	public static final byte[] selection_done = ascii("selection-done");
	public static final byte[] show = ascii("show");
	public static final byte[] show_help = ascii("show-help");
	public static final byte[] size_allocate = ascii("size-allocate");
	public static final byte[] resize = ascii("resize");
	public static final byte[] start_interactive_search = ascii("start-interactive-search");
	public static final byte[] style_updated = ascii("style-updated");
	public static final byte[] switch_page = ascii("switch-page");
	public static final byte[] test_collapse_row = ascii("test-collapse-row");
	public static final byte[] test_expand_row = ascii("test-expand-row");
	public static final byte[] toggled = ascii("toggled");
	public static final byte[] unmap = ascii("unmap");
	public static final byte[] unmap_event = ascii("unmap-event");
	public static final byte[] value_changed = ascii("value-changed");
	public static final byte[] window_state_event = ascii("window-state-event");
	public static final byte[] notify_state = ascii("notify::state");
	public static final byte[] notify_default_height = ascii("notify::default-height");
	public static final byte[] notify_default_width = ascii("notify::default-width");
	public static final byte[] notify_maximized = ascii("notify::maximized");
	public static final byte[] notify_is_active = ascii("notify::is-active");
	public static final byte[] notify_theme_change = ascii("notify::gtk-application-prefer-dark-theme");
	public static final byte[] response = ascii("response");
	public static final byte[] compute_size = ascii("compute-size");

	/** Properties */
	public static final byte[] active = ascii("active");
	public static final byte[] background_rgba = ascii("background-rgba");
	public static final byte[] cell_background_rgba = ascii("cell-background-rgba");
	public static final byte[] default_border = ascii("default-border");
	public static final byte[] expander_size = ascii("expander-size");
	public static final byte[] fixed_height_mode = ascii("fixed-height-mode");
	public static final byte[] focus_line_width = ascii("focus-line-width");
	public static final byte[] focus_padding = ascii("focus-padding");
	public static final byte[] font_desc = ascii("font-desc");
	public static final byte[] foreground_rgba = ascii("foreground-rgba");
	public static final byte[] grid_line_width = ascii("grid-line-width");
	public static final byte[] inner_border = ascii("inner-border");
	public static final byte[] has_backward_stepper = ascii("has-backward-stepper");
	public static final byte[] has_secondary_backward_stepper = ascii("has-secondary-backward-stepper");
	public static final byte[] has_forward_stepper = ascii("has-forward-stepper");
	public static final byte[] has_secondary_forward_stepper = ascii("has-secondary-forward-stepper");
	public static final byte[] horizontal_separator = ascii("horizontal-separator");
	public static final byte[] inconsistent = ascii("inconsistent");
	public static final byte[] indicator_size = ascii("indicator-size");
	public static final byte[] indicator_spacing = ascii("indicator-spacing");
	public static final byte[] interior_focus = ascii("interior-focus");
	public static final byte[] margin = ascii("margin");
	public static final byte[] mode = ascii("mode");
	public static final byte[] model = ascii("model");
	public static final byte[] spacing = ascii("spacing");
	public static final byte[] pixbuf = ascii("pixbuf");
	public static final byte[] gicon = ascii("gicon");
	public static final byte[] text = ascii("text");
	public static final byte[] xalign = ascii("xalign");
	public static final byte[] ypad = ascii("ypad");
	public static final byte[] margin_bottom = ascii("margin-bottom");
	public static final byte[] margin_top = ascii("margin-top");
	public static final byte[] scrollbar_spacing = ascii("scrollbar-spacing");

	/** Actions */
	public static final byte[] action_copy_clipboard = ascii("clipboard.copy");
	public static final byte[] action_cut_clipboard = ascii("clipboard.cut");
	public static final byte[] action_paste_clipboard = ascii("clipboard.paste");

	/** CUSTOM_CODE START
	 *
	 * Functions for which code is not generated automatically.
	 * Don't move to different class or update these unless you also manually update the custom code part as well.
	 * These functions are usually hand-coded in os_custom.c.
	 *
	 * Typical method to generate them is as following:
	 * 1) Move native call and don't auto-generate bindings.
	 * - define function as regular function. SWT Tools should generate wrappers in os.c
	 * - move wrappers from os.c into os_custom.c and make your adaptations/changes.
	 * - add the 'flags=no_gen' to the method in OS.java
	 *  (e.g, 'flags=no_gen' functions)
	 *
	 * 2) Make native call invoke a custom function.
	 * - create a function in os_custom.c
	 * - create a function in OS.java that will call your function.
	 * (e.g, see the 'swt_*' functions).
	 *
	 * Approach 2 is more portable than approach 1.
	 * (e.g '2' functions can be moved around, where as with '1', the c counter-parts have to be updated manually.)
	 *
	 * '@category custom' is for annotation/visibility in outline.
	 * '@flags=no_gen' is an instruction for SWT tools not to generate code.
	 */
	/** @method flags=no_gen
	 * @category custom
	 */
	public static final native boolean GDK_WINDOWING_X11();
	/** @method flags=no_gen
	 * @category custom
	 */
	public static final native boolean GDK_WINDOWING_WAYLAND();
	/** Custom callbacks */
	/** @method flags=no_gen
	 * @category custom
	 */
	public static final native long pangoLayoutNewProc_CALLBACK(long func);
	/** @method flags=no_gen
	 * @category custom
	 */
	public static final native long pangoFontFamilyNewProc_CALLBACK(long func);
	/** @method flags=no_gen
	 * @category custom
	 */
	public static final native long pangoFontFaceNewProc_CALLBACK(long func);
	/** @method flags=no_gen
	 * @category custom
	 */
	public static final native long printerOptionWidgetNewProc_CALLBACK(long func);
	/** @method flags=no_gen
	 * @category custom
	 */
	public static final native long imContextNewProc_CALLBACK(long func);
	/** @method flags=no_gen
	 * @category custom
	 */
	public static final native long imContextLast();
	/** @method flags=no_gen
	 * @category custom
	 */

	/** @category custom */
	/* Add ability to debug gtk warnings for SWT snippets via SWT_FATAL_WARNINGS=1
	 * env variable. Please see Eclipse bug 471477 */
	public static final native void swt_debug_on_fatal_warnings();

	/** @category custom */
	public static final native long swt_fixed_get_type();

	/** @category custom */
	public static final native long swt_fixed_accessible_get_type();
	/**
	 * @param obj cast=(AtkObject*)
	 * @param is_native cast=(gboolean)
	 * @param to_map cast=(GtkWidget *)
	 * @category custom
	 */
	public static final native void swt_fixed_accessible_register_accessible(long obj, boolean is_native, long to_map);
	/**
	 * @param fixed cast=(SwtFixed*)
	 * @param widget cast=(GtkWidget*)
	 * @param sibling cast=(GtkWidget*)
	 * @category custom
	 */
	public static final native void swt_fixed_restack(long fixed, long widget, long sibling, boolean above);
	/**
	 * @param fixed cast=(SwtFixed*)
	 * @param widget cast=(GtkWidget*)
	 * @category custom
	 */
	public static final native void swt_fixed_move(long fixed, long widget, int x, int y);
	/**
	 * @param fixed cast=(SwtFixed*)
	 * @param widget cast=(GtkWidget*)
	 * @category custom
	 */
	public static final native void swt_fixed_resize(long fixed, long widget, int width, int height);

	/**
	 * @param container cast=(SwtFixed*)
	 * @param widget cast=(GtkWidget*)
	 * @category custom
	 */
	public static final native void swt_fixed_add(long container, long widget);
	/**
	 * @param container cast=(SwtFixed*)
	 * @param widget cast=(GtkWidget*)
	 * @category custom
	 */
	public static final native void swt_fixed_remove(long container, long widget);
	public static final native void swt_set_lock_functions();
	/** @param str cast=(const gchar *)
	 * @category custom
	 */
	/* Custom version of g_utf8_pointer_to_offset */
	public static final native long g_utf16_offset_to_pointer(long str, long offset);

	/**
	 * @param str cast=(const gchar *)
	 * @param pos cast=(const gchar *)
	 * @category custom
	 */
	/* Custom version of g_utf8_pointer_to_offset */
	public static final native long g_utf16_pointer_to_offset(long str, long pos);
	/** @param str cast=(const gchar *)
	 * @category custom
	 */
	/* custom version of g_utf8 for 16 bit */
	public static final native long g_utf16_strlen(long str, long max);
	/** @param str cast=(const gchar *)
	 * @category custom
	 */
	/* custom version of g_utf8 for 16 bit */
	public static final native long g_utf8_offset_to_utf16_offset(long str, long offset);
	/** @param str cast=(const gchar *)
	 * @category custom
	 */
	/* custom version of g_utf8 for 16 bit */
	public static final native long g_utf16_offset_to_utf8_offset(long str, long offset);

	/** CUSTOM_CODE END */

	/**
	 * Gtk has a minimum glib version. (But it's not a 1:1 link, one can have a newer version of glib and older gtk).
	 *
	 * Minimum Glib version requirement of gtk can be found in gtk's 'configure.ac' file, see line 'm4_define([glib_required_version],[2.*.*]).
	 *
	 * For reference:
	 * Gtk3.22 has min version of glib 2.49.4
	 * Gtk3.24 has min version of glib 2.58
	 * Gtk4.0 has min version of glib 2.66
	 */
	public static final int GLIB_VERSION = VERSION(glib_major_version(), glib_minor_version(), glib_micro_version());

	/*
	 * New API in GTK3.22 introduced the "popped-up" signal, which provides
	 * information about where a menu was actually positioned after it's been
	 * popped up. Users can set the environment variable SWT_MENU_LOCATION_DEBUGGING
	 * to 1 in order to help them debug menu positioning issues on GTK3.22+.
	 *
	 * For more information see bug 530204.
	 */
	public static final boolean SWT_MENU_LOCATION_DEBUGGING;

	/*
	 * Enable the DEBUG flag via environment variable. See bug 515849.
	 */
	public static final boolean SWT_DEBUG;

	/*
	 * Check for the GTK_THEME environment variable. If set, parse
	 * it to get the theme name and check if a dark variant is specified.
	 * We can make use of this information when loading SWT system colors.
	 * See bug 534007.
	 */
	/**
	 * True if the GTK_THEME environment variable is specified
	 * and is non-empty.
	 */
	public static final boolean GTK_THEME_SET;
	/**
	 * A string containing the theme name supplied via the GTK_THEME
	 * environment variable. Otherwise this will contain an empty string.
	 */
	public static final String GTK_THEME_SET_NAME;
	/**
	 * True iff overlay scrolling has been disabled via GTK_OVERLAY_SCROLLING=0.
	 * See bug 546248.
	 */
	public static final boolean GTK_OVERLAY_SCROLLING_DISABLED;
	/**
	 * True if SWT is running on the GNOME desktop environment.
	 */
	public static final boolean isGNOME;

	/* Feature in Gtk: with the switch to GtkMenuItems from GtkImageMenuItems
	* in Gtk3 came a small Gtk shortfall: a small amount of padding on the left hand
	* side of MenuItems was added. This padding is not accessible to the developer,
	* causing vertical alignment issues in menus that have both image and text only
	* MenuItems. As an option, the user can specify the SWT_PADDED_MENU_ITEMS environment
	* variable, which (when enabled), double pads MenuItems so as to create consistent
	* vertical alignment throughout that particular menu.
	*
	* For more information see:
	* Bug 470298
	*/
	public static final boolean SWT_PADDED_MENU_ITEMS;
	static {
		String paddedProperty = "SWT_PADDED_MENU_ITEMS";
		String paddedCheck = getEnvironmentalVariable(paddedProperty);
		boolean usePadded = false;
		if (paddedCheck != null && paddedCheck.equals("1")) {
			usePadded = true;
		}
		SWT_PADDED_MENU_ITEMS = usePadded;

		String menuLocationProperty = "SWT_MENU_LOCATION_DEBUGGING";
		String menuLocationCheck = getEnvironmentalVariable(menuLocationProperty);
		boolean menuLocationDebuggingEnabled = false;
		if (menuLocationCheck != null && menuLocationCheck.equals("1") && !GTK.GTK4) {
			menuLocationDebuggingEnabled = true;
		}
		SWT_MENU_LOCATION_DEBUGGING = menuLocationDebuggingEnabled;

		String debugProperty = "SWT_DEBUG";
		String debugCheck = getEnvironmentalVariable(debugProperty);
		boolean swtDebuggingEnabled = false;
		if (debugCheck != null && debugCheck.equals("1")) {
			swtDebuggingEnabled = true;
		}
		SWT_DEBUG = swtDebuggingEnabled;

		String gtkThemeProperty = "GTK_THEME";
		String gtkThemeCheck = getEnvironmentalVariable(gtkThemeProperty);
		boolean gtkThemeSet = false;
		String gtkThemeName = "";
		if (gtkThemeCheck != null && !gtkThemeCheck.isEmpty()) {
			gtkThemeSet = true;
			gtkThemeName = gtkThemeCheck;
		}
		GTK_THEME_SET = gtkThemeSet;
		GTK_THEME_SET_NAME = gtkThemeName;

		String scrollingProperty = "GTK_OVERLAY_SCROLLING";
		String scrollingCheck = getEnvironmentalVariable(scrollingProperty);
		boolean scrollingDisabled = false;
		if (scrollingCheck != null && scrollingCheck.equals("0")) {
			scrollingDisabled = true;
		}
		GTK_OVERLAY_SCROLLING_DISABLED = scrollingDisabled;

		Map<String, String> env = System.getenv();
		String desktopEnvironment = env.get("XDG_CURRENT_DESKTOP");
		boolean gnomeDetected = false;
		if (desktopEnvironment != null) {
			gnomeDetected = desktopEnvironment.contains("GNOME");
		}
		isGNOME = gnomeDetected;

		System.setProperty("org.eclipse.swt.internal.gtk.version",
				(GTK.GTK_VERSION >>> 16) + "." + (GTK.GTK_VERSION >>> 8 & 0xFF) + "." + (GTK.GTK_VERSION & 0xFF));
		// set GDK backend if we are on X11
		if (isX11()) {
			System.setProperty("org.eclipse.swt.internal.gdk.backend", "x11");
		}
	}

protected static byte [] ascii (String name) {
	int length = name.length ();
	char [] chars = new char [length];
	name.getChars (0, length, chars, 0);
	byte [] buffer = new byte [length + 1];
	for (int i=0; i<length; i++) {
		buffer [i] = (byte) chars [i];
	}
	return buffer;
}

public static int VERSION(int major, int minor, int micro) {
	return (major << 16) + (minor << 8) + micro;
}

public static boolean isWayland () {
	return !isX11 ();
}

public static boolean isX11 () {
	return OS.GDK_WINDOWING_X11() && GDK.GDK_IS_X11_DISPLAY(GDK.gdk_display_get_default());
}

/** 64 bit */
public static final native int GPollFD_sizeof ();
public static final native int GTypeInfo_sizeof ();
public static final native int GValue_sizeof();
public static final native int PangoAttribute_sizeof();
public static final native int PangoAttrColor_sizeof();
public static final native int PangoAttrInt_sizeof();
public static final native int PangoItem_sizeof();
public static final native int PangoLayoutLine_sizeof();
public static final native int PangoLayoutRun_sizeof();
public static final native int PangoLogAttr_sizeof();
public static final native int PangoRectangle_sizeof();
public static final native int XAnyEvent_sizeof();
public static final native int XEvent_sizeof();
public static final native int XExposeEvent_sizeof();
public static final native int XFocusChangeEvent_sizeof();
public static final native long localeconv_decimal_point();
/**
 * @param path cast=(const char *)
 * @param realPath cast=(char *)
 */
public static final native long realpath(byte[] path, byte[] realPath);


/** Object private fields accessors */
/** @param object_class cast=(GObjectClass *) */
public static final native long G_OBJECT_CLASS_CONSTRUCTOR(long object_class);
/**
 * @param object_class cast=(GObjectClass *)
 * @paramOFF constructor cast=(GObject* (*) (GType, guint, GObjectConstructParam *))
 */
public static final native void G_OBJECT_CLASS_SET_CONSTRUCTOR(long object_class, long constructor);
/** @param xevent cast=(XEvent *) */
public static final native int X_EVENT_TYPE(long xevent);
/** @param xevent cast=(XAnyEvent *) */
public static final native long X_EVENT_WINDOW(long xevent);

/** X11 Native methods and constants */
public static final int CurrentTime = 0;
public static final int Expose = 12;
public static final int FocusIn = 9;
public static final int FocusOut = 10;
public static final int GraphicsExpose = 13;
public static final int ExposureMask = 1 << 15;
public static final int NotifyNormal = 0;
public static final int NotifyWhileGrabbed = 3;
public static final int NotifyAncestor = 0;
public static final int NotifyVirtual = 1;
public static final int NotifyNonlinear = 3;
public static final int NotifyNonlinearVirtual = 4;
public static final int RevertToParent = 2;
public static final native int Call(long proc, long arg1, long arg2);
public static final native long call(long function, long arg0, long arg1, long arg2, long arg3, long arg4, long arg5, long arg6);
public static final native long call(long function, long arg0, long arg1, long arg2, long arg3);
public static final native long call(long function, long arg0, long arg1, long arg2, long arg3, long arg4, long arg5);
/**
 * @param display cast=(Display *)
 * @param event_return cast=(XEvent *)
 * @param predicate cast=(Bool (*)())
 * @param arg cast=(XPointer)
 */
public static final native boolean XCheckIfEvent(long display, long event_return, long predicate, long arg);
/** @param display cast=(Display *) */
public static final native int XDefaultScreen(long display);
/** @param display cast=(Display *) */
public static final native long XDefaultRootWindow(long display);
/** @param address cast=(void *) */
public static final native void XFree(long address);

/**
 * @param display cast=(Display *)
 * @param w cast=(Window)
 * @param root_return cast=(Window *)
 * @param child_return cast=(Window *)
 * @param root_x_return cast=(int *)
 * @param root_y_return cast=(int *)
 * @param win_x_return cast=(int *)
 * @param win_y_return cast=(int *)
 * @param mask_return cast=(unsigned int *)
 */
public static final native int XQueryPointer(long display, long w, long [] root_return, long [] child_return, int[] root_x_return, int[] root_y_return, int[] win_x_return, int[] win_y_return, int[] mask_return);
/** @param handler cast=(XIOErrorHandler) */
public static final native long XSetIOErrorHandler(long handler);
/** @param handler cast=(XErrorHandler) */
public static final native long XSetErrorHandler(long handler);
/**
 * @param display cast=(Display *)
 * @param window cast=(Window)
 */
public static final native int XSetInputFocus(long display, long window, int revert, int time);
/**
 * @param display cast=(Display *)
 * @param w cast=(Window)
 * @param prop_window cast=(Window)
 */
public static final native int XSetTransientForHint(long display, long w, long prop_window);
/** @param display cast=(Display *) */
public static final native long XSynchronize(long display, boolean onoff);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param size cast=(size_t)
 */
public static final native void memmove(long dest, XExposeEvent src, long size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(XExposeEvent dest, long src, long size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(XFocusChangeEvent dest, long src, long size);


/** Natives */
public static final native int Call (long func, long arg0, int arg1, int arg2);
public static final native long G_OBJECT_GET_CLASS(long object);
public static final native long G_OBJECT_TYPE_NAME(long object);
/** @method flags=const */
public static final native long G_TYPE_BOOLEAN();
/** @method flags=const */
public static final native long G_TYPE_DOUBLE();
/** @method flags=const */
public static final native long G_TYPE_FLOAT();
/** @method flags=const */
public static final native long G_TYPE_LONG();
/** @method flags=const */
public static final native long G_TYPE_INT();
/** @method flags=const */
public static final native long G_TYPE_INT64();
public static final native long G_VALUE_TYPE(long value);
public static final native long G_OBJECT_TYPE(long instance);
/** @method flags=const */
public static final native long G_TYPE_STRING();
public static final native int PANGO_PIXELS(int dimension);
/** @method flags=const */
public static final native long PANGO_TYPE_FONT_DESCRIPTION();
/** @method flags=const */
public static final native long PANGO_TYPE_FONT_FAMILY();
/** @method flags=const */
public static final native long PANGO_TYPE_FONT_FACE();
/** @method flags=const */
public static final native long PANGO_TYPE_LAYOUT();
/**
 * @param commandline cast=(gchar *)
 * @param applName cast=(gchar *)
 * @param flags cast=(GAppInfoCreateFlags)
 * @param error cast=(GError **)
 */
public static final native long g_app_info_create_from_commandline(byte[] commandline, byte[] applName, long flags, long error);
public static final native long g_app_info_get_all();
/**
 * @param appInfo cast=(GAppInfo *)
 */
public static final native long g_app_info_get_executable(long appInfo);
/**
 * @param appInfo cast=(GAppInfo *)
 */
public static final native long g_app_info_get_icon(long appInfo);
/**
 * @param appInfo cast=(GAppInfo *)
 */
public static final native long g_app_info_get_name(long appInfo);
/**
 * @param appInfo cast=(GAppInfo *)
 * @param list cast=(GList *)
 * @param launchContext cast=(GAppLaunchContext *)
 * @param error cast=(GError **)
 */
public static final native boolean g_app_info_launch(long appInfo, long list, long launchContext, long error);
/**
 * @param mimeType cast=(gchar *)
 * @param mustSupportURIs cast=(gboolean)
 */
public static final native long g_app_info_get_default_for_type(byte[] mimeType, boolean mustSupportURIs);
/**
 * @param uri cast=(char *)
 * @param launchContext cast=(GAppLaunchContext *)
 * @param error cast=(GError **)
 */
public static final native boolean g_app_info_launch_default_for_uri(long uri, long launchContext, long error);
/**
 * @param appInfo cast=(GAppInfo *)
 */
public static final native boolean g_app_info_supports_uris(long appInfo);
/**
 * @param error cast=(GError *)
 */
public static final native long g_error_get_message(long error);
/**
 * @param error cast=(const GError *)
 * @param domain cast=(GQuark)
 * @param code cast=(gint)
 */
public static final native boolean g_error_matches(long error, int domain, int code);

/**
 * @param gerror cast=(GError *)
 */
public static final native void g_error_free(long gerror);

/**
 * @param type1 cast=(gchar *)
 * @param type2 cast=(gchar *)
 */
public static final native boolean g_content_type_equals(long type1, byte[] type2);
/**
 * @param type cast=(gchar *)
 * @param supertype cast=(gchar *)
 */
public static final native boolean g_content_type_is_a(long type, byte[] supertype);
public static final native int g_file_error_quark();
/**
 * @param info cast=(GFileInfo *)
 */
public static final native long g_file_info_get_content_type(long info);
/**
 * @param file cast=(GFile *)
 */
public static final native long g_file_get_uri(long file);
/** @param fileName cast=(const char *) */
public static final native long g_file_new_for_path(byte[] fileName);
/**
 * @param fileName cast=(const char *)
 */
public static final native long g_file_new_for_commandline_arg(byte[] fileName);
/** @param fileName cast=(const char *) */
public static final native long g_file_new_for_uri(byte[] fileName);
/**
 * @param file cast=(GFile *)
 * @param attributes cast=(const char *)
 * @param flags cast=(GFileQueryInfoFlags)
 * @param cancellable cast=(GCancellable *)
 * @param error cast=(GError **)
 */
public static final native long g_file_query_info(long file, byte[] attributes, long flags, long cancellable, long error);
/**
 * @param file cast=(const gchar *)
 * @param test cast=(GFileTest)
 */
public static final native boolean /*long*/ g_file_test(byte[] file, int test);
/** @param icon cast=(GIcon *) */
public static final native long g_icon_to_string(long icon);
/**
 * @param str cast=(const gchar *)
 * @param error cast=(GError **)
 */
public static final native long g_icon_new_for_string(byte[] str, long error[]);
/**
 * @param signal_id cast=(guint)
 * @param detail cast=(GQuark)
 * @param hook_func cast=(GSignalEmissionHook)
 * @param hook_data cast=(gpointer)
 * @param data_destroy cast=(GDestroyNotify)
 */
public static final native long g_signal_add_emission_hook(int signal_id, int detail, long hook_func, long hook_data, long data_destroy);
/**
 * @param signal_id cast=(guint)
 * @param hook_id cast=(gulong)
 */
public static final native void g_signal_remove_emission_hook(int signal_id, long hook_id);
/**
 * @param callback_func cast=(GCallback)
 * @param user_data cast=(gpointer)
 * @param destroy_data cast=(GClosureNotify)
 */
public static final native long g_cclosure_new(long callback_func, long user_data, long destroy_data);
/** @param closure cast=(GClosure *) */
public static final native long g_closure_ref(long closure);
/** @param closure cast=(GClosure *) */
public static final native void g_closure_sink(long closure);
/** @param closure cast=(GClosure *) */
public static final native void g_closure_unref(long closure);
/** @param context cast=(GMainContext *) */
public static final native boolean g_main_context_acquire(long context);
/**
 * @param context cast=(GMainContext *)
 * @param fds cast=(GPollFD *)
 */
public static final native int g_main_context_check(long context, int max_priority, long fds, int n_fds);
public static final native long g_main_context_default();
/** @param context cast=(GMainContext *) */
public static final native boolean g_main_context_iteration(long context, boolean may_block);
/** @param context cast=(GMainContext *) */
public static final native long g_main_context_get_poll_func(long context);
/**
 * @param context cast=(GMainContext *)
 * @param priority cast=(gint *)
 */
public static final native boolean g_main_context_prepare(long context, int[] priority);
/**
 * @param context cast=(GMainContext *)
 * @param fds cast=(GPollFD *)
 * @param timeout_ cast=(gint *)
 */
public static final native int g_main_context_query(long context, int max_priority, int[] timeout_, long fds, int n_fds);
/** @param context cast=(GMainContext *) */
public static final native void g_main_context_release(long context);
/** @param context cast=(GMainContext *) */
public static final native void g_main_context_wakeup(long context);
/**
 * @param opsysstring cast=(const gchar *)
 * @param len cast=(gssize)
 * @param bytes_read cast=(gsize *)
 * @param bytes_written cast=(gsize *)
 * @param error cast=(GError **)
 */
public static final native long g_filename_to_utf8(long opsysstring, long len, long [] bytes_read, long [] bytes_written, long [] error);
/** @param filename cast=(const gchar *) */
public static final native long g_filename_display_name(long filename);
/**
 * @param filename cast=(const char *)
 * @param hostname cast=(const char *)
 * @param error cast=(GError **)
 */
public static final native long g_filename_to_uri(long filename, long hostname, long [] error);
/**
 * @param opsysstring cast=(const gchar *)
 * @param len cast=(gssize)
 * @param bytes_read cast=(gsize *)
 * @param bytes_written cast=(gsize *)
 * @param error cast=(GError **)
 */
public static final native long g_filename_from_utf8(long opsysstring, long len,  long [] bytes_read, long [] bytes_written, long [] error);
/**
 * @param uri cast=(const char *)
 * @param hostname cast=(char **)
 * @param error cast=(GError **)
 */
public static final native long g_filename_from_uri(long uri, long [] hostname, long [] error);
/** @param mem cast=(gpointer) */
public static final native void g_free(long mem);
/** @method accessor=g_free,flags=const address */
public static final native long addressof_g_free();
/**
 * @param variable cast=(const gchar *),flags=no_out
 */
public static final native long g_getenv(byte [] variable);
/**
 * @method flags=ignore_deprecations
 * @param result cast=(GTimeVal *)
 */
public static final native void g_get_current_time(long result);
/**
 * @method flags=ignore_deprecations
 * @param result cast=(GTimeVal *)
 * @param microseconds cast=(glong)
 */
public static final native void g_time_val_add(long result, long microseconds);
/**
 * @param function cast=(GSourceFunc)
 * @param data cast=(gpointer)
 */
public static final native int g_idle_add(long function, long data);
/**
 * @param list cast=(GList *)
 * @param data cast=(gpointer)
 */
public static final native long g_list_append(long list, long data);
/** @param list cast=(GList *) */
public static final native long g_list_data(long list);
/** @param list cast=(GList *) */
public static final native void g_list_free(long list);
/**
 * @param list cast=(GList *)
 */
public static final native long g_list_last(long list);
/** @param list cast=(GList *) */
public static final native int g_list_length(long list);
public static final native long g_list_next(long list);
/**
 * @param list cast=(GList *)
 * @param n cast=(guint)
 */
public static final native long g_list_nth_data(long list, int n);
public static final native long g_list_previous(long list);
/**
 * @param log_domain cast=(gchar *)
 * @param log_levels cast=(GLogLevelFlags)
 * @param message cast=(gchar *)
 * @param unused_data cast=(gpointer)
 */
public static final native void g_log_default_handler(long log_domain, int log_levels, long message, long unused_data);
/**
 * @param log_domain cast=(gchar *),flags=no_out
 * @param handler_id cast=(gint)
 */
public static final native void g_log_remove_handler(byte[] log_domain, int handler_id);
/**
 * @param log_domain cast=(gchar *),flags=no_out
 * @param log_levels cast=(GLogLevelFlags)
 * @param log_func cast=(GLogFunc)
 * @param user_data cast=(gpointer)
 */
public static final native int g_log_set_handler(byte[] log_domain, int log_levels, long log_func, long user_data);
/** @param size cast=(gulong) */
public static final native long g_malloc(long size);
/**
 * @param object cast=(GObject *)
 * @param first_property_name cast=(const gchar *),flags=no_out
 * @param terminator cast=(const gchar *),flags=sentinel
 */
public static final native void g_object_get(long object, byte[] first_property_name, int[] value, long terminator);
/**
 * @param object cast=(GObject *)
 * @param first_property_name cast=(const gchar *),flags=no_out
 * @param terminator cast=(const gchar *),flags=sentinel
 */
public static final native void g_object_get(long object, byte[] first_property_name, long[] value, long terminator);
/**
 * @param object cast=(GObject *)
 * @param quark cast=(GQuark)
 */
public static final native long g_object_get_qdata(long object, int quark);
/**
 * @param type cast=(GType)
 * @param first_property_name cast=(const gchar *)
 */
public static final native long g_object_new(long type, long first_property_name);
/**
 * @param object cast=(GObject *)
 * @param property_name cast=(const gchar *)
 */
public static final native void g_object_notify(long object, byte[] property_name);
/** @param object cast=(gpointer) */
public static final native long g_object_ref(long object);
/**
 * @param object cast=(gpointer)
 * @param first_property_name cast=(const gchar *),flags=no_out
 * @param terminator cast=(const gchar *),flags=sentinel
 */
public static final native void g_object_set(long object, byte[] first_property_name, boolean data, long terminator);
/**
 * @param object cast=(gpointer)
 * @param first_property_name cast=(const gchar *),flags=no_out
 * @param terminator cast=(const gchar *),flags=sentinel
 */
public static final native void g_object_set(long object, byte[] first_property_name, byte[] data, long terminator);

//Note, the function below is handled in a special way in os.h because of the GdkRGBA (gtk3 only) struct. See os.h
//So although it is not marked as dynamic, it is only build on gtk3.
/**
 * @param object cast=(gpointer)
 * @param first_property_name cast=(const gchar *)
 * @param terminator cast=(const gchar *),flags=sentinel
 */
public static final native void g_object_set(long object, byte[] first_property_name, GdkRGBA data, long terminator);

/**
 * @param object cast=(gpointer)
 * @param first_property_name cast=(const gchar *),flags=no_out
 * @param terminator cast=(const gchar *),flags=sentinel
 */
public static final native void g_object_set(long object, byte[] first_property_name, int data, long terminator);
/**
 * @param object cast=(gpointer)
 * @param first_property_name cast=(const gchar *),flags=no_out
 * @param terminator cast=(const gchar *),flags=sentinel
 */
public static final native void g_object_set(long object, byte[] first_property_name, float data, long terminator);
/**
 * @param object cast=(gpointer)
 * @param first_property_name cast=(const gchar *),flags=no_out
 * @param terminator cast=(const gchar *),flags=sentinel
 */
public static final native void g_object_set(long object, byte[] first_property_name, long data, long terminator);
/**
 * @param object cast=(GObject *)
 * @param quark cast=(GQuark)
 * @param data cast=(gpointer)
 */
public static final native void g_object_set_qdata(long object, int quark, long data);
/** @param object cast=(gpointer) */
public static final native void g_object_unref(long object);

/**
 * @param data cast=(gconstpointer)
 * @param size cast=(gsize)
 */
public static final native long g_bytes_new(byte [] data, long size);

/**
 * @param gBytes cast=(GBytes *)
 */
public static final native void g_bytes_unref(long gBytes);

/** @param string cast=(const gchar *),flags=no_out */
public static final native int g_quark_from_string(byte[] string);
/** @param prgname cast=(const gchar *),flags=no_out */
public static final native void g_set_prgname(byte[] prgname);
/**
 * @param instance cast=(gpointer)
 * @param detailed_signal cast=(const gchar *),flags=no_out
 * @param proc cast=(GCallback)
 * @param data cast=(gpointer)
 */
public static final native int g_signal_connect(long instance, byte[] detailed_signal, long proc, long data);
/**
 * @param instance cast=(gpointer)
 * @param detailed_signal cast=(const gchar *)
 * @param closure cast=(GClosure *)
 * @param after cast=(gboolean)
 */
public static final native int g_signal_connect_closure(long instance, byte[] detailed_signal, long closure, boolean after);
/**
 * @param instance cast=(gpointer)
 * @param signal_id cast=(guint)
 * @param detail cast=(GQuark)
 * @param closure cast=(GClosure *)
 * @param after cast=(gboolean)
 */
public static final native int g_signal_connect_closure_by_id(long instance, int signal_id, int detail, long closure, boolean after);
/**
 * @param instance cast=(gpointer)
 * @param detailed_signal cast=(const gchar *),flags=no_out
 */
public static final native void g_signal_emit_by_name(long instance, byte[] detailed_signal);
/**
 * @param instance cast=(gpointer)
 * @param detailed_signal cast=(const gchar *),flags=no_out
 */
public static final native void g_signal_emit_by_name(long instance, byte[] detailed_signal, long data);
/**
 * @param instance cast=(gpointer)
 * @param detailed_signal cast=(const gchar *),flags=no_out
 */
public static final native void g_signal_emit_by_name(long instance, byte[] detailed_signal, GdkRectangle data);
/**
 * @param instance cast=(gpointer)
 * @param detailed_signal cast=(const gchar *),flags=no_out
 */
public static final native void g_signal_emit_by_name(long instance, byte[] detailed_signal, long data1, long data2);
/**
 * @param instance cast=(gpointer)
 * @param detailed_signal cast=(const gchar *),flags=no_out
 */
public static final native void g_signal_emit_by_name(long instance, byte[] detailed_signal, byte [] data);
/**
 * @param instance cast=(gpointer)
 * @param handler_id cast=(gulong)
 */
public static final native void g_signal_handler_disconnect(long instance, long handler_id);
/**
 * @param instance cast=(gpointer)
 * @param mask cast=(GSignalMatchType)
 * @param signal_id cast=(guint)
 * @param detail cast=(GQuark)
 * @param closure cast=(GClosure *)
 * @param func cast=(gpointer)
 * @param data cast=(gpointer)
 */
public static final native int g_signal_handlers_block_matched(long instance, int mask, int signal_id, int detail, long closure, long func, long data);
/**
 * @param instance cast=(gpointer)
 * @param mask cast=(GSignalMatchType)
 * @param signal_id cast=(guint)
 * @param detail cast=(GQuark)
 * @param closure cast=(GClosure *)
 * @param func cast=(gpointer)
 * @param data cast=(gpointer)
 */
public static final native int g_signal_handlers_unblock_matched(long instance, int mask, int signal_id, int detail, long closure, long func, long data);
/** @param name cast=(const gchar *),flags=no_out */
public static final native int g_signal_lookup(byte[] name, long itype);
/**
 * @param instance cast=(gpointer)
 * @param detailed_signal cast=(const gchar *),flags=no_out
 */
public static final native void g_signal_stop_emission_by_name(long instance, byte[] detailed_signal);
/** @param tag cast=(guint) */
public static final native boolean /*long*/ g_source_remove(long tag);
/**
 * @param list cast=(GSList *)
 * @param data cast=(gpointer)
 */
public static final native long g_slist_append(long list, long data);
/** @param list cast=(GSList *) */
public static final native long g_slist_data(long list);
/** @param list cast=(GSList *) */
public static final native void g_slist_free(long list);
/** @param list cast=(GSList *) */
public static final native long g_slist_next(long list);
/** @param list cast=(GSList *) */
public static final native int g_slist_length(long list);
/** @param string_array cast=(gchar **) */
public static final native void g_strfreev(long string_array);
/**
 * @param str cast=(const gchar *)
 * @param endptr cast=(gchar **)
 */
public static final native double g_strtod(long str, long [] endptr);
/** @param str cast=(char *) */
public static final native long g_strdup (long str);
/** @param g_class cast=(GType) */
public static final native long g_type_class_peek(long g_class);
/** @param g_class cast=(gpointer) */
public static final native long g_type_class_peek_parent(long g_class);
/** @param g_class cast=(GType) */
public static final native long g_type_class_ref(long g_class);
/** @param g_class cast=(gpointer) */
public static final native void g_type_class_unref(long g_class);
/** @param iface cast=(gpointer) */
public static final native long g_type_interface_peek_parent(long iface);
/**
 * @param type cast=(GType)
 * @param is_a_type cast=(GType)
 */
public static final native boolean g_type_is_a(long type, long is_a_type);
/** @param type cast=(GType) */
public static final native long g_type_parent(long type);
/**
 * @param parent_type cast=(GType)
 * @param type_name cast=(const gchar *)
 * @param info cast=(const GTypeInfo *)
 * @param flags cast=(GTypeFlags)
 */
public static final native long g_type_register_static(long parent_type, byte[] type_name, long info, int flags);
/**
 * @param str cast=(const gunichar2 *),flags=no_out critical
 * @param len cast=(glong)
 * @param items_read cast=(glong *),flags=critical
 * @param items_written cast=(glong *),flags=critical
 * @param error cast=(GError **),flags=critical
 */
public static final native long g_utf16_to_utf8(char[] str, long len, long [] items_read, long [] items_written, long [] error);
/**
 * @param str cast=(const gchar *)
 * @param pos cast=(const gchar *)
 */
public static final native long g_utf8_pointer_to_offset(long str, long pos);
/** @param str cast=(const gchar *) */
public static final native long g_utf8_strlen(long str, long max);
/**
 * @param str cast=(const gchar *),flags=no_out critical
 * @param len cast=(glong)
 * @param items_read cast=(glong *),flags=critical
 * @param items_written cast=(glong *),flags=critical
 * @param error cast=(GError **),flags=critical
 */
public static final native long g_utf8_to_utf16(byte[] str, long len, long [] items_read, long [] items_written, long [] error);
/**
 * @param str cast=(const gchar *)
 * @param len cast=(glong)
 * @param items_read cast=(glong *),flags=critical
 * @param items_written cast=(glong *),flags=critical
 * @param error cast=(GError **),flags=critical
 */
public static final native long g_utf8_to_utf16(long str, long len, long [] items_read, long [] items_written, long [] error);
/**
 * @param value cast=(GValue *)
 * @param type cast=(GType)
 */
public static final native long g_value_init (long value, long type);
/** @param value cast=(GValue *) */
public static final native int g_value_get_int (long value);
/** @param value cast=(GValue *) */
public static final native void g_value_set_int (long value, int v);
/** @param value cast=(GValue *) */
public static final native double g_value_get_double (long value);
/** @param value cast=(GValue *) */
public static final native void g_value_set_double (long value, double v);
/** @param value cast=(GValue *) */
public static final native float g_value_get_float (long value);
/** @param value cast=(GValue *) */
public static final native void g_value_set_float (long value, float v);
/** @param value cast=(GValue *) */
public static final native long g_value_get_int64 (long value);
/** @param value cast=(GValue *) */
public static final native void g_value_set_int64 (long value, long v);
/** @param value cast=(GValue *)
 *  @param v_string cast =(const gchar *)
 * */
public static final native void g_value_set_string (long value, byte[] v_string);
/** @param value cast=(GValue *) */
public static final native long g_value_get_string (long value);
/** @param value cast=(GValue *) */
public static final native long g_value_get_object (long value);
/** @param value cast=(GValue *) */
public static final native void g_value_unset (long value);
/** @param value cast=(const GValue *) */
public static final native long g_value_peek_pointer(long value);
/**
 * @param variable cast=(const gchar *),flags=no_out
 */
public static final native void g_unsetenv(byte [] variable);
/** @method flags=const */
public static final native int glib_major_version();
/** @method flags=const */
public static final native int glib_minor_version();
/** @method flags=const */
public static final native int glib_micro_version();
/**
 * @param interval cast=(guint32)
 * @param function cast=(GSourceFunc)
 * @param data cast=(gpointer)
 */
public static final native int g_timeout_add(int interval, long function, long data);

/** @method flags=dynamic */
public static final native boolean FcConfigAppFontAddFile(long config, byte[] file);

/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param size cast=(size_t)
 */
public static final native void memmove(long dest, GTypeInfo src, int size);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param size cast=(size_t)
 */
public static final native void memmove(long dest, GdkRGBA src, long size);
/** @param src flags=no_out */
public static final native void memmove(long dest, GtkWidgetClass src);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param size cast=(size_t)
 */
public static final native void memmove(long dest, PangoAttribute src, long size);
/** @param dest flags=no_in */
public static final native void memmove(GtkWidgetClass dest, long src);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(GtkBorder dest, long src, long size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(GdkKeymapKey dest, long src, long size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(GdkRGBA dest, long src, long size);
public static final native void memmove(long dest, GtkCellRendererClass src);
public static final native void memmove(GtkCellRendererClass dest, long src);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(GdkRectangle dest, long src, long size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(PangoAttribute dest, long src, long size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(PangoAttrColor dest, long src, long size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(PangoAttrInt dest, long src, long size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(PangoItem dest, long src, long size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(PangoLayoutLine dest, long src, long size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(PangoLayoutRun dest, long src, long size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(PangoLogAttr dest, long src, long size);
public static final native int pango_version();
/** @param attribute cast=(const PangoAttribute *) */
public static final native long pango_attribute_copy(long attribute);
public static final native long pango_attr_background_new(short red, short green, short blue);
/** @param desc cast=(const PangoFontDescription *) */
public static final native long pango_attr_font_desc_new(long desc);
public static final native long pango_attr_foreground_new(short red, short green, short blue);
public static final native long pango_attr_rise_new(int rise);
/**
 * @param ink_rect flags=no_out
 * @param logical_rect flags=no_out
 */
public static final native long pango_attr_shape_new(PangoRectangle ink_rect, PangoRectangle logical_rect);
/**
 * @param list cast=(PangoAttrList *)
 * @param attr cast=(PangoAttribute *)
 */
public static final native void pango_attr_list_insert(long list, long attr);
/** @param list cast=(PangoAttrList *) */
public static final native long pango_attr_list_get_iterator(long list);
/** @param iterator cast=(PangoAttrIterator *) */
public static final native boolean pango_attr_iterator_next(long iterator);
/**
 * @param iterator cast=(PangoAttrIterator *)
 * @param start cast=(gint *)
 * @param end cast=(gint *)
 */
public static final native void pango_attr_iterator_range(long iterator, int[] start, int[] end);
/**
 * @param iterator cast=(PangoAttrIterator *)
 * @param type cast=(PangoAttrType)
 */
public static final native long pango_attr_iterator_get(long iterator, int type);
/** @param iterator cast=(PangoAttrIterator *) */
public static final native void pango_attr_iterator_destroy(long iterator);
public static final native long pango_attr_list_new();
/** @param list cast=(PangoAttrList *) */
public static final native void pango_attr_list_unref(long list);
/** @method flags=dynamic **/
public static final native long pango_attr_insert_hyphens_new(boolean hyphens);
public static final native long pango_attr_strikethrough_color_new(short red, short green, short blue);
public static final native long pango_attr_strikethrough_new(boolean strikethrough);
public static final native long pango_attr_underline_color_new(short red, short green, short blue);
public static final native long pango_attr_underline_new(int underline);
public static final native long pango_attr_weight_new(int weight);
/**
 * @param cairo cast=(cairo_t *)
 */
public static final native long pango_cairo_create_layout(long cairo);
public static final native long pango_cairo_font_map_get_default();
/**
 * @param context cast=(PangoContext *)
 */
public static final native long pango_cairo_context_get_font_options(long context);
/**
 * @param context cast=(PangoContext *)
 * @param options cast=( cairo_font_options_t *)
 */
public static final native void pango_cairo_context_set_font_options(long context, long options);
/**
 * @param cairo cast=(cairo_t *)
 * @param layout cast=(PangoLayout *)
 */
public static final native void pango_cairo_layout_path(long cairo, long layout);
/**
 * @param cairo cast=(cairo_t *)
 * @param layout cast=(PangoLayout *)
 */
public static final native void pango_cairo_show_layout(long cairo, long layout);
/** @param context cast=(PangoContext *) */
public static final native int pango_context_get_base_dir(long context);
/** @param context cast=(PangoContext *) */
public static final native long pango_context_get_language(long context);
/**
 * @param context cast=(PangoContext *)
 * @param desc cast=(const PangoFontDescription *)
 * @param language cast=(PangoLanguage *)
 */
public static final native long pango_context_get_metrics(long context, long desc, long language);
/**
 * @param context cast=(PangoContext *)
 * @param families cast=(PangoFontFamily ***)
 * @param n_families cast=(int *)
 */
public static final native void pango_context_list_families(long context, long [] families, int[] n_families);
/** @param context cast=(PangoContext *) */
public static final native void pango_context_set_base_dir(long context, int direction);
/**
 * @param context cast=(PangoContext *)
 * @param language cast=(PangoLanguage *)
 */
public static final native void pango_context_set_language(long context, long language);


/* PangoFontDescription */
/** @param desc cast=(PangoFontDescription *) */
public static final native long pango_font_description_copy(long desc);
/** @param desc cast=(PangoFontDescription *) */
public static final native void pango_font_description_free(long desc);
/** @param str cast=(const char *),flags=no_out critical */
public static final native long pango_font_description_from_string(byte[] str);
/** @param desc cast=(PangoFontDescription *) */
public static final native long pango_font_description_get_family(long desc);
/** @param desc cast=(PangoFontDescription *) */
public static final native int pango_font_description_get_size(long desc);
/** @param desc cast=(PangoFontDescription *) */
public static final native int pango_font_description_get_stretch(long desc);
/** @param desc cast=(PangoFontDescription *) */
public static final native int pango_font_description_get_variant(long desc);
/** @param desc cast=(PangoFontDescription *) */
public static final native int pango_font_description_get_style(long desc);
/** @param desc cast=(PangoFontDescription *) */
public static final native int pango_font_description_get_weight(long desc);
public static final native long pango_font_description_new();
/**
 * @param desc cast=(PangoFontDescription *)
 * @param family cast=(const char *),flags=no_out critical
 */
public static final native void pango_font_description_set_family(long desc, byte[] family);
/**
 * @param desc cast=(PangoFontDescription *)
 * @param size cast=(gint)
 */
public static final native void pango_font_description_set_size(long desc, int size);
/**
 * @param desc cast=(PangoFontDescription *)
 * @param stretch cast=(PangoStretch)
 */
public static final native void pango_font_description_set_stretch(long desc, int stretch);
/**
 * @param desc cast=(PangoFontDescription *)
 * @param weight cast=(PangoStyle)
 */
public static final native void pango_font_description_set_style(long desc, int weight);
/**
 * @param desc cast=(PangoFontDescription *)
 * @param weight cast=(PangoWeight)
 */
public static final native void pango_font_description_set_weight(long desc, int weight);
/**
 * @param desc cast=(PangoFontDescription *)
 * @param variant cast=(PangoVariant)
 */
public static final native void pango_font_description_set_variant(long desc, int variant);
/** @param desc cast=(PangoFontDescription *) */
public static final native long pango_font_description_to_string(long desc);
/** @param desc cast=(PangoFontDescription *) */
public static final native int pango_font_description_get_set_fields(long desc);


/* PangoFontFace */
/** @param face cast=(PangoFontFace *) */
public static final native long pango_font_face_describe(long face);


/* PangoFontFamily */
/** @param family cast=(PangoFontFamily *) */
public static final native long pango_font_family_get_name(long family);
/**
 * @param family cast=(PangoFontFamily *)
 * @param faces cast=(PangoFontFace ***)
 * @param n_faces cast=(int *)
 */
public static final native void pango_font_family_list_faces(long family, long [] faces, int[] n_faces);


/* PangoFontMap */
/** @param fontMap cast=(PangoFontMap *) */
public static final native long pango_font_map_create_context(long fontMap);
/** @param metrics cast=(PangoFontMetrics *) */


/* PangoFontMetrics */
public static final native int pango_font_metrics_get_approximate_char_width(long metrics);
/** @param metrics cast=(PangoFontMetrics *) */
public static final native int pango_font_metrics_get_ascent(long metrics);
/** @param metrics cast=(PangoFontMetrics *) */
public static final native int pango_font_metrics_get_descent(long metrics);
/** @param metrics cast=(PangoFontMetrics *) */
public static final native void pango_font_metrics_unref(long metrics);

/* PangoLayout */
/** @param layout cast=(PangoLayout *) */
public static final native void pango_layout_context_changed(long layout);
/** @param layout cast=(PangoLayout*) */
public static final native int pango_layout_get_alignment(long layout);
/** @param layout cast=(PangoLayout *) */
public static final native long pango_layout_get_context(long layout);
/** @param layout cast=(PangoLayout*) */
public static final native int pango_layout_get_indent(long layout);
/** @param layout cast=(PangoLayout*) */
public static final native long pango_layout_get_iter(long layout);
/** @param layout cast=(PangoLayout*) */
public static final native boolean pango_layout_get_justify(long layout);
/** @param layout cast=(PangoLayout *) */
public static final native long pango_layout_get_line(long layout, int line);
/** @param layout cast=(PangoLayout*) */
public static final native int pango_layout_get_line_count(long layout);
/**
 * @param layout cast=(PangoLayout*)
 * @param attrs cast=(PangoLogAttr **)
 * @param n_attrs cast=(int *)
 */
public static final native void pango_layout_get_log_attrs(long layout, long [] attrs, int[] n_attrs);
/**
 * @param layout cast=(PangoLayout *)
 * @param width cast=(int *)
 * @param height cast=(int *)
 */
public static final native void pango_layout_get_size(long layout, int[] width, int[] height);
/**
 * @param layout cast=(PangoLayout *)
 * @param width cast=(int *)
 * @param height cast=(int *)
 */
public static final native void pango_layout_get_pixel_size(long layout, int[] width, int[] height);
/** @param layout cast=(PangoLayout*) */
public static final native int pango_layout_get_spacing(long layout);
/** @param layout cast=(PangoLayout *) */
public static final native long pango_layout_get_text(long layout);
/** @param layout cast=(PangoLayout *) */
public static final native int pango_layout_get_width(long layout);
/**
 * @param layout cast=(PangoLayout*)
 * @param pos flags=no_in
 */
public static final native void pango_layout_index_to_pos(long layout, int index, PangoRectangle pos);
/** @param iter cast=(PangoLayoutIter*) */
public static final native void pango_layout_iter_free(long iter);
/**
 * @param iter cast=(PangoLayoutIter*)
 * @param ink_rect flags=no_in
 * @param logical_rect flags=no_in
 */
public static final native void pango_layout_iter_get_line_extents(long iter, PangoRectangle ink_rect, PangoRectangle logical_rect);
/** @param iter cast=(PangoLayoutIter*) */
public static final native int pango_layout_iter_get_index(long iter);
/** @param iter cast=(PangoLayoutIter*) */
public static final native long pango_layout_iter_get_run(long iter);
/** @param iter cast=(PangoLayoutIter*) */
public static final native boolean pango_layout_iter_next_line(long iter);
/** @param iter cast=(PangoLayoutIter*) */
public static final native boolean pango_layout_iter_next_run(long iter);
/**
 * @param line cast=(PangoLayoutLine*)
 * @param ink_rect cast=(PangoRectangle *),flags=no_in
 * @param logical_rect cast=(PangoRectangle *),flags=no_in
 */
public static final native void pango_layout_line_get_extents(long line, PangoRectangle ink_rect, PangoRectangle logical_rect);
/** @param context cast=(PangoContext *) */
public static final native long pango_layout_new(long context);
/** @param layout cast=(PangoLayout *) */
public static final native void pango_layout_set_alignment(long layout, int alignment);
/**
 * @param layout cast=(PangoLayout *)
 * @param attrs cast=(PangoAttrList *)
 */
public static final native void pango_layout_set_attributes(long layout, long attrs);
/**
 * @param layout cast=(PangoLayout *)
 */
public static final native void pango_layout_set_auto_dir(long layout, boolean auto_dir);
/**
 * @param context cast=(PangoLayout *)
 * @param descr cast=(PangoFontDescription *)
 */
public static final native void pango_layout_set_font_description(long context, long descr);
/** @param layout cast=(PangoLayout*) */
public static final native void pango_layout_set_indent(long layout, int indent);
/** @param layout cast=(PangoLayout*) */
public static final native void pango_layout_set_justify(long layout, boolean justify);
/**
 * @param context cast=(PangoLayout *)
 * @param setting cast=(gboolean)
 */
public static final native void pango_layout_set_single_paragraph_mode(long context, boolean setting);
/** @param layout cast=(PangoLayout *) */
public static final native void pango_layout_set_spacing(long layout, int spacing);
/**
 * @param layout cast=(PangoLayout *)
 * @param tabs cast=(PangoTabArray *)
 */
public static final native void pango_layout_set_tabs(long layout, long tabs);
/**
 * @param layout cast=(PangoLayout *)
 * @param text cast=(const char *),flags=no_out critical
 * @param length cast=(int)
 */
public static final native void pango_layout_set_text(long layout, byte[] text, int length);
/** @param layout cast=(PangoLayout *) */
public static final native void pango_layout_set_width(long layout, int width);
/** @param layout cast=(PangoLayout *) */
public static final native void pango_layout_set_wrap(long layout, int wrap);
/**
 * @param layout cast=(PangoLayout *)
 * @param index cast=(int *)
 * @param trailing cast=(int *)
 */
public static final native boolean pango_layout_xy_to_index(long layout, int x, int y, int[] index, int[] trailing);


/** @param tab_array cast=(PangoTabArray *) */
public static final native void pango_tab_array_free(long tab_array);
/**
 * @param initial_size cast=(gint)
 * @param positions_in_pixels cast=(gboolean)
 */
public static final native long pango_tab_array_new(int initial_size, boolean positions_in_pixels);
/**
 * @param tab_array cast=(PangoTabArray *)
 * @param tab_index cast=(gint)
 * @param alignment cast=(PangoTabAlign)
 * @param location cast=(gint)
 */
public static final native void pango_tab_array_set_tab(long tab_array, int tab_index, long alignment, int location);
/**
 * @method flags=dynamic
 */
public static final native long ubuntu_menu_proxy_get();
/**
 * @param s1 cast=(const char*)
 * @param s2 cast=(const char*)
 */
public static final native int strcmp (long s1, byte [] s2);

/**
 * Theme name as given by OS.
 * You can see the exact theme name via Tweak Tools -> Appearance -> Themes.
 * E.g
 * 		Adwaita
 * 		Adwaita-Dark
 * 		Ambiance 		(Ubuntu).
 *
 * See also: Device.overrideThemeValues();
 */
public static final String getThemeName() {
	byte[] themeNameBytes = getThemeNameBytes();
	String themeName = "unknown";
	if (themeNameBytes != null && themeNameBytes.length > 0) {
		themeName = new String (Converter.mbcsToWcs (themeNameBytes));
	}
	return themeName;
}

public static final byte [] getThemeNameBytes() {
	byte [] buffer = null;
	int length;
	long settings = GTK.gtk_settings_get_default ();
	long [] ptr = new long [1];
	OS.g_object_get (settings, GTK.gtk_theme_name, ptr, 0);
	if (ptr [0] == 0) {
		return buffer;
	}
	length = C.strlen (ptr [0]);
	if (length == 0) {
		return buffer;
	}
	/* String will be passed to C function later, needs to be zero-terminated */
	buffer = new byte [length + 1];
	C.memmove (buffer, ptr [0], length);
	OS.g_free (ptr [0]);
	return buffer;
}

/**
 * Hint GTK 3 to natively prefer a dark or light theme.
 * <p>
 * Note: This method gets called from the org.eclipse.e4.ui.swt.gtk fragment.
 * </p>
 *
 * @since 3.104
 */
	public static final void setDarkThemePreferred(boolean preferred) {
		g_object_set(GTK.gtk_settings_get_default(), GTK.gtk_application_prefer_dark_theme, preferred, 0);
		g_object_notify(GTK.gtk_settings_get_default(), GTK.gtk_application_prefer_dark_theme);
	}

/**
 * Experimental API for dark theme.
 * <p>
 * On Windows, there is no OS API for dark theme yet, and this method only
 * configures various tweaks. Some of these tweaks have drawbacks. The tweaks
 * are configured with defaults that fit Eclipse. Non-Eclipse applications are
 * expected to configure individual tweaks instead of calling this method.
 * Please see <code>Display#setData()</code> and documentation for string keys
 * used there.
 * </p>
 * <p>
 * On GTK, behavior may be different as the boolean flag doesn't force dark
 * theme instead it specify that dark theme is preferred.
 * </p>
 *
 * @param isDarkTheme <code>true</code> for dark theme
 */
public static final void setTheme(boolean isDarkTheme) {
	setDarkThemePreferred (isDarkTheme);
}

/**
 * @param tmpl cast=(const gchar *)
 * @param error cast=(GError **)
 */
public static final native long g_dir_make_tmp(long tmpl, long [] error);

/**
 * @param info cast=(GDBusInterfaceInfo *)
 * @param name cast=(const gchar *)
 * @param object_path cast=(const gchar *)
 * @param interface_name cast=(const gchar *)
 * @param cancellable cast=(GCancellable *)
 * @param error cast=(GError **)
 * @category gdbus
 */
public static final native long g_dbus_proxy_new_for_bus_sync(int bus_type, int flags, long info, byte [] name, byte [] object_path, byte [] interface_name,
		long cancellable, long [] error);

/**
 * @param proxy cast=(GDBusProxy *)
 * @param method_name cast=(const gchar *)
 * @param parameters cast=(GVariant *)
 * @param cancellable cast=(GCancellable *)
 * @param error cast=(GError **)
 * @category gdbus
 */
public static final native long g_dbus_proxy_call_sync (long proxy, byte[] method_name, long parameters, int flags, int timeout_msec, long cancellable, long [] error);

/**
 * @param proxy cast=(GDBusProxy *)
 * @param method_name cast=(const gchar *)
 * @param parameters cast=(GVariant *)
 * @param cancellable cast=(GCancellable *)
 * @param callback cast=(GAsyncReadyCallback)
 * @param error cast=(GError **)
 * @category gdbus
 */
public static final native void g_dbus_proxy_call (long proxy, byte[] method_name, long parameters, int flags, int timeout_msec, long cancellable, long callback, long [] error);

/**
 * @param proxy cast=(GDBusProxy *)
 * @category gdbus
 */
public static final native long g_dbus_proxy_get_name_owner(long proxy);

/**
 * @param xml_data cast=(const gchar *)
 * @param error cast=(GError **)
 * @category gdbus
 */
public static final native long g_dbus_node_info_new_for_xml(byte[] xml_data, long [] error);

/**
 * @param bus_type cast=(GBusType)
 * @param name cast=(const gchar *)
 * @param flags cast=(GBusNameOwnerFlags)
 * @param bus_acquired_handler cast=(GBusAcquiredCallback)
 * @param name_acquired_handler cast=(GBusNameAcquiredCallback)
 * @param name_lost_handler cast=(GBusNameLostCallback)
 * @param user_data cast=(gpointer)
 * @param user_data_free_func cast=(GDestroyNotify)
 * @category gdbus
 */
public static final native int g_bus_own_name(int bus_type, byte[] name, int flags, long bus_acquired_handler, long name_acquired_handler, long name_lost_handler, long  user_data, long user_data_free_func);

/**
 * @param connection cast=(GDBusConnection *)
 * @param object_path cast=(const gchar *)
 * @param interface_info cast=(GDBusInterfaceInfo *)
 * @param vtable cast=(const GDBusInterfaceVTable *)
 * @param user_data cast=(gpointer)
 * @param user_data_free_func cast=(GDestroyNotify)
 * @param error cast=(GError **)
 * @category gdbus
 */
public static final native int g_dbus_connection_register_object(long connection, byte[] object_path, long interface_info, long [] vtable, long user_data, long user_data_free_func, long [] error);

/**
 * @param info cast=(GDBusNodeInfo *)
 * @param name cast=(const gchar *)
 * @category gdbus
 */
public static final native long g_dbus_node_info_lookup_interface(long info, byte [] name);

/**
 * @param invocation cast=(GDBusMethodInvocation *)
 * @param parameters cast=(GVariant *)
 * @category gdbus
 */
public static final native void g_dbus_method_invocation_return_value(long invocation, long parameters);

/**
 * @param type cast=(const GVariantType *)
 * @category gdbus
 */
public static final native long g_variant_builder_new(long type);

/**
 * @param builder cast=(GVariantBuilder *)
 * @param value cast=(GVariant *)
 * @category gdbus
 */
public static final native void g_variant_builder_add_value(long builder, long value);

/**
 * @param type cast=(GVariantType *)
 * @category gdbus
 */
public static final native void g_variant_type_free(long type);

/**
 * @param type cast=(const gchar *)
 * @category gdbus
 */
public static final native long g_variant_type_new(byte [] type);

/**
 * @param builder cast=(GVariantBuilder *)
 * @category gdbus
 */
public static final native long g_variant_builder_end(long builder);

/**
 * @param builder cast=(GVariantBuilder *)
 * @category gdbus
 */
public static final native void g_variant_builder_unref(long builder);

/**
 * @param format_string cast=(const gchar *),flags=no_out
 * @param arg0 cast=(const gchar *),flags=no_out
 * @category gdbus
 */
public static final native long g_variant_new (byte[] format_string, byte[] arg0);

/**
 * @param format_string cast=(const gchar *),flags=no_out
 * @param arg0 cast=(gboolean)
 * @param arg1 cast=(const gchar *),flags=no_out
 * @category gdbus
 */
public static final native long g_variant_new (byte[] format_string, boolean arg0, byte[] arg1);

/**
 * @param format_string cast=(const gchar *),flags=no_out
 * @param arg0 cast=(const gchar *),flags=no_out
 * @param arg1 cast=(const gchar *),flags=no_out
 * @category gdbus
 */
public static final native long g_variant_new (byte[] format_string, byte[] arg0, byte[] arg1);

/**
 * @param intval cast=(gint32)
 * @category gdbus
 */
public static final native long g_variant_new_int32(int intval);


/**
 * @param gvariant cast=(GVariant *)
 * @category gdbus
 * @return int
 */
public static final native int g_variant_get_int32(long gvariant);

/**
 * @param gvariant cast=(GVariant *)
 * @category gdbus
 * @return guchar
 */
public static final native byte g_variant_get_byte(long gvariant);

/**
 * @param gvariant cast=(GVariant *)
 * @category gdbus
 */
public static final native boolean g_variant_get_boolean(long gvariant);

/**
 * @param gvariant cast=(GVariant *)
 * @param index cast=(gsize)
 * @category gdbus
 */
public static final native long g_variant_get_child_value(long gvariant, int index);

/**
 * @param gvariant cast=(GVariant *)
 * @category gdbus
 */
public static final native double g_variant_get_double(long gvariant);

public static final native long g_variant_new_uint64(long value);

/**
 * @param gvariant cast=(GVariant *)
 * @category gdbus
 */
public static final native long g_variant_get_uint64(long gvariant);

/**
 * @param gvariant cast=(GVariant *)
 * @param length cast=(gsize *)
 * @category gdbus
 */
public static final native long g_variant_get_string(long gvariant, long[] length);

/**
 * @param gvariant cast=(GVariant *)
 * @category gdbus
 */
public static final native long g_variant_get_type_string(long gvariant);

/**
 * @param gvariant cast=(GVariant *)
 * @param type cast=(const GVariantType *)
 * @category gdbus
 */
public static final native boolean g_variant_is_of_type(long gvariant, byte[] type);

/**
 * @param gvariant cast=(GVariant *)
 * @category gdbus
 */
public static final native long g_variant_n_children(long gvariant);

/**
 * @param value cast=(gboolean)
 * @category gdbus
 */
public static final native long g_variant_new_boolean(boolean value);

/**
 * @param value cast=(gboolean)
 * @category gdbus
 */
public static final native long g_variant_new_double(double value);

/**
 * @param value cast=(guchar)
 * @category gdbus
 */
public static final native long g_variant_new_byte(byte value);

/**
 * @param items cast=(GVariant * const *)
 * @param length cast=(gsize)
 * @category gdbus
 */
public static final native long g_variant_new_tuple(long [] items, long length);

/**
 * @param string cast=(const gchar *)
 * @category gdbus
 */
public static final native long g_variant_new_string(byte[] string);

/**
 * @param string cast=(const gchar *)
 * @category gdbus
 */
public static final native long g_variant_new_string(long string);

/**
 * @param value cast=(GVariant *)
 * @category gdbus
 */
public static final native void g_variant_unref(long value);

/**
 * @param object cast=(GObject *)
 */
public static final native long g_object_ref_sink(long object);

/* GDateTime */
/**
 * @param dateTime cast=(GDateTime *)
 * @param year cast=(gint *)
 * @param month cast=(gint *)
 * @param day cast=(gint *)
 */
public static final native void g_date_time_get_ymd(long dateTime, int[] year, int[] month, int[] day);
/**
 * Ranges:
 * year must be between 1 - 9999,
 * month must be between 1 - 12,
 * day must be between 1 and 28, 29, 30, 31,
 * hour must be between 0 - 23,
 * minute must be between 0 - 59,
 * seconds must be between 0.0 - 60.0
 *
 * @param year cast=(gint)
 * @param month cast=(gint)
 * @param day cast=(gint)
 * @param hour cast=(gint)
 * @param minute cast=(gint)
 * @param seconds cast=(gdouble)
 */
public static final native long g_date_time_new_local(int year, int month, int day, int hour, int minute, double seconds);
/** @param datetime cast=(GDateTime *) */
public static final native void g_date_time_unref(long datetime);

/** @param file cast=(GFile *) */
public static final native long g_file_get_path(long file);


/* GMenu */
public static final native long g_menu_new();
/**
 * @param label cast=(const gchar *)
 * @param submenu cast=(GMenuModel *)
 */
public static final native long g_menu_item_new_submenu(byte[] label, long submenu);
/**
 * @param label cast=(const gchar *)
 * @param section cast=(GMenuModel *)
 */
public static final native long g_menu_item_new_section(byte[] label, long section);
/**
 * @param label cast=(const gchar *)
 * @param detailed_action cast=(const gchar *)
 */
public static final native long g_menu_item_new(byte[] label, byte[] detailed_action);
/**
 * @param menu_item cast=(GMenuItem *)
 * @param submenu cast=(GMenuModel *)
 */
public static final native void g_menu_item_set_submenu(long menu_item, long submenu);
/**
 * @param menu cast=(GMenu *)
 * @param item cast=(GMenuItem *)
 */
public static final native void g_menu_insert_item(long menu, int position, long item);
/** @param menu cast=(GMenu *) */
public static final native void g_menu_remove(long menu, int position);
/**
 * @param menu_item cast=(GMenuItem *)
 * @param label cast=(const gchar *)
 */
public static final native void g_menu_item_set_label(long menu_item, byte[] label);
/**
 * @param menu_item cast=(GMenuItem *)
 * @param attribute cast=(const gchar *)
 * @param format_string cast=(const gchar *)
 * @param data cast=(const gchar *)
 */
public static final native void g_menu_item_set_attribute(long menu_item, byte[] attribute, byte[] format_string, long data);

/* GSimpleActionGroup */
public static final native long g_simple_action_group_new();

/* GSimpleAction */
/**
 * @param name cast=(const gchar *)
 * @param parameter_type cast=(const GVariantType *)
 */
public static final native long g_simple_action_new(byte[] name, long parameter_type);
/**
 * @param name cast=(const gchar *)
 * @param parameter_type cast=(const GVariantType *)
 * @param initial_state cast=(GVariant *)
 */
public static final native long g_simple_action_new_stateful(byte[] name, long parameter_type, long initial_state);
/**
 * @param simple_action cast=(GSimpleAction *)
 * @param value cast=(GVariant *)
 */
public static final native void g_simple_action_set_state(long simple_action, long value);
/** @param simple_action cast=(GSimpleAction *) */
public static final native void g_simple_action_set_enabled(long simple_action, boolean enabled);

/* GAction */
/** @param action cast=(GAction *) */
public static final native boolean g_action_get_enabled(long action);
/** @param action cast=(GAction *) */
public static final native long g_action_get_state(long action);

/* GActionMap */
/**
 * @param action_map cast=(GActionMap *)
 * @param action cast=(GAction *)
 */
public static final native void g_action_map_add_action(long action_map, long action);
/**
 * @param action_map cast=(GActionMap *)
 * @param action_name cast=(const gchar *)
 */
public static final native void g_action_map_remove_action(long action_map, byte[] action_name);

/* GListModel */
/** @param list cast=(GListModel *) */
public static final native int g_list_model_get_n_items(long list);
/**
 * @param list cast=(GListModel *)
 * @param position cast=(guint)
 */
public static final native long g_list_model_get_item(long list, int position);

/* GMemoryInputStream */
/**
 * @param data cast=(const void *)
 * @param len cast=(gssize)
 * @param destroy cast=(GDestroyNotify)
 */
public static final native long g_memory_input_stream_new_from_data(long data, long len, long destroy);

}
