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

#include "os.h"

#ifndef NO_ACCEL
ACCEL *getACCELFields(JNIEnv *env, jobject lpObject, ACCEL *lpStruct);
void setACCELFields(JNIEnv *env, jobject lpObject, ACCEL *lpStruct);
#define ACCEL_sizeof() sizeof(ACCEL)
#else
#define getACCELFields(a,b,c) NULL
#define setACCELFields(a,b,c)
#define ACCEL_sizeof() 0
#endif

#ifndef NO_BITMAP
BITMAP *getBITMAPFields(JNIEnv *env, jobject lpObject, BITMAP *lpStruct);
void setBITMAPFields(JNIEnv *env, jobject lpObject, BITMAP *lpStruct);
#define BITMAP_sizeof() sizeof(BITMAP)
#else
#define getBITMAPFields(a,b,c) NULL
#define setBITMAPFields(a,b,c)
#define BITMAP_sizeof() 0
#endif

#ifndef NO_BITMAPINFOHEADER
BITMAPINFOHEADER *getBITMAPINFOHEADERFields(JNIEnv *env, jobject lpObject, BITMAPINFOHEADER *lpStruct);
void setBITMAPINFOHEADERFields(JNIEnv *env, jobject lpObject, BITMAPINFOHEADER *lpStruct);
#define BITMAPINFOHEADER_sizeof() sizeof(BITMAPINFOHEADER)
#else
#define getBITMAPINFOHEADERFields(a,b,c) NULL
#define setBITMAPINFOHEADERFields(a,b,c)
#define BITMAPINFOHEADER_sizeof() 0
#endif

#ifndef NO_BROWSEINFO
BROWSEINFO *getBROWSEINFOFields(JNIEnv *env, jobject lpObject, BROWSEINFO *lpStruct);
void setBROWSEINFOFields(JNIEnv *env, jobject lpObject, BROWSEINFO *lpStruct);
#define BROWSEINFO_sizeof() sizeof(BROWSEINFO)
#else
#define getBROWSEINFOFields(a,b,c) NULL
#define setBROWSEINFOFields(a,b,c)
#define BROWSEINFO_sizeof() 0
#endif

#ifndef NO_CHOOSECOLOR
CHOOSECOLOR *getCHOOSECOLORFields(JNIEnv *env, jobject lpObject, CHOOSECOLOR *lpStruct);
void setCHOOSECOLORFields(JNIEnv *env, jobject lpObject, CHOOSECOLOR *lpStruct);
#define CHOOSECOLOR_sizeof() sizeof(CHOOSECOLOR)
#else
#define getCHOOSECOLORFields(a,b,c) NULL
#define setCHOOSECOLORFields(a,b,c)
#define CHOOSECOLOR_sizeof() 0
#endif

#ifndef NO_CHOOSEFONT
CHOOSEFONT *getCHOOSEFONTFields(JNIEnv *env, jobject lpObject, CHOOSEFONT *lpStruct);
void setCHOOSEFONTFields(JNIEnv *env, jobject lpObject, CHOOSEFONT *lpStruct);
#define CHOOSEFONT_sizeof() sizeof(CHOOSEFONT)
#else
#define getCHOOSEFONTFields(a,b,c) NULL
#define setCHOOSEFONTFields(a,b,c)
#define CHOOSEFONT_sizeof() 0
#endif

#ifndef NO_COMBOBOXINFO
COMBOBOXINFO *getCOMBOBOXINFOFields(JNIEnv *env, jobject lpObject, COMBOBOXINFO *lpStruct);
void setCOMBOBOXINFOFields(JNIEnv *env, jobject lpObject, COMBOBOXINFO *lpStruct);
#define COMBOBOXINFO_sizeof() sizeof(COMBOBOXINFO)
#else
#define getCOMBOBOXINFOFields(a,b,c) NULL
#define setCOMBOBOXINFOFields(a,b,c)
#define COMBOBOXINFO_sizeof() 0
#endif

#ifndef NO_COMPOSITIONFORM
COMPOSITIONFORM *getCOMPOSITIONFORMFields(JNIEnv *env, jobject lpObject, COMPOSITIONFORM *lpStruct);
void setCOMPOSITIONFORMFields(JNIEnv *env, jobject lpObject, COMPOSITIONFORM *lpStruct);
#define COMPOSITIONFORM_sizeof() sizeof(COMPOSITIONFORM)
#else
#define getCOMPOSITIONFORMFields(a,b,c) NULL
#define setCOMPOSITIONFORMFields(a,b,c)
#define COMPOSITIONFORM_sizeof() 0
#endif

#ifndef NO_CREATESTRUCT
CREATESTRUCT *getCREATESTRUCTFields(JNIEnv *env, jobject lpObject, CREATESTRUCT *lpStruct);
void setCREATESTRUCTFields(JNIEnv *env, jobject lpObject, CREATESTRUCT *lpStruct);
#define CREATESTRUCT_sizeof() sizeof(CREATESTRUCT)
#else
#define getCREATESTRUCTFields(a,b,c) NULL
#define setCREATESTRUCTFields(a,b,c)
#define CREATESTRUCT_sizeof() 0
#endif

#ifndef NO_DIBSECTION
DIBSECTION *getDIBSECTIONFields(JNIEnv *env, jobject lpObject, DIBSECTION *lpStruct);
void setDIBSECTIONFields(JNIEnv *env, jobject lpObject, DIBSECTION *lpStruct);
#define DIBSECTION_sizeof() sizeof(DIBSECTION)
#else
#define getDIBSECTIONFields(a,b,c) NULL
#define setDIBSECTIONFields(a,b,c)
#define DIBSECTION_sizeof() 0
#endif

#ifndef NO_DLLVERSIONINFO
DLLVERSIONINFO *getDLLVERSIONINFOFields(JNIEnv *env, jobject lpObject, DLLVERSIONINFO *lpStruct);
void setDLLVERSIONINFOFields(JNIEnv *env, jobject lpObject, DLLVERSIONINFO *lpStruct);
#define DLLVERSIONINFO_sizeof() sizeof(DLLVERSIONINFO)
#else
#define getDLLVERSIONINFOFields(a,b,c) NULL
#define setDLLVERSIONINFOFields(a,b,c)
#define DLLVERSIONINFO_sizeof() 0
#endif

#ifndef NO_DOCINFO
DOCINFO *getDOCINFOFields(JNIEnv *env, jobject lpObject, DOCINFO *lpStruct);
void setDOCINFOFields(JNIEnv *env, jobject lpObject, DOCINFO *lpStruct);
#define DOCINFO_sizeof() sizeof(DOCINFO)
#else
#define getDOCINFOFields(a,b,c) NULL
#define setDOCINFOFields(a,b,c)
#define DOCINFO_sizeof() 0
#endif

#ifndef NO_DRAWITEMSTRUCT
DRAWITEMSTRUCT *getDRAWITEMSTRUCTFields(JNIEnv *env, jobject lpObject, DRAWITEMSTRUCT *lpStruct);
void setDRAWITEMSTRUCTFields(JNIEnv *env, jobject lpObject, DRAWITEMSTRUCT *lpStruct);
#define DRAWITEMSTRUCT_sizeof() sizeof(DRAWITEMSTRUCT)
#else
#define getDRAWITEMSTRUCTFields(a,b,c) NULL
#define setDRAWITEMSTRUCTFields(a,b,c)
#define DRAWITEMSTRUCT_sizeof() 0
#endif

#ifndef NO_DROPFILES
DROPFILES *getDROPFILESFields(JNIEnv *env, jobject lpObject, DROPFILES *lpStruct);
void setDROPFILESFields(JNIEnv *env, jobject lpObject, DROPFILES *lpStruct);
#define DROPFILES_sizeof() sizeof(DROPFILES)
#else
#define getDROPFILESFields(a,b,c) NULL
#define setDROPFILESFields(a,b,c)
#define DROPFILES_sizeof() 0
#endif

#ifndef NO_FILETIME
FILETIME *getFILETIMEFields(JNIEnv *env, jobject lpObject, FILETIME *lpStruct);
void setFILETIMEFields(JNIEnv *env, jobject lpObject, FILETIME *lpStruct);
#define FILETIME_sizeof() sizeof(FILETIME)
#else
#define getFILETIMEFields(a,b,c) NULL
#define setFILETIMEFields(a,b,c)
#define FILETIME_sizeof() 0
#endif

#ifndef NO_GCP_RESULTS
GCP_RESULTS *getGCP_RESULTSFields(JNIEnv *env, jobject lpObject, GCP_RESULTS *lpStruct);
void setGCP_RESULTSFields(JNIEnv *env, jobject lpObject, GCP_RESULTS *lpStruct);
#define GCP_RESULTS_sizeof() sizeof(GCP_RESULTS)
#else
#define getGCP_RESULTSFields(a,b,c) NULL
#define setGCP_RESULTSFields(a,b,c)
#define GCP_RESULTS_sizeof() 0
#endif

#ifndef NO_GRADIENT_RECT
GRADIENT_RECT *getGRADIENT_RECTFields(JNIEnv *env, jobject lpObject, GRADIENT_RECT *lpStruct);
void setGRADIENT_RECTFields(JNIEnv *env, jobject lpObject, GRADIENT_RECT *lpStruct);
#define GRADIENT_RECT_sizeof() sizeof(GRADIENT_RECT)
#else
#define getGRADIENT_RECTFields(a,b,c) NULL
#define setGRADIENT_RECTFields(a,b,c)
#define GRADIENT_RECT_sizeof() 0
#endif

#ifndef NO_HDITEM
HDITEM *getHDITEMFields(JNIEnv *env, jobject lpObject, HDITEM *lpStruct);
void setHDITEMFields(JNIEnv *env, jobject lpObject, HDITEM *lpStruct);
#define HDITEM_sizeof() sizeof(HDITEM)
#else
#define getHDITEMFields(a,b,c) NULL
#define setHDITEMFields(a,b,c)
#define HDITEM_sizeof() 0
#endif

#ifndef NO_HELPINFO
HELPINFO *getHELPINFOFields(JNIEnv *env, jobject lpObject, HELPINFO *lpStruct);
void setHELPINFOFields(JNIEnv *env, jobject lpObject, HELPINFO *lpStruct);
#define HELPINFO_sizeof() sizeof(HELPINFO)
#else
#define getHELPINFOFields(a,b,c) NULL
#define setHELPINFOFields(a,b,c)
#define HELPINFO_sizeof() 0
#endif

#ifndef NO_HIGHCONTRAST
HIGHCONTRAST *getHIGHCONTRASTFields(JNIEnv *env, jobject lpObject, HIGHCONTRAST *lpStruct);
void setHIGHCONTRASTFields(JNIEnv *env, jobject lpObject, HIGHCONTRAST *lpStruct);
#define HIGHCONTRAST_sizeof() sizeof(HIGHCONTRAST)
#else
#define getHIGHCONTRASTFields(a,b,c) NULL
#define setHIGHCONTRASTFields(a,b,c)
#define HIGHCONTRAST_sizeof() 0
#endif

#ifndef NO_ICONINFO
ICONINFO *getICONINFOFields(JNIEnv *env, jobject lpObject, ICONINFO *lpStruct);
void setICONINFOFields(JNIEnv *env, jobject lpObject, ICONINFO *lpStruct);
#define ICONINFO_sizeof() sizeof(ICONINFO)
#else
#define getICONINFOFields(a,b,c) NULL
#define setICONINFOFields(a,b,c)
#define ICONINFO_sizeof() 0
#endif

#ifndef NO_INITCOMMONCONTROLSEX
INITCOMMONCONTROLSEX *getINITCOMMONCONTROLSEXFields(JNIEnv *env, jobject lpObject, INITCOMMONCONTROLSEX *lpStruct);
void setINITCOMMONCONTROLSEXFields(JNIEnv *env, jobject lpObject, INITCOMMONCONTROLSEX *lpStruct);
#define INITCOMMONCONTROLSEX_sizeof() sizeof(INITCOMMONCONTROLSEX)
#else
#define getINITCOMMONCONTROLSEXFields(a,b,c) NULL
#define setINITCOMMONCONTROLSEXFields(a,b,c)
#define INITCOMMONCONTROLSEX_sizeof() 0
#endif

#ifndef NO_INPUT
INPUT *getINPUTFields(JNIEnv *env, jobject lpObject, INPUT *lpStruct);
void setINPUTFields(JNIEnv *env, jobject lpObject, INPUT *lpStruct);
#define INPUT_sizeof() sizeof(INPUT)
#else
#define getINPUTFields(a,b,c) NULL
#define setINPUTFields(a,b,c)
#define INPUT_sizeof() 0
#endif

#ifndef NO_KEYBDINPUT
KEYBDINPUT *getKEYBDINPUTFields(JNIEnv *env, jobject lpObject, KEYBDINPUT *lpStruct);
void setKEYBDINPUTFields(JNIEnv *env, jobject lpObject, KEYBDINPUT *lpStruct);
#define KEYBDINPUT_sizeof() sizeof(KEYBDINPUT)
#else
#define getKEYBDINPUTFields(a,b,c) NULL
#define setKEYBDINPUTFields(a,b,c)
#define KEYBDINPUT_sizeof() 0
#endif

#ifndef NO_LOGBRUSH
LOGBRUSH *getLOGBRUSHFields(JNIEnv *env, jobject lpObject, LOGBRUSH *lpStruct);
void setLOGBRUSHFields(JNIEnv *env, jobject lpObject, LOGBRUSH *lpStruct);
#define LOGBRUSH_sizeof() sizeof(LOGBRUSH)
#else
#define getLOGBRUSHFields(a,b,c) NULL
#define setLOGBRUSHFields(a,b,c)
#define LOGBRUSH_sizeof() 0
#endif

#ifndef NO_LOGFONT
LOGFONT *getLOGFONTFields(JNIEnv *env, jobject lpObject, LOGFONT *lpStruct);
void setLOGFONTFields(JNIEnv *env, jobject lpObject, LOGFONT *lpStruct);
#define LOGFONT_sizeof() sizeof(LOGFONT)
#else
#define getLOGFONTFields(a,b,c) NULL
#define setLOGFONTFields(a,b,c)
#define LOGFONT_sizeof() 0
#endif

#ifndef NO_LOGFONTA
LOGFONTA *getLOGFONTAFields(JNIEnv *env, jobject lpObject, LOGFONTA *lpStruct);
void setLOGFONTAFields(JNIEnv *env, jobject lpObject, LOGFONTA *lpStruct);
#define LOGFONTA_sizeof() sizeof(LOGFONTA)
#else
#define getLOGFONTAFields(a,b,c) NULL
#define setLOGFONTAFields(a,b,c)
#define LOGFONTA_sizeof() 0
#endif

#ifndef NO_LOGFONTW
LOGFONTW *getLOGFONTWFields(JNIEnv *env, jobject lpObject, LOGFONTW *lpStruct);
void setLOGFONTWFields(JNIEnv *env, jobject lpObject, LOGFONTW *lpStruct);
#define LOGFONTW_sizeof() sizeof(LOGFONTW)
#else
#define getLOGFONTWFields(a,b,c) NULL
#define setLOGFONTWFields(a,b,c)
#define LOGFONTW_sizeof() 0
#endif

#ifndef NO_LOGPEN
LOGPEN *getLOGPENFields(JNIEnv *env, jobject lpObject, LOGPEN *lpStruct);
void setLOGPENFields(JNIEnv *env, jobject lpObject, LOGPEN *lpStruct);
#define LOGPEN_sizeof() sizeof(LOGPEN)
#else
#define getLOGPENFields(a,b,c) NULL
#define setLOGPENFields(a,b,c)
#define LOGPEN_sizeof() 0
#endif

#ifndef NO_LVCOLUMN
LVCOLUMN *getLVCOLUMNFields(JNIEnv *env, jobject lpObject, LVCOLUMN *lpStruct);
void setLVCOLUMNFields(JNIEnv *env, jobject lpObject, LVCOLUMN *lpStruct);
#define LVCOLUMN_sizeof() sizeof(LVCOLUMN)
#else
#define getLVCOLUMNFields(a,b,c) NULL
#define setLVCOLUMNFields(a,b,c)
#define LVCOLUMN_sizeof() 0
#endif

#ifndef NO_LVHITTESTINFO
LVHITTESTINFO *getLVHITTESTINFOFields(JNIEnv *env, jobject lpObject, LVHITTESTINFO *lpStruct);
void setLVHITTESTINFOFields(JNIEnv *env, jobject lpObject, LVHITTESTINFO *lpStruct);
#define LVHITTESTINFO_sizeof() sizeof(LVHITTESTINFO)
#else
#define getLVHITTESTINFOFields(a,b,c) NULL
#define setLVHITTESTINFOFields(a,b,c)
#define LVHITTESTINFO_sizeof() 0
#endif

#ifndef NO_LVITEM
LVITEM *getLVITEMFields(JNIEnv *env, jobject lpObject, LVITEM *lpStruct);
void setLVITEMFields(JNIEnv *env, jobject lpObject, LVITEM *lpStruct);
#define LVITEM_sizeof() sizeof(LVITEM)
#else
#define getLVITEMFields(a,b,c) NULL
#define setLVITEMFields(a,b,c)
#define LVITEM_sizeof() 0
#endif

#ifndef NO_MEASUREITEMSTRUCT
MEASUREITEMSTRUCT *getMEASUREITEMSTRUCTFields(JNIEnv *env, jobject lpObject, MEASUREITEMSTRUCT *lpStruct);
void setMEASUREITEMSTRUCTFields(JNIEnv *env, jobject lpObject, MEASUREITEMSTRUCT *lpStruct);
#define MEASUREITEMSTRUCT_sizeof() sizeof(MEASUREITEMSTRUCT)
#else
#define getMEASUREITEMSTRUCTFields(a,b,c) NULL
#define setMEASUREITEMSTRUCTFields(a,b,c)
#define MEASUREITEMSTRUCT_sizeof() 0
#endif

#ifndef NO_MENUBARINFO
MENUBARINFO *getMENUBARINFOFields(JNIEnv *env, jobject lpObject, MENUBARINFO *lpStruct);
void setMENUBARINFOFields(JNIEnv *env, jobject lpObject, MENUBARINFO *lpStruct);
#define MENUBARINFO_sizeof() sizeof(MENUBARINFO)
#else
#define getMENUBARINFOFields(a,b,c) NULL
#define setMENUBARINFOFields(a,b,c)
#define MENUBARINFO_sizeof() 0
#endif

#ifndef NO_MENUINFO
MENUINFO *getMENUINFOFields(JNIEnv *env, jobject lpObject, MENUINFO *lpStruct);
void setMENUINFOFields(JNIEnv *env, jobject lpObject, MENUINFO *lpStruct);
#define MENUINFO_sizeof() sizeof(MENUINFO)
#else
#define getMENUINFOFields(a,b,c) NULL
#define setMENUINFOFields(a,b,c)
#define MENUINFO_sizeof() 0
#endif

#ifndef NO_MENUITEMINFO
MENUITEMINFO *getMENUITEMINFOFields(JNIEnv *env, jobject lpObject, MENUITEMINFO *lpStruct);
void setMENUITEMINFOFields(JNIEnv *env, jobject lpObject, MENUITEMINFO *lpStruct);
#define MENUITEMINFO_sizeof() sizeof(MENUITEMINFO)
#else
#define getMENUITEMINFOFields(a,b,c) NULL
#define setMENUITEMINFOFields(a,b,c)
#define MENUITEMINFO_sizeof() 0
#endif

#ifndef NO_MONITORINFO
MONITORINFO *getMONITORINFOFields(JNIEnv *env, jobject lpObject, MONITORINFO *lpStruct);
void setMONITORINFOFields(JNIEnv *env, jobject lpObject, MONITORINFO *lpStruct);
#define MONITORINFO_sizeof() sizeof(MONITORINFO)
#else
#define getMONITORINFOFields(a,b,c) NULL
#define setMONITORINFOFields(a,b,c)
#define MONITORINFO_sizeof() 0
#endif

#ifndef NO_MOUSEINPUT
MOUSEINPUT *getMOUSEINPUTFields(JNIEnv *env, jobject lpObject, MOUSEINPUT *lpStruct);
void setMOUSEINPUTFields(JNIEnv *env, jobject lpObject, MOUSEINPUT *lpStruct);
#define MOUSEINPUT_sizeof() sizeof(MOUSEINPUT)
#else
#define getMOUSEINPUTFields(a,b,c) NULL
#define setMOUSEINPUTFields(a,b,c)
#define MOUSEINPUT_sizeof() 0
#endif

#ifndef NO_MSG
MSG *getMSGFields(JNIEnv *env, jobject lpObject, MSG *lpStruct);
void setMSGFields(JNIEnv *env, jobject lpObject, MSG *lpStruct);
#define MSG_sizeof() sizeof(MSG)
#else
#define getMSGFields(a,b,c) NULL
#define setMSGFields(a,b,c)
#define MSG_sizeof() 0
#endif

#ifndef NO_NMHDR
NMHDR *getNMHDRFields(JNIEnv *env, jobject lpObject, NMHDR *lpStruct);
void setNMHDRFields(JNIEnv *env, jobject lpObject, NMHDR *lpStruct);
#define NMHDR_sizeof() sizeof(NMHDR)
#else
#define getNMHDRFields(a,b,c) NULL
#define setNMHDRFields(a,b,c)
#define NMHDR_sizeof() 0
#endif

#ifndef NO_NMCUSTOMDRAW
NMCUSTOMDRAW *getNMCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMCUSTOMDRAW *lpStruct);
void setNMCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMCUSTOMDRAW *lpStruct);
#define NMCUSTOMDRAW_sizeof() sizeof(NMCUSTOMDRAW)
#else
#define getNMCUSTOMDRAWFields(a,b,c) NULL
#define setNMCUSTOMDRAWFields(a,b,c)
#define NMCUSTOMDRAW_sizeof() 0
#endif

#ifndef NO_NMHEADER
NMHEADER *getNMHEADERFields(JNIEnv *env, jobject lpObject, NMHEADER *lpStruct);
void setNMHEADERFields(JNIEnv *env, jobject lpObject, NMHEADER *lpStruct);
#define NMHEADER_sizeof() sizeof(NMHEADER)
#else
#define getNMHEADERFields(a,b,c) NULL
#define setNMHEADERFields(a,b,c)
#define NMHEADER_sizeof() 0
#endif

#ifndef NO_NMLISTVIEW
NMLISTVIEW *getNMLISTVIEWFields(JNIEnv *env, jobject lpObject, NMLISTVIEW *lpStruct);
void setNMLISTVIEWFields(JNIEnv *env, jobject lpObject, NMLISTVIEW *lpStruct);
#define NMLISTVIEW_sizeof() sizeof(NMLISTVIEW)
#else
#define getNMLISTVIEWFields(a,b,c) NULL
#define setNMLISTVIEWFields(a,b,c)
#define NMLISTVIEW_sizeof() 0
#endif

#ifndef NO_NMLVCUSTOMDRAW
NMLVCUSTOMDRAW *getNMLVCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMLVCUSTOMDRAW *lpStruct);
void setNMLVCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMLVCUSTOMDRAW *lpStruct);
#define NMLVCUSTOMDRAW_sizeof() sizeof(NMLVCUSTOMDRAW)
#else
#define getNMLVCUSTOMDRAWFields(a,b,c) NULL
#define setNMLVCUSTOMDRAWFields(a,b,c)
#define NMLVCUSTOMDRAW_sizeof() 0
#endif

#ifndef NO_NMLVDISPINFO
NMLVDISPINFO *getNMLVDISPINFOFields(JNIEnv *env, jobject lpObject, NMLVDISPINFO *lpStruct);
void setNMLVDISPINFOFields(JNIEnv *env, jobject lpObject, NMLVDISPINFO *lpStruct);
#define NMLVDISPINFO_sizeof() sizeof(NMLVDISPINFO)
#else
#define getNMLVDISPINFOFields(a,b,c) NULL
#define setNMLVDISPINFOFields(a,b,c)
#define NMLVDISPINFO_sizeof() 0
#endif

#ifndef NO_NMLVFINDITEM
NMLVFINDITEM *getNMLVFINDITEMFields(JNIEnv *env, jobject lpObject, NMLVFINDITEM *lpStruct);
void setNMLVFINDITEMFields(JNIEnv *env, jobject lpObject, NMLVFINDITEM *lpStruct);
#define NMLVFINDITEM_sizeof() sizeof(NMLVFINDITEM)
#else
#define getNMLVFINDITEMFields(a,b,c) NULL
#define setNMLVFINDITEMFields(a,b,c)
#define NMLVFINDITEM_sizeof() 0
#endif

#ifndef NO_NMREBARCHEVRON
NMREBARCHEVRON *getNMREBARCHEVRONFields(JNIEnv *env, jobject lpObject, NMREBARCHEVRON *lpStruct);
void setNMREBARCHEVRONFields(JNIEnv *env, jobject lpObject, NMREBARCHEVRON *lpStruct);
#define NMREBARCHEVRON_sizeof() sizeof(NMREBARCHEVRON)
#else
#define getNMREBARCHEVRONFields(a,b,c) NULL
#define setNMREBARCHEVRONFields(a,b,c)
#define NMREBARCHEVRON_sizeof() 0
#endif

#ifndef NO_NMRGINFO
NMRGINFO *getNMRGINFOFields(JNIEnv *env, jobject lpObject, NMRGINFO *lpStruct);
void setNMRGINFOFields(JNIEnv *env, jobject lpObject, NMRGINFO *lpStruct);
#define NMRGINFO_sizeof() sizeof(NMRGINFO)
#else
#define getNMRGINFOFields(a,b,c) NULL
#define setNMRGINFOFields(a,b,c)
#define NMRGINFO_sizeof() 0
#endif

#ifndef NO_NMTOOLBAR
NMTOOLBAR *getNMTOOLBARFields(JNIEnv *env, jobject lpObject, NMTOOLBAR *lpStruct);
void setNMTOOLBARFields(JNIEnv *env, jobject lpObject, NMTOOLBAR *lpStruct);
#define NMTOOLBAR_sizeof() sizeof(NMTOOLBAR)
#else
#define getNMTOOLBARFields(a,b,c) NULL
#define setNMTOOLBARFields(a,b,c)
#define NMTOOLBAR_sizeof() 0
#endif

#ifndef NO_NMTTDISPINFO
NMTTDISPINFO *getNMTTDISPINFOFields(JNIEnv *env, jobject lpObject, NMTTDISPINFO *lpStruct);
void setNMTTDISPINFOFields(JNIEnv *env, jobject lpObject, NMTTDISPINFO *lpStruct);
#define NMTTDISPINFO_sizeof() sizeof(NMTTDISPINFO)
#else
#define getNMTTDISPINFOFields(a,b,c) NULL
#define setNMTTDISPINFOFields(a,b,c)
#define NMTTDISPINFO_sizeof() 0
#endif

#ifndef NO_NMTTDISPINFOA
NMTTDISPINFOA *getNMTTDISPINFOAFields(JNIEnv *env, jobject lpObject, NMTTDISPINFOA *lpStruct);
void setNMTTDISPINFOAFields(JNIEnv *env, jobject lpObject, NMTTDISPINFOA *lpStruct);
#define NMTTDISPINFOA_sizeof() sizeof(NMTTDISPINFOA)
#else
#define getNMTTDISPINFOAFields(a,b,c) NULL
#define setNMTTDISPINFOAFields(a,b,c)
#define NMTTDISPINFOA_sizeof() 0
#endif

#ifndef NO_NMTTDISPINFOW
NMTTDISPINFOW *getNMTTDISPINFOWFields(JNIEnv *env, jobject lpObject, NMTTDISPINFOW *lpStruct);
void setNMTTDISPINFOWFields(JNIEnv *env, jobject lpObject, NMTTDISPINFOW *lpStruct);
#define NMTTDISPINFOW_sizeof() sizeof(NMTTDISPINFOW)
#else
#define getNMTTDISPINFOWFields(a,b,c) NULL
#define setNMTTDISPINFOWFields(a,b,c)
#define NMTTDISPINFOW_sizeof() 0
#endif

#ifndef NO_NMTVCUSTOMDRAW
NMTVCUSTOMDRAW *getNMTVCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMTVCUSTOMDRAW *lpStruct);
void setNMTVCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMTVCUSTOMDRAW *lpStruct);
#define NMTVCUSTOMDRAW_sizeof() sizeof(NMTVCUSTOMDRAW)
#else
#define getNMTVCUSTOMDRAWFields(a,b,c) NULL
#define setNMTVCUSTOMDRAWFields(a,b,c)
#define NMTVCUSTOMDRAW_sizeof() 0
#endif

#ifndef NO_NONCLIENTMETRICS
NONCLIENTMETRICS *getNONCLIENTMETRICSFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICS *lpStruct);
void setNONCLIENTMETRICSFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICS *lpStruct);
#define NONCLIENTMETRICS_sizeof() sizeof(NONCLIENTMETRICS)
#else
#define getNONCLIENTMETRICSFields(a,b,c) NULL
#define setNONCLIENTMETRICSFields(a,b,c)
#define NONCLIENTMETRICS_sizeof() 0
#endif

#ifndef NO_NONCLIENTMETRICSA
NONCLIENTMETRICSA *getNONCLIENTMETRICSAFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICSA *lpStruct);
void setNONCLIENTMETRICSAFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICSA *lpStruct);
#define NONCLIENTMETRICSA_sizeof() sizeof(NONCLIENTMETRICSA)
#else
#define getNONCLIENTMETRICSAFields(a,b,c) NULL
#define setNONCLIENTMETRICSAFields(a,b,c)
#define NONCLIENTMETRICSA_sizeof() 0
#endif

#ifndef NO_NONCLIENTMETRICSW
NONCLIENTMETRICSW *getNONCLIENTMETRICSWFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICSW *lpStruct);
void setNONCLIENTMETRICSWFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICSW *lpStruct);
#define NONCLIENTMETRICSW_sizeof() sizeof(NONCLIENTMETRICSW)
#else
#define getNONCLIENTMETRICSWFields(a,b,c) NULL
#define setNONCLIENTMETRICSWFields(a,b,c)
#define NONCLIENTMETRICSW_sizeof() 0
#endif

#ifndef NO_NOTIFYICONDATA
NOTIFYICONDATA *getNOTIFYICONDATAFields(JNIEnv *env, jobject lpObject, NOTIFYICONDATA *lpStruct);
void setNOTIFYICONDATAFields(JNIEnv *env, jobject lpObject, NOTIFYICONDATA *lpStruct);
#define NOTIFYICONDATA_sizeof() sizeof(NOTIFYICONDATA)
#else
#define getNOTIFYICONDATAFields(a,b,c) NULL
#define setNOTIFYICONDATAFields(a,b,c)
#define NOTIFYICONDATA_sizeof() 0
#endif

#ifndef NO_NOTIFYICONDATAA
NOTIFYICONDATAA *getNOTIFYICONDATAAFields(JNIEnv *env, jobject lpObject, NOTIFYICONDATAA *lpStruct);
void setNOTIFYICONDATAAFields(JNIEnv *env, jobject lpObject, NOTIFYICONDATAA *lpStruct);
#define NOTIFYICONDATAA_sizeof() sizeof(NOTIFYICONDATAA)
#else
#define getNOTIFYICONDATAAFields(a,b,c) NULL
#define setNOTIFYICONDATAAFields(a,b,c)
#define NOTIFYICONDATAA_sizeof() 0
#endif

#ifndef NO_NOTIFYICONDATAW
NOTIFYICONDATAW *getNOTIFYICONDATAWFields(JNIEnv *env, jobject lpObject, NOTIFYICONDATAW *lpStruct);
void setNOTIFYICONDATAWFields(JNIEnv *env, jobject lpObject, NOTIFYICONDATAW *lpStruct);
#define NOTIFYICONDATAW_sizeof() sizeof(NOTIFYICONDATAW)
#else
#define getNOTIFYICONDATAWFields(a,b,c) NULL
#define setNOTIFYICONDATAWFields(a,b,c)
#define NOTIFYICONDATAW_sizeof() 0
#endif

#ifndef NO_OPENFILENAME
OPENFILENAME *getOPENFILENAMEFields(JNIEnv *env, jobject lpObject, OPENFILENAME *lpStruct);
void setOPENFILENAMEFields(JNIEnv *env, jobject lpObject, OPENFILENAME *lpStruct);
#define OPENFILENAME_sizeof() sizeof(OPENFILENAME)
#else
#define getOPENFILENAMEFields(a,b,c) NULL
#define setOPENFILENAMEFields(a,b,c)
#define OPENFILENAME_sizeof() 0
#endif

#ifndef NO_OSVERSIONINFO
OSVERSIONINFO *getOSVERSIONINFOFields(JNIEnv *env, jobject lpObject, OSVERSIONINFO *lpStruct);
void setOSVERSIONINFOFields(JNIEnv *env, jobject lpObject, OSVERSIONINFO *lpStruct);
#define OSVERSIONINFO_sizeof() sizeof(OSVERSIONINFO)
#else
#define getOSVERSIONINFOFields(a,b,c) NULL
#define setOSVERSIONINFOFields(a,b,c)
#define OSVERSIONINFO_sizeof() 0
#endif

#ifndef NO_OSVERSIONINFOA
OSVERSIONINFOA *getOSVERSIONINFOAFields(JNIEnv *env, jobject lpObject, OSVERSIONINFOA *lpStruct);
void setOSVERSIONINFOAFields(JNIEnv *env, jobject lpObject, OSVERSIONINFOA *lpStruct);
#define OSVERSIONINFOA_sizeof() sizeof(OSVERSIONINFOA)
#else
#define getOSVERSIONINFOAFields(a,b,c) NULL
#define setOSVERSIONINFOAFields(a,b,c)
#define OSVERSIONINFOA_sizeof() 0
#endif

#ifndef NO_OSVERSIONINFOW
OSVERSIONINFOW *getOSVERSIONINFOWFields(JNIEnv *env, jobject lpObject, OSVERSIONINFOW *lpStruct);
void setOSVERSIONINFOWFields(JNIEnv *env, jobject lpObject, OSVERSIONINFOW *lpStruct);
#define OSVERSIONINFOW_sizeof() sizeof(OSVERSIONINFOW)
#else
#define getOSVERSIONINFOWFields(a,b,c) NULL
#define setOSVERSIONINFOWFields(a,b,c)
#define OSVERSIONINFOW_sizeof() 0
#endif

#ifndef NO_PAINTSTRUCT
PAINTSTRUCT *getPAINTSTRUCTFields(JNIEnv *env, jobject lpObject, PAINTSTRUCT *lpStruct);
void setPAINTSTRUCTFields(JNIEnv *env, jobject lpObject, PAINTSTRUCT *lpStruct);
#define PAINTSTRUCT_sizeof() sizeof(PAINTSTRUCT)
#else
#define getPAINTSTRUCTFields(a,b,c) NULL
#define setPAINTSTRUCTFields(a,b,c)
#define PAINTSTRUCT_sizeof() 0
#endif

#ifndef NO_POINT
POINT *getPOINTFields(JNIEnv *env, jobject lpObject, POINT *lpStruct);
void setPOINTFields(JNIEnv *env, jobject lpObject, POINT *lpStruct);
#define POINT_sizeof() sizeof(POINT)
#else
#define getPOINTFields(a,b,c) NULL
#define setPOINTFields(a,b,c)
#define POINT_sizeof() 0
#endif

#ifndef NO_PRINTDLG
PRINTDLG *getPRINTDLGFields(JNIEnv *env, jobject lpObject, PRINTDLG *lpStruct);
void setPRINTDLGFields(JNIEnv *env, jobject lpObject, PRINTDLG *lpStruct);
#define PRINTDLG_sizeof() sizeof(PRINTDLG)
#else
#define getPRINTDLGFields(a,b,c) NULL
#define setPRINTDLGFields(a,b,c)
#define PRINTDLG_sizeof() 0
#endif

#ifndef NO_REBARBANDINFO
REBARBANDINFO *getREBARBANDINFOFields(JNIEnv *env, jobject lpObject, REBARBANDINFO *lpStruct);
void setREBARBANDINFOFields(JNIEnv *env, jobject lpObject, REBARBANDINFO *lpStruct);
#define REBARBANDINFO_sizeof() sizeof(REBARBANDINFO)
#else
#define getREBARBANDINFOFields(a,b,c) NULL
#define setREBARBANDINFOFields(a,b,c)
#define REBARBANDINFO_sizeof() 0
#endif

#ifndef NO_RECT
RECT *getRECTFields(JNIEnv *env, jobject lpObject, RECT *lpStruct);
void setRECTFields(JNIEnv *env, jobject lpObject, RECT *lpStruct);
#define RECT_sizeof() sizeof(RECT)
#else
#define getRECTFields(a,b,c) NULL
#define setRECTFields(a,b,c)
#define RECT_sizeof() 0
#endif

#ifndef NO_SCRIPT_ANALYSIS
SCRIPT_ANALYSIS *getSCRIPT_ANALYSISFields(JNIEnv *env, jobject lpObject, SCRIPT_ANALYSIS *lpStruct);
void setSCRIPT_ANALYSISFields(JNIEnv *env, jobject lpObject, SCRIPT_ANALYSIS *lpStruct);
#define SCRIPT_ANALYSIS_sizeof() sizeof(SCRIPT_ANALYSIS)
#else
#define getSCRIPT_ANALYSISFields(a,b,c) NULL
#define setSCRIPT_ANALYSISFields(a,b,c)
#define SCRIPT_ANALYSIS_sizeof() 0
#endif

#ifndef NO_SCRIPT_CONTROL
SCRIPT_CONTROL *getSCRIPT_CONTROLFields(JNIEnv *env, jobject lpObject, SCRIPT_CONTROL *lpStruct);
void setSCRIPT_CONTROLFields(JNIEnv *env, jobject lpObject, SCRIPT_CONTROL *lpStruct);
#define SCRIPT_CONTROL_sizeof() sizeof(SCRIPT_CONTROL)
#else
#define getSCRIPT_CONTROLFields(a,b,c) NULL
#define setSCRIPT_CONTROLFields(a,b,c)
#define SCRIPT_CONTROL_sizeof() 0
#endif

#ifndef NO_SCRIPT_ITEM
SCRIPT_ITEM *getSCRIPT_ITEMFields(JNIEnv *env, jobject lpObject, SCRIPT_ITEM *lpStruct);
void setSCRIPT_ITEMFields(JNIEnv *env, jobject lpObject, SCRIPT_ITEM *lpStruct);
#define SCRIPT_ITEM_sizeof() sizeof(SCRIPT_ITEM)
#else
#define getSCRIPT_ITEMFields(a,b,c) NULL
#define setSCRIPT_ITEMFields(a,b,c)
#define SCRIPT_ITEM_sizeof() 0
#endif

#ifndef NO_SCRIPT_LOGATTR
SCRIPT_LOGATTR *getSCRIPT_LOGATTRFields(JNIEnv *env, jobject lpObject, SCRIPT_LOGATTR *lpStruct);
void setSCRIPT_LOGATTRFields(JNIEnv *env, jobject lpObject, SCRIPT_LOGATTR *lpStruct);
#define SCRIPT_LOGATTR_sizeof() sizeof(SCRIPT_LOGATTR)
#else
#define getSCRIPT_LOGATTRFields(a,b,c) NULL
#define setSCRIPT_LOGATTRFields(a,b,c)
#define SCRIPT_LOGATTR_sizeof() 0
#endif

#ifndef NO_SCRIPT_STATE
SCRIPT_STATE *getSCRIPT_STATEFields(JNIEnv *env, jobject lpObject, SCRIPT_STATE *lpStruct);
void setSCRIPT_STATEFields(JNIEnv *env, jobject lpObject, SCRIPT_STATE *lpStruct);
#define SCRIPT_STATE_sizeof() sizeof(SCRIPT_STATE)
#else
#define getSCRIPT_STATEFields(a,b,c) NULL
#define setSCRIPT_STATEFields(a,b,c)
#define SCRIPT_STATE_sizeof() 0
#endif

#ifndef NO_SCROLLINFO
SCROLLINFO *getSCROLLINFOFields(JNIEnv *env, jobject lpObject, SCROLLINFO *lpStruct);
void setSCROLLINFOFields(JNIEnv *env, jobject lpObject, SCROLLINFO *lpStruct);
#define SCROLLINFO_sizeof() sizeof(SCROLLINFO)
#else
#define getSCROLLINFOFields(a,b,c) NULL
#define setSCROLLINFOFields(a,b,c)
#define SCROLLINFO_sizeof() 0
#endif

#ifndef NO_SHACTIVATEINFO
SHACTIVATEINFO *getSHACTIVATEINFOFields(JNIEnv *env, jobject lpObject, SHACTIVATEINFO *lpStruct);
void setSHACTIVATEINFOFields(JNIEnv *env, jobject lpObject, SHACTIVATEINFO *lpStruct);
#define SHACTIVATEINFO_sizeof() sizeof(SHACTIVATEINFO)
#else
#define getSHACTIVATEINFOFields(a,b,c) NULL
#define setSHACTIVATEINFOFields(a,b,c)
#define SHACTIVATEINFO_sizeof() 0
#endif

#ifndef NO_SHELLEXECUTEINFO
SHELLEXECUTEINFO *getSHELLEXECUTEINFOFields(JNIEnv *env, jobject lpObject, SHELLEXECUTEINFO *lpStruct);
void setSHELLEXECUTEINFOFields(JNIEnv *env, jobject lpObject, SHELLEXECUTEINFO *lpStruct);
#define SHELLEXECUTEINFO_sizeof() sizeof(SHELLEXECUTEINFO)
#else
#define getSHELLEXECUTEINFOFields(a,b,c) NULL
#define setSHELLEXECUTEINFOFields(a,b,c)
#define SHELLEXECUTEINFO_sizeof() 0
#endif

#ifndef NO_SHMENUBARINFO
SHMENUBARINFO *getSHMENUBARINFOFields(JNIEnv *env, jobject lpObject, SHMENUBARINFO *lpStruct);
void setSHMENUBARINFOFields(JNIEnv *env, jobject lpObject, SHMENUBARINFO *lpStruct);
#define SHMENUBARINFO_sizeof() sizeof(SHMENUBARINFO)
#else
#define getSHMENUBARINFOFields(a,b,c) NULL
#define setSHMENUBARINFOFields(a,b,c)
#define SHMENUBARINFO_sizeof() 0
#endif

#ifndef NO_SHRGINFO
SHRGINFO *getSHRGINFOFields(JNIEnv *env, jobject lpObject, SHRGINFO *lpStruct);
void setSHRGINFOFields(JNIEnv *env, jobject lpObject, SHRGINFO *lpStruct);
#define SHRGINFO_sizeof() sizeof(SHRGINFO)
#else
#define getSHRGINFOFields(a,b,c) NULL
#define setSHRGINFOFields(a,b,c)
#define SHRGINFO_sizeof() 0
#endif

#ifndef NO_SIPINFO
SIPINFO *getSIPINFOFields(JNIEnv *env, jobject lpObject, SIPINFO *lpStruct);
void setSIPINFOFields(JNIEnv *env, jobject lpObject, SIPINFO *lpStruct);
#define SIPINFO_sizeof() sizeof(SIPINFO)
#else
#define getSIPINFOFields(a,b,c) NULL
#define setSIPINFOFields(a,b,c)
#define SIPINFO_sizeof() 0
#endif

#ifndef NO_SIZE
SIZE *getSIZEFields(JNIEnv *env, jobject lpObject, SIZE *lpStruct);
void setSIZEFields(JNIEnv *env, jobject lpObject, SIZE *lpStruct);
#define SIZE_sizeof() sizeof(SIZE)
#else
#define getSIZEFields(a,b,c) NULL
#define setSIZEFields(a,b,c)
#define SIZE_sizeof() 0
#endif

#ifndef NO_TBBUTTON
TBBUTTON *getTBBUTTONFields(JNIEnv *env, jobject lpObject, TBBUTTON *lpStruct);
void setTBBUTTONFields(JNIEnv *env, jobject lpObject, TBBUTTON *lpStruct);
#define TBBUTTON_sizeof() sizeof(TBBUTTON)
#else
#define getTBBUTTONFields(a,b,c) NULL
#define setTBBUTTONFields(a,b,c)
#define TBBUTTON_sizeof() 0
#endif

#ifndef NO_TBBUTTONINFO
TBBUTTONINFO *getTBBUTTONINFOFields(JNIEnv *env, jobject lpObject, TBBUTTONINFO *lpStruct);
void setTBBUTTONINFOFields(JNIEnv *env, jobject lpObject, TBBUTTONINFO *lpStruct);
#define TBBUTTONINFO_sizeof() sizeof(TBBUTTONINFO)
#else
#define getTBBUTTONINFOFields(a,b,c) NULL
#define setTBBUTTONINFOFields(a,b,c)
#define TBBUTTONINFO_sizeof() 0
#endif

#ifndef NO_TCITEM
TCITEM *getTCITEMFields(JNIEnv *env, jobject lpObject, TCITEM *lpStruct);
void setTCITEMFields(JNIEnv *env, jobject lpObject, TCITEM *lpStruct);
#define TCITEM_sizeof() sizeof(TCITEM)
#else
#define getTCITEMFields(a,b,c) NULL
#define setTCITEMFields(a,b,c)
#define TCITEM_sizeof() 0
#endif

#ifndef NO_TEXTMETRIC
TEXTMETRIC *getTEXTMETRICFields(JNIEnv *env, jobject lpObject, TEXTMETRIC *lpStruct);
void setTEXTMETRICFields(JNIEnv *env, jobject lpObject, TEXTMETRIC *lpStruct);
#define TEXTMETRIC_sizeof() sizeof(TEXTMETRIC)
#else
#define getTEXTMETRICFields(a,b,c) NULL
#define setTEXTMETRICFields(a,b,c)
#define TEXTMETRIC_sizeof() 0
#endif

#ifndef NO_TEXTMETRICA
TEXTMETRICA *getTEXTMETRICAFields(JNIEnv *env, jobject lpObject, TEXTMETRICA *lpStruct);
void setTEXTMETRICAFields(JNIEnv *env, jobject lpObject, TEXTMETRICA *lpStruct);
#define TEXTMETRICA_sizeof() sizeof(TEXTMETRICA)
#else
#define getTEXTMETRICAFields(a,b,c) NULL
#define setTEXTMETRICAFields(a,b,c)
#define TEXTMETRICA_sizeof() 0
#endif

#ifndef NO_TEXTMETRICW
TEXTMETRICW *getTEXTMETRICWFields(JNIEnv *env, jobject lpObject, TEXTMETRICW *lpStruct);
void setTEXTMETRICWFields(JNIEnv *env, jobject lpObject, TEXTMETRICW *lpStruct);
#define TEXTMETRICW_sizeof() sizeof(TEXTMETRICW)
#else
#define getTEXTMETRICWFields(a,b,c) NULL
#define setTEXTMETRICWFields(a,b,c)
#define TEXTMETRICW_sizeof() 0
#endif

#ifndef NO_TOOLINFO
TOOLINFO *getTOOLINFOFields(JNIEnv *env, jobject lpObject, TOOLINFO *lpStruct);
void setTOOLINFOFields(JNIEnv *env, jobject lpObject, TOOLINFO *lpStruct);
#define TOOLINFO_sizeof() sizeof(TOOLINFO)
#else
#define getTOOLINFOFields(a,b,c) NULL
#define setTOOLINFOFields(a,b,c)
#define TOOLINFO_sizeof() 0
#endif

#ifndef NO_TRACKMOUSEEVENT
TRACKMOUSEEVENT *getTRACKMOUSEEVENTFields(JNIEnv *env, jobject lpObject, TRACKMOUSEEVENT *lpStruct);
void setTRACKMOUSEEVENTFields(JNIEnv *env, jobject lpObject, TRACKMOUSEEVENT *lpStruct);
#define TRACKMOUSEEVENT_sizeof() sizeof(TRACKMOUSEEVENT)
#else
#define getTRACKMOUSEEVENTFields(a,b,c) NULL
#define setTRACKMOUSEEVENTFields(a,b,c)
#define TRACKMOUSEEVENT_sizeof() 0
#endif

#ifndef NO_TRIVERTEX
TRIVERTEX *getTRIVERTEXFields(JNIEnv *env, jobject lpObject, TRIVERTEX *lpStruct);
void setTRIVERTEXFields(JNIEnv *env, jobject lpObject, TRIVERTEX *lpStruct);
#define TRIVERTEX_sizeof() sizeof(TRIVERTEX)
#else
#define getTRIVERTEXFields(a,b,c) NULL
#define setTRIVERTEXFields(a,b,c)
#define TRIVERTEX_sizeof() 0
#endif

#ifndef NO_TVHITTESTINFO
TVHITTESTINFO *getTVHITTESTINFOFields(JNIEnv *env, jobject lpObject, TVHITTESTINFO *lpStruct);
void setTVHITTESTINFOFields(JNIEnv *env, jobject lpObject, TVHITTESTINFO *lpStruct);
#define TVHITTESTINFO_sizeof() sizeof(TVHITTESTINFO)
#else
#define getTVHITTESTINFOFields(a,b,c) NULL
#define setTVHITTESTINFOFields(a,b,c)
#define TVHITTESTINFO_sizeof() 0
#endif

#ifndef NO_TVINSERTSTRUCT
TVINSERTSTRUCT *getTVINSERTSTRUCTFields(JNIEnv *env, jobject lpObject, TVINSERTSTRUCT *lpStruct);
void setTVINSERTSTRUCTFields(JNIEnv *env, jobject lpObject, TVINSERTSTRUCT *lpStruct);
#define TVINSERTSTRUCT_sizeof() sizeof(TVINSERTSTRUCT)
#else
#define getTVINSERTSTRUCTFields(a,b,c) NULL
#define setTVINSERTSTRUCTFields(a,b,c)
#define TVINSERTSTRUCT_sizeof() 0
#endif

#ifndef NO_TVITEM
TVITEM *getTVITEMFields(JNIEnv *env, jobject lpObject, TVITEM *lpStruct);
void setTVITEMFields(JNIEnv *env, jobject lpObject, TVITEM *lpStruct);
#define TVITEM_sizeof() sizeof(TVITEM)
#else
#define getTVITEMFields(a,b,c) NULL
#define setTVITEMFields(a,b,c)
#define TVITEM_sizeof() 0
#endif

#ifndef NO_WINDOWPLACEMENT
WINDOWPLACEMENT *getWINDOWPLACEMENTFields(JNIEnv *env, jobject lpObject, WINDOWPLACEMENT *lpStruct);
void setWINDOWPLACEMENTFields(JNIEnv *env, jobject lpObject, WINDOWPLACEMENT *lpStruct);
#define WINDOWPLACEMENT_sizeof() sizeof(WINDOWPLACEMENT)
#else
#define getWINDOWPLACEMENTFields(a,b,c) NULL
#define setWINDOWPLACEMENTFields(a,b,c)
#define WINDOWPLACEMENT_sizeof() 0
#endif

#ifndef NO_WINDOWPOS
WINDOWPOS *getWINDOWPOSFields(JNIEnv *env, jobject lpObject, WINDOWPOS *lpStruct);
void setWINDOWPOSFields(JNIEnv *env, jobject lpObject, WINDOWPOS *lpStruct);
#define WINDOWPOS_sizeof() sizeof(WINDOWPOS)
#else
#define getWINDOWPOSFields(a,b,c) NULL
#define setWINDOWPOSFields(a,b,c)
#define WINDOWPOS_sizeof() 0
#endif

#ifndef NO_WNDCLASS
WNDCLASS *getWNDCLASSFields(JNIEnv *env, jobject lpObject, WNDCLASS *lpStruct);
void setWNDCLASSFields(JNIEnv *env, jobject lpObject, WNDCLASS *lpStruct);
#define WNDCLASS_sizeof() sizeof(WNDCLASS)
#else
#define getWNDCLASSFields(a,b,c) NULL
#define setWNDCLASSFields(a,b,c)
#define WNDCLASS_sizeof() 0
#endif

