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
#include "structs.h"

#ifndef NO_ACCEL
typedef struct ACCEL_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cmd, key, fVirt;
} ACCEL_FID_CACHE;

ACCEL_FID_CACHE ACCELFc;

void cacheACCELFields(JNIEnv *env, jobject lpObject)
{
	if (ACCELFc.cached) return;
	ACCELFc.clazz = (*env)->GetObjectClass(env, lpObject);
	ACCELFc.cmd = (*env)->GetFieldID(env, ACCELFc.clazz, "cmd", "S");
	ACCELFc.key = (*env)->GetFieldID(env, ACCELFc.clazz, "key", "S");
	ACCELFc.fVirt = (*env)->GetFieldID(env, ACCELFc.clazz, "fVirt", "B");
	ACCELFc.cached = 1;
}

ACCEL *getACCELFields(JNIEnv *env, jobject lpObject, ACCEL *lpStruct)
{
	if (!ACCELFc.cached) cacheACCELFields(env, lpObject);
	lpStruct->cmd = (*env)->GetShortField(env, lpObject, ACCELFc.cmd);
	lpStruct->key = (*env)->GetShortField(env, lpObject, ACCELFc.key);
	lpStruct->fVirt = (*env)->GetByteField(env, lpObject, ACCELFc.fVirt);
	return lpStruct;
}

void setACCELFields(JNIEnv *env, jobject lpObject, ACCEL *lpStruct)
{
	if (!ACCELFc.cached) cacheACCELFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, ACCELFc.cmd, (jshort)lpStruct->cmd);
	(*env)->SetShortField(env, lpObject, ACCELFc.key, (jshort)lpStruct->key);
	(*env)->SetByteField(env, lpObject, ACCELFc.fVirt, (jbyte)lpStruct->fVirt);
}
#endif

#ifndef NO_BITMAP
typedef struct BITMAP_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID bmBits, bmBitsPixel, bmPlanes, bmWidthBytes, bmHeight, bmWidth, bmType;
} BITMAP_FID_CACHE;

BITMAP_FID_CACHE BITMAPFc;

void cacheBITMAPFields(JNIEnv *env, jobject lpObject)
{
	if (BITMAPFc.cached) return;
	BITMAPFc.clazz = (*env)->GetObjectClass(env, lpObject);
	BITMAPFc.bmBits = (*env)->GetFieldID(env, BITMAPFc.clazz, "bmBits", "I");
	BITMAPFc.bmBitsPixel = (*env)->GetFieldID(env, BITMAPFc.clazz, "bmBitsPixel", "S");
	BITMAPFc.bmPlanes = (*env)->GetFieldID(env, BITMAPFc.clazz, "bmPlanes", "S");
	BITMAPFc.bmWidthBytes = (*env)->GetFieldID(env, BITMAPFc.clazz, "bmWidthBytes", "I");
	BITMAPFc.bmHeight = (*env)->GetFieldID(env, BITMAPFc.clazz, "bmHeight", "I");
	BITMAPFc.bmWidth = (*env)->GetFieldID(env, BITMAPFc.clazz, "bmWidth", "I");
	BITMAPFc.bmType = (*env)->GetFieldID(env, BITMAPFc.clazz, "bmType", "I");
	BITMAPFc.cached = 1;
}

BITMAP *getBITMAPFields(JNIEnv *env, jobject lpObject, BITMAP *lpStruct)
{
	if (!BITMAPFc.cached) cacheBITMAPFields(env, lpObject);
	lpStruct->bmBits = (LPVOID)(*env)->GetIntField(env, lpObject, BITMAPFc.bmBits);
	lpStruct->bmBitsPixel = (*env)->GetShortField(env, lpObject, BITMAPFc.bmBitsPixel);
	lpStruct->bmPlanes = (*env)->GetShortField(env, lpObject, BITMAPFc.bmPlanes);
	lpStruct->bmWidthBytes = (*env)->GetIntField(env, lpObject, BITMAPFc.bmWidthBytes);
	lpStruct->bmHeight = (*env)->GetIntField(env, lpObject, BITMAPFc.bmHeight);
	lpStruct->bmWidth = (*env)->GetIntField(env, lpObject, BITMAPFc.bmWidth);
	lpStruct->bmType = (*env)->GetIntField(env, lpObject, BITMAPFc.bmType);
	return lpStruct;
}

void setBITMAPFields(JNIEnv *env, jobject lpObject, BITMAP *lpStruct)
{
	if (!BITMAPFc.cached) cacheBITMAPFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, BITMAPFc.bmBits, (jint)lpStruct->bmBits);
	(*env)->SetShortField(env, lpObject, BITMAPFc.bmBitsPixel, (jshort)lpStruct->bmBitsPixel);
	(*env)->SetShortField(env, lpObject, BITMAPFc.bmPlanes, (jshort)lpStruct->bmPlanes);
	(*env)->SetIntField(env, lpObject, BITMAPFc.bmWidthBytes, (jint)lpStruct->bmWidthBytes);
	(*env)->SetIntField(env, lpObject, BITMAPFc.bmHeight, (jint)lpStruct->bmHeight);
	(*env)->SetIntField(env, lpObject, BITMAPFc.bmWidth, (jint)lpStruct->bmWidth);
	(*env)->SetIntField(env, lpObject, BITMAPFc.bmType, (jint)lpStruct->bmType);
}
#endif

#ifndef NO_BITMAPINFOHEADER
typedef struct BITMAPINFOHEADER_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID biClrImportant, biClrUsed, biYPelsPerMeter, biXPelsPerMeter, biSizeImage, biCompression, biBitCount, biPlanes, biHeight, biWidth, biSize;
} BITMAPINFOHEADER_FID_CACHE;

BITMAPINFOHEADER_FID_CACHE BITMAPINFOHEADERFc;

void cacheBITMAPINFOHEADERFields(JNIEnv *env, jobject lpObject)
{
	if (BITMAPINFOHEADERFc.cached) return;
	BITMAPINFOHEADERFc.clazz = (*env)->GetObjectClass(env, lpObject);
	BITMAPINFOHEADERFc.biClrImportant = (*env)->GetFieldID(env, BITMAPINFOHEADERFc.clazz, "biClrImportant", "I");
	BITMAPINFOHEADERFc.biClrUsed = (*env)->GetFieldID(env, BITMAPINFOHEADERFc.clazz, "biClrUsed", "I");
	BITMAPINFOHEADERFc.biYPelsPerMeter = (*env)->GetFieldID(env, BITMAPINFOHEADERFc.clazz, "biYPelsPerMeter", "I");
	BITMAPINFOHEADERFc.biXPelsPerMeter = (*env)->GetFieldID(env, BITMAPINFOHEADERFc.clazz, "biXPelsPerMeter", "I");
	BITMAPINFOHEADERFc.biSizeImage = (*env)->GetFieldID(env, BITMAPINFOHEADERFc.clazz, "biSizeImage", "I");
	BITMAPINFOHEADERFc.biCompression = (*env)->GetFieldID(env, BITMAPINFOHEADERFc.clazz, "biCompression", "I");
	BITMAPINFOHEADERFc.biBitCount = (*env)->GetFieldID(env, BITMAPINFOHEADERFc.clazz, "biBitCount", "S");
	BITMAPINFOHEADERFc.biPlanes = (*env)->GetFieldID(env, BITMAPINFOHEADERFc.clazz, "biPlanes", "S");
	BITMAPINFOHEADERFc.biHeight = (*env)->GetFieldID(env, BITMAPINFOHEADERFc.clazz, "biHeight", "I");
	BITMAPINFOHEADERFc.biWidth = (*env)->GetFieldID(env, BITMAPINFOHEADERFc.clazz, "biWidth", "I");
	BITMAPINFOHEADERFc.biSize = (*env)->GetFieldID(env, BITMAPINFOHEADERFc.clazz, "biSize", "I");
	BITMAPINFOHEADERFc.cached = 1;
}

BITMAPINFOHEADER *getBITMAPINFOHEADERFields(JNIEnv *env, jobject lpObject, BITMAPINFOHEADER *lpStruct)
{
	if (!BITMAPINFOHEADERFc.cached) cacheBITMAPINFOHEADERFields(env, lpObject);
	lpStruct->biClrImportant = (*env)->GetIntField(env, lpObject, BITMAPINFOHEADERFc.biClrImportant);
	lpStruct->biClrUsed = (*env)->GetIntField(env, lpObject, BITMAPINFOHEADERFc.biClrUsed);
	lpStruct->biYPelsPerMeter = (*env)->GetIntField(env, lpObject, BITMAPINFOHEADERFc.biYPelsPerMeter);
	lpStruct->biXPelsPerMeter = (*env)->GetIntField(env, lpObject, BITMAPINFOHEADERFc.biXPelsPerMeter);
	lpStruct->biSizeImage = (*env)->GetIntField(env, lpObject, BITMAPINFOHEADERFc.biSizeImage);
	lpStruct->biCompression = (*env)->GetIntField(env, lpObject, BITMAPINFOHEADERFc.biCompression);
	lpStruct->biBitCount = (*env)->GetShortField(env, lpObject, BITMAPINFOHEADERFc.biBitCount);
	lpStruct->biPlanes = (*env)->GetShortField(env, lpObject, BITMAPINFOHEADERFc.biPlanes);
	lpStruct->biHeight = (*env)->GetIntField(env, lpObject, BITMAPINFOHEADERFc.biHeight);
	lpStruct->biWidth = (*env)->GetIntField(env, lpObject, BITMAPINFOHEADERFc.biWidth);
	lpStruct->biSize = (*env)->GetIntField(env, lpObject, BITMAPINFOHEADERFc.biSize);
	return lpStruct;
}

void setBITMAPINFOHEADERFields(JNIEnv *env, jobject lpObject, BITMAPINFOHEADER *lpStruct)
{
	if (!BITMAPINFOHEADERFc.cached) cacheBITMAPINFOHEADERFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, BITMAPINFOHEADERFc.biClrImportant, (jint)lpStruct->biClrImportant);
	(*env)->SetIntField(env, lpObject, BITMAPINFOHEADERFc.biClrUsed, (jint)lpStruct->biClrUsed);
	(*env)->SetIntField(env, lpObject, BITMAPINFOHEADERFc.biYPelsPerMeter, (jint)lpStruct->biYPelsPerMeter);
	(*env)->SetIntField(env, lpObject, BITMAPINFOHEADERFc.biXPelsPerMeter, (jint)lpStruct->biXPelsPerMeter);
	(*env)->SetIntField(env, lpObject, BITMAPINFOHEADERFc.biSizeImage, (jint)lpStruct->biSizeImage);
	(*env)->SetIntField(env, lpObject, BITMAPINFOHEADERFc.biCompression, (jint)lpStruct->biCompression);
	(*env)->SetShortField(env, lpObject, BITMAPINFOHEADERFc.biBitCount, (jshort)lpStruct->biBitCount);
	(*env)->SetShortField(env, lpObject, BITMAPINFOHEADERFc.biPlanes, (jshort)lpStruct->biPlanes);
	(*env)->SetIntField(env, lpObject, BITMAPINFOHEADERFc.biHeight, (jint)lpStruct->biHeight);
	(*env)->SetIntField(env, lpObject, BITMAPINFOHEADERFc.biWidth, (jint)lpStruct->biWidth);
	(*env)->SetIntField(env, lpObject, BITMAPINFOHEADERFc.biSize, (jint)lpStruct->biSize);
}
#endif

#ifndef NO_BROWSEINFO
typedef struct BROWSEINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID iImage, lParam, lpfn, ulFlags, lpszTitle, pszDisplayName, pidlRoot, hwndOwner;
} BROWSEINFO_FID_CACHE;

BROWSEINFO_FID_CACHE BROWSEINFOFc;

void cacheBROWSEINFOFields(JNIEnv *env, jobject lpObject)
{
	if (BROWSEINFOFc.cached) return;
	BROWSEINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	BROWSEINFOFc.iImage = (*env)->GetFieldID(env, BROWSEINFOFc.clazz, "iImage", "I");
	BROWSEINFOFc.lParam = (*env)->GetFieldID(env, BROWSEINFOFc.clazz, "lParam", "I");
	BROWSEINFOFc.lpfn = (*env)->GetFieldID(env, BROWSEINFOFc.clazz, "lpfn", "I");
	BROWSEINFOFc.ulFlags = (*env)->GetFieldID(env, BROWSEINFOFc.clazz, "ulFlags", "I");
	BROWSEINFOFc.lpszTitle = (*env)->GetFieldID(env, BROWSEINFOFc.clazz, "lpszTitle", "I");
	BROWSEINFOFc.pszDisplayName = (*env)->GetFieldID(env, BROWSEINFOFc.clazz, "pszDisplayName", "I");
	BROWSEINFOFc.pidlRoot = (*env)->GetFieldID(env, BROWSEINFOFc.clazz, "pidlRoot", "I");
	BROWSEINFOFc.hwndOwner = (*env)->GetFieldID(env, BROWSEINFOFc.clazz, "hwndOwner", "I");
	BROWSEINFOFc.cached = 1;
}

BROWSEINFO *getBROWSEINFOFields(JNIEnv *env, jobject lpObject, BROWSEINFO *lpStruct)
{
	if (!BROWSEINFOFc.cached) cacheBROWSEINFOFields(env, lpObject);
	lpStruct->iImage = (*env)->GetIntField(env, lpObject, BROWSEINFOFc.iImage);
	lpStruct->lParam = (*env)->GetIntField(env, lpObject, BROWSEINFOFc.lParam);
	lpStruct->lpfn = (BFFCALLBACK)(*env)->GetIntField(env, lpObject, BROWSEINFOFc.lpfn);
	lpStruct->ulFlags = (*env)->GetIntField(env, lpObject, BROWSEINFOFc.ulFlags);
	lpStruct->lpszTitle = (LPCTSTR)(*env)->GetIntField(env, lpObject, BROWSEINFOFc.lpszTitle);
	lpStruct->pszDisplayName = (LPTSTR)(*env)->GetIntField(env, lpObject, BROWSEINFOFc.pszDisplayName);
	lpStruct->pidlRoot = (LPCITEMIDLIST)(*env)->GetIntField(env, lpObject, BROWSEINFOFc.pidlRoot);
	lpStruct->hwndOwner = (HWND)(*env)->GetIntField(env, lpObject, BROWSEINFOFc.hwndOwner);
	return lpStruct;
}

void setBROWSEINFOFields(JNIEnv *env, jobject lpObject, BROWSEINFO *lpStruct)
{
	if (!BROWSEINFOFc.cached) cacheBROWSEINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, BROWSEINFOFc.iImage, (jint)lpStruct->iImage);
	(*env)->SetIntField(env, lpObject, BROWSEINFOFc.lParam, (jint)lpStruct->lParam);
	(*env)->SetIntField(env, lpObject, BROWSEINFOFc.lpfn, (jint)lpStruct->lpfn);
	(*env)->SetIntField(env, lpObject, BROWSEINFOFc.ulFlags, (jint)lpStruct->ulFlags);
	(*env)->SetIntField(env, lpObject, BROWSEINFOFc.lpszTitle, (jint)lpStruct->lpszTitle);
	(*env)->SetIntField(env, lpObject, BROWSEINFOFc.pszDisplayName, (jint)lpStruct->pszDisplayName);
	(*env)->SetIntField(env, lpObject, BROWSEINFOFc.pidlRoot, (jint)lpStruct->pidlRoot);
	(*env)->SetIntField(env, lpObject, BROWSEINFOFc.hwndOwner, (jint)lpStruct->hwndOwner);
}
#endif

#ifndef NO_CHOOSECOLOR
typedef struct CHOOSECOLOR_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lpTemplateName, lpfnHook, lCustData, Flags, lpCustColors, rgbResult, hInstance, hwndOwner, lStructSize;
} CHOOSECOLOR_FID_CACHE;

CHOOSECOLOR_FID_CACHE CHOOSECOLORFc;

void cacheCHOOSECOLORFields(JNIEnv *env, jobject lpObject)
{
	if (CHOOSECOLORFc.cached) return;
	CHOOSECOLORFc.clazz = (*env)->GetObjectClass(env, lpObject);
	CHOOSECOLORFc.lpTemplateName = (*env)->GetFieldID(env, CHOOSECOLORFc.clazz, "lpTemplateName", "I");
	CHOOSECOLORFc.lpfnHook = (*env)->GetFieldID(env, CHOOSECOLORFc.clazz, "lpfnHook", "I");
	CHOOSECOLORFc.lCustData = (*env)->GetFieldID(env, CHOOSECOLORFc.clazz, "lCustData", "I");
	CHOOSECOLORFc.Flags = (*env)->GetFieldID(env, CHOOSECOLORFc.clazz, "Flags", "I");
	CHOOSECOLORFc.lpCustColors = (*env)->GetFieldID(env, CHOOSECOLORFc.clazz, "lpCustColors", "I");
	CHOOSECOLORFc.rgbResult = (*env)->GetFieldID(env, CHOOSECOLORFc.clazz, "rgbResult", "I");
	CHOOSECOLORFc.hInstance = (*env)->GetFieldID(env, CHOOSECOLORFc.clazz, "hInstance", "I");
	CHOOSECOLORFc.hwndOwner = (*env)->GetFieldID(env, CHOOSECOLORFc.clazz, "hwndOwner", "I");
	CHOOSECOLORFc.lStructSize = (*env)->GetFieldID(env, CHOOSECOLORFc.clazz, "lStructSize", "I");
	CHOOSECOLORFc.cached = 1;
}

CHOOSECOLOR *getCHOOSECOLORFields(JNIEnv *env, jobject lpObject, CHOOSECOLOR *lpStruct)
{
	if (!CHOOSECOLORFc.cached) cacheCHOOSECOLORFields(env, lpObject);
	lpStruct->lpTemplateName = (LPCTSTR)(*env)->GetIntField(env, lpObject, CHOOSECOLORFc.lpTemplateName);
	lpStruct->lpfnHook = (LPCCHOOKPROC)(*env)->GetIntField(env, lpObject, CHOOSECOLORFc.lpfnHook);
	lpStruct->lCustData = (*env)->GetIntField(env, lpObject, CHOOSECOLORFc.lCustData);
	lpStruct->Flags = (*env)->GetIntField(env, lpObject, CHOOSECOLORFc.Flags);
	lpStruct->lpCustColors = (COLORREF *)(*env)->GetIntField(env, lpObject, CHOOSECOLORFc.lpCustColors);
	lpStruct->rgbResult = (*env)->GetIntField(env, lpObject, CHOOSECOLORFc.rgbResult);
	lpStruct->hInstance = (HANDLE)(*env)->GetIntField(env, lpObject, CHOOSECOLORFc.hInstance);
	lpStruct->hwndOwner = (HWND)(*env)->GetIntField(env, lpObject, CHOOSECOLORFc.hwndOwner);
	lpStruct->lStructSize = (*env)->GetIntField(env, lpObject, CHOOSECOLORFc.lStructSize);
	return lpStruct;
}

void setCHOOSECOLORFields(JNIEnv *env, jobject lpObject, CHOOSECOLOR *lpStruct)
{
	if (!CHOOSECOLORFc.cached) cacheCHOOSECOLORFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, CHOOSECOLORFc.lpTemplateName, (jint)lpStruct->lpTemplateName);
	(*env)->SetIntField(env, lpObject, CHOOSECOLORFc.lpfnHook, (jint)lpStruct->lpfnHook);
	(*env)->SetIntField(env, lpObject, CHOOSECOLORFc.lCustData, (jint)lpStruct->lCustData);
	(*env)->SetIntField(env, lpObject, CHOOSECOLORFc.Flags, (jint)lpStruct->Flags);
	(*env)->SetIntField(env, lpObject, CHOOSECOLORFc.lpCustColors, (jint)lpStruct->lpCustColors);
	(*env)->SetIntField(env, lpObject, CHOOSECOLORFc.rgbResult, (jint)lpStruct->rgbResult);
	(*env)->SetIntField(env, lpObject, CHOOSECOLORFc.hInstance, (jint)lpStruct->hInstance);
	(*env)->SetIntField(env, lpObject, CHOOSECOLORFc.hwndOwner, (jint)lpStruct->hwndOwner);
	(*env)->SetIntField(env, lpObject, CHOOSECOLORFc.lStructSize, (jint)lpStruct->lStructSize);
}
#endif

#ifndef NO_CHOOSEFONT
typedef struct CHOOSEFONT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID nSizeMax, nSizeMin, nFontType, lpszStyle, hInstance, lpTemplateName, lpfnHook, lCustData, rgbColors, Flags, iPointSize, lpLogFont, hDC, hwndOwner, lStructSize;
} CHOOSEFONT_FID_CACHE;

CHOOSEFONT_FID_CACHE CHOOSEFONTFc;

void cacheCHOOSEFONTFields(JNIEnv *env, jobject lpObject)
{
	if (CHOOSEFONTFc.cached) return;
	CHOOSEFONTFc.clazz = (*env)->GetObjectClass(env, lpObject);
	CHOOSEFONTFc.nSizeMax = (*env)->GetFieldID(env, CHOOSEFONTFc.clazz, "nSizeMax", "I");
	CHOOSEFONTFc.nSizeMin = (*env)->GetFieldID(env, CHOOSEFONTFc.clazz, "nSizeMin", "I");
	CHOOSEFONTFc.nFontType = (*env)->GetFieldID(env, CHOOSEFONTFc.clazz, "nFontType", "S");
	CHOOSEFONTFc.lpszStyle = (*env)->GetFieldID(env, CHOOSEFONTFc.clazz, "lpszStyle", "I");
	CHOOSEFONTFc.hInstance = (*env)->GetFieldID(env, CHOOSEFONTFc.clazz, "hInstance", "I");
	CHOOSEFONTFc.lpTemplateName = (*env)->GetFieldID(env, CHOOSEFONTFc.clazz, "lpTemplateName", "I");
	CHOOSEFONTFc.lpfnHook = (*env)->GetFieldID(env, CHOOSEFONTFc.clazz, "lpfnHook", "I");
	CHOOSEFONTFc.lCustData = (*env)->GetFieldID(env, CHOOSEFONTFc.clazz, "lCustData", "I");
	CHOOSEFONTFc.rgbColors = (*env)->GetFieldID(env, CHOOSEFONTFc.clazz, "rgbColors", "I");
	CHOOSEFONTFc.Flags = (*env)->GetFieldID(env, CHOOSEFONTFc.clazz, "Flags", "I");
	CHOOSEFONTFc.iPointSize = (*env)->GetFieldID(env, CHOOSEFONTFc.clazz, "iPointSize", "I");
	CHOOSEFONTFc.lpLogFont = (*env)->GetFieldID(env, CHOOSEFONTFc.clazz, "lpLogFont", "I");
	CHOOSEFONTFc.hDC = (*env)->GetFieldID(env, CHOOSEFONTFc.clazz, "hDC", "I");
	CHOOSEFONTFc.hwndOwner = (*env)->GetFieldID(env, CHOOSEFONTFc.clazz, "hwndOwner", "I");
	CHOOSEFONTFc.lStructSize = (*env)->GetFieldID(env, CHOOSEFONTFc.clazz, "lStructSize", "I");
	CHOOSEFONTFc.cached = 1;
}

CHOOSEFONT *getCHOOSEFONTFields(JNIEnv *env, jobject lpObject, CHOOSEFONT *lpStruct)
{
	if (!CHOOSEFONTFc.cached) cacheCHOOSEFONTFields(env, lpObject);
	lpStruct->nSizeMax = (*env)->GetIntField(env, lpObject, CHOOSEFONTFc.nSizeMax);
	lpStruct->nSizeMin = (*env)->GetIntField(env, lpObject, CHOOSEFONTFc.nSizeMin);
	lpStruct->nFontType = (*env)->GetShortField(env, lpObject, CHOOSEFONTFc.nFontType);
	lpStruct->lpszStyle = (LPTSTR)(*env)->GetIntField(env, lpObject, CHOOSEFONTFc.lpszStyle);
	lpStruct->hInstance = (HINSTANCE)(*env)->GetIntField(env, lpObject, CHOOSEFONTFc.hInstance);
	lpStruct->lpTemplateName = (LPCTSTR)(*env)->GetIntField(env, lpObject, CHOOSEFONTFc.lpTemplateName);
	lpStruct->lpfnHook = (LPCFHOOKPROC)(*env)->GetIntField(env, lpObject, CHOOSEFONTFc.lpfnHook);
	lpStruct->lCustData = (*env)->GetIntField(env, lpObject, CHOOSEFONTFc.lCustData);
	lpStruct->rgbColors = (*env)->GetIntField(env, lpObject, CHOOSEFONTFc.rgbColors);
	lpStruct->Flags = (*env)->GetIntField(env, lpObject, CHOOSEFONTFc.Flags);
	lpStruct->iPointSize = (*env)->GetIntField(env, lpObject, CHOOSEFONTFc.iPointSize);
	lpStruct->lpLogFont = (LPLOGFONT)(*env)->GetIntField(env, lpObject, CHOOSEFONTFc.lpLogFont);
	lpStruct->hDC = (HDC)(*env)->GetIntField(env, lpObject, CHOOSEFONTFc.hDC);
	lpStruct->hwndOwner = (HWND)(*env)->GetIntField(env, lpObject, CHOOSEFONTFc.hwndOwner);
	lpStruct->lStructSize = (*env)->GetIntField(env, lpObject, CHOOSEFONTFc.lStructSize);
	return lpStruct;
}

void setCHOOSEFONTFields(JNIEnv *env, jobject lpObject, CHOOSEFONT *lpStruct)
{
	if (!CHOOSEFONTFc.cached) cacheCHOOSEFONTFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, CHOOSEFONTFc.nSizeMax, (jint)lpStruct->nSizeMax);
	(*env)->SetIntField(env, lpObject, CHOOSEFONTFc.nSizeMin, (jint)lpStruct->nSizeMin);
	(*env)->SetShortField(env, lpObject, CHOOSEFONTFc.nFontType, (jshort)lpStruct->nFontType);
	(*env)->SetIntField(env, lpObject, CHOOSEFONTFc.lpszStyle, (jint)lpStruct->lpszStyle);
	(*env)->SetIntField(env, lpObject, CHOOSEFONTFc.hInstance, (jint)lpStruct->hInstance);
	(*env)->SetIntField(env, lpObject, CHOOSEFONTFc.lpTemplateName, (jint)lpStruct->lpTemplateName);
	(*env)->SetIntField(env, lpObject, CHOOSEFONTFc.lpfnHook, (jint)lpStruct->lpfnHook);
	(*env)->SetIntField(env, lpObject, CHOOSEFONTFc.lCustData, (jint)lpStruct->lCustData);
	(*env)->SetIntField(env, lpObject, CHOOSEFONTFc.rgbColors, (jint)lpStruct->rgbColors);
	(*env)->SetIntField(env, lpObject, CHOOSEFONTFc.Flags, (jint)lpStruct->Flags);
	(*env)->SetIntField(env, lpObject, CHOOSEFONTFc.iPointSize, (jint)lpStruct->iPointSize);
	(*env)->SetIntField(env, lpObject, CHOOSEFONTFc.lpLogFont, (jint)lpStruct->lpLogFont);
	(*env)->SetIntField(env, lpObject, CHOOSEFONTFc.hDC, (jint)lpStruct->hDC);
	(*env)->SetIntField(env, lpObject, CHOOSEFONTFc.hwndOwner, (jint)lpStruct->hwndOwner);
	(*env)->SetIntField(env, lpObject, CHOOSEFONTFc.lStructSize, (jint)lpStruct->lStructSize);
}
#endif

#ifndef NO_COMPOSITIONFORM
typedef struct COMPOSITIONFORM_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID bottom, right, top, left, y, x, dwStyle;
} COMPOSITIONFORM_FID_CACHE;

COMPOSITIONFORM_FID_CACHE COMPOSITIONFORMFc;

void cacheCOMPOSITIONFORMFields(JNIEnv *env, jobject lpObject)
{
	if (COMPOSITIONFORMFc.cached) return;
	COMPOSITIONFORMFc.clazz = (*env)->GetObjectClass(env, lpObject);
	COMPOSITIONFORMFc.bottom = (*env)->GetFieldID(env, COMPOSITIONFORMFc.clazz, "bottom", "I");
	COMPOSITIONFORMFc.right = (*env)->GetFieldID(env, COMPOSITIONFORMFc.clazz, "right", "I");
	COMPOSITIONFORMFc.top = (*env)->GetFieldID(env, COMPOSITIONFORMFc.clazz, "top", "I");
	COMPOSITIONFORMFc.left = (*env)->GetFieldID(env, COMPOSITIONFORMFc.clazz, "left", "I");
	COMPOSITIONFORMFc.y = (*env)->GetFieldID(env, COMPOSITIONFORMFc.clazz, "y", "I");
	COMPOSITIONFORMFc.x = (*env)->GetFieldID(env, COMPOSITIONFORMFc.clazz, "x", "I");
	COMPOSITIONFORMFc.dwStyle = (*env)->GetFieldID(env, COMPOSITIONFORMFc.clazz, "dwStyle", "I");
	COMPOSITIONFORMFc.cached = 1;
}

COMPOSITIONFORM *getCOMPOSITIONFORMFields(JNIEnv *env, jobject lpObject, COMPOSITIONFORM *lpStruct)
{
	if (!COMPOSITIONFORMFc.cached) cacheCOMPOSITIONFORMFields(env, lpObject);
	lpStruct->rcArea.bottom = (*env)->GetIntField(env, lpObject, COMPOSITIONFORMFc.bottom);
	lpStruct->rcArea.right = (*env)->GetIntField(env, lpObject, COMPOSITIONFORMFc.right);
	lpStruct->rcArea.top = (*env)->GetIntField(env, lpObject, COMPOSITIONFORMFc.top);
	lpStruct->rcArea.left = (*env)->GetIntField(env, lpObject, COMPOSITIONFORMFc.left);
	lpStruct->ptCurrentPos.x = (*env)->GetIntField(env, lpObject, COMPOSITIONFORMFc.y);
	lpStruct->ptCurrentPos.y = (*env)->GetIntField(env, lpObject, COMPOSITIONFORMFc.x);
	lpStruct->dwStyle = (*env)->GetIntField(env, lpObject, COMPOSITIONFORMFc.dwStyle);
	return lpStruct;
}

void setCOMPOSITIONFORMFields(JNIEnv *env, jobject lpObject, COMPOSITIONFORM *lpStruct)
{
	if (!COMPOSITIONFORMFc.cached) cacheCOMPOSITIONFORMFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, COMPOSITIONFORMFc.bottom, (jint)lpStruct->rcArea.bottom);
	(*env)->SetIntField(env, lpObject, COMPOSITIONFORMFc.right, (jint)lpStruct->rcArea.right);
	(*env)->SetIntField(env, lpObject, COMPOSITIONFORMFc.top, (jint)lpStruct->rcArea.top);
	(*env)->SetIntField(env, lpObject, COMPOSITIONFORMFc.left, (jint)lpStruct->rcArea.left);
	(*env)->SetIntField(env, lpObject, COMPOSITIONFORMFc.y, (jint)lpStruct->ptCurrentPos.x);
	(*env)->SetIntField(env, lpObject, COMPOSITIONFORMFc.x, (jint)lpStruct->ptCurrentPos.y);
	(*env)->SetIntField(env, lpObject, COMPOSITIONFORMFc.dwStyle, (jint)lpStruct->dwStyle);
}
#endif

#ifndef NO_CREATESTRUCT
typedef struct CREATESTRUCT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID dwExStyle, lpszClass, lpszName, style, x, y, cx, cy, hwndParent, hMenu, hInstance, lpCreateParams;
} CREATESTRUCT_FID_CACHE;

CREATESTRUCT_FID_CACHE CREATESTRUCTFc;

void cacheCREATESTRUCTFields(JNIEnv *env, jobject lpObject)
{
	if (CREATESTRUCTFc.cached) return;
	CREATESTRUCTFc.clazz = (*env)->GetObjectClass(env, lpObject);
	CREATESTRUCTFc.dwExStyle = (*env)->GetFieldID(env, CREATESTRUCTFc.clazz, "dwExStyle", "I");
	CREATESTRUCTFc.lpszClass = (*env)->GetFieldID(env, CREATESTRUCTFc.clazz, "lpszClass", "I");
	CREATESTRUCTFc.lpszName = (*env)->GetFieldID(env, CREATESTRUCTFc.clazz, "lpszName", "I");
	CREATESTRUCTFc.style = (*env)->GetFieldID(env, CREATESTRUCTFc.clazz, "style", "I");
	CREATESTRUCTFc.x = (*env)->GetFieldID(env, CREATESTRUCTFc.clazz, "x", "I");
	CREATESTRUCTFc.y = (*env)->GetFieldID(env, CREATESTRUCTFc.clazz, "y", "I");
	CREATESTRUCTFc.cx = (*env)->GetFieldID(env, CREATESTRUCTFc.clazz, "cx", "I");
	CREATESTRUCTFc.cy = (*env)->GetFieldID(env, CREATESTRUCTFc.clazz, "cy", "I");
	CREATESTRUCTFc.hwndParent = (*env)->GetFieldID(env, CREATESTRUCTFc.clazz, "hwndParent", "I");
	CREATESTRUCTFc.hMenu = (*env)->GetFieldID(env, CREATESTRUCTFc.clazz, "hMenu", "I");
	CREATESTRUCTFc.hInstance = (*env)->GetFieldID(env, CREATESTRUCTFc.clazz, "hInstance", "I");
	CREATESTRUCTFc.lpCreateParams = (*env)->GetFieldID(env, CREATESTRUCTFc.clazz, "lpCreateParams", "I");
	CREATESTRUCTFc.cached = 1;
}

CREATESTRUCT *getCREATESTRUCTFields(JNIEnv *env, jobject lpObject, CREATESTRUCT *lpStruct)
{
	if (!CREATESTRUCTFc.cached) cacheCREATESTRUCTFields(env, lpObject);
	lpStruct->dwExStyle = (*env)->GetIntField(env, lpObject, CREATESTRUCTFc.dwExStyle);
	lpStruct->lpszClass = (LPCTSTR)(*env)->GetIntField(env, lpObject, CREATESTRUCTFc.lpszClass);
	lpStruct->lpszName = (LPCTSTR)(*env)->GetIntField(env, lpObject, CREATESTRUCTFc.lpszName);
	lpStruct->style = (*env)->GetIntField(env, lpObject, CREATESTRUCTFc.style);
	lpStruct->x = (*env)->GetIntField(env, lpObject, CREATESTRUCTFc.x);
	lpStruct->y = (*env)->GetIntField(env, lpObject, CREATESTRUCTFc.y);
	lpStruct->cx = (*env)->GetIntField(env, lpObject, CREATESTRUCTFc.cx);
	lpStruct->cy = (*env)->GetIntField(env, lpObject, CREATESTRUCTFc.cy);
	lpStruct->hwndParent = (HWND)(*env)->GetIntField(env, lpObject, CREATESTRUCTFc.hwndParent);
	lpStruct->hMenu = (HMENU)(*env)->GetIntField(env, lpObject, CREATESTRUCTFc.hMenu);
	lpStruct->hInstance = (HINSTANCE)(*env)->GetIntField(env, lpObject, CREATESTRUCTFc.hInstance);
	lpStruct->lpCreateParams = (LPVOID)(*env)->GetIntField(env, lpObject, CREATESTRUCTFc.lpCreateParams);
	return lpStruct;
}

void setCREATESTRUCTFields(JNIEnv *env, jobject lpObject, CREATESTRUCT *lpStruct)
{
	if (!CREATESTRUCTFc.cached) cacheCREATESTRUCTFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, CREATESTRUCTFc.dwExStyle, (jint)lpStruct->dwExStyle);
	(*env)->SetIntField(env, lpObject, CREATESTRUCTFc.lpszClass, (jint)lpStruct->lpszClass);
	(*env)->SetIntField(env, lpObject, CREATESTRUCTFc.lpszName, (jint)lpStruct->lpszName);
	(*env)->SetIntField(env, lpObject, CREATESTRUCTFc.style, (jint)lpStruct->style);
	(*env)->SetIntField(env, lpObject, CREATESTRUCTFc.x, (jint)lpStruct->x);
	(*env)->SetIntField(env, lpObject, CREATESTRUCTFc.y, (jint)lpStruct->y);
	(*env)->SetIntField(env, lpObject, CREATESTRUCTFc.cx, (jint)lpStruct->cx);
	(*env)->SetIntField(env, lpObject, CREATESTRUCTFc.cy, (jint)lpStruct->cy);
	(*env)->SetIntField(env, lpObject, CREATESTRUCTFc.hwndParent, (jint)lpStruct->hwndParent);
	(*env)->SetIntField(env, lpObject, CREATESTRUCTFc.hMenu, (jint)lpStruct->hMenu);
	(*env)->SetIntField(env, lpObject, CREATESTRUCTFc.hInstance, (jint)lpStruct->hInstance);
	(*env)->SetIntField(env, lpObject, CREATESTRUCTFc.lpCreateParams, (jint)lpStruct->lpCreateParams);
}
#endif

#ifndef NO_DIBSECTION
typedef struct DIBSECTION_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID dsOffset, dshSection, dsBitfields2, dsBitfields1, dsBitfields0, biClrImportant, biClrUsed, biYPelsPerMeter, biXPelsPerMeter, biSizeImage, biCompression, biBitCount, biPlanes, biHeight, biWidth, biSize;
} DIBSECTION_FID_CACHE;

DIBSECTION_FID_CACHE DIBSECTIONFc;

void cacheDIBSECTIONFields(JNIEnv *env, jobject lpObject)
{
	if (DIBSECTIONFc.cached) return;
	cacheBITMAPFields(env, lpObject);
	DIBSECTIONFc.clazz = (*env)->GetObjectClass(env, lpObject);
	DIBSECTIONFc.dsOffset = (*env)->GetFieldID(env, DIBSECTIONFc.clazz, "dsOffset", "I");
	DIBSECTIONFc.dshSection = (*env)->GetFieldID(env, DIBSECTIONFc.clazz, "dshSection", "I");
	DIBSECTIONFc.dsBitfields2 = (*env)->GetFieldID(env, DIBSECTIONFc.clazz, "dsBitfields2", "I");
	DIBSECTIONFc.dsBitfields1 = (*env)->GetFieldID(env, DIBSECTIONFc.clazz, "dsBitfields1", "I");
	DIBSECTIONFc.dsBitfields0 = (*env)->GetFieldID(env, DIBSECTIONFc.clazz, "dsBitfields0", "I");
	DIBSECTIONFc.biClrImportant = (*env)->GetFieldID(env, DIBSECTIONFc.clazz, "biClrImportant", "I");
	DIBSECTIONFc.biClrUsed = (*env)->GetFieldID(env, DIBSECTIONFc.clazz, "biClrUsed", "I");
	DIBSECTIONFc.biYPelsPerMeter = (*env)->GetFieldID(env, DIBSECTIONFc.clazz, "biYPelsPerMeter", "I");
	DIBSECTIONFc.biXPelsPerMeter = (*env)->GetFieldID(env, DIBSECTIONFc.clazz, "biXPelsPerMeter", "I");
	DIBSECTIONFc.biSizeImage = (*env)->GetFieldID(env, DIBSECTIONFc.clazz, "biSizeImage", "I");
	DIBSECTIONFc.biCompression = (*env)->GetFieldID(env, DIBSECTIONFc.clazz, "biCompression", "I");
	DIBSECTIONFc.biBitCount = (*env)->GetFieldID(env, DIBSECTIONFc.clazz, "biBitCount", "S");
	DIBSECTIONFc.biPlanes = (*env)->GetFieldID(env, DIBSECTIONFc.clazz, "biPlanes", "S");
	DIBSECTIONFc.biHeight = (*env)->GetFieldID(env, DIBSECTIONFc.clazz, "biHeight", "I");
	DIBSECTIONFc.biWidth = (*env)->GetFieldID(env, DIBSECTIONFc.clazz, "biWidth", "I");
	DIBSECTIONFc.biSize = (*env)->GetFieldID(env, DIBSECTIONFc.clazz, "biSize", "I");
	DIBSECTIONFc.cached = 1;
}

DIBSECTION *getDIBSECTIONFields(JNIEnv *env, jobject lpObject, DIBSECTION *lpStruct)
{
	if (!DIBSECTIONFc.cached) cacheDIBSECTIONFields(env, lpObject);
	getBITMAPFields(env, lpObject, (BITMAP *)lpStruct);
	lpStruct->dsOffset = (*env)->GetIntField(env, lpObject, DIBSECTIONFc.dsOffset);
	lpStruct->dshSection = (HANDLE)(*env)->GetIntField(env, lpObject, DIBSECTIONFc.dshSection);
	lpStruct->dsBitfields[2] = (*env)->GetIntField(env, lpObject, DIBSECTIONFc.dsBitfields2);
	lpStruct->dsBitfields[1] = (*env)->GetIntField(env, lpObject, DIBSECTIONFc.dsBitfields1);
	lpStruct->dsBitfields[0] = (*env)->GetIntField(env, lpObject, DIBSECTIONFc.dsBitfields0);
	lpStruct->dsBmih.biClrImportant = (*env)->GetIntField(env, lpObject, DIBSECTIONFc.biClrImportant);
	lpStruct->dsBmih.biClrUsed = (*env)->GetIntField(env, lpObject, DIBSECTIONFc.biClrUsed);
	lpStruct->dsBmih.biYPelsPerMeter = (*env)->GetIntField(env, lpObject, DIBSECTIONFc.biYPelsPerMeter);
	lpStruct->dsBmih.biXPelsPerMeter = (*env)->GetIntField(env, lpObject, DIBSECTIONFc.biXPelsPerMeter);
	lpStruct->dsBmih.biSizeImage = (*env)->GetIntField(env, lpObject, DIBSECTIONFc.biSizeImage);
	lpStruct->dsBmih.biCompression = (*env)->GetIntField(env, lpObject, DIBSECTIONFc.biCompression);
	lpStruct->dsBmih.biBitCount = (*env)->GetShortField(env, lpObject, DIBSECTIONFc.biBitCount);
	lpStruct->dsBmih.biPlanes = (*env)->GetShortField(env, lpObject, DIBSECTIONFc.biPlanes);
	lpStruct->dsBmih.biHeight = (*env)->GetIntField(env, lpObject, DIBSECTIONFc.biHeight);
	lpStruct->dsBmih.biWidth = (*env)->GetIntField(env, lpObject, DIBSECTIONFc.biWidth);
	lpStruct->dsBmih.biSize = (*env)->GetIntField(env, lpObject, DIBSECTIONFc.biSize);
	return lpStruct;
}

void setDIBSECTIONFields(JNIEnv *env, jobject lpObject, DIBSECTION *lpStruct)
{
	if (!DIBSECTIONFc.cached) cacheDIBSECTIONFields(env, lpObject);
	setBITMAPFields(env, lpObject, (BITMAP *)lpStruct);
	(*env)->SetIntField(env, lpObject, DIBSECTIONFc.dsOffset, (jint)lpStruct->dsOffset);
	(*env)->SetIntField(env, lpObject, DIBSECTIONFc.dshSection, (jint)lpStruct->dshSection);
	(*env)->SetIntField(env, lpObject, DIBSECTIONFc.dsBitfields2, (jint)lpStruct->dsBitfields[2]);
	(*env)->SetIntField(env, lpObject, DIBSECTIONFc.dsBitfields1, (jint)lpStruct->dsBitfields[1]);
	(*env)->SetIntField(env, lpObject, DIBSECTIONFc.dsBitfields0, (jint)lpStruct->dsBitfields[0]);
	(*env)->SetIntField(env, lpObject, DIBSECTIONFc.biClrImportant, (jint)lpStruct->dsBmih.biClrImportant);
	(*env)->SetIntField(env, lpObject, DIBSECTIONFc.biClrUsed, (jint)lpStruct->dsBmih.biClrUsed);
	(*env)->SetIntField(env, lpObject, DIBSECTIONFc.biYPelsPerMeter, (jint)lpStruct->dsBmih.biYPelsPerMeter);
	(*env)->SetIntField(env, lpObject, DIBSECTIONFc.biXPelsPerMeter, (jint)lpStruct->dsBmih.biXPelsPerMeter);
	(*env)->SetIntField(env, lpObject, DIBSECTIONFc.biSizeImage, (jint)lpStruct->dsBmih.biSizeImage);
	(*env)->SetIntField(env, lpObject, DIBSECTIONFc.biCompression, (jint)lpStruct->dsBmih.biCompression);
	(*env)->SetShortField(env, lpObject, DIBSECTIONFc.biBitCount, (jshort)lpStruct->dsBmih.biBitCount);
	(*env)->SetShortField(env, lpObject, DIBSECTIONFc.biPlanes, (jshort)lpStruct->dsBmih.biPlanes);
	(*env)->SetIntField(env, lpObject, DIBSECTIONFc.biHeight, (jint)lpStruct->dsBmih.biHeight);
	(*env)->SetIntField(env, lpObject, DIBSECTIONFc.biWidth, (jint)lpStruct->dsBmih.biWidth);
	(*env)->SetIntField(env, lpObject, DIBSECTIONFc.biSize, (jint)lpStruct->dsBmih.biSize);
}
#endif

#ifndef NO_DLLVERSIONINFO
typedef struct DLLVERSIONINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID dwPlatformID, dwBuildNumber, dwMinorVersion, dwMajorVersion, cbSize;
} DLLVERSIONINFO_FID_CACHE;

DLLVERSIONINFO_FID_CACHE DLLVERSIONINFOFc;

void cacheDLLVERSIONINFOFields(JNIEnv *env, jobject lpObject)
{
	if (DLLVERSIONINFOFc.cached) return;
	DLLVERSIONINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	DLLVERSIONINFOFc.dwPlatformID = (*env)->GetFieldID(env, DLLVERSIONINFOFc.clazz, "dwPlatformID", "I");
	DLLVERSIONINFOFc.dwBuildNumber = (*env)->GetFieldID(env, DLLVERSIONINFOFc.clazz, "dwBuildNumber", "I");
	DLLVERSIONINFOFc.dwMinorVersion = (*env)->GetFieldID(env, DLLVERSIONINFOFc.clazz, "dwMinorVersion", "I");
	DLLVERSIONINFOFc.dwMajorVersion = (*env)->GetFieldID(env, DLLVERSIONINFOFc.clazz, "dwMajorVersion", "I");
	DLLVERSIONINFOFc.cbSize = (*env)->GetFieldID(env, DLLVERSIONINFOFc.clazz, "cbSize", "I");
	DLLVERSIONINFOFc.cached = 1;
}

DLLVERSIONINFO *getDLLVERSIONINFOFields(JNIEnv *env, jobject lpObject, DLLVERSIONINFO *lpStruct)
{
	if (!DLLVERSIONINFOFc.cached) cacheDLLVERSIONINFOFields(env, lpObject);
	lpStruct->dwPlatformID = (*env)->GetIntField(env, lpObject, DLLVERSIONINFOFc.dwPlatformID);
	lpStruct->dwBuildNumber = (*env)->GetIntField(env, lpObject, DLLVERSIONINFOFc.dwBuildNumber);
	lpStruct->dwMinorVersion = (*env)->GetIntField(env, lpObject, DLLVERSIONINFOFc.dwMinorVersion);
	lpStruct->dwMajorVersion = (*env)->GetIntField(env, lpObject, DLLVERSIONINFOFc.dwMajorVersion);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, DLLVERSIONINFOFc.cbSize);
	return lpStruct;
}

void setDLLVERSIONINFOFields(JNIEnv *env, jobject lpObject, DLLVERSIONINFO *lpStruct)
{
	if (!DLLVERSIONINFOFc.cached) cacheDLLVERSIONINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, DLLVERSIONINFOFc.dwPlatformID, (jint)lpStruct->dwPlatformID);
	(*env)->SetIntField(env, lpObject, DLLVERSIONINFOFc.dwBuildNumber, (jint)lpStruct->dwBuildNumber);
	(*env)->SetIntField(env, lpObject, DLLVERSIONINFOFc.dwMinorVersion, (jint)lpStruct->dwMinorVersion);
	(*env)->SetIntField(env, lpObject, DLLVERSIONINFOFc.dwMajorVersion, (jint)lpStruct->dwMajorVersion);
	(*env)->SetIntField(env, lpObject, DLLVERSIONINFOFc.cbSize, (jint)lpStruct->cbSize);
}
#endif

#ifndef NO_DOCINFO
typedef struct DOCINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID fwType, lpszDatatype, lpszOutput, lpszDocName, cbSize;
} DOCINFO_FID_CACHE;

DOCINFO_FID_CACHE DOCINFOFc;

void cacheDOCINFOFields(JNIEnv *env, jobject lpObject)
{
	if (DOCINFOFc.cached) return;
	DOCINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	DOCINFOFc.fwType = (*env)->GetFieldID(env, DOCINFOFc.clazz, "fwType", "I");
	DOCINFOFc.lpszDatatype = (*env)->GetFieldID(env, DOCINFOFc.clazz, "lpszDatatype", "I");
	DOCINFOFc.lpszOutput = (*env)->GetFieldID(env, DOCINFOFc.clazz, "lpszOutput", "I");
	DOCINFOFc.lpszDocName = (*env)->GetFieldID(env, DOCINFOFc.clazz, "lpszDocName", "I");
	DOCINFOFc.cbSize = (*env)->GetFieldID(env, DOCINFOFc.clazz, "cbSize", "I");
	DOCINFOFc.cached = 1;
}

DOCINFO *getDOCINFOFields(JNIEnv *env, jobject lpObject, DOCINFO *lpStruct)
{
	if (!DOCINFOFc.cached) cacheDOCINFOFields(env, lpObject);
	lpStruct->fwType = (*env)->GetIntField(env, lpObject, DOCINFOFc.fwType);
	lpStruct->lpszDatatype = (LPCTSTR)(*env)->GetIntField(env, lpObject, DOCINFOFc.lpszDatatype);
	lpStruct->lpszOutput = (LPCTSTR)(*env)->GetIntField(env, lpObject, DOCINFOFc.lpszOutput);
	lpStruct->lpszDocName = (LPCTSTR)(*env)->GetIntField(env, lpObject, DOCINFOFc.lpszDocName);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, DOCINFOFc.cbSize);
	return lpStruct;
}

void setDOCINFOFields(JNIEnv *env, jobject lpObject, DOCINFO *lpStruct)
{
	if (!DOCINFOFc.cached) cacheDOCINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, DOCINFOFc.fwType, (jint)lpStruct->fwType);
	(*env)->SetIntField(env, lpObject, DOCINFOFc.lpszDatatype, (jint)lpStruct->lpszDatatype);
	(*env)->SetIntField(env, lpObject, DOCINFOFc.lpszOutput, (jint)lpStruct->lpszOutput);
	(*env)->SetIntField(env, lpObject, DOCINFOFc.lpszDocName, (jint)lpStruct->lpszDocName);
	(*env)->SetIntField(env, lpObject, DOCINFOFc.cbSize, (jint)lpStruct->cbSize);
}
#endif

#ifndef NO_DRAWITEMSTRUCT
typedef struct DRAWITEMSTRUCT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID itemData, right, bottom, top, left, hDC, hwndItem, itemState, itemAction, itemID, CtlID, CtlType;
} DRAWITEMSTRUCT_FID_CACHE;

DRAWITEMSTRUCT_FID_CACHE DRAWITEMSTRUCTFc;

void cacheDRAWITEMSTRUCTFields(JNIEnv *env, jobject lpObject)
{
	if (DRAWITEMSTRUCTFc.cached) return;
	DRAWITEMSTRUCTFc.clazz = (*env)->GetObjectClass(env, lpObject);
	DRAWITEMSTRUCTFc.itemData = (*env)->GetFieldID(env, DRAWITEMSTRUCTFc.clazz, "itemData", "I");
	DRAWITEMSTRUCTFc.right = (*env)->GetFieldID(env, DRAWITEMSTRUCTFc.clazz, "right", "I");
	DRAWITEMSTRUCTFc.bottom = (*env)->GetFieldID(env, DRAWITEMSTRUCTFc.clazz, "bottom", "I");
	DRAWITEMSTRUCTFc.top = (*env)->GetFieldID(env, DRAWITEMSTRUCTFc.clazz, "top", "I");
	DRAWITEMSTRUCTFc.left = (*env)->GetFieldID(env, DRAWITEMSTRUCTFc.clazz, "left", "I");
	DRAWITEMSTRUCTFc.hDC = (*env)->GetFieldID(env, DRAWITEMSTRUCTFc.clazz, "hDC", "I");
	DRAWITEMSTRUCTFc.hwndItem = (*env)->GetFieldID(env, DRAWITEMSTRUCTFc.clazz, "hwndItem", "I");
	DRAWITEMSTRUCTFc.itemState = (*env)->GetFieldID(env, DRAWITEMSTRUCTFc.clazz, "itemState", "I");
	DRAWITEMSTRUCTFc.itemAction = (*env)->GetFieldID(env, DRAWITEMSTRUCTFc.clazz, "itemAction", "I");
	DRAWITEMSTRUCTFc.itemID = (*env)->GetFieldID(env, DRAWITEMSTRUCTFc.clazz, "itemID", "I");
	DRAWITEMSTRUCTFc.CtlID = (*env)->GetFieldID(env, DRAWITEMSTRUCTFc.clazz, "CtlID", "I");
	DRAWITEMSTRUCTFc.CtlType = (*env)->GetFieldID(env, DRAWITEMSTRUCTFc.clazz, "CtlType", "I");
	DRAWITEMSTRUCTFc.cached = 1;
}

DRAWITEMSTRUCT *getDRAWITEMSTRUCTFields(JNIEnv *env, jobject lpObject, DRAWITEMSTRUCT *lpStruct)
{
	if (!DRAWITEMSTRUCTFc.cached) cacheDRAWITEMSTRUCTFields(env, lpObject);
	lpStruct->itemData = (*env)->GetIntField(env, lpObject, DRAWITEMSTRUCTFc.itemData);
	lpStruct->rcItem.right = (*env)->GetIntField(env, lpObject, DRAWITEMSTRUCTFc.right);
	lpStruct->rcItem.bottom = (*env)->GetIntField(env, lpObject, DRAWITEMSTRUCTFc.bottom);
	lpStruct->rcItem.top = (*env)->GetIntField(env, lpObject, DRAWITEMSTRUCTFc.top);
	lpStruct->rcItem.left = (*env)->GetIntField(env, lpObject, DRAWITEMSTRUCTFc.left);
	lpStruct->hDC = (HDC)(*env)->GetIntField(env, lpObject, DRAWITEMSTRUCTFc.hDC);
	lpStruct->hwndItem = (HWND)(*env)->GetIntField(env, lpObject, DRAWITEMSTRUCTFc.hwndItem);
	lpStruct->itemState = (*env)->GetIntField(env, lpObject, DRAWITEMSTRUCTFc.itemState);
	lpStruct->itemAction = (*env)->GetIntField(env, lpObject, DRAWITEMSTRUCTFc.itemAction);
	lpStruct->itemID = (*env)->GetIntField(env, lpObject, DRAWITEMSTRUCTFc.itemID);
	lpStruct->CtlID = (*env)->GetIntField(env, lpObject, DRAWITEMSTRUCTFc.CtlID);
	lpStruct->CtlType = (*env)->GetIntField(env, lpObject, DRAWITEMSTRUCTFc.CtlType);
	return lpStruct;
}

void setDRAWITEMSTRUCTFields(JNIEnv *env, jobject lpObject, DRAWITEMSTRUCT *lpStruct)
{
	if (!DRAWITEMSTRUCTFc.cached) cacheDRAWITEMSTRUCTFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, DRAWITEMSTRUCTFc.itemData, (jint)lpStruct->itemData);
	(*env)->SetIntField(env, lpObject, DRAWITEMSTRUCTFc.right, (jint)lpStruct->rcItem.right);
	(*env)->SetIntField(env, lpObject, DRAWITEMSTRUCTFc.bottom, (jint)lpStruct->rcItem.bottom);
	(*env)->SetIntField(env, lpObject, DRAWITEMSTRUCTFc.top, (jint)lpStruct->rcItem.top);
	(*env)->SetIntField(env, lpObject, DRAWITEMSTRUCTFc.left, (jint)lpStruct->rcItem.left);
	(*env)->SetIntField(env, lpObject, DRAWITEMSTRUCTFc.hDC, (jint)lpStruct->hDC);
	(*env)->SetIntField(env, lpObject, DRAWITEMSTRUCTFc.hwndItem, (jint)lpStruct->hwndItem);
	(*env)->SetIntField(env, lpObject, DRAWITEMSTRUCTFc.itemState, (jint)lpStruct->itemState);
	(*env)->SetIntField(env, lpObject, DRAWITEMSTRUCTFc.itemAction, (jint)lpStruct->itemAction);
	(*env)->SetIntField(env, lpObject, DRAWITEMSTRUCTFc.itemID, (jint)lpStruct->itemID);
	(*env)->SetIntField(env, lpObject, DRAWITEMSTRUCTFc.CtlID, (jint)lpStruct->CtlID);
	(*env)->SetIntField(env, lpObject, DRAWITEMSTRUCTFc.CtlType, (jint)lpStruct->CtlType);
}
#endif

#ifndef NO_DROPFILES
typedef struct DROPFILES_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID fWide, fNC, pt_y, pt_x, pFiles;
} DROPFILES_FID_CACHE;

DROPFILES_FID_CACHE DROPFILESFc;

void cacheDROPFILESFields(JNIEnv *env, jobject lpObject)
{
	if (DROPFILESFc.cached) return;
	DROPFILESFc.clazz = (*env)->GetObjectClass(env, lpObject);
	DROPFILESFc.fWide = (*env)->GetFieldID(env, DROPFILESFc.clazz, "fWide", "I");
	DROPFILESFc.fNC = (*env)->GetFieldID(env, DROPFILESFc.clazz, "fNC", "I");
	DROPFILESFc.pt_y = (*env)->GetFieldID(env, DROPFILESFc.clazz, "pt_y", "I");
	DROPFILESFc.pt_x = (*env)->GetFieldID(env, DROPFILESFc.clazz, "pt_x", "I");
	DROPFILESFc.pFiles = (*env)->GetFieldID(env, DROPFILESFc.clazz, "pFiles", "I");
	DROPFILESFc.cached = 1;
}

DROPFILES *getDROPFILESFields(JNIEnv *env, jobject lpObject, DROPFILES *lpStruct)
{
	if (!DROPFILESFc.cached) cacheDROPFILESFields(env, lpObject);
	lpStruct->fWide = (*env)->GetIntField(env, lpObject, DROPFILESFc.fWide);
	lpStruct->fNC = (*env)->GetIntField(env, lpObject, DROPFILESFc.fNC);
	lpStruct->pt.y = (*env)->GetIntField(env, lpObject, DROPFILESFc.pt_y);
	lpStruct->pt.x = (*env)->GetIntField(env, lpObject, DROPFILESFc.pt_x);
	lpStruct->pFiles = (*env)->GetIntField(env, lpObject, DROPFILESFc.pFiles);
	return lpStruct;
}

void setDROPFILESFields(JNIEnv *env, jobject lpObject, DROPFILES *lpStruct)
{
	if (!DROPFILESFc.cached) cacheDROPFILESFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, DROPFILESFc.fWide, (jint)lpStruct->fWide);
	(*env)->SetIntField(env, lpObject, DROPFILESFc.fNC, (jint)lpStruct->fNC);
	(*env)->SetIntField(env, lpObject, DROPFILESFc.pt_y, (jint)lpStruct->pt.y);
	(*env)->SetIntField(env, lpObject, DROPFILESFc.pt_x, (jint)lpStruct->pt.x);
	(*env)->SetIntField(env, lpObject, DROPFILESFc.pFiles, (jint)lpStruct->pFiles);
}
#endif

#ifndef NO_FILETIME
typedef struct FILETIME_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID dwHighDateTime, dwLowDateTime;
} FILETIME_FID_CACHE;

FILETIME_FID_CACHE FILETIMEFc;

void cacheFILETIMEFields(JNIEnv *env, jobject lpObject)
{
	if (FILETIMEFc.cached) return;
	FILETIMEFc.clazz = (*env)->GetObjectClass(env, lpObject);
	FILETIMEFc.dwHighDateTime = (*env)->GetFieldID(env, FILETIMEFc.clazz, "dwHighDateTime", "I");
	FILETIMEFc.dwLowDateTime = (*env)->GetFieldID(env, FILETIMEFc.clazz, "dwLowDateTime", "I");
	FILETIMEFc.cached = 1;
}

FILETIME *getFILETIMEFields(JNIEnv *env, jobject lpObject, FILETIME *lpStruct)
{
	if (!FILETIMEFc.cached) cacheFILETIMEFields(env, lpObject);
	lpStruct->dwHighDateTime = (*env)->GetIntField(env, lpObject, FILETIMEFc.dwHighDateTime);
	lpStruct->dwLowDateTime = (*env)->GetIntField(env, lpObject, FILETIMEFc.dwLowDateTime);
	return lpStruct;
}

void setFILETIMEFields(JNIEnv *env, jobject lpObject, FILETIME *lpStruct)
{
	if (!FILETIMEFc.cached) cacheFILETIMEFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, FILETIMEFc.dwHighDateTime, (jint)lpStruct->dwHighDateTime);
	(*env)->SetIntField(env, lpObject, FILETIMEFc.dwLowDateTime, (jint)lpStruct->dwLowDateTime);
}
#endif

#ifndef NO_GCP_RESULTS
typedef struct GCP_RESULTS_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID nMaxFit, nGlyphs, lpGlyphs, lpClass, lpCaretPos, lpDx, lpOrder, lpOutString, lStructSize;
} GCP_RESULTS_FID_CACHE;

GCP_RESULTS_FID_CACHE GCP_RESULTSFc;

void cacheGCP_RESULTSFields(JNIEnv *env, jobject lpObject)
{
	if (GCP_RESULTSFc.cached) return;
	GCP_RESULTSFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GCP_RESULTSFc.nMaxFit = (*env)->GetFieldID(env, GCP_RESULTSFc.clazz, "nMaxFit", "I");
	GCP_RESULTSFc.nGlyphs = (*env)->GetFieldID(env, GCP_RESULTSFc.clazz, "nGlyphs", "I");
	GCP_RESULTSFc.lpGlyphs = (*env)->GetFieldID(env, GCP_RESULTSFc.clazz, "lpGlyphs", "I");
	GCP_RESULTSFc.lpClass = (*env)->GetFieldID(env, GCP_RESULTSFc.clazz, "lpClass", "I");
	GCP_RESULTSFc.lpCaretPos = (*env)->GetFieldID(env, GCP_RESULTSFc.clazz, "lpCaretPos", "I");
	GCP_RESULTSFc.lpDx = (*env)->GetFieldID(env, GCP_RESULTSFc.clazz, "lpDx", "I");
	GCP_RESULTSFc.lpOrder = (*env)->GetFieldID(env, GCP_RESULTSFc.clazz, "lpOrder", "I");
	GCP_RESULTSFc.lpOutString = (*env)->GetFieldID(env, GCP_RESULTSFc.clazz, "lpOutString", "I");
	GCP_RESULTSFc.lStructSize = (*env)->GetFieldID(env, GCP_RESULTSFc.clazz, "lStructSize", "I");
	GCP_RESULTSFc.cached = 1;
}

GCP_RESULTS *getGCP_RESULTSFields(JNIEnv *env, jobject lpObject, GCP_RESULTS *lpStruct)
{
	if (!GCP_RESULTSFc.cached) cacheGCP_RESULTSFields(env, lpObject);
	lpStruct->nMaxFit = (*env)->GetIntField(env, lpObject, GCP_RESULTSFc.nMaxFit);
	lpStruct->nGlyphs = (*env)->GetIntField(env, lpObject, GCP_RESULTSFc.nGlyphs);
	lpStruct->lpGlyphs = (LPWSTR)(*env)->GetIntField(env, lpObject, GCP_RESULTSFc.lpGlyphs);
	lpStruct->lpClass = (LPSTR)(*env)->GetIntField(env, lpObject, GCP_RESULTSFc.lpClass);
	lpStruct->lpCaretPos = (int  *)(*env)->GetIntField(env, lpObject, GCP_RESULTSFc.lpCaretPos);
	lpStruct->lpDx = (int  *)(*env)->GetIntField(env, lpObject, GCP_RESULTSFc.lpDx);
	lpStruct->lpOrder = (UINT  *)(*env)->GetIntField(env, lpObject, GCP_RESULTSFc.lpOrder);
	lpStruct->lpOutString = (LPTSTR)(*env)->GetIntField(env, lpObject, GCP_RESULTSFc.lpOutString);
	lpStruct->lStructSize = (*env)->GetIntField(env, lpObject, GCP_RESULTSFc.lStructSize);
	return lpStruct;
}

void setGCP_RESULTSFields(JNIEnv *env, jobject lpObject, GCP_RESULTS *lpStruct)
{
	if (!GCP_RESULTSFc.cached) cacheGCP_RESULTSFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GCP_RESULTSFc.nMaxFit, (jint)lpStruct->nMaxFit);
	(*env)->SetIntField(env, lpObject, GCP_RESULTSFc.nGlyphs, (jint)lpStruct->nGlyphs);
	(*env)->SetIntField(env, lpObject, GCP_RESULTSFc.lpGlyphs, (jint)lpStruct->lpGlyphs);
	(*env)->SetIntField(env, lpObject, GCP_RESULTSFc.lpClass, (jint)lpStruct->lpClass);
	(*env)->SetIntField(env, lpObject, GCP_RESULTSFc.lpCaretPos, (jint)lpStruct->lpCaretPos);
	(*env)->SetIntField(env, lpObject, GCP_RESULTSFc.lpDx, (jint)lpStruct->lpDx);
	(*env)->SetIntField(env, lpObject, GCP_RESULTSFc.lpOrder, (jint)lpStruct->lpOrder);
	(*env)->SetIntField(env, lpObject, GCP_RESULTSFc.lpOutString, (jint)lpStruct->lpOutString);
	(*env)->SetIntField(env, lpObject, GCP_RESULTSFc.lStructSize, (jint)lpStruct->lStructSize);
}
#endif

#ifndef NO_GRADIENT_RECT
typedef struct GRADIENT_RECT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID LowerRight, UpperLeft;
} GRADIENT_RECT_FID_CACHE;

GRADIENT_RECT_FID_CACHE GRADIENT_RECTFc;

void cacheGRADIENT_RECTFields(JNIEnv *env, jobject lpObject)
{
	if (GRADIENT_RECTFc.cached) return;
	GRADIENT_RECTFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GRADIENT_RECTFc.LowerRight = (*env)->GetFieldID(env, GRADIENT_RECTFc.clazz, "LowerRight", "I");
	GRADIENT_RECTFc.UpperLeft = (*env)->GetFieldID(env, GRADIENT_RECTFc.clazz, "UpperLeft", "I");
	GRADIENT_RECTFc.cached = 1;
}

GRADIENT_RECT *getGRADIENT_RECTFields(JNIEnv *env, jobject lpObject, GRADIENT_RECT *lpStruct)
{
	if (!GRADIENT_RECTFc.cached) cacheGRADIENT_RECTFields(env, lpObject);
	lpStruct->LowerRight = (*env)->GetIntField(env, lpObject, GRADIENT_RECTFc.LowerRight);
	lpStruct->UpperLeft = (*env)->GetIntField(env, lpObject, GRADIENT_RECTFc.UpperLeft);
	return lpStruct;
}

void setGRADIENT_RECTFields(JNIEnv *env, jobject lpObject, GRADIENT_RECT *lpStruct)
{
	if (!GRADIENT_RECTFc.cached) cacheGRADIENT_RECTFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GRADIENT_RECTFc.LowerRight, (jint)lpStruct->LowerRight);
	(*env)->SetIntField(env, lpObject, GRADIENT_RECTFc.UpperLeft, (jint)lpStruct->UpperLeft);
}
#endif

#ifndef NO_HDITEM
typedef struct HDITEM_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID iOrder, iImage, lParam, fmt, cchTextMax, hbm, pszText, cxy, mask;
} HDITEM_FID_CACHE;

HDITEM_FID_CACHE HDITEMFc;

void cacheHDITEMFields(JNIEnv *env, jobject lpObject)
{
	if (HDITEMFc.cached) return;
	HDITEMFc.clazz = (*env)->GetObjectClass(env, lpObject);
	HDITEMFc.iOrder = (*env)->GetFieldID(env, HDITEMFc.clazz, "iOrder", "I");
	HDITEMFc.iImage = (*env)->GetFieldID(env, HDITEMFc.clazz, "iImage", "I");
	HDITEMFc.lParam = (*env)->GetFieldID(env, HDITEMFc.clazz, "lParam", "I");
	HDITEMFc.fmt = (*env)->GetFieldID(env, HDITEMFc.clazz, "fmt", "I");
	HDITEMFc.cchTextMax = (*env)->GetFieldID(env, HDITEMFc.clazz, "cchTextMax", "I");
	HDITEMFc.hbm = (*env)->GetFieldID(env, HDITEMFc.clazz, "hbm", "I");
	HDITEMFc.pszText = (*env)->GetFieldID(env, HDITEMFc.clazz, "pszText", "I");
	HDITEMFc.cxy = (*env)->GetFieldID(env, HDITEMFc.clazz, "cxy", "I");
	HDITEMFc.mask = (*env)->GetFieldID(env, HDITEMFc.clazz, "mask", "I");
	HDITEMFc.cached = 1;
}

HDITEM *getHDITEMFields(JNIEnv *env, jobject lpObject, HDITEM *lpStruct)
{
	if (!HDITEMFc.cached) cacheHDITEMFields(env, lpObject);
	lpStruct->iOrder = (*env)->GetIntField(env, lpObject, HDITEMFc.iOrder);
	lpStruct->iImage = (*env)->GetIntField(env, lpObject, HDITEMFc.iImage);
	lpStruct->lParam = (*env)->GetIntField(env, lpObject, HDITEMFc.lParam);
	lpStruct->fmt = (*env)->GetIntField(env, lpObject, HDITEMFc.fmt);
	lpStruct->cchTextMax = (*env)->GetIntField(env, lpObject, HDITEMFc.cchTextMax);
	lpStruct->hbm = (HBITMAP)(*env)->GetIntField(env, lpObject, HDITEMFc.hbm);
	lpStruct->pszText = (LPTSTR)(*env)->GetIntField(env, lpObject, HDITEMFc.pszText);
	lpStruct->cxy = (*env)->GetIntField(env, lpObject, HDITEMFc.cxy);
	lpStruct->mask = (*env)->GetIntField(env, lpObject, HDITEMFc.mask);
	return lpStruct;
}

void setHDITEMFields(JNIEnv *env, jobject lpObject, HDITEM *lpStruct)
{
	if (!HDITEMFc.cached) cacheHDITEMFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, HDITEMFc.iOrder, (jint)lpStruct->iOrder);
	(*env)->SetIntField(env, lpObject, HDITEMFc.iImage, (jint)lpStruct->iImage);
	(*env)->SetIntField(env, lpObject, HDITEMFc.lParam, (jint)lpStruct->lParam);
	(*env)->SetIntField(env, lpObject, HDITEMFc.fmt, (jint)lpStruct->fmt);
	(*env)->SetIntField(env, lpObject, HDITEMFc.cchTextMax, (jint)lpStruct->cchTextMax);
	(*env)->SetIntField(env, lpObject, HDITEMFc.hbm, (jint)lpStruct->hbm);
	(*env)->SetIntField(env, lpObject, HDITEMFc.pszText, (jint)lpStruct->pszText);
	(*env)->SetIntField(env, lpObject, HDITEMFc.cxy, (jint)lpStruct->cxy);
	(*env)->SetIntField(env, lpObject, HDITEMFc.mask, (jint)lpStruct->mask);
}
#endif

#ifndef NO_HELPINFO
typedef struct HELPINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID y, x, dwContextId, hItemHandle, iCtrlId, iContextType, cbSize;
} HELPINFO_FID_CACHE;

HELPINFO_FID_CACHE HELPINFOFc;

void cacheHELPINFOFields(JNIEnv *env, jobject lpObject)
{
	if (HELPINFOFc.cached) return;
	HELPINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	HELPINFOFc.y = (*env)->GetFieldID(env, HELPINFOFc.clazz, "y", "I");
	HELPINFOFc.x = (*env)->GetFieldID(env, HELPINFOFc.clazz, "x", "I");
	HELPINFOFc.dwContextId = (*env)->GetFieldID(env, HELPINFOFc.clazz, "dwContextId", "I");
	HELPINFOFc.hItemHandle = (*env)->GetFieldID(env, HELPINFOFc.clazz, "hItemHandle", "I");
	HELPINFOFc.iCtrlId = (*env)->GetFieldID(env, HELPINFOFc.clazz, "iCtrlId", "I");
	HELPINFOFc.iContextType = (*env)->GetFieldID(env, HELPINFOFc.clazz, "iContextType", "I");
	HELPINFOFc.cbSize = (*env)->GetFieldID(env, HELPINFOFc.clazz, "cbSize", "I");
	HELPINFOFc.cached = 1;
}

HELPINFO *getHELPINFOFields(JNIEnv *env, jobject lpObject, HELPINFO *lpStruct)
{
	if (!HELPINFOFc.cached) cacheHELPINFOFields(env, lpObject);
	lpStruct->MousePos.y = (*env)->GetIntField(env, lpObject, HELPINFOFc.y);
	lpStruct->MousePos.x = (*env)->GetIntField(env, lpObject, HELPINFOFc.x);
	lpStruct->dwContextId = (*env)->GetIntField(env, lpObject, HELPINFOFc.dwContextId);
	lpStruct->hItemHandle = (HANDLE)(*env)->GetIntField(env, lpObject, HELPINFOFc.hItemHandle);
	lpStruct->iCtrlId = (*env)->GetIntField(env, lpObject, HELPINFOFc.iCtrlId);
	lpStruct->iContextType = (*env)->GetIntField(env, lpObject, HELPINFOFc.iContextType);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, HELPINFOFc.cbSize);
	return lpStruct;
}

void setHELPINFOFields(JNIEnv *env, jobject lpObject, HELPINFO *lpStruct)
{
	if (!HELPINFOFc.cached) cacheHELPINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, HELPINFOFc.y, (jint)lpStruct->MousePos.y);
	(*env)->SetIntField(env, lpObject, HELPINFOFc.x, (jint)lpStruct->MousePos.x);
	(*env)->SetIntField(env, lpObject, HELPINFOFc.dwContextId, (jint)lpStruct->dwContextId);
	(*env)->SetIntField(env, lpObject, HELPINFOFc.hItemHandle, (jint)lpStruct->hItemHandle);
	(*env)->SetIntField(env, lpObject, HELPINFOFc.iCtrlId, (jint)lpStruct->iCtrlId);
	(*env)->SetIntField(env, lpObject, HELPINFOFc.iContextType, (jint)lpStruct->iContextType);
	(*env)->SetIntField(env, lpObject, HELPINFOFc.cbSize, (jint)lpStruct->cbSize);
}
#endif

#ifndef NO_ICONINFO
typedef struct ICONINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID hbmColor, hbmMask, yHotspot, xHotspot, fIcon;
} ICONINFO_FID_CACHE;

ICONINFO_FID_CACHE ICONINFOFc;

void cacheICONINFOFields(JNIEnv *env, jobject lpObject)
{
	if (ICONINFOFc.cached) return;
	ICONINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	ICONINFOFc.hbmColor = (*env)->GetFieldID(env, ICONINFOFc.clazz, "hbmColor", "I");
	ICONINFOFc.hbmMask = (*env)->GetFieldID(env, ICONINFOFc.clazz, "hbmMask", "I");
	ICONINFOFc.yHotspot = (*env)->GetFieldID(env, ICONINFOFc.clazz, "yHotspot", "I");
	ICONINFOFc.xHotspot = (*env)->GetFieldID(env, ICONINFOFc.clazz, "xHotspot", "I");
	ICONINFOFc.fIcon = (*env)->GetFieldID(env, ICONINFOFc.clazz, "fIcon", "Z");
	ICONINFOFc.cached = 1;
}

ICONINFO *getICONINFOFields(JNIEnv *env, jobject lpObject, ICONINFO *lpStruct)
{
	if (!ICONINFOFc.cached) cacheICONINFOFields(env, lpObject);
	lpStruct->hbmColor = (HBITMAP)(*env)->GetIntField(env, lpObject, ICONINFOFc.hbmColor);
	lpStruct->hbmMask = (HBITMAP)(*env)->GetIntField(env, lpObject, ICONINFOFc.hbmMask);
	lpStruct->yHotspot = (*env)->GetIntField(env, lpObject, ICONINFOFc.yHotspot);
	lpStruct->xHotspot = (*env)->GetIntField(env, lpObject, ICONINFOFc.xHotspot);
	lpStruct->fIcon = (*env)->GetBooleanField(env, lpObject, ICONINFOFc.fIcon);
	return lpStruct;
}

void setICONINFOFields(JNIEnv *env, jobject lpObject, ICONINFO *lpStruct)
{
	if (!ICONINFOFc.cached) cacheICONINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, ICONINFOFc.hbmColor, (jint)lpStruct->hbmColor);
	(*env)->SetIntField(env, lpObject, ICONINFOFc.hbmMask, (jint)lpStruct->hbmMask);
	(*env)->SetIntField(env, lpObject, ICONINFOFc.yHotspot, (jint)lpStruct->yHotspot);
	(*env)->SetIntField(env, lpObject, ICONINFOFc.xHotspot, (jint)lpStruct->xHotspot);
	(*env)->SetBooleanField(env, lpObject, ICONINFOFc.fIcon, (jboolean)lpStruct->fIcon);
}
#endif

#ifndef NO_INITCOMMONCONTROLSEX
typedef struct INITCOMMONCONTROLSEX_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID dwICC, dwSize;
} INITCOMMONCONTROLSEX_FID_CACHE;

INITCOMMONCONTROLSEX_FID_CACHE INITCOMMONCONTROLSEXFc;

void cacheINITCOMMONCONTROLSEXFields(JNIEnv *env, jobject lpObject)
{
	if (INITCOMMONCONTROLSEXFc.cached) return;
	INITCOMMONCONTROLSEXFc.clazz = (*env)->GetObjectClass(env, lpObject);
	INITCOMMONCONTROLSEXFc.dwICC = (*env)->GetFieldID(env, INITCOMMONCONTROLSEXFc.clazz, "dwICC", "I");
	INITCOMMONCONTROLSEXFc.dwSize = (*env)->GetFieldID(env, INITCOMMONCONTROLSEXFc.clazz, "dwSize", "I");
	INITCOMMONCONTROLSEXFc.cached = 1;
}

INITCOMMONCONTROLSEX *getINITCOMMONCONTROLSEXFields(JNIEnv *env, jobject lpObject, INITCOMMONCONTROLSEX *lpStruct)
{
	if (!INITCOMMONCONTROLSEXFc.cached) cacheINITCOMMONCONTROLSEXFields(env, lpObject);
	lpStruct->dwICC = (*env)->GetIntField(env, lpObject, INITCOMMONCONTROLSEXFc.dwICC);
	lpStruct->dwSize = (*env)->GetIntField(env, lpObject, INITCOMMONCONTROLSEXFc.dwSize);
	return lpStruct;
}

void setINITCOMMONCONTROLSEXFields(JNIEnv *env, jobject lpObject, INITCOMMONCONTROLSEX *lpStruct)
{
	if (!INITCOMMONCONTROLSEXFc.cached) cacheINITCOMMONCONTROLSEXFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, INITCOMMONCONTROLSEXFc.dwICC, (jint)lpStruct->dwICC);
	(*env)->SetIntField(env, lpObject, INITCOMMONCONTROLSEXFc.dwSize, (jint)lpStruct->dwSize);
}
#endif

#ifndef NO_LOGBRUSH
typedef struct LOGBRUSH_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lbHatch, lbColor, lbStyle;
} LOGBRUSH_FID_CACHE;

LOGBRUSH_FID_CACHE LOGBRUSHFc;

void cacheLOGBRUSHFields(JNIEnv *env, jobject lpObject)
{
	if (LOGBRUSHFc.cached) return;
	LOGBRUSHFc.clazz = (*env)->GetObjectClass(env, lpObject);
	LOGBRUSHFc.lbHatch = (*env)->GetFieldID(env, LOGBRUSHFc.clazz, "lbHatch", "I");
	LOGBRUSHFc.lbColor = (*env)->GetFieldID(env, LOGBRUSHFc.clazz, "lbColor", "I");
	LOGBRUSHFc.lbStyle = (*env)->GetFieldID(env, LOGBRUSHFc.clazz, "lbStyle", "I");
	LOGBRUSHFc.cached = 1;
}

LOGBRUSH *getLOGBRUSHFields(JNIEnv *env, jobject lpObject, LOGBRUSH *lpStruct)
{
	if (!LOGBRUSHFc.cached) cacheLOGBRUSHFields(env, lpObject);
	lpStruct->lbHatch = (*env)->GetIntField(env, lpObject, LOGBRUSHFc.lbHatch);
	lpStruct->lbColor = (*env)->GetIntField(env, lpObject, LOGBRUSHFc.lbColor);
	lpStruct->lbStyle = (*env)->GetIntField(env, lpObject, LOGBRUSHFc.lbStyle);
	return lpStruct;
}

void setLOGBRUSHFields(JNIEnv *env, jobject lpObject, LOGBRUSH *lpStruct)
{
	if (!LOGBRUSHFc.cached) cacheLOGBRUSHFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, LOGBRUSHFc.lbHatch, (jint)lpStruct->lbHatch);
	(*env)->SetIntField(env, lpObject, LOGBRUSHFc.lbColor, (jint)lpStruct->lbColor);
	(*env)->SetIntField(env, lpObject, LOGBRUSHFc.lbStyle, (jint)lpStruct->lbStyle);
}
#endif

#ifndef NO_LOGFONT
typedef struct LOGFONT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lfPitchAndFamily, lfQuality, lfClipPrecision, lfOutPrecision, lfCharSet, lfStrikeOut, lfUnderline, lfItalic, lfWeight, lfOrientation, lfEscapement, lfWidth, lfHeight;
} LOGFONT_FID_CACHE;

LOGFONT_FID_CACHE LOGFONTFc;

void cacheLOGFONTFields(JNIEnv *env, jobject lpObject)
{
	if (LOGFONTFc.cached) return;
	LOGFONTFc.clazz = (*env)->GetObjectClass(env, lpObject);
	LOGFONTFc.lfPitchAndFamily = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfPitchAndFamily", "B");
	LOGFONTFc.lfQuality = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfQuality", "B");
	LOGFONTFc.lfClipPrecision = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfClipPrecision", "B");
	LOGFONTFc.lfOutPrecision = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfOutPrecision", "B");
	LOGFONTFc.lfCharSet = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfCharSet", "B");
	LOGFONTFc.lfStrikeOut = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfStrikeOut", "B");
	LOGFONTFc.lfUnderline = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfUnderline", "B");
	LOGFONTFc.lfItalic = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfItalic", "B");
	LOGFONTFc.lfWeight = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfWeight", "I");
	LOGFONTFc.lfOrientation = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfOrientation", "I");
	LOGFONTFc.lfEscapement = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfEscapement", "I");
	LOGFONTFc.lfWidth = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfWidth", "I");
	LOGFONTFc.lfHeight = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfHeight", "I");
	LOGFONTFc.cached = 1;
}

LOGFONT *getLOGFONTFields(JNIEnv *env, jobject lpObject, LOGFONT *lpStruct)
{
	if (!LOGFONTFc.cached) cacheLOGFONTFields(env, lpObject);
	lpStruct->lfPitchAndFamily = (*env)->GetByteField(env, lpObject, LOGFONTFc.lfPitchAndFamily);
	lpStruct->lfQuality = (*env)->GetByteField(env, lpObject, LOGFONTFc.lfQuality);
	lpStruct->lfClipPrecision = (*env)->GetByteField(env, lpObject, LOGFONTFc.lfClipPrecision);
	lpStruct->lfOutPrecision = (*env)->GetByteField(env, lpObject, LOGFONTFc.lfOutPrecision);
	lpStruct->lfCharSet = (*env)->GetByteField(env, lpObject, LOGFONTFc.lfCharSet);
	lpStruct->lfStrikeOut = (*env)->GetByteField(env, lpObject, LOGFONTFc.lfStrikeOut);
	lpStruct->lfUnderline = (*env)->GetByteField(env, lpObject, LOGFONTFc.lfUnderline);
	lpStruct->lfItalic = (*env)->GetByteField(env, lpObject, LOGFONTFc.lfItalic);
	lpStruct->lfWeight = (*env)->GetIntField(env, lpObject, LOGFONTFc.lfWeight);
	lpStruct->lfOrientation = (*env)->GetIntField(env, lpObject, LOGFONTFc.lfOrientation);
	lpStruct->lfEscapement = (*env)->GetIntField(env, lpObject, LOGFONTFc.lfEscapement);
	lpStruct->lfWidth = (*env)->GetIntField(env, lpObject, LOGFONTFc.lfWidth);
	lpStruct->lfHeight = (*env)->GetIntField(env, lpObject, LOGFONTFc.lfHeight);
	return lpStruct;
}

void setLOGFONTFields(JNIEnv *env, jobject lpObject, LOGFONT *lpStruct)
{
	if (!LOGFONTFc.cached) cacheLOGFONTFields(env, lpObject);
	(*env)->SetByteField(env, lpObject, LOGFONTFc.lfPitchAndFamily, (jbyte)lpStruct->lfPitchAndFamily);
	(*env)->SetByteField(env, lpObject, LOGFONTFc.lfQuality, (jbyte)lpStruct->lfQuality);
	(*env)->SetByteField(env, lpObject, LOGFONTFc.lfClipPrecision, (jbyte)lpStruct->lfClipPrecision);
	(*env)->SetByteField(env, lpObject, LOGFONTFc.lfOutPrecision, (jbyte)lpStruct->lfOutPrecision);
	(*env)->SetByteField(env, lpObject, LOGFONTFc.lfCharSet, (jbyte)lpStruct->lfCharSet);
	(*env)->SetByteField(env, lpObject, LOGFONTFc.lfStrikeOut, (jbyte)lpStruct->lfStrikeOut);
	(*env)->SetByteField(env, lpObject, LOGFONTFc.lfUnderline, (jbyte)lpStruct->lfUnderline);
	(*env)->SetByteField(env, lpObject, LOGFONTFc.lfItalic, (jbyte)lpStruct->lfItalic);
	(*env)->SetIntField(env, lpObject, LOGFONTFc.lfWeight, (jint)lpStruct->lfWeight);
	(*env)->SetIntField(env, lpObject, LOGFONTFc.lfOrientation, (jint)lpStruct->lfOrientation);
	(*env)->SetIntField(env, lpObject, LOGFONTFc.lfEscapement, (jint)lpStruct->lfEscapement);
	(*env)->SetIntField(env, lpObject, LOGFONTFc.lfWidth, (jint)lpStruct->lfWidth);
	(*env)->SetIntField(env, lpObject, LOGFONTFc.lfHeight, (jint)lpStruct->lfHeight);
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
	getLOGFONTFields(env, lpObject, (LOGFONT *)lpStruct);
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, LOGFONTAFc.lfFaceName);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->lfFaceName), (void *)lpStruct->lfFaceName);
	}
	return lpStruct;
}

void setLOGFONTAFields(JNIEnv *env, jobject lpObject, LOGFONTA *lpStruct)
{
	if (!LOGFONTAFc.cached) cacheLOGFONTAFields(env, lpObject);
	setLOGFONTFields(env, lpObject, (LOGFONT *)lpStruct);
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, LOGFONTAFc.lfFaceName);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->lfFaceName), (void *)lpStruct->lfFaceName);
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
	getLOGFONTFields(env, lpObject, (LOGFONT *)lpStruct);
	{
	jcharArray lpObject1 = (*env)->GetObjectField(env, lpObject, LOGFONTWFc.lfFaceName);
	(*env)->GetCharArrayRegion(env, lpObject1, 0, sizeof(lpStruct->lfFaceName) / 2, (void *)lpStruct->lfFaceName);
	}
	return lpStruct;
}

void setLOGFONTWFields(JNIEnv *env, jobject lpObject, LOGFONTW *lpStruct)
{
	if (!LOGFONTWFc.cached) cacheLOGFONTWFields(env, lpObject);
	setLOGFONTFields(env, lpObject, (LOGFONT *)lpStruct);
	{
	jcharArray lpObject1 = (*env)->GetObjectField(env, lpObject, LOGFONTWFc.lfFaceName);
	(*env)->SetCharArrayRegion(env, lpObject1, 0, sizeof(lpStruct->lfFaceName) / 2, (void *)lpStruct->lfFaceName);
	}
}
#endif

#ifndef NO_LOGPEN
typedef struct LOGPEN_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lopnColor, y, x, lopnStyle;
} LOGPEN_FID_CACHE;

LOGPEN_FID_CACHE LOGPENFc;

void cacheLOGPENFields(JNIEnv *env, jobject lpObject)
{
	if (LOGPENFc.cached) return;
	LOGPENFc.clazz = (*env)->GetObjectClass(env, lpObject);
	LOGPENFc.lopnColor = (*env)->GetFieldID(env, LOGPENFc.clazz, "lopnColor", "I");
	LOGPENFc.y = (*env)->GetFieldID(env, LOGPENFc.clazz, "y", "I");
	LOGPENFc.x = (*env)->GetFieldID(env, LOGPENFc.clazz, "x", "I");
	LOGPENFc.lopnStyle = (*env)->GetFieldID(env, LOGPENFc.clazz, "lopnStyle", "I");
	LOGPENFc.cached = 1;
}

LOGPEN *getLOGPENFields(JNIEnv *env, jobject lpObject, LOGPEN *lpStruct)
{
	if (!LOGPENFc.cached) cacheLOGPENFields(env, lpObject);
	lpStruct->lopnColor = (*env)->GetIntField(env, lpObject, LOGPENFc.lopnColor);
	lpStruct->lopnWidth.y = (*env)->GetIntField(env, lpObject, LOGPENFc.y);
	lpStruct->lopnWidth.x = (*env)->GetIntField(env, lpObject, LOGPENFc.x);
	lpStruct->lopnStyle = (*env)->GetIntField(env, lpObject, LOGPENFc.lopnStyle);
	return lpStruct;
}

void setLOGPENFields(JNIEnv *env, jobject lpObject, LOGPEN *lpStruct)
{
	if (!LOGPENFc.cached) cacheLOGPENFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, LOGPENFc.lopnColor, (jint)lpStruct->lopnColor);
	(*env)->SetIntField(env, lpObject, LOGPENFc.y, (jint)lpStruct->lopnWidth.y);
	(*env)->SetIntField(env, lpObject, LOGPENFc.x, (jint)lpStruct->lopnWidth.x);
	(*env)->SetIntField(env, lpObject, LOGPENFc.lopnStyle, (jint)lpStruct->lopnStyle);
}
#endif

#ifndef NO_LVCOLUMN
typedef struct LVCOLUMN_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID iOrder, iImage, iSubItem, cchTextMax, pszText, cx, fmt, mask;
} LVCOLUMN_FID_CACHE;

LVCOLUMN_FID_CACHE LVCOLUMNFc;

void cacheLVCOLUMNFields(JNIEnv *env, jobject lpObject)
{
	if (LVCOLUMNFc.cached) return;
	LVCOLUMNFc.clazz = (*env)->GetObjectClass(env, lpObject);
	LVCOLUMNFc.iOrder = (*env)->GetFieldID(env, LVCOLUMNFc.clazz, "iOrder", "I");
	LVCOLUMNFc.iImage = (*env)->GetFieldID(env, LVCOLUMNFc.clazz, "iImage", "I");
	LVCOLUMNFc.iSubItem = (*env)->GetFieldID(env, LVCOLUMNFc.clazz, "iSubItem", "I");
	LVCOLUMNFc.cchTextMax = (*env)->GetFieldID(env, LVCOLUMNFc.clazz, "cchTextMax", "I");
	LVCOLUMNFc.pszText = (*env)->GetFieldID(env, LVCOLUMNFc.clazz, "pszText", "I");
	LVCOLUMNFc.cx = (*env)->GetFieldID(env, LVCOLUMNFc.clazz, "cx", "I");
	LVCOLUMNFc.fmt = (*env)->GetFieldID(env, LVCOLUMNFc.clazz, "fmt", "I");
	LVCOLUMNFc.mask = (*env)->GetFieldID(env, LVCOLUMNFc.clazz, "mask", "I");
	LVCOLUMNFc.cached = 1;
}

LVCOLUMN *getLVCOLUMNFields(JNIEnv *env, jobject lpObject, LVCOLUMN *lpStruct)
{
	if (!LVCOLUMNFc.cached) cacheLVCOLUMNFields(env, lpObject);
	lpStruct->iOrder = (*env)->GetIntField(env, lpObject, LVCOLUMNFc.iOrder);
	lpStruct->iImage = (*env)->GetIntField(env, lpObject, LVCOLUMNFc.iImage);
	lpStruct->iSubItem = (*env)->GetIntField(env, lpObject, LVCOLUMNFc.iSubItem);
	lpStruct->cchTextMax = (*env)->GetIntField(env, lpObject, LVCOLUMNFc.cchTextMax);
	lpStruct->pszText = (LPTSTR)(*env)->GetIntField(env, lpObject, LVCOLUMNFc.pszText);
	lpStruct->cx = (*env)->GetIntField(env, lpObject, LVCOLUMNFc.cx);
	lpStruct->fmt = (*env)->GetIntField(env, lpObject, LVCOLUMNFc.fmt);
	lpStruct->mask = (*env)->GetIntField(env, lpObject, LVCOLUMNFc.mask);
	return lpStruct;
}

void setLVCOLUMNFields(JNIEnv *env, jobject lpObject, LVCOLUMN *lpStruct)
{
	if (!LVCOLUMNFc.cached) cacheLVCOLUMNFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, LVCOLUMNFc.iOrder, (jint)lpStruct->iOrder);
	(*env)->SetIntField(env, lpObject, LVCOLUMNFc.iImage, (jint)lpStruct->iImage);
	(*env)->SetIntField(env, lpObject, LVCOLUMNFc.iSubItem, (jint)lpStruct->iSubItem);
	(*env)->SetIntField(env, lpObject, LVCOLUMNFc.cchTextMax, (jint)lpStruct->cchTextMax);
	(*env)->SetIntField(env, lpObject, LVCOLUMNFc.pszText, (jint)lpStruct->pszText);
	(*env)->SetIntField(env, lpObject, LVCOLUMNFc.cx, (jint)lpStruct->cx);
	(*env)->SetIntField(env, lpObject, LVCOLUMNFc.fmt, (jint)lpStruct->fmt);
	(*env)->SetIntField(env, lpObject, LVCOLUMNFc.mask, (jint)lpStruct->mask);
}
#endif

#ifndef NO_LVHITTESTINFO
typedef struct LVHITTESTINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID iSubItem, iItem, flags, y, x;
} LVHITTESTINFO_FID_CACHE;

LVHITTESTINFO_FID_CACHE LVHITTESTINFOFc;

void cacheLVHITTESTINFOFields(JNIEnv *env, jobject lpObject)
{
	if (LVHITTESTINFOFc.cached) return;
	LVHITTESTINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	LVHITTESTINFOFc.iSubItem = (*env)->GetFieldID(env, LVHITTESTINFOFc.clazz, "iSubItem", "I");
	LVHITTESTINFOFc.iItem = (*env)->GetFieldID(env, LVHITTESTINFOFc.clazz, "iItem", "I");
	LVHITTESTINFOFc.flags = (*env)->GetFieldID(env, LVHITTESTINFOFc.clazz, "flags", "I");
	LVHITTESTINFOFc.y = (*env)->GetFieldID(env, LVHITTESTINFOFc.clazz, "y", "I");
	LVHITTESTINFOFc.x = (*env)->GetFieldID(env, LVHITTESTINFOFc.clazz, "x", "I");
	LVHITTESTINFOFc.cached = 1;
}

LVHITTESTINFO *getLVHITTESTINFOFields(JNIEnv *env, jobject lpObject, LVHITTESTINFO *lpStruct)
{
	if (!LVHITTESTINFOFc.cached) cacheLVHITTESTINFOFields(env, lpObject);
	lpStruct->iSubItem = (*env)->GetIntField(env, lpObject, LVHITTESTINFOFc.iSubItem);
	lpStruct->iItem = (*env)->GetIntField(env, lpObject, LVHITTESTINFOFc.iItem);
	lpStruct->flags = (*env)->GetIntField(env, lpObject, LVHITTESTINFOFc.flags);
	lpStruct->pt.y = (*env)->GetIntField(env, lpObject, LVHITTESTINFOFc.y);
	lpStruct->pt.x = (*env)->GetIntField(env, lpObject, LVHITTESTINFOFc.x);
	return lpStruct;
}

void setLVHITTESTINFOFields(JNIEnv *env, jobject lpObject, LVHITTESTINFO *lpStruct)
{
	if (!LVHITTESTINFOFc.cached) cacheLVHITTESTINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, LVHITTESTINFOFc.iSubItem, (jint)lpStruct->iSubItem);
	(*env)->SetIntField(env, lpObject, LVHITTESTINFOFc.iItem, (jint)lpStruct->iItem);
	(*env)->SetIntField(env, lpObject, LVHITTESTINFOFc.flags, (jint)lpStruct->flags);
	(*env)->SetIntField(env, lpObject, LVHITTESTINFOFc.y, (jint)lpStruct->pt.y);
	(*env)->SetIntField(env, lpObject, LVHITTESTINFOFc.x, (jint)lpStruct->pt.x);
}
#endif

#ifndef NO_LVITEM
typedef struct LVITEM_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID iIndent, lParam, iImage, cchTextMax, pszText, stateMask, state, iSubItem, iItem, mask;
} LVITEM_FID_CACHE;

LVITEM_FID_CACHE LVITEMFc;

void cacheLVITEMFields(JNIEnv *env, jobject lpObject)
{
	if (LVITEMFc.cached) return;
	LVITEMFc.clazz = (*env)->GetObjectClass(env, lpObject);
	LVITEMFc.iIndent = (*env)->GetFieldID(env, LVITEMFc.clazz, "iIndent", "I");
	LVITEMFc.lParam = (*env)->GetFieldID(env, LVITEMFc.clazz, "lParam", "I");
	LVITEMFc.iImage = (*env)->GetFieldID(env, LVITEMFc.clazz, "iImage", "I");
	LVITEMFc.cchTextMax = (*env)->GetFieldID(env, LVITEMFc.clazz, "cchTextMax", "I");
	LVITEMFc.pszText = (*env)->GetFieldID(env, LVITEMFc.clazz, "pszText", "I");
	LVITEMFc.stateMask = (*env)->GetFieldID(env, LVITEMFc.clazz, "stateMask", "I");
	LVITEMFc.state = (*env)->GetFieldID(env, LVITEMFc.clazz, "state", "I");
	LVITEMFc.iSubItem = (*env)->GetFieldID(env, LVITEMFc.clazz, "iSubItem", "I");
	LVITEMFc.iItem = (*env)->GetFieldID(env, LVITEMFc.clazz, "iItem", "I");
	LVITEMFc.mask = (*env)->GetFieldID(env, LVITEMFc.clazz, "mask", "I");
	LVITEMFc.cached = 1;
}

LVITEM *getLVITEMFields(JNIEnv *env, jobject lpObject, LVITEM *lpStruct)
{
	if (!LVITEMFc.cached) cacheLVITEMFields(env, lpObject);
	lpStruct->iIndent = (*env)->GetIntField(env, lpObject, LVITEMFc.iIndent);
	lpStruct->lParam = (*env)->GetIntField(env, lpObject, LVITEMFc.lParam);
	lpStruct->iImage = (*env)->GetIntField(env, lpObject, LVITEMFc.iImage);
	lpStruct->cchTextMax = (*env)->GetIntField(env, lpObject, LVITEMFc.cchTextMax);
	lpStruct->pszText = (LPTSTR)(*env)->GetIntField(env, lpObject, LVITEMFc.pszText);
	lpStruct->stateMask = (*env)->GetIntField(env, lpObject, LVITEMFc.stateMask);
	lpStruct->state = (*env)->GetIntField(env, lpObject, LVITEMFc.state);
	lpStruct->iSubItem = (*env)->GetIntField(env, lpObject, LVITEMFc.iSubItem);
	lpStruct->iItem = (*env)->GetIntField(env, lpObject, LVITEMFc.iItem);
	lpStruct->mask = (*env)->GetIntField(env, lpObject, LVITEMFc.mask);
	return lpStruct;
}

void setLVITEMFields(JNIEnv *env, jobject lpObject, LVITEM *lpStruct)
{
	if (!LVITEMFc.cached) cacheLVITEMFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, LVITEMFc.iIndent, (jint)lpStruct->iIndent);
	(*env)->SetIntField(env, lpObject, LVITEMFc.lParam, (jint)lpStruct->lParam);
	(*env)->SetIntField(env, lpObject, LVITEMFc.iImage, (jint)lpStruct->iImage);
	(*env)->SetIntField(env, lpObject, LVITEMFc.cchTextMax, (jint)lpStruct->cchTextMax);
	(*env)->SetIntField(env, lpObject, LVITEMFc.pszText, (jint)lpStruct->pszText);
	(*env)->SetIntField(env, lpObject, LVITEMFc.stateMask, (jint)lpStruct->stateMask);
	(*env)->SetIntField(env, lpObject, LVITEMFc.state, (jint)lpStruct->state);
	(*env)->SetIntField(env, lpObject, LVITEMFc.iSubItem, (jint)lpStruct->iSubItem);
	(*env)->SetIntField(env, lpObject, LVITEMFc.iItem, (jint)lpStruct->iItem);
	(*env)->SetIntField(env, lpObject, LVITEMFc.mask, (jint)lpStruct->mask);
}
#endif

#ifndef NO_MEASUREITEMSTRUCT
typedef struct MEASUREITEMSTRUCT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID itemData, itemHeight, itemWidth, itemID, CtlID, CtlType;
} MEASUREITEMSTRUCT_FID_CACHE;

MEASUREITEMSTRUCT_FID_CACHE MEASUREITEMSTRUCTFc;

void cacheMEASUREITEMSTRUCTFields(JNIEnv *env, jobject lpObject)
{
	if (MEASUREITEMSTRUCTFc.cached) return;
	MEASUREITEMSTRUCTFc.clazz = (*env)->GetObjectClass(env, lpObject);
	MEASUREITEMSTRUCTFc.itemData = (*env)->GetFieldID(env, MEASUREITEMSTRUCTFc.clazz, "itemData", "I");
	MEASUREITEMSTRUCTFc.itemHeight = (*env)->GetFieldID(env, MEASUREITEMSTRUCTFc.clazz, "itemHeight", "I");
	MEASUREITEMSTRUCTFc.itemWidth = (*env)->GetFieldID(env, MEASUREITEMSTRUCTFc.clazz, "itemWidth", "I");
	MEASUREITEMSTRUCTFc.itemID = (*env)->GetFieldID(env, MEASUREITEMSTRUCTFc.clazz, "itemID", "I");
	MEASUREITEMSTRUCTFc.CtlID = (*env)->GetFieldID(env, MEASUREITEMSTRUCTFc.clazz, "CtlID", "I");
	MEASUREITEMSTRUCTFc.CtlType = (*env)->GetFieldID(env, MEASUREITEMSTRUCTFc.clazz, "CtlType", "I");
	MEASUREITEMSTRUCTFc.cached = 1;
}

MEASUREITEMSTRUCT *getMEASUREITEMSTRUCTFields(JNIEnv *env, jobject lpObject, MEASUREITEMSTRUCT *lpStruct)
{
	if (!MEASUREITEMSTRUCTFc.cached) cacheMEASUREITEMSTRUCTFields(env, lpObject);
	lpStruct->itemData = (*env)->GetIntField(env, lpObject, MEASUREITEMSTRUCTFc.itemData);
	lpStruct->itemHeight = (*env)->GetIntField(env, lpObject, MEASUREITEMSTRUCTFc.itemHeight);
	lpStruct->itemWidth = (*env)->GetIntField(env, lpObject, MEASUREITEMSTRUCTFc.itemWidth);
	lpStruct->itemID = (*env)->GetIntField(env, lpObject, MEASUREITEMSTRUCTFc.itemID);
	lpStruct->CtlID = (*env)->GetIntField(env, lpObject, MEASUREITEMSTRUCTFc.CtlID);
	lpStruct->CtlType = (*env)->GetIntField(env, lpObject, MEASUREITEMSTRUCTFc.CtlType);
	return lpStruct;
}

void setMEASUREITEMSTRUCTFields(JNIEnv *env, jobject lpObject, MEASUREITEMSTRUCT *lpStruct)
{
	if (!MEASUREITEMSTRUCTFc.cached) cacheMEASUREITEMSTRUCTFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, MEASUREITEMSTRUCTFc.itemData, (jint)lpStruct->itemData);
	(*env)->SetIntField(env, lpObject, MEASUREITEMSTRUCTFc.itemHeight, (jint)lpStruct->itemHeight);
	(*env)->SetIntField(env, lpObject, MEASUREITEMSTRUCTFc.itemWidth, (jint)lpStruct->itemWidth);
	(*env)->SetIntField(env, lpObject, MEASUREITEMSTRUCTFc.itemID, (jint)lpStruct->itemID);
	(*env)->SetIntField(env, lpObject, MEASUREITEMSTRUCTFc.CtlID, (jint)lpStruct->CtlID);
	(*env)->SetIntField(env, lpObject, MEASUREITEMSTRUCTFc.CtlType, (jint)lpStruct->CtlType);
}
#endif

#ifndef NO_MENUINFO
typedef struct MENUINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID dwMenuData, dwContextHelpID, hbrBack, cyMax, dwStyle, fMask, cbSize;
} MENUINFO_FID_CACHE;

MENUINFO_FID_CACHE MENUINFOFc;

void cacheMENUINFOFields(JNIEnv *env, jobject lpObject)
{
	if (MENUINFOFc.cached) return;
	MENUINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	MENUINFOFc.dwMenuData = (*env)->GetFieldID(env, MENUINFOFc.clazz, "dwMenuData", "I");
	MENUINFOFc.dwContextHelpID = (*env)->GetFieldID(env, MENUINFOFc.clazz, "dwContextHelpID", "I");
	MENUINFOFc.hbrBack = (*env)->GetFieldID(env, MENUINFOFc.clazz, "hbrBack", "I");
	MENUINFOFc.cyMax = (*env)->GetFieldID(env, MENUINFOFc.clazz, "cyMax", "I");
	MENUINFOFc.dwStyle = (*env)->GetFieldID(env, MENUINFOFc.clazz, "dwStyle", "I");
	MENUINFOFc.fMask = (*env)->GetFieldID(env, MENUINFOFc.clazz, "fMask", "I");
	MENUINFOFc.cbSize = (*env)->GetFieldID(env, MENUINFOFc.clazz, "cbSize", "I");
	MENUINFOFc.cached = 1;
}

MENUINFO *getMENUINFOFields(JNIEnv *env, jobject lpObject, MENUINFO *lpStruct)
{
	if (!MENUINFOFc.cached) cacheMENUINFOFields(env, lpObject);
	lpStruct->dwMenuData = (*env)->GetIntField(env, lpObject, MENUINFOFc.dwMenuData);
	lpStruct->dwContextHelpID = (*env)->GetIntField(env, lpObject, MENUINFOFc.dwContextHelpID);
	lpStruct->hbrBack = (HBRUSH)(*env)->GetIntField(env, lpObject, MENUINFOFc.hbrBack);
	lpStruct->cyMax = (*env)->GetIntField(env, lpObject, MENUINFOFc.cyMax);
	lpStruct->dwStyle = (*env)->GetIntField(env, lpObject, MENUINFOFc.dwStyle);
	lpStruct->fMask = (*env)->GetIntField(env, lpObject, MENUINFOFc.fMask);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, MENUINFOFc.cbSize);
	return lpStruct;
}

void setMENUINFOFields(JNIEnv *env, jobject lpObject, MENUINFO *lpStruct)
{
	if (!MENUINFOFc.cached) cacheMENUINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, MENUINFOFc.dwMenuData, (jint)lpStruct->dwMenuData);
	(*env)->SetIntField(env, lpObject, MENUINFOFc.dwContextHelpID, (jint)lpStruct->dwContextHelpID);
	(*env)->SetIntField(env, lpObject, MENUINFOFc.hbrBack, (jint)lpStruct->hbrBack);
	(*env)->SetIntField(env, lpObject, MENUINFOFc.cyMax, (jint)lpStruct->cyMax);
	(*env)->SetIntField(env, lpObject, MENUINFOFc.dwStyle, (jint)lpStruct->dwStyle);
	(*env)->SetIntField(env, lpObject, MENUINFOFc.fMask, (jint)lpStruct->fMask);
	(*env)->SetIntField(env, lpObject, MENUINFOFc.cbSize, (jint)lpStruct->cbSize);
}
#endif

#ifndef NO_MENUITEMINFO
typedef struct MENUITEMINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID hbmpItem, cch, dwTypeData, dwItemData, hbmpUnchecked, hbmpChecked, hSubMenu, wID, fState, fType, fMask, cbSize;
} MENUITEMINFO_FID_CACHE;

MENUITEMINFO_FID_CACHE MENUITEMINFOFc;

void cacheMENUITEMINFOFields(JNIEnv *env, jobject lpObject)
{
	if (MENUITEMINFOFc.cached) return;
	MENUITEMINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	MENUITEMINFOFc.hbmpItem = (*env)->GetFieldID(env, MENUITEMINFOFc.clazz, "hbmpItem", "I");
	MENUITEMINFOFc.cch = (*env)->GetFieldID(env, MENUITEMINFOFc.clazz, "cch", "I");
	MENUITEMINFOFc.dwTypeData = (*env)->GetFieldID(env, MENUITEMINFOFc.clazz, "dwTypeData", "I");
	MENUITEMINFOFc.dwItemData = (*env)->GetFieldID(env, MENUITEMINFOFc.clazz, "dwItemData", "I");
	MENUITEMINFOFc.hbmpUnchecked = (*env)->GetFieldID(env, MENUITEMINFOFc.clazz, "hbmpUnchecked", "I");
	MENUITEMINFOFc.hbmpChecked = (*env)->GetFieldID(env, MENUITEMINFOFc.clazz, "hbmpChecked", "I");
	MENUITEMINFOFc.hSubMenu = (*env)->GetFieldID(env, MENUITEMINFOFc.clazz, "hSubMenu", "I");
	MENUITEMINFOFc.wID = (*env)->GetFieldID(env, MENUITEMINFOFc.clazz, "wID", "I");
	MENUITEMINFOFc.fState = (*env)->GetFieldID(env, MENUITEMINFOFc.clazz, "fState", "I");
	MENUITEMINFOFc.fType = (*env)->GetFieldID(env, MENUITEMINFOFc.clazz, "fType", "I");
	MENUITEMINFOFc.fMask = (*env)->GetFieldID(env, MENUITEMINFOFc.clazz, "fMask", "I");
	MENUITEMINFOFc.cbSize = (*env)->GetFieldID(env, MENUITEMINFOFc.clazz, "cbSize", "I");
	MENUITEMINFOFc.cached = 1;
}

MENUITEMINFO *getMENUITEMINFOFields(JNIEnv *env, jobject lpObject, MENUITEMINFO *lpStruct)
{
	if (!MENUITEMINFOFc.cached) cacheMENUITEMINFOFields(env, lpObject);
#ifndef _WIN32_WCE
	lpStruct->hbmpItem = (HBITMAP)(*env)->GetIntField(env, lpObject, MENUITEMINFOFc.hbmpItem);
#endif
	lpStruct->cch = (*env)->GetIntField(env, lpObject, MENUITEMINFOFc.cch);
	lpStruct->dwTypeData = (LPTSTR)(*env)->GetIntField(env, lpObject, MENUITEMINFOFc.dwTypeData);
	lpStruct->dwItemData = (*env)->GetIntField(env, lpObject, MENUITEMINFOFc.dwItemData);
	lpStruct->hbmpUnchecked = (HBITMAP)(*env)->GetIntField(env, lpObject, MENUITEMINFOFc.hbmpUnchecked);
	lpStruct->hbmpChecked = (HBITMAP)(*env)->GetIntField(env, lpObject, MENUITEMINFOFc.hbmpChecked);
	lpStruct->hSubMenu = (HMENU)(*env)->GetIntField(env, lpObject, MENUITEMINFOFc.hSubMenu);
	lpStruct->wID = (*env)->GetIntField(env, lpObject, MENUITEMINFOFc.wID);
	lpStruct->fState = (*env)->GetIntField(env, lpObject, MENUITEMINFOFc.fState);
	lpStruct->fType = (*env)->GetIntField(env, lpObject, MENUITEMINFOFc.fType);
	lpStruct->fMask = (*env)->GetIntField(env, lpObject, MENUITEMINFOFc.fMask);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, MENUITEMINFOFc.cbSize);
	return lpStruct;
}

void setMENUITEMINFOFields(JNIEnv *env, jobject lpObject, MENUITEMINFO *lpStruct)
{
	if (!MENUITEMINFOFc.cached) cacheMENUITEMINFOFields(env, lpObject);
#ifndef _WIN32_WCE
	(*env)->SetIntField(env, lpObject, MENUITEMINFOFc.hbmpItem, (jint)lpStruct->hbmpItem);
#endif
	(*env)->SetIntField(env, lpObject, MENUITEMINFOFc.cch, (jint)lpStruct->cch);
	(*env)->SetIntField(env, lpObject, MENUITEMINFOFc.dwTypeData, (jint)lpStruct->dwTypeData);
	(*env)->SetIntField(env, lpObject, MENUITEMINFOFc.dwItemData, (jint)lpStruct->dwItemData);
	(*env)->SetIntField(env, lpObject, MENUITEMINFOFc.hbmpUnchecked, (jint)lpStruct->hbmpUnchecked);
	(*env)->SetIntField(env, lpObject, MENUITEMINFOFc.hbmpChecked, (jint)lpStruct->hbmpChecked);
	(*env)->SetIntField(env, lpObject, MENUITEMINFOFc.hSubMenu, (jint)lpStruct->hSubMenu);
	(*env)->SetIntField(env, lpObject, MENUITEMINFOFc.wID, (jint)lpStruct->wID);
	(*env)->SetIntField(env, lpObject, MENUITEMINFOFc.fState, (jint)lpStruct->fState);
	(*env)->SetIntField(env, lpObject, MENUITEMINFOFc.fType, (jint)lpStruct->fType);
	(*env)->SetIntField(env, lpObject, MENUITEMINFOFc.fMask, (jint)lpStruct->fMask);
	(*env)->SetIntField(env, lpObject, MENUITEMINFOFc.cbSize, (jint)lpStruct->cbSize);
}
#endif

#ifndef NO_MONITORINFO
typedef struct MONITORINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID dwFlags, rcWork_bottom, rcWork_right, rcWork_top, rcWork_left, rcMonitor_bottom, rcMonitor_right, rcMonitor_top, rcMonitor_left, cbSize;
} MONITORINFO_FID_CACHE;

MONITORINFO_FID_CACHE MONITORINFOFc;

void cacheMONITORINFOFields(JNIEnv *env, jobject lpObject)
{
	if (MONITORINFOFc.cached) return;
	MONITORINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	MONITORINFOFc.dwFlags = (*env)->GetFieldID(env, MONITORINFOFc.clazz, "dwFlags", "I");
	MONITORINFOFc.rcWork_bottom = (*env)->GetFieldID(env, MONITORINFOFc.clazz, "rcWork_bottom", "I");
	MONITORINFOFc.rcWork_right = (*env)->GetFieldID(env, MONITORINFOFc.clazz, "rcWork_right", "I");
	MONITORINFOFc.rcWork_top = (*env)->GetFieldID(env, MONITORINFOFc.clazz, "rcWork_top", "I");
	MONITORINFOFc.rcWork_left = (*env)->GetFieldID(env, MONITORINFOFc.clazz, "rcWork_left", "I");
	MONITORINFOFc.rcMonitor_bottom = (*env)->GetFieldID(env, MONITORINFOFc.clazz, "rcMonitor_bottom", "I");
	MONITORINFOFc.rcMonitor_right = (*env)->GetFieldID(env, MONITORINFOFc.clazz, "rcMonitor_right", "I");
	MONITORINFOFc.rcMonitor_top = (*env)->GetFieldID(env, MONITORINFOFc.clazz, "rcMonitor_top", "I");
	MONITORINFOFc.rcMonitor_left = (*env)->GetFieldID(env, MONITORINFOFc.clazz, "rcMonitor_left", "I");
	MONITORINFOFc.cbSize = (*env)->GetFieldID(env, MONITORINFOFc.clazz, "cbSize", "I");
	MONITORINFOFc.cached = 1;
}

MONITORINFO *getMONITORINFOFields(JNIEnv *env, jobject lpObject, MONITORINFO *lpStruct)
{
	if (!MONITORINFOFc.cached) cacheMONITORINFOFields(env, lpObject);
	lpStruct->dwFlags = (*env)->GetIntField(env, lpObject, MONITORINFOFc.dwFlags);
	lpStruct->rcWork.bottom = (*env)->GetIntField(env, lpObject, MONITORINFOFc.rcWork_bottom);
	lpStruct->rcWork.right = (*env)->GetIntField(env, lpObject, MONITORINFOFc.rcWork_right);
	lpStruct->rcWork.top = (*env)->GetIntField(env, lpObject, MONITORINFOFc.rcWork_top);
	lpStruct->rcWork.left = (*env)->GetIntField(env, lpObject, MONITORINFOFc.rcWork_left);
	lpStruct->rcMonitor.bottom = (*env)->GetIntField(env, lpObject, MONITORINFOFc.rcMonitor_bottom);
	lpStruct->rcMonitor.right = (*env)->GetIntField(env, lpObject, MONITORINFOFc.rcMonitor_right);
	lpStruct->rcMonitor.top = (*env)->GetIntField(env, lpObject, MONITORINFOFc.rcMonitor_top);
	lpStruct->rcMonitor.left = (*env)->GetIntField(env, lpObject, MONITORINFOFc.rcMonitor_left);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, MONITORINFOFc.cbSize);
	return lpStruct;
}

void setMONITORINFOFields(JNIEnv *env, jobject lpObject, MONITORINFO *lpStruct)
{
	if (!MONITORINFOFc.cached) cacheMONITORINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, MONITORINFOFc.dwFlags, (jint)lpStruct->dwFlags);
	(*env)->SetIntField(env, lpObject, MONITORINFOFc.rcWork_bottom, (jint)lpStruct->rcWork.bottom);
	(*env)->SetIntField(env, lpObject, MONITORINFOFc.rcWork_right, (jint)lpStruct->rcWork.right);
	(*env)->SetIntField(env, lpObject, MONITORINFOFc.rcWork_top, (jint)lpStruct->rcWork.top);
	(*env)->SetIntField(env, lpObject, MONITORINFOFc.rcWork_left, (jint)lpStruct->rcWork.left);
	(*env)->SetIntField(env, lpObject, MONITORINFOFc.rcMonitor_bottom, (jint)lpStruct->rcMonitor.bottom);
	(*env)->SetIntField(env, lpObject, MONITORINFOFc.rcMonitor_right, (jint)lpStruct->rcMonitor.right);
	(*env)->SetIntField(env, lpObject, MONITORINFOFc.rcMonitor_top, (jint)lpStruct->rcMonitor.top);
	(*env)->SetIntField(env, lpObject, MONITORINFOFc.rcMonitor_left, (jint)lpStruct->rcMonitor.left);
	(*env)->SetIntField(env, lpObject, MONITORINFOFc.cbSize, (jint)lpStruct->cbSize);
}
#endif

#ifndef NO_MSG
typedef struct MSG_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID y, x, time, lParam, wParam, message, hwnd;
} MSG_FID_CACHE;

MSG_FID_CACHE MSGFc;

void cacheMSGFields(JNIEnv *env, jobject lpObject)
{
	if (MSGFc.cached) return;
	MSGFc.clazz = (*env)->GetObjectClass(env, lpObject);
	MSGFc.y = (*env)->GetFieldID(env, MSGFc.clazz, "y", "I");
	MSGFc.x = (*env)->GetFieldID(env, MSGFc.clazz, "x", "I");
	MSGFc.time = (*env)->GetFieldID(env, MSGFc.clazz, "time", "I");
	MSGFc.lParam = (*env)->GetFieldID(env, MSGFc.clazz, "lParam", "I");
	MSGFc.wParam = (*env)->GetFieldID(env, MSGFc.clazz, "wParam", "I");
	MSGFc.message = (*env)->GetFieldID(env, MSGFc.clazz, "message", "I");
	MSGFc.hwnd = (*env)->GetFieldID(env, MSGFc.clazz, "hwnd", "I");
	MSGFc.cached = 1;
}

MSG *getMSGFields(JNIEnv *env, jobject lpObject, MSG *lpStruct)
{
	if (!MSGFc.cached) cacheMSGFields(env, lpObject);
	lpStruct->pt.y = (*env)->GetIntField(env, lpObject, MSGFc.y);
	lpStruct->pt.x = (*env)->GetIntField(env, lpObject, MSGFc.x);
	lpStruct->time = (*env)->GetIntField(env, lpObject, MSGFc.time);
	lpStruct->lParam = (*env)->GetIntField(env, lpObject, MSGFc.lParam);
	lpStruct->wParam = (*env)->GetIntField(env, lpObject, MSGFc.wParam);
	lpStruct->message = (*env)->GetIntField(env, lpObject, MSGFc.message);
	lpStruct->hwnd = (HWND)(*env)->GetIntField(env, lpObject, MSGFc.hwnd);
	return lpStruct;
}

void setMSGFields(JNIEnv *env, jobject lpObject, MSG *lpStruct)
{
	if (!MSGFc.cached) cacheMSGFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, MSGFc.y, (jint)lpStruct->pt.y);
	(*env)->SetIntField(env, lpObject, MSGFc.x, (jint)lpStruct->pt.x);
	(*env)->SetIntField(env, lpObject, MSGFc.time, (jint)lpStruct->time);
	(*env)->SetIntField(env, lpObject, MSGFc.lParam, (jint)lpStruct->lParam);
	(*env)->SetIntField(env, lpObject, MSGFc.wParam, (jint)lpStruct->wParam);
	(*env)->SetIntField(env, lpObject, MSGFc.message, (jint)lpStruct->message);
	(*env)->SetIntField(env, lpObject, MSGFc.hwnd, (jint)lpStruct->hwnd);
}
#endif

#ifndef NO_NMHDR
typedef struct NMHDR_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID code, idFrom, hwndFrom;
} NMHDR_FID_CACHE;

NMHDR_FID_CACHE NMHDRFc;

void cacheNMHDRFields(JNIEnv *env, jobject lpObject)
{
	if (NMHDRFc.cached) return;
	NMHDRFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NMHDRFc.code = (*env)->GetFieldID(env, NMHDRFc.clazz, "code", "I");
	NMHDRFc.idFrom = (*env)->GetFieldID(env, NMHDRFc.clazz, "idFrom", "I");
	NMHDRFc.hwndFrom = (*env)->GetFieldID(env, NMHDRFc.clazz, "hwndFrom", "I");
	NMHDRFc.cached = 1;
}

NMHDR *getNMHDRFields(JNIEnv *env, jobject lpObject, NMHDR *lpStruct)
{
	if (!NMHDRFc.cached) cacheNMHDRFields(env, lpObject);
	lpStruct->code = (*env)->GetIntField(env, lpObject, NMHDRFc.code);
	lpStruct->idFrom = (*env)->GetIntField(env, lpObject, NMHDRFc.idFrom);
	lpStruct->hwndFrom = (HWND)(*env)->GetIntField(env, lpObject, NMHDRFc.hwndFrom);
	return lpStruct;
}

void setNMHDRFields(JNIEnv *env, jobject lpObject, NMHDR *lpStruct)
{
	if (!NMHDRFc.cached) cacheNMHDRFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, NMHDRFc.code, (jint)lpStruct->code);
	(*env)->SetIntField(env, lpObject, NMHDRFc.idFrom, (jint)lpStruct->idFrom);
	(*env)->SetIntField(env, lpObject, NMHDRFc.hwndFrom, (jint)lpStruct->hwndFrom);
}
#endif

#ifndef NO_NMCUSTOMDRAW
typedef struct NMCUSTOMDRAW_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lItemlParam, uItemState, dwItemSpec, bottom, right, top, left, hdc, dwDrawStage;
} NMCUSTOMDRAW_FID_CACHE;

NMCUSTOMDRAW_FID_CACHE NMCUSTOMDRAWFc;

void cacheNMCUSTOMDRAWFields(JNIEnv *env, jobject lpObject)
{
	if (NMCUSTOMDRAWFc.cached) return;
	cacheNMHDRFields(env, lpObject);
	NMCUSTOMDRAWFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NMCUSTOMDRAWFc.lItemlParam = (*env)->GetFieldID(env, NMCUSTOMDRAWFc.clazz, "lItemlParam", "I");
	NMCUSTOMDRAWFc.uItemState = (*env)->GetFieldID(env, NMCUSTOMDRAWFc.clazz, "uItemState", "I");
	NMCUSTOMDRAWFc.dwItemSpec = (*env)->GetFieldID(env, NMCUSTOMDRAWFc.clazz, "dwItemSpec", "I");
	NMCUSTOMDRAWFc.bottom = (*env)->GetFieldID(env, NMCUSTOMDRAWFc.clazz, "bottom", "I");
	NMCUSTOMDRAWFc.right = (*env)->GetFieldID(env, NMCUSTOMDRAWFc.clazz, "right", "I");
	NMCUSTOMDRAWFc.top = (*env)->GetFieldID(env, NMCUSTOMDRAWFc.clazz, "top", "I");
	NMCUSTOMDRAWFc.left = (*env)->GetFieldID(env, NMCUSTOMDRAWFc.clazz, "left", "I");
	NMCUSTOMDRAWFc.hdc = (*env)->GetFieldID(env, NMCUSTOMDRAWFc.clazz, "hdc", "I");
	NMCUSTOMDRAWFc.dwDrawStage = (*env)->GetFieldID(env, NMCUSTOMDRAWFc.clazz, "dwDrawStage", "I");
	NMCUSTOMDRAWFc.cached = 1;
}

NMCUSTOMDRAW *getNMCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMCUSTOMDRAW *lpStruct)
{
	if (!NMCUSTOMDRAWFc.cached) cacheNMCUSTOMDRAWFields(env, lpObject);
	getNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	lpStruct->lItemlParam = (*env)->GetIntField(env, lpObject, NMCUSTOMDRAWFc.lItemlParam);
	lpStruct->uItemState = (*env)->GetIntField(env, lpObject, NMCUSTOMDRAWFc.uItemState);
	lpStruct->dwItemSpec = (*env)->GetIntField(env, lpObject, NMCUSTOMDRAWFc.dwItemSpec);
	lpStruct->rc.bottom = (*env)->GetIntField(env, lpObject, NMCUSTOMDRAWFc.bottom);
	lpStruct->rc.right = (*env)->GetIntField(env, lpObject, NMCUSTOMDRAWFc.right);
	lpStruct->rc.top = (*env)->GetIntField(env, lpObject, NMCUSTOMDRAWFc.top);
	lpStruct->rc.left = (*env)->GetIntField(env, lpObject, NMCUSTOMDRAWFc.left);
	lpStruct->hdc = (HDC)(*env)->GetIntField(env, lpObject, NMCUSTOMDRAWFc.hdc);
	lpStruct->dwDrawStage = (*env)->GetIntField(env, lpObject, NMCUSTOMDRAWFc.dwDrawStage);
	return lpStruct;
}

void setNMCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMCUSTOMDRAW *lpStruct)
{
	if (!NMCUSTOMDRAWFc.cached) cacheNMCUSTOMDRAWFields(env, lpObject);
	setNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	(*env)->SetIntField(env, lpObject, NMCUSTOMDRAWFc.lItemlParam, (jint)lpStruct->lItemlParam);
	(*env)->SetIntField(env, lpObject, NMCUSTOMDRAWFc.uItemState, (jint)lpStruct->uItemState);
	(*env)->SetIntField(env, lpObject, NMCUSTOMDRAWFc.dwItemSpec, (jint)lpStruct->dwItemSpec);
	(*env)->SetIntField(env, lpObject, NMCUSTOMDRAWFc.bottom, (jint)lpStruct->rc.bottom);
	(*env)->SetIntField(env, lpObject, NMCUSTOMDRAWFc.right, (jint)lpStruct->rc.right);
	(*env)->SetIntField(env, lpObject, NMCUSTOMDRAWFc.top, (jint)lpStruct->rc.top);
	(*env)->SetIntField(env, lpObject, NMCUSTOMDRAWFc.left, (jint)lpStruct->rc.left);
	(*env)->SetIntField(env, lpObject, NMCUSTOMDRAWFc.hdc, (jint)lpStruct->hdc);
	(*env)->SetIntField(env, lpObject, NMCUSTOMDRAWFc.dwDrawStage, (jint)lpStruct->dwDrawStage);
}
#endif

#ifndef NO_NMHEADER
typedef struct NMHEADER_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID pitem, iButton, iItem;
} NMHEADER_FID_CACHE;

NMHEADER_FID_CACHE NMHEADERFc;

void cacheNMHEADERFields(JNIEnv *env, jobject lpObject)
{
	if (NMHEADERFc.cached) return;
	cacheNMHDRFields(env, lpObject);
	NMHEADERFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NMHEADERFc.pitem = (*env)->GetFieldID(env, NMHEADERFc.clazz, "pitem", "I");
	NMHEADERFc.iButton = (*env)->GetFieldID(env, NMHEADERFc.clazz, "iButton", "I");
	NMHEADERFc.iItem = (*env)->GetFieldID(env, NMHEADERFc.clazz, "iItem", "I");
	NMHEADERFc.cached = 1;
}

NMHEADER *getNMHEADERFields(JNIEnv *env, jobject lpObject, NMHEADER *lpStruct)
{
	if (!NMHEADERFc.cached) cacheNMHEADERFields(env, lpObject);
	getNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	lpStruct->pitem = (HDITEM FAR *)(*env)->GetIntField(env, lpObject, NMHEADERFc.pitem);
	lpStruct->iButton = (*env)->GetIntField(env, lpObject, NMHEADERFc.iButton);
	lpStruct->iItem = (*env)->GetIntField(env, lpObject, NMHEADERFc.iItem);
	return lpStruct;
}

void setNMHEADERFields(JNIEnv *env, jobject lpObject, NMHEADER *lpStruct)
{
	if (!NMHEADERFc.cached) cacheNMHEADERFields(env, lpObject);
	setNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	(*env)->SetIntField(env, lpObject, NMHEADERFc.pitem, (jint)lpStruct->pitem);
	(*env)->SetIntField(env, lpObject, NMHEADERFc.iButton, (jint)lpStruct->iButton);
	(*env)->SetIntField(env, lpObject, NMHEADERFc.iItem, (jint)lpStruct->iItem);
}
#endif

#ifndef NO_NMLISTVIEW
typedef struct NMLISTVIEW_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lParam, y, x, uChanged, uOldState, uNewState, iSubItem, iItem;
} NMLISTVIEW_FID_CACHE;

NMLISTVIEW_FID_CACHE NMLISTVIEWFc;

void cacheNMLISTVIEWFields(JNIEnv *env, jobject lpObject)
{
	if (NMLISTVIEWFc.cached) return;
	cacheNMHDRFields(env, lpObject);
	NMLISTVIEWFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NMLISTVIEWFc.lParam = (*env)->GetFieldID(env, NMLISTVIEWFc.clazz, "lParam", "I");
	NMLISTVIEWFc.y = (*env)->GetFieldID(env, NMLISTVIEWFc.clazz, "y", "I");
	NMLISTVIEWFc.x = (*env)->GetFieldID(env, NMLISTVIEWFc.clazz, "x", "I");
	NMLISTVIEWFc.uChanged = (*env)->GetFieldID(env, NMLISTVIEWFc.clazz, "uChanged", "I");
	NMLISTVIEWFc.uOldState = (*env)->GetFieldID(env, NMLISTVIEWFc.clazz, "uOldState", "I");
	NMLISTVIEWFc.uNewState = (*env)->GetFieldID(env, NMLISTVIEWFc.clazz, "uNewState", "I");
	NMLISTVIEWFc.iSubItem = (*env)->GetFieldID(env, NMLISTVIEWFc.clazz, "iSubItem", "I");
	NMLISTVIEWFc.iItem = (*env)->GetFieldID(env, NMLISTVIEWFc.clazz, "iItem", "I");
	NMLISTVIEWFc.cached = 1;
}

NMLISTVIEW *getNMLISTVIEWFields(JNIEnv *env, jobject lpObject, NMLISTVIEW *lpStruct)
{
	if (!NMLISTVIEWFc.cached) cacheNMLISTVIEWFields(env, lpObject);
	getNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	lpStruct->lParam = (*env)->GetIntField(env, lpObject, NMLISTVIEWFc.lParam);
	lpStruct->ptAction.y = (*env)->GetIntField(env, lpObject, NMLISTVIEWFc.y);
	lpStruct->ptAction.x = (*env)->GetIntField(env, lpObject, NMLISTVIEWFc.x);
	lpStruct->uChanged = (*env)->GetIntField(env, lpObject, NMLISTVIEWFc.uChanged);
	lpStruct->uOldState = (*env)->GetIntField(env, lpObject, NMLISTVIEWFc.uOldState);
	lpStruct->uNewState = (*env)->GetIntField(env, lpObject, NMLISTVIEWFc.uNewState);
	lpStruct->iSubItem = (*env)->GetIntField(env, lpObject, NMLISTVIEWFc.iSubItem);
	lpStruct->iItem = (*env)->GetIntField(env, lpObject, NMLISTVIEWFc.iItem);
	return lpStruct;
}

void setNMLISTVIEWFields(JNIEnv *env, jobject lpObject, NMLISTVIEW *lpStruct)
{
	if (!NMLISTVIEWFc.cached) cacheNMLISTVIEWFields(env, lpObject);
	setNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	(*env)->SetIntField(env, lpObject, NMLISTVIEWFc.lParam, (jint)lpStruct->lParam);
	(*env)->SetIntField(env, lpObject, NMLISTVIEWFc.y, (jint)lpStruct->ptAction.y);
	(*env)->SetIntField(env, lpObject, NMLISTVIEWFc.x, (jint)lpStruct->ptAction.x);
	(*env)->SetIntField(env, lpObject, NMLISTVIEWFc.uChanged, (jint)lpStruct->uChanged);
	(*env)->SetIntField(env, lpObject, NMLISTVIEWFc.uOldState, (jint)lpStruct->uOldState);
	(*env)->SetIntField(env, lpObject, NMLISTVIEWFc.uNewState, (jint)lpStruct->uNewState);
	(*env)->SetIntField(env, lpObject, NMLISTVIEWFc.iSubItem, (jint)lpStruct->iSubItem);
	(*env)->SetIntField(env, lpObject, NMLISTVIEWFc.iItem, (jint)lpStruct->iItem);
}
#endif

#ifndef NO_NMLVCUSTOMDRAW
typedef struct NMLVCUSTOMDRAW_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID iSubItem, clrTextBk, clrText;
} NMLVCUSTOMDRAW_FID_CACHE;

NMLVCUSTOMDRAW_FID_CACHE NMLVCUSTOMDRAWFc;

void cacheNMLVCUSTOMDRAWFields(JNIEnv *env, jobject lpObject)
{
	if (NMLVCUSTOMDRAWFc.cached) return;
	cacheNMCUSTOMDRAWFields(env, lpObject);
	NMLVCUSTOMDRAWFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NMLVCUSTOMDRAWFc.iSubItem = (*env)->GetFieldID(env, NMLVCUSTOMDRAWFc.clazz, "iSubItem", "I");
	NMLVCUSTOMDRAWFc.clrTextBk = (*env)->GetFieldID(env, NMLVCUSTOMDRAWFc.clazz, "clrTextBk", "I");
	NMLVCUSTOMDRAWFc.clrText = (*env)->GetFieldID(env, NMLVCUSTOMDRAWFc.clazz, "clrText", "I");
	NMLVCUSTOMDRAWFc.cached = 1;
}

NMLVCUSTOMDRAW *getNMLVCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMLVCUSTOMDRAW *lpStruct)
{
	if (!NMLVCUSTOMDRAWFc.cached) cacheNMLVCUSTOMDRAWFields(env, lpObject);
	getNMCUSTOMDRAWFields(env, lpObject, (NMCUSTOMDRAW *)lpStruct);
	lpStruct->iSubItem = (*env)->GetIntField(env, lpObject, NMLVCUSTOMDRAWFc.iSubItem);
	lpStruct->clrTextBk = (*env)->GetIntField(env, lpObject, NMLVCUSTOMDRAWFc.clrTextBk);
	lpStruct->clrText = (*env)->GetIntField(env, lpObject, NMLVCUSTOMDRAWFc.clrText);
	return lpStruct;
}

void setNMLVCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMLVCUSTOMDRAW *lpStruct)
{
	if (!NMLVCUSTOMDRAWFc.cached) cacheNMLVCUSTOMDRAWFields(env, lpObject);
	setNMCUSTOMDRAWFields(env, lpObject, (NMCUSTOMDRAW *)lpStruct);
	(*env)->SetIntField(env, lpObject, NMLVCUSTOMDRAWFc.iSubItem, (jint)lpStruct->iSubItem);
	(*env)->SetIntField(env, lpObject, NMLVCUSTOMDRAWFc.clrTextBk, (jint)lpStruct->clrTextBk);
	(*env)->SetIntField(env, lpObject, NMLVCUSTOMDRAWFc.clrText, (jint)lpStruct->clrText);
}
#endif

#ifndef NO_NMREBARCHEVRON
typedef struct NMREBARCHEVRON_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lParamNM, bottom, right, top, left, lParam, wID, uBand;
} NMREBARCHEVRON_FID_CACHE;

NMREBARCHEVRON_FID_CACHE NMREBARCHEVRONFc;

void cacheNMREBARCHEVRONFields(JNIEnv *env, jobject lpObject)
{
	if (NMREBARCHEVRONFc.cached) return;
	cacheNMHDRFields(env, lpObject);
	NMREBARCHEVRONFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NMREBARCHEVRONFc.lParamNM = (*env)->GetFieldID(env, NMREBARCHEVRONFc.clazz, "lParamNM", "I");
	NMREBARCHEVRONFc.bottom = (*env)->GetFieldID(env, NMREBARCHEVRONFc.clazz, "bottom", "I");
	NMREBARCHEVRONFc.right = (*env)->GetFieldID(env, NMREBARCHEVRONFc.clazz, "right", "I");
	NMREBARCHEVRONFc.top = (*env)->GetFieldID(env, NMREBARCHEVRONFc.clazz, "top", "I");
	NMREBARCHEVRONFc.left = (*env)->GetFieldID(env, NMREBARCHEVRONFc.clazz, "left", "I");
	NMREBARCHEVRONFc.lParam = (*env)->GetFieldID(env, NMREBARCHEVRONFc.clazz, "lParam", "I");
	NMREBARCHEVRONFc.wID = (*env)->GetFieldID(env, NMREBARCHEVRONFc.clazz, "wID", "I");
	NMREBARCHEVRONFc.uBand = (*env)->GetFieldID(env, NMREBARCHEVRONFc.clazz, "uBand", "I");
	NMREBARCHEVRONFc.cached = 1;
}

NMREBARCHEVRON *getNMREBARCHEVRONFields(JNIEnv *env, jobject lpObject, NMREBARCHEVRON *lpStruct)
{
	if (!NMREBARCHEVRONFc.cached) cacheNMREBARCHEVRONFields(env, lpObject);
	getNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	lpStruct->lParamNM = (*env)->GetIntField(env, lpObject, NMREBARCHEVRONFc.lParamNM);
	lpStruct->rc.bottom = (*env)->GetIntField(env, lpObject, NMREBARCHEVRONFc.bottom);
	lpStruct->rc.right = (*env)->GetIntField(env, lpObject, NMREBARCHEVRONFc.right);
	lpStruct->rc.top = (*env)->GetIntField(env, lpObject, NMREBARCHEVRONFc.top);
	lpStruct->rc.left = (*env)->GetIntField(env, lpObject, NMREBARCHEVRONFc.left);
	lpStruct->lParam = (*env)->GetIntField(env, lpObject, NMREBARCHEVRONFc.lParam);
	lpStruct->wID = (*env)->GetIntField(env, lpObject, NMREBARCHEVRONFc.wID);
	lpStruct->uBand = (*env)->GetIntField(env, lpObject, NMREBARCHEVRONFc.uBand);
	return lpStruct;
}

void setNMREBARCHEVRONFields(JNIEnv *env, jobject lpObject, NMREBARCHEVRON *lpStruct)
{
	if (!NMREBARCHEVRONFc.cached) cacheNMREBARCHEVRONFields(env, lpObject);
	setNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	(*env)->SetIntField(env, lpObject, NMREBARCHEVRONFc.lParamNM, (jint)lpStruct->lParamNM);
	(*env)->SetIntField(env, lpObject, NMREBARCHEVRONFc.bottom, (jint)lpStruct->rc.bottom);
	(*env)->SetIntField(env, lpObject, NMREBARCHEVRONFc.right, (jint)lpStruct->rc.right);
	(*env)->SetIntField(env, lpObject, NMREBARCHEVRONFc.top, (jint)lpStruct->rc.top);
	(*env)->SetIntField(env, lpObject, NMREBARCHEVRONFc.left, (jint)lpStruct->rc.left);
	(*env)->SetIntField(env, lpObject, NMREBARCHEVRONFc.lParam, (jint)lpStruct->lParam);
	(*env)->SetIntField(env, lpObject, NMREBARCHEVRONFc.wID, (jint)lpStruct->wID);
	(*env)->SetIntField(env, lpObject, NMREBARCHEVRONFc.uBand, (jint)lpStruct->uBand);
}
#endif

#ifndef NO_NMTOOLBAR
typedef struct NMTOOLBAR_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID bottom, right, top, left, pszText, cchText, iString, dwData, fsStyle, fsState, idCommand, iBitmap, iItem;
} NMTOOLBAR_FID_CACHE;

NMTOOLBAR_FID_CACHE NMTOOLBARFc;

void cacheNMTOOLBARFields(JNIEnv *env, jobject lpObject)
{
	if (NMTOOLBARFc.cached) return;
	cacheNMHDRFields(env, lpObject);
	NMTOOLBARFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NMTOOLBARFc.bottom = (*env)->GetFieldID(env, NMTOOLBARFc.clazz, "bottom", "I");
	NMTOOLBARFc.right = (*env)->GetFieldID(env, NMTOOLBARFc.clazz, "right", "I");
	NMTOOLBARFc.top = (*env)->GetFieldID(env, NMTOOLBARFc.clazz, "top", "I");
	NMTOOLBARFc.left = (*env)->GetFieldID(env, NMTOOLBARFc.clazz, "left", "I");
	NMTOOLBARFc.pszText = (*env)->GetFieldID(env, NMTOOLBARFc.clazz, "pszText", "I");
	NMTOOLBARFc.cchText = (*env)->GetFieldID(env, NMTOOLBARFc.clazz, "cchText", "I");
	NMTOOLBARFc.iString = (*env)->GetFieldID(env, NMTOOLBARFc.clazz, "iString", "I");
	NMTOOLBARFc.dwData = (*env)->GetFieldID(env, NMTOOLBARFc.clazz, "dwData", "I");
	NMTOOLBARFc.fsStyle = (*env)->GetFieldID(env, NMTOOLBARFc.clazz, "fsStyle", "B");
	NMTOOLBARFc.fsState = (*env)->GetFieldID(env, NMTOOLBARFc.clazz, "fsState", "B");
	NMTOOLBARFc.idCommand = (*env)->GetFieldID(env, NMTOOLBARFc.clazz, "idCommand", "I");
	NMTOOLBARFc.iBitmap = (*env)->GetFieldID(env, NMTOOLBARFc.clazz, "iBitmap", "I");
	NMTOOLBARFc.iItem = (*env)->GetFieldID(env, NMTOOLBARFc.clazz, "iItem", "I");
	NMTOOLBARFc.cached = 1;
}

NMTOOLBAR *getNMTOOLBARFields(JNIEnv *env, jobject lpObject, NMTOOLBAR *lpStruct)
{
	if (!NMTOOLBARFc.cached) cacheNMTOOLBARFields(env, lpObject);
	getNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
#ifndef _WIN32_WCE
	lpStruct->rcButton.bottom = (*env)->GetIntField(env, lpObject, NMTOOLBARFc.bottom);
#endif
#ifndef _WIN32_WCE
	lpStruct->rcButton.right = (*env)->GetIntField(env, lpObject, NMTOOLBARFc.right);
#endif
#ifndef _WIN32_WCE
	lpStruct->rcButton.top = (*env)->GetIntField(env, lpObject, NMTOOLBARFc.top);
#endif
#ifndef _WIN32_WCE
	lpStruct->rcButton.left = (*env)->GetIntField(env, lpObject, NMTOOLBARFc.left);
#endif
	lpStruct->pszText = (LPTSTR)(*env)->GetIntField(env, lpObject, NMTOOLBARFc.pszText);
	lpStruct->cchText = (*env)->GetIntField(env, lpObject, NMTOOLBARFc.cchText);
	lpStruct->tbButton.iString = (*env)->GetIntField(env, lpObject, NMTOOLBARFc.iString);
	lpStruct->tbButton.dwData = (*env)->GetIntField(env, lpObject, NMTOOLBARFc.dwData);
	lpStruct->tbButton.fsStyle = (*env)->GetByteField(env, lpObject, NMTOOLBARFc.fsStyle);
	lpStruct->tbButton.fsState = (*env)->GetByteField(env, lpObject, NMTOOLBARFc.fsState);
	lpStruct->tbButton.idCommand = (*env)->GetIntField(env, lpObject, NMTOOLBARFc.idCommand);
	lpStruct->tbButton.iBitmap = (*env)->GetIntField(env, lpObject, NMTOOLBARFc.iBitmap);
	lpStruct->iItem = (*env)->GetIntField(env, lpObject, NMTOOLBARFc.iItem);
	return lpStruct;
}

void setNMTOOLBARFields(JNIEnv *env, jobject lpObject, NMTOOLBAR *lpStruct)
{
	if (!NMTOOLBARFc.cached) cacheNMTOOLBARFields(env, lpObject);
	setNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
#ifndef _WIN32_WCE
	(*env)->SetIntField(env, lpObject, NMTOOLBARFc.bottom, (jint)lpStruct->rcButton.bottom);
#endif
#ifndef _WIN32_WCE
	(*env)->SetIntField(env, lpObject, NMTOOLBARFc.right, (jint)lpStruct->rcButton.right);
#endif
#ifndef _WIN32_WCE
	(*env)->SetIntField(env, lpObject, NMTOOLBARFc.top, (jint)lpStruct->rcButton.top);
#endif
#ifndef _WIN32_WCE
	(*env)->SetIntField(env, lpObject, NMTOOLBARFc.left, (jint)lpStruct->rcButton.left);
#endif
	(*env)->SetIntField(env, lpObject, NMTOOLBARFc.pszText, (jint)lpStruct->pszText);
	(*env)->SetIntField(env, lpObject, NMTOOLBARFc.cchText, (jint)lpStruct->cchText);
	(*env)->SetIntField(env, lpObject, NMTOOLBARFc.iString, (jint)lpStruct->tbButton.iString);
	(*env)->SetIntField(env, lpObject, NMTOOLBARFc.dwData, (jint)lpStruct->tbButton.dwData);
	(*env)->SetByteField(env, lpObject, NMTOOLBARFc.fsStyle, (jbyte)lpStruct->tbButton.fsStyle);
	(*env)->SetByteField(env, lpObject, NMTOOLBARFc.fsState, (jbyte)lpStruct->tbButton.fsState);
	(*env)->SetIntField(env, lpObject, NMTOOLBARFc.idCommand, (jint)lpStruct->tbButton.idCommand);
	(*env)->SetIntField(env, lpObject, NMTOOLBARFc.iBitmap, (jint)lpStruct->tbButton.iBitmap);
	(*env)->SetIntField(env, lpObject, NMTOOLBARFc.iItem, (jint)lpStruct->iItem);
}
#endif

#ifndef NO_NMTTDISPINFO
typedef struct NMTTDISPINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lParam, uFlags, hinst, lpszText;
} NMTTDISPINFO_FID_CACHE;

NMTTDISPINFO_FID_CACHE NMTTDISPINFOFc;

void cacheNMTTDISPINFOFields(JNIEnv *env, jobject lpObject)
{
	if (NMTTDISPINFOFc.cached) return;
	cacheNMHDRFields(env, lpObject);
	NMTTDISPINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NMTTDISPINFOFc.lParam = (*env)->GetFieldID(env, NMTTDISPINFOFc.clazz, "lParam", "I");
	NMTTDISPINFOFc.uFlags = (*env)->GetFieldID(env, NMTTDISPINFOFc.clazz, "uFlags", "I");
	NMTTDISPINFOFc.hinst = (*env)->GetFieldID(env, NMTTDISPINFOFc.clazz, "hinst", "I");
	NMTTDISPINFOFc.lpszText = (*env)->GetFieldID(env, NMTTDISPINFOFc.clazz, "lpszText", "I");
	NMTTDISPINFOFc.cached = 1;
}

NMTTDISPINFO *getNMTTDISPINFOFields(JNIEnv *env, jobject lpObject, NMTTDISPINFO *lpStruct)
{
	if (!NMTTDISPINFOFc.cached) cacheNMTTDISPINFOFields(env, lpObject);
	getNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	lpStruct->lParam = (*env)->GetIntField(env, lpObject, NMTTDISPINFOFc.lParam);
	lpStruct->uFlags = (*env)->GetIntField(env, lpObject, NMTTDISPINFOFc.uFlags);
	lpStruct->hinst = (HINSTANCE)(*env)->GetIntField(env, lpObject, NMTTDISPINFOFc.hinst);
	lpStruct->lpszText = (LPTSTR)(*env)->GetIntField(env, lpObject, NMTTDISPINFOFc.lpszText);
	return lpStruct;
}

void setNMTTDISPINFOFields(JNIEnv *env, jobject lpObject, NMTTDISPINFO *lpStruct)
{
	if (!NMTTDISPINFOFc.cached) cacheNMTTDISPINFOFields(env, lpObject);
	setNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	(*env)->SetIntField(env, lpObject, NMTTDISPINFOFc.lParam, (jint)lpStruct->lParam);
	(*env)->SetIntField(env, lpObject, NMTTDISPINFOFc.uFlags, (jint)lpStruct->uFlags);
	(*env)->SetIntField(env, lpObject, NMTTDISPINFOFc.hinst, (jint)lpStruct->hinst);
	(*env)->SetIntField(env, lpObject, NMTTDISPINFOFc.lpszText, (jint)lpStruct->lpszText);
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
	getNMTTDISPINFOFields(env, lpObject, (NMTTDISPINFO *)lpStruct);
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, NMTTDISPINFOAFc.szText);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->szText), (void *)lpStruct->szText);
	}
	return lpStruct;
}

void setNMTTDISPINFOAFields(JNIEnv *env, jobject lpObject, NMTTDISPINFOA *lpStruct)
{
	if (!NMTTDISPINFOAFc.cached) cacheNMTTDISPINFOAFields(env, lpObject);
	setNMTTDISPINFOFields(env, lpObject, (NMTTDISPINFO *)lpStruct);
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, NMTTDISPINFOAFc.szText);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->szText), (void *)lpStruct->szText);
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
	getNMTTDISPINFOFields(env, lpObject, (NMTTDISPINFO *)lpStruct);
	{
	jcharArray lpObject1 = (*env)->GetObjectField(env, lpObject, NMTTDISPINFOWFc.szText);
	(*env)->GetCharArrayRegion(env, lpObject1, 0, sizeof(lpStruct->szText) / 2, (void *)lpStruct->szText);
	}
	return lpStruct;
}

void setNMTTDISPINFOWFields(JNIEnv *env, jobject lpObject, NMTTDISPINFOW *lpStruct)
{
	if (!NMTTDISPINFOWFc.cached) cacheNMTTDISPINFOWFields(env, lpObject);
	setNMTTDISPINFOFields(env, lpObject, (NMTTDISPINFO *)lpStruct);
	{
	jcharArray lpObject1 = (*env)->GetObjectField(env, lpObject, NMTTDISPINFOWFc.szText);
	(*env)->SetCharArrayRegion(env, lpObject1, 0, sizeof(lpStruct->szText) / 2, (void *)lpStruct->szText);
	}
}
#endif

#ifndef NO_NMTVCUSTOMDRAW
typedef struct NMTVCUSTOMDRAW_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID iLevel, clrTextBk, clrText;
} NMTVCUSTOMDRAW_FID_CACHE;

NMTVCUSTOMDRAW_FID_CACHE NMTVCUSTOMDRAWFc;

void cacheNMTVCUSTOMDRAWFields(JNIEnv *env, jobject lpObject)
{
	if (NMTVCUSTOMDRAWFc.cached) return;
	cacheNMCUSTOMDRAWFields(env, lpObject);
	NMTVCUSTOMDRAWFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NMTVCUSTOMDRAWFc.iLevel = (*env)->GetFieldID(env, NMTVCUSTOMDRAWFc.clazz, "iLevel", "I");
	NMTVCUSTOMDRAWFc.clrTextBk = (*env)->GetFieldID(env, NMTVCUSTOMDRAWFc.clazz, "clrTextBk", "I");
	NMTVCUSTOMDRAWFc.clrText = (*env)->GetFieldID(env, NMTVCUSTOMDRAWFc.clazz, "clrText", "I");
	NMTVCUSTOMDRAWFc.cached = 1;
}

NMTVCUSTOMDRAW *getNMTVCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMTVCUSTOMDRAW *lpStruct)
{
	if (!NMTVCUSTOMDRAWFc.cached) cacheNMTVCUSTOMDRAWFields(env, lpObject);
	getNMCUSTOMDRAWFields(env, lpObject, (NMCUSTOMDRAW *)lpStruct);
#ifndef _WIN32_WCE
	lpStruct->iLevel = (*env)->GetIntField(env, lpObject, NMTVCUSTOMDRAWFc.iLevel);
#endif
	lpStruct->clrTextBk = (*env)->GetIntField(env, lpObject, NMTVCUSTOMDRAWFc.clrTextBk);
	lpStruct->clrText = (*env)->GetIntField(env, lpObject, NMTVCUSTOMDRAWFc.clrText);
	return lpStruct;
}

void setNMTVCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMTVCUSTOMDRAW *lpStruct)
{
	if (!NMTVCUSTOMDRAWFc.cached) cacheNMTVCUSTOMDRAWFields(env, lpObject);
	setNMCUSTOMDRAWFields(env, lpObject, (NMCUSTOMDRAW *)lpStruct);
#ifndef _WIN32_WCE
	(*env)->SetIntField(env, lpObject, NMTVCUSTOMDRAWFc.iLevel, (jint)lpStruct->iLevel);
#endif
	(*env)->SetIntField(env, lpObject, NMTVCUSTOMDRAWFc.clrTextBk, (jint)lpStruct->clrTextBk);
	(*env)->SetIntField(env, lpObject, NMTVCUSTOMDRAWFc.clrText, (jint)lpStruct->clrText);
}
#endif

#ifndef NO_NONCLIENTMETRICS
typedef struct NONCLIENTMETRICS_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID iMenuHeight, iMenuWidth, iSmCaptionHeight, iSmCaptionWidth, iCaptionHeight, iCaptionWidth, iScrollHeight, iScrollWidth, iBorderWidth, cbSize;
} NONCLIENTMETRICS_FID_CACHE;

NONCLIENTMETRICS_FID_CACHE NONCLIENTMETRICSFc;

void cacheNONCLIENTMETRICSFields(JNIEnv *env, jobject lpObject)
{
	if (NONCLIENTMETRICSFc.cached) return;
	NONCLIENTMETRICSFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NONCLIENTMETRICSFc.iMenuHeight = (*env)->GetFieldID(env, NONCLIENTMETRICSFc.clazz, "iMenuHeight", "I");
	NONCLIENTMETRICSFc.iMenuWidth = (*env)->GetFieldID(env, NONCLIENTMETRICSFc.clazz, "iMenuWidth", "I");
	NONCLIENTMETRICSFc.iSmCaptionHeight = (*env)->GetFieldID(env, NONCLIENTMETRICSFc.clazz, "iSmCaptionHeight", "I");
	NONCLIENTMETRICSFc.iSmCaptionWidth = (*env)->GetFieldID(env, NONCLIENTMETRICSFc.clazz, "iSmCaptionWidth", "I");
	NONCLIENTMETRICSFc.iCaptionHeight = (*env)->GetFieldID(env, NONCLIENTMETRICSFc.clazz, "iCaptionHeight", "I");
	NONCLIENTMETRICSFc.iCaptionWidth = (*env)->GetFieldID(env, NONCLIENTMETRICSFc.clazz, "iCaptionWidth", "I");
	NONCLIENTMETRICSFc.iScrollHeight = (*env)->GetFieldID(env, NONCLIENTMETRICSFc.clazz, "iScrollHeight", "I");
	NONCLIENTMETRICSFc.iScrollWidth = (*env)->GetFieldID(env, NONCLIENTMETRICSFc.clazz, "iScrollWidth", "I");
	NONCLIENTMETRICSFc.iBorderWidth = (*env)->GetFieldID(env, NONCLIENTMETRICSFc.clazz, "iBorderWidth", "I");
	NONCLIENTMETRICSFc.cbSize = (*env)->GetFieldID(env, NONCLIENTMETRICSFc.clazz, "cbSize", "I");
	NONCLIENTMETRICSFc.cached = 1;
}

NONCLIENTMETRICS *getNONCLIENTMETRICSFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICS *lpStruct)
{
	if (!NONCLIENTMETRICSFc.cached) cacheNONCLIENTMETRICSFields(env, lpObject);
	lpStruct->iMenuHeight = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iMenuHeight);
	lpStruct->iMenuWidth = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iMenuWidth);
	lpStruct->iSmCaptionHeight = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iSmCaptionHeight);
	lpStruct->iSmCaptionWidth = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iSmCaptionWidth);
	lpStruct->iCaptionHeight = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iCaptionHeight);
	lpStruct->iCaptionWidth = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iCaptionWidth);
	lpStruct->iScrollHeight = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iScrollHeight);
	lpStruct->iScrollWidth = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iScrollWidth);
	lpStruct->iBorderWidth = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iBorderWidth);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.cbSize);
	return lpStruct;
}

void setNONCLIENTMETRICSFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICS *lpStruct)
{
	if (!NONCLIENTMETRICSFc.cached) cacheNONCLIENTMETRICSFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iMenuHeight, (jint)lpStruct->iMenuHeight);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iMenuWidth, (jint)lpStruct->iMenuWidth);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iSmCaptionHeight, (jint)lpStruct->iSmCaptionHeight);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iSmCaptionWidth, (jint)lpStruct->iSmCaptionWidth);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iCaptionHeight, (jint)lpStruct->iCaptionHeight);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iCaptionWidth, (jint)lpStruct->iCaptionWidth);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iScrollHeight, (jint)lpStruct->iScrollHeight);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iScrollWidth, (jint)lpStruct->iScrollWidth);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iBorderWidth, (jint)lpStruct->iBorderWidth);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.cbSize, (jint)lpStruct->cbSize);
}
#endif

#ifndef NO_NONCLIENTMETRICSA
typedef struct NONCLIENTMETRICSA_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lfMessageFont, lfStatusFont, lfMenuFont, lfSmCaptionFont, lfCaptionFont;
} NONCLIENTMETRICSA_FID_CACHE;

NONCLIENTMETRICSA_FID_CACHE NONCLIENTMETRICSAFc;

void cacheNONCLIENTMETRICSAFields(JNIEnv *env, jobject lpObject)
{
	if (NONCLIENTMETRICSAFc.cached) return;
	cacheNONCLIENTMETRICSFields(env, lpObject);
	NONCLIENTMETRICSAFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NONCLIENTMETRICSAFc.lfMessageFont = (*env)->GetFieldID(env, NONCLIENTMETRICSAFc.clazz, "lfMessageFont", "Lorg/eclipse/swt/internal/win32/LOGFONTA;");
	NONCLIENTMETRICSAFc.lfStatusFont = (*env)->GetFieldID(env, NONCLIENTMETRICSAFc.clazz, "lfStatusFont", "Lorg/eclipse/swt/internal/win32/LOGFONTA;");
	NONCLIENTMETRICSAFc.lfMenuFont = (*env)->GetFieldID(env, NONCLIENTMETRICSAFc.clazz, "lfMenuFont", "Lorg/eclipse/swt/internal/win32/LOGFONTA;");
	NONCLIENTMETRICSAFc.lfSmCaptionFont = (*env)->GetFieldID(env, NONCLIENTMETRICSAFc.clazz, "lfSmCaptionFont", "Lorg/eclipse/swt/internal/win32/LOGFONTA;");
	NONCLIENTMETRICSAFc.lfCaptionFont = (*env)->GetFieldID(env, NONCLIENTMETRICSAFc.clazz, "lfCaptionFont", "Lorg/eclipse/swt/internal/win32/LOGFONTA;");
	NONCLIENTMETRICSAFc.cached = 1;
}

NONCLIENTMETRICSA *getNONCLIENTMETRICSAFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICSA *lpStruct)
{
	if (!NONCLIENTMETRICSAFc.cached) cacheNONCLIENTMETRICSAFields(env, lpObject);
	getNONCLIENTMETRICSFields(env, lpObject, (NONCLIENTMETRICS *)lpStruct);
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSAFc.lfMessageFont);
	getLOGFONTAFields(env, lpObject1, &lpStruct->lfMessageFont);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSAFc.lfStatusFont);
	getLOGFONTAFields(env, lpObject1, &lpStruct->lfStatusFont);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSAFc.lfMenuFont);
	getLOGFONTAFields(env, lpObject1, &lpStruct->lfMenuFont);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSAFc.lfSmCaptionFont);
	getLOGFONTAFields(env, lpObject1, &lpStruct->lfSmCaptionFont);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSAFc.lfCaptionFont);
	getLOGFONTAFields(env, lpObject1, &lpStruct->lfCaptionFont);
	}
	return lpStruct;
}

void setNONCLIENTMETRICSAFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICSA *lpStruct)
{
	if (!NONCLIENTMETRICSAFc.cached) cacheNONCLIENTMETRICSAFields(env, lpObject);
	setNONCLIENTMETRICSFields(env, lpObject, (NONCLIENTMETRICS *)lpStruct);
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSAFc.lfMessageFont);
	setLOGFONTAFields(env, lpObject1, &lpStruct->lfMessageFont);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSAFc.lfStatusFont);
	setLOGFONTAFields(env, lpObject1, &lpStruct->lfStatusFont);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSAFc.lfMenuFont);
	setLOGFONTAFields(env, lpObject1, &lpStruct->lfMenuFont);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSAFc.lfSmCaptionFont);
	setLOGFONTAFields(env, lpObject1, &lpStruct->lfSmCaptionFont);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSAFc.lfCaptionFont);
	setLOGFONTAFields(env, lpObject1, &lpStruct->lfCaptionFont);
	}
}
#endif

#ifndef NO_NONCLIENTMETRICSW
typedef struct NONCLIENTMETRICSW_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lfMessageFont, lfStatusFont, lfMenuFont, lfSmCaptionFont, lfCaptionFont;
} NONCLIENTMETRICSW_FID_CACHE;

NONCLIENTMETRICSW_FID_CACHE NONCLIENTMETRICSWFc;

void cacheNONCLIENTMETRICSWFields(JNIEnv *env, jobject lpObject)
{
	if (NONCLIENTMETRICSWFc.cached) return;
	cacheNONCLIENTMETRICSFields(env, lpObject);
	NONCLIENTMETRICSWFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NONCLIENTMETRICSWFc.lfMessageFont = (*env)->GetFieldID(env, NONCLIENTMETRICSWFc.clazz, "lfMessageFont", "Lorg/eclipse/swt/internal/win32/LOGFONTW;");
	NONCLIENTMETRICSWFc.lfStatusFont = (*env)->GetFieldID(env, NONCLIENTMETRICSWFc.clazz, "lfStatusFont", "Lorg/eclipse/swt/internal/win32/LOGFONTW;");
	NONCLIENTMETRICSWFc.lfMenuFont = (*env)->GetFieldID(env, NONCLIENTMETRICSWFc.clazz, "lfMenuFont", "Lorg/eclipse/swt/internal/win32/LOGFONTW;");
	NONCLIENTMETRICSWFc.lfSmCaptionFont = (*env)->GetFieldID(env, NONCLIENTMETRICSWFc.clazz, "lfSmCaptionFont", "Lorg/eclipse/swt/internal/win32/LOGFONTW;");
	NONCLIENTMETRICSWFc.lfCaptionFont = (*env)->GetFieldID(env, NONCLIENTMETRICSWFc.clazz, "lfCaptionFont", "Lorg/eclipse/swt/internal/win32/LOGFONTW;");
	NONCLIENTMETRICSWFc.cached = 1;
}

NONCLIENTMETRICSW *getNONCLIENTMETRICSWFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICSW *lpStruct)
{
	if (!NONCLIENTMETRICSWFc.cached) cacheNONCLIENTMETRICSWFields(env, lpObject);
	getNONCLIENTMETRICSFields(env, lpObject, (NONCLIENTMETRICS *)lpStruct);
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSWFc.lfMessageFont);
	getLOGFONTWFields(env, lpObject1, &lpStruct->lfMessageFont);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSWFc.lfStatusFont);
	getLOGFONTWFields(env, lpObject1, &lpStruct->lfStatusFont);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSWFc.lfMenuFont);
	getLOGFONTWFields(env, lpObject1, &lpStruct->lfMenuFont);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSWFc.lfSmCaptionFont);
	getLOGFONTWFields(env, lpObject1, &lpStruct->lfSmCaptionFont);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSWFc.lfCaptionFont);
	getLOGFONTWFields(env, lpObject1, &lpStruct->lfCaptionFont);
	}
	return lpStruct;
}

void setNONCLIENTMETRICSWFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICSW *lpStruct)
{
	if (!NONCLIENTMETRICSWFc.cached) cacheNONCLIENTMETRICSWFields(env, lpObject);
	setNONCLIENTMETRICSFields(env, lpObject, (NONCLIENTMETRICS *)lpStruct);
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSWFc.lfMessageFont);
	setLOGFONTWFields(env, lpObject1, &lpStruct->lfMessageFont);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSWFc.lfStatusFont);
	setLOGFONTWFields(env, lpObject1, &lpStruct->lfStatusFont);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSWFc.lfMenuFont);
	setLOGFONTWFields(env, lpObject1, &lpStruct->lfMenuFont);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSWFc.lfSmCaptionFont);
	setLOGFONTWFields(env, lpObject1, &lpStruct->lfSmCaptionFont);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSWFc.lfCaptionFont);
	setLOGFONTWFields(env, lpObject1, &lpStruct->lfCaptionFont);
	}
}
#endif

#ifndef NO_OPENFILENAME
typedef struct OPENFILENAME_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lpTemplateName, lpfnHook, lCustData, lpstrDefExt, nFileExtension, nFileOffset, Flags, lpstrTitle, lpstrInitialDir, nMaxFileTitle, lpstrFileTitle, nMaxFile, lpstrFile, nFilterIndex, nMaxCustFilter, lpstrCustomFilter, lpstrFilter, hInstance, hwndOwner, lStructSize;
} OPENFILENAME_FID_CACHE;

OPENFILENAME_FID_CACHE OPENFILENAMEFc;

void cacheOPENFILENAMEFields(JNIEnv *env, jobject lpObject)
{
	if (OPENFILENAMEFc.cached) return;
	OPENFILENAMEFc.clazz = (*env)->GetObjectClass(env, lpObject);
	OPENFILENAMEFc.lpTemplateName = (*env)->GetFieldID(env, OPENFILENAMEFc.clazz, "lpTemplateName", "I");
	OPENFILENAMEFc.lpfnHook = (*env)->GetFieldID(env, OPENFILENAMEFc.clazz, "lpfnHook", "I");
	OPENFILENAMEFc.lCustData = (*env)->GetFieldID(env, OPENFILENAMEFc.clazz, "lCustData", "I");
	OPENFILENAMEFc.lpstrDefExt = (*env)->GetFieldID(env, OPENFILENAMEFc.clazz, "lpstrDefExt", "I");
	OPENFILENAMEFc.nFileExtension = (*env)->GetFieldID(env, OPENFILENAMEFc.clazz, "nFileExtension", "S");
	OPENFILENAMEFc.nFileOffset = (*env)->GetFieldID(env, OPENFILENAMEFc.clazz, "nFileOffset", "S");
	OPENFILENAMEFc.Flags = (*env)->GetFieldID(env, OPENFILENAMEFc.clazz, "Flags", "I");
	OPENFILENAMEFc.lpstrTitle = (*env)->GetFieldID(env, OPENFILENAMEFc.clazz, "lpstrTitle", "I");
	OPENFILENAMEFc.lpstrInitialDir = (*env)->GetFieldID(env, OPENFILENAMEFc.clazz, "lpstrInitialDir", "I");
	OPENFILENAMEFc.nMaxFileTitle = (*env)->GetFieldID(env, OPENFILENAMEFc.clazz, "nMaxFileTitle", "I");
	OPENFILENAMEFc.lpstrFileTitle = (*env)->GetFieldID(env, OPENFILENAMEFc.clazz, "lpstrFileTitle", "I");
	OPENFILENAMEFc.nMaxFile = (*env)->GetFieldID(env, OPENFILENAMEFc.clazz, "nMaxFile", "I");
	OPENFILENAMEFc.lpstrFile = (*env)->GetFieldID(env, OPENFILENAMEFc.clazz, "lpstrFile", "I");
	OPENFILENAMEFc.nFilterIndex = (*env)->GetFieldID(env, OPENFILENAMEFc.clazz, "nFilterIndex", "I");
	OPENFILENAMEFc.nMaxCustFilter = (*env)->GetFieldID(env, OPENFILENAMEFc.clazz, "nMaxCustFilter", "I");
	OPENFILENAMEFc.lpstrCustomFilter = (*env)->GetFieldID(env, OPENFILENAMEFc.clazz, "lpstrCustomFilter", "I");
	OPENFILENAMEFc.lpstrFilter = (*env)->GetFieldID(env, OPENFILENAMEFc.clazz, "lpstrFilter", "I");
	OPENFILENAMEFc.hInstance = (*env)->GetFieldID(env, OPENFILENAMEFc.clazz, "hInstance", "I");
	OPENFILENAMEFc.hwndOwner = (*env)->GetFieldID(env, OPENFILENAMEFc.clazz, "hwndOwner", "I");
	OPENFILENAMEFc.lStructSize = (*env)->GetFieldID(env, OPENFILENAMEFc.clazz, "lStructSize", "I");
	OPENFILENAMEFc.cached = 1;
}

OPENFILENAME *getOPENFILENAMEFields(JNIEnv *env, jobject lpObject, OPENFILENAME *lpStruct)
{
	if (!OPENFILENAMEFc.cached) cacheOPENFILENAMEFields(env, lpObject);
	lpStruct->lpTemplateName = (LPCTSTR)(*env)->GetIntField(env, lpObject, OPENFILENAMEFc.lpTemplateName);
	lpStruct->lpfnHook = (LPOFNHOOKPROC)(*env)->GetIntField(env, lpObject, OPENFILENAMEFc.lpfnHook);
	lpStruct->lCustData = (*env)->GetIntField(env, lpObject, OPENFILENAMEFc.lCustData);
	lpStruct->lpstrDefExt = (LPCTSTR)(*env)->GetIntField(env, lpObject, OPENFILENAMEFc.lpstrDefExt);
	lpStruct->nFileExtension = (*env)->GetShortField(env, lpObject, OPENFILENAMEFc.nFileExtension);
	lpStruct->nFileOffset = (*env)->GetShortField(env, lpObject, OPENFILENAMEFc.nFileOffset);
	lpStruct->Flags = (*env)->GetIntField(env, lpObject, OPENFILENAMEFc.Flags);
	lpStruct->lpstrTitle = (LPCTSTR)(*env)->GetIntField(env, lpObject, OPENFILENAMEFc.lpstrTitle);
	lpStruct->lpstrInitialDir = (LPCTSTR)(*env)->GetIntField(env, lpObject, OPENFILENAMEFc.lpstrInitialDir);
	lpStruct->nMaxFileTitle = (*env)->GetIntField(env, lpObject, OPENFILENAMEFc.nMaxFileTitle);
	lpStruct->lpstrFileTitle = (LPTSTR)(*env)->GetIntField(env, lpObject, OPENFILENAMEFc.lpstrFileTitle);
	lpStruct->nMaxFile = (*env)->GetIntField(env, lpObject, OPENFILENAMEFc.nMaxFile);
	lpStruct->lpstrFile = (LPTSTR)(*env)->GetIntField(env, lpObject, OPENFILENAMEFc.lpstrFile);
	lpStruct->nFilterIndex = (*env)->GetIntField(env, lpObject, OPENFILENAMEFc.nFilterIndex);
	lpStruct->nMaxCustFilter = (*env)->GetIntField(env, lpObject, OPENFILENAMEFc.nMaxCustFilter);
	lpStruct->lpstrCustomFilter = (LPTSTR)(*env)->GetIntField(env, lpObject, OPENFILENAMEFc.lpstrCustomFilter);
	lpStruct->lpstrFilter = (LPCTSTR)(*env)->GetIntField(env, lpObject, OPENFILENAMEFc.lpstrFilter);
	lpStruct->hInstance = (HINSTANCE)(*env)->GetIntField(env, lpObject, OPENFILENAMEFc.hInstance);
	lpStruct->hwndOwner = (HWND)(*env)->GetIntField(env, lpObject, OPENFILENAMEFc.hwndOwner);
	lpStruct->lStructSize = (*env)->GetIntField(env, lpObject, OPENFILENAMEFc.lStructSize);
	return lpStruct;
}

void setOPENFILENAMEFields(JNIEnv *env, jobject lpObject, OPENFILENAME *lpStruct)
{
	if (!OPENFILENAMEFc.cached) cacheOPENFILENAMEFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, OPENFILENAMEFc.lpTemplateName, (jint)lpStruct->lpTemplateName);
	(*env)->SetIntField(env, lpObject, OPENFILENAMEFc.lpfnHook, (jint)lpStruct->lpfnHook);
	(*env)->SetIntField(env, lpObject, OPENFILENAMEFc.lCustData, (jint)lpStruct->lCustData);
	(*env)->SetIntField(env, lpObject, OPENFILENAMEFc.lpstrDefExt, (jint)lpStruct->lpstrDefExt);
	(*env)->SetShortField(env, lpObject, OPENFILENAMEFc.nFileExtension, (jshort)lpStruct->nFileExtension);
	(*env)->SetShortField(env, lpObject, OPENFILENAMEFc.nFileOffset, (jshort)lpStruct->nFileOffset);
	(*env)->SetIntField(env, lpObject, OPENFILENAMEFc.Flags, (jint)lpStruct->Flags);
	(*env)->SetIntField(env, lpObject, OPENFILENAMEFc.lpstrTitle, (jint)lpStruct->lpstrTitle);
	(*env)->SetIntField(env, lpObject, OPENFILENAMEFc.lpstrInitialDir, (jint)lpStruct->lpstrInitialDir);
	(*env)->SetIntField(env, lpObject, OPENFILENAMEFc.nMaxFileTitle, (jint)lpStruct->nMaxFileTitle);
	(*env)->SetIntField(env, lpObject, OPENFILENAMEFc.lpstrFileTitle, (jint)lpStruct->lpstrFileTitle);
	(*env)->SetIntField(env, lpObject, OPENFILENAMEFc.nMaxFile, (jint)lpStruct->nMaxFile);
	(*env)->SetIntField(env, lpObject, OPENFILENAMEFc.lpstrFile, (jint)lpStruct->lpstrFile);
	(*env)->SetIntField(env, lpObject, OPENFILENAMEFc.nFilterIndex, (jint)lpStruct->nFilterIndex);
	(*env)->SetIntField(env, lpObject, OPENFILENAMEFc.nMaxCustFilter, (jint)lpStruct->nMaxCustFilter);
	(*env)->SetIntField(env, lpObject, OPENFILENAMEFc.lpstrCustomFilter, (jint)lpStruct->lpstrCustomFilter);
	(*env)->SetIntField(env, lpObject, OPENFILENAMEFc.lpstrFilter, (jint)lpStruct->lpstrFilter);
	(*env)->SetIntField(env, lpObject, OPENFILENAMEFc.hInstance, (jint)lpStruct->hInstance);
	(*env)->SetIntField(env, lpObject, OPENFILENAMEFc.hwndOwner, (jint)lpStruct->hwndOwner);
	(*env)->SetIntField(env, lpObject, OPENFILENAMEFc.lStructSize, (jint)lpStruct->lStructSize);
}
#endif

#ifndef NO_OSVERSIONINFO
typedef struct OSVERSIONINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID dwPlatformId, dwBuildNumber, dwMinorVersion, dwMajorVersion, dwOSVersionInfoSize;
} OSVERSIONINFO_FID_CACHE;

OSVERSIONINFO_FID_CACHE OSVERSIONINFOFc;

void cacheOSVERSIONINFOFields(JNIEnv *env, jobject lpObject)
{
	if (OSVERSIONINFOFc.cached) return;
	OSVERSIONINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	OSVERSIONINFOFc.dwPlatformId = (*env)->GetFieldID(env, OSVERSIONINFOFc.clazz, "dwPlatformId", "I");
	OSVERSIONINFOFc.dwBuildNumber = (*env)->GetFieldID(env, OSVERSIONINFOFc.clazz, "dwBuildNumber", "I");
	OSVERSIONINFOFc.dwMinorVersion = (*env)->GetFieldID(env, OSVERSIONINFOFc.clazz, "dwMinorVersion", "I");
	OSVERSIONINFOFc.dwMajorVersion = (*env)->GetFieldID(env, OSVERSIONINFOFc.clazz, "dwMajorVersion", "I");
	OSVERSIONINFOFc.dwOSVersionInfoSize = (*env)->GetFieldID(env, OSVERSIONINFOFc.clazz, "dwOSVersionInfoSize", "I");
	OSVERSIONINFOFc.cached = 1;
}

OSVERSIONINFO *getOSVERSIONINFOFields(JNIEnv *env, jobject lpObject, OSVERSIONINFO *lpStruct)
{
	if (!OSVERSIONINFOFc.cached) cacheOSVERSIONINFOFields(env, lpObject);
	lpStruct->dwPlatformId = (*env)->GetIntField(env, lpObject, OSVERSIONINFOFc.dwPlatformId);
	lpStruct->dwBuildNumber = (*env)->GetIntField(env, lpObject, OSVERSIONINFOFc.dwBuildNumber);
	lpStruct->dwMinorVersion = (*env)->GetIntField(env, lpObject, OSVERSIONINFOFc.dwMinorVersion);
	lpStruct->dwMajorVersion = (*env)->GetIntField(env, lpObject, OSVERSIONINFOFc.dwMajorVersion);
	lpStruct->dwOSVersionInfoSize = (*env)->GetIntField(env, lpObject, OSVERSIONINFOFc.dwOSVersionInfoSize);
	return lpStruct;
}

void setOSVERSIONINFOFields(JNIEnv *env, jobject lpObject, OSVERSIONINFO *lpStruct)
{
	if (!OSVERSIONINFOFc.cached) cacheOSVERSIONINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, OSVERSIONINFOFc.dwPlatformId, (jint)lpStruct->dwPlatformId);
	(*env)->SetIntField(env, lpObject, OSVERSIONINFOFc.dwBuildNumber, (jint)lpStruct->dwBuildNumber);
	(*env)->SetIntField(env, lpObject, OSVERSIONINFOFc.dwMinorVersion, (jint)lpStruct->dwMinorVersion);
	(*env)->SetIntField(env, lpObject, OSVERSIONINFOFc.dwMajorVersion, (jint)lpStruct->dwMajorVersion);
	(*env)->SetIntField(env, lpObject, OSVERSIONINFOFc.dwOSVersionInfoSize, (jint)lpStruct->dwOSVersionInfoSize);
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
	getOSVERSIONINFOFields(env, lpObject, (OSVERSIONINFO *)lpStruct);
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, OSVERSIONINFOAFc.szCSDVersion);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->szCSDVersion), (void *)lpStruct->szCSDVersion);
	}
	return lpStruct;
}

void setOSVERSIONINFOAFields(JNIEnv *env, jobject lpObject, OSVERSIONINFOA *lpStruct)
{
	if (!OSVERSIONINFOAFc.cached) cacheOSVERSIONINFOAFields(env, lpObject);
	setOSVERSIONINFOFields(env, lpObject, (OSVERSIONINFO *)lpStruct);
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, OSVERSIONINFOAFc.szCSDVersion);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->szCSDVersion), (void *)lpStruct->szCSDVersion);
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
	getOSVERSIONINFOFields(env, lpObject, (OSVERSIONINFO *)lpStruct);
	{
	jcharArray lpObject1 = (*env)->GetObjectField(env, lpObject, OSVERSIONINFOWFc.szCSDVersion);
	(*env)->GetCharArrayRegion(env, lpObject1, 0, sizeof(lpStruct->szCSDVersion) / 2, (void *)lpStruct->szCSDVersion);
	}
	return lpStruct;
}

void setOSVERSIONINFOWFields(JNIEnv *env, jobject lpObject, OSVERSIONINFOW *lpStruct)
{
	if (!OSVERSIONINFOWFc.cached) cacheOSVERSIONINFOWFields(env, lpObject);
	setOSVERSIONINFOFields(env, lpObject, (OSVERSIONINFO *)lpStruct);
	{
	jcharArray lpObject1 = (*env)->GetObjectField(env, lpObject, OSVERSIONINFOWFc.szCSDVersion);
	(*env)->SetCharArrayRegion(env, lpObject1, 0, sizeof(lpStruct->szCSDVersion) / 2, (void *)lpStruct->szCSDVersion);
	}
}
#endif

#ifndef NO_PAINTSTRUCT
typedef struct PAINTSTRUCT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID rgbReserved, fIncUpdate, fRestore, bottom, right, top, left, fErase, hdc;
} PAINTSTRUCT_FID_CACHE;

PAINTSTRUCT_FID_CACHE PAINTSTRUCTFc;

void cachePAINTSTRUCTFields(JNIEnv *env, jobject lpObject)
{
	if (PAINTSTRUCTFc.cached) return;
	PAINTSTRUCTFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PAINTSTRUCTFc.rgbReserved = (*env)->GetFieldID(env, PAINTSTRUCTFc.clazz, "rgbReserved", "[B");
	PAINTSTRUCTFc.fIncUpdate = (*env)->GetFieldID(env, PAINTSTRUCTFc.clazz, "fIncUpdate", "Z");
	PAINTSTRUCTFc.fRestore = (*env)->GetFieldID(env, PAINTSTRUCTFc.clazz, "fRestore", "Z");
	PAINTSTRUCTFc.bottom = (*env)->GetFieldID(env, PAINTSTRUCTFc.clazz, "bottom", "I");
	PAINTSTRUCTFc.right = (*env)->GetFieldID(env, PAINTSTRUCTFc.clazz, "right", "I");
	PAINTSTRUCTFc.top = (*env)->GetFieldID(env, PAINTSTRUCTFc.clazz, "top", "I");
	PAINTSTRUCTFc.left = (*env)->GetFieldID(env, PAINTSTRUCTFc.clazz, "left", "I");
	PAINTSTRUCTFc.fErase = (*env)->GetFieldID(env, PAINTSTRUCTFc.clazz, "fErase", "Z");
	PAINTSTRUCTFc.hdc = (*env)->GetFieldID(env, PAINTSTRUCTFc.clazz, "hdc", "I");
	PAINTSTRUCTFc.cached = 1;
}

PAINTSTRUCT *getPAINTSTRUCTFields(JNIEnv *env, jobject lpObject, PAINTSTRUCT *lpStruct)
{
	if (!PAINTSTRUCTFc.cached) cachePAINTSTRUCTFields(env, lpObject);
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, PAINTSTRUCTFc.rgbReserved);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->rgbReserved), (void *)lpStruct->rgbReserved);
	}
	lpStruct->fIncUpdate = (*env)->GetBooleanField(env, lpObject, PAINTSTRUCTFc.fIncUpdate);
	lpStruct->fRestore = (*env)->GetBooleanField(env, lpObject, PAINTSTRUCTFc.fRestore);
	lpStruct->rcPaint.bottom = (*env)->GetIntField(env, lpObject, PAINTSTRUCTFc.bottom);
	lpStruct->rcPaint.right = (*env)->GetIntField(env, lpObject, PAINTSTRUCTFc.right);
	lpStruct->rcPaint.top = (*env)->GetIntField(env, lpObject, PAINTSTRUCTFc.top);
	lpStruct->rcPaint.left = (*env)->GetIntField(env, lpObject, PAINTSTRUCTFc.left);
	lpStruct->fErase = (*env)->GetBooleanField(env, lpObject, PAINTSTRUCTFc.fErase);
	lpStruct->hdc = (HDC)(*env)->GetIntField(env, lpObject, PAINTSTRUCTFc.hdc);
	return lpStruct;
}

void setPAINTSTRUCTFields(JNIEnv *env, jobject lpObject, PAINTSTRUCT *lpStruct)
{
	if (!PAINTSTRUCTFc.cached) cachePAINTSTRUCTFields(env, lpObject);
	{
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, PAINTSTRUCTFc.rgbReserved);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->rgbReserved), (void *)lpStruct->rgbReserved);
	}
	(*env)->SetBooleanField(env, lpObject, PAINTSTRUCTFc.fIncUpdate, (jboolean)lpStruct->fIncUpdate);
	(*env)->SetBooleanField(env, lpObject, PAINTSTRUCTFc.fRestore, (jboolean)lpStruct->fRestore);
	(*env)->SetIntField(env, lpObject, PAINTSTRUCTFc.bottom, (jint)lpStruct->rcPaint.bottom);
	(*env)->SetIntField(env, lpObject, PAINTSTRUCTFc.right, (jint)lpStruct->rcPaint.right);
	(*env)->SetIntField(env, lpObject, PAINTSTRUCTFc.top, (jint)lpStruct->rcPaint.top);
	(*env)->SetIntField(env, lpObject, PAINTSTRUCTFc.left, (jint)lpStruct->rcPaint.left);
	(*env)->SetBooleanField(env, lpObject, PAINTSTRUCTFc.fErase, (jboolean)lpStruct->fErase);
	(*env)->SetIntField(env, lpObject, PAINTSTRUCTFc.hdc, (jint)lpStruct->hdc);
}
#endif

#ifndef NO_POINT
typedef struct POINT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID y, x;
} POINT_FID_CACHE;

POINT_FID_CACHE POINTFc;

void cachePOINTFields(JNIEnv *env, jobject lpObject)
{
	if (POINTFc.cached) return;
	POINTFc.clazz = (*env)->GetObjectClass(env, lpObject);
	POINTFc.y = (*env)->GetFieldID(env, POINTFc.clazz, "y", "I");
	POINTFc.x = (*env)->GetFieldID(env, POINTFc.clazz, "x", "I");
	POINTFc.cached = 1;
}

POINT *getPOINTFields(JNIEnv *env, jobject lpObject, POINT *lpStruct)
{
	if (!POINTFc.cached) cachePOINTFields(env, lpObject);
	lpStruct->y = (*env)->GetIntField(env, lpObject, POINTFc.y);
	lpStruct->x = (*env)->GetIntField(env, lpObject, POINTFc.x);
	return lpStruct;
}

void setPOINTFields(JNIEnv *env, jobject lpObject, POINT *lpStruct)
{
	if (!POINTFc.cached) cachePOINTFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, POINTFc.y, (jint)lpStruct->y);
	(*env)->SetIntField(env, lpObject, POINTFc.x, (jint)lpStruct->x);
}
#endif

#ifndef NO_PRINTDLG
typedef struct PRINTDLG_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID hSetupTemplate, hPrintTemplate, lpSetupTemplateName, lpPrintTemplateName, lpfnSetupHook, lpfnPrintHook, lCustData, hInstance, nCopies, nMaxPage, nMinPage, nToPage, nFromPage, Flags, hDC, hDevNames, hDevMode, hwndOwner, lStructSize;
} PRINTDLG_FID_CACHE;

PRINTDLG_FID_CACHE PRINTDLGFc;

void cachePRINTDLGFields(JNIEnv *env, jobject lpObject)
{
	if (PRINTDLGFc.cached) return;
	PRINTDLGFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PRINTDLGFc.hSetupTemplate = (*env)->GetFieldID(env, PRINTDLGFc.clazz, "hSetupTemplate", "I");
	PRINTDLGFc.hPrintTemplate = (*env)->GetFieldID(env, PRINTDLGFc.clazz, "hPrintTemplate", "I");
	PRINTDLGFc.lpSetupTemplateName = (*env)->GetFieldID(env, PRINTDLGFc.clazz, "lpSetupTemplateName", "I");
	PRINTDLGFc.lpPrintTemplateName = (*env)->GetFieldID(env, PRINTDLGFc.clazz, "lpPrintTemplateName", "I");
	PRINTDLGFc.lpfnSetupHook = (*env)->GetFieldID(env, PRINTDLGFc.clazz, "lpfnSetupHook", "I");
	PRINTDLGFc.lpfnPrintHook = (*env)->GetFieldID(env, PRINTDLGFc.clazz, "lpfnPrintHook", "I");
	PRINTDLGFc.lCustData = (*env)->GetFieldID(env, PRINTDLGFc.clazz, "lCustData", "I");
	PRINTDLGFc.hInstance = (*env)->GetFieldID(env, PRINTDLGFc.clazz, "hInstance", "I");
	PRINTDLGFc.nCopies = (*env)->GetFieldID(env, PRINTDLGFc.clazz, "nCopies", "S");
	PRINTDLGFc.nMaxPage = (*env)->GetFieldID(env, PRINTDLGFc.clazz, "nMaxPage", "S");
	PRINTDLGFc.nMinPage = (*env)->GetFieldID(env, PRINTDLGFc.clazz, "nMinPage", "S");
	PRINTDLGFc.nToPage = (*env)->GetFieldID(env, PRINTDLGFc.clazz, "nToPage", "S");
	PRINTDLGFc.nFromPage = (*env)->GetFieldID(env, PRINTDLGFc.clazz, "nFromPage", "S");
	PRINTDLGFc.Flags = (*env)->GetFieldID(env, PRINTDLGFc.clazz, "Flags", "I");
	PRINTDLGFc.hDC = (*env)->GetFieldID(env, PRINTDLGFc.clazz, "hDC", "I");
	PRINTDLGFc.hDevNames = (*env)->GetFieldID(env, PRINTDLGFc.clazz, "hDevNames", "I");
	PRINTDLGFc.hDevMode = (*env)->GetFieldID(env, PRINTDLGFc.clazz, "hDevMode", "I");
	PRINTDLGFc.hwndOwner = (*env)->GetFieldID(env, PRINTDLGFc.clazz, "hwndOwner", "I");
	PRINTDLGFc.lStructSize = (*env)->GetFieldID(env, PRINTDLGFc.clazz, "lStructSize", "I");
	PRINTDLGFc.cached = 1;
}

PRINTDLG *getPRINTDLGFields(JNIEnv *env, jobject lpObject, PRINTDLG *lpStruct)
{
	if (!PRINTDLGFc.cached) cachePRINTDLGFields(env, lpObject);
	lpStruct->hSetupTemplate = (HGLOBAL)(*env)->GetIntField(env, lpObject, PRINTDLGFc.hSetupTemplate);
	lpStruct->hPrintTemplate = (HGLOBAL)(*env)->GetIntField(env, lpObject, PRINTDLGFc.hPrintTemplate);
	lpStruct->lpSetupTemplateName = (LPCTSTR)(*env)->GetIntField(env, lpObject, PRINTDLGFc.lpSetupTemplateName);
	lpStruct->lpPrintTemplateName = (LPCTSTR)(*env)->GetIntField(env, lpObject, PRINTDLGFc.lpPrintTemplateName);
	lpStruct->lpfnSetupHook = (LPPRINTHOOKPROC)(*env)->GetIntField(env, lpObject, PRINTDLGFc.lpfnSetupHook);
	lpStruct->lpfnPrintHook = (LPPRINTHOOKPROC)(*env)->GetIntField(env, lpObject, PRINTDLGFc.lpfnPrintHook);
	lpStruct->lCustData = (*env)->GetIntField(env, lpObject, PRINTDLGFc.lCustData);
	lpStruct->hInstance = (HINSTANCE)(*env)->GetIntField(env, lpObject, PRINTDLGFc.hInstance);
	lpStruct->nCopies = (*env)->GetShortField(env, lpObject, PRINTDLGFc.nCopies);
	lpStruct->nMaxPage = (*env)->GetShortField(env, lpObject, PRINTDLGFc.nMaxPage);
	lpStruct->nMinPage = (*env)->GetShortField(env, lpObject, PRINTDLGFc.nMinPage);
	lpStruct->nToPage = (*env)->GetShortField(env, lpObject, PRINTDLGFc.nToPage);
	lpStruct->nFromPage = (*env)->GetShortField(env, lpObject, PRINTDLGFc.nFromPage);
	lpStruct->Flags = (*env)->GetIntField(env, lpObject, PRINTDLGFc.Flags);
	lpStruct->hDC = (HDC)(*env)->GetIntField(env, lpObject, PRINTDLGFc.hDC);
	lpStruct->hDevNames = (HGLOBAL)(*env)->GetIntField(env, lpObject, PRINTDLGFc.hDevNames);
	lpStruct->hDevMode = (HGLOBAL)(*env)->GetIntField(env, lpObject, PRINTDLGFc.hDevMode);
	lpStruct->hwndOwner = (HWND)(*env)->GetIntField(env, lpObject, PRINTDLGFc.hwndOwner);
	lpStruct->lStructSize = (*env)->GetIntField(env, lpObject, PRINTDLGFc.lStructSize);
	return lpStruct;
}

void setPRINTDLGFields(JNIEnv *env, jobject lpObject, PRINTDLG *lpStruct)
{
	if (!PRINTDLGFc.cached) cachePRINTDLGFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, PRINTDLGFc.hSetupTemplate, (jint)lpStruct->hSetupTemplate);
	(*env)->SetIntField(env, lpObject, PRINTDLGFc.hPrintTemplate, (jint)lpStruct->hPrintTemplate);
	(*env)->SetIntField(env, lpObject, PRINTDLGFc.lpSetupTemplateName, (jint)lpStruct->lpSetupTemplateName);
	(*env)->SetIntField(env, lpObject, PRINTDLGFc.lpPrintTemplateName, (jint)lpStruct->lpPrintTemplateName);
	(*env)->SetIntField(env, lpObject, PRINTDLGFc.lpfnSetupHook, (jint)lpStruct->lpfnSetupHook);
	(*env)->SetIntField(env, lpObject, PRINTDLGFc.lpfnPrintHook, (jint)lpStruct->lpfnPrintHook);
	(*env)->SetIntField(env, lpObject, PRINTDLGFc.lCustData, (jint)lpStruct->lCustData);
	(*env)->SetIntField(env, lpObject, PRINTDLGFc.hInstance, (jint)lpStruct->hInstance);
	(*env)->SetShortField(env, lpObject, PRINTDLGFc.nCopies, (jshort)lpStruct->nCopies);
	(*env)->SetShortField(env, lpObject, PRINTDLGFc.nMaxPage, (jshort)lpStruct->nMaxPage);
	(*env)->SetShortField(env, lpObject, PRINTDLGFc.nMinPage, (jshort)lpStruct->nMinPage);
	(*env)->SetShortField(env, lpObject, PRINTDLGFc.nToPage, (jshort)lpStruct->nToPage);
	(*env)->SetShortField(env, lpObject, PRINTDLGFc.nFromPage, (jshort)lpStruct->nFromPage);
	(*env)->SetIntField(env, lpObject, PRINTDLGFc.Flags, (jint)lpStruct->Flags);
	(*env)->SetIntField(env, lpObject, PRINTDLGFc.hDC, (jint)lpStruct->hDC);
	(*env)->SetIntField(env, lpObject, PRINTDLGFc.hDevNames, (jint)lpStruct->hDevNames);
	(*env)->SetIntField(env, lpObject, PRINTDLGFc.hDevMode, (jint)lpStruct->hDevMode);
	(*env)->SetIntField(env, lpObject, PRINTDLGFc.hwndOwner, (jint)lpStruct->hwndOwner);
	(*env)->SetIntField(env, lpObject, PRINTDLGFc.lStructSize, (jint)lpStruct->lStructSize);
}
#endif

#ifndef NO_REBARBANDINFO
typedef struct REBARBANDINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cxHeader, lParam, cxIdeal, cyIntegral, cyMaxChild, cyChild, wID, hbmBack, cx, cyMinChild, cxMinChild, hwndChild, iImage, cch, lpText, clrBack, clrFore, fStyle, fMask, cbSize;
} REBARBANDINFO_FID_CACHE;

REBARBANDINFO_FID_CACHE REBARBANDINFOFc;

void cacheREBARBANDINFOFields(JNIEnv *env, jobject lpObject)
{
	if (REBARBANDINFOFc.cached) return;
	REBARBANDINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	REBARBANDINFOFc.cxHeader = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "cxHeader", "I");
	REBARBANDINFOFc.lParam = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "lParam", "I");
	REBARBANDINFOFc.cxIdeal = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "cxIdeal", "I");
	REBARBANDINFOFc.cyIntegral = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "cyIntegral", "I");
	REBARBANDINFOFc.cyMaxChild = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "cyMaxChild", "I");
	REBARBANDINFOFc.cyChild = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "cyChild", "I");
	REBARBANDINFOFc.wID = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "wID", "I");
	REBARBANDINFOFc.hbmBack = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "hbmBack", "I");
	REBARBANDINFOFc.cx = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "cx", "I");
	REBARBANDINFOFc.cyMinChild = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "cyMinChild", "I");
	REBARBANDINFOFc.cxMinChild = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "cxMinChild", "I");
	REBARBANDINFOFc.hwndChild = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "hwndChild", "I");
	REBARBANDINFOFc.iImage = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "iImage", "I");
	REBARBANDINFOFc.cch = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "cch", "I");
	REBARBANDINFOFc.lpText = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "lpText", "I");
	REBARBANDINFOFc.clrBack = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "clrBack", "I");
	REBARBANDINFOFc.clrFore = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "clrFore", "I");
	REBARBANDINFOFc.fStyle = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "fStyle", "I");
	REBARBANDINFOFc.fMask = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "fMask", "I");
	REBARBANDINFOFc.cbSize = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "cbSize", "I");
	REBARBANDINFOFc.cached = 1;
}

REBARBANDINFO *getREBARBANDINFOFields(JNIEnv *env, jobject lpObject, REBARBANDINFO *lpStruct)
{
	if (!REBARBANDINFOFc.cached) cacheREBARBANDINFOFields(env, lpObject);
#ifndef _WIN32_WCE
	lpStruct->cxHeader = (*env)->GetIntField(env, lpObject, REBARBANDINFOFc.cxHeader);
#endif
	lpStruct->lParam = (*env)->GetIntField(env, lpObject, REBARBANDINFOFc.lParam);
	lpStruct->cxIdeal = (*env)->GetIntField(env, lpObject, REBARBANDINFOFc.cxIdeal);
	lpStruct->cyIntegral = (*env)->GetIntField(env, lpObject, REBARBANDINFOFc.cyIntegral);
	lpStruct->cyMaxChild = (*env)->GetIntField(env, lpObject, REBARBANDINFOFc.cyMaxChild);
	lpStruct->cyChild = (*env)->GetIntField(env, lpObject, REBARBANDINFOFc.cyChild);
	lpStruct->wID = (*env)->GetIntField(env, lpObject, REBARBANDINFOFc.wID);
	lpStruct->hbmBack = (HBITMAP)(*env)->GetIntField(env, lpObject, REBARBANDINFOFc.hbmBack);
	lpStruct->cx = (*env)->GetIntField(env, lpObject, REBARBANDINFOFc.cx);
	lpStruct->cyMinChild = (*env)->GetIntField(env, lpObject, REBARBANDINFOFc.cyMinChild);
	lpStruct->cxMinChild = (*env)->GetIntField(env, lpObject, REBARBANDINFOFc.cxMinChild);
	lpStruct->hwndChild = (HWND)(*env)->GetIntField(env, lpObject, REBARBANDINFOFc.hwndChild);
	lpStruct->iImage = (*env)->GetIntField(env, lpObject, REBARBANDINFOFc.iImage);
	lpStruct->cch = (*env)->GetIntField(env, lpObject, REBARBANDINFOFc.cch);
	lpStruct->lpText = (LPTSTR)(*env)->GetIntField(env, lpObject, REBARBANDINFOFc.lpText);
	lpStruct->clrBack = (*env)->GetIntField(env, lpObject, REBARBANDINFOFc.clrBack);
	lpStruct->clrFore = (*env)->GetIntField(env, lpObject, REBARBANDINFOFc.clrFore);
	lpStruct->fStyle = (*env)->GetIntField(env, lpObject, REBARBANDINFOFc.fStyle);
	lpStruct->fMask = (*env)->GetIntField(env, lpObject, REBARBANDINFOFc.fMask);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, REBARBANDINFOFc.cbSize);
	return lpStruct;
}

void setREBARBANDINFOFields(JNIEnv *env, jobject lpObject, REBARBANDINFO *lpStruct)
{
	if (!REBARBANDINFOFc.cached) cacheREBARBANDINFOFields(env, lpObject);
#ifndef _WIN32_WCE
	(*env)->SetIntField(env, lpObject, REBARBANDINFOFc.cxHeader, (jint)lpStruct->cxHeader);
#endif
	(*env)->SetIntField(env, lpObject, REBARBANDINFOFc.lParam, (jint)lpStruct->lParam);
	(*env)->SetIntField(env, lpObject, REBARBANDINFOFc.cxIdeal, (jint)lpStruct->cxIdeal);
	(*env)->SetIntField(env, lpObject, REBARBANDINFOFc.cyIntegral, (jint)lpStruct->cyIntegral);
	(*env)->SetIntField(env, lpObject, REBARBANDINFOFc.cyMaxChild, (jint)lpStruct->cyMaxChild);
	(*env)->SetIntField(env, lpObject, REBARBANDINFOFc.cyChild, (jint)lpStruct->cyChild);
	(*env)->SetIntField(env, lpObject, REBARBANDINFOFc.wID, (jint)lpStruct->wID);
	(*env)->SetIntField(env, lpObject, REBARBANDINFOFc.hbmBack, (jint)lpStruct->hbmBack);
	(*env)->SetIntField(env, lpObject, REBARBANDINFOFc.cx, (jint)lpStruct->cx);
	(*env)->SetIntField(env, lpObject, REBARBANDINFOFc.cyMinChild, (jint)lpStruct->cyMinChild);
	(*env)->SetIntField(env, lpObject, REBARBANDINFOFc.cxMinChild, (jint)lpStruct->cxMinChild);
	(*env)->SetIntField(env, lpObject, REBARBANDINFOFc.hwndChild, (jint)lpStruct->hwndChild);
	(*env)->SetIntField(env, lpObject, REBARBANDINFOFc.iImage, (jint)lpStruct->iImage);
	(*env)->SetIntField(env, lpObject, REBARBANDINFOFc.cch, (jint)lpStruct->cch);
	(*env)->SetIntField(env, lpObject, REBARBANDINFOFc.lpText, (jint)lpStruct->lpText);
	(*env)->SetIntField(env, lpObject, REBARBANDINFOFc.clrBack, (jint)lpStruct->clrBack);
	(*env)->SetIntField(env, lpObject, REBARBANDINFOFc.clrFore, (jint)lpStruct->clrFore);
	(*env)->SetIntField(env, lpObject, REBARBANDINFOFc.fStyle, (jint)lpStruct->fStyle);
	(*env)->SetIntField(env, lpObject, REBARBANDINFOFc.fMask, (jint)lpStruct->fMask);
	(*env)->SetIntField(env, lpObject, REBARBANDINFOFc.cbSize, (jint)lpStruct->cbSize);
}
#endif

#ifndef NO_RECT
typedef struct RECT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID bottom, right, top, left;
} RECT_FID_CACHE;

RECT_FID_CACHE RECTFc;

void cacheRECTFields(JNIEnv *env, jobject lpObject)
{
	if (RECTFc.cached) return;
	RECTFc.clazz = (*env)->GetObjectClass(env, lpObject);
	RECTFc.bottom = (*env)->GetFieldID(env, RECTFc.clazz, "bottom", "I");
	RECTFc.right = (*env)->GetFieldID(env, RECTFc.clazz, "right", "I");
	RECTFc.top = (*env)->GetFieldID(env, RECTFc.clazz, "top", "I");
	RECTFc.left = (*env)->GetFieldID(env, RECTFc.clazz, "left", "I");
	RECTFc.cached = 1;
}

RECT *getRECTFields(JNIEnv *env, jobject lpObject, RECT *lpStruct)
{
	if (!RECTFc.cached) cacheRECTFields(env, lpObject);
	lpStruct->bottom = (*env)->GetIntField(env, lpObject, RECTFc.bottom);
	lpStruct->right = (*env)->GetIntField(env, lpObject, RECTFc.right);
	lpStruct->top = (*env)->GetIntField(env, lpObject, RECTFc.top);
	lpStruct->left = (*env)->GetIntField(env, lpObject, RECTFc.left);
	return lpStruct;
}

void setRECTFields(JNIEnv *env, jobject lpObject, RECT *lpStruct)
{
	if (!RECTFc.cached) cacheRECTFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, RECTFc.bottom, (jint)lpStruct->bottom);
	(*env)->SetIntField(env, lpObject, RECTFc.right, (jint)lpStruct->right);
	(*env)->SetIntField(env, lpObject, RECTFc.top, (jint)lpStruct->top);
	(*env)->SetIntField(env, lpObject, RECTFc.left, (jint)lpStruct->left);
}
#endif

#ifndef NO_SCROLLINFO
typedef struct SCROLLINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID nTrackPos, nPos, nPage, nMax, nMin, fMask, cbSize;
} SCROLLINFO_FID_CACHE;

SCROLLINFO_FID_CACHE SCROLLINFOFc;

void cacheSCROLLINFOFields(JNIEnv *env, jobject lpObject)
{
	if (SCROLLINFOFc.cached) return;
	SCROLLINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	SCROLLINFOFc.nTrackPos = (*env)->GetFieldID(env, SCROLLINFOFc.clazz, "nTrackPos", "I");
	SCROLLINFOFc.nPos = (*env)->GetFieldID(env, SCROLLINFOFc.clazz, "nPos", "I");
	SCROLLINFOFc.nPage = (*env)->GetFieldID(env, SCROLLINFOFc.clazz, "nPage", "I");
	SCROLLINFOFc.nMax = (*env)->GetFieldID(env, SCROLLINFOFc.clazz, "nMax", "I");
	SCROLLINFOFc.nMin = (*env)->GetFieldID(env, SCROLLINFOFc.clazz, "nMin", "I");
	SCROLLINFOFc.fMask = (*env)->GetFieldID(env, SCROLLINFOFc.clazz, "fMask", "I");
	SCROLLINFOFc.cbSize = (*env)->GetFieldID(env, SCROLLINFOFc.clazz, "cbSize", "I");
	SCROLLINFOFc.cached = 1;
}

SCROLLINFO *getSCROLLINFOFields(JNIEnv *env, jobject lpObject, SCROLLINFO *lpStruct)
{
	if (!SCROLLINFOFc.cached) cacheSCROLLINFOFields(env, lpObject);
	lpStruct->nTrackPos = (*env)->GetIntField(env, lpObject, SCROLLINFOFc.nTrackPos);
	lpStruct->nPos = (*env)->GetIntField(env, lpObject, SCROLLINFOFc.nPos);
	lpStruct->nPage = (*env)->GetIntField(env, lpObject, SCROLLINFOFc.nPage);
	lpStruct->nMax = (*env)->GetIntField(env, lpObject, SCROLLINFOFc.nMax);
	lpStruct->nMin = (*env)->GetIntField(env, lpObject, SCROLLINFOFc.nMin);
	lpStruct->fMask = (*env)->GetIntField(env, lpObject, SCROLLINFOFc.fMask);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, SCROLLINFOFc.cbSize);
	return lpStruct;
}

void setSCROLLINFOFields(JNIEnv *env, jobject lpObject, SCROLLINFO *lpStruct)
{
	if (!SCROLLINFOFc.cached) cacheSCROLLINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, SCROLLINFOFc.nTrackPos, (jint)lpStruct->nTrackPos);
	(*env)->SetIntField(env, lpObject, SCROLLINFOFc.nPos, (jint)lpStruct->nPos);
	(*env)->SetIntField(env, lpObject, SCROLLINFOFc.nPage, (jint)lpStruct->nPage);
	(*env)->SetIntField(env, lpObject, SCROLLINFOFc.nMax, (jint)lpStruct->nMax);
	(*env)->SetIntField(env, lpObject, SCROLLINFOFc.nMin, (jint)lpStruct->nMin);
	(*env)->SetIntField(env, lpObject, SCROLLINFOFc.fMask, (jint)lpStruct->fMask);
	(*env)->SetIntField(env, lpObject, SCROLLINFOFc.cbSize, (jint)lpStruct->cbSize);
}
#endif

#ifndef NO_SHACTIVATEINFO
typedef struct SHACTIVATEINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID fReserved, fActive, fSipOnDeactivation, fSipUp, hwndLastFocus, cbSize;
} SHACTIVATEINFO_FID_CACHE;

SHACTIVATEINFO_FID_CACHE SHACTIVATEINFOFc;

void cacheSHACTIVATEINFOFields(JNIEnv *env, jobject lpObject)
{
	if (SHACTIVATEINFOFc.cached) return;
	SHACTIVATEINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	SHACTIVATEINFOFc.fReserved = (*env)->GetFieldID(env, SHACTIVATEINFOFc.clazz, "fReserved", "I");
	SHACTIVATEINFOFc.fActive = (*env)->GetFieldID(env, SHACTIVATEINFOFc.clazz, "fActive", "I");
	SHACTIVATEINFOFc.fSipOnDeactivation = (*env)->GetFieldID(env, SHACTIVATEINFOFc.clazz, "fSipOnDeactivation", "I");
	SHACTIVATEINFOFc.fSipUp = (*env)->GetFieldID(env, SHACTIVATEINFOFc.clazz, "fSipUp", "I");
	SHACTIVATEINFOFc.hwndLastFocus = (*env)->GetFieldID(env, SHACTIVATEINFOFc.clazz, "hwndLastFocus", "I");
	SHACTIVATEINFOFc.cbSize = (*env)->GetFieldID(env, SHACTIVATEINFOFc.clazz, "cbSize", "I");
	SHACTIVATEINFOFc.cached = 1;
}

SHACTIVATEINFO *getSHACTIVATEINFOFields(JNIEnv *env, jobject lpObject, SHACTIVATEINFO *lpStruct)
{
	if (!SHACTIVATEINFOFc.cached) cacheSHACTIVATEINFOFields(env, lpObject);
	lpStruct->fReserved = (*env)->GetIntField(env, lpObject, SHACTIVATEINFOFc.fReserved);
	lpStruct->fActive = (*env)->GetIntField(env, lpObject, SHACTIVATEINFOFc.fActive);
	lpStruct->fSipOnDeactivation = (*env)->GetIntField(env, lpObject, SHACTIVATEINFOFc.fSipOnDeactivation);
	lpStruct->fSipUp = (*env)->GetIntField(env, lpObject, SHACTIVATEINFOFc.fSipUp);
	lpStruct->hwndLastFocus = (HWND)(*env)->GetIntField(env, lpObject, SHACTIVATEINFOFc.hwndLastFocus);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, SHACTIVATEINFOFc.cbSize);
	return lpStruct;
}

void setSHACTIVATEINFOFields(JNIEnv *env, jobject lpObject, SHACTIVATEINFO *lpStruct)
{
	if (!SHACTIVATEINFOFc.cached) cacheSHACTIVATEINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, SHACTIVATEINFOFc.fReserved, (jint)lpStruct->fReserved);
	(*env)->SetIntField(env, lpObject, SHACTIVATEINFOFc.fActive, (jint)lpStruct->fActive);
	(*env)->SetIntField(env, lpObject, SHACTIVATEINFOFc.fSipOnDeactivation, (jint)lpStruct->fSipOnDeactivation);
	(*env)->SetIntField(env, lpObject, SHACTIVATEINFOFc.fSipUp, (jint)lpStruct->fSipUp);
	(*env)->SetIntField(env, lpObject, SHACTIVATEINFOFc.hwndLastFocus, (jint)lpStruct->hwndLastFocus);
	(*env)->SetIntField(env, lpObject, SHACTIVATEINFOFc.cbSize, (jint)lpStruct->cbSize);
}
#endif

#ifndef NO_SHELLEXECUTEINFO
typedef struct SHELLEXECUTEINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID hProcess, hIcon, dwHotKey, hkeyClass, lpClass, lpIDList, hInstApp, nShow, lpDirectory, lpParameters, lpFile, lpVerb, hwnd, fMask, cbSize;
} SHELLEXECUTEINFO_FID_CACHE;

SHELLEXECUTEINFO_FID_CACHE SHELLEXECUTEINFOFc;

void cacheSHELLEXECUTEINFOFields(JNIEnv *env, jobject lpObject)
{
	if (SHELLEXECUTEINFOFc.cached) return;
	SHELLEXECUTEINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	SHELLEXECUTEINFOFc.hProcess = (*env)->GetFieldID(env, SHELLEXECUTEINFOFc.clazz, "hProcess", "I");
	SHELLEXECUTEINFOFc.hIcon = (*env)->GetFieldID(env, SHELLEXECUTEINFOFc.clazz, "hIcon", "I");
	SHELLEXECUTEINFOFc.dwHotKey = (*env)->GetFieldID(env, SHELLEXECUTEINFOFc.clazz, "dwHotKey", "I");
	SHELLEXECUTEINFOFc.hkeyClass = (*env)->GetFieldID(env, SHELLEXECUTEINFOFc.clazz, "hkeyClass", "I");
	SHELLEXECUTEINFOFc.lpClass = (*env)->GetFieldID(env, SHELLEXECUTEINFOFc.clazz, "lpClass", "I");
	SHELLEXECUTEINFOFc.lpIDList = (*env)->GetFieldID(env, SHELLEXECUTEINFOFc.clazz, "lpIDList", "I");
	SHELLEXECUTEINFOFc.hInstApp = (*env)->GetFieldID(env, SHELLEXECUTEINFOFc.clazz, "hInstApp", "I");
	SHELLEXECUTEINFOFc.nShow = (*env)->GetFieldID(env, SHELLEXECUTEINFOFc.clazz, "nShow", "I");
	SHELLEXECUTEINFOFc.lpDirectory = (*env)->GetFieldID(env, SHELLEXECUTEINFOFc.clazz, "lpDirectory", "I");
	SHELLEXECUTEINFOFc.lpParameters = (*env)->GetFieldID(env, SHELLEXECUTEINFOFc.clazz, "lpParameters", "I");
	SHELLEXECUTEINFOFc.lpFile = (*env)->GetFieldID(env, SHELLEXECUTEINFOFc.clazz, "lpFile", "I");
	SHELLEXECUTEINFOFc.lpVerb = (*env)->GetFieldID(env, SHELLEXECUTEINFOFc.clazz, "lpVerb", "I");
	SHELLEXECUTEINFOFc.hwnd = (*env)->GetFieldID(env, SHELLEXECUTEINFOFc.clazz, "hwnd", "I");
	SHELLEXECUTEINFOFc.fMask = (*env)->GetFieldID(env, SHELLEXECUTEINFOFc.clazz, "fMask", "I");
	SHELLEXECUTEINFOFc.cbSize = (*env)->GetFieldID(env, SHELLEXECUTEINFOFc.clazz, "cbSize", "I");
	SHELLEXECUTEINFOFc.cached = 1;
}

SHELLEXECUTEINFO *getSHELLEXECUTEINFOFields(JNIEnv *env, jobject lpObject, SHELLEXECUTEINFO *lpStruct)
{
	if (!SHELLEXECUTEINFOFc.cached) cacheSHELLEXECUTEINFOFields(env, lpObject);
	lpStruct->hProcess = (HANDLE)(*env)->GetIntField(env, lpObject, SHELLEXECUTEINFOFc.hProcess);
	lpStruct->hIcon = (HANDLE)(*env)->GetIntField(env, lpObject, SHELLEXECUTEINFOFc.hIcon);
	lpStruct->dwHotKey = (*env)->GetIntField(env, lpObject, SHELLEXECUTEINFOFc.dwHotKey);
	lpStruct->hkeyClass = (HKEY)(*env)->GetIntField(env, lpObject, SHELLEXECUTEINFOFc.hkeyClass);
	lpStruct->lpClass = (LPCTSTR)(*env)->GetIntField(env, lpObject, SHELLEXECUTEINFOFc.lpClass);
	lpStruct->lpIDList = (LPVOID)(*env)->GetIntField(env, lpObject, SHELLEXECUTEINFOFc.lpIDList);
	lpStruct->hInstApp = (HINSTANCE)(*env)->GetIntField(env, lpObject, SHELLEXECUTEINFOFc.hInstApp);
	lpStruct->nShow = (*env)->GetIntField(env, lpObject, SHELLEXECUTEINFOFc.nShow);
	lpStruct->lpDirectory = (LPCTSTR)(*env)->GetIntField(env, lpObject, SHELLEXECUTEINFOFc.lpDirectory);
	lpStruct->lpParameters = (LPCTSTR)(*env)->GetIntField(env, lpObject, SHELLEXECUTEINFOFc.lpParameters);
	lpStruct->lpFile = (LPCTSTR)(*env)->GetIntField(env, lpObject, SHELLEXECUTEINFOFc.lpFile);
	lpStruct->lpVerb = (LPCTSTR)(*env)->GetIntField(env, lpObject, SHELLEXECUTEINFOFc.lpVerb);
	lpStruct->hwnd = (HWND)(*env)->GetIntField(env, lpObject, SHELLEXECUTEINFOFc.hwnd);
	lpStruct->fMask = (*env)->GetIntField(env, lpObject, SHELLEXECUTEINFOFc.fMask);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, SHELLEXECUTEINFOFc.cbSize);
	return lpStruct;
}

void setSHELLEXECUTEINFOFields(JNIEnv *env, jobject lpObject, SHELLEXECUTEINFO *lpStruct)
{
	if (!SHELLEXECUTEINFOFc.cached) cacheSHELLEXECUTEINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, SHELLEXECUTEINFOFc.hProcess, (jint)lpStruct->hProcess);
	(*env)->SetIntField(env, lpObject, SHELLEXECUTEINFOFc.hIcon, (jint)lpStruct->hIcon);
	(*env)->SetIntField(env, lpObject, SHELLEXECUTEINFOFc.dwHotKey, (jint)lpStruct->dwHotKey);
	(*env)->SetIntField(env, lpObject, SHELLEXECUTEINFOFc.hkeyClass, (jint)lpStruct->hkeyClass);
	(*env)->SetIntField(env, lpObject, SHELLEXECUTEINFOFc.lpClass, (jint)lpStruct->lpClass);
	(*env)->SetIntField(env, lpObject, SHELLEXECUTEINFOFc.lpIDList, (jint)lpStruct->lpIDList);
	(*env)->SetIntField(env, lpObject, SHELLEXECUTEINFOFc.hInstApp, (jint)lpStruct->hInstApp);
	(*env)->SetIntField(env, lpObject, SHELLEXECUTEINFOFc.nShow, (jint)lpStruct->nShow);
	(*env)->SetIntField(env, lpObject, SHELLEXECUTEINFOFc.lpDirectory, (jint)lpStruct->lpDirectory);
	(*env)->SetIntField(env, lpObject, SHELLEXECUTEINFOFc.lpParameters, (jint)lpStruct->lpParameters);
	(*env)->SetIntField(env, lpObject, SHELLEXECUTEINFOFc.lpFile, (jint)lpStruct->lpFile);
	(*env)->SetIntField(env, lpObject, SHELLEXECUTEINFOFc.lpVerb, (jint)lpStruct->lpVerb);
	(*env)->SetIntField(env, lpObject, SHELLEXECUTEINFOFc.hwnd, (jint)lpStruct->hwnd);
	(*env)->SetIntField(env, lpObject, SHELLEXECUTEINFOFc.fMask, (jint)lpStruct->fMask);
	(*env)->SetIntField(env, lpObject, SHELLEXECUTEINFOFc.cbSize, (jint)lpStruct->cbSize);
}
#endif

#ifndef NO_SHMENUBARINFO
typedef struct SHMENUBARINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID hwndMB, cBmpImages, nBmpId, hInstRes, nToolBarId, dwFlags, hwndParent, cbSize;
} SHMENUBARINFO_FID_CACHE;

SHMENUBARINFO_FID_CACHE SHMENUBARINFOFc;

void cacheSHMENUBARINFOFields(JNIEnv *env, jobject lpObject)
{
	if (SHMENUBARINFOFc.cached) return;
	SHMENUBARINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	SHMENUBARINFOFc.hwndMB = (*env)->GetFieldID(env, SHMENUBARINFOFc.clazz, "hwndMB", "I");
	SHMENUBARINFOFc.cBmpImages = (*env)->GetFieldID(env, SHMENUBARINFOFc.clazz, "cBmpImages", "I");
	SHMENUBARINFOFc.nBmpId = (*env)->GetFieldID(env, SHMENUBARINFOFc.clazz, "nBmpId", "I");
	SHMENUBARINFOFc.hInstRes = (*env)->GetFieldID(env, SHMENUBARINFOFc.clazz, "hInstRes", "I");
	SHMENUBARINFOFc.nToolBarId = (*env)->GetFieldID(env, SHMENUBARINFOFc.clazz, "nToolBarId", "I");
	SHMENUBARINFOFc.dwFlags = (*env)->GetFieldID(env, SHMENUBARINFOFc.clazz, "dwFlags", "I");
	SHMENUBARINFOFc.hwndParent = (*env)->GetFieldID(env, SHMENUBARINFOFc.clazz, "hwndParent", "I");
	SHMENUBARINFOFc.cbSize = (*env)->GetFieldID(env, SHMENUBARINFOFc.clazz, "cbSize", "I");
	SHMENUBARINFOFc.cached = 1;
}

SHMENUBARINFO *getSHMENUBARINFOFields(JNIEnv *env, jobject lpObject, SHMENUBARINFO *lpStruct)
{
	if (!SHMENUBARINFOFc.cached) cacheSHMENUBARINFOFields(env, lpObject);
	lpStruct->hwndMB = (HWND)(*env)->GetIntField(env, lpObject, SHMENUBARINFOFc.hwndMB);
	lpStruct->cBmpImages = (*env)->GetIntField(env, lpObject, SHMENUBARINFOFc.cBmpImages);
	lpStruct->nBmpId = (*env)->GetIntField(env, lpObject, SHMENUBARINFOFc.nBmpId);
	lpStruct->hInstRes = (HINSTANCE)(*env)->GetIntField(env, lpObject, SHMENUBARINFOFc.hInstRes);
	lpStruct->nToolBarId = (*env)->GetIntField(env, lpObject, SHMENUBARINFOFc.nToolBarId);
	lpStruct->dwFlags = (*env)->GetIntField(env, lpObject, SHMENUBARINFOFc.dwFlags);
	lpStruct->hwndParent = (HWND)(*env)->GetIntField(env, lpObject, SHMENUBARINFOFc.hwndParent);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, SHMENUBARINFOFc.cbSize);
	return lpStruct;
}

void setSHMENUBARINFOFields(JNIEnv *env, jobject lpObject, SHMENUBARINFO *lpStruct)
{
	if (!SHMENUBARINFOFc.cached) cacheSHMENUBARINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, SHMENUBARINFOFc.hwndMB, (jint)lpStruct->hwndMB);
	(*env)->SetIntField(env, lpObject, SHMENUBARINFOFc.cBmpImages, (jint)lpStruct->cBmpImages);
	(*env)->SetIntField(env, lpObject, SHMENUBARINFOFc.nBmpId, (jint)lpStruct->nBmpId);
	(*env)->SetIntField(env, lpObject, SHMENUBARINFOFc.hInstRes, (jint)lpStruct->hInstRes);
	(*env)->SetIntField(env, lpObject, SHMENUBARINFOFc.nToolBarId, (jint)lpStruct->nToolBarId);
	(*env)->SetIntField(env, lpObject, SHMENUBARINFOFc.dwFlags, (jint)lpStruct->dwFlags);
	(*env)->SetIntField(env, lpObject, SHMENUBARINFOFc.hwndParent, (jint)lpStruct->hwndParent);
	(*env)->SetIntField(env, lpObject, SHMENUBARINFOFc.cbSize, (jint)lpStruct->cbSize);
}
#endif

#ifndef NO_SHRGINFO
typedef struct SHRGINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID dwFlags, ptDown_y, ptDown_x, hwndClient, cbSize;
} SHRGINFO_FID_CACHE;

SHRGINFO_FID_CACHE SHRGINFOFc;

void cacheSHRGINFOFields(JNIEnv *env, jobject lpObject)
{
	if (SHRGINFOFc.cached) return;
	SHRGINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	SHRGINFOFc.dwFlags = (*env)->GetFieldID(env, SHRGINFOFc.clazz, "dwFlags", "I");
	SHRGINFOFc.ptDown_y = (*env)->GetFieldID(env, SHRGINFOFc.clazz, "ptDown_y", "I");
	SHRGINFOFc.ptDown_x = (*env)->GetFieldID(env, SHRGINFOFc.clazz, "ptDown_x", "I");
	SHRGINFOFc.hwndClient = (*env)->GetFieldID(env, SHRGINFOFc.clazz, "hwndClient", "I");
	SHRGINFOFc.cbSize = (*env)->GetFieldID(env, SHRGINFOFc.clazz, "cbSize", "I");
	SHRGINFOFc.cached = 1;
}

SHRGINFO *getSHRGINFOFields(JNIEnv *env, jobject lpObject, SHRGINFO *lpStruct)
{
	if (!SHRGINFOFc.cached) cacheSHRGINFOFields(env, lpObject);
	lpStruct->dwFlags = (*env)->GetIntField(env, lpObject, SHRGINFOFc.dwFlags);
	lpStruct->ptDown.y = (*env)->GetIntField(env, lpObject, SHRGINFOFc.ptDown_y);
	lpStruct->ptDown.x = (*env)->GetIntField(env, lpObject, SHRGINFOFc.ptDown_x);
	lpStruct->hwndClient = (HWND)(*env)->GetIntField(env, lpObject, SHRGINFOFc.hwndClient);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, SHRGINFOFc.cbSize);
	return lpStruct;
}

void setSHRGINFOFields(JNIEnv *env, jobject lpObject, SHRGINFO *lpStruct)
{
	if (!SHRGINFOFc.cached) cacheSHRGINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, SHRGINFOFc.dwFlags, (jint)lpStruct->dwFlags);
	(*env)->SetIntField(env, lpObject, SHRGINFOFc.ptDown_y, (jint)lpStruct->ptDown.y);
	(*env)->SetIntField(env, lpObject, SHRGINFOFc.ptDown_x, (jint)lpStruct->ptDown.x);
	(*env)->SetIntField(env, lpObject, SHRGINFOFc.hwndClient, (jint)lpStruct->hwndClient);
	(*env)->SetIntField(env, lpObject, SHRGINFOFc.cbSize, (jint)lpStruct->cbSize);
}
#endif

#ifndef NO_SIPINFO
typedef struct SIPINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID pvImData, dwImDataSize, rcSipRect_bottom, rcSipRect_right, rcSipRect_top, rcSipRect_left, rcVisibleDesktop_bottom, rcVisibleDesktop_right, rcVisibleDesktop_top, rcVisibleDesktop_left, fdwFlags, cbSize;
} SIPINFO_FID_CACHE;

SIPINFO_FID_CACHE SIPINFOFc;

void cacheSIPINFOFields(JNIEnv *env, jobject lpObject)
{
	if (SIPINFOFc.cached) return;
	SIPINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	SIPINFOFc.pvImData = (*env)->GetFieldID(env, SIPINFOFc.clazz, "pvImData", "I");
	SIPINFOFc.dwImDataSize = (*env)->GetFieldID(env, SIPINFOFc.clazz, "dwImDataSize", "I");
	SIPINFOFc.rcSipRect_bottom = (*env)->GetFieldID(env, SIPINFOFc.clazz, "rcSipRect_bottom", "I");
	SIPINFOFc.rcSipRect_right = (*env)->GetFieldID(env, SIPINFOFc.clazz, "rcSipRect_right", "I");
	SIPINFOFc.rcSipRect_top = (*env)->GetFieldID(env, SIPINFOFc.clazz, "rcSipRect_top", "I");
	SIPINFOFc.rcSipRect_left = (*env)->GetFieldID(env, SIPINFOFc.clazz, "rcSipRect_left", "I");
	SIPINFOFc.rcVisibleDesktop_bottom = (*env)->GetFieldID(env, SIPINFOFc.clazz, "rcVisibleDesktop_bottom", "I");
	SIPINFOFc.rcVisibleDesktop_right = (*env)->GetFieldID(env, SIPINFOFc.clazz, "rcVisibleDesktop_right", "I");
	SIPINFOFc.rcVisibleDesktop_top = (*env)->GetFieldID(env, SIPINFOFc.clazz, "rcVisibleDesktop_top", "I");
	SIPINFOFc.rcVisibleDesktop_left = (*env)->GetFieldID(env, SIPINFOFc.clazz, "rcVisibleDesktop_left", "I");
	SIPINFOFc.fdwFlags = (*env)->GetFieldID(env, SIPINFOFc.clazz, "fdwFlags", "I");
	SIPINFOFc.cbSize = (*env)->GetFieldID(env, SIPINFOFc.clazz, "cbSize", "I");
	SIPINFOFc.cached = 1;
}

SIPINFO *getSIPINFOFields(JNIEnv *env, jobject lpObject, SIPINFO *lpStruct)
{
	if (!SIPINFOFc.cached) cacheSIPINFOFields(env, lpObject);
	lpStruct->pvImData = (void *)(*env)->GetIntField(env, lpObject, SIPINFOFc.pvImData);
	lpStruct->dwImDataSize = (*env)->GetIntField(env, lpObject, SIPINFOFc.dwImDataSize);
	lpStruct->rcSipRect.bottom = (*env)->GetIntField(env, lpObject, SIPINFOFc.rcSipRect_bottom);
	lpStruct->rcSipRect.right = (*env)->GetIntField(env, lpObject, SIPINFOFc.rcSipRect_right);
	lpStruct->rcSipRect.top = (*env)->GetIntField(env, lpObject, SIPINFOFc.rcSipRect_top);
	lpStruct->rcSipRect.left = (*env)->GetIntField(env, lpObject, SIPINFOFc.rcSipRect_left);
	lpStruct->rcVisibleDesktop.bottom = (*env)->GetIntField(env, lpObject, SIPINFOFc.rcVisibleDesktop_bottom);
	lpStruct->rcVisibleDesktop.right = (*env)->GetIntField(env, lpObject, SIPINFOFc.rcVisibleDesktop_right);
	lpStruct->rcVisibleDesktop.top = (*env)->GetIntField(env, lpObject, SIPINFOFc.rcVisibleDesktop_top);
	lpStruct->rcVisibleDesktop.left = (*env)->GetIntField(env, lpObject, SIPINFOFc.rcVisibleDesktop_left);
	lpStruct->fdwFlags = (*env)->GetIntField(env, lpObject, SIPINFOFc.fdwFlags);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, SIPINFOFc.cbSize);
	return lpStruct;
}

void setSIPINFOFields(JNIEnv *env, jobject lpObject, SIPINFO *lpStruct)
{
	if (!SIPINFOFc.cached) cacheSIPINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, SIPINFOFc.pvImData, (jint)lpStruct->pvImData);
	(*env)->SetIntField(env, lpObject, SIPINFOFc.dwImDataSize, (jint)lpStruct->dwImDataSize);
	(*env)->SetIntField(env, lpObject, SIPINFOFc.rcSipRect_bottom, (jint)lpStruct->rcSipRect.bottom);
	(*env)->SetIntField(env, lpObject, SIPINFOFc.rcSipRect_right, (jint)lpStruct->rcSipRect.right);
	(*env)->SetIntField(env, lpObject, SIPINFOFc.rcSipRect_top, (jint)lpStruct->rcSipRect.top);
	(*env)->SetIntField(env, lpObject, SIPINFOFc.rcSipRect_left, (jint)lpStruct->rcSipRect.left);
	(*env)->SetIntField(env, lpObject, SIPINFOFc.rcVisibleDesktop_bottom, (jint)lpStruct->rcVisibleDesktop.bottom);
	(*env)->SetIntField(env, lpObject, SIPINFOFc.rcVisibleDesktop_right, (jint)lpStruct->rcVisibleDesktop.right);
	(*env)->SetIntField(env, lpObject, SIPINFOFc.rcVisibleDesktop_top, (jint)lpStruct->rcVisibleDesktop.top);
	(*env)->SetIntField(env, lpObject, SIPINFOFc.rcVisibleDesktop_left, (jint)lpStruct->rcVisibleDesktop.left);
	(*env)->SetIntField(env, lpObject, SIPINFOFc.fdwFlags, (jint)lpStruct->fdwFlags);
	(*env)->SetIntField(env, lpObject, SIPINFOFc.cbSize, (jint)lpStruct->cbSize);
}
#endif

#ifndef NO_SIZE
typedef struct SIZE_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cy, cx;
} SIZE_FID_CACHE;

SIZE_FID_CACHE SIZEFc;

void cacheSIZEFields(JNIEnv *env, jobject lpObject)
{
	if (SIZEFc.cached) return;
	SIZEFc.clazz = (*env)->GetObjectClass(env, lpObject);
	SIZEFc.cy = (*env)->GetFieldID(env, SIZEFc.clazz, "cy", "I");
	SIZEFc.cx = (*env)->GetFieldID(env, SIZEFc.clazz, "cx", "I");
	SIZEFc.cached = 1;
}

SIZE *getSIZEFields(JNIEnv *env, jobject lpObject, SIZE *lpStruct)
{
	if (!SIZEFc.cached) cacheSIZEFields(env, lpObject);
	lpStruct->cy = (*env)->GetIntField(env, lpObject, SIZEFc.cy);
	lpStruct->cx = (*env)->GetIntField(env, lpObject, SIZEFc.cx);
	return lpStruct;
}

void setSIZEFields(JNIEnv *env, jobject lpObject, SIZE *lpStruct)
{
	if (!SIZEFc.cached) cacheSIZEFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, SIZEFc.cy, (jint)lpStruct->cy);
	(*env)->SetIntField(env, lpObject, SIZEFc.cx, (jint)lpStruct->cx);
}
#endif

#ifndef NO_TBBUTTON
typedef struct TBBUTTON_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID iString, dwData, fsStyle, fsState, idCommand, iBitmap;
} TBBUTTON_FID_CACHE;

TBBUTTON_FID_CACHE TBBUTTONFc;

void cacheTBBUTTONFields(JNIEnv *env, jobject lpObject)
{
	if (TBBUTTONFc.cached) return;
	TBBUTTONFc.clazz = (*env)->GetObjectClass(env, lpObject);
	TBBUTTONFc.iString = (*env)->GetFieldID(env, TBBUTTONFc.clazz, "iString", "I");
	TBBUTTONFc.dwData = (*env)->GetFieldID(env, TBBUTTONFc.clazz, "dwData", "I");
	TBBUTTONFc.fsStyle = (*env)->GetFieldID(env, TBBUTTONFc.clazz, "fsStyle", "B");
	TBBUTTONFc.fsState = (*env)->GetFieldID(env, TBBUTTONFc.clazz, "fsState", "B");
	TBBUTTONFc.idCommand = (*env)->GetFieldID(env, TBBUTTONFc.clazz, "idCommand", "I");
	TBBUTTONFc.iBitmap = (*env)->GetFieldID(env, TBBUTTONFc.clazz, "iBitmap", "I");
	TBBUTTONFc.cached = 1;
}

TBBUTTON *getTBBUTTONFields(JNIEnv *env, jobject lpObject, TBBUTTON *lpStruct)
{
	if (!TBBUTTONFc.cached) cacheTBBUTTONFields(env, lpObject);
	lpStruct->iString = (*env)->GetIntField(env, lpObject, TBBUTTONFc.iString);
	lpStruct->dwData = (*env)->GetIntField(env, lpObject, TBBUTTONFc.dwData);
	lpStruct->fsStyle = (*env)->GetByteField(env, lpObject, TBBUTTONFc.fsStyle);
	lpStruct->fsState = (*env)->GetByteField(env, lpObject, TBBUTTONFc.fsState);
	lpStruct->idCommand = (*env)->GetIntField(env, lpObject, TBBUTTONFc.idCommand);
	lpStruct->iBitmap = (*env)->GetIntField(env, lpObject, TBBUTTONFc.iBitmap);
	return lpStruct;
}

void setTBBUTTONFields(JNIEnv *env, jobject lpObject, TBBUTTON *lpStruct)
{
	if (!TBBUTTONFc.cached) cacheTBBUTTONFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, TBBUTTONFc.iString, (jint)lpStruct->iString);
	(*env)->SetIntField(env, lpObject, TBBUTTONFc.dwData, (jint)lpStruct->dwData);
	(*env)->SetByteField(env, lpObject, TBBUTTONFc.fsStyle, (jbyte)lpStruct->fsStyle);
	(*env)->SetByteField(env, lpObject, TBBUTTONFc.fsState, (jbyte)lpStruct->fsState);
	(*env)->SetIntField(env, lpObject, TBBUTTONFc.idCommand, (jint)lpStruct->idCommand);
	(*env)->SetIntField(env, lpObject, TBBUTTONFc.iBitmap, (jint)lpStruct->iBitmap);
}
#endif

#ifndef NO_TBBUTTONINFO
typedef struct TBBUTTONINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cchText, pszText, lParam, cx, fsStyle, fsState, iImage, idCommand, dwMask, cbSize;
} TBBUTTONINFO_FID_CACHE;

TBBUTTONINFO_FID_CACHE TBBUTTONINFOFc;

void cacheTBBUTTONINFOFields(JNIEnv *env, jobject lpObject)
{
	if (TBBUTTONINFOFc.cached) return;
	TBBUTTONINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	TBBUTTONINFOFc.cchText = (*env)->GetFieldID(env, TBBUTTONINFOFc.clazz, "cchText", "I");
	TBBUTTONINFOFc.pszText = (*env)->GetFieldID(env, TBBUTTONINFOFc.clazz, "pszText", "I");
	TBBUTTONINFOFc.lParam = (*env)->GetFieldID(env, TBBUTTONINFOFc.clazz, "lParam", "I");
	TBBUTTONINFOFc.cx = (*env)->GetFieldID(env, TBBUTTONINFOFc.clazz, "cx", "S");
	TBBUTTONINFOFc.fsStyle = (*env)->GetFieldID(env, TBBUTTONINFOFc.clazz, "fsStyle", "B");
	TBBUTTONINFOFc.fsState = (*env)->GetFieldID(env, TBBUTTONINFOFc.clazz, "fsState", "B");
	TBBUTTONINFOFc.iImage = (*env)->GetFieldID(env, TBBUTTONINFOFc.clazz, "iImage", "I");
	TBBUTTONINFOFc.idCommand = (*env)->GetFieldID(env, TBBUTTONINFOFc.clazz, "idCommand", "I");
	TBBUTTONINFOFc.dwMask = (*env)->GetFieldID(env, TBBUTTONINFOFc.clazz, "dwMask", "I");
	TBBUTTONINFOFc.cbSize = (*env)->GetFieldID(env, TBBUTTONINFOFc.clazz, "cbSize", "I");
	TBBUTTONINFOFc.cached = 1;
}

TBBUTTONINFO *getTBBUTTONINFOFields(JNIEnv *env, jobject lpObject, TBBUTTONINFO *lpStruct)
{
	if (!TBBUTTONINFOFc.cached) cacheTBBUTTONINFOFields(env, lpObject);
	lpStruct->cchText = (*env)->GetIntField(env, lpObject, TBBUTTONINFOFc.cchText);
	lpStruct->pszText = (LPTSTR)(*env)->GetIntField(env, lpObject, TBBUTTONINFOFc.pszText);
	lpStruct->lParam = (*env)->GetIntField(env, lpObject, TBBUTTONINFOFc.lParam);
	lpStruct->cx = (*env)->GetShortField(env, lpObject, TBBUTTONINFOFc.cx);
	lpStruct->fsStyle = (*env)->GetByteField(env, lpObject, TBBUTTONINFOFc.fsStyle);
	lpStruct->fsState = (*env)->GetByteField(env, lpObject, TBBUTTONINFOFc.fsState);
	lpStruct->iImage = (*env)->GetIntField(env, lpObject, TBBUTTONINFOFc.iImage);
	lpStruct->idCommand = (*env)->GetIntField(env, lpObject, TBBUTTONINFOFc.idCommand);
	lpStruct->dwMask = (*env)->GetIntField(env, lpObject, TBBUTTONINFOFc.dwMask);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, TBBUTTONINFOFc.cbSize);
	return lpStruct;
}

void setTBBUTTONINFOFields(JNIEnv *env, jobject lpObject, TBBUTTONINFO *lpStruct)
{
	if (!TBBUTTONINFOFc.cached) cacheTBBUTTONINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, TBBUTTONINFOFc.cchText, (jint)lpStruct->cchText);
	(*env)->SetIntField(env, lpObject, TBBUTTONINFOFc.pszText, (jint)lpStruct->pszText);
	(*env)->SetIntField(env, lpObject, TBBUTTONINFOFc.lParam, (jint)lpStruct->lParam);
	(*env)->SetShortField(env, lpObject, TBBUTTONINFOFc.cx, (jshort)lpStruct->cx);
	(*env)->SetByteField(env, lpObject, TBBUTTONINFOFc.fsStyle, (jbyte)lpStruct->fsStyle);
	(*env)->SetByteField(env, lpObject, TBBUTTONINFOFc.fsState, (jbyte)lpStruct->fsState);
	(*env)->SetIntField(env, lpObject, TBBUTTONINFOFc.iImage, (jint)lpStruct->iImage);
	(*env)->SetIntField(env, lpObject, TBBUTTONINFOFc.idCommand, (jint)lpStruct->idCommand);
	(*env)->SetIntField(env, lpObject, TBBUTTONINFOFc.dwMask, (jint)lpStruct->dwMask);
	(*env)->SetIntField(env, lpObject, TBBUTTONINFOFc.cbSize, (jint)lpStruct->cbSize);
}
#endif

#ifndef NO_TCITEM
typedef struct TCITEM_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lParam, iImage, cchTextMax, pszText, dwStateMask, dwState, mask;
} TCITEM_FID_CACHE;

TCITEM_FID_CACHE TCITEMFc;

void cacheTCITEMFields(JNIEnv *env, jobject lpObject)
{
	if (TCITEMFc.cached) return;
	TCITEMFc.clazz = (*env)->GetObjectClass(env, lpObject);
	TCITEMFc.lParam = (*env)->GetFieldID(env, TCITEMFc.clazz, "lParam", "I");
	TCITEMFc.iImage = (*env)->GetFieldID(env, TCITEMFc.clazz, "iImage", "I");
	TCITEMFc.cchTextMax = (*env)->GetFieldID(env, TCITEMFc.clazz, "cchTextMax", "I");
	TCITEMFc.pszText = (*env)->GetFieldID(env, TCITEMFc.clazz, "pszText", "I");
	TCITEMFc.dwStateMask = (*env)->GetFieldID(env, TCITEMFc.clazz, "dwStateMask", "I");
	TCITEMFc.dwState = (*env)->GetFieldID(env, TCITEMFc.clazz, "dwState", "I");
	TCITEMFc.mask = (*env)->GetFieldID(env, TCITEMFc.clazz, "mask", "I");
	TCITEMFc.cached = 1;
}

TCITEM *getTCITEMFields(JNIEnv *env, jobject lpObject, TCITEM *lpStruct)
{
	if (!TCITEMFc.cached) cacheTCITEMFields(env, lpObject);
	lpStruct->lParam = (*env)->GetIntField(env, lpObject, TCITEMFc.lParam);
	lpStruct->iImage = (*env)->GetIntField(env, lpObject, TCITEMFc.iImage);
	lpStruct->cchTextMax = (*env)->GetIntField(env, lpObject, TCITEMFc.cchTextMax);
	lpStruct->pszText = (LPTSTR)(*env)->GetIntField(env, lpObject, TCITEMFc.pszText);
	lpStruct->dwStateMask = (*env)->GetIntField(env, lpObject, TCITEMFc.dwStateMask);
	lpStruct->dwState = (*env)->GetIntField(env, lpObject, TCITEMFc.dwState);
	lpStruct->mask = (*env)->GetIntField(env, lpObject, TCITEMFc.mask);
	return lpStruct;
}

void setTCITEMFields(JNIEnv *env, jobject lpObject, TCITEM *lpStruct)
{
	if (!TCITEMFc.cached) cacheTCITEMFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, TCITEMFc.lParam, (jint)lpStruct->lParam);
	(*env)->SetIntField(env, lpObject, TCITEMFc.iImage, (jint)lpStruct->iImage);
	(*env)->SetIntField(env, lpObject, TCITEMFc.cchTextMax, (jint)lpStruct->cchTextMax);
	(*env)->SetIntField(env, lpObject, TCITEMFc.pszText, (jint)lpStruct->pszText);
	(*env)->SetIntField(env, lpObject, TCITEMFc.dwStateMask, (jint)lpStruct->dwStateMask);
	(*env)->SetIntField(env, lpObject, TCITEMFc.dwState, (jint)lpStruct->dwState);
	(*env)->SetIntField(env, lpObject, TCITEMFc.mask, (jint)lpStruct->mask);
}
#endif

#ifndef NO_TEXTMETRIC
typedef struct TEXTMETRIC_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID tmCharSet, tmPitchAndFamily, tmStruckOut, tmUnderlined, tmItalic, tmDigitizedAspectY, tmDigitizedAspectX, tmOverhang, tmWeight, tmMaxCharWidth, tmAveCharWidth, tmExternalLeading, tmInternalLeading, tmDescent, tmAscent, tmHeight;
} TEXTMETRIC_FID_CACHE;

TEXTMETRIC_FID_CACHE TEXTMETRICFc;

void cacheTEXTMETRICFields(JNIEnv *env, jobject lpObject)
{
	if (TEXTMETRICFc.cached) return;
	TEXTMETRICFc.clazz = (*env)->GetObjectClass(env, lpObject);
	TEXTMETRICFc.tmCharSet = (*env)->GetFieldID(env, TEXTMETRICFc.clazz, "tmCharSet", "B");
	TEXTMETRICFc.tmPitchAndFamily = (*env)->GetFieldID(env, TEXTMETRICFc.clazz, "tmPitchAndFamily", "B");
	TEXTMETRICFc.tmStruckOut = (*env)->GetFieldID(env, TEXTMETRICFc.clazz, "tmStruckOut", "B");
	TEXTMETRICFc.tmUnderlined = (*env)->GetFieldID(env, TEXTMETRICFc.clazz, "tmUnderlined", "B");
	TEXTMETRICFc.tmItalic = (*env)->GetFieldID(env, TEXTMETRICFc.clazz, "tmItalic", "B");
	TEXTMETRICFc.tmDigitizedAspectY = (*env)->GetFieldID(env, TEXTMETRICFc.clazz, "tmDigitizedAspectY", "I");
	TEXTMETRICFc.tmDigitizedAspectX = (*env)->GetFieldID(env, TEXTMETRICFc.clazz, "tmDigitizedAspectX", "I");
	TEXTMETRICFc.tmOverhang = (*env)->GetFieldID(env, TEXTMETRICFc.clazz, "tmOverhang", "I");
	TEXTMETRICFc.tmWeight = (*env)->GetFieldID(env, TEXTMETRICFc.clazz, "tmWeight", "I");
	TEXTMETRICFc.tmMaxCharWidth = (*env)->GetFieldID(env, TEXTMETRICFc.clazz, "tmMaxCharWidth", "I");
	TEXTMETRICFc.tmAveCharWidth = (*env)->GetFieldID(env, TEXTMETRICFc.clazz, "tmAveCharWidth", "I");
	TEXTMETRICFc.tmExternalLeading = (*env)->GetFieldID(env, TEXTMETRICFc.clazz, "tmExternalLeading", "I");
	TEXTMETRICFc.tmInternalLeading = (*env)->GetFieldID(env, TEXTMETRICFc.clazz, "tmInternalLeading", "I");
	TEXTMETRICFc.tmDescent = (*env)->GetFieldID(env, TEXTMETRICFc.clazz, "tmDescent", "I");
	TEXTMETRICFc.tmAscent = (*env)->GetFieldID(env, TEXTMETRICFc.clazz, "tmAscent", "I");
	TEXTMETRICFc.tmHeight = (*env)->GetFieldID(env, TEXTMETRICFc.clazz, "tmHeight", "I");
	TEXTMETRICFc.cached = 1;
}

TEXTMETRIC *getTEXTMETRICFields(JNIEnv *env, jobject lpObject, TEXTMETRIC *lpStruct)
{
	if (!TEXTMETRICFc.cached) cacheTEXTMETRICFields(env, lpObject);
	lpStruct->tmCharSet = (*env)->GetByteField(env, lpObject, TEXTMETRICFc.tmCharSet);
	lpStruct->tmPitchAndFamily = (*env)->GetByteField(env, lpObject, TEXTMETRICFc.tmPitchAndFamily);
	lpStruct->tmStruckOut = (*env)->GetByteField(env, lpObject, TEXTMETRICFc.tmStruckOut);
	lpStruct->tmUnderlined = (*env)->GetByteField(env, lpObject, TEXTMETRICFc.tmUnderlined);
	lpStruct->tmItalic = (*env)->GetByteField(env, lpObject, TEXTMETRICFc.tmItalic);
	lpStruct->tmDigitizedAspectY = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmDigitizedAspectY);
	lpStruct->tmDigitizedAspectX = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmDigitizedAspectX);
	lpStruct->tmOverhang = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmOverhang);
	lpStruct->tmWeight = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmWeight);
	lpStruct->tmMaxCharWidth = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmMaxCharWidth);
	lpStruct->tmAveCharWidth = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmAveCharWidth);
	lpStruct->tmExternalLeading = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmExternalLeading);
	lpStruct->tmInternalLeading = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmInternalLeading);
	lpStruct->tmDescent = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmDescent);
	lpStruct->tmAscent = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmAscent);
	lpStruct->tmHeight = (*env)->GetIntField(env, lpObject, TEXTMETRICFc.tmHeight);
	return lpStruct;
}

void setTEXTMETRICFields(JNIEnv *env, jobject lpObject, TEXTMETRIC *lpStruct)
{
	if (!TEXTMETRICFc.cached) cacheTEXTMETRICFields(env, lpObject);
	(*env)->SetByteField(env, lpObject, TEXTMETRICFc.tmCharSet, (jbyte)lpStruct->tmCharSet);
	(*env)->SetByteField(env, lpObject, TEXTMETRICFc.tmPitchAndFamily, (jbyte)lpStruct->tmPitchAndFamily);
	(*env)->SetByteField(env, lpObject, TEXTMETRICFc.tmStruckOut, (jbyte)lpStruct->tmStruckOut);
	(*env)->SetByteField(env, lpObject, TEXTMETRICFc.tmUnderlined, (jbyte)lpStruct->tmUnderlined);
	(*env)->SetByteField(env, lpObject, TEXTMETRICFc.tmItalic, (jbyte)lpStruct->tmItalic);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmDigitizedAspectY, (jint)lpStruct->tmDigitizedAspectY);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmDigitizedAspectX, (jint)lpStruct->tmDigitizedAspectX);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmOverhang, (jint)lpStruct->tmOverhang);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmWeight, (jint)lpStruct->tmWeight);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmMaxCharWidth, (jint)lpStruct->tmMaxCharWidth);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmAveCharWidth, (jint)lpStruct->tmAveCharWidth);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmExternalLeading, (jint)lpStruct->tmExternalLeading);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmInternalLeading, (jint)lpStruct->tmInternalLeading);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmDescent, (jint)lpStruct->tmDescent);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmAscent, (jint)lpStruct->tmAscent);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmHeight, (jint)lpStruct->tmHeight);
}
#endif

#ifndef NO_TEXTMETRICA
typedef struct TEXTMETRICA_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID tmBreakChar, tmDefaultChar, tmLastChar, tmFirstChar;
} TEXTMETRICA_FID_CACHE;

TEXTMETRICA_FID_CACHE TEXTMETRICAFc;

void cacheTEXTMETRICAFields(JNIEnv *env, jobject lpObject)
{
	if (TEXTMETRICAFc.cached) return;
	cacheTEXTMETRICFields(env, lpObject);
	TEXTMETRICAFc.clazz = (*env)->GetObjectClass(env, lpObject);
	TEXTMETRICAFc.tmBreakChar = (*env)->GetFieldID(env, TEXTMETRICAFc.clazz, "tmBreakChar", "B");
	TEXTMETRICAFc.tmDefaultChar = (*env)->GetFieldID(env, TEXTMETRICAFc.clazz, "tmDefaultChar", "B");
	TEXTMETRICAFc.tmLastChar = (*env)->GetFieldID(env, TEXTMETRICAFc.clazz, "tmLastChar", "B");
	TEXTMETRICAFc.tmFirstChar = (*env)->GetFieldID(env, TEXTMETRICAFc.clazz, "tmFirstChar", "B");
	TEXTMETRICAFc.cached = 1;
}

TEXTMETRICA *getTEXTMETRICAFields(JNIEnv *env, jobject lpObject, TEXTMETRICA *lpStruct)
{
	if (!TEXTMETRICAFc.cached) cacheTEXTMETRICAFields(env, lpObject);
	getTEXTMETRICFields(env, lpObject, (TEXTMETRIC *)lpStruct);
	lpStruct->tmBreakChar = (*env)->GetByteField(env, lpObject, TEXTMETRICAFc.tmBreakChar);
	lpStruct->tmDefaultChar = (*env)->GetByteField(env, lpObject, TEXTMETRICAFc.tmDefaultChar);
	lpStruct->tmLastChar = (*env)->GetByteField(env, lpObject, TEXTMETRICAFc.tmLastChar);
	lpStruct->tmFirstChar = (*env)->GetByteField(env, lpObject, TEXTMETRICAFc.tmFirstChar);
	return lpStruct;
}

void setTEXTMETRICAFields(JNIEnv *env, jobject lpObject, TEXTMETRICA *lpStruct)
{
	if (!TEXTMETRICAFc.cached) cacheTEXTMETRICAFields(env, lpObject);
	setTEXTMETRICFields(env, lpObject, (TEXTMETRIC *)lpStruct);
	(*env)->SetByteField(env, lpObject, TEXTMETRICAFc.tmBreakChar, (jbyte)lpStruct->tmBreakChar);
	(*env)->SetByteField(env, lpObject, TEXTMETRICAFc.tmDefaultChar, (jbyte)lpStruct->tmDefaultChar);
	(*env)->SetByteField(env, lpObject, TEXTMETRICAFc.tmLastChar, (jbyte)lpStruct->tmLastChar);
	(*env)->SetByteField(env, lpObject, TEXTMETRICAFc.tmFirstChar, (jbyte)lpStruct->tmFirstChar);
}
#endif

#ifndef NO_TEXTMETRICW
typedef struct TEXTMETRICW_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID tmBreakChar, tmDefaultChar, tmLastChar, tmFirstChar;
} TEXTMETRICW_FID_CACHE;

TEXTMETRICW_FID_CACHE TEXTMETRICWFc;

void cacheTEXTMETRICWFields(JNIEnv *env, jobject lpObject)
{
	if (TEXTMETRICWFc.cached) return;
	cacheTEXTMETRICFields(env, lpObject);
	TEXTMETRICWFc.clazz = (*env)->GetObjectClass(env, lpObject);
	TEXTMETRICWFc.tmBreakChar = (*env)->GetFieldID(env, TEXTMETRICWFc.clazz, "tmBreakChar", "C");
	TEXTMETRICWFc.tmDefaultChar = (*env)->GetFieldID(env, TEXTMETRICWFc.clazz, "tmDefaultChar", "C");
	TEXTMETRICWFc.tmLastChar = (*env)->GetFieldID(env, TEXTMETRICWFc.clazz, "tmLastChar", "C");
	TEXTMETRICWFc.tmFirstChar = (*env)->GetFieldID(env, TEXTMETRICWFc.clazz, "tmFirstChar", "C");
	TEXTMETRICWFc.cached = 1;
}

TEXTMETRICW *getTEXTMETRICWFields(JNIEnv *env, jobject lpObject, TEXTMETRICW *lpStruct)
{
	if (!TEXTMETRICWFc.cached) cacheTEXTMETRICWFields(env, lpObject);
	getTEXTMETRICFields(env, lpObject, (TEXTMETRIC *)lpStruct);
	lpStruct->tmBreakChar = (*env)->GetCharField(env, lpObject, TEXTMETRICWFc.tmBreakChar);
	lpStruct->tmDefaultChar = (*env)->GetCharField(env, lpObject, TEXTMETRICWFc.tmDefaultChar);
	lpStruct->tmLastChar = (*env)->GetCharField(env, lpObject, TEXTMETRICWFc.tmLastChar);
	lpStruct->tmFirstChar = (*env)->GetCharField(env, lpObject, TEXTMETRICWFc.tmFirstChar);
	return lpStruct;
}

void setTEXTMETRICWFields(JNIEnv *env, jobject lpObject, TEXTMETRICW *lpStruct)
{
	if (!TEXTMETRICWFc.cached) cacheTEXTMETRICWFields(env, lpObject);
	setTEXTMETRICFields(env, lpObject, (TEXTMETRIC *)lpStruct);
	(*env)->SetCharField(env, lpObject, TEXTMETRICWFc.tmBreakChar, (jchar)lpStruct->tmBreakChar);
	(*env)->SetCharField(env, lpObject, TEXTMETRICWFc.tmDefaultChar, (jchar)lpStruct->tmDefaultChar);
	(*env)->SetCharField(env, lpObject, TEXTMETRICWFc.tmLastChar, (jchar)lpStruct->tmLastChar);
	(*env)->SetCharField(env, lpObject, TEXTMETRICWFc.tmFirstChar, (jchar)lpStruct->tmFirstChar);
}
#endif

#ifndef NO_TOOLINFO
typedef struct TOOLINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lParam, lpszText, hinst, bottom, right, top, left, uId, hwnd, uFlags, cbSize;
} TOOLINFO_FID_CACHE;

TOOLINFO_FID_CACHE TOOLINFOFc;

void cacheTOOLINFOFields(JNIEnv *env, jobject lpObject)
{
	if (TOOLINFOFc.cached) return;
	TOOLINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	TOOLINFOFc.lParam = (*env)->GetFieldID(env, TOOLINFOFc.clazz, "lParam", "I");
	TOOLINFOFc.lpszText = (*env)->GetFieldID(env, TOOLINFOFc.clazz, "lpszText", "I");
	TOOLINFOFc.hinst = (*env)->GetFieldID(env, TOOLINFOFc.clazz, "hinst", "I");
	TOOLINFOFc.bottom = (*env)->GetFieldID(env, TOOLINFOFc.clazz, "bottom", "I");
	TOOLINFOFc.right = (*env)->GetFieldID(env, TOOLINFOFc.clazz, "right", "I");
	TOOLINFOFc.top = (*env)->GetFieldID(env, TOOLINFOFc.clazz, "top", "I");
	TOOLINFOFc.left = (*env)->GetFieldID(env, TOOLINFOFc.clazz, "left", "I");
	TOOLINFOFc.uId = (*env)->GetFieldID(env, TOOLINFOFc.clazz, "uId", "I");
	TOOLINFOFc.hwnd = (*env)->GetFieldID(env, TOOLINFOFc.clazz, "hwnd", "I");
	TOOLINFOFc.uFlags = (*env)->GetFieldID(env, TOOLINFOFc.clazz, "uFlags", "I");
	TOOLINFOFc.cbSize = (*env)->GetFieldID(env, TOOLINFOFc.clazz, "cbSize", "I");
	TOOLINFOFc.cached = 1;
}

TOOLINFO *getTOOLINFOFields(JNIEnv *env, jobject lpObject, TOOLINFO *lpStruct)
{
	if (!TOOLINFOFc.cached) cacheTOOLINFOFields(env, lpObject);
	lpStruct->lParam = (*env)->GetIntField(env, lpObject, TOOLINFOFc.lParam);
	lpStruct->lpszText = (LPTSTR)(*env)->GetIntField(env, lpObject, TOOLINFOFc.lpszText);
	lpStruct->hinst = (HINSTANCE)(*env)->GetIntField(env, lpObject, TOOLINFOFc.hinst);
	lpStruct->rect.bottom = (*env)->GetIntField(env, lpObject, TOOLINFOFc.bottom);
	lpStruct->rect.right = (*env)->GetIntField(env, lpObject, TOOLINFOFc.right);
	lpStruct->rect.top = (*env)->GetIntField(env, lpObject, TOOLINFOFc.top);
	lpStruct->rect.left = (*env)->GetIntField(env, lpObject, TOOLINFOFc.left);
	lpStruct->uId = (*env)->GetIntField(env, lpObject, TOOLINFOFc.uId);
	lpStruct->hwnd = (HWND)(*env)->GetIntField(env, lpObject, TOOLINFOFc.hwnd);
	lpStruct->uFlags = (*env)->GetIntField(env, lpObject, TOOLINFOFc.uFlags);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, TOOLINFOFc.cbSize);
	return lpStruct;
}

void setTOOLINFOFields(JNIEnv *env, jobject lpObject, TOOLINFO *lpStruct)
{
	if (!TOOLINFOFc.cached) cacheTOOLINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, TOOLINFOFc.lParam, (jint)lpStruct->lParam);
	(*env)->SetIntField(env, lpObject, TOOLINFOFc.lpszText, (jint)lpStruct->lpszText);
	(*env)->SetIntField(env, lpObject, TOOLINFOFc.hinst, (jint)lpStruct->hinst);
	(*env)->SetIntField(env, lpObject, TOOLINFOFc.bottom, (jint)lpStruct->rect.bottom);
	(*env)->SetIntField(env, lpObject, TOOLINFOFc.right, (jint)lpStruct->rect.right);
	(*env)->SetIntField(env, lpObject, TOOLINFOFc.top, (jint)lpStruct->rect.top);
	(*env)->SetIntField(env, lpObject, TOOLINFOFc.left, (jint)lpStruct->rect.left);
	(*env)->SetIntField(env, lpObject, TOOLINFOFc.uId, (jint)lpStruct->uId);
	(*env)->SetIntField(env, lpObject, TOOLINFOFc.hwnd, (jint)lpStruct->hwnd);
	(*env)->SetIntField(env, lpObject, TOOLINFOFc.uFlags, (jint)lpStruct->uFlags);
	(*env)->SetIntField(env, lpObject, TOOLINFOFc.cbSize, (jint)lpStruct->cbSize);
}
#endif

#ifndef NO_TRACKMOUSEEVENT
typedef struct TRACKMOUSEEVENT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID dwHoverTime, hwndTrack, dwFlags, cbSize;
} TRACKMOUSEEVENT_FID_CACHE;

TRACKMOUSEEVENT_FID_CACHE TRACKMOUSEEVENTFc;

void cacheTRACKMOUSEEVENTFields(JNIEnv *env, jobject lpObject)
{
	if (TRACKMOUSEEVENTFc.cached) return;
	TRACKMOUSEEVENTFc.clazz = (*env)->GetObjectClass(env, lpObject);
	TRACKMOUSEEVENTFc.dwHoverTime = (*env)->GetFieldID(env, TRACKMOUSEEVENTFc.clazz, "dwHoverTime", "I");
	TRACKMOUSEEVENTFc.hwndTrack = (*env)->GetFieldID(env, TRACKMOUSEEVENTFc.clazz, "hwndTrack", "I");
	TRACKMOUSEEVENTFc.dwFlags = (*env)->GetFieldID(env, TRACKMOUSEEVENTFc.clazz, "dwFlags", "I");
	TRACKMOUSEEVENTFc.cbSize = (*env)->GetFieldID(env, TRACKMOUSEEVENTFc.clazz, "cbSize", "I");
	TRACKMOUSEEVENTFc.cached = 1;
}

TRACKMOUSEEVENT *getTRACKMOUSEEVENTFields(JNIEnv *env, jobject lpObject, TRACKMOUSEEVENT *lpStruct)
{
	if (!TRACKMOUSEEVENTFc.cached) cacheTRACKMOUSEEVENTFields(env, lpObject);
	lpStruct->dwHoverTime = (*env)->GetIntField(env, lpObject, TRACKMOUSEEVENTFc.dwHoverTime);
	lpStruct->hwndTrack = (HWND)(*env)->GetIntField(env, lpObject, TRACKMOUSEEVENTFc.hwndTrack);
	lpStruct->dwFlags = (*env)->GetIntField(env, lpObject, TRACKMOUSEEVENTFc.dwFlags);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, TRACKMOUSEEVENTFc.cbSize);
	return lpStruct;
}

void setTRACKMOUSEEVENTFields(JNIEnv *env, jobject lpObject, TRACKMOUSEEVENT *lpStruct)
{
	if (!TRACKMOUSEEVENTFc.cached) cacheTRACKMOUSEEVENTFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, TRACKMOUSEEVENTFc.dwHoverTime, (jint)lpStruct->dwHoverTime);
	(*env)->SetIntField(env, lpObject, TRACKMOUSEEVENTFc.hwndTrack, (jint)lpStruct->hwndTrack);
	(*env)->SetIntField(env, lpObject, TRACKMOUSEEVENTFc.dwFlags, (jint)lpStruct->dwFlags);
	(*env)->SetIntField(env, lpObject, TRACKMOUSEEVENTFc.cbSize, (jint)lpStruct->cbSize);
}
#endif

#ifndef NO_TRIVERTEX
typedef struct TRIVERTEX_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID Alpha, Blue, Green, Red, y, x;
} TRIVERTEX_FID_CACHE;

TRIVERTEX_FID_CACHE TRIVERTEXFc;

void cacheTRIVERTEXFields(JNIEnv *env, jobject lpObject)
{
	if (TRIVERTEXFc.cached) return;
	TRIVERTEXFc.clazz = (*env)->GetObjectClass(env, lpObject);
	TRIVERTEXFc.Alpha = (*env)->GetFieldID(env, TRIVERTEXFc.clazz, "Alpha", "S");
	TRIVERTEXFc.Blue = (*env)->GetFieldID(env, TRIVERTEXFc.clazz, "Blue", "S");
	TRIVERTEXFc.Green = (*env)->GetFieldID(env, TRIVERTEXFc.clazz, "Green", "S");
	TRIVERTEXFc.Red = (*env)->GetFieldID(env, TRIVERTEXFc.clazz, "Red", "S");
	TRIVERTEXFc.y = (*env)->GetFieldID(env, TRIVERTEXFc.clazz, "y", "I");
	TRIVERTEXFc.x = (*env)->GetFieldID(env, TRIVERTEXFc.clazz, "x", "I");
	TRIVERTEXFc.cached = 1;
}

TRIVERTEX *getTRIVERTEXFields(JNIEnv *env, jobject lpObject, TRIVERTEX *lpStruct)
{
	if (!TRIVERTEXFc.cached) cacheTRIVERTEXFields(env, lpObject);
	lpStruct->Alpha = (*env)->GetShortField(env, lpObject, TRIVERTEXFc.Alpha);
	lpStruct->Blue = (*env)->GetShortField(env, lpObject, TRIVERTEXFc.Blue);
	lpStruct->Green = (*env)->GetShortField(env, lpObject, TRIVERTEXFc.Green);
	lpStruct->Red = (*env)->GetShortField(env, lpObject, TRIVERTEXFc.Red);
	lpStruct->y = (*env)->GetIntField(env, lpObject, TRIVERTEXFc.y);
	lpStruct->x = (*env)->GetIntField(env, lpObject, TRIVERTEXFc.x);
	return lpStruct;
}

void setTRIVERTEXFields(JNIEnv *env, jobject lpObject, TRIVERTEX *lpStruct)
{
	if (!TRIVERTEXFc.cached) cacheTRIVERTEXFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, TRIVERTEXFc.Alpha, (jshort)lpStruct->Alpha);
	(*env)->SetShortField(env, lpObject, TRIVERTEXFc.Blue, (jshort)lpStruct->Blue);
	(*env)->SetShortField(env, lpObject, TRIVERTEXFc.Green, (jshort)lpStruct->Green);
	(*env)->SetShortField(env, lpObject, TRIVERTEXFc.Red, (jshort)lpStruct->Red);
	(*env)->SetIntField(env, lpObject, TRIVERTEXFc.y, (jint)lpStruct->y);
	(*env)->SetIntField(env, lpObject, TRIVERTEXFc.x, (jint)lpStruct->x);
}
#endif

#ifndef NO_TVHITTESTINFO
typedef struct TVHITTESTINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID hItem, flags, y, x;
} TVHITTESTINFO_FID_CACHE;

TVHITTESTINFO_FID_CACHE TVHITTESTINFOFc;

void cacheTVHITTESTINFOFields(JNIEnv *env, jobject lpObject)
{
	if (TVHITTESTINFOFc.cached) return;
	TVHITTESTINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	TVHITTESTINFOFc.hItem = (*env)->GetFieldID(env, TVHITTESTINFOFc.clazz, "hItem", "I");
	TVHITTESTINFOFc.flags = (*env)->GetFieldID(env, TVHITTESTINFOFc.clazz, "flags", "I");
	TVHITTESTINFOFc.y = (*env)->GetFieldID(env, TVHITTESTINFOFc.clazz, "y", "I");
	TVHITTESTINFOFc.x = (*env)->GetFieldID(env, TVHITTESTINFOFc.clazz, "x", "I");
	TVHITTESTINFOFc.cached = 1;
}

TVHITTESTINFO *getTVHITTESTINFOFields(JNIEnv *env, jobject lpObject, TVHITTESTINFO *lpStruct)
{
	if (!TVHITTESTINFOFc.cached) cacheTVHITTESTINFOFields(env, lpObject);
	lpStruct->hItem = (HTREEITEM)(*env)->GetIntField(env, lpObject, TVHITTESTINFOFc.hItem);
	lpStruct->flags = (*env)->GetIntField(env, lpObject, TVHITTESTINFOFc.flags);
	lpStruct->pt.y = (*env)->GetIntField(env, lpObject, TVHITTESTINFOFc.y);
	lpStruct->pt.x = (*env)->GetIntField(env, lpObject, TVHITTESTINFOFc.x);
	return lpStruct;
}

void setTVHITTESTINFOFields(JNIEnv *env, jobject lpObject, TVHITTESTINFO *lpStruct)
{
	if (!TVHITTESTINFOFc.cached) cacheTVHITTESTINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, TVHITTESTINFOFc.hItem, (jint)lpStruct->hItem);
	(*env)->SetIntField(env, lpObject, TVHITTESTINFOFc.flags, (jint)lpStruct->flags);
	(*env)->SetIntField(env, lpObject, TVHITTESTINFOFc.y, (jint)lpStruct->pt.y);
	(*env)->SetIntField(env, lpObject, TVHITTESTINFOFc.x, (jint)lpStruct->pt.x);
}
#endif

#ifndef NO_TVINSERTSTRUCT
typedef struct TVINSERTSTRUCT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lParam, cChildren, iSelectedImage, iImage, cchTextMax, pszText, stateMask, state, hItem, mask, hInsertAfter, hParent;
} TVINSERTSTRUCT_FID_CACHE;

TVINSERTSTRUCT_FID_CACHE TVINSERTSTRUCTFc;

void cacheTVINSERTSTRUCTFields(JNIEnv *env, jobject lpObject)
{
	if (TVINSERTSTRUCTFc.cached) return;
	TVINSERTSTRUCTFc.clazz = (*env)->GetObjectClass(env, lpObject);
	TVINSERTSTRUCTFc.lParam = (*env)->GetFieldID(env, TVINSERTSTRUCTFc.clazz, "lParam", "I");
	TVINSERTSTRUCTFc.cChildren = (*env)->GetFieldID(env, TVINSERTSTRUCTFc.clazz, "cChildren", "I");
	TVINSERTSTRUCTFc.iSelectedImage = (*env)->GetFieldID(env, TVINSERTSTRUCTFc.clazz, "iSelectedImage", "I");
	TVINSERTSTRUCTFc.iImage = (*env)->GetFieldID(env, TVINSERTSTRUCTFc.clazz, "iImage", "I");
	TVINSERTSTRUCTFc.cchTextMax = (*env)->GetFieldID(env, TVINSERTSTRUCTFc.clazz, "cchTextMax", "I");
	TVINSERTSTRUCTFc.pszText = (*env)->GetFieldID(env, TVINSERTSTRUCTFc.clazz, "pszText", "I");
	TVINSERTSTRUCTFc.stateMask = (*env)->GetFieldID(env, TVINSERTSTRUCTFc.clazz, "stateMask", "I");
	TVINSERTSTRUCTFc.state = (*env)->GetFieldID(env, TVINSERTSTRUCTFc.clazz, "state", "I");
	TVINSERTSTRUCTFc.hItem = (*env)->GetFieldID(env, TVINSERTSTRUCTFc.clazz, "hItem", "I");
	TVINSERTSTRUCTFc.mask = (*env)->GetFieldID(env, TVINSERTSTRUCTFc.clazz, "mask", "I");
	TVINSERTSTRUCTFc.hInsertAfter = (*env)->GetFieldID(env, TVINSERTSTRUCTFc.clazz, "hInsertAfter", "I");
	TVINSERTSTRUCTFc.hParent = (*env)->GetFieldID(env, TVINSERTSTRUCTFc.clazz, "hParent", "I");
	TVINSERTSTRUCTFc.cached = 1;
}

TVINSERTSTRUCT *getTVINSERTSTRUCTFields(JNIEnv *env, jobject lpObject, TVINSERTSTRUCT *lpStruct)
{
	if (!TVINSERTSTRUCTFc.cached) cacheTVINSERTSTRUCTFields(env, lpObject);
	lpStruct->item.lParam = (*env)->GetIntField(env, lpObject, TVINSERTSTRUCTFc.lParam);
	lpStruct->item.cChildren = (*env)->GetIntField(env, lpObject, TVINSERTSTRUCTFc.cChildren);
	lpStruct->item.iSelectedImage = (*env)->GetIntField(env, lpObject, TVINSERTSTRUCTFc.iSelectedImage);
	lpStruct->item.iImage = (*env)->GetIntField(env, lpObject, TVINSERTSTRUCTFc.iImage);
	lpStruct->item.cchTextMax = (*env)->GetIntField(env, lpObject, TVINSERTSTRUCTFc.cchTextMax);
	lpStruct->item.pszText = (LPTSTR)(*env)->GetIntField(env, lpObject, TVINSERTSTRUCTFc.pszText);
	lpStruct->item.stateMask = (*env)->GetIntField(env, lpObject, TVINSERTSTRUCTFc.stateMask);
	lpStruct->item.state = (*env)->GetIntField(env, lpObject, TVINSERTSTRUCTFc.state);
	lpStruct->item.hItem = (HTREEITEM)(*env)->GetIntField(env, lpObject, TVINSERTSTRUCTFc.hItem);
	lpStruct->item.mask = (*env)->GetIntField(env, lpObject, TVINSERTSTRUCTFc.mask);
	lpStruct->hInsertAfter = (HTREEITEM)(*env)->GetIntField(env, lpObject, TVINSERTSTRUCTFc.hInsertAfter);
	lpStruct->hParent = (HTREEITEM)(*env)->GetIntField(env, lpObject, TVINSERTSTRUCTFc.hParent);
	return lpStruct;
}

void setTVINSERTSTRUCTFields(JNIEnv *env, jobject lpObject, TVINSERTSTRUCT *lpStruct)
{
	if (!TVINSERTSTRUCTFc.cached) cacheTVINSERTSTRUCTFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, TVINSERTSTRUCTFc.lParam, (jint)lpStruct->item.lParam);
	(*env)->SetIntField(env, lpObject, TVINSERTSTRUCTFc.cChildren, (jint)lpStruct->item.cChildren);
	(*env)->SetIntField(env, lpObject, TVINSERTSTRUCTFc.iSelectedImage, (jint)lpStruct->item.iSelectedImage);
	(*env)->SetIntField(env, lpObject, TVINSERTSTRUCTFc.iImage, (jint)lpStruct->item.iImage);
	(*env)->SetIntField(env, lpObject, TVINSERTSTRUCTFc.cchTextMax, (jint)lpStruct->item.cchTextMax);
	(*env)->SetIntField(env, lpObject, TVINSERTSTRUCTFc.pszText, (jint)lpStruct->item.pszText);
	(*env)->SetIntField(env, lpObject, TVINSERTSTRUCTFc.stateMask, (jint)lpStruct->item.stateMask);
	(*env)->SetIntField(env, lpObject, TVINSERTSTRUCTFc.state, (jint)lpStruct->item.state);
	(*env)->SetIntField(env, lpObject, TVINSERTSTRUCTFc.hItem, (jint)lpStruct->item.hItem);
	(*env)->SetIntField(env, lpObject, TVINSERTSTRUCTFc.mask, (jint)lpStruct->item.mask);
	(*env)->SetIntField(env, lpObject, TVINSERTSTRUCTFc.hInsertAfter, (jint)lpStruct->hInsertAfter);
	(*env)->SetIntField(env, lpObject, TVINSERTSTRUCTFc.hParent, (jint)lpStruct->hParent);
}
#endif

#ifndef NO_TVITEM
typedef struct TVITEM_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lParam, cChildren, iSelectedImage, iImage, cchTextMax, pszText, stateMask, state, hItem, mask;
} TVITEM_FID_CACHE;

TVITEM_FID_CACHE TVITEMFc;

void cacheTVITEMFields(JNIEnv *env, jobject lpObject)
{
	if (TVITEMFc.cached) return;
	TVITEMFc.clazz = (*env)->GetObjectClass(env, lpObject);
	TVITEMFc.lParam = (*env)->GetFieldID(env, TVITEMFc.clazz, "lParam", "I");
	TVITEMFc.cChildren = (*env)->GetFieldID(env, TVITEMFc.clazz, "cChildren", "I");
	TVITEMFc.iSelectedImage = (*env)->GetFieldID(env, TVITEMFc.clazz, "iSelectedImage", "I");
	TVITEMFc.iImage = (*env)->GetFieldID(env, TVITEMFc.clazz, "iImage", "I");
	TVITEMFc.cchTextMax = (*env)->GetFieldID(env, TVITEMFc.clazz, "cchTextMax", "I");
	TVITEMFc.pszText = (*env)->GetFieldID(env, TVITEMFc.clazz, "pszText", "I");
	TVITEMFc.stateMask = (*env)->GetFieldID(env, TVITEMFc.clazz, "stateMask", "I");
	TVITEMFc.state = (*env)->GetFieldID(env, TVITEMFc.clazz, "state", "I");
	TVITEMFc.hItem = (*env)->GetFieldID(env, TVITEMFc.clazz, "hItem", "I");
	TVITEMFc.mask = (*env)->GetFieldID(env, TVITEMFc.clazz, "mask", "I");
	TVITEMFc.cached = 1;
}

TVITEM *getTVITEMFields(JNIEnv *env, jobject lpObject, TVITEM *lpStruct)
{
	if (!TVITEMFc.cached) cacheTVITEMFields(env, lpObject);
	lpStruct->lParam = (*env)->GetIntField(env, lpObject, TVITEMFc.lParam);
	lpStruct->cChildren = (*env)->GetIntField(env, lpObject, TVITEMFc.cChildren);
	lpStruct->iSelectedImage = (*env)->GetIntField(env, lpObject, TVITEMFc.iSelectedImage);
	lpStruct->iImage = (*env)->GetIntField(env, lpObject, TVITEMFc.iImage);
	lpStruct->cchTextMax = (*env)->GetIntField(env, lpObject, TVITEMFc.cchTextMax);
	lpStruct->pszText = (LPTSTR)(*env)->GetIntField(env, lpObject, TVITEMFc.pszText);
	lpStruct->stateMask = (*env)->GetIntField(env, lpObject, TVITEMFc.stateMask);
	lpStruct->state = (*env)->GetIntField(env, lpObject, TVITEMFc.state);
	lpStruct->hItem = (HTREEITEM)(*env)->GetIntField(env, lpObject, TVITEMFc.hItem);
	lpStruct->mask = (*env)->GetIntField(env, lpObject, TVITEMFc.mask);
	return lpStruct;
}

void setTVITEMFields(JNIEnv *env, jobject lpObject, TVITEM *lpStruct)
{
	if (!TVITEMFc.cached) cacheTVITEMFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, TVITEMFc.lParam, (jint)lpStruct->lParam);
	(*env)->SetIntField(env, lpObject, TVITEMFc.cChildren, (jint)lpStruct->cChildren);
	(*env)->SetIntField(env, lpObject, TVITEMFc.iSelectedImage, (jint)lpStruct->iSelectedImage);
	(*env)->SetIntField(env, lpObject, TVITEMFc.iImage, (jint)lpStruct->iImage);
	(*env)->SetIntField(env, lpObject, TVITEMFc.cchTextMax, (jint)lpStruct->cchTextMax);
	(*env)->SetIntField(env, lpObject, TVITEMFc.pszText, (jint)lpStruct->pszText);
	(*env)->SetIntField(env, lpObject, TVITEMFc.stateMask, (jint)lpStruct->stateMask);
	(*env)->SetIntField(env, lpObject, TVITEMFc.state, (jint)lpStruct->state);
	(*env)->SetIntField(env, lpObject, TVITEMFc.hItem, (jint)lpStruct->hItem);
	(*env)->SetIntField(env, lpObject, TVITEMFc.mask, (jint)lpStruct->mask);
}
#endif

#ifndef NO_WINDOWPLACEMENT
typedef struct WINDOWPLACEMENT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID bottom, right, top, left, ptMaxPosition_y, ptMaxPosition_x, ptMinPosition_y, ptMinPosition_x, showCmd, flags, length;
} WINDOWPLACEMENT_FID_CACHE;

WINDOWPLACEMENT_FID_CACHE WINDOWPLACEMENTFc;

void cacheWINDOWPLACEMENTFields(JNIEnv *env, jobject lpObject)
{
	if (WINDOWPLACEMENTFc.cached) return;
	WINDOWPLACEMENTFc.clazz = (*env)->GetObjectClass(env, lpObject);
	WINDOWPLACEMENTFc.bottom = (*env)->GetFieldID(env, WINDOWPLACEMENTFc.clazz, "bottom", "I");
	WINDOWPLACEMENTFc.right = (*env)->GetFieldID(env, WINDOWPLACEMENTFc.clazz, "right", "I");
	WINDOWPLACEMENTFc.top = (*env)->GetFieldID(env, WINDOWPLACEMENTFc.clazz, "top", "I");
	WINDOWPLACEMENTFc.left = (*env)->GetFieldID(env, WINDOWPLACEMENTFc.clazz, "left", "I");
	WINDOWPLACEMENTFc.ptMaxPosition_y = (*env)->GetFieldID(env, WINDOWPLACEMENTFc.clazz, "ptMaxPosition_y", "I");
	WINDOWPLACEMENTFc.ptMaxPosition_x = (*env)->GetFieldID(env, WINDOWPLACEMENTFc.clazz, "ptMaxPosition_x", "I");
	WINDOWPLACEMENTFc.ptMinPosition_y = (*env)->GetFieldID(env, WINDOWPLACEMENTFc.clazz, "ptMinPosition_y", "I");
	WINDOWPLACEMENTFc.ptMinPosition_x = (*env)->GetFieldID(env, WINDOWPLACEMENTFc.clazz, "ptMinPosition_x", "I");
	WINDOWPLACEMENTFc.showCmd = (*env)->GetFieldID(env, WINDOWPLACEMENTFc.clazz, "showCmd", "I");
	WINDOWPLACEMENTFc.flags = (*env)->GetFieldID(env, WINDOWPLACEMENTFc.clazz, "flags", "I");
	WINDOWPLACEMENTFc.length = (*env)->GetFieldID(env, WINDOWPLACEMENTFc.clazz, "length", "I");
	WINDOWPLACEMENTFc.cached = 1;
}

WINDOWPLACEMENT *getWINDOWPLACEMENTFields(JNIEnv *env, jobject lpObject, WINDOWPLACEMENT *lpStruct)
{
	if (!WINDOWPLACEMENTFc.cached) cacheWINDOWPLACEMENTFields(env, lpObject);
	lpStruct->rcNormalPosition.bottom = (*env)->GetIntField(env, lpObject, WINDOWPLACEMENTFc.bottom);
	lpStruct->rcNormalPosition.right = (*env)->GetIntField(env, lpObject, WINDOWPLACEMENTFc.right);
	lpStruct->rcNormalPosition.top = (*env)->GetIntField(env, lpObject, WINDOWPLACEMENTFc.top);
	lpStruct->rcNormalPosition.left = (*env)->GetIntField(env, lpObject, WINDOWPLACEMENTFc.left);
	lpStruct->ptMaxPosition.y = (*env)->GetIntField(env, lpObject, WINDOWPLACEMENTFc.ptMaxPosition_y);
	lpStruct->ptMaxPosition.x = (*env)->GetIntField(env, lpObject, WINDOWPLACEMENTFc.ptMaxPosition_x);
	lpStruct->ptMinPosition.y = (*env)->GetIntField(env, lpObject, WINDOWPLACEMENTFc.ptMinPosition_y);
	lpStruct->ptMinPosition.x = (*env)->GetIntField(env, lpObject, WINDOWPLACEMENTFc.ptMinPosition_x);
	lpStruct->showCmd = (*env)->GetIntField(env, lpObject, WINDOWPLACEMENTFc.showCmd);
	lpStruct->flags = (*env)->GetIntField(env, lpObject, WINDOWPLACEMENTFc.flags);
	lpStruct->length = (*env)->GetIntField(env, lpObject, WINDOWPLACEMENTFc.length);
	return lpStruct;
}

void setWINDOWPLACEMENTFields(JNIEnv *env, jobject lpObject, WINDOWPLACEMENT *lpStruct)
{
	if (!WINDOWPLACEMENTFc.cached) cacheWINDOWPLACEMENTFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, WINDOWPLACEMENTFc.bottom, (jint)lpStruct->rcNormalPosition.bottom);
	(*env)->SetIntField(env, lpObject, WINDOWPLACEMENTFc.right, (jint)lpStruct->rcNormalPosition.right);
	(*env)->SetIntField(env, lpObject, WINDOWPLACEMENTFc.top, (jint)lpStruct->rcNormalPosition.top);
	(*env)->SetIntField(env, lpObject, WINDOWPLACEMENTFc.left, (jint)lpStruct->rcNormalPosition.left);
	(*env)->SetIntField(env, lpObject, WINDOWPLACEMENTFc.ptMaxPosition_y, (jint)lpStruct->ptMaxPosition.y);
	(*env)->SetIntField(env, lpObject, WINDOWPLACEMENTFc.ptMaxPosition_x, (jint)lpStruct->ptMaxPosition.x);
	(*env)->SetIntField(env, lpObject, WINDOWPLACEMENTFc.ptMinPosition_y, (jint)lpStruct->ptMinPosition.y);
	(*env)->SetIntField(env, lpObject, WINDOWPLACEMENTFc.ptMinPosition_x, (jint)lpStruct->ptMinPosition.x);
	(*env)->SetIntField(env, lpObject, WINDOWPLACEMENTFc.showCmd, (jint)lpStruct->showCmd);
	(*env)->SetIntField(env, lpObject, WINDOWPLACEMENTFc.flags, (jint)lpStruct->flags);
	(*env)->SetIntField(env, lpObject, WINDOWPLACEMENTFc.length, (jint)lpStruct->length);
}
#endif

#ifndef NO_WINDOWPOS
typedef struct WINDOWPOS_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID flags, cy, cx, y, x, hwndInsertAfter, hwnd;
} WINDOWPOS_FID_CACHE;

WINDOWPOS_FID_CACHE WINDOWPOSFc;

void cacheWINDOWPOSFields(JNIEnv *env, jobject lpObject)
{
	if (WINDOWPOSFc.cached) return;
	WINDOWPOSFc.clazz = (*env)->GetObjectClass(env, lpObject);
	WINDOWPOSFc.flags = (*env)->GetFieldID(env, WINDOWPOSFc.clazz, "flags", "I");
	WINDOWPOSFc.cy = (*env)->GetFieldID(env, WINDOWPOSFc.clazz, "cy", "I");
	WINDOWPOSFc.cx = (*env)->GetFieldID(env, WINDOWPOSFc.clazz, "cx", "I");
	WINDOWPOSFc.y = (*env)->GetFieldID(env, WINDOWPOSFc.clazz, "y", "I");
	WINDOWPOSFc.x = (*env)->GetFieldID(env, WINDOWPOSFc.clazz, "x", "I");
	WINDOWPOSFc.hwndInsertAfter = (*env)->GetFieldID(env, WINDOWPOSFc.clazz, "hwndInsertAfter", "I");
	WINDOWPOSFc.hwnd = (*env)->GetFieldID(env, WINDOWPOSFc.clazz, "hwnd", "I");
	WINDOWPOSFc.cached = 1;
}

WINDOWPOS *getWINDOWPOSFields(JNIEnv *env, jobject lpObject, WINDOWPOS *lpStruct)
{
	if (!WINDOWPOSFc.cached) cacheWINDOWPOSFields(env, lpObject);
	lpStruct->flags = (*env)->GetIntField(env, lpObject, WINDOWPOSFc.flags);
	lpStruct->cy = (*env)->GetIntField(env, lpObject, WINDOWPOSFc.cy);
	lpStruct->cx = (*env)->GetIntField(env, lpObject, WINDOWPOSFc.cx);
	lpStruct->y = (*env)->GetIntField(env, lpObject, WINDOWPOSFc.y);
	lpStruct->x = (*env)->GetIntField(env, lpObject, WINDOWPOSFc.x);
	lpStruct->hwndInsertAfter = (HWND)(*env)->GetIntField(env, lpObject, WINDOWPOSFc.hwndInsertAfter);
	lpStruct->hwnd = (HWND)(*env)->GetIntField(env, lpObject, WINDOWPOSFc.hwnd);
	return lpStruct;
}

void setWINDOWPOSFields(JNIEnv *env, jobject lpObject, WINDOWPOS *lpStruct)
{
	if (!WINDOWPOSFc.cached) cacheWINDOWPOSFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, WINDOWPOSFc.flags, (jint)lpStruct->flags);
	(*env)->SetIntField(env, lpObject, WINDOWPOSFc.cy, (jint)lpStruct->cy);
	(*env)->SetIntField(env, lpObject, WINDOWPOSFc.cx, (jint)lpStruct->cx);
	(*env)->SetIntField(env, lpObject, WINDOWPOSFc.y, (jint)lpStruct->y);
	(*env)->SetIntField(env, lpObject, WINDOWPOSFc.x, (jint)lpStruct->x);
	(*env)->SetIntField(env, lpObject, WINDOWPOSFc.hwndInsertAfter, (jint)lpStruct->hwndInsertAfter);
	(*env)->SetIntField(env, lpObject, WINDOWPOSFc.hwnd, (jint)lpStruct->hwnd);
}
#endif

#ifndef NO_WNDCLASS
typedef struct WNDCLASS_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lpszClassName, lpszMenuName, hbrBackground, hCursor, hIcon, hInstance, cbWndExtra, cbClsExtra, lpfnWndProc, style;
} WNDCLASS_FID_CACHE;

WNDCLASS_FID_CACHE WNDCLASSFc;

void cacheWNDCLASSFields(JNIEnv *env, jobject lpObject)
{
	if (WNDCLASSFc.cached) return;
	WNDCLASSFc.clazz = (*env)->GetObjectClass(env, lpObject);
	WNDCLASSFc.lpszClassName = (*env)->GetFieldID(env, WNDCLASSFc.clazz, "lpszClassName", "I");
	WNDCLASSFc.lpszMenuName = (*env)->GetFieldID(env, WNDCLASSFc.clazz, "lpszMenuName", "I");
	WNDCLASSFc.hbrBackground = (*env)->GetFieldID(env, WNDCLASSFc.clazz, "hbrBackground", "I");
	WNDCLASSFc.hCursor = (*env)->GetFieldID(env, WNDCLASSFc.clazz, "hCursor", "I");
	WNDCLASSFc.hIcon = (*env)->GetFieldID(env, WNDCLASSFc.clazz, "hIcon", "I");
	WNDCLASSFc.hInstance = (*env)->GetFieldID(env, WNDCLASSFc.clazz, "hInstance", "I");
	WNDCLASSFc.cbWndExtra = (*env)->GetFieldID(env, WNDCLASSFc.clazz, "cbWndExtra", "I");
	WNDCLASSFc.cbClsExtra = (*env)->GetFieldID(env, WNDCLASSFc.clazz, "cbClsExtra", "I");
	WNDCLASSFc.lpfnWndProc = (*env)->GetFieldID(env, WNDCLASSFc.clazz, "lpfnWndProc", "I");
	WNDCLASSFc.style = (*env)->GetFieldID(env, WNDCLASSFc.clazz, "style", "I");
	WNDCLASSFc.cached = 1;
}

WNDCLASS *getWNDCLASSFields(JNIEnv *env, jobject lpObject, WNDCLASS *lpStruct)
{
	if (!WNDCLASSFc.cached) cacheWNDCLASSFields(env, lpObject);
	lpStruct->lpszClassName = (LPCTSTR)(*env)->GetIntField(env, lpObject, WNDCLASSFc.lpszClassName);
	lpStruct->lpszMenuName = (LPCTSTR)(*env)->GetIntField(env, lpObject, WNDCLASSFc.lpszMenuName);
	lpStruct->hbrBackground = (HBRUSH)(*env)->GetIntField(env, lpObject, WNDCLASSFc.hbrBackground);
	lpStruct->hCursor = (HCURSOR)(*env)->GetIntField(env, lpObject, WNDCLASSFc.hCursor);
	lpStruct->hIcon = (HICON)(*env)->GetIntField(env, lpObject, WNDCLASSFc.hIcon);
	lpStruct->hInstance = (HINSTANCE)(*env)->GetIntField(env, lpObject, WNDCLASSFc.hInstance);
	lpStruct->cbWndExtra = (*env)->GetIntField(env, lpObject, WNDCLASSFc.cbWndExtra);
	lpStruct->cbClsExtra = (*env)->GetIntField(env, lpObject, WNDCLASSFc.cbClsExtra);
	lpStruct->lpfnWndProc = (WNDPROC)(*env)->GetIntField(env, lpObject, WNDCLASSFc.lpfnWndProc);
	lpStruct->style = (*env)->GetIntField(env, lpObject, WNDCLASSFc.style);
	return lpStruct;
}

void setWNDCLASSFields(JNIEnv *env, jobject lpObject, WNDCLASS *lpStruct)
{
	if (!WNDCLASSFc.cached) cacheWNDCLASSFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, WNDCLASSFc.lpszClassName, (jint)lpStruct->lpszClassName);
	(*env)->SetIntField(env, lpObject, WNDCLASSFc.lpszMenuName, (jint)lpStruct->lpszMenuName);
	(*env)->SetIntField(env, lpObject, WNDCLASSFc.hbrBackground, (jint)lpStruct->hbrBackground);
	(*env)->SetIntField(env, lpObject, WNDCLASSFc.hCursor, (jint)lpStruct->hCursor);
	(*env)->SetIntField(env, lpObject, WNDCLASSFc.hIcon, (jint)lpStruct->hIcon);
	(*env)->SetIntField(env, lpObject, WNDCLASSFc.hInstance, (jint)lpStruct->hInstance);
	(*env)->SetIntField(env, lpObject, WNDCLASSFc.cbWndExtra, (jint)lpStruct->cbWndExtra);
	(*env)->SetIntField(env, lpObject, WNDCLASSFc.cbClsExtra, (jint)lpStruct->cbClsExtra);
	(*env)->SetIntField(env, lpObject, WNDCLASSFc.lpfnWndProc, (jint)lpStruct->lpfnWndProc);
	(*env)->SetIntField(env, lpObject, WNDCLASSFc.style, (jint)lpStruct->style);
}
#endif

