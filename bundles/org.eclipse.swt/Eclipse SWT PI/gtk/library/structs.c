/*
 * Copyright (c) IBM Corp. 2000, 2001.  All rights reserved.
 *
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 */

/**
 * JNI SWT object field getters and setters declarations for Windows structs
 */

#include "swt.h"
#include "structs.h"

/* Globals */
GdkColor_FID_CACHE GdkColorFc;
GdkEventExpose_FID_CACHE GdkEventExposeFc;
GdkFont_FID_CACHE GdkFontFc;
GdkGCValues_FID_CACHE GdkGCValuesFc;
GdkRectangle_FID_CACHE GdkRectangleFc;
GdkVisual_FID_CACHE GdkVisualFc;

GtkAdjustment_FID_CACHE GtkAdjustmentFc;
GtkAllocation_FID_CACHE GtkAllocationFc;
GtkCombo_FID_CACHE GtkComboFc;
GtkCList_FID_CACHE GtkCListFc;
GtkRequisition_FID_CACHE GtkRequisitionFc;
GtkStyle_FID_CACHE GtkStyleFc;
GtkStyleClass_FID_CACHE GtkStyleClassFc;
GtkCListRow_FID_CACHE GtkCListRowFc;
GtkCListColumn_FID_CACHE GtkCListColumnFc;
GtkCTreeRow_FID_CACHE GtkCTreeRowFc;
GtkCTree_FID_CACHE GtkCTreeFc;

/* ----------- fid and class caches  ----------- */
/*
 * Used for Java objects passed into JNI that are
 * declared like:
 *
 * 	nativeFunction (Rectangle p1, Rectangle p2, Rectangle p3)
 *
 * and not like this
 *
 * 	nativeFunction (Object p1, Object p2, Object p3)
 *
 *
 */
void cacheGdkColorFids(JNIEnv *env, jobject lpGdkColor, PGdkColor_FID_CACHE lpCache)
{
	if (lpCache->cached) return;

	lpCache->GdkColorClass = (*env)->GetObjectClass(env, lpGdkColor);

	lpCache->pixel = (*env)->GetFieldID(env, lpCache->GdkColorClass, "pixel", "I");
	lpCache->red = (*env)->GetFieldID(env, lpCache->GdkColorClass, "red", "S");
	lpCache->green = (*env)->GetFieldID(env, lpCache->GdkColorClass, "green", "S");
	lpCache->blue = (*env)->GetFieldID(env, lpCache->GdkColorClass, "blue", "S");
	lpCache->cached = 1;
};

void cacheGdkEventExposeFids(JNIEnv *env, jobject lpGdkEventExpose, PGdkEventExpose_FID_CACHE lpCache)
{
	if (lpCache->cached) return;

	lpCache->GdkEventExposeClass = (*env)->GetObjectClass(env, lpGdkEventExpose);
	lpCache->x = (*env)->GetFieldID(env, lpCache->GdkEventExposeClass, "x", "S");
	lpCache->y = (*env)->GetFieldID(env, lpCache->GdkEventExposeClass, "y", "S");
	lpCache->width = (*env)->GetFieldID(env, lpCache->GdkEventExposeClass, "width", "S");
	lpCache->height = (*env)->GetFieldID(env, lpCache->GdkEventExposeClass, "height", "S");
	lpCache->region = (*env)->GetFieldID(env, lpCache->GdkEventExposeClass, "region", "I");
	lpCache->count = (*env)->GetFieldID(env, lpCache->GdkEventExposeClass, "count", "I");

	lpCache->cached = 1;
};

void cacheGdkFontFids(JNIEnv *env, jobject lpGdkFont, PGdkFont_FID_CACHE lpCache)
{
	if (lpCache->cached) return;

	lpCache->GdkFontClass = (*env)->GetObjectClass(env, lpGdkFont);
	lpCache->type = (*env)->GetFieldID(env, lpCache->GdkFontClass, "type", "I");
	lpCache->ascent = (*env)->GetFieldID(env, lpCache->GdkFontClass, "ascent", "I");
	lpCache->descent = (*env)->GetFieldID(env, lpCache->GdkFontClass, "descent", "I");

	lpCache->cached = 1;
};

void cacheGdkGCValuesFids(JNIEnv *env, jobject lpGdkGCValues, PGdkGCValues_FID_CACHE lpCache)
{
	if (lpCache->cached) return;

	lpCache->GdkGCValuesClass = (*env)->GetObjectClass(env, lpGdkGCValues);

	lpCache->foreground_pixel = (*env)->GetFieldID(env, lpCache->GdkGCValuesClass, "foreground_pixel", "I");
	lpCache->foreground_red = (*env)->GetFieldID(env, lpCache->GdkGCValuesClass, "foreground_red", "S");
	lpCache->foreground_green = (*env)->GetFieldID(env, lpCache->GdkGCValuesClass, "foreground_green", "S");
	lpCache->foreground_blue = (*env)->GetFieldID(env, lpCache->GdkGCValuesClass, "foreground_blue", "S");
	lpCache->background_pixel = (*env)->GetFieldID(env, lpCache->GdkGCValuesClass, "background_pixel", "I");
	lpCache->background_red = (*env)->GetFieldID(env, lpCache->GdkGCValuesClass, "background_red", "S");
	lpCache->background_green = (*env)->GetFieldID(env, lpCache->GdkGCValuesClass, "background_green", "S");
	lpCache->background_blue = (*env)->GetFieldID(env, lpCache->GdkGCValuesClass, "background_blue", "S");
	lpCache->font = (*env)->GetFieldID(env, lpCache->GdkGCValuesClass, "font", "I");
	lpCache->function = (*env)->GetFieldID(env, lpCache->GdkGCValuesClass, "function", "I");
	lpCache->fill = (*env)->GetFieldID(env, lpCache->GdkGCValuesClass, "fill", "I");
	lpCache->tile = (*env)->GetFieldID(env, lpCache->GdkGCValuesClass, "tile", "I");
	lpCache->stipple = (*env)->GetFieldID(env, lpCache->GdkGCValuesClass, "stipple", "I");
	lpCache->clip_mask = (*env)->GetFieldID(env, lpCache->GdkGCValuesClass, "clip_mask", "I");
	lpCache->subwindow_mode = (*env)->GetFieldID(env, lpCache->GdkGCValuesClass, "subwindow_mode", "I");
	lpCache->ts_x_origin = (*env)->GetFieldID(env, lpCache->GdkGCValuesClass, "ts_x_origin", "I");
	lpCache->ts_y_origin = (*env)->GetFieldID(env, lpCache->GdkGCValuesClass, "ts_y_origin", "I");
	lpCache->clip_x_origin = (*env)->GetFieldID(env, lpCache->GdkGCValuesClass, "clip_x_origin", "I");
	lpCache->clip_y_origin = (*env)->GetFieldID(env, lpCache->GdkGCValuesClass, "clip_y_origin", "I");
	lpCache->graphics_exposures = (*env)->GetFieldID(env, lpCache->GdkGCValuesClass, "graphics_exposures", "I");
	lpCache->line_width = (*env)->GetFieldID(env, lpCache->GdkGCValuesClass, "line_width", "I");
	lpCache->line_style = (*env)->GetFieldID(env, lpCache->GdkGCValuesClass, "line_style", "I");
	lpCache->cap_style = (*env)->GetFieldID(env, lpCache->GdkGCValuesClass, "cap_style", "I");
	lpCache->join_style = (*env)->GetFieldID(env, lpCache->GdkGCValuesClass, "join_style", "I");

	lpCache->cached = 1;
};

void cacheGdkRectangleFids(JNIEnv *env, jobject lpGdkRectangle, PGdkRectangle_FID_CACHE lpCache)
{
	if (lpCache->cached) return;

	lpCache->GdkRectangleClass = (*env)->GetObjectClass(env, lpGdkRectangle);
	lpCache->x = (*env)->GetFieldID(env, lpCache->GdkRectangleClass, "x", "S");
	lpCache->y = (*env)->GetFieldID(env, lpCache->GdkRectangleClass, "y", "S");
	lpCache->width = (*env)->GetFieldID(env, lpCache->GdkRectangleClass, "width", "S");
	lpCache->height = (*env)->GetFieldID(env, lpCache->GdkRectangleClass, "height", "S");

	lpCache->cached = 1;
};

void cacheGdkVisualFids(JNIEnv *env, jobject lpGdkVisual, PGdkVisual_FID_CACHE lpCache)
{
	if (lpCache->cached) return;

	lpCache->GdkVisualClass = (*env)->GetObjectClass(env, lpGdkVisual);
	lpCache->type = (*env)->GetFieldID(env, lpCache->GdkVisualClass, "type", "I");
	lpCache->depth = (*env)->GetFieldID(env, lpCache->GdkVisualClass, "depth", "I");
	lpCache->byte_order = (*env)->GetFieldID(env, lpCache->GdkVisualClass, "byte_order", "I");
	lpCache->colormap_size = (*env)->GetFieldID(env, lpCache->GdkVisualClass, "colormap_size", "I");
	lpCache->bits_per_rgb = (*env)->GetFieldID(env, lpCache->GdkVisualClass, "bits_per_rgb", "I");
	lpCache->red_mask = (*env)->GetFieldID(env, lpCache->GdkVisualClass, "red_mask", "I");
	lpCache->red_shift = (*env)->GetFieldID(env, lpCache->GdkVisualClass, "red_shift", "I");
	lpCache->red_prec = (*env)->GetFieldID(env, lpCache->GdkVisualClass, "red_prec", "I");
	lpCache->green_mask = (*env)->GetFieldID(env, lpCache->GdkVisualClass, "green_mask", "I");
	lpCache->green_shift = (*env)->GetFieldID(env, lpCache->GdkVisualClass, "green_shift", "I");
	lpCache->green_prec = (*env)->GetFieldID(env, lpCache->GdkVisualClass, "green_prec", "I");
	lpCache->blue_mask = (*env)->GetFieldID(env, lpCache->GdkVisualClass, "blue_mask", "I");
	lpCache->blue_shift = (*env)->GetFieldID(env, lpCache->GdkVisualClass, "blue_shift", "I");
	lpCache->blue_prec = (*env)->GetFieldID(env, lpCache->GdkVisualClass, "blue_prec", "I");

	lpCache->cached = 1;
};

void cacheGtkAllocationFids(JNIEnv *env, jobject lpGtkAllocation, PGtkAllocation_FID_CACHE lpCache)
{
	if (lpCache->cached) return;

	lpCache->GtkAllocationClass = (*env)->GetObjectClass(env, lpGtkAllocation);
	lpCache->x = (*env)->GetFieldID(env, lpCache->GtkAllocationClass, "x", "S");
	lpCache->y = (*env)->GetFieldID(env, lpCache->GtkAllocationClass, "y", "S");
	lpCache->width = (*env)->GetFieldID(env, lpCache->GtkAllocationClass, "width", "S");
	lpCache->height = (*env)->GetFieldID(env, lpCache->GtkAllocationClass, "height", "S");

	lpCache->cached = 1;
};

void cacheGtkCListFids(JNIEnv *env, jobject lpGtkCList, PGtkCList_FID_CACHE lpCache)
{
	DECL_GLOB(pGlob)
	if (lpCache->cached) return;

	lpCache->GtkCListClass = (*env)->GetObjectClass(env, lpGtkCList);
	lpCache->clist_flags = (*env)->GetFieldID(env, lpCache->GtkCListClass, "clist_flags", "S");
	lpCache->row_mem_chunk = (*env)->GetFieldID(env, lpCache->GtkCListClass, "row_mem_chunk", "I");
	lpCache->cell_mem_chunk = (*env)->GetFieldID(env, lpCache->GtkCListClass, "cell_mem_chunk", "I");
	lpCache->freeze_count = (*env)->GetFieldID(env, lpCache->GtkCListClass, "freeze_count", "I");
	lpCache->internal_allocation_x = (*env)->GetFieldID(env, lpCache->GtkCListClass, "internal_allocation_x", "S");
	lpCache->internal_allocation_y = (*env)->GetFieldID(env, lpCache->GtkCListClass, "internal_allocation_y", "S");
	lpCache->internal_allocation_width = (*env)->GetFieldID(env, lpCache->GtkCListClass, "internal_allocation_width", "S");
	lpCache->internal_allocation_height = (*env)->GetFieldID(env, lpCache->GtkCListClass, "internal_allocation_height", "S");
	lpCache->rows = (*env)->GetFieldID(env, lpCache->GtkCListClass, "rows", "I");
	lpCache->row_height = (*env)->GetFieldID(env, lpCache->GtkCListClass, "row_height", "I");
	lpCache->row_list = (*env)->GetFieldID(env, lpCache->GtkCListClass, "row_list", "I");
	lpCache->row_list_end = (*env)->GetFieldID(env, lpCache->GtkCListClass, "row_list_end", "I");
	lpCache->columns = (*env)->GetFieldID(env, lpCache->GtkCListClass, "columns", "I");
	lpCache->column_title_area_x = (*env)->GetFieldID(env, lpCache->GtkCListClass, "column_title_area_x", "S");
	lpCache->column_title_area_y = (*env)->GetFieldID(env, lpCache->GtkCListClass, "column_title_area_y", "S");
	lpCache->column_title_area_width = (*env)->GetFieldID(env, lpCache->GtkCListClass, "column_title_area_width", "S");
	lpCache->column_title_area_height = (*env)->GetFieldID(env, lpCache->GtkCListClass, "column_title_area_height", "S");
	lpCache->title_window = (*env)->GetFieldID(env, lpCache->GtkCListClass, "title_window", "I");
	lpCache->column = (*env)->GetFieldID(env, lpCache->GtkCListClass, "column", "I");
	lpCache->clist_window = (*env)->GetFieldID(env, lpCache->GtkCListClass, "clist_window", "I");
	lpCache->clist_window_width = (*env)->GetFieldID(env, lpCache->GtkCListClass, "clist_window_width", "I");
	lpCache->clist_window_height = (*env)->GetFieldID(env, lpCache->GtkCListClass, "clist_window_height", "I");
	lpCache->hoffset = (*env)->GetFieldID(env, lpCache->GtkCListClass, "hoffset", "I");
	lpCache->voffset = (*env)->GetFieldID(env, lpCache->GtkCListClass, "voffset", "I");
	lpCache->shadow_type = (*env)->GetFieldID(env, lpCache->GtkCListClass, "shadow_type", "I");
	lpCache->selection_mode = (*env)->GetFieldID(env, lpCache->GtkCListClass, "selection_mode", "I");
	lpCache->selection = (*env)->GetFieldID(env, lpCache->GtkCListClass, "selection", "I");
	lpCache->selection_end = (*env)->GetFieldID(env, lpCache->GtkCListClass, "selection_end", "I");
	lpCache->undo_selection = (*env)->GetFieldID(env, lpCache->GtkCListClass, "undo_selection", "I");
	lpCache->undo_unselection = (*env)->GetFieldID(env, lpCache->GtkCListClass, "undo_unselection", "I");
	lpCache->undo_anchor = (*env)->GetFieldID(env, lpCache->GtkCListClass, "undo_anchor", "I");
	lpCache->button_actions0 = (*env)->GetFieldID(env, lpCache->GtkCListClass, "button_actions0", "B");
	lpCache->button_actions1 = (*env)->GetFieldID(env, lpCache->GtkCListClass, "button_actions1", "B");
	lpCache->button_actions2 = (*env)->GetFieldID(env, lpCache->GtkCListClass, "button_actions2", "B");
	lpCache->button_actions3 = (*env)->GetFieldID(env, lpCache->GtkCListClass, "button_actions3", "B");
	lpCache->button_actions4 = (*env)->GetFieldID(env, lpCache->GtkCListClass, "button_actions4", "B");
	lpCache->drag_button = (*env)->GetFieldID(env, lpCache->GtkCListClass, "drag_button", "B");
	lpCache->click_cell_row = (*env)->GetFieldID(env, lpCache->GtkCListClass, "click_cell_row", "I");
	lpCache->click_cell_column = (*env)->GetFieldID(env, lpCache->GtkCListClass, "click_cell_column", "I");
	lpCache->hadjustment = (*env)->GetFieldID(env, lpCache->GtkCListClass, "hadjustment", "I");
	lpCache->vadjustment = (*env)->GetFieldID(env, lpCache->GtkCListClass, "vadjustment", "I");
	lpCache->xor_gc = (*env)->GetFieldID(env, lpCache->GtkCListClass, "xor_gc", "I");
	lpCache->fg_gc = (*env)->GetFieldID(env, lpCache->GtkCListClass, "fg_gc", "I");
	lpCache->bg_gc = (*env)->GetFieldID(env, lpCache->GtkCListClass, "bg_gc", "I");
	lpCache->cursor_drag = (*env)->GetFieldID(env, lpCache->GtkCListClass, "cursor_drag", "I");
	lpCache->x_drag = (*env)->GetFieldID(env, lpCache->GtkCListClass, "x_drag", "I");
	lpCache->focus_row = (*env)->GetFieldID(env, lpCache->GtkCListClass, "focus_row", "I");
	lpCache->anchor = (*env)->GetFieldID(env, lpCache->GtkCListClass, "anchor", "I");
	lpCache->anchor_state = (*env)->GetFieldID(env, lpCache->GtkCListClass, "anchor_state", "I");
	lpCache->drag_pos = (*env)->GetFieldID(env, lpCache->GtkCListClass, "drag_pos", "I");
	lpCache->htimer = (*env)->GetFieldID(env, lpCache->GtkCListClass, "htimer", "I");
	lpCache->vtimer = (*env)->GetFieldID(env, lpCache->GtkCListClass, "vtimer", "I");
	lpCache->sort_type = (*env)->GetFieldID(env, lpCache->GtkCListClass, "sort_type", "I");
	lpCache->compare = (*env)->GetFieldID(env, lpCache->GtkCListClass, "compare", "I");
	lpCache->sort_column = (*env)->GetFieldID(env, lpCache->GtkCListClass, "sort_column", "I");

	lpCache->cached = 1;
};

void cacheGtkComboFids(JNIEnv *env, jobject lpGtkCombo, PGtkCombo_FID_CACHE lpCache)
{
	DECL_GLOB(pGlob)
	if (lpCache->cached) return;

	lpCache->GtkComboClass = (*env)->GetObjectClass(env, lpGtkCombo);
	lpCache->entry = (*env)->GetFieldID(env, lpCache->GtkComboClass, "entry", "I");
	lpCache->list = (*env)->GetFieldID(env, lpCache->GtkComboClass, "list", "I");

	lpCache->cached = 1;
};

void cacheGtkRequisitionFids(JNIEnv *env, jobject lpGtkRequisition, PGtkRequisition_FID_CACHE lpCache)
{
	if (lpCache->cached) return;

	lpCache->GtkRequisitionClass = (*env)->GetObjectClass(env, lpGtkRequisition);
	lpCache->width = (*env)->GetFieldID(env, lpCache->GtkRequisitionClass, "width", "S");
	lpCache->height = (*env)->GetFieldID(env, lpCache->GtkRequisitionClass, "height", "S");

	lpCache->cached = 1;
};

void cacheGtkStyleFids(JNIEnv *env, jobject lpGtkStyle, PGtkStyle_FID_CACHE lpCache)
{
	if (lpCache->cached) return;

	lpCache->GtkStyleClazz = (*env)->GetObjectClass(env, lpGtkStyle);
	lpCache->fg0_pixel = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "fg0_pixel", "I");
	lpCache->fg0_red = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "fg0_red", "S");
	lpCache->fg0_green = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "fg0_green", "S");
	lpCache->fg0_blue = (*env)->GetFieldID(env,  lpCache->GtkStyleClazz, "fg0_blue", "S");
	lpCache->fg1_pixel = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "fg1_pixel", "I");
	lpCache->fg1_red = (*env)->GetFieldID(env,   lpCache->GtkStyleClazz, "fg1_red", "S");
	lpCache->fg1_green = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "fg1_green", "S");
	lpCache->fg1_blue = (*env)->GetFieldID(env,  lpCache->GtkStyleClazz, "fg1_blue", "S");
	lpCache->fg2_pixel = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "fg2_pixel", "I");
	lpCache->fg2_red = (*env)->GetFieldID(env,   lpCache->GtkStyleClazz, "fg2_red", "S");
	lpCache->fg2_green = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "fg2_green", "S");
	lpCache->fg2_blue = (*env)->GetFieldID(env,  lpCache->GtkStyleClazz, "fg2_blue", "S");
	lpCache->fg3_pixel = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "fg3_pixel", "I");
	lpCache->fg3_red = (*env)->GetFieldID(env,   lpCache->GtkStyleClazz, "fg3_red", "S");
	lpCache->fg3_green = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "fg3_green", "S");
	lpCache->fg3_blue = (*env)->GetFieldID(env,  lpCache->GtkStyleClazz, "fg3_blue", "S");
	lpCache->fg4_pixel = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "fg4_pixel", "I");
	lpCache->fg4_red = (*env)->GetFieldID(env,   lpCache->GtkStyleClazz, "fg4_red", "S");
	lpCache->fg4_green = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "fg4_green", "S");
	lpCache->fg4_blue = (*env)->GetFieldID(env,  lpCache->GtkStyleClazz, "fg4_blue", "S");
	lpCache->bg0_pixel = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "bg0_pixel", "I");
	lpCache->bg0_red = (*env)->GetFieldID(env,   lpCache->GtkStyleClazz, "bg0_red", "S");
	lpCache->bg0_green = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "bg0_green", "S");
	lpCache->bg0_blue = (*env)->GetFieldID(env,  lpCache->GtkStyleClazz, "bg0_blue", "S");
	lpCache->bg1_pixel = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "bg1_pixel", "I");
	lpCache->bg1_red = (*env)->GetFieldID(env,   lpCache->GtkStyleClazz, "bg1_red", "S");
	lpCache->bg1_green = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "bg1_green", "S");
	lpCache->bg1_blue = (*env)->GetFieldID(env,  lpCache->GtkStyleClazz, "bg1_blue", "S");
	lpCache->bg2_pixel = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "bg2_pixel", "I");
	lpCache->bg2_red = (*env)->GetFieldID(env,   lpCache->GtkStyleClazz, "bg2_red", "S");
	lpCache->bg2_green = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "bg2_green", "S");
	lpCache->bg2_blue = (*env)->GetFieldID(env,  lpCache->GtkStyleClazz, "bg2_blue", "S");
	lpCache->bg3_pixel = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "bg3_pixel", "I");
	lpCache->bg3_red = (*env)->GetFieldID(env,   lpCache->GtkStyleClazz, "bg3_red", "S");
	lpCache->bg3_green = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "bg3_green", "S");
	lpCache->bg3_blue = (*env)->GetFieldID(env,  lpCache->GtkStyleClazz, "bg3_blue", "S");
	lpCache->bg4_pixel = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "bg4_pixel", "I");
	lpCache->bg4_red = (*env)->GetFieldID(env,   lpCache->GtkStyleClazz, "bg4_red", "S");
	lpCache->bg4_green = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "bg4_green", "S");
	lpCache->bg4_blue = (*env)->GetFieldID(env,  lpCache->GtkStyleClazz, "bg4_blue", "S");
	lpCache->light0_pixel = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "light0_pixel", "I");
	lpCache->light0_red = (*env)->GetFieldID(env,   lpCache->GtkStyleClazz, "light0_red", "S");
	lpCache->light0_green = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "light0_green", "S");
	lpCache->light0_blue = (*env)->GetFieldID(env,  lpCache->GtkStyleClazz, "light0_blue", "S");
	lpCache->light1_pixel = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "light1_pixel", "I");
	lpCache->light1_red = (*env)->GetFieldID(env,   lpCache->GtkStyleClazz, "light1_red", "S");
	lpCache->light1_green = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "light1_green", "S");
	lpCache->light1_blue = (*env)->GetFieldID(env,  lpCache->GtkStyleClazz, "light1_blue", "S");
	lpCache->light2_pixel = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "light2_pixel", "I");
	lpCache->light2_red = (*env)->GetFieldID(env,   lpCache->GtkStyleClazz, "light2_red", "S");
	lpCache->light2_green = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "light2_green", "S");
	lpCache->light2_blue = (*env)->GetFieldID(env,  lpCache->GtkStyleClazz, "light2_blue", "S");
	lpCache->light3_pixel = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "light3_pixel", "I");
	lpCache->light3_red = (*env)->GetFieldID(env,   lpCache->GtkStyleClazz, "light3_red", "S");
	lpCache->light3_green = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "light3_green", "S");
	lpCache->light3_blue = (*env)->GetFieldID(env,  lpCache->GtkStyleClazz, "light3_blue", "S");
	lpCache->light4_pixel = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "light4_pixel", "I");
	lpCache->light4_red = (*env)->GetFieldID(env,   lpCache->GtkStyleClazz, "light4_red", "S");
	lpCache->light4_green = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "light4_green", "S");
	lpCache->light4_blue = (*env)->GetFieldID(env,  lpCache->GtkStyleClazz, "light4_blue", "S");
	lpCache->dark0_pixel = (*env)->GetFieldID(env,  lpCache->GtkStyleClazz, "dark0_pixel", "I");
	lpCache->dark0_red = (*env)->GetFieldID(env,    lpCache->GtkStyleClazz, "dark0_red", "S");
	lpCache->dark0_green = (*env)->GetFieldID(env,  lpCache->GtkStyleClazz, "dark0_green", "S");
	lpCache->dark0_blue = (*env)->GetFieldID(env,   lpCache->GtkStyleClazz, "dark0_blue", "S");
	lpCache->dark1_pixel = (*env)->GetFieldID(env,  lpCache->GtkStyleClazz, "dark1_pixel", "I");
	lpCache->dark1_red = (*env)->GetFieldID(env,    lpCache->GtkStyleClazz, "dark1_red", "S");
	lpCache->dark1_green = (*env)->GetFieldID(env,  lpCache->GtkStyleClazz, "dark1_green", "S");
	lpCache->dark1_blue = (*env)->GetFieldID(env,   lpCache->GtkStyleClazz, "dark1_blue", "S");
	lpCache->dark2_pixel = (*env)->GetFieldID(env,  lpCache->GtkStyleClazz, "dark2_pixel", "I");
	lpCache->dark2_red = (*env)->GetFieldID(env,    lpCache->GtkStyleClazz, "dark2_red", "S");
	lpCache->dark2_green = (*env)->GetFieldID(env,  lpCache->GtkStyleClazz, "dark2_green", "S");
	lpCache->dark2_blue = (*env)->GetFieldID(env,   lpCache->GtkStyleClazz, "dark2_blue", "S");
	lpCache->dark3_pixel = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "dark3_pixel", "I");
	lpCache->dark3_red = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "dark3_red", "S");
	lpCache->dark3_green = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "dark3_green", "S");
	lpCache->dark3_blue = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "dark3_blue", "S");
	lpCache->dark4_pixel = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "dark4_pixel", "I");
	lpCache->dark4_red = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "dark4_red", "S");
	lpCache->dark4_green = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "dark4_green", "S");
	lpCache->dark4_blue = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "dark4_blue", "S");
	lpCache->mid0_pixel = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "mid0_pixel", "I");
	lpCache->mid0_red = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "mid0_red", "S");
	lpCache->mid0_green = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "mid0_green", "S");
	lpCache->mid0_blue = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "mid0_blue", "S");
	lpCache->mid1_pixel = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "mid1_pixel", "I");
	lpCache->mid1_red = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "mid1_red", "S");
	lpCache->mid1_green = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "mid1_green", "S");
	lpCache->mid1_blue = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "mid1_blue", "S");
	lpCache->mid2_pixel = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "mid2_pixel", "I");
	lpCache->mid2_red = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "mid2_red", "S");
	lpCache->mid2_green = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "mid2_green", "S");
	lpCache->mid2_blue = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "mid2_blue", "S");
	lpCache->mid3_pixel = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "mid3_pixel", "I");
	lpCache->mid3_red = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "mid3_red", "S");
	lpCache->mid3_green = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "mid3_green", "S");
	lpCache->mid3_blue = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "mid3_blue", "S");
	lpCache->mid4_pixel = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "mid4_pixel", "I");
	lpCache->mid4_red = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "mid4_red", "S");
	lpCache->mid4_green = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "mid4_green", "S");
	lpCache->mid4_blue = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "mid4_blue", "S");
	lpCache->text0_pixel = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "text0_pixel", "I");
	lpCache->text0_red = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "text0_red", "S");
	lpCache->text0_green = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "text0_green", "S");
	lpCache->text0_blue = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "text0_blue", "S");
	lpCache->text1_pixel = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "text1_pixel", "I");
	lpCache->text1_red = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "text1_red", "S");
	lpCache->text1_green = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "text1_green", "S");
	lpCache->text1_blue = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "text1_blue", "S");
	lpCache->text2_pixel = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "text2_pixel", "I");
	lpCache->text2_red = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "text2_red", "S");
	lpCache->text2_green = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "text2_green", "S");
	lpCache->text2_blue = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "text2_blue", "S");
	lpCache->text3_pixel = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "text3_pixel", "I");
	lpCache->text3_red = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "text3_red", "S");
	lpCache->text3_green = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "text3_green", "S");
	lpCache->text3_blue = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "text3_blue", "S");
	lpCache->text4_pixel = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "text4_pixel", "I");
	lpCache->text4_red = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "text4_red", "S");
	lpCache->text4_green = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "text4_green", "S");
	lpCache->text4_blue = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "text4_blue", "S");
	lpCache->base0_pixel = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "base0_pixel", "I");
	lpCache->base0_red = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "base0_red", "S");
	lpCache->base0_green = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "base0_green", "S");
	lpCache->base0_blue = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "base0_blue", "S");
	lpCache->base1_pixel = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "base1_pixel", "I");
	lpCache->base1_red = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "base1_red", "S");
	lpCache->base1_green = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "base1_green", "S");
	lpCache->base1_blue = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "base1_blue", "S");
	lpCache->base2_pixel = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "base2_pixel", "I");
	lpCache->base2_red = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "base2_red", "S");
	lpCache->base2_green = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "base2_green", "S");
	lpCache->base2_blue = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "base2_blue", "S");
	lpCache->base3_pixel = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "base3_pixel", "I");
	lpCache->base3_red = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "base3_red", "S");
	lpCache->base3_green = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "base3_green", "S");
	lpCache->base3_blue = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "base3_blue", "S");
	lpCache->base4_pixel = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "base4_pixel", "I");
	lpCache->base4_red = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "base4_red", "S");
	lpCache->base4_green = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "base4_green", "S");
	lpCache->base4_blue = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "base4_blue", "S");

	lpCache->black_pixel = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "black_pixel", "I");
	lpCache->black_red = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "black_red", "S");
	lpCache->black_green = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "black_green", "S");
	lpCache->black_blue = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "black_blue", "S");
	lpCache->white_pixel = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "white_pixel", "I");
	lpCache->white_red = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "white_red", "S");
	lpCache->white_green = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "white_green", "S");
	lpCache->white_blue = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "white_blue", "S");

	lpCache->font_desc = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "font_desc", "I");

	lpCache->fg_gc0 = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "fg_gc0", "I");
	lpCache->fg_gc1 = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "fg_gc1", "I");
	lpCache->fg_gc2 = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "fg_gc2", "I");
	lpCache->fg_gc3 = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "fg_gc3", "I");
	lpCache->fg_gc4 = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "fg_gc4", "I");
	lpCache->bg_gc0 = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "bg_gc0", "I");
	lpCache->bg_gc1 = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "bg_gc1", "I");
	lpCache->bg_gc2 = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "bg_gc2", "I");
	lpCache->bg_gc3 = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "bg_gc3", "I");
	lpCache->bg_gc4 = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "bg_gc4", "I");
	lpCache->light_gc0 = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "light_gc0", "I");
	lpCache->light_gc1 = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "light_gc1", "I");
	lpCache->light_gc2 = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "light_gc2", "I");
	lpCache->light_gc3 = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "light_gc3", "I");
	lpCache->light_gc4 = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "light_gc4", "I");
	lpCache->dark_gc0 = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "dark_gc0", "I");
	lpCache->dark_gc1 = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "dark_gc1", "I");
	lpCache->dark_gc2 = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "dark_gc2", "I");
	lpCache->dark_gc3 = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "dark_gc3", "I");
	lpCache->dark_gc4 = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "dark_gc4", "I");
	lpCache->mid_gc0 = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "mid_gc0", "I");
	lpCache->mid_gc1 = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "mid_gc1", "I");
	lpCache->mid_gc2 = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "mid_gc2", "I");
	lpCache->mid_gc3 = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "mid_gc3", "I");
	lpCache->mid_gc4 = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "mid_gc4", "I");
	lpCache->text_gc0 = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "text_gc0", "I");
	lpCache->text_gc1 = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "text_gc1", "I");
	lpCache->text_gc2 = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "text_gc2", "I");
	lpCache->text_gc3 = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "text_gc3", "I");
	lpCache->text_gc4 = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "text_gc4", "I");
	lpCache->base_gc0 = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "base_gc0", "I");
	lpCache->base_gc1 = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "base_gc1", "I");
	lpCache->base_gc2 = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "base_gc2", "I");
	lpCache->base_gc3 = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "base_gc3", "I");
	lpCache->base_gc4 = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "base_gc4", "I");

	lpCache->black_gc = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "black_gc", "I");
	lpCache->white_gc = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "white_gc", "I");

	lpCache->bg_pixmap0 = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "bg_pixmap0", "I");
	lpCache->bg_pixmap1 = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "bg_pixmap1", "I");
	lpCache->bg_pixmap2 = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "bg_pixmap2", "I");
	lpCache->bg_pixmap3 = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "bg_pixmap3", "I");
	lpCache->bg_pixmap4 = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "bg_pixmap4", "I");

	lpCache->cached = 1;
};

void cacheGtkStyleClassFids(JNIEnv *env, jobject lpGtkStyleClass, PGtkStyleClass_FID_CACHE lpCache)
{
	if (lpCache->cached) return;

	lpCache->GtkStyleClassClazz = (*env)->GetObjectClass(env, lpGtkStyleClass);
	lpCache->xthickness = (*env)->GetFieldID(env, lpCache->GtkStyleClassClazz, "xthickness", "I");
	lpCache->ythickness = (*env)->GetFieldID(env, lpCache->GtkStyleClassClazz, "ythickness", "I");

	lpCache->cached = 1;
};

void cacheGtkAdjustmentFids(JNIEnv *env, jobject lpGtkAdjustment, PGtkAdjustment_FID_CACHE lpCache)
{
	DECL_GLOB(pGlob)
	if (lpCache->cached) return;

	lpCache->GtkAdjustmentClass = (*env)->GetObjectClass(env, lpGtkAdjustment);
	lpCache->lower = (*env)->GetFieldID(env, lpCache->GtkAdjustmentClass, "lower", "D");
	lpCache->upper = (*env)->GetFieldID(env, lpCache->GtkAdjustmentClass, "upper", "D");
	lpCache->value = (*env)->GetFieldID(env, lpCache->GtkAdjustmentClass, "value", "D");
	lpCache->step_increment = (*env)->GetFieldID(env, lpCache->GtkAdjustmentClass, "step_increment", "D");
	lpCache->page_increment = (*env)->GetFieldID(env, lpCache->GtkAdjustmentClass, "page_increment", "D");
	lpCache->page_size = (*env)->GetFieldID(env, lpCache->GtkAdjustmentClass, "page_size", "D");

	lpCache->cached = 1;
};

void cacheGtkCListRowFids(JNIEnv *env, jobject lpGtkCListRow, PGtkCListRow_FID_CACHE lpCache)
{
	if (lpCache->cached) return;

	lpCache->GtkCListRowClass = (*env)->GetObjectClass(env, lpGtkCListRow);
	lpCache->cell = (*env)->GetFieldID(env, lpCache->GtkCListRowClass, "cell", "I");
	lpCache->state = (*env)->GetFieldID(env, lpCache->GtkCListRowClass, "state", "I");
	lpCache->foreground_red = (*env)->GetFieldID(env, lpCache->GtkCListRowClass, "foreground_red", "S");
	lpCache->foreground_green = (*env)->GetFieldID(env, lpCache->GtkCListRowClass, "foreground_green", "S");
	lpCache->foreground_blue = (*env)->GetFieldID(env, lpCache->GtkCListRowClass, "foreground_blue", "S");
	lpCache->foreground_pixel = (*env)->GetFieldID(env, lpCache->GtkCListRowClass, "foreground_pixel", "I");
	lpCache->background_red = (*env)->GetFieldID(env, lpCache->GtkCListRowClass, "background_red", "S");
	lpCache->background_green = (*env)->GetFieldID(env, lpCache->GtkCListRowClass, "background_green", "S");
	lpCache->background_blue = (*env)->GetFieldID(env, lpCache->GtkCListRowClass, "background_blue", "S");
	lpCache->background_pixel = (*env)->GetFieldID(env, lpCache->GtkCListRowClass, "background_pixel", "I");
	lpCache->style = (*env)->GetFieldID(env, lpCache->GtkCListRowClass, "style", "I");
	lpCache->data = (*env)->GetFieldID(env, lpCache->GtkCListRowClass, "data", "I");
	lpCache->destroy = (*env)->GetFieldID(env, lpCache->GtkCListRowClass, "destroy", "I");
	lpCache->fg_set = (*env)->GetFieldID(env, lpCache->GtkCListRowClass, "fg_set", "I");
	lpCache->bg_set = (*env)->GetFieldID(env, lpCache->GtkCListRowClass, "bg_set", "I");
	lpCache->selectable = (*env)->GetFieldID(env, lpCache->GtkCListRowClass, "selectable", "I");

	lpCache->cached = 1;
}

void cacheGtkCListColumnFids(JNIEnv *env, jobject lpGtkCListColumn, PGtkCListColumn_FID_CACHE lpCache)
{
	if (lpCache->cached) return;

	lpCache->GtkCListColumnClass = (*env)->GetObjectClass(env, lpGtkCListColumn);
	lpCache->title = (*env)->GetFieldID(env, lpCache->GtkCListColumnClass, "title", "I");
	lpCache->area_x = (*env)->GetFieldID(env, lpCache->GtkCListColumnClass, "area_x", "S");
	lpCache->area_y = (*env)->GetFieldID(env, lpCache->GtkCListColumnClass, "area_y", "S");
	lpCache->area_width = (*env)->GetFieldID(env, lpCache->GtkCListColumnClass, "area_width", "S");
	lpCache->area_height = (*env)->GetFieldID(env, lpCache->GtkCListColumnClass, "area_height", "S");
	lpCache->button = (*env)->GetFieldID(env, lpCache->GtkCListColumnClass, "button", "I");
	lpCache->window = (*env)->GetFieldID(env, lpCache->GtkCListColumnClass, "window", "I");
	lpCache->width = (*env)->GetFieldID(env, lpCache->GtkCListColumnClass, "width", "I");
	lpCache->min_width = (*env)->GetFieldID(env, lpCache->GtkCListColumnClass, "min_width", "I");
	lpCache->max_width = (*env)->GetFieldID(env, lpCache->GtkCListColumnClass, "max_width", "I");
	lpCache->justification = (*env)->GetFieldID(env, lpCache->GtkCListColumnClass, "justification", "I");
	lpCache->visible = (*env)->GetFieldID(env, lpCache->GtkCListColumnClass, "visible", "I");
	lpCache->width_set = (*env)->GetFieldID(env, lpCache->GtkCListColumnClass, "width_set", "I");
	lpCache->resizeable = (*env)->GetFieldID(env, lpCache->GtkCListColumnClass, "resizeable", "I");
	lpCache->auto_resize = (*env)->GetFieldID(env, lpCache->GtkCListColumnClass, "auto_resize", "I");
	lpCache->button_passive = (*env)->GetFieldID(env, lpCache->GtkCListColumnClass, "button_passive", "I");

	lpCache->cached = 1;
}

void cacheGtkCTreeFids(JNIEnv *env, jobject lpGtkCTree, PGtkCTree_FID_CACHE lpCache)
{
	DECL_GLOB(pGlob)
	if (lpCache->cached) return;

	lpCache->GtkCTreeClass = (*env)->GetObjectClass(env, lpGtkCTree);
	cacheGtkCListFids(env, lpGtkCTree, &PGLOB(GtkCListFc));
	lpCache->tree_indent = (*env)->GetFieldID(env, lpCache->GtkCTreeClass, "tree_indent", "I");
	lpCache->tree_column = (*env)->GetFieldID(env, lpCache->GtkCTreeClass, "tree_column", "I");

	lpCache->cached = 1;
}

void cacheGtkCTreeRowFids(JNIEnv *env, jobject lpGtkCTreeRow, PGtkCTreeRow_FID_CACHE lpCache)
{
	DECL_GLOB(pGlob)
	if (lpCache->cached) return;

	lpCache->GtkCTreeRowClass = (*env)->GetObjectClass(env, lpGtkCTreeRow);
	cacheGtkCListRowFids(env, lpGtkCTreeRow, &PGLOB(GtkCListRowFc));
	lpCache->parent = (*env)->GetFieldID(env, lpCache->GtkCTreeRowClass, "parent", "I");
	lpCache->sibling = (*env)->GetFieldID(env, lpCache->GtkCTreeRowClass, "sibling", "I");
	lpCache->children = (*env)->GetFieldID(env, lpCache->GtkCTreeRowClass, "children", "I");
	lpCache->pixmap_closed = (*env)->GetFieldID(env, lpCache->GtkCTreeRowClass, "pixmap_closed", "I");
	lpCache->mask_closed = (*env)->GetFieldID(env, lpCache->GtkCTreeRowClass, "mask_closed", "I");
	lpCache->pixmap_opened = (*env)->GetFieldID(env, lpCache->GtkCTreeRowClass, "pixmap_opened", "I");
	lpCache->mask_opened = (*env)->GetFieldID(env, lpCache->GtkCTreeRowClass, "mask_opened", "I");
	lpCache->level = (*env)->GetFieldID(env, lpCache->GtkCTreeRowClass, "level", "S");
	lpCache->is_leaf = (*env)->GetFieldID(env, lpCache->GtkCTreeRowClass, "is_leaf", "I");
	lpCache->expanded = (*env)->GetFieldID(env, lpCache->GtkCTreeRowClass, "expanded", "I");

	lpCache->cached = 1;
}

/* ----------- swt getters and setters  ----------- */
/**
 * These functions get or set object field ids assuming that the
 * fids for these objects have already been cached.
 *
 */
void getGdkColorFields(JNIEnv *env, jobject lpObject, GdkColor *lpGdkColor, GdkColor_FID_CACHE *lpGdkColorFc)
{
	lpGdkColor->pixel = (*env)->GetIntField(env, lpObject, lpGdkColorFc->pixel);
	lpGdkColor->red = (*env)->GetShortField(env, lpObject, lpGdkColorFc->red);
	lpGdkColor->green = (*env)->GetShortField(env, lpObject, lpGdkColorFc->green);
	lpGdkColor->blue = (*env)->GetShortField(env, lpObject, lpGdkColorFc->blue);
}

void setGdkColorFields(JNIEnv *env, jobject lpObject, GdkColor *lpGdkColor, GdkColor_FID_CACHE *lpGdkColorFc)
{
	(*env)->SetIntField(env, lpObject, lpGdkColorFc->pixel, (jint)lpGdkColor->pixel);
	(*env)->SetShortField(env, lpObject, lpGdkColorFc->red, (jshort)lpGdkColor->red);
	(*env)->SetShortField(env, lpObject, lpGdkColorFc->green, (jshort)lpGdkColor->green);
	(*env)->SetShortField(env, lpObject, lpGdkColorFc->blue, (jshort)lpGdkColor->blue);
}

void getGdkEventExposeFields(JNIEnv *env, jobject lpObject, GdkEvent *lpGdkEvent, GdkEventExpose_FID_CACHE *lpGdkEventExposeFc)
{
	GdkEventExpose *lpGdkEventExpose = (GdkEventExpose*)lpGdkEvent;
	lpGdkEventExpose->area.x = (*env)->GetShortField(env, lpObject, lpGdkEventExposeFc->x);
	lpGdkEventExpose->area.y = (*env)->GetShortField(env, lpObject, lpGdkEventExposeFc->y);
	lpGdkEventExpose->area.width = (*env)->GetShortField(env, lpObject, lpGdkEventExposeFc->width);
	lpGdkEventExpose->area.height = (*env)->GetShortField(env, lpObject, lpGdkEventExposeFc->height);
	lpGdkEventExpose->region = (*env)->GetIntField(env, lpObject, lpGdkEventExposeFc->region);
	lpGdkEventExpose->count = (*env)->GetIntField(env, lpObject, lpGdkEventExposeFc->count);
}

void setGdkEventExposeFields(JNIEnv *env, jobject lpObject, GdkEvent *lpGdkEvent, GdkEventExpose_FID_CACHE *lpGdkEventExposeFc)
{
	GdkEventExpose *lpGdkEventExpose = (GdkEventExpose*)lpGdkEvent;
	(*env)->SetShortField(env, lpObject, lpGdkEventExposeFc->x, (jshort)lpGdkEventExpose->area.x);
	(*env)->SetShortField(env, lpObject, lpGdkEventExposeFc->y, (jshort)lpGdkEventExpose->area.y);
	(*env)->SetShortField(env, lpObject, lpGdkEventExposeFc->width, (jshort)lpGdkEventExpose->area.width);
	(*env)->SetShortField(env, lpObject, lpGdkEventExposeFc->height, (jshort)lpGdkEventExpose->area.height);
	(*env)->SetIntField(env, lpObject, lpGdkEventExposeFc->count, (jint)lpGdkEventExpose->count);
}

void getGdkFontFields(JNIEnv *env, jobject lpObject, GdkFont *lpGdkFont, GdkFont_FID_CACHE *lpGdkFontFc)
{
	lpGdkFont->type = (*env)->GetIntField(env, lpObject, lpGdkFontFc->type);
	lpGdkFont->ascent = (*env)->GetIntField(env, lpObject, lpGdkFontFc->ascent);
	lpGdkFont->descent = (*env)->GetIntField(env, lpObject, lpGdkFontFc->descent);
}

void setGdkFontFields(JNIEnv *env, jobject lpObject, GdkFont *lpGdkFont, GdkFont_FID_CACHE *lpGdkFontFc)
{
	(*env)->SetIntField(env, lpObject, lpGdkFontFc->type, (jint)lpGdkFont->type);
	(*env)->SetIntField(env, lpObject, lpGdkFontFc->ascent, (jint)lpGdkFont->ascent);
	(*env)->SetIntField(env, lpObject, lpGdkFontFc->descent, (jint)lpGdkFont->descent);
}

void getGdkGCValuesFields(JNIEnv *env, jobject lpObject, GdkGCValues *lpGdkGCValues, GdkGCValues_FID_CACHE *lpGdkGCValuesFc)
{
	lpGdkGCValues->foreground.pixel = (*env)->GetIntField(env, lpObject, lpGdkGCValuesFc->foreground_pixel);
	lpGdkGCValues->foreground.red = (*env)->GetShortField(env, lpObject, lpGdkGCValuesFc->foreground_red);
	lpGdkGCValues->foreground.green = (*env)->GetShortField(env, lpObject, lpGdkGCValuesFc->foreground_green);
	lpGdkGCValues->foreground.blue = (*env)->GetShortField(env, lpObject, lpGdkGCValuesFc->foreground_blue);
	lpGdkGCValues->background.pixel = (*env)->GetIntField(env, lpObject, lpGdkGCValuesFc->background_pixel);
	lpGdkGCValues->background.red = (*env)->GetShortField(env, lpObject, lpGdkGCValuesFc->background_red);
	lpGdkGCValues->background.green = (*env)->GetShortField(env, lpObject, lpGdkGCValuesFc->background_green);
	lpGdkGCValues->background.blue = (*env)->GetShortField(env, lpObject, lpGdkGCValuesFc->background_blue);
	lpGdkGCValues->font = (GdkFont*)(*env)->GetIntField(env, lpObject, lpGdkGCValuesFc->font);
	lpGdkGCValues->function = (*env)->GetIntField(env, lpObject, lpGdkGCValuesFc->function);
	lpGdkGCValues->fill = (*env)->GetIntField(env, lpObject, lpGdkGCValuesFc->fill);
	lpGdkGCValues->tile = (GdkPixmap*)(*env)->GetIntField(env, lpObject, lpGdkGCValuesFc->tile);
	lpGdkGCValues->stipple = (GdkPixmap*)(*env)->GetIntField(env, lpObject, lpGdkGCValuesFc->stipple);
	lpGdkGCValues->clip_mask = (GdkPixmap*)(*env)->GetIntField(env, lpObject, lpGdkGCValuesFc->clip_mask);
	lpGdkGCValues->subwindow_mode = (*env)->GetIntField(env, lpObject, lpGdkGCValuesFc->subwindow_mode);
	lpGdkGCValues->ts_x_origin = (*env)->GetIntField(env, lpObject, lpGdkGCValuesFc->ts_x_origin);
	lpGdkGCValues->ts_y_origin = (*env)->GetIntField(env, lpObject, lpGdkGCValuesFc->ts_y_origin);
	lpGdkGCValues->clip_x_origin = (*env)->GetIntField(env, lpObject, lpGdkGCValuesFc->clip_x_origin);
	lpGdkGCValues->clip_y_origin = (*env)->GetIntField(env, lpObject, lpGdkGCValuesFc->clip_y_origin);
	lpGdkGCValues->graphics_exposures = (*env)->GetIntField(env, lpObject, lpGdkGCValuesFc->graphics_exposures);
	lpGdkGCValues->line_width = (*env)->GetIntField(env, lpObject, lpGdkGCValuesFc->line_width);
	lpGdkGCValues->line_style = (*env)->GetIntField(env, lpObject, lpGdkGCValuesFc->line_style);
	lpGdkGCValues->cap_style = (*env)->GetIntField(env, lpObject, lpGdkGCValuesFc->cap_style);
	lpGdkGCValues->join_style = (*env)->GetIntField(env, lpObject, lpGdkGCValuesFc->join_style);
}

void setGdkGCValuesFields(JNIEnv *env, jobject lpObject, GdkGCValues *lpGdkGCValues, GdkGCValues_FID_CACHE *lpGdkGCValuesFc)
{
	(*env)->SetIntField(env, lpObject, lpGdkGCValuesFc->foreground_pixel, (jint)lpGdkGCValues->foreground.pixel);
	(*env)->SetShortField(env, lpObject, lpGdkGCValuesFc->foreground_red, (jshort)lpGdkGCValues->foreground.red);
	(*env)->SetShortField(env, lpObject, lpGdkGCValuesFc->foreground_green, (jshort)lpGdkGCValues->foreground.green);
	(*env)->SetShortField(env, lpObject, lpGdkGCValuesFc->foreground_blue, (jshort)lpGdkGCValues->foreground.blue);
	(*env)->SetIntField(env, lpObject, lpGdkGCValuesFc->background_pixel, (jint)lpGdkGCValues->background.pixel);
	(*env)->SetShortField(env, lpObject, lpGdkGCValuesFc->background_red, (jshort)lpGdkGCValues->background.red);
	(*env)->SetShortField(env, lpObject, lpGdkGCValuesFc->background_green, (jshort)lpGdkGCValues->background.green);
	(*env)->SetShortField(env, lpObject, lpGdkGCValuesFc->background_blue, (jshort)lpGdkGCValues->background.blue);
	(*env)->SetIntField(env, lpObject, lpGdkGCValuesFc->font, (jint)lpGdkGCValues->font);
	(*env)->SetIntField(env, lpObject, lpGdkGCValuesFc->function, (jint)lpGdkGCValues->function);
	(*env)->SetIntField(env, lpObject, lpGdkGCValuesFc->fill, (jint)lpGdkGCValues->fill);
	(*env)->SetIntField(env, lpObject, lpGdkGCValuesFc->tile, (jint)lpGdkGCValues->tile);
	(*env)->SetIntField(env, lpObject, lpGdkGCValuesFc->stipple, (jint)lpGdkGCValues->stipple);
	(*env)->SetIntField(env, lpObject, lpGdkGCValuesFc->clip_mask, (jint)lpGdkGCValues->clip_mask);
	(*env)->SetIntField(env, lpObject, lpGdkGCValuesFc->subwindow_mode, (jint)lpGdkGCValues->subwindow_mode);
	(*env)->SetIntField(env, lpObject, lpGdkGCValuesFc->ts_x_origin, (jint)lpGdkGCValues->ts_x_origin);
	(*env)->SetIntField(env, lpObject, lpGdkGCValuesFc->ts_y_origin, (jint)lpGdkGCValues->ts_y_origin);
	(*env)->SetIntField(env, lpObject, lpGdkGCValuesFc->clip_x_origin, (jint)lpGdkGCValues->clip_x_origin);
	(*env)->SetIntField(env, lpObject, lpGdkGCValuesFc->clip_y_origin, (jint)lpGdkGCValues->clip_y_origin);
	(*env)->SetIntField(env, lpObject, lpGdkGCValuesFc->graphics_exposures, (jint)lpGdkGCValues->graphics_exposures);
	(*env)->SetIntField(env, lpObject, lpGdkGCValuesFc->line_width, (jint)lpGdkGCValues->line_width);
	(*env)->SetIntField(env, lpObject, lpGdkGCValuesFc->line_style, (jint)lpGdkGCValues->line_style);
	(*env)->SetIntField(env, lpObject, lpGdkGCValuesFc->cap_style, (jint)lpGdkGCValues->cap_style);
	(*env)->SetIntField(env, lpObject, lpGdkGCValuesFc->join_style, (jint)lpGdkGCValues->join_style);
}

void getGdkRectangleFields(JNIEnv *env, jobject lpObject, GdkRectangle *lpGdkRectangle, GdkRectangle_FID_CACHE *lpGdkRectangleFc)
{
	lpGdkRectangle->x = (*env)->GetShortField(env, lpObject, lpGdkRectangleFc->x);
	lpGdkRectangle->y = (*env)->GetShortField(env, lpObject, lpGdkRectangleFc->y);
	lpGdkRectangle->width = (*env)->GetShortField(env, lpObject, lpGdkRectangleFc->width);
	lpGdkRectangle->height = (*env)->GetShortField(env, lpObject, lpGdkRectangleFc->height);
}

void setGdkRectangleFields(JNIEnv *env, jobject lpObject, GdkRectangle *lpGdkRectangle, GdkRectangle_FID_CACHE *lpGdkRectangleFc)
{
	(*env)->SetShortField(env, lpObject, lpGdkRectangleFc->x, (jshort)lpGdkRectangle->x);
	(*env)->SetShortField(env, lpObject, lpGdkRectangleFc->y, (jshort)lpGdkRectangle->y);
	(*env)->SetShortField(env, lpObject, lpGdkRectangleFc->width, (jshort)lpGdkRectangle->width);
	(*env)->SetShortField(env, lpObject, lpGdkRectangleFc->height, (jshort)lpGdkRectangle->height);
}

void getGdkVisualFields(JNIEnv *env, jobject lpObject, GdkVisual *lpGdkVisual, GdkVisual_FID_CACHE *lpGdkVisualFc)
{
	lpGdkVisual->type = (*env)->GetIntField(env, lpObject, lpGdkVisualFc->type);
	lpGdkVisual->depth = (*env)->GetIntField(env, lpObject, lpGdkVisualFc->depth);
	lpGdkVisual->byte_order = (*env)->GetIntField(env, lpObject, lpGdkVisualFc->byte_order);
	lpGdkVisual->colormap_size = (*env)->GetIntField(env, lpObject, lpGdkVisualFc->colormap_size);
	lpGdkVisual->bits_per_rgb = (*env)->GetIntField(env, lpObject, lpGdkVisualFc->bits_per_rgb);
	lpGdkVisual->red_mask = (*env)->GetIntField(env, lpObject, lpGdkVisualFc->red_mask);
	lpGdkVisual->red_shift = (*env)->GetIntField(env, lpObject, lpGdkVisualFc->red_shift);
	lpGdkVisual->red_prec = (*env)->GetIntField(env, lpObject, lpGdkVisualFc->red_prec);
	lpGdkVisual->green_mask = (*env)->GetIntField(env, lpObject, lpGdkVisualFc->green_mask);
	lpGdkVisual->green_shift = (*env)->GetIntField(env, lpObject, lpGdkVisualFc->green_shift);
	lpGdkVisual->green_prec = (*env)->GetIntField(env, lpObject, lpGdkVisualFc->green_prec);
	lpGdkVisual->blue_mask = (*env)->GetIntField(env, lpObject, lpGdkVisualFc->blue_mask);
	lpGdkVisual->blue_shift = (*env)->GetIntField(env, lpObject, lpGdkVisualFc->blue_shift);
	lpGdkVisual->blue_prec = (*env)->GetIntField(env, lpObject, lpGdkVisualFc->blue_prec);
}

void setGdkVisualFields(JNIEnv *env, jobject lpObject, GdkVisual *lpGdkVisual, GdkVisual_FID_CACHE *lpGdkVisualFc)
{
	(*env)->SetIntField(env, lpObject, lpGdkVisualFc->type, (jint)lpGdkVisual->type);
	(*env)->SetIntField(env, lpObject, lpGdkVisualFc->depth, (jint)lpGdkVisual->depth);
	(*env)->SetIntField(env, lpObject, lpGdkVisualFc->byte_order, (jint)lpGdkVisual->byte_order);
	(*env)->SetIntField(env, lpObject, lpGdkVisualFc->colormap_size, (jint)lpGdkVisual->colormap_size);
	(*env)->SetIntField(env, lpObject, lpGdkVisualFc->bits_per_rgb, (jint)lpGdkVisual->bits_per_rgb);
	(*env)->SetIntField(env, lpObject, lpGdkVisualFc->red_mask, (jint)lpGdkVisual->red_mask);
	(*env)->SetIntField(env, lpObject, lpGdkVisualFc->red_shift, (jint)lpGdkVisual->red_shift);
	(*env)->SetIntField(env, lpObject, lpGdkVisualFc->red_prec, (jint)lpGdkVisual->red_prec);
	(*env)->SetIntField(env, lpObject, lpGdkVisualFc->green_mask, (jint)lpGdkVisual->green_mask);
	(*env)->SetIntField(env, lpObject, lpGdkVisualFc->green_shift, (jint)lpGdkVisual->green_shift);
	(*env)->SetIntField(env, lpObject, lpGdkVisualFc->green_prec, (jint)lpGdkVisual->green_prec);
	(*env)->SetIntField(env, lpObject, lpGdkVisualFc->blue_mask, (jint)lpGdkVisual->blue_mask);
	(*env)->SetIntField(env, lpObject, lpGdkVisualFc->blue_shift, (jint)lpGdkVisual->blue_shift);
	(*env)->SetIntField(env, lpObject, lpGdkVisualFc->blue_prec, (jint)lpGdkVisual->blue_prec);
}

void getGtkAllocationFields(JNIEnv *env, jobject lpObject, GtkAllocation *lpGtkAllocation, GtkAllocation_FID_CACHE *lpGtkAllocationFc)
{
	lpGtkAllocation->x = (*env)->GetShortField(env, lpObject, lpGtkAllocationFc->x);
	lpGtkAllocation->y = (*env)->GetShortField(env, lpObject, lpGtkAllocationFc->y);
	lpGtkAllocation->width = (*env)->GetShortField(env, lpObject, lpGtkAllocationFc->width);
	lpGtkAllocation->height = (*env)->GetShortField(env, lpObject, lpGtkAllocationFc->height);
}

void setGtkAllocationFields(JNIEnv *env, jobject lpObject, GtkAllocation *lpGtkAllocation, GtkAllocation_FID_CACHE *lpGtkAllocationFc)
{
	(*env)->SetShortField(env, lpObject, lpGtkAllocationFc->x, lpGtkAllocation->x);
	(*env)->SetShortField(env, lpObject, lpGtkAllocationFc->y, lpGtkAllocation->y);
	(*env)->SetShortField(env, lpObject, lpGtkAllocationFc->width, lpGtkAllocation->width);
	(*env)->SetShortField(env, lpObject, lpGtkAllocationFc->height, lpGtkAllocation->height);
}

void getGtkCListFields(JNIEnv *env, jobject lpObject, GtkCList *lpGtkCList, GtkCList_FID_CACHE *lpGtkCListFc)
{
	DECL_GLOB(pGlob)
	lpGtkCList->flags = (*env)->GetShortField(env, lpObject, lpGtkCListFc->clist_flags);
	lpGtkCList->row_mem_chunk = (GMemChunk*)(*env)->GetIntField(env, lpObject, lpGtkCListFc->row_mem_chunk);
	lpGtkCList->cell_mem_chunk = (GMemChunk*)(*env)->GetIntField(env, lpObject, lpGtkCListFc->cell_mem_chunk);
	lpGtkCList->freeze_count = (*env)->GetIntField(env, lpObject, lpGtkCListFc->freeze_count);
	lpGtkCList->internal_allocation.x = (*env)->GetShortField(env, lpObject, lpGtkCListFc->internal_allocation_x);
	lpGtkCList->internal_allocation.y = (*env)->GetShortField(env, lpObject, lpGtkCListFc->internal_allocation_y);
	lpGtkCList->internal_allocation.width = (*env)->GetShortField(env, lpObject, lpGtkCListFc->internal_allocation_width);
	lpGtkCList->internal_allocation.height = (*env)->GetShortField(env, lpObject, lpGtkCListFc->internal_allocation_height);
	lpGtkCList->rows = (*env)->GetIntField(env, lpObject, lpGtkCListFc->rows);
	lpGtkCList->row_height = (*env)->GetIntField(env, lpObject, lpGtkCListFc->row_height);
	lpGtkCList->row_list = (GList*)(*env)->GetIntField(env, lpObject, lpGtkCListFc->row_list);
	lpGtkCList->row_list_end = (GList*)(*env)->GetIntField(env, lpObject, lpGtkCListFc->row_list_end);
	lpGtkCList->columns = (*env)->GetIntField(env, lpObject, lpGtkCListFc->columns);
	lpGtkCList->column_title_area.x = (*env)->GetShortField(env, lpObject, lpGtkCListFc->column_title_area_x);
	lpGtkCList->column_title_area.y = (*env)->GetShortField(env, lpObject, lpGtkCListFc->column_title_area_y);
	lpGtkCList->column_title_area.width = (*env)->GetShortField(env, lpObject, lpGtkCListFc->column_title_area_width);
	lpGtkCList->column_title_area.height = (*env)->GetShortField(env, lpObject, lpGtkCListFc->column_title_area_height);
	lpGtkCList->title_window = (GdkWindow*)(*env)->GetIntField(env, lpObject, lpGtkCListFc->title_window);
	lpGtkCList->column = (GtkCListColumn*)(*env)->GetIntField(env, lpObject, lpGtkCListFc->column);
	lpGtkCList->clist_window = (GdkWindow*)(*env)->GetIntField(env, lpObject, lpGtkCListFc->clist_window);
	lpGtkCList->clist_window_width = (*env)->GetIntField(env, lpObject, lpGtkCListFc->clist_window_width);
	lpGtkCList->clist_window_height = (*env)->GetIntField(env, lpObject, lpGtkCListFc->clist_window_height);
	lpGtkCList->hoffset = (*env)->GetIntField(env, lpObject, lpGtkCListFc->hoffset);
	lpGtkCList->voffset = (*env)->GetIntField(env, lpObject, lpGtkCListFc->voffset);
	lpGtkCList->shadow_type = (*env)->GetIntField(env, lpObject, lpGtkCListFc->shadow_type);
	lpGtkCList->selection_mode = (*env)->GetIntField(env, lpObject, lpGtkCListFc->selection_mode);
	lpGtkCList->selection = (GList*)(*env)->GetIntField(env, lpObject, lpGtkCListFc->selection);
	lpGtkCList->selection_end = (GList*)(*env)->GetIntField(env, lpObject, lpGtkCListFc->selection_end);
	lpGtkCList->undo_selection = (GList*)(*env)->GetIntField(env, lpObject, lpGtkCListFc->undo_selection);
	lpGtkCList->undo_unselection = (GList*)(*env)->GetIntField(env, lpObject, lpGtkCListFc->undo_unselection);
	lpGtkCList->undo_anchor = (*env)->GetIntField(env, lpObject, lpGtkCListFc->undo_anchor);
	lpGtkCList->button_actions[0] = (*env)->GetByteField(env, lpObject, lpGtkCListFc->button_actions0);
	lpGtkCList->button_actions[1] = (*env)->GetByteField(env, lpObject, lpGtkCListFc->button_actions1);
	lpGtkCList->button_actions[2] = (*env)->GetByteField(env, lpObject, lpGtkCListFc->button_actions2);
	lpGtkCList->button_actions[3] = (*env)->GetByteField(env, lpObject, lpGtkCListFc->button_actions3);
	lpGtkCList->button_actions[4] = (*env)->GetByteField(env, lpObject, lpGtkCListFc->button_actions4);
	lpGtkCList->drag_button = (*env)->GetByteField(env, lpObject, lpGtkCListFc->drag_button);
	lpGtkCList->click_cell.row = (*env)->GetIntField(env, lpObject, lpGtkCListFc->click_cell_row);
	lpGtkCList->click_cell.column = (*env)->GetIntField(env, lpObject, lpGtkCListFc->click_cell_column);
	lpGtkCList->hadjustment = (GtkAdjustment*)(*env)->GetIntField(env, lpObject, lpGtkCListFc->hadjustment);
	lpGtkCList->vadjustment = (GtkAdjustment*)(*env)->GetIntField(env, lpObject, lpGtkCListFc->vadjustment);
	lpGtkCList->xor_gc = (GdkGC*)(*env)->GetIntField(env, lpObject, lpGtkCListFc->xor_gc);
	lpGtkCList->bg_gc = (GdkGC*)(*env)->GetIntField(env, lpObject, lpGtkCListFc->bg_gc);
	lpGtkCList->bg_gc = (GdkGC*)(*env)->GetIntField(env, lpObject, lpGtkCListFc->bg_gc);
	lpGtkCList->cursor_drag = (GdkCursor*)(*env)->GetIntField(env, lpObject, lpGtkCListFc->cursor_drag);
	lpGtkCList->x_drag = (*env)->GetIntField(env, lpObject, lpGtkCListFc->x_drag);
	lpGtkCList->focus_row = (*env)->GetIntField(env, lpObject, lpGtkCListFc->focus_row);
	lpGtkCList->anchor = (*env)->GetIntField(env, lpObject, lpGtkCListFc->anchor);
	lpGtkCList->anchor_state = (*env)->GetIntField(env, lpObject, lpGtkCListFc->anchor_state);
	lpGtkCList->drag_pos = (*env)->GetIntField(env, lpObject, lpGtkCListFc->drag_pos);
	lpGtkCList->htimer = (*env)->GetIntField(env, lpObject, lpGtkCListFc->htimer);
	lpGtkCList->vtimer = (*env)->GetIntField(env, lpObject, lpGtkCListFc->vtimer);
	lpGtkCList->sort_type = (*env)->GetIntField(env, lpObject, lpGtkCListFc->sort_type);
	lpGtkCList->compare = (GtkCListCompareFunc)(*env)->GetIntField(env, lpObject, lpGtkCListFc->compare);
	lpGtkCList->sort_column = (*env)->GetIntField(env, lpObject, lpGtkCListFc->sort_column);
}

void setGtkCListFields(JNIEnv *env, jobject lpObject, GtkCList *lpGtkCList, GtkCList_FID_CACHE *lpGtkCListFc)
{
	DECL_GLOB(pGlob)
	(*env)->SetShortField(env, lpObject, lpGtkCListFc->clist_flags, (jshort)lpGtkCList->flags);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->row_mem_chunk, (jint)lpGtkCList->row_mem_chunk);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->cell_mem_chunk, (jint)lpGtkCList->cell_mem_chunk);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->freeze_count, (jint)lpGtkCList->freeze_count);
	(*env)->SetShortField(env, lpObject, lpGtkCListFc->internal_allocation_x, (jshort)lpGtkCList->internal_allocation.x);
	(*env)->SetShortField(env, lpObject, lpGtkCListFc->internal_allocation_y, (jshort)lpGtkCList->internal_allocation.y);
	(*env)->SetShortField(env, lpObject, lpGtkCListFc->internal_allocation_width, (jshort)lpGtkCList->internal_allocation.width);
	(*env)->SetShortField(env, lpObject, lpGtkCListFc->internal_allocation_height, (jshort)lpGtkCList->internal_allocation.height);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->rows, (jint)lpGtkCList->rows);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->row_height, (jint)lpGtkCList->row_height);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->row_list, (jint)lpGtkCList->row_list);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->row_list_end, (jint)lpGtkCList->row_list_end);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->columns, (jint)lpGtkCList->columns);
	(*env)->SetShortField(env, lpObject, lpGtkCListFc->column_title_area_x, (jshort)lpGtkCList->column_title_area.x);
	(*env)->SetShortField(env, lpObject, lpGtkCListFc->column_title_area_y, (jshort)lpGtkCList->column_title_area.y);
	(*env)->SetShortField(env, lpObject, lpGtkCListFc->column_title_area_width, (jshort)lpGtkCList->column_title_area.width);
	(*env)->SetShortField(env, lpObject, lpGtkCListFc->column_title_area_height, (jshort)lpGtkCList->column_title_area.height);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->title_window, (jint)lpGtkCList->title_window);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->column, (jint)lpGtkCList->column);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->clist_window, (jint)lpGtkCList->clist_window);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->clist_window_width, (jint)lpGtkCList->clist_window_width);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->clist_window_height, (jint)lpGtkCList->clist_window_height);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->hoffset, (jint)lpGtkCList->hoffset);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->voffset, (jint)lpGtkCList->voffset);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->shadow_type, (jint)lpGtkCList->shadow_type);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->selection_mode, (jint)lpGtkCList->selection_mode);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->selection, (jint)lpGtkCList->selection);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->selection_end, (jint)lpGtkCList->selection_end);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->undo_selection, (jint)lpGtkCList->undo_selection);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->undo_unselection, (jint)lpGtkCList->undo_unselection);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->undo_anchor, (jint)lpGtkCList->undo_anchor);
	(*env)->SetByteField(env, lpObject, lpGtkCListFc->button_actions0, (jbyte)lpGtkCList->button_actions[0]);
	(*env)->SetByteField(env, lpObject, lpGtkCListFc->button_actions1, (jbyte)lpGtkCList->button_actions[1]);
	(*env)->SetByteField(env, lpObject, lpGtkCListFc->button_actions2, (jbyte)lpGtkCList->button_actions[2]);
	(*env)->SetByteField(env, lpObject, lpGtkCListFc->button_actions3, (jbyte)lpGtkCList->button_actions[3]);
	(*env)->SetByteField(env, lpObject, lpGtkCListFc->button_actions4, (jbyte)lpGtkCList->button_actions[4]);
	(*env)->SetByteField(env, lpObject, lpGtkCListFc->drag_button, (jbyte)lpGtkCList->drag_button);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->click_cell_row, (jint)lpGtkCList->click_cell.row);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->click_cell_column, (jint)lpGtkCList->click_cell.column);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->hadjustment, (jint)lpGtkCList->hadjustment);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->vadjustment, (jint)lpGtkCList->vadjustment);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->xor_gc, (jint)lpGtkCList->xor_gc);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->bg_gc, (jint)lpGtkCList->bg_gc);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->bg_gc, (jint)lpGtkCList->bg_gc);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->cursor_drag, (jint)lpGtkCList->cursor_drag);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->x_drag, (jint)lpGtkCList->x_drag);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->focus_row, (jint)lpGtkCList->focus_row);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->anchor, (jint)lpGtkCList->anchor);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->anchor_state, (jint)lpGtkCList->anchor_state);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->drag_pos, (jint)lpGtkCList->drag_pos);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->htimer, (jint)lpGtkCList->htimer);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->vtimer, (jint)lpGtkCList->vtimer);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->sort_type, (jint)lpGtkCList->sort_type);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->compare, (jint)lpGtkCList->compare);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->sort_column, (jint)lpGtkCList->sort_column);
}

void getGtkComboFields(JNIEnv *env, jobject lpObject, GtkCombo *lpGtkCombo, GtkCombo_FID_CACHE *lpGtkComboFc)
{
	DECL_GLOB(pGlob)
	lpGtkCombo->entry = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkComboFc->entry);
	lpGtkCombo->list = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkComboFc->list);
}

void setGtkComboFields(JNIEnv *env, jobject lpObject, GtkCombo *lpGtkCombo, GtkCombo_FID_CACHE *lpGtkComboFc)
{
	DECL_GLOB(pGlob)
	(*env)->SetIntField(env, lpObject, lpGtkComboFc->entry, (jint)lpGtkCombo->entry);
	(*env)->SetIntField(env, lpObject, lpGtkComboFc->list, (jint)lpGtkCombo->list);
}

void getGtkRequisitionFields(JNIEnv *env, jobject lpObject, GtkRequisition *lpGtkRequisition, GtkRequisition_FID_CACHE *lpGtkRequisitionFc)
{
	lpGtkRequisition->width = (*env)->GetShortField(env, lpObject, lpGtkRequisitionFc->width);
	lpGtkRequisition->height = (*env)->GetShortField(env, lpObject, lpGtkRequisitionFc->height);
}

void setGtkRequisitionFields(JNIEnv *env, jobject lpObject, GtkRequisition *lpGtkRequisition, GtkRequisition_FID_CACHE *lpGtkRequisitionFc)
{
	(*env)->SetShortField(env, lpObject, lpGtkRequisitionFc->width, lpGtkRequisition->width);
	(*env)->SetShortField(env, lpObject, lpGtkRequisitionFc->height, lpGtkRequisition->height);
}

void getGtkStyleFields(JNIEnv *env, jobject lpObject, GtkStyle *lpGtkStyle, GtkStyle_FID_CACHE *lpGtkStyleFc)
{
	lpGtkStyle->fg[0].pixel = (*env)->GetIntField(env, lpObject, lpGtkStyleFc->fg0_pixel);
	lpGtkStyle->fg[0].red = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->fg0_red);
	lpGtkStyle->fg[0].green = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->fg0_green);
	lpGtkStyle->fg[0].blue = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->fg0_blue);
	lpGtkStyle->fg[1].pixel = (*env)->GetIntField(env, lpObject, lpGtkStyleFc->fg1_pixel);
	lpGtkStyle->fg[1].red = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->fg1_red);
	lpGtkStyle->fg[1].green = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->fg1_green);
	lpGtkStyle->fg[1].blue = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->fg1_blue);
	lpGtkStyle->fg[2].pixel = (*env)->GetIntField(env, lpObject, lpGtkStyleFc->fg2_pixel);
	lpGtkStyle->fg[2].red = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->fg2_red);
	lpGtkStyle->fg[2].green = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->fg2_green);
	lpGtkStyle->fg[2].blue = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->fg2_blue);
	lpGtkStyle->fg[3].pixel = (*env)->GetIntField(env, lpObject, lpGtkStyleFc->fg3_pixel);
	lpGtkStyle->fg[3].red = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->fg3_red);
	lpGtkStyle->fg[3].green = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->fg3_green);
	lpGtkStyle->fg[3].blue = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->fg3_blue);
	lpGtkStyle->fg[4].pixel = (*env)->GetIntField(env, lpObject, lpGtkStyleFc->fg4_pixel);
	lpGtkStyle->fg[4].red = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->fg4_red);
	lpGtkStyle->fg[4].green = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->fg4_green);
	lpGtkStyle->fg[4].blue = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->fg4_blue);
	lpGtkStyle->bg[0].pixel = (*env)->GetIntField(env, lpObject, lpGtkStyleFc->bg0_pixel);
	lpGtkStyle->bg[0].red = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->bg0_red);
	lpGtkStyle->bg[0].green = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->bg0_green);
	lpGtkStyle->bg[0].blue = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->bg0_blue);
	lpGtkStyle->bg[1].pixel = (*env)->GetIntField(env, lpObject, lpGtkStyleFc->bg1_pixel);
	lpGtkStyle->bg[1].red = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->bg1_red);
	lpGtkStyle->bg[1].green = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->bg1_green);
	lpGtkStyle->bg[1].blue = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->bg1_blue);
	lpGtkStyle->bg[2].pixel = (*env)->GetIntField(env, lpObject, lpGtkStyleFc->bg2_pixel);
	lpGtkStyle->bg[2].red = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->bg2_red);
	lpGtkStyle->bg[2].green = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->bg2_green);
	lpGtkStyle->bg[2].blue = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->bg2_blue);
	lpGtkStyle->bg[3].pixel = (*env)->GetIntField(env, lpObject, lpGtkStyleFc->bg3_pixel);
	lpGtkStyle->bg[3].red = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->bg3_red);
	lpGtkStyle->bg[3].green = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->bg3_green);
	lpGtkStyle->bg[3].blue = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->bg3_blue);
	lpGtkStyle->bg[4].pixel = (*env)->GetIntField(env, lpObject, lpGtkStyleFc->bg4_pixel);
	lpGtkStyle->bg[4].red = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->bg4_red);
	lpGtkStyle->bg[4].green = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->bg4_green);
	lpGtkStyle->bg[4].blue = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->bg4_blue);
	lpGtkStyle->light[0].pixel = (*env)->GetIntField(env, lpObject, lpGtkStyleFc->light0_pixel);
	lpGtkStyle->light[0].red = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->light0_red);
	lpGtkStyle->light[0].green = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->light0_green);
	lpGtkStyle->light[0].blue = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->light0_blue);
	lpGtkStyle->light[1].pixel = (*env)->GetIntField(env, lpObject, lpGtkStyleFc->light1_pixel);
	lpGtkStyle->light[1].red = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->light1_red);
	lpGtkStyle->light[1].green = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->light1_green);
	lpGtkStyle->light[1].blue = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->light1_blue);
	lpGtkStyle->light[2].pixel = (*env)->GetIntField(env, lpObject, lpGtkStyleFc->light2_pixel);
	lpGtkStyle->light[2].red = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->light2_red);
	lpGtkStyle->light[2].green = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->light2_green);
	lpGtkStyle->light[2].blue = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->light2_blue);
	lpGtkStyle->light[3].pixel = (*env)->GetIntField(env, lpObject, lpGtkStyleFc->light3_pixel);
	lpGtkStyle->light[3].red = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->light3_red);
	lpGtkStyle->light[3].green = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->light3_green);
	lpGtkStyle->light[3].blue = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->light3_blue);
	lpGtkStyle->light[4].pixel = (*env)->GetIntField(env, lpObject, lpGtkStyleFc->light4_pixel);
	lpGtkStyle->light[4].red = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->light4_red);
	lpGtkStyle->light[4].green = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->light4_green);
	lpGtkStyle->light[4].blue = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->light4_blue);
	lpGtkStyle->dark[0].pixel = (*env)->GetIntField(env, lpObject, lpGtkStyleFc->dark0_pixel);
	lpGtkStyle->dark[0].red = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->dark0_red);
	lpGtkStyle->dark[0].green = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->dark0_green);
	lpGtkStyle->dark[0].blue = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->dark0_blue);
	lpGtkStyle->dark[1].pixel = (*env)->GetIntField(env, lpObject, lpGtkStyleFc->dark1_pixel);
	lpGtkStyle->dark[1].red = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->dark1_red);
	lpGtkStyle->dark[1].green = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->dark1_green);
	lpGtkStyle->dark[1].blue = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->dark1_blue);
	lpGtkStyle->dark[2].pixel = (*env)->GetIntField(env, lpObject, lpGtkStyleFc->dark2_pixel);
	lpGtkStyle->dark[2].red = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->dark2_red);
	lpGtkStyle->dark[2].green = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->dark2_green);
	lpGtkStyle->dark[2].blue = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->dark2_blue);
	lpGtkStyle->dark[3].pixel = (*env)->GetIntField(env, lpObject, lpGtkStyleFc->dark3_pixel);
	lpGtkStyle->dark[3].red = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->dark3_red);
	lpGtkStyle->dark[3].green = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->dark3_green);
	lpGtkStyle->dark[3].blue = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->dark3_blue);
	lpGtkStyle->dark[4].pixel = (*env)->GetIntField(env, lpObject, lpGtkStyleFc->dark4_pixel);
	lpGtkStyle->dark[4].red = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->dark4_red);
	lpGtkStyle->dark[4].green = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->dark4_green);
	lpGtkStyle->dark[4].blue = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->dark4_blue);
	lpGtkStyle->mid[0].pixel = (*env)->GetIntField(env, lpObject, lpGtkStyleFc->mid0_pixel);
	lpGtkStyle->mid[0].red = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->mid0_red);
	lpGtkStyle->mid[0].green = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->mid0_green);
	lpGtkStyle->mid[0].blue = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->mid0_blue);
	lpGtkStyle->mid[1].pixel = (*env)->GetIntField(env, lpObject, lpGtkStyleFc->mid1_pixel);
	lpGtkStyle->mid[1].red = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->mid1_red);
	lpGtkStyle->mid[1].green = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->mid1_green);
	lpGtkStyle->mid[1].blue = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->mid1_blue);
	lpGtkStyle->mid[2].pixel = (*env)->GetIntField(env, lpObject, lpGtkStyleFc->mid2_pixel);
	lpGtkStyle->mid[2].red = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->mid2_red);
	lpGtkStyle->mid[2].green = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->mid2_green);
	lpGtkStyle->mid[2].blue = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->mid2_blue);
	lpGtkStyle->mid[3].pixel = (*env)->GetIntField(env, lpObject, lpGtkStyleFc->mid3_pixel);
	lpGtkStyle->mid[3].red = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->mid3_red);
	lpGtkStyle->mid[3].green = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->mid3_green);
	lpGtkStyle->mid[3].blue = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->mid3_blue);
	lpGtkStyle->mid[4].pixel = (*env)->GetIntField(env, lpObject, lpGtkStyleFc->mid4_pixel);
	lpGtkStyle->mid[4].red = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->mid4_red);
	lpGtkStyle->mid[4].green = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->mid4_green);
	lpGtkStyle->mid[4].blue = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->mid4_blue);
	lpGtkStyle->text[0].pixel = (*env)->GetIntField(env, lpObject, lpGtkStyleFc->text0_pixel);
	lpGtkStyle->text[0].red = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->text0_red);
	lpGtkStyle->text[0].green = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->text0_green);
	lpGtkStyle->text[0].blue = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->text0_blue);
	lpGtkStyle->text[1].pixel = (*env)->GetIntField(env, lpObject, lpGtkStyleFc->text1_pixel);
	lpGtkStyle->text[1].red = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->text1_red);
	lpGtkStyle->text[1].green = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->text1_green);
	lpGtkStyle->text[1].blue = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->text1_blue);
	lpGtkStyle->text[2].pixel = (*env)->GetIntField(env, lpObject, lpGtkStyleFc->text2_pixel);
	lpGtkStyle->text[2].red = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->text2_red);
	lpGtkStyle->text[2].green = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->text2_green);
	lpGtkStyle->text[2].blue = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->text2_blue);
	lpGtkStyle->text[3].pixel = (*env)->GetIntField(env, lpObject, lpGtkStyleFc->text3_pixel);
	lpGtkStyle->text[3].red = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->text3_red);
	lpGtkStyle->text[3].green = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->text3_green);
	lpGtkStyle->text[3].blue = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->text3_blue);
	lpGtkStyle->text[4].pixel = (*env)->GetIntField(env, lpObject, lpGtkStyleFc->text4_pixel);
	lpGtkStyle->text[4].red = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->text4_red);
	lpGtkStyle->text[4].green = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->text4_green);
	lpGtkStyle->text[4].blue = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->text4_blue);
	lpGtkStyle->base[0].pixel = (*env)->GetIntField(env, lpObject, lpGtkStyleFc->base0_pixel);
	lpGtkStyle->base[0].red = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->base0_red);
	lpGtkStyle->base[0].green = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->base0_green);
	lpGtkStyle->base[0].blue = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->base0_blue);
	lpGtkStyle->base[1].pixel = (*env)->GetIntField(env, lpObject, lpGtkStyleFc->base1_pixel);
	lpGtkStyle->base[1].red = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->base1_red);
	lpGtkStyle->base[1].green = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->base1_green);
	lpGtkStyle->base[1].blue = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->base1_blue);
	lpGtkStyle->base[2].pixel = (*env)->GetIntField(env, lpObject, lpGtkStyleFc->base2_pixel);
	lpGtkStyle->base[2].red = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->base2_red);
	lpGtkStyle->base[2].green = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->base2_green);
	lpGtkStyle->base[2].blue = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->base2_blue);
	lpGtkStyle->base[3].pixel = (*env)->GetIntField(env, lpObject, lpGtkStyleFc->base3_pixel);
	lpGtkStyle->base[3].red = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->base3_red);
	lpGtkStyle->base[3].green = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->base3_green);
	lpGtkStyle->base[3].blue = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->base3_blue);
	lpGtkStyle->base[4].pixel = (*env)->GetIntField(env, lpObject, lpGtkStyleFc->base4_pixel);
	lpGtkStyle->base[4].red = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->base4_red);
	lpGtkStyle->base[4].green = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->base4_green);
	lpGtkStyle->base[4].blue = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->base4_blue);
	lpGtkStyle->black.pixel = (*env)->GetIntField(env, lpObject, lpGtkStyleFc->black_pixel);
	lpGtkStyle->black.red = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->black_red);
	lpGtkStyle->black.green = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->black_green);
	lpGtkStyle->black.blue = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->black_blue);
	lpGtkStyle->white.pixel = (*env)->GetIntField(env, lpObject, lpGtkStyleFc->white_pixel);
	lpGtkStyle->white.red = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->white_red);
	lpGtkStyle->white.green = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->white_green);
	lpGtkStyle->white.blue = (*env)->GetShortField(env, lpObject, lpGtkStyleFc->white_blue);
	lpGtkStyle->fg_gc[0] = (GdkGC*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->fg_gc0);
	lpGtkStyle->fg_gc[1] = (GdkGC*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->fg_gc1);
	lpGtkStyle->fg_gc[2] = (GdkGC*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->fg_gc2);
	lpGtkStyle->fg_gc[3] = (GdkGC*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->fg_gc3);
	lpGtkStyle->fg_gc[4] = (GdkGC*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->fg_gc4);
	lpGtkStyle->bg_gc[0] = (GdkGC*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->bg_gc0);
	lpGtkStyle->bg_gc[1] = (GdkGC*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->bg_gc1);
	lpGtkStyle->bg_gc[2] = (GdkGC*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->bg_gc2);
	lpGtkStyle->bg_gc[3] = (GdkGC*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->bg_gc3);
	lpGtkStyle->bg_gc[4] = (GdkGC*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->bg_gc4);
	lpGtkStyle->light_gc[0] = (GdkGC*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->light_gc0);
	lpGtkStyle->light_gc[1] = (GdkGC*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->light_gc1);
	lpGtkStyle->light_gc[2] = (GdkGC*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->light_gc2);
	lpGtkStyle->light_gc[3] = (GdkGC*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->light_gc3);
	lpGtkStyle->light_gc[4] = (GdkGC*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->light_gc4);
	lpGtkStyle->dark_gc[0] = (GdkGC*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->dark_gc0);
	lpGtkStyle->dark_gc[1] = (GdkGC*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->dark_gc1);
	lpGtkStyle->dark_gc[2] = (GdkGC*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->dark_gc2);
	lpGtkStyle->dark_gc[3] = (GdkGC*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->dark_gc3);
	lpGtkStyle->dark_gc[4] = (GdkGC*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->dark_gc4);
	lpGtkStyle->mid_gc[0] = (GdkGC*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->mid_gc0);
	lpGtkStyle->mid_gc[1] = (GdkGC*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->mid_gc1);
	lpGtkStyle->mid_gc[2] = (GdkGC*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->mid_gc2);
	lpGtkStyle->mid_gc[3] = (GdkGC*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->mid_gc3);
	lpGtkStyle->mid_gc[4] = (GdkGC*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->mid_gc4);
	lpGtkStyle->text_gc[0] = (GdkGC*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->text_gc0);
	lpGtkStyle->text_gc[1] = (GdkGC*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->text_gc1);
	lpGtkStyle->text_gc[2] = (GdkGC*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->text_gc2);
	lpGtkStyle->text_gc[3] = (GdkGC*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->text_gc3);
	lpGtkStyle->text_gc[4] = (GdkGC*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->text_gc4);
	lpGtkStyle->base_gc[0] = (GdkGC*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->base_gc0);
	lpGtkStyle->base_gc[1] = (GdkGC*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->base_gc1);
	lpGtkStyle->base_gc[2] = (GdkGC*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->base_gc2);
	lpGtkStyle->base_gc[3] = (GdkGC*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->base_gc3);
	lpGtkStyle->base_gc[4] = (GdkGC*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->base_gc4);
	lpGtkStyle->black_gc = (GdkGC*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->black_gc);
	lpGtkStyle->white_gc = (GdkGC*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->white_gc);
	lpGtkStyle->bg_pixmap[0] = (GdkPixmap*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->bg_pixmap0);
	lpGtkStyle->bg_pixmap[1] = (GdkPixmap*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->bg_pixmap1);
	lpGtkStyle->bg_pixmap[2] = (GdkPixmap*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->bg_pixmap2);
	lpGtkStyle->bg_pixmap[3] = (GdkPixmap*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->bg_pixmap3);
	lpGtkStyle->bg_pixmap[4] = (GdkPixmap*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->bg_pixmap4);
}

void setGtkStyleFields(JNIEnv *env, jobject lpObject, GtkStyle *lpGtkStyle, GtkStyle_FID_CACHE *lpGtkStyleFc)
{
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->fg0_pixel, (jint)lpGtkStyle->fg[0].pixel);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->fg0_red, (jshort)lpGtkStyle->fg[0].red);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->fg0_green, (jshort)lpGtkStyle->fg[0].green);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->fg0_blue, (jshort)lpGtkStyle->fg[0].blue);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->fg1_pixel, (jint)lpGtkStyle->fg[1].pixel);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->fg1_red, (jshort)lpGtkStyle->fg[1].red);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->fg1_green, (jshort)lpGtkStyle->fg[1].green);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->fg1_blue, (jshort)lpGtkStyle->fg[1].blue);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->fg2_pixel, (jint)lpGtkStyle->fg[2].pixel);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->fg2_red, (jshort)lpGtkStyle->fg[2].red);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->fg2_green, (jshort)lpGtkStyle->fg[2].green);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->fg2_blue, (jshort)lpGtkStyle->fg[2].blue);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->fg3_pixel, (jint)lpGtkStyle->fg[3].pixel);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->fg3_red, (jshort)lpGtkStyle->fg[3].red);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->fg3_green, (jshort)lpGtkStyle->fg[3].green);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->fg3_blue, (jshort)lpGtkStyle->fg[3].blue);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->fg4_pixel, (jint)lpGtkStyle->fg[4].pixel);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->fg4_red, (jshort)lpGtkStyle->fg[4].red);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->fg4_green, (jshort)lpGtkStyle->fg[4].green);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->fg4_blue, (jshort)lpGtkStyle->fg[4].blue);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->bg0_pixel, (jint)lpGtkStyle->bg[0].pixel);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->bg0_red, (jshort)lpGtkStyle->bg[0].red);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->bg0_green, (jshort)lpGtkStyle->bg[0].green);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->bg0_blue, (jshort)lpGtkStyle->bg[0].blue);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->bg1_pixel, (jint)lpGtkStyle->bg[1].pixel);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->bg1_red, (jshort)lpGtkStyle->bg[1].red);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->bg1_green, (jshort)lpGtkStyle->bg[1].green);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->bg1_blue, (jshort)lpGtkStyle->bg[1].blue);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->bg2_pixel, (jint)lpGtkStyle->bg[2].pixel);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->bg2_red, (jshort)lpGtkStyle->bg[2].red);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->bg2_green, (jshort)lpGtkStyle->bg[2].green);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->bg2_blue, (jshort)lpGtkStyle->bg[2].blue);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->bg3_pixel, (jint)lpGtkStyle->bg[3].pixel);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->bg3_red, (jshort)lpGtkStyle->bg[3].red);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->bg3_green, (jshort)lpGtkStyle->bg[3].green);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->bg3_blue, (jshort)lpGtkStyle->bg[3].blue);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->bg4_pixel, (jint)lpGtkStyle->bg[4].pixel);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->bg4_red, (jshort)lpGtkStyle->bg[4].red);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->bg4_green, (jshort)lpGtkStyle->bg[4].green);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->bg4_blue, (jshort)lpGtkStyle->bg[4].blue);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->light0_pixel, (jint)lpGtkStyle->light[0].pixel);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->light0_red, (jshort)lpGtkStyle->light[0].red);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->light0_green, (jshort)lpGtkStyle->light[0].green);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->light0_blue, (jshort)lpGtkStyle->light[0].blue);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->light1_pixel, (jint)lpGtkStyle->light[1].pixel);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->light1_red, (jshort)lpGtkStyle->light[1].red);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->light1_green, (jshort)lpGtkStyle->light[1].green);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->light1_blue, (jshort)lpGtkStyle->light[1].blue);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->light2_pixel, (jint)lpGtkStyle->light[2].pixel);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->light2_red, (jshort)lpGtkStyle->light[2].red);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->light2_green, (jshort)lpGtkStyle->light[2].green);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->light2_blue, (jshort)lpGtkStyle->light[2].blue);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->light3_pixel, (jint)lpGtkStyle->light[3].pixel);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->light3_red, (jshort)lpGtkStyle->light[3].red);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->light3_green, (jshort)lpGtkStyle->light[3].green);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->light3_blue, (jshort)lpGtkStyle->light[3].blue);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->light4_pixel, (jint)lpGtkStyle->light[4].pixel);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->light4_red, (jshort)lpGtkStyle->light[4].red);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->light4_green, (jshort)lpGtkStyle->light[4].green);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->light4_blue, (jshort)lpGtkStyle->light[4].blue);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->dark0_pixel, (jint)lpGtkStyle->dark[0].pixel);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->dark0_red, (jshort)lpGtkStyle->dark[0].red);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->dark0_green, (jshort)lpGtkStyle->dark[0].green);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->dark0_blue, (jshort)lpGtkStyle->dark[0].blue);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->dark1_pixel, (jint)lpGtkStyle->dark[1].pixel);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->dark1_red, (jshort)lpGtkStyle->dark[1].red);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->dark1_green, (jshort)lpGtkStyle->dark[1].green);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->dark1_blue, (jshort)lpGtkStyle->dark[1].blue);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->dark2_pixel, (jint)lpGtkStyle->dark[2].pixel);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->dark2_red, (jshort)lpGtkStyle->dark[2].red);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->dark2_green, (jshort)lpGtkStyle->dark[2].green);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->dark2_blue, (jshort)lpGtkStyle->dark[2].blue);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->dark3_pixel, (jint)lpGtkStyle->dark[3].pixel);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->dark3_red, (jshort)lpGtkStyle->dark[3].red);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->dark3_green, (jshort)lpGtkStyle->dark[3].green);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->dark3_blue, (jshort)lpGtkStyle->dark[3].blue);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->dark4_pixel, (jint)lpGtkStyle->dark[4].pixel);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->dark4_red, (jshort)lpGtkStyle->dark[4].red);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->dark4_green, (jshort)lpGtkStyle->dark[4].green);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->dark4_blue, (jshort)lpGtkStyle->dark[4].blue);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->mid0_pixel, (jint)lpGtkStyle->mid[0].pixel);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->mid0_red, (jshort)lpGtkStyle->mid[0].red);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->mid0_green, (jshort)lpGtkStyle->mid[0].green);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->mid0_blue, (jshort)lpGtkStyle->mid[0].blue);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->mid1_pixel, (jint)lpGtkStyle->mid[1].pixel);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->mid1_red, (jshort)lpGtkStyle->mid[1].red);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->mid1_green, (jshort)lpGtkStyle->mid[1].green);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->mid1_blue, (jshort)lpGtkStyle->mid[1].blue);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->mid2_pixel, (jint)lpGtkStyle->mid[2].pixel);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->mid2_red, (jshort)lpGtkStyle->mid[2].red);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->mid2_green, (jshort)lpGtkStyle->mid[2].green);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->mid2_blue, (jshort)lpGtkStyle->mid[2].blue);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->mid3_pixel, (jint)lpGtkStyle->mid[3].pixel);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->mid3_red, (jshort)lpGtkStyle->mid[3].red);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->mid3_green, (jshort)lpGtkStyle->mid[3].green);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->mid3_blue, (jshort)lpGtkStyle->mid[3].blue);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->mid4_pixel, (jint)lpGtkStyle->mid[4].pixel);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->mid4_red, (jshort)lpGtkStyle->mid[4].red);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->mid4_green, (jshort)lpGtkStyle->mid[4].green);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->mid4_blue, (jshort)lpGtkStyle->mid[4].blue);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->text0_pixel, (jint)lpGtkStyle->text[0].pixel);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->text0_red, (jshort)lpGtkStyle->text[0].red);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->text0_green, (jshort)lpGtkStyle->text[0].green);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->text0_blue, (jshort)lpGtkStyle->text[0].blue);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->text1_pixel, (jint)lpGtkStyle->text[1].pixel);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->text1_red, (jshort)lpGtkStyle->text[1].red);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->text1_green, (jshort)lpGtkStyle->text[1].green);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->text1_blue, (jshort)lpGtkStyle->text[1].blue);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->text2_pixel, (jint)lpGtkStyle->text[2].pixel);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->text2_red, (jshort)lpGtkStyle->text[2].red);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->text2_green, (jshort)lpGtkStyle->text[2].green);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->text2_blue, (jshort)lpGtkStyle->text[2].blue);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->text3_pixel, (jint)lpGtkStyle->text[3].pixel);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->text3_red, (jshort)lpGtkStyle->text[3].red);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->text3_green, (jshort)lpGtkStyle->text[3].green);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->text3_blue, (jshort)lpGtkStyle->text[3].blue);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->text4_pixel, (jint)lpGtkStyle->text[4].pixel);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->text4_red, (jshort)lpGtkStyle->text[4].red);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->text4_green, (jshort)lpGtkStyle->text[4].green);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->text4_blue, (jshort)lpGtkStyle->text[4].blue);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->base0_pixel, (jint)lpGtkStyle->base[0].pixel);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->base0_red, (jshort)lpGtkStyle->base[0].red);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->base0_green, (jshort)lpGtkStyle->base[0].green);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->base0_blue, (jshort)lpGtkStyle->base[0].blue);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->base1_pixel, (jint)lpGtkStyle->base[1].pixel);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->base1_red, (jshort)lpGtkStyle->base[1].red);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->base1_green, (jshort)lpGtkStyle->base[1].green);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->base1_blue, (jshort)lpGtkStyle->base[1].blue);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->base2_pixel, (jint)lpGtkStyle->base[2].pixel);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->base2_red, (jshort)lpGtkStyle->base[2].red);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->base2_green, (jshort)lpGtkStyle->base[2].green);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->base2_blue, (jshort)lpGtkStyle->base[2].blue);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->base3_pixel, (jint)lpGtkStyle->base[3].pixel);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->base3_red, (jshort)lpGtkStyle->base[3].red);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->base3_green, (jshort)lpGtkStyle->base[3].green);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->base3_blue, (jshort)lpGtkStyle->base[3].blue);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->base4_pixel, (jint)lpGtkStyle->base[4].pixel);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->base4_red, (jshort)lpGtkStyle->base[4].red);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->base4_green, (jshort)lpGtkStyle->base[4].green);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->base4_blue, (jshort)lpGtkStyle->base[4].blue);

	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->black_pixel, (jint)lpGtkStyle->black.pixel);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->black_red, (jshort)lpGtkStyle->black.red);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->black_green, (jshort)lpGtkStyle->black.green);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->black_blue, (jshort)lpGtkStyle->black.blue);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->white_pixel, (jint)lpGtkStyle->white.pixel);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->white_red, (jshort)lpGtkStyle->white.red);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->white_green, (jshort)lpGtkStyle->white.green);
	(*env)->SetShortField(env, lpObject, lpGtkStyleFc->white_blue, (jshort)lpGtkStyle->white.blue);

	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->fg_gc0, (jint)lpGtkStyle->fg_gc[0]);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->fg_gc1, (jint)lpGtkStyle->fg_gc[1]);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->fg_gc2, (jint)lpGtkStyle->fg_gc[2]);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->fg_gc3, (jint)lpGtkStyle->fg_gc[3]);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->fg_gc4, (jint)lpGtkStyle->fg_gc[4]);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->bg_gc0, (jint)lpGtkStyle->bg_gc[0]);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->bg_gc1, (jint)lpGtkStyle->bg_gc[1]);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->bg_gc2, (jint)lpGtkStyle->bg_gc[2]);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->bg_gc3, (jint)lpGtkStyle->bg_gc[3]);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->bg_gc4, (jint)lpGtkStyle->bg_gc[4]);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->light_gc0, (jint)lpGtkStyle->light_gc[0]);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->light_gc1, (jint)lpGtkStyle->light_gc[1]);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->light_gc2, (jint)lpGtkStyle->light_gc[2]);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->light_gc3, (jint)lpGtkStyle->light_gc[3]);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->light_gc4, (jint)lpGtkStyle->light_gc[4]);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->dark_gc0, (jint)lpGtkStyle->dark_gc[0]);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->dark_gc1, (jint)lpGtkStyle->dark_gc[1]);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->dark_gc2, (jint)lpGtkStyle->dark_gc[2]);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->dark_gc3, (jint)lpGtkStyle->dark_gc[3]);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->dark_gc4, (jint)lpGtkStyle->dark_gc[4]);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->mid_gc0, (jint)lpGtkStyle->mid_gc[0]);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->mid_gc1, (jint)lpGtkStyle->mid_gc[1]);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->mid_gc2, (jint)lpGtkStyle->mid_gc[2]);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->mid_gc3, (jint)lpGtkStyle->mid_gc[3]);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->mid_gc4, (jint)lpGtkStyle->mid_gc[4]);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->text_gc0, (jint)lpGtkStyle->text_gc[0]);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->text_gc1, (jint)lpGtkStyle->text_gc[1]);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->text_gc2, (jint)lpGtkStyle->text_gc[2]);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->text_gc3, (jint)lpGtkStyle->text_gc[3]);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->text_gc4, (jint)lpGtkStyle->text_gc[4]);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->base_gc0, (jint)lpGtkStyle->base_gc[0]);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->base_gc1, (jint)lpGtkStyle->base_gc[1]);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->base_gc2, (jint)lpGtkStyle->base_gc[2]);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->base_gc3, (jint)lpGtkStyle->base_gc[3]);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->base_gc4, (jint)lpGtkStyle->base_gc[4]);
	
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->black_gc, (jint)lpGtkStyle->black_gc);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->white_gc, (jint)lpGtkStyle->white_gc);
	
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->bg_pixmap0, (jint)lpGtkStyle->bg_pixmap[0]);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->bg_pixmap1, (jint)lpGtkStyle->bg_pixmap[1]);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->bg_pixmap2, (jint)lpGtkStyle->bg_pixmap[2]);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->bg_pixmap3, (jint)lpGtkStyle->bg_pixmap[3]);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->bg_pixmap4, (jint)lpGtkStyle->bg_pixmap[4]);
}

void getGtkStyleClassFields(JNIEnv *env, jobject lpObject, GtkStyleClass *lpGtkStyleClass, GtkStyleClass_FID_CACHE *lpGtkStyleClassFc)
{
}

void setGtkStyleClassFields(JNIEnv *env, jobject lpObject, GtkStyleClass *lpGtkStyleClass, GtkStyleClass_FID_CACHE *lpGtkStyleClassFc)
{
}

void getGtkAdjustmentFields(JNIEnv *env, jobject lpObject, GtkAdjustment *lpGtkAdjustment, GtkAdjustment_FID_CACHE *lpGtkAdjustmentFc)
{
	DECL_GLOB(pGlob)
	lpGtkAdjustment->lower = (*env)->GetFloatField(env, lpObject, lpGtkAdjustmentFc->lower);
	lpGtkAdjustment->upper = (*env)->GetFloatField(env, lpObject, lpGtkAdjustmentFc->upper);
	lpGtkAdjustment->value = (*env)->GetFloatField(env, lpObject, lpGtkAdjustmentFc->value);
	lpGtkAdjustment->step_increment = (*env)->GetFloatField(env, lpObject, lpGtkAdjustmentFc->step_increment);
	lpGtkAdjustment->page_increment = (*env)->GetFloatField(env, lpObject, lpGtkAdjustmentFc->page_increment);
	lpGtkAdjustment->page_size = (*env)->GetFloatField(env, lpObject, lpGtkAdjustmentFc->page_size);
}

void setGtkAdjustmentFields(JNIEnv *env, jobject lpObject, GtkAdjustment *lpGtkAdjustment, GtkAdjustment_FID_CACHE *lpGtkAdjustmentFc)
{
	DECL_GLOB(pGlob)
	(*env)->SetFloatField(env, lpObject, lpGtkAdjustmentFc->lower, (jfloat)lpGtkAdjustment->lower);
	(*env)->SetFloatField(env, lpObject, lpGtkAdjustmentFc->upper, (jfloat)lpGtkAdjustment->upper);
	(*env)->SetFloatField(env, lpObject, lpGtkAdjustmentFc->value, (jfloat)lpGtkAdjustment->value);
	(*env)->SetFloatField(env, lpObject, lpGtkAdjustmentFc->step_increment, (jfloat)lpGtkAdjustment->step_increment);
	(*env)->SetFloatField(env, lpObject, lpGtkAdjustmentFc->page_increment, (jfloat)lpGtkAdjustment->page_increment);
	(*env)->SetFloatField(env, lpObject, lpGtkAdjustmentFc->page_size, (jfloat)lpGtkAdjustment->page_size);
}

void getGtkCListRowFields(JNIEnv *env, jobject lpObject, GtkCListRow *lpGtkCListRow, GtkCListRow_FID_CACHE *lpGtkCListRowFc)
{
	lpGtkCListRow->cell = (GtkCell*)(*env)->GetIntField(env, lpObject, lpGtkCListRowFc->cell);
	lpGtkCListRow->state = (*env)->GetIntField(env, lpObject, lpGtkCListRowFc->state);
	lpGtkCListRow->foreground.red = (*env)->GetShortField(env, lpObject, lpGtkCListRowFc->foreground_red);
	lpGtkCListRow->foreground.green = (*env)->GetShortField(env, lpObject, lpGtkCListRowFc->foreground_green);
	lpGtkCListRow->foreground.blue = (*env)->GetShortField(env, lpObject, lpGtkCListRowFc->foreground_blue);
	lpGtkCListRow->foreground.pixel = (*env)->GetIntField(env, lpObject, lpGtkCListRowFc->foreground_pixel);
	lpGtkCListRow->background.red = (*env)->GetShortField(env, lpObject, lpGtkCListRowFc->background_red);
	lpGtkCListRow->background.green = (*env)->GetShortField(env, lpObject, lpGtkCListRowFc->background_green);
	lpGtkCListRow->background.blue = (*env)->GetShortField(env, lpObject, lpGtkCListRowFc->background_blue);
	lpGtkCListRow->background.pixel = (*env)->GetIntField(env, lpObject, lpGtkCListRowFc->background_pixel);
	lpGtkCListRow->style = (GtkStyle*)(*env)->GetIntField(env, lpObject, lpGtkCListRowFc->style);
	lpGtkCListRow->data = (gpointer)(*env)->GetIntField(env, lpObject, lpGtkCListRowFc->data);
	lpGtkCListRow->destroy = (GtkDestroyNotify)(*env)->GetIntField(env, lpObject, lpGtkCListRowFc->destroy);
	lpGtkCListRow->fg_set = (*env)->GetIntField(env, lpObject, lpGtkCListRowFc->fg_set);
	lpGtkCListRow->bg_set = (*env)->GetIntField(env, lpObject, lpGtkCListRowFc->bg_set);
	lpGtkCListRow->selectable = (*env)->GetIntField(env, lpObject, lpGtkCListRowFc->selectable);
}

void getGtkCListColumnFields(JNIEnv *env, jobject lpObject, GtkCListColumn *lpGtkCListColumn, GtkCListColumn_FID_CACHE *lpGtkCListColumnFc)
{
	lpGtkCListColumn->title = (gchar*)(*env)->GetIntField(env, lpObject, lpGtkCListColumnFc->title);
	lpGtkCListColumn->area.x = (*env)->GetShortField(env, lpObject, lpGtkCListColumnFc->area_x);
	lpGtkCListColumn->area.y = (*env)->GetShortField(env, lpObject, lpGtkCListColumnFc->area_y);
	lpGtkCListColumn->area.width = (*env)->GetShortField(env, lpObject, lpGtkCListColumnFc->area_width);
	lpGtkCListColumn->area.height = (*env)->GetShortField(env, lpObject, lpGtkCListColumnFc->area_height);
	lpGtkCListColumn->button = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkCListColumnFc->button);
	lpGtkCListColumn->window = (GdkWindow*)(*env)->GetIntField(env, lpObject, lpGtkCListColumnFc->window);
	lpGtkCListColumn->width = (*env)->GetIntField(env, lpObject, lpGtkCListColumnFc->width);
	lpGtkCListColumn->min_width = (*env)->GetIntField(env, lpObject, lpGtkCListColumnFc->min_width);
	lpGtkCListColumn->max_width = (*env)->GetIntField(env, lpObject, lpGtkCListColumnFc->max_width);
	lpGtkCListColumn->justification = (GtkJustification)(*env)->GetIntField(env, lpObject, lpGtkCListColumnFc->justification);
	lpGtkCListColumn->visible = (*env)->GetIntField(env, lpObject, lpGtkCListColumnFc->visible);
	lpGtkCListColumn->width_set = (*env)->GetIntField(env, lpObject, lpGtkCListColumnFc->width_set);
	lpGtkCListColumn->resizeable = (*env)->GetIntField(env, lpObject, lpGtkCListColumnFc->resizeable);
	lpGtkCListColumn->auto_resize = (*env)->GetIntField(env, lpObject, lpGtkCListColumnFc->auto_resize);
	lpGtkCListColumn->button_passive = (*env)->GetIntField(env, lpObject, lpGtkCListColumnFc->button_passive);
}

void setGtkCListRowFields(JNIEnv *env, jobject lpObject, GtkCListRow *lpGtkCListRow, GtkCListRow_FID_CACHE *lpGtkCListRowFc)
{
	(*env)->SetIntField(env, lpObject, lpGtkCListRowFc->cell, (jint)lpGtkCListRow->cell);
	(*env)->SetIntField(env, lpObject, lpGtkCListRowFc->state, (jint)lpGtkCListRow->state);
	(*env)->SetShortField(env, lpObject, lpGtkCListRowFc->foreground_red, (jshort)lpGtkCListRow->foreground.red);
	(*env)->SetShortField(env, lpObject, lpGtkCListRowFc->foreground_green, (jshort)lpGtkCListRow->foreground.green);
	(*env)->SetShortField(env, lpObject, lpGtkCListRowFc->foreground_blue, (jshort)lpGtkCListRow->foreground.blue);
	(*env)->SetIntField(env, lpObject, lpGtkCListRowFc->foreground_pixel, (jint)lpGtkCListRow->foreground.pixel);
	(*env)->SetShortField(env, lpObject, lpGtkCListRowFc->background_red, (jshort)lpGtkCListRow->background.red);
	(*env)->SetShortField(env, lpObject, lpGtkCListRowFc->background_green, (jshort)lpGtkCListRow->background.green);
	(*env)->SetShortField(env, lpObject, lpGtkCListRowFc->background_blue, (jshort)lpGtkCListRow->background.blue);
	(*env)->SetIntField(env, lpObject, lpGtkCListRowFc->background_pixel, (jint)lpGtkCListRow->background.pixel);
	(*env)->SetIntField(env, lpObject, lpGtkCListRowFc->style, (jint)lpGtkCListRow->style);
	(*env)->SetIntField(env, lpObject, lpGtkCListRowFc->data, (jint)lpGtkCListRow->data);
	(*env)->SetIntField(env, lpObject, lpGtkCListRowFc->destroy, (jint)lpGtkCListRow->destroy);
	(*env)->SetIntField(env, lpObject, lpGtkCListRowFc->fg_set, (jint)lpGtkCListRow->fg_set);
	(*env)->SetIntField(env, lpObject, lpGtkCListRowFc->bg_set, (jint)lpGtkCListRow->bg_set);
	(*env)->SetIntField(env, lpObject, lpGtkCListRowFc->selectable, (jint)lpGtkCListRow->selectable);
}

void setGtkCListColumnFields(JNIEnv *env, jobject lpObject, GtkCListColumn *lpGtkCListColumn, GtkCListColumn_FID_CACHE *lpGtkCListColumnFc)
{
	(*env)->SetIntField(env, lpObject, lpGtkCListColumnFc->title, (jint)lpGtkCListColumn->title);
	(*env)->SetShortField(env, lpObject, lpGtkCListColumnFc->area_x, (jshort)lpGtkCListColumn->area.x);
	(*env)->SetShortField(env, lpObject, lpGtkCListColumnFc->area_y, (jshort)lpGtkCListColumn->area.y);
	(*env)->SetShortField(env, lpObject, lpGtkCListColumnFc->area_width, (jshort)lpGtkCListColumn->area.width);
	(*env)->SetShortField(env, lpObject, lpGtkCListColumnFc->area_height, (jshort)lpGtkCListColumn->area.height);
	(*env)->SetIntField(env, lpObject, lpGtkCListColumnFc->button, (jint)lpGtkCListColumn->button);
	(*env)->SetIntField(env, lpObject, lpGtkCListColumnFc->window, (jint)lpGtkCListColumn->window);
	(*env)->SetIntField(env, lpObject, lpGtkCListColumnFc->width, (jshort)lpGtkCListColumn->width);
	(*env)->SetIntField(env, lpObject, lpGtkCListColumnFc->min_width, (jint)lpGtkCListColumn->min_width);
	(*env)->SetIntField(env, lpObject, lpGtkCListColumnFc->max_width, (jint)lpGtkCListColumn->max_width);
	(*env)->SetIntField(env, lpObject, lpGtkCListColumnFc->justification, (jint)lpGtkCListColumn->justification);
	(*env)->SetIntField(env, lpObject, lpGtkCListColumnFc->visible, (jint)lpGtkCListColumn->visible);
	(*env)->SetIntField(env, lpObject, lpGtkCListColumnFc->width_set, (jint)lpGtkCListColumn->width_set);
	(*env)->SetIntField(env, lpObject, lpGtkCListColumnFc->resizeable, (jint)lpGtkCListColumn->resizeable);
	(*env)->SetIntField(env, lpObject, lpGtkCListColumnFc->auto_resize, (jint)lpGtkCListColumn->auto_resize);
	(*env)->SetIntField(env, lpObject, lpGtkCListColumnFc->button_passive, (jint)lpGtkCListColumn->button_passive);
}

void getGtkCTreeFields(JNIEnv *env, jobject lpObject, GtkCTree *lpGtkCTree, GtkCTree_FID_CACHE *lpGtkCTreeFc)
{
	DECL_GLOB(pGlob)
	getGtkCListFields(env, lpObject, &lpGtkCTree->clist, &PGLOB(GtkCListFc));
	lpGtkCTree->tree_indent = (*env)->GetIntField(env, lpObject, lpGtkCTreeFc->tree_indent);
	lpGtkCTree->tree_column = (*env)->GetIntField(env, lpObject, lpGtkCTreeFc->tree_column);	
}

void setGtkCTreeFields(JNIEnv *env, jobject lpObject, GtkCTree *lpGtkCTree, GtkCTree_FID_CACHE *lpGtkCTreeFc)
{
	DECL_GLOB(pGlob)
	setGtkCListFields(env, lpObject, &lpGtkCTree->clist, &PGLOB(GtkCListFc));
	(*env)->SetIntField(env, lpObject, lpGtkCTreeFc->tree_indent, (jint)lpGtkCTree->tree_indent);
	(*env)->SetIntField(env, lpObject, lpGtkCTreeFc->tree_column, (jint)lpGtkCTree->tree_column);	
}

void getGtkCTreeRowFields(JNIEnv *env, jobject lpObject, GtkCTreeRow *lpGtkCTreeRow, GtkCTreeRow_FID_CACHE *lpGtkCTreeRowFc)
{
	DECL_GLOB(pGlob)
	getGtkCListRowFields(env, lpObject, &lpGtkCTreeRow->row, &PGLOB(GtkCListRowFc));
	lpGtkCTreeRow->parent = (GtkCTreeNode*)(*env)->GetIntField(env, lpObject, lpGtkCTreeRowFc->parent);
	lpGtkCTreeRow->sibling = (GtkCTreeNode*)(*env)->GetIntField(env, lpObject, lpGtkCTreeRowFc->sibling);
	lpGtkCTreeRow->children = (GtkCTreeNode*)(*env)->GetIntField(env, lpObject, lpGtkCTreeRowFc->children);
	lpGtkCTreeRow->pixmap_closed = (GdkPixmap*)(*env)->GetIntField(env, lpObject, lpGtkCTreeRowFc->pixmap_closed);
	lpGtkCTreeRow->mask_closed = (GdkBitmap*)(*env)->GetIntField(env, lpObject, lpGtkCTreeRowFc->mask_closed);
	lpGtkCTreeRow->pixmap_opened = (GdkPixmap*)(*env)->GetIntField(env, lpObject, lpGtkCTreeRowFc->pixmap_opened);
	lpGtkCTreeRow->mask_opened = (GdkBitmap*)(*env)->GetIntField(env, lpObject, lpGtkCTreeRowFc->mask_opened);
	lpGtkCTreeRow->level = (*env)->GetShortField(env, lpObject, lpGtkCTreeRowFc->level);
	lpGtkCTreeRow->is_leaf = (*env)->GetIntField(env, lpObject, lpGtkCTreeRowFc->is_leaf);
	lpGtkCTreeRow->expanded = (*env)->GetIntField(env, lpObject, lpGtkCTreeRowFc->expanded);
}

void setGtkCTreeRowFields(JNIEnv *env, jobject lpObject, GtkCTreeRow *lpGtkCTreeRow, GtkCTreeRow_FID_CACHE *lpGtkCTreeRowFc)
{
	DECL_GLOB(pGlob)
	setGtkCListRowFields(env, lpObject, &lpGtkCTreeRow->row, &PGLOB(GtkCListRowFc));
	(*env)->SetIntField(env, lpObject, lpGtkCTreeRowFc->parent, (jint)lpGtkCTreeRow->parent);
	(*env)->SetIntField(env, lpObject, lpGtkCTreeRowFc->sibling, (jint)lpGtkCTreeRow->sibling);
	(*env)->SetIntField(env, lpObject, lpGtkCTreeRowFc->children, (jint)lpGtkCTreeRow->children);
	(*env)->SetIntField(env, lpObject, lpGtkCTreeRowFc->pixmap_closed, (jint)lpGtkCTreeRow->pixmap_closed);
	(*env)->SetIntField(env, lpObject, lpGtkCTreeRowFc->mask_closed, (jint)lpGtkCTreeRow->mask_closed);
	(*env)->SetIntField(env, lpObject, lpGtkCTreeRowFc->pixmap_opened, (jint)lpGtkCTreeRow->pixmap_opened);
	(*env)->SetIntField(env, lpObject, lpGtkCTreeRowFc->mask_opened, (jint)lpGtkCTreeRow->mask_opened);
	(*env)->SetShortField(env, lpObject, lpGtkCTreeRowFc->level, (jshort)lpGtkCTreeRow->level);
	(*env)->SetIntField(env, lpObject, lpGtkCTreeRowFc->is_leaf, (jint)lpGtkCTreeRow->is_leaf);
	(*env)->SetIntField(env, lpObject, lpGtkCTreeRowFc->expanded, (jint)lpGtkCTreeRow->expanded);
}
