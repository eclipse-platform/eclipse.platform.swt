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
package org.eclipse.swt.internal.gtk4;

/**
 * This class contains native functions that are present in GTK4 only.
 */
public class GTK4 {
	/**
	 * @param context cast=(GtkIMContext *)
	 * @param event cast=(GdkEvent *)
	 */
	public static final native boolean gtk_im_context_filter_keypress(long context, long event);


	/* GtkButton */
	/** @param icon_name cast=(const gchar *) */
	public static final native long gtk_button_new_from_icon_name(byte[] icon_name);
	/**
	 * @param button cast=(GtkButton *)
	 * @param child cast=(GtkWidget *)
	 */
	public static final native void gtk_button_set_child(long button, long child);


	/* GtkColorChooser Interface */
	/**
	 * @param r cast=(float *)
	 * @param g cast=(float *)
	 * @param b cast=(float *)
	 */
	public static final native void gtk_hsv_to_rgb(float h, float s, float v, float[] r, float[] g, float[] b);
	/**
	 * @param h cast=(float *)
	 * @param s cast=(float *)
	 * @param v cast=(float *)
	 */
	public static final native void gtk_rgb_to_hsv(float r, float g, float b, float[] h, float[] s, float[] v);


	/* GtkBox */
	/**
	 * @param box cast=(GtkBox *)
	 * @param child cast=(GtkWidget *)
	 */
	public static final native void gtk_box_append(long box, long child);
	/**
	 * @param box cast=(GtkBox *)
	 * @param child cast=(GtkWidget *)
	 */
	public static final native void gtk_box_prepend(long box, long child);
	/**
	 * @param box cast=(GtkBox *)
	 * @param child cast=(GtkWidget *)
	 * @param sibling cast=(GtkWidget *)
	 */
	public static final native void gtk_box_insert_child_after(long box, long child, long sibling);


	/* GtkCalendar */
	/** @param calendar cast=(GtkCalendar *) */
	public static final native long gtk_calendar_get_date(long calendar);
	/**
	 * @param calendar cast=(GtkCalendar *)
	 * @param date cast=(GDateTime *)
	 */
	public static final native void gtk_calendar_select_day(long calendar, long date);
	/**
	 * @param calendar cast=(GtkCalendar *)
	 * @param value cast=(gboolean)
	 */
	public static final native void gtk_calendar_set_show_day_names(long calendar, boolean value);
	/**
	 * @param calendar cast=(GtkCalendar *)
	 * @param value cast=(gboolean)
	 */
	public static final native void gtk_calendar_set_show_heading(long calendar, boolean value);
	/**
	 * @param calendar cast=(GtkCalendar *)
	 * @param value cast=(gboolean)
	 */
	public static final native void gtk_calendar_set_show_week_numbers(long calendar, boolean value);


	/* GtkCheckButton */
	/** @param check_button cast=(GtkCheckButton *) */
	public static final native void gtk_check_button_set_use_underline(long check_button, boolean use_underline);
	/** @param check_button cast=(GtkCheckButton *) */
	public static final native void gtk_check_button_set_inconsistent(long check_button, boolean inconsistent);
	/**
	 * @param check_button cast=(GtkCheckButton *)
	 * @param group cast=(GtkCheckButton *)
	 */
	public static final native void gtk_check_button_set_group(long check_button, long group);
	/** @param check_button cast=(GtkCheckButton *) */
	public static final native void gtk_check_button_set_active(long check_button, boolean setting);
	/** @param check_button cast=(GtkCheckButton *) */
	public static final native boolean gtk_check_button_get_active(long check_button);


	/* GtkPicture */
	public static final native long gtk_picture_new();
	/**
	 * @param picture cast=(GtkPicture *)
	 * @param paintable cast=(GdkPaintable *)
	 */
	public static final native void gtk_picture_set_paintable(long picture, long paintable);
}
