/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

 /**
 * JNI SWT object field getters and setters declarations for Photon structs
 */

#ifndef INC_structs_H
#define INC_structs_H

#include <Ph.h>
#include <Pt.h>
#include <photon/PhRender.h>

/* PhPoint_t struct */
typedef struct PhPoint_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID x, y;
} PhPoint_t_FID_CACHE;
typedef PhPoint_t_FID_CACHE *PPhPoint_t_FID_CACHE;

void cachePhPoint_tFids(JNIEnv *env, jobject lpObject, PPhPoint_t_FID_CACHE lpCache);
void getPhPoint_tFields(JNIEnv *env, jobject lpObject, PhPoint_t *lpStruct, PPhPoint_t_FID_CACHE lpCache);
void setPhPoint_tFields(JNIEnv *env, jobject lpObject, PhPoint_t *lpStruct, PPhPoint_t_FID_CACHE lpCache);

/* PhRect_t struct */
typedef struct PhRect_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID ul_x, ul_y, lr_x, lr_y;
} PhRect_t_FID_CACHE;
typedef PhRect_t_FID_CACHE *PPhRect_t_FID_CACHE;

void cachePhRect_tFids(JNIEnv *env, jobject lpObject, PPhRect_t_FID_CACHE lpCache);
void getPhRect_tFields(JNIEnv *env, jobject lpObject, PhRect_t *lpStruct, PPhRect_t_FID_CACHE lpCache);
void setPhRect_tFields(JNIEnv *env, jobject lpObject, PhRect_t *lpStruct, PPhRect_t_FID_CACHE lpCache);

typedef struct PhTile_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID rect_ul_x, rect_ul_y, rect_lr_x, rect_lr_y, next;
} PhTile_t_FID_CACHE;
typedef PhTile_t_FID_CACHE *PPhTile_t_FID_CACHE;

void cachePhTile_tFids(JNIEnv *env, jobject lpObject, PPhTile_t_FID_CACHE lpCache);
void getPhTile_tFields(JNIEnv *env, jobject lpObject, PhTile_t *lpStruct, PPhTile_t_FID_CACHE lpCache);
void setPhTile_tFields(JNIEnv *env, jobject lpObject, PhTile_t *lpStruct, PPhTile_t_FID_CACHE lpCache);

typedef struct PtCallbackInfo_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID reason, reason_subtype, event, cbdata;
} PtCallbackInfo_t_FID_CACHE;
typedef PtCallbackInfo_t_FID_CACHE *PPtCallbackInfo_t_FID_CACHE;

void cachePtCallbackInfo_tFids(JNIEnv *env, jobject lpObject, PPtCallbackInfo_t_FID_CACHE lpCache);
void getPtCallbackInfo_tFields(JNIEnv *env, jobject lpObject, PtCallbackInfo_t *lpStruct, PPtCallbackInfo_t_FID_CACHE lpCache);
void setPtCallbackInfo_tFields(JNIEnv *env, jobject lpObject, PtCallbackInfo_t *lpStruct, PPtCallbackInfo_t_FID_CACHE lpCache);

typedef struct PhWindowEvent_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID event_f, state_f, rid, pos_x, pos_y, size_w, size_h, event_state, input_group, rsvd0, rsvd1, rsvd2, rsvd3;
} PhWindowEvent_t_FID_CACHE;
typedef PhWindowEvent_t_FID_CACHE *PPhWindowEvent_t_FID_CACHE;

void cachePhWindowEvent_tFids(JNIEnv *env, jobject lpObject, PPhWindowEvent_t_FID_CACHE lpCache);
void getPhWindowEvent_tFields(JNIEnv *env, jobject lpObject, PhWindowEvent_t *lpStruct, PPhWindowEvent_t_FID_CACHE lpCache);
void setPhWindowEvent_tFields(JNIEnv *env, jobject lpObject, PhWindowEvent_t *lpStruct, PPhWindowEvent_t_FID_CACHE lpCache);

/* PhEvent_t struct */
typedef struct PhEvent_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID type, subtype, processing_flags, emitter_rid, emitter_handle, collector_rid, collector_handle, input_group, flags, timestamp, translation_x, translation_y, num_rects, data_len;
} PhEvent_t_FID_CACHE;
typedef PhEvent_t_FID_CACHE *PPhEvent_t_FID_CACHE;

void cachePhEvent_tFids(JNIEnv *env, jobject lpObject, PPhEvent_t_FID_CACHE lpCache);
void getPhEvent_tFields(JNIEnv *env, jobject lpObject, PhEvent_t *lpStruct, PPhEvent_t_FID_CACHE lpCache);
void setPhEvent_tFields(JNIEnv *env, jobject lpObject, PhEvent_t *lpStruct, PPhEvent_t_FID_CACHE lpCache);

/* FontQueryInfo struct */
typedef struct FontQueryInfo_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID font, desc, size, style, ascender, descender, width, lochar, hichar;
} FontQueryInfo_FID_CACHE;
typedef FontQueryInfo_FID_CACHE *PFontQueryInfo_FID_CACHE;

void cacheFontQueryInfoFids(JNIEnv *env, jobject lpObject, PFontQueryInfo_FID_CACHE lpCache);
void getFontQueryInfoFields(JNIEnv *env, jobject lpObject, FontQueryInfo *lpStruct, PFontQueryInfo_FID_CACHE lpCache);
void setFontQueryInfoFields(JNIEnv *env, jobject lpObject, FontQueryInfo *lpStruct, PFontQueryInfo_FID_CACHE lpCache);

/* PhDim_t struct */
typedef struct PhDim_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID w, h;
} PhDim_t_FID_CACHE;
typedef PhDim_t_FID_CACHE *PPhDim_t_FID_CACHE;

void cachePhDim_tFids(JNIEnv *env, jobject lpObject, PPhDim_t_FID_CACHE lpCache);
void getPhDim_tFields(JNIEnv *env, jobject lpObject, PhDim_t *lpStruct, PPhDim_t_FID_CACHE lpCache);
void setPhDim_tFields(JNIEnv *env, jobject lpObject, PhDim_t *lpStruct, PPhDim_t_FID_CACHE lpCache);

/* PhImage_t struct */
typedef struct PhImage_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID type, image_tag, bpl, size_w, size_h, palette_tag, colors, alpha, transparent, format, flags, ghost_bpl, spare1, ghost_bitmap, mask_bpl, mask_bm, palette, image;
} PhImage_t_FID_CACHE;
typedef PhImage_t_FID_CACHE *PPhImage_t_FID_CACHE;

void cachePhImage_tFids(JNIEnv *env, jobject lpObject, PPhImage_t_FID_CACHE lpCache);
void getPhImage_tFields(JNIEnv *env, jobject lpObject, PhImage_t *lpStruct, PPhImage_t_FID_CACHE lpCache);
void setPhImage_tFields(JNIEnv *env, jobject lpObject, PhImage_t *lpStruct, PPhImage_t_FID_CACHE lpCache);

/* PhPointerEvent_t struct */
typedef struct PhPointerEvent_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID pos_x, pos_y, buttons, button_state, click_count, flags, z, key_mods, zero;
} PhPointerEvent_t_FID_CACHE;
typedef PhPointerEvent_t_FID_CACHE *PPhPointerEvent_t_FID_CACHE;

void cachePhPointerEvent_tFids(JNIEnv *env, jobject lpObject, PPhPointerEvent_t_FID_CACHE lpCache);
void getPhPointerEvent_tFields(JNIEnv *env, jobject lpObject, PhPointerEvent_t *lpStruct, PPhPointerEvent_t_FID_CACHE lpCache);
void setPhPointerEvent_tFields(JNIEnv *env, jobject lpObject, PhPointerEvent_t *lpStruct, PPhPointerEvent_t_FID_CACHE lpCache);

/* PhKeyEvent_t struct */
typedef struct PhKeyEvent_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID key_flags, key_mods, key_sym, key_cap, key_scan, key_zero, pos_x, pos_y, button_state;
} PhKeyEvent_t_FID_CACHE;
typedef PhKeyEvent_t_FID_CACHE *PPhKeyEvent_t_FID_CACHE;

void cachePhKeyEvent_tFids(JNIEnv *env, jobject lpObject, PPhKeyEvent_t_FID_CACHE lpCache);
void getPhKeyEvent_tFields(JNIEnv *env, jobject lpObject, PhKeyEvent_t *lpStruct, PPhKeyEvent_t_FID_CACHE lpCache);
void setPhKeyEvent_tFields(JNIEnv *env, jobject lpObject, PhKeyEvent_t *lpStruct, PPhKeyEvent_t_FID_CACHE lpCache);

/* PtScrollbarCallback_t struct */
typedef struct PtScrollbarCallback_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID action, position;
} PtScrollbarCallback_t_FID_CACHE;
typedef PtScrollbarCallback_t_FID_CACHE *PPtScrollbarCallback_t_FID_CACHE;

void cachePtScrollbarCallback_tFids(JNIEnv *env, jobject lpObject, PPtScrollbarCallback_t_FID_CACHE lpCache);
void getPtScrollbarCallback_tFields(JNIEnv *env, jobject lpObject, PtScrollbarCallback_t *lpStruct, PPtScrollbarCallback_t_FID_CACHE lpCache);
void setPtScrollbarCallback_tFields(JNIEnv *env, jobject lpObject, PtScrollbarCallback_t *lpStruct, PPtScrollbarCallback_t_FID_CACHE lpCache);

/* PhCursorInfo_t struct */
typedef struct PhCursorInfo_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID pos_x, pos_y, region, ig_region, color, last_press_x, last_press_y, msec, steady_x, steady_y, dragger, drag_boundary_xUL, drag_boundary_yUL, drag_boundary_xLR, drag_boundary_yLR, phantom_rid, type, ig, button_state, click_count, zero10, zero11, zero12, key_mods, zero2;
} PhCursorInfo_t_FID_CACHE;
typedef PhCursorInfo_t_FID_CACHE *PPhCursorInfo_t_FID_CACHE;

void cachePhCursorInfo_tFids(JNIEnv *env, jobject lpObject, PPhCursorInfo_t_FID_CACHE lpCache);
void getPhCursorInfo_tFields(JNIEnv *env, jobject lpObject, PhCursorInfo_t *lpStruct, PPhCursorInfo_t_FID_CACHE lpCache);
void setPhCursorInfo_tFields(JNIEnv *env, jobject lpObject, PhCursorInfo_t *lpStruct, PPhCursorInfo_t_FID_CACHE lpCache);

/* FontDetails struct */
typedef struct FontDetails_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID desc, stem, losize, hisize, flags;
} FontDetails_FID_CACHE;
typedef FontDetails_FID_CACHE *PFontDetails_FID_CACHE;

void cacheFontDetailsFids(JNIEnv *env, jobject lpObject, PFontDetails_FID_CACHE lpCache);
void getFontDetailsFields(JNIEnv *env, jobject lpObject, FontDetails *lpStruct, PFontDetails_FID_CACHE lpCache);
void setFontDetailsFields(JNIEnv *env, jobject lpObject, FontDetails *lpStruct, PFontDetails_FID_CACHE lpCache);

/* PhArea_t struct */
typedef struct PhArea_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID pos_x, pos_y, size_w, size_h;
} PhArea_t_FID_CACHE;
typedef PhArea_t_FID_CACHE *PPhArea_t_FID_CACHE;

void cachePhArea_tFids(JNIEnv *env, jobject lpObject, PPhArea_t_FID_CACHE lpCache);
void getPhArea_tFields(JNIEnv *env, jobject lpObject, PhArea_t *lpStruct, PPhArea_t_FID_CACHE lpCache);
void setPhArea_tFields(JNIEnv *env, jobject lpObject, PhArea_t *lpStruct, PPhArea_t_FID_CACHE lpCache);

/* PtFileSelectionInfo_t struct */
typedef struct PtFileSelectionInfo_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID ret, path, dim, pos, format, fspec, user_data, confirm_display, confirm_selection, new_directory, btn1, btn2, num_args, args, spare;
} PtFileSelectionInfo_t_FID_CACHE;
typedef PtFileSelectionInfo_t_FID_CACHE *PPtFileSelectionInfo_t_FID_CACHE;

void cachePtFileSelectionInfo_tFids(JNIEnv *env, jobject lpObject, PPtFileSelectionInfo_t_FID_CACHE lpCache);
void getPtFileSelectionInfo_tFields(JNIEnv *env, jobject lpObject, PtFileSelectionInfo_t *lpStruct, PPtFileSelectionInfo_t_FID_CACHE lpCache);
void setPtFileSelectionInfo_tFields(JNIEnv *env, jobject lpObject, PtFileSelectionInfo_t *lpStruct, PPtFileSelectionInfo_t_FID_CACHE lpCache);

/* PgAlpha_t struct */
typedef struct PgAlpha_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID alpha_op, src_alpha_map_dim_w, src_alpha_map_dim_h, src_alpha_map_bpl, src_alpha_map_bpp, src_alpha_map_map, src_global_alpha, dest_global_alpha;
} PgAlpha_t_FID_CACHE;
typedef PgAlpha_t_FID_CACHE *PPgAlpha_t_FID_CACHE;

void cachePgAlpha_tFids(JNIEnv *env, jobject lpObject, PPgAlpha_t_FID_CACHE lpCache);
void getPgAlpha_tFields(JNIEnv *env, jobject lpObject, PgAlpha_t *lpStruct, PPgAlpha_t_FID_CACHE lpCache);
void setPgAlpha_tFields(JNIEnv *env, jobject lpObject, PgAlpha_t *lpStruct, PPgAlpha_t_FID_CACHE lpCache);

/* PtTextCallback_t struct */
typedef struct PtTextCallback_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID start_pos, end_pos, cur_insert, new_insert, length, reserved, text, doit;
} PtTextCallback_t_FID_CACHE;
typedef PtTextCallback_t_FID_CACHE *PPtTextCallback_t_FID_CACHE;

void cachePtTextCallback_tFids(JNIEnv *env, jobject lpObject, PPtTextCallback_t_FID_CACHE lpCache);
void getPtTextCallback_tFields(JNIEnv *env, jobject lpObject, PtTextCallback_t *lpStruct, PPtTextCallback_t_FID_CACHE lpCache);
void setPtTextCallback_tFields(JNIEnv *env, jobject lpObject, PtTextCallback_t *lpStruct, PPtTextCallback_t_FID_CACHE lpCache);

/* PtTreeItem_t struct */
typedef struct PtTreeItem_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID list_flags, list_size_w, list_size_h, list_next, list_prev, father, son, brother, dim_w, dim_h, img_set, img_unset, data;
} PtTreeItem_t_FID_CACHE;
typedef PtTreeItem_t_FID_CACHE *PPtTreeItem_t_FID_CACHE;

void cachePtTreeItem_tFids(JNIEnv *env, jobject lpObject, PPtTreeItem_t_FID_CACHE lpCache);
void getPtTreeItem_tFields(JNIEnv *env, jobject lpObject, PtTreeItem_t *lpStruct, PPtTreeItem_t_FID_CACHE lpCache);
void setPtTreeItem_tFields(JNIEnv *env, jobject lpObject, PtTreeItem_t *lpStruct, PPtTreeItem_t_FID_CACHE lpCache);

/* PgMap_t struct */
typedef struct PgMap_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID dim_w, dim_h, bpl, bpp, map;
} PgMap_t_FID_CACHE;
typedef PgMap_t_FID_CACHE *PPgMap_t_FID_CACHE;

void cachePgMap_tFids(JNIEnv *env, jobject lpObject, PPgMap_t_FID_CACHE lpCache);
void getPgMap_tFields(JNIEnv *env, jobject lpObject, PgMap_t *lpStruct, PPgMap_t_FID_CACHE lpCache);
void setPgMap_tFields(JNIEnv *env, jobject lpObject, PgMap_t *lpStruct, PPgMap_t_FID_CACHE lpCache);

/* PtColorSelectInfo_t struct */
typedef struct PtColorSelectInfo_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID flags, nselectors, ncolor_models, color_models, selectors, pos_x, pos_y, size_w, size_h, palette, accept_text, dismiss_text, accept_dismiss_text, apply_f, data, rgb, dialog;
	} PtColorSelectInfo_t_FID_CACHE;
typedef PtColorSelectInfo_t_FID_CACHE *PPtColorSelectInfo_t_FID_CACHE;

void cachePtColorSelectInfo_tFids(JNIEnv *env, jobject lpObject, PPtColorSelectInfo_t_FID_CACHE lpCache);
void getPtColorSelectInfo_tFields(JNIEnv *env, jobject lpObject, PtColorSelectInfo_t *lpStruct, PPtColorSelectInfo_t_FID_CACHE lpCache);
void setPtColorSelectInfo_tFields(JNIEnv *env, jobject lpObject, PtColorSelectInfo_t *lpStruct, PPtColorSelectInfo_t_FID_CACHE lpCache);

/* PhRegion_t struct */
typedef struct PhRegion_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID rid, handle, owner, flags, state, events_sense, events_opaque, origin_x, origin_y, parent, child, bro_in_front, bro_behind, cursor_color, input_group, data_len, cursor_type;
} PhRegion_t_FID_CACHE;
typedef PhRegion_t_FID_CACHE *PPhRegion_t_FID_CACHE;

void cachePhRegion_tFids(JNIEnv *env, jobject lpObject, PPhRegion_t_FID_CACHE lpCache);
void getPhRegion_tFields(JNIEnv *env, jobject lpObject, PhRegion_t *lpStruct, PPhRegion_t_FID_CACHE lpCache);
void setPhRegion_tFields(JNIEnv *env, jobject lpObject, PhRegion_t *lpStruct, PPhRegion_t_FID_CACHE lpCache);

/* PtContainerCallback_t struct */
typedef struct PtContainerCallback_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID new_size_ul_x, new_size_ul_y, new_size_lr_x, new_size_lr_y, old_size_ul_x, old_size_ul_y, old_size_lr_x, old_size_lr_y, new_dim_w, new_dim_h, old_dim_w, old_dim_h;
} PtContainerCallback_t_FID_CACHE;
typedef PtContainerCallback_t_FID_CACHE *PPtContainerCallback_t_FID_CACHE;

void cachePtContainerCallback_tFids(JNIEnv *env, jobject lpObject, PPtContainerCallback_t_FID_CACHE lpCache);
void getPtContainerCallback_tFields(JNIEnv *env, jobject lpObject, PtContainerCallback_t *lpStruct, PPtContainerCallback_t_FID_CACHE lpCache);
void setPtContainerCallback_tFields(JNIEnv *env, jobject lpObject, PtContainerCallback_t *lpStruct, PPtContainerCallback_t_FID_CACHE lpCache);

/* PhCursorDef_t struct */
typedef struct PhCursorDef_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID bytesperline2, color2, offset2_y, offset2_x, size2_y, size2_x, bytesperline1, color1, offset1_y, offset1_x, size1_y, size1_x, hdr_type, hdr_len;
} PhCursorDef_t_FID_CACHE;
typedef PhCursorDef_t_FID_CACHE *PPhCursorDef_t_FID_CACHE;

void cachePhCursorDef_tFids(JNIEnv *env, jobject lpObject, PPhCursorDef_t_FID_CACHE lpCache);
void getPhCursorDef_tFields(JNIEnv *env, jobject lpObject, PhCursorDef_t *lpStruct, PPhCursorDef_t_FID_CACHE lpCache);
void setPhCursorDef_tFields(JNIEnv *env, jobject lpObject, PhCursorDef_t *lpStruct, PPhCursorDef_t_FID_CACHE lpCache);

/* PgDisplaySettings_t struct */
typedef struct PgDisplaySettings_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID reserved, flags, refresh, yres, xres, mode;
} PgDisplaySettings_t_FID_CACHE;
typedef PgDisplaySettings_t_FID_CACHE *PPgDisplaySettings_t_FID_CACHE;

void cachePgDisplaySettings_tFids(JNIEnv *env, jobject lpObject, PPgDisplaySettings_t_FID_CACHE lpCache);
void getPgDisplaySettings_tFields(JNIEnv *env, jobject lpObject, PgDisplaySettings_t *lpStruct, PPgDisplaySettings_t_FID_CACHE lpCache);
void setPgDisplaySettings_tFields(JNIEnv *env, jobject lpObject, PgDisplaySettings_t *lpStruct, PPgDisplaySettings_t_FID_CACHE lpCache);

typedef struct PgVideoModeInfo_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID refresh_rates, mode_capabilities6, mode_capabilities5, mode_capabilities4, mode_capabilities3, mode_capabilities2, mode_capabilities1, type, bytes_per_scanline, bits_per_pixel, height, width;
} PgVideoModeInfo_t_FID_CACHE;
typedef PgVideoModeInfo_t_FID_CACHE *PPgVideoModeInfo_t_FID_CACHE;

void cachePgVideoModeInfo_tFids(JNIEnv *env, jobject lpObject, PPgVideoModeInfo_t_FID_CACHE lpCache);
void getPgVideoModeInfo_tFields(JNIEnv *env, jobject lpObject, PgVideoModeInfo_t *lpStruct, PPgVideoModeInfo_t_FID_CACHE lpCache);
void setPgVideoModeInfo_tFields(JNIEnv *env, jobject lpObject, PgVideoModeInfo_t *lpStruct, PPgVideoModeInfo_t_FID_CACHE lpCache);

/* PhClipHeader struct */
typedef struct PhClipHeader_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID data, length, type_7, type_6, type_5, type_4, type_3, type_2, type_1, type_0;
} PhClipHeader_FID_CACHE;
typedef PhClipHeader_FID_CACHE *PPhClipHeader_FID_CACHE;

void cachePhClipHeaderFids(JNIEnv *env, jobject lpObject, PPhClipHeader_FID_CACHE lpCache);
void getPhClipHeaderFields(JNIEnv *env, jobject lpObject, PhClipHeader *lpStruct, PPhClipHeader_FID_CACHE lpCache);
void setPhClipHeaderFields(JNIEnv *env, jobject lpObject, PhClipHeader *lpStruct, PPhClipHeader_FID_CACHE lpCache);

/* PtTreeCallback_t struct */
typedef struct PtTreeCallback_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID sel_mode, item, nitems, expand, click_count, old_iflags, new_iflags, column, cattr;
} PtTreeCallback_t_FID_CACHE;
typedef PtTreeCallback_t_FID_CACHE *PPtTreeCallback_t_FID_CACHE;

void cachePtTreeCallback_tFids(JNIEnv *env, jobject lpObject, PPtTreeCallback_t_FID_CACHE lpCache);
PtTreeCallback_t* getPtTreeCallback_tFields(JNIEnv *env, jobject lpObject, PtTreeCallback_t *lpStruct, PPtTreeCallback_t_FID_CACHE lpCache);
void setPtTreeCallback_tFields(JNIEnv *env, jobject lpObject, PtTreeCallback_t *lpStruct, PPtTreeCallback_t_FID_CACHE lpCache);

extern PhPoint_t_FID_CACHE PhPoint_tFc;
extern PhRect_t_FID_CACHE PhRect_tFc;
extern PhTile_t_FID_CACHE PhTile_tFc;
extern PtCallbackInfo_t_FID_CACHE PtCallbackInfo_tFc;
extern PhWindowEvent_t_FID_CACHE PhWindowEvent_tFc;
extern PhEvent_t_FID_CACHE PhEvent_tFc;
extern FontQueryInfo_FID_CACHE FontQueryInfoFc;
extern PhDim_t_FID_CACHE PhDim_tFc;
extern PhImage_t_FID_CACHE PhImage_tFc;
extern PhPointerEvent_t_FID_CACHE PhPointerEvent_tFc;
extern PhKeyEvent_t_FID_CACHE PhKeyEvent_tFc;
extern PtScrollbarCallback_t_FID_CACHE PtScrollbarCallback_tFc;
extern PhCursorInfo_t_FID_CACHE PhCursorInfo_tFc;
extern FontDetails_FID_CACHE FontDetailsFc;
extern PhArea_t_FID_CACHE PhArea_tFc;
extern PtFileSelectionInfo_t_FID_CACHE PtFileSelectionInfo_tFc;
extern PgAlpha_t_FID_CACHE PgAlpha_tFc;
extern PtTextCallback_t_FID_CACHE PtTextCallback_tFc;
extern PtTreeItem_t_FID_CACHE PtTreeItem_tFc;
extern PgMap_t_FID_CACHE PgMap_tFc;
extern PtColorSelectInfo_t_FID_CACHE PtColorSelectInfo_tFc;
extern PhRegion_t_FID_CACHE PhRegion_tFc;
extern PtContainerCallback_t_FID_CACHE PtContainerCallback_tFc;
extern PhCursorDef_t_FID_CACHE PhCursorDef_tFc;
extern PgDisplaySettings_t_FID_CACHE PgDisplaySettings_tFc;
extern PgVideoModeInfo_t_FID_CACHE PgVideoModeInfo_tFc;
extern PhClipHeader_FID_CACHE PhClipHeaderFc;
extern PtTreeCallback_t_FID_CACHE PtTreeCallback_tFc;

#endif // INC_structs_H
