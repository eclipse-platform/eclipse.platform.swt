/*******************************************************************************
* Copyright (c) 2000, 2003 IBM Corporation and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Common Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/cpl-v10.html
* 
* Contributors:
*     IBM Corporation - initial API and implementation
*******************************************************************************/

#include "swt.h"
#include "os_structs.h"

#ifndef NO_FontDetails
typedef struct FontDetails_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID desc, stem, losize, hisize, flags;
} FontDetails_FID_CACHE;

FontDetails_FID_CACHE FontDetailsFc;

void cacheFontDetailsFields(JNIEnv *env, jobject lpObject)
{
	if (FontDetailsFc.cached) return;
	FontDetailsFc.clazz = (*env)->GetObjectClass(env, lpObject);
	FontDetailsFc.desc = (*env)->GetFieldID(env, FontDetailsFc.clazz, "desc", "[B");
	FontDetailsFc.stem = (*env)->GetFieldID(env, FontDetailsFc.clazz, "stem", "[B");
	FontDetailsFc.losize = (*env)->GetFieldID(env, FontDetailsFc.clazz, "losize", "S");
	FontDetailsFc.hisize = (*env)->GetFieldID(env, FontDetailsFc.clazz, "hisize", "S");
	FontDetailsFc.flags = (*env)->GetFieldID(env, FontDetailsFc.clazz, "flags", "S");
	FontDetailsFc.cached = 1;
}

FontDetails *getFontDetailsFields(JNIEnv *env, jobject lpObject, FontDetails *lpStruct)
{
	if (!FontDetailsFc.cached) cacheFontDetailsFields(env, lpObject);
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, FontDetailsFc.desc);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->desc), (void *)lpStruct->desc);
	}
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, FontDetailsFc.stem);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->stem), (void *)lpStruct->stem);
	}
	lpStruct->losize = (*env)->GetShortField(env, lpObject, FontDetailsFc.losize);
	lpStruct->hisize = (*env)->GetShortField(env, lpObject, FontDetailsFc.hisize);
	lpStruct->flags = (*env)->GetShortField(env, lpObject, FontDetailsFc.flags);
	return lpStruct;
}

void setFontDetailsFields(JNIEnv *env, jobject lpObject, FontDetails *lpStruct)
{
	if (!FontDetailsFc.cached) cacheFontDetailsFields(env, lpObject);
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, FontDetailsFc.desc);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->desc), (void *)lpStruct->desc);
	}
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, FontDetailsFc.stem);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->stem), (void *)lpStruct->stem);
	}
	(*env)->SetShortField(env, lpObject, FontDetailsFc.losize, (jshort)lpStruct->losize);
	(*env)->SetShortField(env, lpObject, FontDetailsFc.hisize, (jshort)lpStruct->hisize);
	(*env)->SetShortField(env, lpObject, FontDetailsFc.flags, (jshort)lpStruct->flags);
}
#endif

#ifndef NO_FontQueryInfo
typedef struct FontQueryInfo_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID font, desc, size, style, ascender, descender, width, lochar, hichar;
} FontQueryInfo_FID_CACHE;

FontQueryInfo_FID_CACHE FontQueryInfoFc;

void cacheFontQueryInfoFields(JNIEnv *env, jobject lpObject)
{
	if (FontQueryInfoFc.cached) return;
	FontQueryInfoFc.clazz = (*env)->GetObjectClass(env, lpObject);
	FontQueryInfoFc.font = (*env)->GetFieldID(env, FontQueryInfoFc.clazz, "font", "[B");
	FontQueryInfoFc.desc = (*env)->GetFieldID(env, FontQueryInfoFc.clazz, "desc", "[B");
	FontQueryInfoFc.size = (*env)->GetFieldID(env, FontQueryInfoFc.clazz, "size", "S");
	FontQueryInfoFc.style = (*env)->GetFieldID(env, FontQueryInfoFc.clazz, "style", "S");
	FontQueryInfoFc.ascender = (*env)->GetFieldID(env, FontQueryInfoFc.clazz, "ascender", "S");
	FontQueryInfoFc.descender = (*env)->GetFieldID(env, FontQueryInfoFc.clazz, "descender", "S");
	FontQueryInfoFc.width = (*env)->GetFieldID(env, FontQueryInfoFc.clazz, "width", "S");
	FontQueryInfoFc.lochar = (*env)->GetFieldID(env, FontQueryInfoFc.clazz, "lochar", "I");
	FontQueryInfoFc.hichar = (*env)->GetFieldID(env, FontQueryInfoFc.clazz, "hichar", "I");
	FontQueryInfoFc.cached = 1;
}

FontQueryInfo *getFontQueryInfoFields(JNIEnv *env, jobject lpObject, FontQueryInfo *lpStruct)
{
	if (!FontQueryInfoFc.cached) cacheFontQueryInfoFields(env, lpObject);
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, FontQueryInfoFc.font);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->font), (void *)lpStruct->font);
	}
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, FontQueryInfoFc.desc);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->desc), (void *)lpStruct->desc);
	}
	lpStruct->size = (*env)->GetShortField(env, lpObject, FontQueryInfoFc.size);
	lpStruct->style = (*env)->GetShortField(env, lpObject, FontQueryInfoFc.style);
	lpStruct->ascender = (*env)->GetShortField(env, lpObject, FontQueryInfoFc.ascender);
	lpStruct->descender = (*env)->GetShortField(env, lpObject, FontQueryInfoFc.descender);
	lpStruct->width = (*env)->GetShortField(env, lpObject, FontQueryInfoFc.width);
	lpStruct->lochar = (*env)->GetIntField(env, lpObject, FontQueryInfoFc.lochar);
	lpStruct->hichar = (*env)->GetIntField(env, lpObject, FontQueryInfoFc.hichar);
	return lpStruct;
}

void setFontQueryInfoFields(JNIEnv *env, jobject lpObject, FontQueryInfo *lpStruct)
{
	if (!FontQueryInfoFc.cached) cacheFontQueryInfoFields(env, lpObject);
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, FontQueryInfoFc.font);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->font), (void *)lpStruct->font);
	}
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, FontQueryInfoFc.desc);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->desc), (void *)lpStruct->desc);
	}
	(*env)->SetShortField(env, lpObject, FontQueryInfoFc.size, (jshort)lpStruct->size);
	(*env)->SetShortField(env, lpObject, FontQueryInfoFc.style, (jshort)lpStruct->style);
	(*env)->SetShortField(env, lpObject, FontQueryInfoFc.ascender, (jshort)lpStruct->ascender);
	(*env)->SetShortField(env, lpObject, FontQueryInfoFc.descender, (jshort)lpStruct->descender);
	(*env)->SetShortField(env, lpObject, FontQueryInfoFc.width, (jshort)lpStruct->width);
	(*env)->SetIntField(env, lpObject, FontQueryInfoFc.lochar, (jint)lpStruct->lochar);
	(*env)->SetIntField(env, lpObject, FontQueryInfoFc.hichar, (jint)lpStruct->hichar);
}
#endif

#ifndef NO_PgAlpha_t
typedef struct PgAlpha_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID alpha_op, src_alpha_map_dim_w, src_alpha_map_dim_h, src_alpha_map_bpl, src_alpha_map_bpp, src_alpha_map_map, src_global_alpha, dest_global_alpha;
} PgAlpha_t_FID_CACHE;

PgAlpha_t_FID_CACHE PgAlpha_tFc;

void cachePgAlpha_tFields(JNIEnv *env, jobject lpObject)
{
	if (PgAlpha_tFc.cached) return;
	PgAlpha_tFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PgAlpha_tFc.alpha_op = (*env)->GetFieldID(env, PgAlpha_tFc.clazz, "alpha_op", "I");
	PgAlpha_tFc.src_alpha_map_dim_w = (*env)->GetFieldID(env, PgAlpha_tFc.clazz, "src_alpha_map_dim_w", "S");
	PgAlpha_tFc.src_alpha_map_dim_h = (*env)->GetFieldID(env, PgAlpha_tFc.clazz, "src_alpha_map_dim_h", "S");
	PgAlpha_tFc.src_alpha_map_bpl = (*env)->GetFieldID(env, PgAlpha_tFc.clazz, "src_alpha_map_bpl", "S");
	PgAlpha_tFc.src_alpha_map_bpp = (*env)->GetFieldID(env, PgAlpha_tFc.clazz, "src_alpha_map_bpp", "S");
	PgAlpha_tFc.src_alpha_map_map = (*env)->GetFieldID(env, PgAlpha_tFc.clazz, "src_alpha_map_map", "I");
	PgAlpha_tFc.src_global_alpha = (*env)->GetFieldID(env, PgAlpha_tFc.clazz, "src_global_alpha", "B");
	PgAlpha_tFc.dest_global_alpha = (*env)->GetFieldID(env, PgAlpha_tFc.clazz, "dest_global_alpha", "B");
	PgAlpha_tFc.cached = 1;
}

PgAlpha_t *getPgAlpha_tFields(JNIEnv *env, jobject lpObject, PgAlpha_t *lpStruct)
{
	if (!PgAlpha_tFc.cached) cachePgAlpha_tFields(env, lpObject);
	lpStruct->alpha_op = (*env)->GetIntField(env, lpObject, PgAlpha_tFc.alpha_op);
	lpStruct->src_alpha_map.dim.w = (*env)->GetShortField(env, lpObject, PgAlpha_tFc.src_alpha_map_dim_w);
	lpStruct->src_alpha_map.dim.h = (*env)->GetShortField(env, lpObject, PgAlpha_tFc.src_alpha_map_dim_h);
	lpStruct->src_alpha_map.bpl = (*env)->GetShortField(env, lpObject, PgAlpha_tFc.src_alpha_map_bpl);
	lpStruct->src_alpha_map.bpp = (*env)->GetShortField(env, lpObject, PgAlpha_tFc.src_alpha_map_bpp);
	lpStruct->src_alpha_map.map = (char *)(*env)->GetIntField(env, lpObject, PgAlpha_tFc.src_alpha_map_map);
	lpStruct->src_global_alpha = (*env)->GetByteField(env, lpObject, PgAlpha_tFc.src_global_alpha);
	lpStruct->dest_global_alpha = (*env)->GetByteField(env, lpObject, PgAlpha_tFc.dest_global_alpha);
	return lpStruct;
}

void setPgAlpha_tFields(JNIEnv *env, jobject lpObject, PgAlpha_t *lpStruct)
{
	if (!PgAlpha_tFc.cached) cachePgAlpha_tFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, PgAlpha_tFc.alpha_op, (jint)lpStruct->alpha_op);
	(*env)->SetShortField(env, lpObject, PgAlpha_tFc.src_alpha_map_dim_w, (jshort)lpStruct->src_alpha_map.dim.w);
	(*env)->SetShortField(env, lpObject, PgAlpha_tFc.src_alpha_map_dim_h, (jshort)lpStruct->src_alpha_map.dim.h);
	(*env)->SetShortField(env, lpObject, PgAlpha_tFc.src_alpha_map_bpl, (jshort)lpStruct->src_alpha_map.bpl);
	(*env)->SetShortField(env, lpObject, PgAlpha_tFc.src_alpha_map_bpp, (jshort)lpStruct->src_alpha_map.bpp);
	(*env)->SetIntField(env, lpObject, PgAlpha_tFc.src_alpha_map_map, (jint)lpStruct->src_alpha_map.map);
	(*env)->SetByteField(env, lpObject, PgAlpha_tFc.src_global_alpha, (jbyte)lpStruct->src_global_alpha);
	(*env)->SetByteField(env, lpObject, PgAlpha_tFc.dest_global_alpha, (jbyte)lpStruct->dest_global_alpha);
}
#endif

#ifndef NO_PgDisplaySettings_t
typedef struct PgDisplaySettings_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID mode, xres, yres, refresh, flags, reserved;
} PgDisplaySettings_t_FID_CACHE;

PgDisplaySettings_t_FID_CACHE PgDisplaySettings_tFc;

void cachePgDisplaySettings_tFields(JNIEnv *env, jobject lpObject)
{
	if (PgDisplaySettings_tFc.cached) return;
	PgDisplaySettings_tFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PgDisplaySettings_tFc.mode = (*env)->GetFieldID(env, PgDisplaySettings_tFc.clazz, "mode", "I");
	PgDisplaySettings_tFc.xres = (*env)->GetFieldID(env, PgDisplaySettings_tFc.clazz, "xres", "I");
	PgDisplaySettings_tFc.yres = (*env)->GetFieldID(env, PgDisplaySettings_tFc.clazz, "yres", "I");
	PgDisplaySettings_tFc.refresh = (*env)->GetFieldID(env, PgDisplaySettings_tFc.clazz, "refresh", "I");
	PgDisplaySettings_tFc.flags = (*env)->GetFieldID(env, PgDisplaySettings_tFc.clazz, "flags", "I");
	PgDisplaySettings_tFc.reserved = (*env)->GetFieldID(env, PgDisplaySettings_tFc.clazz, "reserved", "[I");
	PgDisplaySettings_tFc.cached = 1;
}

PgDisplaySettings_t *getPgDisplaySettings_tFields(JNIEnv *env, jobject lpObject, PgDisplaySettings_t *lpStruct)
{
	if (!PgDisplaySettings_tFc.cached) cachePgDisplaySettings_tFields(env, lpObject);
	lpStruct->mode = (*env)->GetIntField(env, lpObject, PgDisplaySettings_tFc.mode);
	lpStruct->xres = (*env)->GetIntField(env, lpObject, PgDisplaySettings_tFc.xres);
	lpStruct->yres = (*env)->GetIntField(env, lpObject, PgDisplaySettings_tFc.yres);
	lpStruct->refresh = (*env)->GetIntField(env, lpObject, PgDisplaySettings_tFc.refresh);
	lpStruct->flags = (*env)->GetIntField(env, lpObject, PgDisplaySettings_tFc.flags);
	{
	jintArray lpObject1 = (*env)->GetObjectField(env, lpObject, PgDisplaySettings_tFc.reserved);
	(*env)->GetIntArrayRegion(env, lpObject1, 0, sizeof(lpStruct->reserved) / 4, (void *)lpStruct->reserved);
	}
	return lpStruct;
}

void setPgDisplaySettings_tFields(JNIEnv *env, jobject lpObject, PgDisplaySettings_t *lpStruct)
{
	if (!PgDisplaySettings_tFc.cached) cachePgDisplaySettings_tFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, PgDisplaySettings_tFc.mode, (jint)lpStruct->mode);
	(*env)->SetIntField(env, lpObject, PgDisplaySettings_tFc.xres, (jint)lpStruct->xres);
	(*env)->SetIntField(env, lpObject, PgDisplaySettings_tFc.yres, (jint)lpStruct->yres);
	(*env)->SetIntField(env, lpObject, PgDisplaySettings_tFc.refresh, (jint)lpStruct->refresh);
	(*env)->SetIntField(env, lpObject, PgDisplaySettings_tFc.flags, (jint)lpStruct->flags);
	{
	jintArray lpObject1 = (*env)->GetObjectField(env, lpObject, PgDisplaySettings_tFc.reserved);
	(*env)->SetIntArrayRegion(env, lpObject1, 0, sizeof(lpStruct->reserved) / 4, (void *)lpStruct->reserved);
	}
}
#endif

#ifndef NO_PgMap_t
typedef struct PgMap_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID dim_w, dim_h, bpl, bpp, map;
} PgMap_t_FID_CACHE;

PgMap_t_FID_CACHE PgMap_tFc;

void cachePgMap_tFields(JNIEnv *env, jobject lpObject)
{
	if (PgMap_tFc.cached) return;
	PgMap_tFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PgMap_tFc.dim_w = (*env)->GetFieldID(env, PgMap_tFc.clazz, "dim_w", "S");
	PgMap_tFc.dim_h = (*env)->GetFieldID(env, PgMap_tFc.clazz, "dim_h", "S");
	PgMap_tFc.bpl = (*env)->GetFieldID(env, PgMap_tFc.clazz, "bpl", "S");
	PgMap_tFc.bpp = (*env)->GetFieldID(env, PgMap_tFc.clazz, "bpp", "S");
	PgMap_tFc.map = (*env)->GetFieldID(env, PgMap_tFc.clazz, "map", "I");
	PgMap_tFc.cached = 1;
}

PgMap_t *getPgMap_tFields(JNIEnv *env, jobject lpObject, PgMap_t *lpStruct)
{
	if (!PgMap_tFc.cached) cachePgMap_tFields(env, lpObject);
	lpStruct->dim.w = (*env)->GetShortField(env, lpObject, PgMap_tFc.dim_w);
	lpStruct->dim.h = (*env)->GetShortField(env, lpObject, PgMap_tFc.dim_h);
	lpStruct->bpl = (*env)->GetShortField(env, lpObject, PgMap_tFc.bpl);
	lpStruct->bpp = (*env)->GetShortField(env, lpObject, PgMap_tFc.bpp);
	lpStruct->map = (char *)(*env)->GetIntField(env, lpObject, PgMap_tFc.map);
	return lpStruct;
}

void setPgMap_tFields(JNIEnv *env, jobject lpObject, PgMap_t *lpStruct)
{
	if (!PgMap_tFc.cached) cachePgMap_tFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, PgMap_tFc.dim_w, (jshort)lpStruct->dim.w);
	(*env)->SetShortField(env, lpObject, PgMap_tFc.dim_h, (jshort)lpStruct->dim.h);
	(*env)->SetShortField(env, lpObject, PgMap_tFc.bpl, (jshort)lpStruct->bpl);
	(*env)->SetShortField(env, lpObject, PgMap_tFc.bpp, (jshort)lpStruct->bpp);
	(*env)->SetIntField(env, lpObject, PgMap_tFc.map, (jint)lpStruct->map);
}
#endif

#ifndef NO_PgVideoModeInfo_t
typedef struct PgVideoModeInfo_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID width, height, bits_per_pixel, bytes_per_scanline, type, mode_capabilities1, mode_capabilities2, mode_capabilities3, mode_capabilities4, mode_capabilities5, mode_capabilities6, refresh_rates;
} PgVideoModeInfo_t_FID_CACHE;

PgVideoModeInfo_t_FID_CACHE PgVideoModeInfo_tFc;

void cachePgVideoModeInfo_tFields(JNIEnv *env, jobject lpObject)
{
	if (PgVideoModeInfo_tFc.cached) return;
	PgVideoModeInfo_tFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PgVideoModeInfo_tFc.width = (*env)->GetFieldID(env, PgVideoModeInfo_tFc.clazz, "width", "S");
	PgVideoModeInfo_tFc.height = (*env)->GetFieldID(env, PgVideoModeInfo_tFc.clazz, "height", "S");
	PgVideoModeInfo_tFc.bits_per_pixel = (*env)->GetFieldID(env, PgVideoModeInfo_tFc.clazz, "bits_per_pixel", "S");
	PgVideoModeInfo_tFc.bytes_per_scanline = (*env)->GetFieldID(env, PgVideoModeInfo_tFc.clazz, "bytes_per_scanline", "S");
	PgVideoModeInfo_tFc.type = (*env)->GetFieldID(env, PgVideoModeInfo_tFc.clazz, "type", "I");
	PgVideoModeInfo_tFc.mode_capabilities1 = (*env)->GetFieldID(env, PgVideoModeInfo_tFc.clazz, "mode_capabilities1", "I");
	PgVideoModeInfo_tFc.mode_capabilities2 = (*env)->GetFieldID(env, PgVideoModeInfo_tFc.clazz, "mode_capabilities2", "I");
	PgVideoModeInfo_tFc.mode_capabilities3 = (*env)->GetFieldID(env, PgVideoModeInfo_tFc.clazz, "mode_capabilities3", "I");
	PgVideoModeInfo_tFc.mode_capabilities4 = (*env)->GetFieldID(env, PgVideoModeInfo_tFc.clazz, "mode_capabilities4", "I");
	PgVideoModeInfo_tFc.mode_capabilities5 = (*env)->GetFieldID(env, PgVideoModeInfo_tFc.clazz, "mode_capabilities5", "I");
	PgVideoModeInfo_tFc.mode_capabilities6 = (*env)->GetFieldID(env, PgVideoModeInfo_tFc.clazz, "mode_capabilities6", "I");
	PgVideoModeInfo_tFc.refresh_rates = (*env)->GetFieldID(env, PgVideoModeInfo_tFc.clazz, "refresh_rates", "[B");
	PgVideoModeInfo_tFc.cached = 1;
}

PgVideoModeInfo_t *getPgVideoModeInfo_tFields(JNIEnv *env, jobject lpObject, PgVideoModeInfo_t *lpStruct)
{
	if (!PgVideoModeInfo_tFc.cached) cachePgVideoModeInfo_tFields(env, lpObject);
	lpStruct->width = (*env)->GetShortField(env, lpObject, PgVideoModeInfo_tFc.width);
	lpStruct->height = (*env)->GetShortField(env, lpObject, PgVideoModeInfo_tFc.height);
	lpStruct->bits_per_pixel = (*env)->GetShortField(env, lpObject, PgVideoModeInfo_tFc.bits_per_pixel);
	lpStruct->bytes_per_scanline = (*env)->GetShortField(env, lpObject, PgVideoModeInfo_tFc.bytes_per_scanline);
	lpStruct->type = (*env)->GetIntField(env, lpObject, PgVideoModeInfo_tFc.type);
	lpStruct->mode_capabilities1 = (*env)->GetIntField(env, lpObject, PgVideoModeInfo_tFc.mode_capabilities1);
	lpStruct->mode_capabilities2 = (*env)->GetIntField(env, lpObject, PgVideoModeInfo_tFc.mode_capabilities2);
	lpStruct->mode_capabilities3 = (*env)->GetIntField(env, lpObject, PgVideoModeInfo_tFc.mode_capabilities3);
	lpStruct->mode_capabilities4 = (*env)->GetIntField(env, lpObject, PgVideoModeInfo_tFc.mode_capabilities4);
	lpStruct->mode_capabilities5 = (*env)->GetIntField(env, lpObject, PgVideoModeInfo_tFc.mode_capabilities5);
	lpStruct->mode_capabilities6 = (*env)->GetIntField(env, lpObject, PgVideoModeInfo_tFc.mode_capabilities6);
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, PgVideoModeInfo_tFc.refresh_rates);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->refresh_rates), (void *)lpStruct->refresh_rates);
	}
	return lpStruct;
}

void setPgVideoModeInfo_tFields(JNIEnv *env, jobject lpObject, PgVideoModeInfo_t *lpStruct)
{
	if (!PgVideoModeInfo_tFc.cached) cachePgVideoModeInfo_tFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, PgVideoModeInfo_tFc.width, (jshort)lpStruct->width);
	(*env)->SetShortField(env, lpObject, PgVideoModeInfo_tFc.height, (jshort)lpStruct->height);
	(*env)->SetShortField(env, lpObject, PgVideoModeInfo_tFc.bits_per_pixel, (jshort)lpStruct->bits_per_pixel);
	(*env)->SetShortField(env, lpObject, PgVideoModeInfo_tFc.bytes_per_scanline, (jshort)lpStruct->bytes_per_scanline);
	(*env)->SetIntField(env, lpObject, PgVideoModeInfo_tFc.type, (jint)lpStruct->type);
	(*env)->SetIntField(env, lpObject, PgVideoModeInfo_tFc.mode_capabilities1, (jint)lpStruct->mode_capabilities1);
	(*env)->SetIntField(env, lpObject, PgVideoModeInfo_tFc.mode_capabilities2, (jint)lpStruct->mode_capabilities2);
	(*env)->SetIntField(env, lpObject, PgVideoModeInfo_tFc.mode_capabilities3, (jint)lpStruct->mode_capabilities3);
	(*env)->SetIntField(env, lpObject, PgVideoModeInfo_tFc.mode_capabilities4, (jint)lpStruct->mode_capabilities4);
	(*env)->SetIntField(env, lpObject, PgVideoModeInfo_tFc.mode_capabilities5, (jint)lpStruct->mode_capabilities5);
	(*env)->SetIntField(env, lpObject, PgVideoModeInfo_tFc.mode_capabilities6, (jint)lpStruct->mode_capabilities6);
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, PgVideoModeInfo_tFc.refresh_rates);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->refresh_rates), (void *)lpStruct->refresh_rates);
	}
}
#endif

#ifndef NO_PhArea_t
typedef struct PhArea_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID pos_x, pos_y, size_w, size_h;
} PhArea_t_FID_CACHE;

PhArea_t_FID_CACHE PhArea_tFc;

void cachePhArea_tFields(JNIEnv *env, jobject lpObject)
{
	if (PhArea_tFc.cached) return;
	PhArea_tFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PhArea_tFc.pos_x = (*env)->GetFieldID(env, PhArea_tFc.clazz, "pos_x", "S");
	PhArea_tFc.pos_y = (*env)->GetFieldID(env, PhArea_tFc.clazz, "pos_y", "S");
	PhArea_tFc.size_w = (*env)->GetFieldID(env, PhArea_tFc.clazz, "size_w", "S");
	PhArea_tFc.size_h = (*env)->GetFieldID(env, PhArea_tFc.clazz, "size_h", "S");
	PhArea_tFc.cached = 1;
}

PhArea_t *getPhArea_tFields(JNIEnv *env, jobject lpObject, PhArea_t *lpStruct)
{
	if (!PhArea_tFc.cached) cachePhArea_tFields(env, lpObject);
	lpStruct->pos.x = (*env)->GetShortField(env, lpObject, PhArea_tFc.pos_x);
	lpStruct->pos.y = (*env)->GetShortField(env, lpObject, PhArea_tFc.pos_y);
	lpStruct->size.w = (*env)->GetShortField(env, lpObject, PhArea_tFc.size_w);
	lpStruct->size.h = (*env)->GetShortField(env, lpObject, PhArea_tFc.size_h);
	return lpStruct;
}

void setPhArea_tFields(JNIEnv *env, jobject lpObject, PhArea_t *lpStruct)
{
	if (!PhArea_tFc.cached) cachePhArea_tFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, PhArea_tFc.pos_x, (jshort)lpStruct->pos.x);
	(*env)->SetShortField(env, lpObject, PhArea_tFc.pos_y, (jshort)lpStruct->pos.y);
	(*env)->SetShortField(env, lpObject, PhArea_tFc.size_w, (jshort)lpStruct->size.w);
	(*env)->SetShortField(env, lpObject, PhArea_tFc.size_h, (jshort)lpStruct->size.h);
}
#endif

#ifndef NO_PhClipHeader
typedef struct PhClipHeader_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID type, length, zero, data;
} PhClipHeader_FID_CACHE;

PhClipHeader_FID_CACHE PhClipHeaderFc;

void cachePhClipHeaderFields(JNIEnv *env, jobject lpObject)
{
	if (PhClipHeaderFc.cached) return;
	PhClipHeaderFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PhClipHeaderFc.type = (*env)->GetFieldID(env, PhClipHeaderFc.clazz, "type", "[B");
	PhClipHeaderFc.length = (*env)->GetFieldID(env, PhClipHeaderFc.clazz, "length", "S");
	PhClipHeaderFc.zero = (*env)->GetFieldID(env, PhClipHeaderFc.clazz, "zero", "S");
	PhClipHeaderFc.data = (*env)->GetFieldID(env, PhClipHeaderFc.clazz, "data", "I");
	PhClipHeaderFc.cached = 1;
}

PhClipHeader *getPhClipHeaderFields(JNIEnv *env, jobject lpObject, PhClipHeader *lpStruct)
{
	if (!PhClipHeaderFc.cached) cachePhClipHeaderFields(env, lpObject);
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, PhClipHeaderFc.type);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->type), (void *)lpStruct->type);
	}
	lpStruct->length = (*env)->GetShortField(env, lpObject, PhClipHeaderFc.length);
	lpStruct->zero = (*env)->GetShortField(env, lpObject, PhClipHeaderFc.zero);
	lpStruct->data = (void *)(*env)->GetIntField(env, lpObject, PhClipHeaderFc.data);
	return lpStruct;
}

void setPhClipHeaderFields(JNIEnv *env, jobject lpObject, PhClipHeader *lpStruct)
{
	if (!PhClipHeaderFc.cached) cachePhClipHeaderFields(env, lpObject);
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, PhClipHeaderFc.type);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->type), (void *)lpStruct->type);
	}
	(*env)->SetShortField(env, lpObject, PhClipHeaderFc.length, (jshort)lpStruct->length);
	(*env)->SetShortField(env, lpObject, PhClipHeaderFc.zero, (jshort)lpStruct->zero);
	(*env)->SetIntField(env, lpObject, PhClipHeaderFc.data, (jint)lpStruct->data);
}
#endif

#ifndef NO_PhCursorDef_t
typedef struct PhCursorDef_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID hdr_len, hdr_type, size1_x, size1_y, offset1_x, offset1_y, color1, bytesperline1, size2_x, size2_y, offset2_x, offset2_y, color2, bytesperline2;
} PhCursorDef_t_FID_CACHE;

PhCursorDef_t_FID_CACHE PhCursorDef_tFc;

void cachePhCursorDef_tFields(JNIEnv *env, jobject lpObject)
{
	if (PhCursorDef_tFc.cached) return;
	PhCursorDef_tFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PhCursorDef_tFc.hdr_len = (*env)->GetFieldID(env, PhCursorDef_tFc.clazz, "hdr_len", "S");
	PhCursorDef_tFc.hdr_type = (*env)->GetFieldID(env, PhCursorDef_tFc.clazz, "hdr_type", "S");
	PhCursorDef_tFc.size1_x = (*env)->GetFieldID(env, PhCursorDef_tFc.clazz, "size1_x", "S");
	PhCursorDef_tFc.size1_y = (*env)->GetFieldID(env, PhCursorDef_tFc.clazz, "size1_y", "S");
	PhCursorDef_tFc.offset1_x = (*env)->GetFieldID(env, PhCursorDef_tFc.clazz, "offset1_x", "S");
	PhCursorDef_tFc.offset1_y = (*env)->GetFieldID(env, PhCursorDef_tFc.clazz, "offset1_y", "S");
	PhCursorDef_tFc.color1 = (*env)->GetFieldID(env, PhCursorDef_tFc.clazz, "color1", "I");
	PhCursorDef_tFc.bytesperline1 = (*env)->GetFieldID(env, PhCursorDef_tFc.clazz, "bytesperline1", "B");
	PhCursorDef_tFc.size2_x = (*env)->GetFieldID(env, PhCursorDef_tFc.clazz, "size2_x", "S");
	PhCursorDef_tFc.size2_y = (*env)->GetFieldID(env, PhCursorDef_tFc.clazz, "size2_y", "S");
	PhCursorDef_tFc.offset2_x = (*env)->GetFieldID(env, PhCursorDef_tFc.clazz, "offset2_x", "S");
	PhCursorDef_tFc.offset2_y = (*env)->GetFieldID(env, PhCursorDef_tFc.clazz, "offset2_y", "S");
	PhCursorDef_tFc.color2 = (*env)->GetFieldID(env, PhCursorDef_tFc.clazz, "color2", "I");
	PhCursorDef_tFc.bytesperline2 = (*env)->GetFieldID(env, PhCursorDef_tFc.clazz, "bytesperline2", "B");
	PhCursorDef_tFc.cached = 1;
}

PhCursorDef_t *getPhCursorDef_tFields(JNIEnv *env, jobject lpObject, PhCursorDef_t *lpStruct)
{
	if (!PhCursorDef_tFc.cached) cachePhCursorDef_tFields(env, lpObject);
	lpStruct->hdr.len = (*env)->GetShortField(env, lpObject, PhCursorDef_tFc.hdr_len);
	lpStruct->hdr.type = (*env)->GetShortField(env, lpObject, PhCursorDef_tFc.hdr_type);
	lpStruct->size1.x = (*env)->GetShortField(env, lpObject, PhCursorDef_tFc.size1_x);
	lpStruct->size1.y = (*env)->GetShortField(env, lpObject, PhCursorDef_tFc.size1_y);
	lpStruct->offset1.x = (*env)->GetShortField(env, lpObject, PhCursorDef_tFc.offset1_x);
	lpStruct->offset1.y = (*env)->GetShortField(env, lpObject, PhCursorDef_tFc.offset1_y);
	lpStruct->color1 = (*env)->GetIntField(env, lpObject, PhCursorDef_tFc.color1);
	lpStruct->bytesperline1 = (*env)->GetByteField(env, lpObject, PhCursorDef_tFc.bytesperline1);
	lpStruct->size2.x = (*env)->GetShortField(env, lpObject, PhCursorDef_tFc.size2_x);
	lpStruct->size2.y = (*env)->GetShortField(env, lpObject, PhCursorDef_tFc.size2_y);
	lpStruct->offset2.x = (*env)->GetShortField(env, lpObject, PhCursorDef_tFc.offset2_x);
	lpStruct->offset2.y = (*env)->GetShortField(env, lpObject, PhCursorDef_tFc.offset2_y);
	lpStruct->color2 = (*env)->GetIntField(env, lpObject, PhCursorDef_tFc.color2);
	lpStruct->bytesperline2 = (*env)->GetByteField(env, lpObject, PhCursorDef_tFc.bytesperline2);
	return lpStruct;
}

void setPhCursorDef_tFields(JNIEnv *env, jobject lpObject, PhCursorDef_t *lpStruct)
{
	if (!PhCursorDef_tFc.cached) cachePhCursorDef_tFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, PhCursorDef_tFc.hdr_len, (jshort)lpStruct->hdr.len);
	(*env)->SetShortField(env, lpObject, PhCursorDef_tFc.hdr_type, (jshort)lpStruct->hdr.type);
	(*env)->SetShortField(env, lpObject, PhCursorDef_tFc.size1_x, (jshort)lpStruct->size1.x);
	(*env)->SetShortField(env, lpObject, PhCursorDef_tFc.size1_y, (jshort)lpStruct->size1.y);
	(*env)->SetShortField(env, lpObject, PhCursorDef_tFc.offset1_x, (jshort)lpStruct->offset1.x);
	(*env)->SetShortField(env, lpObject, PhCursorDef_tFc.offset1_y, (jshort)lpStruct->offset1.y);
	(*env)->SetIntField(env, lpObject, PhCursorDef_tFc.color1, (jint)lpStruct->color1);
	(*env)->SetByteField(env, lpObject, PhCursorDef_tFc.bytesperline1, (jbyte)lpStruct->bytesperline1);
	(*env)->SetShortField(env, lpObject, PhCursorDef_tFc.size2_x, (jshort)lpStruct->size2.x);
	(*env)->SetShortField(env, lpObject, PhCursorDef_tFc.size2_y, (jshort)lpStruct->size2.y);
	(*env)->SetShortField(env, lpObject, PhCursorDef_tFc.offset2_x, (jshort)lpStruct->offset2.x);
	(*env)->SetShortField(env, lpObject, PhCursorDef_tFc.offset2_y, (jshort)lpStruct->offset2.y);
	(*env)->SetIntField(env, lpObject, PhCursorDef_tFc.color2, (jint)lpStruct->color2);
	(*env)->SetByteField(env, lpObject, PhCursorDef_tFc.bytesperline2, (jbyte)lpStruct->bytesperline2);
}
#endif

#ifndef NO_PhCursorInfo_t
typedef struct PhCursorInfo_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID pos_x, pos_y, region, ig_region, color, last_press_x, last_press_y, msec, steady_x, steady_y, dragger, drag_boundary_ul_x, drag_boundary_ul_y, drag_boundary_lr_x, drag_boundary_lr_y, phantom_rid, type, ig, button_state, click_count, zero1, key_mods, zero2;
} PhCursorInfo_t_FID_CACHE;

PhCursorInfo_t_FID_CACHE PhCursorInfo_tFc;

void cachePhCursorInfo_tFields(JNIEnv *env, jobject lpObject)
{
	if (PhCursorInfo_tFc.cached) return;
	PhCursorInfo_tFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PhCursorInfo_tFc.pos_x = (*env)->GetFieldID(env, PhCursorInfo_tFc.clazz, "pos_x", "S");
	PhCursorInfo_tFc.pos_y = (*env)->GetFieldID(env, PhCursorInfo_tFc.clazz, "pos_y", "S");
	PhCursorInfo_tFc.region = (*env)->GetFieldID(env, PhCursorInfo_tFc.clazz, "region", "I");
	PhCursorInfo_tFc.ig_region = (*env)->GetFieldID(env, PhCursorInfo_tFc.clazz, "ig_region", "I");
	PhCursorInfo_tFc.color = (*env)->GetFieldID(env, PhCursorInfo_tFc.clazz, "color", "I");
	PhCursorInfo_tFc.last_press_x = (*env)->GetFieldID(env, PhCursorInfo_tFc.clazz, "last_press_x", "S");
	PhCursorInfo_tFc.last_press_y = (*env)->GetFieldID(env, PhCursorInfo_tFc.clazz, "last_press_y", "S");
	PhCursorInfo_tFc.msec = (*env)->GetFieldID(env, PhCursorInfo_tFc.clazz, "msec", "I");
	PhCursorInfo_tFc.steady_x = (*env)->GetFieldID(env, PhCursorInfo_tFc.clazz, "steady_x", "S");
	PhCursorInfo_tFc.steady_y = (*env)->GetFieldID(env, PhCursorInfo_tFc.clazz, "steady_y", "S");
	PhCursorInfo_tFc.dragger = (*env)->GetFieldID(env, PhCursorInfo_tFc.clazz, "dragger", "I");
	PhCursorInfo_tFc.drag_boundary_ul_x = (*env)->GetFieldID(env, PhCursorInfo_tFc.clazz, "drag_boundary_ul_x", "S");
	PhCursorInfo_tFc.drag_boundary_ul_y = (*env)->GetFieldID(env, PhCursorInfo_tFc.clazz, "drag_boundary_ul_y", "S");
	PhCursorInfo_tFc.drag_boundary_lr_x = (*env)->GetFieldID(env, PhCursorInfo_tFc.clazz, "drag_boundary_lr_x", "S");
	PhCursorInfo_tFc.drag_boundary_lr_y = (*env)->GetFieldID(env, PhCursorInfo_tFc.clazz, "drag_boundary_lr_y", "S");
	PhCursorInfo_tFc.phantom_rid = (*env)->GetFieldID(env, PhCursorInfo_tFc.clazz, "phantom_rid", "I");
	PhCursorInfo_tFc.type = (*env)->GetFieldID(env, PhCursorInfo_tFc.clazz, "type", "S");
	PhCursorInfo_tFc.ig = (*env)->GetFieldID(env, PhCursorInfo_tFc.clazz, "ig", "S");
	PhCursorInfo_tFc.button_state = (*env)->GetFieldID(env, PhCursorInfo_tFc.clazz, "button_state", "S");
	PhCursorInfo_tFc.click_count = (*env)->GetFieldID(env, PhCursorInfo_tFc.clazz, "click_count", "B");
	PhCursorInfo_tFc.zero1 = (*env)->GetFieldID(env, PhCursorInfo_tFc.clazz, "zero1", "B");
	PhCursorInfo_tFc.key_mods = (*env)->GetFieldID(env, PhCursorInfo_tFc.clazz, "key_mods", "I");
	PhCursorInfo_tFc.zero2 = (*env)->GetFieldID(env, PhCursorInfo_tFc.clazz, "zero2", "I");
	PhCursorInfo_tFc.cached = 1;
}

PhCursorInfo_t *getPhCursorInfo_tFields(JNIEnv *env, jobject lpObject, PhCursorInfo_t *lpStruct)
{
	if (!PhCursorInfo_tFc.cached) cachePhCursorInfo_tFields(env, lpObject);
	lpStruct->pos.x = (*env)->GetShortField(env, lpObject, PhCursorInfo_tFc.pos_x);
	lpStruct->pos.y = (*env)->GetShortField(env, lpObject, PhCursorInfo_tFc.pos_y);
	lpStruct->region = (*env)->GetIntField(env, lpObject, PhCursorInfo_tFc.region);
	lpStruct->ig_region = (*env)->GetIntField(env, lpObject, PhCursorInfo_tFc.ig_region);
	lpStruct->color = (*env)->GetIntField(env, lpObject, PhCursorInfo_tFc.color);
	lpStruct->last_press.x = (*env)->GetShortField(env, lpObject, PhCursorInfo_tFc.last_press_x);
	lpStruct->last_press.y = (*env)->GetShortField(env, lpObject, PhCursorInfo_tFc.last_press_y);
	lpStruct->msec = (*env)->GetIntField(env, lpObject, PhCursorInfo_tFc.msec);
	lpStruct->steady.x = (*env)->GetShortField(env, lpObject, PhCursorInfo_tFc.steady_x);
	lpStruct->steady.y = (*env)->GetShortField(env, lpObject, PhCursorInfo_tFc.steady_y);
	lpStruct->dragger = (*env)->GetIntField(env, lpObject, PhCursorInfo_tFc.dragger);
	lpStruct->drag_boundary.ul.x = (*env)->GetShortField(env, lpObject, PhCursorInfo_tFc.drag_boundary_ul_x);
	lpStruct->drag_boundary.ul.y = (*env)->GetShortField(env, lpObject, PhCursorInfo_tFc.drag_boundary_ul_y);
	lpStruct->drag_boundary.lr.x = (*env)->GetShortField(env, lpObject, PhCursorInfo_tFc.drag_boundary_lr_x);
	lpStruct->drag_boundary.lr.y = (*env)->GetShortField(env, lpObject, PhCursorInfo_tFc.drag_boundary_lr_y);
	lpStruct->phantom_rid = (*env)->GetIntField(env, lpObject, PhCursorInfo_tFc.phantom_rid);
	lpStruct->type = (*env)->GetShortField(env, lpObject, PhCursorInfo_tFc.type);
	lpStruct->ig = (*env)->GetShortField(env, lpObject, PhCursorInfo_tFc.ig);
	lpStruct->button_state = (*env)->GetShortField(env, lpObject, PhCursorInfo_tFc.button_state);
	lpStruct->click_count = (*env)->GetByteField(env, lpObject, PhCursorInfo_tFc.click_count);
	lpStruct->zero1 = (*env)->GetByteField(env, lpObject, PhCursorInfo_tFc.zero1);
	lpStruct->key_mods = (*env)->GetIntField(env, lpObject, PhCursorInfo_tFc.key_mods);
	lpStruct->zero2 = (*env)->GetIntField(env, lpObject, PhCursorInfo_tFc.zero2);
	return lpStruct;
}

void setPhCursorInfo_tFields(JNIEnv *env, jobject lpObject, PhCursorInfo_t *lpStruct)
{
	if (!PhCursorInfo_tFc.cached) cachePhCursorInfo_tFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, PhCursorInfo_tFc.pos_x, (jshort)lpStruct->pos.x);
	(*env)->SetShortField(env, lpObject, PhCursorInfo_tFc.pos_y, (jshort)lpStruct->pos.y);
	(*env)->SetIntField(env, lpObject, PhCursorInfo_tFc.region, (jint)lpStruct->region);
	(*env)->SetIntField(env, lpObject, PhCursorInfo_tFc.ig_region, (jint)lpStruct->ig_region);
	(*env)->SetIntField(env, lpObject, PhCursorInfo_tFc.color, (jint)lpStruct->color);
	(*env)->SetShortField(env, lpObject, PhCursorInfo_tFc.last_press_x, (jshort)lpStruct->last_press.x);
	(*env)->SetShortField(env, lpObject, PhCursorInfo_tFc.last_press_y, (jshort)lpStruct->last_press.y);
	(*env)->SetIntField(env, lpObject, PhCursorInfo_tFc.msec, (jint)lpStruct->msec);
	(*env)->SetShortField(env, lpObject, PhCursorInfo_tFc.steady_x, (jshort)lpStruct->steady.x);
	(*env)->SetShortField(env, lpObject, PhCursorInfo_tFc.steady_y, (jshort)lpStruct->steady.y);
	(*env)->SetIntField(env, lpObject, PhCursorInfo_tFc.dragger, (jint)lpStruct->dragger);
	(*env)->SetShortField(env, lpObject, PhCursorInfo_tFc.drag_boundary_ul_x, (jshort)lpStruct->drag_boundary.ul.x);
	(*env)->SetShortField(env, lpObject, PhCursorInfo_tFc.drag_boundary_ul_y, (jshort)lpStruct->drag_boundary.ul.y);
	(*env)->SetShortField(env, lpObject, PhCursorInfo_tFc.drag_boundary_lr_x, (jshort)lpStruct->drag_boundary.lr.x);
	(*env)->SetShortField(env, lpObject, PhCursorInfo_tFc.drag_boundary_lr_y, (jshort)lpStruct->drag_boundary.lr.y);
	(*env)->SetIntField(env, lpObject, PhCursorInfo_tFc.phantom_rid, (jint)lpStruct->phantom_rid);
	(*env)->SetShortField(env, lpObject, PhCursorInfo_tFc.type, (jshort)lpStruct->type);
	(*env)->SetShortField(env, lpObject, PhCursorInfo_tFc.ig, (jshort)lpStruct->ig);
	(*env)->SetShortField(env, lpObject, PhCursorInfo_tFc.button_state, (jshort)lpStruct->button_state);
	(*env)->SetByteField(env, lpObject, PhCursorInfo_tFc.click_count, (jbyte)lpStruct->click_count);
	(*env)->SetByteField(env, lpObject, PhCursorInfo_tFc.zero1, (jbyte)lpStruct->zero1);
	(*env)->SetIntField(env, lpObject, PhCursorInfo_tFc.key_mods, (jint)lpStruct->key_mods);
	(*env)->SetIntField(env, lpObject, PhCursorInfo_tFc.zero2, (jint)lpStruct->zero2);
}
#endif

#ifndef NO_PhDim_t
typedef struct PhDim_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID w, h;
} PhDim_t_FID_CACHE;

PhDim_t_FID_CACHE PhDim_tFc;

void cachePhDim_tFields(JNIEnv *env, jobject lpObject)
{
	if (PhDim_tFc.cached) return;
	PhDim_tFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PhDim_tFc.w = (*env)->GetFieldID(env, PhDim_tFc.clazz, "w", "S");
	PhDim_tFc.h = (*env)->GetFieldID(env, PhDim_tFc.clazz, "h", "S");
	PhDim_tFc.cached = 1;
}

PhDim_t *getPhDim_tFields(JNIEnv *env, jobject lpObject, PhDim_t *lpStruct)
{
	if (!PhDim_tFc.cached) cachePhDim_tFields(env, lpObject);
	lpStruct->w = (*env)->GetShortField(env, lpObject, PhDim_tFc.w);
	lpStruct->h = (*env)->GetShortField(env, lpObject, PhDim_tFc.h);
	return lpStruct;
}

void setPhDim_tFields(JNIEnv *env, jobject lpObject, PhDim_t *lpStruct)
{
	if (!PhDim_tFc.cached) cachePhDim_tFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, PhDim_tFc.w, (jshort)lpStruct->w);
	(*env)->SetShortField(env, lpObject, PhDim_tFc.h, (jshort)lpStruct->h);
}
#endif

#ifndef NO_PhEvent_t
typedef struct PhEvent_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID type, subtype, processing_flags, emitter_rid, emitter_handle, collector_rid, collector_handle, input_group, flags, timestamp, translation_x, translation_y, num_rects, data_len;
} PhEvent_t_FID_CACHE;

PhEvent_t_FID_CACHE PhEvent_tFc;

void cachePhEvent_tFields(JNIEnv *env, jobject lpObject)
{
	if (PhEvent_tFc.cached) return;
	PhEvent_tFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PhEvent_tFc.type = (*env)->GetFieldID(env, PhEvent_tFc.clazz, "type", "I");
	PhEvent_tFc.subtype = (*env)->GetFieldID(env, PhEvent_tFc.clazz, "subtype", "S");
	PhEvent_tFc.processing_flags = (*env)->GetFieldID(env, PhEvent_tFc.clazz, "processing_flags", "S");
	PhEvent_tFc.emitter_rid = (*env)->GetFieldID(env, PhEvent_tFc.clazz, "emitter_rid", "I");
	PhEvent_tFc.emitter_handle = (*env)->GetFieldID(env, PhEvent_tFc.clazz, "emitter_handle", "I");
	PhEvent_tFc.collector_rid = (*env)->GetFieldID(env, PhEvent_tFc.clazz, "collector_rid", "I");
	PhEvent_tFc.collector_handle = (*env)->GetFieldID(env, PhEvent_tFc.clazz, "collector_handle", "I");
	PhEvent_tFc.input_group = (*env)->GetFieldID(env, PhEvent_tFc.clazz, "input_group", "S");
	PhEvent_tFc.flags = (*env)->GetFieldID(env, PhEvent_tFc.clazz, "flags", "S");
	PhEvent_tFc.timestamp = (*env)->GetFieldID(env, PhEvent_tFc.clazz, "timestamp", "I");
	PhEvent_tFc.translation_x = (*env)->GetFieldID(env, PhEvent_tFc.clazz, "translation_x", "S");
	PhEvent_tFc.translation_y = (*env)->GetFieldID(env, PhEvent_tFc.clazz, "translation_y", "S");
	PhEvent_tFc.num_rects = (*env)->GetFieldID(env, PhEvent_tFc.clazz, "num_rects", "S");
	PhEvent_tFc.data_len = (*env)->GetFieldID(env, PhEvent_tFc.clazz, "data_len", "S");
	PhEvent_tFc.cached = 1;
}

PhEvent_t *getPhEvent_tFields(JNIEnv *env, jobject lpObject, PhEvent_t *lpStruct)
{
	if (!PhEvent_tFc.cached) cachePhEvent_tFields(env, lpObject);
	lpStruct->type = (*env)->GetIntField(env, lpObject, PhEvent_tFc.type);
	lpStruct->subtype = (*env)->GetShortField(env, lpObject, PhEvent_tFc.subtype);
	lpStruct->processing_flags = (*env)->GetShortField(env, lpObject, PhEvent_tFc.processing_flags);
	lpStruct->emitter.rid = (*env)->GetIntField(env, lpObject, PhEvent_tFc.emitter_rid);
	lpStruct->emitter.handle = (*env)->GetIntField(env, lpObject, PhEvent_tFc.emitter_handle);
	lpStruct->collector.rid = (*env)->GetIntField(env, lpObject, PhEvent_tFc.collector_rid);
	lpStruct->collector.handle = (*env)->GetIntField(env, lpObject, PhEvent_tFc.collector_handle);
	lpStruct->input_group = (*env)->GetShortField(env, lpObject, PhEvent_tFc.input_group);
	lpStruct->flags = (*env)->GetShortField(env, lpObject, PhEvent_tFc.flags);
	lpStruct->timestamp = (*env)->GetIntField(env, lpObject, PhEvent_tFc.timestamp);
	lpStruct->translation.x = (*env)->GetShortField(env, lpObject, PhEvent_tFc.translation_x);
	lpStruct->translation.y = (*env)->GetShortField(env, lpObject, PhEvent_tFc.translation_y);
	lpStruct->num_rects = (*env)->GetShortField(env, lpObject, PhEvent_tFc.num_rects);
	lpStruct->data_len = (*env)->GetShortField(env, lpObject, PhEvent_tFc.data_len);
	return lpStruct;
}

void setPhEvent_tFields(JNIEnv *env, jobject lpObject, PhEvent_t *lpStruct)
{
	if (!PhEvent_tFc.cached) cachePhEvent_tFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, PhEvent_tFc.type, (jint)lpStruct->type);
	(*env)->SetShortField(env, lpObject, PhEvent_tFc.subtype, (jshort)lpStruct->subtype);
	(*env)->SetShortField(env, lpObject, PhEvent_tFc.processing_flags, (jshort)lpStruct->processing_flags);
	(*env)->SetIntField(env, lpObject, PhEvent_tFc.emitter_rid, (jint)lpStruct->emitter.rid);
	(*env)->SetIntField(env, lpObject, PhEvent_tFc.emitter_handle, (jint)lpStruct->emitter.handle);
	(*env)->SetIntField(env, lpObject, PhEvent_tFc.collector_rid, (jint)lpStruct->collector.rid);
	(*env)->SetIntField(env, lpObject, PhEvent_tFc.collector_handle, (jint)lpStruct->collector.handle);
	(*env)->SetShortField(env, lpObject, PhEvent_tFc.input_group, (jshort)lpStruct->input_group);
	(*env)->SetShortField(env, lpObject, PhEvent_tFc.flags, (jshort)lpStruct->flags);
	(*env)->SetIntField(env, lpObject, PhEvent_tFc.timestamp, (jint)lpStruct->timestamp);
	(*env)->SetShortField(env, lpObject, PhEvent_tFc.translation_x, (jshort)lpStruct->translation.x);
	(*env)->SetShortField(env, lpObject, PhEvent_tFc.translation_y, (jshort)lpStruct->translation.y);
	(*env)->SetShortField(env, lpObject, PhEvent_tFc.num_rects, (jshort)lpStruct->num_rects);
	(*env)->SetShortField(env, lpObject, PhEvent_tFc.data_len, (jshort)lpStruct->data_len);
}
#endif

#ifndef NO_PhImage_t
typedef struct PhImage_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID type, image_tag, bpl, size_w, size_h, palette_tag, colors, alpha, transparent, format, flags, ghost_bpl, spare1, ghost_bitmap, mask_bpl, mask_bm, palette, image;
} PhImage_t_FID_CACHE;

PhImage_t_FID_CACHE PhImage_tFc;

void cachePhImage_tFields(JNIEnv *env, jobject lpObject)
{
	if (PhImage_tFc.cached) return;
	PhImage_tFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PhImage_tFc.type = (*env)->GetFieldID(env, PhImage_tFc.clazz, "type", "I");
	PhImage_tFc.image_tag = (*env)->GetFieldID(env, PhImage_tFc.clazz, "image_tag", "I");
	PhImage_tFc.bpl = (*env)->GetFieldID(env, PhImage_tFc.clazz, "bpl", "I");
	PhImage_tFc.size_w = (*env)->GetFieldID(env, PhImage_tFc.clazz, "size_w", "S");
	PhImage_tFc.size_h = (*env)->GetFieldID(env, PhImage_tFc.clazz, "size_h", "S");
	PhImage_tFc.palette_tag = (*env)->GetFieldID(env, PhImage_tFc.clazz, "palette_tag", "I");
	PhImage_tFc.colors = (*env)->GetFieldID(env, PhImage_tFc.clazz, "colors", "I");
	PhImage_tFc.alpha = (*env)->GetFieldID(env, PhImage_tFc.clazz, "alpha", "I");
	PhImage_tFc.transparent = (*env)->GetFieldID(env, PhImage_tFc.clazz, "transparent", "I");
	PhImage_tFc.format = (*env)->GetFieldID(env, PhImage_tFc.clazz, "format", "B");
	PhImage_tFc.flags = (*env)->GetFieldID(env, PhImage_tFc.clazz, "flags", "B");
	PhImage_tFc.ghost_bpl = (*env)->GetFieldID(env, PhImage_tFc.clazz, "ghost_bpl", "B");
	PhImage_tFc.spare1 = (*env)->GetFieldID(env, PhImage_tFc.clazz, "spare1", "B");
	PhImage_tFc.ghost_bitmap = (*env)->GetFieldID(env, PhImage_tFc.clazz, "ghost_bitmap", "I");
	PhImage_tFc.mask_bpl = (*env)->GetFieldID(env, PhImage_tFc.clazz, "mask_bpl", "I");
	PhImage_tFc.mask_bm = (*env)->GetFieldID(env, PhImage_tFc.clazz, "mask_bm", "I");
	PhImage_tFc.palette = (*env)->GetFieldID(env, PhImage_tFc.clazz, "palette", "I");
	PhImage_tFc.image = (*env)->GetFieldID(env, PhImage_tFc.clazz, "image", "I");
	PhImage_tFc.cached = 1;
}

PhImage_t *getPhImage_tFields(JNIEnv *env, jobject lpObject, PhImage_t *lpStruct)
{
	if (!PhImage_tFc.cached) cachePhImage_tFields(env, lpObject);
	lpStruct->type = (*env)->GetIntField(env, lpObject, PhImage_tFc.type);
	lpStruct->image_tag = (*env)->GetIntField(env, lpObject, PhImage_tFc.image_tag);
	lpStruct->bpl = (*env)->GetIntField(env, lpObject, PhImage_tFc.bpl);
	lpStruct->size.w = (*env)->GetShortField(env, lpObject, PhImage_tFc.size_w);
	lpStruct->size.h = (*env)->GetShortField(env, lpObject, PhImage_tFc.size_h);
	lpStruct->palette_tag = (*env)->GetIntField(env, lpObject, PhImage_tFc.palette_tag);
	lpStruct->colors = (*env)->GetIntField(env, lpObject, PhImage_tFc.colors);
	lpStruct->alpha = (PgAlpha_t *)(*env)->GetIntField(env, lpObject, PhImage_tFc.alpha);
	lpStruct->transparent = (*env)->GetIntField(env, lpObject, PhImage_tFc.transparent);
	lpStruct->format = (*env)->GetByteField(env, lpObject, PhImage_tFc.format);
	lpStruct->flags = (*env)->GetByteField(env, lpObject, PhImage_tFc.flags);
	lpStruct->ghost_bpl = (*env)->GetByteField(env, lpObject, PhImage_tFc.ghost_bpl);
	lpStruct->spare1 = (*env)->GetByteField(env, lpObject, PhImage_tFc.spare1);
	lpStruct->ghost_bitmap = (char *)(*env)->GetIntField(env, lpObject, PhImage_tFc.ghost_bitmap);
	lpStruct->mask_bpl = (*env)->GetIntField(env, lpObject, PhImage_tFc.mask_bpl);
	lpStruct->mask_bm = (char *)(*env)->GetIntField(env, lpObject, PhImage_tFc.mask_bm);
	lpStruct->palette = (PgColor_t *)(*env)->GetIntField(env, lpObject, PhImage_tFc.palette);
	lpStruct->image = (char *)(*env)->GetIntField(env, lpObject, PhImage_tFc.image);
	return lpStruct;
}

void setPhImage_tFields(JNIEnv *env, jobject lpObject, PhImage_t *lpStruct)
{
	if (!PhImage_tFc.cached) cachePhImage_tFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, PhImage_tFc.type, (jint)lpStruct->type);
	(*env)->SetIntField(env, lpObject, PhImage_tFc.image_tag, (jint)lpStruct->image_tag);
	(*env)->SetIntField(env, lpObject, PhImage_tFc.bpl, (jint)lpStruct->bpl);
	(*env)->SetShortField(env, lpObject, PhImage_tFc.size_w, (jshort)lpStruct->size.w);
	(*env)->SetShortField(env, lpObject, PhImage_tFc.size_h, (jshort)lpStruct->size.h);
	(*env)->SetIntField(env, lpObject, PhImage_tFc.palette_tag, (jint)lpStruct->palette_tag);
	(*env)->SetIntField(env, lpObject, PhImage_tFc.colors, (jint)lpStruct->colors);
	(*env)->SetIntField(env, lpObject, PhImage_tFc.alpha, (jint)lpStruct->alpha);
	(*env)->SetIntField(env, lpObject, PhImage_tFc.transparent, (jint)lpStruct->transparent);
	(*env)->SetByteField(env, lpObject, PhImage_tFc.format, (jbyte)lpStruct->format);
	(*env)->SetByteField(env, lpObject, PhImage_tFc.flags, (jbyte)lpStruct->flags);
	(*env)->SetByteField(env, lpObject, PhImage_tFc.ghost_bpl, (jbyte)lpStruct->ghost_bpl);
	(*env)->SetByteField(env, lpObject, PhImage_tFc.spare1, (jbyte)lpStruct->spare1);
	(*env)->SetIntField(env, lpObject, PhImage_tFc.ghost_bitmap, (jint)lpStruct->ghost_bitmap);
	(*env)->SetIntField(env, lpObject, PhImage_tFc.mask_bpl, (jint)lpStruct->mask_bpl);
	(*env)->SetIntField(env, lpObject, PhImage_tFc.mask_bm, (jint)lpStruct->mask_bm);
	(*env)->SetIntField(env, lpObject, PhImage_tFc.palette, (jint)lpStruct->palette);
	(*env)->SetIntField(env, lpObject, PhImage_tFc.image, (jint)lpStruct->image);
}
#endif

#ifndef NO_PhKeyEvent_t
typedef struct PhKeyEvent_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID key_mods, key_flags, key_cap, key_sym, key_scan, key_zero, pos_x, pos_y, button_state;
} PhKeyEvent_t_FID_CACHE;

PhKeyEvent_t_FID_CACHE PhKeyEvent_tFc;

void cachePhKeyEvent_tFields(JNIEnv *env, jobject lpObject)
{
	if (PhKeyEvent_tFc.cached) return;
	PhKeyEvent_tFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PhKeyEvent_tFc.key_mods = (*env)->GetFieldID(env, PhKeyEvent_tFc.clazz, "key_mods", "I");
	PhKeyEvent_tFc.key_flags = (*env)->GetFieldID(env, PhKeyEvent_tFc.clazz, "key_flags", "I");
	PhKeyEvent_tFc.key_cap = (*env)->GetFieldID(env, PhKeyEvent_tFc.clazz, "key_cap", "I");
	PhKeyEvent_tFc.key_sym = (*env)->GetFieldID(env, PhKeyEvent_tFc.clazz, "key_sym", "I");
	PhKeyEvent_tFc.key_scan = (*env)->GetFieldID(env, PhKeyEvent_tFc.clazz, "key_scan", "S");
	PhKeyEvent_tFc.key_zero = (*env)->GetFieldID(env, PhKeyEvent_tFc.clazz, "key_zero", "S");
	PhKeyEvent_tFc.pos_x = (*env)->GetFieldID(env, PhKeyEvent_tFc.clazz, "pos_x", "S");
	PhKeyEvent_tFc.pos_y = (*env)->GetFieldID(env, PhKeyEvent_tFc.clazz, "pos_y", "S");
	PhKeyEvent_tFc.button_state = (*env)->GetFieldID(env, PhKeyEvent_tFc.clazz, "button_state", "S");
	PhKeyEvent_tFc.cached = 1;
}

PhKeyEvent_t *getPhKeyEvent_tFields(JNIEnv *env, jobject lpObject, PhKeyEvent_t *lpStruct)
{
	if (!PhKeyEvent_tFc.cached) cachePhKeyEvent_tFields(env, lpObject);
	lpStruct->key_mods = (*env)->GetIntField(env, lpObject, PhKeyEvent_tFc.key_mods);
	lpStruct->key_flags = (*env)->GetIntField(env, lpObject, PhKeyEvent_tFc.key_flags);
	lpStruct->key_cap = (*env)->GetIntField(env, lpObject, PhKeyEvent_tFc.key_cap);
	lpStruct->key_sym = (*env)->GetIntField(env, lpObject, PhKeyEvent_tFc.key_sym);
	lpStruct->key_scan = (*env)->GetShortField(env, lpObject, PhKeyEvent_tFc.key_scan);
	lpStruct->key_zero = (*env)->GetShortField(env, lpObject, PhKeyEvent_tFc.key_zero);
	lpStruct->pos.x = (*env)->GetShortField(env, lpObject, PhKeyEvent_tFc.pos_x);
	lpStruct->pos.y = (*env)->GetShortField(env, lpObject, PhKeyEvent_tFc.pos_y);
	lpStruct->button_state = (*env)->GetShortField(env, lpObject, PhKeyEvent_tFc.button_state);
	return lpStruct;
}

void setPhKeyEvent_tFields(JNIEnv *env, jobject lpObject, PhKeyEvent_t *lpStruct)
{
	if (!PhKeyEvent_tFc.cached) cachePhKeyEvent_tFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, PhKeyEvent_tFc.key_mods, (jint)lpStruct->key_mods);
	(*env)->SetIntField(env, lpObject, PhKeyEvent_tFc.key_flags, (jint)lpStruct->key_flags);
	(*env)->SetIntField(env, lpObject, PhKeyEvent_tFc.key_cap, (jint)lpStruct->key_cap);
	(*env)->SetIntField(env, lpObject, PhKeyEvent_tFc.key_sym, (jint)lpStruct->key_sym);
	(*env)->SetShortField(env, lpObject, PhKeyEvent_tFc.key_scan, (jshort)lpStruct->key_scan);
	(*env)->SetShortField(env, lpObject, PhKeyEvent_tFc.key_zero, (jshort)lpStruct->key_zero);
	(*env)->SetShortField(env, lpObject, PhKeyEvent_tFc.pos_x, (jshort)lpStruct->pos.x);
	(*env)->SetShortField(env, lpObject, PhKeyEvent_tFc.pos_y, (jshort)lpStruct->pos.y);
	(*env)->SetShortField(env, lpObject, PhKeyEvent_tFc.button_state, (jshort)lpStruct->button_state);
}
#endif

#ifndef NO_PhPoint_t
typedef struct PhPoint_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID x, y;
} PhPoint_t_FID_CACHE;

PhPoint_t_FID_CACHE PhPoint_tFc;

void cachePhPoint_tFields(JNIEnv *env, jobject lpObject)
{
	if (PhPoint_tFc.cached) return;
	PhPoint_tFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PhPoint_tFc.x = (*env)->GetFieldID(env, PhPoint_tFc.clazz, "x", "S");
	PhPoint_tFc.y = (*env)->GetFieldID(env, PhPoint_tFc.clazz, "y", "S");
	PhPoint_tFc.cached = 1;
}

PhPoint_t *getPhPoint_tFields(JNIEnv *env, jobject lpObject, PhPoint_t *lpStruct)
{
	if (!PhPoint_tFc.cached) cachePhPoint_tFields(env, lpObject);
	lpStruct->x = (*env)->GetShortField(env, lpObject, PhPoint_tFc.x);
	lpStruct->y = (*env)->GetShortField(env, lpObject, PhPoint_tFc.y);
	return lpStruct;
}

void setPhPoint_tFields(JNIEnv *env, jobject lpObject, PhPoint_t *lpStruct)
{
	if (!PhPoint_tFc.cached) cachePhPoint_tFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, PhPoint_tFc.x, (jshort)lpStruct->x);
	(*env)->SetShortField(env, lpObject, PhPoint_tFc.y, (jshort)lpStruct->y);
}
#endif

#ifndef NO_PhPointerEvent_t
typedef struct PhPointerEvent_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID pos_x, pos_y, buttons, button_state, click_count, flags, z, key_mods, zero;
} PhPointerEvent_t_FID_CACHE;

PhPointerEvent_t_FID_CACHE PhPointerEvent_tFc;

void cachePhPointerEvent_tFields(JNIEnv *env, jobject lpObject)
{
	if (PhPointerEvent_tFc.cached) return;
	PhPointerEvent_tFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PhPointerEvent_tFc.pos_x = (*env)->GetFieldID(env, PhPointerEvent_tFc.clazz, "pos_x", "S");
	PhPointerEvent_tFc.pos_y = (*env)->GetFieldID(env, PhPointerEvent_tFc.clazz, "pos_y", "S");
	PhPointerEvent_tFc.buttons = (*env)->GetFieldID(env, PhPointerEvent_tFc.clazz, "buttons", "S");
	PhPointerEvent_tFc.button_state = (*env)->GetFieldID(env, PhPointerEvent_tFc.clazz, "button_state", "S");
	PhPointerEvent_tFc.click_count = (*env)->GetFieldID(env, PhPointerEvent_tFc.clazz, "click_count", "B");
	PhPointerEvent_tFc.flags = (*env)->GetFieldID(env, PhPointerEvent_tFc.clazz, "flags", "B");
	PhPointerEvent_tFc.z = (*env)->GetFieldID(env, PhPointerEvent_tFc.clazz, "z", "S");
	PhPointerEvent_tFc.key_mods = (*env)->GetFieldID(env, PhPointerEvent_tFc.clazz, "key_mods", "I");
	PhPointerEvent_tFc.zero = (*env)->GetFieldID(env, PhPointerEvent_tFc.clazz, "zero", "I");
	PhPointerEvent_tFc.cached = 1;
}

PhPointerEvent_t *getPhPointerEvent_tFields(JNIEnv *env, jobject lpObject, PhPointerEvent_t *lpStruct)
{
	if (!PhPointerEvent_tFc.cached) cachePhPointerEvent_tFields(env, lpObject);
	lpStruct->pos.x = (*env)->GetShortField(env, lpObject, PhPointerEvent_tFc.pos_x);
	lpStruct->pos.y = (*env)->GetShortField(env, lpObject, PhPointerEvent_tFc.pos_y);
	lpStruct->buttons = (*env)->GetShortField(env, lpObject, PhPointerEvent_tFc.buttons);
	lpStruct->button_state = (*env)->GetShortField(env, lpObject, PhPointerEvent_tFc.button_state);
	lpStruct->click_count = (*env)->GetByteField(env, lpObject, PhPointerEvent_tFc.click_count);
	lpStruct->flags = (*env)->GetByteField(env, lpObject, PhPointerEvent_tFc.flags);
	lpStruct->z = (*env)->GetShortField(env, lpObject, PhPointerEvent_tFc.z);
	lpStruct->key_mods = (*env)->GetIntField(env, lpObject, PhPointerEvent_tFc.key_mods);
	lpStruct->zero = (*env)->GetIntField(env, lpObject, PhPointerEvent_tFc.zero);
	return lpStruct;
}

void setPhPointerEvent_tFields(JNIEnv *env, jobject lpObject, PhPointerEvent_t *lpStruct)
{
	if (!PhPointerEvent_tFc.cached) cachePhPointerEvent_tFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, PhPointerEvent_tFc.pos_x, (jshort)lpStruct->pos.x);
	(*env)->SetShortField(env, lpObject, PhPointerEvent_tFc.pos_y, (jshort)lpStruct->pos.y);
	(*env)->SetShortField(env, lpObject, PhPointerEvent_tFc.buttons, (jshort)lpStruct->buttons);
	(*env)->SetShortField(env, lpObject, PhPointerEvent_tFc.button_state, (jshort)lpStruct->button_state);
	(*env)->SetByteField(env, lpObject, PhPointerEvent_tFc.click_count, (jbyte)lpStruct->click_count);
	(*env)->SetByteField(env, lpObject, PhPointerEvent_tFc.flags, (jbyte)lpStruct->flags);
	(*env)->SetShortField(env, lpObject, PhPointerEvent_tFc.z, (jshort)lpStruct->z);
	(*env)->SetIntField(env, lpObject, PhPointerEvent_tFc.key_mods, (jint)lpStruct->key_mods);
	(*env)->SetIntField(env, lpObject, PhPointerEvent_tFc.zero, (jint)lpStruct->zero);
}
#endif

#ifndef NO_PhRect_t
typedef struct PhRect_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID ul_x, ul_y, lr_x, lr_y;
} PhRect_t_FID_CACHE;

PhRect_t_FID_CACHE PhRect_tFc;

void cachePhRect_tFields(JNIEnv *env, jobject lpObject)
{
	if (PhRect_tFc.cached) return;
	PhRect_tFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PhRect_tFc.ul_x = (*env)->GetFieldID(env, PhRect_tFc.clazz, "ul_x", "S");
	PhRect_tFc.ul_y = (*env)->GetFieldID(env, PhRect_tFc.clazz, "ul_y", "S");
	PhRect_tFc.lr_x = (*env)->GetFieldID(env, PhRect_tFc.clazz, "lr_x", "S");
	PhRect_tFc.lr_y = (*env)->GetFieldID(env, PhRect_tFc.clazz, "lr_y", "S");
	PhRect_tFc.cached = 1;
}

PhRect_t *getPhRect_tFields(JNIEnv *env, jobject lpObject, PhRect_t *lpStruct)
{
	if (!PhRect_tFc.cached) cachePhRect_tFields(env, lpObject);
	lpStruct->ul.x = (*env)->GetShortField(env, lpObject, PhRect_tFc.ul_x);
	lpStruct->ul.y = (*env)->GetShortField(env, lpObject, PhRect_tFc.ul_y);
	lpStruct->lr.x = (*env)->GetShortField(env, lpObject, PhRect_tFc.lr_x);
	lpStruct->lr.y = (*env)->GetShortField(env, lpObject, PhRect_tFc.lr_y);
	return lpStruct;
}

void setPhRect_tFields(JNIEnv *env, jobject lpObject, PhRect_t *lpStruct)
{
	if (!PhRect_tFc.cached) cachePhRect_tFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, PhRect_tFc.ul_x, (jshort)lpStruct->ul.x);
	(*env)->SetShortField(env, lpObject, PhRect_tFc.ul_y, (jshort)lpStruct->ul.y);
	(*env)->SetShortField(env, lpObject, PhRect_tFc.lr_x, (jshort)lpStruct->lr.x);
	(*env)->SetShortField(env, lpObject, PhRect_tFc.lr_y, (jshort)lpStruct->lr.y);
}
#endif

#ifndef NO_PhRegion_t
typedef struct PhRegion_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID rid, handle, owner, flags, state, events_sense, events_opaque, origin_x, origin_y, parent, child, bro_in_front, bro_behind, cursor_color, input_group, data_len, cursor_type;
} PhRegion_t_FID_CACHE;

PhRegion_t_FID_CACHE PhRegion_tFc;

void cachePhRegion_tFields(JNIEnv *env, jobject lpObject)
{
	if (PhRegion_tFc.cached) return;
	PhRegion_tFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PhRegion_tFc.rid = (*env)->GetFieldID(env, PhRegion_tFc.clazz, "rid", "I");
	PhRegion_tFc.handle = (*env)->GetFieldID(env, PhRegion_tFc.clazz, "handle", "I");
	PhRegion_tFc.owner = (*env)->GetFieldID(env, PhRegion_tFc.clazz, "owner", "I");
	PhRegion_tFc.flags = (*env)->GetFieldID(env, PhRegion_tFc.clazz, "flags", "I");
	PhRegion_tFc.state = (*env)->GetFieldID(env, PhRegion_tFc.clazz, "state", "S");
	PhRegion_tFc.events_sense = (*env)->GetFieldID(env, PhRegion_tFc.clazz, "events_sense", "I");
	PhRegion_tFc.events_opaque = (*env)->GetFieldID(env, PhRegion_tFc.clazz, "events_opaque", "I");
	PhRegion_tFc.origin_x = (*env)->GetFieldID(env, PhRegion_tFc.clazz, "origin_x", "S");
	PhRegion_tFc.origin_y = (*env)->GetFieldID(env, PhRegion_tFc.clazz, "origin_y", "S");
	PhRegion_tFc.parent = (*env)->GetFieldID(env, PhRegion_tFc.clazz, "parent", "I");
	PhRegion_tFc.child = (*env)->GetFieldID(env, PhRegion_tFc.clazz, "child", "I");
	PhRegion_tFc.bro_in_front = (*env)->GetFieldID(env, PhRegion_tFc.clazz, "bro_in_front", "I");
	PhRegion_tFc.bro_behind = (*env)->GetFieldID(env, PhRegion_tFc.clazz, "bro_behind", "I");
	PhRegion_tFc.cursor_color = (*env)->GetFieldID(env, PhRegion_tFc.clazz, "cursor_color", "I");
	PhRegion_tFc.input_group = (*env)->GetFieldID(env, PhRegion_tFc.clazz, "input_group", "S");
	PhRegion_tFc.data_len = (*env)->GetFieldID(env, PhRegion_tFc.clazz, "data_len", "S");
	PhRegion_tFc.cursor_type = (*env)->GetFieldID(env, PhRegion_tFc.clazz, "cursor_type", "S");
	PhRegion_tFc.cached = 1;
}

PhRegion_t *getPhRegion_tFields(JNIEnv *env, jobject lpObject, PhRegion_t *lpStruct)
{
	if (!PhRegion_tFc.cached) cachePhRegion_tFields(env, lpObject);
	lpStruct->rid = (*env)->GetIntField(env, lpObject, PhRegion_tFc.rid);
	lpStruct->handle = (*env)->GetIntField(env, lpObject, PhRegion_tFc.handle);
	lpStruct->owner = (*env)->GetIntField(env, lpObject, PhRegion_tFc.owner);
	lpStruct->flags = (*env)->GetIntField(env, lpObject, PhRegion_tFc.flags);
	lpStruct->state = (*env)->GetShortField(env, lpObject, PhRegion_tFc.state);
	lpStruct->events_sense = (*env)->GetIntField(env, lpObject, PhRegion_tFc.events_sense);
	lpStruct->events_opaque = (*env)->GetIntField(env, lpObject, PhRegion_tFc.events_opaque);
	lpStruct->origin.x = (*env)->GetShortField(env, lpObject, PhRegion_tFc.origin_x);
	lpStruct->origin.y = (*env)->GetShortField(env, lpObject, PhRegion_tFc.origin_y);
	lpStruct->parent = (*env)->GetIntField(env, lpObject, PhRegion_tFc.parent);
	lpStruct->child = (*env)->GetIntField(env, lpObject, PhRegion_tFc.child);
	lpStruct->bro_in_front = (*env)->GetIntField(env, lpObject, PhRegion_tFc.bro_in_front);
	lpStruct->bro_behind = (*env)->GetIntField(env, lpObject, PhRegion_tFc.bro_behind);
	lpStruct->cursor_color = (*env)->GetIntField(env, lpObject, PhRegion_tFc.cursor_color);
	lpStruct->input_group = (*env)->GetShortField(env, lpObject, PhRegion_tFc.input_group);
	lpStruct->data_len = (*env)->GetShortField(env, lpObject, PhRegion_tFc.data_len);
	lpStruct->cursor_type = (*env)->GetShortField(env, lpObject, PhRegion_tFc.cursor_type);
	return lpStruct;
}

void setPhRegion_tFields(JNIEnv *env, jobject lpObject, PhRegion_t *lpStruct)
{
	if (!PhRegion_tFc.cached) cachePhRegion_tFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, PhRegion_tFc.rid, (jint)lpStruct->rid);
	(*env)->SetIntField(env, lpObject, PhRegion_tFc.handle, (jint)lpStruct->handle);
	(*env)->SetIntField(env, lpObject, PhRegion_tFc.owner, (jint)lpStruct->owner);
	(*env)->SetIntField(env, lpObject, PhRegion_tFc.flags, (jint)lpStruct->flags);
	(*env)->SetShortField(env, lpObject, PhRegion_tFc.state, (jshort)lpStruct->state);
	(*env)->SetIntField(env, lpObject, PhRegion_tFc.events_sense, (jint)lpStruct->events_sense);
	(*env)->SetIntField(env, lpObject, PhRegion_tFc.events_opaque, (jint)lpStruct->events_opaque);
	(*env)->SetShortField(env, lpObject, PhRegion_tFc.origin_x, (jshort)lpStruct->origin.x);
	(*env)->SetShortField(env, lpObject, PhRegion_tFc.origin_y, (jshort)lpStruct->origin.y);
	(*env)->SetIntField(env, lpObject, PhRegion_tFc.parent, (jint)lpStruct->parent);
	(*env)->SetIntField(env, lpObject, PhRegion_tFc.child, (jint)lpStruct->child);
	(*env)->SetIntField(env, lpObject, PhRegion_tFc.bro_in_front, (jint)lpStruct->bro_in_front);
	(*env)->SetIntField(env, lpObject, PhRegion_tFc.bro_behind, (jint)lpStruct->bro_behind);
	(*env)->SetIntField(env, lpObject, PhRegion_tFc.cursor_color, (jint)lpStruct->cursor_color);
	(*env)->SetShortField(env, lpObject, PhRegion_tFc.input_group, (jshort)lpStruct->input_group);
	(*env)->SetShortField(env, lpObject, PhRegion_tFc.data_len, (jshort)lpStruct->data_len);
	(*env)->SetShortField(env, lpObject, PhRegion_tFc.cursor_type, (jshort)lpStruct->cursor_type);
}
#endif

#ifndef NO_PhTile_t
typedef struct PhTile_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID rect_ul_x, rect_ul_y, rect_lr_x, rect_lr_y, next;
} PhTile_t_FID_CACHE;

PhTile_t_FID_CACHE PhTile_tFc;

void cachePhTile_tFields(JNIEnv *env, jobject lpObject)
{
	if (PhTile_tFc.cached) return;
	PhTile_tFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PhTile_tFc.rect_ul_x = (*env)->GetFieldID(env, PhTile_tFc.clazz, "rect_ul_x", "S");
	PhTile_tFc.rect_ul_y = (*env)->GetFieldID(env, PhTile_tFc.clazz, "rect_ul_y", "S");
	PhTile_tFc.rect_lr_x = (*env)->GetFieldID(env, PhTile_tFc.clazz, "rect_lr_x", "S");
	PhTile_tFc.rect_lr_y = (*env)->GetFieldID(env, PhTile_tFc.clazz, "rect_lr_y", "S");
	PhTile_tFc.next = (*env)->GetFieldID(env, PhTile_tFc.clazz, "next", "I");
	PhTile_tFc.cached = 1;
}

PhTile_t *getPhTile_tFields(JNIEnv *env, jobject lpObject, PhTile_t *lpStruct)
{
	if (!PhTile_tFc.cached) cachePhTile_tFields(env, lpObject);
	lpStruct->rect.ul.x = (*env)->GetShortField(env, lpObject, PhTile_tFc.rect_ul_x);
	lpStruct->rect.ul.y = (*env)->GetShortField(env, lpObject, PhTile_tFc.rect_ul_y);
	lpStruct->rect.lr.x = (*env)->GetShortField(env, lpObject, PhTile_tFc.rect_lr_x);
	lpStruct->rect.lr.y = (*env)->GetShortField(env, lpObject, PhTile_tFc.rect_lr_y);
	lpStruct->next = (PhTile_t *)(*env)->GetIntField(env, lpObject, PhTile_tFc.next);
	return lpStruct;
}

void setPhTile_tFields(JNIEnv *env, jobject lpObject, PhTile_t *lpStruct)
{
	if (!PhTile_tFc.cached) cachePhTile_tFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, PhTile_tFc.rect_ul_x, (jshort)lpStruct->rect.ul.x);
	(*env)->SetShortField(env, lpObject, PhTile_tFc.rect_ul_y, (jshort)lpStruct->rect.ul.y);
	(*env)->SetShortField(env, lpObject, PhTile_tFc.rect_lr_x, (jshort)lpStruct->rect.lr.x);
	(*env)->SetShortField(env, lpObject, PhTile_tFc.rect_lr_y, (jshort)lpStruct->rect.lr.y);
	(*env)->SetIntField(env, lpObject, PhTile_tFc.next, (jint)lpStruct->next);
}
#endif

#ifndef NO_PhWindowEvent_t
typedef struct PhWindowEvent_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID event_f, state_f, rid, pos_x, pos_y, size_w, size_h, event_state, input_group, rsvd0, rsvd1, rsvd2, rsvd3;
} PhWindowEvent_t_FID_CACHE;

PhWindowEvent_t_FID_CACHE PhWindowEvent_tFc;

void cachePhWindowEvent_tFields(JNIEnv *env, jobject lpObject)
{
	if (PhWindowEvent_tFc.cached) return;
	PhWindowEvent_tFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PhWindowEvent_tFc.event_f = (*env)->GetFieldID(env, PhWindowEvent_tFc.clazz, "event_f", "I");
	PhWindowEvent_tFc.state_f = (*env)->GetFieldID(env, PhWindowEvent_tFc.clazz, "state_f", "I");
	PhWindowEvent_tFc.rid = (*env)->GetFieldID(env, PhWindowEvent_tFc.clazz, "rid", "I");
	PhWindowEvent_tFc.pos_x = (*env)->GetFieldID(env, PhWindowEvent_tFc.clazz, "pos_x", "S");
	PhWindowEvent_tFc.pos_y = (*env)->GetFieldID(env, PhWindowEvent_tFc.clazz, "pos_y", "S");
	PhWindowEvent_tFc.size_w = (*env)->GetFieldID(env, PhWindowEvent_tFc.clazz, "size_w", "S");
	PhWindowEvent_tFc.size_h = (*env)->GetFieldID(env, PhWindowEvent_tFc.clazz, "size_h", "S");
	PhWindowEvent_tFc.event_state = (*env)->GetFieldID(env, PhWindowEvent_tFc.clazz, "event_state", "S");
	PhWindowEvent_tFc.input_group = (*env)->GetFieldID(env, PhWindowEvent_tFc.clazz, "input_group", "S");
	PhWindowEvent_tFc.rsvd0 = (*env)->GetFieldID(env, PhWindowEvent_tFc.clazz, "rsvd0", "I");
	PhWindowEvent_tFc.rsvd1 = (*env)->GetFieldID(env, PhWindowEvent_tFc.clazz, "rsvd1", "I");
	PhWindowEvent_tFc.rsvd2 = (*env)->GetFieldID(env, PhWindowEvent_tFc.clazz, "rsvd2", "I");
	PhWindowEvent_tFc.rsvd3 = (*env)->GetFieldID(env, PhWindowEvent_tFc.clazz, "rsvd3", "I");
	PhWindowEvent_tFc.cached = 1;
}

PhWindowEvent_t *getPhWindowEvent_tFields(JNIEnv *env, jobject lpObject, PhWindowEvent_t *lpStruct)
{
	if (!PhWindowEvent_tFc.cached) cachePhWindowEvent_tFields(env, lpObject);
	lpStruct->event_f = (*env)->GetIntField(env, lpObject, PhWindowEvent_tFc.event_f);
	lpStruct->state_f = (*env)->GetIntField(env, lpObject, PhWindowEvent_tFc.state_f);
	lpStruct->rid = (*env)->GetIntField(env, lpObject, PhWindowEvent_tFc.rid);
	lpStruct->pos.x = (*env)->GetShortField(env, lpObject, PhWindowEvent_tFc.pos_x);
	lpStruct->pos.y = (*env)->GetShortField(env, lpObject, PhWindowEvent_tFc.pos_y);
	lpStruct->size.w = (*env)->GetShortField(env, lpObject, PhWindowEvent_tFc.size_w);
	lpStruct->size.h = (*env)->GetShortField(env, lpObject, PhWindowEvent_tFc.size_h);
	lpStruct->event_state = (*env)->GetShortField(env, lpObject, PhWindowEvent_tFc.event_state);
	lpStruct->input_group = (*env)->GetShortField(env, lpObject, PhWindowEvent_tFc.input_group);
	lpStruct->rsvd[0] = (*env)->GetIntField(env, lpObject, PhWindowEvent_tFc.rsvd0);
	lpStruct->rsvd[1] = (*env)->GetIntField(env, lpObject, PhWindowEvent_tFc.rsvd1);
	lpStruct->rsvd[2] = (*env)->GetIntField(env, lpObject, PhWindowEvent_tFc.rsvd2);
	lpStruct->rsvd[3] = (*env)->GetIntField(env, lpObject, PhWindowEvent_tFc.rsvd3);
	return lpStruct;
}

void setPhWindowEvent_tFields(JNIEnv *env, jobject lpObject, PhWindowEvent_t *lpStruct)
{
	if (!PhWindowEvent_tFc.cached) cachePhWindowEvent_tFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, PhWindowEvent_tFc.event_f, (jint)lpStruct->event_f);
	(*env)->SetIntField(env, lpObject, PhWindowEvent_tFc.state_f, (jint)lpStruct->state_f);
	(*env)->SetIntField(env, lpObject, PhWindowEvent_tFc.rid, (jint)lpStruct->rid);
	(*env)->SetShortField(env, lpObject, PhWindowEvent_tFc.pos_x, (jshort)lpStruct->pos.x);
	(*env)->SetShortField(env, lpObject, PhWindowEvent_tFc.pos_y, (jshort)lpStruct->pos.y);
	(*env)->SetShortField(env, lpObject, PhWindowEvent_tFc.size_w, (jshort)lpStruct->size.w);
	(*env)->SetShortField(env, lpObject, PhWindowEvent_tFc.size_h, (jshort)lpStruct->size.h);
	(*env)->SetShortField(env, lpObject, PhWindowEvent_tFc.event_state, (jshort)lpStruct->event_state);
	(*env)->SetShortField(env, lpObject, PhWindowEvent_tFc.input_group, (jshort)lpStruct->input_group);
	(*env)->SetIntField(env, lpObject, PhWindowEvent_tFc.rsvd0, (jint)lpStruct->rsvd[0]);
	(*env)->SetIntField(env, lpObject, PhWindowEvent_tFc.rsvd1, (jint)lpStruct->rsvd[1]);
	(*env)->SetIntField(env, lpObject, PhWindowEvent_tFc.rsvd2, (jint)lpStruct->rsvd[2]);
	(*env)->SetIntField(env, lpObject, PhWindowEvent_tFc.rsvd3, (jint)lpStruct->rsvd[3]);
}
#endif

#ifndef NO_PtCallbackInfo_t
typedef struct PtCallbackInfo_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID reason, reason_subtype, event, cbdata;
} PtCallbackInfo_t_FID_CACHE;

PtCallbackInfo_t_FID_CACHE PtCallbackInfo_tFc;

void cachePtCallbackInfo_tFields(JNIEnv *env, jobject lpObject)
{
	if (PtCallbackInfo_tFc.cached) return;
	PtCallbackInfo_tFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PtCallbackInfo_tFc.reason = (*env)->GetFieldID(env, PtCallbackInfo_tFc.clazz, "reason", "I");
	PtCallbackInfo_tFc.reason_subtype = (*env)->GetFieldID(env, PtCallbackInfo_tFc.clazz, "reason_subtype", "I");
	PtCallbackInfo_tFc.event = (*env)->GetFieldID(env, PtCallbackInfo_tFc.clazz, "event", "I");
	PtCallbackInfo_tFc.cbdata = (*env)->GetFieldID(env, PtCallbackInfo_tFc.clazz, "cbdata", "I");
	PtCallbackInfo_tFc.cached = 1;
}

PtCallbackInfo_t *getPtCallbackInfo_tFields(JNIEnv *env, jobject lpObject, PtCallbackInfo_t *lpStruct)
{
	if (!PtCallbackInfo_tFc.cached) cachePtCallbackInfo_tFields(env, lpObject);
	lpStruct->reason = (*env)->GetIntField(env, lpObject, PtCallbackInfo_tFc.reason);
	lpStruct->reason_subtype = (*env)->GetIntField(env, lpObject, PtCallbackInfo_tFc.reason_subtype);
	lpStruct->event = (PhEvent_t *)(*env)->GetIntField(env, lpObject, PtCallbackInfo_tFc.event);
	lpStruct->cbdata = (void *)(*env)->GetIntField(env, lpObject, PtCallbackInfo_tFc.cbdata);
	return lpStruct;
}

void setPtCallbackInfo_tFields(JNIEnv *env, jobject lpObject, PtCallbackInfo_t *lpStruct)
{
	if (!PtCallbackInfo_tFc.cached) cachePtCallbackInfo_tFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, PtCallbackInfo_tFc.reason, (jint)lpStruct->reason);
	(*env)->SetIntField(env, lpObject, PtCallbackInfo_tFc.reason_subtype, (jint)lpStruct->reason_subtype);
	(*env)->SetIntField(env, lpObject, PtCallbackInfo_tFc.event, (jint)lpStruct->event);
	(*env)->SetIntField(env, lpObject, PtCallbackInfo_tFc.cbdata, (jint)lpStruct->cbdata);
}
#endif

#ifndef NO_PtColorSelectInfo_t
typedef struct PtColorSelectInfo_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID flags, nselectors, ncolor_models, color_models, selectors, pos_x, pos_y, size_w, size_h, palette, accept_text, dismiss_text, accept_dismiss_text, apply_f, data, rgb, dialog;
} PtColorSelectInfo_t_FID_CACHE;

PtColorSelectInfo_t_FID_CACHE PtColorSelectInfo_tFc;

void cachePtColorSelectInfo_tFields(JNIEnv *env, jobject lpObject)
{
	if (PtColorSelectInfo_tFc.cached) return;
	PtColorSelectInfo_tFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PtColorSelectInfo_tFc.flags = (*env)->GetFieldID(env, PtColorSelectInfo_tFc.clazz, "flags", "S");
	PtColorSelectInfo_tFc.nselectors = (*env)->GetFieldID(env, PtColorSelectInfo_tFc.clazz, "nselectors", "B");
	PtColorSelectInfo_tFc.ncolor_models = (*env)->GetFieldID(env, PtColorSelectInfo_tFc.clazz, "ncolor_models", "B");
	PtColorSelectInfo_tFc.color_models = (*env)->GetFieldID(env, PtColorSelectInfo_tFc.clazz, "color_models", "I");
	PtColorSelectInfo_tFc.selectors = (*env)->GetFieldID(env, PtColorSelectInfo_tFc.clazz, "selectors", "I");
	PtColorSelectInfo_tFc.pos_x = (*env)->GetFieldID(env, PtColorSelectInfo_tFc.clazz, "pos_x", "S");
	PtColorSelectInfo_tFc.pos_y = (*env)->GetFieldID(env, PtColorSelectInfo_tFc.clazz, "pos_y", "S");
	PtColorSelectInfo_tFc.size_w = (*env)->GetFieldID(env, PtColorSelectInfo_tFc.clazz, "size_w", "S");
	PtColorSelectInfo_tFc.size_h = (*env)->GetFieldID(env, PtColorSelectInfo_tFc.clazz, "size_h", "S");
	PtColorSelectInfo_tFc.palette = (*env)->GetFieldID(env, PtColorSelectInfo_tFc.clazz, "palette", "I");
	PtColorSelectInfo_tFc.accept_text = (*env)->GetFieldID(env, PtColorSelectInfo_tFc.clazz, "accept_text", "I");
	PtColorSelectInfo_tFc.dismiss_text = (*env)->GetFieldID(env, PtColorSelectInfo_tFc.clazz, "dismiss_text", "I");
	PtColorSelectInfo_tFc.accept_dismiss_text = (*env)->GetFieldID(env, PtColorSelectInfo_tFc.clazz, "accept_dismiss_text", "I");
	PtColorSelectInfo_tFc.apply_f = (*env)->GetFieldID(env, PtColorSelectInfo_tFc.clazz, "apply_f", "I");
	PtColorSelectInfo_tFc.data = (*env)->GetFieldID(env, PtColorSelectInfo_tFc.clazz, "data", "I");
	PtColorSelectInfo_tFc.rgb = (*env)->GetFieldID(env, PtColorSelectInfo_tFc.clazz, "rgb", "I");
	PtColorSelectInfo_tFc.dialog = (*env)->GetFieldID(env, PtColorSelectInfo_tFc.clazz, "dialog", "I");
	PtColorSelectInfo_tFc.cached = 1;
}

PtColorSelectInfo_t *getPtColorSelectInfo_tFields(JNIEnv *env, jobject lpObject, PtColorSelectInfo_t *lpStruct)
{
	if (!PtColorSelectInfo_tFc.cached) cachePtColorSelectInfo_tFields(env, lpObject);
	lpStruct->flags = (*env)->GetShortField(env, lpObject, PtColorSelectInfo_tFc.flags);
	lpStruct->nselectors = (*env)->GetByteField(env, lpObject, PtColorSelectInfo_tFc.nselectors);
	lpStruct->ncolor_models = (*env)->GetByteField(env, lpObject, PtColorSelectInfo_tFc.ncolor_models);
	lpStruct->color_models = (PgColorModel_t **)(*env)->GetIntField(env, lpObject, PtColorSelectInfo_tFc.color_models);
	lpStruct->selectors = (PtColorSelectorSpec_t *)(*env)->GetIntField(env, lpObject, PtColorSelectInfo_tFc.selectors);
	lpStruct->pos.x = (*env)->GetShortField(env, lpObject, PtColorSelectInfo_tFc.pos_x);
	lpStruct->pos.y = (*env)->GetShortField(env, lpObject, PtColorSelectInfo_tFc.pos_y);
	lpStruct->size.w = (*env)->GetShortField(env, lpObject, PtColorSelectInfo_tFc.size_w);
	lpStruct->size.h = (*env)->GetShortField(env, lpObject, PtColorSelectInfo_tFc.size_h);
	lpStruct->palette.instance = (void *)(*env)->GetIntField(env, lpObject, PtColorSelectInfo_tFc.palette);
	lpStruct->accept_text = (char *)(*env)->GetIntField(env, lpObject, PtColorSelectInfo_tFc.accept_text);
	lpStruct->dismiss_text = (char *)(*env)->GetIntField(env, lpObject, PtColorSelectInfo_tFc.dismiss_text);
	lpStruct->accept_dismiss_text = (char *)(*env)->GetIntField(env, lpObject, PtColorSelectInfo_tFc.accept_dismiss_text);
	lpStruct->apply_f = (void *)(*env)->GetIntField(env, lpObject, PtColorSelectInfo_tFc.apply_f);
	lpStruct->data = (void *)(*env)->GetIntField(env, lpObject, PtColorSelectInfo_tFc.data);
	lpStruct->rgb = (*env)->GetIntField(env, lpObject, PtColorSelectInfo_tFc.rgb);
	lpStruct->dialog = (PtWidget_t *)(*env)->GetIntField(env, lpObject, PtColorSelectInfo_tFc.dialog);
	return lpStruct;
}

void setPtColorSelectInfo_tFields(JNIEnv *env, jobject lpObject, PtColorSelectInfo_t *lpStruct)
{
	if (!PtColorSelectInfo_tFc.cached) cachePtColorSelectInfo_tFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, PtColorSelectInfo_tFc.flags, (jshort)lpStruct->flags);
	(*env)->SetByteField(env, lpObject, PtColorSelectInfo_tFc.nselectors, (jbyte)lpStruct->nselectors);
	(*env)->SetByteField(env, lpObject, PtColorSelectInfo_tFc.ncolor_models, (jbyte)lpStruct->ncolor_models);
	(*env)->SetIntField(env, lpObject, PtColorSelectInfo_tFc.color_models, (jint)lpStruct->color_models);
	(*env)->SetIntField(env, lpObject, PtColorSelectInfo_tFc.selectors, (jint)lpStruct->selectors);
	(*env)->SetShortField(env, lpObject, PtColorSelectInfo_tFc.pos_x, (jshort)lpStruct->pos.x);
	(*env)->SetShortField(env, lpObject, PtColorSelectInfo_tFc.pos_y, (jshort)lpStruct->pos.y);
	(*env)->SetShortField(env, lpObject, PtColorSelectInfo_tFc.size_w, (jshort)lpStruct->size.w);
	(*env)->SetShortField(env, lpObject, PtColorSelectInfo_tFc.size_h, (jshort)lpStruct->size.h);
	(*env)->SetIntField(env, lpObject, PtColorSelectInfo_tFc.palette, (jint)lpStruct->palette.instance);
	(*env)->SetIntField(env, lpObject, PtColorSelectInfo_tFc.accept_text, (jint)lpStruct->accept_text);
	(*env)->SetIntField(env, lpObject, PtColorSelectInfo_tFc.dismiss_text, (jint)lpStruct->dismiss_text);
	(*env)->SetIntField(env, lpObject, PtColorSelectInfo_tFc.accept_dismiss_text, (jint)lpStruct->accept_dismiss_text);
	(*env)->SetIntField(env, lpObject, PtColorSelectInfo_tFc.apply_f, (jint)lpStruct->apply_f);
	(*env)->SetIntField(env, lpObject, PtColorSelectInfo_tFc.data, (jint)lpStruct->data);
	(*env)->SetIntField(env, lpObject, PtColorSelectInfo_tFc.rgb, (jint)lpStruct->rgb);
	(*env)->SetIntField(env, lpObject, PtColorSelectInfo_tFc.dialog, (jint)lpStruct->dialog);
}
#endif

#ifndef NO_PtFileSelectionInfo_t
typedef struct PtFileSelectionInfo_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID ret, path, dim, pos, format, fspec, user_data, confirm_display, confirm_selection, new_directory, btn1, btn2, num_args, args, minfo, spare;
} PtFileSelectionInfo_t_FID_CACHE;

PtFileSelectionInfo_t_FID_CACHE PtFileSelectionInfo_tFc;

void cachePtFileSelectionInfo_tFields(JNIEnv *env, jobject lpObject)
{
	if (PtFileSelectionInfo_tFc.cached) return;
	PtFileSelectionInfo_tFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PtFileSelectionInfo_tFc.ret = (*env)->GetFieldID(env, PtFileSelectionInfo_tFc.clazz, "ret", "S");
	PtFileSelectionInfo_tFc.path = (*env)->GetFieldID(env, PtFileSelectionInfo_tFc.clazz, "path", "[B");
	PtFileSelectionInfo_tFc.dim = (*env)->GetFieldID(env, PtFileSelectionInfo_tFc.clazz, "dim", "Lorg/eclipse/swt/internal/photon/PhDim_t;");
	PtFileSelectionInfo_tFc.pos = (*env)->GetFieldID(env, PtFileSelectionInfo_tFc.clazz, "pos", "Lorg/eclipse/swt/internal/photon/PhPoint_t;");
	PtFileSelectionInfo_tFc.format = (*env)->GetFieldID(env, PtFileSelectionInfo_tFc.clazz, "format", "[B");
	PtFileSelectionInfo_tFc.fspec = (*env)->GetFieldID(env, PtFileSelectionInfo_tFc.clazz, "fspec", "[B");
	PtFileSelectionInfo_tFc.user_data = (*env)->GetFieldID(env, PtFileSelectionInfo_tFc.clazz, "user_data", "I");
	PtFileSelectionInfo_tFc.confirm_display = (*env)->GetFieldID(env, PtFileSelectionInfo_tFc.clazz, "confirm_display", "I");
	PtFileSelectionInfo_tFc.confirm_selection = (*env)->GetFieldID(env, PtFileSelectionInfo_tFc.clazz, "confirm_selection", "I");
	PtFileSelectionInfo_tFc.new_directory = (*env)->GetFieldID(env, PtFileSelectionInfo_tFc.clazz, "new_directory", "I");
	PtFileSelectionInfo_tFc.btn1 = (*env)->GetFieldID(env, PtFileSelectionInfo_tFc.clazz, "btn1", "I");
	PtFileSelectionInfo_tFc.btn2 = (*env)->GetFieldID(env, PtFileSelectionInfo_tFc.clazz, "btn2", "I");
	PtFileSelectionInfo_tFc.num_args = (*env)->GetFieldID(env, PtFileSelectionInfo_tFc.clazz, "num_args", "I");
	PtFileSelectionInfo_tFc.args = (*env)->GetFieldID(env, PtFileSelectionInfo_tFc.clazz, "args", "I");
	PtFileSelectionInfo_tFc.minfo = (*env)->GetFieldID(env, PtFileSelectionInfo_tFc.clazz, "minfo", "I");
	PtFileSelectionInfo_tFc.spare = (*env)->GetFieldID(env, PtFileSelectionInfo_tFc.clazz, "spare", "[I");
	PtFileSelectionInfo_tFc.cached = 1;
}

PtFileSelectionInfo_t *getPtFileSelectionInfo_tFields(JNIEnv *env, jobject lpObject, PtFileSelectionInfo_t *lpStruct)
{
	if (!PtFileSelectionInfo_tFc.cached) cachePtFileSelectionInfo_tFields(env, lpObject);
	lpStruct->ret = (*env)->GetShortField(env, lpObject, PtFileSelectionInfo_tFc.ret);
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, PtFileSelectionInfo_tFc.path);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->path), (void *)lpStruct->path);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, PtFileSelectionInfo_tFc.dim);
	getPhDim_tFields(env, lpObject1, &lpStruct->dim);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, PtFileSelectionInfo_tFc.pos);
	getPhPoint_tFields(env, lpObject1, &lpStruct->pos);
	}
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, PtFileSelectionInfo_tFc.format);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->format), (void *)lpStruct->format);
	}
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, PtFileSelectionInfo_tFc.fspec);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->fspec), (void *)lpStruct->fspec);
	}
	lpStruct->user_data = (void *)(*env)->GetIntField(env, lpObject, PtFileSelectionInfo_tFc.user_data);
	lpStruct->confirm_display = (void *)(*env)->GetIntField(env, lpObject, PtFileSelectionInfo_tFc.confirm_display);
	lpStruct->confirm_selection = (void *)(*env)->GetIntField(env, lpObject, PtFileSelectionInfo_tFc.confirm_selection);
	lpStruct->new_directory = (void *)(*env)->GetIntField(env, lpObject, PtFileSelectionInfo_tFc.new_directory);
	lpStruct->btn1 = (char *)(*env)->GetIntField(env, lpObject, PtFileSelectionInfo_tFc.btn1);
	lpStruct->btn2 = (char *)(*env)->GetIntField(env, lpObject, PtFileSelectionInfo_tFc.btn2);
	lpStruct->num_args = (*env)->GetIntField(env, lpObject, PtFileSelectionInfo_tFc.num_args);
	lpStruct->args = (void *)(*env)->GetIntField(env, lpObject, PtFileSelectionInfo_tFc.args);
	lpStruct->minfo = (PtFileSelectorInfo_t *)(*env)->GetIntField(env, lpObject, PtFileSelectionInfo_tFc.minfo);
	{
	jintArray lpObject1 = (*env)->GetObjectField(env, lpObject, PtFileSelectionInfo_tFc.spare);
	(*env)->GetIntArrayRegion(env, lpObject1, 0, sizeof(lpStruct->spare) / 4, (void *)lpStruct->spare);
	}
	return lpStruct;
}

void setPtFileSelectionInfo_tFields(JNIEnv *env, jobject lpObject, PtFileSelectionInfo_t *lpStruct)
{
	if (!PtFileSelectionInfo_tFc.cached) cachePtFileSelectionInfo_tFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, PtFileSelectionInfo_tFc.ret, (jshort)lpStruct->ret);
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, PtFileSelectionInfo_tFc.path);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->path), (void *)lpStruct->path);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, PtFileSelectionInfo_tFc.dim);
	setPhDim_tFields(env, lpObject1, &lpStruct->dim);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, PtFileSelectionInfo_tFc.pos);
	setPhPoint_tFields(env, lpObject1, &lpStruct->pos);
	}
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, PtFileSelectionInfo_tFc.format);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->format), (void *)lpStruct->format);
	}
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, PtFileSelectionInfo_tFc.fspec);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->fspec), (void *)lpStruct->fspec);
	}
	(*env)->SetIntField(env, lpObject, PtFileSelectionInfo_tFc.user_data, (jint)lpStruct->user_data);
	(*env)->SetIntField(env, lpObject, PtFileSelectionInfo_tFc.confirm_display, (jint)lpStruct->confirm_display);
	(*env)->SetIntField(env, lpObject, PtFileSelectionInfo_tFc.confirm_selection, (jint)lpStruct->confirm_selection);
	(*env)->SetIntField(env, lpObject, PtFileSelectionInfo_tFc.new_directory, (jint)lpStruct->new_directory);
	(*env)->SetIntField(env, lpObject, PtFileSelectionInfo_tFc.btn1, (jint)lpStruct->btn1);
	(*env)->SetIntField(env, lpObject, PtFileSelectionInfo_tFc.btn2, (jint)lpStruct->btn2);
	(*env)->SetIntField(env, lpObject, PtFileSelectionInfo_tFc.num_args, (jint)lpStruct->num_args);
	(*env)->SetIntField(env, lpObject, PtFileSelectionInfo_tFc.args, (jint)lpStruct->args);
	(*env)->SetIntField(env, lpObject, PtFileSelectionInfo_tFc.minfo, (jint)lpStruct->minfo);
	{
	jintArray lpObject1 = (*env)->GetObjectField(env, lpObject, PtFileSelectionInfo_tFc.spare);
	(*env)->SetIntArrayRegion(env, lpObject1, 0, sizeof(lpStruct->spare) / 4, (void *)lpStruct->spare);
	}
}
#endif

#ifndef NO_PtScrollbarCallback_t
typedef struct PtScrollbarCallback_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID action, position;
} PtScrollbarCallback_t_FID_CACHE;

PtScrollbarCallback_t_FID_CACHE PtScrollbarCallback_tFc;

void cachePtScrollbarCallback_tFields(JNIEnv *env, jobject lpObject)
{
	if (PtScrollbarCallback_tFc.cached) return;
	PtScrollbarCallback_tFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PtScrollbarCallback_tFc.action = (*env)->GetFieldID(env, PtScrollbarCallback_tFc.clazz, "action", "I");
	PtScrollbarCallback_tFc.position = (*env)->GetFieldID(env, PtScrollbarCallback_tFc.clazz, "position", "I");
	PtScrollbarCallback_tFc.cached = 1;
}

PtScrollbarCallback_t *getPtScrollbarCallback_tFields(JNIEnv *env, jobject lpObject, PtScrollbarCallback_t *lpStruct)
{
	if (!PtScrollbarCallback_tFc.cached) cachePtScrollbarCallback_tFields(env, lpObject);
	lpStruct->action = (*env)->GetIntField(env, lpObject, PtScrollbarCallback_tFc.action);
	lpStruct->position = (*env)->GetIntField(env, lpObject, PtScrollbarCallback_tFc.position);
	return lpStruct;
}

void setPtScrollbarCallback_tFields(JNIEnv *env, jobject lpObject, PtScrollbarCallback_t *lpStruct)
{
	if (!PtScrollbarCallback_tFc.cached) cachePtScrollbarCallback_tFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, PtScrollbarCallback_tFc.action, (jint)lpStruct->action);
	(*env)->SetIntField(env, lpObject, PtScrollbarCallback_tFc.position, (jint)lpStruct->position);
}
#endif

#ifndef NO_PtTextCallback_t
typedef struct PtTextCallback_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID start_pos, end_pos, cur_insert, new_insert, length, reserved, text, doit;
} PtTextCallback_t_FID_CACHE;

PtTextCallback_t_FID_CACHE PtTextCallback_tFc;

void cachePtTextCallback_tFields(JNIEnv *env, jobject lpObject)
{
	if (PtTextCallback_tFc.cached) return;
	PtTextCallback_tFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PtTextCallback_tFc.start_pos = (*env)->GetFieldID(env, PtTextCallback_tFc.clazz, "start_pos", "I");
	PtTextCallback_tFc.end_pos = (*env)->GetFieldID(env, PtTextCallback_tFc.clazz, "end_pos", "I");
	PtTextCallback_tFc.cur_insert = (*env)->GetFieldID(env, PtTextCallback_tFc.clazz, "cur_insert", "I");
	PtTextCallback_tFc.new_insert = (*env)->GetFieldID(env, PtTextCallback_tFc.clazz, "new_insert", "I");
	PtTextCallback_tFc.length = (*env)->GetFieldID(env, PtTextCallback_tFc.clazz, "length", "I");
	PtTextCallback_tFc.reserved = (*env)->GetFieldID(env, PtTextCallback_tFc.clazz, "reserved", "S");
	PtTextCallback_tFc.text = (*env)->GetFieldID(env, PtTextCallback_tFc.clazz, "text", "I");
	PtTextCallback_tFc.doit = (*env)->GetFieldID(env, PtTextCallback_tFc.clazz, "doit", "I");
	PtTextCallback_tFc.cached = 1;
}

PtTextCallback_t *getPtTextCallback_tFields(JNIEnv *env, jobject lpObject, PtTextCallback_t *lpStruct)
{
	if (!PtTextCallback_tFc.cached) cachePtTextCallback_tFields(env, lpObject);
	lpStruct->start_pos = (*env)->GetIntField(env, lpObject, PtTextCallback_tFc.start_pos);
	lpStruct->end_pos = (*env)->GetIntField(env, lpObject, PtTextCallback_tFc.end_pos);
	lpStruct->cur_insert = (*env)->GetIntField(env, lpObject, PtTextCallback_tFc.cur_insert);
	lpStruct->new_insert = (*env)->GetIntField(env, lpObject, PtTextCallback_tFc.new_insert);
	lpStruct->length = (*env)->GetIntField(env, lpObject, PtTextCallback_tFc.length);
	lpStruct->reserved = (*env)->GetShortField(env, lpObject, PtTextCallback_tFc.reserved);
	lpStruct->text = (char *)(*env)->GetIntField(env, lpObject, PtTextCallback_tFc.text);
	lpStruct->doit = (*env)->GetIntField(env, lpObject, PtTextCallback_tFc.doit);
	return lpStruct;
}

void setPtTextCallback_tFields(JNIEnv *env, jobject lpObject, PtTextCallback_t *lpStruct)
{
	if (!PtTextCallback_tFc.cached) cachePtTextCallback_tFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, PtTextCallback_tFc.start_pos, (jint)lpStruct->start_pos);
	(*env)->SetIntField(env, lpObject, PtTextCallback_tFc.end_pos, (jint)lpStruct->end_pos);
	(*env)->SetIntField(env, lpObject, PtTextCallback_tFc.cur_insert, (jint)lpStruct->cur_insert);
	(*env)->SetIntField(env, lpObject, PtTextCallback_tFc.new_insert, (jint)lpStruct->new_insert);
	(*env)->SetIntField(env, lpObject, PtTextCallback_tFc.length, (jint)lpStruct->length);
	(*env)->SetShortField(env, lpObject, PtTextCallback_tFc.reserved, (jshort)lpStruct->reserved);
	(*env)->SetIntField(env, lpObject, PtTextCallback_tFc.text, (jint)lpStruct->text);
	(*env)->SetIntField(env, lpObject, PtTextCallback_tFc.doit, (jint)lpStruct->doit);
}
#endif

#ifndef NO_PtWebClientData_t
typedef struct PtWebClientData_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID type, url, length, data;
} PtWebClientData_t_FID_CACHE;

PtWebClientData_t_FID_CACHE PtWebClientData_tFc;

void cachePtWebClientData_tFields(JNIEnv *env, jobject lpObject)
{
	if (PtWebClientData_tFc.cached) return;
	PtWebClientData_tFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PtWebClientData_tFc.type = (*env)->GetFieldID(env, PtWebClientData_tFc.clazz, "type", "I");
	PtWebClientData_tFc.url = (*env)->GetFieldID(env, PtWebClientData_tFc.clazz, "url", "[B");
	PtWebClientData_tFc.length = (*env)->GetFieldID(env, PtWebClientData_tFc.clazz, "length", "I");
	PtWebClientData_tFc.data = (*env)->GetFieldID(env, PtWebClientData_tFc.clazz, "data", "I");
	PtWebClientData_tFc.cached = 1;
}

PtWebClientData_t *getPtWebClientData_tFields(JNIEnv *env, jobject lpObject, PtWebClientData_t *lpStruct)
{
	if (!PtWebClientData_tFc.cached) cachePtWebClientData_tFields(env, lpObject);
	lpStruct->type = (*env)->GetIntField(env, lpObject, PtWebClientData_tFc.type);
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, PtWebClientData_tFc.url);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->url), (void *)lpStruct->url);
	}
	lpStruct->length = (*env)->GetIntField(env, lpObject, PtWebClientData_tFc.length);
	lpStruct->data = (char *)(*env)->GetIntField(env, lpObject, PtWebClientData_tFc.data);
	return lpStruct;
}

void setPtWebClientData_tFields(JNIEnv *env, jobject lpObject, PtWebClientData_t *lpStruct)
{
	if (!PtWebClientData_tFc.cached) cachePtWebClientData_tFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, PtWebClientData_tFc.type, (jint)lpStruct->type);
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, PtWebClientData_tFc.url);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->url), (void *)lpStruct->url);
	}
	(*env)->SetIntField(env, lpObject, PtWebClientData_tFc.length, (jint)lpStruct->length);
	(*env)->SetIntField(env, lpObject, PtWebClientData_tFc.data, (jint)lpStruct->data);
}
#endif

#ifndef NO_PtWebDataReqCallback_t
typedef struct PtWebDataReqCallback_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID type, length, url;
} PtWebDataReqCallback_t_FID_CACHE;

PtWebDataReqCallback_t_FID_CACHE PtWebDataReqCallback_tFc;

void cachePtWebDataReqCallback_tFields(JNIEnv *env, jobject lpObject)
{
	if (PtWebDataReqCallback_tFc.cached) return;
	PtWebDataReqCallback_tFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PtWebDataReqCallback_tFc.type = (*env)->GetFieldID(env, PtWebDataReqCallback_tFc.clazz, "type", "I");
	PtWebDataReqCallback_tFc.length = (*env)->GetFieldID(env, PtWebDataReqCallback_tFc.clazz, "length", "I");
	PtWebDataReqCallback_tFc.url = (*env)->GetFieldID(env, PtWebDataReqCallback_tFc.clazz, "url", "[B");
	PtWebDataReqCallback_tFc.cached = 1;
}

PtWebDataReqCallback_t *getPtWebDataReqCallback_tFields(JNIEnv *env, jobject lpObject, PtWebDataReqCallback_t *lpStruct)
{
	if (!PtWebDataReqCallback_tFc.cached) cachePtWebDataReqCallback_tFields(env, lpObject);
	lpStruct->type = (*env)->GetIntField(env, lpObject, PtWebDataReqCallback_tFc.type);
	lpStruct->length = (*env)->GetIntField(env, lpObject, PtWebDataReqCallback_tFc.length);
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, PtWebDataReqCallback_tFc.url);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->url), (void *)lpStruct->url);
	}
	return lpStruct;
}

void setPtWebDataReqCallback_tFields(JNIEnv *env, jobject lpObject, PtWebDataReqCallback_t *lpStruct)
{
	if (!PtWebDataReqCallback_tFc.cached) cachePtWebDataReqCallback_tFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, PtWebDataReqCallback_tFc.type, (jint)lpStruct->type);
	(*env)->SetIntField(env, lpObject, PtWebDataReqCallback_tFc.length, (jint)lpStruct->length);
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, PtWebDataReqCallback_tFc.url);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->url), (void *)lpStruct->url);
	}
}
#endif

#ifndef NO_PtWebStatusCallback_t
typedef struct PtWebStatusCallback_t_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID desc, type, url;
} PtWebStatusCallback_t_FID_CACHE;

PtWebStatusCallback_t_FID_CACHE PtWebStatusCallback_tFc;

void cachePtWebStatusCallback_tFields(JNIEnv *env, jobject lpObject)
{
	if (PtWebStatusCallback_tFc.cached) return;
	PtWebStatusCallback_tFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PtWebStatusCallback_tFc.desc = (*env)->GetFieldID(env, PtWebStatusCallback_tFc.clazz, "desc", "[B");
	PtWebStatusCallback_tFc.type = (*env)->GetFieldID(env, PtWebStatusCallback_tFc.clazz, "type", "S");
	PtWebStatusCallback_tFc.url = (*env)->GetFieldID(env, PtWebStatusCallback_tFc.clazz, "url", "[B");
	PtWebStatusCallback_tFc.cached = 1;
}

PtWebStatusCallback_t *getPtWebStatusCallback_tFields(JNIEnv *env, jobject lpObject, PtWebStatusCallback_t *lpStruct)
{
	if (!PtWebStatusCallback_tFc.cached) cachePtWebStatusCallback_tFields(env, lpObject);
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, PtWebStatusCallback_tFc.desc);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->desc), (void *)lpStruct->desc);
	}
	lpStruct->type = (*env)->GetShortField(env, lpObject, PtWebStatusCallback_tFc.type);
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, PtWebStatusCallback_tFc.url);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->url), (void *)lpStruct->url);
	}
	return lpStruct;
}

void setPtWebStatusCallback_tFields(JNIEnv *env, jobject lpObject, PtWebStatusCallback_t *lpStruct)
{
	if (!PtWebStatusCallback_tFc.cached) cachePtWebStatusCallback_tFields(env, lpObject);
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, PtWebStatusCallback_tFc.desc);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->desc), (void *)lpStruct->desc);
	}
	(*env)->SetShortField(env, lpObject, PtWebStatusCallback_tFc.type, (jshort)lpStruct->type);
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, PtWebStatusCallback_tFc.url);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->url), (void *)lpStruct->url);
	}
}
#endif

#ifndef NO_utsname
typedef struct utsname_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID sysname, nodename, release, version, machine;
} utsname_FID_CACHE;

utsname_FID_CACHE utsnameFc;

void cacheutsnameFields(JNIEnv *env, jobject lpObject)
{
	if (utsnameFc.cached) return;
	utsnameFc.clazz = (*env)->GetObjectClass(env, lpObject);
	utsnameFc.sysname = (*env)->GetFieldID(env, utsnameFc.clazz, "sysname", "[B");
	utsnameFc.nodename = (*env)->GetFieldID(env, utsnameFc.clazz, "nodename", "[B");
	utsnameFc.release = (*env)->GetFieldID(env, utsnameFc.clazz, "release", "[B");
	utsnameFc.version = (*env)->GetFieldID(env, utsnameFc.clazz, "version", "[B");
	utsnameFc.machine = (*env)->GetFieldID(env, utsnameFc.clazz, "machine", "[B");
	utsnameFc.cached = 1;
}

utsname *getutsnameFields(JNIEnv *env, jobject lpObject, utsname *lpStruct)
{
	if (!utsnameFc.cached) cacheutsnameFields(env, lpObject);
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, utsnameFc.sysname);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->sysname), (void *)lpStruct->sysname);
	}
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, utsnameFc.nodename);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->nodename), (void *)lpStruct->nodename);
	}
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, utsnameFc.release);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->release), (void *)lpStruct->release);
	}
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, utsnameFc.version);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->version), (void *)lpStruct->version);
	}
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, utsnameFc.machine);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->machine), (void *)lpStruct->machine);
	}
	return lpStruct;
}

void setutsnameFields(JNIEnv *env, jobject lpObject, utsname *lpStruct)
{
	if (!utsnameFc.cached) cacheutsnameFields(env, lpObject);
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, utsnameFc.sysname);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->sysname), (void *)lpStruct->sysname);
	}
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, utsnameFc.nodename);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->nodename), (void *)lpStruct->nodename);
	}
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, utsnameFc.release);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->release), (void *)lpStruct->release);
	}
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, utsnameFc.version);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->version), (void *)lpStruct->version);
	}
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, utsnameFc.machine);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->machine), (void *)lpStruct->machine);
	}
}
#endif

