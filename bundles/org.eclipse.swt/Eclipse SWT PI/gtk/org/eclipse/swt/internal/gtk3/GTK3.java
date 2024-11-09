/*******************************************************************************
 * Copyright (c) 2021, 2024 Syntevo and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Syntevo - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.gtk3;

import org.eclipse.swt.internal.gtk.*;

/**
 * This class contains native functions that are present in GTK3 only.
 */
public class GTK3 {

	/* Macros */
	public static final native boolean GTK_IS_MENU_ITEM(long obj);
	/** @method flags=const */
	public static final native long GTK_TYPE_MENU();

	/**
	 * @param context cast=(GtkIMContext *)
	 * @param event cast=(GdkEventKey *)
	 */
	public static final native boolean gtk_im_context_filter_keypress(long context, long event);

	/* GtkButton */
	/**
	 * @param button cast=(GtkButton *)
	 * @param image cast=(GtkWidget *)
	 */
	public static final native void gtk_button_set_image(long button, long image);

	/* GtkAccelLabel */
	/**
	 * @param label cast=(const gchar *)
	 */
	public static final native long gtk_accel_label_new(byte[] label);
	/**
	 * @param accel_label cast=(GtkAccelLabel *)
	 * @param accel_widget cast=(GtkWidget *)
	 */
	public static final native void gtk_accel_label_set_accel_widget(long accel_label, long accel_widget);
	/**
	 * @param accel_label cast=(GtkAccelLabel *)
	 * @param accel_key cast=(guint)
	 * @param accel_mods cast=(GdkModifierType)
	 */
	public static final native void gtk_accel_label_set_accel(long accel_label, int accel_key, int accel_mods);

	/* GtkBin */
	/** @param bin cast=(GtkBin *) */
	public static final native long gtk_bin_get_child(long bin);

	/* GtkBox */
	/**
	 * @param box cast=(GtkBox *)
	 * @param child cast=(GtkWidget *)
	 */
	public static final native void gtk_box_set_child_packing(long box, long child, boolean expand, boolean fill, int padding, int pack_type);
	/**
	 * @param box cast=(GtkBox *)
	 * @param child cast=(GtkWidget *)
	 * @param position cast=(gint)
	 */
	public static final native void gtk_box_reorder_child(long box, long child, int position);
	/**
	 * @param box cast=(GtkBox *)
	 * @param widget cast=(GtkWidget *)
	 * @param expand cast=(gboolean)
	 * @param fill cast=(gboolean)
	 * @param padding cast=(guint)
	 */
	public static final native void gtk_box_pack_end(long box, long widget, boolean expand, boolean fill, int padding);

	/* GtkCalendar */
	/**
	 * @param calendar cast=(GtkCalendar *)
	 * @param month cast=(guint)
	 * @param year cast=(guint)
	 */
	public static final native void gtk_calendar_select_month(long calendar, int month, int year);
	/**
	 * @param calendar cast=(GtkCalendar *)
	 * @param day cast=(guint)
	 */
	public static final native void gtk_calendar_select_day(long calendar, int day);
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

	/* GtkColorChooser Interface */
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

	/* GtkContainer */
	/**
	 * @param container cast=(GtkContainer *)
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native void gtk_container_add(long container, long widget);
	// Do not confuse this function with gtk_container_foreach(..).
	// Make sure you know what you are doing when using this. Please be attentive to swt_fixed_forall(..)
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
	public static final native void gtk_container_propagate_draw(long container, long child, long cairo);
	/** @param container cast=(GtkContainer *) */
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
	public static final native void gtk_container_set_border_width(long container, int border_width);

	/* GtkDialog */
	/** @param dialog cast=(GtkDialog *) */
	public static final native int gtk_dialog_run(long dialog);

	/* GTK Initialization */
	/**
	 * @param argc cast=(int *)
	 * @param argv cast=(char ***)
	 */
	public static final native boolean gtk_init_check(long [] argc, long [] argv);

	/* GtkGrab */
	/** @param widget cast=(GtkWidget *) */
	public static final native void gtk_grab_add(long widget);
	public static final native long gtk_grab_get_current();
	/** @param widget cast=(GtkWidget *) */
	public static final native void gtk_grab_remove(long widget);

	/* Events */
	public static final native long gtk_get_current_event();
	/** @param state cast=(GdkModifierType*) */
	public static final native boolean gtk_get_current_event_state(int[] state);
	/** @param event cast=(GdkEvent *) */
	public static final native long gtk_get_event_widget(long event);
	/** @param event cast=(GdkEvent *) */
	public static final native void gtk_main_do_event(long event);
	public static final native boolean gtk_main_iteration_do(boolean blocking);
	public static final native boolean gtk_events_pending();

	/* GtkWindow */
	/**
	 * @param window cast=(GtkWindow *)
	 * @param list cast=(GList *)
	 */
	public static final native void gtk_window_set_icon_list(long window, long list);
	/**
	 * @param window cast=(GtkWindow *)
	 * @param accel_group cast=(GtkAccelGroup *)
	 */
	public static final native void gtk_window_add_accel_group(long window, long accel_group);
	/**
	 * @param window cast=(GtkWindow *)
	 * @param accel_group cast=(GtkAccelGroup *)
	 */
	public static final native void gtk_window_remove_accel_group(long window, long accel_group);
	/** @param handle cast=(GtkWindow *) */
	public static final native void gtk_window_deiconify(long handle);
	/** @param handle cast=(GtkWindow *) */
	public static final native void gtk_window_iconify(long handle);
	/**
	 * @param window cast=(GtkWindow *)
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native void gtk_window_set_default(long window, long widget);
	/** @param window cast=(GtkWindow *) */
	public static final native boolean gtk_window_activate_default(long window);
	/** @param window cast=(GtkWindow *) */
	public static final native void gtk_window_set_type_hint(long window, int hint);
	/**
	 * @param window cast=(GtkWindow *)
	 * @param skips_taskbar cast=(gboolean)
	 */
	public static final native void gtk_window_set_skip_taskbar_hint(long window, boolean skips_taskbar);
	/**
	 * @param window cast=(GtkWindow *)
	 * @param setting cast=(gboolean)
	 */
	public static final native void gtk_window_set_keep_above(long window, boolean setting);
	/** @param window cast=(GtkWindow *) */
	public static final native long gtk_window_get_icon_list(long window);
	/**
	 * @param window cast=(GtkWindow *)
	 * @param attach_widget cast=(GtkWidget *)
	 */
	public static final native void gtk_window_set_attached_to(long window, long attach_widget);
	/**
	 * @param handle cast=(GtkWindow *)
	 * @param x cast=(gint)
	 * @param y cast=(gint)
	 */
	public static final native void gtk_window_move(long handle, int x, int y);
	/** @param type cast=(GtkWindowType) */
	public static final native long gtk_window_new(int type);
	/**
	 * @param handle cast=(GtkWindow *)
	 * @param x cast=(gint *)
	 * @param y cast=(gint *)
	 */
	public static final native void gtk_window_get_position(long handle, int[] x, int[] y);
	/** @param window cast=(GtkWindow *) */
	public static final native int gtk_window_get_mnemonic_modifier(long window);
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
	 * @param geometry_widget cast=(GtkWidget *)
	 * @param geometry flags=no_out
	 */
	public static final native void gtk_window_set_geometry_hints(long window, long geometry_widget, GdkGeometry geometry, int geom_mask);

	/* GtkWidget */
	/** @param widget cast=(GtkWidget *) */
	public static final native long gtk_widget_get_accessible(long widget);
	/**
	 * @method flags=ignore_deprecations
	 * @param widget cast=(GtkWidget *)
	 * @param font cast=(const PangoFontDescription *)
	 */
	/* deprecated as of 3.16 */
	public static final native void gtk_widget_override_font(long widget, long font);
	/**
	 * @method flags=ignore_deprecations
	 * @param widget cast=(GtkWidget *)
	 * @param new_parent cast=(GtkWidget *)
	 */
	/* deprecated as of 3.14 */
	public static final native void gtk_widget_reparent(long widget, long new_parent);
	/**
	 * @method flags=ignore_deprecations
	 * @param widget cast=(GtkWidget *)
	 * @param double_buffered cast=(gboolean)
	 */
	/* deprecated as of 3.14 */
	public static final native void gtk_widget_set_double_buffered(long widget, boolean double_buffered);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param width cast=(gint)
	 * @param minimum_size cast=(gint *)
	 * @param natural_size cast=(gint *)
	 */
	public static final native void gtk_widget_get_preferred_height_for_width(long widget, int width, int[] minimum_size, int[] natural_size);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param minimum_size cast=(gint *)
	 * @param natural_size cast=(gint *)
	 */
	public static final native void gtk_widget_get_preferred_height(long widget, int[] minimum_size, int[] natural_size);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param height cast=(gint)
	 * @param minimum_size cast=(gint *)
	 * @param natural_size cast=(gint *)
	 */
	public static final native void gtk_widget_get_preferred_width_for_height(long widget, int height, int[] minimum_size, int[] natural_size);
	/** @param widget cast=(GtkWidget *) */
	public static final native long gtk_widget_get_screen(long widget);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param has_window cast=(gboolean)
	 */
	public static final native void gtk_widget_set_has_window(long widget, boolean has_window);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param accel_signal cast=(const gchar *)
	 * @param accel_group cast=(GtkAccelGroup *)
	 * @param accel_key cast=(guint)
	 * @param accel_mods cast=(GdkModifierType)
	 */
	public static final native void gtk_widget_add_accelerator(long widget, byte[] accel_signal, long accel_group, int accel_key, int accel_mods, int accel_flags);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param accel_group cast=(GtkAccelGroup *)
	 * @param accel_key cast=(guint)
	 * @param accel_mods cast=(GdkModifierType)
	 */
	public static final native void gtk_widget_remove_accelerator(long widget, long accel_group, int accel_key, int accel_mods);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param events cast=(gint)
	 */
	public static final native void gtk_widget_add_events(long widget, int events);
	/** @param widget cast=(GtkWidget *) */
	public static final native void gtk_widget_destroy(long widget);
	/** @param widget cast=(GtkWidget *) */
	public static final native int gtk_widget_get_events(long widget);
	/** @param widget cast=(GtkWidget *) */
	public static final native long gtk_widget_get_window(long widget);
	/** @param widget cast=(GtkWidget *) */
	public static final native long gtk_widget_get_toplevel(long widget);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param redraw cast=(gboolean)
	 */
	public static final native void gtk_widget_set_redraw_on_allocate(long widget, boolean redraw);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param event cast=(GdkEvent *)
	 */
	public static final native boolean gtk_widget_event(long widget, long event);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param cr cast=(cairo_t *)
	 */
	public static final native void gtk_widget_draw(long widget, long cr);
	/** @param widget cast=(GtkWidget *) */
	public static final native boolean gtk_widget_get_has_window(long widget);
	/** @param widget cast=(GtkWidget *) */
	public static final native boolean gtk_widget_get_can_default(long widget);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param can_default cast=(gboolean)
	 */
	public static final native void gtk_widget_set_can_default(long widget, boolean can_default);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param parent_window cast=(GdkWindow *)
	 */
	public static final native void gtk_widget_set_parent_window(long widget, long parent_window);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param region cast=(cairo_region_t *)
	 */
	public static final native void gtk_widget_shape_combine_region(long widget, long region);
	/**
	 * @param src_widget cast=(GtkWidget *)
	 * @param dest_widget cast=(GtkWidget *)
	 * @param dest_x cast=(gint *)
	 * @param dest_y cast=(gint *)
	 */
	public static final native boolean gtk_widget_translate_coordinates(long src_widget, long dest_widget, int src_x, int src_y, int[] dest_x, int[] dest_y);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param property_name cast=(const gchar *)
	 * @param terminator cast=(const gchar *),flags=sentinel
	 */
	public static final native void gtk_widget_style_get(long widget, byte[] property_name, int[] value, long terminator);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param property_name cast=(const gchar *)
	 * @param terminator cast=(const gchar *),flags=sentinel
	 */
	public static final native void gtk_widget_style_get(long widget, byte[] property_name, long[] value, long terminator);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param region cast=(cairo_region_t *)
	 */
	public static final native void gtk_widget_input_shape_combine_region(long widget, long region);
	/** @param widget cast=(GtkWidget *)*/
	public static final native void gtk_widget_set_clip(long widget, GtkAllocation allocation);
	/** @param widget cast=(GtkWidget *)*/
	public static final native void gtk_widget_get_clip(long widget, GtkAllocation allocation);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param allocation cast=(GtkAllocation *),flags=no_out
	 */
	public static final native void gtk_widget_set_allocation(long widget, GtkAllocation allocation);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param allocation cast=(GtkAllocation *),flags=no_out
	 */
	public static final native void gtk_widget_size_allocate(long widget, GtkAllocation allocation);

	/* Drag and Drop API */
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param targets cast=(GtkTargetList *)
	 * @param actions cast=(GdkDragAction)
	 * @param button cast=(gint)
	 * @param event cast=(GdkEvent *)
	 * @param x cast=(gint)
	 * @param y cast=(gint)
	 */
	public static final native long gtk_drag_begin_with_coordinates(long widget, long targets, int actions, int button, long event, int x, int y);
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
	public static final native void gtk_drag_dest_set(long widget, int flags, long targets, int n_targets, int actions);
	/** @param widget cast=(GtkWidget *) */
	public static final native void gtk_drag_dest_unset(long widget);
	/**
	 * @param context cast=(GdkDragContext *)
	 * @param success cast=(gboolean)
	 * @param delete cast=(gboolean)
	 * @param time cast=(guint32)
	 */
	public static final native void gtk_drag_finish(long context, boolean success, boolean delete, int time);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param context cast=(GdkDragContext *)
	 * @param target cast=(GdkAtom)
	 * @param time cast=(guint32)
	 */
	public static final native void gtk_drag_get_data(long widget, long context, long target, int time);
	/**
	 * @param context cast=(GdkDragContext *)
	 * @param surface cast=(cairo_surface_t *)
	 */
	public static final native void gtk_drag_set_icon_surface(long context, long surface);

	/* GtkFileChooser */
	/** @param chooser cast=(GtkFileChooser *) */
	public static final native long gtk_file_chooser_get_filename(long chooser);
	/** @param chooser cast=(GtkFileChooser *) */
	public static final native long gtk_file_chooser_get_filenames(long chooser);
	/** @param chooser cast=(GtkFileChooser *) */
	public static final native long gtk_file_chooser_get_uri(long chooser);
	/** @param chooser cast=(GtkFileChooser *) */
	public static final native long gtk_file_chooser_get_uris(long chooser);
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
	 * @param name cast=(const gchar *)
	 */
	public static final native void gtk_file_chooser_set_filename(long chooser, long name);
	/**
	 * @param chooser cast=(GtkFileChooser *)
	 * @param uri cast=(const char *)
	 */
	public static final native void gtk_file_chooser_set_uri(long chooser, byte [] uri);
	/**
	 * @param chooser cast=(GtkFileChooser *)
	 * @param extra_widget cast=(GtkWidget *)
	 */
	public static final native void gtk_file_chooser_set_extra_widget(long chooser, long extra_widget);
	/**
	 * @param chooser cast=(GtkFileChooser *)
	 * @param filter cast=(GtkFileFilter *)
	 */
	public static final native void gtk_file_chooser_add_filter(long chooser, long filter);
	/** @param chooser cast=(GtkFileChooser *) */
	public static final native long gtk_file_chooser_get_filter(long chooser);
	/**
	 * @param chooser cast=(GtkFileChooser *)
	 * @param name cast=(const gchar *)
	 */
	public static final native void gtk_file_chooser_set_current_name(long chooser, byte[] name);
	/**
	 * @param chooser cast=(GtkFileChooser *)
	 * @param filter cast=(GtkFileFilter *)
	 */
	public static final native void gtk_file_chooser_set_filter(long chooser, long filter);
	/**
	 * @param chooser cast=(GtkFileChooser *)
	 * @param select_multiple cast=(gboolean)
	 */
	public static final native void gtk_file_chooser_set_select_multiple(long chooser, boolean select_multiple);

	/* GtkFileChooserNative */
	/**
	 * @method flags=dynamic
	 * @param title cast=(const gchar *),flags=no_out
	 * @param parent cast=(GtkWindow *)
	 * @param accept_label cast=(const gchar *),flags=no_out
	 * @param cancel_label cast=(const gchar *),flags=no_out
	 */
	public static final native long gtk_file_chooser_native_new(byte[] title, long parent, int action, byte[] accept_label, byte[] cancel_label);

	/* GtkRadioButton */
	/** @param radio_button cast=(GtkRadioButton *) */
	public static final native long gtk_radio_button_get_group(long radio_button);
	/** @param group cast=(GSList *) */
	public static final native long gtk_radio_button_new(long group);

	/* GtkNativeDialog */
	/** @param dialog cast=(GtkNativeDialog *) */
	public static final native int gtk_native_dialog_run(long dialog);

	/* GtkScrolledWindow */
	/**
	 * @param hadjustment cast=(GtkAdjustment *)
	 * @param vadjustment cast=(GtkAdjustment *)
	 */
	public static final native long gtk_scrolled_window_new(long hadjustment, long vadjustment);
	/**
	 * @param scrolled_window cast=(GtkScrolledWindow *)
	 * @param type cast=(GtkShadowType)
	 */
	public static final native void gtk_scrolled_window_set_shadow_type(long scrolled_window, int type);
	/** @param scrolled_window cast=(GtkScrolledWindow *) */
	public static final native int gtk_scrolled_window_get_shadow_type(long scrolled_window);

	/* GtkClipboard */
	/** @param clipboard cast=(GtkClipboard *) */
	public static final native void gtk_clipboard_clear(long clipboard);
	/** @param selection cast=(GdkAtom) */
	public static final native long gtk_clipboard_get(long selection);
	/**
	 * @param clipboard cast=(GtkClipboard *)
	 * @param target cast=(const GtkTargetEntry *)
	 * @param n_targets cast=(guint)
	 * @param get_func cast=(GtkClipboardGetFunc)
	 * @param clear_func cast=(GtkClipboardClearFunc)
	 * @param user_data cast=(GObject *)
	 */
	public static final native boolean gtk_clipboard_set_with_owner(long clipboard, long target, int n_targets, long get_func, long clear_func, long user_data);
	/**
	 * @param clipboard cast=(GtkClipboard *)
	 * @param targets cast=(const GtkTargetEntry *)
	 * @param n_targets cast=(gint)
	 */
	public static final native void gtk_clipboard_set_can_store(long clipboard, long targets, int n_targets);
	/** @param clipboard cast=(GtkClipboard *) */
	public static final native void gtk_clipboard_store(long clipboard);
	/**
	 * @param clipboard cast=(GtkClipboard *)
	 * @param target cast=(GdkAtom)
	 */
	public static final native long gtk_clipboard_wait_for_contents(long clipboard, long target);

	/* GtkStatusIcon */
	/**
	 * @method flags=ignore_deprecations
	 * @param handle cast=(GtkStatusIcon*)
	 */
	public static final native boolean gtk_status_icon_get_visible(long handle);
	/** @method flags=ignore_deprecations */
	public static final native long gtk_status_icon_new();
	/**
	 * @method flags=ignore_deprecations
	 * @param handle cast=(GtkStatusIcon*)
	 * @param pixbuf cast=(GdkPixbuf*)
	 */
	public static final native void gtk_status_icon_set_from_pixbuf(long handle, long pixbuf);
	/**
	 * @method flags=ignore_deprecations
	 * @param handle cast=(GtkStatusIcon*)
	 * @param visible cast=(gboolean)
	 */
	public static final native void gtk_status_icon_set_visible(long handle, boolean visible);
	/**
	 * @method flags=ignore_deprecations
	 * @param handle cast=(GtkStatusIcon *)
	 * @param tip_text cast=(const gchar *)
	 */
	public static final native void gtk_status_icon_set_tooltip_text(long handle, byte[] tip_text);
	/**
	 * @method flags=ignore_deprecations
	 * @param handle cast=(GtkStatusIcon*)
	 * @param screen cast=(GdkScreen**)
	 * @param area cast=(GdkRectangle*)
	 * @param orientation cast=(GtkOrientation*)
	 */
	public static final native boolean gtk_status_icon_get_geometry(long handle, long screen, GdkRectangle area, long orientation);

	/* GtkTargetList */
	/**
	 * @param targets cast=(const GtkTargetEntry *)
	 * @param ntargets cast=(guint)
	 */
	public static final native long gtk_target_list_new(long targets, int ntargets);
	/** @param list cast=(GtkTargetList *) */
	public static final native void gtk_target_list_unref(long list);

	/* GtkSelectionData */
	/** @param selection_data cast=(GtkSelectionData *) */
	public static final native void gtk_selection_data_free(long selection_data);
	/** @param selection_data cast=(GtkSelectionData *) */
	public static final native long gtk_selection_data_get_data(long selection_data);
	/** @param selection_data cast=(GtkSelectionData *) */
	public static final native int gtk_selection_data_get_format(long selection_data);
	/** @param selection_data cast=(GtkSelectionData *) */
	public static final native int gtk_selection_data_get_length(long selection_data);
	/** @param selection_data cast=(GtkSelectionData *) */
	public static final native long gtk_selection_data_get_target(long selection_data);
	/** @param selection_data cast=(GtkSelectionData *) */
	public static final native long gtk_selection_data_get_data_type(long selection_data);
	/**
	 * @param selection_data cast=(GtkSelectionData *)
	 * @param type cast=(GdkAtom)
	 * @param format cast=(gint)
	 * @param data cast=(const guchar *)
	 * @param length cast=(gint)
	 */
	public static final native void gtk_selection_data_set(long selection_data, long type, int format, long data, int length);

	/* GtkMenu */
	public static final native long gtk_menu_new();
	/** @param menu cast=(GtkMenu *) */
	public static final native void gtk_menu_popdown(long menu);
	/**
	 * @param menu cast=(GtkMenu *)
	 * @param trigger_event cast=(const GdkEvent*)
	 */
	public static final native void gtk_menu_popup_at_pointer(long menu, long trigger_event);

	/* GtkMenuBar */
	public static final native long gtk_menu_bar_new();

	/* GtkMenuItem */
	/** @param menu_item cast=(GtkMenuItem *) */
	public static final native long gtk_menu_item_get_submenu(long menu_item);
	public static final native long gtk_menu_item_new();
	/**
	 * @param menu_item cast=(GtkMenuItem *)
	 * @param submenu cast=(GtkWidget *)
	 */
	public static final native void gtk_menu_item_set_submenu(long menu_item, long submenu);
	/** @param check_menu_item cast=(GtkCheckMenuItem *) */
	public static final native boolean gtk_check_menu_item_get_active(long check_menu_item);
	public static final native long gtk_check_menu_item_new();
	/**
	 * @param wid cast=(GtkCheckMenuItem *)
	 * @param active cast=(gboolean)
	 */
	public static final native void gtk_check_menu_item_set_active(long wid, boolean active);
	/** @param radio_menu_item cast=(GtkRadioMenuItem *) */
	public static final native long gtk_radio_menu_item_get_group(long radio_menu_item);
	/** @param group cast=(GSList *) */
	public static final native long gtk_radio_menu_item_new(long group);
	public static final native long gtk_separator_menu_item_new();
	/**
	 * @param menu cast=(GtkMenu *)
	 * @param rect_window cast=(GdkWindow *)
	 * @param rect cast=(GdkRectangle *)
	 * @param rect_anchor cast=(GdkGravity)
	 * @param menu_anchor cast=(GdkGravity)
	 * @param trigger_event cast=(const GdkEvent *)
	 */
	public static final native void gtk_menu_popup_at_rect(long menu, long rect_window, GdkRectangle rect, int rect_anchor, int menu_anchor, long trigger_event);

	/* GtkMenuShell */
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

	/* GtkToolbar */
	public static final native long gtk_toolbar_new();
	/**
	 * @param toolbar cast=(GtkToolbar *)
	 * @param item cast=(GtkToolItem *)
	 */
	public static final native void gtk_toolbar_insert(long toolbar, long item, int pos);
	/**
	 * @param toolbar cast=(GtkToolbar *)
	 * @param show_arrow cast=(gboolean)
	 */
	public static final native void gtk_toolbar_set_show_arrow(long toolbar, boolean show_arrow);
	/**
	 * @param toolbar cast=(GtkToolbar *)
	 * @param style cast=(GtkToolbarStyle)
	 */
	public static final native void gtk_toolbar_set_style(long toolbar, int style);
	/** @param toolbar cast=(GtkToolbar *)*/
	public static final native void gtk_toolbar_set_icon_size(long toolbar, int size);

	/* GtkToolItem */
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

	/* GtkSeparatorToolItem */
	public static final native long gtk_separator_tool_item_new();
	/**
	 * @param item cast=(GtkSeparatorToolItem *)
	 * @param draw cast=(gboolean)
	 */
	public static final native void gtk_separator_tool_item_set_draw(long item, boolean draw);

	/* GtkToolButton */
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

	/* GtkToggleToolButton */
	/** @param button cast=(GtkToggleToolButton *) */
	public static final native boolean gtk_toggle_tool_button_get_active(long button);
	public static final native long gtk_toggle_tool_button_new();
	/**
	 * @param item cast=(GtkToggleToolButton *)
	 * @param selected cast=(gboolean)
	 */
	public static final native void gtk_toggle_tool_button_set_active(long item, boolean selected);

	/* GtkMenuToolButton */
	/**
	 * @param icon_widget cast=(GtkWidget *)
	 * @param label cast=(const gchar *)
	 */
	public static final native long gtk_menu_tool_button_new(long icon_widget, byte[] label);

	/* GtkIconTheme */
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
	public static final native long gtk_icon_theme_get_default();
	/**
	 * @param icon_info cast=(GtkIconInfo *)
	 * @param error cast=(GError **)
	 */
	public static final native long gtk_icon_info_load_icon(long icon_info, long error[]);

	/* GtkEditable Interface */
	/** @param editable cast=(GtkEditable *) */
	public static final native void gtk_editable_copy_clipboard(long editable);
	/** @param editable cast=(GtkEditable *) */
	public static final native void gtk_editable_cut_clipboard(long editable);
	/** @param editable cast=(GtkEditable *) */
	public static final native void gtk_editable_paste_clipboard(long editable);

	/* GtkEntry */
	/**
	 * @param self cast=(GtkEntry *)
	 * @param n_chars cast=(gint)
	 */
	public static final native void gtk_entry_set_width_chars(long self, int n_chars);
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
	/** @param entry cast=(GtkEntry *) */
	public static final native long gtk_entry_get_text(long entry);
	/**
	 * @param entry cast=(GtkEntry *)
	 * @param text cast=(const gchar *)
	 */
	public static final native void gtk_entry_set_text(long entry, byte[] text);

	/* GtkEventController */
	/**
	 * @param gesture cast=(GtkEventController *)
	 * @param event cast=(const GdkEvent *)
	 */
	public static final native void gtk_event_controller_handle_event(long gesture, long event);

	/* GtkFrame */
	/**
	 * @param frame cast=(GtkFrame *)
	 * @param type cast=(GtkShadowType)
	 */
	public static final native void gtk_frame_set_shadow_type(long frame, int type);

	/* GtkViewport */
	/**
	 * @param viewport cast=(GtkViewport *)
	 * @param type cast=(GtkShadowType)
	 */
	public static final native void gtk_viewport_set_shadow_type(long viewport, int type);

	/* GtkAccessible */
	/** @param accessible cast=(GtkAccessible *) */
	public static final native long gtk_accessible_get_widget(long accessible);

	/* GtkComboBox */
	/**
	 * @param combo_box cast=(GtkComboBox *)
	 * @param width cast=(gint)
	 */
	/* Do not use directly. Instead use Combo.gtk_combo_box_toggle_wrap(..) */
	public static final native void gtk_combo_box_set_wrap_width(long combo_box, int width);
	/**
	 * @param combo_box cast=(GtkComboBox *)
	 * @return cast=(gint)
	 */
	public static final native int gtk_combo_box_get_wrap_width(long combo_box);

	/* GtkEventBox */
	public static final native long gtk_event_box_new();

	/* GtkImage */
	/**
	 * @param image cast=(GtkImage *)
	 * @param surface cast=(cairo_surface_t *)
	 */
	public static final native void gtk_image_set_from_surface(long image, long surface);
	/**
	 * @param icon_name cast=(const gchar *)
	 * @param size cast=(GtkIconSize)
	 */
	public static final native long gtk_image_new_from_icon_name(byte[] icon_name, int size);
	/**
	 * @param image cast=(GtkImage *)
	 * @param icon_name cast=(const gchar *)
	 * @param size cast=(GtkIconSize)
	 */
	public static final native void gtk_image_set_from_icon_name(long image, byte[] icon_name, int size);
	/** @param surface cast=(cairo_surface_t *) */
	public static final native long gtk_image_new_from_surface(long surface);

	/* GtkCssProvider */
	/**
	 * @param css_provider cast=(GtkCssProvider *)
	 * @param data cast=(const gchar *)
	 * @param length cast=(gssize)
	 * @param error cast=(GError **)
	 */
	public static final native boolean gtk_css_provider_load_from_data(long css_provider, byte[] data, long length, long error[]);

	/* GtkStyleContext */
	/**
	 * @method flags=ignore_deprecations
	 * @param context cast=(GtkStyleContext *)
	 */
	public static final native void gtk_style_context_invalidate(long context);
	/**
	 * @param screen cast=(GdkScreen *)
	 * @param provider cast=(GtkStyleProvider *)
	 * @param priority cast=(guint)
	 */
	public static final native void gtk_style_context_add_provider_for_screen(long screen, long provider, int priority);
	/**
	 * @method flags=ignore_deprecations
	 * @param context cast=(GtkStyleContext *)
	 * @param state cast=(GtkStateFlags)
	 */
	/* [GTK3; 3.8 deprecated, replaced] */
	public static final native long gtk_style_context_get_font(long context, int state);
	/** @param context cast=(GtkStyleContext *) */
	public static final native long gtk_style_context_get_parent(long context);
	/**
	 * @param context cast=(GtkStyleContext *)
	 * @param state cast=(GtkStateFlags)
	 * @param property cast=(const gchar *),flags=no_out
	 * @param terminator cast=(const gchar *),flags=sentinel
	 */
	public static final native void gtk_style_context_get(long context, int state, byte [] property, long [] value, long terminator);
	/**
	 * @param context cast=(GtkStyleContext *)
	 * @param state cast=(GtkStateFlags)
	 * @param padding cast=(GtkBorder *),flags=no_in
	 */
	public static final native void gtk_style_context_get_padding(long context, int state, GtkBorder padding);
	/**
	 * @param context cast=(GtkStyleContext *)
	 * @param state cast=(GtkStateFlags)
	 * @param color cast=(GdkRGBA *)
	 */
	public static final native void gtk_style_context_get_color(long context, int state, GdkRGBA color);
	/**
	 * @param context cast=(GtkStyleContext *)
	 * @param state cast=(GtkStateFlags)
	 * @param padding cast=(GtkBorder *),flags=no_in
	 */
	public static final native void gtk_style_context_get_border(long context, int state, GtkBorder padding);

	/* GtkLabel */
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

	/* GtkTextView */
	/**
	 * @param text_view cast=(GtkTextView *)
	 * @param win cast=(GtkTextWindowType)
	 */
	public static final native long gtk_text_view_get_window(long text_view, int win);

	/* GtkToggleButton */
	/**
	 * @param toggle_button cast=(GtkToggleButton *)
	 * @param setting cast=(gboolean)
	 */
	public static final native void gtk_toggle_button_set_inconsistent(long toggle_button, boolean setting);

	/* GtkTreeView */
	/** @param tree_view cast=(GtkTreeView *) */
	public static final native long gtk_tree_view_get_bin_window(long tree_view);

	/* GtkTreeViewColumn */
	/**
	 * @param tree_column cast=(GtkTreeViewColumn *)
	 * @param cell_area cast=(GdkRectangle *),flags=no_in
	 * @param x_offset cast=(gint *)
	 * @param y_offset cast=(gint *)
	 * @param width cast=(gint *)
	 * @param height cast=(gint *)
	 */
	public static final native void gtk_tree_view_column_cell_get_size(long tree_column, GdkRectangle cell_area, int[] x_offset, int[] y_offset, int[] width, int[] height);

	/* GdkWindow */
	/**
	 * @param parent cast=(GdkWindow *)
	 * @param attributes flags=no_out
	 */
	public static final native long gdk_window_new(long parent, GdkWindowAttr attributes, int attributes_mask);

	/* Memmove */
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
	/**
	 * @param dest cast=(void *)
	 * @param src cast=(const void *),flags=no_out
	 * @param size cast=(size_t)
	 */
	public static final native void memmove(long dest, GtkTargetEntry src, long size);
	/**
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native long gtk_gesture_rotate_new(long widget);
	/**
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native long gtk_gesture_zoom_new(long widget);
	/**
	 * @param widget cast=(GtkWidget *)
	 */
	public static final native long gtk_gesture_drag_new(long widget);

	/* GtkFontChooserDialog */
	/**
	 * @param title cast=(const gchar *)
	 * @param parent cast=(GtkWindow *)
	 */
	public static final native long gtk_font_chooser_dialog_new(byte[] title, long parent);

	/* GtkFontChooser Interface */
	/** @param fontchooser cast=(GtkFontChooser *) */
	public static final native long gtk_font_chooser_get_font(long fontchooser);
	/**
	 * @param fsd cast=(GtkFontChooser *)
	 * @param fontname cast=(const gchar *)
	 */
	public static final native void gtk_font_chooser_set_font(long fsd, byte[] fontname);

	/* Sizeof */
	public static final native int GtkTargetEntry_sizeof();
	public static final native int GdkEvent_sizeof();
	public static final native int GdkEventButton_sizeof();
	public static final native int GdkEventCrossing_sizeof();
	public static final native int GdkEventFocus_sizeof();
	public static final native int GdkEventKey_sizeof();
	public static final native int GdkEventMotion_sizeof();
	public static final native int GdkEventWindowState_sizeof();
	public static final native int GdkGeometry_sizeof();
	public static final native int GdkWindowAttr_sizeof();

}
