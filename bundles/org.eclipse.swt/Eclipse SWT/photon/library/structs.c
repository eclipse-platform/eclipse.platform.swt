/*
 * (c) Copyright IBM Corp., 2000, 2001
 * All Rights Reserved.
 */

 /**
 * JNI SWT object field getters and setters declarations for Motif structs.
 */

#include <jni.h>
#include "globals.h"
#include "structs.h"

void cachePhPoint_tFids(JNIEnv *env, jobject lpObject, PPhPoint_t_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->x = (*env)->GetFieldID(env, lpCache->clazz, "x", "S");
	lpCache->y = (*env)->GetFieldID(env, lpCache->clazz, "y", "S");
	lpCache->cached = 1;
}

void getPhPoint_tFields(JNIEnv *env, jobject lpObject, PhPoint_t *lpStruct, PPhPoint_t_FID_CACHE lpCache)
{
	lpStruct->x = (*env)->GetShortField(env, lpObject, lpCache->x);
	lpStruct->y = (*env)->GetShortField(env, lpObject, lpCache->y);
}

void setPhPoint_tFields(JNIEnv *env, jobject lpObject, PhPoint_t *lpStruct, PPhPoint_t_FID_CACHE lpCache)
{
	(*env)->SetShortField(env, lpObject, lpCache->x, lpStruct->x);
	(*env)->SetShortField(env, lpObject, lpCache->y, lpStruct->y);
}

void cachePhRect_tFids(JNIEnv *env, jobject lpObject, PPhRect_t_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->ul_x = (*env)->GetFieldID(env, lpCache->clazz, "ul_x", "S");
	lpCache->ul_y = (*env)->GetFieldID(env, lpCache->clazz, "ul_y", "S");
	lpCache->lr_x = (*env)->GetFieldID(env, lpCache->clazz, "lr_x", "S");
	lpCache->lr_y = (*env)->GetFieldID(env, lpCache->clazz, "lr_y", "S");
	lpCache->cached = 1;
}

void getPhRect_tFields(JNIEnv *env, jobject lpObject, PhRect_t *lpStruct, PPhRect_t_FID_CACHE lpCache)
{
	lpStruct->ul.x = (*env)->GetShortField(env, lpObject, lpCache->ul_x);
	lpStruct->ul.y = (*env)->GetShortField(env, lpObject, lpCache->ul_y);
	lpStruct->lr.x = (*env)->GetShortField(env, lpObject, lpCache->lr_x);
	lpStruct->lr.y = (*env)->GetShortField(env, lpObject, lpCache->lr_y);
}

void setPhRect_tFields(JNIEnv *env, jobject lpObject, PhRect_t *lpStruct, PPhRect_t_FID_CACHE lpCache)
{
	(*env)->SetShortField(env, lpObject, lpCache->ul_x, lpStruct->ul.x);
	(*env)->SetShortField(env, lpObject, lpCache->ul_y, lpStruct->ul.y);
	(*env)->SetShortField(env, lpObject, lpCache->lr_x, lpStruct->lr.x);
	(*env)->SetShortField(env, lpObject, lpCache->lr_y, lpStruct->lr.y);
}

void cachePhTile_tFids(JNIEnv *env, jobject lpObject, PPhTile_t_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->rect_ul_x = (*env)->GetFieldID(env, lpCache->clazz, "rect_ul_x", "S");
	lpCache->rect_ul_y = (*env)->GetFieldID(env, lpCache->clazz, "rect_ul_y", "S");
	lpCache->rect_lr_x = (*env)->GetFieldID(env, lpCache->clazz, "rect_lr_x", "S");
	lpCache->rect_lr_y = (*env)->GetFieldID(env, lpCache->clazz, "rect_lr_y", "S");
	lpCache->next = (*env)->GetFieldID(env, lpCache->clazz, "next", "I");
	lpCache->cached = 1;
}

void getPhTile_tFields(JNIEnv *env, jobject lpObject, PhTile_t *lpStruct, PPhTile_t_FID_CACHE lpCache)
{
	lpStruct->rect.ul.x = (*env)->GetShortField(env, lpObject, lpCache->rect_ul_x);
	lpStruct->rect.ul.y = (*env)->GetShortField(env, lpObject, lpCache->rect_ul_y);
	lpStruct->rect.lr.x = (*env)->GetShortField(env, lpObject, lpCache->rect_lr_x);
	lpStruct->rect.lr.y = (*env)->GetShortField(env, lpObject, lpCache->rect_lr_y);
	lpStruct->next = (PhTile_t *)(*env)->GetIntField(env, lpObject, lpCache->next);
}

void setPhTile_tFields(JNIEnv *env, jobject lpObject, PhTile_t *lpStruct, PPhTile_t_FID_CACHE lpCache)
{
	(*env)->SetShortField(env, lpObject, lpCache->rect_ul_x, lpStruct->rect.ul.x);
	(*env)->SetShortField(env, lpObject, lpCache->rect_ul_y, lpStruct->rect.ul.y);
	(*env)->SetShortField(env, lpObject, lpCache->rect_lr_x, lpStruct->rect.lr.x);
	(*env)->SetShortField(env, lpObject, lpCache->rect_lr_y, lpStruct->rect.lr.y);
	(*env)->SetIntField(env, lpObject, lpCache->next, (jint)lpStruct->next);
}

void cachePtCallbackInfo_tFids(JNIEnv *env, jobject lpObject, PPtCallbackInfo_t_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->reason = (*env)->GetFieldID(env, lpCache->clazz, "reason", "I");
	lpCache->reason_subtype = (*env)->GetFieldID(env, lpCache->clazz, "reason_subtype", "I");
	lpCache->event = (*env)->GetFieldID(env, lpCache->clazz, "event", "I");
	lpCache->cbdata = (*env)->GetFieldID(env, lpCache->clazz, "cbdata", "I");
	lpCache->cached = 1;
}

void getPtCallbackInfo_tFields(JNIEnv *env, jobject lpObject, PtCallbackInfo_t *lpStruct, PPtCallbackInfo_t_FID_CACHE lpCache)
{
	lpStruct->reason = (*env)->GetIntField(env, lpObject, lpCache->reason);
	lpStruct->reason_subtype = (*env)->GetIntField(env, lpObject, lpCache->reason_subtype);
	lpStruct->event = (PhEvent_t *)(*env)->GetIntField(env, lpObject, lpCache->event);
	lpStruct->cbdata = (void *)(*env)->GetIntField(env, lpObject, lpCache->cbdata);
}

void setPtCallbackInfo_tFields(JNIEnv *env, jobject lpObject, PtCallbackInfo_t *lpStruct, PPtCallbackInfo_t_FID_CACHE lpCache)
{
	(*env)->SetIntField(env, lpObject, lpCache->reason, lpStruct->reason);
	(*env)->SetIntField(env, lpObject, lpCache->reason_subtype, lpStruct->reason_subtype);
	(*env)->SetIntField(env, lpObject, lpCache->event, (jint)lpStruct->event);
	(*env)->SetIntField(env, lpObject, lpCache->cbdata, (jint)lpStruct->cbdata);
}

void cachePhWindowEvent_tFids(JNIEnv *env, jobject lpObject, PPhWindowEvent_t_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->event_f = (*env)->GetFieldID(env, lpCache->clazz, "event_f", "I");
	lpCache->state_f = (*env)->GetFieldID(env, lpCache->clazz, "state_f", "I");
	lpCache->rid = (*env)->GetFieldID(env, lpCache->clazz, "rid", "I");
	lpCache->pos_x = (*env)->GetFieldID(env, lpCache->clazz, "pos_x", "S");
	lpCache->pos_y = (*env)->GetFieldID(env, lpCache->clazz, "pos_y", "S");
	lpCache->size_w = (*env)->GetFieldID(env, lpCache->clazz, "size_w", "S");
	lpCache->size_h = (*env)->GetFieldID(env, lpCache->clazz, "size_h", "S");
	lpCache->event_state = (*env)->GetFieldID(env, lpCache->clazz, "event_state", "S");
	lpCache->input_group = (*env)->GetFieldID(env, lpCache->clazz, "input_group", "S");
	lpCache->rsvd0 = (*env)->GetFieldID(env, lpCache->clazz, "rsvd0", "I");
	lpCache->rsvd1 = (*env)->GetFieldID(env, lpCache->clazz, "rsvd1", "I");
	lpCache->rsvd2 = (*env)->GetFieldID(env, lpCache->clazz, "rsvd2", "I");
	lpCache->rsvd3 = (*env)->GetFieldID(env, lpCache->clazz, "rsvd3", "I");
	lpCache->cached = 1;
}

void getPhWindowEvent_tFields(JNIEnv *env, jobject lpObject, PhWindowEvent_t *lpStruct, PPhWindowEvent_t_FID_CACHE lpCache)
{
	lpStruct->event_f = (*env)->GetIntField(env, lpObject, lpCache->event_f);
	lpStruct->state_f = (*env)->GetIntField(env, lpObject, lpCache->state_f);
	lpStruct->rid = (*env)->GetIntField(env, lpObject, lpCache->rid);
	lpStruct->pos.x = (*env)->GetShortField(env, lpObject, lpCache->pos_x);
	lpStruct->pos.y = (*env)->GetShortField(env, lpObject, lpCache->pos_y);
	lpStruct->size.w = (*env)->GetShortField(env, lpObject, lpCache->size_w);
	lpStruct->size.h = (*env)->GetShortField(env, lpObject, lpCache->size_h);
	lpStruct->event_state = (*env)->GetShortField(env, lpObject, lpCache->event_state);
	lpStruct->input_group = (*env)->GetShortField(env, lpObject, lpCache->input_group);
	lpStruct->rsvd[0] = (*env)->GetIntField(env, lpObject, lpCache->rsvd0);
	lpStruct->rsvd[1] = (*env)->GetIntField(env, lpObject, lpCache->rsvd1);
	lpStruct->rsvd[2] = (*env)->GetIntField(env, lpObject, lpCache->rsvd2);
	lpStruct->rsvd[3] = (*env)->GetIntField(env, lpObject, lpCache->rsvd3);
}

void setPhWindowEvent_tFields(JNIEnv *env, jobject lpObject, PhWindowEvent_t *lpStruct, PPhWindowEvent_t_FID_CACHE lpCache)
{
	(*env)->SetIntField(env, lpObject, lpCache->event_f, lpStruct->event_f);
	(*env)->SetIntField(env, lpObject, lpCache->state_f, lpStruct->state_f);
	(*env)->SetIntField(env, lpObject, lpCache->rid, lpStruct->rid);
	(*env)->SetShortField(env, lpObject, lpCache->pos_x, lpStruct->pos.x);
	(*env)->SetShortField(env, lpObject, lpCache->pos_y, lpStruct->pos.y);
	(*env)->SetShortField(env, lpObject, lpCache->size_w, lpStruct->size.w);
	(*env)->SetShortField(env, lpObject, lpCache->size_h, lpStruct->size.h);
	(*env)->SetShortField(env, lpObject, lpCache->event_state, lpStruct->event_state);
	(*env)->SetShortField(env, lpObject, lpCache->input_group, lpStruct->input_group);
	(*env)->SetIntField(env, lpObject, lpCache->rsvd0, lpStruct->rsvd[0]);
	(*env)->SetIntField(env, lpObject, lpCache->rsvd1, lpStruct->rsvd[1]);
	(*env)->SetIntField(env, lpObject, lpCache->rsvd2, lpStruct->rsvd[2]);
	(*env)->SetIntField(env, lpObject, lpCache->rsvd3, lpStruct->rsvd[3]);
}


void cachePhEvent_tFids(JNIEnv *env, jobject lpObject, PPhEvent_t_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->type = (*env)->GetFieldID(env, lpCache->clazz, "type", "I");
	lpCache->subtype = (*env)->GetFieldID(env, lpCache->clazz, "subtype", "S");
	lpCache->processing_flags = (*env)->GetFieldID(env, lpCache->clazz, "processing_flags", "S");
	lpCache->emitter_rid = (*env)->GetFieldID(env, lpCache->clazz, "emitter_rid", "I");
	lpCache->emitter_handle = (*env)->GetFieldID(env, lpCache->clazz, "emitter_handle", "I");
	lpCache->collector_rid = (*env)->GetFieldID(env, lpCache->clazz, "collector_rid", "I");
	lpCache->collector_handle = (*env)->GetFieldID(env, lpCache->clazz, "collector_handle", "I");
	lpCache->input_group = (*env)->GetFieldID(env, lpCache->clazz, "input_group", "S");
	lpCache->flags = (*env)->GetFieldID(env, lpCache->clazz, "flags", "S");
	lpCache->timestamp = (*env)->GetFieldID(env, lpCache->clazz, "timestamp", "I");
	lpCache->translation_x = (*env)->GetFieldID(env, lpCache->clazz, "translation_x", "S");
	lpCache->translation_y = (*env)->GetFieldID(env, lpCache->clazz, "translation_y", "S");
	lpCache->num_rects = (*env)->GetFieldID(env, lpCache->clazz, "num_rects", "S");
	lpCache->data_len = (*env)->GetFieldID(env, lpCache->clazz, "data_len", "S");
	lpCache->cached = 1;
}

void getPhEvent_tFields(JNIEnv *env, jobject lpObject, PhEvent_t *lpStruct, PPhEvent_t_FID_CACHE lpCache)
{
	lpStruct->type = (*env)->GetIntField(env, lpObject, lpCache->type);
	lpStruct->subtype = (*env)->GetShortField(env, lpObject, lpCache->subtype);
	lpStruct->processing_flags = (*env)->GetShortField(env, lpObject, lpCache->processing_flags);
	lpStruct->emitter.rid = (*env)->GetIntField(env, lpObject, lpCache->emitter_rid);
	lpStruct->emitter.handle = (*env)->GetIntField(env, lpObject, lpCache->emitter_handle);
	lpStruct->collector.rid = (*env)->GetIntField(env, lpObject, lpCache->collector_rid);
	lpStruct->collector.handle = (*env)->GetIntField(env, lpObject, lpCache->collector_handle);
	lpStruct->input_group = (*env)->GetShortField(env, lpObject, lpCache->input_group);
	lpStruct->flags = (*env)->GetShortField(env, lpObject, lpCache->flags);
	lpStruct->timestamp = (*env)->GetIntField(env, lpObject, lpCache->timestamp);
	lpStruct->translation.x = (*env)->GetShortField(env, lpObject, lpCache->translation_x);
	lpStruct->translation.y = (*env)->GetShortField(env, lpObject, lpCache->translation_y);
	lpStruct->num_rects = (*env)->GetShortField(env, lpObject, lpCache->num_rects);
	lpStruct->data_len = (*env)->GetShortField(env, lpObject, lpCache->data_len);
}

void setPhEvent_tFields(JNIEnv *env, jobject lpObject, PhEvent_t *lpStruct, PPhEvent_t_FID_CACHE lpCache)
{
	(*env)->SetIntField(env, lpObject, lpCache->type, lpStruct->type);
	(*env)->SetShortField(env, lpObject, lpCache->subtype, lpStruct->subtype);
	(*env)->SetShortField(env, lpObject, lpCache->processing_flags, lpStruct->processing_flags);
	(*env)->SetIntField(env, lpObject, lpCache->emitter_rid, lpStruct->emitter.rid);
	(*env)->SetIntField(env, lpObject, lpCache->emitter_handle, lpStruct->emitter.handle);
	(*env)->SetIntField(env, lpObject, lpCache->collector_rid, lpStruct->collector.rid);
	(*env)->SetIntField(env, lpObject, lpCache->collector_handle, lpStruct->collector.handle);
	(*env)->SetShortField(env, lpObject, lpCache->input_group, lpStruct->input_group);
	(*env)->SetShortField(env, lpObject, lpCache->flags, lpStruct->flags);
	(*env)->SetIntField(env, lpObject, lpCache->timestamp, lpStruct->timestamp);
	(*env)->SetShortField(env, lpObject, lpCache->translation_x, lpStruct->translation.x);
	(*env)->SetShortField(env, lpObject, lpCache->translation_y, lpStruct->translation.y);
	(*env)->SetShortField(env, lpObject, lpCache->num_rects, lpStruct->num_rects);
	(*env)->SetShortField(env, lpObject, lpCache->data_len, lpStruct->data_len);
}

void cacheFontQueryInfoFids(JNIEnv *env, jobject lpObject, PFontQueryInfo_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->font = (*env)->GetFieldID(env, lpCache->clazz, "font", "[B");
	lpCache->desc = (*env)->GetFieldID(env, lpCache->clazz, "desc", "[B");
	lpCache->size = (*env)->GetFieldID(env, lpCache->clazz, "size", "S");
	lpCache->style = (*env)->GetFieldID(env, lpCache->clazz, "style", "S");
	lpCache->ascender = (*env)->GetFieldID(env, lpCache->clazz, "ascender", "S");
	lpCache->descender = (*env)->GetFieldID(env, lpCache->clazz, "descender", "S");
	lpCache->width = (*env)->GetFieldID(env, lpCache->clazz, "width", "S");
	lpCache->lochar = (*env)->GetFieldID(env, lpCache->clazz, "lochar", "I");
	lpCache->hichar = (*env)->GetFieldID(env, lpCache->clazz, "hichar", "I");
	lpCache->cached = 1;
}
	
void getFontQueryInfoFields(JNIEnv *env, jobject lpObject, FontQueryInfo *lpStruct, PFontQueryInfo_FID_CACHE lpCache)
{
	jbyteArray desc, font = (*env)->GetObjectField(env, lpObject, lpCache->font);
    if (font) {
        jbyte *font1 = (*env)->GetByteArrayElements(env, font, NULL);
        memcpy(lpStruct->font, font1, MAX_FONT_TAG);
        (*env)->ReleaseByteArrayElements(env, font, font1, JNI_ABORT);
	}
	desc = (*env)->GetObjectField(env, lpObject, lpCache->desc);
	if (desc) {
        jbyte *desc1 = (*env)->GetByteArrayElements(env, desc, NULL);
        memcpy(lpStruct->desc, desc1, MAX_DESC_LENGTH);
        (*env)->ReleaseByteArrayElements(env, desc, desc1, JNI_ABORT);
	}
	lpStruct->size = (*env)->GetShortField(env, lpObject, lpCache->size);
	lpStruct->style = (*env)->GetShortField(env, lpObject, lpCache->style);
	lpStruct->ascender = (*env)->GetShortField(env, lpObject, lpCache->ascender);
	lpStruct->descender = (*env)->GetShortField(env, lpObject, lpCache->descender);
	lpStruct->width = (*env)->GetShortField(env, lpObject, lpCache->width);
	lpStruct->lochar = (*env)->GetIntField(env, lpObject, lpCache->lochar);
	lpStruct->hichar = (*env)->GetIntField(env, lpObject, lpCache->hichar);
}

void setFontQueryInfoFields(JNIEnv *env, jobject lpObject, FontQueryInfo *lpStruct, PFontQueryInfo_FID_CACHE lpCache)
{
	jbyteArray desc, font = (*env)->GetObjectField(env, lpObject, lpCache->font);
    if (font) {
        jbyte *font1 = (*env)->GetByteArrayElements(env, font, NULL);
        memcpy(font1, lpStruct->font, MAX_FONT_TAG);
        (*env)->ReleaseByteArrayElements(env, font, font1, 0);
	}
	desc = (*env)->GetObjectField(env, lpObject, lpCache->desc);
	if (desc) {
        jbyte *desc1 = (*env)->GetByteArrayElements(env, desc, NULL);
        memcpy(desc1, lpStruct->desc, MAX_DESC_LENGTH);
        (*env)->ReleaseByteArrayElements(env, desc, desc1, 0);
	}
	(*env)->SetShortField(env, lpObject, lpCache->size, lpStruct->size);
	(*env)->SetShortField(env, lpObject, lpCache->style, lpStruct->style);
	(*env)->SetShortField(env, lpObject, lpCache->ascender, lpStruct->ascender);
	(*env)->SetShortField(env, lpObject, lpCache->descender, lpStruct->descender);
	(*env)->SetShortField(env, lpObject, lpCache->width, lpStruct->width);
	(*env)->SetIntField(env, lpObject, lpCache->lochar, lpStruct->lochar);
	(*env)->SetIntField(env, lpObject, lpCache->hichar, lpStruct->hichar);
}

void cachePhDim_tFids(JNIEnv *env, jobject lpObject, PPhDim_t_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->w = (*env)->GetFieldID(env, lpCache->clazz, "w", "S");
	lpCache->h = (*env)->GetFieldID(env, lpCache->clazz, "h", "S");
	lpCache->cached = 1;
}

void getPhDim_tFields(JNIEnv *env, jobject lpObject, PhDim_t *lpStruct, PPhDim_t_FID_CACHE lpCache)
{
	lpStruct->w = (*env)->GetShortField(env, lpObject, lpCache->w);
	lpStruct->h = (*env)->GetShortField(env, lpObject, lpCache->h);
}

void setPhDim_tFields(JNIEnv *env, jobject lpObject, PhDim_t *lpStruct, PPhDim_t_FID_CACHE lpCache)
{
	(*env)->SetShortField(env, lpObject, lpCache->w, lpStruct->w);
	(*env)->SetShortField(env, lpObject, lpCache->h, lpStruct->h);
}

void cachePhImage_tFids(JNIEnv *env, jobject lpObject, PPhImage_t_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->type = (*env)->GetFieldID(env, lpCache->clazz, "type", "I");
	lpCache->image_tag = (*env)->GetFieldID(env, lpCache->clazz, "image_tag", "I");
	lpCache->bpl = (*env)->GetFieldID(env, lpCache->clazz, "bpl", "I");
	lpCache->size_w = (*env)->GetFieldID(env, lpCache->clazz, "size_w", "S");
	lpCache->size_h = (*env)->GetFieldID(env, lpCache->clazz, "size_h", "S");
	lpCache->palette_tag = (*env)->GetFieldID(env, lpCache->clazz, "palette_tag", "I");
	lpCache->colors = (*env)->GetFieldID(env, lpCache->clazz, "colors", "I");
	lpCache->alpha = (*env)->GetFieldID(env, lpCache->clazz, "alpha", "I");
	lpCache->transparent = (*env)->GetFieldID(env, lpCache->clazz, "transparent", "I");
	lpCache->format = (*env)->GetFieldID(env, lpCache->clazz, "format", "B");
	lpCache->flags = (*env)->GetFieldID(env, lpCache->clazz, "flags", "B");
	lpCache->ghost_bpl = (*env)->GetFieldID(env, lpCache->clazz, "ghost_bpl", "B");
	lpCache->spare1 = (*env)->GetFieldID(env, lpCache->clazz, "spare1", "B");
	lpCache->ghost_bitmap = (*env)->GetFieldID(env, lpCache->clazz, "ghost_bitmap", "I");
	lpCache->mask_bpl = (*env)->GetFieldID(env, lpCache->clazz, "mask_bpl", "I");
	lpCache->mask_bm = (*env)->GetFieldID(env, lpCache->clazz, "mask_bm", "I");
	lpCache->palette = (*env)->GetFieldID(env, lpCache->clazz, "palette", "I");
	lpCache->image = (*env)->GetFieldID(env, lpCache->clazz, "image", "I");
	lpCache->cached = 1;
}

void getPhImage_tFields(JNIEnv *env, jobject lpObject, PhImage_t *lpStruct, PPhImage_t_FID_CACHE lpCache)
{
	lpStruct->type = (*env)->GetIntField(env, lpObject, lpCache->type);
	lpStruct->image_tag = (*env)->GetIntField(env, lpObject, lpCache->image_tag);
	lpStruct->bpl = (*env)->GetIntField(env, lpObject, lpCache->bpl);
	lpStruct->size.w = (*env)->GetShortField(env, lpObject, lpCache->size_w);
	lpStruct->size.h = (*env)->GetShortField(env, lpObject, lpCache->size_h);
	lpStruct->palette_tag = (*env)->GetIntField(env, lpObject, lpCache->palette_tag);
	lpStruct->colors = (*env)->GetIntField(env, lpObject, lpCache->colors);
	lpStruct->alpha = (PgAlpha_t *)(*env)->GetIntField(env, lpObject, lpCache->alpha);
	lpStruct->transparent = (PgColor_t)(*env)->GetIntField(env, lpObject, lpCache->transparent);
	lpStruct->format = (*env)->GetByteField(env, lpObject, lpCache->format);
	lpStruct->flags = (*env)->GetByteField(env, lpObject, lpCache->flags);
	lpStruct->ghost_bpl = (*env)->GetByteField(env, lpObject, lpCache->ghost_bpl);
	lpStruct->spare1 = (*env)->GetByteField(env, lpObject, lpCache->spare1);
	lpStruct->ghost_bitmap = (char *)(*env)->GetIntField(env, lpObject, lpCache->ghost_bitmap);
	lpStruct->mask_bpl = (*env)->GetIntField(env, lpObject, lpCache->mask_bpl);
	lpStruct->mask_bm = (char *)(*env)->GetIntField(env, lpObject, lpCache->mask_bm);
	lpStruct->palette = (PgColor_t *)(*env)->GetIntField(env, lpObject, lpCache->palette);
	lpStruct->image = (char *)(*env)->GetIntField(env, lpObject, lpCache->image);
}

void setPhImage_tFields(JNIEnv *env, jobject lpObject, PhImage_t *lpStruct, PPhImage_t_FID_CACHE lpCache)
{
	(*env)->SetIntField(env, lpObject, lpCache->type, lpStruct->type);
	(*env)->SetIntField(env, lpObject, lpCache->image_tag, lpStruct->image_tag);
	(*env)->SetIntField(env, lpObject, lpCache->bpl, lpStruct->bpl);
	(*env)->SetShortField(env, lpObject, lpCache->size_w, lpStruct->size.w);
	(*env)->SetShortField(env, lpObject, lpCache->size_h, lpStruct->size.h);
	(*env)->SetIntField(env, lpObject, lpCache->palette_tag, lpStruct->palette_tag);
	(*env)->SetIntField(env, lpObject, lpCache->colors, lpStruct->colors);
	(*env)->SetIntField(env, lpObject, lpCache->alpha, (jint)lpStruct->alpha);
	(*env)->SetIntField(env, lpObject, lpCache->transparent, (jint)lpStruct->transparent);
	(*env)->SetByteField(env, lpObject, lpCache->format, lpStruct->format);
	(*env)->SetByteField(env, lpObject, lpCache->flags, lpStruct->flags);
	(*env)->SetByteField(env, lpObject, lpCache->ghost_bpl, lpStruct->ghost_bpl);
	(*env)->SetByteField(env, lpObject, lpCache->spare1, lpStruct->spare1);
	(*env)->SetIntField(env, lpObject, lpCache->ghost_bitmap, (jint)lpStruct->ghost_bitmap);
	(*env)->SetIntField(env, lpObject, lpCache->mask_bpl, lpStruct->mask_bpl);
	(*env)->SetIntField(env, lpObject, lpCache->mask_bm, (jint)lpStruct->mask_bm);
	(*env)->SetIntField(env, lpObject, lpCache->palette, (jint)lpStruct->palette);
	(*env)->SetIntField(env, lpObject, lpCache->image, (jint)lpStruct->image);
}


void cachePhPointerEvent_tFids(JNIEnv *env, jobject lpObject, PPhPointerEvent_t_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->pos_x = (*env)->GetFieldID(env, lpCache->clazz, "pos_x", "S");
	lpCache->pos_y = (*env)->GetFieldID(env, lpCache->clazz, "pos_y", "S");
	lpCache->buttons = (*env)->GetFieldID(env, lpCache->clazz, "buttons", "S");
	lpCache->button_state = (*env)->GetFieldID(env, lpCache->clazz, "button_state", "S");
	lpCache->click_count = (*env)->GetFieldID(env, lpCache->clazz, "click_count", "B");
	lpCache->flags = (*env)->GetFieldID(env, lpCache->clazz, "flags", "B");
	lpCache->z = (*env)->GetFieldID(env, lpCache->clazz, "z", "S");
	lpCache->key_mods = (*env)->GetFieldID(env, lpCache->clazz, "key_mods", "I");
	lpCache->zero = (*env)->GetFieldID(env, lpCache->clazz, "zero", "I");
	lpCache->cached = 1;
}

void getPhPointerEvent_tFields(JNIEnv *env, jobject lpObject, PhPointerEvent_t *lpStruct, PPhPointerEvent_t_FID_CACHE lpCache)
{
	lpStruct->pos.x = (*env)->GetShortField(env, lpObject, lpCache->pos_x);
	lpStruct->pos.y = (*env)->GetShortField(env, lpObject, lpCache->pos_y);
	lpStruct->buttons = (*env)->GetShortField(env, lpObject, lpCache->buttons);
	lpStruct->button_state = (*env)->GetShortField(env, lpObject, lpCache->button_state);
	lpStruct->click_count = (*env)->GetByteField(env, lpObject, lpCache->click_count);
	lpStruct->flags = (*env)->GetByteField(env, lpObject, lpCache->flags);
	lpStruct->z = (*env)->GetShortField(env, lpObject, lpCache->z);
	lpStruct->key_mods = (*env)->GetIntField(env, lpObject, lpCache->key_mods);
	lpStruct->zero = (*env)->GetIntField(env, lpObject, lpCache->zero);
}

void setPhPointerEvent_tFields(JNIEnv *env, jobject lpObject, PhPointerEvent_t *lpStruct, PPhPointerEvent_t_FID_CACHE lpCache)
{
	(*env)->SetShortField(env, lpObject, lpCache->pos_x, lpStruct->pos.x);
	(*env)->SetShortField(env, lpObject, lpCache->pos_y, lpStruct->pos.y);
	(*env)->SetShortField(env, lpObject, lpCache->buttons, lpStruct->buttons);
	(*env)->SetShortField(env, lpObject, lpCache->button_state, lpStruct->button_state);
	(*env)->SetByteField(env, lpObject, lpCache->click_count, lpStruct->click_count);
	(*env)->SetByteField(env, lpObject, lpCache->flags, lpStruct->flags);
	(*env)->SetShortField(env, lpObject, lpCache->z, lpStruct->z);
	(*env)->SetIntField(env, lpObject, lpCache->key_mods, lpStruct->key_mods);
	(*env)->SetIntField(env, lpObject, lpCache->zero, lpStruct->zero);
}

void cachePhKeyEvent_tFids(JNIEnv *env, jobject lpObject, PPhKeyEvent_t_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->key_flags = (*env)->GetFieldID(env, lpCache->clazz, "key_flags", "I");
	lpCache->key_mods = (*env)->GetFieldID(env, lpCache->clazz, "key_mods", "I");
	lpCache->key_sym = (*env)->GetFieldID(env, lpCache->clazz, "key_sym", "I");
	lpCache->key_cap = (*env)->GetFieldID(env, lpCache->clazz, "key_cap", "I");
	lpCache->key_scan = (*env)->GetFieldID(env, lpCache->clazz, "key_scan", "S");
	lpCache->key_zero = (*env)->GetFieldID(env, lpCache->clazz, "key_zero", "S");
	lpCache->pos_x = (*env)->GetFieldID(env, lpCache->clazz, "pos_x", "S");
	lpCache->pos_y = (*env)->GetFieldID(env, lpCache->clazz, "pos_y", "S");
	lpCache->button_state = (*env)->GetFieldID(env, lpCache->clazz, "button_state", "S");
	lpCache->cached = 1;
}

void getPhKeyEvent_tFields(JNIEnv *env, jobject lpObject, PhKeyEvent_t *lpStruct, PPhKeyEvent_t_FID_CACHE lpCache)
{
	lpStruct->key_flags = (*env)->GetIntField(env, lpObject, lpCache->key_flags);
	lpStruct->key_mods = (*env)->GetIntField(env, lpObject, lpCache->key_mods);
	lpStruct->key_sym = (*env)->GetIntField(env, lpObject, lpCache->key_sym);
	lpStruct->key_cap = (*env)->GetIntField(env, lpObject, lpCache->key_cap);
	lpStruct->key_scan = (*env)->GetShortField(env, lpObject, lpCache->key_scan);
#if _NTO_VERSION+0 >= 610
	lpStruct->key_zero = (*env)->GetShortField(env, lpObject, lpCache->key_zero);
#endif
	lpStruct->pos.x = (*env)->GetShortField(env, lpObject, lpCache->pos_x);
	lpStruct->pos.y = (*env)->GetShortField(env, lpObject, lpCache->pos_y);
	lpStruct->button_state = (*env)->GetShortField(env, lpObject, lpCache->button_state);
}

void setPhKeyEvent_tFields(JNIEnv *env, jobject lpObject, PhKeyEvent_t *lpStruct, PPhKeyEvent_t_FID_CACHE lpCache)
{
	(*env)->SetIntField(env, lpObject, lpCache->key_flags, lpStruct->key_flags);
	(*env)->SetIntField(env, lpObject, lpCache->key_mods, lpStruct->key_mods);
	(*env)->SetIntField(env, lpObject, lpCache->key_sym, lpStruct->key_sym);
	(*env)->SetIntField(env, lpObject, lpCache->key_cap, lpStruct->key_cap);
	(*env)->SetShortField(env, lpObject, lpCache->key_scan, lpStruct->key_scan);
#if _NTO_VERSION+0 >= 610
	(*env)->SetShortField(env, lpObject, lpCache->key_zero, lpStruct->key_zero);
#endif
	(*env)->SetShortField(env, lpObject, lpCache->pos_x, lpStruct->pos.x);
	(*env)->SetShortField(env, lpObject, lpCache->pos_y, lpStruct->pos.y);
	(*env)->SetShortField(env, lpObject, lpCache->button_state, lpStruct->button_state);
}

void cachePtScrollbarCallback_tFids(JNIEnv *env, jobject lpObject, PPtScrollbarCallback_t_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->action = (*env)->GetFieldID(env, lpCache->clazz, "action", "I");
	lpCache->position = (*env)->GetFieldID(env, lpCache->clazz, "position", "I");
	lpCache->cached = 1;
}

void getPtScrollbarCallback_tFields(JNIEnv *env, jobject lpObject, PtScrollbarCallback_t *lpStruct, PPtScrollbarCallback_t_FID_CACHE lpCache)
{
	lpStruct->action = (*env)->GetIntField(env, lpObject, lpCache->action);
	lpStruct->position = (*env)->GetIntField(env, lpObject, lpCache->position);
}

void setPtScrollbarCallback_tFields(JNIEnv *env, jobject lpObject, PtScrollbarCallback_t *lpStruct, PPtScrollbarCallback_t_FID_CACHE lpCache)
{
	(*env)->SetIntField(env, lpObject, lpCache->action, lpStruct->action);
	(*env)->SetIntField(env, lpObject, lpCache->position, lpStruct->position);
}

void cachePhCursorInfo_tFids(JNIEnv *env, jobject lpObject, PPhCursorInfo_t_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->pos_x = (*env)->GetFieldID(env, lpCache->clazz, "pos_x", "S");
	lpCache->pos_y = (*env)->GetFieldID(env, lpCache->clazz, "pos_y", "S");
	lpCache->region = (*env)->GetFieldID(env, lpCache->clazz, "region", "I");
	lpCache->ig_region = (*env)->GetFieldID(env, lpCache->clazz, "ig_region", "I");
	lpCache->color = (*env)->GetFieldID(env, lpCache->clazz, "color", "I");
	lpCache->last_press_x = (*env)->GetFieldID(env, lpCache->clazz, "last_press_x", "S");
	lpCache->last_press_y = (*env)->GetFieldID(env, lpCache->clazz, "last_press_y", "S");
	lpCache->msec = (*env)->GetFieldID(env, lpCache->clazz, "msec", "I");
	lpCache->steady_x = (*env)->GetFieldID(env, lpCache->clazz, "steady_x", "I");
	lpCache->steady_y = (*env)->GetFieldID(env, lpCache->clazz, "steady_y", "I");
	lpCache->dragger = (*env)->GetFieldID(env, lpCache->clazz, "dragger", "I");
	lpCache->drag_boundary_xUL = (*env)->GetFieldID(env, lpCache->clazz, "drag_boundary_xUL", "S");
	lpCache->drag_boundary_yUL = (*env)->GetFieldID(env, lpCache->clazz, "drag_boundary_yUL", "S");
	lpCache->drag_boundary_xLR = (*env)->GetFieldID(env, lpCache->clazz, "drag_boundary_xLR", "S");
	lpCache->drag_boundary_yLR = (*env)->GetFieldID(env, lpCache->clazz, "drag_boundary_yLR", "S");
	lpCache->phantom_rid = (*env)->GetFieldID(env, lpCache->clazz, "phantom_rid", "I");
	lpCache->type = (*env)->GetFieldID(env, lpCache->clazz, "type", "S");
	lpCache->ig = (*env)->GetFieldID(env, lpCache->clazz, "ig", "S");
	lpCache->button_state = (*env)->GetFieldID(env, lpCache->clazz, "button_state", "S");
	lpCache->click_count = (*env)->GetFieldID(env, lpCache->clazz, "click_count", "B");
	lpCache->zero10 = (*env)->GetFieldID(env, lpCache->clazz, "zero10", "B");
	lpCache->zero11 = (*env)->GetFieldID(env, lpCache->clazz, "zero11", "B");
	lpCache->zero12 = (*env)->GetFieldID(env, lpCache->clazz, "zero12", "B");
	lpCache->key_mods = (*env)->GetFieldID(env, lpCache->clazz, "key_mods", "I");
	lpCache->zero2 = (*env)->GetFieldID(env, lpCache->clazz, "zero2", "I");
	lpCache->cached = 1;
}

void getPhCursorInfo_tFields(JNIEnv *env, jobject lpObject, PhCursorInfo_t *lpStruct, PPhCursorInfo_t_FID_CACHE lpCache)
{
	lpStruct->pos.x = (*env)->GetShortField(env, lpObject, lpCache->pos_x);
	lpStruct->pos.y = (*env)->GetShortField(env, lpObject, lpCache->pos_y);
	lpStruct->region = (*env)->GetIntField(env, lpObject, lpCache->region);
	lpStruct->ig_region = (*env)->GetIntField(env, lpObject, lpCache->ig_region);
	lpStruct->color = (*env)->GetIntField(env, lpObject, lpCache->color);
	lpStruct->last_press.x = (*env)->GetShortField(env, lpObject, lpCache->last_press_x);
	lpStruct->last_press.y = (*env)->GetShortField(env, lpObject, lpCache->last_press_y);
	lpStruct->msec = (*env)->GetIntField(env, lpObject, lpCache->msec);
	lpStruct->steady.x = (*env)->GetIntField(env, lpObject, lpCache->steady_x);
	lpStruct->steady.y = (*env)->GetIntField(env, lpObject, lpCache->steady_y);
	lpStruct->dragger = (*env)->GetIntField(env, lpObject, lpCache->dragger);
	lpStruct->drag_boundary.ul.x = (*env)->GetShortField(env, lpObject, lpCache->drag_boundary_xUL);
	lpStruct->drag_boundary.ul.y = (*env)->GetShortField(env, lpObject, lpCache->drag_boundary_yUL);
	lpStruct->drag_boundary.lr.x = (*env)->GetShortField(env, lpObject, lpCache->drag_boundary_xLR);
	lpStruct->drag_boundary.lr.y = (*env)->GetShortField(env, lpObject, lpCache->drag_boundary_yLR);
	lpStruct->phantom_rid = (*env)->GetIntField(env, lpObject, lpCache->phantom_rid);
	lpStruct->type = (*env)->GetShortField(env, lpObject, lpCache->type);
	lpStruct->ig = (*env)->GetShortField(env, lpObject, lpCache->ig);
	lpStruct->button_state = (*env)->GetShortField(env, lpObject, lpCache->button_state);
	lpStruct->click_count = (*env)->GetByteField(env, lpObject, lpCache->click_count);
//	lpStruct->zero10 = (*env)->GetByteField(env, lpObject, lpCache->zero10);
//	lpStruct->zero11 = (*env)->GetByteField(env, lpObject, lpCache->zero11);
//	lpStruct->zero12 = (*env)->GetByteField(env, lpObject, lpCache->zero12);
	lpStruct->key_mods = (*env)->GetIntField(env, lpObject, lpCache->key_mods);
	lpStruct->zero2 = (*env)->GetIntField(env, lpObject, lpCache->zero2);
}

void setPhCursorInfo_tFields(JNIEnv *env, jobject lpObject, PhCursorInfo_t *lpStruct, PPhCursorInfo_t_FID_CACHE lpCache)
{
	(*env)->SetShortField(env, lpObject, lpCache->pos_x, lpStruct->pos.x);
	(*env)->SetShortField(env, lpObject, lpCache->pos_y, lpStruct->pos.y);
	(*env)->SetIntField(env, lpObject, lpCache->region, lpStruct->region);
	(*env)->SetIntField(env, lpObject, lpCache->ig_region, lpStruct->ig_region);
	(*env)->SetIntField(env, lpObject, lpCache->color, lpStruct->color);
	(*env)->SetShortField(env, lpObject, lpCache->last_press_x, lpStruct->last_press.x);
	(*env)->SetShortField(env, lpObject, lpCache->last_press_y, lpStruct->last_press.y);
	(*env)->SetIntField(env, lpObject, lpCache->msec, lpStruct->msec);
	(*env)->SetIntField(env, lpObject, lpCache->steady_x, lpStruct->steady.x);
	(*env)->SetIntField(env, lpObject, lpCache->steady_y, lpStruct->steady.y);
	(*env)->SetIntField(env, lpObject, lpCache->dragger, lpStruct->dragger);
	(*env)->SetShortField(env, lpObject, lpCache->drag_boundary_xUL, lpStruct->drag_boundary.ul.x);
	(*env)->SetShortField(env, lpObject, lpCache->drag_boundary_yUL, lpStruct->drag_boundary.ul.y);
	(*env)->SetShortField(env, lpObject, lpCache->drag_boundary_xLR, lpStruct->drag_boundary.lr.x);
	(*env)->SetShortField(env, lpObject, lpCache->drag_boundary_yLR, lpStruct->drag_boundary.lr.y);
	(*env)->SetIntField(env, lpObject, lpCache->phantom_rid, lpStruct->phantom_rid);
	(*env)->SetShortField(env, lpObject, lpCache->type, lpStruct->type);
	(*env)->SetShortField(env, lpObject, lpCache->ig, lpStruct->ig);
	(*env)->SetShortField(env, lpObject, lpCache->button_state, lpStruct->button_state);
	(*env)->SetByteField(env, lpObject, lpCache->click_count, lpStruct->click_count);
//	(*env)->SetByteField(env, lpObject, lpCache->zero10, lpStruct->zero10);
//	(*env)->SetByteField(env, lpObject, lpCache->zero11, lpStruct->zero11);
//	(*env)->SetByteField(env, lpObject, lpCache->zero12, lpStruct->zero12);
	(*env)->SetIntField(env, lpObject, lpCache->key_mods, lpStruct->key_mods);
	(*env)->SetIntField(env, lpObject, lpCache->zero2, lpStruct->zero2);
}

void cacheFontDetailsFids(JNIEnv *env, jobject lpObject, PFontDetails_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->desc = (*env)->GetFieldID(env, lpCache->clazz, "desc", "[B");
	lpCache->stem = (*env)->GetFieldID(env, lpCache->clazz, "stem", "[B");
	lpCache->losize = (*env)->GetFieldID(env, lpCache->clazz, "losize", "S");
	lpCache->hisize = (*env)->GetFieldID(env, lpCache->clazz, "hisize", "S");
	lpCache->flags = (*env)->GetFieldID(env, lpCache->clazz, "flags", "S");
	lpCache->cached = 1;
}

void getFontDetailsFields(JNIEnv *env, jobject lpObject, FontDetails *lpStruct, PFontDetails_FID_CACHE lpCache)
{
	jbyteArray desc, stem = (*env)->GetObjectField(env, lpObject, lpCache->stem);
    if (stem) {
        jbyte *stem1 = (*env)->GetByteArrayElements(env, stem, NULL);
        memcpy(lpStruct->stem, stem1, MAX_FONT_TAG);
        (*env)->ReleaseByteArrayElements(env, stem, stem1, JNI_ABORT);
	}
	desc = (*env)->GetObjectField(env, lpObject, lpCache->desc);
	if (desc) {
        jbyte *desc1 = (*env)->GetByteArrayElements(env, desc, NULL);
        memcpy(lpStruct->desc, desc1, MAX_DESC_LENGTH);
        (*env)->ReleaseByteArrayElements(env, desc, desc1, JNI_ABORT);
	}
	lpStruct->losize = (*env)->GetShortField(env, lpObject, lpCache->losize);
	lpStruct->hisize = (*env)->GetShortField(env, lpObject, lpCache->hisize);
	lpStruct->flags = (*env)->GetShortField(env, lpObject, lpCache->flags);
}

void setFontDetailsFields(JNIEnv *env, jobject lpObject, FontDetails *lpStruct, PFontDetails_FID_CACHE lpCache)
{
	jbyteArray desc, stem = (*env)->GetObjectField(env, lpObject, lpCache->stem);
    if (stem) {
        jbyte *stem1 = (*env)->GetByteArrayElements(env, stem, NULL);
        memcpy(stem1, lpStruct->stem, MAX_FONT_TAG);
        (*env)->ReleaseByteArrayElements(env, stem, stem1, 0);
	}
	desc = (*env)->GetObjectField(env, lpObject, lpCache->desc);
	if (desc) {
        jbyte *desc1 = (*env)->GetByteArrayElements(env, desc, NULL);
        memcpy(desc1, lpStruct->desc, MAX_DESC_LENGTH);
        (*env)->ReleaseByteArrayElements(env, desc, desc1, 0);
	}
	(*env)->SetShortField(env, lpObject, lpCache->losize, lpStruct->losize);
	(*env)->SetShortField(env, lpObject, lpCache->hisize, lpStruct->hisize);
	(*env)->SetShortField(env, lpObject, lpCache->flags, lpStruct->flags);
}

void cachePhArea_tFids(JNIEnv *env, jobject lpObject, PPhArea_t_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->pos_x = (*env)->GetFieldID(env, lpCache->clazz, "pos_x", "S");
	lpCache->pos_y = (*env)->GetFieldID(env, lpCache->clazz, "pos_y", "S");
	lpCache->size_w = (*env)->GetFieldID(env, lpCache->clazz, "size_w", "S");
	lpCache->size_h = (*env)->GetFieldID(env, lpCache->clazz, "size_h", "S");
	lpCache->cached = 1;
}

void getPhArea_tFields(JNIEnv *env, jobject lpObject, PhArea_t *lpStruct, PPhArea_t_FID_CACHE lpCache)
{
	lpStruct->pos.x = (*env)->GetShortField(env, lpObject, lpCache->pos_x);
	lpStruct->pos.y = (*env)->GetShortField(env, lpObject, lpCache->pos_y);
	lpStruct->size.w = (*env)->GetShortField(env, lpObject, lpCache->size_w);
	lpStruct->size.h = (*env)->GetShortField(env, lpObject, lpCache->size_h);
}

void setPhArea_tFields(JNIEnv *env, jobject lpObject, PhArea_t *lpStruct, PPhArea_t_FID_CACHE lpCache)
{
	(*env)->SetShortField(env, lpObject, lpCache->pos_x, lpStruct->pos.x);
	(*env)->SetShortField(env, lpObject, lpCache->pos_y, lpStruct->pos.y);
	(*env)->SetShortField(env, lpObject, lpCache->size_w, lpStruct->size.w);
	(*env)->SetShortField(env, lpObject, lpCache->size_h, lpStruct->size.h);
}

void cachePtFileSelectionInfo_tFids(JNIEnv *env, jobject lpObject, PPtFileSelectionInfo_t_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->ret = (*env)->GetFieldID(env, lpCache->clazz, "ret", "S");
	lpCache->path = (*env)->GetFieldID(env, lpCache->clazz, "path", "[B");
	lpCache->dim = (*env)->GetFieldID(env, lpCache->clazz, "dim", "Lorg/eclipse/swt/internal/photon/PhDim_t;");
	lpCache->pos = (*env)->GetFieldID(env, lpCache->clazz, "pos", "Lorg/eclipse/swt/internal/photon/PhPoint_t;");
	lpCache->format = (*env)->GetFieldID(env, lpCache->clazz, "format", "[B");
	lpCache->fspec = (*env)->GetFieldID(env, lpCache->clazz, "fspec", "[B");
	lpCache->user_data = (*env)->GetFieldID(env, lpCache->clazz, "user_data", "I");
	lpCache->confirm_display = (*env)->GetFieldID(env, lpCache->clazz, "confirm_display", "I");
	lpCache->confirm_selection = (*env)->GetFieldID(env, lpCache->clazz, "confirm_selection", "I");
	lpCache->new_directory = (*env)->GetFieldID(env, lpCache->clazz, "new_directory", "I");
	lpCache->btn1 = (*env)->GetFieldID(env, lpCache->clazz, "btn1", "I");
	lpCache->btn2 = (*env)->GetFieldID(env, lpCache->clazz, "btn2", "I");
	lpCache->num_args = (*env)->GetFieldID(env, lpCache->clazz, "num_args", "I");
	lpCache->args = (*env)->GetFieldID(env, lpCache->clazz, "args", "I");
	lpCache->spare = (*env)->GetFieldID(env, lpCache->clazz, "spare", "[I");
	lpCache->cached = 1;
}

void getPtFileSelectionInfo_tFields(JNIEnv *env, jobject lpObject, PtFileSelectionInfo_t *lpStruct, PPtFileSelectionInfo_t_FID_CACHE lpCache)
{
	DECL_GLOB(pGlob)
	
	lpStruct->ret = (*env)->GetShortField(env, lpObject, lpCache->ret);
	{
	jbyteArray path = (*env)->GetObjectField(env, lpObject, lpCache->path);
    if (path) {
        jbyte *path1 = (*env)->GetByteArrayElements(env, path, NULL);
        memcpy(lpStruct->path, path1, sizeof (lpStruct->path));
        (*env)->ReleaseByteArrayElements(env, path, path1, 0);
	}
	}
	{
	jobject dim = (*env)->GetObjectField(env, lpObject, lpCache->dim);
    if (dim) {
        cachePhDim_tFids(env, dim, &PGLOB(PhDim_tFc));
        getPhDim_tFields(env, dim, &lpStruct->dim, &PGLOB(PhDim_tFc));
    }
    }
	{
	jobject pos = (*env)->GetObjectField(env, lpObject, lpCache->pos);
    if (pos) {
        cachePhPoint_tFids(env, pos, &PGLOB(PhPoint_tFc));
        getPhPoint_tFields(env, pos, &lpStruct->pos, &PGLOB(PhPoint_tFc));
    }
    }
	{
	jbyteArray format = (*env)->GetObjectField(env, lpObject, lpCache->format);
    if (format) {
        jbyte *format1 = (*env)->GetByteArrayElements(env, format, NULL);
        memcpy(lpStruct->format, format1, sizeof (lpStruct->format));
        (*env)->ReleaseByteArrayElements(env, format, format1, 0);
	}
	}
	{
	jbyteArray fspec = (*env)->GetObjectField(env, lpObject, lpCache->fspec);
    if (fspec) {
        jbyte *fspec1 = (*env)->GetByteArrayElements(env, fspec, NULL);
        memcpy(lpStruct->fspec, fspec1, sizeof (lpStruct->fspec));
        (*env)->ReleaseByteArrayElements(env, fspec, fspec1, 0);
	}
	}
	lpStruct->user_data = (void *) (*env)->GetIntField(env, lpObject, lpCache->user_data);
	lpStruct->confirm_display = (void *) (*env)->GetIntField(env, lpObject, lpCache->confirm_display);
	lpStruct->confirm_selection = (void *) (*env)->GetIntField(env, lpObject, lpCache->confirm_selection);
	lpStruct->new_directory = (void *) (*env)->GetIntField(env, lpObject, lpCache->new_directory);
	lpStruct->btn1 = (char *) (*env)->GetIntField(env, lpObject, lpCache->btn1);
	lpStruct->btn2 = (char *) (*env)->GetIntField(env, lpObject, lpCache->btn2);
	lpStruct->num_args = (*env)->GetIntField(env, lpObject, lpCache->num_args);
	lpStruct->args = (void *)(*env)->GetIntField(env, lpObject, lpCache->args);
	{
	jintArray spare = (*env)->GetObjectField(env, lpObject, lpCache->spare);
    if (spare) {
        jint *spare1 = (*env)->GetIntArrayElements(env, spare, NULL);
        memcpy(lpStruct->spare, spare1, sizeof (lpStruct->spare));
        (*env)->ReleaseIntArrayElements(env, spare, spare1, 0);
	}
	}
}

void setPtFileSelectionInfo_tFields(JNIEnv *env, jobject lpObject, PtFileSelectionInfo_t *lpStruct, PPtFileSelectionInfo_t_FID_CACHE lpCache)
{
	DECL_GLOB(pGlob)
	
	(*env)->SetShortField(env, lpObject, lpCache->ret, lpStruct->ret);
	{
	jbyteArray path = (*env)->GetObjectField(env, lpObject, lpCache->path);
    if (path) {
        jbyte *path1 = (*env)->GetByteArrayElements(env, path, NULL);
        memcpy(path1, lpStruct->path, sizeof (lpStruct->path));
        (*env)->ReleaseByteArrayElements(env, path, path1, 0);
	}
	}
	{
	jobject dim = (*env)->GetObjectField(env, lpObject, lpCache->dim);
    if (dim) { 
        cachePhDim_tFids(env, dim, &PGLOB(PhDim_tFc));
        setPhDim_tFields(env, dim, &lpStruct->dim, &PGLOB(PhDim_tFc));
    }	
	}
	{
	jobject pos = (*env)->GetObjectField(env, lpObject, lpCache->pos);
    if (pos) { 
        cachePhPoint_tFids(env, pos, &PGLOB(PhPoint_tFc));
        setPhPoint_tFields(env, pos, &lpStruct->pos, &PGLOB(PhPoint_tFc));
    }	
	}	
	{
	jbyteArray format = (*env)->GetObjectField(env, lpObject, lpCache->format);
    if (format) {
        jbyte *format1 = (*env)->GetByteArrayElements(env, format, NULL);
        memcpy(format1, lpStruct->format, sizeof (lpStruct->format));
        (*env)->ReleaseByteArrayElements(env, format, format1, 0);
	}
	}
	{
	jbyteArray fspec = (*env)->GetObjectField(env, lpObject, lpCache->fspec);
    if (fspec) {
        jbyte *fspec1 = (*env)->GetByteArrayElements(env, fspec, NULL);
        memcpy(fspec1, lpStruct->fspec, sizeof (lpStruct->fspec));
        (*env)->ReleaseByteArrayElements(env, fspec, fspec1, 0);
	}
	}
	(*env)->SetIntField(env, lpObject, lpCache->user_data, (int) lpStruct->user_data);
	(*env)->SetIntField(env, lpObject, lpCache->confirm_display, (int) lpStruct->confirm_display);
	(*env)->SetIntField(env, lpObject, lpCache->confirm_selection, (int) lpStruct->confirm_selection);
	(*env)->SetIntField(env, lpObject, lpCache->new_directory, (int) lpStruct->new_directory);
	(*env)->SetIntField(env, lpObject, lpCache->btn1, (int) lpStruct->btn1);
	(*env)->SetIntField(env, lpObject, lpCache->btn2, (int) lpStruct->btn2);
	(*env)->SetIntField(env, lpObject, lpCache->num_args, lpStruct->num_args);
	(*env)->SetIntField(env, lpObject, lpCache->args, (int) lpStruct->args);
	{
	jintArray spare = (*env)->GetObjectField(env, lpObject, lpCache->spare);
    if (spare) {
        jint *spare1 = (*env)->GetIntArrayElements(env, spare, NULL);
        memcpy(spare1, lpStruct->spare, sizeof (lpStruct->spare));
        (*env)->ReleaseIntArrayElements(env, spare, spare1, 0);
	}
	}
}

void cachePgAlpha_tFids(JNIEnv *env, jobject lpObject, PPgAlpha_t_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->alpha_op = (*env)->GetFieldID(env, lpCache->clazz, "alpha_op", "I");
	lpCache->src_alpha_map_dim_w = (*env)->GetFieldID(env, lpCache->clazz, "src_alpha_map_dim_w", "S");
	lpCache->src_alpha_map_dim_h = (*env)->GetFieldID(env, lpCache->clazz, "src_alpha_map_dim_h", "S");
	lpCache->src_alpha_map_bpl = (*env)->GetFieldID(env, lpCache->clazz, "src_alpha_map_bpl", "S");
	lpCache->src_alpha_map_bpp = (*env)->GetFieldID(env, lpCache->clazz, "src_alpha_map_bpp", "S");
	lpCache->src_alpha_map_map = (*env)->GetFieldID(env, lpCache->clazz, "src_alpha_map_map", "I");
	lpCache->src_global_alpha = (*env)->GetFieldID(env, lpCache->clazz, "src_global_alpha", "B");
	lpCache->dest_global_alpha = (*env)->GetFieldID(env, lpCache->clazz, "dest_global_alpha", "B");
	lpCache->cached = 1;
}

void getPgAlpha_tFields(JNIEnv *env, jobject lpObject, PgAlpha_t *lpStruct, PPgAlpha_t_FID_CACHE lpCache)
{
	lpStruct->alpha_op = (*env)->GetIntField(env, lpObject, lpCache->alpha_op);
	lpStruct->src_alpha_map.dim.w = (*env)->GetShortField(env, lpObject, lpCache->src_alpha_map_dim_w);
	lpStruct->src_alpha_map.dim.h = (*env)->GetShortField(env, lpObject, lpCache->src_alpha_map_dim_h);
	lpStruct->src_alpha_map.bpl = (*env)->GetShortField(env, lpObject, lpCache->src_alpha_map_bpl);
	lpStruct->src_alpha_map.bpp = (*env)->GetShortField(env, lpObject, lpCache->src_alpha_map_bpp);
	lpStruct->src_alpha_map.map = (char *)(*env)->GetIntField(env, lpObject, lpCache->src_alpha_map_map);
	lpStruct->src_global_alpha = (*env)->GetByteField(env, lpObject, lpCache->src_global_alpha);
	lpStruct->dest_global_alpha = (*env)->GetByteField(env, lpObject, lpCache->dest_global_alpha);
}

void setPgAlpha_tFields(JNIEnv *env, jobject lpObject, PgAlpha_t *lpStruct, PPgAlpha_t_FID_CACHE lpCache)
{
	(*env)->SetIntField(env, lpObject, lpCache->alpha_op, lpStruct->alpha_op);
	(*env)->SetShortField(env, lpObject, lpCache->src_alpha_map_dim_w, lpStruct->src_alpha_map.dim.w);
	(*env)->SetShortField(env, lpObject, lpCache->src_alpha_map_dim_h, lpStruct->src_alpha_map.dim.h);
	(*env)->SetShortField(env, lpObject, lpCache->src_alpha_map_bpl, lpStruct->src_alpha_map.bpl);
	(*env)->SetShortField(env, lpObject, lpCache->src_alpha_map_bpp, lpStruct->src_alpha_map.bpp);
	(*env)->SetIntField(env, lpObject, lpCache->src_alpha_map_map, (jint)lpStruct->src_alpha_map.map);
	(*env)->SetByteField(env, lpObject, lpCache->src_global_alpha, lpStruct->src_global_alpha);
	(*env)->SetByteField(env, lpObject, lpCache->dest_global_alpha, lpStruct->dest_global_alpha);
}

void cachePtTextCallback_tFids(JNIEnv *env, jobject lpObject, PPtTextCallback_t_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->start_pos = (*env)->GetFieldID(env, lpCache->clazz, "start_pos", "I");
	lpCache->end_pos = (*env)->GetFieldID(env, lpCache->clazz, "end_pos", "I");
	lpCache->cur_insert = (*env)->GetFieldID(env, lpCache->clazz, "cur_insert", "I");
	lpCache->new_insert = (*env)->GetFieldID(env, lpCache->clazz, "new_insert", "I");
	lpCache->length = (*env)->GetFieldID(env, lpCache->clazz, "length", "I");
	lpCache->reserved = (*env)->GetFieldID(env, lpCache->clazz, "reserved", "S");
	lpCache->text = (*env)->GetFieldID(env, lpCache->clazz, "text", "I");
	lpCache->doit = (*env)->GetFieldID(env, lpCache->clazz, "doit", "I");
	lpCache->cached = 1;
}
	
void getPtTextCallback_tFields(JNIEnv *env, jobject lpObject, PtTextCallback_t *lpStruct, PPtTextCallback_t_FID_CACHE lpCache)
{
	lpStruct->start_pos = (*env)->GetIntField(env, lpObject, lpCache->start_pos);
	lpStruct->end_pos = (*env)->GetIntField(env, lpObject, lpCache->end_pos);
	lpStruct->cur_insert = (*env)->GetIntField(env, lpObject, lpCache->cur_insert);
	lpStruct->new_insert = (*env)->GetIntField(env, lpObject, lpCache->new_insert);
	lpStruct->length = (*env)->GetIntField(env, lpObject, lpCache->length);
	lpStruct->reserved = (*env)->GetShortField(env, lpObject, lpCache->reserved);
	lpStruct->text = (char *)(*env)->GetIntField(env, lpObject, lpCache->text);
	lpStruct->doit = (*env)->GetIntField(env, lpObject, lpCache->doit);
}

void setPtTextCallback_tFields(JNIEnv *env, jobject lpObject, PtTextCallback_t *lpStruct, PPtTextCallback_t_FID_CACHE lpCache)
{
	(*env)->SetIntField(env, lpObject, lpCache->start_pos, lpStruct->start_pos);
	(*env)->SetIntField(env, lpObject, lpCache->end_pos, lpStruct->end_pos);
	(*env)->SetIntField(env, lpObject, lpCache->cur_insert, lpStruct->cur_insert);
	(*env)->SetIntField(env, lpObject, lpCache->new_insert, lpStruct->new_insert);
	(*env)->SetIntField(env, lpObject, lpCache->length, lpStruct->length);
	(*env)->SetShortField(env, lpObject, lpCache->reserved, lpStruct->reserved);
	(*env)->SetIntField(env, lpObject, lpCache->text, (jint)lpStruct->text);
	(*env)->SetIntField(env, lpObject, lpCache->doit, lpStruct->doit);
}
void cachePtTreeItem_tFids(JNIEnv *env, jobject lpObject, PPtTreeItem_t_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->list_flags = (*env)->GetFieldID(env, lpCache->clazz, "list_flags", "I");
	lpCache->list_size_w = (*env)->GetFieldID(env, lpCache->clazz, "list_size_w", "S");
	lpCache->list_size_h = (*env)->GetFieldID(env, lpCache->clazz, "list_size_h", "S");
	lpCache->list_next = (*env)->GetFieldID(env, lpCache->clazz, "list_next", "I");
	lpCache->list_prev = (*env)->GetFieldID(env, lpCache->clazz, "list_prev", "I");
	lpCache->father = (*env)->GetFieldID(env, lpCache->clazz, "father", "I");
	lpCache->son = (*env)->GetFieldID(env, lpCache->clazz, "son", "I");
	lpCache->brother = (*env)->GetFieldID(env, lpCache->clazz, "brother", "I");
	lpCache->dim_w = (*env)->GetFieldID(env, lpCache->clazz, "dim_w", "S");
	lpCache->dim_h = (*env)->GetFieldID(env, lpCache->clazz, "dim_h", "S");
	lpCache->img_set = (*env)->GetFieldID(env, lpCache->clazz, "img_set", "S");
	lpCache->img_unset = (*env)->GetFieldID(env, lpCache->clazz, "img_unset", "S");
	lpCache->data = (*env)->GetFieldID(env, lpCache->clazz, "data", "I");
	lpCache->cached = 1;
}

void getPtTreeItem_tFields(JNIEnv *env, jobject lpObject, PtTreeItem_t *lpStruct, PPtTreeItem_t_FID_CACHE lpCache)
{
	lpStruct->gen.list.flags = (*env)->GetIntField(env, lpObject, lpCache->list_flags);
	lpStruct->gen.list.size.w = (*env)->GetShortField(env, lpObject, lpCache->list_size_w);
	lpStruct->gen.list.size.h = (*env)->GetShortField(env, lpObject, lpCache->list_size_h);
	lpStruct->gen.list.next = (void *) (*env)->GetIntField(env, lpObject, lpCache->list_next);
	lpStruct->gen.list.prev = (void *) (*env)->GetIntField(env, lpObject, lpCache->list_prev);
	lpStruct->gen.father = (void *) (*env)->GetIntField(env, lpObject, lpCache->father);
	lpStruct->gen.son = (void *) (*env)->GetIntField(env, lpObject, lpCache->son);
	lpStruct->gen.brother = (void *) (*env)->GetIntField(env, lpObject, lpCache->brother);
	lpStruct->gen.dim.w = (*env)->GetShortField(env, lpObject, lpCache->dim_w);
	lpStruct->gen.dim.h = (*env)->GetShortField(env, lpObject, lpCache->dim_h);
	lpStruct->attr.img.set = (*env)->GetShortField(env, lpObject, lpCache->img_set);
	lpStruct->attr.img.unset = (*env)->GetShortField(env, lpObject, lpCache->img_unset);
	lpStruct->data = (void *)(*env)->GetIntField(env, lpObject, lpCache->data);
}

void setPtTreeItem_tFields(JNIEnv *env, jobject lpObject, PtTreeItem_t *lpStruct, PPtTreeItem_t_FID_CACHE lpCache)
{
	(*env)->SetIntField(env, lpObject, lpCache->list_flags, lpStruct->gen.list.flags);
	(*env)->SetShortField(env, lpObject, lpCache->list_size_w, lpStruct->gen.list.size.w);
	(*env)->SetShortField(env, lpObject, lpCache->list_size_h, lpStruct->gen.list.size.h);
	(*env)->SetIntField(env, lpObject, lpCache->list_next, (int) lpStruct->gen.list.next);
	(*env)->SetIntField(env, lpObject, lpCache->list_prev, (int) lpStruct->gen.list.prev);
	(*env)->SetIntField(env, lpObject, lpCache->father, (int) lpStruct->gen.father);
	(*env)->SetIntField(env, lpObject, lpCache->son, (int) lpStruct->gen.son);
	(*env)->SetIntField(env, lpObject, lpCache->brother, (int) lpStruct->gen.brother);
	(*env)->SetShortField(env, lpObject, lpCache->dim_w, lpStruct->gen.dim.w);
	(*env)->SetShortField(env, lpObject, lpCache->dim_h, lpStruct->gen.dim.h);
	(*env)->SetShortField(env, lpObject, lpCache->img_set, lpStruct->attr.img.set);
	(*env)->SetShortField(env, lpObject, lpCache->img_unset, lpStruct->attr.img.unset);
	(*env)->SetIntField(env, lpObject, lpCache->data, (int) lpStruct->data);
}
	
void cachePgMap_tFids(JNIEnv *env, jobject lpObject, PPgMap_t_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->dim_w = (*env)->GetFieldID(env, lpCache->clazz, "dim_w", "S");
	lpCache->dim_h = (*env)->GetFieldID(env, lpCache->clazz, "dim_h", "S");
	lpCache->bpl = (*env)->GetFieldID(env, lpCache->clazz, "bpl", "S");
	lpCache->bpp = (*env)->GetFieldID(env, lpCache->clazz, "bpp", "S");
	lpCache->map = (*env)->GetFieldID(env, lpCache->clazz, "map", "I");
	lpCache->cached = 1;
}

void getPgMap_tFields(JNIEnv *env, jobject lpObject, PgMap_t *lpStruct, PPgMap_t_FID_CACHE lpCache)
{
	lpStruct->dim.w = (*env)->GetShortField(env, lpObject, lpCache->dim_w);
	lpStruct->dim.h = (*env)->GetShortField(env, lpObject, lpCache->dim_h);
	lpStruct->bpl = (*env)->GetShortField(env, lpObject, lpCache->bpl);
	lpStruct->bpp = (*env)->GetShortField(env, lpObject, lpCache->bpp);
	lpStruct->map = (char *)(*env)->GetIntField(env, lpObject, lpCache->map);
}

void setPgMap_tFields(JNIEnv *env, jobject lpObject, PgMap_t *lpStruct, PPgMap_t_FID_CACHE lpCache)
{
	(*env)->SetShortField(env, lpObject, lpCache->dim_w, lpStruct->dim.w);
	(*env)->SetShortField(env, lpObject, lpCache->dim_h, lpStruct->dim.h);
	(*env)->SetShortField(env, lpObject, lpCache->bpl, lpStruct->bpl);
	(*env)->SetShortField(env, lpObject, lpCache->bpp, lpStruct->bpp);
	(*env)->SetIntField(env, lpObject, lpCache->map, (jint)lpStruct->map);
}

void cachePtColorSelectInfo_tFids(JNIEnv *env, jobject lpObject, PPtColorSelectInfo_t_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->flags = (*env)->GetFieldID(env, lpCache->clazz, "flags", "S");
	lpCache->nselectors = (*env)->GetFieldID(env, lpCache->clazz, "nselectors", "B");
	lpCache->ncolor_models = (*env)->GetFieldID(env, lpCache->clazz, "ncolor_models", "B");
	lpCache->color_models = (*env)->GetFieldID(env, lpCache->clazz, "color_models", "I");
	lpCache->selectors = (*env)->GetFieldID(env, lpCache->clazz, "selectors", "I");
	lpCache->pos_x = (*env)->GetFieldID(env, lpCache->clazz, "pos_x", "S");
	lpCache->pos_y = (*env)->GetFieldID(env, lpCache->clazz, "pos_y", "S");
	lpCache->size_w = (*env)->GetFieldID(env, lpCache->clazz, "size_w", "S");
	lpCache->size_h = (*env)->GetFieldID(env, lpCache->clazz, "size_h", "S");
	lpCache->palette = (*env)->GetFieldID(env, lpCache->clazz, "palette", "I");
	lpCache->accept_text = (*env)->GetFieldID(env, lpCache->clazz, "accept_text", "I");
	lpCache->dismiss_text = (*env)->GetFieldID(env, lpCache->clazz, "dismiss_text", "I");
	lpCache->accept_dismiss_text = (*env)->GetFieldID(env, lpCache->clazz, "accept_dismiss_text", "I");
	lpCache->apply_f = (*env)->GetFieldID(env, lpCache->clazz, "apply_f", "I");
	lpCache->data = (*env)->GetFieldID(env, lpCache->clazz, "data", "I");
	lpCache->rgb = (*env)->GetFieldID(env, lpCache->clazz, "rgb", "I");
	lpCache->dialog = (*env)->GetFieldID(env, lpCache->clazz, "dialog", "I");
	lpCache->cached = 1;
}

void getPtColorSelectInfo_tFields(JNIEnv *env, jobject lpObject, PtColorSelectInfo_t *lpStruct, PPtColorSelectInfo_t_FID_CACHE lpCache)
{
	lpStruct->flags = (*env)->GetShortField(env, lpObject, lpCache->flags);
	lpStruct->nselectors = (*env)->GetByteField(env, lpObject, lpCache->nselectors);
	lpStruct->ncolor_models = (*env)->GetByteField(env, lpObject, lpCache->ncolor_models);
	lpStruct->color_models = (PgColorModel_t **)(*env)->GetIntField(env, lpObject, lpCache->color_models);
	lpStruct->selectors = (PtColorSelectorSpec_t *)(*env)->GetIntField(env, lpObject, lpCache->selectors);
	lpStruct->pos.x = (*env)->GetShortField(env, lpObject, lpCache->pos_x);
	lpStruct->pos.y = (*env)->GetShortField(env, lpObject, lpCache->pos_y);
	lpStruct->size.w = (*env)->GetShortField(env, lpObject, lpCache->size_w);
	lpStruct->size.h = (*env)->GetShortField(env, lpObject, lpCache->size_h);
	lpStruct->palette.instance = (void *)(*env)->GetIntField(env, lpObject, lpCache->palette);
	lpStruct->accept_text = (char *)(*env)->GetIntField(env, lpObject, lpCache->accept_text);
	lpStruct->dismiss_text = (char *)(*env)->GetIntField(env, lpObject, lpCache->dismiss_text);
	lpStruct->accept_dismiss_text = (char *)(*env)->GetIntField(env, lpObject, lpCache->accept_dismiss_text);
	lpStruct->apply_f = (void (*))(*env)->GetIntField(env, lpObject, lpCache->apply_f);
	lpStruct->data = (void *)(*env)->GetIntField(env, lpObject, lpCache->data);
	lpStruct->rgb = (*env)->GetIntField(env, lpObject, lpCache->rgb);
	lpStruct->dialog = (PtWidget_t *)(*env)->GetIntField(env, lpObject, lpCache->dialog);
}
	
void setPtColorSelectInfo_tFields(JNIEnv *env, jobject lpObject, PtColorSelectInfo_t *lpStruct, PPtColorSelectInfo_t_FID_CACHE lpCache)
{
	(*env)->SetShortField(env, lpObject, lpCache->flags, lpStruct->flags);
	(*env)->SetByteField(env, lpObject, lpCache->nselectors, lpStruct->nselectors);
	(*env)->SetByteField(env, lpObject, lpCache->ncolor_models, lpStruct->ncolor_models);
	(*env)->SetIntField(env, lpObject, lpCache->color_models, (jint)lpStruct->color_models);
	(*env)->SetIntField(env, lpObject, lpCache->selectors, (jint)lpStruct->selectors);
	(*env)->SetShortField(env, lpObject, lpCache->pos_x, lpStruct->pos.x);
	(*env)->SetShortField(env, lpObject, lpCache->pos_y, lpStruct->pos.y);
	(*env)->SetShortField(env, lpObject, lpCache->size_w, lpStruct->size.w);
	(*env)->SetShortField(env, lpObject, lpCache->size_h, lpStruct->size.h);
	(*env)->SetIntField(env, lpObject, lpCache->palette, (jint)lpStruct->palette.instance);
	(*env)->SetIntField(env, lpObject, lpCache->accept_text, (jint)lpStruct->accept_text);
	(*env)->SetIntField(env, lpObject, lpCache->dismiss_text, (jint)lpStruct->dismiss_text);
	(*env)->SetIntField(env, lpObject, lpCache->accept_dismiss_text, (jint)lpStruct->accept_dismiss_text);
	(*env)->SetIntField(env, lpObject, lpCache->apply_f, (jint)lpStruct->apply_f);
	(*env)->SetIntField(env, lpObject, lpCache->data, (jint)lpStruct->data);
	(*env)->SetIntField(env, lpObject, lpCache->rgb, lpStruct->rgb);
	(*env)->SetIntField(env, lpObject, lpCache->dialog, (jint)lpStruct->dialog);
}

void cachePhRegion_tFids(JNIEnv *env, jobject lpObject, PPhRegion_t_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->rid = (*env)->GetFieldID(env, lpCache->clazz, "rid", "I");
	lpCache->handle = (*env)->GetFieldID(env, lpCache->clazz, "handle", "I");
	lpCache->owner = (*env)->GetFieldID(env, lpCache->clazz, "owner", "I");
	lpCache->flags = (*env)->GetFieldID(env, lpCache->clazz, "flags", "I");
	lpCache->state = (*env)->GetFieldID(env, lpCache->clazz, "state", "S");
	lpCache->events_sense = (*env)->GetFieldID(env, lpCache->clazz, "events_sense", "I");
	lpCache->events_opaque = (*env)->GetFieldID(env, lpCache->clazz, "events_opaque", "I");
	lpCache->origin_x = (*env)->GetFieldID(env, lpCache->clazz, "origin_x", "S");
	lpCache->origin_y = (*env)->GetFieldID(env, lpCache->clazz, "origin_y", "S");
	lpCache->parent = (*env)->GetFieldID(env, lpCache->clazz, "parent", "I");
	lpCache->child = (*env)->GetFieldID(env, lpCache->clazz, "child", "I");
	lpCache->bro_in_front = (*env)->GetFieldID(env, lpCache->clazz, "bro_in_front", "I");
	lpCache->bro_behind = (*env)->GetFieldID(env, lpCache->clazz, "bro_behind", "I");
	lpCache->cursor_color = (*env)->GetFieldID(env, lpCache->clazz, "cursor_color", "I");
	lpCache->input_group = (*env)->GetFieldID(env, lpCache->clazz, "input_group", "S");
	lpCache->data_len = (*env)->GetFieldID(env, lpCache->clazz, "data_len", "S");
	lpCache->cursor_type = (*env)->GetFieldID(env, lpCache->clazz, "cursor_type", "S");
	lpCache->cached = 1;
}

void getPhRegion_tFields(JNIEnv *env, jobject lpObject, PhRegion_t *lpStruct, PPhRegion_t_FID_CACHE lpCache)
{
	lpStruct->rid = (*env)->GetIntField(env, lpObject, lpCache->rid);
	lpStruct->handle = (*env)->GetIntField(env, lpObject, lpCache->handle);
	lpStruct->owner = (*env)->GetIntField(env, lpObject, lpCache->owner);
	lpStruct->flags = (*env)->GetIntField(env, lpObject, lpCache->flags);
	lpStruct->state = (*env)->GetShortField(env, lpObject, lpCache->state);
	lpStruct->events_sense = (*env)->GetIntField(env, lpObject, lpCache->events_sense);
	lpStruct->events_opaque = (*env)->GetIntField(env, lpObject, lpCache->events_opaque);
	lpStruct->origin.x = (*env)->GetShortField(env, lpObject, lpCache->origin_x);
	lpStruct->origin.y = (*env)->GetShortField(env, lpObject, lpCache->origin_y);
	lpStruct->parent = (*env)->GetIntField(env, lpObject, lpCache->parent);
	lpStruct->child = (*env)->GetIntField(env, lpObject, lpCache->child);
	lpStruct->bro_in_front = (*env)->GetIntField(env, lpObject, lpCache->bro_in_front);
	lpStruct->bro_behind = (*env)->GetIntField(env, lpObject, lpCache->bro_behind);
	lpStruct->cursor_color = (*env)->GetIntField(env, lpObject, lpCache->cursor_color);
	lpStruct->input_group = (*env)->GetShortField(env, lpObject, lpCache->input_group);
	lpStruct->data_len = (*env)->GetShortField(env, lpObject, lpCache->data_len);
	lpStruct->cursor_type = (*env)->GetShortField(env, lpObject, lpCache->cursor_type);
}

void setPhRegion_tFields(JNIEnv *env, jobject lpObject, PhRegion_t *lpStruct, PPhRegion_t_FID_CACHE lpCache)
{
	(*env)->SetIntField(env, lpObject, lpCache->rid, lpStruct->rid);
	(*env)->SetIntField(env, lpObject, lpCache->handle, lpStruct->handle);
	(*env)->SetIntField(env, lpObject, lpCache->owner, lpStruct->owner);
	(*env)->SetIntField(env, lpObject, lpCache->flags, lpStruct->flags);
	(*env)->SetShortField(env, lpObject, lpCache->state, lpStruct->state);
	(*env)->SetIntField(env, lpObject, lpCache->events_sense, lpStruct->events_sense);
	(*env)->SetIntField(env, lpObject, lpCache->events_opaque, lpStruct->events_opaque);
	(*env)->SetShortField(env, lpObject, lpCache->origin_x, lpStruct->origin.x);
	(*env)->SetShortField(env, lpObject, lpCache->origin_y, lpStruct->origin.y);
	(*env)->SetIntField(env, lpObject, lpCache->parent, lpStruct->parent);
	(*env)->SetIntField(env, lpObject, lpCache->child, lpStruct->child);
	(*env)->SetIntField(env, lpObject, lpCache->bro_in_front, lpStruct->bro_in_front);
	(*env)->SetIntField(env, lpObject, lpCache->bro_behind, lpStruct->bro_behind);
	(*env)->SetIntField(env, lpObject, lpCache->cursor_color, lpStruct->cursor_color);
	(*env)->SetShortField(env, lpObject, lpCache->input_group, lpStruct->input_group);
	(*env)->SetShortField(env, lpObject, lpCache->data_len, lpStruct->data_len);
	(*env)->SetShortField(env, lpObject, lpCache->cursor_type, lpStruct->cursor_type);
}

void cachePtContainerCallback_tFids(JNIEnv *env, jobject lpObject, PPtContainerCallback_t_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->new_size_ul_x = (*env)->GetFieldID(env, lpCache->clazz, "new_size_ul_x", "S");
	lpCache->new_size_ul_y = (*env)->GetFieldID(env, lpCache->clazz, "new_size_ul_y", "S");
	lpCache->new_size_lr_x = (*env)->GetFieldID(env, lpCache->clazz, "new_size_lr_x", "S");
	lpCache->new_size_lr_y = (*env)->GetFieldID(env, lpCache->clazz, "new_size_lr_y", "S");
	lpCache->old_size_ul_x = (*env)->GetFieldID(env, lpCache->clazz, "old_size_ul_x", "S");
	lpCache->old_size_ul_y = (*env)->GetFieldID(env, lpCache->clazz, "old_size_ul_y", "S");
	lpCache->old_size_lr_x = (*env)->GetFieldID(env, lpCache->clazz, "old_size_lr_x", "S");
	lpCache->old_size_lr_y = (*env)->GetFieldID(env, lpCache->clazz, "old_size_lr_y", "S");
	lpCache->new_dim_w = (*env)->GetFieldID(env, lpCache->clazz, "new_dim_w", "S");
	lpCache->new_dim_h = (*env)->GetFieldID(env, lpCache->clazz, "new_dim_h", "S");
	lpCache->old_dim_w = (*env)->GetFieldID(env, lpCache->clazz, "old_dim_w", "S");
	lpCache->old_dim_h = (*env)->GetFieldID(env, lpCache->clazz, "old_dim_h", "S");
	lpCache->cached = 1;
}

void getPtContainerCallback_tFields(JNIEnv *env, jobject lpObject, PtContainerCallback_t *lpStruct, PPtContainerCallback_t_FID_CACHE lpCache)
{
	lpStruct->new_size.ul.x = (*env)->GetShortField(env, lpObject, lpCache->new_size_ul_x);
	lpStruct->new_size.ul.y = (*env)->GetShortField(env, lpObject, lpCache->new_size_ul_y);
	lpStruct->new_size.lr.x = (*env)->GetShortField(env, lpObject, lpCache->new_size_lr_x);
	lpStruct->new_size.lr.y = (*env)->GetShortField(env, lpObject, lpCache->new_size_lr_y);
	lpStruct->old_size.ul.x = (*env)->GetShortField(env, lpObject, lpCache->old_size_ul_x);
	lpStruct->old_size.ul.y = (*env)->GetShortField(env, lpObject, lpCache->old_size_ul_y);
	lpStruct->old_size.lr.x = (*env)->GetShortField(env, lpObject, lpCache->old_size_lr_x);
	lpStruct->old_size.lr.y = (*env)->GetShortField(env, lpObject, lpCache->old_size_lr_y);
	lpStruct->new_dim.w = (*env)->GetShortField(env, lpObject, lpCache->new_dim_w);
	lpStruct->new_dim.h = (*env)->GetShortField(env, lpObject, lpCache->new_dim_h);
	lpStruct->old_dim.w = (*env)->GetShortField(env, lpObject, lpCache->old_dim_w);
	lpStruct->old_dim.h = (*env)->GetShortField(env, lpObject, lpCache->old_dim_h);
}

void setPtContainerCallback_tFields(JNIEnv *env, jobject lpObject, PtContainerCallback_t *lpStruct, PPtContainerCallback_t_FID_CACHE lpCache)
{
	(*env)->SetShortField(env, lpObject, lpCache->new_size_ul_x, lpStruct->new_size.ul.x);
	(*env)->SetShortField(env, lpObject, lpCache->new_size_ul_y, lpStruct->new_size.ul.y);
	(*env)->SetShortField(env, lpObject, lpCache->new_size_lr_x, lpStruct->new_size.lr.x);
	(*env)->SetShortField(env, lpObject, lpCache->new_size_lr_y, lpStruct->new_size.lr.y);
	(*env)->SetShortField(env, lpObject, lpCache->old_size_ul_x, lpStruct->old_size.ul.x);
	(*env)->SetShortField(env, lpObject, lpCache->old_size_ul_y, lpStruct->old_size.ul.y);
	(*env)->SetShortField(env, lpObject, lpCache->old_size_lr_x, lpStruct->old_size.lr.x);
	(*env)->SetShortField(env, lpObject, lpCache->old_size_lr_y, lpStruct->old_size.lr.y);
	(*env)->SetShortField(env, lpObject, lpCache->new_dim_w, lpStruct->new_dim.w);
	(*env)->SetShortField(env, lpObject, lpCache->new_dim_h, lpStruct->new_dim.h);
	(*env)->SetShortField(env, lpObject, lpCache->old_dim_w, lpStruct->old_dim.w);
	(*env)->SetShortField(env, lpObject, lpCache->old_dim_h, lpStruct->old_dim.h);
}

void cachePhCursorDef_tFids(JNIEnv *env, jobject lpObject, PPhCursorDef_t_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->bytesperline2 = (*env)->GetFieldID(env, lpCache->clazz, "bytesperline2", "B");
	lpCache->color2 = (*env)->GetFieldID(env, lpCache->clazz, "color2", "I");
	lpCache->offset2_y = (*env)->GetFieldID(env, lpCache->clazz, "offset2_y", "S");
	lpCache->offset2_x = (*env)->GetFieldID(env, lpCache->clazz, "offset2_x", "S");
	lpCache->size2_y = (*env)->GetFieldID(env, lpCache->clazz, "size2_y", "S");
	lpCache->size2_x = (*env)->GetFieldID(env, lpCache->clazz, "size2_x", "S");
	lpCache->bytesperline1 = (*env)->GetFieldID(env, lpCache->clazz, "bytesperline1", "B");
	lpCache->color1 = (*env)->GetFieldID(env, lpCache->clazz, "color1", "I");
	lpCache->offset1_y = (*env)->GetFieldID(env, lpCache->clazz, "offset1_y", "S");
	lpCache->offset1_x = (*env)->GetFieldID(env, lpCache->clazz, "offset1_x", "S");
	lpCache->size1_y = (*env)->GetFieldID(env, lpCache->clazz, "size1_y", "S");
	lpCache->size1_x = (*env)->GetFieldID(env, lpCache->clazz, "size1_x", "S");
	lpCache->hdr_type = (*env)->GetFieldID(env, lpCache->clazz, "hdr_type", "S");
	lpCache->hdr_len = (*env)->GetFieldID(env, lpCache->clazz, "hdr_len", "S");
	lpCache->cached = 1;
}

void getPhCursorDef_tFields(JNIEnv *env, jobject lpObject, PhCursorDef_t *lpStruct, PPhCursorDef_t_FID_CACHE lpCache)
{
	lpStruct->bytesperline2 = (*env)->GetByteField(env, lpObject, lpCache->bytesperline2);
	lpStruct->color2 = (*env)->GetIntField(env, lpObject, lpCache->color2);
	lpStruct->offset2.y = (*env)->GetShortField(env, lpObject, lpCache->offset2_y);
	lpStruct->offset2.x = (*env)->GetShortField(env, lpObject, lpCache->offset2_x);
	lpStruct->size2.y = (*env)->GetShortField(env, lpObject, lpCache->size2_y);
	lpStruct->size2.x = (*env)->GetShortField(env, lpObject, lpCache->size2_x);
	lpStruct->bytesperline1 = (*env)->GetByteField(env, lpObject, lpCache->bytesperline1);
	lpStruct->color1 = (*env)->GetIntField(env, lpObject, lpCache->color1);
	lpStruct->offset1.y = (*env)->GetShortField(env, lpObject, lpCache->offset1_y);
	lpStruct->offset1.x = (*env)->GetShortField(env, lpObject, lpCache->offset1_x);
	lpStruct->size1.y = (*env)->GetShortField(env, lpObject, lpCache->size1_y);
	lpStruct->size1.x = (*env)->GetShortField(env, lpObject, lpCache->size1_x);
	lpStruct->hdr.type = (*env)->GetShortField(env, lpObject, lpCache->hdr_type);
	lpStruct->hdr.len = (*env)->GetShortField(env, lpObject, lpCache->hdr_len);
}

void setPhCursorDef_tFields(JNIEnv *env, jobject lpObject, PhCursorDef_t *lpStruct, PPhCursorDef_t_FID_CACHE lpCache)
{
	(*env)->SetByteField(env, lpObject, lpCache->bytesperline2, lpStruct->bytesperline2);
	(*env)->SetIntField(env, lpObject, lpCache->color2, lpStruct->color2);
	(*env)->SetShortField(env, lpObject, lpCache->offset2_y, lpStruct->offset2.y);
	(*env)->SetShortField(env, lpObject, lpCache->offset2_x, lpStruct->offset2.x);
	(*env)->SetShortField(env, lpObject, lpCache->size2_y, lpStruct->size2.y);
	(*env)->SetShortField(env, lpObject, lpCache->size2_x, lpStruct->size2.x);
	(*env)->SetByteField(env, lpObject, lpCache->bytesperline1, lpStruct->bytesperline1);
	(*env)->SetIntField(env, lpObject, lpCache->color1, lpStruct->color1);
	(*env)->SetShortField(env, lpObject, lpCache->offset1_y, lpStruct->offset1.y);
	(*env)->SetShortField(env, lpObject, lpCache->offset1_x, lpStruct->offset1.x);
	(*env)->SetShortField(env, lpObject, lpCache->size1_y, lpStruct->size1.y);
	(*env)->SetShortField(env, lpObject, lpCache->size1_x, lpStruct->size1.x);
	(*env)->SetShortField(env, lpObject, lpCache->hdr_type, lpStruct->hdr.type);
	(*env)->SetShortField(env, lpObject, lpCache->hdr_len, lpStruct->hdr.len);
}

void cachePgDisplaySettings_tFids(JNIEnv *env, jobject lpObject, PPgDisplaySettings_t_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->reserved = (*env)->GetFieldID(env, lpCache->clazz, "reserved", "[I");
	lpCache->flags = (*env)->GetFieldID(env, lpCache->clazz, "flags", "I");
	lpCache->refresh = (*env)->GetFieldID(env, lpCache->clazz, "refresh", "I");
	lpCache->yres = (*env)->GetFieldID(env, lpCache->clazz, "yres", "I");
	lpCache->xres = (*env)->GetFieldID(env, lpCache->clazz, "xres", "I");
	lpCache->mode = (*env)->GetFieldID(env, lpCache->clazz, "mode", "I");
	lpCache->cached = 1;
}

void getPgDisplaySettings_tFields(JNIEnv *env, jobject lpObject, PgDisplaySettings_t *lpStruct, PPgDisplaySettings_t_FID_CACHE lpCache)
{
	jintArray reserved = (*env)->GetObjectField(env, lpObject, lpCache->reserved);
    if (reserved) {
        jint *reserved1 = (*env)->GetIntArrayElements(env, reserved, NULL);
        memcpy(reserved1, lpStruct->reserved, sizeof (lpStruct->reserved));
        (*env)->ReleaseIntArrayElements(env, reserved, reserved1, JNI_ABORT);
	}
	lpStruct->flags = (*env)->GetIntField(env, lpObject, lpCache->flags);
	lpStruct->refresh = (*env)->GetIntField(env, lpObject, lpCache->refresh);
	lpStruct->yres = (*env)->GetIntField(env, lpObject, lpCache->yres);
	lpStruct->xres = (*env)->GetIntField(env, lpObject, lpCache->xres);
	lpStruct->mode = (*env)->GetIntField(env, lpObject, lpCache->mode);
}

void setPgDisplaySettings_tFields(JNIEnv *env, jobject lpObject, PgDisplaySettings_t *lpStruct, PPgDisplaySettings_t_FID_CACHE lpCache)
{
	jintArray reserved = (*env)->GetObjectField(env, lpObject, lpCache->reserved);
    if (reserved) {
        jint *reserved1 = (*env)->GetIntArrayElements(env, reserved, NULL);
        memcpy(reserved1, lpStruct->reserved, sizeof (lpStruct->reserved));
        (*env)->ReleaseIntArrayElements(env, reserved, reserved1, 0);
	}
	(*env)->SetIntField(env, lpObject, lpCache->flags, lpStruct->flags);
	(*env)->SetIntField(env, lpObject, lpCache->refresh, lpStruct->refresh);
	(*env)->SetIntField(env, lpObject, lpCache->yres, lpStruct->yres);
	(*env)->SetIntField(env, lpObject, lpCache->xres, lpStruct->xres);
	(*env)->SetIntField(env, lpObject, lpCache->mode, lpStruct->mode);
}

void cachePgVideoModeInfo_tFids(JNIEnv *env, jobject lpObject, PPgVideoModeInfo_t_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->refresh_rates = (*env)->GetFieldID(env, lpCache->clazz, "refresh_rates", "[B");
	lpCache->mode_capabilities6 = (*env)->GetFieldID(env, lpCache->clazz, "mode_capabilities6", "I");
	lpCache->mode_capabilities5 = (*env)->GetFieldID(env, lpCache->clazz, "mode_capabilities5", "I");
	lpCache->mode_capabilities4 = (*env)->GetFieldID(env, lpCache->clazz, "mode_capabilities4", "I");
	lpCache->mode_capabilities3 = (*env)->GetFieldID(env, lpCache->clazz, "mode_capabilities3", "I");
	lpCache->mode_capabilities2 = (*env)->GetFieldID(env, lpCache->clazz, "mode_capabilities2", "I");
	lpCache->mode_capabilities1 = (*env)->GetFieldID(env, lpCache->clazz, "mode_capabilities1", "I");
	lpCache->type = (*env)->GetFieldID(env, lpCache->clazz, "type", "I");
	lpCache->bytes_per_scanline = (*env)->GetFieldID(env, lpCache->clazz, "bytes_per_scanline", "S");
	lpCache->bits_per_pixel = (*env)->GetFieldID(env, lpCache->clazz, "bits_per_pixel", "S");
	lpCache->height = (*env)->GetFieldID(env, lpCache->clazz, "height", "S");
	lpCache->width = (*env)->GetFieldID(env, lpCache->clazz, "width", "S");
	lpCache->cached = 1;
}

void getPgVideoModeInfo_tFields(JNIEnv *env, jobject lpObject, PgVideoModeInfo_t *lpStruct, PPgVideoModeInfo_t_FID_CACHE lpCache)
{
	jbyteArray refresh_rates = (*env)->GetObjectField(env, lpObject, lpCache->refresh_rates);
    if (refresh_rates) {
        jbyte *refresh_rates1 = (*env)->GetByteArrayElements(env, refresh_rates, NULL);
        memcpy(lpStruct->refresh_rates, refresh_rates1, sizeof(lpStruct->refresh_rates));
        (*env)->ReleaseByteArrayElements(env, refresh_rates, refresh_rates1, JNI_ABORT);
	}
	lpStruct->mode_capabilities6 = (*env)->GetIntField(env, lpObject, lpCache->mode_capabilities6);
	lpStruct->mode_capabilities5 = (*env)->GetIntField(env, lpObject, lpCache->mode_capabilities5);
	lpStruct->mode_capabilities4 = (*env)->GetIntField(env, lpObject, lpCache->mode_capabilities4);
	lpStruct->mode_capabilities3 = (*env)->GetIntField(env, lpObject, lpCache->mode_capabilities3);
	lpStruct->mode_capabilities2 = (*env)->GetIntField(env, lpObject, lpCache->mode_capabilities2);
	lpStruct->mode_capabilities1 = (*env)->GetIntField(env, lpObject, lpCache->mode_capabilities1);
	lpStruct->type = (*env)->GetIntField(env, lpObject, lpCache->type);
	lpStruct->bytes_per_scanline = (*env)->GetShortField(env, lpObject, lpCache->bytes_per_scanline);
	lpStruct->bits_per_pixel = (*env)->GetShortField(env, lpObject, lpCache->bits_per_pixel);
	lpStruct->height = (*env)->GetShortField(env, lpObject, lpCache->height);
	lpStruct->width = (*env)->GetShortField(env, lpObject, lpCache->width);
}

void setPgVideoModeInfo_tFields(JNIEnv *env, jobject lpObject, PgVideoModeInfo_t *lpStruct, PPgVideoModeInfo_t_FID_CACHE lpCache)
{
	jbyteArray refresh_rates = (*env)->GetObjectField(env, lpObject, lpCache->refresh_rates);
    if (refresh_rates) {
        jbyte *refresh_rates1 = (*env)->GetByteArrayElements(env, refresh_rates, NULL);
        memcpy(refresh_rates1, lpStruct->refresh_rates, sizeof(lpStruct->refresh_rates));
        (*env)->ReleaseByteArrayElements(env, refresh_rates, refresh_rates1, 0);
	}
	(*env)->SetIntField(env, lpObject, lpCache->mode_capabilities6, lpStruct->mode_capabilities6);
	(*env)->SetIntField(env, lpObject, lpCache->mode_capabilities5, lpStruct->mode_capabilities5);
	(*env)->SetIntField(env, lpObject, lpCache->mode_capabilities4, lpStruct->mode_capabilities4);
	(*env)->SetIntField(env, lpObject, lpCache->mode_capabilities3, lpStruct->mode_capabilities3);
	(*env)->SetIntField(env, lpObject, lpCache->mode_capabilities2, lpStruct->mode_capabilities2);
	(*env)->SetIntField(env, lpObject, lpCache->mode_capabilities1, lpStruct->mode_capabilities1);
	(*env)->SetIntField(env, lpObject, lpCache->type, lpStruct->type);
	(*env)->SetShortField(env, lpObject, lpCache->bytes_per_scanline, lpStruct->bytes_per_scanline);
	(*env)->SetShortField(env, lpObject, lpCache->bits_per_pixel, lpStruct->bits_per_pixel);
	(*env)->SetShortField(env, lpObject, lpCache->height, lpStruct->height);
	(*env)->SetShortField(env, lpObject, lpCache->width, lpStruct->width);
}

void cachePhClipHeaderFids(JNIEnv *env, jobject lpObject, PPhClipHeader_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->data = (*env)->GetFieldID(env, lpCache->clazz, "data", "I");
	lpCache->length = (*env)->GetFieldID(env, lpCache->clazz, "length", "S");
	lpCache->type_7 = (*env)->GetFieldID(env, lpCache->clazz, "type_7", "B");
	lpCache->type_6 = (*env)->GetFieldID(env, lpCache->clazz, "type_6", "B");
	lpCache->type_5 = (*env)->GetFieldID(env, lpCache->clazz, "type_5", "B");
	lpCache->type_4 = (*env)->GetFieldID(env, lpCache->clazz, "type_4", "B");
	lpCache->type_3 = (*env)->GetFieldID(env, lpCache->clazz, "type_3", "B");
	lpCache->type_2 = (*env)->GetFieldID(env, lpCache->clazz, "type_2", "B");
	lpCache->type_1 = (*env)->GetFieldID(env, lpCache->clazz, "type_1", "B");
	lpCache->type_0 = (*env)->GetFieldID(env, lpCache->clazz, "type_0", "B");
	lpCache->cached = 1;
}

void getPhClipHeaderFields(JNIEnv *env, jobject lpObject, PhClipHeader *lpStruct, PPhClipHeader_FID_CACHE lpCache)
{
	lpStruct->data = (void *)(*env)->GetIntField(env, lpObject, lpCache->data);
	lpStruct->length = (*env)->GetShortField(env, lpObject, lpCache->length);
	lpStruct->type[7] = (*env)->GetByteField(env, lpObject, lpCache->type_7);
	lpStruct->type[6] = (*env)->GetByteField(env, lpObject, lpCache->type_6);
	lpStruct->type[5] = (*env)->GetByteField(env, lpObject, lpCache->type_5);
	lpStruct->type[4] = (*env)->GetByteField(env, lpObject, lpCache->type_4);
	lpStruct->type[3] = (*env)->GetByteField(env, lpObject, lpCache->type_3);
	lpStruct->type[2] = (*env)->GetByteField(env, lpObject, lpCache->type_2);
	lpStruct->type[1] = (*env)->GetByteField(env, lpObject, lpCache->type_1);
	lpStruct->type[0] = (*env)->GetByteField(env, lpObject, lpCache->type_0);
}

void setPhClipHeaderFields(JNIEnv *env, jobject lpObject, PhClipHeader *lpStruct, PPhClipHeader_FID_CACHE lpCache)
{
	(*env)->SetIntField(env, lpObject, lpCache->data, (jint)lpStruct->data);
	(*env)->SetShortField(env, lpObject, lpCache->length, lpStruct->length);
	(*env)->SetByteField(env, lpObject, lpCache->type_7, lpStruct->type[7]);
	(*env)->SetByteField(env, lpObject, lpCache->type_6, lpStruct->type[6]);
	(*env)->SetByteField(env, lpObject, lpCache->type_5, lpStruct->type[5]);
	(*env)->SetByteField(env, lpObject, lpCache->type_4, lpStruct->type[4]);
	(*env)->SetByteField(env, lpObject, lpCache->type_3, lpStruct->type[3]);
	(*env)->SetByteField(env, lpObject, lpCache->type_2, lpStruct->type[2]);
	(*env)->SetByteField(env, lpObject, lpCache->type_1, lpStruct->type[1]);
	(*env)->SetByteField(env, lpObject, lpCache->type_0, lpStruct->type[0]);
}