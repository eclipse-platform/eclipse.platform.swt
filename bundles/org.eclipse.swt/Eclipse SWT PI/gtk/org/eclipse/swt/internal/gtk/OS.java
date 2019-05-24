/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others. All rights reserved.
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


import org.eclipse.swt.internal.*;

// Common type translation table:
// C   ->  Java
// --------------------
// Primitives:
// int   -> int
// guint -> long   #Reason:
//					c : unsigned int range: 4294967295
//                  java : int range      : 2147483647 (less than c unsigned int)
//                  Java : long range: 9,223,372,036,854,775,807
//				    // Note: Not to be used for pointers.
//
// gint* -> int[]
// boolean   -> int  ex setenv
// gboolean  -> boolean
//
// Pointers:
// gpointer -> long
// void *   -> long
//
// Strings:
// gchar *      -> long
// const char * -> byte[]  ex setenv
// const gchar* -> byte[]  ex g_log_remove_handler
//
// Special types:
// GQuark -> int
// GError ** -> long []  ex g_filename_to_uri


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
		String gtk4 = getEnvironmentalVariable (propertyName);
		if (gtk4 != null && gtk4.equals("1")) {
			try {
				Library.loadLibrary("swt-pi4");
			} catch (Throwable e) {
				Library.loadLibrary("swt-pi3");
			}
		} else {
			try {
				Library.loadLibrary("swt-pi3");
			} catch (Throwable e) {
				Library.loadLibrary("swt-pi4");
			}
		}
		cachejvmptr();
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
	public static final int G_FILE_TEST_IS_DIR = 1 << 2;
	public static final int G_FILE_TEST_IS_EXECUTABLE = 1 << 3;
	public static final int G_SIGNAL_MATCH_FUNC = 1 << 3;
	public static final int G_SIGNAL_MATCH_DATA = 1 << 4;
	public static final int G_SIGNAL_MATCH_ID = 1 << 0;
	public static final int G_LOG_FLAG_FATAL = 0x2;
	public static final int G_LOG_FLAG_RECURSION = 0x1;
	public static final int G_LOG_LEVEL_MASK = 0xfffffffc;
	public static final int G_APP_INFO_CREATE_NONE = 0;
	public static final int G_APP_INFO_CREATE_NEEDS_TERMINAL = (1 << 0);
	public static final int G_APP_INFO_CREATE_SUPPORTS_URIS  = (1 << 1);
	public static final int None = 0;
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
	public static final int PANGO_WRAP_WORD = 0;
	public static final int PANGO_WRAP_WORD_CHAR = 2;
	public static final int RTLD_GLOBAL = OS.IsWin32 ? 0 : OS.RTLD_GLOBAL();
	public static final int RTLD_LAZY = OS.IsWin32 ? 0 : OS.RTLD_LAZY();
	public static final int RTLD_MEMBER = 0x00040000;
	public static final int RTLD_NOW = OS.IsWin32 ? 0 : OS.RTLD_NOW();


	/**
	 * GDBus Session types.
	 * @category gdbus */
	public static final int G_BUS_TYPE_STARTER = -1; //An alias for the message bus that activated the process, if any.
	/** @category gdbus */
	public static final int G_BUS_TYPE_NONE = 0;    // Not a message bus.
	/** @category gdbus */
	public static final int G_BUS_TYPE_SYSTEM  = 1; // The system-wide message bus.
	/** @category gdbus */
	public static final int G_BUS_TYPE_SESSION = 2; //The login session message bus.
	/** @category gdbus */
	public static final int G_BUS_NAME_OWNER_FLAGS_NONE = 0; //No flags set.
	/** @category gdbus */
	public static final int G_BUS_NAME_OWNER_FLAGS_ALLOW_REPLACEMENT = (1<<0); //Allow another message bus connection to claim the name.
	/**
	 * If another message bus connection owns the name and have
	 * specified #G_BUS_NAME_OWNER_FLAGS_ALLOW_REPLACEMENT, then take the name from the other connection.
	 * @category gdbus */
	public static final int G_BUS_NAME_OWNER_FLAGS_REPLACE = (1<<1);

	// Proxy flags found here: https://developer.gnome.org/gio/stable/GDBusProxy.html#GDBusProxyFlags
	public static final int G_DBUS_PROXY_FLAGS_NONE = 0;
	public static final int G_DBUS_PROXY_FLAGS_DO_NOT_LOAD_PROPERTIES = 1;
	public static final int G_DBUS_PROXY_FLAGS_DO_NOT_CONNECT_SIGNALS = 2;
	public static final int G_DBUS_PROXY_FLAGS_DO_NOT_AUTO_START = 3;
	public static final int G_DBUS_PROXY_FLAGS_GET_INVALIDATED_PROPERTIES = 4;
	public static final int G_DBUS_PROXY_FLAGS_DO_NOT_AUTO_START_AT_CONSTRUCTION = 5;

	public static final int G_DBUS_CALL_FLAGS_NONE = 0;
	public static final int G_DBUS_CALL_FLAGS_NO_AUTO_START = (1<<0);

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
	public static final byte[] accel_closures_changed = ascii("accel-closures-changed");		// Gtk2,3,4
	public static final byte[] activate = ascii("activate");	// ?
	public static final byte[] angle_changed = ascii("angle_changed");	// Gtk3/4, Guesture related.
	public static final byte[] backspace = ascii("backspace");
	public static final byte[] begin = ascii("begin");
	public static final byte[] button_press_event = ascii("button-press-event");
	public static final byte[] button_release_event = ascii("button-release-event");
	public static final byte[] changed = ascii("changed");
	public static final byte[] change_current_page = ascii("change-current-page");
	public static final byte[] change_value = ascii("change-value");
	public static final byte[] clicked = ascii("clicked");
	public static final byte[] close_request = ascii("close-request");
	public static final byte[] commit = ascii("commit");
	public static final byte[] configure_event = ascii("configure-event");
	public static final byte[] copy_clipboard = ascii("copy-clipboard");
	public static final byte[] cut_clipboard = ascii("cut-clipboard");
	public static final byte[] create_menu_proxy = ascii("create-menu-proxy");
	public static final byte[] destroy = ascii("destroy");
	public static final byte[] delete_event = ascii("delete-event");
	public static final byte[] delete_from_cursor = ascii("delete-from-cursor");
	public static final byte[] day_selected = ascii("day-selected");
	public static final byte[] day_selected_double_click = ascii("day-selected-double-click");
	public static final byte[] decelerate= ascii("decelerate");
	public static final byte[] delete_range = ascii("delete-range");
	public static final byte[] delete_text = ascii("delete-text");
	public static final byte[] direction_changed = ascii("direction-changed");
	public static final byte[] drag_begin = ascii("drag_begin");
	public static final byte[] drag_data_delete = ascii("drag_data_delete");
	public static final byte[] drag_data_get = ascii("drag_data_get");
	public static final byte[] drag_data_received = ascii("drag_data_received");
	public static final byte[] drag_drop = ascii("drag_drop");
	public static final byte[] drag_end = ascii("drag_end");
	public static final byte[] drag_leave = ascii("drag_leave");
	public static final byte[] drag_motion = ascii("drag_motion");
	public static final byte[] draw = ascii("draw");
	public static final byte[] end = ascii("end");
	public static final byte[] enter_notify_event = ascii("enter-notify-event");
	public static final byte[] enter = ascii("enter");
	public static final byte[] event = ascii("event");
	public static final byte[] event_after = ascii("event-after");
	public static final byte[] expand_collapse_cursor_row = ascii("expand-collapse-cursor-row");
	public static final byte[] expose_event = ascii("expose-event");
	public static final byte[] focus = ascii("focus");
	public static final byte[] focus_in_event = ascii("focus-in-event");
	public static final byte[] focus_in = ascii("focus-in");
	public static final byte[] focus_out_event = ascii("focus-out-event");
	public static final byte[] focus_out = ascii("focus-out");
	public static final byte[] grab_focus = ascii("grab-focus");
	public static final byte[] hide = ascii("hide");
	public static final byte[] icon_release = ascii("icon-release");
	public static final byte[] im_update = ascii("im-update");
	public static final byte[] input = ascii("input");
	public static final byte[] insert_text = ascii("insert-text");
	public static final byte[] key_press_event = ascii("key-press-event");
	public static final byte[] key_release_event = ascii("key-release-event");
	public static final byte[] key_pressed = ascii("key-pressed");
	public static final byte[] key_released = ascii("key-released");
	public static final byte[] keys_changed = ascii("keys-changed");
	public static final byte[] leave_notify_event = ascii("leave-notify-event");
	public static final byte[] leave = ascii("leave");
	public static final byte[] link_color = ascii("link-color");
	public static final byte[] map = ascii("map");
	public static final byte[] map_event = ascii("map-event");
	public static final byte[] mnemonic_activate = ascii("mnemonic-activate");
	public static final byte[] month_changed = ascii("month-changed");
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
	public static final byte[] property_notify_event = ascii("property-notify-event");
	public static final byte[] realize = ascii("realize");
	public static final byte[] row_activated = ascii("row-activated");
	public static final byte[] row_changed = ascii("row-changed");
	public static final byte[] row_has_child_toggled = ascii("row-has-child-toggled");
	public static final byte[] row_inserted = ascii("row-inserted");
	public static final byte[] row_deleted = ascii("row-deleted");
	public static final byte[] scale_changed = ascii("scale-changed");
	public static final byte[] scroll_child = ascii("scroll-child");
	public static final byte[] scroll_event = ascii("scroll-event");
	public static final byte[] scroll = ascii("scroll");
	public static final byte[] scroll_begin = ascii("scroll-begin");
	public static final byte[] scroll_end = ascii("scroll-end");
	public static final byte[] select = ascii("select");
	public static final byte[] selection_done = ascii("selection-done");
	public static final byte[] show = ascii("show");
	public static final byte[] show_help = ascii("show-help");
	public static final byte[] size_allocate = ascii("size-allocate");
	public static final byte[] size_request = ascii("size-request");
	public static final byte[] start_interactive_search = ascii("start-interactive-search");
	public static final byte[] style_updated = ascii("style-updated");
	public static final byte[] swipe = ascii("swipe");
	public static final byte[] switch_page = ascii("switch-page");
	public static final byte[] test_collapse_row = ascii("test-collapse-row");
	public static final byte[] test_expand_row = ascii("test-expand-row");
	public static final byte[] toggled = ascii("toggled");
	public static final byte[] unmap = ascii("unmap");
	public static final byte[] unmap_event = ascii("unmap-event");
	public static final byte[] unrealize = ascii("unrealize");
	public static final byte[] value_changed = ascii("value-changed");
	public static final byte[] window_state_event = ascii("window-state-event");
	public static final byte[] notify_state = ascii("notify::state");

	/** Properties */
	public static final byte[] active = ascii("active");
	public static final byte[] background_gdk = ascii("background-gdk");
	public static final byte[] background_rgba = ascii("background-rgba");
	public static final byte[] button_relief = ascii("button-relief");
	public static final byte[] cell_background_rgba = ascii("cell-background-rgba");
	public static final byte[] default_border = ascii("default-border");
	public static final byte[] expander_size = ascii("expander-size");
	public static final byte[] fixed_height_mode = ascii("fixed-height-mode");
	public static final byte[] focus_line_width = ascii("focus-line-width");
	public static final byte[] focus_padding = ascii("focus-padding");
	public static final byte[] font_desc = ascii("font-desc");
	public static final byte[] foreground_gdk = ascii("foreground-gdk");
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
	public static final native void _cachejvmptr ();
	/** Cache the JVM pointer so that it's usable in other implementations. */
	public static final void cachejvmptr() {
		// See bug 521487.
		lock.lock();
		try {
			_cachejvmptr();
		} finally {
			lock.unlock();
		}
	}

	/** @category custom */
	public static final native void _swt_debug_on_fatal_warnings();
	/** Add ability to debug gtk warnings for SWT snippets via SWT_FATAL_WARNINGS=1
	 * env variable. Please see Eclipse bug 471477 */
	public static final void swt_debug_on_fatal_warnings() {
		lock.lock();
		try {
			_swt_debug_on_fatal_warnings ();
		} finally {
			lock.unlock();
		}
	}

	/** @category custom */
	public static final native long _swt_fixed_get_type();
	public static final long swt_fixed_get_type() {
		lock.lock();
		try {
			return _swt_fixed_get_type();
		} finally {
			lock.unlock();
		}
	}

	/** @category custom */
	public static final native long _swt_fixed_accessible_get_type();
	public static final long swt_fixed_accessible_get_type() {
		lock.lock();
		try {
			return _swt_fixed_accessible_get_type();
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param obj cast=(AtkObject*)
	 * @param is_native cast=(gboolean)
	 * @param to_map cast=(GtkWidget *)
	 * @category custom
	 */
	public static final native void _swt_fixed_accessible_register_accessible(long obj, boolean is_native, long to_map);
	public static final void swt_fixed_accessible_register_accessible(long obj, boolean is_native, long to_map) {
		lock.lock();
		try {
			_swt_fixed_accessible_register_accessible(obj, is_native, to_map);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param fixed cast=(SwtFixed*)
	 * @param widget cast=(GtkWidget*)
	 * @param sibling cast=(GtkWidget*)
	 * @category custom
	 */
	public static final native void _swt_fixed_restack(long fixed, long widget, long sibling, boolean above);
	public static final void swt_fixed_restack(long fixed, long widget, long sibling, boolean above) {
		lock.lock();
		try {
			_swt_fixed_restack(fixed, widget, sibling, above);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param fixed cast=(SwtFixed*)
	 * @param widget cast=(GtkWidget*)
	 * @category custom
	 */
	public static final native void _swt_fixed_move(long fixed, long widget, int x, int y);
	public static final void swt_fixed_move(long fixed, long widget, int x, int y) {
		lock.lock();
		try {
			_swt_fixed_move(fixed, widget, x, y);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param fixed cast=(SwtFixed*)
	 * @param widget cast=(GtkWidget*)
	 * @category custom
	 */
	public static final native void _swt_fixed_resize(long fixed, long widget, int width, int height);
	public static final void swt_fixed_resize(long fixed, long widget, int width, int height) {
		lock.lock();
		try {
			_swt_fixed_resize(fixed, widget, width, height);
		} finally {
			lock.unlock();
		}
	}

	/** @param str cast=(const gchar *)
	 * @category custom
	 */
	public static final native long _g_utf16_offset_to_pointer(long str, long offset);
	/** Custom version of g_utf8_pointer_to_offset */
	public static final long g_utf16_offset_to_pointer(long str, long offset) {
		lock.lock();
		try {
			return _g_utf16_offset_to_pointer(str, offset);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * @param str cast=(const gchar *)
	 * @param pos cast=(const gchar *)
	 * @category custom
	 */
	public static final native long _g_utf16_pointer_to_offset(long str, long pos);
	/** Custom version of g_utf8_pointer_to_offset */
	public static final long g_utf16_pointer_to_offset(long str, long pos) {
		lock.lock();
		try {
			return _g_utf16_pointer_to_offset(str, pos);
		} finally {
			lock.unlock();
		}
	}
	/** @param str cast=(const gchar *)
	 * @category custom
	 */
	public static final native long _g_utf16_strlen(long str, long max);
	/** custom version of g_utf8 for 16 bit */
	public static final long g_utf16_strlen(long str, long max) {
		lock.lock();
		try {
			return _g_utf16_strlen(str, max);
		} finally {
			lock.unlock();
		}
	}
	/** @param str cast=(const gchar *)
	 * @category custom
	 */
	public static final native long _g_utf8_offset_to_utf16_offset(long str, long offset);
	/** custom version of g_utf8 for 16 bit */
	public static final long g_utf8_offset_to_utf16_offset(long str, long offset) {
		lock.lock();
		try {
			return _g_utf8_offset_to_utf16_offset(str, offset);
		} finally {
			lock.unlock();
		}
	}
	/** @param str cast=(const gchar *)
	 * @category custom
	 */
	public static final native long _g_utf16_offset_to_utf8_offset(long str, long offset);
	/** custom version of g_utf8 for 16 bit */
	public static final long g_utf16_offset_to_utf8_offset(long str, long offset) {
		lock.lock();
		try {
			return _g_utf16_offset_to_utf8_offset(str, offset);
		} finally {
			lock.unlock();
		}
	}

	/** CUSTOM_CODE END */






	/**
	 * Gtk has a minimum glib version. (But it's not a 1:1 link, one can have a newer version of glib and older gtk).
	 *
	 * Minimum Glib version requirement of gtk can be found in gtk's 'configure.ac' file, see line 'm4_define([glib_required_version],[2.*.*]).
	 *
	 * For reference:
	 * Gtk3.4  has min version of glib 2.32
	 * Gtk3.6  has min version of glib 2.33.1
	 * Gtk3.8  has min version of glib 2.35.3
	 * Gtk3.10 has min version of glib 2.37.5
	 * Gtk3.12 has min version of glib 2.39.5
	 * Gtk3.14 has min version of glib 2.41.2
	 * Gtk3.16 has min version of glib 2.43.4
	 * Gtk3.18 has min version of glib 2.45.8
	 * Gtk3.20 has min version of glib 2.45.8
	 * Gtk3.22 has min version of glib 2.49.4
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
	public static final String GTK_THEME_NAME;
	/**
	 * True if GTK_THEME_SET is true, and if the dark variant was
	 * specified via the GTK_THEME environment variable.
	 */
	public static final boolean GTK_THEME_DARK;

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
		if (menuLocationCheck != null && menuLocationCheck.equals("1")) {
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
		boolean gtkThemeDark = false;
		if (gtkThemeCheck != null && !gtkThemeCheck.isEmpty()) {
			gtkThemeSet = true;
			gtkThemeDark = gtkThemeCheck.contains(":dark") ? true : false;
			String [] themeNameSplit = gtkThemeCheck.split(":");
			gtkThemeName = themeNameSplit[0];
		}
		GTK_THEME_SET = gtkThemeSet;
		GTK_THEME_NAME = gtkThemeName;
		GTK_THEME_DARK = gtkThemeDark;

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

public static boolean isX11 () {
	return OS.GDK_WINDOWING_X11() && GDK.GDK_IS_X11_DISPLAY(GDK.gdk_display_get_default());
}

/** 64 bit */
public static final native int GInterfaceInfo_sizeof ();
public static final native int GPollFD_sizeof ();
public static final native int GTypeInfo_sizeof ();
public static final native int GTypeQuery_sizeof ();
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
public static final int Above = 0;
public static final int Below = 1;
public static final int ButtonRelease = 5;
public static final int CurrentTime = 0;
public static final int CWSibling = 0x20;
public static final int CWStackMode = 0x40;
public static final int EnterNotify = 7;
public static final int Expose = 12;
public static final int FocusChangeMask = 1 << 21;
public static final int FocusIn = 9;
public static final int FocusOut = 10;
public static final int GraphicsExpose = 13;
public static final int NoExpose = 14;
public static final int ExposureMask = 1 << 15;
public static final long NoEventMask = 0;
public static final int NotifyNormal = 0;
public static final int NotifyGrab = 1;
public static final int NotifyHint = 1;
public static final int NotifyUngrab = 2;
public static final int NotifyWhileGrabbed = 3;
public static final int NotifyAncestor = 0;
public static final int NotifyVirtual = 1;
public static final int NotifyNonlinear = 3;
public static final int NotifyNonlinearVirtual = 4;
public static final int NotifyPointer = 5;
public static final int RevertToParent = 2;
public static final native int _Call(long proc, long arg1, long arg2);
public static final int Call(long proc, long arg1, long arg2) {
	lock.lock();
	try {
		return _Call(proc, arg1, arg2);
	} finally {
		lock.unlock();
	}
}
public static final native long _call (long function, long arg0, long arg1, long arg2, long arg3, long arg4, long arg5, long arg6);
public static final long call (long function, long arg0, long arg1, long arg2, long arg3, long arg4, long arg5, long arg6) {
	lock.lock();
	try {
		return _call(function, arg0, arg1, arg2, arg3, arg4, arg5, arg6);
	} finally {
		lock.unlock();
	}
}
public static final native long _call (long function, long arg0, long arg1, long arg2, long arg3);
public static final long call (long function, long arg0, long arg1, long arg2, long arg3) {
	lock.lock();
	try {
		return _call(function, arg0, arg1, arg2, arg3);
	} finally {
		lock.unlock();
	}
}
public static final native long _call (long function, long arg0, long arg1, long arg2, long arg3, long arg4, long arg5);
public static final long call (long function, long arg0, long arg1, long arg2, long arg3, long arg4, long arg5) {
	lock.lock();
	try {
		return _call(function, arg0, arg1, arg2, arg3, arg4, arg5);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param event_return cast=(XEvent *)
 * @param predicate cast=(Bool (*)())
 * @param arg cast=(XPointer)
 */
public static final native boolean _XCheckIfEvent(long display, long event_return, long predicate, long arg);
public static final boolean XCheckIfEvent(long display, long event_return, long predicate, long arg) {
	lock.lock();
	try {
		return _XCheckIfEvent(display, event_return, predicate, arg);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native int _XDefaultScreen(long display);
public static final int XDefaultScreen(long display) {
	lock.lock();
	try {
		return _XDefaultScreen(display);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native long _XDefaultRootWindow(long display);
public static final long XDefaultRootWindow(long display) {
	lock.lock();
	try {
		return _XDefaultRootWindow(display);
	} finally {
		lock.unlock();
	}
}
/** @param address cast=(void *) */
public static final native void _XFree(long address);
public static final void XFree(long address) {
	lock.lock();
	try {
		_XFree(address);
	} finally {
		lock.unlock();
	}
}

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
public static final native int _XQueryPointer(long display, long w, long [] root_return, long [] child_return, int[] root_x_return, int[] root_y_return, int[] win_x_return, int[] win_y_return, int[] mask_return);
public static final int XQueryPointer(long display, long w, long [] root_return, long [] child_return, int[] root_x_return, int[] root_y_return, int[] win_x_return, int[] win_y_return, int[] mask_return) {
	lock.lock();
	try {
		return _XQueryPointer(display, w, root_return, child_return, root_x_return, root_y_return, win_x_return, win_y_return, mask_return);
	} finally {
		lock.unlock();
	}
}
/** @param handler cast=(XIOErrorHandler) */
public static final native long _XSetIOErrorHandler(long handler);
public static final long XSetIOErrorHandler(long handler) {
	lock.lock();
	try {
		return _XSetIOErrorHandler(handler);
	} finally {
		lock.unlock();
	}
}
/** @param handler cast=(XErrorHandler) */
public static final native long _XSetErrorHandler(long handler);
public static final long XSetErrorHandler(long handler) {
	lock.lock();
	try {
		return _XSetErrorHandler(handler);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param window cast=(Window)
 */
public static final native int _XSetInputFocus(long display, long window, int revert, int time);
public static final int XSetInputFocus(long display, long window, int revert, int time) {
	lock.lock();
	try {
		return _XSetInputFocus(display, window, revert, time);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param w cast=(Window)
 * @param prop_window cast=(Window)
 */
public static final native int _XSetTransientForHint(long display, long w, long prop_window);
public static final int XSetTransientForHint(long display, long w, long prop_window) {
	lock.lock();
	try {
		return _XSetTransientForHint(display, w, prop_window);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native long _XSynchronize(long display, boolean onoff);
public static final long XSynchronize(long display, boolean onoff) {
	lock.lock();
	try {
		return _XSynchronize(display, onoff);
	} finally {
		lock.unlock();
	}
}
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

/** @method flags=const */
public static final native int RTLD_GLOBAL();
/** @method flags=const */
public static final native int RTLD_NOW();
/** @method flags=const */
public static final native int RTLD_LAZY();


/** Natives */
public static final native int Call (long func, long arg0, int arg1, int arg2);
public static final native long Call (long func, long arg0, int arg1, long arg2);
public static final native long _G_OBJECT_CLASS (long klass);
public static final long G_OBJECT_CLASS (long klass) {
	lock.lock();
	try {
		return _G_OBJECT_CLASS(klass);
	} finally {
		lock.unlock();
	}
}
public static final native long _G_OBJECT_GET_CLASS (long object);
public static final long G_OBJECT_GET_CLASS (long object) {
	lock.lock();
	try {
		return _G_OBJECT_GET_CLASS(object);
	} finally {
		lock.unlock();
	}
}
public static final native long _G_OBJECT_TYPE_NAME (long object);
public static final long G_OBJECT_TYPE_NAME (long object) {
	lock.lock();
	try {
		return _G_OBJECT_TYPE_NAME(object);
	} finally {
		lock.unlock();
	}
}

public static final native boolean _G_TYPE_CHECK_INSTANCE_TYPE (long instance, long type);
/**
 * Note: G_TYPE_CHECK_INSTANCE_TYPE is not a good way to check for instance type,
 * The C-Macro doesn't seem to work reliably in the context of being invoked from Java
 * via JNI on a dynamically loaded library.
 * But webkit1 development has halted and it's not worth the effort to change this.
 * I.e, kept for legacy reason but don't use this. Instead, to identify type, use user_data. (see Webkit proc3 as example).
 */
public static final boolean G_TYPE_CHECK_INSTANCE_TYPE (long instance, long type) {
	lock.lock();
	try {
		return _G_TYPE_CHECK_INSTANCE_TYPE(instance, type);
	} finally {
		lock.unlock();
	}
}
/** @method flags=const */
public static final native long G_TYPE_BOOLEAN();
/** @method flags=const */
public static final native long G_TYPE_DOUBLE();
/** @method flags=const */
public static final native long G_TYPE_FLOAT();
/** @method flags=const */
public static final native long G_TYPE_INT();
/** @method flags=const */
public static final native long G_TYPE_INT64();
public static final native long G_VALUE_TYPE(long value);
public static final native long _G_OBJECT_TYPE (long instance);
public static final long G_OBJECT_TYPE (long instance) {
	lock.lock();
	try {
		return _G_OBJECT_TYPE(instance);
	} finally {
		lock.unlock();
	}
}
/** @method flags=const */
public static final native long _G_TYPE_STRING();
public static final long G_TYPE_STRING() {
	lock.lock();
	try {
		return _G_TYPE_STRING();
	} finally {
		lock.unlock();
	}
}
public static final native int _PANGO_PIXELS(int dimension);
public static final int PANGO_PIXELS(int dimension) {
	lock.lock();
	try {
		return _PANGO_PIXELS(dimension);
	} finally {
		lock.unlock();
	}
}
/** @method flags=const */
public static final native long _PANGO_TYPE_FONT_DESCRIPTION();
public static final long PANGO_TYPE_FONT_DESCRIPTION() {
	lock.lock();
	try {
		return _PANGO_TYPE_FONT_DESCRIPTION();
	} finally {
		lock.unlock();
	}
}
/** @method flags=const */
public static final native long _PANGO_TYPE_FONT_FAMILY();
public static final long PANGO_TYPE_FONT_FAMILY() {
	lock.lock();
	try {
		return _PANGO_TYPE_FONT_FAMILY();
	} finally {
		lock.unlock();
	}
}
/** @method flags=const */
public static final native long _PANGO_TYPE_FONT_FACE();
public static final long PANGO_TYPE_FONT_FACE() {
	lock.lock();
	try {
		return _PANGO_TYPE_FONT_FACE();
	} finally {
		lock.unlock();
	}
}
/** @method flags=const */
public static final native long _PANGO_TYPE_LAYOUT();
public static final long PANGO_TYPE_LAYOUT() {
	lock.lock();
	try {
		return _PANGO_TYPE_LAYOUT();
	} finally {
		lock.unlock();
	}
}
/** @param filename cast=(const char *) */
public static final native long _dlopen(byte[] filename, int flag);
public static final long dlopen(byte[] filename, int flag) {
	lock.lock();
	try {
		return _dlopen(filename, flag);
	} finally {
		lock.unlock();
	}
}
/**
 * @param commandline cast=(gchar *)
 * @param applName cast=(gchar *)
 * @param flags cast=(GAppInfoCreateFlags)
 * @param error cast=(GError **)
 */
public static final native long _g_app_info_create_from_commandline(byte[] commandline, byte[] applName, long flags, long error);
public static final long g_app_info_create_from_commandline(byte[] commandline, byte[] applName, long flags, long error) {
	lock.lock();
	try {
		return _g_app_info_create_from_commandline(commandline, applName, flags, error);
	} finally {
		lock.unlock();
	}
}
public static final native long _g_app_info_get_all();
public static final long g_app_info_get_all() {
	lock.lock();
	try {
		return _g_app_info_get_all();
	} finally {
		lock.unlock();
	}
}
/**
 * @param appInfo cast=(GAppInfo *)
 */
public static final native long _g_app_info_get_executable(long appInfo);
public static final long g_app_info_get_executable(long appInfo) {
	lock.lock();
	try {
		return _g_app_info_get_executable(appInfo);
	} finally {
		lock.unlock();
	}
}
/**
 * @param appInfo cast=(GAppInfo *)
 */
public static final native long _g_app_info_get_icon(long appInfo);
public static final long g_app_info_get_icon(long appInfo) {
	lock.lock();
	try {
		return _g_app_info_get_icon(appInfo);
	} finally {
		lock.unlock();
	}
}
/**
 * @param appInfo cast=(GAppInfo *)
 */
public static final native long _g_app_info_get_name(long appInfo);
public static final long g_app_info_get_name(long appInfo) {
	lock.lock();
	try {
		return _g_app_info_get_name(appInfo);
	} finally {
		lock.unlock();
	}
}
/**
 * @param appInfo cast=(GAppInfo *)
 * @param list cast=(GList *)
 * @param launchContext cast=(GAppLaunchContext *)
 * @param error cast=(GError **)
 */
public static final native boolean _g_app_info_launch(long appInfo, long list, long launchContext, long error);
public static final boolean g_app_info_launch(long appInfo, long list, long launchContext, long error) {
	lock.lock();
	try {
		return _g_app_info_launch(appInfo, list, launchContext, error);
	} finally {
		lock.unlock();
	}
}
/**
 * @param mimeType cast=(gchar *)
 * @param mustSupportURIs cast=(gboolean)
 */
public static final native long _g_app_info_get_default_for_type(byte[] mimeType, boolean mustSupportURIs);
public static final long g_app_info_get_default_for_type(byte[] mimeType, boolean mustSupportURIs) {
	lock.lock();
	try {
		return _g_app_info_get_default_for_type(mimeType, mustSupportURIs);
	} finally {
		lock.unlock();
	}
}
/**
 * @param uri cast=(char *)
 * @param launchContext cast=(GAppLaunchContext *)
 * @param error cast=(GError **)
 */
public static final native boolean _g_app_info_launch_default_for_uri(long uri, long launchContext, long error);
public static final boolean g_app_info_launch_default_for_uri(long uri, long launchContext, long error) {
	lock.lock();
	try {
		return _g_app_info_launch_default_for_uri(uri, launchContext, error);
	} finally {
		lock.unlock();
	}
}
/**
 * @param appInfo cast=(GAppInfo *)
 */
public static final native boolean _g_app_info_should_show(long appInfo);
public static final boolean g_app_info_should_show(long appInfo) {
	lock.lock();
	try {
		return _g_app_info_should_show(appInfo);
	} finally {
		lock.unlock();
	}
}
/**
 * @param appInfo cast=(GAppInfo *)
 */
public static final native boolean _g_app_info_supports_uris(long appInfo);
public static final boolean g_app_info_supports_uris(long appInfo) {
	lock.lock();
	try {
		return _g_app_info_supports_uris(appInfo);
	} finally {
		lock.unlock();
	}
}
/**
 * @param error cast=(GError *)
 */
public static final native long _g_error_get_message (long error);
public static final long g_error_get_message (long error) {
	lock.lock();
	try {
		return _g_error_get_message (error);
	} finally {
		lock.unlock();
	}
}

/**
 * @param gerror cast=(GError *)
 */
public static final native void _g_error_free(long gerror);
public static final void g_error_free(long gerror) {
	lock.lock();
	try {
		_g_error_free(gerror);
	} finally {
		lock.unlock();
	}
}

/**
 * @param type1 cast=(gchar *)
 * @param type2 cast=(gchar *)
 */
public static final native boolean _g_content_type_equals(long type1, byte[] type2);
public static final boolean g_content_type_equals(long type1, byte[] type2) {
	lock.lock();
	try {
		return _g_content_type_equals(type1, type2);
	} finally {
		lock.unlock();
	}
}
/**
 * @param type cast=(gchar *)
 * @param supertype cast=(gchar *)
 */
public static final native boolean _g_content_type_is_a(long type, byte[] supertype);
public static final boolean g_content_type_is_a(long type, byte[] supertype) {
	lock.lock();
	try {
		return _g_content_type_is_a(type, supertype);
	} finally {
		lock.unlock();
	}
}
/**
 * @param info cast=(GFileInfo *)
 */
public static final native long _g_file_info_get_content_type (long info);
public static final long g_file_info_get_content_type (long info) {
	lock.lock();
	try {
		return _g_file_info_get_content_type (info);
	} finally {
		lock.unlock();
	}
}
/**
 * @param file cast=(GFile *)
 */
public static final native long _g_file_get_uri(long file);
public static final long g_file_get_uri (long file) {
	lock.lock();
	try {
		return _g_file_get_uri(file);
	} finally {
		lock.unlock();
	}
}
/** @param fileName cast=(const char *) */
public static final native long _g_file_new_for_path(byte[] fileName);
public static final long g_file_new_for_path(byte[] fileName) {
	lock.lock();
	try {
		return _g_file_new_for_path(fileName);
	} finally {
		lock.unlock();
	}
}
/**
 * @param fileName cast=(const char *)
 */
public static final native long _g_file_new_for_commandline_arg(byte[] fileName);
public static final long g_file_new_for_commandline_arg(byte[] fileName) {
	lock.lock();
	try {
		return _g_file_new_for_commandline_arg(fileName);
	} finally {
		lock.unlock();
	}
}
/** @param fileName cast=(const char *) */
public static final native long _g_file_new_for_uri(byte[] fileName);
public static final long g_file_new_for_uri(byte[] fileName) {
	lock.lock();
	try {
		return _g_file_new_for_uri(fileName);
	} finally {
		lock.unlock();
	}
}
/**
 * @param file cast=(GFile *)
 * @param attributes cast=(const char *)
 * @param flags cast=(GFileQueryInfoFlags)
 * @param cancellable cast=(GCancellable *)
 * @param error cast=(GError **)
 */
public static final native long _g_file_query_info  (long file, byte[] attributes, long flags, long cancellable, long error);
public static final long g_file_query_info  (long file,byte[] attributes, long flags, long cancellable, long error) {
	lock.lock();
	try {
		return _g_file_query_info (file, attributes, flags, cancellable, error);
	} finally {
		lock.unlock();
	}
}
/**
 * @param file cast=(const gchar *)
 * @param test cast=(GFileTest)
 */
public static final native boolean /*long*/ _g_file_test(byte[] file, int test);
public static final boolean /*long*/ g_file_test(byte[] file, int test) {
	lock.lock();
	try {
		return _g_file_test(file, test);
	} finally {
		lock.unlock();
	}
}
/** @param icon cast=(GIcon *) */
public static final native long _g_icon_to_string(long icon);
public static final long g_icon_to_string (long icon) {
	lock.lock();
	try {
		return _g_icon_to_string(icon);
	} finally {
		lock.unlock();
	}
}
/**
 * @param str cast=(const gchar *)
 * @param error cast=(GError **)
 */
public static final native long _g_icon_new_for_string(byte[] str, long error[]);
public static final long g_icon_new_for_string (byte[] str, long error[]) {
	lock.lock();
	try {
		return _g_icon_new_for_string(str, error);
	} finally {
		lock.unlock();
	}
}
/**
 * @param signal_id cast=(guint)
 * @param detail cast=(GQuark)
 * @param hook_func cast=(GSignalEmissionHook)
 * @param hook_data cast=(gpointer)
 * @param data_destroy cast=(GDestroyNotify)
 */
public static final native long _g_signal_add_emission_hook(int signal_id, int detail, long hook_func, long hook_data, long data_destroy);
public static final long g_signal_add_emission_hook(int signal_id, int detail, long hook_func, long hook_data, long data_destroy) {
	lock.lock();
	try {
		return _g_signal_add_emission_hook(signal_id, detail, hook_func, hook_data, data_destroy);
	} finally {
		lock.unlock();
	}
}
/**
 * @param signal_id cast=(guint)
 * @param hook_id cast=(gulong)
 */
public static final native void _g_signal_remove_emission_hook(int signal_id, long hook_id);
public static final void g_signal_remove_emission_hook(int signal_id, long hook_id) {
	lock.lock();
	try {
		 _g_signal_remove_emission_hook (signal_id, hook_id);
	} finally {
		lock.unlock();
	}
}
/**
 * @param callback_func cast=(GCallback)
 * @param user_data cast=(gpointer)
 * @param destroy_data cast=(GClosureNotify)
 */
public static final native long _g_cclosure_new(long callback_func, long user_data, long destroy_data);
public static final long g_cclosure_new(long callback_func, long user_data, long destroy_data) {
	lock.lock();
	try {
		return _g_cclosure_new(callback_func, user_data, destroy_data);
	} finally {
		lock.unlock();
	}
}
/** @param closure cast=(GClosure *) */
public static final native long _g_closure_ref(long closure);
public static final long g_closure_ref(long closure) {
	lock.lock();
	try {
		return _g_closure_ref(closure);
	} finally {
		lock.unlock();
	}
}
/** @param closure cast=(GClosure *) */
public static final native void _g_closure_sink(long closure);
public static final void g_closure_sink(long closure) {
	lock.lock();
	try {
		_g_closure_sink(closure);
	} finally {
		lock.unlock();
	}
}
/** @param closure cast=(GClosure *) */
public static final native void _g_closure_unref(long closure);
public static final void g_closure_unref(long closure) {
	lock.lock();
	try {
		_g_closure_unref(closure);
	} finally {
		lock.unlock();
	}
}
/** @param context cast=(GMainContext *) */
public static final native boolean _g_main_context_acquire(long context);
public static final boolean g_main_context_acquire(long context) {
	lock.lock();
	try {
		return _g_main_context_acquire(context);
	} finally {
		lock.unlock();
	}
}
/**
 * @param context cast=(GMainContext *)
 * @param fds cast=(GPollFD *)
 */
public static final native int _g_main_context_check(long context, int max_priority, long fds, int n_fds);
public static final int g_main_context_check(long context, int max_priority, long fds, int n_fds) {
	lock.lock();
	try {
		return _g_main_context_check(context, max_priority, fds, n_fds);
	} finally {
		lock.unlock();
	}
}
public static final native long _g_main_context_default();
public static final long g_main_context_default() {
	lock.lock();
	try {
		return _g_main_context_default();
	} finally {
		lock.unlock();
	}
}
/** @param context cast=(GMainContext *) */
public static final native boolean _g_main_context_iteration(long context, boolean may_block);
public static final boolean g_main_context_iteration(long context, boolean may_block) {
	lock.lock();
	try {
		return _g_main_context_iteration(context, may_block);
	} finally {
		lock.unlock();
	}
}
/** @param context cast=(GMainContext *) */
public static final native long _g_main_context_get_poll_func(long context);
public static final long g_main_context_get_poll_func(long context) {
	lock.lock();
	try {
		return _g_main_context_get_poll_func(context);
	} finally {
		lock.unlock();
	}
}
/**
 * @param context cast=(GMainContext *)
 * @param priority cast=(gint *)
 */
public static final native boolean _g_main_context_prepare(long context, int[] priority);
public static final boolean g_main_context_prepare(long context, int[] priority) {
	lock.lock();
	try {
		return _g_main_context_prepare(context, priority);
	} finally {
		lock.unlock();
	}
}
/**
 * @param context cast=(GMainContext *)
 * @param fds cast=(GPollFD *)
 * @param timeout_ cast=(gint *)
 */
public static final native int _g_main_context_query(long context, int max_priority, int[] timeout_, long fds, int n_fds);
public static final int g_main_context_query(long context, int max_priority, int[] timeout_, long fds, int n_fds) {
	lock.lock();
	try {
		return _g_main_context_query(context, max_priority, timeout_, fds, n_fds);
	} finally {
		lock.unlock();
	}
}
/** @param context cast=(GMainContext *) */
public static final native void _g_main_context_release(long context);
public static final void g_main_context_release(long context) {
	lock.lock();
	try {
		_g_main_context_release(context);
	} finally {
		lock.unlock();
	}
}
/** @param context cast=(GMainContext *) */
public static final native void g_main_context_wakeup(long context);
/**
 * @param opsysstring cast=(const gchar *)
 * @param len cast=(gssize)
 * @param bytes_read cast=(gsize *)
 * @param bytes_written cast=(gsize *)
 * @param error cast=(GError **)
 */
public static final native long _g_filename_to_utf8(long opsysstring, long len, long [] bytes_read, long [] bytes_written, long [] error);
public static final long g_filename_to_utf8(long opsysstring, long len, long [] bytes_read, long [] bytes_written, long [] error) {
	lock.lock();
	try {
		return _g_filename_to_utf8(opsysstring, len, bytes_read, bytes_written, error);
	} finally {
		lock.unlock();
	}
}
/** @param filename cast=(const gchar *) */
public static final native long _g_filename_display_name(long filename);
public static final long g_filename_display_name(long filename) {
	lock.lock();
	try {
		return _g_filename_display_name(filename);
	} finally {
		lock.unlock();
	}
}
/**
 * @param filename cast=(const char *)
 * @param hostname cast=(const char *)
 * @param error cast=(GError **)
 */
public static final native long _g_filename_to_uri(long filename, long hostname, long [] error);
public static final long g_filename_to_uri(long filename, long hostname, long [] error) {
	lock.lock();
	try {
		return _g_filename_to_uri(filename, hostname, error);
	} finally {
		lock.unlock();
	}
}
/**
 * @param opsysstring cast=(const gchar *)
 * @param len cast=(gssize)
 * @param bytes_read cast=(gsize *)
 * @param bytes_written cast=(gsize *)
 * @param error cast=(GError **)
 */
public static final native long _g_filename_from_utf8(long opsysstring, long len,  long [] bytes_read, long [] bytes_written, long [] error);
public static final long g_filename_from_utf8(long opsysstring, long len,  long [] bytes_read, long [] bytes_written, long [] error) {
	lock.lock();
	try {
		return _g_filename_from_utf8(opsysstring, len, bytes_read, bytes_written, error);
	} finally {
		lock.unlock();
	}
}
/**
 * @param uri cast=(const char *)
 * @param hostname cast=(char **)
 * @param error cast=(GError **)
 */
public static final native long _g_filename_from_uri(long uri, long [] hostname, long [] error);
public static final long g_filename_from_uri(long uri, long [] hostname, long [] error) {
	lock.lock();
	try {
		return _g_filename_from_uri(uri, hostname, error);
	} finally {
		lock.unlock();
	}
}
/** @param mem cast=(gpointer) */
// NOTE: Lock is not used, see Bug 546743 and Bug 547093
public static final native void g_free(long mem);
/**
 * @param variable cast=(const gchar *),flags=no_out
 */
public static final native long _g_getenv(byte [] variable);
public static final long g_getenv(byte [] variable) {
	lock.lock();
	try {
		return _g_getenv(variable);
	} finally {
		lock.unlock();
	}
}
/**
 * @param table cast=(GHashTable *)
 */
public static final native long _g_hash_table_get_values(long table);
public static final long g_hash_table_get_values(long table) {
	lock.lock();
	try {
		return _g_hash_table_get_values(table);
	} finally {
		lock.unlock();
	}
}
/**
 * @param function cast=(GSourceFunc)
 * @param data cast=(gpointer)
 */
public static final native int _g_idle_add(long function, long data);
public static final int g_idle_add(long function, long data) {
	lock.lock();
	try {
		return _g_idle_add(function, data);
	} finally {
		lock.unlock();
	}
}
/**
 * @param list cast=(GList *)
 * @param data cast=(gpointer)
 */
public static final native long _g_list_append(long list, long data);
public static final long g_list_append(long list, long data) {
	lock.lock();
	try {
		return _g_list_append(list, data);
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(GList *) */
public static final native long _g_list_data(long list);
public static final long g_list_data(long list) {
	lock.lock();
	try {
		return _g_list_data(list);
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(GList *) */
public static final native void _g_list_free(long list);
public static final void g_list_free(long list) {
	lock.lock();
	try {
		_g_list_free(list);
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(GList *) */
public static final native void _g_list_free_1(long list);
public static final void g_list_free_1(long list) {
	lock.lock();
	try {
		_g_list_free_1(list);
	} finally {
		lock.unlock();
	}
}
/**
 * @param list cast=(GList *)
 */
public static final native long _g_list_last(long list);
public static final long g_list_last(long list) {
	lock.lock();
	try {
		return _g_list_last(list);
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(GList *) */
public static final native int _g_list_length(long list);
public static final int g_list_length(long list) {
	lock.lock();
	try {
		return _g_list_length(list);
	} finally {
		lock.unlock();
	}
}
/**
 * @param list cast=(GList *)
 * @param llist cast=(GList *)
 */
public static final native void _g_list_set_next(long list, long llist);
public static final void g_list_set_next(long list, long llist) {
	lock.lock();
	try {
		_g_list_set_next(list, llist);
	} finally {
		lock.unlock();
	}
}
public static final native long _g_list_next(long list);
public static final long g_list_next(long list) {
	lock.lock();
	try {
		return _g_list_next(list);
	} finally {
		lock.unlock();
	}
}
/**
 * @param list cast=(GList *)
 * @param n cast=(guint)
 */
public static final native long _g_list_nth(long list, int n);
public static final long g_list_nth(long list, int n) {
	lock.lock();
	try {
		return _g_list_nth(list, n);
	} finally {
		lock.unlock();
	}
}
/**
 * @param list cast=(GList *)
 * @param n cast=(guint)
 */
public static final native long _g_list_nth_data(long list, int n);
public static final long g_list_nth_data(long list, int n) {
	lock.lock();
	try {
		return _g_list_nth_data(list, n);
	} finally {
		lock.unlock();
	}
}
/**
 * @param list cast=(GList *)
 * @param data cast=(gpointer)
 */
public static final native long _g_list_prepend(long list, long data);
public static final long g_list_prepend(long list, long data) {
	lock.lock();
	try {
		return _g_list_prepend(list, data);
	} finally {
		lock.unlock();
	}
}
/**
 * @param list cast=(GList *)
 * @param llist cast=(GList *)
 */
public static final native void _g_list_set_previous(long list, long llist);
public static final void g_list_set_previous(long list, long llist) {
	lock.lock();
	try {
		_g_list_set_previous(list, llist);
	} finally {
		lock.unlock();
	}
}
public static final native long _g_list_previous(long list);
public static final long g_list_previous(long list) {
	lock.lock();
	try {
		return _g_list_previous(list);
	} finally {
		lock.unlock();
	}
}
/**
 * @param list cast=(GList *)
 * @param link cast=(GList *)
 */
public static final native long _g_list_remove_link(long list, long link);
public static final long g_list_remove_link(long list, long link) {
	lock.lock();
	try {
		return _g_list_remove_link(list, link);
	} finally {
		lock.unlock();
	}
}
/**
 * @param log_domain cast=(gchar *)
 * @param log_levels cast=(GLogLevelFlags)
 * @param message cast=(gchar *)
 * @param unused_data cast=(gpointer)
 */
public static final native void _g_log_default_handler(long log_domain, int log_levels, long message, long unused_data);
public static final void g_log_default_handler(long log_domain, int log_levels, long message, long unused_data) {
	lock.lock();
	try {
		_g_log_default_handler(log_domain, log_levels, message, unused_data);
	} finally {
		lock.unlock();
	}
}
/**
 * @param log_domain cast=(gchar *),flags=no_out
 * @param handler_id cast=(gint)
 */
public static final native void _g_log_remove_handler(byte[] log_domain, int handler_id);
public static final void g_log_remove_handler(byte[] log_domain, int handler_id) {
	lock.lock();
	try {
		_g_log_remove_handler(log_domain, handler_id);
	} finally {
		lock.unlock();
	}
}
/**
 * @param log_domain cast=(gchar *),flags=no_out
 * @param log_levels cast=(GLogLevelFlags)
 * @param log_func cast=(GLogFunc)
 * @param user_data cast=(gpointer)
 */
public static final native int _g_log_set_handler(byte[] log_domain, int log_levels, long log_func, long user_data);
public static final int g_log_set_handler(byte[] log_domain, int log_levels, long log_func, long user_data) {
	lock.lock();
	try {
		return _g_log_set_handler(log_domain, log_levels, log_func, user_data);
	} finally {
		lock.unlock();
	}
}
/** @param size cast=(gulong) */
public static final native long _g_malloc(long size);
public static final long g_malloc(long size) {
	lock.lock();
	try {
		return _g_malloc(size);
	} finally {
		lock.unlock();
	}
}
/**
 * @param oclass cast=(GObjectClass *)
 * @param property_name cast=(const gchar *)
 */
public static final native long _g_object_class_find_property(long oclass, byte[] property_name);
public static final long g_object_class_find_property(long oclass, byte[] property_name) {
	lock.lock();
	try {
		return _g_object_class_find_property(oclass, property_name);
	} finally {
		lock.unlock();
	}
}
/**
 * @param object cast=(GObject *)
 * @param first_property_name cast=(const gchar *),flags=no_out
 * @param terminator cast=(const gchar *),flags=sentinel
 */
public static final native void _g_object_get(long object, byte[] first_property_name, int[] value, long terminator);
public static final void g_object_get(long object, byte[] first_property_name, int[] value, long terminator) {
	lock.lock();
	try {
		_g_object_get(object, first_property_name, value, terminator);
	} finally {
		lock.unlock();
	}
}
/**
 * @param object cast=(GObject *)
 * @param first_property_name cast=(const gchar *),flags=no_out
 * @param terminator cast=(const gchar *),flags=sentinel
 */
public static final native void _g_object_get(long object, byte[] first_property_name, long[] value, long terminator);
public static final void g_object_get(long object, byte[] first_property_name, long[] value, long terminator) {
	lock.lock();
	try {
		_g_object_get(object, first_property_name, value, terminator);
	} finally {
		lock.unlock();
	}
}
/**
 * @param object cast=(GObject *)
 * @param quark cast=(GQuark)
 */
public static final native long _g_object_get_qdata(long object, int quark);
public static final long g_object_get_qdata(long object, int quark) {
	lock.lock();
	try {
		return _g_object_get_qdata(object, quark);
	} finally {
		lock.unlock();
	}
}
/**
 * @param type cast=(GType)
 * @param first_property_name cast=(const gchar *)
 */
public static final native long _g_object_new (long type, long first_property_name);
public static final long g_object_new (long type, long first_property_name) {
	lock.lock();
	try {
		return _g_object_new(type, first_property_name);
	} finally {
		lock.unlock();
	}
}
/**
 * @param object cast=(GObject *)
 * @param property_name cast=(const gchar *)
 */
public static final native void _g_object_notify (long object, byte[] property_name);
public static final void g_object_notify (long object, byte[] property_name) {
	lock.lock();
	try {
		_g_object_notify(object, property_name);
	} finally {
		lock.unlock();
	}
}
/** @param object cast=(gpointer) */
public static final native long _g_object_ref(long object);
public static final long g_object_ref(long object) {
	lock.lock();
	try {
		return _g_object_ref(object);
	} finally {
		lock.unlock();
	}
}
/**
 * @param object cast=(gpointer)
 * @param first_property_name cast=(const gchar *),flags=no_out
 * @param terminator cast=(const gchar *),flags=sentinel
 */
public static final native void _g_object_set(long object, byte[] first_property_name, boolean data, long terminator);
public static final void g_object_set(long object, byte[] first_property_name, boolean data, long terminator) {
	lock.lock();
	try {
		_g_object_set(object, first_property_name, data, terminator);
	} finally {
		lock.unlock();
	}
}
/**
 * @param object cast=(gpointer)
 * @param first_property_name cast=(const gchar *),flags=no_out
 * @param terminator cast=(const gchar *),flags=sentinel
 */
public static final native void _g_object_set(long object, byte[] first_property_name, byte[] data, long terminator);
public static final void g_object_set(long object, byte[] first_property_name, byte[] data, long terminator) {
	lock.lock();
	try {
		_g_object_set(object, first_property_name, data, terminator);
	} finally {
		lock.unlock();
	}
}

//Note, the function below is handled in a special way in os.h because of the GdkRGBA (gtk3 only) struct. See os.h
//So although it is not marked as dynamic, it is only build on gtk3.
/**
 * @param object cast=(gpointer)
 * @param first_property_name cast=(const gchar *)
 * @param terminator cast=(const gchar *),flags=sentinel
 */
public static final native void _g_object_set(long object, byte[] first_property_name, GdkRGBA data, long terminator);
public static final void g_object_set(long object, byte[] first_property_name, GdkRGBA data, long terminator) {
	lock.lock();
	try {
		_g_object_set(object, first_property_name, data, terminator);
	} finally {
		lock.unlock();
	}
}

/**
 * @param object cast=(gpointer)
 * @param first_property_name cast=(const gchar *),flags=no_out
 * @param terminator cast=(const gchar *),flags=sentinel
 */
public static final native void _g_object_set(long object, byte[] first_property_name, int data, long terminator);
public static final void g_object_set(long object, byte[] first_property_name, int data, long terminator) {
	lock.lock();
	try {
		_g_object_set(object, first_property_name, data, terminator);
	} finally {
		lock.unlock();
	}
}
/**
 * @param object cast=(gpointer)
 * @param first_property_name cast=(const gchar *),flags=no_out
 * @param terminator cast=(const gchar *),flags=sentinel
 */
public static final native void _g_object_set(long object, byte[] first_property_name, float data, long terminator);
public static final void g_object_set(long object, byte[] first_property_name, float data, long terminator) {
	lock.lock();
	try {
		_g_object_set(object, first_property_name, data, terminator);
	} finally {
		lock.unlock();
	}
}
/**
 * @param object cast=(gpointer)
 * @param first_property_name cast=(const gchar *),flags=no_out
 * @param terminator cast=(const gchar *),flags=sentinel
 */
public static final native void _g_object_set(long object, byte[] first_property_name, long data, long terminator);
public static final void g_object_set(long object, byte[] first_property_name, long data, long terminator) {
	lock.lock();
	try {
		_g_object_set(object, first_property_name, data, terminator);
	} finally {
		lock.unlock();
	}
}
/**
 * @param object cast=(GObject *)
 * @param quark cast=(GQuark)
 * @param data cast=(gpointer)
 */
public static final native void _g_object_set_qdata(long object, int quark, long data);
public static final void g_object_set_qdata(long object, int quark, long data) {
	lock.lock();
	try {
		_g_object_set_qdata(object, quark, data);
	} finally {
		lock.unlock();
	}
}
/** @param object cast=(gpointer) */
// NOTE: Lock is not used, see Bug 546743 and Bug 547093
public static final native void g_object_unref(long object);

/**
 * @param data cast=(gconstpointer)
 * @param size cast=(gsize)
 */
public static final native long _g_bytes_new (byte [] data, long size);
public static final long g_bytes_new (byte [] data, long size) {
	lock.lock();
	try {
		return _g_bytes_new (data, size);
	} finally {
		lock.unlock();
	}
}

/**
 * @param gBytes cast=(GBytes *)
 */
public static final native void _g_bytes_unref (long gBytes);
public static final void g_bytes_unref (long gBytes) {
	lock.lock();
	try {
		_g_bytes_unref (gBytes);
	} finally {
		lock.unlock();
	}
}

/** @param string cast=(const gchar *),flags=no_out */
public static final native int _g_quark_from_string(byte[] string);
public static final int g_quark_from_string(byte[] string) {
	lock.lock();
	try {
		return _g_quark_from_string(string);
	} finally {
		lock.unlock();
	}
}
/** @param prgname cast=(const gchar *),flags=no_out */
public static final native void _g_set_prgname(byte[] prgname);
public static final void g_set_prgname(byte[] prgname) {
	lock.lock();
	try {
		_g_set_prgname(prgname);
	} finally {
		lock.unlock();
	}
}
/**
 * @param instance cast=(gpointer)
 * @param detailed_signal cast=(const gchar *),flags=no_out
 * @param proc cast=(GCallback)
 * @param data cast=(gpointer)
 */
public static final native int _g_signal_connect(long instance, byte[] detailed_signal, long proc, long data);
public static final int g_signal_connect(long instance, byte[] detailed_signal, long proc, long data) {
	lock.lock();
	try {
		return _g_signal_connect(instance, detailed_signal, proc, data);
	} finally {
		lock.unlock();
	}
}
/**
 * @param instance cast=(gpointer)
 * @param detailed_signal cast=(const gchar *)
 * @param closure cast=(GClosure *)
 * @param after cast=(gboolean)
 */
public static final native int _g_signal_connect_closure(long instance, byte[] detailed_signal, long closure, boolean after);
public static final int g_signal_connect_closure(long instance, byte[] detailed_signal, long closure, boolean after) {
	lock.lock();
	try {
		return _g_signal_connect_closure(instance, detailed_signal, closure, after);
	} finally {
		lock.unlock();
	}
}
/**
 * @param instance cast=(gpointer)
 * @param signal_id cast=(guint)
 * @param detail cast=(GQuark)
 * @param closure cast=(GClosure *)
 * @param after cast=(gboolean)
 */
public static final native int _g_signal_connect_closure_by_id(long instance, int signal_id, int detail, long closure, boolean after);
public static final int g_signal_connect_closure_by_id(long instance, int signal_id, int detail, long closure, boolean after) {
	lock.lock();
	try {
		return _g_signal_connect_closure_by_id(instance, signal_id, detail, closure, after);
	} finally {
		lock.unlock();
	}
}
/**
 * @param instance cast=(gpointer)
 * @param detailed_signal cast=(const gchar *),flags=no_out
 */
public static final native void _g_signal_emit_by_name(long instance, byte[] detailed_signal);
public static final void g_signal_emit_by_name(long instance, byte[] detailed_signal) {
	lock.lock();
	try {
		_g_signal_emit_by_name(instance, detailed_signal);
	} finally {
		lock.unlock();
	}
}
/**
 * @param instance cast=(gpointer)
 * @param detailed_signal cast=(const gchar *),flags=no_out
 */
public static final native void _g_signal_emit_by_name(long instance, byte[] detailed_signal, long data);
public static final void g_signal_emit_by_name(long instance, byte[] detailed_signal, long data) {
	lock.lock();
	try {
		_g_signal_emit_by_name(instance, detailed_signal, data);
	} finally {
		lock.unlock();
	}
}
/**
 * @param instance cast=(gpointer)
 * @param detailed_signal cast=(const gchar *),flags=no_out
 */
public static final native void _g_signal_emit_by_name(long instance, byte[] detailed_signal, GdkRectangle data);
public static final void g_signal_emit_by_name(long instance, byte[] detailed_signal, GdkRectangle data) {
	lock.lock();
	try {
		_g_signal_emit_by_name(instance, detailed_signal, data);
	} finally {
		lock.unlock();
	}
}
/**
 * @param instance cast=(gpointer)
 * @param detailed_signal cast=(const gchar *),flags=no_out
 */
public static final native void _g_signal_emit_by_name(long instance, byte[] detailed_signal, long data1, long data2);
public static final void g_signal_emit_by_name(long instance, byte[] detailed_signal, long data1, long data2) {
	lock.lock();
	try {
		_g_signal_emit_by_name(instance, detailed_signal, data1, data2);
	} finally {
		lock.unlock();
	}
}
/**
 * @param instance cast=(gpointer)
 * @param detailed_signal cast=(const gchar *),flags=no_out
 */
public static final native void _g_signal_emit_by_name(long instance, byte[] detailed_signal, byte [] data);
public static final void g_signal_emit_by_name(long instance, byte[] detailed_signal, byte [] data) {
	lock.lock();
	try {
		_g_signal_emit_by_name(instance, detailed_signal, data);
	} finally {
		lock.unlock();
	}
}
/**
 * @param instance cast=(gpointer)
 * @param handler_id cast=(gulong)
 */
public static final native void _g_signal_handler_disconnect(long instance, int handler_id);
public static final void g_signal_handler_disconnect(long instance, int handler_id) {
	lock.lock();
	try {
		_g_signal_handler_disconnect(instance, handler_id);
	} finally {
		lock.unlock();
	}
}
/**
 * @param instance cast=(gpointer)
 * @param detail cast=(GQuark)
 * @param closure cast=(GClosure *)
 * @param func cast=(gpointer)
 * @param data cast=(gpointer)
 */
public static final native int _g_signal_handler_find(long instance, int mask, int signal_id, int detail, long closure, long func, long data);
public static final int g_signal_handler_find(long instance, int mask, int signal_id, int detail, long closure, long func, long data) {
	lock.lock();
	try {
		return _g_signal_handler_find(instance, mask, signal_id, detail, closure, func, data);
	} finally {
		lock.unlock();
	}
}
/**
 * @param instance cast=(gpointer)
 * @param mask cast=(GSignalMatchType)
 * @param signal_id cast=(guint)
 * @param detail cast=(GQuark)
 * @param closure cast=(GClosure *)
 * @param func cast=(gpointer)
 * @param data cast=(gpointer)
 */
public static final native int _g_signal_handlers_block_matched(long instance, int mask, int signal_id, int detail, long closure, long func, long data);
public static final int g_signal_handlers_block_matched(long instance, int mask, int signal_id, int detail, long closure, long func, long data) {
	lock.lock();
	try {
		return _g_signal_handlers_block_matched(instance, mask, signal_id, detail, closure, func, data);
	} finally {
		lock.unlock();
	}
}
/**
 * @param instance cast=(gpointer)
 * @param mask cast=(GSignalMatchType)
 * @param signal_id cast=(guint)
 * @param detail cast=(GQuark)
 * @param closure cast=(GClosure *)
 * @param func cast=(gpointer)
 * @param data cast=(gpointer)
 */
public static final native int _g_signal_handlers_unblock_matched(long instance, int mask, int signal_id, int detail, long closure, long func, long data);
public static final int g_signal_handlers_unblock_matched(long instance, int mask, int signal_id, int detail, long closure, long func, long data) {
	lock.lock();
	try {
		return _g_signal_handlers_unblock_matched(instance, mask, signal_id, detail, closure, func, data);
	} finally {
		lock.unlock();
	}
}
/** @param name cast=(const gchar *),flags=no_out */
public static final native int _g_signal_lookup (byte[] name, long itype);
public static final int g_signal_lookup (byte[] name, long itype) {
	lock.lock();
	try {
		return _g_signal_lookup(name, itype);
	} finally {
		lock.unlock();
	}
}
/**
 * @param instance cast=(gpointer)
 * @param detailed_signal cast=(const gchar *),flags=no_out
 */
public static final native void _g_signal_stop_emission_by_name(long instance, byte[] detailed_signal);
public static final void g_signal_stop_emission_by_name(long instance, byte[] detailed_signal) {
	lock.lock();
	try {
		_g_signal_stop_emission_by_name(instance, detailed_signal);
	} finally {
		lock.unlock();
	}
}
/** @param tag cast=(guint) */
public static final native boolean /*long*/ _g_source_remove (long tag);
public static final boolean /*long*/ g_source_remove (long tag) {
	lock.lock();
	try {
		return _g_source_remove(tag);
	} finally {
		lock.unlock();
	}
}
/**
 * @param list cast=(GSList *)
 * @param data cast=(gpointer)
 */
public static final native long _g_slist_append(long list, long data);
public static final long g_slist_append(long list, long data) {
	lock.lock();
	try {
		return _g_slist_append(list, data);
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(GSList *) */
public static final native long _g_slist_data (long list);
public static final long g_slist_data (long list) {
	lock.lock();
	try {
		return _g_slist_data(list);
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(GSList *) */
public static final native void _g_slist_free (long list);
public static final void g_slist_free (long list) {
	lock.lock();
	try {
		_g_slist_free(list);
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(GSList *) */
public static final native long _g_slist_next (long list);
public static final long g_slist_next (long list) {
	lock.lock();
	try {
		return _g_slist_next(list);
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(GSList *) */
public static final native int _g_slist_length (long list);
public static final int g_slist_length (long list) {
	lock.lock();
	try {
		return _g_slist_length(list);
	} finally {
		lock.unlock();
	}
}
/** @param string_array cast=(gchar **) */
public static final native void _g_strfreev(long string_array);
public static final void g_strfreev(long string_array) {
	lock.lock();
	try {
		_g_strfreev(string_array);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=getter
 * @param string cast=(GString *)
 */
public static final native int _GString_len(long string);
public static final int GString_len(long string) {
	lock.lock();
	try {
		return _GString_len(string);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=getter
 * @param string cast=(GString *)
 */
public static final native long _GString_str(long string);
public static final long GString_str(long string) {
	lock.lock();
	try {
		return _GString_str(string);
	} finally {
		lock.unlock();
	}
}

/**
 * @param init cast=(const gchar *)
 */
public static final native long _g_string_new_len(long init, long gssize);
/** 				   GString * g_string_new_len (const gchar *init, gssize len); */
public static final long g_string_new_len(long init, long gssize) {
	lock.lock();
	try {
		return _g_string_new_len(init, gssize);
	} finally {
		lock.unlock();
	}
}

/**
 * @param GString cast=(GString *)
 */
public static final native long _g_string_free(long GString, int gboolen_free_segment);
/** 					 gchar * g_string_free (GString *string, gboolean free_segment);*/
public static final long g_string_free(long GString, int gboolen_free_segment) {
	lock.lock();
	try {
		return _g_string_free(GString, gboolen_free_segment);
	} finally {
		lock.unlock();
	}
}

/**
 * @param str cast=(const gchar *)
 * @param endptr cast=(gchar **)
 */
public static final native double _g_strtod(long str, long [] endptr);
public static final double g_strtod(long str, long [] endptr) {
	lock.lock();
	try {
		return _g_strtod(str, endptr);
	} finally {
		lock.unlock();
	}
}
/** @param str cast=(char *) */
public static final native long g_strdup (long str);
/**
 * @param instance_type cast=(GType)
 * @param interface_type cast=(GType)
 * @param info cast=(const GInterfaceInfo *)
 */
public static final native void _g_type_add_interface_static (long instance_type, long interface_type, long info);
public static final void g_type_add_interface_static (long instance_type, long interface_type, long info) {
	lock.lock();
	try {
		_g_type_add_interface_static(instance_type, interface_type, info);
	} finally {
		lock.unlock();
	}
}
/** @param g_class cast=(GType) */
public static final native long _g_type_class_peek (long g_class);
public static final long g_type_class_peek (long g_class) {
	lock.lock();
	try {
		return _g_type_class_peek(g_class);
	} finally {
		lock.unlock();
	}
}
/** @param g_class cast=(gpointer) */
public static final native long _g_type_class_peek_parent (long g_class);
public static final long g_type_class_peek_parent (long g_class) {
	lock.lock();
	try {
		return _g_type_class_peek_parent(g_class);
	} finally {
		lock.unlock();
	}
}
/** @param g_class cast=(GType) */
public static final native long _g_type_class_ref (long g_class);
public static final long g_type_class_ref (long g_class) {
	lock.lock();
	try {
		return _g_type_class_ref(g_class);
	} finally {
		lock.unlock();
	}
}
/** @param g_class cast=(gpointer) */
public static final native void _g_type_class_unref (long g_class);
public static final void g_type_class_unref (long g_class) {
	lock.lock();
	try {
		_g_type_class_unref(g_class);
	} finally {
		lock.unlock();
	}
}
/** @param iface cast=(gpointer) */
public static final native long _g_type_interface_peek_parent (long iface);
public static final long g_type_interface_peek_parent (long iface) {
	lock.lock();
	try {
		return _g_type_interface_peek_parent(iface);
	} finally {
		lock.unlock();
	}
}
/**
 * @param type cast=(GType)
 * @param is_a_type cast=(GType)
 */
public static final native boolean _g_type_is_a (long type, long is_a_type);
public static final boolean g_type_is_a (long type, long is_a_type) {
	lock.lock();
	try {
		return _g_type_is_a(type, is_a_type);
	} finally {
		lock.unlock();
	}
}
/** @param type cast=(GType) */
public static final native long _g_type_parent (long type);
public static final long g_type_parent (long type) {
	lock.lock();
	try {
		return _g_type_parent(type);
	} finally {
		lock.unlock();
	}
}
/**
 * @param type cast=(GType)
 * @param query cast=(GTypeQuery *)
 */
public static final native void _g_type_query (long type, long query);
public static final void g_type_query (long type, long query) {
	lock.lock();
	try {
		_g_type_query(type, query);
	} finally {
		lock.unlock();
	}
}
/**
 * @param parent_type cast=(GType)
 * @param type_name cast=(const gchar *)
 * @param info cast=(const GTypeInfo *)
 * @param flags cast=(GTypeFlags)
 */
public static final native long _g_type_register_static (long parent_type, byte[] type_name, long info, int flags);
public static final long g_type_register_static (long parent_type, byte[] type_name, long info, int flags) {
	lock.lock();
	try {
		return _g_type_register_static(parent_type, type_name, info, flags);
	} finally {
		lock.unlock();
	}
}
/**
 * @param str cast=(const gunichar2 *),flags=no_out critical
 * @param len cast=(glong)
 * @param items_read cast=(glong *),flags=critical
 * @param items_written cast=(glong *),flags=critical
 * @param error cast=(GError **),flags=critical
 */
// NOTE: Lock is not used, see Bug 546743 and Bug 547093
public static final native long g_utf16_to_utf8(char[] str, long len, long [] items_read, long [] items_written, long [] error);
/**
 * @param str cast=(const gchar *)
 * @param pos cast=(const gchar *)
 */
public static final native long _g_utf8_pointer_to_offset(long str, long pos);
public static final long g_utf8_pointer_to_offset(long str, long pos) {
	lock.lock();
	try {
		return _g_utf8_pointer_to_offset(str, pos);
	} finally {
		lock.unlock();
	}
}
/** @param str cast=(const gchar *) */
public static final native long _g_utf8_strlen(long str, long max);
public static final long g_utf8_strlen(long str, long max) {
	lock.lock();
	try {
		return _g_utf8_strlen(str, max);
	} finally {
		lock.unlock();
	}
}
/**
 * @param str cast=(const gchar *),flags=no_out critical
 * @param len cast=(glong)
 * @param items_read cast=(glong *),flags=critical
 * @param items_written cast=(glong *),flags=critical
 * @param error cast=(GError **),flags=critical
 */
public static final native long _g_utf8_to_utf16(byte[] str, long len, long [] items_read, long [] items_written, long [] error);
public static final long g_utf8_to_utf16(byte[] str, long len, long [] items_read, long [] items_written, long [] error) {
	lock.lock();
	try {
		return _g_utf8_to_utf16(str, len, items_read, items_written, error);
	} finally {
		lock.unlock();
	}
}
/**
 * @param str cast=(const gchar *)
 * @param len cast=(glong)
 * @param items_read cast=(glong *),flags=critical
 * @param items_written cast=(glong *),flags=critical
 * @param error cast=(GError **),flags=critical
 */
public static final native long _g_utf8_to_utf16(long str, long len, long [] items_read, long [] items_written, long [] error);
public static final long g_utf8_to_utf16(long str, long len, long [] items_read, long [] items_written, long [] error) {
	lock.lock();
	try {
		return _g_utf8_to_utf16(str, len, items_read, items_written, error);
	} finally {
		lock.unlock();
	}
}
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
/** @param value cast=(GValue *) */
public static final native void g_value_unset (long value);
/** @param value cast=(const GValue *) */
public static final native long _g_value_peek_pointer (long value);
public static final  long g_value_peek_pointer (long value) {
	lock.lock();
	try {
		return _g_value_peek_pointer(value);
	} finally {
		lock.unlock();
	}
}
/**
 * @param variable cast=(const gchar *),flags=no_out
 */
public static final native void _g_unsetenv(byte [] variable);
public static final void g_unsetenv(byte [] variable) {
	lock.lock();
	try {
		_g_unsetenv(variable);
	} finally {
		lock.unlock();
	}
}
/** @method flags=const */
public static final native int _glib_major_version();
public static final int glib_major_version() {
	lock.lock();
	try {
		return _glib_major_version();
	} finally {
		lock.unlock();
	}
}
/** @method flags=const */
public static final native int _glib_minor_version();
public static final int glib_minor_version() {
	lock.lock();
	try {
		return _glib_minor_version();
	} finally {
		lock.unlock();
	}
}
/** @method flags=const */
public static final native int _glib_micro_version();
public static final int glib_micro_version() {
	lock.lock();
	try {
		return _glib_micro_version();
	} finally {
		lock.unlock();
	}
}

/**
 * @param interval cast=(guint32)
 * @param function cast=(GSourceFunc)
 * @param data cast=(gpointer)
 */
public static final native int _g_timeout_add(int interval, long function, long data);
public static final int g_timeout_add(int interval, long function, long data) {
	lock.lock();
	try {
		return _g_timeout_add(interval, function, data);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native boolean _FcConfigAppFontAddFile(long config, byte[] file);
public static final boolean FcConfigAppFontAddFile(long config, byte[] file) {
	lock.lock();
	try {
		return _FcConfigAppFontAddFile(config, file);
	} finally {
		lock.unlock();
	}
}


// Technically works on OSX also, but currently only used on Linux.
// Once SWT is moved to Java 9, consider using 'ProcessHandle.current().getPid();' instead,
// but for now getpid() should do.
// https://stackoverflow.com/questions/35842/how-can-a-java-program-get-its-own-process-id
public static final native int _getpid ();
public static final int getpid() {
	lock.lock();
	try {
		return _getpid();
	} finally {
		lock.unlock();
	}
}
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param size cast=(size_t)
 */
public static final native void memmove(long dest, GInterfaceInfo src, int size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *),flags=no_out
 */
public static final native void memmove(long dest, GObjectClass src);
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
public static final native void memmove(long dest, GtkTargetEntry src, long size);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param size cast=(size_t)
 */
public static final native void memmove(long dest, GdkRGBA src, long size);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param size cast=(size_t)
 */
public static final native void memmove(long dest, GdkEventButton src, long size);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param size cast=(size_t)
 */
public static final native void memmove(long dest, GdkEventKey src, long size);
/** @param src flags=no_out */
public static final native void memmove(long dest, GtkWidgetClass src);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param size cast=(size_t)
 */
public static final native void memmove(long dest, PangoAttribute src, long size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 */
public static final native void memmove(GObjectClass  dest, long src);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(GTypeQuery dest, long src, long size);
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
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(GdkEventButton dest, long src, long size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(GdkEventCrossing dest, long src, long size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(GdkEventFocus dest, long src, long size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(GdkEventKey dest, long src, long size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(GdkEventMotion dest, long src, long size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(GdkEventWindowState dest, long src, long size);
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
/** @param attribute cast=(const PangoAttribute *) */
public static final native long _pango_attribute_copy (long attribute);
public static final long pango_attribute_copy (long attribute) {
	lock.lock();
	try {
		return _pango_attribute_copy(attribute);
	} finally {
		lock.unlock();
	}
}
public static final native long _pango_attr_background_new (short red, short green, short blue);
public static final long pango_attr_background_new (short red, short green, short blue) {
	lock.lock();
	try {
		return _pango_attr_background_new(red, green, blue);
	} finally {
		lock.unlock();
	}
}
/** @param desc cast=(const PangoFontDescription *) */
public static final native long _pango_attr_font_desc_new(long desc);
public static final long pango_attr_font_desc_new(long desc) {
	lock.lock();
	try {
		return _pango_attr_font_desc_new(desc);
	} finally {
		lock.unlock();
	}
}
public static final native long _pango_attr_foreground_new (short red, short green, short blue);
public static final long pango_attr_foreground_new (short red, short green, short blue) {
	lock.lock();
	try {
		return _pango_attr_foreground_new(red, green, blue);
	} finally {
		lock.unlock();
	}
}
public static final native long _pango_attr_rise_new(int rise);
public static final long pango_attr_rise_new(int rise) {
	lock.lock();
	try {
		return _pango_attr_rise_new(rise);
	} finally {
		lock.unlock();
	}
}
/**
 * @param ink_rect flags=no_out
 * @param logical_rect flags=no_out
 */
public static final native long _pango_attr_shape_new(PangoRectangle ink_rect, PangoRectangle logical_rect);
public static final long pango_attr_shape_new(PangoRectangle ink_rect, PangoRectangle logical_rect) {
	lock.lock();
	try {
		return _pango_attr_shape_new(ink_rect, logical_rect);
	} finally {
		lock.unlock();
	}
}
/**
 * @param list cast=(PangoAttrList *)
 * @param attr cast=(PangoAttribute *)
 */
public static final native void _pango_attr_list_insert(long list, long attr);
public static final void pango_attr_list_insert(long list, long attr) {
	lock.lock();
	try {
		_pango_attr_list_insert(list, attr);
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(PangoAttrList *) */
public static final native long _pango_attr_list_get_iterator(long list);
public static final long pango_attr_list_get_iterator(long list) {
	lock.lock();
	try {
		return _pango_attr_list_get_iterator(list);
	} finally {
		lock.unlock();
	}
}
/** @param iterator cast=(PangoAttrIterator *) */
public static final native boolean _pango_attr_iterator_next(long iterator);
public static final boolean pango_attr_iterator_next(long iterator) {
	lock.lock();
	try {
		return _pango_attr_iterator_next(iterator);
	} finally {
		lock.unlock();
	}
}
/**
 * @param iterator cast=(PangoAttrIterator *)
 * @param start cast=(gint *)
 * @param end cast=(gint *)
 */
public static final native void _pango_attr_iterator_range(long iterator, int[] start, int[] end);
public static final void pango_attr_iterator_range(long iterator, int[] start, int[] end) {
	lock.lock();
	try {
		_pango_attr_iterator_range(iterator, start, end);
	} finally {
		lock.unlock();
	}
}
/**
 * @param iterator cast=(PangoAttrIterator *)
 * @param type cast=(PangoAttrType)
 */
public static final native long _pango_attr_iterator_get(long iterator, int type);
public static final long pango_attr_iterator_get(long iterator, int type) {
	lock.lock();
	try {
		return _pango_attr_iterator_get(iterator, type);
	} finally {
		lock.unlock();
	}
}
/** @param iterator cast=(PangoAttrIterator *) */
public static final native void _pango_attr_iterator_destroy(long iterator);
public static final void pango_attr_iterator_destroy(long iterator) {
	lock.lock();
	try {
		_pango_attr_iterator_destroy(iterator);
	} finally {
		lock.unlock();
	}
}
public static final native long _pango_attr_list_new();
public static final long pango_attr_list_new() {
	lock.lock();
	try {
		return _pango_attr_list_new();
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(PangoAttrList *) */
public static final native void _pango_attr_list_unref(long list);
public static final void pango_attr_list_unref(long list) {
	lock.lock();
	try {
		_pango_attr_list_unref(list);
	} finally {
		lock.unlock();
	}
}
public static final native long _pango_attr_strikethrough_color_new(short red, short green, short blue);
public static final long pango_attr_strikethrough_color_new(short red, short green, short blue) {
	lock.lock();
	try {
		return _pango_attr_strikethrough_color_new(red, green, blue);
	} finally {
		lock.unlock();
	}
}
public static final native long _pango_attr_strikethrough_new(boolean strikethrough);
public static final long pango_attr_strikethrough_new(boolean strikethrough) {
	lock.lock();
	try {
		return _pango_attr_strikethrough_new(strikethrough);
	} finally {
		lock.unlock();
	}
}
public static final native long _pango_attr_underline_color_new(short red, short green, short blue);
public static final long pango_attr_underline_color_new(short red, short green, short blue) {
	lock.lock();
	try {
		return _pango_attr_underline_color_new(red, green, blue);
	} finally {
		lock.unlock();
	}
}
public static final native long _pango_attr_underline_new(int underline);
public static final long pango_attr_underline_new(int underline) {
	lock.lock();
	try {
		return _pango_attr_underline_new(underline);
	} finally {
		lock.unlock();
	}
}
public static final native long _pango_attr_weight_new(int weight);
public static final long pango_attr_weight_new(int weight) {
	lock.lock();
	try {
		return _pango_attr_weight_new(weight);
	} finally {
		lock.unlock();
	}
}
/**
 * @param cairo cast=(cairo_t *)
 */
public static final native long _pango_cairo_create_layout(long cairo);
public static final long pango_cairo_create_layout(long cairo) {
	lock.lock();
	try {
		return _pango_cairo_create_layout(cairo);
	} finally {
		lock.unlock();
	}
}
public static final native long _pango_cairo_font_map_get_default();
public static final long pango_cairo_font_map_get_default() {
	lock.lock();
	try {
		return _pango_cairo_font_map_get_default();
	} finally {
		lock.unlock();
	}
}
/**
 * @param context cast=(PangoContext *)
 */
public static final native long _pango_cairo_context_get_font_options(long context);
public static final long pango_cairo_context_get_font_options(long context) {
	lock.lock();
	try {
		return _pango_cairo_context_get_font_options(context);
	} finally {
		lock.unlock();
	}
}
/**
 * @param context cast=(PangoContext *)
 * @param options cast=( cairo_font_options_t *)
 */
public static final native void _pango_cairo_context_set_font_options(long context, long options);
public static final void pango_cairo_context_set_font_options(long context, long options) {
	lock.lock();
	try {
		_pango_cairo_context_set_font_options(context, options);
	} finally {
		lock.unlock();
	}
}
/**
 * @param cairo cast=(cairo_t *)
 * @param layout cast=(PangoLayout *)
 */
public static final native void _pango_cairo_layout_path(long cairo, long layout);
public static final void pango_cairo_layout_path(long cairo, long layout) {
	lock.lock();
	try {
		_pango_cairo_layout_path(cairo, layout);
	} finally {
		lock.unlock();
	}
}
/**
 * @param cairo cast=(cairo_t *)
 * @param layout cast=(PangoLayout *)
 */
public static final native void _pango_cairo_show_layout(long cairo, long layout);
public static final void pango_cairo_show_layout(long cairo, long layout) {
	lock.lock();
	try {
		_pango_cairo_show_layout(cairo, layout);
	} finally {
		lock.unlock();
	}
}
/** @param context cast=(PangoContext *) */
public static final native int _pango_context_get_base_dir(long context);
public static final int pango_context_get_base_dir(long context) {
	lock.lock();
	try {
		return _pango_context_get_base_dir(context);
	} finally {
		lock.unlock();
	}
}
/** @param context cast=(PangoContext *) */
public static final native long _pango_context_get_language(long context);
public static final long pango_context_get_language(long context) {
	lock.lock();
	try {
		return _pango_context_get_language(context);
	} finally {
		lock.unlock();
	}
}
/**
 * @param context cast=(PangoContext *)
 * @param desc cast=(const PangoFontDescription *)
 * @param language cast=(PangoLanguage *)
 */
public static final native long _pango_context_get_metrics(long context, long desc, long language);
public static final long pango_context_get_metrics(long context, long desc, long language) {
	lock.lock();
	try {
		return _pango_context_get_metrics(context, desc, language);
	} finally {
		lock.unlock();
	}
}
/**
 * @param context cast=(PangoContext *)
 * @param families cast=(PangoFontFamily ***)
 * @param n_families cast=(int *)
 */
public static final native void _pango_context_list_families(long context, long [] families, int[] n_families);
public static final void pango_context_list_families(long context, long [] families, int[] n_families) {
	lock.lock();
	try {
		_pango_context_list_families(context, families, n_families);
	} finally {
		lock.unlock();
	}
}
/** @param context cast=(PangoContext *) */
public static final native void _pango_context_set_base_dir(long context, int direction);
public static final void pango_context_set_base_dir(long context, int direction) {
	lock.lock();
	try {
		_pango_context_set_base_dir(context, direction);
	} finally {
		lock.unlock();
	}
}
/**
 * @param context cast=(PangoContext *)
 * @param language cast=(PangoLanguage *)
 */
public static final native void _pango_context_set_language(long context, long language);
public static final void pango_context_set_language(long context, long language) {
	lock.lock();
	try {
		_pango_context_set_language(context, language);
	} finally {
		lock.unlock();
	}
}
/** @param desc cast=(PangoFontDescription *) */
public static final native long _pango_font_description_copy(long desc);
public static final long pango_font_description_copy(long desc) {
	lock.lock();
	try {
		return _pango_font_description_copy(desc);
	} finally {
		lock.unlock();
	}
}
/** @param desc cast=(PangoFontDescription *) */
public static final native void _pango_font_description_free(long desc);
public static final void pango_font_description_free(long desc) {
	lock.lock();
	try {
		_pango_font_description_free(desc);
	} finally {
		lock.unlock();
	}
}
/** @param str cast=(const char *),flags=no_out critical */
public static final native long _pango_font_description_from_string(byte[] str);
public static final long pango_font_description_from_string(byte[] str) {
	lock.lock();
	try {
		return _pango_font_description_from_string(str);
	} finally {
		lock.unlock();
	}
}
/** @param desc cast=(PangoFontDescription *) */
public static final native long _pango_font_description_get_family(long desc);
public static final long pango_font_description_get_family(long desc) {
	lock.lock();
	try {
		return _pango_font_description_get_family(desc);
	} finally {
		lock.unlock();
	}
}
/** @param desc cast=(PangoFontDescription *) */
public static final native int _pango_font_description_get_size(long desc);
public static final int pango_font_description_get_size(long desc) {
	lock.lock();
	try {
		return _pango_font_description_get_size(desc);
	} finally {
		lock.unlock();
	}
}
/** @param desc cast=(PangoFontDescription *) */
public static final native int _pango_font_description_get_stretch(long desc);
public static final int pango_font_description_get_stretch(long desc) {
	lock.lock();
	try {
		return _pango_font_description_get_stretch(desc);
	} finally {
		lock.unlock();
	}
}
/** @param desc cast=(PangoFontDescription *) */
public static final native int _pango_font_description_get_variant(long desc);
public static final int pango_font_description_get_variant(long desc) {
	lock.lock();
	try {
		return _pango_font_description_get_variant(desc);
	} finally {
		lock.unlock();
	}
}
/** @param desc cast=(PangoFontDescription *) */
public static final native int _pango_font_description_get_style(long desc);
public static final int pango_font_description_get_style(long desc) {
	lock.lock();
	try {
		return _pango_font_description_get_style(desc);
	} finally {
		lock.unlock();
	}
}
/** @param desc cast=(PangoFontDescription *) */
public static final native int _pango_font_description_get_weight(long desc);
public static final int pango_font_description_get_weight(long desc) {
	lock.lock();
	try {
		return _pango_font_description_get_weight(desc);
	} finally {
		lock.unlock();
	}
}
public static final native long _pango_font_description_new();
public static final long pango_font_description_new() {
	lock.lock();
	try {
		return _pango_font_description_new();
	} finally {
		lock.unlock();
	}
}
/**
 * @param desc cast=(PangoFontDescription *)
 * @param family cast=(const char *),flags=no_out critical
 */
public static final native void _pango_font_description_set_family(long desc, byte[] family);
public static final void pango_font_description_set_family(long desc, byte[] family) {
	lock.lock();
	try {
		_pango_font_description_set_family(desc, family);
	} finally {
		lock.unlock();
	}
}
/**
 * @param desc cast=(PangoFontDescription *)
 * @param size cast=(gint)
 */
public static final native void _pango_font_description_set_size(long desc, int size);
public static final void pango_font_description_set_size(long desc, int size) {
	lock.lock();
	try {
		_pango_font_description_set_size(desc, size);
	} finally {
		lock.unlock();
	}
}
/**
 * @param desc cast=(PangoFontDescription *)
 * @param stretch cast=(PangoStretch)
 */
public static final native void _pango_font_description_set_stretch(long desc, int stretch);
public static final void pango_font_description_set_stretch(long desc, int stretch) {
	lock.lock();
	try {
		_pango_font_description_set_stretch(desc, stretch);
	} finally {
		lock.unlock();
	}
}
/**
 * @param desc cast=(PangoFontDescription *)
 * @param weight cast=(PangoStyle)
 */
public static final native void _pango_font_description_set_style(long desc, int weight);
public static final void pango_font_description_set_style(long desc, int weight) {
	lock.lock();
	try {
		_pango_font_description_set_style(desc, weight);
	} finally {
		lock.unlock();
	}
}
/**
 * @param desc cast=(PangoFontDescription *)
 * @param weight cast=(PangoWeight)
 */
public static final native void _pango_font_description_set_weight(long desc, int weight);
public static final void pango_font_description_set_weight(long desc, int weight) {
	lock.lock();
	try {
		_pango_font_description_set_weight(desc, weight);
	} finally {
		lock.unlock();
	}
}
/**
 * @param desc cast=(PangoFontDescription *)
 * @param variant cast=(PangoVariant)
 */
public static final native void _pango_font_description_set_variant(long desc, int variant);
public static final void pango_font_description_set_variant(long desc, int variant) {
	lock.lock();
	try {
		_pango_font_description_set_variant(desc, variant);
	} finally {
		lock.unlock();
	}
}
/** @param desc cast=(PangoFontDescription *) */
public static final native long _pango_font_description_to_string(long desc);
public static final long pango_font_description_to_string(long desc) {
	lock.lock();
	try {
		return _pango_font_description_to_string(desc);
	} finally {
		lock.unlock();
	}
}
/** @param face cast=(PangoFontFace *) */
public static final native long _pango_font_face_describe(long face);
public static final long pango_font_face_describe(long face) {
	lock.lock();
	try {
		return _pango_font_face_describe(face);
	} finally {
		lock.unlock();
	}
}
/** @param family cast=(PangoFontFamily *) */
public static final native long _pango_font_family_get_name(long family);
public static final long pango_font_family_get_name(long family) {
	lock.lock();
	try {
		return _pango_font_family_get_name(family);
	} finally {
		lock.unlock();
	}
}
/**
 * @param family cast=(PangoFontFamily *)
 * @param faces cast=(PangoFontFace ***)
 * @param n_faces cast=(int *)
 */
public static final native void _pango_font_family_list_faces(long family, long [] faces, int[] n_faces);
public static final void pango_font_family_list_faces(long family, long [] faces, int[] n_faces) {
	lock.lock();
	try {
		_pango_font_family_list_faces(family, faces, n_faces);
	} finally {
		lock.unlock();
	}
}
/**
 * @param font cast=(PangoFont *)
 * @param language cast=(PangoLanguage *)
 */
public static final native long _pango_font_get_metrics(long font, long language);
public static final long pango_font_get_metrics(long font, long language) {
	lock.lock();
	try {
		return _pango_font_get_metrics(font, language);
	} finally {
		lock.unlock();
	}
}
/**
 * @param fontMap cast=(PangoFontMap *)
 */
public static final native long _pango_font_map_create_context(long fontMap);
public static final long pango_font_map_create_context(long fontMap) {
	lock.lock();
	try {
		return _pango_font_map_create_context(fontMap);
	} finally {
		lock.unlock();
	}
}
/** @param metrics cast=(PangoFontMetrics *) */
public static final native int _pango_font_metrics_get_approximate_char_width(long metrics);
public static final int pango_font_metrics_get_approximate_char_width(long metrics) {
	lock.lock();
	try {
		return _pango_font_metrics_get_approximate_char_width(metrics);
	} finally {
		lock.unlock();
	}
}
/** @param metrics cast=(PangoFontMetrics *) */
public static final native int _pango_font_metrics_get_ascent(long metrics);
public static final int pango_font_metrics_get_ascent(long metrics) {
	lock.lock();
	try {
		return _pango_font_metrics_get_ascent(metrics);
	} finally {
		lock.unlock();
	}
}
/** @param metrics cast=(PangoFontMetrics *) */
public static final native int _pango_font_metrics_get_descent(long metrics);
public static final int pango_font_metrics_get_descent(long metrics) {
	lock.lock();
	try {
		return _pango_font_metrics_get_descent(metrics);
	} finally {
		lock.unlock();
	}
}
/** @param metrics cast=(PangoFontMetrics *) */
public static final native void _pango_font_metrics_unref(long metrics);
public static final void pango_font_metrics_unref(long metrics) {
	lock.lock();
	try {
		_pango_font_metrics_unref(metrics);
	} finally {
		lock.unlock();
	}
}
/** @param layout cast=(PangoLayout *) */
public static final native void _pango_layout_context_changed(long layout);
public static final void pango_layout_context_changed(long layout) {
	lock.lock();
	try {
		_pango_layout_context_changed(layout);
	} finally {
		lock.unlock();
	}
}
/** @param layout cast=(PangoLayout*) */
public static final native int _pango_layout_get_alignment(long layout);
public static final int pango_layout_get_alignment(long layout) {
	lock.lock();
	try {
		return _pango_layout_get_alignment(layout);
	} finally {
		lock.unlock();
	}
}
/** @param layout cast=(PangoLayout *) */
public static final native long _pango_layout_get_context(long layout);
public static final long pango_layout_get_context(long layout) {
	lock.lock();
	try {
		return _pango_layout_get_context(layout);
	} finally {
		lock.unlock();
	}
}
/** @param layout cast=(PangoLayout*) */
public static final native int _pango_layout_get_indent(long layout);
public static final int pango_layout_get_indent(long layout) {
	lock.lock();
	try {
		return _pango_layout_get_indent(layout);
	} finally {
		lock.unlock();
	}
}
/** @param layout cast=(PangoLayout*) */
public static final native long _pango_layout_get_iter(long layout);
public static final long pango_layout_get_iter(long layout) {
	lock.lock();
	try {
		return _pango_layout_get_iter(layout);
	} finally {
		lock.unlock();
	}
}
/** @param layout cast=(PangoLayout*) */
public static final native boolean _pango_layout_get_justify(long layout);
public static final boolean pango_layout_get_justify(long layout) {
	lock.lock();
	try {
		return _pango_layout_get_justify(layout);
	} finally {
		lock.unlock();
	}
}
/** @param layout cast=(PangoLayout *) */
public static final native long _pango_layout_get_line(long layout, int line);
public static final long pango_layout_get_line(long layout, int line) {
	lock.lock();
	try {
		return _pango_layout_get_line(layout, line);
	} finally {
		lock.unlock();
	}
}
/** @param layout cast=(PangoLayout*) */
public static final native int _pango_layout_get_line_count(long layout);
public static final int pango_layout_get_line_count(long layout) {
	lock.lock();
	try {
		return _pango_layout_get_line_count(layout);
	} finally {
		lock.unlock();
	}
}
/**
 * @param layout cast=(PangoLayout*)
 * @param attrs cast=(PangoLogAttr **)
 * @param n_attrs cast=(int *)
 */
public static final native void _pango_layout_get_log_attrs(long layout, long [] attrs, int[] n_attrs);
public static final void pango_layout_get_log_attrs(long layout, long [] attrs, int[] n_attrs) {
	lock.lock();
	try {
		_pango_layout_get_log_attrs(layout, attrs, n_attrs);
	} finally {
		lock.unlock();
	}
}
/**
 * @param layout cast=(PangoLayout *)
 * @param width cast=(int *)
 * @param height cast=(int *)
 */
public static final native void _pango_layout_get_size(long layout, int[] width, int[] height);
public static final void pango_layout_get_size(long layout, int[] width, int[] height) {
	lock.lock();
	try {
		_pango_layout_get_size(layout, width, height);
	} finally {
		lock.unlock();
	}
}
/**
 * @param layout cast=(PangoLayout *)
 * @param width cast=(int *)
 * @param height cast=(int *)
 */
public static final native void _pango_layout_get_pixel_size(long layout, int[] width, int[] height);
public static final void pango_layout_get_pixel_size(long layout, int[] width, int[] height) {
	lock.lock();
	try {
		_pango_layout_get_pixel_size(layout, width, height);
	} finally {
		lock.unlock();
	}
}
/** @param layout cast=(PangoLayout*) */
public static final native int _pango_layout_get_spacing(long layout);
public static final int pango_layout_get_spacing(long layout) {
	lock.lock();
	try {
		return _pango_layout_get_spacing(layout);
	} finally {
		lock.unlock();
	}
}
/** @param layout cast=(PangoLayout *) */
public static final native long _pango_layout_get_text(long layout);
public static final long pango_layout_get_text(long layout) {
	lock.lock();
	try {
		return _pango_layout_get_text(layout);
	} finally {
		lock.unlock();
	}
}
/** @param layout cast=(PangoLayout *) */
public static final native int _pango_layout_get_width(long layout);
public static final int pango_layout_get_width(long layout) {
	lock.lock();
	try {
		return _pango_layout_get_width(layout);
	} finally {
		lock.unlock();
	}
}
/**
 * @param layout cast=(PangoLayout*)
 * @param pos flags=no_in
 */
public static final native void _pango_layout_index_to_pos(long layout, int index, PangoRectangle pos);
public static final void pango_layout_index_to_pos(long layout, int index, PangoRectangle pos) {
	lock.lock();
	try {
		_pango_layout_index_to_pos(layout, index, pos);
	} finally {
		lock.unlock();
	}
}
/** @param iter cast=(PangoLayoutIter*) */
public static final native void _pango_layout_iter_free(long iter);
public static final void pango_layout_iter_free(long iter) {
	lock.lock();
	try {
		_pango_layout_iter_free(iter);
	} finally {
		lock.unlock();
	}
}
/**
 * @param iter cast=(PangoLayoutIter*)
 * @param ink_rect flags=no_in
 * @param logical_rect flags=no_in
 */
public static final native void _pango_layout_iter_get_line_extents(long iter, PangoRectangle ink_rect, PangoRectangle logical_rect);
public static final void pango_layout_iter_get_line_extents(long iter, PangoRectangle ink_rect, PangoRectangle logical_rect) {
	lock.lock();
	try {
		_pango_layout_iter_get_line_extents(iter, ink_rect, logical_rect);
	} finally {
		lock.unlock();
	}
}
/** @param iter cast=(PangoLayoutIter*) */
public static final native int _pango_layout_iter_get_index(long iter);
public static final int pango_layout_iter_get_index(long iter) {
	lock.lock();
	try {
		return _pango_layout_iter_get_index(iter);
	} finally {
		lock.unlock();
	}
}
/** @param iter cast=(PangoLayoutIter*) */
public static final native long _pango_layout_iter_get_run(long iter);
public static final long pango_layout_iter_get_run(long iter) {
	lock.lock();
	try {
		return _pango_layout_iter_get_run(iter);
	} finally {
		lock.unlock();
	}
}
/** @param iter cast=(PangoLayoutIter*) */
public static final native boolean _pango_layout_iter_next_line(long iter);
public static final boolean pango_layout_iter_next_line(long iter) {
	lock.lock();
	try {
		return _pango_layout_iter_next_line(iter);
	} finally {
		lock.unlock();
	}
}
/** @param iter cast=(PangoLayoutIter*) */
public static final native boolean _pango_layout_iter_next_run(long iter);
public static final boolean pango_layout_iter_next_run(long iter) {
	lock.lock();
	try {
		return _pango_layout_iter_next_run(iter);
	} finally {
		lock.unlock();
	}
}
/**
 * @param line cast=(PangoLayoutLine*)
 * @param ink_rect cast=(PangoRectangle *),flags=no_in
 * @param logical_rect cast=(PangoRectangle *),flags=no_in
 */
public static final native void _pango_layout_line_get_extents(long line, PangoRectangle ink_rect, PangoRectangle logical_rect);
public static final void pango_layout_line_get_extents(long line, PangoRectangle ink_rect, PangoRectangle logical_rect) {
	lock.lock();
	try {
		_pango_layout_line_get_extents(line, ink_rect, logical_rect);
	} finally {
		lock.unlock();
	}
}
/** @param context cast=(PangoContext *) */
public static final native long _pango_layout_new(long context);
public static final long pango_layout_new(long context) {
	lock.lock();
	try {
		return _pango_layout_new(context);
	} finally {
		lock.unlock();
	}
}
/** @param layout cast=(PangoLayout *) */
public static final native void _pango_layout_set_alignment (long layout, int alignment);
public static final void pango_layout_set_alignment (long layout, int alignment) {
	lock.lock();
	try {
		_pango_layout_set_alignment(layout, alignment);
	} finally {
		lock.unlock();
	}
}
/**
 * @param layout cast=(PangoLayout *)
 * @param attrs cast=(PangoAttrList *)
 */
public static final native void _pango_layout_set_attributes(long layout, long attrs);
public static final void pango_layout_set_attributes(long layout, long attrs) {
	lock.lock();
	try {
		_pango_layout_set_attributes(layout, attrs);
	} finally {
		lock.unlock();
	}
}
/**
 * @param layout cast=(PangoLayout *)
 */
public static final native void _pango_layout_set_auto_dir(long layout, boolean auto_dir);
public static final void pango_layout_set_auto_dir(long layout, boolean auto_dir) {
	lock.lock();
	try {
		_pango_layout_set_auto_dir(layout, auto_dir);
	} finally {
		lock.unlock();
	}
}
/**
 * @param context cast=(PangoLayout *)
 * @param descr cast=(PangoFontDescription *)
 */
public static final native void _pango_layout_set_font_description(long context, long descr);
public static final void pango_layout_set_font_description(long context, long descr) {
	lock.lock();
	try {
		_pango_layout_set_font_description(context, descr);
	} finally {
		lock.unlock();
	}
}
/** @param layout cast=(PangoLayout*) */
public static final native void _pango_layout_set_indent(long layout, int indent);
public static final void pango_layout_set_indent(long layout, int indent) {
	lock.lock();
	try {
		_pango_layout_set_indent(layout, indent);
	} finally {
		lock.unlock();
	}
}
/** @param layout cast=(PangoLayout*) */
public static final native void _pango_layout_set_justify(long layout, boolean justify);
public static final void pango_layout_set_justify(long layout, boolean justify) {
	lock.lock();
	try {
		_pango_layout_set_justify(layout, justify);
	} finally {
		lock.unlock();
	}
}
/**
 * @param context cast=(PangoLayout *)
 * @param setting cast=(gboolean)
 */
public static final native void _pango_layout_set_single_paragraph_mode(long context, boolean setting);
public static final void pango_layout_set_single_paragraph_mode(long context, boolean setting) {
	lock.lock();
	try {
		_pango_layout_set_single_paragraph_mode(context, setting);
	} finally {
		lock.unlock();
	}
}
/** @param layout cast=(PangoLayout *) */
public static final native void _pango_layout_set_spacing(long layout, int spacing);
public static final void pango_layout_set_spacing(long layout, int spacing) {
	lock.lock();
	try {
		_pango_layout_set_spacing(layout, spacing);
	} finally {
		lock.unlock();
	}
}
/**
 * @param layout cast=(PangoLayout *)
 * @param tabs cast=(PangoTabArray *)
 */
public static final native void _pango_layout_set_tabs(long layout, long tabs);
public static final void pango_layout_set_tabs(long layout, long tabs) {
	lock.lock();
	try {
		_pango_layout_set_tabs(layout, tabs);
	} finally {
		lock.unlock();
	}
}
/**
 * @param layout cast=(PangoLayout *)
 * @param text cast=(const char *),flags=no_out critical
 * @param length cast=(int)
 */
public static final native void _pango_layout_set_text(long layout, byte[] text, int length);
public static final void pango_layout_set_text(long layout, byte[] text, int length) {
	lock.lock();
	try {
		_pango_layout_set_text(layout, text, length);
	} finally {
		lock.unlock();
	}
}
/** @param layout cast=(PangoLayout *) */
public static final native void _pango_layout_set_width(long layout, int width);
public static final void pango_layout_set_width(long layout, int width) {
	lock.lock();
	try {
		_pango_layout_set_width(layout, width);
	} finally {
		lock.unlock();
	}
}
/** @param layout cast=(PangoLayout *) */
public static final native void _pango_layout_set_wrap (long layout, int wrap);
public static final void pango_layout_set_wrap (long layout, int wrap) {
	lock.lock();
	try {
		_pango_layout_set_wrap(layout, wrap);
	} finally {
		lock.unlock();
	}
}
/**
 * @param layout cast=(PangoLayout *)
 * @param index cast=(int *)
 * @param trailing cast=(int *)
 */
public static final native boolean _pango_layout_xy_to_index(long layout, int x, int y, int[] index, int[] trailing);
public static final boolean pango_layout_xy_to_index(long layout, int x, int y, int[] index, int[] trailing) {
	lock.lock();
	try {
		return _pango_layout_xy_to_index(layout, x, y, index, trailing);
	} finally {
		lock.unlock();
	}
}
/** @param tab_array cast=(PangoTabArray *) */
public static final native void _pango_tab_array_free(long tab_array);
public static final void pango_tab_array_free(long tab_array) {
	lock.lock();
	try {
		_pango_tab_array_free(tab_array);
	} finally {
		lock.unlock();
	}
}
/**
 * @param initial_size cast=(gint)
 * @param positions_in_pixels cast=(gboolean)
 */
public static final native long _pango_tab_array_new(int initial_size, boolean positions_in_pixels);
public static final long pango_tab_array_new(int initial_size, boolean positions_in_pixels) {
	lock.lock();
	try {
		return _pango_tab_array_new(initial_size, positions_in_pixels);
	} finally {
		lock.unlock();
	}
}
/**
 * @param tab_array cast=(PangoTabArray *)
 * @param tab_index cast=(gint)
 * @param alignment cast=(PangoTabAlign)
 * @param location cast=(gint)
 */
public static final native void _pango_tab_array_set_tab(long tab_array, int tab_index, long alignment, int location);
public static final void pango_tab_array_set_tab(long tab_array, int tab_index, long alignment, int location) {
	lock.lock();
	try {
		_pango_tab_array_set_tab(tab_array, tab_index, alignment, location);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 */
public static final native long _ubuntu_menu_proxy_get();
public static final long ubuntu_menu_proxy_get() {
	lock.lock();
	try {
		return _ubuntu_menu_proxy_get();
	} finally {
		lock.unlock();
	}
}
/**
 * @param path cast=(const char*)
 */
public static final native int _access (byte [] path, int amode);
public static final int access (byte [] path, int amode) {
	lock.lock();
	try {
		return _access(path, amode);
	} finally {
		lock.unlock();
	}
}
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
 * @param info cast=(GDBusInterfaceInfo *)
 * @param name cast=(const gchar *)
 * @param object_path cast=(const gchar *)
 * @param interface_name cast=(const gchar *)
 * @param cancellable cast=(GCancellable *)
 * @param error cast=(GError **)
 * @category gdbus
 */
public static final native long _g_dbus_proxy_new_for_bus_sync (int bus_type, int flags, long info, byte [] name, byte [] object_path, byte [] interface_name,
		long cancellable, long [] error);
public static final long g_dbus_proxy_new_for_bus_sync (int bus_type, int flags, long info, byte [] name, byte [] object_path, byte [] interface_name,
		long cancellable, long [] error) {
  lock.lock();
  try {
    return _g_dbus_proxy_new_for_bus_sync (bus_type, flags, info, name, object_path, interface_name, cancellable, error);
  } finally {
    lock.unlock();
  }
}

/**
 * @param proxy cast=(GDBusProxy *)
 * @param method_name cast=(const gchar *)
 * @param parameters cast=(GVariant *)
 * @param cancellable cast=(GCancellable *)
 * @param error cast=(GError **)
 * @category gdbus
 */
// NOTE: Lock is not used, see Bug 546743 and Bug 547093
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
// NOTE: Lock is not used, see Bug 546743 and Bug 547093
public static final native void g_dbus_proxy_call (long proxy, byte[] method_name, long parameters, int flags, int timeout_msec, long cancellable, long callback, long [] error);

/**
 * @param proxy cast=(GDBusProxy *)
 * @category gdbus
 */
public static final native long _g_dbus_proxy_get_name_owner (long proxy);
public static final long g_dbus_proxy_get_name_owner (long proxy) {
	lock.lock();
	try {
		return _g_dbus_proxy_get_name_owner (proxy);
	} finally {
		lock.unlock();
	}
}

/**
 * @param proxy cast=(GDBusProxy *)
 * @param res cast=(GAsyncResult *)
 * @param error cast=(GError **)
 * @category gdbus
 */
public static final native long _g_dbus_proxy_call_finish (long proxy, long res, long [] error);
public static final long g_dbus_proxy_call_finish (long proxy, long res, long [] error) {
	lock.lock();
	try {
		return _g_dbus_proxy_call_finish (proxy, res, error);
	} finally {
		lock.unlock();
	}
}

/**
 * @param xml_data cast=(const gchar *)
 * @param error cast=(GError **)
 * @category gdbus
 */
public static final native long _g_dbus_node_info_new_for_xml (byte[] xml_data, long [] error);
/** @category gdbus */
public static final long g_dbus_node_info_new_for_xml (byte[] xml_data, long [] error) {
  lock.lock();
  try {
    return _g_dbus_node_info_new_for_xml (xml_data, error);
  } finally {
    lock.unlock();
  }
}

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
public static final native int _g_bus_own_name (int bus_type, byte[] name, int flags, long bus_acquired_handler, long name_acquired_handler, long name_lost_handler, long  user_data, long user_data_free_func);
/** @category gdbus */
public static final int g_bus_own_name (int bus_type, byte[] name, int flags, long bus_acquired_handler, long name_acquired_handler, long name_lost_handler, long  user_data, long user_data_free_func) {
	lock.lock();
	try {
		return _g_bus_own_name(bus_type, name, flags, bus_acquired_handler, name_acquired_handler, name_lost_handler, user_data, user_data_free_func);
	} finally {
		lock.unlock();
	}
}

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
public static final native int _g_dbus_connection_register_object (long connection, byte[] object_path, long interface_info, long [] vtable, long user_data, long user_data_free_func, long [] error);
/** @category gdbus */
public static final int g_dbus_connection_register_object (long connection, byte[] object_path, long interface_info, long [] vtable, long user_data, long user_data_free_func, long [] error) {
	lock.lock();
	try {
		return _g_dbus_connection_register_object( connection,  object_path,  interface_info,  vtable,  user_data,  user_data_free_func, error);
	} finally {
		lock.unlock();
	}
}

/**
 * @param info cast=(GDBusNodeInfo *)
 * @param name cast=(const gchar *)
 * @category gdbus
 */
public static final native long _g_dbus_node_info_lookup_interface (long info, byte [] name);
/** @category gdbus */
public static final long g_dbus_node_info_lookup_interface (long info, byte [] name) {
	lock.lock();
	try {
		return _g_dbus_node_info_lookup_interface(info, name);
	} finally {
		lock.unlock();
	}
}

/**
 * @param invocation cast=(GDBusMethodInvocation *)
 * @param parameters cast=(GVariant *)
 * @category gdbus
 */
public static final native void _g_dbus_method_invocation_return_value (long invocation, long parameters);
/** @category gdbus */
public static final void g_dbus_method_invocation_return_value (long invocation, long parameters) {
	lock.lock();
	try {
		_g_dbus_method_invocation_return_value (invocation, parameters);
	} finally {
		lock.unlock();
	}
}

/**
 * @param type cast=(const GVariantType *)
 * @category gdbus
 */
public static final native long _g_variant_builder_new (long type);
/** @category gdbus */
public static final long g_variant_builder_new (long type) {
	lock.lock();
	try {
		return _g_variant_builder_new(type);
	} finally {
		lock.unlock();
	}
}

/**
 * @param builder cast=(GVariantBuilder *)
 * @param value cast=(GVariant *)
 * @category gdbus
 */
public static final native void _g_variant_builder_add_value (long builder, long value);
/** @category gdbus */
public static final void g_variant_builder_add_value (long builder, long value) {
	lock.lock();
	try {
		_g_variant_builder_add_value(builder, value);
	} finally {
		lock.unlock();
	}
}

/**
 * @param type cast=(GVariantType *)
 * @category gdbus
 */
public static final native void _g_variant_type_free (long type);
/** @category gdbus */
public static final void g_variant_type_free (long type) {
	lock.lock();
	try {
		_g_variant_type_free(type);
	} finally {
		lock.unlock();
	}
}

/**
 * @param type cast=(const gchar *)
 * @category gdbus
 */
public static final native long _g_variant_type_new (byte [] type);
/** @category gdbus */
public static final long g_variant_type_new (byte [] type) {
	lock.lock();
	try {
		return _g_variant_type_new(type);
	} finally {
		lock.unlock();
	}
}

/**
 * @param builder cast=(GVariantBuilder *)
 * @category gdbus
 */
public static final native long _g_variant_builder_end (long builder);
/** @category gdbus */
public static final long g_variant_builder_end (long builder) {
	lock.lock();
	try {
		return _g_variant_builder_end(builder);
	} finally {
		lock.unlock();
	}
}

/**
 * @param builder cast=(GVariantBuilder *)
 * @category gdbus
 */
public static final native void _g_variant_builder_unref (long builder);
/** @category gdbus */
public static final void g_variant_builder_unref (long builder) {
	lock.lock();
	try {
		_g_variant_builder_unref(builder);
	} finally {
		lock.unlock();
	}
}

/**
 * @param format_string cast=(const gchar *),flags=no_out
 * @param arg0 cast=(const gchar *),flags=no_out
 * @category gdbus
 */
// NOTE: Lock is not used, see Bug 546743 and Bug 547093
public static final native long g_variant_new (byte[] format_string, byte[] arg0);

/**
 * @param format_string cast=(const gchar *),flags=no_out
 * @param arg0 cast=(gboolean)
 * @param arg1 cast=(const gchar *),flags=no_out
 * @category gdbus
 */
// NOTE: Lock is not used, see Bug 546743 and Bug 547093
public static final native long g_variant_new (byte[] format_string, boolean arg0, byte[] arg1);

/**
 * @param format_string cast=(const gchar *),flags=no_out
 * @param arg0 cast=(const gchar *),flags=no_out
 * @param arg1 cast=(const gchar *),flags=no_out
 * @category gdbus
 */
// NOTE: Lock is not used, see Bug 546743 and Bug 547093
public static final native long g_variant_new (byte[] format_string, byte[] arg0, byte[] arg1);

/**
 * @param intval cast=(gint32)
 * @category gdbus
 */
public static final native long _g_variant_new_int32 (int intval);
/** @category gdbus */
public static final long g_variant_new_int32 (int intval) {
	lock.lock();
	try {
		return _g_variant_new_int32(intval);
	} finally {
		lock.unlock();
	}
}


/**
 * @param gvariant cast=(GVariant *)
 * @category gdbus
 * @return int
 */
public static final native int _g_variant_get_int32 (long gvariant);
/** @category gdbus */
public static final int g_variant_get_int32 (long gvariant) {
	lock.lock();
	try {
		return _g_variant_get_int32 (gvariant);
	} finally {
		lock.unlock();
	}
}

/**
 * @param gvariant cast=(GVariant *)
 * @category gdbus
 * @return guchar
 */
public static final native byte _g_variant_get_byte (long gvariant);
/** @category gdbus */
public static final byte g_variant_get_byte (long gvariant) {
	lock.lock();
	try {
		return _g_variant_get_byte (gvariant);
	} finally {
		lock.unlock();
	}
}

/**
 * @param gvariant cast=(GVariant *)
 * @category gdbus
 */
public static final native boolean _g_variant_get_boolean (long gvariant);
/** @category gdbus */
public static final boolean g_variant_get_boolean (long gvariant) {
	lock.lock();
	try {
		return _g_variant_get_boolean (gvariant);
	} finally {
		lock.unlock();
	}
}

/**
 * @param gvariant cast=(GVariant *)
 * @param index cast=(gsize)
 * @category gdbus
 */
public static final native long _g_variant_get_child_value (long gvariant, int index);
/** @category gdbus */
public static final long g_variant_get_child_value (long gvariant, int index) {
	lock.lock();
	try {
		return _g_variant_get_child_value (gvariant, index);
	} finally {
		lock.unlock();
	}
}

/**
 * @param gvariant cast=(GVariant *)
 * @category gdbus
 */
public static final native double _g_variant_get_double (long gvariant);
/** @category gdbus */
public static final double g_variant_get_double (long gvariant) {
	lock.lock();
	try {
		return _g_variant_get_double (gvariant);
	} finally {
		lock.unlock();
	}
}

public static final native long _g_variant_new_uint64 (long value);
public static final long g_variant_new_uint64 (long value) {
	lock.lock();
	try {
		return _g_variant_new_uint64 (value);
	} finally {
		lock.unlock();
	}
}

/**
 * @param gvariant cast=(GVariant *)
 * @category gdbus
 */
public static final native long _g_variant_get_uint64 (long gvariant);
/** @category gdbus */
public static final long g_variant_get_uint64 (long gvariant) {
	lock.lock();
	try {
		return _g_variant_get_uint64 (gvariant);
	} finally {
		lock.unlock();
	}
}

/**
 * @param gvariant cast=(GVariant *)
 * @param length cast=(gsize *)
 * @category gdbus
 */
public static final native long _g_variant_get_string (long gvariant, long[] length);
/** @category gdbus */
public static final long g_variant_get_string (long gvariant, long[] length) {
	lock.lock();
	try {
		return _g_variant_get_string (gvariant, length);
	} finally {
		lock.unlock();
	}
}

/**
 * @param gvariant cast=(GVariant *)
 * @return const GVariantType *
 * @category gdbus
 */
public static final native long _g_variant_get_type (long gvariant);
/** @category gdbus */
public static final long g_variant_get_type (long gvariant) {
	lock.lock();
	try {
		return _g_variant_get_type (gvariant);
	} finally {
		lock.unlock();
	}
}

/**
 * @param gvariant cast=(GVariant *)
 * @category gdbus
 */
public static final native long  _g_variant_get_type_string (long gvariant);
/** @category gdbus */
public static final long g_variant_get_type_string (long gvariant) {
	lock.lock();
	try {
		return _g_variant_get_type_string (gvariant);
	} finally {
		lock.unlock();
	}
}

/**
 * @param gvariant cast=(GVariant *)
 * @param type cast=(const GVariantType *)
 * @category gdbus
 */
public static final native boolean _g_variant_is_of_type (long gvariant, byte[] type);
/** @category gdbus */
public static final boolean g_variant_is_of_type (long gvariant, byte[] type) {
	lock.lock();
	try {
		return _g_variant_is_of_type (gvariant, type);
	} finally {
		lock.unlock();
	}
}

/**
 * @param gvariant cast=(GVariant *)
 * @category gdbus
 */
public static final native long _g_variant_n_children (long gvariant);
/** @category gdbus */
public static final long g_variant_n_children (long gvariant) {
	lock.lock();
	try {
		return _g_variant_n_children (gvariant);
	} finally {
		lock.unlock();
	}
}

/**
 * @param value cast=(gboolean)
 * @category gdbus
 */
public static final native long _g_variant_new_boolean (boolean value);
/** @category gdbus */
public static final long g_variant_new_boolean (boolean value) {
	lock.lock();
	try {
		return _g_variant_new_boolean (value);
	} finally {
		lock.unlock();
	}
}

/**
 * @param value cast=(gboolean)
 * @category gdbus
 */
public static final native long _g_variant_new_double (double value);
/** @category gdbus */
public static final long g_variant_new_double (double value) {
	lock.lock();
	try {
		return _g_variant_new_double (value);
	} finally {
		lock.unlock();
	}
}

/**
 * @param value cast=(guchar)
 * @category gdbus
 */
public static final native long _g_variant_new_byte (byte value);
/** @category gdbus */
public static final long g_variant_new_byte (byte value) {
	lock.lock();
	try {
		return _g_variant_new_byte (value);
	} finally {
		lock.unlock();
	}
}

/**
 * @param items cast=(GVariant * const *)
 * @param length cast=(gsize)
 * @category gdbus
 */
public static final native long _g_variant_new_tuple (long [] items, long length);
/** @category gdbus */
public static final long g_variant_new_tuple (long [] items, long length ) {
	lock.lock();
	try {
		return _g_variant_new_tuple (items, length);
	} finally {
		lock.unlock();
	}
}

/**
 * @param string cast=(const gchar *)
 * @category gdbus
 */
public static final native long _g_variant_new_string (byte[] string);
/** @category gdbus */
public static final long g_variant_new_string (byte[] string) {
	lock.lock();
	try {
		return _g_variant_new_string (string);
	} finally {
		lock.unlock();
	}
}

/**
 * @param value cast=(GVariant *)
 * @category gdbus
 */
public static final native void _g_variant_unref (long value);
/** @category gdbus */
public static final void g_variant_unref (long value) {
	lock.lock();
	try {
		_g_variant_unref (value);
	} finally {
		lock.unlock();
	}
}

/**
 * @param object cast=(GObject *)
 */
public static final native long _g_object_ref_sink(long object);

public static final long g_object_ref_sink(long object) {
	lock.lock();
	try {
		return _g_object_ref_sink(object);
	} finally {
		lock.unlock();
	}
}
}
