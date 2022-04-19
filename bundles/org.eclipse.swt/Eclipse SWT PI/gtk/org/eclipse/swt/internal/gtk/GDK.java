/*******************************************************************************
 * Copyright (c) 2018, 2020 Red Hat Inc. and others. All rights reserved.
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
	public static final int GDK_CROSSING_NORMAL = 0;
	public static final int GDK_CROSSING_GRAB = 1;
	public static final int GDK_CROSSING_UNGRAB = 2;
	public static final int GDK_Break = 0xff6b;
	public static final int GDK_Caps_Lock = 0xffE5;
	public static final int GDK_Control_L = 0xffe3;
	public static final int GDK_Control_R = 0xffe4;
	public static final int GDK_CURRENT_TIME = 0x0;
	public static final int GDK_DECOR_BORDER = 0x2;
	public static final int GDK_DECOR_MAXIMIZE = 0x40;
	public static final int GDK_DECOR_MENU = 0x10;
	public static final int GDK_DECOR_MINIMIZE = 0x20;
	public static final int GDK_DECOR_RESIZEH = 0x4;
	public static final int GDK_DECOR_TITLE = 0x8;
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
	public static final int GDK_Help = 0xFF6A;
	public static final int GDK_HINT_MIN_SIZE = 1 << 1;
	public static final int GDK_HINT_MAX_SIZE = 1 << 2;
	public static final int GDK_Home = 0xff50;
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
	public static final int GDK_Num_Lock = 0xFF7F;
	public static final int GDK_OWNERSHIP_NONE = 0;
	public static final int GDK_POINTER_MOTION_HINT_MASK = 0x8;
	public static final int GDK_POINTER_MOTION_MASK = 0x4;
	public static final int GDK_PROPERTY_NOTIFY = 16;
	public static final int GDK_Page_Down = 0xff56;
	public static final int GDK_Page_Up = 0xff55;
	public static final int GDK_Pause = 0xff13;
	public static final int GDK_Print = 0xff61;
	public static final int GDK_Return = 0xff0d;
	public static final int GDK_Right = 0xff53;
	public static final int GDK_space = 0x20;
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
	public static final int GDK_SURFACE_STATE_MINIMIZED = 1 << 1;
	public static final int GDK_SURFACE_STATE_MAXIMIZED = 1 << 2;
	public static final int GDK_SURFACE_STATE_FULLSCREEN = 1 << 4;
	public static final int GDK_Shift_L = 0xffe1;
	public static final int GDK_Shift_R = 0xffe2;
	public static final int GDK_Scroll_Lock = 0xff14;
	public static final int GDK_Tab = 0xff09;
	public static final int GDK_Up = 0xff52;
	public static final int GDK_WINDOW_CHILD = 2;
	public static final int GDK_WINDOW_STATE = 32;
	public static final int GDK_WINDOW_STATE_WITHDRAWN  = 1 << 0;
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
	public static final native int GdkRectangle_sizeof();

	/** Macros */
	/** @param event cast=(GdkEvent *) */
	public static final native int GDK_EVENT_TYPE(long event);
	/** @param event cast=(GdkEventAny *) */
	public static final native long GDK_EVENT_WINDOW(long event);
	/** @param display cast=(GdkDisplay *) */
	public static final native boolean GDK_IS_X11_DISPLAY(long display);
	/** @method flags=const */
	public static final native long GDK_TYPE_RGBA();
	/** @method flags=const */
	public static final native long GDK_TYPE_PIXBUF();

	/** @param gdkdisplay cast=(GdkDisplay *) */
	public static final native long gdk_x11_display_get_xdisplay(long gdkdisplay);
	public static final native long gdk_x11_get_default_xdisplay();
	/**
	 * @method flags=dynamic
	 * @param xvisualid cast=(VisualID)
	 */
	/* [GTK3/GTK4, GTK3 uses GdkScreen but GTK4 uses GdkX11Screen -- method signature otherwise identical] */
	public static final native long gdk_x11_screen_lookup_visual(long screen, int xvisualid);
	/**
	 * @method flags=dynamic
	 */
	/* [GTK3/GTK4, GTK3 uses GdkScreen but GTK4 uses GdkX11Screen -- method signature otherwise identical] */
	public static final native long gdk_x11_screen_get_window_manager_name(long screen);
	/**
	* @param gdkwindow cast=(GdkWindow *)
	*/
	/* [GTK3 only, if-def'd in os.h] */
	public static final native long gdk_x11_window_get_xid(long gdkwindow);
	/**
	* @param surface cast=(GdkSurface *)
	*/
	/* [GTK4 only, if-def'd in os.h] */
	public static final native long gdk_x11_surface_get_xid(long surface);
	/**
	 * @param gdkdisplay cast=(GdkDisplay *)
	 * @param xid cast=(Window)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native long gdk_x11_window_lookup_for_display(long gdkdisplay, long xid);
	/**
	 * @param gdkdisplay cast=(GdkDisplay *)
	 * @param xid cast=(Window)
	 */
	/* [GTK4 only, if-def'd in os.h] */
	public static final native long gdk_x11_surface_lookup_for_display(long gdkdisplay, long xid);
	/**
	 * @method flags=dynamic
	 * @param atom_name cast=(const gchar *),flags=no_out critical
	 */
	/* [GTK3 only] */
	public static final native long gdk_atom_intern(byte[] atom_name, boolean only_if_exists);
	/** @param atom cast=(GdkAtom) */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native long gdk_atom_name(long atom);
	/**
	 * @method flags=dynamic
	 * @param window cast=(GdkWindow *)
	 */
	/* [GTK3 only, if-def'd in os.h; 3.22 deprecated, replaced] */
	public static final native long gdk_cairo_create(long window);
	/**
	 * @method flags=dynamic
	 * @param cr cast=(cairo_t *)
	 * @param rect cast=(GdkRectangle *),flags=no_in
	 */
	public static final native boolean gdk_cairo_get_clip_rectangle(long cr, GdkRectangle rect);
	/**
	 * @param cairo cast=(cairo_t *)
	 * @param region cast=(cairo_region_t *)
	 */
	public static final native void gdk_cairo_region(long cairo, long region);
	/**
	 * @param cairo cast=(cairo_t *)
	 * @param rgba cast=(const GdkRGBA *)
	 */
	public static final native void gdk_cairo_set_source_rgba(long cairo, GdkRGBA rgba);
	/**
	 * @param window cast=(GdkWindow *)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native int gdk_window_get_state(long window);
	/**
	 * @param window cast=(GdkWindow *)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native int gdk_window_get_width(long window);
	/**
	 * @param surface cast=(GdkSurface *)
	 */
	/* [GTK4 only, if-def'd in os.h] */
	public static final native int gdk_surface_get_width(long surface);
	/**
	 * @param window cast=(GdkWindow *)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native long gdk_window_get_visible_region(long window);
	/**
	 *  @param window cast=(GdkWindow *)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native int gdk_window_get_height(long window);
	/**
	 *  @param surface cast=(GdkSurface *)
	 */
	/* [GTK4 only, if-def'd in os.h] */
	public static final native int gdk_surface_get_height(long surface);
	/**
	 * @param surface cast=(GdkSurface *)
	 * @param region cast=(cairo_region_t *)
	 */
	/* [GTK4 only, if-def'd in os.h] */
	public static final native void gdk_surface_set_input_region(long surface, long region);
	/**
	 * @param surface cast=(GdkSurface *)
	 * @param region cast=(cairo_region_t *)
	 */
	/* [GTK4 only, if-def'd in os.h] */
	public static final native void gdk_surface_set_opaque_region(long surface, long region);
	/**
	 * @param cairo cast=(cairo_t *)
	 * @param pixbuf cast=(const GdkPixbuf *)
	 * @param pixbuf_x cast=(gdouble)
	 * @param pixbuf_y cast=(gdouble)
	 */
	public static final native void gdk_cairo_set_source_pixbuf(long cairo, long pixbuf, double pixbuf_x, double pixbuf_y);
	/**
	 * @param cairo cast=(cairo_t *)
	 * @param window cast=(GdkWindow *)
	 * @param x cast=(gdouble)
	 * @param y cast=(gdouble)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gdk_cairo_set_source_window(long cairo, long window, int x, int y);


	/* GdkCursor [GTK3 only] */
	/**
	 * @param display cast=(GdkDisplay *)
	 * @param cursor_name cast=(const gchar *)
	 */
	public static final native long gdk_cursor_new_from_name(long display, String cursor_name);
	/**
	 * @param display cast=(GdkDisplay *)
	 * @param pixbuf cast=(GdkPixbuf *)
	 * @param x cast=(gint)
	 * @param y cast=(gint)
	 */
	public static final native long gdk_cursor_new_from_pixbuf(long display, long pixbuf, int x, int y);

	/* GdkCursor [GTK4 only] */
	/**
	 * @param cursor_name cast=(const gchar *)
	 * @param fallback cast=(GdkCursor *)
	 */
	public static final native long gdk_cursor_new_from_name(String cursor_name, long fallback);
	/**
	 * @param texture cast=(GdkTexture *)
	 * @param x cast=(gint)
	 * @param y cast=(gint)
	 * @param fallback cast=(GdkCursor *)
	 */
	public static final native long gdk_cursor_new_from_texture(long texture, int x, int y, long fallback);
	/**
	 * @method flags=dynamic
	 * @param display cast=(GdkDisplay *)
	 */
	public static final native long gdk_x11_display_get_default_group(long display);
	/**
	 *  @param window cast=(GdkWindow *)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native long gdk_window_get_display(long window);
	/**
	 *  @param surface cast=(GdkSurface *)
	 */
	/* [GTK4 only, if-def'd in os.h] */
	public static final native long gdk_surface_get_display(long surface);

	/**
	 * @method flags=dynamic
	 */
	// TODO GTK4 function removed
	public static final native int gdk_drag_context_get_actions(long context);
	/**
	 * @param context cast=(GdkDragContext *)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native long gdk_drag_context_get_dest_window(long context);
	/**
	 * @method flags=dynamic
	 */
	// TODO GTK4 function removed
	public static final native int gdk_drag_context_get_selected_action(long context);
	/**
	 * @param context cast=(GdkDragContext *)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native long gdk_drag_context_list_targets(long context);
	/**
	 * @param context cast=(GdkDragContext *)
	 * @param action cast=(GdkDragAction)
	 * @param time cast=(guint32)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gdk_drag_status(long context, int action, int time);

	/* GDK Events [GTK3 only, if-def'd in os.h] */
	/** @param event cast=(GdkEvent *) */
	public static final native long gdk_event_copy(long event);
	/** @param event cast=(GdkEvent *) */
	public static final native void gdk_event_free(long event);
	public static final native long gdk_event_get();
	/**
	 * @param event cast=(GdkEvent *)
	 * @param px cast=(gdouble *)
	 * @param py cast=(gdouble *)
	 */
	public static final native boolean gdk_event_get_coords(long event, double[] px, double[] py);
	/**
	 * @param event cast=(GdkEvent *)
	 * @param button cast=(guint *)
	 */
	public static final native boolean gdk_event_get_button(long event, int[] button);
	/**
	 * @param event cast=(GdkEvent *)
	 * @param keyval cast=(guint *)
	 */
	public static final native boolean gdk_event_get_keyval(long event,int [] keyval);
	/**
	 * @param event cast=(GdkEvent *)
	 * @param keycode cast=(guint16 *)
	 */
	public static final native boolean gdk_event_get_keycode(long event, short [] keycode);
	/**
	 * @param event cast=(GdkEvent *)
	 * @param x cast=(gdouble *)
	 * @param y cast=(gdouble *)
	 */
	public static final native boolean gdk_event_get_root_coords(long event, double[] x, double[] y);
	/**
	 * @param event cast=(GdkEvent *)
	 * @param pmod cast=(GdkModifierType *)
	 */
	public static final native boolean gdk_event_get_state(long event, int[] pmod);
	/**
	 * @param event cast=(const GdkEvent *)
	 * @param delta_x cast=(gdouble *)
	 * @param delta_y cast=(gdouble *)
	 */
	public static final native boolean gdk_event_get_scroll_deltas(long event, double[] delta_x, double[] delta_y);
	/**
	 * @param event cast=(const GdkEvent *)
	 * @param direction cast=(GdkScrollDirection *)
	 */
	public static final native boolean gdk_event_get_scroll_direction(long event, int [] direction);


	/* GDK Events (GTK4 only, if-def'd in os.h) */
	/** @param event cast=(GdkEvent *) */
	public static final native long gdk_event_ref(long event);
	/** @param event cast=(GdkEvent *) */
	public static final native void gdk_event_unref(long event);
	/**
	 * @param event cast=(GdkEvent *)
	 * @param px cast=(double *)
	 * @param py cast=(double *)
	 */
	public static final native boolean gdk_event_get_position(long event, double[] px, double[] py);
	/** @param event cast=(GdkEvent *) */
	public static final native int gdk_crossing_event_get_mode(long event);
	/** @param event cast=(GdkEvent *) */
	public static final native int gdk_button_event_get_button(long event);
	/** @param event cast=(GdkEvent *) */
	public static final native boolean gdk_focus_event_get_in(long event);
	/** @param event cast=(GdkEvent *) */
	public static final native int gdk_key_event_get_keycode(long event);
	/** @param event cast=(GdkEvent *) */
	public static final native int gdk_key_event_get_keyval(long event);
	/** @param event cast=(GdkEvent *) */
	public static final native int gdk_key_event_get_layout(long event);
	/** @param event cast=(GdkEvent *) */
	public static final native int gdk_event_get_modifier_state(long event);
	/** @param event cast=(GdkEvent *) */
	public static final native void gdk_scroll_event_get_deltas(long event, double[] delta_x, double[] delta_y);
	/** @param event cast=(GdkEvent *) */
	public static final native int gdk_scroll_event_get_direction(long event);

	/**
	 * @method flags=dynamic
	 */
	public static final native long gdk_event_get_seat(long event);
	/**
	 * @param event cast=(GdkEvent *)
	 */
	/* [GTK4 only, if-def'd in os.h] **/
	public static final native long gdk_event_get_surface(long event);
	/**
	 * @param event cast=(GdkEvent *)
	 */
	/* [GTK3 only, if-def'd in os.h] **/
	public static final native long gdk_event_get_window(long event);
	/** @param event cast=(GdkEvent *) */
	public static final native int gdk_event_get_time(long event);
	/**
	 * @method flags=dynamic
	 * @param event cast=(GdkEvent *)
	 */
	/* [GTK3.10+] */
	public static final native int gdk_event_get_event_type(long event);
	/**
	 * @method flags=dynamic
	 */
	// TODO GTK4 function removed
	public static final native void gdk_event_handler_set(long func, long data, long notify);
	/* [GTK3 only, if-def'd in os.h] */
	public static final native long gdk_event_new(int type);
	/* [GTK3 only, if-def'd in os.h] */
	public static final native long gdk_event_peek();
	/**
	 * @param event cast=(GdkEvent *)
	 * @param device cast=(GdkDevice *)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gdk_event_set_device(long event, long device);
	/** @param event cast=(GdkEvent *) */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gdk_event_put(long event);
	/**
	 * @param display cast=(GdkDisplay *)
	 */
	public static final native void gdk_x11_display_error_trap_push(long display);
	/**
	 * @param display cast=(GdkDisplay *)
	 */
	public static final native void gdk_x11_display_error_trap_pop_ignored(long display);
	/* [GTK3 only, if-def'd in os.h] */
	public static final native long gdk_get_default_root_window();
	/**
	 * @method flags=dynamic
	 * @param display cast=(GdkDisplay *)
	 */
	/* [GTK3 only] */
	public static final native long gdk_keymap_get_for_display(long display);

	/**
	 * @method flags=dynamic
	 * @param keymap cast=(GdkKeymap *)
	 * @param hardware_keycode cast=(guint)
	 * @param state cast=(GdkModifierType)
	 * @param group cast=(gint)
	 * @param keyval cast=(guint *)
	 * @param effective_group cast=(gint *)
	 * @param level cast=(gint *)
	 * @param consumed_modifiers cast=(GdkModifierType *)
	 */
	/* [GTK3 only] */
	public static final native boolean gdk_keymap_translate_keyboard_state(long keymap, int hardware_keycode, int state, int group, int[] keyval, int[] effective_group, int[] level,  int[] consumed_modifiers);
	/**
	 * @method flags=dynamic
	 * @param keymap cast=(GdkKeymap*)
	 * @param keyval cast=(guint)
	 * @param keys cast=(GdkKeymapKey**)
	 * @param n_keys cast=(gint*)
	 */
	/* [GTK3 only] */
	public static final native boolean gdk_keymap_get_entries_for_keyval(long keymap, int keyval, long [] keys, int[] n_keys);
	public static final native long gdk_keyval_to_lower(long keyval);
	public static final native long gdk_keyval_to_unicode(long keyval);
	/** @param keyval cast=(guint) */
	public static final native long gdk_keyval_name(int keyval);
	/**
	 * @method flags=dynamic
	 */
	/* [GTK3 only] */
	public static final native long gdk_pango_context_get();
	/**
	 * @param layout cast=(PangoLayout *)
	 * @param index_ranges cast=(gint *)
	 */
	public static final native long gdk_pango_layout_get_clip_region(long layout, int x_origin, int y_origin, int[] index_ranges, int n_ranges);

	/** @param animation cast=(GdkPixbufAnimation *) */
	public static final native boolean gdk_pixbuf_animation_is_static_image(long animation);
	/** @param iter cast=(GdkPixbufAnimationIter *) */
	public static final native int gdk_pixbuf_animation_iter_get_delay_time(long iter);
	/** @param iter cast=(GdkPixbufAnimationIter *) */
	public static final native long gdk_pixbuf_animation_iter_get_pixbuf(long iter);
	/**
	 * @method flags=ignore_deprecations
	 * @param iter cast=(GdkPixbufAnimationIter *)
	 * @param current_time cast=(const GTimeVal *)
	 */
	public static final native boolean gdk_pixbuf_animation_iter_advance(long iter, long current_time);
	/**
	 * @method flags=ignore_deprecations
	 * @param animation cast=(GdkPixbufAnimation *)
	 * @param start_time cast=(const GTimeVal *)
	 */
	public static final native long gdk_pixbuf_animation_get_iter(long animation, long start_time);
	/** @param animation cast=(GdkPixbufAnimation *) */
	public static final native long gdk_pixbuf_animation_get_static_image(long animation);
	/**
	 * @param src_pixbuf cast=(GdkPixbuf *)
	 * @param dest_pixbuf cast=(GdkPixbuf *)
	 */
	public static final native void gdk_pixbuf_copy_area(long src_pixbuf, int src_x, int src_y, int width, int height, long dest_pixbuf, int dest_x, int dest_y);
	/** @param pixbuf cast=(const GdkPixbuf *) */
	public static final native boolean gdk_pixbuf_get_has_alpha(long pixbuf);
	/** @param pixbuf cast=(const GdkPixbuf *) */
	public static final native int gdk_pixbuf_get_height(long pixbuf);
	/** @param pixbuf cast=(const GdkPixbuf *) */
	public static final native long gdk_pixbuf_get_pixels(long pixbuf);
	/** @param pixbuf cast=(const GdkPixbuf *) */
	public static final native int gdk_pixbuf_get_rowstride(long pixbuf);
	/** @param pixbuf cast=(const GdkPixbuf *) */
	public static final native int gdk_pixbuf_get_width(long pixbuf);
	/** @param pixbuf cast=(const GdkPixbuf *) */
	public static final native int gdk_pixbuf_get_n_channels(long pixbuf);
	/** @param pixbuf cast=(const GdkPixbuf *) */
	public static final native int gdk_pixbuf_get_bits_per_sample(long pixbuf);
	/** @param pixbuf cast=(const GdkPixbuf *) */
	public static final native long gdk_pixbuf_copy(long pixbuf);
	/** @param loader cast=(GdkPixbufLoader *) */
	public static final native long gdk_pixbuf_loader_get_format(long loader);
	/** @param format cast=(GdkPixbufFormat *) */
	public static final native long gdk_pixbuf_format_get_name(long format);
	/** @param loader cast=(GdkPixbufLoader *) */
	public static final native long gdk_pixbuf_loader_get_animation(long loader);
	/**
	 * @param data cast=(const guchar *)
	 * @param colorspace cast=(GdkColorspace)
	 * @param has_alpha cast=(gboolean)
	 * @param destroy_fn cast=(GdkPixbufDestroyNotify)
	 * @param destroy_fn_data cast=(gpointer)
	 */
	public static final native long gdk_pixbuf_new_from_data(long data, int colorspace, boolean has_alpha, int bits_per_sample, int width, int height, int rowstride, long destroy_fn, long destroy_fn_data);
	public static final native long gdk_pixbuf_loader_new();
	/**
	 * @param loader cast=(GdkPixbufLoader *)
	 * @param error cast=(GError **)
	 */
	public static final native boolean gdk_pixbuf_loader_close(long loader, long [] error);
	/** @param loader cast=(GdkPixbufLoader *) */
	public static final native long gdk_pixbuf_loader_get_pixbuf(long loader);
	/**
	 * @param loader cast=(GdkPixbufLoader *)
	 * @param buffer cast=(const guchar *)
	 * @param count cast=(gsize)
	 * @param error cast=(GError **)
	 */
	public static final native boolean gdk_pixbuf_loader_write(long loader, long buffer, int count, long [] error);
	/**
	 * @param colorspace cast=(GdkColorspace)
	 * @param has_alpha cast=(gboolean)
	 */
	public static final native long gdk_pixbuf_new(int colorspace, boolean has_alpha, int bits_per_sample, int width, int height);
	/**
	 * @param filename cast=(const char *)
	 * @param error cast=(GError**)
	 */
	public static final native long gdk_pixbuf_new_from_file(byte[] filename, long [] error);
	/**
	 * @param pixbuf cast=(GdkPixbuf *)
	 * @param buffer cast=(gchar **)
	 * @param buffer_size cast=(gsize *)
	 * @param type cast=(const char *)
	 * @param option_keys cast=(char **)
	 * @param option_values cast=(char **)
	 * @param error cast=(GError **)
	 */
	public static final native boolean gdk_pixbuf_save_to_bufferv(long pixbuf, long [] buffer, long [] buffer_size, byte [] type, long [] option_keys, long [] option_values, long [] error);
	/**
	 * @param src cast=(const GdkPixbuf *)
	 * @param interp_type cast=(GdkInterpType)
	 */
	public static final native long gdk_pixbuf_scale_simple(long src, int dest_width, int dest_height, int interp_type);

	/* GdkPixbuf Interaction [GTK3 only] */
	/**
	 * @method flags=dynamic
	 * @param window cast=(GdkWindow *)
	 */
	public static final native long gdk_pixbuf_get_from_window(long window, int x, int y, int width, int height);

	/* GdkPixbuf Interaction [GTK4 only] */
	/**
	 * @method flags=dynamic
	 * @param texture cast=(GdkTexture *)
	 */
	public static final native long gdk_pixbuf_get_from_texture(long texture);
	/**
	 * @method flags=dynamic
	 * @param surface cast=(cairo_surface_t *)
	 */
	public static final native long gdk_pixbuf_get_from_surface(long surface, int src_x, int src_y, int width, int height);


	/* GdkDevice */
	/**@param device cast=(GdkDevice *) */
	public static final native long gdk_device_get_seat(long device);

	/* GdkDevice [GTK3 only] */
	/**
	 * @method flags=dynamic
	 * @param device cast=(GdkDevice *)
	 */
	public static final native long gdk_device_get_associated_device(long device);
	/**
	 * @method flags=dynamic
	 * @param device cast=(GdkDevice *)
	 * @param screen cast=(GdkScreen *)
	 * @param x cast=(gint)
	 * @param y cast=(gint)
	 */
	public static final native void gdk_device_warp(long device, long screen, int x, int y);
	/**
	 * @method flags=dynamic
	 * @param device cast=(GdkDevice *)
	 * @param win_x cast=(gint *)
	 * @param win_y cast=(gint *)
	 */
	public static final native long gdk_device_get_window_at_position(long device, int[] win_x, int[] win_y);

	/* GdkDevice [GTK4 only] */
	/**
	 * @method flags=dynamic
	 * @param device cast=(GdkDevice *)
	 * @param win_x cast=(double *)
	 * @param win_y cast=(double *)
	 */
	public static final native long gdk_device_get_surface_at_position(long device, double[] win_x, double[] win_y);


	/**
	 * @param window cast=(GdkWindow *)
	 * @param property cast=(GdkAtom)
	 * @param type cast=(GdkAtom)
	 * @param actual_property_type cast=(GdkAtom *)
	 * @param actual_format cast=(gint *)
	 * @param actual_length cast=(gint *)
	 * @param data cast=(guchar **)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native boolean gdk_property_get(long window, long property, long type, long offset, long length, int pdelete, long [] actual_property_type, int[] actual_format, int[] actual_length, long [] data);
	/**
	 * @param surface cast=(cairo_surface_t *)
	 */
	public static final native long gdk_cairo_region_create_from_surface(long surface);
	/**
	 * @param rgba cast=(GdkRGBA *)
	 */
	public static final native long gdk_rgba_to_string(GdkRGBA rgba);
	/**
	 * @param rgba cast=(GdkRGBA *)
	 */
	public static final native void gdk_rgba_free(long rgba);
	/**
	 * @param rgba cast=(GdkRGBA *)
	 * @param property cast=(const gchar *)
	 */
	public static final native long gdk_rgba_parse(GdkRGBA rgba, byte[] property);
	/**
	 * @param clipboard cast=(GdkClipboard *)
	 * @param provider cast=(GdkContentProvider *)
	 */
	/* [GTK4 only, if-def'd in os.h] */
	public static final native long gdk_clipboard_set_content(long clipboard, long provider);


	/* GdkDisplay */
	/** @param display cast=(GdkDisplay *) */
	public static final native void gdk_display_beep(long display);
	/**
	 * @param display cast=(GdkDisplay *)
	 * @param event cast=(GdkEvent *)
	 */
	public static final native void gdk_display_put_event(long display, long event);
	public static final native long gdk_display_get_default();
	/** @method flags=dynamic */
	public static final native long gdk_display_get_default_seat(long display);

	/* GdkDisplay [GTK3 only] */
	/**
	 * @method flags=dynamic
	 * @param display cast=(GdkDisplay *)
	 */
	public static final native int gdk_display_get_n_monitors(long display);
	/**
	 * @method flags=dynamic
	 * @param display cast=(GdkDisplay *)
	 */
	public static final native long gdk_display_get_primary_monitor(long display);
	/**
	 * @method flags=dynamic
	 * @param display cast=(GdkDisplay *)
	 */
	public static final native long gdk_display_get_monitor_at_point(long display, int x, int y);
	/**
	 * @method flags=dynamic
	 * @param display cast=(GdkDisplay *)
	 */
	public static final native long gdk_display_get_monitor(long display, int monitor_num);
	/**
	 * @method flags=dynamic
	 * @param display cast=(GdkDisplay *)
	 */
	public static final native boolean gdk_display_supports_cursor_color(long display);
	/**
	 * @method flags=dynamic
	 * @param display cast=(GdkDisplay *)
	 */
	public static final native long gdk_display_get_default_group(long display);
	/**
	 * @method flags=dynamic
	 * @param display cast=(GdkDisplay *)
	 * @param window cast=(GdkWindow *)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native long gdk_display_get_monitor_at_window(long display, long window);

	/* GdkDisplay [GTK4 only] */
	/**
	 * @method flags=dynamic
	 * @param display cast=(GdkDisplay *)
	 */
	public static final native long gdk_display_get_monitors(long display);
	/**
	 * @method flags=dynamic
	 * @param display cast=(GdkDisplay *)
	 * @param keyval cast=(guint)
	 * @param keys cast=(GdkKeymapKey**)
	 * @param n_keys cast=(gint*)
	 */
	public static final native boolean gdk_display_map_keyval(long display, int keyval, long[] keys, int[] n_keys);
	/**
	 * @method flags=dynamic
	 * @param display cast=(GdkDisplay *)
	 */
	public static final native boolean gdk_display_is_composited(long display);
	/**
	 * @method flags=dynamic
	 * @param display cast=(GdkDisplay *)
	 */
	public static final native long gdk_display_get_clipboard(long display);
	/**
	 * @method flags=dynamic
	 * @param display cast=(GdkDisplay *)
	 */
	public static final native long gdk_display_get_primary_clipboard(long display);
	/**
	 * @param display cast=(GdkDisplay *)
	 * @param surface cast=(GdkSurface *)
	 */
	/* [GTK4 only, if-def'd in os.h] */
	public static final native long gdk_display_get_monitor_at_surface(long display, long surface);


	/* GdkMonitor */
	/** @method flags=dynamic */
	public static final native int gdk_monitor_get_scale_factor(long window);
	/**
	 * @method flags=dynamic
	 * @param dest flags=no_in
	 */
	public static final native void gdk_monitor_get_geometry(long monitor, GdkRectangle dest);

	/* GdkMonitor [GTK3 only] */
	/**
	 * @method flags=dynamic
	 * @param dest flags=no_in
	 */
	public static final native void gdk_monitor_get_workarea(long monitor, GdkRectangle dest);


	/* GdkScreen [GTK3 only] */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native long gdk_screen_get_default();
	/** @param screen cast=(GdkScreen *) */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native double gdk_screen_get_resolution(long screen);
	/** @param screen cast=(GdkScreen *) */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native boolean gdk_screen_is_composited(long screen);
	/** @param screen cast=(GdkScreen *) */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native long gdk_screen_get_system_visual(long screen);

	/* GdkScreen [GTK3 only, if-def'd in os.h; 3.22 deprecated, replaced] */
	/**
	 * @method flags=dynamic
	 */
	public static final native int gdk_screen_height();
	/**
	 * @method flags=dynamic
	 */
	public static final native int gdk_screen_width();
	/**
	 * @method flags=dynamic
	 */
	public static final native int gdk_seat_grab(long seat, long window, int capabilities, boolean owner_events, long cursor, long event, long func, long func_data);
	/**
	 * @method flags=dynamic
	 */
	public static final native void gdk_seat_ungrab(long seat);
	/**
	 * @method flags=dynamic
	 */
	public static final native long gdk_seat_get_pointer(long seat);
	/**
	 * @method flags=dynamic
	 */
	public static final native long gdk_seat_get_keyboard(long seat);
	/**
	 * @method flags=dynamic
	 * @param program_class cast=(const char *)
	 */
	/* [GTK3 only] */
	public static final native void gdk_set_program_class(byte[] program_class);
	/** @param atom cast=(GdkAtom) */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gdk_selection_owner_get(long atom);
	/**
	 * @param owner cast=(GdkWindow *)
	 * @param atom cast=(GdkAtom)
	 * @param time cast=(guint32)
	 * @param send_event cast=(gboolean)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gdk_selection_owner_set(long owner, long atom, int time, boolean send_event);
	/**
	 * @param display cast=(GdkDisplay *)
	 * @param str cast=(const gchar*)
	 * @param encoding cast=(GdkAtom *)
	 * @param format cast=(gint *)
	 * @param ctext cast=(guchar **)
	 * @param length cast=(gint *)
	 */
	public static final native boolean gdk_x11_display_utf8_to_compound_text(long display, byte[] str, long [] encoding, int[] format, long [] ctext, int[] length);
	/**
	 * @method flags=dynamic
	 * @param str cast=(const gchar *)
	 */
	/* [GTK3 only] */
	public static final native long gdk_utf8_to_string_target(byte[] str);
	/**
	 * @param display cast=(GdkDisplay *)
	 * @param encoding cast=(GdkAtom)
	 * @param text cast=(guchar *)
	 * @param list cast=(gchar ***)
	 */
	public static final native int gdk_text_property_to_utf8_list_for_display(long display, long encoding, int format, long text, int length,  long [] list);


	/* GdkTexture [GTK4 only] */
	/** @param pixbuf cast=(GdkPixbuf *) */
	public static final native long gdk_texture_new_for_pixbuf(long pixbuf);
	/**
	 * @param file cast=(GFile *)
	 * @param error cast=(GError **)
	 */
	public static final native long gdk_texture_new_from_file(long file, long error);
	/**
	 * @method flags=ignore_deprecations
	 * @param function cast=(GSourceFunc)
	 * @param data cast=(gpointer)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native int gdk_threads_add_idle(long function, long data);
	/**
	 * @method flags=ignore_deprecations
	 * @param function cast=(GSourceFunc)
	 * @param data cast=(gpointer)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native int gdk_threads_add_timeout (int interval, long function, long data);
	/** @method flags=ignore_deprecations */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gdk_threads_enter ();
	/** @method flags=ignore_deprecations */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gdk_threads_init ();
	/** @method flags=ignore_deprecations */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gdk_threads_leave ();
	public static final native  int gdk_unicode_to_keyval(int wc);
	/**
	 * @param visual cast=(GdkVisual *)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native int gdk_visual_get_depth(long visual);
	/**
	 * @param window cast=(GdkWindow *)
	 * @param content cast=(cairo_content_t)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native long gdk_window_create_similar_surface(long window, int content, int width, int height);
	/**
	 * @param window cast=(GdkSurface *)
	 * @param content cast=(cairo_content_t)
	 */
	/* [GTK4 only, if-def'd in os.h] */
	public static final native long gdk_surface_create_similar_surface(long window, int content, int width, int height);
	/** @param window cast=(GdkWindow *) */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gdk_window_destroy(long window);
	/** @param window cast=(GdkSurface *) */
	/* [GTK4 only, if-def'd in os.h] */
	public static final native void gdk_surface_destroy(long window);
	/** @param window cast=(GdkWindow *) */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native long gdk_window_get_children(long window);
	/** @param window cast=(GdkWindow *) */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native int gdk_window_get_events(long window);
	/** @param window cast=(GdkWindow *) */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gdk_window_focus(long window, int timestamp);
	/**
	 * @param window cast=(GdkWindow *)
	 * @param rect cast=(GdkRectangle *),flags=no_in
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gdk_window_get_frame_extents(long window, GdkRectangle rect);
	/**
	 * @param window cast=(GdkWindow *)
	 * @param x cast=(gint *)
	 * @param y cast=(gint *)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native int gdk_window_get_origin(long window, int[] x, int[] y);
	/**
	 * @param window cast=(GdkWindow *)
	 * @param device cast=(GdkDevice *)
	 * @param x cast=(gint *)
	 * @param y cast=(gint *)
	 * @param mask cast=(GdkModifierType *)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native long gdk_window_get_device_position(long window, long device, int[] x, int[] y, int[] mask);
	/**
	 * @param surface cast=(GdkSurface *)
	 * @param device cast=(GdkDevice *)
	 * @param x cast=(double *)
	 * @param y cast=(double *)
	 * @param mask cast=(GdkModifierType *)
	 */
	/* [GTK4 only, if-def'd in os.h] */
	public static final native void gdk_surface_get_device_position(long surface, long device, double[] x, double[] y, int[] mask);
	/** @param window cast=(GdkWindow *) */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native long gdk_window_get_parent(long window);
	/**
	 * @param window cast=(GdkWindow *)
	 * @param x cast=(gint *)
	 * @param y cast=(gint *)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gdk_window_get_root_origin(long window, int[] x, int[] y);
	/**
	 * @param window cast=(GdkWindow *)
	 * @param data cast=(gpointer *)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gdk_window_get_user_data(long window, long [] data);
	/** @param window cast=(GdkWindow *) */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gdk_window_hide(long window);
	/** @param surface cast=(GdkSurface *) */
	/* [GTK4 only, if-def'd in os.h] */
	public static final native void gdk_surface_hide(long surface);
	/**
	 * @param window cast=(GdkWindow *)
	 * @param rectangle cast=(GdkRectangle *),flags=no_out
	 * @param invalidate_children cast=(gboolean)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gdk_window_invalidate_rect(long window, GdkRectangle rectangle, boolean invalidate_children);
	/**
	 * @param window cast=(GdkWindow *)
	 * @param region cast=(const cairo_region_t *)
	 * @param invalidate_children cast=(gboolean)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gdk_window_invalidate_region(long window, long region, boolean invalidate_children);
	/** @param window cast=(GdkWindow *) */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gdk_window_move(long window, int x, int y);
	/** @param window cast=(GdkWindow *) */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gdk_window_move_resize(long window, int x, int y, int width, int height);
	/**
	 * @method flags=dynamic
	 * @param display cast=(GdkDisplay *)
	 */
	/* [GTK4 only, if-def'd in os.h] */
	public static final native long gdk_surface_new_toplevel(long display);
	/** @param parent cast=(GdkSurface *) */
	/* [GTK4 only, if-def'd in os.h] */
	public static final native long gdk_surface_new_popup(long parent, boolean autohide);
	/** @param window cast=(GdkWindow *) */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gdk_window_lower(long window);
	/** @param window cast=(GdkWindow *) */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gdk_window_raise(long window);
	/**
	 * @method flags=dynamic
	 * @param toplevel cast=(GdkToplevel *)
	 * @param layout cast=(GdkToplevelLayout *)
	 * */
	/* [GTK4 only] */
	public static final native boolean gdk_toplevel_present(long toplevel, int width, int height, long layout);
	/** @param window cast=(GdkWindow *) */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gdk_window_resize(long window, int width, int height);
	/**
	 * @param window cast=(GdkWindow *)
	 * @param sibling cast=(GdkWindow *)
	 * @param above cast=(gboolean)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gdk_window_restack(long window, long sibling, boolean above);
	/**
	 * @param window cast=(GdkWindow *)
	 * @param cursor cast=(GdkCursor *)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gdk_window_set_cursor(long window, long cursor);
	/**
	 * @param surface cast=(GdkSurface *)
	 * @param cursor cast=(GdkCursor *)
	 */
	/* [GTK4 only, if-def'd in os.h] */
	public static final native void gdk_surface_set_cursor(long surface, long cursor);
	/**
	 * @param window cast=(GdkWindow *)
	 * @param decorations cast=(GdkWMDecoration)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gdk_window_set_decorations(long window, int decorations);
	/**
	 * @param window cast=(GdkWindow *)
	 * @param functions cast=(GdkWMFunction)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gdk_window_set_functions(long window, int functions);
	/** @param window cast=(GdkWindow *) */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gdk_window_set_events(long window, int event_mask);
	/**
	 * @param window cast=(GdkWindow *)
	 * @param override_redirect cast=(gboolean)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gdk_window_set_override_redirect(long window, boolean override_redirect);
	/**
	 * @param window cast=(GdkWindow *)
	 * @param user_data cast=(gpointer)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gdk_window_set_user_data(long window, long user_data);
	/** @param window cast=(GdkWindow *) */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gdk_window_show(long window);
	/** @param window cast=(GdkWindow *) */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gdk_window_show_unraised(long window);


	/* GdkToplevelLayout [GTK4 only] */
	/** @method flags=dynamic */
	public static final native long gdk_toplevel_layout_new(int min_width, int min_height);

	/* GdkPopup [GTK4 only] */
	/** @param popup cast=(GdkPopup *) */
	public static final native long gdk_popup_get_parent(long popup);
	/**
	 * @param popup cast=(GdkPopup *)
	 * @param layout cast=(GdkPopupLayout *)
	 */
	public static final native boolean gdk_popup_present(long popup, int width, int height, long layout);

	/* GdkPopupLayout [GTK4 only] */
	/**
	 * @param anchor_rect cast=(const GdkRectangle *)
	 * @param rect_anchor cast=(GdkGravity)
	 * @param surface_anchor cast=(GdkGravity)
	 */
	public static final native long gdk_popup_layout_new(GdkRectangle anchor_rect, int rect_anchor, int surface_anchor);

	public static long gdk_get_pointer (long display) {
		long default_seat = GDK.gdk_display_get_default_seat(display);
		return GDK.gdk_seat_get_pointer(default_seat);
	}
}
