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
#else
#define getACCELFields(a,b,c) NULL
#define setACCELFields(a,b,c)
#endif

#ifndef NO_BITMAP
BITMAP *getBITMAPFields(JNIEnv *env, jobject lpObject, BITMAP *lpStruct);
void setBITMAPFields(JNIEnv *env, jobject lpObject, BITMAP *lpStruct);
#else
#define getBITMAPFields(a,b,c) NULL
#define setBITMAPFields(a,b,c)
#endif

#ifndef NO_BITMAPINFOHEADER
BITMAPINFOHEADER *getBITMAPINFOHEADERFields(JNIEnv *env, jobject lpObject, BITMAPINFOHEADER *lpStruct);
void setBITMAPINFOHEADERFields(JNIEnv *env, jobject lpObject, BITMAPINFOHEADER *lpStruct);
#else
#define getBITMAPINFOHEADERFields(a,b,c) NULL
#define setBITMAPINFOHEADERFields(a,b,c)
#endif

#ifndef NO_BROWSEINFO
BROWSEINFO *getBROWSEINFOFields(JNIEnv *env, jobject lpObject, BROWSEINFO *lpStruct);
void setBROWSEINFOFields(JNIEnv *env, jobject lpObject, BROWSEINFO *lpStruct);
#else
#define getBROWSEINFOFields(a,b,c) NULL
#define setBROWSEINFOFields(a,b,c)
#endif

#ifndef NO_CHOOSECOLOR
CHOOSECOLOR *getCHOOSECOLORFields(JNIEnv *env, jobject lpObject, CHOOSECOLOR *lpStruct);
void setCHOOSECOLORFields(JNIEnv *env, jobject lpObject, CHOOSECOLOR *lpStruct);
#else
#define getCHOOSECOLORFields(a,b,c) NULL
#define setCHOOSECOLORFields(a,b,c)
#endif

#ifndef NO_CHOOSEFONT
CHOOSEFONT *getCHOOSEFONTFields(JNIEnv *env, jobject lpObject, CHOOSEFONT *lpStruct);
void setCHOOSEFONTFields(JNIEnv *env, jobject lpObject, CHOOSEFONT *lpStruct);
#else
#define getCHOOSEFONTFields(a,b,c) NULL
#define setCHOOSEFONTFields(a,b,c)
#endif

#ifndef NO_COMBOBOXINFO
COMBOBOXINFO *getCOMBOBOXINFOFields(JNIEnv *env, jobject lpObject, COMBOBOXINFO *lpStruct);
void setCOMBOBOXINFOFields(JNIEnv *env, jobject lpObject, COMBOBOXINFO *lpStruct);
#else
#define getCOMBOBOXINFOFields(a,b,c) NULL
#define setCOMBOBOXINFOFields(a,b,c)
#endif

#ifndef NO_COMPOSITIONFORM
COMPOSITIONFORM *getCOMPOSITIONFORMFields(JNIEnv *env, jobject lpObject, COMPOSITIONFORM *lpStruct);
void setCOMPOSITIONFORMFields(JNIEnv *env, jobject lpObject, COMPOSITIONFORM *lpStruct);
#else
#define getCOMPOSITIONFORMFields(a,b,c) NULL
#define setCOMPOSITIONFORMFields(a,b,c)
#endif

#ifndef NO_CREATESTRUCT
CREATESTRUCT *getCREATESTRUCTFields(JNIEnv *env, jobject lpObject, CREATESTRUCT *lpStruct);
void setCREATESTRUCTFields(JNIEnv *env, jobject lpObject, CREATESTRUCT *lpStruct);
#else
#define getCREATESTRUCTFields(a,b,c) NULL
#define setCREATESTRUCTFields(a,b,c)
#endif

#ifndef NO_DIBSECTION
DIBSECTION *getDIBSECTIONFields(JNIEnv *env, jobject lpObject, DIBSECTION *lpStruct);
void setDIBSECTIONFields(JNIEnv *env, jobject lpObject, DIBSECTION *lpStruct);
#else
#define getDIBSECTIONFields(a,b,c) NULL
#define setDIBSECTIONFields(a,b,c)
#endif

#ifndef NO_DLLVERSIONINFO
DLLVERSIONINFO *getDLLVERSIONINFOFields(JNIEnv *env, jobject lpObject, DLLVERSIONINFO *lpStruct);
void setDLLVERSIONINFOFields(JNIEnv *env, jobject lpObject, DLLVERSIONINFO *lpStruct);
#else
#define getDLLVERSIONINFOFields(a,b,c) NULL
#define setDLLVERSIONINFOFields(a,b,c)
#endif

#ifndef NO_DOCINFO
DOCINFO *getDOCINFOFields(JNIEnv *env, jobject lpObject, DOCINFO *lpStruct);
void setDOCINFOFields(JNIEnv *env, jobject lpObject, DOCINFO *lpStruct);
#else
#define getDOCINFOFields(a,b,c) NULL
#define setDOCINFOFields(a,b,c)
#endif

#ifndef NO_DRAWITEMSTRUCT
DRAWITEMSTRUCT *getDRAWITEMSTRUCTFields(JNIEnv *env, jobject lpObject, DRAWITEMSTRUCT *lpStruct);
void setDRAWITEMSTRUCTFields(JNIEnv *env, jobject lpObject, DRAWITEMSTRUCT *lpStruct);
#else
#define getDRAWITEMSTRUCTFields(a,b,c) NULL
#define setDRAWITEMSTRUCTFields(a,b,c)
#endif

#ifndef NO_DROPFILES
DROPFILES *getDROPFILESFields(JNIEnv *env, jobject lpObject, DROPFILES *lpStruct);
void setDROPFILESFields(JNIEnv *env, jobject lpObject, DROPFILES *lpStruct);
#else
#define getDROPFILESFields(a,b,c) NULL
#define setDROPFILESFields(a,b,c)
#endif

#ifndef NO_FILETIME
FILETIME *getFILETIMEFields(JNIEnv *env, jobject lpObject, FILETIME *lpStruct);
void setFILETIMEFields(JNIEnv *env, jobject lpObject, FILETIME *lpStruct);
#else
#define getFILETIMEFields(a,b,c) NULL
#define setFILETIMEFields(a,b,c)
#endif

#ifndef NO_GCP_RESULTS
GCP_RESULTS *getGCP_RESULTSFields(JNIEnv *env, jobject lpObject, GCP_RESULTS *lpStruct);
void setGCP_RESULTSFields(JNIEnv *env, jobject lpObject, GCP_RESULTS *lpStruct);
#else
#define getGCP_RESULTSFields(a,b,c) NULL
#define setGCP_RESULTSFields(a,b,c)
#endif

#ifndef NO_GRADIENT_RECT
GRADIENT_RECT *getGRADIENT_RECTFields(JNIEnv *env, jobject lpObject, GRADIENT_RECT *lpStruct);
void setGRADIENT_RECTFields(JNIEnv *env, jobject lpObject, GRADIENT_RECT *lpStruct);
#else
#define getGRADIENT_RECTFields(a,b,c) NULL
#define setGRADIENT_RECTFields(a,b,c)
#endif

#ifndef NO_HDITEM
HDITEM *getHDITEMFields(JNIEnv *env, jobject lpObject, HDITEM *lpStruct);
void setHDITEMFields(JNIEnv *env, jobject lpObject, HDITEM *lpStruct);
#else
#define getHDITEMFields(a,b,c) NULL
#define setHDITEMFields(a,b,c)
#endif

#ifndef NO_HELPINFO
HELPINFO *getHELPINFOFields(JNIEnv *env, jobject lpObject, HELPINFO *lpStruct);
void setHELPINFOFields(JNIEnv *env, jobject lpObject, HELPINFO *lpStruct);
#else
#define getHELPINFOFields(a,b,c) NULL
#define setHELPINFOFields(a,b,c)
#endif

#ifndef NO_ICONINFO
ICONINFO *getICONINFOFields(JNIEnv *env, jobject lpObject, ICONINFO *lpStruct);
void setICONINFOFields(JNIEnv *env, jobject lpObject, ICONINFO *lpStruct);
#else
#define getICONINFOFields(a,b,c) NULL
#define setICONINFOFields(a,b,c)
#endif

#ifndef NO_INITCOMMONCONTROLSEX
INITCOMMONCONTROLSEX *getINITCOMMONCONTROLSEXFields(JNIEnv *env, jobject lpObject, INITCOMMONCONTROLSEX *lpStruct);
void setINITCOMMONCONTROLSEXFields(JNIEnv *env, jobject lpObject, INITCOMMONCONTROLSEX *lpStruct);
#else
#define getINITCOMMONCONTROLSEXFields(a,b,c) NULL
#define setINITCOMMONCONTROLSEXFields(a,b,c)
#endif

#ifndef NO_LOGBRUSH
LOGBRUSH *getLOGBRUSHFields(JNIEnv *env, jobject lpObject, LOGBRUSH *lpStruct);
void setLOGBRUSHFields(JNIEnv *env, jobject lpObject, LOGBRUSH *lpStruct);
#else
#define getLOGBRUSHFields(a,b,c) NULL
#define setLOGBRUSHFields(a,b,c)
#endif

#ifndef NO_LOGFONT
LOGFONT *getLOGFONTFields(JNIEnv *env, jobject lpObject, LOGFONT *lpStruct);
void setLOGFONTFields(JNIEnv *env, jobject lpObject, LOGFONT *lpStruct);
#else
#define getLOGFONTFields(a,b,c) NULL
#define setLOGFONTFields(a,b,c)
#endif

#ifndef NO_LOGFONTA
LOGFONTA *getLOGFONTAFields(JNIEnv *env, jobject lpObject, LOGFONTA *lpStruct);
void setLOGFONTAFields(JNIEnv *env, jobject lpObject, LOGFONTA *lpStruct);
#else
#define getLOGFONTAFields(a,b,c) NULL
#define setLOGFONTAFields(a,b,c)
#endif

#ifndef NO_LOGFONTW
LOGFONTW *getLOGFONTWFields(JNIEnv *env, jobject lpObject, LOGFONTW *lpStruct);
void setLOGFONTWFields(JNIEnv *env, jobject lpObject, LOGFONTW *lpStruct);
#else
#define getLOGFONTWFields(a,b,c) NULL
#define setLOGFONTWFields(a,b,c)
#endif

#ifndef NO_LOGPEN
LOGPEN *getLOGPENFields(JNIEnv *env, jobject lpObject, LOGPEN *lpStruct);
void setLOGPENFields(JNIEnv *env, jobject lpObject, LOGPEN *lpStruct);
#else
#define getLOGPENFields(a,b,c) NULL
#define setLOGPENFields(a,b,c)
#endif

#ifndef NO_LVCOLUMN
LVCOLUMN *getLVCOLUMNFields(JNIEnv *env, jobject lpObject, LVCOLUMN *lpStruct);
void setLVCOLUMNFields(JNIEnv *env, jobject lpObject, LVCOLUMN *lpStruct);
#else
#define getLVCOLUMNFields(a,b,c) NULL
#define setLVCOLUMNFields(a,b,c)
#endif

#ifndef NO_LVHITTESTINFO
LVHITTESTINFO *getLVHITTESTINFOFields(JNIEnv *env, jobject lpObject, LVHITTESTINFO *lpStruct);
void setLVHITTESTINFOFields(JNIEnv *env, jobject lpObject, LVHITTESTINFO *lpStruct);
#else
#define getLVHITTESTINFOFields(a,b,c) NULL
#define setLVHITTESTINFOFields(a,b,c)
#endif

#ifndef NO_LVITEM
LVITEM *getLVITEMFields(JNIEnv *env, jobject lpObject, LVITEM *lpStruct);
void setLVITEMFields(JNIEnv *env, jobject lpObject, LVITEM *lpStruct);
#else
#define getLVITEMFields(a,b,c) NULL
#define setLVITEMFields(a,b,c)
#endif

#ifndef NO_MEASUREITEMSTRUCT
MEASUREITEMSTRUCT *getMEASUREITEMSTRUCTFields(JNIEnv *env, jobject lpObject, MEASUREITEMSTRUCT *lpStruct);
void setMEASUREITEMSTRUCTFields(JNIEnv *env, jobject lpObject, MEASUREITEMSTRUCT *lpStruct);
#else
#define getMEASUREITEMSTRUCTFields(a,b,c) NULL
#define setMEASUREITEMSTRUCTFields(a,b,c)
#endif

#ifndef NO_MENUINFO
MENUINFO *getMENUINFOFields(JNIEnv *env, jobject lpObject, MENUINFO *lpStruct);
void setMENUINFOFields(JNIEnv *env, jobject lpObject, MENUINFO *lpStruct);
#else
#define getMENUINFOFields(a,b,c) NULL
#define setMENUINFOFields(a,b,c)
#endif

#ifndef NO_MENUITEMINFO
MENUITEMINFO *getMENUITEMINFOFields(JNIEnv *env, jobject lpObject, MENUITEMINFO *lpStruct);
void setMENUITEMINFOFields(JNIEnv *env, jobject lpObject, MENUITEMINFO *lpStruct);
#else
#define getMENUITEMINFOFields(a,b,c) NULL
#define setMENUITEMINFOFields(a,b,c)
#endif

#ifndef NO_MONITORINFO
MONITORINFO *getMONITORINFOFields(JNIEnv *env, jobject lpObject, MONITORINFO *lpStruct);
void setMONITORINFOFields(JNIEnv *env, jobject lpObject, MONITORINFO *lpStruct);
#else
#define getMONITORINFOFields(a,b,c) NULL
#define setMONITORINFOFields(a,b,c)
#endif

#ifndef NO_MSG
MSG *getMSGFields(JNIEnv *env, jobject lpObject, MSG *lpStruct);
void setMSGFields(JNIEnv *env, jobject lpObject, MSG *lpStruct);
#else
#define getMSGFields(a,b,c) NULL
#define setMSGFields(a,b,c)
#endif

#ifndef NO_NMHDR
NMHDR *getNMHDRFields(JNIEnv *env, jobject lpObject, NMHDR *lpStruct);
void setNMHDRFields(JNIEnv *env, jobject lpObject, NMHDR *lpStruct);
#else
#define getNMHDRFields(a,b,c) NULL
#define setNMHDRFields(a,b,c)
#endif

#ifndef NO_NMCUSTOMDRAW
NMCUSTOMDRAW *getNMCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMCUSTOMDRAW *lpStruct);
void setNMCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMCUSTOMDRAW *lpStruct);
#else
#define getNMCUSTOMDRAWFields(a,b,c) NULL
#define setNMCUSTOMDRAWFields(a,b,c)
#endif

#ifndef NO_NMHEADER
NMHEADER *getNMHEADERFields(JNIEnv *env, jobject lpObject, NMHEADER *lpStruct);
void setNMHEADERFields(JNIEnv *env, jobject lpObject, NMHEADER *lpStruct);
#else
#define getNMHEADERFields(a,b,c) NULL
#define setNMHEADERFields(a,b,c)
#endif

#ifndef NO_NMLISTVIEW
NMLISTVIEW *getNMLISTVIEWFields(JNIEnv *env, jobject lpObject, NMLISTVIEW *lpStruct);
void setNMLISTVIEWFields(JNIEnv *env, jobject lpObject, NMLISTVIEW *lpStruct);
#else
#define getNMLISTVIEWFields(a,b,c) NULL
#define setNMLISTVIEWFields(a,b,c)
#endif

#ifndef NO_NMLVCUSTOMDRAW
NMLVCUSTOMDRAW *getNMLVCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMLVCUSTOMDRAW *lpStruct);
void setNMLVCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMLVCUSTOMDRAW *lpStruct);
#else
#define getNMLVCUSTOMDRAWFields(a,b,c) NULL
#define setNMLVCUSTOMDRAWFields(a,b,c)
#endif

#ifndef NO_NMLVDISPINFO
NMLVDISPINFO *getNMLVDISPINFOFields(JNIEnv *env, jobject lpObject, NMLVDISPINFO *lpStruct);
void setNMLVDISPINFOFields(JNIEnv *env, jobject lpObject, NMLVDISPINFO *lpStruct);
#else
#define getNMLVDISPINFOFields(a,b,c) NULL
#define setNMLVDISPINFOFields(a,b,c)
#endif

#ifndef NO_NMREBARCHEVRON
NMREBARCHEVRON *getNMREBARCHEVRONFields(JNIEnv *env, jobject lpObject, NMREBARCHEVRON *lpStruct);
void setNMREBARCHEVRONFields(JNIEnv *env, jobject lpObject, NMREBARCHEVRON *lpStruct);
#else
#define getNMREBARCHEVRONFields(a,b,c) NULL
#define setNMREBARCHEVRONFields(a,b,c)
#endif

#ifndef NO_NMRGINFO
NMRGINFO *getNMRGINFOFields(JNIEnv *env, jobject lpObject, NMRGINFO *lpStruct);
void setNMRGINFOFields(JNIEnv *env, jobject lpObject, NMRGINFO *lpStruct);
#else
#define getNMRGINFOFields(a,b,c) NULL
#define setNMRGINFOFields(a,b,c)
#endif

#ifndef NO_NMTOOLBAR
NMTOOLBAR *getNMTOOLBARFields(JNIEnv *env, jobject lpObject, NMTOOLBAR *lpStruct);
void setNMTOOLBARFields(JNIEnv *env, jobject lpObject, NMTOOLBAR *lpStruct);
#else
#define getNMTOOLBARFields(a,b,c) NULL
#define setNMTOOLBARFields(a,b,c)
#endif

#ifndef NO_NMTTDISPINFO
NMTTDISPINFO *getNMTTDISPINFOFields(JNIEnv *env, jobject lpObject, NMTTDISPINFO *lpStruct);
void setNMTTDISPINFOFields(JNIEnv *env, jobject lpObject, NMTTDISPINFO *lpStruct);
#else
#define getNMTTDISPINFOFields(a,b,c) NULL
#define setNMTTDISPINFOFields(a,b,c)
#endif

#ifndef NO_NMTTDISPINFOA
NMTTDISPINFOA *getNMTTDISPINFOAFields(JNIEnv *env, jobject lpObject, NMTTDISPINFOA *lpStruct);
void setNMTTDISPINFOAFields(JNIEnv *env, jobject lpObject, NMTTDISPINFOA *lpStruct);
#else
#define getNMTTDISPINFOAFields(a,b,c) NULL
#define setNMTTDISPINFOAFields(a,b,c)
#endif

#ifndef NO_NMTTDISPINFOW
NMTTDISPINFOW *getNMTTDISPINFOWFields(JNIEnv *env, jobject lpObject, NMTTDISPINFOW *lpStruct);
void setNMTTDISPINFOWFields(JNIEnv *env, jobject lpObject, NMTTDISPINFOW *lpStruct);
#else
#define getNMTTDISPINFOWFields(a,b,c) NULL
#define setNMTTDISPINFOWFields(a,b,c)
#endif

#ifndef NO_NMTVCUSTOMDRAW
NMTVCUSTOMDRAW *getNMTVCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMTVCUSTOMDRAW *lpStruct);
void setNMTVCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMTVCUSTOMDRAW *lpStruct);
#else
#define getNMTVCUSTOMDRAWFields(a,b,c) NULL
#define setNMTVCUSTOMDRAWFields(a,b,c)
#endif

#ifndef NO_NONCLIENTMETRICS
NONCLIENTMETRICS *getNONCLIENTMETRICSFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICS *lpStruct);
void setNONCLIENTMETRICSFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICS *lpStruct);
#else
#define getNONCLIENTMETRICSFields(a,b,c) NULL
#define setNONCLIENTMETRICSFields(a,b,c)
#endif

#ifndef NO_NONCLIENTMETRICSA
NONCLIENTMETRICSA *getNONCLIENTMETRICSAFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICSA *lpStruct);
void setNONCLIENTMETRICSAFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICSA *lpStruct);
#else
#define getNONCLIENTMETRICSAFields(a,b,c) NULL
#define setNONCLIENTMETRICSAFields(a,b,c)
#endif

#ifndef NO_NONCLIENTMETRICSW
NONCLIENTMETRICSW *getNONCLIENTMETRICSWFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICSW *lpStruct);
void setNONCLIENTMETRICSWFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICSW *lpStruct);
#else
#define getNONCLIENTMETRICSWFields(a,b,c) NULL
#define setNONCLIENTMETRICSWFields(a,b,c)
#endif

#ifndef NO_OPENFILENAME
OPENFILENAME *getOPENFILENAMEFields(JNIEnv *env, jobject lpObject, OPENFILENAME *lpStruct);
void setOPENFILENAMEFields(JNIEnv *env, jobject lpObject, OPENFILENAME *lpStruct);
#else
#define getOPENFILENAMEFields(a,b,c) NULL
#define setOPENFILENAMEFields(a,b,c)
#endif

#ifndef NO_OSVERSIONINFO
OSVERSIONINFO *getOSVERSIONINFOFields(JNIEnv *env, jobject lpObject, OSVERSIONINFO *lpStruct);
void setOSVERSIONINFOFields(JNIEnv *env, jobject lpObject, OSVERSIONINFO *lpStruct);
#else
#define getOSVERSIONINFOFields(a,b,c) NULL
#define setOSVERSIONINFOFields(a,b,c)
#endif

#ifndef NO_OSVERSIONINFOA
OSVERSIONINFOA *getOSVERSIONINFOAFields(JNIEnv *env, jobject lpObject, OSVERSIONINFOA *lpStruct);
void setOSVERSIONINFOAFields(JNIEnv *env, jobject lpObject, OSVERSIONINFOA *lpStruct);
#else
#define getOSVERSIONINFOAFields(a,b,c) NULL
#define setOSVERSIONINFOAFields(a,b,c)
#endif

#ifndef NO_OSVERSIONINFOW
OSVERSIONINFOW *getOSVERSIONINFOWFields(JNIEnv *env, jobject lpObject, OSVERSIONINFOW *lpStruct);
void setOSVERSIONINFOWFields(JNIEnv *env, jobject lpObject, OSVERSIONINFOW *lpStruct);
#else
#define getOSVERSIONINFOWFields(a,b,c) NULL
#define setOSVERSIONINFOWFields(a,b,c)
#endif

#ifndef NO_PAINTSTRUCT
PAINTSTRUCT *getPAINTSTRUCTFields(JNIEnv *env, jobject lpObject, PAINTSTRUCT *lpStruct);
void setPAINTSTRUCTFields(JNIEnv *env, jobject lpObject, PAINTSTRUCT *lpStruct);
#else
#define getPAINTSTRUCTFields(a,b,c) NULL
#define setPAINTSTRUCTFields(a,b,c)
#endif

#ifndef NO_POINT
POINT *getPOINTFields(JNIEnv *env, jobject lpObject, POINT *lpStruct);
void setPOINTFields(JNIEnv *env, jobject lpObject, POINT *lpStruct);
#else
#define getPOINTFields(a,b,c) NULL
#define setPOINTFields(a,b,c)
#endif

#ifndef NO_PRINTDLG
PRINTDLG *getPRINTDLGFields(JNIEnv *env, jobject lpObject, PRINTDLG *lpStruct);
void setPRINTDLGFields(JNIEnv *env, jobject lpObject, PRINTDLG *lpStruct);
#else
#define getPRINTDLGFields(a,b,c) NULL
#define setPRINTDLGFields(a,b,c)
#endif

#ifndef NO_REBARBANDINFO
REBARBANDINFO *getREBARBANDINFOFields(JNIEnv *env, jobject lpObject, REBARBANDINFO *lpStruct);
void setREBARBANDINFOFields(JNIEnv *env, jobject lpObject, REBARBANDINFO *lpStruct);
#else
#define getREBARBANDINFOFields(a,b,c) NULL
#define setREBARBANDINFOFields(a,b,c)
#endif

#ifndef NO_RECT
RECT *getRECTFields(JNIEnv *env, jobject lpObject, RECT *lpStruct);
void setRECTFields(JNIEnv *env, jobject lpObject, RECT *lpStruct);
#else
#define getRECTFields(a,b,c) NULL
#define setRECTFields(a,b,c)
#endif

#ifndef NO_SCROLLINFO
SCROLLINFO *getSCROLLINFOFields(JNIEnv *env, jobject lpObject, SCROLLINFO *lpStruct);
void setSCROLLINFOFields(JNIEnv *env, jobject lpObject, SCROLLINFO *lpStruct);
#else
#define getSCROLLINFOFields(a,b,c) NULL
#define setSCROLLINFOFields(a,b,c)
#endif

#ifndef NO_SHACTIVATEINFO
SHACTIVATEINFO *getSHACTIVATEINFOFields(JNIEnv *env, jobject lpObject, SHACTIVATEINFO *lpStruct);
void setSHACTIVATEINFOFields(JNIEnv *env, jobject lpObject, SHACTIVATEINFO *lpStruct);
#else
#define getSHACTIVATEINFOFields(a,b,c) NULL
#define setSHACTIVATEINFOFields(a,b,c)
#endif

#ifndef NO_SHELLEXECUTEINFO
SHELLEXECUTEINFO *getSHELLEXECUTEINFOFields(JNIEnv *env, jobject lpObject, SHELLEXECUTEINFO *lpStruct);
void setSHELLEXECUTEINFOFields(JNIEnv *env, jobject lpObject, SHELLEXECUTEINFO *lpStruct);
#else
#define getSHELLEXECUTEINFOFields(a,b,c) NULL
#define setSHELLEXECUTEINFOFields(a,b,c)
#endif

#ifndef NO_SHMENUBARINFO
SHMENUBARINFO *getSHMENUBARINFOFields(JNIEnv *env, jobject lpObject, SHMENUBARINFO *lpStruct);
void setSHMENUBARINFOFields(JNIEnv *env, jobject lpObject, SHMENUBARINFO *lpStruct);
#else
#define getSHMENUBARINFOFields(a,b,c) NULL
#define setSHMENUBARINFOFields(a,b,c)
#endif

#ifndef NO_SHRGINFO
SHRGINFO *getSHRGINFOFields(JNIEnv *env, jobject lpObject, SHRGINFO *lpStruct);
void setSHRGINFOFields(JNIEnv *env, jobject lpObject, SHRGINFO *lpStruct);
#else
#define getSHRGINFOFields(a,b,c) NULL
#define setSHRGINFOFields(a,b,c)
#endif

#ifndef NO_SIPINFO
SIPINFO *getSIPINFOFields(JNIEnv *env, jobject lpObject, SIPINFO *lpStruct);
void setSIPINFOFields(JNIEnv *env, jobject lpObject, SIPINFO *lpStruct);
#else
#define getSIPINFOFields(a,b,c) NULL
#define setSIPINFOFields(a,b,c)
#endif

#ifndef NO_SIZE
SIZE *getSIZEFields(JNIEnv *env, jobject lpObject, SIZE *lpStruct);
void setSIZEFields(JNIEnv *env, jobject lpObject, SIZE *lpStruct);
#else
#define getSIZEFields(a,b,c) NULL
#define setSIZEFields(a,b,c)
#endif

#ifndef NO_TBBUTTON
TBBUTTON *getTBBUTTONFields(JNIEnv *env, jobject lpObject, TBBUTTON *lpStruct);
void setTBBUTTONFields(JNIEnv *env, jobject lpObject, TBBUTTON *lpStruct);
#else
#define getTBBUTTONFields(a,b,c) NULL
#define setTBBUTTONFields(a,b,c)
#endif

#ifndef NO_TBBUTTONINFO
TBBUTTONINFO *getTBBUTTONINFOFields(JNIEnv *env, jobject lpObject, TBBUTTONINFO *lpStruct);
void setTBBUTTONINFOFields(JNIEnv *env, jobject lpObject, TBBUTTONINFO *lpStruct);
#else
#define getTBBUTTONINFOFields(a,b,c) NULL
#define setTBBUTTONINFOFields(a,b,c)
#endif

#ifndef NO_TCITEM
TCITEM *getTCITEMFields(JNIEnv *env, jobject lpObject, TCITEM *lpStruct);
void setTCITEMFields(JNIEnv *env, jobject lpObject, TCITEM *lpStruct);
#else
#define getTCITEMFields(a,b,c) NULL
#define setTCITEMFields(a,b,c)
#endif

#ifndef NO_TEXTMETRIC
TEXTMETRIC *getTEXTMETRICFields(JNIEnv *env, jobject lpObject, TEXTMETRIC *lpStruct);
void setTEXTMETRICFields(JNIEnv *env, jobject lpObject, TEXTMETRIC *lpStruct);
#else
#define getTEXTMETRICFields(a,b,c) NULL
#define setTEXTMETRICFields(a,b,c)
#endif

#ifndef NO_TEXTMETRICA
TEXTMETRICA *getTEXTMETRICAFields(JNIEnv *env, jobject lpObject, TEXTMETRICA *lpStruct);
void setTEXTMETRICAFields(JNIEnv *env, jobject lpObject, TEXTMETRICA *lpStruct);
#else
#define getTEXTMETRICAFields(a,b,c) NULL
#define setTEXTMETRICAFields(a,b,c)
#endif

#ifndef NO_TEXTMETRICW
TEXTMETRICW *getTEXTMETRICWFields(JNIEnv *env, jobject lpObject, TEXTMETRICW *lpStruct);
void setTEXTMETRICWFields(JNIEnv *env, jobject lpObject, TEXTMETRICW *lpStruct);
#else
#define getTEXTMETRICWFields(a,b,c) NULL
#define setTEXTMETRICWFields(a,b,c)
#endif

#ifndef NO_TOOLINFO
TOOLINFO *getTOOLINFOFields(JNIEnv *env, jobject lpObject, TOOLINFO *lpStruct);
void setTOOLINFOFields(JNIEnv *env, jobject lpObject, TOOLINFO *lpStruct);
#else
#define getTOOLINFOFields(a,b,c) NULL
#define setTOOLINFOFields(a,b,c)
#endif

#ifndef NO_TRACKMOUSEEVENT
TRACKMOUSEEVENT *getTRACKMOUSEEVENTFields(JNIEnv *env, jobject lpObject, TRACKMOUSEEVENT *lpStruct);
void setTRACKMOUSEEVENTFields(JNIEnv *env, jobject lpObject, TRACKMOUSEEVENT *lpStruct);
#else
#define getTRACKMOUSEEVENTFields(a,b,c) NULL
#define setTRACKMOUSEEVENTFields(a,b,c)
#endif

#ifndef NO_TRIVERTEX
TRIVERTEX *getTRIVERTEXFields(JNIEnv *env, jobject lpObject, TRIVERTEX *lpStruct);
void setTRIVERTEXFields(JNIEnv *env, jobject lpObject, TRIVERTEX *lpStruct);
#else
#define getTRIVERTEXFields(a,b,c) NULL
#define setTRIVERTEXFields(a,b,c)
#endif

#ifndef NO_TVHITTESTINFO
TVHITTESTINFO *getTVHITTESTINFOFields(JNIEnv *env, jobject lpObject, TVHITTESTINFO *lpStruct);
void setTVHITTESTINFOFields(JNIEnv *env, jobject lpObject, TVHITTESTINFO *lpStruct);
#else
#define getTVHITTESTINFOFields(a,b,c) NULL
#define setTVHITTESTINFOFields(a,b,c)
#endif

#ifndef NO_TVINSERTSTRUCT
TVINSERTSTRUCT *getTVINSERTSTRUCTFields(JNIEnv *env, jobject lpObject, TVINSERTSTRUCT *lpStruct);
void setTVINSERTSTRUCTFields(JNIEnv *env, jobject lpObject, TVINSERTSTRUCT *lpStruct);
#else
#define getTVINSERTSTRUCTFields(a,b,c) NULL
#define setTVINSERTSTRUCTFields(a,b,c)
#endif

#ifndef NO_TVITEM
TVITEM *getTVITEMFields(JNIEnv *env, jobject lpObject, TVITEM *lpStruct);
void setTVITEMFields(JNIEnv *env, jobject lpObject, TVITEM *lpStruct);
#else
#define getTVITEMFields(a,b,c) NULL
#define setTVITEMFields(a,b,c)
#endif

#ifndef NO_WINDOWPLACEMENT
WINDOWPLACEMENT *getWINDOWPLACEMENTFields(JNIEnv *env, jobject lpObject, WINDOWPLACEMENT *lpStruct);
void setWINDOWPLACEMENTFields(JNIEnv *env, jobject lpObject, WINDOWPLACEMENT *lpStruct);
#else
#define getWINDOWPLACEMENTFields(a,b,c) NULL
#define setWINDOWPLACEMENTFields(a,b,c)
#endif

#ifndef NO_WINDOWPOS
WINDOWPOS *getWINDOWPOSFields(JNIEnv *env, jobject lpObject, WINDOWPOS *lpStruct);
void setWINDOWPOSFields(JNIEnv *env, jobject lpObject, WINDOWPOS *lpStruct);
#else
#define getWINDOWPOSFields(a,b,c) NULL
#define setWINDOWPOSFields(a,b,c)
#endif

#ifndef NO_WNDCLASS
WNDCLASS *getWNDCLASSFields(JNIEnv *env, jobject lpObject, WNDCLASS *lpStruct);
void setWNDCLASSFields(JNIEnv *env, jobject lpObject, WNDCLASS *lpStruct);
#else
#define getWNDCLASSFields(a,b,c) NULL
#define setWNDCLASSFields(a,b,c)
#endif

