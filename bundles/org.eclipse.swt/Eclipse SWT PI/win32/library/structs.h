/*
 * (c) Copyright IBM Corp., 2000, 2001
 * All Rights Reserved.
 */

/**
 * JNI SWT object field getters and setters declarations for Windows structs.
 */

#ifndef INC_structs_H
#define INC_structs_H

#define WINVER 0x0500
#define _WIN32_IE 0x0500

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#define VC_EXTRALEAN

#include <windows.h>
#include <winuser.h>
#include <commctrl.h>
#include <commdlg.h>
#include <oaidl.h>
#include <shlobj.h>
#include <ole2.h>
#include <olectl.h>
#include <objbase.h>
#include <shlwapi.h>
#include <shellapi.h>

#ifdef _WIN32_WCE
#include <aygshell.h>
#endif // _WIN_32_WCE

/** Structs */

/* ACCEL struct */
typedef struct ACCEL_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID fVirt, key, cmd;
} ACCEL_FID_CACHE;
typedef ACCEL_FID_CACHE *PACCEL_FID_CACHE;

void cacheACCELFids(JNIEnv *env, jobject lpObject, PACCEL_FID_CACHE lpCache);
ACCEL* getACCELFields(JNIEnv *env, jobject lpObject, ACCEL *lpStruct, PACCEL_FID_CACHE lpCache);
void setACCELFields(JNIEnv *env, jobject lpObject, ACCEL *lpStruct, PACCEL_FID_CACHE lpCache);

/* BITMAP struct */
typedef struct BITMAP_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID bmType, bmWidth, bmHeight, bmWidthBytes, bmPlanes, bmBitsPixel, bmBits;
} BITMAP_FID_CACHE;
typedef BITMAP_FID_CACHE *PBITMAP_FID_CACHE;

void cacheBITMAPFids(JNIEnv *env, jobject lpObject, PBITMAP_FID_CACHE lpCache);
BITMAP* getBITMAPFields(JNIEnv *env, jobject lpObject, BITMAP *lpStruct, PBITMAP_FID_CACHE lpCache);
void setBITMAPFields(JNIEnv *env, jobject lpObject, BITMAP *lpStruct, PBITMAP_FID_CACHE lpCache);

#ifndef _WIN32_WCE

/* BROWSEINFO struct */
typedef struct BROWSEINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID hwndOwner, pidlRoot, pszDisplayName, lpszTitle, ulFlags, lpfn, lParam, iImage;
} BROWSEINFO_FID_CACHE;
typedef BROWSEINFO_FID_CACHE *PBROWSEINFO_FID_CACHE;

void cacheBROWSEINFOFids(JNIEnv *env, jobject lpObject, PBROWSEINFO_FID_CACHE lpCache);
BROWSEINFO* getBROWSEINFOFields(JNIEnv *env, jobject lpObject, BROWSEINFO *lpStruct, PBROWSEINFO_FID_CACHE lpCache);
void setBROWSEINFOFields(JNIEnv *env, jobject lpObject, BROWSEINFO *lpStruct, PBROWSEINFO_FID_CACHE lpCache);

#endif // _WIN32_WCE

/* CHOOSECOLOR struct */
typedef struct CHOOSECOLOR_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lStructSize, hwndOwner, hInstance, rgbResult, lpCustColors, Flags, lCustData, lpfnHook, lpTemplateName;
} CHOOSECOLOR_FID_CACHE;
typedef CHOOSECOLOR_FID_CACHE *PCHOOSECOLOR_FID_CACHE;

void cacheCHOOSECOLORFids(JNIEnv *env, jobject lpObject, PCHOOSECOLOR_FID_CACHE lpCache);
CHOOSECOLOR* getCHOOSECOLORFields(JNIEnv *env, jobject lpObject, CHOOSECOLOR *lpStruct, PCHOOSECOLOR_FID_CACHE lpCache);
void setCHOOSECOLORFields(JNIEnv *env, jobject lpObject, CHOOSECOLOR *lpStruct, PCHOOSECOLOR_FID_CACHE lpCache);

#ifndef _WIN32_WCE

/* CHOOSEFONT struct */
typedef struct CHOOSEFONT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lStructSize, hwndOwner, hDC, lpLogFont, iPointSize, Flags, rgbColors, lCustData, lpfnHook, lpTemplateName, hInstance, lpszStyle, nFontType, nSizeMin, nSizeMax;
} CHOOSEFONT_FID_CACHE;
typedef CHOOSEFONT_FID_CACHE *PCHOOSEFONT_FID_CACHE;

void cacheCHOOSEFONTFids(JNIEnv *env, jobject lpObject, PCHOOSEFONT_FID_CACHE lpCache);
CHOOSEFONT* getCHOOSEFONTFields(JNIEnv *env, jobject lpObject, CHOOSEFONT *lpStruct, PCHOOSEFONT_FID_CACHE lpCache);
void setCHOOSEFONTFields(JNIEnv *env, jobject lpObject, CHOOSEFONT *lpStruct, PCHOOSEFONT_FID_CACHE lpCache);

#endif // _WIN32_WCE

/* COMPOSITIONFORM struct */
typedef struct COMPOSITIONFORM_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID dwStyle, x, y, left, top, right, bottom;
} COMPOSITIONFORM_FID_CACHE;
typedef COMPOSITIONFORM_FID_CACHE *PCOMPOSITIONFORM_FID_CACHE;

void cacheCOMPOSITIONFORMFids(JNIEnv *env, jobject lpObject, PCOMPOSITIONFORM_FID_CACHE lpCache);
COMPOSITIONFORM* getCOMPOSITIONFORMFields(JNIEnv *env, jobject lpObject, COMPOSITIONFORM *lpStruct, PCOMPOSITIONFORM_FID_CACHE lpCache);
void setCOMPOSITIONFORMFields(JNIEnv *env, jobject lpObject, COMPOSITIONFORM *lpStruct, PCOMPOSITIONFORM_FID_CACHE lpCache);

/* CREATESTRUCT struct */
typedef struct CREATESTRUCT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lpCreateParams, hInstance, hMenu, hwndParent, cy, cx, y, x, style, lpszName, lpszClass, dwExStyle;
} CREATESTRUCT_FID_CACHE;
typedef CREATESTRUCT_FID_CACHE *PCREATESTRUCT_FID_CACHE;

void cacheCREATESTRUCTFids(JNIEnv *env, jobject lpObject, PCREATESTRUCT_FID_CACHE lpCache);
CREATESTRUCT* getCREATESTRUCTFields(JNIEnv *env, jobject lpObject, CREATESTRUCT *lpStruct, PCREATESTRUCT_FID_CACHE lpCache);
void setCREATESTRUCTFields(JNIEnv *env, jobject lpObject, CREATESTRUCT *lpStruct, PCREATESTRUCT_FID_CACHE lpCache);

/* DIBSECTION struct */
typedef struct DIBSECTION_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID bmType, bmWidth, bmHeight, bmWidthBytes, bmPlanes, bmBitsPixel, bmBits, biSize, biWidth, biHeight, biPlanes, biBitCount, biCompression, biSizeImage, biXPelsPerMeter, biYPelsPerMeter, biClrUsed, biClrImportant, dsBitfields0, dsBitfields1, dsBitfields2, dshSection, dsOffset;
} DIBSECTION_FID_CACHE;
typedef DIBSECTION_FID_CACHE *PDIBSECTION_FID_CACHE;

void cacheDIBSECTIONFids(JNIEnv *env, jobject lpObject, PDIBSECTION_FID_CACHE lpCache);
DIBSECTION* getDIBSECTIONFields(JNIEnv *env, jobject lpObject, DIBSECTION *lpStruct, PDIBSECTION_FID_CACHE lpCache);
void setDIBSECTIONFields(JNIEnv *env, jobject lpObject, DIBSECTION *lpStruct, PDIBSECTION_FID_CACHE lpCache);

/* DLLVERSIONINFO struct */
typedef struct DLLVERSIONINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, dwMajorVersion, dwMinorVersion, dwBuildNumber, dwPlatformID;
} DLLVERSIONINFO_FID_CACHE;
typedef DLLVERSIONINFO_FID_CACHE *PDLLVERSIONINFO_FID_CACHE;

void cacheDLLVERSIONINFOFids(JNIEnv *env, jobject lpObject, PDLLVERSIONINFO_FID_CACHE lpCache);
DLLVERSIONINFO* getDLLVERSIONINFOFields(JNIEnv *env, jobject lpObject, DLLVERSIONINFO *lpStruct, PDLLVERSIONINFO_FID_CACHE lpCache);
void setDLLVERSIONINFOFields(JNIEnv *env, jobject lpObject, DLLVERSIONINFO *lpStruct, PDLLVERSIONINFO_FID_CACHE lpCache);

#ifndef _WIN32_WCE

/* DOCINFO struct */
typedef struct DOCINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, lpszDocName, lpszOutput, lpszDatatype, fwType;
} DOCINFO_FID_CACHE;
typedef DOCINFO_FID_CACHE *PDOCINFO_FID_CACHE;

void cacheDOCINFOFids(JNIEnv *env, jobject lpObject, PDOCINFO_FID_CACHE lpCache);
DOCINFO* getDOCINFOFields(JNIEnv *env, jobject lpObject, DOCINFO *lpStruct, PDOCINFO_FID_CACHE lpCache);
void setDOCINFOFields(JNIEnv *env, jobject lpObject, DOCINFO *lpStruct, PDOCINFO_FID_CACHE lpCache);

#endif // _WIN32_WCE

/* DRAWITEMSTRUCT struct */
typedef struct DRAWITEMSTRUCT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID CtlType, CtlID, itemID, itemAction, itemState, hwndItem, hDC, left, top, bottom, right, itemData;
} DRAWITEMSTRUCT_FID_CACHE;
typedef DRAWITEMSTRUCT_FID_CACHE *PDRAWITEMSTRUCT_FID_CACHE;

void cacheDRAWITEMSTRUCTFids(JNIEnv *env, jobject lpObject, PDRAWITEMSTRUCT_FID_CACHE lpCache);
DRAWITEMSTRUCT* getDRAWITEMSTRUCTFields(JNIEnv *env, jobject lpObject, DRAWITEMSTRUCT *lpStruct, PDRAWITEMSTRUCT_FID_CACHE lpCache);
void setDRAWITEMSTRUCTFields(JNIEnv *env, jobject lpObject, DRAWITEMSTRUCT *lpStruct, PDRAWITEMSTRUCT_FID_CACHE lpCache);

/* FILETIME struct */
typedef struct FILETIME_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID dwLowDateTime, dwHighDateTime;
} FILETIME_FID_CACHE;
typedef FILETIME_FID_CACHE *PFILETIME_FID_CACHE;

void cacheFILETIMEFids(JNIEnv *env, jobject lpObject, PFILETIME_FID_CACHE lpCache);
FILETIME* getFILETIMEFields(JNIEnv *env, jobject lpObject, FILETIME *lpStruct, PFILETIME_FID_CACHE lpCache);
void setFILETIMEFields(JNIEnv *env, jobject lpObject, FILETIME *lpStruct, PFILETIME_FID_CACHE lpCache);

#ifndef _WIN32_WCE

/* GCP_RESULTS struct */
typedef struct GCP_RESULTS_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lStructSize, lpOutString, lpOrder, lpDx, lpCaretPos, lpClass, lpGlyphs, nGlyphs, nMaxFit;
} GCP_RESULTS_FID_CACHE;
typedef GCP_RESULTS_FID_CACHE *PGCP_RESULTS_FID_CACHE;

void cacheGCP_RESULTSFids(JNIEnv *env, jobject lpObject, PGCP_RESULTS_FID_CACHE lpCache);
GCP_RESULTS* getGCP_RESULTSFields(JNIEnv *env, jobject lpObject, GCP_RESULTS *lpStruct, PGCP_RESULTS_FID_CACHE lpCache);
void setGCP_RESULTSFields(JNIEnv *env, jobject lpObject, GCP_RESULTS *lpStruct, PGCP_RESULTS_FID_CACHE lpCache);

#endif // _WIN32_WCE

#ifndef _WIN32_WCE

/* GRADIENT_RECT struct */
typedef struct GRADIENT_RECT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID UpperLeft, LowerRight;
} GRADIENT_RECT_FID_CACHE;
typedef GRADIENT_RECT_FID_CACHE *PGRADIENT_RECT_FID_CACHE;

void cacheGRADIENT_RECTFids(JNIEnv *env, jobject lpObject, PGRADIENT_RECT_FID_CACHE lpCache);
GRADIENT_RECT* getGRADIENT_RECTFields(JNIEnv *env, jobject lpObject, GRADIENT_RECT *lpStruct, PGRADIENT_RECT_FID_CACHE lpCache);
void setGRADIENT_RECTFields(JNIEnv *env, jobject lpObject, GRADIENT_RECT *lpStruct, PGRADIENT_RECT_FID_CACHE lpCache);

#endif // _WIN32_WCE

/* HDITEM struct */
typedef struct HDITEM_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID mask, cxy, pszText, hbm, cchTextMax, fmt, lParam, iImage, iOrder;
} HDITEM_FID_CACHE;
typedef HDITEM_FID_CACHE *PHDITEM_FID_CACHE;

void cacheHDITEMFids(JNIEnv *env, jobject lpObject, PHDITEM_FID_CACHE lpCache);
HDITEM* getHDITEMFields(JNIEnv *env, jobject lpObject, HDITEM *lpStruct, PHDITEM_FID_CACHE lpCache);
void setHDITEMFields(JNIEnv *env, jobject lpObject, HDITEM *lpStruct, PHDITEM_FID_CACHE lpCache);

#ifndef _WIN32_WCE

/* HELPINFO struct */
typedef struct HELPINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, iContextType, iCtrlId, hItemHandle, dwContextId, x, y;
} HELPINFO_FID_CACHE;
typedef HELPINFO_FID_CACHE *PHELPINFO_FID_CACHE;

void cacheHELPINFOFids(JNIEnv *env, jobject lpObject, PHELPINFO_FID_CACHE lpCache);
HELPINFO* getHELPINFOFields(JNIEnv *env, jobject lpObject, HELPINFO *lpStruct, PHELPINFO_FID_CACHE lpCache);
void setHELPINFOFields(JNIEnv *env, jobject lpObject, HELPINFO *lpStruct, PHELPINFO_FID_CACHE lpCache);

#endif // _WIN32_WCE

/* ICONINFO struct */
typedef struct ICONINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID fIcon, xHotspot, yHotspot, hbmMask, hbmColor;
} ICONINFO_FID_CACHE;
typedef ICONINFO_FID_CACHE *PICONINFO_FID_CACHE;

void cacheICONINFOFids(JNIEnv *env, jobject lpObject, PICONINFO_FID_CACHE lpCache);
ICONINFO* getICONINFOFields(JNIEnv *env, jobject lpObject, ICONINFO *lpStruct, PICONINFO_FID_CACHE lpCache);
void setICONINFOFields(JNIEnv *env, jobject lpObject, ICONINFO *lpStruct, PICONINFO_FID_CACHE lpCache);

/* INITCOMMONCONTROLSEX struct */
typedef struct INITCOMMONCONTROLSEX_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID dwSize, dwICC;
} INITCOMMONCONTROLSEX_FID_CACHE;
typedef INITCOMMONCONTROLSEX_FID_CACHE *PINITCOMMONCONTROLSEX_FID_CACHE;

void cacheINITCOMMONCONTROLSEXFids(JNIEnv *env, jobject lpObject, PINITCOMMONCONTROLSEX_FID_CACHE lpCache);
INITCOMMONCONTROLSEX* getINITCOMMONCONTROLSEXFields(JNIEnv *env, jobject lpObject, INITCOMMONCONTROLSEX *lpStruct, PINITCOMMONCONTROLSEX_FID_CACHE lpCache);
void setINITCOMMONCONTROLSEXFields(JNIEnv *env, jobject lpObject, INITCOMMONCONTROLSEX *lpStruct, PINITCOMMONCONTROLSEX_FID_CACHE lpCache);

/* LOGBRUSH struct */
typedef struct LOGBRUSH_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lbStyle, lbColor, lbHatch;
} LOGBRUSH_FID_CACHE;
typedef LOGBRUSH_FID_CACHE *PLOGBRUSH_FID_CACHE;

void cacheLOGBRUSHFids(JNIEnv *env, jobject lpObject, PLOGBRUSH_FID_CACHE lpCache);
LOGBRUSH* getLOGBRUSHFields(JNIEnv *env, jobject lpObject, LOGBRUSH *lpStruct, PLOGBRUSH_FID_CACHE lpCache);
void setLOGBRUSHFields(JNIEnv *env, jobject lpObject, LOGBRUSH *lpStruct, PLOGBRUSH_FID_CACHE lpCache);

/* LOGFONT struct */
typedef struct LOGFONT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lfHeight, lfWidth, lfEscapement, lfOrientation, lfWeight, lfItalic, lfUnderline, lfStrikeOut, lfCharSet, lfOutPrecision, lfClipPrecision, lfQuality, lfPitchAndFamily, lfFaceName0, lfFaceName1, lfFaceName2, lfFaceName3, lfFaceName4, lfFaceName5, lfFaceName6, lfFaceName7, lfFaceName8, lfFaceName9, lfFaceName10, lfFaceName11, lfFaceName12, lfFaceName13, lfFaceName14, lfFaceName15, lfFaceName16, lfFaceName17, lfFaceName18, lfFaceName19, lfFaceName20, lfFaceName21, lfFaceName22, lfFaceName23, lfFaceName24, lfFaceName25, lfFaceName26, lfFaceName27, lfFaceName28, lfFaceName29, lfFaceName30, lfFaceName31;
} LOGFONT_FID_CACHE;
typedef LOGFONT_FID_CACHE *PLOGFONT_FID_CACHE;

void cacheLOGFONTFids(JNIEnv *env, jobject lpObject, PLOGFONT_FID_CACHE lpCache);
#ifndef _WIN32_WCE
LOGFONTA* getLOGFONTAFields(JNIEnv *env, jobject lpObject, LOGFONTA *lpStruct, PLOGFONT_FID_CACHE lpCache);
void setLOGFONTAFields(JNIEnv *env, jobject lpObject, LOGFONTA *lpStruct, PLOGFONT_FID_CACHE lpCache);
#endif // _WIN32_WCE
LOGFONTW* getLOGFONTWFields(JNIEnv *env, jobject lpObject, LOGFONTW *lpStruct, PLOGFONT_FID_CACHE lpCache);
void setLOGFONTWFields(JNIEnv *env, jobject lpObject, LOGFONTW *lpStruct, PLOGFONT_FID_CACHE lpCache);

/* LOGPEN struct */
typedef struct LOGPEN_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lopnStyle, x, y, lopnColor;
} LOGPEN_FID_CACHE;
typedef LOGPEN_FID_CACHE *PLOGPEN_FID_CACHE;

void cacheLOGPENFids(JNIEnv *env, jobject lpObject, PLOGPEN_FID_CACHE lpCache);
LOGPEN* getLOGPENFields(JNIEnv *env, jobject lpObject, LOGPEN *lpStruct, PLOGPEN_FID_CACHE lpCache);
void setLOGPENFields(JNIEnv *env, jobject lpObject, LOGPEN *lpStruct, PLOGPEN_FID_CACHE lpCache);

/* LVCOLUMN struct */
typedef struct LVCOLUMN_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID mask, fmt, cx, pszText, cchTextMax, iSubItem, iImage, iOrder;
} LVCOLUMN_FID_CACHE;
typedef LVCOLUMN_FID_CACHE *PLVCOLUMN_FID_CACHE;

void cacheLVCOLUMNFids(JNIEnv *env, jobject lpObject, PLVCOLUMN_FID_CACHE lpCache);
LVCOLUMN* getLVCOLUMNFields(JNIEnv *env, jobject lpObject, LVCOLUMN *lpStruct, PLVCOLUMN_FID_CACHE lpCache);
void setLVCOLUMNFields(JNIEnv *env, jobject lpObject, LVCOLUMN *lpStruct, PLVCOLUMN_FID_CACHE lpCache);

/* LVHITTESTINFO struct */
typedef struct LVHITTESTINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID x, y, flags, iItem, iSubItem;
} LVHITTESTINFO_FID_CACHE;
typedef LVHITTESTINFO_FID_CACHE *PLVHITTESTINFO_FID_CACHE;

void cacheLVHITTESTINFOFids(JNIEnv *env, jobject lpObject, PLVHITTESTINFO_FID_CACHE lpCache);
LVHITTESTINFO* getLVHITTESTINFOFields(JNIEnv *env, jobject lpObject, LVHITTESTINFO *lpStruct, PLVHITTESTINFO_FID_CACHE lpCache);
void setLVHITTESTINFOFields(JNIEnv *env, jobject lpObject, LVHITTESTINFO *lpStruct, PLVHITTESTINFO_FID_CACHE lpCache);

/* LVITEM struct */
typedef struct LVITEM_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID mask, iItem, iSubItem, state, stateMask, pszText, cchTextMax, iImage, lParam, iIndent;
} LVITEM_FID_CACHE;
typedef LVITEM_FID_CACHE *PLVITEM_FID_CACHE;

void cacheLVITEMFids(JNIEnv *env, jobject lpObject, PLVITEM_FID_CACHE lpCache);
LVITEM* getLVITEMFields(JNIEnv *env, jobject lpObject, LVITEM *lpStruct, PLVITEM_FID_CACHE lpCache);
void setLVITEMFields(JNIEnv *env, jobject lpObject, LVITEM *lpStruct, PLVITEM_FID_CACHE lpCache);

/* MEASUREITEMSTRUCT struct */
typedef struct MEASUREITEMSTRUCT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID CtlType, CtlID, itemID, itemWidth, itemHeight, itemData;
} MEASUREITEMSTRUCT_FID_CACHE;
typedef MEASUREITEMSTRUCT_FID_CACHE *PMEASUREITEMSTRUCT_FID_CACHE;

void cacheMEASUREITEMSTRUCTFids(JNIEnv *env, jobject lpObject, PMEASUREITEMSTRUCT_FID_CACHE lpCache);
MEASUREITEMSTRUCT* getMEASUREITEMSTRUCTFields(JNIEnv *env, jobject lpObject, MEASUREITEMSTRUCT *lpStruct, PMEASUREITEMSTRUCT_FID_CACHE lpCache);
void setMEASUREITEMSTRUCTFields(JNIEnv *env, jobject lpObject, MEASUREITEMSTRUCT *lpStruct, PMEASUREITEMSTRUCT_FID_CACHE lpCache);

#ifndef _WIN32_WCE

/* MENUINFO struct */
typedef struct MENUINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, fMask, dwStyle, cyMax, hbrBack, dwContextHelpID, dwMenuData;
} MENUINFO_FID_CACHE;
typedef MENUINFO_FID_CACHE *PMENUINFO_FID_CACHE;

void cacheMENUINFOFids(JNIEnv *env, jobject lpObject, PMENUINFO_FID_CACHE lpCache);
MENUINFO* getMENUINFOFields(JNIEnv *env, jobject lpObject, MENUINFO *lpStruct, PMENUINFO_FID_CACHE lpCache);
void setMENUINFOFields(JNIEnv *env, jobject lpObject, MENUINFO *lpStruct, PMENUINFO_FID_CACHE lpCache);

#endif // _WIN32_WCE

/* MENUITEMINFO struct */
typedef struct MENUITEMINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, fMask, fType, fState, wID, hSubMenu, hbmpChecked, hbmpUnchecked, dwItemData, dwTypeData, cch, hbmpItem;
} MENUITEMINFO_FID_CACHE;
typedef MENUITEMINFO_FID_CACHE *PMENUITEMINFO_FID_CACHE;

void cacheMENUITEMINFOFids(JNIEnv *env, jobject lpObject, PMENUITEMINFO_FID_CACHE lpCache);
MENUITEMINFO* getMENUITEMINFOFields(JNIEnv *env, jobject lpObject, MENUITEMINFO *lpStruct, PMENUITEMINFO_FID_CACHE lpCache);
void setMENUITEMINFOFields(JNIEnv *env, jobject lpObject, MENUITEMINFO *lpStruct, PMENUITEMINFO_FID_CACHE lpCache);

/* MSG struct */
typedef struct MSG_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID hwnd, message, wParam, lParam, time, x, y;
} MSG_FID_CACHE;
typedef MSG_FID_CACHE *PMSG_FID_CACHE;

void cacheMSGFids(JNIEnv *env, jobject lpObject, PMSG_FID_CACHE lpCache);
MSG* getMSGFields(JNIEnv *env, jobject lpObject, MSG *lpStruct, PMSG_FID_CACHE lpCache);
void setMSGFields(JNIEnv *env, jobject lpObject, MSG *lpStruct, PMSG_FID_CACHE lpCache);

/* NMHDR struct */
typedef struct NMHDR_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID hwndFrom, idFrom, code;
} NMHDR_FID_CACHE;
typedef NMHDR_FID_CACHE *PNMHDR_FID_CACHE;

void cacheNMHDRFids(JNIEnv *env, jobject lpObject, PNMHDR_FID_CACHE lpCache);
NMHDR* getNMHDRFields(JNIEnv *env, jobject lpObject, NMHDR *lpStruct, PNMHDR_FID_CACHE lpCache);
void setNMHDRFields(JNIEnv *env, jobject lpObject, NMHDR *lpStruct, PNMHDR_FID_CACHE lpCache);

/* NMHEADER struct */
typedef struct NMHEADER_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID hwndFrom, idFrom, code, iItem, iButton, pitem;
} NMHEADER_FID_CACHE;
typedef NMHEADER_FID_CACHE *PNMHEADER_FID_CACHE;

void cacheNMHEADERFids(JNIEnv *env, jobject lpObject, PNMHEADER_FID_CACHE lpCache);
NMHEADER* getNMHEADERFields(JNIEnv *env, jobject lpObject, NMHEADER *lpStruct, PNMHEADER_FID_CACHE lpCache);
void setNMHEADERFields(JNIEnv *env, jobject lpObject, NMHEADER *lpStruct, PNMHEADER_FID_CACHE lpCache);

/* NMLISTVIEW struct */
typedef struct NMLISTVIEW_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID hwndFrom, idFrom, code, iItem, iSubItem, uNewState, uOldState, uChanged, x, y, lParam;
} NMLISTVIEW_FID_CACHE;
typedef NMLISTVIEW_FID_CACHE *PNMLISTVIEW_FID_CACHE;

void cacheNMLISTVIEWFids(JNIEnv *env, jobject lpObject, PNMLISTVIEW_FID_CACHE lpCache);
NMLISTVIEW* getNMLISTVIEWFields(JNIEnv *env, jobject lpObject, NMLISTVIEW *lpStruct, PNMLISTVIEW_FID_CACHE lpCache);
void setNMLISTVIEWFields(JNIEnv *env, jobject lpObject, NMLISTVIEW *lpStruct, PNMLISTVIEW_FID_CACHE lpCache);

/* NMTOOLBAR struct */
typedef struct NMTOOLBAR_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID hwndFrom, idFrom, code, iItem, iBitmap, idCommand, fsState, fsStyle, dwData, iString, cchText, pszText, left, top, right, bottom;
} NMTOOLBAR_FID_CACHE;
typedef NMTOOLBAR_FID_CACHE *PNMTOOLBAR_FID_CACHE;

void cacheNMTOOLBARFids(JNIEnv *env, jobject lpObject, PNMTOOLBAR_FID_CACHE lpCache);
NMTOOLBAR* getNMTOOLBARFields(JNIEnv *env, jobject lpObject, NMTOOLBAR *lpStruct, PNMTOOLBAR_FID_CACHE lpCache);
void setNMTOOLBARFields(JNIEnv *env, jobject lpObject, NMTOOLBAR *lpStruct, PNMTOOLBAR_FID_CACHE lpCache);

#ifndef _WIN32_WCE

/* NMTTDISPINFO struct */
typedef struct NMTTDISPINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID hwndFrom, idFrom, code, lpszText, hinst, uFlags, lParam;
} NMTTDISPINFO_FID_CACHE;
typedef NMTTDISPINFO_FID_CACHE *PNMTTDISPINFO_FID_CACHE;

void cacheNMTTDISPINFOFids(JNIEnv *env, jobject lpObject, PNMTTDISPINFO_FID_CACHE lpCache);
NMTTDISPINFOW* getNMTTDISPINFOWFields(JNIEnv *env, jobject lpObject, NMTTDISPINFOW *lpStruct, PNMTTDISPINFO_FID_CACHE lpCache);
void setNMTTDISPINFOWFields(JNIEnv *env, jobject lpObject, NMTTDISPINFOW *lpStruct, PNMTTDISPINFO_FID_CACHE lpCache);
NMTTDISPINFOA* getNMTTDISPINFOAFields(JNIEnv *env, jobject lpObject, NMTTDISPINFOA *lpStruct, PNMTTDISPINFO_FID_CACHE lpCache);
void setNMTTDISPINFOAFields(JNIEnv *env, jobject lpObject, NMTTDISPINFOA *lpStruct, PNMTTDISPINFO_FID_CACHE lpCache);

#endif // _WIN32_WCE

/* NONCLIENTMETRICS struct */
typedef struct NONCLIENTMETRICS_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, iBorderWidth, iScrollWidth, iScrollHeight, iCaptionWidth, iCaptionHeight, iSmCaptionWidth, iSmCaptionHeight, iMenuWidth, iMenuHeight;
	jfieldID lfCaptionFont, lfSmCaptionFont, lfMenuFont, lfStatusFont, lfMessageFont;
} NONCLIENTMETRICS_FID_CACHE;
typedef NONCLIENTMETRICS_FID_CACHE *PNONCLIENTMETRICS_FID_CACHE;

void cacheNONCLIENTMETRICSFids(JNIEnv *env, jobject lpObject, PNONCLIENTMETRICS_FID_CACHE lpCache);
#ifndef _WIN32_WCE
NONCLIENTMETRICSA* getNONCLIENTMETRICSAFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICSA *lpStruct, PNONCLIENTMETRICS_FID_CACHE lpCache);
void setNONCLIENTMETRICSAFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICSA *lpStruct, PNONCLIENTMETRICS_FID_CACHE lpCache);
#endif  // _WIN32_WCE
NONCLIENTMETRICSW* getNONCLIENTMETRICSWFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICSW *lpStruct, PNONCLIENTMETRICS_FID_CACHE lpCache);
void setNONCLIENTMETRICSWFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICSW *lpStruct, PNONCLIENTMETRICS_FID_CACHE lpCache);

/* OPENFILENAME struct */
typedef struct OPENFILENAME_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lStructSize, hwndOwner, hInstance, lpstrFilter, lpstrCustomFilter, nMaxCustFilter, nFilterIndex, lpstrFile, nMaxFile, lpstrFileTitle, nMaxFileTitle, lpstrInitialDir, lpstrTitle, Flags, nFileOffset, nFileExtension, lpstrDefExt, lCustData, lpfnHook, lpTemplateName;
} OPENFILENAME_FID_CACHE;
typedef OPENFILENAME_FID_CACHE *POPENFILENAME_FID_CACHE;

void cacheOPENFILENAMEFids(JNIEnv *env, jobject lpObject, POPENFILENAME_FID_CACHE lpCache);
OPENFILENAME* getOPENFILENAMEFields(JNIEnv *env, jobject lpObject, OPENFILENAME *lpStruct, POPENFILENAME_FID_CACHE lpCache);
void setOPENFILENAMEFields(JNIEnv *env, jobject lpObject, OPENFILENAME *lpStruct, POPENFILENAME_FID_CACHE lpCache);

/* OSVERSIONINFO struct */
typedef struct OSVERSIONINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID dwOSVersionInfoSize, dwMajorVersion, dwMinorVersion, dwBuildNumber, dwPlatformId;
} OSVERSIONINFO_FID_CACHE;
typedef OSVERSIONINFO_FID_CACHE *POSVERSIONINFO_FID_CACHE;

void cacheOSVERSIONINFOFids(JNIEnv *env, jobject lpObject, POSVERSIONINFO_FID_CACHE lpCache);
OSVERSIONINFOA* getOSVERSIONINFOAFields(JNIEnv *env, jobject lpObject, OSVERSIONINFOA *lpStruct, POSVERSIONINFO_FID_CACHE lpCache);
void setOSVERSIONINFOAFields(JNIEnv *env, jobject lpObject, OSVERSIONINFOA *lpStruct, POSVERSIONINFO_FID_CACHE lpCache);
OSVERSIONINFOW* getOSVERSIONINFOWFields(JNIEnv *env, jobject lpObject, OSVERSIONINFOW *lpStruct, POSVERSIONINFO_FID_CACHE lpCache);
void setOSVERSIONINFOWFields(JNIEnv *env, jobject lpObject, OSVERSIONINFOW *lpStruct, POSVERSIONINFO_FID_CACHE lpCache);

/* PAINTSTRUCT struct */
typedef struct PAINTSTRUCT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID hdc, fErase, left, top, right, bottom, fRestore, fIncUpdate/*, pad0, pad1, pad2, pad3, pad4, pad5, pad6, pad7*/;
} PAINTSTRUCT_FID_CACHE;
typedef PAINTSTRUCT_FID_CACHE *PPAINTSTRUCT_FID_CACHE;

void cachePAINTSTRUCTFids(JNIEnv *env, jobject lpObject, PPAINTSTRUCT_FID_CACHE lpCache);
PAINTSTRUCT* getPAINTSTRUCTFields(JNIEnv *env, jobject lpObject, PAINTSTRUCT *lpStruct, PPAINTSTRUCT_FID_CACHE lpCache);
void setPAINTSTRUCTFields(JNIEnv *env, jobject lpObject, PAINTSTRUCT *lpStruct, PPAINTSTRUCT_FID_CACHE lpCache);

/* POINT struct */
typedef struct POINT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID x, y;
} POINT_FID_CACHE;
typedef POINT_FID_CACHE *PPOINT_FID_CACHE;

void cachePOINTFids(JNIEnv *env, jobject lpObject, PPOINT_FID_CACHE lpCache);
POINT* getPOINTFields(JNIEnv *env, jobject lpObject, POINT *lpStruct, PPOINT_FID_CACHE lpCache);
void setPOINTFields(JNIEnv *env, jobject lpObject, POINT *lpStruct, PPOINT_FID_CACHE lpCache);

#ifndef _WIN32_WCE

/* PRINTDLG struct */
typedef struct PRINTDLG_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lStructSize, hwndOwner, hDevMode, hDevNames, hDC, Flags, nFromPage, nToPage, nMinPage, nMaxPage, nCopies, hInstance, lCustData, lpfnPrintHook, lpfnSetupHook, lpPrintTemplateName, lpSetupTemplateName, hPrintTemplate, hSetupTemplate;
} PRINTDLG_FID_CACHE;
typedef PRINTDLG_FID_CACHE *PPRINTDLG_FID_CACHE;

void cachePRINTDLGFids(JNIEnv *env, jobject lpObject, PPRINTDLG_FID_CACHE lpCache);
PRINTDLG* getPRINTDLGFields(JNIEnv *env, jobject lpObject, PRINTDLG *lpStruct, PPRINTDLG_FID_CACHE lpCache);
void setPRINTDLGFields(JNIEnv *env, jobject lpObject, PRINTDLG *lpStruct, PPRINTDLG_FID_CACHE lpCache);

#endif _WIN32_WCE

/* REBARBANDINFO struct */
typedef struct REBARBANDINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, fMask, fStyle, clrFore, clrBack, lpText, cch, iImage, hwndChild, cxMinChild, cyMinChild, cx, hbmBack, wID, cyChild, cyMaxChild, cyIntegral, cxIdeal, lParam, cxHeader;
} REBARBANDINFO_FID_CACHE;
typedef REBARBANDINFO_FID_CACHE *PREBARBANDINFO_FID_CACHE;

void cacheREBARBANDINFOFids(JNIEnv *env, jobject lpObject, PREBARBANDINFO_FID_CACHE lpCache);
REBARBANDINFO* getREBARBANDINFOFields(JNIEnv *env, jobject lpObject, REBARBANDINFO *lpStruct, PREBARBANDINFO_FID_CACHE lpCache);
void setREBARBANDINFOFields(JNIEnv *env, jobject lpObject, REBARBANDINFO *lpStruct, PREBARBANDINFO_FID_CACHE lpCache);

/* RECT struct */
typedef struct RECT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID left, top, right, bottom;
} RECT_FID_CACHE;
typedef RECT_FID_CACHE *PRECT_FID_CACHE;

void cacheRECTFids(JNIEnv *env, jobject lpObject, PRECT_FID_CACHE lpCache);
RECT* getRECTFields(JNIEnv *env, jobject lpObject, RECT *lpStruct, PRECT_FID_CACHE lpCache);
void setRECTFields(JNIEnv *env, jobject lpObject, RECT *lpStruct, PRECT_FID_CACHE lpCache);

/* SCROLLINFO struct */
typedef struct SCROLLINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, fMask, nMin, nMax, nPage, nPos, nTrackPos;
} SCROLLINFO_FID_CACHE;
typedef SCROLLINFO_FID_CACHE *PSCROLLINFO_FID_CACHE;

void cacheSCROLLINFOFids(JNIEnv *env, jobject lpObject, PSCROLLINFO_FID_CACHE lpCache);
SCROLLINFO* getSCROLLINFOFields(JNIEnv *env, jobject lpObject, SCROLLINFO *lpStruct, PSCROLLINFO_FID_CACHE lpCache);
void setSCROLLINFOFields(JNIEnv *env, jobject lpObject, SCROLLINFO *lpStruct, PSCROLLINFO_FID_CACHE lpCache);

/* SHELLEXECUTEINFO struct */
typedef struct SHELLEXECUTEINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, fMask, hwnd, lpVerb, lpFile, lpParameters, lpDirectory, nShow, hInstApp, lpIDList, lpClass, hkeyClass, dwHotKey, hIcon, hProcess;
} SHELLEXECUTEINFO_FID_CACHE;
typedef SHELLEXECUTEINFO_FID_CACHE *PSHELLEXECUTEINFO_FID_CACHE;

void cacheSHELLEXECUTEINFOFids(JNIEnv *env, jobject lpObject, PSHELLEXECUTEINFO_FID_CACHE lpCache);
SHELLEXECUTEINFO* getSHELLEXECUTEINFOFields(JNIEnv *env, jobject lpObject, SHELLEXECUTEINFO *lpStruct, PSHELLEXECUTEINFO_FID_CACHE lpCache);
void setSHELLEXECUTEINFOFields(JNIEnv *env, jobject lpObject, SHELLEXECUTEINFO *lpStruct, PSHELLEXECUTEINFO_FID_CACHE lpCache);

/* SIZE struct */
typedef struct SIZE_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cx, cy;
} SIZE_FID_CACHE;
typedef SIZE_FID_CACHE *PSIZE_FID_CACHE;

void cacheSIZEFids(JNIEnv *env, jobject lpObject, PSIZE_FID_CACHE lpCache);
SIZE* getSIZEFields(JNIEnv *env, jobject lpObject, SIZE *lpStruct, PSIZE_FID_CACHE lpCache);
void setSIZEFields(JNIEnv *env, jobject lpObject, SIZE *lpStruct, PSIZE_FID_CACHE lpCache);

/* TBBUTTON struct */
typedef struct TBBUTTON_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID iBitmap, idCommand, fsState, fsStyle, dwData, iString;
} TBBUTTON_FID_CACHE;
typedef TBBUTTON_FID_CACHE *PTBBUTTON_FID_CACHE;

void cacheTBBUTTONFids(JNIEnv *env, jobject lpObject, PTBBUTTON_FID_CACHE lpCache);
TBBUTTON* getTBBUTTONFields(JNIEnv *env, jobject lpObject, TBBUTTON *lpStruct, PTBBUTTON_FID_CACHE lpCache);
void setTBBUTTONFields(JNIEnv *env, jobject lpObject, TBBUTTON *lpStruct, PTBBUTTON_FID_CACHE lpCache);

/* TBBUTTONINFO struct */
typedef struct TBBUTTONINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, dwMask, idCommand, iImage, fsState, fsStyle, cx, lParam, pszText, cchText;
} TBBUTTONINFO_FID_CACHE;
typedef TBBUTTONINFO_FID_CACHE *PTBBUTTONINFO_FID_CACHE;

void cacheTBBUTTONINFOFids(JNIEnv *env, jobject lpObject, PTBBUTTONINFO_FID_CACHE lpCache);
TBBUTTONINFO* getTBBUTTONINFOFields(JNIEnv *env, jobject lpObject, TBBUTTONINFO *lpStruct, PTBBUTTONINFO_FID_CACHE lpCache);
void setTBBUTTONINFOFields(JNIEnv *env, jobject lpObject, TBBUTTONINFO *lpStruct, PTBBUTTONINFO_FID_CACHE lpCache);

/* TCITEM struct */
typedef struct TCITEM_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID mask, dwState, dwStateMask, pszText, cchTextMax, iImage, lParam;
} TCITEM_FID_CACHE;
typedef TCITEM_FID_CACHE *PTCITEM_FID_CACHE;

void cacheTCITEMFids(JNIEnv *env, jobject lpObject, PTCITEM_FID_CACHE lpCache);
TCITEM* getTCITEMFields(JNIEnv *env, jobject lpObject, TCITEM *lpStruct, PTCITEM_FID_CACHE lpCache);
void setTCITEMFields(JNIEnv *env, jobject lpObject, TCITEM *lpStruct, PTCITEM_FID_CACHE lpCache);

/* TEXTMETRIC struct */
typedef struct TEXTMETRIC_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID tmHeight, tmAscent, tmDescent, tmInternalLeading, tmExternalLeading, tmAveCharWidth, tmMaxCharWidth, tmWeight, tmOverhang, tmDigitizedAspectX, tmDigitizedAspectY, tmItalic, tmUnderlined, tmStruckOut, tmPitchAndFamily, tmCharSet;
} TEXTMETRIC_FID_CACHE;
typedef TEXTMETRIC_FID_CACHE *PTEXTMETRIC_FID_CACHE;

void cacheTEXTMETRICFids(JNIEnv *env, jobject lpObject, PTEXTMETRIC_FID_CACHE lpCache);
#ifndef _WIN32_WCE
TEXTMETRICA* getTEXTMETRICAFields(JNIEnv *env, jobject lpObject, TEXTMETRICA *lpStruct, PTEXTMETRIC_FID_CACHE lpCache);
void setTEXTMETRICAFields(JNIEnv *env, jobject lpObject, TEXTMETRICA *lpStruct, PTEXTMETRIC_FID_CACHE lpCache);
#endif // _WIN32_WCE
TEXTMETRICW* getTEXTMETRICWFields(JNIEnv *env, jobject lpObject, TEXTMETRICW *lpStruct, PTEXTMETRIC_FID_CACHE lpCache);
void setTEXTMETRICWFields(JNIEnv *env, jobject lpObject, TEXTMETRICW *lpStruct, PTEXTMETRIC_FID_CACHE lpCache);

#ifndef _WIN32_WCE

/* TOOLINFO struct */
typedef struct TOOLINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, uFlags, hwnd, uId, left, top, right, bottom, hinst, lpszText, lParam;
} TOOLINFO_FID_CACHE;
typedef TOOLINFO_FID_CACHE *PTOOLINFO_FID_CACHE;

void cacheTOOLINFOFids(JNIEnv *env, jobject lpObject, PTOOLINFO_FID_CACHE lpCache);
TOOLINFO* getTOOLINFOFields(JNIEnv *env, jobject lpObject, TOOLINFO *lpStruct, PTOOLINFO_FID_CACHE lpCache);
void setTOOLINFOFields(JNIEnv *env, jobject lpObject, TOOLINFO *lpStruct, PTOOLINFO_FID_CACHE lpCache);

#endif // _WIN32_WCE

#ifndef _WIN32_WCE

/* TRACKMOUSEEVENT struct */
typedef struct TRACKMOUSEEVENT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbSize, dwFlags, hwndTrack, dwHoverTime;
} TRACKMOUSEEVENT_FID_CACHE;
typedef TRACKMOUSEEVENT_FID_CACHE *PTRACKMOUSEEVENT_FID_CACHE;

void cacheTRACKMOUSEEVENTFids(JNIEnv *env, jobject lpObject, PTRACKMOUSEEVENT_FID_CACHE lpCache);
TRACKMOUSEEVENT* getTRACKMOUSEEVENTFields(JNIEnv *env, jobject lpObject, TRACKMOUSEEVENT *lpStruct, PTRACKMOUSEEVENT_FID_CACHE lpCache);
void setTRACKMOUSEEVENTFields(JNIEnv *env, jobject lpObject, TRACKMOUSEEVENT *lpStruct, PTRACKMOUSEEVENT_FID_CACHE lpCache);

#endif // _WIN32_WCE

#ifndef _WIN32_WCE

/* TRIVERTEX struct */
typedef struct TRIVERTEX_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID x, y, Red, Green, Blue, Alpha;
} TRIVERTEX_FID_CACHE;
typedef TRIVERTEX_FID_CACHE *PTRIVERTEX_FID_CACHE;

void cacheTRIVERTEXFids(JNIEnv *env, jobject lpObject, PTRIVERTEX_FID_CACHE lpCache);
TRIVERTEX* getTRIVERTEXFields(JNIEnv *env, jobject lpObject, TRIVERTEX *lpStruct, PTRIVERTEX_FID_CACHE lpCache);
void setTRIVERTEXFields(JNIEnv *env, jobject lpObject, TRIVERTEX *lpStruct, PTRIVERTEX_FID_CACHE lpCache);

#endif // _WIN32_WCE

/* TVHITTESTINFO struct */
typedef struct TVHITTESTINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID x, y, flags, hItem;
} TVHITTESTINFO_FID_CACHE;
typedef TVHITTESTINFO_FID_CACHE *PTVHITTESTINFO_FID_CACHE;

void cacheTVHITTESTINFOFids(JNIEnv *env, jobject lpObject, PTVHITTESTINFO_FID_CACHE lpCache);
TVHITTESTINFO* getTVHITTESTINFOFields(JNIEnv *env, jobject lpObject, TVHITTESTINFO *lpStruct, PTVHITTESTINFO_FID_CACHE lpCache);
void setTVHITTESTINFOFields(JNIEnv *env, jobject lpObject, TVHITTESTINFO *lpStruct, PTVHITTESTINFO_FID_CACHE lpCache);

/* TVINSERTSTRUCT struct */
typedef struct TVINSERTSTRUCT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID hParent, hInsertAfter, mask, hItem, state, stateMask, pszText, cchTextMax, iImage, iSelectedImage, cChildren, lParam;
} TVINSERTSTRUCT_FID_CACHE;
typedef TVINSERTSTRUCT_FID_CACHE *PTVINSERTSTRUCT_FID_CACHE;

void cacheTVINSERTSTRUCTFids(JNIEnv *env, jobject lpObject, PTVINSERTSTRUCT_FID_CACHE lpCache);
TVINSERTSTRUCT* getTVINSERTSTRUCTFields(JNIEnv *env, jobject lpObject, TVINSERTSTRUCT *lpStruct, PTVINSERTSTRUCT_FID_CACHE lpCache);
void setTVINSERTSTRUCTFields(JNIEnv *env, jobject lpObject, TVINSERTSTRUCT *lpStruct, PTVINSERTSTRUCT_FID_CACHE lpCache);

/* TVITEM struct */
typedef struct TVITEM_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID mask, hItem, state, stateMask, pszText, cchTextMax, iImage, iSelectedImage, cChildren, lParam;
} TVITEM_FID_CACHE;
typedef TVITEM_FID_CACHE *PTVITEM_FID_CACHE;

void cacheTVITEMFids(JNIEnv *env, jobject lpObject, PTVITEM_FID_CACHE lpCache);
TVITEM* getTVITEMFields(JNIEnv *env, jobject lpObject, TVITEM *lpStruct, PTVITEM_FID_CACHE lpCache);
void setTVITEMFields(JNIEnv *env, jobject lpObject, TVITEM *lpStruct, PTVITEM_FID_CACHE lpCache);

#ifndef _WIN32_WCE

/* WINDOWPLACEMENT struct */
typedef struct WINDOWPLACEMENT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID length, flags, showCmd, ptMinPosition_x, ptMinPosition_y, ptMaxPosition_x, ptMaxPosition_y, left, top, right, bottom;
} WINDOWPLACEMENT_FID_CACHE;
typedef WINDOWPLACEMENT_FID_CACHE *PWINDOWPLACEMENT_FID_CACHE;

void cacheWINDOWPLACEMENTFids(JNIEnv *env, jobject lpObject, PWINDOWPLACEMENT_FID_CACHE lpCache);
WINDOWPLACEMENT* getWINDOWPLACEMENTFields(JNIEnv *env, jobject lpObject, WINDOWPLACEMENT *lpStruct, PWINDOWPLACEMENT_FID_CACHE lpCache);
void setWINDOWPLACEMENTFields(JNIEnv *env, jobject lpObject, WINDOWPLACEMENT *lpStruct, PWINDOWPLACEMENT_FID_CACHE lpCache);

#endif // _WIN32_WCE

/* WINDOWPOS struct */
typedef struct WINDOWPOS_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID hwnd, hwndInsertAfter, x, y, cx, cy, flags;
} WINDOWPOS_FID_CACHE;
typedef WINDOWPOS_FID_CACHE *PWINDOWPOS_FID_CACHE;

void cacheWINDOWPOSFids(JNIEnv *env, jobject lpObject, PWINDOWPOS_FID_CACHE lpCache);
WINDOWPOS* getWINDOWPOSFields(JNIEnv *env, jobject lpObject, WINDOWPOS *lpStruct, PWINDOWPOS_FID_CACHE lpCache);
void setWINDOWPOSFields(JNIEnv *env, jobject lpObject, WINDOWPOS *lpStruct, PWINDOWPOS_FID_CACHE lpCache);

/* WNDCLASS struct */
typedef struct WNDCLASS_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID style, lpfnWndProc, cbClsExtra, cbWndExtra, hInstance, hIcon, hCursor, hbrBackground, lpszMenuName, lpszClassName;
} WNDCLASS_FID_CACHE;
typedef WNDCLASS_FID_CACHE *PWNDCLASS_FID_CACHE;

void cacheWNDCLASSFids(JNIEnv *env, jobject lpObject, PWNDCLASS_FID_CACHE lpCache);
WNDCLASS* getWNDCLASSFields(JNIEnv *env, jobject lpObject, WNDCLASS *lpStruct, PWNDCLASS_FID_CACHE lpCache);
void setWNDCLASSFields(JNIEnv *env, jobject lpObject, WNDCLASS *lpStruct, PWNDCLASS_FID_CACHE lpCache);

/************************ OLE ***************************/

/* used to cast Vtabl entries */

/* P_OLE_FN_x typedef for an OLE function returning int with x params*/
typedef jint (STDMETHODCALLTYPE *P_OLE_FN_9)(jint, jint, jint, jint, jint, jint, jint, jint, jint);
typedef jint (STDMETHODCALLTYPE *P_OLE_FN_8)(jint, jint, jint, jint, jint, jint, jint, jint);
typedef jint (STDMETHODCALLTYPE *P_OLE_FN_7)(jint, jint, jint, jint, jint, jint, jint);
typedef jint (STDMETHODCALLTYPE *P_OLE_FN_6)(jint, jint, jint, jint, jint, jint);
typedef jint (STDMETHODCALLTYPE *P_OLE_FN_5)(jint, jint, jint, jint, jint);
typedef jint (STDMETHODCALLTYPE *P_OLE_FN_4)(jint, jint, jint, jint);
typedef jint (STDMETHODCALLTYPE *P_OLE_FN_3)(jint, jint, jint);
typedef jint (STDMETHODCALLTYPE *P_OLE_FN_2)(jint, jint);
typedef jint (STDMETHODCALLTYPE *P_OLE_FN_1)(jint);
typedef jint (STDMETHODCALLTYPE *P_OLE_FN_0)(void);

/* CAUUID struct */
typedef struct CAUUID_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID pElems, cElems;
} CAUUID_FID_CACHE;
typedef CAUUID_FID_CACHE *PCAUUID_FID_CACHE;

void cacheCAUUIDFids(JNIEnv *env, jobject lpObject, PCAUUID_FID_CACHE lpCache);
CAUUID* getCAUUIDFields(JNIEnv *env, jobject lpObject, CAUUID *lpStruct, PCAUUID_FID_CACHE lpCache);
void setCAUUIDFields(JNIEnv *env, jobject lpObject, CAUUID *lpStruct, PCAUUID_FID_CACHE lpCache);

/* CONTROLINFO struct */
typedef struct CONTROLINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID dwFlags, /*filler,*/ cAccel, hAccel, cb;
} CONTROLINFO_FID_CACHE;
typedef CONTROLINFO_FID_CACHE *PCONTROLINFO_FID_CACHE;

void cacheCONTROLINFOFids(JNIEnv *env, jobject lpObject, PCONTROLINFO_FID_CACHE lpCache);
CONTROLINFO* getCONTROLINFOFields(JNIEnv *env, jobject lpObject, CONTROLINFO *lpStruct, PCONTROLINFO_FID_CACHE lpCache);
void setCONTROLINFOFields(JNIEnv *env, jobject lpObject, CONTROLINFO *lpStruct, PCONTROLINFO_FID_CACHE lpCache);

/* COSERVERINFO struct */
typedef struct COSERVERINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID dwReserved2, pAuthInfo, pwszName, dwReserved1;
} COSERVERINFO_FID_CACHE;
typedef COSERVERINFO_FID_CACHE *PCOSERVERINFO_FID_CACHE;

void cacheCOSERVERINFOFids(JNIEnv *env, jobject lpObject, PCOSERVERINFO_FID_CACHE lpCache);
COSERVERINFO* getCOSERVERINFOFields(JNIEnv *env, jobject lpObject, COSERVERINFO *lpStruct, PCOSERVERINFO_FID_CACHE lpCache);
void setCOSERVERINFOFields(JNIEnv *env, jobject lpObject, COSERVERINFO *lpStruct, PCOSERVERINFO_FID_CACHE lpCache);

/* DISPPARAMS struct */
typedef struct DISPPARAMS_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cNamedArgs, cArgs, rgdispidNamedArgs, rgvarg;
} DISPPARAMS_FID_CACHE;
typedef DISPPARAMS_FID_CACHE *PDISPPARAMS_FID_CACHE;

void cacheDISPPARAMSFids(JNIEnv *env, jobject lpObject, PDISPPARAMS_FID_CACHE lpCache);
DISPPARAMS* getDISPPARAMSFields(JNIEnv *env, jobject lpObject, DISPPARAMS *lpStruct, PDISPPARAMS_FID_CACHE lpCache);
void setDISPPARAMSFields(JNIEnv *env, jobject lpObject, DISPPARAMS *lpStruct, PDISPPARAMS_FID_CACHE lpCache);

#ifndef _WIN32_WCE

/* DROPFILES struct */
typedef struct DROPFILES_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID fWide, fNC, pt_y, pt_x, pFiles;
} DROPFILES_FID_CACHE;
typedef DROPFILES_FID_CACHE *PDROPFILES_FID_CACHE;

void cacheDROPFILESFids(JNIEnv *env, jobject lpObject, PDROPFILES_FID_CACHE lpCache);
DROPFILES* getDROPFILESFields(JNIEnv *env, jobject lpObject, DROPFILES *lpStruct, PDROPFILES_FID_CACHE lpCache);
void setDROPFILESFields(JNIEnv *env, jobject lpObject, DROPFILES *lpStruct, PDROPFILES_FID_CACHE lpCache);

#endif // _WIN32_WCE

/* DVTARGETDEVICE struct */
typedef struct DVTARGETDEVICE_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID tdData, tdExtDevmodeOffset, tdPortNameOffset, tdDeviceNameOffset, tdDriverNameOffset, tdSize;
} DVTARGETDEVICE_FID_CACHE;
typedef DVTARGETDEVICE_FID_CACHE *PDVTARGETDEVICE_FID_CACHE;

void cacheDVTARGETDEVICEFids(JNIEnv *env, jobject lpObject, PDVTARGETDEVICE_FID_CACHE lpCache);
DVTARGETDEVICE* getDVTARGETDEVICEFields(JNIEnv *env, jobject lpObject, DVTARGETDEVICE *lpStruct, PDVTARGETDEVICE_FID_CACHE lpCache);
void setDVTARGETDEVICEFields(JNIEnv *env, jobject lpObject, DVTARGETDEVICE *lpStruct, PDVTARGETDEVICE_FID_CACHE lpCache);

/* EXCEPINFO struct */
typedef struct EXCEPINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID scode, pfnDeferredFillIn, pvReserved, dwHelpContext, bstrHelpFile, bstrDescription, bstrSource, wReserved, wCode;
} EXCEPINFO_FID_CACHE;
typedef EXCEPINFO_FID_CACHE *PEXCEPINFO_FID_CACHE;

void cacheEXCEPINFOFids(JNIEnv *env, jobject lpObject, PEXCEPINFO_FID_CACHE lpCache);
EXCEPINFO* getEXCEPINFOFields(JNIEnv *env, jobject lpObject, EXCEPINFO *lpStruct, PEXCEPINFO_FID_CACHE lpCache);
void setEXCEPINFOFields(JNIEnv *env, jobject lpObject, EXCEPINFO *lpStruct, PEXCEPINFO_FID_CACHE lpCache);

/* FORMATETC struct */
typedef struct FORMATETC_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID tymed, lindex, dwAspect, ptd, cfFormat;
} FORMATETC_FID_CACHE;
typedef FORMATETC_FID_CACHE *PFORMATETC_FID_CACHE;

void cacheFORMATETCFids(JNIEnv *env, jobject lpObject, PFORMATETC_FID_CACHE lpCache);
FORMATETC* getFORMATETCFields(JNIEnv *env, jobject lpObject, FORMATETC *lpStruct, PFORMATETC_FID_CACHE lpCache);
void setFORMATETCFields(JNIEnv *env, jobject lpObject, FORMATETC *lpStruct, PFORMATETC_FID_CACHE lpCache);

/* FUNCDESC1 struct */
typedef struct FUNCDESC1_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID wFuncFlags, elemdescFunc_paramdesc_wParamFlags, elemdescFunc_paramdesc_pparamdescex, elemdescFunc_tdesc_filler, elemdescFunc_tdesc_vt, elemdescFunc_tdesc_union, cScodes, oVft, cParamsOpt, cParams, callconv, invkind, funckind, lprgelemdescParam, lprgscode, memid;
} FUNCDESC1_FID_CACHE;
typedef FUNCDESC1_FID_CACHE *PFUNCDESC1_FID_CACHE;

void cacheFUNCDESC1Fids(JNIEnv *env, jobject lpObject, PFUNCDESC1_FID_CACHE lpCache);
FUNCDESC* getFUNCDESC1Fields(JNIEnv *env, jobject lpObject, FUNCDESC *lpStruct, PFUNCDESC1_FID_CACHE lpCache);
void setFUNCDESC1Fields(JNIEnv *env, jobject lpObject, FUNCDESC *lpStruct, PFUNCDESC1_FID_CACHE lpCache);

/* FUNCDESC2 struct */
typedef struct FUNCDESC2_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID wFuncFlags, elemdescFunc_idldesc_filler, elemdescFunc_idldesc_wIDLFlags, elemdescFunc_idldesc_dwReserved, elemdescFunc_tdesc_filler, elemdescFunc_tdesc_vt, elemdescFunc_tdesc_union, cScodes, oVft, cParamsOpt, cParams, callconv, invkind, funckind, lprgelemdescParam, lprgscode, memid;
} FUNCDESC2_FID_CACHE;
typedef FUNCDESC2_FID_CACHE *PFUNCDESC2_FID_CACHE;

void cacheFUNCDESC2Fids(JNIEnv *env, jobject lpObject, PFUNCDESC2_FID_CACHE lpCache);
FUNCDESC* getFUNCDESC2Fields(JNIEnv *env, jobject lpObject, FUNCDESC *lpStruct, PFUNCDESC2_FID_CACHE lpCache);
void setFUNCDESC2Fields(JNIEnv *env, jobject lpObject, FUNCDESC *lpStruct, PFUNCDESC2_FID_CACHE lpCache);

/* GUID struct */
typedef struct GUID_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID b7, b6, b5, b4, b3, b2, b1, b0, data3, data2, data1;
} GUID_FID_CACHE;
typedef GUID_FID_CACHE *PGUID_FID_CACHE;

void cacheGUIDFids(JNIEnv *env, jobject lpObject, PGUID_FID_CACHE lpCache);
GUID* getGUIDFields(JNIEnv *env, jobject lpObject, GUID *lpStruct, PGUID_FID_CACHE lpCache);
void setGUIDFields(JNIEnv *env, jobject lpObject, GUID *lpStruct, PGUID_FID_CACHE lpCache);

/* LICINFO struct */
typedef struct LICINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID fLicVerified, fRuntimeKeyAvail, cbLicInfo;
} LICINFO_FID_CACHE;
typedef LICINFO_FID_CACHE *PLICINFO_FID_CACHE;

void cacheLICINFOFids(JNIEnv *env, jobject lpObject, PLICINFO_FID_CACHE lpCache);
LICINFO* getLICINFOFields(JNIEnv *env, jobject lpObject, LICINFO *lpStruct, PLICINFO_FID_CACHE lpCache);
void setLICINFOFields(JNIEnv *env, jobject lpObject, LICINFO *lpStruct, PLICINFO_FID_CACHE lpCache);

#ifndef _WIN32_WCE

/* OLECMD struct */
typedef struct OLECMD_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cmdf, cmdID;
} OLECMD_FID_CACHE;
typedef OLECMD_FID_CACHE *POLECMD_FID_CACHE;

void cacheOLECMDFids(JNIEnv *env, jobject lpObject, POLECMD_FID_CACHE lpCache);
OLECMD* getOLECMDFields(JNIEnv *env, jobject lpObject, OLECMD *lpStruct, POLECMD_FID_CACHE lpCache);
void setOLECMDFields(JNIEnv *env, jobject lpObject, OLECMD *lpStruct, POLECMD_FID_CACHE lpCache);

#endif // _WIN32_WCE

#ifndef _WIN32_WCE

/* OLECMDTEXT struct */
typedef struct OLECMDTEXT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID rgwz, cwBuf, cwActual, cmdtextf;
} OLECMDTEXT_FID_CACHE;
typedef OLECMDTEXT_FID_CACHE *POLECMDTEXT_FID_CACHE;

void cacheOLECMDTEXTFids(JNIEnv *env, jobject lpObject, POLECMDTEXT_FID_CACHE lpCache);
OLECMDTEXT* getOLECMDTEXTFields(JNIEnv *env, jobject lpObject, OLECMDTEXT *lpStruct, POLECMDTEXT_FID_CACHE lpCache);
void setOLECMDTEXTFields(JNIEnv *env, jobject lpObject, OLECMDTEXT *lpStruct, POLECMDTEXT_FID_CACHE lpCache);

#endif // _WIN32_WCE

/* OLEINPLACEFRAMEINFO struct */
typedef struct OLEINPLACEFRAMEINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cAccelEntries, haccel, hwndFrame, fMDIApp, cb;
} OLEINPLACEFRAMEINFO_FID_CACHE;
typedef OLEINPLACEFRAMEINFO_FID_CACHE *POLEINPLACEFRAMEINFO_FID_CACHE;

void cacheOLEINPLACEFRAMEINFOFids(JNIEnv *env, jobject lpObject, POLEINPLACEFRAMEINFO_FID_CACHE lpCache);
OLEINPLACEFRAMEINFO* getOLEINPLACEFRAMEINFOFields(JNIEnv *env, jobject lpObject, OLEINPLACEFRAMEINFO *lpStruct, POLEINPLACEFRAMEINFO_FID_CACHE lpCache);
void setOLEINPLACEFRAMEINFOFields(JNIEnv *env, jobject lpObject, OLEINPLACEFRAMEINFO *lpStruct, POLEINPLACEFRAMEINFO_FID_CACHE lpCache);

/* STATSTG struct */
typedef struct STATSTG_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID reserved, grfStateBits, clsid_b7, clsid_b6, clsid_b5, clsid_b4, clsid_b3, clsid_b2, clsid_b1, clsid_b0, clsid_data3, clsid_data2, clsid_data1, grfLocksSupported, grfMode, atime_dwHighDateTime, atime_dwLowDateTime, ctime_dwHighDateTime, ctime_dwLowDateTime, mtime_dwHighDateTime, mtime_dwLowDateTime, cbSize, type, pwcsName;
} STATSTG_FID_CACHE;
typedef STATSTG_FID_CACHE *PSTATSTG_FID_CACHE;

void cacheSTATSTGFids(JNIEnv *env, jobject lpObject, PSTATSTG_FID_CACHE lpCache);
STATSTG* getSTATSTGFields(JNIEnv *env, jobject lpObject, STATSTG *lpStruct, PSTATSTG_FID_CACHE lpCache);
void setSTATSTGFields(JNIEnv *env, jobject lpObject, STATSTG *lpStruct, PSTATSTG_FID_CACHE lpCache);

/* STGMEDIUM struct */
typedef struct STGMEDIUM_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID pUnkForRelease, unionField, tymed;
} STGMEDIUM_FID_CACHE;
typedef STGMEDIUM_FID_CACHE *PSTGMEDIUM_FID_CACHE;

void cacheSTGMEDIUMFids(JNIEnv *env, jobject lpObject, PSTGMEDIUM_FID_CACHE lpCache);
STGMEDIUM* getSTGMEDIUMFields(JNIEnv *env, jobject lpObject, STGMEDIUM *lpStruct, PSTGMEDIUM_FID_CACHE lpCache);
void setSTGMEDIUMFields(JNIEnv *env, jobject lpObject, STGMEDIUM *lpStruct, PSTGMEDIUM_FID_CACHE lpCache);

/* TYPEATTR struct */
typedef struct TYPEATTR_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID idldescType_wIDLFlags, idldescType_dwReserved, filler, tdescAlias_vt, tdescAlias_unionField, wMinorVerNum, wMajorVerNum, wTypeFlags, cbAlignment, cbSizeVft, cImplTypes, cVars, cFuncs, typekind, cbSizeInstance, lpstrSchema, memidDestructor, memidConstructor, dwReserved, lcid, guid_b7, guid_b6, guid_b5, guid_b4, guid_b3, guid_b2, guid_b1, guid_b0, guid_data3, guid_data2, guid_data1;
} TYPEATTR_FID_CACHE;
typedef TYPEATTR_FID_CACHE *PTYPEATTR_FID_CACHE;

void cacheTYPEATTRFids(JNIEnv *env, jobject lpObject, PTYPEATTR_FID_CACHE lpCache);
TYPEATTR* getTYPEATTRFields(JNIEnv *env, jobject lpObject, TYPEATTR *lpStruct, PTYPEATTR_FID_CACHE lpCache);
void setTYPEATTRFields(JNIEnv *env, jobject lpObject, TYPEATTR *lpStruct, PTYPEATTR_FID_CACHE lpCache);

/* VARDESC1 struct */
typedef struct VARDESC1_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID varkind, filler, wVarFlags, elemdescVar_paramdesc_filler, elemdescVar_paramdesc_wParamFlags, elemdescVar_paramdesc_pparamdescex, elemdescVar_tdesc_filler, elemdescVar_tdesc_vt, elemdescVar_tdesc_union, unionField, lpstrSchema, memid;
} VARDESC1_FID_CACHE;
typedef VARDESC1_FID_CACHE *PVARDESC1_FID_CACHE;

void cacheVARDESC1Fids(JNIEnv *env, jobject lpObject, PVARDESC1_FID_CACHE lpCache);
VARDESC* getVARDESC1Fields(JNIEnv *env, jobject lpObject, VARDESC *lpStruct, PVARDESC1_FID_CACHE lpCache);
void setVARDESC1Fields(JNIEnv *env, jobject lpObject, VARDESC *lpStruct, PVARDESC1_FID_CACHE lpCache);

/* VARDESC2 struct */
typedef struct VARDESC2_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID varkind, filler, wVarFlags, elemdescFunc_idldesc_filler, elemdescFunc_idldesc_wIDLFlags, elemdescFunc_idldesc_dwReserved, elemdescVar_tdesc_filler, elemdescVar_tdesc_vt, elemdescVar_tdesc_union, unionField, lpstrSchema, memid;
} VARDESC2_FID_CACHE;
typedef VARDESC2_FID_CACHE *PVARDESC2_FID_CACHE;

void cacheVARDESC2Fids(JNIEnv *env, jobject lpObject, PVARDESC2_FID_CACHE lpCache);
VARDESC* getVARDESC2Fields(JNIEnv *env, jobject lpObject, VARDESC *lpStruct, PVARDESC2_FID_CACHE lpCache);
void setVARDESC2Fields(JNIEnv *env, jobject lpObject, VARDESC *lpStruct, PVARDESC2_FID_CACHE lpCache);

/**************************** END OLE ****************************/

/* Win32 globals */
extern ACCEL_FID_CACHE ACCELFc;
extern BITMAP_FID_CACHE BITMAPFc;
extern CHOOSECOLOR_FID_CACHE CHOOSECOLORFc;
extern COMPOSITIONFORM_FID_CACHE COMPOSITIONFORMFc;
extern CREATESTRUCT_FID_CACHE CREATESTRUCTFc;
extern DIBSECTION_FID_CACHE DIBSECTIONFc;
extern DLLVERSIONINFO_FID_CACHE DLLVERSIONINFOFc;
extern DRAWITEMSTRUCT_FID_CACHE DRAWITEMSTRUCTFc;
extern FILETIME_FID_CACHE FILETIMEFc;
extern HDITEM_FID_CACHE HDITEMFc;
extern ICONINFO_FID_CACHE ICONINFOFc;
extern INITCOMMONCONTROLSEX_FID_CACHE INITCOMMONCONTROLSEXFc;
extern LOGBRUSH_FID_CACHE LOGBRUSHFc;
extern LOGFONT_FID_CACHE LOGFONTFc;
extern LOGPEN_FID_CACHE LOGPENFc;
extern LVCOLUMN_FID_CACHE LVCOLUMNFc;
extern LVHITTESTINFO_FID_CACHE LVHITTESTINFOFc;
extern LVITEM_FID_CACHE LVITEMFc;
extern MEASUREITEMSTRUCT_FID_CACHE MEASUREITEMSTRUCTFc;
extern MENUITEMINFO_FID_CACHE MENUITEMINFOFc;
extern MSG_FID_CACHE MSGFc;
extern NMHDR_FID_CACHE NMHDRFc;
extern NMHEADER_FID_CACHE NMHEADERFc;
extern NMLISTVIEW_FID_CACHE NMLISTVIEWFc;
extern NMTOOLBAR_FID_CACHE NMTOOLBARFc;
extern NONCLIENTMETRICS_FID_CACHE NONCLIENTMETRICSFc;
extern OPENFILENAME_FID_CACHE OPENFILENAMEFc;
extern OSVERSIONINFO_FID_CACHE OSVERSIONINFOFc;
extern PAINTSTRUCT_FID_CACHE PAINTSTRUCTFc;
extern POINT_FID_CACHE POINTFc;
extern REBARBANDINFO_FID_CACHE REBARBANDINFOFc;
extern RECT_FID_CACHE RECTFc;
extern SCROLLINFO_FID_CACHE SCROLLINFOFc;
extern SHELLEXECUTEINFO_FID_CACHE SHELLEXECUTEINFOFc;
extern SIZE_FID_CACHE SIZEFc;
extern TBBUTTON_FID_CACHE TBBUTTONFc;
extern TBBUTTONINFO_FID_CACHE TBBUTTONINFOFc;
extern TCITEM_FID_CACHE TCITEMFc;
extern TEXTMETRIC_FID_CACHE TEXTMETRICFc;
extern TVHITTESTINFO_FID_CACHE TVHITTESTINFOFc;
extern TVINSERTSTRUCT_FID_CACHE TVINSERTSTRUCTFc;
extern TVITEM_FID_CACHE TVITEMFc;
extern WINDOWPOS_FID_CACHE WINDOWPOSFc;
extern WNDCLASS_FID_CACHE WNDCLASSFc;
#ifndef _WIN32_WCE
extern 	BROWSEINFO_FID_CACHE BROWSEINFOFc;
extern 	CHOOSEFONT_FID_CACHE CHOOSEFONTFc;
extern 	DOCINFO_FID_CACHE DOCINFOFc;
extern 	GCP_RESULTS_FID_CACHE GCP_RESULTSFc;
extern 	GRADIENT_RECT_FID_CACHE GRADIENT_RECTFc;
extern 	HELPINFO_FID_CACHE HELPINFOFc;
extern 	MENUINFO_FID_CACHE MENUINFOFc;
extern 	NMTTDISPINFO_FID_CACHE NMTTDISPINFOFc;
extern 	PRINTDLG_FID_CACHE PRINTDLGFc;
extern 	TOOLINFO_FID_CACHE TOOLINFOFc;
extern 	TRACKMOUSEEVENT_FID_CACHE TRACKMOUSEEVENTFc;
extern 	TRIVERTEX_FID_CACHE TRIVERTEXFc;
extern 	WINDOWPLACEMENT_FID_CACHE WINDOWPLACEMENTFc;
extern 	DROPFILES_FID_CACHE DROPFILESFc;
extern 	OLECMD_FID_CACHE OLECMDFc;
extern 	OLECMDTEXT_FID_CACHE OLECMDTEXTFc;
#endif

/* OLE globals */
extern CAUUID_FID_CACHE CAUUIDFc;
extern CONTROLINFO_FID_CACHE CONTROLINFOFc;
extern COSERVERINFO_FID_CACHE COSERVERINFOFc;
extern DISPPARAMS_FID_CACHE DISPPARAMSFc;
extern DVTARGETDEVICE_FID_CACHE DVTARGETDEVICEFc;
extern EXCEPINFO_FID_CACHE EXCEPINFOFc;
extern FORMATETC_FID_CACHE FORMATETCFc;
extern FUNCDESC1_FID_CACHE FUNCDESC1Fc;
extern FUNCDESC2_FID_CACHE FUNCDESC2Fc;
extern GUID_FID_CACHE GUIDFc;
extern LICINFO_FID_CACHE LICINFOFc;
extern OLEINPLACEFRAMEINFO_FID_CACHE OLEINPLACEFRAMEINFOFc;
extern STATSTG_FID_CACHE STATSTGFc;
extern STGMEDIUM_FID_CACHE STGMEDIUMFc;
extern TYPEATTR_FID_CACHE TYPEATTRFc;
extern VARDESC1_FID_CACHE VARDESC1Fc;
extern VARDESC2_FID_CACHE VARDESC2Fc;

#endif // INC_structs_H
