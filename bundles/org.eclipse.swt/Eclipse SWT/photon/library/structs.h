 /**
 * JNI SWT object field getters and setters declarations for Photon structs
 */

#ifndef INC_structs_H
#define INC_structs_H

#include <Ph.h>
#include <Pt.h>
#include <photon/PhRender.h>

/* All globals to be declared in globals.h */
#define FID_CACHE_GLOBALS \
	PhPoint_t_FID_CACHE PhPoint_tFc; \
	PhRect_t_FID_CACHE PhRect_tFc; \
	PhTile_t_FID_CACHE PhTile_tFc; \
	PtCallbackInfo_t_FID_CACHE PtCallbackInfo_tFc; \
	PhWindowEvent_t_FID_CACHE PhWindowEvent_tFc; \
	PhEvent_t_FID_CACHE PhEvent_tFc; \
	FontQueryInfo_FID_CACHE FontQueryInfoFc; \
	PhDim_t_FID_CACHE PhDim_tFc; \
	PhImage_t_FID_CACHE PhImage_tFc; \
	PhPointerEvent_t_FID_CACHE PhPointerEvent_tFc; \
	PhKeyEvent_t_FID_CACHE PhKeyEvent_tFc; \
	PtScrollbarCallback_t_FID_CACHE PtScrollbarCallback_tFc; \
	PhCursorInfo_t_FID_CACHE PhCursorInfo_tFc; \
	FontDetails_FID_CACHE FontDetailsFc; \
	PhArea_t_FID_CACHE PhArea_tFc; \
	PtFileSelectionInfo_t_FID_CACHE PtFileSelectionInfo_tFc; \
	PgAlpha_t_FID_CACHE PgAlpha_tFc; \
	PtTextCallback_t_FID_CACHE PtTextCallback_tFc; \
	PtTreeItem_t_FID_CACHE PtTreeItem_tFc; \
	PgMap_t_FID_CACHE PgMap_tFc; \
	PtColorSelectInfo_t_FID_CACHE PtColorSelectInfo_tFc; \
	PhRegion_t_FID_CACHE PhRegion_tFc;

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
	jfieldID key_flags, key_mods, key_sym, key_cap, key_scan, key_zero1, key_zero2, pos_x, pos_y, button_state;
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

#endif // INC_structs_H
