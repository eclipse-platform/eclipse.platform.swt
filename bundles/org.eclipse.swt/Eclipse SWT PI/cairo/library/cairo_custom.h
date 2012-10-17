/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * Contributor(s):
 *
 * IBM
 * -  Binding to permit interfacing between Cairo and SWT
 * -  Copyright (C) 2005, 2012 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */

/* Libraries for dynamic loaded functions */
#ifdef AIX
#define LIB_CAIRO "libcairo.a(libcairo.so.2)"
#elif HPUX
#define LIB_CAIRO "libcairo.so"
#else
#define LIB_CAIRO "libcairo.so.2"
#endif

#define cairo_format_stride_for_width_LIB LIB_CAIRO
#define cairo_pdf_surface_set_size_LIB LIB_CAIRO
#define cairo_ps_surface_set_size_LIB LIB_CAIRO
#define cairo_surface_set_fallback_resolution_LIB LIB_CAIRO
#define cairo_surface_get_type_LIB LIB_CAIRO
#define cairo_image_surface_get_data_LIB LIB_CAIRO
#define cairo_image_surface_get_format_LIB LIB_CAIRO
#define cairo_image_surface_get_stride_LIB LIB_CAIRO
#define cairo_xlib_surface_get_drawable_LIB LIB_CAIRO
#define cairo_xlib_surface_get_height_LIB LIB_CAIRO
#define cairo_xlib_surface_get_width_LIB LIB_CAIRO
#define cairo_surface_get_content_LIB LIB_CAIRO
#define cairo_push_group_LIB LIB_CAIRO
#define cairo_pop_group_to_source_LIB LIB_CAIRO
#define cairo_region_create_LIB LIB_CAIRO
#define cairo_region_destroy_LIB LIB_CAIRO
#define cairo_region_is_empty_LIB LIB_CAIRO
#define cairo_region_subtract_LIB LIB_CAIRO
#define cairo_region_translate_LIB LIB_CAIRO
#define cairo_region_union_LIB LIB_CAIRO
#define cairo_region_intersect_LIB LIB_CAIRO
#define cairo_region_create_rectangle_LIB LIB_CAIRO
#define cairo_region_contains_rectangle_LIB LIB_CAIRO
#define cairo_region_union_rectangle_LIB LIB_CAIRO
#define cairo_region_get_extents_LIB LIB_CAIRO
#define cairo_region_contains_point_LIB LIB_CAIRO
#define cairo_region_num_rectangles_LIB LIB_CAIRO
#define cairo_region_get_rectangle_LIB LIB_CAIRO

#if (CAIRO_VERSION < CAIRO_VERSION_ENCODE(1,10,0))
typedef struct {
    int x, y;
    int width, height;
} cairo_rectangle_int_t;
#endif