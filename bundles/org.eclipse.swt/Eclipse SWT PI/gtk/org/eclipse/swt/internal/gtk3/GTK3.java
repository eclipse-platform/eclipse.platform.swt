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
}
