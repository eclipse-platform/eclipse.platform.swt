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
	public static final int GTK_DIALOG_DESTROY_WITH_PARENT = 1 << 1;
	public static final int GTK_DIALOG_MODAL = 1 << 0;
	public static final int GTK_DIR_TAB_FORWARD = 0;
	public static final int GTK_DIR_TAB_BACKWARD = 1;
	public static final int GTK_ENTRY_ICON_PRIMARY = 0;
	public static final int GTK_ENTRY_ICON_SECONDARY = 1;
	public static final int GTK_FILE_CHOOSER_ACTION_OPEN = 0;
	public static final int GTK_FILE_CHOOSER_ACTION_SAVE = 1;
	public static final int GTK_FILE_CHOOSER_ACTION_SELECT_FOLDER = 2;
	public static final int GTK_ICON_SIZE_MENU = 1;
	public static final int GTK_ICON_SIZE_SMALL_TOOLBAR = 2;
	public static final int GTK_ICON_SIZE_DIALOG = 6;
	public static final int GTK_ICON_LOOKUP_FORCE_SIZE = 4;
	public static final int GTK_JUSTIFY_CENTER = 0x2;
	public static final int GTK_JUSTIFY_LEFT = 0x0;
	public static final int GTK_JUSTIFY_RIGHT = 0x1;
	public static final int GTK_MESSAGE_INFO = 0;
	public static final int GTK_MESSAGE_WARNING = 1;
	public static final int GTK_MESSAGE_QUESTION = 2;
	public static final int GTK_MESSAGE_ERROR = 3;
	public static final int GTK_MOVEMENT_VISUAL_POSITIONS = 1;
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
	public static final int GTK_PRINT_PAGES_ALL = 0;
	public static final int GTK_PRINT_PAGES_CURRENT = 1;
	public static final int GTK_PRINT_PAGES_RANGES = 2;
	public static final int GTK_PRINT_PAGES_SELECTION = 3;
	public static final int GTK_PRINT_DUPLEX_SIMPLEX = 0;
	public static final int GTK_PRINT_DUPLEX_HORIZONTAL = 1;
	public static final int GTK_PRINT_DUPLEX_VERTICAL = 2;
	public static final int GTK_EVENT_CONTROLLER_SCROLL_NONE = 0;
	public static final int GTK_PHASE_CAPTURE = 1;
	public static final int GTK_PHASE_BUBBLE = 2;
	public static final int GTK_PHASE_TARGET = 3;
	public static final int GTK_PROGRESS_LEFT_TO_RIGHT = 0x0;
	public static final int GTK_PROGRESS_BOTTOM_TO_TOP = 0x2;
	public static final int GTK_RESPONSE_CANCEL = 0xfffffffa;
	public static final int GTK_RESPONSE_OK = 0xfffffffb;
	public static final int GTK_RESPONSE_ACCEPT = -3;
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
	public static final int GTK_SHADOW_ETCHED_IN = 0x3;
	public static final int GTK_SHADOW_ETCHED_OUT = 0x4;
	public static final int GTK_SHADOW_IN = 0x1;
	public static final int GTK_SHADOW_NONE = 0x0;
	public static final int GTK_SHADOW_OUT = 0x2;
	public static final int GTK_STATE_FLAG_NORMAL = 0;
	public static final int GTK_STATE_FLAG_ACTIVE = 1 << 0;
	public static final int GTK_STATE_FLAG_PRELIGHT = 1 << 1;
	public static final int GTK_STATE_FLAG_SELECTED = 1 << 2;
	public static final int GTK_STATE_FLAG_INSENSITIVE = 1 << 3;
	public static final int GTK_STATE_FLAG_INCONSISTENT = 1 << 4;
	public static final int GTK_STATE_FLAG_FOCUSED = 1 << 5;
	public static final int GTK_STATE_FLAG_BACKDROP  = 1 << 6;
	public static final int GTK_STATE_FLAG_LINK = 1 << 9;
	public static final int GTK_TEXT_DIR_LTR = 1;
	public static final int GTK_TEXT_DIR_RTL = 2;
	public static final int GTK_TEXT_WINDOW_TEXT = 2;
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

	/** Classes */
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
	public static final byte[] gtk_theme_name = OS.ascii("gtk-theme-name");
	public static final byte[] gtk_im_module = OS.ascii("gtk-im-module");

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
	public static final byte[] GTK_NAMED_ICON_PAN_DOWN = OS.ascii ("pan-down-symbolic");
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
	public static final native long GTK_WIDGET_GET_CLASS(long widget);
	/** @method flags=const */
	public static final native long GTK_TYPE_TEXT_VIEW_ACCESSIBLE ();
	public static final native boolean GTK_IS_ACCEL_LABEL(long obj);
	public static final native boolean GTK_IS_BUTTON(long obj);
	public static final native boolean GTK_IS_LABEL(long obj);
	public static final native boolean GTK_IS_IM_CONTEXT(long obj);
	public static final native boolean GTK_IS_SCROLLED_WINDOW(long obj);
	public static final native boolean GTK_IS_WINDOW(long obj);
	public static final native boolean GTK_IS_CELL_RENDERER_PIXBUF(long obj);
	public static final native boolean GTK_IS_CELL_RENDERER_TEXT(long obj);
	public static final native boolean GTK_IS_CELL_RENDERER_TOGGLE(long obj);
	public static final native boolean GTK_IS_CONTAINER(long obj);
	public static final native boolean GTK_IS_MENU_ITEM(long obj);
	public static final native boolean GTK_IS_PLUG(long obj);
	/** @method flags=const */
	public static final native long GTK_TYPE_CELL_RENDERER_TEXT();
	/** @method flags=const */
	public static final native long GTK_TYPE_CELL_RENDERER_PIXBUF();
	/** @method flags=const */
	public static final native long GTK_TYPE_CELL_RENDERER_TOGGLE();
	/** @method flags=const */
	public static final native long GTK_TYPE_IM_MULTICONTEXT();
	/** @method flags=const */
	public static final native long GTK_TYPE_MENU();
	/** @method flags=const */
	public static final native long GTK_TYPE_WIDGET();
	/** @method flags=const */
	public static final native long GTK_TYPE_WINDOW();

	// See os_custom.h
	// Dynamically get's the function pointer to gtk_false(). Gtk2/Gtk3.
	public static final native long GET_FUNCTION_POINTER_gtk_false();

	/** @param widget cast=(GtkWidget *) */
	public static final native boolean gtk_widget_has_default(long widget);

	/** @param widget cast=(GtkWidget *) */
	public static final native boolean gtk_widget_get_sensitive(long widget);

	/** @param widget cast=(GtkWidget *) */
	/* [GTK4 only, if-def'd in os.h] */
	public static final native long gtk_widget_get_first_child(long widget);

	/** @param widget cast=(GtkWidget *) */
	/* [GTK4 only, if-def'd in os.h] */
	public static final native long gtk_widget_get_next_sibling(long widget);

	/**
	 * @param widget cast=(GtkWidget *)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native long gtk_widget_get_screen(long widget);

	/**
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native int gtk_widget_get_scale_factor(long widget);

	/** @param widget cast=(GtkWidget *) */
	public static final native long gtk_widget_get_name(long widget);

	/** @method flags=dynamic
	 *  @param widget_class cast=(GtkWidgetClass *)
	 */
	public static final native long gtk_widget_class_get_css_name(long widget_class);

	public static final native long gtk_button_new();

	/**
	 * @method flags=dynamic
	 * @param button cast=(GtkButton *)
	 * @param image cast=(GtkWidget *)
	 */
	/* [GTK3 only] */
	public static final native void gtk_button_set_image(long button, long image);

	/**
	 * @method flags=dynamic
	 * @param accel_label cast=(GtkAccelLabel *)
	 * @param accel_key cast=(guint)
	 * @param accel_mods cast=(GdkModifierType)
	 */
	public static final native void gtk_accel_label_set_accel(long accel_label, int accel_key, int accel_mods);
	public static final native int gtk_accelerator_get_default_mod_mask();
	/**
	 * @param accelerator cast=(const gchar *)
	 * @param accelerator_key cast=(guint *)
	 * @param accelerator_mods cast=(GdkModifierType *)
	 */
	public static final native void gtk_accelerator_parse(long accelerator, int [] accelerator_key, int [] accelerator_mods);
	public static final native long gtk_accel_group_new();
	/**
	 * @param accel_label cast=(GtkAccelLabel *)
	 * @param accel_widget cast=(GtkWidget *)
	 */
	public static final native void gtk_accel_label_set_accel_widget(long accel_label, long accel_widget);
	/** @param label cast=(const gchar *) */
	public static final native long gtk_accel_label_new(byte[] label);
	/**
	 * @param accessible cast=(GtkAccessible *)
	 */
	public static final native long gtk_accessible_get_widget(long accessible);
	/**
	 * @param adjustment cast=(GtkAdjustment *)
	 */
	public static final native void gtk_adjustment_configure(long adjustment, double value, double lower, double upper, double step_increment, double page_increment, double page_size);
	/**
	 * @param value cast=(gdouble)
	 * @param lower cast=(gdouble)
	 * @param upper cast=(gdouble)
	 * @param step_increment cast=(gdouble)
	 * @param page_increment cast=(gdouble)
	 */
	public static final native long gtk_adjustment_new(double value, double lower, double upper, double step_increment, double page_increment, double page_size);
	/**
	 * @param adjustment cast=(GtkAdjustment *)
	 */
	public static final native double gtk_adjustment_get_lower(long adjustment);
	/**
	 * @param adjustment cast=(GtkAdjustment *)
	 */
	public static final native double gtk_adjustment_get_page_increment(long adjustment);
	/**
	 * @param adjustment cast=(GtkAdjustment *)
	 */
	public static final native double gtk_adjustment_get_page_size(long adjustment);
	/**
	 * @param adjustment cast=(GtkAdjustment *)
	 */
	public static final native double gtk_adjustment_get_step_increment(long adjustment);
	/**
	 * @param adjustment cast=(GtkAdjustment *)
	 */
	public static final native double gtk_adjustment_get_upper(long adjustment);
	/**
	 * @param adjustment cast=(GtkAdjustment *)
	 */
	public static final native double gtk_adjustment_get_value(long adjustment);
	/**
	 * @param adjustment cast=(GtkAdjustment *)
	 * @param value cast=(gdouble)
	 */
	public static final native void gtk_adjustment_set_value(long adjustment, double value);
	/**
	 * @param adjustment cast=(GtkAdjustment *)
	 * @param value cast=(gdouble)
	 */
	public static final native void gtk_adjustment_set_step_increment(long adjustment, double value);
	/**
	 * @param adjustment cast=(GtkAdjustment *)
	 * @param value cast=(gdouble)
	 */
	public static final native void gtk_adjustment_set_page_increment(long adjustment, double value);
	/** @param bin cast=(GtkBin *) */
	public static final native long gtk_bin_get_child(long bin);
	/** @param border cast=(GtkBorder *) */
	public static final native void gtk_border_free(long border);
	/** @param box cast=(GtkBox *) */
	public static final native void gtk_box_set_spacing(long box, int spacing);
	/**
	 * @method flags=dynamic
	 * @param box cast=(GtkBox *)
	 * @param child cast=(GtkWidget *)
	 */
	/* [GTK3 only] */
	public static final native void gtk_box_set_child_packing(long box, long child, boolean expand, boolean fill, int padding, int pack_type);
	/**
	 * @method flags=dynamic
	 * @param box cast=(GtkBox *)
	 * @param child cast=(GtkWidget *)
	 */
	/* [GTK4 only] */
	public static final native void gtk_box_set_child_packing(long box, long child, int pack_type);
	public static final native long gtk_calendar_new();
	/**
	 * @param calendar cast=(GtkCalendar *)
	 * @param month cast=(guint)
	 * @param year cast=(guint)
	 */
	public static final native void /*long*/ gtk_calendar_select_month(long calendar, int month, int year);
	/**
	 * @param calendar cast=(GtkCalendar *)
	 * @param day cast=(guint)
	 */
	public static final native void gtk_calendar_select_day(long calendar, int day);
	/**
	 * @param calendar cast=(GtkCalendar *)
	 * @param day cast=(guint)
	 */
	public static final native void gtk_calendar_mark_day(long calendar, int day);
	/**
	 * @param calendar cast=(GtkCalendar *)
	 */
	public static final native void gtk_calendar_clear_marks(long calendar);
	/**
	 * @param calendar cast=(GtkCalendar *)
	 * @param flags cast=(GtkCalendarDisplayOptions)
	 */
	public static final native void gtk_calendar_set_display_options(long calendar, int flags);
	/**
	 * @param calendar cast=(GtkCalendar *)
	 * @param year cast=(guint *)
	 * @param month cast=(guint *)
	 * @param day cast=(guint *)
	 */
	public static final native void gtk_calendar_get_date(long calendar, int[] year, int[] month, int[] day);
	/** @param cell_layout cast=(GtkCellLayout *) */
	public static final native void gtk_cell_layout_clear(long cell_layout);
	/** @param cell_layout cast=(GtkCellLayout *) */
	public static final native long gtk_cell_layout_get_cells(long cell_layout);
	/**
	 * @param cell_layout cast=(GtkCellLayout *)
	 * @param cell cast=(GtkCellRenderer *)
	 * @param sentinel cast=(const gchar *),flags=sentinel
	 */
	public static final native void gtk_cell_layout_set_attributes(long cell_layout, long cell, byte[] attribute, int column, long sentinel);
	/**
	 * @param cell_layout cast=(GtkCellLayout *)
	 * @param cell cast=(GtkCellRenderer *)
	 */
	public static final native void gtk_cell_layout_pack_start(long cell_layout, long cell, boolean expand);
	/**
	 * @param cell cast=(GtkCellRenderer *)
	 * @param widget cast=(GtkWidget *)
	 * @param minimum_size cast=(GtkRequisition *)
	 * @param natural_size cast=(GtkRequisition *)
	 */
	public static final native void gtk_cell_renderer_get_preferred_size(long cell, long widget, GtkRequisition minimum_size, GtkRequisition natural_size);
	/**
	 * @param cell cast=(GtkCellRenderer *)
	 * @param xpad cast=(gint *)
	 * @param ypad cast=(gint *)
	 */
	public static final native void gtk_cell_renderer_get_padding(long cell, int [] xpad, int [] ypad);
	/**
	 * @param cell cast=(GtkCellRenderer *)
	 * @param widget cast=(GtkWidget *)
	 * @param width cast=(gint)
	 * @param minimum_height cast=(gint *)
	 * @param natural_height cast=(gint *)
	 */
	public static final native void gtk_cell_renderer_get_preferred_height_for_width(long cell, long widget, int width, int[] minimum_height, int[] natural_height);
	/**
	 * @param cell cast=(GtkCellRenderer *)
	 * @param width cast=(gint)
	 * @param height cast=(gint)
	 */
	public static final native void gtk_cell_renderer_set_fixed_size(long cell, int width, int height);
	/**
	 * @param cell cast=(GtkCellRenderer *)
	 * @param width cast=(gint *)
	 * @param height cast=(gint *)
	 */
	public static final native void gtk_cell_renderer_get_fixed_size(long cell, int[] width, int[] height);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param minimum_size cast=(GtkRequisition *)
	 * @param natural_size cast=(GtkRequisition *)
	 */
	public static final native void gtk_widget_get_preferred_size(long widget, GtkRequisition minimum_size, GtkRequisition natural_size);
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 * @param width cast=(gint)
	 * @param minimum_size cast=(gint *)
	 * @param natural_size cast=(gint *)
	 */
	/* [GTK3 only] */
	public static final native void gtk_widget_get_preferred_height_for_width(long widget, int width, int[] minimum_size, int[] natural_size);
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 * @param minimum_size cast=(gint *)
	 * @param natural_size cast=(gint *)
	 */
	/* [GTK3 only] */
	public static final native void gtk_widget_get_preferred_height(long widget, int[] minimum_size, int[] natural_size);
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 * @param height cast=(gint)
	 * @param minimum_size cast=(gint *)
	 * @param natural_size cast=(gint *)
	 */
	/* [GTK3 only] */
	public static final native void gtk_widget_get_preferred_width_for_height(long widget, int height, int[] minimum_size, int[] natural_size);
	public static final native long gtk_cell_renderer_pixbuf_new();
	public static final native long gtk_cell_renderer_text_new();
	public static final native long gtk_cell_renderer_toggle_new();
	/**
	 * @param cell_view cast=(GtkCellView *)
	 * @param fit_model cast=(gboolean)
	 */
	public static final native void gtk_cell_view_set_fit_model(long cell_view, boolean fit_model);
	public static final native long gtk_check_button_new();
	/** @param check_menu_item cast=(GtkCheckMenuItem *) */
	public static final native boolean gtk_check_menu_item_get_active(long check_menu_item);
	public static final native long gtk_check_menu_item_new();
	/**
	 * @param wid cast=(GtkCheckMenuItem *)
	 * @param active cast=(gboolean)
	 */
	public static final native void gtk_check_menu_item_set_active(long wid, boolean active);
	public static final native long gtk_check_version(int required_major, int required_minor, int required_micro);
	/** @param clipboard cast=(GtkClipboard *) */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gtk_clipboard_clear(long clipboard);
	/** @param selection cast=(GdkAtom) */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native long gtk_clipboard_get(long selection);
	/**
	 * @param clipboard cast=(GtkClipboard *)
	 * @param target cast=(const GtkTargetEntry *)
	 * @param n_targets cast=(guint)
	 * @param get_func cast=(GtkClipboardGetFunc)
	 * @param clear_func cast=(GtkClipboardClearFunc)
	 * @param user_data cast=(GObject *)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native boolean gtk_clipboard_set_with_owner(long clipboard, long target, int n_targets, long get_func, long clear_func, long user_data);
	/**
	 * @param clipboard cast=(GtkClipboard *)
	 * @param targets cast=(const GtkTargetEntry *)
	 * @param n_targets cast=(gint)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gtk_clipboard_set_can_store(long clipboard, long targets, int n_targets);
	/**
	 * @param clipboard cast=(GtkClipboard *)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gtk_clipboard_store(long clipboard);
	/**
	 * @param clipboard cast=(GtkClipboard *)
	 * @param target cast=(GdkAtom)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native long gtk_clipboard_wait_for_contents(long clipboard, long target);
	/**
	 * @param chooser cast=(GtkColorChooser *)
	 * @param orientation cast=(GtkOrientation)
	 * @param colors_per_line cast=(gint)
	 * @param n_colors cast=(gint)
	 * @param colors cast=(GdkRGBA *)
	 */
	public static final native void gtk_color_chooser_add_palette(long chooser, int orientation, int colors_per_line, int n_colors, long colors);
	/**
	 * @param title cast=(const gchar *)
	 * @param parent cast=(GtkWindow *)
	 */
	public static final native long gtk_color_chooser_dialog_new(byte[] title, long parent);
	/**
	 * @param chooser cast=(GtkColorChooser *)
	 * @param use_alpha cast=(gboolean)
	 */
	public static final native void gtk_color_chooser_set_use_alpha(long chooser, boolean use_alpha);
	/**
	 * @param chooser cast=(GtkColorChooser *)
	 */
	public static final native boolean gtk_color_chooser_get_use_alpha(long chooser);
	/**
	 * @param chooser cast=(GtkColorChooser *)
	 * @param color cast=(GdkRGBA *)
	 */
	public static final native void gtk_color_chooser_set_rgba(long chooser, GdkRGBA color);
	/**
	 * @param chooser cast=(GtkColorChooser *)
	 * @param color cast=(GdkRGBA *)
	 */
	public static final native void gtk_color_chooser_get_rgba(long chooser, GdkRGBA color);
	public static final native long gtk_combo_box_text_new();
	public static final native long gtk_combo_box_text_new_with_entry();
	/**
	 * @param combo_box cast=(GtkComboBoxText *)
	 * @param position cast=(gint)
	 * @param id cast=(const gchar *)
	 * @param text cast=(const gchar *)
	 */
	/* Do not call directly, instead use Combo.gtk_combo_box_insert(..) */
	public static final native void gtk_combo_box_text_insert(long combo_box, int position, byte[] id, byte[] text);
	/** @param combo_box cast=(GtkComboBoxText *) */
	public static final native void gtk_combo_box_text_remove(long combo_box, int position);
	/**
	 * @param combo_box cast=(GtkComboBoxText *)
	 */
	/* Do not call directly. Call Combo.gtk_combo_box_text_remove_all(..) instead). */
	public static final native void gtk_combo_box_text_remove_all(long combo_box);
	/**
	* @param combo_box cast=(GtkComboBox *)
	*/
	public static final native int gtk_combo_box_get_active(long combo_box);
	/**
	* @param combo_box cast=(GtkComboBox *)
	*/
	public static final native long gtk_combo_box_get_model(long combo_box);
	/**
	* @param combo_box cast=(GtkComboBox *)
	* @param index cast=(gint)
	*/
	public static final native void gtk_combo_box_set_active(long combo_box, int index);
	/**
	 * @param combo_box cast=(GtkComboBox *)
	 * @param width cast=(gint)
	 */
	/*
	 * Do not use directly. Instead use Combo.gtk_combo_box_toggle_wrap(..)
	 */
	public static final native void gtk_combo_box_set_wrap_width(long combo_box, int width);

	/**
	 * @param combo_box cast=(GtkComboBox *)
	 * @return cast=(gint)
	 */
	public static final native int gtk_combo_box_get_wrap_width(long combo_box);
	/**
	* @param combo_box cast=(GtkComboBox *)
	*/
	public static final native void gtk_combo_box_popup(long combo_box);
	/**
	* @param combo_box cast=(GtkComboBox *)
	*/
	public static final native void gtk_combo_box_popdown(long combo_box);
	/**
	 * @param container cast=(GtkContainer *)
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native void gtk_container_add(long container, long widget);
	//Do not confuse this function with gtk_container_foreach(..).
	//Make sure you know what you are doing when using this. Please be attentive to swt_fixed_forall(..)
	// found in os_custom.c, which overrides this function for swtFixed container with custom behaviour.
	/**
	 * @param container cast=(GtkContainer *)
	 * @param callback cast=(GtkCallback)
	 * @param callback_data cast=(gpointer)
	 */
	public static final native void gtk_container_forall(long container, long callback, long callback_data);
	/**
	 * @param container cast=(GtkContainer *)
	 * @param child cast=(GtkWidget *)
	 * @param cairo cast=(cairo_t *)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gtk_container_propagate_draw(long container, long child, long cairo);
	/**
	 * @param container cast=(GtkContainer *)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native int gtk_container_get_border_width(long container);
	/** @param container cast=(GtkContainer *) */
	public static final native long gtk_container_get_children(long container);
	/**
	 * @param container cast=(GtkContainer *)
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native void gtk_container_remove(long container, long widget);
	/**
	 * @param container cast=(GtkContainer *)
	 * @param border_width cast=(guint)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gtk_container_set_border_width(long container, int border_width);
	/**
	 * @param dialog cast=(GtkDialog *)
	 * @param button_text cast=(const gchar *)
	 * @param response_id cast=(gint)
	 */
	public static final native long gtk_dialog_add_button(long dialog, byte[]  button_text, int response_id);
	/** @param dialog cast=(GtkDialog *) */
	public static final native int gtk_dialog_run(long dialog);
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
	/* [GTK3 only, if-def'd in os.h] */
	public static final native long gtk_drag_begin_with_coordinates(long widget, long targets, int actions, int button, long event, int x, int y);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param device cast=(GdkDevice *)
	 * @param targets cast=(GdkContentFormats *)
	 * @param actions cast=(GdkDragAction)
	 * @param x cast=(gint)
	 * @param y cast=(gint)
	 */
	/* [GTK4 only, if-def'd in os.h] */
	public static final native long gtk_drag_begin_with_coordinates(long widget, long device, long targets, int actions, int x, int y);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param start_x cast=(gint)
	 * @param start_y cast=(gint)
	 * @param current_x cast=(gint)
	 * @param current_y cast=(gint)
	 */
	public static final native boolean gtk_drag_check_threshold(long widget, int start_x, int start_y, int current_x, int current_y);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param flags cast=(GtkDestDefaults)
	 * @param targets cast=(const GtkTargetEntry *)
	 * @param n_targets cast=(gint)
	 * @param actions cast=(GdkDragAction)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gtk_drag_dest_set(long widget, int flags, long targets, int n_targets, int actions);
	/** @param widget cast=(GtkWidget *) */
	public static final native void gtk_drag_dest_unset(long widget);
	/**
	 * @param context cast=(GdkDragContext *)
	 * @param success cast=(gboolean)
	 * @param delete cast=(gboolean)
	 * @param time cast=(guint32)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gtk_drag_finish(long context, boolean success, boolean delete, int time);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param context cast=(GdkDragContext *)
	 * @param target cast=(GdkAtom)
	 * @param time cast=(guint32)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gtk_drag_get_data(long widget, long context, long target, int time);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param drop cast=(GdkDrop *)
	 * @param target cast=(GdkAtom)
	 */
	/* [GTK4 only, if-def'd in os.h] */
	public static final native void gtk_drag_get_data(long widget, long drop, long target);
	/**
	 * @param context cast=(GdkDragContext *)
	 * @param surface cast=(cairo_surface_t *)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gtk_drag_set_icon_surface(long context, long surface);
	/** @param editable cast=(GtkEditable *) */
	public static final native void gtk_editable_copy_clipboard(long editable);
	/** @param editable cast=(GtkEditable *) */
	public static final native void gtk_editable_cut_clipboard(long editable);
	/** @param editable cast=(GtkEditable *) */
	public static final native void gtk_editable_delete_selection(long editable);
	/**
	 * @param editable cast=(GtkEditable *)
	 * @param start_pos cast=(gint)
	 * @param end_pos cast=(gint)
	 */
	public static final native void gtk_editable_delete_text(long editable, int start_pos, int end_pos);
	/** @param editable cast=(GtkEditable *) */
	public static final native boolean gtk_editable_get_editable(long editable);
	/** @param editable cast=(GtkEditable *) */
	public static final native int gtk_editable_get_position(long editable);
	/**
	 * @param editable cast=(GtkEditable *)
	 * @param start cast=(gint *)
	 * @param end cast=(gint *)
	 */
	public static final native boolean gtk_editable_get_selection_bounds(long editable, int[] start, int[] end);
	/**
	 * @param editable cast=(GtkEditable *)
	 * @param new_text cast=(gchar *)
	 * @param new_text_length cast=(gint)
	 * @param position cast=(gint *)
	 */
	public static final native void gtk_editable_insert_text(long editable, byte[] new_text, int new_text_length, int[] position);
	/** @param editable cast=(GtkEditable *) */
	public static final native void gtk_editable_paste_clipboard(long editable);
	/**
	 * @param editable cast=(GtkEditable *)
	 * @param start cast=(gint)
	 * @param end cast=(gint)
	 */
	public static final native void gtk_editable_select_region(long editable, int start, int end);
	/**
	 * @param entry cast=(GtkEditable *)
	 * @param editable cast=(gboolean)
	 */
	public static final native void gtk_editable_set_editable(long entry, boolean editable);
	/**
	 * @param editable cast=(GtkEditable *)
	 * @param position cast=(gint)
	 */
	public static final native void gtk_editable_set_position(long editable, int position);
	/**
	 * @param self cast=(GtkEntry *)
	 * @param n_chars cast=(gint)
	 */
	public static final native void gtk_entry_set_width_chars(long self, int n_chars);
	/** @param entry cast=(GtkEntry *) */
	public static final native char gtk_entry_get_invisible_char(long entry);
	/** @param entry cast=(GtkEntry *) */
	public static final native long gtk_entry_get_layout(long entry);
	/**
	 * @param entry cast=(GtkEntry *)
	 * @param x cast=(gint *)
	 * @param y cast=(gint *)
	 */
	public static final native void gtk_entry_get_layout_offsets(long entry, int[] x, int[] y);
	/**
	 * @param entry cast=(GtkEntry *)
	 * @param index cast=(gint)
	 */
	public static final native int gtk_entry_text_index_to_layout_index(long entry, int index);
	/**
	 * @param entry cast=(GtkEntry *)
	 * @param icon_pos cast=(gint)
	 * @param icon_area cast=(GdkRectangle *),flags=no_in
	 */
	public static final native void gtk_entry_get_icon_area(long entry, int icon_pos, GdkRectangle icon_area);
	/** @param entry cast=(GtkEntry *) */
	public static final native int gtk_entry_get_max_length(long entry);
	/** @param entry cast=(GtkEntry *) */
	public static final native long gtk_entry_get_text(long entry);
	/** @param entry cast=(GtkEntry *) */
	public static final native boolean gtk_entry_get_visibility(long entry);
	public static final native long gtk_entry_new();
	/**
	 * @param entry cast=(GtkEntry *)
	 * @param xalign cast=(gfloat)
	 */
	public static final native void gtk_entry_set_alignment(long entry, float xalign);
	/**
	 * @param entry cast=(GtkEntry *)
	 * @param setting cast=(gboolean)
	 */
	public static final native void gtk_entry_set_has_frame(long entry, boolean setting);
	/**
	 * @param entry cast=(GtkEntry *)
	 * @param iconPos cast=(gint)
	 * @param stock cast=(const gchar *)
	 */
	public static final native void gtk_entry_set_icon_from_icon_name(long entry, int iconPos, byte[] stock);
	/**
	 * @param entry cast=(GtkEntry *)
	 * @param icon_pos cast=(GtkEntryIconPosition)
	 * @param activatable cast=(gboolean)
	 */
	public static final native void gtk_entry_set_icon_activatable(long entry, int icon_pos, boolean activatable);
	/**
	 * @param entry cast=(GtkEntry *)
	 * @param icon_pos cast=(GtkEntryIconPosition)
	 * @param sensitive cast=(gboolean)
	 */
	public static final native void gtk_entry_set_icon_sensitive(long entry, int icon_pos, boolean sensitive);
	/**
	 * @param entry cast=(GtkEntry *)
	 * @param ch cast=(gint)
	 */
	public static final native void gtk_entry_set_invisible_char(long entry, char ch);
	/**
	 * @param entry cast=(GtkEntry *)
	 * @param max cast=(gint)
	 */
	public static final native void gtk_entry_set_max_length(long entry, int max);
	/**
	 * @param entry cast=(GtkEntry *)
	 * @param tabs cast=(PangoTabArray *)
	 */
	public static final native void gtk_entry_set_tabs(long entry, long tabs);
	/**
	 * @param entry cast=(GtkEntry *)
	 * @param text cast=(const gchar *)
	 */
	public static final native void gtk_entry_set_text(long entry, byte[] text);
	/**
	 * @param entry cast=(GtkEntry *)
	 * @param text cast=(const gchar *)
	 */
	public static final native void gtk_entry_set_placeholder_text(long entry, byte[] text);
	/**
	 * @param entry cast=(GtkEntry *)
	 * @param visible cast=(gboolean)
	 */
	public static final native void gtk_entry_set_visibility(long entry, boolean visible);
	/** @param expander cast=(GtkExpander *) */
	public static final native boolean gtk_expander_get_expanded(long expander);
	/**
	 * @param label cast=(const gchar *)
	 */
	public static final native long gtk_expander_new(byte[] label);
	/** @param expander cast=(GtkExpander *) */
	public static final native void gtk_expander_set_expanded(long expander, boolean expanded);
	/**
	 * @param expander cast=(GtkExpander *)
	 * @param label_widget cast=(GtkWidget *)
	 */
	public static final native void gtk_expander_set_label_widget(long expander, long label_widget);
	/**
	 * @param chooser cast=(GtkFileChooser *)
	 * @param filter cast=(GtkFileFilter *)
	 */
	public static final native void gtk_file_chooser_add_filter(long chooser, long filter);
	/** @param chooser cast=(GtkFileChooser *) */
	public static final native long gtk_file_chooser_get_filename(long chooser);
	/**  @param chooser cast=(GtkFileChooser *) */
	public static final native long gtk_file_chooser_get_filenames(long chooser);
	/** @param chooser cast=(GtkFileChooser *) */
	public static final native long gtk_file_chooser_get_uri(long chooser);
	/** @param chooser cast=(GtkFileChooser *) */
	public static final native long gtk_file_chooser_get_uris(long chooser);
	/** @param chooser cast=(GtkFileChooser *) */
	public static final native long gtk_file_chooser_get_filter(long chooser);
	/**
	 * @param chooser cast=(GtkFileChooser *)
	 * @param filename cast=(const gchar *)
	 */
	public static final native void gtk_file_chooser_set_current_folder(long chooser, long filename);
	/**
	 * @param chooser cast=(GtkFileChooser *)
	 * @param uri cast=(const gchar *)
	 */
	public static final native void gtk_file_chooser_set_current_folder_uri(long chooser, byte [] uri);
	/**
	 * @param chooser cast=(GtkFileChooser *)
	 * @param name cast=(const gchar *)
	 */
	public static final native void gtk_file_chooser_set_current_name(long chooser, byte[] name);
	/**
	 * @param chooser cast=(GtkFileChooser *)
	 * @param local_only cast=(gboolean)
	 */
	public static final native void gtk_file_chooser_set_local_only(long chooser, boolean local_only);
	/**
	 * @param chooser cast=(GtkFileChooser *)
	 * @param do_overwrite_confirmation cast=(gboolean)
	 */
	public static final native void gtk_file_chooser_set_do_overwrite_confirmation(long chooser, boolean do_overwrite_confirmation);
	/**
	 * @param chooser cast=(GtkFileChooser *)
	 * @param extra_widget cast=(GtkWidget *)
	 */
	public static final native void gtk_file_chooser_set_extra_widget(long chooser, long extra_widget);
	/**
	 * @param chooser cast=(GtkFileChooser *)
	 * @param name cast=(const gchar *)
	 */
	public static final native void gtk_file_chooser_set_filename(long chooser, long name);
	/**
	 * @param chooser cast=(GtkFileChooser *)
	 * @param filter cast=(GtkFileFilter *)
	 */
	public static final native void gtk_file_chooser_set_filter(long chooser, long filter);
	/**
	 * @param chooser cast=(GtkFileChooser *)
	 * @param uri cast=(const char *)
	 */
	public static final native void gtk_file_chooser_set_uri(long chooser, byte [] uri);
	/**
	 * @param chooser cast=(GtkFileChooser *)
	 * @param select_multiple cast=(gboolean)
	 */
	public static final native void gtk_file_chooser_set_select_multiple(long chooser, boolean select_multiple);
	/* [GTK4 only, if-def'd in os.h] */
	public static final native long gtk_gesture_multi_press_new();
	/* [GTK4 only, if-def'd in os.h] */
	public static final native long gtk_event_controller_key_new();
	/* [GTK4 only, if-def'd in os.h] */
	public static final native long gtk_event_controller_motion_new();
	/* [GTK4 only, if-def'd in os.h] */
	public static final native long gtk_event_controller_scroll_new(int flag);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param controller cast=(GtkEventController *)
	 */
	/* [GTK4 only, if-def'd in os.h] */
	public static final native void gtk_widget_add_controller(long widget, long controller);
	/**
	 * @method flags=dynamic
	 */
	public static final native void gtk_event_controller_set_propagation_phase(long controller, int phase);
	/**
	 * @method flags=dynamic
	 * @param title cast=(const gchar *),flags=no_out
	 * @param parent cast=(GtkWindow *)
	 * @param accept_label cast=(const gchar *),flags=no_out
	 * @param cancel_label cast=(const gchar *),flags=no_out
	 */
	public static final native long gtk_file_chooser_native_new(byte[] title, long parent, int action, byte[] accept_label, byte[] cancel_label);
	/**
	 * @method flags=dynamic
	 */
	public static final native void gtk_event_controller_handle_event(long gesture, long event);
	/**
	 * @method flags=dynamic
	 */
	public static final native long gtk_event_controller_get_widget(long controller);
	/**
	 * @param filter cast=(GtkFileFilter *)
	 * @param pattern cast=(const gchar *)
	 */
	public static final native void gtk_file_filter_add_pattern(long filter, byte[] pattern);
	public static final native long gtk_file_filter_new();
	/** @param filter cast=(GtkFileFilter *) */
	public static final native long gtk_file_filter_get_name(long filter);
	/**
	 * @param filter cast=(GtkFileFilter *)
	 * @param name cast=(const gchar *)
	 */
	public static final native void gtk_file_filter_set_name(long filter, byte[] name);
	/**
	 * @method flags=dynamic
	 */
	public static final native boolean gtk_gesture_drag_get_start_point(long gesture, double[] x, double [] y);
	/**
	 * @method flags=dynamic
	 */
	public static final native boolean gtk_gesture_is_recognized(long gesture);
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native long gtk_gesture_drag_new(long widget);
	/**
	 * @method flags=dynamic
	 */
	public static final native long gtk_gesture_get_last_updated_sequence(long gesture);
	/**
	 * @method flags=dynamic
	 */
	public static final native boolean gtk_gesture_get_point(long gesture, long sequence, double[] x, double [] y);
	/**
	 * @method flags=dynamic
	 */
	public static final native void gtk_gesture_single_set_button(long gesture, int button);
	/**
	 * @method flags=dynamic
	 */
	public static final native boolean gtk_gesture_swipe_get_velocity(long gesture, double [] velocity_x, double[] velocity_y);
	/**
	 * @method flags=dynamic
	 */
	public static final native void gtk_gesture_drag_get_offset(long gesture, double[] x, double[] y);
	/**
	 * @method flags=dynamic
	 */

	public static final native double gtk_gesture_rotate_get_angle_delta(long gesture);
	/**
	 * @method flags=dynamic
	 */

	public static final native long gtk_gesture_rotate_new(long widget);
	/**
	 * @method flags=dynamic
	 */
	public static final native long gtk_gesture_zoom_new(long widget);
	/**
	 * @method flags=dynamic
	 */
	public static final native double gtk_gesture_zoom_get_scale_delta(long gesture);
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native void gtk_widget_set_clip(long widget, GtkAllocation allocation);
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native void gtk_widget_get_clip(long widget, GtkAllocation allocation);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param has_window cast=(gboolean)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gtk_widget_set_has_window(long widget, boolean has_window);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param has_surface cast=(gboolean)
	 */
	/* [GTK4 only, if-def'd in os.h] */
	public static final native void gtk_widget_set_has_surface(long widget, boolean has_surface);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param expand cast=(gboolean)
	 */
	public static final native void gtk_widget_set_hexpand(long widget, boolean expand);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param expand cast=(gboolean)
	 */
	public static final native void gtk_widget_set_vexpand(long widget, boolean expand);
	//since Gtk 3.16. For pre-gtk3.16, use gtk_misc_set_alignment(..)
	/**
	 * @method flags=dynamic
	 * @param label cast=(GtkLabel *)
	 * @param xalign cast=(gfloat)
	 *
	 */
	public static final native void gtk_label_set_xalign(long label, float xalign);
	//since Gtk 3.16. For pre-gtk3.16, use gtk_misc_set_alignment(..)
	/**
	* @method flags=dynamic
	* @param label cast=(GtkLabel *)
	* @param yalign cast=(gfloat)
	*
	*/
	public static final native void gtk_label_set_yalign(long label, float yalign);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param gtk_align cast=(GtkAlign)
	 */
	public static final native void gtk_widget_set_halign(long widget, int gtk_align);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param gtk_align cast=(GtkAlign)
	 */
	public static final native void gtk_widget_set_valign(long widget, int gtk_align);
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 * @param margin cast=(gint)
	 */
	public static final native void gtk_widget_set_margin_start(long widget, int margin);
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 * @param margin cast=(gint)
	 */
	public static final native void gtk_widget_set_margin_end(long widget, int margin);
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 * @param margin cast=(gint)
	 */
	public static final native void gtk_widget_set_margin_top(long widget, int margin);
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 * @param margin cast=(gint)
	 */
	public static final native void gtk_widget_set_margin_bottom(long widget, int margin);
	/**
	 * @param title cast=(const gchar *)
	 * @param parent cast=(GtkWindow *)
	 */
	public static final native long gtk_font_chooser_dialog_new(byte[] title, long parent);
	/**
	 * @param fontchooser cast=(GtkFontChooser *)
	 */
	public static final native long gtk_font_chooser_get_font(long fontchooser);
	/**
	 * @param fsd cast=(GtkFontChooser *)
	 * @param fontname cast=(const gchar *)
	 */
	public static final native void gtk_font_chooser_set_font(long fsd, byte[] fontname);
	/** @param label cast=(const gchar *) */
	public static final native long gtk_frame_new(byte[] label);
	/** @param frame cast=(GtkFrame *) */
	public static final native long gtk_frame_get_label_widget(long frame);
	/**
	 * @param frame cast=(GtkFrame *)
	 * @param label_widget cast=(GtkWidget *)
	 */
	public static final native void gtk_frame_set_label_widget(long frame, long label_widget);
	/**
	 * @param frame cast=(GtkFrame *)
	 * @param type cast=(GtkShadowType)
	 */
	public static final native void gtk_frame_set_shadow_type(long frame, int type);
	public static final native long gtk_get_current_event();
	/** @param state cast=(GdkModifierType*) */
	public static final native boolean gtk_get_current_event_state(int[] state);
	public static final native long gtk_get_default_language();
	/** @param event cast=(GdkEvent *) */
	public static final native long gtk_get_event_widget(long event);
	/** @param widget cast=(GtkWidget *) */
	public static final native void gtk_grab_add(long widget);
	public static final native long gtk_grab_get_current();
	/** @param widget cast=(GtkWidget *) */
	public static final native void gtk_grab_remove(long widget);
	/**
	 * @param h cast=(gdouble)
	 * @param s cast=(gdouble)
	 * @param v cast=(gdouble)
	 * @param r cast=(gdouble *)
	 * @param g cast=(gdouble *)
	 * @param b cast=(gdouble *)
	 */
	public static final native void gtk_hsv_to_rgb(double h, double s, double v, double[] r, double[] g, double[] b);
	/**
	 * @param r cast=(gdouble)
	 * @param g cast=(gdouble)
	 * @param b cast=(gdouble)
	 * @param h cast=(gdouble *)
	 * @param s cast=(gdouble *)
	 * @param v cast=(gdouble *)
	 */
	public static final native void gtk_rgb_to_hsv(double r, double g, double b, double[] h, double[] s, double[] v);
	/**
	 * @param orientation cast=(GtkOrientation)
	 * @param spacing cast=(gint)
	 */
	public static final native long gtk_box_new(int orientation, int spacing);
	/**
	 * @method flags=dynamic
	 * @param box cast=(GtkBox *)
	 * @param widget cast=(GtkWidget *)
	 * @param expand cast=(gboolean)
	 * @param fill cast=(gboolean)
	 * @param padding cast=(guint)
	 */
	/* [GTK3 only] */
	public static final native void gtk_box_pack_end(long box, long widget,
			boolean expand, boolean fill, int padding);
	/**
	 * @method flags=dynamic
	 * @param box cast=(GtkBox *)
	 * @param child cast=(GtkWidget *)
	 */
	/* [GTK4 only] */
	public static final native void gtk_box_pack_end(long box, long child);
	/**
	 * @param box cast=(GtkBox *)
	 * @param child cast=(GtkWidget *)
	 * @param position cast=(gint)
	 */
	public static final native void gtk_box_reorder_child(long box, long child, int position);
	/**
	 * @param box cast=(GtkBox *)
	 * @param homogeneous cast=(gboolean)
	 */
	public static final native void gtk_box_set_homogeneous(long box, boolean homogeneous);
	/**
	 * @method flags=dynamic
	 */
	/* [GTK3 only] */
	public static final native long gtk_event_box_new();
	/**
	 *  @param orientation cast=(GtkOrientation)
	 *  @param adjustment cast=(GtkAdjustment *)
	 */
	public static final native long gtk_scale_new(int orientation, long adjustment);
	/**
	 * @param orientation cast=(GtkOrientation)
	 * @param adjustment cast=(GtkAdjustment *)
	 * */
	public static final native long gtk_scrollbar_new(int orientation, long adjustment);
	public static final native long gtk_search_entry_new();
	/**
	 * @param orientation cast=(GtkOrientation)
	 */
	public static final native long gtk_separator_new(int orientation);
	// Get function pointer to gtk_status_icon_position_menu
	// See os_custom.h
	public static final native long gtk_status_icon_position_menu_func();
	/** @return cast=(GtkIconTheme *) */
	public static final native long gtk_icon_theme_get_default();
	/**
	 * @param icon_theme cast=(GtkIconTheme *)
	 * @param icon cast=(GIcon *)
	 * @param size cast=(gint)
	 * @param flags cast=(GtkIconLookupFlags)
	 */
	public static final native long gtk_icon_theme_lookup_by_gicon(long icon_theme, long icon, int size, int flags);
	/**
	 * @param icon_theme cast=(GtkIconTheme *)
	 * @param icon_name cast=(const gchar *)
	 * @param size cast=(gint)
	 * @param flags cast=(GtkIconLookupFlags)
	 * @param error cast=(GError **)
	 */
	public static final native long gtk_icon_theme_load_icon(long icon_theme, byte[] icon_name, int size, int flags, long error);
	/**
	 * @param icon_info cast=(GtkIconInfo *)
	 * @param error cast=(GError **)
	 */
	public static final native long gtk_icon_info_load_icon(long icon_info, long error[]);
	/**
	 * @param context cast=(GtkIMContext *)
	 * @param event cast=(GdkEventKey *)
	 */
	public static final native boolean gtk_im_context_filter_keypress(long context, long event);
	/** @param context cast=(GtkIMContext *) */
	public static final native void gtk_im_context_focus_in(long context);
	/** @param context cast=(GtkIMContext *) */
	public static final native void gtk_im_context_focus_out(long context);
	/**
	 * @param context cast=(GtkIMContext *)
	 * @param str cast=(gchar **)
	 * @param attrs cast=(PangoAttrList **)
	 * @param cursor_pos cast=(gint *)
	 */
	public static final native void gtk_im_context_get_preedit_string(long context, long [] str, long [] attrs, int[] cursor_pos);
	public static final native long gtk_im_context_get_type();
	/** @param context cast=(GtkIMContext *) */
	public static final native void gtk_im_context_reset(long context);
	/**
	 * @param context cast=(GtkIMContext *)
	 * @param window cast=(GdkWindow *)
	 */
	public static final native void gtk_im_context_set_client_window(long context, long window);
	/**
	 * @param context cast=(GtkIMContext *)
	 * @param area cast=(GdkRectangle *),flags=no_out
	 */
	public static final native void gtk_im_context_set_cursor_location(long context, GdkRectangle area);
	public static final native long gtk_im_multicontext_new();
	public static final native long gtk_image_new();
	/**
	 * @param image cast=(GtkImage *)
	 * @param pixel_size cast=(gint)
	 */
	public static final native void gtk_image_set_pixel_size(long image, int pixel_size);
	/** @param pixbuf cast=(GdkPixbuf *) */
	public static final native long gtk_image_new_from_pixbuf(long pixbuf);
	/**
	 * @param image cast=(GtkImage *)
	 * @param pixbuf cast=(GdkPixbuf *)
	 */
	public static final native void gtk_image_set_from_pixbuf(long image, long pixbuf);
	/**
	 * @method flags=dynamic
	 * @param image cast=(GtkImage *)
	 * @param gicon cast=(GIcon *)
	 * @param size cast=(GtkIconSize)
	 */
	/* [GTK3 only] */
	public static final native void gtk_image_set_from_gicon(long image, long gicon, int size);
	/**
	 * @method flags=dynamic
	 * @param image cast=(GtkImage *)
	 * @param gicon cast=(GIcon *)
	 */
	/* [GTK4 only] */
	public static final native void gtk_image_set_from_gicon(long image, long gicon);
	/**
	 * @method flags=dynamic
	 * @param icon_name cast=(const gchar *)
	 * @param size cast=(GtkIconSize)
	 */
	/* [GTK3 only] */
	public static final native long gtk_image_new_from_icon_name(byte[] icon_name, int size);
	/**
	 * @method flags=dynamic
	 * @param icon_name cast=(const gchar *)
	 */
	/* [GTK4 only] */
	public static final native long gtk_image_new_from_icon_name(byte[] icon_name);
	/**
	 * @method flags=dynamic
	 * @param image cast=(GtkImage *)
	 * @param icon_name cast=(const gchar *)
	 * @param size cast=(GtkIconSize)
	 */
	/* [GTK3 only] */
	public static final native void gtk_image_set_from_icon_name(long image, byte[] icon_name, int size);
	/**
	 * @method flags=dynamic
	 * @param image cast=(GtkImage *)
	 * @param icon_name cast=(const gchar *)
	 */
	/* [GTK4 only] */
	public static final native void gtk_image_set_from_icon_name(long image, byte[] icon_name);
	/**
	 * @method flags=dynamic
	 * @param argc cast=(int *)
	 * @param argv cast=(char ***)
	 */
	/* [GTK3 only] */
	public static final native boolean gtk_init_check(long [] argc, long [] argv);
	/**
	 * @method flags=dynamic
	 */
	/* [GTK4 only] */
	public static final native boolean gtk_init_check();
	/** @param label cast=(GtkLabel *) */
	public static final native long gtk_label_get_layout(long label);
	public static final native long gtk_label_get_type();
	/** @param label cast=(GtkLabel *) */
	public static final native int gtk_label_get_mnemonic_keyval(long label);
	/** @param label cast=(const gchar *) */
	public static final native long gtk_label_new(byte[] label);
	/** @param str cast=(const gchar *) */
	public static final native long gtk_label_new_with_mnemonic(byte[] str);
	/**
	 * @param label cast=(GtkLabel *)
	 * @param attrs cast=(PangoAttrList *)
	 */
	public static final native void gtk_label_set_attributes(long label, long attrs);
	/**
	 * @param label cast=(GtkLabel *)
	 * @param jtype cast=(GtkJustification)
	 */
	public static final native void gtk_label_set_justify(long label, int jtype);
	/**
	 * @param label cast=(GtkLabel *)
	 * @param wrap cast=(gboolean)
	 */
	public static final native void gtk_label_set_line_wrap(long label, boolean wrap);
	/**
	 * @param label cast=(GtkLabel *)
	 * @param wrap_mode cast=(PangoWrapMode)
	 */
	public static final native void gtk_label_set_line_wrap_mode(long label, int wrap_mode);
	/**
	 * @param label cast=(GtkLabel *)
	 * @param str cast=(const gchar *)
	 */
	public static final native void gtk_label_set_text(long label, long str);
	/**
	 * @param label cast=(GtkLabel *)
	 * @param str cast=(const gchar *)
	 */
	public static final native void gtk_label_set_text(long label, byte[] str);
	/**
	 * @param label cast=(GtkLabel *)
	 * @param str cast=(const gchar *)
	 */
	public static final native void gtk_label_set_text_with_mnemonic(long label, byte[] str);
	/**
	 * @param list_store cast=(GtkListStore *)
	 * @param iter cast=(GtkTreeIter *)
	 */
	public static final native void gtk_list_store_append(long list_store, long iter);
	/** @param store cast=(GtkListStore *) */
	public static final native void gtk_list_store_clear(long store);
	/**
	 * @param list_store cast=(GtkListStore *)
	 * @param iter cast=(GtkTreeIter *)
	 * @param position cast=(gint)
	 */
	public static final native void gtk_list_store_insert(long list_store, long iter, int position);
	/**
	 * @param numColumns cast=(gint)
	 * @param types cast=(GType *)
	 */
	public static final native long gtk_list_store_newv(int numColumns, long [] types);
	/**
	 * @param css_provider cast=(GtkCssProvider *)
	 * @param data cast=(const gchar *)
	 * @param length cast=(gssize)
	 * @param error cast=(GError **)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native boolean gtk_css_provider_load_from_data(long css_provider, byte[] data, long length, long error[]);
	/**
	 * @param css_provider cast=(GtkCssProvider *)
	 * @param data cast=(const gchar *)
	 * @param length cast=(gssize)
	 */
	/* [GTK4 only, if-def'd in os.h] */
	public static final native void gtk_css_provider_load_from_data(long css_provider, byte[] data, long length);
	public static final native long gtk_css_provider_new();
	/**
	 * @param provider cast=(GtkCssProvider *)
	 */
	public static final native long gtk_css_provider_to_string(long provider);
	/**
	 * @param screen cast=(GdkScreen *)
	 * @param provider cast=(GtkStyleProvider *)
	 * @param priority cast=(guint)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gtk_style_context_add_provider_for_screen(long screen, long provider, int priority);
	/**
	 * @method flags=dynamic
	 * @param display cast=(GdkDisplay *)
	 * @param provider cast=(GtkStyleProvider *)
	 * @param priority cast=(guint)
	 */
	/* [GTK4 only] */
	public static final native void gtk_style_context_add_provider_for_display(long display, long provider, int priority);
	/**
	 * @param context cast=(GtkStyleContext *)
	 * @param provider cast=(GtkStyleProvider *)
	 * @param priority cast=(guint)
	 */
	public static final native void gtk_style_context_add_provider(long context, long provider, int priority);
	/**
	 * @param list_store cast=(GtkListStore *)
	 * @param iter cast=(GtkTreeIter *)
	 */
	public static final native void gtk_list_store_remove(long list_store, long iter);
	/**
	 * @param store cast=(GtkListStore *)
	 * @param iter cast=(GtkTreeIter *)
	 */
	public static final native void gtk_list_store_set(long store, long iter, int column, byte[] value, int terminator);
	/**
	 * @param store cast=(GtkListStore *)
	 * @param iter cast=(GtkTreeIter *)
	 */
	public static final native void gtk_list_store_set(long store, long iter, int column, int value, int terminator);
	/**
	 * @param store cast=(GtkListStore *)
	 * @param iter cast=(GtkTreeIter *)
	 */
	public static final native void gtk_list_store_set(long store, long iter, int column, long value, int terminator);
	/**
	 * @param store cast=(GtkListStore *)
	 * @param iter cast=(GtkTreeIter *)
	 * @param value flags=no_out
	 */
	public static final native void gtk_list_store_set(long store, long iter, int column, GdkRGBA value, int terminator);
	/**
	 * @param store cast=(GtkListStore *)
	 * @param iter cast=(GtkTreeIter *)
	 */
	public static final native void gtk_list_store_set(long store, long iter, int column, boolean value, int terminator);
	public static final native int gtk_get_major_version();
	public static final native int gtk_get_minor_version();
	public static final native int gtk_get_micro_version();
	/** @param event cast=(GdkEvent *) */
	public static final native void gtk_main_do_event(long event);
	public static final native long gtk_menu_bar_new();
	/** @param menu_item cast=(GtkMenuItem *) */
	public static final native long gtk_menu_item_get_submenu(long menu_item);
	public static final native long gtk_menu_item_new();
	/**
	 * @param menu_item cast=(GtkMenuItem *)
	 * @param submenu cast=(GtkWidget *)
	 */
	public static final native void gtk_menu_item_set_submenu(long menu_item, long submenu);
	public static final native long gtk_menu_new();
	/** @param menu cast=(GtkMenu *) */
	public static final native void gtk_menu_popdown(long menu);
	/**
	 * @method flags=dynamic
	 */
	/* [GTK2/GTK3; 3.22 deprecated, replaced] */
	public static final native void gtk_menu_popup(long menu, long parent_menu_shell, long parent_menu_item, long func, long data, int button, int activate_time);
	/**
	 *  @method flags=dynamic
	 */
	public static final native void gtk_menu_popup_at_pointer(long menu, long trigger_event);
	/**
	 *  @method flags=dynamic
	 */
	public static final native void gtk_menu_popup_at_rect(long menu, long rect_window, GdkRectangle rect, int rect_anchor, int menu_anchor, long trigger_event);
	/** @param menu_shell cast=(GtkMenuShell *) */
	public static final native void gtk_menu_shell_deactivate(long menu_shell);
	/**
	 * @param menu_shell cast=(GtkMenuShell *)
	 * @param child cast=(GtkWidget *)
	 * @param position cast=(gint)
	 */
	public static final native void gtk_menu_shell_insert(long menu_shell, long child, int position);
	/**
	 * @param menu_shell cast=(GtkMenuShell *)
	 * @param take_focus cast=(gboolean)
	 */
	public static final native void gtk_menu_shell_set_take_focus(long menu_shell, boolean take_focus);
	/**
	 * @param icon_widget cast=(GtkWidget *)
	 * @param label cast=(const gchar *)
	 */
	public static final native long gtk_menu_tool_button_new(long icon_widget, byte[] label);
	/**
	 * @param parent cast=(GtkWindow *)
	 * @param flags cast=(GtkDialogFlags)
	 * @param type cast=(GtkMessageType)
	 * @param buttons cast=(GtkButtonsType)
	 * @param message_format cast=(const gchar *)
	 * @param arg cast=(const gchar *)
	 */
	public static final native long gtk_message_dialog_new(long parent, int flags, int type, int buttons, byte[] message_format, byte[] arg);
	/** @param dialog cast=(GtkNativeDialog *) */
	public static final native int gtk_native_dialog_run(long dialog);
	/** @param notebook cast=(GtkNotebook *) */
	public static final native int gtk_notebook_get_current_page(long notebook);
	/** @param notebook cast=(GtkNotebook *) */
	public static final native boolean gtk_notebook_get_scrollable(long notebook);
	/**
	 * @param notebook cast=(GtkNotebook *)
	 * @param child cast=(GtkWidget *)
	 * @param tab_label cast=(GtkWidget *)
	 * @param position cast=(gint)
	 */
	public static final native void gtk_notebook_insert_page(long notebook, long child, long tab_label, int position);
	public static final native long gtk_notebook_new();
	/** @param notebook cast=(GtkNotebook *) */
	public static final native void gtk_notebook_next_page(long notebook);
	/** @param notebook cast=(GtkNotebook *) */
	public static final native void gtk_notebook_prev_page(long notebook);
	/**
	 * @param notebook cast=(GtkNotebook *)
	 * @param page_num cast=(gint)
	 */
	public static final native void gtk_notebook_remove_page(long notebook, int page_num);
	/**
	 * @param notebook cast=(GtkNotebook *)
	 * @param page_num cast=(gint)
	 */
	public static final native void gtk_notebook_set_current_page(long notebook, int page_num);
	/**
	 * @param notebook cast=(GtkNotebook *)
	 * @param scrollable cast=(gboolean)
	 */
	public static final native void gtk_notebook_set_scrollable(long notebook, boolean scrollable);
	/**
	 * @param notebook cast=(GtkNotebook *)
	 * @param show_tabs cast=(gboolean)
	 */
	public static final native void gtk_notebook_set_show_tabs(long notebook, boolean show_tabs);
	/**
	 * @param notebook cast=(GtkNotebook *)
	 * @param pos cast=(GtkPositionType)
	 */
	public static final native void gtk_notebook_set_tab_pos(long notebook, int pos);
	/**
	 * @param orientable cast=(GtkOrientable *)
	 * @param orientation cast=(GtkOrientation)
	 */
	public static final native void gtk_orientable_set_orientation(long orientable, int orientation);
	public static final native long gtk_page_setup_new();
	/**
	 * @param setup cast=(GtkPageSetup *)
	 */
	public static final native int gtk_page_setup_get_orientation(long setup);
	/**
	 * @param setup cast=(GtkPageSetup *)
	 * @param orientation cast=(GtkPageOrientation)
	 */
	public static final native void gtk_page_setup_set_orientation(long setup, int orientation);
	/**
	 * @param setup cast=(GtkPageSetup *)
	 */
	public static final native long gtk_page_setup_get_paper_size(long setup);
	/**
	 * @param setup cast=(GtkPageSetup *)
	 * @param size cast=(GtkPaperSize *)
	 */
	public static final native void gtk_page_setup_set_paper_size(long setup, long size);
	/**
	 * @param setup cast=(GtkPageSetup *)
	 * @param unit cast=(GtkUnit)
	 */
	public static final native double gtk_page_setup_get_top_margin(long setup, int unit);
	/**
	 * @param setup cast=(GtkPageSetup *)
	 * @param margin cast=(gdouble)
	 * @param unit cast=(GtkUnit)
	 */
	public static final native void gtk_page_setup_set_top_margin(long setup, double margin, int unit);
	/**
	 * @param setup cast=(GtkPageSetup *)
	 * @param unit cast=(GtkUnit)
	 *
	 */
	public static final native double gtk_page_setup_get_bottom_margin(long setup, int unit);
	/**
	 * @param setup cast=(GtkPageSetup *)
	 * @param margin cast=(gdouble)
	 * @param unit cast=(GtkUnit)
	 */
	public static final native void gtk_page_setup_set_bottom_margin(long setup, double margin, int unit);
	/**
	 * @param setup cast=(GtkPageSetup *)
	 * @param unit cast=(GtkUnit)
	 */
	public static final native double gtk_page_setup_get_left_margin(long setup, int unit);
	/**
	 * @param setup cast=(GtkPageSetup *)
	 * @param margin cast=(gdouble)
	 * @param unit cast=(GtkUnit)
	 */
	public static final native void gtk_page_setup_set_left_margin(long setup, double margin, int unit);
	/**
	 * @param setup cast=(GtkPageSetup *)
	 * @param unit cast=(GtkUnit)
	 */
	public static final native double gtk_page_setup_get_right_margin(long setup, int unit);
	/**
	 * @param setup cast=(GtkPageSetup *)
	 * @param margin cast=(gdouble)
	 * @param unit cast=(GtkUnit)
	 */
	public static final native void gtk_page_setup_set_right_margin(long setup, double margin, int unit);
	/**
	 * @param setup cast=(GtkPageSetup *)
	 * @param unit cast=(GtkUnit)
	 */
	public static final native double gtk_page_setup_get_paper_width(long setup, int unit);
	/**
	 * @param setup cast=(GtkPageSetup *)
	 * @param unit cast=(GtkUnit)
	 */
	public static final native double gtk_page_setup_get_paper_height(long setup, int unit);
	/**
	 * @param setup cast=(GtkPageSetup *)
	 * @param unit cast=(GtkUnit)
	 */
	public static final native double gtk_page_setup_get_page_width(long setup, int unit);
	/**
	 * @param setup cast=(GtkPageSetup *)
	 * @param unit cast=(GtkUnit)
	 */
	public static final native double gtk_page_setup_get_page_height(long setup, int unit);
	/**
	 * @param context cast=(GtkStyleContext *)
	 * @param cr cast=(cairo_t *)
	 * @param x cast=(gdouble)
	 * @param y cast=(gdouble)
	 * @param width cast=(gdouble)
	 * @param height cast=(gdouble)
	 */
	public static final native void gtk_render_frame(long context, long cr, double x , double y, double width, double height);
	/**
	 * @param context cast=(GtkStyleContext *)
	 * @param cr cast=(cairo_t *)
	 * @param x cast=(gdouble)
	 * @param y cast=(gdouble)
	 * @param width cast=(gdouble)
	 * @param height cast=(gdouble)
	 */
	public static final native void gtk_render_background(long context, long cr, double x , double y, double width, double height);
	/**
	* @param context cast=(GtkStyleContext *)
	* @param cr cast=(cairo_t *)
	* @param x cast=(gdouble)
	* @param y cast=(gdouble)
	* @param width cast=(gdouble)
	* @param height cast=(gdouble)
	*/
	public static final native void gtk_render_focus(long context, long cr,  double x , double y, double width, double height);
	/**
	 * @param size cast=(GtkPaperSize *)
	 */
	public static final native void gtk_paper_size_free(long size);
	/**
	 * @param name cast=(const gchar *)
	 */
	public static final native long gtk_paper_size_new(byte [] name);
	/**
	 * @param ppd_name cast=(const gchar *)
	 * @param ppd_display_name cast=(const gchar *)
	 * @param width cast=(gdouble)
	 * @param height cast=(gdouble)
	 */
	public static final native long gtk_paper_size_new_from_ppd(byte [] ppd_name, byte [] ppd_display_name, double width, double height);
	/**
	 * @param name cast=(const gchar *)
	 * @param display_name cast=(const gchar *)
	 * @param width cast=(gdouble)
	 * @param height cast=(gdouble)
	 * @param unit cast=(GtkUnit)
	 */
	public static final native long gtk_paper_size_new_custom(byte [] name, byte [] display_name, double width, double height, int unit);
	/**
	 * @param size cast=(GtkPaperSize *)
	 */
	public static final native long gtk_paper_size_get_name(long size);
	/**
	 * @param size cast=(GtkPaperSize *)
	 */
	public static final native long gtk_paper_size_get_display_name(long size);
	/**
	 * @param size cast=(GtkPaperSize *)
	 */
	public static final native long gtk_paper_size_get_ppd_name(long size);
	/**
	 * @param size cast=(GtkPaperSize *)
	 * @param unit cast=(GtkUnit)
	 */
	public static final native double gtk_paper_size_get_width(long size, int unit);
	/**
	 * @param size cast=(GtkPaperSize *)
	 * @param unit cast=(GtkUnit)
	 */
	public static final native double gtk_paper_size_get_height(long size, int unit);
	/**
	 * @param size cast=(GtkPaperSize *)
	 */
	public static final native boolean gtk_paper_size_is_custom(long size);
	public static final native long gtk_plug_new(long socket_id);
	/**
	 * @param printer cast=(GtkPrinter *)
	 */
	public static final native long gtk_printer_get_backend(long printer);
	/**
	 * @param printer cast=(GtkPrinter *)
	 */
	public static final native long gtk_printer_get_name(long printer);
	/**
	 * @param printer cast=(GtkPrinter *)
	 */
	public static final native boolean gtk_printer_is_default(long printer);
	/**
	 * @param func cast=(GtkPrinterFunc)
	 * @param data cast=(gpointer)
	 * @param destroy cast=(GDestroyNotify)
	 * @param wait cast=(gboolean)
	 */
	public static final native void gtk_enumerate_printers(long func, long data, long destroy, boolean wait);
	/**
	 * @param title cast=(const gchar *)
	 * @param printer cast=(GtkPrinter *)
	 * @param settings cast=(GtkPrintSettings *)
	 * @param page_setup cast=(GtkPageSetup *)
	 */
	public static final native long gtk_print_job_new(byte[] title, long printer, long settings, long page_setup);
	/**
	 * @param job cast=(GtkPrintJob *)
	 * @param error cast=(GError **)
	 */
	public static final native long gtk_print_job_get_surface(long job, long error[]);
	/**
	 * @param job cast=(GtkPrintJob *)
	 * @param callback cast=(GtkPrintJobCompleteFunc)
	 * @param user_data cast=(gpointer)
	 * @param dnotify cast=(GDestroyNotify)
	 */
	public static final native void gtk_print_job_send(long job, long callback, long user_data, long dnotify);
	public static final native long gtk_print_settings_new();
	/**
	 * @param settings cast=(GtkPrintSettings *)
	 * @param func cast=(GtkPrintSettingsFunc)
	 * @param data cast=(gpointer)
	 */
	public static final native void gtk_print_settings_foreach(long settings, long func, long data);
	/**
	 * @param settings cast=(GtkPrintSettings *)
	 * @param key cast=(const gchar *)
	 */
	public static final native long gtk_print_settings_get(long settings, byte [] key);
	/**
	 * @param settings cast=(GtkPrintSettings *)
	 * @param key cast=(const gchar *)
	 * @param value cast=(const gchar *)
	 */
	public static final native void gtk_print_settings_set(long settings, byte [] key, byte [] value);
	/**
	 * @param settings cast=(GtkPrintSettings *)
	 * @param printer cast=(const gchar *)
	 */
	public static final native void gtk_print_settings_set_printer(long settings, byte[] printer);
	/**
	 * @param settings cast=(GtkPrintSettings *)
	 * @param orientation cast=(GtkPageOrientation)
	 */
	public static final native void gtk_print_settings_set_orientation(long settings, int orientation);
	/**
	 * @param settings cast=(GtkPrintSettings *)
	 */
	public static final native boolean gtk_print_settings_get_collate(long settings);
	/**
	 * @param settings cast=(GtkPrintSettings *)
	 * @param collate cast=(gboolean)
	 */
	public static final native void gtk_print_settings_set_collate(long settings, boolean collate);
	/**
	 * @param settings cast=(GtkPrintSettings *)
	 */
	public static final native int gtk_print_settings_get_duplex(long settings);
	/**
	 * @param settings cast=(GtkPrintSettings *)
	 * @param duplex cast=(GtkPrintDuplex)
	 */
	public static final native void gtk_print_settings_set_duplex(long settings, int duplex);
	/**
	 * @param settings cast=(GtkPrintSettings *)
	 */
	public static final native int gtk_print_settings_get_n_copies(long settings);
	/**
	 * @param settings cast=(GtkPrintSettings *)
	 * @param num_copies cast=(gint)
	 */
	public static final native void gtk_print_settings_set_n_copies(long settings, int num_copies);
	/**
	 * @param settings cast=(GtkPrintSettings *)
	 */
	public static final native int gtk_print_settings_get_print_pages(long settings);
	/**
	 * @param settings cast=(GtkPrintSettings *)
	 * @param pages cast=(GtkPrintPages)
	 */
	public static final native void gtk_print_settings_set_print_pages(long settings, int pages);
	/**
	 * @param settings cast=(GtkPrintSettings *)
	 * @param num_ranges cast=(gint *)
	 */
	public static final native long gtk_print_settings_get_page_ranges(long settings, int[] num_ranges);
	/**
	 * @param settings cast=(GtkPrintSettings *)
	 * @param page_ranges cast=(GtkPageRange *)
	 * @param num_ranges cast=(gint)
	 */
	public static final native void gtk_print_settings_set_page_ranges(long settings, int[] page_ranges, int num_ranges);
	/**
	 * @param settings cast=(GtkPrintSettings *)
	 */
	public static final native int gtk_print_settings_get_resolution(long settings);
	/**
	 * @param title cast=(const gchar *)
	 * @param parent cast=(GtkWindow *)
	 */
	public static final native long gtk_print_unix_dialog_new(byte[] title, long parent);
	/**
	 * @param dialog cast=(GtkPrintUnixDialog *)
	 * @param embed cast=(gboolean)
	 */
	public static final native void gtk_print_unix_dialog_set_embed_page_setup(long dialog, boolean embed);
	/**
	 * @param dialog cast=(GtkPrintUnixDialog *)
	 * @param page_setup cast=(GtkPageSetup *)
	 */
	public static final native void gtk_print_unix_dialog_set_page_setup(long dialog, long page_setup);
	/**
	 * @param dialog cast=(GtkPrintUnixDialog *)
	 */
	public static final native long gtk_print_unix_dialog_get_page_setup(long dialog);
	/**
	 * @param dialog cast=(GtkPrintUnixDialog *)
	 * @param current_page cast=(gint)
	 */
	public static final native void gtk_print_unix_dialog_set_current_page(long dialog, int current_page);
	/**
	 * @param dialog cast=(GtkPrintUnixDialog *)
	 */
	public static final native int gtk_print_unix_dialog_get_current_page(long dialog);
	/**
	 * @param dialog cast=(GtkPrintUnixDialog *)
	 * @param settings cast=(GtkPrintSettings *)
	 */
	public static final native void gtk_print_unix_dialog_set_settings(long dialog, long settings);
	/**
	 * @param dialog cast=(GtkPrintUnixDialog *)
	 */
	public static final native long gtk_print_unix_dialog_get_settings(long dialog);
	/**
	 * @param dialog cast=(GtkPrintUnixDialog *)
	 */
	public static final native long gtk_print_unix_dialog_get_selected_printer(long dialog);
	/**
	 * @param dialog cast=(GtkPrintUnixDialog *)
	 * @param capabilities cast=(GtkPrintCapabilities)
	 */
	public static final native void gtk_print_unix_dialog_set_manual_capabilities(long dialog, long capabilities);
	/**
	 * @param dialog cast=(GtkPrintUnixDialog *)
	 */
	public static final native void gtk_print_unix_dialog_set_support_selection(long dialog, boolean support_selection);
	/**
	 * @param dialog cast=(GtkPrintUnixDialog *)
	 */
	public static final native void gtk_print_unix_dialog_set_has_selection(long dialog, boolean has_selection);
	public static final native long gtk_progress_bar_new();
	/** @param pbar cast=(GtkProgressBar *) */
	public static final native void gtk_progress_bar_pulse(long pbar);
	/**
	 * @param pbar cast=(GtkProgressBar *)
	 * @param fraction cast=(gdouble)
	 */
	public static final native void gtk_progress_bar_set_fraction(long pbar, double fraction);
	/**
	 * @param pbar cast=(GtkProgressBar *)
	 * @param inverted cast=(gboolean)
	 */
	public static final native void gtk_progress_bar_set_inverted(long pbar, boolean inverted);
	/** @param radio_button cast=(GtkRadioButton *) */
	public static final native long gtk_radio_button_get_group(long radio_button);
	/** @param group cast=(GSList *) */
	public static final native long gtk_radio_button_new(long group);
	/** @param radio_menu_item cast=(GtkRadioMenuItem *) */
	public static final native long gtk_radio_menu_item_get_group(long radio_menu_item);
	/** @param group cast=(GSList *) */
	public static final native long gtk_radio_menu_item_new(long group);
	/** @param range cast=(GtkRange *) */
	public static final native long gtk_range_get_adjustment(long range);
	/** @param range cast=(GtkRange *) */
	public static final native void gtk_range_set_increments(long range, double step, double page);
	/** @param range cast=(GtkRange *) */
	public static final native void gtk_range_set_inverted(long range, boolean setting);
	/** @param range cast=(GtkRange *) */
	public static final native void gtk_range_set_range(long range, double min, double max);
	/** @param range cast=(GtkRange *) */
	public static final native void gtk_range_set_value(long range, double value);
	/**
	 *  @param range cast=(GtkRange *)
	 *  @param slider_start cast=(gint *)
	 *  @param slider_end cast=(gint *)
	 */
	public static final native void gtk_range_get_slider_range(long range, int[] slider_start, int[] slider_end);
	/**
	 * @param scale cast=(GtkScale *)
	 * @param digits cast=(gint)
	 */
	public static final native void gtk_scale_set_digits(long scale, int digits);
	/**
	 * @param scale cast=(GtkScale *)
	 * @param draw_value cast=(gboolean)
	 */
	public static final native void gtk_scale_set_draw_value(long scale, boolean draw_value);
	/**
	 * @param scrollable cast=(GtkScrollable *)
	 */
	public static final native long gtk_scrollable_get_vadjustment(long scrollable);
	/** @param scrolled_window cast=(GtkScrolledWindow *) */
	public static final native long gtk_scrolled_window_get_hadjustment(long scrolled_window);
	/**
	 * @param scrolled_window cast=(GtkScrolledWindow *)
	 */
	public static final native long gtk_scrolled_window_get_hscrollbar(long scrolled_window);
	/**
	 * @param scrolled_window cast=(GtkScrolledWindow *)
	 * @param hscrollbar_policy cast=(GtkPolicyType *)
	 * @param vscrollbar_policy cast=(GtkPolicyType *)
	 */
	public static final native void gtk_scrolled_window_get_policy(long scrolled_window, int[] hscrollbar_policy, int[] vscrollbar_policy);
	/** @param scrolled_window cast=(GtkScrolledWindow *) */
	public static final native int gtk_scrolled_window_get_shadow_type(long scrolled_window);
	/** @param scrolled_window cast=(GtkScrolledWindow *) */
	public static final native long gtk_scrolled_window_get_vadjustment(long scrolled_window);
	/**
	 * @param scrolled_window cast=(GtkScrolledWindow *)
	 */
	public static final native long gtk_scrolled_window_get_vscrollbar(long scrolled_window);
	/**
	 * @param hadjustment cast=(GtkAdjustment *)
	 * @param vadjustment cast=(GtkAdjustment *)
	 */
	public static final native long gtk_scrolled_window_new(long hadjustment, long vadjustment);
	/**
	 * @param scrolled_window cast=(GtkScrolledWindow *)
	 * @param hscrollbar_policy cast=(GtkPolicyType)
	 * @param vscrollbar_policy cast=(GtkPolicyType)
	 */
	public static final native void gtk_scrolled_window_set_policy(long scrolled_window, int hscrollbar_policy, int vscrollbar_policy);
	/**
	 * @method flags=dynamic
	 * @param scrolled_window cast=(GtkScrolledWindow *)
	 */
	public static final native boolean gtk_scrolled_window_get_overlay_scrolling(long scrolled_window);
	/**
	 * @param scrolled_window cast=(GtkScrolledWindow *)
	 * @param type cast=(GtkShadowType)
	 */
	public static final native void gtk_scrolled_window_set_shadow_type(long scrolled_window, int type);
	public static final native long gtk_settings_get_default();
	/** @param selection_data cast=(GtkSelectionData *) */
	public static final native void gtk_selection_data_free(long selection_data);
	/**
	 * @param selection_data cast=(GtkSelectionData *)
	 */
	public static final native long gtk_selection_data_get_data(long selection_data);
	/**
	 * @param selection_data cast=(GtkSelectionData *)
	 */
	public static final native int gtk_selection_data_get_format(long selection_data);
	/**
	 * @param selection_data cast=(GtkSelectionData *)
	 */
	public static final native int gtk_selection_data_get_length(long selection_data);
	/**
	 * @param selection_data cast=(GtkSelectionData *)
	 */
	public static final native long gtk_selection_data_get_target(long selection_data);
	/**
	 * @param selection_data cast=(GtkSelectionData *)
	 */
	public static final native long gtk_selection_data_get_data_type(long selection_data);
	/**
	 * @param selection_data cast=(GtkSelectionData *)
	 * @param type cast=(GdkAtom)
	 * @param format cast=(gint)
	 * @param data cast=(const guchar *)
	 * @param length cast=(gint)
	 */
	public static final native void gtk_selection_data_set(long selection_data, long type, int format, long data, int length);
	public static final native long gtk_separator_menu_item_new();
	public static final native long gtk_separator_tool_item_new();
	/**
	 * @param item cast=(GtkSeparatorToolItem *)
	 * @param draw cast=(gboolean)
	 */
	public static final native void gtk_separator_tool_item_set_draw(long item, boolean draw);
	/** @param socket cast=(GtkSocket *) */
	public static final native long gtk_socket_get_id(long socket);
	public static final native long gtk_socket_new();
	/** @param adjustment cast=(GtkAdjustment *) */
	public static final native long gtk_spin_button_new(long adjustment, double climb_rate, int digits);
	/**
	 * @param spin_button cast=(GtkSpinButton*)
	 * @param numeric cast=(gboolean)
	 **/
	public static final native void gtk_spin_button_set_numeric(long spin_button, boolean numeric);
	/**
	 * @param spin_button cast=(GtkSpinButton*)
	 * @param adjustment cast=(GtkAdjustment *)
	 **/
	public static final native void gtk_spin_button_configure(long spin_button, long adjustment, double climb_rate, int digits);
	/** @param spin_button cast=(GtkSpinButton*) */
	public static final native long gtk_spin_button_get_adjustment(long spin_button);
	/** @param spin_button cast=(GtkSpinButton*) */
	public static final native int gtk_spin_button_get_digits(long spin_button);
	/** @param spin_button cast=(GtkSpinButton*) */
	public static final native void gtk_spin_button_set_increments(long spin_button, double step, double page);
	/** @param spin_button cast=(GtkSpinButton*) */
	public static final native void gtk_spin_button_set_range(long spin_button, double max, double min);
	/** @param spin_button cast=(GtkSpinButton*) */
	public static final native void gtk_spin_button_set_value(long spin_button, double value);
	/** @param spin_button cast=(GtkSpinButton*) */
	public static final native void gtk_spin_button_set_wrap(long spin_button, boolean wrap);
	/** @param spin_button cast=(GtkSpinButton*) */
	public static final native void gtk_spin_button_update(long spin_button);
	/**
	 * @param snapshot cast=(GtkSnapshot *)
	 * @param rect cast=(const graphene_rect_t *)
	 */
	/* [GTK4 only, if-def'd in os.h] */
	public static final native long gtk_snapshot_append_cairo(long snapshot, long rect);
	/**
	 * @param handle cast=(GtkStatusIcon*)
	 * @param screen cast=(GdkScreen**)
	 * @param area cast=(GdkRectangle*)
	 * @param orientation cast=(GtkOrientation*)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native boolean gtk_status_icon_get_geometry(long handle, long screen, GdkRectangle area, long orientation);
	/** @param handle cast=(GtkStatusIcon*) */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native boolean gtk_status_icon_get_visible(long handle);
	/* [GTK3 only, if-def'd in os.h] */
	public static final native long gtk_status_icon_new();
	/**
	 * @param handle cast=(GtkStatusIcon*)
	 * @param pixbuf cast=(GdkPixbuf*)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gtk_status_icon_set_from_pixbuf(long handle, long pixbuf);
	/**
	 * @param handle cast=(GtkStatusIcon*)
	 * @param visible cast=(gboolean)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gtk_status_icon_set_visible(long handle, boolean visible);
	/**
	 * @param handle cast=(GtkStatusIcon *)
	 * @param tip_text cast=(const gchar *)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gtk_status_icon_set_tooltip_text(long handle, byte[] tip_text);
	/**
	 * @param context cast=(GtkStyleContext *)
	 * @param class_name cast=(const gchar *)
	 */
	public static final native void gtk_style_context_add_class(long context, byte[] class_name);
	/**
	 * @param context cast=(GtkStyleContext *)
	 * @param class_name cast=(const gchar *)
	 */
	public static final native void gtk_style_context_remove_class(long context, byte[] class_name);
	/**
	 * @method flags=dynamic
	 * @param context cast=(GtkStyleContext *)
	 * @param state cast=(GtkStateFlags)
	 * @param color cast=(GdkRGBA *)
	 */
	/* [GTK3 only] */
	public static final native void gtk_style_context_get_color(long context, int state, GdkRGBA color);
	/**
	 * @method flags=dynamic
	 * @param context cast=(GtkStyleContext *)
	 * @param color cast=(GdkRGBA *)
	 */
	/* [GTK4 only] */
	public static final native void gtk_style_context_get_color(long context, GdkRGBA color);
	/** @method flags=dynamic */
	/* [GTK3; 3.8 deprecated, replaced] */
	public static final native long gtk_style_context_get_font(long context, int state);
	/**
	 * @method flags=dynamic
	 * @param context cast=(GtkStyleContext *)
	 * @param state cast=(GtkStateFlags)
	 * @param padding cast=(GtkBorder *),flags=no_in
	 */
	/* [GTK3 only] */
	public static final native void gtk_style_context_get_padding(long context, int state, GtkBorder padding);
	/**
	 * @param context cast=(GtkStyleContext *)
	 */
	public static final native long gtk_style_context_get_parent(long context);
	/**
	 * @method flags=dynamic
	 * @param context cast=(GtkStyleContext *)
	 * @param padding cast=(GtkBorder *),flags=no_in
	 */
	/* [GTK4 only] */
	public static final native void gtk_style_context_get_padding(long context, GtkBorder padding);
	/**
	 * @param context cast=(GtkStyleContext *)
	 * @param margin cast=(GtkBorder *),flags=no_in
	 */
	/* [GTK4 only, if-def'd in os.h] */
	public static final native void gtk_style_context_get_margin(long context, GtkBorder margin);
	/**
	 * @method flags=dynamic
	 * @param property cast=(const gchar *),flags=no_out
	 * @param terminator cast=(const gchar *),flags=sentinel
	 */
	public static final native void gtk_style_context_get(long context, int state, byte [] property, long [] value, long terminator);
	/**
	 * @method flags=dynamic
	 * @param property cast=(const gchar *),flags=no_out
	 * @param terminator cast=(const gchar *),flags=sentinel
	 */
	public static final native void gtk_style_context_get(long context, byte [] property, long [] value, long terminator);
	/**
	 * @method flags=dynamic
	 * @param context cast=(GtkStyleContext *)
	 * @param state cast=(GtkStateFlags)
	 * @param padding cast=(GtkBorder *),flags=no_in
	 */
	/* [GTK3 only] */
	public static final native void gtk_style_context_get_border(long context, int state, GtkBorder padding);
	/**
	 * @method flags=dynamic
	 * @param context cast=(GtkStyleContext *)
	 * @param padding cast=(GtkBorder *),flags=no_in
	 */
	/* [GTK4 only] */
	public static final native void gtk_style_context_get_border(long context, GtkBorder padding);
	/** @method flags=dynamic */
	/* [GTK3; 3.12 deprecated] */
	public static final native void gtk_style_context_invalidate(long context);
	/**
	 * @param self cast=(GtkStyleContext *)
	 */
	public static final native void gtk_style_context_save(long self);
	/**
	 * @param context cast=(GtkStyleContext *)
	 */
	public static final native void gtk_style_context_restore(long context);
	/**
	 * @param self cast=(GtkWidget *)
	 */
	public static final native int gtk_widget_get_state_flags(long self);
	/**
	 * @param context cast=(GtkStyleContext *)
	 * @param flags cast=(GtkStateFlags)
	 */
	public static final native void gtk_style_context_set_state(long context, long flags);
	/**
	 * @param targets cast=(const GtkTargetEntry *)
	 * @param ntargets cast=(guint)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native long gtk_target_list_new(long targets, int ntargets);
	/** @param list cast=(GtkTargetList *) */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gtk_target_list_unref(long list);
	/**
	 * @method flags=dynamic
	 * @param buffer cast=(GtkTextBuffer *)
	 */
	/* [GTK3/GTK4, GTK3 uses GtkClipboard but GTK4 uses GdkClipboard -- method signature otherwise identical] */
	public static final native void gtk_text_buffer_copy_clipboard(long buffer, long clipboard);
	/**
	 * @param buffer cast=(GtkTextBuffer *)
	 * @param mark_name cast=(const gchar *)
	 * @param where cast=(GtkTextIter *)
	 * @param left_gravity cast=(gboolean)
	 */
	public static final native long gtk_text_buffer_create_mark(long buffer, byte [] mark_name, byte [] where, boolean left_gravity);
	/**
	 * @method flags=dynamic
	 * @param buffer cast=(GtkTextBuffer *)
	 * @param default_editable cast=(gboolean)
	 */
	/* [GTK3/GTK4, GTK3 uses GtkClipboard but GTK4 uses GdkClipboard -- method signature otherwise identical] */
	public static final native void gtk_text_buffer_cut_clipboard(long buffer, long clipboard, boolean default_editable);
	/**
	 * @param buffer cast=(GtkTextBuffer *)
	 * @param start cast=(GtkTextIter *)
	 * @param end cast=(GtkTextIter *)
	 */
	public static final native void gtk_text_buffer_delete(long buffer, byte[] start, byte[] end);
	/**
	 * @param buffer cast=(GtkTextBuffer *)
	 * @param start cast=(GtkTextIter *)
	 * @param end cast=(GtkTextIter *)
	 */
	public static final native void gtk_text_buffer_get_bounds(long buffer, byte[] start, byte[] end);
	/**
	 * @param buffer cast=(GtkTextBuffer *)
	 * @param iter cast=(GtkTextIter *)
	 */
	public static final native void gtk_text_buffer_get_end_iter(long buffer, byte[] iter);
	/** @param buffer cast=(GtkTextBuffer *) */
	public static final native long gtk_text_buffer_get_insert(long buffer);
	/**
	 * @param buffer cast=(GtkTextBuffer *)
	 * @param iter cast=(GtkTextIter *)
	 * @param line_number cast=(gint)
	 */
	public static final native void gtk_text_buffer_get_iter_at_line(long buffer, byte[] iter, int line_number);
	/**
	 * @param buffer cast=(GtkTextBuffer *)
	 * @param iter cast=(GtkTextIter *)
	 * @param mark cast=(GtkTextMark *)
	 */
	public static final native void gtk_text_buffer_get_iter_at_mark(long buffer, byte[] iter, long mark);
	/**
	 * @param buffer cast=(GtkTextBuffer *)
	 * @param iter cast=(GtkTextIter *)
	 * @param char_offset cast=(gint)
	 */
	public static final native void gtk_text_buffer_get_iter_at_offset(long buffer, byte[] iter, int char_offset);
	/** @param buffer cast=(GtkTextBuffer *) */
	public static final native int gtk_text_buffer_get_line_count(long buffer);
	/** @param buffer cast=(GtkTextBuffer *) */
	public static final native long gtk_text_buffer_get_selection_bound(long buffer);
	/**
	 * @param buffer cast=(GtkTextBuffer *)
	 * @param start cast=(GtkTextIter *)
	 * @param end cast=(GtkTextIter *)
	 */
	public static final native boolean gtk_text_buffer_get_selection_bounds(long buffer, byte[] start, byte[] end);
	/**
	 * @param buffer cast=(GtkTextBuffer *)
	 * @param start cast=(GtkTextIter *)
	 * @param end cast=(GtkTextIter *)
	 * @param include_hidden_chars cast=(gboolean)
	 */
	public static final native long gtk_text_buffer_get_text(long buffer, byte[] start, byte[] end, boolean include_hidden_chars);
	/**
	 * @param buffer cast=(GtkTextBuffer *)
	 * @param iter cast=(GtkTextIter *)
	 * @param text cast=(const gchar *)
	 * @param len cast=(gint)
	 */
	public static final native void gtk_text_buffer_insert(long buffer, byte[] iter, byte[] text, int len);
	/**
	 * @param buffer cast=(GtkTextBuffer *)
	 * @param iter cast=(GtkTextIter *)
	 * @param text cast=(const gchar *)
	 * @param len cast=(gint)
	 */
	public static final native void gtk_text_buffer_insert(long buffer, long iter, byte[] text, int len);
	/**
	 * @param buffer cast=(GtkTextBuffer *)
	 * @param ins cast=(const GtkTextIter *)
	 * @param bound cast=(const GtkTextIter *)
	 */
	public static final native void gtk_text_buffer_select_range(long buffer, byte[] ins, byte[] bound);
	/**
	 * @method flags=dynamic
	 * @param buffer cast=(GtkTextBuffer *)
	 * @param override_location cast=(GtkTextIter *)
	 * @param default_editable cast=(gboolean)
	 */
	/* [GTK3/GTK4, GTK3 uses GtkClipboard but GTK4 uses GdkClipboard -- method signature otherwise identical] */
	public static final native void gtk_text_buffer_paste_clipboard(long buffer, long clipboard, byte[] override_location, boolean default_editable);
	/**
	 * @param buffer cast=(GtkTextBuffer *)
	 * @param where cast=(const GtkTextIter *)
	 */
	public static final native void gtk_text_buffer_place_cursor(long buffer, byte[] where);
	/**
	 * @param buffer cast=(GtkTextBuffer *)
	 * @param text cast=(const gchar *)
	 * @param len cast=(gint)
	 */
	public static final native void gtk_text_buffer_set_text(long buffer, byte[] text, int len);
	/** @param iter cast=(const GtkTextIter *) */
	public static final native int gtk_text_iter_get_line(byte[] iter);
	/** @param iter cast=(const GtkTextIter *) */
	public static final native int gtk_text_iter_get_offset(byte[] iter);
	/**
	 * @method flags=dynamic
	 * @param text_view cast=(GtkTextView *)
	 * @param win cast=(GtkTextWindowType)
	 * @param buffer_x cast=(gint)
	 * @param buffer_y cast=(gint)
	 * @param window_x cast=(gint *)
	 * @param window_y cast=(gint *)
	 */
	/* [GTK3 only] */
	public static final native void gtk_text_view_buffer_to_window_coords(long text_view, int win, int buffer_x, int buffer_y, int[] window_x, int[] window_y);
	/**
	 * @method flags=dynamic
	 * @param text_view cast=(GtkTextView *)
	 * @param win cast=(GtkTextWindowType)
	 * @param buffer_x cast=(gint)
	 * @param buffer_y cast=(gint)
	 * @param window_x cast=(gint *)
	 * @param window_y cast=(gint *)
	 */
	/* [GTK4 only] */
	public static final native void gtk_text_view_buffer_to_surface_coords(long text_view, int win, int buffer_x, int buffer_y, int[] window_x, int[] window_y);
	/** @param text_view cast=(GtkTextView *) */
	public static final native long gtk_text_view_get_buffer(long text_view);
	/** @param text_view cast=(GtkTextView *) */
	public static final native boolean gtk_text_view_get_editable(long text_view);
	/**
	 * @param text_view cast=(GtkTextView *)
	 * @param iter cast=(GtkTextIter *)
	 * @param x cast=(gint)
	 * @param y cast=(gint)
	 */
	public static final native void gtk_text_view_get_iter_at_location(long text_view, byte[] iter, int x, int y);
	/**
	 * @param text_view cast=(GtkTextView *)
	 * @param iter cast=(const GtkTextIter *)
	 * @param location cast=(GdkRectangle *),flags=no_in
	 */
	public static final native void gtk_text_view_get_iter_location(long text_view, byte[] iter, GdkRectangle location);
	/**
	 * @param text_view cast=(GtkTextView *)
	 * @param target_iter cast=(GtkTextIter *)
	 * @param y cast=(gint)
	 * @param line_top cast=(gint *)
	 */
	public static final native void gtk_text_view_get_line_at_y(long text_view, byte[] target_iter, int y, int[] line_top);
	/**
	 * @param text_view cast=(GtkTextView *)
	 * @param target_iter cast=(GtkTextIter *)
	 * @param y cast=(gint *)
	 * @param height cast=(gint *)
	 */
	public static final native void gtk_text_view_get_line_yrange(long text_view, byte[] target_iter, int[] y, int[] height);
	/**
	 * @param text_view cast=(GtkTextView *)
	 * @param visible_rect cast=(GdkRectangle *),flags=no_in
	 */
	public static final native void gtk_text_view_get_visible_rect(long text_view, GdkRectangle visible_rect);
	/**
	 * @param text_view cast=(GtkTextView *)
	 * @param win cast=(GtkTextWindowType)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native long gtk_text_view_get_window(long text_view, int win);
	public static final native long gtk_text_view_new();
	/**
	 * @param text_view cast=(GtkTextView *)
	 * @param mark cast=(GtkTextMark *)
	 * @param within_margin cast=(gdouble)
	 * @param use_align cast=(gboolean)
	 * @param xalign cast=(gdouble)
	 * @param yalign cast=(gdouble)
	 */
	public static final native void gtk_text_view_scroll_to_mark(long text_view, long mark, double within_margin, boolean use_align, double xalign, double yalign);
	/**
	 * @param text_view cast=(GtkTextView *)
	 * @param iter cast=(GtkTextIter *)
	 * @param within_margin cast=(gdouble)
	 * @param use_align cast=(gboolean)
	 * @param xalign cast=(gdouble)
	 * @param yalign cast=(gdouble)
	 */
	public static final native boolean gtk_text_view_scroll_to_iter(long text_view, byte[] iter, double within_margin, boolean use_align, double xalign, double yalign);
	/**
	 * @param text_view cast=(GtkTextView *)
	 * @param setting cast=(gboolean)
	 */
	public static final native void gtk_text_view_set_editable(long text_view, boolean setting);
	/** @param text_view cast=(GtkTextView *) */
	public static final native void gtk_text_view_set_justification(long text_view, int justification);
	/**
	 * @param text_view cast=(GtkTextView *)
	 * @param tabs cast=(PangoTabArray *)
	 */
	public static final native void gtk_text_view_set_tabs(long text_view, long tabs);
	/** @param text_view cast=(GtkTextView *) */
	public static final native void gtk_text_view_set_wrap_mode(long text_view, int wrap_mode);
	/** @param toggle_button cast=(GtkToggleButton *) */
	public static final native boolean gtk_toggle_button_get_active(long toggle_button);
	public static final native long gtk_toggle_button_new();
	/**
	 * @param toggle_button cast=(GtkToggleButton *)
	 * @param is_active cast=(gboolean)
	 */
	public static final native void gtk_toggle_button_set_active(long toggle_button, boolean is_active);
	/**
	 * @method flags=dynamic
	 * @param toggle_button cast=(GtkToggleButton *)
	 * @param setting cast=(gboolean)
	 */
	/* [GTK3 only] */
	public static final native void gtk_toggle_button_set_inconsistent(long toggle_button, boolean setting);
	/** @param button cast=(GtkToggleToolButton *) */
	public static final native boolean gtk_toggle_tool_button_get_active(long button);
	public static final native long gtk_toggle_tool_button_new();
	/**
	 * @param item cast=(GtkToggleToolButton *)
	 * @param selected cast=(gboolean)
	 */
	public static final native void gtk_toggle_tool_button_set_active(long item, boolean selected);
	/**
	 * @param icon_widget cast=(GtkWidget *)
	 * @param label cast=(const gchar *)
	 */
	public static final native long gtk_tool_button_new(long icon_widget, byte[] label);
	/**
	 * @param button cast=(GtkToolButton *)
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native void gtk_tool_button_set_icon_widget(long button, long widget);
	/**
	 * @param button cast=(GtkToolButton *)
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native void gtk_tool_button_set_label_widget(long button,  long widget);
	/**
	 * @param item cast=(GtkToolButton *)
	 * @param underline cast=(gboolean)
	 */
	public static final native void gtk_tool_button_set_use_underline(long item, boolean underline);
	/**
	 * @param item cast=(GtkToolItem *)
	 * @param menu_id cast=(const gchar *)
	 */
	public static final native long gtk_tool_item_get_proxy_menu_item(long item, byte[] menu_id);
	/** @param item cast=(GtkToolItem *) */
	public static final native long gtk_tool_item_retrieve_proxy_menu_item(long item);
	/**
	 * @param item cast=(GtkToolItem *)
	 * @param important cast=(gboolean)
	 */
	public static final native void gtk_tool_item_set_is_important(long item, boolean important);
	/**
	 * @param item cast=(GtkToolItem *)
	 * @param homogeneous cast=(gboolean)
	 */
	public static final native void gtk_tool_item_set_homogeneous(long item, boolean homogeneous);
	/**
	 * @param item cast=(GtkToolItem *)
	 * @param menu_id cast=(const gchar *)
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native void gtk_tool_item_set_proxy_menu_item(long item, byte[] menu_id, long widget);
	/**
	 * @param toolbar cast=(GtkToolbar *)
	 * @param item cast=(GtkToolItem *)
	 */
	public static final native void gtk_toolbar_insert(long toolbar, long item, int pos);
	public static final native long gtk_toolbar_new();
	/**
	 * @param toolbar cast=(GtkToolbar *)
	 * @param show_arrow cast=(gboolean)
	 */
	public static final native void gtk_toolbar_set_show_arrow(long toolbar, boolean show_arrow);
	/** @param toolbar cast=(GtkToolbar *)
	 * @param style cast=(GtkToolbarStyle)
	 */
	public static final native void gtk_toolbar_set_style(long toolbar, int style);
	/**
	 * @method flags=dynamic
	 * @param toolbar cast=(GtkToolbar *)
	 */
	/* [GTK3 only] */
	public static final native void gtk_toolbar_set_icon_size(long toolbar, int size);
	public static final native long gtk_tooltip_get_type();
	/**
	 * @param tooltip cast=(GtkTooltip *)
	 * @param custom_widget cast=(GtkWidget *)
	 */
	public static final native void gtk_tooltip_set_custom(long tooltip, long custom_widget);
	/**
	 * @param tree_model cast=(GtkTreeModel *)
	 * @param iter cast=(GtkTreeIter *)
	 */
	public static final native void gtk_tree_model_get(long tree_model, long iter, int column, long[] value, int terminator);
	/**
	 * @param tree_model cast=(GtkTreeModel *)
	 * @param iter cast=(GtkTreeIter *)
	 */
	public static final native void gtk_tree_model_get(long tree_model, long iter, int column, int[] value, int terminator);
	/**
	 * @param tree_model cast=(GtkTreeModel *)
	 * @param iter cast=(GtkTreeIter *)
	 * @param path cast=(GtkTreePath *)
	 */
	public static final native boolean gtk_tree_model_get_iter(long tree_model, long iter, long path);
	/**
	 * @param tree_model cast=(GtkTreeModel *)
	 * @param iter cast=(GtkTreeIter *)
	 */
	public static final native boolean gtk_tree_model_get_iter_first(long tree_model, long iter);
	/** @param tree_model cast=(GtkTreeModel *) */
	public static final native int gtk_tree_model_get_n_columns(long tree_model);
	/**
	 * @param tree_model cast=(GtkTreeModel *)
	 * @param iter cast=(GtkTreeIter *)
	 */
	public static final native long gtk_tree_model_get_path(long tree_model, long iter);
	public static final native long gtk_tree_model_get_type();
	/**
	 * @param model cast=(GtkTreeModel *)
	 * @param iter cast=(GtkTreeIter *)
	 * @param parent cast=(GtkTreeIter *)
	 */
	public static final native boolean gtk_tree_model_iter_children(long model, long iter, long parent);
	/**
	 * @param model cast=(GtkTreeModel *)
	 * @param iter cast=(GtkTreeIter *)
	 */
	public static final native int gtk_tree_model_iter_n_children(long model, long iter);
	/**
	 * @param model cast=(GtkTreeModel *)
	 * @param iter cast=(GtkTreeIter *)
	 */
	public static final native boolean gtk_tree_model_iter_next(long model, long iter);
	/**
	 * @param tree_model cast=(GtkTreeModel *)
	 * @param iter cast=(GtkTreeIter *)
	 * @param parent cast=(GtkTreeIter *)
	 */
	public static final native boolean gtk_tree_model_iter_nth_child(long tree_model, long iter, long parent, int n);
	/** @param path cast=(GtkTreePath *) */
	public static final native void gtk_tree_path_append_index(long path, int index);
	/**
	 * @param a cast=(const GtkTreePath *)
	 * @param b cast=(const GtkTreePath *)
	 */
	public static final native long gtk_tree_path_compare(long a, long b);
	/** @param path cast=(GtkTreePath *) */
	public static final native void gtk_tree_path_free(long path);
	/** @param path cast=(GtkTreePath *) */
	public static final native int gtk_tree_path_get_depth(long path);
	/** @param path cast=(GtkTreePath *) */
	public static final native long gtk_tree_path_get_indices(long path);
	public static final native long gtk_tree_path_new();
	/** @param path cast=(const gchar *) */
	public static final native long gtk_tree_path_new_from_string(byte[] path);
	/** @param path cast=(const gchar *) */
	public static final native long gtk_tree_path_new_from_string(long path);
	/** @param path cast=(GtkTreePath *) */
	public static final native void gtk_tree_path_next(long path);
	/** @param path cast=(GtkTreePath *) */
	public static final native boolean gtk_tree_path_prev(long path);
	/** @param path cast=(GtkTreePath *) */
	public static final native boolean gtk_tree_path_up(long path);
	/**
	 * @param selection cast=(GtkTreeSelection *)
	 */
	public static final native int gtk_tree_selection_count_selected_rows(long selection);
	/**
	 * @param selection cast=(GtkTreeSelection *)
	 * @param model cast=(GtkTreeModel **)
	 */
	public static final native long gtk_tree_selection_get_selected_rows(long selection, long [] model);
	/**
	 * @param selection cast=(GtkTreeSelection *)
	 * @param path cast=(GtkTreePath *)
	 */
	public static final native boolean gtk_tree_selection_path_is_selected(long selection, long path);
	/** @param selection cast=(GtkTreeSelection *) */
	public static final native void gtk_tree_selection_select_all(long selection);
	/**
	 * @param selection cast=(GtkTreeSelection *)
	 * @param iter cast=(GtkTreeIter *)
	 */
	public static final native void gtk_tree_selection_select_iter(long selection, long iter);
	/**
	 * @param selection cast=(GtkTreeSelection *)
	 * @param func cast=(GtkTreeSelectionFunc)
	 * @param data cast=(gpointer)
	 * @param destroy cast=(GDestroyNotify)
	 */
	public static final native void gtk_tree_selection_set_select_function(long selection, long func, long data, long destroy);
	/**
	 * @param selection cast=(GtkTreeSelection *)
	 * @param mode cast=(GtkSelectionMode)
	 */
	public static final native void gtk_tree_selection_set_mode(long selection, int mode);
	/**
	 * @param selection cast=(GtkTreeSelection *)
	 * @param path cast=(GtkTreePath *)
	 */
	public static final native void gtk_tree_selection_unselect_path(long selection, long path);
	/** @param selection cast=(GtkTreeSelection *) */
	public static final native void gtk_tree_selection_unselect_all(long selection);
	/**
	 * @param selection cast=(GtkTreeSelection *)
	 * @param iter cast=(GtkTreeIter *)
	 */
	public static final native void gtk_tree_selection_unselect_iter(long selection, long iter);
	/**
	 * @param store cast=(GtkTreeStore *)
	 * @param iter cast=(GtkTreeIter *)
	 * @param parent cast=(GtkTreeIter *)
	 */
	public static final native void gtk_tree_store_append(long store, long iter, long parent);
	/** @param store cast=(GtkTreeStore *) */
	public static final native void gtk_tree_store_clear(long store);
	/**
	 * @param store cast=(GtkTreeStore *)
	 * @param iter cast=(GtkTreeIter *)
	 * @param parent cast=(GtkTreeIter *)
	 * @param position cast=(gint)
	 */
	public static final native void gtk_tree_store_insert(long store, long iter, long parent, int position);
	/** @param types cast=(GType *) */
	public static final native long gtk_tree_store_newv(int numColumns, long [] types);
	/**
	 * @param store cast=(GtkTreeStore *)
	 * @param iter cast=(GtkTreeIter *)
	 * @param parent cast=(GtkTreeIter *)
	 */
	public static final native void gtk_tree_store_prepend(long store, long iter, long parent);
	/**
	 * @param store cast=(GtkTreeStore *)
	 * @param iter cast=(GtkTreeIter *)
	 */
	public static final native void gtk_tree_store_remove(long store, long iter);
	/**
	 * @param store cast=(GtkTreeStore *)
	 * @param iter cast=(GtkTreeIter *)
	 */
	public static final native void gtk_tree_store_set(long store, long iter, int column, byte[] value, int terminator);
	/**
	 * @param store cast=(GtkTreeStore *)
	 * @param iter cast=(GtkTreeIter *)
	 */
	public static final native void gtk_tree_store_set(long store, long iter, int column, int value, int terminator);
	/**
	 * @param store cast=(GtkTreeStore *)
	 * @param iter cast=(GtkTreeIter *)
	 */
	public static final native void gtk_tree_store_set(long store, long iter, int column, long value, int terminator);
	/**
	 * @param store cast=(GtkTreeStore *)
	 * @param iter cast=(GtkTreeIter *)
	 * @param value flags=no_out
	 */
	public static final native void gtk_tree_store_set(long store, long iter, int column, GdkRGBA value, int terminator);
	/**
	 * @param store cast=(GtkTreeStore *)
	 * @param iter cast=(GtkTreeIter *)
	 */
	public static final native void gtk_tree_store_set(long store, long iter, int column, boolean value, int terminator);
	/**
	 * @param view cast=(GtkTreeView *)
	 * @param path cast=(GtkTreePath *)
	 */
	public static final native long gtk_tree_view_create_row_drag_icon(long view, long path);
	/**
	 * @param view cast=(GtkTreeView *)
	 * @param path cast=(GtkTreePath *)
	 */
	public static final native boolean gtk_tree_view_collapse_row(long view, long path);
	/**
	 * @param treeColumn cast=(GtkTreeViewColumn *)
	 * @param cellRenderer cast=(GtkCellRenderer *)
	 * @param attribute cast=(const gchar *)
	 * @param column cast=(gint)
	 */
	public static final native void gtk_tree_view_column_add_attribute(long treeColumn, long cellRenderer, byte[] attribute, int column);
	/**
	 * @param tree_column cast=(GtkTreeViewColumn *)
	 * @param cell_renderer cast=(GtkCellRenderer *)
	 * @param start_pos cast=(gint *)
	 * @param width cast=(gint *)
	 */
	public static final native boolean gtk_tree_view_column_cell_get_position(long tree_column, long cell_renderer, int[] start_pos, int[] width);
	/**
	 * @param tree_column cast=(GtkTreeViewColumn *)
	 * @param cell_area cast=(GdkRectangle *),flags=no_in
	 * @param x_offset cast=(gint *)
	 * @param y_offset cast=(gint *)
	 * @param width cast=(gint *)
	 * @param height cast=(gint *)
	 */
	public static final native void gtk_tree_view_column_cell_get_size(long tree_column, GdkRectangle cell_area, int[] x_offset, int[] y_offset, int[] width, int[] height);
	/**
	 * @param tree_column cast=(GtkTreeViewColumn *)
	 * @param tree_model cast=(GtkTreeModel *)
	 * @param iter cast=(GtkTreeIter *)
	 */
	public static final native void gtk_tree_view_column_cell_set_cell_data(long tree_column, long tree_model, long iter, boolean is_expander, boolean is_expanded);
	/** @param tree_column cast=(GtkTreeViewColumn *) */
	public static final native void gtk_tree_view_column_clear(long tree_column);
	/**
	 * @param column cast=(GtkTreeViewColumn *)
	 */
	public static final native long gtk_tree_view_column_get_button(long column);
	/** @param column cast=(GtkTreeViewColumn *) */
	public static final native int gtk_tree_view_column_get_fixed_width(long column);
	/** @param column cast=(GtkTreeViewColumn *) */
	public static final native boolean gtk_tree_view_column_get_reorderable(long column);
	/** @param column cast=(GtkTreeViewColumn *) */
	public static final native boolean gtk_tree_view_column_get_resizable(long column);
	/** @param column cast=(GtkTreeViewColumn *) */
	public static final native boolean gtk_tree_view_column_get_visible(long column);
	/** @param column cast=(GtkTreeViewColumn *) */
	public static final native int gtk_tree_view_column_get_width(long column);
	public static final native long gtk_tree_view_column_new();
	/**
	 * @param tree_column cast=(GtkTreeViewColumn *)
	 * @param cell_renderer cast=(GtkCellRenderer *)
	 * @param expand cast=(gboolean)
	 */
	public static final native void gtk_tree_view_column_pack_start(long tree_column, long cell_renderer, boolean expand);
	/**
	 * @param tree_column cast=(GtkTreeViewColumn *)
	 * @param cell_renderer cast=(GtkCellRenderer *)
	 * @param expand cast=(gboolean)
	 */
	public static final native void gtk_tree_view_column_pack_end(long tree_column, long cell_renderer, boolean expand);
	/** @param tree_column cast=(GtkTreeViewColumn *) */
	public static final native void gtk_tree_view_column_set_alignment(long tree_column, float xalign);
	/**
	 * @param tree_column cast=(GtkTreeViewColumn *)
	 * @param cell_renderer cast=(GtkCellRenderer *)
	 * @param func cast=(GtkTreeCellDataFunc)
	 * @param func_data cast=(gpointer)
	 * @param destroy cast=(GDestroyNotify)
	 */
	public static final native void gtk_tree_view_column_set_cell_data_func(long tree_column, long cell_renderer, long func, long func_data, long destroy);
	/**
	 * @param column cast=(GtkTreeViewColumn *)
	 * @param clickable cast=(gboolean)
	 */
	public static final native void gtk_tree_view_column_set_clickable(long column, boolean clickable);
	/**
	 * @param column cast=(GtkTreeViewColumn *)
	 * @param fixed_width cast=(gint)
	 */
	public static final native void gtk_tree_view_column_set_fixed_width(long column, int fixed_width);
	/**
	 * @param tree_column cast=(GtkTreeViewColumn *)
	 * @param min_width cast=(gint)
	 */
	public static final native void gtk_tree_view_column_set_min_width(long tree_column, int min_width);
	/**
	 * @param column cast=(GtkTreeViewColumn *)
	 * @param reorderable cast=(gboolean)
	 */
	public static final native void gtk_tree_view_column_set_reorderable(long column, boolean reorderable);
	/**
	 * @param column cast=(GtkTreeViewColumn *)
	 * @param resizable cast=(gboolean)
	 */
	public static final native void gtk_tree_view_column_set_resizable(long column, boolean resizable);
	/**
	 * @param column cast=(GtkTreeViewColumn *)
	 * @param type cast=(GtkTreeViewColumnSizing)
	 */
	public static final native void gtk_tree_view_column_set_sizing(long column, int type);
	/**
	 * @param tree_column cast=(GtkTreeViewColumn *)
	 * @param setting cast=(gboolean)
	 */
	public static final native void gtk_tree_view_column_set_sort_indicator(long tree_column, boolean setting);
	/**
	 * @param tree_column cast=(GtkTreeViewColumn *)
	 * @param order cast=(GtkSortType)
	 */
	public static final native void gtk_tree_view_column_set_sort_order(long tree_column, int order);
	/** @param tree_column cast=(GtkTreeViewColumn *) */
	public static final native void gtk_tree_view_column_set_visible(long tree_column, boolean visible);
	/**
	 * @param tree_column cast=(GtkTreeViewColumn *)
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native void gtk_tree_view_column_set_widget(long tree_column, long widget);
	/**
	 * @param view cast=(GtkTreeView *)
	 * @param path cast=(GtkTreePath *)
	 */
	public static final native void gtk_tree_view_set_drag_dest_row(long view, long path, int pos);
	/**
	 * @param view cast=(GtkTreeView *)
	 * @param path cast=(GtkTreePath *)
	 * @param open_all cast=(gboolean)
	 */
	public static final native boolean gtk_tree_view_expand_row(long view, long path, boolean open_all);
	/**
	 * @param tree_view cast=(GtkTreeView *)
	 * @param path cast=(GtkTreePath *)
	 * @param column cast=(GtkTreeViewColumn *)
	 * @param rect cast=(GdkRectangle *)
	 */
	public static final native void gtk_tree_view_get_background_area(long tree_view, long path, long column, GdkRectangle rect);
	/**
	 * @param tree_view cast=(GtkTreeView *)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native long gtk_tree_view_get_bin_window(long tree_view);
	/**
	 * @param tree_view cast=(GtkTreeView *)
	 * @param path cast=(GtkTreePath *)
	 * @param column cast=(GtkTreeViewColumn *)
	 * @param rect cast=(GdkRectangle *),flags=no_in
	 */
	public static final native void gtk_tree_view_get_cell_area(long tree_view, long path, long column, GdkRectangle rect);
	/** @param tree_view cast=(GtkTreeView *) */
	public static final native long gtk_tree_view_get_expander_column(long tree_view);
	/**
	 * @param tree_view cast=(GtkTreeView *)
	 * @param n cast=(gint)
	 */
	public static final native long gtk_tree_view_get_column(long tree_view, int n);
	/** @param tree_view cast=(GtkTreeView *) */
	public static final native long gtk_tree_view_get_columns(long tree_view);
	/**
	 * @param tree_view cast=(GtkTreeView *)
	 * @param path cast=(GtkTreePath **)
	 * @param focus_column cast=(GtkTreeViewColumn **)
	 */
	public static final native void gtk_tree_view_get_cursor(long tree_view, long [] path, long [] focus_column);
	/** @param tree_view cast=(GtkTreeView *) */
	public static final native boolean gtk_tree_view_get_headers_visible(long tree_view);
	/**
	 * @param tree_view cast=(GtkTreeView *)
	 * @param x cast=(gint)
	 * @param y cast=(gint)
	 * @param path cast=(GtkTreePath **)
	 * @param column cast=(GtkTreeViewColumn **)
	 * @param cell_x cast=(gint *)
	 * @param cell_y cast=(gint *)
	 */
	public static final native boolean gtk_tree_view_get_path_at_pos(long tree_view, int x, int y, long [] path, long [] column, int[] cell_x, int[] cell_y);
	/** @param tree_view cast=(GtkTreeView *) */
	public static final native long gtk_tree_view_get_selection(long tree_view);
	/**
	 * @param tree_view cast=(GtkTreeView *)
	 * @param visible_rect flags=no_in
	 */
	public static final native void gtk_tree_view_get_visible_rect(long tree_view, GdkRectangle visible_rect);
	/**
	 * @param tree_view cast=(GtkTreeView *)
	 * @param column cast=(GtkTreeViewColumn *)
	 * @param position cast=(gint)
	 */
	public static final native int gtk_tree_view_insert_column(long tree_view, long column, int position);
	/**
	 * @param tree_view cast=(GtkTreeView *)
	 * @param column cast=(GtkTreeViewColumn *)
	 * @param base_column cast=(GtkTreeViewColumn *)
	 */
	public static final native void gtk_tree_view_move_column_after(long tree_view, long column, long base_column);
	/** @param model cast=(GtkTreeModel *) */
	public static final native long gtk_tree_view_new_with_model(long model);
	/**
	 * @param tree_view cast=(GtkTreeView *)
	 * @param column cast=(GtkTreeViewColumn *)
	 */
	public static final native void gtk_tree_view_remove_column(long tree_view, long column);
	/**
	 * @param view cast=(GtkTreeView *)
	 * @param path cast=(GtkTreePath *)
	 */
	public static final native boolean gtk_tree_view_row_expanded(long view, long path);
	/**
	 * @param tree_view cast=(GtkTreeView *)
	 * @param path cast=(GtkTreePath *)
	 * @param column cast=(GtkTreeViewColumn *)
	 * @param use_align cast=(gboolean)
	 * @param row_aligh cast=(gfloat)
	 * @param column_align cast=(gfloat)
	 */
	public static final native void gtk_tree_view_scroll_to_cell(long tree_view, long path, long column, boolean use_align, float row_aligh, float column_align);
	/**
	 * @param tree_view cast=(GtkTreeView *)
	 * @param tree_x cast=(gint)
	 * @param tree_y cast=(gint)
	 */
	public static final native void gtk_tree_view_scroll_to_point(long tree_view, int tree_x, int tree_y);
	/**
	 * @param tree_view cast=(GtkTreeView *)
	 * @param path cast=(GtkTreePath *)
	 * @param focus_column cast=(GtkTreeViewColumn *)
	 */
	public static final native void gtk_tree_view_set_cursor(long tree_view, long path, long focus_column, boolean start_editing);
	/**
	 * @param tree_view cast=(GtkTreeView*)
	 * @param grid_lines cast=(GtkTreeViewGridLines)
	 */
	public static final native void gtk_tree_view_set_grid_lines(long tree_view, int grid_lines);
	/**
	 * @param tree_view cast=(GtkTreeView*)
	 */
	public static final native int gtk_tree_view_get_grid_lines(long tree_view);
	/**
	 * @param tree_view cast=(GtkTreeView *)
	 * @param visible cast=(gboolean)
	 */
	public static final native void gtk_tree_view_set_headers_visible(long tree_view, boolean visible);
	/**
	 * @param tree_view cast=(GtkTreeView *)
	 * @param model cast=(GtkTreeModel *)
	 */
	public static final native void gtk_tree_view_set_model(long tree_view, long model);
	/**
	 * @param tree_view cast=(GtkTreeView *)
	 * @param column cast=(gint)
	 */
	public static final native void gtk_tree_view_set_search_column(long tree_view, int column);
	/**
	 * @param tree_view cast=(GtkTreeView *)
	 * @param bx cast=(gint)
	 * @param by cast=(gint)
	 * @param tx cast=(gint *)
	 * @param ty cast=(gint *)
	 */
	public static final native void gtk_tree_view_convert_bin_window_to_tree_coords(long tree_view, int bx, int by, int[] tx, int[] ty);
	/**
	 * @param viewport cast=(GtkViewport *)
	 * @param type cast=(GtkShadowType)
	 */
	public static final native void gtk_viewport_set_shadow_type(long viewport, int type);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param accel_signal cast=(const gchar *)
	 * @param accel_group cast=(GtkAccelGroup *)
	 * @param accel_key cast=(guint)
	 * @param accel_mods cast=(GdkModifierType)
	 */
	public static final native void gtk_widget_add_accelerator(long widget, byte[] accel_signal, long accel_group, int accel_key, int accel_mods, int accel_flags);
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 * @param events cast=(gint)
	 */
	/* [GTK3 only] */
	public static final native void gtk_widget_add_events(long widget, int events);
	/** @param widget cast=(GtkWidget *) */
	public static final native boolean gtk_widget_child_focus(long widget, int direction);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param text cast=(const gchar *)
	 */
	public static final native long gtk_widget_create_pango_layout(long widget, byte[] text);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param text cast=(const gchar *)
	 */
	public static final native long gtk_widget_create_pango_layout(long widget, long text);
	/** @param widget cast=(GtkWidget *) */
	public static final native void gtk_widget_destroy(long widget);
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 * @param cr cast=(cairo_t *)
	 */
	/* [GTK3 only] */
	public static final native void gtk_widget_draw(long widget, long cr);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param event cast=(GdkEvent *)
	 */
	public static final native boolean gtk_widget_event(long widget, long event);
	/** @param widget cast=(GtkWidget *) */
	public static final native long gtk_widget_get_accessible(long widget);
	/** @param widget cast=(GtkWidget *) */
	public static final native boolean gtk_widget_get_visible(long widget);
	/** @param widget cast=(GtkWidget *) */
	public static final native boolean gtk_widget_get_realized(long widget);
	/**
	 * @param widget cast=(GtkWidget *)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native boolean gtk_widget_get_has_window(long widget);
	/**
	 * @param widget cast=(GtkWidget *)
	 */
	/* [GTK4 only, if-def'd in os.h] */
	public static final native boolean gtk_widget_get_has_surface(long widget);
	/** @param widget cast=(GtkWidget *) */
	public static final native boolean gtk_widget_get_can_default(long widget);
	/** @param widget cast=(GtkWidget *) */
	public static final native boolean gtk_widget_get_child_visible(long widget);
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 */
	/* [GTK3 only] */
	public static final native int gtk_widget_get_events(long widget);
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 */
	/* [GTK3 only] */
	public static final native long gtk_widget_get_window(long widget);
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 */
	/* [GTK4 only] */
	public static final native long gtk_widget_get_surface(long widget);
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native int gtk_widget_get_margin_start(long widget);
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native int gtk_widget_get_margin_end(long widget);
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native int gtk_widget_get_margin_top(long widget);
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native int gtk_widget_get_margin_bottom(long widget);
	/** @param widget cast=(GtkWidget *)  */
	public static final native boolean gtk_widget_get_mapped(long widget);
	/** @param widget cast=(GtkWidget *) */
	public static final native long gtk_widget_get_pango_context(long widget);
	/** @param widget cast=(GtkWidget *) */
	public static final native long gtk_widget_get_parent(long widget);
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native long gtk_widget_get_parent_window(long widget);
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native long gtk_widget_get_parent_surface(long widget);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param allocation cast=(GtkAllocation *),flags=no_in
	 * */
	public static final native void gtk_widget_get_allocation(long widget, GtkAllocation allocation);
	/**
	 * @param context cast=(GtkStyleContext *)
	 * @param cr cast=(cairo_t *)
	 * @param x cast=(gdouble)
	 * @param y cast=(gdouble)
	 * @param width cast=(gdouble)
	 * @param height cast=(gdouble)
	 */
	public static final native void gtk_render_handle(long context, long cr, double x , double y, double width, double height);
	/**
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native long gtk_widget_get_style_context(long widget);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param width cast=(gint *)
	 * @param height cast=(gint *)
	 */
	public static final native void gtk_widget_get_size_request(long widget, int [] width, int [] height);
	/** @param widget cast=(GtkWidget *) */
	public static final native long gtk_widget_get_toplevel(long widget);
	/** @param widget cast=(GtkWidget *) */
	public static final native long gtk_widget_get_tooltip_text(long widget);
	/** @param widget cast=(GtkWidget *) */
	public static final native void gtk_widget_grab_focus(long widget);
	/** @param widget cast=(GtkWidget *) */
	public static final native boolean gtk_widget_has_focus(long widget);
	/** @param widget cast=(GtkWidget *) */
	public static final native void gtk_widget_hide(long widget);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param region cast=(cairo_region_t *)
	 */
	public static final native void gtk_widget_input_shape_combine_region(long widget, long region);
	/** @param widget cast=(GtkWidget *) */
	public static final native boolean gtk_widget_is_focus(long widget);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param orientation cast=(GtkOrientation)
	 * @param for_size cast=(int)
	 * @param minimum cast=(int *)
	 * @param natural cast=(int *)
	 * @param minimum_baseline cast=(int *)
	 * @param natural_baseline cast=(int *)
	 */
	/* [GTK4 only, if-def'd in os.h] */
	public static final native void gtk_widget_measure(long widget, int orientation, int for_size, int[] minimum, int[] natural, int[] minimum_baseline, int[] natural_baseline);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param group_cycling cast=(gboolean)
	 */
	public static final native boolean gtk_widget_mnemonic_activate(long widget, boolean group_cycling);
	/**
	 * @method flags=dynamic
	 */
	/* [GTK3; 3.16 deprecated] */
	public static final native void gtk_widget_override_font(long widget, long font);
	/** @param widget cast=(GtkWidget *) */
	public static final native void gtk_widget_queue_resize(long widget);
	/** @param widget cast=(GtkWidget *) */
	public static final native void gtk_widget_realize(long widget);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param accel_group cast=(GtkAccelGroup *)
	 * @param accel_key cast=(guint)
	 * @param accel_mods cast=(GdkModifierType)
	 */
	public static final native void gtk_widget_remove_accelerator(long widget, long accel_group, int accel_key, int accel_mods);
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 * @param new_parent cast=(GtkWidget *)
	 */
	/* deprecated as of 3.14 */
	public static final native void gtk_widget_reparent(long widget, long new_parent);
	/** @param dir cast=(GtkTextDirection) */
	public static final native void gtk_widget_set_default_direction(int dir);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param can_default cast=(gboolean)
	 */
	public static final native void gtk_widget_set_can_default(long widget, boolean can_default);
	/** @param widget cast=(GtkWidget *) */
	public static final native void gtk_widget_queue_draw(long widget);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param can_focus cast=(gboolean)
	 */
	public static final native void gtk_widget_set_can_focus(long widget, boolean can_focus);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param cursor cast=(GdkCursor *)
	 */
	/* [GTK4 only, if-def'd in os.h] */
	public static final native void gtk_widget_set_cursor(long widget, long cursor);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param visible cast=(gboolean)
	 */
	public static final native void gtk_widget_set_visible(long widget, boolean visible);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param dir cast=(GtkTextDirection)
	 */
	public static final native void gtk_widget_set_direction(long widget, int dir);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param receives_default cast=(gboolean)
	 */
	public static final native void gtk_widget_set_receives_default(long widget, boolean receives_default);
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 * @param double_buffered cast=(gboolean)
	 */
	/* [GTK3 only; 3.14 deprecated] */
	public static final native void gtk_widget_set_double_buffered(long widget, boolean double_buffered);
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 * @param val cast=(gboolean)
	 */
	public static final native void gtk_widget_set_focus_on_click(long widget, boolean val);
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native void gtk_widget_set_opacity(long widget, double opacity);
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native double gtk_widget_get_opacity(long widget);
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 * @param redraw cast=(gboolean)
	 */
	/* [GTK3 only] */
	public static final native void gtk_widget_set_redraw_on_allocate(long widget, boolean redraw);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param sensitive cast=(gboolean)
	 */
	public static final native void gtk_widget_set_sensitive(long widget, boolean sensitive);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param width cast=(gint)
	 * @param height cast=(gint)
	 */
	public static final native void gtk_widget_set_size_request(long widget, int width, int height);
	/** @param widget cast=(GtkWidget *) */
	public static final native void gtk_widget_show(long widget);
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 * @param allocation cast=(GtkAllocation *),flags=no_out
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gtk_widget_size_allocate(long widget, GtkAllocation allocation);
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 * @param allocation cast=(GtkAllocation *),flags=no_out
	 */
	/* [GTK4 only, if-def'd in os.h] */
	public static final native void gtk_widget_size_allocate(long widget, GtkAllocation allocation, int baseline);
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 * @param allocation cast=(GtkAllocation *),flags=no_out
	 */
	/* [GTK3 only] */
	public static final native void gtk_widget_set_allocation(long widget, GtkAllocation allocation);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param property_name cast=(const gchar *)
	 * @param terminator cast=(const gchar *),flags=sentinel
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gtk_widget_style_get(long widget, byte[] property_name, int[] value, long terminator);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param property_name cast=(const gchar *)
	 * @param terminator cast=(const gchar *),flags=sentinel
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gtk_widget_style_get(long widget, byte[] property_name, long[] value, long terminator);
	/**
	 * @param src_widget cast=(GtkWidget *)
	 * @param dest_widget cast=(GtkWidget *)
	 * @param dest_x cast=(gint *)
	 * @param dest_y cast=(gint *)
	 */
	public static final native boolean gtk_widget_translate_coordinates(long src_widget, long dest_widget, int src_x, int src_y, int[] dest_x, int[] dest_y);
	/** @param window cast=(GtkWindow *) */
	public static final native boolean gtk_window_activate_default(long window);
	/**
	 * @param window cast=(GtkWindow *)
	 * @param accel_group cast=(GtkAccelGroup *)
	 */
	public static final native void gtk_window_add_accel_group(long window, long accel_group);
	/** @param handle cast=(GtkWindow *) */
	public static final native void gtk_window_deiconify(long handle);
	/** @param window cast=(GtkWindow *) */
	public static final native long gtk_window_get_focus(long window);
	/**
	 * @param window cast=(GtkWindow *)
	 */
	public static final native long gtk_window_get_group(long window);
	/** @param window cast=(GtkWindow *) */
	public static final native long gtk_window_get_icon_list(long window);
	/** @param window cast=(GtkWindow *) */
	public static final native boolean gtk_window_get_modal(long window);
	/** @param window cast=(GtkWindow *) */
	public static final native int gtk_window_get_mnemonic_modifier(long window);
	/**
	 * @param handle cast=(GtkWindow *)
	 * @param x cast=(gint *)
	 * @param y cast=(gint *)
	 */
	public static final native void gtk_window_get_position(long handle, int[] x, int[] y);
	/**
	 * @param group cast=(GtkWindowGroup*)
	 * @param window cast=(GtkWindow*)
	 */
	public static final native void gtk_window_group_add_window(long group, long window);
	/**
	 * @param group cast=(GtkWindowGroup*)
	 * @param window cast=(GtkWindow*)
	 */
	public static final native void gtk_window_group_remove_window(long group, long window);
	public static final native long gtk_window_group_new();
	/** @param handle cast=(GtkWindow *) */
	public static final native boolean gtk_window_is_active(long handle);
	/** @param handle cast=(GtkWindow *) */
	public static final native void gtk_window_iconify(long handle);
	public static final native long gtk_window_list_toplevels();
	/** @param handle cast=(GtkWindow *) */
	public static final native void gtk_window_maximize(long handle);
	/** @param handle cast=(GtkWindow *) */
	public static final native void gtk_window_fullscreen(long handle);
	/** @param handle cast=(GtkWindow *) */
	public static final native void gtk_window_unfullscreen(long handle);
	/**
	 * @param handle cast=(GtkWindow *)
	 * @param x cast=(gint)
	 * @param y cast=(gint)
	 */
	public static final native void gtk_window_move(long handle, int x, int y);
	/** @param type cast=(GtkWindowType) */
	public static final native long gtk_window_new(int type);
	/**
	 * @param window cast=(GtkWindow *)
	 * @param accel_group cast=(GtkAccelGroup *)
	 */
	public static final native void gtk_window_remove_accel_group(long window, long accel_group);
	/**
	 * @param handle cast=(GtkWindow *)
	 * @param x cast=(gint)
	 * @param y cast=(gint)
	 */
	public static final native void gtk_window_resize(long handle, int x, int y);
	/**
	 * @param handle cast=(GtkWindow *)
	 * @param width cast=(gint *)
	 * @param height cast=(gint *)
	 */
	public static final native void gtk_window_get_size(long handle, int[] width, int[] height);
	/**
	 * @param window cast=(GtkWindow *)
	 * @param attach_widget cast=(GtkWidget *)
	 */
	public static final native void gtk_window_set_attached_to(long window, long attach_widget);
	/**
	 * @param window cast=(GtkWindow *)
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native void gtk_window_set_default(long window, long widget);
	/**
	 * @param window cast=(GtkWindow *)
	 * @param decorated cast=(gboolean)
	 */
	public static final native void gtk_window_set_decorated(long window, boolean decorated);
	/**
	 * @param window cast=(GtkWindow *)
	 * @param setting cast=(gboolean)
	 */
	public static final native void gtk_window_set_destroy_with_parent(long window, boolean setting);
	/**
	 * @param window cast=(GtkWindow *)
	 * @param geometry_widget cast=(GtkWidget *)
	 * @param geometry flags=no_out
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gtk_window_set_geometry_hints(long window, long geometry_widget, GdkGeometry geometry, int geom_mask);
	/**
	 * @param window cast=(GtkWindow *)
	 * @param list cast=(GList *)
	 */
	public static final native void gtk_window_set_icon_list(long window, long list);
	/**
	 * @param window cast=(GtkWindow *)
	 * @param setting cast=(gboolean)
	 */
	public static final native void gtk_window_set_keep_above(long window, boolean setting);
	/**
	 * @param window cast=(GtkWindow *)
	 * @param modal cast=(gboolean)
	 */
	public static final native void gtk_window_set_modal(long window, boolean modal);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param tip_text cast=(const gchar *)
	 */
	public static final native void gtk_widget_set_tooltip_text(long widget, byte[] tip_text);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param parent_window cast=(GdkWindow *)
	 */
	/* [GTK3 only, if-def'd in os.h] */
	public static final native void gtk_widget_set_parent_window(long widget, long parent_window);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param parent_surface cast=(GdkSurface *)
	 */
	/* [GTK4 only, if-def'd in os.h] */
	public static final native void gtk_widget_set_parent_surface(long widget, long parent_surface);
	/**
	 * @param window cast=(GtkWindow *)
	 * @param resizable cast=(gboolean)
	 */
	public static final native void gtk_window_set_resizable(long window, boolean resizable);
	/**
	 * @param window cast=(GtkWindow *)
	 * @param title cast=(const gchar *)
	 */
	public static final native void gtk_window_set_title(long window, byte[] title);
	/**
	 * @param window cast=(GtkWindow *)
	 * @param skips_taskbar cast=(gboolean)
	 */
	public static final native void gtk_window_set_skip_taskbar_hint(long window, boolean skips_taskbar);
	/** @param window cast=(GtkWindow *) */
	public static final native void gtk_window_set_type_hint(long window, int hint);
	/**
	 * @param window cast=(GtkWindow *)
	 * @param parent cast=(GtkWindow *)
	 */
	public static final native void gtk_window_set_transient_for(long window, long parent);
	/** @param handle cast=(GtkWindow *) */
	public static final native void gtk_window_unmaximize(long handle);
	/** @method flags=dynamic */
	public static final native long gtk_printer_option_widget_get_type();
	/**
	 * @method flags=dynamic
	 * @param widget cast=(GtkWidget *)
	 * @param region cast=(cairo_region_t *)
	 */
	/* [GTK3 only] */
	public static final native void gtk_widget_shape_combine_region(long widget, long region);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param child cast=(GtkWidget *)
	 * @param snapshot cast=(GtkSnapshot *)
	 */
	/* [GTK4 only, if-def'd in os.h] */
	public static final native void gtk_widget_snapshot_child(long widget, long child, long snapshot);
}
