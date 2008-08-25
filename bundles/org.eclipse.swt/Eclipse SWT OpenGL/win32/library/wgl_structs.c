/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

#include "swt.h"
#include "wgl_structs.h"

#ifndef NO_LAYERPLANEDESCRIPTOR
typedef struct LAYERPLANEDESCRIPTOR_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID nSize, nVersion, dwFlags, iPixelType, cColorBits, cRedBits, cRedShift, cGreenBits, cGreenShift, cBlueBits, cBlueShift, cAlphaBits, cAlphaShift, cAccumBits, cAccumRedBits, cAccumGreenBits, cAccumBlueBits, cAccumAlphaBits, cDepthBits, cStencilBits, cAuxBuffers, iLayerPlane, bReserved, crTransparent;
} LAYERPLANEDESCRIPTOR_FID_CACHE;

LAYERPLANEDESCRIPTOR_FID_CACHE LAYERPLANEDESCRIPTORFc;

void cacheLAYERPLANEDESCRIPTORFields(JNIEnv *env, jobject lpObject)
{
	if (LAYERPLANEDESCRIPTORFc.cached) return;
	LAYERPLANEDESCRIPTORFc.clazz = (*env)->GetObjectClass(env, lpObject);
	LAYERPLANEDESCRIPTORFc.nSize = (*env)->GetFieldID(env, LAYERPLANEDESCRIPTORFc.clazz, "nSize", "S");
	LAYERPLANEDESCRIPTORFc.nVersion = (*env)->GetFieldID(env, LAYERPLANEDESCRIPTORFc.clazz, "nVersion", "S");
	LAYERPLANEDESCRIPTORFc.dwFlags = (*env)->GetFieldID(env, LAYERPLANEDESCRIPTORFc.clazz, "dwFlags", "I");
	LAYERPLANEDESCRIPTORFc.iPixelType = (*env)->GetFieldID(env, LAYERPLANEDESCRIPTORFc.clazz, "iPixelType", "B");
	LAYERPLANEDESCRIPTORFc.cColorBits = (*env)->GetFieldID(env, LAYERPLANEDESCRIPTORFc.clazz, "cColorBits", "B");
	LAYERPLANEDESCRIPTORFc.cRedBits = (*env)->GetFieldID(env, LAYERPLANEDESCRIPTORFc.clazz, "cRedBits", "B");
	LAYERPLANEDESCRIPTORFc.cRedShift = (*env)->GetFieldID(env, LAYERPLANEDESCRIPTORFc.clazz, "cRedShift", "B");
	LAYERPLANEDESCRIPTORFc.cGreenBits = (*env)->GetFieldID(env, LAYERPLANEDESCRIPTORFc.clazz, "cGreenBits", "B");
	LAYERPLANEDESCRIPTORFc.cGreenShift = (*env)->GetFieldID(env, LAYERPLANEDESCRIPTORFc.clazz, "cGreenShift", "B");
	LAYERPLANEDESCRIPTORFc.cBlueBits = (*env)->GetFieldID(env, LAYERPLANEDESCRIPTORFc.clazz, "cBlueBits", "B");
	LAYERPLANEDESCRIPTORFc.cBlueShift = (*env)->GetFieldID(env, LAYERPLANEDESCRIPTORFc.clazz, "cBlueShift", "B");
	LAYERPLANEDESCRIPTORFc.cAlphaBits = (*env)->GetFieldID(env, LAYERPLANEDESCRIPTORFc.clazz, "cAlphaBits", "B");
	LAYERPLANEDESCRIPTORFc.cAlphaShift = (*env)->GetFieldID(env, LAYERPLANEDESCRIPTORFc.clazz, "cAlphaShift", "B");
	LAYERPLANEDESCRIPTORFc.cAccumBits = (*env)->GetFieldID(env, LAYERPLANEDESCRIPTORFc.clazz, "cAccumBits", "B");
	LAYERPLANEDESCRIPTORFc.cAccumRedBits = (*env)->GetFieldID(env, LAYERPLANEDESCRIPTORFc.clazz, "cAccumRedBits", "B");
	LAYERPLANEDESCRIPTORFc.cAccumGreenBits = (*env)->GetFieldID(env, LAYERPLANEDESCRIPTORFc.clazz, "cAccumGreenBits", "B");
	LAYERPLANEDESCRIPTORFc.cAccumBlueBits = (*env)->GetFieldID(env, LAYERPLANEDESCRIPTORFc.clazz, "cAccumBlueBits", "B");
	LAYERPLANEDESCRIPTORFc.cAccumAlphaBits = (*env)->GetFieldID(env, LAYERPLANEDESCRIPTORFc.clazz, "cAccumAlphaBits", "B");
	LAYERPLANEDESCRIPTORFc.cDepthBits = (*env)->GetFieldID(env, LAYERPLANEDESCRIPTORFc.clazz, "cDepthBits", "B");
	LAYERPLANEDESCRIPTORFc.cStencilBits = (*env)->GetFieldID(env, LAYERPLANEDESCRIPTORFc.clazz, "cStencilBits", "B");
	LAYERPLANEDESCRIPTORFc.cAuxBuffers = (*env)->GetFieldID(env, LAYERPLANEDESCRIPTORFc.clazz, "cAuxBuffers", "B");
	LAYERPLANEDESCRIPTORFc.iLayerPlane = (*env)->GetFieldID(env, LAYERPLANEDESCRIPTORFc.clazz, "iLayerPlane", "B");
	LAYERPLANEDESCRIPTORFc.bReserved = (*env)->GetFieldID(env, LAYERPLANEDESCRIPTORFc.clazz, "bReserved", "B");
	LAYERPLANEDESCRIPTORFc.crTransparent = (*env)->GetFieldID(env, LAYERPLANEDESCRIPTORFc.clazz, "crTransparent", "I");
	LAYERPLANEDESCRIPTORFc.cached = 1;
}

LAYERPLANEDESCRIPTOR *getLAYERPLANEDESCRIPTORFields(JNIEnv *env, jobject lpObject, LAYERPLANEDESCRIPTOR *lpStruct)
{
	if (!LAYERPLANEDESCRIPTORFc.cached) cacheLAYERPLANEDESCRIPTORFields(env, lpObject);
	lpStruct->nSize = (*env)->GetShortField(env, lpObject, LAYERPLANEDESCRIPTORFc.nSize);
	lpStruct->nVersion = (*env)->GetShortField(env, lpObject, LAYERPLANEDESCRIPTORFc.nVersion);
	lpStruct->dwFlags = (*env)->GetIntField(env, lpObject, LAYERPLANEDESCRIPTORFc.dwFlags);
	lpStruct->iPixelType = (*env)->GetByteField(env, lpObject, LAYERPLANEDESCRIPTORFc.iPixelType);
	lpStruct->cColorBits = (*env)->GetByteField(env, lpObject, LAYERPLANEDESCRIPTORFc.cColorBits);
	lpStruct->cRedBits = (*env)->GetByteField(env, lpObject, LAYERPLANEDESCRIPTORFc.cRedBits);
	lpStruct->cRedShift = (*env)->GetByteField(env, lpObject, LAYERPLANEDESCRIPTORFc.cRedShift);
	lpStruct->cGreenBits = (*env)->GetByteField(env, lpObject, LAYERPLANEDESCRIPTORFc.cGreenBits);
	lpStruct->cGreenShift = (*env)->GetByteField(env, lpObject, LAYERPLANEDESCRIPTORFc.cGreenShift);
	lpStruct->cBlueBits = (*env)->GetByteField(env, lpObject, LAYERPLANEDESCRIPTORFc.cBlueBits);
	lpStruct->cBlueShift = (*env)->GetByteField(env, lpObject, LAYERPLANEDESCRIPTORFc.cBlueShift);
	lpStruct->cAlphaBits = (*env)->GetByteField(env, lpObject, LAYERPLANEDESCRIPTORFc.cAlphaBits);
	lpStruct->cAlphaShift = (*env)->GetByteField(env, lpObject, LAYERPLANEDESCRIPTORFc.cAlphaShift);
	lpStruct->cAccumBits = (*env)->GetByteField(env, lpObject, LAYERPLANEDESCRIPTORFc.cAccumBits);
	lpStruct->cAccumRedBits = (*env)->GetByteField(env, lpObject, LAYERPLANEDESCRIPTORFc.cAccumRedBits);
	lpStruct->cAccumGreenBits = (*env)->GetByteField(env, lpObject, LAYERPLANEDESCRIPTORFc.cAccumGreenBits);
	lpStruct->cAccumBlueBits = (*env)->GetByteField(env, lpObject, LAYERPLANEDESCRIPTORFc.cAccumBlueBits);
	lpStruct->cAccumAlphaBits = (*env)->GetByteField(env, lpObject, LAYERPLANEDESCRIPTORFc.cAccumAlphaBits);
	lpStruct->cDepthBits = (*env)->GetByteField(env, lpObject, LAYERPLANEDESCRIPTORFc.cDepthBits);
	lpStruct->cStencilBits = (*env)->GetByteField(env, lpObject, LAYERPLANEDESCRIPTORFc.cStencilBits);
	lpStruct->cAuxBuffers = (*env)->GetByteField(env, lpObject, LAYERPLANEDESCRIPTORFc.cAuxBuffers);
	lpStruct->iLayerPlane = (*env)->GetByteField(env, lpObject, LAYERPLANEDESCRIPTORFc.iLayerPlane);
	lpStruct->bReserved = (*env)->GetByteField(env, lpObject, LAYERPLANEDESCRIPTORFc.bReserved);
	lpStruct->crTransparent = (*env)->GetIntField(env, lpObject, LAYERPLANEDESCRIPTORFc.crTransparent);
	return lpStruct;
}

void setLAYERPLANEDESCRIPTORFields(JNIEnv *env, jobject lpObject, LAYERPLANEDESCRIPTOR *lpStruct)
{
	if (!LAYERPLANEDESCRIPTORFc.cached) cacheLAYERPLANEDESCRIPTORFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, LAYERPLANEDESCRIPTORFc.nSize, (jshort)lpStruct->nSize);
	(*env)->SetShortField(env, lpObject, LAYERPLANEDESCRIPTORFc.nVersion, (jshort)lpStruct->nVersion);
	(*env)->SetIntField(env, lpObject, LAYERPLANEDESCRIPTORFc.dwFlags, (jint)lpStruct->dwFlags);
	(*env)->SetByteField(env, lpObject, LAYERPLANEDESCRIPTORFc.iPixelType, (jbyte)lpStruct->iPixelType);
	(*env)->SetByteField(env, lpObject, LAYERPLANEDESCRIPTORFc.cColorBits, (jbyte)lpStruct->cColorBits);
	(*env)->SetByteField(env, lpObject, LAYERPLANEDESCRIPTORFc.cRedBits, (jbyte)lpStruct->cRedBits);
	(*env)->SetByteField(env, lpObject, LAYERPLANEDESCRIPTORFc.cRedShift, (jbyte)lpStruct->cRedShift);
	(*env)->SetByteField(env, lpObject, LAYERPLANEDESCRIPTORFc.cGreenBits, (jbyte)lpStruct->cGreenBits);
	(*env)->SetByteField(env, lpObject, LAYERPLANEDESCRIPTORFc.cGreenShift, (jbyte)lpStruct->cGreenShift);
	(*env)->SetByteField(env, lpObject, LAYERPLANEDESCRIPTORFc.cBlueBits, (jbyte)lpStruct->cBlueBits);
	(*env)->SetByteField(env, lpObject, LAYERPLANEDESCRIPTORFc.cBlueShift, (jbyte)lpStruct->cBlueShift);
	(*env)->SetByteField(env, lpObject, LAYERPLANEDESCRIPTORFc.cAlphaBits, (jbyte)lpStruct->cAlphaBits);
	(*env)->SetByteField(env, lpObject, LAYERPLANEDESCRIPTORFc.cAlphaShift, (jbyte)lpStruct->cAlphaShift);
	(*env)->SetByteField(env, lpObject, LAYERPLANEDESCRIPTORFc.cAccumBits, (jbyte)lpStruct->cAccumBits);
	(*env)->SetByteField(env, lpObject, LAYERPLANEDESCRIPTORFc.cAccumRedBits, (jbyte)lpStruct->cAccumRedBits);
	(*env)->SetByteField(env, lpObject, LAYERPLANEDESCRIPTORFc.cAccumGreenBits, (jbyte)lpStruct->cAccumGreenBits);
	(*env)->SetByteField(env, lpObject, LAYERPLANEDESCRIPTORFc.cAccumBlueBits, (jbyte)lpStruct->cAccumBlueBits);
	(*env)->SetByteField(env, lpObject, LAYERPLANEDESCRIPTORFc.cAccumAlphaBits, (jbyte)lpStruct->cAccumAlphaBits);
	(*env)->SetByteField(env, lpObject, LAYERPLANEDESCRIPTORFc.cDepthBits, (jbyte)lpStruct->cDepthBits);
	(*env)->SetByteField(env, lpObject, LAYERPLANEDESCRIPTORFc.cStencilBits, (jbyte)lpStruct->cStencilBits);
	(*env)->SetByteField(env, lpObject, LAYERPLANEDESCRIPTORFc.cAuxBuffers, (jbyte)lpStruct->cAuxBuffers);
	(*env)->SetByteField(env, lpObject, LAYERPLANEDESCRIPTORFc.iLayerPlane, (jbyte)lpStruct->iLayerPlane);
	(*env)->SetByteField(env, lpObject, LAYERPLANEDESCRIPTORFc.bReserved, (jbyte)lpStruct->bReserved);
	(*env)->SetIntField(env, lpObject, LAYERPLANEDESCRIPTORFc.crTransparent, (jint)lpStruct->crTransparent);
}
#endif

#ifndef NO_PIXELFORMATDESCRIPTOR
typedef struct PIXELFORMATDESCRIPTOR_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID nSize, nVersion, dwFlags, iPixelType, cColorBits, cRedBits, cRedShift, cGreenBits, cGreenShift, cBlueBits, cBlueShift, cAlphaBits, cAlphaShift, cAccumBits, cAccumRedBits, cAccumGreenBits, cAccumBlueBits, cAccumAlphaBits, cDepthBits, cStencilBits, cAuxBuffers, iLayerType, bReserved, dwLayerMask, dwVisibleMask, dwDamageMask;
} PIXELFORMATDESCRIPTOR_FID_CACHE;

PIXELFORMATDESCRIPTOR_FID_CACHE PIXELFORMATDESCRIPTORFc;

void cachePIXELFORMATDESCRIPTORFields(JNIEnv *env, jobject lpObject)
{
	if (PIXELFORMATDESCRIPTORFc.cached) return;
	PIXELFORMATDESCRIPTORFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PIXELFORMATDESCRIPTORFc.nSize = (*env)->GetFieldID(env, PIXELFORMATDESCRIPTORFc.clazz, "nSize", "S");
	PIXELFORMATDESCRIPTORFc.nVersion = (*env)->GetFieldID(env, PIXELFORMATDESCRIPTORFc.clazz, "nVersion", "S");
	PIXELFORMATDESCRIPTORFc.dwFlags = (*env)->GetFieldID(env, PIXELFORMATDESCRIPTORFc.clazz, "dwFlags", "I");
	PIXELFORMATDESCRIPTORFc.iPixelType = (*env)->GetFieldID(env, PIXELFORMATDESCRIPTORFc.clazz, "iPixelType", "B");
	PIXELFORMATDESCRIPTORFc.cColorBits = (*env)->GetFieldID(env, PIXELFORMATDESCRIPTORFc.clazz, "cColorBits", "B");
	PIXELFORMATDESCRIPTORFc.cRedBits = (*env)->GetFieldID(env, PIXELFORMATDESCRIPTORFc.clazz, "cRedBits", "B");
	PIXELFORMATDESCRIPTORFc.cRedShift = (*env)->GetFieldID(env, PIXELFORMATDESCRIPTORFc.clazz, "cRedShift", "B");
	PIXELFORMATDESCRIPTORFc.cGreenBits = (*env)->GetFieldID(env, PIXELFORMATDESCRIPTORFc.clazz, "cGreenBits", "B");
	PIXELFORMATDESCRIPTORFc.cGreenShift = (*env)->GetFieldID(env, PIXELFORMATDESCRIPTORFc.clazz, "cGreenShift", "B");
	PIXELFORMATDESCRIPTORFc.cBlueBits = (*env)->GetFieldID(env, PIXELFORMATDESCRIPTORFc.clazz, "cBlueBits", "B");
	PIXELFORMATDESCRIPTORFc.cBlueShift = (*env)->GetFieldID(env, PIXELFORMATDESCRIPTORFc.clazz, "cBlueShift", "B");
	PIXELFORMATDESCRIPTORFc.cAlphaBits = (*env)->GetFieldID(env, PIXELFORMATDESCRIPTORFc.clazz, "cAlphaBits", "B");
	PIXELFORMATDESCRIPTORFc.cAlphaShift = (*env)->GetFieldID(env, PIXELFORMATDESCRIPTORFc.clazz, "cAlphaShift", "B");
	PIXELFORMATDESCRIPTORFc.cAccumBits = (*env)->GetFieldID(env, PIXELFORMATDESCRIPTORFc.clazz, "cAccumBits", "B");
	PIXELFORMATDESCRIPTORFc.cAccumRedBits = (*env)->GetFieldID(env, PIXELFORMATDESCRIPTORFc.clazz, "cAccumRedBits", "B");
	PIXELFORMATDESCRIPTORFc.cAccumGreenBits = (*env)->GetFieldID(env, PIXELFORMATDESCRIPTORFc.clazz, "cAccumGreenBits", "B");
	PIXELFORMATDESCRIPTORFc.cAccumBlueBits = (*env)->GetFieldID(env, PIXELFORMATDESCRIPTORFc.clazz, "cAccumBlueBits", "B");
	PIXELFORMATDESCRIPTORFc.cAccumAlphaBits = (*env)->GetFieldID(env, PIXELFORMATDESCRIPTORFc.clazz, "cAccumAlphaBits", "B");
	PIXELFORMATDESCRIPTORFc.cDepthBits = (*env)->GetFieldID(env, PIXELFORMATDESCRIPTORFc.clazz, "cDepthBits", "B");
	PIXELFORMATDESCRIPTORFc.cStencilBits = (*env)->GetFieldID(env, PIXELFORMATDESCRIPTORFc.clazz, "cStencilBits", "B");
	PIXELFORMATDESCRIPTORFc.cAuxBuffers = (*env)->GetFieldID(env, PIXELFORMATDESCRIPTORFc.clazz, "cAuxBuffers", "B");
	PIXELFORMATDESCRIPTORFc.iLayerType = (*env)->GetFieldID(env, PIXELFORMATDESCRIPTORFc.clazz, "iLayerType", "B");
	PIXELFORMATDESCRIPTORFc.bReserved = (*env)->GetFieldID(env, PIXELFORMATDESCRIPTORFc.clazz, "bReserved", "B");
	PIXELFORMATDESCRIPTORFc.dwLayerMask = (*env)->GetFieldID(env, PIXELFORMATDESCRIPTORFc.clazz, "dwLayerMask", "I");
	PIXELFORMATDESCRIPTORFc.dwVisibleMask = (*env)->GetFieldID(env, PIXELFORMATDESCRIPTORFc.clazz, "dwVisibleMask", "I");
	PIXELFORMATDESCRIPTORFc.dwDamageMask = (*env)->GetFieldID(env, PIXELFORMATDESCRIPTORFc.clazz, "dwDamageMask", "I");
	PIXELFORMATDESCRIPTORFc.cached = 1;
}

PIXELFORMATDESCRIPTOR *getPIXELFORMATDESCRIPTORFields(JNIEnv *env, jobject lpObject, PIXELFORMATDESCRIPTOR *lpStruct)
{
	if (!PIXELFORMATDESCRIPTORFc.cached) cachePIXELFORMATDESCRIPTORFields(env, lpObject);
	lpStruct->nSize = (*env)->GetShortField(env, lpObject, PIXELFORMATDESCRIPTORFc.nSize);
	lpStruct->nVersion = (*env)->GetShortField(env, lpObject, PIXELFORMATDESCRIPTORFc.nVersion);
	lpStruct->dwFlags = (*env)->GetIntField(env, lpObject, PIXELFORMATDESCRIPTORFc.dwFlags);
	lpStruct->iPixelType = (*env)->GetByteField(env, lpObject, PIXELFORMATDESCRIPTORFc.iPixelType);
	lpStruct->cColorBits = (*env)->GetByteField(env, lpObject, PIXELFORMATDESCRIPTORFc.cColorBits);
	lpStruct->cRedBits = (*env)->GetByteField(env, lpObject, PIXELFORMATDESCRIPTORFc.cRedBits);
	lpStruct->cRedShift = (*env)->GetByteField(env, lpObject, PIXELFORMATDESCRIPTORFc.cRedShift);
	lpStruct->cGreenBits = (*env)->GetByteField(env, lpObject, PIXELFORMATDESCRIPTORFc.cGreenBits);
	lpStruct->cGreenShift = (*env)->GetByteField(env, lpObject, PIXELFORMATDESCRIPTORFc.cGreenShift);
	lpStruct->cBlueBits = (*env)->GetByteField(env, lpObject, PIXELFORMATDESCRIPTORFc.cBlueBits);
	lpStruct->cBlueShift = (*env)->GetByteField(env, lpObject, PIXELFORMATDESCRIPTORFc.cBlueShift);
	lpStruct->cAlphaBits = (*env)->GetByteField(env, lpObject, PIXELFORMATDESCRIPTORFc.cAlphaBits);
	lpStruct->cAlphaShift = (*env)->GetByteField(env, lpObject, PIXELFORMATDESCRIPTORFc.cAlphaShift);
	lpStruct->cAccumBits = (*env)->GetByteField(env, lpObject, PIXELFORMATDESCRIPTORFc.cAccumBits);
	lpStruct->cAccumRedBits = (*env)->GetByteField(env, lpObject, PIXELFORMATDESCRIPTORFc.cAccumRedBits);
	lpStruct->cAccumGreenBits = (*env)->GetByteField(env, lpObject, PIXELFORMATDESCRIPTORFc.cAccumGreenBits);
	lpStruct->cAccumBlueBits = (*env)->GetByteField(env, lpObject, PIXELFORMATDESCRIPTORFc.cAccumBlueBits);
	lpStruct->cAccumAlphaBits = (*env)->GetByteField(env, lpObject, PIXELFORMATDESCRIPTORFc.cAccumAlphaBits);
	lpStruct->cDepthBits = (*env)->GetByteField(env, lpObject, PIXELFORMATDESCRIPTORFc.cDepthBits);
	lpStruct->cStencilBits = (*env)->GetByteField(env, lpObject, PIXELFORMATDESCRIPTORFc.cStencilBits);
	lpStruct->cAuxBuffers = (*env)->GetByteField(env, lpObject, PIXELFORMATDESCRIPTORFc.cAuxBuffers);
	lpStruct->iLayerType = (*env)->GetByteField(env, lpObject, PIXELFORMATDESCRIPTORFc.iLayerType);
	lpStruct->bReserved = (*env)->GetByteField(env, lpObject, PIXELFORMATDESCRIPTORFc.bReserved);
	lpStruct->dwLayerMask = (*env)->GetIntField(env, lpObject, PIXELFORMATDESCRIPTORFc.dwLayerMask);
	lpStruct->dwVisibleMask = (*env)->GetIntField(env, lpObject, PIXELFORMATDESCRIPTORFc.dwVisibleMask);
	lpStruct->dwDamageMask = (*env)->GetIntField(env, lpObject, PIXELFORMATDESCRIPTORFc.dwDamageMask);
	return lpStruct;
}

void setPIXELFORMATDESCRIPTORFields(JNIEnv *env, jobject lpObject, PIXELFORMATDESCRIPTOR *lpStruct)
{
	if (!PIXELFORMATDESCRIPTORFc.cached) cachePIXELFORMATDESCRIPTORFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, PIXELFORMATDESCRIPTORFc.nSize, (jshort)lpStruct->nSize);
	(*env)->SetShortField(env, lpObject, PIXELFORMATDESCRIPTORFc.nVersion, (jshort)lpStruct->nVersion);
	(*env)->SetIntField(env, lpObject, PIXELFORMATDESCRIPTORFc.dwFlags, (jint)lpStruct->dwFlags);
	(*env)->SetByteField(env, lpObject, PIXELFORMATDESCRIPTORFc.iPixelType, (jbyte)lpStruct->iPixelType);
	(*env)->SetByteField(env, lpObject, PIXELFORMATDESCRIPTORFc.cColorBits, (jbyte)lpStruct->cColorBits);
	(*env)->SetByteField(env, lpObject, PIXELFORMATDESCRIPTORFc.cRedBits, (jbyte)lpStruct->cRedBits);
	(*env)->SetByteField(env, lpObject, PIXELFORMATDESCRIPTORFc.cRedShift, (jbyte)lpStruct->cRedShift);
	(*env)->SetByteField(env, lpObject, PIXELFORMATDESCRIPTORFc.cGreenBits, (jbyte)lpStruct->cGreenBits);
	(*env)->SetByteField(env, lpObject, PIXELFORMATDESCRIPTORFc.cGreenShift, (jbyte)lpStruct->cGreenShift);
	(*env)->SetByteField(env, lpObject, PIXELFORMATDESCRIPTORFc.cBlueBits, (jbyte)lpStruct->cBlueBits);
	(*env)->SetByteField(env, lpObject, PIXELFORMATDESCRIPTORFc.cBlueShift, (jbyte)lpStruct->cBlueShift);
	(*env)->SetByteField(env, lpObject, PIXELFORMATDESCRIPTORFc.cAlphaBits, (jbyte)lpStruct->cAlphaBits);
	(*env)->SetByteField(env, lpObject, PIXELFORMATDESCRIPTORFc.cAlphaShift, (jbyte)lpStruct->cAlphaShift);
	(*env)->SetByteField(env, lpObject, PIXELFORMATDESCRIPTORFc.cAccumBits, (jbyte)lpStruct->cAccumBits);
	(*env)->SetByteField(env, lpObject, PIXELFORMATDESCRIPTORFc.cAccumRedBits, (jbyte)lpStruct->cAccumRedBits);
	(*env)->SetByteField(env, lpObject, PIXELFORMATDESCRIPTORFc.cAccumGreenBits, (jbyte)lpStruct->cAccumGreenBits);
	(*env)->SetByteField(env, lpObject, PIXELFORMATDESCRIPTORFc.cAccumBlueBits, (jbyte)lpStruct->cAccumBlueBits);
	(*env)->SetByteField(env, lpObject, PIXELFORMATDESCRIPTORFc.cAccumAlphaBits, (jbyte)lpStruct->cAccumAlphaBits);
	(*env)->SetByteField(env, lpObject, PIXELFORMATDESCRIPTORFc.cDepthBits, (jbyte)lpStruct->cDepthBits);
	(*env)->SetByteField(env, lpObject, PIXELFORMATDESCRIPTORFc.cStencilBits, (jbyte)lpStruct->cStencilBits);
	(*env)->SetByteField(env, lpObject, PIXELFORMATDESCRIPTORFc.cAuxBuffers, (jbyte)lpStruct->cAuxBuffers);
	(*env)->SetByteField(env, lpObject, PIXELFORMATDESCRIPTORFc.iLayerType, (jbyte)lpStruct->iLayerType);
	(*env)->SetByteField(env, lpObject, PIXELFORMATDESCRIPTORFc.bReserved, (jbyte)lpStruct->bReserved);
	(*env)->SetIntField(env, lpObject, PIXELFORMATDESCRIPTORFc.dwLayerMask, (jint)lpStruct->dwLayerMask);
	(*env)->SetIntField(env, lpObject, PIXELFORMATDESCRIPTORFc.dwVisibleMask, (jint)lpStruct->dwVisibleMask);
	(*env)->SetIntField(env, lpObject, PIXELFORMATDESCRIPTORFc.dwDamageMask, (jint)lpStruct->dwDamageMask);
}
#endif

