/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

#include "swt.h"
#include "structs.h"

typedef struct GLYPHMETRICSFLOAT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID gmfCellIncY, gmfCellIncX, gmfptGlyphOrigin_y, gmfptGlyphOrigin_x, gmfBlackBoxY, gmfBlackBoxX;
} GLYPHMETRICSFLOAT_FID_CACHE;
typedef GLYPHMETRICSFLOAT_FID_CACHE *PGLYPHMETRICSFLOAT_FID_CACHE;

GLYPHMETRICSFLOAT_FID_CACHE GLYPHMETRICSFLOATFc;

void cacheGLYPHMETRICSFLOATFids(JNIEnv *env, jobject lpObject, PGLYPHMETRICSFLOAT_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->gmfCellIncY = (*env)->GetFieldID(env, lpCache->clazz, "gmfCellIncY", "F");
	lpCache->gmfCellIncX = (*env)->GetFieldID(env, lpCache->clazz, "gmfCellIncX", "F");
	lpCache->gmfptGlyphOrigin_y = (*env)->GetFieldID(env, lpCache->clazz, "gmfptGlyphOrigin_y", "F");
	lpCache->gmfptGlyphOrigin_x = (*env)->GetFieldID(env, lpCache->clazz, "gmfptGlyphOrigin_x", "F");
	lpCache->gmfBlackBoxY = (*env)->GetFieldID(env, lpCache->clazz, "gmfBlackBoxY", "F");
	lpCache->gmfBlackBoxX = (*env)->GetFieldID(env, lpCache->clazz, "gmfBlackBoxX", "F");
	lpCache->cached = 1;
}

GLYPHMETRICSFLOAT *getGLYPHMETRICSFLOATFields(JNIEnv *env, jobject lpObject, GLYPHMETRICSFLOAT *lpStruct)
{
	PGLYPHMETRICSFLOAT_FID_CACHE lpCache = &GLYPHMETRICSFLOATFc;
	if (!lpCache->cached) cacheGLYPHMETRICSFLOATFids(env, lpObject, lpCache);
	lpStruct->gmfCellIncY = (*env)->GetFloatField(env, lpObject, lpCache->gmfCellIncY);
	lpStruct->gmfCellIncX = (*env)->GetFloatField(env, lpObject, lpCache->gmfCellIncX);
	lpStruct->gmfptGlyphOrigin.y = (*env)->GetFloatField(env, lpObject, lpCache->gmfptGlyphOrigin_y);
	lpStruct->gmfptGlyphOrigin.x = (*env)->GetFloatField(env, lpObject, lpCache->gmfptGlyphOrigin_x);
	lpStruct->gmfBlackBoxY = (*env)->GetFloatField(env, lpObject, lpCache->gmfBlackBoxY);
	lpStruct->gmfBlackBoxX = (*env)->GetFloatField(env, lpObject, lpCache->gmfBlackBoxX);
	return lpStruct;
}

void setGLYPHMETRICSFLOATFields(JNIEnv *env, jobject lpObject, GLYPHMETRICSFLOAT *lpStruct)
{
	PGLYPHMETRICSFLOAT_FID_CACHE lpCache = &GLYPHMETRICSFLOATFc;
	if (!lpCache->cached) cacheGLYPHMETRICSFLOATFids(env, lpObject, lpCache);
	(*env)->SetFloatField(env, lpObject, lpCache->gmfCellIncY, (jfloat)lpStruct->gmfCellIncY);
	(*env)->SetFloatField(env, lpObject, lpCache->gmfCellIncX, (jfloat)lpStruct->gmfCellIncX);
	(*env)->SetFloatField(env, lpObject, lpCache->gmfptGlyphOrigin_y, (jfloat)lpStruct->gmfptGlyphOrigin.y);
	(*env)->SetFloatField(env, lpObject, lpCache->gmfptGlyphOrigin_x, (jfloat)lpStruct->gmfptGlyphOrigin.x);
	(*env)->SetFloatField(env, lpObject, lpCache->gmfBlackBoxY, (jfloat)lpStruct->gmfBlackBoxY);
	(*env)->SetFloatField(env, lpObject, lpCache->gmfBlackBoxX, (jfloat)lpStruct->gmfBlackBoxX);
}

typedef struct LAYERPLANEDESCRIPTOR_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID crTransparent, bReserved, iLayerPlane, cAuxBuffers, cStencilBits, cDepthBits, cAccumAlphaBits, cAccumBlueBits, cAccumGreenBits, cAccumRedBits, cAccumBits, cAlphaShift, cAlphaBits, cBlueShift, cBlueBits, cGreenShift, cGreenBits, cRedShift, cRedBits, cColorBits, iPixelType, dwFlags, nVersion, nSize;
} LAYERPLANEDESCRIPTOR_FID_CACHE;
typedef LAYERPLANEDESCRIPTOR_FID_CACHE *PLAYERPLANEDESCRIPTOR_FID_CACHE;

LAYERPLANEDESCRIPTOR_FID_CACHE LAYERPLANEDESCRIPTORFc;

void cacheLAYERPLANEDESCRIPTORFids(JNIEnv *env, jobject lpObject, PLAYERPLANEDESCRIPTOR_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->crTransparent = (*env)->GetFieldID(env, lpCache->clazz, "crTransparent", "I");
	lpCache->bReserved = (*env)->GetFieldID(env, lpCache->clazz, "bReserved", "B");
	lpCache->iLayerPlane = (*env)->GetFieldID(env, lpCache->clazz, "iLayerPlane", "B");
	lpCache->cAuxBuffers = (*env)->GetFieldID(env, lpCache->clazz, "cAuxBuffers", "B");
	lpCache->cStencilBits = (*env)->GetFieldID(env, lpCache->clazz, "cStencilBits", "B");
	lpCache->cDepthBits = (*env)->GetFieldID(env, lpCache->clazz, "cDepthBits", "B");
	lpCache->cAccumAlphaBits = (*env)->GetFieldID(env, lpCache->clazz, "cAccumAlphaBits", "B");
	lpCache->cAccumBlueBits = (*env)->GetFieldID(env, lpCache->clazz, "cAccumBlueBits", "B");
	lpCache->cAccumGreenBits = (*env)->GetFieldID(env, lpCache->clazz, "cAccumGreenBits", "B");
	lpCache->cAccumRedBits = (*env)->GetFieldID(env, lpCache->clazz, "cAccumRedBits", "B");
	lpCache->cAccumBits = (*env)->GetFieldID(env, lpCache->clazz, "cAccumBits", "B");
	lpCache->cAlphaShift = (*env)->GetFieldID(env, lpCache->clazz, "cAlphaShift", "B");
	lpCache->cAlphaBits = (*env)->GetFieldID(env, lpCache->clazz, "cAlphaBits", "B");
	lpCache->cBlueShift = (*env)->GetFieldID(env, lpCache->clazz, "cBlueShift", "B");
	lpCache->cBlueBits = (*env)->GetFieldID(env, lpCache->clazz, "cBlueBits", "B");
	lpCache->cGreenShift = (*env)->GetFieldID(env, lpCache->clazz, "cGreenShift", "B");
	lpCache->cGreenBits = (*env)->GetFieldID(env, lpCache->clazz, "cGreenBits", "B");
	lpCache->cRedShift = (*env)->GetFieldID(env, lpCache->clazz, "cRedShift", "B");
	lpCache->cRedBits = (*env)->GetFieldID(env, lpCache->clazz, "cRedBits", "B");
	lpCache->cColorBits = (*env)->GetFieldID(env, lpCache->clazz, "cColorBits", "B");
	lpCache->iPixelType = (*env)->GetFieldID(env, lpCache->clazz, "iPixelType", "B");
	lpCache->dwFlags = (*env)->GetFieldID(env, lpCache->clazz, "dwFlags", "I");
	lpCache->nVersion = (*env)->GetFieldID(env, lpCache->clazz, "nVersion", "S");
	lpCache->nSize = (*env)->GetFieldID(env, lpCache->clazz, "nSize", "S");
	lpCache->cached = 1;
}

LAYERPLANEDESCRIPTOR *getLAYERPLANEDESCRIPTORFields(JNIEnv *env, jobject lpObject, LAYERPLANEDESCRIPTOR *lpStruct)
{
	PLAYERPLANEDESCRIPTOR_FID_CACHE lpCache = &LAYERPLANEDESCRIPTORFc;
	if (!lpCache->cached) cacheLAYERPLANEDESCRIPTORFids(env, lpObject, lpCache);
	lpStruct->crTransparent = (*env)->GetIntField(env, lpObject, lpCache->crTransparent);
	lpStruct->bReserved = (*env)->GetByteField(env, lpObject, lpCache->bReserved);
	lpStruct->iLayerPlane = (*env)->GetByteField(env, lpObject, lpCache->iLayerPlane);
	lpStruct->cAuxBuffers = (*env)->GetByteField(env, lpObject, lpCache->cAuxBuffers);
	lpStruct->cStencilBits = (*env)->GetByteField(env, lpObject, lpCache->cStencilBits);
	lpStruct->cDepthBits = (*env)->GetByteField(env, lpObject, lpCache->cDepthBits);
	lpStruct->cAccumAlphaBits = (*env)->GetByteField(env, lpObject, lpCache->cAccumAlphaBits);
	lpStruct->cAccumBlueBits = (*env)->GetByteField(env, lpObject, lpCache->cAccumBlueBits);
	lpStruct->cAccumGreenBits = (*env)->GetByteField(env, lpObject, lpCache->cAccumGreenBits);
	lpStruct->cAccumRedBits = (*env)->GetByteField(env, lpObject, lpCache->cAccumRedBits);
	lpStruct->cAccumBits = (*env)->GetByteField(env, lpObject, lpCache->cAccumBits);
	lpStruct->cAlphaShift = (*env)->GetByteField(env, lpObject, lpCache->cAlphaShift);
	lpStruct->cAlphaBits = (*env)->GetByteField(env, lpObject, lpCache->cAlphaBits);
	lpStruct->cBlueShift = (*env)->GetByteField(env, lpObject, lpCache->cBlueShift);
	lpStruct->cBlueBits = (*env)->GetByteField(env, lpObject, lpCache->cBlueBits);
	lpStruct->cGreenShift = (*env)->GetByteField(env, lpObject, lpCache->cGreenShift);
	lpStruct->cGreenBits = (*env)->GetByteField(env, lpObject, lpCache->cGreenBits);
	lpStruct->cRedShift = (*env)->GetByteField(env, lpObject, lpCache->cRedShift);
	lpStruct->cRedBits = (*env)->GetByteField(env, lpObject, lpCache->cRedBits);
	lpStruct->cColorBits = (*env)->GetByteField(env, lpObject, lpCache->cColorBits);
	lpStruct->iPixelType = (*env)->GetByteField(env, lpObject, lpCache->iPixelType);
	lpStruct->dwFlags = (*env)->GetIntField(env, lpObject, lpCache->dwFlags);
	lpStruct->nVersion = (*env)->GetShortField(env, lpObject, lpCache->nVersion);
	lpStruct->nSize = (*env)->GetShortField(env, lpObject, lpCache->nSize);
	return lpStruct;
}

void setLAYERPLANEDESCRIPTORFields(JNIEnv *env, jobject lpObject, LAYERPLANEDESCRIPTOR *lpStruct)
{
	PLAYERPLANEDESCRIPTOR_FID_CACHE lpCache = &LAYERPLANEDESCRIPTORFc;
	if (!lpCache->cached) cacheLAYERPLANEDESCRIPTORFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->crTransparent, (jint)lpStruct->crTransparent);
	(*env)->SetByteField(env, lpObject, lpCache->bReserved, (jbyte)lpStruct->bReserved);
	(*env)->SetByteField(env, lpObject, lpCache->iLayerPlane, (jbyte)lpStruct->iLayerPlane);
	(*env)->SetByteField(env, lpObject, lpCache->cAuxBuffers, (jbyte)lpStruct->cAuxBuffers);
	(*env)->SetByteField(env, lpObject, lpCache->cStencilBits, (jbyte)lpStruct->cStencilBits);
	(*env)->SetByteField(env, lpObject, lpCache->cDepthBits, (jbyte)lpStruct->cDepthBits);
	(*env)->SetByteField(env, lpObject, lpCache->cAccumAlphaBits, (jbyte)lpStruct->cAccumAlphaBits);
	(*env)->SetByteField(env, lpObject, lpCache->cAccumBlueBits, (jbyte)lpStruct->cAccumBlueBits);
	(*env)->SetByteField(env, lpObject, lpCache->cAccumGreenBits, (jbyte)lpStruct->cAccumGreenBits);
	(*env)->SetByteField(env, lpObject, lpCache->cAccumRedBits, (jbyte)lpStruct->cAccumRedBits);
	(*env)->SetByteField(env, lpObject, lpCache->cAccumBits, (jbyte)lpStruct->cAccumBits);
	(*env)->SetByteField(env, lpObject, lpCache->cAlphaShift, (jbyte)lpStruct->cAlphaShift);
	(*env)->SetByteField(env, lpObject, lpCache->cAlphaBits, (jbyte)lpStruct->cAlphaBits);
	(*env)->SetByteField(env, lpObject, lpCache->cBlueShift, (jbyte)lpStruct->cBlueShift);
	(*env)->SetByteField(env, lpObject, lpCache->cBlueBits, (jbyte)lpStruct->cBlueBits);
	(*env)->SetByteField(env, lpObject, lpCache->cGreenShift, (jbyte)lpStruct->cGreenShift);
	(*env)->SetByteField(env, lpObject, lpCache->cGreenBits, (jbyte)lpStruct->cGreenBits);
	(*env)->SetByteField(env, lpObject, lpCache->cRedShift, (jbyte)lpStruct->cRedShift);
	(*env)->SetByteField(env, lpObject, lpCache->cRedBits, (jbyte)lpStruct->cRedBits);
	(*env)->SetByteField(env, lpObject, lpCache->cColorBits, (jbyte)lpStruct->cColorBits);
	(*env)->SetByteField(env, lpObject, lpCache->iPixelType, (jbyte)lpStruct->iPixelType);
	(*env)->SetIntField(env, lpObject, lpCache->dwFlags, (jint)lpStruct->dwFlags);
	(*env)->SetShortField(env, lpObject, lpCache->nVersion, (jshort)lpStruct->nVersion);
	(*env)->SetShortField(env, lpObject, lpCache->nSize, (jshort)lpStruct->nSize);
}

typedef struct POINTFLOAT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID y, x;
} POINTFLOAT_FID_CACHE;
typedef POINTFLOAT_FID_CACHE *PPOINTFLOAT_FID_CACHE;

POINTFLOAT_FID_CACHE POINTFLOATFc;

void cachePOINTFLOATFids(JNIEnv *env, jobject lpObject, PPOINTFLOAT_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->y = (*env)->GetFieldID(env, lpCache->clazz, "y", "F");
	lpCache->x = (*env)->GetFieldID(env, lpCache->clazz, "x", "F");
	lpCache->cached = 1;
}

POINTFLOAT *getPOINTFLOATFields(JNIEnv *env, jobject lpObject, POINTFLOAT *lpStruct)
{
	PPOINTFLOAT_FID_CACHE lpCache = &POINTFLOATFc;
	if (!lpCache->cached) cachePOINTFLOATFids(env, lpObject, lpCache);
	lpStruct->y = (*env)->GetFloatField(env, lpObject, lpCache->y);
	lpStruct->x = (*env)->GetFloatField(env, lpObject, lpCache->x);
	return lpStruct;
}

void setPOINTFLOATFields(JNIEnv *env, jobject lpObject, POINTFLOAT *lpStruct)
{
	PPOINTFLOAT_FID_CACHE lpCache = &POINTFLOATFc;
	if (!lpCache->cached) cachePOINTFLOATFids(env, lpObject, lpCache);
	(*env)->SetFloatField(env, lpObject, lpCache->y, (jfloat)lpStruct->y);
	(*env)->SetFloatField(env, lpObject, lpCache->x, (jfloat)lpStruct->x);
}

typedef struct PIXELFORMATDESCRIPTOR_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID dwDamageMask, dwVisibleMask, dwLayerMask, bReserved, iLayerType, cAuxBuffers, cStencilBits, cDepthBits, cAccumAlphaBits, cAccumBlueBits, cAccumGreenBits, cAccumRedBits, cAccumBits, cAlphaShift, cAlphaBits, cBlueShift, cBlueBits, cGreenShift, cGreenBits, cRedShift, cRedBits, cColorBits, iPixelType, dwFlags, nVersion, nSize;
} PIXELFORMATDESCRIPTOR_FID_CACHE;
typedef PIXELFORMATDESCRIPTOR_FID_CACHE *PPIXELFORMATDESCRIPTOR_FID_CACHE;

PIXELFORMATDESCRIPTOR_FID_CACHE PIXELFORMATDESCRIPTORFc;

void cachePIXELFORMATDESCRIPTORFids(JNIEnv *env, jobject lpObject, PPIXELFORMATDESCRIPTOR_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->dwDamageMask = (*env)->GetFieldID(env, lpCache->clazz, "dwDamageMask", "I");
	lpCache->dwVisibleMask = (*env)->GetFieldID(env, lpCache->clazz, "dwVisibleMask", "I");
	lpCache->dwLayerMask = (*env)->GetFieldID(env, lpCache->clazz, "dwLayerMask", "I");
	lpCache->bReserved = (*env)->GetFieldID(env, lpCache->clazz, "bReserved", "B");
	lpCache->iLayerType = (*env)->GetFieldID(env, lpCache->clazz, "iLayerType", "B");
	lpCache->cAuxBuffers = (*env)->GetFieldID(env, lpCache->clazz, "cAuxBuffers", "B");
	lpCache->cStencilBits = (*env)->GetFieldID(env, lpCache->clazz, "cStencilBits", "B");
	lpCache->cDepthBits = (*env)->GetFieldID(env, lpCache->clazz, "cDepthBits", "B");
	lpCache->cAccumAlphaBits = (*env)->GetFieldID(env, lpCache->clazz, "cAccumAlphaBits", "B");
	lpCache->cAccumBlueBits = (*env)->GetFieldID(env, lpCache->clazz, "cAccumBlueBits", "B");
	lpCache->cAccumGreenBits = (*env)->GetFieldID(env, lpCache->clazz, "cAccumGreenBits", "B");
	lpCache->cAccumRedBits = (*env)->GetFieldID(env, lpCache->clazz, "cAccumRedBits", "B");
	lpCache->cAccumBits = (*env)->GetFieldID(env, lpCache->clazz, "cAccumBits", "B");
	lpCache->cAlphaShift = (*env)->GetFieldID(env, lpCache->clazz, "cAlphaShift", "B");
	lpCache->cAlphaBits = (*env)->GetFieldID(env, lpCache->clazz, "cAlphaBits", "B");
	lpCache->cBlueShift = (*env)->GetFieldID(env, lpCache->clazz, "cBlueShift", "B");
	lpCache->cBlueBits = (*env)->GetFieldID(env, lpCache->clazz, "cBlueBits", "B");
	lpCache->cGreenShift = (*env)->GetFieldID(env, lpCache->clazz, "cGreenShift", "B");
	lpCache->cGreenBits = (*env)->GetFieldID(env, lpCache->clazz, "cGreenBits", "B");
	lpCache->cRedShift = (*env)->GetFieldID(env, lpCache->clazz, "cRedShift", "B");
	lpCache->cRedBits = (*env)->GetFieldID(env, lpCache->clazz, "cRedBits", "B");
	lpCache->cColorBits = (*env)->GetFieldID(env, lpCache->clazz, "cColorBits", "B");
	lpCache->iPixelType = (*env)->GetFieldID(env, lpCache->clazz, "iPixelType", "B");
	lpCache->dwFlags = (*env)->GetFieldID(env, lpCache->clazz, "dwFlags", "I");
	lpCache->nVersion = (*env)->GetFieldID(env, lpCache->clazz, "nVersion", "S");
	lpCache->nSize = (*env)->GetFieldID(env, lpCache->clazz, "nSize", "S");
	lpCache->cached = 1;
}

PIXELFORMATDESCRIPTOR *getPIXELFORMATDESCRIPTORFields(JNIEnv *env, jobject lpObject, PIXELFORMATDESCRIPTOR *lpStruct)
{
	PPIXELFORMATDESCRIPTOR_FID_CACHE lpCache = &PIXELFORMATDESCRIPTORFc;
	if (!lpCache->cached) cachePIXELFORMATDESCRIPTORFids(env, lpObject, lpCache);
	lpStruct->dwDamageMask = (*env)->GetIntField(env, lpObject, lpCache->dwDamageMask);
	lpStruct->dwVisibleMask = (*env)->GetIntField(env, lpObject, lpCache->dwVisibleMask);
	lpStruct->dwLayerMask = (*env)->GetIntField(env, lpObject, lpCache->dwLayerMask);
	lpStruct->bReserved = (*env)->GetByteField(env, lpObject, lpCache->bReserved);
	lpStruct->iLayerType = (*env)->GetByteField(env, lpObject, lpCache->iLayerType);
	lpStruct->cAuxBuffers = (*env)->GetByteField(env, lpObject, lpCache->cAuxBuffers);
	lpStruct->cStencilBits = (*env)->GetByteField(env, lpObject, lpCache->cStencilBits);
	lpStruct->cDepthBits = (*env)->GetByteField(env, lpObject, lpCache->cDepthBits);
	lpStruct->cAccumAlphaBits = (*env)->GetByteField(env, lpObject, lpCache->cAccumAlphaBits);
	lpStruct->cAccumBlueBits = (*env)->GetByteField(env, lpObject, lpCache->cAccumBlueBits);
	lpStruct->cAccumGreenBits = (*env)->GetByteField(env, lpObject, lpCache->cAccumGreenBits);
	lpStruct->cAccumRedBits = (*env)->GetByteField(env, lpObject, lpCache->cAccumRedBits);
	lpStruct->cAccumBits = (*env)->GetByteField(env, lpObject, lpCache->cAccumBits);
	lpStruct->cAlphaShift = (*env)->GetByteField(env, lpObject, lpCache->cAlphaShift);
	lpStruct->cAlphaBits = (*env)->GetByteField(env, lpObject, lpCache->cAlphaBits);
	lpStruct->cBlueShift = (*env)->GetByteField(env, lpObject, lpCache->cBlueShift);
	lpStruct->cBlueBits = (*env)->GetByteField(env, lpObject, lpCache->cBlueBits);
	lpStruct->cGreenShift = (*env)->GetByteField(env, lpObject, lpCache->cGreenShift);
	lpStruct->cGreenBits = (*env)->GetByteField(env, lpObject, lpCache->cGreenBits);
	lpStruct->cRedShift = (*env)->GetByteField(env, lpObject, lpCache->cRedShift);
	lpStruct->cRedBits = (*env)->GetByteField(env, lpObject, lpCache->cRedBits);
	lpStruct->cColorBits = (*env)->GetByteField(env, lpObject, lpCache->cColorBits);
	lpStruct->iPixelType = (*env)->GetByteField(env, lpObject, lpCache->iPixelType);
	lpStruct->dwFlags = (*env)->GetIntField(env, lpObject, lpCache->dwFlags);
	lpStruct->nVersion = (*env)->GetShortField(env, lpObject, lpCache->nVersion);
	lpStruct->nSize = (*env)->GetShortField(env, lpObject, lpCache->nSize);
	return lpStruct;
}

void setPIXELFORMATDESCRIPTORFields(JNIEnv *env, jobject lpObject, PIXELFORMATDESCRIPTOR *lpStruct)
{
	PPIXELFORMATDESCRIPTOR_FID_CACHE lpCache = &PIXELFORMATDESCRIPTORFc;
	if (!lpCache->cached) cachePIXELFORMATDESCRIPTORFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->dwDamageMask, (jint)lpStruct->dwDamageMask);
	(*env)->SetIntField(env, lpObject, lpCache->dwVisibleMask, (jint)lpStruct->dwVisibleMask);
	(*env)->SetIntField(env, lpObject, lpCache->dwLayerMask, (jint)lpStruct->dwLayerMask);
	(*env)->SetByteField(env, lpObject, lpCache->bReserved, (jbyte)lpStruct->bReserved);
	(*env)->SetByteField(env, lpObject, lpCache->iLayerType, (jbyte)lpStruct->iLayerType);
	(*env)->SetByteField(env, lpObject, lpCache->cAuxBuffers, (jbyte)lpStruct->cAuxBuffers);
	(*env)->SetByteField(env, lpObject, lpCache->cStencilBits, (jbyte)lpStruct->cStencilBits);
	(*env)->SetByteField(env, lpObject, lpCache->cDepthBits, (jbyte)lpStruct->cDepthBits);
	(*env)->SetByteField(env, lpObject, lpCache->cAccumAlphaBits, (jbyte)lpStruct->cAccumAlphaBits);
	(*env)->SetByteField(env, lpObject, lpCache->cAccumBlueBits, (jbyte)lpStruct->cAccumBlueBits);
	(*env)->SetByteField(env, lpObject, lpCache->cAccumGreenBits, (jbyte)lpStruct->cAccumGreenBits);
	(*env)->SetByteField(env, lpObject, lpCache->cAccumRedBits, (jbyte)lpStruct->cAccumRedBits);
	(*env)->SetByteField(env, lpObject, lpCache->cAccumBits, (jbyte)lpStruct->cAccumBits);
	(*env)->SetByteField(env, lpObject, lpCache->cAlphaShift, (jbyte)lpStruct->cAlphaShift);
	(*env)->SetByteField(env, lpObject, lpCache->cAlphaBits, (jbyte)lpStruct->cAlphaBits);
	(*env)->SetByteField(env, lpObject, lpCache->cBlueShift, (jbyte)lpStruct->cBlueShift);
	(*env)->SetByteField(env, lpObject, lpCache->cBlueBits, (jbyte)lpStruct->cBlueBits);
	(*env)->SetByteField(env, lpObject, lpCache->cGreenShift, (jbyte)lpStruct->cGreenShift);
	(*env)->SetByteField(env, lpObject, lpCache->cGreenBits, (jbyte)lpStruct->cGreenBits);
	(*env)->SetByteField(env, lpObject, lpCache->cRedShift, (jbyte)lpStruct->cRedShift);
	(*env)->SetByteField(env, lpObject, lpCache->cRedBits, (jbyte)lpStruct->cRedBits);
	(*env)->SetByteField(env, lpObject, lpCache->cColorBits, (jbyte)lpStruct->cColorBits);
	(*env)->SetByteField(env, lpObject, lpCache->iPixelType, (jbyte)lpStruct->iPixelType);
	(*env)->SetIntField(env, lpObject, lpCache->dwFlags, (jint)lpStruct->dwFlags);
	(*env)->SetShortField(env, lpObject, lpCache->nVersion, (jshort)lpStruct->nVersion);
	(*env)->SetShortField(env, lpObject, lpCache->nSize, (jshort)lpStruct->nSize);
}

