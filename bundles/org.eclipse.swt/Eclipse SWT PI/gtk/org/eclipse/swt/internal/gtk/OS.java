/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others. All rights reserved.
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

public class OS extends C {
	static {
		Library.loadLibrary("swt-pi");
	}
	
	/** OS Constants */
	public static final boolean IsAIX, IsSunOS, IsLinux, IsHPUX;
	static {
		
		/* Initialize the OS flags and locale constants */
		String osName = System.getProperty ("os.name");
		boolean isAIX = false, isSunOS = false, isLinux = false, isHPUX = false;
		if (osName.equals ("Linux")) isLinux = true;
		if (osName.equals ("AIX")) isAIX = true;
		if (osName.equals ("Solaris")) isSunOS = true;
		if (osName.equals ("SunOS")) isSunOS = true;
		if (osName.equals ("HP-UX")) isHPUX = true;
		IsAIX = isAIX;  IsSunOS = isSunOS;  IsLinux = isLinux;  IsHPUX = isHPUX;
	}

	/** Constants */
	public static final int ATK_RELATION_LABELLED_BY = 4;
	public static final int G_SIGNAL_MATCH_DATA = 1 << 4;
	public static final int G_SIGNAL_MATCH_ID = 1 << 0;
	public static final int GDK_2BUTTON_PRESS = 0x5;
	public static final int GDK_3BUTTON_PRESS = 0x6;
	public static final int GDK_ACTION_COPY = 1 << 1;
	public static final int GDK_ACTION_MOVE = 1 << 2;
	public static final int GDK_ACTION_LINK = 1 << 3;
	public static final int GDK_Alt_L = 0xffe9;
	public static final int GDK_Alt_R = 0xffea;
	public static final int GDK_AND = 4;
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
	public static final int GDK_CAP_BUTT = 0x1;
	public static final int GDK_CAP_PROJECTING = 3;
	public static final int GDK_CAP_ROUND = 0x2;
	public static final int GDK_COLORSPACE_RGB = 0;
	public static final int GDK_CONFIGURE = 13;
	public static final int GDK_CONTROL_MASK = 0x4;
	public static final int GDK_COPY = 0x0;
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
	public static final int GTK_EXPANDER_COLAPSED = 0;
	public static final int GTK_EXPANDER_SEMI_COLLAPSED = 1;
	public static final int GTK_EXPANDER_SEMI_EXPANDED = 2;
	public static final int GTK_EXPANDER_EXPANDED = 3;
	public static final int GDK_EXPOSE = 2;
	public static final int GDK_EXPOSURE_MASK = 0x2;
	public static final int GDK_End = 0xff57;
	public static final int GDK_Escape = 0xff1b;
	public static final int GDK_F1 = 0xffbe;
	public static final int GDK_F10 = 0xffc7;
	public static final int GDK_F11 = 0xffc8;
	public static final int GDK_F12 = 0xffc9;
	public static final int GDK_F13 = 0xffca;
	public static final int GDK_F14 = 0xffcb;
	public static final int GDK_F15 = 0xffcc;
	public static final int GDK_F2 = 0xffbf;
	public static final int GDK_F3 = 0xffc0;
	public static final int GDK_F4 = 0xffc1;
	public static final int GDK_F5 = 0xffc2;
	public static final int GDK_F6 = 0xffc3;
	public static final int GDK_F7 = 0xffc4;
	public static final int GDK_F8 = 0xffc5;
	public static final int GDK_F9 = 0xffc6;
	public static final int GDK_FLEUR = 0x34;
	public static final int GDK_FOCUS_CHANGE = 0xc;
	public static final int GDK_FOCUS_CHANGE_MASK = 0x4000;
	public static final int GDK_GC_FOREGROUND = 0x1;
	public static final int GDK_GC_CLIP_MASK = 0x80;
	public static final int GDK_GC_CLIP_X_ORIGIN = 0x800;
	public static final int GDK_GC_CLIP_Y_ORIGIN = 0x1000;
	public static final int GDK_GC_LINE_WIDTH = 0x4000;
	public static final int GDK_GC_LINE_STYLE = 0x8000;
	public static final int GDK_GC_CAP_STYLE = 0x10000;
	public static final int GDK_GC_JOIN_STYLE = 0x20000;
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
	public static final int GDK_JOIN_MITER = 0x0;
	public static final int GDK_JOIN_ROUND = 0x1;
	public static final int GDK_JOIN_BEVEL = 0x2;
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
	public static final int GDK_LINE_ON_OFF_DASH = 0x1;
	public static final int GDK_LINE_SOLID = 0x0;
	public static final int GDK_Linefeed = 0xff0A;
	public static final int GDK_LSB_FIRST = 0x0;
	public static final int GDK_Left = 0xff51;
	public static final int GDK_Meta_L = 0xFFE7;
	public static final int GDK_Meta_R = 0xFFE8;
	public static final int GDK_MAP = 14;
	public static final int GDK_MOD1_MASK = 0x8;
	public static final int GDK_MOTION_NOTIFY = 0x3;
	public static final int GDK_NO_EXPOSE = 30;
	public static final int GDK_NONE = 0;
	public static final int GDK_NOTIFY_INFERIOR = 2;
	public static final int GDK_Num_Lock = 0xFF7F;
	public static final int GDK_OVERLAP_RECTANGLE_OUT = 0x1;
	public static final int GDK_PIXBUF_ALPHA_BILEVEL = 0x0;
	public static final int GDK_POINTER_MOTION_HINT_MASK = 0x8;
	public static final int GDK_POINTER_MOTION_MASK = 0x4;
	public static final int GDK_PROPERTY_NOTIFY = 16;
	public static final int GDK_Page_Down = 0xff56;
	public static final int GDK_Page_Up = 0xff55;
	public static final int GDK_Pause = 0xff13;
	public static final int GDK_Print = 0xff61;
	public static final int GDK_QUESTION_ARROW = 0x5c;
	public static final int GDK_RGB_DITHER_NORMAL = 0x1;
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
	public static final int GDK_VISIBILITY_FULLY_OBSCURED = 2;
	public static final int GDK_VISIBILITY_NOTIFY_MASK = 1 << 17;
	public static final int GDK_WINDOW_CHILD = 2;
	public static final int GDK_WINDOW_STATE_ICONIFIED  = 1 << 1;
	public static final int GDK_WINDOW_STATE_MAXIMIZED  = 1 << 2;
	public static final int GDK_WINDOW_STATE_FULLSCREEN  = 1 << 4;
	public static final int GTK_ACCEL_VISIBLE = 0x1;
	public static final int GTK_ARROW_DOWN = 0x1;
	public static final int GTK_ARROW_LEFT = 0x2;
	public static final int GTK_ARROW_RIGHT = 0x3;
	public static final int GTK_ARROW_UP = 0x0;
	public static final int GTK_CALENDAR_SHOW_HEADING = 1 << 0;
	public static final int GTK_CALENDAR_SHOW_DAY_NAMES = 1 << 1;
	public static final int GTK_CALENDAR_NO_MONTH_CHANGE = 1 << 2;
	public static final int GTK_CALENDAR_SHOW_WEEK_NUMBERS = 1 << 3;
	public static final int GTK_CALENDAR_WEEK_START_MONDAY = 1 << 4;
	public static final int GTK_CAN_DEFAULT = 0x2000;
	public static final int GTK_CAN_FOCUS = 0x800;
	public static final int GTK_CELL_RENDERER_MODE_ACTIVATABLE = 1;
	public static final int GTK_CELL_RENDERER_SELECTED = 1 << 0;
	public static final int GTK_CELL_RENDERER_FOCUSED = 1 << 4;
	public static final int GTK_CLIST_SHOW_TITLES = 0x4;
	public static final int GTK_CORNER_TOP_LEFT = 0x0;
	public static final int GTK_CORNER_TOP_RIGHT = 0x2;
	public static final int GTK_DIALOG_DESTROY_WITH_PARENT = 1 << 1;
	public static final int GTK_DIALOG_MODAL = 1 << 0;
	public static final int GTK_DIR_TAB_FORWARD = 0;
	public static final int GTK_DIR_TAB_BACKWARD = 1;
	public static final int GTK_FILE_CHOOSER_ACTION_OPEN = 0;
	public static final int GTK_FILE_CHOOSER_ACTION_SAVE = 1;
	public static final int GTK_FILE_CHOOSER_ACTION_SELECT_FOLDER = 2;
	public static final int GTK_HAS_FOCUS = 1 << 12;
	public static final int GTK_ICON_SIZE_MENU = 1;
	public static final int GTK_ICON_SIZE_SMALL_TOOLBAR = 2;
	public static final int GTK_ICON_SIZE_LARGE_TOOLBAR = 3;
	public static final int GTK_ICON_SIZE_DIALOG = 6;
	public static final int GTK_JUSTIFY_CENTER = 0x2;
	public static final int GTK_JUSTIFY_LEFT = 0x0;
	public static final int GTK_JUSTIFY_RIGHT = 0x1;
	public static final int GTK_MAPPED = 1 << 7;
	public static final int GTK_MESSAGE_INFO = 0;
	public static final int GTK_MESSAGE_WARNING = 1;
	public static final int GTK_MESSAGE_QUESTION = 2;
	public static final int GTK_MESSAGE_ERROR = 3;
	public static final int GTK_NO_WINDOW = 1 << 5;
	public static final int GTK_ORIENTATION_HORIZONTAL = 0x0;
	public static final int GTK_ORIENTATION_VERTICAL = 0x1;
	public static final int GTK_PACK_END = 1;
	public static final int GTK_PACK_START = 0;
	public static final int GTK_PAGE_ORIENTATION_PORTRAIT = 0;
	public static final int GTK_PAGE_ORIENTATION_LANDSCAPE = 1;
	public static final int GTK_POLICY_ALWAYS = 0x0;
	public static final int GTK_POLICY_AUTOMATIC = 0x1;
	public static final int GTK_POLICY_NEVER = 0x2;
	public static final int GTK_POS_TOP = 0x2;
	public static final int GTK_POS_BOTTOM = 0x3;
	public static final int GTK_PRINT_CAPABILITY_PAGE_SET     = 1 << 0;
	public static final int GTK_PRINT_CAPABILITY_COPIES       = 1 << 1;
	public static final int GTK_PRINT_CAPABILITY_COLLATE      = 1 << 2;
	public static final int GTK_PRINT_CAPABILITY_REVERSE      = 1 << 3;
	public static final int GTK_PRINT_CAPABILITY_SCALE        = 1 << 4;
	public static final int GTK_PRINT_CAPABILITY_GENERATE_PDF = 1 << 5;
	public static final int GTK_PRINT_CAPABILITY_GENERATE_PS  = 1 << 6;
	public static final int GTK_PRINT_CAPABILITY_PREVIEW      = 1 << 7;
	public static final int GTK_PRINT_PAGES_ALL = 0;
	public static final int GTK_PRINT_PAGES_CURRENT = 1;
	public static final int GTK_PRINT_PAGES_RANGES = 2;
	public static final int GTK_PROGRESS_CONTINUOUS = 0x0;
	public static final int GTK_PROGRESS_DISCRETE = 0x1;
	public static final int GTK_PROGRESS_LEFT_TO_RIGHT = 0x0;
	public static final int GTK_PROGRESS_BOTTOM_TO_TOP = 0x2;
	public static final int GTK_REALIZED  = 1 << 6;
	public static final int GTK_RECEIVES_DEFAULT = 1 << 20;
	public static final int GTK_RELIEF_NONE = 0x2;
	public static final int GTK_RELIEF_NORMAL = 0;
	public static final int GTK_RC_BG = 1 << 1;
	public static final int GTK_RC_FG = 1 << 0;
	public static final int GTK_RC_TEXT = 1 << 2;
	public static final int GTK_RC_BASE = 1 << 3;
	public static final int GTK_RESPONSE_APPLY = 0xfffffff6;
	public static final int GTK_RESPONSE_CANCEL = 0xfffffffa;
	public static final int GTK_RESPONSE_OK = 0xfffffffb;
	public static final int GTK_SCROLL_NONE = 0;
	public static final int GTK_SCROLL_JUMP = 1;
	public static final int GTK_SCROLL_STEP_BACKWARD = 2;
	public static final int GTK_SCROLL_STEP_FORWARD = 3;
	public static final int GTK_SCROLL_PAGE_BACKWARD = 4;
	public static final int GTK_SCROLL_PAGE_FORWARD = 5;
	public static final int GTK_SCROLL_STEP_UP = 6;
	public static final int GTK_SCROLL_STEP_DOWN = 7;
	public static final int GTK_SCROLL_PAGE_UP = 8;
	public static final int GTK_SCROLL_PAGE_DOWN = 9;
	public static final int GTK_SCROLL_STEP_LEFT = 10;
	public static final int GTK_SCROLL_STEP_RIGHT = 11;
	public static final int GTK_SCROLL_PAGE_LEFT = 12;
	public static final int GTK_SCROLL_PAGE_RIGHT = 13;
	public static final int GTK_SCROLL_START = 14;
	public static final int GTK_SCROLL_END = 15;
	public static final int GTK_SELECTION_BROWSE = 0x2;
	public static final int GTK_SELECTION_MULTIPLE = 0x3;
	public static final int GTK_SENSITIVE = 0x200;
	public static final int GTK_SHADOW_ETCHED_IN = 0x3;
	public static final int GTK_SHADOW_ETCHED_OUT = 0x4;
	public static final int GTK_SHADOW_IN = 0x1;
	public static final int GTK_SHADOW_NONE = 0x0;
	public static final int GTK_SHADOW_OUT = 0x2;
	public static final int GTK_STATE_ACTIVE = 0x1;
	public static final int GTK_STATE_INSENSITIVE = 0x4;
	public static final int GTK_STATE_NORMAL = 0x0;
	public static final int GTK_STATE_PRELIGHT = 0x2;
	public static final int GTK_STATE_SELECTED = 0x3;
	public static final int GTK_TEXT_DIR_LTR = 1;
	public static final int GTK_TEXT_DIR_NONE = 0 ;
	public static final int GTK_TEXT_DIR_RTL = 2;
	public static final int GTK_TEXT_WINDOW_TEXT = 2;
	public static final int GTK_TOOLBAR_CHILD_BUTTON = 0x1;
	public static final int GTK_TOOLBAR_CHILD_RADIOBUTTON = 0x3;
	public static final int GTK_TOOLBAR_CHILD_TOGGLEBUTTON = 0x2;
	public static final int GTK_TREE_VIEW_COLUMN_GROW_ONLY = 0;
	public static final int GTK_TREE_VIEW_COLUMN_AUTOSIZE = 1;
	public static final int GTK_TREE_VIEW_COLUMN_FIXED = 2;
	public static final int GTK_TREE_VIEW_DROP_BEFORE = 0;
	public static final int GTK_TREE_VIEW_DROP_AFTER = 1;
	public static final int GTK_TREE_VIEW_DROP_INTO_OR_BEFORE = 2;
	public static final int GTK_TREE_VIEW_DROP_INTO_OR_AFTER = 3;
	public static final int GTK_TREE_VIEW_GRID_LINES_NONE = 0;
	public static final int GTK_TREE_VIEW_GRID_LINES_HORIZONTAL = 1;
	public static final int GTK_TREE_VIEW_GRID_LINES_VERTICAL = 2;
	public static final int GTK_TREE_VIEW_GRID_LINES_BOTH = 3;
	public static final int GDK_UNMAP = 15;
	public static final int GTK_UNIT_PIXEL = 0;
	public static final int GTK_UNIT_POINTS = 1;
	public static final int GTK_UNIT_INCH = 2;
	public static final int GTK_UNIT_MM = 3;
	public static final int GTK_VISIBILITY_FULL = 0x2;
	public static final int GTK_VISIBILITY_NONE = 0x0;
	public static final int GTK_VISIBLE = 0x100;
	public static final int GDK_WA_X = 1 << 2;
	public static final int GDK_WA_Y = 1 << 3;
	public static final int GDK_WA_VISUAL = 1 << 6;
	public static final int GTK_WINDOW_POPUP = 0x1;
	public static final int GTK_WINDOW_TOPLEVEL = 0x0;
	public static final int GDK_WINDOW_TYPE_HINT_DIALOG = 1;
	public static final int GTK_WRAP_NONE = 0;
	public static final int GTK_WRAP_WORD = 2;
	public static final int GTK_WRAP_WORD_CHAR = 3;
	public static final int G_LOG_FLAG_FATAL = 0x2;
	public static final int G_LOG_FLAG_RECURSION = 0x1;
	public static final int G_LOG_LEVEL_MASK = 0xfffffffc;
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
	public static final int PANGO_STRETCH_NORMAL = 0x4;
	public static final int PANGO_STYLE_ITALIC = 0x2;
	public static final int PANGO_STYLE_NORMAL = 0x0;
	public static final int PANGO_STYLE_OBLIQUE = 0x1;
	public static final int PANGO_TAB_LEFT = 0;
	public static final int PANGO_UNDERLINE_NONE = 0;
	public static final int PANGO_UNDERLINE_SINGLE = 1;
	public static final int PANGO_UNDERLINE_DOUBLE = 2;
	public static final int PANGO_UNDERLINE_LOW = 3;
	public static final int PANGO_UNDERLINE_ERROR = 4;
	public static final int PANGO_WEIGHT_BOLD = 0x2bc;
	public static final int PANGO_WEIGHT_NORMAL = 0x190;
	public static final int PANGO_WRAP_WORD = 0;
	public static final int PANGO_WRAP_WORD_CHAR = 2;
	public static final int RTLD_LAZY = 1;
	public static final int XA_CARDINAL = 6;
	public static final int XA_WINDOW = 33;
	
	/** Signals */
	public static final byte[] activate = ascii("activate");
	public static final byte[] button_press_event = ascii("button-press-event");
	public static final byte[] button_release_event = ascii("button-release-event");
	public static final byte[] changed = ascii("changed");
	public static final byte[] change_current_page = ascii("change-current-page");
	public static final byte[] change_value = ascii("change-value");
	public static final byte[] clicked = ascii("clicked");
	public static final byte[] commit = ascii("commit");
	public static final byte[] configure_event = ascii("configure-event");
	public static final byte[] delete_event = ascii("delete-event");
	public static final byte[] day_selected = ascii("day-selected");
	public static final byte[] delete_range = ascii("delete-range");
	public static final byte[] delete_text = ascii("delete-text");
	public static final byte[] drag_data_delete = ascii("drag_data_delete");
	public static final byte[] drag_data_get = ascii("drag_data_get");
	public static final byte[] drag_data_received = ascii("drag_data_received");
	public static final byte[] drag_drop = ascii("drag_drop");
	public static final byte[] drag_end = ascii("drag_end");
	public static final byte[] drag_leave = ascii("drag_leave");
	public static final byte[] drag_motion = ascii("drag_motion");
	public static final byte[] enter_notify_event = ascii("enter-notify-event");
	public static final byte[] event = ascii("event");
	public static final byte[] event_after = ascii("event-after");
	public static final byte[] expand_collapse_cursor_row = ascii("expand-collapse-cursor-row");
	public static final byte[] expose_event = ascii("expose-event");
	public static final byte[] focus = ascii("focus");
	public static final byte[] focus_in_event = ascii("focus-in-event");
	public static final byte[] focus_out_event = ascii("focus-out-event");
	public static final byte[] grab_focus = ascii("grab-focus");
	public static final byte[] hide = ascii("hide");
	public static final byte[] input = ascii("input");
	public static final byte[] insert_text = ascii("insert-text");
	public static final byte[] key_press_event = ascii("key-press-event");
	public static final byte[] key_release_event = ascii("key-release-event");
	public static final byte[] leave_notify_event = ascii("leave-notify-event");
	public static final byte[] map = ascii("map");
	public static final byte[] map_event = ascii("map-event");
	public static final byte[] mnemonic_activate = ascii("mnemonic-activate");
	public static final byte[] month_changed = ascii("month-changed");
	public static final byte[] motion_notify_event = ascii("motion-notify-event");
	public static final byte[] move_focus = ascii("move-focus");
	public static final byte[] output = ascii("output");
	public static final byte[] popup_menu = ascii("popup-menu");
	public static final byte[] populate_popup = ascii("populate-popup");
	public static final byte[] preedit_changed = ascii("preedit-changed");
	public static final byte[] realize = ascii("realize");
	public static final byte[] row_activated = ascii("row-activated");
	public static final byte[] row_changed = ascii("row-changed");
	public static final byte[] row_inserted = ascii("row-inserted");
	public static final byte[] row_deleted = ascii("row-deleted");
	public static final byte[] scroll_child = ascii("scroll-child");
	public static final byte[] scroll_event = ascii("scroll-event");
	public static final byte[] select = ascii("select");
	public static final byte[] show = ascii("show");
	public static final byte[] show_help = ascii("show-help");
	public static final byte[] size_allocate = ascii("size-allocate");
	public static final byte[] size_request = ascii("size-request");
	public static final byte[] style_set = ascii("style-set");
	public static final byte[] switch_page = ascii("switch-page");
	public static final byte[] test_collapse_row = ascii("test-collapse-row");
	public static final byte[] test_expand_row = ascii("test-expand-row");
	public static final byte[] toggled = ascii("toggled");
	public static final byte[] unmap = ascii("unmap");
	public static final byte[] unmap_event = ascii("unmap-event");
	public static final byte[] unrealize = ascii("unrealize");
	public static final byte[] value_changed = ascii("value-changed");
	public static final byte[] visibility_notify_event = ascii("visibility-notify-event");
	public static final byte[] window_state_event = ascii("window-state-event");
	
	/** Properties */
	public static final byte[] active = ascii("active");
	public static final byte[] background_gdk = ascii("background-gdk");
	public static final byte[] button_relief = ascii("button-relief");
	public static final byte[] cell_background_gdk = ascii("cell-background-gdk");
	public static final byte[] default_border = ascii("default-border");
	public static final byte[] expander_size = ascii("expander-size");
	public static final byte[] fixed_height_mode = ascii("fixed-height-mode");
	public static final byte[] focus_line_width = ascii("focus-line-width");
	public static final byte[] font_desc = ascii("font-desc");
	public static final byte[] foreground_gdk = ascii("foreground-gdk");
	public static final byte[] grid_line_width = ascii("grid-line-width");
	public static final byte[] gtk_alternative_button_order = ascii("gtk-alternative-button-order");
	public static final byte[] gtk_cursor_blink = ascii("gtk-cursor-blink");
	public static final byte[] gtk_cursor_blink_time = ascii("gtk-cursor-blink-time");
	public static final byte[] gtk_double_click_time = ascii("gtk-double-click-time");
	public static final byte[] gtk_entry_select_on_focus = ascii("gtk-entry-select-on-focus");
	public static final byte[] inner_border = ascii("inner-border");
	public static final byte[] horizontal_separator = ascii("horizontal-separator");
	public static final byte[] inconsistent = ascii("inconsistent");
	public static final byte[] interior_focus = ascii("interior-focus");
	public static final byte[] mode = ascii("mode");
	public static final byte[] model = ascii("model");
	public static final byte[] pixbuf = ascii("pixbuf");
	public static final byte[] text = ascii("text");
	public static final byte[] xalign = ascii("xalign");
	public static final byte[] ypad = ascii("ypad");
	public static final byte[] GTK_PRINT_SETTINGS_OUTPUT_URI = ascii("output-uri");
	
	public static final int GTK_VERSION = VERSION(gtk_major_version(), gtk_minor_version(), gtk_micro_version()); 
	
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

/** 64 bit */
public static final native int GInterfaceInfo_sizeof ();
public static final native int GPollFD_sizeof ();
public static final native int GTypeInfo_sizeof ();
public static final native int GTypeQuery_sizeof ();
public static final native int GdkColor_sizeof();
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
public static final native int GdkEventVisibility_sizeof();
public static final native int GdkEventWindowState_sizeof();
public static final native int GdkGCValues_sizeof();
public static final native int GdkGeometry_sizeof();
public static final native int GdkImage_sizeof();
public static final native int GdkRectangle_sizeof();
public static final native int GdkVisual_sizeof();
public static final native int GdkWindowAttr_sizeof();
public static final native int GtkAdjustment_sizeof();
public static final native int GtkAllocation_sizeof();
public static final native int GtkBorder_sizeof();
public static final native int GtkColorSelectionDialog_sizeof();
public static final native int GtkCombo_sizeof();
public static final native int GtkFileSelection_sizeof();
public static final native int GtkFixed_sizeof();
public static final native int GtkFixedClass_sizeof();
public static final native int GtkRequisition_sizeof();
public static final native int GtkSelectionData_sizeof();
public static final native int GtkTargetEntry_sizeof();
public static final native int GtkTargetPair_sizeof();
public static final native int GtkTextIter_sizeof();
public static final native int GtkCellRendererText_sizeof();
public static final native int GtkCellRendererTextClass_sizeof();
public static final native int GtkCellRendererPixbuf_sizeof();
public static final native int GtkCellRendererPixbufClass_sizeof();
public static final native int GtkCellRendererToggle_sizeof();
public static final native int GtkCellRendererToggleClass_sizeof();
public static final native int GtkTreeIter_sizeof();
public static final native int PangoAttribute_sizeof();
public static final native int PangoAttrColor_sizeof();
public static final native int PangoAttrInt_sizeof();
public static final native int PangoItem_sizeof();
public static final native int PangoLayoutLine_sizeof();
public static final native int PangoLayoutRun_sizeof();
public static final native int PangoLogAttr_sizeof();
public static final native int PangoRectangle_sizeof();
public static final native int XAnyEvent_sizeof();
public static final native int XButtonEvent_sizeof();
public static final native int XClientMessageEvent_sizeof();
public static final native int XEvent_sizeof();
public static final native int XCrossingEvent_sizeof();
public static final native int XExposeEvent_sizeof();
public static final native int XFocusChangeEvent_sizeof();
public static final native int XVisibilityEvent_sizeof();
public static final native int XWindowChanges_sizeof();
public static final native int /*long*/ localeconv_decimal_point();
/**
 * @param path cast=(const char *)
 * @param realPath cast=(char *)
 */
public static final native int /*long*/ realpath(byte[] path, byte[] realPath);

/** Object private fields accessors */


/** @param object_class cast=(GObjectClass *) */
public static final native int /*long*/ G_OBJECT_CLASS_CONSTRUCTOR(int /*long*/ object_class);
/** 
 * @param object_class cast=(GObjectClass *)
 * @paramOFF constructor cast=(GObject* (*) (GType, guint, GObjectConstructParam *))
 */
public static final native void G_OBJECT_CLASS_SET_CONSTRUCTOR(int /*long*/ object_class, int /*long*/ constructor);
/** @param widget cast=(GtkWidget *) */
public static final native int GTK_WIDGET_HEIGHT(int /*long*/ widget);
/** @param widget cast=(GtkWidget *) */
public static final native int GTK_WIDGET_WIDTH(int /*long*/ widget);
/** @param widget cast=(GtkWidget *) */
public static final native int /*long*/ GTK_WIDGET_WINDOW(int /*long*/ widget);
/** @param widget cast=(GtkWidget *) */
public static final native int GTK_WIDGET_X(int /*long*/ widget);
/** @param widget cast=(GtkWidget *) */
public static final native int GTK_WIDGET_Y(int /*long*/ widget);
/** @param widget cast=(GtkScrolledWindow *) */
public static final native int /*long*/ GTK_SCROLLED_WINDOW_HSCROLLBAR(int /*long*/ widget);
/** @param widget cast=(GtkScrolledWindow *) */
public static final native int /*long*/ GTK_SCROLLED_WINDOW_VSCROLLBAR(int /*long*/ widget);
/** @param widget cast=(GtkScrolledWindow *) */
public static final native int GTK_SCROLLED_WINDOW_SCROLLBAR_SPACING(int /*long*/ widget);
/**
 * @param acce_label cast=(GtkAccelLabel *)
 * @param string cast=(gchar *)
 */
public static final native void GTK_ACCEL_LABEL_SET_ACCEL_STRING(int /*long*/ acce_label, int /*long*/ string);
/** @param acce_label cast=(GtkAccelLabel *) */
public static final native int /*long*/ GTK_ACCEL_LABEL_GET_ACCEL_STRING(int /*long*/ acce_label);
/** @param widget cast=(GtkEntry *) */
public static final native int /*long*/ GTK_ENTRY_IM_CONTEXT(int /*long*/ widget);
/** @param widget cast=(GtkTextView *) */
public static final native int /*long*/ GTK_TEXTVIEW_IM_CONTEXT(int /*long*/ widget);
/** @param widget cast=(GtkTooltips *) */
public static final native int /*long*/ GTK_TOOLTIPS_TIP_WINDOW(int /*long*/ widget);
/**
 * @param widget cast=(GtkTooltips *)
 * @param data cast=(GtkTooltipsData *)
 */
public static final native void GTK_TOOLTIPS_SET_ACTIVE(int /*long*/ widget, int /*long*/ data);
/** @param widget cast=(GtkWidget *) */
public static final native void GTK_WIDGET_SET_HEIGHT(int /*long*/ widget, int height);
/** @param widget cast=(GtkWidget *) */
public static final native void GTK_WIDGET_SET_WIDTH(int /*long*/ widget, int width);
/** @param widget cast=(GtkWidget *) */
public static final native void GTK_WIDGET_SET_X(int /*long*/ widget, int x);
/** @param widget cast=(GtkWidget *) */
public static final native void GTK_WIDGET_SET_Y(int /*long*/ widget, int y);
/** @param widget cast=(GtkWidget *) */
public static final native int GTK_WIDGET_REQUISITION_WIDTH(int /*long*/ widget);
/** @param widget cast=(GtkWidget *) */
public static final native int GTK_WIDGET_REQUISITION_HEIGHT(int /*long*/ widget);
/** @param event cast=(GdkEvent *) */
public static final native int GDK_EVENT_TYPE(int /*long*/ event);
/** @param event cast=(GdkEventAny *) */
public static final native int /*long*/ GDK_EVENT_WINDOW(int /*long*/ event);
/** @param xevent cast=(XEvent *) */
public static final native int X_EVENT_TYPE(int /*long*/ xevent);
/** @param xevent cast=(XAnyEvent *) */
public static final native int /*long*/ X_EVENT_WINDOW(int /*long*/ xevent);

/** X11 Native methods and constants */
public static final int Above = 0;
public static final int Below = 1;
public static final int ButtonRelease = 5;
public static final int ClientMessage = 33;
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
public static final int /*long*/ NoEventMask = 0;
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
public static final int VisibilityChangeMask = 1 << 16;
public static final int VisibilityFullyObscured = 2;
public static final int VisibilityNotify = 15;
public static final int SYSTEM_TRAY_REQUEST_DOCK = 0;
public static final native int _Call(int /*long*/ proc, int /*long*/ arg1, int /*long*/ arg2);
public static final int Call(int /*long*/ proc, int /*long*/ arg1, int /*long*/ arg2) {
	lock.lock();
	try {
		return _Call(proc, arg1, arg2);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _call (int /*long*/ function, int /*long*/ arg0, int /*long*/ arg1, int /*long*/ arg2, int /*long*/ arg3, int /*long*/ arg4, int /*long*/ arg5, int /*long*/ arg6);
public static final int /*long*/ call (int /*long*/ function, int /*long*/ arg0, int /*long*/ arg1, int /*long*/ arg2, int /*long*/ arg3, int /*long*/ arg4, int /*long*/ arg5, int /*long*/ arg6) {
	lock.lock();
	try {
		return _call(function, arg0, arg1, arg2, arg3, arg4, arg5, arg6);
	} finally {
		lock.unlock();
	}
}
/** @method flags=no_gen */
public static final native boolean GDK_WINDOWING_X11();
/** @param pixmap cast=(GdkPixmap *) */
public static final native int /*long*/ _GDK_PIXMAP_XID(int /*long*/ pixmap);
public static final int /*long*/ GDK_PIXMAP_XID(int /*long*/ pixmap) {
	lock.lock();
	try {
		return _GDK_PIXMAP_XID(pixmap);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param event_mask cast=(long)
 * @param event_return cast=(XEvent *)
 */
public static final native boolean _XCheckMaskEvent(int /*long*/ display, int /*long*/ event_mask, int /*long*/ event_return);
public static final boolean XCheckMaskEvent(int /*long*/ display, int /*long*/ event_mask, int /*long*/ event_return) {
	lock.lock();
	try {
		return _XCheckMaskEvent(display, event_mask, event_return);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param window cast=(Window)
 * @param event_mask cast=(long)
 * @param event_return cast=(XEvent *)
 */
public static final native boolean _XCheckWindowEvent(int /*long*/ display, int /*long*/ window, int /*long*/ event_mask, int /*long*/ event_return);
public static final boolean XCheckWindowEvent(int /*long*/ display, int /*long*/ window, int /*long*/ event_mask, int /*long*/ event_return) {
	lock.lock();
	try {
		return _XCheckWindowEvent(display, window, event_mask, event_return);
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
public static final native boolean _XCheckIfEvent(int /*long*/ display, int /*long*/ event_return, int /*long*/ predicate, int /*long*/ arg);
public static final boolean XCheckIfEvent(int /*long*/ display, int /*long*/ event_return, int /*long*/ predicate, int /*long*/ arg) {
	lock.lock();
	try {
		return _XCheckIfEvent(display, event_return, predicate, arg);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native int _XDefaultScreen(int /*long*/ display);
public static final int XDefaultScreen(int /*long*/ display) {
	lock.lock();
	try {
		return _XDefaultScreen(display);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native int /*long*/ _XDefaultRootWindow(int /*long*/ display);
public static final int /*long*/ XDefaultRootWindow(int /*long*/ display) {
	lock.lock();
	try {
		return _XDefaultRootWindow(display);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native void _XFlush(int /*long*/ display);
public static final void XFlush(int /*long*/ display) {
	lock.lock();
	try {
		_XFlush(display);
	} finally {
		lock.unlock();
	}
}
/** @param address cast=(void *) */
public static final native void _XFree(int /*long*/ address);
public static final void XFree(int /*long*/ address) {
	lock.lock();
	try {
		_XFree(address);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param selection cast=(Atom)
 */
public static final native int /*long*/ _XGetSelectionOwner(int /*long*/ display, int /*long*/ selection);
public static final int /*long*/ XGetSelectionOwner(int /*long*/ display, int /*long*/ selection) {
	lock.lock();
	try {
		return _XGetSelectionOwner(display, selection);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param name cast=(char *)
 * @param ifExists cast=(Bool)
 */
public static final native int /*long*/ _XInternAtom(int /*long*/ display, byte[] name, boolean ifExists);
public static final int /*long*/ XInternAtom(int /*long*/ display, byte[] name, boolean ifExists) {
	lock.lock();
	try {
		return _XInternAtom(display, name, ifExists);
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
public static final native int _XQueryPointer(int /*long*/ display, int /*long*/ w, int /*long*/[] root_return, int /*long*/[] child_return, int[] root_x_return, int[] root_y_return, int[] win_x_return, int[] win_y_return, int[] mask_return);
public static final int XQueryPointer(int /*long*/ display, int /*long*/ w, int /*long*/[] root_return, int /*long*/[] child_return, int[] root_x_return, int[] root_y_return, int[] win_x_return, int[] win_y_return, int[] mask_return) {
	lock.lock();
	try {
		return _XQueryPointer(display, w, root_return, child_return, root_x_return, root_y_return, win_x_return, win_y_return, mask_return);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param w cast=(Window)
 * @param root_return cast=(Window *)
 * @param parent_return cast=(Window *)
 * @param children_return cast=(Window **)
 * @param nchildren_return cast=(unsigned int *)
 */
public static final native int _XQueryTree(int /*long*/ display, int /*long*/ w, int /*long*/[] root_return, int /*long*/[] parent_return, int /*long*/[] children_return, int[] nchildren_return);
public static final int XQueryTree(int /*long*/ display, int /*long*/ w, int /*long*/[] root_return, int /*long*/[] parent_return, int /*long*/[] children_return, int[] nchildren_return) {
	lock.lock();
	try {
		return _XQueryTree(display, w, root_return, parent_return, children_return, nchildren_return);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param keysym cast=(KeySym)
 */
public static final native int _XKeysymToKeycode(int /*long*/ display, int /*long*/ keysym);
public static final int XKeysymToKeycode(int /*long*/ display, int /*long*/ keysym) {
	lock.lock();
	try {
		return _XKeysymToKeycode(display, keysym);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param window cast=(Window)
 * @param num_prop_return cast=(int *)
 */
public static final native int /*long*/ _XListProperties(int /*long*/ display, int /*long*/ window, int[] num_prop_return);
public static final int /*long*/ XListProperties(int /*long*/ display, int /*long*/ window, int[] num_prop_return) {
	lock.lock();
	try {
		return _XListProperties(display, window, num_prop_return);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param window cast=(Window)
 * @param values flags=no_out
 */
public static final native int _XReconfigureWMWindow(int /*long*/ display, int /*long*/ window, int screen, int valueMask, XWindowChanges values);
public static final int XReconfigureWMWindow(int /*long*/ display, int /*long*/ window, int screen, int valueMask, XWindowChanges values) {
	lock.lock();
	try {
		return _XReconfigureWMWindow(display, window, screen, valueMask, values);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param w cast=(Window)
 * @param event_send cast=(XEvent *)
 */
public static final native int _XSendEvent(int /*long*/ display, int /*long*/ w, boolean propogate, int /*long*/ event_mask, int /*long*/ event_send);
public static final int XSendEvent(int /*long*/ display, int /*long*/ w, boolean propogate, int /*long*/ event_mask, int /*long*/ event_send) {
	lock.lock();
	try {
		return _XSendEvent(display, w, propogate, event_mask, event_send);
	} finally {
		lock.unlock();
	}
}
/** @param handler cast=(XIOErrorHandler) */
public static final native int /*long*/ _XSetIOErrorHandler(int /*long*/ handler);
public static final int /*long*/ XSetIOErrorHandler(int /*long*/ handler) {
	lock.lock();
	try {
		return _XSetIOErrorHandler(handler);
	} finally {
		lock.unlock();
	}
}
/** @param handler cast=(XErrorHandler) */
public static final native int /*long*/ _XSetErrorHandler(int /*long*/ handler);
public static final int /*long*/ XSetErrorHandler(int /*long*/ handler) {
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
public static final native int _XSetInputFocus(int /*long*/ display, int /*long*/ window, int revert, int time);
public static final int XSetInputFocus(int /*long*/ display, int /*long*/ window, int revert, int time) {
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
public static final native int _XSetTransientForHint(int /*long*/ display, int /*long*/ w, int /*long*/ prop_window);
public static final int XSetTransientForHint(int /*long*/ display, int /*long*/ w, int /*long*/ prop_window) {
	lock.lock();
	try {
		return _XSetTransientForHint(display, w, prop_window);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native int /*long*/ _XSynchronize(int /*long*/ display, boolean onoff);
public static final int /*long*/ XSynchronize(int /*long*/ display, boolean onoff) {
	lock.lock();
	try {
		return _XSynchronize(display, onoff);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param is_press cast=(Bool)
 * @param delay cast=(unsigned long)
 */
public static final native void _XTestFakeButtonEvent(int /*long*/ display, int button, boolean is_press, int /*long*/ delay);
public static final void XTestFakeButtonEvent(int /*long*/ display, int button, boolean is_press, int /*long*/ delay) {
	lock.lock();
	try {
		_XTestFakeButtonEvent(display, button, is_press, delay);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param is_press cast=(Bool)
 * @param delay cast=(unsigned long)
 */
public static final native void _XTestFakeKeyEvent(int /*long*/ display, int keycode, boolean is_press, int /*long*/ delay);
public static final void XTestFakeKeyEvent(int /*long*/ display, int keycode, boolean is_press, int /*long*/ delay) {
	lock.lock();
	try {
		_XTestFakeKeyEvent(display, keycode, is_press, delay);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param delay cast=(unsigned long)
 */
public static final native void _XTestFakeMotionEvent(int /*long*/ display, int screen_number, int x, int y, int /*long*/ delay);
public static final void XTestFakeMotionEvent(int /*long*/ display, int screen_number, int x, int y, int /*long*/ delay) {
	lock.lock();
	try {
		_XTestFakeMotionEvent(display, screen_number, x, y, delay);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param sourceWindow cast=(Window)
 * @param destWindow cast=(Window)
 */
public static final native int _XWarpPointer(int /*long*/ display, int /*long*/ sourceWindow, int /*long*/ destWindow, int sourceX, int sourceY, int sourceWidth, int sourceHeight, int destX, int destY);
public static final int XWarpPointer(int /*long*/ display, int /*long*/ sourceWindow, int /*long*/ destWindow, int sourceX, int sourceY, int sourceWidth, int sourceHeight, int destX, int destY) {
	lock.lock();
	try {
		return _XWarpPointer(display, sourceWindow, destWindow, sourceX, sourceY, sourceWidth, sourceHeight, destX, destY);
	} finally {
		lock.unlock();
	}
}
/** @param atom cast=(GdkAtom) */
public static final native int /*long*/ _gdk_x11_atom_to_xatom(int /*long*/ atom);
public static final int /*long*/ gdk_x11_atom_to_xatom(int /*long*/ atom) {
	lock.lock();
	try {
		return _gdk_x11_atom_to_xatom(atom);
	} finally {
		lock.unlock();
	}
}
/** @param colormap cast=(GdkColormap *) */
public static final native int /*long*/ _gdk_x11_colormap_get_xcolormap(int /*long*/ colormap);
public static final int /*long*/ gdk_x11_colormap_get_xcolormap(int /*long*/ colormap) {
	lock.lock();
	try {
		return _gdk_x11_colormap_get_xcolormap(colormap);
	} finally {
		lock.unlock();
	}
}
/** @param drawable cast=(GdkDrawable *) */
public static final native int /*long*/ _gdk_x11_drawable_get_xdisplay(int /*long*/ drawable);
public static final int /*long*/ gdk_x11_drawable_get_xdisplay(int /*long*/ drawable) {
	lock.lock();
	try {
		return _gdk_x11_drawable_get_xdisplay(drawable);
	} finally {
		lock.unlock();
	}
}
/** @param drawable cast=(GdkDrawable *) */
public static final native int /*long*/ _gdk_x11_drawable_get_xid(int /*long*/ drawable);
public static final int /*long*/ gdk_x11_drawable_get_xid(int /*long*/ drawable) {
	lock.lock();
	try {
		return _gdk_x11_drawable_get_xid(drawable);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param screen cast=(GdkScreen *)
 * @param xvisualid cast=(VisualID)
 */
public static final native int /*long*/ _gdk_x11_screen_lookup_visual(int /*long*/ screen, int xvisualid);
public static final int /*long*/ gdk_x11_screen_lookup_visual(int /*long*/ screen, int xvisualid) {
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
public static final native int /*long*/ _gdk_x11_screen_get_window_manager_name(int /*long*/ screen);
public static final int /*long*/ gdk_x11_screen_get_window_manager_name(int /*long*/ screen) {	
	lock.lock();
	try {
		return _gdk_x11_screen_get_window_manager_name(screen);
	} finally {
		lock.unlock();
	}
}
/** @param visual cast=(GdkVisual *) */
public static final native int /*long*/ _gdk_x11_visual_get_xvisual(int /*long*/ visual);
public static final int /*long*/ gdk_x11_visual_get_xvisual(int /*long*/ visual) {
	lock.lock();
	try {
		return _gdk_x11_visual_get_xvisual(visual);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gdk_pixmap_foreign_new(int /*long*/ anid);
public static final int /*long*/ gdk_pixmap_foreign_new(int /*long*/ anid) {
	lock.lock();
	try {
		return _gdk_pixmap_foreign_new(anid);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gdk_window_lookup(int /*long*/ xid);
public static final int /*long*/ gdk_window_lookup(int /*long*/ xid) {
	lock.lock();
	try {
		return _gdk_window_lookup(xid);
	} finally {
		lock.unlock();
	}
}
/**
 * @param window cast=(GdkWindow *)
 * @param function cast=(GdkFilterFunc)
 * @param data cast=(gpointer)
 */
public static final native void _gdk_window_add_filter(int /*long*/ window, int /*long*/ function, int /*long*/ data);
public static final void gdk_window_add_filter(int /*long*/ window, int /*long*/ function, int /*long*/ data) {
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
public static final native void _gdk_window_remove_filter(int /*long*/ window, int /*long*/ function, int /*long*/ data);
public static final void gdk_window_remove_filter(int /*long*/ window, int /*long*/ function, int /*long*/ data) {
	lock.lock();
	try {
		_gdk_window_remove_filter(window, function, data);
	} finally {
		lock.unlock();
	}
}
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param size cast=(size_t)
 */
public static final native void memmove(int /*long*/ dest, XButtonEvent src, int /*long*/ size);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param size cast=(size_t)
 */
public static final native void memmove(int /*long*/ dest, XClientMessageEvent src, int /*long*/ size);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param size cast=(size_t)
 */
public static final native void memmove(int /*long*/ dest, XCrossingEvent src, int /*long*/ size);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param size cast=(size_t)
 */
public static final native void memmove(int /*long*/ dest, XExposeEvent src, int /*long*/ size);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param size cast=(size_t)
 */
public static final native void memmove(int /*long*/ dest, XFocusChangeEvent src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(XButtonEvent dest, int /*long*/ src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(XCrossingEvent dest, int /*long*/ src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(XExposeEvent dest, int /*long*/ src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(XFocusChangeEvent dest, int /*long*/ src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(XVisibilityEvent dest, int /*long*/ src, int /*long*/ size);

/** X render natives and constants */
public static final int PictStandardARGB32 = 0;
public static final int PictStandardRGB24 = 1;
public static final int PictStandardA8 = 2;
public static final int PictStandardA4 = 3;
public static final int PictStandardA1 = 4;
public static final int PictOpSrc = 1;
public static final int PictOpOver = 3;

public static final native int XRenderPictureAttributes_sizeof();
/** @method flags=dynamic */
public static final native boolean _XRenderQueryExtension(int /*long*/ display, int[] event_basep, int[] error_basep);
public static final boolean XRenderQueryExtension(int /*long*/ display, int[] event_basep, int[] error_basep) {
	lock.lock();
	try {
		return _XRenderQueryExtension(display, event_basep, error_basep);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int _XRenderQueryVersion(int /*long*/ display, int[] major_versionp, int[] minor_versionp);
public static final int XRenderQueryVersion(int /*long*/ display, int[] major_versionp, int[] minor_versionp) {
	lock.lock();
	try {
		return _XRenderQueryVersion(display, major_versionp, minor_versionp);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param attributes flags=no_out
 */
public static final native int /*long*/ _XRenderCreatePicture(int /*long*/ display, int /*long*/ drawable, int /*long*/ format, int /*long*/ valuemask, XRenderPictureAttributes attributes);
public static final int /*long*/ XRenderCreatePicture(int /*long*/ display, int /*long*/ drawable, int /*long*/ format, int /*long*/ valuemask, XRenderPictureAttributes attributes) {
	lock.lock();
	try {
		return _XRenderCreatePicture(display, drawable, format, valuemask, attributes);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _XRenderSetPictureClipRectangles(int /*long*/ display, int /*long*/ picture, int xOrigin, int yOrigin, short[] rects, int count);
public static final void XRenderSetPictureClipRectangles(int /*long*/ display, int /*long*/ picture, int xOrigin, int yOrigin, short[] rects, int count) {
	lock.lock();
	try {
		_XRenderSetPictureClipRectangles(display, picture, xOrigin, yOrigin, rects, count);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _XRenderSetPictureTransform(int /*long*/ display, int /*long*/ picture, int[] transform);
public static final void XRenderSetPictureTransform(int /*long*/ display, int /*long*/ picture, int[] transform) {
	lock.lock();
	try {
		_XRenderSetPictureTransform(display, picture, transform);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _XRenderFreePicture(int /*long*/ display, int /*long*/ picture);
public static final void XRenderFreePicture(int /*long*/ display, int /*long*/ picture) {
	lock.lock();
	try {
		_XRenderFreePicture(display, picture);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _XRenderComposite(int /*long*/ display, int op, int /*long*/ src, int /*long*/ mask, int /*long*/ dst, int src_x, int src_y, int mask_x, int mask_y, int dst_x, int dst_y, int width, int height);
public static final void XRenderComposite(int /*long*/ display, int op, int /*long*/ src, int /*long*/ mask, int /*long*/ dst, int src_x, int src_y, int mask_x, int mask_y, int dst_x, int dst_y, int width, int height) {
	lock.lock();
	try {
		_XRenderComposite(display, op, src, mask, dst, src_x, src_y, mask_x, mask_y, dst_x, dst_y, width, height);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _XRenderFindStandardFormat(int /*long*/ display, int format);
public static final int /*long*/ XRenderFindStandardFormat(int /*long*/ display, int format) {
	lock.lock();
	try {
		return _XRenderFindStandardFormat(display, format);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _XRenderFindVisualFormat(int /*long*/ display, int /*long*/ visual);
public static final int /*long*/ XRenderFindVisualFormat(int /*long*/ display, int /*long*/ visual) {
	lock.lock();
	try {
		return _XRenderFindVisualFormat(display, visual);
	} finally {
		lock.unlock();
	}
}

/** Natives */
public static final native int Call (int /*long*/ func, int /*long*/ arg0, int arg1, int arg2);
public static final native long Call (int /*long*/ func, int /*long*/ arg0, int arg1, long arg2);
public static final native int /*long*/ _GDK_DISPLAY();
public static final int /*long*/ GDK_DISPLAY() {
	lock.lock();
	try {
		return _GDK_DISPLAY();
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _GDK_ROOT_PARENT();
public static final int /*long*/ GDK_ROOT_PARENT() {
	lock.lock();
	try {
		return _GDK_ROOT_PARENT();
	} finally {
		lock.unlock();
	}
}
/** @method flags=const */
public static final native int /*long*/ _GDK_TYPE_COLOR();
public static final int /*long*/ GDK_TYPE_COLOR() {
	lock.lock();
	try {
		return _GDK_TYPE_COLOR();
	} finally {
		lock.unlock();
	}
}
/** @method flags=const */
public static final native int /*long*/ _GDK_TYPE_PIXBUF();
public static final int /*long*/ GDK_TYPE_PIXBUF() {
	lock.lock();
	try {
		return _GDK_TYPE_PIXBUF();
	} finally {
		lock.unlock();
	}
}
public static final native boolean _GTK_IS_BUTTON(int /*long*/ obj);
public static final boolean GTK_IS_BUTTON(int /*long*/ obj) {
	lock.lock();
	try {
		return _GTK_IS_BUTTON(obj);
	} finally {
		lock.unlock();
	}
}
public static final native boolean _GTK_IS_WINDOW(int /*long*/ obj);
public static final boolean GTK_IS_WINDOW(int /*long*/ obj) {
	lock.lock();
	try {
		return _GTK_IS_WINDOW(obj);
	} finally {
		lock.unlock();
	}
}
public static final native boolean _GTK_IS_CELL_RENDERER_PIXBUF(int /*long*/ obj);
public static final boolean GTK_IS_CELL_RENDERER_PIXBUF(int /*long*/ obj) {
	lock.lock();
	try {
		return _GTK_IS_CELL_RENDERER_PIXBUF(obj);
	} finally {
		lock.unlock();
	}
}
public static final native boolean _GTK_IS_CELL_RENDERER_TEXT(int /*long*/ obj);
public static final boolean GTK_IS_CELL_RENDERER_TEXT(int /*long*/ obj) {
	lock.lock();
	try {
		return _GTK_IS_CELL_RENDERER_TEXT(obj);
	} finally {
		lock.unlock();
	}
}
public static final native boolean _GTK_IS_CELL_RENDERER_TOGGLE(int /*long*/ obj);
public static final boolean GTK_IS_CELL_RENDERER_TOGGLE(int /*long*/ obj) {
	lock.lock();
	try {
		return _GTK_IS_CELL_RENDERER_TOGGLE(obj);
	} finally {
		lock.unlock();
	}
}
public static final native boolean _GTK_IS_CONTAINER(int /*long*/ obj);
public static final boolean GTK_IS_CONTAINER(int /*long*/ obj) {
	lock.lock();
	try {
		return _GTK_IS_CONTAINER(obj);
	} finally {
		lock.unlock();
	}
}
public static final native boolean _GTK_IS_IMAGE_MENU_ITEM(int /*long*/ obj);
public static final boolean GTK_IS_IMAGE_MENU_ITEM(int /*long*/ obj) {
	lock.lock();
	try {
		return _GTK_IS_IMAGE_MENU_ITEM(obj);
	} finally {
		lock.unlock();
	}
}
public static final native boolean _GTK_IS_MENU_ITEM(int /*long*/ obj);
public static final boolean GTK_IS_MENU_ITEM(int /*long*/ obj) {
	lock.lock();
	try {
		return _GTK_IS_MENU_ITEM(obj);
	} finally {
		lock.unlock();
	}
}
public static final native boolean _GTK_IS_PLUG(int /*long*/ obj);
public static final boolean GTK_IS_PLUG(int /*long*/ obj) {
	lock.lock();
	try {
		return _GTK_IS_PLUG(obj);
	} finally {
		lock.unlock();
	}
}
public static final native boolean _GTK_IS_SOCKET(int /*long*/ obj);
public static final boolean GTK_IS_SOCKET(int /*long*/ obj) {
	lock.lock();
	try {
		return _GTK_IS_SOCKET(obj);
	} finally {
		lock.unlock();
	}
}
/** @method flags=const */
public static final native int /*long*/ _GTK_STOCK_CANCEL();
public static final int /*long*/ GTK_STOCK_CANCEL() {
	lock.lock();
	try {
		return _GTK_STOCK_CANCEL();
	} finally {
		lock.unlock();
	}
}
/** @method flags=const */
public static final native int /*long*/ _GTK_STOCK_OK();
public static final int /*long*/ GTK_STOCK_OK() {
	lock.lock();
	try {
		return _GTK_STOCK_OK();
	} finally {
		lock.unlock();
	}
}
/** @method flags=const */
public static final native int /*long*/ _GTK_TYPE_CELL_RENDERER_TEXT();
public static final int /*long*/ GTK_TYPE_CELL_RENDERER_TEXT() {
	lock.lock();
	try {
		return _GTK_TYPE_CELL_RENDERER_TEXT();
	} finally {
		lock.unlock();
	}
}
/** @method flags=const */
public static final native int /*long*/ _GTK_TYPE_CELL_RENDERER_PIXBUF();
public static final int /*long*/ GTK_TYPE_CELL_RENDERER_PIXBUF() {
	lock.lock();
	try {
		return _GTK_TYPE_CELL_RENDERER_PIXBUF();
	} finally {
		lock.unlock();
	}
}
/** @method flags=const */
public static final native int /*long*/ _GTK_TYPE_CELL_RENDERER_TOGGLE();
public static final int /*long*/ GTK_TYPE_CELL_RENDERER_TOGGLE() {
	lock.lock();
	try {
		return _GTK_TYPE_CELL_RENDERER_TOGGLE();
	} finally {
		lock.unlock();
	}
}
/** @method flags=const */
public static final native int /*long*/ _GTK_TYPE_FIXED();
public static final int /*long*/ GTK_TYPE_FIXED() {
	lock.lock();
	try {
		return _GTK_TYPE_FIXED();
	} finally {
		lock.unlock();
	}
}
/** @method flags=const */
public static final native int /*long*/ _GTK_TYPE_MENU();
public static final int /*long*/ GTK_TYPE_MENU() {
	lock.lock();
	try {
		return _GTK_TYPE_MENU();
	} finally {
		lock.unlock();
	}
}
/** @method flags=const */
public static final native int /*long*/ _GTK_TYPE_WIDGET();
public static final int /*long*/ GTK_TYPE_WIDGET() {
	lock.lock();
	try {
		return _GTK_TYPE_WIDGET();
	} finally {
		lock.unlock();
	}
}
public static final native int _GTK_WIDGET_FLAGS(int /*long*/ wid);
public static final int GTK_WIDGET_FLAGS(int /*long*/ wid) {
	lock.lock();
	try {
		return _GTK_WIDGET_FLAGS(wid);
	} finally {
		lock.unlock();
	}
}
public static final native int _GTK_WIDGET_STATE(int /*long*/ wid);
public static final int GTK_WIDGET_STATE(int /*long*/ wid) {
	lock.lock();
	try {
		return _GTK_WIDGET_STATE(wid);
	} finally {
		lock.unlock();
	}
}
public static final native boolean _GTK_WIDGET_HAS_DEFAULT(int /*long*/ wid);
public static final boolean GTK_WIDGET_HAS_DEFAULT(int /*long*/ wid) {
	lock.lock();
	try {
		return _GTK_WIDGET_HAS_DEFAULT(wid);
	} finally {
		lock.unlock();
	}
}
public static final native boolean _GTK_WIDGET_HAS_FOCUS(int /*long*/ wid);
public static final boolean GTK_WIDGET_HAS_FOCUS(int /*long*/ wid) {
	lock.lock();
	try {
		return _GTK_WIDGET_HAS_FOCUS(wid);
	} finally {
		lock.unlock();
	}
}
public static final native boolean _GTK_WIDGET_IS_SENSITIVE(int /*long*/ wid);
public static final boolean GTK_WIDGET_IS_SENSITIVE(int /*long*/ wid) {
	lock.lock();
	try {
		return _GTK_WIDGET_IS_SENSITIVE(wid);
	} finally {
		lock.unlock();
	}
}
public static final native boolean _GTK_WIDGET_MAPPED(int /*long*/ wid);
public static final boolean GTK_WIDGET_MAPPED(int /*long*/ wid) {
	lock.lock();
	try {
		return _GTK_WIDGET_MAPPED(wid);
	} finally {
		lock.unlock();
	}
}
public static final native boolean _GTK_WIDGET_SENSITIVE(int /*long*/ wid);
public static final boolean GTK_WIDGET_SENSITIVE(int /*long*/ wid) {
	lock.lock();
	try {
		return _GTK_WIDGET_SENSITIVE(wid);
	} finally {
		lock.unlock();
	}
}
public static final native void _GTK_WIDGET_SET_FLAGS(int /*long*/ wid, int flag);
public static final void GTK_WIDGET_SET_FLAGS(int /*long*/ wid, int flag) {
	lock.lock();
	try {
		_GTK_WIDGET_SET_FLAGS(wid, flag);
	} finally {
		lock.unlock();
	}
}
public static final native void _GTK_WIDGET_UNSET_FLAGS(int /*long*/ wid, int flag);
public static final void GTK_WIDGET_UNSET_FLAGS(int /*long*/ wid, int flag) {
	lock.lock();
	try {
		_GTK_WIDGET_UNSET_FLAGS(wid, flag);
	} finally {
		lock.unlock();
	}
}
public static final native boolean _GTK_WIDGET_VISIBLE(int /*long*/ wid);
public static final boolean GTK_WIDGET_VISIBLE(int /*long*/ wid) {
	lock.lock();
	try {
		return _GTK_WIDGET_VISIBLE(wid);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _G_OBJECT_CLASS (int /*long*/ klass);
public static final int /*long*/ G_OBJECT_CLASS (int /*long*/ klass) {
	lock.lock();
	try {
		return _G_OBJECT_CLASS(klass);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _G_OBJECT_GET_CLASS (int /*long*/ object);
public static final int /*long*/ G_OBJECT_GET_CLASS (int /*long*/ object) {
	lock.lock();
	try {
		return _G_OBJECT_GET_CLASS(object);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _G_OBJECT_TYPE_NAME (int /*long*/ object);
public static final int /*long*/ G_OBJECT_TYPE_NAME (int /*long*/ object) {
	lock.lock();
	try {
		return _G_OBJECT_TYPE_NAME(object);
	} finally {
		lock.unlock();
	}
}
/** @method flags=const */
public static final native int /*long*/ _G_TYPE_BOOLEAN();
public static final int /*long*/ G_TYPE_BOOLEAN() {
	lock.lock();
	try {
		return _G_TYPE_BOOLEAN();
	} finally {
		lock.unlock();
	}
}
/** @method flags=const */
public static final native int /*long*/ _G_TYPE_INT();
public static final int /*long*/ G_TYPE_INT() {
	lock.lock();
	try {
		return _G_TYPE_INT();
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _G_OBJECT_TYPE (int /*long*/ instance);
public static final int /*long*/ G_OBJECT_TYPE (int /*long*/ instance) {
	lock.lock();
	try {
		return _G_OBJECT_TYPE(instance);
	} finally {
		lock.unlock();
	}
}
/** @method flags=const */
public static final native int /*long*/ _G_TYPE_STRING();
public static final int /*long*/ G_TYPE_STRING() {
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
public static final native int /*long*/ _PANGO_TYPE_FONT_DESCRIPTION();
public static final int /*long*/ PANGO_TYPE_FONT_DESCRIPTION() {
	lock.lock();
	try {
		return _PANGO_TYPE_FONT_DESCRIPTION();
	} finally {
		lock.unlock();
	}
}
/** @method flags=const */
public static final native int /*long*/ _PANGO_TYPE_LAYOUT();
public static final int /*long*/ PANGO_TYPE_LAYOUT() {
	lock.lock();
	try {
		return _PANGO_TYPE_LAYOUT();
	} finally {
		lock.unlock();
	}
}
/** @param handle cast=(void *) */
public static final native int _dlclose(int /*long*/ handle);
public static final int dlclose(int /*long*/ handle) {
	lock.lock();
	try {
		return _dlclose(handle);
	} finally {
		lock.unlock();
	}
}
/** @param filename cast=(const char *) */
public static final native int /*long*/ _dlopen(byte[] filename, int flag);
public static final int /*long*/ dlopen(byte[] filename, int flag) {
	lock.lock();
	try {
		return _dlopen(filename, flag);
	} finally {
		lock.unlock();
	}
}
/**
 * @param handle cast=(void *)
 * @param symbol cast=(const char *)
 */
public static final native int /*long*/ _dlsym(int /*long*/ handle, byte[] symbol);
public static final int /*long*/ dlsym(int /*long*/ handle, byte[] symbol) {
	lock.lock();
	try {
		return _dlsym(handle, symbol);
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
public static final native int /*long*/ _g_signal_add_emission_hook(int signal_id, int detail, int /*long*/ hook_func, int /*long*/ hook_data, int /*long*/ data_destroy);
public static final int /*long*/ g_signal_add_emission_hook(int signal_id, int detail, int /*long*/ hook_func, int /*long*/ hook_data, int /*long*/ data_destroy) {
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
public static final native void _g_signal_remove_emission_hook(int signal_id, int /*long*/ hook_id);
public static final void g_signal_remove_emission_hook(int signal_id, int /*long*/ hook_id) {
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
public static final native int /*long*/ _g_cclosure_new(int /*long*/ callback_func, int /*long*/ user_data, int /*long*/ destroy_data);
public static final int /*long*/ g_cclosure_new(int /*long*/ callback_func, int /*long*/ user_data, int /*long*/ destroy_data) {
	lock.lock();
	try {
		return _g_cclosure_new(callback_func, user_data, destroy_data);
	} finally {
		lock.unlock();
	}
}
/** @param closure cast=(GClosure *) */
public static final native int /*long*/ _g_closure_ref(int /*long*/ closure);
public static final int /*long*/ g_closure_ref(int /*long*/ closure) {
	lock.lock();
	try {
		return _g_closure_ref(closure);
	} finally {
		lock.unlock();
	}
}
/** @param closure cast=(GClosure *) */
public static final native void _g_closure_unref(int /*long*/ closure);
public static final void g_closure_unref(int /*long*/ closure) {
	lock.lock();
	try {
		_g_closure_unref(closure);
	} finally {
		lock.unlock();
	}
}
/** @param context cast=(GMainContext *) */
public static final native boolean _g_main_context_acquire(int /*long*/ context);
public static final boolean g_main_context_acquire(int /*long*/ context) {
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
public static final native int _g_main_context_check(int /*long*/ context, int max_priority, int /*long*/ fds, int n_fds);
public static final int g_main_context_check(int /*long*/ context, int max_priority, int /*long*/ fds, int n_fds) {
	lock.lock();
	try {
		return _g_main_context_check(context, max_priority, fds, n_fds);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _g_main_context_default();
public static final int /*long*/ g_main_context_default() {
	lock.lock();
	try {
		return _g_main_context_default();
	} finally {
		lock.unlock();
	}
}
/** @param context cast=(GMainContext *) */
public static final native boolean _g_main_context_iteration(int /*long*/ context, boolean may_block);
public static final boolean g_main_context_iteration(int /*long*/ context, boolean may_block) {
	lock.lock();
	try {
		return _g_main_context_iteration(context, may_block);
	} finally {
		lock.unlock();
	}
}
/** @param context cast=(GMainContext *) */
public static final native boolean _g_main_context_pending(int /*long*/ context);
public static final boolean g_main_context_pending(int /*long*/ context) {
	lock.lock();
	try {
		return _g_main_context_pending(context);
	} finally {
		lock.unlock();
	}
}
/** @param context cast=(GMainContext *) */
public static final native int /*long*/ _g_main_context_get_poll_func(int /*long*/ context);
public static final int /*long*/ g_main_context_get_poll_func(int /*long*/ context) {
	lock.lock();
	try {
		return _g_main_context_get_poll_func(context);
	} finally {
		lock.unlock();
	}
}
/** @param context cast=(GMainContext *) */
public static final native boolean _g_main_context_prepare(int /*long*/ context, int[] priority);
public static final boolean g_main_context_prepare(int /*long*/ context, int[] priority) {
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
 */
public static final native int _g_main_context_query(int /*long*/ context, int max_priority, int[] timeout_, int /*long*/ fds, int n_fds);
public static final int g_main_context_query(int /*long*/ context, int max_priority, int[] timeout_, int /*long*/ fds, int n_fds) {
	lock.lock();
	try {
		return _g_main_context_query(context, max_priority, timeout_, fds, n_fds);
	} finally {
		lock.unlock();
	}
}
/** @param context cast=(GMainContext *) */
public static final native void _g_main_context_release(int /*long*/ context);
public static final void g_main_context_release(int /*long*/ context) {
	lock.lock();
	try {
		_g_main_context_release(context);
	} finally {
		lock.unlock();
	}
}
/** @param context cast=(GMainContext *) */
public static final native void g_main_context_wakeup(int /*long*/ context);
/**
 * @param opsysstring cast=(const gchar *)
 * @param len cast=(gssize)
 * @param bytes_read cast=(gsize *)
 * @param bytes_written cast=(gsize *)
 * @param error cast=(GError **)
 */
public static final native int /*long*/ _g_filename_to_utf8(int /*long*/ opsysstring, int /*long*/ len, int /*long*/[] bytes_read, int /*long*/[] bytes_written, int /*long*/[] error);
public static final int /*long*/ g_filename_to_utf8(int /*long*/ opsysstring, int /*long*/ len, int /*long*/[] bytes_read, int /*long*/[] bytes_written, int /*long*/[] error) {
	lock.lock();
	try {
		return _g_filename_to_utf8(opsysstring, len, bytes_read, bytes_written, error);
	} finally {
		lock.unlock();
	}
}
/**
 * @param filename cast=(const char *)
 * @param hostname cast=(const char *)
 * @param error cast=(GError **)
 */
public static final native int /*long*/ _g_filename_to_uri(int /*long*/ filename, int /*long*/ hostname, int /*long*/[] error);
public static final int /*long*/ g_filename_to_uri(int /*long*/ filename, int /*long*/ hostname, int /*long*/[] error) {
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
public static final native int /*long*/ _g_filename_from_utf8(int /*long*/ opsysstring, int /*long*/ len,  int /*long*/[] bytes_read, int /*long*/[] bytes_written, int /*long*/[] error);
public static final int /*long*/ g_filename_from_utf8(int /*long*/ opsysstring, int /*long*/ len,  int /*long*/[] bytes_read, int /*long*/[] bytes_written, int /*long*/[] error) {
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
public static final native int /*long*/ _g_filename_from_uri(int /*long*/ uri, int /*long*/[] hostname, int /*long*/[] error);
public static final int /*long*/ g_filename_from_uri(int /*long*/ uri, int /*long*/[] hostname, int /*long*/[] error) {
	lock.lock();
	try {
		return _g_filename_from_uri(uri, hostname, error);
	} finally {
		lock.unlock();
	}
}
/** @param mem cast=(gpointer) */
public static final native void _g_free(int /*long*/ mem);
public static final void g_free(int /*long*/ mem) {
	lock.lock();
	try {
		_g_free(mem);
	} finally {
		lock.unlock();
	}
}
/**
 * @param function cast=(GSourceFunc)
 * @param data cast=(gpointer)
 */
public static final native int _g_idle_add(int /*long*/ function, int /*long*/ data);
public static final int g_idle_add(int /*long*/ function, int /*long*/ data) {
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
public static final native int /*long*/ _g_list_append(int /*long*/ list, int /*long*/ data);
public static final int /*long*/ g_list_append(int /*long*/ list, int /*long*/ data) {
	lock.lock();
	try {
		return _g_list_append(list, data);
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(GList *) */
public static final native int /*long*/ _g_list_data(int /*long*/ list);
public static final int /*long*/ g_list_data(int /*long*/ list) {
	lock.lock();
	try {
		return _g_list_data(list);
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(GList *) */
public static final native void _g_list_free(int /*long*/ list);
public static final void g_list_free(int /*long*/ list) {
	lock.lock();
	try {
		_g_list_free(list);
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(GList *) */
public static final native void _g_list_free_1(int /*long*/ list);
public static final void g_list_free_1(int /*long*/ list) {
	lock.lock();
	try {
		_g_list_free_1(list);
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(GList *) */
public static final native int _g_list_length(int /*long*/ list);
public static final int g_list_length(int /*long*/ list) {
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
public static final native void _g_list_set_next(int /*long*/ list, int /*long*/ llist);
public static final void g_list_set_next(int /*long*/ list, int /*long*/ llist) {
	lock.lock();
	try {
		_g_list_set_next(list, llist);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _g_list_next(int /*long*/ list);
public static final int /*long*/ g_list_next(int /*long*/ list) {
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
public static final native int /*long*/ _g_list_nth(int /*long*/ list, int n);
public static final int /*long*/ g_list_nth(int /*long*/ list, int n) {
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
public static final native int /*long*/ _g_list_nth_data(int /*long*/ list, int n);
public static final int /*long*/ g_list_nth_data(int /*long*/ list, int n) {
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
public static final native int /*long*/ _g_list_prepend(int /*long*/ list, int /*long*/ data);
public static final int /*long*/ g_list_prepend(int /*long*/ list, int /*long*/ data) {
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
public static final native void _g_list_set_previous(int /*long*/ list, int /*long*/ llist);
public static final void g_list_set_previous(int /*long*/ list, int /*long*/ llist) {
	lock.lock();
	try {
		_g_list_set_previous(list, llist);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _g_list_previous(int /*long*/ list);
public static final int /*long*/ g_list_previous(int /*long*/ list) {
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
public static final native int /*long*/ _g_list_remove_link(int /*long*/ list, int /*long*/ link);
public static final int /*long*/ g_list_remove_link(int /*long*/ list, int /*long*/ link) {
	lock.lock();
	try {
		return _g_list_remove_link(list, link);
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(GList *) */
public static final native int /*long*/ _g_list_reverse(int /*long*/ list);
public static final int /*long*/ g_list_reverse(int /*long*/ list) {
	lock.lock();
	try {
		return _g_list_reverse(list);
	} finally {
		lock.unlock();
	}
}
/**
 * @param utf8string cast=(const gchar *)
 * @param len cast=(gssize)
 * @param bytes_read cast=(gsize *)
 * @param bytes_written cast=(gsize *)
 * @param error cast=(GError **)
 */
public static final native int /*long*/ _g_locale_from_utf8(int /*long*/ utf8string, int /*long*/ len, int /*long*/[] bytes_read, int /*long*/[] bytes_written, int /*long*/[] error);
public static final int /*long*/ g_locale_from_utf8(int /*long*/ utf8string, int /*long*/ len, int /*long*/[] bytes_read, int /*long*/[] bytes_written, int /*long*/[] error) {
	lock.lock();
	try {
		return _g_locale_from_utf8(utf8string, len, bytes_read, bytes_written, error);
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
public static final native int /*long*/ _g_locale_to_utf8(int /*long*/ opsysstring, int /*long*/ len, int /*long*/[] bytes_read, int /*long*/[] bytes_written, int /*long*/[] error);
public static final int /*long*/ g_locale_to_utf8(int /*long*/ opsysstring, int /*long*/ len, int /*long*/[] bytes_read, int /*long*/[] bytes_written, int /*long*/[] error) {
	lock.lock();
	try {
		return _g_locale_to_utf8(opsysstring, len, bytes_read, bytes_written, error);
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
public static final native void _g_log_default_handler(int /*long*/ log_domain, int log_levels, int /*long*/ message, int /*long*/ unused_data);
public static final void g_log_default_handler(int /*long*/ log_domain, int log_levels, int /*long*/ message, int /*long*/ unused_data) {
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
public static final native int _g_log_set_handler(byte[] log_domain, int log_levels, int /*long*/ log_func, int /*long*/ user_data);
public static final int g_log_set_handler(byte[] log_domain, int log_levels, int /*long*/ log_func, int /*long*/ user_data) {
	lock.lock();
	try {
		return _g_log_set_handler(log_domain, log_levels, log_func, user_data);
	} finally {
		lock.unlock();
	}
}
/** @param size cast=(gulong) */
public static final native int /*long*/ _g_malloc(int /*long*/ size);
public static final int /*long*/ g_malloc(int /*long*/ size) {
	lock.lock();
	try {
		return _g_malloc(size);
	} finally {
		lock.unlock();
	}
}
/**
 * @param object cast=(GObject *)
 * @param first_property_name cast=(const gchar *),flags=no_out
 * @param terminator cast=(const gchar *),flags=sentinel
 */
public static final native void _g_object_get(int /*long*/ object, byte[] first_property_name, int[] value, int /*long*/ terminator);
public static final void g_object_get(int /*long*/ object, byte[] first_property_name, int[] value, int /*long*/ terminator) {
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
public static final native int /*long*/ _g_object_get_qdata(int /*long*/ object, int quark);
public static final int /*long*/ g_object_get_qdata(int /*long*/ object, int quark) {
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
public static final native int /*long*/ _g_object_new (int /*long*/ type, int /*long*/ first_property_name);
public static final int /*long*/ g_object_new (int /*long*/ type, int /*long*/ first_property_name) {
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
public static final native void _g_object_notify (int /*long*/ object, byte[] property_name);
public static final void g_object_notify (int /*long*/ object, byte[] property_name) {
	lock.lock(); 
	try {
		_g_object_notify(object, property_name);
	} finally {
		lock.unlock();
	}
}
/** @param object cast=(gpointer) */
public static final native int /*long*/ _g_object_ref(int /*long*/ object);
public static final int /*long*/ g_object_ref(int /*long*/ object) {
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
public static final native void _g_object_set(int /*long*/ object, byte[] first_property_name, boolean data, int /*long*/ terminator);
public static final void g_object_set(int /*long*/ object, byte[] first_property_name, boolean data, int /*long*/ terminator) {
	lock.lock();
	try {
		_g_object_set(object, first_property_name, data, terminator);
	} finally {
		lock.unlock();
	}
}
/**
 * @param object cast=(gpointer)
 * @param first_property_name cast=(const gchar *)
 * @param terminator cast=(const gchar *),flags=sentinel
 */
public static final native void _g_object_set(int /*long*/ object, byte[] first_property_name, GdkColor data, int /*long*/ terminator);
public static final void g_object_set(int /*long*/ object, byte[] first_property_name, GdkColor data, int /*long*/ terminator) {
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
public static final native void _g_object_set(int /*long*/ object, byte[] first_property_name, int data, int /*long*/ terminator);
public static final void g_object_set(int /*long*/ object, byte[] first_property_name, int data, int /*long*/ terminator) {
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
public static final native void _g_object_set(int /*long*/ object, byte[] first_property_name, float data, int /*long*/ terminator);
public static final void g_object_set(int /*long*/ object, byte[] first_property_name, float data, int /*long*/ terminator) {
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
public static final native void _g_object_set(int /*long*/ object, byte[] first_property_name, long data, int /*long*/ terminator);
public static final void g_object_set(int /*long*/ object, byte[] first_property_name, long data, int /*long*/ terminator) {
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
public static final native void _g_object_set_qdata(int /*long*/ object, int quark, int /*long*/ data);
public static final void g_object_set_qdata(int /*long*/ object, int quark, int /*long*/ data) {
	lock.lock();
	try {
		_g_object_set_qdata(object, quark, data);
	} finally {
		lock.unlock();
	}
}
/** @param object cast=(gpointer) */
public static final native void _g_object_unref(int /*long*/ object);
public static final void g_object_unref(int /*long*/ object) {
	lock.lock();
	try {
		_g_object_unref(object);
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
public static final native int _g_signal_connect(int /*long*/ instance, byte[] detailed_signal, int /*long*/ proc, int /*long*/ data);
public static final int g_signal_connect(int /*long*/ instance, byte[] detailed_signal, int /*long*/ proc, int /*long*/ data) {
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
public static final native int _g_signal_connect_closure(int /*long*/ instance, byte[] detailed_signal, int /*long*/ closure, boolean after);
public static final int g_signal_connect_closure(int /*long*/ instance, byte[] detailed_signal, int /*long*/ closure, boolean after) {
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
public static final native int _g_signal_connect_closure_by_id(int /*long*/ instance, int signal_id, int detail, int /*long*/ closure, boolean after);
public static final int g_signal_connect_closure_by_id(int /*long*/ instance, int signal_id, int detail, int /*long*/ closure, boolean after) {
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
 * @param proc cast=(GCallback)
 * @param data cast=(gpointer)
 */
public static final native int _g_signal_connect_after(int /*long*/ instance, byte[] detailed_signal, int /*long*/ proc, int /*long*/ data);
public static final int g_signal_connect_after(int /*long*/ instance, byte[] detailed_signal, int /*long*/ proc, int /*long*/ data) {
	lock.lock();
	try {
		return _g_signal_connect_after(instance, detailed_signal, proc, data);
	} finally {
		lock.unlock();
	}
}
/**
 * @param instance cast=(gpointer)
 * @param detailed_signal cast=(const gchar *),flags=no_out
 */
public static final native void _g_signal_emit_by_name(int /*long*/ instance, byte[] detailed_signal);
public static final void g_signal_emit_by_name(int /*long*/ instance, byte[] detailed_signal) {
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
public static final native void _g_signal_emit_by_name(int /*long*/ instance, byte[] detailed_signal, int /*long*/ data);
public static final void g_signal_emit_by_name(int /*long*/ instance, byte[] detailed_signal, int /*long*/ data) {
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
public static final native void _g_signal_emit_by_name(int /*long*/ instance, byte[] detailed_signal, int /*long*/ data1, int /*long*/ data2);
public static final void g_signal_emit_by_name(int /*long*/ instance, byte[] detailed_signal, int /*long*/ data1, int /*long*/ data2) {
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
public static final native void _g_signal_emit_by_name(int /*long*/ instance, byte[] detailed_signal, byte [] data);
public static final void g_signal_emit_by_name(int /*long*/ instance, byte[] detailed_signal, byte [] data) {
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
public static final native void _g_signal_handler_disconnect(int /*long*/ instance, int handler_id);
public static final void g_signal_handler_disconnect(int /*long*/ instance, int handler_id) {
	lock.lock();
	try {
		_g_signal_handler_disconnect(instance, handler_id);
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
public static final native int _g_signal_handlers_block_matched(int /*long*/ instance, int mask, int signal_id, int detail, int /*long*/ closure, int /*long*/ func, int /*long*/ data);
public static final int g_signal_handlers_block_matched(int /*long*/ instance, int mask, int signal_id, int detail, int /*long*/ closure, int /*long*/ func, int /*long*/ data) {
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
public static final native int _g_signal_handlers_disconnect_matched(int /*long*/ instance, int mask, int signal_id, int detail, int /*long*/ closure, int /*long*/ func, int /*long*/ data);
public static final int g_signal_handlers_disconnect_matched(int /*long*/ instance, int mask, int signal_id, int detail, int /*long*/ closure, int /*long*/ func, int /*long*/ data) {
	lock.lock();
	try {
		return _g_signal_handlers_disconnect_matched(instance, mask, signal_id, detail, closure, func, data);
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
public static final native int _g_signal_handlers_unblock_matched(int /*long*/ instance, int mask, int signal_id, int detail, int /*long*/ closure, int /*long*/ func, int /*long*/ data);
public static final int g_signal_handlers_unblock_matched(int /*long*/ instance, int mask, int signal_id, int detail, int /*long*/ closure, int /*long*/ func, int /*long*/ data) {
	lock.lock();
	try {
		return _g_signal_handlers_unblock_matched(instance, mask, signal_id, detail, closure, func, data);
	} finally {
		lock.unlock();
	}
}
/** @param name cast=(const gchar *),flags=no_out */
public static final native int _g_signal_lookup (byte[] name, int /*long*/ itype);
public static final int g_signal_lookup (byte[] name, int /*long*/ itype) {
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
public static final native void _g_signal_stop_emission_by_name(int /*long*/ instance, byte[] detailed_signal);
public static final void g_signal_stop_emission_by_name(int /*long*/ instance, byte[] detailed_signal) {
	lock.lock();
	try {
		_g_signal_stop_emission_by_name(instance, detailed_signal);
	} finally {
		lock.unlock();
	}
}
/** @param tag cast=(guint) */
public static final native boolean /*long*/ _g_source_remove (int /*long*/ tag);
public static final boolean /*long*/ g_source_remove (int /*long*/ tag) {
	lock.lock();
	try {
		return _g_source_remove(tag);
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(GSList *) */
public static final native int /*long*/ _g_slist_data (int /*long*/ list);
public static final int /*long*/ g_slist_data (int /*long*/ list) {
	lock.lock();
	try {
		return _g_slist_data(list);
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(GSList *) */
public static final native void _g_slist_free (int /*long*/ list);
public static final void g_slist_free (int /*long*/ list) {
	lock.lock();
	try {
		_g_slist_free(list);
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(GSList *) */
public static final native int /*long*/ _g_slist_next (int /*long*/ list);
public static final int /*long*/ g_slist_next (int /*long*/ list) {
	lock.lock();
	try {
		return _g_slist_next(list);
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(GSList *) */
public static final native int _g_slist_length (int /*long*/ list);
public static final int g_slist_length (int /*long*/ list) {
	lock.lock();
	try {
		return _g_slist_length(list);
	} finally {
		lock.unlock();
	}
}
/** @param string_array cast=(gchar **) */
public static final native void _g_strfreev(int /*long*/ string_array);
public static final void g_strfreev(int /*long*/ string_array) {
	lock.lock();
	try {
		_g_strfreev(string_array);
	} finally {
		lock.unlock();
	}
}
/**
 * @param str cast=(const gchar *)
 * @param endptr cast=(gchar **)
 */
public static final native double _g_strtod(int /*long*/ str, int /*long*/[] endptr);
public static final double g_strtod(int /*long*/ str, int /*long*/[] endptr) {
	lock.lock();
	try {
		return _g_strtod(str, endptr);
	} finally {
		lock.unlock();
	}
}
/**
 * @param instance_type cast=(GType)
 * @param interface_type cast=(GType)
 * @param info cast=(const GInterfaceInfo *)
 */
public static final native void _g_type_add_interface_static (int /*long*/ instance_type, int /*long*/ interface_type, int /*long*/ info);
public static final void g_type_add_interface_static (int /*long*/ instance_type, int /*long*/ interface_type, int /*long*/ info) {
	lock.lock();
	try {
		_g_type_add_interface_static(instance_type, interface_type, info);
	} finally {
		lock.unlock();
	}
}
/** @param g_class cast=(GType) */
public static final native int /*long*/ _g_type_class_peek (int /*long*/ g_class);
public static final int /*long*/ g_type_class_peek (int /*long*/ g_class) {
	lock.lock();
	try {
		return _g_type_class_peek(g_class);
	} finally {
		lock.unlock();
	}
}
/** @param g_class cast=(gpointer) */
public static final native int /*long*/ _g_type_class_peek_parent (int /*long*/ g_class);
public static final int /*long*/ g_type_class_peek_parent (int /*long*/ g_class) {
	lock.lock();
	try {
		return _g_type_class_peek_parent(g_class);
	} finally {
		lock.unlock();
	}
}
/** @param g_class cast=(GType) */
public static final native int /*long*/ _g_type_class_ref (int /*long*/ g_class);
public static final int /*long*/ g_type_class_ref (int /*long*/ g_class) {
	lock.lock();
	try {
		return _g_type_class_ref(g_class);
	} finally {
		lock.unlock();
	}
}
/** @param g_class cast=(gpointer) */
public static final native void _g_type_class_unref (int /*long*/ g_class);
public static final void g_type_class_unref (int /*long*/ g_class) {
	lock.lock();
	try {
		_g_type_class_unref(g_class);
	} finally {
		lock.unlock();
	}
}
/** @param name cast=(const gchar *) */
public static final native int /*long*/ _g_type_from_name (byte[] name);
public static final int /*long*/ g_type_from_name (byte[] name) {
	lock.lock();
	try {
		return _g_type_from_name(name);
	} finally {
		lock.unlock();
	}
}
/** @param iface cast=(gpointer) */
public static final native int /*long*/ _g_type_interface_peek_parent (int /*long*/ iface);
public static final int /*long*/ g_type_interface_peek_parent (int /*long*/ iface) {
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
public static final native boolean _g_type_is_a (int /*long*/ type, int /*long*/ is_a_type);
public static final boolean g_type_is_a (int /*long*/ type, int /*long*/ is_a_type) {
	lock.lock();
	try {
		return _g_type_is_a(type, is_a_type);
	} finally {
		lock.unlock();
	}
}
/** @param handle cast=(GType) */
public static final native int /*long*/ _g_type_name (int /*long*/ handle);
public static final int /*long*/ g_type_name (int /*long*/ handle) {
	lock.lock();
	try {
		return _g_type_name(handle);
	} finally {
		lock.unlock();
	}
}
/** @param type cast=(GType) */
public static final native int /*long*/ _g_type_parent (int /*long*/ type);
public static final int /*long*/ g_type_parent (int /*long*/ type) {
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
public static final native void _g_type_query (int /*long*/ type, int /*long*/ query);
public static final void g_type_query (int /*long*/ type, int /*long*/ query) {
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
public static final native int /*long*/ _g_type_register_static (int /*long*/ parent_type, byte[] type_name, int /*long*/ info, int flags);
public static final int /*long*/ g_type_register_static (int /*long*/ parent_type, byte[] type_name, int /*long*/ info, int flags) {
	lock.lock();
	try {
		return _g_type_register_static(parent_type, type_name, info, flags);
	} finally {
		lock.unlock();
	}
}
/** @param vtable cast=(GThreadFunctions *) */
public static final native void _g_thread_init(int /*long*/ vtable);
public static final void g_thread_init(int /*long*/ vtable) {
	lock.lock();
	try {
		_g_thread_init(vtable);
	} finally {
		lock.unlock();
	}
}
public static final native boolean _g_thread_supported();
public static final boolean g_thread_supported() {
	lock.lock();
	try {
		return _g_thread_supported();
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
public static final native int /*long*/ _g_utf16_to_utf8(char[] str, int /*long*/ len, int /*long*/[] items_read, int /*long*/[] items_written, int /*long*/[] error);
public static final int /*long*/ g_utf16_to_utf8(char[] str, int /*long*/ len, int /*long*/[] items_read, int /*long*/[] items_written, int /*long*/[] error) {
	lock.lock();
	try {
		return _g_utf16_to_utf8(str, len, items_read, items_written, error);
	} finally {
		lock.unlock();
	}
}
/** @param str cast=(const gchar *) */
public static final native int /*long*/ _g_utf8_offset_to_pointer(int /*long*/ str, int /*long*/ offset);
public static final int /*long*/ g_utf8_offset_to_pointer(int /*long*/ str, int /*long*/ offset) {
	lock.lock();
	try {
		return _g_utf8_offset_to_pointer(str, offset);
	} finally {
		lock.unlock();
	}
}
/**
 * @param str cast=(const gchar *)
 * @param pos cast=(const gchar *)
 */
public static final native int /*long*/ _g_utf8_pointer_to_offset(int /*long*/ str, int /*long*/ pos);
public static final int /*long*/ g_utf8_pointer_to_offset(int /*long*/ str, int /*long*/ pos) {
	lock.lock();
	try {
		return _g_utf8_pointer_to_offset(str, pos);
	} finally {
		lock.unlock();
	}
}
/** @param str cast=(const gchar *) */
public static final native int /*long*/ _g_utf8_strlen(int /*long*/ str, int /*long*/ max);
public static final int /*long*/ g_utf8_strlen(int /*long*/ str, int /*long*/ max) {
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
public static final native int /*long*/ _g_utf8_to_utf16(byte[] str, int /*long*/ len, int /*long*/[] items_read, int /*long*/[] items_written, int /*long*/[] error);
public static final int /*long*/ g_utf8_to_utf16(byte[] str, int /*long*/ len, int /*long*/[] items_read, int /*long*/[] items_written, int /*long*/[] error) {
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
public static final native int /*long*/ _g_utf8_to_utf16(int /*long*/ str, int /*long*/ len, int /*long*/[] items_read, int /*long*/[] items_written, int /*long*/[] error);
public static final int /*long*/ g_utf8_to_utf16(int /*long*/ str, int /*long*/ len, int /*long*/[] items_read, int /*long*/[] items_written, int /*long*/[] error) {
	lock.lock();
	try {
		return _g_utf8_to_utf16(str, len, items_read, items_written, error);
	} finally {
		lock.unlock();
	}
}
/** @param value cast=(const GValue *) */
public static final native int /*long*/ _g_value_peek_pointer (int /*long*/ value);
public static final  int /*long*/ g_value_peek_pointer (int /*long*/ value) {
	lock.lock();
	try {
		return _g_value_peek_pointer(value);
	} finally {
		lock.unlock();
	}
}
/** @param atom_name cast=(const gchar *),flags=no_out critical */
public static final native int /*long*/ _gdk_atom_intern(byte[] atom_name, boolean only_if_exists);
public static final int /*long*/ gdk_atom_intern(byte[] atom_name, boolean only_if_exists) {
	lock.lock();
	try {
		return _gdk_atom_intern(atom_name, only_if_exists);
	} finally {
		lock.unlock();
	}
}
/** @param atom cast=(GdkAtom) */
public static final native int /*long*/ _gdk_atom_name(int /*long*/ atom);
public static final int /*long*/ gdk_atom_name(int /*long*/ atom) {
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
 * @param window cast=(GdkWindow *)
 * @param data cast=(const gchar *),flags=no_out critical
 * @param width cast=(gint)
 * @param height cast=(gint)
 */
public static final native int /*long*/ _gdk_bitmap_create_from_data(int /*long*/ window, byte[] data, int width, int height);
public static final int /*long*/ gdk_bitmap_create_from_data(int /*long*/ window, byte[] data, int width, int height) {
	lock.lock();
	try {
		return _gdk_bitmap_create_from_data(window, data, width, height);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _gdk_cairo_create(int /*long*/ drawable);
public static final int /*long*/ gdk_cairo_create(int /*long*/ drawable) {
	lock.lock();
	try {
		return _gdk_cairo_create(drawable);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _gdk_cairo_region(int /*long*/ cairo, int /*long*/ region);
public static final void gdk_cairo_region(int /*long*/ cairo, int /*long*/ region) {
	lock.lock();
	try {
		_gdk_cairo_region(cairo, region);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _gdk_cairo_set_source_color(int /*long*/ cairo, GdkColor color);
public static final void gdk_cairo_set_source_color(int /*long*/ cairo, GdkColor color) {
	lock.lock();
	try {
		_gdk_cairo_set_source_color(cairo, color);
	} finally {
		lock.unlock();
	}
}
/**
 * @param colormap cast=(GdkColormap *)
 * @param color cast=(GdkColor *),flags=no_in
 */
public static final native boolean _gdk_color_white(int /*long*/ colormap, GdkColor color);
public static final boolean gdk_color_white(int /*long*/ colormap, GdkColor color) {
	lock.lock();
	try {
		return _gdk_color_white(colormap, color);
	} finally {
		lock.unlock();
	}
}
/**
 * @param colormap cast=(GdkColormap *)
 * @param color cast=(GdkColor *)
 * @param writeable cast=(gboolean)
 * @param best_match cast=(gboolean)
 */
public static final native boolean _gdk_colormap_alloc_color(int /*long*/ colormap, GdkColor color, boolean writeable, boolean best_match);
public static final boolean gdk_colormap_alloc_color(int /*long*/ colormap, GdkColor color, boolean writeable, boolean best_match) {
	lock.lock();
	try {
		return _gdk_colormap_alloc_color(colormap, color, writeable, best_match);
	} finally {
		lock.unlock();
	}
}
/**
 * @param colormap cast=(GdkColormap *)
 * @param colors cast=(GdkColor *),flags=no_out
 * @param ncolors cast=(gint)
 */
public static final native void _gdk_colormap_free_colors(int /*long*/ colormap, GdkColor colors, int ncolors);
public static final void gdk_colormap_free_colors(int /*long*/ colormap, GdkColor colors, int ncolors) {
	lock.lock();
	try {
		_gdk_colormap_free_colors(colormap, colors, ncolors);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gdk_colormap_get_system();
public static final int /*long*/ gdk_colormap_get_system() {
	lock.lock();
	try {
		return _gdk_colormap_get_system();
	} finally {
		lock.unlock();
	}
}
/**
 * @param colormap cast=(GdkColormap *)
 * @param pixel cast=(gulong)
 * @param result cast=(GdkColor *)
 */
public static final native void _gdk_colormap_query_color(int /*long*/ colormap, int /*long*/ pixel, GdkColor result);
public static final void gdk_colormap_query_color(int /*long*/ colormap, int /*long*/ pixel, GdkColor result) {
	lock.lock();
	try {
		_gdk_colormap_query_color(colormap, pixel, result);
	} finally {
		lock.unlock();
	}
}
/** @param cursor cast=(GdkCursor *) */
public static final native void _gdk_cursor_destroy(int /*long*/ cursor);
public static final void gdk_cursor_destroy(int /*long*/ cursor) {
	lock.lock();
	try {
		_gdk_cursor_destroy(cursor);
	} finally {
		lock.unlock();
	}
}
/** @param cursor_type cast=(GdkCursorType) */
public static final native int /*long*/ _gdk_cursor_new(int /*long*/ cursor_type);
public static final int /*long*/ gdk_cursor_new(int /*long*/ cursor_type) {
	lock.lock();
	try {
		return _gdk_cursor_new(cursor_type);
	} finally {
		lock.unlock();
	}
}
/**
 * @param source cast=(GdkPixmap *)
 * @param mask cast=(GdkPixmap *)
 * @param fg cast=(GdkColor *),flags=no_out
 * @param bg cast=(GdkColor *),flags=no_out
 * @param x cast=(gint)
 * @param y cast=(gint)
 */
public static final native int /*long*/ _gdk_cursor_new_from_pixmap(int /*long*/ source, int /*long*/ mask, GdkColor fg, GdkColor bg, int x, int y);
public static final int /*long*/ gdk_cursor_new_from_pixmap(int /*long*/ source, int /*long*/ mask, GdkColor fg, GdkColor bg, int x, int y) {
	lock.lock();
	try {
		return _gdk_cursor_new_from_pixmap(source, mask, fg, bg, x, y);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _gdk_cursor_new_from_pixbuf(int /*long*/ display, int /*long*/ pixbuf, int x, int y);
public static final int /*long*/ gdk_cursor_new_from_pixbuf(int /*long*/ display, int /*long*/ pixbuf, int x, int y) {
	lock.lock();
	try {
		return _gdk_cursor_new_from_pixbuf(display, pixbuf, x, y);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _gdk_display_get_default();
public static final int /*long*/ gdk_display_get_default() {
	lock.lock();
	try {
		return _gdk_display_get_default();
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native boolean _gdk_display_supports_cursor_color(int /*long*/ display);
public static final boolean gdk_display_supports_cursor_color(int /*long*/ display) {
	lock.lock();
	try {
		return _gdk_display_supports_cursor_color(display);
	} finally {
		lock.unlock();
	}
}
/**
 * @param context cast=(GdkDragContext *)
 * @param action cast=(GdkDragAction)
 * @param time cast=(guint32)
 */
public static final native void _gdk_drag_status(int /*long*/ context, int action, int time);
public static final void gdk_drag_status(int /*long*/ context, int action, int time) {
	lock.lock();
	try {
		_gdk_drag_status(context, action, time);
	} finally {
		lock.unlock();
	}
}
/**
 * @param drawable cast=(GdkDrawable *)
 * @param gc cast=(GdkGC *)
 * @param filled cast=(gint)
 * @param x cast=(gint)
 * @param y cast=(gint)
 * @param width cast=(gint)
 * @param height cast=(gint)
 * @param angle1 cast=(gint)
 * @param angle2 cast=(gint)
 */
public static final native void _gdk_draw_arc(int /*long*/ drawable, int /*long*/ gc, int filled, int x, int y, int width, int height, int angle1, int angle2);
public static final void gdk_draw_arc(int /*long*/ drawable, int /*long*/ gc, int filled, int x, int y, int width, int height, int angle1, int angle2) {
	lock.lock();
	try {
		_gdk_draw_arc(drawable, gc, filled, x, y, width, height, angle1, angle2);
	} finally {
		lock.unlock();
	}
}
/**
 * @param drawable cast=(GdkDrawable *)
 * @param gc cast=(GdkGC *)
 * @param src cast=(GdkDrawable *)
 * @param xsrc cast=(gint)
 * @param ysrc cast=(gint)
 * @param xdest cast=(gint)
 * @param ydest cast=(gint)
 * @param width cast=(gint)
 * @param height cast=(gint)
 */
public static final native void _gdk_draw_drawable(int /*long*/ drawable, int /*long*/ gc, int /*long*/ src, int xsrc, int ysrc, int xdest, int ydest, int width, int height);
public static final void gdk_draw_drawable(int /*long*/ drawable, int /*long*/ gc, int /*long*/ src, int xsrc, int ysrc, int xdest, int ydest, int width, int height) {
	lock.lock();
	try {
		_gdk_draw_drawable(drawable, gc, src, xsrc, ysrc, xdest, ydest, width, height);
	} finally {
		lock.unlock();
	}
}
/**
 * @param drawable cast=(GdkDrawable *)
 * @param gc cast=(GdkGC *)
 * @param image cast=(GdkImage *)
 */
public static final native void _gdk_draw_image(int /*long*/ drawable, int /*long*/ gc, int /*long*/ image, int xsrc, int ysrc, int xdest, int ydest, int width, int height);
public static final void gdk_draw_image(int /*long*/ drawable, int /*long*/ gc, int /*long*/ image, int xsrc, int ysrc, int xdest, int ydest, int width, int height) {
	lock.lock();
	try {
		_gdk_draw_image(drawable, gc, image, xsrc, ysrc, xdest, ydest, width, height);
	} finally {
		lock.unlock();
	}
}
/**
 * @param drawable cast=(GdkDrawable *)
 * @param gc cast=(GdkGC *)
 * @param x cast=(gint)
 * @param y cast=(gint)
 * @param layout cast=(PangoLayout *)
 */
public static final native void _gdk_draw_layout(int /*long*/ drawable, int /*long*/ gc, int x, int y, int /*long*/ layout);
public static final void gdk_draw_layout(int /*long*/ drawable, int /*long*/ gc, int x, int y, int /*long*/ layout) {
	lock.lock();
	try {
		_gdk_draw_layout(drawable, gc, x, y, layout);
	} finally {
		lock.unlock();
	}
}
/**
 * @param drawable cast=(GdkDrawable *)
 * @param gc cast=(GdkGC *)
 * @param x cast=(gint)
 * @param y cast=(gint)
 * @param layout cast=(PangoLayout *)
 * @param foreground flags=no_out
 * @param background flags=no_out
 */
public static final native void _gdk_draw_layout_with_colors(int /*long*/ drawable, int /*long*/ gc, int x, int y, int /*long*/ layout, GdkColor foreground, GdkColor background);
public static final void gdk_draw_layout_with_colors(int /*long*/ drawable, int /*long*/ gc, int x, int y, int /*long*/ layout, GdkColor foreground, GdkColor background) {
	lock.lock();
	try {
		_gdk_draw_layout_with_colors(drawable, gc, x, y, layout, foreground, background);
	} finally {
		lock.unlock();
	}
}
/**
 * @param drawable cast=(GdkDrawable *)
 * @param gc cast=(GdkGC *)
 * @param x1 cast=(gint)
 * @param y1 cast=(gint)
 * @param x2 cast=(gint)
 * @param y2 cast=(gint)
 */
public static final native void _gdk_draw_line(int /*long*/ drawable, int /*long*/ gc, int x1, int y1, int x2, int y2);
public static final void gdk_draw_line(int /*long*/ drawable, int /*long*/ gc, int x1, int y1, int x2, int y2) {
	lock.lock();
	try {
		_gdk_draw_line(drawable, gc, x1, y1, x2, y2);
	} finally {
		lock.unlock();
	}
}
/**
 * @param drawable cast=(GdkDrawable *)
 * @param gc cast=(GdkGC *)
 * @param points cast=(GdkPoint *),flags=no_out critical
 * @param npoints cast=(gint)
 */
public static final native void _gdk_draw_lines(int /*long*/ drawable, int /*long*/ gc, int[] points, int npoints);
public static final void gdk_draw_lines(int /*long*/ drawable, int /*long*/ gc, int[] points, int npoints) {
	lock.lock();
	try {
		_gdk_draw_lines(drawable, gc, points, npoints);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param drawable cast=(GdkDrawable *)
 * @param gc cast=(GdkGC *)
 * @param pixbuf cast=(GdkPixbuf *)
 * @param xsrc cast=(gint)
 * @param ysrc cast=(gint)
 * @param xdest cast=(gint)
 * @param ydest cast=(gint)
 * @param width cast=(gint)
 * @param height cast=(gint)
 * @param dither cast=(GdkRgbDither)
 * @param x_dither cast=(gint)
 * @param y_dither cast=(gint)
 */
public static final native void _gdk_draw_pixbuf(int /*long*/ drawable, int /*long*/ gc, int /*long*/ pixbuf, int xsrc, int ysrc, int xdest, int ydest, int width, int height, int dither, int x_dither, int y_dither);
public static final void gdk_draw_pixbuf(int /*long*/ drawable, int /*long*/ gc, int /*long*/ pixbuf, int xsrc, int ysrc, int xdest, int ydest, int width, int height, int dither, int x_dither, int y_dither) {
	lock.lock();
	try {
		_gdk_draw_pixbuf(drawable, gc, pixbuf, xsrc, ysrc, xdest, ydest, width, height, dither, x_dither, y_dither);
	} finally {
		lock.unlock();
	}
}
/**
 * @param drawable cast=(GdkDrawable *)
 * @param gc cast=(GdkGC *)
 */
public static final native void _gdk_draw_point(int /*long*/ drawable, int /*long*/ gc, int x, int y);
public static final void gdk_draw_point(int /*long*/ drawable, int /*long*/ gc, int x, int y) {
	lock.lock();
	try {
		_gdk_draw_point(drawable, gc, x, y);
	} finally {
		lock.unlock();
	}
}
/**
 * @param drawable cast=(GdkDrawable *)
 * @param gc cast=(GdkGC *)
 * @param filled cast=(gint)
 * @param points cast=(GdkPoint *),flags=no_out critical
 * @param npoints cast=(gint)
 */
public static final native void _gdk_draw_polygon(int /*long*/ drawable, int /*long*/ gc, int filled, int[] points, int npoints);
public static final void gdk_draw_polygon(int /*long*/ drawable, int /*long*/ gc, int filled, int[] points, int npoints) {
	lock.lock();
	try {
		_gdk_draw_polygon(drawable, gc, filled, points, npoints);
	} finally {
		lock.unlock();
	}
}
/**
 * @param drawable cast=(GdkDrawable *)
 * @param gc cast=(GdkGC *)
 * @param filled cast=(gint)
 * @param x cast=(gint)
 * @param y cast=(gint)
 * @param width cast=(gint)
 * @param height cast=(gint)
 */
public static final native void _gdk_draw_rectangle(int /*long*/ drawable, int /*long*/ gc, int filled, int x, int y, int width, int height);
public static final void gdk_draw_rectangle(int /*long*/ drawable, int /*long*/ gc, int filled, int x, int y, int width, int height) {
	lock.lock();
	try {
		_gdk_draw_rectangle(drawable, gc, filled, x, y, width, height);
	} finally {
		lock.unlock();
	}
}
/** @param drawable cast=(GdkDrawable *) */
public static final native int _gdk_drawable_get_depth(int /*long*/ drawable);
public static final int gdk_drawable_get_depth(int /*long*/ drawable) {
	lock.lock();
	try {
		return _gdk_drawable_get_depth(drawable);
	} finally {
		lock.unlock();
	}
}

/**
 * @param drawable cast=(GdkDrawable *)
 * @param x cast=(gint)
 * @param y cast=(gint)
 * @param width cast=(gint)
 * @param height cast=(gint)
 */
public static final native int /*long*/ _gdk_drawable_get_image(int /*long*/ drawable, int x, int y, int width, int height);
public static final int /*long*/ gdk_drawable_get_image(int /*long*/ drawable, int x, int y, int width, int height) {
	lock.lock();
	try {
		return _gdk_drawable_get_image(drawable, x, y, width, height);
	} finally {
		lock.unlock();
	}
}
/**
 * @param drawable cast=(GdkDrawable *)
 * @param width cast=(gint *),flags=no_in critical
 * @param height cast=(gint *),flags=no_in critical
 */
public static final native void _gdk_drawable_get_size(int /*long*/ drawable, int[] width, int[] height);
public static final void gdk_drawable_get_size(int /*long*/ drawable, int[] width, int[] height) {
	lock.lock();
	try {
		_gdk_drawable_get_size(drawable, width, height);
	} finally {
		lock.unlock();
	}
}
/** @param drawable cast=(GdkDrawable *) */
public static final native int /*long*/ _gdk_drawable_get_visible_region(int /*long*/ drawable);
public static final int /*long*/ gdk_drawable_get_visible_region(int /*long*/ drawable) {
	lock.lock();
	try {
		return _gdk_drawable_get_visible_region(drawable);
	} finally {
		lock.unlock();
	}
}
/** @param event cast=(GdkEvent *) */
public static final native int /*long*/ _gdk_event_copy(int /*long*/ event);
public static final int /*long*/ gdk_event_copy(int /*long*/ event) {
	lock.lock();
	try {
		return _gdk_event_copy(event);
	} finally {
		lock.unlock();
	}
}
/** @param event cast=(GdkEvent *) */
public static final native void _gdk_event_free(int /*long*/ event);
public static final void gdk_event_free(int /*long*/ event) {
	lock.lock();
	try {
		_gdk_event_free(event);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gdk_event_get();
public static final int /*long*/ gdk_event_get() {
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
public static final native boolean _gdk_event_get_root_coords(int /*long*/ event, double[] px, double[] py);
public static final boolean gdk_event_get_root_coords(int /*long*/ event, double[] px, double[] py) {
	lock.lock();
	try {
		return _gdk_event_get_root_coords(event, px, py);
	} finally {
		lock.unlock();
	}
}
/**
 * @param event cast=(GdkEvent *)
 * @param px cast=(gdouble *)
 * @param py cast=(gdouble *)
 */
public static final native boolean _gdk_event_get_coords(int /*long*/ event, double[] px, double[] py);
public static final boolean gdk_event_get_coords(int /*long*/ event, double[] px, double[] py) {
	lock.lock();
	try {
		return _gdk_event_get_coords(event, px, py);
	} finally {
		lock.unlock();
	}
}
/** @param window cast=(GdkWindow *) */
public static final native int /*long*/ _gdk_event_get_graphics_expose(int /*long*/ window);
public static final int /*long*/ gdk_event_get_graphics_expose(int /*long*/ window) {
	lock.lock();
	try {
		return _gdk_event_get_graphics_expose(window);
	} finally {
		lock.unlock();
	}
}
/**
 * @param event cast=(GdkEvent *)
 * @param pmod cast=(GdkModifierType *)
 */
public static final native boolean _gdk_event_get_state(int /*long*/ event, int[] pmod);
public static final boolean gdk_event_get_state(int /*long*/ event, int[] pmod) {
	lock.lock();
	try {
		return _gdk_event_get_state(event, pmod);
	} finally {
		lock.unlock();
	}
}
/** @param event cast=(GdkEvent *) */
public static final native int _gdk_event_get_time(int /*long*/ event);
public static final int gdk_event_get_time(int /*long*/ event) {
	lock.lock();
	try {
		return _gdk_event_get_time(event);
	} finally {
		lock.unlock();
	}
}
/**
 * @param func cast=(GdkEventFunc)
 * @param data cast=(gpointer)
 * @param notify cast=(GDestroyNotify)
 */
public static final native void _gdk_event_handler_set(int /*long*/ func, int /*long*/ data, int /*long*/ notify);
public static final void gdk_event_handler_set(int /*long*/ func, int /*long*/ data, int /*long*/ notify) {
	lock.lock();
	try {
		_gdk_event_handler_set(func, data, notify);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gdk_event_new(int type);
public static final int /*long*/ gdk_event_new(int type) {
	lock.lock();
	try {
		return _gdk_event_new(type);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gdk_event_peek();
public static final int /*long*/ gdk_event_peek() {
	lock.lock();
	try {
		return _gdk_event_peek();
	} finally {
		lock.unlock();
	}
}
/** @param event cast=(GdkEvent *) */
public static final native void _gdk_event_put(int /*long*/ event);
public static final void gdk_event_put(int /*long*/ event) {
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
/** @param list cast=(gchar **) */
public static final native void _gdk_free_text_list(int /*long*/ list);
public static final void gdk_free_text_list(int /*long*/ list) {
	lock.lock();
	try {
		_gdk_free_text_list(list);
	} finally {
		lock.unlock();
	}
}
/**
 * @param gc cast=(GdkGC *)
 * @param values cast=(GdkGCValues *),flags=no_in
 */
public static final native void _gdk_gc_get_values(int /*long*/ gc, GdkGCValues values);
public static final void gdk_gc_get_values(int /*long*/ gc, GdkGCValues values) {
	lock.lock();
	try {
		_gdk_gc_get_values(gc, values);
	} finally {
		lock.unlock();
	}
}
/** @param window cast=(GdkDrawable *) */
public static final native int /*long*/ _gdk_gc_new(int /*long*/ window);
public static final int /*long*/ gdk_gc_new(int /*long*/ window) {
	lock.lock();
	try {
		return _gdk_gc_new(window);
	} finally {
		lock.unlock();
	}
}
/**
 * @param gc cast=(GdkGC *)
 * @param color cast=(GdkColor *),flags=no_out
 */
public static final native void _gdk_gc_set_background(int /*long*/ gc, GdkColor color);
public static final void gdk_gc_set_background(int /*long*/ gc, GdkColor color) {
	lock.lock();
	try {
		_gdk_gc_set_background(gc, color);
	} finally {
		lock.unlock();
	}
}
/**
 * @param gc cast=(GdkGC *)
 * @param mask cast=(GdkBitmap *)
 */
public static final native void _gdk_gc_set_clip_mask(int /*long*/ gc, int /*long*/ mask);
public static final void gdk_gc_set_clip_mask(int /*long*/ gc, int /*long*/ mask) {
	lock.lock();
	try {
		_gdk_gc_set_clip_mask(gc, mask);
	} finally {
		lock.unlock();
	}
}
/**
 * @param gc cast=(GdkGC *)
 * @param x cast=(gint)
 * @param y cast=(gint)
 */
public static final native void _gdk_gc_set_clip_origin(int /*long*/ gc, int x, int y);
public static final void gdk_gc_set_clip_origin(int /*long*/ gc, int x, int y) {
	lock.lock();
	try {
		_gdk_gc_set_clip_origin(gc, x, y);
	} finally {
		lock.unlock();
	}
}
/**
 * @param gc cast=(GdkGC *)
 * @param rectangle cast=(GdkRectangle *),flags=no_out
 */
public static final native void _gdk_gc_set_clip_rectangle(int /*long*/ gc, GdkRectangle rectangle);
public static final void gdk_gc_set_clip_rectangle(int /*long*/ gc, GdkRectangle rectangle) {
	lock.lock();
	try {
		_gdk_gc_set_clip_rectangle(gc, rectangle);
	} finally {
		lock.unlock();
	}
}
/**
 * @param gc cast=(GdkGC *)
 * @param region cast=(GdkRegion *)
 */
public static final native void _gdk_gc_set_clip_region(int /*long*/ gc, int /*long*/ region);
public static final void gdk_gc_set_clip_region(int /*long*/ gc, int /*long*/ region) {
	lock.lock();
	try {
		_gdk_gc_set_clip_region(gc, region);
	} finally {
		lock.unlock();
	}
}
/**
 * @param gc cast=(GdkGC *)
 * @param dash_offset cast=(gint)
 * @param dash_list cast=(gint8 *),flags=no_out critical
 * @param n cast=(gint)
 */
public static final native void _gdk_gc_set_dashes(int /*long*/ gc, int dash_offset, byte[] dash_list, int n);
public static final void gdk_gc_set_dashes(int /*long*/ gc, int dash_offset, byte[] dash_list, int n) {
	lock.lock();
	try {
		_gdk_gc_set_dashes(gc, dash_offset, dash_list, n);
	} finally {
		lock.unlock();
	}
}
/**
 * @param gc cast=(GdkGC *)
 * @param exposures cast=(gboolean)
 */
public static final native void _gdk_gc_set_exposures(int /*long*/ gc, boolean exposures);
public static final void gdk_gc_set_exposures(int /*long*/ gc, boolean exposures) {
	lock.lock();
	try {
		_gdk_gc_set_exposures(gc, exposures);
	} finally {
		lock.unlock();
	}
}
/**
 * @param gc cast=(GdkGC *)
 * @param fill cast=(GdkFill)
 */
public static final native void _gdk_gc_set_fill(int /*long*/ gc, int fill);
public static final void gdk_gc_set_fill(int /*long*/ gc, int fill) {
	lock.lock();
	try {
		_gdk_gc_set_fill(gc, fill);
	} finally {
		lock.unlock();
	}
}
/**
 * @param gc cast=(GdkGC *)
 * @param color cast=(GdkColor *),flags=no_out
 */
public static final native void _gdk_gc_set_foreground(int /*long*/ gc, GdkColor color);
public static final void gdk_gc_set_foreground(int /*long*/ gc, GdkColor color) {
	lock.lock();
	try {
		_gdk_gc_set_foreground(gc, color);
	} finally {
		lock.unlock();
	}
}
/**
 * @param gc cast=(GdkGC *)
 * @param function cast=(GdkFunction)
 */
public static final native void _gdk_gc_set_function(int /*long*/ gc, int /*long*/ function);
public static final void gdk_gc_set_function(int /*long*/ gc, int /*long*/ function) {
	lock.lock();
	try {
		_gdk_gc_set_function(gc, function);
	} finally {
		lock.unlock();
	}
}
/**
 * @param gc cast=(GdkGC *)
 * @param line_width cast=(gint)
 * @param line_style cast=(GdkLineStyle)
 * @param cap_style cast=(GdkCapStyle)
 * @param join_style cast=(GdkJoinStyle)
 */
public static final native void _gdk_gc_set_line_attributes(int /*long*/ gc, int line_width, int line_style, int cap_style, int join_style);
public static final void gdk_gc_set_line_attributes(int /*long*/ gc, int line_width, int line_style, int cap_style, int join_style) {
	lock.lock();
	try {
		_gdk_gc_set_line_attributes(gc, line_width, line_style, cap_style, join_style);
	} finally {
		lock.unlock();
	}
}
/**
 * @param gc cast=(GdkGC *)
 * @param stipple cast=(GdkPixmap *)
 */
public static final native void _gdk_gc_set_stipple(int /*long*/ gc, int /*long*/ stipple);
public static final void gdk_gc_set_stipple(int /*long*/ gc, int /*long*/ stipple) {
	lock.lock();
	try {
		_gdk_gc_set_stipple(gc, stipple);
	} finally {
		lock.unlock();
	}
}
/**
 * @param gc cast=(GdkGC *)
 * @param mode cast=(GdkSubwindowMode)
 */
public static final native void _gdk_gc_set_subwindow(int /*long*/ gc, int /*long*/ mode);
public static final void gdk_gc_set_subwindow(int /*long*/ gc, int /*long*/ mode) {
	lock.lock();
	try {
		_gdk_gc_set_subwindow(gc, mode);
	} finally {
		lock.unlock();
	}
}
/**
 * @param gc cast=(GdkGC *)
 * @param tile cast=(GdkPixmap *)
 */
public static final native void _gdk_gc_set_tile(int /*long*/ gc, int /*long*/ tile);
public static final void gdk_gc_set_tile(int /*long*/ gc, int /*long*/ tile) {
	lock.lock();
	try {
		_gdk_gc_set_tile(gc, tile);
	} finally {
		lock.unlock();
	}
}
/** @param gc cast=(GdkGC *) */
public static final native void _gdk_gc_set_ts_origin(int /*long*/ gc, int x, int y);
public static final void gdk_gc_set_ts_origin(int /*long*/ gc, int x, int y) {
	lock.lock();
	try {
		_gdk_gc_set_ts_origin(gc, x, y);
	} finally {
		lock.unlock();
	}
}
/**
 * @param gc cast=(GdkGC *)
 * @param values cast=(GdkGCValues *),flags=no_out
 * @param values_mask cast=(GdkGCValuesMask)
 */
public static final native void _gdk_gc_set_values(int /*long*/ gc, GdkGCValues values, int values_mask);
public static final void gdk_gc_set_values(int /*long*/ gc, GdkGCValues values, int values_mask) {
	lock.lock();
	try {
		_gdk_gc_set_values(gc, values, values_mask);
	} finally {
		lock.unlock();
	}
}
public static final native void _gdk_keyboard_ungrab(int time);
public static final void gdk_keyboard_ungrab(int time) {
	lock.lock();
	try {
		_gdk_keyboard_ungrab(time);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gdk_keymap_get_default();
public static final int /*long*/ gdk_keymap_get_default() {
	lock.lock();
	try {
		return _gdk_keymap_get_default();
	} finally {
		lock.unlock();
	}
}
/**
 * @param keymap cast=(GdkKeymap*)
 * @param state cast=(GdkModifierType)
 * @param keyval cast=(guint*)
 * @param effective_group cast=(gint*)
 * @param level cast=(gint*)
 * @param consumed_modifiers cast=(GdkModifierType *)
 */
public static final native boolean _gdk_keymap_translate_keyboard_state (int /*long*/ keymap, int hardware_keycode, int state, int group, int[] keyval, int[] effective_group, int[] level,  int[] consumed_modifiers);
public static final boolean gdk_keymap_translate_keyboard_state (int /*long*/ keymap, int hardware_keycode, int state, int group, int[] keyval, int[] effective_group, int[] level,  int[] consumed_modifiers) {
	lock.lock();
	try {
		return _gdk_keymap_translate_keyboard_state(keymap, hardware_keycode, state, group, keyval, effective_group, level, consumed_modifiers);
	} finally {
		lock.unlock();
	}
}
public static final native int _gdk_keyval_to_lower(int keyval);
public static final int gdk_keyval_to_lower(int keyval) {
	lock.lock();
	try {
		return _gdk_keyval_to_lower(keyval);
	} finally {
		lock.unlock();
	}
}
public static final native int _gdk_keyval_to_unicode(int keyval);
public static final int gdk_keyval_to_unicode(int keyval) {
	lock.lock();
	try {
		return _gdk_keyval_to_unicode(keyval);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gdk_pango_context_get();
public static final int /*long*/ gdk_pango_context_get() {
	lock.lock();
	try {
		return _gdk_pango_context_get();
	} finally {
		lock.unlock();
	}
}
/**
 * @param context cast=(PangoContext *)
 * @param colormap cast=(GdkColormap *)
 */
public static final native void _gdk_pango_context_set_colormap(int /*long*/ context, int /*long*/ colormap);
public static final void gdk_pango_context_set_colormap(int /*long*/ context, int /*long*/ colormap) {
	lock.lock();
	try {
		_gdk_pango_context_set_colormap(context, colormap);
	} finally {
		lock.unlock();
	}
}
/** @param layout cast=(PangoLayout *) */
public static final native int /*long*/ _gdk_pango_layout_get_clip_region(int /*long*/ layout, int x_origin, int y_origin, int[] index_ranges, int n_ranges);
public static final int /*long*/ gdk_pango_layout_get_clip_region(int /*long*/ layout, int x_origin, int y_origin, int[] index_ranges, int n_ranges) {
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
public static final native void _gdk_pixbuf_copy_area(int /*long*/ src_pixbuf, int src_x, int src_y, int width, int height, int /*long*/ dest_pixbuf, int dest_x, int dest_y);
public static final void gdk_pixbuf_copy_area(int /*long*/ src_pixbuf, int src_x, int src_y, int width, int height, int /*long*/ dest_pixbuf, int dest_x, int dest_y) {
	lock.lock();
	try {
		_gdk_pixbuf_copy_area(src_pixbuf, src_x, src_y, width, height, dest_pixbuf, dest_x, dest_y);
	} finally {
		lock.unlock();
	}
}
/**
 * @param dest cast=(GdkPixbuf *)
 * @param src cast=(GdkDrawable *)
 * @param cmap cast=(GdkColormap *)
 */
public static final native int /*long*/ _gdk_pixbuf_get_from_drawable(int /*long*/ dest, int /*long*/ src, int /*long*/ cmap, int src_x, int src_y, int dest_x, int dest_y, int width, int height);
public static final int /*long*/ gdk_pixbuf_get_from_drawable(int /*long*/ dest, int /*long*/ src, int /*long*/ cmap, int src_x, int src_y, int dest_x, int dest_y, int width, int height) {
	lock.lock();
	try {
		return _gdk_pixbuf_get_from_drawable(dest, src, cmap, src_x, src_y, dest_x, dest_y, width, height);
	} finally {
		lock.unlock();
	}
}
/** @param pixbuf cast=(const GdkPixbuf *) */
public static final native boolean _gdk_pixbuf_get_has_alpha(int /*long*/ pixbuf);
public static final boolean gdk_pixbuf_get_has_alpha(int /*long*/ pixbuf) {
	lock.lock();
	try {
		return _gdk_pixbuf_get_has_alpha(pixbuf);
	} finally {
		lock.unlock();
	}
}
/** @param pixbuf cast=(const GdkPixbuf *) */
public static final native int _gdk_pixbuf_get_height(int /*long*/ pixbuf);
public static final int gdk_pixbuf_get_height(int /*long*/ pixbuf) {
	lock.lock();
	try {
		return _gdk_pixbuf_get_height(pixbuf);
	} finally {
		lock.unlock();
	}
}
/** @param pixbuf cast=(const GdkPixbuf *) */
public static final native int /*long*/ _gdk_pixbuf_get_pixels(int /*long*/ pixbuf);
public static final int /*long*/ gdk_pixbuf_get_pixels(int /*long*/ pixbuf) {
	lock.lock();
	try {
		return _gdk_pixbuf_get_pixels(pixbuf);
	} finally {
		lock.unlock();
	}
}
/** @param pixbuf cast=(const GdkPixbuf *) */
public static final native int _gdk_pixbuf_get_rowstride(int /*long*/ pixbuf);
public static final int gdk_pixbuf_get_rowstride(int /*long*/ pixbuf) {
	lock.lock();
	try {
		return _gdk_pixbuf_get_rowstride(pixbuf);
	} finally {
		lock.unlock();
	}
}
/** @param pixbuf cast=(const GdkPixbuf *) */
public static final native int _gdk_pixbuf_get_width(int /*long*/ pixbuf);
public static final int gdk_pixbuf_get_width(int /*long*/ pixbuf) {
	lock.lock();
	try {
		return _gdk_pixbuf_get_width(pixbuf);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gdk_pixbuf_loader_new();
public static final int /*long*/ gdk_pixbuf_loader_new() {
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
public static final native boolean _gdk_pixbuf_loader_close(int /*long*/ loader, int /*long*/ [] error);
public static final boolean gdk_pixbuf_loader_close(int /*long*/ loader, int /*long*/ [] error) {
	lock.lock();
	try {
		return _gdk_pixbuf_loader_close(loader, error);
	} finally {
		lock.unlock();
	}
}
/** @param loader cast=(GdkPixbufLoader *) */
public static final native int /*long*/ _gdk_pixbuf_loader_get_pixbuf(int /*long*/ loader);
public static final int /*long*/ gdk_pixbuf_loader_get_pixbuf(int /*long*/ loader) {
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
public static final native boolean _gdk_pixbuf_loader_write(int /*long*/ loader, int /*long*/ buffer, int count, int /*long*/ [] error);
public static final boolean gdk_pixbuf_loader_write(int /*long*/ loader, int /*long*/ buffer, int count, int /*long*/ [] error) {
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
public static final native int /*long*/ _gdk_pixbuf_new(int colorspace, boolean has_alpha, int bits_per_sample, int width, int height);
public static final int /*long*/ gdk_pixbuf_new(int colorspace, boolean has_alpha, int bits_per_sample, int width, int height) {
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
public static final native int /*long*/ _gdk_pixbuf_new_from_file(byte[] filename, int /*long*/ [] error); 
public static final int /*long*/ gdk_pixbuf_new_from_file(byte[] filename, int /*long*/ [] error) {
	lock.lock();
	try {
		return _gdk_pixbuf_new_from_file(filename, error);
	} finally {
		lock.unlock();
	}
}
/**
 * @param pixbuf cast=(GdkPixbuf *)
 * @param drawable cast=(GdkDrawable *)
 * @param gc cast=(GdkGC *)
 * @param dither cast=(GdkRgbDither)
 */
public static final native void _gdk_pixbuf_render_to_drawable(int /*long*/ pixbuf, int /*long*/ drawable, int /*long*/ gc, int src_x, int src_y, int dest_x, int dest_y, int width, int height, int dither, int x_dither, int y_dither);
public static final void gdk_pixbuf_render_to_drawable(int /*long*/ pixbuf, int /*long*/ drawable, int /*long*/ gc, int src_x, int src_y, int dest_x, int dest_y, int width, int height, int dither, int x_dither, int y_dither) {
	lock.lock();
	try {
		_gdk_pixbuf_render_to_drawable(pixbuf, drawable, gc, src_x, src_y, dest_x, dest_y, width, height, dither, x_dither, y_dither);
	} finally {
		lock.unlock();
	}
}
/**
 * @param pixbuf cast=(GdkPixbuf *)
 * @param drawable cast=(GdkDrawable *)
 * @param alpha_mode cast=(GdkPixbufAlphaMode)
 * @param dither cast=(GdkRgbDither)
 */
public static final native void _gdk_pixbuf_render_to_drawable_alpha(int /*long*/ pixbuf, int /*long*/ drawable, int src_x, int src_y, int dest_x, int dest_y, int width, int height, int alpha_mode, int alpha_threshold, int dither, int x_dither, int y_dither);
public static final void gdk_pixbuf_render_to_drawable_alpha(int /*long*/ pixbuf, int /*long*/ drawable, int src_x, int src_y, int dest_x, int dest_y, int width, int height, int alpha_mode, int alpha_threshold, int dither, int x_dither, int y_dither) {
	lock.lock();
	try {
		_gdk_pixbuf_render_to_drawable_alpha(pixbuf, drawable, src_x, src_y, dest_x, dest_y, width, height, alpha_mode, alpha_threshold, dither, x_dither, y_dither);
	} finally {
		lock.unlock();
	}
}
/**
 * @param pixbuf cast=(GdkPixbuf *)
 * @param pixmap_return cast=(GdkDrawable **)
 * @param mask_return cast=(GdkBitmap **)
 */
public static final native void _gdk_pixbuf_render_pixmap_and_mask(int /*long*/ pixbuf, int /*long*/[] pixmap_return, int /*long*/[] mask_return, int alpha_threshold);
public static final void gdk_pixbuf_render_pixmap_and_mask(int /*long*/ pixbuf, int /*long*/[] pixmap_return, int /*long*/[] mask_return, int alpha_threshold) {
	lock.lock();
	try {
		_gdk_pixbuf_render_pixmap_and_mask(pixbuf, pixmap_return, mask_return, alpha_threshold);
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
 * @param error cast=(GError **)
 * @param terminate cast=(char *)
 */
public static final native boolean _gdk_pixbuf_save_to_buffer(int /*long*/ pixbuf, int /*long*/ [] buffer, int [] buffer_size, byte [] type, int /*long*/ [] error, byte [] terminate);
public static final boolean gdk_pixbuf_save_to_buffer(int /*long*/ pixbuf, int /*long*/ [] buffer, int [] buffer_size, byte [] type, int /*long*/ [] error, byte [] terminate) {
	lock.lock();
	try {
		return _gdk_pixbuf_save_to_buffer(pixbuf, buffer, buffer_size, type, error, terminate);
	} finally {
		lock.unlock();
	}
}
/**
 * @param src cast=(const GdkPixbuf *)
 * @param dest cast=(GdkPixbuf *)
 * @param offset_x cast=(double)
 * @param offset_y cast=(double)
 * @param scale_x cast=(double)
 * @param scale_y cast=(double)
 */
public static final native void _gdk_pixbuf_scale(int /*long*/ src, int /*long*/ dest, int dest_x, int dest_y, int dest_width, int dest_height, double offset_x, double offset_y, double scale_x, double scale_y, int interp_type);
public static final void gdk_pixbuf_scale(int /*long*/ src, int /*long*/ dest, int dest_x, int dest_y, int dest_width, int dest_height, double offset_x, double offset_y, double scale_x, double scale_y, int interp_type) {
	lock.lock();
	try {
		_gdk_pixbuf_scale(src, dest, dest_x, dest_y, dest_width, dest_height, offset_x, offset_y, scale_x, scale_y, interp_type);
	} finally {
		lock.unlock();
	}
}
/**
 * @param src cast=(const GdkPixbuf *)
 * @param interp_type cast=(GdkInterpType)
 */
public static final native int /*long*/ _gdk_pixbuf_scale_simple(int /*long*/ src, int dest_width, int dest_height, int interp_type);
public static final int /*long*/ gdk_pixbuf_scale_simple(int /*long*/ src, int dest_width, int dest_height, int interp_type) {
	lock.lock();
	try {
		return _gdk_pixbuf_scale_simple(src, dest_width, dest_height, interp_type);
	} finally {
		lock.unlock();
	}
}
/**
 * @param window cast=(GdkWindow *)
 * @param width cast=(gint)
 * @param height cast=(gint)
 * @param depth cast=(gint)
 */
public static final native int /*long*/ _gdk_pixmap_new(int /*long*/ window, int width, int height, int depth);
public static final int /*long*/ gdk_pixmap_new(int /*long*/ window, int width, int height, int depth) {
	lock.lock();
	try {
		return _gdk_pixmap_new(window, width, height, depth);
	} finally {
		lock.unlock();
	}
}
/**
 * @param window cast=(GdkWindow *)
 * @param owner_events cast=(gboolean)
 * @param event_mask cast=(GdkEventMask)
 * @param confine_to cast=(GdkWindow *)
 * @param cursor cast=(GdkCursor *)
 * @param time cast=(guint32)
 */
public static final native int _gdk_pointer_grab(int /*long*/ window, boolean owner_events, int event_mask, int /*long*/ confine_to, int /*long*/ cursor, int time);
public static final int gdk_pointer_grab(int /*long*/ window, boolean owner_events, int event_mask, int /*long*/ confine_to, int /*long*/ cursor, int time) {
	lock.lock();
	try {
		return _gdk_pointer_grab(window, owner_events, event_mask, confine_to, cursor, time);
	} finally {
		lock.unlock();
	}
}
public static final native boolean _gdk_pointer_is_grabbed();
public static final boolean gdk_pointer_is_grabbed() {
	lock.lock();
	try {
		return _gdk_pointer_is_grabbed();
	} finally {
		lock.unlock();
	}
}
/** @param time cast=(guint32) */
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
 * @param window cast=(GdkWindow *)
 * @param property cast=(GdkAtom)
 * @param type cast=(GdkAtom)
 * @param actual_property_type cast=(GdkAtom *)
 * @param actual_format cast=(gint *)
 * @param actual_length cast=(gint *)
 * @param data cast=(guchar **)
 */
public static final native boolean _gdk_property_get(int /*long*/ window, int /*long*/ property, int /*long*/ type, int /*long*/ offset, int /*long*/ length, int pdelete, int /*long*/[] actual_property_type, int[] actual_format, int[] actual_length, int /*long*/[] data);
public static final boolean gdk_property_get(int /*long*/ window, int /*long*/ property, int /*long*/ type, int /*long*/ offset, int /*long*/ length, int pdelete, int /*long*/[] actual_property_type, int[] actual_format, int[] actual_length, int /*long*/[] data) {
	lock.lock();
	try {
		return _gdk_property_get(window, property, type, offset, length, pdelete, actual_property_type, actual_format, actual_length, data);
	} finally {
		lock.unlock();
	}
}
/** @param region cast=(GdkRegion *) */
public static final native void _gdk_region_destroy(int /*long*/ region);
public static final void gdk_region_destroy(int /*long*/ region) {
	lock.lock();
	try {
		_gdk_region_destroy(region);
	} finally {
		lock.unlock();
	}
}
/** @param region cast=(GdkRegion *) */
public static final native boolean _gdk_region_empty(int /*long*/ region);
public static final boolean gdk_region_empty(int /*long*/ region) {
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
public static final native void _gdk_region_get_clipbox(int /*long*/ region, GdkRectangle rectangle);
public static final void gdk_region_get_clipbox(int /*long*/ region, GdkRectangle rectangle) {
	lock.lock();
	try {
		_gdk_region_get_clipbox(region, rectangle);
	} finally {
		lock.unlock();
	}
}
/**
 * @param region cast=(GdkRegion *)
 * @param rectangles cast=(GdkRectangle **)
 * @param n_rectangles cast=(gint *)
 */
public static final native void _gdk_region_get_rectangles(int /*long*/ region, int /*long*/[] rectangles, int[] n_rectangles);
public static final void gdk_region_get_rectangles(int /*long*/ region, int /*long*/[] rectangles, int[] n_rectangles) {
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
public static final native void _gdk_region_intersect(int /*long*/ source1, int /*long*/ source2);
public static final void gdk_region_intersect(int /*long*/ source1, int /*long*/ source2) {
	lock.lock();
	try {
		_gdk_region_intersect(source1, source2);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gdk_region_new();
public static final int /*long*/ gdk_region_new() {
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
public static final native void _gdk_region_offset(int /*long*/ region, int dx, int dy);
public static final void gdk_region_offset(int /*long*/ region, int dx, int dy) {
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
public static final native boolean _gdk_region_point_in(int /*long*/ region, int x, int y);
public static final boolean gdk_region_point_in(int /*long*/ region, int x, int y) {
	lock.lock();
	try {
		return _gdk_region_point_in(region, x, y);
	} finally {
		lock.unlock();
	}
}
/**
 * @param points cast=(GdkPoint *)
 * @param fill_rule cast=(GdkFillRule)
 */
public static final native int /*long*/ _gdk_region_polygon(int[] points, int npoints, int fill_rule);
public static final int /*long*/ gdk_region_polygon(int[] points, int npoints, int fill_rule) {
	lock.lock();
	try {
		return _gdk_region_polygon(points, npoints, fill_rule);
	} finally {
		lock.unlock();
	}
}
/** @param rectangle flags=no_out */
public static final native int /*long*/ _gdk_region_rectangle(GdkRectangle rectangle);
public static final int /*long*/ gdk_region_rectangle(GdkRectangle rectangle) {
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
public static final native int /*long*/ _gdk_region_rect_in(int /*long*/ region, GdkRectangle rect);
public static final int /*long*/ gdk_region_rect_in(int /*long*/ region, GdkRectangle rect) {
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
public static final native void _gdk_region_subtract(int /*long*/ source1, int /*long*/ source2);
public static final void gdk_region_subtract(int /*long*/ source1, int /*long*/ source2) {
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
public static final native void _gdk_region_union(int /*long*/ source1, int /*long*/ source2);
public static final void gdk_region_union(int /*long*/ source1, int /*long*/ source2) {
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
public static final native void _gdk_region_union_with_rect(int /*long*/ region, GdkRectangle rect);
public static final void gdk_region_union_with_rect(int /*long*/ region, GdkRectangle rect) {
	lock.lock();
	try {
		_gdk_region_union_with_rect(region, rect);
	} finally {
		lock.unlock();
	}
}
public static final native void _gdk_rgb_init();
public static final void gdk_rgb_init() {
	lock.lock();
	try {
		_gdk_rgb_init();
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _gdk_screen_get_default();
public static final int /*long*/ gdk_screen_get_default() {
	lock.lock();
	try {
		return _gdk_screen_get_default();
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
public static final native int _gdk_screen_get_monitor_at_point (int /*long*/ screen, int x, int y);
public static final int gdk_screen_get_monitor_at_point (int /*long*/ screen, int x, int y) {
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
public static final native int _gdk_screen_get_monitor_at_window(int /*long*/ screen, int /*long*/ window);
public static final int gdk_screen_get_monitor_at_window(int /*long*/ screen, int /*long*/ window) {
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
public static final native void _gdk_screen_get_monitor_geometry (int /*long*/ screen, int monitor_num, GdkRectangle dest);
public static final void gdk_screen_get_monitor_geometry (int /*long*/ screen, int monitor_num, GdkRectangle dest) {
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
 */
public static final native int _gdk_screen_get_n_monitors(int /*long*/ screen);
public static final int gdk_screen_get_n_monitors(int /*long*/ screen) {
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
public static final native int _gdk_screen_get_number(int /*long*/ screen);
public static final int gdk_screen_get_number(int /*long*/ screen) {
	lock.lock();
	try {
		return _gdk_screen_get_number(screen);
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
/**
 * @param str cast=(const gchar *)
 * @param encoding cast=(GdkAtom *)
 * @param format cast=(gint *)
 * @param ctext cast=(guchar **)
 * @param length cast=(gint *)
 */
public static final native boolean _gdk_utf8_to_compound_text(byte[] str, int /*long*/[] encoding, int[] format, int /*long*/[] ctext, int[] length);
public static final boolean gdk_utf8_to_compound_text(byte[] str, int /*long*/[] encoding, int[] format, int /*long*/[] ctext, int[] length) {
	lock.lock();
	try {
		return _gdk_utf8_to_compound_text(str, encoding, format, ctext, length);
	} finally {
		lock.unlock();
	}
}
/** @param str cast=(const gchar *) */
public static final native int /*long*/ _gdk_utf8_to_string_target(byte[] str);
public static final int /*long*/ gdk_utf8_to_string_target(byte[] str) {
	lock.lock();
	try {
		return _gdk_utf8_to_string_target(str);
	} finally {
		lock.unlock();
	}
}
/**
 * @param encoding cast=(GdkAtom)
 * @param text cast=(guchar *)
 * @param list cast=(gchar ***)
 */
public static final native int _gdk_text_property_to_utf8_list  (int /*long*/ encoding, int format, int /*long*/ text, int length,  int /*long*/[] list);
public static final int gdk_text_property_to_utf8_list  (int /*long*/ encoding, int format, int /*long*/ text, int length,  int /*long*/[] list) {
	lock.lock();
	try {
		return _gdk_text_property_to_utf8_list(encoding, format, text, length, list);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param display cast=(GdkDisplay*)
 */
public static final native void _gtk_tooltip_trigger_tooltip_query (int /*long*/ display);
public static final void gtk_tooltip_trigger_tooltip_query (int /*long*/ display){
	lock.lock();
	try {
		 _gtk_tooltip_trigger_tooltip_query (display);
	} finally {
		lock.unlock();
	}
}

public static final native  int _gdk_unicode_to_keyval(int wc);
public static final  int gdk_unicode_to_keyval(int wc) {
	lock.lock();
	try {
		return _gdk_unicode_to_keyval(wc);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gdk_visual_get_system();
public static final int /*long*/ gdk_visual_get_system() {
	lock.lock();
	try {
		return _gdk_visual_get_system();
	} finally {
		lock.unlock();
	}
}
/**
 * @param win_x cast=(gint *)
 * @param win_y cast=(gint *)
 */
public static final native int /*long*/ _gdk_window_at_pointer(int[] win_x, int[] win_y);
public static final int /*long*/ gdk_window_at_pointer(int[] win_x, int[] win_y) {
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
public static final native void _gdk_window_begin_paint_rect(int /*long*/ window, GdkRectangle rectangle);
public static final void gdk_window_begin_paint_rect(int /*long*/ window, GdkRectangle rectangle) {
	lock.lock();
	try {
		_gdk_window_begin_paint_rect(window, rectangle);
	} finally {
		lock.unlock();
	}
}
/** @param window cast=(GdkWindow *) */
public static final native void _gdk_window_clear_area(int /*long*/ window, int x, int y, int width, int height);
public static final void gdk_window_clear_area(int /*long*/ window, int x, int y, int width, int height) {
	lock.lock();
	try {
		_gdk_window_clear_area(window, x, y, width, height);
	} finally {
		lock.unlock();
	}
}
/** @param window cast=(GdkWindow *) */
public static final native void _gdk_window_destroy(int /*long*/ window);
public static final void gdk_window_destroy(int /*long*/ window) {
	lock.lock();
	try {
		_gdk_window_destroy(window);
	} finally {
		lock.unlock();
	}
}
/** @param window cast=(GdkWindow *) */
public static final native void _gdk_window_end_paint(int /*long*/ window);
public static final void gdk_window_end_paint(int /*long*/ window) {
	lock.lock();
	try {
		_gdk_window_end_paint(window);
	} finally {
		lock.unlock();
	}
}
/** @param window cast=(GdkWindow *) */
public static final native int /*long*/ _gdk_window_get_children(int /*long*/ window);
public static final int /*long*/ gdk_window_get_children(int /*long*/ window) {
	lock.lock();
	try {
		return _gdk_window_get_children(window);
	} finally {
		lock.unlock();
	}
}
/** @param window cast=(GdkWindow *) */
public static final native int _gdk_window_get_events(int /*long*/ window);
public static final int gdk_window_get_events(int /*long*/ window) {
	lock.lock();
	try {
		return _gdk_window_get_events(window);
	} finally {
		lock.unlock();
	}
}
/** @param window cast=(GdkWindow *) */
public static final native void _gdk_window_focus(int /*long*/ window, int timestamp);
public static final void gdk_window_focus(int /*long*/ window, int timestamp) {
	lock.lock();
	try {
		_gdk_window_focus(window, timestamp);
	} finally {
		lock.unlock();
	}
}
/** @param window cast=(GdkWindow *) */
public static final native void _gdk_window_freeze_updates(int /*long*/ window);
public static final void gdk_window_freeze_updates(int /*long*/ window) {
	lock.lock();
	try {
		_gdk_window_freeze_updates(window);
	} finally {
		lock.unlock();
	}
}
/**
 * @param window cast=(GdkWindow *)
 * @param rect cast=(GdkRectangle *),flags=no_in
 */
public static final native void _gdk_window_get_frame_extents(int /*long*/ window, GdkRectangle rect);
public static final void gdk_window_get_frame_extents(int /*long*/ window, GdkRectangle rect) {
	lock.lock();
	try {
		_gdk_window_get_frame_extents(window, rect);
	} finally {
		lock.unlock();
	}
}
/**
 * @param window cast=(GdkWindow *)
 * @param real_drawable cast=(GdkDrawable **)
 * @param x_offset cast=(gint *)
 * @param y_offset cast=(gint *)
 */
public static final native void _gdk_window_get_internal_paint_info(int /*long*/ window, int /*long*/ [] real_drawable, int[] x_offset, int[] y_offset);
public static final void gdk_window_get_internal_paint_info(int /*long*/ window, int /*long*/ [] real_drawable, int[] x_offset, int[] y_offset) {
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
public static final native int _gdk_window_get_origin(int /*long*/ window, int[] x, int[] y);
public static final int gdk_window_get_origin(int /*long*/ window, int[] x, int[] y) {
	lock.lock();
	try {
		return _gdk_window_get_origin(window, x, y);
	} finally {
		lock.unlock();
	}
}
/** @param window cast=(GdkWindow *) */
public static final native int /*long*/ _gdk_window_get_parent(int /*long*/ window);
public static final int /*long*/ gdk_window_get_parent(int /*long*/ window) {
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
 * @param mask cast=(GdkModifierType *)
 */
public static final native int /*long*/ _gdk_window_get_pointer(int /*long*/ window, int[] x, int[] y, int[] mask);
public static final int /*long*/ gdk_window_get_pointer(int /*long*/ window, int[] x, int[] y, int[] mask) {
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
public static final native void _gdk_window_get_position(int /*long*/ window, int[] x, int[] y);
public static final void gdk_window_get_position(int /*long*/ window, int[] x, int[] y) {
	lock.lock();
	try {
		_gdk_window_get_position(window, x, y);
	} finally {
		lock.unlock();
	}
}
/**
 * @param window cast=(GdkWindow *)
 * @param data cast=(gpointer *)
 */
public static final native void _gdk_window_get_user_data(int /*long*/ window, int /*long*/[] data);
public static final void gdk_window_get_user_data(int /*long*/ window, int /*long*/[] data) {
	lock.lock();
	try {
		_gdk_window_get_user_data(window, data);
	} finally {
		lock.unlock();
	}
}
/** @param window cast=(GdkWindow *) */
public static final native void _gdk_window_hide(int /*long*/ window);
public static final void gdk_window_hide(int /*long*/ window) {
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
public static final native void _gdk_window_invalidate_rect(int /*long*/ window, GdkRectangle rectangle, boolean invalidate_children);
public static final void gdk_window_invalidate_rect(int /*long*/ window, GdkRectangle rectangle, boolean invalidate_children) {
	lock.lock();
	try {
		_gdk_window_invalidate_rect(window, rectangle, invalidate_children);
	} finally {
		lock.unlock();
	}
}
/**
 * @param window cast=(GdkWindow *)
 * @param region cast=(GdkRegion *)
 * @param invalidate_children cast=(gboolean)
 */
public static final native void _gdk_window_invalidate_region(int /*long*/ window, int /*long*/ region, boolean invalidate_children);
public static final void gdk_window_invalidate_region(int /*long*/ window, int /*long*/ region, boolean invalidate_children) {
	lock.lock();
	try {
		_gdk_window_invalidate_region(window, region, invalidate_children);
	} finally {
		lock.unlock();
	}
}
/** @param window cast=(GdkWindow *) */
public static final native boolean _gdk_window_is_visible(int /*long*/ window);
public static final boolean gdk_window_is_visible(int /*long*/ window) {
	lock.lock();
	try {
		return _gdk_window_is_visible(window);
	} finally {
		lock.unlock();
	}
}
/** @param window cast=(GdkWindow *) */
public static final native void _gdk_window_move(int /*long*/ window, int x, int y);
public static final void gdk_window_move(int /*long*/ window, int x, int y) {
	lock.lock();
	try {
		_gdk_window_move(window, x, y);
	} finally {
		lock.unlock();
	}
}
/**
 * @param parent cast=(GdkWindow *)
 * @param attributes flags=no_out
 */
public static final native int /*long*/ _gdk_window_new(int /*long*/ parent, GdkWindowAttr attributes, int attributes_mask);
public static final int /*long*/ gdk_window_new(int /*long*/ parent, GdkWindowAttr attributes, int attributes_mask) {
	lock.lock();
	try {
		return _gdk_window_new(parent, attributes, attributes_mask);
	} finally {
		lock.unlock();
	}
}
/** @param window cast=(GdkWindow *) */
public static final native void _gdk_window_lower(int /*long*/ window);
public static final void gdk_window_lower(int /*long*/ window) {
	lock.lock();
	try {
		_gdk_window_lower(window);
	} finally {
		lock.unlock();
	}
}
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
 * @param window cast=(GdkWindow *)
 * @param update_children cast=(gboolean)
 */
public static final native void _gdk_window_process_updates(int /*long*/ window, boolean update_children);
public static final void gdk_window_process_updates(int /*long*/ window, boolean update_children) {
	lock.lock();
	try {
		_gdk_window_process_updates(window, update_children);
	} finally {
		lock.unlock();
	}
}
/** @param window cast=(GdkWindow *) */
public static final native void _gdk_window_raise(int /*long*/ window);
public static final void gdk_window_raise(int /*long*/ window) {
	lock.lock();
	try {
		_gdk_window_raise(window);
	} finally {
		lock.unlock();
	}
}
/** @param window cast=(GdkWindow *) */
public static final native void _gdk_window_resize(int /*long*/ window, int width, int height);
public static final void gdk_window_resize(int /*long*/ window, int width, int height) {
	lock.lock();
	try {
		_gdk_window_resize(window, width, height);
	} finally {
		lock.unlock();
	}
}
/** @param window cast=(GdkWindow *) */
public static final native void _gdk_window_scroll(int /*long*/ window, int dx, int dy);
public static final void gdk_window_scroll(int /*long*/ window, int dx, int dy) {
	lock.lock();
	try {
		_gdk_window_scroll(window, dx, dy);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param window cast=(GdkWindow *)
 * @param accept_focus cast=(gboolean)
 */
public static final native void _gdk_window_set_accept_focus(int /*long*/ window, boolean accept_focus);
public static final void gdk_window_set_accept_focus(int /*long*/ window, boolean accept_focus) {
	lock.lock();
	try {
		_gdk_window_set_accept_focus(window, accept_focus);
	} finally {
		lock.unlock();
	}
}
/**
 * @param window cast=(GdkWindow *)
 * @param pixmap cast=(GdkPixmap *)
 * @param parent_relative cast=(gboolean)
 */
public static final native void _gdk_window_set_back_pixmap(int /*long*/ window, int /*long*/ pixmap, boolean parent_relative);
public static final void gdk_window_set_back_pixmap(int /*long*/ window, int /*long*/ pixmap, boolean parent_relative) {
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
public static final native void _gdk_window_set_cursor(int /*long*/ window, int /*long*/ cursor);
public static final void gdk_window_set_cursor(int /*long*/ window, int /*long*/ cursor) {
	lock.lock();
	try {
		_gdk_window_set_cursor(window, cursor);
	} finally {
		lock.unlock();
	}
}
/** @param setting cast=(gboolean) */
public static final native void _gdk_window_set_debug_updates(boolean setting);
public static final void gdk_window_set_debug_updates(boolean setting) {	
	lock.lock();
	try {
		_gdk_window_set_debug_updates(setting);
	} finally {
		lock.unlock();
	}
}
/**
 * @param window cast=(GdkWindow *)
 * @param decorations cast=(GdkWMDecoration)
 */
public static final native void _gdk_window_set_decorations(int /*long*/ window, int decorations);
public static final void gdk_window_set_decorations(int /*long*/ window, int decorations) {
	lock.lock();
	try {
		_gdk_window_set_decorations(window, decorations);
	} finally {
		lock.unlock();
	}
}
/** @param window cast=(GdkWindow *) */
public static final native void _gdk_window_set_events(int /*long*/ window, int event_mask);
public static final void gdk_window_set_events(int /*long*/ window, int event_mask) {
	lock.lock();
	try {
		_gdk_window_set_events(window, event_mask);
	} finally {
		lock.unlock();
	}
}
/**
 * @param window cast=(GdkWindow *)
 * @param icon_window cast=(GdkWindow *)
 * @param pixmap cast=(GdkPixmap *)
 * @param mask cast=(GdkBitmap *)
 */
public static final native void _gdk_window_set_icon(int /*long*/ window, int /*long*/ icon_window, int /*long*/ pixmap, int /*long*/ mask);
public static final void gdk_window_set_icon(int /*long*/ window, int /*long*/ icon_window, int /*long*/ pixmap, int /*long*/ mask) {
	lock.lock();
	try {
		_gdk_window_set_icon(window, icon_window, pixmap, mask);
	} finally {
		lock.unlock();
	}
}
/**
 * @param window cast=(GdkWindow *)
 * @param pixbufs cast=(GList *)
 */
public static final native void _gdk_window_set_icon_list(int /*long*/ window, int /*long*/ pixbufs);
public static final void gdk_window_set_icon_list(int /*long*/ window, int /*long*/ pixbufs) {
	lock.lock();
	try {
		_gdk_window_set_icon_list(window, pixbufs);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param window cast=(GdkWindow *)
 * @param setting cast=(gboolean)
 */
public static final native void _gdk_window_set_keep_above(int /*long*/ window, boolean setting);
public static final void gdk_window_set_keep_above(int /*long*/ window, boolean setting) {
	lock.lock();
	try {
		_gdk_window_set_keep_above(window, setting);
	} finally {
		lock.unlock();
	}
}
/**
 * @param window cast=(GdkWindow *)
 * @param override_redirect cast=(gboolean)
 */
public static final native void _gdk_window_set_override_redirect(int /*long*/ window, boolean override_redirect);
public static final void gdk_window_set_override_redirect(int /*long*/ window, boolean override_redirect) {
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
public static final native void _gdk_window_set_user_data(int /*long*/ window, int /*long*/ user_data);
public static final void gdk_window_set_user_data(int /*long*/ window, int /*long*/ user_data) {
	lock.lock();
	try {
		_gdk_window_set_user_data(window, user_data);
	} finally {
		lock.unlock();
	}
}
/**
 * @param window cast=(GdkWindow *)
 * @param shape_region cast=(GdkRegion *)
 */
public static final native void _gdk_window_shape_combine_region (int /*long*/ window, int /*long*/  shape_region, int offset_x,  int offset_y);
public static final void gdk_window_shape_combine_region (int /*long*/ window, int /*long*/  shape_region, int offset_x,  int offset_y) {
	lock.lock();
	try {
		_gdk_window_shape_combine_region(window, shape_region, offset_x, offset_y);
	} finally {
		lock.unlock();
	}
}
/** @param window cast=(GdkWindow *) */
public static final native void _gdk_window_show(int /*long*/ window);
public static final void gdk_window_show(int /*long*/ window) {
	lock.lock();
	try {
		_gdk_window_show(window);
	} finally {
		lock.unlock();
	}
}
/** @param window cast=(GdkWindow *) */
public static final native void _gdk_window_show_unraised(int /*long*/ window);
public static final void gdk_window_show_unraised(int /*long*/ window) {
	lock.lock();
	try {
		_gdk_window_show_unraised(window);
	} finally {
		lock.unlock();
	}
}
/** @param window cast=(GdkWindow *) */
public static final native void _gdk_window_thaw_updates(int /*long*/ window);
public static final void gdk_window_thaw_updates(int /*long*/ window) {
	lock.lock();
	try {
		_gdk_window_thaw_updates(window);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gtk_accel_group_new();
public static final int /*long*/ gtk_accel_group_new() {
	lock.lock();
	try {
		return _gtk_accel_group_new();
	} finally {
		lock.unlock();
	}
}
/**
 * @param accelGroup cast=(GObject *)
 * @param accelKey cast=(guint)
 * @param accelMods cast=(GdkModifierType)
 */
public static final native boolean _gtk_accel_groups_activate(int /*long*/ accelGroup, int accelKey, int accelMods);
public static final boolean gtk_accel_groups_activate(int /*long*/ accelGroup, int accelKey, int accelMods) {
	lock.lock();
	try {
		return _gtk_accel_groups_activate(accelGroup, accelKey, accelMods);
	} finally {
		lock.unlock();
	}
}
/**
 * @param accel_label cast=(GtkAccelLabel *)
 * @param accel_widget cast=(GtkWidget *)
 */
public static final native void _gtk_accel_label_set_accel_widget(int /*long*/ accel_label, int /*long*/ accel_widget);
public static final void gtk_accel_label_set_accel_widget(int /*long*/ accel_label, int /*long*/ accel_widget) {
	lock.lock();
	try {
		_gtk_accel_label_set_accel_widget(accel_label, accel_widget);
	} finally {
		lock.unlock();
	}
}
/** @param adjustment cast=(GtkAdjustment *) */
public static final native void _gtk_adjustment_changed(int /*long*/ adjustment);
public static final void gtk_adjustment_changed(int /*long*/ adjustment) {
	lock.lock();
	try {
		_gtk_adjustment_changed(adjustment);
	} finally {
		lock.unlock();
	}
}
/**
 * @param value cast=(gdouble)
 * @param lower cast=(gdouble)
 * @param upper cast=(gdouble)
 * @param step_increment cast=(gdouble)
 * @param page_increment cast=(gdouble)
 */
public static final native int /*long*/ _gtk_adjustment_new(double value, double lower, double upper, double step_increment, double page_increment, double page_size);
public static final int /*long*/ gtk_adjustment_new(double value, double lower, double upper, double step_increment, double page_increment, double page_size) {
	lock.lock();
	try {
		return _gtk_adjustment_new(value, lower, upper, step_increment, page_increment, page_size);
	} finally {
		lock.unlock();
	}
}
/**
 * @param adjustment cast=(GtkAdjustment *)
 * @param value cast=(gdouble)
 */
public static final native void _gtk_adjustment_set_value(int /*long*/ adjustment, double value);
public static final void gtk_adjustment_set_value(int /*long*/ adjustment, double value) {
	lock.lock();
	try {
		_gtk_adjustment_set_value(adjustment, value);
	} finally {
		lock.unlock();
	}
}
/** @param adjustment cast=(GtkAdjustment *) */
public static final native void _gtk_adjustment_value_changed(int /*long*/ adjustment);
public static final void gtk_adjustment_value_changed(int /*long*/ adjustment) {
	lock.lock();
	try {
		_gtk_adjustment_value_changed(adjustment);
	} finally {
		lock.unlock();
	}
}
/**
 * @param arrow_type cast=(GtkArrowType)
 * @param shadow_type cast=(GtkShadowType)
 */
public static final native int /*long*/ _gtk_arrow_new(int arrow_type, int shadow_type);
public static final int /*long*/ gtk_arrow_new(int arrow_type, int shadow_type) {
	lock.lock();
	try {
		return _gtk_arrow_new(arrow_type, shadow_type);
	} finally {
		lock.unlock();
	}
}
/**
 * @param arrow cast=(GtkArrow *)
 * @param arrow_type cast=(GtkArrowType)
 * @param shadow_type cast=(GtkShadowType)
 */
public static final native void _gtk_arrow_set(int /*long*/ arrow, int arrow_type, int shadow_type);
public static final void gtk_arrow_set(int /*long*/ arrow, int arrow_type, int shadow_type) {
	lock.lock();
	try {
		_gtk_arrow_set(arrow, arrow_type, shadow_type);
	} finally {
		lock.unlock();
	}
}
/** @param bin cast=(GtkBin *) */
public static final native int /*long*/ _gtk_bin_get_child(int /*long*/ bin);
public static final int /*long*/ gtk_bin_get_child(int /*long*/ bin) {
	lock.lock();
	try {
		return _gtk_bin_get_child(bin);
	} finally {
		lock.unlock();
	}
}
/** @param border cast=(GtkBorder *) */
public static final native void _gtk_border_free(int /*long*/ border);
public static final void gtk_border_free(int /*long*/ border) {
	lock.lock();
	try {
		_gtk_border_free(border);
	} finally {
		lock.unlock();
	}
}
/** @param box cast=(GtkBox *) */
public static final native void _gtk_box_set_spacing(int /*long*/ box, int spacing);
public static final void gtk_box_set_spacing(int /*long*/ box, int spacing) {
	lock.lock();
	try {
		_gtk_box_set_spacing(box, spacing);
	} finally {
		lock.unlock();
	}
}
/**
 * @param box cast=(GtkBox *)
 * @param child cast=(GtkWidget *)
 */
public static final native void _gtk_box_set_child_packing(int /*long*/ box, int /*long*/ child, boolean expand, boolean fill, int padding, int pack_type);
public static final void gtk_box_set_child_packing(int /*long*/ box, int /*long*/ child, boolean expand, boolean fill, int padding, int pack_type) {
	lock.lock();
	try {
		_gtk_box_set_child_packing(box, child, expand, fill, padding, pack_type);
	} finally {
		lock.unlock();
	}
}
/** @param button cast=(GtkButton *) */
public static final native void _gtk_button_clicked(int /*long*/ button);
public static final void gtk_button_clicked(int /*long*/ button) {
	lock.lock();
	try {
		_gtk_button_clicked(button);
	} finally {
		lock.unlock();
	}
}
/** @param button cast=(GtkButton *) */
public static final native int _gtk_button_get_relief(int /*long*/ button);
public static final int gtk_button_get_relief(int /*long*/ button) {
	lock.lock();
	try {
		return _gtk_button_get_relief(button);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gtk_button_new();
public static final int /*long*/ gtk_button_new() {
	lock.lock();
	try {
		return _gtk_button_new();
	} finally {
		lock.unlock();
	}
}
/**
 * @param button cast=(GtkButton *)
 * @param newstyle cast=(GtkReliefStyle)
 */
public static final native void _gtk_button_set_relief(int /*long*/ button, int newstyle);
public static final void gtk_button_set_relief(int /*long*/ button, int newstyle) {
	lock.lock();
	try {
		_gtk_button_set_relief(button, newstyle);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _gtk_calendar_new();
public static final int /*long*/ gtk_calendar_new() {
	lock.lock();
	try {
		return _gtk_calendar_new();
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param calendar cast=(GtkCalendar *)
 * @param month cast=(guint)
 * @param year cast=(guint)
 */
public static final native boolean /*long*/ _gtk_calendar_select_month(int /*long*/ calendar, int month, int year);
public static final boolean /*long*/ gtk_calendar_select_month(int /*long*/ calendar, int month, int year) {
	lock.lock();
	try {
		return _gtk_calendar_select_month(calendar, month, year);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param calendar cast=(GtkCalendar *)
 * @param day cast=(guint)
 */
public static final native void _gtk_calendar_select_day(int /*long*/ calendar, int day);
public static final void gtk_calendar_select_day(int /*long*/ calendar, int day) {
	lock.lock();
	try {
		_gtk_calendar_select_day(calendar, day);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param calendar cast=(GtkCalendar *)
 * @param flags cast=(GtkCalendarDisplayOptions)
 */
public static final native void _gtk_calendar_set_display_options(int /*long*/ calendar, int flags);
public static final void gtk_calendar_set_display_options(int /*long*/ calendar, int flags) {
	lock.lock();
	try {
		_gtk_calendar_set_display_options(calendar, flags);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param calendar cast=(GtkCalendar *)
 * @param flags cast=(GtkCalendarDisplayOptions)
 */
public static final native void _gtk_calendar_display_options(int /*long*/ calendar, int flags);
public static final void gtk_calendar_display_options(int /*long*/ calendar, int flags) {
	lock.lock();
	try {
		_gtk_calendar_display_options(calendar, flags);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param calendar cast=(GtkCalendar *)
 * @param year cast=(guint *)
 * @param month cast=(guint *)
 * @param day cast=(guint *)
 */
public static final native void _gtk_calendar_get_date(int /*long*/ calendar, int[] year, int[] month, int[] day);
public static final void gtk_calendar_get_date(int /*long*/ calendar, int[] year, int[] month, int[] day) {
	lock.lock();
	try {
		_gtk_calendar_get_date(calendar, year, month, day);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _gtk_cell_layout_clear(int /*long*/ cell_layout);
public static final void gtk_cell_layout_clear(int /*long*/ cell_layout) {
	lock.lock();
	try {
		_gtk_cell_layout_clear(cell_layout);
	} finally {
		lock.unlock();
	}
}
/** @method flags=no_gen */
public static final native void _gtk_cell_layout_set_attributes(int /*long*/ cell_layout, int /*long*/ cell, byte[] attribute, int column, int /*long*/ sentinel);
public static final void gtk_cell_layout_set_attributes(int /*long*/ cell_layout, int /*long*/ cell, byte[] attribute, int column, int /*long*/ sentinel) {
	lock.lock();
	try {
		_gtk_cell_layout_set_attributes(cell_layout, cell, attribute, column, sentinel);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _gtk_cell_layout_pack_start(int /*long*/ cell_layout, int /*long*/ cell, boolean expand);
public static final void gtk_cell_layout_pack_start(int /*long*/ cell_layout, int /*long*/ cell, boolean expand) {
	lock.lock();
	try {
		_gtk_cell_layout_pack_start(cell_layout, cell, expand);
	} finally {
		lock.unlock();
	}
}
/**
 * @param cell cast=(GtkCellRenderer *)
 * @param widget cast=(GtkWidget *)
 * @param area cast=(GdkRectangle *),flags=no_in
 * @param x_offset cast=(gint *)
 * @param y_offset cast=(gint *)
 * @param width cast=(gint *)
 * @param height cast=(gint *)
 */
public static final native void _gtk_cell_renderer_get_size(int /*long*/ cell, int /*long*/ widget, GdkRectangle area, int[] x_offset, int[] y_offset, int[] width, int[] height);
public static final void gtk_cell_renderer_get_size(int /*long*/ cell, int /*long*/ widget, GdkRectangle area, int[] x_offset, int[] y_offset, int[] width, int[] height) {
	lock.lock();
	try {
		_gtk_cell_renderer_get_size(cell, widget, area, x_offset, y_offset, width, height);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gtk_cell_renderer_pixbuf_new();
public static final int /*long*/ gtk_cell_renderer_pixbuf_new() {
	lock.lock();
	try {
		return _gtk_cell_renderer_pixbuf_new();
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gtk_cell_renderer_text_new();
public static final int /*long*/ gtk_cell_renderer_text_new() {
	lock.lock();
	try {
		return _gtk_cell_renderer_text_new();
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gtk_cell_renderer_toggle_new();
public static final int /*long*/ gtk_cell_renderer_toggle_new() {
	lock.lock();
	try {
		return _gtk_cell_renderer_toggle_new();
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gtk_check_button_new();
public static final int /*long*/ gtk_check_button_new() {
	lock.lock();
	try {
		return _gtk_check_button_new();
	} finally {
		lock.unlock();
	}
}
/** @param check_menu_item cast=(GtkCheckMenuItem *) */
public static final native boolean _gtk_check_menu_item_get_active(int /*long*/ check_menu_item);
public static final boolean gtk_check_menu_item_get_active(int /*long*/ check_menu_item) {
	lock.lock();
	try {
		return _gtk_check_menu_item_get_active(check_menu_item);
	} finally {
		lock.unlock();
	}
}
/** @param label cast=(const gchar *) */
public static final native int /*long*/ _gtk_check_menu_item_new_with_label(byte[] label);
public static final int /*long*/ gtk_check_menu_item_new_with_label(byte[] label) {
	lock.lock();
	try {
		return _gtk_check_menu_item_new_with_label(label);
	} finally {
		lock.unlock();
	}
}
/**
 * @param wid cast=(GtkCheckMenuItem *)
 * @param active cast=(gboolean)
 */
public static final native void _gtk_check_menu_item_set_active(int /*long*/ wid, boolean active);
public static final void gtk_check_menu_item_set_active(int /*long*/ wid, boolean active) {
	lock.lock();
	try {
		_gtk_check_menu_item_set_active(wid, active);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gtk_check_version(int required_major, int required_minor, int required_micro);
public static final int /*long*/ gtk_check_version(int required_major, int required_minor, int required_micro) {
	lock.lock();
	try {
		return _gtk_check_version(required_major, required_minor, required_micro);
	} finally {
		lock.unlock();
	}
}
/** @param clipboard cast=(GtkClipboard *) */
public static final native void _gtk_clipboard_clear(int /*long*/ clipboard);
public static final void gtk_clipboard_clear(int /*long*/ clipboard) {
	lock.lock();
	try {
		_gtk_clipboard_clear(clipboard);
	} finally {
		lock.unlock();
	}
}
/** @param selection cast=(GdkAtom) */
public static final native int /*long*/ _gtk_clipboard_get(int /*long*/ selection);
public static final int /*long*/ gtk_clipboard_get(int /*long*/ selection) {
	lock.lock();
	try {
		return _gtk_clipboard_get(selection);
	} finally {
		lock.unlock();
	}
}
/**
 * @param clipboard cast=(GtkClipboard *)
 * @param target cast=(const GtkTargetEntry *)
 * @param n_targets cast=(guint)
 * @param get_func cast=(GtkClipboardGetFunc)
 * @param clear_func cast=(GtkClipboardClearFunc)
 * @param user_data cast=(GObject *)
 */
public static final native boolean _gtk_clipboard_set_with_data(int /*long*/ clipboard, int /*long*/ target, int n_targets, int /*long*/ get_func, int /*long*/ clear_func, int /*long*/ user_data);
public static final boolean gtk_clipboard_set_with_data(int /*long*/ clipboard, int /*long*/ target, int n_targets, int /*long*/ get_func, int /*long*/ clear_func, int /*long*/ user_data) {
	lock.lock();
	try {
		return _gtk_clipboard_set_with_data(clipboard, target, n_targets, get_func, clear_func, user_data);
	} finally {
		lock.unlock();
	}
}
/**
 * @param clipboard cast=(GtkClipboard *)
 * @param target cast=(GdkAtom)
 */
public static final native int /*long*/ _gtk_clipboard_wait_for_contents(int /*long*/ clipboard, int /*long*/ target);
public static final int /*long*/ gtk_clipboard_wait_for_contents(int /*long*/ clipboard, int /*long*/ target) {
	lock.lock();
	try {
		return _gtk_clipboard_wait_for_contents(clipboard, target);
	} finally {
		lock.unlock();
	}
}
/** @param title cast=(const gchar *) */
public static final native int /*long*/ _gtk_color_selection_dialog_new(byte[] title);
public static final int /*long*/ gtk_color_selection_dialog_new(byte[] title) {
	lock.lock();
	try {
		return _gtk_color_selection_dialog_new(title);
	} finally {
		lock.unlock();
	}
}
/**
 * @param colorsel cast=(GtkColorSelection *)
 * @param color cast=(GdkColor *),flags=no_in
 */
public static final native void _gtk_color_selection_get_current_color(int /*long*/ colorsel, GdkColor color);
public static final void gtk_color_selection_get_current_color(int /*long*/ colorsel, GdkColor color) {
	lock.lock();
	try {
		_gtk_color_selection_get_current_color(colorsel, color);
	} finally {
		lock.unlock();
	}
}
/**
 * @param colorsel cast=(GtkColorSelection *)
 * @param color cast=(GdkColor *),flags=no_out
 */
public static final native void _gtk_color_selection_set_current_color(int /*long*/ colorsel, GdkColor color);
public static final void gtk_color_selection_set_current_color(int /*long*/ colorsel, GdkColor color) {
	lock.lock();
	try {
		_gtk_color_selection_set_current_color(colorsel, color);
	} finally {
		lock.unlock();
	}
}
/** @param colorsel cast=(GtkColorSelection *) */
public static final native void _gtk_color_selection_set_has_palette(int /*long*/ colorsel, boolean has_palette);
public static final void gtk_color_selection_set_has_palette(int /*long*/ colorsel, boolean has_palette) {
	lock.lock();
	try {
		_gtk_color_selection_set_has_palette(colorsel, has_palette);
	} finally {
		lock.unlock();
	}
}
/** @param combo cast=(GtkCombo *) */
public static final native void _gtk_combo_disable_activate(int /*long*/ combo);
public static final void gtk_combo_disable_activate(int /*long*/ combo) {
	lock.lock();
	try {
		_gtk_combo_disable_activate(combo);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gtk_combo_new();
public static final int /*long*/ gtk_combo_new() {
	lock.lock();
	try {
		return _gtk_combo_new();
	} finally {
		lock.unlock();
	}
}
/**
 * @param combo cast=(GtkCombo *)
 * @param val cast=(gboolean)
 */
public static final native void _gtk_combo_set_case_sensitive(int /*long*/ combo, boolean val);
public static final void gtk_combo_set_case_sensitive(int /*long*/ combo, boolean val) {
	lock.lock();
	try {
		_gtk_combo_set_case_sensitive(combo, val);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _gtk_combo_box_set_focus_on_click(int /*long*/ combo, boolean val);
public static final void gtk_combo_box_set_focus_on_click(int /*long*/ combo, boolean val) {
	lock.lock();
	try {
		_gtk_combo_box_set_focus_on_click(combo, val);
	} finally {
		lock.unlock();
	}
}
/**
 * @param combo cast=(GtkCombo *)
 * @param strings cast=(GList *)
 */
public static final native void _gtk_combo_set_popdown_strings(int /*long*/ combo, int /*long*/ strings);
public static final void gtk_combo_set_popdown_strings(int /*long*/ combo, int /*long*/ strings) {
	lock.lock();
	try {
		_gtk_combo_set_popdown_strings(combo, strings);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _gtk_combo_box_entry_new_text();
public static final int /*long*/ gtk_combo_box_entry_new_text() {
	lock.lock();
	try {
		return _gtk_combo_box_entry_new_text();
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _gtk_combo_box_new_text();
public static final int /*long*/ gtk_combo_box_new_text() {
	lock.lock();
	try {
		return _gtk_combo_box_new_text();
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _gtk_combo_box_insert_text(int /*long*/ combo_box, int position, byte[] text);
public static final void gtk_combo_box_insert_text(int /*long*/ combo_box, int position, byte[] text) {
	lock.lock();
	try {
		_gtk_combo_box_insert_text(combo_box, position, text);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _gtk_combo_box_remove_text(int /*long*/ combo_box, int position);
public static final void gtk_combo_box_remove_text(int /*long*/ combo_box, int position) {
	lock.lock();
	try {
		_gtk_combo_box_remove_text(combo_box, position);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int _gtk_combo_box_get_active(int /*long*/ combo_box);
public static final int gtk_combo_box_get_active(int /*long*/ combo_box) {
	lock.lock();
	try {
		return _gtk_combo_box_get_active(combo_box);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _gtk_combo_box_get_model(int /*long*/ combo_box);
public static final int /*long*/ gtk_combo_box_get_model(int /*long*/ combo_box) {
	lock.lock();
	try {
		return _gtk_combo_box_get_model(combo_box);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _gtk_combo_box_set_active(int /*long*/ combo_box, int index);
public static final void gtk_combo_box_set_active(int /*long*/ combo_box, int index) {
	lock.lock();
	try {
		_gtk_combo_box_set_active(combo_box, index);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _gtk_combo_box_popup(int /*long*/ combo_box);
public static final void gtk_combo_box_popup(int /*long*/ combo_box) {
	lock.lock();
	try {
		_gtk_combo_box_popup(combo_box);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _gtk_combo_box_popdown(int /*long*/ combo_box);
public static final void gtk_combo_box_popdown(int /*long*/ combo_box) {
	lock.lock();
	try {
		_gtk_combo_box_popdown(combo_box);
	} finally {
		lock.unlock();
	}
}
/**
 * @param container cast=(GtkContainer *)
 * @param widget cast=(GtkWidget *)
 */
public static final native void _gtk_container_add(int /*long*/ container, int /*long*/ widget);
public static final void gtk_container_add(int /*long*/ container, int /*long*/ widget) {
	lock.lock();
	try {
		_gtk_container_add(container, widget);
	} finally {
		lock.unlock();
	}
}
/**
 * @param container cast=(GtkContainer *)
 * @param callback cast=(GtkCallback)
 * @param callback_data cast=(gpointer)
 */
public static final native void _gtk_container_forall(int /*long*/ container, int /*long*/ callback, int /*long*/ callback_data);
public static final void gtk_container_forall(int /*long*/ container, int /*long*/ callback, int /*long*/ callback_data) {
	lock.lock();
	try {
		_gtk_container_forall(container, callback, callback_data);
	} finally {
		lock.unlock();
	}
}
/** @param container cast=(GtkContainer *) */
public static final native int _gtk_container_get_border_width(int /*long*/ container);
public static final int gtk_container_get_border_width(int /*long*/ container) {
	lock.lock();
	try {
		return _gtk_container_get_border_width(container);
	} finally {
		lock.unlock();
	}
}
/** @param container cast=(GtkContainer *) */
public static final native int /*long*/ _gtk_container_get_children(int /*long*/ container);
public static final int /*long*/ gtk_container_get_children(int /*long*/ container) {
	lock.lock();
	try {
		return _gtk_container_get_children(container);
	} finally {
		lock.unlock();
	}
}
/**
 * @param container cast=(GtkContainer *)
 * @param widget cast=(GtkWidget *)
 */
public static final native void _gtk_container_remove(int /*long*/ container, int /*long*/ widget);
public static final void gtk_container_remove(int /*long*/ container, int /*long*/ widget) {
	lock.lock();
	try {
		_gtk_container_remove(container, widget);
	} finally {
		lock.unlock();
	}
}
/** @param container cast=(GtkContainer *) */
public static final native void _gtk_container_resize_children(int /*long*/ container);
public static final void gtk_container_resize_children(int /*long*/ container) {
	lock.lock();
	try {
		_gtk_container_resize_children(container);
	} finally {
		lock.unlock();
	}
}
/**
 * @param container cast=(GtkContainer *)
 * @param border_width cast=(guint)
 */
public static final native void _gtk_container_set_border_width(int /*long*/ container, int border_width);
public static final void gtk_container_set_border_width(int /*long*/ container, int border_width) {
	lock.lock();
	try {
		_gtk_container_set_border_width(container, border_width);
	} finally {
		lock.unlock();
	}
}
/**
 * @param dialog cast=(GtkDialog *)
 * @param button_text cast=(const gchar *)
 * @param response_id cast=(gint)
 */
public static final native int /*long*/ _gtk_dialog_add_button(int /*long*/ dialog, byte[]  button_text, int response_id);
public static final int /*long*/ gtk_dialog_add_button(int /*long*/ dialog, byte[]  button_text, int response_id) {
	lock.lock();
	try {
		return _gtk_dialog_add_button(dialog, button_text, response_id);
	} finally {
		lock.unlock();
	}
}
/** @param dialog cast=(GtkDialog *) */
public static final native int _gtk_dialog_run(int /*long*/ dialog);
public static final int gtk_dialog_run(int /*long*/ dialog) {
	lock.lock();
	try {
		return _gtk_dialog_run(dialog);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(GtkWidget *)
 * @param targets cast=(GtkTargetList *)
 * @param actions cast=(GdkDragAction)
 * @param button cast=(gint)
 * @param event cast=(GdkEvent *)
 */
public static final native int /*long*/ _gtk_drag_begin(int /*long*/ widget, int /*long*/ targets, int actions, int button, int /*long*/ event);
public static final int /*long*/ gtk_drag_begin(int /*long*/ widget, int /*long*/ targets, int actions, int button, int /*long*/ event) {
	lock.lock();
	try {
		return _gtk_drag_begin(widget, targets, actions, button, event);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(GtkWidget *)
 * @param start_x cast=(gint)
 * @param start_y cast=(gint)
 * @param current_x cast=(gint)
 * @param current_y cast=(gint)
 */
public static final native boolean _gtk_drag_check_threshold(int /*long*/ widget, int start_x, int start_y, int current_x, int current_y);
public static final boolean gtk_drag_check_threshold(int /*long*/ widget, int start_x, int start_y, int current_x, int current_y) {
	lock.lock();
	try {
		return _gtk_drag_check_threshold(widget, start_x, start_y, current_x, current_y);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(GtkWidget *)
 * @param context cast=(GdkDragContext *)
 * @param target_list cast=(GtkTargetList *)
 */
public static final native int /*long*/ _gtk_drag_dest_find_target(int /*long*/ widget, int /*long*/ context, int /*long*/ target_list);
public static final int /*long*/ gtk_drag_dest_find_target(int /*long*/ widget, int /*long*/ context, int /*long*/ target_list) {
	lock.lock();
	try {
		return _gtk_drag_dest_find_target(widget, context, target_list);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(GtkWidget *)
 * @param flags cast=(GtkDestDefaults)
 * @param targets cast=(const GtkTargetEntry *)
 * @param n_targets cast=(gint)
 * @param actions cast=(GdkDragAction)
 */
public static final native void _gtk_drag_dest_set(int /*long*/ widget, int flags, int /*long*/ targets, int n_targets, int actions);
public static final void gtk_drag_dest_set(int /*long*/ widget, int flags, int /*long*/ targets, int n_targets, int actions) {
	lock.lock();
	try {
		_gtk_drag_dest_set(widget, flags, targets, n_targets, actions);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(GtkWidget *) */
public static final native void _gtk_drag_dest_unset(int /*long*/ widget);
public static final void gtk_drag_dest_unset(int /*long*/ widget) {
	lock.lock();
	try {
		_gtk_drag_dest_unset(widget);
	} finally {
		lock.unlock();
	}
}
/**
 * @param context cast=(GdkDragContext *)
 * @param success cast=(gboolean)
 * @param delete cast=(gboolean)
 * @param time cast=(guint32)
 */
public static final native void _gtk_drag_finish(int /*long*/ context, boolean success, boolean delete, int time);
public static final void gtk_drag_finish(int /*long*/ context, boolean success, boolean delete, int time) {
	lock.lock();
	try {
		_gtk_drag_finish(context, success, delete, time);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(GtkWidget *)
 * @param context cast=(GdkDragContext *)
 * @param target cast=(GdkAtom)
 * @param time cast=(guint32)
 */
public static final native void _gtk_drag_get_data(int /*long*/ widget, int /*long*/ context, int /*long*/ target, int time);
public static final void gtk_drag_get_data(int /*long*/ widget, int /*long*/ context, int /*long*/ target, int time) {
	lock.lock();
	try {
		_gtk_drag_get_data(widget, context, target, time);
	} finally {
		lock.unlock();
	}
}
/**
 * @param context cast=(GdkDragContext *)
 * @param pixbuf cast=(GdkPixbuf *)
 */
public static final native void _gtk_drag_set_icon_pixbuf(int /*long*/ context, int /*long*/ pixbuf, int hot_x, int hot_y);
public static final void gtk_drag_set_icon_pixbuf(int /*long*/ context, int /*long*/ pixbuf, int hot_x, int hot_y) {
	lock.lock();
	try {
		_gtk_drag_set_icon_pixbuf(context, pixbuf, hot_x, hot_y);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gtk_drawing_area_new();
public static final int /*long*/ gtk_drawing_area_new() {
	lock.lock();
	try {
		return _gtk_drawing_area_new();
	} finally {
		lock.unlock();
	}
}
/** @param editable cast=(GtkEditable *) */
public static final native void _gtk_editable_copy_clipboard(int /*long*/ editable);
public static final void gtk_editable_copy_clipboard(int /*long*/ editable) {
	lock.lock();
	try {
		_gtk_editable_copy_clipboard(editable);
	} finally {
		lock.unlock();
	}
}
/** @param editable cast=(GtkEditable *) */
public static final native void _gtk_editable_cut_clipboard(int /*long*/ editable);
public static final void gtk_editable_cut_clipboard(int /*long*/ editable) {
	lock.lock();
	try {
		_gtk_editable_cut_clipboard(editable);
	} finally {
		lock.unlock();
	}
}
/** @param editable cast=(GtkEditable *) */
public static final native void _gtk_editable_delete_selection(int /*long*/ editable);
public static final void gtk_editable_delete_selection(int /*long*/ editable) {
	lock.lock();
	try {
		_gtk_editable_delete_selection(editable);
	} finally {
		lock.unlock();
	}
}
/**
 * @param editable cast=(GtkEditable *)
 * @param start_pos cast=(gint)
 * @param end_pos cast=(gint)
 */
public static final native void _gtk_editable_delete_text(int /*long*/ editable, int start_pos, int end_pos);
public static final void gtk_editable_delete_text(int /*long*/ editable, int start_pos, int end_pos) {
	lock.lock();
	try {
		_gtk_editable_delete_text(editable, start_pos, end_pos);
	} finally {
		lock.unlock();
	}
}
/**
 * @param editable cast=(GtkEditable *)
 * @param start_pos cast=(gint)
 * @param end_pos cast=(gint)
 */
public static final native int /*long*/ _gtk_editable_get_chars(int /*long*/ editable, int start_pos, int end_pos);
public static final int /*long*/ gtk_editable_get_chars(int /*long*/ editable, int start_pos, int end_pos) {
	lock.lock();
	try {
		return _gtk_editable_get_chars(editable, start_pos, end_pos);
	} finally {
		lock.unlock();
	}
}
/** @param editable cast=(GtkEditable *) */
public static final native boolean _gtk_editable_get_editable(int /*long*/ editable);
public static final boolean gtk_editable_get_editable(int /*long*/ editable) {
	lock.lock();
	try {
		return _gtk_editable_get_editable(editable);
	} finally {
		lock.unlock();
	}
}
/** @param editable cast=(GtkEditable *) */
public static final native int _gtk_editable_get_position(int /*long*/ editable);
public static final int gtk_editable_get_position(int /*long*/ editable) {
	lock.lock();
	try {
		return _gtk_editable_get_position(editable);
	} finally {
		lock.unlock();
	}
}
/**
 * @param editable cast=(GtkEditable *)
 * @param start cast=(gint *)
 * @param end cast=(gint *)
 */
public static final native boolean _gtk_editable_get_selection_bounds(int /*long*/ editable, int[] start, int[] end);
public static final boolean gtk_editable_get_selection_bounds(int /*long*/ editable, int[] start, int[] end) {
	lock.lock();
	try {
		return _gtk_editable_get_selection_bounds(editable, start, end);
	} finally {
		lock.unlock();
	}
}
/**
 * @param editable cast=(GtkEditable *)
 * @param new_text cast=(gchar *)
 * @param new_text_length cast=(gint)
 * @param position cast=(gint *)
 */
public static final native void _gtk_editable_insert_text(int /*long*/ editable, byte[] new_text, int new_text_length, int[] position);
public static final void gtk_editable_insert_text(int /*long*/ editable, byte[] new_text, int new_text_length, int[] position) {
	lock.lock();
	try {
		_gtk_editable_insert_text(editable, new_text, new_text_length, position);
	} finally {
		lock.unlock();
	}
}
/** @param editable cast=(GtkEditable *) */
public static final native void _gtk_editable_paste_clipboard(int /*long*/ editable);
public static final void gtk_editable_paste_clipboard(int /*long*/ editable) {
	lock.lock();
	try {
		_gtk_editable_paste_clipboard(editable);
	} finally {
		lock.unlock();
	}
}
/**
 * @param editable cast=(GtkEditable *)
 * @param start cast=(gint)
 * @param end cast=(gint)
 */
public static final native void _gtk_editable_select_region(int /*long*/ editable, int start, int end);
public static final void gtk_editable_select_region(int /*long*/ editable, int start, int end) {
	lock.lock();
	try {
		_gtk_editable_select_region(editable, start, end);
	} finally {
		lock.unlock();
	}
}
/**
 * @param entry cast=(GtkEditable *)
 * @param editable cast=(gboolean)
 */
public static final native void _gtk_editable_set_editable(int /*long*/ entry, boolean editable);
public static final void gtk_editable_set_editable(int /*long*/ entry, boolean editable) {
	lock.lock();
	try {
		_gtk_editable_set_editable(entry, editable);
	} finally {
		lock.unlock();
	}
}
/**
 * @param editable cast=(GtkEditable *)
 * @param position cast=(gint)
 */
public static final native void _gtk_editable_set_position(int /*long*/ editable, int position);
public static final void gtk_editable_set_position(int /*long*/ editable, int position) {
	lock.lock();
	try {
		_gtk_editable_set_position(editable, position);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _gtk_entry_get_inner_border (int /*long*/ entry);
public static final int /*long*/ gtk_entry_get_inner_border (int /*long*/ entry) {
	lock.lock();
	try {
		return _gtk_entry_get_inner_border(entry);
	} finally {
		lock.unlock();
	}
}
/** @param entry cast=(GtkEntry *) */
public static final native char _gtk_entry_get_invisible_char(int /*long*/ entry);
public static final char gtk_entry_get_invisible_char(int /*long*/ entry) {
	lock.lock();
	try {
		return _gtk_entry_get_invisible_char(entry);
	} finally {
		lock.unlock();
	}
}
/** @param entry cast=(GtkEntry *) */
public static final native int /*long*/ _gtk_entry_get_layout (int /*long*/ entry);
public static final int /*long*/ gtk_entry_get_layout (int /*long*/ entry) {
	lock.lock();
	try {
		return _gtk_entry_get_layout(entry);
	} finally {
		lock.unlock();
	}
}
/** @param entry cast=(GtkEntry *) */
public static final native void _gtk_entry_get_layout_offsets (int /*long*/ entry, int[] x, int[] y);
public static final void gtk_entry_get_layout_offsets (int /*long*/ entry, int[] x, int[] y) {
	lock.lock();
	try {
		_gtk_entry_get_layout_offsets(entry, x, y);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int _gtk_entry_text_index_to_layout_index (int /*long*/ entry, int index);
public static final int gtk_entry_text_index_to_layout_index (int /*long*/ entry, int index) {
	lock.lock();
	try {
		return _gtk_entry_text_index_to_layout_index(entry, index);
	} finally {
		lock.unlock();
	}
}
/** @param entry cast=(GtkEntry *) */
public static final native int _gtk_entry_get_max_length(int /*long*/ entry);
public static final int gtk_entry_get_max_length(int /*long*/ entry) {
	lock.lock();
	try {
		return _gtk_entry_get_max_length(entry);
	} finally {
		lock.unlock();
	}
}
/** @param entry cast=(GtkEntry *) */
public static final native int /*long*/ _gtk_entry_get_text(int /*long*/ entry);
public static final int /*long*/ gtk_entry_get_text(int /*long*/ entry) {
	lock.lock();
	try {
		return _gtk_entry_get_text(entry);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native boolean _FcConfigAppFontAddFile(int /*long*/ config, byte[] file);
public static final boolean FcConfigAppFontAddFile(int /*long*/ config, byte[] file) {
	lock.lock();
	try {
		return _FcConfigAppFontAddFile(config, file);
	} finally {
		lock.unlock();
	}
}
/** @param entry cast=(GtkEntry *) */
public static final native boolean _gtk_entry_get_visibility(int /*long*/ entry);
public static final boolean gtk_entry_get_visibility(int /*long*/ entry) {
	lock.lock();
	try {
		return _gtk_entry_get_visibility(entry);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gtk_entry_new();
public static final int /*long*/ gtk_entry_new() {
	lock.lock();
	try {
		return _gtk_entry_new();
	} finally {
		lock.unlock();
	}
}
/**
 * @param entry cast=(GtkEntry *)
 * @param setting cast=(gboolean)
 */
public static final native void _gtk_entry_set_activates_default(int /*long*/ entry, boolean setting);
public static final void gtk_entry_set_activates_default(int /*long*/ entry, boolean setting) {
	lock.lock();
	try {
		_gtk_entry_set_activates_default(entry, setting);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param entry cast=(GtkEntry *)
 * @param xalign cast=(gfloat)
 */
public static final native void _gtk_entry_set_alignment(int /*long*/ entry, float xalign);
public static final void gtk_entry_set_alignment(int /*long*/ entry, float xalign) {
	lock.lock();
	try {
		_gtk_entry_set_alignment(entry, xalign);
	} finally {
		lock.unlock();
	}
}
/**
 * @param entry cast=(GtkEntry *)
 * @param setting cast=(gboolean)
 */
public static final native void _gtk_entry_set_has_frame(int /*long*/ entry, boolean setting);
public static final void gtk_entry_set_has_frame(int /*long*/ entry, boolean setting) {
	lock.lock();
	try {
		_gtk_entry_set_has_frame(entry, setting);
	} finally {
		lock.unlock();
	}
}
/**
 * @param entry cast=(GtkEntry *)
 * @param ch cast=(gint)
 */
public static final native void _gtk_entry_set_invisible_char(int /*long*/ entry, char ch);
public static final void gtk_entry_set_invisible_char(int /*long*/ entry, char ch) {
	lock.lock();
	try {
		_gtk_entry_set_invisible_char(entry, ch);
	} finally {
		lock.unlock();
	}
}
/**
 * @param entry cast=(GtkEntry *)
 * @param max cast=(gint)
 */
public static final native void _gtk_entry_set_max_length(int /*long*/ entry, int max);
public static final void gtk_entry_set_max_length(int /*long*/ entry, int max) {
	lock.lock();
	try {
		_gtk_entry_set_max_length(entry, max);
	} finally {
		lock.unlock();
	}
}
/**
 * @param entry cast=(GtkEntry *)
 * @param text cast=(const gchar *)
 */
public static final native void _gtk_entry_set_text(int /*long*/ entry, byte[] text);
public static final void gtk_entry_set_text(int /*long*/ entry, byte[] text) {
	lock.lock();
	try {
		_gtk_entry_set_text(entry, text);
	} finally {
		lock.unlock();
	}
}
/**
 * @param entry cast=(GtkEntry *)
 * @param visible cast=(gboolean)
 */
public static final native void _gtk_entry_set_visibility(int /*long*/ entry, boolean visible);
public static final void gtk_entry_set_visibility(int /*long*/ entry, boolean visible) {
	lock.lock();
	try {
		_gtk_entry_set_visibility(entry, visible);
	} finally {
		lock.unlock();
	}
}
public static final native int _gtk_events_pending();
public static final int gtk_events_pending() {
	lock.lock();
	try {
		return _gtk_events_pending();
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native boolean _gtk_expander_get_expanded(int /*long*/ expander);
public static final boolean gtk_expander_get_expanded(int /*long*/ expander) {
	lock.lock();
	try {
		return _gtk_expander_get_expanded(expander);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _gtk_expander_get_label_widget(int /*long*/ expander);
public static final int /*long*/ gtk_expander_get_label_widget(int /*long*/ expander) {
	lock.lock();
	try {
		return _gtk_expander_get_label_widget(expander);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param label cast=(const gchar *)
 */
public static final native int /*long*/ _gtk_expander_new(byte[] label);
public static final int /*long*/ gtk_expander_new(byte[] label) {
	lock.lock();
	try {
		return _gtk_expander_new(label);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _gtk_expander_set_expanded(int /*long*/ expander, boolean expanded);
public static final void gtk_expander_set_expanded(int /*long*/ expander, boolean expanded) {
	lock.lock();
	try {
		_gtk_expander_set_expanded(expander, expanded);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param label cast=(const gchar *)
 */
public static final native void _gtk_expander_set_label(int /*long*/ expander, byte[] label);
public static final void gtk_expander_set_label(int /*long*/ expander, byte[] label) {
	lock.lock();
	try {
		_gtk_expander_set_label(expander, label);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _gtk_expander_set_label_widget(int /*long*/ expander, int /*long*/ label_widget);
public static final void  gtk_expander_set_label_widget(int /*long*/ expander, int /*long*/ label_widget) {
	lock.lock();
	try {
		_gtk_expander_set_label_widget(expander, label_widget);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _gtk_file_chooser_add_filter(int /*long*/ chooser, int /*long*/ filter);
public static final void gtk_file_chooser_add_filter(int /*long*/ chooser, int /*long*/ filter) {
	lock.lock();
	try {
		_gtk_file_chooser_add_filter(chooser, filter);
	} finally {
		lock.unlock();
	}
}
/** @method flags=no_gen */
public static final native int /*long*/ _gtk_file_chooser_dialog_new(byte[] title, int /*long*/ parent, int action, int /*long*/ first_button_text, int first_button_id, int /*long*/ second_button_text, int second_button_id, int /*long*/ terminator);
public static final int /*long*/ gtk_file_chooser_dialog_new(byte[] title, int /*long*/ parent, int action, int /*long*/ first_button_text, int first_button_id, int /*long*/ second_button_text, int second_button_id, int /*long*/ terminator) {
	lock.lock();
	try {
		return _gtk_file_chooser_dialog_new(title, parent, action, first_button_text, first_button_id, second_button_text, second_button_id, terminator);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _gtk_file_chooser_get_current_folder(int /*long*/ chooser);
public static final int /*long*/ gtk_file_chooser_get_current_folder(int /*long*/ chooser) {
	lock.lock();
	try {
		return _gtk_file_chooser_get_current_folder(chooser);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _gtk_file_chooser_get_filename(int /*long*/ chooser);
public static final int /*long*/ gtk_file_chooser_get_filename(int /*long*/ chooser) {
	lock.lock();
	try {
		return _gtk_file_chooser_get_filename(chooser);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _gtk_file_chooser_get_filenames(int /*long*/ chooser);
public static final int /*long*/ gtk_file_chooser_get_filenames(int /*long*/ chooser) {
	lock.lock();
	try {
		return _gtk_file_chooser_get_filenames(chooser);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _gtk_file_chooser_get_uri(int /*long*/ chooser);
public static final int /*long*/ gtk_file_chooser_get_uri(int /*long*/ chooser) {
	lock.lock();
	try {
		return _gtk_file_chooser_get_uri(chooser);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _gtk_file_chooser_get_uris(int /*long*/ chooser);
public static final int /*long*/ gtk_file_chooser_get_uris(int /*long*/ chooser) {
	lock.lock();
	try {
		return _gtk_file_chooser_get_uris(chooser);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _gtk_file_chooser_get_filter(int /*long*/ chooser);
public static final int /*long*/ gtk_file_chooser_get_filter(int /*long*/ chooser) {
	lock.lock();
	try {
		return _gtk_file_chooser_get_filter(chooser);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _gtk_file_chooser_set_current_folder(int /*long*/ chooser, int /*long*/ filename);
public static final void gtk_file_chooser_set_current_folder(int /*long*/ chooser, int /*long*/ filename) {
	lock.lock();
	try {
		_gtk_file_chooser_set_current_folder(chooser, filename);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _gtk_file_chooser_set_current_folder_uri(int /*long*/ chooser, byte [] uri);
public static final void gtk_file_chooser_set_current_folder_uri(int /*long*/ chooser, byte [] uri) {
	lock.lock();
	try {
		_gtk_file_chooser_set_current_folder_uri(chooser, uri);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _gtk_file_chooser_set_current_name(int /*long*/ chooser, byte[] name);
public static final void gtk_file_chooser_set_current_name(int /*long*/ chooser, byte[] name) {
	lock.lock();
	try {
		_gtk_file_chooser_set_current_name(chooser, name);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _gtk_file_chooser_set_local_only(int /*long*/ chooser, boolean local_only);
public static final void gtk_file_chooser_set_local_only(int /*long*/ chooser, boolean local_only) {
	lock.lock();
	try {
		_gtk_file_chooser_set_local_only(chooser, local_only);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _gtk_file_chooser_set_do_overwrite_confirmation(int /*long*/ chooser, boolean do_overwrite_confirmation);
public static final void gtk_file_chooser_set_do_overwrite_confirmation(int /*long*/ chooser, boolean do_overwrite_confirmation) {
	lock.lock();
	try {
		_gtk_file_chooser_set_do_overwrite_confirmation(chooser, do_overwrite_confirmation);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _gtk_file_chooser_set_extra_widget(int /*long*/ chooser, int /*long*/ extra_widget);
public static final void gtk_file_chooser_set_extra_widget(int /*long*/ chooser, int /*long*/ extra_widget) {
	lock.lock();
	try {
		_gtk_file_chooser_set_extra_widget(chooser, extra_widget);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _gtk_file_chooser_set_filename(int /*long*/ chooser, int /*long*/ name);
public static final void gtk_file_chooser_set_filename(int /*long*/ chooser, int /*long*/ name) {
	lock.lock();
	try {
		_gtk_file_chooser_set_filename(chooser, name);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _gtk_file_chooser_set_filter(int /*long*/ chooser, int /*long*/ filter);
public static final void gtk_file_chooser_set_filter(int /*long*/ chooser, int /*long*/ filter) {
	lock.lock();
	try {
		_gtk_file_chooser_set_filter(chooser, filter);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _gtk_file_chooser_set_uri(int /*long*/ chooser, byte [] uri);
public static final void gtk_file_chooser_set_uri(int /*long*/ chooser, byte [] uri) {
	lock.lock();
	try {
		_gtk_file_chooser_set_uri(chooser, uri);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _gtk_file_chooser_set_select_multiple(int /*long*/ chooser, boolean select_multiple);
public static final void gtk_file_chooser_set_select_multiple(int /*long*/ chooser, boolean select_multiple) {
	lock.lock();
	try {
		_gtk_file_chooser_set_select_multiple(chooser, select_multiple);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _gtk_file_filter_add_pattern(int /*long*/ filter, byte[] pattern);
public static final void gtk_file_filter_add_pattern(int /*long*/ filter, byte[] pattern) {
	lock.lock();
	try {
		_gtk_file_filter_add_pattern(filter, pattern);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _gtk_file_filter_new();
public static final int /*long*/ gtk_file_filter_new() {
	lock.lock();
	try {
		return _gtk_file_filter_new();
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _gtk_file_filter_get_name(int /*long*/ filter);
public static final int /*long*/ gtk_file_filter_get_name(int /*long*/ filter) {
	lock.lock();
	try {
		return _gtk_file_filter_get_name(filter);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _gtk_file_filter_set_name(int /*long*/ filter, byte[] name);
public static final void gtk_file_filter_set_name(int /*long*/ filter, byte[] name) {
	lock.lock();
	try {
		_gtk_file_filter_set_name(filter, name);
	} finally {
		lock.unlock();
	}
}
/** @param filesel cast=(GtkFileSelection *) */
public static final native int /*long*/ _gtk_file_selection_get_filename(int /*long*/ filesel);
public static final int /*long*/ gtk_file_selection_get_filename(int /*long*/ filesel) {
	lock.lock();
	try {
		return _gtk_file_selection_get_filename(filesel);
	} finally {
		lock.unlock();
	}
}
/** @param filesel cast=(GtkFileSelection *) */
public static final native int /*long*/ _gtk_file_selection_get_selections(int /*long*/ filesel);
public static final int /*long*/ gtk_file_selection_get_selections(int /*long*/ filesel) {
	lock.lock();
	try {
		return _gtk_file_selection_get_selections(filesel);
	} finally {
		lock.unlock();
	}
}
/** @param filesel cast=(GtkFileSelection *) */
public static final native void _gtk_file_selection_hide_fileop_buttons(int /*long*/ filesel);
public static final void gtk_file_selection_hide_fileop_buttons(int /*long*/ filesel) {
	lock.lock();
	try {
		_gtk_file_selection_hide_fileop_buttons(filesel);
	} finally {
		lock.unlock();
	}
}
/** @param title cast=(const gchar *) */
public static final native int /*long*/ _gtk_file_selection_new(byte[] title);
public static final int /*long*/ gtk_file_selection_new(byte[] title) {
	lock.lock();
	try {
		return _gtk_file_selection_new(title);
	} finally {
		lock.unlock();
	}
}
/**
 * @param filesel cast=(GtkFileSelection *)
 * @param filename cast=(const gchar *)
 */
public static final native void _gtk_file_selection_set_filename(int /*long*/ filesel, int /*long*/ filename);
public static final void gtk_file_selection_set_filename(int /*long*/ filesel, int /*long*/ filename) {
	lock.lock();
	try {
		_gtk_file_selection_set_filename(filesel, filename);
	} finally {
		lock.unlock();
	}
}
/**
 * @param filesel cast=(GtkFileSelection *)
 * @param select_multiple cast=(gboolean)
 */
public static final native void _gtk_file_selection_set_select_multiple(int /*long*/ filesel, boolean select_multiple);
public static final void gtk_file_selection_set_select_multiple(int /*long*/ filesel, boolean select_multiple) {
	lock.lock();
	try {
		_gtk_file_selection_set_select_multiple(filesel, select_multiple);
	} finally {
		lock.unlock();
	}
}
/**
 * @param fixed cast=(GtkFixed *)
 * @param widget cast=(GtkWidget *)
 * @param x cast=(gint)
 * @param y cast=(gint)
 */
public static final native void _gtk_fixed_move(int /*long*/ fixed, int /*long*/ widget, int x, int y);
public static final void gtk_fixed_move(int /*long*/ fixed, int /*long*/ widget, int x, int y) {
	lock.lock();
	try {
		_gtk_fixed_move(fixed, widget, x, y);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gtk_fixed_new();
public static final int /*long*/ gtk_fixed_new() {
	lock.lock();
	try {
		return _gtk_fixed_new();
	} finally {
		lock.unlock();
	}
}
/**
 * @param fixed cast=(GtkFixed *)
 * @param has_window cast=(gboolean)
 */
public static final native void _gtk_fixed_set_has_window(int /*long*/ fixed, boolean has_window);
public static final void gtk_fixed_set_has_window(int /*long*/ fixed, boolean has_window) {
	lock.lock();
	try {
		_gtk_fixed_set_has_window(fixed, has_window);
	} finally {
		lock.unlock();
	}
}
/** @param fsd cast=(GtkFontSelectionDialog *) */
public static final native int /*long*/ _gtk_font_selection_dialog_get_font_name(int /*long*/ fsd);
public static final int /*long*/ gtk_font_selection_dialog_get_font_name(int /*long*/ fsd) {
	lock.lock();
	try {
		return _gtk_font_selection_dialog_get_font_name(fsd);
	} finally {
		lock.unlock();
	}
}
/** @param title cast=(const gchar *) */
public static final native int /*long*/ _gtk_font_selection_dialog_new(byte[] title);
public static final int /*long*/ gtk_font_selection_dialog_new(byte[] title) {
	lock.lock();
	try {
		return _gtk_font_selection_dialog_new(title);
	} finally {
		lock.unlock();
	}
}
/**
 * @param fsd cast=(GtkFontSelectionDialog *)
 * @param fontname cast=(const gchar *)
 */
public static final native boolean _gtk_font_selection_dialog_set_font_name(int /*long*/ fsd, byte[] fontname);
public static final boolean gtk_font_selection_dialog_set_font_name(int /*long*/ fsd, byte[] fontname) {
	lock.lock();
	try {
		return _gtk_font_selection_dialog_set_font_name(fsd, fontname);
	} finally {
		lock.unlock();
	}
}
/** @param label cast=(const gchar *) */
public static final native int /*long*/ _gtk_frame_new(byte[] label);
public static final int /*long*/ gtk_frame_new(byte[] label) {
	lock.lock();
	try {
		return _gtk_frame_new(label);
	} finally {
		lock.unlock();
	}
}
/** @param frame cast=(GtkFrame *) */
public static final native int /*long*/ _gtk_frame_get_label_widget(int /*long*/ frame);
public static final int /*long*/ gtk_frame_get_label_widget(int /*long*/ frame) {
	lock.lock();
	try {
		return _gtk_frame_get_label_widget(frame);
	} finally {
		lock.unlock();
	}
}
/**
 * @param frame cast=(GtkFrame *)
 * @param label cast=(const gchar *)
 */
public static final native void _gtk_frame_set_label(int /*long*/ frame, byte[] label);
public static final void gtk_frame_set_label(int /*long*/ frame, byte[] label) {
	lock.lock();
	try {
		_gtk_frame_set_label(frame, label);
	} finally {
		lock.unlock();
	}
}
/**
 * @param frame cast=(GtkFrame *)
 * @param label_widget cast=(GtkWidget *)
 */
public static final native void _gtk_frame_set_label_widget(int /*long*/ frame, int /*long*/ label_widget);
public static final void gtk_frame_set_label_widget(int /*long*/ frame, int /*long*/ label_widget) {
	lock.lock();
	try {
		_gtk_frame_set_label_widget(frame, label_widget);
	} finally {
		lock.unlock();
	}
}
/**
 * @param frame cast=(GtkFrame *)
 * @param type cast=(GtkShadowType)
 */
public static final native void _gtk_frame_set_shadow_type(int /*long*/ frame, int type);
public static final void gtk_frame_set_shadow_type(int /*long*/ frame, int type) {
	lock.lock();
	try {
		_gtk_frame_set_shadow_type(frame, type);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gtk_get_current_event();
public static final int /*long*/ gtk_get_current_event() {
	lock.lock();
	try {
		return _gtk_get_current_event();
	} finally {
		lock.unlock();
	}
}
/** @param state cast=(GdkModifierType*) */
public static final native boolean _gtk_get_current_event_state (int[] state);
public static final boolean gtk_get_current_event_state (int[] state) {
	lock.lock();
	try {
		return _gtk_get_current_event_state(state);
	} finally {
		lock.unlock();
	}
}
public static final native int _gtk_get_current_event_time();
public static final int gtk_get_current_event_time() {
	lock.lock();
	try {
		return _gtk_get_current_event_time();
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gtk_get_default_language();
public static final int /*long*/ gtk_get_default_language() {
	lock.lock();
	try {
		return _gtk_get_default_language();
	} finally {
		lock.unlock();
	}
}
/** @param event cast=(GdkEvent *) */
public static final native int /*long*/ _gtk_get_event_widget(int /*long*/ event);
public static final int /*long*/ gtk_get_event_widget(int /*long*/ event) {
	lock.lock();
	try {
		return _gtk_get_event_widget(event);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(GtkWidget *) */
public static final native void _gtk_grab_add(int /*long*/ widget);
public static final void gtk_grab_add(int /*long*/ widget) {
	lock.lock();
	try {
		_gtk_grab_add(widget);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gtk_grab_get_current();
public static final int /*long*/ gtk_grab_get_current() {
	lock.lock();
	try {
		return _gtk_grab_get_current();
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(GtkWidget *) */
public static final native void _gtk_grab_remove(int /*long*/ widget);
public static final void gtk_grab_remove(int /*long*/ widget) {
	lock.lock();
	try {
		_gtk_grab_remove(widget);
	} finally {
		lock.unlock();
	}
}
/**
 * @param homogeneous cast=(gboolean)
 * @param spacing cast=(gint)
 */
public static final native int /*long*/ _gtk_hbox_new(boolean homogeneous, int spacing);
public static final int /*long*/ gtk_hbox_new(boolean homogeneous, int spacing) {
	lock.lock();
	try {
		return _gtk_hbox_new(homogeneous, spacing);
	} finally {
		lock.unlock();
	}
}
/** @param adjustment cast=(GtkAdjustment *) */
public static final native int /*long*/ _gtk_hscale_new(int /*long*/ adjustment);
public static final int /*long*/ gtk_hscale_new(int /*long*/ adjustment) {
	lock.lock();
	try {
		return _gtk_hscale_new(adjustment);
	} finally {
		lock.unlock();
	}
}
/** @param adjustment cast=(GtkAdjustment *) */
public static final native int /*long*/ _gtk_hscrollbar_new(int /*long*/ adjustment);
public static final int /*long*/ gtk_hscrollbar_new(int /*long*/ adjustment) {
	lock.lock();
	try {
		return _gtk_hscrollbar_new(adjustment);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gtk_hseparator_new();
public static final int /*long*/ gtk_hseparator_new() {
	lock.lock();
	try {
		return _gtk_hseparator_new();
	} finally {
		lock.unlock();
	}
}
/** @param stock_id cast=(const gchar *) */
public static final native int /*long*/ _gtk_icon_factory_lookup_default(byte[] stock_id);
public static final int /*long*/ gtk_icon_factory_lookup_default(byte[] stock_id) {
	lock.lock();
	try {
		return _gtk_icon_factory_lookup_default(stock_id);
	} finally {
		lock.unlock();
	}
}
/** @param source cast=(GtkIconSource *) */
public static final native void _gtk_icon_source_free(int /*long*/ source);
public static final void gtk_icon_source_free(int /*long*/ source) {
	lock.lock();
	try {
		_gtk_icon_source_free(source);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gtk_icon_source_new();
public static final int /*long*/ gtk_icon_source_new() {
	lock.lock();
	try {
		return _gtk_icon_source_new();
	} finally {
		lock.unlock();
	}
}
/**
 * @param source cast=(GtkIconSource *)
 * @param pixbuf cast=(GdkPixbuf *)
 */
public static final native void _gtk_icon_source_set_pixbuf(int /*long*/ source, int /*long*/ pixbuf);
public static final void gtk_icon_source_set_pixbuf(int /*long*/ source, int /*long*/ pixbuf) {
	lock.lock();
	try {
		_gtk_icon_source_set_pixbuf(source, pixbuf);
	} finally {
		lock.unlock();
	}
}
/**
 * @param icon_set cast=(GtkIconSet *)
 * @param style cast=(GtkStyle *)
 * @param direction cast=(GtkTextDirection)
 * @param state cast=(GtkStateType)
 * @param size cast=(GtkIconSize)
 * @param widget cast=(GtkWidget *)
 * @param detail cast=(const char *)
 */
public static final native int /*long*/ _gtk_icon_set_render_icon(int /*long*/ icon_set, int /*long*/ style, int direction, int state, int size, int /*long*/ widget, int /*long*/ detail);
public static final int /*long*/ gtk_icon_set_render_icon(int /*long*/ icon_set, int /*long*/ style, int direction, int state, int size, int /*long*/ widget, int /*long*/ detail) {
	lock.lock();
	try {
		return _gtk_icon_set_render_icon(icon_set, style, direction, state, size, widget, detail);
	} finally {
		lock.unlock();
	}
}
/**
 * @param context cast=(GtkIMContext *)
 * @param event cast=(GdkEventKey *)
 */
public static final native boolean _gtk_im_context_filter_keypress(int /*long*/ context, int /*long*/ event);
public static final boolean gtk_im_context_filter_keypress(int /*long*/ context, int /*long*/ event) {
	lock.lock();
	try {
		return _gtk_im_context_filter_keypress(context, event);
	} finally {
		lock.unlock();
	}
}
/** @param context cast=(GtkIMContext *) */
public static final native void _gtk_im_context_focus_in(int /*long*/ context);
public static final void gtk_im_context_focus_in(int /*long*/ context) {
	lock.lock();
	try {
		_gtk_im_context_focus_in(context);
	} finally {
		lock.unlock();
	}
}
/** @param context cast=(GtkIMContext *) */
public static final native void _gtk_im_context_focus_out(int /*long*/ context);
public static final void gtk_im_context_focus_out(int /*long*/ context) {
	lock.lock();
	try {
		_gtk_im_context_focus_out(context);
	} finally {
		lock.unlock();
	}
}
/**
 * @param context cast=(GtkIMContext *)
 * @param str cast=(gchar **)
 * @param attrs cast=(PangoAttrList **)
 * @param cursor_pos cast=(gint *)
 */
public static final native void _gtk_im_context_get_preedit_string(int /*long*/ context, int /*long*/[] str, int /*long*/[] attrs, int[] cursor_pos);
public static final void gtk_im_context_get_preedit_string(int /*long*/ context, int /*long*/[] str, int /*long*/[] attrs, int[] cursor_pos) {
	lock.lock();
	try {
		_gtk_im_context_get_preedit_string(context, str, attrs, cursor_pos);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gtk_im_context_get_type();
public static final int /*long*/ gtk_im_context_get_type() {
	lock.lock();
	try {
		return _gtk_im_context_get_type();
	} finally {
		lock.unlock();
	}
}
/** @param context cast=(GtkIMContext *) */
public static final native void _gtk_im_context_reset(int /*long*/ context);
public static final void gtk_im_context_reset(int /*long*/ context) {
	lock.lock();
	try {
		_gtk_im_context_reset(context);
	} finally {
		lock.unlock();
	}
}
/**
 * @param context cast=(GtkIMContext *)
 * @param window cast=(GdkWindow *)
 */
public static final native void _gtk_im_context_set_client_window(int /*long*/ context, int /*long*/ window);
public static final void gtk_im_context_set_client_window(int /*long*/ context, int /*long*/ window) {
	lock.lock();
	try {
		_gtk_im_context_set_client_window(context, window);
	} finally {
		lock.unlock();
	}
}
/**
 * @param context cast=(GtkIMContext *)
 * @param area cast=(GdkRectangle *),flags=no_out
 */
public static final native void _gtk_im_context_set_cursor_location(int /*long*/ context, GdkRectangle area);
public static final void gtk_im_context_set_cursor_location(int /*long*/ context, GdkRectangle area) {
	lock.lock();
	try {
		_gtk_im_context_set_cursor_location(context, area);
	} finally {
		lock.unlock();
	}
}
/**
 * @param context cast=(GtkIMMulticontext *)
 * @param menushell cast=(GtkMenuShell *)
 */
public static final native void _gtk_im_multicontext_append_menuitems (int /*long*/ context, int /*long*/ menushell);
public static final void gtk_im_multicontext_append_menuitems (int /*long*/ context, int /*long*/ menushell) {
	lock.lock();
	try {
		_gtk_im_multicontext_append_menuitems(context, menushell);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gtk_im_multicontext_new();
public static final int /*long*/ gtk_im_multicontext_new() {
	lock.lock();
	try {
		return _gtk_im_multicontext_new();
	} finally {
		lock.unlock();
	}
}
/** @param label cast=(const gchar *) */
public static final native int /*long*/ _gtk_image_menu_item_new_with_label(byte[] label);
public static final int /*long*/ gtk_image_menu_item_new_with_label(byte[] label) {
	lock.lock();
	try {
		return _gtk_image_menu_item_new_with_label(label);
	} finally {
		lock.unlock();
	}
}
/**
 * @param menu_item cast=(GtkImageMenuItem *)
 * @param image cast=(GtkWidget *)
 */
public static final native void _gtk_image_menu_item_set_image(int /*long*/ menu_item, int /*long*/ image);
public static final void gtk_image_menu_item_set_image(int /*long*/ menu_item, int /*long*/ image) {
	lock.lock();
	try {
		_gtk_image_menu_item_set_image(menu_item, image);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gtk_image_new();
public static final int /*long*/ gtk_image_new() {
	lock.lock();
	try {
		return _gtk_image_new();
	} finally {
		lock.unlock();
	}
}
/** @param pixbuf cast=(GdkPixbuf *) */
public static final native int /*long*/ _gtk_image_new_from_pixbuf(int /*long*/ pixbuf); 
public static final int /*long*/ gtk_image_new_from_pixbuf(int /*long*/ pixbuf) {
	lock.lock();
	try {
		return _gtk_image_new_from_pixbuf(pixbuf);
	} finally {
		lock.unlock();
	}
}
/**
 * @param pixmap cast=(GdkPixmap *)
 * @param mask cast=(GdkBitmap *)
 */
public static final native int /*long*/ _gtk_image_new_from_pixmap(int /*long*/ pixmap, int /*long*/ mask);
public static final int /*long*/ gtk_image_new_from_pixmap(int /*long*/ pixmap, int /*long*/ mask) {
	lock.lock();
	try {
		return _gtk_image_new_from_pixmap(pixmap, mask);
	} finally {
		lock.unlock();
	}
}
/**
 * @param image cast=(GtkImage *)
 * @param pixbuf cast=(GdkPixbuf *)
 */
public static final native void _gtk_image_set_from_pixbuf(int /*long*/ image, int /*long*/ pixbuf);
public static final void gtk_image_set_from_pixbuf(int /*long*/ image, int /*long*/ pixbuf) {
	lock.lock();
	try {
		_gtk_image_set_from_pixbuf(image, pixbuf);
	} finally {
		lock.unlock();
	}
}
/**
 * @param image cast=(GtkImage *)
 * @param pixmap cast=(GdkBitmap *)
 * @param mask cast=(GdkBitmap *)
 */
public static final native void _gtk_image_set_from_pixmap(int /*long*/ image, int /*long*/ pixmap, int /*long*/ mask);
public static final void gtk_image_set_from_pixmap(int /*long*/ image, int /*long*/ pixmap, int /*long*/ mask) {
	lock.lock();
	try {
		_gtk_image_set_from_pixmap(image, pixmap, mask);
	} finally {
		lock.unlock();
	}
}
/**
 * @param argc cast=(int *)
 * @param argv cast=(char ***)
 */
public static final native boolean _gtk_init_check(int /*long*/[] argc, int /*long*/[] argv);
public static final boolean gtk_init_check(int /*long*/[] argc, int /*long*/[] argv) {
	lock.lock();
	try {
		return _gtk_init_check(argc, argv);
	} finally {
		lock.unlock();
	}
}
/** @param label cast=(GtkLabel *) */
public static final native int /*long*/ _gtk_label_get_layout(int /*long*/ label);
public static final int /*long*/ gtk_label_get_layout(int /*long*/ label) {
	lock.lock();
	try {
		return _gtk_label_get_layout(label);
	} finally {
		lock.unlock();
	}
}
/** @param label cast=(GtkLabel *) */
public static final native int _gtk_label_get_mnemonic_keyval(int /*long*/ label);
public static final int gtk_label_get_mnemonic_keyval(int /*long*/ label) {
	lock.lock();
	try {
		return _gtk_label_get_mnemonic_keyval(label);
	} finally {
		lock.unlock();
	}
}
/** @param label cast=(const gchar *) */
public static final native int /*long*/ _gtk_label_new(byte[] label);
public static final int /*long*/ gtk_label_new(byte[] label) {
	lock.lock();
	try {
		return _gtk_label_new(label);
	} finally {
		lock.unlock();
	}
}
/** @param str cast=(const gchar *) */
public static final native int /*long*/ _gtk_label_new_with_mnemonic(byte[] str);
public static final int /*long*/ gtk_label_new_with_mnemonic(byte[] str) {
	lock.lock();
	try {
		return _gtk_label_new_with_mnemonic(str);
	} finally {
		lock.unlock();
	}
}
/**
 * @param label cast=(GtkLabel *)
 * @param attrs cast=(PangoAttrList *)
 */
public static final native void _gtk_label_set_attributes(int /*long*/ label, int /*long*/ attrs);
public static final void gtk_label_set_attributes(int /*long*/ label, int /*long*/ attrs) {
	lock.lock();
	try {
		_gtk_label_set_attributes(label, attrs);
	} finally {
		lock.unlock();
	}
}
/**
 * @param label cast=(GtkLabel *)
 * @param jtype cast=(GtkJustification)
 */
public static final native void _gtk_label_set_justify(int /*long*/ label, int jtype);
public static final void gtk_label_set_justify(int /*long*/ label, int jtype) {
	lock.lock();
	try {
		_gtk_label_set_justify(label, jtype);
	} finally {
		lock.unlock();
	}
}
/**
 * @param label cast=(GtkLabel *)
 * @param wrap cast=(gboolean)
 */
public static final native void _gtk_label_set_line_wrap(int /*long*/ label, boolean wrap);
public static final void gtk_label_set_line_wrap(int /*long*/ label, boolean wrap) {
	lock.lock();
	try {
		_gtk_label_set_line_wrap(label, wrap);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _gtk_label_set_line_wrap_mode(int /*long*/ label, int wrap_mode);
public static final void gtk_label_set_line_wrap_mode(int /*long*/ label, int wrap_mode) {
	lock.lock();
	try {
		_gtk_label_set_line_wrap_mode(label, wrap_mode);
	} finally {
		lock.unlock();
	}
}
/**
 * @param label cast=(GtkLabel *)
 * @param str cast=(const gchar *)
 */
public static final native void _gtk_label_set_text(int /*long*/ label, int /*long*/ str);
public static final void gtk_label_set_text(int /*long*/ label, int /*long*/ str) {
	lock.lock();
	try {
		_gtk_label_set_text(label, str);
	} finally {
		lock.unlock();
	}
}
/**
 * @param label cast=(GtkLabel *)
 * @param str cast=(const gchar *)
 */
public static final native void _gtk_label_set_text(int /*long*/ label, byte[] str);
public static final void gtk_label_set_text(int /*long*/ label, byte[] str) {
	lock.lock();
	try {
		_gtk_label_set_text(label, str);
	} finally {
		lock.unlock();
	}
}
/**
 * @param label cast=(GtkLabel *)
 * @param str cast=(const gchar *)
 */
public static final native void _gtk_label_set_text_with_mnemonic(int /*long*/ label, byte[] str);
public static final void gtk_label_set_text_with_mnemonic(int /*long*/ label, byte[] str) {
	lock.lock();
	try {
		_gtk_label_set_text_with_mnemonic(label, str);
	} finally {
		lock.unlock();
	}
}
/**
 * @param list cast=(GtkList *)
 * @param items cast=(GList *)
 */
public static final native void _gtk_list_append_items(int /*long*/ list, int /*long*/ items);
public static final void gtk_list_append_items(int /*long*/ list, int /*long*/ items) {
	lock.lock();
	try {
		_gtk_list_append_items(list, items);
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(GtkList *) */
public static final native void _gtk_list_clear_items(int /*long*/ list, int start, int end);
public static final void gtk_list_clear_items(int /*long*/ list, int start, int end) {
	lock.lock();
	try {
		_gtk_list_clear_items(list, start, end);
	} finally {
		lock.unlock();
	}
}
/**
 * @param list cast=(GtkList *)
 * @param items cast=(GList *)
 */
public static final native void _gtk_list_insert_items(int /*long*/ list, int /*long*/ items, int position);
public static final void gtk_list_insert_items(int /*long*/ list, int /*long*/ items, int position) {
	lock.lock();
	try {
		_gtk_list_insert_items(list, items, position);
	} finally {
		lock.unlock();
	}
}
/** @param label cast=(const gchar *) */
public static final native int /*long*/ _gtk_list_item_new_with_label(byte[] label);
public static final int /*long*/ gtk_list_item_new_with_label(byte[] label) {
	lock.lock();
	try {
		return _gtk_list_item_new_with_label(label);
	} finally {
		lock.unlock();
	}
}
/**
 * @param list cast=(GtkList *)
 * @param items cast=(GList *)
 */
public static final native void _gtk_list_remove_items(int /*long*/ list, int /*long*/ items);
public static final void gtk_list_remove_items(int /*long*/ list, int /*long*/ items) {
	lock.lock();
	try {
		_gtk_list_remove_items(list, items);
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(GtkList *) */
public static final native void _gtk_list_select_item(int /*long*/ list, int item);
public static final void gtk_list_select_item(int /*long*/ list, int item) {
	lock.lock();
	try {
		_gtk_list_select_item(list, item);
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(GtkList *) */
public static final native void _gtk_list_unselect_all(int /*long*/ list);
public static final void gtk_list_unselect_all(int /*long*/ list) {
	lock.lock();
	try {
		_gtk_list_unselect_all(list);
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(GtkList *) */
public static final native void _gtk_list_unselect_item(int /*long*/ list, int item);
public static final void gtk_list_unselect_item(int /*long*/ list, int item) {
	lock.lock();
	try {
		_gtk_list_unselect_item(list, item);
	} finally {
		lock.unlock();
	}
}
/**
 * @param list_store cast=(GtkListStore *)
 * @param iter cast=(GtkTreeIter *)
 */
public static final native void _gtk_list_store_append(int /*long*/ list_store, int /*long*/ iter);
public static final void gtk_list_store_append(int /*long*/ list_store, int /*long*/ iter) {
	lock.lock();
	try {
		_gtk_list_store_append(list_store, iter);
	} finally {
		lock.unlock();
	}
}
/** @param store cast=(GtkListStore *) */
public static final native void _gtk_list_store_clear(int /*long*/ store);
public static final void gtk_list_store_clear(int /*long*/ store) {
	lock.lock();
	try {
		_gtk_list_store_clear(store);
	} finally {
		lock.unlock();
	}
}
/**
 * @param list_store cast=(GtkListStore *)
 * @param iter cast=(GtkTreeIter *)
 * @param position cast=(gint)
 */
public static final native void _gtk_list_store_insert(int /*long*/ list_store, int /*long*/ iter, int position);
public static final void gtk_list_store_insert(int /*long*/ list_store, int /*long*/ iter, int position) {
	lock.lock();
	try {
		_gtk_list_store_insert(list_store, iter, position);
	} finally {
		lock.unlock();
	}
}
/**
 * @param numColumns cast=(gint)
 * @param types cast=(GType *)
 */
public static final native int /*long*/ _gtk_list_store_newv(int numColumns, int /*long*/[] types);
public static final int /*long*/ gtk_list_store_newv(int numColumns, int /*long*/[] types) {
	lock.lock();
	try {
		return _gtk_list_store_newv(numColumns, types);
	} finally {
		lock.unlock();
	}
}
/**
 * @param list_store cast=(GtkListStore *)
 * @param iter cast=(GtkTreeIter *)
 */
public static final native void _gtk_list_store_remove(int /*long*/ list_store, int /*long*/ iter);
public static final void gtk_list_store_remove(int /*long*/ list_store, int /*long*/ iter) {
	lock.lock();
	try {
		_gtk_list_store_remove(list_store, iter);
	} finally {
		lock.unlock();
	}
}
/**
 * @param store cast=(GtkListStore *)
 * @param iter cast=(GtkTreeIter *)
 */
public static final native void _gtk_list_store_set(int /*long*/ store, int /*long*/ iter, int column, byte[] value, int /*long*/ terminator);
public static final void gtk_list_store_set(int /*long*/ store, int /*long*/ iter, int column, byte[] value, int /*long*/ terminator) {
	lock.lock();
	try {
		_gtk_list_store_set(store, iter, column, value, terminator);
	} finally {
		lock.unlock();
	}
}
/**
 * @param store cast=(GtkListStore *)
 * @param iter cast=(GtkTreeIter *)
 */
public static final native void _gtk_list_store_set(int /*long*/ store, int /*long*/ iter, int column, int value, int /*long*/ terminator);
public static final void gtk_list_store_set(int /*long*/ store, int /*long*/ iter, int column, int value, int /*long*/ terminator) {
	lock.lock();
	try {
		_gtk_list_store_set(store, iter, column, value, terminator);
	} finally {
		lock.unlock();
	}
}
/**
 * @param store cast=(GtkListStore *)
 * @param iter cast=(GtkTreeIter *)
 */
public static final native void _gtk_list_store_set(int /*long*/ store, int /*long*/ iter, int column, long value, int /*long*/ terminator);
public static final void gtk_list_store_set(int /*long*/ store, int /*long*/ iter, int column, long value, int /*long*/ terminator) {
	lock.lock();
	try {
		_gtk_list_store_set(store, iter, column, value, terminator);
	} finally {
		lock.unlock();
	}
}
/**
 * @param store cast=(GtkListStore *)
 * @param iter cast=(GtkTreeIter *)
 * @param value flags=no_out
 */
public static final native void _gtk_list_store_set(int /*long*/ store, int /*long*/ iter, int column, GdkColor value, int /*long*/ terminator);
public static final void gtk_list_store_set(int /*long*/ store, int /*long*/ iter, int column, GdkColor value, int /*long*/ terminator) {
	lock.lock();
	try {
		_gtk_list_store_set(store, iter, column, value, terminator);
	} finally {
		lock.unlock();
	}
}
/**
 * @param store cast=(GtkListStore *)
 * @param iter cast=(GtkTreeIter *)
 */
public static final native void _gtk_list_store_set(int /*long*/ store, int /*long*/ iter, int column, boolean value, int /*long*/ terminator);
public static final void gtk_list_store_set(int /*long*/ store, int /*long*/ iter, int column, boolean value, int /*long*/ terminator) {
	lock.lock();
	try {
		_gtk_list_store_set(store, iter, column, value, terminator);
	} finally {
		lock.unlock();
	}
}
/** @method flags=const */
public static final native int _gtk_major_version();
public static final int gtk_major_version() {
	lock.lock();
	try {
		return _gtk_major_version();
	} finally {
		lock.unlock();
	}
}
/** @method flags=const */
public static final native int _gtk_minor_version();
public static final int gtk_minor_version() {
	lock.lock();
	try {
		return _gtk_minor_version();
	} finally {
		lock.unlock();
	}
}
/** @method flags=const */
public static final native int _gtk_micro_version();
public static final int gtk_micro_version() {
	lock.lock();
	try {
		return _gtk_micro_version();
	} finally {
		lock.unlock();
	}
}
public static final native void _gtk_main();
public static final void gtk_main() {
	lock.lock();
	try {
		_gtk_main();
	} finally {
		lock.unlock();
	}
}
public static final native int _gtk_main_iteration();
public static final int gtk_main_iteration() {
	lock.lock();
	try {
		return _gtk_main_iteration();
	} finally {
		lock.unlock();
	}
}
/** @param event cast=(GdkEvent *) */
public static final native void _gtk_main_do_event(int /*long*/ event);
public static final void gtk_main_do_event(int /*long*/ event) {
	lock.lock();
	try {
		_gtk_main_do_event(event);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gtk_menu_bar_new();
public static final int /*long*/ gtk_menu_bar_new() {
	lock.lock();
	try {
		return _gtk_menu_bar_new();
	} finally {
		lock.unlock();
	}
}
/** @param menu cast=(GtkMenu *) */
public static final native int /*long*/ _gtk_menu_get_attach_widget(int /*long*/ menu);
public static final int /*long*/ gtk_menu_get_attach_widget(int /*long*/ menu) {
	lock.lock();
	try {
		return _gtk_menu_get_attach_widget(menu);
	} finally {
		lock.unlock();
	}
}
/** @param menu_item cast=(GtkMenuItem *) */
public static final native void _gtk_menu_item_remove_submenu(int /*long*/ menu_item);
public static final void gtk_menu_item_remove_submenu(int /*long*/ menu_item) {
	lock.lock();
	try {
		_gtk_menu_item_remove_submenu(menu_item);
	} finally {
		lock.unlock();
	}
}
/** @param menu_item cast=(GtkMenuItem *) */
public static final native int /*long*/ _gtk_menu_item_get_submenu(int /*long*/ menu_item);
public static final int /*long*/ gtk_menu_item_get_submenu(int /*long*/ menu_item) {
	lock.lock();
	try {
		return _gtk_menu_item_get_submenu(menu_item);
	} finally {
		lock.unlock();
	}
}
/**
 * @param menu_item cast=(GtkMenuItem *)
 * @param submenu cast=(GtkWidget *)
 */
public static final native void _gtk_menu_item_set_submenu(int /*long*/ menu_item, int /*long*/ submenu);
public static final void gtk_menu_item_set_submenu(int /*long*/ menu_item, int /*long*/ submenu) {
	lock.lock();
	try {
		_gtk_menu_item_set_submenu(menu_item, submenu);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gtk_menu_new();
public static final int /*long*/ gtk_menu_new() {
	lock.lock();
	try {
		return _gtk_menu_new();
	} finally {
		lock.unlock();
	}
}
/** @param menu cast=(GtkMenu *) */
public static final native void _gtk_menu_popdown(int /*long*/ menu);
public static final void gtk_menu_popdown(int /*long*/ menu) {
	lock.lock();
	try {
		_gtk_menu_popdown(menu);
	} finally {
		lock.unlock();
	}
}
/**
 * @param menu cast=(GtkMenu *)
 * @param parent_menu_shell cast=(GtkWidget *)
 * @param parent_menu_item cast=(GtkWidget *)
 * @param func cast=(GtkMenuPositionFunc)
 * @param data cast=(gpointer)
 * @param button cast=(guint)
 * @param activate_time cast=(guint32)
 */
public static final native void _gtk_menu_popup(int /*long*/ menu, int /*long*/ parent_menu_shell, int /*long*/ parent_menu_item, int /*long*/ func, int /*long*/ data, int button, int activate_time);
public static final void gtk_menu_popup(int /*long*/ menu, int /*long*/ parent_menu_shell, int /*long*/ parent_menu_item, int /*long*/ func, int /*long*/ data, int button, int activate_time) {
	lock.lock();
	try {
		_gtk_menu_popup(menu, parent_menu_shell, parent_menu_item, func, data, button, activate_time);
	} finally {
		lock.unlock();
	}
}
/** @param menu_shell cast=(GtkMenuShell *) */
public static final native void _gtk_menu_shell_deactivate(int /*long*/ menu_shell);
public static final void gtk_menu_shell_deactivate(int /*long*/ menu_shell) {
	lock.lock();
	try {
		_gtk_menu_shell_deactivate(menu_shell);
	} finally {
		lock.unlock();
	}
}
/**
 * @param menu_shell cast=(GtkMenuShell *)
 * @param child cast=(GtkWidget *)
 * @param position cast=(gint)
 */
public static final native void _gtk_menu_shell_insert(int /*long*/ menu_shell, int /*long*/ child, int position);
public static final void gtk_menu_shell_insert(int /*long*/ menu_shell, int /*long*/ child, int position) {
	lock.lock();
	try {
		_gtk_menu_shell_insert(menu_shell, child, position);
	} finally {
		lock.unlock();
	}
}
/**
 * @param menu_shell cast=(GtkMenuShell *)
 * @param menu_item cast=(GtkWidget *)
 */
public static final native void _gtk_menu_shell_select_item(int /*long*/ menu_shell, int /*long*/ menu_item);
public static final void gtk_menu_shell_select_item(int /*long*/ menu_shell, int /*long*/ menu_item) {
	lock.lock();
	try {
		_gtk_menu_shell_select_item(menu_shell, menu_item);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param menu_shell cast=(GtkMenuShell *)
 * @param take_focus cast=(gboolean)
 */
public static final native void _gtk_menu_shell_set_take_focus(int /*long*/ menu_shell, boolean take_focus);
public static final void gtk_menu_shell_set_take_focus(int /*long*/ menu_shell, boolean take_focus) {
	lock.lock();
	try {
		_gtk_menu_shell_set_take_focus(menu_shell, take_focus);
	} finally {
		lock.unlock();
	}
}
/**
 * @param parent cast=(GtkWindow *)
 * @param flags cast=(GtkDialogFlags)
 * @param type cast=(GtkMessageType)
 * @param buttons cast=(GtkButtonsType)
 * @param message_format cast=(const gchar *)
 */
public static final native int /*long*/ _gtk_message_dialog_new(int /*long*/ parent, int flags, int type, int buttons, byte[] message_format);
public static final int /*long*/ gtk_message_dialog_new(int /*long*/ parent, int flags, int type, int buttons, byte[] message_format) {
	lock.lock();
	try {
		return _gtk_message_dialog_new(parent, flags, type, buttons, message_format);
	} finally {
		lock.unlock();
	}
}
/**
 * @param misc cast=(GtkMisc *)
 * @param xalign cast=(gfloat)
 * @param yalign cast=(gfloat)
 */
public static final native void _gtk_misc_set_alignment(int /*long*/ misc, float xalign, float yalign);
public static final void gtk_misc_set_alignment(int /*long*/ misc, float xalign, float yalign) {
	lock.lock();
	try {
		_gtk_misc_set_alignment(misc, xalign, yalign);
	} finally {
		lock.unlock();
	}
}
/** @param notebook cast=(GtkNotebook *) */
public static final native int _gtk_notebook_get_current_page(int /*long*/ notebook);
public static final int gtk_notebook_get_current_page(int /*long*/ notebook) {
	lock.lock();
	try {
		return _gtk_notebook_get_current_page(notebook);
	} finally {
		lock.unlock();
	}
}
/** @param notebook cast=(GtkNotebook *) */
public static final native boolean _gtk_notebook_get_scrollable(int /*long*/ notebook);
public static final boolean gtk_notebook_get_scrollable(int /*long*/ notebook) {
	lock.lock();
	try {
		return _gtk_notebook_get_scrollable(notebook);
	} finally {
		lock.unlock();
	}
}
/**
 * @param notebook cast=(GtkNotebook *)
 * @param child cast=(GtkWidget *)
 * @param tab_label cast=(GtkWidget *)
 * @param position cast=(gint)
 */
public static final native void _gtk_notebook_insert_page(int /*long*/ notebook, int /*long*/ child, int /*long*/ tab_label, int position);
public static final void gtk_notebook_insert_page(int /*long*/ notebook, int /*long*/ child, int /*long*/ tab_label, int position) {
	lock.lock();
	try {
		_gtk_notebook_insert_page(notebook, child, tab_label, position);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gtk_notebook_new();
public static final int /*long*/ gtk_notebook_new() {
	lock.lock();
	try {
		return _gtk_notebook_new();
	} finally {
		lock.unlock();
	}
}
/** @param notebook cast=(GtkNotebook *) */
public static final native void _gtk_notebook_next_page(int /*long*/ notebook);
public static final void gtk_notebook_next_page(int /*long*/ notebook) {
	lock.lock();
	try {
		_gtk_notebook_next_page(notebook);
	} finally {
		lock.unlock();
	}
}
/** @param notebook cast=(GtkNotebook *) */
public static final native void _gtk_notebook_prev_page(int /*long*/ notebook);
public static final void gtk_notebook_prev_page(int /*long*/ notebook) {
	lock.lock();
	try {
		_gtk_notebook_prev_page(notebook);
	} finally {
		lock.unlock();
	}
}
/**
 * @param notebook cast=(GtkNotebook *)
 * @param page_num cast=(gint)
 */
public static final native void _gtk_notebook_remove_page(int /*long*/ notebook, int page_num);
public static final void gtk_notebook_remove_page(int /*long*/ notebook, int page_num) {
	lock.lock();
	try {
		_gtk_notebook_remove_page(notebook, page_num);
	} finally {
		lock.unlock();
	}
}
/**
 * @param notebook cast=(GtkNotebook *)
 * @param page_num cast=(gint)
 */
public static final native void _gtk_notebook_set_current_page(int /*long*/ notebook, int page_num);
public static final void gtk_notebook_set_current_page(int /*long*/ notebook, int page_num) {
	lock.lock();
	try {
		_gtk_notebook_set_current_page(notebook, page_num);
	} finally {
		lock.unlock();
	}
}
/**
 * @param notebook cast=(GtkNotebook *)
 * @param scrollable cast=(gboolean)
 */
public static final native void _gtk_notebook_set_scrollable(int /*long*/ notebook, boolean scrollable);
public static final void gtk_notebook_set_scrollable(int /*long*/ notebook, boolean scrollable) {
	lock.lock();
	try {
		_gtk_notebook_set_scrollable(notebook, scrollable);
	} finally {
		lock.unlock();
	}
}
/**
 * @param notebook cast=(GtkNotebook *)
 * @param show_tabs cast=(gboolean)
 */
public static final native void _gtk_notebook_set_show_tabs(int /*long*/ notebook, boolean show_tabs);
public static final void gtk_notebook_set_show_tabs(int /*long*/ notebook, boolean show_tabs) {
	lock.lock();
	try {
		_gtk_notebook_set_show_tabs(notebook, show_tabs);
	} finally {
		lock.unlock();
	}
}
/**
 * @param notebook cast=(GtkNotebook *)
 * @param pos cast=(GtkPositionType)
 */
public static final native void _gtk_notebook_set_tab_pos(int /*long*/ notebook, int pos);
public static final void gtk_notebook_set_tab_pos(int /*long*/ notebook, int pos) {
	lock.lock();
	try {
		_gtk_notebook_set_tab_pos(notebook, pos);
	} finally {
		lock.unlock();
	}
}
/** @param object cast=(GtkObject *) */
public static final native void _gtk_object_sink(int /*long*/ object);
public static final void gtk_object_sink(int /*long*/ object) {
	lock.lock();
	try {
		_gtk_object_sink(object);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _gtk_page_setup_new ();
public static final int /*long*/ gtk_page_setup_new () {
	lock.lock();
	try {
		return _gtk_page_setup_new ();
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int _gtk_page_setup_get_orientation(int /*long*/ setup);
public static final int gtk_page_setup_get_orientation(int /*long*/ setup) {
	lock.lock();
	try {
		return _gtk_page_setup_get_orientation(setup);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _gtk_page_setup_set_orientation(int /*long*/ setup, int orientation);
public static final void gtk_page_setup_set_orientation(int /*long*/ setup, int orientation) {
	lock.lock();
	try {
		_gtk_page_setup_set_orientation(setup, orientation);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _gtk_page_setup_get_paper_size(int /*long*/ setup);
public static final int /*long*/ gtk_page_setup_get_paper_size(int /*long*/ setup) {
	lock.lock();
	try {
		return _gtk_page_setup_get_paper_size(setup);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _gtk_page_setup_set_paper_size(int /*long*/ setup, int /*long*/ size);
public static final void gtk_page_setup_set_paper_size(int /*long*/ setup, int /*long*/ size) {
	lock.lock();
	try {
		_gtk_page_setup_set_paper_size(setup, size);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native double _gtk_page_setup_get_top_margin(int /*long*/ setup, int unit);
public static final double gtk_page_setup_get_top_margin(int /*long*/ setup, int unit) {
	lock.lock();
	try {
		return _gtk_page_setup_get_top_margin(setup, unit);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _gtk_page_setup_set_top_margin(int /*long*/ setup, double margin, int unit);
public static final void gtk_page_setup_set_top_margin(int /*long*/ setup, double margin, int unit) {
	lock.lock();
	try {
		_gtk_page_setup_set_top_margin(setup, margin, unit);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native double _gtk_page_setup_get_bottom_margin(int /*long*/ setup, int unit);
public static final double gtk_page_setup_get_bottom_margin(int /*long*/ setup, int unit) {
	lock.lock();
	try {
		return _gtk_page_setup_get_bottom_margin(setup, unit);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _gtk_page_setup_set_bottom_margin(int /*long*/ setup, double margin, int unit);
public static final void gtk_page_setup_set_bottom_margin(int /*long*/ setup, double margin, int unit) {
	lock.lock();
	try {
		_gtk_page_setup_set_bottom_margin(setup, margin, unit);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native double _gtk_page_setup_get_left_margin(int /*long*/ setup, int unit);
public static final double gtk_page_setup_get_left_margin(int /*long*/ setup, int unit) {
	lock.lock();
	try {
		return _gtk_page_setup_get_left_margin(setup, unit);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _gtk_page_setup_set_left_margin(int /*long*/ setup, double margin, int unit);
public static final void gtk_page_setup_set_left_margin(int /*long*/ setup, double margin, int unit) {
	lock.lock();
	try {
		_gtk_page_setup_set_left_margin(setup, margin, unit);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native double _gtk_page_setup_get_right_margin(int /*long*/ setup, int unit);
public static final double gtk_page_setup_get_right_margin(int /*long*/ setup, int unit) {
	lock.lock();
	try {
		return _gtk_page_setup_get_right_margin(setup, unit);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _gtk_page_setup_set_right_margin(int /*long*/ setup, double margin, int unit);
public static final void gtk_page_setup_set_right_margin(int /*long*/ setup, double margin, int unit) {
	lock.lock();
	try {
		_gtk_page_setup_set_right_margin(setup, margin, unit);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native double _gtk_page_setup_get_paper_width(int /*long*/ setup, int unit);
public static final double gtk_page_setup_get_paper_width(int /*long*/ setup, int unit) {
	lock.lock();
	try {
		return _gtk_page_setup_get_paper_width(setup, unit);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native double _gtk_page_setup_get_paper_height(int /*long*/ setup, int unit);
public static final double gtk_page_setup_get_paper_height(int /*long*/ setup, int unit) {
	lock.lock();
	try {
		return _gtk_page_setup_get_paper_height(setup, unit);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native double _gtk_page_setup_get_page_width(int /*long*/ setup, int unit);
public static final double gtk_page_setup_get_page_width(int /*long*/ setup, int unit) {
	lock.lock();
	try {
		return _gtk_page_setup_get_page_width(setup, unit);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native double _gtk_page_setup_get_page_height(int /*long*/ setup, int unit);
public static final double gtk_page_setup_get_page_height(int /*long*/ setup, int unit) {
	lock.lock();
	try {
		return _gtk_page_setup_get_page_height(setup, unit);
	} finally {
		lock.unlock();
	}
}
/**
 * @param style cast=(GtkStyle *)
 * @param window cast=(GdkWindow *)
 * @param area flags=no_out
 * @param widget cast=(GtkWidget *)
 * @param detail cast=(const gchar *)
 */
public static final native void _gtk_paint_handle(int /*long*/ style, int /*long*/ window, int state_type, int shadow_type, GdkRectangle area, int /*long*/ widget, byte[] detail, int x , int y, int width, int height, int orientation);
public static final void gtk_paint_handle(int /*long*/ style, int /*long*/ window, int state_type, int shadow_type, GdkRectangle area, int /*long*/ widget, byte[] detail, int x , int y, int width, int height, int orientation) {
	lock.lock();
	try {
		_gtk_paint_handle(style, window, state_type, shadow_type, area, widget, detail, x, y, width, height, orientation);
	} finally {
		lock.unlock();
	}
}
/**
 * @param style cast=(GtkStyle *)
 * @param window cast=(GdkWindow *)
 * @param widget cast=(GtkWidget *)
 * @param detail cast=(const gchar *)
 */
public static final native void _gtk_paint_flat_box(int /*long*/ style, int /*long*/ window, int state_type, int shadow_type, GdkRectangle area, int /*long*/ widget, byte[] detail, int x , int y, int width, int height);
public static final void gtk_paint_flat_box(int /*long*/ style, int /*long*/ window, int state_type, int shadow_type, GdkRectangle area, int /*long*/ widget, byte[] detail, int x , int y, int width, int height) {
	lock.lock();
	try {
		_gtk_paint_flat_box(style, window, state_type, shadow_type, area, widget, detail, x, y, width, height);
	} finally {
		lock.unlock();
	}
}
/**
 * @param style cast=(GtkStyle *)
 * @param window cast=(GdkWindow *)
 * @param area flags=no_out
 * @param widget cast=(GtkWidget *)
 * @param detail cast=(const gchar *)
 */
public static final native void _gtk_paint_focus(int /*long*/ style, int /*long*/ window, int state_type, GdkRectangle area, int /*long*/ widget, byte[] detail, int x , int y, int width, int height);
public static final void gtk_paint_focus(int /*long*/ style, int /*long*/ window, int state_type, GdkRectangle area, int /*long*/ widget, byte[] detail, int x , int y, int width, int height) {
	lock.lock();
	try {
		_gtk_paint_focus(style, window, state_type, area, widget, detail, x, y, width, height);
	} finally {
		lock.unlock();
	}
}
/**
 * @param style cast=(GtkStyle *)
 * @param window cast=(GdkWindow *)
 * @param widget cast=(GtkWidget *)
 * @param detail cast=(const gchar *)
 */
public static final native void _gtk_paint_option(int /*long*/ style, int /*long*/ window, int state_type, int shadow_type, GdkRectangle area, int /*long*/ widget, byte[] detail, int x , int y, int width, int height);
public static final void gtk_paint_option(int /*long*/ style, int /*long*/ window, int state_type, int shadow_type, GdkRectangle area, int /*long*/ widget, byte[] detail, int x , int y, int width, int height) {
	lock.lock();
	try {
		_gtk_paint_option(style, window, state_type, shadow_type, area, widget, detail, x, y, width, height);
	} finally {
		lock.unlock();
	}
}
/**
 * @param style cast=(GtkStyle *)
 * @param window cast=(GdkWindow *)
 * @param widget cast=(GtkWidget *)
 * @param detail cast=(const gchar *)
 */
public static final native void _gtk_paint_slider(int /*long*/ style, int /*long*/ window, int state_type, int shadow_type, GdkRectangle area, int /*long*/ widget, byte[] detail, int x , int y, int width, int height, int orientation);
public static final void gtk_paint_slider(int /*long*/ style, int /*long*/ window, int state_type, int shadow_type, GdkRectangle area, int /*long*/ widget, byte[] detail, int x , int y, int width, int height, int orientation) {
	lock.lock();
	try {
		_gtk_paint_slider(style, window, state_type, shadow_type, area, widget, detail, x, y, width, height, orientation);
	} finally {
		lock.unlock();
	}
}
/**
 * @param style cast=(GtkStyle *)
 * @param window cast=(GdkWindow *)
 * @param widget cast=(GtkWidget *)
 * @param detail cast=(const gchar *)
 */
public static final native void _gtk_paint_tab(int /*long*/ style, int /*long*/ window, int state_type, int shadow_type, GdkRectangle area, int /*long*/ widget, byte[] detail, int x , int y, int width, int height);
public static final void gtk_paint_tab(int /*long*/ style, int /*long*/ window, int state_type, int shadow_type, GdkRectangle area, int /*long*/ widget, byte[] detail, int x , int y, int width, int height) {
	lock.lock();
	try {
		_gtk_paint_tab(style, window, state_type, shadow_type, area, widget, detail, x, y, width, height);
	} finally {
		lock.unlock();
	}
}
/**
 * @param style cast=(GtkStyle *)
 * @param window cast=(GdkWindow *)
 * @param widget cast=(GtkWidget *)
 * @param detail cast=(const gchar *)
 */
public static final native void _gtk_paint_arrow(int /*long*/ style, int /*long*/ window, int state_type, int shadow_type, GdkRectangle area, int /*long*/ widget, byte[] detail, int arrow_type, boolean fill, int x, int y, int width, int height);
public static final void gtk_paint_arrow(int /*long*/ style, int /*long*/ window, int state_type, int shadow_type, GdkRectangle area, int /*long*/ widget, byte[] detail, int arrow_type, boolean fill, int x, int y, int width, int height) {
	lock.lock();
	try {
		_gtk_paint_arrow(style, window, state_type, shadow_type, area, widget, detail, arrow_type, fill, x, y, width, height);
	} finally {
		lock.unlock();
	}
}

/**
 * @param style cast=(GtkStyle *)
 * @param window cast=(GdkWindow *)
 * @param widget cast=(GtkWidget *)
 * @param detail cast=(const gchar *)
 */
public static final native void _gtk_paint_box(int /*long*/ style, int /*long*/ window, int state_type, int shadow_type, GdkRectangle area, int /*long*/ widget, byte[] detail, int x , int y, int width, int height);
public static final void gtk_paint_box(int /*long*/ style, int /*long*/ window, int state_type, int shadow_type, GdkRectangle area, int /*long*/ widget, byte[] detail, int x , int y, int width, int height) {
	lock.lock();
	try {
		_gtk_paint_box(style, window, state_type, shadow_type, area, widget, detail, x, y, width, height);
	} finally {
		lock.unlock();
	}
}
/**
 * @param style cast=(GtkStyle *)
 * @param window cast=(GdkWindow *)
 * @param widget cast=(GtkWidget *)
 * @param detail cast=(gchar *)
 */
public static final native void _gtk_paint_box_gap(int /*long*/ style, int /*long*/ window, int state_type, int shadow_type, GdkRectangle area, int /*long*/ widget, byte[] detail, int x , int y, int width, int height, int gap_side, int gap_x, int gap_width);
public static final void gtk_paint_box_gap(int /*long*/ style, int /*long*/ window, int state_type, int shadow_type, GdkRectangle area, int /*long*/ widget, byte[] detail, int x , int y, int width, int height, int gap_side, int gap_x, int gap_width) {
	lock.lock();
	try {
		_gtk_paint_box_gap(style, window, state_type, shadow_type, area, widget, detail, x, y, width, height, gap_side, gap_x, gap_width);
	} finally {
		lock.unlock();
	}
}
/**
 * @param style cast=(GtkStyle *)
 * @param window cast=(GdkWindow *)
 * @param widget cast=(GtkWidget *)
 * @param detail cast=(const gchar *)
 */
public static final native void _gtk_paint_check(int /*long*/ style, int /*long*/ window, int state_type, int shadow_type, GdkRectangle area, int /*long*/ widget, byte[] detail, int x , int y, int width, int height);
public static final void gtk_paint_check(int /*long*/ style, int /*long*/ window, int state_type, int shadow_type, GdkRectangle area, int /*long*/ widget, byte[] detail, int x , int y, int width, int height) {
	lock.lock();
	try {
		_gtk_paint_check(style, window, state_type, shadow_type, area, widget, detail, x, y, width, height);
	} finally {
		lock.unlock();
	}
}
/**
 * @param style cast=(GtkStyle *)
 * @param window cast=(GdkWindow *)
 * @param widget cast=(GtkWidget *)
 * @param detail cast=(const gchar *)
 */
public static final native void _gtk_paint_expander(int /*long*/ style, int /*long*/ window, int state_type, GdkRectangle area, int /*long*/ widget, byte[] detail, int x , int y, int expander_style);
public static final void gtk_paint_expander(int /*long*/ style, int /*long*/ window, int state_type, GdkRectangle area, int /*long*/ widget, byte[] detail, int x , int y, int expander_style) {
	lock.lock();
	try {
		_gtk_paint_expander(style, window, state_type, area, widget, detail, x, y, expander_style);
	} finally {
		lock.unlock();
	}
}
/**
 * @param style cast=(GtkStyle *)
 * @param window cast=(GdkWindow *)
 * @param widget cast=(GtkWidget *)
 * @param detail cast=(gchar *)
 */
public static final native void _gtk_paint_extension(int /*long*/ style, int /*long*/ window, int state_type, int shadow_type, GdkRectangle area, int /*long*/ widget, byte[] detail, int x , int y, int width, int height, int gap_side);
public static final void gtk_paint_extension(int /*long*/ style, int /*long*/ window, int state_type, int shadow_type, GdkRectangle area, int /*long*/ widget, byte[] detail, int x , int y, int width, int height, int gap_side) {
	lock.lock();
	try {
		_gtk_paint_extension(style, window, state_type, shadow_type, area, widget, detail, x, y, width, height, gap_side);
	} finally {
		lock.unlock();
	}
}
/**
 * @param style cast=(GtkStyle *)
 * @param window cast=(GdkWindow *)
 * @param widget cast=(GtkWidget *)
 * @param detail cast=(const gchar *)
 */
public static final native void _gtk_paint_hline(int /*long*/ style, int /*long*/ window, int state_type, GdkRectangle area, int /*long*/ widget, byte[] detail, int x1 , int x2, int y);
public static final void gtk_paint_hline(int /*long*/ style, int /*long*/ window, int state_type, GdkRectangle area, int /*long*/ widget, byte[] detail, int x1 , int x2, int y) {
	lock.lock();
	try {
		_gtk_paint_hline(style, window, state_type, area, widget, detail, x1, x2, y);
	} finally {
		lock.unlock();
	}
}
/**
 * @param style cast=(GtkStyle *)
 * @param window cast=(GdkWindow *)
 * @param widget cast=(GtkWidget *)
 * @param detail cast=(const gchar *)
 * @param layout cast=(PangoLayout *)
 */
public static final native void _gtk_paint_layout(int /*long*/ style, int /*long*/ window, int state_type, boolean use_text, GdkRectangle area, int /*long*/ widget, byte[] detail, int x , int y, int /*long*/ layout);
public static final void gtk_paint_layout(int /*long*/ style, int /*long*/ window, int state_type, boolean use_text, GdkRectangle area, int /*long*/ widget, byte[] detail, int x , int y, int /*long*/ layout) {
	lock.lock();
	try {
		_gtk_paint_layout(style, window, state_type, use_text, area, widget, detail, x, y, layout);
	} finally {
		lock.unlock();
	}
}
/**
 * @param style cast=(GtkStyle *)
 * @param window cast=(GdkWindow *)
 * @param widget cast=(GtkWidget *)
 * @param detail cast=(gchar *)
 */
public static final native void _gtk_paint_shadow_gap(int /*long*/ style, int /*long*/ window, int state_type, int shadow_type, GdkRectangle area, int /*long*/ widget, byte[] detail, int x , int y, int width, int height, int gap_side, int gap_x, int gap_width);
public static final void gtk_paint_shadow_gap(int /*long*/ style, int /*long*/ window, int state_type, int shadow_type, GdkRectangle area, int /*long*/ widget, byte[] detail, int x , int y, int width, int height, int gap_side, int gap_x, int gap_width) {
	lock.lock();
	try {
		_gtk_paint_shadow_gap(style, window, state_type, shadow_type, area, widget, detail, x, y, width, height, gap_side, gap_x, gap_width);
	} finally {
		lock.unlock();
	}
}
/**
 * @param style cast=(GtkStyle *)
 * @param window cast=(GdkWindow *)
 * @param widget cast=(GtkWidget *)
 * @param detail cast=(gchar *)
 */
public static final native void _gtk_paint_shadow(int /*long*/ style, int /*long*/ window, int state_type, int shadow_type, GdkRectangle area, int /*long*/ widget, byte[] detail, int x , int y, int width, int height);
public static final void gtk_paint_shadow(int /*long*/ style, int /*long*/ window, int state_type, int shadow_type, GdkRectangle area, int /*long*/ widget, byte[] detail, int x , int y, int width, int height) {
	lock.lock();
	try {
		_gtk_paint_shadow(style, window, state_type, shadow_type, area, widget, detail, x, y, width, height);
	} finally {
		lock.unlock();
	}
}
/**
 * @param style cast=(GtkStyle *)
 * @param window cast=(GdkWindow *)
 * @param widget cast=(GtkWidget *)
 * @param detail cast=(const gchar *)
 */
public static final native void _gtk_paint_vline(int /*long*/ style, int /*long*/ window, int state_type, GdkRectangle area, int /*long*/ widget, byte[] detail, int y1 , int y2, int x);
public static final void gtk_paint_vline(int /*long*/ style, int /*long*/ window, int state_type, GdkRectangle area, int /*long*/ widget, byte[] detail, int y1 , int y2, int x) {
	lock.lock();
	try {
		_gtk_paint_vline(style, window, state_type, area, widget, detail, y1, y2, x);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _gtk_paper_size_free(int /*long*/ size);
public static final void gtk_paper_size_free(int /*long*/ size) {
	lock.lock();
	try {
		_gtk_paper_size_free(size);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _gtk_paper_size_new(byte [] name);
public static final int /*long*/ gtk_paper_size_new(byte [] name) {
	lock.lock();
	try {
		return _gtk_paper_size_new(name);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _gtk_paper_size_new_from_ppd(byte [] ppd_name, byte [] ppd_display_name, double width, double height);
public static final int /*long*/ gtk_paper_size_new_from_ppd(byte [] ppd_name, byte [] ppd_display_name, double width, double height) {
	lock.lock();
	try {
		return _gtk_paper_size_new_from_ppd(ppd_name, ppd_display_name, width, height);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _gtk_paper_size_new_custom(byte [] name, byte [] display_name, double width, double height, int unit);
public static final int /*long*/ gtk_paper_size_new_custom(byte [] name, byte [] display_name, double width, double height, int unit) {
	lock.lock();
	try {
		return _gtk_paper_size_new_custom(name, display_name, width, height, unit);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _gtk_paper_size_get_name(int /*long*/ size);
public static final int /*long*/ gtk_paper_size_get_name(int /*long*/ size) {
	lock.lock();
	try {
		return _gtk_paper_size_get_name(size);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _gtk_paper_size_get_display_name(int /*long*/ size);
public static final int /*long*/ gtk_paper_size_get_display_name(int /*long*/ size) {
	lock.lock();
	try {
		return _gtk_paper_size_get_display_name(size);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _gtk_paper_size_get_ppd_name(int /*long*/ size);
public static final int /*long*/ gtk_paper_size_get_ppd_name(int /*long*/ size) {
	lock.lock();
	try {
		return _gtk_paper_size_get_ppd_name(size);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native double _gtk_paper_size_get_width(int /*long*/ size, int unit);
public static final double gtk_paper_size_get_width(int /*long*/ size, int unit) {
	lock.lock();
	try {
		return _gtk_paper_size_get_width(size, unit);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native double _gtk_paper_size_get_height(int /*long*/ size, int unit);
public static final double gtk_paper_size_get_height(int /*long*/ size, int unit) {
	lock.lock();
	try {
		return _gtk_paper_size_get_height(size, unit);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native boolean _gtk_paper_size_is_custom(int /*long*/ size);
public static final boolean gtk_paper_size_is_custom(int /*long*/ size) {
	lock.lock();
	try {
		return _gtk_paper_size_is_custom(size);
	} finally {
		lock.unlock();
	}
}
/** @param plug cast=(GtkPlug *) */
public static final native int /*long*/ _gtk_plug_get_id(int /*long*/ plug);
public static final int /*long*/ gtk_plug_get_id(int /*long*/ plug) {
	lock.lock();
	try {
		return _gtk_plug_get_id(plug);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gtk_plug_new(int /*long*/ socket_id);
public static final int /*long*/ gtk_plug_new(int /*long*/ socket_id) {
	lock.lock();
	try {
		return _gtk_plug_new(socket_id);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _gtk_printer_get_backend(int /*long*/ printer);
public static final int /*long*/ gtk_printer_get_backend(int /*long*/ printer) {
	lock.lock();
	try {
		return _gtk_printer_get_backend(printer);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _gtk_printer_get_name(int /*long*/ printer);
public static final int /*long*/ gtk_printer_get_name(int /*long*/ printer) {
	lock.lock();
	try {
		return _gtk_printer_get_name(printer);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native boolean _gtk_printer_is_default(int /*long*/ printer);
public static final boolean gtk_printer_is_default(int /*long*/ printer) {
	lock.lock();
	try {
		return _gtk_printer_is_default(printer);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param data cast=(gpointer)
 * @param destroy cast=(GDestroyNotify)
 * @param wait cast=(gboolean)
 */
public static final native void _gtk_enumerate_printers(int /*long*/ func, int /*long*/data, int /*long*/ destroy, boolean wait);
public static final void gtk_enumerate_printers(int /*long*/ func, int /*long*/data, int /*long*/ destroy, boolean wait) {
	lock.lock();
	try {
		_gtk_enumerate_printers(func, data, destroy, wait);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param title cast=(const gchar *)
 */
public static final native int /*long*/ _gtk_print_job_new(byte[] title, int /*long*/ printer, int /*long*/ settings, int /*long*/ page_setup);
public static final int /*long*/ gtk_print_job_new(byte[] title, int /*long*/ printer, int /*long*/ settings, int /*long*/ page_setup) {
	lock.lock();
	try {
		return _gtk_print_job_new(title, printer, settings, page_setup);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _gtk_print_job_get_settings(int /*long*/ job);
public static final int /*long*/ gtk_print_job_get_settings(int /*long*/ job) {
	lock.lock();
	try {
		return _gtk_print_job_get_settings(job);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _gtk_print_job_get_printer(int /*long*/ job);
public static final int /*long*/ gtk_print_job_get_printer(int /*long*/ job) {
	lock.lock();
	try {
		return _gtk_print_job_get_printer(job);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _gtk_print_job_get_title(int /*long*/ job);
public static final int /*long*/ gtk_print_job_get_title(int /*long*/ job) {
	lock.lock();
	try {
		return _gtk_print_job_get_title(job);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int _gtk_print_job_get_status(int /*long*/ job);
public static final int gtk_print_job_get_status(int /*long*/ job) {
	lock.lock();
	try {
		return _gtk_print_job_get_status(job);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param filename cast=(const gchar *)
 * @param error cast=(GError **)
 */
public static final native boolean _gtk_print_job_set_source_file(int /*long*/ job, byte[] filename, int /*long*/ error[]);
public static final boolean gtk_print_job_set_source_file(int /*long*/ job, byte[] filename, int /*long*/ error[]) {
	lock.lock();
	try {
		return _gtk_print_job_set_source_file(job, filename, error);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param error cast=(GError **)
 */
public static final native int /*long*/ _gtk_print_job_get_surface(int /*long*/ job, int /*long*/ error[]);
public static final int /*long*/ gtk_print_job_get_surface(int /*long*/ job, int /*long*/ error[]) {
	lock.lock();
	try {
		return _gtk_print_job_get_surface(job, error);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param user_data cast=(gpointer)
 * @param dnotify cast=(GDestroyNotify)
 */
public static final native void _gtk_print_job_send(int /*long*/ job, int /*long*/ callback, int /*long*/ user_data, int /*long*/ dnotify);
public static final void gtk_print_job_send(int /*long*/ job, int /*long*/ callback, int /*long*/ user_data, int /*long*/ dnotify) {
	lock.lock();
	try {
		_gtk_print_job_send(job, callback, user_data, dnotify);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _gtk_print_settings_new();
public static final int /*long*/ gtk_print_settings_new() {
	lock.lock();
	try {
		return _gtk_print_settings_new();
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param data cast=(gpointer)
 */
public static final native void _gtk_print_settings_foreach(int /*long*/ settings, int /*long*/ func, int /*long*/ data);
public static final void gtk_print_settings_foreach(int /*long*/ settings, int /*long*/ func, int /*long*/ data) {
	lock.lock();
	try {
		_gtk_print_settings_foreach(settings, func, data);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param key cast=(const gchar *)
 */
public static final native int /*long*/ _gtk_print_settings_get(int /*long*/ settings, byte [] key);
public static final int /*long*/ gtk_print_settings_get(int /*long*/ settings, byte [] key) {
	lock.lock();
	try {
		return _gtk_print_settings_get(settings, key);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param key cast=(const gchar *)
 * @param value cast=(const gchar *)
 */
public static final native void _gtk_print_settings_set(int /*long*/ settings, byte [] key, byte [] value);
public static final void gtk_print_settings_set(int /*long*/ settings, byte [] key, byte [] value) {
	lock.lock();
	try {
		_gtk_print_settings_set(settings, key, value);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _gtk_print_settings_get_printer(int /*long*/ settings);
public static final int /*long*/ gtk_print_settings_get_printer(int /*long*/ settings) {
	lock.lock();
	try {
		return _gtk_print_settings_get_printer(settings);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _gtk_print_settings_set_printer(int /*long*/ settings, byte[] printer);
public static final void gtk_print_settings_set_printer(int /*long*/ settings, byte[] printer) {
	lock.lock();
	try {
		_gtk_print_settings_set_printer(settings, printer);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int _gtk_print_settings_get_orientation(int /*long*/ settings);
public static final int gtk_print_settings_get_orientation(int /*long*/ settings) {
	lock.lock();
	try {
		return _gtk_print_settings_get_orientation(settings);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _gtk_print_settings_set_orientation(int /*long*/ settings, int orientation);
public static final void gtk_print_settings_set_orientation(int /*long*/ settings, int orientation) {
	lock.lock();
	try {
		_gtk_print_settings_set_orientation(settings, orientation);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native boolean _gtk_print_settings_get_collate(int /*long*/ settings);
public static final boolean gtk_print_settings_get_collate(int /*long*/ settings) {
	lock.lock();
	try {
		return _gtk_print_settings_get_collate(settings);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param collate cast=(gboolean)
 */
public static final native void _gtk_print_settings_set_collate(int /*long*/ settings, boolean collate);
public static final void gtk_print_settings_set_collate(int /*long*/ settings, boolean collate) {
	lock.lock();
	try {
		_gtk_print_settings_set_collate(settings, collate);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int _gtk_print_settings_get_n_copies(int /*long*/ settings);
public static final int gtk_print_settings_get_n_copies(int /*long*/ settings) {
	lock.lock();
	try {
		return _gtk_print_settings_get_n_copies(settings);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param num_copies cast=(gint)
 */
public static final native void _gtk_print_settings_set_n_copies(int /*long*/ settings, int num_copies);
public static final void gtk_print_settings_set_n_copies(int /*long*/ settings, int num_copies) {
	lock.lock();
	try {
		_gtk_print_settings_set_n_copies(settings, num_copies);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int _gtk_print_settings_get_print_pages(int /*long*/ settings);
public static final int gtk_print_settings_get_print_pages(int /*long*/ settings) {
	lock.lock();
	try {
		return _gtk_print_settings_get_print_pages(settings);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _gtk_print_settings_set_print_pages(int /*long*/ settings, int pages);
public static final void gtk_print_settings_set_print_pages(int /*long*/ settings, int pages) {
	lock.lock();
	try {
		_gtk_print_settings_set_print_pages(settings, pages);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param num_ranges cast=(gint *)
 */
public static final native int /*long*/ _gtk_print_settings_get_page_ranges(int /*long*/ settings, int[] num_ranges);
public static final int /*long*/ gtk_print_settings_get_page_ranges(int /*long*/ settings, int[] num_ranges) {
	lock.lock();
	try {
		return _gtk_print_settings_get_page_ranges(settings, num_ranges);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param num_ranges cast=(gint)
 */
public static final native void _gtk_print_settings_set_page_ranges(int /*long*/ settings, int[] page_ranges, int num_ranges);
public static final void gtk_print_settings_set_page_ranges(int /*long*/ settings, int[] page_ranges, int num_ranges) {
	lock.lock();
	try {
		_gtk_print_settings_set_page_ranges(settings, page_ranges, num_ranges);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native double _gtk_print_settings_get_paper_width(int /*long*/ settings, int unit);
public static final double gtk_print_settings_get_paper_width(int /*long*/ settings, int unit) {
	lock.lock();
	try {
		return _gtk_print_settings_get_paper_width(settings, unit);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native double _gtk_print_settings_get_paper_height(int /*long*/ settings, int unit);
public static final double gtk_print_settings_get_paper_height(int /*long*/ settings, int unit) {
	lock.lock();
	try {
		return _gtk_print_settings_get_paper_height(settings, unit);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int _gtk_print_settings_get_resolution(int /*long*/ settings);
public static final int gtk_print_settings_get_resolution(int /*long*/ settings) {
	lock.lock();
	try {
		return _gtk_print_settings_get_resolution(settings);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param title cast=(const gchar *)
 * @param parent cast=(GtkWindow *)
 */
public static final native int /*long*/ _gtk_print_unix_dialog_new(byte[] title, int /*long*/ parent);
public static final int /*long*/ gtk_print_unix_dialog_new(byte[] title, int /*long*/ parent) {
	lock.lock();
	try {
		return _gtk_print_unix_dialog_new(title, parent);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _gtk_print_unix_dialog_set_page_setup(int /*long*/ dialog, int /*long*/ page_setup);
public static final void gtk_print_unix_dialog_set_page_setup(int /*long*/ dialog, int /*long*/ page_setup) {
	lock.lock();
	try {
		_gtk_print_unix_dialog_set_page_setup(dialog, page_setup);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _gtk_print_unix_dialog_get_page_setup(int /*long*/ dialog);
public static final int /*long*/ gtk_print_unix_dialog_get_page_setup(int /*long*/ dialog) {
	lock.lock();
	try {
		return _gtk_print_unix_dialog_get_page_setup(dialog);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param current_page cast=(gint)
 */
public static final native void _gtk_print_unix_dialog_set_current_page(int /*long*/ dialog, int current_page);
public static final void gtk_print_unix_dialog_set_current_page(int /*long*/ dialog, int current_page) {
	lock.lock();
	try {
		_gtk_print_unix_dialog_set_current_page(dialog, current_page);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int _gtk_print_unix_dialog_get_current_page(int /*long*/ dialog);
public static final int gtk_print_unix_dialog_get_current_page(int /*long*/ dialog) {
	lock.lock();
	try {
		return _gtk_print_unix_dialog_get_current_page(dialog);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _gtk_print_unix_dialog_set_settings(int /*long*/ dialog, int /*long*/ settings);
public static final void gtk_print_unix_dialog_set_settings(int /*long*/ dialog, int /*long*/ settings) {
	lock.lock();
	try {
		_gtk_print_unix_dialog_set_settings(dialog, settings);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _gtk_print_unix_dialog_get_settings(int /*long*/ dialog);
public static final int /*long*/ gtk_print_unix_dialog_get_settings(int /*long*/ dialog) {
	lock.lock();
	try {
		return _gtk_print_unix_dialog_get_settings(dialog);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _gtk_print_unix_dialog_get_selected_printer(int /*long*/ dialog);
public static final int /*long*/ gtk_print_unix_dialog_get_selected_printer(int /*long*/ dialog) {
	lock.lock();
	try {
		return _gtk_print_unix_dialog_get_selected_printer(dialog);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _gtk_print_unix_dialog_set_manual_capabilities(int /*long*/ dialog, int /*long*/ capabilities);
public static final void gtk_print_unix_dialog_set_manual_capabilities(int /*long*/ dialog, int /*long*/ capabilities) {
	lock.lock();
	try {
		_gtk_print_unix_dialog_set_manual_capabilities(dialog, capabilities);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gtk_progress_bar_new();
public static final int /*long*/ gtk_progress_bar_new() {
	lock.lock();
	try {
		return _gtk_progress_bar_new();
	} finally {
		lock.unlock();
	}
}
/** @param pbar cast=(GtkProgressBar *) */
public static final native void _gtk_progress_bar_pulse(int /*long*/ pbar);
public static final void gtk_progress_bar_pulse(int /*long*/ pbar) {
	lock.lock();
	try {
		_gtk_progress_bar_pulse(pbar);
	} finally {
		lock.unlock();
	}
}
/**
 * @param pbar cast=(GtkProgressBar *)
 * @param fraction cast=(gdouble)
 */
public static final native void _gtk_progress_bar_set_fraction(int /*long*/ pbar, double fraction);
public static final void gtk_progress_bar_set_fraction(int /*long*/ pbar, double fraction) {
	lock.lock();
	try {
		_gtk_progress_bar_set_fraction(pbar, fraction);
	} finally {
		lock.unlock();
	}
}
/**
 * @param pbar cast=(GtkProgressBar *)
 * @param orientation cast=(GtkProgressBarOrientation)
 */
public static final native void _gtk_progress_bar_set_orientation(int /*long*/ pbar, int orientation);
public static final void gtk_progress_bar_set_orientation(int /*long*/ pbar, int orientation) {
	lock.lock();
	try {
		_gtk_progress_bar_set_orientation(pbar, orientation);
	} finally {
		lock.unlock();
	}
}
/** @param radio_button cast=(GtkRadioButton *) */
public static final native int /*long*/ _gtk_radio_button_get_group(int /*long*/ radio_button);
public static final int /*long*/ gtk_radio_button_get_group(int /*long*/ radio_button) {
	lock.lock();
	try {
		return _gtk_radio_button_get_group(radio_button);
	} finally {
		lock.unlock();
	}
}
/** @param group cast=(GSList *) */
public static final native int /*long*/ _gtk_radio_button_new(int /*long*/ group);
public static final int /*long*/ gtk_radio_button_new(int /*long*/ group) {
	lock.lock();
	try {
		return _gtk_radio_button_new(group);
	} finally {
		lock.unlock();
	}
}
/** @param radio_menu_item cast=(GtkRadioMenuItem *) */
public static final native int /*long*/ _gtk_radio_menu_item_get_group(int /*long*/ radio_menu_item);
public static final int /*long*/ gtk_radio_menu_item_get_group(int /*long*/ radio_menu_item) {
	lock.lock();
	try {
		return _gtk_radio_menu_item_get_group(radio_menu_item);
	} finally {
		lock.unlock();
	}
}
/** @param group cast=(GSList *) */
public static final native int /*long*/ _gtk_radio_menu_item_new(int /*long*/ group);
public static final int /*long*/ gtk_radio_menu_item_new(int /*long*/ group) {
	lock.lock();
	try {
		return _gtk_radio_menu_item_new(group);
	} finally {
		lock.unlock();
	}
}
/**
 * @param group cast=(GSList *)
 * @param label cast=(const gchar *)
 */
public static final native int /*long*/ _gtk_radio_menu_item_new_with_label(int /*long*/ group, byte[] label);
public static final int /*long*/ gtk_radio_menu_item_new_with_label(int /*long*/ group, byte[] label) {
	lock.lock();
	try {
		return _gtk_radio_menu_item_new_with_label(group, label);
	} finally {
		lock.unlock();
	}
}
/** @param range cast=(GtkRange *) */
public static final native int /*long*/ _gtk_range_get_adjustment(int /*long*/ range);
public static final int /*long*/ gtk_range_get_adjustment(int /*long*/ range) {
	lock.lock();
	try {
		return _gtk_range_get_adjustment(range);
	} finally {
		lock.unlock();
	}
}
/** @param range cast=(GtkRange *) */
public static final native void _gtk_range_set_increments(int /*long*/ range, double step, double page);
public static final void gtk_range_set_increments(int /*long*/ range, double step, double page) {
	lock.lock();
	try {
		_gtk_range_set_increments(range, step, page);
	} finally {
		lock.unlock();
	}
}
/** @param range cast=(GtkRange *) */
public static final native void _gtk_range_set_inverted(int /*long*/ range, boolean setting);
public static final void gtk_range_set_inverted(int /*long*/ range, boolean setting) {
	lock.lock();
	try {
		_gtk_range_set_inverted(range, setting);
	} finally {
		lock.unlock();
	}
}
/** @param range cast=(GtkRange *) */
public static final native void _gtk_range_set_range(int /*long*/ range, double min, double max);
public static final void gtk_range_set_range(int /*long*/ range, double min, double max) {
	lock.lock();
	try {
		_gtk_range_set_range(range, min, max);
	} finally {
		lock.unlock();
	}
}
/** @param range cast=(GtkRange *) */
public static final native void _gtk_range_set_value(int /*long*/ range, double value);
public static final void gtk_range_set_value(int /*long*/ range, double value) {
	lock.lock();
	try {
		_gtk_range_set_value(range, value);
	} finally {
		lock.unlock();
	}
}
/** @param rc_string cast=(const gchar *) */
public static final native void _gtk_rc_parse_string(byte[] rc_string);
public static final void gtk_rc_parse_string(byte[] rc_string) {
	lock.lock();
	try {
		_gtk_rc_parse_string(rc_string);
	} finally {
		lock.unlock();
	}
}
/** @param style cast=(GtkRcStyle *) */
public static final native int /*long*/ _gtk_rc_style_get_bg_pixmap_name(int /*long*/ style, int index);
public static final int /*long*/ gtk_rc_style_get_bg_pixmap_name(int /*long*/ style, int index) {
	lock.lock();
	try {
		return _gtk_rc_style_get_bg_pixmap_name(style, index);
	} finally {
		lock.unlock();
	}
}
/** @param style cast=(GtkRcStyle *) */
public static final native int _gtk_rc_style_get_color_flags(int /*long*/ style, int index);
public static final int gtk_rc_style_get_color_flags(int /*long*/ style, int index) {
	lock.lock();
	try {
		return _gtk_rc_style_get_color_flags(style, index);
	} finally {
		lock.unlock();
	}
}
/**
 * @param style cast=(GtkRcStyle *)
 * @param color flags=no_out
 */
public static final native void _gtk_rc_style_set_bg(int /*long*/ style, int index, GdkColor color);
public static final void gtk_rc_style_set_bg(int /*long*/ style, int index, GdkColor color) {
	lock.lock();
	try {
		_gtk_rc_style_set_bg(style, index, color);
	} finally {
		lock.unlock();
	}
}
/**
 * @param style cast=(GtkRcStyle *)
 * @param name cast=(char *)
 */
public static final native void _gtk_rc_style_set_bg_pixmap_name(int /*long*/ style, int index, int /*long*/ name);
public static final void gtk_rc_style_set_bg_pixmap_name(int /*long*/ style, int index, int /*long*/ name) {
	lock.lock();
	try {
		_gtk_rc_style_set_bg_pixmap_name(style, index, name);
	} finally {
		lock.unlock();
	}
}
/** @param style cast=(GtkRcStyle *) */
public static final native void _gtk_rc_style_set_color_flags(int /*long*/ style, int index, int flag);
public static final void gtk_rc_style_set_color_flags(int /*long*/ style, int index, int flag) {
	lock.lock();
	try {
		_gtk_rc_style_set_color_flags(style, index, flag);
	} finally {
		lock.unlock();
	}
}
/**
 * @param scale cast=(GtkScale *)
 * @param digits cast=(gint)
 */
public static final native void _gtk_scale_set_digits(int /*long*/ scale, int digits);
public static final void gtk_scale_set_digits(int /*long*/ scale, int digits) {
	lock.lock();
	try {
		_gtk_scale_set_digits(scale, digits);
	} finally {
		lock.unlock();
	}
}
/**
 * @param scale cast=(GtkScale *)
 * @param draw_value cast=(gboolean)
 */
public static final native void _gtk_scale_set_draw_value(int /*long*/ scale, boolean draw_value);
public static final void gtk_scale_set_draw_value(int /*long*/ scale, boolean draw_value) {
	lock.lock();
	try {
		_gtk_scale_set_draw_value(scale, draw_value);
	} finally {
		lock.unlock();
	}
}
/**
 * @param style cast=(GtkRcStyle *)
 * @param color flags=no_out
 */
public static final native void _gtk_rc_style_set_fg(int /*long*/ style, int index, GdkColor color);
public static final void gtk_rc_style_set_fg(int /*long*/ style, int index, GdkColor color) {
	lock.lock();
	try {
		_gtk_rc_style_set_fg(style, index, color);
	} finally {
		lock.unlock();
	}
}
/**
 * @param style cast=(GtkRcStyle *)
 * @param color flags=no_out
 */
public static final native void _gtk_rc_style_set_text(int /*long*/ style, int index, GdkColor color);
public static final void gtk_rc_style_set_text(int /*long*/ style, int index, GdkColor color) {
	lock.lock();
	try {
		_gtk_rc_style_set_text(style, index, color);
	} finally {
		lock.unlock();
	}
}
/**
 * @param scrolled_window cast=(GtkScrolledWindow *)
 * @param child cast=(GtkWidget *)
 */
public static final native void _gtk_scrolled_window_add_with_viewport(int /*long*/ scrolled_window, int /*long*/ child);
public static final void gtk_scrolled_window_add_with_viewport(int /*long*/ scrolled_window, int /*long*/ child) {
	lock.lock();
	try {
		_gtk_scrolled_window_add_with_viewport(scrolled_window, child);
	} finally {
		lock.unlock();
	}
}
/** @param scrolled_window cast=(GtkScrolledWindow *) */
public static final native int /*long*/ _gtk_scrolled_window_get_hadjustment(int /*long*/ scrolled_window);
public static final int /*long*/ gtk_scrolled_window_get_hadjustment(int /*long*/ scrolled_window) {
	lock.lock();
	try {
		return _gtk_scrolled_window_get_hadjustment(scrolled_window);
	} finally {
		lock.unlock();
	}
}
/**
 * @param scrolled_window cast=(GtkScrolledWindow *)
 * @param hscrollbar_policy cast=(GtkPolicyType *)
 * @param vscrollbar_policy cast=(GtkPolicyType *)
 */
public static final native void _gtk_scrolled_window_get_policy(int /*long*/ scrolled_window, int[] hscrollbar_policy, int[] vscrollbar_policy);
public static final void gtk_scrolled_window_get_policy(int /*long*/ scrolled_window, int[] hscrollbar_policy, int[] vscrollbar_policy) {
	lock.lock();
	try {
		_gtk_scrolled_window_get_policy(scrolled_window, hscrollbar_policy, vscrollbar_policy);
	} finally {
		lock.unlock();
	}
}
/** @param scrolled_window cast=(GtkScrolledWindow *) */
public static final native int _gtk_scrolled_window_get_shadow_type(int /*long*/ scrolled_window);
public static final int gtk_scrolled_window_get_shadow_type(int /*long*/ scrolled_window) {
	lock.lock();
	try {
		return _gtk_scrolled_window_get_shadow_type(scrolled_window);
	} finally {
		lock.unlock();
	}
}
/** @param scrolled_window cast=(GtkScrolledWindow *) */
public static final native int /*long*/ _gtk_scrolled_window_get_vadjustment(int /*long*/ scrolled_window);
public static final int /*long*/ gtk_scrolled_window_get_vadjustment(int /*long*/ scrolled_window) {
	lock.lock();
	try {
		return _gtk_scrolled_window_get_vadjustment(scrolled_window);
	} finally {
		lock.unlock();
	}
}
/**
 * @param hadjustment cast=(GtkAdjustment *)
 * @param vadjustment cast=(GtkAdjustment *)
 */
public static final native int /*long*/ _gtk_scrolled_window_new(int /*long*/ hadjustment, int /*long*/ vadjustment);
public static final int /*long*/ gtk_scrolled_window_new(int /*long*/ hadjustment, int /*long*/ vadjustment) {
	lock.lock();
	try {
		return _gtk_scrolled_window_new(hadjustment, vadjustment);
	} finally {
		lock.unlock();
	}
}
/**
 * @param scrolled_window cast=(GtkScrolledWindow *)
 * @param placement cast=(GtkCornerType)
 */
public static final native void _gtk_scrolled_window_set_placement(int /*long*/ scrolled_window, int placement);
public static final void gtk_scrolled_window_set_placement(int /*long*/ scrolled_window, int placement) {
	lock.lock();
	try {
		_gtk_scrolled_window_set_placement(scrolled_window, placement);
	} finally {
		lock.unlock();
	}
}
/**
 * @param scrolled_window cast=(GtkScrolledWindow *)
 * @param hscrollbar_policy cast=(GtkPolicyType)
 * @param vscrollbar_policy cast=(GtkPolicyType)
 */
public static final native void _gtk_scrolled_window_set_policy(int /*long*/ scrolled_window, int hscrollbar_policy, int vscrollbar_policy);
public static final void gtk_scrolled_window_set_policy(int /*long*/ scrolled_window, int hscrollbar_policy, int vscrollbar_policy) {
	lock.lock();
	try {
		_gtk_scrolled_window_set_policy(scrolled_window, hscrollbar_policy, vscrollbar_policy);
	} finally {
		lock.unlock();
	}
}
/**
 * @param scrolled_window cast=(GtkScrolledWindow *)
 * @param type cast=(GtkShadowType)
 */
public static final native void _gtk_scrolled_window_set_shadow_type(int /*long*/ scrolled_window, int type);
public static final void gtk_scrolled_window_set_shadow_type(int /*long*/ scrolled_window, int type) {
	lock.lock();
	try {
		_gtk_scrolled_window_set_shadow_type(scrolled_window, type);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gtk_settings_get_default();
public static final int /*long*/ gtk_settings_get_default() {
	lock.lock();
	try {
		return _gtk_settings_get_default();
	} finally {
		lock.unlock();
	}
}
/** @param selection_data cast=(GtkSelectionData *) */
public static final native void _gtk_selection_data_free(int /*long*/ selection_data);
public static final void gtk_selection_data_free(int /*long*/ selection_data) {
	lock.lock();
	try {
		_gtk_selection_data_free(selection_data);
	} finally {
		lock.unlock();
	}
}
/**
 * @param selection_data cast=(GtkSelectionData *)
 * @param type cast=(GdkAtom)
 * @param format cast=(gint)
 * @param data cast=(const guchar *)
 * @param length cast=(gint)
 */
public static final native void _gtk_selection_data_set(int /*long*/ selection_data, int /*long*/ type, int format, int /*long*/ data, int length);
public static final void gtk_selection_data_set(int /*long*/ selection_data, int /*long*/ type, int format, int /*long*/ data, int length) {
	lock.lock();
	try {
		_gtk_selection_data_set(selection_data, type, format, data, length);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gtk_separator_menu_item_new();
public static final int /*long*/ gtk_separator_menu_item_new() {
	lock.lock();
	try {
		return _gtk_separator_menu_item_new();
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gtk_set_locale();
public static final int /*long*/ gtk_set_locale() {
	lock.lock();
	try {
		return _gtk_set_locale();
	} finally {
		lock.unlock();
	}
}
/** @param socket cast=(GtkSocket *) */
public static final native int /*long*/ _gtk_socket_get_id(int /*long*/ socket);
public static final int /*long*/ gtk_socket_get_id(int /*long*/ socket) {
	lock.lock();
	try {
		return _gtk_socket_get_id(socket);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gtk_socket_new();
public static final int /*long*/ gtk_socket_new() {
	lock.lock();
	try {
		return _gtk_socket_new();
	} finally {
		lock.unlock();
	}
}
/** @param adjustment cast=(GtkAdjustment *) */
public static final native int /*long*/ _gtk_spin_button_new(int /*long*/ adjustment, double climb_rate, int digits);
public static final int /*long*/ gtk_spin_button_new(int /*long*/ adjustment, double climb_rate, int digits) {
	lock.lock();
	try {
		return _gtk_spin_button_new(adjustment, climb_rate, digits);
	} finally {
		lock.unlock();
	}
}
/** @param spin_button cast=(GtkSpinButton*) */
public static final native int /*long*/ _gtk_spin_button_get_adjustment(int /*long*/ spin_button);
public static final int /*long*/ gtk_spin_button_get_adjustment(int /*long*/ spin_button) {
	lock.lock();
	try {
		return _gtk_spin_button_get_adjustment(spin_button);
	} finally {
		lock.unlock();
	}
}

/** @param spin_button cast=(GtkSpinButton*) */
public static final native int _gtk_spin_button_get_digits(int /*long*/ spin_button);
public static final int gtk_spin_button_get_digits(int /*long*/ spin_button) {
	lock.lock();
	try {
		return _gtk_spin_button_get_digits(spin_button);
	} finally {
		lock.unlock();
	}
}
/** @param spin_button cast=(GtkSpinButton*) */
public static final native void _gtk_spin_button_set_digits(int /*long*/ spin_button, int digits);
public static final void gtk_spin_button_set_digits(int /*long*/ spin_button, int digits) {
	lock.lock();
	try {
		_gtk_spin_button_set_digits(spin_button, digits);
	} finally {
		lock.unlock();
	}
}
/** @param spin_button cast=(GtkSpinButton*) */
public static final native void _gtk_spin_button_set_increments(int /*long*/ spin_button, double step, double page);
public static final void gtk_spin_button_set_increments(int /*long*/ spin_button, double step, double page) {
	lock.lock();
	try {
		_gtk_spin_button_set_increments(spin_button, step, page);
	} finally {
		lock.unlock();
	}
}
/** @param spin_button cast=(GtkSpinButton*) */
public static final native void _gtk_spin_button_set_range(int /*long*/ spin_button, double max, double min);
public static final void gtk_spin_button_set_range(int /*long*/ spin_button, double max, double min) {
	lock.lock();
	try {
		_gtk_spin_button_set_range(spin_button, max, min);
	} finally {
		lock.unlock();
	}
}
/** @param spin_button cast=(GtkSpinButton*) */
public static final native void _gtk_spin_button_set_value(int /*long*/ spin_button, double value);
public static final void gtk_spin_button_set_value(int /*long*/ spin_button, double value) {
	lock.lock();
	try {
		_gtk_spin_button_set_value(spin_button, value);
	} finally {
		lock.unlock();
	}
}
/** @param spin_button cast=(GtkSpinButton*) */
public static final native void _gtk_spin_button_set_wrap(int /*long*/ spin_button, boolean wrap);
public static final void gtk_spin_button_set_wrap(int /*long*/ spin_button, boolean wrap) {
	lock.lock();
	try {
		_gtk_spin_button_set_wrap(spin_button, wrap);
	} finally {
		lock.unlock();
	}
}
/** @param spin_button cast=(GtkSpinButton*) */
public static final native void _gtk_spin_button_update(int /*long*/ spin_button);
public static final void gtk_spin_button_update(int /*long*/ spin_button) {
	lock.lock();
	try {
		_gtk_spin_button_update(spin_button);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 */
public static final native boolean _gtk_status_icon_get_geometry(int /*long*/ handle, int /*long*/ screen, GdkRectangle area, int /*long*/ orientation);
public static final boolean gtk_status_icon_get_geometry(int /*long*/ handle, int /*long*/ screen, GdkRectangle area, int /*long*/ orientation) {
	lock.lock();
	try {
		return _gtk_status_icon_get_geometry(handle, screen, area, orientation);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 */
public static final native boolean _gtk_status_icon_get_visible(int /*long*/ handle);
public static final boolean gtk_status_icon_get_visible(int /*long*/ handle) {
	lock.lock();
	try {
		return _gtk_status_icon_get_visible(handle);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _gtk_status_icon_new();
public static final int /*long*/ gtk_status_icon_new() {
	lock.lock();
	try {
		return _gtk_status_icon_new();
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 */
public static final native void _gtk_status_icon_set_from_pixbuf(int /*long*/ handle, int /*long*/ pixbuf);
public static final void gtk_status_icon_set_from_pixbuf(int /*long*/ handle, int /*long*/ pixbuf) {
	lock.lock();
	try {
		_gtk_status_icon_set_from_pixbuf(handle, pixbuf);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 */
public static final native void _gtk_status_icon_set_visible(int /*long*/ handle, boolean visible);
public static final void gtk_status_icon_set_visible(int /*long*/ handle, boolean visible) {
	lock.lock();
	try {
		_gtk_status_icon_set_visible(handle, visible);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 */
public static final native void _gtk_status_icon_set_tooltip(int /*long*/ handle, byte[] tip_text);
public static final void gtk_status_icon_set_tooltip(int /*long*/ handle, byte[] tip_text) {
	lock.lock();
	try {
		_gtk_status_icon_set_tooltip(handle, tip_text);
	} finally {
		lock.unlock();
	}
}
/**
 * @param style cast=(GtkStyle *)
 * @param color flags=no_in
 */
public static final native void _gtk_style_get_base(int /*long*/ style, int index, GdkColor color);
public static final void gtk_style_get_base(int /*long*/ style, int index, GdkColor color) {
	lock.lock();
	try {
		_gtk_style_get_base(style, index, color);
	} finally {
		lock.unlock();
	}
}
/**
 * @param style cast=(GtkStyle *)
 * @param color flags=no_in
 */
public static final native void _gtk_style_get_black(int /*long*/ style, GdkColor color);
public static final void gtk_style_get_black(int /*long*/ style, GdkColor color) {
	lock.lock();
	try {
		_gtk_style_get_black(style, color);
	} finally {
		lock.unlock();
	}
}
/**
 * @param style cast=(GtkStyle *)
 * @param color flags=no_in
 */
public static final native void _gtk_style_get_bg(int /*long*/ style, int index, GdkColor color);
public static final void gtk_style_get_bg(int /*long*/ style, int index, GdkColor color) {
	lock.lock();
	try {
		_gtk_style_get_bg(style, index, color);
	} finally {
		lock.unlock();
	}
}
/**
 * @param style cast=(GtkStyle *)
 * @param color flags=no_in
 */
public static final native void _gtk_style_get_dark(int /*long*/ style, int index, GdkColor color);
public static final void gtk_style_get_dark(int /*long*/ style, int index, GdkColor color) {
	lock.lock();
	try {
		_gtk_style_get_dark(style, index, color);
	} finally {
		lock.unlock();
	}
}
/**
 * @param style cast=(GtkStyle *)
 * @param color flags=no_in
 */
public static final native void _gtk_style_get_fg(int /*long*/ style, int index, GdkColor color);
public static final void gtk_style_get_fg(int /*long*/ style, int index, GdkColor color) {
	lock.lock();
	try {
		_gtk_style_get_fg(style, index, color);
	} finally {
		lock.unlock();
	}
}
/**
 * @param style cast=(GtkStyle *)
 * @param gc cast=(GdkGC **),flags=no_in
 */
public static final native void _gtk_style_get_fg_gc(int /*long*/ style, int index, int /*long*/[] gc);
public static final void gtk_style_get_fg_gc(int /*long*/ style, int index, int /*long*/[] gc) {
	lock.lock();
	try {
		_gtk_style_get_fg_gc(style, index, gc);
	} finally {
		lock.unlock();
	}
}
/**
 * @param style cast=(GtkStyle *)
 * @param gc cast=(GdkGC **),flags=no_in
 */
public static final native void _gtk_style_get_bg_gc(int /*long*/ style, int index, int /*long*/[] gc);
public static final void gtk_style_get_bg_gc(int /*long*/ style, int index, int /*long*/[] gc) {
	lock.lock();
	try {
		_gtk_style_get_bg_gc(style, index, gc);
	} finally {
		lock.unlock();
	}
}
/**
 * @param style cast=(GtkStyle *)
 * @param gc cast=(GdkGC **)
 */
public static final native void _gtk_style_get_light_gc(int /*long*/ style, int index, int /*long*/[] gc);
public static final void gtk_style_get_light_gc(int /*long*/ style, int index, int /*long*/[] gc) {
	lock.lock();
	try {
		_gtk_style_get_light_gc(style, index, gc);
	} finally {
		lock.unlock();
	}
}
/**
 * @param style cast=(GtkStyle *)
 * @param gc cast=(GdkGC **),flags=no_in
 */
public static final native void _gtk_style_get_dark_gc(int /*long*/ style, int index, int /*long*/[] gc);
public static final void gtk_style_get_dark_gc(int /*long*/ style, int index, int /*long*/[] gc) {
	lock.lock();
	try {
		_gtk_style_get_dark_gc(style, index, gc);
	} finally {
		lock.unlock();
	}
}
/**
 * @param style cast=(GtkStyle *)
 * @param gc cast=(GdkGC **)
 */
public static final native void _gtk_style_get_mid_gc(int /*long*/ style, int index, int /*long*/[] gc);
public static final void gtk_style_get_mid_gc(int /*long*/ style, int index, int /*long*/[] gc) {
	lock.lock();
	try {
		_gtk_style_get_mid_gc(style, index, gc);
	} finally {
		lock.unlock();
	}
}
/**
 * @param style cast=(GtkStyle *)
 * @param gc cast=(GdkGC **)
 */
public static final native void _gtk_style_get_text_gc(int /*long*/ style, int index, int /*long*/[] gc);
public static final void gtk_style_get_text_gc(int /*long*/ style, int index, int /*long*/[] gc) {
	lock.lock();
	try {
		_gtk_style_get_text_gc(style, index, gc);
	} finally {
		lock.unlock();
	}
}
/**
 * @param style cast=(GtkStyle *)
 * @param gc cast=(GdkGC **)
 */
public static final native void _gtk_style_get_text_aa_gc(int /*long*/ style, int index, int /*long*/[] gc);
public static final void gtk_style_get_text_aa_gc(int /*long*/ style, int index, int /*long*/[] gc) {
	lock.lock();
	try {
		_gtk_style_get_text_aa_gc(style, index, gc);
	} finally {
		lock.unlock();
	}
}
/**
 * @param style cast=(GtkStyle *)
 * @param gc cast=(GdkGC **),flags=no_in
 */
public static final native void _gtk_style_get_black_gc(int /*long*/ style, int /*long*/[] gc);
public static final void gtk_style_get_black_gc(int /*long*/ style, int /*long*/[] gc) {
	lock.lock();
	try {
		_gtk_style_get_black_gc(style, gc);
	} finally {
		lock.unlock();
	}
}
/**
 * @param style cast=(GtkStyle *)
 * @param gc cast=(GdkGC **)
 */
public static final native void _gtk_style_get_white_gc(int /*long*/ style, int /*long*/[] gc);
public static final void gtk_style_get_white_gc(int /*long*/ style, int /*long*/[] gc) {
	lock.lock();
	try {
		_gtk_style_get_white_gc(style, gc);
	} finally {
		lock.unlock();
	}
}
/** @param style cast=(GtkStyle *) */
public static final native int /*long*/ _gtk_style_get_font_desc(int /*long*/ style);
public static final int /*long*/ gtk_style_get_font_desc(int /*long*/ style) {
	lock.lock();
	try {
		return _gtk_style_get_font_desc(style);
	} finally {
		lock.unlock();
	}
}
/**
 * @param style cast=(GtkStyle *)
 * @param color flags=no_in
 */
public static final native void _gtk_style_get_light(int /*long*/ style, int index, GdkColor color);
public static final void gtk_style_get_light(int /*long*/ style, int index, GdkColor color) {
	lock.lock();
	try {
		_gtk_style_get_light(style, index, color);
	} finally {
		lock.unlock();
	}
}
/**
 * @param style cast=(GtkStyle *)
 * @param color flags=no_in
 */
public static final native void _gtk_style_get_text(int /*long*/ style, int index, GdkColor color);
public static final void gtk_style_get_text(int /*long*/ style, int index, GdkColor color) {
	lock.lock();
	try {
		_gtk_style_get_text(style, index, color);
	} finally {
		lock.unlock();
	}
}
/** @param style cast=(GtkStyle *) */
public static final native int _gtk_style_get_xthickness(int /*long*/ style);
public static final int gtk_style_get_xthickness(int /*long*/ style) {
	lock.lock();
	try {
		return _gtk_style_get_xthickness(style);
	} finally {
		lock.unlock();
	}
}
/** @param style cast=(GtkStyle *) */
public static final native int _gtk_style_get_ythickness(int /*long*/ style);
public static final int gtk_style_get_ythickness(int /*long*/ style) {
	lock.lock();
	try {
		return _gtk_style_get_ythickness(style);
	} finally {
		lock.unlock();
	}
}
/**
 * @param style cast=(GtkStyle *)
 * @param source cast=(GtkIconSource *)
 * @param widget cast=(GtkWidget *)
 * @param detail cast=(const gchar *)
 */
public static final native int /*long*/ _gtk_style_render_icon(int /*long*/ style, int /*long*/ source, int direction, int state, int size, int /*long*/ widget, byte[] detail);
public static final int /*long*/ gtk_style_render_icon(int /*long*/ style, int /*long*/ source, int direction, int state, int size, int /*long*/ widget, byte[] detail) {
	lock.lock();
	try {
		return _gtk_style_render_icon(style, source, direction, state, size, widget, detail);
	} finally {
		lock.unlock();
	}
}
/**
 * @param targets cast=(const GtkTargetEntry *)
 * @param ntargets cast=(guint)
 */
public static final native int /*long*/ _gtk_target_list_new(int /*long*/ targets, int ntargets);
public static final int /*long*/ gtk_target_list_new(int /*long*/ targets, int ntargets) {
	lock.lock();
	try {
		return _gtk_target_list_new(targets, ntargets);
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(GtkTargetList *) */
public static final native void _gtk_target_list_unref(int /*long*/ list);
public static final void gtk_target_list_unref(int /*long*/ list) {
	lock.lock();
	try {
		_gtk_target_list_unref(list);
	} finally {
		lock.unlock();
	}
}
/**
 * @param buffer cast=(GtkTextBuffer *)
 * @param clipboard cast=(GtkClipboard *)
 */
public static final native void _gtk_text_buffer_copy_clipboard(int /*long*/ buffer, int /*long*/ clipboard);
public static final void gtk_text_buffer_copy_clipboard(int /*long*/ buffer, int /*long*/ clipboard) {
	lock.lock();
	try {
		_gtk_text_buffer_copy_clipboard(buffer, clipboard);
	} finally {
		lock.unlock();
	}
}
/**
 * @param buffer cast=(GtkTextBuffer *)
 * @param clipboard cast=(GtkClipboard *)
 * @param default_editable cast=(gboolean)
 */
public static final native void _gtk_text_buffer_cut_clipboard(int /*long*/ buffer, int /*long*/ clipboard, boolean default_editable);
public static final void gtk_text_buffer_cut_clipboard(int /*long*/ buffer, int /*long*/ clipboard, boolean default_editable) {
	lock.lock();
	try {
		_gtk_text_buffer_cut_clipboard(buffer, clipboard, default_editable);
	} finally {
		lock.unlock();
	}
}
/**
 * @param buffer cast=(GtkTextBuffer *)
 * @param start cast=(GtkTextIter *)
 * @param end cast=(GtkTextIter *)
 */
public static final native void _gtk_text_buffer_delete(int /*long*/ buffer, byte[] start, byte[] end);
public static final void gtk_text_buffer_delete(int /*long*/ buffer, byte[] start, byte[] end) {
	lock.lock();
	try {
		_gtk_text_buffer_delete(buffer, start, end);
	} finally {
		lock.unlock();
	}
}
/**
 * @param buffer cast=(GtkTextBuffer *)
 * @param start cast=(GtkTextIter *)
 * @param end cast=(GtkTextIter *)
 */
public static final native void _gtk_text_buffer_get_bounds(int /*long*/ buffer, byte[] start, byte[] end);
public static final void gtk_text_buffer_get_bounds(int /*long*/ buffer, byte[] start, byte[] end) {
	lock.lock();
	try {
		_gtk_text_buffer_get_bounds(buffer, start, end);
	} finally {
		lock.unlock();
	}
}
/** @param buffer cast=(GtkTextBuffer *) */
public static final native int _gtk_text_buffer_get_char_count(int /*long*/ buffer);
public static final int gtk_text_buffer_get_char_count(int /*long*/ buffer) {
	lock.lock();
	try {
		return _gtk_text_buffer_get_char_count(buffer);
	} finally {
		lock.unlock();
	}
}
/**
 * @param buffer cast=(GtkTextBuffer *)
 * @param iter cast=(GtkTextIter *)
 */
public static final native void _gtk_text_buffer_get_end_iter(int /*long*/ buffer, byte[] iter);
public static final void gtk_text_buffer_get_end_iter(int /*long*/ buffer, byte[] iter) {
	lock.lock();
	try {
		_gtk_text_buffer_get_end_iter(buffer, iter);
	} finally {
		lock.unlock();
	}
}
/** @param buffer cast=(GtkTextBuffer *) */
public static final native int /*long*/ _gtk_text_buffer_get_insert(int /*long*/ buffer);
public static final int /*long*/ gtk_text_buffer_get_insert(int /*long*/ buffer) {
	lock.lock();
	try {
		return _gtk_text_buffer_get_insert(buffer);
	} finally {
		lock.unlock();
	}
}
/**
 * @param buffer cast=(GtkTextBuffer *)
 * @param iter cast=(GtkTextIter *)
 * @param line_number cast=(gint)
 */
public static final native void _gtk_text_buffer_get_iter_at_line(int /*long*/ buffer, byte[] iter, int line_number);
public static final void gtk_text_buffer_get_iter_at_line(int /*long*/ buffer, byte[] iter, int line_number) {
	lock.lock();
	try {
		_gtk_text_buffer_get_iter_at_line(buffer, iter, line_number);
	} finally {
		lock.unlock();
	}
}
/**
 * @param buffer cast=(GtkTextBuffer *)
 * @param iter cast=(GtkTextIter *)
 * @param mark cast=(GtkTextMark *)
 */
public static final native void _gtk_text_buffer_get_iter_at_mark(int /*long*/ buffer, byte[] iter, int /*long*/ mark);
public static final void gtk_text_buffer_get_iter_at_mark(int /*long*/ buffer, byte[] iter, int /*long*/ mark) {
	lock.lock();
	try {
		_gtk_text_buffer_get_iter_at_mark(buffer, iter, mark);
	} finally {
		lock.unlock();
	}
}
/**
 * @param buffer cast=(GtkTextBuffer *)
 * @param iter cast=(GtkTextIter *)
 * @param char_offset cast=(gint)
 */
public static final native void _gtk_text_buffer_get_iter_at_offset(int /*long*/ buffer, byte[] iter, int char_offset);
public static final void gtk_text_buffer_get_iter_at_offset(int /*long*/ buffer, byte[] iter, int char_offset) {
	lock.lock();
	try {
		_gtk_text_buffer_get_iter_at_offset(buffer, iter, char_offset);
	} finally {
		lock.unlock();
	}
}
/** @param buffer cast=(GtkTextBuffer *) */
public static final native int _gtk_text_buffer_get_line_count(int /*long*/ buffer);
public static final int gtk_text_buffer_get_line_count(int /*long*/ buffer) {
	lock.lock();
	try {
		return _gtk_text_buffer_get_line_count(buffer);
	} finally {
		lock.unlock();
	}
}
/** @param buffer cast=(GtkTextBuffer *) */
public static final native int /*long*/ _gtk_text_buffer_get_selection_bound(int /*long*/ buffer);
public static final int /*long*/ gtk_text_buffer_get_selection_bound(int /*long*/ buffer) {
	lock.lock();
	try {
		return _gtk_text_buffer_get_selection_bound(buffer);
	} finally {
		lock.unlock();
	}
}
/**
 * @param buffer cast=(GtkTextBuffer *)
 * @param start cast=(GtkTextIter *)
 * @param end cast=(GtkTextIter *)
 */
public static final native boolean _gtk_text_buffer_get_selection_bounds(int /*long*/ buffer, byte[] start, byte[] end);
public static final boolean gtk_text_buffer_get_selection_bounds(int /*long*/ buffer, byte[] start, byte[] end) {
	lock.lock();
	try {
		return _gtk_text_buffer_get_selection_bounds(buffer, start, end);
	} finally {
		lock.unlock();
	}
}
/**
 * @param buffer cast=(GtkTextBuffer *)
 * @param start cast=(GtkTextIter *)
 * @param end cast=(GtkTextIter *)
 * @param include_hidden_chars cast=(gboolean)
 */
public static final native int /*long*/ _gtk_text_buffer_get_text(int /*long*/ buffer, byte[] start, byte[] end, boolean include_hidden_chars);
public static final int /*long*/ gtk_text_buffer_get_text(int /*long*/ buffer, byte[] start, byte[] end, boolean include_hidden_chars) {
	lock.lock();
	try {
		return _gtk_text_buffer_get_text(buffer, start, end, include_hidden_chars);
	} finally {
		lock.unlock();
	}
}
/**
 * @param buffer cast=(GtkTextBuffer *)
 * @param iter cast=(GtkTextIter *)
 * @param text cast=(const gchar *)
 * @param len cast=(gint)
 */
public static final native void _gtk_text_buffer_insert(int /*long*/ buffer, byte[] iter, byte[] text, int len);
public static final void gtk_text_buffer_insert(int /*long*/ buffer, byte[] iter, byte[] text, int len) {
	lock.lock();
	try {
		_gtk_text_buffer_insert(buffer, iter, text, len);
	} finally {
		lock.unlock();
	}
}
/**
 * @param buffer cast=(GtkTextBuffer *)
 * @param iter cast=(GtkTextIter *)
 * @param text cast=(const gchar *)
 * @param len cast=(gint)
 */
public static final native void _gtk_text_buffer_insert(int /*long*/ buffer, int /*long*/ iter, byte[] text, int len);
public static final void gtk_text_buffer_insert(int /*long*/ buffer, int /*long*/ iter, byte[] text, int len) {
	lock.lock();
	try {
		_gtk_text_buffer_insert(buffer, iter, text, len);
	} finally {
		lock.unlock();
	}
}
/**
 * @param buffer cast=(GtkTextBuffer *)
 * @param mark cast=(GtkTextMark *)
 * @param where cast=(const GtkTextIter *)
 */
public static final native void _gtk_text_buffer_move_mark(int /*long*/ buffer, int /*long*/ mark, byte[] where);
public static final void gtk_text_buffer_move_mark(int /*long*/ buffer, int /*long*/ mark, byte[] where) {
	lock.lock();
	try {
		_gtk_text_buffer_move_mark(buffer, mark, where);
	} finally {
		lock.unlock();
	}
}
/**
 * @param buffer cast=(GtkTextBuffer *)
 * @param clipboard cast=(GtkClipboard *)
 * @param override_location cast=(GtkTextIter *)
 * @param default_editable cast=(gboolean)
 */
public static final native void _gtk_text_buffer_paste_clipboard(int /*long*/ buffer, int /*long*/ clipboard, byte[] override_location, boolean default_editable);
public static final void gtk_text_buffer_paste_clipboard(int /*long*/ buffer, int /*long*/ clipboard, byte[] override_location, boolean default_editable) {
	lock.lock();
	try {
		_gtk_text_buffer_paste_clipboard(buffer, clipboard, override_location, default_editable);
	} finally {
		lock.unlock();
	}
}
/**
 * @param buffer cast=(GtkTextBuffer *)
 * @param where cast=(const GtkTextIter *)
 */
public static final native void _gtk_text_buffer_place_cursor(int /*long*/ buffer, byte[] where);
public static final void gtk_text_buffer_place_cursor(int /*long*/ buffer, byte[] where) {
	lock.lock();
	try {
		_gtk_text_buffer_place_cursor(buffer, where);
	} finally {
		lock.unlock();
	}
}
/**
 * @param buffer cast=(GtkTextBuffer *)
 * @param text cast=(const gchar *)
 * @param len cast=(gint)
 */
public static final native void _gtk_text_buffer_set_text(int /*long*/ buffer, byte[] text, int len);
public static final void gtk_text_buffer_set_text(int /*long*/ buffer, byte[] text, int len) {
	lock.lock();
	try {
		_gtk_text_buffer_set_text(buffer, text, len);
	} finally {
		lock.unlock();
	}
}
/** @param iter cast=(const GtkTextIter *) */
public static final native int _gtk_text_iter_get_line(byte[] iter);
public static final int gtk_text_iter_get_line(byte[] iter) {
	lock.lock();
	try {
		return _gtk_text_iter_get_line(iter);
	} finally {
		lock.unlock();
	}
}
/** @param iter cast=(const GtkTextIter *) */
public static final native int _gtk_text_iter_get_offset(byte[] iter);
public static final int gtk_text_iter_get_offset(byte[] iter) {
	lock.lock();
	try {
		return _gtk_text_iter_get_offset(iter);
	} finally {
		lock.unlock();
	}
}
/**
 * @param text_view cast=(GtkTextView *)
 * @param win cast=(GtkTextWindowType)
 * @param buffer_x cast=(gint)
 * @param buffer_y cast=(gint)
 * @param window_x cast=(gint *)
 * @param window_y cast=(gint *)
 */
public static final native void _gtk_text_view_buffer_to_window_coords(int /*long*/ text_view, int win, int buffer_x, int buffer_y, int[] window_x, int[] window_y);
public static final void gtk_text_view_buffer_to_window_coords(int /*long*/ text_view, int win, int buffer_x, int buffer_y, int[] window_x, int[] window_y) {
	lock.lock();
	try {
		_gtk_text_view_buffer_to_window_coords(text_view, win, buffer_x, buffer_y, window_x, window_y);
	} finally {
		lock.unlock();
	}
}
/** @param text_view cast=(GtkTextView *) */
public static final native int /*long*/ _gtk_text_view_get_buffer(int /*long*/ text_view);
public static final int /*long*/ gtk_text_view_get_buffer(int /*long*/ text_view) {
	lock.lock();
	try {
		return _gtk_text_view_get_buffer(text_view);
	} finally {
		lock.unlock();
	}
}
/** @param text_view cast=(GtkTextView *) */
public static final native boolean _gtk_text_view_get_editable(int /*long*/ text_view);
public static final boolean gtk_text_view_get_editable(int /*long*/ text_view) {
	lock.lock();
	try {
		return _gtk_text_view_get_editable(text_view);
	} finally {
		lock.unlock();
	}
}
/**
 * @param text_view cast=(GtkTextView *)
 * @param iter cast=(GtkTextIter *)
 * @param x cast=(gint)
 * @param y cast=(gint)
 */
public static final native void _gtk_text_view_get_iter_at_location(int /*long*/ text_view, byte[] iter, int x, int y);
public static final void gtk_text_view_get_iter_at_location(int /*long*/ text_view, byte[] iter, int x, int y) {
	lock.lock();
	try {
		_gtk_text_view_get_iter_at_location(text_view, iter, x, y);
	} finally {
		lock.unlock();
	}
}
/**
 * @param text_view cast=(GtkTextView *)
 * @param iter cast=(const GtkTextIter *)
 * @param location cast=(GdkRectangle *),flags=no_in
 */
public static final native void _gtk_text_view_get_iter_location(int /*long*/ text_view, byte[] iter, GdkRectangle location);
public static final void gtk_text_view_get_iter_location(int /*long*/ text_view, byte[] iter, GdkRectangle location) {
	lock.lock();
	try {
		_gtk_text_view_get_iter_location(text_view, iter, location);
	} finally {
		lock.unlock();
	}
}
/**
 * @param text_view cast=(GtkTextView *)
 * @param target_iter cast=(GtkTextIter *)
 * @param y cast=(gint)
 * @param line_top cast=(gint *)
 */
public static final native void _gtk_text_view_get_line_at_y(int /*long*/ text_view, byte[] target_iter, int y, int[] line_top);
public static final void gtk_text_view_get_line_at_y(int /*long*/ text_view, byte[] target_iter, int y, int[] line_top) {
	lock.lock();
	try {
		_gtk_text_view_get_line_at_y(text_view, target_iter, y, line_top);
	} finally {
		lock.unlock();
	}
}
/**
 * @param text_view cast=(GtkTextView *)
 * @param visible_rect cast=(GdkRectangle *),flags=no_in
 */
public static final native void _gtk_text_view_get_visible_rect(int /*long*/ text_view, GdkRectangle visible_rect);
public static final void gtk_text_view_get_visible_rect(int /*long*/ text_view, GdkRectangle visible_rect) {
	lock.lock();
	try {
		_gtk_text_view_get_visible_rect(text_view, visible_rect);
	} finally {
		lock.unlock();
	}
}
/**
 * @param text_view cast=(GtkTextView *)
 * @param win cast=(GtkTextWindowType)
 */
public static final native int /*long*/ _gtk_text_view_get_window(int /*long*/ text_view, int win);
public static final int /*long*/ gtk_text_view_get_window(int /*long*/ text_view, int win) {
	lock.lock();
	try {
		return _gtk_text_view_get_window(text_view, win);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gtk_text_view_new();
public static final int /*long*/ gtk_text_view_new() {
	lock.lock();
	try {
		return _gtk_text_view_new();
	} finally {
		lock.unlock();
	}
}
/**
 * @param text_view cast=(GtkTextView *)
 * @param mark cast=(GtkTextMark *)
 */
public static final native void _gtk_text_view_scroll_mark_onscreen(int /*long*/ text_view, int /*long*/ mark);
public static final void gtk_text_view_scroll_mark_onscreen(int /*long*/ text_view, int /*long*/ mark) {
	lock.lock();
	try {
		_gtk_text_view_scroll_mark_onscreen(text_view, mark);
	} finally {
		lock.unlock();
	}
}
/**
 * @param text_view cast=(GtkTextView *)
 * @param iter cast=(GtkTextIter *)
 * @param within_margin cast=(gdouble)
 * @param use_align cast=(gboolean)
 * @param xalign cast=(gdouble)
 * @param yalign cast=(gdouble)
 */
public static final native boolean _gtk_text_view_scroll_to_iter(int /*long*/ text_view, byte[] iter, double within_margin, boolean use_align, double xalign, double yalign);
public static final boolean gtk_text_view_scroll_to_iter(int /*long*/ text_view, byte[] iter, double within_margin, boolean use_align, double xalign, double yalign) {
	lock.lock();
	try {
		return _gtk_text_view_scroll_to_iter(text_view, iter, within_margin, use_align, xalign, yalign);
	} finally {
		lock.unlock();
	}
}
/**
 * @param text_view cast=(GtkTextView *)
 * @param setting cast=(gboolean)
 */
public static final native void _gtk_text_view_set_editable(int /*long*/ text_view, boolean setting);
public static final void gtk_text_view_set_editable(int /*long*/ text_view, boolean setting) {
	lock.lock();
	try {
		_gtk_text_view_set_editable(text_view, setting);
	} finally {
		lock.unlock();
	}
}
/** @param text_view cast=(GtkTextView *) */
public static final native void _gtk_text_view_set_justification(int /*long*/ text_view, int justification);
public static final void gtk_text_view_set_justification(int /*long*/ text_view, int justification) {
	lock.lock();
	try {
		_gtk_text_view_set_justification(text_view, justification);
	} finally {
		lock.unlock();
	}
}
/**
 * @param text_view cast=(GtkTextView *)
 * @param tabs cast=(PangoTabArray *)
 */
public static final native void _gtk_text_view_set_tabs(int /*long*/ text_view, int /*long*/ tabs);
public static final void gtk_text_view_set_tabs(int /*long*/ text_view, int /*long*/ tabs) {
	lock.lock();
	try {
		_gtk_text_view_set_tabs(text_view, tabs);
	} finally {
		lock.unlock();
	}
}
/** @param text_view cast=(GtkTextView *) */
public static final native void _gtk_text_view_set_wrap_mode(int /*long*/ text_view, int wrap_mode);
public static final void gtk_text_view_set_wrap_mode(int /*long*/ text_view, int wrap_mode) {
	lock.lock();
	try {
		_gtk_text_view_set_wrap_mode(text_view, wrap_mode);
	} finally {
		lock.unlock();
	}
}
/**
 * @param text_view cast=(GtkTextView *)
 * @param win cast=(GtkTextWindowType)
 * @param window_x cast=(gint)
 * @param window_y cast=(gint)
 * @param buffer_x cast=(gint *)
 * @param buffer_y cast=(gint *)
 */
public static final native void _gtk_text_view_window_to_buffer_coords(int /*long*/ text_view, int win, int window_x, int window_y, int[] buffer_x, int[] buffer_y);
public static final void gtk_text_view_window_to_buffer_coords(int /*long*/ text_view,  int win, int window_x, int window_y, int[] buffer_x, int[] buffer_y) {
	lock.lock();
	try {
		_gtk_text_view_window_to_buffer_coords(text_view, win, window_x, window_y, buffer_x, buffer_y);
	} finally {
		lock.unlock();
	}
}
/**
 * @param interval cast=(guint32)
 * @param function cast=(GtkFunction)
 * @param data cast=(gpointer)
 */
public static final native int _gtk_timeout_add(int interval, int /*long*/ function, int /*long*/ data);
public static final int gtk_timeout_add(int interval, int /*long*/ function, int /*long*/ data) {
	lock.lock();
	try {
		return _gtk_timeout_add(interval, function, data);
	} finally {
		lock.unlock();
	}
}
/** @param timeout_handler_id cast=(guint) */
public static final native void _gtk_timeout_remove(int timeout_handler_id);
public static final void gtk_timeout_remove(int timeout_handler_id) {
	lock.lock();
	try {
		_gtk_timeout_remove(timeout_handler_id);
	} finally {
		lock.unlock();
	}
}
/** @param toggle_button cast=(GtkToggleButton *) */
public static final native boolean _gtk_toggle_button_get_active(int /*long*/ toggle_button);
public static final boolean gtk_toggle_button_get_active(int /*long*/ toggle_button) {
	lock.lock();
	try {
		return _gtk_toggle_button_get_active(toggle_button);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gtk_toggle_button_new();
public static final int /*long*/ gtk_toggle_button_new() {
	lock.lock();
	try {
		return _gtk_toggle_button_new();
	} finally {
		lock.unlock();
	}
}
/** @param toggle_button cast=(GtkToggleButton *) */
public static final native boolean _gtk_toggle_button_get_inconsistent(int /*long*/ toggle_button);
public static final boolean gtk_toggle_button_get_inconsistent(int /*long*/ toggle_button) {
	lock.lock();
	try {
		return _gtk_toggle_button_get_inconsistent(toggle_button);
	} finally {
		lock.unlock();
	}
}
/**
 * @param toggle_button cast=(GtkToggleButton *)
 * @param is_active cast=(gboolean)
 */
public static final native void _gtk_toggle_button_set_active(int /*long*/ toggle_button, boolean is_active);
public static final void gtk_toggle_button_set_active(int /*long*/ toggle_button, boolean is_active) {
	lock.lock();
	try {
		_gtk_toggle_button_set_active(toggle_button, is_active);
	} finally {
		lock.unlock();
	}
}
/**
 * @param toggle_button cast=(GtkToggleButton *)
 * @param setting cast=(gboolean)
 */
public static final native void _gtk_toggle_button_set_inconsistent(int /*long*/ toggle_button, boolean setting);
public static final void gtk_toggle_button_set_inconsistent(int /*long*/ toggle_button, boolean setting) {
	lock.lock();
	try {
		_gtk_toggle_button_set_inconsistent(toggle_button, setting);
	} finally {
		lock.unlock();
	}
}
/**
 * @param toggle_button cast=(GtkToggleButton *)
 * @param draw_indicator cast=(gboolean)
 */
public static final native void _gtk_toggle_button_set_mode(int /*long*/ toggle_button, boolean draw_indicator);
public static final void gtk_toggle_button_set_mode(int /*long*/ toggle_button, boolean draw_indicator) {
	lock.lock();
	try {
		_gtk_toggle_button_set_mode(toggle_button, draw_indicator);
	} finally {
		lock.unlock();
	}
}
/**
 * @param toolbar cast=(GtkToolbar *)
 * @param widget cast=(GtkWidget *)
 * @param tooltip_text cast=(const char *)
 * @param tooltip_private_text cast=(const char *)
 * @param position cast=(gint)
 */
public static final native void _gtk_toolbar_insert_widget(int /*long*/ toolbar, int /*long*/ widget, byte[] tooltip_text, byte[] tooltip_private_text, int position);
public static final void gtk_toolbar_insert_widget(int /*long*/ toolbar, int /*long*/ widget, byte[] tooltip_text, byte[] tooltip_private_text, int position) {
	lock.lock();
	try {
		_gtk_toolbar_insert_widget(toolbar, widget, tooltip_text, tooltip_private_text, position);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gtk_toolbar_new();
public static final int /*long*/ gtk_toolbar_new() {
	lock.lock();
	try {
		return _gtk_toolbar_new();
	} finally {
		lock.unlock();
	}
}
/**
 * @param toolbar cast=(GtkToolbar *)
 * @param orientation cast=(GtkOrientation)
 */
public static final native void _gtk_toolbar_set_orientation(int /*long*/ toolbar, int orientation);
public static final void gtk_toolbar_set_orientation(int /*long*/ toolbar, int orientation) {
	lock.lock();
	try {
		_gtk_toolbar_set_orientation(toolbar, orientation);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(GtkWidget *) */
public static final native int /*long*/ _gtk_tooltips_data_get(int /*long*/ widget);
public static final int /*long*/ gtk_tooltips_data_get(int /*long*/ widget) {
	lock.lock();
	try {
		return _gtk_tooltips_data_get(widget);
	} finally {
		lock.unlock();
	}
}
/** @param tooltips cast=(GtkTooltips *) */
public static final native void _gtk_tooltips_disable(int /*long*/ tooltips);
public static final void gtk_tooltips_disable(int /*long*/ tooltips) {
	lock.lock();
	try {
		_gtk_tooltips_disable(tooltips);
	} finally {
		lock.unlock();
	}
}
/** @param tooltips cast=(GtkTooltips *) */
public static final native void _gtk_tooltips_enable(int /*long*/ tooltips);
public static final void gtk_tooltips_enable(int /*long*/ tooltips) {
	lock.lock();
	try {
		_gtk_tooltips_enable(tooltips);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gtk_tooltips_new();
public static final int /*long*/ gtk_tooltips_new() {
	lock.lock();
	try {
		return _gtk_tooltips_new();
	} finally {
		lock.unlock();
	}
}
/** @param tooltips cast=(GtkTooltips *) */
public static final native void _gtk_tooltips_force_window(int /*long*/ tooltips);
public static final void gtk_tooltips_force_window(int /*long*/ tooltips) {
	lock.lock();
	try {
		_gtk_tooltips_force_window(tooltips);
	} finally {
		lock.unlock();
	}
}
/**
 * @param tooltips cast=(GtkTooltips *)
 * @param widget cast=(GtkWidget *)
 * @param tip_text cast=(const gchar *)
 * @param tip_private cast=(const gchar *)
 */
public static final native void _gtk_tooltips_set_tip(int /*long*/ tooltips, int /*long*/ widget, byte[] tip_text, byte[] tip_private);
public static final void gtk_tooltips_set_tip(int /*long*/ tooltips, int /*long*/ widget, byte[] tip_text, byte[] tip_private) {
	lock.lock();
	try {
		_gtk_tooltips_set_tip(tooltips, widget, tip_text, tip_private);
	} finally {
		lock.unlock();
	}
}
/**
 * @param tree_model cast=(GtkTreeModel *)
 * @param iter cast=(GtkTreeIter *)
 */
public static final native void _gtk_tree_model_get(int /*long*/ tree_model, int /*long*/ iter, int column, int[] value, int /*long*/ terminator);
public static final void gtk_tree_model_get(int /*long*/ tree_model, int /*long*/ iter, int column, int[] value, int /*long*/ terminator) {
	lock.lock();
	try {
		_gtk_tree_model_get(tree_model, iter, column, value, terminator);
	} finally {
		lock.unlock();
	}
}
/**
 * @param tree_model cast=(GtkTreeModel *)
 * @param iter cast=(GtkTreeIter *)
 */
public static final native void _gtk_tree_model_get(int /*long*/ tree_model, int /*long*/ iter, int column, long[] value, int /*long*/ terminator);
public static final void gtk_tree_model_get(int /*long*/ tree_model, int /*long*/ iter, int column, long[] value, int /*long*/ terminator) {
	lock.lock();
	try {
		_gtk_tree_model_get(tree_model, iter, column, value, terminator);
	} finally {
		lock.unlock();
	}
}
/**
 * @param tree_model cast=(GtkTreeModel *)
 * @param iter cast=(GtkTreeIter *)
 * @param path cast=(GtkTreePath *)
 */
public static final native boolean _gtk_tree_model_get_iter(int /*long*/ tree_model, int /*long*/ iter, int /*long*/ path);
public static final boolean gtk_tree_model_get_iter(int /*long*/ tree_model, int /*long*/ iter, int /*long*/ path) {
	lock.lock();
	try {
		return _gtk_tree_model_get_iter(tree_model, iter, path);
	} finally {
		lock.unlock();
	}
}
/**
 * @param tree_model cast=(GtkTreeModel *)
 * @param iter cast=(GtkTreeIter *)
 */
public static final native boolean _gtk_tree_model_get_iter_first(int /*long*/ tree_model, int /*long*/ iter);
public static final boolean gtk_tree_model_get_iter_first(int /*long*/ tree_model, int /*long*/ iter) {
	lock.lock();
	try {
		return _gtk_tree_model_get_iter_first(tree_model, iter);
	} finally {
		lock.unlock();
	}
}
/** @param tree_model cast=(GtkTreeModel *) */
public static final native int _gtk_tree_model_get_n_columns(int /*long*/ tree_model);
public static final int gtk_tree_model_get_n_columns(int /*long*/ tree_model) {
	lock.lock();
	try {
		return _gtk_tree_model_get_n_columns(tree_model);
	} finally {
		lock.unlock();
	}
}
/**
 * @param tree_model cast=(GtkTreeModel *)
 * @param iter cast=(GtkTreeIter *)
 */
public static final native int /*long*/ _gtk_tree_model_get_path(int /*long*/ tree_model, int /*long*/ iter);
public static final int /*long*/ gtk_tree_model_get_path(int /*long*/ tree_model, int /*long*/ iter) {
	lock.lock();
	try {
		return _gtk_tree_model_get_path(tree_model, iter);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gtk_tree_model_get_type();
public static final int /*long*/ gtk_tree_model_get_type() {
	lock.lock();
	try {
		return _gtk_tree_model_get_type();
	} finally {
		lock.unlock();
	}
}
/**
 * @param model cast=(GtkTreeModel *)
 * @param iter cast=(GtkTreeIter *)
 * @param parent cast=(GtkTreeIter *)
 */
public static final native boolean _gtk_tree_model_iter_children(int /*long*/ model, int /*long*/ iter, int /*long*/ parent);
public static final boolean gtk_tree_model_iter_children(int /*long*/ model, int /*long*/ iter, int /*long*/ parent) {
	lock.lock();
	try {
		return _gtk_tree_model_iter_children(model, iter, parent);
	} finally {
		lock.unlock();
	}
}
/**
 * @param model cast=(GtkTreeModel *)
 * @param iter cast=(GtkTreeIter *)
 */
public static final native int _gtk_tree_model_iter_n_children(int /*long*/ model, int /*long*/ iter);
public static final int gtk_tree_model_iter_n_children(int /*long*/ model, int /*long*/ iter) {
	lock.lock();
	try {
		return _gtk_tree_model_iter_n_children(model, iter);
	} finally {
		lock.unlock();
	}
}
/**
 * @param model cast=(GtkTreeModel *)
 * @param iter cast=(GtkTreeIter *)
 */
public static final native boolean _gtk_tree_model_iter_next(int /*long*/ model, int /*long*/ iter);
public static final boolean gtk_tree_model_iter_next(int /*long*/ model, int /*long*/ iter) {
	lock.lock();
	try {
		return _gtk_tree_model_iter_next(model, iter);
	} finally {
		lock.unlock();
	}
}
/**
 * @param tree_model cast=(GtkTreeModel *)
 * @param iter cast=(GtkTreeIter *)
 * @param parent cast=(GtkTreeIter *)
 */
public static final native boolean _gtk_tree_model_iter_nth_child(int /*long*/ tree_model, int /*long*/ iter, int /*long*/ parent, int n);
public static final boolean gtk_tree_model_iter_nth_child(int /*long*/ tree_model, int /*long*/ iter, int /*long*/ parent, int n) {
	lock.lock();
	try {
		return _gtk_tree_model_iter_nth_child(tree_model, iter, parent, n);
	} finally {
		lock.unlock();
	}
}
/** @param path cast=(GtkTreePath *) */
public static final native void _gtk_tree_path_append_index(int /*long*/ path, int index);
public static final void gtk_tree_path_append_index(int /*long*/ path, int index) {
	lock.lock();
	try {
		_gtk_tree_path_append_index(path, index);
	} finally {
		lock.unlock();
	}
}
/**
 * @param a cast=(const GtkTreePath *)
 * @param b cast=(const GtkTreePath *)
 */
public static final native int /*long*/ _gtk_tree_path_compare(int /*long*/ a, int /*long*/ b);
public static final int /*long*/ gtk_tree_path_compare(int /*long*/ a, int /*long*/ b) {
	lock.lock();
	try {
		 return _gtk_tree_path_compare(a, b);
	} finally {
		lock.unlock();
	}
}
/** @param path cast=(GtkTreePath *) */
public static final native void _gtk_tree_path_down(int /*long*/ path);
public static final void gtk_tree_path_down(int /*long*/ path) {
	lock.lock();
	try {
		 _gtk_tree_path_down(path);
	} finally {
		lock.unlock();
	}
}
/** @param path cast=(GtkTreePath *) */
public static final native void _gtk_tree_path_free(int /*long*/ path);
public static final void gtk_tree_path_free(int /*long*/ path) {
	lock.lock();
	try {
		_gtk_tree_path_free(path);
	} finally {
		lock.unlock();
	}
}
/** @param path cast=(GtkTreePath *) */
public static final native int _gtk_tree_path_get_depth(int /*long*/ path);
public static final int gtk_tree_path_get_depth(int /*long*/ path) {
	lock.lock();
	try {
		return _gtk_tree_path_get_depth(path);
	} finally {
		lock.unlock();
	}
}
/** @param path cast=(GtkTreePath *) */
public static final native int /*long*/ _gtk_tree_path_get_indices(int /*long*/ path);
public static final int /*long*/ gtk_tree_path_get_indices(int /*long*/ path) {
	lock.lock();
	try {
		return _gtk_tree_path_get_indices(path);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gtk_tree_path_new();
public static final int /*long*/ gtk_tree_path_new() {
	lock.lock();
	try {
		return _gtk_tree_path_new();
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gtk_tree_path_new_first();
public static final int /*long*/ gtk_tree_path_new_first() {
	lock.lock();
	try {
		return _gtk_tree_path_new_first();
	} finally {
		lock.unlock();
	}
}
/** @param path cast=(const gchar *) */
public static final native int /*long*/ _gtk_tree_path_new_from_string(byte[] path);
public static final int /*long*/ gtk_tree_path_new_from_string(byte[] path) {
	lock.lock();
	try {
		return _gtk_tree_path_new_from_string(path);
	} finally {
		lock.unlock();
	}
}
/** @param path cast=(const gchar *) */
public static final native int /*long*/ _gtk_tree_path_new_from_string(int /*long*/ path);
public static final int /*long*/ gtk_tree_path_new_from_string(int /*long*/ path) {
	lock.lock();
	try {
		return _gtk_tree_path_new_from_string(path);
	} finally {
		lock.unlock();
	}
}
/** @param path cast=(GtkTreePath *) */
public static final native void _gtk_tree_path_next(int /*long*/ path);
public static final void gtk_tree_path_next(int /*long*/ path) {
	lock.lock();
	try {
		_gtk_tree_path_next(path);
	} finally {
		lock.unlock();
	}
}
/** @param path cast=(GtkTreePath *) */
public static final native boolean _gtk_tree_path_prev(int /*long*/ path);
public static final boolean gtk_tree_path_prev(int /*long*/ path) {
	lock.lock();
	try {
		return _gtk_tree_path_prev(path);
	} finally {
		lock.unlock();
	}
}
/** @param path cast=(GtkTreePath *) */
public static final native boolean _gtk_tree_path_up(int /*long*/ path);
public static final boolean gtk_tree_path_up(int /*long*/ path) {
	lock.lock();
	try {
		return _gtk_tree_path_up(path);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param selection cast=(GtkTreeSelection *)
 */
public static final native int _gtk_tree_selection_count_selected_rows(int /*long*/ selection);
public static final int gtk_tree_selection_count_selected_rows(int /*long*/ selection) {
	lock.lock();
	try {
		return _gtk_tree_selection_count_selected_rows(selection);
	} finally {
		lock.unlock();
	}
}
/**
 * @param selection cast=(GtkTreeSelection *)
 * @param model cast=(GtkTreeModel **)
 * @param iter cast=(GtkTreeIter *)
 */
public static final native boolean _gtk_tree_selection_get_selected(int /*long*/ selection, int /*long*/[] model, int /*long*/ iter);
public static final boolean gtk_tree_selection_get_selected(int /*long*/ selection, int /*long*/[] model, int /*long*/ iter) {
	lock.lock();
	try {
		return _gtk_tree_selection_get_selected(selection, model, iter);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param selection cast=(GtkTreeSelection *)
 * @param model cast=(GtkTreeModel **)
 */
public static final native int /*long*/ _gtk_tree_selection_get_selected_rows(int /*long*/ selection, int /*long*/[] model);
public static final int /*long*/ gtk_tree_selection_get_selected_rows(int /*long*/ selection, int /*long*/[] model) {
	lock.lock();
	try {
		return _gtk_tree_selection_get_selected_rows(selection, model);
	} finally {
		lock.unlock();
	}
}
/**
 * @param selection cast=(GtkTreeSelection *)
 * @param path cast=(GtkTreePath *)
 */
public static final native boolean _gtk_tree_selection_path_is_selected(int /*long*/ selection, int /*long*/ path);
public static final boolean gtk_tree_selection_path_is_selected(int /*long*/ selection, int /*long*/ path) {
	lock.lock();
	try {
		return _gtk_tree_selection_path_is_selected(selection, path);
	} finally {
		lock.unlock();
	}
}
/** @param selection cast=(GtkTreeSelection *) */
public static final native void _gtk_tree_selection_select_all(int /*long*/ selection);
public static final void gtk_tree_selection_select_all(int /*long*/ selection) {
	lock.lock();
	try {
		_gtk_tree_selection_select_all(selection);
	} finally {
		lock.unlock();
	}
}
/**
 * @param selection cast=(GtkTreeSelection *)
 * @param iter cast=(GtkTreeIter *)
 */
public static final native void _gtk_tree_selection_select_iter(int /*long*/ selection, int /*long*/ iter);
public static final void gtk_tree_selection_select_iter(int /*long*/ selection, int /*long*/ iter) {
	lock.lock();
	try {
		_gtk_tree_selection_select_iter(selection, iter);
	} finally {
		lock.unlock();
	}
}
/**
 * @param selection cast=(GtkTreeSelection *)
 * @param func cast=(GtkTreeSelectionForeachFunc)
 * @param data cast=(gpointer)
 */
public static final native void _gtk_tree_selection_selected_foreach(int /*long*/ selection, int /*long*/ func, int /*long*/ data);
public static final void gtk_tree_selection_selected_foreach(int /*long*/ selection, int /*long*/ func, int /*long*/ data) {
	lock.lock();
	try {
		_gtk_tree_selection_selected_foreach(selection, func, data);
	} finally {
		lock.unlock();
	}
}
/**
 * @param selection cast=(GtkTreeSelection *)
 * @param mode cast=(GtkSelectionMode)
 */
public static final native void _gtk_tree_selection_set_mode(int /*long*/ selection, int mode);
public static final void gtk_tree_selection_set_mode(int /*long*/ selection, int mode) {
	lock.lock();
	try {
		_gtk_tree_selection_set_mode(selection, mode);
	} finally {
		lock.unlock();
	}
}
/** @param selection cast=(GtkTreeSelection *) */
public static final native void _gtk_tree_selection_unselect_all(int /*long*/ selection);
public static final void gtk_tree_selection_unselect_all(int /*long*/ selection) {
	lock.lock();
	try {
		_gtk_tree_selection_unselect_all(selection);
	} finally {
		lock.unlock();
	}
}
/**
 * @param selection cast=(GtkTreeSelection *)
 * @param iter cast=(GtkTreeIter *)
 */
public static final native void _gtk_tree_selection_unselect_iter(int /*long*/ selection, int /*long*/ iter);
public static final void gtk_tree_selection_unselect_iter(int /*long*/ selection, int /*long*/ iter) {
	lock.lock();
	try {
		_gtk_tree_selection_unselect_iter(selection, iter);
	} finally {
		lock.unlock();
	}
}
/**
 * @param store cast=(GtkTreeStore *)
 * @param iter cast=(GtkTreeIter *)
 * @param parent cast=(GtkTreeIter *)
 */
public static final native void _gtk_tree_store_append(int /*long*/ store, int /*long*/ iter, int /*long*/ parent);
public static final void gtk_tree_store_append(int /*long*/ store, int /*long*/ iter, int /*long*/ parent) {
	lock.lock();
	try {
		_gtk_tree_store_append(store, iter, parent);
	} finally {
		lock.unlock();
	}
}
/** @param store cast=(GtkTreeStore *) */
public static final native void _gtk_tree_store_clear(int /*long*/ store);
public static final void gtk_tree_store_clear(int /*long*/ store) {
	lock.lock();
	try {
		_gtk_tree_store_clear(store);
	} finally {
		lock.unlock();
	}
}
/**
 * @param store cast=(GtkTreeStore *)
 * @param iter cast=(GtkTreeIter *)
 * @param parent cast=(GtkTreeIter *)
 * @param position cast=(gint)
 */
public static final native void _gtk_tree_store_insert(int /*long*/ store, int /*long*/ iter, int /*long*/ parent, int position);
public static final void gtk_tree_store_insert(int /*long*/ store, int /*long*/ iter, int /*long*/ parent, int position) {
	lock.lock();
	try {
		_gtk_tree_store_insert(store, iter, parent, position);
	} finally {
		lock.unlock();
	}
}
/** @param types cast=(GType *) */
public static final native int /*long*/ _gtk_tree_store_newv(int numColumns, int /*long*/[] types);
public static final int /*long*/ gtk_tree_store_newv(int numColumns, int /*long*/[] types) {
	lock.lock();
	try {
		return _gtk_tree_store_newv(numColumns, types);
	} finally {
		lock.unlock();
	}
}
/**
 * @param store cast=(GtkTreeStore *)
 * @param iter cast=(GtkTreeIter *)
 */
public static final native void _gtk_tree_store_remove(int /*long*/ store, int /*long*/ iter);
public static final void gtk_tree_store_remove(int /*long*/ store, int /*long*/ iter) {
	lock.lock();
	try {
		_gtk_tree_store_remove(store, iter);
	} finally {
		lock.unlock();
	}
}
/**
 * @param store cast=(GtkTreeStore *)
 * @param iter cast=(GtkTreeIter *)
 */
public static final native void _gtk_tree_store_set(int /*long*/ store, int /*long*/ iter, int column, byte[] value, int /*long*/ terminator);
public static final void gtk_tree_store_set(int /*long*/ store, int /*long*/ iter, int column, byte[] value, int /*long*/ terminator) {
	lock.lock();
	try {
		_gtk_tree_store_set(store, iter, column, value, terminator);
	} finally {
		lock.unlock();
	}
}
/**
 * @param store cast=(GtkTreeStore *)
 * @param iter cast=(GtkTreeIter *)
 */
public static final native void _gtk_tree_store_set(int /*long*/ store, int /*long*/ iter, int column, int value, int /*long*/ terminator);
public static final void gtk_tree_store_set(int /*long*/ store, int /*long*/ iter, int column, int value, int /*long*/ terminator) {
	lock.lock();
	try {
		_gtk_tree_store_set(store, iter, column, value, terminator);
	} finally {
		lock.unlock();
	}
}
/**
 * @param store cast=(GtkTreeStore *)
 * @param iter cast=(GtkTreeIter *)
 */
public static final native void _gtk_tree_store_set(int /*long*/ store, int /*long*/ iter, int column, long value, int /*long*/ terminator);
public static final void gtk_tree_store_set(int /*long*/ store, int /*long*/ iter, int column, long value, int /*long*/ terminator) {
	lock.lock();
	try {
		_gtk_tree_store_set(store, iter, column, value, terminator);
	} finally {
		lock.unlock();
	}
}
/**
 * @param store cast=(GtkTreeStore *)
 * @param iter cast=(GtkTreeIter *)
 * @param value flags=no_out
 */
public static final native void _gtk_tree_store_set(int /*long*/ store, int /*long*/ iter, int column, GdkColor value, int /*long*/ terminator);
public static final void gtk_tree_store_set(int /*long*/ store, int /*long*/ iter, int column, GdkColor value, int /*long*/ terminator) {
	lock.lock();
	try {
		_gtk_tree_store_set(store, iter, column, value, terminator);
	} finally {
		lock.unlock();
	}
}
/**
 * @param store cast=(GtkTreeStore *)
 * @param iter cast=(GtkTreeIter *)
 */
public static final native void _gtk_tree_store_set(int /*long*/ store, int /*long*/ iter, int column, boolean value, int /*long*/ terminator);
public static final void gtk_tree_store_set(int /*long*/ store, int /*long*/ iter, int column, boolean value, int /*long*/ terminator) {
	lock.lock();
	try {
		_gtk_tree_store_set(store, iter, column, value, terminator);
	} finally {
		lock.unlock();
	}
}
/**
 * @param view cast=(GtkTreeView *)
 * @param path cast=(GtkTreePath *)
 */
public static final native int /*long*/ _gtk_tree_view_create_row_drag_icon(int /*long*/ view, int /*long*/ path);
public static final int /*long*/ gtk_tree_view_create_row_drag_icon(int /*long*/ view, int /*long*/ path) {
	lock.lock();
	try {
		return _gtk_tree_view_create_row_drag_icon(view, path);
	} finally {
		lock.unlock();
	}
}
/**
 * @param view cast=(GtkTreeView *)
 * @param path cast=(GtkTreePath *)
 */
public static final native boolean _gtk_tree_view_collapse_row(int /*long*/ view, int /*long*/ path);
public static final boolean gtk_tree_view_collapse_row(int /*long*/ view, int /*long*/ path) {
	lock.lock();
	try {
		return _gtk_tree_view_collapse_row(view, path);
	} finally {
		lock.unlock();
	}
}
/**
 * @param treeColumn cast=(GtkTreeViewColumn *)
 * @param cellRenderer cast=(GtkCellRenderer *)
 * @param attribute cast=(const gchar *)
 * @param column cast=(gint)
 */
public static final native void _gtk_tree_view_column_add_attribute(int /*long*/ treeColumn, int /*long*/ cellRenderer, byte[] attribute, int column);
public static final void gtk_tree_view_column_add_attribute(int /*long*/ treeColumn, int /*long*/ cellRenderer, byte[] attribute, int column) {
	lock.lock();
	try {
		_gtk_tree_view_column_add_attribute(treeColumn, cellRenderer, attribute, column);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param tree_column cast=(GtkTreeViewColumn *)
 * @param cell_renderer cast=(GtkCellRenderer *)
 * @param start_pos cast=(gint *)
 * @param width cast=(gint *)
 */
public static final native boolean _gtk_tree_view_column_cell_get_position(int /*long*/ tree_column, int /*long*/ cell_renderer, int[] start_pos, int[] width);
public static final boolean gtk_tree_view_column_cell_get_position(int /*long*/ tree_column, int /*long*/ cell_renderer, int[] start_pos, int[] width) {
	lock.lock();
	try {
		return _gtk_tree_view_column_cell_get_position(tree_column, cell_renderer, start_pos, width);
	} finally {
		lock.unlock();
	}
}
/**
 * @param tree_column cast=(GtkTreeViewColumn *)
 * @param cell_area cast=(GdkRectangle *),flags=no_in
 * @param x_offset cast=(gint *)
 * @param y_offset cast=(gint *)
 * @param width cast=(gint *)
 * @param height cast=(gint *)
 */
public static final native void _gtk_tree_view_column_cell_get_size(int /*long*/ tree_column, GdkRectangle cell_area, int[] x_offset, int[] y_offset, int[] width, int[] height);
public static final void gtk_tree_view_column_cell_get_size(int /*long*/ tree_column, GdkRectangle cell_area, int[] x_offset, int[] y_offset, int[] width, int[] height) {
	lock.lock();
	try {
		_gtk_tree_view_column_cell_get_size(tree_column, cell_area, x_offset, y_offset, width, height);
	} finally {
		lock.unlock();
	}
}
/**
 * @param tree_column cast=(GtkTreeViewColumn *)
 * @param tree_model cast=(GtkTreeModel *)
 * @param iter cast=(GtkTreeIter *)
 */
public static final native void _gtk_tree_view_column_cell_set_cell_data(int /*long*/ tree_column, int /*long*/ tree_model, int /*long*/ iter, boolean is_expander, boolean is_expanded);
public static final void gtk_tree_view_column_cell_set_cell_data(int /*long*/ tree_column, int /*long*/ tree_model, int /*long*/ iter, boolean is_expander, boolean is_expanded) {
	lock.lock();
	try {
		_gtk_tree_view_column_cell_set_cell_data(tree_column, tree_model, iter, is_expander, is_expanded);
	} finally {
		lock.unlock();
	}
}
/** @param tree_column cast=(GtkTreeViewColumn *) */
public static final native void _gtk_tree_view_column_clear(int /*long*/ tree_column);
public static final void gtk_tree_view_column_clear(int /*long*/ tree_column) {
	lock.lock();
	try {
		_gtk_tree_view_column_clear(tree_column);
	} finally {
		lock.unlock();
	}
}
/** @param tree_column cast=(GtkTreeViewColumn *) */
public static final native int /*long*/ _gtk_tree_view_column_get_cell_renderers(int /*long*/ tree_column);
public static final int /*long*/ gtk_tree_view_column_get_cell_renderers(int /*long*/ tree_column) {
	lock.lock();
	try {
		return _gtk_tree_view_column_get_cell_renderers(tree_column);
	} finally {
		lock.unlock();
	}
}
/** @param column cast=(GtkTreeViewColumn *) */
public static final native int _gtk_tree_view_column_get_fixed_width(int /*long*/ column);
public static final int gtk_tree_view_column_get_fixed_width(int /*long*/ column) {
	lock.lock();
	try {
		return _gtk_tree_view_column_get_fixed_width(column);
	} finally {
		lock.unlock();
	}
}
/** @param column cast=(GtkTreeViewColumn *) */
public static final native boolean _gtk_tree_view_column_get_reorderable(int /*long*/ column);
public static final boolean gtk_tree_view_column_get_reorderable(int /*long*/ column) {
	lock.lock();
	try {
		return _gtk_tree_view_column_get_reorderable(column);
	} finally {
		lock.unlock();
	}
}
/** @param column cast=(GtkTreeViewColumn *) */
public static final native boolean _gtk_tree_view_column_get_resizable(int /*long*/ column);
public static final boolean gtk_tree_view_column_get_resizable(int /*long*/ column) {
	lock.lock();
	try {
		return _gtk_tree_view_column_get_resizable(column);
	} finally {
		lock.unlock();
	}
}
/** @param tree_column cast=(GtkTreeViewColumn *) */
public static final native int _gtk_tree_view_column_get_sizing(int /*long*/ tree_column);
public static final int gtk_tree_view_column_get_sizing(int /*long*/ tree_column) {
	lock.lock();
	try {
		return _gtk_tree_view_column_get_sizing(tree_column);
	} finally {
		lock.unlock();
	}
}
/** @param tree_column cast=(GtkTreeViewColumn *) */
public static final native int _gtk_tree_view_column_get_spacing(int /*long*/ tree_column);
public static final int gtk_tree_view_column_get_spacing(int /*long*/ tree_column) {
	lock.lock();
	try {
		return _gtk_tree_view_column_get_spacing(tree_column);
	} finally {
		lock.unlock();
	}
}
/** @param column cast=(GtkTreeViewColumn *) */
public static final native boolean _gtk_tree_view_column_get_visible(int /*long*/ column);
public static final boolean gtk_tree_view_column_get_visible(int /*long*/ column) {
	lock.lock();
	try {
		return _gtk_tree_view_column_get_visible(column);
	} finally {
		lock.unlock();
	}
}
/** @param tree_column cast=(GtkTreeViewColumn *) */
public static final native boolean _gtk_tree_view_column_get_sort_indicator(int /*long*/ tree_column);
public static final boolean gtk_tree_view_column_get_sort_indicator(int /*long*/ tree_column) {
	lock.lock();
	try {
		return _gtk_tree_view_column_get_sort_indicator(tree_column);
	} finally {
		lock.unlock();
	}
}
/** @param tree_column cast=(GtkTreeViewColumn *) */
public static final native int _gtk_tree_view_column_get_sort_order(int /*long*/ tree_column);
public static final int gtk_tree_view_column_get_sort_order(int /*long*/ tree_column) {
	lock.lock();
	try {
		return _gtk_tree_view_column_get_sort_order(tree_column);
	} finally {
		lock.unlock();
	}
}
/** @param column cast=(GtkTreeViewColumn *) */
public static final native int _gtk_tree_view_column_get_width(int /*long*/ column);
public static final int gtk_tree_view_column_get_width(int /*long*/ column) {
	lock.lock();
	try {
		return _gtk_tree_view_column_get_width(column);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gtk_tree_view_column_new();
public static final int /*long*/ gtk_tree_view_column_new() {
	lock.lock();
	try {
		return _gtk_tree_view_column_new();
	} finally {
		lock.unlock();
	}
}
/**
 * @param tree_column cast=(GtkTreeViewColumn *)
 * @param cell_renderer cast=(GtkCellRenderer *)
 * @param expand cast=(gboolean)
 */
public static final native void _gtk_tree_view_column_pack_start(int /*long*/ tree_column, int /*long*/ cell_renderer, boolean expand);
public static final void gtk_tree_view_column_pack_start(int /*long*/ tree_column, int /*long*/ cell_renderer, boolean expand) {
	lock.lock();
	try {
		_gtk_tree_view_column_pack_start(tree_column, cell_renderer, expand);
	} finally {
		lock.unlock();
	}
}
/**
 * @param tree_column cast=(GtkTreeViewColumn *)
 * @param cell_renderer cast=(GtkCellRenderer *)
 * @param expand cast=(gboolean)
 */
public static final native void _gtk_tree_view_column_pack_end(int /*long*/ tree_column, int /*long*/ cell_renderer, boolean expand);
public static final void gtk_tree_view_column_pack_end(int /*long*/ tree_column, int /*long*/ cell_renderer, boolean expand) {
	lock.lock();
	try {
		_gtk_tree_view_column_pack_end(tree_column, cell_renderer, expand);
	} finally {
		lock.unlock();
	}
}
/** @param tree_column cast=(GtkTreeViewColumn *) */
public static final native void _gtk_tree_view_column_set_alignment(int /*long*/ tree_column, float xalign);
public static final void gtk_tree_view_column_set_alignment(int /*long*/ tree_column, float xalign) {
	lock.lock();
	try {
		_gtk_tree_view_column_set_alignment(tree_column, xalign);
	} finally {
		lock.unlock();
	}
}
/**
 * @param tree_column cast=(GtkTreeViewColumn *)
 * @param cell_renderer cast=(GtkCellRenderer *)
 * @param func cast=(GtkTreeCellDataFunc)
 * @param func_data cast=(gpointer)
 * @param destroy cast=(GtkDestroyNotify)
 */
public static final native void _gtk_tree_view_column_set_cell_data_func(int /*long*/ tree_column, int /*long*/ cell_renderer, int /*long*/ func, int /*long*/ func_data, int /*long*/ destroy);
public static final void gtk_tree_view_column_set_cell_data_func(int /*long*/ tree_column, int /*long*/ cell_renderer, int /*long*/ func, int /*long*/ func_data, int /*long*/ destroy) {
	lock.lock();
	try {
		_gtk_tree_view_column_set_cell_data_func(tree_column, cell_renderer, func, func_data, destroy);
	} finally {
		lock.unlock();
	}
}
/**
 * @param column cast=(GtkTreeViewColumn *)
 * @param clickable cast=(gboolean)
 */
public static final native void _gtk_tree_view_column_set_clickable(int /*long*/ column, boolean clickable);
public static final void gtk_tree_view_column_set_clickable(int /*long*/ column, boolean clickable) {
	lock.lock();
	try {
		_gtk_tree_view_column_set_clickable(column, clickable);
	} finally {
		lock.unlock();
	}
}
/**
 * @param column cast=(GtkTreeViewColumn *)
 * @param fixed_width cast=(gint)
 */
public static final native void _gtk_tree_view_column_set_fixed_width(int /*long*/ column, int fixed_width);
public static final void gtk_tree_view_column_set_fixed_width(int /*long*/ column, int fixed_width) {
	lock.lock();
	try {
		_gtk_tree_view_column_set_fixed_width(column, fixed_width);
	} finally {
		lock.unlock();
	}
}
/**
 * @param tree_column cast=(GtkTreeViewColumn *)
 * @param min_width cast=(gint)
 */
public static final native void _gtk_tree_view_column_set_min_width(int /*long*/ tree_column, int min_width);
public static final void gtk_tree_view_column_set_min_width(int /*long*/ tree_column, int min_width) {
	lock.lock();
	try {
		_gtk_tree_view_column_set_min_width(tree_column, min_width);
	} finally {
		lock.unlock();
	}
}
/**
 * @param column cast=(GtkTreeViewColumn *)
 * @param reorderable cast=(gboolean)
 */
public static final native void _gtk_tree_view_column_set_reorderable(int /*long*/ column, boolean reorderable);
public static final void gtk_tree_view_column_set_reorderable(int /*long*/ column, boolean reorderable) {
	lock.lock();
	try {
		_gtk_tree_view_column_set_reorderable(column, reorderable);
	} finally {
		lock.unlock();
	}
}
/**
 * @param column cast=(GtkTreeViewColumn *)
 * @param resizable cast=(gboolean)
 */
public static final native void _gtk_tree_view_column_set_resizable(int /*long*/ column, boolean resizable);
public static final void gtk_tree_view_column_set_resizable(int /*long*/ column, boolean resizable) {
	lock.lock();
	try {
		_gtk_tree_view_column_set_resizable(column, resizable);
	} finally {
		lock.unlock();
	}
}
/**
 * @param column cast=(GtkTreeViewColumn *)
 * @param type cast=(GtkTreeViewColumnSizing)
 */
public static final native void _gtk_tree_view_column_set_sizing(int /*long*/ column, int type);
public static final void gtk_tree_view_column_set_sizing(int /*long*/ column, int type) {
	lock.lock();
	try {
		_gtk_tree_view_column_set_sizing(column, type);
	} finally {
		lock.unlock();
	}
}
/**
 * @param tree_column cast=(GtkTreeViewColumn *)
 * @param setting cast=(gboolean)
 */
public static final native void _gtk_tree_view_column_set_sort_indicator(int /*long*/ tree_column, boolean setting);
public static final void gtk_tree_view_column_set_sort_indicator(int /*long*/ tree_column, boolean setting) {
	lock.lock();
	try {
		_gtk_tree_view_column_set_sort_indicator(tree_column, setting);
	} finally {
		lock.unlock();
	}
}
/**
 * @param tree_column cast=(GtkTreeViewColumn *)
 * @param order cast=(GtkSortType)
 */
public static final native void _gtk_tree_view_column_set_sort_order(int /*long*/ tree_column, int order);
public static final void gtk_tree_view_column_set_sort_order(int /*long*/ tree_column, int order) {
	lock.lock();
	try {
		_gtk_tree_view_column_set_sort_order(tree_column, order);
	} finally {
		lock.unlock();
	}
}
/**
 * @param tree_column cast=(GtkTreeViewColumn *)
 * @param title cast=(const gchar *)
 */
public static final native void _gtk_tree_view_column_set_title(int /*long*/ tree_column, byte[] title);
public static final void gtk_tree_view_column_set_title(int /*long*/ tree_column, byte[] title) {
	lock.lock();
	try {
		_gtk_tree_view_column_set_title(tree_column, title);
	} finally {
		lock.unlock();
	}
}
/** @param tree_column cast=(GtkTreeViewColumn *) */
public static final native void _gtk_tree_view_column_set_visible (int /*long*/ tree_column, boolean visible);
public static final void gtk_tree_view_column_set_visible (int /*long*/ tree_column, boolean visible) {
	lock.lock();
	try {
		_gtk_tree_view_column_set_visible(tree_column, visible);
	} finally {
		lock.unlock();
	}
}
/**
 * @param tree_column cast=(GtkTreeViewColumn *)
 * @param widget cast=(GtkWidget *)
 */
public static final native void _gtk_tree_view_column_set_widget(int /*long*/ tree_column, int /*long*/ widget);
public static final void gtk_tree_view_column_set_widget(int /*long*/ tree_column, int /*long*/ widget) {
	lock.lock();
	try {
		_gtk_tree_view_column_set_widget(tree_column, widget);
	} finally {
		lock.unlock();
	}
}
/**
 * @param view cast=(GtkTreeView *)
 * @param path cast=(GtkTreePath *)
 */
public static final native void _gtk_tree_view_set_drag_dest_row(int /*long*/ view, int /*long*/ path, int pos);
public static final void gtk_tree_view_set_drag_dest_row(int /*long*/ view, int /*long*/ path, int pos) {
	lock.lock();
	try {
		_gtk_tree_view_set_drag_dest_row(view, path, pos);
	} finally {
		lock.unlock();
	}
}
/** @param view cast=(GtkTreeView *) */
public static final native void _gtk_tree_view_set_enable_search (int /*long*/ view, boolean enable_search);
public static final void gtk_tree_view_set_enable_search (int /*long*/ view, boolean enable_search) {
	lock.lock();
	try {
		_gtk_tree_view_set_enable_search(view, enable_search);
	} finally {
		lock.unlock();
	}
}
/**
 * @param view cast=(GtkTreeView *)
 * @param path cast=(GtkTreePath *)
 * @param open_all cast=(gboolean)
 */
public static final native boolean _gtk_tree_view_expand_row(int /*long*/ view, int /*long*/ path, boolean open_all);
public static final boolean gtk_tree_view_expand_row(int /*long*/ view, int /*long*/ path, boolean open_all) {
	lock.lock();
	try {
		return _gtk_tree_view_expand_row(view, path, open_all);
	} finally {
		lock.unlock();
	}
}
/**
 * @param tree_view cast=(GtkTreeView *)
 * @param path cast=(GtkTreePath *)
 * @param column cast=(GtkTreeViewColumn *)
 * @param rect cast=(GdkRectangle *)
 */
public static final native void _gtk_tree_view_get_background_area(int /*long*/ tree_view, int /*long*/ path, int /*long*/ column, GdkRectangle rect);
public static final void gtk_tree_view_get_background_area(int /*long*/ tree_view, int /*long*/ path, int /*long*/ column, GdkRectangle rect) {
	lock.lock();
	try {
		_gtk_tree_view_get_background_area(tree_view, path, column, rect);
	} finally {
		lock.unlock();
	}
}
/** @param tree_view cast=(GtkTreeView *) */
public static final native int /*long*/ _gtk_tree_view_get_bin_window(int /*long*/ tree_view);
public static final int /*long*/ gtk_tree_view_get_bin_window(int /*long*/ tree_view) {
	lock.lock();
	try {
		return _gtk_tree_view_get_bin_window(tree_view);
	} finally {
		lock.unlock();
	}
}
/**
 * @param tree_view cast=(GtkTreeView *)
 * @param path cast=(GtkTreePath *)
 * @param column cast=(GtkTreeViewColumn *)
 * @param rect cast=(GdkRectangle *),flags=no_in
 */
public static final native void _gtk_tree_view_get_cell_area(int /*long*/ tree_view, int /*long*/ path, int /*long*/ column, GdkRectangle rect);
public static final void gtk_tree_view_get_cell_area(int /*long*/ tree_view, int /*long*/ path, int /*long*/ column, GdkRectangle rect) {
	lock.lock();
	try {
		_gtk_tree_view_get_cell_area(tree_view, path, column, rect);
	} finally {
		lock.unlock();
	}
}
/** @param tree_view cast=(GtkTreeView *) */
public static final native int /*long*/_gtk_tree_view_get_expander_column(int /*long*/ tree_view);
public static final int /*long*/gtk_tree_view_get_expander_column(int /*long*/ tree_view) {
	lock.lock();
	try {
		return _gtk_tree_view_get_expander_column(tree_view);
	} finally {
		lock.unlock();
	}
}
/**
 * @param tree_view cast=(GtkTreeView *)
 * @param n cast=(gint)
 */
public static final native int /*long*/ _gtk_tree_view_get_column(int /*long*/ tree_view, int n);
public static final int /*long*/ gtk_tree_view_get_column(int /*long*/ tree_view, int n) {
	lock.lock();
	try {
		return _gtk_tree_view_get_column(tree_view, n);
	} finally {
		lock.unlock();
	}
}
/** @param tree_view cast=(GtkTreeView *) */
public static final native int /*long*/ _gtk_tree_view_get_columns(int /*long*/ tree_view);
public static final int /*long*/ gtk_tree_view_get_columns(int /*long*/ tree_view) {
	lock.lock();
	try {
		return _gtk_tree_view_get_columns(tree_view);
	} finally {
		lock.unlock();
	}
}
/**
 * @param tree_view cast=(GtkTreeView *)
 * @param path cast=(GtkTreePath **)
 * @param focus_column cast=(GtkTreeViewColumn **)
 */
public static final native void _gtk_tree_view_get_cursor(int /*long*/ tree_view, int /*long*/[] path, int /*long*/[] focus_column);
public static final void gtk_tree_view_get_cursor(int /*long*/ tree_view, int /*long*/[] path, int /*long*/[] focus_column) {
	lock.lock();
	try {
		_gtk_tree_view_get_cursor(tree_view, path, focus_column);
	} finally {
		lock.unlock();
	}
}
/** @param tree_view cast=(GtkTreeView *) */
public static final native boolean _gtk_tree_view_get_headers_visible(int /*long*/ tree_view);
public static final boolean gtk_tree_view_get_headers_visible(int /*long*/ tree_view) {
	lock.lock();
	try {
		return _gtk_tree_view_get_headers_visible(tree_view);
	} finally {
		lock.unlock();
	}
}
/**
 * @param tree_view cast=(GtkTreeView *)
 * @param x cast=(gint)
 * @param y cast=(gint)
 * @param path cast=(GtkTreePath **)
 * @param column cast=(GtkTreeViewColumn **)
 * @param cell_x cast=(gint *)
 * @param cell_y cast=(gint *)
 */
public static final native boolean _gtk_tree_view_get_path_at_pos(int /*long*/ tree_view, int x, int y, int /*long*/[] path, int /*long*/[] column, int[] cell_x, int[] cell_y);
public static final boolean gtk_tree_view_get_path_at_pos(int /*long*/ tree_view, int x, int y, int /*long*/[] path, int /*long*/[] column, int[] cell_x, int[] cell_y) {
	lock.lock();
	try {
		return _gtk_tree_view_get_path_at_pos(tree_view, x, y, path, column, cell_x, cell_y);
	} finally {
		lock.unlock();
	}
}
/** @param tree_view cast=(GtkTreeView *) */
public static final native boolean _gtk_tree_view_get_rules_hint(int /*long*/ tree_view);
public static final boolean gtk_tree_view_get_rules_hint(int /*long*/ tree_view) {
	lock.lock();
	try {
		return _gtk_tree_view_get_rules_hint(tree_view);
	} finally {
		lock.unlock();
	}
}
/** @param tree_view cast=(GtkTreeView *) */
public static final native int /*long*/ _gtk_tree_view_get_selection(int /*long*/ tree_view);
public static final int /*long*/ gtk_tree_view_get_selection(int /*long*/ tree_view) {
	lock.lock();
	try {
		return _gtk_tree_view_get_selection(tree_view);
	} finally {
		lock.unlock();
	}
}
/**
 * @param tree_view cast=(GtkTreeView *)
 * @param visible_rect flags=no_in
 */
public static final native void _gtk_tree_view_get_visible_rect(int /*long*/ tree_view, GdkRectangle visible_rect);
public static final void gtk_tree_view_get_visible_rect(int /*long*/ tree_view, GdkRectangle visible_rect) {
	lock.lock();
	try {
		_gtk_tree_view_get_visible_rect(tree_view, visible_rect);
	} finally {
		lock.unlock();
	}
}
/**
 * @param tree_view cast=(GtkTreeView *)
 * @param column cast=(GtkTreeViewColumn *)
 * @param position cast=(gint)
 */
public static final native int _gtk_tree_view_insert_column(int /*long*/ tree_view, int /*long*/ column, int position);
public static final int gtk_tree_view_insert_column(int /*long*/ tree_view, int /*long*/ column, int position) {
	lock.lock();
	try {
		return _gtk_tree_view_insert_column(tree_view, column, position);
	} finally {
		lock.unlock();
	}
}
/**
 * @param tree_view cast=(GtkTreeView *)
 * @param column cast=(GtkTreeViewColumn *)
 * @param base_column cast=(GtkTreeViewColumn *)
 */
public static final native void _gtk_tree_view_move_column_after(int /*long*/ tree_view, int /*long*/ column, int /*long*/ base_column);
public static final void gtk_tree_view_move_column_after(int /*long*/ tree_view, int /*long*/ column, int /*long*/base_column) {
	lock.lock();
	try {
		_gtk_tree_view_move_column_after(tree_view, column, base_column);
	} finally {
		lock.unlock();
	}
}
/** @param model cast=(GtkTreeModel *) */
public static final native int /*long*/ _gtk_tree_view_new_with_model(int /*long*/ model);
public static final int /*long*/ gtk_tree_view_new_with_model(int /*long*/ model) {
	lock.lock();
	try {
		return _gtk_tree_view_new_with_model(model);
	} finally {
		lock.unlock();
	}
}
/**
 * @param tree_view cast=(GtkTreeView *)
 * @param column cast=(GtkTreeViewColumn *)
 */
public static final native void _gtk_tree_view_remove_column(int /*long*/ tree_view, int /*long*/ column);
public static final void gtk_tree_view_remove_column(int /*long*/ tree_view, int /*long*/ column) {
	lock.lock();
	try {
		_gtk_tree_view_remove_column(tree_view, column);
	} finally {
		lock.unlock();
	}
}
/**
 * @param view cast=(GtkTreeView *)
 * @param path cast=(GtkTreePath *)
 */
public static final native boolean _gtk_tree_view_row_expanded(int /*long*/ view, int /*long*/ path);
public static final boolean gtk_tree_view_row_expanded(int /*long*/ view, int /*long*/ path) {
	lock.lock();
	try {
		return _gtk_tree_view_row_expanded(view, path);
	} finally {
		lock.unlock();
	}
}
/**
 * @param tree_view cast=(GtkTreeView *)
 * @param path cast=(GtkTreePath *)
 * @param column cast=(GtkTreeViewColumn *)
 * @param use_align cast=(gboolean)
 * @param row_aligh cast=(gfloat)
 * @param column_align cast=(gfloat)
 */
public static final native void _gtk_tree_view_scroll_to_cell(int /*long*/ tree_view, int /*long*/ path, int /*long*/ column, boolean use_align, float row_aligh, float column_align);
public static final void gtk_tree_view_scroll_to_cell(int /*long*/ tree_view, int /*long*/ path, int /*long*/ column, boolean use_align, float row_aligh, float column_align) {
	lock.lock();
	try {
		_gtk_tree_view_scroll_to_cell(tree_view, path, column, use_align, row_aligh, column_align);
	} finally {
		lock.unlock();
	}
}
/**
 * @param tree_view cast=(GtkTreeView *)
 * @param tree_x cast=(gint)
 * @param tree_y cast=(gint)
 */
public static final native void _gtk_tree_view_scroll_to_point (int /*long*/ tree_view, int tree_x, int tree_y);
public static final void gtk_tree_view_scroll_to_point (int /*long*/ tree_view, int tree_x, int tree_y) {
	lock.lock();
	try {
		_gtk_tree_view_scroll_to_point(tree_view, tree_x, tree_y);
	} finally {
		lock.unlock();
	}
}
/**
 * @param tree_view cast=(GtkTreeView *)
 * @param path cast=(GtkTreePath *)
 * @param focus_column cast=(GtkTreeViewColumn *)
 */
public static final native void _gtk_tree_view_set_cursor(int /*long*/ tree_view, int /*long*/ path, int /*long*/ focus_column, boolean start_editing);
public static final void gtk_tree_view_set_cursor(int /*long*/ tree_view, int /*long*/ path, int /*long*/ focus_column, boolean start_editing) {
	lock.lock();
	try {
		_gtk_tree_view_set_cursor(tree_view, path, focus_column, start_editing);
	} finally {
		lock.unlock();
	}
} 
/**
 * @method flags=dynamic
 * @param tree_view cast=(GtkTreeView*)
 */
public static final native void _gtk_tree_view_set_grid_lines(int /*long*/ tree_view, int grid_lines);
public static final void gtk_tree_view_set_grid_lines(int /*long*/ tree_view, int grid_lines) {
	lock.lock();
	try {
		_gtk_tree_view_set_grid_lines(tree_view, grid_lines);
	} finally {
		lock.unlock();
	}
}
/**
 * @param tree_view cast=(GtkTreeView *)
 * @param visible cast=(gboolean)
 */
public static final native void _gtk_tree_view_set_headers_visible(int /*long*/ tree_view, boolean visible);
public static final void gtk_tree_view_set_headers_visible(int /*long*/ tree_view, boolean visible) {
	lock.lock();
	try {
		_gtk_tree_view_set_headers_visible(tree_view, visible);
	} finally {
		lock.unlock();
	}
}
/**
 * @param tree_view cast=(GtkTreeView *)
 * @param model cast=(GtkTreeModel *)
 */
public static final native void _gtk_tree_view_set_model(int /*long*/ tree_view, int /*long*/ model);
public static final void gtk_tree_view_set_model(int /*long*/ tree_view, int /*long*/ model) {
	lock.lock();
	try {
		_gtk_tree_view_set_model(tree_view, model);
	} finally {
		lock.unlock();
	}
}
/** @param tree_view cast=(GtkTreeView *) */
public static final native void _gtk_tree_view_set_rules_hint(int /*long*/ tree_view, boolean setting);
public static final void gtk_tree_view_set_rules_hint(int /*long*/ tree_view, boolean setting) {
	lock.lock();
	try {
		_gtk_tree_view_set_rules_hint(tree_view, setting);
	} finally {
		lock.unlock();
	}
}
/**
 * @param tree_view cast=(GtkTreeView *)
 * @param column cast=(gint)
 */
public static final native void _gtk_tree_view_set_search_column(int /*long*/ tree_view, int column);
public static final void gtk_tree_view_set_search_column(int /*long*/ tree_view, int column) {
	lock.lock();
	try {
		_gtk_tree_view_set_search_column(tree_view, column);
	} finally {
		lock.unlock();
	}
}
/**
 * @param tree_view cast=(GtkTreeView *)
 * @param tx cast=(gint)
 * @param ty cast=(gint)
 * @param wx cast=(gint *)
 * @param wy cast=(gint *)
 */
public static final native void _gtk_tree_view_tree_to_widget_coords(int /*long*/ tree_view, int tx, int ty, int[] wx, int[] wy);
public static final void gtk_tree_view_tree_to_widget_coords(int /*long*/ tree_view, int tx, int ty, int[] wx, int[] wy) {
	lock.lock();
	try {
		_gtk_tree_view_tree_to_widget_coords(tree_view, tx, ty, wx, wy);
	} finally {
		lock.unlock();
	}
}
/** @param tree_view cast=(GtkTreeView *) */
public static final native void _gtk_tree_view_unset_rows_drag_dest(int /*long*/ tree_view);
public static final void gtk_tree_view_unset_rows_drag_dest(int /*long*/ tree_view) {
	lock.lock();
	try {
		_gtk_tree_view_unset_rows_drag_dest(tree_view);
	} finally {
		lock.unlock();
	}
}
/** @param tree_view cast=(GtkTreeView *) */
public static final native void _gtk_tree_view_widget_to_tree_coords(int /*long*/ tree_view, int wx, int wy, int[] tx, int[] ty);
public static final void gtk_tree_view_widget_to_tree_coords(int /*long*/ tree_view, int wx, int wy, int[] tx, int[] ty) {
	lock.lock();
	try {
		_gtk_tree_view_widget_to_tree_coords(tree_view, wx, wy, tx, ty);
	} finally {
		lock.unlock();
	}
}
/**
 * @param homogeneous cast=(gboolean)
 * @param spacing cast=(gint)
 */
public static final native int /*long*/ _gtk_vbox_new(boolean homogeneous, int spacing);
public static final int /*long*/ gtk_vbox_new(boolean homogeneous, int spacing) {
	lock.lock();
	try {
		return _gtk_vbox_new(homogeneous, spacing);
	} finally {
		lock.unlock();
	}
}
/** @param viewport cast=(GtkViewport *) */
public static final native int _gtk_viewport_get_shadow_type(int /*long*/ viewport);
public static final int gtk_viewport_get_shadow_type(int /*long*/ viewport) {
	lock.lock();
	try {
		return _gtk_viewport_get_shadow_type(viewport);
	} finally {
		lock.unlock();
	}
}
/**
 * @param viewport cast=(GtkViewport *)
 * @param type cast=(GtkShadowType)
 */
public static final native void _gtk_viewport_set_shadow_type(int /*long*/ viewport, int type);
public static final void gtk_viewport_set_shadow_type(int /*long*/ viewport, int type) {
	lock.lock();
	try {
		_gtk_viewport_set_shadow_type(viewport, type);
	} finally {
		lock.unlock();
	}
}
/** @param adjustment cast=(GtkAdjustment *) */
public static final native int /*long*/ _gtk_vscale_new(int /*long*/ adjustment);
public static final int /*long*/ gtk_vscale_new(int /*long*/ adjustment) {
	lock.lock();
	try {
		return _gtk_vscale_new(adjustment);
	} finally {
		lock.unlock();
	}
}
/** @param adjustment cast=(GtkAdjustment *) */
public static final native int /*long*/ _gtk_vscrollbar_new(int /*long*/ adjustment);
public static final int /*long*/ gtk_vscrollbar_new(int /*long*/ adjustment) {
	lock.lock();
	try {
		return _gtk_vscrollbar_new(adjustment);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gtk_vseparator_new();
public static final int /*long*/ gtk_vseparator_new() {
	lock.lock();
	try {
		return _gtk_vseparator_new();
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(GtkWidget *)
 * @param accel_signal cast=(const gchar *)
 * @param accel_group cast=(GtkAccelGroup *)
 * @param accel_key cast=(guint)
 * @param accel_mods cast=(GdkModifierType)
 */
public static final native void _gtk_widget_add_accelerator(int /*long*/ widget, byte[] accel_signal, int /*long*/ accel_group, int accel_key, int accel_mods, int accel_flags);
public static final void gtk_widget_add_accelerator(int /*long*/ widget, byte[] accel_signal, int /*long*/ accel_group, int accel_key, int accel_mods, int accel_flags) {
	lock.lock();
	try {
		_gtk_widget_add_accelerator(widget, accel_signal, accel_group, accel_key, accel_mods, accel_flags);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(GtkWidget *)
 * @param events cast=(gint)
 */
public static final native void _gtk_widget_add_events(int /*long*/ widget, int events);
public static final void gtk_widget_add_events(int /*long*/ widget, int events) {
	lock.lock();
	try {
		_gtk_widget_add_events(widget, events);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(GtkWidget *) */
public static final native boolean _gtk_widget_child_focus(int /*long*/ widget, int direction);
public static final boolean gtk_widget_child_focus(int /*long*/ widget, int direction) {
	lock.lock();
	try {
		return _gtk_widget_child_focus(widget, direction);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(GtkWidget *)
 * @param text cast=(const gchar *)
 */
public static final native int /*long*/ _gtk_widget_create_pango_layout(int /*long*/ widget, byte[] text);
public static final int /*long*/ gtk_widget_create_pango_layout(int /*long*/ widget, byte[] text) {
	lock.lock();
	try {
		return _gtk_widget_create_pango_layout(widget, text);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(GtkWidget *)
 * @param text cast=(const gchar *)
 */
public static final native int /*long*/ _gtk_widget_create_pango_layout(int /*long*/ widget, int /*long*/ text);
public static final int /*long*/ gtk_widget_create_pango_layout(int /*long*/ widget, int /*long*/ text) {
	lock.lock();
	try {
		return _gtk_widget_create_pango_layout(widget, text);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(GtkWidget *) */
public static final native void _gtk_widget_destroy(int /*long*/ widget);
public static final void gtk_widget_destroy(int /*long*/ widget) {
	lock.lock();
	try {
		_gtk_widget_destroy(widget);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(GtkWidget *)
 * @param event cast=(GdkEvent *)
 */
public static final native boolean _gtk_widget_event(int /*long*/ widget, int /*long*/ event);
public static final boolean gtk_widget_event(int /*long*/ widget, int /*long*/ event) {
	lock.lock();
	try {
		return _gtk_widget_event(widget, event);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(GtkWidget *) */
public static final native int /*long*/ _gtk_widget_get_accessible (int /*long*/ widget);
public static final int /*long*/ gtk_widget_get_accessible (int /*long*/ widget) {
	lock.lock();
	try {
		return _gtk_widget_get_accessible(widget);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(GtkWidget *) */
public static final native boolean _gtk_widget_get_child_visible (int /*long*/ widget);
public static final boolean gtk_widget_get_child_visible (int /*long*/ widget) {
	lock.lock();
	try {
		return _gtk_widget_get_child_visible(widget);
	} finally {
		lock.unlock();
	}
}
public static final native int _gtk_widget_get_default_direction();
public static final int gtk_widget_get_default_direction() {
	lock.lock();
	try {
		return _gtk_widget_get_default_direction();
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gtk_widget_get_default_style();
public static final int /*long*/ gtk_widget_get_default_style() {
	lock.lock();
	try {
		return _gtk_widget_get_default_style();
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(GtkWidget *) */
public static final native int _gtk_widget_get_direction(int /*long*/ widget);
public static final int gtk_widget_get_direction(int /*long*/ widget) {
	lock.lock();
	try {
		return _gtk_widget_get_direction(widget);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(GtkWidget *) */
public static final native int _gtk_widget_get_events(int /*long*/ widget);
public static final int gtk_widget_get_events(int /*long*/ widget) {
	lock.lock();
	try {
		return _gtk_widget_get_events(widget);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(GtkWidget *) */
public static final native int /*long*/ _gtk_widget_get_modifier_style(int /*long*/ widget);
public static final int /*long*/ gtk_widget_get_modifier_style(int /*long*/ widget) {
	lock.lock();
	try {
		return _gtk_widget_get_modifier_style(widget);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(GtkWidget *) */
public static final native int /*long*/ _gtk_widget_get_pango_context(int /*long*/ widget);
public static final int /*long*/ gtk_widget_get_pango_context(int /*long*/ widget) {
	lock.lock();
	try {
		return _gtk_widget_get_pango_context(widget);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(GtkWidget *) */
public static final native int /*long*/ _gtk_widget_get_parent(int /*long*/ widget);
public static final int /*long*/ gtk_widget_get_parent(int /*long*/ widget) {
	lock.lock();
	try {
		return _gtk_widget_get_parent(widget);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(GtkWidget *) */
public static final native int /*long*/ _gtk_widget_get_style(int /*long*/ widget);
public static final int /*long*/ gtk_widget_get_style(int /*long*/ widget) {
	lock.lock();
	try {
		return _gtk_widget_get_style(widget);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(GtkWidget *)
 * @param width cast=(gint *)
 * @param height cast=(gint *)
 */
public static final native void _gtk_widget_get_size_request(int /*long*/ widget, int [] width, int [] height);
public static final void gtk_widget_get_size_request(int /*long*/ widget, int [] width, int [] height) {
	lock.lock();
	try {
		_gtk_widget_get_size_request(widget, width, height);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(GtkWidget *) */
public static final native int /*long*/ _gtk_widget_get_toplevel (int /*long*/ widget);
public static final int /*long*/ gtk_widget_get_toplevel (int /*long*/ widget) {
	lock.lock();
	try {
		return _gtk_widget_get_toplevel(widget);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(GtkWidget *) */
public static final native void _gtk_widget_grab_focus(int /*long*/ widget);
public static final void gtk_widget_grab_focus(int /*long*/ widget) {
	lock.lock();
	try {
		_gtk_widget_grab_focus(widget);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(GtkWidget *) */
public static final native void _gtk_widget_hide(int /*long*/ widget);
public static final void gtk_widget_hide(int /*long*/ widget) {
	lock.lock();
	try {
		_gtk_widget_hide(widget);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param widget cast=(GtkWidget *)
 */
public static final native boolean _gtk_widget_is_composited(int /*long*/ widget);
public static final boolean gtk_widget_is_composited(int /*long*/ widget) {
	lock.lock();
	try {
		return _gtk_widget_is_composited(widget);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(GtkWidget *) */
public static final native boolean _gtk_widget_is_focus(int /*long*/ widget);
public static final boolean gtk_widget_is_focus(int /*long*/ widget) {
	lock.lock();
	try {
		return _gtk_widget_is_focus(widget);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(GtkWidget *) */
public static final native void _gtk_widget_map(int /*long*/ widget);
public static final void gtk_widget_map(int /*long*/ widget) {
	lock.lock();
	try {
		_gtk_widget_map(widget);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(GtkWidget *)
 * @param group_cycling cast=(gboolean)
 */
public static final native boolean _gtk_widget_mnemonic_activate(int /*long*/ widget, boolean group_cycling);
public static final boolean gtk_widget_mnemonic_activate(int /*long*/ widget, boolean group_cycling) {
	lock.lock();
	try {
		return _gtk_widget_mnemonic_activate(widget, group_cycling);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(GtkWidget *)
 * @param state cast=(GtkStateType)
 * @param color cast=(GdkColor *),flags=no_out
 */
public static final native void _gtk_widget_modify_base(int /*long*/ widget, int state, GdkColor color);
public static final void gtk_widget_modify_base(int /*long*/ widget, int state, GdkColor color) {
	lock.lock();
	try {
		_gtk_widget_modify_base(widget, state, color);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(GtkWidget *)
 * @param state cast=(GtkStateType)
 * @param color cast=(GdkColor *),flags=no_out
 */
public static final native void _gtk_widget_modify_bg(int /*long*/ widget, int state, GdkColor color);
public static final void gtk_widget_modify_bg(int /*long*/ widget, int state, GdkColor color) {
	lock.lock();
	try {
		_gtk_widget_modify_bg(widget, state, color);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(GtkWidget *)
 * @param state cast=(GtkStateType)
 * @param color cast=(GdkColor *),flags=no_out
 */
public static final native void _gtk_widget_modify_fg(int /*long*/ widget, int state, GdkColor color);
public static final void gtk_widget_modify_fg(int /*long*/ widget, int state, GdkColor color) {
	lock.lock();
	try {
		_gtk_widget_modify_fg(widget, state, color);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(GtkWidget *)
 * @param pango_font_descr cast=(PangoFontDescription *)
 */
public static final native void _gtk_widget_modify_font(int /*long*/ widget, int /*long*/ pango_font_descr);
public static final void gtk_widget_modify_font(int /*long*/ widget, int /*long*/ pango_font_descr) {
	lock.lock();
	try {
		_gtk_widget_modify_font(widget, pango_font_descr);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(GtkWidget *)
 * @param style cast=(GtkRcStyle *)
 */
public static final native void _gtk_widget_modify_style(int /*long*/ widget, int /*long*/ style);
public static final void gtk_widget_modify_style(int /*long*/ widget, int /*long*/ style) {
	lock.lock();
	try {
		_gtk_widget_modify_style(widget, style);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(GtkWidget *)
 * @param state cast=(GtkStateType)
 * @param color cast=(GdkColor *),flags=no_out
 */
public static final native void _gtk_widget_modify_text(int /*long*/ widget, int state, GdkColor color);
public static final void gtk_widget_modify_text(int /*long*/ widget, int state, GdkColor color) {
	lock.lock();
	try {
		_gtk_widget_modify_text(widget, state, color);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(GtkWidget *) */
public static final native void _gtk_widget_queue_resize(int /*long*/ widget);
public static final void gtk_widget_queue_resize(int /*long*/ widget) {
	lock.lock();
	try {
		_gtk_widget_queue_resize(widget);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(GtkWidget *) */
public static final native void _gtk_widget_realize(int /*long*/ widget);
public static final void gtk_widget_realize(int /*long*/ widget) {
	lock.lock();
	try {
		_gtk_widget_realize(widget);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(GtkWidget *)
 * @param accel_group cast=(GtkAccelGroup *)
 * @param accel_key cast=(guint)
 * @param accel_mods cast=(GdkModifierType)
 */
public static final native void _gtk_widget_remove_accelerator(int /*long*/ widget, int /*long*/ accel_group, int accel_key, int accel_mods);
public static final void gtk_widget_remove_accelerator(int /*long*/ widget, int /*long*/ accel_group, int accel_key, int accel_mods) {
	lock.lock();
	try {
		_gtk_widget_remove_accelerator(widget, accel_group, accel_key, accel_mods);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(GtkWidget *)
 * @param new_parent cast=(GtkWidget *)
 */
public static final native void _gtk_widget_reparent(int /*long*/ widget, int /*long*/ new_parent);
public static final void gtk_widget_reparent(int /*long*/ widget, int /*long*/ new_parent) {
	lock.lock();
	try {
		_gtk_widget_reparent(widget, new_parent);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(GtkWidget *)
 * @param event cast=(GdkEvent *)
 */
public static final native int _gtk_widget_send_expose(int /*long*/ widget, int /*long*/ event);
public static final int gtk_widget_send_expose(int /*long*/ widget, int /*long*/ event) {
	lock.lock();
	try {
		return _gtk_widget_send_expose(widget, event);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(GtkWidget *) */
public static final native void _gtk_widget_set_app_paintable(int /*long*/ widget, boolean app_paintable);
public static final void gtk_widget_set_app_paintable(int /*long*/ widget, boolean app_paintable) {
	lock.lock();
	try {
		_gtk_widget_set_app_paintable(widget, app_paintable);
	} finally {
		lock.unlock();
	}
}
/** @param dir cast=(GtkTextDirection) */
public static final native void _gtk_widget_set_default_direction(int dir);
public static final void gtk_widget_set_default_direction(int dir) {
	lock.lock();
	try {
		_gtk_widget_set_default_direction(dir);
	} finally {
		lock.unlock();
	}
} 
/**
 * @param widget cast=(GtkWidget *)
 * @param dir cast=(GtkTextDirection)
 */
public static final native void _gtk_widget_set_direction(int /*long*/ widget, int dir);
public static final void gtk_widget_set_direction(int /*long*/ widget, int dir) {
	lock.lock();
	try {
		_gtk_widget_set_direction(widget, dir);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(GtkWidget *)
 * @param double_buffered cast=(gboolean)
 */
public static final native void _gtk_widget_set_double_buffered(int /*long*/ widget, boolean double_buffered);
public static final void gtk_widget_set_double_buffered(int /*long*/ widget, boolean double_buffered) {
	lock.lock();
	try {
		_gtk_widget_set_double_buffered(widget, double_buffered);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(GtkWidget *)
 * @param name cast=(const char *)
 */
public static final native void _gtk_widget_set_name(int /*long*/ widget, byte[] name);
public static final void gtk_widget_set_name(int /*long*/ widget, byte[] name) {
	lock.lock();
	try {
		_gtk_widget_set_name(widget, name);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(GtkWidget *)
 * @param redraw cast=(gboolean)
 */
public static final native void _gtk_widget_set_redraw_on_allocate(int /*long*/ widget, boolean redraw);
public static final void gtk_widget_set_redraw_on_allocate(int /*long*/ widget, boolean redraw) {
	lock.lock();
	try {
		_gtk_widget_set_redraw_on_allocate(widget, redraw);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(GtkWidget *)
 * @param sensitive cast=(gboolean)
 */
public static final native void _gtk_widget_set_sensitive(int /*long*/ widget, boolean sensitive);
public static final void gtk_widget_set_sensitive(int /*long*/ widget, boolean sensitive) {
	lock.lock();
	try {
		_gtk_widget_set_sensitive(widget, sensitive);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(GtkWidget *)
 * @param width cast=(gint)
 * @param height cast=(gint)
 */
public static final native void _gtk_widget_set_size_request(int /*long*/ widget, int width, int height);
public static final void gtk_widget_set_size_request(int /*long*/ widget, int width, int height) {
	lock.lock();
	try {
		_gtk_widget_set_size_request(widget, width, height);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(GtkWidget *)
 * @param state cast=(GtkStateType)
 */
public static final native void _gtk_widget_set_state(int /*long*/ widget, int state);
public static final void gtk_widget_set_state(int /*long*/ widget, int state) {
	lock.lock();
	try {
		_gtk_widget_set_state(widget, state);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(GtkWidget *)
 * @param style cast=(GtkStyle *)
 */
public static final native void _gtk_widget_set_style(int /*long*/ widget, int /*long*/ style);
public static final void gtk_widget_set_style(int /*long*/ widget, int /*long*/ style) {
	lock.lock();
	try {
		_gtk_widget_set_style(widget, style);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(GtkWidget *)
 * @param shape_mask cast=(GdkBitmap *)
 * @param offset_x cast=(gint)
 * @param offset_y cast=(gint)
 */
public static final native void _gtk_widget_shape_combine_mask(int /*long*/ widget, int /*long*/ shape_mask, int offset_x, int offset_y);
public static final void gtk_widget_shape_combine_mask(int /*long*/ widget, int /*long*/ shape_mask, int offset_x, int offset_y) {
	lock.lock();
	try {
		_gtk_widget_shape_combine_mask(widget, shape_mask, offset_x, offset_y);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(GtkWidget *) */
public static final native void _gtk_widget_show(int /*long*/ widget);
public static final void gtk_widget_show(int /*long*/ widget) {
	lock.lock();
	try {
		_gtk_widget_show(widget);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(GtkWidget *) */
public static final native void _gtk_widget_show_now(int /*long*/ widget);
public static final void gtk_widget_show_now(int /*long*/ widget) {
	lock.lock();
	try {
		_gtk_widget_show_now(widget);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(GtkWidget *)
 * @param allocation cast=(GtkAllocation *),flags=no_out
 */
public static final native void _gtk_widget_size_allocate(int /*long*/ widget, GtkAllocation allocation);
public static final void gtk_widget_size_allocate(int /*long*/ widget, GtkAllocation allocation) {
	lock.lock();
	try {
		_gtk_widget_size_allocate(widget, allocation);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(GtkWidget *)
 * @param requisition cast=(GtkRequisition *),flags=no_in
 */
public static final native void _gtk_widget_size_request(int /*long*/ widget, GtkRequisition requisition);
public static final void gtk_widget_size_request(int /*long*/ widget, GtkRequisition requisition) {
	lock.lock();
	try {
		_gtk_widget_size_request(widget, requisition);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(GtkWidget *)
 * @param property_name cast=(const gchar *)
 * @param terminator cast=(const gchar *),flags=sentinel
 */
public static final native void _gtk_widget_style_get(int /*long*/ widget, byte[] property_name, int[] value, int /*long*/ terminator);
public static final void gtk_widget_style_get(int /*long*/ widget, byte[] property_name, int[] value, int /*long*/ terminator) {
	lock.lock();
	try {
		_gtk_widget_style_get(widget, property_name, value, terminator);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(GtkWidget *)
 * @param property_name cast=(const gchar *)
 * @param terminator cast=(const gchar *),flags=sentinel
 */
public static final native void _gtk_widget_style_get(int /*long*/ widget, byte[] property_name, long[] value, int /*long*/ terminator);
public static final void gtk_widget_style_get(int /*long*/ widget, byte[] property_name, long[] value, int /*long*/ terminator) {
	lock.lock();
	try {
		_gtk_widget_style_get(widget, property_name, value, terminator);
	} finally {
		lock.unlock();
	}
}
/**
 * @param src_widget cast=(GtkWidget *)
 * @param dest_widget cast=(GtkWidget *)
 */
public static final native boolean _gtk_widget_translate_coordinates(int /*long*/ src_widget, int /*long*/ dest_widget, int src_x, int src_y, int[] dest_x, int[] dest_y);
public static final boolean gtk_widget_translate_coordinates(int /*long*/ src_widget, int /*long*/ dest_widget, int src_x, int src_y, int[] dest_x, int[] dest_y) {
	lock.lock();
	try {
		return _gtk_widget_translate_coordinates(src_widget, dest_widget, src_x, src_y, dest_x, dest_y);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(GtkWidget *) */
public static final native void _gtk_widget_unrealize(int /*long*/ widget);
public static final void gtk_widget_unrealize(int /*long*/ widget) {
	lock.lock();
	try {
		_gtk_widget_unrealize(widget);
	} finally {
		lock.unlock();
	}
}
/** @param window cast=(GtkWindow *) */
public static final native boolean _gtk_window_activate_default(int /*long*/ window);
public static final boolean gtk_window_activate_default(int /*long*/ window) {
	lock.lock();
	try {
		return _gtk_window_activate_default(window);
	} finally {
		lock.unlock();
	}
}
/**
 * @param window cast=(GtkWindow *)
 * @param accel_group cast=(GtkAccelGroup *)
 */
public static final native void _gtk_window_add_accel_group(int /*long*/ window, int /*long*/ accel_group);
public static final void gtk_window_add_accel_group(int /*long*/ window, int /*long*/ accel_group) {
	lock.lock();
	try {
		_gtk_window_add_accel_group(window, accel_group);
	} finally {
		lock.unlock();
	}
}
/** @param handle cast=(GtkWindow *) */
public static final native void _gtk_window_deiconify(int /*long*/ handle);
public static final void gtk_window_deiconify(int /*long*/ handle) {
	lock.lock();
	try {
		_gtk_window_deiconify(handle);
	} finally {
		lock.unlock();
	}
}
/** @param window cast=(GtkWindow *) */
public static final native int /*long*/ _gtk_window_get_focus(int /*long*/ window);
public static final int /*long*/ gtk_window_get_focus(int /*long*/ window) {
	lock.lock();
	try {
		return _gtk_window_get_focus(window);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param window cast=(GtkWindow *)
 */
public static final native int /*long*/ _gtk_window_get_group(int /*long*/ window);
public static final int /*long*/ gtk_window_get_group(int /*long*/ window) {
	lock.lock();
	try {
		return _gtk_window_get_group(window);
	} finally {
		lock.unlock();
	}
}
/** @param window cast=(GtkWindow *) */
public static final native int /*long*/ _gtk_window_get_icon_list(int /*long*/ window);
public static final int /*long*/ gtk_window_get_icon_list(int /*long*/ window) {
	lock.lock();
	try {
		return _gtk_window_get_icon_list(window);
	} finally {
		lock.unlock();
	}
}
/** @param window cast=(GtkWindow *) */
public static final native boolean _gtk_window_get_modal(int /*long*/ window);
public static final boolean gtk_window_get_modal(int /*long*/ window) {
	lock.lock();
	try {
		return _gtk_window_get_modal(window);
	} finally {
		lock.unlock();
	}
}
/** @param window cast=(GtkWindow *) */
public static final native int _gtk_window_get_mnemonic_modifier(int /*long*/ window);
public static final int gtk_window_get_mnemonic_modifier(int /*long*/ window) {
	lock.lock();
	try {
		return _gtk_window_get_mnemonic_modifier(window);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param window cast=(GtkWindow *)
 */
public static final native double _gtk_window_get_opacity (int /*long*/ window);
public static final double gtk_window_get_opacity (int /*long*/ window) {
	lock.lock();
	try {
		return _gtk_window_get_opacity (window);
	} finally {
		lock.unlock();
	}
}
/**
 * @param handle cast=(GtkWindow *)
 * @param x cast=(gint *)
 * @param y cast=(gint *)
 */
public static final native void _gtk_window_get_position(int /*long*/ handle, int[] x, int[] y);
public static final void gtk_window_get_position(int /*long*/ handle, int[] x, int[] y) {
	lock.lock();
	try {
		_gtk_window_get_position(handle, x, y);
	} finally {
		lock.unlock();
	}
}
/**
 * @param handle cast=(GtkWindow *)
 * @param x cast=(gint *)
 * @param y cast=(gint *)
 */
public static final native void _gtk_window_get_size(int /*long*/ handle, int[] x, int[] y);
public static final void gtk_window_get_size(int /*long*/ handle, int[] x, int[] y) {
	lock.lock();
	try {
		_gtk_window_get_size(handle, x, y);
	} finally {
		lock.unlock();
	}
}
/**
 * @param group cast=(GtkWindowGroup*)
 * @param window cast=(GtkWindow*)
 */
public static final native void _gtk_window_group_add_window(int /*long*/ group, int /*long*/ window);
public static final void gtk_window_group_add_window(int /*long*/ group, int /*long*/ window) {
	lock.lock();
	try {
		_gtk_window_group_add_window(group, window);
	} finally {
		lock.unlock();
	}
}
/**
 * @param group cast=(GtkWindowGroup*)
 * @param window cast=(GtkWindow*)
 */
public static final native void _gtk_window_group_remove_window(int /*long*/ group, int /*long*/ window);
public static final void gtk_window_group_remove_window(int /*long*/ group, int /*long*/ window) {
	lock.lock();
	try {
		_gtk_window_group_remove_window(group, window);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gtk_window_group_new();
public static final int /*long*/ gtk_window_group_new() {
	lock.lock();
	try {
		return _gtk_window_group_new();
	} finally {
		lock.unlock();
	}
}
/** @param handle cast=(GtkWindow *) */
public static final native void _gtk_window_iconify(int /*long*/ handle);
public static final void gtk_window_iconify(int /*long*/ handle) {
	lock.lock();
	try {
		_gtk_window_iconify(handle);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gtk_window_list_toplevels ();
public static final int /*long*/ gtk_window_list_toplevels () {
	lock.lock();
	try {
		return _gtk_window_list_toplevels ();
	} finally {
		lock.unlock();
	}
}
/** @param handle cast=(GtkWindow *) */
public static final native void _gtk_window_maximize(int /*long*/ handle);
public static final void gtk_window_maximize(int /*long*/ handle) {
	lock.lock();
	try {
		_gtk_window_maximize(handle);
	} finally {
		lock.unlock();
	}
}
/** @param handle cast=(GtkWindow *) */
public static final native void _gtk_window_fullscreen(int /*long*/ handle);
public static final void gtk_window_fullscreen(int /*long*/ handle) {
	lock.lock();
	try {
		_gtk_window_fullscreen(handle);
	} finally {
		lock.unlock();
	}
}
/** @param handle cast=(GtkWindow *) */
public static final native void _gtk_window_unfullscreen(int /*long*/ handle);
public static final void gtk_window_unfullscreen(int /*long*/ handle) {
	lock.lock();
	try {
		_gtk_window_unfullscreen(handle);
	} finally {
		lock.unlock();
	}
}
/**
 * @param handle cast=(GtkWindow *)
 * @param x cast=(gint)
 * @param y cast=(gint)
 */
public static final native void _gtk_window_move(int /*long*/ handle, int x, int y);
public static final void gtk_window_move(int /*long*/ handle, int x, int y) {
	lock.lock();
	try {
		_gtk_window_move(handle, x, y);
	} finally {
		lock.unlock();
	}
}
/** @param type cast=(GtkWindowType) */
public static final native int /*long*/ _gtk_window_new(int type);
public static final int /*long*/ gtk_window_new(int type) {
	lock.lock();
	try {
		return _gtk_window_new(type);
	} finally {
		lock.unlock();
	}
}
/** @param window cast=(GtkWindow *) */
public static final native void _gtk_window_present(int /*long*/ window);
public static final void gtk_window_present(int /*long*/ window) {
	lock.lock();
	try {
		_gtk_window_present(window);
	} finally {
		lock.unlock();
	}
}
/**
 * @param window cast=(GtkWindow *)
 * @param accel_group cast=(GtkAccelGroup *)
 */
public static final native void _gtk_window_remove_accel_group(int /*long*/ window, int /*long*/ accel_group);
public static final void gtk_window_remove_accel_group(int /*long*/ window, int /*long*/ accel_group) {
	lock.lock();
	try {
		_gtk_window_remove_accel_group(window, accel_group);
	} finally {
		lock.unlock();
	}
}
/**
 * @param handle cast=(GtkWindow *)
 * @param x cast=(gint)
 * @param y cast=(gint)
 */
public static final native void _gtk_window_resize(int /*long*/ handle, int x, int y);
public static final void gtk_window_resize(int /*long*/ handle, int x, int y) {
	lock.lock();
	try {
		_gtk_window_resize(handle, x, y);
	} finally {
		lock.unlock();
	}
}
/**
 * @param window cast=(GtkWindow *)
 * @param widget cast=(GtkWidget *)
 */
public static final native void _gtk_window_set_default(int /*long*/ window, int /*long*/ widget);
public static final void gtk_window_set_default(int /*long*/ window, int /*long*/ widget) {
	lock.lock();
	try {
		_gtk_window_set_default(window, widget);
	} finally {
		lock.unlock();
	}
}
/**
 * @param window cast=(GtkWindow *)
 * @param setting cast=(gboolean)
 */
public static final native void _gtk_window_set_destroy_with_parent(int /*long*/ window, boolean setting);
public static final void gtk_window_set_destroy_with_parent(int /*long*/ window, boolean setting) {
	lock.lock();
	try {
		_gtk_window_set_destroy_with_parent(window, setting);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param window cast=(GtkWindow *)
 * @param setting cast=(gboolean)
 */
public static final native void _gtk_window_set_keep_below(int /*long*/ window, boolean setting);
public static final void gtk_window_set_keep_below(int /*long*/ window,  boolean setting) {
	lock.lock();
	try {
		_gtk_window_set_keep_below(window, setting);
	} finally {
		lock.unlock();
	}
}
/**
 * @param window cast=(GtkWindow *)
 * @param geometry_widget cast=(GtkWidget *)
 * @param geometry flags=no_out
 */
public static final native void _gtk_window_set_geometry_hints(int /*long*/ window, int /*long*/ geometry_widget, GdkGeometry geometry, int geom_mask);
public static final void gtk_window_set_geometry_hints(int /*long*/ window, int /*long*/ geometry_widget, GdkGeometry geometry, int geom_mask) {
	lock.lock();
	try {
		_gtk_window_set_geometry_hints(window, geometry_widget, geometry, geom_mask);
	} finally {
		lock.unlock();
	}
}
/**
 * @param window cast=(GtkWindow *)
 * @param list cast=(GList *)
 */
public static final native void _gtk_window_set_icon_list(int /*long*/ window, int /*long*/ list);
public static final void gtk_window_set_icon_list(int /*long*/ window, int /*long*/ list) {	
	lock.lock();
	try {
		_gtk_window_set_icon_list(window, list);
	} finally {
		lock.unlock();
	}
}
/**
 * @param window cast=(GtkWindow *)
 * @param modal cast=(gboolean)
 */
public static final native void _gtk_window_set_modal(int /*long*/ window, boolean modal);
public static final void gtk_window_set_modal(int /*long*/ window, boolean modal) {
	lock.lock();
	try {
		_gtk_window_set_modal(window, modal);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param window cast=(GtkWindow *)
 */
public static final native void _gtk_window_set_opacity(int /*long*/ window, double opacity);
public static final void gtk_window_set_opacity(int /*long*/ window, double opacity) {
	lock.lock();
	try {
		 _gtk_window_set_opacity(window, opacity);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param widget cast=(GtkWidget *)
 * @param tip_text cast=(const gchar *)
 */
public static final native void _gtk_widget_set_tooltip_text(int /*long*/ widget, byte[] tip_text);
public static final void gtk_widget_set_tooltip_text(int /*long*/ widget, byte[] tip_text) {
	lock.lock();
	try {
		_gtk_widget_set_tooltip_text(widget, tip_text);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(GtkWidget *)
 * @param parent_window cast=(GdkWindow *)
 */
public static final native void _gtk_widget_set_parent_window(int /*long*/ widget, int /*long*/ parent_window);
public static final void gtk_widget_set_parent_window(int /*long*/ widget, int /*long*/ parent_window) {	
	lock.lock();
	try {
		_gtk_widget_set_parent_window(widget, parent_window);
	} finally {
		lock.unlock();
	}
}
/**
 * @param window cast=(GtkWindow *)
 * @param resizable cast=(gboolean)
 */
public static final native void _gtk_window_set_resizable(int /*long*/ window, boolean resizable);
public static final void gtk_window_set_resizable(int /*long*/ window, boolean resizable) {
	lock.lock();
	try {
		_gtk_window_set_resizable(window, resizable);
	} finally {
		lock.unlock();
	}
}
/**
 * @param window cast=(GtkWindow *)
 * @param title cast=(const gchar *)
 */
public static final native void _gtk_window_set_title(int /*long*/ window, byte[] title);
public static final void gtk_window_set_title(int /*long*/ window, byte[] title) {
	lock.lock();
	try {
		_gtk_window_set_title(window, title);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param window cast=(GtkWindow *)
 * @param skips_taskbar cast=(gboolean)
 */
public static final native void _gtk_window_set_skip_taskbar_hint(int /*long*/ window, boolean skips_taskbar);
public static final void gtk_window_set_skip_taskbar_hint(int /*long*/ window, boolean skips_taskbar) {
	lock.lock();
	try {
		_gtk_window_set_skip_taskbar_hint(window, skips_taskbar);
	} finally {
		lock.unlock();
	}
}
/** @param window cast=(GtkWindow *) */
public static final native void _gtk_window_set_type_hint(int /*long*/ window, int hint);
public static final void gtk_window_set_type_hint(int /*long*/ window, int hint) {
	lock.lock();
	try {
		_gtk_window_set_type_hint(window, hint);
	} finally {
		lock.unlock();
	}
}
/**
 * @param window cast=(GtkWindow *)
 * @param parent cast=(GtkWindow *)
 */
public static final native void _gtk_window_set_transient_for(int /*long*/ window, int /*long*/ parent);
public static final void gtk_window_set_transient_for(int /*long*/ window, int /*long*/ parent) {
	lock.lock();
	try {
		_gtk_window_set_transient_for(window, parent);
	} finally {
		lock.unlock();
	}
}
/** @param handle cast=(GtkWindow *) */
public static final native void _gtk_window_unmaximize(int /*long*/ handle);
public static final void gtk_window_unmaximize(int /*long*/ handle) {
	lock.lock();
	try {
		_gtk_window_unmaximize(handle);
	} finally {
		lock.unlock();
	}
}
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param size cast=(size_t)
 */
public static final native void memmove(int /*long*/ dest, GInterfaceInfo src, int size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *),flags=no_out
 */
public static final native void memmove(int /*long*/ dest, GObjectClass src);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param size cast=(size_t)
 */
public static final native void memmove(int /*long*/ dest, GTypeInfo src, int size);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param size cast=(size_t)
 */
public static final native void memmove(int /*long*/ dest, GtkTargetEntry src, int /*long*/ size);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 */
public static final native void memmove(int /*long*/ dest, GtkAdjustment src);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param size cast=(size_t)
 */
public static final native void memmove(int /*long*/ dest, GdkEventButton src, int /*long*/ size);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param size cast=(size_t)
 */
public static final native void memmove(int /*long*/ dest, GdkEventExpose src, int /*long*/ size);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param size cast=(size_t)
 */
public static final native void memmove(int /*long*/ dest, GdkEventMotion src, int /*long*/ size);
/** @param src flags=no_out */
public static final native void memmove(int /*long*/ dest, GtkWidgetClass src);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param size cast=(size_t)
 */
public static final native void memmove(int /*long*/ dest, PangoAttribute src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 */
public static final native void memmove(GObjectClass  dest, int /*long*/ src);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(GTypeQuery dest, int /*long*/ src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 */
public static final native void memmove(GtkColorSelectionDialog dest, int /*long*/ src);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 */
public static final native void memmove(GtkFileSelection dest, int /*long*/ src);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(GdkDragContext dest, int /*long*/ src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(GtkSelectionData dest, int /*long*/ src, int /*long*/ size);
/** @param dest flags=no_in */
public static final native void memmove(GtkWidgetClass dest, int /*long*/ src);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(GtkTargetPair dest, int /*long*/ src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 */
public static final native void memmove(GtkCombo dest, int /*long*/ src);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 */
public static final native void memmove(GtkAdjustment dest, int /*long*/ src);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(GtkBorder dest, int /*long*/ src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(GdkColor dest, int /*long*/ src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(GdkEvent dest, int /*long*/ src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(GdkEventAny dest, int /*long*/ src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(GdkEventButton dest, int /*long*/ src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(GdkEventCrossing dest, int /*long*/ src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(GdkEventExpose dest, int /*long*/ src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(GdkEventFocus dest, int /*long*/ src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(GdkEventKey dest, int /*long*/ src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(GdkEventMotion dest, int /*long*/ src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(GdkEventScroll dest, int /*long*/ src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(GdkEventVisibility dest, int /*long*/ src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(GdkEventWindowState dest, int /*long*/ src, int /*long*/ size);
public static final native void memmove(int /*long*/ dest, GtkCellRendererClass src);
public static final native void memmove(GtkCellRendererClass dest, int /*long*/ src);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 */
public static final native void memmove(GtkFixed dest, int /*long*/ src);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *),flags=no_out
 */
public static final native void memmove(int /*long*/ dest, GtkFixed src);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 */
public static final native void memmove(GdkVisual dest, int /*long*/ src);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 */
public static final native void memmove(GdkImage dest, int /*long*/ src);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(GdkRectangle dest, int /*long*/ src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(PangoAttribute dest, int /*long*/ src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(PangoAttrColor dest, int /*long*/ src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(PangoAttrInt dest, int /*long*/ src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(PangoItem dest, int /*long*/ src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(PangoLayoutLine dest, int /*long*/ src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(PangoLayoutRun dest, int /*long*/ src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param size cast=(size_t)
 */
public static final native void memmove(PangoLogAttr dest, int /*long*/ src, int /*long*/ size);
public static final native int /*long*/ _pango_attr_background_new (short red, short green, short blue);
public static final int /*long*/ pango_attr_background_new (short red, short green, short blue) {
	lock.lock();
	try {
		return _pango_attr_background_new(red, green, blue);
	} finally {
		lock.unlock();
	}
}
/** @param desc cast=(const PangoFontDescription *) */
public static final native int /*long*/ _pango_attr_font_desc_new(int /*long*/ desc);
public static final int /*long*/ pango_attr_font_desc_new(int /*long*/ desc) {
	lock.lock();
	try {
		return _pango_attr_font_desc_new(desc);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _pango_attr_foreground_new (short red, short green, short blue);
public static final int /*long*/ pango_attr_foreground_new (short red, short green, short blue) {
	lock.lock();
	try {
		return _pango_attr_foreground_new(red, green, blue);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _pango_attr_rise_new(int rise);
public static final int /*long*/ pango_attr_rise_new(int rise) {
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
public static final native int /*long*/ _pango_attr_shape_new(PangoRectangle ink_rect, PangoRectangle logical_rect);
public static final int /*long*/ pango_attr_shape_new(PangoRectangle ink_rect, PangoRectangle logical_rect) {
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
public static final native void _pango_attr_list_insert(int /*long*/ list, int /*long*/ attr);
public static final void pango_attr_list_insert(int /*long*/ list, int /*long*/ attr) {
	lock.lock();
	try {
		_pango_attr_list_insert(list, attr);
	} finally {
		lock.unlock();
	}
}
/**
 * @param list cast=(PangoAttrList *)
 * @param attr cast=(PangoAttribute *)
 */
public static final native void _pango_attr_list_change(int /*long*/ list, int /*long*/ attr);
public static final void pango_attr_list_change(int /*long*/ list, int /*long*/ attr) {
	lock.lock();
	try {
		_pango_attr_list_change(list, attr);
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(PangoAttrList *) */
public static final native int /*long*/ _pango_attr_list_get_iterator(int /*long*/ list);
public static final int /*long*/ pango_attr_list_get_iterator(int /*long*/ list) {
	lock.lock();
	try {
		return _pango_attr_list_get_iterator(list);
	} finally {
		lock.unlock();
	}
}
/** @param iterator cast=(PangoAttrIterator *) */
public static final native boolean _pango_attr_iterator_next(int /*long*/ iterator);
public static final boolean pango_attr_iterator_next(int /*long*/ iterator) {
	lock.lock();
	try {
		return _pango_attr_iterator_next(iterator);
	} finally {
		lock.unlock();
	}
}
/** @param iterator cast=(PangoAttrIterator *) */
public static final native void _pango_attr_iterator_range(int /*long*/ iterator, int[] start, int[] end);
public static final void pango_attr_iterator_range(int /*long*/ iterator, int[] start, int[] end) {
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
public static final native int /*long*/ _pango_attr_iterator_get(int /*long*/ iterator, int type);
public static final int /*long*/ pango_attr_iterator_get(int /*long*/ iterator, int type) {
	lock.lock();
	try {
		return _pango_attr_iterator_get(iterator, type);
	} finally {
		lock.unlock();
	}
}
/** @param iterator cast=(PangoAttrIterator *) */
public static final native int /*long*/ _pango_attr_iterator_get_attrs(int /*long*/ iterator);
public static final int /*long*/ pango_attr_iterator_get_attrs(int /*long*/ iterator) {
	lock.lock();
	try {
		return _pango_attr_iterator_get_attrs(iterator);
	} finally {
		lock.unlock();
	}
}

/** @param iterator cast=(PangoAttrIterator *) */
public static final native void _pango_attr_iterator_destroy(int /*long*/ iterator);
public static final void pango_attr_iterator_destroy(int /*long*/ iterator) {
	lock.lock();
	try {
		_pango_attr_iterator_destroy(iterator);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _pango_attr_list_new();
public static final int /*long*/ pango_attr_list_new() {
	lock.lock();
	try {
		return _pango_attr_list_new();
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(PangoAttrList *) */
public static final native void _pango_attr_list_unref(int /*long*/ list);
public static final void pango_attr_list_unref(int /*long*/ list) {
	lock.lock();
	try {
		_pango_attr_list_unref(list);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _pango_attr_strikethrough_color_new(short red, short green, short blue);
public static final int /*long*/ pango_attr_strikethrough_color_new(short red, short green, short blue) {
	lock.lock();
	try {
		return _pango_attr_strikethrough_color_new(red, green, blue);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _pango_attr_strikethrough_new(boolean strikethrough);
public static final int /*long*/ pango_attr_strikethrough_new(boolean strikethrough) {
	lock.lock();
	try {
		return _pango_attr_strikethrough_new(strikethrough);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _pango_attr_underline_color_new(short red, short green, short blue);
public static final int /*long*/ pango_attr_underline_color_new(short red, short green, short blue) {
	lock.lock();
	try {
		return _pango_attr_underline_color_new(red, green, blue);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _pango_attr_underline_new(int underline);
public static final int /*long*/ pango_attr_underline_new(int underline) {
	lock.lock();
	try {
		return _pango_attr_underline_new(underline);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _pango_attr_weight_new(int weight);
public static final int /*long*/ pango_attr_weight_new(int weight) {
	lock.lock();
	try {
		return _pango_attr_weight_new(weight);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _pango_cairo_font_map_get_default();
public static final int /*long*/ pango_cairo_font_map_get_default() {
	lock.lock();
	try {
		return _pango_cairo_font_map_get_default();
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _pango_cairo_font_map_new();
public static final int /*long*/ pango_cairo_font_map_new() {
	lock.lock();
	try {
		return _pango_cairo_font_map_new();
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _pango_cairo_font_map_create_context(int /*long*/ fontmap);
public static final int /*long*/ pango_cairo_font_map_create_context(int /*long*/ fontmap) {
	lock.lock();
	try {
		return _pango_cairo_font_map_create_context(fontmap);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _pango_cairo_create_layout(int /*long*/ cairo);
public static final int /*long*/ pango_cairo_create_layout(int /*long*/ cairo) {
	lock.lock();
	try {
		return _pango_cairo_create_layout(cairo);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param context cast=(PangoContext *)
 */
public static final native int /*long*/ _pango_cairo_context_get_font_options(int /*long*/ context);
public static final int /*long*/ pango_cairo_context_get_font_options(int /*long*/ context) {
	lock.lock();
	try {
		return _pango_cairo_context_get_font_options(context);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param context cast=(PangoContext *)
 */
public static final native void _pango_cairo_context_set_font_options(int /*long*/ context, int /*long*/ options);
public static final void pango_cairo_context_set_font_options(int /*long*/ context, int /*long*/ options) {
	lock.lock();
	try {
		_pango_cairo_context_set_font_options(context, options);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _pango_cairo_font_map_set_resolution(int /*long*/ fontmap, double dpi);
public static final void pango_cairo_font_map_set_resolution(int /*long*/ fontmap, double dpi) {
	lock.lock();
	try {
		_pango_cairo_font_map_set_resolution(fontmap, dpi);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _pango_cairo_layout_path(int /*long*/ cairo, int /*long*/ layout);
public static final void pango_cairo_layout_path(int /*long*/ cairo, int /*long*/ layout) {
	lock.lock();
	try {
		_pango_cairo_layout_path(cairo, layout);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _pango_cairo_show_layout(int /*long*/ cairo, int /*long*/ layout);
public static final void pango_cairo_show_layout(int /*long*/ cairo, int /*long*/ layout) {
	lock.lock();
	try {
		_pango_cairo_show_layout(cairo, layout);
	} finally {
		lock.unlock();
	}
}
/** @param context cast=(PangoContext *) */
public static final native int _pango_context_get_base_dir(int /*long*/ context);
public static final int pango_context_get_base_dir(int /*long*/ context) {
	lock.lock();
	try {
		return _pango_context_get_base_dir(context);
	} finally {
		lock.unlock();
	}
}
/** @param context cast=(PangoContext *) */
public static final native int /*long*/ _pango_context_get_language(int /*long*/ context);
public static final int /*long*/ pango_context_get_language(int /*long*/ context) {
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
public static final native int /*long*/ _pango_context_get_metrics(int /*long*/ context, int /*long*/ desc, int /*long*/ language);
public static final int /*long*/ pango_context_get_metrics(int /*long*/ context, int /*long*/ desc, int /*long*/ language) {
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
public static final native void _pango_context_list_families(int /*long*/ context, int /*long*/[] families, int[] n_families);
public static final void pango_context_list_families(int /*long*/ context, int /*long*/[] families, int[] n_families) {
	lock.lock();
	try {
		_pango_context_list_families(context, families, n_families);
	} finally {
		lock.unlock();
	}
}
/** @param context cast=(PangoContext *) */
public static final native void _pango_context_set_base_dir(int /*long*/ context, int direction);
public static final void pango_context_set_base_dir(int /*long*/ context, int direction) {
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
public static final native void _pango_context_set_language(int /*long*/ context, int /*long*/ language);
public static final void pango_context_set_language(int /*long*/ context, int /*long*/ language) {
	lock.lock();
	try {
		_pango_context_set_language(context, language);
	} finally {
		lock.unlock();
	}
}
/** @param desc cast=(PangoFontDescription *) */
public static final native int /*long*/ _pango_font_description_copy(int /*long*/ desc);
public static final int /*long*/ pango_font_description_copy(int /*long*/ desc) {
	lock.lock();
	try {
		return _pango_font_description_copy(desc);
	} finally {
		lock.unlock();
	}
}
/** @param desc cast=(PangoFontDescription *) */
public static final native void _pango_font_description_free(int /*long*/ desc);
public static final void pango_font_description_free(int /*long*/ desc) {
	lock.lock();
	try {
		_pango_font_description_free(desc);
	} finally {
		lock.unlock();
	}
}
/** @param str cast=(const char *),flags=no_out critical */
public static final native int /*long*/ _pango_font_description_from_string(byte[] str);
public static final int /*long*/ pango_font_description_from_string(byte[] str) {
	lock.lock();
	try {
		return _pango_font_description_from_string(str);
	} finally {
		lock.unlock();
	}
}
/** @param desc cast=(PangoFontDescription *) */
public static final native int /*long*/ _pango_font_description_get_family(int /*long*/ desc);
public static final int /*long*/ pango_font_description_get_family(int /*long*/ desc) {
	lock.lock();
	try {
		return _pango_font_description_get_family(desc);
	} finally {
		lock.unlock();
	}
}
/** @param desc cast=(PangoFontDescription *) */
public static final native int _pango_font_description_get_size(int /*long*/ desc);
public static final int pango_font_description_get_size(int /*long*/ desc) {
	lock.lock();
	try {
		return _pango_font_description_get_size(desc);
	} finally {
		lock.unlock();
	}
}
/** @param desc cast=(PangoFontDescription *) */
public static final native int _pango_font_description_get_style(int /*long*/ desc);
public static final int pango_font_description_get_style(int /*long*/ desc) {
	lock.lock();
	try {
		return _pango_font_description_get_style(desc);
	} finally {
		lock.unlock();
	}
}
/** @param desc cast=(PangoFontDescription *) */
public static final native int _pango_font_description_get_weight(int /*long*/ desc);
public static final int pango_font_description_get_weight(int /*long*/ desc) {
	lock.lock();
	try {
		return _pango_font_description_get_weight(desc);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _pango_font_description_new();
public static final int /*long*/ pango_font_description_new() {
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
public static final native void _pango_font_description_set_family(int /*long*/ desc, byte[] family);
public static final void pango_font_description_set_family(int /*long*/ desc, byte[] family) {
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
public static final native void _pango_font_description_set_size(int /*long*/ desc, int size);
public static final void pango_font_description_set_size(int /*long*/ desc, int size) {
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
public static final native void _pango_font_description_set_stretch(int /*long*/ desc, int stretch);
public static final void pango_font_description_set_stretch(int /*long*/ desc, int stretch) {
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
public static final native void _pango_font_description_set_style(int /*long*/ desc, int weight);
public static final void pango_font_description_set_style(int /*long*/ desc, int weight) {
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
public static final native void _pango_font_description_set_weight(int /*long*/ desc, int weight);
public static final void pango_font_description_set_weight(int /*long*/ desc, int weight) {
	lock.lock();
	try {
		_pango_font_description_set_weight(desc, weight);
	} finally {
		lock.unlock();
	}
}
/** @param desc cast=(PangoFontDescription *) */
public static final native int /*long*/ _pango_font_description_to_string(int /*long*/ desc);
public static final int /*long*/ pango_font_description_to_string(int /*long*/ desc) {
	lock.lock();
	try {
		return _pango_font_description_to_string(desc);
	} finally {
		lock.unlock();
	}
}
/** @param face cast=(PangoFontFace *) */
public static final native int /*long*/ _pango_font_face_describe(int /*long*/ face);
public static final int /*long*/ pango_font_face_describe(int /*long*/ face) {
	lock.lock();
	try {
		return _pango_font_face_describe(face);
	} finally {
		lock.unlock();
	}
}
/** @param family cast=(PangoFontFamily *) */
public static final native int /*long*/ _pango_font_family_get_name(int /*long*/ family);
public static final int /*long*/ pango_font_family_get_name(int /*long*/ family) {
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
public static final native void _pango_font_family_list_faces(int /*long*/ family, int /*long*/[] faces, int[] n_faces);
public static final void pango_font_family_list_faces(int /*long*/ family, int /*long*/[] faces, int[] n_faces) {
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
public static final native int /*long*/ _pango_font_get_metrics(int /*long*/ font, int /*long*/ language);
public static final int /*long*/ pango_font_get_metrics(int /*long*/ font, int /*long*/ language) {
	lock.lock();
	try {
		return _pango_font_get_metrics(font, language);
	} finally {
		lock.unlock();
	}
}
/** @param metrics cast=(PangoFontMetrics *) */
public static final native int _pango_font_metrics_get_approximate_char_width(int /*long*/ metrics);
public static final int pango_font_metrics_get_approximate_char_width(int /*long*/ metrics) {
	lock.lock();
	try {
		return _pango_font_metrics_get_approximate_char_width(metrics);
	} finally {
		lock.unlock();
	}
}
/** @param metrics cast=(PangoFontMetrics *) */
public static final native int _pango_font_metrics_get_ascent(int /*long*/ metrics);
public static final int pango_font_metrics_get_ascent(int /*long*/ metrics) {
	lock.lock();
	try {
		return _pango_font_metrics_get_ascent(metrics);
	} finally {
		lock.unlock();
	}
}
/** @param metrics cast=(PangoFontMetrics *) */
public static final native int _pango_font_metrics_get_descent(int /*long*/ metrics);
public static final int pango_font_metrics_get_descent(int /*long*/ metrics) {
	lock.lock();
	try {
		return _pango_font_metrics_get_descent(metrics);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int _pango_font_metrics_get_underline_thickness(int /*long*/ metrics);
public static final int pango_font_metrics_get_underline_thickness(int /*long*/ metrics) {
	lock.lock();
	try {
		return _pango_font_metrics_get_underline_thickness(metrics);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int _pango_font_metrics_get_underline_position(int /*long*/ metrics);
public static final int pango_font_metrics_get_underline_position(int /*long*/ metrics) {
	lock.lock();
	try {
		return _pango_font_metrics_get_underline_position(metrics);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int _pango_font_metrics_get_strikethrough_thickness(int /*long*/ metrics);
public static final int pango_font_metrics_get_strikethrough_thickness(int /*long*/ metrics) {
	lock.lock();
	try {
		return _pango_font_metrics_get_strikethrough_thickness(metrics);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int _pango_font_metrics_get_strikethrough_position(int /*long*/ metrics);
public static final int pango_font_metrics_get_strikethrough_position(int /*long*/ metrics) {
	lock.lock();
	try {
		return _pango_font_metrics_get_strikethrough_position(metrics);
	} finally {
		lock.unlock();
	}
}
/** @param metrics cast=(PangoFontMetrics *) */
public static final native void _pango_font_metrics_unref(int /*long*/ metrics);
public static final void pango_font_metrics_unref(int /*long*/ metrics) {
	lock.lock();
	try {
		_pango_font_metrics_unref(metrics);
	} finally {
		lock.unlock();
	}
}
/** @param language cast=(const char *),flags=no_out */
public static final native int /*long*/ _pango_language_from_string(byte[] language);
public static final int /*long*/ pango_language_from_string(byte[] language) {
	lock.lock();
	try {
		return _pango_language_from_string(language);
	} finally {
		lock.unlock();
	}
}
/** @param layout cast=(PangoLayout *) */
public static final native void _pango_layout_context_changed(int /*long*/ layout);
public static final void pango_layout_context_changed(int /*long*/ layout) {
	lock.lock();
	try {
		_pango_layout_context_changed(layout);
	} finally {
		lock.unlock();
	}
}
/** @param layout cast=(PangoLayout*) */
public static final native int _pango_layout_get_alignment(int /*long*/ layout);
public static final int pango_layout_get_alignment(int /*long*/ layout) {
	lock.lock();
	try {
		return _pango_layout_get_alignment(layout);
	} finally {
		lock.unlock();
	}
}
/** @param layout cast=(PangoLayout *) */
public static final native int /*long*/ _pango_layout_get_context(int /*long*/ layout);
public static final int /*long*/ pango_layout_get_context(int /*long*/ layout) {
	lock.lock();
	try {
		return _pango_layout_get_context(layout);
	} finally {
		lock.unlock();
	}
}
/** @param layout cast=(PangoLayout *) */
public static final native int /*long*/ _pango_layout_get_attributes(int /*long*/ layout);
public static final int /*long*/ pango_layout_get_attributes(int /*long*/ layout) {
	lock.lock();
	try {
		return _pango_layout_get_attributes(layout);
	} finally {
		lock.unlock();
	}
}
/** @param layout cast=(PangoLayout*) */
public static final native int _pango_layout_get_indent(int /*long*/ layout);
public static final int pango_layout_get_indent(int /*long*/ layout) {
	lock.lock();
	try {
		return _pango_layout_get_indent(layout);
	} finally {
		lock.unlock();
	}
}
/** @param layout cast=(PangoLayout*) */
public static final native int /*long*/ _pango_layout_get_iter(int /*long*/ layout);
public static final int /*long*/ pango_layout_get_iter(int /*long*/ layout) {
	lock.lock();
	try {
		return _pango_layout_get_iter(layout);
	} finally {
		lock.unlock();
	}
}
/** @param layout cast=(PangoLayout*) */
public static final native boolean _pango_layout_get_justify(int /*long*/ layout);
public static final boolean pango_layout_get_justify(int /*long*/ layout) {
	lock.lock();
	try {
		return _pango_layout_get_justify(layout);
	} finally {
		lock.unlock();
	}
}
/** @param layout cast=(PangoLayout *) */
public static final native int /*long*/ _pango_layout_get_line(int /*long*/ layout, int line);
public static final int /*long*/ pango_layout_get_line(int /*long*/ layout, int line) {
	lock.lock();
	try {
		return _pango_layout_get_line(layout, line);
	} finally {
		lock.unlock();
	}
}
/** @param layout cast=(PangoLayout*) */
public static final native int _pango_layout_get_line_count(int /*long*/ layout);
public static final int pango_layout_get_line_count(int /*long*/ layout) {
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
public static final native void _pango_layout_get_log_attrs(int /*long*/ layout, int /*long*/[] attrs, int[] n_attrs);
public static final void pango_layout_get_log_attrs(int /*long*/ layout, int /*long*/[] attrs, int[] n_attrs) {
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
public static final native void _pango_layout_get_size(int /*long*/ layout, int[] width, int[] height);
public static final void pango_layout_get_size(int /*long*/ layout, int[] width, int[] height) {
	lock.lock();
	try {
		_pango_layout_get_size(layout, width, height);
	} finally {
		lock.unlock();
	}
}
/** @param layout cast=(PangoLayout*) */
public static final native int _pango_layout_get_spacing(int /*long*/ layout);
public static final int pango_layout_get_spacing(int /*long*/ layout) {
	lock.lock();
	try {
		return _pango_layout_get_spacing(layout);
	} finally {
		lock.unlock();
	}
}
/** @param layout cast=(PangoLayout*) */
public static final native int /*long*/ _pango_layout_get_tabs(int /*long*/ layout);
public static final int /*long*/ pango_layout_get_tabs(int /*long*/ layout) {
	lock.lock();
	try {
		return _pango_layout_get_tabs(layout);
	} finally {
		lock.unlock();
	}
}
/** @param layout cast=(PangoLayout *) */
public static final native int /*long*/ _pango_layout_get_text(int /*long*/ layout);
public static final int /*long*/ pango_layout_get_text(int /*long*/ layout) {
	lock.lock();
	try {
		return _pango_layout_get_text(layout);
	} finally {
		lock.unlock();
	}
}
/** @param layout cast=(PangoLayout *) */
public static final native int _pango_layout_get_width(int /*long*/ layout);
public static final int pango_layout_get_width(int /*long*/ layout) {
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
public static final native void _pango_layout_index_to_pos(int /*long*/ layout, int index, PangoRectangle pos);
public static final void pango_layout_index_to_pos(int /*long*/ layout, int index, PangoRectangle pos) {
	lock.lock();
	try {
		_pango_layout_index_to_pos(layout, index, pos);
	} finally {
		lock.unlock();
	}
}
/** @param iter cast=(PangoLayoutIter*) */
public static final native void _pango_layout_iter_free(int /*long*/ iter);
public static final void pango_layout_iter_free(int /*long*/ iter) {
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
public static final native void _pango_layout_iter_get_line_extents(int /*long*/ iter, PangoRectangle ink_rect, PangoRectangle logical_rect);
public static final void pango_layout_iter_get_line_extents(int /*long*/ iter, PangoRectangle ink_rect, PangoRectangle logical_rect) {
	lock.lock();
	try {
		_pango_layout_iter_get_line_extents(iter, ink_rect, logical_rect);
	} finally {
		lock.unlock();
	}
}
/** @param iter cast=(PangoLayoutIter*) */
public static final native int _pango_layout_iter_get_index(int /*long*/ iter);
public static final int pango_layout_iter_get_index(int /*long*/ iter) {
	lock.lock();
	try {
		return _pango_layout_iter_get_index(iter);
	} finally {
		lock.unlock();
	}
}
/** @param iter cast=(PangoLayoutIter*) */
public static final native int /*long*/ _pango_layout_iter_get_run(int /*long*/ iter);
public static final int /*long*/ pango_layout_iter_get_run(int /*long*/ iter) {
	lock.lock();
	try {
		return _pango_layout_iter_get_run(iter);
	} finally {
		lock.unlock();
	}
}
/** @param iter cast=(PangoLayoutIter*) */
public static final native boolean _pango_layout_iter_next_line(int /*long*/ iter);
public static final boolean pango_layout_iter_next_line(int /*long*/ iter) {
	lock.lock();
	try {
		return _pango_layout_iter_next_line(iter);
	} finally {
		lock.unlock();
	}
}
/** @param iter cast=(PangoLayoutIter*) */
public static final native boolean _pango_layout_iter_next_run(int /*long*/ iter);
public static final boolean pango_layout_iter_next_run(int /*long*/ iter) {
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
public static final native void _pango_layout_line_get_extents(int /*long*/ line, PangoRectangle ink_rect, PangoRectangle logical_rect);
public static final void pango_layout_line_get_extents(int /*long*/ line, PangoRectangle ink_rect, PangoRectangle logical_rect) {
	lock.lock();
	try {
		_pango_layout_line_get_extents(line, ink_rect, logical_rect);
	} finally {
		lock.unlock();
	}
}
/**
 * @param line cast=(PangoLayoutLine*)
 * @param index_ cast=(int *)
 * @param trailing cast=(int *)
 */
public static final native boolean _pango_layout_line_x_to_index(int /*long*/ line, int x_pos, int[] index_, int[] trailing);
public static final boolean pango_layout_line_x_to_index(int /*long*/ line, int x_pos, int[] index_, int[] trailing) {
	lock.lock();
	try {
		return _pango_layout_line_x_to_index(line, x_pos, index_, trailing);
	} finally {
		lock.unlock();
	}
}
/** @param context cast=(PangoContext *) */
public static final native int /*long*/ _pango_layout_new(int /*long*/ context);
public static final int /*long*/ pango_layout_new(int /*long*/ context) {
	lock.lock();
	try {
		return _pango_layout_new(context);
	} finally {
		lock.unlock();
	}
}
/** @param layout cast=(PangoLayout *) */
public static final native void _pango_layout_set_alignment (int /*long*/ layout, int alignment);
public static final void pango_layout_set_alignment (int /*long*/ layout, int alignment) {
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
public static final native void _pango_layout_set_attributes(int /*long*/ layout, int /*long*/ attrs);
public static final void pango_layout_set_attributes(int /*long*/ layout, int /*long*/ attrs) {
	lock.lock();
	try {
		_pango_layout_set_attributes(layout, attrs);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _pango_layout_set_auto_dir(int /*long*/ layout, boolean auto_dir);
public static final void pango_layout_set_auto_dir(int /*long*/ layout, boolean auto_dir) {
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
public static final native void _pango_layout_set_font_description(int /*long*/ context, int /*long*/ descr);
public static final void pango_layout_set_font_description(int /*long*/ context, int /*long*/ descr) {
	lock.lock();
	try {
		_pango_layout_set_font_description(context, descr);
	} finally {
		lock.unlock();
	}
}
/** @param layout cast=(PangoLayout*) */
public static final native void _pango_layout_set_indent(int /*long*/ layout, int indent);
public static final void pango_layout_set_indent(int /*long*/ layout, int indent) {
	lock.lock();
	try {
		_pango_layout_set_indent(layout, indent);
	} finally {
		lock.unlock();
	}
}
/** @param layout cast=(PangoLayout*) */
public static final native void _pango_layout_set_justify(int /*long*/ layout, boolean justify);
public static final void pango_layout_set_justify(int /*long*/ layout, boolean justify) {
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
public static final native void _pango_layout_set_single_paragraph_mode(int /*long*/ context, boolean setting);
public static final void pango_layout_set_single_paragraph_mode(int /*long*/ context, boolean setting) {
	lock.lock();
	try {
		_pango_layout_set_single_paragraph_mode(context, setting);
	} finally {
		lock.unlock();
	}
}
/** @param layout cast=(PangoLayout *) */
public static final native void _pango_layout_set_spacing(int /*long*/ layout, int spacing);
public static final void pango_layout_set_spacing(int /*long*/ layout, int spacing) {
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
public static final native void _pango_layout_set_tabs(int /*long*/ layout, int /*long*/ tabs);
public static final void pango_layout_set_tabs(int /*long*/ layout, int /*long*/ tabs) {
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
public static final native void _pango_layout_set_text(int /*long*/ layout, byte[] text, int length);
public static final void pango_layout_set_text(int /*long*/ layout, byte[] text, int length) {
	lock.lock();
	try {
		_pango_layout_set_text(layout, text, length);
	} finally {
		lock.unlock();
	}
}
/** @param layout cast=(PangoLayout *) */
public static final native void _pango_layout_set_width(int /*long*/ layout, int width);
public static final void pango_layout_set_width(int /*long*/ layout, int width) {
	lock.lock();
	try {
		_pango_layout_set_width(layout, width);
	} finally {
		lock.unlock();
	}
}
/** @param layout cast=(PangoLayout *) */
public static final native void _pango_layout_set_wrap (int /*long*/ layout, int wrap);
public static final void pango_layout_set_wrap (int /*long*/ layout, int wrap) {
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
public static final native boolean _pango_layout_xy_to_index(int /*long*/ layout, int x, int y, int[] index, int[] trailing);
public static final boolean pango_layout_xy_to_index(int /*long*/ layout, int x, int y, int[] index, int[] trailing) {
	lock.lock();
	try {
		return _pango_layout_xy_to_index(layout, x, y, index, trailing);
	} finally {
		lock.unlock();
	}
}
/** @param tab_array cast=(PangoTabArray *) */
public static final native int _pango_tab_array_get_size(int /*long*/ tab_array);
public static final int pango_tab_array_get_size(int /*long*/ tab_array) {
	lock.lock();
	try {
		return _pango_tab_array_get_size(tab_array);
	} finally {
		lock.unlock();
	}
}
/**
 * @param tab_array cast=(PangoTabArray *)
 * @param alignments cast=(PangoTabAlign **)
 * @param locations cast=(int **)
 */
public static final native void _pango_tab_array_get_tabs(int /*long*/ tab_array, int /*long*/[] alignments, int /*long*/[] locations);
public static final void pango_tab_array_get_tabs(int /*long*/ tab_array, int /*long*/[] alignments, int /*long*/[] locations) {
	lock.lock();
	try {
		_pango_tab_array_get_tabs(tab_array, alignments, locations);
	} finally {
		lock.unlock();
	}
}
/** @param tab_array cast=(PangoTabArray *) */
public static final native void _pango_tab_array_free(int /*long*/ tab_array);
public static final void pango_tab_array_free(int /*long*/ tab_array) {
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
public static final native int /*long*/ _pango_tab_array_new(int initial_size, boolean positions_in_pixels);
public static final int /*long*/ pango_tab_array_new(int initial_size, boolean positions_in_pixels) {
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
public static final native void _pango_tab_array_set_tab(int /*long*/ tab_array, int tab_index, int /*long*/ alignment, int location);
public static final void pango_tab_array_set_tab(int /*long*/ tab_array, int tab_index, int /*long*/ alignment, int location) {
	lock.lock();
	try {
		_pango_tab_array_set_tab(tab_array, tab_index, alignment, location);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=dynamic
 * @param object cast=(AtkObject *)
 * @param relationship cast=(AtkRelationType)
 * @param target cast=(AtkObject *)
 */
public static final native boolean _atk_object_add_relationship (int /*long*/ object, int relationship, int /*long*/ target);
public static final boolean atk_object_add_relationship (int /*long*/ object, int relationship, int /*long*/ target) {
	lock.lock();
	try {
		return _atk_object_add_relationship(object, relationship, target);
	} finally {
		lock.unlock();
	}
}
}
