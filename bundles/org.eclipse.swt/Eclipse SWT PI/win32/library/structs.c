/*
 * (c) Copyright IBM Corp., 2000, 2001
 * All Rights Reserved.
 */

/**
 * JNI SWT object field getters and setters declarations for Windows structs
 */

#include "swt.h"
#include "structs.h"

/* Win32 globals */
ACCEL_FID_CACHE ACCELFc;
BITMAP_FID_CACHE BITMAPFc;
CHOOSECOLOR_FID_CACHE CHOOSECOLORFc;
COMPOSITIONFORM_FID_CACHE COMPOSITIONFORMFc;
CREATESTRUCT_FID_CACHE CREATESTRUCTFc;
DIBSECTION_FID_CACHE DIBSECTIONFc;
DLLVERSIONINFO_FID_CACHE DLLVERSIONINFOFc;
DRAWITEMSTRUCT_FID_CACHE DRAWITEMSTRUCTFc;
FILETIME_FID_CACHE FILETIMEFc;
HDITEM_FID_CACHE HDITEMFc;
ICONINFO_FID_CACHE ICONINFOFc;
INITCOMMONCONTROLSEX_FID_CACHE INITCOMMONCONTROLSEXFc;
LOGBRUSH_FID_CACHE LOGBRUSHFc;
LOGFONT_FID_CACHE LOGFONTFc;
LOGPEN_FID_CACHE LOGPENFc;
LVCOLUMN_FID_CACHE LVCOLUMNFc;
LVHITTESTINFO_FID_CACHE LVHITTESTINFOFc;
LVITEM_FID_CACHE LVITEMFc;
MEASUREITEMSTRUCT_FID_CACHE MEASUREITEMSTRUCTFc;
MENUITEMINFO_FID_CACHE MENUITEMINFOFc;
MSG_FID_CACHE MSGFc;
NMHDR_FID_CACHE NMHDRFc;
NMHEADER_FID_CACHE NMHEADERFc;
NMLISTVIEW_FID_CACHE NMLISTVIEWFc;
NMTOOLBAR_FID_CACHE NMTOOLBARFc;
OPENFILENAME_FID_CACHE OPENFILENAMEFc;
OSVERSIONINFO_FID_CACHE OSVERSIONINFOFc;
PAINTSTRUCT_FID_CACHE PAINTSTRUCTFc;
POINT_FID_CACHE POINTFc;
REBARBANDINFO_FID_CACHE REBARBANDINFOFc;
RECT_FID_CACHE RECTFc;
SCROLLINFO_FID_CACHE SCROLLINFOFc;
SHELLEXECUTEINFO_FID_CACHE SHELLEXECUTEINFOFc;
SIZE_FID_CACHE SIZEFc;
TBBUTTON_FID_CACHE TBBUTTONFc;
TBBUTTONINFO_FID_CACHE TBBUTTONINFOFc;
TCITEM_FID_CACHE TCITEMFc;
TEXTMETRIC_FID_CACHE TEXTMETRICFc;
TVHITTESTINFO_FID_CACHE TVHITTESTINFOFc;
TVINSERTSTRUCT_FID_CACHE TVINSERTSTRUCTFc;
TVITEM_FID_CACHE TVITEMFc;
WINDOWPOS_FID_CACHE WINDOWPOSFc;
WNDCLASS_FID_CACHE WNDCLASSFc;
#ifndef _WIN32_WCE
	BROWSEINFO_FID_CACHE BROWSEINFOFc;
	CHOOSEFONT_FID_CACHE CHOOSEFONTFc;
	DOCINFO_FID_CACHE DOCINFOFc;
	GCP_RESULTS_FID_CACHE GCP_RESULTSFc;
	GRADIENT_RECT_FID_CACHE GRADIENT_RECTFc;
	HELPINFO_FID_CACHE HELPINFOFc;
	MENUINFO_FID_CACHE MENUINFOFc;
	NMTTDISPINFO_FID_CACHE NMTTDISPINFOFc;
	NONCLIENTMETRICS_FID_CACHE NONCLIENTMETRICSFc;
	PRINTDLG_FID_CACHE PRINTDLGFc;
	TOOLINFO_FID_CACHE TOOLINFOFc;
	TRACKMOUSEEVENT_FID_CACHE TRACKMOUSEEVENTFc;
	TRIVERTEX_FID_CACHE TRIVERTEXFc;
	WINDOWPLACEMENT_FID_CACHE WINDOWPLACEMENTFc;
	DROPFILES_FID_CACHE DROPFILESFc;
	OLECMD_FID_CACHE OLECMDFc;
	OLECMDTEXT_FID_CACHE OLECMDTEXTFc;
#endif

/* OLE globals */
CAUUID_FID_CACHE CAUUIDFc;
CONTROLINFO_FID_CACHE CONTROLINFOFc;
COSERVERINFO_FID_CACHE COSERVERINFOFc;
DISPPARAMS_FID_CACHE DISPPARAMSFc;
DVTARGETDEVICE_FID_CACHE DVTARGETDEVICEFc;
EXCEPINFO_FID_CACHE EXCEPINFOFc;
FORMATETC_FID_CACHE FORMATETCFc;
FUNCDESC1_FID_CACHE FUNCDESC1Fc;
FUNCDESC2_FID_CACHE FUNCDESC2Fc;
GUID_FID_CACHE GUIDFc;
LICINFO_FID_CACHE LICINFOFc;
OLEINPLACEFRAMEINFO_FID_CACHE OLEINPLACEFRAMEINFOFc;
STATSTG_FID_CACHE STATSTGFc;
STGMEDIUM_FID_CACHE STGMEDIUMFc;
TYPEATTR_FID_CACHE TYPEATTRFc;
VARDESC1_FID_CACHE VARDESC1Fc;
VARDESC2_FID_CACHE VARDESC2Fc;

void cacheACCELFids(JNIEnv *env, jobject lpObject, PACCEL_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->fVirt = (*env)->GetFieldID(env, lpCache->clazz, "fVirt", "B");
	lpCache->key = (*env)->GetFieldID(env, lpCache->clazz, "key", "S");
	lpCache->cmd = (*env)->GetFieldID(env, lpCache->clazz, "cmd", "S");
	lpCache->cached = 1;
}

ACCEL* getACCELFields(JNIEnv *env, jobject lpObject, ACCEL *lpStruct, PACCEL_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheACCELFids(env, lpObject, lpCache);
	lpStruct->fVirt = (*env)->GetByteField(env, lpObject, lpCache->fVirt);
	lpStruct->key = (*env)->GetShortField(env, lpObject, lpCache->key);
	lpStruct->cmd = (*env)->GetShortField(env, lpObject, lpCache->cmd);
	return lpStruct;
}

void setACCELFields(JNIEnv *env, jobject lpObject, ACCEL *lpStruct, PACCEL_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheACCELFids(env, lpObject, lpCache);
	(*env)->SetByteField(env, lpObject, lpCache->fVirt, lpStruct->fVirt);
	(*env)->SetShortField(env, lpObject, lpCache->key, lpStruct->key);
	(*env)->SetShortField(env, lpObject, lpCache->cmd, lpStruct->cmd);
}

void cacheBITMAPFids(JNIEnv *env, jobject lpObject, PBITMAP_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->bmType = (*env)->GetFieldID(env, lpCache->clazz, "bmType", "I");
	lpCache->bmWidth = (*env)->GetFieldID(env, lpCache->clazz, "bmWidth", "I");
	lpCache->bmHeight = (*env)->GetFieldID(env, lpCache->clazz, "bmHeight", "I");
	lpCache->bmWidthBytes = (*env)->GetFieldID(env, lpCache->clazz, "bmWidthBytes", "I");
	lpCache->bmPlanes = (*env)->GetFieldID(env, lpCache->clazz, "bmPlanes", "S");
	lpCache->bmBitsPixel = (*env)->GetFieldID(env, lpCache->clazz, "bmBitsPixel", "S");
	lpCache->bmBits = (*env)->GetFieldID(env, lpCache->clazz, "bmBits", "I");
	lpCache->cached = 1;
}

BITMAP* getBITMAPFields(JNIEnv *env, jobject lpObject, BITMAP *lpStruct, PBITMAP_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheBITMAPFids(env, lpObject, lpCache);
	lpStruct->bmType = (*env)->GetIntField(env, lpObject, lpCache->bmType);
	lpStruct->bmWidth = (*env)->GetIntField(env, lpObject, lpCache->bmWidth);
	lpStruct->bmHeight = (*env)->GetIntField(env, lpObject, lpCache->bmHeight);
	lpStruct->bmWidthBytes = (*env)->GetIntField(env, lpObject, lpCache->bmWidthBytes);
	lpStruct->bmPlanes = (*env)->GetShortField(env, lpObject, lpCache->bmPlanes);
	lpStruct->bmBitsPixel = (*env)->GetShortField(env, lpObject, lpCache->bmBitsPixel);
	lpStruct->bmBits = (LPVOID)(*env)->GetIntField(env, lpObject, lpCache->bmBits);
	return lpStruct;
}

void setBITMAPFields(JNIEnv *env, jobject lpObject, BITMAP *lpStruct, PBITMAP_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheBITMAPFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->bmType, lpStruct->bmType);
	(*env)->SetIntField(env, lpObject, lpCache->bmWidth, lpStruct->bmWidth);
	(*env)->SetIntField(env, lpObject, lpCache->bmHeight, lpStruct->bmHeight);
	(*env)->SetIntField(env, lpObject, lpCache->bmWidthBytes, lpStruct->bmWidthBytes);
	(*env)->SetShortField(env, lpObject, lpCache->bmPlanes, lpStruct->bmPlanes);
	(*env)->SetShortField(env, lpObject, lpCache->bmBitsPixel, lpStruct->bmBitsPixel);
	(*env)->SetIntField(env, lpObject, lpCache->bmBits, (jint)lpStruct->bmBits);
}

#ifndef _WIN32_WCE

void cacheBROWSEINFOFids(JNIEnv *env, jobject lpObject, PBROWSEINFO_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->hwndOwner = (*env)->GetFieldID(env, lpCache->clazz, "hwndOwner", "I");
	lpCache->pidlRoot = (*env)->GetFieldID(env, lpCache->clazz, "pidlRoot", "I");
	lpCache->pszDisplayName = (*env)->GetFieldID(env, lpCache->clazz, "pszDisplayName", "I");
	lpCache->lpszTitle = (*env)->GetFieldID(env, lpCache->clazz, "lpszTitle", "I");
	lpCache->ulFlags = (*env)->GetFieldID(env, lpCache->clazz, "ulFlags", "I");
	lpCache->lpfn = (*env)->GetFieldID(env, lpCache->clazz, "lpfn", "I");
	lpCache->lParam = (*env)->GetFieldID(env, lpCache->clazz, "lParam", "I");
	lpCache->iImage = (*env)->GetFieldID(env, lpCache->clazz, "iImage", "I");
	lpCache->cached = 1;
}

BROWSEINFO* getBROWSEINFOFields(JNIEnv *env, jobject lpObject, BROWSEINFO *lpStruct, PBROWSEINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheBROWSEINFOFids(env, lpObject, lpCache);
	lpStruct->hwndOwner = (HWND)(*env)->GetIntField(env, lpObject, lpCache->hwndOwner);
	lpStruct->pidlRoot = (LPCITEMIDLIST)(*env)->GetIntField(env, lpObject, lpCache->pidlRoot);
	lpStruct->pszDisplayName = (LPTSTR)(*env)->GetIntField(env, lpObject, lpCache->pszDisplayName);
	lpStruct->lpszTitle = (LPCTSTR)(*env)->GetIntField(env, lpObject, lpCache->lpszTitle);
	lpStruct->ulFlags = (*env)->GetIntField(env, lpObject, lpCache->ulFlags);
	lpStruct->lpfn = (BFFCALLBACK)(*env)->GetIntField(env, lpObject, lpCache->lpfn);
	lpStruct->lParam = (*env)->GetIntField(env, lpObject, lpCache->lParam);
	lpStruct->iImage = (*env)->GetIntField(env, lpObject, lpCache->iImage);
	return lpStruct;
}

void setBROWSEINFOFields(JNIEnv *env, jobject lpObject, BROWSEINFO *lpStruct, PBROWSEINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheBROWSEINFOFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->hwndOwner, (jint)lpStruct->hwndOwner);
	(*env)->SetIntField(env, lpObject, lpCache->pidlRoot, (jint)lpStruct->pidlRoot);
	(*env)->SetIntField(env, lpObject, lpCache->pszDisplayName, (jint)lpStruct->pszDisplayName);
	(*env)->SetIntField(env, lpObject, lpCache->lpszTitle, (jint)lpStruct->lpszTitle);
	(*env)->SetIntField(env, lpObject, lpCache->ulFlags, lpStruct->ulFlags);
	(*env)->SetIntField(env, lpObject, lpCache->lpfn, (jint)lpStruct->lpfn);
	(*env)->SetIntField(env, lpObject, lpCache->lParam, lpStruct->lParam);
	(*env)->SetIntField(env, lpObject, lpCache->iImage, lpStruct->iImage);
}

#endif // _WIN32_WCE

void cacheCHOOSECOLORFids(JNIEnv *env, jobject lpObject, PCHOOSECOLOR_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->lStructSize = (*env)->GetFieldID(env, lpCache->clazz, "lStructSize", "I");
	lpCache->hwndOwner = (*env)->GetFieldID(env, lpCache->clazz, "hwndOwner", "I");
	lpCache->hInstance = (*env)->GetFieldID(env, lpCache->clazz, "hInstance", "I");
	lpCache->rgbResult = (*env)->GetFieldID(env, lpCache->clazz, "rgbResult", "I");
	lpCache->lpCustColors = (*env)->GetFieldID(env, lpCache->clazz, "lpCustColors", "I");
	lpCache->Flags = (*env)->GetFieldID(env, lpCache->clazz, "Flags", "I");
	lpCache->lCustData = (*env)->GetFieldID(env, lpCache->clazz, "lCustData", "I");
	lpCache->lpfnHook = (*env)->GetFieldID(env, lpCache->clazz, "lpfnHook", "I");
	lpCache->lpTemplateName = (*env)->GetFieldID(env, lpCache->clazz, "lpTemplateName", "I");
	lpCache->cached = 1;
}

CHOOSECOLOR* getCHOOSECOLORFields(JNIEnv *env, jobject lpObject, CHOOSECOLOR *lpStruct, PCHOOSECOLOR_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheCHOOSECOLORFids(env, lpObject, lpCache);
	lpStruct->lStructSize = (*env)->GetIntField(env, lpObject, lpCache->lStructSize);
	lpStruct->hwndOwner = (HWND)(*env)->GetIntField(env, lpObject, lpCache->hwndOwner);
	lpStruct->hInstance = (HANDLE)(*env)->GetIntField(env, lpObject, lpCache->hInstance);
	lpStruct->rgbResult = (*env)->GetIntField(env, lpObject, lpCache->rgbResult);
	lpStruct->lpCustColors = (COLORREF *)(*env)->GetIntField(env, lpObject, lpCache->lpCustColors);
	lpStruct->Flags = (*env)->GetIntField(env, lpObject, lpCache->Flags);
	lpStruct->lCustData = (*env)->GetIntField(env, lpObject, lpCache->lCustData);
	lpStruct->lpfnHook = (LPCCHOOKPROC)(*env)->GetIntField(env, lpObject, lpCache->lpfnHook);
	lpStruct->lpTemplateName = (LPCTSTR)(*env)->GetIntField(env, lpObject, lpCache->lpTemplateName);
	return lpStruct;
}

void setCHOOSECOLORFields(JNIEnv *env, jobject lpObject, CHOOSECOLOR *lpStruct, PCHOOSECOLOR_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheCHOOSECOLORFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->lStructSize, lpStruct->lStructSize);
	(*env)->SetIntField(env, lpObject, lpCache->hwndOwner, (jint)lpStruct->hwndOwner);
	(*env)->SetIntField(env, lpObject, lpCache->hInstance, (jint)lpStruct->hInstance);
	(*env)->SetIntField(env, lpObject, lpCache->rgbResult, lpStruct->rgbResult);
	(*env)->SetIntField(env, lpObject, lpCache->lpCustColors, (jint)lpStruct->lpCustColors);
	(*env)->SetIntField(env, lpObject, lpCache->Flags, lpStruct->Flags);
	(*env)->SetIntField(env, lpObject, lpCache->lCustData, lpStruct->lCustData);
	(*env)->SetIntField(env, lpObject, lpCache->lpfnHook, (jint)lpStruct->lpfnHook);
	(*env)->SetIntField(env, lpObject, lpCache->lpTemplateName, (jint)lpStruct->lpTemplateName);
}

#ifndef _WIN32_WCE

void cacheCHOOSEFONTFids(JNIEnv *env, jobject lpObject, PCHOOSEFONT_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->lStructSize = (*env)->GetFieldID(env, lpCache->clazz, "lStructSize", "I");
	lpCache->hwndOwner = (*env)->GetFieldID(env, lpCache->clazz, "hwndOwner", "I");
	lpCache->hDC = (*env)->GetFieldID(env, lpCache->clazz, "hDC", "I");
	lpCache->lpLogFont = (*env)->GetFieldID(env, lpCache->clazz, "lpLogFont", "I");
	lpCache->iPointSize = (*env)->GetFieldID(env, lpCache->clazz, "iPointSize", "I");
	lpCache->Flags = (*env)->GetFieldID(env, lpCache->clazz, "Flags", "I");
	lpCache->rgbColors = (*env)->GetFieldID(env, lpCache->clazz, "rgbColors", "I");
	lpCache->lCustData = (*env)->GetFieldID(env, lpCache->clazz, "lCustData", "I");
	lpCache->lpfnHook = (*env)->GetFieldID(env, lpCache->clazz, "lpfnHook", "I");
	lpCache->lpTemplateName = (*env)->GetFieldID(env, lpCache->clazz, "lpTemplateName", "I");
	lpCache->hInstance = (*env)->GetFieldID(env, lpCache->clazz, "hInstance", "I");
	lpCache->lpszStyle = (*env)->GetFieldID(env, lpCache->clazz, "lpszStyle", "I");
	lpCache->nFontType = (*env)->GetFieldID(env, lpCache->clazz, "nFontType", "S");
	lpCache->nSizeMin = (*env)->GetFieldID(env, lpCache->clazz, "nSizeMin", "I");
	lpCache->nSizeMax = (*env)->GetFieldID(env, lpCache->clazz, "nSizeMax", "I");
	lpCache->cached = 1;
}

CHOOSEFONT* getCHOOSEFONTFields(JNIEnv *env, jobject lpObject, CHOOSEFONT *lpStruct, PCHOOSEFONT_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheCHOOSEFONTFids(env, lpObject, lpCache);
	lpStruct->lStructSize = (*env)->GetIntField(env, lpObject, lpCache->lStructSize);
	lpStruct->hwndOwner = (HWND)(*env)->GetIntField(env, lpObject, lpCache->hwndOwner);
	lpStruct->hDC = (HDC)(*env)->GetIntField(env, lpObject, lpCache->hDC);
	lpStruct->lpLogFont = (LPLOGFONT)(*env)->GetIntField(env, lpObject, lpCache->lpLogFont);
	lpStruct->iPointSize = (*env)->GetIntField(env, lpObject, lpCache->iPointSize);
	lpStruct->Flags = (*env)->GetIntField(env, lpObject, lpCache->Flags);
	lpStruct->rgbColors = (*env)->GetIntField(env, lpObject, lpCache->rgbColors);
	lpStruct->lCustData = (*env)->GetIntField(env, lpObject, lpCache->lCustData);
	lpStruct->lpfnHook = (LPCFHOOKPROC)(*env)->GetIntField(env, lpObject, lpCache->lpfnHook);
	lpStruct->lpTemplateName = (LPCTSTR)(*env)->GetIntField(env, lpObject, lpCache->lpTemplateName);
	lpStruct->hInstance = (HINSTANCE)(*env)->GetIntField(env, lpObject, lpCache->hInstance);
	lpStruct->lpszStyle = (LPTSTR)(*env)->GetIntField(env, lpObject, lpCache->lpszStyle);
	lpStruct->nFontType = (*env)->GetShortField(env, lpObject, lpCache->nFontType);
	lpStruct->nSizeMin = (*env)->GetIntField(env, lpObject, lpCache->nSizeMin);
	lpStruct->nSizeMax = (*env)->GetIntField(env, lpObject, lpCache->nSizeMax);
	return lpStruct;
}

void setCHOOSEFONTFields(JNIEnv *env, jobject lpObject, CHOOSEFONT *lpStruct, PCHOOSEFONT_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheCHOOSEFONTFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->lStructSize, lpStruct->lStructSize);
	(*env)->SetIntField(env, lpObject, lpCache->hwndOwner, (jint)lpStruct->hwndOwner);
	(*env)->SetIntField(env, lpObject, lpCache->hDC, (jint)lpStruct->hDC);
	(*env)->SetIntField(env, lpObject, lpCache->lpLogFont, (jint)lpStruct->lpLogFont);
	(*env)->SetIntField(env, lpObject, lpCache->iPointSize, lpStruct->iPointSize);
	(*env)->SetIntField(env, lpObject, lpCache->Flags, lpStruct->Flags);
	(*env)->SetIntField(env, lpObject, lpCache->rgbColors, lpStruct->rgbColors);
	(*env)->SetIntField(env, lpObject, lpCache->lCustData, lpStruct->lCustData);
	(*env)->SetIntField(env, lpObject, lpCache->lpfnHook, (jint)lpStruct->lpfnHook);
	(*env)->SetIntField(env, lpObject, lpCache->lpTemplateName, (jint)lpStruct->lpTemplateName);
	(*env)->SetIntField(env, lpObject, lpCache->hInstance, (jint)lpStruct->hInstance);
	(*env)->SetIntField(env, lpObject, lpCache->lpszStyle, (jint)lpStruct->lpszStyle);
	(*env)->SetShortField(env, lpObject, lpCache->nFontType, lpStruct->nFontType);
	(*env)->SetIntField(env, lpObject, lpCache->nSizeMin, lpStruct->nSizeMin);
	(*env)->SetIntField(env, lpObject, lpCache->nSizeMax, lpStruct->nSizeMax);
}

#endif // _WIN32_WCE

void cacheCOMPOSITIONFORMFids(JNIEnv *env, jobject lpObject, PCOMPOSITIONFORM_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->dwStyle = (*env)->GetFieldID(env, lpCache->clazz, "dwStyle", "I");
	lpCache->x = (*env)->GetFieldID(env, lpCache->clazz, "x", "I");
	lpCache->y = (*env)->GetFieldID(env, lpCache->clazz, "y", "I");
	lpCache->left = (*env)->GetFieldID(env, lpCache->clazz, "left", "I");
	lpCache->top = (*env)->GetFieldID(env, lpCache->clazz, "top", "I");
	lpCache->right = (*env)->GetFieldID(env, lpCache->clazz, "right", "I");
	lpCache->bottom = (*env)->GetFieldID(env, lpCache->clazz, "bottom", "I");
	lpCache->cached = 1;
}

COMPOSITIONFORM* getCOMPOSITIONFORMFields(JNIEnv *env, jobject lpObject, COMPOSITIONFORM *lpStruct, PCOMPOSITIONFORM_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheCOMPOSITIONFORMFids(env, lpObject, lpCache);
	lpStruct->dwStyle = (*env)->GetIntField(env, lpObject, lpCache->dwStyle);
	lpStruct->ptCurrentPos.x = (*env)->GetIntField(env, lpObject, lpCache->x);
	lpStruct->ptCurrentPos.y = (*env)->GetIntField(env, lpObject, lpCache->y);
	lpStruct->rcArea.left = (*env)->GetIntField(env, lpObject, lpCache->left);
	lpStruct->rcArea.top = (*env)->GetIntField(env, lpObject, lpCache->top);
	lpStruct->rcArea.right = (*env)->GetIntField(env, lpObject, lpCache->right);
	lpStruct->rcArea.bottom = (*env)->GetIntField(env, lpObject, lpCache->bottom);
	return lpStruct;
}

void setCOMPOSITIONFORMFields(JNIEnv *env, jobject lpObject, COMPOSITIONFORM *lpStruct, PCOMPOSITIONFORM_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheCOMPOSITIONFORMFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->dwStyle, lpStruct->dwStyle);
	(*env)->SetIntField(env, lpObject, lpCache->x, lpStruct->ptCurrentPos.x);
	(*env)->SetIntField(env, lpObject, lpCache->y, lpStruct->ptCurrentPos.y);
	(*env)->SetIntField(env, lpObject, lpCache->left, lpStruct->rcArea.left);
	(*env)->SetIntField(env, lpObject, lpCache->top, lpStruct->rcArea.top);
	(*env)->SetIntField(env, lpObject, lpCache->right, lpStruct->rcArea.right);
	(*env)->SetIntField(env, lpObject, lpCache->bottom, lpStruct->rcArea.bottom);
}

void cacheCREATESTRUCTFids(JNIEnv *env, jobject lpObject, PCREATESTRUCT_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->lpCreateParams = (*env)->GetFieldID(env, lpCache->clazz, "lpCreateParams", "I");
	lpCache->hInstance = (*env)->GetFieldID(env, lpCache->clazz, "hInstance", "I");
	lpCache->hMenu = (*env)->GetFieldID(env, lpCache->clazz, "hMenu", "I");
	lpCache->hwndParent = (*env)->GetFieldID(env, lpCache->clazz, "hwndParent", "I");
	lpCache->cy = (*env)->GetFieldID(env, lpCache->clazz, "cy", "I");
	lpCache->cx = (*env)->GetFieldID(env, lpCache->clazz, "cx", "I");
	lpCache->y = (*env)->GetFieldID(env, lpCache->clazz, "y", "I");
	lpCache->x = (*env)->GetFieldID(env, lpCache->clazz, "x", "I");
	lpCache->style = (*env)->GetFieldID(env, lpCache->clazz, "style", "I");
	lpCache->lpszName = (*env)->GetFieldID(env, lpCache->clazz, "lpszName", "I");
	lpCache->lpszClass = (*env)->GetFieldID(env, lpCache->clazz, "lpszClass", "I");
	lpCache->dwExStyle = (*env)->GetFieldID(env, lpCache->clazz, "dwExStyle", "I");
	lpCache->cached = 1;
}

CREATESTRUCT* getCREATESTRUCTFields(JNIEnv *env, jobject lpObject, CREATESTRUCT *lpStruct, PCREATESTRUCT_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheCREATESTRUCTFids(env, lpObject, lpCache);
	lpStruct->lpCreateParams = (LPVOID)(*env)->GetIntField(env, lpObject, lpCache->lpCreateParams);
	lpStruct->hInstance = (HINSTANCE)(*env)->GetIntField(env, lpObject, lpCache->hInstance);
	lpStruct->hMenu = (HMENU)(*env)->GetIntField(env, lpObject, lpCache->hMenu);
	lpStruct->hwndParent = (HWND)(*env)->GetIntField(env, lpObject, lpCache->hwndParent);
	lpStruct->cy = (*env)->GetIntField(env, lpObject, lpCache->cy);
	lpStruct->cx = (*env)->GetIntField(env, lpObject, lpCache->cx);
	lpStruct->y = (*env)->GetIntField(env, lpObject, lpCache->y);
	lpStruct->x = (*env)->GetIntField(env, lpObject, lpCache->x);
	lpStruct->style = (*env)->GetIntField(env, lpObject, lpCache->style);
	lpStruct->lpszName = (LPCTSTR)(*env)->GetIntField(env, lpObject, lpCache->lpszName);
	lpStruct->lpszClass = (LPCTSTR)(*env)->GetIntField(env, lpObject, lpCache->lpszClass);
	lpStruct->dwExStyle = (*env)->GetIntField(env, lpObject, lpCache->dwExStyle);
	return lpStruct;
}

void setCREATESTRUCTFields(JNIEnv *env, jobject lpObject, CREATESTRUCT *lpStruct, PCREATESTRUCT_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheCREATESTRUCTFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->lpCreateParams, (jint)lpStruct->lpCreateParams);
	(*env)->SetIntField(env, lpObject, lpCache->hInstance, (jint)lpStruct->hInstance);
	(*env)->SetIntField(env, lpObject, lpCache->hMenu, (jint)lpStruct->hMenu);
	(*env)->SetIntField(env, lpObject, lpCache->hwndParent, (jint)lpStruct->hwndParent);
	(*env)->SetIntField(env, lpObject, lpCache->cy, lpStruct->cy);
	(*env)->SetIntField(env, lpObject, lpCache->cx, lpStruct->cx);
	(*env)->SetIntField(env, lpObject, lpCache->y, lpStruct->y);
	(*env)->SetIntField(env, lpObject, lpCache->x, lpStruct->x);
	(*env)->SetIntField(env, lpObject, lpCache->style, lpStruct->style);
	(*env)->SetIntField(env, lpObject, lpCache->lpszName, (jint)lpStruct->lpszName);
	(*env)->SetIntField(env, lpObject, lpCache->lpszClass, (jint)lpStruct->lpszClass);
	(*env)->SetIntField(env, lpObject, lpCache->dwExStyle, lpStruct->dwExStyle);
}

void cacheDIBSECTIONFids(JNIEnv *env, jobject lpObject, PDIBSECTION_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->bmType = (*env)->GetFieldID(env, lpCache->clazz, "bmType", "I");
	lpCache->bmWidth = (*env)->GetFieldID(env, lpCache->clazz, "bmWidth", "I");
	lpCache->bmHeight = (*env)->GetFieldID(env, lpCache->clazz, "bmHeight", "I");
	lpCache->bmWidthBytes = (*env)->GetFieldID(env, lpCache->clazz, "bmWidthBytes", "I");
	lpCache->bmPlanes = (*env)->GetFieldID(env, lpCache->clazz, "bmPlanes", "S");
	lpCache->bmBitsPixel = (*env)->GetFieldID(env, lpCache->clazz, "bmBitsPixel", "S");
	lpCache->bmBits = (*env)->GetFieldID(env, lpCache->clazz, "bmBits", "I");
	lpCache->biSize = (*env)->GetFieldID(env, lpCache->clazz, "biSize", "I");
	lpCache->biWidth = (*env)->GetFieldID(env, lpCache->clazz, "biWidth", "I");
	lpCache->biHeight = (*env)->GetFieldID(env, lpCache->clazz, "biHeight", "I");
	lpCache->biPlanes = (*env)->GetFieldID(env, lpCache->clazz, "biPlanes", "S");
	lpCache->biBitCount = (*env)->GetFieldID(env, lpCache->clazz, "biBitCount", "S");
	lpCache->biCompression = (*env)->GetFieldID(env, lpCache->clazz, "biCompression", "I");
	lpCache->biSizeImage = (*env)->GetFieldID(env, lpCache->clazz, "biSizeImage", "I");
	lpCache->biXPelsPerMeter = (*env)->GetFieldID(env, lpCache->clazz, "biXPelsPerMeter", "I");
	lpCache->biYPelsPerMeter = (*env)->GetFieldID(env, lpCache->clazz, "biYPelsPerMeter", "I");
	lpCache->biClrUsed = (*env)->GetFieldID(env, lpCache->clazz, "biClrUsed", "I");
	lpCache->biClrImportant = (*env)->GetFieldID(env, lpCache->clazz, "biClrImportant", "I");
	lpCache->dsBitfields0 = (*env)->GetFieldID(env, lpCache->clazz, "dsBitfields0", "I");
	lpCache->dsBitfields1 = (*env)->GetFieldID(env, lpCache->clazz, "dsBitfields1", "I");
	lpCache->dsBitfields2 = (*env)->GetFieldID(env, lpCache->clazz, "dsBitfields2", "I");
	lpCache->dshSection = (*env)->GetFieldID(env, lpCache->clazz, "dshSection", "I");
	lpCache->dsOffset = (*env)->GetFieldID(env, lpCache->clazz, "dsOffset", "I");
	lpCache->cached = 1;
}

DIBSECTION* getDIBSECTIONFields(JNIEnv *env, jobject lpObject, DIBSECTION *lpStruct, PDIBSECTION_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheDIBSECTIONFids(env, lpObject, lpCache);
	lpStruct->dsBm.bmType = (*env)->GetIntField(env, lpObject, lpCache->bmType);
	lpStruct->dsBm.bmWidth = (*env)->GetIntField(env, lpObject, lpCache->bmWidth);
	lpStruct->dsBm.bmHeight = (*env)->GetIntField(env, lpObject, lpCache->bmHeight);
	lpStruct->dsBm.bmWidthBytes = (*env)->GetIntField(env, lpObject, lpCache->bmWidthBytes);
	lpStruct->dsBm.bmPlanes = (*env)->GetShortField(env, lpObject, lpCache->bmPlanes);
	lpStruct->dsBm.bmBitsPixel = (*env)->GetShortField(env, lpObject, lpCache->bmBitsPixel);
	lpStruct->dsBm.bmBits = (LPVOID)(*env)->GetIntField(env, lpObject, lpCache->bmBits);
	lpStruct->dsBmih.biSize = (*env)->GetIntField(env, lpObject, lpCache->biSize);
	lpStruct->dsBmih.biWidth = (*env)->GetIntField(env, lpObject, lpCache->biWidth);
	lpStruct->dsBmih.biHeight = (*env)->GetIntField(env, lpObject, lpCache->biHeight);
	lpStruct->dsBmih.biPlanes = (*env)->GetShortField(env, lpObject, lpCache->biPlanes);
	lpStruct->dsBmih.biBitCount = (*env)->GetShortField(env, lpObject, lpCache->biBitCount);
	lpStruct->dsBmih.biCompression = (*env)->GetIntField(env, lpObject, lpCache->biCompression);
	lpStruct->dsBmih.biSizeImage = (*env)->GetIntField(env, lpObject, lpCache->biSizeImage);
	lpStruct->dsBmih.biXPelsPerMeter = (*env)->GetIntField(env, lpObject, lpCache->biXPelsPerMeter);
	lpStruct->dsBmih.biYPelsPerMeter = (*env)->GetIntField(env, lpObject, lpCache->biYPelsPerMeter);
	lpStruct->dsBmih.biClrUsed = (*env)->GetIntField(env, lpObject, lpCache->biClrUsed);
	lpStruct->dsBmih.biClrImportant = (*env)->GetIntField(env, lpObject, lpCache->biClrImportant);
	lpStruct->dsBitfields[0] = (*env)->GetIntField(env, lpObject, lpCache->dsBitfields0);
	lpStruct->dsBitfields[1] = (*env)->GetIntField(env, lpObject, lpCache->dsBitfields1);
	lpStruct->dsBitfields[2] = (*env)->GetIntField(env, lpObject, lpCache->dsBitfields2);
	lpStruct->dshSection = (HANDLE)(*env)->GetIntField(env, lpObject, lpCache->dshSection);
	lpStruct->dsOffset = (*env)->GetIntField(env, lpObject, lpCache->dsOffset);
	return lpStruct;
}

void setDIBSECTIONFields(JNIEnv *env, jobject lpObject, DIBSECTION *lpStruct, PDIBSECTION_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheDIBSECTIONFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->bmType, lpStruct->dsBm.bmType);
	(*env)->SetIntField(env, lpObject, lpCache->bmWidth, lpStruct->dsBm.bmWidth);
	(*env)->SetIntField(env, lpObject, lpCache->bmHeight, lpStruct->dsBm.bmHeight);
	(*env)->SetIntField(env, lpObject, lpCache->bmWidthBytes, lpStruct->dsBm.bmWidthBytes);
	(*env)->SetShortField(env, lpObject, lpCache->bmPlanes, lpStruct->dsBm.bmPlanes);
	(*env)->SetShortField(env, lpObject, lpCache->bmBitsPixel, lpStruct->dsBm.bmBitsPixel);
	(*env)->SetIntField(env, lpObject, lpCache->bmBits, (jint)lpStruct->dsBm.bmBits);
	(*env)->SetIntField(env, lpObject, lpCache->biSize, lpStruct->dsBmih.biSize);
	(*env)->SetIntField(env, lpObject, lpCache->biWidth, lpStruct->dsBmih.biWidth);
	(*env)->SetIntField(env, lpObject, lpCache->biHeight, lpStruct->dsBmih.biHeight);
	(*env)->SetShortField(env, lpObject, lpCache->biPlanes, lpStruct->dsBmih.biPlanes);
	(*env)->SetShortField(env, lpObject, lpCache->biBitCount, lpStruct->dsBmih.biBitCount);
	(*env)->SetIntField(env, lpObject, lpCache->biCompression, lpStruct->dsBmih.biCompression);
	(*env)->SetIntField(env, lpObject, lpCache->biSizeImage, lpStruct->dsBmih.biSizeImage);
	(*env)->SetIntField(env, lpObject, lpCache->biXPelsPerMeter, lpStruct->dsBmih.biXPelsPerMeter);
	(*env)->SetIntField(env, lpObject, lpCache->biYPelsPerMeter, lpStruct->dsBmih.biYPelsPerMeter);
	(*env)->SetIntField(env, lpObject, lpCache->biClrUsed, lpStruct->dsBmih.biClrUsed);
	(*env)->SetIntField(env, lpObject, lpCache->biClrImportant, lpStruct->dsBmih.biClrImportant);
	(*env)->SetIntField(env, lpObject, lpCache->dsBitfields0, lpStruct->dsBitfields[0]);
	(*env)->SetIntField(env, lpObject, lpCache->dsBitfields1, lpStruct->dsBitfields[1]);
	(*env)->SetIntField(env, lpObject, lpCache->dsBitfields2, lpStruct->dsBitfields[2]);
	(*env)->SetIntField(env, lpObject, lpCache->dshSection, (jint)lpStruct->dshSection);
	(*env)->SetIntField(env, lpObject, lpCache->dsOffset, lpStruct->dsOffset);
}

void cacheDLLVERSIONINFOFids(JNIEnv *env, jobject lpObject, PDLLVERSIONINFO_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->cbSize = (*env)->GetFieldID(env, lpCache->clazz, "cbSize", "I");
	lpCache->dwMajorVersion = (*env)->GetFieldID(env, lpCache->clazz, "dwMajorVersion", "I");
	lpCache->dwMinorVersion = (*env)->GetFieldID(env, lpCache->clazz, "dwMinorVersion", "I");
	lpCache->dwBuildNumber = (*env)->GetFieldID(env, lpCache->clazz, "dwBuildNumber", "I");
	lpCache->dwPlatformID = (*env)->GetFieldID(env, lpCache->clazz, "dwPlatformID", "I");
	lpCache->cached = 1;
}

DLLVERSIONINFO* getDLLVERSIONINFOFields(JNIEnv *env, jobject lpObject, DLLVERSIONINFO *lpStruct, PDLLVERSIONINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheDLLVERSIONINFOFids(env, lpObject, lpCache);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, lpCache->cbSize);
	lpStruct->dwMajorVersion = (*env)->GetIntField(env, lpObject, lpCache->dwMajorVersion);
	lpStruct->dwMinorVersion = (*env)->GetIntField(env, lpObject, lpCache->dwMinorVersion);
	lpStruct->dwBuildNumber = (*env)->GetIntField(env, lpObject, lpCache->dwBuildNumber);
	lpStruct->dwPlatformID = (*env)->GetIntField(env, lpObject, lpCache->dwPlatformID);
	return lpStruct;
}

void setDLLVERSIONINFOFields(JNIEnv *env, jobject lpObject, DLLVERSIONINFO *lpStruct, PDLLVERSIONINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheDLLVERSIONINFOFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->cbSize, lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, lpCache->dwMajorVersion, lpStruct->dwMajorVersion);
	(*env)->SetIntField(env, lpObject, lpCache->dwMinorVersion, lpStruct->dwMinorVersion);
	(*env)->SetIntField(env, lpObject, lpCache->dwBuildNumber, lpStruct->dwBuildNumber);
	(*env)->SetIntField(env, lpObject, lpCache->dwPlatformID, lpStruct->dwPlatformID);
}

#ifndef _WIN32_WCE

void cacheDOCINFOFids(JNIEnv *env, jobject lpObject, PDOCINFO_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->cbSize = (*env)->GetFieldID(env, lpCache->clazz, "cbSize", "I");
	lpCache->lpszDocName = (*env)->GetFieldID(env, lpCache->clazz, "lpszDocName", "I");
	lpCache->lpszOutput = (*env)->GetFieldID(env, lpCache->clazz, "lpszOutput", "I");
	lpCache->lpszDatatype = (*env)->GetFieldID(env, lpCache->clazz, "lpszDatatype", "I");
	lpCache->fwType = (*env)->GetFieldID(env, lpCache->clazz, "fwType", "I");
	lpCache->cached = 1;
}

DOCINFO* getDOCINFOFields(JNIEnv *env, jobject lpObject, DOCINFO *lpStruct, PDOCINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheDOCINFOFids(env, lpObject, lpCache);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, lpCache->cbSize);
	lpStruct->lpszDocName = (LPCTSTR)(*env)->GetIntField(env, lpObject, lpCache->lpszDocName);
	lpStruct->lpszOutput = (LPCTSTR)(*env)->GetIntField(env, lpObject, lpCache->lpszOutput);
	lpStruct->lpszDatatype = (LPCTSTR)(*env)->GetIntField(env, lpObject, lpCache->lpszDatatype);
	lpStruct->fwType = (*env)->GetIntField(env, lpObject, lpCache->fwType);
	return lpStruct;
}

void setDOCINFOFields(JNIEnv *env, jobject lpObject, DOCINFO *lpStruct, PDOCINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheDOCINFOFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->cbSize, lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, lpCache->lpszDocName, (jint)lpStruct->lpszDocName);
	(*env)->SetIntField(env, lpObject, lpCache->lpszOutput, (jint)lpStruct->lpszOutput);
	(*env)->SetIntField(env, lpObject, lpCache->lpszDatatype, (jint)lpStruct->lpszDatatype);
	(*env)->SetIntField(env, lpObject, lpCache->fwType, lpStruct->fwType);
}

#endif // _WIN32_WCE

void cacheDRAWITEMSTRUCTFids(JNIEnv *env, jobject lpObject, PDRAWITEMSTRUCT_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->CtlType = (*env)->GetFieldID(env, lpCache->clazz, "CtlType", "I");
	lpCache->CtlID = (*env)->GetFieldID(env, lpCache->clazz, "CtlID", "I");
	lpCache->itemID = (*env)->GetFieldID(env, lpCache->clazz, "itemID", "I");
	lpCache->itemAction = (*env)->GetFieldID(env, lpCache->clazz, "itemAction", "I");
	lpCache->itemState = (*env)->GetFieldID(env, lpCache->clazz, "itemState", "I");
	lpCache->hwndItem = (*env)->GetFieldID(env, lpCache->clazz, "hwndItem", "I");
	lpCache->hDC = (*env)->GetFieldID(env, lpCache->clazz, "hDC", "I");
	lpCache->left = (*env)->GetFieldID(env, lpCache->clazz, "left", "I");
	lpCache->top = (*env)->GetFieldID(env, lpCache->clazz, "top", "I");
	lpCache->bottom = (*env)->GetFieldID(env, lpCache->clazz, "bottom", "I");
	lpCache->right = (*env)->GetFieldID(env, lpCache->clazz, "right", "I");
	lpCache->itemData = (*env)->GetFieldID(env, lpCache->clazz, "itemData", "I");
	lpCache->cached = 1;
}

DRAWITEMSTRUCT* getDRAWITEMSTRUCTFields(JNIEnv *env, jobject lpObject, DRAWITEMSTRUCT *lpStruct, PDRAWITEMSTRUCT_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheDRAWITEMSTRUCTFids(env, lpObject, lpCache);
	lpStruct->CtlType = (*env)->GetIntField(env, lpObject, lpCache->CtlType);
	lpStruct->CtlID = (*env)->GetIntField(env, lpObject, lpCache->CtlID);
	lpStruct->itemID = (*env)->GetIntField(env, lpObject, lpCache->itemID);
	lpStruct->itemAction = (*env)->GetIntField(env, lpObject, lpCache->itemAction);
	lpStruct->itemState = (*env)->GetIntField(env, lpObject, lpCache->itemState);
	lpStruct->hwndItem = (HWND)(*env)->GetIntField(env, lpObject, lpCache->hwndItem);
	lpStruct->hDC = (HDC)(*env)->GetIntField(env, lpObject, lpCache->hDC);
	lpStruct->rcItem.left = (*env)->GetIntField(env, lpObject, lpCache->left);
	lpStruct->rcItem.top = (*env)->GetIntField(env, lpObject, lpCache->top);
	lpStruct->rcItem.bottom = (*env)->GetIntField(env, lpObject, lpCache->bottom);
	lpStruct->rcItem.right = (*env)->GetIntField(env, lpObject, lpCache->right);
	lpStruct->itemData = (*env)->GetIntField(env, lpObject, lpCache->itemData);
	return lpStruct;
}

void setDRAWITEMSTRUCTFields(JNIEnv *env, jobject lpObject, DRAWITEMSTRUCT *lpStruct, PDRAWITEMSTRUCT_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheDRAWITEMSTRUCTFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->CtlType, lpStruct->CtlType);
	(*env)->SetIntField(env, lpObject, lpCache->CtlID, lpStruct->CtlID);
	(*env)->SetIntField(env, lpObject, lpCache->itemID, lpStruct->itemID);
	(*env)->SetIntField(env, lpObject, lpCache->itemAction, lpStruct->itemAction);
	(*env)->SetIntField(env, lpObject, lpCache->itemState, lpStruct->itemState);
	(*env)->SetIntField(env, lpObject, lpCache->hwndItem, (jint)lpStruct->hwndItem);
	(*env)->SetIntField(env, lpObject, lpCache->hDC, (jint)lpStruct->hDC);
	(*env)->SetIntField(env, lpObject, lpCache->left, lpStruct->rcItem.left);
	(*env)->SetIntField(env, lpObject, lpCache->top, lpStruct->rcItem.top);
	(*env)->SetIntField(env, lpObject, lpCache->bottom, lpStruct->rcItem.bottom);
	(*env)->SetIntField(env, lpObject, lpCache->right, lpStruct->rcItem.right);
	(*env)->SetIntField(env, lpObject, lpCache->itemData, lpStruct->itemData);
}

void cacheFILETIMEFids(JNIEnv *env, jobject lpObject, PFILETIME_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->dwLowDateTime = (*env)->GetFieldID(env, lpCache->clazz, "dwLowDateTime", "I");
	lpCache->dwHighDateTime = (*env)->GetFieldID(env, lpCache->clazz, "dwHighDateTime", "I");
	lpCache->cached = 1;
}

FILETIME* getFILETIMEFields(JNIEnv *env, jobject lpObject, FILETIME *lpStruct, PFILETIME_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheFILETIMEFids(env, lpObject, lpCache);
	lpStruct->dwLowDateTime = (*env)->GetIntField(env, lpObject, lpCache->dwLowDateTime);
	lpStruct->dwHighDateTime = (*env)->GetIntField(env, lpObject, lpCache->dwHighDateTime);
	return lpStruct;
}

void setFILETIMEFields(JNIEnv *env, jobject lpObject, FILETIME *lpStruct, PFILETIME_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheFILETIMEFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->dwLowDateTime, lpStruct->dwLowDateTime);
	(*env)->SetIntField(env, lpObject, lpCache->dwHighDateTime, lpStruct->dwHighDateTime);
}

#ifndef _WIN32_WCE

void cacheGCP_RESULTSFids(JNIEnv *env, jobject lpObject, PGCP_RESULTS_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->lStructSize = (*env)->GetFieldID(env, lpCache->clazz, "lStructSize", "I");
	lpCache->lpOutString = (*env)->GetFieldID(env, lpCache->clazz, "lpOutString", "I");
	lpCache->lpOrder = (*env)->GetFieldID(env, lpCache->clazz, "lpOrder", "I");
	lpCache->lpDx = (*env)->GetFieldID(env, lpCache->clazz, "lpDx", "I");
	lpCache->lpCaretPos = (*env)->GetFieldID(env, lpCache->clazz, "lpCaretPos", "I");
	lpCache->lpClass = (*env)->GetFieldID(env, lpCache->clazz, "lpClass", "I");
	lpCache->lpGlyphs = (*env)->GetFieldID(env, lpCache->clazz, "lpGlyphs", "I");
	lpCache->nGlyphs = (*env)->GetFieldID(env, lpCache->clazz, "nGlyphs", "I");
	lpCache->nMaxFit = (*env)->GetFieldID(env, lpCache->clazz, "nMaxFit", "I");
	lpCache->cached = 1;
}

GCP_RESULTS* getGCP_RESULTSFields(JNIEnv *env, jobject lpObject, GCP_RESULTS *lpStruct, PGCP_RESULTS_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheGCP_RESULTSFids(env, lpObject, lpCache);
	lpStruct->lStructSize = (*env)->GetIntField(env, lpObject, lpCache->lStructSize);
	lpStruct->lpOutString = (LPTSTR)(*env)->GetIntField(env, lpObject, lpCache->lpOutString);
	lpStruct->lpOrder = (UINT  *)(*env)->GetIntField(env, lpObject, lpCache->lpOrder);
	lpStruct->lpDx = (int  *)(*env)->GetIntField(env, lpObject, lpCache->lpDx);
	lpStruct->lpCaretPos = (int  *)(*env)->GetIntField(env, lpObject, lpCache->lpCaretPos);
	lpStruct->lpClass = (LPSTR)(*env)->GetIntField(env, lpObject, lpCache->lpClass);
	lpStruct->lpGlyphs = (LPWSTR)(*env)->GetIntField(env, lpObject, lpCache->lpGlyphs);
	lpStruct->nGlyphs = (*env)->GetIntField(env, lpObject, lpCache->nGlyphs);
	lpStruct->nMaxFit = (*env)->GetIntField(env, lpObject, lpCache->nMaxFit);
	return lpStruct;
}

void setGCP_RESULTSFields(JNIEnv *env, jobject lpObject, GCP_RESULTS *lpStruct, PGCP_RESULTS_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheGCP_RESULTSFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->lStructSize, lpStruct->lStructSize);
	(*env)->SetIntField(env, lpObject, lpCache->lpOutString, (jint)lpStruct->lpOutString);
	(*env)->SetIntField(env, lpObject, lpCache->lpOrder, (jint)lpStruct->lpOrder);
	(*env)->SetIntField(env, lpObject, lpCache->lpDx, (jint)lpStruct->lpDx);
	(*env)->SetIntField(env, lpObject, lpCache->lpCaretPos, (jint)lpStruct->lpCaretPos);
	(*env)->SetIntField(env, lpObject, lpCache->lpClass, (jint)lpStruct->lpClass);
	(*env)->SetIntField(env, lpObject, lpCache->lpGlyphs, (jint)lpStruct->lpGlyphs);
	(*env)->SetIntField(env, lpObject, lpCache->nGlyphs, lpStruct->nGlyphs);
	(*env)->SetIntField(env, lpObject, lpCache->nMaxFit, lpStruct->nMaxFit);
}

#endif // _WIN32_WCE

#ifndef _WIN32_WCE

void cacheGRADIENT_RECTFids(JNIEnv *env, jobject lpObject, PGRADIENT_RECT_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->UpperLeft = (*env)->GetFieldID(env, lpCache->clazz, "UpperLeft", "I");
	lpCache->LowerRight = (*env)->GetFieldID(env, lpCache->clazz, "LowerRight", "I");
	lpCache->cached = 1;
}

GRADIENT_RECT* getGRADIENT_RECTFields(JNIEnv *env, jobject lpObject, GRADIENT_RECT *lpStruct, PGRADIENT_RECT_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheGRADIENT_RECTFids(env, lpObject, lpCache);
	lpStruct->UpperLeft = (*env)->GetIntField(env, lpObject, lpCache->UpperLeft);
	lpStruct->LowerRight = (*env)->GetIntField(env, lpObject, lpCache->LowerRight);
	return lpStruct;
}

void setGRADIENT_RECTFields(JNIEnv *env, jobject lpObject, GRADIENT_RECT *lpStruct, PGRADIENT_RECT_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheGRADIENT_RECTFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->UpperLeft, lpStruct->UpperLeft);
	(*env)->SetIntField(env, lpObject, lpCache->LowerRight, lpStruct->LowerRight);
}

#endif // _WIN32_WCE

void cacheHDITEMFids(JNIEnv *env, jobject lpObject, PHDITEM_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->mask = (*env)->GetFieldID(env, lpCache->clazz, "mask", "I");
	lpCache->cxy = (*env)->GetFieldID(env, lpCache->clazz, "cxy", "I");
	lpCache->pszText = (*env)->GetFieldID(env, lpCache->clazz, "pszText", "I");
	lpCache->hbm = (*env)->GetFieldID(env, lpCache->clazz, "hbm", "I");
	lpCache->cchTextMax = (*env)->GetFieldID(env, lpCache->clazz, "cchTextMax", "I");
	lpCache->fmt = (*env)->GetFieldID(env, lpCache->clazz, "fmt", "I");
	lpCache->lParam = (*env)->GetFieldID(env, lpCache->clazz, "lParam", "I");
	lpCache->iImage = (*env)->GetFieldID(env, lpCache->clazz, "iImage", "I");
	lpCache->iOrder = (*env)->GetFieldID(env, lpCache->clazz, "iOrder", "I");
	lpCache->cached = 1;
}

HDITEM* getHDITEMFields(JNIEnv *env, jobject lpObject, HDITEM *lpStruct, PHDITEM_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheHDITEMFids(env, lpObject, lpCache);
	lpStruct->mask = (*env)->GetIntField(env, lpObject, lpCache->mask);
	lpStruct->cxy = (*env)->GetIntField(env, lpObject, lpCache->cxy);
	lpStruct->pszText = (LPTSTR)(*env)->GetIntField(env, lpObject, lpCache->pszText);
	lpStruct->hbm = (HBITMAP)(*env)->GetIntField(env, lpObject, lpCache->hbm);
	lpStruct->cchTextMax = (*env)->GetIntField(env, lpObject, lpCache->cchTextMax);
	lpStruct->fmt = (*env)->GetIntField(env, lpObject, lpCache->fmt);
	lpStruct->lParam = (*env)->GetIntField(env, lpObject, lpCache->lParam);
	lpStruct->iImage = (*env)->GetIntField(env, lpObject, lpCache->iImage);
	lpStruct->iOrder = (*env)->GetIntField(env, lpObject, lpCache->iOrder);
	return lpStruct;
}

void setHDITEMFields(JNIEnv *env, jobject lpObject, HDITEM *lpStruct, PHDITEM_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheHDITEMFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->mask, lpStruct->mask);
	(*env)->SetIntField(env, lpObject, lpCache->cxy, lpStruct->cxy);
	(*env)->SetIntField(env, lpObject, lpCache->pszText, (jint)lpStruct->pszText);
	(*env)->SetIntField(env, lpObject, lpCache->hbm, (jint)lpStruct->hbm);
	(*env)->SetIntField(env, lpObject, lpCache->cchTextMax, lpStruct->cchTextMax);
	(*env)->SetIntField(env, lpObject, lpCache->fmt, lpStruct->fmt);
	(*env)->SetIntField(env, lpObject, lpCache->lParam, lpStruct->lParam);
	(*env)->SetIntField(env, lpObject, lpCache->iImage, lpStruct->iImage);
	(*env)->SetIntField(env, lpObject, lpCache->iOrder, lpStruct->iOrder);
}

#ifndef _WIN32_WCE

void cacheHELPINFOFids(JNIEnv *env, jobject lpObject, PHELPINFO_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->cbSize = (*env)->GetFieldID(env, lpCache->clazz, "cbSize", "I");
	lpCache->iContextType = (*env)->GetFieldID(env, lpCache->clazz, "iContextType", "I");
	lpCache->iCtrlId = (*env)->GetFieldID(env, lpCache->clazz, "iCtrlId", "I");
	lpCache->hItemHandle = (*env)->GetFieldID(env, lpCache->clazz, "hItemHandle", "I");
	lpCache->dwContextId = (*env)->GetFieldID(env, lpCache->clazz, "dwContextId", "I");
	lpCache->x = (*env)->GetFieldID(env, lpCache->clazz, "x", "I");
	lpCache->y = (*env)->GetFieldID(env, lpCache->clazz, "y", "I");
	lpCache->cached = 1;
}

HELPINFO* getHELPINFOFields(JNIEnv *env, jobject lpObject, HELPINFO *lpStruct, PHELPINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheHELPINFOFids(env, lpObject, lpCache);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, lpCache->cbSize);
	lpStruct->iContextType = (*env)->GetIntField(env, lpObject, lpCache->iContextType);
	lpStruct->iCtrlId = (*env)->GetIntField(env, lpObject, lpCache->iCtrlId);
	lpStruct->hItemHandle = (HANDLE)(*env)->GetIntField(env, lpObject, lpCache->hItemHandle);
	lpStruct->dwContextId = (*env)->GetIntField(env, lpObject, lpCache->dwContextId);
	lpStruct->MousePos.x = (*env)->GetIntField(env, lpObject, lpCache->x);
	lpStruct->MousePos.y = (*env)->GetIntField(env, lpObject, lpCache->y);
	return lpStruct;
}

void setHELPINFOFields(JNIEnv *env, jobject lpObject, HELPINFO *lpStruct, PHELPINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheHELPINFOFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->cbSize, lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, lpCache->iContextType, lpStruct->iContextType);
	(*env)->SetIntField(env, lpObject, lpCache->iCtrlId, lpStruct->iCtrlId);
	(*env)->SetIntField(env, lpObject, lpCache->hItemHandle, (jint)lpStruct->hItemHandle);
	(*env)->SetIntField(env, lpObject, lpCache->dwContextId, lpStruct->dwContextId);
	(*env)->SetIntField(env, lpObject, lpCache->x, lpStruct->MousePos.x);
	(*env)->SetIntField(env, lpObject, lpCache->y, lpStruct->MousePos.y);
}

#endif // _WIN32_WCE

void cacheICONINFOFids(JNIEnv *env, jobject lpObject, PICONINFO_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->fIcon = (*env)->GetFieldID(env, lpCache->clazz, "fIcon", "Z");
	lpCache->xHotspot = (*env)->GetFieldID(env, lpCache->clazz, "xHotspot", "I");
	lpCache->yHotspot = (*env)->GetFieldID(env, lpCache->clazz, "yHotspot", "I");
	lpCache->hbmMask = (*env)->GetFieldID(env, lpCache->clazz, "hbmMask", "I");
	lpCache->hbmColor = (*env)->GetFieldID(env, lpCache->clazz, "hbmColor", "I");
	lpCache->cached = 1;
}

ICONINFO* getICONINFOFields(JNIEnv *env, jobject lpObject, ICONINFO *lpStruct, PICONINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheICONINFOFids(env, lpObject, lpCache);
	lpStruct->fIcon = (*env)->GetBooleanField(env, lpObject, lpCache->fIcon);
	lpStruct->xHotspot = (*env)->GetIntField(env, lpObject, lpCache->xHotspot);
	lpStruct->yHotspot = (*env)->GetIntField(env, lpObject, lpCache->yHotspot);
	lpStruct->hbmMask = (HBITMAP)(*env)->GetIntField(env, lpObject, lpCache->hbmMask);
	lpStruct->hbmColor = (HBITMAP)(*env)->GetIntField(env, lpObject, lpCache->hbmColor);
	return lpStruct;
}

void setICONINFOFields(JNIEnv *env, jobject lpObject, ICONINFO *lpStruct, PICONINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheICONINFOFids(env, lpObject, lpCache);
	(*env)->SetBooleanField(env, lpObject, lpCache->fIcon, (jboolean)lpStruct->fIcon);
	(*env)->SetIntField(env, lpObject, lpCache->xHotspot, lpStruct->xHotspot);
	(*env)->SetIntField(env, lpObject, lpCache->yHotspot, lpStruct->yHotspot);
	(*env)->SetIntField(env, lpObject, lpCache->hbmMask, (jint)lpStruct->hbmMask);
	(*env)->SetIntField(env, lpObject, lpCache->hbmColor, (jint)lpStruct->hbmColor);
}

void cacheINITCOMMONCONTROLSEXFids(JNIEnv *env, jobject lpObject, PINITCOMMONCONTROLSEX_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->dwSize = (*env)->GetFieldID(env, lpCache->clazz, "dwSize", "I");
	lpCache->dwICC = (*env)->GetFieldID(env, lpCache->clazz, "dwICC", "I");
	lpCache->cached = 1;
}

INITCOMMONCONTROLSEX* getINITCOMMONCONTROLSEXFields(JNIEnv *env, jobject lpObject, INITCOMMONCONTROLSEX *lpStruct, PINITCOMMONCONTROLSEX_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheINITCOMMONCONTROLSEXFids(env, lpObject, lpCache);
	lpStruct->dwSize = (*env)->GetIntField(env, lpObject, lpCache->dwSize);
	lpStruct->dwICC = (*env)->GetIntField(env, lpObject, lpCache->dwICC);
	return lpStruct;
}

void setINITCOMMONCONTROLSEXFields(JNIEnv *env, jobject lpObject, INITCOMMONCONTROLSEX *lpStruct, PINITCOMMONCONTROLSEX_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheINITCOMMONCONTROLSEXFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->dwSize, lpStruct->dwSize);
	(*env)->SetIntField(env, lpObject, lpCache->dwICC, lpStruct->dwICC);
}

void cacheLOGBRUSHFids(JNIEnv *env, jobject lpObject, PLOGBRUSH_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->lbStyle = (*env)->GetFieldID(env, lpCache->clazz, "lbStyle", "I");
	lpCache->lbColor = (*env)->GetFieldID(env, lpCache->clazz, "lbColor", "I");
	lpCache->lbHatch = (*env)->GetFieldID(env, lpCache->clazz, "lbHatch", "I");
	lpCache->cached = 1;
}

LOGBRUSH* getLOGBRUSHFields(JNIEnv *env, jobject lpObject, LOGBRUSH *lpStruct, PLOGBRUSH_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheLOGBRUSHFids(env, lpObject, lpCache);
	lpStruct->lbStyle = (*env)->GetIntField(env, lpObject, lpCache->lbStyle);
	lpStruct->lbColor = (*env)->GetIntField(env, lpObject, lpCache->lbColor);
	lpStruct->lbHatch = (*env)->GetIntField(env, lpObject, lpCache->lbHatch);
	return lpStruct;
}

void setLOGBRUSHFields(JNIEnv *env, jobject lpObject, LOGBRUSH *lpStruct, PLOGBRUSH_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheLOGBRUSHFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->lbStyle, lpStruct->lbStyle);
	(*env)->SetIntField(env, lpObject, lpCache->lbColor, lpStruct->lbColor);
	(*env)->SetIntField(env, lpObject, lpCache->lbHatch, lpStruct->lbHatch);
}

void cacheLOGFONTFids(JNIEnv *env, jobject lpObject, PLOGFONT_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->lfHeight = (*env)->GetFieldID(env, lpCache->clazz, "lfHeight", "I");
	lpCache->lfWidth = (*env)->GetFieldID(env, lpCache->clazz, "lfWidth", "I");
	lpCache->lfEscapement = (*env)->GetFieldID(env, lpCache->clazz, "lfEscapement", "I");
	lpCache->lfOrientation = (*env)->GetFieldID(env, lpCache->clazz, "lfOrientation", "I");
	lpCache->lfWeight = (*env)->GetFieldID(env, lpCache->clazz, "lfWeight", "I");
	lpCache->lfItalic = (*env)->GetFieldID(env, lpCache->clazz, "lfItalic", "B");
	lpCache->lfUnderline = (*env)->GetFieldID(env, lpCache->clazz, "lfUnderline", "B");
	lpCache->lfStrikeOut = (*env)->GetFieldID(env, lpCache->clazz, "lfStrikeOut", "B");
	lpCache->lfCharSet = (*env)->GetFieldID(env, lpCache->clazz, "lfCharSet", "B");
	lpCache->lfOutPrecision = (*env)->GetFieldID(env, lpCache->clazz, "lfOutPrecision", "B");
	lpCache->lfClipPrecision = (*env)->GetFieldID(env, lpCache->clazz, "lfClipPrecision", "B");
	lpCache->lfQuality = (*env)->GetFieldID(env, lpCache->clazz, "lfQuality", "B");
	lpCache->lfPitchAndFamily = (*env)->GetFieldID(env, lpCache->clazz, "lfPitchAndFamily", "B");
	lpCache->lfFaceName0 = (*env)->GetFieldID(env, lpCache->clazz, "lfFaceName0", "C");
	lpCache->lfFaceName1 = (*env)->GetFieldID(env, lpCache->clazz, "lfFaceName1", "C");
	lpCache->lfFaceName2 = (*env)->GetFieldID(env, lpCache->clazz, "lfFaceName2", "C");
	lpCache->lfFaceName3 = (*env)->GetFieldID(env, lpCache->clazz, "lfFaceName3", "C");
	lpCache->lfFaceName4 = (*env)->GetFieldID(env, lpCache->clazz, "lfFaceName4", "C");
	lpCache->lfFaceName5 = (*env)->GetFieldID(env, lpCache->clazz, "lfFaceName5", "C");
	lpCache->lfFaceName6 = (*env)->GetFieldID(env, lpCache->clazz, "lfFaceName6", "C");
	lpCache->lfFaceName7 = (*env)->GetFieldID(env, lpCache->clazz, "lfFaceName7", "C");
	lpCache->lfFaceName8 = (*env)->GetFieldID(env, lpCache->clazz, "lfFaceName8", "C");
	lpCache->lfFaceName9 = (*env)->GetFieldID(env, lpCache->clazz, "lfFaceName9", "C");
	lpCache->lfFaceName10 = (*env)->GetFieldID(env, lpCache->clazz, "lfFaceName10", "C");
	lpCache->lfFaceName11 = (*env)->GetFieldID(env, lpCache->clazz, "lfFaceName11", "C");
	lpCache->lfFaceName12 = (*env)->GetFieldID(env, lpCache->clazz, "lfFaceName12", "C");
	lpCache->lfFaceName13 = (*env)->GetFieldID(env, lpCache->clazz, "lfFaceName13", "C");
	lpCache->lfFaceName14 = (*env)->GetFieldID(env, lpCache->clazz, "lfFaceName14", "C");
	lpCache->lfFaceName15 = (*env)->GetFieldID(env, lpCache->clazz, "lfFaceName15", "C");
	lpCache->lfFaceName16 = (*env)->GetFieldID(env, lpCache->clazz, "lfFaceName16", "C");
	lpCache->lfFaceName17 = (*env)->GetFieldID(env, lpCache->clazz, "lfFaceName17", "C");
	lpCache->lfFaceName18 = (*env)->GetFieldID(env, lpCache->clazz, "lfFaceName18", "C");
	lpCache->lfFaceName19 = (*env)->GetFieldID(env, lpCache->clazz, "lfFaceName19", "C");
	lpCache->lfFaceName20 = (*env)->GetFieldID(env, lpCache->clazz, "lfFaceName20", "C");
	lpCache->lfFaceName21 = (*env)->GetFieldID(env, lpCache->clazz, "lfFaceName21", "C");
	lpCache->lfFaceName22 = (*env)->GetFieldID(env, lpCache->clazz, "lfFaceName22", "C");
	lpCache->lfFaceName23 = (*env)->GetFieldID(env, lpCache->clazz, "lfFaceName23", "C");
	lpCache->lfFaceName24 = (*env)->GetFieldID(env, lpCache->clazz, "lfFaceName24", "C");
	lpCache->lfFaceName25 = (*env)->GetFieldID(env, lpCache->clazz, "lfFaceName25", "C");
	lpCache->lfFaceName26 = (*env)->GetFieldID(env, lpCache->clazz, "lfFaceName26", "C");
	lpCache->lfFaceName27 = (*env)->GetFieldID(env, lpCache->clazz, "lfFaceName27", "C");
	lpCache->lfFaceName28 = (*env)->GetFieldID(env, lpCache->clazz, "lfFaceName28", "C");
	lpCache->lfFaceName29 = (*env)->GetFieldID(env, lpCache->clazz, "lfFaceName29", "C");
	lpCache->lfFaceName30 = (*env)->GetFieldID(env, lpCache->clazz, "lfFaceName30", "C");
	lpCache->lfFaceName31 = (*env)->GetFieldID(env, lpCache->clazz, "lfFaceName31", "C");
	lpCache->cached = 1;
}

#ifndef _WIN32_WCE

LOGFONTA* getLOGFONTAFields(JNIEnv *env, jobject lpObject, LOGFONTA *lpStruct, LOGFONT_FID_CACHE *lpCache)
{
	if (!lpCache->cached) cacheLOGFONTFids(env, lpObject, lpCache);
    lpStruct->lfHeight = (*env)->GetIntField(env,lpObject,lpCache->lfHeight);
    lpStruct->lfWidth = (*env)->GetIntField(env,lpObject,lpCache->lfWidth);
    lpStruct->lfEscapement = (*env)->GetIntField(env,lpObject,lpCache->lfEscapement);
    lpStruct->lfOrientation = (*env)->GetIntField(env,lpObject,lpCache->lfOrientation);
    lpStruct->lfWeight = (*env)->GetIntField(env,lpObject,lpCache->lfWeight);
    lpStruct->lfItalic = (*env)->GetByteField(env,lpObject,lpCache->lfItalic);
    lpStruct->lfUnderline = (*env)->GetByteField(env,lpObject,lpCache->lfUnderline);
    lpStruct->lfStrikeOut = (*env)->GetByteField(env,lpObject,lpCache->lfStrikeOut);
    lpStruct->lfCharSet = (*env)->GetByteField(env,lpObject,lpCache->lfCharSet);
    lpStruct->lfOutPrecision = (*env)->GetByteField(env,lpObject,lpCache->lfOutPrecision);
    lpStruct->lfClipPrecision = (*env)->GetByteField(env,lpObject,lpCache->lfClipPrecision);
    lpStruct->lfQuality = (*env)->GetByteField(env,lpObject,lpCache->lfQuality);
    lpStruct->lfPitchAndFamily = (*env)->GetByteField(env,lpObject,lpCache->lfPitchAndFamily);
    {
    WCHAR lfFaceName [32];
    lfFaceName[0] = (*env)->GetCharField(env,lpObject,lpCache->lfFaceName0);
    lfFaceName[1] = (*env)->GetCharField(env,lpObject,lpCache->lfFaceName1);
    lfFaceName[2] = (*env)->GetCharField(env,lpObject,lpCache->lfFaceName2);
    lfFaceName[3] = (*env)->GetCharField(env,lpObject,lpCache->lfFaceName3);
    lfFaceName[4] = (*env)->GetCharField(env,lpObject,lpCache->lfFaceName4);
    lfFaceName[5] = (*env)->GetCharField(env,lpObject,lpCache->lfFaceName5);
    lfFaceName[6] = (*env)->GetCharField(env,lpObject,lpCache->lfFaceName6);
    lfFaceName[7] = (*env)->GetCharField(env,lpObject,lpCache->lfFaceName7);
    lfFaceName[8] = (*env)->GetCharField(env,lpObject,lpCache->lfFaceName8);
    lfFaceName[9] = (*env)->GetCharField(env,lpObject,lpCache->lfFaceName9);
    lfFaceName[10] = (*env)->GetCharField(env,lpObject,lpCache->lfFaceName10);
    lfFaceName[11] = (*env)->GetCharField(env,lpObject,lpCache->lfFaceName11);
    lfFaceName[12] = (*env)->GetCharField(env,lpObject,lpCache->lfFaceName12);
    lfFaceName[13] = (*env)->GetCharField(env,lpObject,lpCache->lfFaceName13);
    lfFaceName[14] = (*env)->GetCharField(env,lpObject,lpCache->lfFaceName14);
    lfFaceName[15] = (*env)->GetCharField(env,lpObject,lpCache->lfFaceName15);
    lfFaceName[16] = (*env)->GetCharField(env,lpObject,lpCache->lfFaceName16);
    lfFaceName[17] = (*env)->GetCharField(env,lpObject,lpCache->lfFaceName17);
    lfFaceName[18] = (*env)->GetCharField(env,lpObject,lpCache->lfFaceName18);
    lfFaceName[19] = (*env)->GetCharField(env,lpObject,lpCache->lfFaceName19);
    lfFaceName[20] = (*env)->GetCharField(env,lpObject,lpCache->lfFaceName20);
    lfFaceName[21] = (*env)->GetCharField(env,lpObject,lpCache->lfFaceName21);
    lfFaceName[22] = (*env)->GetCharField(env,lpObject,lpCache->lfFaceName22);
    lfFaceName[23] = (*env)->GetCharField(env,lpObject,lpCache->lfFaceName23);
    lfFaceName[24] = (*env)->GetCharField(env,lpObject,lpCache->lfFaceName24);
    lfFaceName[25] = (*env)->GetCharField(env,lpObject,lpCache->lfFaceName25);
    lfFaceName[26] = (*env)->GetCharField(env,lpObject,lpCache->lfFaceName26);
    lfFaceName[27] = (*env)->GetCharField(env,lpObject,lpCache->lfFaceName27);
    lfFaceName[28] = (*env)->GetCharField(env,lpObject,lpCache->lfFaceName28);
    lfFaceName[29] = (*env)->GetCharField(env,lpObject,lpCache->lfFaceName29);
    lfFaceName[30] = (*env)->GetCharField(env,lpObject,lpCache->lfFaceName30);
    lfFaceName[31] = (*env)->GetCharField(env,lpObject,lpCache->lfFaceName31);
   	WideCharToMultiByte (CP_ACP, 0, lfFaceName, 32, lpStruct->lfFaceName, 32, NULL, NULL);
    }
    return lpStruct;
}

void setLOGFONTAFields(JNIEnv *env, jobject lpObject, LOGFONTA *lpStruct, LOGFONT_FID_CACHE *lpCache)
{
	if (!lpCache->cached) cacheLOGFONTFids(env, lpObject, lpCache);
    (*env)->SetIntField(env,lpObject,lpCache->lfHeight, lpStruct->lfHeight);
    (*env)->SetIntField(env,lpObject,lpCache->lfWidth, lpStruct->lfWidth);
    (*env)->SetIntField(env,lpObject,lpCache->lfEscapement, lpStruct->lfEscapement);
    (*env)->SetIntField(env,lpObject,lpCache->lfOrientation, lpStruct->lfOrientation);
    (*env)->SetIntField(env,lpObject,lpCache->lfWeight, lpStruct->lfWeight);
    (*env)->SetByteField(env,lpObject,lpCache->lfItalic, lpStruct->lfItalic);
    (*env)->SetByteField(env,lpObject,lpCache->lfUnderline, lpStruct->lfUnderline);
    (*env)->SetByteField(env,lpObject,lpCache->lfStrikeOut, lpStruct->lfStrikeOut);
    (*env)->SetByteField(env,lpObject,lpCache->lfCharSet, lpStruct->lfCharSet);
    (*env)->SetByteField(env,lpObject,lpCache->lfOutPrecision, lpStruct->lfOutPrecision);
    (*env)->SetByteField(env,lpObject,lpCache->lfClipPrecision, lpStruct->lfClipPrecision);
    (*env)->SetByteField(env,lpObject,lpCache->lfQuality, lpStruct->lfQuality);
    (*env)->SetByteField(env,lpObject,lpCache->lfPitchAndFamily, lpStruct->lfPitchAndFamily);
    {
    WCHAR lfFaceName [32] = {0};
	MultiByteToWideChar (CP_ACP, MB_PRECOMPOSED, lpStruct->lfFaceName, -1, lfFaceName, 32);
    (*env)->SetCharField(env,lpObject,lpCache->lfFaceName0, lfFaceName[0]);
    (*env)->SetCharField(env,lpObject,lpCache->lfFaceName1, lfFaceName[1]);
    (*env)->SetCharField(env,lpObject,lpCache->lfFaceName2, lfFaceName[2]);
    (*env)->SetCharField(env,lpObject,lpCache->lfFaceName3, lfFaceName[3]);
    (*env)->SetCharField(env,lpObject,lpCache->lfFaceName4, lfFaceName[4]);
    (*env)->SetCharField(env,lpObject,lpCache->lfFaceName5, lfFaceName[5]);
    (*env)->SetCharField(env,lpObject,lpCache->lfFaceName6, lfFaceName[6]);
    (*env)->SetCharField(env,lpObject,lpCache->lfFaceName7, lfFaceName[7]);
    (*env)->SetCharField(env,lpObject,lpCache->lfFaceName8, lfFaceName[8]);
    (*env)->SetCharField(env,lpObject,lpCache->lfFaceName9, lfFaceName[9]);
    (*env)->SetCharField(env,lpObject,lpCache->lfFaceName10, lfFaceName[10]);
    (*env)->SetCharField(env,lpObject,lpCache->lfFaceName11, lfFaceName[11]);
    (*env)->SetCharField(env,lpObject,lpCache->lfFaceName12, lfFaceName[12]);
    (*env)->SetCharField(env,lpObject,lpCache->lfFaceName13, lfFaceName[13]);
    (*env)->SetCharField(env,lpObject,lpCache->lfFaceName14, lfFaceName[14]);
    (*env)->SetCharField(env,lpObject,lpCache->lfFaceName15, lfFaceName[15]);
    (*env)->SetCharField(env,lpObject,lpCache->lfFaceName16, lfFaceName[16]);
    (*env)->SetCharField(env,lpObject,lpCache->lfFaceName17, lfFaceName[17]);
    (*env)->SetCharField(env,lpObject,lpCache->lfFaceName18, lfFaceName[18]);
    (*env)->SetCharField(env,lpObject,lpCache->lfFaceName19, lfFaceName[19]);
    (*env)->SetCharField(env,lpObject,lpCache->lfFaceName20, lfFaceName[20]);
    (*env)->SetCharField(env,lpObject,lpCache->lfFaceName21, lfFaceName[21]);
    (*env)->SetCharField(env,lpObject,lpCache->lfFaceName22, lfFaceName[22]);
    (*env)->SetCharField(env,lpObject,lpCache->lfFaceName23, lfFaceName[23]);
    (*env)->SetCharField(env,lpObject,lpCache->lfFaceName24, lfFaceName[24]);
    (*env)->SetCharField(env,lpObject,lpCache->lfFaceName25, lfFaceName[25]);
    (*env)->SetCharField(env,lpObject,lpCache->lfFaceName26, lfFaceName[26]);
    (*env)->SetCharField(env,lpObject,lpCache->lfFaceName27, lfFaceName[27]);
    (*env)->SetCharField(env,lpObject,lpCache->lfFaceName28, lfFaceName[28]);
    (*env)->SetCharField(env,lpObject,lpCache->lfFaceName29, lfFaceName[29]);
    (*env)->SetCharField(env,lpObject,lpCache->lfFaceName30, lfFaceName[30]);
    (*env)->SetCharField(env,lpObject,lpCache->lfFaceName31, lfFaceName[31]);
    }
}

#endif // _WIN32_WCE

LOGFONTW* getLOGFONTWFields(JNIEnv *env, jobject lpObject, LOGFONTW *lpStruct, PLOGFONT_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheLOGFONTFids(env, lpObject, lpCache);
	lpStruct->lfHeight = (*env)->GetIntField(env, lpObject, lpCache->lfHeight);
	lpStruct->lfWidth = (*env)->GetIntField(env, lpObject, lpCache->lfWidth);
	lpStruct->lfEscapement = (*env)->GetIntField(env, lpObject, lpCache->lfEscapement);
	lpStruct->lfOrientation = (*env)->GetIntField(env, lpObject, lpCache->lfOrientation);
	lpStruct->lfWeight = (*env)->GetIntField(env, lpObject, lpCache->lfWeight);
	lpStruct->lfItalic = (*env)->GetByteField(env, lpObject, lpCache->lfItalic);
	lpStruct->lfUnderline = (*env)->GetByteField(env, lpObject, lpCache->lfUnderline);
	lpStruct->lfStrikeOut = (*env)->GetByteField(env, lpObject, lpCache->lfStrikeOut);
	lpStruct->lfCharSet = (*env)->GetByteField(env, lpObject, lpCache->lfCharSet);
	lpStruct->lfOutPrecision = (*env)->GetByteField(env, lpObject, lpCache->lfOutPrecision);
	lpStruct->lfClipPrecision = (*env)->GetByteField(env, lpObject, lpCache->lfClipPrecision);
	lpStruct->lfQuality = (*env)->GetByteField(env, lpObject, lpCache->lfQuality);
	lpStruct->lfPitchAndFamily = (*env)->GetByteField(env, lpObject, lpCache->lfPitchAndFamily);
	lpStruct->lfFaceName[0] = (*env)->GetCharField(env, lpObject, lpCache->lfFaceName0);
	lpStruct->lfFaceName[1] = (*env)->GetCharField(env, lpObject, lpCache->lfFaceName1);
	lpStruct->lfFaceName[2] = (*env)->GetCharField(env, lpObject, lpCache->lfFaceName2);
	lpStruct->lfFaceName[3] = (*env)->GetCharField(env, lpObject, lpCache->lfFaceName3);
	lpStruct->lfFaceName[4] = (*env)->GetCharField(env, lpObject, lpCache->lfFaceName4);
	lpStruct->lfFaceName[5] = (*env)->GetCharField(env, lpObject, lpCache->lfFaceName5);
	lpStruct->lfFaceName[6] = (*env)->GetCharField(env, lpObject, lpCache->lfFaceName6);
	lpStruct->lfFaceName[7] = (*env)->GetCharField(env, lpObject, lpCache->lfFaceName7);
	lpStruct->lfFaceName[8] = (*env)->GetCharField(env, lpObject, lpCache->lfFaceName8);
	lpStruct->lfFaceName[9] = (*env)->GetCharField(env, lpObject, lpCache->lfFaceName9);
	lpStruct->lfFaceName[10] = (*env)->GetCharField(env, lpObject, lpCache->lfFaceName10);
	lpStruct->lfFaceName[11] = (*env)->GetCharField(env, lpObject, lpCache->lfFaceName11);
	lpStruct->lfFaceName[12] = (*env)->GetCharField(env, lpObject, lpCache->lfFaceName12);
	lpStruct->lfFaceName[13] = (*env)->GetCharField(env, lpObject, lpCache->lfFaceName13);
	lpStruct->lfFaceName[14] = (*env)->GetCharField(env, lpObject, lpCache->lfFaceName14);
	lpStruct->lfFaceName[15] = (*env)->GetCharField(env, lpObject, lpCache->lfFaceName15);
	lpStruct->lfFaceName[16] = (*env)->GetCharField(env, lpObject, lpCache->lfFaceName16);
	lpStruct->lfFaceName[17] = (*env)->GetCharField(env, lpObject, lpCache->lfFaceName17);
	lpStruct->lfFaceName[18] = (*env)->GetCharField(env, lpObject, lpCache->lfFaceName18);
	lpStruct->lfFaceName[19] = (*env)->GetCharField(env, lpObject, lpCache->lfFaceName19);
	lpStruct->lfFaceName[20] = (*env)->GetCharField(env, lpObject, lpCache->lfFaceName20);
	lpStruct->lfFaceName[21] = (*env)->GetCharField(env, lpObject, lpCache->lfFaceName21);
	lpStruct->lfFaceName[22] = (*env)->GetCharField(env, lpObject, lpCache->lfFaceName22);
	lpStruct->lfFaceName[23] = (*env)->GetCharField(env, lpObject, lpCache->lfFaceName23);
	lpStruct->lfFaceName[24] = (*env)->GetCharField(env, lpObject, lpCache->lfFaceName24);
	lpStruct->lfFaceName[25] = (*env)->GetCharField(env, lpObject, lpCache->lfFaceName25);
	lpStruct->lfFaceName[26] = (*env)->GetCharField(env, lpObject, lpCache->lfFaceName26);
	lpStruct->lfFaceName[27] = (*env)->GetCharField(env, lpObject, lpCache->lfFaceName27);
	lpStruct->lfFaceName[28] = (*env)->GetCharField(env, lpObject, lpCache->lfFaceName28);
	lpStruct->lfFaceName[29] = (*env)->GetCharField(env, lpObject, lpCache->lfFaceName29);
	lpStruct->lfFaceName[30] = (*env)->GetCharField(env, lpObject, lpCache->lfFaceName30);
	lpStruct->lfFaceName[31] = (*env)->GetCharField(env, lpObject, lpCache->lfFaceName31);
	return lpStruct;
}

void setLOGFONTWFields(JNIEnv *env, jobject lpObject, LOGFONTW *lpStruct, PLOGFONT_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheLOGFONTFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->lfHeight, lpStruct->lfHeight);
	(*env)->SetIntField(env, lpObject, lpCache->lfWidth, lpStruct->lfWidth);
	(*env)->SetIntField(env, lpObject, lpCache->lfEscapement, lpStruct->lfEscapement);
	(*env)->SetIntField(env, lpObject, lpCache->lfOrientation, lpStruct->lfOrientation);
	(*env)->SetIntField(env, lpObject, lpCache->lfWeight, lpStruct->lfWeight);
	(*env)->SetByteField(env, lpObject, lpCache->lfItalic, lpStruct->lfItalic);
	(*env)->SetByteField(env, lpObject, lpCache->lfUnderline, lpStruct->lfUnderline);
	(*env)->SetByteField(env, lpObject, lpCache->lfStrikeOut, lpStruct->lfStrikeOut);
	(*env)->SetByteField(env, lpObject, lpCache->lfCharSet, lpStruct->lfCharSet);
	(*env)->SetByteField(env, lpObject, lpCache->lfOutPrecision, lpStruct->lfOutPrecision);
	(*env)->SetByteField(env, lpObject, lpCache->lfClipPrecision, lpStruct->lfClipPrecision);
	(*env)->SetByteField(env, lpObject, lpCache->lfQuality, lpStruct->lfQuality);
	(*env)->SetByteField(env, lpObject, lpCache->lfPitchAndFamily, lpStruct->lfPitchAndFamily);
	(*env)->SetCharField(env, lpObject, lpCache->lfFaceName0, lpStruct->lfFaceName[0]);
	(*env)->SetCharField(env, lpObject, lpCache->lfFaceName1, lpStruct->lfFaceName[1]);
	(*env)->SetCharField(env, lpObject, lpCache->lfFaceName2, lpStruct->lfFaceName[2]);
	(*env)->SetCharField(env, lpObject, lpCache->lfFaceName3, lpStruct->lfFaceName[3]);
	(*env)->SetCharField(env, lpObject, lpCache->lfFaceName4, lpStruct->lfFaceName[4]);
	(*env)->SetCharField(env, lpObject, lpCache->lfFaceName5, lpStruct->lfFaceName[5]);
	(*env)->SetCharField(env, lpObject, lpCache->lfFaceName6, lpStruct->lfFaceName[6]);
	(*env)->SetCharField(env, lpObject, lpCache->lfFaceName7, lpStruct->lfFaceName[7]);
	(*env)->SetCharField(env, lpObject, lpCache->lfFaceName8, lpStruct->lfFaceName[8]);
	(*env)->SetCharField(env, lpObject, lpCache->lfFaceName9, lpStruct->lfFaceName[9]);
	(*env)->SetCharField(env, lpObject, lpCache->lfFaceName10, lpStruct->lfFaceName[10]);
	(*env)->SetCharField(env, lpObject, lpCache->lfFaceName11, lpStruct->lfFaceName[11]);
	(*env)->SetCharField(env, lpObject, lpCache->lfFaceName12, lpStruct->lfFaceName[12]);
	(*env)->SetCharField(env, lpObject, lpCache->lfFaceName13, lpStruct->lfFaceName[13]);
	(*env)->SetCharField(env, lpObject, lpCache->lfFaceName14, lpStruct->lfFaceName[14]);
	(*env)->SetCharField(env, lpObject, lpCache->lfFaceName15, lpStruct->lfFaceName[15]);
	(*env)->SetCharField(env, lpObject, lpCache->lfFaceName16, lpStruct->lfFaceName[16]);
	(*env)->SetCharField(env, lpObject, lpCache->lfFaceName17, lpStruct->lfFaceName[17]);
	(*env)->SetCharField(env, lpObject, lpCache->lfFaceName18, lpStruct->lfFaceName[18]);
	(*env)->SetCharField(env, lpObject, lpCache->lfFaceName19, lpStruct->lfFaceName[19]);
	(*env)->SetCharField(env, lpObject, lpCache->lfFaceName20, lpStruct->lfFaceName[20]);
	(*env)->SetCharField(env, lpObject, lpCache->lfFaceName21, lpStruct->lfFaceName[21]);
	(*env)->SetCharField(env, lpObject, lpCache->lfFaceName22, lpStruct->lfFaceName[22]);
	(*env)->SetCharField(env, lpObject, lpCache->lfFaceName23, lpStruct->lfFaceName[23]);
	(*env)->SetCharField(env, lpObject, lpCache->lfFaceName24, lpStruct->lfFaceName[24]);
	(*env)->SetCharField(env, lpObject, lpCache->lfFaceName25, lpStruct->lfFaceName[25]);
	(*env)->SetCharField(env, lpObject, lpCache->lfFaceName26, lpStruct->lfFaceName[26]);
	(*env)->SetCharField(env, lpObject, lpCache->lfFaceName27, lpStruct->lfFaceName[27]);
	(*env)->SetCharField(env, lpObject, lpCache->lfFaceName28, lpStruct->lfFaceName[28]);
	(*env)->SetCharField(env, lpObject, lpCache->lfFaceName29, lpStruct->lfFaceName[29]);
	(*env)->SetCharField(env, lpObject, lpCache->lfFaceName30, lpStruct->lfFaceName[30]);
	(*env)->SetCharField(env, lpObject, lpCache->lfFaceName31, lpStruct->lfFaceName[31]);
}

void cacheLOGPENFids(JNIEnv *env, jobject lpObject, PLOGPEN_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->lopnStyle = (*env)->GetFieldID(env, lpCache->clazz, "lopnStyle", "I");
	lpCache->x = (*env)->GetFieldID(env, lpCache->clazz, "x", "I");
	lpCache->y = (*env)->GetFieldID(env, lpCache->clazz, "y", "I");
	lpCache->lopnColor = (*env)->GetFieldID(env, lpCache->clazz, "lopnColor", "I");
	lpCache->cached = 1;
}

LOGPEN* getLOGPENFields(JNIEnv *env, jobject lpObject, LOGPEN *lpStruct, PLOGPEN_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheLOGPENFids(env, lpObject, lpCache);
	lpStruct->lopnStyle = (*env)->GetIntField(env, lpObject, lpCache->lopnStyle);
	lpStruct->lopnWidth.x = (*env)->GetIntField(env, lpObject, lpCache->x);
	lpStruct->lopnWidth.y = (*env)->GetIntField(env, lpObject, lpCache->y);
	lpStruct->lopnColor = (*env)->GetIntField(env, lpObject, lpCache->lopnColor);
	return lpStruct;
}

void setLOGPENFields(JNIEnv *env, jobject lpObject, LOGPEN *lpStruct, PLOGPEN_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheLOGPENFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->lopnStyle, lpStruct->lopnStyle);
	(*env)->SetIntField(env, lpObject, lpCache->x, lpStruct->lopnWidth.x);
	(*env)->SetIntField(env, lpObject, lpCache->y, lpStruct->lopnWidth.y);
	(*env)->SetIntField(env, lpObject, lpCache->lopnColor, lpStruct->lopnColor);
}

void cacheLVCOLUMNFids(JNIEnv *env, jobject lpObject, PLVCOLUMN_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->mask = (*env)->GetFieldID(env, lpCache->clazz, "mask", "I");
	lpCache->fmt = (*env)->GetFieldID(env, lpCache->clazz, "fmt", "I");
	lpCache->cx = (*env)->GetFieldID(env, lpCache->clazz, "cx", "I");
	lpCache->pszText = (*env)->GetFieldID(env, lpCache->clazz, "pszText", "I");
	lpCache->cchTextMax = (*env)->GetFieldID(env, lpCache->clazz, "cchTextMax", "I");
	lpCache->iSubItem = (*env)->GetFieldID(env, lpCache->clazz, "iSubItem", "I");
	lpCache->iImage = (*env)->GetFieldID(env, lpCache->clazz, "iImage", "I");
	lpCache->iOrder = (*env)->GetFieldID(env, lpCache->clazz, "iOrder", "I");
	lpCache->cached = 1;
}

LVCOLUMN* getLVCOLUMNFields(JNIEnv *env, jobject lpObject, LVCOLUMN *lpStruct, PLVCOLUMN_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheLVCOLUMNFids(env, lpObject, lpCache);
	lpStruct->mask = (*env)->GetIntField(env, lpObject, lpCache->mask);
	lpStruct->fmt = (*env)->GetIntField(env, lpObject, lpCache->fmt);
	lpStruct->cx = (*env)->GetIntField(env, lpObject, lpCache->cx);
	lpStruct->pszText = (LPTSTR)(*env)->GetIntField(env, lpObject, lpCache->pszText);
	lpStruct->cchTextMax = (*env)->GetIntField(env, lpObject, lpCache->cchTextMax);
	lpStruct->iSubItem = (*env)->GetIntField(env, lpObject, lpCache->iSubItem);
	lpStruct->iImage = (*env)->GetIntField(env, lpObject, lpCache->iImage);
	lpStruct->iOrder = (*env)->GetIntField(env, lpObject, lpCache->iOrder);
	return lpStruct;
}

void setLVCOLUMNFields(JNIEnv *env, jobject lpObject, LVCOLUMN *lpStruct, PLVCOLUMN_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheLVCOLUMNFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->mask, lpStruct->mask);
	(*env)->SetIntField(env, lpObject, lpCache->fmt, lpStruct->fmt);
	(*env)->SetIntField(env, lpObject, lpCache->cx, lpStruct->cx);
	(*env)->SetIntField(env, lpObject, lpCache->pszText, (jint)lpStruct->pszText);
	(*env)->SetIntField(env, lpObject, lpCache->cchTextMax, lpStruct->cchTextMax);
	(*env)->SetIntField(env, lpObject, lpCache->iSubItem, lpStruct->iSubItem);
	(*env)->SetIntField(env, lpObject, lpCache->iImage, lpStruct->iImage);
	(*env)->SetIntField(env, lpObject, lpCache->iOrder, lpStruct->iOrder);
}

void cacheLVHITTESTINFOFids(JNIEnv *env, jobject lpObject, PLVHITTESTINFO_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->x = (*env)->GetFieldID(env, lpCache->clazz, "x", "I");
	lpCache->y = (*env)->GetFieldID(env, lpCache->clazz, "y", "I");
	lpCache->flags = (*env)->GetFieldID(env, lpCache->clazz, "flags", "I");
	lpCache->iItem = (*env)->GetFieldID(env, lpCache->clazz, "iItem", "I");
	lpCache->iSubItem = (*env)->GetFieldID(env, lpCache->clazz, "iSubItem", "I");
	lpCache->cached = 1;
}

LVHITTESTINFO* getLVHITTESTINFOFields(JNIEnv *env, jobject lpObject, LVHITTESTINFO *lpStruct, PLVHITTESTINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheLVHITTESTINFOFids(env, lpObject, lpCache);
	lpStruct->pt.x = (*env)->GetIntField(env, lpObject, lpCache->x);
	lpStruct->pt.y = (*env)->GetIntField(env, lpObject, lpCache->y);
	lpStruct->flags = (*env)->GetIntField(env, lpObject, lpCache->flags);
	lpStruct->iItem = (*env)->GetIntField(env, lpObject, lpCache->iItem);
	lpStruct->iSubItem = (*env)->GetIntField(env, lpObject, lpCache->iSubItem);
	return lpStruct;
}

void setLVHITTESTINFOFields(JNIEnv *env, jobject lpObject, LVHITTESTINFO *lpStruct, PLVHITTESTINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheLVHITTESTINFOFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->x, lpStruct->pt.x);
	(*env)->SetIntField(env, lpObject, lpCache->y, lpStruct->pt.y);
	(*env)->SetIntField(env, lpObject, lpCache->flags, lpStruct->flags);
	(*env)->SetIntField(env, lpObject, lpCache->iItem, lpStruct->iItem);
	(*env)->SetIntField(env, lpObject, lpCache->iSubItem, lpStruct->iSubItem);
}

void cacheLVITEMFids(JNIEnv *env, jobject lpObject, PLVITEM_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->mask = (*env)->GetFieldID(env, lpCache->clazz, "mask", "I");
	lpCache->iItem = (*env)->GetFieldID(env, lpCache->clazz, "iItem", "I");
	lpCache->iSubItem = (*env)->GetFieldID(env, lpCache->clazz, "iSubItem", "I");
	lpCache->state = (*env)->GetFieldID(env, lpCache->clazz, "state", "I");
	lpCache->stateMask = (*env)->GetFieldID(env, lpCache->clazz, "stateMask", "I");
	lpCache->pszText = (*env)->GetFieldID(env, lpCache->clazz, "pszText", "I");
	lpCache->cchTextMax = (*env)->GetFieldID(env, lpCache->clazz, "cchTextMax", "I");
	lpCache->iImage = (*env)->GetFieldID(env, lpCache->clazz, "iImage", "I");
	lpCache->lParam = (*env)->GetFieldID(env, lpCache->clazz, "lParam", "I");
	lpCache->iIndent = (*env)->GetFieldID(env, lpCache->clazz, "iIndent", "I");
	lpCache->cached = 1;
}

LVITEM* getLVITEMFields(JNIEnv *env, jobject lpObject, LVITEM *lpStruct, PLVITEM_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheLVITEMFids(env, lpObject, lpCache);
	lpStruct->mask = (*env)->GetIntField(env, lpObject, lpCache->mask);
	lpStruct->iItem = (*env)->GetIntField(env, lpObject, lpCache->iItem);
	lpStruct->iSubItem = (*env)->GetIntField(env, lpObject, lpCache->iSubItem);
	lpStruct->state = (*env)->GetIntField(env, lpObject, lpCache->state);
	lpStruct->stateMask = (*env)->GetIntField(env, lpObject, lpCache->stateMask);
	lpStruct->pszText = (LPTSTR)(*env)->GetIntField(env, lpObject, lpCache->pszText);
	lpStruct->cchTextMax = (*env)->GetIntField(env, lpObject, lpCache->cchTextMax);
	lpStruct->iImage = (*env)->GetIntField(env, lpObject, lpCache->iImage);
	lpStruct->lParam = (*env)->GetIntField(env, lpObject, lpCache->lParam);
	lpStruct->iIndent = (*env)->GetIntField(env, lpObject, lpCache->iIndent);
	return lpStruct;
}

void setLVITEMFields(JNIEnv *env, jobject lpObject, LVITEM *lpStruct, PLVITEM_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheLVITEMFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->mask, lpStruct->mask);
	(*env)->SetIntField(env, lpObject, lpCache->iItem, lpStruct->iItem);
	(*env)->SetIntField(env, lpObject, lpCache->iSubItem, lpStruct->iSubItem);
	(*env)->SetIntField(env, lpObject, lpCache->state, lpStruct->state);
	(*env)->SetIntField(env, lpObject, lpCache->stateMask, lpStruct->stateMask);
	(*env)->SetIntField(env, lpObject, lpCache->pszText, (jint)lpStruct->pszText);
	(*env)->SetIntField(env, lpObject, lpCache->cchTextMax, lpStruct->cchTextMax);
	(*env)->SetIntField(env, lpObject, lpCache->iImage, lpStruct->iImage);
	(*env)->SetIntField(env, lpObject, lpCache->lParam, lpStruct->lParam);
	(*env)->SetIntField(env, lpObject, lpCache->iIndent, lpStruct->iIndent);
}

void cacheMEASUREITEMSTRUCTFids(JNIEnv *env, jobject lpObject, PMEASUREITEMSTRUCT_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->CtlType = (*env)->GetFieldID(env, lpCache->clazz, "CtlType", "I");
	lpCache->CtlID = (*env)->GetFieldID(env, lpCache->clazz, "CtlID", "I");
	lpCache->itemID = (*env)->GetFieldID(env, lpCache->clazz, "itemID", "I");
	lpCache->itemWidth = (*env)->GetFieldID(env, lpCache->clazz, "itemWidth", "I");
	lpCache->itemHeight = (*env)->GetFieldID(env, lpCache->clazz, "itemHeight", "I");
	lpCache->itemData = (*env)->GetFieldID(env, lpCache->clazz, "itemData", "I");
	lpCache->cached = 1;
}

MEASUREITEMSTRUCT* getMEASUREITEMSTRUCTFields(JNIEnv *env, jobject lpObject, MEASUREITEMSTRUCT *lpStruct, PMEASUREITEMSTRUCT_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheMEASUREITEMSTRUCTFids(env, lpObject, lpCache);
	lpStruct->CtlType = (*env)->GetIntField(env, lpObject, lpCache->CtlType);
	lpStruct->CtlID = (*env)->GetIntField(env, lpObject, lpCache->CtlID);
	lpStruct->itemID = (*env)->GetIntField(env, lpObject, lpCache->itemID);
	lpStruct->itemWidth = (*env)->GetIntField(env, lpObject, lpCache->itemWidth);
	lpStruct->itemHeight = (*env)->GetIntField(env, lpObject, lpCache->itemHeight);
	lpStruct->itemData = (*env)->GetIntField(env, lpObject, lpCache->itemData);
	return lpStruct;
}

void setMEASUREITEMSTRUCTFields(JNIEnv *env, jobject lpObject, MEASUREITEMSTRUCT *lpStruct, PMEASUREITEMSTRUCT_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheMEASUREITEMSTRUCTFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->CtlType, lpStruct->CtlType);
	(*env)->SetIntField(env, lpObject, lpCache->CtlID, lpStruct->CtlID);
	(*env)->SetIntField(env, lpObject, lpCache->itemID, lpStruct->itemID);
	(*env)->SetIntField(env, lpObject, lpCache->itemWidth, lpStruct->itemWidth);
	(*env)->SetIntField(env, lpObject, lpCache->itemHeight, lpStruct->itemHeight);
	(*env)->SetIntField(env, lpObject, lpCache->itemData, lpStruct->itemData);
}

#ifndef _WIN32_WCE

void cacheMENUINFOFids(JNIEnv *env, jobject lpObject, PMENUINFO_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->cbSize = (*env)->GetFieldID(env, lpCache->clazz, "cbSize", "I");
	lpCache->fMask = (*env)->GetFieldID(env, lpCache->clazz, "fMask", "I");
	lpCache->dwStyle = (*env)->GetFieldID(env, lpCache->clazz, "dwStyle", "I");
	lpCache->cyMax = (*env)->GetFieldID(env, lpCache->clazz, "cyMax", "I");
	lpCache->hbrBack = (*env)->GetFieldID(env, lpCache->clazz, "hbrBack", "I");
	lpCache->dwContextHelpID = (*env)->GetFieldID(env, lpCache->clazz, "dwContextHelpID", "I");
	lpCache->dwMenuData = (*env)->GetFieldID(env, lpCache->clazz, "dwMenuData", "I");
	lpCache->cached = 1;
}

MENUINFO* getMENUINFOFields(JNIEnv *env, jobject lpObject, MENUINFO *lpStruct, PMENUINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheMENUINFOFids(env, lpObject, lpCache);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, lpCache->cbSize);
	lpStruct->fMask = (*env)->GetIntField(env, lpObject, lpCache->fMask);
	lpStruct->dwStyle = (*env)->GetIntField(env, lpObject, lpCache->dwStyle);
	lpStruct->cyMax = (*env)->GetIntField(env, lpObject, lpCache->cyMax);
	lpStruct->hbrBack = (HBRUSH)(*env)->GetIntField(env, lpObject, lpCache->hbrBack);
	lpStruct->dwContextHelpID = (*env)->GetIntField(env, lpObject, lpCache->dwContextHelpID);
	lpStruct->dwMenuData = (*env)->GetIntField(env, lpObject, lpCache->dwMenuData);
	return lpStruct;
}

void setMENUINFOFields(JNIEnv *env, jobject lpObject, MENUINFO *lpStruct, PMENUINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheMENUINFOFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->cbSize, lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, lpCache->fMask, lpStruct->fMask);
	(*env)->SetIntField(env, lpObject, lpCache->dwStyle, lpStruct->dwStyle);
	(*env)->SetIntField(env, lpObject, lpCache->cyMax, lpStruct->cyMax);
	(*env)->SetIntField(env, lpObject, lpCache->hbrBack, (jint)lpStruct->hbrBack);
	(*env)->SetIntField(env, lpObject, lpCache->dwContextHelpID, lpStruct->dwContextHelpID);
	(*env)->SetIntField(env, lpObject, lpCache->dwMenuData, lpStruct->dwMenuData);
}

#endif // _WIN32_WCE

void cacheMENUITEMINFOFids(JNIEnv *env, jobject lpObject, PMENUITEMINFO_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->cbSize = (*env)->GetFieldID(env, lpCache->clazz, "cbSize", "I");
	lpCache->fMask = (*env)->GetFieldID(env, lpCache->clazz, "fMask", "I");
	lpCache->fType = (*env)->GetFieldID(env, lpCache->clazz, "fType", "I");
	lpCache->fState = (*env)->GetFieldID(env, lpCache->clazz, "fState", "I");
	lpCache->wID = (*env)->GetFieldID(env, lpCache->clazz, "wID", "I");
	lpCache->hSubMenu = (*env)->GetFieldID(env, lpCache->clazz, "hSubMenu", "I");
	lpCache->hbmpChecked = (*env)->GetFieldID(env, lpCache->clazz, "hbmpChecked", "I");
	lpCache->hbmpUnchecked = (*env)->GetFieldID(env, lpCache->clazz, "hbmpUnchecked", "I");
	lpCache->dwItemData = (*env)->GetFieldID(env, lpCache->clazz, "dwItemData", "I");
	lpCache->dwTypeData = (*env)->GetFieldID(env, lpCache->clazz, "dwTypeData", "I");
	lpCache->cch = (*env)->GetFieldID(env, lpCache->clazz, "cch", "I");
	lpCache->hbmpItem = (*env)->GetFieldID(env, lpCache->clazz, "hbmpItem", "I");
	lpCache->cached = 1;
}

MENUITEMINFO* getMENUITEMINFOFields(JNIEnv *env, jobject lpObject, MENUITEMINFO *lpStruct, PMENUITEMINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheMENUITEMINFOFids(env, lpObject, lpCache);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, lpCache->cbSize);
	lpStruct->fMask = (*env)->GetIntField(env, lpObject, lpCache->fMask);
	lpStruct->fType = (*env)->GetIntField(env, lpObject, lpCache->fType);
	lpStruct->fState = (*env)->GetIntField(env, lpObject, lpCache->fState);
	lpStruct->wID = (*env)->GetIntField(env, lpObject, lpCache->wID);
	lpStruct->hSubMenu = (HMENU)(*env)->GetIntField(env, lpObject, lpCache->hSubMenu);
	lpStruct->hbmpChecked = (HBITMAP)(*env)->GetIntField(env, lpObject, lpCache->hbmpChecked);
	lpStruct->hbmpUnchecked = (HBITMAP)(*env)->GetIntField(env, lpObject, lpCache->hbmpUnchecked);
	lpStruct->dwItemData = (*env)->GetIntField(env, lpObject, lpCache->dwItemData);
	lpStruct->dwTypeData = (LPTSTR)(*env)->GetIntField(env, lpObject, lpCache->dwTypeData);
	lpStruct->cch = (*env)->GetIntField(env, lpObject, lpCache->cch);
#ifndef _WIN32_WCE
	lpStruct->hbmpItem = (HBITMAP)(*env)->GetIntField(env, lpObject, lpCache->hbmpItem);
#endif
	return lpStruct;
}

void setMENUITEMINFOFields(JNIEnv *env, jobject lpObject, MENUITEMINFO *lpStruct, PMENUITEMINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheMENUITEMINFOFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->cbSize, lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, lpCache->fMask, lpStruct->fMask);
	(*env)->SetIntField(env, lpObject, lpCache->fType, lpStruct->fType);
	(*env)->SetIntField(env, lpObject, lpCache->fState, lpStruct->fState);
	(*env)->SetIntField(env, lpObject, lpCache->wID, lpStruct->wID);
	(*env)->SetIntField(env, lpObject, lpCache->hSubMenu, (jint)lpStruct->hSubMenu);
	(*env)->SetIntField(env, lpObject, lpCache->hbmpChecked, (jint)lpStruct->hbmpChecked);
	(*env)->SetIntField(env, lpObject, lpCache->hbmpUnchecked, (jint)lpStruct->hbmpUnchecked);
	(*env)->SetIntField(env, lpObject, lpCache->dwItemData, lpStruct->dwItemData);
	(*env)->SetIntField(env, lpObject, lpCache->dwTypeData, (jint)lpStruct->dwTypeData);
	(*env)->SetIntField(env, lpObject, lpCache->cch, lpStruct->cch);
#ifndef _WIN32_WCE
	(*env)->SetIntField(env, lpObject, lpCache->hbmpItem, (jint)lpStruct->hbmpItem);
#endif
}

void cacheMSGFids(JNIEnv *env, jobject lpObject, PMSG_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->hwnd = (*env)->GetFieldID(env, lpCache->clazz, "hwnd", "I");
	lpCache->message = (*env)->GetFieldID(env, lpCache->clazz, "message", "I");
	lpCache->wParam = (*env)->GetFieldID(env, lpCache->clazz, "wParam", "I");
	lpCache->lParam = (*env)->GetFieldID(env, lpCache->clazz, "lParam", "I");
	lpCache->time = (*env)->GetFieldID(env, lpCache->clazz, "time", "I");
	lpCache->x = (*env)->GetFieldID(env, lpCache->clazz, "x", "I");
	lpCache->y = (*env)->GetFieldID(env, lpCache->clazz, "y", "I");
	lpCache->cached = 1;
}

MSG* getMSGFields(JNIEnv *env, jobject lpObject, MSG *lpStruct, PMSG_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheMSGFids(env, lpObject, lpCache);
	lpStruct->hwnd = (HWND)(*env)->GetIntField(env, lpObject, lpCache->hwnd);
	lpStruct->message = (*env)->GetIntField(env, lpObject, lpCache->message);
	lpStruct->wParam = (*env)->GetIntField(env, lpObject, lpCache->wParam);
	lpStruct->lParam = (*env)->GetIntField(env, lpObject, lpCache->lParam);
	lpStruct->time = (*env)->GetIntField(env, lpObject, lpCache->time);
	lpStruct->pt.x = (*env)->GetIntField(env, lpObject, lpCache->x);
	lpStruct->pt.y = (*env)->GetIntField(env, lpObject, lpCache->y);
	return lpStruct;
}

void setMSGFields(JNIEnv *env, jobject lpObject, MSG *lpStruct, PMSG_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheMSGFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->hwnd, (jint)lpStruct->hwnd);
	(*env)->SetIntField(env, lpObject, lpCache->message, lpStruct->message);
	(*env)->SetIntField(env, lpObject, lpCache->wParam, lpStruct->wParam);
	(*env)->SetIntField(env, lpObject, lpCache->lParam, lpStruct->lParam);
	(*env)->SetIntField(env, lpObject, lpCache->time, lpStruct->time);
	(*env)->SetIntField(env, lpObject, lpCache->x, lpStruct->pt.x);
	(*env)->SetIntField(env, lpObject, lpCache->y, lpStruct->pt.y);
}

void cacheNMHDRFids(JNIEnv *env, jobject lpObject, PNMHDR_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->hwndFrom = (*env)->GetFieldID(env, lpCache->clazz, "hwndFrom", "I");
	lpCache->idFrom = (*env)->GetFieldID(env, lpCache->clazz, "idFrom", "I");
	lpCache->code = (*env)->GetFieldID(env, lpCache->clazz, "code", "I");
	lpCache->cached = 1;
}

NMHDR* getNMHDRFields(JNIEnv *env, jobject lpObject, NMHDR *lpStruct, PNMHDR_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheNMHDRFids(env, lpObject, lpCache);
	lpStruct->hwndFrom = (HWND)(*env)->GetIntField(env, lpObject, lpCache->hwndFrom);
	lpStruct->idFrom = (*env)->GetIntField(env, lpObject, lpCache->idFrom);
	lpStruct->code = (*env)->GetIntField(env, lpObject, lpCache->code);
	return lpStruct;
}

void setNMHDRFields(JNIEnv *env, jobject lpObject, NMHDR *lpStruct, PNMHDR_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheNMHDRFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->hwndFrom, (jint)lpStruct->hwndFrom);
	(*env)->SetIntField(env, lpObject, lpCache->idFrom, lpStruct->idFrom);
	(*env)->SetIntField(env, lpObject, lpCache->code, lpStruct->code);
}

void cacheNMHEADERFids(JNIEnv *env, jobject lpObject, PNMHEADER_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->hwndFrom = (*env)->GetFieldID(env, lpCache->clazz, "hwndFrom", "I");
	lpCache->idFrom = (*env)->GetFieldID(env, lpCache->clazz, "idFrom", "I");
	lpCache->code = (*env)->GetFieldID(env, lpCache->clazz, "code", "I");
	lpCache->iItem = (*env)->GetFieldID(env, lpCache->clazz, "iItem", "I");
	lpCache->iButton = (*env)->GetFieldID(env, lpCache->clazz, "iButton", "I");
	lpCache->pitem = (*env)->GetFieldID(env, lpCache->clazz, "pitem", "I");
	lpCache->cached = 1;
}

NMHEADER* getNMHEADERFields(JNIEnv *env, jobject lpObject, NMHEADER *lpStruct, PNMHEADER_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheNMHEADERFids(env, lpObject, lpCache);
	lpStruct->hdr.hwndFrom = (HWND)(*env)->GetIntField(env, lpObject, lpCache->hwndFrom);
	lpStruct->hdr.idFrom = (*env)->GetIntField(env, lpObject, lpCache->idFrom);
	lpStruct->hdr.code = (*env)->GetIntField(env, lpObject, lpCache->code);
	lpStruct->iItem = (*env)->GetIntField(env, lpObject, lpCache->iItem);
	lpStruct->iButton = (*env)->GetIntField(env, lpObject, lpCache->iButton);
	lpStruct->pitem = (HDITEM FAR *)(*env)->GetIntField(env, lpObject, lpCache->pitem);
	return lpStruct;
}

void setNMHEADERFields(JNIEnv *env, jobject lpObject, NMHEADER *lpStruct, PNMHEADER_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheNMHEADERFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->hwndFrom, (jint)lpStruct->hdr.hwndFrom);
	(*env)->SetIntField(env, lpObject, lpCache->idFrom, lpStruct->hdr.idFrom);
	(*env)->SetIntField(env, lpObject, lpCache->code, lpStruct->hdr.code);
	(*env)->SetIntField(env, lpObject, lpCache->iItem, lpStruct->iItem);
	(*env)->SetIntField(env, lpObject, lpCache->iButton, lpStruct->iButton);
	(*env)->SetIntField(env, lpObject, lpCache->pitem, (jint)lpStruct->pitem);
}

void cacheNMLISTVIEWFids(JNIEnv *env, jobject lpObject, PNMLISTVIEW_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->hwndFrom = (*env)->GetFieldID(env, lpCache->clazz, "hwndFrom", "I");
	lpCache->idFrom = (*env)->GetFieldID(env, lpCache->clazz, "idFrom", "I");
	lpCache->code = (*env)->GetFieldID(env, lpCache->clazz, "code", "I");
	lpCache->iItem = (*env)->GetFieldID(env, lpCache->clazz, "iItem", "I");
	lpCache->iSubItem = (*env)->GetFieldID(env, lpCache->clazz, "iSubItem", "I");
	lpCache->uNewState = (*env)->GetFieldID(env, lpCache->clazz, "uNewState", "I");
	lpCache->uOldState = (*env)->GetFieldID(env, lpCache->clazz, "uOldState", "I");
	lpCache->uChanged = (*env)->GetFieldID(env, lpCache->clazz, "uChanged", "I");
	lpCache->x = (*env)->GetFieldID(env, lpCache->clazz, "x", "I");
	lpCache->y = (*env)->GetFieldID(env, lpCache->clazz, "y", "I");
	lpCache->lParam = (*env)->GetFieldID(env, lpCache->clazz, "lParam", "I");
	lpCache->cached = 1;
}

NMLISTVIEW* getNMLISTVIEWFields(JNIEnv *env, jobject lpObject, NMLISTVIEW *lpStruct, PNMLISTVIEW_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheNMLISTVIEWFids(env, lpObject, lpCache);
	lpStruct->hdr.hwndFrom = (HWND)(*env)->GetIntField(env, lpObject, lpCache->hwndFrom);
	lpStruct->hdr.idFrom = (*env)->GetIntField(env, lpObject, lpCache->idFrom);
	lpStruct->hdr.code = (*env)->GetIntField(env, lpObject, lpCache->code);
	lpStruct->iItem = (*env)->GetIntField(env, lpObject, lpCache->iItem);
	lpStruct->iSubItem = (*env)->GetIntField(env, lpObject, lpCache->iSubItem);
	lpStruct->uNewState = (*env)->GetIntField(env, lpObject, lpCache->uNewState);
	lpStruct->uOldState = (*env)->GetIntField(env, lpObject, lpCache->uOldState);
	lpStruct->uChanged = (*env)->GetIntField(env, lpObject, lpCache->uChanged);
	lpStruct->ptAction.x = (*env)->GetIntField(env, lpObject, lpCache->x);
	lpStruct->ptAction.y = (*env)->GetIntField(env, lpObject, lpCache->y);
	lpStruct->lParam = (*env)->GetIntField(env, lpObject, lpCache->lParam);
	return lpStruct;
}

void setNMLISTVIEWFields(JNIEnv *env, jobject lpObject, NMLISTVIEW *lpStruct, PNMLISTVIEW_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheNMLISTVIEWFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->hwndFrom, (jint)lpStruct->hdr.hwndFrom);
	(*env)->SetIntField(env, lpObject, lpCache->idFrom, lpStruct->hdr.idFrom);
	(*env)->SetIntField(env, lpObject, lpCache->code, lpStruct->hdr.code);
	(*env)->SetIntField(env, lpObject, lpCache->iItem, lpStruct->iItem);
	(*env)->SetIntField(env, lpObject, lpCache->iSubItem, lpStruct->iSubItem);
	(*env)->SetIntField(env, lpObject, lpCache->uNewState, lpStruct->uNewState);
	(*env)->SetIntField(env, lpObject, lpCache->uOldState, lpStruct->uOldState);
	(*env)->SetIntField(env, lpObject, lpCache->uChanged, lpStruct->uChanged);
	(*env)->SetIntField(env, lpObject, lpCache->x, lpStruct->ptAction.x);
	(*env)->SetIntField(env, lpObject, lpCache->y, lpStruct->ptAction.y);
	(*env)->SetIntField(env, lpObject, lpCache->lParam, lpStruct->lParam);
}

void cacheNMTOOLBARFids(JNIEnv *env, jobject lpObject, PNMTOOLBAR_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->hwndFrom = (*env)->GetFieldID(env, lpCache->clazz, "hwndFrom", "I");
	lpCache->idFrom = (*env)->GetFieldID(env, lpCache->clazz, "idFrom", "I");
	lpCache->code = (*env)->GetFieldID(env, lpCache->clazz, "code", "I");
	lpCache->iItem = (*env)->GetFieldID(env, lpCache->clazz, "iItem", "I");
	lpCache->iBitmap = (*env)->GetFieldID(env, lpCache->clazz, "iBitmap", "I");
	lpCache->idCommand = (*env)->GetFieldID(env, lpCache->clazz, "idCommand", "I");
	lpCache->fsState = (*env)->GetFieldID(env, lpCache->clazz, "fsState", "B");
	lpCache->fsStyle = (*env)->GetFieldID(env, lpCache->clazz, "fsStyle", "B");
	lpCache->dwData = (*env)->GetFieldID(env, lpCache->clazz, "dwData", "I");
	lpCache->iString = (*env)->GetFieldID(env, lpCache->clazz, "iString", "I");
	lpCache->cchText = (*env)->GetFieldID(env, lpCache->clazz, "cchText", "I");
	lpCache->pszText = (*env)->GetFieldID(env, lpCache->clazz, "pszText", "I");
	lpCache->left = (*env)->GetFieldID(env, lpCache->clazz, "left", "I");
	lpCache->top = (*env)->GetFieldID(env, lpCache->clazz, "top", "I");
	lpCache->right = (*env)->GetFieldID(env, lpCache->clazz, "right", "I");
	lpCache->bottom = (*env)->GetFieldID(env, lpCache->clazz, "bottom", "I");
	lpCache->cached = 1;
}

NMTOOLBAR* getNMTOOLBARFields(JNIEnv *env, jobject lpObject, NMTOOLBAR *lpStruct, PNMTOOLBAR_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheNMTOOLBARFids(env, lpObject, lpCache);
	lpStruct->hdr.hwndFrom = (HWND)(*env)->GetIntField(env, lpObject, lpCache->hwndFrom);
	lpStruct->hdr.idFrom = (*env)->GetIntField(env, lpObject, lpCache->idFrom);
	lpStruct->hdr.code = (*env)->GetIntField(env, lpObject, lpCache->code);
	lpStruct->iItem = (*env)->GetIntField(env, lpObject, lpCache->iItem);
	lpStruct->tbButton.iBitmap = (*env)->GetIntField(env, lpObject, lpCache->iBitmap);
	lpStruct->tbButton.idCommand = (*env)->GetIntField(env, lpObject, lpCache->idCommand);
	lpStruct->tbButton.fsState = (*env)->GetByteField(env, lpObject, lpCache->fsState);
	lpStruct->tbButton.fsStyle = (*env)->GetByteField(env, lpObject, lpCache->fsStyle);
	lpStruct->tbButton.dwData = (*env)->GetIntField(env, lpObject, lpCache->dwData);
	lpStruct->tbButton.iString = (*env)->GetIntField(env, lpObject, lpCache->iString);
	lpStruct->cchText = (*env)->GetIntField(env, lpObject, lpCache->cchText);
	lpStruct->pszText = (LPTSTR)(*env)->GetIntField(env, lpObject, lpCache->pszText);
#ifndef _WIN32_WCE
	lpStruct->rcButton.left = (*env)->GetIntField(env, lpObject, lpCache->left);
	lpStruct->rcButton.top = (*env)->GetIntField(env, lpObject, lpCache->top);
	lpStruct->rcButton.right = (*env)->GetIntField(env, lpObject, lpCache->right);
	lpStruct->rcButton.bottom = (*env)->GetIntField(env, lpObject, lpCache->bottom);
#endif
	return lpStruct;
}

void setNMTOOLBARFields(JNIEnv *env, jobject lpObject, NMTOOLBAR *lpStruct, PNMTOOLBAR_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheNMTOOLBARFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->hwndFrom, (jint)lpStruct->hdr.hwndFrom);
	(*env)->SetIntField(env, lpObject, lpCache->idFrom, lpStruct->hdr.idFrom);
	(*env)->SetIntField(env, lpObject, lpCache->code, lpStruct->hdr.code);
	(*env)->SetIntField(env, lpObject, lpCache->iItem, lpStruct->iItem);
	(*env)->SetIntField(env, lpObject, lpCache->iBitmap, lpStruct->tbButton.iBitmap);
	(*env)->SetIntField(env, lpObject, lpCache->idCommand, lpStruct->tbButton.idCommand);
	(*env)->SetByteField(env, lpObject, lpCache->fsState, lpStruct->tbButton.fsState);
	(*env)->SetByteField(env, lpObject, lpCache->fsStyle, lpStruct->tbButton.fsStyle);
	(*env)->SetIntField(env, lpObject, lpCache->dwData, lpStruct->tbButton.dwData);
	(*env)->SetIntField(env, lpObject, lpCache->iString, lpStruct->tbButton.iString);
	(*env)->SetIntField(env, lpObject, lpCache->cchText, lpStruct->cchText);
	(*env)->SetIntField(env, lpObject, lpCache->pszText, (jint)lpStruct->pszText);
#ifndef _WIN32_WCE
	(*env)->SetIntField(env, lpObject, lpCache->left, lpStruct->rcButton.left);
	(*env)->SetIntField(env, lpObject, lpCache->top, lpStruct->rcButton.top);
	(*env)->SetIntField(env, lpObject, lpCache->right, lpStruct->rcButton.right);
	(*env)->SetIntField(env, lpObject, lpCache->bottom, lpStruct->rcButton.bottom);
#endif
}

#ifndef _WIN32_WCE

void cacheNMTTDISPINFOFids(JNIEnv *env, jobject lpObject, PNMTTDISPINFO_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->hwndFrom = (*env)->GetFieldID(env, lpCache->clazz, "hwndFrom", "I");
	lpCache->idFrom = (*env)->GetFieldID(env, lpCache->clazz, "idFrom", "I");
	lpCache->code = (*env)->GetFieldID(env, lpCache->clazz, "code", "I");
	lpCache->lpszText = (*env)->GetFieldID(env, lpCache->clazz, "lpszText", "I");
	lpCache->hinst = (*env)->GetFieldID(env, lpCache->clazz, "hinst", "I");
	lpCache->uFlags = (*env)->GetFieldID(env, lpCache->clazz, "uFlags", "I");
	lpCache->lParam = (*env)->GetFieldID(env, lpCache->clazz, "lParam", "I");
	lpCache->cached = 1;
}

NMTTDISPINFOA* getNMTTDISPINFOAFields(JNIEnv *env, jobject lpObject, NMTTDISPINFOA *lpStruct, PNMTTDISPINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheNMTTDISPINFOFids(env, lpObject, lpCache);
	lpStruct->hdr.hwndFrom = (HWND)(*env)->GetIntField(env, lpObject, lpCache->hwndFrom);
	lpStruct->hdr.idFrom = (*env)->GetIntField(env, lpObject, lpCache->idFrom);
	lpStruct->hdr.code = (*env)->GetIntField(env, lpObject, lpCache->code);
	lpStruct->lpszText = (LPTSTR)(*env)->GetIntField(env, lpObject, lpCache->lpszText);
	lpStruct->hinst = (HINSTANCE)(*env)->GetIntField(env, lpObject, lpCache->hinst);
	lpStruct->uFlags = (*env)->GetIntField(env, lpObject, lpCache->uFlags);
	lpStruct->lParam = (*env)->GetIntField(env, lpObject, lpCache->lParam);
	return lpStruct;
}

void setNMTTDISPINFOAFields(JNIEnv *env, jobject lpObject, NMTTDISPINFOA *lpStruct, PNMTTDISPINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheNMTTDISPINFOFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->hwndFrom, (jint)lpStruct->hdr.hwndFrom);
	(*env)->SetIntField(env, lpObject, lpCache->idFrom, lpStruct->hdr.idFrom);
	(*env)->SetIntField(env, lpObject, lpCache->code, lpStruct->hdr.code);
	(*env)->SetIntField(env, lpObject, lpCache->lpszText, (jint)lpStruct->lpszText);
	(*env)->SetIntField(env, lpObject, lpCache->hinst, (jint)lpStruct->hinst);
	(*env)->SetIntField(env, lpObject, lpCache->uFlags, lpStruct->uFlags);
	(*env)->SetIntField(env, lpObject, lpCache->lParam, lpStruct->lParam);
}

NMTTDISPINFOW* getNMTTDISPINFOWFields(JNIEnv *env, jobject lpObject, NMTTDISPINFOW *lpStruct, PNMTTDISPINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheNMTTDISPINFOFids(env, lpObject, lpCache);
	lpStruct->hdr.hwndFrom = (HWND)(*env)->GetIntField(env, lpObject, lpCache->hwndFrom);
	lpStruct->hdr.idFrom = (*env)->GetIntField(env, lpObject, lpCache->idFrom);
	lpStruct->hdr.code = (*env)->GetIntField(env, lpObject, lpCache->code);
	lpStruct->lpszText = (LPWSTR)(*env)->GetIntField(env, lpObject, lpCache->lpszText);
	lpStruct->hinst = (HINSTANCE)(*env)->GetIntField(env, lpObject, lpCache->hinst);
	lpStruct->uFlags = (*env)->GetIntField(env, lpObject, lpCache->uFlags);
	lpStruct->lParam = (*env)->GetIntField(env, lpObject, lpCache->lParam);
	return lpStruct;
}

void setNMTTDISPINFOWFields(JNIEnv *env, jobject lpObject, NMTTDISPINFOW *lpStruct, PNMTTDISPINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheNMTTDISPINFOFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->hwndFrom, (jint)lpStruct->hdr.hwndFrom);
	(*env)->SetIntField(env, lpObject, lpCache->idFrom, lpStruct->hdr.idFrom);
	(*env)->SetIntField(env, lpObject, lpCache->code, lpStruct->hdr.code);
	(*env)->SetIntField(env, lpObject, lpCache->lpszText, (jint)lpStruct->lpszText);
	(*env)->SetIntField(env, lpObject, lpCache->hinst, (jint)lpStruct->hinst);
	(*env)->SetIntField(env, lpObject, lpCache->uFlags, lpStruct->uFlags);
	(*env)->SetIntField(env, lpObject, lpCache->lParam, lpStruct->lParam);
}

#endif // _WIN32_WCE

#ifndef _WIN32_WCE

NONCLIENTMETRICSA* getNONCLIENTMETRICSAFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICSA *lpStruct, PNONCLIENTMETRICS_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheNONCLIENTMETRICSFids(env, lpObject, lpCache);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, lpCache->cbSize);
	lpStruct->iBorderWidth = (*env)->GetIntField(env, lpObject, lpCache->iBorderWidth);
	lpStruct->iScrollWidth = (*env)->GetIntField(env, lpObject, lpCache->iScrollWidth);
	lpStruct->iScrollHeight = (*env)->GetIntField(env, lpObject, lpCache->iScrollHeight);
	lpStruct->iCaptionWidth = (*env)->GetIntField(env, lpObject, lpCache->iCaptionWidth);
	lpStruct->iCaptionHeight = (*env)->GetIntField(env, lpObject, lpCache->iCaptionHeight);
	{
	jobject lpLogfont = (*env)->GetObjectField(env, lpObject, lpCache->lfCaptionFont);
	getLOGFONTAFields(env, lpLogfont, &lpStruct->lfCaptionFont, &PGLOB(LOGFONTFc));
	}
	lpStruct->iSmCaptionWidth = (*env)->GetIntField(env, lpObject, lpCache->iSmCaptionWidth);
	lpStruct->iSmCaptionHeight = (*env)->GetIntField(env, lpObject, lpCache->iSmCaptionHeight);
	{
	jobject lpLogfont = (*env)->GetObjectField(env, lpObject, lpCache->lfSmCaptionFont);
	getLOGFONTAFields(env, lpLogfont, &lpStruct->lfSmCaptionFont, &PGLOB(LOGFONTFc));
	}
	lpStruct->iMenuWidth = (*env)->GetIntField(env, lpObject, lpCache->iMenuWidth);
	lpStruct->iMenuHeight = (*env)->GetIntField(env, lpObject, lpCache->iMenuHeight);
	{
	jobject lpLogfont = (*env)->GetObjectField(env, lpObject, lpCache->lfMenuFont);
	getLOGFONTAFields(env, lpLogfont, &lpStruct->lfMenuFont, &PGLOB(LOGFONTFc));
	}
	{
	jobject lpLogfont = (*env)->GetObjectField(env, lpObject, lpCache->lfStatusFont);
	getLOGFONTAFields(env, lpLogfont, &lpStruct->lfStatusFont, &PGLOB(LOGFONTFc));
	}
	{
	jobject lpLogfont = (*env)->GetObjectField(env, lpObject, lpCache->lfMessageFont);
	getLOGFONTAFields(env, lpLogfont, &lpStruct->lfMessageFont, &PGLOB(LOGFONTFc));
	}
	return lpStruct;
}

void setNONCLIENTMETRICSAFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICSA *lpStruct, PNONCLIENTMETRICS_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheNONCLIENTMETRICSFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->cbSize, lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, lpCache->iBorderWidth, lpStruct->iBorderWidth);
	(*env)->SetIntField(env, lpObject, lpCache->iScrollWidth, lpStruct->iScrollWidth);
	(*env)->SetIntField(env, lpObject, lpCache->iScrollHeight, lpStruct->iScrollHeight);
	(*env)->SetIntField(env, lpObject, lpCache->iCaptionWidth, lpStruct->iCaptionWidth);
	(*env)->SetIntField(env, lpObject, lpCache->iCaptionHeight, lpStruct->iCaptionHeight);
	{
	jobject lpLogfont = (*env)->GetObjectField(env, lpObject, lpCache->lfCaptionFont);
	setLOGFONTAFields(env, lpLogfont, &lpStruct->lfCaptionFont, &PGLOB(LOGFONTFc));
	}
	(*env)->SetIntField(env, lpObject, lpCache->iSmCaptionWidth, lpStruct->iSmCaptionWidth);
	(*env)->SetIntField(env, lpObject, lpCache->iSmCaptionHeight, lpStruct->iSmCaptionHeight);
	{
	jobject lpLogfont = (*env)->GetObjectField(env, lpObject, lpCache->lfSmCaptionFont);
	setLOGFONTAFields(env, lpLogfont, &lpStruct->lfSmCaptionFont, &PGLOB(LOGFONTFc));
	}
	(*env)->SetIntField(env, lpObject, lpCache->iMenuWidth, lpStruct->iMenuWidth);
	(*env)->SetIntField(env, lpObject, lpCache->iMenuHeight, lpStruct->iMenuHeight);
	{
	jobject lpLogfont = (*env)->GetObjectField(env, lpObject, lpCache->lfMenuFont);
	setLOGFONTAFields(env, lpLogfont, &lpStruct->lfMenuFont, &PGLOB(LOGFONTFc));
	}
	{
	jobject lpLogfont = (*env)->GetObjectField(env, lpObject, lpCache->lfStatusFont);
	setLOGFONTAFields(env, lpLogfont, &lpStruct->lfStatusFont, &PGLOB(LOGFONTFc));
	}
	{
	jobject lpLogfont = (*env)->GetObjectField(env, lpObject, lpCache->lfMessageFont);
	setLOGFONTAFields(env, lpLogfont, &lpStruct->lfMessageFont, &PGLOB(LOGFONTFc));
	}
}

NONCLIENTMETRICSW* getNONCLIENTMETRICSWFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICSW *lpStruct, PNONCLIENTMETRICS_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheNONCLIENTMETRICSFids(env, lpObject, lpCache);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, lpCache->cbSize);
	lpStruct->iBorderWidth = (*env)->GetIntField(env, lpObject, lpCache->iBorderWidth);
	lpStruct->iScrollWidth = (*env)->GetIntField(env, lpObject, lpCache->iScrollWidth);
	lpStruct->iScrollHeight = (*env)->GetIntField(env, lpObject, lpCache->iScrollHeight);
	lpStruct->iCaptionWidth = (*env)->GetIntField(env, lpObject, lpCache->iCaptionWidth);
	lpStruct->iCaptionHeight = (*env)->GetIntField(env, lpObject, lpCache->iCaptionHeight);
	{
	jobject lpLogfont = (*env)->GetObjectField(env, lpObject, lpCache->lfCaptionFont);
	getLOGFONTWFields(env, lpLogfont, &lpStruct->lfCaptionFont, &PGLOB(LOGFONTFc));
	}
	lpStruct->iSmCaptionWidth = (*env)->GetIntField(env, lpObject, lpCache->iSmCaptionWidth);
	lpStruct->iSmCaptionHeight = (*env)->GetIntField(env, lpObject, lpCache->iSmCaptionHeight);
	{
	jobject lpLogfont = (*env)->GetObjectField(env, lpObject, lpCache->lfSmCaptionFont);
	getLOGFONTWFields(env, lpLogfont, &lpStruct->lfSmCaptionFont, &PGLOB(LOGFONTFc));
	}
	lpStruct->iMenuWidth = (*env)->GetIntField(env, lpObject, lpCache->iMenuWidth);
	lpStruct->iMenuHeight = (*env)->GetIntField(env, lpObject, lpCache->iMenuHeight);
	{
	jobject lpLogfont = (*env)->GetObjectField(env, lpObject, lpCache->lfMenuFont);
	getLOGFONTWFields(env, lpLogfont, &lpStruct->lfMenuFont, &PGLOB(LOGFONTFc));
	}
	{
	jobject lpLogfont = (*env)->GetObjectField(env, lpObject, lpCache->lfStatusFont);
	getLOGFONTWFields(env, lpLogfont, &lpStruct->lfStatusFont, &PGLOB(LOGFONTFc));
	}
	{
	jobject lpLogfont = (*env)->GetObjectField(env, lpObject, lpCache->lfMessageFont);
	getLOGFONTWFields(env, lpLogfont, &lpStruct->lfMessageFont, &PGLOB(LOGFONTFc));
	}
	return lpStruct;
}

void setNONCLIENTMETRICSWFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICSW *lpStruct, PNONCLIENTMETRICS_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheNONCLIENTMETRICSFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->cbSize, lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, lpCache->iBorderWidth, lpStruct->iBorderWidth);
	(*env)->SetIntField(env, lpObject, lpCache->iScrollWidth, lpStruct->iScrollWidth);
	(*env)->SetIntField(env, lpObject, lpCache->iScrollHeight, lpStruct->iScrollHeight);
	(*env)->SetIntField(env, lpObject, lpCache->iCaptionWidth, lpStruct->iCaptionWidth);
	(*env)->SetIntField(env, lpObject, lpCache->iCaptionHeight, lpStruct->iCaptionHeight);
	{
	jobject lpLogfont = (*env)->GetObjectField(env, lpObject, lpCache->lfCaptionFont);
	setLOGFONTWFields(env, lpLogfont, &lpStruct->lfCaptionFont, &PGLOB(LOGFONTFc));
	}
	(*env)->SetIntField(env, lpObject, lpCache->iSmCaptionWidth, lpStruct->iSmCaptionWidth);
	(*env)->SetIntField(env, lpObject, lpCache->iSmCaptionHeight, lpStruct->iSmCaptionHeight);
	{
	jobject lpLogfont = (*env)->GetObjectField(env, lpObject, lpCache->lfSmCaptionFont);
	setLOGFONTWFields(env, lpLogfont, &lpStruct->lfSmCaptionFont, &PGLOB(LOGFONTFc));
	}
	(*env)->SetIntField(env, lpObject, lpCache->iMenuWidth, lpStruct->iMenuWidth);
	(*env)->SetIntField(env, lpObject, lpCache->iMenuHeight, lpStruct->iMenuHeight);
	{
	jobject lpLogfont = (*env)->GetObjectField(env, lpObject, lpCache->lfMenuFont);
	setLOGFONTWFields(env, lpLogfont, &lpStruct->lfMenuFont, &PGLOB(LOGFONTFc));
	}
	{
	jobject lpLogfont = (*env)->GetObjectField(env, lpObject, lpCache->lfStatusFont);
	setLOGFONTWFields(env, lpLogfont, &lpStruct->lfStatusFont, &PGLOB(LOGFONTFc));
	}
	{
	jobject lpLogfont = (*env)->GetObjectField(env, lpObject, lpCache->lfMessageFont);
	setLOGFONTWFields(env, lpLogfont, &lpStruct->lfMessageFont, &PGLOB(LOGFONTFc));
	}
}

#endif _WIN32_WCE

void cacheOPENFILENAMEFids(JNIEnv *env, jobject lpObject, POPENFILENAME_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->lStructSize = (*env)->GetFieldID(env, lpCache->clazz, "lStructSize", "I");
	lpCache->hwndOwner = (*env)->GetFieldID(env, lpCache->clazz, "hwndOwner", "I");
	lpCache->hInstance = (*env)->GetFieldID(env, lpCache->clazz, "hInstance", "I");
	lpCache->lpstrFilter = (*env)->GetFieldID(env, lpCache->clazz, "lpstrFilter", "I");
	lpCache->lpstrCustomFilter = (*env)->GetFieldID(env, lpCache->clazz, "lpstrCustomFilter", "I");
	lpCache->nMaxCustFilter = (*env)->GetFieldID(env, lpCache->clazz, "nMaxCustFilter", "I");
	lpCache->nFilterIndex = (*env)->GetFieldID(env, lpCache->clazz, "nFilterIndex", "I");
	lpCache->lpstrFile = (*env)->GetFieldID(env, lpCache->clazz, "lpstrFile", "I");
	lpCache->nMaxFile = (*env)->GetFieldID(env, lpCache->clazz, "nMaxFile", "I");
	lpCache->lpstrFileTitle = (*env)->GetFieldID(env, lpCache->clazz, "lpstrFileTitle", "I");
	lpCache->nMaxFileTitle = (*env)->GetFieldID(env, lpCache->clazz, "nMaxFileTitle", "I");
	lpCache->lpstrInitialDir = (*env)->GetFieldID(env, lpCache->clazz, "lpstrInitialDir", "I");
	lpCache->lpstrTitle = (*env)->GetFieldID(env, lpCache->clazz, "lpstrTitle", "I");
	lpCache->Flags = (*env)->GetFieldID(env, lpCache->clazz, "Flags", "I");
	lpCache->nFileOffset = (*env)->GetFieldID(env, lpCache->clazz, "nFileOffset", "S");
	lpCache->nFileExtension = (*env)->GetFieldID(env, lpCache->clazz, "nFileExtension", "S");
	lpCache->lpstrDefExt = (*env)->GetFieldID(env, lpCache->clazz, "lpstrDefExt", "I");
	lpCache->lCustData = (*env)->GetFieldID(env, lpCache->clazz, "lCustData", "I");
	lpCache->lpfnHook = (*env)->GetFieldID(env, lpCache->clazz, "lpfnHook", "I");
	lpCache->lpTemplateName = (*env)->GetFieldID(env, lpCache->clazz, "lpTemplateName", "I");
	lpCache->cached = 1;
}

OPENFILENAME* getOPENFILENAMEFields(JNIEnv *env, jobject lpObject, OPENFILENAME *lpStruct, POPENFILENAME_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheOPENFILENAMEFids(env, lpObject, lpCache);
	lpStruct->lStructSize = (*env)->GetIntField(env, lpObject, lpCache->lStructSize);
	lpStruct->hwndOwner = (HWND)(*env)->GetIntField(env, lpObject, lpCache->hwndOwner);
	lpStruct->hInstance = (HINSTANCE)(*env)->GetIntField(env, lpObject, lpCache->hInstance);
	lpStruct->lpstrFilter = (LPCTSTR)(*env)->GetIntField(env, lpObject, lpCache->lpstrFilter);
	lpStruct->lpstrCustomFilter = (LPTSTR)(*env)->GetIntField(env, lpObject, lpCache->lpstrCustomFilter);
	lpStruct->nMaxCustFilter = (*env)->GetIntField(env, lpObject, lpCache->nMaxCustFilter);
	lpStruct->nFilterIndex = (*env)->GetIntField(env, lpObject, lpCache->nFilterIndex);
	lpStruct->lpstrFile = (LPTSTR)(*env)->GetIntField(env, lpObject, lpCache->lpstrFile);
	lpStruct->nMaxFile = (*env)->GetIntField(env, lpObject, lpCache->nMaxFile);
	lpStruct->lpstrFileTitle = (LPTSTR)(*env)->GetIntField(env, lpObject, lpCache->lpstrFileTitle);
	lpStruct->nMaxFileTitle = (*env)->GetIntField(env, lpObject, lpCache->nMaxFileTitle);
	lpStruct->lpstrInitialDir = (LPCTSTR)(*env)->GetIntField(env, lpObject, lpCache->lpstrInitialDir);
	lpStruct->lpstrTitle = (LPCTSTR)(*env)->GetIntField(env, lpObject, lpCache->lpstrTitle);
	lpStruct->Flags = (*env)->GetIntField(env, lpObject, lpCache->Flags);
	lpStruct->nFileOffset = (*env)->GetShortField(env, lpObject, lpCache->nFileOffset);
	lpStruct->nFileExtension = (*env)->GetShortField(env, lpObject, lpCache->nFileExtension);
	lpStruct->lpstrDefExt = (LPCTSTR)(*env)->GetIntField(env, lpObject, lpCache->lpstrDefExt);
	lpStruct->lCustData = (*env)->GetIntField(env, lpObject, lpCache->lCustData);
	lpStruct->lpfnHook = (LPOFNHOOKPROC)(*env)->GetIntField(env, lpObject, lpCache->lpfnHook);
	lpStruct->lpTemplateName = (LPCTSTR)(*env)->GetIntField(env, lpObject, lpCache->lpTemplateName);
	return lpStruct;
}

void setOPENFILENAMEFields(JNIEnv *env, jobject lpObject, OPENFILENAME *lpStruct, POPENFILENAME_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheOPENFILENAMEFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->lStructSize, lpStruct->lStructSize);
	(*env)->SetIntField(env, lpObject, lpCache->hwndOwner, (jint)lpStruct->hwndOwner);
	(*env)->SetIntField(env, lpObject, lpCache->hInstance, (jint)lpStruct->hInstance);
	(*env)->SetIntField(env, lpObject, lpCache->lpstrFilter, (jint)lpStruct->lpstrFilter);
	(*env)->SetIntField(env, lpObject, lpCache->lpstrCustomFilter, (jint)lpStruct->lpstrCustomFilter);
	(*env)->SetIntField(env, lpObject, lpCache->nMaxCustFilter, lpStruct->nMaxCustFilter);
	(*env)->SetIntField(env, lpObject, lpCache->nFilterIndex, lpStruct->nFilterIndex);
	(*env)->SetIntField(env, lpObject, lpCache->lpstrFile, (jint)lpStruct->lpstrFile);
	(*env)->SetIntField(env, lpObject, lpCache->nMaxFile, lpStruct->nMaxFile);
	(*env)->SetIntField(env, lpObject, lpCache->lpstrFileTitle, (jint)lpStruct->lpstrFileTitle);
	(*env)->SetIntField(env, lpObject, lpCache->nMaxFileTitle, lpStruct->nMaxFileTitle);
	(*env)->SetIntField(env, lpObject, lpCache->lpstrInitialDir, (jint)lpStruct->lpstrInitialDir);
	(*env)->SetIntField(env, lpObject, lpCache->lpstrTitle, (jint)lpStruct->lpstrTitle);
	(*env)->SetIntField(env, lpObject, lpCache->Flags, lpStruct->Flags);
	(*env)->SetShortField(env, lpObject, lpCache->nFileOffset, lpStruct->nFileOffset);
	(*env)->SetShortField(env, lpObject, lpCache->nFileExtension, lpStruct->nFileExtension);
	(*env)->SetIntField(env, lpObject, lpCache->lpstrDefExt, (jint)lpStruct->lpstrDefExt);
	(*env)->SetIntField(env, lpObject, lpCache->lCustData, lpStruct->lCustData);
	(*env)->SetIntField(env, lpObject, lpCache->lpfnHook, (jint)lpStruct->lpfnHook);
	(*env)->SetIntField(env, lpObject, lpCache->lpTemplateName, (jint)lpStruct->lpTemplateName);
}

void cacheOSVERSIONINFOFids(JNIEnv *env, jobject lpObject, POSVERSIONINFO_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->dwOSVersionInfoSize = (*env)->GetFieldID(env, lpCache->clazz, "dwOSVersionInfoSize", "I");
	lpCache->dwMajorVersion = (*env)->GetFieldID(env, lpCache->clazz, "dwMajorVersion", "I");
	lpCache->dwMinorVersion = (*env)->GetFieldID(env, lpCache->clazz, "dwMinorVersion", "I");
	lpCache->dwBuildNumber = (*env)->GetFieldID(env, lpCache->clazz, "dwBuildNumber", "I");
	lpCache->dwPlatformId = (*env)->GetFieldID(env, lpCache->clazz, "dwPlatformId", "I");
	lpCache->cached = 1;
}

#ifndef _WIN32_WCE

OSVERSIONINFOA* getOSVERSIONINFOAFields(JNIEnv *env, jobject lpObject, OSVERSIONINFOA *lpStruct, POSVERSIONINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheOSVERSIONINFOFids(env, lpObject, lpCache);
	lpStruct->dwOSVersionInfoSize = (*env)->GetIntField(env, lpObject, lpCache->dwOSVersionInfoSize);
	lpStruct->dwMajorVersion = (*env)->GetIntField(env, lpObject, lpCache->dwMajorVersion);
	lpStruct->dwMinorVersion = (*env)->GetIntField(env, lpObject, lpCache->dwMinorVersion);
	lpStruct->dwBuildNumber = (*env)->GetIntField(env, lpObject, lpCache->dwBuildNumber);
	lpStruct->dwPlatformId = (*env)->GetIntField(env, lpObject, lpCache->dwPlatformId);
	return lpStruct;
}

void setOSVERSIONINFOAFields(JNIEnv *env, jobject lpObject, OSVERSIONINFOA *lpStruct, POSVERSIONINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheOSVERSIONINFOFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->dwOSVersionInfoSize, lpStruct->dwOSVersionInfoSize);
	(*env)->SetIntField(env, lpObject, lpCache->dwMajorVersion, lpStruct->dwMajorVersion);
	(*env)->SetIntField(env, lpObject, lpCache->dwMinorVersion, lpStruct->dwMinorVersion);
	(*env)->SetIntField(env, lpObject, lpCache->dwBuildNumber, lpStruct->dwBuildNumber);
	(*env)->SetIntField(env, lpObject, lpCache->dwPlatformId, lpStruct->dwPlatformId);
}

#endif // _WIN32_WCE


OSVERSIONINFOW* getOSVERSIONINFOWFields(JNIEnv *env, jobject lpObject, OSVERSIONINFOW *lpStruct, POSVERSIONINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheOSVERSIONINFOFids(env, lpObject, lpCache);
	lpStruct->dwOSVersionInfoSize = (*env)->GetIntField(env, lpObject, lpCache->dwOSVersionInfoSize);
	lpStruct->dwMajorVersion = (*env)->GetIntField(env, lpObject, lpCache->dwMajorVersion);
	lpStruct->dwMinorVersion = (*env)->GetIntField(env, lpObject, lpCache->dwMinorVersion);
	lpStruct->dwBuildNumber = (*env)->GetIntField(env, lpObject, lpCache->dwBuildNumber);
	lpStruct->dwPlatformId = (*env)->GetIntField(env, lpObject, lpCache->dwPlatformId);
	return lpStruct;
}

void setOSVERSIONINFOWFields(JNIEnv *env, jobject lpObject, OSVERSIONINFOW *lpStruct, POSVERSIONINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheOSVERSIONINFOFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->dwOSVersionInfoSize, lpStruct->dwOSVersionInfoSize);
	(*env)->SetIntField(env, lpObject, lpCache->dwMajorVersion, lpStruct->dwMajorVersion);
	(*env)->SetIntField(env, lpObject, lpCache->dwMinorVersion, lpStruct->dwMinorVersion);
	(*env)->SetIntField(env, lpObject, lpCache->dwBuildNumber, lpStruct->dwBuildNumber);
	(*env)->SetIntField(env, lpObject, lpCache->dwPlatformId, lpStruct->dwPlatformId);
}

void cachePAINTSTRUCTFids(JNIEnv *env, jobject lpObject, PPAINTSTRUCT_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->hdc = (*env)->GetFieldID(env, lpCache->clazz, "hdc", "I");
	lpCache->fErase = (*env)->GetFieldID(env, lpCache->clazz, "fErase", "Z");
	lpCache->left = (*env)->GetFieldID(env, lpCache->clazz, "left", "I");
	lpCache->top = (*env)->GetFieldID(env, lpCache->clazz, "top", "I");
	lpCache->right = (*env)->GetFieldID(env, lpCache->clazz, "right", "I");
	lpCache->bottom = (*env)->GetFieldID(env, lpCache->clazz, "bottom", "I");
	lpCache->fRestore = (*env)->GetFieldID(env, lpCache->clazz, "fRestore", "Z");
	lpCache->fIncUpdate = (*env)->GetFieldID(env, lpCache->clazz, "fIncUpdate", "Z");
	lpCache->cached = 1;
}

PAINTSTRUCT* getPAINTSTRUCTFields(JNIEnv *env, jobject lpObject, PAINTSTRUCT *lpStruct, PPAINTSTRUCT_FID_CACHE lpCache)
{
	if (!lpCache->cached) cachePAINTSTRUCTFids(env, lpObject, lpCache);
	lpStruct->hdc = (HDC)(*env)->GetIntField(env, lpObject, lpCache->hdc);
	lpStruct->fErase = (*env)->GetBooleanField(env, lpObject, lpCache->fErase);
	lpStruct->rcPaint.left = (*env)->GetIntField(env, lpObject, lpCache->left);
	lpStruct->rcPaint.top = (*env)->GetIntField(env, lpObject, lpCache->top);
	lpStruct->rcPaint.right = (*env)->GetIntField(env, lpObject, lpCache->right);
	lpStruct->rcPaint.bottom = (*env)->GetIntField(env, lpObject, lpCache->bottom);
	lpStruct->fRestore = (*env)->GetBooleanField(env, lpObject, lpCache->fRestore);
	lpStruct->fIncUpdate = (*env)->GetBooleanField(env, lpObject, lpCache->fIncUpdate);
	return lpStruct;
}

void setPAINTSTRUCTFields(JNIEnv *env, jobject lpObject, PAINTSTRUCT *lpStruct, PPAINTSTRUCT_FID_CACHE lpCache)
{
	if (!lpCache->cached) cachePAINTSTRUCTFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->hdc, (jint)lpStruct->hdc);
	(*env)->SetBooleanField(env, lpObject, lpCache->fErase, (jboolean)lpStruct->fErase);
	(*env)->SetIntField(env, lpObject, lpCache->left, lpStruct->rcPaint.left);
	(*env)->SetIntField(env, lpObject, lpCache->top, lpStruct->rcPaint.top);
	(*env)->SetIntField(env, lpObject, lpCache->right, lpStruct->rcPaint.right);
	(*env)->SetIntField(env, lpObject, lpCache->bottom, lpStruct->rcPaint.bottom);
	(*env)->SetBooleanField(env, lpObject, lpCache->fRestore, (jboolean)lpStruct->fRestore);
	(*env)->SetBooleanField(env, lpObject, lpCache->fIncUpdate, (jboolean)lpStruct->fIncUpdate);
}

void cachePOINTFids(JNIEnv *env, jobject lpObject, PPOINT_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->x = (*env)->GetFieldID(env, lpCache->clazz, "x", "I");
	lpCache->y = (*env)->GetFieldID(env, lpCache->clazz, "y", "I");
	lpCache->cached = 1;
}

POINT* getPOINTFields(JNIEnv *env, jobject lpObject, POINT *lpStruct, PPOINT_FID_CACHE lpCache)
{
	if (!lpCache->cached) cachePOINTFids(env, lpObject, lpCache);
	lpStruct->x = (*env)->GetIntField(env, lpObject, lpCache->x);
	lpStruct->y = (*env)->GetIntField(env, lpObject, lpCache->y);
	return lpStruct;
}

void setPOINTFields(JNIEnv *env, jobject lpObject, POINT *lpStruct, PPOINT_FID_CACHE lpCache)
{
	if (!lpCache->cached) cachePOINTFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->x, lpStruct->x);
	(*env)->SetIntField(env, lpObject, lpCache->y, lpStruct->y);
}

#ifndef _WIN32_WCE

void cachePRINTDLGFids(JNIEnv *env, jobject lpObject, PPRINTDLG_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->lStructSize = (*env)->GetFieldID(env, lpCache->clazz, "lStructSize", "I");
	lpCache->hwndOwner = (*env)->GetFieldID(env, lpCache->clazz, "hwndOwner", "I");
	lpCache->hDevMode = (*env)->GetFieldID(env, lpCache->clazz, "hDevMode", "I");
	lpCache->hDevNames = (*env)->GetFieldID(env, lpCache->clazz, "hDevNames", "I");
	lpCache->hDC = (*env)->GetFieldID(env, lpCache->clazz, "hDC", "I");
	lpCache->Flags = (*env)->GetFieldID(env, lpCache->clazz, "Flags", "I");
	lpCache->nFromPage = (*env)->GetFieldID(env, lpCache->clazz, "nFromPage", "S");
	lpCache->nToPage = (*env)->GetFieldID(env, lpCache->clazz, "nToPage", "S");
	lpCache->nMinPage = (*env)->GetFieldID(env, lpCache->clazz, "nMinPage", "S");
	lpCache->nMaxPage = (*env)->GetFieldID(env, lpCache->clazz, "nMaxPage", "S");
	lpCache->nCopies = (*env)->GetFieldID(env, lpCache->clazz, "nCopies", "S");
	lpCache->hInstance = (*env)->GetFieldID(env, lpCache->clazz, "hInstance", "I");
	lpCache->lCustData = (*env)->GetFieldID(env, lpCache->clazz, "lCustData", "I");
	lpCache->lpfnPrintHook = (*env)->GetFieldID(env, lpCache->clazz, "lpfnPrintHook", "I");
	lpCache->lpfnSetupHook = (*env)->GetFieldID(env, lpCache->clazz, "lpfnSetupHook", "I");
	lpCache->lpPrintTemplateName = (*env)->GetFieldID(env, lpCache->clazz, "lpPrintTemplateName", "I");
	lpCache->lpSetupTemplateName = (*env)->GetFieldID(env, lpCache->clazz, "lpSetupTemplateName", "I");
	lpCache->hPrintTemplate = (*env)->GetFieldID(env, lpCache->clazz, "hPrintTemplate", "I");
	lpCache->hSetupTemplate = (*env)->GetFieldID(env, lpCache->clazz, "hSetupTemplate", "I");
	lpCache->cached = 1;
}

PRINTDLG* getPRINTDLGFields(JNIEnv *env, jobject lpObject, PRINTDLG *lpStruct, PPRINTDLG_FID_CACHE lpCache)
{
	if (!lpCache->cached) cachePRINTDLGFids(env, lpObject, lpCache);
	lpStruct->lStructSize = (*env)->GetIntField(env, lpObject, lpCache->lStructSize);
	lpStruct->hwndOwner = (HWND)(*env)->GetIntField(env, lpObject, lpCache->hwndOwner);
	lpStruct->hDevMode = (HGLOBAL)(*env)->GetIntField(env, lpObject, lpCache->hDevMode);
	lpStruct->hDevNames = (HGLOBAL)(*env)->GetIntField(env, lpObject, lpCache->hDevNames);
	lpStruct->hDC = (HDC)(*env)->GetIntField(env, lpObject, lpCache->hDC);
	lpStruct->Flags = (*env)->GetIntField(env, lpObject, lpCache->Flags);
	lpStruct->nFromPage = (*env)->GetShortField(env, lpObject, lpCache->nFromPage);
	lpStruct->nToPage = (*env)->GetShortField(env, lpObject, lpCache->nToPage);
	lpStruct->nMinPage = (*env)->GetShortField(env, lpObject, lpCache->nMinPage);
	lpStruct->nMaxPage = (*env)->GetShortField(env, lpObject, lpCache->nMaxPage);
	lpStruct->nCopies = (*env)->GetShortField(env, lpObject, lpCache->nCopies);
	lpStruct->hInstance = (HINSTANCE)(*env)->GetIntField(env, lpObject, lpCache->hInstance);
	lpStruct->lCustData = (*env)->GetIntField(env, lpObject, lpCache->lCustData);
	lpStruct->lpfnPrintHook = (LPPRINTHOOKPROC)(*env)->GetIntField(env, lpObject, lpCache->lpfnPrintHook);
	lpStruct->lpfnSetupHook = (LPPRINTHOOKPROC)(*env)->GetIntField(env, lpObject, lpCache->lpfnSetupHook);
	lpStruct->lpPrintTemplateName = (LPCTSTR)(*env)->GetIntField(env, lpObject, lpCache->lpPrintTemplateName);
	lpStruct->lpSetupTemplateName = (LPCTSTR)(*env)->GetIntField(env, lpObject, lpCache->lpSetupTemplateName);
	lpStruct->hPrintTemplate = (HGLOBAL)(*env)->GetIntField(env, lpObject, lpCache->hPrintTemplate);
	lpStruct->hSetupTemplate = (HGLOBAL)(*env)->GetIntField(env, lpObject, lpCache->hSetupTemplate);
	return lpStruct;
}

void setPRINTDLGFields(JNIEnv *env, jobject lpObject, PRINTDLG *lpStruct, PPRINTDLG_FID_CACHE lpCache)
{
	if (!lpCache->cached) cachePRINTDLGFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->lStructSize, lpStruct->lStructSize);
	(*env)->SetIntField(env, lpObject, lpCache->hwndOwner, (jint)lpStruct->hwndOwner);
	(*env)->SetIntField(env, lpObject, lpCache->hDevMode, (jint)lpStruct->hDevMode);
	(*env)->SetIntField(env, lpObject, lpCache->hDevNames, (jint)lpStruct->hDevNames);
	(*env)->SetIntField(env, lpObject, lpCache->hDC, (jint)lpStruct->hDC);
	(*env)->SetIntField(env, lpObject, lpCache->Flags, lpStruct->Flags);
	(*env)->SetShortField(env, lpObject, lpCache->nFromPage, lpStruct->nFromPage);
	(*env)->SetShortField(env, lpObject, lpCache->nToPage, lpStruct->nToPage);
	(*env)->SetShortField(env, lpObject, lpCache->nMinPage, lpStruct->nMinPage);
	(*env)->SetShortField(env, lpObject, lpCache->nMaxPage, lpStruct->nMaxPage);
	(*env)->SetShortField(env, lpObject, lpCache->nCopies, lpStruct->nCopies);
	(*env)->SetIntField(env, lpObject, lpCache->hInstance, (jint)lpStruct->hInstance);
	(*env)->SetIntField(env, lpObject, lpCache->lCustData, lpStruct->lCustData);
	(*env)->SetIntField(env, lpObject, lpCache->lpfnPrintHook, (jint)lpStruct->lpfnPrintHook);
	(*env)->SetIntField(env, lpObject, lpCache->lpfnSetupHook, (jint)lpStruct->lpfnSetupHook);
	(*env)->SetIntField(env, lpObject, lpCache->lpPrintTemplateName, (jint)lpStruct->lpPrintTemplateName);
	(*env)->SetIntField(env, lpObject, lpCache->lpSetupTemplateName, (jint)lpStruct->lpSetupTemplateName);
	(*env)->SetIntField(env, lpObject, lpCache->hPrintTemplate, (jint)lpStruct->hPrintTemplate);
	(*env)->SetIntField(env, lpObject, lpCache->hSetupTemplate, (jint)lpStruct->hSetupTemplate);
}

#endif // _WIN32_WCE

void cacheREBARBANDINFOFids(JNIEnv *env, jobject lpObject, PREBARBANDINFO_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->cbSize = (*env)->GetFieldID(env, lpCache->clazz, "cbSize", "I");
	lpCache->fMask = (*env)->GetFieldID(env, lpCache->clazz, "fMask", "I");
	lpCache->fStyle = (*env)->GetFieldID(env, lpCache->clazz, "fStyle", "I");
	lpCache->clrFore = (*env)->GetFieldID(env, lpCache->clazz, "clrFore", "I");
	lpCache->clrBack = (*env)->GetFieldID(env, lpCache->clazz, "clrBack", "I");
	lpCache->lpText = (*env)->GetFieldID(env, lpCache->clazz, "lpText", "I");
	lpCache->cch = (*env)->GetFieldID(env, lpCache->clazz, "cch", "I");
	lpCache->iImage = (*env)->GetFieldID(env, lpCache->clazz, "iImage", "I");
	lpCache->hwndChild = (*env)->GetFieldID(env, lpCache->clazz, "hwndChild", "I");
	lpCache->cxMinChild = (*env)->GetFieldID(env, lpCache->clazz, "cxMinChild", "I");
	lpCache->cyMinChild = (*env)->GetFieldID(env, lpCache->clazz, "cyMinChild", "I");
	lpCache->cx = (*env)->GetFieldID(env, lpCache->clazz, "cx", "I");
	lpCache->hbmBack = (*env)->GetFieldID(env, lpCache->clazz, "hbmBack", "I");
	lpCache->wID = (*env)->GetFieldID(env, lpCache->clazz, "wID", "I");
#ifndef _WIN32_WCE
	lpCache->cyChild = (*env)->GetFieldID(env, lpCache->clazz, "cyChild", "I");
	lpCache->cyMaxChild = (*env)->GetFieldID(env, lpCache->clazz, "cyMaxChild", "I");
	lpCache->cyIntegral = (*env)->GetFieldID(env, lpCache->clazz, "cyIntegral", "I");
	lpCache->cxIdeal = (*env)->GetFieldID(env, lpCache->clazz, "cxIdeal", "I");
	lpCache->lParam = (*env)->GetFieldID(env, lpCache->clazz, "lParam", "I");
	lpCache->cxHeader = (*env)->GetFieldID(env, lpCache->clazz, "cxHeader", "I");
#endif // _WIN32_WCE
	lpCache->cached = 1;
}

REBARBANDINFO* getREBARBANDINFOFields(JNIEnv *env, jobject lpObject, REBARBANDINFO *lpStruct, PREBARBANDINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheREBARBANDINFOFids(env, lpObject, lpCache);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, lpCache->cbSize);
	lpStruct->fMask = (*env)->GetIntField(env, lpObject, lpCache->fMask);
	lpStruct->fStyle = (*env)->GetIntField(env, lpObject, lpCache->fStyle);
	lpStruct->clrFore = (*env)->GetIntField(env, lpObject, lpCache->clrFore);
	lpStruct->clrBack = (*env)->GetIntField(env, lpObject, lpCache->clrBack);
	lpStruct->lpText = (LPTSTR)(*env)->GetIntField(env, lpObject, lpCache->lpText);
	lpStruct->cch = (*env)->GetIntField(env, lpObject, lpCache->cch);
	lpStruct->iImage = (*env)->GetIntField(env, lpObject, lpCache->iImage);
	lpStruct->hwndChild = (HWND)(*env)->GetIntField(env, lpObject, lpCache->hwndChild);
	lpStruct->cxMinChild = (*env)->GetIntField(env, lpObject, lpCache->cxMinChild);
	lpStruct->cyMinChild = (*env)->GetIntField(env, lpObject, lpCache->cyMinChild);
	lpStruct->cx = (*env)->GetIntField(env, lpObject, lpCache->cx);
	lpStruct->hbmBack = (HBITMAP)(*env)->GetIntField(env, lpObject, lpCache->hbmBack);
	lpStruct->wID = (*env)->GetIntField(env, lpObject, lpCache->wID);
#ifndef _WIN32_WCE
	lpStruct->cyChild = (*env)->GetIntField(env, lpObject, lpCache->cyChild);
	lpStruct->cyMaxChild = (*env)->GetIntField(env, lpObject, lpCache->cyMaxChild);
	lpStruct->cyIntegral = (*env)->GetIntField(env, lpObject, lpCache->cyIntegral);
	lpStruct->cxIdeal = (*env)->GetIntField(env, lpObject, lpCache->cxIdeal);
	lpStruct->lParam = (*env)->GetIntField(env, lpObject, lpCache->lParam);
	lpStruct->cxHeader = (*env)->GetIntField(env, lpObject, lpCache->cxHeader);
#endif // _WIN32_WCE
	return lpStruct;
}

void setREBARBANDINFOFields(JNIEnv *env, jobject lpObject, REBARBANDINFO *lpStruct, PREBARBANDINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheREBARBANDINFOFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->cbSize, lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, lpCache->fMask, lpStruct->fMask);
	(*env)->SetIntField(env, lpObject, lpCache->fStyle, lpStruct->fStyle);
	(*env)->SetIntField(env, lpObject, lpCache->clrFore, lpStruct->clrFore);
	(*env)->SetIntField(env, lpObject, lpCache->clrBack, lpStruct->clrBack);
	(*env)->SetIntField(env, lpObject, lpCache->lpText, (jint)lpStruct->lpText);
	(*env)->SetIntField(env, lpObject, lpCache->cch, lpStruct->cch);
	(*env)->SetIntField(env, lpObject, lpCache->iImage, lpStruct->iImage);
	(*env)->SetIntField(env, lpObject, lpCache->hwndChild, (jint)lpStruct->hwndChild);
	(*env)->SetIntField(env, lpObject, lpCache->cxMinChild, lpStruct->cxMinChild);
	(*env)->SetIntField(env, lpObject, lpCache->cyMinChild, lpStruct->cyMinChild);
	(*env)->SetIntField(env, lpObject, lpCache->cx, lpStruct->cx);
	(*env)->SetIntField(env, lpObject, lpCache->hbmBack, (jint)lpStruct->hbmBack);
	(*env)->SetIntField(env, lpObject, lpCache->wID, lpStruct->wID);
#ifndef _WIN32_WCE
	(*env)->SetIntField(env, lpObject, lpCache->cyChild, lpStruct->cyChild);
	(*env)->SetIntField(env, lpObject, lpCache->cyMaxChild, lpStruct->cyMaxChild);
	(*env)->SetIntField(env, lpObject, lpCache->cyIntegral, lpStruct->cyIntegral);
	(*env)->SetIntField(env, lpObject, lpCache->cxIdeal, lpStruct->cxIdeal);
	(*env)->SetIntField(env, lpObject, lpCache->lParam, lpStruct->lParam);
	(*env)->SetIntField(env, lpObject, lpCache->cxHeader, lpStruct->cxHeader);
#endif // _WIN32_WCE
}

void cacheRECTFids(JNIEnv *env, jobject lpObject, PRECT_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->left = (*env)->GetFieldID(env, lpCache->clazz, "left", "I");
	lpCache->top = (*env)->GetFieldID(env, lpCache->clazz, "top", "I");
	lpCache->right = (*env)->GetFieldID(env, lpCache->clazz, "right", "I");
	lpCache->bottom = (*env)->GetFieldID(env, lpCache->clazz, "bottom", "I");
	lpCache->cached = 1;
}

RECT* getRECTFields(JNIEnv *env, jobject lpObject, RECT *lpStruct, PRECT_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheRECTFids(env, lpObject, lpCache);
	lpStruct->left = (*env)->GetIntField(env, lpObject, lpCache->left);
	lpStruct->top = (*env)->GetIntField(env, lpObject, lpCache->top);
	lpStruct->right = (*env)->GetIntField(env, lpObject, lpCache->right);
	lpStruct->bottom = (*env)->GetIntField(env, lpObject, lpCache->bottom);
	return lpStruct;
}

void setRECTFields(JNIEnv *env, jobject lpObject, RECT *lpStruct, PRECT_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheRECTFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->left, lpStruct->left);
	(*env)->SetIntField(env, lpObject, lpCache->top, lpStruct->top);
	(*env)->SetIntField(env, lpObject, lpCache->right, lpStruct->right);
	(*env)->SetIntField(env, lpObject, lpCache->bottom, lpStruct->bottom);
}

void cacheSCROLLINFOFids(JNIEnv *env, jobject lpObject, PSCROLLINFO_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->cbSize = (*env)->GetFieldID(env, lpCache->clazz, "cbSize", "I");
	lpCache->fMask = (*env)->GetFieldID(env, lpCache->clazz, "fMask", "I");
	lpCache->nMin = (*env)->GetFieldID(env, lpCache->clazz, "nMin", "I");
	lpCache->nMax = (*env)->GetFieldID(env, lpCache->clazz, "nMax", "I");
	lpCache->nPage = (*env)->GetFieldID(env, lpCache->clazz, "nPage", "I");
	lpCache->nPos = (*env)->GetFieldID(env, lpCache->clazz, "nPos", "I");
	lpCache->nTrackPos = (*env)->GetFieldID(env, lpCache->clazz, "nTrackPos", "I");
	lpCache->cached = 1;
}

SCROLLINFO* getSCROLLINFOFields(JNIEnv *env, jobject lpObject, SCROLLINFO *lpStruct, PSCROLLINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheSCROLLINFOFids(env, lpObject, lpCache);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, lpCache->cbSize);
	lpStruct->fMask = (*env)->GetIntField(env, lpObject, lpCache->fMask);
	lpStruct->nMin = (*env)->GetIntField(env, lpObject, lpCache->nMin);
	lpStruct->nMax = (*env)->GetIntField(env, lpObject, lpCache->nMax);
	lpStruct->nPage = (*env)->GetIntField(env, lpObject, lpCache->nPage);
	lpStruct->nPos = (*env)->GetIntField(env, lpObject, lpCache->nPos);
	lpStruct->nTrackPos = (*env)->GetIntField(env, lpObject, lpCache->nTrackPos);
	return lpStruct;
}

void setSCROLLINFOFields(JNIEnv *env, jobject lpObject, SCROLLINFO *lpStruct, PSCROLLINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheSCROLLINFOFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->cbSize, lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, lpCache->fMask, lpStruct->fMask);
	(*env)->SetIntField(env, lpObject, lpCache->nMin, lpStruct->nMin);
	(*env)->SetIntField(env, lpObject, lpCache->nMax, lpStruct->nMax);
	(*env)->SetIntField(env, lpObject, lpCache->nPage, lpStruct->nPage);
	(*env)->SetIntField(env, lpObject, lpCache->nPos, lpStruct->nPos);
	(*env)->SetIntField(env, lpObject, lpCache->nTrackPos, lpStruct->nTrackPos);
}

void cacheSHELLEXECUTEINFOFids(JNIEnv *env, jobject lpObject, PSHELLEXECUTEINFO_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->cbSize = (*env)->GetFieldID(env, lpCache->clazz, "cbSize", "I");
	lpCache->fMask = (*env)->GetFieldID(env, lpCache->clazz, "fMask", "I");
	lpCache->hwnd = (*env)->GetFieldID(env, lpCache->clazz, "hwnd", "I");
	lpCache->lpVerb = (*env)->GetFieldID(env, lpCache->clazz, "lpVerb", "I");
	lpCache->lpFile = (*env)->GetFieldID(env, lpCache->clazz, "lpFile", "I");
	lpCache->lpParameters = (*env)->GetFieldID(env, lpCache->clazz, "lpParameters", "I");
	lpCache->lpDirectory = (*env)->GetFieldID(env, lpCache->clazz, "lpDirectory", "I");
	lpCache->nShow = (*env)->GetFieldID(env, lpCache->clazz, "nShow", "I");
	lpCache->hInstApp = (*env)->GetFieldID(env, lpCache->clazz, "hInstApp", "I");
	lpCache->lpIDList = (*env)->GetFieldID(env, lpCache->clazz, "lpIDList", "I");
	lpCache->lpClass = (*env)->GetFieldID(env, lpCache->clazz, "lpClass", "I");
	lpCache->hkeyClass = (*env)->GetFieldID(env, lpCache->clazz, "hkeyClass", "I");
	lpCache->dwHotKey = (*env)->GetFieldID(env, lpCache->clazz, "dwHotKey", "I");
	lpCache->hIcon = (*env)->GetFieldID(env, lpCache->clazz, "hIcon", "I");
	lpCache->hProcess = (*env)->GetFieldID(env, lpCache->clazz, "hProcess", "I");
	lpCache->cached = 1;
}

SHELLEXECUTEINFO* getSHELLEXECUTEINFOFields(JNIEnv *env, jobject lpObject, SHELLEXECUTEINFO *lpStruct, PSHELLEXECUTEINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheSHELLEXECUTEINFOFids(env, lpObject, lpCache);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, lpCache->cbSize);
	lpStruct->fMask = (*env)->GetIntField(env, lpObject, lpCache->fMask);
	lpStruct->hwnd = (HWND)(*env)->GetIntField(env, lpObject, lpCache->hwnd);
	lpStruct->lpVerb = (LPCTSTR)(*env)->GetIntField(env, lpObject, lpCache->lpVerb);
	lpStruct->lpFile = (LPCTSTR)(*env)->GetIntField(env, lpObject, lpCache->lpFile);
	lpStruct->lpParameters = (LPCTSTR)(*env)->GetIntField(env, lpObject, lpCache->lpParameters);
	lpStruct->lpDirectory = (LPCTSTR)(*env)->GetIntField(env, lpObject, lpCache->lpDirectory);
	lpStruct->nShow = (*env)->GetIntField(env, lpObject, lpCache->nShow);
	lpStruct->hInstApp = (HINSTANCE)(*env)->GetIntField(env, lpObject, lpCache->hInstApp);
	lpStruct->lpIDList = (LPVOID)(*env)->GetIntField(env, lpObject, lpCache->lpIDList);
	lpStruct->lpClass = (LPCTSTR)(*env)->GetIntField(env, lpObject, lpCache->lpClass);
	lpStruct->hkeyClass = (HKEY)(*env)->GetIntField(env, lpObject, lpCache->hkeyClass);
	lpStruct->dwHotKey = (*env)->GetIntField(env, lpObject, lpCache->dwHotKey);
	lpStruct->hIcon = (HANDLE)(*env)->GetIntField(env, lpObject, lpCache->hIcon);
	lpStruct->hProcess = (HANDLE)(*env)->GetIntField(env, lpObject, lpCache->hProcess);
	return lpStruct;
}

void setSHELLEXECUTEINFOFields(JNIEnv *env, jobject lpObject, SHELLEXECUTEINFO *lpStruct, PSHELLEXECUTEINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheSHELLEXECUTEINFOFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->cbSize, lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, lpCache->fMask, lpStruct->fMask);
	(*env)->SetIntField(env, lpObject, lpCache->hwnd, (jint)lpStruct->hwnd);
	(*env)->SetIntField(env, lpObject, lpCache->lpVerb, (jint)lpStruct->lpVerb);
	(*env)->SetIntField(env, lpObject, lpCache->lpFile, (jint)lpStruct->lpFile);
	(*env)->SetIntField(env, lpObject, lpCache->lpParameters, (jint)lpStruct->lpParameters);
	(*env)->SetIntField(env, lpObject, lpCache->lpDirectory, (jint)lpStruct->lpDirectory);
	(*env)->SetIntField(env, lpObject, lpCache->nShow, lpStruct->nShow);
	(*env)->SetIntField(env, lpObject, lpCache->hInstApp, (jint)lpStruct->hInstApp);
	(*env)->SetIntField(env, lpObject, lpCache->lpIDList, (jint)lpStruct->lpIDList);
	(*env)->SetIntField(env, lpObject, lpCache->lpClass, (jint)lpStruct->lpClass);
	(*env)->SetIntField(env, lpObject, lpCache->hkeyClass, (jint)lpStruct->hkeyClass);
	(*env)->SetIntField(env, lpObject, lpCache->dwHotKey, lpStruct->dwHotKey);
	(*env)->SetIntField(env, lpObject, lpCache->hIcon, (jint)lpStruct->hIcon);
	(*env)->SetIntField(env, lpObject, lpCache->hProcess, (jint)lpStruct->hProcess);
}

void cacheSIZEFids(JNIEnv *env, jobject lpObject, PSIZE_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->cx = (*env)->GetFieldID(env, lpCache->clazz, "cx", "I");
	lpCache->cy = (*env)->GetFieldID(env, lpCache->clazz, "cy", "I");
	lpCache->cached = 1;
}

SIZE* getSIZEFields(JNIEnv *env, jobject lpObject, SIZE *lpStruct, PSIZE_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheSIZEFids(env, lpObject, lpCache);
	lpStruct->cx = (*env)->GetIntField(env, lpObject, lpCache->cx);
	lpStruct->cy = (*env)->GetIntField(env, lpObject, lpCache->cy);
	return lpStruct;
}

void setSIZEFields(JNIEnv *env, jobject lpObject, SIZE *lpStruct, PSIZE_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheSIZEFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->cx, lpStruct->cx);
	(*env)->SetIntField(env, lpObject, lpCache->cy, lpStruct->cy);
}

void cacheTBBUTTONFids(JNIEnv *env, jobject lpObject, PTBBUTTON_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->iBitmap = (*env)->GetFieldID(env, lpCache->clazz, "iBitmap", "I");
	lpCache->idCommand = (*env)->GetFieldID(env, lpCache->clazz, "idCommand", "I");
	lpCache->fsState = (*env)->GetFieldID(env, lpCache->clazz, "fsState", "B");
	lpCache->fsStyle = (*env)->GetFieldID(env, lpCache->clazz, "fsStyle", "B");
	lpCache->dwData = (*env)->GetFieldID(env, lpCache->clazz, "dwData", "I");
	lpCache->iString = (*env)->GetFieldID(env, lpCache->clazz, "iString", "I");
	lpCache->cached = 1;
}

TBBUTTON* getTBBUTTONFields(JNIEnv *env, jobject lpObject, TBBUTTON *lpStruct, PTBBUTTON_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheTBBUTTONFids(env, lpObject, lpCache);
	lpStruct->iBitmap = (*env)->GetIntField(env, lpObject, lpCache->iBitmap);
	lpStruct->idCommand = (*env)->GetIntField(env, lpObject, lpCache->idCommand);
	lpStruct->fsState = (*env)->GetByteField(env, lpObject, lpCache->fsState);
	lpStruct->fsStyle = (*env)->GetByteField(env, lpObject, lpCache->fsStyle);
	lpStruct->dwData = (*env)->GetIntField(env, lpObject, lpCache->dwData);
	lpStruct->iString = (*env)->GetIntField(env, lpObject, lpCache->iString);
	return lpStruct;
}

void setTBBUTTONFields(JNIEnv *env, jobject lpObject, TBBUTTON *lpStruct, PTBBUTTON_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheTBBUTTONFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->iBitmap, lpStruct->iBitmap);
	(*env)->SetIntField(env, lpObject, lpCache->idCommand, lpStruct->idCommand);
	(*env)->SetByteField(env, lpObject, lpCache->fsState, lpStruct->fsState);
	(*env)->SetByteField(env, lpObject, lpCache->fsStyle, lpStruct->fsStyle);
	(*env)->SetIntField(env, lpObject, lpCache->dwData, lpStruct->dwData);
	(*env)->SetIntField(env, lpObject, lpCache->iString, lpStruct->iString);
}

void cacheTBBUTTONINFOFids(JNIEnv *env, jobject lpObject, PTBBUTTONINFO_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->cbSize = (*env)->GetFieldID(env, lpCache->clazz, "cbSize", "I");
	lpCache->dwMask = (*env)->GetFieldID(env, lpCache->clazz, "dwMask", "I");
	lpCache->idCommand = (*env)->GetFieldID(env, lpCache->clazz, "idCommand", "I");
	lpCache->iImage = (*env)->GetFieldID(env, lpCache->clazz, "iImage", "I");
	lpCache->fsState = (*env)->GetFieldID(env, lpCache->clazz, "fsState", "B");
	lpCache->fsStyle = (*env)->GetFieldID(env, lpCache->clazz, "fsStyle", "B");
	lpCache->cx = (*env)->GetFieldID(env, lpCache->clazz, "cx", "S");
	lpCache->lParam = (*env)->GetFieldID(env, lpCache->clazz, "lParam", "I");
	lpCache->pszText = (*env)->GetFieldID(env, lpCache->clazz, "pszText", "I");
	lpCache->cchText = (*env)->GetFieldID(env, lpCache->clazz, "cchText", "I");
	lpCache->cached = 1;
}

TBBUTTONINFO* getTBBUTTONINFOFields(JNIEnv *env, jobject lpObject, TBBUTTONINFO *lpStruct, PTBBUTTONINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheTBBUTTONINFOFids(env, lpObject, lpCache);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, lpCache->cbSize);
	lpStruct->dwMask = (*env)->GetIntField(env, lpObject, lpCache->dwMask);
	lpStruct->idCommand = (*env)->GetIntField(env, lpObject, lpCache->idCommand);
	lpStruct->iImage = (*env)->GetIntField(env, lpObject, lpCache->iImage);
	lpStruct->fsState = (*env)->GetByteField(env, lpObject, lpCache->fsState);
	lpStruct->fsStyle = (*env)->GetByteField(env, lpObject, lpCache->fsStyle);
	lpStruct->cx = (*env)->GetShortField(env, lpObject, lpCache->cx);
	lpStruct->lParam = (*env)->GetIntField(env, lpObject, lpCache->lParam);
	lpStruct->pszText = (LPTSTR)(*env)->GetIntField(env, lpObject, lpCache->pszText);
	lpStruct->cchText = (*env)->GetIntField(env, lpObject, lpCache->cchText);
	return lpStruct;
}

void setTBBUTTONINFOFields(JNIEnv *env, jobject lpObject, TBBUTTONINFO *lpStruct, PTBBUTTONINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheTBBUTTONINFOFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->cbSize, lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, lpCache->dwMask, lpStruct->dwMask);
	(*env)->SetIntField(env, lpObject, lpCache->idCommand, lpStruct->idCommand);
	(*env)->SetIntField(env, lpObject, lpCache->iImage, lpStruct->iImage);
	(*env)->SetByteField(env, lpObject, lpCache->fsState, lpStruct->fsState);
	(*env)->SetByteField(env, lpObject, lpCache->fsStyle, lpStruct->fsStyle);
	(*env)->SetShortField(env, lpObject, lpCache->cx, lpStruct->cx);
	(*env)->SetIntField(env, lpObject, lpCache->lParam, lpStruct->lParam);
	(*env)->SetIntField(env, lpObject, lpCache->pszText, (jint)lpStruct->pszText);
	(*env)->SetIntField(env, lpObject, lpCache->cchText, lpStruct->cchText);
}

void cacheTCITEMFids(JNIEnv *env, jobject lpObject, PTCITEM_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->mask = (*env)->GetFieldID(env, lpCache->clazz, "mask", "I");
	lpCache->dwState = (*env)->GetFieldID(env, lpCache->clazz, "dwState", "I");
	lpCache->dwStateMask = (*env)->GetFieldID(env, lpCache->clazz, "dwStateMask", "I");
	lpCache->pszText = (*env)->GetFieldID(env, lpCache->clazz, "pszText", "I");
	lpCache->cchTextMax = (*env)->GetFieldID(env, lpCache->clazz, "cchTextMax", "I");
	lpCache->iImage = (*env)->GetFieldID(env, lpCache->clazz, "iImage", "I");
	lpCache->lParam = (*env)->GetFieldID(env, lpCache->clazz, "lParam", "I");
	lpCache->cached = 1;
}

TCITEM* getTCITEMFields(JNIEnv *env, jobject lpObject, TCITEM *lpStruct, PTCITEM_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheTCITEMFids(env, lpObject, lpCache);
	lpStruct->mask = (*env)->GetIntField(env, lpObject, lpCache->mask);
	lpStruct->dwState = (*env)->GetIntField(env, lpObject, lpCache->dwState);
	lpStruct->dwStateMask = (*env)->GetIntField(env, lpObject, lpCache->dwStateMask);
	lpStruct->pszText = (LPTSTR)(*env)->GetIntField(env, lpObject, lpCache->pszText);
	lpStruct->cchTextMax = (*env)->GetIntField(env, lpObject, lpCache->cchTextMax);
	lpStruct->iImage = (*env)->GetIntField(env, lpObject, lpCache->iImage);
	lpStruct->lParam = (*env)->GetIntField(env, lpObject, lpCache->lParam);
	return lpStruct;
}

void setTCITEMFields(JNIEnv *env, jobject lpObject, TCITEM *lpStruct, PTCITEM_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheTCITEMFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->mask, lpStruct->mask);
	(*env)->SetIntField(env, lpObject, lpCache->dwState, lpStruct->dwState);
	(*env)->SetIntField(env, lpObject, lpCache->dwStateMask, lpStruct->dwStateMask);
	(*env)->SetIntField(env, lpObject, lpCache->pszText, (jint)lpStruct->pszText);
	(*env)->SetIntField(env, lpObject, lpCache->cchTextMax, lpStruct->cchTextMax);
	(*env)->SetIntField(env, lpObject, lpCache->iImage, lpStruct->iImage);
	(*env)->SetIntField(env, lpObject, lpCache->lParam, lpStruct->lParam);
}

void cacheTEXTMETRICFids(JNIEnv *env, jobject lpObject, PTEXTMETRIC_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->tmHeight = (*env)->GetFieldID(env, lpCache->clazz, "tmHeight", "I");
	lpCache->tmAscent = (*env)->GetFieldID(env, lpCache->clazz, "tmAscent", "I");
	lpCache->tmDescent = (*env)->GetFieldID(env, lpCache->clazz, "tmDescent", "I");
	lpCache->tmInternalLeading = (*env)->GetFieldID(env, lpCache->clazz, "tmInternalLeading", "I");
	lpCache->tmExternalLeading = (*env)->GetFieldID(env, lpCache->clazz, "tmExternalLeading", "I");
	lpCache->tmAveCharWidth = (*env)->GetFieldID(env, lpCache->clazz, "tmAveCharWidth", "I");
	lpCache->tmMaxCharWidth = (*env)->GetFieldID(env, lpCache->clazz, "tmMaxCharWidth", "I");
	lpCache->tmWeight = (*env)->GetFieldID(env, lpCache->clazz, "tmWeight", "I");
	lpCache->tmOverhang = (*env)->GetFieldID(env, lpCache->clazz, "tmOverhang", "I");
	lpCache->tmDigitizedAspectX = (*env)->GetFieldID(env, lpCache->clazz, "tmDigitizedAspectX", "I");
	lpCache->tmDigitizedAspectY = (*env)->GetFieldID(env, lpCache->clazz, "tmDigitizedAspectY", "I");
	lpCache->tmItalic = (*env)->GetFieldID(env, lpCache->clazz, "tmItalic", "B");
	lpCache->tmUnderlined = (*env)->GetFieldID(env, lpCache->clazz, "tmUnderlined", "B");
	lpCache->tmStruckOut = (*env)->GetFieldID(env, lpCache->clazz, "tmStruckOut", "B");
	lpCache->tmPitchAndFamily = (*env)->GetFieldID(env, lpCache->clazz, "tmPitchAndFamily", "B");
	lpCache->tmCharSet = (*env)->GetFieldID(env, lpCache->clazz, "tmCharSet", "B");
	lpCache->cached = 1;
}

#ifndef _WIN32_WCE

TEXTMETRICA* getTEXTMETRICAFields(JNIEnv *env, jobject lpObject, TEXTMETRICA *lpStruct, PTEXTMETRIC_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheTEXTMETRICFids(env, lpObject, lpCache);
	lpStruct->tmHeight = (*env)->GetIntField(env, lpObject, lpCache->tmHeight);
	lpStruct->tmAscent = (*env)->GetIntField(env, lpObject, lpCache->tmAscent);
	lpStruct->tmDescent = (*env)->GetIntField(env, lpObject, lpCache->tmDescent);
	lpStruct->tmInternalLeading = (*env)->GetIntField(env, lpObject, lpCache->tmInternalLeading);
	lpStruct->tmExternalLeading = (*env)->GetIntField(env, lpObject, lpCache->tmExternalLeading);
	lpStruct->tmAveCharWidth = (*env)->GetIntField(env, lpObject, lpCache->tmAveCharWidth);
	lpStruct->tmMaxCharWidth = (*env)->GetIntField(env, lpObject, lpCache->tmMaxCharWidth);
	lpStruct->tmWeight = (*env)->GetIntField(env, lpObject, lpCache->tmWeight);
	lpStruct->tmOverhang = (*env)->GetIntField(env, lpObject, lpCache->tmOverhang);
	lpStruct->tmDigitizedAspectX = (*env)->GetIntField(env, lpObject, lpCache->tmDigitizedAspectX);
	lpStruct->tmDigitizedAspectY = (*env)->GetIntField(env, lpObject, lpCache->tmDigitizedAspectY);
	lpStruct->tmItalic = (*env)->GetByteField(env, lpObject, lpCache->tmItalic);
	lpStruct->tmUnderlined = (*env)->GetByteField(env, lpObject, lpCache->tmUnderlined);
	lpStruct->tmStruckOut = (*env)->GetByteField(env, lpObject, lpCache->tmStruckOut);
	lpStruct->tmPitchAndFamily = (*env)->GetByteField(env, lpObject, lpCache->tmPitchAndFamily);
	lpStruct->tmCharSet = (*env)->GetByteField(env, lpObject, lpCache->tmCharSet);
	return lpStruct;
}

void setTEXTMETRICAFields(JNIEnv *env, jobject lpObject, TEXTMETRICA *lpStruct, PTEXTMETRIC_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheTEXTMETRICFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->tmHeight, lpStruct->tmHeight);
	(*env)->SetIntField(env, lpObject, lpCache->tmAscent, lpStruct->tmAscent);
	(*env)->SetIntField(env, lpObject, lpCache->tmDescent, lpStruct->tmDescent);
	(*env)->SetIntField(env, lpObject, lpCache->tmInternalLeading, lpStruct->tmInternalLeading);
	(*env)->SetIntField(env, lpObject, lpCache->tmExternalLeading, lpStruct->tmExternalLeading);
	(*env)->SetIntField(env, lpObject, lpCache->tmAveCharWidth, lpStruct->tmAveCharWidth);
	(*env)->SetIntField(env, lpObject, lpCache->tmMaxCharWidth, lpStruct->tmMaxCharWidth);
	(*env)->SetIntField(env, lpObject, lpCache->tmWeight, lpStruct->tmWeight);
	(*env)->SetIntField(env, lpObject, lpCache->tmOverhang, lpStruct->tmOverhang);
	(*env)->SetIntField(env, lpObject, lpCache->tmDigitizedAspectX, lpStruct->tmDigitizedAspectX);
	(*env)->SetIntField(env, lpObject, lpCache->tmDigitizedAspectY, lpStruct->tmDigitizedAspectY);
	(*env)->SetByteField(env, lpObject, lpCache->tmItalic, lpStruct->tmItalic);
	(*env)->SetByteField(env, lpObject, lpCache->tmUnderlined, lpStruct->tmUnderlined);
	(*env)->SetByteField(env, lpObject, lpCache->tmStruckOut, lpStruct->tmStruckOut);
	(*env)->SetByteField(env, lpObject, lpCache->tmPitchAndFamily, lpStruct->tmPitchAndFamily);
	(*env)->SetByteField(env, lpObject, lpCache->tmCharSet, lpStruct->tmCharSet);
}

#endif // _WIN32_WCE

TEXTMETRICW* getTEXTMETRICWFields(JNIEnv *env, jobject lpObject, TEXTMETRICW *lpStruct, PTEXTMETRIC_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheTEXTMETRICFids(env, lpObject, lpCache);
	lpStruct->tmHeight = (*env)->GetIntField(env, lpObject, lpCache->tmHeight);
	lpStruct->tmAscent = (*env)->GetIntField(env, lpObject, lpCache->tmAscent);
	lpStruct->tmDescent = (*env)->GetIntField(env, lpObject, lpCache->tmDescent);
	lpStruct->tmInternalLeading = (*env)->GetIntField(env, lpObject, lpCache->tmInternalLeading);
	lpStruct->tmExternalLeading = (*env)->GetIntField(env, lpObject, lpCache->tmExternalLeading);
	lpStruct->tmAveCharWidth = (*env)->GetIntField(env, lpObject, lpCache->tmAveCharWidth);
	lpStruct->tmMaxCharWidth = (*env)->GetIntField(env, lpObject, lpCache->tmMaxCharWidth);
	lpStruct->tmWeight = (*env)->GetIntField(env, lpObject, lpCache->tmWeight);
	lpStruct->tmOverhang = (*env)->GetIntField(env, lpObject, lpCache->tmOverhang);
	lpStruct->tmDigitizedAspectX = (*env)->GetIntField(env, lpObject, lpCache->tmDigitizedAspectX);
	lpStruct->tmDigitizedAspectY = (*env)->GetIntField(env, lpObject, lpCache->tmDigitizedAspectY);
	lpStruct->tmItalic = (*env)->GetByteField(env, lpObject, lpCache->tmItalic);
	lpStruct->tmUnderlined = (*env)->GetByteField(env, lpObject, lpCache->tmUnderlined);
	lpStruct->tmStruckOut = (*env)->GetByteField(env, lpObject, lpCache->tmStruckOut);
	lpStruct->tmPitchAndFamily = (*env)->GetByteField(env, lpObject, lpCache->tmPitchAndFamily);
	lpStruct->tmCharSet = (*env)->GetByteField(env, lpObject, lpCache->tmCharSet);
	return lpStruct;
}

void setTEXTMETRICWFields(JNIEnv *env, jobject lpObject, TEXTMETRICW *lpStruct, PTEXTMETRIC_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheTEXTMETRICFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->tmHeight, lpStruct->tmHeight);
	(*env)->SetIntField(env, lpObject, lpCache->tmAscent, lpStruct->tmAscent);
	(*env)->SetIntField(env, lpObject, lpCache->tmDescent, lpStruct->tmDescent);
	(*env)->SetIntField(env, lpObject, lpCache->tmInternalLeading, lpStruct->tmInternalLeading);
	(*env)->SetIntField(env, lpObject, lpCache->tmExternalLeading, lpStruct->tmExternalLeading);
	(*env)->SetIntField(env, lpObject, lpCache->tmAveCharWidth, lpStruct->tmAveCharWidth);
	(*env)->SetIntField(env, lpObject, lpCache->tmMaxCharWidth, lpStruct->tmMaxCharWidth);
	(*env)->SetIntField(env, lpObject, lpCache->tmWeight, lpStruct->tmWeight);
	(*env)->SetIntField(env, lpObject, lpCache->tmOverhang, lpStruct->tmOverhang);
	(*env)->SetIntField(env, lpObject, lpCache->tmDigitizedAspectX, lpStruct->tmDigitizedAspectX);
	(*env)->SetIntField(env, lpObject, lpCache->tmDigitizedAspectY, lpStruct->tmDigitizedAspectY);
	(*env)->SetByteField(env, lpObject, lpCache->tmItalic, lpStruct->tmItalic);
	(*env)->SetByteField(env, lpObject, lpCache->tmUnderlined, lpStruct->tmUnderlined);
	(*env)->SetByteField(env, lpObject, lpCache->tmStruckOut, lpStruct->tmStruckOut);
	(*env)->SetByteField(env, lpObject, lpCache->tmPitchAndFamily, lpStruct->tmPitchAndFamily);
	(*env)->SetByteField(env, lpObject, lpCache->tmCharSet, lpStruct->tmCharSet);
}

#ifndef _WIN32_WCE

void cacheTOOLINFOFids(JNIEnv *env, jobject lpObject, PTOOLINFO_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->cbSize = (*env)->GetFieldID(env, lpCache->clazz, "cbSize", "I");
	lpCache->uFlags = (*env)->GetFieldID(env, lpCache->clazz, "uFlags", "I");
	lpCache->hwnd = (*env)->GetFieldID(env, lpCache->clazz, "hwnd", "I");
	lpCache->uId = (*env)->GetFieldID(env, lpCache->clazz, "uId", "I");
	lpCache->left = (*env)->GetFieldID(env, lpCache->clazz, "left", "I");
	lpCache->top = (*env)->GetFieldID(env, lpCache->clazz, "top", "I");
	lpCache->right = (*env)->GetFieldID(env, lpCache->clazz, "right", "I");
	lpCache->bottom = (*env)->GetFieldID(env, lpCache->clazz, "bottom", "I");
	lpCache->hinst = (*env)->GetFieldID(env, lpCache->clazz, "hinst", "I");
	lpCache->lpszText = (*env)->GetFieldID(env, lpCache->clazz, "lpszText", "I");
	lpCache->lParam = (*env)->GetFieldID(env, lpCache->clazz, "lParam", "I");
	lpCache->cached = 1;
}

TOOLINFO* getTOOLINFOFields(JNIEnv *env, jobject lpObject, TOOLINFO *lpStruct, PTOOLINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheTOOLINFOFids(env, lpObject, lpCache);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, lpCache->cbSize);
	lpStruct->uFlags = (*env)->GetIntField(env, lpObject, lpCache->uFlags);
	lpStruct->hwnd = (HWND)(*env)->GetIntField(env, lpObject, lpCache->hwnd);
	lpStruct->uId = (*env)->GetIntField(env, lpObject, lpCache->uId);
	lpStruct->rect.left = (*env)->GetIntField(env, lpObject, lpCache->left);
	lpStruct->rect.top = (*env)->GetIntField(env, lpObject, lpCache->top);
	lpStruct->rect.right = (*env)->GetIntField(env, lpObject, lpCache->right);
	lpStruct->rect.bottom = (*env)->GetIntField(env, lpObject, lpCache->bottom);
	lpStruct->hinst = (HINSTANCE)(*env)->GetIntField(env, lpObject, lpCache->hinst);
	lpStruct->lpszText = (LPTSTR)(*env)->GetIntField(env, lpObject, lpCache->lpszText);
	lpStruct->lParam = (*env)->GetIntField(env, lpObject, lpCache->lParam);
	return lpStruct;
}

void setTOOLINFOFields(JNIEnv *env, jobject lpObject, TOOLINFO *lpStruct, PTOOLINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheTOOLINFOFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->cbSize, lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, lpCache->uFlags, lpStruct->uFlags);
	(*env)->SetIntField(env, lpObject, lpCache->hwnd, (jint)lpStruct->hwnd);
	(*env)->SetIntField(env, lpObject, lpCache->uId, lpStruct->uId);
	(*env)->SetIntField(env, lpObject, lpCache->left, lpStruct->rect.left);
	(*env)->SetIntField(env, lpObject, lpCache->top, lpStruct->rect.top);
	(*env)->SetIntField(env, lpObject, lpCache->right, lpStruct->rect.right);
	(*env)->SetIntField(env, lpObject, lpCache->bottom, lpStruct->rect.bottom);
	(*env)->SetIntField(env, lpObject, lpCache->hinst, (jint)lpStruct->hinst);
	(*env)->SetIntField(env, lpObject, lpCache->lpszText, (jint)lpStruct->lpszText);
	(*env)->SetIntField(env, lpObject, lpCache->lParam, lpStruct->lParam);
}

#endif // _WIN32_WCE

#ifndef _WIN32_WCE

void cacheTRACKMOUSEEVENTFids(JNIEnv *env, jobject lpObject, PTRACKMOUSEEVENT_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->cbSize = (*env)->GetFieldID(env, lpCache->clazz, "cbSize", "I");
	lpCache->dwFlags = (*env)->GetFieldID(env, lpCache->clazz, "dwFlags", "I");
	lpCache->hwndTrack = (*env)->GetFieldID(env, lpCache->clazz, "hwndTrack", "I");
	lpCache->dwHoverTime = (*env)->GetFieldID(env, lpCache->clazz, "dwHoverTime", "I");
	lpCache->cached = 1;
}

TRACKMOUSEEVENT* getTRACKMOUSEEVENTFields(JNIEnv *env, jobject lpObject, TRACKMOUSEEVENT *lpStruct, PTRACKMOUSEEVENT_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheTRACKMOUSEEVENTFids(env, lpObject, lpCache);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, lpCache->cbSize);
	lpStruct->dwFlags = (*env)->GetIntField(env, lpObject, lpCache->dwFlags);
	lpStruct->hwndTrack = (HWND)(*env)->GetIntField(env, lpObject, lpCache->hwndTrack);
	lpStruct->dwHoverTime = (*env)->GetIntField(env, lpObject, lpCache->dwHoverTime);
	return lpStruct;
}

void setTRACKMOUSEEVENTFields(JNIEnv *env, jobject lpObject, TRACKMOUSEEVENT *lpStruct, PTRACKMOUSEEVENT_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheTRACKMOUSEEVENTFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->cbSize, lpStruct->cbSize);
	(*env)->SetIntField(env, lpObject, lpCache->dwFlags, lpStruct->dwFlags);
	(*env)->SetIntField(env, lpObject, lpCache->hwndTrack, (jint)lpStruct->hwndTrack);
	(*env)->SetIntField(env, lpObject, lpCache->dwHoverTime, lpStruct->dwHoverTime);
}

#endif // _WIN32_WCE

#ifndef _WIN32_WCE

void cacheTRIVERTEXFids(JNIEnv *env, jobject lpObject, PTRIVERTEX_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->x = (*env)->GetFieldID(env, lpCache->clazz, "x", "I");
	lpCache->y = (*env)->GetFieldID(env, lpCache->clazz, "y", "I");
	lpCache->Red = (*env)->GetFieldID(env, lpCache->clazz, "Red", "S");
	lpCache->Green = (*env)->GetFieldID(env, lpCache->clazz, "Green", "S");
	lpCache->Blue = (*env)->GetFieldID(env, lpCache->clazz, "Blue", "S");
	lpCache->Alpha = (*env)->GetFieldID(env, lpCache->clazz, "Alpha", "S");
	lpCache->cached = 1;
}

TRIVERTEX* getTRIVERTEXFields(JNIEnv *env, jobject lpObject, TRIVERTEX *lpStruct, PTRIVERTEX_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheTRIVERTEXFids(env, lpObject, lpCache);
	lpStruct->x = (*env)->GetIntField(env, lpObject, lpCache->x);
	lpStruct->y = (*env)->GetIntField(env, lpObject, lpCache->y);
	lpStruct->Red = (*env)->GetShortField(env, lpObject, lpCache->Red);
	lpStruct->Green = (*env)->GetShortField(env, lpObject, lpCache->Green);
	lpStruct->Blue = (*env)->GetShortField(env, lpObject, lpCache->Blue);
	lpStruct->Alpha = (*env)->GetShortField(env, lpObject, lpCache->Alpha);
	return lpStruct;
}

void setTRIVERTEXFields(JNIEnv *env, jobject lpObject, TRIVERTEX *lpStruct, PTRIVERTEX_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheTRIVERTEXFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->x, lpStruct->x);
	(*env)->SetIntField(env, lpObject, lpCache->y, lpStruct->y);
	(*env)->SetShortField(env, lpObject, lpCache->Red, lpStruct->Red);
	(*env)->SetShortField(env, lpObject, lpCache->Green, lpStruct->Green);
	(*env)->SetShortField(env, lpObject, lpCache->Blue, lpStruct->Blue);
	(*env)->SetShortField(env, lpObject, lpCache->Alpha, lpStruct->Alpha);
}

#endif // _WIN32_WCE

void cacheTVHITTESTINFOFids(JNIEnv *env, jobject lpObject, PTVHITTESTINFO_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->x = (*env)->GetFieldID(env, lpCache->clazz, "x", "I");
	lpCache->y = (*env)->GetFieldID(env, lpCache->clazz, "y", "I");
	lpCache->flags = (*env)->GetFieldID(env, lpCache->clazz, "flags", "I");
	lpCache->hItem = (*env)->GetFieldID(env, lpCache->clazz, "hItem", "I");
	lpCache->cached = 1;
}

TVHITTESTINFO* getTVHITTESTINFOFields(JNIEnv *env, jobject lpObject, TVHITTESTINFO *lpStruct, PTVHITTESTINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheTVHITTESTINFOFids(env, lpObject, lpCache);
	lpStruct->pt.x = (*env)->GetIntField(env, lpObject, lpCache->x);
	lpStruct->pt.y = (*env)->GetIntField(env, lpObject, lpCache->y);
	lpStruct->flags = (*env)->GetIntField(env, lpObject, lpCache->flags);
	lpStruct->hItem = (HTREEITEM)(*env)->GetIntField(env, lpObject, lpCache->hItem);
	return lpStruct;
}

void setTVHITTESTINFOFields(JNIEnv *env, jobject lpObject, TVHITTESTINFO *lpStruct, PTVHITTESTINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheTVHITTESTINFOFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->x, lpStruct->pt.x);
	(*env)->SetIntField(env, lpObject, lpCache->y, lpStruct->pt.y);
	(*env)->SetIntField(env, lpObject, lpCache->flags, lpStruct->flags);
	(*env)->SetIntField(env, lpObject, lpCache->hItem, (jint)lpStruct->hItem);
}

void cacheTVINSERTSTRUCTFids(JNIEnv *env, jobject lpObject, PTVINSERTSTRUCT_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->hParent = (*env)->GetFieldID(env, lpCache->clazz, "hParent", "I");
	lpCache->hInsertAfter = (*env)->GetFieldID(env, lpCache->clazz, "hInsertAfter", "I");
	lpCache->mask = (*env)->GetFieldID(env, lpCache->clazz, "mask", "I");
	lpCache->hItem = (*env)->GetFieldID(env, lpCache->clazz, "hItem", "I");
	lpCache->state = (*env)->GetFieldID(env, lpCache->clazz, "state", "I");
	lpCache->stateMask = (*env)->GetFieldID(env, lpCache->clazz, "stateMask", "I");
	lpCache->pszText = (*env)->GetFieldID(env, lpCache->clazz, "pszText", "I");
	lpCache->cchTextMax = (*env)->GetFieldID(env, lpCache->clazz, "cchTextMax", "I");
	lpCache->iImage = (*env)->GetFieldID(env, lpCache->clazz, "iImage", "I");
	lpCache->iSelectedImage = (*env)->GetFieldID(env, lpCache->clazz, "iSelectedImage", "I");
	lpCache->cChildren = (*env)->GetFieldID(env, lpCache->clazz, "cChildren", "I");
	lpCache->lParam = (*env)->GetFieldID(env, lpCache->clazz, "lParam", "I");
	lpCache->cached = 1;
}

TVINSERTSTRUCT* getTVINSERTSTRUCTFields(JNIEnv *env, jobject lpObject, TVINSERTSTRUCT *lpStruct, PTVINSERTSTRUCT_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheTVINSERTSTRUCTFids(env, lpObject, lpCache);
	lpStruct->hParent = (HTREEITEM)(*env)->GetIntField(env, lpObject, lpCache->hParent);
	lpStruct->hInsertAfter = (HTREEITEM)(*env)->GetIntField(env, lpObject, lpCache->hInsertAfter);
	lpStruct->item.mask = (*env)->GetIntField(env, lpObject, lpCache->mask);
	lpStruct->item.hItem = (HTREEITEM)(*env)->GetIntField(env, lpObject, lpCache->hItem);
	lpStruct->item.state = (*env)->GetIntField(env, lpObject, lpCache->state);
	lpStruct->item.stateMask = (*env)->GetIntField(env, lpObject, lpCache->stateMask);
	lpStruct->item.pszText = (LPTSTR)(*env)->GetIntField(env, lpObject, lpCache->pszText);
	lpStruct->item.cchTextMax = (*env)->GetIntField(env, lpObject, lpCache->cchTextMax);
	lpStruct->item.iImage = (*env)->GetIntField(env, lpObject, lpCache->iImage);
	lpStruct->item.iSelectedImage = (*env)->GetIntField(env, lpObject, lpCache->iSelectedImage);
	lpStruct->item.cChildren = (*env)->GetIntField(env, lpObject, lpCache->cChildren);
	lpStruct->item.lParam = (*env)->GetIntField(env, lpObject, lpCache->lParam);
	return lpStruct;
}

void setTVINSERTSTRUCTFields(JNIEnv *env, jobject lpObject, TVINSERTSTRUCT *lpStruct, PTVINSERTSTRUCT_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheTVINSERTSTRUCTFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->hParent, (jint)lpStruct->hParent);
	(*env)->SetIntField(env, lpObject, lpCache->hInsertAfter, (jint)lpStruct->hInsertAfter);
	(*env)->SetIntField(env, lpObject, lpCache->mask, lpStruct->item.mask);
	(*env)->SetIntField(env, lpObject, lpCache->hItem, (jint)lpStruct->item.hItem);
	(*env)->SetIntField(env, lpObject, lpCache->state, lpStruct->item.state);
	(*env)->SetIntField(env, lpObject, lpCache->stateMask, lpStruct->item.stateMask);
	(*env)->SetIntField(env, lpObject, lpCache->pszText, (jint)lpStruct->item.pszText);
	(*env)->SetIntField(env, lpObject, lpCache->cchTextMax, lpStruct->item.cchTextMax);
	(*env)->SetIntField(env, lpObject, lpCache->iImage, lpStruct->item.iImage);
	(*env)->SetIntField(env, lpObject, lpCache->iSelectedImage, lpStruct->item.iSelectedImage);
	(*env)->SetIntField(env, lpObject, lpCache->cChildren, lpStruct->item.cChildren);
	(*env)->SetIntField(env, lpObject, lpCache->lParam, lpStruct->item.lParam);
}

void cacheTVITEMFids(JNIEnv *env, jobject lpObject, PTVITEM_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->mask = (*env)->GetFieldID(env, lpCache->clazz, "mask", "I");
	lpCache->hItem = (*env)->GetFieldID(env, lpCache->clazz, "hItem", "I");
	lpCache->state = (*env)->GetFieldID(env, lpCache->clazz, "state", "I");
	lpCache->stateMask = (*env)->GetFieldID(env, lpCache->clazz, "stateMask", "I");
	lpCache->pszText = (*env)->GetFieldID(env, lpCache->clazz, "pszText", "I");
	lpCache->cchTextMax = (*env)->GetFieldID(env, lpCache->clazz, "cchTextMax", "I");
	lpCache->iImage = (*env)->GetFieldID(env, lpCache->clazz, "iImage", "I");
	lpCache->iSelectedImage = (*env)->GetFieldID(env, lpCache->clazz, "iSelectedImage", "I");
	lpCache->cChildren = (*env)->GetFieldID(env, lpCache->clazz, "cChildren", "I");
	lpCache->lParam = (*env)->GetFieldID(env, lpCache->clazz, "lParam", "I");
	lpCache->cached = 1;
}

TVITEM* getTVITEMFields(JNIEnv *env, jobject lpObject, TVITEM *lpStruct, PTVITEM_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheTVITEMFids(env, lpObject, lpCache);
	lpStruct->mask = (*env)->GetIntField(env, lpObject, lpCache->mask);
	lpStruct->hItem = (HTREEITEM)(*env)->GetIntField(env, lpObject, lpCache->hItem);
	lpStruct->state = (*env)->GetIntField(env, lpObject, lpCache->state);
	lpStruct->stateMask = (*env)->GetIntField(env, lpObject, lpCache->stateMask);
	lpStruct->pszText = (LPTSTR)(*env)->GetIntField(env, lpObject, lpCache->pszText);
	lpStruct->cchTextMax = (*env)->GetIntField(env, lpObject, lpCache->cchTextMax);
	lpStruct->iImage = (*env)->GetIntField(env, lpObject, lpCache->iImage);
	lpStruct->iSelectedImage = (*env)->GetIntField(env, lpObject, lpCache->iSelectedImage);
	lpStruct->cChildren = (*env)->GetIntField(env, lpObject, lpCache->cChildren);
	lpStruct->lParam = (*env)->GetIntField(env, lpObject, lpCache->lParam);
	return lpStruct;
}

void setTVITEMFields(JNIEnv *env, jobject lpObject, TVITEM *lpStruct, PTVITEM_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheTVITEMFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->mask, lpStruct->mask);
	(*env)->SetIntField(env, lpObject, lpCache->hItem, (jint)lpStruct->hItem);
	(*env)->SetIntField(env, lpObject, lpCache->state, lpStruct->state);
	(*env)->SetIntField(env, lpObject, lpCache->stateMask, lpStruct->stateMask);
	(*env)->SetIntField(env, lpObject, lpCache->pszText, (jint)lpStruct->pszText);
	(*env)->SetIntField(env, lpObject, lpCache->cchTextMax, lpStruct->cchTextMax);
	(*env)->SetIntField(env, lpObject, lpCache->iImage, lpStruct->iImage);
	(*env)->SetIntField(env, lpObject, lpCache->iSelectedImage, lpStruct->iSelectedImage);
	(*env)->SetIntField(env, lpObject, lpCache->cChildren, lpStruct->cChildren);
	(*env)->SetIntField(env, lpObject, lpCache->lParam, lpStruct->lParam);
}

#ifndef _WIN32_WCE

void cacheWINDOWPLACEMENTFids(JNIEnv *env, jobject lpObject, PWINDOWPLACEMENT_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->length = (*env)->GetFieldID(env, lpCache->clazz, "length", "I");
	lpCache->flags = (*env)->GetFieldID(env, lpCache->clazz, "flags", "I");
	lpCache->showCmd = (*env)->GetFieldID(env, lpCache->clazz, "showCmd", "I");
	lpCache->ptMinPosition_x = (*env)->GetFieldID(env, lpCache->clazz, "ptMinPosition_x", "I");
	lpCache->ptMinPosition_y = (*env)->GetFieldID(env, lpCache->clazz, "ptMinPosition_y", "I");
	lpCache->ptMaxPosition_x = (*env)->GetFieldID(env, lpCache->clazz, "ptMaxPosition_x", "I");
	lpCache->ptMaxPosition_y = (*env)->GetFieldID(env, lpCache->clazz, "ptMaxPosition_y", "I");
	lpCache->left = (*env)->GetFieldID(env, lpCache->clazz, "left", "I");
	lpCache->top = (*env)->GetFieldID(env, lpCache->clazz, "top", "I");
	lpCache->right = (*env)->GetFieldID(env, lpCache->clazz, "right", "I");
	lpCache->bottom = (*env)->GetFieldID(env, lpCache->clazz, "bottom", "I");
	lpCache->cached = 1;
}

WINDOWPLACEMENT* getWINDOWPLACEMENTFields(JNIEnv *env, jobject lpObject, WINDOWPLACEMENT *lpStruct, PWINDOWPLACEMENT_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheWINDOWPLACEMENTFids(env, lpObject, lpCache);
	lpStruct->length = (*env)->GetIntField(env, lpObject, lpCache->length);
	lpStruct->flags = (*env)->GetIntField(env, lpObject, lpCache->flags);
	lpStruct->showCmd = (*env)->GetIntField(env, lpObject, lpCache->showCmd);
	lpStruct->ptMinPosition.x = (*env)->GetIntField(env, lpObject, lpCache->ptMinPosition_x);
	lpStruct->ptMinPosition.y = (*env)->GetIntField(env, lpObject, lpCache->ptMinPosition_y);
	lpStruct->ptMaxPosition.x = (*env)->GetIntField(env, lpObject, lpCache->ptMaxPosition_x);
	lpStruct->ptMaxPosition.y = (*env)->GetIntField(env, lpObject, lpCache->ptMaxPosition_y);
	lpStruct->rcNormalPosition.left = (*env)->GetIntField(env, lpObject, lpCache->left);
	lpStruct->rcNormalPosition.top = (*env)->GetIntField(env, lpObject, lpCache->top);
	lpStruct->rcNormalPosition.right = (*env)->GetIntField(env, lpObject, lpCache->right);
	lpStruct->rcNormalPosition.bottom = (*env)->GetIntField(env, lpObject, lpCache->bottom);
	return lpStruct;
}

void setWINDOWPLACEMENTFields(JNIEnv *env, jobject lpObject, WINDOWPLACEMENT *lpStruct, PWINDOWPLACEMENT_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheWINDOWPLACEMENTFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->length, lpStruct->length);
	(*env)->SetIntField(env, lpObject, lpCache->flags, lpStruct->flags);
	(*env)->SetIntField(env, lpObject, lpCache->showCmd, lpStruct->showCmd);
	(*env)->SetIntField(env, lpObject, lpCache->ptMinPosition_x, lpStruct->ptMinPosition.x);
	(*env)->SetIntField(env, lpObject, lpCache->ptMinPosition_y, lpStruct->ptMinPosition.y);
	(*env)->SetIntField(env, lpObject, lpCache->ptMaxPosition_x, lpStruct->ptMaxPosition.x);
	(*env)->SetIntField(env, lpObject, lpCache->ptMaxPosition_y, lpStruct->ptMaxPosition.y);
	(*env)->SetIntField(env, lpObject, lpCache->left, lpStruct->rcNormalPosition.left);
	(*env)->SetIntField(env, lpObject, lpCache->top, lpStruct->rcNormalPosition.top);
	(*env)->SetIntField(env, lpObject, lpCache->right, lpStruct->rcNormalPosition.right);
	(*env)->SetIntField(env, lpObject, lpCache->bottom, lpStruct->rcNormalPosition.bottom);
}

#endif // _WIN32_WCE

void cacheWINDOWPOSFids(JNIEnv *env, jobject lpObject, PWINDOWPOS_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->hwnd = (*env)->GetFieldID(env, lpCache->clazz, "hwnd", "I");
	lpCache->hwndInsertAfter = (*env)->GetFieldID(env, lpCache->clazz, "hwndInsertAfter", "I");
	lpCache->x = (*env)->GetFieldID(env, lpCache->clazz, "x", "I");
	lpCache->y = (*env)->GetFieldID(env, lpCache->clazz, "y", "I");
	lpCache->cx = (*env)->GetFieldID(env, lpCache->clazz, "cx", "I");
	lpCache->cy = (*env)->GetFieldID(env, lpCache->clazz, "cy", "I");
	lpCache->flags = (*env)->GetFieldID(env, lpCache->clazz, "flags", "I");
	lpCache->cached = 1;
}

WINDOWPOS* getWINDOWPOSFields(JNIEnv *env, jobject lpObject, WINDOWPOS *lpStruct, PWINDOWPOS_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheWINDOWPOSFids(env, lpObject, lpCache);
	lpStruct->hwnd = (HWND)(*env)->GetIntField(env, lpObject, lpCache->hwnd);
	lpStruct->hwndInsertAfter = (HWND)(*env)->GetIntField(env, lpObject, lpCache->hwndInsertAfter);
	lpStruct->x = (*env)->GetIntField(env, lpObject, lpCache->x);
	lpStruct->y = (*env)->GetIntField(env, lpObject, lpCache->y);
	lpStruct->cx = (*env)->GetIntField(env, lpObject, lpCache->cx);
	lpStruct->cy = (*env)->GetIntField(env, lpObject, lpCache->cy);
	lpStruct->flags = (*env)->GetIntField(env, lpObject, lpCache->flags);
	return lpStruct;
}

void setWINDOWPOSFields(JNIEnv *env, jobject lpObject, WINDOWPOS *lpStruct, PWINDOWPOS_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheWINDOWPOSFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->hwnd, (jint)lpStruct->hwnd);
	(*env)->SetIntField(env, lpObject, lpCache->hwndInsertAfter, (jint)lpStruct->hwndInsertAfter);
	(*env)->SetIntField(env, lpObject, lpCache->x, lpStruct->x);
	(*env)->SetIntField(env, lpObject, lpCache->y, lpStruct->y);
	(*env)->SetIntField(env, lpObject, lpCache->cx, lpStruct->cx);
	(*env)->SetIntField(env, lpObject, lpCache->cy, lpStruct->cy);
	(*env)->SetIntField(env, lpObject, lpCache->flags, lpStruct->flags);
}

void cacheWNDCLASSFids(JNIEnv *env, jobject lpObject, PWNDCLASS_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->style = (*env)->GetFieldID(env, lpCache->clazz, "style", "I");
	lpCache->lpfnWndProc = (*env)->GetFieldID(env, lpCache->clazz, "lpfnWndProc", "I");
	lpCache->cbClsExtra = (*env)->GetFieldID(env, lpCache->clazz, "cbClsExtra", "I");
	lpCache->cbWndExtra = (*env)->GetFieldID(env, lpCache->clazz, "cbWndExtra", "I");
	lpCache->hInstance = (*env)->GetFieldID(env, lpCache->clazz, "hInstance", "I");
	lpCache->hIcon = (*env)->GetFieldID(env, lpCache->clazz, "hIcon", "I");
	lpCache->hCursor = (*env)->GetFieldID(env, lpCache->clazz, "hCursor", "I");
	lpCache->hbrBackground = (*env)->GetFieldID(env, lpCache->clazz, "hbrBackground", "I");
	lpCache->lpszMenuName = (*env)->GetFieldID(env, lpCache->clazz, "lpszMenuName", "I");
	lpCache->lpszClassName = (*env)->GetFieldID(env, lpCache->clazz, "lpszClassName", "I");
	lpCache->cached = 1;
}

WNDCLASS* getWNDCLASSFields(JNIEnv *env, jobject lpObject, WNDCLASS *lpStruct, PWNDCLASS_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheWNDCLASSFids(env, lpObject, lpCache);
	lpStruct->style = (*env)->GetIntField(env, lpObject, lpCache->style);
	lpStruct->lpfnWndProc = (WNDPROC)(*env)->GetIntField(env, lpObject, lpCache->lpfnWndProc);
	lpStruct->cbClsExtra = (*env)->GetIntField(env, lpObject, lpCache->cbClsExtra);
	lpStruct->cbWndExtra = (*env)->GetIntField(env, lpObject, lpCache->cbWndExtra);
	lpStruct->hInstance = (HINSTANCE)(*env)->GetIntField(env, lpObject, lpCache->hInstance);
	lpStruct->hIcon = (HICON)(*env)->GetIntField(env, lpObject, lpCache->hIcon);
	lpStruct->hCursor = (HCURSOR)(*env)->GetIntField(env, lpObject, lpCache->hCursor);
	lpStruct->hbrBackground = (HBRUSH)(*env)->GetIntField(env, lpObject, lpCache->hbrBackground);
	lpStruct->lpszMenuName = (LPCTSTR)(*env)->GetIntField(env, lpObject, lpCache->lpszMenuName);
	lpStruct->lpszClassName = (LPCTSTR)(*env)->GetIntField(env, lpObject, lpCache->lpszClassName);
	return lpStruct;
}

void setWNDCLASSFields(JNIEnv *env, jobject lpObject, WNDCLASS *lpStruct, PWNDCLASS_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheWNDCLASSFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->style, lpStruct->style);
	(*env)->SetIntField(env, lpObject, lpCache->lpfnWndProc, (jint)lpStruct->lpfnWndProc);
	(*env)->SetIntField(env, lpObject, lpCache->cbClsExtra, lpStruct->cbClsExtra);
	(*env)->SetIntField(env, lpObject, lpCache->cbWndExtra, lpStruct->cbWndExtra);
	(*env)->SetIntField(env, lpObject, lpCache->hInstance, (jint)lpStruct->hInstance);
	(*env)->SetIntField(env, lpObject, lpCache->hIcon, (jint)lpStruct->hIcon);
	(*env)->SetIntField(env, lpObject, lpCache->hCursor, (jint)lpStruct->hCursor);
	(*env)->SetIntField(env, lpObject, lpCache->hbrBackground, (jint)lpStruct->hbrBackground);
	(*env)->SetIntField(env, lpObject, lpCache->lpszMenuName, (jint)lpStruct->lpszMenuName);
	(*env)->SetIntField(env, lpObject, lpCache->lpszClassName, (jint)lpStruct->lpszClassName);
}

#ifndef _WIN32_WCE

void cacheNONCLIENTMETRICSFids(JNIEnv *env, jobject lpObject, PNONCLIENTMETRICS_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->cbSize = (*env)->GetFieldID(env, lpCache->clazz, "cbSize", "I");
	lpCache->iBorderWidth = (*env)->GetFieldID(env, lpCache->clazz, "iBorderWidth", "I");
	lpCache->iScrollWidth = (*env)->GetFieldID(env, lpCache->clazz, "iScrollWidth", "I");
	lpCache->iScrollHeight = (*env)->GetFieldID(env, lpCache->clazz, "iScrollHeight", "I");
	lpCache->iCaptionWidth = (*env)->GetFieldID(env, lpCache->clazz, "iCaptionWidth", "I");
	lpCache->iCaptionHeight = (*env)->GetFieldID(env, lpCache->clazz, "iCaptionHeight", "I");
	lpCache->lfCaptionFont = (*env)->GetFieldID(env, lpCache->clazz, "lfCaptionFont", "Lorg/eclipse/swt/internal/win32/LOGFONT;");
	lpCache->iSmCaptionWidth = (*env)->GetFieldID(env, lpCache->clazz, "iSmCaptionWidth", "I");
	lpCache->iSmCaptionHeight = (*env)->GetFieldID(env, lpCache->clazz, "iSmCaptionHeight", "I");
	lpCache->lfSmCaptionFont = (*env)->GetFieldID(env, lpCache->clazz, "lfSmCaptionFont", "Lorg/eclipse/swt/internal/win32/LOGFONT;");
	lpCache->iMenuWidth = (*env)->GetFieldID(env, lpCache->clazz, "iMenuWidth", "I");
	lpCache->iMenuHeight = (*env)->GetFieldID(env, lpCache->clazz, "iMenuHeight", "I");
	lpCache->lfMenuFont = (*env)->GetFieldID(env, lpCache->clazz, "lfMenuFont", "Lorg/eclipse/swt/internal/win32/LOGFONT;");
	lpCache->lfStatusFont = (*env)->GetFieldID(env, lpCache->clazz, "lfStatusFont", "Lorg/eclipse/swt/internal/win32/LOGFONT;");
	lpCache->lfMessageFont = (*env)->GetFieldID(env, lpCache->clazz, "lfMessageFont", "Lorg/eclipse/swt/internal/win32/LOGFONT;");
	lpCache->cached = 1;
}

#endif // _WIN32_WCE

/************************ OLE ***************************/

void cacheCAUUIDFids(JNIEnv *env, jobject lpObject, PCAUUID_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->pElems = (*env)->GetFieldID(env, lpCache->clazz, "pElems", "I");
	lpCache->cElems = (*env)->GetFieldID(env, lpCache->clazz, "cElems", "I");
	lpCache->cached = 1;
}

CAUUID* getCAUUIDFields(JNIEnv *env, jobject lpObject, CAUUID *lpStruct, PCAUUID_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheCAUUIDFids(env, lpObject, lpCache);
	lpStruct->pElems = (GUID FAR *)(*env)->GetIntField(env, lpObject, lpCache->pElems);
	lpStruct->cElems = (*env)->GetIntField(env, lpObject, lpCache->cElems);
	return lpStruct;
}

void setCAUUIDFields(JNIEnv *env, jobject lpObject, CAUUID *lpStruct, PCAUUID_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheCAUUIDFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->pElems, (jint)lpStruct->pElems);
	(*env)->SetIntField(env, lpObject, lpCache->cElems, lpStruct->cElems);
}

void cacheCONTROLINFOFids(JNIEnv *env, jobject lpObject, PCONTROLINFO_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->dwFlags = (*env)->GetFieldID(env, lpCache->clazz, "dwFlags", "I");
	lpCache->cAccel = (*env)->GetFieldID(env, lpCache->clazz, "cAccel", "S");
	lpCache->hAccel = (*env)->GetFieldID(env, lpCache->clazz, "hAccel", "I");
	lpCache->cb = (*env)->GetFieldID(env, lpCache->clazz, "cb", "I");
	lpCache->cached = 1;
}

CONTROLINFO* getCONTROLINFOFields(JNIEnv *env, jobject lpObject, CONTROLINFO *lpStruct, PCONTROLINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheCONTROLINFOFids(env, lpObject, lpCache);
	lpStruct->dwFlags = (*env)->GetIntField(env, lpObject, lpCache->dwFlags);
	lpStruct->cAccel = (*env)->GetShortField(env, lpObject, lpCache->cAccel);
	lpStruct->hAccel = (HACCEL)(*env)->GetIntField(env, lpObject, lpCache->hAccel);
	lpStruct->cb = (*env)->GetIntField(env, lpObject, lpCache->cb);
	return lpStruct;
}

void setCONTROLINFOFields(JNIEnv *env, jobject lpObject, CONTROLINFO *lpStruct, PCONTROLINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheCONTROLINFOFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->dwFlags, lpStruct->dwFlags);
	(*env)->SetShortField(env, lpObject, lpCache->cAccel, lpStruct->cAccel);
	(*env)->SetIntField(env, lpObject, lpCache->hAccel, (jint)lpStruct->hAccel);
	(*env)->SetIntField(env, lpObject, lpCache->cb, lpStruct->cb);
}

void cacheCOSERVERINFOFids(JNIEnv *env, jobject lpObject, PCOSERVERINFO_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->dwReserved2 = (*env)->GetFieldID(env, lpCache->clazz, "dwReserved2", "I");
	lpCache->pAuthInfo = (*env)->GetFieldID(env, lpCache->clazz, "pAuthInfo", "I");
	lpCache->pwszName = (*env)->GetFieldID(env, lpCache->clazz, "pwszName", "I");
	lpCache->dwReserved1 = (*env)->GetFieldID(env, lpCache->clazz, "dwReserved1", "I");
	lpCache->cached = 1;
}

COSERVERINFO* getCOSERVERINFOFields(JNIEnv *env, jobject lpObject, COSERVERINFO *lpStruct, PCOSERVERINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheCOSERVERINFOFids(env, lpObject, lpCache);
	lpStruct->dwReserved2 = (*env)->GetIntField(env, lpObject, lpCache->dwReserved2);
	lpStruct->pAuthInfo = (COAUTHINFO *)(*env)->GetIntField(env, lpObject, lpCache->pAuthInfo);
	lpStruct->pwszName = (LPWSTR)(*env)->GetIntField(env, lpObject, lpCache->pwszName);
	lpStruct->dwReserved1 = (*env)->GetIntField(env, lpObject, lpCache->dwReserved1);
	return lpStruct;
}

void setCOSERVERINFOFields(JNIEnv *env, jobject lpObject, COSERVERINFO *lpStruct, PCOSERVERINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheCOSERVERINFOFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->dwReserved2, lpStruct->dwReserved2);
	(*env)->SetIntField(env, lpObject, lpCache->pAuthInfo, (jint)lpStruct->pAuthInfo);
	(*env)->SetIntField(env, lpObject, lpCache->pwszName, (jint)lpStruct->pwszName);
	(*env)->SetIntField(env, lpObject, lpCache->dwReserved1, lpStruct->dwReserved1);
}

void cacheDISPPARAMSFids(JNIEnv *env, jobject lpObject, PDISPPARAMS_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->cNamedArgs = (*env)->GetFieldID(env, lpCache->clazz, "cNamedArgs", "I");
	lpCache->cArgs = (*env)->GetFieldID(env, lpCache->clazz, "cArgs", "I");
	lpCache->rgdispidNamedArgs = (*env)->GetFieldID(env, lpCache->clazz, "rgdispidNamedArgs", "I");
	lpCache->rgvarg = (*env)->GetFieldID(env, lpCache->clazz, "rgvarg", "I");
	lpCache->cached = 1;
}

DISPPARAMS* getDISPPARAMSFields(JNIEnv *env, jobject lpObject, DISPPARAMS *lpStruct, PDISPPARAMS_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheDISPPARAMSFids(env, lpObject, lpCache);
	lpStruct->cNamedArgs = (*env)->GetIntField(env, lpObject, lpCache->cNamedArgs);
	lpStruct->cArgs = (*env)->GetIntField(env, lpObject, lpCache->cArgs);
	lpStruct->rgdispidNamedArgs = (DISPID FAR *)(*env)->GetIntField(env, lpObject, lpCache->rgdispidNamedArgs);
	lpStruct->rgvarg = (VARIANTARG FAR *)(*env)->GetIntField(env, lpObject, lpCache->rgvarg);
	return lpStruct;
}

void setDISPPARAMSFields(JNIEnv *env, jobject lpObject, DISPPARAMS *lpStruct, PDISPPARAMS_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheDISPPARAMSFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->cNamedArgs, lpStruct->cNamedArgs);
	(*env)->SetIntField(env, lpObject, lpCache->cArgs, lpStruct->cArgs);
	(*env)->SetIntField(env, lpObject, lpCache->rgdispidNamedArgs, (jint)lpStruct->rgdispidNamedArgs);
	(*env)->SetIntField(env, lpObject, lpCache->rgvarg, (jint)lpStruct->rgvarg);
}

#ifndef _WIN32_WCE

void cacheDROPFILESFids(JNIEnv *env, jobject lpObject, PDROPFILES_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->fWide = (*env)->GetFieldID(env, lpCache->clazz, "fWide", "I");
	lpCache->fNC = (*env)->GetFieldID(env, lpCache->clazz, "fNC", "I");
	lpCache->pt_y = (*env)->GetFieldID(env, lpCache->clazz, "pt_y", "I");
	lpCache->pt_x = (*env)->GetFieldID(env, lpCache->clazz, "pt_x", "I");
	lpCache->pFiles = (*env)->GetFieldID(env, lpCache->clazz, "pFiles", "I");
	lpCache->cached = 1;
}

DROPFILES* getDROPFILESFields(JNIEnv *env, jobject lpObject, DROPFILES *lpStruct, PDROPFILES_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheDROPFILESFids(env, lpObject, lpCache);
	lpStruct->fWide = (*env)->GetIntField(env, lpObject, lpCache->fWide);
	lpStruct->fNC = (*env)->GetIntField(env, lpObject, lpCache->fNC);
	lpStruct->pt.y = (*env)->GetIntField(env, lpObject, lpCache->pt_y);
	lpStruct->pt.x = (*env)->GetIntField(env, lpObject, lpCache->pt_x);
	lpStruct->pFiles = (*env)->GetIntField(env, lpObject, lpCache->pFiles);
	return lpStruct;
}

void setDROPFILESFields(JNIEnv *env, jobject lpObject, DROPFILES *lpStruct, PDROPFILES_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheDROPFILESFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->fWide, lpStruct->fWide);
	(*env)->SetIntField(env, lpObject, lpCache->fNC, lpStruct->fNC);
	(*env)->SetIntField(env, lpObject, lpCache->pt_y, lpStruct->pt.y);
	(*env)->SetIntField(env, lpObject, lpCache->pt_x, lpStruct->pt.x);
	(*env)->SetIntField(env, lpObject, lpCache->pFiles, lpStruct->pFiles);
}

#endif // _WIN32_WCE

void cacheDVTARGETDEVICEFids(JNIEnv *env, jobject lpObject, PDVTARGETDEVICE_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->tdData = (*env)->GetFieldID(env, lpCache->clazz, "tdData", "B");
	lpCache->tdExtDevmodeOffset = (*env)->GetFieldID(env, lpCache->clazz, "tdExtDevmodeOffset", "S");
	lpCache->tdPortNameOffset = (*env)->GetFieldID(env, lpCache->clazz, "tdPortNameOffset", "S");
	lpCache->tdDeviceNameOffset = (*env)->GetFieldID(env, lpCache->clazz, "tdDeviceNameOffset", "S");
	lpCache->tdDriverNameOffset = (*env)->GetFieldID(env, lpCache->clazz, "tdDriverNameOffset", "S");
	lpCache->tdSize = (*env)->GetFieldID(env, lpCache->clazz, "tdSize", "I");
	lpCache->cached = 1;
}

DVTARGETDEVICE* getDVTARGETDEVICEFields(JNIEnv *env, jobject lpObject, DVTARGETDEVICE *lpStruct, PDVTARGETDEVICE_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheDVTARGETDEVICEFids(env, lpObject, lpCache);
	*lpStruct->tdData = (*env)->GetByteField(env, lpObject, lpCache->tdData);
	lpStruct->tdExtDevmodeOffset = (*env)->GetShortField(env, lpObject, lpCache->tdExtDevmodeOffset);
	lpStruct->tdPortNameOffset = (*env)->GetShortField(env, lpObject, lpCache->tdPortNameOffset);
	lpStruct->tdDeviceNameOffset = (*env)->GetShortField(env, lpObject, lpCache->tdDeviceNameOffset);
	lpStruct->tdDriverNameOffset = (*env)->GetShortField(env, lpObject, lpCache->tdDriverNameOffset);
	lpStruct->tdSize = (*env)->GetIntField(env, lpObject, lpCache->tdSize);
	return lpStruct;
}

void setDVTARGETDEVICEFields(JNIEnv *env, jobject lpObject, DVTARGETDEVICE *lpStruct, PDVTARGETDEVICE_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheDVTARGETDEVICEFids(env, lpObject, lpCache);
	(*env)->SetByteField(env, lpObject, lpCache->tdData, *lpStruct->tdData);
	(*env)->SetShortField(env, lpObject, lpCache->tdExtDevmodeOffset, lpStruct->tdExtDevmodeOffset);
	(*env)->SetShortField(env, lpObject, lpCache->tdPortNameOffset, lpStruct->tdPortNameOffset);
	(*env)->SetShortField(env, lpObject, lpCache->tdDeviceNameOffset, lpStruct->tdDeviceNameOffset);
	(*env)->SetShortField(env, lpObject, lpCache->tdDriverNameOffset, lpStruct->tdDriverNameOffset);
	(*env)->SetIntField(env, lpObject, lpCache->tdSize, lpStruct->tdSize);
}

void cacheEXCEPINFOFids(JNIEnv *env, jobject lpObject, PEXCEPINFO_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->scode = (*env)->GetFieldID(env, lpCache->clazz, "scode", "I");
	lpCache->pfnDeferredFillIn = (*env)->GetFieldID(env, lpCache->clazz, "pfnDeferredFillIn", "I");
	lpCache->pvReserved = (*env)->GetFieldID(env, lpCache->clazz, "pvReserved", "I");
	lpCache->dwHelpContext = (*env)->GetFieldID(env, lpCache->clazz, "dwHelpContext", "I");
	lpCache->bstrHelpFile = (*env)->GetFieldID(env, lpCache->clazz, "bstrHelpFile", "I");
	lpCache->bstrDescription = (*env)->GetFieldID(env, lpCache->clazz, "bstrDescription", "I");
	lpCache->bstrSource = (*env)->GetFieldID(env, lpCache->clazz, "bstrSource", "I");
	lpCache->wReserved = (*env)->GetFieldID(env, lpCache->clazz, "wReserved", "S");
	lpCache->wCode = (*env)->GetFieldID(env, lpCache->clazz, "wCode", "S");
	lpCache->cached = 1;
}

EXCEPINFO* getEXCEPINFOFields(JNIEnv *env, jobject lpObject, EXCEPINFO *lpStruct, PEXCEPINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheEXCEPINFOFids(env, lpObject, lpCache);
	lpStruct->scode = (*env)->GetIntField(env, lpObject, lpCache->scode);
	lpStruct->pfnDeferredFillIn = (HRESULT (STDAPICALLTYPE FAR* )(struct tagEXCEPINFO FAR*))(*env)->GetIntField(env, lpObject, lpCache->pfnDeferredFillIn);
	lpStruct->pvReserved = (void FAR *)(*env)->GetIntField(env, lpObject, lpCache->pvReserved);
	lpStruct->dwHelpContext = (*env)->GetIntField(env, lpObject, lpCache->dwHelpContext);
	lpStruct->bstrHelpFile = (BSTR)(*env)->GetIntField(env, lpObject, lpCache->bstrHelpFile);
	lpStruct->bstrDescription = (BSTR)(*env)->GetIntField(env, lpObject, lpCache->bstrDescription);
	lpStruct->bstrSource = (BSTR)(*env)->GetIntField(env, lpObject, lpCache->bstrSource);
	lpStruct->wReserved = (*env)->GetShortField(env, lpObject, lpCache->wReserved);
	lpStruct->wCode = (*env)->GetShortField(env, lpObject, lpCache->wCode);
	return lpStruct;
}

void setEXCEPINFOFields(JNIEnv *env, jobject lpObject, EXCEPINFO *lpStruct, PEXCEPINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheEXCEPINFOFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->scode, lpStruct->scode);
	(*env)->SetIntField(env, lpObject, lpCache->pfnDeferredFillIn, (jint)lpStruct->pfnDeferredFillIn);
	(*env)->SetIntField(env, lpObject, lpCache->pvReserved, (jint)lpStruct->pvReserved);
	(*env)->SetIntField(env, lpObject, lpCache->dwHelpContext, lpStruct->dwHelpContext);
	(*env)->SetIntField(env, lpObject, lpCache->bstrHelpFile, (jint)lpStruct->bstrHelpFile);
	(*env)->SetIntField(env, lpObject, lpCache->bstrDescription, (jint)lpStruct->bstrDescription);
	(*env)->SetIntField(env, lpObject, lpCache->bstrSource, (jint)lpStruct->bstrSource);
	(*env)->SetShortField(env, lpObject, lpCache->wReserved, lpStruct->wReserved);
	(*env)->SetShortField(env, lpObject, lpCache->wCode, lpStruct->wCode);
}

void cacheFORMATETCFids(JNIEnv *env, jobject lpObject, PFORMATETC_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->tymed = (*env)->GetFieldID(env, lpCache->clazz, "tymed", "I");
	lpCache->lindex = (*env)->GetFieldID(env, lpCache->clazz, "lindex", "I");
	lpCache->dwAspect = (*env)->GetFieldID(env, lpCache->clazz, "dwAspect", "I");
	lpCache->ptd = (*env)->GetFieldID(env, lpCache->clazz, "ptd", "I");
	lpCache->cfFormat = (*env)->GetFieldID(env, lpCache->clazz, "cfFormat", "I");
	lpCache->cached = 1;
}

FORMATETC* getFORMATETCFields(JNIEnv *env, jobject lpObject, FORMATETC *lpStruct, PFORMATETC_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheFORMATETCFids(env, lpObject, lpCache);
	lpStruct->tymed = (*env)->GetIntField(env, lpObject, lpCache->tymed);
	lpStruct->lindex = (*env)->GetIntField(env, lpObject, lpCache->lindex);
	lpStruct->dwAspect = (*env)->GetIntField(env, lpObject, lpCache->dwAspect);
	lpStruct->ptd = (DVTARGETDEVICE *)(*env)->GetIntField(env, lpObject, lpCache->ptd);
	lpStruct->cfFormat = (CLIPFORMAT)(*env)->GetIntField(env, lpObject, lpCache->cfFormat);
	return lpStruct;
}

void setFORMATETCFields(JNIEnv *env, jobject lpObject, FORMATETC *lpStruct, PFORMATETC_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheFORMATETCFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->tymed, lpStruct->tymed);
	(*env)->SetIntField(env, lpObject, lpCache->lindex, lpStruct->lindex);
	(*env)->SetIntField(env, lpObject, lpCache->dwAspect, lpStruct->dwAspect);
	(*env)->SetIntField(env, lpObject, lpCache->ptd, (jint)lpStruct->ptd);
	(*env)->SetIntField(env, lpObject, lpCache->cfFormat, (jint)lpStruct->cfFormat);
}

void cacheFUNCDESC1Fids(JNIEnv *env, jobject lpObject, PFUNCDESC1_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->wFuncFlags = (*env)->GetFieldID(env, lpCache->clazz, "wFuncFlags", "S");
	lpCache->elemdescFunc_paramdesc_wParamFlags = (*env)->GetFieldID(env, lpCache->clazz, "elemdescFunc_paramdesc_wParamFlags", "S");
	lpCache->elemdescFunc_paramdesc_pparamdescex = (*env)->GetFieldID(env, lpCache->clazz, "elemdescFunc_paramdesc_pparamdescex", "I");
	lpCache->elemdescFunc_tdesc_vt = (*env)->GetFieldID(env, lpCache->clazz, "elemdescFunc_tdesc_vt", "S");
	lpCache->elemdescFunc_tdesc_union = (*env)->GetFieldID(env, lpCache->clazz, "elemdescFunc_tdesc_union", "I");
	lpCache->cScodes = (*env)->GetFieldID(env, lpCache->clazz, "cScodes", "S");
	lpCache->oVft = (*env)->GetFieldID(env, lpCache->clazz, "oVft", "S");
	lpCache->cParamsOpt = (*env)->GetFieldID(env, lpCache->clazz, "cParamsOpt", "S");
	lpCache->cParams = (*env)->GetFieldID(env, lpCache->clazz, "cParams", "S");
	lpCache->callconv = (*env)->GetFieldID(env, lpCache->clazz, "callconv", "I");
	lpCache->invkind = (*env)->GetFieldID(env, lpCache->clazz, "invkind", "I");
	lpCache->funckind = (*env)->GetFieldID(env, lpCache->clazz, "funckind", "I");
	lpCache->lprgelemdescParam = (*env)->GetFieldID(env, lpCache->clazz, "lprgelemdescParam", "I");
	lpCache->lprgscode = (*env)->GetFieldID(env, lpCache->clazz, "lprgscode", "I");
	lpCache->memid = (*env)->GetFieldID(env, lpCache->clazz, "memid", "I");
	lpCache->cached = 1;
}

FUNCDESC* getFUNCDESC1Fields(JNIEnv *env, jobject lpObject, FUNCDESC *lpStruct, PFUNCDESC1_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheFUNCDESC1Fids(env, lpObject, lpCache);
	lpStruct->wFuncFlags = (*env)->GetShortField(env, lpObject, lpCache->wFuncFlags);
	lpStruct->elemdescFunc.paramdesc.wParamFlags = (*env)->GetShortField(env, lpObject, lpCache->elemdescFunc_paramdesc_wParamFlags);
	lpStruct->elemdescFunc.paramdesc.pparamdescex = (LPPARAMDESCEX)(*env)->GetIntField(env, lpObject, lpCache->elemdescFunc_paramdesc_pparamdescex);
	lpStruct->elemdescFunc.tdesc.vt = (*env)->GetShortField(env, lpObject, lpCache->elemdescFunc_tdesc_vt);
	lpStruct->elemdescFunc.tdesc.lptdesc = (struct FARSTRUCT tagTYPEDESC FAR* )(*env)->GetIntField(env, lpObject, lpCache->elemdescFunc_tdesc_union);
	lpStruct->cScodes = (*env)->GetShortField(env, lpObject, lpCache->cScodes);
	lpStruct->oVft = (*env)->GetShortField(env, lpObject, lpCache->oVft);
	lpStruct->cParamsOpt = (*env)->GetShortField(env, lpObject, lpCache->cParamsOpt);
	lpStruct->cParams = (*env)->GetShortField(env, lpObject, lpCache->cParams);
	lpStruct->callconv = (*env)->GetIntField(env, lpObject, lpCache->callconv);
	lpStruct->invkind = (*env)->GetIntField(env, lpObject, lpCache->invkind);
	lpStruct->funckind = (*env)->GetIntField(env, lpObject, lpCache->funckind);
	lpStruct->lprgelemdescParam = (ELEMDESC FAR *)(*env)->GetIntField(env, lpObject, lpCache->lprgelemdescParam);
	lpStruct->lprgscode = (SCODE FAR *)(*env)->GetIntField(env, lpObject, lpCache->lprgscode);
	lpStruct->memid = (*env)->GetIntField(env, lpObject, lpCache->memid);
	return lpStruct;
}

void setFUNCDESC1Fields(JNIEnv *env, jobject lpObject, FUNCDESC *lpStruct, PFUNCDESC1_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheFUNCDESC1Fids(env, lpObject, lpCache);
	(*env)->SetShortField(env, lpObject, lpCache->wFuncFlags, lpStruct->wFuncFlags);
	(*env)->SetShortField(env, lpObject, lpCache->elemdescFunc_paramdesc_wParamFlags, lpStruct->elemdescFunc.paramdesc.wParamFlags);
	(*env)->SetIntField(env, lpObject, lpCache->elemdescFunc_paramdesc_pparamdescex, (jint)lpStruct->elemdescFunc.paramdesc.pparamdescex);
	(*env)->SetShortField(env, lpObject, lpCache->elemdescFunc_tdesc_vt, lpStruct->elemdescFunc.tdesc.vt);
	(*env)->SetIntField(env, lpObject, lpCache->elemdescFunc_tdesc_union, (jint)lpStruct->elemdescFunc.tdesc.lptdesc);
	(*env)->SetShortField(env, lpObject, lpCache->cScodes, lpStruct->cScodes);
	(*env)->SetShortField(env, lpObject, lpCache->oVft, lpStruct->oVft);
	(*env)->SetShortField(env, lpObject, lpCache->cParamsOpt, lpStruct->cParamsOpt);
	(*env)->SetShortField(env, lpObject, lpCache->cParams, lpStruct->cParams);
	(*env)->SetIntField(env, lpObject, lpCache->callconv, lpStruct->callconv);
	(*env)->SetIntField(env, lpObject, lpCache->invkind, lpStruct->invkind);
	(*env)->SetIntField(env, lpObject, lpCache->funckind, lpStruct->funckind);
	(*env)->SetIntField(env, lpObject, lpCache->lprgelemdescParam, (jint)lpStruct->lprgelemdescParam);
	(*env)->SetIntField(env, lpObject, lpCache->lprgscode, (jint)lpStruct->lprgscode);
	(*env)->SetIntField(env, lpObject, lpCache->memid, lpStruct->memid);
}

void cacheFUNCDESC2Fids(JNIEnv *env, jobject lpObject, PFUNCDESC2_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->wFuncFlags = (*env)->GetFieldID(env, lpCache->clazz, "wFuncFlags", "S");
	lpCache->elemdescFunc_idldesc_wIDLFlags = (*env)->GetFieldID(env, lpCache->clazz, "elemdescFunc_idldesc_wIDLFlags", "S");
	lpCache->elemdescFunc_idldesc_dwReserved = (*env)->GetFieldID(env, lpCache->clazz, "elemdescFunc_idldesc_dwReserved", "I");
	lpCache->elemdescFunc_tdesc_vt = (*env)->GetFieldID(env, lpCache->clazz, "elemdescFunc_tdesc_vt", "S");
	lpCache->elemdescFunc_tdesc_union = (*env)->GetFieldID(env, lpCache->clazz, "elemdescFunc_tdesc_union", "I");
	lpCache->cScodes = (*env)->GetFieldID(env, lpCache->clazz, "cScodes", "S");
	lpCache->oVft = (*env)->GetFieldID(env, lpCache->clazz, "oVft", "S");
	lpCache->cParamsOpt = (*env)->GetFieldID(env, lpCache->clazz, "cParamsOpt", "S");
	lpCache->cParams = (*env)->GetFieldID(env, lpCache->clazz, "cParams", "S");
	lpCache->callconv = (*env)->GetFieldID(env, lpCache->clazz, "callconv", "I");
	lpCache->invkind = (*env)->GetFieldID(env, lpCache->clazz, "invkind", "I");
	lpCache->funckind = (*env)->GetFieldID(env, lpCache->clazz, "funckind", "I");
	lpCache->lprgelemdescParam = (*env)->GetFieldID(env, lpCache->clazz, "lprgelemdescParam", "I");
	lpCache->lprgscode = (*env)->GetFieldID(env, lpCache->clazz, "lprgscode", "I");
	lpCache->memid = (*env)->GetFieldID(env, lpCache->clazz, "memid", "I");
	lpCache->cached = 1;
}

FUNCDESC* getFUNCDESC2Fields(JNIEnv *env, jobject lpObject, FUNCDESC *lpStruct, PFUNCDESC2_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheFUNCDESC2Fids(env, lpObject, lpCache);
	lpStruct->wFuncFlags = (*env)->GetShortField(env, lpObject, lpCache->wFuncFlags);
	lpStruct->elemdescFunc.idldesc.wIDLFlags = (*env)->GetShortField(env, lpObject, lpCache->elemdescFunc_idldesc_wIDLFlags);
	lpStruct->elemdescFunc.idldesc.dwReserved = (*env)->GetIntField(env, lpObject, lpCache->elemdescFunc_idldesc_dwReserved);
	lpStruct->elemdescFunc.tdesc.vt = (*env)->GetShortField(env, lpObject, lpCache->elemdescFunc_tdesc_vt);
	lpStruct->elemdescFunc.tdesc.lptdesc = (struct FARSTRUCT tagTYPEDESC FAR* )(*env)->GetIntField(env, lpObject, lpCache->elemdescFunc_tdesc_union);
	lpStruct->cScodes = (*env)->GetShortField(env, lpObject, lpCache->cScodes);
	lpStruct->oVft = (*env)->GetShortField(env, lpObject, lpCache->oVft);
	lpStruct->cParamsOpt = (*env)->GetShortField(env, lpObject, lpCache->cParamsOpt);
	lpStruct->cParams = (*env)->GetShortField(env, lpObject, lpCache->cParams);
	lpStruct->callconv = (*env)->GetIntField(env, lpObject, lpCache->callconv);
	lpStruct->invkind = (*env)->GetIntField(env, lpObject, lpCache->invkind);
	lpStruct->funckind = (*env)->GetIntField(env, lpObject, lpCache->funckind);
	lpStruct->lprgelemdescParam = (ELEMDESC FAR *)(*env)->GetIntField(env, lpObject, lpCache->lprgelemdescParam);
	lpStruct->lprgscode = (SCODE FAR *)(*env)->GetIntField(env, lpObject, lpCache->lprgscode);
	lpStruct->memid = (*env)->GetIntField(env, lpObject, lpCache->memid);
	return lpStruct;
}

void setFUNCDESC2Fields(JNIEnv *env, jobject lpObject, FUNCDESC *lpStruct, PFUNCDESC2_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheFUNCDESC2Fids(env, lpObject, lpCache);
	(*env)->SetShortField(env, lpObject, lpCache->wFuncFlags, lpStruct->wFuncFlags);
	(*env)->SetShortField(env, lpObject, lpCache->elemdescFunc_idldesc_wIDLFlags, lpStruct->elemdescFunc.idldesc.wIDLFlags);
	(*env)->SetIntField(env, lpObject, lpCache->elemdescFunc_idldesc_dwReserved, lpStruct->elemdescFunc.idldesc.dwReserved);
	(*env)->SetShortField(env, lpObject, lpCache->elemdescFunc_tdesc_vt, lpStruct->elemdescFunc.tdesc.vt);
	(*env)->SetIntField(env, lpObject, lpCache->elemdescFunc_tdesc_union, (jint)lpStruct->elemdescFunc.tdesc.lptdesc);
	(*env)->SetShortField(env, lpObject, lpCache->cScodes, lpStruct->cScodes);
	(*env)->SetShortField(env, lpObject, lpCache->oVft, lpStruct->oVft);
	(*env)->SetShortField(env, lpObject, lpCache->cParamsOpt, lpStruct->cParamsOpt);
	(*env)->SetShortField(env, lpObject, lpCache->cParams, lpStruct->cParams);
	(*env)->SetIntField(env, lpObject, lpCache->callconv, lpStruct->callconv);
	(*env)->SetIntField(env, lpObject, lpCache->invkind, lpStruct->invkind);
	(*env)->SetIntField(env, lpObject, lpCache->funckind, lpStruct->funckind);
	(*env)->SetIntField(env, lpObject, lpCache->lprgelemdescParam, (jint)lpStruct->lprgelemdescParam);
	(*env)->SetIntField(env, lpObject, lpCache->lprgscode, (jint)lpStruct->lprgscode);
	(*env)->SetIntField(env, lpObject, lpCache->memid, lpStruct->memid);
}

void cacheGUIDFids(JNIEnv *env, jobject lpObject, PGUID_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->b7 = (*env)->GetFieldID(env, lpCache->clazz, "b7", "B");
	lpCache->b6 = (*env)->GetFieldID(env, lpCache->clazz, "b6", "B");
	lpCache->b5 = (*env)->GetFieldID(env, lpCache->clazz, "b5", "B");
	lpCache->b4 = (*env)->GetFieldID(env, lpCache->clazz, "b4", "B");
	lpCache->b3 = (*env)->GetFieldID(env, lpCache->clazz, "b3", "B");
	lpCache->b2 = (*env)->GetFieldID(env, lpCache->clazz, "b2", "B");
	lpCache->b1 = (*env)->GetFieldID(env, lpCache->clazz, "b1", "B");
	lpCache->b0 = (*env)->GetFieldID(env, lpCache->clazz, "b0", "B");
	lpCache->data3 = (*env)->GetFieldID(env, lpCache->clazz, "data3", "S");
	lpCache->data2 = (*env)->GetFieldID(env, lpCache->clazz, "data2", "S");
	lpCache->data1 = (*env)->GetFieldID(env, lpCache->clazz, "data1", "I");
	lpCache->cached = 1;
}

GUID* getGUIDFields(JNIEnv *env, jobject lpObject, GUID *lpStruct, PGUID_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheGUIDFids(env, lpObject, lpCache);
	lpStruct->Data4[7] = (*env)->GetByteField(env, lpObject, lpCache->b7);
	lpStruct->Data4[6] = (*env)->GetByteField(env, lpObject, lpCache->b6);
	lpStruct->Data4[5] = (*env)->GetByteField(env, lpObject, lpCache->b5);
	lpStruct->Data4[4] = (*env)->GetByteField(env, lpObject, lpCache->b4);
	lpStruct->Data4[3] = (*env)->GetByteField(env, lpObject, lpCache->b3);
	lpStruct->Data4[2] = (*env)->GetByteField(env, lpObject, lpCache->b2);
	lpStruct->Data4[1] = (*env)->GetByteField(env, lpObject, lpCache->b1);
	lpStruct->Data4[0] = (*env)->GetByteField(env, lpObject, lpCache->b0);
	lpStruct->Data3 = (*env)->GetShortField(env, lpObject, lpCache->data3);
	lpStruct->Data2 = (*env)->GetShortField(env, lpObject, lpCache->data2);
	lpStruct->Data1 = (*env)->GetIntField(env, lpObject, lpCache->data1);
	return lpStruct;
}

void setGUIDFields(JNIEnv *env, jobject lpObject, GUID *lpStruct, PGUID_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheGUIDFids(env, lpObject, lpCache);
	(*env)->SetByteField(env, lpObject, lpCache->b7, lpStruct->Data4[7]);
	(*env)->SetByteField(env, lpObject, lpCache->b6, lpStruct->Data4[6]);
	(*env)->SetByteField(env, lpObject, lpCache->b5, lpStruct->Data4[5]);
	(*env)->SetByteField(env, lpObject, lpCache->b4, lpStruct->Data4[4]);
	(*env)->SetByteField(env, lpObject, lpCache->b3, lpStruct->Data4[3]);
	(*env)->SetByteField(env, lpObject, lpCache->b2, lpStruct->Data4[2]);
	(*env)->SetByteField(env, lpObject, lpCache->b1, lpStruct->Data4[1]);
	(*env)->SetByteField(env, lpObject, lpCache->b0, lpStruct->Data4[0]);
	(*env)->SetShortField(env, lpObject, lpCache->data3, lpStruct->Data3);
	(*env)->SetShortField(env, lpObject, lpCache->data2, lpStruct->Data2);
	(*env)->SetIntField(env, lpObject, lpCache->data1, lpStruct->Data1);
}

void cacheLICINFOFids(JNIEnv *env, jobject lpObject, PLICINFO_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->fLicVerified = (*env)->GetFieldID(env, lpCache->clazz, "fLicVerified", "I");
	lpCache->fRuntimeKeyAvail = (*env)->GetFieldID(env, lpCache->clazz, "fRuntimeKeyAvail", "I");
	lpCache->cbLicInfo = (*env)->GetFieldID(env, lpCache->clazz, "cbLicInfo", "I");
	lpCache->cached = 1;
}

LICINFO* getLICINFOFields(JNIEnv *env, jobject lpObject, LICINFO *lpStruct, PLICINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheLICINFOFids(env, lpObject, lpCache);
	lpStruct->fLicVerified = (*env)->GetIntField(env, lpObject, lpCache->fLicVerified);
	lpStruct->fRuntimeKeyAvail = (*env)->GetIntField(env, lpObject, lpCache->fRuntimeKeyAvail);
	lpStruct->cbLicInfo = (*env)->GetIntField(env, lpObject, lpCache->cbLicInfo);
	return lpStruct;
}

void setLICINFOFields(JNIEnv *env, jobject lpObject, LICINFO *lpStruct, PLICINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheLICINFOFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->fLicVerified, lpStruct->fLicVerified);
	(*env)->SetIntField(env, lpObject, lpCache->fRuntimeKeyAvail, lpStruct->fRuntimeKeyAvail);
	(*env)->SetIntField(env, lpObject, lpCache->cbLicInfo, lpStruct->cbLicInfo);
}

#ifndef _WIN32_WCE

void cacheOLECMDFids(JNIEnv *env, jobject lpObject, POLECMD_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->cmdf = (*env)->GetFieldID(env, lpCache->clazz, "cmdf", "I");
	lpCache->cmdID = (*env)->GetFieldID(env, lpCache->clazz, "cmdID", "I");
	lpCache->cached = 1;
}

OLECMD* getOLECMDFields(JNIEnv *env, jobject lpObject, OLECMD *lpStruct, POLECMD_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheOLECMDFids(env, lpObject, lpCache);
	lpStruct->cmdf = (*env)->GetIntField(env, lpObject, lpCache->cmdf);
	lpStruct->cmdID = (*env)->GetIntField(env, lpObject, lpCache->cmdID);
	return lpStruct;
}

void setOLECMDFields(JNIEnv *env, jobject lpObject, OLECMD *lpStruct, POLECMD_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheOLECMDFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->cmdf, lpStruct->cmdf);
	(*env)->SetIntField(env, lpObject, lpCache->cmdID, lpStruct->cmdID);
}

#endif // _WIN32_WCE

#ifndef _WIN32_WCE

void cacheOLECMDTEXTFids(JNIEnv *env, jobject lpObject, POLECMDTEXT_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->rgwz = (*env)->GetFieldID(env, lpCache->clazz, "rgwz", "S");
	lpCache->cwBuf = (*env)->GetFieldID(env, lpCache->clazz, "cwBuf", "I");
	lpCache->cwActual = (*env)->GetFieldID(env, lpCache->clazz, "cwActual", "I");
	lpCache->cmdtextf = (*env)->GetFieldID(env, lpCache->clazz, "cmdtextf", "I");
	lpCache->cached = 1;
}

OLECMDTEXT* getOLECMDTEXTFields(JNIEnv *env, jobject lpObject, OLECMDTEXT *lpStruct, POLECMDTEXT_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheOLECMDTEXTFids(env, lpObject, lpCache);
	lpStruct->rgwz[0] = (*env)->GetShortField(env, lpObject, lpCache->rgwz);
	lpStruct->cwBuf = (*env)->GetIntField(env, lpObject, lpCache->cwBuf);
	lpStruct->cwActual = (*env)->GetIntField(env, lpObject, lpCache->cwActual);
	lpStruct->cmdtextf = (*env)->GetIntField(env, lpObject, lpCache->cmdtextf);
	return lpStruct;
}

void setOLECMDTEXTFields(JNIEnv *env, jobject lpObject, OLECMDTEXT *lpStruct, POLECMDTEXT_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheOLECMDTEXTFids(env, lpObject, lpCache);
	(*env)->SetShortField(env, lpObject, lpCache->rgwz, lpStruct->rgwz[0]);
	(*env)->SetIntField(env, lpObject, lpCache->cwBuf, lpStruct->cwBuf);
	(*env)->SetIntField(env, lpObject, lpCache->cwActual, lpStruct->cwActual);
	(*env)->SetIntField(env, lpObject, lpCache->cmdtextf, lpStruct->cmdtextf);
}

#endif // _WIN32_WCE

void cacheOLEINPLACEFRAMEINFOFids(JNIEnv *env, jobject lpObject, POLEINPLACEFRAMEINFO_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->cAccelEntries = (*env)->GetFieldID(env, lpCache->clazz, "cAccelEntries", "I");
	lpCache->haccel = (*env)->GetFieldID(env, lpCache->clazz, "haccel", "I");
	lpCache->hwndFrame = (*env)->GetFieldID(env, lpCache->clazz, "hwndFrame", "I");
	lpCache->fMDIApp = (*env)->GetFieldID(env, lpCache->clazz, "fMDIApp", "I");
	lpCache->cb = (*env)->GetFieldID(env, lpCache->clazz, "cb", "I");
	lpCache->cached = 1;
}

OLEINPLACEFRAMEINFO* getOLEINPLACEFRAMEINFOFields(JNIEnv *env, jobject lpObject, OLEINPLACEFRAMEINFO *lpStruct, POLEINPLACEFRAMEINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheOLEINPLACEFRAMEINFOFids(env, lpObject, lpCache);
	lpStruct->cAccelEntries = (*env)->GetIntField(env, lpObject, lpCache->cAccelEntries);
	lpStruct->haccel = (HACCEL)(*env)->GetIntField(env, lpObject, lpCache->haccel);
	lpStruct->hwndFrame = (HWND)(*env)->GetIntField(env, lpObject, lpCache->hwndFrame);
	lpStruct->fMDIApp = (*env)->GetIntField(env, lpObject, lpCache->fMDIApp);
	lpStruct->cb = (*env)->GetIntField(env, lpObject, lpCache->cb);
	return lpStruct;
}

void setOLEINPLACEFRAMEINFOFields(JNIEnv *env, jobject lpObject, OLEINPLACEFRAMEINFO *lpStruct, POLEINPLACEFRAMEINFO_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheOLEINPLACEFRAMEINFOFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->cAccelEntries, lpStruct->cAccelEntries);
	(*env)->SetIntField(env, lpObject, lpCache->haccel, (jint)lpStruct->haccel);
	(*env)->SetIntField(env, lpObject, lpCache->hwndFrame, (jint)lpStruct->hwndFrame);
	(*env)->SetIntField(env, lpObject, lpCache->fMDIApp, lpStruct->fMDIApp);
	(*env)->SetIntField(env, lpObject, lpCache->cb, lpStruct->cb);
}

void cacheSTATSTGFids(JNIEnv *env, jobject lpObject, PSTATSTG_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->reserved = (*env)->GetFieldID(env, lpCache->clazz, "reserved", "I");
	lpCache->grfStateBits = (*env)->GetFieldID(env, lpCache->clazz, "grfStateBits", "I");
	lpCache->clsid_b7 = (*env)->GetFieldID(env, lpCache->clazz, "clsid_b7", "B");
	lpCache->clsid_b6 = (*env)->GetFieldID(env, lpCache->clazz, "clsid_b6", "B");
	lpCache->clsid_b5 = (*env)->GetFieldID(env, lpCache->clazz, "clsid_b5", "B");
	lpCache->clsid_b4 = (*env)->GetFieldID(env, lpCache->clazz, "clsid_b4", "B");
	lpCache->clsid_b3 = (*env)->GetFieldID(env, lpCache->clazz, "clsid_b3", "B");
	lpCache->clsid_b2 = (*env)->GetFieldID(env, lpCache->clazz, "clsid_b2", "B");
	lpCache->clsid_b1 = (*env)->GetFieldID(env, lpCache->clazz, "clsid_b1", "B");
	lpCache->clsid_b0 = (*env)->GetFieldID(env, lpCache->clazz, "clsid_b0", "B");
	lpCache->clsid_data3 = (*env)->GetFieldID(env, lpCache->clazz, "clsid_data3", "S");
	lpCache->clsid_data2 = (*env)->GetFieldID(env, lpCache->clazz, "clsid_data2", "S");
	lpCache->clsid_data1 = (*env)->GetFieldID(env, lpCache->clazz, "clsid_data1", "I");
	lpCache->grfLocksSupported = (*env)->GetFieldID(env, lpCache->clazz, "grfLocksSupported", "I");
	lpCache->grfMode = (*env)->GetFieldID(env, lpCache->clazz, "grfMode", "I");
	lpCache->atime_dwHighDateTime = (*env)->GetFieldID(env, lpCache->clazz, "atime_dwHighDateTime", "I");
	lpCache->atime_dwLowDateTime = (*env)->GetFieldID(env, lpCache->clazz, "atime_dwLowDateTime", "I");
	lpCache->ctime_dwHighDateTime = (*env)->GetFieldID(env, lpCache->clazz, "ctime_dwHighDateTime", "I");
	lpCache->ctime_dwLowDateTime = (*env)->GetFieldID(env, lpCache->clazz, "ctime_dwLowDateTime", "I");
	lpCache->mtime_dwHighDateTime = (*env)->GetFieldID(env, lpCache->clazz, "mtime_dwHighDateTime", "I");
	lpCache->mtime_dwLowDateTime = (*env)->GetFieldID(env, lpCache->clazz, "mtime_dwLowDateTime", "I");
	lpCache->cbSize = (*env)->GetFieldID(env, lpCache->clazz, "cbSize", "J");
	lpCache->type = (*env)->GetFieldID(env, lpCache->clazz, "type", "I");
	lpCache->pwcsName = (*env)->GetFieldID(env, lpCache->clazz, "pwcsName", "I");
	lpCache->cached = 1;
}

STATSTG* getSTATSTGFields(JNIEnv *env, jobject lpObject, STATSTG *lpStruct, PSTATSTG_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheSTATSTGFids(env, lpObject, lpCache);
	lpStruct->reserved = (*env)->GetIntField(env, lpObject, lpCache->reserved);
	lpStruct->grfStateBits = (*env)->GetIntField(env, lpObject, lpCache->grfStateBits);
	lpStruct->clsid.Data4[7] = (*env)->GetByteField(env, lpObject, lpCache->clsid_b7);
	lpStruct->clsid.Data4[6] = (*env)->GetByteField(env, lpObject, lpCache->clsid_b6);
	lpStruct->clsid.Data4[5] = (*env)->GetByteField(env, lpObject, lpCache->clsid_b5);
	lpStruct->clsid.Data4[4] = (*env)->GetByteField(env, lpObject, lpCache->clsid_b4);
	lpStruct->clsid.Data4[3] = (*env)->GetByteField(env, lpObject, lpCache->clsid_b3);
	lpStruct->clsid.Data4[2] = (*env)->GetByteField(env, lpObject, lpCache->clsid_b2);
	lpStruct->clsid.Data4[1] = (*env)->GetByteField(env, lpObject, lpCache->clsid_b1);
	lpStruct->clsid.Data4[0] = (*env)->GetByteField(env, lpObject, lpCache->clsid_b0);
	lpStruct->clsid.Data3 = (*env)->GetShortField(env, lpObject, lpCache->clsid_data3);
	lpStruct->clsid.Data2 = (*env)->GetShortField(env, lpObject, lpCache->clsid_data2);
	lpStruct->clsid.Data1 = (*env)->GetIntField(env, lpObject, lpCache->clsid_data1);
	lpStruct->grfLocksSupported = (*env)->GetIntField(env, lpObject, lpCache->grfLocksSupported);
	lpStruct->grfMode = (*env)->GetIntField(env, lpObject, lpCache->grfMode);
	lpStruct->atime.dwHighDateTime = (*env)->GetIntField(env, lpObject, lpCache->atime_dwHighDateTime);
	lpStruct->atime.dwLowDateTime = (*env)->GetIntField(env, lpObject, lpCache->atime_dwLowDateTime);
	lpStruct->ctime.dwHighDateTime = (*env)->GetIntField(env, lpObject, lpCache->ctime_dwHighDateTime);
	lpStruct->ctime.dwLowDateTime = (*env)->GetIntField(env, lpObject, lpCache->ctime_dwLowDateTime);
	lpStruct->mtime.dwHighDateTime = (*env)->GetIntField(env, lpObject, lpCache->mtime_dwHighDateTime);
	lpStruct->mtime.dwLowDateTime = (*env)->GetIntField(env, lpObject, lpCache->mtime_dwLowDateTime);
	lpStruct->cbSize.QuadPart = (*env)->GetLongField(env, lpObject, lpCache->cbSize);
	lpStruct->type = (*env)->GetIntField(env, lpObject, lpCache->type);
	lpStruct->pwcsName = (LPWSTR)(*env)->GetIntField(env, lpObject, lpCache->pwcsName);
	return lpStruct;
}

void setSTATSTGFields(JNIEnv *env, jobject lpObject, STATSTG *lpStruct, PSTATSTG_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheSTATSTGFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->reserved, lpStruct->reserved);
	(*env)->SetIntField(env, lpObject, lpCache->grfStateBits, lpStruct->grfStateBits);
	(*env)->SetByteField(env, lpObject, lpCache->clsid_b7, lpStruct->clsid.Data4[7]);
	(*env)->SetByteField(env, lpObject, lpCache->clsid_b6, lpStruct->clsid.Data4[6]);
	(*env)->SetByteField(env, lpObject, lpCache->clsid_b5, lpStruct->clsid.Data4[5]);
	(*env)->SetByteField(env, lpObject, lpCache->clsid_b4, lpStruct->clsid.Data4[4]);
	(*env)->SetByteField(env, lpObject, lpCache->clsid_b3, lpStruct->clsid.Data4[3]);
	(*env)->SetByteField(env, lpObject, lpCache->clsid_b2, lpStruct->clsid.Data4[2]);
	(*env)->SetByteField(env, lpObject, lpCache->clsid_b1, lpStruct->clsid.Data4[1]);
	(*env)->SetByteField(env, lpObject, lpCache->clsid_b0, lpStruct->clsid.Data4[0]);
	(*env)->SetShortField(env, lpObject, lpCache->clsid_data3, lpStruct->clsid.Data3);
	(*env)->SetShortField(env, lpObject, lpCache->clsid_data2, lpStruct->clsid.Data2);
	(*env)->SetIntField(env, lpObject, lpCache->clsid_data1, lpStruct->clsid.Data1);
	(*env)->SetIntField(env, lpObject, lpCache->grfLocksSupported, lpStruct->grfLocksSupported);
	(*env)->SetIntField(env, lpObject, lpCache->grfMode, lpStruct->grfMode);
	(*env)->SetIntField(env, lpObject, lpCache->atime_dwHighDateTime, lpStruct->atime.dwHighDateTime);
	(*env)->SetIntField(env, lpObject, lpCache->atime_dwLowDateTime, lpStruct->atime.dwLowDateTime);
	(*env)->SetIntField(env, lpObject, lpCache->ctime_dwHighDateTime, lpStruct->ctime.dwHighDateTime);
	(*env)->SetIntField(env, lpObject, lpCache->ctime_dwLowDateTime, lpStruct->ctime.dwLowDateTime);
	(*env)->SetIntField(env, lpObject, lpCache->mtime_dwHighDateTime, lpStruct->mtime.dwHighDateTime);
	(*env)->SetIntField(env, lpObject, lpCache->mtime_dwLowDateTime, lpStruct->mtime.dwLowDateTime);
	(*env)->SetLongField(env, lpObject, lpCache->cbSize, lpStruct->cbSize.QuadPart);
	(*env)->SetIntField(env, lpObject, lpCache->type, lpStruct->type);
	(*env)->SetIntField(env, lpObject, lpCache->pwcsName, (jint)lpStruct->pwcsName);
}

void cacheSTGMEDIUMFids(JNIEnv *env, jobject lpObject, PSTGMEDIUM_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->pUnkForRelease = (*env)->GetFieldID(env, lpCache->clazz, "pUnkForRelease", "I");
	lpCache->unionField = (*env)->GetFieldID(env, lpCache->clazz, "unionField", "I");
	lpCache->tymed = (*env)->GetFieldID(env, lpCache->clazz, "tymed", "I");
	lpCache->cached = 1;
}

STGMEDIUM* getSTGMEDIUMFields(JNIEnv *env, jobject lpObject, STGMEDIUM *lpStruct, PSTGMEDIUM_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheSTGMEDIUMFids(env, lpObject, lpCache);
	lpStruct->pUnkForRelease = (IUnknown *)(*env)->GetIntField(env, lpObject, lpCache->pUnkForRelease);
	lpStruct->hGlobal = (HGLOBAL)(*env)->GetIntField(env, lpObject, lpCache->unionField);
	lpStruct->tymed = (*env)->GetIntField(env, lpObject, lpCache->tymed);
	return lpStruct;
}

void setSTGMEDIUMFields(JNIEnv *env, jobject lpObject, STGMEDIUM *lpStruct, PSTGMEDIUM_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheSTGMEDIUMFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->pUnkForRelease, (jint)lpStruct->pUnkForRelease);
	(*env)->SetIntField(env, lpObject, lpCache->unionField, (jint)lpStruct->hGlobal);
	(*env)->SetIntField(env, lpObject, lpCache->tymed, lpStruct->tymed);
}

void cacheTYPEATTRFids(JNIEnv *env, jobject lpObject, PTYPEATTR_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->idldescType_wIDLFlags = (*env)->GetFieldID(env, lpCache->clazz, "idldescType_wIDLFlags", "S");
	lpCache->idldescType_dwReserved = (*env)->GetFieldID(env, lpCache->clazz, "idldescType_dwReserved", "I");
	lpCache->tdescAlias_vt = (*env)->GetFieldID(env, lpCache->clazz, "tdescAlias_vt", "S");
	lpCache->tdescAlias_unionField = (*env)->GetFieldID(env, lpCache->clazz, "tdescAlias_unionField", "I");
	lpCache->wMinorVerNum = (*env)->GetFieldID(env, lpCache->clazz, "wMinorVerNum", "S");
	lpCache->wMajorVerNum = (*env)->GetFieldID(env, lpCache->clazz, "wMajorVerNum", "S");
	lpCache->wTypeFlags = (*env)->GetFieldID(env, lpCache->clazz, "wTypeFlags", "S");
	lpCache->cbAlignment = (*env)->GetFieldID(env, lpCache->clazz, "cbAlignment", "S");
	lpCache->cbSizeVft = (*env)->GetFieldID(env, lpCache->clazz, "cbSizeVft", "S");
	lpCache->cImplTypes = (*env)->GetFieldID(env, lpCache->clazz, "cImplTypes", "S");
	lpCache->cVars = (*env)->GetFieldID(env, lpCache->clazz, "cVars", "S");
	lpCache->cFuncs = (*env)->GetFieldID(env, lpCache->clazz, "cFuncs", "S");
	lpCache->typekind = (*env)->GetFieldID(env, lpCache->clazz, "typekind", "I");
	lpCache->cbSizeInstance = (*env)->GetFieldID(env, lpCache->clazz, "cbSizeInstance", "I");
	lpCache->lpstrSchema = (*env)->GetFieldID(env, lpCache->clazz, "lpstrSchema", "I");
	lpCache->memidDestructor = (*env)->GetFieldID(env, lpCache->clazz, "memidDestructor", "I");
	lpCache->memidConstructor = (*env)->GetFieldID(env, lpCache->clazz, "memidConstructor", "I");
	lpCache->dwReserved = (*env)->GetFieldID(env, lpCache->clazz, "dwReserved", "I");
	lpCache->lcid = (*env)->GetFieldID(env, lpCache->clazz, "lcid", "I");
	lpCache->guid_b7 = (*env)->GetFieldID(env, lpCache->clazz, "guid_b7", "B");
	lpCache->guid_b6 = (*env)->GetFieldID(env, lpCache->clazz, "guid_b6", "B");
	lpCache->guid_b5 = (*env)->GetFieldID(env, lpCache->clazz, "guid_b5", "B");
	lpCache->guid_b4 = (*env)->GetFieldID(env, lpCache->clazz, "guid_b4", "B");
	lpCache->guid_b3 = (*env)->GetFieldID(env, lpCache->clazz, "guid_b3", "B");
	lpCache->guid_b2 = (*env)->GetFieldID(env, lpCache->clazz, "guid_b2", "B");
	lpCache->guid_b1 = (*env)->GetFieldID(env, lpCache->clazz, "guid_b1", "B");
	lpCache->guid_b0 = (*env)->GetFieldID(env, lpCache->clazz, "guid_b0", "B");
	lpCache->guid_data3 = (*env)->GetFieldID(env, lpCache->clazz, "guid_data3", "S");
	lpCache->guid_data2 = (*env)->GetFieldID(env, lpCache->clazz, "guid_data2", "S");
	lpCache->guid_data1 = (*env)->GetFieldID(env, lpCache->clazz, "guid_data1", "I");
	lpCache->cached = 1;
}

TYPEATTR* getTYPEATTRFields(JNIEnv *env, jobject lpObject, TYPEATTR *lpStruct, PTYPEATTR_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheTYPEATTRFids(env, lpObject, lpCache);
	lpStruct->idldescType.wIDLFlags = (*env)->GetShortField(env, lpObject, lpCache->idldescType_wIDLFlags);
	lpStruct->idldescType.dwReserved = (*env)->GetIntField(env, lpObject, lpCache->idldescType_dwReserved);
	lpStruct->tdescAlias.vt = (*env)->GetShortField(env, lpObject, lpCache->tdescAlias_vt);
	lpStruct->tdescAlias.lptdesc = (struct FARSTRUCT tagTYPEDESC FAR *)(*env)->GetIntField(env, lpObject, lpCache->tdescAlias_unionField);
	lpStruct->wMinorVerNum = (*env)->GetShortField(env, lpObject, lpCache->wMinorVerNum);
	lpStruct->wMajorVerNum = (*env)->GetShortField(env, lpObject, lpCache->wMajorVerNum);
	lpStruct->wTypeFlags = (*env)->GetShortField(env, lpObject, lpCache->wTypeFlags);
	lpStruct->cbAlignment = (*env)->GetShortField(env, lpObject, lpCache->cbAlignment);
	lpStruct->cbSizeVft = (*env)->GetShortField(env, lpObject, lpCache->cbSizeVft);
	lpStruct->cImplTypes = (*env)->GetShortField(env, lpObject, lpCache->cImplTypes);
	lpStruct->cVars = (*env)->GetShortField(env, lpObject, lpCache->cVars);
	lpStruct->cFuncs = (*env)->GetShortField(env, lpObject, lpCache->cFuncs);
	lpStruct->typekind = (*env)->GetIntField(env, lpObject, lpCache->typekind);
	lpStruct->cbSizeInstance = (*env)->GetIntField(env, lpObject, lpCache->cbSizeInstance);
	lpStruct->lpstrSchema = (OLECHAR FAR *)(*env)->GetIntField(env, lpObject, lpCache->lpstrSchema);
	lpStruct->memidDestructor = (*env)->GetIntField(env, lpObject, lpCache->memidDestructor);
	lpStruct->memidConstructor = (*env)->GetIntField(env, lpObject, lpCache->memidConstructor);
	lpStruct->dwReserved = (*env)->GetIntField(env, lpObject, lpCache->dwReserved);
	lpStruct->lcid = (*env)->GetIntField(env, lpObject, lpCache->lcid);
	lpStruct->guid.Data4[7] = (*env)->GetByteField(env, lpObject, lpCache->guid_b7);
	lpStruct->guid.Data4[6] = (*env)->GetByteField(env, lpObject, lpCache->guid_b6);
	lpStruct->guid.Data4[5] = (*env)->GetByteField(env, lpObject, lpCache->guid_b5);
	lpStruct->guid.Data4[4] = (*env)->GetByteField(env, lpObject, lpCache->guid_b4);
	lpStruct->guid.Data4[3] = (*env)->GetByteField(env, lpObject, lpCache->guid_b3);
	lpStruct->guid.Data4[2] = (*env)->GetByteField(env, lpObject, lpCache->guid_b2);
	lpStruct->guid.Data4[1] = (*env)->GetByteField(env, lpObject, lpCache->guid_b1);
	lpStruct->guid.Data4[0] = (*env)->GetByteField(env, lpObject, lpCache->guid_b0);
	lpStruct->guid.Data3 = (*env)->GetShortField(env, lpObject, lpCache->guid_data3);
	lpStruct->guid.Data2 = (*env)->GetShortField(env, lpObject, lpCache->guid_data2);
	lpStruct->guid.Data1 = (*env)->GetIntField(env, lpObject, lpCache->guid_data1);
	return lpStruct;
}

void setTYPEATTRFields(JNIEnv *env, jobject lpObject, TYPEATTR *lpStruct, PTYPEATTR_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheTYPEATTRFids(env, lpObject, lpCache);
	(*env)->SetShortField(env, lpObject, lpCache->idldescType_wIDLFlags, lpStruct->idldescType.wIDLFlags);
	(*env)->SetIntField(env, lpObject, lpCache->idldescType_dwReserved, lpStruct->idldescType.dwReserved);
	(*env)->SetShortField(env, lpObject, lpCache->tdescAlias_vt, lpStruct->tdescAlias.vt);
	(*env)->SetIntField(env, lpObject, lpCache->tdescAlias_unionField, (jint)lpStruct->tdescAlias.lptdesc);
	(*env)->SetShortField(env, lpObject, lpCache->wMinorVerNum, lpStruct->wMinorVerNum);
	(*env)->SetShortField(env, lpObject, lpCache->wMajorVerNum, lpStruct->wMajorVerNum);
	(*env)->SetShortField(env, lpObject, lpCache->wTypeFlags, lpStruct->wTypeFlags);
	(*env)->SetShortField(env, lpObject, lpCache->cbAlignment, lpStruct->cbAlignment);
	(*env)->SetShortField(env, lpObject, lpCache->cbSizeVft, lpStruct->cbSizeVft);
	(*env)->SetShortField(env, lpObject, lpCache->cImplTypes, lpStruct->cImplTypes);
	(*env)->SetShortField(env, lpObject, lpCache->cVars, lpStruct->cVars);
	(*env)->SetShortField(env, lpObject, lpCache->cFuncs, lpStruct->cFuncs);
	(*env)->SetIntField(env, lpObject, lpCache->typekind, lpStruct->typekind);
	(*env)->SetIntField(env, lpObject, lpCache->cbSizeInstance, lpStruct->cbSizeInstance);
	(*env)->SetIntField(env, lpObject, lpCache->lpstrSchema, (jint)lpStruct->lpstrSchema);
	(*env)->SetIntField(env, lpObject, lpCache->memidDestructor, lpStruct->memidDestructor);
	(*env)->SetIntField(env, lpObject, lpCache->memidConstructor, lpStruct->memidConstructor);
	(*env)->SetIntField(env, lpObject, lpCache->dwReserved, lpStruct->dwReserved);
	(*env)->SetIntField(env, lpObject, lpCache->lcid, lpStruct->lcid);
	(*env)->SetByteField(env, lpObject, lpCache->guid_b7, lpStruct->guid.Data4[7]);
	(*env)->SetByteField(env, lpObject, lpCache->guid_b6, lpStruct->guid.Data4[6]);
	(*env)->SetByteField(env, lpObject, lpCache->guid_b5, lpStruct->guid.Data4[5]);
	(*env)->SetByteField(env, lpObject, lpCache->guid_b4, lpStruct->guid.Data4[4]);
	(*env)->SetByteField(env, lpObject, lpCache->guid_b3, lpStruct->guid.Data4[3]);
	(*env)->SetByteField(env, lpObject, lpCache->guid_b2, lpStruct->guid.Data4[2]);
	(*env)->SetByteField(env, lpObject, lpCache->guid_b1, lpStruct->guid.Data4[1]);
	(*env)->SetByteField(env, lpObject, lpCache->guid_b0, lpStruct->guid.Data4[0]);
	(*env)->SetShortField(env, lpObject, lpCache->guid_data3, lpStruct->guid.Data3);
	(*env)->SetShortField(env, lpObject, lpCache->guid_data2, lpStruct->guid.Data2);
	(*env)->SetIntField(env, lpObject, lpCache->guid_data1, lpStruct->guid.Data1);
}

void cacheVARDESC1Fids(JNIEnv *env, jobject lpObject, PVARDESC1_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->varkind = (*env)->GetFieldID(env, lpCache->clazz, "varkind", "I");
	lpCache->wVarFlags = (*env)->GetFieldID(env, lpCache->clazz, "wVarFlags", "S");
	lpCache->elemdescVar_paramdesc_wParamFlags = (*env)->GetFieldID(env, lpCache->clazz, "elemdescVar_paramdesc_wParamFlags", "S");
	lpCache->elemdescVar_paramdesc_pparamdescex = (*env)->GetFieldID(env, lpCache->clazz, "elemdescVar_paramdesc_pparamdescex", "I");
	lpCache->elemdescVar_tdesc_vt = (*env)->GetFieldID(env, lpCache->clazz, "elemdescVar_tdesc_vt", "S");
	lpCache->elemdescVar_tdesc_union = (*env)->GetFieldID(env, lpCache->clazz, "elemdescVar_tdesc_union", "I");
	lpCache->unionField = (*env)->GetFieldID(env, lpCache->clazz, "unionField", "I");
	lpCache->lpstrSchema = (*env)->GetFieldID(env, lpCache->clazz, "lpstrSchema", "I");
	lpCache->memid = (*env)->GetFieldID(env, lpCache->clazz, "memid", "I");
	lpCache->cached = 1;
}

VARDESC* getVARDESC1Fields(JNIEnv *env, jobject lpObject, VARDESC *lpStruct, PVARDESC1_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheVARDESC1Fids(env, lpObject, lpCache);
	lpStruct->varkind = (*env)->GetIntField(env, lpObject, lpCache->varkind);
	lpStruct->wVarFlags = (*env)->GetShortField(env, lpObject, lpCache->wVarFlags);
	lpStruct->elemdescVar.paramdesc.wParamFlags = (*env)->GetShortField(env, lpObject, lpCache->elemdescVar_paramdesc_wParamFlags);
	lpStruct->elemdescVar.paramdesc.pparamdescex = (LPPARAMDESCEX)(*env)->GetIntField(env, lpObject, lpCache->elemdescVar_paramdesc_pparamdescex);
	lpStruct->elemdescVar.tdesc.vt = (*env)->GetShortField(env, lpObject, lpCache->elemdescVar_tdesc_vt);
	lpStruct->elemdescVar.tdesc.lptdesc = (struct FARSTRUCT tagTYPEDESC FAR *)(*env)->GetIntField(env, lpObject, lpCache->elemdescVar_tdesc_union);
	lpStruct->oInst = (*env)->GetIntField(env, lpObject, lpCache->unionField);
	lpStruct->lpstrSchema = (OLECHAR FAR *)(*env)->GetIntField(env, lpObject, lpCache->lpstrSchema);
	lpStruct->memid = (*env)->GetIntField(env, lpObject, lpCache->memid);
	return lpStruct;
}

void setVARDESC1Fields(JNIEnv *env, jobject lpObject, VARDESC *lpStruct, PVARDESC1_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheVARDESC1Fids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->varkind, lpStruct->varkind);
	(*env)->SetShortField(env, lpObject, lpCache->wVarFlags, lpStruct->wVarFlags);
	(*env)->SetShortField(env, lpObject, lpCache->elemdescVar_paramdesc_wParamFlags, lpStruct->elemdescVar.paramdesc.wParamFlags);
	(*env)->SetIntField(env, lpObject, lpCache->elemdescVar_paramdesc_pparamdescex, (jint)lpStruct->elemdescVar.paramdesc.pparamdescex);
	(*env)->SetShortField(env, lpObject, lpCache->elemdescVar_tdesc_vt, lpStruct->elemdescVar.tdesc.vt);
	(*env)->SetIntField(env, lpObject, lpCache->elemdescVar_tdesc_union, (jint)lpStruct->elemdescVar.tdesc.lptdesc);
	(*env)->SetIntField(env, lpObject, lpCache->unionField, lpStruct->oInst);
	(*env)->SetIntField(env, lpObject, lpCache->lpstrSchema, (jint)lpStruct->lpstrSchema);
	(*env)->SetIntField(env, lpObject, lpCache->memid, lpStruct->memid);
}

void cacheVARDESC2Fids(JNIEnv *env, jobject lpObject, PVARDESC2_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->varkind = (*env)->GetFieldID(env, lpCache->clazz, "varkind", "I");
	lpCache->wVarFlags = (*env)->GetFieldID(env, lpCache->clazz, "wVarFlags", "S");
	lpCache->elemdescFunc_idldesc_wIDLFlags = (*env)->GetFieldID(env, lpCache->clazz, "elemdescFunc_idldesc_wIDLFlags", "S");
	lpCache->elemdescFunc_idldesc_dwReserved = (*env)->GetFieldID(env, lpCache->clazz, "elemdescFunc_idldesc_dwReserved", "I");
	lpCache->elemdescVar_tdesc_vt = (*env)->GetFieldID(env, lpCache->clazz, "elemdescVar_tdesc_vt", "S");
	lpCache->elemdescVar_tdesc_union = (*env)->GetFieldID(env, lpCache->clazz, "elemdescVar_tdesc_union", "I");
	lpCache->unionField = (*env)->GetFieldID(env, lpCache->clazz, "unionField", "I");
	lpCache->lpstrSchema = (*env)->GetFieldID(env, lpCache->clazz, "lpstrSchema", "I");
	lpCache->memid = (*env)->GetFieldID(env, lpCache->clazz, "memid", "I");
	lpCache->cached = 1;
}

VARDESC* getVARDESC2Fields(JNIEnv *env, jobject lpObject, VARDESC *lpStruct, PVARDESC2_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheVARDESC2Fids(env, lpObject, lpCache);
	lpStruct->varkind = (*env)->GetIntField(env, lpObject, lpCache->varkind);
	lpStruct->wVarFlags = (*env)->GetShortField(env, lpObject, lpCache->wVarFlags);
	lpStruct->elemdescVar.idldesc.wIDLFlags = (*env)->GetShortField(env, lpObject, lpCache->elemdescFunc_idldesc_wIDLFlags);
	lpStruct->elemdescVar.idldesc.dwReserved = (*env)->GetIntField(env, lpObject, lpCache->elemdescFunc_idldesc_dwReserved);
	lpStruct->elemdescVar.tdesc.vt = (*env)->GetShortField(env, lpObject, lpCache->elemdescVar_tdesc_vt);
	lpStruct->elemdescVar.tdesc.lptdesc = (struct FARSTRUCT tagTYPEDESC FAR *)(*env)->GetIntField(env, lpObject, lpCache->elemdescVar_tdesc_union);
	lpStruct->oInst = (*env)->GetIntField(env, lpObject, lpCache->unionField);
	lpStruct->lpstrSchema = (OLECHAR FAR *)(*env)->GetIntField(env, lpObject, lpCache->lpstrSchema);
	lpStruct->memid = (*env)->GetIntField(env, lpObject, lpCache->memid);
	return lpStruct;
}

void setVARDESC2Fields(JNIEnv *env, jobject lpObject, VARDESC *lpStruct, PVARDESC2_FID_CACHE lpCache)
{
	if (!lpCache->cached) cacheVARDESC2Fids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->varkind, lpStruct->varkind);
	(*env)->SetShortField(env, lpObject, lpCache->wVarFlags, lpStruct->wVarFlags);
	(*env)->SetShortField(env, lpObject, lpCache->elemdescFunc_idldesc_wIDLFlags, lpStruct->elemdescVar.idldesc.wIDLFlags);
	(*env)->SetIntField(env, lpObject, lpCache->elemdescFunc_idldesc_dwReserved, lpStruct->elemdescVar.idldesc.dwReserved);
	(*env)->SetShortField(env, lpObject, lpCache->elemdescVar_tdesc_vt, lpStruct->elemdescVar.tdesc.vt);
	(*env)->SetIntField(env, lpObject, lpCache->elemdescVar_tdesc_union, (jint)lpStruct->elemdescVar.tdesc.lptdesc);
	(*env)->SetIntField(env, lpObject, lpCache->unionField, lpStruct->oInst);
	(*env)->SetIntField(env, lpObject, lpCache->lpstrSchema, (jint)lpStruct->lpstrSchema);
	(*env)->SetIntField(env, lpObject, lpCache->memid, lpStruct->memid);
}

/**************************** END OLE ****************************/

