/*******************************************************************************
 * Copyright (c) 2018 Red Hat Inc. and others. All rights reserved.
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

/**
 * This class contains GDK specific native functions.
 *
 * In contrast to OS.java, dynamic functions are automatically linked, no need to add os_custom.h entries.
 */
public class GDK extends OS {

	/** Constants */
	public static final int GDK_2BUTTON_PRESS = 0x5;
	public static final int GDK_3BUTTON_PRESS = 0x6;
	public static final int GDK_ACTION_COPY = 1 << 1;
	public static final int GDK_ACTION_MOVE = 1 << 2;
	public static final int GDK_ACTION_LINK = 1 << 3;
	public static final int GDK_Alt_L = 0xffe9;
	public static final int GDK_Alt_R = 0xffea;
	public static final int GDK_BackSpace = 0xff08;
	public static final int GDK_BOTTOM_LEFT_CORNER = 0xc;
	public static final int GDK_BOTTOM_RIGHT_CORNER = 0xe;
	public static final int GDK_BOTTOM_SIDE = 0x10;
	public static final int GDK_BUTTON1_MASK = 0x100;
	public static final int GDK_BUTTON2_MASK = 0x200;
	public static final int GDK_BUTTON3_MASK = 0x400;
	public static final int GDK_BUTTON_MOTION_MASK	= 1 << 4;
	public static final int GDK_BUTTON1_MOTION_MASK	= 1 << 5;
	public static final int GDK_BUTTON2_MOTION_MASK	= 1 << 6;
	public static final int GDK_BUTTON3_MOTION_MASK	= 1 << 7;
	public static final int GDK_BUTTON_PRESS = 0x4;
	public static final int GDK_BUTTON_PRESS_MASK = 0x100;
	public static final int GDK_BUTTON_RELEASE = 0x7;
	public static final int GDK_BUTTON_RELEASE_MASK = 0x200;
	public static final int GDK_COLORSPACE_RGB = 0;
	public static final int GDK_CONFIGURE = 13;
	public static final int GDK_CONTROL_MASK = 0x4;
	public static final int GDK_CROSS = 0x1e;
	public static final int GDK_CROSSING_NORMAL = 0;
	public static final int GDK_CROSSING_GRAB = 1;
	public static final int GDK_CROSSING_UNGRAB = 2;
	public static final int GDK_Break = 0xff6b;
	public static final int GDK_Cancel = 0xff69;
	public static final int GDK_Caps_Lock = 0xffE5;
	public static final int GDK_Clear = 0xff0B;
	public static final int GDK_Control_L = 0xffe3;
	public static final int GDK_Control_R = 0xffe4;
	public static final int GDK_CURRENT_TIME = 0x0;
	public static final int GDK_DECOR_BORDER = 0x2;
	public static final int GDK_DECOR_MAXIMIZE = 0x40;
	public static final int GDK_DECOR_MENU = 0x10;
	public static final int GDK_DECOR_MINIMIZE = 0x20;
	public static final int GDK_DECOR_RESIZEH = 0x4;
	public static final int GDK_DECOR_TITLE = 0x8;
	public static final int GDK_DOUBLE_ARROW = 0x2a;
	public static final int GDK_Delete = 0xffff;
	public static final int GDK_Down = 0xff54;
	public static final int GDK_ENTER_NOTIFY_MASK = 0x1000;
	public static final int GDK_ENTER_NOTIFY = 10;
	public static final int GDK_EVEN_ODD_RULE = 0;
	public static final int GDK_EXPOSE = 2;
	public static final int GDK_EXPOSURE_MASK = 0x2;
	public static final int GDK_End = 0xff57;
	public static final int GDK_Escape = 0xff1b;
	public static final int GDK_ISO_Enter = 0xfe34;
	public static final int GDK_F1 = 0xffbe;
	public static final int GDK_F10 = 0xffc7;
	public static final int GDK_F11 = 0xffc8;
	public static final int GDK_F12 = 0xffc9;
	public static final int GDK_F13 = 0xffca;
	public static final int GDK_F14 = 0xffcb;
	public static final int GDK_F15 = 0xffcc;
	public static final int GDK_F16 = 0xffcd;
	public static final int GDK_F17 = 0xffce;
	public static final int GDK_F18 = 0xffcf;
	public static final int GDK_F19 = 0xffd0;
	public static final int GDK_F20 = 0xffd1;
	public static final int GDK_F2 = 0xffbf;
	public static final int GDK_F3 = 0xffc0;
	public static final int GDK_F4 = 0xffc1;
	public static final int GDK_F5 = 0xffc2;
	public static final int GDK_F6 = 0xffc3;
	public static final int GDK_F7 = 0xffc4;
	public static final int GDK_F8 = 0xffc5;
	public static final int GDK_F9 = 0xffc6;
	public static final int GDK_KEY_a = 0x061;
	public static final int GDK_KEY_z = 0x07a;
	public static final int GDK_FLEUR = 0x34;
	public static final int GDK_FOCUS_CHANGE = 0xc;
	public static final int GDK_FOCUS_CHANGE_MASK = 0x4000;
	public static final int GDK_FUNC_ALL = 1;
	public static final int GDK_FUNC_RESIZE = 2;
	public static final int GDK_FUNC_MOVE = 4;
	public static final int GDK_FUNC_MINIMIZE = 8;
	public static final int GDK_FUNC_MAXIMIZE = 16;
	public static final int GDK_FUNC_CLOSE = 32;
	public static final int GDK_GRAB_SUCCESS = 0x0;
	public static final int GDK_GRAVITY_NORTH_WEST = 1;
	public static final int GDK_GRAVITY_NORTH = 2;
	public static final int GDK_GRAVITY_NORTH_EAST = 3;
	public static final int GDK_GRAVITY_WEST = 4;
	public static final int GDK_GRAVITY_CENTER = 5;
	public static final int GDK_GRAVITY_EAST = 6;
	public static final int GDK_GRAVITY_SOUTH_WEST = 7;
	public static final int GDK_GRAVITY_SOUTH = 8;
	public static final int GDK_GRAVITY_SOUTH_EAST = 9;
	public static final int GDK_GRAVITY_STATIC = 10;
	public static final int GDK_HAND2 = 0x3c;
	public static final int GDK_Help = 0xFF6A;
	public static final int GDK_HINT_MIN_SIZE = 1 << 1;
	public static final int GDK_Home = 0xff50;
	public static final int GDK_INCLUDE_INFERIORS = 0x1;
	public static final int GDK_INPUT_ONLY = 1;
	public static final int GDK_INTERP_BILINEAR = 0x2;
	public static final int GDK_Insert = 0xff63;
	public static final int GDK_ISO_Left_Tab = 0xfe20;
	public static final int GDK_ISO_Level3_Shift = 0xfe03;
	public static final int GDK_KEY_PRESS = 0x8;
	public static final int GDK_KEY_PRESS_MASK = 0x400;
	public static final int GDK_KEY_RELEASE = 0x9;
	public static final int GDK_KEY_RELEASE_MASK = 0x800;
	public static final int GDK_KP_0 = 0xffb0;
	public static final int GDK_KP_1 = 0xffb1;
	public static final int GDK_KP_2 = 0xffb2;
	public static final int GDK_KP_3 = 0xffb3;
	public static final int GDK_KP_4 = 0xffb4;
	public static final int GDK_KP_5 = 0xffb5;
	public static final int GDK_KP_6 = 0xffb6;
	public static final int GDK_KP_7 = 0xffb7;
	public static final int GDK_KP_8 = 0xffb8;
	public static final int GDK_KP_9 = 0xffb9;
	public static final int GDK_KP_Add = 0xffab;
	public static final int GDK_KP_Decimal = 0xffae;
	public static final int GDK_KP_Delete = 0xFF9F;
	public static final int GDK_KP_Divide = 0xffaf;
	public static final int GDK_KP_Down = 0xFF99;
	public static final int GDK_KP_End = 0xFF9C;
	public static final int GDK_KP_Enter = 0xff8d;
	public static final int GDK_KP_Equal = 0xffbd;
	public static final int GDK_KP_Home = 0xFF95;
	public static final int GDK_KP_Insert = 0xFF9E;
	public static final int GDK_KP_Left = 0xFF96;
	public static final int GDK_KP_Multiply = 0xffaa;
	public static final int GDK_KP_Page_Down = 0xFF9B;
	public static final int GDK_KP_Page_Up = 0xFF9A;
	public static final int GDK_KP_Right = 0xFF98;
	public static final int GDK_KP_Subtract = 0xffad;
	public static final int GDK_KP_Up = 0xFF97;
	public static final int GDK_LEAVE_NOTIFY = 11;
	public static final int GDK_LEAVE_NOTIFY_MASK = 0x2000;
	public static final int GDK_LEFT_PTR = 0x44;
	public static final int GDK_LEFT_SIDE = 0x46;
	public static final int GDK_Linefeed = 0xff0A;
	public static final int GDK_Left = 0xff51;
	public static final int GDK_Meta_L = 0xFFE7;
	public static final int GDK_Meta_R = 0xFFE8;
	public static final int GDK_MAP = 14;

	/**
	 * <p>
	 * GDK_MOD1_MASK = usually 'alt' button   			<br>
	 * GDK_SUPER_MASK = usually win/cmd button			<br>
	 * GDK_HYPER_MASK = usually ctrl button				<br>
	 * By 'usually' I mean you should test. In my experience keyboard behaviour can vary.
	 * </p>
	 *
	 * See also:
	 * <a href="https://askubuntu.com/questions/19558/what-are-the-meta-super-and-hyper-keys">Stack Overflow post</a>
	 */
	public static final int GDK_MOD1_MASK = 1 << 3,
							GDK_MOD5_MASK = 1 << 7,
							GDK_SUPER_MASK = 0x4000000,
							GDK_HYPER_MASK = 0x8000000,
							GDK_META_MASK = 0x10000000;

	public static final int GDK_MOTION_NOTIFY = 0x3;
	public static final int GDK_NO_EXPOSE = 30;
	public static final int GDK_NONE = 0;
	public static final int GDK_NOTIFY_INFERIOR = 2;
	public static final int GDK_Num_Lock = 0xFF7F;
	public static final int GDK_OWNERSHIP_NONE = 0;
	public static final int GDK_PIXBUF_ALPHA_BILEVEL = 0x0;
	public static final int GDK_POINTER_MOTION_HINT_MASK = 0x8;
	public static final int GDK_POINTER_MOTION_MASK = 0x4;
	public static final int GDK_PROPERTY_NOTIFY = 16;
	public static final int GDK_PROPERTY_CHANGE_MASK = 1 << 16;
	public static final int GDK_Page_Down = 0xff56;
	public static final int GDK_Page_Up = 0xff55;
	public static final int GDK_Pause = 0xff13;
	public static final int GDK_Print = 0xff61;
	public static final int GDK_QUESTION_ARROW = 0x5c;
	public static final int GDK_RIGHT_SIDE = 0x60;
	public static final int GDK_Return = 0xff0d;
	public static final int GDK_Right = 0xff53;
	public static final int GDK_space = 0x20;
	public static final int GDK_SB_H_DOUBLE_ARROW = 0x6c;
	public static final int GDK_SB_UP_ARROW = 0x72;
	public static final int GDK_SB_V_DOUBLE_ARROW = 0x74;
	public static final int GDK_SEAT_CAPABILITY_NONE = 0;
	public static final int GDK_SEAT_CAPABILITY_POINTER = 1 << 0;
	public static final int GDK_SEAT_CAPABILITY_TOUCH = 1 << 1;
	public static final int GDK_SEAT_CAPABILITY_TABLET_STYLUS = 1 << 2;
	public static final int GDK_SEAT_CAPABILITY_KEYBOARD = 1 << 3;
	public static final int GDK_SEAT_CAPABILITY_TABLET_PAD = 1 << 4;
	public static final int GDK_SEAT_CAPABILITY_ALL_POINTING = GDK_SEAT_CAPABILITY_POINTER | GDK_SEAT_CAPABILITY_TOUCH | GDK_SEAT_CAPABILITY_TABLET_STYLUS;
	public static final int GDK_SEAT_CAPABILITY_ALL = GDK_SEAT_CAPABILITY_ALL_POINTING | GDK_SEAT_CAPABILITY_KEYBOARD;
	public static final int GDK_SCROLL_UP = 0;
	public static final int GDK_SCROLL_DOWN = 1;
	public static final int GDK_SCROLL_LEFT = 2;
	public static final int GDK_SCROLL_RIGHT = 3;
	public static final int GDK_SCROLL_SMOOTH = 4;
	public static final int GDK_SCROLL_MASK = 1 << 21;
	public static final int GDK_SMOOTH_SCROLL_MASK = 1 << 23;
	public static final int GDK_SELECTION_CLEAR = 17;
	public static final int GDK_SELECTION_NOTIFY = 19;
	public static final int GDK_SELECTION_REQUEST = 18;
	public static final int GDK_SHIFT_MASK = 0x1;
	public static final int GDK_SIZING = 0x78;
	public static final int GDK_STIPPLED = 0x2;
	public static final int GDK_SURFACE_STATE_ICONIFIED = 1 << 1;
	public static final int GDK_SURFACE_STATE_MAXIMIZED = 1 << 2;
	public static final int GDK_SURFACE_STATE_FULLSCREEN = 1 << 4;
	public static final int GDK_TILED = 0x1;
	public static final int GDK_Shift_L = 0xffe1;
	public static final int GDK_Shift_R = 0xffe2;
	public static final int GDK_SCROLL = 31;
	public static final int GDK_Scroll_Lock = 0xff14;
	public static final int GDK_TOP_LEFT_CORNER = 0x86;
	public static final int GDK_TOP_RIGHT_CORNER = 0x88;
	public static final int GDK_TOP_SIDE = 0x8a;
	public static final int GDK_Tab = 0xff09;
	public static final int GDK_Up = 0xff52;
	public static final int GDK_WATCH = 0x96;
	public static final int GDK_XOR = 0x2;
	public static final int GDK_XTERM = 0x98;
	public static final int GDK_X_CURSOR = 0x0;
	public static final int GDK_WINDOW_CHILD = 2;
	public static final int GDK_WINDOW_STATE = 32;
	public static final int GDK_WINDOW_STATE_ICONIFIED  = 1 << 1;
	public static final int GDK_WINDOW_STATE_MAXIMIZED  = 1 << 2;
	public static final int GDK_WINDOW_STATE_FULLSCREEN  = 1 << 4;
	public static final int GDK_WINDOW_STATE_FOCUSED = 1 << 7;
	public static final int GDK_UNMAP = 15;
	public static final int GDK_WA_X = 1 << 2;
	public static final int GDK_WA_Y = 1 << 3;
	public static final int GDK_WA_VISUAL = 1<<5;
	public static final int GDK_WINDOW_TYPE_HINT_DIALOG = 1;
	public static final int GDK_WINDOW_TYPE_HINT_UTILITY = 5;
	public static final int GDK_WINDOW_TYPE_HINT_TOOLTIP = 10;

	/** GdkEventType constants are different on GTK4 */
	public static final int GDK4_EXPOSE = 3;
	public static final int GDK4_MOTION_NOTIFY = 4;
	public static final int GDK4_BUTTON_PRESS = 5;
	public static final int GDK4_BUTTON_RELEASE = 6;
	public static final int GDK4_KEY_PRESS = 7;
	public static final int GDK4_KEY_RELEASE = 8;
	public static final int GDK4_ENTER_NOTIFY = 9;
	public static final int GDK4_LEAVE_NOTIFY = 10;
	public static final int GDK4_FOCUS_CHANGE = 11;
	public static final int GDK4_CONFIGURE = 12;
	public static final int GDK4_MAP = 13;
	public static final int GDK4_UNMAP = 14;

	/** sizeof(TYPE) for 32/64 bit support */
	public static final native int GdkKeymapKey_sizeof();
	public static final native int GdkRGBA_sizeof();
	public static final native int GdkEvent_sizeof();
	public static final native int GdkEventAny_sizeof();
	public static final native int GdkEventButton_sizeof();
	public static final native int GdkEventCrossing_sizeof();
	public static final native int GdkEventFocus_sizeof();
	public static final native int GdkEventKey_sizeof();
	public static final native int GdkEventMotion_sizeof();
	public static final native int GdkEventWindowState_sizeof();
	public static final native int GdkGeometry_sizeof();
	public static final native int GdkRectangle_sizeof();
	public static final native int GdkWindowAttr_sizeof();


	/** Macros */
	/** @param event cast=(GdkEvent *) */
	public static final native int GDK_EVENT_TYPE(long event);
	/** @param event cast=(GdkEventAny *) */
	public static final native long GDK_EVENT_WINDOW(long event);
	/** @param display cast=(GdkDisplay *) */
	public static final native boolean GDK_IS_X11_DISPLAY(long display);
	/** @method flags=const */
	public static final native long _GDK_TYPE_RGBA();
	public static final long GDK_TYPE_RGBA() {
		lock.lock();
		try {
			return _GDK_TYPE_RGBA();
		} finally {
			lock.unlock();
		}
	}
	/** @method flags=const */
	public static final native long _GDK_TYPE_PIXBUF();
	public static final long GDK_TYPE_PIXBUF() {
		lock.lock();
		try {
			return _GDK_TYPE_PIXBUF();
		} finally {
			lock.unlock();
		}
	}

	/** @param gdkdisplay cast=(GdkDisplay *) */
	public static final native long _gdk_x11_display_get_xdisplay(long gdkdisplay);
	public static final long gdk_x11_display_get_xdisplay (long gdkdisplay) {
		lock.lock();
		try {
			return _gdk_x11_display_get_xdisplay(gdkdisplay);
		} finally {
			lock.unlock();
		}
	}
	public static final native long _gdk_x11_get_default_xdisplay();
	public static final long gdk_x11_get_default_xdisplay () {
		lock.lock();
		try {
			return _gdk_x11_get_default_xdisplay();
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param xvisualid cast=(VisualID)
	 */
	public static final native long _gdk_x11_screen_lookup_visual(long screen, int xvisualid);
	/** [GTK3/GTK4, GTK3 uses GdkScreen but GTK4 uses GdkX11Screen -- method signature otherwise identical] */
	public static final long gdk_x11_screen_lookup_visual(long screen, int xvisualid) {
		lock.lock();
		try {
			return _gdk_x11_screen_lookup_visual(screen, xvisualid);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native long _gdk_x11_screen_get_window_manager_name(long screen);
	/** [GTK3/GTK4, GTK3 uses GdkScreen but GTK4 uses GdkX11Screen -- method signature otherwise identical] */
	public static final long gdk_x11_screen_get_window_manager_name(long screen) {
		lock.lock();
		try {
			return _gdk_x11_screen_get_window_manager_name(screen);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native long _gdk_x11_visual_get_xvisual(long visual);
	/** [GTK3/GTK4, GTK3 uses GdkVisual but GTK4 uses GdkX11Visual -- method signature otherwise identical] */
	public static final long gdk_x11_visual_get_xvisual(long visual) {
		lock.lock();
		try {
			return _gdk_x11_visual_get_xvisual(visual);
		} finally {
			lock.unlock();
		}
	}
	/**
	* @param gdkwindow cast=(GdkWindow *)
	*/
	public static final native long _gdk_x11_window_get_xid(long gdkwindow);
	/** [GTK3 only, if-def'd in os.h] */
	public static final long gdk_x11_window_get_xid(long gdkwindow) {
		lock.lock();
		try {
			return _gdk_x11_window_get_xid(gdkwindow);
		} finally {
			lock.unlock();

		}
	}
	/**
	* @param surface cast=(GdkSurface *)
	*/
	public static final native long _gdk_x11_surface_get_xid(long surface);
	/** [GTK4 only, if-def'd in os.h] */
	public static final long gdk_x11_surface_get_xid(long surface) {
		lock.lock();
		try {
			return _gdk_x11_surface_get_xid(surface);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param gdkdisplay cast=(GdkDisplay *)
	 * @param xid cast=(Window)
	 */
	public static final native long _gdk_x11_window_lookup_for_display(long gdkdisplay, long xid);
	/** [GTK3 only, if-def'd in os.h] */
	public static final long gdk_x11_window_lookup_for_display(long gdkdisplay, long xid) {
		lock.lock();
		try {
			return _gdk_x11_window_lookup_for_display(gdkdisplay, xid);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param gdkdisplay cast=(GdkDisplay *)
	 * @param xid cast=(Window)
	 */
	public static final native long _gdk_x11_surface_lookup_for_display(long gdkdisplay, long xid);
	/** [GTK4 only, if-def'd in os.h] */
	public static final long gdk_x11_surface_lookup_for_display(long gdkdisplay, long xid) {
		lock.lock();
		try {
			return _gdk_x11_surface_lookup_for_display(gdkdisplay, xid);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param atom_name cast=(const gchar *),flags=no_out critical
	 */
	public static final native long _gdk_atom_intern(byte[] atom_name, boolean only_if_exists);
	/** [GTK3 only] */
	public static final long gdk_atom_intern(byte[] atom_name, boolean only_if_exists) {
		lock.lock();
		try {
			return _gdk_atom_intern(atom_name, only_if_exists);
		} finally {
			lock.unlock();
		}
	}
	/** @param atom cast=(GdkAtom) */
	public static final native long _gdk_atom_name(long atom);
	/** [GTK3 only, if-def'd in os.h] */
	public static final long gdk_atom_name(long atom) {
		lock.lock();
		try {
			return _gdk_atom_name(atom);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param display cast=(GdkDisplay *)
	 */
	public static final native void _gdk_display_beep(long display);
	public static final void gdk_display_beep(long display) {
		lock.lock();
		try {
			_gdk_display_beep(display);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param window cast=(GdkWindow *)
	 */
	public static final native long _gdk_cairo_create(long window);
	/** [GTK3 only, if-def'd in os.h; 3.22 deprecated, replaced] */
	public static final long gdk_cairo_create(long window) {
		lock.lock();
		try {
			return _gdk_cairo_create(window);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param cr cast=(cairo_t *)
	 * @param rect cast=(GdkRectangle *),flags=no_in
	 */
	public static final native boolean _gdk_cairo_get_clip_rectangle(long cr, GdkRectangle rect);
	public static final boolean gdk_cairo_get_clip_rectangle(long cr, GdkRectangle rect) {
		lock.lock();
		try {
			return _gdk_cairo_get_clip_rectangle(cr, rect);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param cairo cast=(cairo_t *)
	 * @param region cast=(cairo_region_t *)
	 */
	public static final native void _gdk_cairo_region(long cairo, long region);
	public static final void gdk_cairo_region(long cairo, long region) {
		lock.lock();
		try {
			_gdk_cairo_region(cairo, region);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param cairo cast=(cairo_t *)
	 * @param rgba cast=(const GdkRGBA *)
	 */
	public static final native void _gdk_cairo_set_source_rgba(long cairo, GdkRGBA rgba);
	public static final void gdk_cairo_set_source_rgba(long cairo, GdkRGBA rgba) {
		lock.lock();
		try {
			_gdk_cairo_set_source_rgba(cairo, rgba);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GdkWindow *)
	 * @param region cast=(cairo_region_t *)
	 * @method flags=dynamic
	 */
	public static final native long _gdk_window_begin_draw_frame(long window, long region);
	public static final long gdk_window_begin_draw_frame(long window, long region) {
		lock.lock();
		try {
			return _gdk_window_begin_draw_frame(window, region);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GdkWindow *)
	 * @param context cast=(GdkDrawingContext *)
	 * @method flags=dynamic
	 */
	public static final native long _gdk_window_end_draw_frame(long window, long context);
	public static final long gdk_window_end_draw_frame(long window, long context) {
		lock.lock();
		try {
			return _gdk_window_end_draw_frame(window, context);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GdkWindow *)
	 */
	public static final native int _gdk_window_get_state(long window);
	/** [GTK3 only, if-def'd in os.h] */
	public static final int gdk_window_get_state(long window) {
		lock.lock();
		try {
			return _gdk_window_get_state(window);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GdkWindow *)
	 */
	public static final native int _gdk_window_get_width(long window);
	/** [GTK3 only, if-def'd in os.h] */
	public static final int gdk_window_get_width(long window) {
		lock.lock();
		try {
			return _gdk_window_get_width(window);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param surface cast=(GdkSurface *)
	 */
	public static final native int _gdk_surface_get_width(long surface);
	/** [GTK4 only, if-def'd in os.h] */
	public static final int gdk_surface_get_width(long surface) {
		lock.lock();
		try {
			return _gdk_surface_get_width(surface);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param surface cast=(GdkSurface *)
	 */
	public static final native int _gdk_surface_get_state(long surface);
	/** [GTK4 only, if-def'd in os.h] */
	public static final int gdk_surface_get_state(long surface) {
		lock.lock();
		try {
			return _gdk_surface_get_state(surface);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GdkWindow *)
	 */
	public static final native long _gdk_window_get_visible_region(long window);
	/** [GTK3 only, if-def'd in os.h] */
	public static final long gdk_window_get_visible_region(long window) {
		lock.lock();
		try {
			return _gdk_window_get_visible_region(window);
		} finally {
			lock.unlock();
		}
	}
	/**
	 *  @param window cast=(GdkWindow *)
	 */
	public static final native int _gdk_window_get_height(long window);
	/** [GTK3 only, if-def'd in os.h] */
	public static final int gdk_window_get_height(long window) {
		lock.lock();
		try {
			return _gdk_window_get_height(window);
		} finally {
			lock.unlock();
		}
	}
	/**
	 *  @param surface cast=(GdkSurface *)
	 */
	public static final native int _gdk_surface_get_height(long surface);
	/** [GTK4 only, if-def'd in os.h] */
	public static final int gdk_surface_get_height(long surface) {
		lock.lock();
		try {
			return _gdk_surface_get_height(surface);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param cairo cast=(cairo_t *)
	 * @param pixbuf cast=(const GdkPixbuf *)
	 * @param pixbuf_x cast=(gdouble)
	 * @param pixbuf_y cast=(gdouble)
	 */
	public static final native void _gdk_cairo_set_source_pixbuf(long cairo, long pixbuf, double pixbuf_x, double pixbuf_y);
	public static final void gdk_cairo_set_source_pixbuf(long cairo, long pixbuf, double pixbuf_x, double pixbuf_y) {
	        lock.lock();
	        try {
	                _gdk_cairo_set_source_pixbuf(cairo,pixbuf,pixbuf_x,pixbuf_y);
	        }
	        finally {
	                lock.unlock();
	        }
	}
	/**
	 * @param cairo cast=(cairo_t *)
	 * @param window cast=(GdkWindow *)
	 * @param x cast=(gdouble)
	 * @param y cast=(gdouble)
	 */
	public static final native void _gdk_cairo_set_source_window(long cairo, long window, int x, int y);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gdk_cairo_set_source_window(long cairo, long window, int x, int y) {
	        lock.lock();
	        try {
	                _gdk_cairo_set_source_window(cairo, window, x, y);
	        }
	        finally {
	                lock.unlock();
	        }
	}
	/** @param display cast=(GdkDisplay *)
	 *  @param cursor_type cast=(GdkCursorType)
	 */
	public static final native long _gdk_cursor_new_for_display(long display, long cursor_type);
	/** [GTK3 only, if-def'd in os.h] */
	public static final long gdk_cursor_new_for_display(long display, long cursor_type) {
		lock.lock();
		try {
			return _gdk_cursor_new_for_display(display, cursor_type);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param display cast=(GdkDisplay *)
	 * @param cursor_name cast=(const gchar *)
	 */
	public static final native long _gdk_cursor_new_from_name(long display, byte[] cursor_name);
	/** [GTK3 only, if-def'd in os.h] */
	public static final long gdk_cursor_new_from_name(long display, byte[] cursor_name) {
		lock.lock();
		try {
			return _gdk_cursor_new_from_name(display, cursor_name);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param cursor_name cast=(const gchar *)
	 * @param fallback cast=(GdkCursor *)
	 */
	public static final native long _gdk_cursor_new_from_name(byte[] cursor_name, long fallback);
	/** [GTK4 only, if-def'd in os.h] */
	public static final long gdk_cursor_new_from_name(byte[] cursor_name, long fallback) {
		lock.lock();
		try {
			return _gdk_cursor_new_from_name(cursor_name, fallback);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param display cast=(GdkDisplay *)
	 * @param pixbuf cast=(GdkPixbuf *)
	 * @param x cast=(gint)
	 * @param y cast=(gint)
	 */
	public static final native long _gdk_cursor_new_from_pixbuf(long display, long pixbuf, int x, int y);
	/** [GTK3 only, if-def'd in os.h] */
	public static final long gdk_cursor_new_from_pixbuf(long display, long pixbuf, int x, int y) {
		lock.lock();
		try {
			return _gdk_cursor_new_from_pixbuf(display, pixbuf, x, y);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param texture cast=(GdkTexture *)
	 * @param x cast=(gint)
	 * @param y cast=(gint)
	 * @param fallback cast=(GdkCursor *)
	 */
	public static final native long _gdk_cursor_new_from_texture(long texture, int x, int y, long fallback);
	/** [GTK4 only, if-def'd in os.h] */
	public static final long gdk_cursor_new_from_texture(long texture, int x, int y, long fallback) {
		lock.lock();
		try {
			return _gdk_cursor_new_from_texture(texture, x, y, fallback);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param device cast=(GdkDevice *)
	 * @param screen cast=(GdkScreen *)
	 * @param x cast=(gint)
	 * @param y cast=(gint)
	 */
	public static final native void _gdk_device_warp(long device, long screen, int x, int y);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gdk_device_warp(long device, long screen, int x, int y) {
		lock.lock();
		try {
			_gdk_device_warp(device, screen, x, y);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param device cast=(GdkDevice *)
	 * @param x cast=(gint)
	 * @param y cast=(gint)
	 */
	public static final native void _gdk_device_warp(long device, int x, int y);
	/** [GTK4 only, if-def'd in os.h] */
	public static final void gdk_device_warp(long device, int x, int y) {
		lock.lock();
		try {
			_gdk_device_warp(device, x, y);
		} finally {
			lock.unlock();
		}
	}
	public static final native long _gdk_display_get_default();
	public static final long gdk_display_get_default() {
		lock.lock();
		try {
			return _gdk_display_get_default();
		} finally {
			lock.unlock();
		}
	}
	/**
	 *  @param display cast=(GdkDisplay *)
	 */
	public static final native long _gdk_display_get_default_group(long display);
	public static final long gdk_display_get_default_group(long display) {
		lock.lock();
		try {
			return _gdk_display_get_default_group(display);
		} finally {
			lock.unlock();
		}
	}
	/**
	 *  @param display cast=(GdkDisplay *)
	 */
	public static final native long _gdk_display_get_clipboard(long display);
	/** [GTK4 only, if-def'd in os.h] */
	public static final long gdk_display_get_clipboard(long display) {
		lock.lock();
		try {
			return _gdk_display_get_clipboard(display);
		} finally {
			lock.unlock();
		}
	}
	/**
	 *  @param display cast=(GdkDisplay *)
	 */
	public static final native long _gdk_display_get_primary_clipboard(long display);
	/** [GTK4 only, if-def'd in os.h] */
	public static final long gdk_display_get_primary_clipboard(long display) {
		lock.lock();
		try {
			return _gdk_display_get_primary_clipboard(display);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native long _gdk_display_get_default_seat(long display);
	public static final long gdk_display_get_default_seat(long display) {
		lock.lock();
		try {
			return _gdk_display_get_default_seat(display);
		} finally {
			lock.unlock();
		}
	}
	/**
	 *  @param window cast=(GdkWindow *)
	 */
	public static final native long _gdk_window_get_display(long window);
	/** [GTK3 only, if-def'd in os.h] */
	public static final long gdk_window_get_display(long window) {
		lock.lock();
		try {
			return _gdk_window_get_display(window);
		} finally {
			lock.unlock();
		}
	}
	/**
	 *  @param surface cast=(GdkSurface *)
	 */
	public static final native long _gdk_surface_get_display(long surface);
	/** [GTK4 only, if-def'd in os.h] */
	public static final long gdk_surface_get_display(long surface) {
		lock.lock();
		try {
			return _gdk_surface_get_display(surface);
		} finally {
			lock.unlock();
		}
	}
	/**
	 *  @method flags=dynamic
	 *  @param display cast=(GdkDisplay *)
	 */
	public static final native long _gdk_display_get_device_manager(long display);
	public static final long gdk_display_get_device_manager(long display) {
		lock.lock();
		try {
			return _gdk_display_get_device_manager(display);
		} finally {
			lock.unlock();
		}
	}
	/** @method flags=dynamic */
	public static final native long _gdk_device_manager_get_client_pointer(long device_manager);
	public static final long gdk_device_manager_get_client_pointer(long device_manager) {
		lock.lock();
		try {
			return _gdk_device_manager_get_client_pointer(device_manager);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param device cast=(GdkDevice *)
	 * @param win_x cast=(gint *)
	 * @param win_y cast=(gint *)
	 */
	public static final native long _gdk_device_get_window_at_position(long device, int[] win_x, int[] win_y);
	/** [GTK3 only] */
	public static final long gdk_device_get_window_at_position(long device, int[] win_x, int[] win_y) {
		lock.lock();
		try {
			return _gdk_device_get_window_at_position(device, win_x, win_y);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param device cast=(GdkDevice *)
	 * @param win_x cast=(gint *)
	 * @param win_y cast=(gint *)
	 */
	public static final native long _gdk_device_get_surface_at_position(long device, int[] win_x, int[] win_y);
	/** [GTK4 only] */
	public static final long gdk_device_get_surface_at_position(long device, int[] win_x, int[] win_y) {
		lock.lock();
		try {
			return _gdk_device_get_surface_at_position(device, win_x, win_y);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param display cast=(GdkDisplay *)
	 */
	public static final native boolean _gdk_display_supports_cursor_color(long display);
	/** [GTK3 only, if-def'd in os.h] */
	public static final boolean gdk_display_supports_cursor_color(long display) {
		lock.lock();
		try {
			return _gdk_display_supports_cursor_color(display);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param context cast=(GdkDragContext *)
	 */
	public static final native int _gdk_drag_context_get_actions(long context);
	public static final int gdk_drag_context_get_actions(long context) {
		lock.lock();
		try {
			return _gdk_drag_context_get_actions(context);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param context cast=(GdkDragContext *)
	 */
	public static final native long _gdk_drag_context_get_dest_window(long context);
	/** [GTK3 only, if-def'd in os.h] */
	public static final long gdk_drag_context_get_dest_window(long context) {
		lock.lock();
		try {
			return _gdk_drag_context_get_dest_window(context);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param context cast=(GdkDragContext *)
	 */
	public static final native int _gdk_drag_context_get_selected_action(long context);
	public static final int gdk_drag_context_get_selected_action(long context) {
		lock.lock();
		try {
			return _gdk_drag_context_get_selected_action(context);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param context cast=(GdkDragContext *)
	 */
	public static final native long _gdk_drag_context_list_targets(long context);
	/** [GTK3 only, if-def'd in os.h] */
	public static final long gdk_drag_context_list_targets(long context) {
		lock.lock();
		try {
			return _gdk_drag_context_list_targets(context);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param context cast=(GdkDragContext *)
	 * @param action cast=(GdkDragAction)
	 * @param time cast=(guint32)
	 */
	public static final native void _gdk_drag_status(long context, int action, int time);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gdk_drag_status(long context, int action, int time) {
		lock.lock();
		try {
			_gdk_drag_status(context, action, time);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param context cast=(GdkDrawingContext *)
	 * @method flags=dynamic
	 */
	public static final native long _gdk_drawing_context_get_cairo_context(long context);
	public static final long gdk_drawing_context_get_cairo_context(long context) {
		lock.lock();
		try {
			return _gdk_drawing_context_get_cairo_context(context);
		} finally {
			lock.unlock();
		}
	}
	/** @param event cast=(GdkEvent *) */
	public static final native long _gdk_event_copy(long event);
	public static final long gdk_event_copy(long event) {
		lock.lock();
		try {
			return _gdk_event_copy(event);
		} finally {
			lock.unlock();
		}
	}
	/** @param event cast=(GdkEvent *) */
	public static final native void _gdk_event_free(long event);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gdk_event_free(long event) {
		lock.lock();
		try {
			_gdk_event_free(event);
		} finally {
			lock.unlock();
		}
	}
	public static final native long _gdk_event_get();
	/** [GTK3 only, if-def'd in os.h] */
	public static final long gdk_event_get() {
		lock.lock();
		try {
			return _gdk_event_get();
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param event cast=(GdkEvent *)
	 * @param px cast=(gdouble *)
	 * @param py cast=(gdouble *)
	 */
	public static final native boolean _gdk_event_get_coords(long event, double[] px, double[] py);
	public static final boolean gdk_event_get_coords(long event, double[] px, double[] py) {
		lock.lock();
		try {
			return _gdk_event_get_coords(event, px, py);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param event cast=(GdkEvent *)
	 * @param mode cast=(GdkCrossingMode *)
	 */
	public static final native boolean _gdk_event_get_crossing_mode(long event, int [] mode);
	/** [GTK4 only, if-def'd in os.h] */
	public static final boolean gdk_event_get_crossing_mode(long event, int [] mode) {
		lock.lock();
		try {
			return _gdk_event_get_crossing_mode(event, mode);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param event cast=(GdkEvent *)
	 * @param button cast=(guint *)
	 */
	public static final native boolean _gdk_event_get_button(long event, int[] button);
	public static final boolean gdk_event_get_button(long event, int[] button) {
		lock.lock();
		try {
			return _gdk_event_get_button(event, button);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param event cast=(GdkEvent *)
	 * @param in cast=(gboolean *)
	 */
	public static final native boolean _gdk_event_get_focus_in(long event, boolean [] in);
	/** [GTK4 only, if-def'd in os.h] */
	public static final boolean gdk_event_get_focus_in(long event, boolean [] in) {
		lock.lock();
		try {
			return _gdk_event_get_focus_in(event, in);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param event cast=(GdkEvent *)
	 * @param keyval cast=(guint *)
	 */
	public static final native boolean _gdk_event_get_keyval(long event,int [] keyval);
	public static final boolean gdk_event_get_keyval(long event, int [] keyval) {
		lock.lock();
		try {
			return _gdk_event_get_keyval(event, keyval);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param event cast=(GdkEvent *)
	 * @param keycode cast=(guint16 *)
	 */
	public static final native boolean _gdk_event_get_keycode(long event, short [] keycode);
	public static final boolean gdk_event_get_keycode(long event, short [] keycode) {
		lock.lock();
		try {
			return _gdk_event_get_keycode(event, keycode);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param event cast=(GdkEvent *)
	 * @param group cast=(guint *)
	 */
	public static final native boolean _gdk_event_get_key_group(long event,int [] group);
	/** [GTK4 only, if-def'd in os.h] */
	public static final boolean gdk_event_get_key_group(long event, int [] group) {
		lock.lock();
		try {
			return _gdk_event_get_key_group(event, group);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param event cast=(GdkEvent *)
	 * @param x cast=(gdouble *)
	 * @param y cast=(gdouble *)
	 */
	public static final native boolean _gdk_event_get_root_coords(long event, double[] x, double[] y);
	public static final boolean gdk_event_get_root_coords(long event, double[] x, double[] y) {
		lock.lock();
		try {
			return _gdk_event_get_root_coords(event, x, y);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param event cast=(GdkEvent *)
	 * @param pmod cast=(GdkModifierType *)
	 */
	public static final native boolean _gdk_event_get_state(long event, int[] pmod);
	public static final boolean gdk_event_get_state(long event, int[] pmod) {
		lock.lock();
		try {
			return _gdk_event_get_state(event, pmod);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param event cast=(GdkEvent *)
	 * @param string cast=(const char **)
	 */
	public static final native boolean _gdk_event_get_string(long event, long [] string);
	/** [GTK4 only, if-def'd in os.h] */
	public static final boolean gdk_event_get_string(long event, long [] string) {
		lock.lock();
		try {
			return _gdk_event_get_string(event, string);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param event cast=(const GdkEvent *)
	 * @param delta_x cast=(gdouble *)
	 * @param delta_y cast=(gdouble *)
	 */
	public static final native boolean _gdk_event_get_scroll_deltas(long event, double[] delta_x, double[] delta_y);
	public static final boolean gdk_event_get_scroll_deltas(long event, double[] delta_x, double[] delta_y) {
		lock.lock();
		try {
			return _gdk_event_get_scroll_deltas(event, delta_x, delta_y);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param event cast=(const GdkEvent *)
	 * @param direction cast=(GdkScrollDirection *)
	 */
	public static final native boolean _gdk_event_get_scroll_direction(long event, int [] direction);
	public static final boolean gdk_event_get_scroll_direction(long event, int [] direction) {
		lock.lock();
		try {
			return _gdk_event_get_scroll_direction(event, direction);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native long _gdk_event_get_seat(long event);
	public static final long gdk_event_get_seat(long event) {
		lock.lock();
		try {
			return _gdk_event_get_seat(event);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param event cast=(GdkEvent *)
	 */
	public static final native long _gdk_event_get_surface(long event);
	/** [GTK4 only, if-def'd in os.h] **/
	public static final long gdk_event_get_surface(long event) {
		lock.lock();
		try {
			return _gdk_event_get_surface(event);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param event cast=(GdkEvent *)
	 */
	public static final native long _gdk_event_get_window(long event);
	/** [GTK3 only, if-def'd in os.h] **/
	public static final long gdk_event_get_window(long event) {
		lock.lock();
		try {
			return _gdk_event_get_window(event);
		} finally {
			lock.unlock();
		}
	}
	/** @param event cast=(GdkEvent *) */
	public static final native int _gdk_event_get_time(long event);
	public static final int gdk_event_get_time(long event) {
		lock.lock();
		try {
			return _gdk_event_get_time(event);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param event cast=(GdkEvent *)
	 */
	public static final native int _gdk_event_get_event_type(long event);
	/** [GTK3.10+] */
	public static final int gdk_event_get_event_type(long event) {
		lock.lock();
		try {
			return _gdk_event_get_event_type(event);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param func cast=(GdkEventFunc)
	 * @param data cast=(gpointer)
	 * @param notify cast=(GDestroyNotify)
	 */
	public static final native void _gdk_event_handler_set(long func, long data, long notify);
	public static final void gdk_event_handler_set(long func, long data, long notify) {
		lock.lock();
		try {
			_gdk_event_handler_set(func, data, notify);
		} finally {
			lock.unlock();
		}
	}
	public static final native long _gdk_event_new(int type);
	public static final long gdk_event_new(int type) {
		lock.lock();
		try {
			return _gdk_event_new(type);
		} finally {
			lock.unlock();
		}
	}
	public static final native long _gdk_event_peek();
	/** [GTK3 only, if-def'd in os.h] */
	public static final long gdk_event_peek() {
		lock.lock();
		try {
			return _gdk_event_peek();
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param event cast=(GdkEvent *)
	 * @param device cast=(GdkDevice *)
	 */
	public static final native void _gdk_event_set_device(long event, long device);
	public static final void gdk_event_set_device(long event, long device) {
		lock.lock();
		try {
			_gdk_event_set_device(event, device);
		} finally {
			lock.unlock();
		}
	}
	/** @param event cast=(GdkEvent *) */
	public static final native void _gdk_event_put(long event);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gdk_event_put(long event) {
		lock.lock();
		try {
			_gdk_event_put(event);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param display cast=(GdkDisplay *)
	 */
	public static final native void _gdk_x11_display_error_trap_push(long display);
	public static final void gdk_x11_display_error_trap_push(long display) {
		lock.lock();
		try {
			_gdk_x11_display_error_trap_push(display);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param display cast=(GdkDisplay *)
	 */
	public static final native void _gdk_x11_display_error_trap_pop_ignored(long display);
	public static final void gdk_x11_display_error_trap_pop_ignored(long display) {
		lock.lock();
		try {
			_gdk_x11_display_error_trap_pop_ignored(display);
		} finally {
			lock.unlock();
		}
	}
	public static final native long _gdk_get_default_root_window();
	/** [GTK3 only, if-def'd in os.h] */
	public static final long gdk_get_default_root_window() {
		lock.lock();
		try {
			return _gdk_get_default_root_window();
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param display cast=(GdkDisplay *)
	 */
	public static final native long _gdk_keymap_get_for_display(long display);
	/** [GTK3 only] */
	public static final long gdk_keymap_get_for_display(long display) {
		lock.lock();
		try {
			return _gdk_keymap_get_for_display(display);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param display cast=(GdkDisplay *)
	 */
	public static final native long _gdk_display_get_keymap(long display);
	/** [GTK4 only, if-def'd in os.h] */
	public static final long gdk_display_get_keymap(long display) {
		lock.lock();
		try {
			return _gdk_display_get_keymap(display);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param keymap cast=(GdkKeymap *)
	 * @param hardware_keycode cast=(guint)
	 * @param state cast=(GdkModifierType)
	 * @param group cast=(gint)
	 * @param keyval cast=(guint *)
	 * @param effective_group cast=(gint *)
	 * @param level cast=(gint *)
	 * @param consumed_modifiers cast=(GdkModifierType *)
	 */
	public static final native boolean _gdk_keymap_translate_keyboard_state (long keymap, int hardware_keycode, int state, int group, int[] keyval, int[] effective_group, int[] level,  int[] consumed_modifiers);
	public static final boolean gdk_keymap_translate_keyboard_state (long keymap, int hardware_keycode, int state, int group, int[] keyval, int[] effective_group, int[] level,  int[] consumed_modifiers) {
		lock.lock();
		try {
			return _gdk_keymap_translate_keyboard_state(keymap, hardware_keycode, state, group, keyval, effective_group, level, consumed_modifiers);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param keymap cast=(GdkKeymap*)
	 * @param keyval cast=(guint)
	 * @param keys cast=(GdkKeymapKey**)
	 * @param n_keys cast=(gint*)
	 */
	public static final native boolean _gdk_keymap_get_entries_for_keyval (long keymap, int keyval, long [] keys, int[] n_keys);
	public static final boolean gdk_keymap_get_entries_for_keyval (long keymap, int keyval, long [] keys, int[] n_keys) {
		lock.lock();
		try {
			return _gdk_keymap_get_entries_for_keyval(keymap, keyval, keys, n_keys);
	 	} finally {
	 		lock.unlock();
	 	}
	}
	public static final native long _gdk_keyval_to_lower(long keyval);
	public static final long gdk_keyval_to_lower(long keyval) {
		lock.lock();
		try {
			return _gdk_keyval_to_lower(keyval);
		} finally {
			lock.unlock();
		}
	}
	public static final native long _gdk_keyval_to_unicode(long keyval);
	public static final long gdk_keyval_to_unicode(long keyval) {
		lock.lock();
		try {
			return _gdk_keyval_to_unicode(keyval);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native long _gdk_pango_context_get();
	/** [GTK3 only] */
	public static final long gdk_pango_context_get() {
		lock.lock();
		try {
			return _gdk_pango_context_get();
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param layout cast=(PangoLayout *)
	 * @param index_ranges cast=(gint *)
	 */
	public static final native long _gdk_pango_layout_get_clip_region(long layout, int x_origin, int y_origin, int[] index_ranges, int n_ranges);
	public static final long gdk_pango_layout_get_clip_region(long layout, int x_origin, int y_origin, int[] index_ranges, int n_ranges) {
		lock.lock();
		try {
			return _gdk_pango_layout_get_clip_region(layout, x_origin, y_origin, index_ranges, n_ranges);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param src_pixbuf cast=(GdkPixbuf *)
	 * @param dest_pixbuf cast=(GdkPixbuf *)
	 */
	public static final native void _gdk_pixbuf_copy_area(long src_pixbuf, int src_x, int src_y, int width, int height, long dest_pixbuf, int dest_x, int dest_y);
	public static final void gdk_pixbuf_copy_area(long src_pixbuf, int src_x, int src_y, int width, int height, long dest_pixbuf, int dest_x, int dest_y) {
		lock.lock();
		try {
			_gdk_pixbuf_copy_area(src_pixbuf, src_x, src_y, width, height, dest_pixbuf, dest_x, dest_y);
		} finally {
			lock.unlock();
		}
	}
	/** @param pixbuf cast=(const GdkPixbuf *) */
	public static final native boolean _gdk_pixbuf_get_has_alpha(long pixbuf);
	public static final boolean gdk_pixbuf_get_has_alpha(long pixbuf) {
		lock.lock();
		try {
			return _gdk_pixbuf_get_has_alpha(pixbuf);
		} finally {
			lock.unlock();
		}
	}
	/** @param pixbuf cast=(const GdkPixbuf *) */
	public static final native int _gdk_pixbuf_get_height(long pixbuf);
	public static final int gdk_pixbuf_get_height(long pixbuf) {
		lock.lock();
		try {
			return _gdk_pixbuf_get_height(pixbuf);
		} finally {
			lock.unlock();
		}
	}
	/** @param pixbuf cast=(const GdkPixbuf *) */
	public static final native long _gdk_pixbuf_get_pixels(long pixbuf);
	public static final long gdk_pixbuf_get_pixels(long pixbuf) {
		lock.lock();
		try {
			return _gdk_pixbuf_get_pixels(pixbuf);
		} finally {
			lock.unlock();
		}
	}
	/** @param pixbuf cast=(const GdkPixbuf *) */
	public static final native int _gdk_pixbuf_get_rowstride(long pixbuf);
	public static final int gdk_pixbuf_get_rowstride(long pixbuf) {
		lock.lock();
		try {
			return _gdk_pixbuf_get_rowstride(pixbuf);
		} finally {
			lock.unlock();
		}
	}
	/** @param pixbuf cast=(const GdkPixbuf *) */
	public static final native int _gdk_pixbuf_get_width(long pixbuf);
	public static final int gdk_pixbuf_get_width(long pixbuf) {
		lock.lock();
		try {
			return _gdk_pixbuf_get_width(pixbuf);
		} finally {
			lock.unlock();
		}
	}
	public static final native long _gdk_pixbuf_loader_new();
	public static final long gdk_pixbuf_loader_new() {
		lock.lock();
		try {
			return _gdk_pixbuf_loader_new();
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param loader cast=(GdkPixbufLoader *)
	 * @param error cast=(GError **)
	 */
	public static final native boolean _gdk_pixbuf_loader_close(long loader, long [] error);
	public static final boolean gdk_pixbuf_loader_close(long loader, long [] error) {
		lock.lock();
		try {
			return _gdk_pixbuf_loader_close(loader, error);
		} finally {
			lock.unlock();
		}
	}
	/** @param loader cast=(GdkPixbufLoader *) */
	public static final native long _gdk_pixbuf_loader_get_pixbuf(long loader);
	public static final long gdk_pixbuf_loader_get_pixbuf(long loader) {
		lock.lock();
		try {
			return _gdk_pixbuf_loader_get_pixbuf(loader);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param loader cast=(GdkPixbufLoader *)
	 * @param buffer cast=(const guchar *)
	 * @param count cast=(gsize)
	 * @param error cast=(GError **)
	 */
	public static final native boolean _gdk_pixbuf_loader_write(long loader, long buffer, int count, long [] error);
	public static final boolean gdk_pixbuf_loader_write(long loader, long buffer, int count, long [] error) {
		lock.lock();
		try {
			return _gdk_pixbuf_loader_write(loader, buffer, count, error);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param colorspace cast=(GdkColorspace)
	 * @param has_alpha cast=(gboolean)
	 */
	public static final native long _gdk_pixbuf_new(int colorspace, boolean has_alpha, int bits_per_sample, int width, int height);
	public static final long gdk_pixbuf_new(int colorspace, boolean has_alpha, int bits_per_sample, int width, int height) {
		lock.lock();
		try {
			return _gdk_pixbuf_new(colorspace, has_alpha, bits_per_sample, width, height);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param filename cast=(const char *)
	 * @param error cast=(GError**)
	 */
	public static final native long _gdk_pixbuf_new_from_file(byte[] filename, long [] error);
	public static final long gdk_pixbuf_new_from_file(byte[] filename, long [] error) {
		lock.lock();
		try {
			return _gdk_pixbuf_new_from_file(filename, error);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param pixbuf cast=(GdkPixbuf *)
	 * @param buffer cast=(gchar **)
	 * @param buffer_size cast=(gsize *)
	 * @param type cast=(const char *)
	 * @param option_keys cast=(char **)
	 * @param option_values cast=(char **)
	 * @param error cast=(GError **)
	 */
	public static final native boolean _gdk_pixbuf_save_to_bufferv(long pixbuf, long [] buffer, long [] buffer_size, byte [] type, long [] option_keys, long [] option_values, long [] error);
	public static final boolean gdk_pixbuf_save_to_bufferv(long pixbuf, long [] buffer, long [] buffer_size, byte [] type, long [] option_keys, long [] option_values, long [] error) {
		lock.lock();
		try {
			return _gdk_pixbuf_save_to_bufferv(pixbuf, buffer, buffer_size, type, option_keys, option_values, error);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param src cast=(const GdkPixbuf *)
	 * @param interp_type cast=(GdkInterpType)
	 */
	public static final native long _gdk_pixbuf_scale_simple(long src, int dest_width, int dest_height, int interp_type);
	public static final long gdk_pixbuf_scale_simple(long src, int dest_width, int dest_height, int interp_type) {
		lock.lock();
		try {
			return _gdk_pixbuf_scale_simple(src, dest_width, dest_height, interp_type);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param device cast=(GdkDevice *)
	 * @param window cast=(GdkWindow *)
	 * @param owner_events cast=(gboolean)
	 * @param event_mask cast=(GdkEventMask)
	 * @param cursor cast=(GdkCursor *)
	 * @param time_ cast=(guint32)
	 */
	public static final native int _gdk_device_grab(long device, long window, int grab_ownership, boolean owner_events, int event_mask, long cursor, int time_);
	/**  [GTK3 only, if-def'd in os.h; 3.20 deprecated, replaced] */
	public static final int gdk_device_grab(long device, long window, int grab_ownership, boolean owner_events, int event_mask, long cursor, int time_) {
		lock.lock();
		try {
			return _gdk_device_grab(device, window, grab_ownership, owner_events, event_mask, cursor,time_);
		} finally {
			lock.unlock();
		}
	}
	/**
	 *  @method flags=dynamic
	 *  @param device cast=(GdkDevice *)
	 *  @param time_ cast=(guint32)
	 */
	public static final native void _gdk_device_ungrab(long device, int time_);
	/**  [GTK3; 3.20 deprecated] */
	public static final void  gdk_device_ungrab(long device, int time_) {
		lock.lock();
		try {
			_gdk_device_ungrab(device, time_);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param device cast=(GdkDevice *)
	 */
	public static final native long _gdk_device_get_associated_device(long device);
	public static final long  gdk_device_get_associated_device(long device) {
		lock.lock();
		try {
			return _gdk_device_get_associated_device(device);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param device cast=(GdkDevice *)
	 */
	public static final native long _gdk_device_get_seat(long device);
	public static final long  gdk_device_get_seat(long device) {
		lock.lock();
		try {
			return _gdk_device_get_seat(device);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GdkWindow *)
	 * @param property cast=(GdkAtom)
	 * @param type cast=(GdkAtom)
	 * @param actual_property_type cast=(GdkAtom *)
	 * @param actual_format cast=(gint *)
	 * @param actual_length cast=(gint *)
	 * @param data cast=(guchar **)
	 */
	public static final native boolean _gdk_property_get(long window, long property, long type, long offset, long length, int pdelete, long [] actual_property_type, int[] actual_format, int[] actual_length, long [] data);
	/** [GTK3 only, if-def'd in os.h] */
	public static final boolean gdk_property_get(long window, long property, long type, long offset, long length, int pdelete, long [] actual_property_type, int[] actual_format, int[] actual_length, long [] data) {
		lock.lock();
		try {
			return _gdk_property_get(window, property, type, offset, length, pdelete, actual_property_type, actual_format, actual_length, data);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param surface cast=(cairo_surface_t *)
	 */
	public static final native long _gdk_cairo_region_create_from_surface(long surface);
	public static final long gdk_cairo_region_create_from_surface(long surface) {
		lock.lock();
		try {
			return _gdk_cairo_region_create_from_surface(surface);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param rgba cast=(GdkRGBA *)
	 */
	public static final native long _gdk_rgba_to_string(GdkRGBA rgba);
	public static final long gdk_rgba_to_string(GdkRGBA rgba) {
		lock.lock();
		try {
			return _gdk_rgba_to_string(rgba);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param rgba cast=(GdkRGBA *)
	 */
	public static final native void _gdk_rgba_free(long rgba);
	public static final void gdk_rgba_free(long rgba) {
		lock.lock();
		try {
			_gdk_rgba_free(rgba);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param rgba cast=(GdkRGBA *)
	 */
	public static final native int _gdk_rgba_hash(GdkRGBA rgba);
	public static final int gdk_rgba_hash(GdkRGBA  rgba) {
		lock.lock();
		try {
			return _gdk_rgba_hash(rgba);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param rgba cast=(GdkRGBA *)
	 * @param property cast=(const gchar *)
	 */
	public static final native long _gdk_rgba_parse(GdkRGBA rgba, byte[] property);
	public static final long gdk_rgba_parse(GdkRGBA rgba, byte[] property) {
		lock.lock();
		try {
			return _gdk_rgba_parse(rgba, property);
		} finally {
			lock.unlock();
		}
	}
	public static final native long _gdk_screen_get_default();
	/** [GTK3 only, if-def'd in os.h] */
	public static final long gdk_screen_get_default() {
		lock.lock();
		try {
			return _gdk_screen_get_default();
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param screen cast=(GdkScreen *)
	 */
	public static final native double _gdk_screen_get_resolution(long screen);
	/** [GTK3 only, if-def'd in os.h] */
	public static final double gdk_screen_get_resolution(long screen) {
		lock.lock();
		try {
			return _gdk_screen_get_resolution(screen);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param clipboard cast=(GdkClipboard *)
	 * @param provider cast=(GdkContentProvider *)
	 */
	public static final native long _gdk_clipboard_set_content(long clipboard, long provider);
	/** [GTK4 only, if-def'd in os.h] */
	public static final long gdk_clipboard_set_content(long clipboard, long provider) {
		lock.lock();
		try {
			return _gdk_clipboard_set_content(clipboard, provider);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param screen cast=(GdkScreen *)
	 * @param monitor_num cast=(gint)
	 */
	public static final native int _gdk_screen_get_monitor_scale_factor(long screen, int monitor_num);
	/** [GTK3 only, if-def'd in os.h; 3.22 deprecated, replaced] */
	public static final int gdk_screen_get_monitor_scale_factor(long screen, int monitor_num) {
		lock.lock();
		try {
			return _gdk_screen_get_monitor_scale_factor(screen, monitor_num);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native int _gdk_monitor_get_scale_factor(long window);
	public static final int gdk_monitor_get_scale_factor(long window) {
		lock.lock();
		try {
			return _gdk_monitor_get_scale_factor(window);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param screen cast=(GdkScreen *)
	 * @param x cast=(gint)
	 * @param y cast=(gint)
	 */
	public static final native int _gdk_screen_get_monitor_at_point (long screen, int x, int y);
	/** [GTK3 only, if-def'd in os.h; 3.22 deprecated, replaced] */
	public static final int gdk_screen_get_monitor_at_point (long screen, int x, int y) {
		lock.lock();
		try {
			return _gdk_screen_get_monitor_at_point (screen, x, y);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param screen cast=(GdkScreen *)
	 * @param window cast=(GdkWindow *)
	 */
	public static final native int _gdk_screen_get_monitor_at_window(long screen, long window);
	/** [GTK3 only, if-def'd in os.h; 3.22 deprecated, replaced] */
	public static final int gdk_screen_get_monitor_at_window(long screen, long window) {
		lock.lock();
		try {
			return _gdk_screen_get_monitor_at_window(screen, window);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native long _gdk_display_get_monitor(long display, int monitor_num);
	public static final long gdk_display_get_monitor(long display, int monitor_num) {
		lock.lock();
		try {
			return _gdk_display_get_monitor(display, monitor_num);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native long _gdk_display_get_monitor_at_point(long display, int x, int y);
	public static final long gdk_display_get_monitor_at_point(long display, int x, int y) {
		lock.lock();
		try {
			return _gdk_display_get_monitor_at_point(display, x, y);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param display cast=(GdkDisplay *)
	 * @param window cast=(GdkWindow *)
	 */
	public static final native long _gdk_display_get_monitor_at_window(long display, long window);
	/** [GTK3 only, if-def'd in os.h] */
	public static final long gdk_display_get_monitor_at_window(long display, long window) {
		lock.lock();
		try {
			return _gdk_display_get_monitor_at_window(display, window);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param display cast=(GdkDisplay *)
	 * @param surface cast=(GdkSurface *)
	 */
	public static final native long _gdk_display_get_monitor_at_surface(long display, long surface);
	/** [GTK4 only, if-def'd in os.h] */
	public static final long gdk_display_get_monitor_at_surface(long display, long surface) {
		lock.lock();
		try {
			return _gdk_display_get_monitor_at_surface(display, surface);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native int _gdk_display_get_n_monitors(long display);
	public static final int gdk_display_get_n_monitors(long display) {
		lock.lock();
		try {
			return _gdk_display_get_n_monitors(display);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native int _gdk_monitor_get_width_mm(long monitor);
	public static final int gdk_monitor_get_width_mm(long monitor) {
		lock.lock();
		try {
			return _gdk_monitor_get_width_mm(monitor);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native long _gdk_display_get_primary_monitor(long display);
	public static final long gdk_display_get_primary_monitor(long display) {
		lock.lock();
		try {
			return _gdk_display_get_primary_monitor(display);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param display cast=(GdkDisplay *)
	 */
	public static final native boolean _gdk_display_is_composited(long display);
	/** [GTK4 only, if-def'd in os.h] */
	public static final boolean gdk_display_is_composited(long display) {
		lock.lock();
		try {
			return _gdk_display_is_composited(display);
		} finally {
			lock.unlock();
		}
	}
	/** @param display cast=(GdkDisplay *) */
	public static final native long _gdk_display_peek_event(long display);
	public static final long gdk_display_peek_event(long display) {
		lock.lock();
		try {
			return _gdk_display_peek_event(display);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param display cast=(GdkDisplay *)
	 * @param event cast=(const GdkEvent *)
	 */
	public static final native void _gdk_display_put_event(long display, long event);
	public static final void gdk_display_put_event(long display, long event) {
		lock.lock();
		try {
			_gdk_display_put_event(display, event);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param screen cast=(GdkScreen *)
	 * @param dest flags=no_in
	 */
	public static final native void _gdk_screen_get_monitor_geometry (long screen, int monitor_num, GdkRectangle dest);
	/** [GTK3 only, if-def'd in os.h; 3.22 deprecated, replaced] */
	public static final void gdk_screen_get_monitor_geometry (long screen, int monitor_num, GdkRectangle dest) {
		lock.lock();
		try {
			_gdk_screen_get_monitor_geometry(screen, monitor_num, dest);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param dest flags=no_in
	 */
	public static final native void _gdk_monitor_get_geometry (long monitor, GdkRectangle dest);
	public static final void gdk_monitor_get_geometry (long monitor, GdkRectangle dest) {
		lock.lock();
		try {
			_gdk_monitor_get_geometry(monitor, dest);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param dest flags=no_in
	 */
	public static final native void _gdk_monitor_get_workarea (long monitor, GdkRectangle dest);
	public static final void gdk_monitor_get_workarea (long monitor, GdkRectangle dest) {
		lock.lock();
		try {
			_gdk_monitor_get_workarea(monitor, dest);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param screen cast=(GdkScreen *)
	 * @param dest flags=no_in
	 */
	public static final native void _gdk_screen_get_monitor_workarea (long screen, int monitor_num, GdkRectangle dest);
	/** [GTK3 only, if-def'd in os.h; 3.22 deprecated, replaced] */
	public static final void gdk_screen_get_monitor_workarea (long screen, int monitor_num, GdkRectangle dest) {
		lock.lock();
		try {
			_gdk_screen_get_monitor_workarea(screen, monitor_num, dest);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param screen cast=(GdkScreen *)
	 */
	public static final native int _gdk_screen_get_n_monitors(long screen);
	/** [GTK3 only, if-def'd in os.h; 3.22 deprecated, replaced] */
	public static final int gdk_screen_get_n_monitors(long screen) {
		lock.lock();
		try {
			return _gdk_screen_get_n_monitors(screen);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param screen cast=(GdkScreen *)
	 */
	public static final native int _gdk_screen_get_primary_monitor(long screen);
	/** [GTK3 only, if-def'd in os.h; 3.22 deprecated, replaced] */
	public static final int gdk_screen_get_primary_monitor(long screen) {
		lock.lock();
		try {
			return _gdk_screen_get_primary_monitor(screen);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param screen cast=(GdkScreen *)
	 */
	public static final native long _gdk_screen_get_system_visual(long screen);
	/** [GTK3 only, if-def'd in os.h] */
	public static final long gdk_screen_get_system_visual(long screen) {
		lock.lock();
		try {
			return _gdk_screen_get_system_visual(screen);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native int _gdk_screen_height();
	public static final int gdk_screen_height() {
		lock.lock();
		try {
			return _gdk_screen_height();
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param screen cast=(GdkScreen *)
	 */
	public static final native boolean _gdk_screen_is_composited(long screen);
	/** [GTK3 only, if-def'd in os.h] */
	public static final boolean gdk_screen_is_composited(long screen) {
		lock.lock();
		try {
			return _gdk_screen_is_composited(screen);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native int _gdk_screen_width();
	public static final int gdk_screen_width() {
		lock.lock();
		try {
			return _gdk_screen_width();
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native int _gdk_seat_grab(long seat, long window, int capabilities, boolean owner_events, long cursor, long event, long func, long func_data);
	public static final int gdk_seat_grab(long seat, long window, int capabilities, boolean owner_events, long cursor, long event, long func, long func_data) {
		lock.lock();
		try {
			return _gdk_seat_grab(seat, window, capabilities, owner_events, cursor, event, func, func_data);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native void _gdk_seat_ungrab(long seat);
	public static final void gdk_seat_ungrab(long seat) {
		lock.lock();
		try {
			_gdk_seat_ungrab(seat);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native long _gdk_seat_get_pointer(long seat);
	public static final long gdk_seat_get_pointer(long seat) {
		lock.lock();
		try {
			return _gdk_seat_get_pointer(seat);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native long _gdk_seat_get_keyboard(long seat);
	public static final long gdk_seat_get_keyboard(long seat) {
		lock.lock();
		try {
			return _gdk_seat_get_keyboard(seat);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param program_class cast=(const char *)
	 */
	public static final native void _gdk_set_program_class(byte[] program_class);
	/** [GTK3 only] */
	public static final void gdk_set_program_class(byte[] program_class) {
		lock.lock();
		try {
			_gdk_set_program_class(program_class);
		} finally {
			lock.unlock();
		}
	}
	/** @param atom cast=(GdkAtom) */
	public static final native void _gdk_selection_owner_get(long atom);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gdk_selection_owner_get(long atom) {
		lock.lock();
		try {
			_gdk_selection_owner_get(atom);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param owner cast=(GdkWindow *)
	 * @param atom cast=(GdkAtom)
	 * @param time cast=(guint32)
	 * @param send_event cast=(gboolean)
	 */
	public static final native void _gdk_selection_owner_set(long owner, long atom, int time, boolean send_event);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gdk_selection_owner_set(long owner, long atom, int time, boolean send_event) {
		lock.lock();
		try {
			_gdk_selection_owner_set(owner, atom, time, send_event);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param display cast=(GdkDisplay *)
	 * @param str cast=(const gchar*)
	 * @param encoding cast=(GdkAtom *)
	 * @param format cast=(gint *)
	 * @param ctext cast=(guchar **)
	 * @param length cast=(gint *)
	 */
	public static final native boolean _gdk_x11_display_utf8_to_compound_text(long display, byte[] str, long [] encoding, int[] format, long [] ctext, int[] length);
	public static final boolean gdk_x11_display_utf8_to_compound_text(long display, byte[] str, long [] encoding, int[] format, long [] ctext, int[] length) {
		lock.lock();
		try {
			return _gdk_x11_display_utf8_to_compound_text(display, str, encoding, format, ctext, length);
		} finally {
			lock.unlock();
		}
	}
	/** @param str cast=(const gchar *) */
	public static final native long _gdk_utf8_to_string_target(byte[] str);
	public static final long gdk_utf8_to_string_target(byte[] str) {
		lock.lock();
		try {
			return _gdk_utf8_to_string_target(str);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param display cast=(GdkDisplay *)
	 * @param encoding cast=(GdkAtom)
	 * @param text cast=(guchar *)
	 * @param list cast=(gchar ***)
	 */
	public static final native int _gdk_text_property_to_utf8_list_for_display  (long display, long encoding, int format, long text, int length,  long [] list);
	public static final int gdk_text_property_to_utf8_list_for_display  (long display, long encoding, int format, long text, int length,  long [] list) {
		lock.lock();
		try {
			return _gdk_text_property_to_utf8_list_for_display(display, encoding, format, text, length, list);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param pixbuf cast=(GdkPixbuf *)
	 */
	public static final native long _gdk_texture_new_for_pixbuf (long pixbuf);
	/** [GTK4 only, if-def'd in os.h] */
	public static final long gdk_texture_new_for_pixbuf (long pixbuf) {
		lock.lock();
		try {
			return _gdk_texture_new_for_pixbuf(pixbuf);
		} finally {
			lock.unlock();
		}
	}
	public static final native void gdk_threads_leave ();
	public static final native  int _gdk_unicode_to_keyval(int wc);
	public static final  int gdk_unicode_to_keyval(int wc) {
		lock.lock();
		try {
			return _gdk_unicode_to_keyval(wc);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param visual cast=(GdkVisual *)
	 */
	public static final native int _gdk_visual_get_depth(long visual);
	/** [GTK3 only, if-def'd in os.h] */
	public static final int gdk_visual_get_depth(long visual) {
		lock.lock();
		try {
			return _gdk_visual_get_depth(visual);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GdkWindow *)
	 * @param content cast=(cairo_content_t)
	 */
	public static final native long _gdk_window_create_similar_surface(long window, int content, int width, int height);
	/** [GTK3 only, if-def'd in os.h] */
	public static final long gdk_window_create_similar_surface(long window, int content, int width, int height) {
		lock.lock();
		try {
			return _gdk_window_create_similar_surface(window, content, width, height);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GdkSurface *)
	 * @param content cast=(cairo_content_t)
	 */
	public static final native long _gdk_surface_create_similar_surface(long window, int content, int width, int height);
	/** [GTK4 only, if-def'd in os.h] */
	public static final long gdk_surface_create_similar_surface(long window, int content, int width, int height) {
		lock.lock();
		try {
			return _gdk_surface_create_similar_surface(window, content, width, height);
		} finally {
			lock.unlock();
		}
	}
	/** @param window cast=(GdkWindow *) */
	public static final native void _gdk_window_destroy(long window);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gdk_window_destroy(long window) {
		lock.lock();
		try {
			_gdk_window_destroy(window);
		} finally {
			lock.unlock();
		}
	}
	/** @param window cast=(GdkSurface *) */
	public static final native void _gdk_surface_destroy(long window);
	/** [GTK4 only, if-def'd in os.h] */
	public static final void gdk_surface_destroy(long window) {
		lock.lock();
		try {
			_gdk_surface_destroy(window);
		} finally {
			lock.unlock();
		}
	}
	/** @param window cast=(GdkWindow *) */
	public static final native long _gdk_window_get_children(long window);
	/** [GTK3 only, if-def'd in os.h] */
	public static final long gdk_window_get_children(long window) {
		lock.lock();
		try {
			return _gdk_window_get_children(window);
		} finally {
			lock.unlock();
		}
	}
	/** @param surface cast=(GdkSurface *) */
	public static final native long _gdk_surface_get_children(long surface);
	/** [GTK4 only, if-def'd in os.h] */
	public static final long gdk_surface_get_children(long surface) {
		lock.lock();
		try {
			return _gdk_surface_get_children(surface);
		} finally {
			lock.unlock();
		}
	}
	/** @param window cast=(GdkWindow *) */
	public static final native int _gdk_window_get_events(long window);
	/** [GTK3 only, if-def'd in os.h] */
	public static final int gdk_window_get_events(long window) {
		lock.lock();
		try {
			return _gdk_window_get_events(window);
		} finally {
			lock.unlock();
		}
	}
	/** @param window cast=(GdkWindow *) */
	public static final native void _gdk_window_focus(long window, int timestamp);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gdk_window_focus(long window, int timestamp) {
		lock.lock();
		try {
			_gdk_window_focus(window, timestamp);
		} finally {
			lock.unlock();
		}
	}
	/** @param surface cast=(GdkSurface *) */
	public static final native void _gdk_surface_focus(long surface, int timestamp);
	/** [GTK4 only, if-def'd in os.h] */
	public static final void gdk_surface_focus(long surface, int timestamp) {
		lock.lock();
		try {
			_gdk_surface_focus(surface, timestamp);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GdkWindow *)
	 * @param rect cast=(GdkRectangle *),flags=no_in
	 */
	public static final native void _gdk_window_get_frame_extents(long window, GdkRectangle rect);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gdk_window_get_frame_extents(long window, GdkRectangle rect) {
		lock.lock();
		try {
			_gdk_window_get_frame_extents(window, rect);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param surface cast=(GdkSurface *)
	 * @param rect cast=(GdkRectangle *),flags=no_in
	 */
	public static final native void _gdk_surface_get_frame_extents(long surface, GdkRectangle rect);
	/** [GTK4 only, if-def'd in os.h] */
	public static final void gdk_surface_get_frame_extents(long surface, GdkRectangle rect) {
		lock.lock();
		try {
			_gdk_surface_get_frame_extents(surface, rect);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GdkWindow *)
	 * @param x cast=(gint *)
	 * @param y cast=(gint *)
	 */
	public static final native int _gdk_window_get_origin(long window, int[] x, int[] y);
	/** [GTK3 only, if-def'd in os.h] */
	public static final int gdk_window_get_origin(long window, int[] x, int[] y) {
		lock.lock();
		try {
			return _gdk_window_get_origin(window, x, y);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param surface cast=(GdkSurface *)
	 * @param x cast=(gint *)
	 * @param y cast=(gint *)
	 */
	public static final native int _gdk_surface_get_origin(long surface, int[] x, int[] y);
	/** [GTK4 only, if-def'd in os.h] */
	public static final int gdk_surface_get_origin(long surface, int[] x, int[] y) {
		lock.lock();
		try {
			return _gdk_surface_get_origin(surface, x, y);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GdkWindow *)
	 * @param device cast=(GdkDevice *)
	 * @param x cast=(gint *)
	 * @param y cast=(gint *)
	 * @param mask cast=(GdkModifierType *)
	 */
	public static final native long _gdk_window_get_device_position(long window, long device, int[] x, int[] y, int[] mask);
	/** [GTK3 only, if-def'd in os.h] */
	public static final long gdk_window_get_device_position(long window, long device, int[] x, int[] y, int[] mask) {
		lock.lock();
		try {
			return _gdk_window_get_device_position(window, device, x, y, mask);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param surface cast=(GdkSurface *)
	 * @param device cast=(GdkDevice *)
	 * @param x cast=(gint *)
	 * @param y cast=(gint *)
	 * @param mask cast=(GdkModifierType *)
	 */
	public static final native long _gdk_surface_get_device_position(long surface, long device, int[] x, int[] y, int[] mask);
	/** [GTK4 only, if-def'd in os.h] */
	public static final long gdk_surface_get_device_position(long surface, long device, int[] x, int[] y, int[] mask) {
		lock.lock();
		try {
			return _gdk_surface_get_device_position(surface, device, x, y, mask);
		} finally {
			lock.unlock();
		}
	}
	/** @param window cast=(GdkWindow *) */
	public static final native long _gdk_window_get_parent(long window);
	/** [GTK3 only, if-def'd in os.h] */
	public static final long gdk_window_get_parent(long window) {
		lock.lock();
		try {
			return _gdk_window_get_parent(window);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GdkWindow *)
	 * @param x cast=(gint *)
	 * @param y cast=(gint *)
	 */
	public static final native void _gdk_window_get_root_origin(long window, int[] x, int[] y);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gdk_window_get_root_origin(long window, int[] x, int[] y) {
		lock.lock();
		try {
			_gdk_window_get_root_origin(window, x, y);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param surface cast=(GdkSurface *)
	 * @param x cast=(gint *)
	 * @param y cast=(gint *)
	 */
	public static final native void _gdk_surface_get_root_origin(long surface, int[] x, int[] y);
	/** [GTK4 only, if-def'd in os.h] */
	public static final void gdk_surface_get_root_origin(long surface, int[] x, int[] y) {
		lock.lock();
		try {
			_gdk_surface_get_root_origin(surface, x, y);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GdkWindow *)
	 * @param data cast=(gpointer *)
	 */
	public static final native void _gdk_window_get_user_data(long window, long [] data);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gdk_window_get_user_data(long window, long [] data) {
		lock.lock();
		try {
			_gdk_window_get_user_data(window, data);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param surface cast=(GdkSurface *)
	 * @param data cast=(gpointer *)
	 */
	public static final native void _gdk_surface_get_user_data(long surface, long [] data);
	/** [GTK4 only, if-def'd in os.h] */
	public static final void gdk_surface_get_user_data(long surface, long [] data) {
		lock.lock();
		try {
			_gdk_surface_get_user_data(surface, data);
		} finally {
			lock.unlock();
		}
	}
	/** @param window cast=(GdkWindow *) */
	public static final native void _gdk_window_hide(long window);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gdk_window_hide(long window) {
		lock.lock();
		try {
			_gdk_window_hide(window);
		} finally {
			lock.unlock();
		}
	}
	/** @param surface cast=(GdkSurface *) */
	public static final native void _gdk_surface_hide(long surface);
	/** [GTK4 only, if-def'd in os.h] */
	public static final void gdk_surface_hide(long surface) {
		lock.lock();
		try {
			_gdk_surface_hide(surface);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GdkWindow *)
	 * @param rectangle cast=(GdkRectangle *),flags=no_out
	 * @param invalidate_children cast=(gboolean)
	 */
	public static final native void _gdk_window_invalidate_rect(long window, GdkRectangle rectangle, boolean invalidate_children);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gdk_window_invalidate_rect(long window, GdkRectangle rectangle, boolean invalidate_children) {
		lock.lock();
		try {
			_gdk_window_invalidate_rect(window, rectangle, invalidate_children);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param surface cast=(GdkSurface *)
	 * @param rectangle cast=(GdkRectangle *),flags=no_out
	 */
	public static final native void _gdk_surface_invalidate_rect(long surface, GdkRectangle rectangle);
	/** [GTK4 only, if-def'd in os.h] */
	public static final void gdk_surface_invalidate_rect(long surface, GdkRectangle rectangle) {
		lock.lock();
		try {
			_gdk_surface_invalidate_rect(surface, rectangle);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GdkWindow *)
	 * @param region cast=(const cairo_region_t *)
	 * @param invalidate_children cast=(gboolean)
	 */
	public static final native void _gdk_window_invalidate_region(long window, long region, boolean invalidate_children);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gdk_window_invalidate_region(long window, long region, boolean invalidate_children) {
		lock.lock();
		try {
			_gdk_window_invalidate_region(window, region, invalidate_children);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param surface cast=(GdkSurface *)
	 * @param region cast=(const cairo_region_t *)
	 */
	public static final native void _gdk_surface_invalidate_region(long surface, long region);
	/** [GTK4 only, if-def'd in os.h] */
	public static final void gdk_surface_invalidate_region(long surface, long region) {
		lock.lock();
		try {
			_gdk_surface_invalidate_region(surface, region);
		} finally {
			lock.unlock();
		}
	}
	/** @param window cast=(GdkWindow *) */
	public static final native void _gdk_window_move(long window, int x, int y);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gdk_window_move(long window, int x, int y) {
		lock.lock();
		try {
			_gdk_window_move(window, x, y);
		} finally {
			lock.unlock();
		}
	}
	/** @param surface cast=(GdkSurface *) */
	public static final native void _gdk_surface_move(long surface, int x, int y);
	/** [GTK4 only, if-def'd in os.h] */
	public static final void gdk_surface_move(long surface, int x, int y) {
		lock.lock();
		try {
			_gdk_surface_move(surface, x, y);
		} finally {
			lock.unlock();
		}
	}
	/** @param window cast=(GdkWindow *) */
	public static final native void _gdk_window_move_resize(long window, int x, int y, int width, int height);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gdk_window_move_resize(long window, int x, int y, int width, int height) {
		lock.lock();
		try {
			_gdk_window_move_resize(window, x, y, width, height);
		} finally {
			lock.unlock();
		}
	}
	/** @param surface cast=(GdkSurface *) */
	public static final native void _gdk_surface_move_resize(long surface, int x, int y, int width, int height);
	/** [GTK4 only, if-def'd in os.h] */
	public static final void gdk_surface_move_resize(long surface, int x, int y, int width, int height) {
		lock.lock();
		try {
			_gdk_surface_move_resize(surface, x, y, width, height);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param parent cast=(GdkWindow *)
	 * @param attributes flags=no_out
	 */
	public static final native long _gdk_window_new(long parent, GdkWindowAttr attributes, int attributes_mask);
	/** [GTK3 only, if-def'd in os.h] */
	public static final long gdk_window_new(long parent, GdkWindowAttr attributes, int attributes_mask) {
		lock.lock();
		try {
			return _gdk_window_new(parent, attributes, attributes_mask);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param surface cast=(GdkSurface *)
	 * @param rect flags=no_out
	 */
	public static final native long _gdk_surface_new_child(long surface, GdkRectangle rect);
	/** [GTK4 only, if-def'd in os.h] */
	public static final long gdk_surface_new_child(long surface, GdkRectangle rect) {
		lock.lock();
		try {
			return _gdk_surface_new_child(surface, rect);
		} finally {
			lock.unlock();
		}
	}
	/** @param window cast=(GdkWindow *) */
	public static final native void _gdk_window_lower(long window);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gdk_window_lower(long window) {
		lock.lock();
		try {
			_gdk_window_lower(window);
		} finally {
			lock.unlock();
		}
	}
	/** @param surface cast=(GdkSurface *) */
	public static final native void _gdk_surface_lower(long surface);
	/** [GTK4 only, if-def'd in os.h] */
	public static final void gdk_surface_lower(long surface) {
		lock.lock();
		try {
			_gdk_surface_lower(surface);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native void _gdk_window_process_all_updates();
	/** [GTK3 only; 3.16 deprecated, replaced] */
	public static final void gdk_window_process_all_updates() {
		lock.lock();
		try {
			_gdk_window_process_all_updates();
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param window cast=(GdkWindow *)
	 * @param update_children cast=(gboolean)
	 */
	public static final native void _gdk_window_process_updates(long window, boolean update_children);
	/** [GTK3 only, if-def'd in os.h; 3.16 deprecated, replaced] */
	public static final void gdk_window_process_updates(long window, boolean update_children) {
		lock.lock();
		try {
			_gdk_window_process_updates(window, update_children);
		} finally {
			lock.unlock();
		}
	}
	/** @param window cast=(GdkWindow *) */
	public static final native void _gdk_window_raise(long window);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gdk_window_raise(long window) {
		lock.lock();
		try {
			_gdk_window_raise(window);
		} finally {
			lock.unlock();
		}
	}
	/** @param surface cast=(GdkSurface *) */
	public static final native void _gdk_surface_raise(long surface);
	/** [GTK4 only, if-def'd in os.h] */
	public static final void gdk_surface_raise(long surface) {
		lock.lock();
		try {
			_gdk_surface_raise(surface);
		} finally {
			lock.unlock();
		}
	}
	/** @param window cast=(GdkWindow *) */
	public static final native void _gdk_window_resize(long window, int width, int height);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gdk_window_resize(long window, int width, int height) {
		lock.lock();
		try {
			_gdk_window_resize(window, width, height);
		} finally {
			lock.unlock();
		}
	}
	/** @param surface cast=(GdkSurface *) */
	public static final native void _gdk_surface_resize(long surface, int width, int height);
	/** [GTK4 only, if-def'd in os.h] */
	public static final void gdk_surface_resize(long surface, int width, int height) {
		lock.lock();
		try {
			_gdk_surface_resize(surface, width, height);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GdkWindow *)
	 * @param sibling cast=(GdkWindow *)
	 * @param above cast=(gboolean)
	 */
	public static final native void _gdk_window_restack(long window, long sibling, boolean above);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gdk_window_restack(long window, long sibling, boolean above) {
		lock.lock();
		try {
			_gdk_window_restack(window, sibling, above);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param surface cast=(GdkSurface *)
	 * @param sibling cast=(GdkSurface *)
	 * @param above cast=(gboolean)
	 */
	public static final native void _gdk_surface_restack(long surface, long sibling, boolean above);
	/** [GTK4 only, if-def'd in os.h] */
	public static final void gdk_surface_restack(long surface, long sibling, boolean above) {
		lock.lock();
		try {
			_gdk_surface_restack(surface, sibling, above);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GdkWindow *)
	 * @param pattern cast=(cairo_pattern_t *)
	 */
	public static final native void _gdk_window_set_background_pattern(long window, long pattern);
	/**  [GTK3 only; 3.22 deprecated] */
	public static final void gdk_window_set_background_pattern(long window, long pattern) {
		lock.lock();
		try {
			_gdk_window_set_background_pattern(window, pattern);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GdkWindow *)
	 * @param cursor cast=(GdkCursor *)
	 */
	public static final native void _gdk_window_set_cursor(long window, long cursor);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gdk_window_set_cursor(long window, long cursor) {
		lock.lock();
		try {
			_gdk_window_set_cursor(window, cursor);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param surface cast=(GdkSurface *)
	 * @param cursor cast=(GdkCursor *)
	 */
	public static final native void _gdk_surface_set_cursor(long surface, long cursor);
	/** [GTK4 only, if-def'd in os.h] */
	public static final void gdk_surface_set_cursor(long surface, long cursor) {
		lock.lock();
		try {
			_gdk_surface_set_cursor(surface, cursor);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GdkWindow *)
	 * @param decorations cast=(GdkWMDecoration)
	 */
	public static final native void _gdk_window_set_decorations(long window, int decorations);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gdk_window_set_decorations(long window, int decorations) {
		lock.lock();
		try {
			_gdk_window_set_decorations(window, decorations);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param surface cast=(GdkSurface *)
	 * @param decorations cast=(GdkWMDecoration)
	 */
	public static final native void _gdk_surface_set_decorations(long surface, int decorations);
	/** [GTK4 only, if-def'd in os.h] */
	public static final void gdk_surface_set_decorations(long surface, int decorations) {
		lock.lock();
		try {
			_gdk_surface_set_decorations(surface, decorations);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GdkWindow *)
	 * @param functions cast=(GdkWMFunction)
	 */
	public static final native void _gdk_window_set_functions(long window, int functions);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gdk_window_set_functions(long window, int functions) {
		lock.lock();
		try {
			_gdk_window_set_functions(window, functions);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param surface cast=(GdkSurface *)
	 * @param functions cast=(GdkWMFunction)
	 */
	public static final native void _gdk_surface_set_functions(long surface, int functions);
	/** [GTK4 only, if-def'd in os.h] */
	public static final void gdk_surface_set_functions(long surface, int functions) {
		lock.lock();
		try {
			_gdk_surface_set_functions(surface, functions);
		} finally {
			lock.unlock();
		}
	}
	/** @param window cast=(GdkWindow *) */
	public static final native void _gdk_window_set_events(long window, int event_mask);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gdk_window_set_events(long window, int event_mask) {
		lock.lock();
		try {
			_gdk_window_set_events(window, event_mask);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GdkWindow *)
	 * @param override_redirect cast=(gboolean)
	 */
	public static final native void _gdk_window_set_override_redirect(long window, boolean override_redirect);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gdk_window_set_override_redirect(long window, boolean override_redirect) {
		lock.lock();
		try {
			_gdk_window_set_override_redirect(window, override_redirect);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GdkWindow *)
	 * @param user_data cast=(gpointer)
	 */
	public static final native void _gdk_window_set_user_data(long window, long user_data);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gdk_window_set_user_data(long window, long user_data) {
		lock.lock();
		try {
			_gdk_window_set_user_data(window, user_data);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param surface cast=(GdkSurface *)
	 * @param user_data cast=(gpointer)
	 */
	public static final native void _gdk_surface_set_user_data(long surface, long user_data);
	/** [GTK4 only, if-def'd in os.h] */
	public static final void gdk_surface_set_user_data(long surface, long user_data) {
		lock.lock();
		try {
			_gdk_surface_set_user_data(surface, user_data);
		} finally {
			lock.unlock();
		}
	}
	/** @param window cast=(GdkWindow *) */
	public static final native void _gdk_window_show(long window);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gdk_window_show(long window) {
		lock.lock();
		try {
			_gdk_window_show(window);
		} finally {
			lock.unlock();
		}
	}
	/** @param surface cast=(GdkSurface *) */
	public static final native void _gdk_surface_show(long surface);
	/** [GTK4 only, if-def'd in os.h] */
	public static final void gdk_surface_show(long surface) {
		lock.lock();
		try {
			_gdk_surface_show(surface);
		} finally {
			lock.unlock();
		}
	}
	/** @param window cast=(GdkWindow *) */
	public static final native void _gdk_window_show_unraised(long window);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gdk_window_show_unraised(long window) {
		lock.lock();
		try {
			_gdk_window_show_unraised(window);
		} finally {
			lock.unlock();
		}
	}

	/** @param surface cast=(GdkSurface *) */
	public static final native void _gdk_surface_show_unraised(long surface);
	/** [GTK4 only, if-def'd in os.h] */
	public static final void gdk_surface_show_unraised(long surface) {
		lock.lock();
		try {
			_gdk_surface_show_unraised(surface);
		} finally {
			lock.unlock();
		}
	}

	public static long gdk_get_pointer (long display) {
		if (GTK.GTK_VERSION >= OS.VERSION(3, 20, 0)) {
			long default_seat = GDK.gdk_display_get_default_seat(display);
			return GDK.gdk_seat_get_pointer(default_seat);
		} else {
			long device_manager = GDK.gdk_display_get_device_manager(display);
			return GDK.gdk_device_manager_get_client_pointer(device_manager);
		}
	}

}
