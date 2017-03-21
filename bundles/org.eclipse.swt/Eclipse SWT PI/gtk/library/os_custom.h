/*******************************************************************************
* Copyright (c) 2000, 2016 IBM Corporation and others. All rights reserved.
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

/* Include guard */
#ifndef ORG_ECLIPSE_SWT_GTK_OS_CUSTOM_H
#define ORG_ECLIPSE_SWT_GTK_OS_CUSTOM_H

/* Special sizeof's */
#define GPollFD_sizeof() sizeof(GPollFD)
#define GtkFixedClass_sizeof() sizeof(GtkFixedClass)
#define GtkCellRendererText_sizeof() sizeof(GtkCellRendererText)
#define GtkCellRendererTextClass_sizeof() sizeof(GtkCellRendererTextClass)
#define GtkCellRendererPixbuf_sizeof() sizeof(GtkCellRendererPixbuf)
#define GtkCellRendererPixbufClass_sizeof() sizeof(GtkCellRendererPixbufClass)
#define GtkCellRendererToggle_sizeof() sizeof(GtkCellRendererToggle)
#define GtkCellRendererToggleClass_sizeof() sizeof(GtkCellRendererToggleClass)
#define GtkTextIter_sizeof() sizeof(GtkTextIter)
#define GtkTreeIter_sizeof() sizeof(GtkTreeIter)

#ifdef AIX
#define LIB_GTK "libgtk-x11-2.0.a(libgtk-x11-2.0.so.0)"
#define LIB_GDK "libgdk-x11-2.0.a(libgdk-x11-2.0.so.0)"
#define LIB_GTHREAD "libgthread-2.0.a(libgthread-2.0.so.0)"
#define LIB_ATK "libatk-1.0.a(libatk-1.0.so.0)"
#define LIB_FONTCONFIG "libfontconfig.a(libfontconfig.so.1)"
#elif HPUX
#define LIB_GTK "libgtk-x11-2.0.so"
#define LIB_GDK "libgdk-x11-2.0.so"
#define LIB_GTHREAD "libgthread-2.0.so"
#define LIB_ATK "libatk-1.0.so"
#define LIB_FONTCONFIG "libfontconfig.so"
#elif _WIN32
#if GTK_CHECK_VERSION(3,0,0)
#define LIB_GTK "libgtk-3-0.dll"
#define LIB_GDK "libgdk-3-0.dll"
#else
#define LIB_GTK "libgtk-win32-2.0-0.dll"
#define LIB_GDK "libgdk-win32-2.0-0.dll"
#endif
#define LIB_GTHREAD "libgthread-2.0-0.dll"
#define LIB_ATK "libatk-1.0-0.dll"
#define LIB_FONTCONFIG "libfontconfig-1.dll"
#else
#if GTK_CHECK_VERSION(3,0,0)
#define LIB_GTK "libgtk-3.so.0"
#define LIB_GDK "libgdk-3.so.0"
#else
#define LIB_GTK "libgtk-x11-2.0.so.0"
#define LIB_GDK "libgdk-x11-2.0.so.0"
#endif
#define LIB_GTHREAD "libgthread-2.0.so.0"
#define LIB_ATK "libatk-1.0.so.0"
#define LIB_FONTCONFIG "libfontconfig.so.1"
#endif

/* Libraries for dynamic loaded functions */

#define g_thread_init_LIB LIB_GTHREAD
#define gtk_widget_set_opacity_LIB LIB_GTK
#define gtk_widget_get_opacity_LIB LIB_GTK
#define gtk_scrollable_get_vadjustment_LIB LIB_GTK
#define gtk_adjustment_value_changed_LIB LIB_GTK
#define gtk_arrow_new_LIB LIB_GTK
#define gtk_arrow_set_LIB LIB_GTK
#define gtk_box_new_LIB LIB_GTK
#define gtk_hbox_new_LIB LIB_GTK
#define gtk_vbox_new_LIB LIB_GTK
#define gtk_cell_renderer_get_preferred_size_LIB LIB_GTK
#define gtk_cell_renderer_get_size_LIB LIB_GTK
#define gtk_color_selection_dialog_get_color_selection_LIB LIB_GTK
#define gtk_color_selection_dialog_new_LIB LIB_GTK
#define gtk_color_selection_set_has_palette_LIB LIB_GTK
#define gtk_color_selection_get_current_color_LIB LIB_GTK
#define gtk_color_selection_set_current_color_LIB LIB_GTK
#define gtk_color_selection_palette_to_string_LIB LIB_GTK
#define gtk_color_chooser_add_palette_LIB LIB_GTK
#define gtk_color_chooser_get_rgba_LIB LIB_GTK
#define gtk_color_chooser_dialog_new_LIB LIB_GTK
#define gtk_color_chooser_set_rgba_LIB LIB_GTK
#define gtk_color_chooser_set_use_alpha_LIB LIB_GTK
#define gtk_color_chooser_get_use_alpha_LIB LIB_GTK
#define gtk_combo_box_text_insert_LIB LIB_GTK
#define gtk_combo_box_text_remove_all_LIB LIB_GTK
#define gtk_combo_box_set_focus_on_click_LIB LIB_GTK
#define gtk_drag_begin_LIB LIB_GTK
#define gtk_entry_get_inner_border_LIB LIB_GTK
#define gtk_font_chooser_dialog_new_LIB LIB_GTK
#define gtk_font_chooser_get_font_LIB LIB_GTK
#define gtk_font_chooser_set_font_LIB LIB_GTK
#define gtk_font_selection_dialog_get_font_name_LIB LIB_GTK
#define gtk_font_selection_dialog_set_font_name_LIB LIB_GTK
#define gtk_font_selection_dialog_new_LIB LIB_GTK
#define gtk_icon_info_free_LIB LIB_GTK
#define gtk_image_menu_item_set_image_LIB LIB_GTK
#define gdk_keyboard_ungrab_LIB LIB_GDK
#define gtk_image_menu_item_new_with_label_LIB LIB_GTK
#define gtk_scrollbar_new_LIB LIB_GTK
#define gtk_hscrollbar_new_LIB LIB_GTK
#define gtk_vscrollbar_new_LIB LIB_GTK
#define gtk_separator_new_LIB LIB_GTK
#define gtk_rc_parse_string_LIB LIB_GTK
#define gtk_hseparator_new_LIB LIB_GTK
#define gtk_vseparator_new_LIB LIB_GTK
#define gtk_tree_view_column_get_button_LIB LIB_GTK
#define gtk_tree_view_get_vadjustment_LIB LIB_GTK
#define gtk_tree_view_set_rules_hint_LIB LIB_GTK
#define gtk_tree_view_get_rules_hint_LIB LIB_GTK
#define gdk_cursor_new_from_pixbuf_LIB LIB_GDK
#define gdk_cursor_unref_LIB LIB_GDK
#define gdk_display_warp_pointer_LIB LIB_GDK
#define gdk_device_warp_LIB LIB_GDK
#define gdk_device_manager_get_client_pointer_LIB LIB_GDK
#define gdk_device_get_window_at_position_LIB LIB_GDK
#define gdk_device_grab_LIB LIB_GDK
#define gdk_device_ungrab_LIB LIB_GDK
#define gdk_display_get_device_manager_LIB LIB_GDK
#define gdk_device_get_associated_device_LIB LIB_GDK
#define gdk_display_get_default_LIB LIB_GDK
#define gdk_display_supports_cursor_color_LIB LIB_GDK
#define gdk_drag_context_get_actions_LIB LIB_GDK
#define gdk_drag_context_get_dest_window_LIB LIB_GDK
#define gdk_drag_context_get_selected_action_LIB LIB_GDK
#define gdk_drag_context_list_targets_LIB LIB_GDK
#define gdk_draw_arc_LIB LIB_GDK
#define gdk_draw_layout_LIB LIB_GDK
#define gdk_draw_layout_with_colors_LIB LIB_GDK
#define gdk_draw_line_LIB LIB_GDK
#define gdk_draw_lines_LIB LIB_GDK
#define gdk_draw_pixbuf_LIB LIB_GDK
#define gdk_draw_point_LIB LIB_GDK
#define gdk_draw_polygon_LIB LIB_GDK
#define gdk_draw_image_LIB LIB_GDK
#define gdk_drawable_get_image_LIB LIB_GDK
#define gdk_drawable_get_visible_region_LIB LIB_GDK
#define gdk_gc_new_LIB LIB_GDK
#define gdk_draw_rectangle_LIB LIB_GDK
#define gdk_gc_set_clip_region_LIB LIB_GDK
#define gdk_gc_set_foreground_LIB LIB_GDK
#define gdk_gc_set_function_LIB LIB_GDK
#define gdk_gc_set_subwindow_LIB LIB_GDK
#define gdk_draw_drawable_LIB LIB_GDK
#define gdk_drawable_get_depth_LIB LIB_GDK
#define gdk_pixmap_new_LIB LIB_GDK
#define gdk_color_white_LIB LIB_GDK
#define gdk_colormap_get_system_LIB LIB_GDK
#define gdk_bitmap_create_from_data_LIB LIB_GDK
#define gdk_cursor_new_from_pixmap_LIB LIB_GDK
#define gdk_window_get_internal_paint_info_LIB LIB_GDK
#define gdk_window_invalidate_region_LIB LIB_GDK
#define gdk_window_shape_combine_region_LIB LIB_GDK
#define gdk_window_set_back_pixmap_LIB LIB_GDK
#define gdk_gc_set_background_LIB LIB_GDK
#define gdk_gc_set_stipple_LIB LIB_GDK
#define gdk_gc_set_clip_mask_LIB LIB_GDK
#define gdk_gc_set_fill_LIB LIB_GDK
#define gdk_gc_set_clip_origin_LIB LIB_GDK
#define gdk_gc_set_dashes_LIB LIB_GDK
#define gdk_gc_set_exposures_LIB LIB_GDK
#define gdk_gc_set_line_attributes_LIB LIB_GDK
#define gdk_gc_set_tile_LIB LIB_GDK
#define gdk_gc_set_ts_origin_LIB LIB_GDK
#define gdk_gc_set_values_LIB LIB_GDK
#define gdk_gc_get_values_LIB LIB_GDK
#define gdk_pixbuf_save_to_bufferv_LIB LIB_GDK
#define gdk_pixbuf_get_from_drawable_LIB LIB_GDK
#define gdk_pixbuf_get_from_window_LIB LIB_GDK
#define gdk_pixbuf_render_pixmap_and_mask_LIB LIB_GDK
#define gdk_pointer_grab_LIB LIB_GDK
#define gdk_pointer_ungrab_LIB LIB_GDK
#define gdk_region_polygon_LIB LIB_GDK
#define gdk_region_get_rectangles_LIB LIB_GDK
#define gdk_rgba_to_string_LIB LIB_GDK
#define gdk_rgba_parse_LIB LIB_GDK
#define gdk_screen_get_default_LIB LIB_GDK
#define gdk_screen_get_resolution_LIB LIB_GDK
#define gdk_screen_get_monitor_scale_factor_LIB LIB_GDK
#define gdk_screen_get_monitor_at_point_LIB LIB_GDK
#define gdk_screen_get_monitor_at_window_LIB LIB_GDK
#define gdk_screen_get_monitor_geometry_LIB LIB_GDK
#define gdk_screen_get_n_monitors_LIB LIB_GDK
#define gdk_screen_get_monitor_width_mm_LIB LIB_GDK
#define gdk_screen_get_primary_monitor_LIB LIB_GDK
#define gdk_pixbuf_render_to_drawable_LIB LIB_GDK
#define gdk_visual_get_depth_LIB LIB_GDK
#define gtk_scale_new_LIB LIB_GTK
#define gtk_style_context_add_provider_for_screen_LIB LIB_GTK
#define gtk_style_context_add_provider_LIB LIB_GTK
#define gtk_style_context_restore_LIB LIB_GTK
#define gtk_style_context_save_LIB LIB_GTK
#define gtk_style_context_set_state_LIB LIB_GTK
#define gtk_hscale_new_LIB LIB_GTK
#define gtk_vscale_new_LIB LIB_GTK
#define gtk_scrolled_window_add_with_viewport_LIB LIB_GTK
#define gtk_settings_set_string_property_LIB LIB_GTK
#define gtk_status_icon_position_menu_LIB LIB_GTK
#define gtk_false_LIB LIB_GTK
#define gtk_window_get_opacity_LIB LIB_GTK
#define gdk_window_create_similar_surface_LIB LIB_GDK
#define gdk_window_restack_LIB LIB_GDK
#define gdk_window_get_device_position_LIB LIB_GDK
#define gdk_window_get_pointer_LIB LIB_GDK
#define gdk_window_at_pointer_LIB LIB_GDK
#define gdk_window_get_height_LIB LIB_GDK
#define gdk_window_get_width_LIB LIB_GDK
#define gdk_pixmap_get_size_LIB LIB_GDK
#define gdk_window_get_display_LIB LIB_GDK
#define gdk_window_get_visible_region_LIB LIB_GDK
#define gdk_window_set_keep_above_LIB LIB_GDK
#define gdk_window_set_accept_focus_LIB LIB_GDK
#define gtk_misc_set_alignment_LIB LIB_GTK
#define gtk_label_set_xalign_LIB LIB_GTK
#define gtk_label_set_yalign_LIB LIB_GTK
#define gtk_widget_set_halign_LIB LIB_GTK
#define gtk_widget_set_valign_LIB LIB_GTK
#define gtk_window_set_opacity_LIB LIB_GTK
#define gtk_widget_is_composited_LIB LIB_GTK
#define gtk_widget_class_get_css_name_LIB LIB_GTK
#define gtk_widget_size_request_LIB LIB_GTK
#define gtk_widget_get_default_style_LIB LIB_GTK
#define gtk_widget_get_preferred_size_LIB LIB_GTK
#define gtk_widget_get_style_context_LIB LIB_GTK
#define gtk_widget_get_state_flags_LIB LIB_GTK
#define gtk_widget_set_state_LIB LIB_GTK
#define gtk_widget_shape_combine_region_LIB LIB_GTK
#define gtk_widget_modify_base_LIB LIB_GTK
#define gtk_widget_modify_bg_LIB LIB_GTK
#define gtk_widget_modify_font_LIB LIB_GTK
#define gtk_widget_modify_style_LIB LIB_GTK
#define gtk_widget_get_style_LIB LIB_GTK
#define gtk_widget_get_modifier_style_LIB LIB_GTK
#define gtk_widget_set_focus_on_click_LIB LIB_GTK
#define gdk_threads_set_lock_functions_LIB LIB_GDK

#define atk_object_add_relationship_LIB LIB_ATK
#define atk_object_remove_relationship_LIB LIB_ATK
#define gdk_cairo_reset_clip_LIB LIB_GDK
#define gdk_cairo_get_clip_rectangle_LIB LIB_GDK
#define gdk_cairo_set_source_pixbuf_LIB LIB_GDK
#define gdk_cairo_set_source_color_LIB LIB_GDK
#define gdk_cairo_set_source_window_LIB LIB_GDK
#define gdk_cairo_region_create_from_surface_LIB LIB_GDK
#define gdk_cairo_region_LIB LIB_GDK
#define gdk_cairo_create_LIB LIB_GDK
#define gdk_colormap_alloc_color_LIB LIB_GDK
#define gdk_colormap_free_colors_LIB LIB_GDK
#define gtk_paint_box_LIB LIB_GTK
#define gtk_paint_handle_LIB LIB_GTK
#define gtk_paint_focus_LIB LIB_GTK
#define gtk_paint_flat_box_LIB LIB_GTK
#define gtk_printer_option_widget_get_type_LIB LIB_GTK
#define gtk_render_handle_LIB LIB_GTK
#define gtk_render_focus_LIB LIB_GTK
#define gtk_render_frame_LIB LIB_GTK
#define gtk_render_background_LIB LIB_GTK
#define gtk_progress_bar_set_inverted_LIB LIB_GTK
#define gtk_progress_bar_set_orientation_LIB LIB_GTK
#define ubuntu_menu_proxy_get_LIB LIB_GTK
#define FcConfigAppFontAddFile_LIB LIB_FONTCONFIG

// GTK3 only
#define gtk_event_controller_handle_event_LIB LIB_GTK
#define gtk_event_controller_set_propagation_phase_LIB LIB_GTK
#define gtk_file_filter_add_pattern_LIB LIB_GTK
#define gtk_gesture_drag_new_LIB LIB_GTK
#define gtk_gesture_drag_get_offset_LIB LIB_GTK
#define gtk_gesture_get_last_event_LIB LIB_GTK
#define gtk_gesture_get_last_updated_sequence_LIB LIB_GTK
#define gtk_gesture_get_point_LIB LIB_GTK
#define gtk_gesture_get_sequences_LIB LIB_GTK
#define gtk_gesture_group_LIB LIB_GTK
#define gtk_gesture_is_recognized_LIB LIB_GTK
#define gtk_gesture_pan_get_orientation_LIB LIB_GTK
#define gtk_gesture_pan_set_orientation_LIB LIB_GTK
#define gtk_gesture_pan_new_LIB LIB_GTK
#define gtk_gesture_rotate_get_angle_delta_LIB LIB_GTK
#define gtk_gesture_rotate_new_LIB LIB_GTK
#define gtk_gesture_swipe_get_velocity_LIB LIB_GTK
#define gtk_gesture_swipe_new_LIB LIB_GTK
#define gtk_gesture_zoom_get_scale_delta_LIB LIB_GTK
#define gtk_gesture_zoom_new_LIB LIB_GTK
#define gtk_gesture_single_set_button_LIB LIB_GTK
#define gtk_widget_draw_LIB LIB_GTK
#define gtk_widget_override_color_LIB LIB_GTK
#define gtk_widget_override_background_color_LIB LIB_GTK
#define gtk_widget_override_font_LIB LIB_GTK
#define gtk_widget_get_preferred_height_for_width_LIB LIB_GTK
#define gtk_widget_get_preferred_width_for_height_LIB LIB_GTK
#define gtk_style_context_get_font_LIB LIB_GTK
#define gtk_style_context_get_color_LIB LIB_GTK
#define gtk_style_context_get_background_color_LIB LIB_GTK
#define gtk_style_context_add_class_LIB LIB_GTK
#define gtk_style_context_get_padding_LIB LIB_GTK
#define gtk_style_context_get_border_LIB LIB_GTK
#define gtk_style_context_invalidate_LIB LIB_GTK
#define gdk_screen_get_monitor_workarea_LIB LIB_GDK
#define gdk_window_set_background_pattern_LIB LIB_GTK
#define gtk_widget_input_shape_combine_region_LIB LIB_GTK
#define gtk_entry_set_placeholder_text_LIB LIB_GTK
#define gtk_entry_get_icon_area_LIB LIB_GTK
#define gdk_event_get_scroll_deltas_LIB LIB_GTK
#define gtk_cell_renderer_get_preferred_height_for_width_LIB LIB_GTK
#define gtk_css_provider_load_from_data_LIB LIB_GTK
#define gtk_css_provider_new_LIB LIB_GTK
#define gtk_css_provider_to_string_LIB LIB_GTK
#define gtk_css_provider_get_named_LIB LIB_GTK
#define gtk_drag_set_icon_surface_LIB LIB_GTK
#define gtk_accel_label_set_accel_LIB LIB_GTK
#define gtk_drag_begin_with_coordinates_LIB LIB_GTK
#ifndef g_thread_supported
#define g_thread_supported() 0
#endif


#ifdef GDK_WINDOWING_X11

#define gdk_x11_screen_get_window_manager_name_LIB LIB_GDK
#define gdk_x11_screen_lookup_visual_LIB LIB_GDK
#define gdk_x11_window_lookup_for_display_LIB LIB_GDK
#define gdk_x11_display_utf8_to_compound_text_LIB LIB_GDK
#define gdk_x11_drawable_get_xid_LIB LIB_GDK
#define gdk_x11_get_default_xdisplay_LIB LIB_GDK
#define gdk_x11_window_get_xid_LIB LIB_GDK

#endif

/* Field accessors */
#define G_OBJECT_CLASS_CONSTRUCTOR(arg0) (arg0)->constructor
#define G_OBJECT_CLASS_SET_CONSTRUCTOR(arg0, arg1) (arg0)->constructor = (GObject* (*) (GType, guint, GObjectConstructParam *))arg1
struct _GtkAccelLabelPrivate
{
  GtkWidget     *accel_widget;       /* done */
  GClosure      *accel_closure;      /* has set function */
  GtkAccelGroup *accel_group;        /* set by set_accel_closure() */
  gchar         *accel_string;       /* has set function */
  guint          accel_padding;      /* should be style property? */
  guint16        accel_string_width; /* seems to be private */
};
#if GTK_CHECK_VERSION(3,0,0)
#define GTK_ACCEL_LABEL_SET_ACCEL_STRING(arg0, arg1) (arg0)->priv->accel_string = arg1
#define GTK_ACCEL_LABEL_GET_ACCEL_STRING(arg0) (arg0)->priv->accel_string
#else
#define GTK_ACCEL_LABEL_SET_ACCEL_STRING(arg0, arg1) (arg0)->accel_string = arg1
#define GTK_ACCEL_LABEL_GET_ACCEL_STRING(arg0) (arg0)->accel_string
#endif
#ifndef GTK_WIDGET_SET_FLAGS
#define GTK_WIDGET_SET_FLAGS(arg0, arg1)
#endif
#ifndef GTK_WIDGET_UNSET_FLAGS
#define GTK_WIDGET_UNSET_FLAGS(arg0, arg1)
#endif
#if !GTK_CHECK_VERSION(3,0,0)
#define GDK_IS_X11_DISPLAY(arg0) 1
#endif
#if GTK_CHECK_VERSION(3,0,0)
#define NO__1GTK_1STOCK_1CANCEL
#define NO__1GTK_1STOCK_1OK
#endif
#if GTK_CHECK_VERSION(3,0,0)
#define NO__1GTK_1IS_1IMAGE_1MENU_1ITEM
#endif
#define GTK_ENTRY_IM_CONTEXT(arg0) (arg0)->im_context
#define GTK_TEXTVIEW_IM_CONTEXT(arg0) (arg0)->im_context
#define GTK_WIDGET_REQUISITION_WIDTH(arg0) (arg0)->requisition.width
#define GTK_WIDGET_REQUISITION_HEIGHT(arg0) (arg0)->requisition.height
#define GDK_EVENT_TYPE(arg0) (arg0)->type
#define GDK_EVENT_WINDOW(arg0) (arg0)->window
#define X_EVENT_TYPE(arg0) (arg0)->type
#define X_EVENT_WINDOW(arg0) (arg0)->window
#define g_error_get_message(arg0) (arg0)->message
#define g_list_data(arg0) (arg0)->data
#define g_slist_data(arg0) (arg0)->data
#define g_list_set_next(arg0, arg1) (arg0)->next = arg1
#define g_list_set_previous(arg0, arg1) (arg0)->prev = arg1
#define gtk_rc_style_get_bg_pixmap_name(arg0, arg1) (arg0)->bg_pixmap_name[arg1]
#define gtk_rc_style_get_color_flags(arg0, arg1) (arg0)->color_flags[arg1]
#define gtk_rc_style_set_bg(arg0, arg1, arg2) if (arg2) (arg0)->bg[arg1] = *arg2
#define gtk_rc_style_set_bg_pixmap_name(arg0, arg1, arg2) (arg0)->bg_pixmap_name[arg1] = (char *)arg2
#define gtk_rc_style_set_color_flags(arg0, arg1, arg2) (arg0)->color_flags[arg1] = arg2
#define gtk_rc_style_set_fg(arg0, arg1, arg2) if (arg2) (arg0)->fg[arg1] = *arg2
#define gtk_rc_style_set_text(arg0, arg1, arg2) if (arg2) (arg0)->text[arg1] = *arg2
#define gtk_style_get_font_desc(arg0) (arg0)->font_desc
#define gtk_style_get_base(arg0, arg1, arg2) *arg2 = (arg0)->base[arg1]
#define gtk_style_get_bg(arg0, arg1, arg2) *arg2 = (arg0)->bg[arg1]
#define gtk_style_get_black(arg0, arg1) *arg1 = (arg0)->black
#define gtk_style_get_dark(arg0, arg1, arg2) *arg2 = (arg0)->dark[arg1]
#define gtk_style_get_fg(arg0, arg1, arg2) *arg2 = (arg0)->fg[arg1]
#define gtk_style_get_light(arg0, arg1, arg2) *arg2 = (arg0)->light[arg1]
#define gtk_style_get_text(arg0, arg1, arg2) *arg2 = (arg0)->text[arg1]
#define gtk_style_get_xthickness(arg0) (arg0)->xthickness
#define gtk_style_get_ythickness(arg0) (arg0)->ythickness
#define localeconv_decimal_point() localeconv()->decimal_point

// Mechanism to get function pointers of C/gtk functions back to java.
// Idea is that you substitute the return value with the function pointer.
// NOTE: functions like gtk_false need to be linked to a lib. Eg see gtk_false_LIB above.
#define GET_FUNCTION_POINTER_gtk_false() 0; \
OS_LOAD_FUNCTION(fp, gtk_false) \
rc = (jintLong)fp;

#define gtk_status_icon_position_menu_func() 0; \
OS_LOAD_FUNCTION(fp, gtk_status_icon_position_menu) \
rc = (jintLong)fp;

glong g_utf16_pointer_to_offset(const gchar*, const gchar*);
gchar* g_utf16_offset_to_pointer(const gchar*, glong);
glong g_utf16_strlen(const gchar*, glong max);
glong g_utf16_offset_to_utf8_offset(const gchar*, glong);
glong g_utf8_offset_to_utf16_offset(const gchar*, glong);

#ifndef NO_SwtFixed

#define SWT_TYPE_FIXED (swt_fixed_get_type ())
#define SWT_FIXED(obj) (G_TYPE_CHECK_INSTANCE_CAST ((obj), SWT_TYPE_FIXED, SwtFixed))
#define SWT_FIXED_CLASS(klass) (G_TYPE_CHECK_CLASS_CAST ((klass), SWT_TYPE_FIXED, SwtFixedClass))
#define SWT_IS_FIXED(obj) (G_TYPE_CHECK_INSTANCE_TYPE ((obj), SWT_TYPE_FIXED))
#define SWT_IS_FIXED_CLASS(klass) (G_TYPE_CHECK_CLASS_TYPE ((klass), SWT_TYPE_FIXED))
#define SWT_FIXED_GET_CLASS(obj) (G_TYPE_INSTANCE_GET_CLASS ((obj), SWT_TYPE_FIXED, SwtFixedClass))


typedef struct _SwtFixed SwtFixed;
typedef struct _SwtFixedPrivate SwtFixedPrivate;
typedef struct _SwtFixedClass SwtFixedClass;

struct _SwtFixed
{
  GtkContainer container;

  /*< private >*/
  SwtFixedPrivate *priv;
};

struct _SwtFixedClass
{
  GtkContainerClass parent_class;
};

GType swt_fixed_get_type (void) G_GNUC_CONST;

void swt_fixed_restack(SwtFixed *fixed, GtkWidget *widget, GtkWidget *sibling, gboolean above);
void swt_fixed_move(SwtFixed *fixed, GtkWidget *widget, gint x, gint y);
void swt_fixed_resize(SwtFixed *fixed, GtkWidget *widget, gint width, gint height);

#endif

void swt_debug_on_fatal_warnings() ;

#endif /* ORG_ECLIPSE_SWT_GTK_OS_CUSTOM_H (include guard, this should be the last line) */
