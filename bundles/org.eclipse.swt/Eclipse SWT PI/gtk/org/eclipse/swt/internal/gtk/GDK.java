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
	public static final int GDK_HAND2 = 0x3c;
	public static final int GDK_Help = 0xFF6A;
	public static final int GDK_HINT_MIN_SIZE = 1 << 1;
	public static final int GDK_Home = 0xff50;
	public static final int GDK_INCLUDE_INFERIORS = 0x1;
	public static final int GDK_INPUT_ONLY = 1;
	public static final int GDK_INTERP_BILINEAR = 0x2;
	public static final int GDK_Insert = 0xff63;
	public static final int GDK_ISO_Left_Tab = 0xfe20;
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
	public static final int GDK_MOD1_MASK = 0x8,
							GDK_SUPER_MASK = 0x4000000,
							GDK_HYPER_MASK = 0x8000000,
							GDK_META_MASK = 0x10000000;

	public static final int GDK_MOTION_NOTIFY = 0x3;
	public static final int GDK_NO_EXPOSE = 30;
	public static final int GDK_NONE = 0;
	public static final int GDK_NOTIFY_INFERIOR = 2;
	public static final int GDK_Num_Lock = 0xFF7F;
	public static final int GDK_OVERLAP_RECTANGLE_OUT = 0x1;
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
	public static final int GDK_UNMAP = 15;
	public static final int GDK_WA_X = 1 << 2;
	public static final int GDK_WA_Y = 1 << 3;
	public static final int GDK_WA_VISUAL = 1 << 6;
	public static final int GDK_WINDOW_TYPE_HINT_DIALOG = 1;
	public static final int GDK_WINDOW_TYPE_HINT_TOOLTIP = 10;

	/** sizeof(TYPE) for 32/64 bit support */
	public static final native int GdkColor_sizeof();
	public static final native int GdkKeymapKey_sizeof();
	public static final native int GdkRGBA_sizeof();
	public static final native int GdkDragContext_sizeof();
	public static final native int GdkEvent_sizeof();
	public static final native int GdkEventAny_sizeof();
	public static final native int GdkEventButton_sizeof();
	public static final native int GdkEventCrossing_sizeof();
	public static final native int GdkEventExpose_sizeof();
	public static final native int GdkEventFocus_sizeof();
	public static final native int GdkEventKey_sizeof();
	public static final native int GdkEventMotion_sizeof();
	public static final native int GdkEventScroll_sizeof();
	public static final native int GdkEventWindowState_sizeof();
	public static final native int GdkGeometry_sizeof();
	public static final native int GdkRectangle_sizeof();
	public static final native int GdkWindowAttr_sizeof();


	/** Macros */
	/** @param event cast=(GdkEvent *) */
	public static final native int GDK_EVENT_TYPE(long /*int*/ event);
	/** @param event cast=(GdkEventAny *) */
	public static final native long /*int*/ GDK_EVENT_WINDOW(long /*int*/ event);
	/** @param display cast=(GdkDisplay *) */
	public static final native boolean GDK_IS_X11_DISPLAY(long /*int*/ display);
	/** @param pixmap cast=(GdkPixmap *) */
	public static final native long /*int*/ _GDK_PIXMAP_XID(long /*int*/ pixmap);
	public static final long /*int*/ GDK_PIXMAP_XID(long /*int*/ pixmap) {
		lock.lock();
		try {
			return _GDK_PIXMAP_XID(pixmap);
		} finally {
			lock.unlock();
		}
	}

	/** @method flags=const */
	public static final native long /*int*/ _GDK_TYPE_COLOR();
	public static final long /*int*/ GDK_TYPE_COLOR() {
		lock.lock();
		try {
			return _GDK_TYPE_COLOR();
		} finally {
			lock.unlock();
		}
	}
	/** @method flags=const */
	public static final native long /*int*/ _GDK_TYPE_RGBA();
	public static final long /*int*/ GDK_TYPE_RGBA() {
		lock.lock();
		assert GTK.GTK3 : "GTK3 code was run by GTK2";
		try {
			return _GDK_TYPE_RGBA();
		} finally {
			lock.unlock();
		}
	}
	/** @method flags=const */
	public static final native long /*int*/ _GDK_TYPE_PIXBUF();
	public static final long /*int*/ GDK_TYPE_PIXBUF() {
		lock.lock();
		try {
			return _GDK_TYPE_PIXBUF();
		} finally {
			lock.unlock();
		}
	}

	/** @param gdkdisplay cast=(GdkDisplay *) */
	public static final native long /*int*/ _gdk_x11_display_get_xdisplay(long /*int*/ gdkdisplay);
	public static final long /*int*/ gdk_x11_display_get_xdisplay (long /*int*/ gdkdisplay) {
		lock.lock();
		try {
			return _gdk_x11_display_get_xdisplay(gdkdisplay);
		} finally {
			lock.unlock();
		}
	}
	/** @method flags=dynamic */
	public static final native long /*int*/ _gdk_x11_drawable_get_xid(long /*int*/ drawable);
	public static final long /*int*/ gdk_x11_drawable_get_xid(long /*int*/ drawable) {
		lock.lock();
		try {
			return _gdk_x11_drawable_get_xid(drawable);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gdk_x11_get_default_xdisplay();
	public static final long /*int*/ gdk_x11_get_default_xdisplay () {
		lock.lock();
		try {
			return _gdk_x11_get_default_xdisplay();
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param screen cast=(GdkScreen *)
	 * @param xvisualid cast=(VisualID)
	 */
	public static final native long /*int*/ _gdk_x11_screen_lookup_visual(long /*int*/ screen, int xvisualid);
	public static final long /*int*/ gdk_x11_screen_lookup_visual(long /*int*/ screen, int xvisualid) {
		lock.lock();
		try {
			return _gdk_x11_screen_lookup_visual(screen, xvisualid);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param screen cast=(GdkScreen *)
	 */
	public static final native long /*int*/ _gdk_x11_screen_get_window_manager_name(long /*int*/ screen);
	public static final long /*int*/ gdk_x11_screen_get_window_manager_name(long /*int*/ screen) {
		lock.lock();
		try {
			return _gdk_x11_screen_get_window_manager_name(screen);
		} finally {
			lock.unlock();
		}
	}
	/** @param visual cast=(GdkVisual *) */
	public static final native long /*int*/ _gdk_x11_visual_get_xvisual(long /*int*/ visual);
	public static final long /*int*/ gdk_x11_visual_get_xvisual(long /*int*/ visual) {
		lock.lock();
		try {
			return _gdk_x11_visual_get_xvisual(visual);
		} finally {
			lock.unlock();
		}
	}
	/**
	* @method flags=dynamic
	* @param gdkwindow cast=(GdkWindow *)
	*/
	public static final native long /*int*/ _gdk_x11_window_get_xid(long /*int*/ gdkwindow);
	public static final long /*int*/ gdk_x11_window_get_xid(long /*int*/ gdkwindow) {
		lock.lock();
		try {
			return _gdk_x11_window_get_xid(gdkwindow);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param gdkdisplay cast=(GdkDisplay *)
	 */
	public static final native long /*int*/ _gdk_x11_window_lookup_for_display(long /*int*/ gdkdisplay, long /*int*/ xid);
	public static final long /*int*/ gdk_x11_window_lookup_for_display(long /*int*/ gdkdisplay, long /*int*/ xid) {
		lock.lock();
		try {
			return _gdk_x11_window_lookup_for_display(gdkdisplay, xid);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GdkWindow *)
	 * @param function cast=(GdkFilterFunc)
	 * @param data cast=(gpointer)
	 */
	public static final native void _gdk_window_add_filter(long /*int*/ window, long /*int*/ function, long /*int*/ data);
	public static final void gdk_window_add_filter(long /*int*/ window, long /*int*/ function, long /*int*/ data) {
		lock.lock();
		try {
			_gdk_window_add_filter(window, function, data);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GdkWindow *)
	 * @param function cast=(GdkFilterFunc)
	 * @param data cast=(gpointer)
	 */
	public static final native void _gdk_window_remove_filter(long /*int*/ window, long /*int*/ function, long /*int*/ data);
	public static final void gdk_window_remove_filter(long /*int*/ window, long /*int*/ function, long /*int*/ data) {
		lock.lock();
		try {
			_gdk_window_remove_filter(window, function, data);
		} finally {
			lock.unlock();
		}
	}
	/** @param atom_name cast=(const gchar *),flags=no_out critical */
	public static final native long /*int*/ _gdk_atom_intern(byte[] atom_name, boolean only_if_exists);
	public static final long /*int*/ gdk_atom_intern(byte[] atom_name, boolean only_if_exists) {
		lock.lock();
		try {
			return _gdk_atom_intern(atom_name, only_if_exists);
		} finally {
			lock.unlock();
		}
	}
	/** @param atom cast=(GdkAtom) */
	public static final native long /*int*/ _gdk_atom_name(long /*int*/ atom);
	public static final long /*int*/ gdk_atom_name(long /*int*/ atom) {
		lock.lock();
		try {
			return _gdk_atom_name(atom);
		} finally {
			lock.unlock();
		}
	}
	public static final native void _gdk_beep();
	public static final void gdk_beep() {
		lock.lock();
		try {
			_gdk_beep();
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param window cast=(GdkWindow *)
	 * @param data cast=(const gchar *),flags=no_out critical
	 * @param width cast=(gint)
	 * @param height cast=(gint)
	 */
	public static final native long /*int*/ _gdk_bitmap_create_from_data(long /*int*/ window, byte[] data, int width, int height);
	public static final long /*int*/ gdk_bitmap_create_from_data(long /*int*/ window, byte[] data, int width, int height) {
		lock.lock();
		try {
			return _gdk_bitmap_create_from_data(window, data, width, height);
		} finally {
			lock.unlock();
		}
	}
	/** @method flags=dynamic */
	public static final native long /*int*/ _gdk_cairo_create(long /*int*/ drawable);
	public static final long /*int*/ gdk_cairo_create(long /*int*/ drawable) {
		lock.lock();
		try {
			return _gdk_cairo_create(drawable);
		} finally {
			lock.unlock();
		}
	}
	/** @method flags=dynamic */
	public static final native boolean _gdk_cairo_get_clip_rectangle(long /*int*/ cr, GdkRectangle rect);
	public static final boolean gdk_cairo_get_clip_rectangle(long /*int*/ cr, GdkRectangle rect) {
		lock.lock();
		try {
			return _gdk_cairo_get_clip_rectangle(cr, rect);
		} finally {
			lock.unlock();
		}
	}
	/** @method flags=dynamic */
	public static final native void _gdk_cairo_region(long /*int*/ cairo, long /*int*/ region);
	public static final void gdk_cairo_region(long /*int*/ cairo, long /*int*/ region) {
		lock.lock();
		try {
			_gdk_cairo_region(cairo, region);
		} finally {
			lock.unlock();
		}
	}
	/** @method flags=dynamic */
	public static final native void _gdk_cairo_reset_clip(long /*int*/ cairo, long /*int*/ drawable);
	public static final void gdk_cairo_reset_clip(long /*int*/ cairo, long /*int*/ drawable) {
		lock.lock();
		try {
			_gdk_cairo_reset_clip(cairo, drawable);
		} finally {
			lock.unlock();
		}
	}
	/** @method flags=dynamic */
	public static final native void _gdk_cairo_set_source_color(long /*int*/ cairo, GdkColor color);
	public static final void gdk_cairo_set_source_color(long /*int*/ cairo, GdkColor color) {
		lock.lock();
		try {
			_gdk_cairo_set_source_color(cairo, color);
		} finally {
			lock.unlock();
		}
	}
	/** @method flags=dynamic */
	public static final native void _gdk_cairo_set_source_rgba(long /*int*/ cairo, GdkRGBA rgba);
	public static final void gdk_cairo_set_source_rgba(long /*int*/ cairo, GdkRGBA rgba) {
		lock.lock();
		try {
			_gdk_cairo_set_source_rgba(cairo, rgba);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GdkWindow *)
	 * @method flags=dynamic
	 */
	public static final native int _gdk_window_get_width(long /*int*/ window);
	public static final int gdk_window_get_width(long /*int*/ window) {
		lock.lock();
		try {
			return _gdk_window_get_width(window);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GdkWindow *)
	 * @method flags=dynamic
	 */
	public static final native long /*int*/ _gdk_window_get_visible_region(long /*int*/ window);
	public static final long /*int*/ gdk_window_get_visible_region(long /*int*/ window) {
		lock.lock();
		try {
			return _gdk_window_get_visible_region(window);
		} finally {
			lock.unlock();
		}
	}
	/**
	 *  @param window cast=(GdkWindow *)
	 *  @method flags=dynamic
	 */
	public static final native int _gdk_window_get_height(long /*int*/ window);
	public static final int gdk_window_get_height(long /*int*/ window) {
		lock.lock();
		try {
			return _gdk_window_get_height(window);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param pixbuf cast=(const GdkPixbuf *)
	 */
	public static final native void _gdk_cairo_set_source_pixbuf(long /*int*/ cairo, long /*int*/ pixbuf, double pixbuf_x, double pixbuf_y);
	public static final void gdk_cairo_set_source_pixbuf(long /*int*/ cairo, long /*int*/ pixbuf, double pixbuf_x, double pixbuf_y) {
	        lock.lock();
	        try {
	                _gdk_cairo_set_source_pixbuf(cairo,pixbuf,pixbuf_x,pixbuf_y);
	        }
	        finally {
	                lock.unlock();
	        }
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native void _gdk_cairo_set_source_pixmap(long /*int*/ cairo, long /*int*/ pixmap, double pixbuf_x, double pixbuf_y);
	public static final void gdk_cairo_set_source_pixmap(long /*int*/ cairo, long /*int*/ pixmap, double pixbuf_x, double pixbuf_y) {
	        lock.lock();
	        try {
	                _gdk_cairo_set_source_pixmap(cairo,pixmap,pixbuf_x,pixbuf_y);
	        }
	        finally {
	                lock.unlock();
	        }
	}
	/**
	 * @method flags=dynamic
	 * @param window cast=(GdkWindow *)
	 */
	public static final native void _gdk_cairo_set_source_window(long /*int*/ cairo, long /*int*/ window, int x, int y);
	public static final void gdk_cairo_set_source_window(long /*int*/ cairo, long /*int*/ window, int x, int y) {
	        lock.lock();
	        try {
	                _gdk_cairo_set_source_window(cairo, window, x, y);
	        }
	        finally {
	                lock.unlock();
	        }
	}
	/**
	 * @method flags=dynamic
	 * @param color cast=(GdkColor *) */
	public static final native void _gdk_color_free(long /*int*/ color);
	public static final void gdk_color_free(long /*int*/ color) {
		lock.lock();
		assert !GTK.GTK3 : "GTK2 code was run by GTK3";
		try {
			_gdk_color_free(color);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param spec cast=(const gchar *)
	 * @param color cast=(GdkColor *),flags=no_in
	 */
	public static final native boolean _gdk_color_parse(byte[] spec, GdkColor color);
	public static final boolean gdk_color_parse(byte[] spec, GdkColor color) {
		lock.lock();
		assert !GTK.GTK3 : "GTK2 code was run by GTK3";
		try {
			return _gdk_color_parse(spec, color);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param color flags=no_in
	 */
	public static final native boolean _gdk_color_white(long /*int*/ colormap, GdkColor color);
	public static final boolean gdk_color_white(long /*int*/ colormap, GdkColor color) {
		lock.lock();
		try {
			return _gdk_color_white(colormap, color);
		} finally {
			lock.unlock();
		}
	}
	/**
	 *  @method flags=dynamic
	 * @param color cast=(GdkColor *)
	 * @param writeable cast=(gboolean)
	 * @param best_match cast=(gboolean)
	 */
	public static final native boolean _gdk_colormap_alloc_color(long /*int*/ colormap, GdkColor color, boolean writeable, boolean best_match);
	public static final boolean gdk_colormap_alloc_color(long /*int*/ colormap, GdkColor color, boolean writeable, boolean best_match) {
		lock.lock();
		try {
			return _gdk_colormap_alloc_color(colormap, color, writeable, best_match);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param colors cast=(GdkColor *),flags=no_out
	 * @param ncolors cast=(gint)
	 */
	public static final native void _gdk_colormap_free_colors(long /*int*/ colormap, GdkColor colors, int ncolors);
	public static final void gdk_colormap_free_colors(long /*int*/ colormap, GdkColor colors, int ncolors) {
		lock.lock();
		try {
			_gdk_colormap_free_colors(colormap, colors, ncolors);
		} finally {
			lock.unlock();
		}
	}
	/** @method flags=dynamic */
	public static final native long /*int*/ _gdk_colormap_get_system();
	public static final long /*int*/ gdk_colormap_get_system() {
		lock.lock();
		try {
			return _gdk_colormap_get_system();
		} finally {
			lock.unlock();
		}
	}
	/** @method flags=dynamic */
	public static final native void _gdk_cursor_unref(long /*int*/ cursor);
	public static final void gdk_cursor_unref(long /*int*/ cursor) {
		lock.lock();
		try {
			_gdk_cursor_unref(cursor);
		} finally {
			lock.unlock();
		}
	}
	/** @param display cast=(GdkDisplay *)
	 *  @param cursor_type cast=(GdkCursorType)
	 */
	public static final native long /*int*/ _gdk_cursor_new_for_display(long /*int*/ display, long /*int*/ cursor_type);
	public static final long /*int*/ gdk_cursor_new_for_display(long /*int*/ display, long /*int*/ cursor_type) {
		lock.lock();
		try {
			return _gdk_cursor_new_for_display(display, cursor_type);
		} finally {
			lock.unlock();
		}
	}
	/** @param display cast=(GdkDisplay *)
	 *  @param cursor_name cast=(const gchar *)
	 */
	public static final native long /*int*/ _gdk_cursor_new_from_name(long /*int*/ display, byte[] cursor_name);
	public static final long /*int*/ gdk_cursor_new_from_name(long /*int*/ display, byte[] cursor_name) {
		lock.lock();
		try {
			return _gdk_cursor_new_from_name(display, cursor_name);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param fg cast=(GdkColor *),flags=no_out
	 * @param bg cast=(GdkColor *),flags=no_out
	 * @param x cast=(gint)
	 * @param y cast=(gint)
	 */
	public static final native long /*int*/ _gdk_cursor_new_from_pixmap(long /*int*/ source, long /*int*/ mask, GdkColor fg, GdkColor bg, int x, int y);
	public static final long /*int*/ gdk_cursor_new_from_pixmap(long /*int*/ source, long /*int*/ mask, GdkColor fg, GdkColor bg, int x, int y) {
		lock.lock();
		try {
			return _gdk_cursor_new_from_pixmap(source, mask, fg, bg, x, y);
		} finally {
			lock.unlock();
		}
	}
	/** @method flags=dynamic */
	public static final native long /*int*/ _gdk_cursor_new_from_pixbuf(long /*int*/ display, long /*int*/ pixbuf, int x, int y);
	public static final long /*int*/ gdk_cursor_new_from_pixbuf(long /*int*/ display, long /*int*/ pixbuf, int x, int y) {
		lock.lock();
		try {
			return _gdk_cursor_new_from_pixbuf(display, pixbuf, x, y);
		} finally {
			lock.unlock();
		}
	}
	/** @method flags=dynamic */
	public static final native void _gdk_display_warp_pointer(long /*int*/ device, long /*int*/ screen, int x, int y);
	public static final void gdk_display_warp_pointer(long /*int*/ device, long /*int*/ screen, int x, int y) {
		lock.lock();
		try {
			_gdk_display_warp_pointer(device, screen, x, y);
		} finally {
			lock.unlock();
		}
	}
	/** @method flags=dynamic */
	public static final native void _gdk_device_warp(long /*int*/ device, long /*int*/ screen, int x, int y);
	public static final void gdk_device_warp(long /*int*/ device, long /*int*/ screen, int x, int y) {
		lock.lock();
		try {
			_gdk_device_warp(device, screen, x, y);
		} finally {
			lock.unlock();
		}
	}
	/** @method flags=dynamic */
	public static final native long /*int*/ _gdk_display_get_default();
	public static final long /*int*/ gdk_display_get_default() {
		lock.lock();
		try {
			return _gdk_display_get_default();
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native long /*int*/ _gdk_display_get_default_seat(long /*int*/ display);
	public static final long /*int*/ gdk_display_get_default_seat(long /*int*/ display) {
		lock.lock();
		try {
			return _gdk_display_get_default_seat(display);
		} finally {
			lock.unlock();
		}
	}
	/**
	 *  @method flags=dynamic
	 *  @param window cast=(GdkWindow *)
	 */
	public static final native long /*int*/ _gdk_window_get_display(long /*int*/ window);
	public static final long /*int*/ gdk_window_get_display(long /*int*/ window) {
		lock.lock();
		try {
			return _gdk_window_get_display(window);
		} finally {
			lock.unlock();
		}
	}
	/**
	 *  @method flags=dynamic
	 *  @param display cast=(GdkDisplay *)
	 */
	public static final native long /*int*/ _gdk_display_get_device_manager(long /*int*/ display);
	public static final long /*int*/ gdk_display_get_device_manager(long /*int*/ display) {
		lock.lock();
		try {
			return _gdk_display_get_device_manager(display);
		} finally {
			lock.unlock();
		}
	}
	/** @method flags=dynamic */
	public static final native long /*int*/ _gdk_device_manager_get_client_pointer(long /*int*/ device_manager);
	public static final long /*int*/ gdk_device_manager_get_client_pointer(long /*int*/ device_manager) {
		lock.lock();
		try {
			return _gdk_device_manager_get_client_pointer(device_manager);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param win_x cast=(gint *)
	 * @param win_y cast=(gint *)
	 */
	public static final native long /*int*/ _gdk_device_get_window_at_position(long /*int*/ device, int[] win_x, int[] win_y);
	public static final long /*int*/ gdk_device_get_window_at_position(long /*int*/ device, int[] win_x, int[] win_y) {
		lock.lock();
		try {
			return _gdk_device_get_window_at_position(device, win_x, win_y);
		} finally {
			lock.unlock();
		}
	}
	/** @method flags=dynamic */
	public static final native boolean _gdk_display_supports_cursor_color(long /*int*/ display);
	public static final boolean gdk_display_supports_cursor_color(long /*int*/ display) {
		lock.lock();
		try {
			return _gdk_display_supports_cursor_color(display);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param context cast=(GdkDragContext *)
	 */
	public static final native int _gdk_drag_context_get_actions(long /*int*/ context);
	public static final int gdk_drag_context_get_actions(long /*int*/ context) {
		lock.lock();
		try {
			return _gdk_drag_context_get_actions(context);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param context cast=(GdkDragContext *)
	 */
	public static final native long /*int*/ _gdk_drag_context_get_dest_window(long /*int*/ context);
	public static final long /*int*/ gdk_drag_context_get_dest_window(long /*int*/ context) {
		lock.lock();
		try {
			return _gdk_drag_context_get_dest_window(context);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param context cast=(GdkDragContext *)
	 */
	public static final native int _gdk_drag_context_get_selected_action(long /*int*/ context);
	public static final int gdk_drag_context_get_selected_action(long /*int*/ context) {
		lock.lock();
		try {
			return _gdk_drag_context_get_selected_action(context);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param context cast=(GdkDragContext *)
	 */
	public static final native long /*int*/ _gdk_drag_context_list_targets(long /*int*/ context);
	public static final long /*int*/ gdk_drag_context_list_targets(long /*int*/ context) {
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
	public static final native void _gdk_drag_status(long /*int*/ context, int action, int time);
	public static final void gdk_drag_status(long /*int*/ context, int action, int time) {
		lock.lock();
		try {
			_gdk_drag_status(context, action, time);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param filled cast=(gint)
	 * @param x cast=(gint)
	 * @param y cast=(gint)
	 * @param width cast=(gint)
	 * @param height cast=(gint)
	 * @param angle1 cast=(gint)
	 * @param angle2 cast=(gint)
	 */
	public static final native void _gdk_draw_arc(long /*int*/ drawable, long /*int*/ gc, int filled, int x, int y, int width, int height, int angle1, int angle2);
	public static final void gdk_draw_arc(long /*int*/ drawable, long /*int*/ gc, int filled, int x, int y, int width, int height, int angle1, int angle2) {
		lock.lock();
		try {
			_gdk_draw_arc(drawable, gc, filled, x, y, width, height, angle1, angle2);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native void _gdk_draw_image(long /*int*/ drawable, long /*int*/ gc, long /*int*/ image, int xsrc, int ysrc, int xdest, int ydest, int width, int height);
	public static final void gdk_draw_image(long /*int*/ drawable, long /*int*/ gc, long /*int*/ image, int xsrc, int ysrc, int xdest, int ydest, int width, int height) {
		lock.lock();
		try {
			_gdk_draw_image(drawable, gc, image, xsrc, ysrc, xdest, ydest, width, height);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param xsrc cast=(gint)
	 * @param ysrc cast=(gint)
	 * @param xdest cast=(gint)
	 * @param ydest cast=(gint)
	 * @param width cast=(gint)
	 * @param height cast=(gint)
	 * @param x_dither cast=(gint)
	 * @param y_dither cast=(gint)
	 */
	public static final native void _gdk_draw_pixbuf(long /*int*/ drawable, long /*int*/ gc, long /*int*/ pixbuf, int xsrc, int ysrc, int xdest, int ydest, int width, int height, int dither, int x_dither, int y_dither);
	public static final void gdk_draw_pixbuf(long /*int*/ drawable, long /*int*/ gc, long /*int*/ pixbuf, int xsrc, int ysrc, int xdest, int ydest, int width, int height, int dither, int x_dither, int y_dither) {
		lock.lock();
		try {
			_gdk_draw_pixbuf(drawable, gc, pixbuf, xsrc, ysrc, xdest, ydest, width, height, dither, x_dither, y_dither);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param filled cast=(gint)
	 * @param x cast=(gint)
	 * @param y cast=(gint)
	 * @param width cast=(gint)
	 * @param height cast=(gint)
	 */
	public static final native void _gdk_draw_rectangle(long /*int*/ drawable, long /*int*/ gc, int filled, int x, int y, int width, int height);
	public static final void gdk_draw_rectangle(long /*int*/ drawable, long /*int*/ gc, int filled, int x, int y, int width, int height) {
		lock.lock();
		try {
			_gdk_draw_rectangle(drawable, gc, filled, x, y, width, height);
		} finally {
			lock.unlock();
		}
	}
	/** @method flags=dynamic */
	public static final native int _gdk_drawable_get_depth(long /*int*/ drawable);
	public static final int gdk_drawable_get_depth(long /*int*/ drawable) {
		lock.lock();
		try {
			return _gdk_drawable_get_depth(drawable);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param width cast=(gint *),flags=no_in critical
	 * @param height cast=(gint *),flags=no_in critical
	 * @method flags=dynamic
	 */
	public static final native void _gdk_pixmap_get_size(long /*int*/ pixmap, int[] width, int[] height);
	public static final void gdk_pixmap_get_size(long /*int*/ pixmap, int[] width, int[] height) {
		lock.lock();
		try {
			_gdk_pixmap_get_size (pixmap,width,height);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param x cast=(gint)
	 * @param y cast=(gint)
	 * @param width cast=(gint)
	 * @param height cast=(gint)
	 */
	public static final native long /*int*/ _gdk_drawable_get_image(long /*int*/ drawable, int x, int y, int width, int height);
	public static final long /*int*/ gdk_drawable_get_image(long /*int*/ drawable, int x, int y, int width, int height) {
		lock.lock();
		try {
			return _gdk_drawable_get_image(drawable, x, y, width, height);
		} finally {
			lock.unlock();
		}
	}
	/** @method flags=dynamic */
	public static final native long /*int*/ _gdk_drawable_get_visible_region(long /*int*/ drawable);
	public static final long /*int*/ gdk_drawable_get_visible_region(long /*int*/ drawable) {
		lock.lock();
		try {
			return _gdk_drawable_get_visible_region(drawable);
		} finally {
			lock.unlock();
		}
	}
	/** @param event cast=(GdkEvent *) */
	public static final native long /*int*/ _gdk_event_copy(long /*int*/ event);
	public static final long /*int*/ gdk_event_copy(long /*int*/ event) {
		lock.lock();
		try {
			return _gdk_event_copy(event);
		} finally {
			lock.unlock();
		}
	}
	/** @param event cast=(GdkEvent *) */
	public static final native void _gdk_event_free(long /*int*/ event);
	public static final void gdk_event_free(long /*int*/ event) {
		lock.lock();
		try {
			_gdk_event_free(event);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gdk_event_get();
	public static final long /*int*/ gdk_event_get() {
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
	public static final native boolean _gdk_event_get_coords(long /*int*/ event, double[] px, double[] py);
	public static final boolean gdk_event_get_coords(long /*int*/ event, double[] px, double[] py) {
		lock.lock();
		try {
			return _gdk_event_get_coords(event, px, py);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param event cast=(GdkEvent *)
	 * @param pmod cast=(GdkModifierType *)
	 */
	public static final native boolean _gdk_event_get_state(long /*int*/ event, int[] pmod);
	public static final boolean gdk_event_get_state(long /*int*/ event, int[] pmod) {
		lock.lock();
		try {
			return _gdk_event_get_state(event, pmod);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param event cast=(GdkEvent *)
	 */
	public static final native boolean _gdk_event_get_scroll_deltas(long /*int*/ event, double[] delta_x, double[] delta_y);
	public static final boolean gdk_event_get_scroll_deltas(long /*int*/ event, double[] delta_x, double[] delta_y) {
		lock.lock();
		try {
			return _gdk_event_get_scroll_deltas(event, delta_x, delta_y);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native long /*int*/ _gdk_event_get_seat(long /*int*/ event);
	public static final long /*int*/ gdk_event_get_seat(long /*int*/ event) {
		lock.lock();
		try {
			return _gdk_event_get_seat(event);
		} finally {
			lock.unlock();
		}
	}
	/** @param event cast=(GdkEvent *) */
	public static final native int _gdk_event_get_time(long /*int*/ event);
	public static final int gdk_event_get_time(long /*int*/ event) {
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
	public static final native int _gdk_event_get_event_type(long /*int*/ event);
	/** [GTK3.10+] */
	public static final int gdk_event_get_event_type(long /*int*/ event) {
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
	public static final native void _gdk_event_handler_set(long /*int*/ func, long /*int*/ data, long /*int*/ notify);
	public static final void gdk_event_handler_set(long /*int*/ func, long /*int*/ data, long /*int*/ notify) {
		lock.lock();
		try {
			_gdk_event_handler_set(func, data, notify);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gdk_event_new(int type);
	public static final long /*int*/ gdk_event_new(int type) {
		lock.lock();
		try {
			return _gdk_event_new(type);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gdk_event_peek();
	public static final long /*int*/ gdk_event_peek() {
		lock.lock();
		try {
			return _gdk_event_peek();
		} finally {
			lock.unlock();
		}
	}
	/** @param event cast=(GdkEvent *) */
	public static final native void _gdk_event_put(long /*int*/ event);
	public static final void gdk_event_put(long /*int*/ event) {
		lock.lock();
		try {
			_gdk_event_put(event);
		} finally {
			lock.unlock();
		}
	}
	public static final native void _gdk_error_trap_push();
	public static final void gdk_error_trap_push() {
		lock.lock();
		try {
			_gdk_error_trap_push();
		} finally {
			lock.unlock();
		}
	}
	public static final native int _gdk_error_trap_pop();
	public static final int gdk_error_trap_pop() {
		lock.lock();
		try {
			return _gdk_error_trap_pop();
		} finally {
			lock.unlock();
		}
	}
	public static final native void _gdk_flush();
	public static final void gdk_flush() {
		lock.lock();
		try {
			_gdk_flush();
		} finally {
			lock.unlock();
		}
	}
	/** @method flags=dynamic */
	public static final native long /*int*/ _gdk_gc_new(long /*int*/ window);
	public static final long /*int*/ gdk_gc_new(long /*int*/ window) {
		lock.lock();
		try {
			return _gdk_gc_new(window);
		} finally {
			lock.unlock();
		}
	}
	/** @method flags=dynamic */
	public static final native void _gdk_gc_set_fill(long /*int*/ gc, int fill);
	public static final void gdk_gc_set_fill(long /*int*/ gc, int fill) {
		lock.lock();
		try {
			_gdk_gc_set_fill(gc, fill);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param color flags=no_out
	 */
	public static final native void _gdk_gc_set_foreground(long /*int*/ gc, GdkColor color);
	public static final void gdk_gc_set_foreground(long /*int*/ gc, GdkColor color) {
		lock.lock();
		try {
			_gdk_gc_set_foreground(gc, color);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native void _gdk_gc_set_function(long /*int*/ gc, long /*int*/ function);
	public static final void gdk_gc_set_function(long /*int*/ gc, long /*int*/ function) {
		lock.lock();
		try {
			_gdk_gc_set_function(gc, function);
		} finally {
			lock.unlock();
		}
	}
	/** @method flags=dynamic */
	public static final native void _gdk_gc_set_stipple(long /*int*/ gc, long /*int*/ stipple);
	public static final void gdk_gc_set_stipple(long /*int*/ gc, long /*int*/ stipple) {
		lock.lock();
		try {
			_gdk_gc_set_stipple(gc, stipple);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native void _gdk_gc_set_subwindow(long /*int*/ gc, long /*int*/ mode);
	public static final void gdk_gc_set_subwindow(long /*int*/ gc, long /*int*/ mode) {
		lock.lock();
		try {
			_gdk_gc_set_subwindow(gc, mode);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gdk_get_default_root_window();
	public static final long /*int*/ gdk_get_default_root_window() {
		lock.lock();
		try {
			return _gdk_get_default_root_window();
		} finally {
			lock.unlock();
		}
	}
	/** @method flags=dynamic */
	public static final native void _gdk_keyboard_ungrab(int time);
	public static final void gdk_keyboard_ungrab(int time) {
		lock.lock();
		try {
			_gdk_keyboard_ungrab(time);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gdk_keymap_get_default();
	public static final long /*int*/ gdk_keymap_get_default() {
		lock.lock();
		try {
			return _gdk_keymap_get_default();
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
	public static final native boolean _gdk_keymap_get_entries_for_keyval (long /*int*/ keymap, long keyval, long /*int*/[] keys, int[] n_keys);
	public static final boolean gdk_keymap_get_entries_for_keyval (long /*int*/ keymap, long keyval, long /*int*/[] keys, int[] n_keys) {
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
	public static final native long /*int*/ _gdk_pango_context_get();
	public static final long /*int*/ gdk_pango_context_get() {
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
	public static final native long /*int*/ _gdk_pango_layout_get_clip_region(long /*int*/ layout, int x_origin, int y_origin, int[] index_ranges, int n_ranges);
	public static final long /*int*/ gdk_pango_layout_get_clip_region(long /*int*/ layout, int x_origin, int y_origin, int[] index_ranges, int n_ranges) {
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
	public static final native void _gdk_pixbuf_copy_area(long /*int*/ src_pixbuf, int src_x, int src_y, int width, int height, long /*int*/ dest_pixbuf, int dest_x, int dest_y);
	public static final void gdk_pixbuf_copy_area(long /*int*/ src_pixbuf, int src_x, int src_y, int width, int height, long /*int*/ dest_pixbuf, int dest_x, int dest_y) {
		lock.lock();
		try {
			_gdk_pixbuf_copy_area(src_pixbuf, src_x, src_y, width, height, dest_pixbuf, dest_x, dest_y);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param dest cast=(GdkPixbuf *)
	 */
	public static final native long /*int*/ _gdk_pixbuf_get_from_drawable(long /*int*/ dest, long /*int*/ src, long /*int*/ cmap, int src_x, int src_y, int dest_x, int dest_y, int width, int height);
	public static final long /*int*/ gdk_pixbuf_get_from_drawable(long /*int*/ dest, long /*int*/ src, long /*int*/ cmap, int src_x, int src_y, int dest_x, int dest_y, int width, int height) {
		lock.lock();
		try {
			return _gdk_pixbuf_get_from_drawable(dest, src, cmap, src_x, src_y, dest_x, dest_y, width, height);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param window cast=(GdkWindow *)
	 * @param src_x cast=(gint)
	 * @param src_y cast=(gint)
	 * @param width cast=(gint)
	 * @param height cast=(gint)
	 */
	public static final native long /*int*/ _gdk_pixbuf_get_from_window(long /*int*/ window, int src_x, int src_y, int width, int height);
	public static final long /*int*/ gdk_pixbuf_get_from_window(long /*int*/ window, int src_x, int src_y, int width, int height) {
		lock.lock();
		try {
			return _gdk_pixbuf_get_from_window(window, src_x, src_y, width, height);
		} finally {
			lock.unlock();
		}
	}
	/** @param pixbuf cast=(const GdkPixbuf *) */
	public static final native boolean _gdk_pixbuf_get_has_alpha(long /*int*/ pixbuf);
	public static final boolean gdk_pixbuf_get_has_alpha(long /*int*/ pixbuf) {
		lock.lock();
		try {
			return _gdk_pixbuf_get_has_alpha(pixbuf);
		} finally {
			lock.unlock();
		}
	}
	/** @param pixbuf cast=(const GdkPixbuf *) */
	public static final native int _gdk_pixbuf_get_height(long /*int*/ pixbuf);
	public static final int gdk_pixbuf_get_height(long /*int*/ pixbuf) {
		lock.lock();
		try {
			return _gdk_pixbuf_get_height(pixbuf);
		} finally {
			lock.unlock();
		}
	}
	/** @param pixbuf cast=(const GdkPixbuf *) */
	public static final native long /*int*/ _gdk_pixbuf_get_pixels(long /*int*/ pixbuf);
	public static final long /*int*/ gdk_pixbuf_get_pixels(long /*int*/ pixbuf) {
		lock.lock();
		try {
			return _gdk_pixbuf_get_pixels(pixbuf);
		} finally {
			lock.unlock();
		}
	}
	/** @param pixbuf cast=(const GdkPixbuf *) */
	public static final native int _gdk_pixbuf_get_rowstride(long /*int*/ pixbuf);
	public static final int gdk_pixbuf_get_rowstride(long /*int*/ pixbuf) {
		lock.lock();
		try {
			return _gdk_pixbuf_get_rowstride(pixbuf);
		} finally {
			lock.unlock();
		}
	}
	/** @param pixbuf cast=(const GdkPixbuf *) */
	public static final native int _gdk_pixbuf_get_width(long /*int*/ pixbuf);
	public static final int gdk_pixbuf_get_width(long /*int*/ pixbuf) {
		lock.lock();
		try {
			return _gdk_pixbuf_get_width(pixbuf);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gdk_pixbuf_loader_new();
	public static final long /*int*/ gdk_pixbuf_loader_new() {
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
	public static final native boolean _gdk_pixbuf_loader_close(long /*int*/ loader, long /*int*/ [] error);
	public static final boolean gdk_pixbuf_loader_close(long /*int*/ loader, long /*int*/ [] error) {
		lock.lock();
		try {
			return _gdk_pixbuf_loader_close(loader, error);
		} finally {
			lock.unlock();
		}
	}
	/** @param loader cast=(GdkPixbufLoader *) */
	public static final native long /*int*/ _gdk_pixbuf_loader_get_pixbuf(long /*int*/ loader);
	public static final long /*int*/ gdk_pixbuf_loader_get_pixbuf(long /*int*/ loader) {
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
	public static final native boolean _gdk_pixbuf_loader_write(long /*int*/ loader, long /*int*/ buffer, int count, long /*int*/ [] error);
	public static final boolean gdk_pixbuf_loader_write(long /*int*/ loader, long /*int*/ buffer, int count, long /*int*/ [] error) {
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
	public static final native long /*int*/ _gdk_pixbuf_new(int colorspace, boolean has_alpha, int bits_per_sample, int width, int height);
	public static final long /*int*/ gdk_pixbuf_new(int colorspace, boolean has_alpha, int bits_per_sample, int width, int height) {
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
	public static final native long /*int*/ _gdk_pixbuf_new_from_file(byte[] filename, long /*int*/ [] error);
	public static final long /*int*/ gdk_pixbuf_new_from_file(byte[] filename, long /*int*/ [] error) {
		lock.lock();
		try {
			return _gdk_pixbuf_new_from_file(filename, error);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param pixbuf cast=(GdkPixbuf *)
	 * @param buffer cast=(gchar **)
	 * @param buffer_size cast=(gsize *)
	 * @param type cast=(const char *)
	 * @param option_keys cast=(char **)
	 * @param option_values cast=(char **)
	 * @param error cast=(GError **)
	 */
	public static final native boolean _gdk_pixbuf_save_to_bufferv(long /*int*/ pixbuf, long /*int*/ [] buffer, long /*int*/ [] buffer_size, byte [] type, long /*int*/ [] option_keys, long /*int*/ [] option_values, long /*int*/ [] error);
	public static final boolean gdk_pixbuf_save_to_bufferv(long /*int*/ pixbuf, long /*int*/ [] buffer, long /*int*/ [] buffer_size, byte [] type, long /*int*/ [] option_keys, long /*int*/ [] option_values, long /*int*/ [] error) {
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
	public static final native long /*int*/ _gdk_pixbuf_scale_simple(long /*int*/ src, int dest_width, int dest_height, int interp_type);
	public static final long /*int*/ gdk_pixbuf_scale_simple(long /*int*/ src, int dest_width, int dest_height, int interp_type) {
		lock.lock();
		try {
			return _gdk_pixbuf_scale_simple(src, dest_width, dest_height, interp_type);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param window cast=(GdkWindow *)
	 * @param width cast=(gint)
	 * @param height cast=(gint)
	 * @param depth cast=(gint)
	 */
	public static final native long /*int*/ _gdk_pixmap_new(long /*int*/ window, int width, int height, int depth);
	public static final long /*int*/ gdk_pixmap_new(long /*int*/ window, int width, int height, int depth) {
		lock.lock();
		try {
			return _gdk_pixmap_new(window, width, height, depth);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param window cast=(GdkWindow *)
	 * @param owner_events cast=(gboolean)
	 * @param event_mask cast=(GdkEventMask)
	 * @param confine_to cast=(GdkWindow *)
	 * @param cursor cast=(GdkCursor *)
	 * @param time cast=(guint32)
	 */
	public static final native int _gdk_pointer_grab(long /*int*/ window, boolean owner_events, int event_mask, long /*int*/ confine_to, long /*int*/ cursor, int time);
	public static final int gdk_pointer_grab(long /*int*/ window, boolean owner_events, int event_mask, long /*int*/ confine_to, long /*int*/ cursor, int time) {
		lock.lock();
		try {
			return _gdk_pointer_grab(window, owner_events, event_mask, confine_to, cursor, time);
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
	public static final native int _gdk_device_grab(long /*int*/ device, long /*int*/ window, int grab_ownership, boolean owner_events, int event_mask, long /*int*/ cursor, int time_);
	public static final int gdk_device_grab(long /*int*/ device, long /*int*/ window, int grab_ownership, boolean owner_events, int event_mask, long /*int*/ cursor, int time_) {
		lock.lock();
		try {
			return _gdk_device_grab(device, window, grab_ownership, owner_events, event_mask, cursor,time_);
		} finally {
			lock.unlock();
		}
	}
	/**
	 *  @method flags=dynamic
	 *  @param time cast=(guint32)
	 */
	public static final native void _gdk_pointer_ungrab(int time);
	public static final void gdk_pointer_ungrab(int time) {
		lock.lock();
		try {
			_gdk_pointer_ungrab(time);
		} finally {
			lock.unlock();
		}
	}
	/**
	 *  @method flags=dynamic
	 *  @param device cast=(GdkDevice *)
	 *  @param time_ cast=(guint32)
	 */
	public static final native void _gdk_device_ungrab(long /*int*/ device, int time_);
	public static final void  gdk_device_ungrab(long /*int*/ device, int time_) {
		lock.lock();
		try {
			_gdk_device_ungrab(device, time_);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param device cast=(GdkDevice *)
	 */
	public static final native long /*int*/ _gdk_device_get_associated_device(long /*int*/ device);
	public static final long /*int*/  gdk_device_get_associated_device(long /*int*/ device) {
		lock.lock();
		try {
			return _gdk_device_get_associated_device(device);
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
	public static final native boolean _gdk_property_get(long /*int*/ window, long /*int*/ property, long /*int*/ type, long /*int*/ offset, long /*int*/ length, int pdelete, long /*int*/[] actual_property_type, int[] actual_format, int[] actual_length, long /*int*/[] data);
	public static final boolean gdk_property_get(long /*int*/ window, long /*int*/ property, long /*int*/ type, long /*int*/ offset, long /*int*/ length, int pdelete, long /*int*/[] actual_property_type, int[] actual_format, int[] actual_length, long /*int*/[] data) {
		lock.lock();
		try {
			return _gdk_property_get(window, property, type, offset, length, pdelete, actual_property_type, actual_format, actual_length, data);
		} finally {
			lock.unlock();
		}
	}
	/** @param region cast=(GdkRegion *) */
	public static final native void _gdk_region_destroy(long /*int*/ region);
	public static final void gdk_region_destroy(long /*int*/ region) {
		lock.lock();
		try {
			_gdk_region_destroy(region);
		} finally {
			lock.unlock();
		}
	}
	/** @param region cast=(GdkRegion *) */
	public static final native boolean _gdk_region_empty(long /*int*/ region);
	public static final boolean gdk_region_empty(long /*int*/ region) {
		lock.lock();
		try {
			return _gdk_region_empty(region);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param region cast=(GdkRegion *)
	 * @param rectangle cast=(GdkRectangle *),flags=no_in
	 */
	public static final native void _gdk_region_get_clipbox(long /*int*/ region, GdkRectangle rectangle);
	public static final void gdk_region_get_clipbox(long /*int*/ region, GdkRectangle rectangle) {
		lock.lock();
		try {
			_gdk_region_get_clipbox(region, rectangle);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param n_rectangles cast=(gint *)
	 */
	public static final native void _gdk_region_get_rectangles(long /*int*/ region, long /*int*/[] rectangles, int[] n_rectangles);
	public static final void gdk_region_get_rectangles(long /*int*/ region, long /*int*/[] rectangles, int[] n_rectangles) {
		lock.lock();
		try {
			_gdk_region_get_rectangles(region, rectangles, n_rectangles);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param source1 cast=(GdkRegion *)
	 * @param source2 cast=(GdkRegion *)
	 */
	public static final native void _gdk_region_intersect(long /*int*/ source1, long /*int*/ source2);
	public static final void gdk_region_intersect(long /*int*/ source1, long /*int*/ source2) {
		lock.lock();
		try {
			_gdk_region_intersect(source1, source2);
		} finally {
			lock.unlock();
		}
	}
	/** @method flags=dynamic */
	public static final native long /*int*/ _gdk_cairo_region_create_from_surface(long /*int*/ surface);
	public static final long /*int*/ gdk_cairo_region_create_from_surface(long /*int*/ surface) {
		lock.lock();
		try {
			return _gdk_cairo_region_create_from_surface(surface);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gdk_region_new();
	public static final long /*int*/ gdk_region_new() {
		lock.lock();
		try {
			return _gdk_region_new();
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param region cast=(GdkRegion *)
	 * @param dx cast=(gint)
	 * @param dy cast=(gint)
	 */
	public static final native void _gdk_region_offset(long /*int*/ region, int dx, int dy);
	public static final void gdk_region_offset(long /*int*/ region, int dx, int dy) {
		lock.lock();
		try {
			_gdk_region_offset(region, dx, dy);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param region cast=(GdkRegion *)
	 * @param x cast=(gint)
	 * @param y cast=(gint)
	 */
	public static final native boolean _gdk_region_point_in(long /*int*/ region, int x, int y);
	public static final boolean gdk_region_point_in(long /*int*/ region, int x, int y) {
		lock.lock();
		try {
			return _gdk_region_point_in(region, x, y);
		} finally {
			lock.unlock();
		}
	}
	/** @method flags=dynamic */
	public static final native long /*int*/ _gdk_region_polygon(int[] points, int npoints, int fill_rule);
	public static final long /*int*/ gdk_region_polygon(int[] points, int npoints, int fill_rule) {
		lock.lock();
		try {
			return _gdk_region_polygon(points, npoints, fill_rule);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param rectangle flags=no_out
	 */
	public static final native long /*int*/ _gdk_region_rectangle(GdkRectangle rectangle);
	public static final long /*int*/ gdk_region_rectangle(GdkRectangle rectangle) {
		lock.lock();
		try {
			return _gdk_region_rectangle(rectangle);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param region cast=(GdkRegion *)
	 * @param rect cast=(GdkRectangle *),flags=no_out
	 */
	public static final native long /*int*/ _gdk_region_rect_in(long /*int*/ region, GdkRectangle rect);
	public static final long /*int*/ gdk_region_rect_in(long /*int*/ region, GdkRectangle rect) {
		lock.lock();
		try {
			return _gdk_region_rect_in(region, rect);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param source1 cast=(GdkRegion *)
	 * @param source2 cast=(GdkRegion *)
	 */
	public static final native void _gdk_region_subtract(long /*int*/ source1, long /*int*/ source2);
	public static final void gdk_region_subtract(long /*int*/ source1, long /*int*/ source2) {
		lock.lock();
		try {
			_gdk_region_subtract(source1, source2);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param source1 cast=(GdkRegion *)
	 * @param source2 cast=(GdkRegion *)
	 */
	public static final native void _gdk_region_union(long /*int*/ source1, long /*int*/ source2);
	public static final void gdk_region_union(long /*int*/ source1, long /*int*/ source2) {
		lock.lock();
		try {
			_gdk_region_union(source1, source2);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param region cast=(GdkRegion *)
	 * @param rect cast=(GdkRectangle *),flags=no_out
	 */
	public static final native void _gdk_region_union_with_rect(long /*int*/ region, GdkRectangle rect);
	public static final void gdk_region_union_with_rect(long /*int*/ region, GdkRectangle rect) {
		lock.lock();
		try {
			_gdk_region_union_with_rect(region, rect);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param rgba cast=(GdkRGBA *)
	 */
	public static final native long /*int*/ _gdk_rgba_to_string(GdkRGBA rgba);
	public static final long /*int*/ gdk_rgba_to_string(GdkRGBA rgba) {
		lock.lock();
		assert GTK.GTK3 : "GTK3 code was run by GTK2";
		try {
			return _gdk_rgba_to_string(rgba);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param rgba cast=(GdkRGBA *)
	 */
	public static final native void _gdk_rgba_free(long /*int*/ rgba);
	public static final void gdk_rgba_free(long /*int*/ rgba) {
		lock.lock();
		assert GTK.GTK3 : "GTK3 code was run by GTK2";
		try {
			_gdk_rgba_free(rgba);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param rgba cast=(GdkRGBA *)
	 */
	public static final native int _gdk_rgba_hash(GdkRGBA rgba);
	public static final int gdk_rgba_hash(GdkRGBA  rgba) {
		lock.lock();
		assert GTK.GTK3 : "GTK3 code was run by GTK2";
		try {
			return _gdk_rgba_hash(rgba);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param rgba cast=(GdkRGBA *)
	 * @param property cast=(const gchar *)
	 */
	public static final native long /*int*/ _gdk_rgba_parse(GdkRGBA rgba, byte[] property);
	public static final long /*int*/ gdk_rgba_parse(GdkRGBA rgba, byte[] property) {
		lock.lock();
		assert GTK.GTK3 : "GTK3 code was run by GTK2";
		try {
			return _gdk_rgba_parse(rgba, property);
		} finally {
			lock.unlock();
		}
	}
	/** @method flags=dynamic */
	public static final native long /*int*/ _gdk_screen_get_default();
	public static final long /*int*/ gdk_screen_get_default() {
		lock.lock();
		try {
			return _gdk_screen_get_default();
		} finally {
			lock.unlock();
		}
	}
	/** @param screen cast=(GdkScreen *) */
	public static final native long /*int*/ _gdk_screen_get_active_window(long /*int*/ screen);
	public static final long /*int*/ gdk_screen_get_active_window(long /*int*/ screen) {
		lock.lock();
		try {
			return _gdk_screen_get_active_window(screen);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param screen cast=(GdkScreen *)
	 */
	public static final native double _gdk_screen_get_resolution(long /*int*/ screen);
	public static final double gdk_screen_get_resolution(long /*int*/ screen) {
		lock.lock();
		try {
			return _gdk_screen_get_resolution(screen);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param screen cast=(GdkScreen *)
	 * @param monitor_num cast=(gint)
	 */
	public static final native int _gdk_screen_get_monitor_scale_factor(long /*int*/ screen, int monitor_num);
	public static final int gdk_screen_get_monitor_scale_factor(long /*int*/ screen, int monitor_num) {
		lock.lock();
		try {
			return _gdk_screen_get_monitor_scale_factor(screen, monitor_num);
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
	public static final native int _gdk_screen_get_monitor_at_point (long /*int*/ screen, int x, int y);
	public static final int gdk_screen_get_monitor_at_point (long /*int*/ screen, int x, int y) {
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
	public static final native int _gdk_screen_get_monitor_at_window(long /*int*/ screen, long /*int*/ window);
	public static final int gdk_screen_get_monitor_at_window(long /*int*/ screen, long /*int*/ window) {
		lock.lock();
		try {
			return _gdk_screen_get_monitor_at_window(screen, window);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param screen cast=(GdkScreen *)
	 * @param dest flags=no_in
	 */
	public static final native void _gdk_screen_get_monitor_geometry (long /*int*/ screen, int monitor_num, GdkRectangle dest);
	public static final void gdk_screen_get_monitor_geometry (long /*int*/ screen, int monitor_num, GdkRectangle dest) {
		lock.lock();
		try {
			_gdk_screen_get_monitor_geometry(screen, monitor_num, dest);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param screen cast=(GdkScreen *)
	 * @param dest flags=no_in
	 */
	public static final native void _gdk_screen_get_monitor_workarea (long /*int*/ screen, int monitor_num, GdkRectangle dest);
	public static final void gdk_screen_get_monitor_workarea (long /*int*/ screen, int monitor_num, GdkRectangle dest) {
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
	public static final native int _gdk_screen_get_n_monitors(long /*int*/ screen);
	public static final int gdk_screen_get_n_monitors(long /*int*/ screen) {
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
	public static final native int _gdk_screen_get_primary_monitor(long /*int*/ screen);
	public static final int gdk_screen_get_primary_monitor(long /*int*/ screen) {
		lock.lock();
		try {
			return _gdk_screen_get_primary_monitor(screen);
		} finally {
			lock.unlock();
		}
	}
	public static final native int _gdk_screen_height();
	public static final int gdk_screen_height() {
		lock.lock();
		try {
			return _gdk_screen_height();
		} finally {
			lock.unlock();
		}
	}
	public static final native int _gdk_screen_width();
	public static final int gdk_screen_width() {
		lock.lock();
		try {
			return _gdk_screen_width();
		} finally {
			lock.unlock();
		}
	}
	public static final native int _gdk_screen_width_mm();
	public static final int gdk_screen_width_mm() {
		lock.lock();
		try {
			return _gdk_screen_width_mm();
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param screen cast=(GdkScreen *)
	 */
	public static final native int _gdk_screen_get_monitor_width_mm(long /*int*/ screen, int monitor_num);
	public static final int gdk_screen_get_monitor_width_mm(long /*int*/ screen, int monitor_num) {
		lock.lock();
		try {
			return _gdk_screen_get_monitor_width_mm(screen, monitor_num);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native int _gdk_seat_grab(long /*int*/ seat, long /*int*/ window, int capabilities, boolean owner_events, long /*int*/ cursor, long /*int*/ event, long /*int*/ func, long /*int*/ func_data);
	public static final int gdk_seat_grab(long /*int*/ seat, long /*int*/ window, int capabilities, boolean owner_events, long /*int*/ cursor, long /*int*/ event, long /*int*/ func, long /*int*/ func_data) {
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
	public static final native void _gdk_seat_ungrab(long /*int*/ seat);
	public static final void gdk_seat_ungrab(long /*int*/ seat) {
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
	public static final native long /*int*/ _gdk_seat_get_pointer(long /*int*/ seat);
	public static final long /*int*/ gdk_seat_get_pointer(long /*int*/ seat) {
		lock.lock();
		try {
			return _gdk_seat_get_pointer(seat);
		} finally {
			lock.unlock();
		}
	}
	/** @param program_class cast=(const char *) */
	public static final native void _gdk_set_program_class(byte[] program_class);
	public static final void gdk_set_program_class(byte[] program_class) {
		lock.lock();
		try {
			_gdk_set_program_class(program_class);
		} finally {
			lock.unlock();
		}
	}
	/** @param atom cast=(GdkAtom) */
	public static final native void _gdk_selection_owner_get(long /*int*/ atom);
	public static final void gdk_selection_owner_get(long /*int*/ atom) {
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
	public static final native void _gdk_selection_owner_set(long /*int*/ owner, long /*int*/ atom, int time, boolean send_event);
	public static final void gdk_selection_owner_set(long /*int*/ owner, long /*int*/ atom, int time, boolean send_event) {
		lock.lock();
		try {
			_gdk_selection_owner_set(owner, atom, time, send_event);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param display cast=(GdkDisplay *)
	 * @method flags=dynamic
	 */
	public static final native boolean _gdk_x11_display_utf8_to_compound_text(long /*int*/ display, byte[] str, long /*int*/[] encoding, int[] format, long /*int*/[] ctext, int[] length);
	public static final boolean gdk_x11_display_utf8_to_compound_text(long /*int*/ display, byte[] str, long /*int*/[] encoding, int[] format, long /*int*/[] ctext, int[] length) {
		lock.lock();
		try {
			return _gdk_x11_display_utf8_to_compound_text(display, str, encoding, format, ctext, length);
		} finally {
			lock.unlock();
		}
	}
	/** @param str cast=(const gchar *) */
	public static final native long /*int*/ _gdk_utf8_to_string_target(byte[] str);
	public static final long /*int*/ gdk_utf8_to_string_target(byte[] str) {
		lock.lock();
		try {
			return _gdk_utf8_to_string_target(str);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GdkWindow *)
	 * @param x cast=(gint)
	 * @param y cast=(gint)
	 * @param button cast=(guint)
	 * @param modifiers cast=(GdkModifierType)
	 * @param button_pressrelease cast=(GdkEventType)
	 */
	public static final native boolean _gdk_test_simulate_button(long /*int*/ window, int x, int y, int button,
			int modifiers, int button_pressrelease);
	public static final boolean gdk_test_simulate_button(long /*int*/ window, int x, int y, int button, int modifiers,
			int button_pressrelease) {
		lock.lock();
		try {
			return _gdk_test_simulate_button(window, x, y, button, modifiers, button_pressrelease);
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
	public static final native int _gdk_text_property_to_utf8_list_for_display  (long /*int*/ display, long /*int*/ encoding, int format, long /*int*/ text, int length,  long /*int*/[] list);
	public static final int gdk_text_property_to_utf8_list_for_display  (long /*int*/ display, long /*int*/ encoding, int format, long /*int*/ text, int length,  long /*int*/[] list) {
		lock.lock();
		try {
			return _gdk_text_property_to_utf8_list_for_display(display, encoding, format, text, length, list);
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
	 * @method flags=dynamic
	 * @param visual cast=(GdkVisual *)
	 */
	public static final native int _gdk_visual_get_depth(long /*int*/ visual);
	public static final int gdk_visual_get_depth(long /*int*/ visual) {
		lock.lock();
		try {
			return _gdk_visual_get_depth(visual);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gdk_visual_get_system();
	public static final long /*int*/ gdk_visual_get_system() {
		lock.lock();
		try {
			return _gdk_visual_get_system();
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param win_x cast=(gint *)
	 * @param win_y cast=(gint *)
	 */
	public static final native long /*int*/ _gdk_window_at_pointer(int[] win_x, int[] win_y);
	public static final long /*int*/ gdk_window_at_pointer(int[] win_x, int[] win_y) {
		lock.lock();
		try {
			return _gdk_window_at_pointer(win_x, win_y);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GdkWindow *)
	 * @param rectangle cast=(GdkRectangle *),flags=no_out
	 */
	public static final native void _gdk_window_begin_paint_rect(long /*int*/ window, GdkRectangle rectangle);
	public static final void gdk_window_begin_paint_rect(long /*int*/ window, GdkRectangle rectangle) {
		lock.lock();
		try {
			_gdk_window_begin_paint_rect(window, rectangle);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param window cast=(GdkWindow *)
	 */
	public static final native long /*int*/ _gdk_window_create_similar_surface(long /*int*/ window, int content, int width, int height);
	public static final long /*int*/ gdk_window_create_similar_surface(long /*int*/ window, int content, int width, int height) {
		lock.lock();
		try {
			return _gdk_window_create_similar_surface(window, content, width, height);
		} finally {
			lock.unlock();
		}
	}
	/** @param window cast=(GdkWindow *) */
	public static final native void _gdk_window_destroy(long /*int*/ window);
	public static final void gdk_window_destroy(long /*int*/ window) {
		lock.lock();
		try {
			_gdk_window_destroy(window);
		} finally {
			lock.unlock();
		}
	}
	/** @param window cast=(GdkWindow *) */
	public static final native void _gdk_window_end_paint(long /*int*/ window);
	public static final void gdk_window_end_paint(long /*int*/ window) {
		lock.lock();
		try {
			_gdk_window_end_paint(window);
		} finally {
			lock.unlock();
		}
	}
	/** @param window cast=(GdkWindow *) */
	public static final native long /*int*/ _gdk_window_get_children(long /*int*/ window);
	public static final long /*int*/ gdk_window_get_children(long /*int*/ window) {
		lock.lock();
		try {
			return _gdk_window_get_children(window);
		} finally {
			lock.unlock();
		}
	}
	/** @param window cast=(GdkWindow *) */
	public static final native int _gdk_window_get_events(long /*int*/ window);
	public static final int gdk_window_get_events(long /*int*/ window) {
		lock.lock();
		try {
			return _gdk_window_get_events(window);
		} finally {
			lock.unlock();
		}
	}
	/** @param window cast=(GdkWindow *) */
	public static final native void _gdk_window_focus(long /*int*/ window, int timestamp);
	public static final void gdk_window_focus(long /*int*/ window, int timestamp) {
		lock.lock();
		try {
			_gdk_window_focus(window, timestamp);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GdkWindow *)
	 * @param rect cast=(GdkRectangle *),flags=no_in
	 */
	public static final native void _gdk_window_get_frame_extents(long /*int*/ window, GdkRectangle rect);
	public static final void gdk_window_get_frame_extents(long /*int*/ window, GdkRectangle rect) {
		lock.lock();
		try {
			_gdk_window_get_frame_extents(window, rect);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param window cast=(GdkWindow *)
	 * @param x_offset cast=(gint *)
	 * @param y_offset cast=(gint *)
	 */
	public static final native void _gdk_window_get_internal_paint_info(long /*int*/ window, long /*int*/ [] real_drawable, int[] x_offset, int[] y_offset);
	public static final void gdk_window_get_internal_paint_info(long /*int*/ window, long /*int*/ [] real_drawable, int[] x_offset, int[] y_offset) {
		lock.lock();
		try {
			_gdk_window_get_internal_paint_info(window, real_drawable, x_offset, y_offset);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GdkWindow *)
	 * @param x cast=(gint *)
	 * @param y cast=(gint *)
	 */
	public static final native int _gdk_window_get_origin(long /*int*/ window, int[] x, int[] y);
	public static final int gdk_window_get_origin(long /*int*/ window, int[] x, int[] y) {
		lock.lock();
		try {
			return _gdk_window_get_origin(window, x, y);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param window cast=(GdkWindow *)
	 * @param device cast=(GdkDevice *)
	 * @param x cast=(gint *)
	 * @param y cast=(gint *)
	 * @param mask cast=(GdkModifierType *)
	 */
	public static final native long /*int*/ _gdk_window_get_device_position(long /*int*/ window, long /*int*/ device, int[] x, int[] y, int[] mask);
	public static final long /*int*/ gdk_window_get_device_position(long /*int*/ window, long /*int*/ device, int[] x, int[] y, int[] mask) {
		lock.lock();
		try {
			return _gdk_window_get_device_position(window, device, x, y, mask);
		} finally {
			lock.unlock();
		}
	}
	/** @param window cast=(GdkWindow *) */
	public static final native long /*int*/ _gdk_window_get_parent(long /*int*/ window);
	public static final long /*int*/ gdk_window_get_parent(long /*int*/ window) {
		lock.lock();
		try {
			return _gdk_window_get_parent(window);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param window cast=(GdkWindow *)
	 * @param x cast=(gint *)
	 * @param y cast=(gint *)
	 * @param mask cast=(GdkModifierType *)
	 */
	public static final native long /*int*/ _gdk_window_get_pointer(long /*int*/ window, int[] x, int[] y, int[] mask);
	public static final long /*int*/ gdk_window_get_pointer(long /*int*/ window, int[] x, int[] y, int[] mask) {
		lock.lock();
		try {
			return _gdk_window_get_pointer(window, x, y, mask);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GdkWindow *)
	 * @param x cast=(gint *)
	 * @param y cast=(gint *)
	 */
	public static final native void _gdk_window_get_position(long /*int*/ window, int[] x, int[] y);
	public static final void gdk_window_get_position(long /*int*/ window, int[] x, int[] y) {
		lock.lock();
		try {
			_gdk_window_get_position(window, x, y);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GdkWindow *)
	 * @param x cast=(gint *)
	 * @param y cast=(gint *)
	 */
	public static final native void _gdk_window_get_root_origin(long /*int*/ window, int[] x, int[] y);
	public static final void gdk_window_get_root_origin(long /*int*/ window, int[] x, int[] y) {
		lock.lock();
		try {
			_gdk_window_get_root_origin(window, x, y);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GdkWindow *)
	 * @param data cast=(gpointer *)
	 */
	public static final native void _gdk_window_get_user_data(long /*int*/ window, long /*int*/[] data);
	public static final void gdk_window_get_user_data(long /*int*/ window, long /*int*/[] data) {
		lock.lock();
		try {
			_gdk_window_get_user_data(window, data);
		} finally {
			lock.unlock();
		}
	}
	/** @param window cast=(GdkWindow *) */
	public static final native void _gdk_window_hide(long /*int*/ window);
	public static final void gdk_window_hide(long /*int*/ window) {
		lock.lock();
		try {
			_gdk_window_hide(window);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GdkWindow *)
	 * @param rectangle cast=(GdkRectangle *),flags=no_out
	 * @param invalidate_children cast=(gboolean)
	 */
	public static final native void _gdk_window_invalidate_rect(long /*int*/ window, GdkRectangle rectangle, boolean invalidate_children);
	public static final void gdk_window_invalidate_rect(long /*int*/ window, GdkRectangle rectangle, boolean invalidate_children) {
		lock.lock();
		try {
			_gdk_window_invalidate_rect(window, rectangle, invalidate_children);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param window cast=(GdkWindow *)
	 * @param invalidate_children cast=(gboolean)
	 */
	public static final native void _gdk_window_invalidate_region(long /*int*/ window, long /*int*/ region, boolean invalidate_children);
	public static final void gdk_window_invalidate_region(long /*int*/ window, long /*int*/ region, boolean invalidate_children) {
		lock.lock();
		try {
			_gdk_window_invalidate_region(window, region, invalidate_children);
		} finally {
			lock.unlock();
		}
	}
	/** @param window cast=(GdkWindow *) */
	public static final native boolean _gdk_window_is_visible(long /*int*/ window);
	public static final boolean gdk_window_is_visible(long /*int*/ window) {
		lock.lock();
		try {
			return _gdk_window_is_visible(window);
		} finally {
			lock.unlock();
		}
	}
	/** @param window cast=(GdkWindow *) */
	public static final native void _gdk_window_move(long /*int*/ window, int x, int y);
	public static final void gdk_window_move(long /*int*/ window, int x, int y) {
		lock.lock();
		try {
			_gdk_window_move(window, x, y);
		} finally {
			lock.unlock();
		}
	}
	/** @param window cast=(GdkWindow *) */
	public static final native void _gdk_window_move_resize(long /*int*/ window, int x, int y, int width, int height);
	public static final void gdk_window_move_resize(long /*int*/ window, int x, int y, int width, int height) {
		lock.lock();
		try {
			_gdk_window_move_resize(window, x, y, width, height);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param parent cast=(GdkWindow *)
	 * @param attributes flags=no_out
	 */
	public static final native long /*int*/ _gdk_window_new(long /*int*/ parent, GdkWindowAttr attributes, int attributes_mask);
	public static final long /*int*/ gdk_window_new(long /*int*/ parent, GdkWindowAttr attributes, int attributes_mask) {
		lock.lock();
		try {
			return _gdk_window_new(parent, attributes, attributes_mask);
		} finally {
			lock.unlock();
		}
	}
	/** @param window cast=(GdkWindow *) */
	public static final native void _gdk_window_lower(long /*int*/ window);
	public static final void gdk_window_lower(long /*int*/ window) {
		lock.lock();
		try {
			_gdk_window_lower(window);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native void _gdk_window_process_all_updates();
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
	public static final native void _gdk_window_process_updates(long /*int*/ window, boolean update_children);
	public static final void gdk_window_process_updates(long /*int*/ window, boolean update_children) {
		lock.lock();
		try {
			_gdk_window_process_updates(window, update_children);
		} finally {
			lock.unlock();
		}
	}
	/** @param window cast=(GdkWindow *) */
	public static final native void _gdk_window_raise(long /*int*/ window);
	public static final void gdk_window_raise(long /*int*/ window) {
		lock.lock();
		try {
			_gdk_window_raise(window);
		} finally {
			lock.unlock();
		}
	}
	/** @param window cast=(GdkWindow *) */
	public static final native void _gdk_window_resize(long /*int*/ window, int width, int height);
	public static final void gdk_window_resize(long /*int*/ window, int width, int height) {
		lock.lock();
		try {
			_gdk_window_resize(window, width, height);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param window cast=(GdkWindow *)
	 * @param sibling cast=(GdkWindow *)
	 * @param above cast=(gboolean)
	 */
	public static final native void _gdk_window_restack(long /*int*/ window, long /*int*/ sibling, boolean above);
	public static final void gdk_window_restack(long /*int*/ window, long /*int*/ sibling, boolean above) {
		lock.lock();
		try {
			_gdk_window_restack(window, sibling, above);
		} finally {
			lock.unlock();
		}
	}
	/** @method flags=dynamic */
	public static final native void _gdk_window_set_background_pattern(long /*int*/ window, long /*int*/ pattern);
	public static final void gdk_window_set_background_pattern(long /*int*/ window, long /*int*/ pattern) {
		lock.lock();
		try {
			_gdk_window_set_background_pattern(window, pattern);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param window cast=(GdkWindow *)
	 * @param parent_relative cast=(gboolean)
	 */
	public static final native void _gdk_window_set_back_pixmap(long /*int*/ window, long /*int*/ pixmap, boolean parent_relative);
	public static final void gdk_window_set_back_pixmap(long /*int*/ window, long /*int*/ pixmap, boolean parent_relative) {
		lock.lock();
		try {
			_gdk_window_set_back_pixmap(window, pixmap, parent_relative);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GdkWindow *)
	 * @param cursor cast=(GdkCursor *)
	 */
	public static final native void _gdk_window_set_cursor(long /*int*/ window, long /*int*/ cursor);
	public static final void gdk_window_set_cursor(long /*int*/ window, long /*int*/ cursor) {
		lock.lock();
		try {
			_gdk_window_set_cursor(window, cursor);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GdkWindow *)
	 * @param decorations cast=(GdkWMDecoration)
	 */
	public static final native void _gdk_window_set_decorations(long /*int*/ window, int decorations);
	public static final void gdk_window_set_decorations(long /*int*/ window, int decorations) {
		lock.lock();
		try {
			_gdk_window_set_decorations(window, decorations);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GdkWindow *)
	 * @param functions cast=(GdkWMFunction)
	 */
	public static final native void _gdk_window_set_functions(long /*int*/ window, int functions);
	public static final void gdk_window_set_functions(long /*int*/ window, int functions) {
		lock.lock();
		try {
			_gdk_window_set_functions(window, functions);
		} finally {
			lock.unlock();
		}
	}
	/** @param window cast=(GdkWindow *) */
	public static final native void _gdk_window_set_events(long /*int*/ window, int event_mask);
	public static final void gdk_window_set_events(long /*int*/ window, int event_mask) {
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
	public static final native void _gdk_window_set_override_redirect(long /*int*/ window, boolean override_redirect);
	public static final void gdk_window_set_override_redirect(long /*int*/ window, boolean override_redirect) {
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
	public static final native void _gdk_window_set_user_data(long /*int*/ window, long /*int*/ user_data);
	public static final void gdk_window_set_user_data(long /*int*/ window, long /*int*/ user_data) {
		lock.lock();
		try {
			_gdk_window_set_user_data(window, user_data);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param window cast=(GdkWindow *)
	 */
	public static final native void _gdk_window_shape_combine_region (long /*int*/ window, long /*int*/  shape_region, int offset_x,  int offset_y);
	public static final void gdk_window_shape_combine_region (long /*int*/ window, long /*int*/  shape_region, int offset_x,  int offset_y) {
		lock.lock();
		try {
			_gdk_window_shape_combine_region(window, shape_region, offset_x, offset_y);
		} finally {
			lock.unlock();
		}
	}
	/** @param window cast=(GdkWindow *) */
	public static final native void _gdk_window_show(long /*int*/ window);
	public static final void gdk_window_show(long /*int*/ window) {
		lock.lock();
		try {
			_gdk_window_show(window);
		} finally {
			lock.unlock();
		}
	}
	/** @param window cast=(GdkWindow *) */
	public static final native void _gdk_window_show_unraised(long /*int*/ window);
	public static final void gdk_window_show_unraised(long /*int*/ window) {
		lock.lock();
		try {
			_gdk_window_show_unraised(window);
		} finally {
			lock.unlock();
		}
	}




}
