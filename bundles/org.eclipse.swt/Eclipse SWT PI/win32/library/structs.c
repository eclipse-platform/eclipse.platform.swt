/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

/**
 * JNI SWT object field getters and setters declarations for Windows structs
 */

#include "swt.h"
#include "structs.h"

#ifndef NO_ACCEL
typedef struct ACCEL_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID fVirt, key, cmd;
} ACCEL_FID_CACHE;

ACCEL_FID_CACHE ACCELFc;

void cacheACCELFids(JNIEnv *env, jobject lpObject)
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
	if (!ACCELFc.cached) cacheACCELFids(env, lpObject);
	lpStruct->fVirt = (*env)->GetByteField(env, lpObject, ACCELFc.fVirt);
	lpStruct->key = (*env)->GetShortField(env, lpObject, ACCELFc.key);
	lpStruct->cmd = (*env)->GetShortField(env, lpObject, ACCELFc.cmd);
	return lpStruct;
}

void setACCELFields(JNIEnv *env, jobject lpObject, ACCEL *lpStruct)
{
	if (!ACCELFc.cached) cacheACCELFids(env, lpObject);
	(*env)->SetByteField(env, lpObject, ACCELFc.fVirt, (jbyte)lpStruct->fVirt);
	(*env)->SetShortField(env, lpObject, ACCELFc.key, (jshort)lpStruct->key);
	(*env)->SetShortField(env, lpObject, ACCELFc.cmd, (jshort)lpStruct->cmd);
}
#endif /* NO_ACCEL */

#ifndef NO_BITMAP
typedef struct BITMAP_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID bmType, bmWidth, bmHeight, bmWidthBytes, bmPlanes, bmBitsPixel, bmBits;
} BITMAP_FID_CACHE;

BITMAP_FID_CACHE BITMAPFc;

void cacheBITMAPFids(JNIEnv *env, jobject lpObject)
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
	if (!BITMAPFc.cached) cacheBITMAPFids(env, lpObject);
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
	if (!BITMAPFc.cached) cacheBITMAPFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, BITMAPFc.bmType, (jint)lpStruct->bmType);
	(*env)->SetIntField(env, lpObject, BITMAPFc.bmWidth, (jint)lpStruct->bmWidth);
	(*env)->SetIntField(env, lpObject, BITMAPFc.bmHeight, (jint)lpStruct->bmHeight);
	(*env)->SetIntField(env, lpObject, BITMAPFc.bmWidthBytes, (jint)lpStruct->bmWidthBytes);
	(*env)->SetShortField(env, lpObject, BITMAPFc.bmPlanes, (jshort)lpStruct->bmPlanes);
	(*env)->SetShortField(env, lpObject, BITMAPFc.bmBitsPixel, (jshort)lpStruct->bmBitsPixel);
	(*env)->SetIntField(env, lpObject, BITMAPFc.bmBits, (jint)lpStruct->bmBits);
}
#endif /* NO_BITMAP */

#ifndef NO_BROWSEINFO
typedef struct BROWSEINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID hwndOwner, pidlRoot, pszDisplayName, lpszTitle, ulFlags, lpfn, lParam, iImage;
} BROWSEINFO_FID_CACHE;

BROWSEINFO_FID_CACHE BROWSEINFOFc;

void cacheBROWSEINFOFids(JNIEnv *env, jobject lpObject)
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
	if (!BROWSEINFOFc.cached) cacheBROWSEINFOFids(env, lpObject);
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
	if (!BROWSEINFOFc.cached) cacheBROWSEINFOFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, BROWSEINFOFc.hwndOwner, (jint)lpStruct->hwndOwner);
	(*env)->SetIntField(env, lpObject, BROWSEINFOFc.pidlRoot, (jint)lpStruct->pidlRoot);
	(*env)->SetIntField(env, lpObject, BROWSEINFOFc.pszDisplayName, (jint)lpStruct->pszDisplayName);
	(*env)->SetIntField(env, lpObject, BROWSEINFOFc.lpszTitle, (jint)lpStruct->lpszTitle);
	(*env)->SetIntField(env, lpObject, BROWSEINFOFc.ulFlags, (jint)lpStruct->ulFlags);
	(*env)->SetIntField(env, lpObject, BROWSEINFOFc.lpfn, (jint)lpStruct->lpfn);
	(*env)->SetIntField(env, lpObject, BROWSEINFOFc.lParam, (jint)lpStruct->lParam);
	(*env)->SetIntField(env, lpObject, BROWSEINFOFc.iImage, (jint)lpStruct->iImage);
}
#endif /* NO_BROWSEINFO */

#ifndef NO_CHOOSECOLOR
typedef struct CHOOSECOLOR_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lStructSize, hwndOwner, hInstance, rgbResult, lpCustColors, Flags, lCustData, lpfnHook, lpTemplateName;
} CHOOSECOLOR_FID_CACHE;

CHOOSECOLOR_FID_CACHE CHOOSECOLORFc;

void cacheCHOOSECOLORFids(JNIEnv *env, jobject lpObject)
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
	if (!CHOOSECOLORFc.cached) cacheCHOOSECOLORFids(env, lpObject);
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
	if (!CHOOSECOLORFc.cached) cacheCHOOSECOLORFids(env, lpObject);
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
#endif /* NO_CHOOSECOLOR */

#ifndef NO_CHOOSEFONT
typedef struct CHOOSEFONT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lStructSize, hwndOwner, hDC, lpLogFont, iPointSize, Flags, rgbColors, lCustData, lpfnHook, lpTemplateName, hInstance, lpszStyle, nFontType, nSizeMin, nSizeMax;
} CHOOSEFONT_FID_CACHE;

CHOOSEFONT_FID_CACHE CHOOSEFONTFc;

void cacheCHOOSEFONTFids(JNIEnv *env, jobject lpObject)
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
	if (!CHOOSEFONTFc.cached) cacheCHOOSEFONTFids(env, lpObject);
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
	if (!CHOOSEFONTFc.cached) cacheCHOOSEFONTFids(env, lpObject);
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
#endif /* NO_CHOOSEFONT */

#ifndef NO_COMPOSITIONFORM
typedef struct COMPOSITIONFORM_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID dwStyle, x, y, left, top, right, bottom;
} COMPOSITIONFORM_FID_CACHE;

COMPOSITIONFORM_FID_CACHE COMPOSITIONFORMFc;

void cacheCOMPOSITIONFORMFids(JNIEnv *env, jobject lpObject)
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
	if (!COMPOSITIONFORMFc.cached) cacheCOMPOSITIONFORMFids(env, lpObject);
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
	if (!COMPOSITIONFORMFc.cached) cacheCOMPOSITIONFORMFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, COMPOSITIONFORMFc.dwStyle, (jint)lpStruct->dwStyle);
	(*env)->SetIntField(env, lpObject, COMPOSITIONFORMFc.x, (jint)lpStruct->ptCurrentPos.x);
	(*env)->SetIntField(env, lpObject, COMPOSITIONFORMFc.y, (jint)lpStruct->ptCurrentPos.y);
	(*env)->SetIntField(env, lpObject, COMPOSITIONFORMFc.left, (jint)lpStruct->rcArea.left);
	(*env)->SetIntField(env, lpObject, COMPOSITIONFORMFc.top, (jint)lpStruct->rcArea.top);
	(*env)->SetIntField(env, lpObject, COMPOSITIONFORMFc.right, (jint)lpStruct->rcArea.right);
	(*env)->SetIntField(env, lpObject, COMPOSITIONFORMFc.bottom, (jint)lpStruct->rcArea.bottom);
}
#endif /* NO_COMPOSITIONFORM */

#ifndef NO_CREATESTRUCT
typedef struct CREATESTRUCT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lpCreateParams, hInstance, hMenu, hwndParent, cy, cx, y, x, style, lpszName, lpszClass, dwExStyle;
} CREATESTRUCT_FID_CACHE;

CREATESTRUCT_FID_CACHE CREATESTRUCTFc;

void cacheCREATESTRUCTFids(JNIEnv *env, jobject lpObject)
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
	if (!CREATESTRUCTFc.cached) cacheCREATESTRUCTFids(env, lpObject);
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
	if (!CREATESTRUCTFc.cached) cacheCREATESTRUCTFids(env, lpObject);
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
#endif /* NO_CREATESTRUCT */

#ifndef NO_DIBSECTION
typedef struct DIBSECTION_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID biSize, biWidth, biHeight, biPlanes, biBitCount, biCompression, biSizeImage, biXPelsPerMeter, biYPelsPerMeter, biClrUsed, biClrImportant, dsBitfields0, dsBitfields1, dsBitfields2, dshSection, dsOffset;
} DIBSECTION_FID_CACHE;

DIBSECTION_FID_CACHE DIBSECTIONFc;

void cacheDIBSECTIONFids(JNIEnv *env, jobject lpObject)
{
	if (DIBSECTIONFc.cached) return;
	cacheBITMAPFids(env, lpObject);
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
	if (!DIBSECTIONFc.cached) cacheDIBSECTIONFids(env, lpObject);
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
	if (!DIBSECTIONFc.cached) cacheDIBSECTIONFids(env, lpObject);
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
#endif /* NO_DIBSECTION */

#ifndef NO_DLLVERSIONINFO
typedef struct DLLVERSIONINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, dwMajorVersion, dwMinorVersion, dwBuildNumber, dwPlatformID;
} DLLVERSIONINFO_FID_CACHE;

DLLVERSIONINFO_FID_CACHE DLLVERSIONINFOFc;

void cacheDLLVERSIONINFOFids(JNIEnv *env, jobject lpObject)
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
	if (!DLLVERSIONINFOFc.cached) cacheDLLVERSIONINFOFids(env, lpObject);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, DLLVERSIONINFOFc.cbSize);
	lpStruct->dwMajorVersion = (*env)->GetIntField(env, lpObject, DLLVERSIONINFOFc.dwMajorVersion);
	lpStruct->dwMinorVersion = (*env)->GetIntField(env, lpObject, DLLVERSIONINFOFc.dwMinorVersion);
	lpStruct->dwBuildNumber = (*env)->GetIntField(env, lpObject, DLLVERSIONINFOFc.dwBuildNumber);
	lpStruct->dwPlatformID = (*env)->GetIntField(env, lpObject, DLLVERSIONINFOFc.dwPlatformID);
	return lpStruct;
}

void setDLLVERSIONINFOFields(JNIEnv *env, jobject lpObject, DLLVERSIONINFO *lpStruct)
{
	if (!DLLVERSIONINFOFc.cached) cacheDLLVERSIONINFOFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, DLLVERSIONINFOFc.cbSize, (jint)lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, DLLVERSIONINFOFc.dwMajorVersion, (jint)lpStruct->dwMajorVersion);
	(*env)->SetIntField(env, lpObject, DLLVERSIONINFOFc.dwMinorVersion, (jint)lpStruct->dwMinorVersion);
	(*env)->SetIntField(env, lpObject, DLLVERSIONINFOFc.dwBuildNumber, (jint)lpStruct->dwBuildNumber);
	(*env)->SetIntField(env, lpObject, DLLVERSIONINFOFc.dwPlatformID, (jint)lpStruct->dwPlatformID);
}
#endif /* NO_DLLVERSIONINFO */

#ifndef NO_DOCINFO
typedef struct DOCINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, lpszDocName, lpszOutput, lpszDatatype, fwType;
} DOCINFO_FID_CACHE;

DOCINFO_FID_CACHE DOCINFOFc;

void cacheDOCINFOFids(JNIEnv *env, jobject lpObject)
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
	if (!DOCINFOFc.cached) cacheDOCINFOFids(env, lpObject);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, DOCINFOFc.cbSize);
	lpStruct->lpszDocName = (LPCTSTR)(*env)->GetIntField(env, lpObject, DOCINFOFc.lpszDocName);
	lpStruct->lpszOutput = (LPCTSTR)(*env)->GetIntField(env, lpObject, DOCINFOFc.lpszOutput);
	lpStruct->lpszDatatype = (LPCTSTR)(*env)->GetIntField(env, lpObject, DOCINFOFc.lpszDatatype);
	lpStruct->fwType = (*env)->GetIntField(env, lpObject, DOCINFOFc.fwType);
	return lpStruct;
}

void setDOCINFOFields(JNIEnv *env, jobject lpObject, DOCINFO *lpStruct)
{
	if (!DOCINFOFc.cached) cacheDOCINFOFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, DOCINFOFc.cbSize, (jint)lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, DOCINFOFc.lpszDocName, (jint)lpStruct->lpszDocName);
	(*env)->SetIntField(env, lpObject, DOCINFOFc.lpszOutput, (jint)lpStruct->lpszOutput);
	(*env)->SetIntField(env, lpObject, DOCINFOFc.lpszDatatype, (jint)lpStruct->lpszDatatype);
	(*env)->SetIntField(env, lpObject, DOCINFOFc.fwType, (jint)lpStruct->fwType);
}
#endif /* NO_DOCINFO */

#ifndef NO_DRAWITEMSTRUCT
typedef struct DRAWITEMSTRUCT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID CtlType, CtlID, itemID, itemAction, itemState, hwndItem, hDC, left, top, bottom, right, itemData;
} DRAWITEMSTRUCT_FID_CACHE;

DRAWITEMSTRUCT_FID_CACHE DRAWITEMSTRUCTFc;

void cacheDRAWITEMSTRUCTFids(JNIEnv *env, jobject lpObject)
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
	if (!DRAWITEMSTRUCTFc.cached) cacheDRAWITEMSTRUCTFids(env, lpObject);
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
	if (!DRAWITEMSTRUCTFc.cached) cacheDRAWITEMSTRUCTFids(env, lpObject);
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
#endif /* NO_DRAWITEMSTRUCT */

#ifndef NO_DROPFILES
typedef struct DROPFILES_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID pFiles, pt_x, pt_y, fNC, fWide;
} DROPFILES_FID_CACHE;

DROPFILES_FID_CACHE DROPFILESFc;

void cacheDROPFILESFids(JNIEnv *env, jobject lpObject)
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
	if (!DROPFILESFc.cached) cacheDROPFILESFids(env, lpObject);
	lpStruct->pFiles = (*env)->GetIntField(env, lpObject, DROPFILESFc.pFiles);
	lpStruct->pt.x = (*env)->GetIntField(env, lpObject, DROPFILESFc.pt_x);
	lpStruct->pt.y = (*env)->GetIntField(env, lpObject, DROPFILESFc.pt_y);
	lpStruct->fNC = (*env)->GetIntField(env, lpObject, DROPFILESFc.fNC);
	lpStruct->fWide = (*env)->GetIntField(env, lpObject, DROPFILESFc.fWide);
	return lpStruct;
}

void setDROPFILESFields(JNIEnv *env, jobject lpObject, DROPFILES *lpStruct)
{
	if (!DROPFILESFc.cached) cacheDROPFILESFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, DROPFILESFc.pFiles, (jint)lpStruct->pFiles);
	(*env)->SetIntField(env, lpObject, DROPFILESFc.pt_x, (jint)lpStruct->pt.x);
	(*env)->SetIntField(env, lpObject, DROPFILESFc.pt_y, (jint)lpStruct->pt.y);
	(*env)->SetIntField(env, lpObject, DROPFILESFc.fNC, (jint)lpStruct->fNC);
	(*env)->SetIntField(env, lpObject, DROPFILESFc.fWide, (jint)lpStruct->fWide);
}
#endif /* NO_DROPFILES */

#ifndef NO_FILETIME
typedef struct FILETIME_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID dwLowDateTime, dwHighDateTime;
} FILETIME_FID_CACHE;

FILETIME_FID_CACHE FILETIMEFc;

void cacheFILETIMEFids(JNIEnv *env, jobject lpObject)
{
	if (FILETIMEFc.cached) return;
	FILETIMEFc.clazz = (*env)->GetObjectClass(env, lpObject);
	FILETIMEFc.dwLowDateTime = (*env)->GetFieldID(env, FILETIMEFc.clazz, "dwLowDateTime", "I");
	FILETIMEFc.dwHighDateTime = (*env)->GetFieldID(env, FILETIMEFc.clazz, "dwHighDateTime", "I");
	FILETIMEFc.cached = 1;
}

FILETIME *getFILETIMEFields(JNIEnv *env, jobject lpObject, FILETIME *lpStruct)
{
	if (!FILETIMEFc.cached) cacheFILETIMEFids(env, lpObject);
	lpStruct->dwLowDateTime = (*env)->GetIntField(env, lpObject, FILETIMEFc.dwLowDateTime);
	lpStruct->dwHighDateTime = (*env)->GetIntField(env, lpObject, FILETIMEFc.dwHighDateTime);
	return lpStruct;
}

void setFILETIMEFields(JNIEnv *env, jobject lpObject, FILETIME *lpStruct)
{
	if (!FILETIMEFc.cached) cacheFILETIMEFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, FILETIMEFc.dwLowDateTime, (jint)lpStruct->dwLowDateTime);
	(*env)->SetIntField(env, lpObject, FILETIMEFc.dwHighDateTime, (jint)lpStruct->dwHighDateTime);
}
#endif /* NO_FILETIME */

#ifndef NO_GCP_RESULTS
typedef struct GCP_RESULTS_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lStructSize, lpOutString, lpOrder, lpDx, lpCaretPos, lpClass, lpGlyphs, nGlyphs, nMaxFit;
} GCP_RESULTS_FID_CACHE;

GCP_RESULTS_FID_CACHE GCP_RESULTSFc;

void cacheGCP_RESULTSFids(JNIEnv *env, jobject lpObject)
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
	if (!GCP_RESULTSFc.cached) cacheGCP_RESULTSFids(env, lpObject);
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
	if (!GCP_RESULTSFc.cached) cacheGCP_RESULTSFids(env, lpObject);
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
#endif /* NO_GCP_RESULTS */

#ifndef NO_GRADIENT_RECT
typedef struct GRADIENT_RECT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID UpperLeft, LowerRight;
} GRADIENT_RECT_FID_CACHE;

GRADIENT_RECT_FID_CACHE GRADIENT_RECTFc;

void cacheGRADIENT_RECTFids(JNIEnv *env, jobject lpObject)
{
	if (GRADIENT_RECTFc.cached) return;
	GRADIENT_RECTFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GRADIENT_RECTFc.UpperLeft = (*env)->GetFieldID(env, GRADIENT_RECTFc.clazz, "UpperLeft", "I");
	GRADIENT_RECTFc.LowerRight = (*env)->GetFieldID(env, GRADIENT_RECTFc.clazz, "LowerRight", "I");
	GRADIENT_RECTFc.cached = 1;
}

GRADIENT_RECT *getGRADIENT_RECTFields(JNIEnv *env, jobject lpObject, GRADIENT_RECT *lpStruct)
{
	if (!GRADIENT_RECTFc.cached) cacheGRADIENT_RECTFids(env, lpObject);
	lpStruct->UpperLeft = (*env)->GetIntField(env, lpObject, GRADIENT_RECTFc.UpperLeft);
	lpStruct->LowerRight = (*env)->GetIntField(env, lpObject, GRADIENT_RECTFc.LowerRight);
	return lpStruct;
}

void setGRADIENT_RECTFields(JNIEnv *env, jobject lpObject, GRADIENT_RECT *lpStruct)
{
	if (!GRADIENT_RECTFc.cached) cacheGRADIENT_RECTFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, GRADIENT_RECTFc.UpperLeft, (jint)lpStruct->UpperLeft);
	(*env)->SetIntField(env, lpObject, GRADIENT_RECTFc.LowerRight, (jint)lpStruct->LowerRight);
}
#endif /* NO_GRADIENT_RECT */

#ifndef NO_HDITEM
typedef struct HDITEM_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID mask, cxy, pszText, hbm, cchTextMax, fmt, lParam, iImage, iOrder;
} HDITEM_FID_CACHE;

HDITEM_FID_CACHE HDITEMFc;

void cacheHDITEMFids(JNIEnv *env, jobject lpObject)
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
	if (!HDITEMFc.cached) cacheHDITEMFids(env, lpObject);
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
	if (!HDITEMFc.cached) cacheHDITEMFids(env, lpObject);
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
#endif /* NO_HDITEM */

#ifndef NO_HELPINFO
typedef struct HELPINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, iContextType, iCtrlId, hItemHandle, dwContextId, x, y;
} HELPINFO_FID_CACHE;

HELPINFO_FID_CACHE HELPINFOFc;

void cacheHELPINFOFids(JNIEnv *env, jobject lpObject)
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
	if (!HELPINFOFc.cached) cacheHELPINFOFids(env, lpObject);
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
	if (!HELPINFOFc.cached) cacheHELPINFOFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, HELPINFOFc.cbSize, (jint)lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, HELPINFOFc.iContextType, (jint)lpStruct->iContextType);
	(*env)->SetIntField(env, lpObject, HELPINFOFc.iCtrlId, (jint)lpStruct->iCtrlId);
	(*env)->SetIntField(env, lpObject, HELPINFOFc.hItemHandle, (jint)lpStruct->hItemHandle);
	(*env)->SetIntField(env, lpObject, HELPINFOFc.dwContextId, (jint)lpStruct->dwContextId);
	(*env)->SetIntField(env, lpObject, HELPINFOFc.x, (jint)lpStruct->MousePos.x);
	(*env)->SetIntField(env, lpObject, HELPINFOFc.y, (jint)lpStruct->MousePos.y);
}
#endif /* NO_HELPINFO */

#ifndef NO_ICONINFO
typedef struct ICONINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID fIcon, xHotspot, yHotspot, hbmMask, hbmColor;
} ICONINFO_FID_CACHE;

ICONINFO_FID_CACHE ICONINFOFc;

void cacheICONINFOFids(JNIEnv *env, jobject lpObject)
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
	if (!ICONINFOFc.cached) cacheICONINFOFids(env, lpObject);
	lpStruct->fIcon = (*env)->GetBooleanField(env, lpObject, ICONINFOFc.fIcon);
	lpStruct->xHotspot = (*env)->GetIntField(env, lpObject, ICONINFOFc.xHotspot);
	lpStruct->yHotspot = (*env)->GetIntField(env, lpObject, ICONINFOFc.yHotspot);
	lpStruct->hbmMask = (HBITMAP)(*env)->GetIntField(env, lpObject, ICONINFOFc.hbmMask);
	lpStruct->hbmColor = (HBITMAP)(*env)->GetIntField(env, lpObject, ICONINFOFc.hbmColor);
	return lpStruct;
}

void setICONINFOFields(JNIEnv *env, jobject lpObject, ICONINFO *lpStruct)
{
	if (!ICONINFOFc.cached) cacheICONINFOFids(env, lpObject);
	(*env)->SetBooleanField(env, lpObject, ICONINFOFc.fIcon, (jboolean)lpStruct->fIcon);
	(*env)->SetIntField(env, lpObject, ICONINFOFc.xHotspot, (jint)lpStruct->xHotspot);
	(*env)->SetIntField(env, lpObject, ICONINFOFc.yHotspot, (jint)lpStruct->yHotspot);
	(*env)->SetIntField(env, lpObject, ICONINFOFc.hbmMask, (jint)lpStruct->hbmMask);
	(*env)->SetIntField(env, lpObject, ICONINFOFc.hbmColor, (jint)lpStruct->hbmColor);
}
#endif /* NO_ICONINFO */

#ifndef NO_INITCOMMONCONTROLSEX
typedef struct INITCOMMONCONTROLSEX_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID dwSize, dwICC;
} INITCOMMONCONTROLSEX_FID_CACHE;

INITCOMMONCONTROLSEX_FID_CACHE INITCOMMONCONTROLSEXFc;

void cacheINITCOMMONCONTROLSEXFids(JNIEnv *env, jobject lpObject)
{
	if (INITCOMMONCONTROLSEXFc.cached) return;
	INITCOMMONCONTROLSEXFc.clazz = (*env)->GetObjectClass(env, lpObject);
	INITCOMMONCONTROLSEXFc.dwSize = (*env)->GetFieldID(env, INITCOMMONCONTROLSEXFc.clazz, "dwSize", "I");
	INITCOMMONCONTROLSEXFc.dwICC = (*env)->GetFieldID(env, INITCOMMONCONTROLSEXFc.clazz, "dwICC", "I");
	INITCOMMONCONTROLSEXFc.cached = 1;
}

INITCOMMONCONTROLSEX *getINITCOMMONCONTROLSEXFields(JNIEnv *env, jobject lpObject, INITCOMMONCONTROLSEX *lpStruct)
{
	if (!INITCOMMONCONTROLSEXFc.cached) cacheINITCOMMONCONTROLSEXFids(env, lpObject);
	lpStruct->dwSize = (*env)->GetIntField(env, lpObject, INITCOMMONCONTROLSEXFc.dwSize);
	lpStruct->dwICC = (*env)->GetIntField(env, lpObject, INITCOMMONCONTROLSEXFc.dwICC);
	return lpStruct;
}

void setINITCOMMONCONTROLSEXFields(JNIEnv *env, jobject lpObject, INITCOMMONCONTROLSEX *lpStruct)
{
	if (!INITCOMMONCONTROLSEXFc.cached) cacheINITCOMMONCONTROLSEXFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, INITCOMMONCONTROLSEXFc.dwSize, (jint)lpStruct->dwSize);
	(*env)->SetIntField(env, lpObject, INITCOMMONCONTROLSEXFc.dwICC, (jint)lpStruct->dwICC);
}
#endif /* NO_INITCOMMONCONTROLSEX */

#ifndef NO_LOGBRUSH
typedef struct LOGBRUSH_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lbStyle, lbColor, lbHatch;
} LOGBRUSH_FID_CACHE;

LOGBRUSH_FID_CACHE LOGBRUSHFc;

void cacheLOGBRUSHFids(JNIEnv *env, jobject lpObject)
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
	if (!LOGBRUSHFc.cached) cacheLOGBRUSHFids(env, lpObject);
	lpStruct->lbStyle = (*env)->GetIntField(env, lpObject, LOGBRUSHFc.lbStyle);
	lpStruct->lbColor = (*env)->GetIntField(env, lpObject, LOGBRUSHFc.lbColor);
	lpStruct->lbHatch = (*env)->GetIntField(env, lpObject, LOGBRUSHFc.lbHatch);
	return lpStruct;
}

void setLOGBRUSHFields(JNIEnv *env, jobject lpObject, LOGBRUSH *lpStruct)
{
	if (!LOGBRUSHFc.cached) cacheLOGBRUSHFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, LOGBRUSHFc.lbStyle, (jint)lpStruct->lbStyle);
	(*env)->SetIntField(env, lpObject, LOGBRUSHFc.lbColor, (jint)lpStruct->lbColor);
	(*env)->SetIntField(env, lpObject, LOGBRUSHFc.lbHatch, (jint)lpStruct->lbHatch);
}
#endif /* NO_LOGBRUSH */

#ifndef NO_LOGFONT
typedef struct LOGFONT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lfHeight, lfWidth, lfEscapement, lfOrientation, lfWeight, lfItalic, lfUnderline, lfStrikeOut, lfCharSet, lfOutPrecision, lfClipPrecision, lfQuality, lfPitchAndFamily, lfFaceName0, lfFaceName1, lfFaceName2, lfFaceName3, lfFaceName4, lfFaceName5, lfFaceName6, lfFaceName7, lfFaceName8, lfFaceName9, lfFaceName10, lfFaceName11, lfFaceName12, lfFaceName13, lfFaceName14, lfFaceName15, lfFaceName16, lfFaceName17, lfFaceName18, lfFaceName19, lfFaceName20, lfFaceName21, lfFaceName22, lfFaceName23, lfFaceName24, lfFaceName25, lfFaceName26, lfFaceName27, lfFaceName28, lfFaceName29, lfFaceName30, lfFaceName31;
} LOGFONT_FID_CACHE;

LOGFONT_FID_CACHE LOGFONTFc;

void cacheLOGFONTFids(JNIEnv *env, jobject lpObject)
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
	LOGFONTFc.lfFaceName0 = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfFaceName0", "C");
	LOGFONTFc.lfFaceName1 = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfFaceName1", "C");
	LOGFONTFc.lfFaceName2 = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfFaceName2", "C");
	LOGFONTFc.lfFaceName3 = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfFaceName3", "C");
	LOGFONTFc.lfFaceName4 = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfFaceName4", "C");
	LOGFONTFc.lfFaceName5 = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfFaceName5", "C");
	LOGFONTFc.lfFaceName6 = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfFaceName6", "C");
	LOGFONTFc.lfFaceName7 = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfFaceName7", "C");
	LOGFONTFc.lfFaceName8 = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfFaceName8", "C");
	LOGFONTFc.lfFaceName9 = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfFaceName9", "C");
	LOGFONTFc.lfFaceName10 = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfFaceName10", "C");
	LOGFONTFc.lfFaceName11 = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfFaceName11", "C");
	LOGFONTFc.lfFaceName12 = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfFaceName12", "C");
	LOGFONTFc.lfFaceName13 = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfFaceName13", "C");
	LOGFONTFc.lfFaceName14 = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfFaceName14", "C");
	LOGFONTFc.lfFaceName15 = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfFaceName15", "C");
	LOGFONTFc.lfFaceName16 = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfFaceName16", "C");
	LOGFONTFc.lfFaceName17 = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfFaceName17", "C");
	LOGFONTFc.lfFaceName18 = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfFaceName18", "C");
	LOGFONTFc.lfFaceName19 = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfFaceName19", "C");
	LOGFONTFc.lfFaceName20 = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfFaceName20", "C");
	LOGFONTFc.lfFaceName21 = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfFaceName21", "C");
	LOGFONTFc.lfFaceName22 = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfFaceName22", "C");
	LOGFONTFc.lfFaceName23 = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfFaceName23", "C");
	LOGFONTFc.lfFaceName24 = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfFaceName24", "C");
	LOGFONTFc.lfFaceName25 = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfFaceName25", "C");
	LOGFONTFc.lfFaceName26 = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfFaceName26", "C");
	LOGFONTFc.lfFaceName27 = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfFaceName27", "C");
	LOGFONTFc.lfFaceName28 = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfFaceName28", "C");
	LOGFONTFc.lfFaceName29 = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfFaceName29", "C");
	LOGFONTFc.lfFaceName30 = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfFaceName30", "C");
	LOGFONTFc.lfFaceName31 = (*env)->GetFieldID(env, LOGFONTFc.clazz, "lfFaceName31", "C");
	LOGFONTFc.cached = 1;
}

#ifndef NO_LOGFONTA
LOGFONTA* getLOGFONTAFields(JNIEnv *env, jobject lpObject, LOGFONTA *lpStruct)
{
	if (!LOGFONTFc.cached) cacheLOGFONTFids(env, lpObject);
    lpStruct->lfHeight = (*env)->GetIntField(env,lpObject,LOGFONTFc.lfHeight);
    lpStruct->lfWidth = (*env)->GetIntField(env,lpObject,LOGFONTFc.lfWidth);
    lpStruct->lfEscapement = (*env)->GetIntField(env,lpObject,LOGFONTFc.lfEscapement);
    lpStruct->lfOrientation = (*env)->GetIntField(env,lpObject,LOGFONTFc.lfOrientation);
    lpStruct->lfWeight = (*env)->GetIntField(env,lpObject,LOGFONTFc.lfWeight);
    lpStruct->lfItalic = (*env)->GetByteField(env,lpObject,LOGFONTFc.lfItalic);
    lpStruct->lfUnderline = (*env)->GetByteField(env,lpObject,LOGFONTFc.lfUnderline);
    lpStruct->lfStrikeOut = (*env)->GetByteField(env,lpObject,LOGFONTFc.lfStrikeOut);
    lpStruct->lfCharSet = (*env)->GetByteField(env,lpObject,LOGFONTFc.lfCharSet);
    lpStruct->lfOutPrecision = (*env)->GetByteField(env,lpObject,LOGFONTFc.lfOutPrecision);
    lpStruct->lfClipPrecision = (*env)->GetByteField(env,lpObject,LOGFONTFc.lfClipPrecision);
    lpStruct->lfQuality = (*env)->GetByteField(env,lpObject,LOGFONTFc.lfQuality);
    lpStruct->lfPitchAndFamily = (*env)->GetByteField(env,lpObject,LOGFONTFc.lfPitchAndFamily);
    {
    WCHAR lfFaceName [32];
    lfFaceName[0] = (*env)->GetCharField(env,lpObject,LOGFONTFc.lfFaceName0);
    lfFaceName[1] = (*env)->GetCharField(env,lpObject,LOGFONTFc.lfFaceName1);
    lfFaceName[2] = (*env)->GetCharField(env,lpObject,LOGFONTFc.lfFaceName2);
    lfFaceName[3] = (*env)->GetCharField(env,lpObject,LOGFONTFc.lfFaceName3);
    lfFaceName[4] = (*env)->GetCharField(env,lpObject,LOGFONTFc.lfFaceName4);
    lfFaceName[5] = (*env)->GetCharField(env,lpObject,LOGFONTFc.lfFaceName5);
    lfFaceName[6] = (*env)->GetCharField(env,lpObject,LOGFONTFc.lfFaceName6);
    lfFaceName[7] = (*env)->GetCharField(env,lpObject,LOGFONTFc.lfFaceName7);
    lfFaceName[8] = (*env)->GetCharField(env,lpObject,LOGFONTFc.lfFaceName8);
    lfFaceName[9] = (*env)->GetCharField(env,lpObject,LOGFONTFc.lfFaceName9);
    lfFaceName[10] = (*env)->GetCharField(env,lpObject,LOGFONTFc.lfFaceName10);
    lfFaceName[11] = (*env)->GetCharField(env,lpObject,LOGFONTFc.lfFaceName11);
    lfFaceName[12] = (*env)->GetCharField(env,lpObject,LOGFONTFc.lfFaceName12);
    lfFaceName[13] = (*env)->GetCharField(env,lpObject,LOGFONTFc.lfFaceName13);
    lfFaceName[14] = (*env)->GetCharField(env,lpObject,LOGFONTFc.lfFaceName14);
    lfFaceName[15] = (*env)->GetCharField(env,lpObject,LOGFONTFc.lfFaceName15);
    lfFaceName[16] = (*env)->GetCharField(env,lpObject,LOGFONTFc.lfFaceName16);
    lfFaceName[17] = (*env)->GetCharField(env,lpObject,LOGFONTFc.lfFaceName17);
    lfFaceName[18] = (*env)->GetCharField(env,lpObject,LOGFONTFc.lfFaceName18);
    lfFaceName[19] = (*env)->GetCharField(env,lpObject,LOGFONTFc.lfFaceName19);
    lfFaceName[20] = (*env)->GetCharField(env,lpObject,LOGFONTFc.lfFaceName20);
    lfFaceName[21] = (*env)->GetCharField(env,lpObject,LOGFONTFc.lfFaceName21);
    lfFaceName[22] = (*env)->GetCharField(env,lpObject,LOGFONTFc.lfFaceName22);
    lfFaceName[23] = (*env)->GetCharField(env,lpObject,LOGFONTFc.lfFaceName23);
    lfFaceName[24] = (*env)->GetCharField(env,lpObject,LOGFONTFc.lfFaceName24);
    lfFaceName[25] = (*env)->GetCharField(env,lpObject,LOGFONTFc.lfFaceName25);
    lfFaceName[26] = (*env)->GetCharField(env,lpObject,LOGFONTFc.lfFaceName26);
    lfFaceName[27] = (*env)->GetCharField(env,lpObject,LOGFONTFc.lfFaceName27);
    lfFaceName[28] = (*env)->GetCharField(env,lpObject,LOGFONTFc.lfFaceName28);
    lfFaceName[29] = (*env)->GetCharField(env,lpObject,LOGFONTFc.lfFaceName29);
    lfFaceName[30] = (*env)->GetCharField(env,lpObject,LOGFONTFc.lfFaceName30);
    lfFaceName[31] = (*env)->GetCharField(env,lpObject,LOGFONTFc.lfFaceName31);
   	WideCharToMultiByte (CP_ACP, 0, lfFaceName, 32, lpStruct->lfFaceName, 32, NULL, NULL);
    }
    return lpStruct;
}

void setLOGFONTAFields(JNIEnv *env, jobject lpObject, LOGFONTA *lpStruct)
{
	if (!LOGFONTFc.cached) cacheLOGFONTFids(env, lpObject);
    (*env)->SetIntField(env,lpObject,LOGFONTFc.lfHeight, lpStruct->lfHeight);
    (*env)->SetIntField(env,lpObject,LOGFONTFc.lfWidth, lpStruct->lfWidth);
    (*env)->SetIntField(env,lpObject,LOGFONTFc.lfEscapement, lpStruct->lfEscapement);
    (*env)->SetIntField(env,lpObject,LOGFONTFc.lfOrientation, lpStruct->lfOrientation);
    (*env)->SetIntField(env,lpObject,LOGFONTFc.lfWeight, lpStruct->lfWeight);
    (*env)->SetByteField(env,lpObject,LOGFONTFc.lfItalic, lpStruct->lfItalic);
    (*env)->SetByteField(env,lpObject,LOGFONTFc.lfUnderline, lpStruct->lfUnderline);
    (*env)->SetByteField(env,lpObject,LOGFONTFc.lfStrikeOut, lpStruct->lfStrikeOut);
    (*env)->SetByteField(env,lpObject,LOGFONTFc.lfCharSet, lpStruct->lfCharSet);
    (*env)->SetByteField(env,lpObject,LOGFONTFc.lfOutPrecision, lpStruct->lfOutPrecision);
    (*env)->SetByteField(env,lpObject,LOGFONTFc.lfClipPrecision, lpStruct->lfClipPrecision);
    (*env)->SetByteField(env,lpObject,LOGFONTFc.lfQuality, lpStruct->lfQuality);
    (*env)->SetByteField(env,lpObject,LOGFONTFc.lfPitchAndFamily, lpStruct->lfPitchAndFamily);
    {
    WCHAR lfFaceName [32] = {0};
	MultiByteToWideChar (CP_ACP, MB_PRECOMPOSED, lpStruct->lfFaceName, -1, lfFaceName, 32);
    (*env)->SetCharField(env,lpObject,LOGFONTFc.lfFaceName0, lfFaceName[0]);
    (*env)->SetCharField(env,lpObject,LOGFONTFc.lfFaceName1, lfFaceName[1]);
    (*env)->SetCharField(env,lpObject,LOGFONTFc.lfFaceName2, lfFaceName[2]);
    (*env)->SetCharField(env,lpObject,LOGFONTFc.lfFaceName3, lfFaceName[3]);
    (*env)->SetCharField(env,lpObject,LOGFONTFc.lfFaceName4, lfFaceName[4]);
    (*env)->SetCharField(env,lpObject,LOGFONTFc.lfFaceName5, lfFaceName[5]);
    (*env)->SetCharField(env,lpObject,LOGFONTFc.lfFaceName6, lfFaceName[6]);
    (*env)->SetCharField(env,lpObject,LOGFONTFc.lfFaceName7, lfFaceName[7]);
    (*env)->SetCharField(env,lpObject,LOGFONTFc.lfFaceName8, lfFaceName[8]);
    (*env)->SetCharField(env,lpObject,LOGFONTFc.lfFaceName9, lfFaceName[9]);
    (*env)->SetCharField(env,lpObject,LOGFONTFc.lfFaceName10, lfFaceName[10]);
    (*env)->SetCharField(env,lpObject,LOGFONTFc.lfFaceName11, lfFaceName[11]);
    (*env)->SetCharField(env,lpObject,LOGFONTFc.lfFaceName12, lfFaceName[12]);
    (*env)->SetCharField(env,lpObject,LOGFONTFc.lfFaceName13, lfFaceName[13]);
    (*env)->SetCharField(env,lpObject,LOGFONTFc.lfFaceName14, lfFaceName[14]);
    (*env)->SetCharField(env,lpObject,LOGFONTFc.lfFaceName15, lfFaceName[15]);
    (*env)->SetCharField(env,lpObject,LOGFONTFc.lfFaceName16, lfFaceName[16]);
    (*env)->SetCharField(env,lpObject,LOGFONTFc.lfFaceName17, lfFaceName[17]);
    (*env)->SetCharField(env,lpObject,LOGFONTFc.lfFaceName18, lfFaceName[18]);
    (*env)->SetCharField(env,lpObject,LOGFONTFc.lfFaceName19, lfFaceName[19]);
    (*env)->SetCharField(env,lpObject,LOGFONTFc.lfFaceName20, lfFaceName[20]);
    (*env)->SetCharField(env,lpObject,LOGFONTFc.lfFaceName21, lfFaceName[21]);
    (*env)->SetCharField(env,lpObject,LOGFONTFc.lfFaceName22, lfFaceName[22]);
    (*env)->SetCharField(env,lpObject,LOGFONTFc.lfFaceName23, lfFaceName[23]);
    (*env)->SetCharField(env,lpObject,LOGFONTFc.lfFaceName24, lfFaceName[24]);
    (*env)->SetCharField(env,lpObject,LOGFONTFc.lfFaceName25, lfFaceName[25]);
    (*env)->SetCharField(env,lpObject,LOGFONTFc.lfFaceName26, lfFaceName[26]);
    (*env)->SetCharField(env,lpObject,LOGFONTFc.lfFaceName27, lfFaceName[27]);
    (*env)->SetCharField(env,lpObject,LOGFONTFc.lfFaceName28, lfFaceName[28]);
    (*env)->SetCharField(env,lpObject,LOGFONTFc.lfFaceName29, lfFaceName[29]);
    (*env)->SetCharField(env,lpObject,LOGFONTFc.lfFaceName30, lfFaceName[30]);
    (*env)->SetCharField(env,lpObject,LOGFONTFc.lfFaceName31, lfFaceName[31]);
    }
}
#endif /* NO_LOGFONTA */

#ifndef NO_LOGFONTW
LOGFONTW *getLOGFONTWFields(JNIEnv *env, jobject lpObject, LOGFONTW *lpStruct)
{
	if (!LOGFONTFc.cached) cacheLOGFONTFids(env, lpObject);
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
	lpStruct->lfFaceName[0] = (*env)->GetCharField(env, lpObject, LOGFONTFc.lfFaceName0);
	lpStruct->lfFaceName[1] = (*env)->GetCharField(env, lpObject, LOGFONTFc.lfFaceName1);
	lpStruct->lfFaceName[2] = (*env)->GetCharField(env, lpObject, LOGFONTFc.lfFaceName2);
	lpStruct->lfFaceName[3] = (*env)->GetCharField(env, lpObject, LOGFONTFc.lfFaceName3);
	lpStruct->lfFaceName[4] = (*env)->GetCharField(env, lpObject, LOGFONTFc.lfFaceName4);
	lpStruct->lfFaceName[5] = (*env)->GetCharField(env, lpObject, LOGFONTFc.lfFaceName5);
	lpStruct->lfFaceName[6] = (*env)->GetCharField(env, lpObject, LOGFONTFc.lfFaceName6);
	lpStruct->lfFaceName[7] = (*env)->GetCharField(env, lpObject, LOGFONTFc.lfFaceName7);
	lpStruct->lfFaceName[8] = (*env)->GetCharField(env, lpObject, LOGFONTFc.lfFaceName8);
	lpStruct->lfFaceName[9] = (*env)->GetCharField(env, lpObject, LOGFONTFc.lfFaceName9);
	lpStruct->lfFaceName[10] = (*env)->GetCharField(env, lpObject, LOGFONTFc.lfFaceName10);
	lpStruct->lfFaceName[11] = (*env)->GetCharField(env, lpObject, LOGFONTFc.lfFaceName11);
	lpStruct->lfFaceName[12] = (*env)->GetCharField(env, lpObject, LOGFONTFc.lfFaceName12);
	lpStruct->lfFaceName[13] = (*env)->GetCharField(env, lpObject, LOGFONTFc.lfFaceName13);
	lpStruct->lfFaceName[14] = (*env)->GetCharField(env, lpObject, LOGFONTFc.lfFaceName14);
	lpStruct->lfFaceName[15] = (*env)->GetCharField(env, lpObject, LOGFONTFc.lfFaceName15);
	lpStruct->lfFaceName[16] = (*env)->GetCharField(env, lpObject, LOGFONTFc.lfFaceName16);
	lpStruct->lfFaceName[17] = (*env)->GetCharField(env, lpObject, LOGFONTFc.lfFaceName17);
	lpStruct->lfFaceName[18] = (*env)->GetCharField(env, lpObject, LOGFONTFc.lfFaceName18);
	lpStruct->lfFaceName[19] = (*env)->GetCharField(env, lpObject, LOGFONTFc.lfFaceName19);
	lpStruct->lfFaceName[20] = (*env)->GetCharField(env, lpObject, LOGFONTFc.lfFaceName20);
	lpStruct->lfFaceName[21] = (*env)->GetCharField(env, lpObject, LOGFONTFc.lfFaceName21);
	lpStruct->lfFaceName[22] = (*env)->GetCharField(env, lpObject, LOGFONTFc.lfFaceName22);
	lpStruct->lfFaceName[23] = (*env)->GetCharField(env, lpObject, LOGFONTFc.lfFaceName23);
	lpStruct->lfFaceName[24] = (*env)->GetCharField(env, lpObject, LOGFONTFc.lfFaceName24);
	lpStruct->lfFaceName[25] = (*env)->GetCharField(env, lpObject, LOGFONTFc.lfFaceName25);
	lpStruct->lfFaceName[26] = (*env)->GetCharField(env, lpObject, LOGFONTFc.lfFaceName26);
	lpStruct->lfFaceName[27] = (*env)->GetCharField(env, lpObject, LOGFONTFc.lfFaceName27);
	lpStruct->lfFaceName[28] = (*env)->GetCharField(env, lpObject, LOGFONTFc.lfFaceName28);
	lpStruct->lfFaceName[29] = (*env)->GetCharField(env, lpObject, LOGFONTFc.lfFaceName29);
	lpStruct->lfFaceName[30] = (*env)->GetCharField(env, lpObject, LOGFONTFc.lfFaceName30);
	lpStruct->lfFaceName[31] = (*env)->GetCharField(env, lpObject, LOGFONTFc.lfFaceName31);
	return lpStruct;
}

void setLOGFONTWFields(JNIEnv *env, jobject lpObject, LOGFONTW *lpStruct)
{
	if (!LOGFONTFc.cached) cacheLOGFONTFids(env, lpObject);
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
	(*env)->SetCharField(env, lpObject, LOGFONTFc.lfFaceName0, (jchar)lpStruct->lfFaceName[0]);
	(*env)->SetCharField(env, lpObject, LOGFONTFc.lfFaceName1, (jchar)lpStruct->lfFaceName[1]);
	(*env)->SetCharField(env, lpObject, LOGFONTFc.lfFaceName2, (jchar)lpStruct->lfFaceName[2]);
	(*env)->SetCharField(env, lpObject, LOGFONTFc.lfFaceName3, (jchar)lpStruct->lfFaceName[3]);
	(*env)->SetCharField(env, lpObject, LOGFONTFc.lfFaceName4, (jchar)lpStruct->lfFaceName[4]);
	(*env)->SetCharField(env, lpObject, LOGFONTFc.lfFaceName5, (jchar)lpStruct->lfFaceName[5]);
	(*env)->SetCharField(env, lpObject, LOGFONTFc.lfFaceName6, (jchar)lpStruct->lfFaceName[6]);
	(*env)->SetCharField(env, lpObject, LOGFONTFc.lfFaceName7, (jchar)lpStruct->lfFaceName[7]);
	(*env)->SetCharField(env, lpObject, LOGFONTFc.lfFaceName8, (jchar)lpStruct->lfFaceName[8]);
	(*env)->SetCharField(env, lpObject, LOGFONTFc.lfFaceName9, (jchar)lpStruct->lfFaceName[9]);
	(*env)->SetCharField(env, lpObject, LOGFONTFc.lfFaceName10, (jchar)lpStruct->lfFaceName[10]);
	(*env)->SetCharField(env, lpObject, LOGFONTFc.lfFaceName11, (jchar)lpStruct->lfFaceName[11]);
	(*env)->SetCharField(env, lpObject, LOGFONTFc.lfFaceName12, (jchar)lpStruct->lfFaceName[12]);
	(*env)->SetCharField(env, lpObject, LOGFONTFc.lfFaceName13, (jchar)lpStruct->lfFaceName[13]);
	(*env)->SetCharField(env, lpObject, LOGFONTFc.lfFaceName14, (jchar)lpStruct->lfFaceName[14]);
	(*env)->SetCharField(env, lpObject, LOGFONTFc.lfFaceName15, (jchar)lpStruct->lfFaceName[15]);
	(*env)->SetCharField(env, lpObject, LOGFONTFc.lfFaceName16, (jchar)lpStruct->lfFaceName[16]);
	(*env)->SetCharField(env, lpObject, LOGFONTFc.lfFaceName17, (jchar)lpStruct->lfFaceName[17]);
	(*env)->SetCharField(env, lpObject, LOGFONTFc.lfFaceName18, (jchar)lpStruct->lfFaceName[18]);
	(*env)->SetCharField(env, lpObject, LOGFONTFc.lfFaceName19, (jchar)lpStruct->lfFaceName[19]);
	(*env)->SetCharField(env, lpObject, LOGFONTFc.lfFaceName20, (jchar)lpStruct->lfFaceName[20]);
	(*env)->SetCharField(env, lpObject, LOGFONTFc.lfFaceName21, (jchar)lpStruct->lfFaceName[21]);
	(*env)->SetCharField(env, lpObject, LOGFONTFc.lfFaceName22, (jchar)lpStruct->lfFaceName[22]);
	(*env)->SetCharField(env, lpObject, LOGFONTFc.lfFaceName23, (jchar)lpStruct->lfFaceName[23]);
	(*env)->SetCharField(env, lpObject, LOGFONTFc.lfFaceName24, (jchar)lpStruct->lfFaceName[24]);
	(*env)->SetCharField(env, lpObject, LOGFONTFc.lfFaceName25, (jchar)lpStruct->lfFaceName[25]);
	(*env)->SetCharField(env, lpObject, LOGFONTFc.lfFaceName26, (jchar)lpStruct->lfFaceName[26]);
	(*env)->SetCharField(env, lpObject, LOGFONTFc.lfFaceName27, (jchar)lpStruct->lfFaceName[27]);
	(*env)->SetCharField(env, lpObject, LOGFONTFc.lfFaceName28, (jchar)lpStruct->lfFaceName[28]);
	(*env)->SetCharField(env, lpObject, LOGFONTFc.lfFaceName29, (jchar)lpStruct->lfFaceName[29]);
	(*env)->SetCharField(env, lpObject, LOGFONTFc.lfFaceName30, (jchar)lpStruct->lfFaceName[30]);
	(*env)->SetCharField(env, lpObject, LOGFONTFc.lfFaceName31, (jchar)lpStruct->lfFaceName[31]);
}
#endif /* NO_LOGFONTW */
#endif /* NO_LOGFONT */

#ifndef NO_LOGPEN
typedef struct LOGPEN_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lopnStyle, x, y, lopnColor;
} LOGPEN_FID_CACHE;

LOGPEN_FID_CACHE LOGPENFc;

void cacheLOGPENFids(JNIEnv *env, jobject lpObject)
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
	if (!LOGPENFc.cached) cacheLOGPENFids(env, lpObject);
	lpStruct->lopnStyle = (*env)->GetIntField(env, lpObject, LOGPENFc.lopnStyle);
	lpStruct->lopnWidth.x = (*env)->GetIntField(env, lpObject, LOGPENFc.x);
	lpStruct->lopnWidth.y = (*env)->GetIntField(env, lpObject, LOGPENFc.y);
	lpStruct->lopnColor = (*env)->GetIntField(env, lpObject, LOGPENFc.lopnColor);
	return lpStruct;
}

void setLOGPENFields(JNIEnv *env, jobject lpObject, LOGPEN *lpStruct)
{
	if (!LOGPENFc.cached) cacheLOGPENFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, LOGPENFc.lopnStyle, (jint)lpStruct->lopnStyle);
	(*env)->SetIntField(env, lpObject, LOGPENFc.x, (jint)lpStruct->lopnWidth.x);
	(*env)->SetIntField(env, lpObject, LOGPENFc.y, (jint)lpStruct->lopnWidth.y);
	(*env)->SetIntField(env, lpObject, LOGPENFc.lopnColor, (jint)lpStruct->lopnColor);
}
#endif /* NO_LOGPEN */

#ifndef NO_LVCOLUMN
typedef struct LVCOLUMN_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID mask, fmt, cx, pszText, cchTextMax, iSubItem, iImage, iOrder;
} LVCOLUMN_FID_CACHE;

LVCOLUMN_FID_CACHE LVCOLUMNFc;

void cacheLVCOLUMNFids(JNIEnv *env, jobject lpObject)
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
	if (!LVCOLUMNFc.cached) cacheLVCOLUMNFids(env, lpObject);
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
	if (!LVCOLUMNFc.cached) cacheLVCOLUMNFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, LVCOLUMNFc.mask, (jint)lpStruct->mask);
	(*env)->SetIntField(env, lpObject, LVCOLUMNFc.fmt, (jint)lpStruct->fmt);
	(*env)->SetIntField(env, lpObject, LVCOLUMNFc.cx, (jint)lpStruct->cx);
	(*env)->SetIntField(env, lpObject, LVCOLUMNFc.pszText, (jint)lpStruct->pszText);
	(*env)->SetIntField(env, lpObject, LVCOLUMNFc.cchTextMax, (jint)lpStruct->cchTextMax);
	(*env)->SetIntField(env, lpObject, LVCOLUMNFc.iSubItem, (jint)lpStruct->iSubItem);
	(*env)->SetIntField(env, lpObject, LVCOLUMNFc.iImage, (jint)lpStruct->iImage);
	(*env)->SetIntField(env, lpObject, LVCOLUMNFc.iOrder, (jint)lpStruct->iOrder);
}
#endif /* NO_LVCOLUMN */

#ifndef NO_LVHITTESTINFO
typedef struct LVHITTESTINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID x, y, flags, iItem, iSubItem;
} LVHITTESTINFO_FID_CACHE;

LVHITTESTINFO_FID_CACHE LVHITTESTINFOFc;

void cacheLVHITTESTINFOFids(JNIEnv *env, jobject lpObject)
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
	if (!LVHITTESTINFOFc.cached) cacheLVHITTESTINFOFids(env, lpObject);
	lpStruct->pt.x = (*env)->GetIntField(env, lpObject, LVHITTESTINFOFc.x);
	lpStruct->pt.y = (*env)->GetIntField(env, lpObject, LVHITTESTINFOFc.y);
	lpStruct->flags = (*env)->GetIntField(env, lpObject, LVHITTESTINFOFc.flags);
	lpStruct->iItem = (*env)->GetIntField(env, lpObject, LVHITTESTINFOFc.iItem);
	lpStruct->iSubItem = (*env)->GetIntField(env, lpObject, LVHITTESTINFOFc.iSubItem);
	return lpStruct;
}

void setLVHITTESTINFOFields(JNIEnv *env, jobject lpObject, LVHITTESTINFO *lpStruct)
{
	if (!LVHITTESTINFOFc.cached) cacheLVHITTESTINFOFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, LVHITTESTINFOFc.x, (jint)lpStruct->pt.x);
	(*env)->SetIntField(env, lpObject, LVHITTESTINFOFc.y, (jint)lpStruct->pt.y);
	(*env)->SetIntField(env, lpObject, LVHITTESTINFOFc.flags, (jint)lpStruct->flags);
	(*env)->SetIntField(env, lpObject, LVHITTESTINFOFc.iItem, (jint)lpStruct->iItem);
	(*env)->SetIntField(env, lpObject, LVHITTESTINFOFc.iSubItem, (jint)lpStruct->iSubItem);
}
#endif /* NO_LVHITTESTINFO */

#ifndef NO_LVITEM
typedef struct LVITEM_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID mask, iItem, iSubItem, state, stateMask, pszText, cchTextMax, iImage, lParam, iIndent;
} LVITEM_FID_CACHE;

LVITEM_FID_CACHE LVITEMFc;

void cacheLVITEMFids(JNIEnv *env, jobject lpObject)
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
	if (!LVITEMFc.cached) cacheLVITEMFids(env, lpObject);
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
	if (!LVITEMFc.cached) cacheLVITEMFids(env, lpObject);
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
#endif /* NO_LVITEM */

#ifndef NO_MEASUREITEMSTRUCT
typedef struct MEASUREITEMSTRUCT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID CtlType, CtlID, itemID, itemWidth, itemHeight, itemData;
} MEASUREITEMSTRUCT_FID_CACHE;

MEASUREITEMSTRUCT_FID_CACHE MEASUREITEMSTRUCTFc;

void cacheMEASUREITEMSTRUCTFids(JNIEnv *env, jobject lpObject)
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
	if (!MEASUREITEMSTRUCTFc.cached) cacheMEASUREITEMSTRUCTFids(env, lpObject);
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
	if (!MEASUREITEMSTRUCTFc.cached) cacheMEASUREITEMSTRUCTFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, MEASUREITEMSTRUCTFc.CtlType, (jint)lpStruct->CtlType);
	(*env)->SetIntField(env, lpObject, MEASUREITEMSTRUCTFc.CtlID, (jint)lpStruct->CtlID);
	(*env)->SetIntField(env, lpObject, MEASUREITEMSTRUCTFc.itemID, (jint)lpStruct->itemID);
	(*env)->SetIntField(env, lpObject, MEASUREITEMSTRUCTFc.itemWidth, (jint)lpStruct->itemWidth);
	(*env)->SetIntField(env, lpObject, MEASUREITEMSTRUCTFc.itemHeight, (jint)lpStruct->itemHeight);
	(*env)->SetIntField(env, lpObject, MEASUREITEMSTRUCTFc.itemData, (jint)lpStruct->itemData);
}
#endif /* NO_MEASUREITEMSTRUCT */

#ifndef NO_MENUINFO
typedef struct MENUINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, fMask, dwStyle, cyMax, hbrBack, dwContextHelpID, dwMenuData;
} MENUINFO_FID_CACHE;

MENUINFO_FID_CACHE MENUINFOFc;

void cacheMENUINFOFids(JNIEnv *env, jobject lpObject)
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
	if (!MENUINFOFc.cached) cacheMENUINFOFids(env, lpObject);
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
	if (!MENUINFOFc.cached) cacheMENUINFOFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, MENUINFOFc.cbSize, (jint)lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, MENUINFOFc.fMask, (jint)lpStruct->fMask);
	(*env)->SetIntField(env, lpObject, MENUINFOFc.dwStyle, (jint)lpStruct->dwStyle);
	(*env)->SetIntField(env, lpObject, MENUINFOFc.cyMax, (jint)lpStruct->cyMax);
	(*env)->SetIntField(env, lpObject, MENUINFOFc.hbrBack, (jint)lpStruct->hbrBack);
	(*env)->SetIntField(env, lpObject, MENUINFOFc.dwContextHelpID, (jint)lpStruct->dwContextHelpID);
	(*env)->SetIntField(env, lpObject, MENUINFOFc.dwMenuData, (jint)lpStruct->dwMenuData);
}
#endif /* NO_MENUINFO */

#ifndef NO_MENUITEMINFO
typedef struct MENUITEMINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, fMask, fType, fState, wID, hSubMenu, hbmpChecked, hbmpUnchecked, dwItemData, dwTypeData, cch, hbmpItem;
} MENUITEMINFO_FID_CACHE;

MENUITEMINFO_FID_CACHE MENUITEMINFOFc;

void cacheMENUITEMINFOFids(JNIEnv *env, jobject lpObject)
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
#ifndef _WIN32_WCE
	MENUITEMINFOFc.hbmpItem = (*env)->GetFieldID(env, MENUITEMINFOFc.clazz, "hbmpItem", "I");
#endif /* _WIN32_WCE */
	MENUITEMINFOFc.cached = 1;
}

MENUITEMINFO *getMENUITEMINFOFields(JNIEnv *env, jobject lpObject, MENUITEMINFO *lpStruct)
{
	if (!MENUITEMINFOFc.cached) cacheMENUITEMINFOFids(env, lpObject);
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
#endif /* _WIN32_WCE */
	return lpStruct;
}

void setMENUITEMINFOFields(JNIEnv *env, jobject lpObject, MENUITEMINFO *lpStruct)
{
	if (!MENUITEMINFOFc.cached) cacheMENUITEMINFOFids(env, lpObject);
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
#endif /* _WIN32_WCE */
}
#endif /* NO_MENUITEMINFO */

#ifndef NO_MSG
typedef struct MSG_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID hwnd, message, wParam, lParam, time, x, y;
} MSG_FID_CACHE;

MSG_FID_CACHE MSGFc;

void cacheMSGFids(JNIEnv *env, jobject lpObject)
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
	if (!MSGFc.cached) cacheMSGFids(env, lpObject);
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
	if (!MSGFc.cached) cacheMSGFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, MSGFc.hwnd, (jint)lpStruct->hwnd);
	(*env)->SetIntField(env, lpObject, MSGFc.message, (jint)lpStruct->message);
	(*env)->SetIntField(env, lpObject, MSGFc.wParam, (jint)lpStruct->wParam);
	(*env)->SetIntField(env, lpObject, MSGFc.lParam, (jint)lpStruct->lParam);
	(*env)->SetIntField(env, lpObject, MSGFc.time, (jint)lpStruct->time);
	(*env)->SetIntField(env, lpObject, MSGFc.x, (jint)lpStruct->pt.x);
	(*env)->SetIntField(env, lpObject, MSGFc.y, (jint)lpStruct->pt.y);
}
#endif /* NO_MSG */

#ifndef NO_NMHDR
typedef struct NMHDR_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID hwndFrom, idFrom, code;
} NMHDR_FID_CACHE;

NMHDR_FID_CACHE NMHDRFc;

void cacheNMHDRFids(JNIEnv *env, jobject lpObject)
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
	if (!NMHDRFc.cached) cacheNMHDRFids(env, lpObject);
	lpStruct->hwndFrom = (HWND)(*env)->GetIntField(env, lpObject, NMHDRFc.hwndFrom);
	lpStruct->idFrom = (*env)->GetIntField(env, lpObject, NMHDRFc.idFrom);
	lpStruct->code = (*env)->GetIntField(env, lpObject, NMHDRFc.code);
	return lpStruct;
}

void setNMHDRFields(JNIEnv *env, jobject lpObject, NMHDR *lpStruct)
{
	if (!NMHDRFc.cached) cacheNMHDRFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, NMHDRFc.hwndFrom, (jint)lpStruct->hwndFrom);
	(*env)->SetIntField(env, lpObject, NMHDRFc.idFrom, (jint)lpStruct->idFrom);
	(*env)->SetIntField(env, lpObject, NMHDRFc.code, (jint)lpStruct->code);
}
#endif /* NO_NMHDR */

#ifndef NO_NMCUSTOMDRAW
typedef struct NMCUSTOMDRAW_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID dwDrawStage, hdc, left, top, right, bottom, dwItemSpec, uItemState, lItemlParam;
} NMCUSTOMDRAW_FID_CACHE;

NMCUSTOMDRAW_FID_CACHE NMCUSTOMDRAWFc;

void cacheNMCUSTOMDRAWFids(JNIEnv *env, jobject lpObject)
{
	if (NMCUSTOMDRAWFc.cached) return;
	cacheNMHDRFids(env, lpObject);
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
	if (!NMCUSTOMDRAWFc.cached) cacheNMCUSTOMDRAWFids(env, lpObject);
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
	if (!NMCUSTOMDRAWFc.cached) cacheNMCUSTOMDRAWFids(env, lpObject);
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
#endif /* NO_NMCUSTOMDRAW */

#ifndef NO_NMHEADER
typedef struct NMHEADER_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID iItem, iButton, pitem;
} NMHEADER_FID_CACHE;

NMHEADER_FID_CACHE NMHEADERFc;

void cacheNMHEADERFids(JNIEnv *env, jobject lpObject)
{
	if (NMHEADERFc.cached) return;
	cacheNMHDRFids(env, lpObject);
	NMHEADERFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NMHEADERFc.iItem = (*env)->GetFieldID(env, NMHEADERFc.clazz, "iItem", "I");
	NMHEADERFc.iButton = (*env)->GetFieldID(env, NMHEADERFc.clazz, "iButton", "I");
	NMHEADERFc.pitem = (*env)->GetFieldID(env, NMHEADERFc.clazz, "pitem", "I");
	NMHEADERFc.cached = 1;
}

NMHEADER *getNMHEADERFields(JNIEnv *env, jobject lpObject, NMHEADER *lpStruct)
{
	if (!NMHEADERFc.cached) cacheNMHEADERFids(env, lpObject);
	getNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	lpStruct->iItem = (*env)->GetIntField(env, lpObject, NMHEADERFc.iItem);
	lpStruct->iButton = (*env)->GetIntField(env, lpObject, NMHEADERFc.iButton);
	lpStruct->pitem = (HDITEM FAR *)(*env)->GetIntField(env, lpObject, NMHEADERFc.pitem);
	return lpStruct;
}

void setNMHEADERFields(JNIEnv *env, jobject lpObject, NMHEADER *lpStruct)
{
	if (!NMHEADERFc.cached) cacheNMHEADERFids(env, lpObject);
	setNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	(*env)->SetIntField(env, lpObject, NMHEADERFc.iItem, (jint)lpStruct->iItem);
	(*env)->SetIntField(env, lpObject, NMHEADERFc.iButton, (jint)lpStruct->iButton);
	(*env)->SetIntField(env, lpObject, NMHEADERFc.pitem, (jint)lpStruct->pitem);
}
#endif /* NO_NMHEADER */

#ifndef NO_NMLISTVIEW
typedef struct NMLISTVIEW_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID iItem, iSubItem, uNewState, uOldState, uChanged, x, y, lParam;
} NMLISTVIEW_FID_CACHE;

NMLISTVIEW_FID_CACHE NMLISTVIEWFc;

void cacheNMLISTVIEWFids(JNIEnv *env, jobject lpObject)
{
	if (NMLISTVIEWFc.cached) return;
	cacheNMHDRFids(env, lpObject);
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
	if (!NMLISTVIEWFc.cached) cacheNMLISTVIEWFids(env, lpObject);
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
	if (!NMLISTVIEWFc.cached) cacheNMLISTVIEWFids(env, lpObject);
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
#endif /* NO_NMLISTVIEW */

#ifndef NO_NMLVCUSTOMDRAW
typedef struct NMLVCUSTOMDRAW_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID clrText, clrTextBk, iSubItem;
} NMLVCUSTOMDRAW_FID_CACHE;

NMLVCUSTOMDRAW_FID_CACHE NMLVCUSTOMDRAWFc;

void cacheNMLVCUSTOMDRAWFids(JNIEnv *env, jobject lpObject)
{
	if (NMLVCUSTOMDRAWFc.cached) return;
	cacheNMCUSTOMDRAWFids(env, lpObject);
	NMLVCUSTOMDRAWFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NMLVCUSTOMDRAWFc.clrText = (*env)->GetFieldID(env, NMLVCUSTOMDRAWFc.clazz, "clrText", "I");
	NMLVCUSTOMDRAWFc.clrTextBk = (*env)->GetFieldID(env, NMLVCUSTOMDRAWFc.clazz, "clrTextBk", "I");
	NMLVCUSTOMDRAWFc.iSubItem = (*env)->GetFieldID(env, NMLVCUSTOMDRAWFc.clazz, "iSubItem", "I");
	NMLVCUSTOMDRAWFc.cached = 1;
}

NMLVCUSTOMDRAW *getNMLVCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMLVCUSTOMDRAW *lpStruct)
{
	if (!NMLVCUSTOMDRAWFc.cached) cacheNMLVCUSTOMDRAWFids(env, lpObject);
	getNMCUSTOMDRAWFields(env, lpObject, (NMCUSTOMDRAW *)lpStruct);
	lpStruct->clrText = (*env)->GetIntField(env, lpObject, NMLVCUSTOMDRAWFc.clrText);
	lpStruct->clrTextBk = (*env)->GetIntField(env, lpObject, NMLVCUSTOMDRAWFc.clrTextBk);
	lpStruct->iSubItem = (*env)->GetIntField(env, lpObject, NMLVCUSTOMDRAWFc.iSubItem);
	return lpStruct;
}

void setNMLVCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMLVCUSTOMDRAW *lpStruct)
{
	if (!NMLVCUSTOMDRAWFc.cached) cacheNMLVCUSTOMDRAWFids(env, lpObject);
	setNMCUSTOMDRAWFields(env, lpObject, (NMCUSTOMDRAW *)lpStruct);
	(*env)->SetIntField(env, lpObject, NMLVCUSTOMDRAWFc.clrText, (jint)lpStruct->clrText);
	(*env)->SetIntField(env, lpObject, NMLVCUSTOMDRAWFc.clrTextBk, (jint)lpStruct->clrTextBk);
	(*env)->SetIntField(env, lpObject, NMLVCUSTOMDRAWFc.iSubItem, (jint)lpStruct->iSubItem);
}
#endif /* NO_NMLVCUSTOMDRAW */

#ifndef NO_NMREBARCHEVRON
typedef struct NMREBARCHEVRON_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID uBand, wID, lParam, left, top, right, bottom, lParamNM;
} NMREBARCHEVRON_FID_CACHE;

NMREBARCHEVRON_FID_CACHE NMREBARCHEVRONFc;

void cacheNMREBARCHEVRONFids(JNIEnv *env, jobject lpObject)
{
	if (NMREBARCHEVRONFc.cached) return;
	cacheNMHDRFids(env, lpObject);
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
	if (!NMREBARCHEVRONFc.cached) cacheNMREBARCHEVRONFids(env, lpObject);
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
	if (!NMREBARCHEVRONFc.cached) cacheNMREBARCHEVRONFids(env, lpObject);
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
#endif /* NO_NMREBARCHEVRON */

#ifndef NO_NMTOOLBAR
typedef struct NMTOOLBAR_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID iItem, iBitmap, idCommand, fsState, fsStyle, dwData, iString, cchText, pszText;
#ifndef _WIN32_WCE
	jfieldID left, top, right, bottom;
#endif /* _WIN32_WCE */
} NMTOOLBAR_FID_CACHE;

NMTOOLBAR_FID_CACHE NMTOOLBARFc;

void cacheNMTOOLBARFids(JNIEnv *env, jobject lpObject)
{
	if (NMTOOLBARFc.cached) return;
	cacheNMHDRFids(env, lpObject);
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
#ifndef _WIN32_WCE
	NMTOOLBARFc.left = (*env)->GetFieldID(env, NMTOOLBARFc.clazz, "left", "I");
	NMTOOLBARFc.top = (*env)->GetFieldID(env, NMTOOLBARFc.clazz, "top", "I");
	NMTOOLBARFc.right = (*env)->GetFieldID(env, NMTOOLBARFc.clazz, "right", "I");
	NMTOOLBARFc.bottom = (*env)->GetFieldID(env, NMTOOLBARFc.clazz, "bottom", "I");
#endif /* _WIN32_WCE */
	NMTOOLBARFc.cached = 1;
}

NMTOOLBAR *getNMTOOLBARFields(JNIEnv *env, jobject lpObject, NMTOOLBAR *lpStruct)
{
	if (!NMTOOLBARFc.cached) cacheNMTOOLBARFids(env, lpObject);
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
	lpStruct->rcButton.top = (*env)->GetIntField(env, lpObject, NMTOOLBARFc.top);
	lpStruct->rcButton.right = (*env)->GetIntField(env, lpObject, NMTOOLBARFc.right);
	lpStruct->rcButton.bottom = (*env)->GetIntField(env, lpObject, NMTOOLBARFc.bottom);
#endif /* _WIN32_WCE */
	return lpStruct;
}

void setNMTOOLBARFields(JNIEnv *env, jobject lpObject, NMTOOLBAR *lpStruct)
{
	if (!NMTOOLBARFc.cached) cacheNMTOOLBARFids(env, lpObject);
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
	(*env)->SetIntField(env, lpObject, NMTOOLBARFc.top, (jint)lpStruct->rcButton.top);
	(*env)->SetIntField(env, lpObject, NMTOOLBARFc.right, (jint)lpStruct->rcButton.right);
	(*env)->SetIntField(env, lpObject, NMTOOLBARFc.bottom, (jint)lpStruct->rcButton.bottom);
#endif /* _WIN32_WCE */
}
#endif /* NO_NMTOOLBAR */

#ifndef NO_NMTTDISPINFO
typedef struct NMTTDISPINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lpszText, hinst, uFlags, lParam;
} NMTTDISPINFO_FID_CACHE;

NMTTDISPINFO_FID_CACHE NMTTDISPINFOFc;

void cacheNMTTDISPINFOFids(JNIEnv *env, jobject lpObject)
{
	if (NMTTDISPINFOFc.cached) return;
	cacheNMHDRFids(env, lpObject);
	NMTTDISPINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NMTTDISPINFOFc.lpszText = (*env)->GetFieldID(env, NMTTDISPINFOFc.clazz, "lpszText", "I");
	NMTTDISPINFOFc.hinst = (*env)->GetFieldID(env, NMTTDISPINFOFc.clazz, "hinst", "I");
	NMTTDISPINFOFc.uFlags = (*env)->GetFieldID(env, NMTTDISPINFOFc.clazz, "uFlags", "I");
	NMTTDISPINFOFc.lParam = (*env)->GetFieldID(env, NMTTDISPINFOFc.clazz, "lParam", "I");
	NMTTDISPINFOFc.cached = 1;
}

#ifndef NO_NMTTDISPINFOA
NMTTDISPINFOA* getNMTTDISPINFOAFields(JNIEnv *env, jobject lpObject, NMTTDISPINFOA *lpStruct)
{
	if (!NMTTDISPINFOFc.cached) cacheNMTTDISPINFOFids(env, lpObject);
	getNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	lpStruct->lpszText = (LPTSTR)(*env)->GetIntField(env, lpObject, NMTTDISPINFOFc.lpszText);
	lpStruct->hinst = (HINSTANCE)(*env)->GetIntField(env, lpObject, NMTTDISPINFOFc.hinst);
	lpStruct->uFlags = (*env)->GetIntField(env, lpObject, NMTTDISPINFOFc.uFlags);
	lpStruct->lParam = (*env)->GetIntField(env, lpObject, NMTTDISPINFOFc.lParam);
	return lpStruct;
}

void setNMTTDISPINFOAFields(JNIEnv *env, jobject lpObject, NMTTDISPINFOA *lpStruct)
{
	if (!NMTTDISPINFOFc.cached) cacheNMTTDISPINFOFids(env, lpObject);
	setNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	(*env)->SetIntField(env, lpObject, NMTTDISPINFOFc.lpszText, (jint)lpStruct->lpszText);
	(*env)->SetIntField(env, lpObject, NMTTDISPINFOFc.hinst, (jint)lpStruct->hinst);
	(*env)->SetIntField(env, lpObject, NMTTDISPINFOFc.uFlags, lpStruct->uFlags);
	(*env)->SetIntField(env, lpObject, NMTTDISPINFOFc.lParam, lpStruct->lParam);
}
#endif /* NO_NMTTDISPINFOA */

#ifndef NO_NMTTDISPINFOW
NMTTDISPINFOW *getNMTTDISPINFOWFields(JNIEnv *env, jobject lpObject, NMTTDISPINFOW *lpStruct)
{
	if (!NMTTDISPINFOFc.cached) cacheNMTTDISPINFOFids(env, lpObject);
	getNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	lpStruct->lpszText = (LPWSTR)(*env)->GetIntField(env, lpObject, NMTTDISPINFOFc.lpszText);
	lpStruct->hinst = (HINSTANCE)(*env)->GetIntField(env, lpObject, NMTTDISPINFOFc.hinst);
	lpStruct->uFlags = (*env)->GetIntField(env, lpObject, NMTTDISPINFOFc.uFlags);
	lpStruct->lParam = (*env)->GetIntField(env, lpObject, NMTTDISPINFOFc.lParam);
	return lpStruct;
}

void setNMTTDISPINFOWFields(JNIEnv *env, jobject lpObject, NMTTDISPINFOW *lpStruct)
{
	if (!NMTTDISPINFOFc.cached) cacheNMTTDISPINFOFids(env, lpObject);
	setNMHDRFields(env, lpObject, (NMHDR *)lpStruct);
	(*env)->SetIntField(env, lpObject, NMTTDISPINFOFc.lpszText, (jint)lpStruct->lpszText);
	(*env)->SetIntField(env, lpObject, NMTTDISPINFOFc.hinst, (jint)lpStruct->hinst);
	(*env)->SetIntField(env, lpObject, NMTTDISPINFOFc.uFlags, (jint)lpStruct->uFlags);
	(*env)->SetIntField(env, lpObject, NMTTDISPINFOFc.lParam, (jint)lpStruct->lParam);
}
#endif /* NO_NMTTDISPINFOW */
#endif /* NO_NMTTDISPINFO */

#ifndef NO_NMTVCUSTOMDRAW
typedef struct NMTVCUSTOMDRAW_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID clrText, clrTextBk;
#ifndef _WIN32_WCE
	jfieldID iLevel;
#endif /* _WIN32_WCE */
} NMTVCUSTOMDRAW_FID_CACHE;

NMTVCUSTOMDRAW_FID_CACHE NMTVCUSTOMDRAWFc;

void cacheNMTVCUSTOMDRAWFids(JNIEnv *env, jobject lpObject)
{
	if (NMTVCUSTOMDRAWFc.cached) return;
	cacheNMCUSTOMDRAWFids(env, lpObject);
	NMTVCUSTOMDRAWFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NMTVCUSTOMDRAWFc.clrText = (*env)->GetFieldID(env, NMTVCUSTOMDRAWFc.clazz, "clrText", "I");
	NMTVCUSTOMDRAWFc.clrTextBk = (*env)->GetFieldID(env, NMTVCUSTOMDRAWFc.clazz, "clrTextBk", "I");
#ifndef _WIN32_WCE
	NMTVCUSTOMDRAWFc.iLevel = (*env)->GetFieldID(env, NMTVCUSTOMDRAWFc.clazz, "iLevel", "I");
#endif /* _WIN32_WCE */
	NMTVCUSTOMDRAWFc.cached = 1;
}

NMTVCUSTOMDRAW *getNMTVCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMTVCUSTOMDRAW *lpStruct)
{
	if (!NMTVCUSTOMDRAWFc.cached) cacheNMTVCUSTOMDRAWFids(env, lpObject);
	getNMCUSTOMDRAWFields(env, lpObject, (NMCUSTOMDRAW *)lpStruct);
	lpStruct->clrText = (*env)->GetIntField(env, lpObject, NMTVCUSTOMDRAWFc.clrText);
	lpStruct->clrTextBk = (*env)->GetIntField(env, lpObject, NMTVCUSTOMDRAWFc.clrTextBk);
#ifndef _WIN32_WCE
	lpStruct->iLevel = (*env)->GetIntField(env, lpObject, NMTVCUSTOMDRAWFc.iLevel);
#endif /* _WIN32_WCE */
	return lpStruct;
}

void setNMTVCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMTVCUSTOMDRAW *lpStruct)
{
	if (!NMTVCUSTOMDRAWFc.cached) cacheNMTVCUSTOMDRAWFids(env, lpObject);
	setNMCUSTOMDRAWFields(env, lpObject, (NMCUSTOMDRAW *)lpStruct);
	(*env)->SetIntField(env, lpObject, NMTVCUSTOMDRAWFc.clrText, (jint)lpStruct->clrText);
	(*env)->SetIntField(env, lpObject, NMTVCUSTOMDRAWFc.clrTextBk, (jint)lpStruct->clrTextBk);
#ifndef _WIN32_WCE
	(*env)->SetIntField(env, lpObject, NMTVCUSTOMDRAWFc.iLevel, (jint)lpStruct->iLevel);
#endif /* _WIN32_WCE */
}
#endif /* NO_NMTVCUSTOMDRAW */

#ifndef NO_NONCLIENTMETRICS
typedef struct NONCLIENTMETRICS_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, iBorderWidth, iScrollWidth, iScrollHeight, iCaptionWidth, iCaptionHeight, lfCaptionFont, iSmCaptionWidth, iSmCaptionHeight, lfSmCaptionFont, iMenuWidth, iMenuHeight, lfMenuFont, lfStatusFont, lfMessageFont;
} NONCLIENTMETRICS_FID_CACHE;

NONCLIENTMETRICS_FID_CACHE NONCLIENTMETRICSFc;

void cacheNONCLIENTMETRICSFids(JNIEnv *env, jobject lpObject)
{
	if (NONCLIENTMETRICSFc.cached) return;
	NONCLIENTMETRICSFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NONCLIENTMETRICSFc.cbSize = (*env)->GetFieldID(env, NONCLIENTMETRICSFc.clazz, "cbSize", "I");
	NONCLIENTMETRICSFc.iBorderWidth = (*env)->GetFieldID(env, NONCLIENTMETRICSFc.clazz, "iBorderWidth", "I");
	NONCLIENTMETRICSFc.iScrollWidth = (*env)->GetFieldID(env, NONCLIENTMETRICSFc.clazz, "iScrollWidth", "I");
	NONCLIENTMETRICSFc.iScrollHeight = (*env)->GetFieldID(env, NONCLIENTMETRICSFc.clazz, "iScrollHeight", "I");
	NONCLIENTMETRICSFc.iCaptionWidth = (*env)->GetFieldID(env, NONCLIENTMETRICSFc.clazz, "iCaptionWidth", "I");
	NONCLIENTMETRICSFc.iCaptionHeight = (*env)->GetFieldID(env, NONCLIENTMETRICSFc.clazz, "iCaptionHeight", "I");
	NONCLIENTMETRICSFc.lfCaptionFont = (*env)->GetFieldID(env, NONCLIENTMETRICSFc.clazz, "lfCaptionFont", "Lorg/eclipse/swt/internal/win32/LOGFONT;");
	NONCLIENTMETRICSFc.iSmCaptionWidth = (*env)->GetFieldID(env, NONCLIENTMETRICSFc.clazz, "iSmCaptionWidth", "I");
	NONCLIENTMETRICSFc.iSmCaptionHeight = (*env)->GetFieldID(env, NONCLIENTMETRICSFc.clazz, "iSmCaptionHeight", "I");
	NONCLIENTMETRICSFc.lfSmCaptionFont = (*env)->GetFieldID(env, NONCLIENTMETRICSFc.clazz, "lfSmCaptionFont", "Lorg/eclipse/swt/internal/win32/LOGFONT;");
	NONCLIENTMETRICSFc.iMenuWidth = (*env)->GetFieldID(env, NONCLIENTMETRICSFc.clazz, "iMenuWidth", "I");
	NONCLIENTMETRICSFc.iMenuHeight = (*env)->GetFieldID(env, NONCLIENTMETRICSFc.clazz, "iMenuHeight", "I");
	NONCLIENTMETRICSFc.lfMenuFont = (*env)->GetFieldID(env, NONCLIENTMETRICSFc.clazz, "lfMenuFont", "Lorg/eclipse/swt/internal/win32/LOGFONT;");
	NONCLIENTMETRICSFc.lfStatusFont = (*env)->GetFieldID(env, NONCLIENTMETRICSFc.clazz, "lfStatusFont", "Lorg/eclipse/swt/internal/win32/LOGFONT;");
	NONCLIENTMETRICSFc.lfMessageFont = (*env)->GetFieldID(env, NONCLIENTMETRICSFc.clazz, "lfMessageFont", "Lorg/eclipse/swt/internal/win32/LOGFONT;");
	NONCLIENTMETRICSFc.cached = 1;
}

#ifndef NONCLIENTMETRICSA
NONCLIENTMETRICSA* getNONCLIENTMETRICSAFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICSA *lpStruct)
{
	if (!NONCLIENTMETRICSFc.cached) cacheNONCLIENTMETRICSFids(env, lpObject);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.cbSize);
	lpStruct->iBorderWidth = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iBorderWidth);
	lpStruct->iScrollWidth = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iScrollWidth);
	lpStruct->iScrollHeight = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iScrollHeight);
	lpStruct->iCaptionWidth = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iCaptionWidth);
	lpStruct->iCaptionHeight = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iCaptionHeight);
	{
	jobject lpLogfont = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSFc.lfCaptionFont);
	getLOGFONTAFields(env, lpLogfont, &lpStruct->lfCaptionFont);
	}
	lpStruct->iSmCaptionWidth = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iSmCaptionWidth);
	lpStruct->iSmCaptionHeight = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iSmCaptionHeight);
	{
	jobject lpLogfont = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSFc.lfSmCaptionFont);
	getLOGFONTAFields(env, lpLogfont, &lpStruct->lfSmCaptionFont);
	}
	lpStruct->iMenuWidth = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iMenuWidth);
	lpStruct->iMenuHeight = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iMenuHeight);
	{
	jobject lpLogfont = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSFc.lfMenuFont);
	getLOGFONTAFields(env, lpLogfont, &lpStruct->lfMenuFont);
	}
	{
	jobject lpLogfont = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSFc.lfStatusFont);
	getLOGFONTAFields(env, lpLogfont, &lpStruct->lfStatusFont);
	}
	{
	jobject lpLogfont = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSFc.lfMessageFont);
	getLOGFONTAFields(env, lpLogfont, &lpStruct->lfMessageFont);
	}
	return lpStruct;
}

void setNONCLIENTMETRICSAFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICSA *lpStruct)
{
	if (!NONCLIENTMETRICSFc.cached) cacheNONCLIENTMETRICSFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.cbSize, lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iBorderWidth, lpStruct->iBorderWidth);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iScrollWidth, lpStruct->iScrollWidth);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iScrollHeight, lpStruct->iScrollHeight);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iCaptionWidth, lpStruct->iCaptionWidth);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iCaptionHeight, lpStruct->iCaptionHeight);
	{
	jobject lpLogfont = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSFc.lfCaptionFont);
	setLOGFONTAFields(env, lpLogfont, &lpStruct->lfCaptionFont);
	}
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iSmCaptionWidth, lpStruct->iSmCaptionWidth);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iSmCaptionHeight, lpStruct->iSmCaptionHeight);
	{
	jobject lpLogfont = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSFc.lfSmCaptionFont);
	setLOGFONTAFields(env, lpLogfont, &lpStruct->lfSmCaptionFont);
	}
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iMenuWidth, lpStruct->iMenuWidth);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iMenuHeight, lpStruct->iMenuHeight);
	{
	jobject lpLogfont = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSFc.lfMenuFont);
	setLOGFONTAFields(env, lpLogfont, &lpStruct->lfMenuFont);
	}
	{
	jobject lpLogfont = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSFc.lfStatusFont);
	setLOGFONTAFields(env, lpLogfont, &lpStruct->lfStatusFont);
	}
	{
	jobject lpLogfont = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSFc.lfMessageFont);
	setLOGFONTAFields(env, lpLogfont, &lpStruct->lfMessageFont);
	}
}
#endif /* NONCLIENTMETRICSA */

#ifndef NONCLIENTMETRICSW
NONCLIENTMETRICSW *getNONCLIENTMETRICSWFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICSW *lpStruct)
{
	if (!NONCLIENTMETRICSFc.cached) cacheNONCLIENTMETRICSFids(env, lpObject);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.cbSize);
	lpStruct->iBorderWidth = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iBorderWidth);
	lpStruct->iScrollWidth = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iScrollWidth);
	lpStruct->iScrollHeight = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iScrollHeight);
	lpStruct->iCaptionWidth = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iCaptionWidth);
	lpStruct->iCaptionHeight = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iCaptionHeight);
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSFc.lfCaptionFont);
	getLOGFONTWFields(env, lpObject1, &lpStruct->lfCaptionFont);
	}
	lpStruct->iSmCaptionWidth = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iSmCaptionWidth);
	lpStruct->iSmCaptionHeight = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iSmCaptionHeight);
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSFc.lfSmCaptionFont);
	getLOGFONTWFields(env, lpObject1, &lpStruct->lfSmCaptionFont);
	}
	lpStruct->iMenuWidth = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iMenuWidth);
	lpStruct->iMenuHeight = (*env)->GetIntField(env, lpObject, NONCLIENTMETRICSFc.iMenuHeight);
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSFc.lfMenuFont);
	getLOGFONTWFields(env, lpObject1, &lpStruct->lfMenuFont);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSFc.lfStatusFont);
	getLOGFONTWFields(env, lpObject1, &lpStruct->lfStatusFont);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSFc.lfMessageFont);
	getLOGFONTWFields(env, lpObject1, &lpStruct->lfMessageFont);
	}
	return lpStruct;
}

void setNONCLIENTMETRICSWFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICSW *lpStruct)
{
	if (!NONCLIENTMETRICSFc.cached) cacheNONCLIENTMETRICSFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.cbSize, (jint)lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iBorderWidth, (jint)lpStruct->iBorderWidth);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iScrollWidth, (jint)lpStruct->iScrollWidth);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iScrollHeight, (jint)lpStruct->iScrollHeight);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iCaptionWidth, (jint)lpStruct->iCaptionWidth);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iCaptionHeight, (jint)lpStruct->iCaptionHeight);
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSFc.lfCaptionFont);
	setLOGFONTWFields(env, lpObject1, &lpStruct->lfCaptionFont);
	}
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iSmCaptionWidth, (jint)lpStruct->iSmCaptionWidth);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iSmCaptionHeight, (jint)lpStruct->iSmCaptionHeight);
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSFc.lfSmCaptionFont);
	setLOGFONTWFields(env, lpObject1, &lpStruct->lfSmCaptionFont);
	}
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iMenuWidth, (jint)lpStruct->iMenuWidth);
	(*env)->SetIntField(env, lpObject, NONCLIENTMETRICSFc.iMenuHeight, (jint)lpStruct->iMenuHeight);
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSFc.lfMenuFont);
	setLOGFONTWFields(env, lpObject1, &lpStruct->lfMenuFont);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSFc.lfStatusFont);
	setLOGFONTWFields(env, lpObject1, &lpStruct->lfStatusFont);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NONCLIENTMETRICSFc.lfMessageFont);
	setLOGFONTWFields(env, lpObject1, &lpStruct->lfMessageFont);
	}
}
#endif /* NONCLIENTMETRICSW */
#endif /* NO_NONCLIENTMETRICS */

#ifndef NO_OPENFILENAME
typedef struct OPENFILENAME_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lStructSize, hwndOwner, hInstance, lpstrFilter, lpstrCustomFilter, nMaxCustFilter, nFilterIndex, lpstrFile, nMaxFile, lpstrFileTitle, nMaxFileTitle, lpstrInitialDir, lpstrTitle, Flags, nFileOffset, nFileExtension, lpstrDefExt, lCustData, lpfnHook, lpTemplateName;
} OPENFILENAME_FID_CACHE;

OPENFILENAME_FID_CACHE OPENFILENAMEFc;

void cacheOPENFILENAMEFids(JNIEnv *env, jobject lpObject)
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
	if (!OPENFILENAMEFc.cached) cacheOPENFILENAMEFids(env, lpObject);
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
	if (!OPENFILENAMEFc.cached) cacheOPENFILENAMEFids(env, lpObject);
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
#endif /* NO_OPENFILENAME */

#ifndef NO_OSVERSIONINFO
typedef struct OSVERSIONINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID dwOSVersionInfoSize, dwMajorVersion, dwMinorVersion, dwBuildNumber, dwPlatformId;
} OSVERSIONINFO_FID_CACHE;

OSVERSIONINFO_FID_CACHE OSVERSIONINFOFc;

void cacheOSVERSIONINFOFids(JNIEnv *env, jobject lpObject)
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

#ifndef OSVERSIONINFOA
OSVERSIONINFOA* getOSVERSIONINFOAFields(JNIEnv *env, jobject lpObject, OSVERSIONINFOA *lpStruct)
{
	if (!OSVERSIONINFOFc.cached) cacheOSVERSIONINFOFids(env, lpObject);
	lpStruct->dwOSVersionInfoSize = (*env)->GetIntField(env, lpObject, OSVERSIONINFOFc.dwOSVersionInfoSize);
	lpStruct->dwMajorVersion = (*env)->GetIntField(env, lpObject, OSVERSIONINFOFc.dwMajorVersion);
	lpStruct->dwMinorVersion = (*env)->GetIntField(env, lpObject, OSVERSIONINFOFc.dwMinorVersion);
	lpStruct->dwBuildNumber = (*env)->GetIntField(env, lpObject, OSVERSIONINFOFc.dwBuildNumber);
	lpStruct->dwPlatformId = (*env)->GetIntField(env, lpObject, OSVERSIONINFOFc.dwPlatformId);
	return lpStruct;
}

void setOSVERSIONINFOAFields(JNIEnv *env, jobject lpObject, OSVERSIONINFOA *lpStruct)
{
	if (!OSVERSIONINFOFc.cached) cacheOSVERSIONINFOFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, OSVERSIONINFOFc.dwOSVersionInfoSize, lpStruct->dwOSVersionInfoSize);
	(*env)->SetIntField(env, lpObject, OSVERSIONINFOFc.dwMajorVersion, lpStruct->dwMajorVersion);
	(*env)->SetIntField(env, lpObject, OSVERSIONINFOFc.dwMinorVersion, lpStruct->dwMinorVersion);
	(*env)->SetIntField(env, lpObject, OSVERSIONINFOFc.dwBuildNumber, lpStruct->dwBuildNumber);
	(*env)->SetIntField(env, lpObject, OSVERSIONINFOFc.dwPlatformId, lpStruct->dwPlatformId);
}
#endif /* OSVERSIONINFOA */

#ifndef OSVERSIONINFOW
OSVERSIONINFOW *getOSVERSIONINFOWFields(JNIEnv *env, jobject lpObject, OSVERSIONINFOW *lpStruct)
{
	if (!OSVERSIONINFOFc.cached) cacheOSVERSIONINFOFids(env, lpObject);
	lpStruct->dwOSVersionInfoSize = (*env)->GetIntField(env, lpObject, OSVERSIONINFOFc.dwOSVersionInfoSize);
	lpStruct->dwMajorVersion = (*env)->GetIntField(env, lpObject, OSVERSIONINFOFc.dwMajorVersion);
	lpStruct->dwMinorVersion = (*env)->GetIntField(env, lpObject, OSVERSIONINFOFc.dwMinorVersion);
	lpStruct->dwBuildNumber = (*env)->GetIntField(env, lpObject, OSVERSIONINFOFc.dwBuildNumber);
	lpStruct->dwPlatformId = (*env)->GetIntField(env, lpObject, OSVERSIONINFOFc.dwPlatformId);
	return lpStruct;
}

void setOSVERSIONINFOWFields(JNIEnv *env, jobject lpObject, OSVERSIONINFOW *lpStruct)
{
	if (!OSVERSIONINFOFc.cached) cacheOSVERSIONINFOFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, OSVERSIONINFOFc.dwOSVersionInfoSize, (jint)lpStruct->dwOSVersionInfoSize);
	(*env)->SetIntField(env, lpObject, OSVERSIONINFOFc.dwMajorVersion, (jint)lpStruct->dwMajorVersion);
	(*env)->SetIntField(env, lpObject, OSVERSIONINFOFc.dwMinorVersion, (jint)lpStruct->dwMinorVersion);
	(*env)->SetIntField(env, lpObject, OSVERSIONINFOFc.dwBuildNumber, (jint)lpStruct->dwBuildNumber);
	(*env)->SetIntField(env, lpObject, OSVERSIONINFOFc.dwPlatformId, (jint)lpStruct->dwPlatformId);
}
#endif /* OSVERSIONINFOW */
#endif /* NO_OSVERSIONINFO */

#ifndef NO_PAINTSTRUCT
typedef struct PAINTSTRUCT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID hdc, fErase, left, top, right, bottom, fRestore, fIncUpdate /*, pad0, pad1, pad2, pad3, pad4, pad5, pad6, pad7 */;
} PAINTSTRUCT_FID_CACHE;

PAINTSTRUCT_FID_CACHE PAINTSTRUCTFc;

void cachePAINTSTRUCTFids(JNIEnv *env, jobject lpObject)
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
	PAINTSTRUCTFc.cached = 1;
}

PAINTSTRUCT *getPAINTSTRUCTFields(JNIEnv *env, jobject lpObject, PAINTSTRUCT *lpStruct)
{
	if (!PAINTSTRUCTFc.cached) cachePAINTSTRUCTFids(env, lpObject);
	lpStruct->hdc = (HDC)(*env)->GetIntField(env, lpObject, PAINTSTRUCTFc.hdc);
	lpStruct->fErase = (*env)->GetBooleanField(env, lpObject, PAINTSTRUCTFc.fErase);
	lpStruct->rcPaint.left = (*env)->GetIntField(env, lpObject, PAINTSTRUCTFc.left);
	lpStruct->rcPaint.top = (*env)->GetIntField(env, lpObject, PAINTSTRUCTFc.top);
	lpStruct->rcPaint.right = (*env)->GetIntField(env, lpObject, PAINTSTRUCTFc.right);
	lpStruct->rcPaint.bottom = (*env)->GetIntField(env, lpObject, PAINTSTRUCTFc.bottom);
	lpStruct->fRestore = (*env)->GetBooleanField(env, lpObject, PAINTSTRUCTFc.fRestore);
	lpStruct->fIncUpdate = (*env)->GetBooleanField(env, lpObject, PAINTSTRUCTFc.fIncUpdate);
	return lpStruct;
}

void setPAINTSTRUCTFields(JNIEnv *env, jobject lpObject, PAINTSTRUCT *lpStruct)
{
	if (!PAINTSTRUCTFc.cached) cachePAINTSTRUCTFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, PAINTSTRUCTFc.hdc, (jint)lpStruct->hdc);
	(*env)->SetBooleanField(env, lpObject, PAINTSTRUCTFc.fErase, (jboolean)lpStruct->fErase);
	(*env)->SetIntField(env, lpObject, PAINTSTRUCTFc.left, (jint)lpStruct->rcPaint.left);
	(*env)->SetIntField(env, lpObject, PAINTSTRUCTFc.top, (jint)lpStruct->rcPaint.top);
	(*env)->SetIntField(env, lpObject, PAINTSTRUCTFc.right, (jint)lpStruct->rcPaint.right);
	(*env)->SetIntField(env, lpObject, PAINTSTRUCTFc.bottom, (jint)lpStruct->rcPaint.bottom);
	(*env)->SetBooleanField(env, lpObject, PAINTSTRUCTFc.fRestore, (jboolean)lpStruct->fRestore);
	(*env)->SetBooleanField(env, lpObject, PAINTSTRUCTFc.fIncUpdate, (jboolean)lpStruct->fIncUpdate);
}
#endif /* NO_PAINTSTRUCT */

#ifndef NO_POINT
typedef struct POINT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID x, y;
} POINT_FID_CACHE;

POINT_FID_CACHE POINTFc;

void cachePOINTFids(JNIEnv *env, jobject lpObject)
{
	if (POINTFc.cached) return;
	POINTFc.clazz = (*env)->GetObjectClass(env, lpObject);
	POINTFc.x = (*env)->GetFieldID(env, POINTFc.clazz, "x", "I");
	POINTFc.y = (*env)->GetFieldID(env, POINTFc.clazz, "y", "I");
	POINTFc.cached = 1;
}

POINT *getPOINTFields(JNIEnv *env, jobject lpObject, POINT *lpStruct)
{
	if (!POINTFc.cached) cachePOINTFids(env, lpObject);
	lpStruct->x = (*env)->GetIntField(env, lpObject, POINTFc.x);
	lpStruct->y = (*env)->GetIntField(env, lpObject, POINTFc.y);
	return lpStruct;
}

void setPOINTFields(JNIEnv *env, jobject lpObject, POINT *lpStruct)
{
	if (!POINTFc.cached) cachePOINTFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, POINTFc.x, (jint)lpStruct->x);
	(*env)->SetIntField(env, lpObject, POINTFc.y, (jint)lpStruct->y);
}
#endif /* NO_POINT */

#ifndef NO_PRINTDLG
typedef struct PRINTDLG_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lStructSize, hwndOwner, hDevMode, hDevNames, hDC, Flags, nFromPage, nToPage, nMinPage, nMaxPage, nCopies, hInstance, lCustData, lpfnPrintHook, lpfnSetupHook, lpPrintTemplateName, lpSetupTemplateName, hPrintTemplate, hSetupTemplate;
} PRINTDLG_FID_CACHE;

PRINTDLG_FID_CACHE PRINTDLGFc;

void cachePRINTDLGFids(JNIEnv *env, jobject lpObject)
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
	if (!PRINTDLGFc.cached) cachePRINTDLGFids(env, lpObject);
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
	if (!PRINTDLGFc.cached) cachePRINTDLGFids(env, lpObject);
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
#endif /* NO_PRINTDLG */

#ifndef NO_REBARBANDINFO
typedef struct REBARBANDINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, fMask, fStyle, clrFore, clrBack, lpText, cch, iImage, hwndChild, cxMinChild, cyMinChild, cx, hbmBack, wID, cyChild, cyMaxChild, cyIntegral, cxIdeal, lParam;
#ifndef _WIN32_WCE
	jfieldID cxHeader;
#endif /* _WIN32_WCE */
} REBARBANDINFO_FID_CACHE;

REBARBANDINFO_FID_CACHE REBARBANDINFOFc;

void cacheREBARBANDINFOFids(JNIEnv *env, jobject lpObject)
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
#ifndef _WIN32_WCE
	REBARBANDINFOFc.cxHeader = (*env)->GetFieldID(env, REBARBANDINFOFc.clazz, "cxHeader", "I");
#endif /* _WIN32_WCE */
	REBARBANDINFOFc.cached = 1;
}

REBARBANDINFO *getREBARBANDINFOFields(JNIEnv *env, jobject lpObject, REBARBANDINFO *lpStruct)
{
	if (!REBARBANDINFOFc.cached) cacheREBARBANDINFOFids(env, lpObject);
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
#endif /* _WIN32_WCE */
	return lpStruct;
}

void setREBARBANDINFOFields(JNIEnv *env, jobject lpObject, REBARBANDINFO *lpStruct)
{
	if (!REBARBANDINFOFc.cached) cacheREBARBANDINFOFids(env, lpObject);
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
#endif /* _WIN32_WCE */
}
#endif /* NO_REBARBANDINFO */

#ifndef NO_RECT
typedef struct RECT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID left, top, right, bottom;
} RECT_FID_CACHE;

RECT_FID_CACHE RECTFc;

void cacheRECTFids(JNIEnv *env, jobject lpObject)
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
	if (!RECTFc.cached) cacheRECTFids(env, lpObject);
	lpStruct->left = (*env)->GetIntField(env, lpObject, RECTFc.left);
	lpStruct->top = (*env)->GetIntField(env, lpObject, RECTFc.top);
	lpStruct->right = (*env)->GetIntField(env, lpObject, RECTFc.right);
	lpStruct->bottom = (*env)->GetIntField(env, lpObject, RECTFc.bottom);
	return lpStruct;
}

void setRECTFields(JNIEnv *env, jobject lpObject, RECT *lpStruct)
{
	if (!RECTFc.cached) cacheRECTFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, RECTFc.left, (jint)lpStruct->left);
	(*env)->SetIntField(env, lpObject, RECTFc.top, (jint)lpStruct->top);
	(*env)->SetIntField(env, lpObject, RECTFc.right, (jint)lpStruct->right);
	(*env)->SetIntField(env, lpObject, RECTFc.bottom, (jint)lpStruct->bottom);
}
#endif /* NO_RECT */

#ifndef NO_SCROLLINFO
typedef struct SCROLLINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, fMask, nMin, nMax, nPage, nPos, nTrackPos;
} SCROLLINFO_FID_CACHE;

SCROLLINFO_FID_CACHE SCROLLINFOFc;

void cacheSCROLLINFOFids(JNIEnv *env, jobject lpObject)
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
	if (!SCROLLINFOFc.cached) cacheSCROLLINFOFids(env, lpObject);
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
	if (!SCROLLINFOFc.cached) cacheSCROLLINFOFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, SCROLLINFOFc.cbSize, (jint)lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, SCROLLINFOFc.fMask, (jint)lpStruct->fMask);
	(*env)->SetIntField(env, lpObject, SCROLLINFOFc.nMin, (jint)lpStruct->nMin);
	(*env)->SetIntField(env, lpObject, SCROLLINFOFc.nMax, (jint)lpStruct->nMax);
	(*env)->SetIntField(env, lpObject, SCROLLINFOFc.nPage, (jint)lpStruct->nPage);
	(*env)->SetIntField(env, lpObject, SCROLLINFOFc.nPos, (jint)lpStruct->nPos);
	(*env)->SetIntField(env, lpObject, SCROLLINFOFc.nTrackPos, (jint)lpStruct->nTrackPos);
}
#endif /* NO_SCROLLINFO */

#ifndef NO_SHACTIVATEINFO
typedef struct SHACTIVATEINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, hwndLastFocus, fSipUp, fSipOnDeactivation, fActive, fReserved;
} SHACTIVATEINFO_FID_CACHE;

SHACTIVATEINFO_FID_CACHE SHACTIVATEINFOFc;

void cacheSHACTIVATEINFOFids(JNIEnv *env, jobject lpObject)
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
	if (!SHACTIVATEINFOFc.cached) cacheSHACTIVATEINFOFids(env, lpObject);
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
	if (!SHACTIVATEINFOFc.cached) cacheSHACTIVATEINFOFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, SHACTIVATEINFOFc.cbSize, (jint)lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, SHACTIVATEINFOFc.hwndLastFocus, (jint)lpStruct->hwndLastFocus);
	(*env)->SetIntField(env, lpObject, SHACTIVATEINFOFc.fSipUp, (jint)lpStruct->fSipUp);
	(*env)->SetIntField(env, lpObject, SHACTIVATEINFOFc.fSipOnDeactivation, (jint)lpStruct->fSipOnDeactivation);
	(*env)->SetIntField(env, lpObject, SHACTIVATEINFOFc.fActive, (jint)lpStruct->fActive);
	(*env)->SetIntField(env, lpObject, SHACTIVATEINFOFc.fReserved, (jint)lpStruct->fReserved);
}
#endif /* NO_SHACTIVATEINFO */

#ifndef NO_SHELLEXECUTEINFO
typedef struct SHELLEXECUTEINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, fMask, hwnd, lpVerb, lpFile, lpParameters, lpDirectory, nShow, hInstApp, lpIDList, lpClass, hkeyClass, dwHotKey, hIcon, hProcess;
} SHELLEXECUTEINFO_FID_CACHE;

SHELLEXECUTEINFO_FID_CACHE SHELLEXECUTEINFOFc;

void cacheSHELLEXECUTEINFOFids(JNIEnv *env, jobject lpObject)
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
	if (!SHELLEXECUTEINFOFc.cached) cacheSHELLEXECUTEINFOFids(env, lpObject);
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
	if (!SHELLEXECUTEINFOFc.cached) cacheSHELLEXECUTEINFOFids(env, lpObject);
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
#endif /* NO_SHELLEXECUTEINFO */

#ifndef NO_SHMENUBARINFO
typedef struct SHMENUBARINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, hwndParent, dwFlags, nToolBarId, hInstRes, nBmpId, cBmpImages, hwndMB;
} SHMENUBARINFO_FID_CACHE;

SHMENUBARINFO_FID_CACHE SHMENUBARINFOFc;

void cacheSHMENUBARINFOFids(JNIEnv *env, jobject lpObject)
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
	if (!SHMENUBARINFOFc.cached) cacheSHMENUBARINFOFids(env, lpObject);
	lpStruct->cbSize = (DWORD)(*env)->GetIntField(env, lpObject, SHMENUBARINFOFc.cbSize);
	lpStruct->hwndParent = (HWND)(*env)->GetIntField(env, lpObject, SHMENUBARINFOFc.hwndParent);
	lpStruct->dwFlags = (DWORD)(*env)->GetIntField(env, lpObject, SHMENUBARINFOFc.dwFlags);
	lpStruct->nToolBarId = (UINT)(*env)->GetIntField(env, lpObject, SHMENUBARINFOFc.nToolBarId);
	lpStruct->hInstRes = (HINSTANCE)(*env)->GetIntField(env, lpObject, SHMENUBARINFOFc.hInstRes);
	lpStruct->nBmpId = (*env)->GetIntField(env, lpObject, SHMENUBARINFOFc.nBmpId);
	lpStruct->cBmpImages = (*env)->GetIntField(env, lpObject, SHMENUBARINFOFc.cBmpImages);
	lpStruct->hwndMB = (HWND)(*env)->GetIntField(env, lpObject, SHMENUBARINFOFc.hwndMB);
	return lpStruct;
}

void setSHMENUBARINFOFields(JNIEnv *env, jobject lpObject, SHMENUBARINFO *lpStruct)
{
	if (!SHMENUBARINFOFc.cached) cacheSHMENUBARINFOFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, SHMENUBARINFOFc.cbSize, (jint)lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, SHMENUBARINFOFc.hwndParent, (jint)lpStruct->hwndParent);
	(*env)->SetIntField(env, lpObject, SHMENUBARINFOFc.dwFlags, (jint)lpStruct->dwFlags);
	(*env)->SetIntField(env, lpObject, SHMENUBARINFOFc.nToolBarId, (jint)lpStruct->nToolBarId);
	(*env)->SetIntField(env, lpObject, SHMENUBARINFOFc.hInstRes, (jint)lpStruct->hInstRes);
	(*env)->SetIntField(env, lpObject, SHMENUBARINFOFc.nBmpId, (jint)lpStruct->nBmpId);
	(*env)->SetIntField(env, lpObject, SHMENUBARINFOFc.cBmpImages, (jint)lpStruct->cBmpImages);
	(*env)->SetIntField(env, lpObject, SHMENUBARINFOFc.hwndMB, (jint)lpStruct->hwndMB);
}
#endif /* NO_SHMENUBARINFO */

#ifndef NO_SIZE
typedef struct SIZE_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cx, cy;
} SIZE_FID_CACHE;

SIZE_FID_CACHE SIZEFc;

void cacheSIZEFids(JNIEnv *env, jobject lpObject)
{
	if (SIZEFc.cached) return;
	SIZEFc.clazz = (*env)->GetObjectClass(env, lpObject);
	SIZEFc.cx = (*env)->GetFieldID(env, SIZEFc.clazz, "cx", "I");
	SIZEFc.cy = (*env)->GetFieldID(env, SIZEFc.clazz, "cy", "I");
	SIZEFc.cached = 1;
}

SIZE *getSIZEFields(JNIEnv *env, jobject lpObject, SIZE *lpStruct)
{
	if (!SIZEFc.cached) cacheSIZEFids(env, lpObject);
	lpStruct->cx = (*env)->GetIntField(env, lpObject, SIZEFc.cx);
	lpStruct->cy = (*env)->GetIntField(env, lpObject, SIZEFc.cy);
	return lpStruct;
}

void setSIZEFields(JNIEnv *env, jobject lpObject, SIZE *lpStruct)
{
	if (!SIZEFc.cached) cacheSIZEFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, SIZEFc.cx, (jint)lpStruct->cx);
	(*env)->SetIntField(env, lpObject, SIZEFc.cy, (jint)lpStruct->cy);
}
#endif /* NO_SIZE */

#ifndef NO_TBBUTTON
typedef struct TBBUTTON_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID iBitmap, idCommand, fsState, fsStyle, dwData, iString;
} TBBUTTON_FID_CACHE;

TBBUTTON_FID_CACHE TBBUTTONFc;

void cacheTBBUTTONFids(JNIEnv *env, jobject lpObject)
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
	if (!TBBUTTONFc.cached) cacheTBBUTTONFids(env, lpObject);
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
	if (!TBBUTTONFc.cached) cacheTBBUTTONFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, TBBUTTONFc.iBitmap, (jint)lpStruct->iBitmap);
	(*env)->SetIntField(env, lpObject, TBBUTTONFc.idCommand, (jint)lpStruct->idCommand);
	(*env)->SetByteField(env, lpObject, TBBUTTONFc.fsState, (jbyte)lpStruct->fsState);
	(*env)->SetByteField(env, lpObject, TBBUTTONFc.fsStyle, (jbyte)lpStruct->fsStyle);
	(*env)->SetIntField(env, lpObject, TBBUTTONFc.dwData, (jint)lpStruct->dwData);
	(*env)->SetIntField(env, lpObject, TBBUTTONFc.iString, (jint)lpStruct->iString);
}
#endif /* NO_TBBUTTON */

#ifndef NO_TBBUTTONINFO
typedef struct TBBUTTONINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, dwMask, idCommand, iImage, fsState, fsStyle, cx, lParam, pszText, cchText;
} TBBUTTONINFO_FID_CACHE;

TBBUTTONINFO_FID_CACHE TBBUTTONINFOFc;

void cacheTBBUTTONINFOFids(JNIEnv *env, jobject lpObject)
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
	if (!TBBUTTONINFOFc.cached) cacheTBBUTTONINFOFids(env, lpObject);
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
	if (!TBBUTTONINFOFc.cached) cacheTBBUTTONINFOFids(env, lpObject);
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
#endif /* NO_TBBUTTONINFO */

#ifndef NO_TCITEM
typedef struct TCITEM_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID mask, dwState, dwStateMask, pszText, cchTextMax, iImage, lParam;
} TCITEM_FID_CACHE;

TCITEM_FID_CACHE TCITEMFc;

void cacheTCITEMFids(JNIEnv *env, jobject lpObject)
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
	if (!TCITEMFc.cached) cacheTCITEMFids(env, lpObject);
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
	if (!TCITEMFc.cached) cacheTCITEMFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, TCITEMFc.mask, (jint)lpStruct->mask);
	(*env)->SetIntField(env, lpObject, TCITEMFc.dwState, (jint)lpStruct->dwState);
	(*env)->SetIntField(env, lpObject, TCITEMFc.dwStateMask, (jint)lpStruct->dwStateMask);
	(*env)->SetIntField(env, lpObject, TCITEMFc.pszText, (jint)lpStruct->pszText);
	(*env)->SetIntField(env, lpObject, TCITEMFc.cchTextMax, (jint)lpStruct->cchTextMax);
	(*env)->SetIntField(env, lpObject, TCITEMFc.iImage, (jint)lpStruct->iImage);
	(*env)->SetIntField(env, lpObject, TCITEMFc.lParam, (jint)lpStruct->lParam);
}
#endif /* NO_TCITEM */

#ifndef NO_TEXTMETRIC
typedef struct TEXTMETRIC_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID tmHeight, tmAscent, tmDescent, tmInternalLeading, tmExternalLeading, tmAveCharWidth, tmMaxCharWidth, tmWeight, tmOverhang, tmDigitizedAspectX, tmDigitizedAspectY, tmItalic, tmUnderlined, tmStruckOut, tmPitchAndFamily, tmCharSet;
} TEXTMETRIC_FID_CACHE;

TEXTMETRIC_FID_CACHE TEXTMETRICFc;

void cacheTEXTMETRICFids(JNIEnv *env, jobject lpObject)
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
#ifndef NO_TEXTMETRICA
TEXTMETRICA* getTEXTMETRICAFields(JNIEnv *env, jobject lpObject, TEXTMETRICA *lpStruct)
{
	if (!TEXTMETRICFc.cached) cacheTEXTMETRICFids(env, lpObject);
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

void setTEXTMETRICAFields(JNIEnv *env, jobject lpObject, TEXTMETRICA *lpStruct)
{
	if (!TEXTMETRICFc.cached) cacheTEXTMETRICFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmHeight, lpStruct->tmHeight);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmAscent, lpStruct->tmAscent);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmDescent, lpStruct->tmDescent);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmInternalLeading, lpStruct->tmInternalLeading);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmExternalLeading, lpStruct->tmExternalLeading);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmAveCharWidth, lpStruct->tmAveCharWidth);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmMaxCharWidth, lpStruct->tmMaxCharWidth);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmWeight, lpStruct->tmWeight);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmOverhang, lpStruct->tmOverhang);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmDigitizedAspectX, lpStruct->tmDigitizedAspectX);
	(*env)->SetIntField(env, lpObject, TEXTMETRICFc.tmDigitizedAspectY, lpStruct->tmDigitizedAspectY);
	(*env)->SetByteField(env, lpObject, TEXTMETRICFc.tmItalic, lpStruct->tmItalic);
	(*env)->SetByteField(env, lpObject, TEXTMETRICFc.tmUnderlined, lpStruct->tmUnderlined);
	(*env)->SetByteField(env, lpObject, TEXTMETRICFc.tmStruckOut, lpStruct->tmStruckOut);
	(*env)->SetByteField(env, lpObject, TEXTMETRICFc.tmPitchAndFamily, lpStruct->tmPitchAndFamily);
	(*env)->SetByteField(env, lpObject, TEXTMETRICFc.tmCharSet, lpStruct->tmCharSet);
}
#endif /* NO_TEXTMETRICA */

#ifndef NO_TEXTMETRICW
TEXTMETRICW *getTEXTMETRICWFields(JNIEnv *env, jobject lpObject, TEXTMETRICW *lpStruct)
{
	if (!TEXTMETRICFc.cached) cacheTEXTMETRICFids(env, lpObject);
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

void setTEXTMETRICWFields(JNIEnv *env, jobject lpObject, TEXTMETRICW *lpStruct)
{
	if (!TEXTMETRICFc.cached) cacheTEXTMETRICFids(env, lpObject);
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
#endif /* NO_TEXTMETRICW */
#endif /* NO_TEXTMETRIC */

#ifndef NO_TOOLINFO
typedef struct TOOLINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, uFlags, hwnd, uId, left, top, right, bottom, hinst, lpszText, lParam;
} TOOLINFO_FID_CACHE;

TOOLINFO_FID_CACHE TOOLINFOFc;

void cacheTOOLINFOFids(JNIEnv *env, jobject lpObject)
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
	if (!TOOLINFOFc.cached) cacheTOOLINFOFids(env, lpObject);
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
	if (!TOOLINFOFc.cached) cacheTOOLINFOFids(env, lpObject);
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
#endif /* NO_TOOLINFO */

#ifndef NO_TRACKMOUSEEVENT
typedef struct TRACKMOUSEEVENT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, dwFlags, hwndTrack, dwHoverTime;
} TRACKMOUSEEVENT_FID_CACHE;

TRACKMOUSEEVENT_FID_CACHE TRACKMOUSEEVENTFc;

void cacheTRACKMOUSEEVENTFids(JNIEnv *env, jobject lpObject)
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
	if (!TRACKMOUSEEVENTFc.cached) cacheTRACKMOUSEEVENTFids(env, lpObject);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, TRACKMOUSEEVENTFc.cbSize);
	lpStruct->dwFlags = (*env)->GetIntField(env, lpObject, TRACKMOUSEEVENTFc.dwFlags);
	lpStruct->hwndTrack = (HWND)(*env)->GetIntField(env, lpObject, TRACKMOUSEEVENTFc.hwndTrack);
	lpStruct->dwHoverTime = (*env)->GetIntField(env, lpObject, TRACKMOUSEEVENTFc.dwHoverTime);
	return lpStruct;
}

void setTRACKMOUSEEVENTFields(JNIEnv *env, jobject lpObject, TRACKMOUSEEVENT *lpStruct)
{
	if (!TRACKMOUSEEVENTFc.cached) cacheTRACKMOUSEEVENTFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, TRACKMOUSEEVENTFc.cbSize, (jint)lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, TRACKMOUSEEVENTFc.dwFlags, (jint)lpStruct->dwFlags);
	(*env)->SetIntField(env, lpObject, TRACKMOUSEEVENTFc.hwndTrack, (jint)lpStruct->hwndTrack);
	(*env)->SetIntField(env, lpObject, TRACKMOUSEEVENTFc.dwHoverTime, (jint)lpStruct->dwHoverTime);
}
#endif /* NO_TRACKMOUSEEVENT */

#ifndef NO_TRIVERTEX
typedef struct TRIVERTEX_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID x, y, Red, Green, Blue, Alpha;
} TRIVERTEX_FID_CACHE;

TRIVERTEX_FID_CACHE TRIVERTEXFc;

void cacheTRIVERTEXFids(JNIEnv *env, jobject lpObject)
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
	if (!TRIVERTEXFc.cached) cacheTRIVERTEXFids(env, lpObject);
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
	if (!TRIVERTEXFc.cached) cacheTRIVERTEXFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, TRIVERTEXFc.x, (jint)lpStruct->x);
	(*env)->SetIntField(env, lpObject, TRIVERTEXFc.y, (jint)lpStruct->y);
	(*env)->SetShortField(env, lpObject, TRIVERTEXFc.Red, (jshort)lpStruct->Red);
	(*env)->SetShortField(env, lpObject, TRIVERTEXFc.Green, (jshort)lpStruct->Green);
	(*env)->SetShortField(env, lpObject, TRIVERTEXFc.Blue, (jshort)lpStruct->Blue);
	(*env)->SetShortField(env, lpObject, TRIVERTEXFc.Alpha, (jshort)lpStruct->Alpha);
}
#endif /* NO_TRIVERTEX */

#ifndef NO_TVHITTESTINFO
typedef struct TVHITTESTINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID x, y, flags, hItem;
} TVHITTESTINFO_FID_CACHE;

TVHITTESTINFO_FID_CACHE TVHITTESTINFOFc;

void cacheTVHITTESTINFOFids(JNIEnv *env, jobject lpObject)
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
	if (!TVHITTESTINFOFc.cached) cacheTVHITTESTINFOFids(env, lpObject);
	lpStruct->pt.x = (*env)->GetIntField(env, lpObject, TVHITTESTINFOFc.x);
	lpStruct->pt.y = (*env)->GetIntField(env, lpObject, TVHITTESTINFOFc.y);
	lpStruct->flags = (*env)->GetIntField(env, lpObject, TVHITTESTINFOFc.flags);
	lpStruct->hItem = (HTREEITEM)(*env)->GetIntField(env, lpObject, TVHITTESTINFOFc.hItem);
	return lpStruct;
}

void setTVHITTESTINFOFields(JNIEnv *env, jobject lpObject, TVHITTESTINFO *lpStruct)
{
	if (!TVHITTESTINFOFc.cached) cacheTVHITTESTINFOFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, TVHITTESTINFOFc.x, (jint)lpStruct->pt.x);
	(*env)->SetIntField(env, lpObject, TVHITTESTINFOFc.y, (jint)lpStruct->pt.y);
	(*env)->SetIntField(env, lpObject, TVHITTESTINFOFc.flags, (jint)lpStruct->flags);
	(*env)->SetIntField(env, lpObject, TVHITTESTINFOFc.hItem, (jint)lpStruct->hItem);
}
#endif /* NO_TVHITTESTINFO */

#ifndef NO_TVINSERTSTRUCT
typedef struct TVINSERTSTRUCT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID hParent, hInsertAfter, mask, hItem, state, stateMask, pszText, cchTextMax, iImage, iSelectedImage, cChildren, lParam;
} TVINSERTSTRUCT_FID_CACHE;

TVINSERTSTRUCT_FID_CACHE TVINSERTSTRUCTFc;

void cacheTVINSERTSTRUCTFids(JNIEnv *env, jobject lpObject)
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
	if (!TVINSERTSTRUCTFc.cached) cacheTVINSERTSTRUCTFids(env, lpObject);
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
	if (!TVINSERTSTRUCTFc.cached) cacheTVINSERTSTRUCTFids(env, lpObject);
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
#endif /* NO_TVINSERTSTRUCT */

#ifndef NO_TVITEM
typedef struct TVITEM_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID mask, hItem, state, stateMask, pszText, cchTextMax, iImage, iSelectedImage, cChildren, lParam;
} TVITEM_FID_CACHE;

TVITEM_FID_CACHE TVITEMFc;

void cacheTVITEMFids(JNIEnv *env, jobject lpObject)
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
	if (!TVITEMFc.cached) cacheTVITEMFids(env, lpObject);
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
	if (!TVITEMFc.cached) cacheTVITEMFids(env, lpObject);
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
#endif /* NO_TVITEM */

#ifndef NO_WINDOWPLACEMENT
typedef struct WINDOWPLACEMENT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID length, flags, showCmd, ptMinPosition_x, ptMinPosition_y, ptMaxPosition_x, ptMaxPosition_y, left, top, right, bottom;
} WINDOWPLACEMENT_FID_CACHE;

WINDOWPLACEMENT_FID_CACHE WINDOWPLACEMENTFc;

void cacheWINDOWPLACEMENTFids(JNIEnv *env, jobject lpObject)
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
	if (!WINDOWPLACEMENTFc.cached) cacheWINDOWPLACEMENTFids(env, lpObject);
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
	if (!WINDOWPLACEMENTFc.cached) cacheWINDOWPLACEMENTFids(env, lpObject);
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
#endif /* NO_WINDOWPLACEMENT */

#ifndef NO_WINDOWPOS
typedef struct WINDOWPOS_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID hwnd, hwndInsertAfter, x, y, cx, cy, flags;
} WINDOWPOS_FID_CACHE;

WINDOWPOS_FID_CACHE WINDOWPOSFc;

void cacheWINDOWPOSFids(JNIEnv *env, jobject lpObject)
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
	if (!WINDOWPOSFc.cached) cacheWINDOWPOSFids(env, lpObject);
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
	if (!WINDOWPOSFc.cached) cacheWINDOWPOSFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, WINDOWPOSFc.hwnd, (jint)lpStruct->hwnd);
	(*env)->SetIntField(env, lpObject, WINDOWPOSFc.hwndInsertAfter, (jint)lpStruct->hwndInsertAfter);
	(*env)->SetIntField(env, lpObject, WINDOWPOSFc.x, (jint)lpStruct->x);
	(*env)->SetIntField(env, lpObject, WINDOWPOSFc.y, (jint)lpStruct->y);
	(*env)->SetIntField(env, lpObject, WINDOWPOSFc.cx, (jint)lpStruct->cx);
	(*env)->SetIntField(env, lpObject, WINDOWPOSFc.cy, (jint)lpStruct->cy);
	(*env)->SetIntField(env, lpObject, WINDOWPOSFc.flags, (jint)lpStruct->flags);
}
#endif /* NO_WINDOWPOS */

#ifndef NO_WNDCLASS
typedef struct WNDCLASS_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID style, lpfnWndProc, cbClsExtra, cbWndExtra, hInstance, hIcon, hCursor, hbrBackground, lpszMenuName, lpszClassName;
} WNDCLASS_FID_CACHE;

WNDCLASS_FID_CACHE WNDCLASSFc;

void cacheWNDCLASSFids(JNIEnv *env, jobject lpObject)
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
	if (!WNDCLASSFc.cached) cacheWNDCLASSFids(env, lpObject);
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
	if (!WNDCLASSFc.cached) cacheWNDCLASSFids(env, lpObject);
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
#endif /* NO_WNDCLASS */

/************************ OLE ***************************/

#ifndef NO_CAUUID
typedef struct CAUUID_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cElems, pElems;
} CAUUID_FID_CACHE;

CAUUID_FID_CACHE CAUUIDFc;

void cacheCAUUIDFids(JNIEnv *env, jobject lpObject)
{
	if (CAUUIDFc.cached) return;
	CAUUIDFc.clazz = (*env)->GetObjectClass(env, lpObject);
	CAUUIDFc.cElems = (*env)->GetFieldID(env, CAUUIDFc.clazz, "cElems", "I");
	CAUUIDFc.pElems = (*env)->GetFieldID(env, CAUUIDFc.clazz, "pElems", "I");
	CAUUIDFc.cached = 1;
}

CAUUID *getCAUUIDFields(JNIEnv *env, jobject lpObject, CAUUID *lpStruct)
{
	if (!CAUUIDFc.cached) cacheCAUUIDFids(env, lpObject);
	lpStruct->cElems = (*env)->GetIntField(env, lpObject, CAUUIDFc.cElems);
	lpStruct->pElems = (GUID FAR *)(*env)->GetIntField(env, lpObject, CAUUIDFc.pElems);
	return lpStruct;
}

void setCAUUIDFields(JNIEnv *env, jobject lpObject, CAUUID *lpStruct)
{
	if (!CAUUIDFc.cached) cacheCAUUIDFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, CAUUIDFc.cElems, (jint)lpStruct->cElems);
	(*env)->SetIntField(env, lpObject, CAUUIDFc.pElems, (jint)lpStruct->pElems);
}
#endif /* NO_CAUUID */

#ifndef NO_CONTROLINFO
typedef struct CONTROLINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cb, hAccel, cAccel, dwFlags;
} CONTROLINFO_FID_CACHE;

CONTROLINFO_FID_CACHE CONTROLINFOFc;

void cacheCONTROLINFOFids(JNIEnv *env, jobject lpObject)
{
	if (CONTROLINFOFc.cached) return;
	CONTROLINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	CONTROLINFOFc.cb = (*env)->GetFieldID(env, CONTROLINFOFc.clazz, "cb", "I");
	CONTROLINFOFc.hAccel = (*env)->GetFieldID(env, CONTROLINFOFc.clazz, "hAccel", "I");
	CONTROLINFOFc.cAccel = (*env)->GetFieldID(env, CONTROLINFOFc.clazz, "cAccel", "S");
	CONTROLINFOFc.dwFlags = (*env)->GetFieldID(env, CONTROLINFOFc.clazz, "dwFlags", "I");
	CONTROLINFOFc.cached = 1;
}

CONTROLINFO *getCONTROLINFOFields(JNIEnv *env, jobject lpObject, CONTROLINFO *lpStruct)
{
	if (!CONTROLINFOFc.cached) cacheCONTROLINFOFids(env, lpObject);
	lpStruct->cb = (*env)->GetIntField(env, lpObject, CONTROLINFOFc.cb);
	lpStruct->hAccel = (HACCEL)(*env)->GetIntField(env, lpObject, CONTROLINFOFc.hAccel);
	lpStruct->cAccel = (*env)->GetShortField(env, lpObject, CONTROLINFOFc.cAccel);
	lpStruct->dwFlags = (*env)->GetIntField(env, lpObject, CONTROLINFOFc.dwFlags);
	return lpStruct;
}

void setCONTROLINFOFields(JNIEnv *env, jobject lpObject, CONTROLINFO *lpStruct)
{
	if (!CONTROLINFOFc.cached) cacheCONTROLINFOFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, CONTROLINFOFc.cb, (jint)lpStruct->cb);
	(*env)->SetIntField(env, lpObject, CONTROLINFOFc.hAccel, (jint)lpStruct->hAccel);
	(*env)->SetShortField(env, lpObject, CONTROLINFOFc.cAccel, (jshort)lpStruct->cAccel);
	(*env)->SetIntField(env, lpObject, CONTROLINFOFc.dwFlags, (jint)lpStruct->dwFlags);
}
#endif /* NO_CONTROLINFO */

#ifndef NO_COSERVERINFO
typedef struct COSERVERINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID dwReserved1, pwszName, pAuthInfo, dwReserved2;
} COSERVERINFO_FID_CACHE;

COSERVERINFO_FID_CACHE COSERVERINFOFc;

void cacheCOSERVERINFOFids(JNIEnv *env, jobject lpObject)
{
	if (COSERVERINFOFc.cached) return;
	COSERVERINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	COSERVERINFOFc.dwReserved1 = (*env)->GetFieldID(env, COSERVERINFOFc.clazz, "dwReserved1", "I");
	COSERVERINFOFc.pwszName = (*env)->GetFieldID(env, COSERVERINFOFc.clazz, "pwszName", "I");
	COSERVERINFOFc.pAuthInfo = (*env)->GetFieldID(env, COSERVERINFOFc.clazz, "pAuthInfo", "I");
	COSERVERINFOFc.dwReserved2 = (*env)->GetFieldID(env, COSERVERINFOFc.clazz, "dwReserved2", "I");
	COSERVERINFOFc.cached = 1;
}

COSERVERINFO *getCOSERVERINFOFields(JNIEnv *env, jobject lpObject, COSERVERINFO *lpStruct)
{
	if (!COSERVERINFOFc.cached) cacheCOSERVERINFOFids(env, lpObject);
	lpStruct->dwReserved1 = (*env)->GetIntField(env, lpObject, COSERVERINFOFc.dwReserved1);
	lpStruct->pwszName = (LPWSTR)(*env)->GetIntField(env, lpObject, COSERVERINFOFc.pwszName);
	lpStruct->pAuthInfo = (COAUTHINFO *)(*env)->GetIntField(env, lpObject, COSERVERINFOFc.pAuthInfo);
	lpStruct->dwReserved2 = (*env)->GetIntField(env, lpObject, COSERVERINFOFc.dwReserved2);
	return lpStruct;
}

void setCOSERVERINFOFields(JNIEnv *env, jobject lpObject, COSERVERINFO *lpStruct)
{
	if (!COSERVERINFOFc.cached) cacheCOSERVERINFOFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, COSERVERINFOFc.dwReserved1, (jint)lpStruct->dwReserved1);
	(*env)->SetIntField(env, lpObject, COSERVERINFOFc.pwszName, (jint)lpStruct->pwszName);
	(*env)->SetIntField(env, lpObject, COSERVERINFOFc.pAuthInfo, (jint)lpStruct->pAuthInfo);
	(*env)->SetIntField(env, lpObject, COSERVERINFOFc.dwReserved2, (jint)lpStruct->dwReserved2);
}
#endif /* NO_COSERVERINFO */

#ifndef NO_DISPPARAMS
typedef struct DISPPARAMS_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID rgvarg, rgdispidNamedArgs, cArgs, cNamedArgs;
} DISPPARAMS_FID_CACHE;

DISPPARAMS_FID_CACHE DISPPARAMSFc;

void cacheDISPPARAMSFids(JNIEnv *env, jobject lpObject)
{
	if (DISPPARAMSFc.cached) return;
	DISPPARAMSFc.clazz = (*env)->GetObjectClass(env, lpObject);
	DISPPARAMSFc.rgvarg = (*env)->GetFieldID(env, DISPPARAMSFc.clazz, "rgvarg", "I");
	DISPPARAMSFc.rgdispidNamedArgs = (*env)->GetFieldID(env, DISPPARAMSFc.clazz, "rgdispidNamedArgs", "I");
	DISPPARAMSFc.cArgs = (*env)->GetFieldID(env, DISPPARAMSFc.clazz, "cArgs", "I");
	DISPPARAMSFc.cNamedArgs = (*env)->GetFieldID(env, DISPPARAMSFc.clazz, "cNamedArgs", "I");
	DISPPARAMSFc.cached = 1;
}

DISPPARAMS *getDISPPARAMSFields(JNIEnv *env, jobject lpObject, DISPPARAMS *lpStruct)
{
	if (!DISPPARAMSFc.cached) cacheDISPPARAMSFids(env, lpObject);
	lpStruct->rgvarg = (VARIANTARG FAR *)(*env)->GetIntField(env, lpObject, DISPPARAMSFc.rgvarg);
	lpStruct->rgdispidNamedArgs = (DISPID FAR *)(*env)->GetIntField(env, lpObject, DISPPARAMSFc.rgdispidNamedArgs);
	lpStruct->cArgs = (*env)->GetIntField(env, lpObject, DISPPARAMSFc.cArgs);
	lpStruct->cNamedArgs = (*env)->GetIntField(env, lpObject, DISPPARAMSFc.cNamedArgs);
	return lpStruct;
}

void setDISPPARAMSFields(JNIEnv *env, jobject lpObject, DISPPARAMS *lpStruct)
{
	if (!DISPPARAMSFc.cached) cacheDISPPARAMSFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, DISPPARAMSFc.rgvarg, (jint)lpStruct->rgvarg);
	(*env)->SetIntField(env, lpObject, DISPPARAMSFc.rgdispidNamedArgs, (jint)lpStruct->rgdispidNamedArgs);
	(*env)->SetIntField(env, lpObject, DISPPARAMSFc.cArgs, (jint)lpStruct->cArgs);
	(*env)->SetIntField(env, lpObject, DISPPARAMSFc.cNamedArgs, (jint)lpStruct->cNamedArgs);
}
#endif /* NO_DISPPARAMS */

#ifndef NO_DVTARGETDEVICE
typedef struct DVTARGETDEVICE_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID tdSize, tdDriverNameOffset, tdDeviceNameOffset, tdPortNameOffset, tdExtDevmodeOffset, tdData;
} DVTARGETDEVICE_FID_CACHE;

DVTARGETDEVICE_FID_CACHE DVTARGETDEVICEFc;

void cacheDVTARGETDEVICEFids(JNIEnv *env, jobject lpObject)
{
	if (DVTARGETDEVICEFc.cached) return;
	DVTARGETDEVICEFc.clazz = (*env)->GetObjectClass(env, lpObject);
	DVTARGETDEVICEFc.tdSize = (*env)->GetFieldID(env, DVTARGETDEVICEFc.clazz, "tdSize", "I");
	DVTARGETDEVICEFc.tdDriverNameOffset = (*env)->GetFieldID(env, DVTARGETDEVICEFc.clazz, "tdDriverNameOffset", "S");
	DVTARGETDEVICEFc.tdDeviceNameOffset = (*env)->GetFieldID(env, DVTARGETDEVICEFc.clazz, "tdDeviceNameOffset", "S");
	DVTARGETDEVICEFc.tdPortNameOffset = (*env)->GetFieldID(env, DVTARGETDEVICEFc.clazz, "tdPortNameOffset", "S");
	DVTARGETDEVICEFc.tdExtDevmodeOffset = (*env)->GetFieldID(env, DVTARGETDEVICEFc.clazz, "tdExtDevmodeOffset", "S");
	DVTARGETDEVICEFc.tdData = (*env)->GetFieldID(env, DVTARGETDEVICEFc.clazz, "tdData", "B");
	DVTARGETDEVICEFc.cached = 1;
}

DVTARGETDEVICE *getDVTARGETDEVICEFields(JNIEnv *env, jobject lpObject, DVTARGETDEVICE *lpStruct)
{
	if (!DVTARGETDEVICEFc.cached) cacheDVTARGETDEVICEFids(env, lpObject);
	lpStruct->tdSize = (*env)->GetIntField(env, lpObject, DVTARGETDEVICEFc.tdSize);
	lpStruct->tdDriverNameOffset = (*env)->GetShortField(env, lpObject, DVTARGETDEVICEFc.tdDriverNameOffset);
	lpStruct->tdDeviceNameOffset = (*env)->GetShortField(env, lpObject, DVTARGETDEVICEFc.tdDeviceNameOffset);
	lpStruct->tdPortNameOffset = (*env)->GetShortField(env, lpObject, DVTARGETDEVICEFc.tdPortNameOffset);
	lpStruct->tdExtDevmodeOffset = (*env)->GetShortField(env, lpObject, DVTARGETDEVICEFc.tdExtDevmodeOffset);
	*lpStruct->tdData = (*env)->GetByteField(env, lpObject, DVTARGETDEVICEFc.tdData);
	return lpStruct;
}

void setDVTARGETDEVICEFields(JNIEnv *env, jobject lpObject, DVTARGETDEVICE *lpStruct)
{
	if (!DVTARGETDEVICEFc.cached) cacheDVTARGETDEVICEFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, DVTARGETDEVICEFc.tdSize, (jint)lpStruct->tdSize);
	(*env)->SetShortField(env, lpObject, DVTARGETDEVICEFc.tdDriverNameOffset, (jshort)lpStruct->tdDriverNameOffset);
	(*env)->SetShortField(env, lpObject, DVTARGETDEVICEFc.tdDeviceNameOffset, (jshort)lpStruct->tdDeviceNameOffset);
	(*env)->SetShortField(env, lpObject, DVTARGETDEVICEFc.tdPortNameOffset, (jshort)lpStruct->tdPortNameOffset);
	(*env)->SetShortField(env, lpObject, DVTARGETDEVICEFc.tdExtDevmodeOffset, (jshort)lpStruct->tdExtDevmodeOffset);
	(*env)->SetByteField(env, lpObject, DVTARGETDEVICEFc.tdData, (jbyte)*lpStruct->tdData);
}
#endif /* NO_DVTARGETDEVICE */

#ifndef NO_EXCEPINFO
typedef struct EXCEPINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID wCode, wReserved, bstrSource, bstrDescription, bstrHelpFile, dwHelpContext, pvReserved, pfnDeferredFillIn, scode;
} EXCEPINFO_FID_CACHE;

EXCEPINFO_FID_CACHE EXCEPINFOFc;

void cacheEXCEPINFOFids(JNIEnv *env, jobject lpObject)
{
	if (EXCEPINFOFc.cached) return;
	EXCEPINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	EXCEPINFOFc.wCode = (*env)->GetFieldID(env, EXCEPINFOFc.clazz, "wCode", "S");
	EXCEPINFOFc.wReserved = (*env)->GetFieldID(env, EXCEPINFOFc.clazz, "wReserved", "S");
	EXCEPINFOFc.bstrSource = (*env)->GetFieldID(env, EXCEPINFOFc.clazz, "bstrSource", "I");
	EXCEPINFOFc.bstrDescription = (*env)->GetFieldID(env, EXCEPINFOFc.clazz, "bstrDescription", "I");
	EXCEPINFOFc.bstrHelpFile = (*env)->GetFieldID(env, EXCEPINFOFc.clazz, "bstrHelpFile", "I");
	EXCEPINFOFc.dwHelpContext = (*env)->GetFieldID(env, EXCEPINFOFc.clazz, "dwHelpContext", "I");
	EXCEPINFOFc.pvReserved = (*env)->GetFieldID(env, EXCEPINFOFc.clazz, "pvReserved", "I");
	EXCEPINFOFc.pfnDeferredFillIn = (*env)->GetFieldID(env, EXCEPINFOFc.clazz, "pfnDeferredFillIn", "I");
	EXCEPINFOFc.scode = (*env)->GetFieldID(env, EXCEPINFOFc.clazz, "scode", "I");
	EXCEPINFOFc.cached = 1;
}

EXCEPINFO *getEXCEPINFOFields(JNIEnv *env, jobject lpObject, EXCEPINFO *lpStruct)
{
	if (!EXCEPINFOFc.cached) cacheEXCEPINFOFids(env, lpObject);
	lpStruct->wCode = (*env)->GetShortField(env, lpObject, EXCEPINFOFc.wCode);
	lpStruct->wReserved = (*env)->GetShortField(env, lpObject, EXCEPINFOFc.wReserved);
	lpStruct->bstrSource = (BSTR)(*env)->GetIntField(env, lpObject, EXCEPINFOFc.bstrSource);
	lpStruct->bstrDescription = (BSTR)(*env)->GetIntField(env, lpObject, EXCEPINFOFc.bstrDescription);
	lpStruct->bstrHelpFile = (BSTR)(*env)->GetIntField(env, lpObject, EXCEPINFOFc.bstrHelpFile);
	lpStruct->dwHelpContext = (*env)->GetIntField(env, lpObject, EXCEPINFOFc.dwHelpContext);
	lpStruct->pvReserved = (void FAR *)(*env)->GetIntField(env, lpObject, EXCEPINFOFc.pvReserved);
	lpStruct->pfnDeferredFillIn = (HRESULT (STDAPICALLTYPE FAR* )(struct tagEXCEPINFO FAR*))(*env)->GetIntField(env, lpObject, EXCEPINFOFc.pfnDeferredFillIn);
	lpStruct->scode = (*env)->GetIntField(env, lpObject, EXCEPINFOFc.scode);
	return lpStruct;
}

void setEXCEPINFOFields(JNIEnv *env, jobject lpObject, EXCEPINFO *lpStruct)
{
	if (!EXCEPINFOFc.cached) cacheEXCEPINFOFids(env, lpObject);
	(*env)->SetShortField(env, lpObject, EXCEPINFOFc.wCode, (jshort)lpStruct->wCode);
	(*env)->SetShortField(env, lpObject, EXCEPINFOFc.wReserved, (jshort)lpStruct->wReserved);
	(*env)->SetIntField(env, lpObject, EXCEPINFOFc.bstrSource, (jint)lpStruct->bstrSource);
	(*env)->SetIntField(env, lpObject, EXCEPINFOFc.bstrDescription, (jint)lpStruct->bstrDescription);
	(*env)->SetIntField(env, lpObject, EXCEPINFOFc.bstrHelpFile, (jint)lpStruct->bstrHelpFile);
	(*env)->SetIntField(env, lpObject, EXCEPINFOFc.dwHelpContext, (jint)lpStruct->dwHelpContext);
	(*env)->SetIntField(env, lpObject, EXCEPINFOFc.pvReserved, (jint)lpStruct->pvReserved);
	(*env)->SetIntField(env, lpObject, EXCEPINFOFc.pfnDeferredFillIn, (jint)lpStruct->pfnDeferredFillIn);
	(*env)->SetIntField(env, lpObject, EXCEPINFOFc.scode, (jint)lpStruct->scode);
}
#endif /* NO_EXCEPINFO */

#ifndef NO_FORMATETC
typedef struct FORMATETC_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cfFormat, ptd, dwAspect, lindex, tymed;
} FORMATETC_FID_CACHE;

FORMATETC_FID_CACHE FORMATETCFc;

void cacheFORMATETCFids(JNIEnv *env, jobject lpObject)
{
	if (FORMATETCFc.cached) return;
	FORMATETCFc.clazz = (*env)->GetObjectClass(env, lpObject);
	FORMATETCFc.cfFormat = (*env)->GetFieldID(env, FORMATETCFc.clazz, "cfFormat", "I");
	FORMATETCFc.ptd = (*env)->GetFieldID(env, FORMATETCFc.clazz, "ptd", "I");
	FORMATETCFc.dwAspect = (*env)->GetFieldID(env, FORMATETCFc.clazz, "dwAspect", "I");
	FORMATETCFc.lindex = (*env)->GetFieldID(env, FORMATETCFc.clazz, "lindex", "I");
	FORMATETCFc.tymed = (*env)->GetFieldID(env, FORMATETCFc.clazz, "tymed", "I");
	FORMATETCFc.cached = 1;
}

FORMATETC *getFORMATETCFields(JNIEnv *env, jobject lpObject, FORMATETC *lpStruct)
{
	if (!FORMATETCFc.cached) cacheFORMATETCFids(env, lpObject);
	lpStruct->cfFormat = (CLIPFORMAT)(*env)->GetIntField(env, lpObject, FORMATETCFc.cfFormat);
	lpStruct->ptd = (DVTARGETDEVICE *)(*env)->GetIntField(env, lpObject, FORMATETCFc.ptd);
	lpStruct->dwAspect = (*env)->GetIntField(env, lpObject, FORMATETCFc.dwAspect);
	lpStruct->lindex = (*env)->GetIntField(env, lpObject, FORMATETCFc.lindex);
	lpStruct->tymed = (*env)->GetIntField(env, lpObject, FORMATETCFc.tymed);
	return lpStruct;
}

void setFORMATETCFields(JNIEnv *env, jobject lpObject, FORMATETC *lpStruct)
{
	if (!FORMATETCFc.cached) cacheFORMATETCFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, FORMATETCFc.cfFormat, (jint)lpStruct->cfFormat);
	(*env)->SetIntField(env, lpObject, FORMATETCFc.ptd, (jint)lpStruct->ptd);
	(*env)->SetIntField(env, lpObject, FORMATETCFc.dwAspect, (jint)lpStruct->dwAspect);
	(*env)->SetIntField(env, lpObject, FORMATETCFc.lindex, (jint)lpStruct->lindex);
	(*env)->SetIntField(env, lpObject, FORMATETCFc.tymed, (jint)lpStruct->tymed);
}
#endif /* NO_FORMATETC */

#ifndef NO_FUNCDESC1
typedef struct FUNCDESC1_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID memid, lprgscode, lprgelemdescParam, funckind, invkind, callconv, cParams, cParamsOpt, oVft, cScodes, elemdescFunc_tdesc_union, elemdescFunc_tdesc_vt, elemdescFunc_paramdesc_pparamdescex, elemdescFunc_paramdesc_wParamFlags, wFuncFlags;
} FUNCDESC1_FID_CACHE;

FUNCDESC1_FID_CACHE FUNCDESC1Fc;

void cacheFUNCDESC1Fids(JNIEnv *env, jobject lpObject)
{
	if (FUNCDESC1Fc.cached) return;
	FUNCDESC1Fc.clazz = (*env)->GetObjectClass(env, lpObject);
	FUNCDESC1Fc.memid = (*env)->GetFieldID(env, FUNCDESC1Fc.clazz, "memid", "I");
	FUNCDESC1Fc.lprgscode = (*env)->GetFieldID(env, FUNCDESC1Fc.clazz, "lprgscode", "I");
	FUNCDESC1Fc.lprgelemdescParam = (*env)->GetFieldID(env, FUNCDESC1Fc.clazz, "lprgelemdescParam", "I");
	FUNCDESC1Fc.funckind = (*env)->GetFieldID(env, FUNCDESC1Fc.clazz, "funckind", "I");
	FUNCDESC1Fc.invkind = (*env)->GetFieldID(env, FUNCDESC1Fc.clazz, "invkind", "I");
	FUNCDESC1Fc.callconv = (*env)->GetFieldID(env, FUNCDESC1Fc.clazz, "callconv", "I");
	FUNCDESC1Fc.cParams = (*env)->GetFieldID(env, FUNCDESC1Fc.clazz, "cParams", "S");
	FUNCDESC1Fc.cParamsOpt = (*env)->GetFieldID(env, FUNCDESC1Fc.clazz, "cParamsOpt", "S");
	FUNCDESC1Fc.oVft = (*env)->GetFieldID(env, FUNCDESC1Fc.clazz, "oVft", "S");
	FUNCDESC1Fc.cScodes = (*env)->GetFieldID(env, FUNCDESC1Fc.clazz, "cScodes", "S");
	FUNCDESC1Fc.elemdescFunc_tdesc_union = (*env)->GetFieldID(env, FUNCDESC1Fc.clazz, "elemdescFunc_tdesc_union", "I");
	FUNCDESC1Fc.elemdescFunc_tdesc_vt = (*env)->GetFieldID(env, FUNCDESC1Fc.clazz, "elemdescFunc_tdesc_vt", "S");
	FUNCDESC1Fc.elemdescFunc_paramdesc_pparamdescex = (*env)->GetFieldID(env, FUNCDESC1Fc.clazz, "elemdescFunc_paramdesc_pparamdescex", "I");
	FUNCDESC1Fc.elemdescFunc_paramdesc_wParamFlags = (*env)->GetFieldID(env, FUNCDESC1Fc.clazz, "elemdescFunc_paramdesc_wParamFlags", "S");
	FUNCDESC1Fc.wFuncFlags = (*env)->GetFieldID(env, FUNCDESC1Fc.clazz, "wFuncFlags", "S");
	FUNCDESC1Fc.cached = 1;
}

FUNCDESC *getFUNCDESC1Fields(JNIEnv *env, jobject lpObject, FUNCDESC *lpStruct)
{
	if (!FUNCDESC1Fc.cached) cacheFUNCDESC1Fids(env, lpObject);
	lpStruct->memid = (*env)->GetIntField(env, lpObject, FUNCDESC1Fc.memid);
	lpStruct->lprgscode = (SCODE FAR *)(*env)->GetIntField(env, lpObject, FUNCDESC1Fc.lprgscode);
	lpStruct->lprgelemdescParam = (ELEMDESC FAR *)(*env)->GetIntField(env, lpObject, FUNCDESC1Fc.lprgelemdescParam);
	lpStruct->funckind = (*env)->GetIntField(env, lpObject, FUNCDESC1Fc.funckind);
	lpStruct->invkind = (*env)->GetIntField(env, lpObject, FUNCDESC1Fc.invkind);
	lpStruct->callconv = (*env)->GetIntField(env, lpObject, FUNCDESC1Fc.callconv);
	lpStruct->cParams = (*env)->GetShortField(env, lpObject, FUNCDESC1Fc.cParams);
	lpStruct->cParamsOpt = (*env)->GetShortField(env, lpObject, FUNCDESC1Fc.cParamsOpt);
	lpStruct->oVft = (*env)->GetShortField(env, lpObject, FUNCDESC1Fc.oVft);
	lpStruct->cScodes = (*env)->GetShortField(env, lpObject, FUNCDESC1Fc.cScodes);
	lpStruct->elemdescFunc.tdesc.lptdesc = (struct FARSTRUCT tagTYPEDESC FAR* )(*env)->GetIntField(env, lpObject, FUNCDESC1Fc.elemdescFunc_tdesc_union);
	lpStruct->elemdescFunc.tdesc.vt = (*env)->GetShortField(env, lpObject, FUNCDESC1Fc.elemdescFunc_tdesc_vt);
	lpStruct->elemdescFunc.paramdesc.pparamdescex = (LPPARAMDESCEX)(*env)->GetIntField(env, lpObject, FUNCDESC1Fc.elemdescFunc_paramdesc_pparamdescex);
	lpStruct->elemdescFunc.paramdesc.wParamFlags = (*env)->GetShortField(env, lpObject, FUNCDESC1Fc.elemdescFunc_paramdesc_wParamFlags);
	lpStruct->wFuncFlags = (*env)->GetShortField(env, lpObject, FUNCDESC1Fc.wFuncFlags);
	return lpStruct;
}

void setFUNCDESC1Fields(JNIEnv *env, jobject lpObject, FUNCDESC *lpStruct)
{
	if (!FUNCDESC1Fc.cached) cacheFUNCDESC1Fids(env, lpObject);
	(*env)->SetIntField(env, lpObject, FUNCDESC1Fc.memid, (jint)lpStruct->memid);
	(*env)->SetIntField(env, lpObject, FUNCDESC1Fc.lprgscode, (jint)lpStruct->lprgscode);
	(*env)->SetIntField(env, lpObject, FUNCDESC1Fc.lprgelemdescParam, (jint)lpStruct->lprgelemdescParam);
	(*env)->SetIntField(env, lpObject, FUNCDESC1Fc.funckind, (jint)lpStruct->funckind);
	(*env)->SetIntField(env, lpObject, FUNCDESC1Fc.invkind, (jint)lpStruct->invkind);
	(*env)->SetIntField(env, lpObject, FUNCDESC1Fc.callconv, (jint)lpStruct->callconv);
	(*env)->SetShortField(env, lpObject, FUNCDESC1Fc.cParams, (jshort)lpStruct->cParams);
	(*env)->SetShortField(env, lpObject, FUNCDESC1Fc.cParamsOpt, (jshort)lpStruct->cParamsOpt);
	(*env)->SetShortField(env, lpObject, FUNCDESC1Fc.oVft, (jshort)lpStruct->oVft);
	(*env)->SetShortField(env, lpObject, FUNCDESC1Fc.cScodes, (jshort)lpStruct->cScodes);
	(*env)->SetIntField(env, lpObject, FUNCDESC1Fc.elemdescFunc_tdesc_union, (jint)lpStruct->elemdescFunc.tdesc.lptdesc);
	(*env)->SetShortField(env, lpObject, FUNCDESC1Fc.elemdescFunc_tdesc_vt, (jshort)lpStruct->elemdescFunc.tdesc.vt);
	(*env)->SetIntField(env, lpObject, FUNCDESC1Fc.elemdescFunc_paramdesc_pparamdescex, (jint)lpStruct->elemdescFunc.paramdesc.pparamdescex);
	(*env)->SetShortField(env, lpObject, FUNCDESC1Fc.elemdescFunc_paramdesc_wParamFlags, (jshort)lpStruct->elemdescFunc.paramdesc.wParamFlags);
	(*env)->SetShortField(env, lpObject, FUNCDESC1Fc.wFuncFlags, (jshort)lpStruct->wFuncFlags);
}
#endif /* NO_FUNCDESC1 */

#ifndef NO_FUNCDESC2
typedef struct FUNCDESC2_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID memid, lprgscode, lprgelemdescParam, funckind, invkind, callconv, cParams, cParamsOpt, oVft, cScodes, elemdescFunc_tdesc_union, elemdescFunc_tdesc_vt, elemdescFunc_idldesc_dwReserved, elemdescFunc_idldesc_wIDLFlags, wFuncFlags;
} FUNCDESC2_FID_CACHE;

FUNCDESC2_FID_CACHE FUNCDESC2Fc;

void cacheFUNCDESC2Fids(JNIEnv *env, jobject lpObject)
{
	if (FUNCDESC2Fc.cached) return;
	FUNCDESC2Fc.clazz = (*env)->GetObjectClass(env, lpObject);
	FUNCDESC2Fc.memid = (*env)->GetFieldID(env, FUNCDESC2Fc.clazz, "memid", "I");
	FUNCDESC2Fc.lprgscode = (*env)->GetFieldID(env, FUNCDESC2Fc.clazz, "lprgscode", "I");
	FUNCDESC2Fc.lprgelemdescParam = (*env)->GetFieldID(env, FUNCDESC2Fc.clazz, "lprgelemdescParam", "I");
	FUNCDESC2Fc.funckind = (*env)->GetFieldID(env, FUNCDESC2Fc.clazz, "funckind", "I");
	FUNCDESC2Fc.invkind = (*env)->GetFieldID(env, FUNCDESC2Fc.clazz, "invkind", "I");
	FUNCDESC2Fc.callconv = (*env)->GetFieldID(env, FUNCDESC2Fc.clazz, "callconv", "I");
	FUNCDESC2Fc.cParams = (*env)->GetFieldID(env, FUNCDESC2Fc.clazz, "cParams", "S");
	FUNCDESC2Fc.cParamsOpt = (*env)->GetFieldID(env, FUNCDESC2Fc.clazz, "cParamsOpt", "S");
	FUNCDESC2Fc.oVft = (*env)->GetFieldID(env, FUNCDESC2Fc.clazz, "oVft", "S");
	FUNCDESC2Fc.cScodes = (*env)->GetFieldID(env, FUNCDESC2Fc.clazz, "cScodes", "S");
	FUNCDESC2Fc.elemdescFunc_tdesc_union = (*env)->GetFieldID(env, FUNCDESC2Fc.clazz, "elemdescFunc_tdesc_union", "I");
	FUNCDESC2Fc.elemdescFunc_tdesc_vt = (*env)->GetFieldID(env, FUNCDESC2Fc.clazz, "elemdescFunc_tdesc_vt", "S");
	FUNCDESC2Fc.elemdescFunc_idldesc_dwReserved = (*env)->GetFieldID(env, FUNCDESC2Fc.clazz, "elemdescFunc_idldesc_dwReserved", "I");
	FUNCDESC2Fc.elemdescFunc_idldesc_wIDLFlags = (*env)->GetFieldID(env, FUNCDESC2Fc.clazz, "elemdescFunc_idldesc_wIDLFlags", "S");
	FUNCDESC2Fc.wFuncFlags = (*env)->GetFieldID(env, FUNCDESC2Fc.clazz, "wFuncFlags", "S");
	FUNCDESC2Fc.cached = 1;
}

FUNCDESC *getFUNCDESC2Fields(JNIEnv *env, jobject lpObject, FUNCDESC *lpStruct)
{
	if (!FUNCDESC2Fc.cached) cacheFUNCDESC2Fids(env, lpObject);
	lpStruct->memid = (*env)->GetIntField(env, lpObject, FUNCDESC2Fc.memid);
	lpStruct->lprgscode = (SCODE FAR *)(*env)->GetIntField(env, lpObject, FUNCDESC2Fc.lprgscode);
	lpStruct->lprgelemdescParam = (ELEMDESC FAR *)(*env)->GetIntField(env, lpObject, FUNCDESC2Fc.lprgelemdescParam);
	lpStruct->funckind = (*env)->GetIntField(env, lpObject, FUNCDESC2Fc.funckind);
	lpStruct->invkind = (*env)->GetIntField(env, lpObject, FUNCDESC2Fc.invkind);
	lpStruct->callconv = (*env)->GetIntField(env, lpObject, FUNCDESC2Fc.callconv);
	lpStruct->cParams = (*env)->GetShortField(env, lpObject, FUNCDESC2Fc.cParams);
	lpStruct->cParamsOpt = (*env)->GetShortField(env, lpObject, FUNCDESC2Fc.cParamsOpt);
	lpStruct->oVft = (*env)->GetShortField(env, lpObject, FUNCDESC2Fc.oVft);
	lpStruct->cScodes = (*env)->GetShortField(env, lpObject, FUNCDESC2Fc.cScodes);
	lpStruct->elemdescFunc.tdesc.lptdesc = (struct FARSTRUCT tagTYPEDESC FAR* )(*env)->GetIntField(env, lpObject, FUNCDESC2Fc.elemdescFunc_tdesc_union);
	lpStruct->elemdescFunc.tdesc.vt = (*env)->GetShortField(env, lpObject, FUNCDESC2Fc.elemdescFunc_tdesc_vt);
	lpStruct->elemdescFunc.idldesc.dwReserved = (*env)->GetIntField(env, lpObject, FUNCDESC2Fc.elemdescFunc_idldesc_dwReserved);
	lpStruct->elemdescFunc.idldesc.wIDLFlags = (*env)->GetShortField(env, lpObject, FUNCDESC2Fc.elemdescFunc_idldesc_wIDLFlags);
	lpStruct->wFuncFlags = (*env)->GetShortField(env, lpObject, FUNCDESC2Fc.wFuncFlags);
	return lpStruct;
}

void setFUNCDESC2Fields(JNIEnv *env, jobject lpObject, FUNCDESC *lpStruct)
{
	if (!FUNCDESC2Fc.cached) cacheFUNCDESC2Fids(env, lpObject);
	(*env)->SetIntField(env, lpObject, FUNCDESC2Fc.memid, (jint)lpStruct->memid);
	(*env)->SetIntField(env, lpObject, FUNCDESC2Fc.lprgscode, (jint)lpStruct->lprgscode);
	(*env)->SetIntField(env, lpObject, FUNCDESC2Fc.lprgelemdescParam, (jint)lpStruct->lprgelemdescParam);
	(*env)->SetIntField(env, lpObject, FUNCDESC2Fc.funckind, (jint)lpStruct->funckind);
	(*env)->SetIntField(env, lpObject, FUNCDESC2Fc.invkind, (jint)lpStruct->invkind);
	(*env)->SetIntField(env, lpObject, FUNCDESC2Fc.callconv, (jint)lpStruct->callconv);
	(*env)->SetShortField(env, lpObject, FUNCDESC2Fc.cParams, (jshort)lpStruct->cParams);
	(*env)->SetShortField(env, lpObject, FUNCDESC2Fc.cParamsOpt, (jshort)lpStruct->cParamsOpt);
	(*env)->SetShortField(env, lpObject, FUNCDESC2Fc.oVft, (jshort)lpStruct->oVft);
	(*env)->SetShortField(env, lpObject, FUNCDESC2Fc.cScodes, (jshort)lpStruct->cScodes);
	(*env)->SetIntField(env, lpObject, FUNCDESC2Fc.elemdescFunc_tdesc_union, (jint)lpStruct->elemdescFunc.tdesc.lptdesc);
	(*env)->SetShortField(env, lpObject, FUNCDESC2Fc.elemdescFunc_tdesc_vt, (jshort)lpStruct->elemdescFunc.tdesc.vt);
	(*env)->SetIntField(env, lpObject, FUNCDESC2Fc.elemdescFunc_idldesc_dwReserved, (jint)lpStruct->elemdescFunc.idldesc.dwReserved);
	(*env)->SetShortField(env, lpObject, FUNCDESC2Fc.elemdescFunc_idldesc_wIDLFlags, (jshort)lpStruct->elemdescFunc.idldesc.wIDLFlags);
	(*env)->SetShortField(env, lpObject, FUNCDESC2Fc.wFuncFlags, (jshort)lpStruct->wFuncFlags);
}
#endif /* NO_FUNCDESC2 */

#ifndef NO_GUID
typedef struct GUID_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID data1, data2, data3, b0, b1, b2, b3, b4, b5, b6, b7;
} GUID_FID_CACHE;

GUID_FID_CACHE GUIDFc;

void cacheGUIDFids(JNIEnv *env, jobject lpObject)
{
	if (GUIDFc.cached) return;
	GUIDFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GUIDFc.data1 = (*env)->GetFieldID(env, GUIDFc.clazz, "data1", "I");
	GUIDFc.data2 = (*env)->GetFieldID(env, GUIDFc.clazz, "data2", "S");
	GUIDFc.data3 = (*env)->GetFieldID(env, GUIDFc.clazz, "data3", "S");
	GUIDFc.b0 = (*env)->GetFieldID(env, GUIDFc.clazz, "b0", "B");
	GUIDFc.b1 = (*env)->GetFieldID(env, GUIDFc.clazz, "b1", "B");
	GUIDFc.b2 = (*env)->GetFieldID(env, GUIDFc.clazz, "b2", "B");
	GUIDFc.b3 = (*env)->GetFieldID(env, GUIDFc.clazz, "b3", "B");
	GUIDFc.b4 = (*env)->GetFieldID(env, GUIDFc.clazz, "b4", "B");
	GUIDFc.b5 = (*env)->GetFieldID(env, GUIDFc.clazz, "b5", "B");
	GUIDFc.b6 = (*env)->GetFieldID(env, GUIDFc.clazz, "b6", "B");
	GUIDFc.b7 = (*env)->GetFieldID(env, GUIDFc.clazz, "b7", "B");
	GUIDFc.cached = 1;
}

GUID *getGUIDFields(JNIEnv *env, jobject lpObject, GUID *lpStruct)
{
	if (!GUIDFc.cached) cacheGUIDFids(env, lpObject);
	lpStruct->Data4[7] = (*env)->GetByteField(env, lpObject, GUIDFc.b7);
	lpStruct->Data4[6] = (*env)->GetByteField(env, lpObject, GUIDFc.b6);
	lpStruct->Data4[5] = (*env)->GetByteField(env, lpObject, GUIDFc.b5);
	lpStruct->Data4[4] = (*env)->GetByteField(env, lpObject, GUIDFc.b4);
	lpStruct->Data4[3] = (*env)->GetByteField(env, lpObject, GUIDFc.b3);
	lpStruct->Data4[2] = (*env)->GetByteField(env, lpObject, GUIDFc.b2);
	lpStruct->Data4[1] = (*env)->GetByteField(env, lpObject, GUIDFc.b1);
	lpStruct->Data4[0] = (*env)->GetByteField(env, lpObject, GUIDFc.b0);
	lpStruct->Data3 = (*env)->GetShortField(env, lpObject, GUIDFc.data3);
	lpStruct->Data2 = (*env)->GetShortField(env, lpObject, GUIDFc.data2);
	lpStruct->Data1 = (*env)->GetIntField(env, lpObject, GUIDFc.data1);
	return lpStruct;
}

void setGUIDFields(JNIEnv *env, jobject lpObject, GUID *lpStruct)
{
	if (!GUIDFc.cached) cacheGUIDFids(env, lpObject);
	(*env)->SetByteField(env, lpObject, GUIDFc.b7, lpStruct->Data4[7]);
	(*env)->SetByteField(env, lpObject, GUIDFc.b6, lpStruct->Data4[6]);
	(*env)->SetByteField(env, lpObject, GUIDFc.b5, lpStruct->Data4[5]);
	(*env)->SetByteField(env, lpObject, GUIDFc.b4, lpStruct->Data4[4]);
	(*env)->SetByteField(env, lpObject, GUIDFc.b3, lpStruct->Data4[3]);
	(*env)->SetByteField(env, lpObject, GUIDFc.b2, lpStruct->Data4[2]);
	(*env)->SetByteField(env, lpObject, GUIDFc.b1, lpStruct->Data4[1]);
	(*env)->SetByteField(env, lpObject, GUIDFc.b0, lpStruct->Data4[0]);
	(*env)->SetShortField(env, lpObject, GUIDFc.data3, lpStruct->Data3);
	(*env)->SetShortField(env, lpObject, GUIDFc.data2, lpStruct->Data2);
	(*env)->SetIntField(env, lpObject, GUIDFc.data1, lpStruct->Data1);
}

#endif /* NO_GUID */

#ifndef NO_LICINFO
typedef struct LICINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbLicInfo, fRuntimeKeyAvail, fLicVerified;
} LICINFO_FID_CACHE;

LICINFO_FID_CACHE LICINFOFc;

void cacheLICINFOFids(JNIEnv *env, jobject lpObject)
{
	if (LICINFOFc.cached) return;
	LICINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	LICINFOFc.cbLicInfo = (*env)->GetFieldID(env, LICINFOFc.clazz, "cbLicInfo", "I");
	LICINFOFc.fRuntimeKeyAvail = (*env)->GetFieldID(env, LICINFOFc.clazz, "fRuntimeKeyAvail", "I");
	LICINFOFc.fLicVerified = (*env)->GetFieldID(env, LICINFOFc.clazz, "fLicVerified", "I");
	LICINFOFc.cached = 1;
}

LICINFO *getLICINFOFields(JNIEnv *env, jobject lpObject, LICINFO *lpStruct)
{
	if (!LICINFOFc.cached) cacheLICINFOFids(env, lpObject);
	lpStruct->cbLicInfo = (*env)->GetIntField(env, lpObject, LICINFOFc.cbLicInfo);
	lpStruct->fRuntimeKeyAvail = (*env)->GetIntField(env, lpObject, LICINFOFc.fRuntimeKeyAvail);
	lpStruct->fLicVerified = (*env)->GetIntField(env, lpObject, LICINFOFc.fLicVerified);
	return lpStruct;
}

void setLICINFOFields(JNIEnv *env, jobject lpObject, LICINFO *lpStruct)
{
	if (!LICINFOFc.cached) cacheLICINFOFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, LICINFOFc.cbLicInfo, (jint)lpStruct->cbLicInfo);
	(*env)->SetIntField(env, lpObject, LICINFOFc.fRuntimeKeyAvail, (jint)lpStruct->fRuntimeKeyAvail);
	(*env)->SetIntField(env, lpObject, LICINFOFc.fLicVerified, (jint)lpStruct->fLicVerified);
}
#endif /* NO_LICINFO */

#ifndef NO_OLECMD
typedef struct OLECMD_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cmdID, cmdf;
} OLECMD_FID_CACHE;

OLECMD_FID_CACHE OLECMDFc;

void cacheOLECMDFids(JNIEnv *env, jobject lpObject)
{
	if (OLECMDFc.cached) return;
	OLECMDFc.clazz = (*env)->GetObjectClass(env, lpObject);
	OLECMDFc.cmdID = (*env)->GetFieldID(env, OLECMDFc.clazz, "cmdID", "I");
	OLECMDFc.cmdf = (*env)->GetFieldID(env, OLECMDFc.clazz, "cmdf", "I");
	OLECMDFc.cached = 1;
}

OLECMD *getOLECMDFields(JNIEnv *env, jobject lpObject, OLECMD *lpStruct)
{
	if (!OLECMDFc.cached) cacheOLECMDFids(env, lpObject);
	lpStruct->cmdID = (*env)->GetIntField(env, lpObject, OLECMDFc.cmdID);
	lpStruct->cmdf = (*env)->GetIntField(env, lpObject, OLECMDFc.cmdf);
	return lpStruct;
}

void setOLECMDFields(JNIEnv *env, jobject lpObject, OLECMD *lpStruct)
{
	if (!OLECMDFc.cached) cacheOLECMDFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, OLECMDFc.cmdID, (jint)lpStruct->cmdID);
	(*env)->SetIntField(env, lpObject, OLECMDFc.cmdf, (jint)lpStruct->cmdf);
}
#endif /* NO_OLECMD */

#ifndef NO_OLECMDTEXT
typedef struct OLECMDTEXT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cmdtextf, cwActual, cwBuf, rgwz;
} OLECMDTEXT_FID_CACHE;

OLECMDTEXT_FID_CACHE OLECMDTEXTFc;

void cacheOLECMDTEXTFids(JNIEnv *env, jobject lpObject)
{
	if (OLECMDTEXTFc.cached) return;
	OLECMDTEXTFc.clazz = (*env)->GetObjectClass(env, lpObject);
	OLECMDTEXTFc.cmdtextf = (*env)->GetFieldID(env, OLECMDTEXTFc.clazz, "cmdtextf", "I");
	OLECMDTEXTFc.cwActual = (*env)->GetFieldID(env, OLECMDTEXTFc.clazz, "cwActual", "I");
	OLECMDTEXTFc.cwBuf = (*env)->GetFieldID(env, OLECMDTEXTFc.clazz, "cwBuf", "I");
	OLECMDTEXTFc.rgwz = (*env)->GetFieldID(env, OLECMDTEXTFc.clazz, "rgwz", "S");
	OLECMDTEXTFc.cached = 1;
}

OLECMDTEXT *getOLECMDTEXTFields(JNIEnv *env, jobject lpObject, OLECMDTEXT *lpStruct)
{
	if (!OLECMDTEXTFc.cached) cacheOLECMDTEXTFids(env, lpObject);
	lpStruct->cmdtextf = (*env)->GetIntField(env, lpObject, OLECMDTEXTFc.cmdtextf);
	lpStruct->cwActual = (*env)->GetIntField(env, lpObject, OLECMDTEXTFc.cwActual);
	lpStruct->cwBuf = (*env)->GetIntField(env, lpObject, OLECMDTEXTFc.cwBuf);
	lpStruct->rgwz[0] = (*env)->GetShortField(env, lpObject, OLECMDTEXTFc.rgwz); /* SPECIAL */
	return lpStruct;
}

void setOLECMDTEXTFields(JNIEnv *env, jobject lpObject, OLECMDTEXT *lpStruct)
{
	if (!OLECMDTEXTFc.cached) cacheOLECMDTEXTFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, OLECMDTEXTFc.cmdtextf, (jint)lpStruct->cmdtextf);
	(*env)->SetIntField(env, lpObject, OLECMDTEXTFc.cwActual, (jint)lpStruct->cwActual);
	(*env)->SetIntField(env, lpObject, OLECMDTEXTFc.cwBuf, (jint)lpStruct->cwBuf);
	(*env)->SetShortField(env, lpObject, OLECMDTEXTFc.rgwz, (jshort)lpStruct->rgwz[0]); /* SPECIAL */
}
#endif /* NO_OLECMDTEXT */

#ifndef NO_OLEINPLACEFRAMEINFO
typedef struct OLEINPLACEFRAMEINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cb, fMDIApp, hwndFrame, haccel, cAccelEntries;
} OLEINPLACEFRAMEINFO_FID_CACHE;

OLEINPLACEFRAMEINFO_FID_CACHE OLEINPLACEFRAMEINFOFc;

void cacheOLEINPLACEFRAMEINFOFids(JNIEnv *env, jobject lpObject)
{
	if (OLEINPLACEFRAMEINFOFc.cached) return;
	OLEINPLACEFRAMEINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	OLEINPLACEFRAMEINFOFc.cb = (*env)->GetFieldID(env, OLEINPLACEFRAMEINFOFc.clazz, "cb", "I");
	OLEINPLACEFRAMEINFOFc.fMDIApp = (*env)->GetFieldID(env, OLEINPLACEFRAMEINFOFc.clazz, "fMDIApp", "I");
	OLEINPLACEFRAMEINFOFc.hwndFrame = (*env)->GetFieldID(env, OLEINPLACEFRAMEINFOFc.clazz, "hwndFrame", "I");
	OLEINPLACEFRAMEINFOFc.haccel = (*env)->GetFieldID(env, OLEINPLACEFRAMEINFOFc.clazz, "haccel", "I");
	OLEINPLACEFRAMEINFOFc.cAccelEntries = (*env)->GetFieldID(env, OLEINPLACEFRAMEINFOFc.clazz, "cAccelEntries", "I");
	OLEINPLACEFRAMEINFOFc.cached = 1;
}

OLEINPLACEFRAMEINFO *getOLEINPLACEFRAMEINFOFields(JNIEnv *env, jobject lpObject, OLEINPLACEFRAMEINFO *lpStruct)
{
	if (!OLEINPLACEFRAMEINFOFc.cached) cacheOLEINPLACEFRAMEINFOFids(env, lpObject);
	lpStruct->cb = (*env)->GetIntField(env, lpObject, OLEINPLACEFRAMEINFOFc.cb);
	lpStruct->fMDIApp = (*env)->GetIntField(env, lpObject, OLEINPLACEFRAMEINFOFc.fMDIApp);
	lpStruct->hwndFrame = (HWND)(*env)->GetIntField(env, lpObject, OLEINPLACEFRAMEINFOFc.hwndFrame);
	lpStruct->haccel = (HACCEL)(*env)->GetIntField(env, lpObject, OLEINPLACEFRAMEINFOFc.haccel);
	lpStruct->cAccelEntries = (*env)->GetIntField(env, lpObject, OLEINPLACEFRAMEINFOFc.cAccelEntries);
	return lpStruct;
}

void setOLEINPLACEFRAMEINFOFields(JNIEnv *env, jobject lpObject, OLEINPLACEFRAMEINFO *lpStruct)
{
	if (!OLEINPLACEFRAMEINFOFc.cached) cacheOLEINPLACEFRAMEINFOFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, OLEINPLACEFRAMEINFOFc.cb, (jint)lpStruct->cb);
	(*env)->SetIntField(env, lpObject, OLEINPLACEFRAMEINFOFc.fMDIApp, (jint)lpStruct->fMDIApp);
	(*env)->SetIntField(env, lpObject, OLEINPLACEFRAMEINFOFc.hwndFrame, (jint)lpStruct->hwndFrame);
	(*env)->SetIntField(env, lpObject, OLEINPLACEFRAMEINFOFc.haccel, (jint)lpStruct->haccel);
	(*env)->SetIntField(env, lpObject, OLEINPLACEFRAMEINFOFc.cAccelEntries, (jint)lpStruct->cAccelEntries);
}
#endif /* NO_OLEINPLACEFRAMEINFO */

#ifndef NO_STATSTG
typedef struct STATSTG_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID pwcsName, type, cbSize, mtime_dwLowDateTime, mtime_dwHighDateTime, ctime_dwLowDateTime, ctime_dwHighDateTime, atime_dwLowDateTime, atime_dwHighDateTime, grfMode, grfLocksSupported, clsid_data1, clsid_data2, clsid_data3, clsid_b0, clsid_b1, clsid_b2, clsid_b3, clsid_b4, clsid_b5, clsid_b6, clsid_b7, grfStateBits, reserved;
} STATSTG_FID_CACHE;

STATSTG_FID_CACHE STATSTGFc;

void cacheSTATSTGFids(JNIEnv *env, jobject lpObject)
{
	if (STATSTGFc.cached) return;
	STATSTGFc.clazz = (*env)->GetObjectClass(env, lpObject);
	STATSTGFc.pwcsName = (*env)->GetFieldID(env, STATSTGFc.clazz, "pwcsName", "I");
	STATSTGFc.type = (*env)->GetFieldID(env, STATSTGFc.clazz, "type", "I");
	STATSTGFc.cbSize = (*env)->GetFieldID(env, STATSTGFc.clazz, "cbSize", "J");
	STATSTGFc.mtime_dwLowDateTime = (*env)->GetFieldID(env, STATSTGFc.clazz, "mtime_dwLowDateTime", "I");
	STATSTGFc.mtime_dwHighDateTime = (*env)->GetFieldID(env, STATSTGFc.clazz, "mtime_dwHighDateTime", "I");
	STATSTGFc.ctime_dwLowDateTime = (*env)->GetFieldID(env, STATSTGFc.clazz, "ctime_dwLowDateTime", "I");
	STATSTGFc.ctime_dwHighDateTime = (*env)->GetFieldID(env, STATSTGFc.clazz, "ctime_dwHighDateTime", "I");
	STATSTGFc.atime_dwLowDateTime = (*env)->GetFieldID(env, STATSTGFc.clazz, "atime_dwLowDateTime", "I");
	STATSTGFc.atime_dwHighDateTime = (*env)->GetFieldID(env, STATSTGFc.clazz, "atime_dwHighDateTime", "I");
	STATSTGFc.grfMode = (*env)->GetFieldID(env, STATSTGFc.clazz, "grfMode", "I");
	STATSTGFc.grfLocksSupported = (*env)->GetFieldID(env, STATSTGFc.clazz, "grfLocksSupported", "I");
	STATSTGFc.clsid_data1 = (*env)->GetFieldID(env, STATSTGFc.clazz, "clsid_data1", "I");
	STATSTGFc.clsid_data2 = (*env)->GetFieldID(env, STATSTGFc.clazz, "clsid_data2", "S");
	STATSTGFc.clsid_data3 = (*env)->GetFieldID(env, STATSTGFc.clazz, "clsid_data3", "S");
	STATSTGFc.clsid_b0 = (*env)->GetFieldID(env, STATSTGFc.clazz, "clsid_b0", "B");
	STATSTGFc.clsid_b1 = (*env)->GetFieldID(env, STATSTGFc.clazz, "clsid_b1", "B");
	STATSTGFc.clsid_b2 = (*env)->GetFieldID(env, STATSTGFc.clazz, "clsid_b2", "B");
	STATSTGFc.clsid_b3 = (*env)->GetFieldID(env, STATSTGFc.clazz, "clsid_b3", "B");
	STATSTGFc.clsid_b4 = (*env)->GetFieldID(env, STATSTGFc.clazz, "clsid_b4", "B");
	STATSTGFc.clsid_b5 = (*env)->GetFieldID(env, STATSTGFc.clazz, "clsid_b5", "B");
	STATSTGFc.clsid_b6 = (*env)->GetFieldID(env, STATSTGFc.clazz, "clsid_b6", "B");
	STATSTGFc.clsid_b7 = (*env)->GetFieldID(env, STATSTGFc.clazz, "clsid_b7", "B");
	STATSTGFc.grfStateBits = (*env)->GetFieldID(env, STATSTGFc.clazz, "grfStateBits", "I");
	STATSTGFc.reserved = (*env)->GetFieldID(env, STATSTGFc.clazz, "reserved", "I");
	STATSTGFc.cached = 1;
}

STATSTG *getSTATSTGFields(JNIEnv *env, jobject lpObject, STATSTG *lpStruct)
{
	if (!STATSTGFc.cached) cacheSTATSTGFids(env, lpObject);
	lpStruct->pwcsName = (LPWSTR)(*env)->GetIntField(env, lpObject, STATSTGFc.pwcsName);
	lpStruct->type = (*env)->GetIntField(env, lpObject, STATSTGFc.type);
	lpStruct->cbSize.QuadPart = (*env)->GetLongField(env, lpObject, STATSTGFc.cbSize);
	lpStruct->mtime.dwLowDateTime = (*env)->GetIntField(env, lpObject, STATSTGFc.mtime_dwLowDateTime);
	lpStruct->mtime.dwHighDateTime = (*env)->GetIntField(env, lpObject, STATSTGFc.mtime_dwHighDateTime);
	lpStruct->ctime.dwLowDateTime = (*env)->GetIntField(env, lpObject, STATSTGFc.ctime_dwLowDateTime);
	lpStruct->ctime.dwHighDateTime = (*env)->GetIntField(env, lpObject, STATSTGFc.ctime_dwHighDateTime);
	lpStruct->atime.dwLowDateTime = (*env)->GetIntField(env, lpObject, STATSTGFc.atime_dwLowDateTime);
	lpStruct->atime.dwHighDateTime = (*env)->GetIntField(env, lpObject, STATSTGFc.atime_dwHighDateTime);
	lpStruct->grfMode = (*env)->GetIntField(env, lpObject, STATSTGFc.grfMode);
	lpStruct->grfLocksSupported = (*env)->GetIntField(env, lpObject, STATSTGFc.grfLocksSupported);
	lpStruct->clsid.Data4[7] = (*env)->GetByteField(env, lpObject, STATSTGFc.clsid_b7);
	lpStruct->clsid.Data4[6] = (*env)->GetByteField(env, lpObject, STATSTGFc.clsid_b6);
	lpStruct->clsid.Data4[5] = (*env)->GetByteField(env, lpObject, STATSTGFc.clsid_b5);
	lpStruct->clsid.Data4[4] = (*env)->GetByteField(env, lpObject, STATSTGFc.clsid_b4);
	lpStruct->clsid.Data4[3] = (*env)->GetByteField(env, lpObject, STATSTGFc.clsid_b3);
	lpStruct->clsid.Data4[2] = (*env)->GetByteField(env, lpObject, STATSTGFc.clsid_b2);
	lpStruct->clsid.Data4[1] = (*env)->GetByteField(env, lpObject, STATSTGFc.clsid_b1);
	lpStruct->clsid.Data4[0] = (*env)->GetByteField(env, lpObject, STATSTGFc.clsid_b0);
	lpStruct->clsid.Data3 = (*env)->GetShortField(env, lpObject, STATSTGFc.clsid_data3);
	lpStruct->clsid.Data2 = (*env)->GetShortField(env, lpObject, STATSTGFc.clsid_data2);
	lpStruct->clsid.Data1 = (*env)->GetIntField(env, lpObject, STATSTGFc.clsid_data1);
	lpStruct->grfStateBits = (*env)->GetIntField(env, lpObject, STATSTGFc.grfStateBits);
	lpStruct->reserved = (*env)->GetIntField(env, lpObject, STATSTGFc.reserved);
	return lpStruct;
}

void setSTATSTGFields(JNIEnv *env, jobject lpObject, STATSTG *lpStruct)
{
	if (!STATSTGFc.cached) cacheSTATSTGFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, STATSTGFc.pwcsName, (jint)lpStruct->pwcsName);
	(*env)->SetIntField(env, lpObject, STATSTGFc.type, (jint)lpStruct->type);
	(*env)->SetLongField(env, lpObject, STATSTGFc.cbSize, (jlong)lpStruct->cbSize.QuadPart);
	(*env)->SetIntField(env, lpObject, STATSTGFc.mtime_dwLowDateTime, (jint)lpStruct->mtime.dwLowDateTime);
	(*env)->SetIntField(env, lpObject, STATSTGFc.mtime_dwHighDateTime, (jint)lpStruct->mtime.dwHighDateTime);
	(*env)->SetIntField(env, lpObject, STATSTGFc.ctime_dwLowDateTime, (jint)lpStruct->ctime.dwLowDateTime);
	(*env)->SetIntField(env, lpObject, STATSTGFc.ctime_dwHighDateTime, (jint)lpStruct->ctime.dwHighDateTime);
	(*env)->SetIntField(env, lpObject, STATSTGFc.atime_dwLowDateTime, (jint)lpStruct->atime.dwLowDateTime);
	(*env)->SetIntField(env, lpObject, STATSTGFc.atime_dwHighDateTime, (jint)lpStruct->atime.dwHighDateTime);
	(*env)->SetIntField(env, lpObject, STATSTGFc.grfMode, (jint)lpStruct->grfMode);
	(*env)->SetIntField(env, lpObject, STATSTGFc.grfLocksSupported, (jint)lpStruct->grfLocksSupported);
	(*env)->SetByteField(env, lpObject, STATSTGFc.clsid_b7, lpStruct->clsid.Data4[7]);
	(*env)->SetByteField(env, lpObject, STATSTGFc.clsid_b6, lpStruct->clsid.Data4[6]);
	(*env)->SetByteField(env, lpObject, STATSTGFc.clsid_b5, lpStruct->clsid.Data4[5]);
	(*env)->SetByteField(env, lpObject, STATSTGFc.clsid_b4, lpStruct->clsid.Data4[4]);
	(*env)->SetByteField(env, lpObject, STATSTGFc.clsid_b3, lpStruct->clsid.Data4[3]);
	(*env)->SetByteField(env, lpObject, STATSTGFc.clsid_b2, lpStruct->clsid.Data4[2]);
	(*env)->SetByteField(env, lpObject, STATSTGFc.clsid_b1, lpStruct->clsid.Data4[1]);
	(*env)->SetByteField(env, lpObject, STATSTGFc.clsid_b0, lpStruct->clsid.Data4[0]);
	(*env)->SetShortField(env, lpObject, STATSTGFc.clsid_data3, lpStruct->clsid.Data3);
	(*env)->SetShortField(env, lpObject, STATSTGFc.clsid_data2, lpStruct->clsid.Data2);
	(*env)->SetIntField(env, lpObject, STATSTGFc.clsid_data1, lpStruct->clsid.Data1);
	(*env)->SetIntField(env, lpObject, STATSTGFc.grfStateBits, (jint)lpStruct->grfStateBits);
	(*env)->SetIntField(env, lpObject, STATSTGFc.reserved, (jint)lpStruct->reserved);
}
#endif /* NO_STATSTG */

#ifndef NO_STGMEDIUM
typedef struct STGMEDIUM_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID tymed, unionField, pUnkForRelease;
} STGMEDIUM_FID_CACHE;

STGMEDIUM_FID_CACHE STGMEDIUMFc;

void cacheSTGMEDIUMFids(JNIEnv *env, jobject lpObject)
{
	if (STGMEDIUMFc.cached) return;
	STGMEDIUMFc.clazz = (*env)->GetObjectClass(env, lpObject);
	STGMEDIUMFc.tymed = (*env)->GetFieldID(env, STGMEDIUMFc.clazz, "tymed", "I");
	STGMEDIUMFc.unionField = (*env)->GetFieldID(env, STGMEDIUMFc.clazz, "unionField", "I");
	STGMEDIUMFc.pUnkForRelease = (*env)->GetFieldID(env, STGMEDIUMFc.clazz, "pUnkForRelease", "I");
	STGMEDIUMFc.cached = 1;
}

STGMEDIUM *getSTGMEDIUMFields(JNIEnv *env, jobject lpObject, STGMEDIUM *lpStruct)
{
	if (!STGMEDIUMFc.cached) cacheSTGMEDIUMFids(env, lpObject);
	lpStruct->tymed = (*env)->GetIntField(env, lpObject, STGMEDIUMFc.tymed);
	lpStruct->hGlobal = (HGLOBAL)(*env)->GetIntField(env, lpObject, STGMEDIUMFc.unionField);
	lpStruct->pUnkForRelease = (IUnknown *)(*env)->GetIntField(env, lpObject, STGMEDIUMFc.pUnkForRelease);
	return lpStruct;
}

void setSTGMEDIUMFields(JNIEnv *env, jobject lpObject, STGMEDIUM *lpStruct)
{
	if (!STGMEDIUMFc.cached) cacheSTGMEDIUMFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, STGMEDIUMFc.tymed, (jint)lpStruct->tymed);
	(*env)->SetIntField(env, lpObject, STGMEDIUMFc.unionField, (jint)lpStruct->hGlobal);
	(*env)->SetIntField(env, lpObject, STGMEDIUMFc.pUnkForRelease, (jint)lpStruct->pUnkForRelease);
}
#endif /* NO_STGMEDIUM */

#ifndef NO_TYPEATTR
typedef struct TYPEATTR_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID guid_data1, guid_data2, guid_data3, guid_b0, guid_b1, guid_b2, guid_b3, guid_b4, guid_b5, guid_b6, guid_b7, lcid, dwReserved, memidConstructor, memidDestructor, lpstrSchema, cbSizeInstance, typekind, cFuncs, cVars, cImplTypes, cbSizeVft, cbAlignment, wTypeFlags, wMajorVerNum, wMinorVerNum, tdescAlias_unionField, tdescAlias_vt, idldescType_dwReserved, idldescType_wIDLFlags;
} TYPEATTR_FID_CACHE;

TYPEATTR_FID_CACHE TYPEATTRFc;

void cacheTYPEATTRFids(JNIEnv *env, jobject lpObject)
{
	if (TYPEATTRFc.cached) return;
	TYPEATTRFc.clazz = (*env)->GetObjectClass(env, lpObject);
	TYPEATTRFc.guid_data1 = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "guid_data1", "I");
	TYPEATTRFc.guid_data2 = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "guid_data2", "S");
	TYPEATTRFc.guid_data3 = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "guid_data3", "S");
	TYPEATTRFc.guid_b0 = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "guid_b0", "B");
	TYPEATTRFc.guid_b1 = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "guid_b1", "B");
	TYPEATTRFc.guid_b2 = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "guid_b2", "B");
	TYPEATTRFc.guid_b3 = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "guid_b3", "B");
	TYPEATTRFc.guid_b4 = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "guid_b4", "B");
	TYPEATTRFc.guid_b5 = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "guid_b5", "B");
	TYPEATTRFc.guid_b6 = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "guid_b6", "B");
	TYPEATTRFc.guid_b7 = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "guid_b7", "B");
	TYPEATTRFc.lcid = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "lcid", "I");
	TYPEATTRFc.dwReserved = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "dwReserved", "I");
	TYPEATTRFc.memidConstructor = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "memidConstructor", "I");
	TYPEATTRFc.memidDestructor = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "memidDestructor", "I");
	TYPEATTRFc.lpstrSchema = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "lpstrSchema", "I");
	TYPEATTRFc.cbSizeInstance = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "cbSizeInstance", "I");
	TYPEATTRFc.typekind = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "typekind", "I");
	TYPEATTRFc.cFuncs = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "cFuncs", "S");
	TYPEATTRFc.cVars = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "cVars", "S");
	TYPEATTRFc.cImplTypes = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "cImplTypes", "S");
	TYPEATTRFc.cbSizeVft = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "cbSizeVft", "S");
	TYPEATTRFc.cbAlignment = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "cbAlignment", "S");
	TYPEATTRFc.wTypeFlags = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "wTypeFlags", "S");
	TYPEATTRFc.wMajorVerNum = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "wMajorVerNum", "S");
	TYPEATTRFc.wMinorVerNum = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "wMinorVerNum", "S");
	TYPEATTRFc.tdescAlias_unionField = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "tdescAlias_unionField", "I");
	TYPEATTRFc.tdescAlias_vt = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "tdescAlias_vt", "S");
	TYPEATTRFc.idldescType_dwReserved = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "idldescType_dwReserved", "I");
	TYPEATTRFc.idldescType_wIDLFlags = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "idldescType_wIDLFlags", "S");
	TYPEATTRFc.cached = 1;
}

TYPEATTR *getTYPEATTRFields(JNIEnv *env, jobject lpObject, TYPEATTR *lpStruct)
{
	if (!TYPEATTRFc.cached) cacheTYPEATTRFids(env, lpObject);
	lpStruct->idldescType.wIDLFlags = (*env)->GetShortField(env, lpObject, TYPEATTRFc.idldescType_wIDLFlags);
	lpStruct->idldescType.dwReserved = (*env)->GetIntField(env, lpObject, TYPEATTRFc.idldescType_dwReserved);
	lpStruct->tdescAlias.vt = (*env)->GetShortField(env, lpObject, TYPEATTRFc.tdescAlias_vt);
	lpStruct->tdescAlias.lptdesc = (struct FARSTRUCT tagTYPEDESC FAR *)(*env)->GetIntField(env, lpObject, TYPEATTRFc.tdescAlias_unionField);
	lpStruct->wMinorVerNum = (*env)->GetShortField(env, lpObject, TYPEATTRFc.wMinorVerNum);
	lpStruct->wMajorVerNum = (*env)->GetShortField(env, lpObject, TYPEATTRFc.wMajorVerNum);
	lpStruct->wTypeFlags = (*env)->GetShortField(env, lpObject, TYPEATTRFc.wTypeFlags);
	lpStruct->cbAlignment = (*env)->GetShortField(env, lpObject, TYPEATTRFc.cbAlignment);
	lpStruct->cbSizeVft = (*env)->GetShortField(env, lpObject, TYPEATTRFc.cbSizeVft);
	lpStruct->cImplTypes = (*env)->GetShortField(env, lpObject, TYPEATTRFc.cImplTypes);
	lpStruct->cVars = (*env)->GetShortField(env, lpObject, TYPEATTRFc.cVars);
	lpStruct->cFuncs = (*env)->GetShortField(env, lpObject, TYPEATTRFc.cFuncs);
	lpStruct->typekind = (*env)->GetIntField(env, lpObject, TYPEATTRFc.typekind);
	lpStruct->cbSizeInstance = (*env)->GetIntField(env, lpObject, TYPEATTRFc.cbSizeInstance);
	lpStruct->lpstrSchema = (OLECHAR FAR *)(*env)->GetIntField(env, lpObject, TYPEATTRFc.lpstrSchema);
	lpStruct->memidDestructor = (*env)->GetIntField(env, lpObject, TYPEATTRFc.memidDestructor);
	lpStruct->memidConstructor = (*env)->GetIntField(env, lpObject, TYPEATTRFc.memidConstructor);
	lpStruct->dwReserved = (*env)->GetIntField(env, lpObject, TYPEATTRFc.dwReserved);
	lpStruct->lcid = (*env)->GetIntField(env, lpObject, TYPEATTRFc.lcid);
	lpStruct->guid.Data4[7] = (*env)->GetByteField(env, lpObject, TYPEATTRFc.guid_b7);
	lpStruct->guid.Data4[6] = (*env)->GetByteField(env, lpObject, TYPEATTRFc.guid_b6);
	lpStruct->guid.Data4[5] = (*env)->GetByteField(env, lpObject, TYPEATTRFc.guid_b5);
	lpStruct->guid.Data4[4] = (*env)->GetByteField(env, lpObject, TYPEATTRFc.guid_b4);
	lpStruct->guid.Data4[3] = (*env)->GetByteField(env, lpObject, TYPEATTRFc.guid_b3);
	lpStruct->guid.Data4[2] = (*env)->GetByteField(env, lpObject, TYPEATTRFc.guid_b2);
	lpStruct->guid.Data4[1] = (*env)->GetByteField(env, lpObject, TYPEATTRFc.guid_b1);
	lpStruct->guid.Data4[0] = (*env)->GetByteField(env, lpObject, TYPEATTRFc.guid_b0);
	lpStruct->guid.Data3 = (*env)->GetShortField(env, lpObject, TYPEATTRFc.guid_data3);
	lpStruct->guid.Data2 = (*env)->GetShortField(env, lpObject, TYPEATTRFc.guid_data2);
	lpStruct->guid.Data1 = (*env)->GetIntField(env, lpObject, TYPEATTRFc.guid_data1);
	return lpStruct;
}

void setTYPEATTRFields(JNIEnv *env, jobject lpObject, TYPEATTR *lpStruct)
{
	if (!TYPEATTRFc.cached) cacheTYPEATTRFids(env, lpObject);
	(*env)->SetShortField(env, lpObject, TYPEATTRFc.idldescType_wIDLFlags, lpStruct->idldescType.wIDLFlags);
	(*env)->SetIntField(env, lpObject, TYPEATTRFc.idldescType_dwReserved, lpStruct->idldescType.dwReserved);
	(*env)->SetShortField(env, lpObject, TYPEATTRFc.tdescAlias_vt, lpStruct->tdescAlias.vt);
	(*env)->SetIntField(env, lpObject, TYPEATTRFc.tdescAlias_unionField, (jint)lpStruct->tdescAlias.lptdesc);
	(*env)->SetShortField(env, lpObject, TYPEATTRFc.wMinorVerNum, lpStruct->wMinorVerNum);
	(*env)->SetShortField(env, lpObject, TYPEATTRFc.wMajorVerNum, lpStruct->wMajorVerNum);
	(*env)->SetShortField(env, lpObject, TYPEATTRFc.wTypeFlags, lpStruct->wTypeFlags);
	(*env)->SetShortField(env, lpObject, TYPEATTRFc.cbAlignment, lpStruct->cbAlignment);
	(*env)->SetShortField(env, lpObject, TYPEATTRFc.cbSizeVft, lpStruct->cbSizeVft);
	(*env)->SetShortField(env, lpObject, TYPEATTRFc.cImplTypes, lpStruct->cImplTypes);
	(*env)->SetShortField(env, lpObject, TYPEATTRFc.cVars, lpStruct->cVars);
	(*env)->SetShortField(env, lpObject, TYPEATTRFc.cFuncs, lpStruct->cFuncs);
	(*env)->SetIntField(env, lpObject, TYPEATTRFc.typekind, lpStruct->typekind);
	(*env)->SetIntField(env, lpObject, TYPEATTRFc.cbSizeInstance, lpStruct->cbSizeInstance);
	(*env)->SetIntField(env, lpObject, TYPEATTRFc.lpstrSchema, (jint)lpStruct->lpstrSchema);
	(*env)->SetIntField(env, lpObject, TYPEATTRFc.memidDestructor, lpStruct->memidDestructor);
	(*env)->SetIntField(env, lpObject, TYPEATTRFc.memidConstructor, lpStruct->memidConstructor);
	(*env)->SetIntField(env, lpObject, TYPEATTRFc.dwReserved, lpStruct->dwReserved);
	(*env)->SetIntField(env, lpObject, TYPEATTRFc.lcid, lpStruct->lcid);
	(*env)->SetByteField(env, lpObject, TYPEATTRFc.guid_b7, lpStruct->guid.Data4[7]);
	(*env)->SetByteField(env, lpObject, TYPEATTRFc.guid_b6, lpStruct->guid.Data4[6]);
	(*env)->SetByteField(env, lpObject, TYPEATTRFc.guid_b5, lpStruct->guid.Data4[5]);
	(*env)->SetByteField(env, lpObject, TYPEATTRFc.guid_b4, lpStruct->guid.Data4[4]);
	(*env)->SetByteField(env, lpObject, TYPEATTRFc.guid_b3, lpStruct->guid.Data4[3]);
	(*env)->SetByteField(env, lpObject, TYPEATTRFc.guid_b2, lpStruct->guid.Data4[2]);
	(*env)->SetByteField(env, lpObject, TYPEATTRFc.guid_b1, lpStruct->guid.Data4[1]);
	(*env)->SetByteField(env, lpObject, TYPEATTRFc.guid_b0, lpStruct->guid.Data4[0]);
	(*env)->SetShortField(env, lpObject, TYPEATTRFc.guid_data3, lpStruct->guid.Data3);
	(*env)->SetShortField(env, lpObject, TYPEATTRFc.guid_data2, lpStruct->guid.Data2);
	(*env)->SetIntField(env, lpObject, TYPEATTRFc.guid_data1, lpStruct->guid.Data1);
}
#endif /* NO_TYPEATTR */

#ifndef NO_VARDESC1
typedef struct VARDESC1_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID memid, lpstrSchema, unionField, elemdescVar_tdesc_union, elemdescVar_tdesc_vt, elemdescVar_paramdesc_pparamdescex, elemdescVar_paramdesc_wParamFlags, wVarFlags, varkind;
} VARDESC1_FID_CACHE;

VARDESC1_FID_CACHE VARDESC1Fc;

void cacheVARDESC1Fids(JNIEnv *env, jobject lpObject)
{
	if (VARDESC1Fc.cached) return;
	VARDESC1Fc.clazz = (*env)->GetObjectClass(env, lpObject);
	VARDESC1Fc.memid = (*env)->GetFieldID(env, VARDESC1Fc.clazz, "memid", "I");
	VARDESC1Fc.lpstrSchema = (*env)->GetFieldID(env, VARDESC1Fc.clazz, "lpstrSchema", "I");
	VARDESC1Fc.unionField = (*env)->GetFieldID(env, VARDESC1Fc.clazz, "unionField", "I");
	VARDESC1Fc.elemdescVar_tdesc_union = (*env)->GetFieldID(env, VARDESC1Fc.clazz, "elemdescVar_tdesc_union", "I");
	VARDESC1Fc.elemdescVar_tdesc_vt = (*env)->GetFieldID(env, VARDESC1Fc.clazz, "elemdescVar_tdesc_vt", "S");
	VARDESC1Fc.elemdescVar_paramdesc_pparamdescex = (*env)->GetFieldID(env, VARDESC1Fc.clazz, "elemdescVar_paramdesc_pparamdescex", "I");
	VARDESC1Fc.elemdescVar_paramdesc_wParamFlags = (*env)->GetFieldID(env, VARDESC1Fc.clazz, "elemdescVar_paramdesc_wParamFlags", "S");
	VARDESC1Fc.wVarFlags = (*env)->GetFieldID(env, VARDESC1Fc.clazz, "wVarFlags", "S");
	VARDESC1Fc.varkind = (*env)->GetFieldID(env, VARDESC1Fc.clazz, "varkind", "I");
	VARDESC1Fc.cached = 1;
}

VARDESC *getVARDESC1Fields(JNIEnv *env, jobject lpObject, VARDESC *lpStruct)
{
	if (!VARDESC1Fc.cached) cacheVARDESC1Fids(env, lpObject);
	lpStruct->varkind = (*env)->GetIntField(env, lpObject, VARDESC1Fc.varkind);
	lpStruct->wVarFlags = (*env)->GetShortField(env, lpObject, VARDESC1Fc.wVarFlags);
	lpStruct->elemdescVar.paramdesc.wParamFlags = (*env)->GetShortField(env, lpObject, VARDESC1Fc.elemdescVar_paramdesc_wParamFlags);
	lpStruct->elemdescVar.paramdesc.pparamdescex = (LPPARAMDESCEX)(*env)->GetIntField(env, lpObject, VARDESC1Fc.elemdescVar_paramdesc_pparamdescex);
	lpStruct->elemdescVar.tdesc.vt = (*env)->GetShortField(env, lpObject, VARDESC1Fc.elemdescVar_tdesc_vt);
	lpStruct->elemdescVar.tdesc.lptdesc = (struct FARSTRUCT tagTYPEDESC FAR *)(*env)->GetIntField(env, lpObject, VARDESC1Fc.elemdescVar_tdesc_union);
	lpStruct->oInst = (*env)->GetIntField(env, lpObject, VARDESC1Fc.unionField);
	lpStruct->lpstrSchema = (OLECHAR FAR *)(*env)->GetIntField(env, lpObject, VARDESC1Fc.lpstrSchema);
	lpStruct->memid = (*env)->GetIntField(env, lpObject, VARDESC1Fc.memid);
	return lpStruct;
}

void setVARDESC1Fields(JNIEnv *env, jobject lpObject, VARDESC *lpStruct)
{
	if (!VARDESC1Fc.cached) cacheVARDESC1Fids(env, lpObject);
	(*env)->SetIntField(env, lpObject, VARDESC1Fc.varkind, lpStruct->varkind);
	(*env)->SetShortField(env, lpObject, VARDESC1Fc.wVarFlags, lpStruct->wVarFlags);
	(*env)->SetShortField(env, lpObject, VARDESC1Fc.elemdescVar_paramdesc_wParamFlags, lpStruct->elemdescVar.paramdesc.wParamFlags);
	(*env)->SetIntField(env, lpObject, VARDESC1Fc.elemdescVar_paramdesc_pparamdescex, (jint)lpStruct->elemdescVar.paramdesc.pparamdescex);
	(*env)->SetShortField(env, lpObject, VARDESC1Fc.elemdescVar_tdesc_vt, lpStruct->elemdescVar.tdesc.vt);
	(*env)->SetIntField(env, lpObject, VARDESC1Fc.elemdescVar_tdesc_union, (jint)lpStruct->elemdescVar.tdesc.lptdesc);
	(*env)->SetIntField(env, lpObject, VARDESC1Fc.unionField, lpStruct->oInst);
	(*env)->SetIntField(env, lpObject, VARDESC1Fc.lpstrSchema, (jint)lpStruct->lpstrSchema);
	(*env)->SetIntField(env, lpObject, VARDESC1Fc.memid, lpStruct->memid);
}
#endif /* NO_VARDESC1 */

#ifndef NO_VARDESC2
typedef struct VARDESC2_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID memid, lpstrSchema, unionField, elemdescVar_tdesc_union, elemdescVar_tdesc_vt, elemdescFunc_idldesc_dwReserved, elemdescFunc_idldesc_wIDLFlags, wVarFlags, varkind;
} VARDESC2_FID_CACHE;

VARDESC2_FID_CACHE VARDESC2Fc;

void cacheVARDESC2Fids(JNIEnv *env, jobject lpObject)
{
	if (VARDESC2Fc.cached) return;
	VARDESC2Fc.clazz = (*env)->GetObjectClass(env, lpObject);
	VARDESC2Fc.memid = (*env)->GetFieldID(env, VARDESC2Fc.clazz, "memid", "I");
	VARDESC2Fc.lpstrSchema = (*env)->GetFieldID(env, VARDESC2Fc.clazz, "lpstrSchema", "I");
	VARDESC2Fc.unionField = (*env)->GetFieldID(env, VARDESC2Fc.clazz, "unionField", "I");
	VARDESC2Fc.elemdescVar_tdesc_union = (*env)->GetFieldID(env, VARDESC2Fc.clazz, "elemdescVar_tdesc_union", "I");
	VARDESC2Fc.elemdescVar_tdesc_vt = (*env)->GetFieldID(env, VARDESC2Fc.clazz, "elemdescVar_tdesc_vt", "S");
	VARDESC2Fc.elemdescFunc_idldesc_dwReserved = (*env)->GetFieldID(env, VARDESC2Fc.clazz, "elemdescFunc_idldesc_dwReserved", "I");
	VARDESC2Fc.elemdescFunc_idldesc_wIDLFlags = (*env)->GetFieldID(env, VARDESC2Fc.clazz, "elemdescFunc_idldesc_wIDLFlags", "S");
	VARDESC2Fc.wVarFlags = (*env)->GetFieldID(env, VARDESC2Fc.clazz, "wVarFlags", "S");
	VARDESC2Fc.varkind = (*env)->GetFieldID(env, VARDESC2Fc.clazz, "varkind", "I");
	VARDESC2Fc.cached = 1;
}

VARDESC *getVARDESC2Fields(JNIEnv *env, jobject lpObject, VARDESC *lpStruct)
{
	if (!VARDESC2Fc.cached) cacheVARDESC2Fids(env, lpObject);
	lpStruct->varkind = (*env)->GetIntField(env, lpObject, VARDESC2Fc.varkind);
	lpStruct->wVarFlags = (*env)->GetShortField(env, lpObject, VARDESC2Fc.wVarFlags);
	lpStruct->elemdescVar.idldesc.wIDLFlags = (*env)->GetShortField(env, lpObject, VARDESC2Fc.elemdescFunc_idldesc_wIDLFlags);
	lpStruct->elemdescVar.idldesc.dwReserved = (*env)->GetIntField(env, lpObject, VARDESC2Fc.elemdescFunc_idldesc_dwReserved);
	lpStruct->elemdescVar.tdesc.vt = (*env)->GetShortField(env, lpObject, VARDESC2Fc.elemdescVar_tdesc_vt);
	lpStruct->elemdescVar.tdesc.lptdesc = (struct FARSTRUCT tagTYPEDESC FAR *)(*env)->GetIntField(env, lpObject, VARDESC2Fc.elemdescVar_tdesc_union);
	lpStruct->oInst = (*env)->GetIntField(env, lpObject, VARDESC2Fc.unionField);
	lpStruct->lpstrSchema = (OLECHAR FAR *)(*env)->GetIntField(env, lpObject, VARDESC2Fc.lpstrSchema);
	lpStruct->memid = (*env)->GetIntField(env, lpObject, VARDESC2Fc.memid);
	return lpStruct;
}

void setVARDESC2Fields(JNIEnv *env, jobject lpObject, VARDESC *lpStruct)
{
	if (!VARDESC2Fc.cached) cacheVARDESC2Fids(env, lpObject);
	(*env)->SetIntField(env, lpObject, VARDESC2Fc.varkind, lpStruct->varkind);
	(*env)->SetShortField(env, lpObject, VARDESC2Fc.wVarFlags, lpStruct->wVarFlags);
	(*env)->SetShortField(env, lpObject, VARDESC2Fc.elemdescFunc_idldesc_wIDLFlags, lpStruct->elemdescVar.idldesc.wIDLFlags);
	(*env)->SetIntField(env, lpObject, VARDESC2Fc.elemdescFunc_idldesc_dwReserved, lpStruct->elemdescVar.idldesc.dwReserved);
	(*env)->SetShortField(env, lpObject, VARDESC2Fc.elemdescVar_tdesc_vt, lpStruct->elemdescVar.tdesc.vt);
	(*env)->SetIntField(env, lpObject, VARDESC2Fc.elemdescVar_tdesc_union, (jint)lpStruct->elemdescVar.tdesc.lptdesc);
	(*env)->SetIntField(env, lpObject, VARDESC2Fc.unionField, lpStruct->oInst);
	(*env)->SetIntField(env, lpObject, VARDESC2Fc.lpstrSchema, (jint)lpStruct->lpstrSchema);
	(*env)->SetIntField(env, lpObject, VARDESC2Fc.memid, lpStruct->memid);
}
#endif /* NO_VARDESC2 */

/**************************** END OLE ****************************/