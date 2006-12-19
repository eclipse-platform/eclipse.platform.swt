/*******************************************************************************
* Copyright (c) 2000, 2006 IBM Corporation and others. All rights reserved.
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
#define PTR_sizeof() sizeof(void *)

/* Libraries for dynamic loaded functions */
#define XRenderQueryExtension_LIB "libXrender.so"
#define XRenderQueryVersion_LIB "libXrender.so"
#define XRenderFindStandardFormat_LIB "libXrender.so"
#define XRenderFindVisualFormat_LIB "libXrender.so"
#define XRenderComposite_LIB "libXrender.so"
#define XRenderCreatePicture_LIB "libXrender.so"
#define XRenderFreePicture_LIB "libXrender.so"
#define XRenderSetPictureClipRectangles_LIB "libXrender.so"
#define XRenderSetPictureTransform_LIB "libXrender.so"
#define gtk_entry_text_index_to_layout_index_LIB "libgtk-x11-2.0.so.0"
#define gtk_file_chooser_add_filter_LIB "libgtk-x11-2.0.so.0"
#define gtk_file_chooser_dialog_new_LIB "libgtk-x11-2.0.so.0"
#define gtk_file_chooser_get_current_folder_LIB "libgtk-x11-2.0.so.0"
#define gtk_file_chooser_get_filename_LIB "libgtk-x11-2.0.so.0"
#define gtk_file_chooser_get_filenames_LIB "libgtk-x11-2.0.so.0"
#define gtk_file_chooser_set_current_folder_LIB "libgtk-x11-2.0.so.0"
#define gtk_file_chooser_set_current_name_LIB "libgtk-x11-2.0.so.0"
#define gtk_file_chooser_set_extra_widget_LIB "libgtk-x11-2.0.so.0"
#define gtk_file_chooser_set_filename_LIB "libgtk-x11-2.0.so.0"
#define gtk_file_chooser_set_select_multiple_LIB "libgtk-x11-2.0.so.0"
#define gtk_file_filter_add_pattern_LIB "libgtk-x11-2.0.so.0"
#define gtk_file_filter_new_LIB "libgtk-x11-2.0.so.0"
#define gtk_file_filter_set_name_LIB "libgtk-x11-2.0.so.0"
#define gtk_expander_get_expanded_LIB "libgtk-x11-2.0.so.0"
#define gtk_expander_get_label_widget_LIB "libgtk-x11-2.0.so.0"
#define gtk_expander_new_LIB "libgtk-x11-2.0.so.0"
#define gtk_expander_set_expanded_LIB "libgtk-x11-2.0.so.0"
#define gtk_expander_set_label_LIB "libgtk-x11-2.0.so.0"
#define gtk_expander_set_label_widget_LIB "libgtk-x11-2.0.so.0"
#define gtk_tree_selection_count_selected_rows_LIB "libgtk-x11-2.0.so.0"
#define gtk_tree_selection_get_selected_rows_LIB "libgtk-x11-2.0.so.0"
#define gtk_tree_view_column_cell_get_position_LIB "libgtk-x11-2.0.so.0"
#define gtk_entry_set_alignment_LIB "libgtk-x11-2.0.so.0"
#define gdk_draw_pixbuf_LIB "libgdk-x11-2.0.so.0"
#define gdk_screen_get_default_LIB "libgdk-x11-2.0.so.0"
#define gdk_screen_get_monitor_at_point_LIB "libgdk-x11-2.0.so.0"
#define gdk_screen_get_monitor_at_window_LIB "libgdk-x11-2.0.so.0"
#define gdk_screen_get_monitor_geometry_LIB "libgdk-x11-2.0.so.0"
#define gdk_screen_get_n_monitors_LIB "libgdk-x11-2.0.so.0"
#define gdk_screen_get_number_LIB "libgdk-x11-2.0.so.0"
#define gdk_window_set_keep_above_LIB "libgdk-x11-2.0.so.0"
#define gdk_window_set_accept_focus_LIB "libgdk-x11-2.0.so.0"
#define gdk_x11_screen_get_window_manager_name_LIB "libgdk-x11-2.0.so.0"
#define gdk_x11_screen_lookup_visual_LIB "libgdk-x11-2.0.so.0"
#define atk_object_add_relationship_LIB "libatk-1.0.so.0"
#define pango_layout_set_auto_dir_LIB "libpango-1.0.so.0"
#define pango_cairo_create_layout_LIB "libpangocairo-1.0.so.0"
#define pango_cairo_layout_path_LIB "libpangocairo-1.0.so.0"
#define pango_cairo_show_layout_LIB "libpangocairo-1.0.so.0"
#define pango_cairo_font_map_create_context_LIB "libpangocairo-1.0.so.0"
#define pango_cairo_font_map_new_LIB "libpangocairo-1.0.so.0"
#define pango_cairo_font_map_get_default_LIB "libpangocairo-1.0.so.0"
#define gdk_cairo_set_source_color_LIB "libgdk-x11-2.0.so.0"
#define gdk_cairo_region_LIB "libgdk-x11-2.0.so.0"

/* Field accessors */
#define GTK_ACCEL_LABEL_SET_ACCEL_STRING(arg0, arg1) (arg0)->accel_string = arg1
#define GTK_ACCEL_LABEL_GET_ACCEL_STRING(arg0) (arg0)->accel_string
#define GTK_SCROLLED_WINDOW_HSCROLLBAR(arg0) (arg0)->hscrollbar
#define GTK_SCROLLED_WINDOW_SCROLLBAR_SPACING(arg0) (GTK_SCROLLED_WINDOW_GET_CLASS (arg0)->scrollbar_spacing >= 0 ? GTK_SCROLLED_WINDOW_GET_CLASS (arg0)->scrollbar_spacing : 3)		
#define GTK_SCROLLED_WINDOW_VSCROLLBAR(arg0) (arg0)->vscrollbar
#define GTK_WIDGET_HEIGHT(arg0) (arg0)->allocation.height
#define GTK_WIDGET_SET_HEIGHT(arg0, arg1) (arg0)->allocation.height = arg1
#define GTK_WIDGET_WIDTH(arg0) (arg0)->allocation.width
#define GTK_WIDGET_SET_WIDTH(arg0, arg1) (arg0)->allocation.width = arg1
#define GTK_WIDGET_WINDOW(arg0) (arg0)->window
#define GTK_WIDGET_X(arg0) (arg0)->allocation.x
#define GTK_WIDGET_SET_X(arg0, arg1) (arg0)->allocation.x = arg1
#define GTK_ENTRY_IM_CONTEXT(arg0) (arg0)->im_context
#define GTK_TEXTVIEW_IM_CONTEXT(arg0) (arg0)->im_context
#define GTK_TOOLTIPS_TIP_WINDOW(arg0) (arg0)->tip_window
#define GTK_TOOLTIPS_SET_ACTIVE(arg0, arg1) (arg0)->active_tips_data = arg1
#define GTK_WIDGET_Y(arg0) ((GtkWidget *)arg0)->allocation.y
#define GTK_WIDGET_SET_Y(arg0, arg1) (arg0)->allocation.y = arg1
#define GTK_WIDGET_REQUISITION_WIDTH(arg0) (arg0)->requisition.width
#define GTK_WIDGET_REQUISITION_HEIGHT(arg0) (arg0)->requisition.height
#define GDK_EVENT_TYPE(arg0) (arg0)->type
#define GDK_EVENT_WINDOW(arg0) (arg0)->window
#define X_EVENT_TYPE(arg0) (arg0)->type
#define X_EVENT_WINDOW(arg0) (arg0)->window
#define g_list_data(arg0) (arg0)->data
#define g_slist_data(arg0) (arg0)->data
#define g_list_set_next(arg0, arg1) (arg0)->next = arg1
#define g_list_set_previous(arg0, arg1) (arg0)->prev = arg1
#define gtk_rc_style_get_bg_pixmap_name(arg0, arg1) (arg0)->bg_pixmap_name[arg1]
#define gtk_rc_style_get_color_flags(arg0, arg1) (arg0)->color_flags[arg1]
#define gtk_rc_style_set_bg(arg0, arg1, arg2) if (arg2) (arg0)->bg[arg1] = *arg2
#define gtk_rc_style_set_bg_pixmap_name(arg0, arg1, arg2) (arg0)->bg_pixmap_name[arg1] = (char *)arg2
#define gtk_rc_style_set_color_flags(arg0, arg1, arg2) (arg0)->color_flags[arg1] = arg2
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
#define gtk_style_get_fg_gc(arg0, arg1, arg2) *arg2 = (arg0)->fg_gc[arg1]
#define gtk_style_get_bg_gc(arg0, arg1, arg2) *arg2 = (arg0)->bg_gc[arg1]
#define gtk_style_get_light_gc(arg0, arg1, arg2) *arg2 = (arg0)->light_gc[arg1]
#define gtk_style_get_dark_gc(arg0, arg1, arg2) *arg2 = (arg0)->dark_gc[arg1]
#define gtk_style_get_mid_gc(arg0, arg1, arg2) *arg2 = (arg0)->mid_gc[arg1]
#define gtk_style_get_text_gc(arg0, arg1, arg2) *arg2 = (arg0)->text_gc[arg1]
#define gtk_style_get_text_aa_gc(arg0, arg1, arg2) *arg2 = (arg0)->text_aa_gc[arg1]
#define gtk_style_get_black_gc(arg0, arg1) *arg1 = (arg0)->black_gc
#define gtk_style_get_white_gc(arg0, arg1) *arg1 = (arg0)->white_gc
#define localeconv_decimal_point() localeconv()->decimal_point

