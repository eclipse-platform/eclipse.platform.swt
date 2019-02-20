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
 * This class contains GTK specific native functions.
 *
 * In contrast to OS.java, dynamic functions are automatically linked, no need to add os_custom.h entries.
 */
public class GTK extends OS {

	/** Constants */
	public static final int GTK_ACCEL_VISIBLE = 0x1;
	public static final int GTK_ALIGN_FILL = 0x0; //Gtk3 GtkAlign Enum
	public static final int GTK_ALIGN_START = 0x1;
	public static final int GTK_ALIGN_END = 0x2;
	public static final int GTK_ALIGN_CENTER = 0x3;
	public static final int GTK_ALIGN_BASELINE = 0x4;
	public static final int GTK_CALENDAR_SHOW_HEADING = 1 << 0;
	public static final int GTK_CALENDAR_SHOW_DAY_NAMES = 1 << 1;
	public static final int GTK_CALENDAR_NO_MONTH_CHANGE = 1 << 2;
	public static final int GTK_CALENDAR_SHOW_WEEK_NUMBERS = 1 << 3;
	public static final int GTK_CALENDAR_WEEK_START_MONDAY = 1 << 4;
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
	public static final int GTK_ENTRY_ICON_PRIMARY = 0;
	public static final int GTK_ENTRY_ICON_SECONDARY = 1;
	public static final int GTK_FILE_CHOOSER_ACTION_OPEN = 0;
	public static final int GTK_FILE_CHOOSER_ACTION_SAVE = 1;
	public static final int GTK_FILE_CHOOSER_ACTION_SELECT_FOLDER = 2;
	public static final int GTK_FRAME_LABEL_PAD = 1;
	public static final int GTK_FRAME_LABEL_SIDE_PAD = 2;
	public static final int GTK_ICON_SIZE_MENU = 1;
	public static final int GTK_ICON_SIZE_SMALL_TOOLBAR = 2;
	public static final int GTK_ICON_SIZE_DIALOG = 6;
	public static final int GTK_ICON_LOOKUP_FORCE_SIZE = 4;
	public static final int GTK_JUSTIFY_CENTER = 0x2;
	public static final int GTK_JUSTIFY_LEFT = 0x0;
	public static final int GTK_JUSTIFY_RIGHT = 0x1;
	public static final int GTK_MAPPED = 1 << 7;
	public static final int GTK_MESSAGE_INFO = 0;
	public static final int GTK_MESSAGE_WARNING = 1;
	public static final int GTK_MESSAGE_QUESTION = 2;
	public static final int GTK_MESSAGE_ERROR = 3;
	public static final int GTK_MOVEMENT_VISUAL_POSITIONS = 1;
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
	public static final int GTK_POLICY_EXTERNAL = 0x3;
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
	public static final int GTK_PRINT_DUPLEX_SIMPLEX = 0;
	public static final int GTK_PRINT_DUPLEX_HORIZONTAL = 1;
	public static final int GTK_PRINT_DUPLEX_VERTICAL = 2;
	public static final int GTK_EVENT_CONTROLLER_SCROLL_NONE = 0;
	public static final int GTK_EVENT_CONTROLLER_SCROLL_VERTICAL = 1;
	public static final int GTK_EVENT_CONTROLLER_SCROLL_HORIZONTAL = 2;
	public static final int GTK_EVENT_CONTROLLER_SCROLL_DISCRETE = 3;
	public static final int GTK_EVENT_CONTROLLER_SCROLL_KINETIC = 4;
	public static final int GTK_EVENT_CONTROLLER_SCROLL_BOTH_AXES = 5;
	public static final int GTK_PHASE_NONE = 0;
	public static final int GTK_PHASE_CAPTURE = 1;
	public static final int GTK_PHASE_BUBBLE = 2;
	public static final int GTK_PHASE_TARGET = 3;
	public static final int GTK_PROGRESS_CONTINUOUS = 0x0;
	public static final int GTK_PROGRESS_DISCRETE = 0x1;
	public static final int GTK_PROGRESS_LEFT_TO_RIGHT = 0x0;
	public static final int GTK_PROGRESS_BOTTOM_TO_TOP = 0x2;
	public static final int GTK_RECEIVES_DEFAULT = 1 << 20;
	public static final int GTK_RELIEF_NONE = 0x2;
	public static final int GTK_RELIEF_NORMAL = 0;
	public static final int GTK_RESPONSE_APPLY = 0xfffffff6;
	public static final int GTK_RESPONSE_CANCEL = 0xfffffffa;
	public static final int GTK_RESPONSE_OK = 0xfffffffb;
	public static final int GTK_RESPONSE_ACCEPT = -3;
	public static final int GTK_RESPONSE_DELETE_EVENT = -4;
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
	public static final int GTK_STATE_FLAG_NORMAL = 0;
	public static final int GTK_STATE_FLAG_ACTIVE = 1 << 0;
	public static final int GTK_STATE_FLAG_PRELIGHT = 1 << 1;
	public static final int GTK_STATE_FLAG_SELECTED = 1 << 2;
	public static final int GTK_STATE_FLAG_INSENSITIVE = 1 << 3;
	public static final int GTK_STATE_FLAG_INCONSISTENT = 1 << 4;
	public static final int GTK_STATE_FLAG_FOCUSED = 1 << 5;
	public static final int GTK_STATE_FLAG_BACKDROP  = 1 << 6;
	public static final int GTK_TEXT_DIR_LTR = 1;
	public static final int GTK_TEXT_DIR_NONE = 0 ;
	public static final int GTK_TEXT_DIR_RTL = 2;
	public static final int GTK_TEXT_WINDOW_TEXT = 2;
	public static final int GTK_TOOLBAR_CHILD_BUTTON = 0x1;
	public static final int GTK_TOOLBAR_CHILD_RADIOBUTTON = 0x3;
	public static final int GTK_TOOLBAR_CHILD_TOGGLEBUTTON = 0x2;
	public static final int GTK_TOOLBAR_ICONS = 0;
	public static final int GTK_TOOLBAR_TEXT = 1;
	public static final int GTK_TOOLBAR_BOTH = 2;
	public static final int GTK_TOOLBAR_BOTH_HORIZ = 3;
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
	public static final int GTK_STYLE_PROVIDER_PRIORITY_APPLICATION = 600;
	public static final int GTK_STYLE_PROVIDER_PRIORITY_USER = 800;
	public static final int GTK_UNIT_PIXEL = 0;
	public static final int GTK_UNIT_POINTS = 1;
	public static final int GTK_UNIT_INCH = 2;
	public static final int GTK_UNIT_MM = 3;
	public static final int GTK_WINDOW_POPUP = 0x1;
	public static final int GTK_WINDOW_TOPLEVEL = 0x0;
	public static final int GTK_WRAP_NONE = 0;
	public static final int GTK_WRAP_WORD = 2;
	public static final int GTK_WRAP_WORD_CHAR = 3;
	public static final int GTK_EXPANDER_COLAPSED = 0;
	public static final int GTK_EXPANDER_SEMI_COLLAPSED = 1;
	public static final int GTK_EXPANDER_SEMI_EXPANDED = 2;
	public static final int GTK_EXPANDER_EXPANDED = 3;

	/** Classes */
	public static final byte[] GTK_STYLE_CLASS_TOOLTIP = OS.ascii("tooltip");
	public static final byte[] GTK_STYLE_CLASS_VIEW = OS.ascii("view");
	public static final byte[] GTK_STYLE_CLASS_CELL = OS.ascii("cell");
	public static final byte[] GTK_STYLE_CLASS_PANE_SEPARATOR = OS.ascii("pane-separator");
	public static final byte[] GTK_STYLE_CLASS_SUGGESTED_ACTION = OS.ascii("suggested-action");
	public static final byte[] GTK_STYLE_CLASS_FRAME = OS.ascii("frame");

	/** Properties */
	public static final byte[] gtk_alternative_button_order = OS.ascii("gtk-alternative-button-order");
	public static final byte[] gtk_cursor_blink = OS.ascii("gtk-cursor-blink");
	public static final byte[] gtk_cursor_blink_time = OS.ascii("gtk-cursor-blink-time");
	public static final byte[] gtk_double_click_time = OS.ascii("gtk-double-click-time");
	public static final byte[] gtk_entry_select_on_focus = OS.ascii("gtk-entry-select-on-focus");
	public static final byte[] gtk_style_property_font = OS.ascii("font");
	public static final byte[] gtk_menu_bar_accel = OS.ascii("gtk-menu-bar-accel");
	public static final byte[] gtk_menu_images = OS.ascii("gtk-menu-images");
	public static final byte[] gtk_theme_name = OS.ascii("gtk-theme-name");


	/** Misc **/
	public static final byte[] GTK_PRINT_SETTINGS_OUTPUT_URI = OS.ascii("output-uri");

	/**
	 * Needed to tell GTK 3 to prefer a dark or light theme in the UI.
	 * Improves the look of the Eclipse Dark theme in GTK 3 systems.
	 */
	public static final byte[] gtk_application_prefer_dark_theme = OS.ascii("gtk-application-prefer-dark-theme");

	/** Named icons.
	 * See https://docs.google.com/spreadsheet/pub?key=0AsPAM3pPwxagdGF4THNMMUpjUW5xMXZfdUNzMXhEa2c&output=html
	 * See http://standards.freedesktop.org/icon-naming-spec/icon-naming-spec-latest.html#names
	 * Icon preview tool: gtk3-icon-browser
	 * Snippets often demonstrate usage of these. E.x 309, 258.
	 * */
	public static final byte[] GTK_NAMED_ICON_GO_UP = OS.ascii ("go-up-symbolic");
	public static final byte[] GTK_NAMED_ICON_GO_DOWN = OS.ascii ("go-down-symbolic");
	public static final byte[] GTK_NAMED_ICON_GO_NEXT = OS.ascii ("go-next-symbolic");
	public static final byte[] GTK_NAMED_ICON_GO_PREVIOUS = OS.ascii ("go-previous-symbolic");
	public static final byte[] GTK_NAMED_LABEL_OK = OS.ascii("_OK");
	public static final byte[] GTK_NAMED_LABEL_CANCEL = OS.ascii("_Cancel");

	public static final int GTK_VERSION = OS.VERSION(GTK.gtk_get_major_version(), GTK.gtk_get_minor_version(), GTK.gtk_get_micro_version());
	public static final boolean GTK4 = GTK_VERSION >= OS.VERSION(3, 94, 0);

	/** SWT Tools translates TYPE_sizeof() into sizeof(TYPE) at native level. os.c will have a binding to functions auto-generated in os_structs.h */
	public static final native int GtkAllocation_sizeof();
	public static final native int GtkBorder_sizeof();
	public static final native int GtkRequisition_sizeof();
	public static final native int GtkTargetEntry_sizeof();
	public static final native int GtkTextIter_sizeof();
	public static final native int GtkCellRendererText_sizeof();
	public static final native int GtkCellRendererTextClass_sizeof();
	public static final native int GtkCellRendererPixbuf_sizeof();
	public static final native int GtkCellRendererPixbufClass_sizeof();
	public static final native int GtkCellRendererToggle_sizeof();
	public static final native int GtkCellRendererToggleClass_sizeof();
	public static final native int GtkTreeIter_sizeof();



	/**
	 * Macros.
	 *
	 * Some of these are not found in dev documentation, only in the sources.
	 */

	/** @param widget cast=(GtkWidget *) */
	public static final native long /*int*/ GTK_WIDGET_GET_CLASS(long /*int*/ widget);
	/** @method flags=const */
	public static final native long /*int*/ GTK_TYPE_TEXT_VIEW_ACCESSIBLE ();
	public static final native long /*int*/ _GTK_ACCESSIBLE (long /*int*/ handle);
	public static final native boolean _GTK_IS_ACCEL_LABEL(long /*int*/ obj);
	public static final boolean GTK_IS_ACCEL_LABEL(long /*int*/ obj) {
		lock.lock();
		try {
			return _GTK_IS_ACCEL_LABEL(obj);
		} finally {
			lock.unlock();
		}
	}
	public static final native boolean _GTK_IS_BUTTON(long /*int*/ obj);
	public static final boolean GTK_IS_BUTTON(long /*int*/ obj) {
		lock.lock();
		try {
			return _GTK_IS_BUTTON(obj);
		} finally {
			lock.unlock();
		}
	}
	public static final native boolean _GTK_IS_LABEL(long /*int*/ obj);
	public static final boolean GTK_IS_LABEL(long /*int*/ obj) {
		lock.lock();
		try {
			return _GTK_IS_LABEL(obj);
		} finally {
			lock.unlock();
		}
	}
	public static final native boolean _GTK_IS_SCROLLED_WINDOW(long /*int*/ obj);
	public static final boolean GTK_IS_SCROLLED_WINDOW(long /*int*/ obj) {
		lock.lock();
		try {
			return _GTK_IS_SCROLLED_WINDOW(obj);
		} finally {
			lock.unlock();
		}
	}
	public static final native boolean _GTK_IS_WINDOW(long /*int*/ obj);
	public static final boolean GTK_IS_WINDOW(long /*int*/ obj) {
		lock.lock();
		try {
			return _GTK_IS_WINDOW(obj);
		} finally {
			lock.unlock();
		}
	}
	public static final native boolean _GTK_IS_CELL_RENDERER_PIXBUF(long /*int*/ obj);
	public static final boolean GTK_IS_CELL_RENDERER_PIXBUF(long /*int*/ obj) {
		lock.lock();
		try {
			return _GTK_IS_CELL_RENDERER_PIXBUF(obj);
		} finally {
			lock.unlock();
		}
	}
	public static final native boolean _GTK_IS_CELL_RENDERER_TEXT(long /*int*/ obj);
	public static final boolean GTK_IS_CELL_RENDERER_TEXT(long /*int*/ obj) {
		lock.lock();
		try {
			return _GTK_IS_CELL_RENDERER_TEXT(obj);
		} finally {
			lock.unlock();
		}
	}
	public static final native boolean _GTK_IS_CELL_RENDERER_TOGGLE(long /*int*/ obj);
	public static final boolean GTK_IS_CELL_RENDERER_TOGGLE(long /*int*/ obj) {
		lock.lock();
		try {
			return _GTK_IS_CELL_RENDERER_TOGGLE(obj);
		} finally {
			lock.unlock();
		}
	}
	public static final native boolean _GTK_IS_CONTAINER(long /*int*/ obj);
	public static final boolean GTK_IS_CONTAINER(long /*int*/ obj) {
		lock.lock();
		try {
			return _GTK_IS_CONTAINER(obj);
		} finally {
			lock.unlock();
		}
	}
	public static final native boolean _GTK_IS_MENU_ITEM(long /*int*/ obj);
	public static final boolean GTK_IS_MENU_ITEM(long /*int*/ obj) {
		lock.lock();
		try {
			return _GTK_IS_MENU_ITEM(obj);
		} finally {
			lock.unlock();
		}
	}
	public static final native boolean _GTK_IS_PLUG(long /*int*/ obj);
	public static final boolean GTK_IS_PLUG(long /*int*/ obj) {
		lock.lock();
		try {
			return _GTK_IS_PLUG(obj);
		} finally {
			lock.unlock();
		}
	}
	/** @method flags=const */
	public static final native long /*int*/ _GTK_TYPE_CELL_RENDERER_TEXT();
	public static final long /*int*/ GTK_TYPE_CELL_RENDERER_TEXT() {
		lock.lock();
		try {
			return _GTK_TYPE_CELL_RENDERER_TEXT();
		} finally {
			lock.unlock();
		}
	}
	/** @method flags=const */
	public static final native long /*int*/ _GTK_TYPE_CELL_RENDERER_PIXBUF();
	public static final long /*int*/ GTK_TYPE_CELL_RENDERER_PIXBUF() {
		lock.lock();
		try {
			return _GTK_TYPE_CELL_RENDERER_PIXBUF();
		} finally {
			lock.unlock();
		}
	}
	/** @method flags=const */
	public static final native long /*int*/ _GTK_TYPE_CELL_RENDERER_TOGGLE();
	public static final long /*int*/ GTK_TYPE_CELL_RENDERER_TOGGLE() {
		lock.lock();
		try {
			return _GTK_TYPE_CELL_RENDERER_TOGGLE();
		} finally {
			lock.unlock();
		}
	}
	/** @method flags=const */
	public static final native long /*int*/ _GTK_TYPE_IM_MULTICONTEXT();
	public static final long /*int*/ GTK_TYPE_IM_MULTICONTEXT() {
		lock.lock();
		try {
			return _GTK_TYPE_IM_MULTICONTEXT();
		} finally {
			lock.unlock();
		}
	}
	/** @method flags=const */
	public static final native long /*int*/ _GTK_TYPE_MENU();
	public static final long /*int*/ GTK_TYPE_MENU() {
		lock.lock();
		try {
			return _GTK_TYPE_MENU();
		} finally {
			lock.unlock();
		}
	}
	/** @method flags=const */
	public static final native long /*int*/ _GTK_TYPE_WIDGET();
	public static final long /*int*/ GTK_TYPE_WIDGET() {
		lock.lock();
		try {
			return _GTK_TYPE_WIDGET();
		} finally {
			lock.unlock();
		}
	}
	/** @method flags=const */
	public static final native long /*int*/ _GTK_TYPE_WINDOW();
	public static final long /*int*/ GTK_TYPE_WINDOW() {
		lock.lock();
		try {
			return _GTK_TYPE_WINDOW();
		} finally {
			lock.unlock();
		}
	}

	// See os_custom.h
	// Dynamically get's the function pointer to gtk_false(). Gtk2/Gtk3.
	public static final native long /*int*/ _GET_FUNCTION_POINTER_gtk_false();
	public static final long /*int*/ GET_FUNCTION_POINTER_gtk_false() {
		lock.lock();
		try {
			return _GET_FUNCTION_POINTER_gtk_false();
		} finally {
			lock.unlock();
		}
	}

	/** @param widget cast=(GtkWidget *) */
	public static final native boolean _gtk_widget_has_default(long /*int*/ widget);
	public static final boolean gtk_widget_has_default(long /*int*/ widget) {
		lock.lock();
		try {
			return _gtk_widget_has_default(widget);
		} finally {
			lock.unlock();
		}
	}

	/** @param widget cast=(GtkWidget *) */
	public static final native boolean _gtk_widget_get_sensitive(long /*int*/ widget);
	public static final boolean gtk_widget_get_sensitive(long /*int*/ widget) {
		lock.lock();
		try {
			return _gtk_widget_get_sensitive(widget);
		} finally {
			lock.unlock();
		}
	}

	/** @param widget cast=(GtkWidget *) */
	public static final native long /*int*/ _gtk_widget_get_first_child(long /*int*/ widget);
	/** [GTK4 only, if-def'd in os.h] */
	public static final long /*int*/ gtk_widget_get_first_child(long /*int*/ widget) {
		lock.lock();
		try {
			return _gtk_widget_get_first_child(widget);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native long /*int*/ _gtk_widget_get_screen(long /*int*/ widget);
	/** [GTK3 only, if-def'd in os.h] */
	public static final long /*int*/ gtk_widget_get_screen(long /*int*/ widget) {
		lock.lock();
		try {
			return _gtk_widget_get_screen(widget);
		} finally {
			lock.unlock();
		}
	}

	/** @param widget cast=(GtkWidget *) */
	public static final native long /*int*/ _gtk_widget_get_name(long /*int*/ widget);
	public static final long /*int*/ gtk_widget_get_name(long /*int*/ widget) {
		lock.lock();
		try {
			return _gtk_widget_get_name(widget);
		} finally {
			lock.unlock();
		}
	}

	/** @method flags=dynamic
	 *  @param widget_class cast=(GtkWidgetClass *)
	 */
	public static final native long /*int*/ _gtk_widget_class_get_css_name(long /*int*/ widget_class);
	public static final long /*int*/ gtk_widget_class_get_css_name(long /*int*/ widget_class) {
		lock.lock();
		try {
			return _gtk_widget_class_get_css_name(widget_class);
		} finally {
			lock.unlock();
		}
	}

	/** @param button cast=(GtkButton *) */
	public static final native void _gtk_button_clicked(long /*int*/ button);
	public static final void gtk_button_clicked(long /*int*/ button) {
		lock.lock();
		try {
			_gtk_button_clicked(button);
		} finally {
			lock.unlock();
		}
	}

	public static final native long /*int*/ _gtk_button_new();
	public static final long /*int*/ gtk_button_new() {
		lock.lock();
		try {
			return _gtk_button_new();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * @method flags=dynamic
	 * @param button cast=(GtkButton *)
	 * @param image cast=(GtkWidget *)
	 */
	public static final native void /*int*/ _gtk_button_set_image(long /*int*/ button, long /*int*/ image);
	/** [GTK3 only] */
	public static final void /*int*/ gtk_button_set_image(long /*int*/ button, long /*int*/ image) {
		lock.lock();
		try {
			_gtk_button_set_image(button, image);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * @method flags=dynamic
	 * @param accel_label cast=(GtkAccelLabel *)
	 * @param accel_key cast=(guint)
	 * @param accel_mods cast=(GdkModifierType)
	 */
	public static final native void _gtk_accel_label_set_accel(long /*int*/ accel_label, int accel_key, int accel_mods);
	public static final void gtk_accel_label_set_accel(long /*int*/ accel_label, int accel_key, int accel_mods) {
		lock.lock();
		try {
			_gtk_accel_label_set_accel(accel_label, accel_key, accel_mods);
		} finally {
			lock.unlock();
		}
	}
	public static final native int _gtk_accelerator_get_default_mod_mask();
	public static final int gtk_accelerator_get_default_mod_mask() {
		lock.lock();
		try {
			return _gtk_accelerator_get_default_mod_mask();
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param accelerator cast=(const gchar *)
	 * @param accelerator_key cast=(guint *)
	 * @param accelerator_mods cast=(GdkModifierType *)
	 */
	public static final native void _gtk_accelerator_parse(long /*int*/ accelerator, int [] accelerator_key, int [] accelerator_mods);
	public static final void gtk_accelerator_parse(long /*int*/ accelerator, int [] accelerator_key, int [] accelerator_mods) {
		lock.lock();
		try {
			_gtk_accelerator_parse(accelerator, accelerator_key, accelerator_mods);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_accel_group_new();
	public static final long /*int*/ gtk_accel_group_new() {
		lock.lock();
		try {
			return _gtk_accel_group_new();
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param accel_label cast=(GtkAccelLabel *)
	 * @param accel_widget cast=(GtkWidget *)
	 */
	public static final native void _gtk_accel_label_set_accel_widget(long /*int*/ accel_label, long /*int*/ accel_widget);
	public static final void gtk_accel_label_set_accel_widget(long /*int*/ accel_label, long /*int*/ accel_widget) {
		lock.lock();
		try {
			_gtk_accel_label_set_accel_widget(accel_label, accel_widget);
		} finally {
			lock.unlock();
		}
	}
	/** @param label cast=(const gchar *) */
	public static final native long /*int*/ _gtk_accel_label_new(byte[] label);
	public static final long /*int*/ gtk_accel_label_new(byte[] label) {
		lock.lock();
		try {
			return _gtk_accel_label_new(label);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param accessible cast=(GtkAccessible *)
	 */
	public static final native long /*int*/ _gtk_accessible_get_widget(long /*int*/ accessible);
	public static final long /*int*/ gtk_accessible_get_widget(long /*int*/ accessible) {
		lock.lock();
		try {
			return _gtk_accessible_get_widget(accessible);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param adjustment cast=(GtkAdjustment *)
	 */
	public static final native void _gtk_adjustment_configure(long /*int*/ adjustment, double value, double lower, double upper, double step_increment, double page_increment, double page_size);
	public static final void gtk_adjustment_configure(long /*int*/ adjustment, double value, double lower, double upper, double step_increment, double page_increment, double page_size) {
		lock.lock();
		try {
			_gtk_adjustment_configure(adjustment, value, lower, upper, step_increment, page_increment, page_size);
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
	public static final native long /*int*/ _gtk_adjustment_new(double value, double lower, double upper, double step_increment, double page_increment, double page_size);
	public static final long /*int*/ gtk_adjustment_new(double value, double lower, double upper, double step_increment, double page_increment, double page_size) {
		lock.lock();
		try {
			return _gtk_adjustment_new(value, lower, upper, step_increment, page_increment, page_size);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param adjustment cast=(GtkAdjustment *)
	 */
	public static final native double _gtk_adjustment_get_lower(long /*int*/ adjustment);
	public static final double gtk_adjustment_get_lower(long /*int*/ adjustment) {
		lock.lock();
		try {
			return _gtk_adjustment_get_lower(adjustment);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param adjustment cast=(GtkAdjustment *)
	 */
	public static final native double _gtk_adjustment_get_page_increment(long /*int*/ adjustment);
	public static final double gtk_adjustment_get_page_increment(long /*int*/ adjustment) {
		lock.lock();
		try {
			return _gtk_adjustment_get_page_increment(adjustment);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param adjustment cast=(GtkAdjustment *)
	 */
	public static final native double _gtk_adjustment_get_page_size(long /*int*/ adjustment);
	public static final double gtk_adjustment_get_page_size(long /*int*/ adjustment) {
		lock.lock();
		try {
			return _gtk_adjustment_get_page_size(adjustment);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param adjustment cast=(GtkAdjustment *)
	 */
	public static final native double _gtk_adjustment_get_step_increment(long /*int*/ adjustment);
	public static final double gtk_adjustment_get_step_increment(long /*int*/ adjustment) {
		lock.lock();
		try {
			return _gtk_adjustment_get_step_increment(adjustment);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param adjustment cast=(GtkAdjustment *)
	 */
	public static final native double _gtk_adjustment_get_upper(long /*int*/ adjustment);
	public static final double gtk_adjustment_get_upper(long /*int*/ adjustment) {
		lock.lock();
		try {
			return _gtk_adjustment_get_upper(adjustment);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param adjustment cast=(GtkAdjustment *)
	 */
	public static final native double _gtk_adjustment_get_value(long /*int*/ adjustment);
	public static final double gtk_adjustment_get_value(long /*int*/ adjustment) {
		lock.lock();
		try {
			return _gtk_adjustment_get_value(adjustment);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param adjustment cast=(GtkAdjustment *)
	 * @param value cast=(gdouble)
	 */
	public static final native void _gtk_adjustment_set_value(long /*int*/ adjustment, double value);
	public static final void gtk_adjustment_set_value(long /*int*/ adjustment, double value) {
		lock.lock();
		try {
			_gtk_adjustment_set_value(adjustment, value);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param adjustment cast=(GtkAdjustment *)
	 * @param value cast=(gdouble)
	 */
	public static final native void _gtk_adjustment_set_step_increment(long /*int*/ adjustment, double value);
	public static final void gtk_adjustment_set_step_increment(long /*int*/ adjustment, double value) {
		lock.lock();
		try {
			_gtk_adjustment_set_step_increment(adjustment, value);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param adjustment cast=(GtkAdjustment *)
	 * @param value cast=(gdouble)
	 */
	public static final native void _gtk_adjustment_set_page_increment(long /*int*/ adjustment, double value);
	public static final void gtk_adjustment_set_page_increment(long /*int*/ adjustment, double value) {
		lock.lock();
		try {
			_gtk_adjustment_set_page_increment(adjustment, value);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param adjustment cast=(GtkAdjustment *)
	 * */
	public static final native void _gtk_adjustment_value_changed(long /*int*/ adjustment);
	/**  [GTK2/GTK3; 3.18 deprecated, replaced] */
	public static final void gtk_adjustment_value_changed(long /*int*/ adjustment) {
		lock.lock();
		try {
			_gtk_adjustment_value_changed(adjustment);
		} finally {
			lock.unlock();
		}
	}
	/** @param bin cast=(GtkBin *) */
	public static final native long /*int*/ _gtk_bin_get_child(long /*int*/ bin);
	public static final long /*int*/ gtk_bin_get_child(long /*int*/ bin) {
		lock.lock();
		try {
			return _gtk_bin_get_child(bin);
		} finally {
			lock.unlock();
		}
	}
	/** @param border cast=(GtkBorder *) */
	public static final native void _gtk_border_free(long /*int*/ border);
	public static final void gtk_border_free(long /*int*/ border) {
		lock.lock();
		try {
			_gtk_border_free(border);
		} finally {
			lock.unlock();
		}
	}
	/** @param box cast=(GtkBox *) */
	public static final native void _gtk_box_set_spacing(long /*int*/ box, int spacing);
	public static final void gtk_box_set_spacing(long /*int*/ box, int spacing) {
		lock.lock();
		try {
			_gtk_box_set_spacing(box, spacing);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param box cast=(GtkBox *)
	 * @param child cast=(GtkWidget *)
	 */
	public static final native void _gtk_box_set_child_packing(long /*int*/ box, long /*int*/ child, boolean expand, boolean fill, int padding, int pack_type);
	/** [GTK3 only] */
	public static final void gtk_box_set_child_packing(long /*int*/ box, long /*int*/ child, boolean expand, boolean fill, int padding, int pack_type) {
		lock.lock();
		try {
			_gtk_box_set_child_packing(box, child, expand, fill, padding, pack_type);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param box cast=(GtkBox *)
	 * @param child cast=(GtkWidget *)
	 */
	public static final native void _gtk_box_set_child_packing(long /*int*/ box, long /*int*/ child, int pack_type);
	/** [GTK4 only] */
	public static final void gtk_box_set_child_packing(long /*int*/ box, long /*int*/ child, int pack_type) {
		lock.lock();
		try {
			_gtk_box_set_child_packing(box, child, pack_type);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_calendar_new();
	public static final long /*int*/ gtk_calendar_new() {
		lock.lock();
		try {
			return _gtk_calendar_new();
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param calendar cast=(GtkCalendar *)
	 * @param month cast=(guint)
	 * @param year cast=(guint)
	 */
	public static final native void /*long*/ _gtk_calendar_select_month(long /*int*/ calendar, int month, int year);
	public static final void /*long*/ gtk_calendar_select_month(long /*int*/ calendar, int month, int year) {
		lock.lock();
		try {
			_gtk_calendar_select_month(calendar, month, year);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param calendar cast=(GtkCalendar *)
	 * @param day cast=(guint)
	 */
	public static final native void _gtk_calendar_select_day(long /*int*/ calendar, int day);
	public static final void gtk_calendar_select_day(long /*int*/ calendar, int day) {
		lock.lock();
		try {
			_gtk_calendar_select_day(calendar, day);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param calendar cast=(GtkCalendar *)
	 * @param day cast=(guint)
	 */
	public static final native void _gtk_calendar_mark_day(long /*int*/ calendar, int day);
	public static final void gtk_calendar_mark_day(long /*int*/ calendar, int day) {
		lock.lock();
		try {
			_gtk_calendar_mark_day(calendar, day);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param calendar cast=(GtkCalendar *)
	 */
	public static final native void _gtk_calendar_clear_marks(long /*int*/ calendar);
	public static final void gtk_calendar_clear_marks(long /*int*/ calendar) {
		lock.lock();
		try {
			_gtk_calendar_clear_marks(calendar);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param calendar cast=(GtkCalendar *)
	 * @param flags cast=(GtkCalendarDisplayOptions)
	 */
	public static final native void _gtk_calendar_set_display_options(long /*int*/ calendar, int flags);
	public static final void gtk_calendar_set_display_options(long /*int*/ calendar, int flags) {
		lock.lock();
		try {
			_gtk_calendar_set_display_options(calendar, flags);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param calendar cast=(GtkCalendar *)
	 * @param year cast=(guint *)
	 * @param month cast=(guint *)
	 * @param day cast=(guint *)
	 */
	public static final native void _gtk_calendar_get_date(long /*int*/ calendar, int[] year, int[] month, int[] day);
	public static final void gtk_calendar_get_date(long /*int*/ calendar, int[] year, int[] month, int[] day) {
		lock.lock();
		try {
			_gtk_calendar_get_date(calendar, year, month, day);
		} finally {
			lock.unlock();
		}
	}
	/** @param cell_layout cast=(GtkCellLayout *) */
	public static final native void _gtk_cell_layout_clear(long /*int*/ cell_layout);
	public static final void gtk_cell_layout_clear(long /*int*/ cell_layout) {
		lock.lock();
		try {
			_gtk_cell_layout_clear(cell_layout);
		} finally {
			lock.unlock();
		}
	}
	/** @param cell_layout cast=(GtkCellLayout *) */
	public static final native long /*int*/ _gtk_cell_layout_get_cells(long /*int*/ cell_layout);
	public static final long /*int*/ gtk_cell_layout_get_cells(long /*int*/ cell_layout) {
		lock.lock();
		try {
			return _gtk_cell_layout_get_cells(cell_layout);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param cell_layout cast=(GtkCellLayout *)
	 * @param cell cast=(GtkCellRenderer *)
	 * @param sentinel cast=(const gchar *),flags=sentinel
	 */
	public static final native void _gtk_cell_layout_set_attributes(long /*int*/ cell_layout, long /*int*/ cell, byte[] attribute, int column, long /*int*/ sentinel);
	public static final void gtk_cell_layout_set_attributes(long /*int*/ cell_layout, long /*int*/ cell, byte[] attribute, int column, long /*int*/ sentinel) {
		lock.lock();
		try {
			_gtk_cell_layout_set_attributes(cell_layout, cell, attribute, column, sentinel);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param cell_layout cast=(GtkCellLayout *)
	 * @param cell cast=(GtkCellRenderer *)
	 */
	public static final native void _gtk_cell_layout_pack_start(long /*int*/ cell_layout, long /*int*/ cell, boolean expand);
	public static final void gtk_cell_layout_pack_start(long /*int*/ cell_layout, long /*int*/ cell, boolean expand) {
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
	 * @param minimum_size cast=(GtkRequisition *)
	 * @param natural_size cast=(GtkRequisition *)
	 */
	public static final native void _gtk_cell_renderer_get_preferred_size(long /*int*/ cell, long /*int*/ widget, GtkRequisition minimum_size, GtkRequisition natural_size);
	public static final void gtk_cell_renderer_get_preferred_size(long /*int*/ cell, long /*int*/ widget, GtkRequisition minimum_size, GtkRequisition natural_size) {
		lock.lock();
		try {
			_gtk_cell_renderer_get_preferred_size(cell, widget, minimum_size, natural_size);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param cell cast=(GtkCellRenderer *)
	 * @param xpad cast=(gint *)
	 * @param ypad cast=(gint *)
	 */
	public static final native void _gtk_cell_renderer_get_padding(long /*int*/ cell, int [] xpad, int [] ypad);
	public static final void gtk_cell_renderer_get_padding(long /*int*/ cell, int [] xpad, int [] ypad) {
		lock.lock();
		try {
			_gtk_cell_renderer_get_padding(cell, xpad, ypad);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param cell cast=(GtkCellRenderer *)
	 * @param widget cast=(GtkWidget *)
	 * @param width cast=(gint)
	 * @param minimum_height cast=(gint *)
	 * @param natural_height cast=(gint *)
	 */
	public static final native void _gtk_cell_renderer_get_preferred_height_for_width(long /*int*/ cell, long /*int*/ widget, int width, int[] minimum_height, int[] natural_height);
	public static final void gtk_cell_renderer_get_preferred_height_for_width(long /*int*/ cell, long /*int*/ widget, int width, int[] minimum_height, int[] natural_height) {
		lock.lock();
		try {
			_gtk_cell_renderer_get_preferred_height_for_width(cell, widget, width, minimum_height, natural_height);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param cell cast=(GtkCellRenderer *)
	 * @param width cast=(gint)
	 * @param height cast=(gint)
	 */
	public static final native void _gtk_cell_renderer_set_fixed_size(long /*int*/ cell, int width, int height);
	public static final void gtk_cell_renderer_set_fixed_size (long /*int*/ cell, int width, int height) {
		lock.lock();
		try {
			_gtk_cell_renderer_set_fixed_size(cell, width, height);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param cell cast=(GtkCellRenderer *)
	 * @param width cast=(gint *)
	 * @param height cast=(gint *)
	 */
	public static final native void _gtk_cell_renderer_get_fixed_size(long /*int*/ cell, int[] width, int[] height);
	public static final void gtk_cell_renderer_get_fixed_size (long /*int*/ cell, int[] width, int[] height) {
		lock.lock();
		try {
			_gtk_cell_renderer_get_fixed_size(cell, width, height);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param minimum_size cast=(GtkRequisition *)
	 * @param natural_size cast=(GtkRequisition *)
	 */
	public static final native void _gtk_widget_get_preferred_size(long /*int*/ widget, GtkRequisition minimum_size, GtkRequisition natural_size);
	public static final void gtk_widget_get_preferred_size(long /*int*/ widget, GtkRequisition minimum_size, GtkRequisition natural_size) {
		lock.lock();
		try {
			_gtk_widget_get_preferred_size(widget, minimum_size, natural_size);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 * @param width cast=(gint)
	 * @param minimum_size cast=(gint *)
	 * @param natural_size cast=(gint *)
	 */
	public static final native void _gtk_widget_get_preferred_height_for_width(long /*int*/ widget, int width, int[] minimum_size, int[] natural_size);
	/** [GTK3 only] */
	public static final void gtk_widget_get_preferred_height_for_width(long /*int*/ widget, int width, int[] minimum_size, int[] natural_size) {
		lock.lock();
		try {
			_gtk_widget_get_preferred_height_for_width(widget, width, minimum_size, natural_size);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 * @param minimum_size cast=(gint *)
	 * @param natural_size cast=(gint *)
	 */
	public static final native void _gtk_widget_get_preferred_height(long /*int*/ widget, int[] minimum_size, int[] natural_size);
	/** [GTK3 only] */
	public static final void gtk_widget_get_preferred_height(long /*int*/ widget, int[] minimum_size, int[] natural_size) {
		lock.lock();
		try {
			_gtk_widget_get_preferred_height(widget, minimum_size, natural_size);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 * @param height cast=(gint)
	 * @param minimum_size cast=(gint *)
	 * @param natural_size cast=(gint *)
	 */
	public static final native void _gtk_widget_get_preferred_width_for_height(long /*int*/ widget, int height, int[] minimum_size, int[] natural_size);
	/** [GTK3 only] */
	public static final void gtk_widget_get_preferred_width_for_height(long /*int*/ widget, int height, int[] minimum_size, int[] natural_size) {
		lock.lock();
		try {
			_gtk_widget_get_preferred_width_for_height(widget, height, minimum_size, natural_size);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_cell_renderer_pixbuf_new();
	public static final long /*int*/ gtk_cell_renderer_pixbuf_new() {
		lock.lock();
		try {
			return _gtk_cell_renderer_pixbuf_new();
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_cell_renderer_text_new();
	public static final long /*int*/ gtk_cell_renderer_text_new() {
		lock.lock();
		try {
			return _gtk_cell_renderer_text_new();
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_cell_renderer_toggle_new();
	public static final long /*int*/ gtk_cell_renderer_toggle_new() {
		lock.lock();
		try {
			return _gtk_cell_renderer_toggle_new();
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param cell_view cast=(GtkCellView *)
	 * @param fit_model cast=(gboolean)
	 */
	public static final native void _gtk_cell_view_set_fit_model(long /*int*/ cell_view, boolean fit_model);
	public static final void gtk_cell_view_set_fit_model(long /*int*/ cell_view, boolean fit_model) {
		lock.lock();
		try {
			_gtk_cell_view_set_fit_model(cell_view, fit_model);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_check_button_new();
	public static final long /*int*/ gtk_check_button_new() {
		lock.lock();
		try {
			return _gtk_check_button_new();
		} finally {
			lock.unlock();
		}
	}
	/** @param check_menu_item cast=(GtkCheckMenuItem *) */
	public static final native boolean _gtk_check_menu_item_get_active(long /*int*/ check_menu_item);
	public static final boolean gtk_check_menu_item_get_active(long /*int*/ check_menu_item) {
		lock.lock();
		try {
			return _gtk_check_menu_item_get_active(check_menu_item);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_check_menu_item_new();
	public static final long /*int*/ gtk_check_menu_item_new() {
		lock.lock();
		try {
			return _gtk_check_menu_item_new();
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param wid cast=(GtkCheckMenuItem *)
	 * @param active cast=(gboolean)
	 */
	public static final native void _gtk_check_menu_item_set_active(long /*int*/ wid, boolean active);
	public static final void gtk_check_menu_item_set_active(long /*int*/ wid, boolean active) {
		lock.lock();
		try {
			_gtk_check_menu_item_set_active(wid, active);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_check_version(int required_major, int required_minor, int required_micro);
	public static final long /*int*/ gtk_check_version(int required_major, int required_minor, int required_micro) {
		lock.lock();
		try {
			return _gtk_check_version(required_major, required_minor, required_micro);
		} finally {
			lock.unlock();
		}
	}
	/** @param clipboard cast=(GtkClipboard *) */
	public static final native void _gtk_clipboard_clear(long /*int*/ clipboard);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gtk_clipboard_clear(long /*int*/ clipboard) {
		lock.lock();
		try {
			_gtk_clipboard_clear(clipboard);
		} finally {
			lock.unlock();
		}
	}
	/** @param selection cast=(GdkAtom) */
	public static final native long /*int*/ _gtk_clipboard_get(long /*int*/ selection);
	/** [GTK3 only, if-def'd in os.h] */
	public static final long /*int*/ gtk_clipboard_get(long /*int*/ selection) {
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
	public static final native boolean _gtk_clipboard_set_with_owner(long /*int*/ clipboard, long /*int*/ target, int n_targets, long /*int*/ get_func, long /*int*/ clear_func, long /*int*/ user_data);
	/** [GTK3 only, if-def'd in os.h] */
	public static final boolean gtk_clipboard_set_with_owner(long /*int*/ clipboard, long /*int*/ target, int n_targets, long /*int*/ get_func, long /*int*/ clear_func, long /*int*/ user_data) {
		lock.lock();
		try {
			return _gtk_clipboard_set_with_owner(clipboard, target, n_targets, get_func, clear_func, user_data);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param clipboard cast=(GtkClipboard *)
	 * @param targets cast=(const GtkTargetEntry *)
	 * @param n_targets cast=(gint)
	 */
	public static final native void _gtk_clipboard_set_can_store(long /*int*/ clipboard, long /*int*/ targets, int n_targets);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gtk_clipboard_set_can_store(long /*int*/ clipboard, long /*int*/ targets, int n_targets) {
		lock.lock();
		try {
			_gtk_clipboard_set_can_store(clipboard, targets, n_targets);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param clipboard cast=(GtkClipboard *)
	 */
	public static final native void _gtk_clipboard_store(long /*int*/ clipboard);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gtk_clipboard_store(long /*int*/ clipboard) {
		lock.lock();
		try {
			_gtk_clipboard_store(clipboard);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param clipboard cast=(GtkClipboard *)
	 * @param target cast=(GdkAtom)
	 */
	public static final native long /*int*/ _gtk_clipboard_wait_for_contents(long /*int*/ clipboard, long /*int*/ target);
	/** [GTK3 only, if-def'd in os.h] */
	public static final long /*int*/ gtk_clipboard_wait_for_contents(long /*int*/ clipboard, long /*int*/ target) {
		lock.lock();
		try {
			return _gtk_clipboard_wait_for_contents(clipboard, target);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param chooser cast=(GtkColorChooser *)
	 * @param orientation cast=(GtkOrientation)
	 * @param colors_per_line cast=(gint)
	 * @param n_colors cast(gint)
	 * @param colors cast=(GdkRGBA *)
	 */
	public static final native void _gtk_color_chooser_add_palette(long /*int*/ chooser, int orientation, int colors_per_line, int n_colors, long /*int*/ colors);
	public static final void gtk_color_chooser_add_palette(long /*int*/ chooser, int orientation, int colors_per_line, int n_colors, long /*int*/ colors) {
		lock.lock();
		try {
			_gtk_color_chooser_add_palette(chooser, orientation, colors_per_line, n_colors, colors);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param title cast=(const gchar *)
	 * @param parent cast=(GtkWindow *)
	 */
	public static final native long /*int*/ _gtk_color_chooser_dialog_new (byte[] title, long /*int*/ parent);
	public static final long /*int*/  gtk_color_chooser_dialog_new (byte[] title, long /*int*/ parent) {
		lock.lock();
		try {
			return _gtk_color_chooser_dialog_new (title, parent);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param chooser cast=(GtkColorChooser *)
	 * @param use_alpha cast=(gboolean)
	 */
	public static final native void _gtk_color_chooser_set_use_alpha (long /*int*/ chooser, boolean use_alpha);
	public static final void  gtk_color_chooser_set_use_alpha (long /*int*/ chooser, boolean use_alpha) {
		lock.lock();
		try {
			 _gtk_color_chooser_set_use_alpha (chooser, use_alpha);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param chooser cast=(GtkColorChooser *)
	 */
	public static final native boolean _gtk_color_chooser_get_use_alpha (long /*int*/ chooser);
	public static final boolean  gtk_color_chooser_get_use_alpha (long /*int*/ chooser) {
		lock.lock();
		try {
			 return _gtk_color_chooser_get_use_alpha (chooser);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param chooser cast=(GtkColorChooser *)
	 * @param color cast=(GdkRGBA *)
	 */
	public static final native void _gtk_color_chooser_set_rgba(long /*int*/ chooser, GdkRGBA color);
	public static final void  gtk_color_chooser_get_rgba(long /*int*/ chooser, GdkRGBA color) {
		lock.lock();
		try {
			 _gtk_color_chooser_get_rgba(chooser, color);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param chooser cast=(GtkColorChooser *)
	 * @param color cast=(GdkRGBA *)
	 */
	public static final native void _gtk_color_chooser_get_rgba(long /*int*/ chooser, GdkRGBA color);
	public static final void  gtk_color_chooser_set_rgba(long /*int*/ chooser, GdkRGBA color) {
		lock.lock();
		try {
			 _gtk_color_chooser_set_rgba(chooser, color);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param combo cast=(GtkComboBox *)
	 * @param val cast=(gboolean)
	 */
	public static final native void _gtk_combo_box_set_focus_on_click(long /*int*/ combo, boolean val);
	/** [GTK2/GTK3; 3.20 deprecated, replaced] */
	public static final void gtk_combo_box_set_focus_on_click(long /*int*/ combo, boolean val) {
		lock.lock();
		try {
			_gtk_combo_box_set_focus_on_click(combo, val);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_combo_box_text_new();
	public static final long /*int*/ gtk_combo_box_text_new() {
		lock.lock();
		try {
			return _gtk_combo_box_text_new();
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_combo_box_text_new_with_entry();
	public static final long /*int*/ gtk_combo_box_text_new_with_entry() {
		lock.lock();
		try {
			return _gtk_combo_box_text_new_with_entry();
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param combo_box cast=(GtkComboBoxText *)
	 * @param position cast=(gint)
	 * @param id cast=(const gchar *)
	 * @param text cast=(const gchar *)
	 */
	public static final native void _gtk_combo_box_text_insert(long /*int*/ combo_box, int position, byte[] id, byte[] text);
	/** Do not call directly, instead use Combo.gtk_combo_box_insert(..) */
	public static final void gtk_combo_box_text_insert(long /*int*/ combo_box, int position, byte[] id, byte[] text) {
		lock.lock();
		try {
			_gtk_combo_box_text_insert(combo_box, position, id, text);
		} finally {
			lock.unlock();
		}
	}
	/** @param combo_box cast=(GtkComboBoxText *) */
	public static final native void _gtk_combo_box_text_remove(long /*int*/ combo_box, int position);
	public static final void gtk_combo_box_text_remove(long /*int*/ combo_box, int position) {
		lock.lock();
		try {
			_gtk_combo_box_text_remove(combo_box, position);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param combo_box cast=(GtkComboBoxText *)
	 */
	public static final native void _gtk_combo_box_text_remove_all(long /*int*/ combo_box);
	/** Do not call directly. Call Combo.gtk_combo_box_text_remove_all(..) instead). */
	public static final void gtk_combo_box_text_remove_all(long /*int*/ combo_box) {
		lock.lock();
		try {
			_gtk_combo_box_text_remove_all(combo_box);
		} finally {
			lock.unlock();
		}
	}
	/**
	* @param combo_box cast=(GtkComboBox *)
	*/
	public static final native int _gtk_combo_box_get_active(long /*int*/ combo_box);
	public static final int gtk_combo_box_get_active(long /*int*/ combo_box) {
		lock.lock();
		try {
			return _gtk_combo_box_get_active(combo_box);
		} finally {
			lock.unlock();
		}
	}
	/**
	* @param combo_box cast=(GtkComboBox *)
	*/
	public static final native long /*int*/ _gtk_combo_box_get_model(long /*int*/ combo_box);
	public static final long /*int*/ gtk_combo_box_get_model(long /*int*/ combo_box) {
		lock.lock();
		try {
			return _gtk_combo_box_get_model(combo_box);
		} finally {
			lock.unlock();
		}
	}
	/**
	* @param combo_box cast=(GtkComboBox *)
	* @param index cast=(gint)
	*/
	public static final native void _gtk_combo_box_set_active(long /*int*/ combo_box, int index);
	public static final void gtk_combo_box_set_active(long /*int*/ combo_box, int index) {
		lock.lock();
		try {
			_gtk_combo_box_set_active(combo_box, index);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param combo_box cast=(GtkComboBox *)
	 * @param width cast=(gint)
	 */
	public static final native void _gtk_combo_box_set_wrap_width(long /*int*/ combo_box, int width);
	/**
	 * Do not use directly. Instead use Combo.gtk_combo_box_toggle_wrap(..)
	 */
	public static final void gtk_combo_box_set_wrap_width(long /*int*/ combo_box, int width) {
		lock.lock();
		try {
			_gtk_combo_box_set_wrap_width(combo_box, width);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * @param combo_box cast=(GtkComboBox *)
	 * @return cast=(gint)
	 */
	public static final native int _gtk_combo_box_get_wrap_width(long /*int*/ combo_box);
	public static final int gtk_combo_box_get_wrap_width(long /*int*/ combo_box) {
		lock.lock();
		try {
			return _gtk_combo_box_get_wrap_width(combo_box);
		} finally {
			lock.unlock();
		}
	}
	/**
	* @param combo_box cast=(GtkComboBox *)
	*/
	public static final native void _gtk_combo_box_popup(long /*int*/ combo_box);
	public static final void gtk_combo_box_popup(long /*int*/ combo_box) {
		lock.lock();
		try {
			_gtk_combo_box_popup(combo_box);
		} finally {
			lock.unlock();
		}
	}
	/**
	* @param combo_box cast=(GtkComboBox *)
	*/
	public static final native void _gtk_combo_box_popdown(long /*int*/ combo_box);
	public static final void gtk_combo_box_popdown(long /*int*/ combo_box) {
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
	public static final native void _gtk_container_add(long /*int*/ container, long /*int*/ widget);
	public static final void gtk_container_add(long /*int*/ container, long /*int*/ widget) {
		lock.lock();
		try {
			_gtk_container_add(container, widget);
		} finally {
			lock.unlock();
		}
	}
	//Do not confuse this function with gtk_container_foreach(..).
	//Make sure you know what you are doing when using this. Please be attentive to swt_fixed_forall(..)
	// found in os_custom.c, which overrides this function for swtFixed container with custom behaviour.
	/**
	 * @param container cast=(GtkContainer *)
	 * @param callback cast=(GtkCallback)
	 * @param callback_data cast=(gpointer)
	 */
	public static final native void _gtk_container_forall(long /*int*/ container, long /*int*/ callback, long /*int*/ callback_data);
	public static final void gtk_container_forall(long /*int*/ container, long /*int*/ callback, long /*int*/ callback_data) {
		lock.lock();
		try {
			_gtk_container_forall(container, callback, callback_data);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param container cast=(GtkContainer *)
	 * @param child cast=(GtkWidget *)
	 * @param cairo cast=(cairo_t *)
	 */
	public static final native void _gtk_container_propagate_draw(long /*int*/ container, long /*int*/ child, long /*int*/ cairo);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gtk_container_propagate_draw(long /*int*/ container, long /*int*/ child, long /*int*/ cairo) {
		lock.lock();
		try {
			_gtk_container_propagate_draw(container, child, cairo);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param container cast=(GtkContainer *)
	 */
	public static final native int _gtk_container_get_border_width(long /*int*/ container);
	/** [GTK3 only, if-def'd in os.h] */
	public static final int gtk_container_get_border_width(long /*int*/ container) {
		lock.lock();
		try {
			return _gtk_container_get_border_width(container);
		} finally {
			lock.unlock();
		}
	}
	/** @param container cast=(GtkContainer *) */
	public static final native long /*int*/ _gtk_container_get_children(long /*int*/ container);
	public static final long /*int*/ gtk_container_get_children(long /*int*/ container) {
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
	public static final native void _gtk_container_remove(long /*int*/ container, long /*int*/ widget);
	public static final void gtk_container_remove(long /*int*/ container, long /*int*/ widget) {
		lock.lock();
		try {
			_gtk_container_remove(container, widget);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param container cast=(GtkContainer *)
	 * @param border_width cast=(guint)
	 */
	public static final native void _gtk_container_set_border_width(long /*int*/ container, int border_width);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gtk_container_set_border_width(long /*int*/ container, int border_width) {
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
	public static final native long /*int*/ _gtk_dialog_add_button(long /*int*/ dialog, byte[]  button_text, int response_id);
	public static final long /*int*/ gtk_dialog_add_button(long /*int*/ dialog, byte[]  button_text, int response_id) {
		lock.lock();
		try {
			return _gtk_dialog_add_button(dialog, button_text, response_id);
		} finally {
			lock.unlock();
		}
	}
	/** @param dialog cast=(GtkDialog *) */
	public static final native int _gtk_dialog_run(long /*int*/ dialog);
	public static final int gtk_dialog_run(long /*int*/ dialog) {
		lock.lock();
		try {
			return _gtk_dialog_run(dialog);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 * @param targets cast=(GtkTargetList *)
	 * @param actions cast=(GdkDragAction)
	 * @param button cast=(gint)
	 * @param event cast=(GdkEvent *)
	 * @param x cast=(gint)
	 * @param y cast=(gint)
	 */
	public static final native long /*int*/ _gtk_drag_begin_with_coordinates(long /*int*/ widget, long /*int*/ targets, int actions, int button, long /*int*/ event, int x, int y);
	/** [GTK3 only, if-def'd in os.h] */
	public static final long /*int*/ gtk_drag_begin_with_coordinates(long /*int*/ widget, long /*int*/ targets, int actions, int button, long /*int*/ event, int x, int y) {
		lock.lock();
		try {
			return _gtk_drag_begin_with_coordinates(widget, targets, actions, button, event, x, y);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param device cast=(GdkDevice *)
	 * @param targets cast=(GdkContentFormats *)
	 * @param actions cast=(GdkDragAction)
	 * @param x cast=(gint)
	 * @param y cast=(gint)
	 */
	public static final native long /*int*/ _gtk_drag_begin_with_coordinates(long /*int*/ widget, long /*int*/ device, long /*int*/ targets, int actions, int x, int y);
	/** [GTK4 only, if-def'd in os.h] */
	public static final long /*int*/ gtk_drag_begin_with_coordinates(long /*int*/ widget, long /*int*/ device, long /*int*/ targets, int actions, int x, int y) {
		lock.lock();
		try {
			return _gtk_drag_begin_with_coordinates(widget, device, targets, actions, x, y);
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
	public static final native boolean _gtk_drag_check_threshold(long /*int*/ widget, int start_x, int start_y, int current_x, int current_y);
	public static final boolean gtk_drag_check_threshold(long /*int*/ widget, int start_x, int start_y, int current_x, int current_y) {
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
	public static final native long /*int*/ _gtk_drag_dest_find_target(long /*int*/ widget, long /*int*/ context, long /*int*/ target_list);
	/** [GTK3/GTK4: both have identical signatures but accept differing parameters at the native level] */
	public static final long /*int*/ gtk_drag_dest_find_target(long /*int*/ widget, long /*int*/ context, long /*int*/ target_list) {
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
	public static final native void _gtk_drag_dest_set(long /*int*/ widget, int flags, long /*int*/ targets, int n_targets, int actions);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gtk_drag_dest_set(long /*int*/ widget, int flags, long /*int*/ targets, int n_targets, int actions) {
		lock.lock();
		try {
			_gtk_drag_dest_set(widget, flags, targets, n_targets, actions);
		} finally {
			lock.unlock();
		}
	}
	/** @param widget cast=(GtkWidget *) */
	public static final native void _gtk_drag_dest_unset(long /*int*/ widget);
	public static final void gtk_drag_dest_unset(long /*int*/ widget) {
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
	public static final native void _gtk_drag_finish(long /*int*/ context, boolean success, boolean delete, int time);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gtk_drag_finish(long /*int*/ context, boolean success, boolean delete, int time) {
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
	public static final native void _gtk_drag_get_data(long /*int*/ widget, long /*int*/ context, long /*int*/ target, int time);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gtk_drag_get_data(long /*int*/ widget, long /*int*/ context, long /*int*/ target, int time) {
		lock.lock();
		try {
			_gtk_drag_get_data(widget, context, target, time);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param drop cast=(GdkDrop *)
	 * @param target cast=(GdkAtom)
	 */
	public static final native void _gtk_drag_get_data(long /*int*/ widget, long /*int*/ drop, long /*int*/ target);
	/** [GTK4 only, if-def'd in os.h] */
	public static final void gtk_drag_get_data(long /*int*/ widget, long /*int*/ drop, long /*int*/ target) {
		lock.lock();
		try {
			_gtk_drag_get_data(widget, drop, target);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param context cast=(GdkDragContext *)
	 * @param surface cast=(cairo_surface_t *)
	 */
	public static final native void _gtk_drag_set_icon_surface(long /*int*/ context, long /*int*/ surface);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gtk_drag_set_icon_surface(long /*int*/ context, long /*int*/ surface) {
		lock.lock();
		try {
			_gtk_drag_set_icon_surface(context, surface);
		} finally {
			lock.unlock();
		}
	}
	/** @param editable cast=(GtkEditable *) */
	public static final native void _gtk_editable_copy_clipboard(long /*int*/ editable);
	public static final void gtk_editable_copy_clipboard(long /*int*/ editable) {
		lock.lock();
		try {
			_gtk_editable_copy_clipboard(editable);
		} finally {
			lock.unlock();
		}
	}
	/** @param editable cast=(GtkEditable *) */
	public static final native void _gtk_editable_cut_clipboard(long /*int*/ editable);
	public static final void gtk_editable_cut_clipboard(long /*int*/ editable) {
		lock.lock();
		try {
			_gtk_editable_cut_clipboard(editable);
		} finally {
			lock.unlock();
		}
	}
	/** @param editable cast=(GtkEditable *) */
	public static final native void _gtk_editable_delete_selection(long /*int*/ editable);
	public static final void gtk_editable_delete_selection(long /*int*/ editable) {
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
	public static final native void _gtk_editable_delete_text(long /*int*/ editable, int start_pos, int end_pos);
	public static final void gtk_editable_delete_text(long /*int*/ editable, int start_pos, int end_pos) {
		lock.lock();
		try {
			_gtk_editable_delete_text(editable, start_pos, end_pos);
		} finally {
			lock.unlock();
		}
	}
	/** @param editable cast=(GtkEditable *) */
	public static final native boolean _gtk_editable_get_editable(long /*int*/ editable);
	public static final boolean gtk_editable_get_editable(long /*int*/ editable) {
		lock.lock();
		try {
			return _gtk_editable_get_editable(editable);
		} finally {
			lock.unlock();
		}
	}
	/** @param editable cast=(GtkEditable *) */
	public static final native int _gtk_editable_get_position(long /*int*/ editable);
	public static final int gtk_editable_get_position(long /*int*/ editable) {
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
	public static final native boolean _gtk_editable_get_selection_bounds(long /*int*/ editable, int[] start, int[] end);
	public static final boolean gtk_editable_get_selection_bounds(long /*int*/ editable, int[] start, int[] end) {
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
	public static final native void _gtk_editable_insert_text(long /*int*/ editable, byte[] new_text, int new_text_length, int[] position);
	public static final void gtk_editable_insert_text(long /*int*/ editable, byte[] new_text, int new_text_length, int[] position) {
		lock.lock();
		try {
			_gtk_editable_insert_text(editable, new_text, new_text_length, position);
		} finally {
			lock.unlock();
		}
	}
	/** @param editable cast=(GtkEditable *) */
	public static final native void _gtk_editable_paste_clipboard(long /*int*/ editable);
	public static final void gtk_editable_paste_clipboard(long /*int*/ editable) {
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
	public static final native void _gtk_editable_select_region(long /*int*/ editable, int start, int end);
	public static final void gtk_editable_select_region(long /*int*/ editable, int start, int end) {
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
	public static final native void _gtk_editable_set_editable(long /*int*/ entry, boolean editable);
	public static final void gtk_editable_set_editable(long /*int*/ entry, boolean editable) {
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
	public static final native void _gtk_editable_set_position(long /*int*/ editable, int position);
	public static final void gtk_editable_set_position(long /*int*/ editable, int position) {
		lock.lock();
		try {
			_gtk_editable_set_position(editable, position);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param self cast=(GtkEntry *)
	 * @param n_chars cast=(gint)
	 */
	public static final native void  _gtk_entry_set_width_chars (long /*int*/ self, int n_chars);
	public static final void  gtk_entry_set_width_chars (long /*int*/ self, int n_chars) {
		lock.lock();
		try {
			_gtk_entry_set_width_chars(self, n_chars);
		} finally {
			lock.unlock();
		}
	}
	/** @param entry cast=(GtkEntry *) */
	public static final native char _gtk_entry_get_invisible_char(long /*int*/ entry);
	public static final char gtk_entry_get_invisible_char(long /*int*/ entry) {
		lock.lock();
		try {
			return _gtk_entry_get_invisible_char(entry);
		} finally {
			lock.unlock();
		}
	}
	/** @param entry cast=(GtkEntry *) */
	public static final native long /*int*/ _gtk_entry_get_layout (long /*int*/ entry);
	public static final long /*int*/ gtk_entry_get_layout (long /*int*/ entry) {
		lock.lock();
		try {
			return _gtk_entry_get_layout(entry);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param entry cast=(GtkEntry *)
	 * @param x cast=(gint *)
	 * @param y cast=(gint *)
	 */
	public static final native void _gtk_entry_get_layout_offsets (long /*int*/ entry, int[] x, int[] y);
	public static final void gtk_entry_get_layout_offsets (long /*int*/ entry, int[] x, int[] y) {
		lock.lock();
		try {
			_gtk_entry_get_layout_offsets(entry, x, y);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param entry cast=(GtkEntry *)
	 * @param index cast=(gint)
	 */
	public static final native int _gtk_entry_text_index_to_layout_index (long /*int*/ entry, int index);
	public static final int gtk_entry_text_index_to_layout_index (long /*int*/ entry, int index) {
		lock.lock();
		try {
			return _gtk_entry_text_index_to_layout_index(entry, index);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param entry cast=(GtkEntry *)
	 * @param icon_pos cast=(gint)
	 * @param icon_area cast=(GdkRectangle *),flags=no_in
	 */
	public static final native void _gtk_entry_get_icon_area(long /*int*/ entry, int icon_pos, GdkRectangle icon_area);
	public static final void gtk_entry_get_icon_area(long /*int*/ entry, int icon_pos, GdkRectangle icon_area) {
		lock.lock();
		try {
			_gtk_entry_get_icon_area(entry, icon_pos, icon_area);
		} finally {
			lock.unlock();
		}
	}
	/** @param entry cast=(GtkEntry *) */
	public static final native int _gtk_entry_get_max_length(long /*int*/ entry);
	public static final int gtk_entry_get_max_length(long /*int*/ entry) {
		lock.lock();
		try {
			return _gtk_entry_get_max_length(entry);
		} finally {
			lock.unlock();
		}
	}
	/** @param entry cast=(GtkEntry *) */
	public static final native long /*int*/ _gtk_entry_get_text(long /*int*/ entry);
	public static final long /*int*/ gtk_entry_get_text(long /*int*/ entry) {
		lock.lock();
		try {
			return _gtk_entry_get_text(entry);
		} finally {
			lock.unlock();
		}
	}
	/** @param entry cast=(GtkEntry *) */
	public static final native boolean _gtk_entry_get_visibility(long /*int*/ entry);
	public static final boolean gtk_entry_get_visibility(long /*int*/ entry) {
		lock.lock();
		try {
			return _gtk_entry_get_visibility(entry);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_entry_new();
	public static final long /*int*/ gtk_entry_new() {
		lock.lock();
		try {
			return _gtk_entry_new();
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param entry cast=(GtkEntry *)
	 * @param xalign cast=(gfloat)
	 */
	public static final native void _gtk_entry_set_alignment(long /*int*/ entry, float xalign);
	public static final void gtk_entry_set_alignment(long /*int*/ entry, float xalign) {
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
	public static final native void _gtk_entry_set_has_frame(long /*int*/ entry, boolean setting);
	public static final void gtk_entry_set_has_frame(long /*int*/ entry, boolean setting) {
		lock.lock();
		try {
			_gtk_entry_set_has_frame(entry, setting);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param entry cast=(GtkEntry *)
	 * @param iconPos cast=(gint)
	 * @param stock cast=(const gchar *)
	 */
	public static final native void _gtk_entry_set_icon_from_icon_name(long /*int*/ entry, int iconPos, byte[] stock);
	public static final void gtk_entry_set_icon_from_icon_name(long /*int*/ entry, int iconPos, byte[] iconName) {
		lock.lock();
		try {
			_gtk_entry_set_icon_from_icon_name(entry, iconPos, iconName);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param entry cast=(GtkEntry *)
	 * @param icon_pos cast=(GtkEntryIconPosition)
	 * @param activatable cast=(gboolean)
	 */
	public static final native void _gtk_entry_set_icon_activatable(long /*int*/ entry, int icon_pos, boolean activatable);
	public static final void gtk_entry_set_icon_activatable(long /*int*/ entry, int icon_pos, boolean activatable) {
		lock.lock();
		try {
			_gtk_entry_set_icon_activatable(entry, icon_pos, activatable);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param entry cast=(GtkEntry *)
	 * @param icon_pos cast=(GtkEntryIconPosition)
	 * @param sensitive cast=(gboolean)
	 */
	public static final native void _gtk_entry_set_icon_sensitive(long /*int*/ entry, int icon_pos, boolean sensitive);
	public static final void gtk_entry_set_icon_sensitive(long /*int*/ entry, int icon_pos, boolean sensitive) {
		lock.lock();
		try {
			_gtk_entry_set_icon_sensitive(entry, icon_pos, sensitive);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param entry cast=(GtkEntry *)
	 * @param ch cast=(gint)
	 */
	public static final native void _gtk_entry_set_invisible_char(long /*int*/ entry, char ch);
	public static final void gtk_entry_set_invisible_char(long /*int*/ entry, char ch) {
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
	public static final native void _gtk_entry_set_max_length(long /*int*/ entry, int max);
	public static final void gtk_entry_set_max_length(long /*int*/ entry, int max) {
		lock.lock();
		try {
			_gtk_entry_set_max_length(entry, max);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param entry cast=(GtkEntry *)
	 * @param tabs cast=(PangoTabArray *)
	 */
	public static final native void _gtk_entry_set_tabs(long /*int*/ entry, long /*int*/ tabs);
	public static final void gtk_entry_set_tabs(long /*int*/ entry, long /*int*/ tabs) {
		lock.lock();
		try {
			_gtk_entry_set_tabs(entry, tabs);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param entry cast=(GtkEntry *)
	 * @param text cast=(const gchar *)
	 */
	public static final native void _gtk_entry_set_text(long /*int*/ entry, byte[] text);
	public static final void gtk_entry_set_text(long /*int*/ entry, byte[] text) {
		lock.lock();
		try {
			_gtk_entry_set_text(entry, text);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param entry cast=(GtkEntry *)
	 * @param text cast=(const gchar *)
	 */
	public static final native void _gtk_entry_set_placeholder_text(long /*int*/ entry, byte[] text);
	public static final void gtk_entry_set_placeholder_text(long /*int*/ entry, byte[] text) {
		lock.lock();
		try {
			_gtk_entry_set_placeholder_text(entry, text);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param entry cast=(GtkEntry *)
	 * @param visible cast=(gboolean)
	 */
	public static final native void _gtk_entry_set_visibility(long /*int*/ entry, boolean visible);
	public static final void gtk_entry_set_visibility(long /*int*/ entry, boolean visible) {
		lock.lock();
		try {
			_gtk_entry_set_visibility(entry, visible);
		} finally {
			lock.unlock();
		}
	}
	/** @param expander cast=(GtkExpander *) */
	public static final native boolean _gtk_expander_get_expanded(long /*int*/ expander);
	public static final boolean gtk_expander_get_expanded(long /*int*/ expander) {
		lock.lock();
		try {
			return _gtk_expander_get_expanded(expander);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param label cast=(const gchar *)
	 */
	public static final native long /*int*/ _gtk_expander_new(byte[] label);
	public static final long /*int*/ gtk_expander_new(byte[] label) {
		lock.lock();
		try {
			return _gtk_expander_new(label);
		} finally {
			lock.unlock();
		}
	}
	/** @param expander cast=(GtkExpander *) */
	public static final native void _gtk_expander_set_expanded(long /*int*/ expander, boolean expanded);
	public static final void gtk_expander_set_expanded(long /*int*/ expander, boolean expanded) {
		lock.lock();
		try {
			_gtk_expander_set_expanded(expander, expanded);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param expander cast=(GtkExpander *)
	 * @param label_widget cast=(GtkWidget *)
	 */
	public static final native void _gtk_expander_set_label_widget(long /*int*/ expander, long /*int*/ label_widget);
	public static final void  gtk_expander_set_label_widget(long /*int*/ expander, long /*int*/ label_widget) {
		lock.lock();
		try {
			_gtk_expander_set_label_widget(expander, label_widget);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param chooser cast=(GtkFileChooser *)
	 * @param filter cast=(GtkFileFilter *)
	 */
	public static final native void _gtk_file_chooser_add_filter(long /*int*/ chooser, long /*int*/ filter);
	public static final void gtk_file_chooser_add_filter(long /*int*/ chooser, long /*int*/ filter) {
		lock.lock();
		try {
			_gtk_file_chooser_add_filter(chooser, filter);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param title cast=(const gchar *),flags=no_out
	 * @param parent cast=(GtkWindow *)
	 * @param first_button_text cast=(const gchar *),flags=no_out
	 * @param terminator cast=(const gchar *),flags=sentinel
	 */
	public static final native long /*int*/ _gtk_file_chooser_dialog_new(byte[] title, long /*int*/ parent, int action, byte[] first_button_text, int first_button_id, byte[] second_button_text, int second_button_id, long /*int*/ terminator);
	public static final long /*int*/ gtk_file_chooser_dialog_new(byte[] title, long /*int*/ parent, int action, byte[] first_button_text, int first_button_id, byte[] second_button_text, int second_button_id, long /*int*/ terminator) {
		lock.lock();
		try {
			return _gtk_file_chooser_dialog_new(title, parent, action, first_button_text, first_button_id, second_button_text, second_button_id, terminator);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param title cast=(const gchar *),flags=no_out
	 * @param parent cast=(GtkWindow *)
	 * @param first_button_text cast=(const gchar *),flags=no_out
	 * @param terminator cast=(const gchar *),flags=sentinel
	 */
	public static final native long /*int*/ _gtk_file_chooser_dialog_new(byte[] title, long /*int*/ parent, int action, long /*int*/ first_button_text, int first_button_id, long /*int*/ second_button_text, int second_button_id, long /*int*/ terminator);
	public static final long /*int*/ gtk_file_chooser_dialog_new(byte[] title, long /*int*/ parent, int action, long /*int*/ first_button_text, int first_button_id, long /*int*/ second_button_text, int second_button_id, long /*int*/ terminator) {
		lock.lock();
		try {
			return _gtk_file_chooser_dialog_new(title, parent, action, first_button_text, first_button_id, second_button_text, second_button_id, terminator);
		} finally {
			lock.unlock();
		}
	}
	/** @param chooser cast=(GtkFileChooser *) */
	public static final native long /*int*/ _gtk_file_chooser_get_filename(long /*int*/ chooser);
	public static final long /*int*/ gtk_file_chooser_get_filename(long /*int*/ chooser) {
		lock.lock();
		try {
			return _gtk_file_chooser_get_filename(chooser);
		} finally {
			lock.unlock();
		}
	}
	/**  @param chooser cast=(GtkFileChooser *) */
	public static final native long /*int*/ _gtk_file_chooser_get_filenames(long /*int*/ chooser);
	public static final long /*int*/ gtk_file_chooser_get_filenames(long /*int*/ chooser) {
		lock.lock();
		try {
			return _gtk_file_chooser_get_filenames(chooser);
		} finally {
			lock.unlock();
		}
	}
	/** @param chooser cast=(GtkFileChooser *) */
	public static final native long /*int*/ _gtk_file_chooser_get_uri(long /*int*/ chooser);
	public static final long /*int*/ gtk_file_chooser_get_uri(long /*int*/ chooser) {
		lock.lock();
		try {
			return _gtk_file_chooser_get_uri(chooser);
		} finally {
			lock.unlock();
		}
	}
	/** @param chooser cast=(GtkFileChooser *) */
	public static final native long /*int*/ _gtk_file_chooser_get_uris(long /*int*/ chooser);
	public static final long /*int*/ gtk_file_chooser_get_uris(long /*int*/ chooser) {
		lock.lock();
		try {
			return _gtk_file_chooser_get_uris(chooser);
		} finally {
			lock.unlock();
		}
	}
	/** @param chooser cast=(GtkFileChooser *) */
	public static final native long /*int*/ _gtk_file_chooser_get_filter(long /*int*/ chooser);
	public static final long /*int*/ gtk_file_chooser_get_filter(long /*int*/ chooser) {
		lock.lock();
		try {
			return _gtk_file_chooser_get_filter(chooser);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param chooser cast=(GtkFileChooser *)
	 * @param filename cast=(const gchar *)
	 */
	public static final native void _gtk_file_chooser_set_current_folder(long /*int*/ chooser, long /*int*/ filename);
	public static final void gtk_file_chooser_set_current_folder(long /*int*/ chooser, long /*int*/ filename) {
		lock.lock();
		try {
			_gtk_file_chooser_set_current_folder(chooser, filename);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param chooser cast=(GtkFileChooser *)
	 * @param uri cast=(const gchar *)
	 */
	public static final native void _gtk_file_chooser_set_current_folder_uri(long /*int*/ chooser, byte [] uri);
	public static final void gtk_file_chooser_set_current_folder_uri(long /*int*/ chooser, byte [] uri) {
		lock.lock();
		try {
			_gtk_file_chooser_set_current_folder_uri(chooser, uri);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param chooser cast=(GtkFileChooser *)
	 * @param name cast=(const gchar *)
	 */
	public static final native void _gtk_file_chooser_set_current_name(long /*int*/ chooser, byte[] name);
	public static final void gtk_file_chooser_set_current_name(long /*int*/ chooser, byte[] name) {
		lock.lock();
		try {
			_gtk_file_chooser_set_current_name(chooser, name);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param chooser cast=(GtkFileChooser *)
	 * @param local_only cast=(gboolean)
	 */
	public static final native void _gtk_file_chooser_set_local_only(long /*int*/ chooser, boolean local_only);
	public static final void gtk_file_chooser_set_local_only(long /*int*/ chooser, boolean local_only) {
		lock.lock();
		try {
			_gtk_file_chooser_set_local_only(chooser, local_only);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param chooser cast=(GtkFileChooser *)
	 * @param do_overwrite_confirmation cast=(gboolean)
	 */
	public static final native void _gtk_file_chooser_set_do_overwrite_confirmation(long /*int*/ chooser, boolean do_overwrite_confirmation);
	public static final void gtk_file_chooser_set_do_overwrite_confirmation(long /*int*/ chooser, boolean do_overwrite_confirmation) {
		lock.lock();
		try {
			_gtk_file_chooser_set_do_overwrite_confirmation(chooser, do_overwrite_confirmation);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param chooser cast=(GtkFileChooser *)
	 * @param extra_widget cast=(GtkWidget *)
	 */
	public static final native void _gtk_file_chooser_set_extra_widget(long /*int*/ chooser, long /*int*/ extra_widget);
	public static final void gtk_file_chooser_set_extra_widget(long /*int*/ chooser, long /*int*/ extra_widget) {
		lock.lock();
		try {
			_gtk_file_chooser_set_extra_widget(chooser, extra_widget);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param chooser cast=(GtkFileChooser *)
	 * @param name cast=(const gchar *)
	 */
	public static final native void _gtk_file_chooser_set_filename(long /*int*/ chooser, long /*int*/ name);
	public static final void gtk_file_chooser_set_filename(long /*int*/ chooser, long /*int*/ name) {
		lock.lock();
		try {
			_gtk_file_chooser_set_filename(chooser, name);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param chooser cast=(GtkFileChooser *)
	 * @param filter cast=(GtkFileFilter *)
	 */
	public static final native void _gtk_file_chooser_set_filter(long /*int*/ chooser, long /*int*/ filter);
	public static final void gtk_file_chooser_set_filter(long /*int*/ chooser, long /*int*/ filter) {
		lock.lock();
		try {
			_gtk_file_chooser_set_filter(chooser, filter);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param chooser cast=(GtkFileChooser *)
	 * @param uri cast=(const char *)
	 */
	public static final native void _gtk_file_chooser_set_uri(long /*int*/ chooser, byte [] uri);
	public static final void gtk_file_chooser_set_uri(long /*int*/ chooser, byte [] uri) {
		lock.lock();
		try {
			_gtk_file_chooser_set_uri(chooser, uri);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param chooser cast=(GtkFileChooser *)
	 * @param select_multiple cast=(gboolean)
	 */
	public static final native void _gtk_file_chooser_set_select_multiple(long /*int*/ chooser, boolean select_multiple);
	public static final void gtk_file_chooser_set_select_multiple(long /*int*/ chooser, boolean select_multiple) {
		lock.lock();
		try {
			_gtk_file_chooser_set_select_multiple(chooser, select_multiple);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_gesture_multi_press_new();
	/** [GTK4 only, if-def'd in os.h] */
	public static final long /*int*/ gtk_gesture_multi_press_new() {
	        lock.lock();
	        try {
	        	return _gtk_gesture_multi_press_new();
	        } finally {
	                lock.unlock();
	        }
	}
	public static final native long /*int*/ _gtk_event_controller_key_new();
	/** [GTK4 only, if-def'd in os.h] */
	public static final long /*int*/ gtk_event_controller_key_new() {
	        lock.lock();
	        try {
	        	return _gtk_event_controller_key_new();
	        } finally {
	                lock.unlock();
	        }
	}
	public static final native long /*int*/ _gtk_event_controller_motion_new();
	/** [GTK4 only, if-def'd in os.h] */
	public static final long /*int*/ gtk_event_controller_motion_new() {
	        lock.lock();
	        try {
	        	return _gtk_event_controller_motion_new();
	        } finally {
	                lock.unlock();
	        }
	}
	public static final native long /*int*/ _gtk_event_controller_scroll_new(int flag);
	/** [GTK4 only, if-def'd in os.h] */
	public static final long /*int*/ gtk_event_controller_scroll_new(int flag) {
	        lock.lock();
	        try {
	        	return _gtk_event_controller_scroll_new(flag);
	        } finally {
	                lock.unlock();
	        }
	}
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param controller cast=(GtkEventController *)
	 */
	public static final native void _gtk_widget_add_controller(long /*int*/ widget, long /*int*/ controller);
	/** [GTK4 only, if-def'd in os.h] */
	public static final void gtk_widget_add_controller(long /*int*/ widget, long /*int*/ controller) {
	        lock.lock();
	        try {
	        	_gtk_widget_add_controller(widget, controller);
	        } finally {
	                lock.unlock();
	        }
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native void _gtk_event_controller_set_propagation_phase(long /*int*/ controller, int phase);
	public static final void gtk_event_controller_set_propagation_phase(long /*int*/ controller, int phase) {
	        lock.lock();
	        try {
	                _gtk_event_controller_set_propagation_phase(controller, phase);
	        } finally {
	                lock.unlock();
	        }
	}
	/**
	 * @method flags=dynamic
	 * @param title cast=(const gchar *),flags=no_out
	 * @param parent cast=(GtkWindow *)
	 * @param accept_label cast=(const gchar *),flags=no_out
	 * @param cancel_label cast=(const gchar *),flags=no_out
	 */
	public static final native long /*int*/ _gtk_file_chooser_native_new(byte[] title, long /*int*/ parent, int action, byte[] accept_label, byte[] cancel_label);
	public static final long /*int*/ gtk_file_chooser_native_new(byte[] title, long /*int*/ parent, int action,  byte[] accept_label, byte[] cancel_label) {
		lock.lock();
		try {
			return _gtk_file_chooser_native_new(title, parent, action, accept_label, cancel_label);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native void _gtk_event_controller_handle_event(long /*int*/ gesture, long /*int*/ event);
	public static final void gtk_event_controller_handle_event(long /*int*/ gesture, long /*int*/ event) {
	        lock.lock();
	        try {
	                _gtk_event_controller_handle_event(gesture, event);
	        } finally {
	                lock.unlock();
	        }
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native long /*int*/ _gtk_event_controller_get_widget(long /*int*/ controller);
	public static final long /*int*/ gtk_event_controller_get_widget(long /*int*/ controller) {
	        lock.lock();
	        try {
	        		return _gtk_event_controller_get_widget(controller);
	        } finally {
	                lock.unlock();
	        }
	}
	/**
	 * @param filter cast=(GtkFileFilter *)
	 * @param pattern cast=(const gchar *)
	 */
	public static final native void _gtk_file_filter_add_pattern(long /*int*/ filter, byte[] pattern);
	public static final void gtk_file_filter_add_pattern(long /*int*/ filter, byte[] pattern) {
		lock.lock();
		try {
			_gtk_file_filter_add_pattern(filter, pattern);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_file_filter_new();
	public static final long /*int*/ gtk_file_filter_new() {
		lock.lock();
		try {
			return _gtk_file_filter_new();
		} finally {
			lock.unlock();
		}
	}
	/** @param filter cast=(GtkFileFilter *) */
	public static final native long /*int*/ _gtk_file_filter_get_name(long /*int*/ filter);
	public static final long /*int*/ gtk_file_filter_get_name(long /*int*/ filter) {
		lock.lock();
		try {
			return _gtk_file_filter_get_name(filter);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param filter cast=(GtkFileFilter *)
	 * @param name cast=(const gchar *)
	 */
	public static final native void _gtk_file_filter_set_name(long /*int*/ filter, byte[] name);
	public static final void gtk_file_filter_set_name(long /*int*/ filter, byte[] name) {
		lock.lock();
		try {
			_gtk_file_filter_set_name(filter, name);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native boolean _gtk_gesture_drag_get_start_point(long /*int*/ gesture, double[] x, double [] y);
	public static final boolean gtk_gesture_drag_get_start_point(long /*int*/ gesture, double[] x, double [] y) {
		lock.lock();
		try {
			return _gtk_gesture_drag_get_start_point(gesture, x, y);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native boolean _gtk_gesture_is_recognized(long /*int*/ gesture);
	public static final boolean gtk_gesture_is_recognized(long /*int*/ gesture) {
		lock.lock();
		try {
			return _gtk_gesture_is_recognized(gesture);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native long /*int*/ _gtk_gesture_drag_new(long /*int*/ widget);
	public static final long /*int*/ gtk_gesture_drag_new(long /*int*/ widget) {
	        lock.lock();
	        try {
	                return _gtk_gesture_drag_new(widget);
	        } finally {
	                lock.unlock();
	        }
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native long /*int*/ _gtk_gesture_get_last_event(long /*int*/ gesture, long /*int*/ sequence);
	public static final long /*int*/ gtk_gesture_get_last_event(long /*int*/ gesture, long /*int*/ sequence) {
			lock.lock();
			try {
				return _gtk_gesture_get_last_event(gesture,sequence);
			} finally {
				lock.unlock();
			}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native long /*int*/ _gtk_gesture_get_last_updated_sequence(long /*int*/ gesture);
	public static final long /*int*/ gtk_gesture_get_last_updated_sequence(long /*int*/ gesture) {
			lock.lock();
			try {
				return _gtk_gesture_get_last_updated_sequence(gesture);
			} finally {
				lock.unlock();
			}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native boolean _gtk_gesture_get_point(long /*int*/ gesture, long /*int*/ sequence, double[] x, double [] y);
	public static final boolean gtk_gesture_get_point(long /*int*/ gesture, long /*int*/ sequence, double[] x, double [] y) {
			lock.lock();
			try {
				return _gtk_gesture_get_point(gesture, sequence, x, y);
			} finally {
				lock.unlock();
			}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native long /*int*/ _gtk_gesture_get_sequences(long /*int*/ gesture);
	public static final long /*int*/ gtk_gesture_get_sequences(long /*int*/ gesture) {
			lock.lock();
			try {
				return _gtk_gesture_get_sequences(gesture);
			} finally {
				lock.unlock();
			}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native void _gtk_gesture_group (long /*int*/ group_gesture, long /*int*/ gesture);
	public static final void gtk_gesture_group (long /*int*/ group_gesture, long /*int*/ gesture) {
	        lock.lock();
	        try {
	                 _gtk_gesture_group(group_gesture, gesture);
	        } finally {
	                lock.unlock();
	        }
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native void _gtk_gesture_pan_set_orientation(long /*int*/ orientation);
	public static final void gtk_gesture_pan_set_orientation(long /*int*/ orientation) {
		lock.lock();
		try {
			 _gtk_gesture_pan_set_orientation(orientation);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native long /*int*/ _gtk_gesture_pan_get_orientation(long /*int*/ gesture);
	public static final long /*int*/ gtk_gesture_pan_get_orientation(long /*int*/ gesture) {
		lock.lock();
		try {
			return _gtk_gesture_pan_get_orientation(gesture);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native void _gtk_gesture_pan_new (long /*int*/ widget, long /*int*/ orientation);
	public static final void gtk_gesture_pan_new (long /*int*/ widget, long /*int*/ orientation) {
		lock.lock();
		try {
			_gtk_gesture_pan_new(widget, orientation);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native void _gtk_gesture_single_set_button(long /*int*/ gesture, int button);
	public static final void gtk_gesture_single_set_button(long /*int*/ gesture, int button) {
	        lock.lock();
	        try {
	                 _gtk_gesture_single_set_button(gesture, button);
	        } finally {
	                lock.unlock();
	        }
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native boolean _gtk_gesture_swipe_get_velocity(long /*int*/ gesture, double [] velocity_x, double[] velocity_y);
	public static final boolean gtk_gesture_swipe_get_velocity(long /*int*/ gesture, double [] velocity_x, double[] velocity_y) {
		lock.lock();
		try {
			return _gtk_gesture_swipe_get_velocity(gesture, velocity_x, velocity_y);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native long /*int*/ _gtk_gesture_swipe_new (long /*int*/ widget);
	public static final long /*int*/ gtk_gesture_swipe_new (long /*int*/ widget) {
		lock.lock();
		try {
			return _gtk_gesture_swipe_new(widget);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native void _gtk_gesture_drag_get_offset(long /*int*/ gesture, double[] x, double[] y);
	public static final void gtk_gesture_drag_get_offset(long /*int*/ gesture, double[] x, double[] y) {
	        lock.lock();
	        try {
	                _gtk_gesture_drag_get_offset(gesture, x, y);
	        } finally {
	                lock.unlock();
	        }
	}
	/**
	 * @method flags=dynamic
	 */

	public static final native double _gtk_gesture_rotate_get_angle_delta (long /*int*/ gesture);
	public static final double gtk_gesture_rotate_get_angle_delta (long /*int*/ gesture) {
		lock.lock();
		try {
				return _gtk_gesture_rotate_get_angle_delta(gesture);
		} finally {
				lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 */

	public static final native long /*int*/ _gtk_gesture_rotate_new(long /*int*/ widget);
	public static final long /*int*/ gtk_gesture_rotate_new(long /*int*/ widget) {
	        lock.lock();
	        try {
	                return _gtk_gesture_rotate_new(widget);
	        } finally {
	                lock.unlock();
	        }
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native long /*int*/ _gtk_gesture_zoom_new(long /*int*/ widget);
	public static final long /*int*/ gtk_gesture_zoom_new(long /*int*/ widget) {
	        lock.lock();
	        try {
	                return _gtk_gesture_zoom_new(widget);
	        } finally {
	                lock.unlock();
	        }
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native double _gtk_gesture_zoom_get_scale_delta (long /*int*/ gesture);
	public static final double gtk_gesture_zoom_get_scale_delta (long /*int*/ gesture) {
	        lock.lock();
	        try {
	                return _gtk_gesture_zoom_get_scale_delta(gesture);
	        } finally {
	                lock.unlock();
	        }
	}
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native void _gtk_widget_set_clip(long /*int*/ widget, GtkAllocation allocation);
	public static final void gtk_widget_set_clip(long /*int*/ widget, GtkAllocation allocation) {
		lock.lock();
		try {
			_gtk_widget_set_clip(widget, allocation);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native void _gtk_widget_get_clip(long /*int*/ widget, GtkAllocation allocation);
	public static final void gtk_widget_get_clip(long /*int*/ widget, GtkAllocation allocation) {
		lock.lock();
		try {
			_gtk_widget_get_clip(widget, allocation);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param has_window cast=(gboolean)
	 */
	public static final native void _gtk_widget_set_has_window(long /*int*/ widget, boolean has_window);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gtk_widget_set_has_window(long /*int*/ widget, boolean has_window) {
		lock.lock();
		try {
			_gtk_widget_set_has_window(widget, has_window);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param has_surface cast=(gboolean)
	 */
	public static final native void _gtk_widget_set_has_surface(long /*int*/ widget, boolean has_surface);
	/** [GTK4 only, if-def'd in os.h] */
	public static final void gtk_widget_set_has_surface(long /*int*/ widget, boolean has_surface) {
		lock.lock();
		try {
			_gtk_widget_set_has_surface(widget, has_surface);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param expand cast=(gboolean)
	 */
	public static final native void _gtk_widget_set_hexpand(long /*int*/ widget, boolean expand);
	public static final void gtk_widget_set_hexpand(long /*int*/ widget, boolean expand) {
		lock.lock();
		try {
			_gtk_widget_set_hexpand(widget, expand);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param expand cast=(gboolean)
	 */
	public static final native void _gtk_widget_set_vexpand(long /*int*/ widget, boolean expand);
	public static final void gtk_widget_set_vexpand(long /*int*/ widget, boolean expand) {
		lock.lock();
		try {
			_gtk_widget_set_vexpand(widget, expand);
		} finally {
			lock.unlock();
		}
	}
	//since Gtk 3.16. For pre-gtk3.16, use gtk_misc_set_alignment(..)
	/**
	 * @method flags=dynamic
	 * @param label cast=(GtkLabel *)
	 * @param xalign cast=(gfloat)
	 *
	 */
	public static final native void _gtk_label_set_xalign(long /*int*/ label, float xalign);
	public static final void gtk_label_set_xalign(long /*int*/ label, float xalign) {
		lock.lock();
		try {
			_gtk_label_set_xalign(label, xalign);
		} finally {
			lock.unlock();
		}
	}
	//since Gtk 3.16. For pre-gtk3.16, use gtk_misc_set_alignment(..)
	/**
	* @method flags=dynamic
	* @param label cast=(GtkLabel *)
	* @param yalign cast=(gfloat)
	*
	*/
	public static final native void _gtk_label_set_yalign(long /*int*/ label, float yalign);
	public static final void gtk_label_set_yalign(long /*int*/ label, float yalign) {
		lock.lock();
		try {
			_gtk_label_set_yalign(label, yalign);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param gtk_align cast=(GtkAlign)
	 */
	public static final native void _gtk_widget_set_halign(long /*int*/ widget, int gtk_align);
	public static final void gtk_widget_set_halign(long /*int*/ widget, int gtk_align) {
		lock.lock();
		try {
			_gtk_widget_set_halign(widget, gtk_align);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param gtk_align cast=(GtkAlign)
	 */
	public static final native void _gtk_widget_set_valign(long /*int*/ widget, int gtk_align);
	public static final void gtk_widget_set_valign(long /*int*/ widget, int gtk_align ) {
		lock.lock();
		try {
			_gtk_widget_set_valign(widget, gtk_align);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 * @param margin cast=(gint)
	 */
	public static final native void _gtk_widget_set_margin_start (long /*int*/ widget, int margin);
	public static final void gtk_widget_set_margin_start (long /*int*/ widget, int margin) {
		lock.lock();
		try {
			 _gtk_widget_set_margin_start(widget, margin);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 * @param margin cast=(gint)
	 */
	public static final native void _gtk_widget_set_margin_end (long /*int*/ widget, int margin);
	public static final void gtk_widget_set_margin_end (long /*int*/ widget, int margin) {
		lock.lock();
		try {
			 _gtk_widget_set_margin_end(widget, margin);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 * @param margin cast=(gint)
	 */
	public static final native void _gtk_widget_set_margin_top (long /*int*/ widget, int margin);
	public static final void gtk_widget_set_margin_top (long /*int*/ widget, int margin) {
		lock.lock();
		try {
			 _gtk_widget_set_margin_top(widget, margin);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 * @param margin cast=(gint)
	 */
	public static final native void _gtk_widget_set_margin_bottom (long /*int*/ widget, int margin);
	public static final void gtk_widget_set_margin_bottom (long /*int*/ widget, int margin) {
		lock.lock();
		try {
			 _gtk_widget_set_margin_bottom(widget, margin);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param title cast=(const gchar *)
	 * @param parent cast=(GtkWindow *)
	 */
	public static final native long /*int*/ _gtk_font_chooser_dialog_new(byte[] title, long /*int*/ parent);
	public static final long /*int*/ gtk_font_chooser_dialog_new(byte[] title, long /*int*/ parent) {
		lock.lock();
		try {
			return _gtk_font_chooser_dialog_new(title, parent);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param fontchooser cast=(GtkFontChooser *)
	 */
	public static final native long /*int*/ _gtk_font_chooser_get_font(long /*int*/ fontchooser);
	public static final long /*int*/ gtk_font_chooser_get_font(long /*int*/ fontchooser) {
		lock.lock();
		try {
			return _gtk_font_chooser_get_font(fontchooser);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param fsd cast=(GtkFontChooser *)
	 * @param fontname cast=(const gchar *)
	 */
	public static final native void _gtk_font_chooser_set_font(long /*int*/ fsd, byte[] fontname);
	public static final void gtk_font_chooser_set_font(long /*int*/ fsd, byte[] fontname) {
		lock.lock();
		try {
			_gtk_font_chooser_set_font(fsd, fontname);
		} finally {
			lock.unlock();
		}
	}
	/** @param label cast=(const gchar *) */
	public static final native long /*int*/ _gtk_frame_new(byte[] label);
	public static final long /*int*/ gtk_frame_new(byte[] label) {
		lock.lock();
		try {
			return _gtk_frame_new(label);
		} finally {
			lock.unlock();
		}
	}
	/** @param frame cast=(GtkFrame *) */
	public static final native long /*int*/ _gtk_frame_get_label_widget(long /*int*/ frame);
	public static final long /*int*/ gtk_frame_get_label_widget(long /*int*/ frame) {
		lock.lock();
		try {
			return _gtk_frame_get_label_widget(frame);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param frame cast=(GtkFrame *)
	 * @param label_widget cast=(GtkWidget *)
	 */
	public static final native void _gtk_frame_set_label_widget(long /*int*/ frame, long /*int*/ label_widget);
	public static final void gtk_frame_set_label_widget(long /*int*/ frame, long /*int*/ label_widget) {
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
	public static final native void _gtk_frame_set_shadow_type(long /*int*/ frame, int type);
	public static final void gtk_frame_set_shadow_type(long /*int*/ frame, int type) {
		lock.lock();
		try {
			_gtk_frame_set_shadow_type(frame, type);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_get_current_event();
	public static final long /*int*/ gtk_get_current_event() {
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
	public static final native long /*int*/ _gtk_get_default_language();
	public static final long /*int*/ gtk_get_default_language() {
		lock.lock();
		try {
			return _gtk_get_default_language();
		} finally {
			lock.unlock();
		}
	}
	/** @param event cast=(GdkEvent *) */
	public static final native long /*int*/ _gtk_get_event_widget(long /*int*/ event);
	public static final long /*int*/ gtk_get_event_widget(long /*int*/ event) {
		lock.lock();
		try {
			return _gtk_get_event_widget(event);
		} finally {
			lock.unlock();
		}
	}
	/** @param widget cast=(GtkWidget *) */
	public static final native void _gtk_grab_add(long /*int*/ widget);
	public static final void gtk_grab_add(long /*int*/ widget) {
		lock.lock();
		try {
			_gtk_grab_add(widget);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_grab_get_current();
	public static final long /*int*/ gtk_grab_get_current() {
		lock.lock();
		try {
			return _gtk_grab_get_current();
		} finally {
			lock.unlock();
		}
	}
	/** @param widget cast=(GtkWidget *) */
	public static final native void _gtk_grab_remove(long /*int*/ widget);
	public static final void gtk_grab_remove(long /*int*/ widget) {
		lock.lock();
		try {
			_gtk_grab_remove(widget);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param h cast=(gdouble)
	 * @param s cast=(gdouble)
	 * @param v cast=(gdouble)
	 * @param r cast=(gdouble *)
	 * @param g cast=(gdouble *)
	 * @param b cast=(gdouble *)
	 */
	public static final native void _gtk_hsv_to_rgb(double h, double s, double v, double[] r, double[] g, double[] b);
	public static final void gtk_hsv_to_rgb(double h, double s, double v, double[] r, double[] g, double[] b) {
		lock.lock();
		try {
			_gtk_hsv_to_rgb(h, s, v, r, g, b);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param r cast=(gdouble)
	 * @param g cast=(gdouble)
	 * @param b cast=(gdouble)
	 * @param h cast=(gdouble *)
	 * @param s cast=(gdouble *)
	 * @param v cast=(gdouble *)
	 */
	public static final native void _gtk_rgb_to_hsv(double r, double g, double b, double[] h, double[] s, double[] v);
	public static final void gtk_rgb_to_hsv(double r, double g, double b, double[] h, double[] s, double[] v) {
		lock.lock();
		try {
			_gtk_rgb_to_hsv(r, g, b, h, s, v);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param orientation cast=(GtkOrientation)
	 * @param spacing cast=(gint)
	 */
	public static final native long /*int*/ _gtk_box_new(int orientation, int spacing);
	public static final long /*int*/ gtk_box_new(int orientation, int spacing) {
		lock.lock();
		try {
			return _gtk_box_new(orientation, spacing);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param box cast=(GtkBox *)
	 * @param widget cast=(GtkWidget *)
	 * @param expand cast=(gboolean)
	 * @param fill cast=(gboolean)
	 * @param padding cast=(guint)
	 */
	public static final native void /*int*/ _gtk_box_pack_end(long /*int*/ box, long /*int*/ widget,
			boolean expand, boolean fill, int padding);
	/** [GTK3 only] */
	public static final void /*int*/ gtk_box_pack_end(long /*int*/ box, long /*int*/ widget,
			boolean expand, boolean fill, int padding) {
		lock.lock();
		try {
			_gtk_box_pack_end(box, widget, expand, fill, padding);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param box cast=(GtkBox *)
	 * @param child cast=(GtkWidget *)
	 */
	public static final native void /*int*/ _gtk_box_pack_end(long /*int*/ box, long /*int*/ child);
	/** [GTK4 only] */
	public static final void /*int*/ gtk_box_pack_end(long /*int*/ box, long /*int*/ child) {
		lock.lock();
		try {
			_gtk_box_pack_end(box, child);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param box cast=(GtkBox *)
	 * @param child cast=(GtkWidget *)
	 * @param position cast=(gint)
	 */
	public static final native void /*int*/ _gtk_box_reorder_child(long /*int*/ box, long /*int*/ child, int position);
	public static final void /*int*/ gtk_box_reorder_child(long /*int*/ box, long /*int*/ child, int position) {
		lock.lock();
		try {
			_gtk_box_reorder_child(box, child, position);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param box cast=(GtkBox *)
	 * @param homogeneous cast=(gboolean)
	 */
	public static final native void _gtk_box_set_homogeneous(long /*int*/ box, boolean homogeneous);
	public static final void gtk_box_set_homogeneous(long /*int*/ box, boolean homogeneous) {
		lock.lock();
		try {
			_gtk_box_set_homogeneous(box, homogeneous);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native long /*int*/ _gtk_event_box_new();
	/** [GTK3 only] */
	public static final long /*int*/ gtk_event_box_new() {
		lock.lock();
		try {
			return _gtk_event_box_new();
		} finally {
			lock.unlock();
		}
	}
	/**
	 *  @param orientation cast=(GtkOrientation)
	 *  @param adjustment cast=(GtkAdjustment *)
	 */
	public static final native long /*int*/ _gtk_scale_new(int orientation, long /*int*/ adjustment);
	public static final long /*int*/ gtk_scale_new(int orientation, long /*int*/ adjustment) {
		lock.lock();
		try {
			return _gtk_scale_new(orientation, adjustment);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param orientation cast=(GtkOrientation)
	 * @param adjustment cast=(GtkAdjustment *)
	 * */
	public static final native long /*int*/ _gtk_scrollbar_new(int orientation, long /*int*/ adjustment);
	public static final long /*int*/ gtk_scrollbar_new(int orientation, long /*int*/ adjustment) {
		lock.lock();
		try {
			return _gtk_scrollbar_new(orientation, adjustment);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_search_entry_new();
	public static final long /*int*/ gtk_search_entry_new() {
		lock.lock();
		try {
			return _gtk_search_entry_new();
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param orientation cast=(GtkOrientation)
	 */
	public static final native long /*int*/ _gtk_separator_new(int orientation);
	public static final long /*int*/ gtk_separator_new(int orientation) {
		lock.lock();
		try {
			return _gtk_separator_new(orientation);
		} finally {
			lock.unlock();
		}
	}
	// Get function pointer to gtk_status_icon_position_menu
	// See os_custom.h
	public static final native long /*int*/ _gtk_status_icon_position_menu_func();
	public static final long /*int*/ gtk_status_icon_position_menu_func() {
		lock.lock();
		try {
			return _gtk_status_icon_position_menu_func();
		} finally {
			lock.unlock();
		}
	}
	/** @return cast=(GtkIconTheme *) */
	public static final native long /*int*/ _gtk_icon_theme_get_default();
	public static final long /*int*/ gtk_icon_theme_get_default() {
		lock.lock();
		try {
			return _gtk_icon_theme_get_default ();
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param icon_theme cast=(GtkIconTheme *)
	 * @param icon cast=(GIcon *)
	 * @param size cast=(gint)
	 * @param flags cast=(GtkIconLookupFlags)
	 */
	public static final native long /*int*/ _gtk_icon_theme_lookup_by_gicon(long /*int*/ icon_theme, long /*int*/ icon, int size, int flags);
	public static final long /*int*/ gtk_icon_theme_lookup_by_gicon(long /*int*/ icon_theme, long /*int*/ icon, int size, int flags) {
		lock.lock();
		try {
			return _gtk_icon_theme_lookup_by_gicon (icon_theme, icon, size, flags);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param icon_theme cast=(GtkIconTheme *)
	 * @param icon_name cast=(const gchar *)
	 * @param size cast=(gint)
	 * @param flags cast=(GtkIconLookupFlags)
	 * @param error cast=(GError **)
	 */
	public static final native long /*int*/ _gtk_icon_theme_load_icon(long /*int*/ icon_theme, byte[] icon_name, int size, int flags, long /*int*/ error);
	public static final long /*int*/ gtk_icon_theme_load_icon(long /*int*/ icon_theme, byte[] icon_name, int size, int flags, long /*int*/ error) {
		lock.lock();
		try {
			return _gtk_icon_theme_load_icon (icon_theme, icon_name, size, flags, error);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param icon_info cast=(GtkIconInfo *)
	 * @param error cast=(GError **)
	 */
	public static final native long /*int*/ _gtk_icon_info_load_icon(long /*int*/ icon_info, long /*int*/ error[]);
	public static final long /*int*/ gtk_icon_info_load_icon(long /*int*/ icon_info, long /*int*/ error[]) {
		lock.lock();
		try {
			return _gtk_icon_info_load_icon(icon_info, error);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param context cast=(GtkIMContext *)
	 * @param event cast=(GdkEventKey *)
	 */
	public static final native boolean _gtk_im_context_filter_keypress(long /*int*/ context, long /*int*/ event);
	public static final boolean gtk_im_context_filter_keypress(long /*int*/ context, long /*int*/ event) {
		lock.lock();
		try {
			return _gtk_im_context_filter_keypress(context, event);
		} finally {
			lock.unlock();
		}
	}
	/** @param context cast=(GtkIMContext *) */
	public static final native void _gtk_im_context_focus_in(long /*int*/ context);
	public static final void gtk_im_context_focus_in(long /*int*/ context) {
		lock.lock();
		try {
			_gtk_im_context_focus_in(context);
		} finally {
			lock.unlock();
		}
	}
	/** @param context cast=(GtkIMContext *) */
	public static final native void _gtk_im_context_focus_out(long /*int*/ context);
	public static final void gtk_im_context_focus_out(long /*int*/ context) {
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
	public static final native void _gtk_im_context_get_preedit_string(long /*int*/ context, long /*int*/[] str, long /*int*/[] attrs, int[] cursor_pos);
	public static final void gtk_im_context_get_preedit_string(long /*int*/ context, long /*int*/[] str, long /*int*/[] attrs, int[] cursor_pos) {
		lock.lock();
		try {
			_gtk_im_context_get_preedit_string(context, str, attrs, cursor_pos);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_im_context_get_type();
	public static final long /*int*/ gtk_im_context_get_type() {
		lock.lock();
		try {
			return _gtk_im_context_get_type();
		} finally {
			lock.unlock();
		}
	}
	/** @param context cast=(GtkIMContext *) */
	public static final native void _gtk_im_context_reset(long /*int*/ context);
	public static final void gtk_im_context_reset(long /*int*/ context) {
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
	public static final native void _gtk_im_context_set_client_window(long /*int*/ context, long /*int*/ window);
	public static final void gtk_im_context_set_client_window(long /*int*/ context, long /*int*/ window) {
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
	public static final native void _gtk_im_context_set_cursor_location(long /*int*/ context, GdkRectangle area);
	public static final void gtk_im_context_set_cursor_location(long /*int*/ context, GdkRectangle area) {
		lock.lock();
		try {
			_gtk_im_context_set_cursor_location(context, area);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param context cast=(GtkIMMulticontext *)
	 * @param menushell cast=(GtkMenuShell *)
	 */
	public static final native void _gtk_im_multicontext_append_menuitems (long /*int*/ context, long /*int*/ menushell);
	public static final void gtk_im_multicontext_append_menuitems (long /*int*/ context, long /*int*/ menushell) {
		lock.lock();
		try {
			_gtk_im_multicontext_append_menuitems(context, menushell);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_im_multicontext_new();
	public static final long /*int*/ gtk_im_multicontext_new() {
		lock.lock();
		try {
			return _gtk_im_multicontext_new();
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_image_new();
	public static final long /*int*/ gtk_image_new() {
		lock.lock();
		try {
			return _gtk_image_new();
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param image cast=(GtkImage *)
	 * @param pixel_size cast=(gint)
	 */
	public static final native void /*int*/ _gtk_image_set_pixel_size(long /*int*/ image, int pixel_size);
	public static final void /*int*/ gtk_image_set_pixel_size(long /*int*/ image, int pixel_size) {
		lock.lock();
		try {
			_gtk_image_set_pixel_size(image, pixel_size);
		} finally {
			lock.unlock();
		}
	}
	/** @param pixbuf cast=(GdkPixbuf *) */
	public static final native long /*int*/ _gtk_image_new_from_pixbuf(long /*int*/ pixbuf);
	public static final long /*int*/ gtk_image_new_from_pixbuf(long /*int*/ pixbuf) {
		lock.lock();
		try {
			return _gtk_image_new_from_pixbuf(pixbuf);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param image cast=(GtkImage *)
	 * @param pixbuf cast=(GdkPixbuf *)
	 */
	public static final native void _gtk_image_set_from_pixbuf(long /*int*/ image, long /*int*/ pixbuf);
	public static final void gtk_image_set_from_pixbuf(long /*int*/ image, long /*int*/ pixbuf) {
		lock.lock();
		try {
			_gtk_image_set_from_pixbuf(image, pixbuf);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param image cast=(GtkImage *)
	 * @param gicon cast=(GIcon *)
	 * @param size cast=(GtkIconSize)
	 */
	public static final native void _gtk_image_set_from_gicon(long /*int*/ image, long /*int*/ gicon, int size);
	/** [GTK3 only] */
	public static final void gtk_image_set_from_gicon(long /*int*/ image, long /*int*/ gicon, int size) {
		lock.lock();
		try {
			_gtk_image_set_from_gicon(image, gicon, size);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param image cast=(GtkImage *)
	 * @param gicon cast=(GIcon *)
	 */
	public static final native void _gtk_image_set_from_gicon(long /*int*/ image, long /*int*/ gicon);
	/** [GTK4 only] */
	public static final void gtk_image_set_from_gicon(long /*int*/ image, long /*int*/ gicon) {
		lock.lock();
		try {
			_gtk_image_set_from_gicon(image, gicon);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param icon_name cast=(const gchar *)
	 * @param size cast=(GtkIconSize)
	 */
	public static final native long /*int*/ _gtk_image_new_from_icon_name (byte[] icon_name, int size);
	/** [GTK3 only] */
	public static final long /*int*/ gtk_image_new_from_icon_name (byte[] icon_name, int size) {
		lock.lock();
		try {
			return _gtk_image_new_from_icon_name (icon_name, size);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param icon_name cast=(const gchar *)
	 */
	public static final native long /*int*/ _gtk_image_new_from_icon_name (byte[] icon_name);
	/** [GTK4 only] */
	public static final long /*int*/ gtk_image_new_from_icon_name (byte[] icon_name) {
		lock.lock();
		try {
			return _gtk_image_new_from_icon_name (icon_name);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param image cast=(GtkImage *)
	 * @param icon_name cast=(const gchar *)
	 * @param size cast=(GtkIconSize)
	 */
	public static final native void _gtk_image_set_from_icon_name (long /*int*/ image, byte[] icon_name, int size);
	/** [GTK3 only] */
	public static final void gtk_image_set_from_icon_name (long /*int*/ image, byte[] icon_name, int size) {
		lock.lock();
		try {
			_gtk_image_set_from_icon_name (image, icon_name, size);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param image cast=(GtkImage *)
	 * @param icon_name cast=(const gchar *)
	 */
	public static final native void _gtk_image_set_from_icon_name (long /*int*/ image, byte[] icon_name);
	/** [GTK4 only] */
	public static final void gtk_image_set_from_icon_name (long /*int*/ image, byte[] icon_name) {
		lock.lock();
		try {
			_gtk_image_set_from_icon_name (image, icon_name);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param argc cast=(int *)
	 * @param argv cast=(char ***)
	 */
	public static final native boolean _gtk_init_check(long /*int*/[] argc, long /*int*/[] argv);
	/** [GTK3 only] */
	public static final boolean gtk_init_check(long /*int*/[] argc, long /*int*/[] argv) {
		lock.lock();
		try {
			return _gtk_init_check(argc, argv);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native boolean _gtk_init_check();
	/** [GTK4 only] */
	public static final boolean gtk_init_check() {
		lock.lock();
		try {
			return _gtk_init_check();
		} finally {
			lock.unlock();
		}
	}
	/** @param label cast=(GtkLabel *) */
	public static final native long /*int*/ _gtk_label_get_layout(long /*int*/ label);
	public static final long /*int*/ gtk_label_get_layout(long /*int*/ label) {
		lock.lock();
		try {
			return _gtk_label_get_layout(label);
		} finally {
			lock.unlock();
		}
	}
	/** @param label cast=(GtkLabel *) */
	public static final native int _gtk_label_get_mnemonic_keyval(long /*int*/ label);
	public static final int gtk_label_get_mnemonic_keyval(long /*int*/ label) {
		lock.lock();
		try {
			return _gtk_label_get_mnemonic_keyval(label);
		} finally {
			lock.unlock();
		}
	}
	/** @param label cast=(const gchar *) */
	public static final native long /*int*/ _gtk_label_new(byte[] label);
	public static final long /*int*/ gtk_label_new(byte[] label) {
		lock.lock();
		try {
			return _gtk_label_new(label);
		} finally {
			lock.unlock();
		}
	}
	/** @param str cast=(const gchar *) */
	public static final native long /*int*/ _gtk_label_new_with_mnemonic(byte[] str);
	public static final long /*int*/ gtk_label_new_with_mnemonic(byte[] str) {
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
	public static final native void _gtk_label_set_attributes(long /*int*/ label, long /*int*/ attrs);
	public static final void gtk_label_set_attributes(long /*int*/ label, long /*int*/ attrs) {
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
	public static final native void _gtk_label_set_justify(long /*int*/ label, int jtype);
	public static final void gtk_label_set_justify(long /*int*/ label, int jtype) {
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
	public static final native void _gtk_label_set_line_wrap(long /*int*/ label, boolean wrap);
	public static final void gtk_label_set_line_wrap(long /*int*/ label, boolean wrap) {
		lock.lock();
		try {
			_gtk_label_set_line_wrap(label, wrap);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param label cast=(GtkLabel *)
	 * @param wrap_mode cast=(PangoWrapMode)
	 */
	public static final native void _gtk_label_set_line_wrap_mode(long /*int*/ label, int wrap_mode);
	public static final void gtk_label_set_line_wrap_mode(long /*int*/ label, int wrap_mode) {
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
	public static final native void _gtk_label_set_text(long /*int*/ label, long /*int*/ str);
	public static final void gtk_label_set_text(long /*int*/ label, long /*int*/ str) {
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
	public static final native void _gtk_label_set_text(long /*int*/ label, byte[] str);
	public static final void gtk_label_set_text(long /*int*/ label, byte[] str) {
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
	public static final native void _gtk_label_set_text_with_mnemonic(long /*int*/ label, byte[] str);
	public static final void gtk_label_set_text_with_mnemonic(long /*int*/ label, byte[] str) {
		lock.lock();
		try {
			_gtk_label_set_text_with_mnemonic(label, str);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param list_store cast=(GtkListStore *)
	 * @param iter cast=(GtkTreeIter *)
	 */
	public static final native void _gtk_list_store_append(long /*int*/ list_store, long /*int*/ iter);
	public static final void gtk_list_store_append(long /*int*/ list_store, long /*int*/ iter) {
		lock.lock();
		try {
			_gtk_list_store_append(list_store, iter);
		} finally {
			lock.unlock();
		}
	}
	/** @param store cast=(GtkListStore *) */
	public static final native void _gtk_list_store_clear(long /*int*/ store);
	public static final void gtk_list_store_clear(long /*int*/ store) {
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
	public static final native void _gtk_list_store_insert(long /*int*/ list_store, long /*int*/ iter, int position);
	public static final void gtk_list_store_insert(long /*int*/ list_store, long /*int*/ iter, int position) {
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
	public static final native long /*int*/ _gtk_list_store_newv(int numColumns, long /*int*/[] types);
	public static final long /*int*/ gtk_list_store_newv(int numColumns, long /*int*/[] types) {
		lock.lock();
		try {
			return _gtk_list_store_newv(numColumns, types);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param css_provider cast=(GtkCssProvider *)
	 * @param data cast=(const gchar *)
	 * @param length cast=(gssize)
	 * @param error cast=(GError **)
	 */
	public static final native boolean _gtk_css_provider_load_from_data(long /*int*/ css_provider, byte[] data, long /*int*/ length, long /*int*/ error[]);
	/** [GTK3 only, if-def'd in os.h] */
	public static final boolean gtk_css_provider_load_from_data(long /*int*/ css_provider, byte[] data, long /*int*/ length, long /*int*/ error[] ) {
		lock.lock();
		try {
			return _gtk_css_provider_load_from_data(css_provider, data, length, error);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param css_provider cast=(GtkCssProvider *)
	 * @param data cast=(const gchar *)
	 * @param length cast=(gssize)
	 */
	public static final native void _gtk_css_provider_load_from_data(long /*int*/ css_provider, byte[] data, long /*int*/ length);
	/** [GTK4 only, if-def'd in os.h] */
	public static final void gtk_css_provider_load_from_data(long /*int*/ css_provider, byte[] data, long /*int*/ length) {
		lock.lock();
		try {
			_gtk_css_provider_load_from_data(css_provider, data, length);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_css_provider_new();
	public static final long /*int*/gtk_css_provider_new() {
		lock.lock();
		try {
			return _gtk_css_provider_new();
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param provider cast=(GtkCssProvider *)
	 */
	public static final native long /*int*/ _gtk_css_provider_to_string(long /*int*/ provider);
	public static final long /*int*/ gtk_css_provider_to_string(long /*int*/ provider) {
		lock.lock();
		try {
			return _gtk_css_provider_to_string(provider);
		} finally {
			lock.unlock();
		}
	}
	/**
	 *  @param name cast=(const gchar *)
	 *  @param variant cast=(const gchar *)
	 */
	public static final native long /*int*/ _gtk_css_provider_get_named (byte[] name, byte[] variant);
	public static final long /*int*/ gtk_css_provider_get_named(byte[] name, byte[] variant) {
		lock.lock();
		try {
			return _gtk_css_provider_get_named(name, variant);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param screen cast=(GdkScreen *)
	 * @param provider cast=(GtkStyleProvider *)
	 * @param priority cast=(guint)
	 */
	public static final native void _gtk_style_context_add_provider_for_screen (long /*int*/ screen, long /*int*/ provider, int priority);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gtk_style_context_add_provider_for_screen(long /*int*/ screen, long /*int*/ provider, int priority) {
		lock.lock();
		try {
			_gtk_style_context_add_provider_for_screen(screen, provider, priority);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param display cast=(GdkDisplay *)
	 * @param provider cast=(GtkStyleProvider *)
	 * @param priority cast=(guint)
	 */
	public static final native void _gtk_style_context_add_provider_for_display (long /*int*/ display, long /*int*/ provider, int priority);
	/** [GTK4 only] */
	public static final void gtk_style_context_add_provider_for_display(long /*int*/ display, long /*int*/ provider, int priority) {
		lock.lock();
		try {
			_gtk_style_context_add_provider_for_display(display, provider, priority);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param context cast=(GtkStyleContext *)
	 * @param provider cast=(GtkStyleProvider *)
	 * @param priority cast=(guint)
	 */
	public static final native void _gtk_style_context_add_provider (long /*int*/ context, long /*int*/ provider, int priority);
	public static final void gtk_style_context_add_provider(
		long /*int*/context, long /*int*/provider, int priority) {
		lock.lock();
		try {
			_gtk_style_context_add_provider(context, provider, priority);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param list_store cast=(GtkListStore *)
	 * @param iter cast=(GtkTreeIter *)
	 */
	public static final native void _gtk_list_store_remove(long /*int*/ list_store, long /*int*/ iter);
	public static final void gtk_list_store_remove(long /*int*/ list_store, long /*int*/ iter) {
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
	public static final native void _gtk_list_store_set(long /*int*/ store, long /*int*/ iter, int column, byte[] value, int terminator);
	public static final void gtk_list_store_set(long /*int*/ store, long /*int*/ iter, int column, byte[] value, int terminator) {
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
	public static final native void _gtk_list_store_set(long /*int*/ store, long /*int*/ iter, int column, int value, int terminator);
	public static final void gtk_list_store_set(long /*int*/ store, long /*int*/ iter, int column, int value, int terminator) {
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
	public static final native void _gtk_list_store_set(long /*int*/ store, long /*int*/ iter, int column, long value, int terminator);
	public static final void gtk_list_store_set(long /*int*/ store, long /*int*/ iter, int column, long value, int terminator) {
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
	public static final native void _gtk_list_store_set(long /*int*/ store, long /*int*/ iter, int column, GdkRGBA value, int terminator);
	public static final void gtk_list_store_set(long /*int*/ store, long /*int*/ iter, int column, GdkRGBA value, int terminator) {
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
	public static final native void _gtk_list_store_set(long /*int*/ store, long /*int*/ iter, int column, boolean value, int terminator);
	public static final void gtk_list_store_set(long /*int*/ store, long /*int*/ iter, int column, boolean value, int terminator) {
		lock.lock();
		try {
			_gtk_list_store_set(store, iter, column, value, terminator);
		} finally {
			lock.unlock();
		}
	}
	public static final native int _gtk_get_major_version();
	public static final int gtk_get_major_version() {
		lock.lock();
		try {
			return _gtk_get_major_version();
		} finally {
			lock.unlock();
		}
	}
	public static final native int _gtk_get_minor_version();
	public static final int gtk_get_minor_version() {
		lock.lock();
		try {
			return _gtk_get_minor_version();
		} finally {
			lock.unlock();
		}
	}
	public static final native int _gtk_get_micro_version();
	public static final int gtk_get_micro_version() {
		lock.lock();
		try {
			return _gtk_get_micro_version();
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
	/** @param event cast=(GdkEvent *) */
	public static final native void _gtk_main_do_event(long /*int*/ event);
	public static final void gtk_main_do_event(long /*int*/ event) {
		lock.lock();
		try {
			_gtk_main_do_event(event);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_menu_bar_new();
	public static final long /*int*/ gtk_menu_bar_new() {
		lock.lock();
		try {
			return _gtk_menu_bar_new();
		} finally {
			lock.unlock();
		}
	}
	/** @param menu_item cast=(GtkMenuItem *) */
	public static final native long /*int*/ _gtk_menu_item_get_submenu(long /*int*/ menu_item);
	public static final long /*int*/ gtk_menu_item_get_submenu(long /*int*/ menu_item) {
		lock.lock();
		try {
			return _gtk_menu_item_get_submenu(menu_item);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_menu_item_new();
	public static final long /*int*/ gtk_menu_item_new() {
		lock.lock();
		try {
			return _gtk_menu_item_new();
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param menu_item cast=(GtkMenuItem *)
	 * @param submenu cast=(GtkWidget *)
	 */
	public static final native void _gtk_menu_item_set_submenu(long /*int*/ menu_item, long /*int*/ submenu);
	public static final void gtk_menu_item_set_submenu(long /*int*/ menu_item, long /*int*/ submenu) {
		lock.lock();
		try {
			_gtk_menu_item_set_submenu(menu_item, submenu);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_menu_new();
	public static final long /*int*/ gtk_menu_new() {
		lock.lock();
		try {
			return _gtk_menu_new();
		} finally {
			lock.unlock();
		}
	}
	/** @param menu cast=(GtkMenu *) */
	public static final native void _gtk_menu_popdown(long /*int*/ menu);
	public static final void gtk_menu_popdown(long /*int*/ menu) {
		lock.lock();
		try {
			_gtk_menu_popdown(menu);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param menu cast=(GtkMenu *)
	 * @param parent_menu_shell cast=(GtkWidget *)
	 * @param parent_menu_item cast=(GtkWidget *)
	 * @param func cast=(GtkMenuPositionFunc)
	 * @param data cast=(gpointer)
	 * @param button cast=(guint)
	 * @param activate_time cast=(guint32)
	 */
	public static final native void _gtk_menu_popup(long /*int*/ menu, long /*int*/ parent_menu_shell, long /*int*/ parent_menu_item, long /*int*/ func, long /*int*/ data, int button, int activate_time);
	/** [GTK2/GTK3; 3.22 deprecated, replaced] */
	public static final void gtk_menu_popup(long /*int*/ menu, long /*int*/ parent_menu_shell, long /*int*/ parent_menu_item, long /*int*/ func, long /*int*/ data, int button, int activate_time) {
		lock.lock();
		try {
			_gtk_menu_popup(menu, parent_menu_shell, parent_menu_item, func, data, button, activate_time);
		} finally {
			lock.unlock();
		}
	}
	/**
	 *  @method flags=dynamic
	 */
	public static final native void _gtk_menu_popup_at_pointer(long /*int*/ menu, long /*int*/ trigger_event);
	public static void gtk_menu_popup_at_pointer(long /*int*/ menu, long /*int*/ trigger_event) {
		lock.lock();
		try {
			_gtk_menu_popup_at_pointer(menu, trigger_event);
		} finally {
			lock.unlock();
		}
	}
	/**
	 *  @method flags=dynamic
	 */
	public static final native void _gtk_menu_popup_at_rect(long /*int*/ menu, long /*int*/ rect_window, GdkRectangle rect, int rect_anchor, int menu_anchor, long /*int*/ trigger_event);
	public static void gtk_menu_popup_at_rect(long /*int*/ menu, long /*int*/ rect_window, GdkRectangle rect, int rect_anchor, int menu_anchor, long /*int*/ trigger_event) {
		lock.lock();
		try {
			_gtk_menu_popup_at_rect(menu, rect_window, rect, rect_anchor, menu_anchor, trigger_event);
		} finally {
			lock.unlock();
		}
	}
	/** @param menu_shell cast=(GtkMenuShell *) */
	public static final native void _gtk_menu_shell_deactivate(long /*int*/ menu_shell);
	public static final void gtk_menu_shell_deactivate(long /*int*/ menu_shell) {
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
	public static final native void _gtk_menu_shell_insert(long /*int*/ menu_shell, long /*int*/ child, int position);
	public static final void gtk_menu_shell_insert(long /*int*/ menu_shell, long /*int*/ child, int position) {
		lock.lock();
		try {
			_gtk_menu_shell_insert(menu_shell, child, position);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param menu_shell cast=(GtkMenuShell *)
	 * @param take_focus cast=(gboolean)
	 */
	public static final native void _gtk_menu_shell_set_take_focus(long /*int*/ menu_shell, boolean take_focus);
	public static final void gtk_menu_shell_set_take_focus(long /*int*/ menu_shell, boolean take_focus) {
		lock.lock();
		try {
			_gtk_menu_shell_set_take_focus(menu_shell, take_focus);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param icon_widget cast=(GtkWidget *)
	 * @param label cast=(const gchar *)
	 */
	public static final native long /*int*/ _gtk_menu_tool_button_new(long /*int*/ icon_widget, byte[] label);
	public static final long /*int*/ gtk_menu_tool_button_new(long /*int*/ icon_widget, byte[] label) {
		lock.lock();
		try {
			return _gtk_menu_tool_button_new(icon_widget, label);
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
	 * @param arg cast=(const gchar *)
	 */
	public static final native long /*int*/ _gtk_message_dialog_new(long /*int*/ parent, int flags, int type, int buttons, byte[] message_format, byte[] arg);
	public static final long /*int*/ gtk_message_dialog_new(long /*int*/ parent, int flags, int type, int buttons, byte[] message_format, byte[] arg) {
		lock.lock();
		try {
			return _gtk_message_dialog_new(parent, flags, type, buttons, message_format, arg);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param misc cast=(GtkMisc *)
	 * @param xalign cast=(gfloat)
	 * @param yalign cast=(gfloat)
	 */
	public static final native void _gtk_misc_set_alignment(long /*int*/ misc, float xalign, float yalign);
	/** [GTK3 only, if-def'd in os.h; 3.14 deprecated, replaced] */
	public static final void gtk_misc_set_alignment(long /*int*/ misc, float xalign, float yalign) {
		lock.lock();
		try {
			_gtk_misc_set_alignment(misc, xalign, yalign);
		} finally {
			lock.unlock();
		}
	}
	/** @param dialog cast=(GtkNativeDialog *) */
	public static final native int _gtk_native_dialog_run(long /*int*/ dialog);
	public static final int gtk_native_dialog_run(long /*int*/ dialog) {
		lock.lock();
		try {
			return _gtk_native_dialog_run(dialog);
		} finally {
			lock.unlock();
		}
	}
	/** @param notebook cast=(GtkNotebook *) */
	public static final native int _gtk_notebook_get_current_page(long /*int*/ notebook);
	public static final int gtk_notebook_get_current_page(long /*int*/ notebook) {
		lock.lock();
		try {
			return _gtk_notebook_get_current_page(notebook);
		} finally {
			lock.unlock();
		}
	}
	/** @param notebook cast=(GtkNotebook *) */
	public static final native boolean _gtk_notebook_get_scrollable(long /*int*/ notebook);
	public static final boolean gtk_notebook_get_scrollable(long /*int*/ notebook) {
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
	public static final native void _gtk_notebook_insert_page(long /*int*/ notebook, long /*int*/ child, long /*int*/ tab_label, int position);
	public static final void gtk_notebook_insert_page(long /*int*/ notebook, long /*int*/ child, long /*int*/ tab_label, int position) {
		lock.lock();
		try {
			_gtk_notebook_insert_page(notebook, child, tab_label, position);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_notebook_new();
	public static final long /*int*/ gtk_notebook_new() {
		lock.lock();
		try {
			return _gtk_notebook_new();
		} finally {
			lock.unlock();
		}
	}
	/** @param notebook cast=(GtkNotebook *) */
	public static final native void _gtk_notebook_next_page(long /*int*/ notebook);
	public static final void gtk_notebook_next_page(long /*int*/ notebook) {
		lock.lock();
		try {
			_gtk_notebook_next_page(notebook);
		} finally {
			lock.unlock();
		}
	}
	/** @param notebook cast=(GtkNotebook *) */
	public static final native void _gtk_notebook_prev_page(long /*int*/ notebook);
	public static final void gtk_notebook_prev_page(long /*int*/ notebook) {
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
	public static final native void _gtk_notebook_remove_page(long /*int*/ notebook, int page_num);
	public static final void gtk_notebook_remove_page(long /*int*/ notebook, int page_num) {
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
	public static final native void _gtk_notebook_set_current_page(long /*int*/ notebook, int page_num);
	public static final void gtk_notebook_set_current_page(long /*int*/ notebook, int page_num) {
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
	public static final native void _gtk_notebook_set_scrollable(long /*int*/ notebook, boolean scrollable);
	public static final void gtk_notebook_set_scrollable(long /*int*/ notebook, boolean scrollable) {
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
	public static final native void _gtk_notebook_set_show_tabs(long /*int*/ notebook, boolean show_tabs);
	public static final void gtk_notebook_set_show_tabs(long /*int*/ notebook, boolean show_tabs) {
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
	public static final native void _gtk_notebook_set_tab_pos(long /*int*/ notebook, int pos);
	public static final void gtk_notebook_set_tab_pos(long /*int*/ notebook, int pos) {
		lock.lock();
		try {
			_gtk_notebook_set_tab_pos(notebook, pos);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param orientable cast=(GtkOrientable *)
	 * @param orientation cast=(GtkOrientation)
	 */
	public static final native void _gtk_orientable_set_orientation(long /*int*/ orientable, int orientation);
	public static final void gtk_orientable_set_orientation(long /*int*/ orientable, int orientation) {
		lock.lock();
		try {
			_gtk_orientable_set_orientation(orientable, orientation);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_page_setup_new ();
	public static final long /*int*/ gtk_page_setup_new () {
		lock.lock();
		try {
			return _gtk_page_setup_new ();
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param setup cast=(GtkPageSetup *)
	 */
	public static final native int _gtk_page_setup_get_orientation(long /*int*/ setup);
	public static final int gtk_page_setup_get_orientation(long /*int*/ setup) {
		lock.lock();
		try {
			return _gtk_page_setup_get_orientation(setup);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param setup cast=(GtkPageSetup *)
	 * @param orientation cast=(GtkPageOrientation)
	 */
	public static final native void _gtk_page_setup_set_orientation(long /*int*/ setup, int orientation);
	public static final void gtk_page_setup_set_orientation(long /*int*/ setup, int orientation) {
		lock.lock();
		try {
			_gtk_page_setup_set_orientation(setup, orientation);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param setup cast=(GtkPageSetup *)
	 */
	public static final native long /*int*/ _gtk_page_setup_get_paper_size(long /*int*/ setup);
	public static final long /*int*/ gtk_page_setup_get_paper_size(long /*int*/ setup) {
		lock.lock();
		try {
			return _gtk_page_setup_get_paper_size(setup);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param setup cast=(GtkPageSetup *)
	 * @param size cast=(GtkPaperSize *)
	 */
	public static final native void _gtk_page_setup_set_paper_size(long /*int*/ setup, long /*int*/ size);
	public static final void gtk_page_setup_set_paper_size(long /*int*/ setup, long /*int*/ size) {
		lock.lock();
		try {
			_gtk_page_setup_set_paper_size(setup, size);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param setup cast=(GtkPageSetup *)
	 * @param unit cast=(GtkUnit)
	 */
	public static final native double _gtk_page_setup_get_top_margin(long /*int*/ setup, int unit);
	public static final double gtk_page_setup_get_top_margin(long /*int*/ setup, int unit) {
		lock.lock();
		try {
			return _gtk_page_setup_get_top_margin(setup, unit);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param setup cast=(GtkPageSetup *)
	 * @param margin cast=(gdouble)
	 * @param unit cast=(GtkUnit)
	 */
	public static final native void _gtk_page_setup_set_top_margin(long /*int*/ setup, double margin, int unit);
	public static final void gtk_page_setup_set_top_margin(long /*int*/ setup, double margin, int unit) {
		lock.lock();
		try {
			_gtk_page_setup_set_top_margin(setup, margin, unit);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param setup cast=(GtkPageSetup *)
	 * @param unit cast=(GtkUnit)
	 *
	 */
	public static final native double _gtk_page_setup_get_bottom_margin(long /*int*/ setup, int unit);
	public static final double gtk_page_setup_get_bottom_margin(long /*int*/ setup, int unit) {
		lock.lock();
		try {
			return _gtk_page_setup_get_bottom_margin(setup, unit);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param setup cast=(GtkPageSetup *)
	 * @param margin cast=(gdouble)
	 * @param unit cast=(GtkUnit)
	 */
	public static final native void _gtk_page_setup_set_bottom_margin(long /*int*/ setup, double margin, int unit);
	public static final void gtk_page_setup_set_bottom_margin(long /*int*/ setup, double margin, int unit) {
		lock.lock();
		try {
			_gtk_page_setup_set_bottom_margin(setup, margin, unit);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param setup cast=(GtkPageSetup *)
	 * @param unit cast=(GtkUnit)
	 */
	public static final native double _gtk_page_setup_get_left_margin(long /*int*/ setup, int unit);
	public static final double gtk_page_setup_get_left_margin(long /*int*/ setup, int unit) {
		lock.lock();
		try {
			return _gtk_page_setup_get_left_margin(setup, unit);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param setup cast=(GtkPageSetup *)
	 * @param margin cast=(gdouble)
	 * @param unit cast=(GtkUnit)
	 */
	public static final native void _gtk_page_setup_set_left_margin(long /*int*/ setup, double margin, int unit);
	public static final void gtk_page_setup_set_left_margin(long /*int*/ setup, double margin, int unit) {
		lock.lock();
		try {
			_gtk_page_setup_set_left_margin(setup, margin, unit);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param setup cast=(GtkPageSetup *)
	 * @param unit cast=(GtkUnit)
	 */
	public static final native double _gtk_page_setup_get_right_margin(long /*int*/ setup, int unit);
	public static final double gtk_page_setup_get_right_margin(long /*int*/ setup, int unit) {
		lock.lock();
		try {
			return _gtk_page_setup_get_right_margin(setup, unit);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param setup cast=(GtkPageSetup *)
	 * @param margin cast=(gdouble)
	 * @param unit cast=(GtkUnit)
	 */
	public static final native void _gtk_page_setup_set_right_margin(long /*int*/ setup, double margin, int unit);
	public static final void gtk_page_setup_set_right_margin(long /*int*/ setup, double margin, int unit) {
		lock.lock();
		try {
			_gtk_page_setup_set_right_margin(setup, margin, unit);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param setup cast=(GtkPageSetup *)
	 * @param unit cast=(GtkUnit)
	 */
	public static final native double _gtk_page_setup_get_paper_width(long /*int*/ setup, int unit);
	public static final double gtk_page_setup_get_paper_width(long /*int*/ setup, int unit) {
		lock.lock();
		try {
			return _gtk_page_setup_get_paper_width(setup, unit);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param setup cast=(GtkPageSetup *)
	 * @param unit cast=(GtkUnit)
	 */
	public static final native double _gtk_page_setup_get_paper_height(long /*int*/ setup, int unit);
	public static final double gtk_page_setup_get_paper_height(long /*int*/ setup, int unit) {
		lock.lock();
		try {
			return _gtk_page_setup_get_paper_height(setup, unit);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param setup cast=(GtkPageSetup *)
	 * @param unit cast=(GtkUnit)
	 */
	public static final native double _gtk_page_setup_get_page_width(long /*int*/ setup, int unit);
	public static final double gtk_page_setup_get_page_width(long /*int*/ setup, int unit) {
		lock.lock();
		try {
			return _gtk_page_setup_get_page_width(setup, unit);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param setup cast=(GtkPageSetup *)
	 * @param unit cast=(GtkUnit)
	 */
	public static final native double _gtk_page_setup_get_page_height(long /*int*/ setup, int unit);
	public static final double gtk_page_setup_get_page_height(long /*int*/ setup, int unit) {
		lock.lock();
		try {
			return _gtk_page_setup_get_page_height(setup, unit);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param context cast=(GtkStyleContext *)
	 * @param cr cast=(cairo_t *)
	 * @param x cast=(gdouble)
	 * @param y cast=(gdouble)
	 * @param width cast=(gdouble)
	 * @param height cast=(gdouble)
	 */
	public static final native void _gtk_render_frame(long /*int*/ context, long /*int*/ cr, double x , double y, double width, double height);
	public static final void gtk_render_frame(long /*int*/ context, long /*int*/ cr, double x , double y, double width, double height) {
		lock.lock();
		try {
			_gtk_render_frame(context, cr, x, y, width, height);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param context cast=(GtkStyleContext *)
	 * @param cr cast=(cairo_t *)
	 * @param x cast=(gdouble)
	 * @param y cast=(gdouble)
	 * @param width cast=(gdouble)
	 * @param height cast=(gdouble)
	 */
	public static final native void _gtk_render_background(long /*int*/ context, long /*int*/ cr, double x , double y, double width, double height);
	public static final void gtk_render_background(long /*int*/ context, long /*int*/ cr, double x , double y, double width, double height) {
		lock.lock();
		try {
			_gtk_render_background(context, cr, x, y, width, height);
		} finally {
			lock.unlock();
		}
	}
	/**
	* @param context cast=(GtkStyleContext *)
	* @param cr cast=(cairo_t *)
	* @param x cast=(gdouble)
	* @param y cast=(gdouble)
	* @param width cast=(gdouble)
	* @param height cast=(gdouble)
	*/
	public static final native void _gtk_render_focus(long /*int*/ context, long /*int*/ cr,  double x , double y, double width, double height);
	public static final void gtk_render_focus(long /*int*/ context, long /*int*/ cr,  double x , double y, double width, double height) {
		lock.lock();
		try {
			_gtk_render_focus(context, cr,  x, y, width, height);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param size cast=(GtkPaperSize *)
	 */
	public static final native void _gtk_paper_size_free(long /*int*/ size);
	public static final void gtk_paper_size_free(long /*int*/ size) {
		lock.lock();
		try {
			_gtk_paper_size_free(size);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param name cast=(const gchar *)
	 */
	public static final native long /*int*/ _gtk_paper_size_new(byte [] name);
	public static final long /*int*/ gtk_paper_size_new(byte [] name) {
		lock.lock();
		try {
			return _gtk_paper_size_new(name);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param ppd_name cast=(const gchar *)
	 * @param ppd_display_name cast=(const gchar *)
	 * @param width cast=(gdouble)
	 * @param height cast=(gdouble)
	 */
	public static final native long /*int*/ _gtk_paper_size_new_from_ppd(byte [] ppd_name, byte [] ppd_display_name, double width, double height);
	public static final long /*int*/ gtk_paper_size_new_from_ppd(byte [] ppd_name, byte [] ppd_display_name, double width, double height) {
		lock.lock();
		try {
			return _gtk_paper_size_new_from_ppd(ppd_name, ppd_display_name, width, height);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param name cast=(const gchar *)
	 * @param display_name cast=(const gchar *)
	 * @param width cast=(gdouble)
	 * @param height cast=(gdouble)
	 * @param unit cast=(GtkUnit)
	 */
	public static final native long /*int*/ _gtk_paper_size_new_custom(byte [] name, byte [] display_name, double width, double height, int unit);
	public static final long /*int*/ gtk_paper_size_new_custom(byte [] name, byte [] display_name, double width, double height, int unit) {
		lock.lock();
		try {
			return _gtk_paper_size_new_custom(name, display_name, width, height, unit);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param size cast=(GtkPaperSize *)
	 */
	public static final native long /*int*/ _gtk_paper_size_get_name(long /*int*/ size);
	public static final long /*int*/ gtk_paper_size_get_name(long /*int*/ size) {
		lock.lock();
		try {
			return _gtk_paper_size_get_name(size);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param size cast=(GtkPaperSize *)
	 */
	public static final native long /*int*/ _gtk_paper_size_get_display_name(long /*int*/ size);
	public static final long /*int*/ gtk_paper_size_get_display_name(long /*int*/ size) {
		lock.lock();
		try {
			return _gtk_paper_size_get_display_name(size);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param size cast=(GtkPaperSize *)
	 */
	public static final native long /*int*/ _gtk_paper_size_get_ppd_name(long /*int*/ size);
	public static final long /*int*/ gtk_paper_size_get_ppd_name(long /*int*/ size) {
		lock.lock();
		try {
			return _gtk_paper_size_get_ppd_name(size);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param size cast=(GtkPaperSize *)
	 * @param unit cast=(GtkUnit)
	 */
	public static final native double _gtk_paper_size_get_width(long /*int*/ size, int unit);
	public static final double gtk_paper_size_get_width(long /*int*/ size, int unit) {
		lock.lock();
		try {
			return _gtk_paper_size_get_width(size, unit);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param size cast=(GtkPaperSize *)
	 * @param unit cast=(GtkUnit)
	 */
	public static final native double _gtk_paper_size_get_height(long /*int*/ size, int unit);
	public static final double gtk_paper_size_get_height(long /*int*/ size, int unit) {
		lock.lock();
		try {
			return _gtk_paper_size_get_height(size, unit);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param size cast=(GtkPaperSize *)
	 */
	public static final native boolean _gtk_paper_size_is_custom(long /*int*/ size);
	public static final boolean gtk_paper_size_is_custom(long /*int*/ size) {
		lock.lock();
		try {
			return _gtk_paper_size_is_custom(size);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_plug_new(long /*int*/ socket_id);
	public static final long /*int*/ gtk_plug_new(long /*int*/ socket_id) {
		lock.lock();
		try {
			return _gtk_plug_new(socket_id);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param printer cast=(GtkPrinter *)
	 */
	public static final native long /*int*/ _gtk_printer_get_backend(long /*int*/ printer);
	public static final long /*int*/ gtk_printer_get_backend(long /*int*/ printer) {
		lock.lock();
		try {
			return _gtk_printer_get_backend(printer);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param printer cast=(GtkPrinter *)
	 */
	public static final native long /*int*/ _gtk_printer_get_name(long /*int*/ printer);
	public static final long /*int*/ gtk_printer_get_name(long /*int*/ printer) {
		lock.lock();
		try {
			return _gtk_printer_get_name(printer);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param printer cast=(GtkPrinter *)
	 */
	public static final native boolean _gtk_printer_is_default(long /*int*/ printer);
	public static final boolean gtk_printer_is_default(long /*int*/ printer) {
		lock.lock();
		try {
			return _gtk_printer_is_default(printer);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param func cast=(GtkPrinterFunc)
	 * @param data cast=(gpointer)
	 * @param destroy cast=(GDestroyNotify)
	 * @param wait cast=(gboolean)
	 */
	public static final native void _gtk_enumerate_printers(long /*int*/ func, long /*int*/data, long /*int*/ destroy, boolean wait);
	public static final void gtk_enumerate_printers(long /*int*/ func, long /*int*/data, long /*int*/ destroy, boolean wait) {
		lock.lock();
		try {
			_gtk_enumerate_printers(func, data, destroy, wait);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param title cast=(const gchar *)
	 * @param printer cast=(GtkPrinter *)
	 * @param settings cast=(GtkPrintSettings *)
	 * @param page_setup cast=(GtkPageSetup *)
	 */
	public static final native long /*int*/ _gtk_print_job_new(byte[] title, long /*int*/ printer, long /*int*/ settings, long /*int*/ page_setup);
	public static final long /*int*/ gtk_print_job_new(byte[] title, long /*int*/ printer, long /*int*/ settings, long /*int*/ page_setup) {
		lock.lock();
		try {
			return _gtk_print_job_new(title, printer, settings, page_setup);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param job cast=(GtkPrintJob *)
	 * @param error cast=(GError **)
	 */
	public static final native long /*int*/ _gtk_print_job_get_surface(long /*int*/ job, long /*int*/ error[]);
	public static final long /*int*/ gtk_print_job_get_surface(long /*int*/ job, long /*int*/ error[]) {
		lock.lock();
		try {
			return _gtk_print_job_get_surface(job, error);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param job cast=(GtkPrintJob *)
	 * @param callback cast=(GtkPrintJobCompleteFunc)
	 * @param user_data cast=(gpointer)
	 * @param dnotify cast=(GDestroyNotify)
	 */
	public static final native void _gtk_print_job_send(long /*int*/ job, long /*int*/ callback, long /*int*/ user_data, long /*int*/ dnotify);
	public static final void gtk_print_job_send(long /*int*/ job, long /*int*/ callback, long /*int*/ user_data, long /*int*/ dnotify) {
		lock.lock();
		try {
			_gtk_print_job_send(job, callback, user_data, dnotify);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_print_settings_new();
	public static final long /*int*/ gtk_print_settings_new() {
		lock.lock();
		try {
			return _gtk_print_settings_new();
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param settings cast=(GtkPrintSettings *)
	 * @param func cast=(GtkPrintSettingsFunc)
	 * @param data cast=(gpointer)
	 */
	public static final native void _gtk_print_settings_foreach(long /*int*/ settings, long /*int*/ func, long /*int*/ data);
	public static final void gtk_print_settings_foreach(long /*int*/ settings, long /*int*/ func, long /*int*/ data) {
		lock.lock();
		try {
			_gtk_print_settings_foreach(settings, func, data);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param settings cast=(GtkPrintSettings *)
	 * @param key cast=(const gchar *)
	 */
	public static final native long /*int*/ _gtk_print_settings_get(long /*int*/ settings, byte [] key);
	public static final long /*int*/ gtk_print_settings_get(long /*int*/ settings, byte [] key) {
		lock.lock();
		try {
			return _gtk_print_settings_get(settings, key);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param settings cast=(GtkPrintSettings *)
	 * @param key cast=(const gchar *)
	 * @param value cast=(const gchar *)
	 */
	public static final native void _gtk_print_settings_set(long /*int*/ settings, byte [] key, byte [] value);
	public static final void gtk_print_settings_set(long /*int*/ settings, byte [] key, byte [] value) {
		lock.lock();
		try {
			_gtk_print_settings_set(settings, key, value);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param settings cast=(GtkPrintSettings *)
	 * @param printer cast=(const gchar *)
	 */
	public static final native void _gtk_print_settings_set_printer(long /*int*/ settings, byte[] printer);
	public static final void gtk_print_settings_set_printer(long /*int*/ settings, byte[] printer) {
		lock.lock();
		try {
			_gtk_print_settings_set_printer(settings, printer);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param settings cast=(GtkPrintSettings *)
	 * @param orientation cast=(GtkPageOrientation)
	 */
	public static final native void _gtk_print_settings_set_orientation(long /*int*/ settings, int orientation);
	public static final void gtk_print_settings_set_orientation(long /*int*/ settings, int orientation) {
		lock.lock();
		try {
			_gtk_print_settings_set_orientation(settings, orientation);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param settings cast=(GtkPrintSettings *)
	 */
	public static final native boolean _gtk_print_settings_get_collate(long /*int*/ settings);
	public static final boolean gtk_print_settings_get_collate(long /*int*/ settings) {
		lock.lock();
		try {
			return _gtk_print_settings_get_collate(settings);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param settings cast=(GtkPrintSettings *)
	 * @param collate cast=(gboolean)
	 */
	public static final native void _gtk_print_settings_set_collate(long /*int*/ settings, boolean collate);
	public static final void gtk_print_settings_set_collate(long /*int*/ settings, boolean collate) {
		lock.lock();
		try {
			_gtk_print_settings_set_collate(settings, collate);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param settings cast=(GtkPrintSettings *)
	 */
	public static final native int _gtk_print_settings_get_duplex(long /*int*/ settings);
	public static final int gtk_print_settings_get_duplex(long /*int*/ settings) {
		lock.lock();
		try {
			return _gtk_print_settings_get_duplex(settings);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param settings cast=(GtkPrintSettings *)
	 * @param duplex cast=(GtkPrintDuplex)
	 */
	public static final native void _gtk_print_settings_set_duplex(long /*int*/ settings, int duplex);
	public static final void gtk_print_settings_set_duplex(long /*int*/ settings, int duplex) {
		lock.lock();
		try {
			_gtk_print_settings_set_duplex(settings, duplex);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param settings cast=(GtkPrintSettings *)
	 */
	public static final native int _gtk_print_settings_get_n_copies(long /*int*/ settings);
	public static final int gtk_print_settings_get_n_copies(long /*int*/ settings) {
		lock.lock();
		try {
			return _gtk_print_settings_get_n_copies(settings);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param settings cast=(GtkPrintSettings *)
	 * @param num_copies cast=(gint)
	 */
	public static final native void _gtk_print_settings_set_n_copies(long /*int*/ settings, int num_copies);
	public static final void gtk_print_settings_set_n_copies(long /*int*/ settings, int num_copies) {
		lock.lock();
		try {
			_gtk_print_settings_set_n_copies(settings, num_copies);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param settings cast=(GtkPrintSettings *)
	 */
	public static final native int _gtk_print_settings_get_print_pages(long /*int*/ settings);
	public static final int gtk_print_settings_get_print_pages(long /*int*/ settings) {
		lock.lock();
		try {
			return _gtk_print_settings_get_print_pages(settings);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param settings cast=(GtkPrintSettings *)
	 * @param pages cast=(GtkPrintPages)
	 */
	public static final native void _gtk_print_settings_set_print_pages(long /*int*/ settings, int pages);
	public static final void gtk_print_settings_set_print_pages(long /*int*/ settings, int pages) {
		lock.lock();
		try {
			_gtk_print_settings_set_print_pages(settings, pages);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param settings cast=(GtkPrintSettings *)
	 * @param num_ranges cast=(gint *)
	 */
	public static final native long /*int*/ _gtk_print_settings_get_page_ranges(long /*int*/ settings, int[] num_ranges);
	public static final long /*int*/ gtk_print_settings_get_page_ranges(long /*int*/ settings, int[] num_ranges) {
		lock.lock();
		try {
			return _gtk_print_settings_get_page_ranges(settings, num_ranges);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param settings cast=(GtkPrintSettings *)
	 * @param page_ranges cast=(GtkPageRange *)
	 * @param num_ranges cast=(gint)
	 */
	public static final native void _gtk_print_settings_set_page_ranges(long /*int*/ settings, int[] page_ranges, int num_ranges);
	public static final void gtk_print_settings_set_page_ranges(long /*int*/ settings, int[] page_ranges, int num_ranges) {
		lock.lock();
		try {
			_gtk_print_settings_set_page_ranges(settings, page_ranges, num_ranges);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param settings cast=(GtkPrintSettings *)
	 */
	public static final native int _gtk_print_settings_get_resolution(long /*int*/ settings);
	public static final int gtk_print_settings_get_resolution(long /*int*/ settings) {
		lock.lock();
		try {
			return _gtk_print_settings_get_resolution(settings);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param title cast=(const gchar *)
	 * @param parent cast=(GtkWindow *)
	 */
	public static final native long /*int*/ _gtk_print_unix_dialog_new(byte[] title, long /*int*/ parent);
	public static final long /*int*/ gtk_print_unix_dialog_new(byte[] title, long /*int*/ parent) {
		lock.lock();
		try {
			return _gtk_print_unix_dialog_new(title, parent);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param dialog cast=(GtkPrintUnixDialog *)
	 * @param embed cast=(gboolean)
	 */
	public static final native void _gtk_print_unix_dialog_set_embed_page_setup(long /*int*/ dialog, boolean embed);
	public static final void gtk_print_unix_dialog_set_embed_page_setup(long /*int*/ dialog, boolean embed) {
		lock.lock();
		try {
			_gtk_print_unix_dialog_set_embed_page_setup(dialog, embed);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param dialog cast=(GtkPrintUnixDialog *)
	 * @param page_setup cast=(GtkPageSetup *)
	 */
	public static final native void _gtk_print_unix_dialog_set_page_setup(long /*int*/ dialog, long /*int*/ page_setup);
	public static final void gtk_print_unix_dialog_set_page_setup(long /*int*/ dialog, long /*int*/ page_setup) {
		lock.lock();
		try {
			_gtk_print_unix_dialog_set_page_setup(dialog, page_setup);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param dialog cast=(GtkPrintUnixDialog *)
	 */
	public static final native long /*int*/ _gtk_print_unix_dialog_get_page_setup(long /*int*/ dialog);
	public static final long /*int*/ gtk_print_unix_dialog_get_page_setup(long /*int*/ dialog) {
		lock.lock();
		try {
			return _gtk_print_unix_dialog_get_page_setup(dialog);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param dialog cast=(GtkPrintUnixDialog *)
	 * @param current_page cast=(gint)
	 */
	public static final native void _gtk_print_unix_dialog_set_current_page(long /*int*/ dialog, int current_page);
	public static final void gtk_print_unix_dialog_set_current_page(long /*int*/ dialog, int current_page) {
		lock.lock();
		try {
			_gtk_print_unix_dialog_set_current_page(dialog, current_page);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param dialog cast=(GtkPrintUnixDialog *)
	 */
	public static final native int _gtk_print_unix_dialog_get_current_page(long /*int*/ dialog);
	public static final int gtk_print_unix_dialog_get_current_page(long /*int*/ dialog) {
		lock.lock();
		try {
			return _gtk_print_unix_dialog_get_current_page(dialog);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param dialog cast=(GtkPrintUnixDialog *)
	 * @param settings cast=(GtkPrintSettings *)
	 */
	public static final native void _gtk_print_unix_dialog_set_settings(long /*int*/ dialog, long /*int*/ settings);
	public static final void gtk_print_unix_dialog_set_settings(long /*int*/ dialog, long /*int*/ settings) {
		lock.lock();
		try {
			_gtk_print_unix_dialog_set_settings(dialog, settings);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param dialog cast=(GtkPrintUnixDialog *)
	 */
	public static final native long /*int*/ _gtk_print_unix_dialog_get_settings(long /*int*/ dialog);
	public static final long /*int*/ gtk_print_unix_dialog_get_settings(long /*int*/ dialog) {
		lock.lock();
		try {
			return _gtk_print_unix_dialog_get_settings(dialog);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param dialog cast=(GtkPrintUnixDialog *)
	 */
	public static final native long /*int*/ _gtk_print_unix_dialog_get_selected_printer(long /*int*/ dialog);
	public static final long /*int*/ gtk_print_unix_dialog_get_selected_printer(long /*int*/ dialog) {
		lock.lock();
		try {
			return _gtk_print_unix_dialog_get_selected_printer(dialog);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param dialog cast=(GtkPrintUnixDialog *)
	 * @param capabilities cast=(GtkPrintCapabilities)
	 */
	public static final native void _gtk_print_unix_dialog_set_manual_capabilities(long /*int*/ dialog, long /*int*/ capabilities);
	public static final void gtk_print_unix_dialog_set_manual_capabilities(long /*int*/ dialog, long /*int*/ capabilities) {
		lock.lock();
		try {
			_gtk_print_unix_dialog_set_manual_capabilities(dialog, capabilities);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_progress_bar_new();
	public static final long /*int*/ gtk_progress_bar_new() {
		lock.lock();
		try {
			return _gtk_progress_bar_new();
		} finally {
			lock.unlock();
		}
	}
	/** @param pbar cast=(GtkProgressBar *) */
	public static final native void _gtk_progress_bar_pulse(long /*int*/ pbar);
	public static final void gtk_progress_bar_pulse(long /*int*/ pbar) {
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
	public static final native void _gtk_progress_bar_set_fraction(long /*int*/ pbar, double fraction);
	public static final void gtk_progress_bar_set_fraction(long /*int*/ pbar, double fraction) {
		lock.lock();
		try {
			_gtk_progress_bar_set_fraction(pbar, fraction);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param pbar cast=(GtkProgressBar *)
	 * @param inverted cast=(gboolean)
	 */
	public static final native void _gtk_progress_bar_set_inverted(long /*int*/ pbar, boolean inverted);
	public static final void gtk_progress_bar_set_inverted(long /*int*/ pbar, boolean inverted) {
		lock.lock();
		try {
			_gtk_progress_bar_set_inverted(pbar, inverted);
		} finally {
			lock.unlock();
		}
	}
	/** @param radio_button cast=(GtkRadioButton *) */
	public static final native long /*int*/ _gtk_radio_button_get_group(long /*int*/ radio_button);
	public static final long /*int*/ gtk_radio_button_get_group(long /*int*/ radio_button) {
		lock.lock();
		try {
			return _gtk_radio_button_get_group(radio_button);
		} finally {
			lock.unlock();
		}
	}
	/** @param group cast=(GSList *) */
	public static final native long /*int*/ _gtk_radio_button_new(long /*int*/ group);
	public static final long /*int*/ gtk_radio_button_new(long /*int*/ group) {
		lock.lock();
		try {
			return _gtk_radio_button_new(group);
		} finally {
			lock.unlock();
		}
	}
	/** @param radio_menu_item cast=(GtkRadioMenuItem *) */
	public static final native long /*int*/ _gtk_radio_menu_item_get_group(long /*int*/ radio_menu_item);
	public static final long /*int*/ gtk_radio_menu_item_get_group(long /*int*/ radio_menu_item) {
		lock.lock();
		try {
			return _gtk_radio_menu_item_get_group(radio_menu_item);
		} finally {
			lock.unlock();
		}
	}
	/** @param group cast=(GSList *) */
	public static final native long /*int*/ _gtk_radio_menu_item_new(long /*int*/ group);
	public static final long /*int*/ gtk_radio_menu_item_new(long /*int*/ group) {
		lock.lock();
		try {
			return _gtk_radio_menu_item_new(group);
		} finally {
			lock.unlock();
		}
	}
	/** @param range cast=(GtkRange *) */
	public static final native long /*int*/ _gtk_range_get_adjustment(long /*int*/ range);
	public static final long /*int*/ gtk_range_get_adjustment(long /*int*/ range) {
		lock.lock();
		try {
			return _gtk_range_get_adjustment(range);
		} finally {
			lock.unlock();
		}
	}
	/** @param range cast=(GtkRange *) */
	public static final native void _gtk_range_set_increments(long /*int*/ range, double step, double page);
	public static final void gtk_range_set_increments(long /*int*/ range, double step, double page) {
		lock.lock();
		try {
			_gtk_range_set_increments(range, step, page);
		} finally {
			lock.unlock();
		}
	}
	/** @param range cast=(GtkRange *) */
	public static final native void _gtk_range_set_inverted(long /*int*/ range, boolean setting);
	public static final void gtk_range_set_inverted(long /*int*/ range, boolean setting) {
		lock.lock();
		try {
			_gtk_range_set_inverted(range, setting);
		} finally {
			lock.unlock();
		}
	}
	/** @param range cast=(GtkRange *) */
	public static final native void _gtk_range_set_range(long /*int*/ range, double min, double max);
	public static final void gtk_range_set_range(long /*int*/ range, double min, double max) {
		lock.lock();
		try {
			_gtk_range_set_range(range, min, max);
		} finally {
			lock.unlock();
		}
	}
	/** @param range cast=(GtkRange *) */
	public static final native void _gtk_range_set_value(long /*int*/ range, double value);
	public static final void gtk_range_set_value(long /*int*/ range, double value) {
		lock.lock();
		try {
			_gtk_range_set_value(range, value);
		} finally {
			lock.unlock();
		}
	}
	/**
	 *  @param range cast=(GtkRange *)
	 *  @param slider_start cast=(gint *)
	 *  @param slider_end cast=(gint *)
	 */
	public static final native void _gtk_range_get_slider_range(long /*int*/ range, int[] slider_start, int[] slider_end);
	public static final void gtk_range_get_slider_range(long /*int*/ range, int[] slider_start, int[] slider_end) {
	        lock.lock();
	        try {
	        	_gtk_range_get_slider_range(range, slider_start, slider_end);
	        } finally {
	                lock.unlock();
	        }
	}
	/**
	 * @param scale cast=(GtkScale *)
	 * @param digits cast=(gint)
	 */
	public static final native void _gtk_scale_set_digits(long /*int*/ scale, int digits);
	public static final void gtk_scale_set_digits(long /*int*/ scale, int digits) {
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
	public static final native void _gtk_scale_set_draw_value(long /*int*/ scale, boolean draw_value);
	public static final void gtk_scale_set_draw_value(long /*int*/ scale, boolean draw_value) {
		lock.lock();
		try {
			_gtk_scale_set_draw_value(scale, draw_value);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param scrollable cast=(GtkScrollable *)
	 */
	public static final native long /*int*/ _gtk_scrollable_get_vadjustment(long /*int*/ scrollable);
	public static final long /*int*/ gtk_scrollable_get_vadjustment(long /*int*/ scrollable) {
		lock.lock();
		try {
			return _gtk_scrollable_get_vadjustment(scrollable);
		} finally {
			lock.unlock();
		}
	}
	/** @param scrolled_window cast=(GtkScrolledWindow *) */
	public static final native long /*int*/ _gtk_scrolled_window_get_hadjustment(long /*int*/ scrolled_window);
	public static final long /*int*/ gtk_scrolled_window_get_hadjustment(long /*int*/ scrolled_window) {
		lock.lock();
		try {
			return _gtk_scrolled_window_get_hadjustment(scrolled_window);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param scrolled_window cast=(GtkScrolledWindow *)
	 */
	public static final native long /*int*/ _gtk_scrolled_window_get_hscrollbar(long /*int*/ scrolled_window);
	public static final long /*int*/ gtk_scrolled_window_get_hscrollbar(long /*int*/ scrolled_window) {
		lock.lock();
		try {
			return _gtk_scrolled_window_get_hscrollbar(scrolled_window);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param scrolled_window cast=(GtkScrolledWindow *)
	 * @param hscrollbar_policy cast=(GtkPolicyType *)
	 * @param vscrollbar_policy cast=(GtkPolicyType *)
	 */
	public static final native void _gtk_scrolled_window_get_policy(long /*int*/ scrolled_window, int[] hscrollbar_policy, int[] vscrollbar_policy);
	public static final void gtk_scrolled_window_get_policy(long /*int*/ scrolled_window, int[] hscrollbar_policy, int[] vscrollbar_policy) {
		lock.lock();
		try {
			_gtk_scrolled_window_get_policy(scrolled_window, hscrollbar_policy, vscrollbar_policy);
		} finally {
			lock.unlock();
		}
	}
	/** @param scrolled_window cast=(GtkScrolledWindow *) */
	public static final native int _gtk_scrolled_window_get_shadow_type(long /*int*/ scrolled_window);
	public static final int gtk_scrolled_window_get_shadow_type(long /*int*/ scrolled_window) {
		lock.lock();
		try {
			return _gtk_scrolled_window_get_shadow_type(scrolled_window);
		} finally {
			lock.unlock();
		}
	}
	/** @param scrolled_window cast=(GtkScrolledWindow *) */
	public static final native long /*int*/ _gtk_scrolled_window_get_vadjustment(long /*int*/ scrolled_window);
	public static final long /*int*/ gtk_scrolled_window_get_vadjustment(long /*int*/ scrolled_window) {
		lock.lock();
		try {
			return _gtk_scrolled_window_get_vadjustment(scrolled_window);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param scrolled_window cast=(GtkScrolledWindow *)
	 */
	public static final native long /*int*/ _gtk_scrolled_window_get_vscrollbar(long /*int*/ scrolled_window);
	public static final long /*int*/ gtk_scrolled_window_get_vscrollbar(long /*int*/ scrolled_window) {
		lock.lock();
		try {
			return _gtk_scrolled_window_get_vscrollbar(scrolled_window);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param hadjustment cast=(GtkAdjustment *)
	 * @param vadjustment cast=(GtkAdjustment *)
	 */
	public static final native long /*int*/ _gtk_scrolled_window_new(long /*int*/ hadjustment, long /*int*/ vadjustment);
	public static final long /*int*/ gtk_scrolled_window_new(long /*int*/ hadjustment, long /*int*/ vadjustment) {
		lock.lock();
		try {
			return _gtk_scrolled_window_new(hadjustment, vadjustment);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param scrolled_window cast=(GtkScrolledWindow *)
	 * @param hscrollbar_policy cast=(GtkPolicyType)
	 * @param vscrollbar_policy cast=(GtkPolicyType)
	 */
	public static final native void _gtk_scrolled_window_set_policy(long /*int*/ scrolled_window, int hscrollbar_policy, int vscrollbar_policy);
	public static final void gtk_scrolled_window_set_policy(long /*int*/ scrolled_window, int hscrollbar_policy, int vscrollbar_policy) {
		lock.lock();
		try {
			_gtk_scrolled_window_set_policy(scrolled_window, hscrollbar_policy, vscrollbar_policy);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param scrolled_window cast=(GtkScrolledWindow *)
	 */
	public static final native boolean _gtk_scrolled_window_get_overlay_scrolling(long /*int*/ scrolled_window);
	public static final boolean gtk_scrolled_window_get_overlay_scrolling(long /*int*/ scrolled_window) {
		lock.lock();
		try {
			return _gtk_scrolled_window_get_overlay_scrolling(scrolled_window);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param scrolled_window cast=(GtkScrolledWindow *)
	 * @param type cast=(GtkShadowType)
	 */
	public static final native void _gtk_scrolled_window_set_shadow_type(long /*int*/ scrolled_window, int type);
	public static final void gtk_scrolled_window_set_shadow_type(long /*int*/ scrolled_window, int type) {
		lock.lock();
		try {
			_gtk_scrolled_window_set_shadow_type(scrolled_window, type);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_settings_get_default();
	public static final long /*int*/ gtk_settings_get_default() {
		lock.lock();
		try {
			return _gtk_settings_get_default();
		} finally {
			lock.unlock();
		}
	}
	/** @param selection_data cast=(GtkSelectionData *) */
	public static final native void _gtk_selection_data_free(long /*int*/ selection_data);
	public static final void gtk_selection_data_free(long /*int*/ selection_data) {
		lock.lock();
		try {
			_gtk_selection_data_free(selection_data);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param selection_data cast=(GtkSelectionData *)
	 */
	public static final native long /*int*/ _gtk_selection_data_get_data(long /*int*/ selection_data);
	public static final long /*int*/ gtk_selection_data_get_data(long /*int*/ selection_data) {
		lock.lock();
		try {
			return _gtk_selection_data_get_data(selection_data);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param selection_data cast=(GtkSelectionData *)
	 */
	public static final native int _gtk_selection_data_get_format(long /*int*/ selection_data);
	public static final int gtk_selection_data_get_format(long /*int*/ selection_data) {
		lock.lock();
		try {
			return _gtk_selection_data_get_format(selection_data);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param selection_data cast=(GtkSelectionData *)
	 */
	public static final native int _gtk_selection_data_get_length(long /*int*/ selection_data);
	public static final int gtk_selection_data_get_length(long /*int*/ selection_data) {
		lock.lock();
		try {
			return _gtk_selection_data_get_length(selection_data);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param selection_data cast=(GtkSelectionData *)
	 */
	public static final native long /*int*/ _gtk_selection_data_get_target(long /*int*/ selection_data);
	public static final long /*int*/ gtk_selection_data_get_target(long /*int*/ selection_data) {
		lock.lock();
		try {
			return _gtk_selection_data_get_target(selection_data);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param selection_data cast=(GtkSelectionData *)
	 */
	public static final native long /*int*/ _gtk_selection_data_get_data_type(long /*int*/ selection_data);
	public static final long /*int*/ gtk_selection_data_get_data_type(long /*int*/ selection_data) {
		lock.lock();
		try {
			return _gtk_selection_data_get_data_type(selection_data);
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
	public static final native void _gtk_selection_data_set(long /*int*/ selection_data, long /*int*/ type, int format, long /*int*/ data, int length);
	public static final void gtk_selection_data_set(long /*int*/ selection_data, long /*int*/ type, int format, long /*int*/ data, int length) {
		lock.lock();
		try {
			_gtk_selection_data_set(selection_data, type, format, data, length);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_separator_menu_item_new();
	public static final long /*int*/ gtk_separator_menu_item_new() {
		lock.lock();
		try {
			return _gtk_separator_menu_item_new();
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_separator_tool_item_new();
	public static final long /*int*/ gtk_separator_tool_item_new() {
		lock.lock();
		try {
			return _gtk_separator_tool_item_new();
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param item cast=(GtkSeparatorToolItem *)
	 * @param draw cast=(gboolean)
	 */
	public static final native void _gtk_separator_tool_item_set_draw(long /*int*/ item, boolean draw);
	public static final void gtk_separator_tool_item_set_draw(long /*int*/ item, boolean draw) {
		lock.lock();
		try {
			_gtk_separator_tool_item_set_draw(item, draw);
		} finally {
			lock.unlock();
		}
	}
	/** @param socket cast=(GtkSocket *) */
	public static final native long /*int*/ _gtk_socket_get_id(long /*int*/ socket);
	public static final long /*int*/ gtk_socket_get_id(long /*int*/ socket) {
		lock.lock();
		try {
			return _gtk_socket_get_id(socket);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_socket_new();
	public static final long /*int*/ gtk_socket_new() {
		lock.lock();
		try {
			return _gtk_socket_new();
		} finally {
			lock.unlock();
		}
	}
	/** @param adjustment cast=(GtkAdjustment *) */
	public static final native long /*int*/ _gtk_spin_button_new(long /*int*/ adjustment, double climb_rate, int digits);
	public static final long /*int*/ gtk_spin_button_new(long /*int*/ adjustment, double climb_rate, int digits) {
		lock.lock();
		try {
			return _gtk_spin_button_new(adjustment, climb_rate, digits);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param spin_button cast=(GtkSpinButton*)
	 * @param numeric cast=(gboolean)
	 **/
	public static final native void _gtk_spin_button_set_numeric(long /*int*/ spin_button, boolean numeric);
	public static final void gtk_spin_button_set_numeric(long /*int*/ spin_button, boolean numeric) {
		lock.lock();
		try {
			_gtk_spin_button_set_numeric(spin_button, numeric);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param spin_button cast=(GtkSpinButton*)
	 * @param adjustment cast=(GtkAdjustment *)
	 **/
	public static final native void _gtk_spin_button_configure(long /*int*/ spin_button, long /*int*/ adjustment, double climb_rate, int digits);
	public static final void gtk_spin_button_configure(long /*int*/ spin_button, long /*int*/ adjustment, double climb_rate, int digits) {
		lock.lock();
		try {
			_gtk_spin_button_configure(spin_button, adjustment, climb_rate, digits);
		} finally {
			lock.unlock();
		}
	}
	/** @param spin_button cast=(GtkSpinButton*) */
	public static final native long /*int*/ _gtk_spin_button_get_adjustment(long /*int*/ spin_button);
	public static final long /*int*/ gtk_spin_button_get_adjustment(long /*int*/ spin_button) {
		lock.lock();
		try {
			return _gtk_spin_button_get_adjustment(spin_button);
		} finally {
			lock.unlock();
		}
	}
	/** @param spin_button cast=(GtkSpinButton*) */
	public static final native int _gtk_spin_button_get_digits(long /*int*/ spin_button);
	public static final int gtk_spin_button_get_digits(long /*int*/ spin_button) {
		lock.lock();
		try {
			return _gtk_spin_button_get_digits(spin_button);
		} finally {
			lock.unlock();
		}
	}
	/** @param spin_button cast=(GtkSpinButton*) */
	public static final native void _gtk_spin_button_set_digits(long /*int*/ spin_button, int digits);
	public static final void gtk_spin_button_set_digits(long /*int*/ spin_button, int digits) {
		lock.lock();
		try {
			_gtk_spin_button_set_digits(spin_button, digits);
		} finally {
			lock.unlock();
		}
	}
	/** @param spin_button cast=(GtkSpinButton*) */
	public static final native void _gtk_spin_button_set_increments(long /*int*/ spin_button, double step, double page);
	public static final void gtk_spin_button_set_increments(long /*int*/ spin_button, double step, double page) {
		lock.lock();
		try {
			_gtk_spin_button_set_increments(spin_button, step, page);
		} finally {
			lock.unlock();
		}
	}
	/** @param spin_button cast=(GtkSpinButton*) */
	public static final native void _gtk_spin_button_set_range(long /*int*/ spin_button, double max, double min);
	public static final void gtk_spin_button_set_range(long /*int*/ spin_button, double max, double min) {
		lock.lock();
		try {
			_gtk_spin_button_set_range(spin_button, max, min);
		} finally {
			lock.unlock();
		}
	}
	/** @param spin_button cast=(GtkSpinButton*) */
	public static final native void _gtk_spin_button_set_value(long /*int*/ spin_button, double value);
	public static final void gtk_spin_button_set_value(long /*int*/ spin_button, double value) {
		lock.lock();
		try {
			_gtk_spin_button_set_value(spin_button, value);
		} finally {
			lock.unlock();
		}
	}
	/** @param spin_button cast=(GtkSpinButton*) */
	public static final native void _gtk_spin_button_set_wrap(long /*int*/ spin_button, boolean wrap);
	public static final void gtk_spin_button_set_wrap(long /*int*/ spin_button, boolean wrap) {
		lock.lock();
		try {
			_gtk_spin_button_set_wrap(spin_button, wrap);
		} finally {
			lock.unlock();
		}
	}
	/** @param spin_button cast=(GtkSpinButton*) */
	public static final native void _gtk_spin_button_update(long /*int*/ spin_button);
	public static final void gtk_spin_button_update(long /*int*/ spin_button) {
		lock.lock();
		try {
			_gtk_spin_button_update(spin_button);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param snapshot cast=(GtkSnapshot *)
	 * @param rect cast=(const graphene_rect_t *)
	 */
	public static final native long /*int*/ _gtk_snapshot_append_cairo(long /*int*/ snapshot, long /*int*/ rect);
	/** [GTK4 only, if-def'd in os.h] */
	public static final long /*int*/ gtk_snapshot_append_cairo(long /*int*/ snapshot, long /*int*/ rect) {
		lock.lock();
		try {
			 return _gtk_snapshot_append_cairo(snapshot, rect);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param handle cast=(GtkStatusIcon*)
	 * @param screen cast=(GdkScreen**)
	 * @param area cast=(GdkRectangle*)
	 * @param orientation cast=(GtkOrientation*)
	 */
	public static final native boolean _gtk_status_icon_get_geometry(long /*int*/ handle, long /*int*/ screen, GdkRectangle area, long /*int*/ orientation);
	/** [GTK3 only, if-def'd in os.h] */
	public static final boolean gtk_status_icon_get_geometry(long /*int*/ handle, long /*int*/ screen, GdkRectangle area, long /*int*/ orientation) {
		lock.lock();
		try {
			return _gtk_status_icon_get_geometry(handle, screen, area, orientation);
		} finally {
			lock.unlock();
		}
	}
	/** @param handle cast=(GtkStatusIcon*) */
	public static final native boolean _gtk_status_icon_get_visible(long /*int*/ handle);
	/** [GTK3 only, if-def'd in os.h] */
	public static final boolean gtk_status_icon_get_visible(long /*int*/ handle) {
		lock.lock();
		try {
			return _gtk_status_icon_get_visible(handle);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_status_icon_new();
	/** [GTK3 only, if-def'd in os.h] */
	public static final long /*int*/ gtk_status_icon_new() {
		lock.lock();
		try {
			return _gtk_status_icon_new();
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param handle cast=(GtkStatusIcon*)
	 * @param pixbuf cast=(GdkPixbuf*)
	 */
	public static final native void _gtk_status_icon_set_from_pixbuf(long /*int*/ handle, long /*int*/ pixbuf);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gtk_status_icon_set_from_pixbuf(long /*int*/ handle, long /*int*/ pixbuf) {
		lock.lock();
		try {
			_gtk_status_icon_set_from_pixbuf(handle, pixbuf);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param handle cast=(GtkStatusIcon*)
	 * @param visible cast=(gboolean)
	 */
	public static final native void _gtk_status_icon_set_visible(long /*int*/ handle, boolean visible);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gtk_status_icon_set_visible(long /*int*/ handle, boolean visible) {
		lock.lock();
		try {
			_gtk_status_icon_set_visible(handle, visible);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param handle cast=(GtkStatusIcon *)
	 * @param tip_text cast=(const gchar *)
	 */
	public static final native void _gtk_status_icon_set_tooltip_text(long /*int*/ handle, byte[] tip_text);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gtk_status_icon_set_tooltip_text(long /*int*/ handle, byte[] tip_text) {
		lock.lock();
		try {
			_gtk_status_icon_set_tooltip_text(handle, tip_text);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param context cast=(GtkStyleContext *)
	 * @param class_name cast=(const gchar *)
	 */
	public static final native void _gtk_style_context_add_class(long /*int*/ context, byte[] class_name);
	public static final void gtk_style_context_add_class(long /*int*/ context, byte[] class_name) {
		lock.lock();
		try {
			_gtk_style_context_add_class(context, class_name);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param context cast=(GtkStyleContext *)
	 * @param class_name cast=(const gchar *)
	 */
	public static final native void _gtk_style_context_remove_class(long /*int*/ context, byte[] class_name);
	public static final void gtk_style_context_remove_class(long /*int*/ context, byte[] class_name) {
		lock.lock();
		try {
			_gtk_style_context_remove_class(context, class_name);
		} finally {
			lock.unlock();
		}
	}
	/** @method flags=dynamic */
	public static final native void _gtk_style_context_get_background_color(long /*int*/ context, int state, GdkRGBA color);
	/** [GTK3; 3.16 deprecated, replaced] */
	public static final void gtk_style_context_get_background_color(long /*int*/ context, int state, GdkRGBA color) {
		lock.lock();
		try {
			_gtk_style_context_get_background_color(context, state, color);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param context cast=(GtkStyleContext *)
	 * @param state cast=(GtkStateFlags)
	 * @param color cast=(GdkRGBA *)
	 */
	public static final native void _gtk_style_context_get_color(long /*int*/ context, int state, GdkRGBA color);
	/** [GTK3 only] */
	public static final void gtk_style_context_get_color(long /*int*/ context, int state, GdkRGBA color) {
		lock.lock();
		try {
			_gtk_style_context_get_color(context, state, color);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param context cast=(GtkStyleContext *)
	 * @param color cast=(GdkRGBA *)
	 */
	public static final native void _gtk_style_context_get_color(long /*int*/ context, GdkRGBA color);
	/** [GTK4 only] */
	public static final void gtk_style_context_get_color(long /*int*/ context, GdkRGBA color) {
		lock.lock();
		try {
			_gtk_style_context_get_color(context, color);
		} finally {
			lock.unlock();
		}
	}
	/** @method flags=dynamic */
	public static final native long /*int*/ _gtk_style_context_get_font(long /*int*/ context, int state);
	/** [GTK3; 3.8 deprecated, replaced] */
	public static final long /*int*/ gtk_style_context_get_font(long /*int*/ context, int state) {
		lock.lock();
		try {
			return _gtk_style_context_get_font(context, state);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param context cast=(GtkStyleContext *)
	 * @param state cast=(GtkStateFlags)
	 * @param padding cast=(GtkBorder *),flags=no_in
	 */
	public static final native void _gtk_style_context_get_padding(long /*int*/ context, int state, GtkBorder padding);
	/** [GTK3 only] */
	public static final void gtk_style_context_get_padding(long /*int*/ context, int state, GtkBorder padding) {
		lock.lock();
		try {
			_gtk_style_context_get_padding(context, state, padding);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param context cast=(GtkStyleContext *)
	 * @param padding cast=(GtkBorder *),flags=no_in
	 */
	public static final native void _gtk_style_context_get_padding(long /*int*/ context, GtkBorder padding);
	/** [GTK4 only] */
	public static final void gtk_style_context_get_padding(long /*int*/ context, GtkBorder padding) {
		lock.lock();
		try {
			_gtk_style_context_get_padding(context, padding);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param property cast=(const gchar *),flags=no_out
	 * @param terminator cast=(const gchar *),flags=sentinel
	 */
	public static final native void _gtk_style_context_get(long /*int*/ context, int state, byte [] property, long /*int*/ [] value, long /*int*/ terminator);
	public static final void gtk_style_context_get(long /*int*/ context, int state, byte [] property, long /*int*/ [] value, long /*int*/ terminator) {
		lock.lock();
		try {
			_gtk_style_context_get(context, state, property, value, terminator);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param property cast=(const gchar *),flags=no_out
	 * @param terminator cast=(const gchar *),flags=sentinel
	 */
	public static final native void _gtk_style_context_get(long /*int*/ context, byte [] property, long /*int*/ [] value, long /*int*/ terminator);
	public static final void gtk_style_context_get(long /*int*/ context, byte [] property, long /*int*/ [] value, long /*int*/ terminator) {
		lock.lock();
		try {
			_gtk_style_context_get(context, property, value, terminator);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param context cast=(GtkStyleContext *)
	 * @param state cast=(GtkStateFlags)
	 * @param padding cast=(GtkBorder *),flags=no_in
	 */
	public static final native void _gtk_style_context_get_border(long /*int*/ context, int state, GtkBorder padding);
	/** [GTK3 only] */
	public static final void gtk_style_context_get_border(long /*int*/ context, int state, GtkBorder padding) {
		lock.lock();
		try {
			_gtk_style_context_get_border(context, state, padding);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param context cast=(GtkStyleContext *)
	 * @param padding cast=(GtkBorder *),flags=no_in
	 */
	public static final native void _gtk_style_context_get_border(long /*int*/ context, GtkBorder padding);
	/** [GTK4 only] */
	public static final void gtk_style_context_get_border(long /*int*/ context, GtkBorder padding) {
		lock.lock();
		try {
			_gtk_style_context_get_border(context, padding);
		} finally {
			lock.unlock();
		}
	}
	/** @method flags=dynamic */
	public static final native void _gtk_style_context_invalidate(long /*int*/ context);
	/** [GTK3; 3.12 deprecated] */
	public static final void gtk_style_context_invalidate(long /*int*/ context) {
		lock.lock();
		try {
			_gtk_style_context_invalidate(context);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param self cast=(GtkStyleContext *)
	 */
	public static final native void _gtk_style_context_save(long /*int*/ self);
	public static final void gtk_style_context_save(long /*int*/ self) {
		lock.lock();
		try {
			_gtk_style_context_save(self);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param context cast=(GtkStyleContext *)
	 */
	public static final native void _gtk_style_context_restore(long /*int*/ context);
	public static final void gtk_style_context_restore(long /*int*/ context) {
		lock.lock();
		try {
			_gtk_style_context_restore(context);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param self cast=(GtkWidget *)
	 */
	public static final native int _gtk_widget_get_state_flags(long /*int*/ self);
	public static final int gtk_widget_get_state_flags(long /*int*/ self) {
		lock.lock();
		try {
			return _gtk_widget_get_state_flags(self);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param context cast=(GtkStyleContext *)
	 * @param flags cast=(GtkStateFlags)
	 */
	public static final native void _gtk_style_context_set_state(long /*int*/ context, long /*int*/ flags);
	public static final void gtk_style_context_set_state(long /*int*/ context, long /*int*/ flags) {
		lock.lock();
		try {
			_gtk_style_context_set_state(context,flags);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param targets cast=(const GtkTargetEntry *)
	 * @param ntargets cast=(guint)
	 */
	public static final native long /*int*/ _gtk_target_list_new(long /*int*/ targets, int ntargets);
	/** [GTK3 only, if-def'd in os.h] */
	public static final long /*int*/ gtk_target_list_new(long /*int*/ targets, int ntargets) {
		lock.lock();
		try {
			return _gtk_target_list_new(targets, ntargets);
		} finally {
			lock.unlock();
		}
	}
	/** @param list cast=(GtkTargetList *) */
	public static final native void _gtk_target_list_unref(long /*int*/ list);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gtk_target_list_unref(long /*int*/ list) {
		lock.lock();
		try {
			_gtk_target_list_unref(list);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param buffer cast=(GtkTextBuffer *)
	 */
	public static final native void _gtk_text_buffer_copy_clipboard(long /*int*/ buffer, long /*int*/ clipboard);
	/** [GTK3/GTK4, GTK3 uses GtkClipboard but GTK4 uses GdkClipboard -- method signature otherwise identical] */
	public static final void gtk_text_buffer_copy_clipboard(long /*int*/ buffer, long /*int*/ clipboard) {
		lock.lock();
		try {
			_gtk_text_buffer_copy_clipboard(buffer, clipboard);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param buffer cast=(GtkTextBuffer *)
	 * @param mark_name cast=(const gchar *)
	 * @param where cast=(GtkTextIter *)
	 * @param left_gravity cast=(gboolean)
	 */
	public static final native long /*int*/ _gtk_text_buffer_create_mark(long /*int*/ buffer, byte [] mark_name, byte [] where, boolean left_gravity);
	public static final long /*int*/ gtk_text_buffer_create_mark(long /*int*/ buffer, byte [] mark_name, byte [] where, boolean left_gravity) {
		lock.lock();
		try {
			return _gtk_text_buffer_create_mark(buffer, mark_name, where, left_gravity);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param buffer cast=(GtkTextBuffer *)
	 * @param default_editable cast=(gboolean)
	 */
	public static final native void _gtk_text_buffer_cut_clipboard(long /*int*/ buffer, long /*int*/ clipboard, boolean default_editable);
	/** [GTK3/GTK4, GTK3 uses GtkClipboard but GTK4 uses GdkClipboard -- method signature otherwise identical] */
	public static final void gtk_text_buffer_cut_clipboard(long /*int*/ buffer, long /*int*/ clipboard, boolean default_editable) {
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
	public static final native void _gtk_text_buffer_delete(long /*int*/ buffer, byte[] start, byte[] end);
	public static final void gtk_text_buffer_delete(long /*int*/ buffer, byte[] start, byte[] end) {
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
	public static final native void _gtk_text_buffer_get_bounds(long /*int*/ buffer, byte[] start, byte[] end);
	public static final void gtk_text_buffer_get_bounds(long /*int*/ buffer, byte[] start, byte[] end) {
		lock.lock();
		try {
			_gtk_text_buffer_get_bounds(buffer, start, end);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param buffer cast=(GtkTextBuffer *)
	 * @param iter cast=(GtkTextIter *)
	 */
	public static final native void _gtk_text_buffer_get_end_iter(long /*int*/ buffer, byte[] iter);
	public static final void gtk_text_buffer_get_end_iter(long /*int*/ buffer, byte[] iter) {
		lock.lock();
		try {
			_gtk_text_buffer_get_end_iter(buffer, iter);
		} finally {
			lock.unlock();
		}
	}
	/** @param buffer cast=(GtkTextBuffer *) */
	public static final native long /*int*/ _gtk_text_buffer_get_insert(long /*int*/ buffer);
	public static final long /*int*/ gtk_text_buffer_get_insert(long /*int*/ buffer) {
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
	public static final native void _gtk_text_buffer_get_iter_at_line(long /*int*/ buffer, byte[] iter, int line_number);
	public static final void gtk_text_buffer_get_iter_at_line(long /*int*/ buffer, byte[] iter, int line_number) {
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
	public static final native void _gtk_text_buffer_get_iter_at_mark(long /*int*/ buffer, byte[] iter, long /*int*/ mark);
	public static final void gtk_text_buffer_get_iter_at_mark(long /*int*/ buffer, byte[] iter, long /*int*/ mark) {
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
	public static final native void _gtk_text_buffer_get_iter_at_offset(long /*int*/ buffer, byte[] iter, int char_offset);
	public static final void gtk_text_buffer_get_iter_at_offset(long /*int*/ buffer, byte[] iter, int char_offset) {
		lock.lock();
		try {
			_gtk_text_buffer_get_iter_at_offset(buffer, iter, char_offset);
		} finally {
			lock.unlock();
		}
	}
	/** @param buffer cast=(GtkTextBuffer *) */
	public static final native int _gtk_text_buffer_get_line_count(long /*int*/ buffer);
	public static final int gtk_text_buffer_get_line_count(long /*int*/ buffer) {
		lock.lock();
		try {
			return _gtk_text_buffer_get_line_count(buffer);
		} finally {
			lock.unlock();
		}
	}
	/** @param buffer cast=(GtkTextBuffer *) */
	public static final native long /*int*/ _gtk_text_buffer_get_selection_bound(long /*int*/ buffer);
	public static final long /*int*/ gtk_text_buffer_get_selection_bound(long /*int*/ buffer) {
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
	public static final native boolean _gtk_text_buffer_get_selection_bounds(long /*int*/ buffer, byte[] start, byte[] end);
	public static final boolean gtk_text_buffer_get_selection_bounds(long /*int*/ buffer, byte[] start, byte[] end) {
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
	public static final native long /*int*/ _gtk_text_buffer_get_text(long /*int*/ buffer, byte[] start, byte[] end, boolean include_hidden_chars);
	public static final long /*int*/ gtk_text_buffer_get_text(long /*int*/ buffer, byte[] start, byte[] end, boolean include_hidden_chars) {
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
	public static final native void _gtk_text_buffer_insert(long /*int*/ buffer, byte[] iter, byte[] text, int len);
	public static final void gtk_text_buffer_insert(long /*int*/ buffer, byte[] iter, byte[] text, int len) {
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
	public static final native void _gtk_text_buffer_insert(long /*int*/ buffer, long /*int*/ iter, byte[] text, int len);
	public static final void gtk_text_buffer_insert(long /*int*/ buffer, long /*int*/ iter, byte[] text, int len) {
		lock.lock();
		try {
			_gtk_text_buffer_insert(buffer, iter, text, len);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param buffer cast=(GtkTextBuffer *)
	 * @param ins cast=(const GtkTextIter *)
	 * @param bound cast=(const GtkTextIter *)
	 */
	public static final native void _gtk_text_buffer_select_range (long /*int*/ buffer, byte[] ins, byte[] bound);
	public static final void gtk_text_buffer_select_range (long /*int*/ buffer, byte[] ins, byte[] bound) {
		lock.lock();
		try {
			_gtk_text_buffer_select_range(buffer, ins, bound);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param buffer cast=(GtkTextBuffer *)
	 * @param override_location cast=(GtkTextIter *)
	 * @param default_editable cast=(gboolean)
	 */
	public static final native void _gtk_text_buffer_paste_clipboard(long /*int*/ buffer, long /*int*/ clipboard, byte[] override_location, boolean default_editable);
	/** [GTK3/GTK4, GTK3 uses GtkClipboard but GTK4 uses GdkClipboard -- method signature otherwise identical] */
	public static final void gtk_text_buffer_paste_clipboard(long /*int*/ buffer, long /*int*/ clipboard, byte[] override_location, boolean default_editable) {
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
	public static final native void _gtk_text_buffer_place_cursor(long /*int*/ buffer, byte[] where);
	public static final void gtk_text_buffer_place_cursor(long /*int*/ buffer, byte[] where) {
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
	public static final native void _gtk_text_buffer_set_text(long /*int*/ buffer, byte[] text, int len);
	public static final void gtk_text_buffer_set_text(long /*int*/ buffer, byte[] text, int len) {
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
	 * @method flags=dynamic
	 * @param text_view cast=(GtkTextView *)
	 * @param win cast=(GtkTextWindowType)
	 * @param buffer_x cast=(gint)
	 * @param buffer_y cast=(gint)
	 * @param window_x cast=(gint *)
	 * @param window_y cast=(gint *)
	 */
	public static final native void _gtk_text_view_buffer_to_window_coords(long /*int*/ text_view, int win, int buffer_x, int buffer_y, int[] window_x, int[] window_y);
	/** [GTK3 only] */
	public static final void gtk_text_view_buffer_to_window_coords(long /*int*/ text_view, int win, int buffer_x, int buffer_y, int[] window_x, int[] window_y) {
		lock.lock();
		try {
			_gtk_text_view_buffer_to_window_coords(text_view, win, buffer_x, buffer_y, window_x, window_y);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param text_view cast=(GtkTextView *)
	 * @param win cast=(GtkTextWindowType)
	 * @param buffer_x cast=(gint)
	 * @param buffer_y cast=(gint)
	 * @param window_x cast=(gint *)
	 * @param window_y cast=(gint *)
	 */
	public static final native void _gtk_text_view_buffer_to_surface_coords(long /*int*/ text_view, int win, int buffer_x, int buffer_y, int[] window_x, int[] window_y);
	/** [GTK4 only] */
	public static final void gtk_text_view_buffer_to_surface_coords(long /*int*/ text_view, int win, int buffer_x, int buffer_y, int[] window_x, int[] window_y) {
		lock.lock();
		try {
			_gtk_text_view_buffer_to_surface_coords(text_view, win, buffer_x, buffer_y, window_x, window_y);
		} finally {
			lock.unlock();
		}
	}
	/** @param text_view cast=(GtkTextView *) */
	public static final native long /*int*/ _gtk_text_view_get_buffer(long /*int*/ text_view);
	public static final long /*int*/ gtk_text_view_get_buffer(long /*int*/ text_view) {
		lock.lock();
		try {
			return _gtk_text_view_get_buffer(text_view);
		} finally {
			lock.unlock();
		}
	}
	/** @param text_view cast=(GtkTextView *) */
	public static final native boolean _gtk_text_view_get_editable(long /*int*/ text_view);
	public static final boolean gtk_text_view_get_editable(long /*int*/ text_view) {
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
	public static final native void _gtk_text_view_get_iter_at_location(long /*int*/ text_view, byte[] iter, int x, int y);
	public static final void gtk_text_view_get_iter_at_location(long /*int*/ text_view, byte[] iter, int x, int y) {
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
	public static final native void _gtk_text_view_get_iter_location(long /*int*/ text_view, byte[] iter, GdkRectangle location);
	public static final void gtk_text_view_get_iter_location(long /*int*/ text_view, byte[] iter, GdkRectangle location) {
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
	public static final native void _gtk_text_view_get_line_at_y(long /*int*/ text_view, byte[] target_iter, int y, int[] line_top);
	public static final void gtk_text_view_get_line_at_y(long /*int*/ text_view, byte[] target_iter, int y, int[] line_top) {
		lock.lock();
		try {
			_gtk_text_view_get_line_at_y(text_view, target_iter, y, line_top);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param text_view cast=(GtkTextView *)
	 * @param target_iter cast=(GtkTextIter *)
	 * @param y cast=(gint *)
	 * @param height cast=(gint *)
	 */
	public static final native void _gtk_text_view_get_line_yrange(long /*int*/ text_view, byte[] target_iter, int[] y, int[] height);
	public static final void gtk_text_view_get_line_yrange(long /*int*/ text_view, byte[] target_iter, int[] y, int[] height) {
		lock.lock();
		try {
			_gtk_text_view_get_line_yrange(text_view, target_iter, y, height);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param text_view cast=(GtkTextView *)
	 * @param visible_rect cast=(GdkRectangle *),flags=no_in
	 */
	public static final native void _gtk_text_view_get_visible_rect(long /*int*/ text_view, GdkRectangle visible_rect);
	public static final void gtk_text_view_get_visible_rect(long /*int*/ text_view, GdkRectangle visible_rect) {
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
	public static final native long /*int*/ _gtk_text_view_get_window(long /*int*/ text_view, int win);
	/** [GTK3 only, if-def'd in os.h] */
	public static final long /*int*/ gtk_text_view_get_window(long /*int*/ text_view, int win) {
		lock.lock();
		try {
			return _gtk_text_view_get_window(text_view, win);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_text_view_new();
	public static final long /*int*/ gtk_text_view_new() {
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
	 * @param within_margin cast=(gdouble)
	 * @param use_align cast=(gboolean)
	 * @param xalign cast=(gdouble)
	 * @param yalign cast=(gdouble)
	 */
	public static final native void _gtk_text_view_scroll_to_mark(long /*int*/ text_view, long /*int*/ mark, double within_margin, boolean use_align, double xalign, double yalign);
	public static final void gtk_text_view_scroll_to_mark(long /*int*/ text_view, long /*int*/ mark, double within_margin, boolean use_align, double xalign, double yalign) {
		lock.lock();
		try {
			_gtk_text_view_scroll_to_mark(text_view, mark, within_margin, use_align, xalign, yalign);
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
	public static final native boolean _gtk_text_view_scroll_to_iter(long /*int*/ text_view, byte[] iter, double within_margin, boolean use_align, double xalign, double yalign);
	public static final boolean gtk_text_view_scroll_to_iter(long /*int*/ text_view, byte[] iter, double within_margin, boolean use_align, double xalign, double yalign) {
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
	public static final native void _gtk_text_view_set_editable(long /*int*/ text_view, boolean setting);
	public static final void gtk_text_view_set_editable(long /*int*/ text_view, boolean setting) {
		lock.lock();
		try {
			_gtk_text_view_set_editable(text_view, setting);
		} finally {
			lock.unlock();
		}
	}
	/** @param text_view cast=(GtkTextView *) */
	public static final native void _gtk_text_view_set_justification(long /*int*/ text_view, int justification);
	public static final void gtk_text_view_set_justification(long /*int*/ text_view, int justification) {
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
	public static final native void _gtk_text_view_set_tabs(long /*int*/ text_view, long /*int*/ tabs);
	public static final void gtk_text_view_set_tabs(long /*int*/ text_view, long /*int*/ tabs) {
		lock.lock();
		try {
			_gtk_text_view_set_tabs(text_view, tabs);
		} finally {
			lock.unlock();
		}
	}
	/** @param text_view cast=(GtkTextView *) */
	public static final native void _gtk_text_view_set_wrap_mode(long /*int*/ text_view, int wrap_mode);
	public static final void gtk_text_view_set_wrap_mode(long /*int*/ text_view, int wrap_mode) {
		lock.lock();
		try {
			_gtk_text_view_set_wrap_mode(text_view, wrap_mode);
		} finally {
			lock.unlock();
		}
	}
	/** @param toggle_button cast=(GtkToggleButton *) */
	public static final native boolean _gtk_toggle_button_get_active(long /*int*/ toggle_button);
	public static final boolean gtk_toggle_button_get_active(long /*int*/ toggle_button) {
		lock.lock();
		try {
			return _gtk_toggle_button_get_active(toggle_button);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_toggle_button_new();
	public static final long /*int*/ gtk_toggle_button_new() {
		lock.lock();
		try {
			return _gtk_toggle_button_new();
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param toggle_button cast=(GtkToggleButton *)
	 * @param is_active cast=(gboolean)
	 */
	public static final native void _gtk_toggle_button_set_active(long /*int*/ toggle_button, boolean is_active);
	public static final void gtk_toggle_button_set_active(long /*int*/ toggle_button, boolean is_active) {
		lock.lock();
		try {
			_gtk_toggle_button_set_active(toggle_button, is_active);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param toggle_button cast=(GtkToggleButton *)
	 * @param setting cast=(gboolean)
	 */
	public static final native void _gtk_toggle_button_set_inconsistent(long /*int*/ toggle_button, boolean setting);
	/** [GTK3 only] */
	public static final void gtk_toggle_button_set_inconsistent(long /*int*/ toggle_button, boolean setting) {
		lock.lock();
		try {
			_gtk_toggle_button_set_inconsistent(toggle_button, setting);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param toggle_button cast=(GtkToggleButton *)
	 * @param draw_indicator cast=(gboolean)
	 */
	public static final native void _gtk_toggle_button_set_mode(long /*int*/ toggle_button, boolean draw_indicator);
	/** [GTK3 only] */
	public static final void gtk_toggle_button_set_mode(long /*int*/ toggle_button, boolean draw_indicator) {
		lock.lock();
		try {
			_gtk_toggle_button_set_mode(toggle_button, draw_indicator);
		} finally {
			lock.unlock();
		}
	}
	/** @param button cast=(GtkToggleToolButton *) */
	public static final native boolean _gtk_toggle_tool_button_get_active(long /*int*/ button);
	public static final boolean gtk_toggle_tool_button_get_active(long /*int*/ button) {
		lock.lock();
		try {
			return _gtk_toggle_tool_button_get_active(button);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_toggle_tool_button_new();
	public static final long /*int*/ gtk_toggle_tool_button_new() {
		lock.lock();
		try {
			return _gtk_toggle_tool_button_new();
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param item cast=(GtkToggleToolButton *)
	 * @param selected cast=(gboolean)
	 */
	public static final native void _gtk_toggle_tool_button_set_active(long /*int*/ item, boolean selected);
	public static final void gtk_toggle_tool_button_set_active(long /*int*/ item, boolean selected) {
		lock.lock();
		try {
			_gtk_toggle_tool_button_set_active(item, selected);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param icon_widget cast=(GtkWidget *)
	 * @param label cast=(const gchar *)
	 */
	public static final native long /*int*/ _gtk_tool_button_new(long /*int*/ icon_widget, byte[] label);
	public static final long /*int*/ gtk_tool_button_new(long /*int*/ icon_widget, byte[] label) {
		lock.lock();
		try {
			return _gtk_tool_button_new(icon_widget, label);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param button cast=(GtkToolButton *)
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native void _gtk_tool_button_set_icon_widget(long /*int*/ button, long /*int*/ widget);
	public static final void gtk_tool_button_set_icon_widget(long /*int*/ button, long /*int*/ widget) {
		lock.lock();
		try {
			_gtk_tool_button_set_icon_widget(button, widget);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param button cast=(GtkToolButton *)
	 * @param label cast=(const gchar *)
	 */
	public static final native void _gtk_tool_button_set_label(long /*int*/ button,  byte[] label);
	public static final void gtk_tool_button_set_label(long /*int*/ button,  byte[] label) {
		lock.lock();
		try {
			_gtk_tool_button_set_label(button, label);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param button cast=(GtkToolButton *)
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native void _gtk_tool_button_set_label_widget(long /*int*/ button,  long /*int*/ widget);
	public static final void gtk_tool_button_set_label_widget(long /*int*/ button,  long /*int*/ widget) {
		lock.lock();
		try {
			_gtk_tool_button_set_label_widget(button, widget);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param item cast=(GtkToolButton *)
	 * @param underline cast=(gboolean)
	 */
	public static final native void _gtk_tool_button_set_use_underline(long /*int*/ item, boolean underline);
	public static final void gtk_tool_button_set_use_underline(long /*int*/ item, boolean underline) {
		lock.lock();
		try {
			_gtk_tool_button_set_use_underline(item, underline);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param item cast=(GtkToolItem *)
	 * @param menu_id cast=(const gchar *)
	 */
	public static final native long /*int*/ _gtk_tool_item_get_proxy_menu_item(long /*int*/ item, byte[] menu_id);
	public static final long /*int*/ gtk_tool_item_get_proxy_menu_item(long /*int*/ item, byte[] menu_id) {
		lock.lock();
		try {
			return _gtk_tool_item_get_proxy_menu_item(item, menu_id);
		} finally {
			lock.unlock();
		}
	}
	/** @param item cast=(GtkToolItem *) */
	public static final native long /*int*/ _gtk_tool_item_retrieve_proxy_menu_item(long /*int*/ item);
	public static final long /*int*/ gtk_tool_item_retrieve_proxy_menu_item(long /*int*/ item) {
		lock.lock();
		try {
			return _gtk_tool_item_retrieve_proxy_menu_item(item);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param item cast=(GtkToolItem *)
	 * @param important cast=(gboolean)
	 */
	public static final native void _gtk_tool_item_set_is_important(long /*int*/ item, boolean important);
	public static final void gtk_tool_item_set_is_important(long /*int*/ item, boolean important) {
		lock.lock();
		try {
			_gtk_tool_item_set_is_important(item, important);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param item cast=(GtkToolItem *)
	 * @param menu_id cast=(const gchar *)
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native void _gtk_tool_item_set_proxy_menu_item(long /*int*/ item, byte[] menu_id, long /*int*/ widget);
	public static final void gtk_tool_item_set_proxy_menu_item(long /*int*/ item, byte[] menu_id, long /*int*/ widget) {
		lock.lock();
		try {
			_gtk_tool_item_set_proxy_menu_item(item, menu_id, widget);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param toolbar cast=(GtkToolbar *)
	 * @param item cast=(GtkToolItem *)
	 */
	public static final native void _gtk_toolbar_insert(long /*int*/ toolbar, long /*int*/ item, int pos);
	public static final void gtk_toolbar_insert(long /*int*/ toolbar, long /*int*/ item, int pos) {
		lock.lock();
		try {
			_gtk_toolbar_insert(toolbar, item, pos);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_toolbar_new();
	public static final long /*int*/ gtk_toolbar_new() {
		lock.lock();
		try {
			return _gtk_toolbar_new();
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param toolbar cast=(GtkToolbar *)
	 * @param show_arrow cast=(gboolean)
	 */
	public static final native void _gtk_toolbar_set_show_arrow(long /*int*/ toolbar, boolean show_arrow);
	public static final void gtk_toolbar_set_show_arrow(long /*int*/ toolbar, boolean show_arrow) {
		lock.lock();
		try {
			_gtk_toolbar_set_show_arrow(toolbar, show_arrow);
		} finally {
			lock.unlock();
		}
	}
	/** @param toolbar cast=(GtkToolbar *)
	 * @param style cast=(GtkToolbarStyle)
	 */
	public static final native void _gtk_toolbar_set_style(long /*int*/ toolbar, int style);
	public static final void gtk_toolbar_set_style(long /*int*/ toolbar, int style) {
		lock.lock();
		try {
			_gtk_toolbar_set_style(toolbar, style);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param toolbar cast=(GtkToolbar *)
	 */
	public static final native void _gtk_toolbar_set_icon_size(long /*int*/ toolbar, int size);
	/** [GTK3 only] */
	public static final void gtk_toolbar_set_icon_size(long /*int*/ toolbar, int size) {
		lock.lock();
		try {
			_gtk_toolbar_set_icon_size(toolbar, size);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param tree_model cast=(GtkTreeModel *)
	 * @param iter cast=(GtkTreeIter *)
	 */
	public static final native void _gtk_tree_model_get(long /*int*/ tree_model, long /*int*/ iter, int column, long[] value, int terminator);
	public static final void gtk_tree_model_get(long /*int*/ tree_model, long /*int*/ iter, int column, long[] value, int terminator) {
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
	public static final native void _gtk_tree_model_get(long /*int*/ tree_model, long /*int*/ iter, int column, int[] value, int terminator);
	public static final void gtk_tree_model_get(long /*int*/ tree_model, long /*int*/ iter, int column, int[] value, int terminator) {
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
	public static final native boolean _gtk_tree_model_get_iter(long /*int*/ tree_model, long /*int*/ iter, long /*int*/ path);
	public static final boolean gtk_tree_model_get_iter(long /*int*/ tree_model, long /*int*/ iter, long /*int*/ path) {
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
	public static final native boolean _gtk_tree_model_get_iter_first(long /*int*/ tree_model, long /*int*/ iter);
	public static final boolean gtk_tree_model_get_iter_first(long /*int*/ tree_model, long /*int*/ iter) {
		lock.lock();
		try {
			return _gtk_tree_model_get_iter_first(tree_model, iter);
		} finally {
			lock.unlock();
		}
	}
	/** @param tree_model cast=(GtkTreeModel *) */
	public static final native int _gtk_tree_model_get_n_columns(long /*int*/ tree_model);
	public static final int gtk_tree_model_get_n_columns(long /*int*/ tree_model) {
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
	public static final native long /*int*/ _gtk_tree_model_get_path(long /*int*/ tree_model, long /*int*/ iter);
	public static final long /*int*/ gtk_tree_model_get_path(long /*int*/ tree_model, long /*int*/ iter) {
		lock.lock();
		try {
			return _gtk_tree_model_get_path(tree_model, iter);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_tree_model_get_type();
	public static final long /*int*/ gtk_tree_model_get_type() {
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
	public static final native boolean _gtk_tree_model_iter_children(long /*int*/ model, long /*int*/ iter, long /*int*/ parent);
	public static final boolean gtk_tree_model_iter_children(long /*int*/ model, long /*int*/ iter, long /*int*/ parent) {
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
	public static final native int _gtk_tree_model_iter_n_children(long /*int*/ model, long /*int*/ iter);
	public static final int gtk_tree_model_iter_n_children(long /*int*/ model, long /*int*/ iter) {
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
	public static final native boolean _gtk_tree_model_iter_next(long /*int*/ model, long /*int*/ iter);
	public static final boolean gtk_tree_model_iter_next(long /*int*/ model, long /*int*/ iter) {
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
	public static final native boolean _gtk_tree_model_iter_nth_child(long /*int*/ tree_model, long /*int*/ iter, long /*int*/ parent, int n);
	public static final boolean gtk_tree_model_iter_nth_child(long /*int*/ tree_model, long /*int*/ iter, long /*int*/ parent, int n) {
		lock.lock();
		try {
			return _gtk_tree_model_iter_nth_child(tree_model, iter, parent, n);
		} finally {
			lock.unlock();
		}
	}
	/** @param path cast=(GtkTreePath *) */
	public static final native void _gtk_tree_path_append_index(long /*int*/ path, int index);
	public static final void gtk_tree_path_append_index(long /*int*/ path, int index) {
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
	public static final native long /*int*/ _gtk_tree_path_compare(long /*int*/ a, long /*int*/ b);
	public static final long /*int*/ gtk_tree_path_compare(long /*int*/ a, long /*int*/ b) {
		lock.lock();
		try {
			 return _gtk_tree_path_compare(a, b);
		} finally {
			lock.unlock();
		}
	}
	/** @param path cast=(GtkTreePath *) */
	public static final native void _gtk_tree_path_free(long /*int*/ path);
	public static final void gtk_tree_path_free(long /*int*/ path) {
		lock.lock();
		try {
			_gtk_tree_path_free(path);
		} finally {
			lock.unlock();
		}
	}
	/** @param path cast=(GtkTreePath *) */
	public static final native int _gtk_tree_path_get_depth(long /*int*/ path);
	public static final int gtk_tree_path_get_depth(long /*int*/ path) {
		lock.lock();
		try {
			return _gtk_tree_path_get_depth(path);
		} finally {
			lock.unlock();
		}
	}
	/** @param path cast=(GtkTreePath *) */
	public static final native long /*int*/ _gtk_tree_path_get_indices(long /*int*/ path);
	public static final long /*int*/ gtk_tree_path_get_indices(long /*int*/ path) {
		lock.lock();
		try {
			return _gtk_tree_path_get_indices(path);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_tree_path_new();
	public static final long /*int*/ gtk_tree_path_new() {
		lock.lock();
		try {
			return _gtk_tree_path_new();
		} finally {
			lock.unlock();
		}
	}
	/** @param path cast=(const gchar *) */
	public static final native long /*int*/ _gtk_tree_path_new_from_string(byte[] path);
	public static final long /*int*/ gtk_tree_path_new_from_string(byte[] path) {
		lock.lock();
		try {
			return _gtk_tree_path_new_from_string(path);
		} finally {
			lock.unlock();
		}
	}
	/** @param path cast=(const gchar *) */
	public static final native long /*int*/ _gtk_tree_path_new_from_string(long /*int*/ path);
	public static final long /*int*/ gtk_tree_path_new_from_string(long /*int*/ path) {
		lock.lock();
		try {
			return _gtk_tree_path_new_from_string(path);
		} finally {
			lock.unlock();
		}
	}
	/** @param path cast=(GtkTreePath *) */
	public static final native void _gtk_tree_path_next(long /*int*/ path);
	public static final void gtk_tree_path_next(long /*int*/ path) {
		lock.lock();
		try {
			_gtk_tree_path_next(path);
		} finally {
			lock.unlock();
		}
	}
	/** @param path cast=(GtkTreePath *) */
	public static final native boolean _gtk_tree_path_prev(long /*int*/ path);
	public static final boolean gtk_tree_path_prev(long /*int*/ path) {
		lock.lock();
		try {
			return _gtk_tree_path_prev(path);
		} finally {
			lock.unlock();
		}
	}
	/** @param path cast=(GtkTreePath *) */
	public static final native boolean _gtk_tree_path_up(long /*int*/ path);
	public static final boolean gtk_tree_path_up(long /*int*/ path) {
		lock.lock();
		try {
			return _gtk_tree_path_up(path);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param selection cast=(GtkTreeSelection *)
	 */
	public static final native int _gtk_tree_selection_count_selected_rows(long /*int*/ selection);
	public static final int gtk_tree_selection_count_selected_rows(long /*int*/ selection) {
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
	 */
	public static final native long /*int*/ _gtk_tree_selection_get_selected_rows(long /*int*/ selection, long /*int*/[] model);
	public static final long /*int*/ gtk_tree_selection_get_selected_rows(long /*int*/ selection, long /*int*/[] model) {
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
	public static final native boolean _gtk_tree_selection_path_is_selected(long /*int*/ selection, long /*int*/ path);
	public static final boolean gtk_tree_selection_path_is_selected(long /*int*/ selection, long /*int*/ path) {
		lock.lock();
		try {
			return _gtk_tree_selection_path_is_selected(selection, path);
		} finally {
			lock.unlock();
		}
	}
	/** @param selection cast=(GtkTreeSelection *) */
	public static final native void _gtk_tree_selection_select_all(long /*int*/ selection);
	public static final void gtk_tree_selection_select_all(long /*int*/ selection) {
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
	public static final native void _gtk_tree_selection_select_iter(long /*int*/ selection, long /*int*/ iter);
	public static final void gtk_tree_selection_select_iter(long /*int*/ selection, long /*int*/ iter) {
		lock.lock();
		try {
			_gtk_tree_selection_select_iter(selection, iter);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param selection cast=(GtkTreeSelection *)
	 */
	public static final native long /*int*/ _gtk_tree_selection_get_select_function(long /*int*/ selection);
	public static final long /*int*/ gtk_tree_selection_get_select_function(long /*int*/ selection) {
		lock.lock();
		try {
			return _gtk_tree_selection_get_select_function(selection);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param selection cast=(GtkTreeSelection *)
	 * @param func cast=(GtkTreeSelectionFunc)
	 * @param data cast=(gpointer)
	 * @param destroy cast=(GDestroyNotify)
	 */
	public static final native void _gtk_tree_selection_set_select_function(long /*int*/ selection, long /*int*/ func, long /*int*/ data, long /*int*/ destroy);
	public static final void gtk_tree_selection_set_select_function(long /*int*/ selection, long /*int*/ func, long /*int*/ data, long /*int*/ destroy) {
		lock.lock();
		try {
			_gtk_tree_selection_set_select_function(selection, func, data, destroy);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param selection cast=(GtkTreeSelection *)
	 * @param path cast=(GtkTreePath *)
	 */
	public static final native void _gtk_tree_selection_select_path(long /*int*/ selection, long /*int*/ path);
	public static final  void gtk_tree_selection_select_path(long /*int*/ selection, long /*int*/ path) {
		lock.lock();
		try {
			_gtk_tree_selection_select_path(selection, path);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param selection cast=(GtkTreeSelection *)
	 * @param mode cast=(GtkSelectionMode)
	 */
	public static final native void _gtk_tree_selection_set_mode(long /*int*/ selection, int mode);
	public static final void gtk_tree_selection_set_mode(long /*int*/ selection, int mode) {
		lock.lock();
		try {
			_gtk_tree_selection_set_mode(selection, mode);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param selection cast=(GtkTreeSelection *)
	 * @param path cast=(GtkTreePath *)
	 */
	public static final native void _gtk_tree_selection_unselect_path(long /*int*/ selection, long /*int*/ path);
	public static final  void gtk_tree_selection_unselect_path(long /*int*/ selection, long /*int*/ path) {
		lock.lock();
		try {
			_gtk_tree_selection_unselect_path(selection, path);
		} finally {
			lock.unlock();
		}
	}
	/** @param selection cast=(GtkTreeSelection *) */
	public static final native void _gtk_tree_selection_unselect_all(long /*int*/ selection);
	public static final void gtk_tree_selection_unselect_all(long /*int*/ selection) {
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
	public static final native void _gtk_tree_selection_unselect_iter(long /*int*/ selection, long /*int*/ iter);
	public static final void gtk_tree_selection_unselect_iter(long /*int*/ selection, long /*int*/ iter) {
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
	public static final native void _gtk_tree_store_append(long /*int*/ store, long /*int*/ iter, long /*int*/ parent);
	public static final void gtk_tree_store_append(long /*int*/ store, long /*int*/ iter, long /*int*/ parent) {
		lock.lock();
		try {
			_gtk_tree_store_append(store, iter, parent);
		} finally {
			lock.unlock();
		}
	}
	/** @param store cast=(GtkTreeStore *) */
	public static final native void _gtk_tree_store_clear(long /*int*/ store);
	public static final void gtk_tree_store_clear(long /*int*/ store) {
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
	public static final native void _gtk_tree_store_insert(long /*int*/ store, long /*int*/ iter, long /*int*/ parent, int position);
	public static final void gtk_tree_store_insert(long /*int*/ store, long /*int*/ iter, long /*int*/ parent, int position) {
		lock.lock();
		try {
			_gtk_tree_store_insert(store, iter, parent, position);
		} finally {
			lock.unlock();
		}
	}
	/** @param types cast=(GType *) */
	public static final native long /*int*/ _gtk_tree_store_newv(int numColumns, long /*int*/[] types);
	public static final long /*int*/ gtk_tree_store_newv(int numColumns, long /*int*/[] types) {
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
	public static final native void _gtk_tree_store_remove(long /*int*/ store, long /*int*/ iter);
	public static final void gtk_tree_store_remove(long /*int*/ store, long /*int*/ iter) {
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
	public static final native void _gtk_tree_store_set(long /*int*/ store, long /*int*/ iter, int column, byte[] value, int terminator);
	public static final void gtk_tree_store_set(long /*int*/ store, long /*int*/ iter, int column, byte[] value, int terminator) {
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
	public static final native void _gtk_tree_store_set(long /*int*/ store, long /*int*/ iter, int column, int value, int terminator);
	public static final void gtk_tree_store_set(long /*int*/ store, long /*int*/ iter, int column, int value, int terminator) {
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
	public static final native void _gtk_tree_store_set(long /*int*/ store, long /*int*/ iter, int column, long value, int terminator);
	public static final void gtk_tree_store_set(long /*int*/ store, long /*int*/ iter, int column, long value, int terminator) {
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
	public static final native void _gtk_tree_store_set(long /*int*/ store, long /*int*/ iter, int column, GdkRGBA value, int terminator);
	public static final void gtk_tree_store_set(long /*int*/ store, long /*int*/ iter, int column, GdkRGBA value, int terminator) {
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
	public static final native void _gtk_tree_store_set(long /*int*/ store, long /*int*/ iter, int column, boolean value, int terminator);
	public static final void gtk_tree_store_set(long /*int*/ store, long /*int*/ iter, int column, boolean value, int terminator) {
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
	public static final native long /*int*/ _gtk_tree_view_create_row_drag_icon(long /*int*/ view, long /*int*/ path);
	public static final long /*int*/ gtk_tree_view_create_row_drag_icon(long /*int*/ view, long /*int*/ path) {
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
	public static final native boolean _gtk_tree_view_collapse_row(long /*int*/ view, long /*int*/ path);
	public static final boolean gtk_tree_view_collapse_row(long /*int*/ view, long /*int*/ path) {
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
	public static final native void _gtk_tree_view_column_add_attribute(long /*int*/ treeColumn, long /*int*/ cellRenderer, byte[] attribute, int column);
	public static final void gtk_tree_view_column_add_attribute(long /*int*/ treeColumn, long /*int*/ cellRenderer, byte[] attribute, int column) {
		lock.lock();
		try {
			_gtk_tree_view_column_add_attribute(treeColumn, cellRenderer, attribute, column);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param tree_column cast=(GtkTreeViewColumn *)
	 * @param cell_renderer cast=(GtkCellRenderer *)
	 * @param start_pos cast=(gint *)
	 * @param width cast=(gint *)
	 */
	public static final native boolean _gtk_tree_view_column_cell_get_position(long /*int*/ tree_column, long /*int*/ cell_renderer, int[] start_pos, int[] width);
	public static final boolean gtk_tree_view_column_cell_get_position(long /*int*/ tree_column, long /*int*/ cell_renderer, int[] start_pos, int[] width) {
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
	public static final native void _gtk_tree_view_column_cell_get_size(long /*int*/ tree_column, GdkRectangle cell_area, int[] x_offset, int[] y_offset, int[] width, int[] height);
	public static final void gtk_tree_view_column_cell_get_size(long /*int*/ tree_column, GdkRectangle cell_area, int[] x_offset, int[] y_offset, int[] width, int[] height) {
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
	public static final native void _gtk_tree_view_column_cell_set_cell_data(long /*int*/ tree_column, long /*int*/ tree_model, long /*int*/ iter, boolean is_expander, boolean is_expanded);
	public static final void gtk_tree_view_column_cell_set_cell_data(long /*int*/ tree_column, long /*int*/ tree_model, long /*int*/ iter, boolean is_expander, boolean is_expanded) {
		lock.lock();
		try {
			_gtk_tree_view_column_cell_set_cell_data(tree_column, tree_model, iter, is_expander, is_expanded);
		} finally {
			lock.unlock();
		}
	}
	/** @param tree_column cast=(GtkTreeViewColumn *) */
	public static final native void _gtk_tree_view_column_clear(long /*int*/ tree_column);
	public static final void gtk_tree_view_column_clear(long /*int*/ tree_column) {
		lock.lock();
		try {
			_gtk_tree_view_column_clear(tree_column);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param column cast=(GtkTreeViewColumn *)
	 */
	public static final native long /*int*/_gtk_tree_view_column_get_button(long /*int*/ column);
	public static final long /*int*/ gtk_tree_view_column_get_button(long /*int*/ column) {
		lock.lock();
		try {
			return _gtk_tree_view_column_get_button(column);
		} finally {
			lock.unlock();
		}
	}
	/** @param column cast=(GtkTreeViewColumn *) */
	public static final native int _gtk_tree_view_column_get_fixed_width(long /*int*/ column);
	public static final int gtk_tree_view_column_get_fixed_width(long /*int*/ column) {
		lock.lock();
		try {
			return _gtk_tree_view_column_get_fixed_width(column);
		} finally {
			lock.unlock();
		}
	}
	/** @param column cast=(GtkTreeViewColumn *) */
	public static final native boolean _gtk_tree_view_column_get_reorderable(long /*int*/ column);
	public static final boolean gtk_tree_view_column_get_reorderable(long /*int*/ column) {
		lock.lock();
		try {
			return _gtk_tree_view_column_get_reorderable(column);
		} finally {
			lock.unlock();
		}
	}
	/** @param column cast=(GtkTreeViewColumn *) */
	public static final native boolean _gtk_tree_view_column_get_resizable(long /*int*/ column);
	public static final boolean gtk_tree_view_column_get_resizable(long /*int*/ column) {
		lock.lock();
		try {
			return _gtk_tree_view_column_get_resizable(column);
		} finally {
			lock.unlock();
		}
	}
	/** @param column cast=(GtkTreeViewColumn *) */
	public static final native boolean _gtk_tree_view_column_get_visible(long /*int*/ column);
	public static final boolean gtk_tree_view_column_get_visible(long /*int*/ column) {
		lock.lock();
		try {
			return _gtk_tree_view_column_get_visible(column);
		} finally {
			lock.unlock();
		}
	}
	/** @param column cast=(GtkTreeViewColumn *) */
	public static final native int _gtk_tree_view_column_get_width(long /*int*/ column);
	public static final int gtk_tree_view_column_get_width(long /*int*/ column) {
		lock.lock();
		try {
			return _gtk_tree_view_column_get_width(column);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_tree_view_column_new();
	public static final long /*int*/ gtk_tree_view_column_new() {
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
	public static final native void _gtk_tree_view_column_pack_start(long /*int*/ tree_column, long /*int*/ cell_renderer, boolean expand);
	public static final void gtk_tree_view_column_pack_start(long /*int*/ tree_column, long /*int*/ cell_renderer, boolean expand) {
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
	public static final native void _gtk_tree_view_column_pack_end(long /*int*/ tree_column, long /*int*/ cell_renderer, boolean expand);
	public static final void gtk_tree_view_column_pack_end(long /*int*/ tree_column, long /*int*/ cell_renderer, boolean expand) {
		lock.lock();
		try {
			_gtk_tree_view_column_pack_end(tree_column, cell_renderer, expand);
		} finally {
			lock.unlock();
		}
	}
	/** @param tree_column cast=(GtkTreeViewColumn *) */
	public static final native void _gtk_tree_view_column_set_alignment(long /*int*/ tree_column, float xalign);
	public static final void gtk_tree_view_column_set_alignment(long /*int*/ tree_column, float xalign) {
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
	 * @param destroy cast=(GDestroyNotify)
	 */
	public static final native void _gtk_tree_view_column_set_cell_data_func(long /*int*/ tree_column, long /*int*/ cell_renderer, long /*int*/ func, long /*int*/ func_data, long /*int*/ destroy);
	public static final void gtk_tree_view_column_set_cell_data_func(long /*int*/ tree_column, long /*int*/ cell_renderer, long /*int*/ func, long /*int*/ func_data, long /*int*/ destroy) {
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
	public static final native void _gtk_tree_view_column_set_clickable(long /*int*/ column, boolean clickable);
	public static final void gtk_tree_view_column_set_clickable(long /*int*/ column, boolean clickable) {
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
	public static final native void _gtk_tree_view_column_set_fixed_width(long /*int*/ column, int fixed_width);
	public static final void gtk_tree_view_column_set_fixed_width(long /*int*/ column, int fixed_width) {
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
	public static final native void _gtk_tree_view_column_set_min_width(long /*int*/ tree_column, int min_width);
	public static final void gtk_tree_view_column_set_min_width(long /*int*/ tree_column, int min_width) {
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
	public static final native void _gtk_tree_view_column_set_reorderable(long /*int*/ column, boolean reorderable);
	public static final void gtk_tree_view_column_set_reorderable(long /*int*/ column, boolean reorderable) {
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
	public static final native void _gtk_tree_view_column_set_resizable(long /*int*/ column, boolean resizable);
	public static final void gtk_tree_view_column_set_resizable(long /*int*/ column, boolean resizable) {
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
	public static final native void _gtk_tree_view_column_set_sizing(long /*int*/ column, int type);
	public static final void gtk_tree_view_column_set_sizing(long /*int*/ column, int type) {
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
	public static final native void _gtk_tree_view_column_set_sort_indicator(long /*int*/ tree_column, boolean setting);
	public static final void gtk_tree_view_column_set_sort_indicator(long /*int*/ tree_column, boolean setting) {
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
	public static final native void _gtk_tree_view_column_set_sort_order(long /*int*/ tree_column, int order);
	public static final void gtk_tree_view_column_set_sort_order(long /*int*/ tree_column, int order) {
		lock.lock();
		try {
			_gtk_tree_view_column_set_sort_order(tree_column, order);
		} finally {
			lock.unlock();
		}
	}
	/** @param tree_column cast=(GtkTreeViewColumn *) */
	public static final native void _gtk_tree_view_column_set_visible (long /*int*/ tree_column, boolean visible);
	public static final void gtk_tree_view_column_set_visible (long /*int*/ tree_column, boolean visible) {
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
	public static final native void _gtk_tree_view_column_set_widget(long /*int*/ tree_column, long /*int*/ widget);
	public static final void gtk_tree_view_column_set_widget(long /*int*/ tree_column, long /*int*/ widget) {
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
	public static final native void _gtk_tree_view_set_drag_dest_row(long /*int*/ view, long /*int*/ path, int pos);
	public static final void gtk_tree_view_set_drag_dest_row(long /*int*/ view, long /*int*/ path, int pos) {
		lock.lock();
		try {
			_gtk_tree_view_set_drag_dest_row(view, path, pos);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param view cast=(GtkTreeView *)
	 * @param path cast=(GtkTreePath *)
	 * @param open_all cast=(gboolean)
	 */
	public static final native boolean _gtk_tree_view_expand_row(long /*int*/ view, long /*int*/ path, boolean open_all);
	public static final boolean gtk_tree_view_expand_row(long /*int*/ view, long /*int*/ path, boolean open_all) {
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
	public static final native void _gtk_tree_view_get_background_area(long /*int*/ tree_view, long /*int*/ path, long /*int*/ column, GdkRectangle rect);
	public static final void gtk_tree_view_get_background_area(long /*int*/ tree_view, long /*int*/ path, long /*int*/ column, GdkRectangle rect) {
		lock.lock();
		try {
			_gtk_tree_view_get_background_area(tree_view, path, column, rect);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param tree_view cast=(GtkTreeView *)
	 */
	public static final native long /*int*/ _gtk_tree_view_get_bin_window(long /*int*/ tree_view);
	/** [GTK3 only, if-def'd in os.h] */
	public static final long /*int*/ gtk_tree_view_get_bin_window(long /*int*/ tree_view) {
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
	public static final native void _gtk_tree_view_get_cell_area(long /*int*/ tree_view, long /*int*/ path, long /*int*/ column, GdkRectangle rect);
	public static final void gtk_tree_view_get_cell_area(long /*int*/ tree_view, long /*int*/ path, long /*int*/ column, GdkRectangle rect) {
		lock.lock();
		try {
			_gtk_tree_view_get_cell_area(tree_view, path, column, rect);
		} finally {
			lock.unlock();
		}
	}
	/** @param tree_view cast=(GtkTreeView *) */
	public static final native long /*int*/_gtk_tree_view_get_expander_column(long /*int*/ tree_view);
	public static final long /*int*/gtk_tree_view_get_expander_column(long /*int*/ tree_view) {
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
	public static final native long /*int*/ _gtk_tree_view_get_column(long /*int*/ tree_view, int n);
	public static final long /*int*/ gtk_tree_view_get_column(long /*int*/ tree_view, int n) {
		lock.lock();
		try {
			return _gtk_tree_view_get_column(tree_view, n);
		} finally {
			lock.unlock();
		}
	}
	/** @param tree_view cast=(GtkTreeView *) */
	public static final native long /*int*/ _gtk_tree_view_get_columns(long /*int*/ tree_view);
	public static final long /*int*/ gtk_tree_view_get_columns(long /*int*/ tree_view) {
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
	public static final native void _gtk_tree_view_get_cursor(long /*int*/ tree_view, long /*int*/[] path, long /*int*/[] focus_column);
	public static final void gtk_tree_view_get_cursor(long /*int*/ tree_view, long /*int*/[] path, long /*int*/[] focus_column) {
		lock.lock();
		try {
			_gtk_tree_view_get_cursor(tree_view, path, focus_column);
		} finally {
			lock.unlock();
		}
	}
	/** @param tree_view cast=(GtkTreeView *) */
	public static final native boolean _gtk_tree_view_get_headers_visible(long /*int*/ tree_view);
	public static final boolean gtk_tree_view_get_headers_visible(long /*int*/ tree_view) {
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
	public static final native boolean _gtk_tree_view_get_path_at_pos(long /*int*/ tree_view, int x, int y, long /*int*/[] path, long /*int*/[] column, int[] cell_x, int[] cell_y);
	public static final boolean gtk_tree_view_get_path_at_pos(long /*int*/ tree_view, int x, int y, long /*int*/[] path, long /*int*/[] column, int[] cell_x, int[] cell_y) {
		lock.lock();
		try {
			return _gtk_tree_view_get_path_at_pos(tree_view, x, y, path, column, cell_x, cell_y);
		} finally {
			lock.unlock();
		}
	}
	/** @param tree_view cast=(GtkTreeView *) */
	public static final native long /*int*/ _gtk_tree_view_get_selection(long /*int*/ tree_view);
	public static final long /*int*/ gtk_tree_view_get_selection(long /*int*/ tree_view) {
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
	public static final native void _gtk_tree_view_get_visible_rect(long /*int*/ tree_view, GdkRectangle visible_rect);
	public static final void gtk_tree_view_get_visible_rect(long /*int*/ tree_view, GdkRectangle visible_rect) {
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
	public static final native int _gtk_tree_view_insert_column(long /*int*/ tree_view, long /*int*/ column, int position);
	public static final int gtk_tree_view_insert_column(long /*int*/ tree_view, long /*int*/ column, int position) {
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
	public static final native void _gtk_tree_view_move_column_after(long /*int*/ tree_view, long /*int*/ column, long /*int*/ base_column);
	public static final void gtk_tree_view_move_column_after(long /*int*/ tree_view, long /*int*/ column, long /*int*/base_column) {
		lock.lock();
		try {
			_gtk_tree_view_move_column_after(tree_view, column, base_column);
		} finally {
			lock.unlock();
		}
	}
	/** @param model cast=(GtkTreeModel *) */
	public static final native long /*int*/ _gtk_tree_view_new_with_model(long /*int*/ model);
	public static final long /*int*/ gtk_tree_view_new_with_model(long /*int*/ model) {
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
	public static final native void _gtk_tree_view_remove_column(long /*int*/ tree_view, long /*int*/ column);
	public static final void gtk_tree_view_remove_column(long /*int*/ tree_view, long /*int*/ column) {
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
	public static final native boolean _gtk_tree_view_row_expanded(long /*int*/ view, long /*int*/ path);
	public static final boolean gtk_tree_view_row_expanded(long /*int*/ view, long /*int*/ path) {
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
	public static final native void _gtk_tree_view_scroll_to_cell(long /*int*/ tree_view, long /*int*/ path, long /*int*/ column, boolean use_align, float row_aligh, float column_align);
	public static final void gtk_tree_view_scroll_to_cell(long /*int*/ tree_view, long /*int*/ path, long /*int*/ column, boolean use_align, float row_aligh, float column_align) {
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
	public static final native void _gtk_tree_view_scroll_to_point (long /*int*/ tree_view, int tree_x, int tree_y);
	public static final void gtk_tree_view_scroll_to_point (long /*int*/ tree_view, int tree_x, int tree_y) {
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
	public static final native void _gtk_tree_view_set_cursor(long /*int*/ tree_view, long /*int*/ path, long /*int*/ focus_column, boolean start_editing);
	public static final void gtk_tree_view_set_cursor(long /*int*/ tree_view, long /*int*/ path, long /*int*/ focus_column, boolean start_editing) {
		lock.lock();
		try {
			_gtk_tree_view_set_cursor(tree_view, path, focus_column, start_editing);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param tree_view cast=(GtkTreeView*)
	 * @param grid_lines cast=(GtkTreeViewGridLines)
	 */
	public static final native void _gtk_tree_view_set_grid_lines(long /*int*/ tree_view, int grid_lines);
	public static final void gtk_tree_view_set_grid_lines(long /*int*/ tree_view, int grid_lines) {
		lock.lock();
		try {
			_gtk_tree_view_set_grid_lines(tree_view, grid_lines);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param tree_view cast=(GtkTreeView*)
	 */
	public static final native int _gtk_tree_view_get_grid_lines(long /*int*/ tree_view);
	public static final int gtk_tree_view_get_grid_lines(long /*int*/ tree_view) {
		lock.lock();
		try {
			return _gtk_tree_view_get_grid_lines(tree_view);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param tree_view cast=(GtkTreeView *)
	 * @param visible cast=(gboolean)
	 */
	public static final native void _gtk_tree_view_set_headers_visible(long /*int*/ tree_view, boolean visible);
	public static final void gtk_tree_view_set_headers_visible(long /*int*/ tree_view, boolean visible) {
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
	public static final native void _gtk_tree_view_set_model(long /*int*/ tree_view, long /*int*/ model);
	public static final void gtk_tree_view_set_model(long /*int*/ tree_view, long /*int*/ model) {
		lock.lock();
		try {
			_gtk_tree_view_set_model(tree_view, model);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param tree_view cast=(GtkTreeView *)
	 * @param column cast=(gint)
	 */
	public static final native void _gtk_tree_view_set_search_column(long /*int*/ tree_view, int column);
	public static final void gtk_tree_view_set_search_column(long /*int*/ tree_view, int column) {
		lock.lock();
		try {
			_gtk_tree_view_set_search_column(tree_view, column);
		} finally {
			lock.unlock();
		}
	}
	/** @param tree_view cast=(GtkTreeView *) */
	public static final native void _gtk_tree_view_unset_rows_drag_dest(long /*int*/ tree_view);
	public static final void gtk_tree_view_unset_rows_drag_dest(long /*int*/ tree_view) {
		lock.lock();
		try {
			_gtk_tree_view_unset_rows_drag_dest(tree_view);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param tree_view cast=(GtkTreeView *)
	 * @param bx cast=(gint)
	 * @param by cast=(gint)
	 * @param tx cast=(gint *)
	 * @param ty cast=(gint *)
	 */
	public static final native void _gtk_tree_view_convert_bin_window_to_tree_coords(long /*int*/ tree_view, int bx, int by, int[] tx, int[] ty);
	public static final void gtk_tree_view_convert_bin_window_to_tree_coords(long /*int*/ tree_view, int bx, int by, int[] tx, int[] ty) {
		lock.lock();
		try {
			_gtk_tree_view_convert_bin_window_to_tree_coords(tree_view, bx, by, tx, ty);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param viewport cast=(GtkViewport *)
	 * @param type cast=(GtkShadowType)
	 */
	public static final native void _gtk_viewport_set_shadow_type(long /*int*/ viewport, int type);
	public static final void gtk_viewport_set_shadow_type(long /*int*/ viewport, int type) {
		lock.lock();
		try {
			_gtk_viewport_set_shadow_type(viewport, type);
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
	public static final native void _gtk_widget_add_accelerator(long /*int*/ widget, byte[] accel_signal, long /*int*/ accel_group, int accel_key, int accel_mods, int accel_flags);
	public static final void gtk_widget_add_accelerator(long /*int*/ widget, byte[] accel_signal, long /*int*/ accel_group, int accel_key, int accel_mods, int accel_flags) {
		lock.lock();
		try {
			_gtk_widget_add_accelerator(widget, accel_signal, accel_group, accel_key, accel_mods, accel_flags);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 * @param events cast=(gint)
	 */
	public static final native void _gtk_widget_add_events(long /*int*/ widget, int events);
	/** [GTK3 only] */
	public static final void gtk_widget_add_events(long /*int*/ widget, int events) {
		lock.lock();
		try {
			_gtk_widget_add_events(widget, events);
		} finally {
			lock.unlock();
		}
	}
	/** @param widget cast=(GtkWidget *) */
	public static final native boolean _gtk_widget_child_focus(long /*int*/ widget, int direction);
	public static final boolean gtk_widget_child_focus(long /*int*/ widget, int direction) {
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
	public static final native long /*int*/ _gtk_widget_create_pango_layout(long /*int*/ widget, byte[] text);
	public static final long /*int*/ gtk_widget_create_pango_layout(long /*int*/ widget, byte[] text) {
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
	public static final native long /*int*/ _gtk_widget_create_pango_layout(long /*int*/ widget, long /*int*/ text);
	public static final long /*int*/ gtk_widget_create_pango_layout(long /*int*/ widget, long /*int*/ text) {
		lock.lock();
		try {
			return _gtk_widget_create_pango_layout(widget, text);
		} finally {
			lock.unlock();
		}
	}
	/** @param widget cast=(GtkWidget *) */
	public static final native void _gtk_widget_destroy(long /*int*/ widget);
	public static final void gtk_widget_destroy(long /*int*/ widget) {
		lock.lock();
		try {
			_gtk_widget_destroy(widget);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 * @param cr cast=(cairo_t *)
	 */
	public static final native void _gtk_widget_draw(long /*int*/ widget, long /*int*/ cr);
	/** [GTK3 only] */
	public static final void gtk_widget_draw(long /*int*/ widget, long /*int*/ cr) {
		lock.lock();
		try {
			_gtk_widget_draw(widget, cr);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param event cast=(GdkEvent *)
	 */
	public static final native boolean _gtk_widget_event(long /*int*/ widget, long /*int*/ event);
	public static final boolean gtk_widget_event(long /*int*/ widget, long /*int*/ event) {
		lock.lock();
		try {
			return _gtk_widget_event(widget, event);
		} finally {
			lock.unlock();
		}
	}
	/** @param widget cast=(GtkWidget *) */
	public static final native long /*int*/ _gtk_widget_get_accessible (long /*int*/ widget);
	public static final long /*int*/ gtk_widget_get_accessible (long /*int*/ widget) {
		lock.lock();
		try {
			return _gtk_widget_get_accessible(widget);
		} finally {
			lock.unlock();
		}
	}
	/** @param widget cast=(GtkWidget *) */
	public static final native boolean _gtk_widget_get_visible (long /*int*/ widget);
	public static final boolean gtk_widget_get_visible (long /*int*/ widget) {
		lock.lock();
		try {
			return _gtk_widget_get_visible(widget);
		} finally {
			lock.unlock();
		}
	}
	/** @param widget cast=(GtkWidget *) */
	public static final native boolean _gtk_widget_get_realized (long /*int*/ widget);
	public static final boolean gtk_widget_get_realized (long /*int*/ widget) {
		lock.lock();
		try {
			return _gtk_widget_get_realized(widget);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native boolean _gtk_widget_get_has_window (long /*int*/ widget);
	/** [GTK3 only, if-def'd in os.h] */
	public static final boolean gtk_widget_get_has_window (long /*int*/ widget) {
		lock.lock();
		try {
			return _gtk_widget_get_has_window(widget);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native boolean _gtk_widget_get_has_surface (long /*int*/ widget);
	/** [GTK4 only, if-def'd in os.h] */
	public static final boolean gtk_widget_get_has_surface (long /*int*/ widget) {
		lock.lock();
		try {
			return _gtk_widget_get_has_surface(widget);
		} finally {
			lock.unlock();
		}
	}
	/** @param widget cast=(GtkWidget *) */
	public static final native boolean _gtk_widget_get_can_default (long /*int*/ widget);
	public static final boolean gtk_widget_get_can_default (long /*int*/ widget) {
		lock.lock();
		try {
			return _gtk_widget_get_can_default(widget);
		} finally {
			lock.unlock();
		}
	}
	/** @param widget cast=(GtkWidget *) */
	public static final native boolean _gtk_widget_get_child_visible (long /*int*/ widget);
	public static final boolean gtk_widget_get_child_visible (long /*int*/ widget) {
		lock.lock();
		try {
			return _gtk_widget_get_child_visible(widget);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native int _gtk_widget_get_events(long /*int*/ widget);
	/** [GTK3 only] */
	public static final int gtk_widget_get_events(long /*int*/ widget) {
		lock.lock();
		try {
			return _gtk_widget_get_events(widget);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native long /*int*/ _gtk_widget_get_window (long /*int*/ widget);
	/** [GTK3 only] */
	public static final long /*int*/ gtk_widget_get_window (long /*int*/ widget) {
		lock.lock();
		try {
			return _gtk_widget_get_window(widget);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native long /*int*/ _gtk_widget_get_surface (long /*int*/ widget);
	/** [GTK4 only] */
	public static final long /*int*/ gtk_widget_get_surface (long /*int*/ widget) {
		lock.lock();
		try {
			return _gtk_widget_get_surface(widget);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native int _gtk_widget_get_margin_start (long /*int*/ widget);
	public static final int gtk_widget_get_margin_start (long /*int*/ widget) {
		lock.lock();
		try {
			return _gtk_widget_get_margin_start(widget);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native int _gtk_widget_get_margin_end (long /*int*/ widget);
	public static final int gtk_widget_get_margin_end (long /*int*/ widget) {
		lock.lock();
		try {
			return _gtk_widget_get_margin_end(widget);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native int _gtk_widget_get_margin_top (long /*int*/ widget);
	public static final int gtk_widget_get_margin_top (long /*int*/ widget) {
		lock.lock();
		try {
			return _gtk_widget_get_margin_top(widget);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native int _gtk_widget_get_margin_bottom (long /*int*/ widget);
	public static final int gtk_widget_get_margin_bottom (long /*int*/ widget) {
		lock.lock();
		try {
			return _gtk_widget_get_margin_bottom(widget);
		} finally {
			lock.unlock();
		}
	}
	/** @param widget cast=(GtkWidget *)  */
	public static final native boolean _gtk_widget_get_mapped(long /*int*/ widget);
	public static final boolean gtk_widget_get_mapped(long /*int*/ widget) {
		lock.lock();
		try {
			return _gtk_widget_get_mapped(widget);
		} finally {
			lock.unlock();
		}
	}
	/** @param widget cast=(GtkWidget *) */
	public static final native long /*int*/ _gtk_widget_get_pango_context(long /*int*/ widget);
	public static final long /*int*/ gtk_widget_get_pango_context(long /*int*/ widget) {
		lock.lock();
		try {
			return _gtk_widget_get_pango_context(widget);
		} finally {
			lock.unlock();
		}
	}
	/** @param widget cast=(GtkWidget *) */
	public static final native long /*int*/ _gtk_widget_get_parent(long /*int*/ widget);
	public static final long /*int*/ gtk_widget_get_parent(long /*int*/ widget) {
		lock.lock();
		try {
			return _gtk_widget_get_parent(widget);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native long /*int*/ _gtk_widget_get_parent_window(long /*int*/ widget);
	public static final long /*int*/ gtk_widget_get_parent_window(long /*int*/ widget) {
		lock.lock();
		try {
			return _gtk_widget_get_parent_window(widget);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native long /*int*/ _gtk_widget_get_parent_surface(long /*int*/ widget);
	public static final long /*int*/ gtk_widget_get_parent_surface(long /*int*/ widget) {
		lock.lock();
		try {
			return _gtk_widget_get_parent_surface(widget);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param allocation cast=(GtkAllocation *),flags=no_in
	 * */
	public static final native void _gtk_widget_get_allocation (long /*int*/ widget, GtkAllocation allocation);
	public static final void gtk_widget_get_allocation (long /*int*/ widget, GtkAllocation allocation) {
		lock.lock();
		try {
			_gtk_widget_get_allocation(widget, allocation);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param context cast=(GtkStyleContext *)
	 * @param cr cast=(cairo_t *)
	 * @param x cast=(gdouble)
	 * @param y cast=(gdouble)
	 * @param width cast=(gdouble)
	 * @param height cast=(gdouble)
	 */
	public static final native void _gtk_render_handle(long /*int*/ context, long /*int*/ cr, double x , double y, double width, double height);
	public static final void gtk_render_handle(long /*int*/ context, long /*int*/ cr, double x , double y, double width, double height) {
		lock.lock();
		try {
			_gtk_render_handle(context,cr, x ,y, width, height);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native long /*int*/ _gtk_widget_get_style_context(long /*int*/ widget);
	public static final long /*int*/ gtk_widget_get_style_context(long /*int*/ widget) {
		lock.lock();
		try {
			return _gtk_widget_get_style_context(widget);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param width cast=(gint *)
	 * @param height cast=(gint *)
	 */
	public static final native void _gtk_widget_get_size_request(long /*int*/ widget, int [] width, int [] height);
	public static final void gtk_widget_get_size_request(long /*int*/ widget, int [] width, int [] height) {
		lock.lock();
		try {
			_gtk_widget_get_size_request(widget, width, height);
		} finally {
			lock.unlock();
		}
	}
	/** @param widget cast=(GtkWidget *) */
	public static final native long /*int*/ _gtk_widget_get_toplevel (long /*int*/ widget);
	public static final long /*int*/ gtk_widget_get_toplevel (long /*int*/ widget) {
		lock.lock();
		try {
			return _gtk_widget_get_toplevel(widget);
		} finally {
			lock.unlock();
		}
	}
	/** @param widget cast=(GtkWidget *) */
	public static final native long /*int*/ _gtk_widget_get_tooltip_text (long /*int*/ widget);
	public static final long /*int*/ gtk_widget_get_tooltip_text (long /*int*/ widget) {
		lock.lock();
		try {
			return _gtk_widget_get_tooltip_text(widget);
		} finally {
			lock.unlock();
		}
	}
	/** @param widget cast=(GtkWidget *) */
	public static final native void _gtk_widget_grab_focus(long /*int*/ widget);
	public static final void gtk_widget_grab_focus(long /*int*/ widget) {
		lock.lock();
		try {
			_gtk_widget_grab_focus(widget);
		} finally {
			lock.unlock();
		}
	}
	/** @param widget cast=(GtkWidget *) */
	public static final native boolean _gtk_widget_has_focus(long /*int*/ widget);
	public static final boolean gtk_widget_has_focus(long /*int*/ widget) {
	       lock.lock();
	       try {
	               return _gtk_widget_has_focus(widget);
	       } finally {
	               lock.unlock();
	       }
	}
	/** @param widget cast=(GtkWidget *) */
	public static final native void _gtk_widget_hide(long /*int*/ widget);
	public static final void gtk_widget_hide(long /*int*/ widget) {
		lock.lock();
		try {
			_gtk_widget_hide(widget);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param region cast=(cairo_region_t *)
	 */
	public static final native void _gtk_widget_input_shape_combine_region(long /*int*/ widget, long /*int*/ region);
	public static final void gtk_widget_input_shape_combine_region(long /*int*/ widget, long /*int*/ region) {
		lock.lock();
		try {
			_gtk_widget_input_shape_combine_region(widget, region);
		} finally {
			lock.unlock();
		}
	}
	/** @param widget cast=(GtkWidget *) */
	public static final native boolean _gtk_widget_is_focus(long /*int*/ widget);
	public static final boolean gtk_widget_is_focus(long /*int*/ widget) {
		lock.lock();
		try {
			return _gtk_widget_is_focus(widget);
		} finally {
			lock.unlock();
		}
	}
	/** @param widget cast=(GtkWidget *) */
	public static final native void _gtk_widget_map(long /*int*/ widget);
	public static final void gtk_widget_map(long /*int*/ widget) {
		lock.lock();
		try {
			_gtk_widget_map(widget);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param orientation cast=(GtkOrientation)
	 * @param for_size cast=(int)
	 * @param minimum cast=(int *)
	 * @param natural cast=(int *)
	 * @param minimum_baseline cast=(int *)
	 * @param natural_baseline cast=(int *)
	 */
	public static final native void _gtk_widget_measure(long /*int*/ widget, int orientation, int for_size, int[] minimum, int[] natural, int[] minimum_baseline, int[] natural_baseline);
	/** [GTK4 only, if-def'd in os.h] */
	public static final void gtk_widget_measure(long /*int*/ widget,  int orientation, int for_size, int[] minimum, int[] natural, int[] minimum_baseline, int[] natural_baseline) {
		lock.lock();
		try {
			_gtk_widget_measure(widget, orientation, for_size, minimum, natural, minimum_baseline, natural_baseline);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param group_cycling cast=(gboolean)
	 */
	public static final native boolean _gtk_widget_mnemonic_activate(long /*int*/ widget, boolean group_cycling);
	public static final boolean gtk_widget_mnemonic_activate(long /*int*/ widget, boolean group_cycling) {
		lock.lock();
		try {
			return _gtk_widget_mnemonic_activate(widget, group_cycling);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native void _gtk_widget_override_color(long /*int*/ widget, int state, GdkRGBA color);
	/** [GTK3; 3.16 deprecated, replaced]*/
	public static final void gtk_widget_override_color(long /*int*/ widget, int state, GdkRGBA color) {
		lock.lock();
		try {
			_gtk_widget_override_color(widget, state, color);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native void _gtk_widget_override_background_color(long /*int*/ widget, int state, GdkRGBA color);
	/** [GTK3; 3.16 deprecated, replaced] */
	public static final void gtk_widget_override_background_color(long /*int*/ widget, int state, GdkRGBA color) {
		lock.lock();
		try {
			_gtk_widget_override_background_color(widget, state, color);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 */
	public static final native void _gtk_widget_override_font(long /*int*/ widget, long /*int*/ font);
	/** [GTK3; 3.16 deprecated] */
	public static final void gtk_widget_override_font(long /*int*/ widget, long /*int*/ font) {
		lock.lock();
		try {
			_gtk_widget_override_font(widget, font);
		} finally {
			lock.unlock();
		}
	}
	/** @param widget cast=(GtkWidget *) */
	public static final native void _gtk_widget_queue_resize(long /*int*/ widget);
	public static final void gtk_widget_queue_resize(long /*int*/ widget) {
		lock.lock();
		try {
			_gtk_widget_queue_resize(widget);
		} finally {
			lock.unlock();
		}
	}
	/** @param widget cast=(GtkWidget *) */
	public static final native void _gtk_widget_realize(long /*int*/ widget);
	public static final void gtk_widget_realize(long /*int*/ widget) {
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
	public static final native void _gtk_widget_remove_accelerator(long /*int*/ widget, long /*int*/ accel_group, int accel_key, int accel_mods);
	public static final void gtk_widget_remove_accelerator(long /*int*/ widget, long /*int*/ accel_group, int accel_key, int accel_mods) {
		lock.lock();
		try {
			_gtk_widget_remove_accelerator(widget, accel_group, accel_key, accel_mods);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 * @param new_parent cast=(GtkWidget *)
	 */
	public static final native void _gtk_widget_reparent(long /*int*/ widget, long /*int*/ new_parent);
	/** deprecated as of 3.14 */
	public static final void gtk_widget_reparent(long /*int*/ widget, long /*int*/ new_parent) {
		lock.lock();
		try {
			_gtk_widget_reparent(widget, new_parent);
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
	 * @param can_default cast=(gboolean)
	 */
	public static final native void _gtk_widget_set_can_default(long /*int*/ widget, boolean can_default);
	public static final void gtk_widget_set_can_default(long /*int*/ widget, boolean can_default) {
		lock.lock();
		try {
			_gtk_widget_set_can_default(widget,can_default) ;
		} finally {
			lock.unlock();
		}
	}
	/** @param widget cast=(GtkWidget *) */
	public static final native void _gtk_widget_queue_draw(long /*int*/ widget);
	public static final void gtk_widget_queue_draw(long /*int*/ widget) {
		lock.lock();
		try {
			_gtk_widget_queue_draw(widget) ;
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param can_focus cast=(gboolean)
	 */
	public static final native void _gtk_widget_set_can_focus(long /*int*/ widget, boolean can_focus);
	public static final void gtk_widget_set_can_focus(long /*int*/ widget, boolean can_focus) {
		lock.lock();
		try {
			_gtk_widget_set_can_focus(widget,can_focus);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param visible cast=(gboolean)
	 */
	public static final native void _gtk_widget_set_visible(long /*int*/ widget, boolean visible);
	public static final void gtk_widget_set_visible(long /*int*/ widget, boolean visible) {
		lock.lock();
		try {
			_gtk_widget_set_visible(widget, visible);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param dir cast=(GtkTextDirection)
	 */
	public static final native void _gtk_widget_set_direction(long /*int*/ widget, int dir);
	public static final void gtk_widget_set_direction(long /*int*/ widget, int dir) {
		lock.lock();
		try {
			_gtk_widget_set_direction(widget, dir);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param receives_default cast=(gboolean)
	 */
	public static final native void _gtk_widget_set_receives_default(long /*int*/ widget, boolean receives_default);
	public static final void gtk_widget_set_receives_default(long /*int*/ widget, boolean receives_default) {
		lock.lock();
		try {
			_gtk_widget_set_receives_default(widget, receives_default);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 * @param double_buffered cast=(gboolean)
	 */
	public static final native void _gtk_widget_set_double_buffered(long /*int*/ widget, boolean double_buffered);
	/** [GTK3 only; 3.14 deprecated] */
	public static final void gtk_widget_set_double_buffered(long /*int*/ widget, boolean double_buffered) {
		lock.lock();
		try {
			_gtk_widget_set_double_buffered(widget, double_buffered);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 * @param val cast=(gboolean)
	 */
	public static final native void _gtk_widget_set_focus_on_click(long /*int*/ widget, boolean val);
	public static final void gtk_widget_set_focus_on_click(long /*int*/ widget, boolean val) {
		lock.lock();
		try {
			_gtk_widget_set_focus_on_click(widget, val);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param name cast=(const char *)
	 */
	public static final native void _gtk_widget_set_name(long /*int*/ widget, byte[] name);
	public static final void gtk_widget_set_name(long /*int*/ widget, byte[] name) {
		lock.lock();
		try {
			_gtk_widget_set_name(widget, name);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native void _gtk_widget_set_opacity(long /*int*/ widget, double opacity);
	public static final void gtk_widget_set_opacity(long /*int*/ widget, double opacity) {
		lock.lock();
		try {
			_gtk_widget_set_opacity(widget, opacity);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native double _gtk_widget_get_opacity(long /*int*/ widget);
	public static final double gtk_widget_get_opacity(long /*int*/ widget) {
		lock.lock();
		try {
			return _gtk_widget_get_opacity(widget);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 * @param redraw cast=(gboolean)
	 */
	public static final native void _gtk_widget_set_redraw_on_allocate(long /*int*/ widget, boolean redraw);
	/** [GTK3 only] */
	public static final void gtk_widget_set_redraw_on_allocate(long /*int*/ widget, boolean redraw) {
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
	public static final native void _gtk_widget_set_sensitive(long /*int*/ widget, boolean sensitive);
	public static final void gtk_widget_set_sensitive(long /*int*/ widget, boolean sensitive) {
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
	public static final native void _gtk_widget_set_size_request(long /*int*/ widget, int width, int height);
	public static final void gtk_widget_set_size_request(long /*int*/ widget, int width, int height) {
		lock.lock();
		try {
			_gtk_widget_set_size_request(widget, width, height);
		} finally {
			lock.unlock();
		}
	}
	/** @param widget cast=(GtkWidget *) */
	public static final native void _gtk_widget_show(long /*int*/ widget);
	public static final void gtk_widget_show(long /*int*/ widget) {
		lock.lock();
		try {
			_gtk_widget_show(widget);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 * @param allocation cast=(GtkAllocation *),flags=no_out
	 */
	public static final native void _gtk_widget_size_allocate(long /*int*/ widget, GtkAllocation allocation);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gtk_widget_size_allocate(long /*int*/ widget, GtkAllocation allocation) {
		lock.lock();
		try {
			_gtk_widget_size_allocate(widget, allocation);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 * @param allocation cast=(GtkAllocation *),flags=no_out
	 */
	public static final native void _gtk_widget_size_allocate(long /*int*/ widget, GtkAllocation allocation, int baseline);
	/** [GTK4 only, if-def'd in os.h] */
	public static final void gtk_widget_size_allocate(long /*int*/ widget, GtkAllocation allocation, int baseline) {
		lock.lock();
		try {
			_gtk_widget_size_allocate(widget, allocation, baseline);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 * @param allocation cast=(GtkAllocation *),flags=no_out
	 */
	public static final native void _gtk_widget_set_allocation(long /*int*/ widget, GtkAllocation allocation);
	/** [GTK3 only] */
	public static final void gtk_widget_set_allocation(long /*int*/ widget, GtkAllocation allocation) {
		lock.lock();
		try {
			_gtk_widget_set_allocation(widget, allocation);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param property_name cast=(const gchar *)
	 * @param terminator cast=(const gchar *),flags=sentinel
	 */
	public static final native void _gtk_widget_style_get(long /*int*/ widget, byte[] property_name, int[] value, long /*int*/ terminator);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gtk_widget_style_get(long /*int*/ widget, byte[] property_name, int[] value, long /*int*/ terminator) {
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
	public static final native void _gtk_widget_style_get(long /*int*/ widget, byte[] property_name, long[] value, long /*int*/ terminator);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gtk_widget_style_get(long /*int*/ widget, byte[] property_name, long[] value, long /*int*/ terminator) {
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
	 * @param dest_x cast=(gint *)
	 * @param dest_y cast=(gint *)
	 */
	public static final native boolean _gtk_widget_translate_coordinates(long /*int*/ src_widget, long /*int*/ dest_widget, int src_x, int src_y, int[] dest_x, int[] dest_y);
	public static final boolean gtk_widget_translate_coordinates(long /*int*/ src_widget, long /*int*/ dest_widget, int src_x, int src_y, int[] dest_x, int[] dest_y) {
		lock.lock();
		try {
			return _gtk_widget_translate_coordinates(src_widget, dest_widget, src_x, src_y, dest_x, dest_y);
		} finally {
			lock.unlock();
		}
	}
	/** @param window cast=(GtkWindow *) */
	public static final native boolean _gtk_window_activate_default(long /*int*/ window);
	public static final boolean gtk_window_activate_default(long /*int*/ window) {
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
	public static final native void _gtk_window_add_accel_group(long /*int*/ window, long /*int*/ accel_group);
	public static final void gtk_window_add_accel_group(long /*int*/ window, long /*int*/ accel_group) {
		lock.lock();
		try {
			_gtk_window_add_accel_group(window, accel_group);
		} finally {
			lock.unlock();
		}
	}
	/** @param handle cast=(GtkWindow *) */
	public static final native void _gtk_window_deiconify(long /*int*/ handle);
	public static final void gtk_window_deiconify(long /*int*/ handle) {
		lock.lock();
		try {
			_gtk_window_deiconify(handle);
		} finally {
			lock.unlock();
		}
	}
	/** @param window cast=(GtkWindow *) */
	public static final native long /*int*/ _gtk_window_get_focus(long /*int*/ window);
	public static final long /*int*/ gtk_window_get_focus(long /*int*/ window) {
		lock.lock();
		try {
			return _gtk_window_get_focus(window);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GtkWindow *)
	 */
	public static final native long /*int*/ _gtk_window_get_group(long /*int*/ window);
	public static final long /*int*/ gtk_window_get_group(long /*int*/ window) {
		lock.lock();
		try {
			return _gtk_window_get_group(window);
		} finally {
			lock.unlock();
		}
	}
	/** @param window cast=(GtkWindow *) */
	public static final native long /*int*/ _gtk_window_get_icon_list(long /*int*/ window);
	public static final long /*int*/ gtk_window_get_icon_list(long /*int*/ window) {
		lock.lock();
		try {
			return _gtk_window_get_icon_list(window);
		} finally {
			lock.unlock();
		}
	}
	/** @param window cast=(GtkWindow *) */
	public static final native boolean _gtk_window_get_modal(long /*int*/ window);
	public static final boolean gtk_window_get_modal(long /*int*/ window) {
		lock.lock();
		try {
			return _gtk_window_get_modal(window);
		} finally {
			lock.unlock();
		}
	}
	/** @param window cast=(GtkWindow *) */
	public static final native int _gtk_window_get_mnemonic_modifier(long /*int*/ window);
	public static final int gtk_window_get_mnemonic_modifier(long /*int*/ window) {
		lock.lock();
		try {
			return _gtk_window_get_mnemonic_modifier(window);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param handle cast=(GtkWindow *)
	 * @param x cast=(gint *)
	 * @param y cast=(gint *)
	 */
	public static final native void _gtk_window_get_position(long /*int*/ handle, int[] x, int[] y);
	public static final void gtk_window_get_position(long /*int*/ handle, int[] x, int[] y) {
		lock.lock();
		try {
			_gtk_window_get_position(handle, x, y);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param group cast=(GtkWindowGroup*)
	 * @param window cast=(GtkWindow*)
	 */
	public static final native void _gtk_window_group_add_window(long /*int*/ group, long /*int*/ window);
	public static final void gtk_window_group_add_window(long /*int*/ group, long /*int*/ window) {
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
	public static final native void _gtk_window_group_remove_window(long /*int*/ group, long /*int*/ window);
	public static final void gtk_window_group_remove_window(long /*int*/ group, long /*int*/ window) {
		lock.lock();
		try {
			_gtk_window_group_remove_window(group, window);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_window_group_new();
	public static final long /*int*/ gtk_window_group_new() {
		lock.lock();
		try {
			return _gtk_window_group_new();
		} finally {
			lock.unlock();
		}
	}
	/** @param handle cast=(GtkWindow *) */
	public static final native boolean _gtk_window_is_active(long /*int*/ handle);
	public static final boolean gtk_window_is_active(long /*int*/ handle) {
		lock.lock();
		try {
			return _gtk_window_is_active(handle);
		} finally {
			lock.unlock();
		}
	}
	/** @param handle cast=(GtkWindow *) */
	public static final native void _gtk_window_iconify(long /*int*/ handle);
	public static final void gtk_window_iconify(long /*int*/ handle) {
		lock.lock();
		try {
			_gtk_window_iconify(handle);
		} finally {
			lock.unlock();
		}
	}
	public static final native long /*int*/ _gtk_window_list_toplevels ();
	public static final long /*int*/ gtk_window_list_toplevels () {
		lock.lock();
		try {
			return _gtk_window_list_toplevels ();
		} finally {
			lock.unlock();
		}
	}
	/** @param handle cast=(GtkWindow *) */
	public static final native void _gtk_window_maximize(long /*int*/ handle);
	public static final void gtk_window_maximize(long /*int*/ handle) {
		lock.lock();
		try {
			_gtk_window_maximize(handle);
		} finally {
			lock.unlock();
		}
	}
	/** @param handle cast=(GtkWindow *) */
	public static final native void _gtk_window_fullscreen(long /*int*/ handle);
	public static final void gtk_window_fullscreen(long /*int*/ handle) {
		lock.lock();
		try {
			_gtk_window_fullscreen(handle);
		} finally {
			lock.unlock();
		}
	}
	/** @param handle cast=(GtkWindow *) */
	public static final native void _gtk_window_unfullscreen(long /*int*/ handle);
	public static final void gtk_window_unfullscreen(long /*int*/ handle) {
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
	public static final native void _gtk_window_move(long /*int*/ handle, int x, int y);
	public static final void gtk_window_move(long /*int*/ handle, int x, int y) {
		lock.lock();
		try {
			_gtk_window_move(handle, x, y);
		} finally {
			lock.unlock();
		}
	}
	/** @param type cast=(GtkWindowType) */
	public static final native long /*int*/ _gtk_window_new(int type);
	public static final long /*int*/ gtk_window_new(int type) {
		lock.lock();
		try {
			return _gtk_window_new(type);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GtkWindow *)
	 * @param accel_group cast=(GtkAccelGroup *)
	 */
	public static final native void _gtk_window_remove_accel_group(long /*int*/ window, long /*int*/ accel_group);
	public static final void gtk_window_remove_accel_group(long /*int*/ window, long /*int*/ accel_group) {
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
	public static final native void _gtk_window_resize(long /*int*/ handle, int x, int y);
	public static final void gtk_window_resize(long /*int*/ handle, int x, int y) {
		lock.lock();
		try {
			_gtk_window_resize(handle, x, y);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param handle cast=(GtkWindow *)
	 * @param width cast=(gint *)
	 * @param height cast=(gint *)
	 */
	public static final native void _gtk_window_get_size(long /*int*/ handle, int[] width, int[] height);
	public static final void gtk_window_get_size(long /*int*/ handle, int[] width, int[] height) {
		lock.lock();
		try {
			_gtk_window_get_size(handle, width, height);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GtkWindow *)
	 * @param attach_widget cast=(GtkWidget *)
	 */
	public static final native void _gtk_window_set_attached_to(long /*int*/ window, long /*int*/ attach_widget);
	public static final void gtk_window_set_attached_to(long /*int*/ window, long /*int*/ attach_widget) {
		lock.lock();
		try {
			_gtk_window_set_attached_to(window, attach_widget);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GtkWindow *)
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native void _gtk_window_set_default(long /*int*/ window, long /*int*/ widget);
	public static final void gtk_window_set_default(long /*int*/ window, long /*int*/ widget) {
		lock.lock();
		try {
			_gtk_window_set_default(window, widget);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GtkWindow *)
	 * @param decorated cast=(gboolean)
	 */
	public static final native void _gtk_window_set_decorated(long /*int*/ window, boolean decorated);
	public static final void gtk_window_set_decorated(long /*int*/ window, boolean decorated) {
		lock.lock();
		try {
			_gtk_window_set_decorated(window, decorated);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GtkWindow *)
	 * @param setting cast=(gboolean)
	 */
	public static final native void _gtk_window_set_destroy_with_parent(long /*int*/ window, boolean setting);
	public static final void gtk_window_set_destroy_with_parent(long /*int*/ window, boolean setting) {
		lock.lock();
		try {
			_gtk_window_set_destroy_with_parent(window, setting);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GtkWindow *)
	 * @param geometry_widget cast=(GtkWidget *)
	 * @param geometry flags=no_out
	 */
	public static final native void _gtk_window_set_geometry_hints(long /*int*/ window, long /*int*/ geometry_widget, GdkGeometry geometry, int geom_mask);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gtk_window_set_geometry_hints(long /*int*/ window, long /*int*/ geometry_widget, GdkGeometry geometry, int geom_mask) {
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
	public static final native void _gtk_window_set_icon_list(long /*int*/ window, long /*int*/ list);
	public static final void gtk_window_set_icon_list(long /*int*/ window, long /*int*/ list) {
		lock.lock();
		try {
			_gtk_window_set_icon_list(window, list);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GtkWindow *)
	 * @param setting cast=(gboolean)
	 */
	public static final native void _gtk_window_set_keep_above(long /*int*/ window, boolean setting);
	public static final void gtk_window_set_keep_above(long /*int*/ window, boolean setting) {
		lock.lock();
		try {
			_gtk_window_set_keep_above(window, setting);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GtkWindow *)
	 * @param modal cast=(gboolean)
	 */
	public static final native void _gtk_window_set_modal(long /*int*/ window, boolean modal);
	public static final void gtk_window_set_modal(long /*int*/ window, boolean modal) {
		lock.lock();
		try {
			_gtk_window_set_modal(window, modal);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param tip_text cast=(const gchar *)
	 */
	public static final native void _gtk_widget_set_tooltip_text(long /*int*/ widget, byte[] tip_text);
	public static final void gtk_widget_set_tooltip_text(long /*int*/ widget, byte[] tip_text) {
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
	public static final native void _gtk_widget_set_parent_window(long /*int*/ widget, long /*int*/ parent_window);
	/** [GTK3 only, if-def'd in os.h] */
	public static final void gtk_widget_set_parent_window(long /*int*/ widget, long /*int*/ parent_window) {
		lock.lock();
		try {
			_gtk_widget_set_parent_window(widget, parent_window);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param parent_surface cast=(GdkSurface *)
	 */
	public static final native void _gtk_widget_set_parent_surface(long /*int*/ widget, long /*int*/ parent_surface);
	/** [GTK4 only, if-def'd in os.h] */
	public static final void gtk_widget_set_parent_surface(long /*int*/ widget, long /*int*/ parent_surface) {
		lock.lock();
		try {
			_gtk_widget_set_parent_surface(widget, parent_surface);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GtkWindow *)
	 * @param resizable cast=(gboolean)
	 */
	public static final native void _gtk_window_set_resizable(long /*int*/ window, boolean resizable);
	public static final void gtk_window_set_resizable(long /*int*/ window, boolean resizable) {
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
	public static final native void _gtk_window_set_title(long /*int*/ window, byte[] title);
	public static final void gtk_window_set_title(long /*int*/ window, byte[] title) {
		lock.lock();
		try {
			_gtk_window_set_title(window, title);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param window cast=(GtkWindow *)
	 * @param skips_taskbar cast=(gboolean)
	 */
	public static final native void _gtk_window_set_skip_taskbar_hint(long /*int*/ window, boolean skips_taskbar);
	public static final void gtk_window_set_skip_taskbar_hint(long /*int*/ window, boolean skips_taskbar) {
		lock.lock();
		try {
			_gtk_window_set_skip_taskbar_hint(window, skips_taskbar);
		} finally {
			lock.unlock();
		}
	}
	/** @param window cast=(GtkWindow *) */
	public static final native void _gtk_window_set_type_hint(long /*int*/ window, int hint);
	public static final void gtk_window_set_type_hint(long /*int*/ window, int hint) {
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
	public static final native void _gtk_window_set_transient_for(long /*int*/ window, long /*int*/ parent);
	public static final void gtk_window_set_transient_for(long /*int*/ window, long /*int*/ parent) {
		lock.lock();
		try {
			_gtk_window_set_transient_for(window, parent);
		} finally {
			lock.unlock();
		}
	}
	/** @param handle cast=(GtkWindow *) */
	public static final native void _gtk_window_unmaximize(long /*int*/ handle);
	public static final void gtk_window_unmaximize(long /*int*/ handle) {
		lock.lock();
		try {
			_gtk_window_unmaximize(handle);
		} finally {
			lock.unlock();
		}
	}
	/** @method flags=dynamic */
	public static final native long /*int*/ _gtk_printer_option_widget_get_type();
	public static final long /*int*/ gtk_printer_option_widget_get_type() {
		lock.lock();
		try {
			return _gtk_printer_option_widget_get_type();
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 * @param region cast=(cairo_region_t *)
	 */
	public static final native void _gtk_widget_shape_combine_region(long /*int*/ widget, long /*int*/ region);
	/** [GTK3 only] */
	public static final void gtk_widget_shape_combine_region(long /*int*/ widget, long /*int*/ region) {
		lock.lock();
		try {
			_gtk_widget_shape_combine_region(widget,region);
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param child cast=(GtkWidget *)
	 * @param snapshot cast=(GtkSnapshot *)
	 */
	public static final native void _gtk_widget_snapshot_child(long /*int*/ widget, long /*int*/ child, long /*int*/ snapshot);
	/** [GTK4 only, if-def'd in os.h] */
	public static final void gtk_widget_snapshot_child(long /*int*/ widget, long /*int*/ child, long /*int*/ snapshot) {
		lock.lock();
		try {
			_gtk_widget_snapshot_child(widget,child,snapshot);
		} finally {
			lock.unlock();
		}
	}
}
