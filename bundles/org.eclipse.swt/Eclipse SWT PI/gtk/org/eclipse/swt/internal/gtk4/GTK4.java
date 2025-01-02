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
package org.eclipse.swt.internal.gtk4;

import org.eclipse.swt.internal.gtk.*;

/**
 * This class contains native functions that are present in GTK4 only.
 */
public class GTK4 {

	public static final int GTK_POPOVER_MENU_NESTED = 1 << 0;

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

	/** @param editable cast=(GtkEditable *) */
	public static final native long gtk_editable_get_text(long editable);
	/** @param editable cast=(GtkEditable *) */
	public static final native long gtk_editable_get_delegate(long editable);

	/* GtkPicture */
	public static final native long gtk_picture_new();
	/**
	 * @param picture cast=(GtkPicture *)
	 * @param paintable cast=(GdkPaintable *)
	 */
	public static final native void gtk_picture_set_paintable(long picture, long paintable);
	/** @param picture cast=(GtkPicture *) */
	public static final native void gtk_picture_set_can_shrink(long picture, boolean can_shrink);

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

	/* GtkFileDialog */
	/** @method flags=dynamic **/
	public static final native long gtk_file_dialog_new();
	/**
	 * @method flags=dynamic
	 * 
	 * @param parent cast=(GtkWindow *)
	 * @param cancellable cast=(GCancellable *)
	 * @param callback cast=(GAsyncReadyCallback)
	 * @param user_data cast=(gpointer)
	 */
	public static final native void gtk_file_dialog_select_folder(long self, long parent, long cancellable, long callback, long user_data);
	/**
	 * @method flags=dynamic
	 * 
	 * @param result cast=(GAsyncResult *)
	 * @param error cast=(GError **)
	 */
	public static final native long gtk_file_dialog_select_folder_finish(long self, long result, long[] error);
	/**
	 * @method flags=dynamic
	 * 
	 * @param folder cast=(GFile *)
	 */
	public static final native void gtk_file_dialog_set_initial_folder(long self, long folder);
	/**
	 * @method flags=dynamic
	 * 
	 * @param title cast=(char *)
	 */
	public static final native void gtk_file_dialog_set_initial_name(long self, byte[] title);
	/**
	 * @method flags=dynamic
	 * 
	 * @param file cast=(GFile *)
	 */
	public static final native void gtk_file_dialog_set_initial_file(long self, long file);
	/**
	 * @method flags=dynamic
	 * 
	 * @param title cast=(char *)
	 */
	public static final native void gtk_file_dialog_set_title(long self, byte[] title);
	/**
	 * @method flags=dynamic
	 * 
	 * @param filter cast=(GtkFileFilter *)
	 */
	public static final native void gtk_file_dialog_set_default_filter(long self, long filter);
	/**
	 * @method flags=dynamic
	 * 
	 * @param filters cast=(GListModel *)
	 */
	public static final native void gtk_file_dialog_set_filters(long self, long filters);
	/**
	 * @method flags=dynamic
	 * 
	 */
	public static final native long gtk_file_dialog_get_default_filter(long self);
	/**
	 * @method flags=dynamic
	 * 
	 * @param parent cast=(GtkWindow *)
	 * @param cancellable cast=(GCancellable *)
	 * @param callback cast=(GAsyncReadyCallback)
	 * @param user_data cast=(gpointer)
	 */
	public static final native void gtk_file_dialog_open_multiple(long self, long parent, long cancellable, long callback, long user_data);
	/**
	 * @method flags=dynamic
	 * 
	 * @param result cast=(GAsyncResult *)
	 * @param error cast=(GError **)
	 */
	public static final native long gtk_file_dialog_open_multiple_finish(long self, long result, long[] error);
	/**
	 * 
	 * @param parent cast=(GtkWindow *)
	 * @param cancellable cast=(GCancellable *)
	 * @param callback cast=(GAsyncReadyCallback)
	 * @param user_data cast=(gpointer)
	 * @method flags=dynamic
	 */
	public static final native void gtk_file_dialog_open(long self, long parent, long cancellable, long callback, long user_data);
	/**
	 * @method flags=dynamic
	 * 
	 * @param result cast=(GAsyncResult *)
	 * @param error cast=(GError **)
	 */
	public static final native long gtk_file_dialog_open_finish(long self, long result, long[] error);
	/**
	 * @method flags=dynamic
	 * 
	 * @param parent cast=(GtkWindow *)
	 * @param cancellable cast=(GCancellable *)
	 * @param callback cast=(GAsyncReadyCallback)
	 * @param user_data cast=(gpointer)
	 */
	public static final native void gtk_file_dialog_save(long self, long parent, long cancellable, long callback, long user_data);
	/**
	 * @method flags=dynamic
	 * 
	 * @param result cast=(GAsyncResult *)
	 * @param error cast=(GError **)
	 */
	public static final native long gtk_file_dialog_save_finish(long self, long result, long[] error);

	/* GtkFontDialog */
	/**
	 * @method flags=dynamic
	 *
	 * @param parent cast=(GtkWindow *)
	 * @param initial_value cast=(PangoFontDescription *)
	 * @param cancellable cast=(GCancellable *)
	 * @param callback cast=(GAsyncReadyCallback)
	 * @param user_data cast=(gpointer)
	 */
	public static final native void gtk_font_dialog_choose_font(long self, long parent, long initial_value, long cancellable, long callback, long user_data);
	/**
	 * @method flags=dynamic
	 *
	 * @param result cast=(GAsyncResult *)
	 * @param error cast=(GError **)
	 */
	public static final native long gtk_font_dialog_choose_font_finish(long self, long result, long[] error);
	/**
	 * @method flags=dynamic
	 */
	public static final native long gtk_font_dialog_new();
	/**
	 * @method flags=dynamic
	 *
	 * @param modal cast=(gboolean)
	 */
	public static final native long gtk_font_dialog_set_modal(long self, boolean modal);
	/**
	 * @method flags=dynamic
	 *
	 * @param title cast=(char *)
	 */
	public static final native long gtk_font_dialog_set_title(long self, byte[] title);


	/* GtkScrolledWindow */
	public static final native long gtk_scrolled_window_new();
	/** @param scrolled_window cast=(GtkScrolledWindow *) */
	public static final native void gtk_scrolled_window_set_has_frame(long scrolled_window, boolean has_frame);
	/** @param scrolled_window cast=(GtkScrolledWindow *) */
	public static final native boolean gtk_scrolled_window_get_has_frame(long scrolled_window);
	/**
	 * @param scrolled_window cast=(GtkScrolledWindow *)
	 * @param child cast=(GtkWidget *)
	 */
	public static final native void gtk_scrolled_window_set_child(long scrolled_window, long child);

	/* GtkWindow */
	public static final native long gtk_window_new();
	/** @param window cast=(GtkWindow *) */
	public static final native void gtk_window_maximize(long window);
	/** @param window cast=(GtkWindow *) */
	public static final native void gtk_window_minimize(long window);
	/** @param window cast=(GtkWindow *) */
	public static final native void gtk_window_unminimize(long window);
	/** @param window cast=(GtkWindow *) */
	public static final native boolean gtk_window_is_maximized(long window);
	/**
	 * @param window cast=(GtkWindow *)
	 * @param default_widget cast=(GtkWidget *)
	 */
	public static final native void gtk_window_set_default_widget(long window, long default_widget);
	/**
	 * @param window cast=(GtkWindow *)
	 * @param child cast=(GtkWidget *)
	 */
	public static final native void gtk_window_set_child(long window, long child);
	/** @param window cast=(GtkWindow *) */
	public static final native void gtk_window_destroy(long window);
	/** @param window cast=(GtkWindow *) */
	public static final native long gtk_window_get_icon_name(long window);
	/**
	 * @param window cast=(GtkWindow *)
	 * @param name cast=(const char *)
	 * */
	public static final native void gtk_window_set_icon_name(long window, long name);

	/* GtkShortcutController */
	public static final native long gtk_shortcut_controller_new();
	/**
	 * @param controller cast=(GtkShortcutController *)
	 * @param scope cast=(GtkShortcutScope)
	 */
	public static final native void gtk_shortcut_controller_set_scope(long controller, int scope);
	/**
	 * @param controller cast=(GtkShortcutController *)
	 * @param shortcut cast=(GtkShortcut *)
	 */
	public static final native void gtk_shortcut_controller_add_shortcut(long controller, long shortcut);
	/**
	 * @param controller cast=(GtkShortcutController *)
	 * @param shortcut cast=(GtkShortcut *)
	 */
	public static final native void gtk_shortcut_controller_remove_shortcut(long controller, long shortcut);

	/* GtkShortcut */
	/**
	 * @param trigger cast=(GtkShortcutTrigger *)
	 * @param action cast=(GtkShortcutAction *)
	 */
	public static final native long gtk_shortcut_new(long trigger, long action);

	/* GtkShortcutTrigger */
	/**
	 * @param keyval cast=(guint)
	 * @param modifiers cast=(GdkModifierType)
	 */
	public static final native long gtk_keyval_trigger_new(int keyval, int modifiers);

	/* GtkShortcutAction */
	/** @param name cast=(const char *) */
	public static final native long gtk_named_action_new(byte[] name);

	/* GtkIconPaintable */
	/** @param paintable cast=(GtkIconPaintable *) */
	public static final native long gtk_icon_paintable_get_file(long paintable);

	/* GtkText */
	/** @param self cast=(GtkText *) */
	public static final native long gtk_text_get_buffer(long self);
	/** @param self cast=(GtkText *) */
	public static final native void gtk_text_set_visibility(long self, boolean visible);
	/**
	 * @param self cast=(GtkText *)
	 * @param text cast=(const gchar *)
	 */
	public static final native void gtk_text_set_placeholder_text(long self, byte[] text);
	/**
	 * @param entry cast=(GtkText *)
	 * @param tabs cast=(PangoTabArray *)
	 */
	public static final native void gtk_text_set_tabs(long entry, long tabs);

	/* GtkPopoverMenu */
	/**
	 * @param model cast=(GMenuModel *)
	 * @param flags cast=(GtkPopoverMenuFlags)
	 */
	public static final native long gtk_popover_menu_new_from_model_full(long model, int flags);
	/**
	 * @param popover cast=(GtkPopoverMenu *)
	 * @param model cast=(GMenuModel *)
	 */
	public static final native void gtk_popover_menu_set_menu_model(long popover, long model);

	/* GtkPopover */
	/** @param popover cast=(GtkPopover *) */
	public static final native void gtk_popover_set_has_arrow(long popover, boolean has_arrow);

	/* GtkPopoverMenuBar */
	/** @param model cast=(GMenuModel *) */
	public static final native long gtk_popover_menu_bar_new_from_model(long model);

	/* GtkIconTheme */
	/** @param display cast=(GdkDisplay *) */
	public static final native long gtk_icon_theme_get_for_display(long display);
	/**
	 * @param self cast=(GtkIconTheme *)
	 * @param icon_name cast=(const char *)
	 * @param fallbacks cast=(const char **)
	 * @param direction cast=(GtkTextDirection)
	 * @param flags cast=(GtkIconLookupFlags)
	 */
	public static final native long gtk_icon_theme_lookup_icon(long self, byte[] icon_name, long fallbacks, int size, int scale, int direction, int flags);
	/**
	 * @param self cast=(GtkIconTheme *)
	 * @param icon cast=(GIcon *)
	 * @param direction cast=(GtkTextDirection)
	 * @param flags cast=(GtkIconLookupFlags)
	 */
	public static final native long gtk_icon_theme_lookup_by_gicon(long self, long icon, int size, int scale, int direction, int flags);

	/* GtkNative */
	/** @param self cast=(GtkNative *) */
	public static final native long gtk_native_get_surface(long self);

	/* GtkEntry */
	/**
	 * @param entry cast=(GtkEntry *)
	 * @param buffer cast=(GtkEntryBuffer *)
	 */
	public static final native void gtk_entry_set_buffer(long entry, long buffer);
	/** @param entry cast=(GtkEntry *) */
	public static final native long gtk_entry_get_buffer(long entry);
	/** @param entry cast=(GtkEntry *) */
	public static final native int gtk_entry_get_text_length(long entry);

	/* GtkExpander */
	/**
	 * @param expander cast=(GtkExpander *)
	 * @param child cast=(GtkWidget *)
	 */
	public static final native void gtk_expander_set_child(long expander, long child);

	/* GtkEventController */
	public static final native long gtk_event_controller_focus_new();
	/** @param controller cast=(GtkEventController *) */
	public static final native long gtk_event_controller_get_current_event(long controller);
	/** @param controller cast=(GtkEventController *) */
	public static final native int gtk_event_controller_get_current_event_state(long controller);
	public static final native long gtk_event_controller_key_new();
	public static final native long gtk_event_controller_motion_new();
	/** @param flag cast=(GtkEventControllerScrollFlags) */
	public static final native long gtk_event_controller_scroll_new(int flag);

	/* GtkGestureClick */
	public static final native long gtk_gesture_click_new();

	/* GtkFrame */
	/**
	 * @param frame cast=(GtkFrame *)
	 * @param child cast=(GtkWidget *)
	 */
	public static final native void gtk_frame_set_child(long frame, long child);

	/* GtkScrollbar */
	/** @param scrollbar cast=(GtkScrollbar *) */
	public static final native long gtk_scrollbar_get_adjustment(long scrollbar);

	/* GtkWidget */
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param child cast=(GtkWidget *)
	 * @param snapshot cast=(GtkSnapshot *)
	 */
	public static final native void gtk_widget_snapshot_child(long widget, long child, long snapshot);
	/**
	 * @param src_widget cast=(GtkWidget *)
	 * @param dest_widget cast=(GtkWidget *)
	 * @param dest_x cast=(double *)
	 * @param dest_y cast=(double *)
	 */
	public static final native boolean gtk_widget_translate_coordinates(long src_widget, long dest_widget, double src_x, double src_y, double[] dest_x, double[] dest_y);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param cursor cast=(GdkCursor *)
	 */
	public static final native void gtk_widget_set_cursor(long widget, long cursor);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param orientation cast=(GtkOrientation)
	 * @param for_size cast=(int)
	 * @param minimum cast=(int *)
	 * @param natural cast=(int *)
	 * @param minimum_baseline cast=(int *)
	 * @param natural_baseline cast=(int *)
	 */
	public static final native void gtk_widget_measure(long widget, int orientation, int for_size, int[] minimum, int[] natural, int[] minimum_baseline, int[] natural_baseline);
	/** @param widget cast=(GtkWidget *) */
	public static final native long gtk_widget_get_native(long widget);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param name cast=(const char *)
	 * @param format_string cast=(const char *)
	 */
	public static final native boolean gtk_widget_activate_action(long widget, byte[] name, byte[] format_string);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param action_name cast=(const char *)
	 */
	public static final native void gtk_widget_action_set_enabled(long widget, byte[] action_name, boolean enabled);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param controller cast=(GtkEventController *)
	 */
	public static final native void gtk_widget_add_controller(long widget, long controller);
	/** @param widget cast=(GtkWidget *) */
	public static final native long gtk_widget_get_first_child(long widget);
	/** @param widget cast=(GtkWidget *) */
	public static final native long gtk_widget_get_last_child(long widget);
	/** @param widget cast=(GtkWidget *) */
	public static final native long gtk_widget_get_next_sibling(long widget);
	/** @param widget cast=(GtkWidget *) */
	public static final native long gtk_widget_get_prev_sibling(long widget);
	/** @param widget cast=(GtkWidget *) */
	public static final native long gtk_widget_get_root(long widget);
	/**
	 * @param widget_class cast=(GtkWidgetClass *)
	 * @param mods cast=(GdkModifierType)
	 * @param signal cast=(const char *)
	 * @param format_string cast=(const char *)
	 */
	public static final native void gtk_widget_class_add_binding_signal(long widget_class, int keyval, int mods, byte[] signal, byte[] format_string, boolean arg1, boolean arg2, boolean arg3);
	/** @param widget cast=(GtkWidget *) */
	public static final native boolean gtk_widget_get_receives_default(long widget);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param focusable cast=(gboolean)
	 */
	public static final native void gtk_widget_set_focusable(long widget, boolean focusable);
	/** @param widget cast=(GtkWidget *) */
	public static final native long gtk_widget_get_clipboard(long widget);
	/**
	 * @param widget cast=(GtkWidget *)
	 * @param allocation cast=(GtkAllocation *),flags=no_out
	 */
	public static final native void gtk_widget_size_allocate(long widget, GtkAllocation allocation, int baseline);

	/* GtkComboBox */
	/** @param combo_box cast=(GtkComboBox *) */
	public static final native long gtk_combo_box_get_child(long combo_box);

	/* GtkSnapshot */
	/**
	 * @param snapshot cast=(GtkSnapshot *)
	 * @param rect cast=(const graphene_rect_t *)
	 */
	public static final native long gtk_snapshot_append_cairo(long snapshot, long rect);

	/* GtkImage */
	/**
	 * @param image cast=(GtkImage *)
	 * @param paintable cast=(GdkPaintable *)
	 */
	public static final native void gtk_image_set_from_paintable(long image, long paintable);
	/** @param icon_name cast=(const char *) */
	public static final native long gtk_image_new_from_icon_name(byte[] icon_name);
	/**
	 * @param image cast=(GtkImage *)
	 * @param icon_name cast=(const gchar *)
	 */
	public static final native void gtk_image_set_from_icon_name(long image, byte[] icon_name);
	/** @param image cast=(GtkImage *) */
	public static final native void gtk_image_clear(long image);

	/* GtkCssProvider */
	/**
	 * @param css_provider cast=(GtkCssProvider *)
	 * @param data cast=(const gchar *)
	 * @param length cast=(gssize)
	 */
	public static final native void gtk_css_provider_load_from_data(long css_provider, byte[] data, long length);

	/* GtkLabel */
	/**
	 * @param label cast=(GtkLabel *)
	 * @param wrap cast=(gboolean)
	 */
	public static final native void gtk_label_set_wrap(long label, boolean wrap);
	/**
	 * @param label cast=(GtkLabel *)
	 * @param wrap_mode cast=(PangoWrapMode)
	 */
	public static final native void gtk_label_set_wrap_mode(long label, int wrap_mode);

	/* GtkStyleContext */
	/**
	 * @param display cast=(GdkDisplay *)
	 * @param provider cast=(GtkStyleProvider *)
	 * @param priority cast=(guint)
	 */
	public static final native void gtk_style_context_add_provider_for_display(long display, long provider, int priority);
	/**
	 * @param context cast=(GtkStyleContext *)
	 * @param color cast=(GdkRGBA *)
	 */
	public static final native void gtk_style_context_get_color(long context, GdkRGBA color);
	/**
	 * @param context cast=(GtkStyleContext *)
	 * @param padding cast=(GtkBorder *),flags=no_in
	 */
	public static final native void gtk_style_context_get_padding(long context, GtkBorder padding);
	/**
	 * @param context cast=(GtkStyleContext *)
	 * @param margin cast=(GtkBorder *),flags=no_in
	 */
	public static final native void gtk_style_context_get_margin(long context, GtkBorder margin);
	/**
	 * @param context cast=(GtkStyleContext *)
	 * @param padding cast=(GtkBorder *),flags=no_in
	 */
	public static final native void gtk_style_context_get_border(long context, GtkBorder padding);

	/* GtkMenuButton */
	/** @param menu_button cast=(GtkMenuButton *) */
	public static final native void gtk_menu_button_set_use_underline(long menu_button, boolean use_underline);

	/* GtkTreeViewColumn */
	/**
	 * @param tree_column cast=(GtkTreeViewColumn *)
	 * @param x_offset cast=(int *)
	 * @param y_offset cast=(int *)
	 * @param width cast=(int *)
	 * @param height cast=(int *)
	 */
	public static final native void gtk_tree_view_column_cell_get_size(long tree_column, int[] x_offset, int[] y_offset, int[] width, int[] height);

	/* GdkToplevelSize */
	/**
	 * @param size cast=(GdkToplevelSize*)
	 * @param bounds_width cast=(int *)
	 * @param bounds_height cast=(int *)
	 */
	public static final native void gdk_toplevel_size_get_bounds(long size, int[] bounds_width, int[] bounds_height);
	/**
	 * @param size cast=(GdkToplevelSize*)
	 * @param min_width cast=(int)
	 * @param min_height cast=(int)
	 */
	public static final native void gdk_toplevel_size_set_min_size(long size, int min_width, int min_height);
	/**
	 * @param size cast=(GdkToplevelSize*)
	 * @param width cast=(int)
	 * @param height cast=(int)
	 */
	public static final native void gdk_toplevel_size_set_size(long size, int width, int height);

	/* GdkClipboard */
	/**
	 * @param clipboard cast=(GdkClipboard*)
	 * @param text cast=(const char *)
	 */
	public static final native void gdk_clipboard_set_text(long clipboard, byte[] text);
	/**
	 * @param clipboard cast=(GdkClipboard*)
	 * @param type cast=(GType)
	 */
	public static final native void gdk_clipboard_set(long clipboard, long type, long data);
	/**
	 * @param clipboard cast=(GdkClipboard*)
	 * @param provider cast=(GdkContentProvider*)
	 */
	public static final native boolean gdk_clipboard_set_content(long clipboard, long provider);
	/**
	 * @param clipboard cast=(GdkClipboard*)
	 */
	public static final native long gdk_clipboard_get_formats(long clipboard);
	/**
	 * @param clipboard cast=(GdkClipboard*)
	 */
	public static final native long gdk_clipboard_get_content(long clipboard);
	/**
	 * @param provider cast=(GdkContentProvider *)
	 * @param value cast=(GValue *)
	 * @param error cast=(GError **)
	 */
	public static final native boolean gdk_content_provider_get_value(long provider, long value, long[] error);
	/**
	 * @param type cast=(GType)
	 */
	public static final native long gdk_content_provider_new_typed(long type, long data);
	/**
	 * @param value cast=(const GValue*)
	 */
	public static final native long gdk_content_provider_new_for_value(long value);
	/**
	 * @param providers cast=(GdkContentProvider **)
	 * @param n_providers cast=(gsize)
	 */
	public static final native long gdk_content_provider_new_union(long[] providers, int n_providers);
	/** @param formats cast=(GdkContentFormats *) */
	public static final native long gdk_content_formats_to_string(long formats);

	public static final native long gtk_gesture_rotate_new();

	public static final native long gtk_gesture_zoom_new();

	public static final native long gtk_gesture_drag_new();

}
