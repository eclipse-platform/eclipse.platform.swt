/*
 * (c) Copyright IBM Corp., 2000, 2001
 * All Rights Reserved.
 */

/**
 * JNI SWT object field getters and setters declarations for Windows structs.
 */

#ifndef INC_structs_H
#define INC_structs_H

#define USE_2000_CALLS
//#undef USE_2000_CALLS

#ifdef USE_2000_CALLS
#define WINVER 0x0500
#define _WIN32_IE 0x0500
#endif

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#define VC_EXTRALEAN

#include <windows.h>
#include <winuser.h>
#include <commctrl.h>
#include <oaidl.h>
#include <shlobj.h>
#include <ole2.h>
#include <olectl.h>
#include <objbase.h>
#include <richedit.h>
#include <shlwapi.h>

/* All globals to be declared in globals.h */
#define FID_CACHE_GLOBALS \
	MULTI_QI_FID_CACHE MultiqiFc; \
	OLEINPLACEFRAMEINFO_FID_CACHE OleinplaceframeinfoFc; \
	CAUUID_FID_CACHE CauuidFc; \
	HDLAYOUT_FID_CACHE HdlayoutFc; \
	OLECMD_FID_CACHE OlecmdFc; \
	TYPEATTR_FID_CACHE TypeattrFc; \
	FORMATETC_FID_CACHE FormatetcFc; \
	OLECMDTEXT_FID_CACHE OlecmdtextFc; \
	GUID_FID_CACHE GuidFc; \
	COSERVERINFO_FID_CACHE CoserverinfoFc; \
	STGMEDIUM_FID_CACHE StgmediumFc; \
	EXCEPINFO_FID_CACHE ExcepinfoFc; \
	DVTARGETDEVICE_FID_CACHE DvtargetdeviceFc; \
	DISPPARAMS_FID_CACHE DispparamsFc; \
	CONTROLINFO_FID_CACHE ControlinfoFc; \
	STATSTG_FID_CACHE StatstgFc; \
	LICINFO_FID_CACHE LicinfoFc; \
	DROPFILES_FID_CACHE DropfilesFc; \
	TVHITTESTINFO_FID_CACHE TvhittestinfoFc; \
	WINDOWPOS_FID_CACHE WindowposFc; \
	ACCEL_FID_CACHE AccelFc; \
	DRAWITEMSTRUCT_FID_CACHE DrawitemstructFc; \
	NMLISTVIEW_FID_CACHE NmlistviewFc; \
	TEXTMETRIC_FID_CACHE TextmetricFc; \
	RECT_FID_CACHE RectFc; \
	LOGBRUSH_FID_CACHE LogbrushFc; \
	CHOOSECOLOR_FID_CACHE ChoosecolorFc; \
	TOOLINFO_FID_CACHE ToolinfoFc; \
	SCROLLINFO_FID_CACHE ScrollinfoFc; \
	MEASUREITEMSTRUCT_FID_CACHE MeasureitemstructFc; \
	CREATESTRUCT_FID_CACHE CreatestructFc; \
	OPENFILENAME_FID_CACHE OpenfilenameFc; \
	LVITEM_FID_CACHE LvitemFc; \
	TVITEM_FID_CACHE TvitemFc; \
	LVHITTESTINFO_FID_CACHE LvhittestinfoFc; \
	NMTVCUSTOMDRAW_FID_CACHE NmtvcustomdrawFc; \
	CHOOSEFONT_FID_CACHE ChoosefontFc; \
	HDITEM_FID_CACHE HditemFc; \
	NMTOOLBAR_FID_CACHE NmtoolbarFc; \
	TBBUTTON_FID_CACHE TbbuttonFc; \
	NMTTDISPINFO_FID_CACHE NmttdispinfoFc; \
	WINDOWPLACEMENT_FID_CACHE WindowplacementFc; \
	REBARBANDINFO_FID_CACHE RebarbandinfoFc; \
	POINT_FID_CACHE PointFc; \
	TBBUTTONINFO_FID_CACHE TbbuttoninfoFc; \
	NMHDR_FID_CACHE NmhdrFc; \
	SIZE_FID_CACHE SizeFc; \
	INITCOMMONCONTROLSEX_FID_CACHE InitcommoncontrolsexFc; \
	DLLVERSIONINFO_FID_CACHE DllversioninfoFc; \
	MSGFILTER_FID_CACHE MsgfilterFc; \
	BITMAP_FID_CACHE BitmapFc; \
	PAINTSTRUCT_FID_CACHE PaintstructFc; \
	MENUITEMINFO_FID_CACHE MenuiteminfoFc; \
	ICONINFO_FID_CACHE IconinfoFc; \
	WNDCLASSEX_FID_CACHE WndclassexFc; \
	MENUINFO_FID_CACHE MenuinfoFc; \
	LOGPEN_FID_CACHE LogpenFc; \
	COMPOSITIONFORM_FID_CACHE CompositionformFc; \
	LVCOLUMN_FID_CACHE LvcolumnFc; \
	BROWSEINFO_FID_CACHE BrowseinfoFc; \
	DOCINFO_FID_CACHE DocinfoFc; \
	TVINSERTSTRUCT_FID_CACHE TvinsertstructFc; \
	HELPINFO_FID_CACHE HelpinfoFc; \
	LOGFONT_FID_CACHE LogfontFc; \
	DIBSECTION_FID_CACHE DibsectionFc; \
	NMHEADER_FID_CACHE NmheaderFc; \
	TCITEM_FID_CACHE TcitemFc; \
	PRINTDLG_FID_CACHE PrintdlgFc; \
	MSG_FID_CACHE MsgFc; \
	PAGESETUPDLG_FID_CACHE PagesetupdlgFc; \
	TRACKMOUSEEVENT_FID_CACHE TrackmouseeventFc; \
	FUNCDESC1_FID_CACHE Funcdesc1Fc; \
	FUNCDESC2_FID_CACHE Funcdesc2Fc; \
	VARDESC1_FID_CACHE Vardesc1Fc; \
	VARDESC2_FID_CACHE Vardesc2Fc; \
	GCP_RESULTS_FID_CACHE GCP_RESULTSFc; \
	TRIVERTEX_FID_CACHE TrivertexFc; \
	GRADIENT_RECT_FID_CACHE GradientrectFc;

/*	PARAFORMAT_FID_CACHE ParaformatFc; \*/
/*	CHARFORMAT_FID_CACHE CharformatFc; \*/
/*	CHARFORMAT2_FID_CACHE Charformat2Fc; \*/
/*	SHFILEINFO_FID_CACHE ShfileinfoFc; \*/
/*	EXTLOGPEN_FID_CACHE ExtlogpenFc; \*/
	
/* ----------- fid and class caches  ----------- */
/**
 * Used for Java objects passed into JNI that are
 * declared like:
 *
 * 	nativeFunction (Rectangle p1, Rectangle p2, Rectangle p3)
 *
 * and not like this
 *
 * 	nativeFunction (Object p1, Object p2, Object p3)
 *
 *
 */

/* ----------- fid cache structures  ----------- */

/* CALLBACK struct */
typedef struct CALLBACK_FID_CACHE {

    int cached;
    jclass callbackClass;
    jfieldID object, method, signature, isStatic;

} CALLBACK_FID_CACHE;

typedef CALLBACK_FID_CACHE *PCALLBACK_FID_CACHE;

/* ACCEL struct */
typedef struct ACCEL_FID_CACHE {
    
    int cached;
    jclass accelClass;
    jfieldID fVirt, key, cmd;

} ACCEL_FID_CACHE;

typedef ACCEL_FID_CACHE *PACCEL_FID_CACHE;

/* BITMAP struct */
typedef struct BITMAP_FID_CACHE {
    
    int cached;
    jclass bitmapClass;
    jfieldID bmType, bmWidth, bmHeight, bmWidthBytes, bmPlanes, \
             bmBitsPixel, bmBits;

} BITMAP_FID_CACHE;

typedef BITMAP_FID_CACHE *PBITMAP_FID_CACHE;

/* BROWSEINFO struct */
typedef struct BROWSEINFO_FID_CACHE {
    
    int cached;
    jclass browseinfoClass;
    jfieldID hwndOwner, pidlRoot, pszDisplayName, lpszTitle, ulFlags, \
             lpfn, lParam, iImage;

} BROWSEINFO_FID_CACHE;

typedef BROWSEINFO_FID_CACHE *PBROWSEINFO_FID_CACHE;

/* CHARFORMAT struct */
/*
typedef struct CHARFORMAT_FID_CACHE {
    
    int cached;
    jclass charformatClass;
    jfieldID cbSize, dwMask, dwEffects, yHeight, yOffset, crTextColor, \
             bCharSet, bPitchAndFamily, \
             szFaceName0, szFaceName1, szFaceName2, szFaceName3, szFaceName4, \
             szFaceName5, szFaceName6, szFaceName7, szFaceName8, szFaceName9, \
             szFaceName10, szFaceName11, szFaceName12, szFaceName13, szFaceName14, \
             szFaceName15, szFaceName16, szFaceName17, szFaceName18, szFaceName19, \
             szFaceName20, szFaceName21, szFaceName22, szFaceName23, szFaceName24, \
             szFaceName25, szFaceName26, szFaceName27, szFaceName28, szFaceName29, \
             szFaceName30, szFaceName31;

} CHARFORMAT_FID_CACHE;
typedef CHARFORMAT_FID_CACHE *PCHARFORMAT_FID_CACHE;
*/

/* CHARFORMAT2 struct */
/*
typedef struct CHARFORMAT2_FID_CACHE {
    
    int cached;
    jclass charformat2Class;
    jfieldID cbSize, dwMask, dwEffects, yHeight, yOffset, crTextColor, \
             bCharSet, bPitchAndFamily, \
             szFaceName0, szFaceName1, szFaceName2, szFaceName3, szFaceName4, \
             szFaceName5, szFaceName6, szFaceName7, szFaceName8, szFaceName9, \
             szFaceName10, szFaceName11, szFaceName12, szFaceName13, szFaceName14, \
             szFaceName15, szFaceName16, szFaceName17, szFaceName18, szFaceName19, \
             szFaceName20, szFaceName21, szFaceName22, szFaceName23, szFaceName24, \
             szFaceName25, szFaceName26, szFaceName27, szFaceName28, szFaceName29, \
             szFaceName30, szFaceName31, \
             wWeight, sSpacing, crBackColor, lcid, dwReserved, sStyle, wKerning, \
             bUnderlineType, bAnimation, bRevAuthor, bReserved1;
} CHARFORMAT2_FID_CACHE;
typedef CHARFORMAT2_FID_CACHE *PCHARFORMAT2_FID_CACHE;
*/

/* CHOOSECOLOR struct */
typedef struct CHOOSECOLOR_FID_CACHE {
    
    int cached;
    jclass choosecolorClass;
    jfieldID lStructSize, hwndOwner, hInstance, rgbResult, lpCustColors, \
             Flags, lCustData, lpfnHook, lpTemplateName;

} CHOOSECOLOR_FID_CACHE;

typedef CHOOSECOLOR_FID_CACHE *PCHOOSECOLOR_FID_CACHE;

/* CHOOSEFONT struct */
typedef struct CHOOSEFONT_FID_CACHE {
    
    int cached;
    jclass choosefontClass;
    jfieldID lStructSize, hwndOwner, hDC, lpLogFont, \
             iPointSize, Flags, rgbColors, lCustData, lpfnHook, \
             lpTemplateName, hInstance, lpszStyle, nFontType, \
             ___MISSING_ALIGNMENT__, nSizeMin, nSizeMax;

} CHOOSEFONT_FID_CACHE;

typedef CHOOSEFONT_FID_CACHE *PCHOOSEFONT_FID_CACHE;

/* COMPOSITIONFORM struct */
typedef struct COMPOSITIONFORM_FID_CACHE {
    
    int cached;
    jclass compositionformClass;
    jfieldID dwStyle, x, y, left, top, right, bottom;

} COMPOSITIONFORM_FID_CACHE;

typedef COMPOSITIONFORM_FID_CACHE *PCOMPOSITIONFORM_FID_CACHE;

/* CREATESTRUCT struct */
typedef struct CREATESTRUCT_FID_CACHE {
    
    int cached;
    jclass createstructClass;
    jfieldID lpCreateParams, hInstance, hMenu, hwndParent, \
             cx, cy, x, y, style, lpszName, lpszClass, \
             dwExStyle;

} CREATESTRUCT_FID_CACHE;

typedef CREATESTRUCT_FID_CACHE *PCREATESTRUCT_FID_CACHE;

/* DIBSECTION struct */
typedef struct DIBSECTION_FID_CACHE {
    
    int cached;
    jclass dibsectionClass;
    jfieldID bmType, bmWidth, bmHeight, bmWidthBytes, bmPlanes, \
             bmBitsPixel, bmBits,\
             biSize, biWidth, biHeight, biPlanes, biBitCount, \
             biCompression, biSizeImage, biXPelsPerMeter, biYPelsPerMeter, \
             biClrUsed, biClrImportant, dsBitfields0, dsBitfields1, \
             dsBitfields2, dshSection, dsOffset;

} DIBSECTION_FID_CACHE;

typedef DIBSECTION_FID_CACHE *PDIBSECTION_FID_CACHE;

/* DLLVERSIONINFO struct */
typedef struct DLLVERSIONINFO_FID_CACHE {
    
    int cached;
    jclass dllversioninfoClass;
    jfieldID cbSize, dwMajorVersion, dwMinorVersion, dwBuildNumber, dwPlatformID;

} DLLVERSIONINFO_FID_CACHE;

typedef DLLVERSIONINFO_FID_CACHE *PDLLVERSIONINFO_FID_CACHE;

/* DOCINFO struct */
typedef struct DOCINFO_FID_CACHE {
    
    int cached;
    jclass docinfoClass;
    jfieldID cbSize, lpszDocName, lpszOutput, lpszDatatype, fwType;

} DOCINFO_FID_CACHE;

typedef DOCINFO_FID_CACHE *PDOCINFO_FID_CACHE;

/* DRAWITEMSTRUCT struct */
typedef struct DRAWITEMSTRUCT_FID_CACHE {
    
    int cached;
    jclass drawitemstructClass;
    jfieldID CtlType, CtlID, itemID, itemAction, \
             itemState, hwndItem, hDC, left, top, \
             right, bottom, itemData;

} DRAWITEMSTRUCT_FID_CACHE;

typedef DRAWITEMSTRUCT_FID_CACHE *PDRAWITEMSTRUCT_FID_CACHE;

/* GRADIENT_RECT struct */
typedef struct GRADIENT_RECT_FID_CACHE {
    
    int cached;
    jclass gradientrectClass;
    jfieldID UpperLeft, LowerRight;

} GRADIENT_RECT_FID_CACHE;

typedef GRADIENT_RECT_FID_CACHE *PGRADIENT_RECT_FID_CACHE;

/* HDITEM struct */
typedef struct HDITEM_FID_CACHE {
    
    int cached;
    jclass hditemClass;
    jfieldID mask, cxy, pszText, hbm, cchTextMax, fmt, \
             lParam, iImage, iOrder;

} HDITEM_FID_CACHE;

typedef HDITEM_FID_CACHE *PHDITEM_FID_CACHE;

/* HDLAYOUT struct */
typedef struct HDLAYOUT_FID_CACHE {
    
    int cached;
    jclass hdlayoutClass;
    jfieldID prc, pwpos;

} HDLAYOUT_FID_CACHE;

typedef HDLAYOUT_FID_CACHE *PHDLAYOUT_FID_CACHE;

/* HELPINFO struct */
typedef struct HELPINFO_FID_CACHE {
    
    int cached;
    jclass helpinfoClass;
    jfieldID cbSize, iContextType, iCtrlId, hItemHandle, dwContextId, x, y;

} HELPINFO_FID_CACHE;

typedef HELPINFO_FID_CACHE *PHELPINFO_FID_CACHE;

/* ICONINFO struct */
typedef struct ICONINFO_FID_CACHE {
    
    int cached;
    jclass iconinfoClass;
    jfieldID fIcon, xHotspot, yHotspot, hbmMask, hbmColor;

} ICONINFO_FID_CACHE;

typedef ICONINFO_FID_CACHE *PICONINFO_FID_CACHE;

/* INITCOMMONCONTROLSEX struct */
typedef struct INITCOMMONCONTROLSEX_FID_CACHE {
    
    int cached;
    jclass initcommoncontrolsexClass;
    jfieldID dwSize, dwICC;

} INITCOMMONCONTROLSEX_FID_CACHE;

typedef INITCOMMONCONTROLSEX_FID_CACHE *PINITCOMMONCONTROLSEX_FID_CACHE;

/* LOGFONT struct */
typedef struct LOGFONT_FID_CACHE {
    
    int cached;
    jclass logfontClass;
    jfieldID lfHeight, lfWidth, lfEscapement, lfOrientation, \
             lfWeight, lfItalic, lfUnderline, lfStrikeOut, \
             lfCharSet, lfOutPrecision, lfClipPrecision, \
             lfQuality, lfPitchAndFamily, \
             lfFaceName0, lfFaceName1, lfFaceName2, lfFaceName3, \
             lfFaceName4, lfFaceName5, lfFaceName6, lfFaceName7, \
             lfFaceName8, lfFaceName9, lfFaceName10, lfFaceName11, \
             lfFaceName12, lfFaceName13, lfFaceName14, lfFaceName15, \
             lfFaceName16, lfFaceName17, lfFaceName18, lfFaceName19, \
             lfFaceName20, lfFaceName21, lfFaceName22, lfFaceName23, \
             lfFaceName24, lfFaceName25, lfFaceName26, lfFaceName27, \
             lfFaceName28, lfFaceName29, lfFaceName30, lfFaceName31;

} LOGFONT_FID_CACHE;

typedef LOGFONT_FID_CACHE *PLOGFONT_FID_CACHE;


/* LOGPEN struct */
typedef struct LOGPEN_FID_CACHE {

    int cached;
    jclass logpenClass;
    jfieldID lopnStyle, x, y, lopnColor;

} LOGPEN_FID_CACHE;

typedef LOGPEN_FID_CACHE *PLOGPEN_FID_CACHE;

/* EXTLOGPEN struct */
/*
typedef struct EXTLOGPEN_FID_CACHE { 

	int cached;
    jclass extlogpenClass;
    jfieldID elpPenStyle, elpWidth, elpBrushStyle, elpColor, \
			 elpHatch, elpNumEntries;
} EXTLOGPEN_FID_CACHE; 

typedef EXTLOGPEN_FID_CACHE *PEXTLOGPEN_FID_CACHE;
*/

/* LVCOLUMN struct */
typedef struct LVCOLUMN_FID_CACHE {
    
    int cached;
    jclass lvcolumnClass;
    jfieldID mask, fmt, cx, pszText, cchTextMax, iSubItem, \
             iImage, iOrder;

} LVCOLUMN_FID_CACHE;

typedef LVCOLUMN_FID_CACHE *PLVCOLUMN_FID_CACHE;

/* LVHITTESTINFO struct */
typedef struct LVHITTESTINFO_FID_CACHE {
    
    int cached;
    jclass lvhittestinfoClass;
    jfieldID x, y, flags, iItem, iSubItem;

} LVHITTESTINFO_FID_CACHE;

typedef LVHITTESTINFO_FID_CACHE *PLVHITTESTINFO_FID_CACHE;

/* LVITEM struct */
typedef struct LVITEM_FID_CACHE {

    int cached;
    jclass lvitemClass;    
    jfieldID mask, iItem, iSubItem, state, \
             stateMask, pszText, cchTextMax, iImage, \
             lParam, iIndent;

} LVITEM_FID_CACHE;

typedef LVITEM_FID_CACHE *PLVITEM_FID_CACHE;

/* LOGBRUSH struct */
typedef struct LOGBRUSH_FID_CACHE {
    
    int cached;
    jclass logbrushClass;
    jfieldID lbStyle, lbColor, lbHatch;

} LOGBRUSH_FID_CACHE;

typedef LOGBRUSH_FID_CACHE *PLOGBRUSH_FID_CACHE;

/* MEASUREITEMSTRUCT struct */
typedef struct MEASUREITEMSTRUCT_FID_CACHE {
    
    int cached;
    jclass measureitemstructClass;
    jfieldID CtlType, CtlID, itemID, itemWidth, itemHeight, itemData;

} MEASUREITEMSTRUCT_FID_CACHE;

typedef MEASUREITEMSTRUCT_FID_CACHE *PMEASUREITEMSTRUCT_FID_CACHE;

/* MENUINFO struct */
typedef struct MENUINFO_FID_CACHE {
    
    int cached;
    jclass menuinfoClass;
    jfieldID cbSize, fMask, dwStyle, cyMax, hbrBack, dwContextHelpID, dwMenuData;

} MENUINFO_FID_CACHE;

typedef MENUINFO_FID_CACHE *PMENUINFO_FID_CACHE;


/*  struct MENUITEMINFO*/
typedef struct MENUITEMINFO_FID_CACHE {
    
    int cached;
    jclass menuiteminfoClass;
    jfieldID cbSize, fMask, fType, fState, wID, hSubMenu, \
             hbmpChecked, hbmpUnchecked, dwItemData, dwTypeData, \
             cch
#ifdef USE_2000_CALLS
             ,hbmpItem       
#endif
              ;

} MENUITEMINFO_FID_CACHE ;

typedef MENUITEMINFO_FID_CACHE *PMENUITEMINFO_FID_CACHE;

/* MSG struct */
typedef struct MSG_FID_CACHE {
    
    int cached;
    jclass msgClass;
    jfieldID hwnd, message, wParam, lParam, time, x, y;

} MSG_FID_CACHE;

typedef MSG_FID_CACHE *PMSG_FID_CACHE;

/* MSGFILTER struct */
typedef struct MSGFILTER_FID_CACHE {
    
    int cached;
    jclass msgfilterClass;
    jfieldID hwndFrom, idFrom, code, msg, wParam, lParam;

} MSGFILTER_FID_CACHE;

typedef MSGFILTER_FID_CACHE *PMSGFILTER_FID_CACHE;

/* NMHDR struct */
typedef struct NMHDR_FID_CACHE {
    
    int cached;
    jclass nmhdrClass;
    jfieldID hwndFrom, idFrom, code;

} NMHDR_FID_CACHE;

typedef NMHDR_FID_CACHE *PNMHDR_FID_CACHE;

/* NMHEADER struct */
typedef struct NMHEADER_FID_CACHE {
    
    int cached;
    jclass nmheaderClass;
    jfieldID hwndFrom, idFrom, code, iItem, iButton, pitem;

} NMHEADER_FID_CACHE;

typedef NMHEADER_FID_CACHE *PNMHEADER_FID_CACHE;

/* NMLISTVIEW struct */
typedef struct NMLISTVIEW_FID_CACHE {

    int cached;
    jclass nmlistviewClass;
    jfieldID hwndFrom, idFrom, code, iItem, iSubItem, uNewState, \
             uOldState, uChanged, x, y, lParam;

} NMLISTVIEW_FID_CACHE;

typedef NMLISTVIEW_FID_CACHE *PNMLISTVIEW_FID_CACHE;

/* NMTOOLBAR struct */
typedef struct NMTOOLBAR_FID_CACHE {

    int cached;
    jclass nmtoolbarClass;
    jfieldID hwndFrom, idFrom, code, iItem, iBitmap, idCommand,
             fsState, fsStyle, ___MISSING_ALIGNMENT__, dwData,
             iString, cchText, pszText, left, top, right, bottom; 

} NMTOOLBAR_FID_CACHE;

typedef NMTOOLBAR_FID_CACHE *PNMTOOLBAR_FID_CACHE;

/* NMTTDISPINFO struct */
typedef struct NMTTDISPINFO_FID_CACHE {
    
    int cached;
    jclass nmttdispinfoClass;
    jfieldID hwndFrom, idFrom, code, lpszText, pad0, pad1, pad2, pad3, \
             pad4, pad5, pad6, pad7, pad8, pad9, pad10, pad11, pad12, \
             pad13, pad14, pad15, pad16, pad17, pad18, pad19, hinst, \
             uFlags, lParam;

} NMTTDISPINFO_FID_CACHE;

typedef NMTTDISPINFO_FID_CACHE *PNMTTDISPINFO_FID_CACHE;

/* NMTVCUSTOMDRAW struct */
typedef struct NMTVCUSTOMDRAW_FID_CACHE {
    
    int cached;
    jclass nmtvcustomdrawClass;
    jfieldID hwndFrom, idFrom, code, dwDrawStage, hdc, left, top, \
             right, bottom, dwItemSpec, uItemState, lItemlParam, \
             clrText, clrTextBk, iLevel;

} NMTVCUSTOMDRAW_FID_CACHE;

typedef NMTVCUSTOMDRAW_FID_CACHE *PNMTVCUSTOMDRAW_FID_CACHE;

/* OPENFILENAME struct */
typedef struct OPENFILENAME_FID_CACHE {
    
    int cached;
    jclass openfilenameClass;
    jfieldID lStructSize, hwndOwner, hInstance, lpstrFilter, \
             lpstrCustomFilter, nMaxCustFilter, nFilterIndex, lpstrFile, \
             nMaxFile, lpstrFileTitle, nMaxFileTitle, lpstrInitialDir, \
             lpstrTitle, Flags, nFileOffset, nFileExtension, lpstrDefExt, \
             lCustData, lpfnHook, lpTemplateName;

} OPENFILENAME_FID_CACHE;

typedef OPENFILENAME_FID_CACHE *POPENFILENAME_FID_CACHE;

/* PAINTSTRUCT struct */
typedef struct PAINTSTRUCT_FID_CACHE {
    
    int cached;
    jclass paintClass;
    jfieldID hdc, fErase, left, top, right, bottom, \
             fRestore, fIncUpdate;

} PAINTSTRUCT_FID_CACHE;

typedef PAINTSTRUCT_FID_CACHE *PPAINTSTRUCT_FID_CACHE;

/* PAGESETUPDLG struct */
typedef struct PAGESETUPDLG_FID_CACHE {
    
    int cached;
    jclass pagesetupdlgClass;
    jfieldID lStructSize, hwndOwner, hDevMode, hDevNames, Flags, \
             ptPaperSize_x, ptPaperSize_y, rtMinMargin_left, rtMinMargin_top, \
             rtMinMargin_right, rtMinMargin_bottom, rtMargin_left, rtMargin_top, \
             rtMargin_right, rtMargin_bottom, hInstance, lCustData, lpfnPageSetupHook, \
             lpfnPagePaintHook, lpPageSetupTemplateName, hPageSetupTemplate;

} PAGESETUPDLG_FID_CACHE;

typedef PAGESETUPDLG_FID_CACHE *PPAGESETUPDLG_FID_CACHE;

/* PARAFORMAT struct */
/*
typedef struct PARAFORMAT_FID_CACHE {
    int cached;
    jclass paraformatClass;
    jfieldID cbSize, dwMask, wNumbering, wEffects, dxStartIndent, \
             dxRightIndent, dxOffset, wAlignment, cTabCount, \
             rgxTabs0, rgxTabs1, rgxTabs2, rgxTabs3, rgxTabs4, \
             rgxTabs5, rgxTabs6, rgxTabs7, rgxTabs8, rgxTabs9, \
             rgxTabs10, rgxTabs11, rgxTabs12, rgxTabs13, rgxTabs14, \
             rgxTabs15, rgxTabs16, rgxTabs17, rgxTabs18, rgxTabs19, \
             rgxTabs20, rgxTabs21, rgxTabs22, rgxTabs23, rgxTabs24, \
             rgxTabs25, rgxTabs26, rgxTabs27, rgxTabs28, rgxTabs29, \
             rgxTabs30, rgxTabs31;
} PARAFORMAT_FID_CACHE;
typedef PARAFORMAT_FID_CACHE *PPARAFORMAT_FID_CACHE;
*/

/* POINT struct */
typedef struct POINT_FID_CACHE {
    
    int cached;
    jclass pointClass;
    jfieldID x, y;

} POINT_FID_CACHE;

typedef POINT_FID_CACHE *PPOINT_FID_CACHE;

/* PRINTDLG struct */
typedef struct PRINTDLG_FID_CACHE {
    
    int cached;
    jclass printdlgClass;
    jfieldID lStructSize, hwndOwner, hDevMode, hDevNames, hDC, Flags, \
             nFromPage, nToPage, nMinPage, nMaxPage, nCopies, hInstance, \
             lCustData, lpfnPrintHook, lpfnSetupHook, lpPrintTemplateName, \
             lpSetupTemplateName, hPrintTemplate, hSetupTemplate;

} PRINTDLG_FID_CACHE;

typedef PRINTDLG_FID_CACHE *PPRINTDLG_FID_CACHE;

/* REBARBANDINFO struct */
typedef struct REBARBANDINFO_FID_CACHE {
    
    int cached;
    jclass rebarbandinfoClass;
    jfieldID cbSize, fMask, fStyle, clrFore, clrBack, lpText, cch, \
             iImage, hwndChild, cxMinChild, cyMinChild, cx, hbmBack, \
             wID, cyChild, cyMaxChild, cyIntegral, cxIdeal, lParam, cxHeader;

} REBARBANDINFO_FID_CACHE;

typedef REBARBANDINFO_FID_CACHE *PREBARBANDINFO_FID_CACHE;

/* RECT struct */
typedef struct RECT_FID_CACHE {
    
    int cached;
    jclass rectClass;
    jfieldID left, top, right, bottom;

} RECT_FID_CACHE;

typedef RECT_FID_CACHE *PRECT_FID_CACHE;

/* SCROLLINFO struct */
typedef struct SCROLLINFO_FID_CACHE {
    
    int cached;
    jclass scrollinfoClass;
    jfieldID cbSize, fMask, nMin, nMax, nPage, nPos, nTrackPos;

} SCROLLINFO_FID_CACHE;

typedef SCROLLINFO_FID_CACHE *PSCROLLINFO_FID_CACHE;

/* SIZE struct */
typedef struct SIZE_FID_CACHE {
    
    int cached;
    jclass sizeClass;
    jfieldID cx, cy;

} SIZE_FID_CACHE;

typedef SIZE_FID_CACHE *PSIZE_FID_CACHE;

/* TBBUTTON struct */
typedef struct TBBUTTON_FID_CACHE {
    
    int cached;
    jclass tbbuttonClass;
    jfieldID iBitmap, idCommand, fsState, fsStyle, dwData, iString;

} TBBUTTON_FID_CACHE;

typedef TBBUTTON_FID_CACHE *PTBBUTTON_FID_CACHE;

/* TBBUTTONINFO struct */
typedef struct TBBUTTONINFO_FID_CACHE {
    
    int cached;
    jclass tbbuttoninfoClass;
    jfieldID cbSize, dwMask, idCommand, iImage, fsState, fsStyle, cx, lParam, pszText, cchText;

} TBBUTTONINFO_FID_CACHE;

typedef TBBUTTONINFO_FID_CACHE *PTBBUTTONINFO_FID_CACHE;

/* TCITEM struct */
typedef struct TCITEM_FID_CACHE {
    
    int cached;
    jclass tcitemClass;
    jfieldID mask, dwState, dwStateMask, pszText, cchTextMax, iImage, lParam;

} TCITEM_FID_CACHE;

typedef TCITEM_FID_CACHE *PTCITEM_FID_CACHE;

/* TEXTMETRIC struct */
typedef struct TEXTMETRIC_FID_CACHE {
    
    int cached;
    jclass textmetricClass;
    jfieldID tmHeight, tmAscent, tmDescent, tmInternalLeading, tmExternalLeading, \
             tmAveCharWidth, tmMaxCharWidth, tmWeight, tmOverhang, tmDigitizedAspectX, \
             tmDigitizedAspectY, tmFirstChar, tmLastChar, tmDefaultChar, tmBreakChar, tmItalic, \
             tmUnderlined, tmStruckOut, tmPitchAndFamily, tmCharSet;

} TEXTMETRIC_FID_CACHE;

typedef TEXTMETRIC_FID_CACHE *PTEXTMETRIC_FID_CACHE;

/* TOOLINFO struct */
typedef struct TOOLINFO_FID_CACHE {
    
    int cached;
    jclass toolinfoClass;
    jfieldID cbSize, uFlags, hwnd, uId, left, top, right, bottom, \
             hinst, lpszText, lParam;

} TOOLINFO_FID_CACHE;

typedef TOOLINFO_FID_CACHE *PTOOLINFO_FID_CACHE;

/* TRACKMOUSEEVENT struct */
typedef struct TRACKMOUSEEVENT_FID_CACHE {

    int cached;
    jclass trackmouseeventClass;
    jfieldID cbSize, dwFlags, hwndTrack, dwHoverTime;

} TRACKMOUSEEVENT_FID_CACHE;

typedef TRACKMOUSEEVENT_FID_CACHE *PTRACKMOUSEEVENT_FID_CACHE;

/* TRIVERTEX struct */
typedef struct TRIVERTEX_FID_CACHE {
    
    int cached;
    jclass trivertexClass;
    jfieldID x, y, Red, Green, Blue, Alpha;

} TRIVERTEX_FID_CACHE;

typedef TRIVERTEX_FID_CACHE *PTRIVERTEX_FID_CACHE;

/* TVHITTESTINFO struct */
typedef struct TVHITTESTINFO_FID_CACHE {
    
    int cached;
    jclass tvhittestinfoClass;
    jfieldID x, y, flags, hItem;

} TVHITTESTINFO_FID_CACHE;

typedef TVHITTESTINFO_FID_CACHE *PTVHITTESTINFO_FID_CACHE;

/* TVINSERTSTRUCT struct */
typedef struct TVINSERTSTRUCT_FID_CACHE {
    
    int cached;
    jclass tvinsertstructClass;
    jfieldID hParent, hInsertAfter, mask, hItem, state, stateMask, pszText, cchTextMax, \
             iImage, iSelectedImage, cChildren, lParam;

} TVINSERTSTRUCT_FID_CACHE;

typedef TVINSERTSTRUCT_FID_CACHE *PTVINSERTSTRUCT_FID_CACHE;

/* TVITEM struct */
typedef struct TVITEM_FID_CACHE {
    
    int cached;
    jclass tvitemClass;
    jfieldID mask, hItem, state, stateMask, pszText, cchTextMax, \
             iImage, iSelectedImage, cChildren, lParam;

} TVITEM_FID_CACHE;

typedef TVITEM_FID_CACHE *PTVITEM_FID_CACHE;

/* WINDOWPLACEMENT struct */
typedef struct WINDOWPLACEMENT_FID_CACHE {
    
    int cached;
    jclass windowplacementClass;
    jfieldID length, flags, showCmd, ptMinPosition_x, ptMinPosition_y, ptMaxPosition_x, ptMaxPosition_y,
        left, top, right, bottom;

} WINDOWPLACEMENT_FID_CACHE;

typedef WINDOWPLACEMENT_FID_CACHE *PWINDOWPLACEMENT_FID_CACHE;

/* WINDOWPOS struct */
typedef struct WINDOWPOS_FID_CACHE {
    
    int cached;
    jclass windowposClass;
    jfieldID hwnd, hwndInsertAfter, x, y, cx, cy, flags;  

} WINDOWPOS_FID_CACHE;

typedef WINDOWPOS_FID_CACHE *PWINDOWPOS_FID_CACHE;

/* WNDCLASSEX struct */
typedef struct WNDCLASSEX_FID_CACHE {
    
    int cached;
    jclass wndclassexClass;
    jfieldID cbSize, style, lpfnWndProc, cbClsExtra, \
             cbWndExtra, hInstance, hIcon, hCursor, \
             hbrBackground, lpszMenuName, lpszClassName, \
             hIconSm;

} WNDCLASSEX_FID_CACHE;

typedef WNDCLASSEX_FID_CACHE *PWNDCLASSEX_FID_CACHE;

/* SHFILEINFO struct */
/*
typedef struct SHFILEINFO_FID_CACHE {
    
    int cached;
    jclass shfileinfoClass;
    jfieldID hIcon, iIcon, dwAttributes, szDisplayName, szTypeName;

} SHFILEINFO_FID_CACHE;

typedef SHFILEINFO_FID_CACHE *PSHFILEINFO_FID_CACHE;
*/

/* ----------- ole fid cache structures  ----------- */

/* CAUUID struct */
typedef struct CAUUID_FID_CACHE {
    
    int cached;
    jclass cauuidClass;
    jfieldID cElems, pElems;

} CAUUID_FID_CACHE;

typedef CAUUID_FID_CACHE *PCAUUID_FID_CACHE;

/* COSERVERINFO struct */
typedef struct COSERVERINFO_FID_CACHE {
    
    int cached;
    jclass coserverinfoClass;
    jfieldID dwReserved1, pwszName, pAuthInfo, dwReserved2;

} COSERVERINFO_FID_CACHE;

typedef COSERVERINFO_FID_CACHE *PCOSERVERINFO_FID_CACHE;

/* CONTROLINFO struct */
typedef struct CONTROLINFO_FID_CACHE {
    
    int cached;
    jclass controlinfoClass;
    jfieldID cb, hAccel, cAccel, filler, dwFlags;

} CONTROLINFO_FID_CACHE;

typedef CONTROLINFO_FID_CACHE *PCONTROLINFO_FID_CACHE;

/* DISPPARAMS struct */
typedef struct DISPPARAMS_FID_CACHE {
    
    int cached;
    jclass dispparamsClass;
    jfieldID rgvarg, rgdispidNamedArgs, cArgs, cNamedArgs;

} DISPPARAMS_FID_CACHE;

typedef DISPPARAMS_FID_CACHE *PDISPPARAMS_FID_CACHE;

/* DROPFILES struct */
typedef struct DROPFILES_FID_CACHE {
    
    int cached;
    jclass dropfilesClass;
    jfieldID pFiles, pt_x, pt_y, fNC, fWide;

} DROPFILES_FID_CACHE;

typedef DROPFILES_FID_CACHE *PDROPFILES_FID_CACHE;

/* DVASPECTINFO struct */
typedef struct DVASPECTINFO_FID_CACHE {
    
    int cached;
    jclass dvaspectinfoClass;
    jfieldID cb, dwFlags;

} DVASPECTINFO_FID_CACHE;

typedef DVASPECTINFO_FID_CACHE *PDVASPECTINFO_FID_CACHE;

/* DVTARGETDEVICE struct */
typedef struct DVTARGETDEVICE_FID_CACHE {
    
    int cached;
    jclass dvtargetdeviceClass;
    jfieldID tdSize, tdDriverNameOffset, tdDeviceNameOffset, \
             tdPortNameOffset, tdExtDevmodeOffset, tdData;

} DVTARGETDEVICE_FID_CACHE;

typedef DVTARGETDEVICE_FID_CACHE *PDVTARGETDEVICE_FID_CACHE;

/* EXCEPINFO struct */
typedef struct EXCEPINFO_FID_CACHE {
    
    int cached;
    jclass excepinfoClass;
    jfieldID wCode, wReserved, bstrSource, bstrDescription, bstrHelpFile, \
             dwHelpContext, pvReserved, pfnDeferredFillIn, scode;

} EXCEPINFO_FID_CACHE;

typedef EXCEPINFO_FID_CACHE *PEXCEPINFO_FID_CACHE;

/* FILETIME struct */
typedef struct FILETIME_FID_CACHE {
    
    int cached;
    jclass filetimeClass;
    jfieldID dwLowDateTime, dwHighDateTime;

} FILETIME_FID_CACHE;

typedef FILETIME_FID_CACHE *PFILETIME_FID_CACHE;

/* FORMATETC struct */
typedef struct FORMATEC_FID_CACHE {
    
    int cached;
    jclass formatetcClass;
    jfieldID cfFormat, ptd, dwAspect, lindex, tymed;

} FORMATETC_FID_CACHE;

typedef FORMATETC_FID_CACHE *PFORMATETC_FID_CACHE;

/* GUID struct */
typedef struct GUID_FID_CACHE {
    
    int cached;
    jclass guidClass;
    jfieldID data1, data2, data3, b0, b1, b2, b3, b4, b5, b6, b7;

} GUID_FID_CACHE;

typedef GUID_FID_CACHE *PGUID_FID_CACHE;

/* IDLDESC struct */
typedef struct IDLDESC_FID_CACHE {
    
    int cached;
    jclass idldescClass;
    jfieldID dwReserved, wIDLFlags;

} IDLDESC_FID_CACHE;

typedef IDLDESC_FID_CACHE *PIDLDESC_FID_CACHE;

/* LICINFO struct */
typedef struct LICINFO_FID_CACHE {
    
    int cached;
    jclass licinfoClass;
    jfieldID cbLicInfo, fRuntimeKeyAvail, fLicVerified;

} LICINFO_FID_CACHE;

typedef LICINFO_FID_CACHE *PLICINFO_FID_CACHE;

/* MULTI_QI struct */
typedef struct MULTI_QI_FID_CACHE {
    
    int cached;
    jclass multi_qiClass;
    jfieldID pIID, pItf, hr;

} MULTI_QI_FID_CACHE;

typedef MULTI_QI_FID_CACHE *PMULTI_QI_FID_CACHE;

/* OLECMD struct */
typedef struct OLECMD_FID_CACHE {
    
    int cached;
    jclass olecmdClass;
    jfieldID cmdID, cmdf;

} OLECMD_FID_CACHE;

typedef OLECMD_FID_CACHE *POLECMD_FID_CACHE;

/* OLECMDTEXT struct */
typedef struct OLECMDTEXT_FID_CACHE {
    
    int cached;
    jclass olecmdtextClass;
    jfieldID cmdtextf, cwActual, cwBuf, rgwz;

} OLECMDTEXT_FID_CACHE;

typedef OLECMDTEXT_FID_CACHE *POLECMDTEXT_FID_CACHE;

/* OLEINPLACEFRAMEINFO struct */
typedef struct OLEINPLACEFRAMEINFO_FID_CACHE {
    
    int cached;
    jclass oleinplaceframeinfoClass;
    jfieldID cb, fMDIApp, hwndFrame, haccel, cAccelEntries;

} OLEINPLACEFRAMEINFO_FID_CACHE;

typedef OLEINPLACEFRAMEINFO_FID_CACHE *POLEINPLACEFRAMEINFO_FID_CACHE;

/* OLEVERB struct */
typedef struct OLEVERB_FID_CACHE {
    
    int cached;
    jclass oleverbClass;
    jfieldID lVerb, lpszVerbName, fuFlags, grfAttribs;

} OLEVERB_FID_CACHE;

typedef OLEVERB_FID_CACHE *POLEVERB_FID_CACHE;

/* STATSTG struct */
typedef struct STATSTG_FID_CACHE {
    
    int cached;
    jclass statstgClass;
    jfieldID pwcsName, type, cbSize, mtime_dwLowDateTime, mtime_dwHighDateTime, \
             ctime_dwLowDateTime, ctime_dwHighDateTime, atime_dwLowDateTime, \
             atime_dwHighDateTime, grfMode, grfLocksSupported, clsid_data1, \
             clsid_data2, clsid_data3, clsid_b0, clsid_b1, clsid_b2, clsid_b3, \
             clsid_b4, clsid_b5, clsid_b6, clsid_b7, grfStateBits, reserved;

} STATSTG_FID_CACHE;

typedef STATSTG_FID_CACHE *PSTATSTG_FID_CACHE;

/* STGMEDIUM struct */
typedef struct STGMEDIUM_FID_CACHE {
    
    int cached;
    jclass stgmediumClass;
    jfieldID tymed, unionField, pUnkForRelease;

} STGMEDIUM_FID_CACHE;

typedef STGMEDIUM_FID_CACHE *PSTGMEDIUM_FID_CACHE;

/* TYPEATTR struct */
typedef struct TYPEATTR_FID_CACHE {
    
    int cached;
    jclass typeattrClass;
    jfieldID guid_data1, guid_data2, guid_data3, \
             guid_b0, guid_b1, guid_b2, guid_b3, guid_b4, guid_b5, guid_b6, guid_b7, \
             lcid, dwReserved, memidConstructor, memidDestructor, lpstrSchema, \
             cbSizeInstance, typekind, cFuncs, cVars, cImplTypes, cbSizeVft, \
             cbAlignment, wTypeFlags, wMajorVerNum, wMinorVerNum, \
             tdescAlias_unionField, tdescAlias_vt, \
             idldescType_dwReserved, idldescType_wIDLFlags;

} TYPEATTR_FID_CACHE;

typedef TYPEATTR_FID_CACHE *PTYPEATTR_FID_CACHE;

/* TYPEDESC struct */
typedef struct TYPEDESC_FID_CACHE {
    
    int cached;
    jclass typedescClass;
    jfieldID typedesc_union, vt;

} TYPEDESC_FID_CACHE;

typedef TYPEDESC_FID_CACHE *PTYPEDESC_FID_CACHE;

/* FUNCDESC1 struct */
typedef struct FUNCDESC1_FID_CACHE {
    
    int cached;
    jclass funcdescClass;
    jfieldID memid, lprgscode, lprgelemdescParam, funckind, invkind, \
	         callconv, cParams, cParamsOpt, oVft, cScodes, \
             elemdescFunc_tdesc_union, elemdescFunc_tdesc_vt, elemdescFunc_tdesc_filler, \
             elemdescFunc_paramdesc_pparamdescex, elemdescFunc_paramdesc_wParamFlags, elemdescFunc_paramdesc_filler, \
             wFuncFlags;

} FUNCDESC1_FID_CACHE;

typedef FUNCDESC1_FID_CACHE *PFUNCDESC1_FID_CACHE;

/* FUNCDESC2 struct */
typedef struct FUNCDESC2_FID_CACHE {
    
    int cached;
    jclass funcdescClass;
    jfieldID memid, lprgscode, lprgelemdescParam, funckind, invkind, \
	         callconv, cParams, cParamsOpt, oVft, cScodes, \
             elemdescFunc_tdesc_union, elemdescFunc_tdesc_vt, elemdescFunc_tdesc_filler, \
             elemdescFunc_idldesc_dwReserved, elemdescFunc_idldesc_wIDLFlags, elemdescFunc_idldesc_filler, \
             wFuncFlags;

} FUNCDESC2_FID_CACHE;

typedef FUNCDESC2_FID_CACHE *PFUNCDESC2_FID_CACHE;


/* VARDESC1 struct */
typedef struct VARDESC1_FID_CACHE {
    
    int cached;
    jclass vardescClass;
    jfieldID memid, lpstrSchema, unionField, \
             elemdescVar_tdesc_union, elemdescVar_tdesc_vt, elemdescVar_tdesc_filler, \
             elemdescVar_paramdesc_pparamdescex, elemdescVar_paramdesc_wParamFlags, elemdescVar_paramdesc_filler, \
             wVarFlags, filler, varkind;

} VARDESC1_FID_CACHE;

typedef VARDESC1_FID_CACHE *PVARDESC1_FID_CACHE;

/* VARDESC2 struct */
typedef struct VARDESC2_FID_CACHE {
    
    int cached;
    jclass vardescClass;
    jfieldID memid, lpstrSchema, unionField, \
             elemdescVar_tdesc_union, elemdescVar_tdesc_vt, elemdescVar_tdesc_filler, \
             elemdescVar_idldesc_dwReserved, elemdescVar_idldesc_wIDLFlags, elemdescVar_idldesc_filler, \
             wVarFlags, filler, varkind;

} VARDESC2_FID_CACHE;

typedef VARDESC2_FID_CACHE *PVARDESC2_FID_CACHE;

/* GCP_RESULTS struct */
typedef struct GCP_RESULTS_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID nMaxFit, nGlyphs, lpGlyphs, lpClass, lpCaretPos, lpDx, lpOrder, lpOutString, lStructSize;
} GCP_RESULTS_FID_CACHE;

typedef GCP_RESULTS_FID_CACHE *PGCP_RESULTS_FID_CACHE;


/* ----------- ole cache function prototypes  ----------- */

void cacheGuidFids(JNIEnv *env, jobject lpGuid, PGUID_FID_CACHE lpCache);
void cacheOlecmdFids(JNIEnv *env, jobject lpOlecmd, POLECMD_FID_CACHE lpCache);
void cacheDvtargetdeviceFids(JNIEnv *env, jobject lpDvtargetdevice, PDVTARGETDEVICE_FID_CACHE lpCache);
void cacheOleinplaceframeinfoFids(JNIEnv *env, jobject lpOleinplaceframeinfo, POLEINPLACEFRAMEINFO_FID_CACHE lpCache);
void cacheFormatetcFids(JNIEnv *env, jobject lpFormatetc, PFORMATETC_FID_CACHE lpCache);
void cacheOleverbFids(JNIEnv *env, jobject lpOleverb, POLEVERB_FID_CACHE lpCache);
void cacheTypedescFids(JNIEnv *env, jobject lpTypedesc, PTYPEDESC_FID_CACHE lpCache);
void cacheIdldescFids(JNIEnv *env, jobject lpIdldesc, PIDLDESC_FID_CACHE lpCache);
void cacheOlecmdtextFids(JNIEnv *env, jobject lpOlecmdtext, POLECMDTEXT_FID_CACHE lpCache);
void cacheFiletimeFids(JNIEnv *env, jobject lpFiletime, PFILETIME_FID_CACHE lpCache);
void cacheControlinfoFids(JNIEnv *env, jobject lpControlinfo, PCONTROLINFO_FID_CACHE lpCache);
void cacheStgmediumFids(JNIEnv *env, jobject lpStgmedium, PSTGMEDIUM_FID_CACHE lpCache);
void cacheDvaspectinfoFids(JNIEnv *env, jobject lpDvaspectinfo, PDVASPECTINFO_FID_CACHE lpCache);
void cacheDispparamsFids(JNIEnv *env, jobject lpDispparams, PDISPPARAMS_FID_CACHE lpCache);
void cacheLicinfoFids(JNIEnv *env, jobject lpLicinfo, PLICINFO_FID_CACHE lpCache);
void cacheExcepinfoFids(JNIEnv *env, jobject lpExcepinfo, PEXCEPINFO_FID_CACHE lpCache);
void cacheTypeattrFids(JNIEnv *env, jobject lpTypeattr, PTYPEATTR_FID_CACHE lpCache);
void cacheFuncdesc1Fids(JNIEnv *env, jobject lpFuncdesc, PFUNCDESC1_FID_CACHE lpCache);
void cacheFuncdesc2Fids(JNIEnv *env, jobject lpFuncdesc, PFUNCDESC2_FID_CACHE lpCache);
void cacheVardesc1Fids(JNIEnv *env, jobject lpVardesc, PVARDESC1_FID_CACHE lpCache);
void cacheVardesc2Fids(JNIEnv *env, jobject lpVardesc, PVARDESC2_FID_CACHE lpCache);
void cacheGCP_RESULTSFids(JNIEnv *env, jobject lpObject, PGCP_RESULTS_FID_CACHE lpCache);

/* ----------- cache function prototypes  ----------- */

void cacheAccelFids(JNIEnv *env, jobject lpAccel, PACCEL_FID_CACHE lpCache);
void cacheBitmapFids(JNIEnv *env, jobject lpBitmap, PBITMAP_FID_CACHE lpCache);
void cacheBrowseinfoFids(JNIEnv *env, jobject lpBrowseinfo, PBROWSEINFO_FID_CACHE lpCache);
void cacheCauuidFids(JNIEnv *env, jobject lpCauuid, PCAUUID_FID_CACHE lpCache);
/*
void cacheCharformatFids(JNIEnv *env, jobject lpCharformat, PCHARFORMAT_FID_CACHE lpCache);
void cacheCharformat2Fids(JNIEnv *env, jobject lpCharformat2, PCHARFORMAT2_FID_CACHE lpCache);
*/
void cacheChoosecolorFids(JNIEnv *env, jobject lpChoosecolor, PCHOOSECOLOR_FID_CACHE lpCache);
void cacheChoosefontFids(JNIEnv *env, jobject lpChoosefont, PCHOOSEFONT_FID_CACHE lpCache);
void cacheCompositionformFids(JNIEnv *env, jobject lpCompositionform, PCOMPOSITIONFORM_FID_CACHE lpCache);
void cacheCoserverinfoFids(JNIEnv *env, jobject lpCoservinfo, PCOSERVERINFO_FID_CACHE lpCache);
void cacheCreatestructFids(JNIEnv *env, jobject lpCreatestruct, PCREATESTRUCT_FID_CACHE lpCache);
void cacheDibsectionFids(JNIEnv *env, jobject lpDibsection, PDIBSECTION_FID_CACHE lpCache);
void cacheDllversioninfoFids(JNIEnv *env, jobject lpDllversioninfo, PDLLVERSIONINFO_FID_CACHE lpCache);
void cacheDocinfoFids(JNIEnv *env, jobject lpDocinfo, PDOCINFO_FID_CACHE lpCache);
void cacheDrawitemstructFids(JNIEnv *env, jobject lpDrawitemstruct, PDRAWITEMSTRUCT_FID_CACHE lpCache);
void cacheDropfilesFids(JNIEnv *env, jobject lpDropfiles, PDROPFILES_FID_CACHE lpCache);
void cacheGradientrectFids(JNIEnv *env, jobject lpGradientrect, PGRADIENT_RECT_FID_CACHE lpCache);
void cacheHditemFids(JNIEnv *env, jobject lpHditem, PHDITEM_FID_CACHE lpCache);
void cacheHdlayoutFids(JNIEnv *env, jobject lpHdlayout, PHDLAYOUT_FID_CACHE lpCache);
void cacheHelpinfoFids(JNIEnv *env, jobject lpHelpinfo, PHELPINFO_FID_CACHE lpCache);
void cacheIconinfoFids(JNIEnv *env, jobject lpIconinfo, PICONINFO_FID_CACHE lpCache);
void cacheInitcommoncontrolsexFids(JNIEnv *env, jobject lpInitcommoncontrolsex, PINITCOMMONCONTROLSEX_FID_CACHE lpCache);
void cacheLogbrushFids(JNIEnv *env, jobject lpLogbrush, PLOGBRUSH_FID_CACHE lpCache);
void cacheLogfontFids(JNIEnv *env, jobject lpLogfont, PLOGFONT_FID_CACHE lpCache);
void cacheLogpenFids(JNIEnv *env, jobject lpLogpen, PLOGPEN_FID_CACHE lpCache);
/*
void cacheExtlogpenFids(JNIEnv *env, jobject lpExtlogpen, PEXTLOGPEN_FID_CACHE lpCache);
*/
void cacheLvcolumnFids(JNIEnv *env, jobject lpLVColumn, PLVCOLUMN_FID_CACHE lpCache);
void cacheLvhittestinfoFids(JNIEnv *env, jobject lpLvhittestinfo, PLVHITTESTINFO_FID_CACHE lpCache);
void cacheLvitemFids(JNIEnv *env, jobject lpLVItem, PLVITEM_FID_CACHE lpCache);
void cacheMenuinfoFids(JNIEnv *env, jobject lpMenuinfo, PMENUINFO_FID_CACHE lpCache);
void cacheMeasureitemstructFids(JNIEnv *env, jobject lpMeasureitemstruct, PMEASUREITEMSTRUCT_FID_CACHE lpCache);
void cacheMenuiteminfoFids(JNIEnv *env, jobject lpMenuiteminfo, PMENUITEMINFO_FID_CACHE lpCache);
void cacheMsgFids(JNIEnv *env, jobject lpMsg, PMSG_FID_CACHE lpCache);
void cacheMsgfilterFids(JNIEnv *env, jobject lpMsgfilter, PMSGFILTER_FID_CACHE lpCache);
void cacheMulti_qiFids(JNIEnv *env, jobject lpMulti_qi, PMULTI_QI_FID_CACHE lpCache);
void cacheNmhdrFids(JNIEnv *env, jobject lpNmhdr, PNMHDR_FID_CACHE lpCache);
void cacheNmheaderFids(JNIEnv *env, jobject lpNmheader, PNMHEADER_FID_CACHE lpCache);
void cacheNmlistviewFids(JNIEnv *env, jobject lpNmlistview, PNMLISTVIEW_FID_CACHE lpCache);
void cacheNmtoolbarFids(JNIEnv *env, jobject lpNmtoolbar, PNMTOOLBAR_FID_CACHE lpCache);
void cacheNmttdispinfoFids(JNIEnv *env, jobject lpNmttdispinfo, PNMTTDISPINFO_FID_CACHE lpCache);
void cacheNmtvcustomdrawFids(JNIEnv *env, jobject lpNmtvcustomdraw, PNMTVCUSTOMDRAW_FID_CACHE lpCache);
void cacheOpenfilenameFids(JNIEnv *env, jobject lpOpenfilename, POPENFILENAME_FID_CACHE lpCache);
void cachePagesetupdlgFids(JNIEnv *env, jobject lpPagesetupdlg, PPAGESETUPDLG_FID_CACHE lpCache);
void cachePaintstructFids(JNIEnv *env, jobject lpPaint, PPAINTSTRUCT_FID_CACHE lpCache);
/*
void cacheParaformatFids(JNIEnv *env, jobject lpParaformat, PPARAFORMAT_FID_CACHE lpCache);
*/
void cachePointFids(JNIEnv *env, jobject lpPoint, PPOINT_FID_CACHE lpCache);
void cachePrintdlgFids(JNIEnv *env, jobject lpPrintdlg, PPRINTDLG_FID_CACHE lpCache);
void cacheRebarbandinfoFids(JNIEnv *env, jobject lpRebarbandinfo, PREBARBANDINFO_FID_CACHE lpCache);
void cacheRectFids(JNIEnv *env, jobject lpRect, PRECT_FID_CACHE lpCache);
void cacheScrollinfoFids(JNIEnv *env, jobject lpScrollinfo, PSCROLLINFO_FID_CACHE lpCache);
void cacheSizeFids(JNIEnv *env, jobject lpSize, PSIZE_FID_CACHE lpCache);
void cacheStatstgFids(JNIEnv *env, jobject lpStatstg, PSTATSTG_FID_CACHE lpCache);
void cacheTbbuttonFids(JNIEnv *env, jobject lpTbbutton, PTBBUTTON_FID_CACHE lpCache);
void cacheTbbuttoninfoFids(JNIEnv *env, jobject lpTbbuttoninfo, PTBBUTTONINFO_FID_CACHE lpCache);
void cacheTcitemFids(JNIEnv *env, jobject lpTcitem, PTCITEM_FID_CACHE lpCache);
void cacheTextmetricFids(JNIEnv *env, jobject lpTextmetric, PTEXTMETRIC_FID_CACHE lpCache);
void cacheToolinfoFids(JNIEnv *env, jobject lpToolinfo, PTOOLINFO_FID_CACHE lpCache);
void cacheTrackmouseeventFids(JNIEnv *env, jobject lpTrackmouseevent, PTRACKMOUSEEVENT_FID_CACHE lpCache);
void cacheTrivertexFids(JNIEnv *env, jobject lpTrivertex, PTRIVERTEX_FID_CACHE lpCache);
void cacheTvhittestinfoFids(JNIEnv *env, jobject lpTvhittestinfo, PTVHITTESTINFO_FID_CACHE lpCache);
void cacheTvinsertstructFids(JNIEnv *env, jobject lpTvinsertstruct, PTVINSERTSTRUCT_FID_CACHE lpCache);
void cacheTvitemFids(JNIEnv *env, jobject lpTvitem, PTVITEM_FID_CACHE lpCache);
void cacheWindowplacementFids(JNIEnv *env, jobject lpWindowplacement, PWINDOWPLACEMENT_FID_CACHE lpCache);
void cacheWindowposFids(JNIEnv *env, jobject lpWindowpos, PWINDOWPOS_FID_CACHE lpCache);
void cacheWndclassexFids(JNIEnv *env, jobject lpWndclassex, PWNDCLASSEX_FID_CACHE lpCache);
/*
*void cacheShfileinfoFids(JNIEnv *env, jobject lpShfileinfo, PSHFILEINFO_FID_CACHE lpCache);
*/

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

/* ----------- swt getter and setter prototypes  ----------- */
/**
 * These functions get or set object field ids assuming that the
 * fids for these objects have already been cached.
 *
 * The header file just contains function prototypes
 */
void getAccelFields(JNIEnv *env, jobject lpObject, ACCEL *lpAccel, PACCEL_FID_CACHE lpAccelFc);
void setAccelFields(JNIEnv *env, jobject lpObject, ACCEL *lpAccel, PACCEL_FID_CACHE lpAccelFc);
void getBitmapFields(JNIEnv *env, jobject lpObject, BITMAP *lpBitmap, PBITMAP_FID_CACHE lpBitmapFc);
void setBitmapFields(JNIEnv *env, jobject lpObject, BITMAP *lpBitmap, PBITMAP_FID_CACHE lpBitmapFc);
void getBrowseinfoFields(JNIEnv *env, jobject lpObject, BROWSEINFO *lpBrowseinfo, PBROWSEINFO_FID_CACHE lpBrowseinfoFc);
void setBrowseinfoFields(JNIEnv *env, jobject lpObject, BROWSEINFO *lpBrowseinfo, PBROWSEINFO_FID_CACHE lpBrowseinfoFc);
void getCauuidFields(JNIEnv *env, jobject lpObject, CAUUID *lpCauuid, CAUUID_FID_CACHE *lpCauuidFc);
void setCauuidFields(JNIEnv *env, jobject lpObject, CAUUID *lpCauuid, CAUUID_FID_CACHE *lpCauuidFc);
/*
void getCharformatFields(JNIEnv *env, jobject lpObject, CHARFORMAT *lpCharformat, CHARFORMAT_FID_CACHE *lpCharformatFc);
void setCharformatFields(JNIEnv *env, jobject lpObject, CHARFORMAT *lpCharformat, CHARFORMAT_FID_CACHE *lpCharformatFc);
void getCharformat2Fields(JNIEnv *env, jobject lpObject, CHARFORMAT2 *lpCharformat2, CHARFORMAT2_FID_CACHE *lpCharformat2Fc);
void setCharformat2Fields(JNIEnv *env, jobject lpObject, CHARFORMAT2 *lpCharformat2, CHARFORMAT2_FID_CACHE *lpCharformat2Fc);
*/
void getChoosecolorFields(JNIEnv *env, jobject lpObject, CHOOSECOLOR *lpChoosecolor, CHOOSECOLOR_FID_CACHE *lpChoosecolorFc);
void setChoosecolorFields(JNIEnv *env, jobject lpObject, CHOOSECOLOR *lpChoosecolor, CHOOSECOLOR_FID_CACHE *lpChoosecolorFc);
void getChoosefontFields(JNIEnv *env, jobject lpObject, CHOOSEFONT *lpChoosefont, CHOOSEFONT_FID_CACHE *lpChoosefontFc);
void setChoosefontFields(JNIEnv *env, jobject lpObject, CHOOSEFONT *lpChoosefont, CHOOSEFONT_FID_CACHE *lpChoosefontFc);
void getCompositionformFields(JNIEnv *env, jobject lpObject, COMPOSITIONFORM *lpCompositionform, COMPOSITIONFORM_FID_CACHE *lpCompositionformFc);
void setCompositionformFields(JNIEnv *env, jobject lpObject, COMPOSITIONFORM *lpCompositionform, COMPOSITIONFORM_FID_CACHE *lpCompositionformFc);
void getCreatestructFields(JNIEnv *env, jobject lpObject, CREATESTRUCT *lpCreatestruct, PCREATESTRUCT_FID_CACHE lpCreatestructFc);
void setCreatestructFields(JNIEnv *env, jobject lpObject, CREATESTRUCT *lpCreatestruct, PCREATESTRUCT_FID_CACHE lpCreatestructFc);
void getDibsectionFields(JNIEnv *env, jobject lpObject, DIBSECTION *lpDibsection, DIBSECTION_FID_CACHE *lpDibsectionFc);
void setDibsectionFields(JNIEnv *env, jobject lpObject, DIBSECTION *lpDibsection, DIBSECTION_FID_CACHE *lpDibsectionFc);
void getDllversioninfoFields(JNIEnv *env, jobject lpObject, DLLVERSIONINFO *lpDllversioninfo, DLLVERSIONINFO_FID_CACHE *lpDllversioninfoFc);
void setDllversioninfoFields(JNIEnv *env, jobject lpObject, DLLVERSIONINFO *lpDllversioninfo, DLLVERSIONINFO_FID_CACHE *lpDllversioninfoFc);
void getDocinfoFields(JNIEnv *env, jobject lpObject, DOCINFO *lpDocinfo, PDOCINFO_FID_CACHE lpDocinfoFc);
void setDocinfoFields(JNIEnv *env, jobject lpObject, DOCINFO *lpDocinfo, PDOCINFO_FID_CACHE lpDocinfoFc);
void getDrawitemstructFields(JNIEnv *env, jobject lpObject, DRAWITEMSTRUCT *lpDrawitemstruct, PDRAWITEMSTRUCT_FID_CACHE lpDrawitemstructFc);
void setDrawitemstructFields(JNIEnv *env, jobject lpObject, DRAWITEMSTRUCT *lpDrawitemstruct, PDRAWITEMSTRUCT_FID_CACHE lpDrawitemstructFc);
void getGradientrectFields(JNIEnv *env, jobject lpObject, GRADIENT_RECT *lpGradientrect, GRADIENT_RECT_FID_CACHE *lpGradientrectFc);
void setGradientrectFields(JNIEnv *env, jobject lpObject, GRADIENT_RECT *lpGradientrect, GRADIENT_RECT_FID_CACHE *lpGradientrectFc);
void getHditemFields(JNIEnv *env, jobject lpObject, HDITEM *lpHditem, HDITEM_FID_CACHE *lpHditemFc);
void setHditemFields(JNIEnv *env, jobject lpObject, HDITEM *lpHditem, HDITEM_FID_CACHE *lpHditemFc);
void getHdlayoutFields(JNIEnv *env, jobject lpObject, HDLAYOUT *lpHdlayout, HDLAYOUT_FID_CACHE *lpHdlayoutFc);
void setHdlayoutFields(JNIEnv *env, jobject lpObject, HDLAYOUT *lpHdlayout, HDLAYOUT_FID_CACHE *lpHdlayoutFc);
void getHelpinfoFields(JNIEnv *env, jobject lpObject, HELPINFO *lpHelpinfo, HELPINFO_FID_CACHE *lpHelpinfoFc);
void setHelpinfoFields(JNIEnv *env, jobject lpObject, HELPINFO *lpHelpinfo, HELPINFO_FID_CACHE *lpHelpinfoFc);
void getIconinfoFields(JNIEnv *env, jobject lpObject, ICONINFO *lpIconinfo, PICONINFO_FID_CACHE lpIconinfoFc);
void setIconinfoFields(JNIEnv *env, jobject lpObject, ICONINFO *lpIconinfo, PICONINFO_FID_CACHE lpIconinfoFc);
void getInitcommoncontrolsexFields(JNIEnv *env, jobject lpObject, INITCOMMONCONTROLSEX *lpInitcommoncontrolsex, INITCOMMONCONTROLSEX_FID_CACHE *lpInitcommoncontrolsexFc);
void setInitcommoncontrolsexFields(JNIEnv *env, jobject lpObject, INITCOMMONCONTROLSEX *lpInitcommoncontrolsex, INITCOMMONCONTROLSEX_FID_CACHE *lpInitcommoncontrolsexFc);
void getLogbrushFields(JNIEnv *env, jobject lpObject, LOGBRUSH *lpLogbrush, LOGBRUSH_FID_CACHE *lpLogbrushFc);
void setLogbrushFields(JNIEnv *env, jobject lpObject, LOGBRUSH *lpLogbrush, LOGBRUSH_FID_CACHE *lpLogbrushFc);
void getLogfontFields(JNIEnv *env, jobject lpObject, LOGFONT *lpLogfont, PLOGFONT_FID_CACHE lpLogfontFc);
void setLogfontFields(JNIEnv *env, jobject lpObject, LOGFONT *lpLogfont, PLOGFONT_FID_CACHE lpLogfontFc);
void getLogpenFields(JNIEnv *env, jobject lpObject, LOGPEN *lpLogpen, PLOGPEN_FID_CACHE lpLogpenFc);
void setLogpenFields(JNIEnv *env, jobject lpObject, LOGPEN *lpLogpen, PLOGPEN_FID_CACHE lpLogpenFc);
/*
void getExtlogpenFields(JNIEnv *env, jobject lpObject, EXTLOGPEN *lpExtlogpen, PEXTLOGPEN_FID_CACHE lpExtlogpenFc);
void setExtlogpenFields(JNIEnv *env, jobject lpObject, EXTLOGPEN *lpExtlogpen, PEXTLOGPEN_FID_CACHE lpExtlogpenFc);
*/
void getLvcolumnFields(JNIEnv *env, jobject lpObject, LVCOLUMN *lpLvcolumn, PLVCOLUMN_FID_CACHE lpLvcolumnFc);
void setLvcolumnFields(JNIEnv *env, jobject lpObject, LVCOLUMN *lpLvcolumn, PLVCOLUMN_FID_CACHE lpLvcolumnFc);
void getLvhittestinfoFields(JNIEnv *env, jobject lpObject, LVHITTESTINFO *lpLvhittestinfo, PLVHITTESTINFO_FID_CACHE lpLvhittestinfoFc);
void setLvhittestinfoFields(JNIEnv *env, jobject lpObject, LVHITTESTINFO *lpLvhittestinfo, PLVHITTESTINFO_FID_CACHE lpLvhittestinfoFc);
void getLvitemFields(JNIEnv *env, jobject lpObject, LVITEM *lpLvitem, LVITEM_FID_CACHE *lpLvitemFc);
void setLvitemFields(JNIEnv *env, jobject lpObject, LVITEM *lpLvitem, LVITEM_FID_CACHE *lpLvitemFc);
void getMeasureitemstructFields(JNIEnv *env, jobject lpObject, MEASUREITEMSTRUCT *lpMeasureitemstruct, MEASUREITEMSTRUCT_FID_CACHE *lpMeasureitemstructFc);
void setMeasureitemstructFields(JNIEnv *env, jobject lpObject, MEASUREITEMSTRUCT *lpMeasureitemstruct, MEASUREITEMSTRUCT_FID_CACHE *lpMeasureitemstructFc);
#ifdef USE_2000_CALLS
void getMenuinfoFields(JNIEnv *env, jobject lpObject, MENUINFO *lpMenuinfo, MENUINFO_FID_CACHE *lpMenuinfoFc);
void setMenuinfoFields(JNIEnv *env, jobject lpObject, MENUINFO *lpMenuinfo, MENUINFO_FID_CACHE *lpMenuinfoFc);
#endif
void getMenuiteminfoFields(JNIEnv *env, jobject lpObject, MENUITEMINFO *lpMenuiteminfo, MENUITEMINFO_FID_CACHE *lpMenuiteminfoFc);
void setMenuiteminfoFields(JNIEnv *env, jobject lpObject, MENUITEMINFO *lpMenuiteminfo, MENUITEMINFO_FID_CACHE *lpMenuiteminfoFc);
void getMsgFields(JNIEnv *env, jobject lpObject, MSG *lpMsg, MSG_FID_CACHE *lpMsgFc);
void setMsgFields(JNIEnv *env, jobject lpObject, MSG *lpMsg, MSG_FID_CACHE *lpMsgFc);
void getMsgfilterFields(JNIEnv *env, jobject lpObject, MSGFILTER *lpMsgfilter, MSGFILTER_FID_CACHE *lpMsgfilterFc);
void setMsgfilterFields(JNIEnv *env, jobject lpObject, MSGFILTER *lpMsgfilter, MSGFILTER_FID_CACHE *lpMsgfilterFc);
void getNmhdrFields(JNIEnv *env, jobject lpObject, NMHDR *lpNmhdr, NMHDR_FID_CACHE *lpNmhdrFc);
void setNmhdrFields(JNIEnv *env, jobject lpObject, NMHDR *lpNmhdr, NMHDR_FID_CACHE *lpNmhdrFc);
void getNmheaderFields(JNIEnv *env, jobject lpObject, NMHEADER *lpNmheader, NMHEADER_FID_CACHE *lpNmheaderFc);
void setNmheaderFields(JNIEnv *env, jobject lpObject, NMHEADER *lpNmheader, NMHEADER_FID_CACHE *lpNmheaderFc);
void getNmlistviewFields(JNIEnv *env, jobject lpObject, NMLISTVIEW *lpNmlistview, NMLISTVIEW_FID_CACHE *lpNmlistviewFc);
void setNmlistviewFields(JNIEnv *env, jobject lpObject, NMLISTVIEW *lpNmlistview, NMLISTVIEW_FID_CACHE *lpNmlistviewFc);
void getNmtoolbarFields(JNIEnv *env, jobject lpObject, NMTOOLBAR *lpNmtoolbar, NMTOOLBAR_FID_CACHE *lpNmtoolbarFc);
void setNmtoolbarFields(JNIEnv *env, jobject lpObject, NMTOOLBAR *lpNmtoolbar, NMTOOLBAR_FID_CACHE *lpNmtoolbarFc);
void getNmttdispinfoFields(JNIEnv *env, jobject lpObject, NMTTDISPINFO *lpNmttdispinfo, NMTTDISPINFO_FID_CACHE *lpNmttdispinfoFc);
void setNmttdispinfoFields(JNIEnv *env, jobject lpObject, NMTTDISPINFO *lpNmttdispinfo, NMTTDISPINFO_FID_CACHE *lpNmttdispinfoFc);
void getNmtvcustomdrawFields(JNIEnv *env, jobject lpObject, NMTVCUSTOMDRAW *lpNmtvcustomdraw, NMTVCUSTOMDRAW_FID_CACHE *lpNmtvcustomdrawFc);
void setNmtvcustomdrawFields(JNIEnv *env, jobject lpObject, NMTVCUSTOMDRAW *lpNmtvcustomdraw, NMTVCUSTOMDRAW_FID_CACHE *lpNmtvcustomdrawFc);
void getOpenfilenameFields(JNIEnv *env, jobject lpObject, OPENFILENAME *lpOpenfilename, OPENFILENAME_FID_CACHE *lpOpenfilenameFc);
void setOpenfilenameFields(JNIEnv *env, jobject lpObject, OPENFILENAME *lpOpenfilename, OPENFILENAME_FID_CACHE *lpOpenfilenameFc);
void getPagesetupdlgFields(JNIEnv *env, jobject lpObject, PAGESETUPDLG *lpPagesetupdlg, PAGESETUPDLG_FID_CACHE *lpPagesetupdlgFc);
void setPagesetupdlgFields(JNIEnv *env, jobject lpObject, PAGESETUPDLG *lpPagesetupdlg, PAGESETUPDLG_FID_CACHE *lpPagesetupdlgFc);
void getPaintstructFields(JNIEnv *env, jobject lpObject, PAINTSTRUCT *lpPaint, PAINTSTRUCT_FID_CACHE *lpPaintFc);
void setPaintstructFields(JNIEnv *env, jobject lpObject, PAINTSTRUCT *lpPaint, PAINTSTRUCT_FID_CACHE *lpPaintFc);
/*
void getParaformatFields(JNIEnv *env, jobject lpObject, PARAFORMAT *lpParaformat, PARAFORMAT_FID_CACHE *lpParaformatFc);
void setParaformatFields(JNIEnv *env, jobject lpObject, PARAFORMAT *lpParaformat, PARAFORMAT_FID_CACHE *lpParaformatFc);
*/
void getPointFields(JNIEnv *env, jobject lpObject, POINT *lpPoint, PPOINT_FID_CACHE lpPointFc);
void setPointFields(JNIEnv *env, jobject lpObject, POINT *lpPoint, PPOINT_FID_CACHE lpPointFc);
void getPrintdlgFields(JNIEnv *env, jobject lpObject, PRINTDLG *lpPrintdlg, PRINTDLG_FID_CACHE *lpPrintdlgFc);
void setPrintdlgFields(JNIEnv *env, jobject lpObject, PRINTDLG *lpPrintdlg, PRINTDLG_FID_CACHE *lpPrintdlgFc);
void getRebarbandinfoFields(JNIEnv *env, jobject lpObject, REBARBANDINFO *lpRebarbandinfo, REBARBANDINFO_FID_CACHE *lpRebarbandinfoFc);
void setRebarbandinfoFields(JNIEnv *env, jobject lpObject, REBARBANDINFO *lpRebarbandinfo, REBARBANDINFO_FID_CACHE *lpRebarbandinfoFc);
void getRectFields(JNIEnv *env, jobject lpObject, RECT *lpRect, PRECT_FID_CACHE lpRectFc);
void setRectFields(JNIEnv *env, jobject lpObject, RECT *lpRect, PRECT_FID_CACHE lpRectFc);
void getScrollinfoFields(JNIEnv *env, jobject lpObject, SCROLLINFO *lpScrollinfo, SCROLLINFO_FID_CACHE *lpScrollinfoFc);
void setScrollinfoFields(JNIEnv *env, jobject lpObject, SCROLLINFO *lpScrollinfo, SCROLLINFO_FID_CACHE *lpScrollinfoFc);
void getSizeFields(JNIEnv *env, jobject lpObject, SIZE *lpSize, SIZE_FID_CACHE *lpSizeFc);
void setSizeFields(JNIEnv *env, jobject lpObject, SIZE *lpSize, SIZE_FID_CACHE *lpSizeFc);
void getTbbuttonFields(JNIEnv *env, jobject lpObject, TBBUTTON *lpTbbutton, TBBUTTON_FID_CACHE *lpTbbuttonFc);
void setTbbuttonFields(JNIEnv *env, jobject lpObject, TBBUTTON *lpTbbutton, TBBUTTON_FID_CACHE *lpTbbuttonFc);
void getTbbuttoninfoFields(JNIEnv *env, jobject lpObject, TBBUTTONINFO *lpTbbuttoninfo, TBBUTTONINFO_FID_CACHE *lpTbbuttoninfoFc);
void setTbbuttoninfoFields(JNIEnv *env, jobject lpObject, TBBUTTONINFO *lpTbbuttoninfo, TBBUTTONINFO_FID_CACHE *lpTbbuttoninfoFc);
void getTcitemFields(JNIEnv *env, jobject lpObject, TCITEM *lpTcitem, TCITEM_FID_CACHE *lpTcitemFc);
void setTcitemFields(JNIEnv *env, jobject lpObject, TCITEM *lpTcitem, TCITEM_FID_CACHE *lpTcitemFc);
void getTextmetricFields(JNIEnv *env, jobject lpObject, TEXTMETRIC *lpTextmetric, TEXTMETRIC_FID_CACHE *lpTextmetricFc);
void setTextmetricFields(JNIEnv *env, jobject lpObject, TEXTMETRIC *lpTextmetric, TEXTMETRIC_FID_CACHE *lpTextmetricFc);
void getToolinfoFields(JNIEnv *env, jobject lpObject, TOOLINFO *lpToolinfo, TOOLINFO_FID_CACHE *lpToolinfoFc);
void setToolinfoFields(JNIEnv *env, jobject lpObject, TOOLINFO *lpToolinfo, TOOLINFO_FID_CACHE *lpToolinfoFc);
void getTrackmouseeventFields(JNIEnv *env, jobject lpObject, TRACKMOUSEEVENT *lpTrackmouseevent, TRACKMOUSEEVENT_FID_CACHE *lpTrackmouseeventFc);
void setTrackmouseeventFields(JNIEnv *env, jobject lpObject, TRACKMOUSEEVENT *lpTrackmouseevent, TRACKMOUSEEVENT_FID_CACHE *lpTrackmouseeventFc);
void getTrivertexFields(JNIEnv *env, jobject lpObject, TRIVERTEX *lpTrivertex, TRIVERTEX_FID_CACHE *lpTrivertexFc);
void setTrivertexFields(JNIEnv *env, jobject lpObject, TRIVERTEX *lpTrivertex, TRIVERTEX_FID_CACHE *lpTrivertexFc);
void getTvhittestinfoFields(JNIEnv *env, jobject lpObject, TVHITTESTINFO *lpTvhittestinfo, TVHITTESTINFO_FID_CACHE *lpTvhittestinfoFc);
void setTvhittestinfoFields(JNIEnv *env, jobject lpObject, TVHITTESTINFO *lpTvhittestinfo, TVHITTESTINFO_FID_CACHE *lpTvhittestinfoFc);
void getTvinsertstructFields(JNIEnv *env, jobject lpObject, TVINSERTSTRUCT *lpTvinsertstruct, TVINSERTSTRUCT_FID_CACHE *lpTvinsertstructFc);
void setTvinsertstructFields(JNIEnv *env, jobject lpObject, TVINSERTSTRUCT *lpTvinsertstruct, TVINSERTSTRUCT_FID_CACHE *lpTvinsertstructFc);
void getTvitemFields(JNIEnv *env, jobject lpObject, TVITEM *lpTvitem, TVITEM_FID_CACHE *lpTvitemFc);
void setTvitemFields(JNIEnv *env, jobject lpObject, TVITEM *lpTvitem, TVITEM_FID_CACHE *lpTvitemFc);
void getWindowplacementFields(JNIEnv *env, jobject lpObject, WINDOWPLACEMENT *lpWindowplacement, WINDOWPLACEMENT_FID_CACHE *lpWindowplacementFc);
void setWindowplacementFields(JNIEnv *env, jobject lpObject, WINDOWPLACEMENT *lpWindowplacement, WINDOWPLACEMENT_FID_CACHE *lpWindowplacementFc);
void getWindowposFields(JNIEnv *env, jobject lpObject, WINDOWPOS *lpWindowpos, WINDOWPOS_FID_CACHE *lpWindowposFc);
void setWindowposFields(JNIEnv *env, jobject lpObject, WINDOWPOS *lpWindowpos, WINDOWPOS_FID_CACHE *lpWindowposFc);
void getWndclassexFields(JNIEnv *env, jobject lpObject, WNDCLASSEX *lpWndclassex, WNDCLASSEX_FID_CACHE *lpWndclassexFc);
void setWndclassexFields(JNIEnv *env, jobject lpObject, WNDCLASSEX *lpWndclassex, WNDCLASSEX_FID_CACHE *lpWndclassexFc);
/*
void getShfileinfoFields(JNIEnv *env, jobject lpObject, SHFILEINFO *lpShfileinfo, SHFILEINFO_FID_CACHE *lpShfileinfoFc);
void setShfileinfoFields(JNIEnv *env, jobject lpObject, SHFILEINFO *lpShfileinfo, SHFILEINFO_FID_CACHE *lpShfileinfoFc);
*/

/* ----------- ole/activex getter and setter prototypes  ----------- */
void getCauuidFields(JNIEnv *env, jobject lpObject, CAUUID *lpCauuid, CAUUID_FID_CACHE *lpCauuidFc);
void setCauuidFields(JNIEnv *env, jobject lpObject, CAUUID *lpCauuid, CAUUID_FID_CACHE *lpCauuidFc);
void getControlinfoFields(JNIEnv *env, jobject lpObject, CONTROLINFO *lpControlinfo, CONTROLINFO_FID_CACHE *lpControlinfoFc);
void setControlinfoFields(JNIEnv *env, jobject lpObject, CONTROLINFO *lpControlinfo, CONTROLINFO_FID_CACHE *lpControlinfoFc);
void getCoserverinfoFields(JNIEnv *env, jobject lpObject, COSERVERINFO *lpCoserverinfo, COSERVERINFO_FID_CACHE *lpCoserverinfoFc);
void setCoserverinfoFields(JNIEnv *env, jobject lpObject, COSERVERINFO *lpCoserverinfo, COSERVERINFO_FID_CACHE *lpCoserverinfoFc);
void getDispparamsFields(JNIEnv *env, jobject lpObject, DISPPARAMS *lpDispparams, DISPPARAMS_FID_CACHE *lpDispparamsFc);
void setDispparamsFields(JNIEnv *env, jobject lpObject, DISPPARAMS *lpDispparams, DISPPARAMS_FID_CACHE *lpDispparamsFc);
void getDropfilesFields(JNIEnv *env, jobject lpObject, DROPFILES *lpDropfiles, DROPFILES_FID_CACHE *lpDropfilesFc);
void setDropfilesFields(JNIEnv *env, jobject lpObject, DROPFILES *lpDropfiles, DROPFILES_FID_CACHE *lpDropfilesFc);
void getDvaspectinfoFields(JNIEnv *env, jobject lpObject, DVASPECTINFO *lpDvaspectinfo, DVASPECTINFO_FID_CACHE *lpDvaspectinfoFc);
void setDvaspectinfoFields(JNIEnv *env, jobject lpObject, DVASPECTINFO *lpDvaspectinfo, DVASPECTINFO_FID_CACHE *lpDvaspectinfoFc);
void getDvtargetdeviceFields(JNIEnv *env, jobject lpObject, DVTARGETDEVICE *lpDvtargetdevice, DVTARGETDEVICE_FID_CACHE *lpDvtargetdeviceFc);
void setDvtargetdeviceFields(JNIEnv *env, jobject lpObject, DVTARGETDEVICE *lpDvtargetdevice, DVTARGETDEVICE_FID_CACHE *lpDvtargetdeviceFc);
void getExcepinfoFields(JNIEnv *env, jobject lpObject, EXCEPINFO *lpExcepinfo, EXCEPINFO_FID_CACHE *lpExcepinfoFc);
void setExcepinfoFields(JNIEnv *env, jobject lpObject, EXCEPINFO *lpExcepinfo, EXCEPINFO_FID_CACHE *lpExcepinfoFc);
void getFiletimeFields(JNIEnv *env, jobject lpObject, FILETIME *lpFiletime, FILETIME_FID_CACHE *lpFiletimeFc);
void setFiletimeFields(JNIEnv *env, jobject lpObject, FILETIME *lpFiletime, FILETIME_FID_CACHE *lpFiletimeFc);
void getFormatetcFields(JNIEnv *env, jobject lpObject, FORMATETC *lpFormatetc, FORMATETC_FID_CACHE *lpFormatetcFc);
void setFormatetcFields(JNIEnv *env, jobject lpObject, FORMATETC *lpFormatetc, FORMATETC_FID_CACHE *lpFormatetcFc);
void getGuidFields(JNIEnv *env, jobject lpObject, GUID *lpGuid, GUID_FID_CACHE *lpGuidFc);
void setGuidFields(JNIEnv *env, jobject lpObject, GUID *lpGuid, GUID_FID_CACHE *lpGuidFc);
void getIdldescFields(JNIEnv *env, jobject lpObject, IDLDESC *lpIdldesc, IDLDESC_FID_CACHE *lpIdldescFc);
void setIdldescFields(JNIEnv *env, jobject lpObject, IDLDESC *lpIdldesc, IDLDESC_FID_CACHE *lpIdldescFc);
void getLicinfoFields(JNIEnv *env, jobject lpObject, LICINFO *lpLicinfo, LICINFO_FID_CACHE *lpLicinfoFc);
void setLicinfoFields(JNIEnv *env, jobject lpObject, LICINFO *lpLicinfo, LICINFO_FID_CACHE *lpLicinfoFc);
void getMulti_qiFields(JNIEnv *env, jobject lpObject, MULTI_QI *lpMulti_qi, MULTI_QI_FID_CACHE *lpMulti_qiFc);
void setMulti_qiFields(JNIEnv *env, jobject lpObject, MULTI_QI *lpMulti_qi, MULTI_QI_FID_CACHE *lpMulti_qiFc);
void getOlecmdFields(JNIEnv *env, jobject lpObject, OLECMD *lpOlecmd, OLECMD_FID_CACHE *lpOlecmdFc);
void setOlecmdFields(JNIEnv *env, jobject lpObject, OLECMD *lpOlecmd, OLECMD_FID_CACHE *lpOlecmdFc);
void getOlecmdtextFields(JNIEnv *env, jobject lpObject, OLECMDTEXT *lpOlecmdtext, OLECMDTEXT_FID_CACHE *lpOlecmdtextFc);
void setOlecmdtextFields(JNIEnv *env, jobject lpObject, OLECMDTEXT *lpOlecmdtext, OLECMDTEXT_FID_CACHE *lpOlecmdtextFc);
void getOleinplaceframeinfoFields(JNIEnv *env, jobject lpObject, OLEINPLACEFRAMEINFO *lpOleinplaceframeinfo, OLEINPLACEFRAMEINFO_FID_CACHE *lpOleinplaceframeinfoFc);
void setOleinplaceframeinfoFields(JNIEnv *env, jobject lpObject, OLEINPLACEFRAMEINFO *lpOleinplaceframeinfo, OLEINPLACEFRAMEINFO_FID_CACHE *lpOleinplaceframeinfoFc);
void getOleverbFields(JNIEnv *env, jobject lpObject, OLEVERB *lpOleverb, OLEVERB_FID_CACHE *lpOleverbFc);
void setOleverbFields(JNIEnv *env, jobject lpObject, OLEVERB *lpOleverb, OLEVERB_FID_CACHE *lpOleverbFc);
void getStgmediumFields(JNIEnv *env, jobject lpObject, STGMEDIUM *lpStgmedium, STGMEDIUM_FID_CACHE *lpStgmediumFc);
void setStgmediumFields(JNIEnv *env, jobject lpObject, STGMEDIUM *lpStgmedium, STGMEDIUM_FID_CACHE *lpStgmediumFc);
void getStatstgFields(JNIEnv *env, jobject lpObject, STATSTG *lpStatstg, STATSTG_FID_CACHE *lpStatstgFc);
void setStatstgFields(JNIEnv *env, jobject lpObject, STATSTG *lpStatstg, STATSTG_FID_CACHE *lpStatstgFc);
void getTypeattrFields(JNIEnv *env, jobject lpObject, TYPEATTR *lpTypeattr, TYPEATTR_FID_CACHE *lpTypeattrFc);
void setTypeattrFields(JNIEnv *env, jobject lpObject, TYPEATTR *lpTypeattr, TYPEATTR_FID_CACHE *lpTypeattrFc);
void getTypedescFields(JNIEnv *env, jobject lpObject, TYPEDESC *lpTypedesc, TYPEDESC_FID_CACHE *lpTypedescFc);
void setTypedescFields(JNIEnv *env, jobject lpObject, TYPEDESC *lpTypedesc, TYPEDESC_FID_CACHE *lpTypedescFc);
void getFuncdesc1Fields(JNIEnv *env, jobject lpObject, FUNCDESC *lpFuncdesc, FUNCDESC1_FID_CACHE *lpFuncdescFc);
void setFuncdesc1Fields(JNIEnv *env, jobject lpObject, FUNCDESC *lpFuncdesc, FUNCDESC1_FID_CACHE *lpFuncdescFc);
void getFuncdesc2Fields(JNIEnv *env, jobject lpObject, FUNCDESC *lpFuncdesc, FUNCDESC2_FID_CACHE *lpFuncdescFc);
void setFuncdesc2Fields(JNIEnv *env, jobject lpObject, FUNCDESC *lpFuncdesc, FUNCDESC2_FID_CACHE *lpFuncdescFc);
void getVardesc1Fields(JNIEnv *env, jobject lpObject, VARDESC *lpVardesc, VARDESC1_FID_CACHE *lpVardescFc);
void setVardesc1Fields(JNIEnv *env, jobject lpObject, VARDESC *lpVardesc, VARDESC1_FID_CACHE *lpVardescFc);
void getVardesc2Fields(JNIEnv *env, jobject lpObject, VARDESC *lpVardesc, VARDESC2_FID_CACHE *lpVardescFc);
void setVardesc2Fields(JNIEnv *env, jobject lpObject, VARDESC *lpVardesc, VARDESC2_FID_CACHE *lpVardescFc);
void getGCP_RESULTSFields(JNIEnv *env, jobject lpObject, GCP_RESULTS *lpStruct, PGCP_RESULTS_FID_CACHE lpCache);
void setGCP_RESULTSFields(JNIEnv *env, jobject lpObject, GCP_RESULTS *lpStruct, PGCP_RESULTS_FID_CACHE lpCache);


#endif // INC_structs_H