package org.eclipse.swt.internal.gtk;

/*
 * Copyright (c) IBM Corp. 2000, 2001.  All rights reserved.
 *
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 */
 
import org.eclipse.swt.internal.Library;

public class OS {
	static {
		Library.loadLibrary("swt-pi");
	}
	
		
	public static final int GDK_NONE = 0;
	
	/* GIF-like animation overlay modes for frames */
	public final static int GDK_PIXBUF_FRAME_RETAIN = 0;
	public final static int GDK_PIXBUF_FRAME_DISPOSE = 1;
	public final static int GDK_PIXBUF_FRAME_REVERT = 2;
	
	/* Alpha compositing mode */
	public final static int GDK_PIXBUF_ALPHA_BILEVEL = 0;
	public final static int GDK_PIXBUF_ALPHA_FULL = 1;

	/* Interpolation modes */
	public final static int GDK_INTERP_NEAREST = 0;
	public final static int GDK_INTERP_TILES = 1;
	public final static int GDK_INTERP_BILINEAR = 2;
	public final static int GDK_INTERP_HYPER = 3;

	/* GdkGC set values mask */
	public static final int GDK_GC_FOREGROUND    = 1 << 0;
	public static final int GDK_GC_BACKGROUND    = 1 << 1;
	public static final int GDK_GC_FONT          = 1 << 2;
	public static final int GDK_GC_FUNCTION      = 1 << 3;
	public static final int GDK_GC_FILL          = 1 << 4;
	public static final int GDK_GC_TILE          = 1 << 5;
	public static final int GDK_GC_STIPPLE       = 1 << 6;
	public static final int GDK_GC_CLIP_MASK     = 1 << 7;
	public static final int GDK_GC_SUBWINDOW     = 1 << 8;
	public static final int GDK_GC_TS_X_ORIGIN   = 1 << 9;
	public static final int GDK_GC_TS_Y_ORIGIN   = 1 << 10;
	public static final int GDK_GC_CLIP_X_ORIGIN = 1 << 11;
	public static final int GDK_GC_CLIP_Y_ORIGIN = 1 << 12;
	public static final int GDK_GC_EXPOSURES     = 1 << 13;
	public static final int GDK_GC_LINE_WIDTH    = 1 << 14;
	public static final int GDK_GC_LINE_STYLE    = 1 << 15;
	public static final int GDK_GC_CAP_STYLE     = 1 << 16;
	public static final int GDK_GC_JOIN_STYLE    = 1 << 17;
	
	/* GdkImage byte order */
	public static final int GDK_LSB_FIRST = 0;
	public static final int GDK_MSB_FIRST = 1;

	/* For Display.KeyTable: */
	/* Keyboard and mouse masks */
	public static final int GDK_Alt_L = 0xFFE9;
	public static final int GDK_Alt_R = 0xFFEA;
	public static final int GDK_Shift_L = 0xFFE1;
	public static final int GDK_Shift_R = 0xFFE2;
	public static final int GDK_Control_L = 0xFFE3;
	public static final int GDK_Control_R = 0xFFE4;
	/* Non-numeric keypad constants */
	public static final int GDK_Up = 0xFF52;
	public static final int GDK_Down = 0xFF54;
	public static final int GDK_Left = 0xFF51;
	public static final int GDK_Right = 0xFF53;
	public static final int GDK_Page_Up = 0xFF55;
	public static final int GDK_Page_Down = 0xFF56;
	public static final int GDK_Home = 0xFF50;
	public static final int GDK_End = 0xFF57;
	public static final int GDK_Insert = 0xFF63;
	public static final int GDK_Delete = 0xFFFF;
	public static final int GDK_Return = 0xFF0D;
	public static final int GDK_Escape = 0xFF1B;
	public static final int GDK_Cancel = 0xFF69;
	public static final int GDK_Tab = 0xFF09;
	/* Functions Keys */
	public static final int GDK_F1 = 0xFFBE;
	public static final int GDK_F2 = 0xFFBF;
	public static final int GDK_F3 = 0xFFC0;
	public static final int GDK_F4 = 0xFFC1;
	public static final int GDK_F5 = 0xFFC2;
	public static final int GDK_F6 = 0xFFC3;
	public static final int GDK_F7 = 0xFFC4;
	public static final int GDK_F8 = 0xFFC5;
	public static final int GDK_F9 = 0xFFC6;
	public static final int GDK_F10 = 0xFFC7;
	public static final int GDK_F11 = 0xFFC8;
	public static final int GDK_F12 = 0xFFC9;
	/* Numeric Keypad */
	public static final int GDK_KP_Add = 0xFFAB;
	public static final int GDK_KP_Subtract = 0xFFAD;
	public static final int GDK_KP_Multiply = 0xFFAA;
	public static final int GDK_KP_Divide = 0xFFAF;
	public static final int GDK_KP_Enter = 0xFF8D;
	public static final int GDK_KP_Decimal = 0xFFAE;
	public static final int GDK_KP_0 = 0xFFB0;
	public static final int GDK_KP_1 = 0xFFB1;
	public static final int GDK_KP_2 = 0xFFB2;
	public static final int GDK_KP_3 = 0xFFB3;
	public static final int GDK_KP_4 = 0xFFB4;
	public static final int GDK_KP_5 = 0xFFB5;
	public static final int GDK_KP_6 = 0xFFB6;
	public static final int GDK_KP_7 = 0xFFB7;
	public static final int GDK_KP_8 = 0xFFB8;
	public static final int GDK_KP_9 = 0xFFB9;

	public static final int GDK_FONT_FONT = 0;
	public static final int GDK_FONT_FONTSET = 1;
	public static final int GDK_COPY = 0;
	public static final int GDK_INVERT = 1;
	public static final int GDK_XOR = 2;
	public static final int GDK_AND = 4;
	public static final int GDK_STIPPLED = 2;
	public static final int GDK_LINE_SOLID = 0;
	public static final int GDK_LINE_ON_OFF_DASH = 1;
	public static final int GDK_LINE_DOUBLE_DASH = 2;
	public static final int GDK_CAP_BUTT = 1;
	public static final int GDK_JOIN_MITER = 0;
	public static final int GDK_X_CURSOR = 0;
	public static final int GDK_BOTTOM_LEFT_CORNER = 12;
	public static final int GDK_BOTTOM_RIGHT_CORNER = 14;
	public static final int GDK_BOTTOM_SIDE = 16;
	public static final int GDK_CROSS = 30;
	public static final int GDK_DIAMOND_CROSS = 36;
	public static final int GDK_DOUBLE_ARROW = 42;
	public static final int GDK_HAND1 = 58;
	public static final int GDK_LEFT_PTR = 68;
	public static final int GDK_LEFT_SIDE = 70;
	public static final int GDK_QUESTION_ARROW = 92;
	public static final int GDK_RIGHT_PTR = 94;
	public static final int GDK_RIGHT_SIDE = 96;
	public static final int GDK_SB_H_DOUBLE_ARROW = 108;
	public static final int GDK_SB_UP_ARROW = 114;
	public static final int GDK_SB_V_DOUBLE_ARROW = 116;
	public static final int GDK_SIZING = 120;
	public static final int GDK_TOP_LEFT_CORNER = 134;
	public static final int GDK_TOP_RIGHT_CORNER = 136;
	public static final int GDK_TOP_SIDE = 138;
	public static final int GDK_WATCH = 150;
	public static final int GDK_XTERM = 152;
	public static final int GDK_CURSOR_IS_PIXMAP = -1;
	public static final int GDK_MOTION_NOTIFY = 3;
	public static final int GDK_BUTTON_PRESS = 4;
	public static final int GDK_2BUTTON_PRESS = 5;
	public static final int GDK_3BUTTON_PRESS = 6;
	public static final int GDK_BUTTON_RELEASE = 7;
	public static final int GDK_KEY_PRESS = 8;
	public static final int GDK_KEY_RELEASE = 9;
	public static final int GDK_FOCUS_CHANGE = 12;
	public static final int GDK_NO_EXPOSE = 30;

	/* The values for the GdkModifierType constants are specified in the
	 * documentation, therefore there is no need to get them from the OS.
	 */
	public static final int GDK_SHIFT_MASK    = 1 << 0;
	public static final int GDK_LOCK_MASK	    = 1 << 1;
	public static final int GDK_CONTROL_MASK  = 1 << 2;
	public static final int GDK_MOD1_MASK	    = 1 << 3;
	public static final int GDK_MOD2_MASK	    = 1 << 4;
	public static final int GDK_MOD3_MASK	    = 1 << 5;
	public static final int GDK_MOD4_MASK	    = 1 << 6;
	public static final int GDK_MOD5_MASK	    = 1 << 7;
	public static final int GDK_BUTTON1_MASK  = 1 << 8;
	public static final int GDK_BUTTON2_MASK  = 1 << 9;
	public static final int GDK_BUTTON3_MASK  = 1 << 10;
	public static final int GDK_BUTTON4_MASK  = 1 << 11;
	public static final int GDK_BUTTON5_MASK  = 1 << 12;
	public static final int GDK_RELEASE_MASK  = 1 << 30;
	public static final int GDK_MODIFIER_MASK = GDK_RELEASE_MASK | 0x1fff;

	/* The values for the GdkEventMask constants are specified in the
	 * documentation, therefore there is no need to get them from the OS.
	 */
	public static final int GDK_EXPOSURE_MASK       	= 1 << 1;
	public static final int GDK_POINTER_MOTION_MASK 	= 1 << 2;
	public static final int GDK_POINTER_MOTION_HINT_MASK = 1 << 3;
	public static final int GDK_BUTTON_MOTION_MASK    	= 1 << 4;
	public static final int GDK_BUTTON1_MOTION_MASK    	= 1 << 5;
	public static final int GDK_BUTTON2_MOTION_MASK    	= 1 << 6;
	public static final int GDK_BUTTON3_MOTION_MASK    	= 1 << 7;
	public static final int GDK_BUTTON_PRESS_MASK     	= 1 << 8;
	public static final int GDK_BUTTON_RELEASE_MASK     = 1 << 9;
	public static final int GDK_KEY_PRESS_MASK       	= 1 << 10;
	public static final int GDK_KEY_RELEASE_MASK     	= 1 << 11;
	public static final int GDK_ENTER_NOTIFY_MASK    	= 1 << 12;
	public static final int GDK_LEAVE_NOTIFY_MASK    	= 1 << 13;
	public static final int GDK_FOCUS_CHANGE_MASK 		= 1 << 14;
	public static final int GDK_STRUCTURE_MASK			= 1 << 15;
	public static final int GDK_PROPERTY_CHANGE_MASK	= 1 << 16;
	public static final int GDK_VISIBILITY_NOTIFY_MASK	= 1 << 17;
	public static final int GDK_PROXIMITY_IN_MASK		= 1 << 18;
	public static final int GDK_PROXIMITY_OUT_MASK		= 1 << 19;
	public static final int GDK_SUBSTRUCTURE_MASK		= 1 << 20;
	public static final int GDK_SCROLL_MASK           	= 1 << 21;
	public static final int GDK_ALL_EVENTS_MASK			= 0x3FFFFE;
	
	public static final int GDK_INCLUDE_INFERIORS = 1;
	public static final int GDK_DECOR_ALL = 1 << 0;
	public static final int GDK_DECOR_BORDER = 1 << 1;
	public static final int GDK_DECOR_RESIZEH = 1 << 2;
	public static final int GDK_DECOR_TITLE = 1 << 3;
	public static final int GDK_DECOR_MENU = 1 << 4;
	public static final int GDK_DECOR_MINIMIZE = 1 << 5;
	public static final int GDK_DECOR_MAXIMIZE = 1 << 6;
	public static final int GDK_OVERLAP_RECTANGLE_IN = 0;
	public static final int GDK_OVERLAP_RECTANGLE_OUT = 1;
	public static final int GDK_OVERLAP_RECTANGLE_PART = 2;
	public static final int GDK_RGB_DITHER_NONE = 0;
	public static final int GDK_RGB_DITHER_NORMAL = 1;
	public static final int GDK_RGB_DITHER_MAX = 2;
	public static final int GTK_ARROW_UP = 0;
	public static final int GTK_ARROW_DOWN = 1;
	public static final int GTK_ARROW_LEFT = 2;
	public static final int GTK_ARROW_RIGHT = 3;
	public static final int GTK_JUSTIFY_LEFT = 0;
	public static final int GTK_JUSTIFY_RIGHT = 1;
	public static final int GTK_JUSTIFY_CENTER = 2;
	public static final int GTK_JUSTIFY_FILL = 3;
	public static final int GTK_ORIENTATION_HORIZONTAL = 0;
	public static final int GTK_ORIENTATION_VERTICAL = 1;
	public static final int GTK_POLICY_ALWAYS = 0;
	public static final int GTK_POLICY_AUTOMATIC = 1; // can't fix now
	public static final int GTK_POLICY_NEVER = 2;
	public static final int GTK_RELIEF_NORMAL = 0;
//	public static final int GTK_RELIEF_HALF = 1;
	public static final int GTK_RELIEF_NONE = 2;
	public static final int GTK_SELECTION_SINGLE = 0; // extra code in Table, can't fix now
	public static final int GTK_SELECTION_BROWSE = 1;
	public static final int GTK_SELECTION_MULTIPLE = 2;
	public static final int GTK_SELECTION_EXTENDED = 3;
	public static final int GTK_SHADOW_NONE = 0;
	public static final int GTK_SHADOW_IN = 1;
	public static final int GTK_SHADOW_OUT = 2;
	public static final int GTK_SHADOW_ETCHED_IN = 3;
	public static final int GTK_SHADOW_ETCHED_OUT = 4;
	public static final int GTK_STATE_NORMAL = 0;
	public static final int GTK_STATE_ACTIVE = 1;
	public static final int GTK_STATE_PRELIGHT = 2;
	public static final int GTK_STATE_SELECTED = 3;
	public static final int GTK_STATE_INSENSITIVE = 4;
	public static final int GTK_TOP_BOTTOM = 0;
	public static final int GTK_LEFT_RIGHT = 1;
	public static final int GTK_TOOLBAR_ICONS = 0;
	public static final int GTK_TOOLBAR_TEXT = 1;
	public static final int GTK_TOOLBAR_BOTH = 2;
	public static final int GTK_VISIBILITY_NONE = 0;
	public static final int GTK_VISIBILITY_PARTIAL = 1;
	public static final int GTK_VISIBILITY_FULL = 2;
	public static final int GTK_WINDOW_TOPLEVEL = 0;
	public static final int GTK_WINDOW_POPUP = 1;
	public static final int GTK_ACCEL_VISIBLE = 1 << 0;
	public static final int GTK_NO_WINDOW = 1 << 5;
	public static final int GTK_MAPPED = 1 << 7;
	public static final int GTK_VISIBLE = 1 << 8;
	public static final int GTK_SENSITIVE = 1 << 9;
	public static final int GTK_CAN_FOCUS = 1 << 11;
	public static final int GTK_HAS_FOCUS = 1 << 12;
	public static final int GTK_CAN_DEFAULT = 1 << 13;
	public static final int GTK_CLIST_SHOW_TITLES         = 1 <<  2;
	public static final int GTK_PROGRESS_CONTINUOUS = 0;
	public static final int GTK_PROGRESS_DISCRETE = 1;
	public static final int GTK_PROGRESS_LEFT_TO_RIGHT = 0;
	public static final int GTK_PROGRESS_RIGHT_TO_LEFT = 1;
	public static final int GTK_PROGRESS_BOTTOM_TO_TOP = 2;
	public static final int GTK_PROGRESS_TOP_TO_BOTTOM = 3;
	public static final int GTK_RECEIVES_DEFAULT = 1<<20;
	
	/* The values for the GdkGrabStatus constants are specified in
	 * the documentation, therefore there is no need to get them from the OS.
	 */
	public static final int GDK_GRAB_SUCCESS = 0;
	public static final int GDK_GRAB_ALREADY_GRABBED = 1;
	public static final int GDK_GRAB_INVALID_TIME = 2;
	public static final int GDK_GRAB_NOT_VIEWABLE = 3;
	public static final int GDK_GRAB_FROZEN = 4;
	

public static final native int GTK_TOOLBAR_CHILD_SPACE();
public static final native int GTK_TOOLBAR_CHILD_BUTTON();
public static final native int GTK_TOOLBAR_CHILD_TOGGLEBUTTON();
public static final native int GTK_TOOLBAR_CHILD_RADIOBUTTON();
public static final native int GTK_TOOLBAR_CHILD_WIDGET();

	public static final int G_LOG_FLAG_RECURSION = 1 << 0;
	public static final int G_LOG_FLAG_FATAL = 1 << 1;
//	public static final int G_LOG_LEVEL_ERROR = 1 << 2;
//	public static final int G_LOG_LEVEL_CRITICAL = 1 << 3;
//	public static final int G_LOG_LEVEL_WARNING = 1 << 4;
//	public static final int G_LOG_LEVEL_MESSAGE = 1 << 5;
//	public static final int G_LOG_LEVEL_INFO = 1 << 6;
//	public static final int G_LOG_LEVEL_DEBUG = 1 << 7;
	public static final int G_LOG_LEVEL_MASK = ~(G_LOG_FLAG_RECURSION | G_LOG_FLAG_FATAL);


/*
 *         Native methods.
 */

/* GLIB */

public static final native void g_signal_connect (int handle, String eventName, int proc, int swtEvent);
public static final native void g_signal_connect_after (int handle, String eventName, int proc, int swtEvent);


/*
 * Main loop
 */

public static final native int gtk_set_locale();
public static final native boolean gtk_init_check(int[] argc, int[] argv);




/* GtkWidget */
public static final native int GTK_WIDGET_FLAGS(int wid);
public static final native void GTK_WIDGET_SET_FLAGS(int wid,int flag);
public static final native void GTK_WIDGET_UNSET_FLAGS(int wid,int flag);
public static final native int GTK_WIDGET_WINDOW(int wid);
public static final native int GTK_WIDGET_PARENT(int wid);
public static final native boolean GTK_WIDGET_NO_WINDOW(int wid);
public static final native boolean GTK_WIDGET_SENSITIVE(int wid);
public static final native boolean GTK_WIDGET_IS_SENSITIVE(int wid);
public static final native boolean GTK_WIDGET_TOPLEVEL(int wid);
public static final native boolean GTK_WIDGET_REALISED(int wid);
public static final native boolean GTK_WIDGET_MAPPED(int wid);
public static final native boolean GTK_WIDGET_VISIBLE(int wid);
public static final native boolean GTK_WIDGET_DRAWABLE(int wid);
public static final native boolean GTK_WIDGET_CAN_FOCUS(int wid);
public static final native boolean GTK_WIDGET_HAS_FOCUS(int wid);
public static final native boolean GTK_WIDGET_HAS_GRAB(int wid);


/* GtkWindow and Dialogs */

public static final native int GTK_FONT_SELECTION_DIALOG_OK_BUTTON(int handle);
public static final native int GTK_FONT_SELECTION_DIALOG_CANCEL_BUTTON(int handle);
public static final native int GTK_FILE_SELECTION_OK_BUTTON(int handle);
public static final native int GTK_FILE_SELECTION_CANCEL_BUTTON(int handle);
public static final native int GTK_COLOR_SELECTION_OK_BUTTON(int handle);
public static final native int GTK_COLOR_SELECTION_CANCEL_BUTTON(int handle);
public static final native int GTK_COLOR_SELECTION_HELP_BUTTON(int handle);
public static final native int GTK_COLOR_SELECTION_COLORSEL(int handle);
public static final native int GTK_DIALOG_ACTION_AREA(int handle);
public static final native int GTK_DIALOG_VBOX(int handle);
public static final native void gtk_window_get_position(int handle, int[] x, int[] y);
public static final native void gtk_window_get_size(int handle, int[] x, int[] y);
public static final native void gtk_window_move(int handle, int x, int y);
public static final native void gtk_window_resize(int handle, int x, int y);
public static final native void gtk_window_iconify(int handle);
public static final native void gtk_window_deiconify(int handle);
public static final native void gtk_window_maximize(int handle);
public static final native void gtk_window_unmaximize(int handle);



/* Menus */

public static final native boolean gtk_check_menu_item_get_active(int wid);
public static final native void gtk_check_menu_item_set_active(int wid, boolean active);

/* Containers */

//public static final native int eclipse_fixed_new();
//public static final native void eclipse_fixed_get_location(int fixed, int child, int[] loc);
//public static final native boolean eclipse_fixed_get_size(int fixed, int child, int[] sz);
//public static final native void eclipse_fixed_set_location(int fixed, int child, int x, int y);
//public static final native void eclipse_fixed_set_size(int fixed, int child, int width, int height);
//public static final native void eclipse_fixed_move_above(int fixed, int child, int sibling);
//public static final native void eclipse_fixed_move_below(int fixed, int child, int sibling);


/* GDK */

public static final native void gdk_rgb_init();
public static final native int gdk_font_from_description(int desc);



public static final native void gtk_signal_handler_block_by_data(int object, int data);
public static final native void gtk_signal_handler_unblock_by_data(int object, int data);
public static final native int gtk_object_get_data_by_id(int object, int data_id);
public static final native void gtk_object_set_data_by_id(int object, int data_id, int data);
public static final native int g_quark_from_string(byte [] string);
public static final native void gtk_object_unref(int object);
public static final native void gtk_object_destroy(int object);
public static final native int GTK_WIDGET_TYPE(int wid);
public static final native int gtk_label_get_type();
public static final native int g_log_set_handler(byte [] log_domain, int log_levels, int log_func, int user_data);
public static final native void g_log_remove_handler(byte [] log_domain, int handler_id);
public static final native void g_log_default_handler(int log_domain, int log_levels, int message, int unused_data);
public static final native int gtk_clist_row_is_visible(int clist, int row);
public static final native void gtk_ctree_post_recursive_to_depth(int ctree, int node, int depth, int func, int data);
public static final native void gtk_draw_check(int style, int window, int state_type, int shadow_type, int x, int y, int width, int height);

public static final native void g_free(int mem);
public static final native int g_get_home_dir();
public static final native int g_list_length(int list);
public static final native int g_list_nth(int list, int n);
public static final native int g_list_nth_data(int list, int n);
public static final native void g_list_free(int list);
public static final native int g_malloc(int size);
public static final native void g_object_unref(int object);
public static final native int g_list_append(int list, int data);
public static final native int g_slist_length(int list);
public static final native int g_slist_nth(int list, int n);
public static final native int g_slist_nth_data(int list, int n);
public static final native int g_strdup(byte[] str);
public static final native int gdk_colormap_get_system();
public static final native void gdk_colors_free(int colormap, int[] pixels, int npixels, int planes);
public static final native boolean gdk_color_alloc(int colormap, GdkColor color);
public static final native boolean gdk_colormap_alloc_color(int colormap, GdkColor color, boolean writeable, boolean best_match);
public static final native void gdk_colormap_query_color(int colormap, int pixel, GdkColor result);
public static final native void gdk_colormap_free_colors(int colormap, GdkColor colors, int ncolors);
public static final native int gdk_cursor_new(int cursor_type);
public static final native int gdk_bitmap_create_from_data(int window, byte[] data, int width, int height);
public static final native int gdk_cursor_new_from_pixmap(int source, int mask, GdkColor fg, GdkColor bg, int x, int y);
public static final native void gdk_cursor_destroy(int cursor);
public static final native int GDK_FONT_ASCENT(int font);
public static final native int GDK_FONT_DESCENT(int font);
public static final native int gdk_font_load(byte[] font_name);
public static final native int gdk_font_ref(int font);
public static final native void gdk_font_unref(int font);
public static final native boolean gdk_font_equal(int fonta, int fontb);
public static final native int gdk_char_width(int font, byte character);
public static final native void gdk_gc_get_values(int gc, GdkGCValues values);
public static final native void gdk_gc_set_values(int gc, GdkGCValues values, int values_mask);
public static final native void gdk_gc_set_font(int gc, int font);
public static final native void gdk_gc_set_foreground(int gc, GdkColor color);
public static final native void gdk_gc_set_background(int gc, GdkColor color);
public static final native void gdk_gc_set_clip_mask(int gc, int mask);
public static final native void gdk_gc_set_clip_origin(int gc, int x, int y);
public static final native void gdk_gc_set_clip_rectangle(int gc, GdkRectangle rectangle);
public static final native void gdk_gc_set_clip_region(int gc, int region);
public static final native void gdk_gc_set_line_attributes(int gc, int line_width, int line_style, int cap_style, int join_style);
public static final native void gdk_gc_set_dashes(int gc, int dash_offset, byte[] dash_list, int n);
public static final native void gdk_gc_set_function(int gc, int function);
public static final native void gdk_draw_line(int drawable, int gc, int x1, int y1, int x2, int y2);
public static final native void gdk_draw_arc(int drawable, int gc, int filled, int x, int y, int width, int height, int angle1, int angle2);
public static final native void gdk_draw_drawable(int drawable, int gc, int src, int xsrc, int ysrc, int xdest, int ydest, int width, int height);
public static final native void gdk_draw_layout(int drawable, int gc, int x, int y, int layout);
public static final native void gdk_draw_layout_with_colors(int drawable, int gc, int x, int y, int layout, GdkColor foreground, GdkColor background);
public static final native void gdk_draw_rectangle(int drawable, int gc, int filled, int x, int y, int width, int height);
public static final native void gdk_draw_lines(int drawable, int gc, int[] points, int npoints);
public static final native void gdk_draw_polygon(int drawable, int gc, int filled, int[] points, int npoints);
public static final native void gdk_draw_string(int drawable, int font, int gc, int x, int y, byte[] string);
public static final native int gdk_gc_new(int window);
public static final native boolean gdk_color_white(int colormap, GdkColor color);
public static final native int gdk_image_get(int window, int x, int y, int width, int height);
public static final native int gdk_image_get_pixel(int image, int x, int y);
public static final native void gdk_gc_set_exposures(int gc, boolean exposures);


/*
 * GdkEvents
 */
public static final native int gdk_event_get_graphics_expose(int window);
public static final native void gdk_event_free(int event);
public static final native int gdk_event_get_time(int event);
public static final native boolean gdk_event_get_state(int event, int[] pmod);
public static final native boolean gdk_event_get_coords(int event, double[] px, double[] py);
public static final native boolean gdk_event_get_root_coords(int event, double[] px, double[] py);
public static final native int gdk_event_key_get_keyval(int event);
public static final native int gdk_event_key_get_length(int event);
public static final native int gdk_event_key_get_string(int event);
public static final native int gdk_event_button_get_button(int event);



public static final native void gdk_flush();
public static final native void gdk_beep();
public static final native void gdk_color_free(GdkColor color);
public static final native int GDK_ROOT_PARENT();
public static final native int GDK_EVENT_TYPE(int event);
public static final native void gdk_gc_set_stipple(int gc, int stipple);
public static final native void gdk_gc_set_subwindow(int gc, int mode);
public static final native void gdk_gc_set_fill(int gc, int fill);
public static final native int gdk_atom_intern(byte[] atom_name, int only_if_exists);
public static final native int gdk_event_get();
public static final native void gdk_region_get_clipbox(int region, GdkRectangle rectangle);
public static final native int gdk_region_new();
public static final native void gdk_region_union_with_rect(int region, GdkRectangle rect);
public static final native void gdk_region_subtract(int source1, int source2);
public static final native void gdk_region_intersect(int source1, int source2);
public static final native void gdk_region_offset(int region, int dx, int dy);
public static final native void gdk_region_union(int source1, int source2);
public static final native void gdk_region_destroy(int region);
public static final native int gdk_pixmap_new(int window, int width, int height, int depth);
public static final native boolean gdk_region_point_in(int region, int x, int y);
public static final native boolean gdk_region_empty(int region);
public static final native boolean gdk_region_equal(int region1, int region2);
public static final native int gdk_screen_height();
public static final native int gdk_screen_width();
public static final native int gdk_region_rect_in(int region, GdkRectangle rect);
public static final native int gdk_visual_get_system();
public static final native void gdk_string_extents(int font, byte[] string, int[] lbearing, int[] rbearing, int[] width, int[] ascent, int[] descent);
public static final native int gdk_string_height(int font, byte[] string);
public static final native int gdk_string_width(int font, byte[] string);
public static final native void gdk_window_copy_area(int window, int gc, int x, int y, int source_window, int source_x, int source_y, int width, int height);
//public static final native void gdk_window_clear_area(int window, int x, int y, int width, int height);
//public static final native void gdk_window_clear_area_e(int window, int x, int y, int width, int height);
public static final native void gdk_window_resize(int window, int width, int height);
public static final native void gdk_window_move  (int window, int x, int y);
public static final native int gdk_window_at_pointer(int[] win_x, int[] win_y);
public static final native int GDK_CURRENT_TIME();
public static final native int gdk_screen_width_mm();
public static final native int gdk_drawable_get_image(int drawable, int x, int y, int width, int height);
public static final native void gdk_drawable_get_size(int drawable, int[] width, int[] height);
public static final native int  gdk_drawable_get_depth(int drawable);
public static final native void gdk_window_raise(int window);
public static final native void gdk_window_lower(int window);
public static final native int gdk_window_get_origin(int window, int[] x, int[] y);
public static final native int gdk_window_get_pointer(int window, int[] x, int[] y, int[] mask);
public static final native void gdk_window_set_cursor(int window, int cursor);
public static final native void gdk_window_set_icon(int window, int icon_window, int pixmap, int mask);
public static final native void gdk_window_set_user_data(int window, int user_data);
public static final native void gdk_window_show(int window);
public static final native void gdk_window_get_user_data(int window, int[] data);
public static final native void gdk_window_set_decorations(int window, int decorations);
public static final native int gtk_adjustment_new(float value, float lower, float upper, float step_increment, float page_increment, float page_size);
public static final native void gtk_adjustment_changed(int adjustment);
public static final native void gtk_adjustment_set_value(int adjustment, float value);
public static final native void gtk_adjustment_value_changed(int adjustment);
public static final native int gtk_accel_group_new();
public static final native void gtk_accel_group_unref(int accel_group);
public static final native boolean gtk_accel_groups_activate(int accelGroup, int accelKey, int accelMods); // accelMods is one of GdkModifierType defined in gdk/gdktypes.h
public static final native int gtk_arrow_new(int arrow_type, int shadow_type);
public static final native void gtk_arrow_set(int arrow, int arrow_type, int shadow_type);
public static final native void gtk_box_pack_start(int box, int child, boolean expand, boolean fill, int padding);
public static final native void gtk_box_pack_end(int box, int child, boolean expand, boolean fill, int padding);
public static final native int gtk_button_new();
public static final native int gtk_check_button_new();
public static final native int gtk_check_version(int required_major, int required_minor, int required_micro);
public static final native int gtk_clist_append(int clist, int[] text);
public static final native void gtk_clist_clear(int clist);
public static final native int gtk_check_menu_item_new_with_label(byte[] label);
public static final native int gtk_button_new_with_label(byte[] label);
public static final native void gtk_clist_column_title_passive(int clist, int column);
public static final native void gtk_clist_column_titles_show(int clist);
public static final native void gtk_clist_column_titles_hide(int clist);
public static final native void gtk_clist_freeze(int clist);
public static final native void gtk_clist_get_pixtext(int clist, int row, int column, int[] text, byte[] spacing, int[] pixmap, int[] mask);
public static final native void gtk_check_menu_item_set_show_toggle(int menu_item, boolean always);
public static final native void gtk_clist_column_titles_passive(int clist);
public static final native int gtk_clist_insert(int clist, int row, int[] text);
public static final native int gtk_clist_new(int columns);
public static final native void gtk_clist_set_selection_mode(int clist, int mode);
public static final native int gtk_clist_get_text(int clist, int row, int column, int[] text);
public static final native void gtk_clist_remove(int clist, int row);
public static final native void gtk_clist_select_row(int clist, int row, int column);
public static final native void gtk_clist_select_all(int clist);
public static final native void gtk_clist_moveto(int clist, int row, int column, float row_align, float col_align);
public static final native void gtk_clist_set_column_title(int clist, int column, byte[] title);
public static final native void gtk_clist_set_column_visibility(int clist, int column, boolean visible);
public static final native int gtk_clist_get_selection_info(int clist, int x, int y, int[] row, int[] column);
public static final native void gtk_clist_set_column_justification(int clist, int column, int justification);
public static final native void gtk_clist_set_column_resizeable(int clist, int column, boolean resizeable);
public static final native void gtk_clist_set_column_width(int clist, int column, int width);
public static final native void gtk_clist_set_pixtext(int clist, int row, int column, byte[] text, byte spacing, int pixmap, int mask);
//public static final native void gtk_clist_set_pixmap(int clist, int row, int column, int pixmap, int mask);
public static final native void gtk_container_add(int container, int widget);
public static final native int gtk_container_children(int container);
public static final native int gtk_color_selection_dialog_new(byte[] title);
public static final native void gtk_color_selection_get_color(int colorsel, double[] color);
public static final native void gtk_color_selection_set_color(int colorsel, double[] color);
public static final native int gtk_combo_new();
public static final native void gtk_combo_set_popdown_strings(int combo, int strings);
public static final native void gtk_clist_set_shadow_type(int clist, int type);
public static final native void gtk_clist_unselect_row(int clist, int row, int column);
public static final native void gtk_clist_unselect_all(int clist);
public static final native void gtk_clist_set_text(int clist, int row, int column, byte[] text);
public static final native void gtk_clist_thaw(int clist);
public static final native void gtk_container_remove(int container, int widget);
public static final native int gtk_ctree_new(int columns, int tree_column);
public static final native int gtk_ctree_insert_node(int ctree, int parent, int sibling, int[] text, byte spacing, int pixmap_closed, int mask_closed, int pixmap_opened, int mask_opened, boolean is_leaf, boolean expanded);
public static final native int gtk_ctree_node_get_row_data(int ctree, int node);
public static final native void gtk_ctree_expand(int ctree, int node);
public static final native boolean gtk_ctree_is_hot_spot(int ctree, int x, int y);
public static final native boolean gtk_ctree_is_viewable(int ctree, int node);
public static final native int gtk_ctree_node_get_row_style(int ctree, int node);
public static final native void gtk_ctree_collapse(int ctree, int node);
public static final native int gtk_ctree_get_node_info(int ctree, int node, int[] text, byte[] spacing, int[] pixmap_closed, int[] mask_closed, int[] pixmap_opened, int[] mask_opened, boolean[] is_leaf, boolean[] expanded);
public static final native void gtk_ctree_node_set_row_data(int ctree, int node, int data);
public static final native void gtk_ctree_select(int ctree, int node);
public static final native int gtk_ctree_node_nth(int ctree, int row);
public static final native void gtk_ctree_select_recursive(int ctree, int node);
public static final native void gtk_ctree_unselect_recursive(int ctree, int node);
public static final native void gtk_ctree_post_recursive(int ctree, int node, int func, int data);
public static final native void gtk_ctree_remove_node(int ctree, int node);
public static final native int gtk_ctree_node_is_visible(int ctree, int node);
public static final native void gtk_ctree_node_moveto(int ctree, int node, int column, float row_align, float col_align);
public static final native void gtk_ctree_set_node_info(int ctree, int node, byte[] text, byte spacing, int pixmap_closed, int mask_closed, int pixmap_opened, int mask_opened, boolean is_leaf, boolean expanded);
public static final native int gtk_drawing_area_new();
public static final native int gtk_dialog_new();
public static final native int gtk_event_box_new();

public static final native int gtk_editable_get_selection_start(int editable);
public static final native int gtk_editable_get_selection_end(int editable);
public static final native boolean gtk_editable_get_editable(int editable);
public static final native void gtk_editable_copy_clipboard(int editable);
public static final native void gtk_editable_cut_clipboard(int editable);
public static final native void gtk_editable_paste_clipboard(int editable);
public static final native int gtk_editable_get_position(int editable);
public static final native void gtk_editable_set_position(int editable, int position);
public static final native void gtk_entry_set_editable(int entry, boolean editable);
public static final native int gtk_entry_get_text(int entry);
public static final native void gtk_entry_set_text(int entry, byte[] text);
public static final native void gtk_editable_select_region(int editable, int start, int end);
public static final native void gtk_editable_delete_text(int editable, int start_pos, int end_pos);
public static final native void gtk_editable_insert_text(int editable, byte[] new_text, int new_text_length, int[] position);
public static final native void gtk_drawing_area_size(int darea, int width, int height);
public static final native int gtk_events_pending();
public static final native int gtk_file_selection_get_filename(int filesel);
public static final native int gtk_file_selection_new(byte[] title);
public static final native void gtk_file_selection_set_filename(int filesel, byte[] filename);
public static final native void gtk_file_selection_complete(int filesel, byte[] pattern);
public static final native int gtk_font_selection_dialog_get_font_name(int fsd);
public static final native int gtk_font_selection_dialog_new(byte[] title);
public static final native boolean gtk_font_selection_dialog_set_font_name(int fsd, byte[] fontname);
public static final native void gtk_text_set_editable(int editable, boolean is_editable);
public static final native int gtk_entry_new();
public static final native void gtk_entry_append_text(int entry, byte[] text);
public static final native void gtk_editable_delete_selection(int editable);
public static final native int gtk_editable_get_chars(int editable, int start_pos, int end_pos);
public static final native void gtk_entry_set_visibility(int entry, boolean visible);
public static final native void gtk_entry_set_max_length(int entry, short max);
/* frame */
public static final native int gtk_frame_new(byte[] label);
public static final native void gtk_frame_set_shadow_type(int frame, int type);
public static final native void gtk_frame_set_label(int frame, byte[] label);
public static final native void swt_frame_get_trim(int handle, int[] trims);

public static final native int gtk_hseparator_new();
public static final native int gtk_hbox_new(boolean homogeneous, int spacing);
public static final native void gtk_grab_add(int widget);
public static final native int gtk_grab_get_current();
public static final native void gtk_grab_remove(int widget);
public static final native int gtk_hscale_new(int adjustment);
public static final native int gtk_hscrollbar_new(int adjustment);
public static final native void gtk_label_set_justify(int label, int jtype);
public static final native int gtk_label_new(String label);
public static final native void gtk_label_set_pattern(int label, byte[] pattern);
public static final native void gtk_main_quit();
public static final native void gtk_main();
public static final native void gtk_list_clear_items(int list, int start, int end);
public static final native void gtk_list_select_item(int list, int item);
public static final native int gtk_main_iteration();
public static final native void gtk_label_set_line_wrap(int label, boolean wrap);
public static final native int gtk_label_parse_uline(int label, byte[] string);
public static final native void gtk_label_set_text(int label, byte[] str);
public static final native void gtk_misc_set_alignment(int misc, float xalign, float yalign);
public static final native int gtk_menu_bar_new();
public static final native int gtk_menu_new();
public static final native void gtk_menu_popdown(int menu);
public static final native void gtk_menu_popup(int menu, int parent_menu_shell, int parent_menu_item, int func, int data, int button, int activate_time);
public static final native int gtk_menu_item_new();
public static final native int gtk_menu_item_new_with_label(byte[] label);
public static final native void gtk_menu_bar_insert(int menu_bar, int child, int position);
public static final native void gtk_menu_insert(int menu, int child, int position);
public static final native void gtk_menu_item_set_submenu(int menu_item, int submenu);
public static final native void gtk_menu_item_remove_submenu(int menu_item);
public static final native int gtk_notebook_new();
public static final native void gtk_notebook_insert_page(int notebook, int child, int tab_label, int position);
public static final native int gtk_notebook_get_current_page(int notebook);
public static final native void gtk_object_ref(int object);
public static final native void gtk_notebook_set_show_tabs(int notebook, boolean show_tabs);
public static final native void gtk_notebook_remove_page(int notebook, int page_num);
public static final native void gtk_notebook_set_page(int notebook, int page_num);
public static final native void gtk_object_set_user_data(int object, int data);
public static final native int gtk_object_get_user_data(int object);
public static final native int gtk_pixmap_new(int pixmap, int mask);
public static final native double gtk_adjustment_get_value(int adj);
public static final native int gtk_progress_bar_new();
public static final native void gtk_progress_bar_set_orientation(int pbar, int orientation);
public static final native void gtk_progress_bar_set_bar_style(int pbar, int style);
public static final native void gtk_progress_configure(int progress, float value, float min, float max);
public static final native void gtk_pixmap_set(int pixmap, int val, int mask);
public static final native int gtk_radio_button_new(int group);
public static final native int gtk_radio_button_group(int radio_button);
public static final native int gtk_radio_menu_item_new_with_label(int group, byte[] label);
public static final native int gtk_range_get_adjustment(int range);
public static final native int gtk_scrolled_window_new(int hadjustment, int vadjustment);
public static final native void gtk_scale_set_digits(int scale, int digits);
public static final native void gtk_scale_set_draw_value(int scale, boolean draw_value);
public static final native void gtk_scale_set_value_pos(int scale, int pos);
public static final native int gtk_scrolled_window_get_hadjustment(int scrolled_window);
public static final native void gtk_scrolled_window_set_policy(int scrolled_window, int hscrollbar_policy, int vscrollbar_policy);
public static final native int gtk_scrolled_window_get_vadjustment(int scrolled_window);
public static final native int gtk_selection_convert(int widget, int selection, int target, int time);
public static final native int gtk_signal_connect(int object, byte[] name, int func, int func_data);
public static final native int gtk_selection_owner_set(int widget, int selection, int time);
public static final native void gtk_signal_emit_stop_by_name(int object, byte[] name);
public static final native void gtk_signal_handler_block_by_func(int object, int func, int data);
public static final native void gtk_signal_handler_unblock_by_func(int object, int func, int data);
public static final native int gtk_signal_connect_after(int object, byte[] name, int func, int func_data);
public static final native int gtk_style_copy(int style);
public static final native void gtk_style_unref(int style);
public static final native int gtk_toggle_button_new();
public static final native void gtk_toggle_button_set_active(int toggle_button, boolean is_active);
public static final native boolean gtk_toggle_button_get_active(int toggle_button);
public static final native int gtk_timeout_add(int interval, int function, int data);
public static final native void gtk_timeout_remove(int timeout_handler_id);
public static final native int gtk_text_new(int hadj, int vadj);
public static final native void gtk_text_set_word_wrap(int text, int word_wrap);
public static final native int gtk_text_get_length(int text);
public static final native int gtk_toolbar_new();
public static final native void gtk_toolbar_set_button_relief(int toolbar, int relief);
public static final native void gtk_toolbar_insert_widget(int toolbar, int widget, byte[] tooltip_text, byte[] tooltip_private_text, int position);
public static final native void gtk_toolbar_set_orientation(int toolbar, int orientation);
public static final native int gtk_toolbar_insert_element(int toolbar, int type, int widget, byte[] text, byte[] tooltip_text, byte[] tooltip_private_text, int icon, int callback, int user_data, int position);
public static final native int gtk_tooltips_new();
public static final native int gtk_vseparator_new();
public static final native int gtk_vbox_new(boolean homogeneous, int spacing);
public static final native int gtk_vscale_new(int adjustment);
public static final native int gtk_vscrollbar_new(int adjustment);
public static final native void gtk_tooltips_set_tip(int tooltips, int widget, byte[] tip_text, byte[] tip_private);
public static final native int gtk_widget_get_pango_context(int widget);
public static final native int gtk_widget_get_default_style();
public static final native void gtk_widget_add_events(int widget, int events);
public static final native void gtk_widget_destroy(int widget);
public static final native int gtk_widget_event(int widget, int event);
public static final native void gtk_widget_hide(int widget);
public static final native void gtk_widget_grab_focus(int widget);
public static final native int gtk_widget_get_style(int widget);
public static final native void gtk_widget_add_accelerator(int widget, byte[] accel_signal, int accel_group, int accel_key, int accel_mods, int accel_flags);
public static final native void gtk_widget_ensure_style(int widget);
public static final native void gtk_widget_show(int widget);
public static final native void gtk_widget_realize(int widget);
static int malloc(String name) {
	int length = name.length();
	char [] unicode = new char [length];
	name.getChars (0, length, unicode, 0);
	byte[] buffer = new byte [length + 1];
	for (int i = 0; i < length; i++) {
		buffer[i] = (byte) unicode[i];
	}
	return OS.g_strdup (buffer);
}

public static final native void gtk_widget_show_all(int widget);
public static final native void gtk_widget_show_now(int widget);
public static final native void gtk_widget_queue_draw(int widget);
public static final native void gtk_widget_set_style(int widget, int style);
public static final native void gtk_widget_set_sensitive(int widget, boolean sensitive);
public static final native void gtk_widget_set_state(int widget, int state);
public static final native void gtk_widget_size_request(int widget, GtkRequisition requisition);
public static final native void gtk_widget_set_uposition(int widget, int x, int y);
public static final native void gtk_widget_set_usize(int widget, int width, int height);
public static final native void gtk_widget_remove_accelerator(int widget, int accel_group, int accel_key, int accel_mods);
public static final native void gtk_widget_set_parent(int widget, int parent);
public static final native void gtk_widget_modify_font(int widget, int pango_font_descr);
public static final native void gtk_widget_reparent(int widget, int new_parent);
public static final native void gtk_widget_size_allocate(int widget, GtkAllocation allocation);


/* gtk_window_* */
public static final native int  gtk_window_new(int type);
public static final native void gtk_window_set_title(int window, byte[] title);
public static final native void gtk_window_set_policy(int window, int allow_shrink, int allow_grow, int auto_shrink);
public static final native void gtk_window_set_resizable(int window, boolean resizable);
public static final native int  gtk_window_get_focus(int window);


public static final native void gtk_window_set_modal(int window, boolean modal);
public static final native void gtk_window_add_accel_group(int window, int accel_group);
public static final native void gtk_window_set_transient_for(int window, int parent);



/* Pango */
public static final native int PANGO_WEIGHT_NORMAL();
public static final native int PANGO_WEIGHT_BOLD();
public static final native int PANGO_STYLE_NORMAL();
public static final native int PANGO_STYLE_ITALIC();
public static final native int PANGO_STYLE_OBLIQUE();
public static final native int PANGO_SCALE();
public static final native int PANGO_STRETCH_NORMAL();

public static final native int PANGO_PIXELS(int dimension);

public static final native int gdk_pango_context_get();
public static final native int pango_context_new();
public static final native int pango_context_get_font_description(int context);
public static final native void pango_context_set_font_description(int context, int descr);
public static final native int pango_context_get_metrics(int context, int desc, int language);
public static final native int pango_context_get_language(int context);
public static final native void pango_context_list_families(int context, int[] families, int[] n_families);
public static final native void pango_font_family_list_faces(int family, int[] faces, int[] n_faces);
public static final native int pango_font_face_describe(int face);

public static final native int pango_language_from_string(byte[] language);
public static final native int pango_language_to_string(int language);

public static final native int  pango_layout_new(int context);
public static final native void pango_layout_set_text(int layout, byte[] text, int length);
public static final native void pango_layout_get_size(int layout, int[] width, int[] height);
public static final native void pango_layout_set_font_description(int layout, int desc);
public static final native void pango_layout_set_markup_with_accel(int layout, byte[] markup, int length, char accel_marker, char[] accel_char);

public static final native int pango_font_description_new();
public static final native int pango_font_description_from_string(byte[] str);
public static final native int pango_font_description_to_string(int desc);
public static final native boolean pango_font_description_equal(int desc1, int desc2);
public static final native void pango_font_description_free(int desc);
public static final native int pango_font_description_get_family(int desc);
public static final native void pango_font_description_set_family(int desc, byte[] family);
public static final native int pango_font_description_get_size(int desc);
public static final native void pango_font_description_set_size(int desc, int size);
public static final native void pango_font_description_set_stretch(int desc, int stretch);
public static final native int pango_font_description_get_style(int desc);
public static final native void pango_font_description_set_style(int desc, int weight);
public static final native int pango_font_description_get_weight(int desc);
public static final native void pango_font_description_set_weight(int desc, int weight);

public static final native int pango_font_metrics_get_ascent(int metrics);
public static final native int pango_font_metrics_get_descent(int metrics);
public static final native int pango_font_metrics_get_approximate_char_width(int metrics);

/* GdkColorspace enumeration */
/* R/G/B additive color space */
public static final native int GDK_COLORSPACE_RGB();

/* GdkPixbuf accessors */

public static final native int gdk_pixbuf_get_colorspace (int pixbuf);
public static final native int gdk_pixbuf_get_n_channels (int pixbuf);
public static final native boolean gdk_pixbuf_get_has_alpha (int pixbuf);
public static final native int gdk_pixbuf_get_bits_per_sample (int pixbuf);
public static final native int gdk_pixbuf_get_pixels (int pixbuf);
public static final native int gdk_pixbuf_get_width (int pixbuf);
public static final native int gdk_pixbuf_get_height (int pixbuf);
public static final native int gdk_pixbuf_get_rowstride (int pixbuf);

/* PIXBUF CREATION FROM DATA IN MEMORY */

public static final native int gdk_pixbuf_new (int colorspace, boolean has_alpha, int bits_per_sample, int width, int height);
public static final native int gdk_pixbuf_copy(int source);
public static final native int gdk_pixbuf_add_alpha (int pixbuf, boolean substitute_color, byte r, byte g, byte b);
public static final native int gdk_pixbuf_new_from_data (byte[] data, int colorspace, boolean has_alpha, int bits_per_sample, int width, int height, int rowstride, int destroy_fn, int destroy_fn_data);
public static final native int gdk_pixbuf_new_from_xpm_data (int pdata);

/* PIXBUF CREATION - FILE LOADING */

public static final native int gdk_pixbuf_new_from_file (byte[] filename);

/* RENDERING TO A DRAWABLE */

public static final native void gdk_pixbuf_render_to_drawable_alpha (int pixbuf, int drawable, int src_x, int src_y, int dest_x, int dest_y, int width, int height, int alpha_mode, int alpha_threshold, int dither, int x_dither, int y_dither);
public static final native void gdk_pixbuf_render_to_drawable (int pixbuf, int drawable, int gc, int src_x, int src_y, int dest_x, int dest_y, int width, int height, int dither, int x_dither, int y_dither);

/* SCALING */

public static final native void gdk_pixbuf_scale (int src, int dest, int dest_x, int dest_y, int dest_width, int dest_height, double offset_x, double offset_y, double scale_x, double scale_y, int interp_type);
public static final native void gdk_pixbuf_composite (int src, int dest, int dest_x, int dest_y, int dest_width, int dest_height, double offset_x, double offset_y, double scale_x, double scale_y,  int interp_type, int overall_alpha);
public static final native void gdk_pixbuf_composite_color (int src, int dest, int dest_x, int dest_y,  int dest_width, int dest_height, double offset_x, double offset_y, double scale_x,double scale_y, int interp_type, int overall_alpha, int check_x, int check_y, int check_size, int color1, int color2);
public static final native int gdk_pixbuf_scale_simple (int src, int dest_width, int dest_height, int interp_type);
public static final native int gdk_pixbuf_composite_color_simple (int src,int dest_width, int dest_height, int interp_type, int overall_alpha, int check_size, int color1, int color2);
public static final native int gdk_pixbuf_get_from_drawable (int dest, int src, int cmap, int src_x, int src_y, int dest_x, int dest_y, int width, int height);

/* Other */

public static final native int XListFonts(byte[] pattern, int maxFonts, int[] pnum_fonts);
public static final native int strlen (int str);



/* Primitive memmoves */
public static final native void memmove(int[] dest, int src, int size);
public static final native void memmove(byte[] dest, int src, int size);
public static final native void memmove(int[] dest, byte[] src, int size);
public static final native void memmove(int dest, byte[] src, int size);
public static final native void memmove(int dest, int[] src, int size);


/* Read memmoves */
static final native void memmove(GdkImage dest, int src);
static final native void memmove(GdkVisual dest, int src);
static final native void memmove(GdkFont dest, int src);
static final native void memmove(GdkColor dest, int src);
static final native void memmove(GdkEventExpose dest, int src);
static final native void memmove(GtkAdjustment dest, int src);
static final native void memmove(GtkCombo dest, int src);
static final native void memmove(GtkEditable dest, int src);
static final native void memmove(GtkStyle dest, int src);
static final native void memmove(GtkStyleClass dest, int src);
static final native void memmove(GtkCListRow dest, int src);
static final native void memmove(GtkCListColumn dest, int src);
static final native void memmove(GtkCList dest, int src);
static final native void memmove(GtkCTreeRow dest, int src);
static final native void memmove(GtkCTree dest, int src);

/* Write memmoves */
public static final native void memmove(int dest, GtkEditable src, int size);
public static final native void memmove(int dest, GtkStyle src, int size);
public static final native void memmove(int dest, GtkAdjustment src); // sure needed
public static final native void memmove(int dest, GtkCListColumn src, int size);

public static final native void gdk_window_get_frame_extents(int window, GdkRectangle rect);
public static final native void gdk_window_process_all_updates();
public static final native void gdk_window_process_updates(int window, boolean update_children);
//public static final native boolean gtk_widget_translate_coordinates(int src_widget, int dest_widget, int src_x, int src_y, int [] dest_x, int [] dest_y);

public static final native void gtk_label_set_text_with_mnemonic(int label, byte[] str);
public static final native int gtk_label_new_with_mnemonic(byte[] str);
public static final native int gtk_frame_get_label_widget(int frame);

public static final native void gtk_widget_set_size_request(int widget, int width, int height);
public static final native void gtk_widget_get_size_request(int widget, int [] width, int [] height);
public static final native void gtk_widget_get_child_requisition(int widget, GtkRequisition requisition);
public static final native int GTK_WIDGET_X(int wid);
public static final native int GTK_WIDGET_Y(int wid);
public static final native int GTK_WIDGET_WIDTH(int wid);
public static final native int GTK_WIDGET_HEIGHT(int wid);
public static final native void gtk_container_resize_children(int container);
public static final native void gtk_container_set_resize_mode(int container, int mode);
public static final native int gtk_fixed_new();
public static final native void gtk_fixed_move(int fixed, int widget, int x, int y);
public static final native void gtk_fixed_set_has_window(int fixed, boolean has_window);
public static final native void gtk_scrolled_window_add_with_viewport (int scrolled_window, int child);
public static final native void gtk_widget_modify_bg (int widget, int state, GdkColor color);
public static final native void gtk_widget_modify_fg (int widget, int state, GdkColor color);
public static final native void GTK_BIN_SET_CHILD (int bin, int child);
public static final native void gtk_scrolled_window_set_shadow_type(int scrolled_window, int type);
public static final native void gtk_widget_queue_resize(int widget);
public static final native void memmove(GtkAllocation dest, int src);
public static final native void gtk_widget_set_redraw_on_allocate(int widget, boolean redraw);
public static final native int gdk_pointer_grab(int window, boolean owner_events, int event_mask, int confine_to, int cursor, int time);
public static final native int gdk_pointer_ungrab(int time);
public static final native int g_signal_connect_swapped(int instance, byte[] detailed_sigal, int c_handler, int data);
public static final native void gtk_window_set_default(int window, int widget);
public static final native int gtk_window_get_default(int window);
public static final native boolean gtk_window_activate_default(int window);
public static final native void gtk_widget_activate(int widget);
public static final native void gtk_clist_set_row_height(int clist, int height);
public static final native boolean gdk_event_focus_get_in(int event);
public static final native void gdk_window_set_back_pixmap(int window, int pixmap, boolean parent_relative);
public static final native void gdk_window_set_override_redirect(int window, boolean override_redirect);

public static final native int gtk_widget_send_expose(int widget, int event); 
public static final native void gdk_window_scroll(int window, int dx, int dy);
public static final native void gtk_container_set_border_width(int container, int border_width);
public static final native int gdk_drawable_get_visible_region (int drawable);

public static final native void gdk_window_invalidate_rect(int window, GdkRectangle rectangle, boolean invalidate_children);
public static final native void gdk_window_invalidate_region(int window, int region, boolean invalidate_children);
public static final native void  gtk_notebook_set_scrollable(int notebook, boolean scrollable);


}