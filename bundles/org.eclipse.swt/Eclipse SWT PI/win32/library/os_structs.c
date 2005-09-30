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
#include "os_structs.h"

#ifndef NO_ACCEL
typedef struct ACCEL_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID fVirt, key, cmd;
} ACCEL_FID_CACHE;

ACCEL_FID_CACHE ACCELFc;

void cacheACCELFields(JNIEnv *env, jobject lpObject)
{
	if (ACCELFc.cached) return;
	ACCELFc.clazz = (*env)->GetObjectClass(env, lpObject);
	ACCELFc.fVirt = (*env)->GetFieldID(env, ACCELFc.clazz, "fVirt", "B");
	ACCELFc.key = (*env)->GetFieldID(env, ACCELFc.clazz, "key", "S");
	ACCELFc.cmd = (*env)->GetFieldID(env, ACCELFc.clazz, "cmd", "S");
	ACCELFc.cached = 1;
}

ACCEL *getACCELFields(JNIEnv *env, jobject lpObject, ACCEL *lpStruct)
{
	if (!ACCELFc.cached) cacheACCELFields(env, lpObject);
	lpStruct->fVirt = (*env)->GetByteField(env, lpObject, ACCELFc.fVirt);
	lpStruct->key = (*env)->GetShortField(env, lpObject, ACCELFc.key);
	lpStruct->cmd = (*env)->GetShortField(env, lpObject, ACCELFc.cmd);
	return lpStruct;
}

void setACCELFields(JNIEnv *env, jobject lpObject, ACCEL *lpStruct)
{
	if (!ACCELFc.cached) cacheACCELFields(env, lpObject);
	(*env)->SetByteField(env, lpObject, ACCELFc.fVirt, (jbyte)lpStruct->fVirt);
	(*env)->SetShortField(env, lpObject, ACCELFc.key, (jshort)lpStruct->key);
	(*env)->SetShortField(env, lpObject, ACCELFc.cmd, (jshort)lpStruct->cmd);
}
#endif

#ifndef NO_BITMAP
typedef struct BITMAP_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID bmType, bmWidth, bmHeight, bmWidthBytes, bmPlanes, bmBitsPixel, bmBits;
} BITMAP_FID_CACHE;

BITMAP_FID_CACHE BITMAPFc;

void cacheBITMAPFields(JNIEnv *env, jobject lpObject)
{
	if (BITMAPFc.cached) return;
	BITMAPFc.clazz = (*env)->GetObjectClass(env, lpObject);
	BITMAPFc.bmType = (*env)->GetFieldID(env, BITMAPFc.clazz, "bmType", "I");
	BITMAPFc.bmWidth = (*env)->GetFieldID(env, BITMAPFc.clazz, "bmWidth", "I");
	BITMAPFc.bmHeight = (*env)->GetFieldID(env, BITMAPFc.clazz, "bmHeight", "I");
	BITMAPFc.bmWidthBytes = (*env)->GetFieldID(env, BITMAPFc.clazz, "bmWidthBytes", "I");
	BITMAPFc.bmPlanes = (*env)->GetFieldID(env, BITMAPFc.clazz, "bmPlanes", "S");
	BITMAPFc.bmBitsPixel = (*env)->GetFieldID(env, BITMAPFc.clazz, "bmBitsPixel", "S");
	BITMAPFc.bmBits = (*env)->GetFieldID(env, BITMAPFc.clazz, "bmBits", "I");
	BITMAPFc.cached = 1;
}

BITMAP *getBITMAPFields(JNIEnv *env, jobject lpObject, BITMAP *lpStruct)
{
	if (!BITMAPFc.cached) cacheBITMAPFields(env, lpObject);
	lpStruct->bmType = (*env)->GetIntField(env, lpObject, BITMAPFc.bmType);
	lpStruct->bmWidth = (*env)->GetIntField(env, lpObject, BITMAPFc.bmWidth);
	lpStruct->bmHeight = (*env)->GetIntField(env, lpObject, BITMAPFc.bmHeight);
	lpStruct->bmWidthBytes = (*env)->GetIntField(env, lpObject, BITMAPFc.bmWidthBytes);
	lpStruct->bmPlanes = (*env)->GetShortField(env, lpObject, BITMAPFc.bmPlanes);
	lpStruct->bmBitsPixel = (*env)->GetShortField(env, lpObject, BITMAPFc.bmBitsPixel);
	lpStruct->bmBits = (LPVOID)(*env)->GetIntField(env, lpObject, BITMAPFc.bmBits);
	return lpStruct;
}

void setBITMAPFields(JNIEnv *env, jobject lpObject, BITMAP *lpStruct)
{
	if (!BITMAPFc.cached) cacheBITMAPFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, BITMAPFc.bmType, (jint)lpStruct->bmType);
	(*env)->SetIntField(env, lpObject, BITMAPFc.bmWidth, (jint)lpStruct->bmWidth);
	(*env)->SetIntField(env, lpObject, BITMAPFc.bmHeight, (jint)lpStruct->bmHeight);
	(*env)->SetIntField(env, lpObject, BITMAPFc.bmWidthBytes, (jint)lpStruct->bmWidthBytes);
	(*env)->SetShortField(env, lpObject, BITMAPFc.bmPlanes, (jshort)lpStruct->bmPlanes);
	(*env)->SetShortField(env, lpObject, BITMAPFc.bmBitsPixel, (jshort)lpStruct->bmBitsPixel);
	(*env)->SetIntField(env, lpObject, BITMAPFc.bmBits, (jint)lpStruct->bmBits);
}
#endif

#ifndef NO_BITMAPINFOHEADER
typedef struct BITMAPINFOHEADER_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID biSize, biWidth, biHeight, biPlanes, biBitCount, biCompression, biSizeImage, biXPelsPerMeter, biYPelsPerMeter, biClrUsed, biClrImportant;
} BITMAPINFOHEADER_FID_CACHE;

BITMAPINFOHEADER_FID_CACHE BITMAPINFOHEADERFc;

void cacheBITMAPINFOHEADERFields(JNIEnv *env, jobject lpObject)
{
	if (BITMAPINFOHEADERFc.cached) return;
	BITMAPINFOHEADERFc.clazz = (*env)->GetObjectClass(env, lpObject);
	BITMAPINFOHEADERFc.biSize = (*env)->GetFieldID(env, BITMAPINFOHEADERFc.clazz, "biSize", "I");
	BITMAPINFOHEADERFc.biWidth = (*env)->GetFieldID(env, BITMAPINFOHEADERFc.clazz, "biWidth", "I");
	BITMAPINFOHEADERFc.biHeight = (*env)->GetFieldID(env, BITMAPINFOHEADERFc.clazz, "biHeight", "I");
	BITMAPINFOHEADERFc.biPlanes = (*env)->GetFieldID(env, BITMAPINFOHEADERFc.clazz, "biPlanes", "S");
	BITMAPINFOHEADERFc.biBitCount = (*env)->GetFieldID(env, BITMAPINFOHEADERFc.clazz, "biBitCount", "S");
	BITMAPINFOHEADERFc.biCompression = (*env)->GetFieldID(env, BITMAPINFOHEADERFc.clazz, "biCompression", "I");
	BITMAPINFOHEADERFc.biSizeImage = (*env)->GetFieldID(env, BITMAPINFOHEADERFc.clazz, "biSizeImage", "I");
	BITMAPINFOHEADERFc.biXPelsPerMeter = (*env)->GetFieldID(env, BITMAPINFOHEADERFc.clazz, "biXPelsPerMeter", "I");
	BITMAPINFOHEADERFc.biYPelsPerMeter = (*env)->GetFieldID(env, BITMAPINFOHEADERFc.clazz, "biYPelsPerMeter", "I");
	BITMAPINFOHEADERFc.biClrUsed = (*env)->GetFieldID(env, BITMAPINFOHEADERFc.clazz, "biClrUsed", "I");
	BITMAPINFOHEADERFc.biClrImportant = (*env)->GetFieldID(env, BITMAPINFOHEADERFc.clazz, "biClrImportant", "I");
	BITMAPINFOHEADERFc.cached = 1;
}

BITMAPINFOHEADER *getBITMAPINFOHEADERFields(JNIEnv *env, jobject lpObject, BITMAPINFOHEADER *lpStruct)
{
	if (!BITMAPINFOHEADERFc.cached) cacheBITMAPINFOHEADERFields(env, lpObject);
	lpStruct->biSize = (*env)->GetIntField(env, lpObject, BITMAPINFOHEADERFc.biSize);
	lpStruct->biWidth = (*env)->GetIntField(env, lpObject, BITMAPINFOHEADERFc.biWidth);
	lpStruct->biHeight = (*env)->GetIntField(env, lpObject, BITMAPINFOHEADERFc.biHeight);
	lpStruct->biPlanes = (*env)->GetShortField(env, lpObject, BITMAPINFOHEADERFc.biPlanes);
	lpStruct->biBitCount = (*env)->GetShortField(env, lpObject, BITMAPINFOHEADERFc.biBitCount);
	lpStruct->biCompression = (*env)->GetIntField(env, lpObject, BITMAPINFOHEADERFc.biCompression);
	lpStruct->biSizeImage = (*env)->GetIntField(env, lpObject, BITMAPINFOHEADERFc.biSizeImage);
	lpStruct->biXPelsPerMeter = (*env)->GetIntField(env, lpObject, BITMAPINFOHEADERFc.biXPelsPerMeter);
	lpStruct->biYPelsPerMeter = (*env)->GetIntField(env, lpObject, BITMAPINFOHEADERFc.biYPelsPerMeter);
	lpStruct->biClrUsed = (*env)->GetIntField(env, lpObject, BITMAPINFOHEADERFc.biClrUsed);
	lpStruct->biClrImportant = (*env)->GetIntField(env, lpObject, BITMAPINFOHEADERFc.biClrImportant);
	return lpStruct;
}

void setBITMAPINFOHEADERFields(JNIEnv *env, jobject lpObject, BITMAPINFOHEADER *lpStruct)
{
	if (!BITMAPINFOHEADERFc.cached) cacheBITMAPINFOHEADERFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, BITMAPINFOHEADERFc.biSize, (jint)lpStruct->biSize);
	(*env)->SetIntField(env, lpObject, BITMAPINFOHEADERFc.biWidth, (jint)lpStruct->biWidth);
	(*env)->SetIntField(env, lpObject, BITMAPINFOHEADERFc.biHeight, (jint)lpStruct->biHeight);
	(*env)->SetShortField(env, lpObject, BITMAPINFOHEADERFc.biPlanes, (jshort)lpStruct->biPlanes);
	(*env)->SetShortField(env, lpObject, BITMAPINFOHEADERFc.biBitCount, (jshort)lpStruct->biBitCount);
	(*env)->SetIntField(env, lpObject, BITMAPINFOHEADERFc.biCompression, (jint)lpStruct->biCompression);
	(*env)->SetIntField(env, lpObject, BITMAPINFOHEADERFc.biSizeImage, (jint)lpStruct->biSizeImage);
	(*env)->SetIntField(env, lpObject, BITMAPINFOHEADERFc.biXPelsPerMeter, (jint)lpStruct->biXPelsPerMeter);
	(*env)->SetIntField(env, lpObject, BITMAPINFOHEADERFc.biYPelsPerMeter, (jint)lpStruct->biYPelsPerMeter);
	(*env)->SetIntField(env, lpObject, BITMAPINFOHEADERFc.biClrUsed, (jint)lpStruct->biClrUsed);
	(*env)->SetIntField(env, lpObject, BITMAPINFOHEADERFc.biClrImportant, (jint)lpStruct->biClrImportant);
}
#endif

#ifndef NO_BLENDFUNCTION
typedef struct BLENDFUNCTION_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID BlendOp, BlendFlags, SourceConstantAlpha, AlphaFormat;
} BLENDFUNCTION_FID_CACHE;

BLENDFUNCTION_FID_CACHE BLENDFUNCTIONFc;

void cacheBLENDFUNCTIONFields(JNIEnv *env, jobject lpObject)
{
	if (BLENDFUNCTIONFc.cached) return;
	BLENDFUNCTIONFc.clazz = (*env)->GetObjectClass(env, lpObject);
	BLENDFUNCTIONFc.BlendOp = (*env)->GetFieldID(env, BLENDFUNCTIONFc.clazz, "BlendOp", "B");
	BLENDFUNCTIONFc.BlendFlags = (*env)->GetFieldID(env, BLENDFUNCTIONFc.clazz, "BlendFlags", "B");
	BLENDFUNCTIONFc.SourceConstantAlpha = (*env)->GetFieldID(env, BLENDFUNCTIONFc.clazz, "SourceConstantAlpha", "B");
	BLENDFUNCTIONFc.AlphaFormat = (*env)->GetFieldID(env, BLENDFUNCTIONFc.clazz, "AlphaFormat", "B");
	BLENDFUNCTIONFc.cached = 1;
}

BLENDFUNCTION *getBLENDFUNCTIONFields(JNIEnv *env, jobject lpObject, BLENDFUNCTION *lpStruct)
{
	if (!BLENDFUNCTIONFc.cached) cacheBLENDFUNCTIONFields(env, lpObject);
	lpStruct->BlendOp = (*env)->GetByteField(env, lpObject, BLENDFUNCTIONFc.BlendOp);
	lpStruct->BlendFlags = (*env)->GetByteField(env, lpObject, BLENDFUNCTIONFc.BlendFlags);
	lpStruct->SourceConstantAlpha = (*env)->GetByteField(env, lpObject, BLENDFUNCTIONFc.SourceConstantAlpha);
	lpStruct->AlphaFormat = (*env)->GetByteField(env, lpObject, BLENDFUNCTIONFc.AlphaFormat);
	return lpStruct;
}

void setBLENDFUNCTIONFields(JNIEnv *env, jobject lpObject, BLENDFUNCTION *lpStruct)
{
	if (!BLENDFUNCTIONFc.cached) cacheBLENDFUNCTIONFields(env, lpObject);
	(*env)->SetByteField(env, lpObject, BLENDFUNCTIONFc.BlendOp, (jbyte)lpStruct->BlendOp);
	(*env)->SetByteField(env, lpObject, BLENDFUNCTIONFc.BlendFlags, (jbyte)lpStruct->BlendFlags);
	(*env)->SetByteField(env, lpObject, BLENDFUNCTIONFc.SourceConstantAlpha, (jbyte)lpStruct->SourceConstantAlpha);
	(*env)->SetByteField(env, lpObject, BLENDFUNCTIONFc.AlphaFormat, (jbyte)lpStruct->AlphaFormat);
}
#endif

#ifndef NO_BROWSEINFO
typedef struct BROWSEINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID hwndOwner, pidlRoot, pszDisplayName, lpszTitle, ulFlags, lpfn, lParam, iImage;
} BROWSEINFO_FID_CACHE;

BROWSEINFO_FID_CACHE BROWSEINFOFc;

void cacheBROWSEINFOFields(JNIEnv *env, jobject lpObject)
{
	if (BROWSEINFOFc.cached) return;
	BROWSEINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	BROWSEINFOFc.hwndOwner = (*env)->GetFieldID(env, BROWSEINFOFc.clazz, "hwndOwner", "I");
	BROWSEINFOFc.pidlRoot = (*env)->GetFieldID(env, BROWSEINFOFc.clazz, "pidlRoot", "I");
	BROWSEINFOFc.pszDisplayName = (*env)->GetFieldID(env, BROWSEINFOFc.clazz, "pszDisplayName", "I");
	BROWSEINFOFc.lpszTitle = (*env)->GetFieldID(env, BROWSEINFOFc.clazz, "lpszTitle", "I");
	BROWSEINFOFc.ulFlags = (*env)->GetFieldID(env, BROWSEINFOFc.clazz, "ulFlags", "I");
	BROWSEINFOFc.lpfn = (*env)->GetFieldID(env, BROWSEINFOFc.clazz, "lpfn", "I");
	BROWSEINFOFc.lParam = (*env)->GetFieldID(env, BROWSEINFOFc.clazz, "lParam", "I");
	BROWSEINFOFc.iImage = (*env)->GetFieldID(env, BROWSEINFOFc.clazz, "iImage", "I");
	BROWSEINFOFc.cached = 1;
}

BROWSEINFO *getBROWSEINFOFields(JNIEnv *env, jobject lpObject, BROWSEINFO *lpStruct)
{
	if (!BROWSEINFOFc.cached) cacheBROWSEINFOFields(env, lpObject);
	lpStruct->hwndOwner = (HWND)(*env)->GetIntField(env, lpObject, BROWSEINFOFc.hwndOwner);
	lpStruct->pidlRoot = (LPCITEMIDLIST)(*env)->GetIntField(env, lpObject, BROWSEINFOFc.pidlRoot);
	lpStruct->pszDisplayName = (LPTSTR)(*env)->GetIntField(env, lpObject, BROWSEINFOFc.pszDisplayName);
	lpStruct->lpszTitle = (LPCTSTR)(*env)->GetIntField(env, lpObject, BROWSEINFOFc.lpszTitle);
	lpStruct->ulFlags = (*env)->GetIntField(env, lpObject, BROWSEINFOFc.ulFlags);
	lpStruct->lpfn = (BFFCALLBACK)(*env)->GetIntField(env, lpObject, BROWSEINFOFc.lpfn);
	lpStruct->lParam = (*env)->GetIntField(env, lpObject, BROWSEINFOFc.lParam);
	lpStruct->iImage = (*env)->GetIntField(env, lpObject, BROWSEINFOFc.iImage);
	return lpStruct;
}

void setBROWSEINFOFields(JNIEnv *env, jobject lpObject, BROWSEINFO *lpStruct)
{
	if (!BROWSEINFOFc.cached) cacheBROWSEINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, BROWSEINFOFc.hwndOwner, (jint)lpStruct->hwndOwner);
	(*env)->SetIntField(env, lpObject, BROWSEINFOFc.pidlRoot, (jint)lpStruct->pidlRoot);
	(*env)->SetIntField(env, lpObject, BROWSEINFOFc.pszDisplayName, (jint)lpStruct->pszDisplayName);
	(*env)->SetIntField(env, lpObject, BROWSEINFOFc.lpszTitle, (jint)lpStruct->lpszTitle);
	(*env)->SetIntField(env, lpObject, BROWSEINFOFc.ulFlags, (jint)lpStruct->ulFlags);
	(*env)->SetIntField(env, lpObject, BROWSEINFOFc.lpfn, (jint)lpStruct->lpfn);
	(*env)->SetIntField(env, lpObject, BROWSEINFOFc.lParam, (jint)lpStruct->lParam);
	(*env)->SetIntField(env, lpObject, BROWSEINFOFc.iImage, (jint)lpStruct->iImage);
}
#endif

#ifndef NO_BUTTON_IMAGELIST
typedef struct BUTTON_IMAGELIST_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID himl, margin_left, margin_top, margin_right, margin_bottom, uAlign;
} BUTTON_IMAGELIST_FID_CACHE;

BUTTON_IMAGELIST_FID_CACHE BUTTON_IMAGELISTFc;

void cacheBUTTON_IMAGELISTFields(JNIEnv *env, jobject lpObject)
{
	if (BUTTON_IMAGELISTFc.cached) return;
	BUTTON_IMAGELISTFc.clazz = (*env)->GetObjectClass(env, lpObject);
	BUTTON_IMAGELISTFc.himl = (*env)->GetFieldID(env, BUTTON_IMAGELISTFc.clazz, "himl", "I");
	BUTTON_IMAGELISTFc.margin_left = (*env)->GetFieldID(env, BUTTON_IMAGELISTFc.clazz, "margin_left", "I");
	BUTTON_IMAGELISTFc.margin_top = (*env)->GetFieldID(env, BUTTON_IMAGELISTFc.clazz, "margin_top", "I");
	BUTTON_IMAGELISTFc.margin_right = (*env)->GetFieldID(env, BUTTON_IMAGELISTFc.clazz, "margin_right", "I");
	BUTTON_IMAGELISTFc.margin_bottom = (*env)->GetFieldID(env, BUTTON_IMAGELISTFc.clazz, "margin_bottom", "I");
	BUTTON_IMAGELISTFc.uAlign = (*env)->GetFieldID(env, BUTTON_IMAGELISTFc.clazz, "uAlign", "I");
	BUTTON_IMAGELISTFc.cached = 1;
}

BUTTON_IMAGELIST *getBUTTON_IMAGELISTFields(JNIEnv *env, jobject lpObject, BUTTON_IMAGELIST *lpStruct)
{
	if (!BUTTON_IMAGELISTFc.cached) cacheBUTTON_IMAGELISTFields(env, lpObject);
	lpStruct->himl = (HIMAGELIST)(*env)->GetIntField(env, lpObject, BUTTON_IMAGELISTFc.himl);
	lpStruct->margin.left = (LONG)(*env)->GetIntField(env, lpObject, BUTTON_IMAGELISTFc.margin_left);
	lpStruct->margin.top = (LONG)(*env)->GetIntField(env, lpObject, BUTTON_IMAGELISTFc.margin_top);
	lpStruct->margin.right = (LONG)(*env)->GetIntField(env, lpObject, BUTTON_IMAGELISTFc.margin_right);
	lpStruct->margin.bottom = (LONG)(*env)->GetIntField(env, lpObject, BUTTON_IMAGELISTFc.margin_bottom);
	lpStruct->uAlign = (UINT)(*env)->GetIntField(env, lpObject, BUTTON_IMAGELISTFc.uAlign);
	return lpStruct;
}

void setBUTTON_IMAGELISTFields(JNIEnv *env, jobject lpObject, BUTTON_IMAGELIST *lpStruct)
{
	if (!BUTTON_IMAGELISTFc.cached) cacheBUTTON_IMAGELISTFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, BUTTON_IMAGELISTFc.himl, (jint)lpStruct->himl);
	(*env)->SetIntField(env, lpObject, BUTTON_IMAGELISTFc.margin_left, (jint)lpStruct->margin.left);
	(*env)->SetIntField(env, lpObject, BUTTON_IMAGELISTFc.margin_top, (jint)lpStruct->margin.top);
	(*env)->SetIntField(env, lpObject, BUTTON_IMAGELISTFc.margin_right, (jint)lpStruct->margin.right);
	(*env)->SetIntField(env, lpObject, BUTTON_IMAGELISTFc.margin_bottom, (jint)lpStruct->margin.bottom);
	(*env)->SetIntField(env, lpObject, BUTTON_IMAGELISTFc.uAlign, (jint)lpStruct->uAlign);
}
#endif

#ifndef NO_CHOOSECOLOR
typedef struct CHOOSECOLOR_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lStructSize, hwndOwner, hInstance, rgbResult, lpCustColors, Flags, lCustData, lpfnHook, lpTemplateName;
} CHOOSECOLOR_FID_CACHE;

CHOOSECOLOR_FID_CACHE CHOOSECOLORFc;

void cacheCHOOSECOLORFields(JNIEnv *env, jobject lpObject)
{
	if (CHOOSECOLORFc.cached) return;
	CHOOSECOLORFc.clazz = (*env)->GetObjectClass(env, lpObject);
	CHOOSECOLORFc.lStructSize = (*env)->GetFieldID(env, CHOOSECOLORFc.clazz, "lStructSize", "I");
	CHOOSECOLORFc.hwndOwner = (*env)->GetFieldID(env, CHOOSECOLORFc.clazz, "hwndOwner", "I");
	CHOOSECOLORFc.hInstance = (*env)->GetFieldID(env, CHOOSECOLORFc.clazz, "hInstance", "I");
	CHOOSECOLORFc.rgbResult = (*env)->GetFieldID(env, CHOOSECOLORFc.clazz, "rgbResult", "I");
	CHOOSECOLORFc.lpCustColors = (*env)->GetFieldID(env, CHOOSECOLORFc.clazz, "lpCustColors", "I");
	CHOOSECOLORFc.Flags = (*env)->GetFieldID(env, CHOOSECOLORFc.clazz, "Flags", "I");
	CHOOSECOLORFc.lCustData = (*env)->GetFieldID(env, CHOOSECOLORFc.clazz, "lCustData", "I");
	CHOOSECOLORFc.lpfnHook = (*env)->GetFieldID(env, CHOOSECOLORFc.clazz, "lpfnHook", "I");
	CHOOSECOLORFc.lpTemplateName = (*env)->GetFieldID(env, CHOOSECOLORFc.clazz, "lpTemplateName", "I");
	CHOOSECOLORFc.cached = 1;
}

CHOOSECOLOR *getCHOOSECOLORFields(JNIEnv *env, jobject lpObject, CHOOSECOLOR *lpStruct)
{
	if (!CHOOSECOLORFc.cached) cacheCHOOSECOLORFields(env, lpObject);
	lpStruct->lStructSize = (*env)->GetIntField(env, lpObject, CHOOSECOLORFc.lStructSize);
	lpStruct->hwndOwner = (HWND)(*env)->GetIntField(env, lpObject, CHOOSECOLORFc.hwndOwner);
	lpStruct->hInstance = (HANDLE)(*env)->GetIntField(env, lpObject, CHOOSECOLORFc.hInstance);
	lpStruct->rgbResult = (*env)->GetIntField(env, lpObject, CHOOSECOLORFc.rgbResult);
	lpStruct->lpCustColors = (COLORREF *)(*env)->GetIntField(env, lpObject, CHOOSECOLORFc.lpCustColors);
	lpStruct->Flags = (*env)->GetIntField(env, lpObject, CHOOSECOLORFc.Flags);
	lpStruct->lCustData = (*env)->GetIntField(env, lpObject, CHOOSECOLORFc.lCustData);
	lpStruct->lpfnHook = (LPCCHOOKPROC)(*env)->GetIntField(env, lpObject, CHOOSECOLORFc.lpfnHook);
	lpStruct->lpTemplateName = (LPCTSTR)(*env)->GetIntField(env, lpObject, CHOOSECOLORFc.lpTemplateName);
	return lpStruct;
}

void setCHOOSECOLORFields(JNIEnv *env, jobject lpObject, CHOOSECOLOR *lpStruct)
{
	if (!CHOOSECOLORFc.cached) cacheCHOOSECOLORFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, CHOOSECOLORFc.lStructSize, (jint)lpStruct->lStructSize);
	(*env)->SetIntField(env, lpObject, CHOOSECOLORFc.hwndOwner, (jint)lpStruct->hwndOwner);
	(*env)->SetIntField(env, lpObject, CHOOSECOLORFc.hInstance, (jint)lpStruct->hInstance);
	(*env)->SetIntField(env, lpObject, CHOOSECOLORFc.rgbResult, (jint)lpStruct->rgbResult);
	(*env)->SetIntField(env, lpObject, CHOOSECOLORFc.lpCustColors, (jint)lpStruct->lpCustColors);
	(*env)->SetIntField(env, lpObject, CHOOSECOLORFc.Flags, (jint)lpStruct->Flags);
	(*env)->SetIntField(env, lpObject, CHOOSECOLORFc.lCustData, (jint)lpStruct->lCustData);
	(*env)->SetIntField(env, lpObject, CHOOSECOLORFc.lpfnHook, (jint)lpStruct->lpfnHook);
	(*env)->SetIntField(env, lpObject, CHOOSECOLORFc.lpTemplateName, (jint)lpStruct->lpTemplateName);
}
#endif

#ifndef NO_CHOOSEFONT
typedef struct CHOOSEFONT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lStructSize, hwndOwner, hDC, lpLogFont, iPointSize, Flags, rgbColors, lCustData, lpfnHook, lpTemplateName, hInstance, lpszStyle, nFontType, nSizeMin, nSizeMax;
} CHOOSEFONT_FID_CACHE;

CHOOSEFONT_FID_CACHE CHOOSEFONTFc;

void cacheCHOOSEFONTFields(JNIEnv *env, jobject lpObject)
{
	if (CHOOSEFONTFc.cached) return;
	CHOOSEFONTFc.clazz = (*env)->GetObjectClass(env, lpObject);
	CHOOSEFONTFc.lStructSize = (*env)->GetFieldID(env, CHOOSEFONTFc.clazz, "lStructSize", "I");
	CHOOSEFONTFc.hwndOwner = (*env)->GetFieldID(env, CHOOSEFONTFc.clazz, "hwndOwner", "I");
	CHOOSEFONTFc.hDC = (*env)->GetFieldID(env, CHOOSEFONTFc.clazz, "hDC", "I");
	CHOOSEFONTFc.lpLogFont = (*env)->GetFieldID(env, CHOOSEFONTFc.clazz, "lpLogFont", "I");
	CHOOSEFONTFc.iPointSize = (*env)->GetFieldID(env, CHOOSEFONTFc.clazz, "iPointSize", "I");
	CHOOSEFONTFc.Flags = (*env)->GetFieldID(env, CHOOSEFONTFc.clazz, "Flags", "I");
	CHOOSEFONTFc.rgbColors = (*env)->GetFieldID(env, CHOOSEFONTFc.clazz, "rgbColors", "I");
	CHOOSEFONTFc.lCustData = (*env)->GetFieldID(env, CHOOSEFONTFc.clazz, "lCustData", "I");
	CHOOSEFONTFc.lpfnHook = (*env)->GetFieldID(env, CHOOSEFONTFc.clazz, "lpfnHook", "I");
	CHOOSEFONTFc.lpTemplateName = (*env)->GetFieldID(env, CHOOSEFONTFc.clazz, "lpTemplateName", "I");
	CHOOSEFONTFc.hInstance = (*env)->GetFieldID(env, CHOOSEFONTFc.clazz, "hInstance", "I");
	CHOOSEFONTFc.lpszStyle = (*env)->GetFieldID(env, CHOOSEFONTFc.clazz, "lpszStyle", "I");
	CHOOSEFONTFc.nFontType = (*env)->GetFieldID(env, CHOOSEFONTFc.clazz, "nFontType", "S");
	CHOOSEFONTFc.nSizeMin = (*env)->GetFieldID(env, CHOOSEFONTFc.clazz, "nSizeMin", "I");
	CHOOSEFONTFc.nSizeMax = (*env)->GetFieldID(env, CHOOSEFONTFc.clazz, "nSizeMax", "I");
	CHOOSEFONTFc.cached = 1;
}

CHOOSEFONT *getCHOOSEFONTFields(JNIEnv *env, jobject lpObject, CHOOSEFONT *lpStruct)
{
	if (!CHOOSEFONTFc.cached) cacheCHOOSEFONTFields(env, lpObject);
	lpStruct->lStructSize = (*env)->GetIntField(env, lpObject, CHOOSEFONTFc.lStructSize);
	lpStruct->hwndOwner = (HWND)(*env)->GetIntField(env, lpObject, CHOOSEFONTFc.hwndOwner);
	lpStruct->hDC = (HDC)(*env)->GetIntField(env, lpObject, CHOOSEFONTFc.hDC);
	lpStruct->lpLogFont = (LPLOGFONT)(*env)->GetIntField(env, lpObject, CHOOSEFONTFc.lpLogFont);
	lpStruct->iPointSize = (*env)->GetIntField(env, lpObject, CHOOSEFONTFc.iPointSize);
	lpStruct->Flags = (*env)->GetIntField(env, lpObject, CHOOSEFONTFc.Flags);
	lpStruct->rgbColors = (*env)->GetIntField(env, lpObject, CHOOSEFONTFc.rgbColors);
	lpStruct->lCustData = (*env)->GetIntField(env, lpObject, CHOOSEFONTFc.lCustData);
	lpStruct->lpfnHook = (LPCFHOOKPROC)(*env)->GetIntField(env, lpObject, CHOOSEFONTFc.lpfnHook);
	lpStruct->lpTemplateName = (LPCTSTR)(*env)->GetIntField(env, lpObject, CHOOSEFONTFc.lpTemplateName);
	lpStruct->hInstance = (HINSTANCE)(*env)->GetIntField(env, lpObject, CHOOSEFONTFc.hInstance);
	lpStruct->lpszStyle = (LPTSTR)(*env)->GetIntField(env, lpObject, CHOOSEFONTFc.lpszStyle);
	lpStruct->nFontType = (*env)->GetShortField(env, lpObject, CHOOSEFONTFc.nFontType);
	lpStruct->nSizeMin = (*env)->GetIntField(env, lpObject, CHOOSEFONTFc.nSizeMin);
	lpStruct->nSizeMax = (*env)->GetIntField(env, lpObject, CHOOSEFONTFc.nSizeMax);
	return lpStruct;
}

void setCHOOSEFONTFields(JNIEnv *env, jobject lpObject, CHOOSEFONT *lpStruct)
{
	if (!CHOOSEFONTFc.cached) cacheCHOOSEFONTFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, CHOOSEFONTFc.lStructSize, (jint)lpStruct->lStructSize);
	(*env)->SetIntField(env, lpObject, CHOOSEFONTFc.hwndOwner, (jint)lpStruct->hwndOwner);
	(*env)->SetIntField(env, lpObject, CHOOSEFONTFc.hDC, (jint)lpStruct->hDC);
	(*env)->SetIntField(env, lpObject, CHOOSEFONTFc.lpLogFont, (jint)lpStruct->lpLogFont);
	(*env)->SetIntField(env, lpObject, CHOOSEFONTFc.iPointSize, (jint)lpStruct->iPointSize);
	(*env)->SetIntField(env, lpObject, CHOOSEFONTFc.Flags, (jint)lpStruct->Flags);
	(*env)->SetIntField(env, lpObject, CHOOSEFONTFc.rgbColors, (jint)lpStruct->rgbColors);
	(*env)->SetIntField(env, lpObject, CHOOSEFONTFc.lCustData, (jint)lpStruct->lCustData);
	(*env)->SetIntField(env, lpObject, CHOOSEFONTFc.lpfnHook, (jint)lpStruct->lpfnHook);
	(*env)->SetIntField(env, lpObject, CHOOSEFONTFc.lpTemplateName, (jint)lpStruct->lpTemplateName);
	(*env)->SetIntField(env, lpObject, CHOOSEFONTFc.hInstance, (jint)lpStruct->hInstance);
	(*env)->SetIntField(env, lpObject, CHOOSEFONTFc.lpszStyle, (jint)lpStruct->lpszStyle);
	(*env)->SetShortField(env, lpObject, CHOOSEFONTFc.nFontType, (jshort)lpStruct->nFontType);
	(*env)->SetIntField(env, lpObject, CHOOSEFONTFc.nSizeMin, (jint)lpStruct->nSizeMin);
	(*env)->SetIntField(env, lpObject, CHOOSEFONTFc.nSizeMax, (jint)lpStruct->nSizeMax);
}
#endif

#ifndef NO_COMBOBOXINFO
typedef struct COMBOBOXINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, itemLeft, itemTop, itemRight, itemBottom, buttonLeft, buttonTop, buttonRight, buttonBottom, stateButton, hwndCombo, hwndItem, hwndList;
} COMBOBOXINFO_FID_CACHE;

COMBOBOXINFO_FID_CACHE COMBOBOXINFOFc;

void cacheCOMBOBOXINFOFields(JNIEnv *env, jobject lpObject)
{
	if (COMBOBOXINFOFc.cached) return;
	COMBOBOXINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	COMBOBOXINFOFc.cbSize = (*env)->GetFieldID(env, COMBOBOXINFOFc.clazz, "cbSize", "I");
	COMBOBOXINFOFc.itemLeft = (*env)->GetFieldID(env, COMBOBOXINFOFc.clazz, "itemLeft", "I");
	COMBOBOXINFOFc.itemTop = (*env)->GetFieldID(env, COMBOBOXINFOFc.clazz, "itemTop", "I");
	COMBOBOXINFOFc.itemRight = (*env)->GetFieldID(env, COMBOBOXINFOFc.clazz, "itemRight", "I");
	COMBOBOXINFOFc.itemBottom = (*env)->GetFieldID(env, COMBOBOXINFOFc.clazz, "itemBottom", "I");
	COMBOBOXINFOFc.buttonLeft = (*env)->GetFieldID(env, COMBOBOXINFOFc.clazz, "buttonLeft", "I");
	COMBOBOXINFOFc.buttonTop = (*env)->GetFieldID(env, COMBOBOXINFOFc.clazz, "buttonTop", "I");
	COMBOBOXINFOFc.buttonRight = (*env)->GetFieldID(env, COMBOBOXINFOFc.clazz, "buttonRight", "I");
	COMBOBOXINFOFc.buttonBottom = (*env)->GetFieldID(env, COMBOBOXINFOFc.clazz, "buttonBottom", "I");
	COMBOBOXINFOFc.stateButton = (*env)->GetFieldID(env, COMBOBOXINFOFc.clazz, "stateButton", "I");
	COMBOBOXINFOFc.hwndCombo = (*env)->GetFieldID(env, COMBOBOXINFOFc.clazz, "hwndCombo", "I");
	COMBOBOXINFOFc.hwndItem = (*env)->GetFieldID(env, COMBOBOXINFOFc.clazz, "hwndItem", "I");
	COMBOBOXINFOFc.hwndList = (*env)->GetFieldID(env, COMBOBOXINFOFc.clazz, "hwndList", "I");
	COMBOBOXINFOFc.cached = 1;
}

COMBOBOXINFO *getCOMBOBOXINFOFields(JNIEnv *env, jobject lpObject, COMBOBOXINFO *lpStruct)
{
	if (!COMBOBOXINFOFc.cached) cacheCOMBOBOXINFOFields(env, lpObject);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, COMBOBOXINFOFc.cbSize);
	lpStruct->rcItem.left = (*env)->GetIntField(env, lpObject, COMBOBOXINFOFc.itemLeft);
	lpStruct->rcItem.top = (*env)->GetIntField(env, lpObject, COMBOBOXINFOFc.itemTop);
	lpStruct->rcItem.right = (*env)->GetIntField(env, lpObject, COMBOBOXINFOFc.itemRight);
	lpStruct->rcItem.bottom = (*env)->GetIntField(env, lpObject, COMBOBOXINFOFc.itemBottom);
	lpStruct->rcButton.left = (*env)->GetIntField(env, lpObject, COMBOBOXINFOFc.buttonLeft);
	lpStruct->rcButton.top = (*env)->GetIntField(env, lpObject, COMBOBOXINFOFc.buttonTop);
	lpStruct->rcButton.right = (*env)->GetIntField(env, lpObject, COMBOBOXINFOFc.buttonRight);
	lpStruct->rcButton.bottom = (*env)->GetIntField(env, lpObject, COMBOBOXINFOFc.buttonBottom);
	lpStruct->stateButton = (*env)->GetIntField(env, lpObject, COMBOBOXINFOFc.stateButton);
	lpStruct->hwndCombo = (HWND)(*env)->GetIntField(env, lpObject, COMBOBOXINFOFc.hwndCombo);
	lpStruct->hwndItem = (HWND)(*env)->GetIntField(env, lpObject, COMBOBOXINFOFc.hwndItem);
	lpStruct->hwndList = (HWND)(*env)->GetIntField(env, lpObject, COMBOBOXINFOFc.hwndList);
	return lpStruct;
}

void setCOMBOBOXINFOFields(JNIEnv *env, jobject lpObject, COMBOBOXINFO *lpStruct)
{
	if (!COMBOBOXINFOFc.cached) cacheCOMBOBOXINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, COMBOBOXINFOFc.cbSize, (jint)lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, COMBOBOXINFOFc.itemLeft, (jint)lpStruct->rcItem.left);
	(*env)->SetIntField(env, lpObject, COMBOBOXINFOFc.itemTop, (jint)lpStruct->rcItem.top);
	(*env)->SetIntField(env, lpObject, COMBOBOXINFOFc.itemRight, (jint)lpStruct->rcItem.right);
	(*env)->SetIntField(env, lpObject, COMBOBOXINFOFc.itemBottom, (jint)lpStruct->rcItem.bottom);
	(*env)->SetIntField(env, lpObject, COMBOBOXINFOFc.buttonLeft, (jint)lpStruct->rcButton.left);
	(*env)->SetIntField(env, lpObject, COMBOBOXINFOFc.buttonTop, (jint)lpStruct->rcButton.top);
	(*env)->SetIntField(env, lpObject, COMBOBOXINFOFc.buttonRight, (jint)lpStruct->rcButton.right);
	(*env)->SetIntField(env, lpObject, COMBOBOXINFOFc.buttonBottom, (jint)lpStruct->rcButton.bottom);
	(*env)->SetIntField(env, lpObject, COMBOBOXINFOFc.stateButton, (jint)lpStruct->stateButton);
	(*env)->SetIntField(env, lpObject, COMBOBOXINFOFc.hwndCombo, (jint)lpStruct->hwndCombo);
	(*env)->SetIntField(env, lpObject, COMBOBOXINFOFc.hwndItem, (jint)lpStruct->hwndItem);
	(*env)->SetIntField(env, lpObject, COMBOBOXINFOFc.hwndList, (jint)lpStruct->hwndList);
}
#endif

#ifndef NO_COMPOSITIONFORM
typedef struct COMPOSITIONFORM_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID dwStyle, x, y, left, top, right, bottom;
} COMPOSITIONFORM_FID_CACHE;

COMPOSITIONFORM_FID_CACHE COMPOSITIONFORMFc;

void cacheCOMPOSITIONFORMFields(JNIEnv *env, jobject lpObject)
{
	if (COMPOSITIONFORMFc.cached) return;
	COMPOSITIONFORMFc.clazz = (*env)->GetObjectClass(env, lpObject);
	COMPOSITIONFORMFc.dwStyle = (*env)->GetFieldID(env, COMPOSITIONFORMFc.clazz, "dwStyle", "I");
	COMPOSITIONFORMFc.x = (*env)->GetFieldID(env, COMPOSITIONFORMFc.clazz, "x", "I");
	COMPOSITIONFORMFc.y = (*env)->GetFieldID(env, COMPOSITIONFORMFc.clazz, "y", "I");
	COMPOSITIONFORMFc.left = (*env)->GetFieldID(env, COMPOSITIONFORMFc.clazz, "left", "I");
	COMPOSITIONFORMFc.top = (*env)->GetFieldID(env, COMPOSITIONFORMFc.clazz, "top", "I");
	COMPOSITIONFORMFc.right = (*env)->GetFieldID(env, COMPOSITIONFORMFc.clazz, "right", "I");
	COMPOSITIONFORMFc.bottom = (*env)->GetFieldID(env, COMPOSITIONFORMFc.clazz, "bottom", "I");
	COMPOSITIONFORMFc.cached = 1;
}

COMPOSITIONFORM *getCOMPOSITIONFORMFields(JNIEnv *env, jobject lpObject, COMPOSITIONFORM *lpStruct)
{
	if (!COMPOSITIONFORMFc.cached) cacheCOMPOSITIONFORMFields(env, lpObject);
	lpStruct->dwStyle = (*env)->GetIntField(env, lpObject, COMPOSITIONFORMFc.dwStyle);
	lpStruct->ptCurrentPos.x = (*env)->GetIntField(env, lpObject, COMPOSITIONFORMFc.x);
	lpStruct->ptCurrentPos.y = (*env)->GetIntField(env, lpObject, COMPOSITIONFORMFc.y);
	lpStruct->rcArea.left = (*env)->GetIntField(env, lpObject, COMPOSITIONFORMFc.left);
	lpStruct->rcArea.top = (*env)->GetIntField(env, lpObject, COMPOSITIONFORMFc.top);
	lpStruct->rcArea.right = (*env)->GetIntField(env, lpObject, COMPOSITIONFORMFc.right);
	lpStruct->rcArea.bottom = (*env)->GetIntField(env, lpObject, COMPOSITIONFORMFc.bottom);
	return lpStruct;
}

void setCOMPOSITIONFORMFields(JNIEnv *env, jobject lpObject, COMPOSITIONFORM *lpStruct)
{
	if (!COMPOSITIONFORMFc.cached) cacheCOMPOSITIONFORMFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, COMPOSITIONFORMFc.dwStyle, (jint)lpStruct->dwStyle);
	(*env)->SetIntField(env, lpObject, COMPOSITIONFORMFc.x, (jint)lpStruct->ptCurrentPos.x);
	(*env)->SetIntField(env, lpObject, COMPOSITIONFORMFc.y, (jint)lpStruct->ptCurrentPos.y);
	(*env)->SetIntField(env, lpObject, COMPOSITIONFORMFc.left, (jint)lpStruct->rcArea.left);
	(*env)->SetIntField(env, lpObject, COMPOSITIONFORMFc.top, (jint)lpStruct->rcArea.top);
	(*env)->SetIntField(env, lpObject, COMPOSITIONFORMFc.right, (jint)lpStruct->rcArea.right);
	(*env)->SetIntField(env, lpObject, COMPOSITIONFORMFc.bottom, (jint)lpStruct->rcArea.bottom);
}
#endif

#ifndef NO_CREATESTRUCT
typedef struct CREATESTRUCT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lpCreateParams, hInstance, hMenu, hwndParent, cy, cx, y, x, style, lpszName, lpszClass, dwExStyle;
} CREATESTRUCT_FID_CACHE;

CREATESTRUCT_FID_CACHE CREATESTRUCTFc;

void cacheCREATESTRUCTFields(JNIEnv *env, jobject lpObject)
{
	if (CREATESTRUCTFc.cached) return;
	CREATESTRUCTFc.clazz = (*env)->GetObjectClass(env, lpObject);
	CREATESTRUCTFc.lpCreateParams = (*env)->GetFieldID(env, CREATESTRUCTFc.clazz, "lpCreateParams", "I");
	CREATESTRUCTFc.hInstance = (*env)->GetFieldID(env, CREATESTRUCTFc.clazz, "hInstance", "I");
	CREATESTRUCTFc.hMenu = (*env)->GetFieldID(env, CREATESTRUCTFc.clazz, "hMenu", "I");
	CREATESTRUCTFc.hwndParent = (*env)->GetFieldID(env, CREATESTRUCTFc.clazz, "hwndParent", "I");
	CREATESTRUCTFc.cy = (*env)->GetFieldID(env, CREATESTRUCTFc.clazz, "cy", "I");
	CREATESTRUCTFc.cx = (*env)->GetFieldID(env, CREATESTRUCTFc.clazz, "cx", "I");
	CREATESTRUCTFc.y = (*env)->GetFieldID(env, CREATESTRUCTFc.clazz, "y", "I");
	CREATESTRUCTFc.x = (*env)->GetFieldID(env, CREATESTRUCTFc.clazz, "x", "I");
	CREATESTRUCTFc.style = (*env)->GetFieldID(env, CREATESTRUCTFc.clazz, "style", "I");
	CREATESTRUCTFc.lpszName = (*env)->GetFieldID(env, CREATESTRUCTFc.clazz, "lpszName", "I");
	CREATESTRUCTFc.lpszClass = (*env)->GetFieldID(env, CREATESTRUCTFc.clazz, "lpszClass", "I");
	CREATESTRUCTFc.dwExStyle = (*env)->GetFieldID(env, CREATESTRUCTFc.clazz, "dwExStyle", "I");
	CREATESTRUCTFc.cached = 1;
}

CREATESTRUCT *getCREATESTRUCTFields(JNIEnv *env, jobject lpObject, CREATESTRUCT *lpStruct)
{
	if (!CREATESTRUCTFc.cached) cacheCREATESTRUCTFields(env, lpObject);
	lpStruct->lpCreateParams = (LPVOID)(*env)->GetIntField(env, lpObject, CREATESTRUCTFc.lpCreateParams);
	lpStruct->hInstance = (HINSTANCE)(*env)->GetIntField(env, lpObject, CREATESTRUCTFc.hInstance);
	lpStruct->hMenu = (HMENU)(*env)->GetIntField(env, lpObject, CREATESTRUCTFc.hMenu);
	lpStruct->hwndParent = (HWND)(*env)->GetIntField(env, lpObject, CREATESTRUCTFc.hwndParent);
	lpStruct->cy = (*env)->GetIntField(env, lpObject, CREATESTRUCTFc.cy);
	lpStruct->cx = (*env)->GetIntField(env, lpObject, CREATESTRUCTFc.cx);
	lpStruct->y = (*env)->GetIntField(env, lpObject, CREATESTRUCTFc.y);
	lpStruct->x = (*env)->GetIntField(env, lpObject, CREATESTRUCTFc.x);
	lpStruct->style = (*env)->GetIntField(env, lpObject, CREATESTRUCTFc.style);
	lpStruct->lpszName = (LPCTSTR)(*env)->GetIntField(env, lpObject, CREATESTRUCTFc.lpszName);
	lpStruct->lpszClass = (LPCTSTR)(*env)->GetIntField(env, lpObject, CREATESTRUCTFc.lpszClass);
	lpStruct->dwExStyle = (*env)->GetIntField(env, lpObject, CREATESTRUCTFc.dwExStyle);
	return lpStruct;
}

void setCREATESTRUCTFields(JNIEnv *env, jobject lpObject, CREATESTRUCT *lpStruct)
{
	if (!CREATESTRUCTFc.cached) cacheCREATESTRUCTFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, CREATESTRUCTFc.lpCreateParams, (jint)lpStruct->lpCreateParams);
	(*env)->SetIntField(env, lpObject, CREATESTRUCTFc.hInstance, (jint)lpStruct->hInstance);
	(*env)->SetIntField(env, lpObject, CREATESTRUCTFc.hMenu, (jint)lpStruct->hMenu);
	(*env)->SetIntField(env, lpObject, CREATESTRUCTFc.hwndParent, (jint)lpStruct->hwndParent);
	(*env)->SetIntField(env, lpObject, CREATESTRUCTFc.cy, (jint)lpStruct->cy);
	(*env)->SetIntField(env, lpObject, CREATESTRUCTFc.cx, (jint)lpStruct->cx);
	(*env)->SetIntField(env, lpObject, CREATESTRUCTFc.y, (jint)lpStruct->y);
	(*env)->SetIntField(env, lpObject, CREATESTRUCTFc.x, (jint)lpStruct->x);
	(*env)->SetIntField(env, lpObject, CREATESTRUCTFc.style, (jint)lpStruct->style);
	(*env)->SetIntField(env, lpObject, CREATESTRUCTFc.lpszName, (jint)lpStruct->lpszName);
	(*env)->SetIntField(env, lpObject, CREATESTRUCTFc.lpszClass, (jint)lpStruct->lpszClass);
	(*env)->SetIntField(env, lpObject, CREATESTRUCTFc.dwExStyle, (jint)lpStruct->dwExStyle);
}
#endif

#ifndef NO_DIBSECTION
typedef struct DIBSECTION_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID biSize, biWidth, biHeight, biPlanes, biBitCount, biCompression, biSizeImage, biXPelsPerMeter, biYPelsPerMeter, biClrUsed, biClrImportant, dsBitfields0, dsBitfields1, dsBitfields2, dshSection, dsOffset;
} DIBSECTION_FID_CACHE;

DIBSECTION_FID_CACHE DIBSECTIONFc;

void cacheDIBSECTIONFields(JNIEnv *env, jobject lpObject)
{
	if (DIBSECTIONFc.cached) return;
	cacheBITMAPFields(env, lpObject);
	DIBSECTIONFc.clazz = (*env)->GetObjectClass(env, lpObject);
	DIBSECTIONFc.biSize = (*env)->GetFieldID(env, DIBSECTIONFc.clazz, "biSize", "I");
	DIBSECTIONFc.biWidth = (*env)->GetFieldID(env, DIBSECTIONFc.clazz, "biWidth", "I");
	DIBSECTIONFc.biHeight = (*env)->GetFieldID(env, DIBSECTIONFc.clazz, "biHeight", "I");
	DIBSECTIONFc.biPlanes = (*env)->GetFieldID(env, DIBSECTIONFc.clazz, "biPlanes", "S");
	DIBSECTIONFc.biBitCount = (*env)->GetFieldID(env, DIBSECTIONFc.clazz, "biBitCount", "S");
	DIBSECTIONFc.biCompression = (*env)->GetFieldID(env, DIBSECTIONFc.clazz, "biCompression", "I");
	DIBSECTIONFc.biSizeImage = (*env)->GetFieldID(env, DIBSECTIONFc.clazz, "biSizeImage", "I");
	DIBSECTIONFc.biXPelsPerMeter = (*env)->GetFieldID(env, DIBSECTIONFc.clazz, "biXPelsPerMeter", "I");
	DIBSECTIONFc.biYPelsPerMeter = (*env)->GetFieldID(env, DIBSECTIONFc.clazz, "biYPelsPerMeter", "I");
	DIBSECTIONFc.biClrUsed = (*env)->GetFieldID(env, DIBSECTIONFc.clazz, "biClrUsed", "I");
	DIBSECTIONFc.biClrImportant = (*env)->GetFieldID(env, DIBSECTIONFc.clazz, "biClrImportant", "I");
	DIBSECTIONFc.dsBitfields0 = (*env)->GetFieldID(env, DIBSECTIONFc.clazz, "dsBitfields0", "I");
	DIBSECTIONFc.dsBitfields1 = (*env)->GetFieldID(env, DIBSECTIONFc.clazz, "dsBitfields1", "I");
	DIBSECTIONFc.dsBitfields2 = (*env)->GetFieldID(env, DIBSECTIONFc.clazz, "dsBitfields2", "I");
	DIBSECTIONFc.dshSection = (*env)->GetFieldID(env, DIBSECTIONFc.clazz, "dshSection", "I");
	DIBSECTIONFc.dsOffset = (*env)->GetFieldID(env, DIBSECTIONFc.clazz, "dsOffset", "I");
	DIBSECTIONFc.cached = 1;
}

DIBSECTION *getDIBSECTIONFields(JNIEnv *env, jobject lpObject, DIBSECTION *lpStruct)
{
	if (!DIBSECTIONFc.cached) cacheDIBSECTIONFields(env, lpObject);
	getBITMAPFields(env, lpObject, (BITMAP *)lpStruct);
	lpStruct->dsBmih.biSize = (*env)->GetIntField(env, lpObject, DIBSECTIONFc.biSize);
	lpStruct->dsBmih.biWidth = (*env)->GetIntField(env, lpObject, DIBSECTIONFc.biWidth);
	lpStruct->dsBmih.biHeight = (*env)->GetIntField(env, lpObject, DIBSECTIONFc.biHeight);
	lpStruct->dsBmih.biPlanes = (*env)->GetShortField(env, lpObject, DIBSECTIONFc.biPlanes);
	lpStruct->dsBmih.biBitCount = (*env)->GetShortField(env, lpObject, DIBSECTIONFc.biBitCount);
	lpStruct->dsBmih.biCompression = (*env)->GetIntField(env, lpObject, DIBSECTIONFc.biCompression);
	lpStruct->dsBmih.biSizeImage = (*env)->GetIntField(env, lpObject, DIBSECTIONFc.biSizeImage);
	lpStruct->dsBmih.biXPelsPerMeter = (*env)->GetIntField(env, lpObject, DIBSECTIONFc.biXPelsPerMeter);
	lpStruct->dsBmih.biYPelsPerMeter = (*env)->GetIntField(env, lpObject, DIBSECTIONFc.biYPelsPerMeter);
	lpStruct->dsBmih.biClrUsed = (*env)->GetIntField(env, lpObject, DIBSECTIONFc.biClrUsed);
	lpStruct->dsBmih.biClrImportant = (*env)->GetIntField(env, lpObject, DIBSECTIONFc.biClrImportant);
	lpStruct->dsBitfields[0] = (*env)->GetIntField(env, lpObject, DIBSECTIONFc.dsBitfields0);
	lpStruct->dsBitfields[1] = (*env)->GetIntField(env, lpObject, DIBSECTIONFc.dsBitfields1);
	lpStruct->dsBitfields[2] = (*env)->GetIntField(env, lpObject, DIBSECTIONFc.dsBitfields2);
	lpStruct->dshSection = (HANDLE)(*env)->GetIntField(env, lpObject, DIBSECTIONFc.dshSection);
	lpStruct->dsOffset = (*env)->GetIntField(env, lpObject, DIBSECTIONFc.dsOffset);
	return lpStruct;
}

void setDIBSECTIONFields(JNIEnv *env, jobject lpObject, DIBSECTION *lpStruct)
{
	if (!DIBSECTIONFc.cached) cacheDIBSECTIONFields(env, lpObject);
	setBITMAPFields(env, lpObject, (BITMAP *)lpStruct);
	(*env)->SetIntField(env, lpObject, DIBSECTIONFc.biSize, (jint)lpStruct->dsBmih.biSize);
	(*env)->SetIntField(env, lpObject, DIBSECTIONFc.biWidth, (jint)lpStruct->dsBmih.biWidth);
	(*env)->SetIntField(env, lpObject, DIBSECTIONFc.biHeight, (jint)lpStruct->dsBmih.biHeight);
	(*env)->SetShortField(env, lpObject, DIBSECTIONFc.biPlanes, (jshort)lpStruct->dsBmih.biPlanes);
	(*env)->SetShortField(env, lpObject, DIBSECTIONFc.biBitCount, (jshort)lpStruct->dsBmih.biBitCount);
	(*env)->SetIntField(env, lpObject, DIBSECTIONFc.biCompression, (jint)lpStruct->dsBmih.biCompression);
	(*env)->SetIntField(env, lpObject, DIBSECTIONFc.biSizeImage, (jint)lpStruct->dsBmih.biSizeImage);
	(*env)->SetIntField(env, lpObject, DIBSECTIONFc.biXPelsPerMeter, (jint)lpStruct->dsBmih.biXPelsPerMeter);
	(*env)->SetIntField(env, lpObject, DIBSECTIONFc.biYPelsPerMeter, (jint)lpStruct->dsBmih.biYPelsPerMeter);
	(*env)->SetIntField(env, lpObject, DIBSECTIONFc.biClrUsed, (jint)lpStruct->dsBmih.biClrUsed);
	(*env)->SetIntField(env, lpObject, DIBSECTIONFc.biClrImportant, (jint)lpStruct->dsBmih.biClrImportant);
	(*env)->SetIntField(env, lpObject, DIBSECTIONFc.dsBitfields0, (jint)lpStruct->dsBitfields[0]);
	(*env)->SetIntField(env, lpObject, DIBSECTIONFc.dsBitfields1, (jint)lpStruct->dsBitfields[1]);
	(*env)->SetIntField(env, lpObject, DIBSECTIONFc.dsBitfields2, (jint)lpStruct->dsBitfields[2]);
	(*env)->SetIntField(env, lpObject, DIBSECTIONFc.dshSection, (jint)lpStruct->dshSection);
	(*env)->SetIntField(env, lpObject, DIBSECTIONFc.dsOffset, (jint)lpStruct->dsOffset);
}
#endif

#ifndef NO_DLLVERSIONINFO
typedef struct DLLVERSIONINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, dwMajorVersion, dwMinorVersion, dwBuildNumber, dwPlatformID;
} DLLVERSIONINFO_FID_CACHE;

DLLVERSIONINFO_FID_CACHE DLLVERSIONINFOFc;

void cacheDLLVERSIONINFOFields(JNIEnv *env, jobject lpObject)
{
	if (DLLVERSIONINFOFc.cached) return;
	DLLVERSIONINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	DLLVERSIONINFOFc.cbSize = (*env)->GetFieldID(env, DLLVERSIONINFOFc.clazz, "cbSize", "I");
	DLLVERSIONINFOFc.dwMajorVersion = (*env)->GetFieldID(env, DLLVERSIONINFOFc.clazz, "dwMajorVersion", "I");
	DLLVERSIONINFOFc.dwMinorVersion = (*env)->GetFieldID(env, DLLVERSIONINFOFc.clazz, "dwMinorVersion", "I");
	DLLVERSIONINFOFc.dwBuildNumber = (*env)->GetFieldID(env, DLLVERSIONINFOFc.clazz, "dwBuildNumber", "I");
	DLLVERSIONINFOFc.dwPlatformID = (*env)->GetFieldID(env, DLLVERSIONINFOFc.clazz, "dwPlatformID", "I");
	DLLVERSIONINFOFc.cached = 1;
}

DLLVERSIONINFO *getDLLVERSIONINFOFields(JNIEnv *env, jobject lpObject, DLLVERSIONINFO *lpStruct)
{
	if (!DLLVERSIONINFOFc.cached) cacheDLLVERSIONINFOFields(env, lpObject);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, DLLVERSIONINFOFc.cbSize);
	lpStruct->dwMajorVersion = (*env)->GetIntField(env, lpObject, DLLVERSIONINFOFc.dwMajorVersion);
	lpStruct->dwMinorVersion = (*env)->GetIntField(env, lpObject, DLLVERSIONINFOFc.dwMinorVersion);
	lpStruct->dwBuildNumber = (*env)->GetIntField(env, lpObject, DLLVERSIONINFOFc.dwBuildNumber);
	lpStruct->dwPlatformID = (*env)->GetIntField(env, lpObject, DLLVERSIONINFOFc.dwPlatformID);
	return lpStruct;
}

void setDLLVERSIONINFOFields(JNIEnv *env, jobject lpObject, DLLVERSIONINFO *lpStruct)
{
	if (!DLLVERSIONINFOFc.cached) cacheDLLVERSIONINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, DLLVERSIONINFOFc.cbSize, (jint)lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, DLLVERSIONINFOFc.dwMajorVersion, (jint)lpStruct->dwMajorVersion);
	(*env)->SetIntField(env, lpObject, DLLVERSIONINFOFc.dwMinorVersion, (jint)lpStruct->dwMinorVersion);
	(*env)->SetIntField(env, lpObject, DLLVERSIONINFOFc.dwBuildNumber, (jint)lpStruct->dwBuildNumber);
	(*env)->SetIntField(env, lpObject, DLLVERSIONINFOFc.dwPlatformID, (jint)lpStruct->dwPlatformID);
}
#endif

#ifndef NO_DOCINFO
typedef struct DOCINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, lpszDocName, lpszOutput, lpszDatatype, fwType;
} DOCINFO_FID_CACHE;

DOCINFO_FID_CACHE DOCINFOFc;

void cacheDOCINFOFields(JNIEnv *env, jobject lpObject)
{
	if (DOCINFOFc.cached) return;
	DOCINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	DOCINFOFc.cbSize = (*env)->GetFieldID(env, DOCINFOFc.clazz, "cbSize", "I");
	DOCINFOFc.lpszDocName = (*env)->GetFieldID(env, DOCINFOFc.clazz, "lpszDocName", "I");
	DOCINFOFc.lpszOutput = (*env)->GetFieldID(env, DOCINFOFc.clazz, "lpszOutput", "I");
	DOCINFOFc.lpszDatatype = (*env)->GetFieldID(env, DOCINFOFc.clazz, "lpszDatatype", "I");
	DOCINFOFc.fwType = (*env)->GetFieldID(env, DOCINFOFc.clazz, "fwType", "I");
	DOCINFOFc.cached = 1;
}

DOCINFO *getDOCINFOFields(JNIEnv *env, jobject lpObject, DOCINFO *lpStruct)
{
	if (!DOCINFOFc.cached) cacheDOCINFOFields(env, lpObject);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, DOCINFOFc.cbSize);
	lpStruct->lpszDocName = (LPCTSTR)(*env)->GetIntField(env, lpObject, DOCINFOFc.lpszDocName);
	lpStruct->lpszOutput = (LPCTSTR)(*env)->GetIntField(env, lpObject, DOCINFOFc.lpszOutput);
	lpStruct->lpszDatatype = (LPCTSTR)(*env)->GetIntField(env, lpObject, DOCINFOFc.lpszDatatype);
	lpStruct->fwType = (*env)->GetIntField(env, lpObject, DOCINFOFc.fwType);
	return lpStruct;
}

void setDOCINFOFields(JNIEnv *env, jobject lpObject, DOCINFO *lpStruct)
{
	if (!DOCINFOFc.cached) cacheDOCINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, DOCINFOFc.cbSize, (jint)lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, DOCINFOFc.lpszDocName, (jint)lpStruct->lpszDocName);
	(*env)->SetIntField(env, lpObject, DOCINFOFc.lpszOutput, (jint)lpStruct->lpszOutput);
	(*env)->SetIntField(env, lpObject, DOCINFOFc.lpszDatatype, (jint)lpStruct->lpszDatatype);
	(*env)->SetIntField(env, lpObject, DOCINFOFc.fwType, (jint)lpStruct->fwType);
}
#endif

#ifndef NO_DRAWITEMSTRUCT
typedef struct DRAWITEMSTRUCT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID CtlType, CtlID, itemID, itemAction, itemState, hwndItem, hDC, left, top, bottom, right, itemData;
} DRAWITEMSTRUCT_FID_CACHE;

DRAWITEMSTRUCT_FID_CACHE DRAWITEMSTRUCTFc;

void cacheDRAWITEMSTRUCTFields(JNIEnv *env, jobject lpObject)
{
	if (DRAWITEMSTRUCTFc.cached) return;
	DRAWITEMSTRUCTFc.clazz = (*env)->GetObjectClass(env, lpObject);
	DRAWITEMSTRUCTFc.CtlType = (*env)->GetFieldID(env, DRAWITEMSTRUCTFc.clazz, "CtlType", "I");
	DRAWITEMSTRUCTFc.CtlID = (*env)->GetFieldID(env, DRAWITEMSTRUCTFc.clazz, "CtlID", "I");
	DRAWITEMSTRUCTFc.itemID = (*env)->GetFieldID(env, DRAWITEMSTRUCTFc.clazz, "itemID", "I");
	DRAWITEMSTRUCTFc.itemAction = (*env)->GetFieldID(env, DRAWITEMSTRUCTFc.clazz, "itemAction", "I");
	DRAWITEMSTRUCTFc.itemState = (*env)->GetFieldID(env, DRAWITEMSTRUCTFc.clazz, "itemState", "I");
	DRAWITEMSTRUCTFc.hwndItem = (*env)->GetFieldID(env, DRAWITEMSTRUCTFc.clazz, "hwndItem", "I");
	DRAWITEMSTRUCTFc.hDC = (*env)->GetFieldID(env, DRAWITEMSTRUCTFc.clazz, "hDC", "I");
	DRAWITEMSTRUCTFc.left = (*env)->GetFieldID(env, DRAWITEMSTRUCTFc.clazz, "left", "I");
	DRAWITEMSTRUCTFc.top = (*env)->GetFieldID(env, DRAWITEMSTRUCTFc.clazz, "top", "I");
	DRAWITEMSTRUCTFc.bottom = (*env)->GetFieldID(env, DRAWITEMSTRUCTFc.clazz, "bottom", "I");
	DRAWITEMSTRUCTFc.right = (*env)->GetFieldID(env, DRAWITEMSTRUCTFc.clazz, "right", "I");
	DRAWITEMSTRUCTFc.itemData = (*env)->GetFieldID(env, DRAWITEMSTRUCTFc.clazz, "itemData", "I");
	DRAWITEMSTRUCTFc.cached = 1;
}

DRAWITEMSTRUCT *getDRAWITEMSTRUCTFields(JNIEnv *env, jobject lpObject, DRAWITEMSTRUCT *lpStruct)
{
	if (!DRAWITEMSTRUCTFc.cached) cacheDRAWITEMSTRUCTFields(env, lpObject);
	lpStruct->CtlType = (*env)->GetIntField(env, lpObject, DRAWITEMSTRUCTFc.CtlType);
	lpStruct->CtlID = (*env)->GetIntField(env, lpObject, DRAWITEMSTRUCTFc.CtlID);
	lpStruct->itemID = (*env)->GetIntField(env, lpObject, DRAWITEMSTRUCTFc.itemID);
	lpStruct->itemAction = (*env)->GetIntField(env, lpObject, DRAWITEMSTRUCTFc.itemAction);
	lpStruct->itemState = (*env)->GetIntField(env, lpObject, DRAWITEMSTRUCTFc.itemState);
	lpStruct->hwndItem = (HWND)(*env)->GetIntField(env, lpObject, DRAWITEMSTRUCTFc.hwndItem);
	lpStruct->hDC = (HDC)(*env)->GetIntField(env, lpObject, DRAWITEMSTRUCTFc.hDC);
	lpStruct->rcItem.left = (*env)->GetIntField(env, lpObject, DRAWITEMSTRUCTFc.left);
	lpStruct->rcItem.top = (*env)->GetIntField(env, lpObject, DRAWITEMSTRUCTFc.top);
	lpStruct->rcItem.bottom = (*env)->GetIntField(env, lpObject, DRAWITEMSTRUCTFc.bottom);
	lpStruct->rcItem.right = (*env)->GetIntField(env, lpObject, DRAWITEMSTRUCTFc.right);
	lpStruct->itemData = (*env)->GetIntField(env, lpObject, DRAWITEMSTRUCTFc.itemData);
	return lpStruct;
}

void setDRAWITEMSTRUCTFields(JNIEnv *env, jobject lpObject, DRAWITEMSTRUCT *lpStruct)
{
	if (!DRAWITEMSTRUCTFc.cached) cacheDRAWITEMSTRUCTFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, DRAWITEMSTRUCTFc.CtlType, (jint)lpStruct->CtlType);
	(*env)->SetIntField(env, lpObject, DRAWITEMSTRUCTFc.CtlID, (jint)lpStruct->CtlID);
	(*env)->SetIntField(env, lpObject, DRAWITEMSTRUCTFc.itemID, (jint)lpStruct->itemID);
	(*env)->SetIntField(env, lpObject, DRAWITEMSTRUCTFc.itemAction, (jint)lpStruct->itemAction);
	(*env)->SetIntField(env, lpObject, DRAWITEMSTRUCTFc.itemState, (jint)lpStruct->itemState);
	(*env)->SetIntField(env, lpObject, DRAWITEMSTRUCTFc.hwndItem, (jint)lpStruct->hwndItem);
	(*env)->SetIntField(env, lpObject, DRAWITEMSTRUCTFc.hDC, (jint)lpStruct->hDC);
	(*env)->SetIntField(env, lpObject, DRAWITEMSTRUCTFc.left, (jint)lpStruct->rcItem.left);
	(*env)->SetIntField(env, lpObject, DRAWITEMSTRUCTFc.top, (jint)lpStruct->rcItem.top);
	(*env)->SetIntField(env, lpObject, DRAWITEMSTRUCTFc.bottom, (jint)lpStruct->rcItem.bottom);
	(*env)->SetIntField(env, lpObject, DRAWITEMSTRUCTFc.right, (jint)lpStruct->rcItem.right);
	(*env)->SetIntField(env, lpObject, DRAWITEMSTRUCTFc.itemData, (jint)lpStruct->itemData);
}
#endif

#ifndef NO_DROPFILES
typedef struct DROPFILES_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID pFiles, pt_x, pt_y, fNC, fWide;
} DROPFILES_FID_CACHE;

DROPFILES_FID_CACHE DROPFILESFc;

void cacheDROPFILESFields(JNIEnv *env, jobject lpObject)
{
	if (DROPFILESFc.cached) return;
	DROPFILESFc.clazz = (*env)->GetObjectClass(env, lpObject);
	DROPFILESFc.pFiles = (*env)->GetFieldID(env, DROPFILESFc.clazz, "pFiles", "I");
	DROPFILESFc.pt_x = (*env)->GetFieldID(env, DROPFILESFc.clazz, "pt_x", "I");
	DROPFILESFc.pt_y = (*env)->GetFieldID(env, DROPFILESFc.clazz, "pt_y", "I");
	DROPFILESFc.fNC = (*env)->GetFieldID(env, DROPFILESFc.clazz, "fNC", "I");
	DROPFILESFc.fWide = (*env)->GetFieldID(env, DROPFILESFc.clazz, "fWide", "I");
	DROPFILESFc.cached = 1;
}

DROPFILES *getDROPFILESFields(JNIEnv *env, jobject lpObject, DROPFILES *lpStruct)
{
	if (!DROPFILESFc.cached) cacheDROPFILESFields(env, lpObject);
	lpStruct->pFiles = (*env)->GetIntField(env, lpObject, DROPFILESFc.pFiles);
	lpStruct->pt.x = (*env)->GetIntField(env, lpObject, DROPFILESFc.pt_x);
	lpStruct->pt.y = (*env)->GetIntField(env, lpObject, DROPFILESFc.pt_y);
	lpStruct->fNC = (*env)->GetIntField(env, lpObject, DROPFILESFc.fNC);
	lpStruct->fWide = (*env)->GetIntField(env, lpObject, DROPFILESFc.fWide);
	return lpStruct;
}

void setDROPFILESFields(JNIEnv *env, jobject lpObject, DROPFILES *lpStruct)
{
	if (!DROPFILESFc.cached) cacheDROPFILESFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, DROPFILESFc.pFiles, (jint)lpStruct->pFiles);
	(*env)->SetIntField(env, lpObject, DROPFILESFc.pt_x, (jint)lpStruct->pt.x);
	(*env)->SetIntField(env, lpObject, DROPFILESFc.pt_y, (jint)lpStruct->pt.y);
	(*env)->SetIntField(env, lpObject, DROPFILESFc.fNC, (jint)lpStruct->fNC);
	(*env)->SetIntField(env, lpObject, DROPFILESFc.fWide, (jint)lpStruct->fWide);
}
#endif

#ifndef NO_EXTLOGPEN
typedef struct EXTLOGPEN_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID elpPenStyle, elpWidth, elpBrushStyle, elpColor, elpHatch, elpNumEntries, elpStyleEntry;
} EXTLOGPEN_FID_CACHE;

EXTLOGPEN_FID_CACHE EXTLOGPENFc;

void cacheEXTLOGPENFields(JNIEnv *env, jobject lpObject)
{
	if (EXTLOGPENFc.cached) return;
	EXTLOGPENFc.clazz = (*env)->GetObjectClass(env, lpObject);
	EXTLOGPENFc.elpPenStyle = (*env)->GetFieldID(env, EXTLOGPENFc.clazz, "elpPenStyle", "I");
	EXTLOGPENFc.elpWidth = (*env)->GetFieldID(env, EXTLOGPENFc.clazz, "elpWidth", "I");
	EXTLOGPENFc.elpBrushStyle = (*env)->GetFieldID(env, EXTLOGPENFc.clazz, "elpBrushStyle", "I");
	EXTLOGPENFc.elpColor = (*env)->GetFieldID(env, EXTLOGPENFc.clazz, "elpColor", "I");
	EXTLOGPENFc.elpHatch = (*env)->GetFieldID(env, EXTLOGPENFc.clazz, "elpHatch", "I");
	EXTLOGPENFc.elpNumEntries = (*env)->GetFieldID(env, EXTLOGPENFc.clazz, "elpNumEntries", "I");
	EXTLOGPENFc.elpStyleEntry = (*env)->GetFieldID(env, EXTLOGPENFc.clazz, "elpStyleEntry", "[I");
	EXTLOGPENFc.cached = 1;
}

EXTLOGPEN *getEXTLOGPENFields(JNIEnv *env, jobject lpObject, EXTLOGPEN *lpStruct)
{
	if (!EXTLOGPENFc.cached) cacheEXTLOGPENFields(env, lpObject);
	lpStruct->elpPenStyle = (*env)->GetIntField(env, lpObject, EXTLOGPENFc.elpPenStyle);
	lpStruct->elpWidth = (*env)->GetIntField(env, lpObject, EXTLOGPENFc.elpWidth);
	lpStruct->elpBrushStyle = (*env)->GetIntField(env, lpObject, EXTLOGPENFc.elpBrushStyle);
	lpStruct->elpColor = (*env)->GetIntField(env, lpObject, EXTLOGPENFc.elpColor);
	lpStruct->elpHatch = (*env)->GetIntField(env, lpObject, EXTLOGPENFc.elpHatch);
	lpStruct->elpNumEntries = (*env)->GetIntField(env, lpObject, EXTLOGPENFc.elpNumEntries);
	{
	jintArray lpObject1 = (jintArray)(*env)->GetObjectField(env, lpObject, EXTLOGPENFc.elpStyleEntry);
	(*env)->GetIntArrayRegion(env, lpObject1, 0, sizeof(lpStruct->elpStyleEntry) / 4, (jint *)lpStruct->elpStyleEntry);
	}
	return lpStruct;
}

void setEXTLOGPENFields(JNIEnv *env, jobject lpObject, EXTLOGPEN *lpStruct)
{
	if (!EXTLOGPENFc.cached) cacheEXTLOGPENFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, EXTLOGPENFc.elpPenStyle, (jint)lpStruct->elpPenStyle);
	(*env)->SetIntField(env, lpObject, EXTLOGPENFc.elpWidth, (jint)lpStruct->elpWidth);
	(*env)->SetIntField(env, lpObject, EXTLOGPENFc.elpBrushStyle, (jint)lpStruct->elpBrushStyle);
	(*env)->SetIntField(env, lpObject, EXTLOGPENFc.elpColor, (jint)lpStruct->elpColor);
	(*env)->SetIntField(env, lpObject, EXTLOGPENFc.elpHatch, (jint)lpStruct->elpHatch);
	(*env)->SetIntField(env, lpObject, EXTLOGPENFc.elpNumEntries, (jint)lpStruct->elpNumEntries);
	{
	jintArray lpObject1 = (jintArray)(*env)->GetObjectField(env, lpObject, EXTLOGPENFc.elpStyleEntry);
	(*env)->SetIntArrayRegion(env, lpObject1, 0, sizeof(lpStruct->elpStyleEntry) / 4, (jint *)lpStruct->elpStyleEntry);
	}
}
#endif

#ifndef NO_FILETIME
typedef struct FILETIME_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID dwLowDateTime, dwHighDateTime;
} FILETIME_FID_CACHE;

FILETIME_FID_CACHE FILETIMEFc;

void cacheFILETIMEFields(JNIEnv *env, jobject lpObject)
{
	if (FILETIMEFc.cached) return;
	FILETIMEFc.clazz = (*env)->GetObjectClass(env, lpObject);
	FILETIMEFc.dwLowDateTime = (*env)->GetFieldID(env, FILETIMEFc.clazz, "dwLowDateTime", "I");
	FILETIMEFc.dwHighDateTime = (*env)->GetFieldID(env, FILETIMEFc.clazz, "dwHighDateTime", "I");
	FILETIMEFc.cached = 1;
}

FILETIME *getFILETIMEFields(JNIEnv *env, jobject lpObject, FILETIME *lpStruct)
{
	if (!FILETIMEFc.cached) cacheFILETIMEFields(env, lpObject);
	lpStruct->dwLowDateTime = (*env)->GetIntField(env, lpObject, FILETIMEFc.dwLowDateTime);
	lpStruct->dwHighDateTime = (*env)->GetIntField(env, lpObject, FILETIMEFc.dwHighDateTime);
	return lpStruct;
}

void setFILETIMEFields(JNIEnv *env, jobject lpObject, FILETIME *lpStruct)
{
	if (!FILETIMEFc.cached) cacheFILETIMEFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, FILETIMEFc.dwLowDateTime, (jint)lpStruct->dwLowDateTime);
	(*env)->SetIntField(env, lpObject, FILETIMEFc.dwHighDateTime, (jint)lpStruct->dwHighDateTime);
}
#endif

#ifndef NO_GCP_RESULTS
typedef struct GCP_RESULTS_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lStructSize, lpOutString, lpOrder, lpDx, lpCaretPos, lpClass, lpGlyphs, nGlyphs, nMaxFit;
} GCP_RESULTS_FID_CACHE;

GCP_RESULTS_FID_CACHE GCP_RESULTSFc;

void cacheGCP_RESULTSFields(JNIEnv *env, jobject lpObject)
{
	if (GCP_RESULTSFc.cached) return;
	GCP_RESULTSFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GCP_RESULTSFc.lStructSize = (*env)->GetFieldID(env, GCP_RESULTSFc.clazz, "lStructSize", "I");
	GCP_RESULTSFc.lpOutString = (*env)->GetFieldID(env, GCP_RESULTSFc.clazz, "lpOutString", "I");
	GCP_RESULTSFc.lpOrder = (*env)->GetFieldID(env, GCP_RESULTSFc.clazz, "lpOrder", "I");
	GCP_RESULTSFc.lpDx = (*env)->GetFieldID(env, GCP_RESULTSFc.clazz, "lpDx", "I");
	GCP_RESULTSFc.lpCaretPos = (*env)->GetFieldID(env, GCP_RESULTSFc.clazz, "lpCaretPos", "I");
	GCP_RESULTSFc.lpClass = (*env)->GetFieldID(env, GCP_RESULTSFc.clazz, "lpClass", "I");
	GCP_RESULTSFc.lpGlyphs = (*env)->GetFieldID(env, GCP_RESULTSFc.clazz, "lpGlyphs", "I");
	GCP_RESULTSFc.nGlyphs = (*env)->GetFieldID(env, GCP_RESULTSFc.clazz, "nGlyphs", "I");
	GCP_RESULTSFc.nMaxFit = (*env)->GetFieldID(env, GCP_RESULTSFc.clazz, "nMaxFit", "I");
	GCP_RESULTSFc.cached = 1;
}

GCP_RESULTS *getGCP_RESULTSFields(JNIEnv *env, jobject lpObject, GCP_RESULTS *lpStruct)
{
	if (!GCP_RESULTSFc.cached) cacheGCP_RESULTSFields(env, lpObject);
	lpStruct->lStructSize = (*env)->GetIntField(env, lpObject, GCP_RESULTSFc.lStructSize);
	lpStruct->lpOutString = (LPTSTR)(*env)->GetIntField(env, lpObject, GCP_RESULTSFc.lpOutString);
	lpStruct->lpOrder = (UINT  *)(*env)->GetIntField(env, lpObject, GCP_RESULTSFc.lpOrder);
	lpStruct->lpDx = (int  *)(*env)->GetIntField(env, lpObject, GCP_RESULTSFc.lpDx);
	lpStruct->lpCaretPos = (int  *)(*env)->GetIntField(env, lpObject, GCP_RESULTSFc.lpCaretPos);
	lpStruct->lpClass = (LPSTR)(*env)->GetIntField(env, lpObject, GCP_RESULTSFc.lpClass);
	lpStruct->lpGlyphs = (LPWSTR)(*env)->GetIntField(env, lpObject, GCP_RESULTSFc.lpGlyphs);
	lpStruct->nGlyphs = (*env)->GetIntField(env, lpObject, GCP_RESULTSFc.nGlyphs);
	lpStruct->nMaxFit = (*env)->GetIntField(env, lpObject, GCP_RESULTSFc.nMaxFit);
	return lpStruct;
}

void setGCP_RESULTSFields(JNIEnv *env, jobject lpObject, GCP_RESULTS *lpStruct)
{
	if (!GCP_RESULTSFc.cached) cacheGCP_RESULTSFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GCP_RESULTSFc.lStructSize, (jint)lpStruct->lStructSize);
	(*env)->SetIntField(env, lpObject, GCP_RESULTSFc.lpOutString, (jint)lpStruct->lpOutString);
	(*env)->SetIntField(env, lpObject, GCP_RESULTSFc.lpOrder, (jint)lpStruct->lpOrder);
	(*env)->SetIntField(env, lpObject, GCP_RESULTSFc.lpDx, (jint)lpStruct->lpDx);
	(*env)->SetIntField(env, lpObject, GCP_RESULTSFc.lpCaretPos, (jint)lpStruct->lpCaretPos);
	(*env)->SetIntField(env, lpObject, GCP_RESULTSFc.lpClass, (jint)lpStruct->lpClass);
	(*env)->SetIntField(env, lpObject, GCP_RESULTSFc.lpGlyphs, (jint)lpStruct->lpGlyphs);
	(*env)->SetIntField(env, lpObject, GCP_RESULTSFc.nGlyphs, (jint)lpStruct->nGlyphs);
	(*env)->SetIntField(env, lpObject, GCP_RESULTSFc.nMaxFit, (jint)lpStruct->nMaxFit);
}
#endif

#ifndef NO_GRADIENT_RECT
typedef struct GRADIENT_RECT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID UpperLeft, LowerRight;
} GRADIENT_RECT_FID_CACHE;

GRADIENT_RECT_FID_CACHE GRADIENT_RECTFc;

void cacheGRADIENT_RECTFields(JNIEnv *env, jobject lpObject)
{
	if (GRADIENT_RECTFc.cached) return;
	GRADIENT_RECTFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GRADIENT_RECTFc.UpperLeft = (*env)->GetFieldID(env, GRADIENT_RECTFc.clazz, "UpperLeft", "I");
	GRADIENT_RECTFc.LowerRight = (*env)->GetFieldID(env, GRADIENT_RECTFc.clazz, "LowerRight", "I");
	GRADIENT_RECTFc.cached = 1;
}

GRADIENT_RECT *getGRADIENT_RECTFields(JNIEnv *env, jobject lpObject, GRADIENT_RECT *lpStruct)
{
	if (!GRADIENT_RECTFc.cached) cacheGRADIENT_RECTFields(env, lpObject);
	lpStruct->UpperLeft = (*env)->GetIntField(env, lpObject, GRADIENT_RECTFc.UpperLeft);
	lpStruct->LowerRight = (*env)->GetIntField(env, lpObject, GRADIENT_RECTFc.LowerRight);
	return lpStruct;
}

void setGRADIENT_RECTFields(JNIEnv *env, jobject lpObject, GRADIENT_RECT *lpStruct)
{
	if (!GRADIENT_RECTFc.cached) cacheGRADIENT_RECTFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GRADIENT_RECTFc.UpperLeft, (jint)lpStruct->UpperLeft);
	(*env)->SetIntField(env, lpObject, GRADIENT_RECTFc.LowerRight, (jint)lpStruct->LowerRight);
}
#endif

#ifndef NO_GUITHREADINFO
typedef struct GUITHREADINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, flags, hwndActive, hwndFocus, hwndCapture, hwndMenuOwner, hwndMoveSize, hwndCaret, left, top, right, bottom;
} GUITHREADINFO_FID_CACHE;

GUITHREADINFO_FID_CACHE GUITHREADINFOFc;

void cacheGUITHREADINFOFields(JNIEnv *env, jobject lpObject)
{
	if (GUITHREADINFOFc.cached) return;
	GUITHREADINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GUITHREADINFOFc.cbSize = (*env)->GetFieldID(env, GUITHREADINFOFc.clazz, "cbSize", "I");
	GUITHREADINFOFc.flags = (*env)->GetFieldID(env, GUITHREADINFOFc.clazz, "flags", "I");
	GUITHREADINFOFc.hwndActive = (*env)->GetFieldID(env, GUITHREADINFOFc.clazz, "hwndActive", "I");
	GUITHREADINFOFc.hwndFocus = (*env)->GetFieldID(env, GUITHREADINFOFc.clazz, "hwndFocus", "I");
	GUITHREADINFOFc.hwndCapture = (*env)->GetFieldID(env, GUITHREADINFOFc.clazz, "hwndCapture", "I");
	GUITHREADINFOFc.hwndMenuOwner = (*env)->GetFieldID(env, GUITHREADINFOFc.clazz, "hwndMenuOwner", "I");
	GUITHREADINFOFc.hwndMoveSize = (*env)->GetFieldID(env, GUITHREADINFOFc.clazz, "hwndMoveSize", "I");
	GUITHREADINFOFc.hwndCaret = (*env)->GetFieldID(env, GUITHREADINFOFc.clazz, "hwndCaret", "I");
	GUITHREADINFOFc.left = (*env)->GetFieldID(env, GUITHREADINFOFc.clazz, "left", "I");
	GUITHREADINFOFc.top = (*env)->GetFieldID(env, GUITHREADINFOFc.clazz, "top", "I");
	GUITHREADINFOFc.right = (*env)->GetFieldID(env, GUITHREADINFOFc.clazz, "right", "I");
	GUITHREADINFOFc.bottom = (*env)->GetFieldID(env, GUITHREADINFOFc.clazz, "bottom", "I");
	GUITHREADINFOFc.cached = 1;
}

GUITHREADINFO *getGUITHREADINFOFields(JNIEnv *env, jobject lpObject, GUITHREADINFO *lpStruct)
{
	if (!GUITHREADINFOFc.cached) cacheGUITHREADINFOFields(env, lpObject);
	lpStruct->cbSize = (DWORD)(*env)->GetIntField(env, lpObject, GUITHREADINFOFc.cbSize);
	lpStruct->flags = (DWORD)(*env)->GetIntField(env, lpObject, GUITHREADINFOFc.flags);
	lpStruct->hwndActive = (HWND)(*env)->GetIntField(env, lpObject, GUITHREADINFOFc.hwndActive);
	lpStruct->hwndFocus = (HWND)(*env)->GetIntField(env, lpObject, GUITHREADINFOFc.hwndFocus);
	lpStruct->hwndCapture = (HWND)(*env)->GetIntField(env, lpObject, GUITHREADINFOFc.hwndCapture);
	lpStruct->hwndMenuOwner = (HWND)(*env)->GetIntField(env, lpObject, GUITHREADINFOFc.hwndMenuOwner);
	lpStruct->hwndMoveSize = (HWND)(*env)->GetIntField(env, lpObject, GUITHREADINFOFc.hwndMoveSize);
	lpStruct->hwndCaret = (HWND)(*env)->GetIntField(env, lpObject, GUITHREADINFOFc.hwndCaret);
	lpStruct->rcCaret.left = (*env)->GetIntField(env, lpObject, GUITHREADINFOFc.left);
	lpStruct->rcCaret.top = (*env)->GetIntField(env, lpObject, GUITHREADINFOFc.top);
	lpStruct->rcCaret.right = (*env)->GetIntField(env, lpObject, GUITHREADINFOFc.right);
	lpStruct->rcCaret.bottom = (*env)->GetIntField(env, lpObject, GUITHREADINFOFc.bottom);
	return lpStruct;
}

void setGUITHREADINFOFields(JNIEnv *env, jobject lpObject, GUITHREADINFO *lpStruct)
{
	if (!GUITHREADINFOFc.cached) cacheGUITHREADINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GUITHREADINFOFc.cbSize, (jint)lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, GUITHREADINFOFc.flags, (jint)lpStruct->flags);
	(*env)->SetIntField(env, lpObject, GUITHREADINFOFc.hwndActive, (jint)lpStruct->hwndActive);
	(*env)->SetIntField(env, lpObject, GUITHREADINFOFc.hwndFocus, (jint)lpStruct->hwndFocus);
	(*env)->SetIntField(env, lpObject, GUITHREADINFOFc.hwndCapture, (jint)lpStruct->hwndCapture);
	(*env)->SetIntField(env, lpObject, GUITHREADINFOFc.hwndMenuOwner, (jint)lpStruct->hwndMenuOwner);
	(*env)->SetIntField(env, lpObject, GUITHREADINFOFc.hwndMoveSize, (jint)lpStruct->hwndMoveSize);
	(*env)->SetIntField(env, lpObject, GUITHREADINFOFc.hwndCaret, (jint)lpStruct->hwndCaret);
	(*env)->SetIntField(env, lpObject, GUITHREADINFOFc.left, (jint)lpStruct->rcCaret.left);
	(*env)->SetIntField(env, lpObject, GUITHREADINFOFc.top, (jint)lpStruct->rcCaret.top);
	(*env)->SetIntField(env, lpObject, GUITHREADINFOFc.right, (jint)lpStruct->rcCaret.right);
	(*env)->SetIntField(env, lpObject, GUITHREADINFOFc.bottom, (jint)lpStruct->rcCaret.bottom);
}
#endif

#ifndef NO_HDITEM
typedef struct HDITEM_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID mask, cxy, pszText, hbm, cchTextMax, fmt, lParam, iImage, iOrder;
} HDITEM_FID_CACHE;

HDITEM_FID_CACHE HDITEMFc;

void cacheHDITEMFields(JNIEnv *env, jobject lpObject)
{
	if (HDITEMFc.cached) return;
	HDITEMFc.clazz = (*env)->GetObjectClass(env, lpObject);
	HDITEMFc.mask = (*env)->GetFieldID(env, HDITEMFc.clazz, "mask", "I");
	HDITEMFc.cxy = (*env)->GetFieldID(env, HDITEMFc.clazz, "cxy", "I");
	HDITEMFc.pszText = (*env)->GetFieldID(env, HDITEMFc.clazz, "pszText", "I");
	HDITEMFc.hbm = (*env)->GetFieldID(env, HDITEMFc.clazz, "hbm", "I");
	HDITEMFc.cchTextMax = (*env)->GetFieldID(env, HDITEMFc.clazz, "cchTextMax", "I");
	HDITEMFc.fmt = (*env)->GetFieldID(env, HDITEMFc.clazz, "fmt", "I");
	HDITEMFc.lParam = (*env)->GetFieldID(env, HDITEMFc.clazz, "lParam", "I");
	HDITEMFc.iImage = (*env)->GetFieldID(env, HDITEMFc.clazz, "iImage", "I");
	HDITEMFc.iOrder = (*env)->GetFieldID(env, HDITEMFc.clazz, "iOrder", "I");
	HDITEMFc.cached = 1;
}

HDITEM *getHDITEMFields(JNIEnv *env, jobject lpObject, HDITEM *lpStruct)
{
	if (!HDITEMFc.cached) cacheHDITEMFields(env, lpObject);
	lpStruct->mask = (*env)->GetIntField(env, lpObject, HDITEMFc.mask);
	lpStruct->cxy = (*env)->GetIntField(env, lpObject, HDITEMFc.cxy);
	lpStruct->pszText = (LPTSTR)(*env)->GetIntField(env, lpObject, HDITEMFc.pszText);
	lpStruct->hbm = (HBITMAP)(*env)->GetIntField(env, lpObject, HDITEMFc.hbm);
	lpStruct->cchTextMax = (*env)->GetIntField(env, lpObject, HDITEMFc.cchTextMax);
	lpStruct->fmt = (*env)->GetIntField(env, lpObject, HDITEMFc.fmt);
	lpStruct->lParam = (*env)->GetIntField(env, lpObject, HDITEMFc.lParam);
	lpStruct->iImage = (*env)->GetIntField(env, lpObject, HDITEMFc.iImage);
	lpStruct->iOrder = (*env)->GetIntField(env, lpObject, HDITEMFc.iOrder);
	return lpStruct;
}

void setHDITEMFields(JNIEnv *env, jobject lpObject, HDITEM *lpStruct)
{
	if (!HDITEMFc.cached) cacheHDITEMFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, HDITEMFc.mask, (jint)lpStruct->mask);
	(*env)->SetIntField(env, lpObject, HDITEMFc.cxy, (jint)lpStruct->cxy);
	(*env)->SetIntField(env, lpObject, HDITEMFc.pszText, (jint)lpStruct->pszText);
	(*env)->SetIntField(env, lpObject, HDITEMFc.hbm, (jint)lpStruct->hbm);
	(*env)->SetIntField(env, lpObject, HDITEMFc.cchTextMax, (jint)lpStruct->cchTextMax);
	(*env)->SetIntField(env, lpObject, HDITEMFc.fmt, (jint)lpStruct->fmt);
	(*env)->SetIntField(env, lpObject, HDITEMFc.lParam, (jint)lpStruct->lParam);
	(*env)->SetIntField(env, lpObject, HDITEMFc.iImage, (jint)lpStruct->iImage);
	(*env)->SetIntField(env, lpObject, HDITEMFc.iOrder, (jint)lpStruct->iOrder);
}
#endif

#ifndef NO_HDLAYOUT
typedef struct HDLAYOUT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID prc, pwpos;
} HDLAYOUT_FID_CACHE;

HDLAYOUT_FID_CACHE HDLAYOUTFc;

void cacheHDLAYOUTFields(JNIEnv *env, jobject lpObject)
{
	if (HDLAYOUTFc.cached) return;
	HDLAYOUTFc.clazz = (*env)->GetObjectClass(env, lpObject);
	HDLAYOUTFc.prc = (*env)->GetFieldID(env, HDLAYOUTFc.clazz, "prc", "I");
	HDLAYOUTFc.pwpos = (*env)->GetFieldID(env, HDLAYOUTFc.clazz, "pwpos", "I");
	HDLAYOUTFc.cached = 1;
}

HDLAYOUT *getHDLAYOUTFields(JNIEnv *env, jobject lpObject, HDLAYOUT *lpStruct)
{
	if (!HDLAYOUTFc.cached) cacheHDLAYOUTFields(env, lpObject);
	lpStruct->prc = (RECT *)(*env)->GetIntField(env, lpObject, HDLAYOUTFc.prc);
	lpStruct->pwpos = (WINDOWPOS *)(*env)->GetIntField(env, lpObject, HDLAYOUTFc.pwpos);
	return lpStruct;
}

void setHDLAYOUTFields(JNIEnv *env, jobject lpObject, HDLAYOUT *lpStruct)
{
	if (!HDLAYOUTFc.cached) cacheHDLAYOUTFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, HDLAYOUTFc.prc, (jint)lpStruct->prc);
	(*env)->SetIntField(env, lpObject, HDLAYOUTFc.pwpos, (jint)lpStruct->pwpos);
}
#endif

#ifndef NO_HELPINFO
typedef struct HELPINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, iContextType, iCtrlId, hItemHandle, dwContextId, x, y;
} HELPINFO_FID_CACHE;

HELPINFO_FID_CACHE HELPINFOFc;

void cacheHELPINFOFields(JNIEnv *env, jobject lpObject)
{
	if (HELPINFOFc.cached) return;
	HELPINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	HELPINFOFc.cbSize = (*env)->GetFieldID(env, HELPINFOFc.clazz, "cbSize", "I");
	HELPINFOFc.iContextType = (*env)->GetFieldID(env, HELPINFOFc.clazz, "iContextType", "I");
	HELPINFOFc.iCtrlId = (*env)->GetFieldID(env, HELPINFOFc.clazz, "iCtrlId", "I");
	HELPINFOFc.hItemHandle = (*env)->GetFieldID(env, HELPINFOFc.clazz, "hItemHandle", "I");
	HELPINFOFc.dwContextId = (*env)->GetFieldID(env, HELPINFOFc.clazz, "dwContextId", "I");
	HELPINFOFc.x = (*env)->GetFieldID(env, HELPINFOFc.clazz, "x", "I");
	HELPINFOFc.y = (*env)->GetFieldID(env, HELPINFOFc.clazz, "y", "I");
	HELPINFOFc.cached = 1;
}

HELPINFO *getHELPINFOFields(JNIEnv *env, jobject lpObject, HELPINFO *lpStruct)
{
	if (!HELPINFOFc.cached) cacheHELPINFOFields(env, lpObject);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, HELPINFOFc.cbSize);
	lpStruct->iContextType = (*env)->GetIntField(env, lpObject, HELPINFOFc.iContextType);
	lpStruct->iCtrlId = (*env)->GetIntField(env, lpObject, HELPINFOFc.iCtrlId);
	lpStruct->hItemHandle = (HANDLE)(*env)->GetIntField(env, lpObject, HELPINFOFc.hItemHandle);
	lpStruct->dwContextId = (*env)->GetIntField(env, lpObject, HELPINFOFc.dwContextId);
	lpStruct->MousePos.x = (*env)->GetIntField(env, lpObject, HELPINFOFc.x);
	lpStruct->MousePos.y = (*env)->GetIntField(env, lpObject, HELPINFOFc.y);
	return lpStruct;
}

void setHELPINFOFields(JNIEnv *env, jobject lpObject, HELPINFO *lpStruct)
{
	if (!HELPINFOFc.cached) cacheHELPINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, HELPINFOFc.cbSize, (jint)lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, HELPINFOFc.iContextType, (jint)lpStruct->iContextType);
	(*env)->SetIntField(env, lpObject, HELPINFOFc.iCtrlId, (jint)lpStruct->iCtrlId);
	(*env)->SetIntField(env, lpObject, HELPINFOFc.hItemHandle, (jint)lpStruct->hItemHandle);
	(*env)->SetIntField(env, lpObject, HELPINFOFc.dwContextId, (jint)lpStruct->dwContextId);
	(*env)->SetIntField(env, lpObject, HELPINFOFc.x, (jint)lpStruct->MousePos.x);
	(*env)->SetIntField(env, lpObject, HELPINFOFc.y, (jint)lpStruct->MousePos.y);
}
#endif

#ifndef NO_HIGHCONTRAST
typedef struct HIGHCONTRAST_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, dwFlags, lpszDefaultScheme;
} HIGHCONTRAST_FID_CACHE;

HIGHCONTRAST_FID_CACHE HIGHCONTRASTFc;

void cacheHIGHCONTRASTFields(JNIEnv *env, jobject lpObject)
{
	if (HIGHCONTRASTFc.cached) return;
	HIGHCONTRASTFc.clazz = (*env)->GetObjectClass(env, lpObject);
	HIGHCONTRASTFc.cbSize = (*env)->GetFieldID(env, HIGHCONTRASTFc.clazz, "cbSize", "I");
	HIGHCONTRASTFc.dwFlags = (*env)->GetFieldID(env, HIGHCONTRASTFc.clazz, "dwFlags", "I");
	HIGHCONTRASTFc.lpszDefaultScheme = (*env)->GetFieldID(env, HIGHCONTRASTFc.clazz, "lpszDefaultScheme", "I");
	HIGHCONTRASTFc.cached = 1;
}

HIGHCONTRAST *getHIGHCONTRASTFields(JNIEnv *env, jobject lpObject, HIGHCONTRAST *lpStruct)
{
	if (!HIGHCONTRASTFc.cached) cacheHIGHCONTRASTFields(env, lpObject);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, HIGHCONTRASTFc.cbSize);
	lpStruct->dwFlags = (*env)->GetIntField(env, lpObject, HIGHCONTRASTFc.dwFlags);
	lpStruct->lpszDefaultScheme = (LPTSTR)(*env)->GetIntField(env, lpObject, HIGHCONTRASTFc.lpszDefaultScheme);
	return lpStruct;
}

void setHIGHCONTRASTFields(JNIEnv *env, jobject lpObject, HIGHCONTRAST *lpStruct)
{
	if (!HIGHCONTRASTFc.cached) cacheHIGHCONTRASTFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, HIGHCONTRASTFc.cbSize, (jint)lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, HIGHCONTRASTFc.dwFlags, (jint)lpStruct->dwFlags);
	(*env)->SetIntField(env, lpObject, HIGHCONTRASTFc.lpszDefaultScheme, (jint)lpStruct->lpszDefaultScheme);
}
#endif

#ifndef NO_ICONINFO
typedef struct ICONINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID fIcon, xHotspot, yHotspot, hbmMask, hbmColor;
} ICONINFO_FID_CACHE;

ICONINFO_FID_CACHE ICONINFOFc;

void cacheICONINFOFields(JNIEnv *env, jobject lpObject)
{
	if (ICONINFOFc.cached) return;
	ICONINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	ICONINFOFc.fIcon = (*env)->GetFieldID(env, ICONINFOFc.clazz, "fIcon", "Z");
	ICONINFOFc.xHotspot = (*env)->GetFieldID(env, ICONINFOFc.clazz, "xHotspot", "I");
	ICONINFOFc.yHotspot = (*env)->GetFieldID(env, ICONINFOFc.clazz, "yHotspot", "I");
	ICONINFOFc.hbmMask = (*env)->GetFieldID(env, ICONINFOFc.clazz, "hbmMask", "I");
	ICONINFOFc.hbmColor = (*env)->GetFieldID(env, ICONINFOFc.clazz, "hbmColor", "I");
	ICONINFOFc.cached = 1;
}

ICONINFO *getICONINFOFields(JNIEnv *env, jobject lpObject, ICONINFO *lpStruct)
{
	if (!ICONINFOFc.cached) cacheICONINFOFields(env, lpObject);
	lpStruct->fIcon = (*env)->GetBooleanField(env, lpObject, ICONINFOFc.fIcon);
	lpStruct->xHotspot = (*env)->GetIntField(env, lpObject, ICONINFOFc.xHotspot);
	lpStruct->yHotspot = (*env)->GetIntField(env, lpObject, ICONINFOFc.yHotspot);
	lpStruct->hbmMask = (HBITMAP)(*env)->GetIntField(env, lpObject, ICONINFOFc.hbmMask);
	lpStruct->hbmColor = (HBITMAP)(*env)->GetIntField(env, lpObject, ICONINFOFc.hbmColor);
	return lpStruct;
}

void setICONINFOFields(JNIEnv *env, jobject lpObject, ICONINFO *lpStruct)
{
	if (!ICONINFOFc.cached) cacheICONINFOFields(env, lpObject);
	(*env)->SetBooleanField(env, lpObject, ICONINFOFc.fIcon, (jboolean)lpStruct->fIcon);
	(*env)->SetIntField(env, lpObject, ICONINFOFc.xHotspot, (jint)lpStruct->xHotspot);
	(*env)->SetIntField(env, lpObject, ICONINFOFc.yHotspot, (jint)lpStruct->yHotspot);
	(*env)->SetIntField(env, lpObject, ICONINFOFc.hbmMask, (jint)lpStruct->hbmMask);
	(*env)->SetIntField(env, lpObject, ICONINFOFc.hbmColor, (jint)lpStruct->hbmColor);
}
#endif

#ifndef NO_INITCOMMONCONTROLSEX
typedef struct INITCOMMONCONTROLSEX_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID dwSize, dwICC;
} INITCOMMONCONTROLSEX_FID_CACHE;

INITCOMMONCONTROLSEX_FID_CACHE INITCOMMONCONTROLSEXFc;

void cacheINITCOMMONCONTROLSEXFields(JNIEnv *env, jobject lpObject)
{
	if (INITCOMMONCONTROLSEXFc.cached) return;
	INITCOMMONCONTROLSEXFc.clazz = (*env)->GetObjectClass(env, lpObject);
	INITCOMMONCONTROLSEXFc.dwSize = (*env)->GetFieldID(env, INITCOMMONCONTROLSEXFc.clazz, "dwSize", "I");
	INITCOMMONCONTROLSEXFc.dwICC = (*env)->GetFieldID(env, INITCOMMONCONTROLSEXFc.clazz, "dwICC", "I");
	INITCOMMONCONTROLSEXFc.cached = 1;
}

INITCOMMONCONTROLSEX *getINITCOMMONCONTROLSEXFields(JNIEnv *env, jobject lpObject, INITCOMMONCONTROLSEX *lpStruct)
{
	if (!INITCOMMONCONTROLSEXFc.cached) cacheINITCOMMONCONTROLSEXFields(env, lpObject);
	lpStruct->dwSize = (*env)->GetIntField(env, lpObject, INITCOMMONCONTROLSEXFc.dwSize);
	lpStruct->dwICC = (*env)->GetIntField(env, lpObject, INITCOMMONCONTROLSEXFc.dwICC);
	return lpStruct;
}

void setINITCOMMONCONTROLSEXFields(JNIEnv *env, jobject lpObject, INITCOMMONCONTROLSEX *lpStruct)
{
	if (!INITCOMMONCONTROLSEXFc.cached) cacheINITCOMMONCONTROLSEXFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, INITCOMMONCONTROLSEXFc.dwSize, (jint)lpStruct->dwSize);
	(*env)->SetIntField(env, lpObject, INITCOMMONCONTROLSEXFc.dwICC, (jint)lpStruct->dwICC);
}
#endif

#ifndef NO_INPUT
typedef struct INPUT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID type;
} INPUT_FID_CACHE;

INPUT_FID_CACHE INPUTFc;

void cacheINPUTFields(JNIEnv *env, jobject lpObject)
{
	if (INPUTFc.cached) return;
	INPUTFc.clazz = (*env)->GetObjectClass(env, lpObject);
	INPUTFc.type = (*env)->GetFieldID(env, INPUTFc.clazz, "type", "I");
	INPUTFc.cached = 1;
}

INPUT *getINPUTFields(JNIEnv *env, jobject lpObject, INPUT *lpStruct)
{
	if (!INPUTFc.cached) cacheINPUTFields(env, lpObject);
	lpStruct->type = (*env)->GetIntField(env, lpObject, INPUTFc.type);
	return lpStruct;
}

void setINPUTFields(JNIEnv *env, jobject lpObject, INPUT *lpStruct)
{
	if (!INPUTFc.cached) cacheINPUTFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, INPUTFc.type, (jint)lpStruct->type);
}
#endif

#ifndef NO_KEYBDINPUT
typedef struct KEYBDINPUT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID wVk, wScan, dwFlags, time, dwExtraInfo;
} KEYBDINPUT_FID_CACHE;

KEYBDINPUT_FID_CACHE KEYBDINPUTFc;

void cacheKEYBDINPUTFields(JNIEnv *env, jobject lpObject)
{
	if (KEYBDINPUTFc.cached) return;
	KEYBDINPUTFc.clazz = (*env)->GetObjectClass(env, lpObject);
	KEYBDINPUTFc.wVk = (*env)->GetFieldID(env, KEYBDINPUTFc.clazz, "wVk", "S");
	KEYBDINPUTFc.wScan = (*env)->GetFieldID(env, KEYBDINPUTFc.clazz, "wScan", "S");
	KEYBDINPUTFc.dwFlags = (*env)->GetFieldID(env, KEYBDINPUTFc.clazz, "dwFlags", "I");
	KEYBDINPUTFc.time = (*env)->GetFieldID(env, KEYBDINPUTFc.clazz, "time", "I");
	KEYBDINPUTFc.dwExtraInfo = (*env)->GetFieldID(env, KEYBDINPUTFc.clazz, "dwExtraInfo", "I");
	KEYBDINPUTFc.cached = 1;
}

KEYBDINPUT *getKEYBDINPUTFields(JNIEnv *env, jobject lpObject, KEYBDINPUT *lpStruct)
{
	if (!KEYBDINPUTFc.cached) cacheKEYBDINPUTFields(env, lpObject);
	lpStruct->wVk = (*env)->GetShortField(env, lpObject, KEYBDINPUTFc.wVk);
	lpStruct->wScan = (*env)->GetShortField(env, lpObject, KEYBDINPUTFc.wScan);
	lpStruct->dwFlags = (*env)->GetIntField(env, lpObject, KEYBDINPUTFc.dwFlags);
	lpStruct->time = (*env)->GetIntField(env, lpObject, KEYBDINPUTFc.time);
	lpStruct->dwExtraInfo = (*env)->GetIntField(env, lpObject, KEYBDINPUTFc.dwExtraInfo);
	return lpStruct;
}

void setKEYBDINPUTFields(JNIEnv *env, jobject lpObject, KEYBDINPUT *lpStruct)
{
	if (!KEYBDINPUTFc.cached) cacheKEYBDINPUTFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, KEYBDINPUTFc.wVk, (jshort)lpStruct->wVk);
	(*env)->SetShortField(env, lpObject, KEYBDINPUTFc.wScan, (jshort)lpStruct->wScan);
	(*env)->SetIntField(env, lpObject, KEYBDINPUTFc.dwFlags, (jint)lpStruct->dwFlags);
	(*env)->SetIntField(env, lpObject, KEYBDINPUTFc.time, (jint)lpStruct->time);
	(*env)->SetIntField(env, lpObject, KEYBDINPUTFc.dwExtraInfo, (jint)lpStruct->dwExtraInfo);
}
#endif

#ifndef NO_LITEM
typedef struct LITEM_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID mask, iLink, state, stateMask, szID, szUrl;
} LITEM_FID_CACHE;

LITEM_FID_CACHE LITEMFc;

void cacheLITEMFields(JNIEnv *env, jobject lpObject)
{
	if (LITEMFc.cached) return;
	LITEMFc.clazz = (*env)->GetObjectClass(env, lpObject);
	LITEMFc.mask = (*env)->GetFieldID(env, LITEMFc.clazz, "mask", "I");
	LITEMFc.iLink = (*env)->GetFieldID(env, LITEMFc.clazz, "iLink", "I");
	LITEMFc.state = (*env)->GetFieldID(env, LITEMFc.clazz, "state", "I");
	LITEMFc.stateMask = (*env)->GetFieldID(env, LITEMFc.clazz, "stateMask", "I");
	LITEMFc.szID = (*env)->GetFieldID(env, LITEMFc.clazz, "szID", "[C");
	LITEMFc.szUrl = (*env)->GetFieldID(env, LITEMFc.clazz, "szUrl", "[C");
	LITEMFc.cached = 1;
}

LITEM *getLITEMFields(JNIEnv *env, jobject lpObject, LITEM *lpStruct)
{
	if (!LITEMFc.cached) cacheLITEMFields(env, lpObject);
	lpStruct->mask = (*env)->GetIntField(env, lpObject, LITEMFc.mask);
	lpStruct->iLink = (*env)->GetIntField(env, lpObject, LITEMFc.iLink);
	lpStruct->state = (*env)->GetIntField(env, lpObject, LITEMFc.state);
	lpStruct->stateMask = (*env)->GetIntField(env, lpObject, LITEMFc.stateMask);
	{
	jcharArray lpObject1 = (jcharArray)(*env)->GetObjectField(env, lpObject, LITEMFc.szID);
	(*env)->GetCharArrayRegion(env, lpObject1, 0, sizeof(lpStruct->szID) / 2, (jchar *)lpStruct->szID);
	}
	{
	jcharArray lpObject1 = (jcharArray)(*env)->GetObjectField(env, lpObject, LITEMFc.szUrl);
	(*env)->GetCharArrayRegion(env, lpObject1, 0, sizeof(lpStruct->szUrl) / 2, (jchar *)lpStruct->szUrl);
	}
	return lpStruct;
}

void setLITEMFields(JNIEnv *env, jobject lpObject, LITEM *lpStruct)
{
	if (!LITEMFc.cached) cacheLITEMFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, LITEMFc.mask, (jint)lpStruct->mask);
	(*env)->SetIntField(env, lpObject, LITEMFc.iLink, (jint)lpStruct->iLink);
	(*env)->SetIntField(env, lpObject, LITEMFc.state, (jint)lpStruct->state);
	(*env)->SetIntField(env, lpObject, LITEMFc.stateMask, (jint)lpStruct->stateMask);
	{
	jcharArray lpObject1 = (jcharArray)(*env)->GetObjectField(env, lpObject, LITEMFc.szID);
	(*env)->SetCharArrayRegion(env, lpObject1, 0, sizeof(lpStruct->szID) / 2, (jchar *)lpStruct->szID);
	}
	{
	jcharArray lpObject1 = (jcharArray)(*env)->GetObjectField(env, lpObject, LITEMFc.szUrl);
	(*env)->SetCharArrayRegion(env, lpObject1, 0, sizeof(lpStruct->szUrl) / 2, (jchar *)lpStruct->szUrl);
	}
}
#endif

#ifndef NO_LOGBRUSH
typedef struct LOGBRUSH_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lbStyle, lbColor, lbHatch;
} LOGBRUSH_FID_CACHE;

LOGBRUSH_FID_CACHE LOGBRUSHFc;

void cacheLOGBRUSHFields(JNIEnv *env, jobject lpObject)
{
	if (LOGBRUSHFc.cached) return;
	LOGBRUSHFc.clazz = (*env)->GetObjectClass(env, lpObject);
	LOGBRUSHFc.lbStyle = (*env)->GetFieldID(env, LOGBRUSHFc.clazz, "lbStyle", "I");
	LOGBRUSHFc.lbColor = (*env)->GetFieldID(env, LOGBRUSHFc.clazz, "lbColor", "I");
	LOGBRUSHFc.lbHatch = (*env)->GetFieldID(env, LOGBRUSHFc.clazz, "lbHatch", "I");
	LOGBRUSHFc.cached = 1;
}

LOGBRUSH *getLOGBRUSHFields(JNIEnv *env, jobject lpObject, LOGBRUSH *lpStruct)
{
	if (!LOGBRUSHFc.cached) cacheLOGBRUSHFields(env, lpObject);
	lpStruct->lbStyle = (*env)->GetIntField(env, lpObject, LOGBRUSHFc.lbStyle);
	lpStruct->lbColor = (*env)->GetIntField(env, lpObject, LOGBRUSHFc.lbColor);
	lpStruct->lbHatch = (*env)->GetIntField(env, lpObject, LOGBRUSHFc.lbHatch);
	return lpStruct;
}

void setLOGBRUSHFields(JNIEnv *env, jobject lpObject, LOGBRUSH *lpStruct)
{
	if (!LOGBRUSHFc.cached) cacheLOGBRUSHFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, LOGBRUSHFc.lbStyle, (jint)lpStruct->lbStyle);
	(*env)->SetIntField(env, lpObject, LOGBRUSHFc.lbColor, (jint)lpStruct->lbColor);
	(*env)->SetIntField(env, lpObject, LOGBRUSHFc.lbHatch, (jint)lpStruct->lbHatch);
}
#endif

#ifndef NO_LOGFONT
typedef struct LOGFONT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lfHeight, lfWidth, lfEscapement, lfOrientation, lfWeight, lfItalic, lfUnderline, lfStrikeOut, lfCharSet, lfOutPrecision, lfClipPrecision, lfQuality, lfPitchAndFamily;
} LOGFONT_FID_CACHE;

LOGFONT_FID_CACHE LOGFONTFc;

void cacheLOGFONTFields(JNIEnv *env, jobject lpObject)
{
	if (LOGFONTFc.cached) return;
	LOGFONTFc.clazz = (*env)->GetObjectClass(env, lpObject);
	LOGFONTFc.lfHeight = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfHeight", "I");
	LOGFONTFc.lfWidth = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfWidth", "I");
	LOGFONTFc.lfEscapement = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfEscapement", "I");
	LOGFONTFc.lfOrientation = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfOrientation", "I");
	LOGFONTFc.lfWeight = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfWeight", "I");
	LOGFONTFc.lfItalic = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfItalic", "B");
	LOGFONTFc.lfUnderline = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfUnderline", "B");
	LOGFONTFc.lfStrikeOut = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfStrikeOut", "B");
	LOGFONTFc.lfCharSet = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfCharSet", "B");
	LOGFONTFc.lfOutPrecision = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfOutPrecision", "B");
	LOGFONTFc.lfClipPrecision = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfClipPrecision", "B");
	LOGFONTFc.lfQuality = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfQuality", "B");
	LOGFONTFc.lfPitchAndFamily = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfPitchAndFamily", "B");
	LOGFONTFc.cached = 1;
}

LOGFONT *getLOGFONTFields(JNIEnv *env, jobject lpObject, LOGFONT *lpStruct)
{
	if (!LOGFONTFc.cached) cacheLOGFONTFields(env, lpObject);
	lpStruct->lfHeight = (*env)->GetIntField(env, lpObject, LOGFONTFc.lfHeight);
	lpStruct->lfWidth = (*env)->GetIntField(env, lpObject, LOGFONTFc.lfWidth);
	lpStruct->lfEscapement = (*env)->GetIntField(env, lpObject, LOGFONTFc.lfEscapement);
	lpStruct->lfOrientation = (*env)->GetIntField(env, lpObject, LOGFONTFc.lfOrientation);
	lpStruct->lfWeight = (*env)->GetIntField(env, lpObject, LOGFONTFc.lfWeight);
	lpStruct->lfItalic = (*env)->GetByteField(env, lpObject, LOGFONTFc.lfItalic);
	lpStruct->lfUnderline = (*env)->GetByteField(env, lpObject, LOGFONTFc.lfUnderline);
	lpStruct->lfStrikeOut = (*env)->GetByteField(env, lpObject, LOGFONTFc.lfStrikeOut);
	lpStruct->lfCharSet = (*env)->GetByteField(env, lpObject, LOGFONTFc.lfCharSet);
	lpStruct->lfOutPrecision = (*env)->GetByteField(env, lpObject, LOGFONTFc.lfOutPrecision);
	lpStruct->lfClipPrecision = (*env)->GetByteField(env, lpObject, LOGFONTFc.lfClipPrecision);
	lpStruct->lfQuality = (*env)->GetByteField(env, lpObject, LOGFONTFc.lfQuality);
	lpStruct->lfPitchAndFamily = (*env)->GetByteField(env, lpObject, LOGFONTFc.lfPitchAndFamily);
	return lpStruct;
}

void setLOGFONTFields(JNIEnv *env, jobject lpObject, LOGFONT *lpStruct)
{
	if (!LOGFONTFc.cached) cacheLOGFONTFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, LOGFONTFc.lfHeight, (jint)lpStruct->lfHeight);
	(*env)->SetIntField(env, lpObject, LOGFONTFc.lfWidth, (jint)lpStruct->lfWidth);
	(*env)->SetIntField(env, lpObject, LOGFONTFc.lfEscapement, (jint)lpStruct->lfEscapement);
	(*env)->SetIntField(env, lpObject, LOGFONTFc.lfOrientation, (jint)lpStruct->lfOrientation);
	(*env)->SetIntField(env, lpObject, LOGFONTFc.lfWeight, (jint)lpStruct->lfWeight);
	(*env)->SetByteField(env, lpObject, LOGFONTFc.lfItalic, (jbyte)lpStruct->lfItalic);
	(*env)->SetByteField(env, lpObject, LOGFONTFc.lfUnderline, (jbyte)lpStruct->lfUnderline);
	(*env)->SetByteField(env, lpObject, LOGFONTFc.lfStrikeOut, (jbyte)lpStruct->lfStrikeOut);
	(*env)->SetByteField(env, lpObject, LOGFONTFc.lfCharSet, (jbyte)lpStruct->lfCharSet);
	(*env)->SetByteField(env, lpObject, LOGFONTFc.lfOutPrecision, (jbyte)lpStruct->lfOutPrecision);
	(*env)->SetByteField(env, lpObject, LOGFONTFc.lfClipPrecision, (jbyte)lpStruct->lfClipPrecision);
	(*env)->SetByteField(env, lpObject, LOGFONTFc.lfQuality, (jbyte)lpStruct->lfQuality);
	(*env)->SetByteField(env, lpObject, LOGFONTFc.lfPitchAndFamily, (jbyte)lpStruct->lfPitchAndFamily);
}
#endif

#ifndef NO_LOGFONTA
typedef struct LOGFONTA_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lfFaceName;
} LOGFONTA_FID_CACHE;

LOGFONTA_FID_CACHE LOGFONTAFc;

void cacheLOGFONTAFields(JNIEnv *env, jobject lpObject)
{
	if (LOGFONTAFc.cached) return;
	cacheLOGFONTFields(env, lpObject);
	LOGFONTAFc.clazz = (*env)->GetObjectClass(env, lpObject);
	LOGFONTAFc.lfFaceName = (*env)->GetFieldID(env, LOGFONTAFc.clazz, "lfFaceName", "[B");
	LOGFONTAFc.cached = 1;
}

LOGFONTA *getLOGFONTAFields(JNIEnv *env, jobject lpObject, LOGFONTA *lpStruct)
{
	if (!LOGFONTAFc.cached) cacheLOGFONTAFields(env, lpObject);
	lpStruct->lfHeight = (*env)->GetIntField(env, lpObject, LOGFONTFc.lfHeight);
	lpStruct->lfWidth = (*env)->GetIntField(env, lpObject, LOGFONTFc.lfWidth);
	lpStruct->lfEscapement = (*env)->GetIntField(env, lpObject, LOGFONTFc.lfEscapement);
	lpStruct->lfOrientation = (*env)->GetIntField(env, lpObject, LOGFONTFc.lfOrientation);
	lpStruct->lfWeight = (*env)->GetIntField(env, lpObject, LOGFONTFc.lfWeight);
	lpStruct->lfItalic = (*env)->GetByteField(env, lpObject, LOGFONTFc.lfItalic);
	lpStruct->lfUnderline = (*env)->GetByteField(env, lpObject, LOGFONTFc.lfUnderline);
	lpStruct->lfStrikeOut = (*env)->GetByteField(env, lpObject, LOGFONTFc.lfStrikeOut);
	lpStruct->lfCharSet = (*env)->GetByteField(env, lpObject, LOGFONTFc.lfCharSet);
	lpStruct->lfOutPrecision = (*env)->GetByteField(env, lpObject, LOGFONTFc.lfOutPrecision);
	lpStruct->lfClipPrecision = (*env)->GetByteField(env, lpObject, LOGFONTFc.lfClipPrecision);
	lpStruct->lfQuality = (*env)->GetByteField(env, lpObject, LOGFONTFc.lfQuality);
	lpStruct->lfPitchAndFamily = (*env)->GetByteField(env, lpObject, LOGFONTFc.lfPitchAndFamily);
	{
	jbyteArray lpObject1 = (jbyteArray)(*env)->GetObjectField(env, lpObject, LOGFONTAFc.lfFaceName);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->lfFaceName), (jbyte *)lpStruct->lfFaceName);
	}
	return lpStruct;
}

void setLOGFONTAFields(JNIEnv *env, jobject lpObject, LOGFONTA *lpStruct)
{
	if (!LOGFONTAFc.cached) cacheLOGFONTAFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, LOGFONTFc.lfHeight, (jint)lpStruct->lfHeight);
	(*env)->SetIntField(env, lpObject, LOGFONTFc.lfWidth, (jint)lpStruct->lfWidth);
	(*env)->SetIntField(env, lpObject, LOGFONTFc.lfEscapement, (jint)lpStruct->lfEscapement);
	(*env)->SetIntField(env, lpObject, LOGFONTFc.lfOrientation, (jint)lpStruct->lfOrientation);
	(*env)->SetIntField(env, lpObject, LOGFONTFc.lfWeight, (jint)lpStruct->lfWeight);
	(*env)->SetByteField(env, lpObject, LOGFONTFc.lfItalic, (jbyte)lpStruct->lfItalic);
	(*env)->SetByteField(env, lpObject, LOGFONTFc.lfUnderline, (jbyte)lpStruct->lfUnderline);
	(*env)->SetByteField(env, lpObject, LOGFONTFc.lfStrikeOut, (jbyte)lpStruct->lfStrikeOut);
	(*env)->SetByteField(env, lpObject, LOGFONTFc.lfCharSet, (jbyte)lpStruct->lfCharSet);
	(*env)->SetByteField(env, lpObject, LOGFONTFc.lfOutPrecision, (jbyte)lpStruct->lfOutPrecision);
	(*env)->SetByteField(env, lpObject, LOGFONTFc.lfClipPrecision, (jbyte)lpStruct->lfClipPrecision);
	(*env)->SetByteField(env, lpObject, LOGFONTFc.lfQuality, (jbyte)lpStruct->lfQuality);
	(*env)->SetByteField(env, lpObject, LOGFONTFc.lfPitchAndFamily, (jbyte)lpStruct->lfPitchAndFamily);
	{
	jbyteArray lpObject1 = (jbyteArray)(*env)->GetObjectField(env, lpObject, LOGFONTAFc.lfFaceName);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->lfFaceName), (jbyte *)lpStruct->lfFaceName);
	}
}
#endif

#ifndef NO_LOGFONTW
typedef struct LOGFONTW_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lfFaceName;
} LOGFONTW_FID_CACHE;

LOGFONTW_FID_CACHE LOGFONTWFc;

void cacheLOGFONTWFields(JNIEnv *env, jobject lpObject)
{
	if (LOGFONTWFc.cached) return;
	cacheLOGFONTFields(env, lpObject);
	LOGFONTWFc.clazz = (*env)->GetObjectClass(env, lpObject);
	LOGFONTWFc.lfFaceName = (*env)->GetFieldID(env, LOGFONTWFc.clazz, "lfFaceName", "[C");
	LOGFONTWFc.cached = 1;
}

LOGFONTW *getLOGFONTWFields(JNIEnv *env, jobject lpObject, LOGFONTW *lpStruct)
{
	if (!LOGFONTWFc.cached) cacheLOGFONTWFields(env, lpObject);
	lpStruct->lfHeight = (*env)->GetIntField(env, lpObject, LOGFONTFc.lfHeight);
	lpStruct->lfWidth = (*env)->GetIntField(env, lpObject, LOGFONTFc.lfWidth);
	lpStruct->lfEscapement = (*env)->GetIntField(env, lpObject, LOGFONTFc.lfEscapement);
	lpStruct->lfOrientation = (*env)->GetIntField(env, lpObject, LOGFONTFc.lfOrientation);
	lpStruct->lfWeight = (*env)->GetIntField(env, lpObject, LOGFONTFc.lfWeight);
	lpStruct->lfItalic = (*env)->GetByteField(env, lpObject, LOGFONTFc.lfItalic);
	lpStruct->lfUnderline = (*env)->GetByteField(env, lpObject, LOGFONTFc.lfUnderline);
	lpStruct->lfStrikeOut = (*env)->GetByteField(env, lpObject, LOGFONTFc.lfStrikeOut);
	lpStruct->lfCharSet = (*env)->GetByteField(env, lpObject, LOGFONTFc.lfCharSet);
	lpStruct->lfOutPrecision = (*env)->GetByteField(env, lpObject, LOGFONTFc.lfOutPrecision);
	lpStruct->lfClipPrecision = (*env)->GetByteField(env, lpObject, LOGFONTFc.lfClipPrecision);
	lpStruct->lfQuality = (*env)->GetByteField(env, lpObject, LOGFONTFc.lfQuality);
	lpStruct->lfPitchAndFamily = (*env)->GetByteField(env, lpObject, LOGFONTFc.lfPitchAndFamily);
	{
	jcharArray lpObject1 = (jcharArray)(*env)->GetObjectField(env, lpObject, LOGFONTWFc.lfFaceName);
	(*env)->GetCharArrayRegion(env, lpObject1, 0, sizeof(lpStruct->lfFaceName) / 2, (jchar *)lpStruct->lfFaceName);
	}
	return lpStruct;
}

void setLOGFONTWFields(JNIEnv *env, jobject lpObject, LOGFONTW *lpStruct)
{
	if (!LOGFONTWFc.cached) cacheLOGFONTWFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, LOGFONTFc.lfHeight, (jint)lpStruct->lfHeight);
	(*env)->SetIntField(env, lpObject, LOGFONTFc.lfWidth, (jint)lpStruct->lfWidth);
	(*env)->SetIntField(env, lpObject, LOGFONTFc.lfEscapement, (jint)lpStruct->lfEscapement);
	(*env)->SetIntField(env, lpObject, LOGFONTFc.lfOrientation, (jint)lpStruct->lfOrientation);
	(*env)->SetIntField(env, lpObject, LOGFONTFc.lfWeight, (jint)lpStruct->lfWeight);
	(*env)->SetByteField(env, lpObject, LOGFONTFc.lfItalic, (jbyte)lpStruct->lfItalic);
	(*env)->SetByteField(env, lpObject, LOGFONTFc.lfUnderline, (jbyte)lpStruct->lfUnderline);
	(*env)->SetByteField(env, lpObject, LOGFONTFc.lfStrikeOut, (jbyte)lpStruct->lfStrikeOut);
	(*env)->SetByteField(env, lpObject, LOGFONTFc.lfCharSet, (jbyte)lpStruct->lfCharSet);
	(*env)->SetByteField(env, lpObject, LOGFONTFc.lfOutPrecision, (jbyte)lpStruct->lfOutPrecision);
	(*env)->SetByteField(env, lpObject, LOGFONTFc.lfClipPrecision, (jbyte)lpStruct->lfClipPrecision);
	(*env)->SetByteField(env, lpObject, LOGFONTFc.lfQuality, (jbyte)lpStruct->lfQuality);
	(*env)->SetByteField(env, lpObject, LOGFONTFc.lfPitchAndFamily, (jbyte)lpStruct->lfPitchAndFamily);
	{
	jcharArray lpObject1 = (jcharArray)(*env)->GetObjectField(env, lpObject, LOGFONTWFc.lfFaceName);
	(*env)->SetCharArrayRegion(env, lpObject1, 0, sizeof(lpStruct->lfFaceName) / 2, (jchar *)lpStruct->lfFaceName);
	}
}
#endif

#ifndef NO_LOGPEN
typedef struct LOGPEN_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lopnStyle, x, y, lopnColor;
} LOGPEN_FID_CACHE;

LOGPEN_FID_CACHE LOGPENFc;

void cacheLOGPENFields(JNIEnv *env, jobject lpObject)
{
	if (LOGPENFc.cached) return;
	LOGPENFc.clazz = (*env)->GetObjectClass(env, lpObject);
	LOGPENFc.lopnStyle = (*env)->GetFieldID(env, LOGPENFc.clazz, "lopnStyle", "I");
	LOGPENFc.x = (*env)->GetFieldID(env, LOGPENFc.clazz, "x", "I");
	LOGPENFc.y = (*env)->GetFieldID(env, LOGPENFc.clazz, "y", "I");
	LOGPENFc.lopnColor = (*env)->GetFieldID(env, LOGPENFc.clazz, "lopnColor", "I");
	LOGPENFc.cached = 1;
}

LOGPEN *getLOGPENFields(JNIEnv *env, jobject lpObject, LOGPEN *lpStruct)
{
	if (!LOGPENFc.cached) cacheLOGPENFields(env, lpObject);
	lpStruct->lopnStyle = (*env)->GetIntField(env, lpObject, LOGPENFc.lopnStyle);
	lpStruct->lopnWidth.x = (*env)->GetIntField(env, lpObject, LOGPENFc.x);
	lpStruct->lopnWidth.y = (*env)->GetIntField(env, lpObject, LOGPENFc.y);
	lpStruct->lopnColor = (*env)->GetIntField(env, lpObject, LOGPENFc.lopnColor);
	return lpStruct;
}

void setLOGPENFields(JNIEnv *env, jobject lpObject, LOGPEN *lpStruct)
{
	if (!LOGPENFc.cached) cacheLOGPENFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, LOGPENFc.lopnStyle, (jint)lpStruct->lopnStyle);
	(*env)->SetIntField(env, lpObject, LOGPENFc.x, (jint)lpStruct->lopnWidth.x);
	(*env)->SetIntField(env, lpObject, LOGPENFc.y, (jint)lpStruct->lopnWidth.y);
	(*env)->SetIntField(env, lpObject, LOGPENFc.lopnColor, (jint)lpStruct->lopnColor);
}
#endif

#ifndef NO_LVCOLUMN
typedef struct LVCOLUMN_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID mask, fmt, cx, pszText, cchTextMax, iSubItem, iImage, iOrder;
} LVCOLUMN_FID_CACHE;

LVCOLUMN_FID_CACHE LVCOLUMNFc;

void cacheLVCOLUMNFields(JNIEnv *env, jobject lpObject)
{
	if (LVCOLUMNFc.cached) return;
	LVCOLUMNFc.clazz = (*env)->GetObjectClass(env, lpObject);
	LVCOLUMNFc.mask = (*env)->GetFieldID(env, LVCOLUMNFc.clazz, "mask", "I");
	LVCOLUMNFc.fmt = (*env)->GetFieldID(env, LVCOLUMNFc.clazz, "fmt", "I");
	LVCOLUMNFc.cx = (*env)->GetFieldID(env, LVCOLUMNFc.clazz, "cx", "I");
	LVCOLUMNFc.pszText = (*env)->GetFieldID(env, LVCOLUMNFc.clazz, "pszText", "I");
	LVCOLUMNFc.cchTextMax = (*env)->GetFieldID(env, LVCOLUMNFc.clazz, "cchTextMax", "I");
	LVCOLUMNFc.iSubItem = (*env)->GetFieldID(env, LVCOLUMNFc.clazz, "iSubItem", "I");
	LVCOLUMNFc.iImage = (*env)->GetFieldID(env, LVCOLUMNFc.clazz, "iImage", "I");
	LVCOLUMNFc.iOrder = (*env)->GetFieldID(env, LVCOLUMNFc.clazz, "iOrder", "I");
	LVCOLUMNFc.cached = 1;
}

LVCOLUMN *getLVCOLUMNFields(JNIEnv *env, jobject lpObject, LVCOLUMN *lpStruct)
{
	if (!LVCOLUMNFc.cached) cacheLVCOLUMNFields(env, lpObject);
	lpStruct->mask = (*env)->GetIntField(env, lpObject, LVCOLUMNFc.mask);
	lpStruct->fmt = (*env)->GetIntField(env, lpObject, LVCOLUMNFc.fmt);
	lpStruct->cx = (*env)->GetIntField(env, lpObject, LVCOLUMNFc.cx);
	lpStruct->pszText = (LPTSTR)(*env)->GetIntField(env, lpObject, LVCOLUMNFc.pszText);
	lpStruct->cchTextMax = (*env)->GetIntField(env, lpObject, LVCOLUMNFc.cchTextMax);
	lpStruct->iSubItem = (*env)->GetIntField(env, lpObject, LVCOLUMNFc.iSubItem);
	lpStruct->iImage = (*env)->GetIntField(env, lpObject, LVCOLUMNFc.iImage);
	lpStruct->iOrder = (*env)->GetIntField(env, lpObject, LVCOLUMNFc.iOrder);
	return lpStruct;
}

void setLVCOLUMNFields(JNIEnv *env, jobject lpObject, LVCOLUMN *lpStruct)
{
	if (!LVCOLUMNFc.cached) cacheLVCOLUMNFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, LVCOLUMNFc.mask, (jint)lpStruct->mask);
	(*env)->SetIntField(env, lpObject, LVCOLUMNFc.fmt, (jint)lpStruct->fmt);
	(*env)->SetIntField(env, lpObject, LVCOLUMNFc.cx, (jint)lpStruct->cx);
	(*env)->SetIntField(env, lpObject, LVCOLUMNFc.pszText, (jint)lpStruct->pszText);
	(*env)->SetIntField(env, lpObject, LVCOLUMNFc.cchTextMax, (jint)lpStruct->cchTextMax);
	(*env)->SetIntField(env, lpObject, LVCOLUMNFc.iSubItem, (jint)lpStruct->iSubItem);
	(*env)->SetIntField(env, lpObject, LVCOLUMNFc.iImage, (jint)lpStruct->iImage);
	(*env)->SetIntField(env, lpObject, LVCOLUMNFc.iOrder, (jint)lpStruct->iOrder);
}
#endif

#ifndef NO_LVHITTESTINFO
typedef struct LVHITTESTINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID x, y, flags, iItem, iSubItem;
} LVHITTESTINFO_FID_CACHE;

LVHITTESTINFO_FID_CACHE LVHITTESTINFOFc;

void cacheLVHITTESTINFOFields(JNIEnv *env, jobject lpObject)
{
	if (LVHITTESTINFOFc.cached) return;
	LVHITTESTINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	LVHITTESTINFOFc.x = (*env)->GetFieldID(env, LVHITTESTINFOFc.clazz, "x", "I");
	LVHITTESTINFOFc.y = (*env)->GetFieldID(env, LVHITTESTINFOFc.clazz, "y", "I");
	LVHITTESTINFOFc.flags = (*env)->GetFieldID(env, LVHITTESTINFOFc.clazz, "flags", "I");
	LVHITTESTINFOFc.iItem = (*env)->GetFieldID(env, LVHITTESTINFOFc.clazz, "iItem", "I");
	LVHITTESTINFOFc.iSubItem = (*env)->GetFieldID(env, LVHITTESTINFOFc.clazz, "iSubItem", "I");
	LVHITTESTINFOFc.cached = 1;
}

LVHITTESTINFO *getLVHITTESTINFOFields(JNIEnv *env, jobject lpObject, LVHITTESTINFO *lpStruct)
{
	if (!LVHITTESTINFOFc.cached) cacheLVHITTESTINFOFields(env, lpObject);
	lpStruct->pt.x = (*env)->GetIntField(env, lpObject, LVHITTESTINFOFc.x);
	lpStruct->pt.y = (*env)->GetIntField(env, lpObject, LVHITTESTINFOFc.y);
	lpStruct->flags = (*env)->GetIntField(env, lpObject, LVHITTESTINFOFc.flags);
	lpStruct->iItem = (*env)->GetIntField(env, lpObject, LVHITTESTINFOFc.iItem);
	lpStruct->iSubItem = (*env)->GetIntField(env, lpObject, LVHITTESTINFOFc.iSubItem);
	return lpStruct;
}

void setLVHITTESTINFOFields(JNIEnv *env, jobject lpObject, LVHITTESTINFO *lpStruct)
{
	if (!LVHITTESTINFOFc.cached) cacheLVHITTESTINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, LVHITTESTINFOFc.x, (jint)lpStruct->pt.x);
	(*env)->SetIntField(env, lpObject, LVHITTESTINFOFc.y, (jint)lpStruct->pt.y);
	(*env)->SetIntField(env, lpObject, LVHITTESTINFOFc.flags, (jint)lpStruct->flags);
	(*env)->SetIntField(env, lpObject, LVHITTESTINFOFc.iItem, (jint)lpStruct->iItem);
	(*env)->SetIntField(env, lpObject, LVHITTESTINFOFc.iSubItem, (jint)lpStruct->iSubItem);
}
#endif

#ifndef NO_LVITEM
typedef struct LVITEM_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID mask, iItem, iSubItem, state, stateMask, pszText, cchTextMax, iImage, lParam, iIndent;
} LVITEM_FID_CACHE;

LVITEM_FID_CACHE LVITEMFc;

void cacheLVITEMFields(JNIEnv *env, jobject lpObject)
{
	if (LVITEMFc.cached) return;
	LVITEMFc.clazz = (*env)->GetObjectClass(env, lpObject);
	LVITEMFc.mask = (*env)->GetFieldID(env, LVITEMFc.clazz, "mask", "I");
	LVITEMFc.iItem = (*env)->GetFieldID(env, LVITEMFc.clazz, "iItem", "I");
	LVITEMFc.iSubItem = (*env)->GetFieldID(env, LVITEMFc.clazz, "iSubItem", "I");
	LVITEMFc.state = (*env)->GetFieldID(env, LVITEMFc.clazz, "state", "I");
	LVITEMFc.stateMask = (*env)->GetFieldID(env, LVITEMFc.clazz, "stateMask", "I");
	LVITEMFc.pszText = (*env)->GetFieldID(env, LVITEMFc.clazz, "pszText", "I");
	LVITEMFc.cchTextMax = (*env)->GetFieldID(env, LVITEMFc.clazz, "cchTextMax", "I");
	LVITEMFc.iImage = (*env)->GetFieldID(env, LVITEMFc.clazz, "iImage", "I");
	LVITEMFc.lParam = (*env)->GetFieldID(env, LVITEMFc.clazz, "lParam", "I");
	LVITEMFc.iIndent = (*env)->GetFieldID(env, LVITEMFc.clazz, "iIndent", "I");
	LVITEMFc.cached = 1;
}

LVITEM *getLVITEMFields(JNIEnv *env, jobject lpObject, LVITEM *lpStruct)
{
	if (!LVITEMFc.cached) cacheLVITEMFields(env, lpObject);
	lpStruct->mask = (*env)->GetIntField(env, lpObject, LVITEMFc.mask);
	lpStruct->iItem = (*env)->GetIntField(env, lpObject, LVITEMFc.iItem);
	lpStruct->iSubItem = (*env)->GetIntField(env, lpObject, LVITEMFc.iSubItem);
	lpStruct->state = (*env)->GetIntField(env, lpObject, LVITEMFc.state);
	lpStruct->stateMask = (*env)->GetIntField(env, lpObject, LVITEMFc.stateMask);
	lpStruct->pszText = (LPTSTR)(*env)->GetIntField(env, lpObject, LVITEMFc.pszText);
	lpStruct->cchTextMax = (*env)->GetIntField(env, lpObject, LVITEMFc.cchTextMax);
	lpStruct->iImage = (*env)->GetIntField(env, lpObject, LVITEMFc.iImage);
	lpStruct->lParam = (*env)->GetIntField(env, lpObject, LVITEMFc.lParam);
	lpStruct->iIndent = (*env)->GetIntField(env, lpObject, LVITEMFc.iIndent);
	return lpStruct;
}

void setLVITEMFields(JNIEnv *env, jobject lpObject, LVITEM *lpStruct)
{
	if (!LVITEMFc.cached) cacheLVITEMFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, LVITEMFc.mask, (jint)lpStruct->mask);
	(*env)->SetIntField(env, lpObject, LVITEMFc.iItem, (jint)lpStruct->iItem);
	(*env)->SetIntField(env, lpObject, LVITEMFc.iSubItem, (jint)lpStruct->iSubItem);
	(*env)->SetIntField(env, lpObject, LVITEMFc.state, (jint)lpStruct->state);
	(*env)->SetIntField(env, lpObject, LVITEMFc.stateMask, (jint)lpStruct->stateMask);
	(*env)->SetIntField(env, lpObject, LVITEMFc.pszText, (jint)lpStruct->pszText);
	(*env)->SetIntField(env, lpObject, LVITEMFc.cchTextMax, (jint)lpStruct->cchTextMax);
	(*env)->SetIntField(env, lpObject, LVITEMFc.iImage, (jint)lpStruct->iImage);
	(*env)->SetIntField(env, lpObject, LVITEMFc.lParam, (jint)lpStruct->lParam);
	(*env)->SetIntField(env, lpObject, LVITEMFc.iIndent, (jint)lpStruct->iIndent);
}
#endif

#ifndef NO_MARGINS
typedef struct MARGINS_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cxLeftWidth, cxRightWidth, cyTopHeight, cyBottomHeight;
} MARGINS_FID_CACHE;

MARGINS_FID_CACHE MARGINSFc;

void cacheMARGINSFields(JNIEnv *env, jobject lpObject)
{
	if (MARGINSFc.cached) return;
	MARGINSFc.clazz = (*env)->GetObjectClass(env, lpObject);
	MARGINSFc.cxLeftWidth = (*env)->GetFieldID(env, MARGINSFc.clazz, "cxLeftWidth", "I");
	MARGINSFc.cxRightWidth = (*env)->GetFieldID(env, MARGINSFc.clazz, "cxRightWidth", "I");
	MARGINSFc.cyTopHeight = (*env)->GetFieldID(env, MARGINSFc.clazz, "cyTopHeight", "I");
	MARGINSFc.cyBottomHeight = (*env)->GetFieldID(env, MARGINSFc.clazz, "cyBottomHeight", "I");
	MARGINSFc.cached = 1;
}

MARGINS *getMARGINSFields(JNIEnv *env, jobject lpObject, MARGINS *lpStruct)
{
	if (!MARGINSFc.cached) cacheMARGINSFields(env, lpObject);
	lpStruct->cxLeftWidth = (*env)->GetIntField(env, lpObject, MARGINSFc.cxLeftWidth);
	lpStruct->cxRightWidth = (*env)->GetIntField(env, lpObject, MARGINSFc.cxRightWidth);
	lpStruct->cyTopHeight = (*env)->GetIntField(env, lpObject, MARGINSFc.cyTopHeight);
	lpStruct->cyBottomHeight = (*env)->GetIntField(env, lpObject, MARGINSFc.cyBottomHeight);
	return lpStruct;
}

void setMARGINSFields(JNIEnv *env, jobject lpObject, MARGINS *lpStruct)
{
	if (!MARGINSFc.cached) cacheMARGINSFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, MARGINSFc.cxLeftWidth, (jint)lpStruct->cxLeftWidth);
	(*env)->SetIntField(env, lpObject, MARGINSFc.cxRightWidth, (jint)lpStruct->cxRightWidth);
	(*env)->SetIntField(env, lpObject, MARGINSFc.cyTopHeight, (jint)lpStruct->cyTopHeight);
	(*env)->SetIntField(env, lpObject, MARGINSFc.cyBottomHeight, (jint)lpStruct->cyBottomHeight);
}
#endif

#ifndef NO_MEASUREITEMSTRUCT
typedef struct MEASUREITEMSTRUCT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID CtlType, CtlID, itemID, itemWidth, itemHeight, itemData;
} MEASUREITEMSTRUCT_FID_CACHE;

MEASUREITEMSTRUCT_FID_CACHE MEASUREITEMSTRUCTFc;

void cacheMEASUREITEMSTRUCTFields(JNIEnv *env, jobject lpObject)
{
	if (MEASUREITEMSTRUCTFc.cached) return;
	MEASUREITEMSTRUCTFc.clazz = (*env)->GetObjectClass(env, lpObject);
	MEASUREITEMSTRUCTFc.CtlType = (*env)->GetFieldID(env, MEASUREITEMSTRUCTFc.clazz, "CtlType", "I");
	MEASUREITEMSTRUCTFc.CtlID = (*env)->GetFieldID(env, MEASUREITEMSTRUCTFc.clazz, "CtlID", "I");
	MEASUREITEMSTRUCTFc.itemID = (*env)->GetFieldID(env, MEASUREITEMSTRUCTFc.clazz, "itemID", "I");
	MEASUREITEMSTRUCTFc.itemWidth = (*env)->GetFieldID(env, MEASUREITEMSTRUCTFc.clazz, "itemWidth", "I");
	MEASUREITEMSTRUCTFc.itemHeight = (*env)->GetFieldID(env, MEASUREITEMSTRUCTFc.clazz, "itemHeight", "I");
	MEASUREITEMSTRUCTFc.itemData = (*env)->GetFieldID(env, MEASUREITEMSTRUCTFc.clazz, "itemData", "I");
	MEASUREITEMSTRUCTFc.cached = 1;
}

MEASUREITEMSTRUCT *getMEASUREITEMSTRUCTFields(JNIEnv *env, jobject lpObject, MEASUREITEMSTRUCT *lpStruct)
{
	if (!MEASUREITEMSTRUCTFc.cached) cacheMEASUREITEMSTRUCTFields(env, lpObject);
	lpStruct->CtlType = (*env)->GetIntField(env, lpObject, MEASUREITEMSTRUCTFc.CtlType);
	lpStruct->CtlID = (*env)->GetIntField(env, lpObject, MEASUREITEMSTRUCTFc.CtlID);
	lpStruct->itemID = (*env)->GetIntField(env, lpObject, MEASUREITEMSTRUCTFc.itemID);
	lpStruct->itemWidth = (*env)->GetIntField(env, lpObject, MEASUREITEMSTRUCTFc.itemWidth);
	lpStruct->itemHeight = (*env)->GetIntField(env, lpObject, MEASUREITEMSTRUCTFc.itemHeight);
	lpStruct->itemData = (*env)->GetIntField(env, lpObject, MEASUREITEMSTRUCTFc.itemData);
	return lpStruct;
}

void setMEASUREITEMSTRUCTFields(JNIEnv *env, jobject lpObject, MEASUREITEMSTRUCT *lpStruct)
{
	if (!MEASUREITEMSTRUCTFc.cached) cacheMEASUREITEMSTRUCTFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, MEASUREITEMSTRUCTFc.CtlType, (jint)lpStruct->CtlType);
	(*env)->SetIntField(env, lpObject, MEASUREITEMSTRUCTFc.CtlID, (jint)lpStruct->CtlID);
	(*env)->SetIntField(env, lpObject, MEASUREITEMSTRUCTFc.itemID, (jint)lpStruct->itemID);
	(*env)->SetIntField(env, lpObject, MEASUREITEMSTRUCTFc.itemWidth, (jint)lpStruct->itemWidth);
	(*env)->SetIntField(env, lpObject, MEASUREITEMSTRUCTFc.itemHeight, (jint)lpStruct->itemHeight);
	(*env)->SetIntField(env, lpObject, MEASUREITEMSTRUCTFc.itemData, (jint)lpStruct->itemData);
}
#endif

#ifndef NO_MENUBARINFO
typedef struct MENUBARINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, left, top, right, bottom, hMenu, hwndMenu, fBarFocused, fFocused;
} MENUBARINFO_FID_CACHE;

MENUBARINFO_FID_CACHE MENUBARINFOFc;

void cacheMENUBARINFOFields(JNIEnv *env, jobject lpObject)
{
	if (MENUBARINFOFc.cached) return;
	MENUBARINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	MENUBARINFOFc.cbSize = (*env)->GetFieldID(env, MENUBARINFOFc.clazz, "cbSize", "I");
	MENUBARINFOFc.left = (*env)->GetFieldID(env, MENUBARINFOFc.clazz, "left", "I");
	MENUBARINFOFc.top = (*env)->GetFieldID(env, MENUBARINFOFc.clazz, "top", "I");
	MENUBARINFOFc.right = (*env)->GetFieldID(env, MENUBARINFOFc.clazz, "right", "I");
	MENUBARINFOFc.bottom = (*env)->GetFieldID(env, MENUBARINFOFc.clazz, "bottom", "I");
	MENUBARINFOFc.hMenu = (*env)->GetFieldID(env, MENUBARINFOFc.clazz, "hMenu", "I");
	MENUBARINFOFc.hwndMenu = (*env)->GetFieldID(env, MENUBARINFOFc.clazz, "hwndMenu", "I");
	MENUBARINFOFc.fBarFocused = (*env)->GetFieldID(env, MENUBARINFOFc.clazz, "fBarFocused", "Z");
	MENUBARINFOFc.fFocused = (*env)->GetFieldID(env, MENUBARINFOFc.clazz, "fFocused", "Z");
	MENUBARINFOFc.cached = 1;
}

MENUBARINFO *getMENUBARINFOFields(JNIEnv *env, jobject lpObject, MENUBARINFO *lpStruct)
{
	if (!MENUBARINFOFc.cached) cacheMENUBARINFOFields(env, lpObject);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, MENUBARINFOFc.cbSize);
	lpStruct->rcBar.left = (*env)->GetIntField(env, lpObject, MENUBARINFOFc.left);
	lpStruct->rcBar.top = (*env)->GetIntField(env, lpObject, MENUBARINFOFc.top);
	lpStruct->rcBar.right = (*env)->GetIntField(env, lpObject, MENUBARINFOFc.right);
	lpStruct->rcBar.bottom = (*env)->GetIntField(env, lpObject, MENUBARINFOFc.bottom);
	lpStruct->hMenu = (HMENU)(*env)->GetIntField(env, lpObject, MENUBARINFOFc.hMenu);
	lpStruct->hwndMenu = (HWND)(*env)->GetIntField(env, lpObject, MENUBARINFOFc.hwndMenu);
	lpStruct->fBarFocused = (*env)->GetBooleanField(env, lpObject, MENUBARINFOFc.fBarFocused);
	lpStruct->fFocused = (*env)->GetBooleanField(env, lpObject, MENUBARINFOFc.fFocused);
	return lpStruct;
}

void setMENUBARINFOFields(JNIEnv *env, jobject lpObject, MENUBARINFO *lpStruct)
{
	if (!MENUBARINFOFc.cached) cacheMENUBARINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, MENUBARINFOFc.cbSize, (jint)lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, MENUBARINFOFc.left, (jint)lpStruct->rcBar.left);
	(*env)->SetIntField(env, lpObject, MENUBARINFOFc.top, (jint)lpStruct->rcBar.top);
	(*env)->SetIntField(env, lpObject, MENUBARINFOFc.right, (jint)lpStruct->rcBar.right);
	(*env)->SetIntField(env, lpObject, MENUBARINFOFc.bottom, (jint)lpStruct->rcBar.bottom);
	(*env)->SetIntField(env, lpObject, MENUBARINFOFc.hMenu, (jint)lpStruct->hMenu);
	(*env)->SetIntField(env, lpObject, MENUBARINFOFc.hwndMenu, (jint)lpStruct->hwndMenu);
	(*env)->SetBooleanField(env, lpObject, MENUBARINFOFc.fBarFocused, (jboolean)lpStruct->fBarFocused);
	(*env)->SetBooleanField(env, lpObject, MENUBARINFOFc.fFocused, (jboolean)lpStruct->fFocused);
}
#endif

#ifndef NO_MENUINFO
typedef struct MENUINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, fMask, dwStyle, cyMax, hbrBack, dwContextHelpID, dwMenuData;
} MENUINFO_FID_CACHE;

MENUINFO_FID_CACHE MENUINFOFc;

void cacheMENUINFOFields(JNIEnv *env, jobject lpObject)
{
	if (MENUINFOFc.cached) return;
	MENUINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	MENUINFOFc.cbSize = (*env)->GetFieldID(env, MENUINFOFc.clazz, "cbSize", "I");
	MENUINFOFc.fMask = (*env)->GetFieldID(env, MENUINFOFc.clazz, "fMask", "I");
	MENUINFOFc.dwStyle = (*env)->GetFieldID(env, MENUINFOFc.clazz, "dwStyle", "I");
	MENUINFOFc.cyMax = (*env)->GetFieldID(env, MENUINFOFc.clazz, "cyMax", "I");
	MENUINFOFc.hbrBack = (*env)->GetFieldID(env, MENUINFOFc.clazz, "hbrBack", "I");
	MENUINFOFc.dwContextHelpID = (*env)->GetFieldID(env, MENUINFOFc.clazz, "dwContextHelpID", "I");
	MENUINFOFc.dwMenuData = (*env)->GetFieldID(env, MENUINFOFc.clazz, "dwMenuData", "I");
	MENUINFOFc.cached = 1;
}

MENUINFO *getMENUINFOFields(JNIEnv *env, jobject lpObject, MENUINFO *lpStruct)
{
	if (!MENUINFOFc.cached) cacheMENUINFOFields(env, lpObject);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, MENUINFOFc.cbSize);
	lpStruct->fMask = (*env)->GetIntField(env, lpObject, MENUINFOFc.fMask);
	lpStruct->dwStyle = (*env)->GetIntField(env, lpObject, MENUINFOFc.dwStyle);
	lpStruct->cyMax = (*env)->GetIntField(env, lpObject, MENUINFOFc.cyMax);
	lpStruct->hbrBack = (HBRUSH)(*env)->GetIntField(env, lpObject, MENUINFOFc.hbrBack);
	lpStruct->dwContextHelpID = (*env)->GetIntField(env, lpObject, MENUINFOFc.dwContextHelpID);
	lpStruct->dwMenuData = (*env)->GetIntField(env, lpObject, MENUINFOFc.dwMenuData);
	return lpStruct;
}

void setMENUINFOFields(JNIEnv *env, jobject lpObject, MENUINFO *lpStruct)
{
	if (!MENUINFOFc.cached) cacheMENUINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, MENUINFOFc.cbSize, (jint)lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, MENUINFOFc.fMask, (jint)lpStruct->fMask);
	(*env)->SetIntField(env, lpObject, MENUINFOFc.dwStyle, (jint)lpStruct->dwStyle);
	(*env)->SetIntField(env, lpObject, MENUINFOFc.cyMax, (jint)lpStruct->cyMax);
	(*env)->SetIntField(env, lpObject, MENUINFOFc.hbrBack, (jint)lpStruct->hbrBack);
	(*env)->SetIntField(env, lpObject, MENUINFOFc.dwContextHelpID, (jint)lpStruct->dwContextHelpID);
	(*env)->SetIntField(env, lpObject, MENUINFOFc.dwMenuData, (jint)lpStruct->dwMenuData);
}
#endif

#ifndef NO_MENUITEMINFO
typedef struct MENUITEMINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, fMask, fType, fState, wID, hSubMenu, hbmpChecked, hbmpUnchecked, dwItemData, dwTypeData, cch, hbmpItem;
} MENUITEMINFO_FID_CACHE;

MENUITEMINFO_FID_CACHE MENUITEMINFOFc;

void cacheMENUITEMINFOFields(JNIEnv *env, jobject lpObject)
{
	if (MENUITEMINFOFc.cached) return;
	MENUITEMINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	MENUITEMINFOFc.cbSize = (*env)->GetFieldID(env, MENUITEMINFOFc.clazz, "cbSize", "I");
	MENUITEMINFOFc.fMask = (*env)->GetFieldID(env, MENUITEMINFOFc.clazz, "fMask", "I");
	MENUITEMINFOFc.fType = (*env)->GetFieldID(env, MENUITEMINFOFc.clazz, "fType", "I");
	MENUITEMINFOFc.fState = (*env)->GetFieldID(env, MENUITEMINFOFc.clazz, "fState", "I");
	MENUITEMINFOFc.wID = (*env)->GetFieldID(env, MENUITEMINFOFc.clazz, "wID", "I");
	MENUITEMINFOFc.hSubMenu = (*env)->GetFieldID(env, MENUITEMINFOFc.clazz, "hSubMenu", "I");
	MENUITEMINFOFc.hbmpChecked = (*env)->GetFieldID(env, MENUITEMINFOFc.clazz, "hbmpChecked", "I");
	MENUITEMINFOFc.hbmpUnchecked = (*env)->GetFieldID(env, MENUITEMINFOFc.clazz, "hbmpUnchecked", "I");
	MENUITEMINFOFc.dwItemData = (*env)->GetFieldID(env, MENUITEMINFOFc.clazz, "dwItemData", "I");
	MENUITEMINFOFc.dwTypeData = (*env)->GetFieldID(env, MENUITEMINFOFc.clazz, "dwTypeData", "I");
	MENUITEMINFOFc.cch = (*env)->GetFieldID(env, MENUITEMINFOFc.clazz, "cch", "I");
	MENUITEMINFOFc.hbmpItem = (*env)->GetFieldID(env, MENUITEMINFOFc.clazz, "hbmpItem", "I");
	MENUITEMINFOFc.cached = 1;
}

MENUITEMINFO *getMENUITEMINFOFields(JNIEnv *env, jobject lpObject, MENUITEMINFO *lpStruct)
{
	if (!MENUITEMINFOFc.cached) cacheMENUITEMINFOFields(env, lpObject);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, MENUITEMINFOFc.cbSize);
	lpStruct->fMask = (*env)->GetIntField(env, lpObject, MENUITEMINFOFc.fMask);
	lpStruct->fType = (*env)->GetIntField(env, lpObject, MENUITEMINFOFc.fType);
	lpStruct->fState = (*env)->GetIntField(env, lpObject, MENUITEMINFOFc.fState);
	lpStruct->wID = (*env)->GetIntField(env, lpObject, MENUITEMINFOFc.wID);
	lpStruct->hSubMenu = (HMENU)(*env)->GetIntField(env, lpObject, MENUITEMINFOFc.hSubMenu);
	lpStruct->hbmpChecked = (HBITMAP)(*env)->GetIntField(env, lpObject, MENUITEMINFOFc.hbmpChecked);
	lpStruct->hbmpUnchecked = (HBITMAP)(*env)->GetIntField(env, lpObject, MENUITEMINFOFc.hbmpUnchecked);
	lpStruct->dwItemData = (*env)->GetIntField(env, lpObject, MENUITEMINFOFc.dwItemData);
	lpStruct->dwTypeData = (LPTSTR)(*env)->GetIntField(env, lpObject, MENUITEMINFOFc.dwTypeData);
	lpStruct->cch = (*env)->GetIntField(env, lpObject, MENUITEMINFOFc.cch);
#ifndef _WIN32_WCE
	lpStruct->hbmpItem = (HBITMAP)(*env)->GetIntField(env, lpObject, MENUITEMINFOFc.hbmpItem);
#endif
	return lpStruct;
}

void setMENUITEMINFOFields(JNIEnv *env, jobject lpObject, MENUITEMINFO *lpStruct)
{
	if (!MENUITEMINFOFc.cached) cacheMENUITEMINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, MENUITEMINFOFc.cbSize, (jint)lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, MENUITEMINFOFc.fMask, (jint)lpStruct->fMask);
	(*env)->SetIntField(env, lpObject, MENUITEMINFOFc.fType, (jint)lpStruct->fType);
	(*env)->SetIntField(env, lpObject, MENUITEMINFOFc.fState, (jint)lpStruct->fState);
	(*env)->SetIntField(env, lpObject, MENUITEMINFOFc.wID, (jint)lpStruct->wID);
	(*env)->SetIntField(env, lpObject, MENUITEMINFOFc.hSubMenu, (jint)lpStruct->hSubMenu);
	(*env)->SetIntField(env, lpObject, MENUITEMINFOFc.hbmpChecked, (jint)lpStruct->hbmpChecked);
	(*env)->SetIntField(env, lpObject, MENUITEMINFOFc.hbmpUnchecked, (jint)lpStruct->hbmpUnchecked);
	(*env)->SetIntField(env, lpObject, MENUITEMINFOFc.dwItemData, (jint)lpStruct->dwItemData);
	(*env)->SetIntField(env, lpObject, MENUITEMINFOFc.dwTypeData, (jint)lpStruct->dwTypeData);
	(*env)->SetIntField(env, lpObject, MENUITEMINFOFc.cch, (jint)lpStruct->cch);
#ifndef _WIN32_WCE
	(*env)->SetIntField(env, lpObject, MENUITEMINFOFc.hbmpItem, (jint)lpStruct->hbmpItem);
#endif
}
#endif

#ifndef NO_MINMAXINFO
typedef struct MINMAXINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID ptReserved_x, ptReserved_y, ptMaxSize_x, ptMaxSize_y, ptMaxPosition_x, ptMaxPosition_y, ptMinTrackSize_x, ptMinTrackSize_y, ptMaxTrackSize_x, ptMaxTrackSize_y;
} MINMAXINFO_FID_CACHE;

MINMAXINFO_FID_CACHE MINMAXINFOFc;

void cacheMINMAXINFOFields(JNIEnv *env, jobject lpObject)
{
	if (MINMAXINFOFc.cached) return;
	MINMAXINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	MINMAXINFOFc.ptReserved_x = (*env)->GetFieldID(env, MINMAXINFOFc.clazz, "ptReserved_x", "I");
	MINMAXINFOFc.ptReserved_y = (*env)->GetFieldID(env, MINMAXINFOFc.clazz, "ptReserved_y", "I");
	MINMAXINFOFc.ptMaxSize_x = (*env)->GetFieldID(env, MINMAXINFOFc.clazz, "ptMaxSize_x", "I");
	MINMAXINFOFc.ptMaxSize_y = (*env)->GetFieldID(env, MINMAXINFOFc.clazz, "ptMaxSize_y", "I");
	MINMAXINFOFc.ptMaxPosition_x = (*env)->GetFieldID(env, MINMAXINFOFc.clazz, "ptMaxPosition_x", "I");
	MINMAXINFOFc.ptMaxPosition_y = (*env)->GetFieldID(env, MINMAXINFOFc.clazz, "ptMaxPosition_y", "I");
	MINMAXINFOFc.ptMinTrackSize_x = (*env)->GetFieldID(env, MINMAXINFOFc.clazz, "ptMinTrackSize_x", "I");
	MINMAXINFOFc.ptMinTrackSize_y = (*env)->GetFieldID(env, MINMAXINFOFc.clazz, "ptMinTrackSize_y", "I");
	MINMAXINFOFc.ptMaxTrackSize_x = (*env)->GetFieldID(env, MINMAXINFOFc.clazz, "ptMaxTrackSize_x", "I");
	MINMAXINFOFc.ptMaxTrackSize_y = (*env)->GetFieldID(env, MINMAXINFOFc.clazz, "ptMaxTrackSize_y", "I");
	MINMAXINFOFc.cached = 1;
}

MINMAXINFO *getMINMAXINFOFields(JNIEnv *env, jobject lpObject, MINMAXINFO *lpStruct)
{
	if (!MINMAXINFOFc.cached) cacheMINMAXINFOFields(env, lpObject);
	lpStruct->ptReserved.x = (*env)->GetIntField(env, lpObject, MINMAXINFOFc.ptReserved_x);
	lpStruct->ptReserved.y = (*env)->GetIntField(env, lpObject, MINMAXINFOFc.ptReserved_y);
	lpStruct->ptMaxSize.x = (*env)->GetIntField(env, lpObject, MINMAXINFOFc.ptMaxSize_x);
	lpStruct->ptMaxSize.y = (*env)->GetIntField(env, lpObject, MINMAXINFOFc.ptMaxSize_y);
	lpStruct->ptMaxPosition.x = (*env)->GetIntField(env, lpObject, MINMAXINFOFc.ptMaxPosition_x);
	lpStruct->ptMaxPosition.y = (*env)->GetIntField(env, lpObject, MINMAXINFOFc.ptMaxPosition_y);
	lpStruct->ptMinTrackSize.x = (*env)->GetIntField(env, lpObject, MINMAXINFOFc.ptMinTrackSize_x);
	lpStruct->ptMinTrackSize.y = (*env)->GetIntField(env, lpObject, MINMAXINFOFc.ptMinTrackSize_y);
	lpStruct->ptMaxTrackSize.x = (*env)->GetIntField(env, lpObject, MINMAXINFOFc.ptMaxTrackSize_x);
	lpStruct->ptMaxTrackSize.y = (*env)->GetIntField(env, lpObject, MINMAXINFOFc.ptMaxTrackSize_y);
	return lpStruct;
}

void setMINMAXINFOFields(JNIEnv *env, jobject lpObject, MINMAXINFO *lpStruct)
{
	if (!MINMAXINFOFc.cached) cacheMINMAXINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, MINMAXINFOFc.ptReserved_x, (jint)lpStruct->ptReserved.x);
	(*env)->SetIntField(env, lpObject, MINMAXINFOFc.ptReserved_y, (jint)lpStruct->ptReserved.y);
	(*env)->SetIntField(env, lpObject, MINMAXINFOFc.ptMaxSize_x, (jint)lpStruct->ptMaxSize.x);
	(*env)->SetIntField(env, lpObject, MINMAXINFOFc.ptMaxSize_y, (jint)lpStruct->ptMaxSize.y);
	(*env)->SetIntField(env, lpObject, MINMAXINFOFc.ptMaxPosition_x, (jint)lpStruct->ptMaxPosition.x);
	(*env)->SetIntField(env, lpObject, MINMAXINFOFc.ptMaxPosition_y, (jint)lpStruct->ptMaxPosition.y);
	(*env)->SetIntField(env, lpObject, MINMAXINFOFc.ptMinTrackSize_x, (jint)lpStruct->ptMinTrackSize.x);
	(*env)->SetIntField(env, lpObject, MINMAXINFOFc.ptMinTrackSize_y, (jint)lpStruct->ptMinTrackSize.y);
	(*env)->SetIntField(env, lpObject, MINMAXINFOFc.ptMaxTrackSize_x, (jint)lpStruct->ptMaxTrackSize.x);
	(*env)->SetIntField(env, lpObject, MINMAXINFOFc.ptMaxTrackSize_y, (jint)lpStruct->ptMaxTrackSize.y);
}
#endif

#ifndef NO_MONITORINFO
typedef struct MONITORINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, rcMonitor_left, rcMonitor_top, rcMonitor_right, rcMonitor_bottom, rcWork_left, rcWork_top, rcWork_right, rcWork_bottom, dwFlags;
} MONITORINFO_FID_CACHE;

MONITORINFO_FID_CACHE MONITORINFOFc;

void cacheMONITORINFOFields(JNIEnv *env, jobject lpObject)
{
	if (MONITORINFOFc.cached) return;
	MONITORINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	MONITORINFOFc.cbSize = (*env)->GetFieldID(env, MONITORINFOFc.clazz, "cbSize", "I");
	MONITORINFOFc.rcMonitor_left = (*env)->GetFieldID(env, MONITORINFOFc.clazz, "rcMonitor_left", "I");
	MONITORINFOFc.rcMonitor_top = (*env)->GetFieldID(env, MONITORINFOFc.clazz, "rcMonitor_top", "I");
	MONITORINFOFc.rcMonitor_right = (*env)->GetFieldID(env, MONITORINFOFc.clazz, "rcMonitor_right", "I");
	MONITORINFOFc.rcMonitor_bottom = (*env)->GetFieldID(env, MONITORINFOFc.clazz, "rcMonitor_bottom", "I");
	MONITORINFOFc.rcWork_left = (*env)->GetFieldID(env, MONITORINFOFc.clazz, "rcWork_left", "I");
	MONITORINFOFc.rcWork_top = (*env)->GetFieldID(env, MONITORINFOFc.clazz, "rcWork_top", "I");
	MONITORINFOFc.rcWork_right = (*env)->GetFieldID(env, MONITORINFOFc.clazz, "rcWork_right", "I");
	MONITORINFOFc.rcWork_bottom = (*env)->GetFieldID(env, MONITORINFOFc.clazz, "rcWork_bottom", "I");
	MONITORINFOFc.dwFlags = (*env)->GetFieldID(env, MONITORINFOFc.clazz, "dwFlags", "I");
	MONITORINFOFc.cached = 1;
}

MONITORINFO *getMONITORINFOFields(JNIEnv *env, jobject lpObject, MONITORINFO *lpStruct)
{
	if (!MONITORINFOFc.cached) cacheMONITORINFOFields(env, lpObject);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, MONITORINFOFc.cbSize);
	lpStruct->rcMonitor.left = (*env)->GetIntField(env, lpObject, MONITORINFOFc.rcMonitor_left);
	lpStruct->rcMonitor.top = (*env)->GetIntField(env, lpObject, MONITORINFOFc.rcMonitor_top);
	lpStruct->rcMonitor.right = (*env)->GetIntField(env, lpObject, MONITORINFOFc.rcMonitor_right);
	lpStruct->rcMonitor.bottom = (*env)->GetIntField(env, lpObject, MONITORINFOFc.rcMonitor_bottom);
	lpStruct->rcWork.left = (*env)->GetIntField(env, lpObject, MONITORINFOFc.rcWork_left);
	lpStruct->rcWork.top = (*env)->GetIntField(env, lpObject, MONITORINFOFc.rcWork_top);
	lpStruct->rcWork.right = (*env)->GetIntField(env, lpObject, MONITORINFOFc.rcWork_right);
	lpStruct->rcWork.bottom = (*env)->GetIntField(env, lpObject, MONITORINFOFc.rcWork_bottom);
	lpStruct->dwFlags = (*env)->GetIntField(env, lpObject, MONITORINFOFc.dwFlags);
	return lpStruct;
}

void setMONITORINFOFields(JNIEnv *env, jobject lpObject, MONITORINFO *lpStruct)
{
	if (!MONITORINFOFc.cached) cacheMONITORINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, MONITORINFOFc.cbSize, (jint)lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, MONITORINFOFc.rcMonitor_left, (jint)lpStruct->rcMonitor.left);
	(*env)->SetIntField(env, lpObject, MONITORINFOFc.rcMonitor_top, (jint)lpStruct->rcMonitor.top);
	(*env)->SetIntField(env, lpObject, MONITORINFOFc.rcMonitor_right, (jint)lpStruct->rcMonitor.right);
	(*env)->SetIntField(env, lpObject, MONITORINFOFc.rcMonitor_bottom, (jint)lpStruct->rcMonitor.bottom);
	(*env)->SetIntField(env, lpObject, MONITORINFOFc.rcWork_left, (jint)lpStruct->rcWork.left);
	(*env)->SetIntField(env, lpObject, MONITORINFOFc.rcWork_top, (jint)lpStruct->rcWork.top);
	(*env)->SetIntField(env, lpObject, MONITORINFOFc.rcWork_right, (jint)lpStruct->rcWork.right);
	(*env)->SetIntField(env, lpObject, MONITORINFOFc.rcWork_bottom, (jint)lpStruct->rcWork.bottom);
	(*env)->SetIntField(env, lpObject, MONITORINFOFc.dwFlags, (jint)lpStruct->dwFlags);
}
#endif

#ifndef NO_MOUSEINPUT
typedef struct MOUSEINPUT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID dx, dy, mouseData, dwFlags, time, dwExtraInfo;
} MOUSEINPUT_FID_CACHE;

MOUSEINPUT_FID_CACHE MOUSEINPUTFc;

void cacheMOUSEINPUTFields(JNIEnv *env, jobject lpObject)
{
	if (MOUSEINPUTFc.cached) return;
	MOUSEINPUTFc.clazz = (*env)->GetObjectClass(env, lpObject);
	MOUSEINPUTFc.dx = (*env)->GetFieldID(env, MOUSEINPUTFc.clazz, "dx", "I");
	MOUSEINPUTFc.dy = (*env)->GetFieldID(env, MOUSEINPUTFc.clazz, "dy", "I");
	MOUSEINPUTFc.mouseData = (*env)->GetFieldID(env, MOUSEINPUTFc.clazz, "mouseData", "I");
	MOUSEINPUTFc.dwFlags = (*env)->GetFieldID(env, MOUSEINPUTFc.clazz, "dwFlags", "I");
	MOUSEINPUTFc.time = (*env)->GetFieldID(env, MOUSEINPUTFc.clazz, "time", "I");
	MOUSEINPUTFc.dwExtraInfo = (*env)->GetFieldID(env, MOUSEINPUTFc.clazz, "dwExtraInfo", "I");
	MOUSEINPUTFc.cached = 1;
}

MOUSEINPUT *getMOUSEINPUTFields(JNIEnv *env, jobject lpObject, MOUSEINPUT *lpStruct)
{
	if (!MOUSEINPUTFc.cached) cacheMOUSEINPUTFields(env, lpObject);
	lpStruct->dx = (*env)->GetIntField(env, lpObject, MOUSEINPUTFc.dx);
	lpStruct->dy = (*env)->GetIntField(env, lpObject, MOUSEINPUTFc.dy);
	lpStruct->mouseData = (*env)->GetIntField(env, lpObject, MOUSEINPUTFc.mouseData);
	lpStruct->dwFlags = (*env)->GetIntField(env, lpObject, MOUSEINPUTFc.dwFlags);
	lpStruct->time = (*env)->GetIntField(env, lpObject, MOUSEINPUTFc.time);
	lpStruct->dwExtraInfo = (*env)->GetIntField(env, lpObject, MOUSEINPUTFc.dwExtraInfo);
	return lpStruct;
}

void setMOUSEINPUTFields(JNIEnv *env, jobject lpObject, MOUSEINPUT *lpStruct)
{
	if (!MOUSEINPUTFc.cached) cacheMOUSEINPUTFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, MOUSEINPUTFc.dx, (jint)lpStruct->dx);
	(*env)->SetIntField(env, lpObject, MOUSEINPUTFc.dy, (jint)lpStruct->dy);
	(*env)->SetIntField(env, lpObject, MOUSEINPUTFc.mouseData, (jint)lpStruct->mouseData);
	(*env)->SetIntField(env, lpObject, MOUSEINPUTFc.dwFlags, (jint)lpStruct->dwFlags);
	(*env)->SetIntField(env, lpObject, MOUSEINPUTFc.time, (jint)lpStruct->time);
	(*env)->SetIntField(env, lpObject, MOUSEINPUTFc.dwExtraInfo, (jint)lpStruct->dwExtraInfo);
}
#endif

#ifndef NO_MSG
typedef struct MSG_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID hwnd, message, wParam, lParam, time, x, y;
} MSG_FID_CACHE;

MSG_FID_CACHE MSGFc;

void cacheMSGFields(JNIEnv *env, jobject lpObject)
{
	if (MSGFc.cached) return;
	MSGFc.clazz = (*env)->GetObjectClass(env, lpObject);
	MSGFc.hwnd = (*env)->GetFieldID(env, MSGFc.clazz, "hwnd", "I");
	MSGFc.message = (*env)->GetFieldID(env, MSGFc.clazz, "message", "I");
	MSGFc.wParam = (*env)->GetFieldID(env, MSGFc.clazz, "wParam", "I");
	MSGFc.lParam = (*env)->GetFieldID(env, MSGFc.clazz, "lParam", "I");
	MSGFc.time = (*env)->GetFieldID(env, MSGFc.clazz, "time", "I");
	MSGFc.x = (*env)->GetFieldID(env, MSGFc.clazz, "x", "I");
	MSGFc.y = (*env)->GetFieldID(env, MSGFc.clazz, "y", "I");
	MSGFc.cached = 1;
}

MSG *getMSGFields(JNIEnv *env, jobject lpObject, MSG *lpStruct)
{
	if (!MSGFc.cached) cacheMSGFields(env, lpObject);
	lpStruct->hwnd = (HWND)(*env)->GetIntField(env, lpObject, MSGFc.hwnd);
	lpStruct->message = (*env)->GetIntField(env, lpObject, MSGFc.message);
	lpStruct->wParam = (*env)->GetIntField(env, lpObject, MSGFc.wParam);
	lpStruct->lParam = (*env)->GetIntField(env, lpObject, MSGFc.lParam);
	lpStruct->time = (*env)->GetIntField(env, lpObject, MSGFc.time);
	lpStruct->pt.x = (*env)->GetIntField(env, lpObject, MSGFc.x);
	lpStruct->pt.y = (*env)->GetIntField(env, lpObject, MSGFc.y);
	return lpStruct;
}

void setMSGFields(JNIEnv *env, jobject lpObject, MSG *lpStruct)
{
	if (!MSGFc.cached) cacheMSGFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, MSGFc.hwnd, (jint)lpStruct->hwnd);
	(*env)->SetIntField(env, lpObject, MSGFc.message, (jint)lpStruct->message);
	(*env)->SetIntField(env, lpObject, MSGFc.wParam, (jint)lpStruct->wParam);
	(*env)->SetIntField(env, lpObject, MSGFc.lParam, (jint)lpStruct->lParam);
	(*env)->SetIntField(env, lpObject, MSGFc.time, (jint)lpStruct->time);
	(*env)->SetIntField(env, lpObject, MSGFc.x, (jint)lpStruct->pt.x);
	(*env)->SetIntField(env, lpObject, MSGFc.y, (jint)lpStruct->pt.y);
}
#endif

#ifndef NO_NMCUSTOMDRAW
typedef struct NMCUSTOMDRAW_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID dwDrawStage, hdc, left, top, right, bottom, dwItemSpec, uItemState, lItemlParam;
} NMCUSTOMDRAW_FID_CACHE;

NMCUSTOMDRAW_FID_CACHE NMCUSTOMDRAWFc;

void cacheNMCUSTOMDRAWFields(JNIEnv *env, jobject lpObject)
{
	if (NMCUSTOMDRAWFc.cached) return;
	cacheNMHDRFields(env, lpObject);
	NMCUSTOMDRAWFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NMCUSTOMDRAWFc.dwDrawStage = (*env)->GetFieldID(env, NMCUSTOMDRAWFc.clazz, "dwDrawStage", "I");
	NMCUSTOMDRAWFc.hdc = (*env)->GetFieldID(env, NMCUSTOMDRAWFc.clazz, "hdc", "I");
	NMCUSTOMDRAWFc.left = (*env)->GetFieldID(env, NMCUSTOMDRAWFc.clazz, "left", "I");
	NMCUSTOMDRAWFc.top = (*env)->GetFieldID(env, NMCUSTOMDRAWFc.clazz, "top", "I");
	NMCUSTOMDRAWFc.right = (*env)->GetFieldID(env, NMCUSTOMDRAWFc.clazz, "right", "I");
	NMCUSTOMDRAWFc.bottom = (*env)->GetFieldID(env, NMCUSTOMDRAWFc.clazz, "bottom", "I");
	NMCUSTOMDRAWFc.dwItemSpec = (*env)->GetFieldID(env, NMCUSTOMDRAWFc.clazz, "dwItemSpec", "I");
	NMCUSTOMDRAWFc.uItemState = (*env)->GetFieldID(env, NMCUSTOMDRAWFc.clazz, "uItemState", "I");
	NMCUSTOMDRAWFc.lItemlParam = (*env)->GetFieldID(env, NMCUSTOMDRAWFc.clazz, "lItemlParam", "I");
	NMCUSTOMDRAWFc.cached = 1;
}

NMCUSTOMDRAW *getNMCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMCUSTOMDRAW *lpStruct)
{
	if (!NMCUSTOMDRAWFc.cached) cacheNMCUSTOMDRAWFields(env, lpObject);
	getNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	lpStruct->dwDrawStage = (*env)->GetIntField(env, lpObject, NMCUSTOMDRAWFc.dwDrawStage);
	lpStruct->hdc = (HDC)(*env)->GetIntField(env, lpObject, NMCUSTOMDRAWFc.hdc);
	lpStruct->rc.left = (*env)->GetIntField(env, lpObject, NMCUSTOMDRAWFc.left);
	lpStruct->rc.top = (*env)->GetIntField(env, lpObject, NMCUSTOMDRAWFc.top);
	lpStruct->rc.right = (*env)->GetIntField(env, lpObject, NMCUSTOMDRAWFc.right);
	lpStruct->rc.bottom = (*env)->GetIntField(env, lpObject, NMCUSTOMDRAWFc.bottom);
	lpStruct->dwItemSpec = (*env)->GetIntField(env, lpObject, NMCUSTOMDRAWFc.dwItemSpec);
	lpStruct->uItemState = (*env)->GetIntField(env, lpObject, NMCUSTOMDRAWFc.uItemState);
	lpStruct->lItemlParam = (*env)->GetIntField(env, lpObject, NMCUSTOMDRAWFc.lItemlParam);
	return lpStruct;
}

void setNMCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMCUSTOMDRAW *lpStruct)
{
	if (!NMCUSTOMDRAWFc.cached) cacheNMCUSTOMDRAWFields(env, lpObject);
	setNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	(*env)->SetIntField(env, lpObject, NMCUSTOMDRAWFc.dwDrawStage, (jint)lpStruct->dwDrawStage);
	(*env)->SetIntField(env, lpObject, NMCUSTOMDRAWFc.hdc, (jint)lpStruct->hdc);
	(*env)->SetIntField(env, lpObject, NMCUSTOMDRAWFc.left, (jint)lpStruct->rc.left);
	(*env)->SetIntField(env, lpObject, NMCUSTOMDRAWFc.top, (jint)lpStruct->rc.top);
	(*env)->SetIntField(env, lpObject, NMCUSTOMDRAWFc.right, (jint)lpStruct->rc.right);
	(*env)->SetIntField(env, lpObject, NMCUSTOMDRAWFc.bottom, (jint)lpStruct->rc.bottom);
	(*env)->SetIntField(env, lpObject, NMCUSTOMDRAWFc.dwItemSpec, (jint)lpStruct->dwItemSpec);
	(*env)->SetIntField(env, lpObject, NMCUSTOMDRAWFc.uItemState, (jint)lpStruct->uItemState);
	(*env)->SetIntField(env, lpObject, NMCUSTOMDRAWFc.lItemlParam, (jint)lpStruct->lItemlParam);
}
#endif

#ifndef NO_NMHDR
typedef struct NMHDR_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID hwndFrom, idFrom, code;
} NMHDR_FID_CACHE;

NMHDR_FID_CACHE NMHDRFc;

void cacheNMHDRFields(JNIEnv *env, jobject lpObject)
{
	if (NMHDRFc.cached) return;
	NMHDRFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NMHDRFc.hwndFrom = (*env)->GetFieldID(env, NMHDRFc.clazz, "hwndFrom", "I");
	NMHDRFc.idFrom = (*env)->GetFieldID(env, NMHDRFc.clazz, "idFrom", "I");
	NMHDRFc.code = (*env)->GetFieldID(env, NMHDRFc.clazz, "code", "I");
	NMHDRFc.cached = 1;
}

NMHDR *getNMHDRFields(JNIEnv *env, jobject lpObject, NMHDR *lpStruct)
{
	if (!NMHDRFc.cached) cacheNMHDRFields(env, lpObject);
	lpStruct->hwndFrom = (HWND)(*env)->GetIntField(env, lpObject, NMHDRFc.hwndFrom);
	lpStruct->idFrom = (*env)->GetIntField(env, lpObject, NMHDRFc.idFrom);
	lpStruct->code = (*env)->GetIntField(env, lpObject, NMHDRFc.code);
	return lpStruct;
}

void setNMHDRFields(JNIEnv *env, jobject lpObject, NMHDR *lpStruct)
{
	if (!NMHDRFc.cached) cacheNMHDRFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, NMHDRFc.hwndFrom, (jint)lpStruct->hwndFrom);
	(*env)->SetIntField(env, lpObject, NMHDRFc.idFrom, (jint)lpStruct->idFrom);
	(*env)->SetIntField(env, lpObject, NMHDRFc.code, (jint)lpStruct->code);
}
#endif

#ifndef NO_NMHEADER
typedef struct NMHEADER_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID iItem, iButton, pitem;
} NMHEADER_FID_CACHE;

NMHEADER_FID_CACHE NMHEADERFc;

void cacheNMHEADERFields(JNIEnv *env, jobject lpObject)
{
	if (NMHEADERFc.cached) return;
	cacheNMHDRFields(env, lpObject);
	NMHEADERFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NMHEADERFc.iItem = (*env)->GetFieldID(env, NMHEADERFc.clazz, "iItem", "I");
	NMHEADERFc.iButton = (*env)->GetFieldID(env, NMHEADERFc.clazz, "iButton", "I");
	NMHEADERFc.pitem = (*env)->GetFieldID(env, NMHEADERFc.clazz, "pitem", "I");
	NMHEADERFc.cached = 1;
}

NMHEADER *getNMHEADERFields(JNIEnv *env, jobject lpObject, NMHEADER *lpStruct)
{
	if (!NMHEADERFc.cached) cacheNMHEADERFields(env, lpObject);
	getNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	lpStruct->iItem = (*env)->GetIntField(env, lpObject, NMHEADERFc.iItem);
	lpStruct->iButton = (*env)->GetIntField(env, lpObject, NMHEADERFc.iButton);
	lpStruct->pitem = (HDITEM FAR *)(*env)->GetIntField(env, lpObject, NMHEADERFc.pitem);
	return lpStruct;
}

void setNMHEADERFields(JNIEnv *env, jobject lpObject, NMHEADER *lpStruct)
{
	if (!NMHEADERFc.cached) cacheNMHEADERFields(env, lpObject);
	setNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	(*env)->SetIntField(env, lpObject, NMHEADERFc.iItem, (jint)lpStruct->iItem);
	(*env)->SetIntField(env, lpObject, NMHEADERFc.iButton, (jint)lpStruct->iButton);
	(*env)->SetIntField(env, lpObject, NMHEADERFc.pitem, (jint)lpStruct->pitem);
}
#endif

#ifndef NO_NMLINK
typedef struct NMLINK_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID mask, iLink, state, stateMask, szID, szUrl;
} NMLINK_FID_CACHE;

NMLINK_FID_CACHE NMLINKFc;

void cacheNMLINKFields(JNIEnv *env, jobject lpObject)
{
	if (NMLINKFc.cached) return;
	cacheNMHDRFields(env, lpObject);
	NMLINKFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NMLINKFc.mask = (*env)->GetFieldID(env, NMLINKFc.clazz, "mask", "I");
	NMLINKFc.iLink = (*env)->GetFieldID(env, NMLINKFc.clazz, "iLink", "I");
	NMLINKFc.state = (*env)->GetFieldID(env, NMLINKFc.clazz, "state", "I");
	NMLINKFc.stateMask = (*env)->GetFieldID(env, NMLINKFc.clazz, "stateMask", "I");
	NMLINKFc.szID = (*env)->GetFieldID(env, NMLINKFc.clazz, "szID", "[C");
	NMLINKFc.szUrl = (*env)->GetFieldID(env, NMLINKFc.clazz, "szUrl", "[C");
	NMLINKFc.cached = 1;
}

NMLINK *getNMLINKFields(JNIEnv *env, jobject lpObject, NMLINK *lpStruct)
{
	if (!NMLINKFc.cached) cacheNMLINKFields(env, lpObject);
	getNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	lpStruct->item.mask = (*env)->GetIntField(env, lpObject, NMLINKFc.mask);
	lpStruct->item.iLink = (*env)->GetIntField(env, lpObject, NMLINKFc.iLink);
	lpStruct->item.state = (*env)->GetIntField(env, lpObject, NMLINKFc.state);
	lpStruct->item.stateMask = (*env)->GetIntField(env, lpObject, NMLINKFc.stateMask);
	{
	jcharArray lpObject1 = (jcharArray)(*env)->GetObjectField(env, lpObject, NMLINKFc.szID);
	(*env)->GetCharArrayRegion(env, lpObject1, 0, sizeof(lpStruct->item.szID) / 2, (jchar *)lpStruct->item.szID);
	}
	{
	jcharArray lpObject1 = (jcharArray)(*env)->GetObjectField(env, lpObject, NMLINKFc.szUrl);
	(*env)->GetCharArrayRegion(env, lpObject1, 0, sizeof(lpStruct->item.szUrl) / 2, (jchar *)lpStruct->item.szUrl);
	}
	return lpStruct;
}

void setNMLINKFields(JNIEnv *env, jobject lpObject, NMLINK *lpStruct)
{
	if (!NMLINKFc.cached) cacheNMLINKFields(env, lpObject);
	setNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	(*env)->SetIntField(env, lpObject, NMLINKFc.mask, (jint)lpStruct->item.mask);
	(*env)->SetIntField(env, lpObject, NMLINKFc.iLink, (jint)lpStruct->item.iLink);
	(*env)->SetIntField(env, lpObject, NMLINKFc.state, (jint)lpStruct->item.state);
	(*env)->SetIntField(env, lpObject, NMLINKFc.stateMask, (jint)lpStruct->item.stateMask);
	{
	jcharArray lpObject1 = (jcharArray)(*env)->GetObjectField(env, lpObject, NMLINKFc.szID);
	(*env)->SetCharArrayRegion(env, lpObject1, 0, sizeof(lpStruct->item.szID) / 2, (jchar *)lpStruct->item.szID);
	}
	{
	jcharArray lpObject1 = (jcharArray)(*env)->GetObjectField(env, lpObject, NMLINKFc.szUrl);
	(*env)->SetCharArrayRegion(env, lpObject1, 0, sizeof(lpStruct->item.szUrl) / 2, (jchar *)lpStruct->item.szUrl);
	}
}
#endif

#ifndef NO_NMLISTVIEW
typedef struct NMLISTVIEW_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID iItem, iSubItem, uNewState, uOldState, uChanged, x, y, lParam;
} NMLISTVIEW_FID_CACHE;

NMLISTVIEW_FID_CACHE NMLISTVIEWFc;

void cacheNMLISTVIEWFields(JNIEnv *env, jobject lpObject)
{
	if (NMLISTVIEWFc.cached) return;
	cacheNMHDRFields(env, lpObject);
	NMLISTVIEWFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NMLISTVIEWFc.iItem = (*env)->GetFieldID(env, NMLISTVIEWFc.clazz, "iItem", "I");
	NMLISTVIEWFc.iSubItem = (*env)->GetFieldID(env, NMLISTVIEWFc.clazz, "iSubItem", "I");
	NMLISTVIEWFc.uNewState = (*env)->GetFieldID(env, NMLISTVIEWFc.clazz, "uNewState", "I");
	NMLISTVIEWFc.uOldState = (*env)->GetFieldID(env, NMLISTVIEWFc.clazz, "uOldState", "I");
	NMLISTVIEWFc.uChanged = (*env)->GetFieldID(env, NMLISTVIEWFc.clazz, "uChanged", "I");
	NMLISTVIEWFc.x = (*env)->GetFieldID(env, NMLISTVIEWFc.clazz, "x", "I");
	NMLISTVIEWFc.y = (*env)->GetFieldID(env, NMLISTVIEWFc.clazz, "y", "I");
	NMLISTVIEWFc.lParam = (*env)->GetFieldID(env, NMLISTVIEWFc.clazz, "lParam", "I");
	NMLISTVIEWFc.cached = 1;
}

NMLISTVIEW *getNMLISTVIEWFields(JNIEnv *env, jobject lpObject, NMLISTVIEW *lpStruct)
{
	if (!NMLISTVIEWFc.cached) cacheNMLISTVIEWFields(env, lpObject);
	getNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	lpStruct->iItem = (*env)->GetIntField(env, lpObject, NMLISTVIEWFc.iItem);
	lpStruct->iSubItem = (*env)->GetIntField(env, lpObject, NMLISTVIEWFc.iSubItem);
	lpStruct->uNewState = (*env)->GetIntField(env, lpObject, NMLISTVIEWFc.uNewState);
	lpStruct->uOldState = (*env)->GetIntField(env, lpObject, NMLISTVIEWFc.uOldState);
	lpStruct->uChanged = (*env)->GetIntField(env, lpObject, NMLISTVIEWFc.uChanged);
	lpStruct->ptAction.x = (*env)->GetIntField(env, lpObject, NMLISTVIEWFc.x);
	lpStruct->ptAction.y = (*env)->GetIntField(env, lpObject, NMLISTVIEWFc.y);
	lpStruct->lParam = (*env)->GetIntField(env, lpObject, NMLISTVIEWFc.lParam);
	return lpStruct;
}

void setNMLISTVIEWFields(JNIEnv *env, jobject lpObject, NMLISTVIEW *lpStruct)
{
	if (!NMLISTVIEWFc.cached) cacheNMLISTVIEWFields(env, lpObject);
	setNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	(*env)->SetIntField(env, lpObject, NMLISTVIEWFc.iItem, (jint)lpStruct->iItem);
	(*env)->SetIntField(env, lpObject, NMLISTVIEWFc.iSubItem, (jint)lpStruct->iSubItem);
	(*env)->SetIntField(env, lpObject, NMLISTVIEWFc.uNewState, (jint)lpStruct->uNewState);
	(*env)->SetIntField(env, lpObject, NMLISTVIEWFc.uOldState, (jint)lpStruct->uOldState);
	(*env)->SetIntField(env, lpObject, NMLISTVIEWFc.uChanged, (jint)lpStruct->uChanged);
	(*env)->SetIntField(env, lpObject, NMLISTVIEWFc.x, (jint)lpStruct->ptAction.x);
	(*env)->SetIntField(env, lpObject, NMLISTVIEWFc.y, (jint)lpStruct->ptAction.y);
	(*env)->SetIntField(env, lpObject, NMLISTVIEWFc.lParam, (jint)lpStruct->lParam);
}
#endif

#ifndef NO_NMLVCUSTOMDRAW
typedef struct NMLVCUSTOMDRAW_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID clrText, clrTextBk, iSubItem;
} NMLVCUSTOMDRAW_FID_CACHE;

NMLVCUSTOMDRAW_FID_CACHE NMLVCUSTOMDRAWFc;

void cacheNMLVCUSTOMDRAWFields(JNIEnv *env, jobject lpObject)
{
	if (NMLVCUSTOMDRAWFc.cached) return;
	cacheNMCUSTOMDRAWFields(env, lpObject);
	NMLVCUSTOMDRAWFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NMLVCUSTOMDRAWFc.clrText = (*env)->GetFieldID(env, NMLVCUSTOMDRAWFc.clazz, "clrText", "I");
	NMLVCUSTOMDRAWFc.clrTextBk = (*env)->GetFieldID(env, NMLVCUSTOMDRAWFc.clazz, "clrTextBk", "I");
	NMLVCUSTOMDRAWFc.iSubItem = (*env)->GetFieldID(env, NMLVCUSTOMDRAWFc.clazz, "iSubItem", "I");
	NMLVCUSTOMDRAWFc.cached = 1;
}

NMLVCUSTOMDRAW *getNMLVCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMLVCUSTOMDRAW *lpStruct)
{
	if (!NMLVCUSTOMDRAWFc.cached) cacheNMLVCUSTOMDRAWFields(env, lpObject);
	getNMCUSTOMDRAWFields(env, lpObject, (NMCUSTOMDRAW *)lpStruct);
	lpStruct->clrText = (*env)->GetIntField(env, lpObject, NMLVCUSTOMDRAWFc.clrText);
	lpStruct->clrTextBk = (*env)->GetIntField(env, lpObject, NMLVCUSTOMDRAWFc.clrTextBk);
	lpStruct->iSubItem = (*env)->GetIntField(env, lpObject, NMLVCUSTOMDRAWFc.iSubItem);
	return lpStruct;
}

void setNMLVCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMLVCUSTOMDRAW *lpStruct)
{
	if (!NMLVCUSTOMDRAWFc.cached) cacheNMLVCUSTOMDRAWFields(env, lpObject);
	setNMCUSTOMDRAWFields(env, lpObject, (NMCUSTOMDRAW *)lpStruct);
	(*env)->SetIntField(env, lpObject, NMLVCUSTOMDRAWFc.clrText, (jint)lpStruct->clrText);
	(*env)->SetIntField(env, lpObject, NMLVCUSTOMDRAWFc.clrTextBk, (jint)lpStruct->clrTextBk);
	(*env)->SetIntField(env, lpObject, NMLVCUSTOMDRAWFc.iSubItem, (jint)lpStruct->iSubItem);
}
#endif

#ifndef NO_NMLVDISPINFO
typedef struct NMLVDISPINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID mask, iItem, iSubItem, state, stateMask, pszText, cchTextMax, iImage, lParam, iIndent;
} NMLVDISPINFO_FID_CACHE;

NMLVDISPINFO_FID_CACHE NMLVDISPINFOFc;

void cacheNMLVDISPINFOFields(JNIEnv *env, jobject lpObject)
{
	if (NMLVDISPINFOFc.cached) return;
	cacheNMHDRFields(env, lpObject);
	NMLVDISPINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NMLVDISPINFOFc.mask = (*env)->GetFieldID(env, NMLVDISPINFOFc.clazz, "mask", "I");
	NMLVDISPINFOFc.iItem = (*env)->GetFieldID(env, NMLVDISPINFOFc.clazz, "iItem", "I");
	NMLVDISPINFOFc.iSubItem = (*env)->GetFieldID(env, NMLVDISPINFOFc.clazz, "iSubItem", "I");
	NMLVDISPINFOFc.state = (*env)->GetFieldID(env, NMLVDISPINFOFc.clazz, "state", "I");
	NMLVDISPINFOFc.stateMask = (*env)->GetFieldID(env, NMLVDISPINFOFc.clazz, "stateMask", "I");
	NMLVDISPINFOFc.pszText = (*env)->GetFieldID(env, NMLVDISPINFOFc.clazz, "pszText", "I");
	NMLVDISPINFOFc.cchTextMax = (*env)->GetFieldID(env, NMLVDISPINFOFc.clazz, "cchTextMax", "I");
	NMLVDISPINFOFc.iImage = (*env)->GetFieldID(env, NMLVDISPINFOFc.clazz, "iImage", "I");
	NMLVDISPINFOFc.lParam = (*env)->GetFieldID(env, NMLVDISPINFOFc.clazz, "lParam", "I");
	NMLVDISPINFOFc.iIndent = (*env)->GetFieldID(env, NMLVDISPINFOFc.clazz, "iIndent", "I");
	NMLVDISPINFOFc.cached = 1;
}

NMLVDISPINFO *getNMLVDISPINFOFields(JNIEnv *env, jobject lpObject, NMLVDISPINFO *lpStruct)
{
	if (!NMLVDISPINFOFc.cached) cacheNMLVDISPINFOFields(env, lpObject);
	getNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	lpStruct->item.mask = (*env)->GetIntField(env, lpObject, NMLVDISPINFOFc.mask);
	lpStruct->item.iItem = (*env)->GetIntField(env, lpObject, NMLVDISPINFOFc.iItem);
	lpStruct->item.iSubItem = (*env)->GetIntField(env, lpObject, NMLVDISPINFOFc.iSubItem);
	lpStruct->item.state = (*env)->GetIntField(env, lpObject, NMLVDISPINFOFc.state);
	lpStruct->item.stateMask = (*env)->GetIntField(env, lpObject, NMLVDISPINFOFc.stateMask);
	lpStruct->item.pszText = (LPTSTR)(*env)->GetIntField(env, lpObject, NMLVDISPINFOFc.pszText);
	lpStruct->item.cchTextMax = (*env)->GetIntField(env, lpObject, NMLVDISPINFOFc.cchTextMax);
	lpStruct->item.iImage = (*env)->GetIntField(env, lpObject, NMLVDISPINFOFc.iImage);
	lpStruct->item.lParam = (*env)->GetIntField(env, lpObject, NMLVDISPINFOFc.lParam);
	lpStruct->item.iIndent = (*env)->GetIntField(env, lpObject, NMLVDISPINFOFc.iIndent);
	return lpStruct;
}

void setNMLVDISPINFOFields(JNIEnv *env, jobject lpObject, NMLVDISPINFO *lpStruct)
{
	if (!NMLVDISPINFOFc.cached) cacheNMLVDISPINFOFields(env, lpObject);
	setNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	(*env)->SetIntField(env, lpObject, NMLVDISPINFOFc.mask, (jint)lpStruct->item.mask);
	(*env)->SetIntField(env, lpObject, NMLVDISPINFOFc.iItem, (jint)lpStruct->item.iItem);
	(*env)->SetIntField(env, lpObject, NMLVDISPINFOFc.iSubItem, (jint)lpStruct->item.iSubItem);
	(*env)->SetIntField(env, lpObject, NMLVDISPINFOFc.state, (jint)lpStruct->item.state);
	(*env)->SetIntField(env, lpObject, NMLVDISPINFOFc.stateMask, (jint)lpStruct->item.stateMask);
	(*env)->SetIntField(env, lpObject, NMLVDISPINFOFc.pszText, (jint)lpStruct->item.pszText);
	(*env)->SetIntField(env, lpObject, NMLVDISPINFOFc.cchTextMax, (jint)lpStruct->item.cchTextMax);
	(*env)->SetIntField(env, lpObject, NMLVDISPINFOFc.iImage, (jint)lpStruct->item.iImage);
	(*env)->SetIntField(env, lpObject, NMLVDISPINFOFc.lParam, (jint)lpStruct->item.lParam);
	(*env)->SetIntField(env, lpObject, NMLVDISPINFOFc.iIndent, (jint)lpStruct->item.iIndent);
}
#endif

#ifndef NO_NMLVFINDITEM
typedef struct NMLVFINDITEM_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID iStart, flags, psz, lParam, x, y, vkDirection;
} NMLVFINDITEM_FID_CACHE;

NMLVFINDITEM_FID_CACHE NMLVFINDITEMFc;

void cacheNMLVFINDITEMFields(JNIEnv *env, jobject lpObject)
{
	if (NMLVFINDITEMFc.cached) return;
	cacheNMHDRFields(env, lpObject);
	NMLVFINDITEMFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NMLVFINDITEMFc.iStart = (*env)->GetFieldID(env, NMLVFINDITEMFc.clazz, "iStart", "I");
	NMLVFINDITEMFc.flags = (*env)->GetFieldID(env, NMLVFINDITEMFc.clazz, "flags", "I");
	NMLVFINDITEMFc.psz = (*env)->GetFieldID(env, NMLVFINDITEMFc.clazz, "psz", "I");
	NMLVFINDITEMFc.lParam = (*env)->GetFieldID(env, NMLVFINDITEMFc.clazz, "lParam", "I");
	NMLVFINDITEMFc.x = (*env)->GetFieldID(env, NMLVFINDITEMFc.clazz, "x", "I");
	NMLVFINDITEMFc.y = (*env)->GetFieldID(env, NMLVFINDITEMFc.clazz, "y", "I");
	NMLVFINDITEMFc.vkDirection = (*env)->GetFieldID(env, NMLVFINDITEMFc.clazz, "vkDirection", "I");
	NMLVFINDITEMFc.cached = 1;
}

NMLVFINDITEM *getNMLVFINDITEMFields(JNIEnv *env, jobject lpObject, NMLVFINDITEM *lpStruct)
{
	if (!NMLVFINDITEMFc.cached) cacheNMLVFINDITEMFields(env, lpObject);
	getNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	lpStruct->iStart = (*env)->GetIntField(env, lpObject, NMLVFINDITEMFc.iStart);
	lpStruct->lvfi.flags = (*env)->GetIntField(env, lpObject, NMLVFINDITEMFc.flags);
	lpStruct->lvfi.psz = (LPCTSTR)(*env)->GetIntField(env, lpObject, NMLVFINDITEMFc.psz);
	lpStruct->lvfi.lParam = (*env)->GetIntField(env, lpObject, NMLVFINDITEMFc.lParam);
	lpStruct->lvfi.pt.x = (*env)->GetIntField(env, lpObject, NMLVFINDITEMFc.x);
	lpStruct->lvfi.pt.y = (*env)->GetIntField(env, lpObject, NMLVFINDITEMFc.y);
	lpStruct->lvfi.vkDirection = (*env)->GetIntField(env, lpObject, NMLVFINDITEMFc.vkDirection);
	return lpStruct;
}

void setNMLVFINDITEMFields(JNIEnv *env, jobject lpObject, NMLVFINDITEM *lpStruct)
{
	if (!NMLVFINDITEMFc.cached) cacheNMLVFINDITEMFields(env, lpObject);
	setNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	(*env)->SetIntField(env, lpObject, NMLVFINDITEMFc.iStart, (jint)lpStruct->iStart);
	(*env)->SetIntField(env, lpObject, NMLVFINDITEMFc.flags, (jint)lpStruct->lvfi.flags);
	(*env)->SetIntField(env, lpObject, NMLVFINDITEMFc.psz, (jint)lpStruct->lvfi.psz);
	(*env)->SetIntField(env, lpObject, NMLVFINDITEMFc.lParam, (jint)lpStruct->lvfi.lParam);
	(*env)->SetIntField(env, lpObject, NMLVFINDITEMFc.x, (jint)lpStruct->lvfi.pt.x);
	(*env)->SetIntField(env, lpObject, NMLVFINDITEMFc.y, (jint)lpStruct->lvfi.pt.y);
	(*env)->SetIntField(env, lpObject, NMLVFINDITEMFc.vkDirection, (jint)lpStruct->lvfi.vkDirection);
}
#endif

#ifndef NO_NMREBARCHEVRON
typedef struct NMREBARCHEVRON_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID uBand, wID, lParam, left, top, right, bottom, lParamNM;
} NMREBARCHEVRON_FID_CACHE;

NMREBARCHEVRON_FID_CACHE NMREBARCHEVRONFc;

void cacheNMREBARCHEVRONFields(JNIEnv *env, jobject lpObject)
{
	if (NMREBARCHEVRONFc.cached) return;
	cacheNMHDRFields(env, lpObject);
	NMREBARCHEVRONFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NMREBARCHEVRONFc.uBand = (*env)->GetFieldID(env, NMREBARCHEVRONFc.clazz, "uBand", "I");
	NMREBARCHEVRONFc.wID = (*env)->GetFieldID(env, NMREBARCHEVRONFc.clazz, "wID", "I");
	NMREBARCHEVRONFc.lParam = (*env)->GetFieldID(env, NMREBARCHEVRONFc.clazz, "lParam", "I");
	NMREBARCHEVRONFc.left = (*env)->GetFieldID(env, NMREBARCHEVRONFc.clazz, "left", "I");
	NMREBARCHEVRONFc.top = (*env)->GetFieldID(env, NMREBARCHEVRONFc.clazz, "top", "I");
	NMREBARCHEVRONFc.right = (*env)->GetFieldID(env, NMREBARCHEVRONFc.clazz, "right", "I");
	NMREBARCHEVRONFc.bottom = (*env)->GetFieldID(env, NMREBARCHEVRONFc.clazz, "bottom", "I");
	NMREBARCHEVRONFc.lParamNM = (*env)->GetFieldID(env, NMREBARCHEVRONFc.clazz, "lParamNM", "I");
	NMREBARCHEVRONFc.cached = 1;
}

NMREBARCHEVRON *getNMREBARCHEVRONFields(JNIEnv *env, jobject lpObject, NMREBARCHEVRON *lpStruct)
{
	if (!NMREBARCHEVRONFc.cached) cacheNMREBARCHEVRONFields(env, lpObject);
	getNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	lpStruct->uBand = (*env)->GetIntField(env, lpObject, NMREBARCHEVRONFc.uBand);
	lpStruct->wID = (*env)->GetIntField(env, lpObject, NMREBARCHEVRONFc.wID);
	lpStruct->lParam = (*env)->GetIntField(env, lpObject, NMREBARCHEVRONFc.lParam);
	lpStruct->rc.left = (*env)->GetIntField(env, lpObject, NMREBARCHEVRONFc.left);
	lpStruct->rc.top = (*env)->GetIntField(env, lpObject, NMREBARCHEVRONFc.top);
	lpStruct->rc.right = (*env)->GetIntField(env, lpObject, NMREBARCHEVRONFc.right);
	lpStruct->rc.bottom = (*env)->GetIntField(env, lpObject, NMREBARCHEVRONFc.bottom);
	lpStruct->lParamNM = (*env)->GetIntField(env, lpObject, NMREBARCHEVRONFc.lParamNM);
	return lpStruct;
}

void setNMREBARCHEVRONFields(JNIEnv *env, jobject lpObject, NMREBARCHEVRON *lpStruct)
{
	if (!NMREBARCHEVRONFc.cached) cacheNMREBARCHEVRONFields(env, lpObject);
	setNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	(*env)->SetIntField(env, lpObject, NMREBARCHEVRONFc.uBand, (jint)lpStruct->uBand);
	(*env)->SetIntField(env, lpObject, NMREBARCHEVRONFc.wID, (jint)lpStruct->wID);
	(*env)->SetIntField(env, lpObject, NMREBARCHEVRONFc.lParam, (jint)lpStruct->lParam);
	(*env)->SetIntField(env, lpObject, NMREBARCHEVRONFc.left, (jint)lpStruct->rc.left);
	(*env)->SetIntField(env, lpObject, NMREBARCHEVRONFc.top, (jint)lpStruct->rc.top);
	(*env)->SetIntField(env, lpObject, NMREBARCHEVRONFc.right, (jint)lpStruct->rc.right);
	(*env)->SetIntField(env, lpObject, NMREBARCHEVRONFc.bottom, (jint)lpStruct->rc.bottom);
	(*env)->SetIntField(env, lpObject, NMREBARCHEVRONFc.lParamNM, (jint)lpStruct->lParamNM);
}
#endif

#ifndef NO_NMREBARCHILDSIZE
typedef struct NMREBARCHILDSIZE_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID uBand, wID, rcChild_left, rcChild_top, rcChild_right, rcChild_bottom, rcBand_left, rcBand_top, rcBand_right, rcBand_bottom;
} NMREBARCHILDSIZE_FID_CACHE;

NMREBARCHILDSIZE_FID_CACHE NMREBARCHILDSIZEFc;

void cacheNMREBARCHILDSIZEFields(JNIEnv *env, jobject lpObject)
{
	if (NMREBARCHILDSIZEFc.cached) return;
	cacheNMHDRFields(env, lpObject);
	NMREBARCHILDSIZEFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NMREBARCHILDSIZEFc.uBand = (*env)->GetFieldID(env, NMREBARCHILDSIZEFc.clazz, "uBand", "I");
	NMREBARCHILDSIZEFc.wID = (*env)->GetFieldID(env, NMREBARCHILDSIZEFc.clazz, "wID", "I");
	NMREBARCHILDSIZEFc.rcChild_left = (*env)->GetFieldID(env, NMREBARCHILDSIZEFc.clazz, "rcChild_left", "I");
	NMREBARCHILDSIZEFc.rcChild_top = (*env)->GetFieldID(env, NMREBARCHILDSIZEFc.clazz, "rcChild_top", "I");
	NMREBARCHILDSIZEFc.rcChild_right = (*env)->GetFieldID(env, NMREBARCHILDSIZEFc.clazz, "rcChild_right", "I");
	NMREBARCHILDSIZEFc.rcChild_bottom = (*env)->GetFieldID(env, NMREBARCHILDSIZEFc.clazz, "rcChild_bottom", "I");
	NMREBARCHILDSIZEFc.rcBand_left = (*env)->GetFieldID(env, NMREBARCHILDSIZEFc.clazz, "rcBand_left", "I");
	NMREBARCHILDSIZEFc.rcBand_top = (*env)->GetFieldID(env, NMREBARCHILDSIZEFc.clazz, "rcBand_top", "I");
	NMREBARCHILDSIZEFc.rcBand_right = (*env)->GetFieldID(env, NMREBARCHILDSIZEFc.clazz, "rcBand_right", "I");
	NMREBARCHILDSIZEFc.rcBand_bottom = (*env)->GetFieldID(env, NMREBARCHILDSIZEFc.clazz, "rcBand_bottom", "I");
	NMREBARCHILDSIZEFc.cached = 1;
}

NMREBARCHILDSIZE *getNMREBARCHILDSIZEFields(JNIEnv *env, jobject lpObject, NMREBARCHILDSIZE *lpStruct)
{
	if (!NMREBARCHILDSIZEFc.cached) cacheNMREBARCHILDSIZEFields(env, lpObject);
	getNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	lpStruct->uBand = (*env)->GetIntField(env, lpObject, NMREBARCHILDSIZEFc.uBand);
	lpStruct->wID = (*env)->GetIntField(env, lpObject, NMREBARCHILDSIZEFc.wID);
	lpStruct->rcChild.left = (*env)->GetIntField(env, lpObject, NMREBARCHILDSIZEFc.rcChild_left);
	lpStruct->rcChild.top = (*env)->GetIntField(env, lpObject, NMREBARCHILDSIZEFc.rcChild_top);
	lpStruct->rcChild.right = (*env)->GetIntField(env, lpObject, NMREBARCHILDSIZEFc.rcChild_right);
	lpStruct->rcChild.bottom = (*env)->GetIntField(env, lpObject, NMREBARCHILDSIZEFc.rcChild_bottom);
	lpStruct->rcBand.left = (*env)->GetIntField(env, lpObject, NMREBARCHILDSIZEFc.rcBand_left);
	lpStruct->rcBand.top = (*env)->GetIntField(env, lpObject, NMREBARCHILDSIZEFc.rcBand_top);
	lpStruct->rcBand.right = (*env)->GetIntField(env, lpObject, NMREBARCHILDSIZEFc.rcBand_right);
	lpStruct->rcBand.bottom = (*env)->GetIntField(env, lpObject, NMREBARCHILDSIZEFc.rcBand_bottom);
	return lpStruct;
}

void setNMREBARCHILDSIZEFields(JNIEnv *env, jobject lpObject, NMREBARCHILDSIZE *lpStruct)
{
	if (!NMREBARCHILDSIZEFc.cached) cacheNMREBARCHILDSIZEFields(env, lpObject);
	setNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	(*env)->SetIntField(env, lpObject, NMREBARCHILDSIZEFc.uBand, (jint)lpStruct->uBand);
	(*env)->SetIntField(env, lpObject, NMREBARCHILDSIZEFc.wID, (jint)lpStruct->wID);
	(*env)->SetIntField(env, lpObject, NMREBARCHILDSIZEFc.rcChild_left, (jint)lpStruct->rcChild.left);
	(*env)->SetIntField(env, lpObject, NMREBARCHILDSIZEFc.rcChild_top, (jint)lpStruct->rcChild.top);
	(*env)->SetIntField(env, lpObject, NMREBARCHILDSIZEFc.rcChild_right, (jint)lpStruct->rcChild.right);
	(*env)->SetIntField(env, lpObject, NMREBARCHILDSIZEFc.rcChild_bottom, (jint)lpStruct->rcChild.bottom);
	(*env)->SetIntField(env, lpObject, NMREBARCHILDSIZEFc.rcBand_left, (jint)lpStruct->rcBand.left);
	(*env)->SetIntField(env, lpObject, NMREBARCHILDSIZEFc.rcBand_top, (jint)lpStruct->rcBand.top);
	(*env)->SetIntField(env, lpObject, NMREBARCHILDSIZEFc.rcBand_right, (jint)lpStruct->rcBand.right);
	(*env)->SetIntField(env, lpObject, NMREBARCHILDSIZEFc.rcBand_bottom, (jint)lpStruct->rcBand.bottom);
}
#endif

#ifndef NO_NMRGINFO
typedef struct NMRGINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID x, y, dwItemSpec;
} NMRGINFO_FID_CACHE;

NMRGINFO_FID_CACHE NMRGINFOFc;

void cacheNMRGINFOFields(JNIEnv *env, jobject lpObject)
{
	if (NMRGINFOFc.cached) return;
	cacheNMHDRFields(env, lpObject);
	NMRGINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NMRGINFOFc.x = (*env)->GetFieldID(env, NMRGINFOFc.clazz, "x", "I");
	NMRGINFOFc.y = (*env)->GetFieldID(env, NMRGINFOFc.clazz, "y", "I");
	NMRGINFOFc.dwItemSpec = (*env)->GetFieldID(env, NMRGINFOFc.clazz, "dwItemSpec", "I");
	NMRGINFOFc.cached = 1;
}

NMRGINFO *getNMRGINFOFields(JNIEnv *env, jobject lpObject, NMRGINFO *lpStruct)
{
	if (!NMRGINFOFc.cached) cacheNMRGINFOFields(env, lpObject);
	getNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	lpStruct->ptAction.x = (*env)->GetIntField(env, lpObject, NMRGINFOFc.x);
	lpStruct->ptAction.y = (*env)->GetIntField(env, lpObject, NMRGINFOFc.y);
	lpStruct->dwItemSpec = (*env)->GetIntField(env, lpObject, NMRGINFOFc.dwItemSpec);
	return lpStruct;
}

void setNMRGINFOFields(JNIEnv *env, jobject lpObject, NMRGINFO *lpStruct)
{
	if (!NMRGINFOFc.cached) cacheNMRGINFOFields(env, lpObject);
	setNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	(*env)->SetIntField(env, lpObject, NMRGINFOFc.x, (jint)lpStruct->ptAction.x);
	(*env)->SetIntField(env, lpObject, NMRGINFOFc.y, (jint)lpStruct->ptAction.y);
	(*env)->SetIntField(env, lpObject, NMRGINFOFc.dwItemSpec, (jint)lpStruct->dwItemSpec);
}
#endif

#ifndef NO_NMTBHOTITEM
typedef struct NMTBHOTITEM_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID idOld, idNew, dwFlags;
} NMTBHOTITEM_FID_CACHE;

NMTBHOTITEM_FID_CACHE NMTBHOTITEMFc;

void cacheNMTBHOTITEMFields(JNIEnv *env, jobject lpObject)
{
	if (NMTBHOTITEMFc.cached) return;
	cacheNMHDRFields(env, lpObject);
	NMTBHOTITEMFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NMTBHOTITEMFc.idOld = (*env)->GetFieldID(env, NMTBHOTITEMFc.clazz, "idOld", "I");
	NMTBHOTITEMFc.idNew = (*env)->GetFieldID(env, NMTBHOTITEMFc.clazz, "idNew", "I");
	NMTBHOTITEMFc.dwFlags = (*env)->GetFieldID(env, NMTBHOTITEMFc.clazz, "dwFlags", "I");
	NMTBHOTITEMFc.cached = 1;
}

NMTBHOTITEM *getNMTBHOTITEMFields(JNIEnv *env, jobject lpObject, NMTBHOTITEM *lpStruct)
{
	if (!NMTBHOTITEMFc.cached) cacheNMTBHOTITEMFields(env, lpObject);
	getNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	lpStruct->idOld = (*env)->GetIntField(env, lpObject, NMTBHOTITEMFc.idOld);
	lpStruct->idNew = (*env)->GetIntField(env, lpObject, NMTBHOTITEMFc.idNew);
	lpStruct->dwFlags = (*env)->GetIntField(env, lpObject, NMTBHOTITEMFc.dwFlags);
	return lpStruct;
}

void setNMTBHOTITEMFields(JNIEnv *env, jobject lpObject, NMTBHOTITEM *lpStruct)
{
	if (!NMTBHOTITEMFc.cached) cacheNMTBHOTITEMFields(env, lpObject);
	setNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	(*env)->SetIntField(env, lpObject, NMTBHOTITEMFc.idOld, (jint)lpStruct->idOld);
	(*env)->SetIntField(env, lpObject, NMTBHOTITEMFc.idNew, (jint)lpStruct->idNew);
	(*env)->SetIntField(env, lpObject, NMTBHOTITEMFc.dwFlags, (jint)lpStruct->dwFlags);
}
#endif

#ifndef NO_NMTOOLBAR
typedef struct NMTOOLBAR_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID iItem, iBitmap, idCommand, fsState, fsStyle, dwData, iString, cchText, pszText, left, top, right, bottom;
} NMTOOLBAR_FID_CACHE;

NMTOOLBAR_FID_CACHE NMTOOLBARFc;

void cacheNMTOOLBARFields(JNIEnv *env, jobject lpObject)
{
	if (NMTOOLBARFc.cached) return;
	cacheNMHDRFields(env, lpObject);
	NMTOOLBARFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NMTOOLBARFc.iItem = (*env)->GetFieldID(env, NMTOOLBARFc.clazz, "iItem", "I");
	NMTOOLBARFc.iBitmap = (*env)->GetFieldID(env, NMTOOLBARFc.clazz, "iBitmap", "I");
	NMTOOLBARFc.idCommand = (*env)->GetFieldID(env, NMTOOLBARFc.clazz, "idCommand", "I");
	NMTOOLBARFc.fsState = (*env)->GetFieldID(env, NMTOOLBARFc.clazz, "fsState", "B");
	NMTOOLBARFc.fsStyle = (*env)->GetFieldID(env, NMTOOLBARFc.clazz, "fsStyle", "B");
	NMTOOLBARFc.dwData = (*env)->GetFieldID(env, NMTOOLBARFc.clazz, "dwData", "I");
	NMTOOLBARFc.iString = (*env)->GetFieldID(env, NMTOOLBARFc.clazz, "iString", "I");
	NMTOOLBARFc.cchText = (*env)->GetFieldID(env, NMTOOLBARFc.clazz, "cchText", "I");
	NMTOOLBARFc.pszText = (*env)->GetFieldID(env, NMTOOLBARFc.clazz, "pszText", "I");
	NMTOOLBARFc.left = (*env)->GetFieldID(env, NMTOOLBARFc.clazz, "left", "I");
	NMTOOLBARFc.top = (*env)->GetFieldID(env, NMTOOLBARFc.clazz, "top", "I");
	NMTOOLBARFc.right = (*env)->GetFieldID(env, NMTOOLBARFc.clazz, "right", "I");
	NMTOOLBARFc.bottom = (*env)->GetFieldID(env, NMTOOLBARFc.clazz, "bottom", "I");
	NMTOOLBARFc.cached = 1;
}

NMTOOLBAR *getNMTOOLBARFields(JNIEnv *env, jobject lpObject, NMTOOLBAR *lpStruct)
{
	if (!NMTOOLBARFc.cached) cacheNMTOOLBARFields(env, lpObject);
	getNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	lpStruct->iItem = (*env)->GetIntField(env, lpObject, NMTOOLBARFc.iItem);
	lpStruct->tbButton.iBitmap = (*env)->GetIntField(env, lpObject, NMTOOLBARFc.iBitmap);
	lpStruct->tbButton.idCommand = (*env)->GetIntField(env, lpObject, NMTOOLBARFc.idCommand);
	lpStruct->tbButton.fsState = (*env)->GetByteField(env, lpObject, NMTOOLBARFc.fsState);
	lpStruct->tbButton.fsStyle = (*env)->GetByteField(env, lpObject, NMTOOLBARFc.fsStyle);
	lpStruct->tbButton.dwData = (*env)->GetIntField(env, lpObject, NMTOOLBARFc.dwData);
	lpStruct->tbButton.iString = (*env)->GetIntField(env, lpObject, NMTOOLBARFc.iString);
	lpStruct->cchText = (*env)->GetIntField(env, lpObject, NMTOOLBARFc.cchText);
	lpStruct->pszText = (LPTSTR)(*env)->GetIntField(env, lpObject, NMTOOLBARFc.pszText);
#ifndef _WIN32_WCE
	lpStruct->rcButton.left = (*env)->GetIntField(env, lpObject, NMTOOLBARFc.left);
#endif
#ifndef _WIN32_WCE
	lpStruct->rcButton.top = (*env)->GetIntField(env, lpObject, NMTOOLBARFc.top);
#endif
#ifndef _WIN32_WCE
	lpStruct->rcButton.right = (*env)->GetIntField(env, lpObject, NMTOOLBARFc.right);
#endif
#ifndef _WIN32_WCE
	lpStruct->rcButton.bottom = (*env)->GetIntField(env, lpObject, NMTOOLBARFc.bottom);
#endif
	return lpStruct;
}

void setNMTOOLBARFields(JNIEnv *env, jobject lpObject, NMTOOLBAR *lpStruct)
{
	if (!NMTOOLBARFc.cached) cacheNMTOOLBARFields(env, lpObject);
	setNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	(*env)->SetIntField(env, lpObject, NMTOOLBARFc.iItem, (jint)lpStruct->iItem);
	(*env)->SetIntField(env, lpObject, NMTOOLBARFc.iBitmap, (jint)lpStruct->tbButton.iBitmap);
	(*env)->SetIntField(env, lpObject, NMTOOLBARFc.idCommand, (jint)lpStruct->tbButton.idCommand);
	(*env)->SetByteField(env, lpObject, NMTOOLBARFc.fsState, (jbyte)lpStruct->tbButton.fsState);
	(*env)->SetByteField(env, lpObject, NMTOOLBARFc.fsStyle, (jbyte)lpStruct->tbButton.fsStyle);
	(*env)->SetIntField(env, lpObject, NMTOOLBARFc.dwData, (jint)lpStruct->tbButton.dwData);
	(*env)->SetIntField(env, lpObject, NMTOOLBARFc.iString, (jint)lpStruct->tbButton.iString);
	(*env)->SetIntField(env, lpObject, NMTOOLBARFc.cchText, (jint)lpStruct->cchText);
	(*env)->SetIntField(env, lpObject, NMTOOLBARFc.pszText, (jint)lpStruct->pszText);
#ifndef _WIN32_WCE
	(*env)->SetIntField(env, lpObject, NMTOOLBARFc.left, (jint)lpStruct->rcButton.left);
#endif
#ifndef _WIN32_WCE
	(*env)->SetIntField(env, lpObject, NMTOOLBARFc.top, (jint)lpStruct->rcButton.top);
#endif
#ifndef _WIN32_WCE
	(*env)->SetIntField(env, lpObject, NMTOOLBARFc.right, (jint)lpStruct->rcButton.right);
#endif
#ifndef _WIN32_WCE
	(*env)->SetIntField(env, lpObject, NMTOOLBARFc.bottom, (jint)lpStruct->rcButton.bottom);
#endif
}
#endif

#ifndef NO_NMTTDISPINFO
typedef struct NMTTDISPINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lpszText, hinst, uFlags, lParam;
} NMTTDISPINFO_FID_CACHE;

NMTTDISPINFO_FID_CACHE NMTTDISPINFOFc;

void cacheNMTTDISPINFOFields(JNIEnv *env, jobject lpObject)
{
	if (NMTTDISPINFOFc.cached) return;
	cacheNMHDRFields(env, lpObject);
	NMTTDISPINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NMTTDISPINFOFc.lpszText = (*env)->GetFieldID(env, NMTTDISPINFOFc.clazz, "lpszText", "I");
	NMTTDISPINFOFc.hinst = (*env)->GetFieldID(env, NMTTDISPINFOFc.clazz, "hinst", "I");
	NMTTDISPINFOFc.uFlags = (*env)->GetFieldID(env, NMTTDISPINFOFc.clazz, "uFlags", "I");
	NMTTDISPINFOFc.lParam = (*env)->GetFieldID(env, NMTTDISPINFOFc.clazz, "lParam", "I");
	NMTTDISPINFOFc.cached = 1;
}

NMTTDISPINFO *getNMTTDISPINFOFields(JNIEnv *env, jobject lpObject, NMTTDISPINFO *lpStruct)
{
	if (!NMTTDISPINFOFc.cached) cacheNMTTDISPINFOFields(env, lpObject);
	getNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	lpStruct->lpszText = (void *)(*env)->GetIntField(env, lpObject, NMTTDISPINFOFc.lpszText);
	lpStruct->hinst = (HINSTANCE)(*env)->GetIntField(env, lpObject, NMTTDISPINFOFc.hinst);
	lpStruct->uFlags = (*env)->GetIntField(env, lpObject, NMTTDISPINFOFc.uFlags);
	lpStruct->lParam = (*env)->GetIntField(env, lpObject, NMTTDISPINFOFc.lParam);
	return lpStruct;
}

void setNMTTDISPINFOFields(JNIEnv *env, jobject lpObject, NMTTDISPINFO *lpStruct)
{
	if (!NMTTDISPINFOFc.cached) cacheNMTTDISPINFOFields(env, lpObject);
	setNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	(*env)->SetIntField(env, lpObject, NMTTDISPINFOFc.lpszText, (jint)lpStruct->lpszText);
	(*env)->SetIntField(env, lpObject, NMTTDISPINFOFc.hinst, (jint)lpStruct->hinst);
	(*env)->SetIntField(env, lpObject, NMTTDISPINFOFc.uFlags, (jint)lpStruct->uFlags);
	(*env)->SetIntField(env, lpObject, NMTTDISPINFOFc.lParam, (jint)lpStruct->lParam);
}
#endif

#ifndef NO_NMTTDISPINFOA
typedef struct NMTTDISPINFOA_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID szText;
} NMTTDISPINFOA_FID_CACHE;

NMTTDISPINFOA_FID_CACHE NMTTDISPINFOAFc;

void cacheNMTTDISPINFOAFields(JNIEnv *env, jobject lpObject)
{
	if (NMTTDISPINFOAFc.cached) return;
	cacheNMTTDISPINFOFields(env, lpObject);
	NMTTDISPINFOAFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NMTTDISPINFOAFc.szText = (*env)->GetFieldID(env, NMTTDISPINFOAFc.clazz, "szText", "[B");
	NMTTDISPINFOAFc.cached = 1;
}

NMTTDISPINFOA *getNMTTDISPINFOAFields(JNIEnv *env, jobject lpObject, NMTTDISPINFOA *lpStruct)
{
	if (!NMTTDISPINFOAFc.cached) cacheNMTTDISPINFOAFields(env, lpObject);
	getNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	lpStruct->lpszText = (void *)(*env)->GetIntField(env, lpObject, NMTTDISPINFOFc.lpszText);
	lpStruct->hinst = (HINSTANCE)(*env)->GetIntField(env, lpObject, NMTTDISPINFOFc.hinst);
	lpStruct->uFlags = (*env)->GetIntField(env, lpObject, NMTTDISPINFOFc.uFlags);
	lpStruct->lParam = (*env)->GetIntField(env, lpObject, NMTTDISPINFOFc.lParam);
	{
	jbyteArray lpObject1 = (jbyteArray)(*env)->GetObjectField(env, lpObject, NMTTDISPINFOAFc.szText);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->szText), (jbyte *)lpStruct->szText);
	}
	return lpStruct;
}

void setNMTTDISPINFOAFields(JNIEnv *env, jobject lpObject, NMTTDISPINFOA *lpStruct)
{
	if (!NMTTDISPINFOAFc.cached) cacheNMTTDISPINFOAFields(env, lpObject);
	setNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	(*env)->SetIntField(env, lpObject, NMTTDISPINFOFc.lpszText, (jint)lpStruct->lpszText);
	(*env)->SetIntField(env, lpObject, NMTTDISPINFOFc.hinst, (jint)lpStruct->hinst);
	(*env)->SetIntField(env, lpObject, NMTTDISPINFOFc.uFlags, (jint)lpStruct->uFlags);
	(*env)->SetIntField(env, lpObject, NMTTDISPINFOFc.lParam, (jint)lpStruct->lParam);
	{
	jbyteArray lpObject1 = (jbyteArray)(*env)->GetObjectField(env, lpObject, NMTTDISPINFOAFc.szText);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->szText), (jbyte *)lpStruct->szText);
	}
}
#endif

#ifndef NO_NMTTDISPINFOW
typedef struct NMTTDISPINFOW_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID szText;
} NMTTDISPINFOW_FID_CACHE;

NMTTDISPINFOW_FID_CACHE NMTTDISPINFOWFc;

void cacheNMTTDISPINFOWFields(JNIEnv *env, jobject lpObject)
{
	if (NMTTDISPINFOWFc.cached) return;
	cacheNMTTDISPINFOFields(env, lpObject);
	NMTTDISPINFOWFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NMTTDISPINFOWFc.szText = (*env)->GetFieldID(env, NMTTDISPINFOWFc.clazz, "szText", "[C");
	NMTTDISPINFOWFc.cached = 1;
}

NMTTDISPINFOW *getNMTTDISPINFOWFields(JNIEnv *env, jobject lpObject, NMTTDISPINFOW *lpStruct)
{
	if (!NMTTDISPINFOWFc.cached) cacheNMTTDISPINFOWFields(env, lpObject);
	getNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	lpStruct->lpszText = (void *)(*env)->GetIntField(env, lpObject, NMTTDISPINFOFc.lpszText);
	lpStruct->hinst = (HINSTANCE)(*env)->GetIntField(env, lpObject, NMTTDISPINFOFc.hinst);
	lpStruct->uFlags = (*env)->GetIntField(env, lpObject, NMTTDISPINFOFc.uFlags);
	lpStruct->lParam = (*env)->GetIntField(env, lpObject, NMTTDISPINFOFc.lParam);
	{
	jcharArray lpObject1 = (jcharArray)(*env)->GetObjectField(env, lpObject, NMTTDISPINFOWFc.szText);
	(*env)->GetCharArrayRegion(env, lpObject1, 0, sizeof(lpStruct->szText) / 2, (jchar *)lpStruct->szText);
	}
	return lpStruct;
}

void setNMTTDISPINFOWFields(JNIEnv *env, jobject lpObject, NMTTDISPINFOW *lpStruct)
{
	if (!NMTTDISPINFOWFc.cached) cacheNMTTDISPINFOWFields(env, lpObject);
	setNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	(*env)->SetIntField(env, lpObject, NMTTDISPINFOFc.lpszText, (jint)lpStruct->lpszText);
	(*env)->SetIntField(env, lpObject, NMTTDISPINFOFc.hinst, (jint)lpStruct->hinst);
	(*env)->SetIntField(env, lpObject, NMTTDISPINFOFc.uFlags, (jint)lpStruct->uFlags);
	(*env)->SetIntField(env, lpObject, NMTTDISPINFOFc.lParam, (jint)lpStruct->lParam);
	{
	jcharArray lpObject1 = (jcharArray)(*env)->GetObjectField(env, lpObject, NMTTDISPINFOWFc.szText);
	(*env)->SetCharArrayRegion(env, lpObject1, 0, sizeof(lpStruct->szText) / 2, (jchar *)lpStruct->szText);
	}
}
#endif

#ifndef NO_NMTVCUSTOMDRAW
typedef struct NMTVCUSTOMDRAW_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID clrText, clrTextBk, iLevel;
} NMTVCUSTOMDRAW_FID_CACHE;

NMTVCUSTOMDRAW_FID_CACHE NMTVCUSTOMDRAWFc;

void cacheNMTVCUSTOMDRAWFields(JNIEnv *env, jobject lpObject)
{
	if (NMTVCUSTOMDRAWFc.cached) return;
	cacheNMCUSTOMDRAWFields(env, lpObject);
	NMTVCUSTOMDRAWFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NMTVCUSTOMDRAWFc.clrText = (*env)->GetFieldID(env, NMTVCUSTOMDRAWFc.clazz, "clrText", "I");
	NMTVCUSTOMDRAWFc.clrTextBk = (*env)->GetFieldID(env, NMTVCUSTOMDRAWFc.clazz, "clrTextBk", "I");
	NMTVCUSTOMDRAWFc.iLevel = (*env)->GetFieldID(env, NMTVCUSTOMDRAWFc.clazz, "iLevel", "I");
	NMTVCUSTOMDRAWFc.cached = 1;
}

NMTVCUSTOMDRAW *getNMTVCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMTVCUSTOMDRAW *lpStruct)
{
	if (!NMTVCUSTOMDRAWFc.cached) cacheNMTVCUSTOMDRAWFields(env, lpObject);
	getNMCUSTOMDRAWFields(env, lpObject, (NMCUSTOMDRAW *)lpStruct);
	lpStruct->clrText = (*env)->GetIntField(env, lpObject, NMTVCUSTOMDRAWFc.clrText);
	lpStruct->clrTextBk = (*env)->GetIntField(env, lpObject, NMTVCUSTOMDRAWFc.clrTextBk);
#ifndef _WIN32_WCE
	lpStruct->iLevel = (*env)->GetIntField(env, lpObject, NMTVCUSTOMDRAWFc.iLevel);
#endif
	return lpStruct;
}

void setNMTVCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMTVCUSTOMDRAW *lpStruct)
{
	if (!NMTVCUSTOMDRAWFc.cached) cacheNMTVCUSTOMDRAWFields(env, lpObject);
	setNMCUSTOMDRAWFields(env, lpObject, (NMCUSTOMDRAW *)lpStruct);
	(*env)->SetIntField(env, lpObject, NMTVCUSTOMDRAWFc.clrText, (jint)lpStruct->clrText);
	(*env)->SetIntField(env, lpObject, NMTVCUSTOMDRAWFc.clrTextBk, (jint)lpStruct->clrTextBk);
#ifndef _WIN32_WCE
	(*env)->SetIntField(env, lpObject, NMTVCUSTOMDRAWFc.iLevel, (jint)lpStruct->iLevel);
#endif
}
#endif

#ifndef NO_NMTVDISPINFO
typedef struct NMTVDISPINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID mask, hItem, state, stateMask, pszText, cchTextMax, iImage, iSelectedImage, cChildren, lParam;
} NMTVDISPINFO_FID_CACHE;

NMTVDISPINFO_FID_CACHE NMTVDISPINFOFc;

void cacheNMTVDISPINFOFields(JNIEnv *env, jobject lpObject)
{
	if (NMTVDISPINFOFc.cached) return;
	cacheNMHDRFields(env, lpObject);
	NMTVDISPINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NMTVDISPINFOFc.mask = (*env)->GetFieldID(env, NMTVDISPINFOFc.clazz, "mask", "I");
	NMTVDISPINFOFc.hItem = (*env)->GetFieldID(env, NMTVDISPINFOFc.clazz, "hItem", "I");
	NMTVDISPINFOFc.state = (*env)->GetFieldID(env, NMTVDISPINFOFc.clazz, "state", "I");
	NMTVDISPINFOFc.stateMask = (*env)->GetFieldID(env, NMTVDISPINFOFc.clazz, "stateMask", "I");
	NMTVDISPINFOFc.pszText = (*env)->GetFieldID(env, NMTVDISPINFOFc.clazz, "pszText", "I");
	NMTVDISPINFOFc.cchTextMax = (*env)->GetFieldID(env, NMTVDISPINFOFc.clazz, "cchTextMax", "I");
	NMTVDISPINFOFc.iImage = (*env)->GetFieldID(env, NMTVDISPINFOFc.clazz, "iImage", "I");
	NMTVDISPINFOFc.iSelectedImage = (*env)->GetFieldID(env, NMTVDISPINFOFc.clazz, "iSelectedImage", "I");
	NMTVDISPINFOFc.cChildren = (*env)->GetFieldID(env, NMTVDISPINFOFc.clazz, "cChildren", "I");
	NMTVDISPINFOFc.lParam = (*env)->GetFieldID(env, NMTVDISPINFOFc.clazz, "lParam", "I");
	NMTVDISPINFOFc.cached = 1;
}

NMTVDISPINFO *getNMTVDISPINFOFields(JNIEnv *env, jobject lpObject, NMTVDISPINFO *lpStruct)
{
	if (!NMTVDISPINFOFc.cached) cacheNMTVDISPINFOFields(env, lpObject);
	getNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	lpStruct->item.mask = (*env)->GetIntField(env, lpObject, NMTVDISPINFOFc.mask);
	lpStruct->item.hItem = (HTREEITEM)(*env)->GetIntField(env, lpObject, NMTVDISPINFOFc.hItem);
	lpStruct->item.state = (*env)->GetIntField(env, lpObject, NMTVDISPINFOFc.state);
	lpStruct->item.stateMask = (*env)->GetIntField(env, lpObject, NMTVDISPINFOFc.stateMask);
	lpStruct->item.pszText = (LPTSTR)(*env)->GetIntField(env, lpObject, NMTVDISPINFOFc.pszText);
	lpStruct->item.cchTextMax = (*env)->GetIntField(env, lpObject, NMTVDISPINFOFc.cchTextMax);
	lpStruct->item.iImage = (*env)->GetIntField(env, lpObject, NMTVDISPINFOFc.iImage);
	lpStruct->item.iSelectedImage = (*env)->GetIntField(env, lpObject, NMTVDISPINFOFc.iSelectedImage);
	lpStruct->item.cChildren = (*env)->GetIntField(env, lpObject, NMTVDISPINFOFc.cChildren);
	lpStruct->item.lParam = (*env)->GetIntField(env, lpObject, NMTVDISPINFOFc.lParam);
	return lpStruct;
}

void setNMTVDISPINFOFields(JNIEnv *env, jobject lpObject, NMTVDISPINFO *lpStruct)
{
	if (!NMTVDISPINFOFc.cached) cacheNMTVDISPINFOFields(env, lpObject);
	setNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	(*env)->SetIntField(env, lpObject, NMTVDISPINFOFc.mask, (jint)lpStruct->item.mask);
	(*env)->SetIntField(env, lpObject, NMTVDISPINFOFc.hItem, (jint)lpStruct->item.hItem);
	(*env)->SetIntField(env, lpObject, NMTVDISPINFOFc.state, (jint)lpStruct->item.state);
	(*env)->SetIntField(env, lpObject, NMTVDISPINFOFc.stateMask, (jint)lpStruct->item.stateMask);
	(*env)->SetIntField(env, lpObject, NMTVDISPINFOFc.pszText, (jint)lpStruct->item.pszText);
	(*env)->SetIntField(env, lpObject, NMTVDISPINFOFc.cchTextMax, (jint)lpStruct->item.cchTextMax);
	(*env)->SetIntField(env, lpObject, NMTVDISPINFOFc.iImage, (jint)lpStruct->item.iImage);
	(*env)->SetIntField(env, lpObject, NMTVDISPINFOFc.iSelectedImage, (jint)lpStruct->item.iSelectedImage);
	(*env)->SetIntField(env, lpObject, NMTVDISPINFOFc.cChildren, (jint)lpStruct->item.cChildren);
	(*env)->SetIntField(env, lpObject, NMTVDISPINFOFc.lParam, (jint)lpStruct->item.lParam);
}
#endif

#ifndef NO_NMUPDOWN
typedef struct NMUPDOWN_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID iPos, iDelta;
} NMUPDOWN_FID_CACHE;

NMUPDOWN_FID_CACHE NMUPDOWNFc;

void cacheNMUPDOWNFields(JNIEnv *env, jobject lpObject)
{
	if (NMUPDOWNFc.cached) return;
	cacheNMHDRFields(env, lpObject);
	NMUPDOWNFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NMUPDOWNFc.iPos = (*env)->GetFieldID(env, NMUPDOWNFc.clazz, "iPos", "I");
	NMUPDOWNFc.iDelta = (*env)->GetFieldID(env, NMUPDOWNFc.clazz, "iDelta", "I");
	NMUPDOWNFc.cached = 1;
}

NMUPDOWN *getNMUPDOWNFields(JNIEnv *env, jobject lpObject, NMUPDOWN *lpStruct)
{
	if (!NMUPDOWNFc.cached) cacheNMUPDOWNFields(env, lpObject);
	getNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	lpStruct->iPos = (*env)->GetIntField(env, lpObject, NMUPDOWNFc.iPos);
	lpStruct->iDelta = (*env)->GetIntField(env, lpObject, NMUPDOWNFc.iDelta);
	return lpStruct;
}

void setNMUPDOWNFields(JNIEnv *env, jobject lpObject, NMUPDOWN *lpStruct)
{
	if (!NMUPDOWNFc.cached) cacheNMUPDOWNFields(env, lpObject);
	setNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	(*env)->SetIntField(env, lpObject, NMUPDOWNFc.iPos, (jint)lpStruct->iPos);
	(*env)->SetIntField(env, lpObject, NMUPDOWNFc.iDelta, (jint)lpStruct->iDelta);
}
#endif

#ifndef NO_NONCLIENTMETRICS
typedef struct NONCLIENTMETRICS_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, iBorderWidth, iScrollWidth, iScrollHeight, iCaptionWidth, iCaptionHeight, iSmCaptionWidth, iSmCaptionHeight, iMenuWidth, iMenuHeight;
} NONCLIENTMETRICS_FID_CACHE;

NONCLIENTMETRICS_FID_CACHE NONCLIENTMETRICSFc;

void cacheNONCLIENTMETRICSFields(JNIEnv *env, jobject lpObject)
{
	if (NONCLIENTMETRICSFc.cached) return;
	NONCLIENTMETRICSFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NONCLIENTMETRICSFc.cbSize = (*env)->GetFieldID(env, NONCLIENTMETRICSFc.clazz, "cbSize", "I");
	NONCLIENTMETRICSFc.iBorderWidth = (*env)->GetFieldID(env, NONCLIENTMETRICSFc.clazz, "iBorderWidth", "I");
	NONCLIENTMETRICSFc.iScrollWidth = (*env)->GetFieldID(env, NONCLIENTMETRICSFc.clazz, "iScrollWidth", "I");
	NONCLIENTMETRICSFc.iScrollHeight = (*env)->GetFieldID(env, NONCLIENTMETRICSFc.clazz, "iScrollHeight", "I");
	NONCLIENTMETRICSFc.iCaptionWidth = (*env)->GetFieldID(env, NONCLIENTMETRICSFc.clazz, "iCaptionWidth", "I");
	NONCLIENTMETRICSFc.iCaptionHeight = (*env)->GetFieldID(env, NONCLIENTMETRICSFc.clazz, "iCaptionHeight", "I");
	NONCLIENTMETRICSFc.iSmCaptionWidth = (*env)->GetFieldID(env, NONCLIENTMETRICSFc.clazz, "iSmCaptionWidth", "I");
	NONCLIENTMETRICSFc.iSmCaptionHeight = (*env)->GetFieldID(env, NONCLIENTMETRICSFc.clazz, "iSmCaptionHeight", "I");
	NONCLIENTMETRICSFc.iMenuWidth = (*env)->GetFieldID(env, NONCLIENTMETRICSFc.clazz, "iMenuWidth", "I");
	NONCLIENTMETRICSFc.iMenuHeight = (*env)->GetFieldID(env, NONCLIENTMETRICSFc.clazz, "iMenuHeight", "I");
	NONCLIENTMETRICSFc.cached = 1;
}

NONCLIENTMETRICS *getNONCLIENTMETRICSFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICS *lpStruct)
{
	if (!NONCLIENTMETRICSFc.cached) cacheNONCLIENTMETRICSFields(env, lpObject);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.cbSize);
	lpStruct->iBorderWidth = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iBorderWidth);
	lpStruct->iScrollWidth = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iScrollWidth);
	lpStruct->iScrollHeight = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iScrollHeight);
	lpStruct->iCaptionWidth = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iCaptionWidth);
	lpStruct->iCaptionHeight = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iCaptionHeight);
	lpStruct->iSmCaptionWidth = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iSmCaptionWidth);
	lpStruct->iSmCaptionHeight = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iSmCaptionHeight);
	lpStruct->iMenuWidth = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iMenuWidth);
	lpStruct->iMenuHeight = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iMenuHeight);
	return lpStruct;
}

void setNONCLIENTMETRICSFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICS *lpStruct)
{
	if (!NONCLIENTMETRICSFc.cached) cacheNONCLIENTMETRICSFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.cbSize, (jint)lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iBorderWidth, (jint)lpStruct->iBorderWidth);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iScrollWidth, (jint)lpStruct->iScrollWidth);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iScrollHeight, (jint)lpStruct->iScrollHeight);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iCaptionWidth, (jint)lpStruct->iCaptionWidth);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iCaptionHeight, (jint)lpStruct->iCaptionHeight);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iSmCaptionWidth, (jint)lpStruct->iSmCaptionWidth);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iSmCaptionHeight, (jint)lpStruct->iSmCaptionHeight);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iMenuWidth, (jint)lpStruct->iMenuWidth);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iMenuHeight, (jint)lpStruct->iMenuHeight);
}
#endif

#ifndef NO_NONCLIENTMETRICSA
typedef struct NONCLIENTMETRICSA_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lfCaptionFont, lfSmCaptionFont, lfMenuFont, lfStatusFont, lfMessageFont;
} NONCLIENTMETRICSA_FID_CACHE;

NONCLIENTMETRICSA_FID_CACHE NONCLIENTMETRICSAFc;

void cacheNONCLIENTMETRICSAFields(JNIEnv *env, jobject lpObject)
{
	if (NONCLIENTMETRICSAFc.cached) return;
	cacheNONCLIENTMETRICSFields(env, lpObject);
	NONCLIENTMETRICSAFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NONCLIENTMETRICSAFc.lfCaptionFont = (*env)->GetFieldID(env, NONCLIENTMETRICSAFc.clazz, "lfCaptionFont", "Lorg/eclipse/swt/internal/win32/LOGFONTA;");
	NONCLIENTMETRICSAFc.lfSmCaptionFont = (*env)->GetFieldID(env, NONCLIENTMETRICSAFc.clazz, "lfSmCaptionFont", "Lorg/eclipse/swt/internal/win32/LOGFONTA;");
	NONCLIENTMETRICSAFc.lfMenuFont = (*env)->GetFieldID(env, NONCLIENTMETRICSAFc.clazz, "lfMenuFont", "Lorg/eclipse/swt/internal/win32/LOGFONTA;");
	NONCLIENTMETRICSAFc.lfStatusFont = (*env)->GetFieldID(env, NONCLIENTMETRICSAFc.clazz, "lfStatusFont", "Lorg/eclipse/swt/internal/win32/LOGFONTA;");
	NONCLIENTMETRICSAFc.lfMessageFont = (*env)->GetFieldID(env, NONCLIENTMETRICSAFc.clazz, "lfMessageFont", "Lorg/eclipse/swt/internal/win32/LOGFONTA;");
	NONCLIENTMETRICSAFc.cached = 1;
}

NONCLIENTMETRICSA *getNONCLIENTMETRICSAFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICSA *lpStruct)
{
	if (!NONCLIENTMETRICSAFc.cached) cacheNONCLIENTMETRICSAFields(env, lpObject);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.cbSize);
	lpStruct->iBorderWidth = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iBorderWidth);
	lpStruct->iScrollWidth = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iScrollWidth);
	lpStruct->iScrollHeight = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iScrollHeight);
	lpStruct->iCaptionWidth = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iCaptionWidth);
	lpStruct->iCaptionHeight = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iCaptionHeight);
	lpStruct->iSmCaptionWidth = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iSmCaptionWidth);
	lpStruct->iSmCaptionHeight = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iSmCaptionHeight);
	lpStruct->iMenuWidth = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iMenuWidth);
	lpStruct->iMenuHeight = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iMenuHeight);
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSAFc.lfCaptionFont);
	if (lpObject1 != NULL) getLOGFONTAFields(env, lpObject1, &lpStruct->lfCaptionFont);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSAFc.lfSmCaptionFont);
	if (lpObject1 != NULL) getLOGFONTAFields(env, lpObject1, &lpStruct->lfSmCaptionFont);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSAFc.lfMenuFont);
	if (lpObject1 != NULL) getLOGFONTAFields(env, lpObject1, &lpStruct->lfMenuFont);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSAFc.lfStatusFont);
	if (lpObject1 != NULL) getLOGFONTAFields(env, lpObject1, &lpStruct->lfStatusFont);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSAFc.lfMessageFont);
	if (lpObject1 != NULL) getLOGFONTAFields(env, lpObject1, &lpStruct->lfMessageFont);
	}
	return lpStruct;
}

void setNONCLIENTMETRICSAFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICSA *lpStruct)
{
	if (!NONCLIENTMETRICSAFc.cached) cacheNONCLIENTMETRICSAFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.cbSize, (jint)lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iBorderWidth, (jint)lpStruct->iBorderWidth);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iScrollWidth, (jint)lpStruct->iScrollWidth);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iScrollHeight, (jint)lpStruct->iScrollHeight);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iCaptionWidth, (jint)lpStruct->iCaptionWidth);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iCaptionHeight, (jint)lpStruct->iCaptionHeight);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iSmCaptionWidth, (jint)lpStruct->iSmCaptionWidth);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iSmCaptionHeight, (jint)lpStruct->iSmCaptionHeight);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iMenuWidth, (jint)lpStruct->iMenuWidth);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iMenuHeight, (jint)lpStruct->iMenuHeight);
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSAFc.lfCaptionFont);
	if (lpObject1 != NULL) setLOGFONTAFields(env, lpObject1, &lpStruct->lfCaptionFont);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSAFc.lfSmCaptionFont);
	if (lpObject1 != NULL) setLOGFONTAFields(env, lpObject1, &lpStruct->lfSmCaptionFont);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSAFc.lfMenuFont);
	if (lpObject1 != NULL) setLOGFONTAFields(env, lpObject1, &lpStruct->lfMenuFont);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSAFc.lfStatusFont);
	if (lpObject1 != NULL) setLOGFONTAFields(env, lpObject1, &lpStruct->lfStatusFont);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSAFc.lfMessageFont);
	if (lpObject1 != NULL) setLOGFONTAFields(env, lpObject1, &lpStruct->lfMessageFont);
	}
}
#endif

#ifndef NO_NONCLIENTMETRICSW
typedef struct NONCLIENTMETRICSW_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lfCaptionFont, lfSmCaptionFont, lfMenuFont, lfStatusFont, lfMessageFont;
} NONCLIENTMETRICSW_FID_CACHE;

NONCLIENTMETRICSW_FID_CACHE NONCLIENTMETRICSWFc;

void cacheNONCLIENTMETRICSWFields(JNIEnv *env, jobject lpObject)
{
	if (NONCLIENTMETRICSWFc.cached) return;
	cacheNONCLIENTMETRICSFields(env, lpObject);
	NONCLIENTMETRICSWFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NONCLIENTMETRICSWFc.lfCaptionFont = (*env)->GetFieldID(env, NONCLIENTMETRICSWFc.clazz, "lfCaptionFont", "Lorg/eclipse/swt/internal/win32/LOGFONTW;");
	NONCLIENTMETRICSWFc.lfSmCaptionFont = (*env)->GetFieldID(env, NONCLIENTMETRICSWFc.clazz, "lfSmCaptionFont", "Lorg/eclipse/swt/internal/win32/LOGFONTW;");
	NONCLIENTMETRICSWFc.lfMenuFont = (*env)->GetFieldID(env, NONCLIENTMETRICSWFc.clazz, "lfMenuFont", "Lorg/eclipse/swt/internal/win32/LOGFONTW;");
	NONCLIENTMETRICSWFc.lfStatusFont = (*env)->GetFieldID(env, NONCLIENTMETRICSWFc.clazz, "lfStatusFont", "Lorg/eclipse/swt/internal/win32/LOGFONTW;");
	NONCLIENTMETRICSWFc.lfMessageFont = (*env)->GetFieldID(env, NONCLIENTMETRICSWFc.clazz, "lfMessageFont", "Lorg/eclipse/swt/internal/win32/LOGFONTW;");
	NONCLIENTMETRICSWFc.cached = 1;
}

NONCLIENTMETRICSW *getNONCLIENTMETRICSWFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICSW *lpStruct)
{
	if (!NONCLIENTMETRICSWFc.cached) cacheNONCLIENTMETRICSWFields(env, lpObject);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.cbSize);
	lpStruct->iBorderWidth = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iBorderWidth);
	lpStruct->iScrollWidth = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iScrollWidth);
	lpStruct->iScrollHeight = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iScrollHeight);
	lpStruct->iCaptionWidth = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iCaptionWidth);
	lpStruct->iCaptionHeight = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iCaptionHeight);
	lpStruct->iSmCaptionWidth = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iSmCaptionWidth);
	lpStruct->iSmCaptionHeight = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iSmCaptionHeight);
	lpStruct->iMenuWidth = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iMenuWidth);
	lpStruct->iMenuHeight = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iMenuHeight);
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSWFc.lfCaptionFont);
	if (lpObject1 != NULL) getLOGFONTWFields(env, lpObject1, &lpStruct->lfCaptionFont);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSWFc.lfSmCaptionFont);
	if (lpObject1 != NULL) getLOGFONTWFields(env, lpObject1, &lpStruct->lfSmCaptionFont);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSWFc.lfMenuFont);
	if (lpObject1 != NULL) getLOGFONTWFields(env, lpObject1, &lpStruct->lfMenuFont);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSWFc.lfStatusFont);
	if (lpObject1 != NULL) getLOGFONTWFields(env, lpObject1, &lpStruct->lfStatusFont);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSWFc.lfMessageFont);
	if (lpObject1 != NULL) getLOGFONTWFields(env, lpObject1, &lpStruct->lfMessageFont);
	}
	return lpStruct;
}

void setNONCLIENTMETRICSWFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICSW *lpStruct)
{
	if (!NONCLIENTMETRICSWFc.cached) cacheNONCLIENTMETRICSWFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.cbSize, (jint)lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iBorderWidth, (jint)lpStruct->iBorderWidth);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iScrollWidth, (jint)lpStruct->iScrollWidth);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iScrollHeight, (jint)lpStruct->iScrollHeight);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iCaptionWidth, (jint)lpStruct->iCaptionWidth);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iCaptionHeight, (jint)lpStruct->iCaptionHeight);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iSmCaptionWidth, (jint)lpStruct->iSmCaptionWidth);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iSmCaptionHeight, (jint)lpStruct->iSmCaptionHeight);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iMenuWidth, (jint)lpStruct->iMenuWidth);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iMenuHeight, (jint)lpStruct->iMenuHeight);
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSWFc.lfCaptionFont);
	if (lpObject1 != NULL) setLOGFONTWFields(env, lpObject1, &lpStruct->lfCaptionFont);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSWFc.lfSmCaptionFont);
	if (lpObject1 != NULL) setLOGFONTWFields(env, lpObject1, &lpStruct->lfSmCaptionFont);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSWFc.lfMenuFont);
	if (lpObject1 != NULL) setLOGFONTWFields(env, lpObject1, &lpStruct->lfMenuFont);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSWFc.lfStatusFont);
	if (lpObject1 != NULL) setLOGFONTWFields(env, lpObject1, &lpStruct->lfStatusFont);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSWFc.lfMessageFont);
	if (lpObject1 != NULL) setLOGFONTWFields(env, lpObject1, &lpStruct->lfMessageFont);
	}
}
#endif

#ifndef NO_NOTIFYICONDATA
typedef struct NOTIFYICONDATA_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, hWnd, uID, uFlags, uCallbackMessage, hIcon, dwState, dwStateMask, uVersion, dwInfoFlags;
} NOTIFYICONDATA_FID_CACHE;

NOTIFYICONDATA_FID_CACHE NOTIFYICONDATAFc;

void cacheNOTIFYICONDATAFields(JNIEnv *env, jobject lpObject)
{
	if (NOTIFYICONDATAFc.cached) return;
	NOTIFYICONDATAFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NOTIFYICONDATAFc.cbSize = (*env)->GetFieldID(env, NOTIFYICONDATAFc.clazz, "cbSize", "I");
	NOTIFYICONDATAFc.hWnd = (*env)->GetFieldID(env, NOTIFYICONDATAFc.clazz, "hWnd", "I");
	NOTIFYICONDATAFc.uID = (*env)->GetFieldID(env, NOTIFYICONDATAFc.clazz, "uID", "I");
	NOTIFYICONDATAFc.uFlags = (*env)->GetFieldID(env, NOTIFYICONDATAFc.clazz, "uFlags", "I");
	NOTIFYICONDATAFc.uCallbackMessage = (*env)->GetFieldID(env, NOTIFYICONDATAFc.clazz, "uCallbackMessage", "I");
	NOTIFYICONDATAFc.hIcon = (*env)->GetFieldID(env, NOTIFYICONDATAFc.clazz, "hIcon", "I");
	NOTIFYICONDATAFc.dwState = (*env)->GetFieldID(env, NOTIFYICONDATAFc.clazz, "dwState", "I");
	NOTIFYICONDATAFc.dwStateMask = (*env)->GetFieldID(env, NOTIFYICONDATAFc.clazz, "dwStateMask", "I");
	NOTIFYICONDATAFc.uVersion = (*env)->GetFieldID(env, NOTIFYICONDATAFc.clazz, "uVersion", "I");
	NOTIFYICONDATAFc.dwInfoFlags = (*env)->GetFieldID(env, NOTIFYICONDATAFc.clazz, "dwInfoFlags", "I");
	NOTIFYICONDATAFc.cached = 1;
}

NOTIFYICONDATA *getNOTIFYICONDATAFields(JNIEnv *env, jobject lpObject, NOTIFYICONDATA *lpStruct)
{
	if (!NOTIFYICONDATAFc.cached) cacheNOTIFYICONDATAFields(env, lpObject);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, NOTIFYICONDATAFc.cbSize);
	lpStruct->hWnd = (HWND)(*env)->GetIntField(env, lpObject, NOTIFYICONDATAFc.hWnd);
	lpStruct->uID = (*env)->GetIntField(env, lpObject, NOTIFYICONDATAFc.uID);
	lpStruct->uFlags = (*env)->GetIntField(env, lpObject, NOTIFYICONDATAFc.uFlags);
	lpStruct->uCallbackMessage = (*env)->GetIntField(env, lpObject, NOTIFYICONDATAFc.uCallbackMessage);
	lpStruct->hIcon = (HICON)(*env)->GetIntField(env, lpObject, NOTIFYICONDATAFc.hIcon);
#ifndef _WIN32_WCE
	lpStruct->dwState = (*env)->GetIntField(env, lpObject, NOTIFYICONDATAFc.dwState);
#endif
#ifndef _WIN32_WCE
	lpStruct->dwStateMask = (*env)->GetIntField(env, lpObject, NOTIFYICONDATAFc.dwStateMask);
#endif
#ifndef _WIN32_WCE
	lpStruct->uVersion = (*env)->GetIntField(env, lpObject, NOTIFYICONDATAFc.uVersion);
#endif
#ifndef _WIN32_WCE
	lpStruct->dwInfoFlags = (*env)->GetIntField(env, lpObject, NOTIFYICONDATAFc.dwInfoFlags);
#endif
	return lpStruct;
}

void setNOTIFYICONDATAFields(JNIEnv *env, jobject lpObject, NOTIFYICONDATA *lpStruct)
{
	if (!NOTIFYICONDATAFc.cached) cacheNOTIFYICONDATAFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, NOTIFYICONDATAFc.cbSize, (jint)lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, NOTIFYICONDATAFc.hWnd, (jint)lpStruct->hWnd);
	(*env)->SetIntField(env, lpObject, NOTIFYICONDATAFc.uID, (jint)lpStruct->uID);
	(*env)->SetIntField(env, lpObject, NOTIFYICONDATAFc.uFlags, (jint)lpStruct->uFlags);
	(*env)->SetIntField(env, lpObject, NOTIFYICONDATAFc.uCallbackMessage, (jint)lpStruct->uCallbackMessage);
	(*env)->SetIntField(env, lpObject, NOTIFYICONDATAFc.hIcon, (jint)lpStruct->hIcon);
#ifndef _WIN32_WCE
	(*env)->SetIntField(env, lpObject, NOTIFYICONDATAFc.dwState, (jint)lpStruct->dwState);
#endif
#ifndef _WIN32_WCE
	(*env)->SetIntField(env, lpObject, NOTIFYICONDATAFc.dwStateMask, (jint)lpStruct->dwStateMask);
#endif
#ifndef _WIN32_WCE
	(*env)->SetIntField(env, lpObject, NOTIFYICONDATAFc.uVersion, (jint)lpStruct->uVersion);
#endif
#ifndef _WIN32_WCE
	(*env)->SetIntField(env, lpObject, NOTIFYICONDATAFc.dwInfoFlags, (jint)lpStruct->dwInfoFlags);
#endif
}
#endif

#ifndef NO_NOTIFYICONDATAA
typedef struct NOTIFYICONDATAA_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID szTip, szInfo, szInfoTitle;
} NOTIFYICONDATAA_FID_CACHE;

NOTIFYICONDATAA_FID_CACHE NOTIFYICONDATAAFc;

void cacheNOTIFYICONDATAAFields(JNIEnv *env, jobject lpObject)
{
	if (NOTIFYICONDATAAFc.cached) return;
	cacheNOTIFYICONDATAFields(env, lpObject);
	NOTIFYICONDATAAFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NOTIFYICONDATAAFc.szTip = (*env)->GetFieldID(env, NOTIFYICONDATAAFc.clazz, "szTip", "[B");
	NOTIFYICONDATAAFc.szInfo = (*env)->GetFieldID(env, NOTIFYICONDATAAFc.clazz, "szInfo", "[B");
	NOTIFYICONDATAAFc.szInfoTitle = (*env)->GetFieldID(env, NOTIFYICONDATAAFc.clazz, "szInfoTitle", "[B");
	NOTIFYICONDATAAFc.cached = 1;
}

NOTIFYICONDATAA *getNOTIFYICONDATAAFields(JNIEnv *env, jobject lpObject, NOTIFYICONDATAA *lpStruct)
{
	if (!NOTIFYICONDATAAFc.cached) cacheNOTIFYICONDATAAFields(env, lpObject);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, NOTIFYICONDATAFc.cbSize);
	lpStruct->hWnd = (HWND)(*env)->GetIntField(env, lpObject, NOTIFYICONDATAFc.hWnd);
	lpStruct->uID = (*env)->GetIntField(env, lpObject, NOTIFYICONDATAFc.uID);
	lpStruct->uFlags = (*env)->GetIntField(env, lpObject, NOTIFYICONDATAFc.uFlags);
	lpStruct->uCallbackMessage = (*env)->GetIntField(env, lpObject, NOTIFYICONDATAFc.uCallbackMessage);
	lpStruct->hIcon = (HICON)(*env)->GetIntField(env, lpObject, NOTIFYICONDATAFc.hIcon);
#ifndef _WIN32_WCE
	lpStruct->dwState = (*env)->GetIntField(env, lpObject, NOTIFYICONDATAFc.dwState);
#endif
#ifndef _WIN32_WCE
	lpStruct->dwStateMask = (*env)->GetIntField(env, lpObject, NOTIFYICONDATAFc.dwStateMask);
#endif
#ifndef _WIN32_WCE
	lpStruct->uVersion = (*env)->GetIntField(env, lpObject, NOTIFYICONDATAFc.uVersion);
#endif
#ifndef _WIN32_WCE
	lpStruct->dwInfoFlags = (*env)->GetIntField(env, lpObject, NOTIFYICONDATAFc.dwInfoFlags);
#endif
	{
	jbyteArray lpObject1 = (jbyteArray)(*env)->GetObjectField(env, lpObject, NOTIFYICONDATAAFc.szTip);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->szTip), (jbyte *)lpStruct->szTip);
	}
#ifndef _WIN32_WCE
	{
	jbyteArray lpObject1 = (jbyteArray)(*env)->GetObjectField(env, lpObject, NOTIFYICONDATAAFc.szInfo);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->szInfo), (jbyte *)lpStruct->szInfo);
	}
#endif
#ifndef _WIN32_WCE
	{
	jbyteArray lpObject1 = (jbyteArray)(*env)->GetObjectField(env, lpObject, NOTIFYICONDATAAFc.szInfoTitle);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->szInfoTitle), (jbyte *)lpStruct->szInfoTitle);
	}
#endif
	return lpStruct;
}

void setNOTIFYICONDATAAFields(JNIEnv *env, jobject lpObject, NOTIFYICONDATAA *lpStruct)
{
	if (!NOTIFYICONDATAAFc.cached) cacheNOTIFYICONDATAAFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, NOTIFYICONDATAFc.cbSize, (jint)lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, NOTIFYICONDATAFc.hWnd, (jint)lpStruct->hWnd);
	(*env)->SetIntField(env, lpObject, NOTIFYICONDATAFc.uID, (jint)lpStruct->uID);
	(*env)->SetIntField(env, lpObject, NOTIFYICONDATAFc.uFlags, (jint)lpStruct->uFlags);
	(*env)->SetIntField(env, lpObject, NOTIFYICONDATAFc.uCallbackMessage, (jint)lpStruct->uCallbackMessage);
	(*env)->SetIntField(env, lpObject, NOTIFYICONDATAFc.hIcon, (jint)lpStruct->hIcon);
#ifndef _WIN32_WCE
	(*env)->SetIntField(env, lpObject, NOTIFYICONDATAFc.dwState, (jint)lpStruct->dwState);
#endif
#ifndef _WIN32_WCE
	(*env)->SetIntField(env, lpObject, NOTIFYICONDATAFc.dwStateMask, (jint)lpStruct->dwStateMask);
#endif
#ifndef _WIN32_WCE
	(*env)->SetIntField(env, lpObject, NOTIFYICONDATAFc.uVersion, (jint)lpStruct->uVersion);
#endif
#ifndef _WIN32_WCE
	(*env)->SetIntField(env, lpObject, NOTIFYICONDATAFc.dwInfoFlags, (jint)lpStruct->dwInfoFlags);
#endif
	{
	jbyteArray lpObject1 = (jbyteArray)(*env)->GetObjectField(env, lpObject, NOTIFYICONDATAAFc.szTip);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->szTip), (jbyte *)lpStruct->szTip);
	}
#ifndef _WIN32_WCE
	{
	jbyteArray lpObject1 = (jbyteArray)(*env)->GetObjectField(env, lpObject, NOTIFYICONDATAAFc.szInfo);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->szInfo), (jbyte *)lpStruct->szInfo);
	}
#endif
#ifndef _WIN32_WCE
	{
	jbyteArray lpObject1 = (jbyteArray)(*env)->GetObjectField(env, lpObject, NOTIFYICONDATAAFc.szInfoTitle);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->szInfoTitle), (jbyte *)lpStruct->szInfoTitle);
	}
#endif
}
#endif

#ifndef NO_NOTIFYICONDATAW
typedef struct NOTIFYICONDATAW_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID szTip, szInfo, szInfoTitle;
} NOTIFYICONDATAW_FID_CACHE;

NOTIFYICONDATAW_FID_CACHE NOTIFYICONDATAWFc;

void cacheNOTIFYICONDATAWFields(JNIEnv *env, jobject lpObject)
{
	if (NOTIFYICONDATAWFc.cached) return;
	cacheNOTIFYICONDATAFields(env, lpObject);
	NOTIFYICONDATAWFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NOTIFYICONDATAWFc.szTip = (*env)->GetFieldID(env, NOTIFYICONDATAWFc.clazz, "szTip", "[C");
	NOTIFYICONDATAWFc.szInfo = (*env)->GetFieldID(env, NOTIFYICONDATAWFc.clazz, "szInfo", "[C");
	NOTIFYICONDATAWFc.szInfoTitle = (*env)->GetFieldID(env, NOTIFYICONDATAWFc.clazz, "szInfoTitle", "[C");
	NOTIFYICONDATAWFc.cached = 1;
}

NOTIFYICONDATAW *getNOTIFYICONDATAWFields(JNIEnv *env, jobject lpObject, NOTIFYICONDATAW *lpStruct)
{
	if (!NOTIFYICONDATAWFc.cached) cacheNOTIFYICONDATAWFields(env, lpObject);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, NOTIFYICONDATAFc.cbSize);
	lpStruct->hWnd = (HWND)(*env)->GetIntField(env, lpObject, NOTIFYICONDATAFc.hWnd);
	lpStruct->uID = (*env)->GetIntField(env, lpObject, NOTIFYICONDATAFc.uID);
	lpStruct->uFlags = (*env)->GetIntField(env, lpObject, NOTIFYICONDATAFc.uFlags);
	lpStruct->uCallbackMessage = (*env)->GetIntField(env, lpObject, NOTIFYICONDATAFc.uCallbackMessage);
	lpStruct->hIcon = (HICON)(*env)->GetIntField(env, lpObject, NOTIFYICONDATAFc.hIcon);
#ifndef _WIN32_WCE
	lpStruct->dwState = (*env)->GetIntField(env, lpObject, NOTIFYICONDATAFc.dwState);
#endif
#ifndef _WIN32_WCE
	lpStruct->dwStateMask = (*env)->GetIntField(env, lpObject, NOTIFYICONDATAFc.dwStateMask);
#endif
#ifndef _WIN32_WCE
	lpStruct->uVersion = (*env)->GetIntField(env, lpObject, NOTIFYICONDATAFc.uVersion);
#endif
#ifndef _WIN32_WCE
	lpStruct->dwInfoFlags = (*env)->GetIntField(env, lpObject, NOTIFYICONDATAFc.dwInfoFlags);
#endif
	{
	jcharArray lpObject1 = (jcharArray)(*env)->GetObjectField(env, lpObject, NOTIFYICONDATAWFc.szTip);
	(*env)->GetCharArrayRegion(env, lpObject1, 0, sizeof(lpStruct->szTip) / 2, (jchar *)lpStruct->szTip);
	}
#ifndef _WIN32_WCE
	{
	jcharArray lpObject1 = (jcharArray)(*env)->GetObjectField(env, lpObject, NOTIFYICONDATAWFc.szInfo);
	(*env)->GetCharArrayRegion(env, lpObject1, 0, sizeof(lpStruct->szInfo) / 2, (jchar *)lpStruct->szInfo);
	}
#endif
#ifndef _WIN32_WCE
	{
	jcharArray lpObject1 = (jcharArray)(*env)->GetObjectField(env, lpObject, NOTIFYICONDATAWFc.szInfoTitle);
	(*env)->GetCharArrayRegion(env, lpObject1, 0, sizeof(lpStruct->szInfoTitle) / 2, (jchar *)lpStruct->szInfoTitle);
	}
#endif
	return lpStruct;
}

void setNOTIFYICONDATAWFields(JNIEnv *env, jobject lpObject, NOTIFYICONDATAW *lpStruct)
{
	if (!NOTIFYICONDATAWFc.cached) cacheNOTIFYICONDATAWFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, NOTIFYICONDATAFc.cbSize, (jint)lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, NOTIFYICONDATAFc.hWnd, (jint)lpStruct->hWnd);
	(*env)->SetIntField(env, lpObject, NOTIFYICONDATAFc.uID, (jint)lpStruct->uID);
	(*env)->SetIntField(env, lpObject, NOTIFYICONDATAFc.uFlags, (jint)lpStruct->uFlags);
	(*env)->SetIntField(env, lpObject, NOTIFYICONDATAFc.uCallbackMessage, (jint)lpStruct->uCallbackMessage);
	(*env)->SetIntField(env, lpObject, NOTIFYICONDATAFc.hIcon, (jint)lpStruct->hIcon);
#ifndef _WIN32_WCE
	(*env)->SetIntField(env, lpObject, NOTIFYICONDATAFc.dwState, (jint)lpStruct->dwState);
#endif
#ifndef _WIN32_WCE
	(*env)->SetIntField(env, lpObject, NOTIFYICONDATAFc.dwStateMask, (jint)lpStruct->dwStateMask);
#endif
#ifndef _WIN32_WCE
	(*env)->SetIntField(env, lpObject, NOTIFYICONDATAFc.uVersion, (jint)lpStruct->uVersion);
#endif
#ifndef _WIN32_WCE
	(*env)->SetIntField(env, lpObject, NOTIFYICONDATAFc.dwInfoFlags, (jint)lpStruct->dwInfoFlags);
#endif
	{
	jcharArray lpObject1 = (jcharArray)(*env)->GetObjectField(env, lpObject, NOTIFYICONDATAWFc.szTip);
	(*env)->SetCharArrayRegion(env, lpObject1, 0, sizeof(lpStruct->szTip) / 2, (jchar *)lpStruct->szTip);
	}
#ifndef _WIN32_WCE
	{
	jcharArray lpObject1 = (jcharArray)(*env)->GetObjectField(env, lpObject, NOTIFYICONDATAWFc.szInfo);
	(*env)->SetCharArrayRegion(env, lpObject1, 0, sizeof(lpStruct->szInfo) / 2, (jchar *)lpStruct->szInfo);
	}
#endif
#ifndef _WIN32_WCE
	{
	jcharArray lpObject1 = (jcharArray)(*env)->GetObjectField(env, lpObject, NOTIFYICONDATAWFc.szInfoTitle);
	(*env)->SetCharArrayRegion(env, lpObject1, 0, sizeof(lpStruct->szInfoTitle) / 2, (jchar *)lpStruct->szInfoTitle);
	}
#endif
}
#endif

#ifndef NO_OPENFILENAME
typedef struct OPENFILENAME_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lStructSize, hwndOwner, hInstance, lpstrFilter, lpstrCustomFilter, nMaxCustFilter, nFilterIndex, lpstrFile, nMaxFile, lpstrFileTitle, nMaxFileTitle, lpstrInitialDir, lpstrTitle, Flags, nFileOffset, nFileExtension, lpstrDefExt, lCustData, lpfnHook, lpTemplateName;
} OPENFILENAME_FID_CACHE;

OPENFILENAME_FID_CACHE OPENFILENAMEFc;

void cacheOPENFILENAMEFields(JNIEnv *env, jobject lpObject)
{
	if (OPENFILENAMEFc.cached) return;
	OPENFILENAMEFc.clazz = (*env)->GetObjectClass(env, lpObject);
	OPENFILENAMEFc.lStructSize = (*env)->GetFieldID(env, OPENFILENAMEFc.clazz, "lStructSize", "I");
	OPENFILENAMEFc.hwndOwner = (*env)->GetFieldID(env, OPENFILENAMEFc.clazz, "hwndOwner", "I");
	OPENFILENAMEFc.hInstance = (*env)->GetFieldID(env, OPENFILENAMEFc.clazz, "hInstance", "I");
	OPENFILENAMEFc.lpstrFilter = (*env)->GetFieldID(env, OPENFILENAMEFc.clazz, "lpstrFilter", "I");
	OPENFILENAMEFc.lpstrCustomFilter = (*env)->GetFieldID(env, OPENFILENAMEFc.clazz, "lpstrCustomFilter", "I");
	OPENFILENAMEFc.nMaxCustFilter = (*env)->GetFieldID(env, OPENFILENAMEFc.clazz, "nMaxCustFilter", "I");
	OPENFILENAMEFc.nFilterIndex = (*env)->GetFieldID(env, OPENFILENAMEFc.clazz, "nFilterIndex", "I");
	OPENFILENAMEFc.lpstrFile = (*env)->GetFieldID(env, OPENFILENAMEFc.clazz, "lpstrFile", "I");
	OPENFILENAMEFc.nMaxFile = (*env)->GetFieldID(env, OPENFILENAMEFc.clazz, "nMaxFile", "I");
	OPENFILENAMEFc.lpstrFileTitle = (*env)->GetFieldID(env, OPENFILENAMEFc.clazz, "lpstrFileTitle", "I");
	OPENFILENAMEFc.nMaxFileTitle = (*env)->GetFieldID(env, OPENFILENAMEFc.clazz, "nMaxFileTitle", "I");
	OPENFILENAMEFc.lpstrInitialDir = (*env)->GetFieldID(env, OPENFILENAMEFc.clazz, "lpstrInitialDir", "I");
	OPENFILENAMEFc.lpstrTitle = (*env)->GetFieldID(env, OPENFILENAMEFc.clazz, "lpstrTitle", "I");
	OPENFILENAMEFc.Flags = (*env)->GetFieldID(env, OPENFILENAMEFc.clazz, "Flags", "I");
	OPENFILENAMEFc.nFileOffset = (*env)->GetFieldID(env, OPENFILENAMEFc.clazz, "nFileOffset", "S");
	OPENFILENAMEFc.nFileExtension = (*env)->GetFieldID(env, OPENFILENAMEFc.clazz, "nFileExtension", "S");
	OPENFILENAMEFc.lpstrDefExt = (*env)->GetFieldID(env, OPENFILENAMEFc.clazz, "lpstrDefExt", "I");
	OPENFILENAMEFc.lCustData = (*env)->GetFieldID(env, OPENFILENAMEFc.clazz, "lCustData", "I");
	OPENFILENAMEFc.lpfnHook = (*env)->GetFieldID(env, OPENFILENAMEFc.clazz, "lpfnHook", "I");
	OPENFILENAMEFc.lpTemplateName = (*env)->GetFieldID(env, OPENFILENAMEFc.clazz, "lpTemplateName", "I");
	OPENFILENAMEFc.cached = 1;
}

OPENFILENAME *getOPENFILENAMEFields(JNIEnv *env, jobject lpObject, OPENFILENAME *lpStruct)
{
	if (!OPENFILENAMEFc.cached) cacheOPENFILENAMEFields(env, lpObject);
	lpStruct->lStructSize = (*env)->GetIntField(env, lpObject, OPENFILENAMEFc.lStructSize);
	lpStruct->hwndOwner = (HWND)(*env)->GetIntField(env, lpObject, OPENFILENAMEFc.hwndOwner);
	lpStruct->hInstance = (HINSTANCE)(*env)->GetIntField(env, lpObject, OPENFILENAMEFc.hInstance);
	lpStruct->lpstrFilter = (LPCTSTR)(*env)->GetIntField(env, lpObject, OPENFILENAMEFc.lpstrFilter);
	lpStruct->lpstrCustomFilter = (LPTSTR)(*env)->GetIntField(env, lpObject, OPENFILENAMEFc.lpstrCustomFilter);
	lpStruct->nMaxCustFilter = (*env)->GetIntField(env, lpObject, OPENFILENAMEFc.nMaxCustFilter);
	lpStruct->nFilterIndex = (*env)->GetIntField(env, lpObject, OPENFILENAMEFc.nFilterIndex);
	lpStruct->lpstrFile = (LPTSTR)(*env)->GetIntField(env, lpObject, OPENFILENAMEFc.lpstrFile);
	lpStruct->nMaxFile = (*env)->GetIntField(env, lpObject, OPENFILENAMEFc.nMaxFile);
	lpStruct->lpstrFileTitle = (LPTSTR)(*env)->GetIntField(env, lpObject, OPENFILENAMEFc.lpstrFileTitle);
	lpStruct->nMaxFileTitle = (*env)->GetIntField(env, lpObject, OPENFILENAMEFc.nMaxFileTitle);
	lpStruct->lpstrInitialDir = (LPCTSTR)(*env)->GetIntField(env, lpObject, OPENFILENAMEFc.lpstrInitialDir);
	lpStruct->lpstrTitle = (LPCTSTR)(*env)->GetIntField(env, lpObject, OPENFILENAMEFc.lpstrTitle);
	lpStruct->Flags = (*env)->GetIntField(env, lpObject, OPENFILENAMEFc.Flags);
	lpStruct->nFileOffset = (*env)->GetShortField(env, lpObject, OPENFILENAMEFc.nFileOffset);
	lpStruct->nFileExtension = (*env)->GetShortField(env, lpObject, OPENFILENAMEFc.nFileExtension);
	lpStruct->lpstrDefExt = (LPCTSTR)(*env)->GetIntField(env, lpObject, OPENFILENAMEFc.lpstrDefExt);
	lpStruct->lCustData = (*env)->GetIntField(env, lpObject, OPENFILENAMEFc.lCustData);
	lpStruct->lpfnHook = (LPOFNHOOKPROC)(*env)->GetIntField(env, lpObject, OPENFILENAMEFc.lpfnHook);
	lpStruct->lpTemplateName = (LPCTSTR)(*env)->GetIntField(env, lpObject, OPENFILENAMEFc.lpTemplateName);
	return lpStruct;
}

void setOPENFILENAMEFields(JNIEnv *env, jobject lpObject, OPENFILENAME *lpStruct)
{
	if (!OPENFILENAMEFc.cached) cacheOPENFILENAMEFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, OPENFILENAMEFc.lStructSize, (jint)lpStruct->lStructSize);
	(*env)->SetIntField(env, lpObject, OPENFILENAMEFc.hwndOwner, (jint)lpStruct->hwndOwner);
	(*env)->SetIntField(env, lpObject, OPENFILENAMEFc.hInstance, (jint)lpStruct->hInstance);
	(*env)->SetIntField(env, lpObject, OPENFILENAMEFc.lpstrFilter, (jint)lpStruct->lpstrFilter);
	(*env)->SetIntField(env, lpObject, OPENFILENAMEFc.lpstrCustomFilter, (jint)lpStruct->lpstrCustomFilter);
	(*env)->SetIntField(env, lpObject, OPENFILENAMEFc.nMaxCustFilter, (jint)lpStruct->nMaxCustFilter);
	(*env)->SetIntField(env, lpObject, OPENFILENAMEFc.nFilterIndex, (jint)lpStruct->nFilterIndex);
	(*env)->SetIntField(env, lpObject, OPENFILENAMEFc.lpstrFile, (jint)lpStruct->lpstrFile);
	(*env)->SetIntField(env, lpObject, OPENFILENAMEFc.nMaxFile, (jint)lpStruct->nMaxFile);
	(*env)->SetIntField(env, lpObject, OPENFILENAMEFc.lpstrFileTitle, (jint)lpStruct->lpstrFileTitle);
	(*env)->SetIntField(env, lpObject, OPENFILENAMEFc.nMaxFileTitle, (jint)lpStruct->nMaxFileTitle);
	(*env)->SetIntField(env, lpObject, OPENFILENAMEFc.lpstrInitialDir, (jint)lpStruct->lpstrInitialDir);
	(*env)->SetIntField(env, lpObject, OPENFILENAMEFc.lpstrTitle, (jint)lpStruct->lpstrTitle);
	(*env)->SetIntField(env, lpObject, OPENFILENAMEFc.Flags, (jint)lpStruct->Flags);
	(*env)->SetShortField(env, lpObject, OPENFILENAMEFc.nFileOffset, (jshort)lpStruct->nFileOffset);
	(*env)->SetShortField(env, lpObject, OPENFILENAMEFc.nFileExtension, (jshort)lpStruct->nFileExtension);
	(*env)->SetIntField(env, lpObject, OPENFILENAMEFc.lpstrDefExt, (jint)lpStruct->lpstrDefExt);
	(*env)->SetIntField(env, lpObject, OPENFILENAMEFc.lCustData, (jint)lpStruct->lCustData);
	(*env)->SetIntField(env, lpObject, OPENFILENAMEFc.lpfnHook, (jint)lpStruct->lpfnHook);
	(*env)->SetIntField(env, lpObject, OPENFILENAMEFc.lpTemplateName, (jint)lpStruct->lpTemplateName);
}
#endif

#ifndef NO_OSVERSIONINFO
typedef struct OSVERSIONINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID dwOSVersionInfoSize, dwMajorVersion, dwMinorVersion, dwBuildNumber, dwPlatformId;
} OSVERSIONINFO_FID_CACHE;

OSVERSIONINFO_FID_CACHE OSVERSIONINFOFc;

void cacheOSVERSIONINFOFields(JNIEnv *env, jobject lpObject)
{
	if (OSVERSIONINFOFc.cached) return;
	OSVERSIONINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	OSVERSIONINFOFc.dwOSVersionInfoSize = (*env)->GetFieldID(env, OSVERSIONINFOFc.clazz, "dwOSVersionInfoSize", "I");
	OSVERSIONINFOFc.dwMajorVersion = (*env)->GetFieldID(env, OSVERSIONINFOFc.clazz, "dwMajorVersion", "I");
	OSVERSIONINFOFc.dwMinorVersion = (*env)->GetFieldID(env, OSVERSIONINFOFc.clazz, "dwMinorVersion", "I");
	OSVERSIONINFOFc.dwBuildNumber = (*env)->GetFieldID(env, OSVERSIONINFOFc.clazz, "dwBuildNumber", "I");
	OSVERSIONINFOFc.dwPlatformId = (*env)->GetFieldID(env, OSVERSIONINFOFc.clazz, "dwPlatformId", "I");
	OSVERSIONINFOFc.cached = 1;
}

OSVERSIONINFO *getOSVERSIONINFOFields(JNIEnv *env, jobject lpObject, OSVERSIONINFO *lpStruct)
{
	if (!OSVERSIONINFOFc.cached) cacheOSVERSIONINFOFields(env, lpObject);
	lpStruct->dwOSVersionInfoSize = (*env)->GetIntField(env, lpObject, OSVERSIONINFOFc.dwOSVersionInfoSize);
	lpStruct->dwMajorVersion = (*env)->GetIntField(env, lpObject, OSVERSIONINFOFc.dwMajorVersion);
	lpStruct->dwMinorVersion = (*env)->GetIntField(env, lpObject, OSVERSIONINFOFc.dwMinorVersion);
	lpStruct->dwBuildNumber = (*env)->GetIntField(env, lpObject, OSVERSIONINFOFc.dwBuildNumber);
	lpStruct->dwPlatformId = (*env)->GetIntField(env, lpObject, OSVERSIONINFOFc.dwPlatformId);
	return lpStruct;
}

void setOSVERSIONINFOFields(JNIEnv *env, jobject lpObject, OSVERSIONINFO *lpStruct)
{
	if (!OSVERSIONINFOFc.cached) cacheOSVERSIONINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, OSVERSIONINFOFc.dwOSVersionInfoSize, (jint)lpStruct->dwOSVersionInfoSize);
	(*env)->SetIntField(env, lpObject, OSVERSIONINFOFc.dwMajorVersion, (jint)lpStruct->dwMajorVersion);
	(*env)->SetIntField(env, lpObject, OSVERSIONINFOFc.dwMinorVersion, (jint)lpStruct->dwMinorVersion);
	(*env)->SetIntField(env, lpObject, OSVERSIONINFOFc.dwBuildNumber, (jint)lpStruct->dwBuildNumber);
	(*env)->SetIntField(env, lpObject, OSVERSIONINFOFc.dwPlatformId, (jint)lpStruct->dwPlatformId);
}
#endif

#ifndef NO_OSVERSIONINFOA
typedef struct OSVERSIONINFOA_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID szCSDVersion;
} OSVERSIONINFOA_FID_CACHE;

OSVERSIONINFOA_FID_CACHE OSVERSIONINFOAFc;

void cacheOSVERSIONINFOAFields(JNIEnv *env, jobject lpObject)
{
	if (OSVERSIONINFOAFc.cached) return;
	cacheOSVERSIONINFOFields(env, lpObject);
	OSVERSIONINFOAFc.clazz = (*env)->GetObjectClass(env, lpObject);
	OSVERSIONINFOAFc.szCSDVersion = (*env)->GetFieldID(env, OSVERSIONINFOAFc.clazz, "szCSDVersion", "[B");
	OSVERSIONINFOAFc.cached = 1;
}

OSVERSIONINFOA *getOSVERSIONINFOAFields(JNIEnv *env, jobject lpObject, OSVERSIONINFOA *lpStruct)
{
	if (!OSVERSIONINFOAFc.cached) cacheOSVERSIONINFOAFields(env, lpObject);
	lpStruct->dwOSVersionInfoSize = (*env)->GetIntField(env, lpObject, OSVERSIONINFOFc.dwOSVersionInfoSize);
	lpStruct->dwMajorVersion = (*env)->GetIntField(env, lpObject, OSVERSIONINFOFc.dwMajorVersion);
	lpStruct->dwMinorVersion = (*env)->GetIntField(env, lpObject, OSVERSIONINFOFc.dwMinorVersion);
	lpStruct->dwBuildNumber = (*env)->GetIntField(env, lpObject, OSVERSIONINFOFc.dwBuildNumber);
	lpStruct->dwPlatformId = (*env)->GetIntField(env, lpObject, OSVERSIONINFOFc.dwPlatformId);
	{
	jbyteArray lpObject1 = (jbyteArray)(*env)->GetObjectField(env, lpObject, OSVERSIONINFOAFc.szCSDVersion);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->szCSDVersion), (jbyte *)lpStruct->szCSDVersion);
	}
	return lpStruct;
}

void setOSVERSIONINFOAFields(JNIEnv *env, jobject lpObject, OSVERSIONINFOA *lpStruct)
{
	if (!OSVERSIONINFOAFc.cached) cacheOSVERSIONINFOAFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, OSVERSIONINFOFc.dwOSVersionInfoSize, (jint)lpStruct->dwOSVersionInfoSize);
	(*env)->SetIntField(env, lpObject, OSVERSIONINFOFc.dwMajorVersion, (jint)lpStruct->dwMajorVersion);
	(*env)->SetIntField(env, lpObject, OSVERSIONINFOFc.dwMinorVersion, (jint)lpStruct->dwMinorVersion);
	(*env)->SetIntField(env, lpObject, OSVERSIONINFOFc.dwBuildNumber, (jint)lpStruct->dwBuildNumber);
	(*env)->SetIntField(env, lpObject, OSVERSIONINFOFc.dwPlatformId, (jint)lpStruct->dwPlatformId);
	{
	jbyteArray lpObject1 = (jbyteArray)(*env)->GetObjectField(env, lpObject, OSVERSIONINFOAFc.szCSDVersion);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->szCSDVersion), (jbyte *)lpStruct->szCSDVersion);
	}
}
#endif

#ifndef NO_OSVERSIONINFOW
typedef struct OSVERSIONINFOW_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID szCSDVersion;
} OSVERSIONINFOW_FID_CACHE;

OSVERSIONINFOW_FID_CACHE OSVERSIONINFOWFc;

void cacheOSVERSIONINFOWFields(JNIEnv *env, jobject lpObject)
{
	if (OSVERSIONINFOWFc.cached) return;
	cacheOSVERSIONINFOFields(env, lpObject);
	OSVERSIONINFOWFc.clazz = (*env)->GetObjectClass(env, lpObject);
	OSVERSIONINFOWFc.szCSDVersion = (*env)->GetFieldID(env, OSVERSIONINFOWFc.clazz, "szCSDVersion", "[C");
	OSVERSIONINFOWFc.cached = 1;
}

OSVERSIONINFOW *getOSVERSIONINFOWFields(JNIEnv *env, jobject lpObject, OSVERSIONINFOW *lpStruct)
{
	if (!OSVERSIONINFOWFc.cached) cacheOSVERSIONINFOWFields(env, lpObject);
	lpStruct->dwOSVersionInfoSize = (*env)->GetIntField(env, lpObject, OSVERSIONINFOFc.dwOSVersionInfoSize);
	lpStruct->dwMajorVersion = (*env)->GetIntField(env, lpObject, OSVERSIONINFOFc.dwMajorVersion);
	lpStruct->dwMinorVersion = (*env)->GetIntField(env, lpObject, OSVERSIONINFOFc.dwMinorVersion);
	lpStruct->dwBuildNumber = (*env)->GetIntField(env, lpObject, OSVERSIONINFOFc.dwBuildNumber);
	lpStruct->dwPlatformId = (*env)->GetIntField(env, lpObject, OSVERSIONINFOFc.dwPlatformId);
	{
	jcharArray lpObject1 = (jcharArray)(*env)->GetObjectField(env, lpObject, OSVERSIONINFOWFc.szCSDVersion);
	(*env)->GetCharArrayRegion(env, lpObject1, 0, sizeof(lpStruct->szCSDVersion) / 2, (jchar *)lpStruct->szCSDVersion);
	}
	return lpStruct;
}

void setOSVERSIONINFOWFields(JNIEnv *env, jobject lpObject, OSVERSIONINFOW *lpStruct)
{
	if (!OSVERSIONINFOWFc.cached) cacheOSVERSIONINFOWFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, OSVERSIONINFOFc.dwOSVersionInfoSize, (jint)lpStruct->dwOSVersionInfoSize);
	(*env)->SetIntField(env, lpObject, OSVERSIONINFOFc.dwMajorVersion, (jint)lpStruct->dwMajorVersion);
	(*env)->SetIntField(env, lpObject, OSVERSIONINFOFc.dwMinorVersion, (jint)lpStruct->dwMinorVersion);
	(*env)->SetIntField(env, lpObject, OSVERSIONINFOFc.dwBuildNumber, (jint)lpStruct->dwBuildNumber);
	(*env)->SetIntField(env, lpObject, OSVERSIONINFOFc.dwPlatformId, (jint)lpStruct->dwPlatformId);
	{
	jcharArray lpObject1 = (jcharArray)(*env)->GetObjectField(env, lpObject, OSVERSIONINFOWFc.szCSDVersion);
	(*env)->SetCharArrayRegion(env, lpObject1, 0, sizeof(lpStruct->szCSDVersion) / 2, (jchar *)lpStruct->szCSDVersion);
	}
}
#endif

#ifndef NO_PAINTSTRUCT
typedef struct PAINTSTRUCT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID hdc, fErase, left, top, right, bottom, fRestore, fIncUpdate, rgbReserved;
} PAINTSTRUCT_FID_CACHE;

PAINTSTRUCT_FID_CACHE PAINTSTRUCTFc;

void cachePAINTSTRUCTFields(JNIEnv *env, jobject lpObject)
{
	if (PAINTSTRUCTFc.cached) return;
	PAINTSTRUCTFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PAINTSTRUCTFc.hdc = (*env)->GetFieldID(env, PAINTSTRUCTFc.clazz, "hdc", "I");
	PAINTSTRUCTFc.fErase = (*env)->GetFieldID(env, PAINTSTRUCTFc.clazz, "fErase", "Z");
	PAINTSTRUCTFc.left = (*env)->GetFieldID(env, PAINTSTRUCTFc.clazz, "left", "I");
	PAINTSTRUCTFc.top = (*env)->GetFieldID(env, PAINTSTRUCTFc.clazz, "top", "I");
	PAINTSTRUCTFc.right = (*env)->GetFieldID(env, PAINTSTRUCTFc.clazz, "right", "I");
	PAINTSTRUCTFc.bottom = (*env)->GetFieldID(env, PAINTSTRUCTFc.clazz, "bottom", "I");
	PAINTSTRUCTFc.fRestore = (*env)->GetFieldID(env, PAINTSTRUCTFc.clazz, "fRestore", "Z");
	PAINTSTRUCTFc.fIncUpdate = (*env)->GetFieldID(env, PAINTSTRUCTFc.clazz, "fIncUpdate", "Z");
	PAINTSTRUCTFc.rgbReserved = (*env)->GetFieldID(env, PAINTSTRUCTFc.clazz, "rgbReserved", "[B");
	PAINTSTRUCTFc.cached = 1;
}

PAINTSTRUCT *getPAINTSTRUCTFields(JNIEnv *env, jobject lpObject, PAINTSTRUCT *lpStruct)
{
	if (!PAINTSTRUCTFc.cached) cachePAINTSTRUCTFields(env, lpObject);
	lpStruct->hdc = (HDC)(*env)->GetIntField(env, lpObject, PAINTSTRUCTFc.hdc);
	lpStruct->fErase = (*env)->GetBooleanField(env, lpObject, PAINTSTRUCTFc.fErase);
	lpStruct->rcPaint.left = (*env)->GetIntField(env, lpObject, PAINTSTRUCTFc.left);
	lpStruct->rcPaint.top = (*env)->GetIntField(env, lpObject, PAINTSTRUCTFc.top);
	lpStruct->rcPaint.right = (*env)->GetIntField(env, lpObject, PAINTSTRUCTFc.right);
	lpStruct->rcPaint.bottom = (*env)->GetIntField(env, lpObject, PAINTSTRUCTFc.bottom);
	lpStruct->fRestore = (*env)->GetBooleanField(env, lpObject, PAINTSTRUCTFc.fRestore);
	lpStruct->fIncUpdate = (*env)->GetBooleanField(env, lpObject, PAINTSTRUCTFc.fIncUpdate);
	{
	jbyteArray lpObject1 = (jbyteArray)(*env)->GetObjectField(env, lpObject, PAINTSTRUCTFc.rgbReserved);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->rgbReserved), (jbyte *)lpStruct->rgbReserved);
	}
	return lpStruct;
}

void setPAINTSTRUCTFields(JNIEnv *env, jobject lpObject, PAINTSTRUCT *lpStruct)
{
	if (!PAINTSTRUCTFc.cached) cachePAINTSTRUCTFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, PAINTSTRUCTFc.hdc, (jint)lpStruct->hdc);
	(*env)->SetBooleanField(env, lpObject, PAINTSTRUCTFc.fErase, (jboolean)lpStruct->fErase);
	(*env)->SetIntField(env, lpObject, PAINTSTRUCTFc.left, (jint)lpStruct->rcPaint.left);
	(*env)->SetIntField(env, lpObject, PAINTSTRUCTFc.top, (jint)lpStruct->rcPaint.top);
	(*env)->SetIntField(env, lpObject, PAINTSTRUCTFc.right, (jint)lpStruct->rcPaint.right);
	(*env)->SetIntField(env, lpObject, PAINTSTRUCTFc.bottom, (jint)lpStruct->rcPaint.bottom);
	(*env)->SetBooleanField(env, lpObject, PAINTSTRUCTFc.fRestore, (jboolean)lpStruct->fRestore);
	(*env)->SetBooleanField(env, lpObject, PAINTSTRUCTFc.fIncUpdate, (jboolean)lpStruct->fIncUpdate);
	{
	jbyteArray lpObject1 = (jbyteArray)(*env)->GetObjectField(env, lpObject, PAINTSTRUCTFc.rgbReserved);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->rgbReserved), (jbyte *)lpStruct->rgbReserved);
	}
}
#endif

#ifndef NO_POINT
typedef struct POINT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID x, y;
} POINT_FID_CACHE;

POINT_FID_CACHE POINTFc;

void cachePOINTFields(JNIEnv *env, jobject lpObject)
{
	if (POINTFc.cached) return;
	POINTFc.clazz = (*env)->GetObjectClass(env, lpObject);
	POINTFc.x = (*env)->GetFieldID(env, POINTFc.clazz, "x", "I");
	POINTFc.y = (*env)->GetFieldID(env, POINTFc.clazz, "y", "I");
	POINTFc.cached = 1;
}

POINT *getPOINTFields(JNIEnv *env, jobject lpObject, POINT *lpStruct)
{
	if (!POINTFc.cached) cachePOINTFields(env, lpObject);
	lpStruct->x = (*env)->GetIntField(env, lpObject, POINTFc.x);
	lpStruct->y = (*env)->GetIntField(env, lpObject, POINTFc.y);
	return lpStruct;
}

void setPOINTFields(JNIEnv *env, jobject lpObject, POINT *lpStruct)
{
	if (!POINTFc.cached) cachePOINTFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, POINTFc.x, (jint)lpStruct->x);
	(*env)->SetIntField(env, lpObject, POINTFc.y, (jint)lpStruct->y);
}
#endif

#ifndef NO_PRINTDLG
typedef struct PRINTDLG_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lStructSize, hwndOwner, hDevMode, hDevNames, hDC, Flags, nFromPage, nToPage, nMinPage, nMaxPage, nCopies, hInstance, lCustData, lpfnPrintHook, lpfnSetupHook, lpPrintTemplateName, lpSetupTemplateName, hPrintTemplate, hSetupTemplate;
} PRINTDLG_FID_CACHE;

PRINTDLG_FID_CACHE PRINTDLGFc;

void cachePRINTDLGFields(JNIEnv *env, jobject lpObject)
{
	if (PRINTDLGFc.cached) return;
	PRINTDLGFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PRINTDLGFc.lStructSize = (*env)->GetFieldID(env, PRINTDLGFc.clazz, "lStructSize", "I");
	PRINTDLGFc.hwndOwner = (*env)->GetFieldID(env, PRINTDLGFc.clazz, "hwndOwner", "I");
	PRINTDLGFc.hDevMode = (*env)->GetFieldID(env, PRINTDLGFc.clazz, "hDevMode", "I");
	PRINTDLGFc.hDevNames = (*env)->GetFieldID(env, PRINTDLGFc.clazz, "hDevNames", "I");
	PRINTDLGFc.hDC = (*env)->GetFieldID(env, PRINTDLGFc.clazz, "hDC", "I");
	PRINTDLGFc.Flags = (*env)->GetFieldID(env, PRINTDLGFc.clazz, "Flags", "I");
	PRINTDLGFc.nFromPage = (*env)->GetFieldID(env, PRINTDLGFc.clazz, "nFromPage", "S");
	PRINTDLGFc.nToPage = (*env)->GetFieldID(env, PRINTDLGFc.clazz, "nToPage", "S");
	PRINTDLGFc.nMinPage = (*env)->GetFieldID(env, PRINTDLGFc.clazz, "nMinPage", "S");
	PRINTDLGFc.nMaxPage = (*env)->GetFieldID(env, PRINTDLGFc.clazz, "nMaxPage", "S");
	PRINTDLGFc.nCopies = (*env)->GetFieldID(env, PRINTDLGFc.clazz, "nCopies", "S");
	PRINTDLGFc.hInstance = (*env)->GetFieldID(env, PRINTDLGFc.clazz, "hInstance", "I");
	PRINTDLGFc.lCustData = (*env)->GetFieldID(env, PRINTDLGFc.clazz, "lCustData", "I");
	PRINTDLGFc.lpfnPrintHook = (*env)->GetFieldID(env, PRINTDLGFc.clazz, "lpfnPrintHook", "I");
	PRINTDLGFc.lpfnSetupHook = (*env)->GetFieldID(env, PRINTDLGFc.clazz, "lpfnSetupHook", "I");
	PRINTDLGFc.lpPrintTemplateName = (*env)->GetFieldID(env, PRINTDLGFc.clazz, "lpPrintTemplateName", "I");
	PRINTDLGFc.lpSetupTemplateName = (*env)->GetFieldID(env, PRINTDLGFc.clazz, "lpSetupTemplateName", "I");
	PRINTDLGFc.hPrintTemplate = (*env)->GetFieldID(env, PRINTDLGFc.clazz, "hPrintTemplate", "I");
	PRINTDLGFc.hSetupTemplate = (*env)->GetFieldID(env, PRINTDLGFc.clazz, "hSetupTemplate", "I");
	PRINTDLGFc.cached = 1;
}

PRINTDLG *getPRINTDLGFields(JNIEnv *env, jobject lpObject, PRINTDLG *lpStruct)
{
	if (!PRINTDLGFc.cached) cachePRINTDLGFields(env, lpObject);
	lpStruct->lStructSize = (*env)->GetIntField(env, lpObject, PRINTDLGFc.lStructSize);
	lpStruct->hwndOwner = (HWND)(*env)->GetIntField(env, lpObject, PRINTDLGFc.hwndOwner);
	lpStruct->hDevMode = (HGLOBAL)(*env)->GetIntField(env, lpObject, PRINTDLGFc.hDevMode);
	lpStruct->hDevNames = (HGLOBAL)(*env)->GetIntField(env, lpObject, PRINTDLGFc.hDevNames);
	lpStruct->hDC = (HDC)(*env)->GetIntField(env, lpObject, PRINTDLGFc.hDC);
	lpStruct->Flags = (*env)->GetIntField(env, lpObject, PRINTDLGFc.Flags);
	lpStruct->nFromPage = (*env)->GetShortField(env, lpObject, PRINTDLGFc.nFromPage);
	lpStruct->nToPage = (*env)->GetShortField(env, lpObject, PRINTDLGFc.nToPage);
	lpStruct->nMinPage = (*env)->GetShortField(env, lpObject, PRINTDLGFc.nMinPage);
	lpStruct->nMaxPage = (*env)->GetShortField(env, lpObject, PRINTDLGFc.nMaxPage);
	lpStruct->nCopies = (*env)->GetShortField(env, lpObject, PRINTDLGFc.nCopies);
	lpStruct->hInstance = (HINSTANCE)(*env)->GetIntField(env, lpObject, PRINTDLGFc.hInstance);
	lpStruct->lCustData = (*env)->GetIntField(env, lpObject, PRINTDLGFc.lCustData);
	lpStruct->lpfnPrintHook = (LPPRINTHOOKPROC)(*env)->GetIntField(env, lpObject, PRINTDLGFc.lpfnPrintHook);
	lpStruct->lpfnSetupHook = (LPPRINTHOOKPROC)(*env)->GetIntField(env, lpObject, PRINTDLGFc.lpfnSetupHook);
	lpStruct->lpPrintTemplateName = (LPCTSTR)(*env)->GetIntField(env, lpObject, PRINTDLGFc.lpPrintTemplateName);
	lpStruct->lpSetupTemplateName = (LPCTSTR)(*env)->GetIntField(env, lpObject, PRINTDLGFc.lpSetupTemplateName);
	lpStruct->hPrintTemplate = (HGLOBAL)(*env)->GetIntField(env, lpObject, PRINTDLGFc.hPrintTemplate);
	lpStruct->hSetupTemplate = (HGLOBAL)(*env)->GetIntField(env, lpObject, PRINTDLGFc.hSetupTemplate);
	return lpStruct;
}

void setPRINTDLGFields(JNIEnv *env, jobject lpObject, PRINTDLG *lpStruct)
{
	if (!PRINTDLGFc.cached) cachePRINTDLGFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, PRINTDLGFc.lStructSize, (jint)lpStruct->lStructSize);
	(*env)->SetIntField(env, lpObject, PRINTDLGFc.hwndOwner, (jint)lpStruct->hwndOwner);
	(*env)->SetIntField(env, lpObject, PRINTDLGFc.hDevMode, (jint)lpStruct->hDevMode);
	(*env)->SetIntField(env, lpObject, PRINTDLGFc.hDevNames, (jint)lpStruct->hDevNames);
	(*env)->SetIntField(env, lpObject, PRINTDLGFc.hDC, (jint)lpStruct->hDC);
	(*env)->SetIntField(env, lpObject, PRINTDLGFc.Flags, (jint)lpStruct->Flags);
	(*env)->SetShortField(env, lpObject, PRINTDLGFc.nFromPage, (jshort)lpStruct->nFromPage);
	(*env)->SetShortField(env, lpObject, PRINTDLGFc.nToPage, (jshort)lpStruct->nToPage);
	(*env)->SetShortField(env, lpObject, PRINTDLGFc.nMinPage, (jshort)lpStruct->nMinPage);
	(*env)->SetShortField(env, lpObject, PRINTDLGFc.nMaxPage, (jshort)lpStruct->nMaxPage);
	(*env)->SetShortField(env, lpObject, PRINTDLGFc.nCopies, (jshort)lpStruct->nCopies);
	(*env)->SetIntField(env, lpObject, PRINTDLGFc.hInstance, (jint)lpStruct->hInstance);
	(*env)->SetIntField(env, lpObject, PRINTDLGFc.lCustData, (jint)lpStruct->lCustData);
	(*env)->SetIntField(env, lpObject, PRINTDLGFc.lpfnPrintHook, (jint)lpStruct->lpfnPrintHook);
	(*env)->SetIntField(env, lpObject, PRINTDLGFc.lpfnSetupHook, (jint)lpStruct->lpfnSetupHook);
	(*env)->SetIntField(env, lpObject, PRINTDLGFc.lpPrintTemplateName, (jint)lpStruct->lpPrintTemplateName);
	(*env)->SetIntField(env, lpObject, PRINTDLGFc.lpSetupTemplateName, (jint)lpStruct->lpSetupTemplateName);
	(*env)->SetIntField(env, lpObject, PRINTDLGFc.hPrintTemplate, (jint)lpStruct->hPrintTemplate);
	(*env)->SetIntField(env, lpObject, PRINTDLGFc.hSetupTemplate, (jint)lpStruct->hSetupTemplate);
}
#endif

#ifndef NO_REBARBANDINFO
typedef struct REBARBANDINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, fMask, fStyle, clrFore, clrBack, lpText, cch, iImage, hwndChild, cxMinChild, cyMinChild, cx, hbmBack, wID, cyChild, cyMaxChild, cyIntegral, cxIdeal, lParam, cxHeader;
} REBARBANDINFO_FID_CACHE;

REBARBANDINFO_FID_CACHE REBARBANDINFOFc;

void cacheREBARBANDINFOFields(JNIEnv *env, jobject lpObject)
{
	if (REBARBANDINFOFc.cached) return;
	REBARBANDINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	REBARBANDINFOFc.cbSize = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "cbSize", "I");
	REBARBANDINFOFc.fMask = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "fMask", "I");
	REBARBANDINFOFc.fStyle = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "fStyle", "I");
	REBARBANDINFOFc.clrFore = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "clrFore", "I");
	REBARBANDINFOFc.clrBack = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "clrBack", "I");
	REBARBANDINFOFc.lpText = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "lpText", "I");
	REBARBANDINFOFc.cch = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "cch", "I");
	REBARBANDINFOFc.iImage = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "iImage", "I");
	REBARBANDINFOFc.hwndChild = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "hwndChild", "I");
	REBARBANDINFOFc.cxMinChild = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "cxMinChild", "I");
	REBARBANDINFOFc.cyMinChild = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "cyMinChild", "I");
	REBARBANDINFOFc.cx = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "cx", "I");
	REBARBANDINFOFc.hbmBack = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "hbmBack", "I");
	REBARBANDINFOFc.wID = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "wID", "I");
	REBARBANDINFOFc.cyChild = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "cyChild", "I");
	REBARBANDINFOFc.cyMaxChild = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "cyMaxChild", "I");
	REBARBANDINFOFc.cyIntegral = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "cyIntegral", "I");
	REBARBANDINFOFc.cxIdeal = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "cxIdeal", "I");
	REBARBANDINFOFc.lParam = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "lParam", "I");
	REBARBANDINFOFc.cxHeader = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "cxHeader", "I");
	REBARBANDINFOFc.cached = 1;
}

REBARBANDINFO *getREBARBANDINFOFields(JNIEnv *env, jobject lpObject, REBARBANDINFO *lpStruct)
{
	if (!REBARBANDINFOFc.cached) cacheREBARBANDINFOFields(env, lpObject);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, REBARBANDINFOFc.cbSize);
	lpStruct->fMask = (*env)->GetIntField(env, lpObject, REBARBANDINFOFc.fMask);
	lpStruct->fStyle = (*env)->GetIntField(env, lpObject, REBARBANDINFOFc.fStyle);
	lpStruct->clrFore = (*env)->GetIntField(env, lpObject, REBARBANDINFOFc.clrFore);
	lpStruct->clrBack = (*env)->GetIntField(env, lpObject, REBARBANDINFOFc.clrBack);
	lpStruct->lpText = (LPTSTR)(*env)->GetIntField(env, lpObject, REBARBANDINFOFc.lpText);
	lpStruct->cch = (*env)->GetIntField(env, lpObject, REBARBANDINFOFc.cch);
	lpStruct->iImage = (*env)->GetIntField(env, lpObject, REBARBANDINFOFc.iImage);
	lpStruct->hwndChild = (HWND)(*env)->GetIntField(env, lpObject, REBARBANDINFOFc.hwndChild);
	lpStruct->cxMinChild = (*env)->GetIntField(env, lpObject, REBARBANDINFOFc.cxMinChild);
	lpStruct->cyMinChild = (*env)->GetIntField(env, lpObject, REBARBANDINFOFc.cyMinChild);
	lpStruct->cx = (*env)->GetIntField(env, lpObject, REBARBANDINFOFc.cx);
	lpStruct->hbmBack = (HBITMAP)(*env)->GetIntField(env, lpObject, REBARBANDINFOFc.hbmBack);
	lpStruct->wID = (*env)->GetIntField(env, lpObject, REBARBANDINFOFc.wID);
	lpStruct->cyChild = (*env)->GetIntField(env, lpObject, REBARBANDINFOFc.cyChild);
	lpStruct->cyMaxChild = (*env)->GetIntField(env, lpObject, REBARBANDINFOFc.cyMaxChild);
	lpStruct->cyIntegral = (*env)->GetIntField(env, lpObject, REBARBANDINFOFc.cyIntegral);
	lpStruct->cxIdeal = (*env)->GetIntField(env, lpObject, REBARBANDINFOFc.cxIdeal);
	lpStruct->lParam = (*env)->GetIntField(env, lpObject, REBARBANDINFOFc.lParam);
#ifndef _WIN32_WCE
	lpStruct->cxHeader = (*env)->GetIntField(env, lpObject, REBARBANDINFOFc.cxHeader);
#endif
	return lpStruct;
}

void setREBARBANDINFOFields(JNIEnv *env, jobject lpObject, REBARBANDINFO *lpStruct)
{
	if (!REBARBANDINFOFc.cached) cacheREBARBANDINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, REBARBANDINFOFc.cbSize, (jint)lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, REBARBANDINFOFc.fMask, (jint)lpStruct->fMask);
	(*env)->SetIntField(env, lpObject, REBARBANDINFOFc.fStyle, (jint)lpStruct->fStyle);
	(*env)->SetIntField(env, lpObject, REBARBANDINFOFc.clrFore, (jint)lpStruct->clrFore);
	(*env)->SetIntField(env, lpObject, REBARBANDINFOFc.clrBack, (jint)lpStruct->clrBack);
	(*env)->SetIntField(env, lpObject, REBARBANDINFOFc.lpText, (jint)lpStruct->lpText);
	(*env)->SetIntField(env, lpObject, REBARBANDINFOFc.cch, (jint)lpStruct->cch);
	(*env)->SetIntField(env, lpObject, REBARBANDINFOFc.iImage, (jint)lpStruct->iImage);
	(*env)->SetIntField(env, lpObject, REBARBANDINFOFc.hwndChild, (jint)lpStruct->hwndChild);
	(*env)->SetIntField(env, lpObject, REBARBANDINFOFc.cxMinChild, (jint)lpStruct->cxMinChild);
	(*env)->SetIntField(env, lpObject, REBARBANDINFOFc.cyMinChild, (jint)lpStruct->cyMinChild);
	(*env)->SetIntField(env, lpObject, REBARBANDINFOFc.cx, (jint)lpStruct->cx);
	(*env)->SetIntField(env, lpObject, REBARBANDINFOFc.hbmBack, (jint)lpStruct->hbmBack);
	(*env)->SetIntField(env, lpObject, REBARBANDINFOFc.wID, (jint)lpStruct->wID);
	(*env)->SetIntField(env, lpObject, REBARBANDINFOFc.cyChild, (jint)lpStruct->cyChild);
	(*env)->SetIntField(env, lpObject, REBARBANDINFOFc.cyMaxChild, (jint)lpStruct->cyMaxChild);
	(*env)->SetIntField(env, lpObject, REBARBANDINFOFc.cyIntegral, (jint)lpStruct->cyIntegral);
	(*env)->SetIntField(env, lpObject, REBARBANDINFOFc.cxIdeal, (jint)lpStruct->cxIdeal);
	(*env)->SetIntField(env, lpObject, REBARBANDINFOFc.lParam, (jint)lpStruct->lParam);
#ifndef _WIN32_WCE
	(*env)->SetIntField(env, lpObject, REBARBANDINFOFc.cxHeader, (jint)lpStruct->cxHeader);
#endif
}
#endif

#ifndef NO_RECT
typedef struct RECT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID left, top, right, bottom;
} RECT_FID_CACHE;

RECT_FID_CACHE RECTFc;

void cacheRECTFields(JNIEnv *env, jobject lpObject)
{
	if (RECTFc.cached) return;
	RECTFc.clazz = (*env)->GetObjectClass(env, lpObject);
	RECTFc.left = (*env)->GetFieldID(env, RECTFc.clazz, "left", "I");
	RECTFc.top = (*env)->GetFieldID(env, RECTFc.clazz, "top", "I");
	RECTFc.right = (*env)->GetFieldID(env, RECTFc.clazz, "right", "I");
	RECTFc.bottom = (*env)->GetFieldID(env, RECTFc.clazz, "bottom", "I");
	RECTFc.cached = 1;
}

RECT *getRECTFields(JNIEnv *env, jobject lpObject, RECT *lpStruct)
{
	if (!RECTFc.cached) cacheRECTFields(env, lpObject);
	lpStruct->left = (*env)->GetIntField(env, lpObject, RECTFc.left);
	lpStruct->top = (*env)->GetIntField(env, lpObject, RECTFc.top);
	lpStruct->right = (*env)->GetIntField(env, lpObject, RECTFc.right);
	lpStruct->bottom = (*env)->GetIntField(env, lpObject, RECTFc.bottom);
	return lpStruct;
}

void setRECTFields(JNIEnv *env, jobject lpObject, RECT *lpStruct)
{
	if (!RECTFc.cached) cacheRECTFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, RECTFc.left, (jint)lpStruct->left);
	(*env)->SetIntField(env, lpObject, RECTFc.top, (jint)lpStruct->top);
	(*env)->SetIntField(env, lpObject, RECTFc.right, (jint)lpStruct->right);
	(*env)->SetIntField(env, lpObject, RECTFc.bottom, (jint)lpStruct->bottom);
}
#endif

#ifndef NO_SCRIPT_ANALYSIS
typedef struct SCRIPT_ANALYSIS_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID eScript, fRTL, fLayoutRTL, fLinkBefore, fLinkAfter, fLogicalOrder, fNoGlyphIndex, s;
} SCRIPT_ANALYSIS_FID_CACHE;

SCRIPT_ANALYSIS_FID_CACHE SCRIPT_ANALYSISFc;

void cacheSCRIPT_ANALYSISFields(JNIEnv *env, jobject lpObject)
{
	if (SCRIPT_ANALYSISFc.cached) return;
	SCRIPT_ANALYSISFc.clazz = (*env)->GetObjectClass(env, lpObject);
	SCRIPT_ANALYSISFc.eScript = (*env)->GetFieldID(env, SCRIPT_ANALYSISFc.clazz, "eScript", "S");
	SCRIPT_ANALYSISFc.fRTL = (*env)->GetFieldID(env, SCRIPT_ANALYSISFc.clazz, "fRTL", "Z");
	SCRIPT_ANALYSISFc.fLayoutRTL = (*env)->GetFieldID(env, SCRIPT_ANALYSISFc.clazz, "fLayoutRTL", "Z");
	SCRIPT_ANALYSISFc.fLinkBefore = (*env)->GetFieldID(env, SCRIPT_ANALYSISFc.clazz, "fLinkBefore", "Z");
	SCRIPT_ANALYSISFc.fLinkAfter = (*env)->GetFieldID(env, SCRIPT_ANALYSISFc.clazz, "fLinkAfter", "Z");
	SCRIPT_ANALYSISFc.fLogicalOrder = (*env)->GetFieldID(env, SCRIPT_ANALYSISFc.clazz, "fLogicalOrder", "Z");
	SCRIPT_ANALYSISFc.fNoGlyphIndex = (*env)->GetFieldID(env, SCRIPT_ANALYSISFc.clazz, "fNoGlyphIndex", "Z");
	SCRIPT_ANALYSISFc.s = (*env)->GetFieldID(env, SCRIPT_ANALYSISFc.clazz, "s", "Lorg/eclipse/swt/internal/win32/SCRIPT_STATE;");
	SCRIPT_ANALYSISFc.cached = 1;
}

SCRIPT_ANALYSIS *getSCRIPT_ANALYSISFields(JNIEnv *env, jobject lpObject, SCRIPT_ANALYSIS *lpStruct)
{
	if (!SCRIPT_ANALYSISFc.cached) cacheSCRIPT_ANALYSISFields(env, lpObject);
	lpStruct->eScript = (*env)->GetShortField(env, lpObject, SCRIPT_ANALYSISFc.eScript);
	lpStruct->fRTL = (*env)->GetBooleanField(env, lpObject, SCRIPT_ANALYSISFc.fRTL);
	lpStruct->fLayoutRTL = (*env)->GetBooleanField(env, lpObject, SCRIPT_ANALYSISFc.fLayoutRTL);
	lpStruct->fLinkBefore = (*env)->GetBooleanField(env, lpObject, SCRIPT_ANALYSISFc.fLinkBefore);
	lpStruct->fLinkAfter = (*env)->GetBooleanField(env, lpObject, SCRIPT_ANALYSISFc.fLinkAfter);
	lpStruct->fLogicalOrder = (*env)->GetBooleanField(env, lpObject, SCRIPT_ANALYSISFc.fLogicalOrder);
	lpStruct->fNoGlyphIndex = (*env)->GetBooleanField(env, lpObject, SCRIPT_ANALYSISFc.fNoGlyphIndex);
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, SCRIPT_ANALYSISFc.s);
	if (lpObject1 != NULL) getSCRIPT_STATEFields(env, lpObject1, &lpStruct->s);
	}
	return lpStruct;
}

void setSCRIPT_ANALYSISFields(JNIEnv *env, jobject lpObject, SCRIPT_ANALYSIS *lpStruct)
{
	if (!SCRIPT_ANALYSISFc.cached) cacheSCRIPT_ANALYSISFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, SCRIPT_ANALYSISFc.eScript, (jshort)lpStruct->eScript);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_ANALYSISFc.fRTL, (jboolean)lpStruct->fRTL);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_ANALYSISFc.fLayoutRTL, (jboolean)lpStruct->fLayoutRTL);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_ANALYSISFc.fLinkBefore, (jboolean)lpStruct->fLinkBefore);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_ANALYSISFc.fLinkAfter, (jboolean)lpStruct->fLinkAfter);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_ANALYSISFc.fLogicalOrder, (jboolean)lpStruct->fLogicalOrder);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_ANALYSISFc.fNoGlyphIndex, (jboolean)lpStruct->fNoGlyphIndex);
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, SCRIPT_ANALYSISFc.s);
	if (lpObject1 != NULL) setSCRIPT_STATEFields(env, lpObject1, &lpStruct->s);
	}
}
#endif

#ifndef NO_SCRIPT_CONTROL
typedef struct SCRIPT_CONTROL_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID uDefaultLanguage, fContextDigits, fInvertPreBoundDir, fInvertPostBoundDir, fLinkStringBefore, fLinkStringAfter, fNeutralOverride, fNumericOverride, fLegacyBidiClass, fReserved;
} SCRIPT_CONTROL_FID_CACHE;

SCRIPT_CONTROL_FID_CACHE SCRIPT_CONTROLFc;

void cacheSCRIPT_CONTROLFields(JNIEnv *env, jobject lpObject)
{
	if (SCRIPT_CONTROLFc.cached) return;
	SCRIPT_CONTROLFc.clazz = (*env)->GetObjectClass(env, lpObject);
	SCRIPT_CONTROLFc.uDefaultLanguage = (*env)->GetFieldID(env, SCRIPT_CONTROLFc.clazz, "uDefaultLanguage", "I");
	SCRIPT_CONTROLFc.fContextDigits = (*env)->GetFieldID(env, SCRIPT_CONTROLFc.clazz, "fContextDigits", "Z");
	SCRIPT_CONTROLFc.fInvertPreBoundDir = (*env)->GetFieldID(env, SCRIPT_CONTROLFc.clazz, "fInvertPreBoundDir", "Z");
	SCRIPT_CONTROLFc.fInvertPostBoundDir = (*env)->GetFieldID(env, SCRIPT_CONTROLFc.clazz, "fInvertPostBoundDir", "Z");
	SCRIPT_CONTROLFc.fLinkStringBefore = (*env)->GetFieldID(env, SCRIPT_CONTROLFc.clazz, "fLinkStringBefore", "Z");
	SCRIPT_CONTROLFc.fLinkStringAfter = (*env)->GetFieldID(env, SCRIPT_CONTROLFc.clazz, "fLinkStringAfter", "Z");
	SCRIPT_CONTROLFc.fNeutralOverride = (*env)->GetFieldID(env, SCRIPT_CONTROLFc.clazz, "fNeutralOverride", "Z");
	SCRIPT_CONTROLFc.fNumericOverride = (*env)->GetFieldID(env, SCRIPT_CONTROLFc.clazz, "fNumericOverride", "Z");
	SCRIPT_CONTROLFc.fLegacyBidiClass = (*env)->GetFieldID(env, SCRIPT_CONTROLFc.clazz, "fLegacyBidiClass", "Z");
	SCRIPT_CONTROLFc.fReserved = (*env)->GetFieldID(env, SCRIPT_CONTROLFc.clazz, "fReserved", "I");
	SCRIPT_CONTROLFc.cached = 1;
}

SCRIPT_CONTROL *getSCRIPT_CONTROLFields(JNIEnv *env, jobject lpObject, SCRIPT_CONTROL *lpStruct)
{
	if (!SCRIPT_CONTROLFc.cached) cacheSCRIPT_CONTROLFields(env, lpObject);
	lpStruct->uDefaultLanguage = (*env)->GetIntField(env, lpObject, SCRIPT_CONTROLFc.uDefaultLanguage);
	lpStruct->fContextDigits = (*env)->GetBooleanField(env, lpObject, SCRIPT_CONTROLFc.fContextDigits);
	lpStruct->fInvertPreBoundDir = (*env)->GetBooleanField(env, lpObject, SCRIPT_CONTROLFc.fInvertPreBoundDir);
	lpStruct->fInvertPostBoundDir = (*env)->GetBooleanField(env, lpObject, SCRIPT_CONTROLFc.fInvertPostBoundDir);
	lpStruct->fLinkStringBefore = (*env)->GetBooleanField(env, lpObject, SCRIPT_CONTROLFc.fLinkStringBefore);
	lpStruct->fLinkStringAfter = (*env)->GetBooleanField(env, lpObject, SCRIPT_CONTROLFc.fLinkStringAfter);
	lpStruct->fNeutralOverride = (*env)->GetBooleanField(env, lpObject, SCRIPT_CONTROLFc.fNeutralOverride);
	lpStruct->fNumericOverride = (*env)->GetBooleanField(env, lpObject, SCRIPT_CONTROLFc.fNumericOverride);
	lpStruct->fLegacyBidiClass = (*env)->GetBooleanField(env, lpObject, SCRIPT_CONTROLFc.fLegacyBidiClass);
	lpStruct->fReserved = (*env)->GetIntField(env, lpObject, SCRIPT_CONTROLFc.fReserved);
	return lpStruct;
}

void setSCRIPT_CONTROLFields(JNIEnv *env, jobject lpObject, SCRIPT_CONTROL *lpStruct)
{
	if (!SCRIPT_CONTROLFc.cached) cacheSCRIPT_CONTROLFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, SCRIPT_CONTROLFc.uDefaultLanguage, (jint)lpStruct->uDefaultLanguage);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_CONTROLFc.fContextDigits, (jboolean)lpStruct->fContextDigits);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_CONTROLFc.fInvertPreBoundDir, (jboolean)lpStruct->fInvertPreBoundDir);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_CONTROLFc.fInvertPostBoundDir, (jboolean)lpStruct->fInvertPostBoundDir);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_CONTROLFc.fLinkStringBefore, (jboolean)lpStruct->fLinkStringBefore);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_CONTROLFc.fLinkStringAfter, (jboolean)lpStruct->fLinkStringAfter);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_CONTROLFc.fNeutralOverride, (jboolean)lpStruct->fNeutralOverride);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_CONTROLFc.fNumericOverride, (jboolean)lpStruct->fNumericOverride);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_CONTROLFc.fLegacyBidiClass, (jboolean)lpStruct->fLegacyBidiClass);
	(*env)->SetIntField(env, lpObject, SCRIPT_CONTROLFc.fReserved, (jint)lpStruct->fReserved);
}
#endif

#ifndef NO_SCRIPT_FONTPROPERTIES
typedef struct SCRIPT_FONTPROPERTIES_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cBytes, wgBlank, wgDefault, wgInvalid, wgKashida, iKashidaWidth;
} SCRIPT_FONTPROPERTIES_FID_CACHE;

SCRIPT_FONTPROPERTIES_FID_CACHE SCRIPT_FONTPROPERTIESFc;

void cacheSCRIPT_FONTPROPERTIESFields(JNIEnv *env, jobject lpObject)
{
	if (SCRIPT_FONTPROPERTIESFc.cached) return;
	SCRIPT_FONTPROPERTIESFc.clazz = (*env)->GetObjectClass(env, lpObject);
	SCRIPT_FONTPROPERTIESFc.cBytes = (*env)->GetFieldID(env, SCRIPT_FONTPROPERTIESFc.clazz, "cBytes", "I");
	SCRIPT_FONTPROPERTIESFc.wgBlank = (*env)->GetFieldID(env, SCRIPT_FONTPROPERTIESFc.clazz, "wgBlank", "S");
	SCRIPT_FONTPROPERTIESFc.wgDefault = (*env)->GetFieldID(env, SCRIPT_FONTPROPERTIESFc.clazz, "wgDefault", "S");
	SCRIPT_FONTPROPERTIESFc.wgInvalid = (*env)->GetFieldID(env, SCRIPT_FONTPROPERTIESFc.clazz, "wgInvalid", "S");
	SCRIPT_FONTPROPERTIESFc.wgKashida = (*env)->GetFieldID(env, SCRIPT_FONTPROPERTIESFc.clazz, "wgKashida", "S");
	SCRIPT_FONTPROPERTIESFc.iKashidaWidth = (*env)->GetFieldID(env, SCRIPT_FONTPROPERTIESFc.clazz, "iKashidaWidth", "I");
	SCRIPT_FONTPROPERTIESFc.cached = 1;
}

SCRIPT_FONTPROPERTIES *getSCRIPT_FONTPROPERTIESFields(JNIEnv *env, jobject lpObject, SCRIPT_FONTPROPERTIES *lpStruct)
{
	if (!SCRIPT_FONTPROPERTIESFc.cached) cacheSCRIPT_FONTPROPERTIESFields(env, lpObject);
	lpStruct->cBytes = (*env)->GetIntField(env, lpObject, SCRIPT_FONTPROPERTIESFc.cBytes);
	lpStruct->wgBlank = (*env)->GetShortField(env, lpObject, SCRIPT_FONTPROPERTIESFc.wgBlank);
	lpStruct->wgDefault = (*env)->GetShortField(env, lpObject, SCRIPT_FONTPROPERTIESFc.wgDefault);
	lpStruct->wgInvalid = (*env)->GetShortField(env, lpObject, SCRIPT_FONTPROPERTIESFc.wgInvalid);
	lpStruct->wgKashida = (*env)->GetShortField(env, lpObject, SCRIPT_FONTPROPERTIESFc.wgKashida);
	lpStruct->iKashidaWidth = (*env)->GetIntField(env, lpObject, SCRIPT_FONTPROPERTIESFc.iKashidaWidth);
	return lpStruct;
}

void setSCRIPT_FONTPROPERTIESFields(JNIEnv *env, jobject lpObject, SCRIPT_FONTPROPERTIES *lpStruct)
{
	if (!SCRIPT_FONTPROPERTIESFc.cached) cacheSCRIPT_FONTPROPERTIESFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, SCRIPT_FONTPROPERTIESFc.cBytes, (jint)lpStruct->cBytes);
	(*env)->SetShortField(env, lpObject, SCRIPT_FONTPROPERTIESFc.wgBlank, (jshort)lpStruct->wgBlank);
	(*env)->SetShortField(env, lpObject, SCRIPT_FONTPROPERTIESFc.wgDefault, (jshort)lpStruct->wgDefault);
	(*env)->SetShortField(env, lpObject, SCRIPT_FONTPROPERTIESFc.wgInvalid, (jshort)lpStruct->wgInvalid);
	(*env)->SetShortField(env, lpObject, SCRIPT_FONTPROPERTIESFc.wgKashida, (jshort)lpStruct->wgKashida);
	(*env)->SetIntField(env, lpObject, SCRIPT_FONTPROPERTIESFc.iKashidaWidth, (jint)lpStruct->iKashidaWidth);
}
#endif

#ifndef NO_SCRIPT_ITEM
typedef struct SCRIPT_ITEM_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID iCharPos, a;
} SCRIPT_ITEM_FID_CACHE;

SCRIPT_ITEM_FID_CACHE SCRIPT_ITEMFc;

void cacheSCRIPT_ITEMFields(JNIEnv *env, jobject lpObject)
{
	if (SCRIPT_ITEMFc.cached) return;
	SCRIPT_ITEMFc.clazz = (*env)->GetObjectClass(env, lpObject);
	SCRIPT_ITEMFc.iCharPos = (*env)->GetFieldID(env, SCRIPT_ITEMFc.clazz, "iCharPos", "I");
	SCRIPT_ITEMFc.a = (*env)->GetFieldID(env, SCRIPT_ITEMFc.clazz, "a", "Lorg/eclipse/swt/internal/win32/SCRIPT_ANALYSIS;");
	SCRIPT_ITEMFc.cached = 1;
}

SCRIPT_ITEM *getSCRIPT_ITEMFields(JNIEnv *env, jobject lpObject, SCRIPT_ITEM *lpStruct)
{
	if (!SCRIPT_ITEMFc.cached) cacheSCRIPT_ITEMFields(env, lpObject);
	lpStruct->iCharPos = (*env)->GetIntField(env, lpObject, SCRIPT_ITEMFc.iCharPos);
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, SCRIPT_ITEMFc.a);
	if (lpObject1 != NULL) getSCRIPT_ANALYSISFields(env, lpObject1, &lpStruct->a);
	}
	return lpStruct;
}

void setSCRIPT_ITEMFields(JNIEnv *env, jobject lpObject, SCRIPT_ITEM *lpStruct)
{
	if (!SCRIPT_ITEMFc.cached) cacheSCRIPT_ITEMFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, SCRIPT_ITEMFc.iCharPos, (jint)lpStruct->iCharPos);
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, SCRIPT_ITEMFc.a);
	if (lpObject1 != NULL) setSCRIPT_ANALYSISFields(env, lpObject1, &lpStruct->a);
	}
}
#endif

#ifndef NO_SCRIPT_LOGATTR
typedef struct SCRIPT_LOGATTR_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID fSoftBreak, fWhiteSpace, fCharStop, fWordStop, fInvalid, fReserved;
} SCRIPT_LOGATTR_FID_CACHE;

SCRIPT_LOGATTR_FID_CACHE SCRIPT_LOGATTRFc;

void cacheSCRIPT_LOGATTRFields(JNIEnv *env, jobject lpObject)
{
	if (SCRIPT_LOGATTRFc.cached) return;
	SCRIPT_LOGATTRFc.clazz = (*env)->GetObjectClass(env, lpObject);
	SCRIPT_LOGATTRFc.fSoftBreak = (*env)->GetFieldID(env, SCRIPT_LOGATTRFc.clazz, "fSoftBreak", "Z");
	SCRIPT_LOGATTRFc.fWhiteSpace = (*env)->GetFieldID(env, SCRIPT_LOGATTRFc.clazz, "fWhiteSpace", "Z");
	SCRIPT_LOGATTRFc.fCharStop = (*env)->GetFieldID(env, SCRIPT_LOGATTRFc.clazz, "fCharStop", "Z");
	SCRIPT_LOGATTRFc.fWordStop = (*env)->GetFieldID(env, SCRIPT_LOGATTRFc.clazz, "fWordStop", "Z");
	SCRIPT_LOGATTRFc.fInvalid = (*env)->GetFieldID(env, SCRIPT_LOGATTRFc.clazz, "fInvalid", "Z");
	SCRIPT_LOGATTRFc.fReserved = (*env)->GetFieldID(env, SCRIPT_LOGATTRFc.clazz, "fReserved", "B");
	SCRIPT_LOGATTRFc.cached = 1;
}

SCRIPT_LOGATTR *getSCRIPT_LOGATTRFields(JNIEnv *env, jobject lpObject, SCRIPT_LOGATTR *lpStruct)
{
	if (!SCRIPT_LOGATTRFc.cached) cacheSCRIPT_LOGATTRFields(env, lpObject);
	lpStruct->fSoftBreak = (*env)->GetBooleanField(env, lpObject, SCRIPT_LOGATTRFc.fSoftBreak);
	lpStruct->fWhiteSpace = (*env)->GetBooleanField(env, lpObject, SCRIPT_LOGATTRFc.fWhiteSpace);
	lpStruct->fCharStop = (*env)->GetBooleanField(env, lpObject, SCRIPT_LOGATTRFc.fCharStop);
	lpStruct->fWordStop = (*env)->GetBooleanField(env, lpObject, SCRIPT_LOGATTRFc.fWordStop);
	lpStruct->fInvalid = (*env)->GetBooleanField(env, lpObject, SCRIPT_LOGATTRFc.fInvalid);
	lpStruct->fReserved = (*env)->GetByteField(env, lpObject, SCRIPT_LOGATTRFc.fReserved);
	return lpStruct;
}

void setSCRIPT_LOGATTRFields(JNIEnv *env, jobject lpObject, SCRIPT_LOGATTR *lpStruct)
{
	if (!SCRIPT_LOGATTRFc.cached) cacheSCRIPT_LOGATTRFields(env, lpObject);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_LOGATTRFc.fSoftBreak, (jboolean)lpStruct->fSoftBreak);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_LOGATTRFc.fWhiteSpace, (jboolean)lpStruct->fWhiteSpace);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_LOGATTRFc.fCharStop, (jboolean)lpStruct->fCharStop);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_LOGATTRFc.fWordStop, (jboolean)lpStruct->fWordStop);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_LOGATTRFc.fInvalid, (jboolean)lpStruct->fInvalid);
	(*env)->SetByteField(env, lpObject, SCRIPT_LOGATTRFc.fReserved, (jbyte)lpStruct->fReserved);
}
#endif

#ifndef NO_SCRIPT_PROPERTIES
typedef struct SCRIPT_PROPERTIES_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID langid, fNumeric, fComplex, fNeedsWordBreaking, fNeedsCaretInfo, bCharSet, fControl, fPrivateUseArea, fNeedsCharacterJustify, fInvalidGlyph, fInvalidLogAttr, fCDM, fAmbiguousCharSet, fClusterSizeVaries, fRejectInvalid;
} SCRIPT_PROPERTIES_FID_CACHE;

SCRIPT_PROPERTIES_FID_CACHE SCRIPT_PROPERTIESFc;

void cacheSCRIPT_PROPERTIESFields(JNIEnv *env, jobject lpObject)
{
	if (SCRIPT_PROPERTIESFc.cached) return;
	SCRIPT_PROPERTIESFc.clazz = (*env)->GetObjectClass(env, lpObject);
	SCRIPT_PROPERTIESFc.langid = (*env)->GetFieldID(env, SCRIPT_PROPERTIESFc.clazz, "langid", "S");
	SCRIPT_PROPERTIESFc.fNumeric = (*env)->GetFieldID(env, SCRIPT_PROPERTIESFc.clazz, "fNumeric", "Z");
	SCRIPT_PROPERTIESFc.fComplex = (*env)->GetFieldID(env, SCRIPT_PROPERTIESFc.clazz, "fComplex", "Z");
	SCRIPT_PROPERTIESFc.fNeedsWordBreaking = (*env)->GetFieldID(env, SCRIPT_PROPERTIESFc.clazz, "fNeedsWordBreaking", "Z");
	SCRIPT_PROPERTIESFc.fNeedsCaretInfo = (*env)->GetFieldID(env, SCRIPT_PROPERTIESFc.clazz, "fNeedsCaretInfo", "Z");
	SCRIPT_PROPERTIESFc.bCharSet = (*env)->GetFieldID(env, SCRIPT_PROPERTIESFc.clazz, "bCharSet", "B");
	SCRIPT_PROPERTIESFc.fControl = (*env)->GetFieldID(env, SCRIPT_PROPERTIESFc.clazz, "fControl", "Z");
	SCRIPT_PROPERTIESFc.fPrivateUseArea = (*env)->GetFieldID(env, SCRIPT_PROPERTIESFc.clazz, "fPrivateUseArea", "Z");
	SCRIPT_PROPERTIESFc.fNeedsCharacterJustify = (*env)->GetFieldID(env, SCRIPT_PROPERTIESFc.clazz, "fNeedsCharacterJustify", "Z");
	SCRIPT_PROPERTIESFc.fInvalidGlyph = (*env)->GetFieldID(env, SCRIPT_PROPERTIESFc.clazz, "fInvalidGlyph", "Z");
	SCRIPT_PROPERTIESFc.fInvalidLogAttr = (*env)->GetFieldID(env, SCRIPT_PROPERTIESFc.clazz, "fInvalidLogAttr", "Z");
	SCRIPT_PROPERTIESFc.fCDM = (*env)->GetFieldID(env, SCRIPT_PROPERTIESFc.clazz, "fCDM", "Z");
	SCRIPT_PROPERTIESFc.fAmbiguousCharSet = (*env)->GetFieldID(env, SCRIPT_PROPERTIESFc.clazz, "fAmbiguousCharSet", "Z");
	SCRIPT_PROPERTIESFc.fClusterSizeVaries = (*env)->GetFieldID(env, SCRIPT_PROPERTIESFc.clazz, "fClusterSizeVaries", "Z");
	SCRIPT_PROPERTIESFc.fRejectInvalid = (*env)->GetFieldID(env, SCRIPT_PROPERTIESFc.clazz, "fRejectInvalid", "Z");
	SCRIPT_PROPERTIESFc.cached = 1;
}

SCRIPT_PROPERTIES *getSCRIPT_PROPERTIESFields(JNIEnv *env, jobject lpObject, SCRIPT_PROPERTIES *lpStruct)
{
	if (!SCRIPT_PROPERTIESFc.cached) cacheSCRIPT_PROPERTIESFields(env, lpObject);
	lpStruct->langid = (*env)->GetShortField(env, lpObject, SCRIPT_PROPERTIESFc.langid);
	lpStruct->fNumeric = (*env)->GetBooleanField(env, lpObject, SCRIPT_PROPERTIESFc.fNumeric);
	lpStruct->fComplex = (*env)->GetBooleanField(env, lpObject, SCRIPT_PROPERTIESFc.fComplex);
	lpStruct->fNeedsWordBreaking = (*env)->GetBooleanField(env, lpObject, SCRIPT_PROPERTIESFc.fNeedsWordBreaking);
	lpStruct->fNeedsCaretInfo = (*env)->GetBooleanField(env, lpObject, SCRIPT_PROPERTIESFc.fNeedsCaretInfo);
	lpStruct->bCharSet = (*env)->GetByteField(env, lpObject, SCRIPT_PROPERTIESFc.bCharSet);
	lpStruct->fControl = (*env)->GetBooleanField(env, lpObject, SCRIPT_PROPERTIESFc.fControl);
	lpStruct->fPrivateUseArea = (*env)->GetBooleanField(env, lpObject, SCRIPT_PROPERTIESFc.fPrivateUseArea);
	lpStruct->fNeedsCharacterJustify = (*env)->GetBooleanField(env, lpObject, SCRIPT_PROPERTIESFc.fNeedsCharacterJustify);
	lpStruct->fInvalidGlyph = (*env)->GetBooleanField(env, lpObject, SCRIPT_PROPERTIESFc.fInvalidGlyph);
	lpStruct->fInvalidLogAttr = (*env)->GetBooleanField(env, lpObject, SCRIPT_PROPERTIESFc.fInvalidLogAttr);
	lpStruct->fCDM = (*env)->GetBooleanField(env, lpObject, SCRIPT_PROPERTIESFc.fCDM);
	lpStruct->fAmbiguousCharSet = (*env)->GetBooleanField(env, lpObject, SCRIPT_PROPERTIESFc.fAmbiguousCharSet);
	lpStruct->fClusterSizeVaries = (*env)->GetBooleanField(env, lpObject, SCRIPT_PROPERTIESFc.fClusterSizeVaries);
	lpStruct->fRejectInvalid = (*env)->GetBooleanField(env, lpObject, SCRIPT_PROPERTIESFc.fRejectInvalid);
	return lpStruct;
}

void setSCRIPT_PROPERTIESFields(JNIEnv *env, jobject lpObject, SCRIPT_PROPERTIES *lpStruct)
{
	if (!SCRIPT_PROPERTIESFc.cached) cacheSCRIPT_PROPERTIESFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, SCRIPT_PROPERTIESFc.langid, (jshort)lpStruct->langid);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_PROPERTIESFc.fNumeric, (jboolean)lpStruct->fNumeric);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_PROPERTIESFc.fComplex, (jboolean)lpStruct->fComplex);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_PROPERTIESFc.fNeedsWordBreaking, (jboolean)lpStruct->fNeedsWordBreaking);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_PROPERTIESFc.fNeedsCaretInfo, (jboolean)lpStruct->fNeedsCaretInfo);
	(*env)->SetByteField(env, lpObject, SCRIPT_PROPERTIESFc.bCharSet, (jbyte)lpStruct->bCharSet);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_PROPERTIESFc.fControl, (jboolean)lpStruct->fControl);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_PROPERTIESFc.fPrivateUseArea, (jboolean)lpStruct->fPrivateUseArea);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_PROPERTIESFc.fNeedsCharacterJustify, (jboolean)lpStruct->fNeedsCharacterJustify);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_PROPERTIESFc.fInvalidGlyph, (jboolean)lpStruct->fInvalidGlyph);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_PROPERTIESFc.fInvalidLogAttr, (jboolean)lpStruct->fInvalidLogAttr);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_PROPERTIESFc.fCDM, (jboolean)lpStruct->fCDM);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_PROPERTIESFc.fAmbiguousCharSet, (jboolean)lpStruct->fAmbiguousCharSet);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_PROPERTIESFc.fClusterSizeVaries, (jboolean)lpStruct->fClusterSizeVaries);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_PROPERTIESFc.fRejectInvalid, (jboolean)lpStruct->fRejectInvalid);
}
#endif

#ifndef NO_SCRIPT_STATE
typedef struct SCRIPT_STATE_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID uBidiLevel, fOverrideDirection, fInhibitSymSwap, fCharShape, fDigitSubstitute, fInhibitLigate, fDisplayZWG, fArabicNumContext, fGcpClusters, fReserved, fEngineReserved;
} SCRIPT_STATE_FID_CACHE;

SCRIPT_STATE_FID_CACHE SCRIPT_STATEFc;

void cacheSCRIPT_STATEFields(JNIEnv *env, jobject lpObject)
{
	if (SCRIPT_STATEFc.cached) return;
	SCRIPT_STATEFc.clazz = (*env)->GetObjectClass(env, lpObject);
	SCRIPT_STATEFc.uBidiLevel = (*env)->GetFieldID(env, SCRIPT_STATEFc.clazz, "uBidiLevel", "S");
	SCRIPT_STATEFc.fOverrideDirection = (*env)->GetFieldID(env, SCRIPT_STATEFc.clazz, "fOverrideDirection", "Z");
	SCRIPT_STATEFc.fInhibitSymSwap = (*env)->GetFieldID(env, SCRIPT_STATEFc.clazz, "fInhibitSymSwap", "Z");
	SCRIPT_STATEFc.fCharShape = (*env)->GetFieldID(env, SCRIPT_STATEFc.clazz, "fCharShape", "Z");
	SCRIPT_STATEFc.fDigitSubstitute = (*env)->GetFieldID(env, SCRIPT_STATEFc.clazz, "fDigitSubstitute", "Z");
	SCRIPT_STATEFc.fInhibitLigate = (*env)->GetFieldID(env, SCRIPT_STATEFc.clazz, "fInhibitLigate", "Z");
	SCRIPT_STATEFc.fDisplayZWG = (*env)->GetFieldID(env, SCRIPT_STATEFc.clazz, "fDisplayZWG", "Z");
	SCRIPT_STATEFc.fArabicNumContext = (*env)->GetFieldID(env, SCRIPT_STATEFc.clazz, "fArabicNumContext", "Z");
	SCRIPT_STATEFc.fGcpClusters = (*env)->GetFieldID(env, SCRIPT_STATEFc.clazz, "fGcpClusters", "Z");
	SCRIPT_STATEFc.fReserved = (*env)->GetFieldID(env, SCRIPT_STATEFc.clazz, "fReserved", "Z");
	SCRIPT_STATEFc.fEngineReserved = (*env)->GetFieldID(env, SCRIPT_STATEFc.clazz, "fEngineReserved", "S");
	SCRIPT_STATEFc.cached = 1;
}

SCRIPT_STATE *getSCRIPT_STATEFields(JNIEnv *env, jobject lpObject, SCRIPT_STATE *lpStruct)
{
	if (!SCRIPT_STATEFc.cached) cacheSCRIPT_STATEFields(env, lpObject);
	lpStruct->uBidiLevel = (*env)->GetShortField(env, lpObject, SCRIPT_STATEFc.uBidiLevel);
	lpStruct->fOverrideDirection = (*env)->GetBooleanField(env, lpObject, SCRIPT_STATEFc.fOverrideDirection);
	lpStruct->fInhibitSymSwap = (*env)->GetBooleanField(env, lpObject, SCRIPT_STATEFc.fInhibitSymSwap);
	lpStruct->fCharShape = (*env)->GetBooleanField(env, lpObject, SCRIPT_STATEFc.fCharShape);
	lpStruct->fDigitSubstitute = (*env)->GetBooleanField(env, lpObject, SCRIPT_STATEFc.fDigitSubstitute);
	lpStruct->fInhibitLigate = (*env)->GetBooleanField(env, lpObject, SCRIPT_STATEFc.fInhibitLigate);
	lpStruct->fDisplayZWG = (*env)->GetBooleanField(env, lpObject, SCRIPT_STATEFc.fDisplayZWG);
	lpStruct->fArabicNumContext = (*env)->GetBooleanField(env, lpObject, SCRIPT_STATEFc.fArabicNumContext);
	lpStruct->fGcpClusters = (*env)->GetBooleanField(env, lpObject, SCRIPT_STATEFc.fGcpClusters);
	lpStruct->fReserved = (*env)->GetBooleanField(env, lpObject, SCRIPT_STATEFc.fReserved);
	lpStruct->fEngineReserved = (*env)->GetShortField(env, lpObject, SCRIPT_STATEFc.fEngineReserved);
	return lpStruct;
}

void setSCRIPT_STATEFields(JNIEnv *env, jobject lpObject, SCRIPT_STATE *lpStruct)
{
	if (!SCRIPT_STATEFc.cached) cacheSCRIPT_STATEFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, SCRIPT_STATEFc.uBidiLevel, (jshort)lpStruct->uBidiLevel);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_STATEFc.fOverrideDirection, (jboolean)lpStruct->fOverrideDirection);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_STATEFc.fInhibitSymSwap, (jboolean)lpStruct->fInhibitSymSwap);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_STATEFc.fCharShape, (jboolean)lpStruct->fCharShape);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_STATEFc.fDigitSubstitute, (jboolean)lpStruct->fDigitSubstitute);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_STATEFc.fInhibitLigate, (jboolean)lpStruct->fInhibitLigate);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_STATEFc.fDisplayZWG, (jboolean)lpStruct->fDisplayZWG);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_STATEFc.fArabicNumContext, (jboolean)lpStruct->fArabicNumContext);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_STATEFc.fGcpClusters, (jboolean)lpStruct->fGcpClusters);
	(*env)->SetBooleanField(env, lpObject, SCRIPT_STATEFc.fReserved, (jboolean)lpStruct->fReserved);
	(*env)->SetShortField(env, lpObject, SCRIPT_STATEFc.fEngineReserved, (jshort)lpStruct->fEngineReserved);
}
#endif

#ifndef NO_SCROLLINFO
typedef struct SCROLLINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, fMask, nMin, nMax, nPage, nPos, nTrackPos;
} SCROLLINFO_FID_CACHE;

SCROLLINFO_FID_CACHE SCROLLINFOFc;

void cacheSCROLLINFOFields(JNIEnv *env, jobject lpObject)
{
	if (SCROLLINFOFc.cached) return;
	SCROLLINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	SCROLLINFOFc.cbSize = (*env)->GetFieldID(env, SCROLLINFOFc.clazz, "cbSize", "I");
	SCROLLINFOFc.fMask = (*env)->GetFieldID(env, SCROLLINFOFc.clazz, "fMask", "I");
	SCROLLINFOFc.nMin = (*env)->GetFieldID(env, SCROLLINFOFc.clazz, "nMin", "I");
	SCROLLINFOFc.nMax = (*env)->GetFieldID(env, SCROLLINFOFc.clazz, "nMax", "I");
	SCROLLINFOFc.nPage = (*env)->GetFieldID(env, SCROLLINFOFc.clazz, "nPage", "I");
	SCROLLINFOFc.nPos = (*env)->GetFieldID(env, SCROLLINFOFc.clazz, "nPos", "I");
	SCROLLINFOFc.nTrackPos = (*env)->GetFieldID(env, SCROLLINFOFc.clazz, "nTrackPos", "I");
	SCROLLINFOFc.cached = 1;
}

SCROLLINFO *getSCROLLINFOFields(JNIEnv *env, jobject lpObject, SCROLLINFO *lpStruct)
{
	if (!SCROLLINFOFc.cached) cacheSCROLLINFOFields(env, lpObject);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, SCROLLINFOFc.cbSize);
	lpStruct->fMask = (*env)->GetIntField(env, lpObject, SCROLLINFOFc.fMask);
	lpStruct->nMin = (*env)->GetIntField(env, lpObject, SCROLLINFOFc.nMin);
	lpStruct->nMax = (*env)->GetIntField(env, lpObject, SCROLLINFOFc.nMax);
	lpStruct->nPage = (*env)->GetIntField(env, lpObject, SCROLLINFOFc.nPage);
	lpStruct->nPos = (*env)->GetIntField(env, lpObject, SCROLLINFOFc.nPos);
	lpStruct->nTrackPos = (*env)->GetIntField(env, lpObject, SCROLLINFOFc.nTrackPos);
	return lpStruct;
}

void setSCROLLINFOFields(JNIEnv *env, jobject lpObject, SCROLLINFO *lpStruct)
{
	if (!SCROLLINFOFc.cached) cacheSCROLLINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, SCROLLINFOFc.cbSize, (jint)lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, SCROLLINFOFc.fMask, (jint)lpStruct->fMask);
	(*env)->SetIntField(env, lpObject, SCROLLINFOFc.nMin, (jint)lpStruct->nMin);
	(*env)->SetIntField(env, lpObject, SCROLLINFOFc.nMax, (jint)lpStruct->nMax);
	(*env)->SetIntField(env, lpObject, SCROLLINFOFc.nPage, (jint)lpStruct->nPage);
	(*env)->SetIntField(env, lpObject, SCROLLINFOFc.nPos, (jint)lpStruct->nPos);
	(*env)->SetIntField(env, lpObject, SCROLLINFOFc.nTrackPos, (jint)lpStruct->nTrackPos);
}
#endif

#ifndef NO_SHACTIVATEINFO
typedef struct SHACTIVATEINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, hwndLastFocus, fSipUp, fSipOnDeactivation, fActive, fReserved;
} SHACTIVATEINFO_FID_CACHE;

SHACTIVATEINFO_FID_CACHE SHACTIVATEINFOFc;

void cacheSHACTIVATEINFOFields(JNIEnv *env, jobject lpObject)
{
	if (SHACTIVATEINFOFc.cached) return;
	SHACTIVATEINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	SHACTIVATEINFOFc.cbSize = (*env)->GetFieldID(env, SHACTIVATEINFOFc.clazz, "cbSize", "I");
	SHACTIVATEINFOFc.hwndLastFocus = (*env)->GetFieldID(env, SHACTIVATEINFOFc.clazz, "hwndLastFocus", "I");
	SHACTIVATEINFOFc.fSipUp = (*env)->GetFieldID(env, SHACTIVATEINFOFc.clazz, "fSipUp", "I");
	SHACTIVATEINFOFc.fSipOnDeactivation = (*env)->GetFieldID(env, SHACTIVATEINFOFc.clazz, "fSipOnDeactivation", "I");
	SHACTIVATEINFOFc.fActive = (*env)->GetFieldID(env, SHACTIVATEINFOFc.clazz, "fActive", "I");
	SHACTIVATEINFOFc.fReserved = (*env)->GetFieldID(env, SHACTIVATEINFOFc.clazz, "fReserved", "I");
	SHACTIVATEINFOFc.cached = 1;
}

SHACTIVATEINFO *getSHACTIVATEINFOFields(JNIEnv *env, jobject lpObject, SHACTIVATEINFO *lpStruct)
{
	if (!SHACTIVATEINFOFc.cached) cacheSHACTIVATEINFOFields(env, lpObject);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, SHACTIVATEINFOFc.cbSize);
	lpStruct->hwndLastFocus = (HWND)(*env)->GetIntField(env, lpObject, SHACTIVATEINFOFc.hwndLastFocus);
	lpStruct->fSipUp = (*env)->GetIntField(env, lpObject, SHACTIVATEINFOFc.fSipUp);
	lpStruct->fSipOnDeactivation = (*env)->GetIntField(env, lpObject, SHACTIVATEINFOFc.fSipOnDeactivation);
	lpStruct->fActive = (*env)->GetIntField(env, lpObject, SHACTIVATEINFOFc.fActive);
	lpStruct->fReserved = (*env)->GetIntField(env, lpObject, SHACTIVATEINFOFc.fReserved);
	return lpStruct;
}

void setSHACTIVATEINFOFields(JNIEnv *env, jobject lpObject, SHACTIVATEINFO *lpStruct)
{
	if (!SHACTIVATEINFOFc.cached) cacheSHACTIVATEINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, SHACTIVATEINFOFc.cbSize, (jint)lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, SHACTIVATEINFOFc.hwndLastFocus, (jint)lpStruct->hwndLastFocus);
	(*env)->SetIntField(env, lpObject, SHACTIVATEINFOFc.fSipUp, (jint)lpStruct->fSipUp);
	(*env)->SetIntField(env, lpObject, SHACTIVATEINFOFc.fSipOnDeactivation, (jint)lpStruct->fSipOnDeactivation);
	(*env)->SetIntField(env, lpObject, SHACTIVATEINFOFc.fActive, (jint)lpStruct->fActive);
	(*env)->SetIntField(env, lpObject, SHACTIVATEINFOFc.fReserved, (jint)lpStruct->fReserved);
}
#endif

#ifndef NO_SHELLEXECUTEINFO
typedef struct SHELLEXECUTEINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, fMask, hwnd, lpVerb, lpFile, lpParameters, lpDirectory, nShow, hInstApp, lpIDList, lpClass, hkeyClass, dwHotKey, hIcon, hProcess;
} SHELLEXECUTEINFO_FID_CACHE;

SHELLEXECUTEINFO_FID_CACHE SHELLEXECUTEINFOFc;

void cacheSHELLEXECUTEINFOFields(JNIEnv *env, jobject lpObject)
{
	if (SHELLEXECUTEINFOFc.cached) return;
	SHELLEXECUTEINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	SHELLEXECUTEINFOFc.cbSize = (*env)->GetFieldID(env, SHELLEXECUTEINFOFc.clazz, "cbSize", "I");
	SHELLEXECUTEINFOFc.fMask = (*env)->GetFieldID(env, SHELLEXECUTEINFOFc.clazz, "fMask", "I");
	SHELLEXECUTEINFOFc.hwnd = (*env)->GetFieldID(env, SHELLEXECUTEINFOFc.clazz, "hwnd", "I");
	SHELLEXECUTEINFOFc.lpVerb = (*env)->GetFieldID(env, SHELLEXECUTEINFOFc.clazz, "lpVerb", "I");
	SHELLEXECUTEINFOFc.lpFile = (*env)->GetFieldID(env, SHELLEXECUTEINFOFc.clazz, "lpFile", "I");
	SHELLEXECUTEINFOFc.lpParameters = (*env)->GetFieldID(env, SHELLEXECUTEINFOFc.clazz, "lpParameters", "I");
	SHELLEXECUTEINFOFc.lpDirectory = (*env)->GetFieldID(env, SHELLEXECUTEINFOFc.clazz, "lpDirectory", "I");
	SHELLEXECUTEINFOFc.nShow = (*env)->GetFieldID(env, SHELLEXECUTEINFOFc.clazz, "nShow", "I");
	SHELLEXECUTEINFOFc.hInstApp = (*env)->GetFieldID(env, SHELLEXECUTEINFOFc.clazz, "hInstApp", "I");
	SHELLEXECUTEINFOFc.lpIDList = (*env)->GetFieldID(env, SHELLEXECUTEINFOFc.clazz, "lpIDList", "I");
	SHELLEXECUTEINFOFc.lpClass = (*env)->GetFieldID(env, SHELLEXECUTEINFOFc.clazz, "lpClass", "I");
	SHELLEXECUTEINFOFc.hkeyClass = (*env)->GetFieldID(env, SHELLEXECUTEINFOFc.clazz, "hkeyClass", "I");
	SHELLEXECUTEINFOFc.dwHotKey = (*env)->GetFieldID(env, SHELLEXECUTEINFOFc.clazz, "dwHotKey", "I");
	SHELLEXECUTEINFOFc.hIcon = (*env)->GetFieldID(env, SHELLEXECUTEINFOFc.clazz, "hIcon", "I");
	SHELLEXECUTEINFOFc.hProcess = (*env)->GetFieldID(env, SHELLEXECUTEINFOFc.clazz, "hProcess", "I");
	SHELLEXECUTEINFOFc.cached = 1;
}

SHELLEXECUTEINFO *getSHELLEXECUTEINFOFields(JNIEnv *env, jobject lpObject, SHELLEXECUTEINFO *lpStruct)
{
	if (!SHELLEXECUTEINFOFc.cached) cacheSHELLEXECUTEINFOFields(env, lpObject);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, SHELLEXECUTEINFOFc.cbSize);
	lpStruct->fMask = (*env)->GetIntField(env, lpObject, SHELLEXECUTEINFOFc.fMask);
	lpStruct->hwnd = (HWND)(*env)->GetIntField(env, lpObject, SHELLEXECUTEINFOFc.hwnd);
	lpStruct->lpVerb = (LPCTSTR)(*env)->GetIntField(env, lpObject, SHELLEXECUTEINFOFc.lpVerb);
	lpStruct->lpFile = (LPCTSTR)(*env)->GetIntField(env, lpObject, SHELLEXECUTEINFOFc.lpFile);
	lpStruct->lpParameters = (LPCTSTR)(*env)->GetIntField(env, lpObject, SHELLEXECUTEINFOFc.lpParameters);
	lpStruct->lpDirectory = (LPCTSTR)(*env)->GetIntField(env, lpObject, SHELLEXECUTEINFOFc.lpDirectory);
	lpStruct->nShow = (*env)->GetIntField(env, lpObject, SHELLEXECUTEINFOFc.nShow);
	lpStruct->hInstApp = (HINSTANCE)(*env)->GetIntField(env, lpObject, SHELLEXECUTEINFOFc.hInstApp);
	lpStruct->lpIDList = (LPVOID)(*env)->GetIntField(env, lpObject, SHELLEXECUTEINFOFc.lpIDList);
	lpStruct->lpClass = (LPCTSTR)(*env)->GetIntField(env, lpObject, SHELLEXECUTEINFOFc.lpClass);
	lpStruct->hkeyClass = (HKEY)(*env)->GetIntField(env, lpObject, SHELLEXECUTEINFOFc.hkeyClass);
	lpStruct->dwHotKey = (*env)->GetIntField(env, lpObject, SHELLEXECUTEINFOFc.dwHotKey);
	lpStruct->hIcon = (HANDLE)(*env)->GetIntField(env, lpObject, SHELLEXECUTEINFOFc.hIcon);
	lpStruct->hProcess = (HANDLE)(*env)->GetIntField(env, lpObject, SHELLEXECUTEINFOFc.hProcess);
	return lpStruct;
}

void setSHELLEXECUTEINFOFields(JNIEnv *env, jobject lpObject, SHELLEXECUTEINFO *lpStruct)
{
	if (!SHELLEXECUTEINFOFc.cached) cacheSHELLEXECUTEINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, SHELLEXECUTEINFOFc.cbSize, (jint)lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, SHELLEXECUTEINFOFc.fMask, (jint)lpStruct->fMask);
	(*env)->SetIntField(env, lpObject, SHELLEXECUTEINFOFc.hwnd, (jint)lpStruct->hwnd);
	(*env)->SetIntField(env, lpObject, SHELLEXECUTEINFOFc.lpVerb, (jint)lpStruct->lpVerb);
	(*env)->SetIntField(env, lpObject, SHELLEXECUTEINFOFc.lpFile, (jint)lpStruct->lpFile);
	(*env)->SetIntField(env, lpObject, SHELLEXECUTEINFOFc.lpParameters, (jint)lpStruct->lpParameters);
	(*env)->SetIntField(env, lpObject, SHELLEXECUTEINFOFc.lpDirectory, (jint)lpStruct->lpDirectory);
	(*env)->SetIntField(env, lpObject, SHELLEXECUTEINFOFc.nShow, (jint)lpStruct->nShow);
	(*env)->SetIntField(env, lpObject, SHELLEXECUTEINFOFc.hInstApp, (jint)lpStruct->hInstApp);
	(*env)->SetIntField(env, lpObject, SHELLEXECUTEINFOFc.lpIDList, (jint)lpStruct->lpIDList);
	(*env)->SetIntField(env, lpObject, SHELLEXECUTEINFOFc.lpClass, (jint)lpStruct->lpClass);
	(*env)->SetIntField(env, lpObject, SHELLEXECUTEINFOFc.hkeyClass, (jint)lpStruct->hkeyClass);
	(*env)->SetIntField(env, lpObject, SHELLEXECUTEINFOFc.dwHotKey, (jint)lpStruct->dwHotKey);
	(*env)->SetIntField(env, lpObject, SHELLEXECUTEINFOFc.hIcon, (jint)lpStruct->hIcon);
	(*env)->SetIntField(env, lpObject, SHELLEXECUTEINFOFc.hProcess, (jint)lpStruct->hProcess);
}
#endif

#ifndef NO_SHMENUBARINFO
typedef struct SHMENUBARINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, hwndParent, dwFlags, nToolBarId, hInstRes, nBmpId, cBmpImages, hwndMB;
} SHMENUBARINFO_FID_CACHE;

SHMENUBARINFO_FID_CACHE SHMENUBARINFOFc;

void cacheSHMENUBARINFOFields(JNIEnv *env, jobject lpObject)
{
	if (SHMENUBARINFOFc.cached) return;
	SHMENUBARINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	SHMENUBARINFOFc.cbSize = (*env)->GetFieldID(env, SHMENUBARINFOFc.clazz, "cbSize", "I");
	SHMENUBARINFOFc.hwndParent = (*env)->GetFieldID(env, SHMENUBARINFOFc.clazz, "hwndParent", "I");
	SHMENUBARINFOFc.dwFlags = (*env)->GetFieldID(env, SHMENUBARINFOFc.clazz, "dwFlags", "I");
	SHMENUBARINFOFc.nToolBarId = (*env)->GetFieldID(env, SHMENUBARINFOFc.clazz, "nToolBarId", "I");
	SHMENUBARINFOFc.hInstRes = (*env)->GetFieldID(env, SHMENUBARINFOFc.clazz, "hInstRes", "I");
	SHMENUBARINFOFc.nBmpId = (*env)->GetFieldID(env, SHMENUBARINFOFc.clazz, "nBmpId", "I");
	SHMENUBARINFOFc.cBmpImages = (*env)->GetFieldID(env, SHMENUBARINFOFc.clazz, "cBmpImages", "I");
	SHMENUBARINFOFc.hwndMB = (*env)->GetFieldID(env, SHMENUBARINFOFc.clazz, "hwndMB", "I");
	SHMENUBARINFOFc.cached = 1;
}

SHMENUBARINFO *getSHMENUBARINFOFields(JNIEnv *env, jobject lpObject, SHMENUBARINFO *lpStruct)
{
	if (!SHMENUBARINFOFc.cached) cacheSHMENUBARINFOFields(env, lpObject);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, SHMENUBARINFOFc.cbSize);
	lpStruct->hwndParent = (HWND)(*env)->GetIntField(env, lpObject, SHMENUBARINFOFc.hwndParent);
	lpStruct->dwFlags = (*env)->GetIntField(env, lpObject, SHMENUBARINFOFc.dwFlags);
	lpStruct->nToolBarId = (*env)->GetIntField(env, lpObject, SHMENUBARINFOFc.nToolBarId);
	lpStruct->hInstRes = (HINSTANCE)(*env)->GetIntField(env, lpObject, SHMENUBARINFOFc.hInstRes);
	lpStruct->nBmpId = (*env)->GetIntField(env, lpObject, SHMENUBARINFOFc.nBmpId);
	lpStruct->cBmpImages = (*env)->GetIntField(env, lpObject, SHMENUBARINFOFc.cBmpImages);
	lpStruct->hwndMB = (HWND)(*env)->GetIntField(env, lpObject, SHMENUBARINFOFc.hwndMB);
	return lpStruct;
}

void setSHMENUBARINFOFields(JNIEnv *env, jobject lpObject, SHMENUBARINFO *lpStruct)
{
	if (!SHMENUBARINFOFc.cached) cacheSHMENUBARINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, SHMENUBARINFOFc.cbSize, (jint)lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, SHMENUBARINFOFc.hwndParent, (jint)lpStruct->hwndParent);
	(*env)->SetIntField(env, lpObject, SHMENUBARINFOFc.dwFlags, (jint)lpStruct->dwFlags);
	(*env)->SetIntField(env, lpObject, SHMENUBARINFOFc.nToolBarId, (jint)lpStruct->nToolBarId);
	(*env)->SetIntField(env, lpObject, SHMENUBARINFOFc.hInstRes, (jint)lpStruct->hInstRes);
	(*env)->SetIntField(env, lpObject, SHMENUBARINFOFc.nBmpId, (jint)lpStruct->nBmpId);
	(*env)->SetIntField(env, lpObject, SHMENUBARINFOFc.cBmpImages, (jint)lpStruct->cBmpImages);
	(*env)->SetIntField(env, lpObject, SHMENUBARINFOFc.hwndMB, (jint)lpStruct->hwndMB);
}
#endif

#ifndef NO_SHRGINFO
typedef struct SHRGINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, hwndClient, ptDown_x, ptDown_y, dwFlags;
} SHRGINFO_FID_CACHE;

SHRGINFO_FID_CACHE SHRGINFOFc;

void cacheSHRGINFOFields(JNIEnv *env, jobject lpObject)
{
	if (SHRGINFOFc.cached) return;
	SHRGINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	SHRGINFOFc.cbSize = (*env)->GetFieldID(env, SHRGINFOFc.clazz, "cbSize", "I");
	SHRGINFOFc.hwndClient = (*env)->GetFieldID(env, SHRGINFOFc.clazz, "hwndClient", "I");
	SHRGINFOFc.ptDown_x = (*env)->GetFieldID(env, SHRGINFOFc.clazz, "ptDown_x", "I");
	SHRGINFOFc.ptDown_y = (*env)->GetFieldID(env, SHRGINFOFc.clazz, "ptDown_y", "I");
	SHRGINFOFc.dwFlags = (*env)->GetFieldID(env, SHRGINFOFc.clazz, "dwFlags", "I");
	SHRGINFOFc.cached = 1;
}

SHRGINFO *getSHRGINFOFields(JNIEnv *env, jobject lpObject, SHRGINFO *lpStruct)
{
	if (!SHRGINFOFc.cached) cacheSHRGINFOFields(env, lpObject);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, SHRGINFOFc.cbSize);
	lpStruct->hwndClient = (HWND)(*env)->GetIntField(env, lpObject, SHRGINFOFc.hwndClient);
	lpStruct->ptDown.x = (*env)->GetIntField(env, lpObject, SHRGINFOFc.ptDown_x);
	lpStruct->ptDown.y = (*env)->GetIntField(env, lpObject, SHRGINFOFc.ptDown_y);
	lpStruct->dwFlags = (*env)->GetIntField(env, lpObject, SHRGINFOFc.dwFlags);
	return lpStruct;
}

void setSHRGINFOFields(JNIEnv *env, jobject lpObject, SHRGINFO *lpStruct)
{
	if (!SHRGINFOFc.cached) cacheSHRGINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, SHRGINFOFc.cbSize, (jint)lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, SHRGINFOFc.hwndClient, (jint)lpStruct->hwndClient);
	(*env)->SetIntField(env, lpObject, SHRGINFOFc.ptDown_x, (jint)lpStruct->ptDown.x);
	(*env)->SetIntField(env, lpObject, SHRGINFOFc.ptDown_y, (jint)lpStruct->ptDown.y);
	(*env)->SetIntField(env, lpObject, SHRGINFOFc.dwFlags, (jint)lpStruct->dwFlags);
}
#endif

#ifndef NO_SIPINFO
typedef struct SIPINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, fdwFlags, rcVisibleDesktop_left, rcVisibleDesktop_top, rcVisibleDesktop_right, rcVisibleDesktop_bottom, rcSipRect_left, rcSipRect_top, rcSipRect_right, rcSipRect_bottom, dwImDataSize, pvImData;
} SIPINFO_FID_CACHE;

SIPINFO_FID_CACHE SIPINFOFc;

void cacheSIPINFOFields(JNIEnv *env, jobject lpObject)
{
	if (SIPINFOFc.cached) return;
	SIPINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	SIPINFOFc.cbSize = (*env)->GetFieldID(env, SIPINFOFc.clazz, "cbSize", "I");
	SIPINFOFc.fdwFlags = (*env)->GetFieldID(env, SIPINFOFc.clazz, "fdwFlags", "I");
	SIPINFOFc.rcVisibleDesktop_left = (*env)->GetFieldID(env, SIPINFOFc.clazz, "rcVisibleDesktop_left", "I");
	SIPINFOFc.rcVisibleDesktop_top = (*env)->GetFieldID(env, SIPINFOFc.clazz, "rcVisibleDesktop_top", "I");
	SIPINFOFc.rcVisibleDesktop_right = (*env)->GetFieldID(env, SIPINFOFc.clazz, "rcVisibleDesktop_right", "I");
	SIPINFOFc.rcVisibleDesktop_bottom = (*env)->GetFieldID(env, SIPINFOFc.clazz, "rcVisibleDesktop_bottom", "I");
	SIPINFOFc.rcSipRect_left = (*env)->GetFieldID(env, SIPINFOFc.clazz, "rcSipRect_left", "I");
	SIPINFOFc.rcSipRect_top = (*env)->GetFieldID(env, SIPINFOFc.clazz, "rcSipRect_top", "I");
	SIPINFOFc.rcSipRect_right = (*env)->GetFieldID(env, SIPINFOFc.clazz, "rcSipRect_right", "I");
	SIPINFOFc.rcSipRect_bottom = (*env)->GetFieldID(env, SIPINFOFc.clazz, "rcSipRect_bottom", "I");
	SIPINFOFc.dwImDataSize = (*env)->GetFieldID(env, SIPINFOFc.clazz, "dwImDataSize", "I");
	SIPINFOFc.pvImData = (*env)->GetFieldID(env, SIPINFOFc.clazz, "pvImData", "I");
	SIPINFOFc.cached = 1;
}

SIPINFO *getSIPINFOFields(JNIEnv *env, jobject lpObject, SIPINFO *lpStruct)
{
	if (!SIPINFOFc.cached) cacheSIPINFOFields(env, lpObject);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, SIPINFOFc.cbSize);
	lpStruct->fdwFlags = (*env)->GetIntField(env, lpObject, SIPINFOFc.fdwFlags);
	lpStruct->rcVisibleDesktop.left = (*env)->GetIntField(env, lpObject, SIPINFOFc.rcVisibleDesktop_left);
	lpStruct->rcVisibleDesktop.top = (*env)->GetIntField(env, lpObject, SIPINFOFc.rcVisibleDesktop_top);
	lpStruct->rcVisibleDesktop.right = (*env)->GetIntField(env, lpObject, SIPINFOFc.rcVisibleDesktop_right);
	lpStruct->rcVisibleDesktop.bottom = (*env)->GetIntField(env, lpObject, SIPINFOFc.rcVisibleDesktop_bottom);
	lpStruct->rcSipRect.left = (*env)->GetIntField(env, lpObject, SIPINFOFc.rcSipRect_left);
	lpStruct->rcSipRect.top = (*env)->GetIntField(env, lpObject, SIPINFOFc.rcSipRect_top);
	lpStruct->rcSipRect.right = (*env)->GetIntField(env, lpObject, SIPINFOFc.rcSipRect_right);
	lpStruct->rcSipRect.bottom = (*env)->GetIntField(env, lpObject, SIPINFOFc.rcSipRect_bottom);
	lpStruct->dwImDataSize = (*env)->GetIntField(env, lpObject, SIPINFOFc.dwImDataSize);
	lpStruct->pvImData = (void *)(*env)->GetIntField(env, lpObject, SIPINFOFc.pvImData);
	return lpStruct;
}

void setSIPINFOFields(JNIEnv *env, jobject lpObject, SIPINFO *lpStruct)
{
	if (!SIPINFOFc.cached) cacheSIPINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, SIPINFOFc.cbSize, (jint)lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, SIPINFOFc.fdwFlags, (jint)lpStruct->fdwFlags);
	(*env)->SetIntField(env, lpObject, SIPINFOFc.rcVisibleDesktop_left, (jint)lpStruct->rcVisibleDesktop.left);
	(*env)->SetIntField(env, lpObject, SIPINFOFc.rcVisibleDesktop_top, (jint)lpStruct->rcVisibleDesktop.top);
	(*env)->SetIntField(env, lpObject, SIPINFOFc.rcVisibleDesktop_right, (jint)lpStruct->rcVisibleDesktop.right);
	(*env)->SetIntField(env, lpObject, SIPINFOFc.rcVisibleDesktop_bottom, (jint)lpStruct->rcVisibleDesktop.bottom);
	(*env)->SetIntField(env, lpObject, SIPINFOFc.rcSipRect_left, (jint)lpStruct->rcSipRect.left);
	(*env)->SetIntField(env, lpObject, SIPINFOFc.rcSipRect_top, (jint)lpStruct->rcSipRect.top);
	(*env)->SetIntField(env, lpObject, SIPINFOFc.rcSipRect_right, (jint)lpStruct->rcSipRect.right);
	(*env)->SetIntField(env, lpObject, SIPINFOFc.rcSipRect_bottom, (jint)lpStruct->rcSipRect.bottom);
	(*env)->SetIntField(env, lpObject, SIPINFOFc.dwImDataSize, (jint)lpStruct->dwImDataSize);
	(*env)->SetIntField(env, lpObject, SIPINFOFc.pvImData, (jint)lpStruct->pvImData);
}
#endif

#ifndef NO_SIZE
typedef struct SIZE_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cx, cy;
} SIZE_FID_CACHE;

SIZE_FID_CACHE SIZEFc;

void cacheSIZEFields(JNIEnv *env, jobject lpObject)
{
	if (SIZEFc.cached) return;
	SIZEFc.clazz = (*env)->GetObjectClass(env, lpObject);
	SIZEFc.cx = (*env)->GetFieldID(env, SIZEFc.clazz, "cx", "I");
	SIZEFc.cy = (*env)->GetFieldID(env, SIZEFc.clazz, "cy", "I");
	SIZEFc.cached = 1;
}

SIZE *getSIZEFields(JNIEnv *env, jobject lpObject, SIZE *lpStruct)
{
	if (!SIZEFc.cached) cacheSIZEFields(env, lpObject);
	lpStruct->cx = (*env)->GetIntField(env, lpObject, SIZEFc.cx);
	lpStruct->cy = (*env)->GetIntField(env, lpObject, SIZEFc.cy);
	return lpStruct;
}

void setSIZEFields(JNIEnv *env, jobject lpObject, SIZE *lpStruct)
{
	if (!SIZEFc.cached) cacheSIZEFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, SIZEFc.cx, (jint)lpStruct->cx);
	(*env)->SetIntField(env, lpObject, SIZEFc.cy, (jint)lpStruct->cy);
}
#endif

#ifndef NO_TBBUTTON
typedef struct TBBUTTON_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID iBitmap, idCommand, fsState, fsStyle, dwData, iString;
} TBBUTTON_FID_CACHE;

TBBUTTON_FID_CACHE TBBUTTONFc;

void cacheTBBUTTONFields(JNIEnv *env, jobject lpObject)
{
	if (TBBUTTONFc.cached) return;
	TBBUTTONFc.clazz = (*env)->GetObjectClass(env, lpObject);
	TBBUTTONFc.iBitmap = (*env)->GetFieldID(env, TBBUTTONFc.clazz, "iBitmap", "I");
	TBBUTTONFc.idCommand = (*env)->GetFieldID(env, TBBUTTONFc.clazz, "idCommand", "I");
	TBBUTTONFc.fsState = (*env)->GetFieldID(env, TBBUTTONFc.clazz, "fsState", "B");
	TBBUTTONFc.fsStyle = (*env)->GetFieldID(env, TBBUTTONFc.clazz, "fsStyle", "B");
	TBBUTTONFc.dwData = (*env)->GetFieldID(env, TBBUTTONFc.clazz, "dwData", "I");
	TBBUTTONFc.iString = (*env)->GetFieldID(env, TBBUTTONFc.clazz, "iString", "I");
	TBBUTTONFc.cached = 1;
}

TBBUTTON *getTBBUTTONFields(JNIEnv *env, jobject lpObject, TBBUTTON *lpStruct)
{
	if (!TBBUTTONFc.cached) cacheTBBUTTONFields(env, lpObject);
	lpStruct->iBitmap = (*env)->GetIntField(env, lpObject, TBBUTTONFc.iBitmap);
	lpStruct->idCommand = (*env)->GetIntField(env, lpObject, TBBUTTONFc.idCommand);
	lpStruct->fsState = (*env)->GetByteField(env, lpObject, TBBUTTONFc.fsState);
	lpStruct->fsStyle = (*env)->GetByteField(env, lpObject, TBBUTTONFc.fsStyle);
	lpStruct->dwData = (*env)->GetIntField(env, lpObject, TBBUTTONFc.dwData);
	lpStruct->iString = (*env)->GetIntField(env, lpObject, TBBUTTONFc.iString);
	return lpStruct;
}

void setTBBUTTONFields(JNIEnv *env, jobject lpObject, TBBUTTON *lpStruct)
{
	if (!TBBUTTONFc.cached) cacheTBBUTTONFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, TBBUTTONFc.iBitmap, (jint)lpStruct->iBitmap);
	(*env)->SetIntField(env, lpObject, TBBUTTONFc.idCommand, (jint)lpStruct->idCommand);
	(*env)->SetByteField(env, lpObject, TBBUTTONFc.fsState, (jbyte)lpStruct->fsState);
	(*env)->SetByteField(env, lpObject, TBBUTTONFc.fsStyle, (jbyte)lpStruct->fsStyle);
	(*env)->SetIntField(env, lpObject, TBBUTTONFc.dwData, (jint)lpStruct->dwData);
	(*env)->SetIntField(env, lpObject, TBBUTTONFc.iString, (jint)lpStruct->iString);
}
#endif

#ifndef NO_TBBUTTONINFO
typedef struct TBBUTTONINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, dwMask, idCommand, iImage, fsState, fsStyle, cx, lParam, pszText, cchText;
} TBBUTTONINFO_FID_CACHE;

TBBUTTONINFO_FID_CACHE TBBUTTONINFOFc;

void cacheTBBUTTONINFOFields(JNIEnv *env, jobject lpObject)
{
	if (TBBUTTONINFOFc.cached) return;
	TBBUTTONINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	TBBUTTONINFOFc.cbSize = (*env)->GetFieldID(env, TBBUTTONINFOFc.clazz, "cbSize", "I");
	TBBUTTONINFOFc.dwMask = (*env)->GetFieldID(env, TBBUTTONINFOFc.clazz, "dwMask", "I");
	TBBUTTONINFOFc.idCommand = (*env)->GetFieldID(env, TBBUTTONINFOFc.clazz, "idCommand", "I");
	TBBUTTONINFOFc.iImage = (*env)->GetFieldID(env, TBBUTTONINFOFc.clazz, "iImage", "I");
	TBBUTTONINFOFc.fsState = (*env)->GetFieldID(env, TBBUTTONINFOFc.clazz, "fsState", "B");
	TBBUTTONINFOFc.fsStyle = (*env)->GetFieldID(env, TBBUTTONINFOFc.clazz, "fsStyle", "B");
	TBBUTTONINFOFc.cx = (*env)->GetFieldID(env, TBBUTTONINFOFc.clazz, "cx", "S");
	TBBUTTONINFOFc.lParam = (*env)->GetFieldID(env, TBBUTTONINFOFc.clazz, "lParam", "I");
	TBBUTTONINFOFc.pszText = (*env)->GetFieldID(env, TBBUTTONINFOFc.clazz, "pszText", "I");
	TBBUTTONINFOFc.cchText = (*env)->GetFieldID(env, TBBUTTONINFOFc.clazz, "cchText", "I");
	TBBUTTONINFOFc.cached = 1;
}

TBBUTTONINFO *getTBBUTTONINFOFields(JNIEnv *env, jobject lpObject, TBBUTTONINFO *lpStruct)
{
	if (!TBBUTTONINFOFc.cached) cacheTBBUTTONINFOFields(env, lpObject);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, TBBUTTONINFOFc.cbSize);
	lpStruct->dwMask = (*env)->GetIntField(env, lpObject, TBBUTTONINFOFc.dwMask);
	lpStruct->idCommand = (*env)->GetIntField(env, lpObject, TBBUTTONINFOFc.idCommand);
	lpStruct->iImage = (*env)->GetIntField(env, lpObject, TBBUTTONINFOFc.iImage);
	lpStruct->fsState = (*env)->GetByteField(env, lpObject, TBBUTTONINFOFc.fsState);
	lpStruct->fsStyle = (*env)->GetByteField(env, lpObject, TBBUTTONINFOFc.fsStyle);
	lpStruct->cx = (*env)->GetShortField(env, lpObject, TBBUTTONINFOFc.cx);
	lpStruct->lParam = (*env)->GetIntField(env, lpObject, TBBUTTONINFOFc.lParam);
	lpStruct->pszText = (LPTSTR)(*env)->GetIntField(env, lpObject, TBBUTTONINFOFc.pszText);
	lpStruct->cchText = (*env)->GetIntField(env, lpObject, TBBUTTONINFOFc.cchText);
	return lpStruct;
}

void setTBBUTTONINFOFields(JNIEnv *env, jobject lpObject, TBBUTTONINFO *lpStruct)
{
	if (!TBBUTTONINFOFc.cached) cacheTBBUTTONINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, TBBUTTONINFOFc.cbSize, (jint)lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, TBBUTTONINFOFc.dwMask, (jint)lpStruct->dwMask);
	(*env)->SetIntField(env, lpObject, TBBUTTONINFOFc.idCommand, (jint)lpStruct->idCommand);
	(*env)->SetIntField(env, lpObject, TBBUTTONINFOFc.iImage, (jint)lpStruct->iImage);
	(*env)->SetByteField(env, lpObject, TBBUTTONINFOFc.fsState, (jbyte)lpStruct->fsState);
	(*env)->SetByteField(env, lpObject, TBBUTTONINFOFc.fsStyle, (jbyte)lpStruct->fsStyle);
	(*env)->SetShortField(env, lpObject, TBBUTTONINFOFc.cx, (jshort)lpStruct->cx);
	(*env)->SetIntField(env, lpObject, TBBUTTONINFOFc.lParam, (jint)lpStruct->lParam);
	(*env)->SetIntField(env, lpObject, TBBUTTONINFOFc.pszText, (jint)lpStruct->pszText);
	(*env)->SetIntField(env, lpObject, TBBUTTONINFOFc.cchText, (jint)lpStruct->cchText);
}
#endif

#ifndef NO_TCITEM
typedef struct TCITEM_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID mask, dwState, dwStateMask, pszText, cchTextMax, iImage, lParam;
} TCITEM_FID_CACHE;

TCITEM_FID_CACHE TCITEMFc;

void cacheTCITEMFields(JNIEnv *env, jobject lpObject)
{
	if (TCITEMFc.cached) return;
	TCITEMFc.clazz = (*env)->GetObjectClass(env, lpObject);
	TCITEMFc.mask = (*env)->GetFieldID(env, TCITEMFc.clazz, "mask", "I");
	TCITEMFc.dwState = (*env)->GetFieldID(env, TCITEMFc.clazz, "dwState", "I");
	TCITEMFc.dwStateMask = (*env)->GetFieldID(env, TCITEMFc.clazz, "dwStateMask", "I");
	TCITEMFc.pszText = (*env)->GetFieldID(env, TCITEMFc.clazz, "pszText", "I");
	TCITEMFc.cchTextMax = (*env)->GetFieldID(env, TCITEMFc.clazz, "cchTextMax", "I");
	TCITEMFc.iImage = (*env)->GetFieldID(env, TCITEMFc.clazz, "iImage", "I");
	TCITEMFc.lParam = (*env)->GetFieldID(env, TCITEMFc.clazz, "lParam", "I");
	TCITEMFc.cached = 1;
}

TCITEM *getTCITEMFields(JNIEnv *env, jobject lpObject, TCITEM *lpStruct)
{
	if (!TCITEMFc.cached) cacheTCITEMFields(env, lpObject);
	lpStruct->mask = (*env)->GetIntField(env, lpObject, TCITEMFc.mask);
	lpStruct->dwState = (*env)->GetIntField(env, lpObject, TCITEMFc.dwState);
	lpStruct->dwStateMask = (*env)->GetIntField(env, lpObject, TCITEMFc.dwStateMask);
	lpStruct->pszText = (LPTSTR)(*env)->GetIntField(env, lpObject, TCITEMFc.pszText);
	lpStruct->cchTextMax = (*env)->GetIntField(env, lpObject, TCITEMFc.cchTextMax);
	lpStruct->iImage = (*env)->GetIntField(env, lpObject, TCITEMFc.iImage);
	lpStruct->lParam = (*env)->GetIntField(env, lpObject, TCITEMFc.lParam);
	return lpStruct;
}

void setTCITEMFields(JNIEnv *env, jobject lpObject, TCITEM *lpStruct)
{
	if (!TCITEMFc.cached) cacheTCITEMFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, TCITEMFc.mask, (jint)lpStruct->mask);
	(*env)->SetIntField(env, lpObject, TCITEMFc.dwState, (jint)lpStruct->dwState);
	(*env)->SetIntField(env, lpObject, TCITEMFc.dwStateMask, (jint)lpStruct->dwStateMask);
	(*env)->SetIntField(env, lpObject, TCITEMFc.pszText, (jint)lpStruct->pszText);
	(*env)->SetIntField(env, lpObject, TCITEMFc.cchTextMax, (jint)lpStruct->cchTextMax);
	(*env)->SetIntField(env, lpObject, TCITEMFc.iImage, (jint)lpStruct->iImage);
	(*env)->SetIntField(env, lpObject, TCITEMFc.lParam, (jint)lpStruct->lParam);
}
#endif

#ifndef NO_TEXTMETRIC
typedef struct TEXTMETRIC_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID tmHeight, tmAscent, tmDescent, tmInternalLeading, tmExternalLeading, tmAveCharWidth, tmMaxCharWidth, tmWeight, tmOverhang, tmDigitizedAspectX, tmDigitizedAspectY, tmItalic, tmUnderlined, tmStruckOut, tmPitchAndFamily, tmCharSet;
} TEXTMETRIC_FID_CACHE;

TEXTMETRIC_FID_CACHE TEXTMETRICFc;

void cacheTEXTMETRICFields(JNIEnv *env, jobject lpObject)
{
	if (TEXTMETRICFc.cached) return;
	TEXTMETRICFc.clazz = (*env)->GetObjectClass(env, lpObject);
	TEXTMETRICFc.tmHeight = (*env)->GetFieldID(env, TEXTMETRICFc.clazz, "tmHeight", "I");
	TEXTMETRICFc.tmAscent = (*env)->GetFieldID(env, TEXTMETRICFc.clazz, "tmAscent", "I");
	TEXTMETRICFc.tmDescent = (*env)->GetFieldID(env, TEXTMETRICFc.clazz, "tmDescent", "I");
	TEXTMETRICFc.tmInternalLeading = (*env)->GetFieldID(env, TEXTMETRICFc.clazz, "tmInternalLeading", "I");
	TEXTMETRICFc.tmExternalLeading = (*env)->GetFieldID(env, TEXTMETRICFc.clazz, "tmExternalLeading", "I");
	TEXTMETRICFc.tmAveCharWidth = (*env)->GetFieldID(env, TEXTMETRICFc.clazz, "tmAveCharWidth", "I");
	TEXTMETRICFc.tmMaxCharWidth = (*env)->GetFieldID(env, TEXTMETRICFc.clazz, "tmMaxCharWidth", "I");
	TEXTMETRICFc.tmWeight = (*env)->GetFieldID(env, TEXTMETRICFc.clazz, "tmWeight", "I");
	TEXTMETRICFc.tmOverhang = (*env)->GetFieldID(env, TEXTMETRICFc.clazz, "tmOverhang", "I");
	TEXTMETRICFc.tmDigitizedAspectX = (*env)->GetFieldID(env, TEXTMETRICFc.clazz, "tmDigitizedAspectX", "I");
	TEXTMETRICFc.tmDigitizedAspectY = (*env)->GetFieldID(env, TEXTMETRICFc.clazz, "tmDigitizedAspectY", "I");
	TEXTMETRICFc.tmItalic = (*env)->GetFieldID(env, TEXTMETRICFc.clazz, "tmItalic", "B");
	TEXTMETRICFc.tmUnderlined = (*env)->GetFieldID(env, TEXTMETRICFc.clazz, "tmUnderlined", "B");
	TEXTMETRICFc.tmStruckOut = (*env)->GetFieldID(env, TEXTMETRICFc.clazz, "tmStruckOut", "B");
	TEXTMETRICFc.tmPitchAndFamily = (*env)->GetFieldID(env, TEXTMETRICFc.clazz, "tmPitchAndFamily", "B");
	TEXTMETRICFc.tmCharSet = (*env)->GetFieldID(env, TEXTMETRICFc.clazz, "tmCharSet", "B");
	TEXTMETRICFc.cached = 1;
}

TEXTMETRIC *getTEXTMETRICFields(JNIEnv *env, jobject lpObject, TEXTMETRIC *lpStruct)
{
	if (!TEXTMETRICFc.cached) cacheTEXTMETRICFields(env, lpObject);
	lpStruct->tmHeight = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmHeight);
	lpStruct->tmAscent = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmAscent);
	lpStruct->tmDescent = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmDescent);
	lpStruct->tmInternalLeading = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmInternalLeading);
	lpStruct->tmExternalLeading = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmExternalLeading);
	lpStruct->tmAveCharWidth = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmAveCharWidth);
	lpStruct->tmMaxCharWidth = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmMaxCharWidth);
	lpStruct->tmWeight = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmWeight);
	lpStruct->tmOverhang = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmOverhang);
	lpStruct->tmDigitizedAspectX = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmDigitizedAspectX);
	lpStruct->tmDigitizedAspectY = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmDigitizedAspectY);
	lpStruct->tmItalic = (*env)->GetByteField(env, lpObject, TEXTMETRICFc.tmItalic);
	lpStruct->tmUnderlined = (*env)->GetByteField(env, lpObject, TEXTMETRICFc.tmUnderlined);
	lpStruct->tmStruckOut = (*env)->GetByteField(env, lpObject, TEXTMETRICFc.tmStruckOut);
	lpStruct->tmPitchAndFamily = (*env)->GetByteField(env, lpObject, TEXTMETRICFc.tmPitchAndFamily);
	lpStruct->tmCharSet = (*env)->GetByteField(env, lpObject, TEXTMETRICFc.tmCharSet);
	return lpStruct;
}

void setTEXTMETRICFields(JNIEnv *env, jobject lpObject, TEXTMETRIC *lpStruct)
{
	if (!TEXTMETRICFc.cached) cacheTEXTMETRICFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmHeight, (jint)lpStruct->tmHeight);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmAscent, (jint)lpStruct->tmAscent);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmDescent, (jint)lpStruct->tmDescent);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmInternalLeading, (jint)lpStruct->tmInternalLeading);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmExternalLeading, (jint)lpStruct->tmExternalLeading);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmAveCharWidth, (jint)lpStruct->tmAveCharWidth);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmMaxCharWidth, (jint)lpStruct->tmMaxCharWidth);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmWeight, (jint)lpStruct->tmWeight);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmOverhang, (jint)lpStruct->tmOverhang);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmDigitizedAspectX, (jint)lpStruct->tmDigitizedAspectX);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmDigitizedAspectY, (jint)lpStruct->tmDigitizedAspectY);
	(*env)->SetByteField(env, lpObject, TEXTMETRICFc.tmItalic, (jbyte)lpStruct->tmItalic);
	(*env)->SetByteField(env, lpObject, TEXTMETRICFc.tmUnderlined, (jbyte)lpStruct->tmUnderlined);
	(*env)->SetByteField(env, lpObject, TEXTMETRICFc.tmStruckOut, (jbyte)lpStruct->tmStruckOut);
	(*env)->SetByteField(env, lpObject, TEXTMETRICFc.tmPitchAndFamily, (jbyte)lpStruct->tmPitchAndFamily);
	(*env)->SetByteField(env, lpObject, TEXTMETRICFc.tmCharSet, (jbyte)lpStruct->tmCharSet);
}
#endif

#ifndef NO_TEXTMETRICA
typedef struct TEXTMETRICA_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID tmFirstChar, tmLastChar, tmDefaultChar, tmBreakChar;
} TEXTMETRICA_FID_CACHE;

TEXTMETRICA_FID_CACHE TEXTMETRICAFc;

void cacheTEXTMETRICAFields(JNIEnv *env, jobject lpObject)
{
	if (TEXTMETRICAFc.cached) return;
	cacheTEXTMETRICFields(env, lpObject);
	TEXTMETRICAFc.clazz = (*env)->GetObjectClass(env, lpObject);
	TEXTMETRICAFc.tmFirstChar = (*env)->GetFieldID(env, TEXTMETRICAFc.clazz, "tmFirstChar", "B");
	TEXTMETRICAFc.tmLastChar = (*env)->GetFieldID(env, TEXTMETRICAFc.clazz, "tmLastChar", "B");
	TEXTMETRICAFc.tmDefaultChar = (*env)->GetFieldID(env, TEXTMETRICAFc.clazz, "tmDefaultChar", "B");
	TEXTMETRICAFc.tmBreakChar = (*env)->GetFieldID(env, TEXTMETRICAFc.clazz, "tmBreakChar", "B");
	TEXTMETRICAFc.cached = 1;
}

TEXTMETRICA *getTEXTMETRICAFields(JNIEnv *env, jobject lpObject, TEXTMETRICA *lpStruct)
{
	if (!TEXTMETRICAFc.cached) cacheTEXTMETRICAFields(env, lpObject);
	lpStruct->tmHeight = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmHeight);
	lpStruct->tmAscent = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmAscent);
	lpStruct->tmDescent = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmDescent);
	lpStruct->tmInternalLeading = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmInternalLeading);
	lpStruct->tmExternalLeading = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmExternalLeading);
	lpStruct->tmAveCharWidth = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmAveCharWidth);
	lpStruct->tmMaxCharWidth = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmMaxCharWidth);
	lpStruct->tmWeight = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmWeight);
	lpStruct->tmOverhang = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmOverhang);
	lpStruct->tmDigitizedAspectX = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmDigitizedAspectX);
	lpStruct->tmDigitizedAspectY = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmDigitizedAspectY);
	lpStruct->tmItalic = (*env)->GetByteField(env, lpObject, TEXTMETRICFc.tmItalic);
	lpStruct->tmUnderlined = (*env)->GetByteField(env, lpObject, TEXTMETRICFc.tmUnderlined);
	lpStruct->tmStruckOut = (*env)->GetByteField(env, lpObject, TEXTMETRICFc.tmStruckOut);
	lpStruct->tmPitchAndFamily = (*env)->GetByteField(env, lpObject, TEXTMETRICFc.tmPitchAndFamily);
	lpStruct->tmCharSet = (*env)->GetByteField(env, lpObject, TEXTMETRICFc.tmCharSet);
	lpStruct->tmFirstChar = (*env)->GetByteField(env, lpObject, TEXTMETRICAFc.tmFirstChar);
	lpStruct->tmLastChar = (*env)->GetByteField(env, lpObject, TEXTMETRICAFc.tmLastChar);
	lpStruct->tmDefaultChar = (*env)->GetByteField(env, lpObject, TEXTMETRICAFc.tmDefaultChar);
	lpStruct->tmBreakChar = (*env)->GetByteField(env, lpObject, TEXTMETRICAFc.tmBreakChar);
	return lpStruct;
}

void setTEXTMETRICAFields(JNIEnv *env, jobject lpObject, TEXTMETRICA *lpStruct)
{
	if (!TEXTMETRICAFc.cached) cacheTEXTMETRICAFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmHeight, (jint)lpStruct->tmHeight);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmAscent, (jint)lpStruct->tmAscent);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmDescent, (jint)lpStruct->tmDescent);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmInternalLeading, (jint)lpStruct->tmInternalLeading);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmExternalLeading, (jint)lpStruct->tmExternalLeading);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmAveCharWidth, (jint)lpStruct->tmAveCharWidth);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmMaxCharWidth, (jint)lpStruct->tmMaxCharWidth);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmWeight, (jint)lpStruct->tmWeight);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmOverhang, (jint)lpStruct->tmOverhang);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmDigitizedAspectX, (jint)lpStruct->tmDigitizedAspectX);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmDigitizedAspectY, (jint)lpStruct->tmDigitizedAspectY);
	(*env)->SetByteField(env, lpObject, TEXTMETRICFc.tmItalic, (jbyte)lpStruct->tmItalic);
	(*env)->SetByteField(env, lpObject, TEXTMETRICFc.tmUnderlined, (jbyte)lpStruct->tmUnderlined);
	(*env)->SetByteField(env, lpObject, TEXTMETRICFc.tmStruckOut, (jbyte)lpStruct->tmStruckOut);
	(*env)->SetByteField(env, lpObject, TEXTMETRICFc.tmPitchAndFamily, (jbyte)lpStruct->tmPitchAndFamily);
	(*env)->SetByteField(env, lpObject, TEXTMETRICFc.tmCharSet, (jbyte)lpStruct->tmCharSet);
	(*env)->SetByteField(env, lpObject, TEXTMETRICAFc.tmFirstChar, (jbyte)lpStruct->tmFirstChar);
	(*env)->SetByteField(env, lpObject, TEXTMETRICAFc.tmLastChar, (jbyte)lpStruct->tmLastChar);
	(*env)->SetByteField(env, lpObject, TEXTMETRICAFc.tmDefaultChar, (jbyte)lpStruct->tmDefaultChar);
	(*env)->SetByteField(env, lpObject, TEXTMETRICAFc.tmBreakChar, (jbyte)lpStruct->tmBreakChar);
}
#endif

#ifndef NO_TEXTMETRICW
typedef struct TEXTMETRICW_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID tmFirstChar, tmLastChar, tmDefaultChar, tmBreakChar;
} TEXTMETRICW_FID_CACHE;

TEXTMETRICW_FID_CACHE TEXTMETRICWFc;

void cacheTEXTMETRICWFields(JNIEnv *env, jobject lpObject)
{
	if (TEXTMETRICWFc.cached) return;
	cacheTEXTMETRICFields(env, lpObject);
	TEXTMETRICWFc.clazz = (*env)->GetObjectClass(env, lpObject);
	TEXTMETRICWFc.tmFirstChar = (*env)->GetFieldID(env, TEXTMETRICWFc.clazz, "tmFirstChar", "C");
	TEXTMETRICWFc.tmLastChar = (*env)->GetFieldID(env, TEXTMETRICWFc.clazz, "tmLastChar", "C");
	TEXTMETRICWFc.tmDefaultChar = (*env)->GetFieldID(env, TEXTMETRICWFc.clazz, "tmDefaultChar", "C");
	TEXTMETRICWFc.tmBreakChar = (*env)->GetFieldID(env, TEXTMETRICWFc.clazz, "tmBreakChar", "C");
	TEXTMETRICWFc.cached = 1;
}

TEXTMETRICW *getTEXTMETRICWFields(JNIEnv *env, jobject lpObject, TEXTMETRICW *lpStruct)
{
	if (!TEXTMETRICWFc.cached) cacheTEXTMETRICWFields(env, lpObject);
	lpStruct->tmHeight = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmHeight);
	lpStruct->tmAscent = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmAscent);
	lpStruct->tmDescent = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmDescent);
	lpStruct->tmInternalLeading = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmInternalLeading);
	lpStruct->tmExternalLeading = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmExternalLeading);
	lpStruct->tmAveCharWidth = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmAveCharWidth);
	lpStruct->tmMaxCharWidth = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmMaxCharWidth);
	lpStruct->tmWeight = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmWeight);
	lpStruct->tmOverhang = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmOverhang);
	lpStruct->tmDigitizedAspectX = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmDigitizedAspectX);
	lpStruct->tmDigitizedAspectY = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmDigitizedAspectY);
	lpStruct->tmItalic = (*env)->GetByteField(env, lpObject, TEXTMETRICFc.tmItalic);
	lpStruct->tmUnderlined = (*env)->GetByteField(env, lpObject, TEXTMETRICFc.tmUnderlined);
	lpStruct->tmStruckOut = (*env)->GetByteField(env, lpObject, TEXTMETRICFc.tmStruckOut);
	lpStruct->tmPitchAndFamily = (*env)->GetByteField(env, lpObject, TEXTMETRICFc.tmPitchAndFamily);
	lpStruct->tmCharSet = (*env)->GetByteField(env, lpObject, TEXTMETRICFc.tmCharSet);
	lpStruct->tmFirstChar = (*env)->GetCharField(env, lpObject, TEXTMETRICWFc.tmFirstChar);
	lpStruct->tmLastChar = (*env)->GetCharField(env, lpObject, TEXTMETRICWFc.tmLastChar);
	lpStruct->tmDefaultChar = (*env)->GetCharField(env, lpObject, TEXTMETRICWFc.tmDefaultChar);
	lpStruct->tmBreakChar = (*env)->GetCharField(env, lpObject, TEXTMETRICWFc.tmBreakChar);
	return lpStruct;
}

void setTEXTMETRICWFields(JNIEnv *env, jobject lpObject, TEXTMETRICW *lpStruct)
{
	if (!TEXTMETRICWFc.cached) cacheTEXTMETRICWFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmHeight, (jint)lpStruct->tmHeight);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmAscent, (jint)lpStruct->tmAscent);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmDescent, (jint)lpStruct->tmDescent);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmInternalLeading, (jint)lpStruct->tmInternalLeading);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmExternalLeading, (jint)lpStruct->tmExternalLeading);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmAveCharWidth, (jint)lpStruct->tmAveCharWidth);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmMaxCharWidth, (jint)lpStruct->tmMaxCharWidth);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmWeight, (jint)lpStruct->tmWeight);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmOverhang, (jint)lpStruct->tmOverhang);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmDigitizedAspectX, (jint)lpStruct->tmDigitizedAspectX);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmDigitizedAspectY, (jint)lpStruct->tmDigitizedAspectY);
	(*env)->SetByteField(env, lpObject, TEXTMETRICFc.tmItalic, (jbyte)lpStruct->tmItalic);
	(*env)->SetByteField(env, lpObject, TEXTMETRICFc.tmUnderlined, (jbyte)lpStruct->tmUnderlined);
	(*env)->SetByteField(env, lpObject, TEXTMETRICFc.tmStruckOut, (jbyte)lpStruct->tmStruckOut);
	(*env)->SetByteField(env, lpObject, TEXTMETRICFc.tmPitchAndFamily, (jbyte)lpStruct->tmPitchAndFamily);
	(*env)->SetByteField(env, lpObject, TEXTMETRICFc.tmCharSet, (jbyte)lpStruct->tmCharSet);
	(*env)->SetCharField(env, lpObject, TEXTMETRICWFc.tmFirstChar, (jchar)lpStruct->tmFirstChar);
	(*env)->SetCharField(env, lpObject, TEXTMETRICWFc.tmLastChar, (jchar)lpStruct->tmLastChar);
	(*env)->SetCharField(env, lpObject, TEXTMETRICWFc.tmDefaultChar, (jchar)lpStruct->tmDefaultChar);
	(*env)->SetCharField(env, lpObject, TEXTMETRICWFc.tmBreakChar, (jchar)lpStruct->tmBreakChar);
}
#endif

#ifndef NO_TOOLINFO
typedef struct TOOLINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, uFlags, hwnd, uId, left, top, right, bottom, hinst, lpszText, lParam;
} TOOLINFO_FID_CACHE;

TOOLINFO_FID_CACHE TOOLINFOFc;

void cacheTOOLINFOFields(JNIEnv *env, jobject lpObject)
{
	if (TOOLINFOFc.cached) return;
	TOOLINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	TOOLINFOFc.cbSize = (*env)->GetFieldID(env, TOOLINFOFc.clazz, "cbSize", "I");
	TOOLINFOFc.uFlags = (*env)->GetFieldID(env, TOOLINFOFc.clazz, "uFlags", "I");
	TOOLINFOFc.hwnd = (*env)->GetFieldID(env, TOOLINFOFc.clazz, "hwnd", "I");
	TOOLINFOFc.uId = (*env)->GetFieldID(env, TOOLINFOFc.clazz, "uId", "I");
	TOOLINFOFc.left = (*env)->GetFieldID(env, TOOLINFOFc.clazz, "left", "I");
	TOOLINFOFc.top = (*env)->GetFieldID(env, TOOLINFOFc.clazz, "top", "I");
	TOOLINFOFc.right = (*env)->GetFieldID(env, TOOLINFOFc.clazz, "right", "I");
	TOOLINFOFc.bottom = (*env)->GetFieldID(env, TOOLINFOFc.clazz, "bottom", "I");
	TOOLINFOFc.hinst = (*env)->GetFieldID(env, TOOLINFOFc.clazz, "hinst", "I");
	TOOLINFOFc.lpszText = (*env)->GetFieldID(env, TOOLINFOFc.clazz, "lpszText", "I");
	TOOLINFOFc.lParam = (*env)->GetFieldID(env, TOOLINFOFc.clazz, "lParam", "I");
	TOOLINFOFc.cached = 1;
}

TOOLINFO *getTOOLINFOFields(JNIEnv *env, jobject lpObject, TOOLINFO *lpStruct)
{
	if (!TOOLINFOFc.cached) cacheTOOLINFOFields(env, lpObject);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, TOOLINFOFc.cbSize);
	lpStruct->uFlags = (*env)->GetIntField(env, lpObject, TOOLINFOFc.uFlags);
	lpStruct->hwnd = (HWND)(*env)->GetIntField(env, lpObject, TOOLINFOFc.hwnd);
	lpStruct->uId = (*env)->GetIntField(env, lpObject, TOOLINFOFc.uId);
	lpStruct->rect.left = (*env)->GetIntField(env, lpObject, TOOLINFOFc.left);
	lpStruct->rect.top = (*env)->GetIntField(env, lpObject, TOOLINFOFc.top);
	lpStruct->rect.right = (*env)->GetIntField(env, lpObject, TOOLINFOFc.right);
	lpStruct->rect.bottom = (*env)->GetIntField(env, lpObject, TOOLINFOFc.bottom);
	lpStruct->hinst = (HINSTANCE)(*env)->GetIntField(env, lpObject, TOOLINFOFc.hinst);
	lpStruct->lpszText = (LPTSTR)(*env)->GetIntField(env, lpObject, TOOLINFOFc.lpszText);
	lpStruct->lParam = (*env)->GetIntField(env, lpObject, TOOLINFOFc.lParam);
	return lpStruct;
}

void setTOOLINFOFields(JNIEnv *env, jobject lpObject, TOOLINFO *lpStruct)
{
	if (!TOOLINFOFc.cached) cacheTOOLINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, TOOLINFOFc.cbSize, (jint)lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, TOOLINFOFc.uFlags, (jint)lpStruct->uFlags);
	(*env)->SetIntField(env, lpObject, TOOLINFOFc.hwnd, (jint)lpStruct->hwnd);
	(*env)->SetIntField(env, lpObject, TOOLINFOFc.uId, (jint)lpStruct->uId);
	(*env)->SetIntField(env, lpObject, TOOLINFOFc.left, (jint)lpStruct->rect.left);
	(*env)->SetIntField(env, lpObject, TOOLINFOFc.top, (jint)lpStruct->rect.top);
	(*env)->SetIntField(env, lpObject, TOOLINFOFc.right, (jint)lpStruct->rect.right);
	(*env)->SetIntField(env, lpObject, TOOLINFOFc.bottom, (jint)lpStruct->rect.bottom);
	(*env)->SetIntField(env, lpObject, TOOLINFOFc.hinst, (jint)lpStruct->hinst);
	(*env)->SetIntField(env, lpObject, TOOLINFOFc.lpszText, (jint)lpStruct->lpszText);
	(*env)->SetIntField(env, lpObject, TOOLINFOFc.lParam, (jint)lpStruct->lParam);
}
#endif

#ifndef NO_TRACKMOUSEEVENT
typedef struct TRACKMOUSEEVENT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, dwFlags, hwndTrack, dwHoverTime;
} TRACKMOUSEEVENT_FID_CACHE;

TRACKMOUSEEVENT_FID_CACHE TRACKMOUSEEVENTFc;

void cacheTRACKMOUSEEVENTFields(JNIEnv *env, jobject lpObject)
{
	if (TRACKMOUSEEVENTFc.cached) return;
	TRACKMOUSEEVENTFc.clazz = (*env)->GetObjectClass(env, lpObject);
	TRACKMOUSEEVENTFc.cbSize = (*env)->GetFieldID(env, TRACKMOUSEEVENTFc.clazz, "cbSize", "I");
	TRACKMOUSEEVENTFc.dwFlags = (*env)->GetFieldID(env, TRACKMOUSEEVENTFc.clazz, "dwFlags", "I");
	TRACKMOUSEEVENTFc.hwndTrack = (*env)->GetFieldID(env, TRACKMOUSEEVENTFc.clazz, "hwndTrack", "I");
	TRACKMOUSEEVENTFc.dwHoverTime = (*env)->GetFieldID(env, TRACKMOUSEEVENTFc.clazz, "dwHoverTime", "I");
	TRACKMOUSEEVENTFc.cached = 1;
}

TRACKMOUSEEVENT *getTRACKMOUSEEVENTFields(JNIEnv *env, jobject lpObject, TRACKMOUSEEVENT *lpStruct)
{
	if (!TRACKMOUSEEVENTFc.cached) cacheTRACKMOUSEEVENTFields(env, lpObject);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, TRACKMOUSEEVENTFc.cbSize);
	lpStruct->dwFlags = (*env)->GetIntField(env, lpObject, TRACKMOUSEEVENTFc.dwFlags);
	lpStruct->hwndTrack = (HWND)(*env)->GetIntField(env, lpObject, TRACKMOUSEEVENTFc.hwndTrack);
	lpStruct->dwHoverTime = (*env)->GetIntField(env, lpObject, TRACKMOUSEEVENTFc.dwHoverTime);
	return lpStruct;
}

void setTRACKMOUSEEVENTFields(JNIEnv *env, jobject lpObject, TRACKMOUSEEVENT *lpStruct)
{
	if (!TRACKMOUSEEVENTFc.cached) cacheTRACKMOUSEEVENTFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, TRACKMOUSEEVENTFc.cbSize, (jint)lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, TRACKMOUSEEVENTFc.dwFlags, (jint)lpStruct->dwFlags);
	(*env)->SetIntField(env, lpObject, TRACKMOUSEEVENTFc.hwndTrack, (jint)lpStruct->hwndTrack);
	(*env)->SetIntField(env, lpObject, TRACKMOUSEEVENTFc.dwHoverTime, (jint)lpStruct->dwHoverTime);
}
#endif

#ifndef NO_TRIVERTEX
typedef struct TRIVERTEX_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID x, y, Red, Green, Blue, Alpha;
} TRIVERTEX_FID_CACHE;

TRIVERTEX_FID_CACHE TRIVERTEXFc;

void cacheTRIVERTEXFields(JNIEnv *env, jobject lpObject)
{
	if (TRIVERTEXFc.cached) return;
	TRIVERTEXFc.clazz = (*env)->GetObjectClass(env, lpObject);
	TRIVERTEXFc.x = (*env)->GetFieldID(env, TRIVERTEXFc.clazz, "x", "I");
	TRIVERTEXFc.y = (*env)->GetFieldID(env, TRIVERTEXFc.clazz, "y", "I");
	TRIVERTEXFc.Red = (*env)->GetFieldID(env, TRIVERTEXFc.clazz, "Red", "S");
	TRIVERTEXFc.Green = (*env)->GetFieldID(env, TRIVERTEXFc.clazz, "Green", "S");
	TRIVERTEXFc.Blue = (*env)->GetFieldID(env, TRIVERTEXFc.clazz, "Blue", "S");
	TRIVERTEXFc.Alpha = (*env)->GetFieldID(env, TRIVERTEXFc.clazz, "Alpha", "S");
	TRIVERTEXFc.cached = 1;
}

TRIVERTEX *getTRIVERTEXFields(JNIEnv *env, jobject lpObject, TRIVERTEX *lpStruct)
{
	if (!TRIVERTEXFc.cached) cacheTRIVERTEXFields(env, lpObject);
	lpStruct->x = (*env)->GetIntField(env, lpObject, TRIVERTEXFc.x);
	lpStruct->y = (*env)->GetIntField(env, lpObject, TRIVERTEXFc.y);
	lpStruct->Red = (*env)->GetShortField(env, lpObject, TRIVERTEXFc.Red);
	lpStruct->Green = (*env)->GetShortField(env, lpObject, TRIVERTEXFc.Green);
	lpStruct->Blue = (*env)->GetShortField(env, lpObject, TRIVERTEXFc.Blue);
	lpStruct->Alpha = (*env)->GetShortField(env, lpObject, TRIVERTEXFc.Alpha);
	return lpStruct;
}

void setTRIVERTEXFields(JNIEnv *env, jobject lpObject, TRIVERTEX *lpStruct)
{
	if (!TRIVERTEXFc.cached) cacheTRIVERTEXFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, TRIVERTEXFc.x, (jint)lpStruct->x);
	(*env)->SetIntField(env, lpObject, TRIVERTEXFc.y, (jint)lpStruct->y);
	(*env)->SetShortField(env, lpObject, TRIVERTEXFc.Red, (jshort)lpStruct->Red);
	(*env)->SetShortField(env, lpObject, TRIVERTEXFc.Green, (jshort)lpStruct->Green);
	(*env)->SetShortField(env, lpObject, TRIVERTEXFc.Blue, (jshort)lpStruct->Blue);
	(*env)->SetShortField(env, lpObject, TRIVERTEXFc.Alpha, (jshort)lpStruct->Alpha);
}
#endif

#ifndef NO_TVHITTESTINFO
typedef struct TVHITTESTINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID x, y, flags, hItem;
} TVHITTESTINFO_FID_CACHE;

TVHITTESTINFO_FID_CACHE TVHITTESTINFOFc;

void cacheTVHITTESTINFOFields(JNIEnv *env, jobject lpObject)
{
	if (TVHITTESTINFOFc.cached) return;
	TVHITTESTINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	TVHITTESTINFOFc.x = (*env)->GetFieldID(env, TVHITTESTINFOFc.clazz, "x", "I");
	TVHITTESTINFOFc.y = (*env)->GetFieldID(env, TVHITTESTINFOFc.clazz, "y", "I");
	TVHITTESTINFOFc.flags = (*env)->GetFieldID(env, TVHITTESTINFOFc.clazz, "flags", "I");
	TVHITTESTINFOFc.hItem = (*env)->GetFieldID(env, TVHITTESTINFOFc.clazz, "hItem", "I");
	TVHITTESTINFOFc.cached = 1;
}

TVHITTESTINFO *getTVHITTESTINFOFields(JNIEnv *env, jobject lpObject, TVHITTESTINFO *lpStruct)
{
	if (!TVHITTESTINFOFc.cached) cacheTVHITTESTINFOFields(env, lpObject);
	lpStruct->pt.x = (*env)->GetIntField(env, lpObject, TVHITTESTINFOFc.x);
	lpStruct->pt.y = (*env)->GetIntField(env, lpObject, TVHITTESTINFOFc.y);
	lpStruct->flags = (*env)->GetIntField(env, lpObject, TVHITTESTINFOFc.flags);
	lpStruct->hItem = (HTREEITEM)(*env)->GetIntField(env, lpObject, TVHITTESTINFOFc.hItem);
	return lpStruct;
}

void setTVHITTESTINFOFields(JNIEnv *env, jobject lpObject, TVHITTESTINFO *lpStruct)
{
	if (!TVHITTESTINFOFc.cached) cacheTVHITTESTINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, TVHITTESTINFOFc.x, (jint)lpStruct->pt.x);
	(*env)->SetIntField(env, lpObject, TVHITTESTINFOFc.y, (jint)lpStruct->pt.y);
	(*env)->SetIntField(env, lpObject, TVHITTESTINFOFc.flags, (jint)lpStruct->flags);
	(*env)->SetIntField(env, lpObject, TVHITTESTINFOFc.hItem, (jint)lpStruct->hItem);
}
#endif

#ifndef NO_TVINSERTSTRUCT
typedef struct TVINSERTSTRUCT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID hParent, hInsertAfter, mask, hItem, state, stateMask, pszText, cchTextMax, iImage, iSelectedImage, cChildren, lParam;
} TVINSERTSTRUCT_FID_CACHE;

TVINSERTSTRUCT_FID_CACHE TVINSERTSTRUCTFc;

void cacheTVINSERTSTRUCTFields(JNIEnv *env, jobject lpObject)
{
	if (TVINSERTSTRUCTFc.cached) return;
	TVINSERTSTRUCTFc.clazz = (*env)->GetObjectClass(env, lpObject);
	TVINSERTSTRUCTFc.hParent = (*env)->GetFieldID(env, TVINSERTSTRUCTFc.clazz, "hParent", "I");
	TVINSERTSTRUCTFc.hInsertAfter = (*env)->GetFieldID(env, TVINSERTSTRUCTFc.clazz, "hInsertAfter", "I");
	TVINSERTSTRUCTFc.mask = (*env)->GetFieldID(env, TVINSERTSTRUCTFc.clazz, "mask", "I");
	TVINSERTSTRUCTFc.hItem = (*env)->GetFieldID(env, TVINSERTSTRUCTFc.clazz, "hItem", "I");
	TVINSERTSTRUCTFc.state = (*env)->GetFieldID(env, TVINSERTSTRUCTFc.clazz, "state", "I");
	TVINSERTSTRUCTFc.stateMask = (*env)->GetFieldID(env, TVINSERTSTRUCTFc.clazz, "stateMask", "I");
	TVINSERTSTRUCTFc.pszText = (*env)->GetFieldID(env, TVINSERTSTRUCTFc.clazz, "pszText", "I");
	TVINSERTSTRUCTFc.cchTextMax = (*env)->GetFieldID(env, TVINSERTSTRUCTFc.clazz, "cchTextMax", "I");
	TVINSERTSTRUCTFc.iImage = (*env)->GetFieldID(env, TVINSERTSTRUCTFc.clazz, "iImage", "I");
	TVINSERTSTRUCTFc.iSelectedImage = (*env)->GetFieldID(env, TVINSERTSTRUCTFc.clazz, "iSelectedImage", "I");
	TVINSERTSTRUCTFc.cChildren = (*env)->GetFieldID(env, TVINSERTSTRUCTFc.clazz, "cChildren", "I");
	TVINSERTSTRUCTFc.lParam = (*env)->GetFieldID(env, TVINSERTSTRUCTFc.clazz, "lParam", "I");
	TVINSERTSTRUCTFc.cached = 1;
}

TVINSERTSTRUCT *getTVINSERTSTRUCTFields(JNIEnv *env, jobject lpObject, TVINSERTSTRUCT *lpStruct)
{
	if (!TVINSERTSTRUCTFc.cached) cacheTVINSERTSTRUCTFields(env, lpObject);
	lpStruct->hParent = (HTREEITEM)(*env)->GetIntField(env, lpObject, TVINSERTSTRUCTFc.hParent);
	lpStruct->hInsertAfter = (HTREEITEM)(*env)->GetIntField(env, lpObject, TVINSERTSTRUCTFc.hInsertAfter);
	lpStruct->item.mask = (*env)->GetIntField(env, lpObject, TVINSERTSTRUCTFc.mask);
	lpStruct->item.hItem = (HTREEITEM)(*env)->GetIntField(env, lpObject, TVINSERTSTRUCTFc.hItem);
	lpStruct->item.state = (*env)->GetIntField(env, lpObject, TVINSERTSTRUCTFc.state);
	lpStruct->item.stateMask = (*env)->GetIntField(env, lpObject, TVINSERTSTRUCTFc.stateMask);
	lpStruct->item.pszText = (LPTSTR)(*env)->GetIntField(env, lpObject, TVINSERTSTRUCTFc.pszText);
	lpStruct->item.cchTextMax = (*env)->GetIntField(env, lpObject, TVINSERTSTRUCTFc.cchTextMax);
	lpStruct->item.iImage = (*env)->GetIntField(env, lpObject, TVINSERTSTRUCTFc.iImage);
	lpStruct->item.iSelectedImage = (*env)->GetIntField(env, lpObject, TVINSERTSTRUCTFc.iSelectedImage);
	lpStruct->item.cChildren = (*env)->GetIntField(env, lpObject, TVINSERTSTRUCTFc.cChildren);
	lpStruct->item.lParam = (*env)->GetIntField(env, lpObject, TVINSERTSTRUCTFc.lParam);
	return lpStruct;
}

void setTVINSERTSTRUCTFields(JNIEnv *env, jobject lpObject, TVINSERTSTRUCT *lpStruct)
{
	if (!TVINSERTSTRUCTFc.cached) cacheTVINSERTSTRUCTFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, TVINSERTSTRUCTFc.hParent, (jint)lpStruct->hParent);
	(*env)->SetIntField(env, lpObject, TVINSERTSTRUCTFc.hInsertAfter, (jint)lpStruct->hInsertAfter);
	(*env)->SetIntField(env, lpObject, TVINSERTSTRUCTFc.mask, (jint)lpStruct->item.mask);
	(*env)->SetIntField(env, lpObject, TVINSERTSTRUCTFc.hItem, (jint)lpStruct->item.hItem);
	(*env)->SetIntField(env, lpObject, TVINSERTSTRUCTFc.state, (jint)lpStruct->item.state);
	(*env)->SetIntField(env, lpObject, TVINSERTSTRUCTFc.stateMask, (jint)lpStruct->item.stateMask);
	(*env)->SetIntField(env, lpObject, TVINSERTSTRUCTFc.pszText, (jint)lpStruct->item.pszText);
	(*env)->SetIntField(env, lpObject, TVINSERTSTRUCTFc.cchTextMax, (jint)lpStruct->item.cchTextMax);
	(*env)->SetIntField(env, lpObject, TVINSERTSTRUCTFc.iImage, (jint)lpStruct->item.iImage);
	(*env)->SetIntField(env, lpObject, TVINSERTSTRUCTFc.iSelectedImage, (jint)lpStruct->item.iSelectedImage);
	(*env)->SetIntField(env, lpObject, TVINSERTSTRUCTFc.cChildren, (jint)lpStruct->item.cChildren);
	(*env)->SetIntField(env, lpObject, TVINSERTSTRUCTFc.lParam, (jint)lpStruct->item.lParam);
}
#endif

#ifndef NO_TVITEM
typedef struct TVITEM_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID mask, hItem, state, stateMask, pszText, cchTextMax, iImage, iSelectedImage, cChildren, lParam;
} TVITEM_FID_CACHE;

TVITEM_FID_CACHE TVITEMFc;

void cacheTVITEMFields(JNIEnv *env, jobject lpObject)
{
	if (TVITEMFc.cached) return;
	TVITEMFc.clazz = (*env)->GetObjectClass(env, lpObject);
	TVITEMFc.mask = (*env)->GetFieldID(env, TVITEMFc.clazz, "mask", "I");
	TVITEMFc.hItem = (*env)->GetFieldID(env, TVITEMFc.clazz, "hItem", "I");
	TVITEMFc.state = (*env)->GetFieldID(env, TVITEMFc.clazz, "state", "I");
	TVITEMFc.stateMask = (*env)->GetFieldID(env, TVITEMFc.clazz, "stateMask", "I");
	TVITEMFc.pszText = (*env)->GetFieldID(env, TVITEMFc.clazz, "pszText", "I");
	TVITEMFc.cchTextMax = (*env)->GetFieldID(env, TVITEMFc.clazz, "cchTextMax", "I");
	TVITEMFc.iImage = (*env)->GetFieldID(env, TVITEMFc.clazz, "iImage", "I");
	TVITEMFc.iSelectedImage = (*env)->GetFieldID(env, TVITEMFc.clazz, "iSelectedImage", "I");
	TVITEMFc.cChildren = (*env)->GetFieldID(env, TVITEMFc.clazz, "cChildren", "I");
	TVITEMFc.lParam = (*env)->GetFieldID(env, TVITEMFc.clazz, "lParam", "I");
	TVITEMFc.cached = 1;
}

TVITEM *getTVITEMFields(JNIEnv *env, jobject lpObject, TVITEM *lpStruct)
{
	if (!TVITEMFc.cached) cacheTVITEMFields(env, lpObject);
	lpStruct->mask = (*env)->GetIntField(env, lpObject, TVITEMFc.mask);
	lpStruct->hItem = (HTREEITEM)(*env)->GetIntField(env, lpObject, TVITEMFc.hItem);
	lpStruct->state = (*env)->GetIntField(env, lpObject, TVITEMFc.state);
	lpStruct->stateMask = (*env)->GetIntField(env, lpObject, TVITEMFc.stateMask);
	lpStruct->pszText = (LPTSTR)(*env)->GetIntField(env, lpObject, TVITEMFc.pszText);
	lpStruct->cchTextMax = (*env)->GetIntField(env, lpObject, TVITEMFc.cchTextMax);
	lpStruct->iImage = (*env)->GetIntField(env, lpObject, TVITEMFc.iImage);
	lpStruct->iSelectedImage = (*env)->GetIntField(env, lpObject, TVITEMFc.iSelectedImage);
	lpStruct->cChildren = (*env)->GetIntField(env, lpObject, TVITEMFc.cChildren);
	lpStruct->lParam = (*env)->GetIntField(env, lpObject, TVITEMFc.lParam);
	return lpStruct;
}

void setTVITEMFields(JNIEnv *env, jobject lpObject, TVITEM *lpStruct)
{
	if (!TVITEMFc.cached) cacheTVITEMFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, TVITEMFc.mask, (jint)lpStruct->mask);
	(*env)->SetIntField(env, lpObject, TVITEMFc.hItem, (jint)lpStruct->hItem);
	(*env)->SetIntField(env, lpObject, TVITEMFc.state, (jint)lpStruct->state);
	(*env)->SetIntField(env, lpObject, TVITEMFc.stateMask, (jint)lpStruct->stateMask);
	(*env)->SetIntField(env, lpObject, TVITEMFc.pszText, (jint)lpStruct->pszText);
	(*env)->SetIntField(env, lpObject, TVITEMFc.cchTextMax, (jint)lpStruct->cchTextMax);
	(*env)->SetIntField(env, lpObject, TVITEMFc.iImage, (jint)lpStruct->iImage);
	(*env)->SetIntField(env, lpObject, TVITEMFc.iSelectedImage, (jint)lpStruct->iSelectedImage);
	(*env)->SetIntField(env, lpObject, TVITEMFc.cChildren, (jint)lpStruct->cChildren);
	(*env)->SetIntField(env, lpObject, TVITEMFc.lParam, (jint)lpStruct->lParam);
}
#endif

#ifndef NO_UDACCEL
typedef struct UDACCEL_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID nSec, nInc;
} UDACCEL_FID_CACHE;

UDACCEL_FID_CACHE UDACCELFc;

void cacheUDACCELFields(JNIEnv *env, jobject lpObject)
{
	if (UDACCELFc.cached) return;
	UDACCELFc.clazz = (*env)->GetObjectClass(env, lpObject);
	UDACCELFc.nSec = (*env)->GetFieldID(env, UDACCELFc.clazz, "nSec", "I");
	UDACCELFc.nInc = (*env)->GetFieldID(env, UDACCELFc.clazz, "nInc", "I");
	UDACCELFc.cached = 1;
}

UDACCEL *getUDACCELFields(JNIEnv *env, jobject lpObject, UDACCEL *lpStruct)
{
	if (!UDACCELFc.cached) cacheUDACCELFields(env, lpObject);
	lpStruct->nSec = (*env)->GetIntField(env, lpObject, UDACCELFc.nSec);
	lpStruct->nInc = (*env)->GetIntField(env, lpObject, UDACCELFc.nInc);
	return lpStruct;
}

void setUDACCELFields(JNIEnv *env, jobject lpObject, UDACCEL *lpStruct)
{
	if (!UDACCELFc.cached) cacheUDACCELFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, UDACCELFc.nSec, (jint)lpStruct->nSec);
	(*env)->SetIntField(env, lpObject, UDACCELFc.nInc, (jint)lpStruct->nInc);
}
#endif

#ifndef NO_WINDOWPLACEMENT
typedef struct WINDOWPLACEMENT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID length, flags, showCmd, ptMinPosition_x, ptMinPosition_y, ptMaxPosition_x, ptMaxPosition_y, left, top, right, bottom;
} WINDOWPLACEMENT_FID_CACHE;

WINDOWPLACEMENT_FID_CACHE WINDOWPLACEMENTFc;

void cacheWINDOWPLACEMENTFields(JNIEnv *env, jobject lpObject)
{
	if (WINDOWPLACEMENTFc.cached) return;
	WINDOWPLACEMENTFc.clazz = (*env)->GetObjectClass(env, lpObject);
	WINDOWPLACEMENTFc.length = (*env)->GetFieldID(env, WINDOWPLACEMENTFc.clazz, "length", "I");
	WINDOWPLACEMENTFc.flags = (*env)->GetFieldID(env, WINDOWPLACEMENTFc.clazz, "flags", "I");
	WINDOWPLACEMENTFc.showCmd = (*env)->GetFieldID(env, WINDOWPLACEMENTFc.clazz, "showCmd", "I");
	WINDOWPLACEMENTFc.ptMinPosition_x = (*env)->GetFieldID(env, WINDOWPLACEMENTFc.clazz, "ptMinPosition_x", "I");
	WINDOWPLACEMENTFc.ptMinPosition_y = (*env)->GetFieldID(env, WINDOWPLACEMENTFc.clazz, "ptMinPosition_y", "I");
	WINDOWPLACEMENTFc.ptMaxPosition_x = (*env)->GetFieldID(env, WINDOWPLACEMENTFc.clazz, "ptMaxPosition_x", "I");
	WINDOWPLACEMENTFc.ptMaxPosition_y = (*env)->GetFieldID(env, WINDOWPLACEMENTFc.clazz, "ptMaxPosition_y", "I");
	WINDOWPLACEMENTFc.left = (*env)->GetFieldID(env, WINDOWPLACEMENTFc.clazz, "left", "I");
	WINDOWPLACEMENTFc.top = (*env)->GetFieldID(env, WINDOWPLACEMENTFc.clazz, "top", "I");
	WINDOWPLACEMENTFc.right = (*env)->GetFieldID(env, WINDOWPLACEMENTFc.clazz, "right", "I");
	WINDOWPLACEMENTFc.bottom = (*env)->GetFieldID(env, WINDOWPLACEMENTFc.clazz, "bottom", "I");
	WINDOWPLACEMENTFc.cached = 1;
}

WINDOWPLACEMENT *getWINDOWPLACEMENTFields(JNIEnv *env, jobject lpObject, WINDOWPLACEMENT *lpStruct)
{
	if (!WINDOWPLACEMENTFc.cached) cacheWINDOWPLACEMENTFields(env, lpObject);
	lpStruct->length = (*env)->GetIntField(env, lpObject, WINDOWPLACEMENTFc.length);
	lpStruct->flags = (*env)->GetIntField(env, lpObject, WINDOWPLACEMENTFc.flags);
	lpStruct->showCmd = (*env)->GetIntField(env, lpObject, WINDOWPLACEMENTFc.showCmd);
	lpStruct->ptMinPosition.x = (*env)->GetIntField(env, lpObject, WINDOWPLACEMENTFc.ptMinPosition_x);
	lpStruct->ptMinPosition.y = (*env)->GetIntField(env, lpObject, WINDOWPLACEMENTFc.ptMinPosition_y);
	lpStruct->ptMaxPosition.x = (*env)->GetIntField(env, lpObject, WINDOWPLACEMENTFc.ptMaxPosition_x);
	lpStruct->ptMaxPosition.y = (*env)->GetIntField(env, lpObject, WINDOWPLACEMENTFc.ptMaxPosition_y);
	lpStruct->rcNormalPosition.left = (*env)->GetIntField(env, lpObject, WINDOWPLACEMENTFc.left);
	lpStruct->rcNormalPosition.top = (*env)->GetIntField(env, lpObject, WINDOWPLACEMENTFc.top);
	lpStruct->rcNormalPosition.right = (*env)->GetIntField(env, lpObject, WINDOWPLACEMENTFc.right);
	lpStruct->rcNormalPosition.bottom = (*env)->GetIntField(env, lpObject, WINDOWPLACEMENTFc.bottom);
	return lpStruct;
}

void setWINDOWPLACEMENTFields(JNIEnv *env, jobject lpObject, WINDOWPLACEMENT *lpStruct)
{
	if (!WINDOWPLACEMENTFc.cached) cacheWINDOWPLACEMENTFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, WINDOWPLACEMENTFc.length, (jint)lpStruct->length);
	(*env)->SetIntField(env, lpObject, WINDOWPLACEMENTFc.flags, (jint)lpStruct->flags);
	(*env)->SetIntField(env, lpObject, WINDOWPLACEMENTFc.showCmd, (jint)lpStruct->showCmd);
	(*env)->SetIntField(env, lpObject, WINDOWPLACEMENTFc.ptMinPosition_x, (jint)lpStruct->ptMinPosition.x);
	(*env)->SetIntField(env, lpObject, WINDOWPLACEMENTFc.ptMinPosition_y, (jint)lpStruct->ptMinPosition.y);
	(*env)->SetIntField(env, lpObject, WINDOWPLACEMENTFc.ptMaxPosition_x, (jint)lpStruct->ptMaxPosition.x);
	(*env)->SetIntField(env, lpObject, WINDOWPLACEMENTFc.ptMaxPosition_y, (jint)lpStruct->ptMaxPosition.y);
	(*env)->SetIntField(env, lpObject, WINDOWPLACEMENTFc.left, (jint)lpStruct->rcNormalPosition.left);
	(*env)->SetIntField(env, lpObject, WINDOWPLACEMENTFc.top, (jint)lpStruct->rcNormalPosition.top);
	(*env)->SetIntField(env, lpObject, WINDOWPLACEMENTFc.right, (jint)lpStruct->rcNormalPosition.right);
	(*env)->SetIntField(env, lpObject, WINDOWPLACEMENTFc.bottom, (jint)lpStruct->rcNormalPosition.bottom);
}
#endif

#ifndef NO_WINDOWPOS
typedef struct WINDOWPOS_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID hwnd, hwndInsertAfter, x, y, cx, cy, flags;
} WINDOWPOS_FID_CACHE;

WINDOWPOS_FID_CACHE WINDOWPOSFc;

void cacheWINDOWPOSFields(JNIEnv *env, jobject lpObject)
{
	if (WINDOWPOSFc.cached) return;
	WINDOWPOSFc.clazz = (*env)->GetObjectClass(env, lpObject);
	WINDOWPOSFc.hwnd = (*env)->GetFieldID(env, WINDOWPOSFc.clazz, "hwnd", "I");
	WINDOWPOSFc.hwndInsertAfter = (*env)->GetFieldID(env, WINDOWPOSFc.clazz, "hwndInsertAfter", "I");
	WINDOWPOSFc.x = (*env)->GetFieldID(env, WINDOWPOSFc.clazz, "x", "I");
	WINDOWPOSFc.y = (*env)->GetFieldID(env, WINDOWPOSFc.clazz, "y", "I");
	WINDOWPOSFc.cx = (*env)->GetFieldID(env, WINDOWPOSFc.clazz, "cx", "I");
	WINDOWPOSFc.cy = (*env)->GetFieldID(env, WINDOWPOSFc.clazz, "cy", "I");
	WINDOWPOSFc.flags = (*env)->GetFieldID(env, WINDOWPOSFc.clazz, "flags", "I");
	WINDOWPOSFc.cached = 1;
}

WINDOWPOS *getWINDOWPOSFields(JNIEnv *env, jobject lpObject, WINDOWPOS *lpStruct)
{
	if (!WINDOWPOSFc.cached) cacheWINDOWPOSFields(env, lpObject);
	lpStruct->hwnd = (HWND)(*env)->GetIntField(env, lpObject, WINDOWPOSFc.hwnd);
	lpStruct->hwndInsertAfter = (HWND)(*env)->GetIntField(env, lpObject, WINDOWPOSFc.hwndInsertAfter);
	lpStruct->x = (*env)->GetIntField(env, lpObject, WINDOWPOSFc.x);
	lpStruct->y = (*env)->GetIntField(env, lpObject, WINDOWPOSFc.y);
	lpStruct->cx = (*env)->GetIntField(env, lpObject, WINDOWPOSFc.cx);
	lpStruct->cy = (*env)->GetIntField(env, lpObject, WINDOWPOSFc.cy);
	lpStruct->flags = (*env)->GetIntField(env, lpObject, WINDOWPOSFc.flags);
	return lpStruct;
}

void setWINDOWPOSFields(JNIEnv *env, jobject lpObject, WINDOWPOS *lpStruct)
{
	if (!WINDOWPOSFc.cached) cacheWINDOWPOSFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, WINDOWPOSFc.hwnd, (jint)lpStruct->hwnd);
	(*env)->SetIntField(env, lpObject, WINDOWPOSFc.hwndInsertAfter, (jint)lpStruct->hwndInsertAfter);
	(*env)->SetIntField(env, lpObject, WINDOWPOSFc.x, (jint)lpStruct->x);
	(*env)->SetIntField(env, lpObject, WINDOWPOSFc.y, (jint)lpStruct->y);
	(*env)->SetIntField(env, lpObject, WINDOWPOSFc.cx, (jint)lpStruct->cx);
	(*env)->SetIntField(env, lpObject, WINDOWPOSFc.cy, (jint)lpStruct->cy);
	(*env)->SetIntField(env, lpObject, WINDOWPOSFc.flags, (jint)lpStruct->flags);
}
#endif

#ifndef NO_WNDCLASS
typedef struct WNDCLASS_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID style, lpfnWndProc, cbClsExtra, cbWndExtra, hInstance, hIcon, hCursor, hbrBackground, lpszMenuName, lpszClassName;
} WNDCLASS_FID_CACHE;

WNDCLASS_FID_CACHE WNDCLASSFc;

void cacheWNDCLASSFields(JNIEnv *env, jobject lpObject)
{
	if (WNDCLASSFc.cached) return;
	WNDCLASSFc.clazz = (*env)->GetObjectClass(env, lpObject);
	WNDCLASSFc.style = (*env)->GetFieldID(env, WNDCLASSFc.clazz, "style", "I");
	WNDCLASSFc.lpfnWndProc = (*env)->GetFieldID(env, WNDCLASSFc.clazz, "lpfnWndProc", "I");
	WNDCLASSFc.cbClsExtra = (*env)->GetFieldID(env, WNDCLASSFc.clazz, "cbClsExtra", "I");
	WNDCLASSFc.cbWndExtra = (*env)->GetFieldID(env, WNDCLASSFc.clazz, "cbWndExtra", "I");
	WNDCLASSFc.hInstance = (*env)->GetFieldID(env, WNDCLASSFc.clazz, "hInstance", "I");
	WNDCLASSFc.hIcon = (*env)->GetFieldID(env, WNDCLASSFc.clazz, "hIcon", "I");
	WNDCLASSFc.hCursor = (*env)->GetFieldID(env, WNDCLASSFc.clazz, "hCursor", "I");
	WNDCLASSFc.hbrBackground = (*env)->GetFieldID(env, WNDCLASSFc.clazz, "hbrBackground", "I");
	WNDCLASSFc.lpszMenuName = (*env)->GetFieldID(env, WNDCLASSFc.clazz, "lpszMenuName", "I");
	WNDCLASSFc.lpszClassName = (*env)->GetFieldID(env, WNDCLASSFc.clazz, "lpszClassName", "I");
	WNDCLASSFc.cached = 1;
}

WNDCLASS *getWNDCLASSFields(JNIEnv *env, jobject lpObject, WNDCLASS *lpStruct)
{
	if (!WNDCLASSFc.cached) cacheWNDCLASSFields(env, lpObject);
	lpStruct->style = (*env)->GetIntField(env, lpObject, WNDCLASSFc.style);
	lpStruct->lpfnWndProc = (WNDPROC)(*env)->GetIntField(env, lpObject, WNDCLASSFc.lpfnWndProc);
	lpStruct->cbClsExtra = (*env)->GetIntField(env, lpObject, WNDCLASSFc.cbClsExtra);
	lpStruct->cbWndExtra = (*env)->GetIntField(env, lpObject, WNDCLASSFc.cbWndExtra);
	lpStruct->hInstance = (HINSTANCE)(*env)->GetIntField(env, lpObject, WNDCLASSFc.hInstance);
	lpStruct->hIcon = (HICON)(*env)->GetIntField(env, lpObject, WNDCLASSFc.hIcon);
	lpStruct->hCursor = (HCURSOR)(*env)->GetIntField(env, lpObject, WNDCLASSFc.hCursor);
	lpStruct->hbrBackground = (HBRUSH)(*env)->GetIntField(env, lpObject, WNDCLASSFc.hbrBackground);
	lpStruct->lpszMenuName = (LPCTSTR)(*env)->GetIntField(env, lpObject, WNDCLASSFc.lpszMenuName);
	lpStruct->lpszClassName = (LPCTSTR)(*env)->GetIntField(env, lpObject, WNDCLASSFc.lpszClassName);
	return lpStruct;
}

void setWNDCLASSFields(JNIEnv *env, jobject lpObject, WNDCLASS *lpStruct)
{
	if (!WNDCLASSFc.cached) cacheWNDCLASSFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, WNDCLASSFc.style, (jint)lpStruct->style);
	(*env)->SetIntField(env, lpObject, WNDCLASSFc.lpfnWndProc, (jint)lpStruct->lpfnWndProc);
	(*env)->SetIntField(env, lpObject, WNDCLASSFc.cbClsExtra, (jint)lpStruct->cbClsExtra);
	(*env)->SetIntField(env, lpObject, WNDCLASSFc.cbWndExtra, (jint)lpStruct->cbWndExtra);
	(*env)->SetIntField(env, lpObject, WNDCLASSFc.hInstance, (jint)lpStruct->hInstance);
	(*env)->SetIntField(env, lpObject, WNDCLASSFc.hIcon, (jint)lpStruct->hIcon);
	(*env)->SetIntField(env, lpObject, WNDCLASSFc.hCursor, (jint)lpStruct->hCursor);
	(*env)->SetIntField(env, lpObject, WNDCLASSFc.hbrBackground, (jint)lpStruct->hbrBackground);
	(*env)->SetIntField(env, lpObject, WNDCLASSFc.lpszMenuName, (jint)lpStruct->lpszMenuName);
	(*env)->SetIntField(env, lpObject, WNDCLASSFc.lpszClassName, (jint)lpStruct->lpszClassName);
}
#endif

