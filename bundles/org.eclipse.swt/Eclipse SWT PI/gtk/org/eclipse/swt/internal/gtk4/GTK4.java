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

	/* GTK Initialization */
	public static final native boolean gtk_init_check();

	/* GdkToplevel */
	/** @param toplevel cast=(GdkToplevel *) */
	public static final native int gdk_toplevel_get_state(long toplevel);
	/**
	 * @param toplevel cast=(GdkToplevel *)
	 * @param textures cast=(GList *)
	 */
	public static final native void gdk_toplevel_set_icon_list(long toplevel, long textures);
	/** @param toplevel cast=(GdkToplevel *) */
	public static final native boolean gdk_toplevel_lower(long toplevel);
	/** @param surface cast=(GdkToplevel *) */
	public static final native void gdk_toplevel_focus(long surface, int timestamp);

	/* GtkDragSource */
	public static final native long gtk_drag_source_new();
	/**
	 * @param source cast=(GtkDragSource *)
	 * @param actions cast=(GdkDragAction)
	 */
	public static final native void gtk_drag_source_set_actions(long source, int actions);
	/**
	 * @param source cast=(GtkDragSource *)
	 * @param paintable cast=(GdkPaintable *)
	 */
	public static final native void gtk_drag_source_set_icon(long source, long paintable, int hot_x, int hot_y);

	/* GtkDropTarget */
	/**
	 * @param formats cast=(GdkContentFormats *)
	 * @param actions cast=(GdkDragAction)
	 */
	public static final native long gtk_drop_target_async_new(long formats, int actions);
	/**
	 * @param target cast=(GtkDropTargetAsync *)
	 * @param formats cast=(GdkContentFormats *)
	 */
	public static final native void gtk_drop_target_async_set_formats(long target, long formats);

	/* GdkContentFormats */
	public static final native long gdk_content_formats_builder_new();
	/**
	 * @param builder cast=(GdkContentFormatsBuilder *)
	 * @param mime_type cast=(const char *)
	 */
	public static final native void gdk_content_formats_builder_add_mime_type(long builder, byte[] mime_type);
	/** @param builder cast=(GdkContentFormatsBuilder *) */
	public static final native long gdk_content_formats_builder_free_to_formats(long builder);

	/* GtkFileChooser */
	/** @param chooser cast=(GtkFileChooser *) */
	public static final native long gtk_file_chooser_get_files(long chooser);
	/** @param chooser cast=(GtkFileChooser *) */
	public static final native long gtk_file_chooser_get_file(long chooser);
	/**
	 * @param chooser cast=(GtkFileChooser *)
	 * @param file cast=(GFile *)
	 * @param error cast=(GError **)
	 */
	public static final native boolean gtk_file_chooser_set_current_folder(long chooser, long file, long error);
	/**
	 * @param chooser cast=(GtkFileChooser *)
	 * @param file cast=(GFile *)
	 * @param error cast=(GError **)
	 */
	public static final native boolean gtk_file_chooser_set_file(long chooser, long file, long error);
}
