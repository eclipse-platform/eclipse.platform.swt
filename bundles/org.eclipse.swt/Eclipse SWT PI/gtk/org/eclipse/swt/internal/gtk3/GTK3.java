/*******************************************************************************
 * Copyright (c) 2021 Syntevo and others.
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

/**
 * This class contains native functions that are present in GTK3 only.
 */
public class GTK3 {
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
	public static final native boolean gtk_events_pending ();

	/* GtkWindow */
	/**
	 * @param window cast=(GtkWindow *)
	 * @param list cast=(GList *)
	 */
	public static final native void gtk_window_set_icon_list(long window, long list);

	/* GtkWidget */
	/** @param widget cast=(GtkWidget *) */
	public static final native long gtk_widget_get_accessible(long widget);

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

	/* GtkTargetList */
	/**
	 * @param targets cast=(const GtkTargetEntry *)
	 * @param ntargets cast=(guint)
	 */
	public static final native long gtk_target_list_new(long targets, int ntargets);
	/** @param list cast=(GtkTargetList *) */
	public static final native void gtk_target_list_unref(long list);
}
