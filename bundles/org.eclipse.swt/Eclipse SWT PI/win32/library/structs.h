/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
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

#ifndef _WIN32_WCE
#include <initguid.h>
#include <oleacc.h>
#endif // _WIN32_WCE

#if defined(WIN32_PLATFORM_PSPC) || defined(WIN32_PLATFORM_WFSP)
#include <aygshell.h>
#endif // WIN32_PLATFORM_PSPC, WIN32_PLATFORM_WFSP

#ifdef WIN32_PLATFORM_WFSP
#include <tpcshell.h>
#endif /* WIN32_PLATFORM_WFSP */

/* Optional custom rules to exclude some types */
#include "rules.h"

#ifdef _WIN32_WCE
#define NO_BROWSEINFO
#define NO_CHOOSEFONT
#define NO_DOCINFO
#define NO_GCP_RESULTS
#define NO_GRADIENT_RECT
#define NO_HELPINFO
#define NO_MENUINFO
#define NO_NMREBARCHEVRON
#define NO_NMTTDISPINFO
#define NO_NONCLIENTMETRICS
#define NO_PRINTDLG
#define NO_TOOLINFO
#define NO_TRACKMOUSEEVENT
#define NO_TRIVERTEX
#define NO_WINDOWPLACEMENT
#define NO_DROPFILES
#define NO_OLECMD
#define NO_OLECMDTEXT

/* OLE */
#define NO_CAUUID
#define NO_CONTROLINFO
#define NO_COSERVERINFO
#define NO_DISPPARAMS
#define NO_DVTARGETDEVICE
#define NO_EXCEPINFO
#define NO_FORMATETC
#define NO_FUNCDESC1
#define NO_FUNCDESC2
#define NO_GUID
#define NO_LICINFO
#define NO_OLEINPLACEFRAMEINFO
#define NO_STATSTG
#define NO_STGMEDIUM
#define NO_TYPEATTR
#define NO_VARDESC1
#define NO_VARDESC2
#endif /* _WIN32_WCE */

#if !defined(WIN32_PLATFORM_PSPC) && !defined(WIN32_PLATFORM_WFSP)
#define NO_SHMENUBARINFO
#endif /* WIN32_PLATFORM_PSPC, WIN32_PLATFORM_WFSP */

#ifndef WIN32_PLATFORM_PSPC
#define NO_SHACTIVATEINFO
#endif /* WIN32_PLATFORM_PSPC */

/** Structs */
#ifndef NO_ACCEL
ACCEL *getACCELFields(JNIEnv *env, jobject lpObject, ACCEL *lpStruct);
void setACCELFields(JNIEnv *env, jobject lpObject, ACCEL *lpStruct);
#else
#define getACCELFields(a,b,c) NULL
#define setACCELFields(a,b,c)
#endif /* NO_ACCEL */

#ifndef NO_BITMAP
BITMAP *getBITMAPFields(JNIEnv *env, jobject lpObject, BITMAP *lpStruct);
void setBITMAPFields(JNIEnv *env, jobject lpObject, BITMAP *lpStruct);
#else
#define getBITMAPFields(a,b,c) NULL
#define setBITMAPFields(a,b,c)
#endif /* NO_BITMAP */

#ifndef NO_BROWSEINFO
BROWSEINFO *getBROWSEINFOFields(JNIEnv *env, jobject lpObject, BROWSEINFO *lpStruct);
void setBROWSEINFOFields(JNIEnv *env, jobject lpObject, BROWSEINFO *lpStruct);
#else
#define getBROWSEINFOFields(a,b,c) NULL
#define setBROWSEINFOFields(a,b,c)
#endif /* NO_BROWSEINFO */

#ifndef NO_CHOOSECOLOR
CHOOSECOLOR *getCHOOSECOLORFields(JNIEnv *env, jobject lpObject, CHOOSECOLOR *lpStruct);
void setCHOOSECOLORFields(JNIEnv *env, jobject lpObject, CHOOSECOLOR *lpStruct);
#else
#define getCHOOSECOLORFields(a,b,c) NULL
#define setCHOOSECOLORFields(a,b,c)
#endif /* NO_CHOOSECOLOR */

#ifndef NO_CHOOSEFONT
CHOOSEFONT *getCHOOSEFONTFields(JNIEnv *env, jobject lpObject, CHOOSEFONT *lpStruct);
void setCHOOSEFONTFields(JNIEnv *env, jobject lpObject, CHOOSEFONT *lpStruct);
#else
#define getCHOOSEFONTFields(a,b,c) NULL
#define setCHOOSEFONTFields(a,b,c)
#endif /* NO_CHOOSEFONT */

#ifndef NO_COMPOSITIONFORM
COMPOSITIONFORM *getCOMPOSITIONFORMFields(JNIEnv *env, jobject lpObject, COMPOSITIONFORM *lpStruct);
void setCOMPOSITIONFORMFields(JNIEnv *env, jobject lpObject, COMPOSITIONFORM *lpStruct);
#else
#define getCOMPOSITIONFORMFields(a,b,c) NULL
#define setCOMPOSITIONFORMFields(a,b,c)
#endif /* NO_COMPOSITIONFORM */

#ifndef NO_CREATESTRUCT
CREATESTRUCT *getCREATESTRUCTFields(JNIEnv *env, jobject lpObject, CREATESTRUCT *lpStruct);
void setCREATESTRUCTFields(JNIEnv *env, jobject lpObject, CREATESTRUCT *lpStruct);
#else
#define getCREATESTRUCTFields(a,b,c) NULL
#define setCREATESTRUCTFields(a,b,c)
#endif /* NO_CREATESTRUCT */

#ifndef NO_DIBSECTION
DIBSECTION *getDIBSECTIONFields(JNIEnv *env, jobject lpObject, DIBSECTION *lpStruct);
void setDIBSECTIONFields(JNIEnv *env, jobject lpObject, DIBSECTION *lpStruct);
#else
#define getDIBSECTIONFields(a,b,c) NULL
#define setDIBSECTIONFields(a,b,c)
#endif /* NO_DIBSECTION */

#ifndef NO_DLLVERSIONINFO
DLLVERSIONINFO *getDLLVERSIONINFOFields(JNIEnv *env, jobject lpObject, DLLVERSIONINFO *lpStruct);
void setDLLVERSIONINFOFields(JNIEnv *env, jobject lpObject, DLLVERSIONINFO *lpStruct);
#else
#define getDLLVERSIONINFOFields(a,b,c) NULL
#define setDLLVERSIONINFOFields(a,b,c)
#endif /* NO_DLLVERSIONINFO */

#ifndef NO_DOCINFO
DOCINFO *getDOCINFOFields(JNIEnv *env, jobject lpObject, DOCINFO *lpStruct);
void setDOCINFOFields(JNIEnv *env, jobject lpObject, DOCINFO *lpStruct);
#else
#define getDOCINFOFields(a,b,c) NULL
#define setDOCINFOFields(a,b,c)
#endif /* NO_DOCINFO */

#ifndef NO_DRAWITEMSTRUCT
DRAWITEMSTRUCT *getDRAWITEMSTRUCTFields(JNIEnv *env, jobject lpObject, DRAWITEMSTRUCT *lpStruct);
void setDRAWITEMSTRUCTFields(JNIEnv *env, jobject lpObject, DRAWITEMSTRUCT *lpStruct);
#else
#define getDRAWITEMSTRUCTFields(a,b,c) NULL
#define setDRAWITEMSTRUCTFields(a,b,c)
#endif /* NO_DRAWITEMSTRUCT */

#ifndef NO_DROPFILES
DROPFILES *getDROPFILESFields(JNIEnv *env, jobject lpObject, DROPFILES *lpStruct);
void setDROPFILESFields(JNIEnv *env, jobject lpObject, DROPFILES *lpStruct);
#else
#define getDROPFILESFields(a,b,c) NULL
#define setDROPFILESFields(a,b,c)
#endif /* NO_DROPFILES */

#ifndef NO_FILETIME
FILETIME *getFILETIMEFields(JNIEnv *env, jobject lpObject, FILETIME *lpStruct);
void setFILETIMEFields(JNIEnv *env, jobject lpObject, FILETIME *lpStruct);
#else
#define getFILETIMEFields(a,b,c) NULL
#define setFILETIMEFields(a,b,c)
#endif /* NO_FILETIME */

#ifndef NO_GCP_RESULTS
GCP_RESULTS *getGCP_RESULTSFields(JNIEnv *env, jobject lpObject, GCP_RESULTS *lpStruct);
void setGCP_RESULTSFields(JNIEnv *env, jobject lpObject, GCP_RESULTS *lpStruct);
#else
#define getGCP_RESULTSFields(a,b,c) NULL
#define setGCP_RESULTSFields(a,b,c)
#endif /* NO_GCP_RESULTS */

#ifndef NO_GRADIENT_RECT
GRADIENT_RECT *getGRADIENT_RECTFields(JNIEnv *env, jobject lpObject, GRADIENT_RECT *lpStruct);
void setGRADIENT_RECTFields(JNIEnv *env, jobject lpObject, GRADIENT_RECT *lpStruct);
#else
#define getGRADIENT_RECTFields(a,b,c) NULL
#define setGRADIENT_RECTFields(a,b,c)
#endif /* NO_GRADIENT_RECT */

#ifndef NO_HDITEM
HDITEM *getHDITEMFields(JNIEnv *env, jobject lpObject, HDITEM *lpStruct);
void setHDITEMFields(JNIEnv *env, jobject lpObject, HDITEM *lpStruct);
#else
#define getHDITEMFields(a,b,c) NULL
#define setHDITEMFields(a,b,c)
#endif /* NO_HDITEM */

#ifndef NO_HELPINFO
HELPINFO *getHELPINFOFields(JNIEnv *env, jobject lpObject, HELPINFO *lpStruct);
void setHELPINFOFields(JNIEnv *env, jobject lpObject, HELPINFO *lpStruct);
#else
#define getHELPINFOFields(a,b,c) NULL
#define setHELPINFOFields(a,b,c)
#endif /* NO_HELPINFO */

#ifndef NO_ICONINFO
ICONINFO *getICONINFOFields(JNIEnv *env, jobject lpObject, ICONINFO *lpStruct);
void setICONINFOFields(JNIEnv *env, jobject lpObject, ICONINFO *lpStruct);
#else
#define getICONINFOFields(a,b,c) NULL
#define setICONINFOFields(a,b,c)
#endif /* NO_ICONINFO */

#ifndef NO_INITCOMMONCONTROLSEX
INITCOMMONCONTROLSEX *getINITCOMMONCONTROLSEXFields(JNIEnv *env, jobject lpObject, INITCOMMONCONTROLSEX *lpStruct);
void setINITCOMMONCONTROLSEXFields(JNIEnv *env, jobject lpObject, INITCOMMONCONTROLSEX *lpStruct);
#else
#define getINITCOMMONCONTROLSEXFields(a,b,c) NULL
#define setINITCOMMONCONTROLSEXFields(a,b,c)
#endif /* NO_INITCOMMONCONTROLSEX */

#ifndef NO_LOGBRUSH
LOGBRUSH *getLOGBRUSHFields(JNIEnv *env, jobject lpObject, LOGBRUSH *lpStruct);
void setLOGBRUSHFields(JNIEnv *env, jobject lpObject, LOGBRUSH *lpStruct);
#else
#define getLOGBRUSHFields(a,b,c) NULL
#define setLOGBRUSHFields(a,b,c)
#endif /* NO_LOGBRUSH */

#ifdef NO_LOGFONT
#define NO_LOGFONTA
#define NO_LOGFONTW
#endif /* NO_LOGFONT */

#ifndef NO_LOGFONTA
LOGFONTA *getLOGFONTAFields(JNIEnv *env, jobject lpObject, LOGFONTA *lpStruct);
void setLOGFONTAFields(JNIEnv *env, jobject lpObject, LOGFONTA *lpStruct);
#else
#define getLOGFONTAFields(a,b,c) NULL
#define setLOGFONTAFields(a,b,c)
#endif /* NO_LOGFONTA */

#ifndef NO_LOGFONTW
LOGFONTW *getLOGFONTWFields(JNIEnv *env, jobject lpObject, LOGFONTW *lpStruct);
void setLOGFONTWFields(JNIEnv *env, jobject lpObject, LOGFONTW *lpStruct);
#else
#define getLOGFONTWFields(a,b,c) NULL
#define setLOGFONTWFields(a,b,c)
#endif /* NO_LOGFONTW */

#ifndef NO_LOGPEN
LOGPEN *getLOGPENFields(JNIEnv *env, jobject lpObject, LOGPEN *lpStruct);
void setLOGPENFields(JNIEnv *env, jobject lpObject, LOGPEN *lpStruct);
#else
#define getLOGPENFields(a,b,c) NULL
#define setLOGPENFields(a,b,c)
#endif /* NO_LOGPEN */

#ifndef NO_LVCOLUMN
LVCOLUMN *getLVCOLUMNFields(JNIEnv *env, jobject lpObject, LVCOLUMN *lpStruct);
void setLVCOLUMNFields(JNIEnv *env, jobject lpObject, LVCOLUMN *lpStruct);
#else
#define getLVCOLUMNFields(a,b,c) NULL
#define setLVCOLUMNFields(a,b,c)
#endif /* NO_LVCOLUMN */

#ifndef NO_LVHITTESTINFO
LVHITTESTINFO *getLVHITTESTINFOFields(JNIEnv *env, jobject lpObject, LVHITTESTINFO *lpStruct);
void setLVHITTESTINFOFields(JNIEnv *env, jobject lpObject, LVHITTESTINFO *lpStruct);
#else
#define getLVHITTESTINFOFields(a,b,c) NULL
#define setLVHITTESTINFOFields(a,b,c)
#endif /* NO_LVHITTESTINFO */

#ifndef NO_LVITEM
LVITEM *getLVITEMFields(JNIEnv *env, jobject lpObject, LVITEM *lpStruct);
void setLVITEMFields(JNIEnv *env, jobject lpObject, LVITEM *lpStruct);
#else
#define getLVITEMFields(a,b,c) NULL
#define setLVITEMFields(a,b,c)
#endif /* NO_LVITEM */

#ifndef NO_MEASUREITEMSTRUCT
MEASUREITEMSTRUCT *getMEASUREITEMSTRUCTFields(JNIEnv *env, jobject lpObject, MEASUREITEMSTRUCT *lpStruct);
void setMEASUREITEMSTRUCTFields(JNIEnv *env, jobject lpObject, MEASUREITEMSTRUCT *lpStruct);
#else
#define getMEASUREITEMSTRUCTFields(a,b,c) NULL
#define setMEASUREITEMSTRUCTFields(a,b,c)
#endif /* NO_MEASUREITEMSTRUCT */

#ifndef NO_MENUINFO
MENUINFO *getMENUINFOFields(JNIEnv *env, jobject lpObject, MENUINFO *lpStruct);
void setMENUINFOFields(JNIEnv *env, jobject lpObject, MENUINFO *lpStruct);
#else
#define getMENUINFOFields(a,b,c) NULL
#define setMENUINFOFields(a,b,c)
#endif /* NO_MENUINFO */

#ifndef NO_MENUITEMINFO
MENUITEMINFO *getMENUITEMINFOFields(JNIEnv *env, jobject lpObject, MENUITEMINFO *lpStruct);
void setMENUITEMINFOFields(JNIEnv *env, jobject lpObject, MENUITEMINFO *lpStruct);
#else
#define getMENUITEMINFOFields(a,b,c) NULL
#define setMENUITEMINFOFields(a,b,c)
#endif /* NO_MENUITEMINFO */

#ifndef NO_MSG
MSG *getMSGFields(JNIEnv *env, jobject lpObject, MSG *lpStruct);
void setMSGFields(JNIEnv *env, jobject lpObject, MSG *lpStruct);
#else
#define getMSGFields(a,b,c) NULL
#define setMSGFields(a,b,c)
#endif /* NO_MSG */

#ifndef NO_NMCUSTOMDRAW
NMCUSTOMDRAW *getNMCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMCUSTOMDRAW *lpStruct);
void setNMCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMCUSTOMDRAW *lpStruct);
#else
#define getNMCUSTOMDRAWFields(a,b,c) NULL
#define setNMCUSTOMDRAWFields(a,b,c)
#endif /* NO_NMCUSTOMDRAW */

#ifndef NO_NMHDR
NMHDR *getNMHDRFields(JNIEnv *env, jobject lpObject, NMHDR *lpStruct);
void setNMHDRFields(JNIEnv *env, jobject lpObject, NMHDR *lpStruct);
#else
#define getNMHDRFields(a,b,c) NULL
#define setNMHDRFields(a,b,c)
#endif /* NO_NMHDR */

#ifndef NO_NMHEADER
NMHEADER *getNMHEADERFields(JNIEnv *env, jobject lpObject, NMHEADER *lpStruct);
void setNMHEADERFields(JNIEnv *env, jobject lpObject, NMHEADER *lpStruct);
#else
#define getNMHEADERFields(a,b,c) NULL
#define setNMHEADERFields(a,b,c)
#endif /* NO_NMHEADER */

#ifndef NO_NMLISTVIEW
NMLISTVIEW *getNMLISTVIEWFields(JNIEnv *env, jobject lpObject, NMLISTVIEW *lpStruct);
void setNMLISTVIEWFields(JNIEnv *env, jobject lpObject, NMLISTVIEW *lpStruct);
#else
#define getNMLISTVIEWFields(a,b,c) NULL
#define setNMLISTVIEWFields(a,b,c)
#endif /* NO_NMLISTVIEW */

#ifndef NO_NMLVCUSTOMDRAW
NMLVCUSTOMDRAW *getNMLVCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMLVCUSTOMDRAW *lpStruct);
void setNMLVCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMLVCUSTOMDRAW *lpStruct);
#else
#define getNMLVCUSTOMDRAWFields(a,b,c) NULL
#define setNMLVCUSTOMDRAWFields(a,b,c)
#endif /* NO_NMLVCUSTOMDRAW */

#ifndef NO_NMREBARCHEVRON
NMREBARCHEVRON *getNMREBARCHEVRONFields(JNIEnv *env, jobject lpObject, NMREBARCHEVRON *lpStruct);
void setNMREBARCHEVRONFields(JNIEnv *env, jobject lpObject, NMREBARCHEVRON *lpStruct);
#else
#define getNMREBARCHEVRONFields(a,b,c) NULL
#define setNMREBARCHEVRONFields(a,b,c)
#endif /* NO_NMREBARCHEVRON */

#ifndef NO_NMTOOLBAR
NMTOOLBAR *getNMTOOLBARFields(JNIEnv *env, jobject lpObject, NMTOOLBAR *lpStruct);
void setNMTOOLBARFields(JNIEnv *env, jobject lpObject, NMTOOLBAR *lpStruct);
#else
#define getNMTOOLBARFields(a,b,c) NULL
#define setNMTOOLBARFields(a,b,c)
#endif /* NO_NMTOOLBAR */

#ifdef NO_NMTTDISPINFO
#define NO_NMTTDISPINFOA
#define NO_NMTTDISPINFOW
#endif /* NO_NMTTDISPINFO */

#ifndef NO_NMTTDISPINFOA
NMTTDISPINFOA *getNMTTDISPINFOAFields(JNIEnv *env, jobject lpObject, NMTTDISPINFOA *lpStruct);
void setNMTTDISPINFOAFields(JNIEnv *env, jobject lpObject, NMTTDISPINFOA *lpStruct);
#else
#define getNMTTDISPINFOAFields(a,b,c) NULL
#define setNMTTDISPINFOAFields(a,b,c)
#endif /* NO_NMTTDISPINFOA */

#ifndef NO_NMTTDISPINFOW
NMTTDISPINFOW *getNMTTDISPINFOWFields(JNIEnv *env, jobject lpObject, NMTTDISPINFOW *lpStruct);
void setNMTTDISPINFOWFields(JNIEnv *env, jobject lpObject, NMTTDISPINFOW *lpStruct);
#else
#define getNMTTDISPINFOWFields(a,b,c) NULL
#define setNMTTDISPINFOWFields(a,b,c)
#endif /* NO_NMTTDISPINFOW */

#ifndef NO_NMTVCUSTOMDRAW
NMTVCUSTOMDRAW *getNMTVCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMTVCUSTOMDRAW *lpStruct);
void setNMTVCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMTVCUSTOMDRAW *lpStruct);
#else
#define getNMTVCUSTOMDRAWFields(a,b,c) NULL
#define setNMTVCUSTOMDRAWFields(a,b,c)
#endif /* NO_NMTVCUSTOMDRAW */

#ifdef NO_NONCLIENTMETRICS
#define NO_NONCLIENTMETRICSA
#define NO_NONCLIENTMETRICSW
#endif /* NO_NONCLIENTMETRICS */

#ifndef NO_NONCLIENTMETRICSA
NONCLIENTMETRICSA *getNONCLIENTMETRICSAFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICSA *lpStruct);
void setNONCLIENTMETRICSAFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICSA *lpStruct);
#else
#define getNONCLIENTMETRICSAFields(a,b,c) NULL
#define setNONCLIENTMETRICSAFields(a,b,c)
#endif /* NO_NONCLIENTMETRICSA */

#ifndef NO_NONCLIENTMETRICSW
NONCLIENTMETRICSW *getNONCLIENTMETRICSWFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICSW *lpStruct);
void setNONCLIENTMETRICSWFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICSW *lpStruct);
#else
#define getNONCLIENTMETRICSWFields(a,b,c) NULL
#define setNONCLIENTMETRICSWFields(a,b,c)
#endif /* NO_NONCLIENTMETRICSW */

#ifndef NO_OPENFILENAME
OPENFILENAME *getOPENFILENAMEFields(JNIEnv *env, jobject lpObject, OPENFILENAME *lpStruct);
void setOPENFILENAMEFields(JNIEnv *env, jobject lpObject, OPENFILENAME *lpStruct);
#else
#define getOPENFILENAMEFields(a,b,c) NULL
#define setOPENFILENAMEFields(a,b,c)
#endif /* NO_OPENFILENAME */

#ifdef NO_OSVERSIONINFO
#define NO_OSVERSIONINFOA
#define NO_OSVERSIONINFOW
#endif /* NO_OSVERSIONINFO */

#ifndef NO_OSVERSIONINFOA
OSVERSIONINFOA *getOSVERSIONINFOAFields(JNIEnv *env, jobject lpObject, OSVERSIONINFOA *lpStruct);
void setOSVERSIONINFOAFields(JNIEnv *env, jobject lpObject, OSVERSIONINFOA *lpStruct);
#else
#define getOSVERSIONINFOAFields(a,b,c) NULL
#define setOSVERSIONINFOAFields(a,b,c)
#endif /* NO_OSVERSIONINFOA */

#ifndef NO_OSVERSIONINFOW
OSVERSIONINFOW *getOSVERSIONINFOWFields(JNIEnv *env, jobject lpObject, OSVERSIONINFOW *lpStruct);
void setOSVERSIONINFOWFields(JNIEnv *env, jobject lpObject, OSVERSIONINFOW *lpStruct);
#else
#define getOSVERSIONINFOWFields(a,b,c) NULL
#define setOSVERSIONINFOWFields(a,b,c)
#endif /* NO_OSVERSIONINFOW */

#ifndef NO_PAINTSTRUCT
PAINTSTRUCT *getPAINTSTRUCTFields(JNIEnv *env, jobject lpObject, PAINTSTRUCT *lpStruct);
void setPAINTSTRUCTFields(JNIEnv *env, jobject lpObject, PAINTSTRUCT *lpStruct);
#else
#define getPAINTSTRUCTFields(a,b,c) NULL
#define setPAINTSTRUCTFields(a,b,c)
#endif /* NO_PAINTSTRUCT */

#ifndef NO_POINT
POINT *getPOINTFields(JNIEnv *env, jobject lpObject, POINT *lpStruct);
void setPOINTFields(JNIEnv *env, jobject lpObject, POINT *lpStruct);
#else
#define getPOINTFields(a,b,c) NULL
#define setPOINTFields(a,b,c)
#endif /* NO_POINT */

#ifndef NO_PRINTDLG
PRINTDLG *getPRINTDLGFields(JNIEnv *env, jobject lpObject, PRINTDLG *lpStruct);
void setPRINTDLGFields(JNIEnv *env, jobject lpObject, PRINTDLG *lpStruct);
#else
#define getPRINTDLGFields(a,b,c) NULL
#define setPRINTDLGFields(a,b,c)
#endif /* NO_PRINTDLG */

#ifndef NO_REBARBANDINFO
REBARBANDINFO *getREBARBANDINFOFields(JNIEnv *env, jobject lpObject, REBARBANDINFO *lpStruct);
void setREBARBANDINFOFields(JNIEnv *env, jobject lpObject, REBARBANDINFO *lpStruct);
#else
#define getREBARBANDINFOFields(a,b,c) NULL
#define setREBARBANDINFOFields(a,b,c)
#endif /* NO_REBARBANDINFO */

#ifndef NO_RECT
RECT *getRECTFields(JNIEnv *env, jobject lpObject, RECT *lpStruct);
void setRECTFields(JNIEnv *env, jobject lpObject, RECT *lpStruct);
#else
#define getRECTFields(a,b,c) NULL
#define setRECTFields(a,b,c)
#endif /* NO_RECT */

#ifndef NO_SCROLLINFO
SCROLLINFO *getSCROLLINFOFields(JNIEnv *env, jobject lpObject, SCROLLINFO *lpStruct);
void setSCROLLINFOFields(JNIEnv *env, jobject lpObject, SCROLLINFO *lpStruct);
#else
#define getSCROLLINFOFields(a,b,c) NULL
#define setSCROLLINFOFields(a,b,c)
#endif /* NO_SCROLLINFO */

#ifndef NO_SHACTIVATEINFO
SHACTIVATEINFO *getSHACTIVATEINFOFields(JNIEnv *env, jobject lpObject, SHACTIVATEINFO *lpStruct);
void setSHACTIVATEINFOFields(JNIEnv *env, jobject lpObject, SHACTIVATEINFO *lpStruct);
#else
#define getSHACTIVATEINFOFields(a,b,c) NULL
#define setSHACTIVATEINFOFields(a,b,c)
#endif /* NO_SHACTIVATEINFO */

#ifndef NO_SHELLEXECUTEINFO
SHELLEXECUTEINFO *getSHELLEXECUTEINFOFields(JNIEnv *env, jobject lpObject, SHELLEXECUTEINFO *lpStruct);
void setSHELLEXECUTEINFOFields(JNIEnv *env, jobject lpObject, SHELLEXECUTEINFO *lpStruct);
#else
#define getSHELLEXECUTEINFOFields(a,b,c) NULL
#define setSHELLEXECUTEINFOFields(a,b,c)
#endif /* NO_SHELLEXECUTEINFO */

#ifndef NO_SHMENUBARINFO
SHMENUBARINFO *getSHMENUBARINFOFields(JNIEnv *env, jobject lpObject, SHMENUBARINFO *lpStruct);
void setSHMENUBARINFOFields(JNIEnv *env, jobject lpObject, SHMENUBARINFO *lpStruct);
#else
#define getSHMENUBARINFOFields(a,b,c) NULL
#define setSHMENUBARINFOFields(a,b,c)
#endif /* NO_SHMENUBARINFO */

#ifndef NO_SIZE
SIZE *getSIZEFields(JNIEnv *env, jobject lpObject, SIZE *lpStruct);
void setSIZEFields(JNIEnv *env, jobject lpObject, SIZE *lpStruct);
#else
#define getSIZEFields(a,b,c) NULL
#define setSIZEFields(a,b,c)
#endif /* NO_SIZE */

#ifndef NO_TBBUTTON
TBBUTTON *getTBBUTTONFields(JNIEnv *env, jobject lpObject, TBBUTTON *lpStruct);
void setTBBUTTONFields(JNIEnv *env, jobject lpObject, TBBUTTON *lpStruct);
#else
#define getTBBUTTONFields(a,b,c) NULL
#define setTBBUTTONFields(a,b,c)
#endif /* NO_TBBUTTON */

#ifndef NO_TBBUTTONINFO
TBBUTTONINFO *getTBBUTTONINFOFields(JNIEnv *env, jobject lpObject, TBBUTTONINFO *lpStruct);
void setTBBUTTONINFOFields(JNIEnv *env, jobject lpObject, TBBUTTONINFO *lpStruct);
#else
#define getTBBUTTONINFOFields(a,b,c) NULL
#define setTBBUTTONINFOFields(a,b,c)
#endif /* NO_TBBUTTONINFO */

#ifndef NO_TCITEM
TCITEM *getTCITEMFields(JNIEnv *env, jobject lpObject, TCITEM *lpStruct);
void setTCITEMFields(JNIEnv *env, jobject lpObject, TCITEM *lpStruct);
#else
#define getTCITEMFields(a,b,c) NULL
#define setTCITEMFields(a,b,c)
#endif /* NO_TCITEM */

#ifdef NO_TEXTMETRIC
#define NO_TEXTMETRICA
#define NO_TEXTMETRICW
#endif /* NO_TEXTMETRIC */

#ifndef NO_TEXTMETRICA
TEXTMETRICA *getTEXTMETRICAFields(JNIEnv *env, jobject lpObject, TEXTMETRICA *lpStruct);
void setTEXTMETRICAFields(JNIEnv *env, jobject lpObject, TEXTMETRICA *lpStruct);
#else
#define getTEXTMETRICAFields(a,b,c) NULL
#define setTEXTMETRICAFields(a,b,c)
#endif /* NO_TEXTMETRICA */

#ifndef NO_TEXTMETRICW
TEXTMETRICW *getTEXTMETRICWFields(JNIEnv *env, jobject lpObject, TEXTMETRICW *lpStruct);
void setTEXTMETRICWFields(JNIEnv *env, jobject lpObject, TEXTMETRICW *lpStruct);
#else
#define getTEXTMETRICWFields(a,b,c) NULL
#define setTEXTMETRICWFields(a,b,c)
#endif /* NO_TEXTMETRICW */

#ifndef NO_TOOLINFO
TOOLINFO *getTOOLINFOFields(JNIEnv *env, jobject lpObject, TOOLINFO *lpStruct);
void setTOOLINFOFields(JNIEnv *env, jobject lpObject, TOOLINFO *lpStruct);
#else
#define getTOOLINFOFields(a,b,c) NULL
#define setTOOLINFOFields(a,b,c)
#endif /* NO_TOOLINFO */

#ifndef NO_TRACKMOUSEEVENT
TRACKMOUSEEVENT *getTRACKMOUSEEVENTFields(JNIEnv *env, jobject lpObject, TRACKMOUSEEVENT *lpStruct);
void setTRACKMOUSEEVENTFields(JNIEnv *env, jobject lpObject, TRACKMOUSEEVENT *lpStruct);
#else
#define getTRACKMOUSEEVENTFields(a,b,c) NULL
#define setTRACKMOUSEEVENTFields(a,b,c)
#endif /* NO_TRACKMOUSEEVENT */

#ifndef NO_TRIVERTEX
TRIVERTEX *getTRIVERTEXFields(JNIEnv *env, jobject lpObject, TRIVERTEX *lpStruct);
void setTRIVERTEXFields(JNIEnv *env, jobject lpObject, TRIVERTEX *lpStruct);
#else
#define getTRIVERTEXFields(a,b,c) NULL
#define setTRIVERTEXFields(a,b,c)
#endif /* NO_TRIVERTEX */

#ifndef NO_TVHITTESTINFO
TVHITTESTINFO *getTVHITTESTINFOFields(JNIEnv *env, jobject lpObject, TVHITTESTINFO *lpStruct);
void setTVHITTESTINFOFields(JNIEnv *env, jobject lpObject, TVHITTESTINFO *lpStruct);
#else
#define getTVHITTESTINFOFields(a,b,c) NULL
#define setTVHITTESTINFOFields(a,b,c)
#endif /* NO_TVHITTESTINFO */

#ifndef NO_TVINSERTSTRUCT
TVINSERTSTRUCT *getTVINSERTSTRUCTFields(JNIEnv *env, jobject lpObject, TVINSERTSTRUCT *lpStruct);
void setTVINSERTSTRUCTFields(JNIEnv *env, jobject lpObject, TVINSERTSTRUCT *lpStruct);
#else
#define getTVINSERTSTRUCTFields(a,b,c) NULL
#define setTVINSERTSTRUCTFields(a,b,c)
#endif /* NO_TVINSERTSTRUCT */

#ifndef NO_TVITEM
TVITEM *getTVITEMFields(JNIEnv *env, jobject lpObject, TVITEM *lpStruct);
void setTVITEMFields(JNIEnv *env, jobject lpObject, TVITEM *lpStruct);
#else
#define getTVITEMFields(a,b,c) NULL
#define setTVITEMFields(a,b,c)
#endif /* NO_TVITEM */

#ifndef NO_WINDOWPLACEMENT
WINDOWPLACEMENT *getWINDOWPLACEMENTFields(JNIEnv *env, jobject lpObject, WINDOWPLACEMENT *lpStruct);
void setWINDOWPLACEMENTFields(JNIEnv *env, jobject lpObject, WINDOWPLACEMENT *lpStruct);
#else
#define getWINDOWPLACEMENTFields(a,b,c) NULL
#define setWINDOWPLACEMENTFields(a,b,c)
#endif /* NO_WINDOWPLACEMENT */

#ifndef NO_WINDOWPOS
WINDOWPOS *getWINDOWPOSFields(JNIEnv *env, jobject lpObject, WINDOWPOS *lpStruct);
void setWINDOWPOSFields(JNIEnv *env, jobject lpObject, WINDOWPOS *lpStruct);
#else
#define getWINDOWPOSFields(a,b,c) NULL
#define setWINDOWPOSFields(a,b,c)
#endif /* NO_WINDOWPOS */

#ifndef NO_WNDCLASS
WNDCLASS *getWNDCLASSFields(JNIEnv *env, jobject lpObject, WNDCLASS *lpStruct);
void setWNDCLASSFields(JNIEnv *env, jobject lpObject, WNDCLASS *lpStruct);
#else
#define getWNDCLASSFields(a,b,c) NULL
#define setWNDCLASSFields(a,b,c)
#endif /* NO_WNDCLASS */

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

#ifndef NO_CAUUID
CAUUID *getCAUUIDFields(JNIEnv *env, jobject lpObject, CAUUID *lpStruct);
void setCAUUIDFields(JNIEnv *env, jobject lpObject, CAUUID *lpStruct);
#endif /* NO_CAUUID */

#ifndef NO_CONTROLINFO
CONTROLINFO *getCONTROLINFOFields(JNIEnv *env, jobject lpObject, CONTROLINFO *lpStruct);
void setCONTROLINFOFields(JNIEnv *env, jobject lpObject, CONTROLINFO *lpStruct);
#endif /* NO_CONTROLINFO */

#ifndef NO_COSERVERINFO
COSERVERINFO *getCOSERVERINFOFields(JNIEnv *env, jobject lpObject, COSERVERINFO *lpStruct);
void setCOSERVERINFOFields(JNIEnv *env, jobject lpObject, COSERVERINFO *lpStruct);
#endif /* NO_COSERVERINFO */

#ifndef NO_DISPPARAMS
DISPPARAMS *getDISPPARAMSFields(JNIEnv *env, jobject lpObject, DISPPARAMS *lpStruct);
void setDISPPARAMSFields(JNIEnv *env, jobject lpObject, DISPPARAMS *lpStruct);
#endif /* NO_DISPPARAMS */

#ifndef NO_DVTARGETDEVICE
DVTARGETDEVICE *getDVTARGETDEVICEFields(JNIEnv *env, jobject lpObject, DVTARGETDEVICE *lpStruct);
void setDVTARGETDEVICEFields(JNIEnv *env, jobject lpObject, DVTARGETDEVICE *lpStruct);
#endif /* NO_DVTARGETDEVICE */

#ifndef NO_EXCEPINFO
EXCEPINFO *getEXCEPINFOFields(JNIEnv *env, jobject lpObject, EXCEPINFO *lpStruct);
void setEXCEPINFOFields(JNIEnv *env, jobject lpObject, EXCEPINFO *lpStruct);
#endif /* NO_EXCEPINFO */

#ifndef NO_FORMATETC
FORMATETC *getFORMATETCFields(JNIEnv *env, jobject lpObject, FORMATETC *lpStruct);
void setFORMATETCFields(JNIEnv *env, jobject lpObject, FORMATETC *lpStruct);
#endif /* NO_FORMATETC */

#ifndef NO_FUNCDESC1
FUNCDESC *getFUNCDESC1Fields(JNIEnv *env, jobject lpObject, FUNCDESC *lpStruct);
void setFUNCDESC1Fields(JNIEnv *env, jobject lpObject, FUNCDESC *lpStruct);
#endif /* NO_FUNCDESC1 */

#ifndef NO_FUNCDESC2
FUNCDESC *getFUNCDESC2Fields(JNIEnv *env, jobject lpObject, FUNCDESC *lpStruct);
void setFUNCDESC2Fields(JNIEnv *env, jobject lpObject, FUNCDESC *lpStruct);
#endif /* NO_FUNCDESC2 */

#ifndef NO_GUID
GUID *getGUIDFields(JNIEnv *env, jobject lpObject, GUID *lpStruct);
void setGUIDFields(JNIEnv *env, jobject lpObject, GUID *lpStruct);
#endif /* NO_GUID */

#ifndef NO_LICINFO
LICINFO *getLICINFOFields(JNIEnv *env, jobject lpObject, LICINFO *lpStruct);
void setLICINFOFields(JNIEnv *env, jobject lpObject, LICINFO *lpStruct);
#endif /* NO_LICINFO */

#ifndef NO_OLECMD
OLECMD *getOLECMDFields(JNIEnv *env, jobject lpObject, OLECMD *lpStruct);
void setOLECMDFields(JNIEnv *env, jobject lpObject, OLECMD *lpStruct);
#endif /* NO_OLECMD */

#ifndef NO_OLECMDTEXT
OLECMDTEXT *getOLECMDTEXTFields(JNIEnv *env, jobject lpObject, OLECMDTEXT *lpStruct);
void setOLECMDTEXTFields(JNIEnv *env, jobject lpObject, OLECMDTEXT *lpStruct);
#endif /* NO_OLECMDTEXT */

#ifndef NO_OLEINPLACEFRAMEINFO
OLEINPLACEFRAMEINFO *getOLEINPLACEFRAMEINFOFields(JNIEnv *env, jobject lpObject, OLEINPLACEFRAMEINFO *lpStruct);
void setOLEINPLACEFRAMEINFOFields(JNIEnv *env, jobject lpObject, OLEINPLACEFRAMEINFO *lpStruct);
#endif /* NO_OLEINPLACEFRAMEINFO */

#ifndef NO_STATSTG
STATSTG *getSTATSTGFields(JNIEnv *env, jobject lpObject, STATSTG *lpStruct);
void setSTATSTGFields(JNIEnv *env, jobject lpObject, STATSTG *lpStruct);
#endif /* NO_STATSTG */

#ifndef NO_STGMEDIUM
STGMEDIUM *getSTGMEDIUMFields(JNIEnv *env, jobject lpObject, STGMEDIUM *lpStruct);
void setSTGMEDIUMFields(JNIEnv *env, jobject lpObject, STGMEDIUM *lpStruct);
#endif /* NO_STGMEDIUM */

#ifndef NO_TYPEATTR
TYPEATTR *getTYPEATTRFields(JNIEnv *env, jobject lpObject, TYPEATTR *lpStruct);
void setTYPEATTRFields(JNIEnv *env, jobject lpObject, TYPEATTR *lpStruct);
#endif /* NO_TYPEATTR */

#ifndef NO_VARDESC1
VARDESC *getVARDESC1Fields(JNIEnv *env, jobject lpObject, VARDESC *lpStruct);
void setVARDESC1Fields(JNIEnv *env, jobject lpObject, VARDESC *lpStruct);
#endif /* NO_VARDESC1 */

#ifndef NO_VARDESC2
VARDESC *getVARDESC2Fields(JNIEnv *env, jobject lpObject, VARDESC *lpStruct);
void setVARDESC2Fields(JNIEnv *env, jobject lpObject, VARDESC *lpStruct);
#endif /* NO_VARDESC2 */

/**************************** END OLE ****************************/

#endif // INC_structs_H
