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
GdkEventKey_FID_CACHE GdkEventKeyFc;
GdkEventButton_FID_CACHE GdkEventButtonFc;
GdkEventMotion_FID_CACHE GdkEventMotionFc;
GdkEventExpose_FID_CACHE GdkEventExposeFc;
GdkFont_FID_CACHE GdkFontFc;
GdkGCValues_FID_CACHE GdkGCValuesFc;
GdkImage_FID_CACHE GdkImageFc;
GdkPoint_FID_CACHE GdkPointFc;
GdkRectangle_FID_CACHE GdkRectangleFc;
GdkVisual_FID_CACHE GdkVisualFc;
GtkObject_FID_CACHE GtkObjectFc;
GtkData_FID_CACHE GtkDataFc;
GtkAdjustment_FID_CACHE GtkAdjustmentFc;
GtkAllocation_FID_CACHE GtkAllocationFc;
GtkWidget_FID_CACHE GtkWidgetFc;
GtkContainer_FID_CACHE GtkContainerFc;
GtkBin_FID_CACHE GtkBinFc;
GtkMenu_FID_CACHE GtkMenuFc;
GtkItem_FID_CACHE GtkItemFc;
GtkMenuShell_FID_CACHE GtkMenuShellFc;
GtkMenuItem_FID_CACHE GtkMenuItemFc;
GtkCheckMenuItem_FID_CACHE GtkCheckMenuItemFc;
GtkWindow_FID_CACHE GtkWindowFc;
GtkDialog_FID_CACHE GtkDialogFc;
GtkColorSelectionDialog_FID_CACHE GtkColorSelectionDialogFc;\
GtkBox_FID_CACHE GtkBoxFc;
GtkHBox_FID_CACHE GtkHBoxFc;
GtkCombo_FID_CACHE GtkComboFc;
GtkFileSelection_FID_CACHE GtkFileSelectionFc;
GtkFrame_FID_CACHE GtkFrameFc;
GtkFontSelectionDialog_FID_CACHE GtkFontSelectionDialogFc;
GtkCList_FID_CACHE GtkCListFc;
GtkEditable_FID_CACHE GtkEditableFc;
GtkText_FID_CACHE GtkTextFc;
GtkProgress_FID_CACHE GtkProgressFc;
GtkProgressBar_FID_CACHE GtkProgressBarFc;
GtkArg_FID_CACHE GtkArgFc;
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

void cacheGdkEventKeyFids(JNIEnv *env, jobject lpGdkEventKey, PGdkEventKey_FID_CACHE lpCache)
{
	if (lpCache->cached) return;

	lpCache->GdkEventKeyClass = (*env)->GetObjectClass(env, lpGdkEventKey);

	lpCache->type = (*env)->GetFieldID(env, lpCache->GdkEventKeyClass, "type", "I");
	lpCache->window = (*env)->GetFieldID(env, lpCache->GdkEventKeyClass, "window", "I");
	lpCache->send_event = (*env)->GetFieldID(env, lpCache->GdkEventKeyClass, "send_event", "B");
	lpCache->time = (*env)->GetFieldID(env, lpCache->GdkEventKeyClass, "time", "I");
	lpCache->state = (*env)->GetFieldID(env, lpCache->GdkEventKeyClass, "state", "I");
	lpCache->keyval = (*env)->GetFieldID(env, lpCache->GdkEventKeyClass, "keyval", "I");
	lpCache->length = (*env)->GetFieldID(env, lpCache->GdkEventKeyClass, "length", "I");
	lpCache->string = (*env)->GetFieldID(env, lpCache->GdkEventKeyClass, "string", "I");
	lpCache->cached = 1;
};

void cacheGdkEventButtonFids(JNIEnv *env, jobject lpGdkEventButton, PGdkEventButton_FID_CACHE lpCache)
{
	if (lpCache->cached) return;

	lpCache->GdkEventButtonClass = (*env)->GetObjectClass(env, lpGdkEventButton);
	lpCache->type = (*env)->GetFieldID(env, lpCache->GdkEventButtonClass, "type", "I");
	lpCache->window = (*env)->GetFieldID(env, lpCache->GdkEventButtonClass, "window", "I");
	lpCache->send_event = (*env)->GetFieldID(env, lpCache->GdkEventButtonClass, "send_event", "B");
	lpCache->time = (*env)->GetFieldID(env, lpCache->GdkEventButtonClass, "time", "I");
	lpCache->x = (*env)->GetFieldID(env, lpCache->GdkEventButtonClass, "x", "J");
	lpCache->y = (*env)->GetFieldID(env, lpCache->GdkEventButtonClass, "y", "J");
	lpCache->pressure = (*env)->GetFieldID(env, lpCache->GdkEventButtonClass, "pressure", "J");
	lpCache->xtilt = (*env)->GetFieldID(env, lpCache->GdkEventButtonClass, "xtilt", "J");
	lpCache->ytilt = (*env)->GetFieldID(env, lpCache->GdkEventButtonClass, "ytilt", "J");
	lpCache->state = (*env)->GetFieldID(env, lpCache->GdkEventButtonClass, "state", "I");
	lpCache->button = (*env)->GetFieldID(env, lpCache->GdkEventButtonClass, "button", "I");
	lpCache->source = (*env)->GetFieldID(env, lpCache->GdkEventButtonClass, "source", "I");
	lpCache->deviceid = (*env)->GetFieldID(env, lpCache->GdkEventButtonClass, "deviceid", "I");
	lpCache->x_root = (*env)->GetFieldID(env, lpCache->GdkEventButtonClass, "x_root", "J");
	lpCache->y_root = (*env)->GetFieldID(env, lpCache->GdkEventButtonClass, "y_root", "J");

	lpCache->cached = 1;
};

void cacheGdkEventMotionFids(JNIEnv *env, jobject lpGdkEventMotion, PGdkEventMotion_FID_CACHE lpCache)
{
	if (lpCache->cached) return;

	lpCache->GdkEventMotionClass = (*env)->GetObjectClass(env, lpGdkEventMotion);
	lpCache->type = (*env)->GetFieldID(env, lpCache->GdkEventMotionClass, "type", "I");
	lpCache->window = (*env)->GetFieldID(env, lpCache->GdkEventMotionClass, "window", "I");
	lpCache->send_event = (*env)->GetFieldID(env, lpCache->GdkEventMotionClass, "send_event", "B");
	lpCache->time = (*env)->GetFieldID(env, lpCache->GdkEventMotionClass, "time", "I");
	lpCache->x = (*env)->GetFieldID(env, lpCache->GdkEventMotionClass, "x", "J");
	lpCache->y = (*env)->GetFieldID(env, lpCache->GdkEventMotionClass, "y", "J");
	lpCache->pressure = (*env)->GetFieldID(env, lpCache->GdkEventMotionClass, "pressure", "J");
	lpCache->xtilt = (*env)->GetFieldID(env, lpCache->GdkEventMotionClass, "xtilt", "J");
	lpCache->ytilt = (*env)->GetFieldID(env, lpCache->GdkEventMotionClass, "ytilt", "J");
	lpCache->state = (*env)->GetFieldID(env, lpCache->GdkEventMotionClass, "state", "I");
	lpCache->is_hint = (*env)->GetFieldID(env, lpCache->GdkEventMotionClass, "is_hint", "I");
	lpCache->source = (*env)->GetFieldID(env, lpCache->GdkEventMotionClass, "source", "I");
	lpCache->deviceid = (*env)->GetFieldID(env, lpCache->GdkEventMotionClass, "deviceid", "I");
	lpCache->x_root = (*env)->GetFieldID(env, lpCache->GdkEventMotionClass, "x_root", "J");
	lpCache->y_root = (*env)->GetFieldID(env, lpCache->GdkEventMotionClass, "y_root", "J");

	lpCache->cached = 1;
};

void cacheGdkEventExposeFids(JNIEnv *env, jobject lpGdkEventExpose, PGdkEventExpose_FID_CACHE lpCache)
{
	if (lpCache->cached) return;

	lpCache->GdkEventExposeClass = (*env)->GetObjectClass(env, lpGdkEventExpose);
	lpCache->type = (*env)->GetFieldID(env, lpCache->GdkEventExposeClass, "type", "I");
	lpCache->window = (*env)->GetFieldID(env, lpCache->GdkEventExposeClass, "window", "I");
	lpCache->send_event = (*env)->GetFieldID(env, lpCache->GdkEventExposeClass, "send_event", "B");
	lpCache->x = (*env)->GetFieldID(env, lpCache->GdkEventExposeClass, "x", "S");
	lpCache->y = (*env)->GetFieldID(env, lpCache->GdkEventExposeClass, "y", "S");
	lpCache->width = (*env)->GetFieldID(env, lpCache->GdkEventExposeClass, "width", "S");
	lpCache->height = (*env)->GetFieldID(env, lpCache->GdkEventExposeClass, "height", "S");
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

void cacheGdkImageFids(JNIEnv *env, jobject lpGdkImage, PGdkImage_FID_CACHE lpCache)
{
	if (lpCache->cached) return;

	lpCache->GdkImageClass = (*env)->GetObjectClass(env, lpGdkImage);
	lpCache->type = (*env)->GetFieldID(env, lpCache->GdkImageClass, "type", "I");
	lpCache->visual = (*env)->GetFieldID(env, lpCache->GdkImageClass, "visual", "I");
	lpCache->byte_order = (*env)->GetFieldID(env, lpCache->GdkImageClass, "byte_order", "I");
	lpCache->width = (*env)->GetFieldID(env, lpCache->GdkImageClass, "width", "S");
	lpCache->height = (*env)->GetFieldID(env, lpCache->GdkImageClass, "height", "S");
	lpCache->depth = (*env)->GetFieldID(env, lpCache->GdkImageClass, "depth", "S");
	lpCache->bpp = (*env)->GetFieldID(env, lpCache->GdkImageClass, "bpp", "S");
	lpCache->bpl = (*env)->GetFieldID(env, lpCache->GdkImageClass, "bpl", "S");
	lpCache->mem = (*env)->GetFieldID(env, lpCache->GdkImageClass, "mem", "I");

	lpCache->cached = 1;
};

void cacheGdkPointFids(JNIEnv *env, jobject lpGdkPoint, PGdkPoint_FID_CACHE lpCache)
{
	if (lpCache->cached) return;

	lpCache->GdkPointClass = (*env)->GetObjectClass(env, lpGdkPoint);
	lpCache->x = (*env)->GetFieldID(env, lpCache->GdkPointClass, "x", "S");
	lpCache->y = (*env)->GetFieldID(env, lpCache->GdkPointClass, "y", "S");

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

void cacheGtkArgFids(JNIEnv *env, jobject lpGtkArg, PGtkArg_FID_CACHE lpCache)
{
	if (lpCache->cached) return;

	lpCache->GtkArgClass = (*env)->GetObjectClass(env, lpGtkArg);

	fprintf(stderr, "WARNING: Unimplemented method cacheGtkArgFids.\n");
	lpCache->cached = 1;
};

void cacheGtkBinFids(JNIEnv *env, jobject lpGtkBin, PGtkBin_FID_CACHE lpCache)
{
	DECL_GLOB(pGlob)
	if (lpCache->cached) return;

	lpCache->GtkBinClass = (*env)->GetObjectClass(env, lpGtkBin);
	cacheGtkContainerFids(env, lpGtkBin, &PGLOB(GtkContainerFc));
	lpCache->child = (*env)->GetFieldID(env, lpCache->GtkBinClass, "child", "I");

	lpCache->cached = 1;
};

void cacheGtkCListFids(JNIEnv *env, jobject lpGtkCList, PGtkCList_FID_CACHE lpCache)
{
	DECL_GLOB(pGlob)
	if (lpCache->cached) return;

	lpCache->GtkCListClass = (*env)->GetObjectClass(env, lpGtkCList);
	cacheGtkContainerFids(env, lpGtkCList, &PGLOB(GtkContainerFc));
	lpCache->clist_flags = (*env)->GetFieldID(env, lpCache->GtkCListClass, "clist_flags", "S");
	lpCache->row_mem_chunk = (*env)->GetFieldID(env, lpCache->GtkCListClass, "row_mem_chunk", "I");
	lpCache->cell_mem_chunk = (*env)->GetFieldID(env, lpCache->GtkCListClass, "cell_mem_chunk", "I");
	lpCache->freeze_count = (*env)->GetFieldID(env, lpCache->GtkCListClass, "freeze_count", "I");
	lpCache->internal_allocation_x = (*env)->GetFieldID(env, lpCache->GtkCListClass, "internal_allocation_x", "S");
	lpCache->internal_allocation_y = (*env)->GetFieldID(env, lpCache->GtkCListClass, "internal_allocation_y", "S");
	lpCache->internal_allocation_width = (*env)->GetFieldID(env, lpCache->GtkCListClass, "internal_allocation_width", "S");
	lpCache->internal_allocation_height = (*env)->GetFieldID(env, lpCache->GtkCListClass, "internal_allocation_height", "S");
	lpCache->rows = (*env)->GetFieldID(env, lpCache->GtkCListClass, "rows", "I");
	lpCache->row_center_offset = (*env)->GetFieldID(env, lpCache->GtkCListClass, "row_center_offset", "I");
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


void cacheGtkColorSelectionDialogFids(JNIEnv *env, jobject lpGtkColorSelectionDialog, PGtkColorSelectionDialog_FID_CACHE lpCache)
{
	DECL_GLOB(pGlob)
	if (lpCache->cached) return;

	lpCache->GtkColorSelectionDialogClass = (*env)->GetObjectClass(env, lpGtkColorSelectionDialog);
	cacheGtkWindowFids(env, lpGtkColorSelectionDialog, &PGLOB(GtkWindowFc));
	lpCache->colorsel = (*env)->GetFieldID(env, lpCache->GtkColorSelectionDialogClass, "colorsel", "I");
	lpCache->main_vbox = (*env)->GetFieldID(env, lpCache->GtkColorSelectionDialogClass, "main_vbox", "I");
	lpCache->ok_button = (*env)->GetFieldID(env, lpCache->GtkColorSelectionDialogClass, "ok_button", "I");
	lpCache->reset_button = (*env)->GetFieldID(env, lpCache->GtkColorSelectionDialogClass, "reset_button", "I");
	lpCache->cancel_button = (*env)->GetFieldID(env, lpCache->GtkColorSelectionDialogClass, "cancel_button", "I");
	lpCache->help_button = (*env)->GetFieldID(env, lpCache->GtkColorSelectionDialogClass, "help_button", "I");

	lpCache->cached = 1;
};

void cacheGtkComboFids(JNIEnv *env, jobject lpGtkCombo, PGtkCombo_FID_CACHE lpCache)
{
	DECL_GLOB(pGlob)
	if (lpCache->cached) return;

	lpCache->GtkComboClass = (*env)->GetObjectClass(env, lpGtkCombo);
	cacheGtkHBoxFids(env, lpGtkCombo, &PGLOB(GtkHBoxFc));
	lpCache->entry = (*env)->GetFieldID(env, lpCache->GtkComboClass, "entry", "I");
	lpCache->button = (*env)->GetFieldID(env, lpCache->GtkComboClass, "button", "I");
	lpCache->popup = (*env)->GetFieldID(env, lpCache->GtkComboClass, "popup", "I");
	lpCache->popwin = (*env)->GetFieldID(env, lpCache->GtkComboClass, "popwin", "I");
	lpCache->list = (*env)->GetFieldID(env, lpCache->GtkComboClass, "list", "I");
	lpCache->entry_change_id = (*env)->GetFieldID(env, lpCache->GtkComboClass, "entry_change_id", "I");
	lpCache->list_change_id = (*env)->GetFieldID(env, lpCache->GtkComboClass, "list_change_id", "I");
	lpCache->value_in_list = (*env)->GetFieldID(env, lpCache->GtkComboClass, "value_in_list", "I");
	lpCache->ok_if_empty = (*env)->GetFieldID(env, lpCache->GtkComboClass, "ok_if_empty", "I");
	lpCache->case_sensitive = (*env)->GetFieldID(env, lpCache->GtkComboClass, "case_sensitive", "I");
	lpCache->use_arrows = (*env)->GetFieldID(env, lpCache->GtkComboClass, "use_arrows", "I");
	lpCache->use_arrows_always = (*env)->GetFieldID(env, lpCache->GtkComboClass, "use_arrows_always", "I");
	lpCache->current_button = (*env)->GetFieldID(env, lpCache->GtkComboClass, "current_button", "S");
	lpCache->activate_id = (*env)->GetFieldID(env, lpCache->GtkComboClass, "activate_id", "I");

	lpCache->cached = 1;
};

void cacheGtkContainerFids(JNIEnv *env, jobject lpGtkContainer, PGtkContainer_FID_CACHE lpCache)
{
	DECL_GLOB(pGlob)
	if (lpCache->cached) return;

	lpCache->GtkContainerClass = (*env)->GetObjectClass(env, lpGtkContainer);
	cacheGtkWidgetFids(env, lpGtkContainer, &PGLOB(GtkWidgetFc));
	lpCache->focus_child = (*env)->GetFieldID(env, lpCache->GtkContainerClass, "focus_child", "I");
	lpCache->border_width = (*env)->GetFieldID(env, lpCache->GtkContainerClass, "border_width", "I");
	lpCache->need_resize = (*env)->GetFieldID(env, lpCache->GtkContainerClass, "need_resize", "I");
	lpCache->resize_mode = (*env)->GetFieldID(env, lpCache->GtkContainerClass, "resize_mode", "I");
	lpCache->resize_widgets = (*env)->GetFieldID(env, lpCache->GtkContainerClass, "resize_widgets", "I");

	lpCache->cached = 1;
};

void cacheGtkDataFids(JNIEnv *env, jobject lpGtkData, PGtkData_FID_CACHE lpCache)
{
	DECL_GLOB(pGlob)
	if (lpCache->cached) return;

	lpCache->GtkDataClass = (*env)->GetObjectClass(env, lpGtkData);
	cacheGtkObjectFids(env, lpGtkData, &PGLOB(GtkObjectFc));

	lpCache->cached = 1;
};

void cacheGtkEditableFids(JNIEnv *env, jobject lpGtkEditable, PGtkEditable_FID_CACHE lpCache)
{
	DECL_GLOB(pGlob)
	if (lpCache->cached) return;

	lpCache->GtkEditableClass = (*env)->GetObjectClass(env, lpGtkEditable);
	cacheGtkWidgetFids(env, lpGtkEditable, &PGLOB(GtkWidgetFc));
	lpCache->current_pos = (*env)->GetFieldID(env, lpCache->GtkEditableClass, "current_pos", "I");
	lpCache->selection_start_pos = (*env)->GetFieldID(env, lpCache->GtkEditableClass, "selection_start_pos", "I");
	lpCache->selection_end_pos = (*env)->GetFieldID(env, lpCache->GtkEditableClass, "selection_end_pos", "I");
	lpCache->has_selection = (*env)->GetFieldID(env, lpCache->GtkEditableClass, "has_selection", "I");
	lpCache->editable = (*env)->GetFieldID(env, lpCache->GtkEditableClass, "editable", "I");
	lpCache->visible = (*env)->GetFieldID(env, lpCache->GtkEditableClass, "visible", "I");
	lpCache->ic = (*env)->GetFieldID(env, lpCache->GtkEditableClass, "ic", "I");
	lpCache->ic_attr = (*env)->GetFieldID(env, lpCache->GtkEditableClass, "ic_attr", "I");
	lpCache->clipboard_text = (*env)->GetFieldID(env, lpCache->GtkEditableClass, "clipboard_text", "I");

	lpCache->cached = 1;
};

void cacheGtkTextFids(JNIEnv *env, jobject lpGtkText, PGtkText_FID_CACHE lpCache)
{
	DECL_GLOB(pGlob)
	if (lpCache->cached) return;

	lpCache->GtkTextClass = (*env)->GetObjectClass(env, lpGtkText);
	cacheGtkEditableFids(env, lpGtkText, &PGLOB(GtkEditableFc));

	lpCache->first_line_start_index = (*env)->GetFieldID(env, lpCache->GtkTextClass, "first_line_start_index", "I");
	lpCache->first_onscreen_hor_pixel = (*env)->GetFieldID(env, lpCache->GtkTextClass, "first_onscreen_hor_pixel", "I");
	lpCache->first_onscreen_ver_pixel = (*env)->GetFieldID(env, lpCache->GtkTextClass, "first_onscreen_ver_pixel", "I");
	lpCache->default_tab_width = (*env)->GetFieldID(env, lpCache->GtkTextClass, "default_tab_width", "I");
	lpCache->cursor_pos_x = (*env)->GetFieldID(env, lpCache->GtkTextClass, "cursor_pos_x", "I");
	lpCache->cursor_pos_y = (*env)->GetFieldID(env, lpCache->GtkTextClass, "cursor_pos_y", "I");
	lpCache->cursor_virtual_x = (*env)->GetFieldID(env, lpCache->GtkTextClass, "cursor_virtual_x", "I");
	
	lpCache->cached = 1;
};

void cacheGtkFileSelectionFids(JNIEnv *env, jobject lpGtkFileSelection, PGtkFileSelection_FID_CACHE lpCache)
{
	DECL_GLOB(pGlob)
	if (lpCache->cached) return;

	lpCache->GtkFileSelectionClass = (*env)->GetObjectClass(env, lpGtkFileSelection);
	cacheGtkWindowFids(env, lpGtkFileSelection, &PGLOB(GtkWindowFc));
	lpCache->dir_list = (*env)->GetFieldID(env, lpCache->GtkFileSelectionClass, "dir_list", "I");
	lpCache->file_list = (*env)->GetFieldID(env, lpCache->GtkFileSelectionClass, "file_list", "I");
	lpCache->selection_entry = (*env)->GetFieldID(env, lpCache->GtkFileSelectionClass, "selection_entry", "I");
	lpCache->selection_text = (*env)->GetFieldID(env, lpCache->GtkFileSelectionClass, "selection_text", "I");
	lpCache->main_vbox = (*env)->GetFieldID(env, lpCache->GtkFileSelectionClass, "main_vbox", "I");
	lpCache->ok_button = (*env)->GetFieldID(env, lpCache->GtkFileSelectionClass, "ok_button", "I");
	lpCache->cancel_button = (*env)->GetFieldID(env, lpCache->GtkFileSelectionClass, "cancel_button", "I");
	lpCache->help_button = (*env)->GetFieldID(env, lpCache->GtkFileSelectionClass, "help_button", "I");
	lpCache->history_pulldown = (*env)->GetFieldID(env, lpCache->GtkFileSelectionClass, "history_pulldown", "I");
	lpCache->history_menu = (*env)->GetFieldID(env, lpCache->GtkFileSelectionClass, "history_menu", "I");
	lpCache->history_list = (*env)->GetFieldID(env, lpCache->GtkFileSelectionClass, "history_list", "I");
	lpCache->fileop_dialog = (*env)->GetFieldID(env, lpCache->GtkFileSelectionClass, "fileop_dialog", "I");
	lpCache->fileop_entry = (*env)->GetFieldID(env, lpCache->GtkFileSelectionClass, "fileop_entry", "I");
	lpCache->fileop_file = (*env)->GetFieldID(env, lpCache->GtkFileSelectionClass, "fileop_file", "I");
	lpCache->cmpl_state = (*env)->GetFieldID(env, lpCache->GtkFileSelectionClass, "cmpl_state", "I");
	lpCache->fileop_c_dir = (*env)->GetFieldID(env, lpCache->GtkFileSelectionClass, "fileop_c_dir", "I");
	lpCache->fileop_del_file = (*env)->GetFieldID(env, lpCache->GtkFileSelectionClass, "fileop_del_file", "I");
	lpCache->fileop_ren_file = (*env)->GetFieldID(env, lpCache->GtkFileSelectionClass, "fileop_ren_file", "I");
	lpCache->button_area = (*env)->GetFieldID(env, lpCache->GtkFileSelectionClass, "button_area", "I");
	lpCache->action_area = (*env)->GetFieldID(env, lpCache->GtkFileSelectionClass, "action_area", "I");

	lpCache->cached = 1;
};

void cacheGtkFontSelectionDialogFids(JNIEnv *env, jobject lpGtkFontSelectionDialog, PGtkFontSelectionDialog_FID_CACHE lpCache)
{
	DECL_GLOB(pGlob)
	if (lpCache->cached) return;

	lpCache->GtkFontSelectionDialogClass = (*env)->GetObjectClass(env, lpGtkFontSelectionDialog);
	cacheGtkWindowFids(env, lpGtkFontSelectionDialog, &PGLOB(GtkWindowFc));
	lpCache->fontsel = (*env)->GetFieldID(env, lpCache->GtkFontSelectionDialogClass, "fontsel", "I");
	lpCache->main_vbox = (*env)->GetFieldID(env, lpCache->GtkFontSelectionDialogClass, "main_vbox", "I");
	lpCache->action_area = (*env)->GetFieldID(env, lpCache->GtkFontSelectionDialogClass, "action_area", "I");
	lpCache->ok_button = (*env)->GetFieldID(env, lpCache->GtkFontSelectionDialogClass, "ok_button", "I");
	lpCache->apply_button = (*env)->GetFieldID(env, lpCache->GtkFontSelectionDialogClass, "apply_button", "I");
	lpCache->cancel_button = (*env)->GetFieldID(env, lpCache->GtkFontSelectionDialogClass, "cancel_button", "I");
	lpCache->dialog_width = (*env)->GetFieldID(env, lpCache->GtkFontSelectionDialogClass, "dialog_width", "I");
	lpCache->auto_resize = (*env)->GetFieldID(env, lpCache->GtkFontSelectionDialogClass, "auto_resize", "I");

	lpCache->cached = 1;
};

void cacheGtkObjectFids(JNIEnv *env, jobject lpGtkObject, PGtkObject_FID_CACHE lpCache)
{
	if (lpCache->cached) return;

	lpCache->GtkObjectClass = (*env)->GetObjectClass(env, lpGtkObject);
	lpCache->klass = (*env)->GetFieldID(env, lpCache->GtkObjectClass, "klass", "I");
	lpCache->flags = (*env)->GetFieldID(env, lpCache->GtkObjectClass, "flags", "I");
	lpCache->ref_count = (*env)->GetFieldID(env, lpCache->GtkObjectClass, "ref_count", "I");
	lpCache->object_data = (*env)->GetFieldID(env, lpCache->GtkObjectClass, "object_data", "I");

	lpCache->cached = 1;
};

void cacheGtkProgressFids(JNIEnv *env, jobject lpGtkProgress, PGtkProgress_FID_CACHE lpCache)
{
	if (lpCache->cached) return;

	lpCache->GtkProgressClass = (*env)->GetObjectClass(env, lpGtkProgress);

	fprintf(stderr, "WARNING: Unimplemented method cacheGtkProgressFids.\n");
	lpCache->cached = 1;
};

void cacheGtkProgressBarFids(JNIEnv *env, jobject lpGtkProgressBar, PGtkProgressBar_FID_CACHE lpCache)
{
	DECL_GLOB(pGlob)
	if (lpCache->cached) return;

	lpCache->GtkProgressBarClass = (*env)->GetObjectClass(env, lpGtkProgressBar);
	cacheGtkProgressFids(env, lpGtkProgressBar, &PGLOB(GtkProgressFc));
	lpCache->bar_style = (*env)->GetFieldID(env, lpCache->GtkProgressBarClass, "bar_style", "I");
	lpCache->orientation = (*env)->GetFieldID(env, lpCache->GtkProgressBarClass, "orientation", "I");
	lpCache->blocks = (*env)->GetFieldID(env, lpCache->GtkProgressBarClass, "blocks", "I");
	lpCache->in_block = (*env)->GetFieldID(env, lpCache->GtkProgressBarClass, "in_block", "I");
	lpCache->activity_pos = (*env)->GetFieldID(env, lpCache->GtkProgressBarClass, "activity_pos", "I");
	lpCache->activity_step = (*env)->GetFieldID(env, lpCache->GtkProgressBarClass, "activity_step", "I");
	lpCache->activity_blocks = (*env)->GetFieldID(env, lpCache->GtkProgressBarClass, "activity_blocks", "I");
	lpCache->activity_dir = (*env)->GetFieldID(env, lpCache->GtkProgressBarClass, "activity_dir", "I");

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
	lpCache->klass = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "klass", "I");
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
	lpCache->font = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "font", "I");
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
	lpCache->ref_count = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "ref_count", "I");
	lpCache->attach_count = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "attach_count", "I");
	lpCache->depth = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "depth", "I");
	lpCache->colormap = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "colormap", "I");
	lpCache->engine = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "engine", "I");
	lpCache->engine_data = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "engine_data", "I");
	lpCache->rc_style = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "rc_style", "I");
	lpCache->styles = (*env)->GetFieldID(env, lpCache->GtkStyleClazz, "styles", "I");

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

void cacheGtkWidgetFids(JNIEnv *env, jobject lpGtkWidget, PGtkWidget_FID_CACHE lpCache)
{
	DECL_GLOB(pGlob)
	if (lpCache->cached) return;

	lpCache->GtkWidgetClass = (*env)->GetObjectClass(env, lpGtkWidget);
	cacheGtkObjectFids(env, lpGtkWidget, &PGLOB(GtkObjectFc));
	lpCache->private_flags = (*env)->GetFieldID(env, lpCache->GtkWidgetClass, "private_flags", "S");
	lpCache->state = (*env)->GetFieldID(env, lpCache->GtkWidgetClass, "state", "B");
	lpCache->saved_state = (*env)->GetFieldID(env, lpCache->GtkWidgetClass, "saved_state", "B");
	lpCache->name = (*env)->GetFieldID(env, lpCache->GtkWidgetClass, "name", "I");
	lpCache->style = (*env)->GetFieldID(env, lpCache->GtkWidgetClass, "style", "I");
	lpCache->req_width = (*env)->GetFieldID(env, lpCache->GtkWidgetClass, "req_width", "S");
	lpCache->req_height = (*env)->GetFieldID(env, lpCache->GtkWidgetClass, "req_height", "S");
	lpCache->alloc_x = (*env)->GetFieldID(env, lpCache->GtkWidgetClass, "alloc_x", "S");
	lpCache->alloc_y = (*env)->GetFieldID(env, lpCache->GtkWidgetClass, "alloc_y", "S");
	lpCache->alloc_width = (*env)->GetFieldID(env, lpCache->GtkWidgetClass, "alloc_width", "S");
	lpCache->alloc_height = (*env)->GetFieldID(env, lpCache->GtkWidgetClass, "alloc_height", "S");
	lpCache->window = (*env)->GetFieldID(env, lpCache->GtkWidgetClass, "window", "I");
	lpCache->parent = (*env)->GetFieldID(env, lpCache->GtkWidgetClass, "parent", "I");

	lpCache->cached = 1;
};

void cacheGtkFrameFids(JNIEnv *env, jobject lpGtkFrame, PGtkFrame_FID_CACHE lpCache)
{
	DECL_GLOB(pGlob)
	if (lpCache->cached) return;

	lpCache->GtkFrameClass = (*env)->GetObjectClass(env, lpGtkFrame);
	cacheGtkBinFids(env, lpGtkFrame, &PGLOB(GtkBinFc));
	lpCache->label = (*env)->GetFieldID(env, lpCache->GtkFrameClass, "label", "I");
	lpCache->shadow_type = (*env)->GetFieldID(env, lpCache->GtkFrameClass, "shadow_type", "S");
	lpCache->label_width = (*env)->GetFieldID(env, lpCache->GtkFrameClass, "label_width", "S");
	lpCache->label_height = (*env)->GetFieldID(env, lpCache->GtkFrameClass, "label_height", "S");
	lpCache->label_xalign = (*env)->GetFieldID(env, lpCache->GtkFrameClass, "label_xalign", "F");
	lpCache->label_yalign = (*env)->GetFieldID(env, lpCache->GtkFrameClass, "label_yalign", "F");

	lpCache->cached = 1;
};

void cacheGtkWindowFids(JNIEnv *env, jobject lpGtkWindow, PGtkWindow_FID_CACHE lpCache)
{
	DECL_GLOB(pGlob)
	if (lpCache->cached) return;

	lpCache->GtkWindowClass = (*env)->GetObjectClass(env, lpGtkWindow);
	cacheGtkBinFids(env, lpGtkWindow, &PGLOB(GtkBinFc));
	lpCache->title = (*env)->GetFieldID(env, lpCache->GtkWindowClass, "title", "I");
	lpCache->wmclass_name = (*env)->GetFieldID(env, lpCache->GtkWindowClass, "wmclass_name", "I");
	lpCache->wmclass_class = (*env)->GetFieldID(env, lpCache->GtkWindowClass, "wmclass_class", "I");
	lpCache->type = (*env)->GetFieldID(env, lpCache->GtkWindowClass, "type", "I");
	lpCache->focus_widget = (*env)->GetFieldID(env, lpCache->GtkWindowClass, "focus_widget", "I");
	lpCache->default_widget = (*env)->GetFieldID(env, lpCache->GtkWindowClass, "default_widget", "I");
	lpCache->transient_parent = (*env)->GetFieldID(env, lpCache->GtkWindowClass, "transient_parent", "I");
	lpCache->resize_count = (*env)->GetFieldID(env, lpCache->GtkWindowClass, "resize_count", "S");
	lpCache->allow_shrink = (*env)->GetFieldID(env, lpCache->GtkWindowClass, "allow_shrink", "I");
	lpCache->allow_grow = (*env)->GetFieldID(env, lpCache->GtkWindowClass, "allow_grow", "I");
	lpCache->auto_shrink = (*env)->GetFieldID(env, lpCache->GtkWindowClass, "auto_shrink", "I");
	lpCache->handling_resize = (*env)->GetFieldID(env, lpCache->GtkWindowClass, "handling_resize", "I");
	lpCache->position = (*env)->GetFieldID(env, lpCache->GtkWindowClass, "position", "I");
	lpCache->use_uposition = (*env)->GetFieldID(env, lpCache->GtkWindowClass, "use_uposition", "I");
	lpCache->modal = (*env)->GetFieldID(env, lpCache->GtkWindowClass, "modal", "I");

	lpCache->cached = 1;
};

void cacheGtkDialogFids(JNIEnv *env, jobject lpGtkDialog, PGtkDialog_FID_CACHE lpCache)
{
	DECL_GLOB(pGlob)
	if (lpCache->cached) return;

	lpCache->GtkDialogClass = (*env)->GetObjectClass(env, lpGtkDialog);
	cacheGtkWindowFids(env, lpGtkDialog, &PGLOB(GtkWindowFc));
	lpCache->vbox = (*env)->GetFieldID(env, lpCache->GtkDialogClass, "vbox", "I");
	lpCache->action_area = (*env)->GetFieldID(env, lpCache->GtkDialogClass, "action_area", "I");

	lpCache->cached = 1;
};

void cacheGtkCheckMenuItemFids(JNIEnv *env, jobject lpGtkCheckMenuItem, PGtkCheckMenuItem_FID_CACHE lpCache)
{
	DECL_GLOB(pGlob)
	if (lpCache->cached) return;

	lpCache->GtkCheckMenuItemClass = (*env)->GetObjectClass(env, lpGtkCheckMenuItem);
	cacheGtkMenuItemFids(env, lpGtkCheckMenuItem, &PGLOB(GtkMenuItemFc));
	lpCache->active = (*env)->GetFieldID(env, lpCache->GtkCheckMenuItemClass, "active", "I");
	lpCache->always_show_toggle = (*env)->GetFieldID(env, lpCache->GtkCheckMenuItemClass, "always_show_toggle", "I");

	lpCache->cached = 1;
};

void cacheGtkAdjustmentFids(JNIEnv *env, jobject lpGtkAdjustment, PGtkAdjustment_FID_CACHE lpCache)
{
	DECL_GLOB(pGlob)
	if (lpCache->cached) return;

	lpCache->GtkAdjustmentClass = (*env)->GetObjectClass(env, lpGtkAdjustment);
	cacheGtkDataFids(env, lpGtkAdjustment, &PGLOB(GtkDataFc));
	lpCache->lower = (*env)->GetFieldID(env, lpCache->GtkAdjustmentClass, "lower", "F");
	lpCache->upper = (*env)->GetFieldID(env, lpCache->GtkAdjustmentClass, "upper", "F");
	lpCache->value = (*env)->GetFieldID(env, lpCache->GtkAdjustmentClass, "value", "F");
	lpCache->step_increment = (*env)->GetFieldID(env, lpCache->GtkAdjustmentClass, "step_increment", "F");
	lpCache->page_increment = (*env)->GetFieldID(env, lpCache->GtkAdjustmentClass, "page_increment", "F");
	lpCache->page_size = (*env)->GetFieldID(env, lpCache->GtkAdjustmentClass, "page_size", "F");

	lpCache->cached = 1;
};

void cacheGtkBoxFids(JNIEnv *env, jobject lpGtkBox, PGtkBox_FID_CACHE lpCache)
{
	DECL_GLOB(pGlob)
	if (lpCache->cached) return;

	lpCache->GtkBoxClass = (*env)->GetObjectClass(env, lpGtkBox);
	cacheGtkContainerFids(env, lpGtkBox, &PGLOB(GtkContainerFc));
	lpCache->children = (*env)->GetFieldID(env, lpCache->GtkBoxClass, "children", "I");
	lpCache->spacing = (*env)->GetFieldID(env, lpCache->GtkBoxClass, "spacing", "S");
	lpCache->homogeneous = (*env)->GetFieldID(env, lpCache->GtkBoxClass, "homogeneous", "I");

	lpCache->cached = 1;
};

void cacheGtkHBoxFids(JNIEnv *env, jobject lpGtkHBox, PGtkHBox_FID_CACHE lpCache)
{
	DECL_GLOB(pGlob)
	if (lpCache->cached) return;

	lpCache->GtkHBoxClass = (*env)->GetObjectClass(env, lpGtkHBox);
	cacheGtkBoxFids(env, lpGtkHBox, &PGLOB(GtkBoxFc));

	lpCache->cached = 1;
};

void cacheGtkMenuFids(JNIEnv *env, jobject lpGtkMenu, PGtkMenu_FID_CACHE lpCache)
{
	DECL_GLOB(pGlob)
	if (lpCache->cached) return;

	lpCache->GtkMenuClass = (*env)->GetObjectClass(env, lpGtkMenu);
	cacheGtkMenuShellFids(env, lpGtkMenu, &PGLOB(GtkMenuShellFc));
	lpCache->parent_menu_item = (*env)->GetFieldID(env, lpCache->GtkMenuClass, "parent_menu_item", "I");
	lpCache->old_active_menu_item = (*env)->GetFieldID(env, lpCache->GtkMenuClass, "old_active_menu_item", "I");
	lpCache->accel_group = (*env)->GetFieldID(env, lpCache->GtkMenuClass, "accel_group", "I");
	lpCache->position_func = (*env)->GetFieldID(env, lpCache->GtkMenuClass, "position_func", "I");
	lpCache->position_func_data = (*env)->GetFieldID(env, lpCache->GtkMenuClass, "position_func_data", "I");
	lpCache->toplevel = (*env)->GetFieldID(env, lpCache->GtkMenuClass, "toplevel", "I");
	lpCache->tearoff_window = (*env)->GetFieldID(env, lpCache->GtkMenuClass, "tearoff_window", "I");
	lpCache->torn_off = (*env)->GetFieldID(env, lpCache->GtkMenuClass, "torn_off", "I");

	lpCache->cached = 1;
};

void cacheGtkMenuShellFids(JNIEnv *env, jobject lpGtkMenuShell, PGtkMenuShell_FID_CACHE lpCache)
{
	DECL_GLOB(pGlob)
	if (lpCache->cached) return;

	lpCache->GtkMenuShellClass = (*env)->GetObjectClass(env, lpGtkMenuShell);
	cacheGtkContainerFids(env, lpGtkMenuShell, &PGLOB(GtkContainerFc));
	lpCache->active = (*env)->GetFieldID(env, lpCache->GtkMenuShellClass, "active", "I");	
	lpCache->cached = 1;
	
};

void cacheGtkItemFids(JNIEnv *env, jobject lpGtkItem, PGtkItem_FID_CACHE lpCache)
{
	DECL_GLOB(pGlob)
	if (lpCache->cached) return;

	lpCache->GtkItemClass = (*env)->GetObjectClass(env, lpGtkItem);
	cacheGtkBinFids(env, lpGtkItem, &PGLOB(GtkBinFc));

	lpCache->cached = 1;
}

void cacheGtkMenuItemFids(JNIEnv *env, jobject lpGtkMenuItem, PGtkMenuItem_FID_CACHE lpCache)
{
	DECL_GLOB(pGlob)
	if (lpCache->cached) return;

	lpCache->GtkMenuItemClass = (*env)->GetObjectClass(env, lpGtkMenuItem);
	cacheGtkItemFids(env, lpGtkMenuItem, &PGLOB(GtkItemFc));
	lpCache->submenu = (*env)->GetFieldID(env, lpCache->GtkMenuItemClass, "submenu", "I");
	lpCache->accelerator_signal = (*env)->GetFieldID(env, lpCache->GtkMenuItemClass, "accelerator_signal", "I");
	lpCache->toggle_size = (*env)->GetFieldID(env, lpCache->GtkMenuItemClass, "toggle_size", "I");
	lpCache->accelerator_width = (*env)->GetFieldID(env, lpCache->GtkMenuItemClass, "accelerator_width", "I");
	lpCache->show_toggle_indicator = (*env)->GetFieldID(env, lpCache->GtkMenuItemClass, "show_toggle_indicator", "I");
	lpCache->show_submenu_indicator = (*env)->GetFieldID(env, lpCache->GtkMenuItemClass, "show_submenu_indicator", "I");
	lpCache->submenu_placement = (*env)->GetFieldID(env, lpCache->GtkMenuItemClass, "submenu_placement", "I");
	lpCache->submenu_direction = (*env)->GetFieldID(env, lpCache->GtkMenuItemClass, "submenu_direction", "I");
	lpCache->right_justify = (*env)->GetFieldID(env, lpCache->GtkMenuItemClass, "right_justify", "I");
	lpCache->timer = (*env)->GetFieldID(env, lpCache->GtkMenuItemClass, "timer", "I");

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

void getGdkEventKeyFields(JNIEnv *env, jobject lpObject, GdkEvent *lpGdkEvent, GdkEventKey_FID_CACHE *lpGdkEventKeyFc)
{
	GdkEventKey *lpGdkEventKey = (GdkEventKey*)lpGdkEvent;
	lpGdkEventKey->type = (*env)->GetIntField(env, lpObject, lpGdkEventKeyFc->type);
	lpGdkEventKey->window = (GdkWindow*)(*env)->GetIntField(env, lpObject, lpGdkEventKeyFc->window);
	lpGdkEventKey->send_event = (*env)->GetByteField(env, lpObject, lpGdkEventKeyFc->send_event);
	lpGdkEventKey->time = (*env)->GetIntField(env, lpObject, lpGdkEventKeyFc->time);
	lpGdkEventKey->state = (*env)->GetIntField(env, lpObject, lpGdkEventKeyFc->state);
	lpGdkEventKey->keyval = (*env)->GetIntField(env, lpObject, lpGdkEventKeyFc->keyval);
	lpGdkEventKey->length = (*env)->GetIntField(env, lpObject, lpGdkEventKeyFc->length);
	lpGdkEventKey->string = (char*)(*env)->GetIntField(env, lpObject, lpGdkEventKeyFc->string);
}

void setGdkEventKeyFields(JNIEnv *env, jobject lpObject, GdkEvent *lpGdkEvent, GdkEventKey_FID_CACHE *lpGdkEventKeyFc)
{
	GdkEventKey *lpGdkEventKey = (GdkEventKey*)lpGdkEvent;
	(*env)->SetIntField(env, lpObject, lpGdkEventKeyFc->type, (jint)lpGdkEventKey->type);
	(*env)->SetIntField(env, lpObject, lpGdkEventKeyFc->window, (jint)lpGdkEventKey->window);
	(*env)->SetByteField(env, lpObject, lpGdkEventKeyFc->send_event, (jbyte)lpGdkEventKey->send_event);
	(*env)->SetIntField(env, lpObject, lpGdkEventKeyFc->time, (jint)lpGdkEventKey->time);
	(*env)->SetIntField(env, lpObject, lpGdkEventKeyFc->state, (jint)lpGdkEventKey->state);
	(*env)->SetIntField(env, lpObject, lpGdkEventKeyFc->keyval, (jint)lpGdkEventKey->keyval);
	(*env)->SetIntField(env, lpObject, lpGdkEventKeyFc->length, (jint)lpGdkEventKey->length);
	(*env)->SetIntField(env, lpObject, lpGdkEventKeyFc->string, (jint)lpGdkEventKey->string);
}

void getGdkEventButtonFields(JNIEnv *env, jobject lpObject, GdkEvent *lpGdkEvent, GdkEventButton_FID_CACHE *lpGdkEventButtonFc)
{
	GdkEventButton *lpGdkEventButton = (GdkEventButton*)lpGdkEvent;
	lpGdkEventButton->type = (*env)->GetIntField(env, lpObject, lpGdkEventButtonFc->type);
	lpGdkEventButton->window = (GdkWindow*)(*env)->GetIntField(env, lpObject, lpGdkEventButtonFc->window);
	lpGdkEventButton->send_event = (*env)->GetByteField(env, lpObject, lpGdkEventButtonFc->send_event);
	lpGdkEventButton->time = (*env)->GetIntField(env, lpObject, lpGdkEventButtonFc->time);
	lpGdkEventButton->x = (*env)->GetLongField(env, lpObject, lpGdkEventButtonFc->x);
	lpGdkEventButton->y = (*env)->GetLongField(env, lpObject, lpGdkEventButtonFc->y);
	lpGdkEventButton->pressure = (*env)->GetLongField(env, lpObject, lpGdkEventButtonFc->pressure);
	lpGdkEventButton->xtilt = (*env)->GetLongField(env, lpObject, lpGdkEventButtonFc->xtilt);
	lpGdkEventButton->ytilt = (*env)->GetLongField(env, lpObject, lpGdkEventButtonFc->ytilt);
	lpGdkEventButton->state = (*env)->GetIntField(env, lpObject, lpGdkEventButtonFc->state);
	lpGdkEventButton->button = (*env)->GetIntField(env, lpObject, lpGdkEventButtonFc->button);
	lpGdkEventButton->source = (*env)->GetIntField(env, lpObject, lpGdkEventButtonFc->source);
	lpGdkEventButton->deviceid = (*env)->GetIntField(env, lpObject, lpGdkEventButtonFc->deviceid);
	lpGdkEventButton->x_root = (*env)->GetLongField(env, lpObject, lpGdkEventButtonFc->x_root);
	lpGdkEventButton->y_root = (*env)->GetLongField(env, lpObject, lpGdkEventButtonFc->y_root);
}

void setGdkEventButtonFields(JNIEnv *env, jobject lpObject, GdkEvent *lpGdkEvent, GdkEventButton_FID_CACHE *lpGdkEventButtonFc)
{
	GdkEventButton *lpGdkEventButton = (GdkEventButton*)lpGdkEvent;
	(*env)->SetIntField(env, lpObject, lpGdkEventButtonFc->type, (jint)lpGdkEventButton->type);
	(*env)->SetIntField(env, lpObject, lpGdkEventButtonFc->window, (jint)lpGdkEventButton->window);
	(*env)->SetByteField(env, lpObject, lpGdkEventButtonFc->send_event, (jbyte)lpGdkEventButton->send_event);
	(*env)->SetIntField(env, lpObject, lpGdkEventButtonFc->time, (jint)lpGdkEventButton->time);
	(*env)->SetLongField(env, lpObject, lpGdkEventButtonFc->x, (jlong)lpGdkEventButton->x);
	(*env)->SetLongField(env, lpObject, lpGdkEventButtonFc->y, (jlong)lpGdkEventButton->y);
	(*env)->SetLongField(env, lpObject, lpGdkEventButtonFc->pressure, (jlong)lpGdkEventButton->pressure);
	(*env)->SetLongField(env, lpObject, lpGdkEventButtonFc->xtilt, (jlong)lpGdkEventButton->xtilt);
	(*env)->SetLongField(env, lpObject, lpGdkEventButtonFc->ytilt, (jlong)lpGdkEventButton->ytilt);
	(*env)->SetIntField(env, lpObject, lpGdkEventButtonFc->state, (jint)lpGdkEventButton->state);
	(*env)->SetIntField(env, lpObject, lpGdkEventButtonFc->button, (jint)lpGdkEventButton->button);
	(*env)->SetIntField(env, lpObject, lpGdkEventButtonFc->source, (jint)lpGdkEventButton->source);
	(*env)->SetIntField(env, lpObject, lpGdkEventButtonFc->deviceid, (jint)lpGdkEventButton->deviceid);
	(*env)->SetLongField(env, lpObject, lpGdkEventButtonFc->x_root, (jlong)lpGdkEventButton->x_root);
	(*env)->SetLongField(env, lpObject, lpGdkEventButtonFc->y_root, (jlong)lpGdkEventButton->y_root);
}

void getGdkEventMotionFields(JNIEnv *env, jobject lpObject, GdkEvent *lpGdkEvent, GdkEventMotion_FID_CACHE *lpGdkEventMotionFc)
{
	GdkEventMotion *lpGdkEventMotion = (GdkEventMotion*)lpGdkEvent;
	lpGdkEventMotion->type = (*env)->GetIntField(env, lpObject, lpGdkEventMotionFc->type);
	lpGdkEventMotion->window = (GdkWindow*)(*env)->GetIntField(env, lpObject, lpGdkEventMotionFc->window);
	lpGdkEventMotion->send_event = (*env)->GetByteField(env, lpObject, lpGdkEventMotionFc->send_event);
	lpGdkEventMotion->time = (*env)->GetIntField(env, lpObject, lpGdkEventMotionFc->time);
	lpGdkEventMotion->x = (*env)->GetLongField(env, lpObject, lpGdkEventMotionFc->x);
	lpGdkEventMotion->y = (*env)->GetLongField(env, lpObject, lpGdkEventMotionFc->y);
	lpGdkEventMotion->pressure = (*env)->GetLongField(env, lpObject, lpGdkEventMotionFc->pressure);
	lpGdkEventMotion->xtilt = (*env)->GetLongField(env, lpObject, lpGdkEventMotionFc->xtilt);
	lpGdkEventMotion->ytilt = (*env)->GetLongField(env, lpObject, lpGdkEventMotionFc->ytilt);
	lpGdkEventMotion->state = (*env)->GetIntField(env, lpObject, lpGdkEventMotionFc->state);
	lpGdkEventMotion->is_hint = (*env)->GetIntField(env, lpObject, lpGdkEventMotionFc->is_hint);
	lpGdkEventMotion->source = (*env)->GetIntField(env, lpObject, lpGdkEventMotionFc->source);
	lpGdkEventMotion->deviceid = (*env)->GetIntField(env, lpObject, lpGdkEventMotionFc->deviceid);
	lpGdkEventMotion->x_root = (*env)->GetLongField(env, lpObject, lpGdkEventMotionFc->x_root);
	lpGdkEventMotion->y_root = (*env)->GetLongField(env, lpObject, lpGdkEventMotionFc->y_root);
}

void setGdkEventMotionFields(JNIEnv *env, jobject lpObject, GdkEvent *lpGdkEvent, GdkEventMotion_FID_CACHE *lpGdkEventMotionFc)
{
	GdkEventMotion *lpGdkEventMotion = (GdkEventMotion*)lpGdkEvent;
	(*env)->SetIntField(env, lpObject, lpGdkEventMotionFc->type, lpGdkEventMotion->type);
	(*env)->SetIntField(env, lpObject, lpGdkEventMotionFc->window, (jint)lpGdkEventMotion->window);
	(*env)->SetByteField(env, lpObject, lpGdkEventMotionFc->send_event, lpGdkEventMotion->send_event);
	(*env)->SetIntField(env, lpObject, lpGdkEventMotionFc->time, (jint)lpGdkEventMotion->time);
	(*env)->SetLongField(env, lpObject, lpGdkEventMotionFc->x, (jlong)lpGdkEventMotion->x);
	(*env)->SetLongField(env, lpObject, lpGdkEventMotionFc->y, (jlong)lpGdkEventMotion->y);
	(*env)->SetLongField(env, lpObject, lpGdkEventMotionFc->pressure, (jlong)lpGdkEventMotion->pressure);
	(*env)->SetLongField(env, lpObject, lpGdkEventMotionFc->xtilt, (jlong)lpGdkEventMotion->xtilt);
	(*env)->SetLongField(env, lpObject, lpGdkEventMotionFc->ytilt, (jlong)lpGdkEventMotion->ytilt);
	(*env)->SetIntField(env, lpObject, lpGdkEventMotionFc->state, (jint)lpGdkEventMotion->state);
	(*env)->SetIntField(env, lpObject, lpGdkEventMotionFc->is_hint, (jint)lpGdkEventMotion->is_hint);
	(*env)->SetIntField(env, lpObject, lpGdkEventMotionFc->source, (jint)lpGdkEventMotion->source);
	(*env)->SetIntField(env, lpObject, lpGdkEventMotionFc->deviceid, (jint)lpGdkEventMotion->deviceid);
	(*env)->SetLongField(env, lpObject, lpGdkEventMotionFc->x_root, (jlong)lpGdkEventMotion->x_root);
	(*env)->SetLongField(env, lpObject, lpGdkEventMotionFc->y_root, (jlong)lpGdkEventMotion->y_root);
}

void getGdkEventExposeFields(JNIEnv *env, jobject lpObject, GdkEvent *lpGdkEvent, GdkEventExpose_FID_CACHE *lpGdkEventExposeFc)
{
	GdkEventExpose *lpGdkEventExpose = (GdkEventExpose*)lpGdkEvent;
	lpGdkEventExpose->area.x = (*env)->GetShortField(env, lpObject, lpGdkEventExposeFc->x);
	lpGdkEventExpose->area.y = (*env)->GetShortField(env, lpObject, lpGdkEventExposeFc->y);
	lpGdkEventExpose->area.width = (*env)->GetShortField(env, lpObject, lpGdkEventExposeFc->width);
	lpGdkEventExpose->area.height = (*env)->GetShortField(env, lpObject, lpGdkEventExposeFc->height);
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

void getGdkImageFields(JNIEnv *env, jobject lpObject, GdkImage *lpGdkImage, GdkImage_FID_CACHE *lpGdkImageFc)
{
	lpGdkImage->type = (*env)->GetIntField(env, lpObject, lpGdkImageFc->type);
	lpGdkImage->visual = (GdkVisual*)(*env)->GetIntField(env, lpObject, lpGdkImageFc->visual);
	lpGdkImage->byte_order = (*env)->GetIntField(env, lpObject, lpGdkImageFc->byte_order);
	lpGdkImage->width = (*env)->GetShortField(env, lpObject, lpGdkImageFc->width);
	lpGdkImage->height = (*env)->GetShortField(env, lpObject, lpGdkImageFc->height);
	lpGdkImage->depth = (*env)->GetShortField(env, lpObject, lpGdkImageFc->depth);
	lpGdkImage->bpp = (*env)->GetShortField(env, lpObject, lpGdkImageFc->bpp);
	lpGdkImage->bpl = (*env)->GetShortField(env, lpObject, lpGdkImageFc->bpl);
	lpGdkImage->mem = (gpointer)(*env)->GetIntField(env, lpObject, lpGdkImageFc->mem);
}

void setGdkImageFields(JNIEnv *env, jobject lpObject, GdkImage *lpGdkImage, GdkImage_FID_CACHE *lpGdkImageFc)
{
	(*env)->SetIntField(env, lpObject, lpGdkImageFc->type, (jint)lpGdkImage->type);
	(*env)->SetIntField(env, lpObject, lpGdkImageFc->visual, (jint)lpGdkImage->visual);
	(*env)->SetIntField(env, lpObject, lpGdkImageFc->byte_order, (jint)lpGdkImage->byte_order);
	(*env)->SetShortField(env, lpObject, lpGdkImageFc->width, (jshort)lpGdkImage->width);
	(*env)->SetShortField(env, lpObject, lpGdkImageFc->height, (jshort)lpGdkImage->height);
	(*env)->SetShortField(env, lpObject, lpGdkImageFc->depth, (jshort)lpGdkImage->depth);
	(*env)->SetShortField(env, lpObject, lpGdkImageFc->bpp, (jshort)lpGdkImage->bpp);
	(*env)->SetShortField(env, lpObject, lpGdkImageFc->bpl, (jshort)lpGdkImage->bpl);
	(*env)->SetIntField(env, lpObject, lpGdkImageFc->mem, (jint)lpGdkImage->mem);
}

void getGdkPointFields(JNIEnv *env, jobject lpObject, GdkPoint *lpGdkPoint, GdkPoint_FID_CACHE *lpGdkPointFc)
{
	lpGdkPoint->x = (*env)->GetShortField(env, lpObject, lpGdkPointFc->x);
	lpGdkPoint->y = (*env)->GetShortField(env, lpObject, lpGdkPointFc->y);
}

void setGdkPointFields(JNIEnv *env, jobject lpObject, GdkPoint *lpGdkPoint, GdkPoint_FID_CACHE *lpGdkPointFc)
{
	(*env)->SetShortField(env, lpObject, lpGdkPointFc->x, (jshort)lpGdkPoint->x);
	(*env)->SetShortField(env, lpObject, lpGdkPointFc->y, (jshort)lpGdkPoint->y);
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

void getGtkArgFields(JNIEnv *env, jobject lpObject, GtkArg *lpGtkArg, GtkArg_FID_CACHE *lpGtkArgFc)
{
	fprintf(stderr, "WARNING: Unimplemented method getGtkArgFields.\n");
}

void setGtkArgFields(JNIEnv *env, jobject lpObject, GtkArg *lpGtkArg, GtkArg_FID_CACHE *lpGtkArgFc)
{
	fprintf(stderr, "WARNING: Unimplemented method setGtkArgFields.\n");
}

void getGtkBinFields(JNIEnv *env, jobject lpObject, GtkBin *lpGtkBin, GtkBin_FID_CACHE *lpGtkBinFc)
{
	DECL_GLOB(pGlob)
	getGtkContainerFields(env, lpObject, &lpGtkBin->container, &PGLOB(GtkContainerFc));
	lpGtkBin->child = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkBinFc->child);
}

void setGtkBinFields(JNIEnv *env, jobject lpObject, GtkBin *lpGtkBin, GtkBin_FID_CACHE *lpGtkBinFc)
{
	DECL_GLOB(pGlob)
	setGtkContainerFields(env, lpObject, &lpGtkBin->container, &PGLOB(GtkContainerFc));
	(*env)->SetIntField(env, lpObject, lpGtkBinFc->child, (jint)lpGtkBin->child);
}

void getGtkBoxFields(JNIEnv *env, jobject lpObject, GtkBox *lpGtkBox, GtkBox_FID_CACHE *lpGtkBoxFc)
{
	DECL_GLOB(pGlob)
	getGtkContainerFields(env, lpObject, &lpGtkBox->container, &PGLOB(GtkContainerFc));
	lpGtkBox->children = (GList*)(*env)->GetIntField(env, lpObject, lpGtkBoxFc->children);
	lpGtkBox->spacing = (*env)->GetShortField(env, lpObject, lpGtkBoxFc->spacing);
	lpGtkBox->homogeneous = (*env)->GetIntField(env, lpObject, lpGtkBoxFc->homogeneous);
}

void setGtkBoxFields(JNIEnv *env, jobject lpObject, GtkBox *lpGtkBox, GtkBox_FID_CACHE *lpGtkBoxFc)
{
	DECL_GLOB(pGlob)
	setGtkContainerFields(env, lpObject, &lpGtkBox->container, &PGLOB(GtkContainerFc));
	(*env)->SetIntField(env, lpObject, lpGtkBoxFc->children, (jint)lpGtkBox->children);
	(*env)->SetShortField(env, lpObject, lpGtkBoxFc->spacing, (jshort)lpGtkBox->spacing);
	(*env)->SetIntField(env, lpObject, lpGtkBoxFc->homogeneous, (jint)lpGtkBox->homogeneous);
}

void getGtkCListFields(JNIEnv *env, jobject lpObject, GtkCList *lpGtkCList, GtkCList_FID_CACHE *lpGtkCListFc)
{
	DECL_GLOB(pGlob)
	getGtkContainerFields(env, lpObject, &lpGtkCList->container, &PGLOB(GtkContainerFc));
	lpGtkCList->flags = (*env)->GetShortField(env, lpObject, lpGtkCListFc->clist_flags);
	lpGtkCList->row_mem_chunk = (GMemChunk*)(*env)->GetIntField(env, lpObject, lpGtkCListFc->row_mem_chunk);
	lpGtkCList->cell_mem_chunk = (GMemChunk*)(*env)->GetIntField(env, lpObject, lpGtkCListFc->cell_mem_chunk);
	lpGtkCList->freeze_count = (*env)->GetIntField(env, lpObject, lpGtkCListFc->freeze_count);
	lpGtkCList->internal_allocation.x = (*env)->GetShortField(env, lpObject, lpGtkCListFc->internal_allocation_x);
	lpGtkCList->internal_allocation.y = (*env)->GetShortField(env, lpObject, lpGtkCListFc->internal_allocation_y);
	lpGtkCList->internal_allocation.width = (*env)->GetShortField(env, lpObject, lpGtkCListFc->internal_allocation_width);
	lpGtkCList->internal_allocation.height = (*env)->GetShortField(env, lpObject, lpGtkCListFc->internal_allocation_height);
	lpGtkCList->rows = (*env)->GetIntField(env, lpObject, lpGtkCListFc->rows);
	lpGtkCList->row_center_offset = (*env)->GetIntField(env, lpObject, lpGtkCListFc->row_center_offset);
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
	setGtkContainerFields(env, lpObject, &lpGtkCList->container, &PGLOB(GtkContainerFc));
	(*env)->SetShortField(env, lpObject, lpGtkCListFc->clist_flags, (jshort)lpGtkCList->flags);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->row_mem_chunk, (jint)lpGtkCList->row_mem_chunk);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->cell_mem_chunk, (jint)lpGtkCList->cell_mem_chunk);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->freeze_count, (jint)lpGtkCList->freeze_count);
	(*env)->SetShortField(env, lpObject, lpGtkCListFc->internal_allocation_x, (jshort)lpGtkCList->internal_allocation.x);
	(*env)->SetShortField(env, lpObject, lpGtkCListFc->internal_allocation_y, (jshort)lpGtkCList->internal_allocation.y);
	(*env)->SetShortField(env, lpObject, lpGtkCListFc->internal_allocation_width, (jshort)lpGtkCList->internal_allocation.width);
	(*env)->SetShortField(env, lpObject, lpGtkCListFc->internal_allocation_height, (jshort)lpGtkCList->internal_allocation.height);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->rows, (jint)lpGtkCList->rows);
	(*env)->SetIntField(env, lpObject, lpGtkCListFc->row_center_offset, (jint)lpGtkCList->row_center_offset);
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

void getGtkColorSelectionDialogFields(JNIEnv *env, jobject lpObject, GtkColorSelectionDialog *lpGtkColorSelectionDialog, GtkColorSelectionDialog_FID_CACHE *lpGtkColorSelectionDialogFc)
{
	DECL_GLOB(pGlob)
	getGtkWindowFields(env, lpObject, &lpGtkColorSelectionDialog->window, &PGLOB(GtkWindowFc));
	lpGtkColorSelectionDialog->colorsel = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkColorSelectionDialogFc->colorsel);
	lpGtkColorSelectionDialog->main_vbox = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkColorSelectionDialogFc->main_vbox);
	lpGtkColorSelectionDialog->ok_button = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkColorSelectionDialogFc->ok_button);
	lpGtkColorSelectionDialog->reset_button = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkColorSelectionDialogFc->reset_button);
	lpGtkColorSelectionDialog->cancel_button = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkColorSelectionDialogFc->cancel_button);
	lpGtkColorSelectionDialog->help_button = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkColorSelectionDialogFc->help_button);
}

void setGtkColorSelectionDialogFields(JNIEnv *env, jobject lpObject, GtkColorSelectionDialog *lpGtkColorSelectionDialog, GtkColorSelectionDialog_FID_CACHE *lpGtkColorSelectionDialogFc)
{
	DECL_GLOB(pGlob)
	setGtkWindowFields(env, lpObject, &lpGtkColorSelectionDialog->window, &PGLOB(GtkWindowFc));
	(*env)->SetIntField(env, lpObject, lpGtkColorSelectionDialogFc->colorsel, (jint)lpGtkColorSelectionDialog->colorsel);
	(*env)->SetIntField(env, lpObject, lpGtkColorSelectionDialogFc->main_vbox, (jint)lpGtkColorSelectionDialog->main_vbox);
	(*env)->SetIntField(env, lpObject, lpGtkColorSelectionDialogFc->ok_button, (jint)lpGtkColorSelectionDialog->ok_button);
	(*env)->SetIntField(env, lpObject, lpGtkColorSelectionDialogFc->reset_button, (jint)lpGtkColorSelectionDialog->reset_button);
	(*env)->SetIntField(env, lpObject, lpGtkColorSelectionDialogFc->cancel_button, (jint)lpGtkColorSelectionDialog->cancel_button);
	(*env)->SetIntField(env, lpObject, lpGtkColorSelectionDialogFc->help_button, (jint)lpGtkColorSelectionDialog->help_button);
}

void getGtkComboFields(JNIEnv *env, jobject lpObject, GtkCombo *lpGtkCombo, GtkCombo_FID_CACHE *lpGtkComboFc)
{
	DECL_GLOB(pGlob)
	getGtkHBoxFields(env, lpObject, &lpGtkCombo->hbox, &PGLOB(GtkHBoxFc));
	lpGtkCombo->entry = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkComboFc->entry);
	lpGtkCombo->button = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkComboFc->button);
	lpGtkCombo->popup = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkComboFc->popup);
	lpGtkCombo->popwin = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkComboFc->popwin);
	lpGtkCombo->list = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkComboFc->list);
	lpGtkCombo->entry_change_id = (*env)->GetIntField(env, lpObject, lpGtkComboFc->entry_change_id);
	lpGtkCombo->list_change_id = (*env)->GetIntField(env, lpObject, lpGtkComboFc->list_change_id);
	lpGtkCombo->value_in_list = (*env)->GetIntField(env, lpObject, lpGtkComboFc->value_in_list);
	lpGtkCombo->ok_if_empty = (*env)->GetIntField(env, lpObject, lpGtkComboFc->ok_if_empty);
	lpGtkCombo->case_sensitive = (*env)->GetIntField(env, lpObject, lpGtkComboFc->case_sensitive);
	lpGtkCombo->use_arrows = (*env)->GetIntField(env, lpObject, lpGtkComboFc->use_arrows);
	lpGtkCombo->use_arrows_always = (*env)->GetIntField(env, lpObject, lpGtkComboFc->use_arrows_always);
	lpGtkCombo->current_button = (*env)->GetShortField(env, lpObject, lpGtkComboFc->current_button);
	lpGtkCombo->activate_id = (*env)->GetIntField(env, lpObject, lpGtkComboFc->activate_id);
}

void setGtkComboFields(JNIEnv *env, jobject lpObject, GtkCombo *lpGtkCombo, GtkCombo_FID_CACHE *lpGtkComboFc)
{
	DECL_GLOB(pGlob)
	setGtkHBoxFields(env, lpObject, &lpGtkCombo->hbox, &PGLOB(GtkHBoxFc));
	(*env)->SetIntField(env, lpObject, lpGtkComboFc->entry, (jint)lpGtkCombo->entry);
	(*env)->SetIntField(env, lpObject, lpGtkComboFc->button, (jint)lpGtkCombo->button);
	(*env)->SetIntField(env, lpObject, lpGtkComboFc->popup, (jint)lpGtkCombo->popup);
	(*env)->SetIntField(env, lpObject, lpGtkComboFc->popwin, (jint)lpGtkCombo->popwin);
	(*env)->SetIntField(env, lpObject, lpGtkComboFc->list, (jint)lpGtkCombo->list);
	(*env)->SetIntField(env, lpObject, lpGtkComboFc->entry_change_id, (jint)lpGtkCombo->entry_change_id);
	(*env)->SetIntField(env, lpObject, lpGtkComboFc->list_change_id, (jint)lpGtkCombo->list_change_id);
	(*env)->SetIntField(env, lpObject, lpGtkComboFc->value_in_list, (jint)lpGtkCombo->value_in_list);
	(*env)->SetIntField(env, lpObject, lpGtkComboFc->ok_if_empty, (jint)lpGtkCombo->ok_if_empty);
	(*env)->SetIntField(env, lpObject, lpGtkComboFc->case_sensitive, (jint)lpGtkCombo->case_sensitive);
	(*env)->SetIntField(env, lpObject, lpGtkComboFc->use_arrows, (jint)lpGtkCombo->use_arrows);
	(*env)->SetIntField(env, lpObject, lpGtkComboFc->use_arrows_always, (jint)lpGtkCombo->use_arrows_always);
	(*env)->SetShortField(env, lpObject, lpGtkComboFc->current_button, (jshort)lpGtkCombo->current_button);
	(*env)->SetIntField(env, lpObject, lpGtkComboFc->activate_id, (jint)lpGtkCombo->activate_id);
}

void getGtkContainerFields(JNIEnv *env, jobject lpObject, GtkContainer *lpGtkContainer, GtkContainer_FID_CACHE *lpGtkContainerFc)
{
	DECL_GLOB(pGlob)
	getGtkWidgetFields(env, lpObject, &lpGtkContainer->widget, &PGLOB(GtkWidgetFc));
	lpGtkContainer->focus_child = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkContainerFc->focus_child);
	lpGtkContainer->border_width = (*env)->GetIntField(env, lpObject, lpGtkContainerFc->border_width);
	lpGtkContainer->need_resize = (*env)->GetIntField(env, lpObject, lpGtkContainerFc->need_resize);
	lpGtkContainer->resize_mode = (*env)->GetIntField(env, lpObject, lpGtkContainerFc->resize_mode);
	lpGtkContainer->resize_widgets = (GSList*)(*env)->GetIntField(env, lpObject, lpGtkContainerFc->resize_widgets);
}

void setGtkContainerFields(JNIEnv *env, jobject lpObject, GtkContainer *lpGtkContainer, GtkContainer_FID_CACHE *lpGtkContainerFc)
{
	DECL_GLOB(pGlob)
	setGtkWidgetFields(env, lpObject, &lpGtkContainer->widget, &PGLOB(GtkWidgetFc));
	(*env)->SetIntField(env, lpObject, lpGtkContainerFc->focus_child, (jint)lpGtkContainer->focus_child);
	(*env)->SetIntField(env, lpObject, lpGtkContainerFc->border_width, (jint)lpGtkContainer->border_width);
	(*env)->SetIntField(env, lpObject, lpGtkContainerFc->need_resize, (jint)lpGtkContainer->need_resize);
	(*env)->SetIntField(env, lpObject, lpGtkContainerFc->resize_mode, (jint)lpGtkContainer->resize_mode);
	(*env)->SetIntField(env, lpObject, lpGtkContainerFc->resize_widgets, (jint)lpGtkContainer->resize_widgets);
}

void getGtkEditableFields(JNIEnv *env, jobject lpObject, GtkEditable *lpGtkEditable, GtkEditable_FID_CACHE *lpGtkEditableFc)
{
	DECL_GLOB(pGlob)
	getGtkWidgetFields(env, lpObject, &lpGtkEditable->widget, &PGLOB(GtkWidgetFc));
	lpGtkEditable->current_pos = (*env)->GetIntField(env, lpObject, lpGtkEditableFc->current_pos);
	lpGtkEditable->selection_start_pos = (*env)->GetIntField(env, lpObject, lpGtkEditableFc->selection_start_pos);
	lpGtkEditable->selection_end_pos = (*env)->GetIntField(env, lpObject, lpGtkEditableFc->selection_end_pos);
	lpGtkEditable->has_selection = (*env)->GetIntField(env, lpObject, lpGtkEditableFc->has_selection);
	lpGtkEditable->editable = (*env)->GetIntField(env, lpObject, lpGtkEditableFc->editable);
	lpGtkEditable->visible = (*env)->GetIntField(env, lpObject, lpGtkEditableFc->visible);
	lpGtkEditable->ic = (GdkIC*)(*env)->GetIntField(env, lpObject, lpGtkEditableFc->ic);
	lpGtkEditable->ic_attr = (GdkICAttr*)(*env)->GetIntField(env, lpObject, lpGtkEditableFc->ic_attr);
	lpGtkEditable->clipboard_text = (gchar*)(*env)->GetIntField(env, lpObject, lpGtkEditableFc->clipboard_text);
}

void setGtkEditableFields(JNIEnv *env, jobject lpObject, GtkEditable *lpGtkEditable, GtkEditable_FID_CACHE *lpGtkEditableFc)
{
	DECL_GLOB(pGlob)
	setGtkWidgetFields(env, lpObject, &lpGtkEditable->widget, &PGLOB(GtkWidgetFc));
	(*env)->SetIntField(env, lpObject, lpGtkEditableFc->current_pos, (jint)lpGtkEditable->current_pos);
	(*env)->SetIntField(env, lpObject, lpGtkEditableFc->selection_start_pos, (jint)lpGtkEditable->selection_start_pos);
	(*env)->SetIntField(env, lpObject, lpGtkEditableFc->selection_end_pos, (jint)lpGtkEditable->selection_end_pos);
	(*env)->SetIntField(env, lpObject, lpGtkEditableFc->has_selection, (jint)lpGtkEditable->has_selection);
	(*env)->SetIntField(env, lpObject, lpGtkEditableFc->editable, (jint)lpGtkEditable->editable);
	(*env)->SetIntField(env, lpObject, lpGtkEditableFc->visible, (jint)lpGtkEditable->visible);
	(*env)->SetIntField(env, lpObject, lpGtkEditableFc->ic, (jint)lpGtkEditable->ic);
	(*env)->SetIntField(env, lpObject, lpGtkEditableFc->ic_attr, (jint)lpGtkEditable->ic_attr);
	(*env)->SetIntField(env, lpObject, lpGtkEditableFc->clipboard_text, (jint)lpGtkEditable->clipboard_text);
}

void getGtkTextFields(JNIEnv *env, jobject lpObject, GtkText *lpGtkText, GtkText_FID_CACHE *lpGtkTextFc)
{
	DECL_GLOB(pGlob)
	getGtkEditableFields(env, lpObject, &lpGtkText->editable, &PGLOB(GtkEditableFc));

	lpGtkText->first_line_start_index = (*env)->GetIntField(env, lpObject, lpGtkTextFc->first_line_start_index);
	lpGtkText->first_onscreen_hor_pixel = (*env)->GetIntField(env, lpObject, lpGtkTextFc->first_onscreen_hor_pixel);
	lpGtkText->first_onscreen_ver_pixel = (*env)->GetIntField(env, lpObject, lpGtkTextFc->first_onscreen_ver_pixel);
	lpGtkText->default_tab_width = (*env)->GetIntField(env, lpObject, lpGtkTextFc->default_tab_width);
	lpGtkText->cursor_pos_x = (*env)->GetIntField(env, lpObject, lpGtkTextFc->cursor_pos_x);
	lpGtkText->cursor_pos_y = (*env)->GetIntField(env, lpObject, lpGtkTextFc->cursor_pos_y);
	lpGtkText->cursor_virtual_x = (*env)->GetIntField(env, lpObject, lpGtkTextFc->cursor_virtual_x);
}

void setGtkTextFields(JNIEnv *env, jobject lpObject, GtkText *lpGtkText, GtkText_FID_CACHE *lpGtkTextFc)
{
	DECL_GLOB(pGlob)
	setGtkEditableFields(env, lpObject, &lpGtkText->editable, &PGLOB(GtkEditableFc));
	(*env)->SetIntField(env, lpObject, lpGtkTextFc->first_line_start_index, (jint)lpGtkText->first_line_start_index);
	(*env)->SetIntField(env, lpObject, lpGtkTextFc->first_onscreen_hor_pixel, (jint)lpGtkText->first_onscreen_hor_pixel);
	(*env)->SetIntField(env, lpObject, lpGtkTextFc->first_onscreen_ver_pixel, (jint)lpGtkText->first_onscreen_ver_pixel);
	(*env)->SetIntField(env, lpObject, lpGtkTextFc->default_tab_width, (jint)lpGtkText->default_tab_width);
	(*env)->SetIntField(env, lpObject, lpGtkTextFc->cursor_pos_x, (jint)lpGtkText->cursor_pos_x);
	(*env)->SetIntField(env, lpObject, lpGtkTextFc->cursor_pos_y, (jint)lpGtkText->cursor_pos_y);
	(*env)->SetIntField(env, lpObject, lpGtkTextFc->cursor_virtual_x, (jint)lpGtkText->cursor_virtual_x);
}

void getGtkFileSelectionFields(JNIEnv *env, jobject lpObject, GtkFileSelection *lpGtkFileSelection, GtkFileSelection_FID_CACHE *lpGtkFileSelectionFc)
{
	DECL_GLOB(pGlob)
	getGtkWindowFields(env, lpObject, &lpGtkFileSelection->window, &PGLOB(GtkWindowFc));
	lpGtkFileSelection->dir_list = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkFileSelectionFc->dir_list);
	lpGtkFileSelection->file_list = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkFileSelectionFc->file_list);
	lpGtkFileSelection->selection_entry = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkFileSelectionFc->selection_entry);
	lpGtkFileSelection->selection_text = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkFileSelectionFc->selection_text);
	lpGtkFileSelection->main_vbox = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkFileSelectionFc->main_vbox);
	lpGtkFileSelection->ok_button = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkFileSelectionFc->ok_button);
	lpGtkFileSelection->cancel_button = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkFileSelectionFc->cancel_button);
	lpGtkFileSelection->help_button = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkFileSelectionFc->help_button);
	lpGtkFileSelection->history_pulldown = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkFileSelectionFc->history_pulldown);
	lpGtkFileSelection->history_menu = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkFileSelectionFc->history_menu);
	lpGtkFileSelection->history_list = (GList*)(*env)->GetIntField(env, lpObject, lpGtkFileSelectionFc->history_list);
	lpGtkFileSelection->fileop_dialog = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkFileSelectionFc->fileop_dialog);
	lpGtkFileSelection->fileop_entry = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkFileSelectionFc->fileop_entry);
	lpGtkFileSelection->fileop_file = (gchar*)(*env)->GetIntField(env, lpObject, lpGtkFileSelectionFc->fileop_file);
	lpGtkFileSelection->cmpl_state = (gpointer)(*env)->GetIntField(env, lpObject, lpGtkFileSelectionFc->cmpl_state);
	lpGtkFileSelection->fileop_c_dir = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkFileSelectionFc->fileop_c_dir);
	lpGtkFileSelection->fileop_del_file = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkFileSelectionFc->fileop_del_file);
	lpGtkFileSelection->fileop_ren_file = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkFileSelectionFc->fileop_ren_file);
	lpGtkFileSelection->button_area = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkFileSelectionFc->button_area);
	lpGtkFileSelection->action_area = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkFileSelectionFc->action_area);
}

void setGtkFileSelectionFields(JNIEnv *env, jobject lpObject, GtkFileSelection *lpGtkFileSelection, GtkFileSelection_FID_CACHE *lpGtkFileSelectionFc)
{
	DECL_GLOB(pGlob)
	setGtkWindowFields(env, lpObject, &lpGtkFileSelection->window, &PGLOB(GtkWindowFc));
	(*env)->SetIntField(env, lpObject, lpGtkFileSelectionFc->dir_list, (jint)lpGtkFileSelection->dir_list);
	(*env)->SetIntField(env, lpObject, lpGtkFileSelectionFc->file_list, (jint)lpGtkFileSelection->file_list);
	(*env)->SetIntField(env, lpObject, lpGtkFileSelectionFc->selection_entry, (jint)lpGtkFileSelection->selection_entry);
	(*env)->SetIntField(env, lpObject, lpGtkFileSelectionFc->selection_text, (jint)lpGtkFileSelection->selection_text);
	(*env)->SetIntField(env, lpObject, lpGtkFileSelectionFc->main_vbox, (jint)lpGtkFileSelection->main_vbox);
	(*env)->SetIntField(env, lpObject, lpGtkFileSelectionFc->ok_button, (jint)lpGtkFileSelection->ok_button);
	(*env)->SetIntField(env, lpObject, lpGtkFileSelectionFc->cancel_button, (jint)lpGtkFileSelection->cancel_button);
	(*env)->SetIntField(env, lpObject, lpGtkFileSelectionFc->help_button, (jint)lpGtkFileSelection->help_button);
	(*env)->SetIntField(env, lpObject, lpGtkFileSelectionFc->history_pulldown, (jint)lpGtkFileSelection->history_pulldown);
	(*env)->SetIntField(env, lpObject, lpGtkFileSelectionFc->history_menu, (jint)lpGtkFileSelection->history_menu);
	(*env)->SetIntField(env, lpObject, lpGtkFileSelectionFc->history_list, (jint)lpGtkFileSelection->history_list);
	(*env)->SetIntField(env, lpObject, lpGtkFileSelectionFc->fileop_dialog, (jint)lpGtkFileSelection->fileop_dialog);
	(*env)->SetIntField(env, lpObject, lpGtkFileSelectionFc->fileop_entry, (jint)lpGtkFileSelection->fileop_entry);
	(*env)->SetIntField(env, lpObject, lpGtkFileSelectionFc->fileop_file, (jint)lpGtkFileSelection->fileop_file);
	(*env)->SetIntField(env, lpObject, lpGtkFileSelectionFc->cmpl_state, (jint)lpGtkFileSelection->cmpl_state);
	(*env)->SetIntField(env, lpObject, lpGtkFileSelectionFc->fileop_c_dir, (jint)lpGtkFileSelection->fileop_c_dir);
	(*env)->SetIntField(env, lpObject, lpGtkFileSelectionFc->fileop_del_file, (jint)lpGtkFileSelection->fileop_del_file);
	(*env)->SetIntField(env, lpObject, lpGtkFileSelectionFc->fileop_ren_file, (jint)lpGtkFileSelection->fileop_ren_file);
	(*env)->SetIntField(env, lpObject, lpGtkFileSelectionFc->button_area, (jint)lpGtkFileSelection->button_area);
	(*env)->SetIntField(env, lpObject, lpGtkFileSelectionFc->action_area, (jint)lpGtkFileSelection->action_area);
}

void getGtkFontSelectionDialogFields(JNIEnv *env, jobject lpObject, GtkFontSelectionDialog *lpGtkFontSelectionDialog, GtkFontSelectionDialog_FID_CACHE *lpGtkFontSelectionDialogFc)
{
	DECL_GLOB(pGlob)
	getGtkWindowFields(env, lpObject, &lpGtkFontSelectionDialog->window, &PGLOB(GtkWindowFc));
	lpGtkFontSelectionDialog->fontsel = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkFontSelectionDialogFc->fontsel);
	lpGtkFontSelectionDialog->main_vbox = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkFontSelectionDialogFc->main_vbox);
	lpGtkFontSelectionDialog->action_area = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkFontSelectionDialogFc->action_area);
	lpGtkFontSelectionDialog->ok_button = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkFontSelectionDialogFc->ok_button);
	lpGtkFontSelectionDialog->apply_button = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkFontSelectionDialogFc->apply_button);
	lpGtkFontSelectionDialog->cancel_button = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkFontSelectionDialogFc->cancel_button);
	lpGtkFontSelectionDialog->dialog_width = (*env)->GetIntField(env, lpObject, lpGtkFontSelectionDialogFc->dialog_width);
	lpGtkFontSelectionDialog->auto_resize = (*env)->GetIntField(env, lpObject, lpGtkFontSelectionDialogFc->auto_resize);
}

void setGtkFontSelectionDialogFields(JNIEnv *env, jobject lpObject, GtkFontSelectionDialog *lpGtkFontSelectionDialog, GtkFontSelectionDialog_FID_CACHE *lpGtkFontSelectionDialogFc)
{
	DECL_GLOB(pGlob)
	setGtkWindowFields(env, lpObject, &lpGtkFontSelectionDialog->window, &PGLOB(GtkWindowFc));
	(*env)->SetIntField(env, lpObject, lpGtkFontSelectionDialogFc->fontsel, (jint)lpGtkFontSelectionDialog->fontsel);
	(*env)->SetIntField(env, lpObject, lpGtkFontSelectionDialogFc->main_vbox, (jint)lpGtkFontSelectionDialog->main_vbox);
	(*env)->SetIntField(env, lpObject, lpGtkFontSelectionDialogFc->action_area, (jint)lpGtkFontSelectionDialog->action_area);
	(*env)->SetIntField(env, lpObject, lpGtkFontSelectionDialogFc->ok_button, (jint)lpGtkFontSelectionDialog->ok_button);
	(*env)->SetIntField(env, lpObject, lpGtkFontSelectionDialogFc->apply_button, (jint)lpGtkFontSelectionDialog->apply_button);
	(*env)->SetIntField(env, lpObject, lpGtkFontSelectionDialogFc->cancel_button, (jint)lpGtkFontSelectionDialog->cancel_button);
	(*env)->SetIntField(env, lpObject, lpGtkFontSelectionDialogFc->dialog_width, (jint)lpGtkFontSelectionDialog->dialog_width);
	(*env)->SetIntField(env, lpObject, lpGtkFontSelectionDialogFc->auto_resize, (jint)lpGtkFontSelectionDialog->auto_resize);
}

void getGtkHBoxFields(JNIEnv *env, jobject lpObject, GtkHBox *lpGtkHBox, GtkHBox_FID_CACHE *lpGtkHBoxFc)
{
	DECL_GLOB(pGlob)
	getGtkBoxFields(env, lpObject, &lpGtkHBox->box, &PGLOB(GtkBoxFc));
}

void setGtkHBoxFields(JNIEnv *env, jobject lpObject, GtkHBox *lpGtkHBox, GtkHBox_FID_CACHE *lpGtkHBoxFc)
{
	DECL_GLOB(pGlob)
	setGtkBoxFields(env, lpObject, &lpGtkHBox->box, &PGLOB(GtkBoxFc));
}

void getGtkObjectFields(JNIEnv *env, jobject lpObject, GtkObject *lpGtkObject, GtkObject_FID_CACHE *lpGtkObjectFc)
{
	lpGtkObject->klass = (GtkObjectClass*)(*env)->GetIntField(env, lpObject, lpGtkObjectFc->klass);
	lpGtkObject->flags = (*env)->GetIntField(env, lpObject, lpGtkObjectFc->flags);
	lpGtkObject->ref_count = (*env)->GetIntField(env, lpObject, lpGtkObjectFc->ref_count);
	lpGtkObject->object_data = (GData*)(*env)->GetIntField(env, lpObject, lpGtkObjectFc->object_data);
}

void setGtkObjectFields(JNIEnv *env, jobject lpObject, GtkObject *lpGtkObject, GtkObject_FID_CACHE *lpGtkObjectFc)
{
	(*env)->SetIntField(env, lpObject, lpGtkObjectFc->klass, (jint)lpGtkObject->klass);
	(*env)->SetIntField(env, lpObject, lpGtkObjectFc->flags, (jint)lpGtkObject->flags);
	(*env)->SetIntField(env, lpObject, lpGtkObjectFc->ref_count, (jint)lpGtkObject->ref_count);
	(*env)->SetIntField(env, lpObject, lpGtkObjectFc->object_data, (jint)lpGtkObject->object_data);
}

void getGtkProgressFields(JNIEnv *env, jobject lpObject, GtkProgress *lpGtkProgress, GtkProgress_FID_CACHE *lpGtkProgressFc)
{
	DECL_GLOB(pGlob)
	getGtkWidgetFields(env, lpObject, &lpGtkProgress->widget, &PGLOB(GtkWidgetFc));
	lpGtkProgress->adjustment = (GtkAdjustment*)(*env)->GetIntField(env, lpObject, lpGtkProgressFc->adjustment);
	lpGtkProgress->offscreen_pixmap = (GdkPixmap*)(*env)->GetIntField(env, lpObject, lpGtkProgressFc->offscreen_pixmap);
	lpGtkProgress->format = (gchar*)(*env)->GetIntField(env, lpObject, lpGtkProgressFc->format);
	lpGtkProgress->x_align = (*env)->GetFloatField(env, lpObject, lpGtkProgressFc->x_align);
	lpGtkProgress->y_align = (*env)->GetFloatField(env, lpObject, lpGtkProgressFc->y_align);
	lpGtkProgress->show_text = (*env)->GetIntField(env, lpObject, lpGtkProgressFc->show_text);
	lpGtkProgress->activity_mode = (*env)->GetIntField(env, lpObject, lpGtkProgressFc->activity_mode);
}

void setGtkProgressFields(JNIEnv *env, jobject lpObject, GtkProgress *lpGtkProgress, GtkProgress_FID_CACHE *lpGtkProgressFc)
{
	DECL_GLOB(pGlob)
	setGtkWidgetFields(env, lpObject, &lpGtkProgress->widget, &PGLOB(GtkWidgetFc));
	(*env)->SetIntField(env, lpObject, lpGtkProgressFc->adjustment, (jint)lpGtkProgress->adjustment);
	(*env)->SetIntField(env, lpObject, lpGtkProgressFc->offscreen_pixmap, (jint)lpGtkProgress->offscreen_pixmap);
	(*env)->SetIntField(env, lpObject, lpGtkProgressFc->format, (jint)lpGtkProgress->format);
	(*env)->SetFloatField(env, lpObject, lpGtkProgressFc->x_align, (jfloat)lpGtkProgress->x_align);
	(*env)->SetFloatField(env, lpObject, lpGtkProgressFc->y_align, (jfloat)lpGtkProgress->y_align);
	(*env)->SetIntField(env, lpObject, lpGtkProgressFc->show_text, (jint)lpGtkProgress->show_text);
	(*env)->SetIntField(env, lpObject, lpGtkProgressFc->activity_mode, (jint)lpGtkProgress->activity_mode);
}

void getGtkProgressBarFields(JNIEnv *env, jobject lpObject, GtkProgressBar *lpGtkProgressBar, GtkProgressBar_FID_CACHE *lpGtkProgressBarFc)
{
	DECL_GLOB(pGlob)
	getGtkProgressFields(env, lpObject, &lpGtkProgressBar->progress, &PGLOB(GtkProgressFc));
	lpGtkProgressBar->bar_style = (*env)->GetIntField(env, lpObject, lpGtkProgressBarFc->bar_style);
	lpGtkProgressBar->orientation = (*env)->GetIntField(env, lpObject, lpGtkProgressBarFc->orientation);
	lpGtkProgressBar->blocks = (*env)->GetIntField(env, lpObject, lpGtkProgressBarFc->blocks);
	lpGtkProgressBar->in_block = (*env)->GetIntField(env, lpObject, lpGtkProgressBarFc->in_block);
	lpGtkProgressBar->activity_pos = (*env)->GetIntField(env, lpObject, lpGtkProgressBarFc->activity_pos);
	lpGtkProgressBar->activity_step = (*env)->GetIntField(env, lpObject, lpGtkProgressBarFc->activity_step);
	lpGtkProgressBar->activity_blocks = (*env)->GetIntField(env, lpObject, lpGtkProgressBarFc->activity_blocks);
	lpGtkProgressBar->activity_dir = (*env)->GetIntField(env, lpObject, lpGtkProgressBarFc->activity_dir);
}

void setGtkProgressBarFields(JNIEnv *env, jobject lpObject, GtkProgressBar *lpGtkProgressBar, GtkProgressBar_FID_CACHE *lpGtkProgressBarFc)
{
	DECL_GLOB(pGlob)
	setGtkProgressFields(env, lpObject, &lpGtkProgressBar->progress, &PGLOB(GtkProgressFc));
	(*env)->SetIntField(env, lpObject, lpGtkProgressBarFc->bar_style, (jint)lpGtkProgressBar->bar_style);
	(*env)->SetIntField(env, lpObject, lpGtkProgressBarFc->orientation, (jint)lpGtkProgressBar->orientation);
	(*env)->SetIntField(env, lpObject, lpGtkProgressBarFc->blocks, (jint)lpGtkProgressBar->blocks);
	(*env)->SetIntField(env, lpObject, lpGtkProgressBarFc->in_block, (jint)lpGtkProgressBar->in_block);
	(*env)->SetIntField(env, lpObject, lpGtkProgressBarFc->activity_pos, (jint)lpGtkProgressBar->activity_pos);
	(*env)->SetIntField(env, lpObject, lpGtkProgressBarFc->activity_step, (jint)lpGtkProgressBar->activity_step);
	(*env)->SetIntField(env, lpObject, lpGtkProgressBarFc->activity_blocks, (jint)lpGtkProgressBar->activity_blocks);
	(*env)->SetIntField(env, lpObject, lpGtkProgressBarFc->activity_dir, (jint)lpGtkProgressBar->activity_dir);
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
	lpGtkStyle->klass = (GtkStyleClass*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->klass);
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
	lpGtkStyle->font = (GdkFont*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->font);
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
	lpGtkStyle->ref_count = (*env)->GetIntField(env, lpObject, lpGtkStyleFc->ref_count);
	lpGtkStyle->attach_count = (*env)->GetIntField(env, lpObject, lpGtkStyleFc->attach_count);
	lpGtkStyle->depth = (*env)->GetIntField(env, lpObject, lpGtkStyleFc->depth);
	lpGtkStyle->colormap = (GdkColormap*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->colormap);
	lpGtkStyle->engine = (GtkThemeEngine*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->engine);
	lpGtkStyle->engine_data = (gpointer)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->engine_data);
	lpGtkStyle->rc_style = (GtkRcStyle*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->rc_style);
	lpGtkStyle->styles = (GSList*)(*env)->GetIntField(env, lpObject, lpGtkStyleFc->styles);
}

void setGtkStyleFields(JNIEnv *env, jobject lpObject, GtkStyle *lpGtkStyle, GtkStyle_FID_CACHE *lpGtkStyleFc)
{
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->klass, (jint)lpGtkStyle->klass);
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
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->font, (jint)lpGtkStyle->font);
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
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->ref_count, (jint)lpGtkStyle->ref_count);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->attach_count, (jint)lpGtkStyle->attach_count);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->depth, (jint)lpGtkStyle->depth);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->colormap, (jint)lpGtkStyle->colormap);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->engine, (jint)lpGtkStyle->engine);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->engine_data, (jint)lpGtkStyle->engine_data);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->rc_style, (jint)lpGtkStyle->rc_style);
	(*env)->SetIntField(env, lpObject, lpGtkStyleFc->styles, (jint)lpGtkStyle->styles);
}

void getGtkStyleClassFields(JNIEnv *env, jobject lpObject, GtkStyleClass *lpGtkStyleClass, GtkStyleClass_FID_CACHE *lpGtkStyleClassFc)
{
	lpGtkStyleClass->xthickness = (*env)->GetIntField(env, lpObject, lpGtkStyleClassFc->xthickness);
	lpGtkStyleClass->ythickness = (*env)->GetIntField(env, lpObject, lpGtkStyleClassFc->ythickness);
}

void setGtkStyleClassFields(JNIEnv *env, jobject lpObject, GtkStyleClass *lpGtkStyleClass, GtkStyleClass_FID_CACHE *lpGtkStyleClassFc)
{
	(*env)->SetIntField(env, lpObject, lpGtkStyleClassFc->xthickness, (jint)lpGtkStyleClass->xthickness);
	(*env)->SetIntField(env, lpObject, lpGtkStyleClassFc->ythickness, (jint)lpGtkStyleClass->ythickness);
}

void getGtkWidgetFields(JNIEnv *env, jobject lpObject, GtkWidget *lpGtkWidget, GtkWidget_FID_CACHE *lpGtkWidgetFc)
{
	DECL_GLOB(pGlob)
	getGtkObjectFields(env, lpObject, &lpGtkWidget->object, &PGLOB(GtkObjectFc));
	lpGtkWidget->private_flags = (*env)->GetShortField(env, lpObject, lpGtkWidgetFc->private_flags);
	lpGtkWidget->state = (*env)->GetByteField(env, lpObject, lpGtkWidgetFc->state);
	lpGtkWidget->saved_state = (*env)->GetByteField(env, lpObject, lpGtkWidgetFc->saved_state);
	lpGtkWidget->name = (gchar*)(*env)->GetIntField(env, lpObject, lpGtkWidgetFc->name);
	lpGtkWidget->style = (GtkStyle*)(*env)->GetIntField(env, lpObject, lpGtkWidgetFc->style);
	lpGtkWidget->requisition.width = (*env)->GetShortField(env, lpObject, lpGtkWidgetFc->req_width);
	lpGtkWidget->requisition.height = (*env)->GetShortField(env, lpObject, lpGtkWidgetFc->req_height);
	lpGtkWidget->allocation.x = (*env)->GetShortField(env, lpObject, lpGtkWidgetFc->alloc_x);
	lpGtkWidget->allocation.y = (*env)->GetShortField(env, lpObject, lpGtkWidgetFc->alloc_y);
	lpGtkWidget->allocation.width = (*env)->GetShortField(env, lpObject, lpGtkWidgetFc->alloc_width);
	lpGtkWidget->allocation.height = (*env)->GetShortField(env, lpObject, lpGtkWidgetFc->alloc_height);
	lpGtkWidget->window = (GdkWindow*)(*env)->GetIntField(env, lpObject, lpGtkWidgetFc->window);
	lpGtkWidget->parent = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkWidgetFc->parent);
}

void getGtkFrameFields(JNIEnv *env, jobject lpObject, GtkFrame *lpGtkFrame, GtkFrame_FID_CACHE *lpGtkFrameFc)
{
	DECL_GLOB(pGlob)
	getGtkBinFields(env, lpObject, &lpGtkFrame->bin, &PGLOB(GtkBinFc));
	lpGtkFrame->label = (gchar*)(*env)->GetIntField(env, lpObject, lpGtkFrameFc->label);
	lpGtkFrame->shadow_type = (*env)->GetShortField(env, lpObject, lpGtkFrameFc->shadow_type);
	lpGtkFrame->label_width = (*env)->GetShortField(env, lpObject, lpGtkFrameFc->label_width);
	lpGtkFrame->label_height = (*env)->GetShortField(env, lpObject, lpGtkFrameFc->label_height);
	lpGtkFrame->label_xalign = (*env)->GetFloatField(env, lpObject, lpGtkFrameFc->label_xalign);
	lpGtkFrame->label_yalign = (*env)->GetFloatField(env, lpObject, lpGtkFrameFc->label_yalign);
}

void setGtkWidgetFields(JNIEnv *env, jobject lpObject, GtkWidget *lpGtkWidget, GtkWidget_FID_CACHE *lpGtkWidgetFc)
{
	DECL_GLOB(pGlob)
	setGtkObjectFields(env, lpObject, &lpGtkWidget->object, &PGLOB(GtkObjectFc));
	(*env)->SetShortField(env, lpObject, lpGtkWidgetFc->private_flags, (jshort)lpGtkWidget->private_flags);
	(*env)->SetByteField(env, lpObject, lpGtkWidgetFc->state, (jbyte)lpGtkWidget->state);
	(*env)->SetByteField(env, lpObject, lpGtkWidgetFc->saved_state, (jbyte)lpGtkWidget->saved_state);
	(*env)->SetIntField(env, lpObject, lpGtkWidgetFc->name, (jint)lpGtkWidget->name);
	(*env)->SetIntField(env, lpObject, lpGtkWidgetFc->style, (jint)lpGtkWidget->style);
	(*env)->SetShortField(env, lpObject, lpGtkWidgetFc->req_width, (jshort)lpGtkWidget->requisition.width);
	(*env)->SetShortField(env, lpObject, lpGtkWidgetFc->req_height, (jshort)lpGtkWidget->requisition.height);
	(*env)->SetShortField(env, lpObject, lpGtkWidgetFc->alloc_x, (jshort)lpGtkWidget->allocation.x);
	(*env)->SetShortField(env, lpObject, lpGtkWidgetFc->alloc_y, (jshort)lpGtkWidget->allocation.y);
	(*env)->SetShortField(env, lpObject, lpGtkWidgetFc->alloc_width, (jshort)lpGtkWidget->allocation.width);
	(*env)->SetShortField(env, lpObject, lpGtkWidgetFc->alloc_height, (jshort)lpGtkWidget->allocation.height);
	(*env)->SetIntField(env, lpObject, lpGtkWidgetFc->window, (jint)lpGtkWidget->window);
	(*env)->SetIntField(env, lpObject, lpGtkWidgetFc->parent, (jint)lpGtkWidget->parent);
}

void setGtkFrameFields(JNIEnv *env, jobject lpObject, GtkFrame *lpGtkFrame, GtkFrame_FID_CACHE *lpGtkFrameFc)
{
	DECL_GLOB(pGlob)
	setGtkBinFields(env, lpObject, &lpGtkFrame->bin, &PGLOB(GtkBinFc));
	(*env)->SetIntField  (env, lpObject, lpGtkFrameFc->label, (jint)lpGtkFrame->label);
	(*env)->SetShortField(env, lpObject, lpGtkFrameFc->label_width, (jshort)lpGtkFrame->label_width);
	(*env)->SetShortField(env, lpObject, lpGtkFrameFc->label_height, (jshort)lpGtkFrame->label_height);
	(*env)->SetFloatField(env, lpObject, lpGtkFrameFc->label_xalign, (jfloat)lpGtkFrame->label_xalign);
	(*env)->SetFloatField(env, lpObject, lpGtkFrameFc->label_yalign, (jfloat)lpGtkFrame->label_yalign);
}

void getGtkWindowFields(JNIEnv *env, jobject lpObject, GtkWindow *lpGtkWindow, GtkWindow_FID_CACHE *lpGtkWindowFc)
{
	DECL_GLOB(pGlob)
	getGtkBinFields(env, lpObject, &lpGtkWindow->bin, &PGLOB(GtkBinFc));
	lpGtkWindow->title = (gchar*)(*env)->GetIntField(env, lpObject, lpGtkWindowFc->title);
	lpGtkWindow->wmclass_name = (gchar*)(*env)->GetIntField(env, lpObject, lpGtkWindowFc->wmclass_name);
	lpGtkWindow->wmclass_class = (gchar*)(*env)->GetIntField(env, lpObject, lpGtkWindowFc->wmclass_class);
	lpGtkWindow->type = (*env)->GetIntField(env, lpObject, lpGtkWindowFc->type);
	lpGtkWindow->focus_widget = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkWindowFc->focus_widget);
	lpGtkWindow->default_widget = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkWindowFc->default_widget);
	lpGtkWindow->transient_parent = (GtkWindow*)(*env)->GetIntField(env, lpObject, lpGtkWindowFc->transient_parent);
	lpGtkWindow->resize_count = (*env)->GetShortField(env, lpObject, lpGtkWindowFc->resize_count);
	lpGtkWindow->allow_shrink = (*env)->GetIntField(env, lpObject, lpGtkWindowFc->allow_shrink);
	lpGtkWindow->allow_grow = (*env)->GetIntField(env, lpObject, lpGtkWindowFc->allow_grow);
	lpGtkWindow->auto_shrink = (*env)->GetIntField(env, lpObject, lpGtkWindowFc->auto_shrink);
	lpGtkWindow->handling_resize = (*env)->GetIntField(env, lpObject, lpGtkWindowFc->handling_resize);
	lpGtkWindow->position = (*env)->GetIntField(env, lpObject, lpGtkWindowFc->position);
	lpGtkWindow->use_uposition = (*env)->GetIntField(env, lpObject, lpGtkWindowFc->use_uposition);
	lpGtkWindow->modal = (*env)->GetIntField(env, lpObject, lpGtkWindowFc->title);
}

void setGtkWindowFields(JNIEnv *env, jobject lpObject, GtkWindow *lpGtkWindow, GtkWindow_FID_CACHE *lpGtkWindowFc)
{
	DECL_GLOB(pGlob)
	setGtkBinFields(env, lpObject, &lpGtkWindow->bin, &PGLOB(GtkBinFc));
	(*env)->SetIntField(env, lpObject, lpGtkWindowFc->title, (jint)lpGtkWindow->title);
	(*env)->SetIntField(env, lpObject, lpGtkWindowFc->wmclass_name, (jint)lpGtkWindow->wmclass_name);
	(*env)->SetIntField(env, lpObject, lpGtkWindowFc->wmclass_class, (jint)lpGtkWindow->wmclass_class);
	(*env)->SetIntField(env, lpObject, lpGtkWindowFc->type, (jint)lpGtkWindow->type);
	(*env)->SetIntField(env, lpObject, lpGtkWindowFc->focus_widget, (jint)lpGtkWindow->focus_widget);
	(*env)->SetIntField(env, lpObject, lpGtkWindowFc->default_widget, (jint)lpGtkWindow->default_widget);
	(*env)->SetIntField(env, lpObject, lpGtkWindowFc->transient_parent, (jint)lpGtkWindow->title);
	(*env)->SetShortField(env, lpObject, lpGtkWindowFc->resize_count, (jshort)lpGtkWindow->resize_count);
	(*env)->SetIntField(env, lpObject, lpGtkWindowFc->allow_shrink, (jint)lpGtkWindow->allow_shrink);
	(*env)->SetIntField(env, lpObject, lpGtkWindowFc->allow_grow, (jint)lpGtkWindow->allow_grow);
	(*env)->SetIntField(env, lpObject, lpGtkWindowFc->auto_shrink, (jint)lpGtkWindow->auto_shrink);
	(*env)->SetIntField(env, lpObject, lpGtkWindowFc->handling_resize, (jint)lpGtkWindow->handling_resize);
	(*env)->SetIntField(env, lpObject, lpGtkWindowFc->position, (jint)lpGtkWindow->position);
	(*env)->SetIntField(env, lpObject, lpGtkWindowFc->use_uposition, (jint)lpGtkWindow->use_uposition);
	(*env)->SetIntField(env, lpObject, lpGtkWindowFc->modal, (jint)lpGtkWindow->modal);
}

void getGtkDialogFields(JNIEnv *env, jobject lpObject, GtkDialog *lpGtkDialog, GtkDialog_FID_CACHE *lpGtkDialogFc)
{
	DECL_GLOB(pGlob)
	getGtkWindowFields(env, lpObject, &lpGtkDialog->window, &PGLOB(GtkWindowFc));
	lpGtkDialog->vbox = (GtkWidget*) (*env)->GetIntField(env, lpObject, lpGtkDialogFc->vbox);
	lpGtkDialog->action_area = (GtkWidget*) (*env)->GetIntField(env, lpObject, lpGtkDialogFc->action_area);
}

void setGtkDialogFields(JNIEnv *env, jobject lpObject, GtkDialog *lpGtkDialog, GtkDialog_FID_CACHE *lpGtkDialogFc)
{
	DECL_GLOB(pGlob)
	setGtkWindowFields(env, lpObject, &lpGtkDialog->window, &PGLOB(GtkWindowFc));
	(*env)->SetIntField(env, lpObject, lpGtkDialogFc->vbox, (jint)lpGtkDialog->vbox);
	(*env)->SetIntField(env, lpObject, lpGtkDialogFc->action_area, (jint)lpGtkDialog->action_area);
}

void getGtkMenuFields(JNIEnv *env, jobject lpObject, GtkMenu *lpGtkMenu, GtkMenu_FID_CACHE *lpGtkMenuFc)
{
	DECL_GLOB(pGlob)
	getGtkMenuShellFields(env, lpObject, &lpGtkMenu->menu_shell, &PGLOB(GtkMenuShellFc));
	lpGtkMenu->parent_menu_item = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkMenuFc->parent_menu_item);
	lpGtkMenu->old_active_menu_item = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkMenuFc->old_active_menu_item);
	lpGtkMenu->accel_group = (GtkAccelGroup*)(*env)->GetIntField(env, lpObject, lpGtkMenuFc->accel_group);
	lpGtkMenu->position_func = (GtkMenuPositionFunc)(*env)->GetIntField(env, lpObject, lpGtkMenuFc->position_func);
	lpGtkMenu->position_func_data = (gpointer)(*env)->GetIntField(env, lpObject, lpGtkMenuFc->position_func_data);
	lpGtkMenu->toplevel = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkMenuFc->toplevel);
	lpGtkMenu->tearoff_window = (GtkWidget*)(*env)->GetIntField(env, lpObject, lpGtkMenuFc->tearoff_window);
	lpGtkMenu->torn_off = (*env)->GetIntField(env, lpObject, lpGtkMenuFc->torn_off);
}

void setGtkMenuFields(JNIEnv *env, jobject lpObject, GtkMenu *lpGtkMenu, GtkMenu_FID_CACHE *lpGtkMenuFc)
{
	DECL_GLOB(pGlob)
	setGtkMenuShellFields(env, lpObject, &lpGtkMenu->menu_shell, &PGLOB(GtkMenuShellFc));
	(*env)->SetIntField(env, lpObject, lpGtkMenuFc->parent_menu_item, (jint)lpGtkMenu->parent_menu_item);
	(*env)->SetIntField(env, lpObject, lpGtkMenuFc->old_active_menu_item, (jint)lpGtkMenu->old_active_menu_item);
	(*env)->SetIntField(env, lpObject, lpGtkMenuFc->accel_group, (jint)lpGtkMenu->accel_group);
	(*env)->SetIntField(env, lpObject, lpGtkMenuFc->position_func, (jint)lpGtkMenu->position_func);
	(*env)->SetIntField(env, lpObject, lpGtkMenuFc->position_func_data, (jint)lpGtkMenu->position_func_data);
	(*env)->SetIntField(env, lpObject, lpGtkMenuFc->toplevel, (jint)lpGtkMenu->toplevel);
	(*env)->SetIntField(env, lpObject, lpGtkMenuFc->tearoff_window, (jint)lpGtkMenu->tearoff_window);
	(*env)->SetIntField(env, lpObject, lpGtkMenuFc->torn_off, (jint)lpGtkMenu->torn_off);
}

void getGtkMenuShellFields(JNIEnv *env, jobject lpObject, GtkMenuShell *lpGtkMenuShell, GtkMenuShell_FID_CACHE *lpGtkMenuShellFc)
{
	DECL_GLOB(pGlob)
	getGtkContainerFields(env, lpObject, &lpGtkMenuShell->container, &PGLOB(GtkContainerFc));
	lpGtkMenuShell->active = (*env)->GetIntField(env, lpObject, lpGtkMenuShellFc->active);
}

void setGtkMenuShellFields(JNIEnv *env, jobject lpObject, GtkMenuShell *lpGtkMenuShell, GtkMenuShell_FID_CACHE *lpGtkMenuShellFc)
{
	DECL_GLOB(pGlob)
	setGtkContainerFields(env, lpObject, &lpGtkMenuShell->container, &PGLOB(GtkContainerFc));
	(*env)->SetIntField(env, lpObject, lpGtkMenuShellFc->active, (jint)lpGtkMenuShell->active);
}

void getGtkItemFields(JNIEnv *env, jobject lpObject, GtkItem *lpGtkItem, GtkItem_FID_CACHE *lpGtkItemFc)
{
	DECL_GLOB(pGlob)
	getGtkBinFields(env, lpObject, &lpGtkItem->bin, &PGLOB(GtkBinFc));
}

void setGtkItemFields(JNIEnv *env, jobject lpObject, GtkItem *lpGtkItem, GtkItem_FID_CACHE *lpGtkItemFc)
{
	DECL_GLOB(pGlob)
	setGtkBinFields(env, lpObject, &lpGtkItem->bin, &PGLOB(GtkBinFc));
}

void getGtkMenuItemFields(JNIEnv *env, jobject lpObject, GtkMenuItem *lpGtkMenuItem, GtkMenuItem_FID_CACHE *lpGtkMenuItemFc)
{
	DECL_GLOB(pGlob)
	getGtkItemFields(env, lpObject, &lpGtkMenuItem->item, &PGLOB(GtkItemFc));
}

void setGtkMenuItemFields(JNIEnv *env, jobject lpObject, GtkMenuItem *lpGtkMenuItem, GtkMenuItem_FID_CACHE *lpGtkMenuItemFc)
{
	DECL_GLOB(pGlob)
	setGtkItemFields(env, lpObject, &lpGtkMenuItem->item, &PGLOB(GtkItemFc));
}

void getGtkCheckMenuItemFields(JNIEnv *env, jobject lpObject, GtkCheckMenuItem *lpGtkCheckMenuItem, GtkCheckMenuItem_FID_CACHE *lpGtkCheckMenuItemFc)
{
	DECL_GLOB(pGlob)
	getGtkMenuItemFields(env, lpObject, &lpGtkCheckMenuItem->menu_item, &PGLOB(GtkMenuItemFc));
	lpGtkCheckMenuItem->active = (*env)->GetIntField(env, lpObject, lpGtkCheckMenuItemFc->active);
	lpGtkCheckMenuItem->always_show_toggle = (*env)->GetIntField(env, lpObject, lpGtkCheckMenuItemFc->always_show_toggle);
}

void setGtkCheckMenuItemFields(JNIEnv *env, jobject lpObject, GtkCheckMenuItem *lpGtkCheckMenuItem, GtkCheckMenuItem_FID_CACHE *lpGtkCheckMenuItemFc)
{
	DECL_GLOB(pGlob)
	setGtkMenuItemFields(env, lpObject, &lpGtkCheckMenuItem->menu_item, &PGLOB(GtkMenuItemFc));
	(*env)->SetIntField(env, lpObject, lpGtkCheckMenuItemFc->active, (jint)lpGtkCheckMenuItem->active);
	(*env)->SetIntField(env, lpObject, lpGtkCheckMenuItemFc->always_show_toggle, (jint)lpGtkCheckMenuItem->always_show_toggle);
}

void getGtkDataFields(JNIEnv *env, jobject lpObject, GtkData *lpGtkData, GtkData_FID_CACHE *lpGtkDataFc)
{
	DECL_GLOB(pGlob)
	getGtkObjectFields(env, lpObject, &lpGtkData->object, &PGLOB(GtkObjectFc));
}

void setGtkDataFields(JNIEnv *env, jobject lpObject, GtkData *lpGtkData, GtkData_FID_CACHE *lpGtkDataFc)
{
	DECL_GLOB(pGlob)
	setGtkObjectFields(env, lpObject, &lpGtkData->object, &PGLOB(GtkObjectFc));
}

void getGtkAdjustmentFields(JNIEnv *env, jobject lpObject, GtkAdjustment *lpGtkAdjustment, GtkAdjustment_FID_CACHE *lpGtkAdjustmentFc)
{
	DECL_GLOB(pGlob)
	getGtkDataFields(env, lpObject, &lpGtkAdjustment->data, &PGLOB(GtkDataFc));
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
	setGtkDataFields(env, lpObject, &lpGtkAdjustment->data, &PGLOB(GtkDataFc));
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
