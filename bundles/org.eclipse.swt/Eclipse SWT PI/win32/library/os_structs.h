/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

#include "os.h"

#ifndef NO_ACCEL
void cacheACCELFields(JNIEnv *env, jobject lpObject);
ACCEL *getACCELFields(JNIEnv *env, jobject lpObject, ACCEL *lpStruct);
void setACCELFields(JNIEnv *env, jobject lpObject, ACCEL *lpStruct);
#define ACCEL_sizeof() sizeof(ACCEL)
#else
#define cacheACCELFields(a,b)
#define getACCELFields(a,b,c) NULL
#define setACCELFields(a,b,c)
#define ACCEL_sizeof() 0
#endif

#ifndef NO_ACTCTX
void cacheACTCTXFields(JNIEnv *env, jobject lpObject);
ACTCTX *getACTCTXFields(JNIEnv *env, jobject lpObject, ACTCTX *lpStruct);
void setACTCTXFields(JNIEnv *env, jobject lpObject, ACTCTX *lpStruct);
#define ACTCTX_sizeof() sizeof(ACTCTX)
#else
#define cacheACTCTXFields(a,b)
#define getACTCTXFields(a,b,c) NULL
#define setACTCTXFields(a,b,c)
#define ACTCTX_sizeof() 0
#endif

#ifndef NO_BITMAP
void cacheBITMAPFields(JNIEnv *env, jobject lpObject);
BITMAP *getBITMAPFields(JNIEnv *env, jobject lpObject, BITMAP *lpStruct);
void setBITMAPFields(JNIEnv *env, jobject lpObject, BITMAP *lpStruct);
#define BITMAP_sizeof() sizeof(BITMAP)
#else
#define cacheBITMAPFields(a,b)
#define getBITMAPFields(a,b,c) NULL
#define setBITMAPFields(a,b,c)
#define BITMAP_sizeof() 0
#endif

#ifndef NO_BITMAPINFOHEADER
void cacheBITMAPINFOHEADERFields(JNIEnv *env, jobject lpObject);
BITMAPINFOHEADER *getBITMAPINFOHEADERFields(JNIEnv *env, jobject lpObject, BITMAPINFOHEADER *lpStruct);
void setBITMAPINFOHEADERFields(JNIEnv *env, jobject lpObject, BITMAPINFOHEADER *lpStruct);
#define BITMAPINFOHEADER_sizeof() sizeof(BITMAPINFOHEADER)
#else
#define cacheBITMAPINFOHEADERFields(a,b)
#define getBITMAPINFOHEADERFields(a,b,c) NULL
#define setBITMAPINFOHEADERFields(a,b,c)
#define BITMAPINFOHEADER_sizeof() 0
#endif

#ifndef NO_BLENDFUNCTION
void cacheBLENDFUNCTIONFields(JNIEnv *env, jobject lpObject);
BLENDFUNCTION *getBLENDFUNCTIONFields(JNIEnv *env, jobject lpObject, BLENDFUNCTION *lpStruct);
void setBLENDFUNCTIONFields(JNIEnv *env, jobject lpObject, BLENDFUNCTION *lpStruct);
#define BLENDFUNCTION_sizeof() sizeof(BLENDFUNCTION)
#else
#define cacheBLENDFUNCTIONFields(a,b)
#define getBLENDFUNCTIONFields(a,b,c) NULL
#define setBLENDFUNCTIONFields(a,b,c)
#define BLENDFUNCTION_sizeof() 0
#endif

#ifndef NO_BP_PAINTPARAMS
void cacheBP_PAINTPARAMSFields(JNIEnv *env, jobject lpObject);
BP_PAINTPARAMS *getBP_PAINTPARAMSFields(JNIEnv *env, jobject lpObject, BP_PAINTPARAMS *lpStruct);
void setBP_PAINTPARAMSFields(JNIEnv *env, jobject lpObject, BP_PAINTPARAMS *lpStruct);
#define BP_PAINTPARAMS_sizeof() sizeof(BP_PAINTPARAMS)
#else
#define cacheBP_PAINTPARAMSFields(a,b)
#define getBP_PAINTPARAMSFields(a,b,c) NULL
#define setBP_PAINTPARAMSFields(a,b,c)
#define BP_PAINTPARAMS_sizeof() 0
#endif

#ifndef NO_BROWSEINFO
void cacheBROWSEINFOFields(JNIEnv *env, jobject lpObject);
BROWSEINFO *getBROWSEINFOFields(JNIEnv *env, jobject lpObject, BROWSEINFO *lpStruct);
void setBROWSEINFOFields(JNIEnv *env, jobject lpObject, BROWSEINFO *lpStruct);
#define BROWSEINFO_sizeof() sizeof(BROWSEINFO)
#else
#define cacheBROWSEINFOFields(a,b)
#define getBROWSEINFOFields(a,b,c) NULL
#define setBROWSEINFOFields(a,b,c)
#define BROWSEINFO_sizeof() 0
#endif

#ifndef NO_BUTTON_IMAGELIST
void cacheBUTTON_IMAGELISTFields(JNIEnv *env, jobject lpObject);
BUTTON_IMAGELIST *getBUTTON_IMAGELISTFields(JNIEnv *env, jobject lpObject, BUTTON_IMAGELIST *lpStruct);
void setBUTTON_IMAGELISTFields(JNIEnv *env, jobject lpObject, BUTTON_IMAGELIST *lpStruct);
#define BUTTON_IMAGELIST_sizeof() sizeof(BUTTON_IMAGELIST)
#else
#define cacheBUTTON_IMAGELISTFields(a,b)
#define getBUTTON_IMAGELISTFields(a,b,c) NULL
#define setBUTTON_IMAGELISTFields(a,b,c)
#define BUTTON_IMAGELIST_sizeof() 0
#endif

#ifndef NO_CANDIDATEFORM
void cacheCANDIDATEFORMFields(JNIEnv *env, jobject lpObject);
CANDIDATEFORM *getCANDIDATEFORMFields(JNIEnv *env, jobject lpObject, CANDIDATEFORM *lpStruct);
void setCANDIDATEFORMFields(JNIEnv *env, jobject lpObject, CANDIDATEFORM *lpStruct);
#define CANDIDATEFORM_sizeof() sizeof(CANDIDATEFORM)
#else
#define cacheCANDIDATEFORMFields(a,b)
#define getCANDIDATEFORMFields(a,b,c) NULL
#define setCANDIDATEFORMFields(a,b,c)
#define CANDIDATEFORM_sizeof() 0
#endif

#ifndef NO_CHOOSECOLOR
void cacheCHOOSECOLORFields(JNIEnv *env, jobject lpObject);
CHOOSECOLOR *getCHOOSECOLORFields(JNIEnv *env, jobject lpObject, CHOOSECOLOR *lpStruct);
void setCHOOSECOLORFields(JNIEnv *env, jobject lpObject, CHOOSECOLOR *lpStruct);
#define CHOOSECOLOR_sizeof() sizeof(CHOOSECOLOR)
#else
#define cacheCHOOSECOLORFields(a,b)
#define getCHOOSECOLORFields(a,b,c) NULL
#define setCHOOSECOLORFields(a,b,c)
#define CHOOSECOLOR_sizeof() 0
#endif

#ifndef NO_CHOOSEFONT
void cacheCHOOSEFONTFields(JNIEnv *env, jobject lpObject);
CHOOSEFONT *getCHOOSEFONTFields(JNIEnv *env, jobject lpObject, CHOOSEFONT *lpStruct);
void setCHOOSEFONTFields(JNIEnv *env, jobject lpObject, CHOOSEFONT *lpStruct);
#define CHOOSEFONT_sizeof() sizeof(CHOOSEFONT)
#else
#define cacheCHOOSEFONTFields(a,b)
#define getCHOOSEFONTFields(a,b,c) NULL
#define setCHOOSEFONTFields(a,b,c)
#define CHOOSEFONT_sizeof() 0
#endif

#ifndef NO_COMBOBOXINFO
void cacheCOMBOBOXINFOFields(JNIEnv *env, jobject lpObject);
COMBOBOXINFO *getCOMBOBOXINFOFields(JNIEnv *env, jobject lpObject, COMBOBOXINFO *lpStruct);
void setCOMBOBOXINFOFields(JNIEnv *env, jobject lpObject, COMBOBOXINFO *lpStruct);
#define COMBOBOXINFO_sizeof() sizeof(COMBOBOXINFO)
#else
#define cacheCOMBOBOXINFOFields(a,b)
#define getCOMBOBOXINFOFields(a,b,c) NULL
#define setCOMBOBOXINFOFields(a,b,c)
#define COMBOBOXINFO_sizeof() 0
#endif

#ifndef NO_COMPOSITIONFORM
void cacheCOMPOSITIONFORMFields(JNIEnv *env, jobject lpObject);
COMPOSITIONFORM *getCOMPOSITIONFORMFields(JNIEnv *env, jobject lpObject, COMPOSITIONFORM *lpStruct);
void setCOMPOSITIONFORMFields(JNIEnv *env, jobject lpObject, COMPOSITIONFORM *lpStruct);
#define COMPOSITIONFORM_sizeof() sizeof(COMPOSITIONFORM)
#else
#define cacheCOMPOSITIONFORMFields(a,b)
#define getCOMPOSITIONFORMFields(a,b,c) NULL
#define setCOMPOSITIONFORMFields(a,b,c)
#define COMPOSITIONFORM_sizeof() 0
#endif

#ifndef NO_CREATESTRUCT
void cacheCREATESTRUCTFields(JNIEnv *env, jobject lpObject);
CREATESTRUCT *getCREATESTRUCTFields(JNIEnv *env, jobject lpObject, CREATESTRUCT *lpStruct);
void setCREATESTRUCTFields(JNIEnv *env, jobject lpObject, CREATESTRUCT *lpStruct);
#define CREATESTRUCT_sizeof() sizeof(CREATESTRUCT)
#else
#define cacheCREATESTRUCTFields(a,b)
#define getCREATESTRUCTFields(a,b,c) NULL
#define setCREATESTRUCTFields(a,b,c)
#define CREATESTRUCT_sizeof() 0
#endif

#ifndef NO_DEVMODE
void cacheDEVMODEFields(JNIEnv *env, jobject lpObject);
DEVMODE *getDEVMODEFields(JNIEnv *env, jobject lpObject, DEVMODE *lpStruct);
void setDEVMODEFields(JNIEnv *env, jobject lpObject, DEVMODE *lpStruct);
#define DEVMODE_sizeof() sizeof(DEVMODE)
#else
#define cacheDEVMODEFields(a,b)
#define getDEVMODEFields(a,b,c) NULL
#define setDEVMODEFields(a,b,c)
#define DEVMODE_sizeof() 0
#endif

#ifndef NO_DEVMODEA
void cacheDEVMODEAFields(JNIEnv *env, jobject lpObject);
DEVMODEA *getDEVMODEAFields(JNIEnv *env, jobject lpObject, DEVMODEA *lpStruct);
void setDEVMODEAFields(JNIEnv *env, jobject lpObject, DEVMODEA *lpStruct);
#define DEVMODEA_sizeof() sizeof(DEVMODEA)
#else
#define cacheDEVMODEAFields(a,b)
#define getDEVMODEAFields(a,b,c) NULL
#define setDEVMODEAFields(a,b,c)
#define DEVMODEA_sizeof() 0
#endif

#ifndef NO_DEVMODEW
void cacheDEVMODEWFields(JNIEnv *env, jobject lpObject);
DEVMODEW *getDEVMODEWFields(JNIEnv *env, jobject lpObject, DEVMODEW *lpStruct);
void setDEVMODEWFields(JNIEnv *env, jobject lpObject, DEVMODEW *lpStruct);
#define DEVMODEW_sizeof() sizeof(DEVMODEW)
#else
#define cacheDEVMODEWFields(a,b)
#define getDEVMODEWFields(a,b,c) NULL
#define setDEVMODEWFields(a,b,c)
#define DEVMODEW_sizeof() 0
#endif

#ifndef NO_DIBSECTION
void cacheDIBSECTIONFields(JNIEnv *env, jobject lpObject);
DIBSECTION *getDIBSECTIONFields(JNIEnv *env, jobject lpObject, DIBSECTION *lpStruct);
void setDIBSECTIONFields(JNIEnv *env, jobject lpObject, DIBSECTION *lpStruct);
#define DIBSECTION_sizeof() sizeof(DIBSECTION)
#else
#define cacheDIBSECTIONFields(a,b)
#define getDIBSECTIONFields(a,b,c) NULL
#define setDIBSECTIONFields(a,b,c)
#define DIBSECTION_sizeof() 0
#endif

#ifndef NO_DLLVERSIONINFO
void cacheDLLVERSIONINFOFields(JNIEnv *env, jobject lpObject);
DLLVERSIONINFO *getDLLVERSIONINFOFields(JNIEnv *env, jobject lpObject, DLLVERSIONINFO *lpStruct);
void setDLLVERSIONINFOFields(JNIEnv *env, jobject lpObject, DLLVERSIONINFO *lpStruct);
#define DLLVERSIONINFO_sizeof() sizeof(DLLVERSIONINFO)
#else
#define cacheDLLVERSIONINFOFields(a,b)
#define getDLLVERSIONINFOFields(a,b,c) NULL
#define setDLLVERSIONINFOFields(a,b,c)
#define DLLVERSIONINFO_sizeof() 0
#endif

#ifndef NO_DOCHOSTUIINFO
void cacheDOCHOSTUIINFOFields(JNIEnv *env, jobject lpObject);
DOCHOSTUIINFO *getDOCHOSTUIINFOFields(JNIEnv *env, jobject lpObject, DOCHOSTUIINFO *lpStruct);
void setDOCHOSTUIINFOFields(JNIEnv *env, jobject lpObject, DOCHOSTUIINFO *lpStruct);
#define DOCHOSTUIINFO_sizeof() sizeof(DOCHOSTUIINFO)
#else
#define cacheDOCHOSTUIINFOFields(a,b)
#define getDOCHOSTUIINFOFields(a,b,c) NULL
#define setDOCHOSTUIINFOFields(a,b,c)
#define DOCHOSTUIINFO_sizeof() 0
#endif

#ifndef NO_DOCINFO
void cacheDOCINFOFields(JNIEnv *env, jobject lpObject);
DOCINFO *getDOCINFOFields(JNIEnv *env, jobject lpObject, DOCINFO *lpStruct);
void setDOCINFOFields(JNIEnv *env, jobject lpObject, DOCINFO *lpStruct);
#define DOCINFO_sizeof() sizeof(DOCINFO)
#else
#define cacheDOCINFOFields(a,b)
#define getDOCINFOFields(a,b,c) NULL
#define setDOCINFOFields(a,b,c)
#define DOCINFO_sizeof() 0
#endif

#ifndef NO_DRAWITEMSTRUCT
void cacheDRAWITEMSTRUCTFields(JNIEnv *env, jobject lpObject);
DRAWITEMSTRUCT *getDRAWITEMSTRUCTFields(JNIEnv *env, jobject lpObject, DRAWITEMSTRUCT *lpStruct);
void setDRAWITEMSTRUCTFields(JNIEnv *env, jobject lpObject, DRAWITEMSTRUCT *lpStruct);
#define DRAWITEMSTRUCT_sizeof() sizeof(DRAWITEMSTRUCT)
#else
#define cacheDRAWITEMSTRUCTFields(a,b)
#define getDRAWITEMSTRUCTFields(a,b,c) NULL
#define setDRAWITEMSTRUCTFields(a,b,c)
#define DRAWITEMSTRUCT_sizeof() 0
#endif

#ifndef NO_DROPFILES
void cacheDROPFILESFields(JNIEnv *env, jobject lpObject);
DROPFILES *getDROPFILESFields(JNIEnv *env, jobject lpObject, DROPFILES *lpStruct);
void setDROPFILESFields(JNIEnv *env, jobject lpObject, DROPFILES *lpStruct);
#define DROPFILES_sizeof() sizeof(DROPFILES)
#else
#define cacheDROPFILESFields(a,b)
#define getDROPFILESFields(a,b,c) NULL
#define setDROPFILESFields(a,b,c)
#define DROPFILES_sizeof() 0
#endif

#ifndef NO_DWM_BLURBEHIND
void cacheDWM_BLURBEHINDFields(JNIEnv *env, jobject lpObject);
DWM_BLURBEHIND *getDWM_BLURBEHINDFields(JNIEnv *env, jobject lpObject, DWM_BLURBEHIND *lpStruct);
void setDWM_BLURBEHINDFields(JNIEnv *env, jobject lpObject, DWM_BLURBEHIND *lpStruct);
#define DWM_BLURBEHIND_sizeof() sizeof(DWM_BLURBEHIND)
#else
#define cacheDWM_BLURBEHINDFields(a,b)
#define getDWM_BLURBEHINDFields(a,b,c) NULL
#define setDWM_BLURBEHINDFields(a,b,c)
#define DWM_BLURBEHIND_sizeof() 0
#endif

#ifndef NO_EMR
void cacheEMRFields(JNIEnv *env, jobject lpObject);
EMR *getEMRFields(JNIEnv *env, jobject lpObject, EMR *lpStruct);
void setEMRFields(JNIEnv *env, jobject lpObject, EMR *lpStruct);
#define EMR_sizeof() sizeof(EMR)
#else
#define cacheEMRFields(a,b)
#define getEMRFields(a,b,c) NULL
#define setEMRFields(a,b,c)
#define EMR_sizeof() 0
#endif

#ifndef NO_EMREXTCREATEFONTINDIRECTW
void cacheEMREXTCREATEFONTINDIRECTWFields(JNIEnv *env, jobject lpObject);
EMREXTCREATEFONTINDIRECTW *getEMREXTCREATEFONTINDIRECTWFields(JNIEnv *env, jobject lpObject, EMREXTCREATEFONTINDIRECTW *lpStruct);
void setEMREXTCREATEFONTINDIRECTWFields(JNIEnv *env, jobject lpObject, EMREXTCREATEFONTINDIRECTW *lpStruct);
#define EMREXTCREATEFONTINDIRECTW_sizeof() sizeof(EMREXTCREATEFONTINDIRECTW)
#else
#define cacheEMREXTCREATEFONTINDIRECTWFields(a,b)
#define getEMREXTCREATEFONTINDIRECTWFields(a,b,c) NULL
#define setEMREXTCREATEFONTINDIRECTWFields(a,b,c)
#define EMREXTCREATEFONTINDIRECTW_sizeof() 0
#endif

#ifndef NO_EXTLOGFONTW
void cacheEXTLOGFONTWFields(JNIEnv *env, jobject lpObject);
EXTLOGFONTW *getEXTLOGFONTWFields(JNIEnv *env, jobject lpObject, EXTLOGFONTW *lpStruct);
void setEXTLOGFONTWFields(JNIEnv *env, jobject lpObject, EXTLOGFONTW *lpStruct);
#define EXTLOGFONTW_sizeof() sizeof(EXTLOGFONTW)
#else
#define cacheEXTLOGFONTWFields(a,b)
#define getEXTLOGFONTWFields(a,b,c) NULL
#define setEXTLOGFONTWFields(a,b,c)
#define EXTLOGFONTW_sizeof() 0
#endif

#ifndef NO_EXTLOGPEN
void cacheEXTLOGPENFields(JNIEnv *env, jobject lpObject);
EXTLOGPEN *getEXTLOGPENFields(JNIEnv *env, jobject lpObject, EXTLOGPEN *lpStruct);
void setEXTLOGPENFields(JNIEnv *env, jobject lpObject, EXTLOGPEN *lpStruct);
#define EXTLOGPEN_sizeof() sizeof(EXTLOGPEN)
#else
#define cacheEXTLOGPENFields(a,b)
#define getEXTLOGPENFields(a,b,c) NULL
#define setEXTLOGPENFields(a,b,c)
#define EXTLOGPEN_sizeof() 0
#endif

#ifndef NO_FILETIME
void cacheFILETIMEFields(JNIEnv *env, jobject lpObject);
FILETIME *getFILETIMEFields(JNIEnv *env, jobject lpObject, FILETIME *lpStruct);
void setFILETIMEFields(JNIEnv *env, jobject lpObject, FILETIME *lpStruct);
#define FILETIME_sizeof() sizeof(FILETIME)
#else
#define cacheFILETIMEFields(a,b)
#define getFILETIMEFields(a,b,c) NULL
#define setFILETIMEFields(a,b,c)
#define FILETIME_sizeof() 0
#endif

#ifndef NO_FLICK_DATA
void cacheFLICK_DATAFields(JNIEnv *env, jobject lpObject);
FLICK_DATA *getFLICK_DATAFields(JNIEnv *env, jobject lpObject, FLICK_DATA *lpStruct);
void setFLICK_DATAFields(JNIEnv *env, jobject lpObject, FLICK_DATA *lpStruct);
#define FLICK_DATA_sizeof() sizeof(FLICK_DATA)
#else
#define cacheFLICK_DATAFields(a,b)
#define getFLICK_DATAFields(a,b,c) NULL
#define setFLICK_DATAFields(a,b,c)
#define FLICK_DATA_sizeof() 0
#endif

#ifndef NO_FLICK_POINT
void cacheFLICK_POINTFields(JNIEnv *env, jobject lpObject);
FLICK_POINT *getFLICK_POINTFields(JNIEnv *env, jobject lpObject, FLICK_POINT *lpStruct);
void setFLICK_POINTFields(JNIEnv *env, jobject lpObject, FLICK_POINT *lpStruct);
#define FLICK_POINT_sizeof() sizeof(FLICK_POINT)
#else
#define cacheFLICK_POINTFields(a,b)
#define getFLICK_POINTFields(a,b,c) NULL
#define setFLICK_POINTFields(a,b,c)
#define FLICK_POINT_sizeof() 0
#endif

#ifndef NO_GCP_RESULTS
void cacheGCP_RESULTSFields(JNIEnv *env, jobject lpObject);
GCP_RESULTS *getGCP_RESULTSFields(JNIEnv *env, jobject lpObject, GCP_RESULTS *lpStruct);
void setGCP_RESULTSFields(JNIEnv *env, jobject lpObject, GCP_RESULTS *lpStruct);
#define GCP_RESULTS_sizeof() sizeof(GCP_RESULTS)
#else
#define cacheGCP_RESULTSFields(a,b)
#define getGCP_RESULTSFields(a,b,c) NULL
#define setGCP_RESULTSFields(a,b,c)
#define GCP_RESULTS_sizeof() 0
#endif

#ifndef NO_GESTURECONFIG
void cacheGESTURECONFIGFields(JNIEnv *env, jobject lpObject);
GESTURECONFIG *getGESTURECONFIGFields(JNIEnv *env, jobject lpObject, GESTURECONFIG *lpStruct);
void setGESTURECONFIGFields(JNIEnv *env, jobject lpObject, GESTURECONFIG *lpStruct);
#define GESTURECONFIG_sizeof() sizeof(GESTURECONFIG)
#else
#define cacheGESTURECONFIGFields(a,b)
#define getGESTURECONFIGFields(a,b,c) NULL
#define setGESTURECONFIGFields(a,b,c)
#define GESTURECONFIG_sizeof() 0
#endif

#ifndef NO_GESTUREINFO
void cacheGESTUREINFOFields(JNIEnv *env, jobject lpObject);
GESTUREINFO *getGESTUREINFOFields(JNIEnv *env, jobject lpObject, GESTUREINFO *lpStruct);
void setGESTUREINFOFields(JNIEnv *env, jobject lpObject, GESTUREINFO *lpStruct);
#define GESTUREINFO_sizeof() sizeof(GESTUREINFO)
#else
#define cacheGESTUREINFOFields(a,b)
#define getGESTUREINFOFields(a,b,c) NULL
#define setGESTUREINFOFields(a,b,c)
#define GESTUREINFO_sizeof() 0
#endif

#ifndef NO_GRADIENT_RECT
void cacheGRADIENT_RECTFields(JNIEnv *env, jobject lpObject);
GRADIENT_RECT *getGRADIENT_RECTFields(JNIEnv *env, jobject lpObject, GRADIENT_RECT *lpStruct);
void setGRADIENT_RECTFields(JNIEnv *env, jobject lpObject, GRADIENT_RECT *lpStruct);
#define GRADIENT_RECT_sizeof() sizeof(GRADIENT_RECT)
#else
#define cacheGRADIENT_RECTFields(a,b)
#define getGRADIENT_RECTFields(a,b,c) NULL
#define setGRADIENT_RECTFields(a,b,c)
#define GRADIENT_RECT_sizeof() 0
#endif

#ifndef NO_GUITHREADINFO
void cacheGUITHREADINFOFields(JNIEnv *env, jobject lpObject);
GUITHREADINFO *getGUITHREADINFOFields(JNIEnv *env, jobject lpObject, GUITHREADINFO *lpStruct);
void setGUITHREADINFOFields(JNIEnv *env, jobject lpObject, GUITHREADINFO *lpStruct);
#define GUITHREADINFO_sizeof() sizeof(GUITHREADINFO)
#else
#define cacheGUITHREADINFOFields(a,b)
#define getGUITHREADINFOFields(a,b,c) NULL
#define setGUITHREADINFOFields(a,b,c)
#define GUITHREADINFO_sizeof() 0
#endif

#ifndef NO_HDHITTESTINFO
void cacheHDHITTESTINFOFields(JNIEnv *env, jobject lpObject);
HDHITTESTINFO *getHDHITTESTINFOFields(JNIEnv *env, jobject lpObject, HDHITTESTINFO *lpStruct);
void setHDHITTESTINFOFields(JNIEnv *env, jobject lpObject, HDHITTESTINFO *lpStruct);
#define HDHITTESTINFO_sizeof() sizeof(HDHITTESTINFO)
#else
#define cacheHDHITTESTINFOFields(a,b)
#define getHDHITTESTINFOFields(a,b,c) NULL
#define setHDHITTESTINFOFields(a,b,c)
#define HDHITTESTINFO_sizeof() 0
#endif

#ifndef NO_HDITEM
void cacheHDITEMFields(JNIEnv *env, jobject lpObject);
HDITEM *getHDITEMFields(JNIEnv *env, jobject lpObject, HDITEM *lpStruct);
void setHDITEMFields(JNIEnv *env, jobject lpObject, HDITEM *lpStruct);
#define HDITEM_sizeof() sizeof(HDITEM)
#else
#define cacheHDITEMFields(a,b)
#define getHDITEMFields(a,b,c) NULL
#define setHDITEMFields(a,b,c)
#define HDITEM_sizeof() 0
#endif

#ifndef NO_HDLAYOUT
void cacheHDLAYOUTFields(JNIEnv *env, jobject lpObject);
HDLAYOUT *getHDLAYOUTFields(JNIEnv *env, jobject lpObject, HDLAYOUT *lpStruct);
void setHDLAYOUTFields(JNIEnv *env, jobject lpObject, HDLAYOUT *lpStruct);
#define HDLAYOUT_sizeof() sizeof(HDLAYOUT)
#else
#define cacheHDLAYOUTFields(a,b)
#define getHDLAYOUTFields(a,b,c) NULL
#define setHDLAYOUTFields(a,b,c)
#define HDLAYOUT_sizeof() 0
#endif

#ifndef NO_HELPINFO
void cacheHELPINFOFields(JNIEnv *env, jobject lpObject);
HELPINFO *getHELPINFOFields(JNIEnv *env, jobject lpObject, HELPINFO *lpStruct);
void setHELPINFOFields(JNIEnv *env, jobject lpObject, HELPINFO *lpStruct);
#define HELPINFO_sizeof() sizeof(HELPINFO)
#else
#define cacheHELPINFOFields(a,b)
#define getHELPINFOFields(a,b,c) NULL
#define setHELPINFOFields(a,b,c)
#define HELPINFO_sizeof() 0
#endif

#ifndef NO_HIGHCONTRAST
void cacheHIGHCONTRASTFields(JNIEnv *env, jobject lpObject);
HIGHCONTRAST *getHIGHCONTRASTFields(JNIEnv *env, jobject lpObject, HIGHCONTRAST *lpStruct);
void setHIGHCONTRASTFields(JNIEnv *env, jobject lpObject, HIGHCONTRAST *lpStruct);
#define HIGHCONTRAST_sizeof() sizeof(HIGHCONTRAST)
#else
#define cacheHIGHCONTRASTFields(a,b)
#define getHIGHCONTRASTFields(a,b,c) NULL
#define setHIGHCONTRASTFields(a,b,c)
#define HIGHCONTRAST_sizeof() 0
#endif

#ifndef NO_ICONINFO
void cacheICONINFOFields(JNIEnv *env, jobject lpObject);
ICONINFO *getICONINFOFields(JNIEnv *env, jobject lpObject, ICONINFO *lpStruct);
void setICONINFOFields(JNIEnv *env, jobject lpObject, ICONINFO *lpStruct);
#define ICONINFO_sizeof() sizeof(ICONINFO)
#else
#define cacheICONINFOFields(a,b)
#define getICONINFOFields(a,b,c) NULL
#define setICONINFOFields(a,b,c)
#define ICONINFO_sizeof() 0
#endif

#ifndef NO_INITCOMMONCONTROLSEX
void cacheINITCOMMONCONTROLSEXFields(JNIEnv *env, jobject lpObject);
INITCOMMONCONTROLSEX *getINITCOMMONCONTROLSEXFields(JNIEnv *env, jobject lpObject, INITCOMMONCONTROLSEX *lpStruct);
void setINITCOMMONCONTROLSEXFields(JNIEnv *env, jobject lpObject, INITCOMMONCONTROLSEX *lpStruct);
#define INITCOMMONCONTROLSEX_sizeof() sizeof(INITCOMMONCONTROLSEX)
#else
#define cacheINITCOMMONCONTROLSEXFields(a,b)
#define getINITCOMMONCONTROLSEXFields(a,b,c) NULL
#define setINITCOMMONCONTROLSEXFields(a,b,c)
#define INITCOMMONCONTROLSEX_sizeof() 0
#endif

#ifndef NO_INPUT
void cacheINPUTFields(JNIEnv *env, jobject lpObject);
INPUT *getINPUTFields(JNIEnv *env, jobject lpObject, INPUT *lpStruct);
void setINPUTFields(JNIEnv *env, jobject lpObject, INPUT *lpStruct);
#define INPUT_sizeof() sizeof(INPUT)
#else
#define cacheINPUTFields(a,b)
#define getINPUTFields(a,b,c) NULL
#define setINPUTFields(a,b,c)
#define INPUT_sizeof() 0
#endif

#ifndef NO_KEYBDINPUT
void cacheKEYBDINPUTFields(JNIEnv *env, jobject lpObject);
KEYBDINPUT *getKEYBDINPUTFields(JNIEnv *env, jobject lpObject, KEYBDINPUT *lpStruct);
void setKEYBDINPUTFields(JNIEnv *env, jobject lpObject, KEYBDINPUT *lpStruct);
#define KEYBDINPUT_sizeof() sizeof(KEYBDINPUT)
#else
#define cacheKEYBDINPUTFields(a,b)
#define getKEYBDINPUTFields(a,b,c) NULL
#define setKEYBDINPUTFields(a,b,c)
#define KEYBDINPUT_sizeof() 0
#endif

#ifndef NO_LITEM
void cacheLITEMFields(JNIEnv *env, jobject lpObject);
LITEM *getLITEMFields(JNIEnv *env, jobject lpObject, LITEM *lpStruct);
void setLITEMFields(JNIEnv *env, jobject lpObject, LITEM *lpStruct);
#define LITEM_sizeof() sizeof(LITEM)
#else
#define cacheLITEMFields(a,b)
#define getLITEMFields(a,b,c) NULL
#define setLITEMFields(a,b,c)
#define LITEM_sizeof() 0
#endif

#ifndef NO_LOGBRUSH
void cacheLOGBRUSHFields(JNIEnv *env, jobject lpObject);
LOGBRUSH *getLOGBRUSHFields(JNIEnv *env, jobject lpObject, LOGBRUSH *lpStruct);
void setLOGBRUSHFields(JNIEnv *env, jobject lpObject, LOGBRUSH *lpStruct);
#define LOGBRUSH_sizeof() sizeof(LOGBRUSH)
#else
#define cacheLOGBRUSHFields(a,b)
#define getLOGBRUSHFields(a,b,c) NULL
#define setLOGBRUSHFields(a,b,c)
#define LOGBRUSH_sizeof() 0
#endif

#ifndef NO_LOGFONT
void cacheLOGFONTFields(JNIEnv *env, jobject lpObject);
LOGFONT *getLOGFONTFields(JNIEnv *env, jobject lpObject, LOGFONT *lpStruct);
void setLOGFONTFields(JNIEnv *env, jobject lpObject, LOGFONT *lpStruct);
#define LOGFONT_sizeof() sizeof(LOGFONT)
#else
#define cacheLOGFONTFields(a,b)
#define getLOGFONTFields(a,b,c) NULL
#define setLOGFONTFields(a,b,c)
#define LOGFONT_sizeof() 0
#endif

#ifndef NO_LOGFONTA
void cacheLOGFONTAFields(JNIEnv *env, jobject lpObject);
LOGFONTA *getLOGFONTAFields(JNIEnv *env, jobject lpObject, LOGFONTA *lpStruct);
void setLOGFONTAFields(JNIEnv *env, jobject lpObject, LOGFONTA *lpStruct);
#define LOGFONTA_sizeof() sizeof(LOGFONTA)
#else
#define cacheLOGFONTAFields(a,b)
#define getLOGFONTAFields(a,b,c) NULL
#define setLOGFONTAFields(a,b,c)
#define LOGFONTA_sizeof() 0
#endif

#ifndef NO_LOGFONTW
void cacheLOGFONTWFields(JNIEnv *env, jobject lpObject);
LOGFONTW *getLOGFONTWFields(JNIEnv *env, jobject lpObject, LOGFONTW *lpStruct);
void setLOGFONTWFields(JNIEnv *env, jobject lpObject, LOGFONTW *lpStruct);
#define LOGFONTW_sizeof() sizeof(LOGFONTW)
#else
#define cacheLOGFONTWFields(a,b)
#define getLOGFONTWFields(a,b,c) NULL
#define setLOGFONTWFields(a,b,c)
#define LOGFONTW_sizeof() 0
#endif

#ifndef NO_LOGPEN
void cacheLOGPENFields(JNIEnv *env, jobject lpObject);
LOGPEN *getLOGPENFields(JNIEnv *env, jobject lpObject, LOGPEN *lpStruct);
void setLOGPENFields(JNIEnv *env, jobject lpObject, LOGPEN *lpStruct);
#define LOGPEN_sizeof() sizeof(LOGPEN)
#else
#define cacheLOGPENFields(a,b)
#define getLOGPENFields(a,b,c) NULL
#define setLOGPENFields(a,b,c)
#define LOGPEN_sizeof() 0
#endif

#ifndef NO_LVCOLUMN
void cacheLVCOLUMNFields(JNIEnv *env, jobject lpObject);
LVCOLUMN *getLVCOLUMNFields(JNIEnv *env, jobject lpObject, LVCOLUMN *lpStruct);
void setLVCOLUMNFields(JNIEnv *env, jobject lpObject, LVCOLUMN *lpStruct);
#define LVCOLUMN_sizeof() sizeof(LVCOLUMN)
#else
#define cacheLVCOLUMNFields(a,b)
#define getLVCOLUMNFields(a,b,c) NULL
#define setLVCOLUMNFields(a,b,c)
#define LVCOLUMN_sizeof() 0
#endif

#ifndef NO_LVHITTESTINFO
void cacheLVHITTESTINFOFields(JNIEnv *env, jobject lpObject);
LVHITTESTINFO *getLVHITTESTINFOFields(JNIEnv *env, jobject lpObject, LVHITTESTINFO *lpStruct);
void setLVHITTESTINFOFields(JNIEnv *env, jobject lpObject, LVHITTESTINFO *lpStruct);
#define LVHITTESTINFO_sizeof() sizeof(LVHITTESTINFO)
#else
#define cacheLVHITTESTINFOFields(a,b)
#define getLVHITTESTINFOFields(a,b,c) NULL
#define setLVHITTESTINFOFields(a,b,c)
#define LVHITTESTINFO_sizeof() 0
#endif

#ifndef NO_LVINSERTMARK
void cacheLVINSERTMARKFields(JNIEnv *env, jobject lpObject);
LVINSERTMARK *getLVINSERTMARKFields(JNIEnv *env, jobject lpObject, LVINSERTMARK *lpStruct);
void setLVINSERTMARKFields(JNIEnv *env, jobject lpObject, LVINSERTMARK *lpStruct);
#define LVINSERTMARK_sizeof() sizeof(LVINSERTMARK)
#else
#define cacheLVINSERTMARKFields(a,b)
#define getLVINSERTMARKFields(a,b,c) NULL
#define setLVINSERTMARKFields(a,b,c)
#define LVINSERTMARK_sizeof() 0
#endif

#ifndef NO_LVITEM
void cacheLVITEMFields(JNIEnv *env, jobject lpObject);
LVITEM *getLVITEMFields(JNIEnv *env, jobject lpObject, LVITEM *lpStruct);
void setLVITEMFields(JNIEnv *env, jobject lpObject, LVITEM *lpStruct);
#define LVITEM_sizeof() sizeof(LVITEM)
#else
#define cacheLVITEMFields(a,b)
#define getLVITEMFields(a,b,c) NULL
#define setLVITEMFields(a,b,c)
#define LVITEM_sizeof() 0
#endif

#ifndef NO_MARGINS
void cacheMARGINSFields(JNIEnv *env, jobject lpObject);
MARGINS *getMARGINSFields(JNIEnv *env, jobject lpObject, MARGINS *lpStruct);
void setMARGINSFields(JNIEnv *env, jobject lpObject, MARGINS *lpStruct);
#define MARGINS_sizeof() sizeof(MARGINS)
#else
#define cacheMARGINSFields(a,b)
#define getMARGINSFields(a,b,c) NULL
#define setMARGINSFields(a,b,c)
#define MARGINS_sizeof() 0
#endif

#ifndef NO_MCHITTESTINFO
void cacheMCHITTESTINFOFields(JNIEnv *env, jobject lpObject);
MCHITTESTINFO *getMCHITTESTINFOFields(JNIEnv *env, jobject lpObject, MCHITTESTINFO *lpStruct);
void setMCHITTESTINFOFields(JNIEnv *env, jobject lpObject, MCHITTESTINFO *lpStruct);
#define MCHITTESTINFO_sizeof() sizeof(MCHITTESTINFO)
#else
#define cacheMCHITTESTINFOFields(a,b)
#define getMCHITTESTINFOFields(a,b,c) NULL
#define setMCHITTESTINFOFields(a,b,c)
#define MCHITTESTINFO_sizeof() 0
#endif

#ifndef NO_MEASUREITEMSTRUCT
void cacheMEASUREITEMSTRUCTFields(JNIEnv *env, jobject lpObject);
MEASUREITEMSTRUCT *getMEASUREITEMSTRUCTFields(JNIEnv *env, jobject lpObject, MEASUREITEMSTRUCT *lpStruct);
void setMEASUREITEMSTRUCTFields(JNIEnv *env, jobject lpObject, MEASUREITEMSTRUCT *lpStruct);
#define MEASUREITEMSTRUCT_sizeof() sizeof(MEASUREITEMSTRUCT)
#else
#define cacheMEASUREITEMSTRUCTFields(a,b)
#define getMEASUREITEMSTRUCTFields(a,b,c) NULL
#define setMEASUREITEMSTRUCTFields(a,b,c)
#define MEASUREITEMSTRUCT_sizeof() 0
#endif

#ifndef NO_MENUBARINFO
void cacheMENUBARINFOFields(JNIEnv *env, jobject lpObject);
MENUBARINFO *getMENUBARINFOFields(JNIEnv *env, jobject lpObject, MENUBARINFO *lpStruct);
void setMENUBARINFOFields(JNIEnv *env, jobject lpObject, MENUBARINFO *lpStruct);
#define MENUBARINFO_sizeof() sizeof(MENUBARINFO)
#else
#define cacheMENUBARINFOFields(a,b)
#define getMENUBARINFOFields(a,b,c) NULL
#define setMENUBARINFOFields(a,b,c)
#define MENUBARINFO_sizeof() 0
#endif

#ifndef NO_MENUINFO
void cacheMENUINFOFields(JNIEnv *env, jobject lpObject);
MENUINFO *getMENUINFOFields(JNIEnv *env, jobject lpObject, MENUINFO *lpStruct);
void setMENUINFOFields(JNIEnv *env, jobject lpObject, MENUINFO *lpStruct);
#define MENUINFO_sizeof() sizeof(MENUINFO)
#else
#define cacheMENUINFOFields(a,b)
#define getMENUINFOFields(a,b,c) NULL
#define setMENUINFOFields(a,b,c)
#define MENUINFO_sizeof() 0
#endif

#ifndef NO_MENUITEMINFO
void cacheMENUITEMINFOFields(JNIEnv *env, jobject lpObject);
MENUITEMINFO *getMENUITEMINFOFields(JNIEnv *env, jobject lpObject, MENUITEMINFO *lpStruct);
void setMENUITEMINFOFields(JNIEnv *env, jobject lpObject, MENUITEMINFO *lpStruct);
#define MENUITEMINFO_sizeof() sizeof(MENUITEMINFO)
#else
#define cacheMENUITEMINFOFields(a,b)
#define getMENUITEMINFOFields(a,b,c) NULL
#define setMENUITEMINFOFields(a,b,c)
#define MENUITEMINFO_sizeof() 0
#endif

#ifndef NO_MINMAXINFO
void cacheMINMAXINFOFields(JNIEnv *env, jobject lpObject);
MINMAXINFO *getMINMAXINFOFields(JNIEnv *env, jobject lpObject, MINMAXINFO *lpStruct);
void setMINMAXINFOFields(JNIEnv *env, jobject lpObject, MINMAXINFO *lpStruct);
#define MINMAXINFO_sizeof() sizeof(MINMAXINFO)
#else
#define cacheMINMAXINFOFields(a,b)
#define getMINMAXINFOFields(a,b,c) NULL
#define setMINMAXINFOFields(a,b,c)
#define MINMAXINFO_sizeof() 0
#endif

#ifndef NO_MONITORINFO
void cacheMONITORINFOFields(JNIEnv *env, jobject lpObject);
MONITORINFO *getMONITORINFOFields(JNIEnv *env, jobject lpObject, MONITORINFO *lpStruct);
void setMONITORINFOFields(JNIEnv *env, jobject lpObject, MONITORINFO *lpStruct);
#define MONITORINFO_sizeof() sizeof(MONITORINFO)
#else
#define cacheMONITORINFOFields(a,b)
#define getMONITORINFOFields(a,b,c) NULL
#define setMONITORINFOFields(a,b,c)
#define MONITORINFO_sizeof() 0
#endif

#ifndef NO_MOUSEINPUT
void cacheMOUSEINPUTFields(JNIEnv *env, jobject lpObject);
MOUSEINPUT *getMOUSEINPUTFields(JNIEnv *env, jobject lpObject, MOUSEINPUT *lpStruct);
void setMOUSEINPUTFields(JNIEnv *env, jobject lpObject, MOUSEINPUT *lpStruct);
#define MOUSEINPUT_sizeof() sizeof(MOUSEINPUT)
#else
#define cacheMOUSEINPUTFields(a,b)
#define getMOUSEINPUTFields(a,b,c) NULL
#define setMOUSEINPUTFields(a,b,c)
#define MOUSEINPUT_sizeof() 0
#endif

#ifndef NO_MSG
void cacheMSGFields(JNIEnv *env, jobject lpObject);
MSG *getMSGFields(JNIEnv *env, jobject lpObject, MSG *lpStruct);
void setMSGFields(JNIEnv *env, jobject lpObject, MSG *lpStruct);
#define MSG_sizeof() sizeof(MSG)
#else
#define cacheMSGFields(a,b)
#define getMSGFields(a,b,c) NULL
#define setMSGFields(a,b,c)
#define MSG_sizeof() 0
#endif

#ifndef NO_NMCUSTOMDRAW
void cacheNMCUSTOMDRAWFields(JNIEnv *env, jobject lpObject);
NMCUSTOMDRAW *getNMCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMCUSTOMDRAW *lpStruct);
void setNMCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMCUSTOMDRAW *lpStruct);
#define NMCUSTOMDRAW_sizeof() sizeof(NMCUSTOMDRAW)
#else
#define cacheNMCUSTOMDRAWFields(a,b)
#define getNMCUSTOMDRAWFields(a,b,c) NULL
#define setNMCUSTOMDRAWFields(a,b,c)
#define NMCUSTOMDRAW_sizeof() 0
#endif

#ifndef NO_NMHDR
void cacheNMHDRFields(JNIEnv *env, jobject lpObject);
NMHDR *getNMHDRFields(JNIEnv *env, jobject lpObject, NMHDR *lpStruct);
void setNMHDRFields(JNIEnv *env, jobject lpObject, NMHDR *lpStruct);
#define NMHDR_sizeof() sizeof(NMHDR)
#else
#define cacheNMHDRFields(a,b)
#define getNMHDRFields(a,b,c) NULL
#define setNMHDRFields(a,b,c)
#define NMHDR_sizeof() 0
#endif

#ifndef NO_NMHEADER
void cacheNMHEADERFields(JNIEnv *env, jobject lpObject);
NMHEADER *getNMHEADERFields(JNIEnv *env, jobject lpObject, NMHEADER *lpStruct);
void setNMHEADERFields(JNIEnv *env, jobject lpObject, NMHEADER *lpStruct);
#define NMHEADER_sizeof() sizeof(NMHEADER)
#else
#define cacheNMHEADERFields(a,b)
#define getNMHEADERFields(a,b,c) NULL
#define setNMHEADERFields(a,b,c)
#define NMHEADER_sizeof() 0
#endif

#ifndef NO_NMLINK
void cacheNMLINKFields(JNIEnv *env, jobject lpObject);
NMLINK *getNMLINKFields(JNIEnv *env, jobject lpObject, NMLINK *lpStruct);
void setNMLINKFields(JNIEnv *env, jobject lpObject, NMLINK *lpStruct);
#define NMLINK_sizeof() sizeof(NMLINK)
#else
#define cacheNMLINKFields(a,b)
#define getNMLINKFields(a,b,c) NULL
#define setNMLINKFields(a,b,c)
#define NMLINK_sizeof() 0
#endif

#ifndef NO_NMLISTVIEW
void cacheNMLISTVIEWFields(JNIEnv *env, jobject lpObject);
NMLISTVIEW *getNMLISTVIEWFields(JNIEnv *env, jobject lpObject, NMLISTVIEW *lpStruct);
void setNMLISTVIEWFields(JNIEnv *env, jobject lpObject, NMLISTVIEW *lpStruct);
#define NMLISTVIEW_sizeof() sizeof(NMLISTVIEW)
#else
#define cacheNMLISTVIEWFields(a,b)
#define getNMLISTVIEWFields(a,b,c) NULL
#define setNMLISTVIEWFields(a,b,c)
#define NMLISTVIEW_sizeof() 0
#endif

#ifndef NO_NMLVCUSTOMDRAW
void cacheNMLVCUSTOMDRAWFields(JNIEnv *env, jobject lpObject);
NMLVCUSTOMDRAW *getNMLVCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMLVCUSTOMDRAW *lpStruct);
void setNMLVCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMLVCUSTOMDRAW *lpStruct);
#define NMLVCUSTOMDRAW_sizeof() sizeof(NMLVCUSTOMDRAW)
#else
#define cacheNMLVCUSTOMDRAWFields(a,b)
#define getNMLVCUSTOMDRAWFields(a,b,c) NULL
#define setNMLVCUSTOMDRAWFields(a,b,c)
#define NMLVCUSTOMDRAW_sizeof() 0
#endif

#ifndef NO_NMLVDISPINFO
void cacheNMLVDISPINFOFields(JNIEnv *env, jobject lpObject);
NMLVDISPINFO *getNMLVDISPINFOFields(JNIEnv *env, jobject lpObject, NMLVDISPINFO *lpStruct);
void setNMLVDISPINFOFields(JNIEnv *env, jobject lpObject, NMLVDISPINFO *lpStruct);
#define NMLVDISPINFO_sizeof() sizeof(NMLVDISPINFO)
#else
#define cacheNMLVDISPINFOFields(a,b)
#define getNMLVDISPINFOFields(a,b,c) NULL
#define setNMLVDISPINFOFields(a,b,c)
#define NMLVDISPINFO_sizeof() 0
#endif

#ifndef NO_NMLVFINDITEM
void cacheNMLVFINDITEMFields(JNIEnv *env, jobject lpObject);
NMLVFINDITEM *getNMLVFINDITEMFields(JNIEnv *env, jobject lpObject, NMLVFINDITEM *lpStruct);
void setNMLVFINDITEMFields(JNIEnv *env, jobject lpObject, NMLVFINDITEM *lpStruct);
#define NMLVFINDITEM_sizeof() sizeof(NMLVFINDITEM)
#else
#define cacheNMLVFINDITEMFields(a,b)
#define getNMLVFINDITEMFields(a,b,c) NULL
#define setNMLVFINDITEMFields(a,b,c)
#define NMLVFINDITEM_sizeof() 0
#endif

#ifndef NO_NMLVODSTATECHANGE
void cacheNMLVODSTATECHANGEFields(JNIEnv *env, jobject lpObject);
NMLVODSTATECHANGE *getNMLVODSTATECHANGEFields(JNIEnv *env, jobject lpObject, NMLVODSTATECHANGE *lpStruct);
void setNMLVODSTATECHANGEFields(JNIEnv *env, jobject lpObject, NMLVODSTATECHANGE *lpStruct);
#define NMLVODSTATECHANGE_sizeof() sizeof(NMLVODSTATECHANGE)
#else
#define cacheNMLVODSTATECHANGEFields(a,b)
#define getNMLVODSTATECHANGEFields(a,b,c) NULL
#define setNMLVODSTATECHANGEFields(a,b,c)
#define NMLVODSTATECHANGE_sizeof() 0
#endif

#ifndef NO_NMREBARCHEVRON
void cacheNMREBARCHEVRONFields(JNIEnv *env, jobject lpObject);
NMREBARCHEVRON *getNMREBARCHEVRONFields(JNIEnv *env, jobject lpObject, NMREBARCHEVRON *lpStruct);
void setNMREBARCHEVRONFields(JNIEnv *env, jobject lpObject, NMREBARCHEVRON *lpStruct);
#define NMREBARCHEVRON_sizeof() sizeof(NMREBARCHEVRON)
#else
#define cacheNMREBARCHEVRONFields(a,b)
#define getNMREBARCHEVRONFields(a,b,c) NULL
#define setNMREBARCHEVRONFields(a,b,c)
#define NMREBARCHEVRON_sizeof() 0
#endif

#ifndef NO_NMREBARCHILDSIZE
void cacheNMREBARCHILDSIZEFields(JNIEnv *env, jobject lpObject);
NMREBARCHILDSIZE *getNMREBARCHILDSIZEFields(JNIEnv *env, jobject lpObject, NMREBARCHILDSIZE *lpStruct);
void setNMREBARCHILDSIZEFields(JNIEnv *env, jobject lpObject, NMREBARCHILDSIZE *lpStruct);
#define NMREBARCHILDSIZE_sizeof() sizeof(NMREBARCHILDSIZE)
#else
#define cacheNMREBARCHILDSIZEFields(a,b)
#define getNMREBARCHILDSIZEFields(a,b,c) NULL
#define setNMREBARCHILDSIZEFields(a,b,c)
#define NMREBARCHILDSIZE_sizeof() 0
#endif

#ifndef NO_NMRGINFO
void cacheNMRGINFOFields(JNIEnv *env, jobject lpObject);
NMRGINFO *getNMRGINFOFields(JNIEnv *env, jobject lpObject, NMRGINFO *lpStruct);
void setNMRGINFOFields(JNIEnv *env, jobject lpObject, NMRGINFO *lpStruct);
#define NMRGINFO_sizeof() sizeof(NMRGINFO)
#else
#define cacheNMRGINFOFields(a,b)
#define getNMRGINFOFields(a,b,c) NULL
#define setNMRGINFOFields(a,b,c)
#define NMRGINFO_sizeof() 0
#endif

#ifndef NO_NMTBHOTITEM
void cacheNMTBHOTITEMFields(JNIEnv *env, jobject lpObject);
NMTBHOTITEM *getNMTBHOTITEMFields(JNIEnv *env, jobject lpObject, NMTBHOTITEM *lpStruct);
void setNMTBHOTITEMFields(JNIEnv *env, jobject lpObject, NMTBHOTITEM *lpStruct);
#define NMTBHOTITEM_sizeof() sizeof(NMTBHOTITEM)
#else
#define cacheNMTBHOTITEMFields(a,b)
#define getNMTBHOTITEMFields(a,b,c) NULL
#define setNMTBHOTITEMFields(a,b,c)
#define NMTBHOTITEM_sizeof() 0
#endif

#ifndef NO_NMTOOLBAR
void cacheNMTOOLBARFields(JNIEnv *env, jobject lpObject);
NMTOOLBAR *getNMTOOLBARFields(JNIEnv *env, jobject lpObject, NMTOOLBAR *lpStruct);
void setNMTOOLBARFields(JNIEnv *env, jobject lpObject, NMTOOLBAR *lpStruct);
#define NMTOOLBAR_sizeof() sizeof(NMTOOLBAR)
#else
#define cacheNMTOOLBARFields(a,b)
#define getNMTOOLBARFields(a,b,c) NULL
#define setNMTOOLBARFields(a,b,c)
#define NMTOOLBAR_sizeof() 0
#endif

#ifndef NO_NMTREEVIEW
void cacheNMTREEVIEWFields(JNIEnv *env, jobject lpObject);
NMTREEVIEW *getNMTREEVIEWFields(JNIEnv *env, jobject lpObject, NMTREEVIEW *lpStruct);
void setNMTREEVIEWFields(JNIEnv *env, jobject lpObject, NMTREEVIEW *lpStruct);
#define NMTREEVIEW_sizeof() sizeof(NMTREEVIEW)
#else
#define cacheNMTREEVIEWFields(a,b)
#define getNMTREEVIEWFields(a,b,c) NULL
#define setNMTREEVIEWFields(a,b,c)
#define NMTREEVIEW_sizeof() 0
#endif

#ifndef NO_NMTTCUSTOMDRAW
void cacheNMTTCUSTOMDRAWFields(JNIEnv *env, jobject lpObject);
NMTTCUSTOMDRAW *getNMTTCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMTTCUSTOMDRAW *lpStruct);
void setNMTTCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMTTCUSTOMDRAW *lpStruct);
#define NMTTCUSTOMDRAW_sizeof() sizeof(NMTTCUSTOMDRAW)
#else
#define cacheNMTTCUSTOMDRAWFields(a,b)
#define getNMTTCUSTOMDRAWFields(a,b,c) NULL
#define setNMTTCUSTOMDRAWFields(a,b,c)
#define NMTTCUSTOMDRAW_sizeof() 0
#endif

#ifndef NO_NMTTDISPINFO
void cacheNMTTDISPINFOFields(JNIEnv *env, jobject lpObject);
NMTTDISPINFO *getNMTTDISPINFOFields(JNIEnv *env, jobject lpObject, NMTTDISPINFO *lpStruct);
void setNMTTDISPINFOFields(JNIEnv *env, jobject lpObject, NMTTDISPINFO *lpStruct);
#define NMTTDISPINFO_sizeof() sizeof(NMTTDISPINFO)
#else
#define cacheNMTTDISPINFOFields(a,b)
#define getNMTTDISPINFOFields(a,b,c) NULL
#define setNMTTDISPINFOFields(a,b,c)
#define NMTTDISPINFO_sizeof() 0
#endif

#ifndef NO_NMTTDISPINFOA
void cacheNMTTDISPINFOAFields(JNIEnv *env, jobject lpObject);
NMTTDISPINFOA *getNMTTDISPINFOAFields(JNIEnv *env, jobject lpObject, NMTTDISPINFOA *lpStruct);
void setNMTTDISPINFOAFields(JNIEnv *env, jobject lpObject, NMTTDISPINFOA *lpStruct);
#define NMTTDISPINFOA_sizeof() sizeof(NMTTDISPINFOA)
#else
#define cacheNMTTDISPINFOAFields(a,b)
#define getNMTTDISPINFOAFields(a,b,c) NULL
#define setNMTTDISPINFOAFields(a,b,c)
#define NMTTDISPINFOA_sizeof() 0
#endif

#ifndef NO_NMTTDISPINFOW
void cacheNMTTDISPINFOWFields(JNIEnv *env, jobject lpObject);
NMTTDISPINFOW *getNMTTDISPINFOWFields(JNIEnv *env, jobject lpObject, NMTTDISPINFOW *lpStruct);
void setNMTTDISPINFOWFields(JNIEnv *env, jobject lpObject, NMTTDISPINFOW *lpStruct);
#define NMTTDISPINFOW_sizeof() sizeof(NMTTDISPINFOW)
#else
#define cacheNMTTDISPINFOWFields(a,b)
#define getNMTTDISPINFOWFields(a,b,c) NULL
#define setNMTTDISPINFOWFields(a,b,c)
#define NMTTDISPINFOW_sizeof() 0
#endif

#ifndef NO_NMTVCUSTOMDRAW
void cacheNMTVCUSTOMDRAWFields(JNIEnv *env, jobject lpObject);
NMTVCUSTOMDRAW *getNMTVCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMTVCUSTOMDRAW *lpStruct);
void setNMTVCUSTOMDRAWFields(JNIEnv *env, jobject lpObject, NMTVCUSTOMDRAW *lpStruct);
#define NMTVCUSTOMDRAW_sizeof() sizeof(NMTVCUSTOMDRAW)
#else
#define cacheNMTVCUSTOMDRAWFields(a,b)
#define getNMTVCUSTOMDRAWFields(a,b,c) NULL
#define setNMTVCUSTOMDRAWFields(a,b,c)
#define NMTVCUSTOMDRAW_sizeof() 0
#endif

#ifndef NO_NMTVDISPINFO
void cacheNMTVDISPINFOFields(JNIEnv *env, jobject lpObject);
NMTVDISPINFO *getNMTVDISPINFOFields(JNIEnv *env, jobject lpObject, NMTVDISPINFO *lpStruct);
void setNMTVDISPINFOFields(JNIEnv *env, jobject lpObject, NMTVDISPINFO *lpStruct);
#define NMTVDISPINFO_sizeof() sizeof(NMTVDISPINFO)
#else
#define cacheNMTVDISPINFOFields(a,b)
#define getNMTVDISPINFOFields(a,b,c) NULL
#define setNMTVDISPINFOFields(a,b,c)
#define NMTVDISPINFO_sizeof() 0
#endif

#ifndef NO_NMTVITEMCHANGE
void cacheNMTVITEMCHANGEFields(JNIEnv *env, jobject lpObject);
NMTVITEMCHANGE *getNMTVITEMCHANGEFields(JNIEnv *env, jobject lpObject, NMTVITEMCHANGE *lpStruct);
void setNMTVITEMCHANGEFields(JNIEnv *env, jobject lpObject, NMTVITEMCHANGE *lpStruct);
#define NMTVITEMCHANGE_sizeof() sizeof(NMTVITEMCHANGE)
#else
#define cacheNMTVITEMCHANGEFields(a,b)
#define getNMTVITEMCHANGEFields(a,b,c) NULL
#define setNMTVITEMCHANGEFields(a,b,c)
#define NMTVITEMCHANGE_sizeof() 0
#endif

#ifndef NO_NMUPDOWN
void cacheNMUPDOWNFields(JNIEnv *env, jobject lpObject);
NMUPDOWN *getNMUPDOWNFields(JNIEnv *env, jobject lpObject, NMUPDOWN *lpStruct);
void setNMUPDOWNFields(JNIEnv *env, jobject lpObject, NMUPDOWN *lpStruct);
#define NMUPDOWN_sizeof() sizeof(NMUPDOWN)
#else
#define cacheNMUPDOWNFields(a,b)
#define getNMUPDOWNFields(a,b,c) NULL
#define setNMUPDOWNFields(a,b,c)
#define NMUPDOWN_sizeof() 0
#endif

#ifndef NO_NONCLIENTMETRICS
void cacheNONCLIENTMETRICSFields(JNIEnv *env, jobject lpObject);
NONCLIENTMETRICS *getNONCLIENTMETRICSFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICS *lpStruct);
void setNONCLIENTMETRICSFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICS *lpStruct);
#define NONCLIENTMETRICS_sizeof() sizeof(NONCLIENTMETRICS)
#else
#define cacheNONCLIENTMETRICSFields(a,b)
#define getNONCLIENTMETRICSFields(a,b,c) NULL
#define setNONCLIENTMETRICSFields(a,b,c)
#define NONCLIENTMETRICS_sizeof() 0
#endif

#ifndef NO_NONCLIENTMETRICSA
void cacheNONCLIENTMETRICSAFields(JNIEnv *env, jobject lpObject);
NONCLIENTMETRICSA *getNONCLIENTMETRICSAFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICSA *lpStruct);
void setNONCLIENTMETRICSAFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICSA *lpStruct);
#define NONCLIENTMETRICSA_sizeof() sizeof(NONCLIENTMETRICSA)
#else
#define cacheNONCLIENTMETRICSAFields(a,b)
#define getNONCLIENTMETRICSAFields(a,b,c) NULL
#define setNONCLIENTMETRICSAFields(a,b,c)
#define NONCLIENTMETRICSA_sizeof() 0
#endif

#ifndef NO_NONCLIENTMETRICSW
void cacheNONCLIENTMETRICSWFields(JNIEnv *env, jobject lpObject);
NONCLIENTMETRICSW *getNONCLIENTMETRICSWFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICSW *lpStruct);
void setNONCLIENTMETRICSWFields(JNIEnv *env, jobject lpObject, NONCLIENTMETRICSW *lpStruct);
#define NONCLIENTMETRICSW_sizeof() sizeof(NONCLIENTMETRICSW)
#else
#define cacheNONCLIENTMETRICSWFields(a,b)
#define getNONCLIENTMETRICSWFields(a,b,c) NULL
#define setNONCLIENTMETRICSWFields(a,b,c)
#define NONCLIENTMETRICSW_sizeof() 0
#endif

#ifndef NO_NOTIFYICONDATA
void cacheNOTIFYICONDATAFields(JNIEnv *env, jobject lpObject);
NOTIFYICONDATA *getNOTIFYICONDATAFields(JNIEnv *env, jobject lpObject, NOTIFYICONDATA *lpStruct);
void setNOTIFYICONDATAFields(JNIEnv *env, jobject lpObject, NOTIFYICONDATA *lpStruct);
#define NOTIFYICONDATA_sizeof() sizeof(NOTIFYICONDATA)
#else
#define cacheNOTIFYICONDATAFields(a,b)
#define getNOTIFYICONDATAFields(a,b,c) NULL
#define setNOTIFYICONDATAFields(a,b,c)
#define NOTIFYICONDATA_sizeof() 0
#endif

#ifndef NO_NOTIFYICONDATAA
void cacheNOTIFYICONDATAAFields(JNIEnv *env, jobject lpObject);
NOTIFYICONDATAA *getNOTIFYICONDATAAFields(JNIEnv *env, jobject lpObject, NOTIFYICONDATAA *lpStruct);
void setNOTIFYICONDATAAFields(JNIEnv *env, jobject lpObject, NOTIFYICONDATAA *lpStruct);
#define NOTIFYICONDATAA_sizeof() sizeof(NOTIFYICONDATAA)
#else
#define cacheNOTIFYICONDATAAFields(a,b)
#define getNOTIFYICONDATAAFields(a,b,c) NULL
#define setNOTIFYICONDATAAFields(a,b,c)
#define NOTIFYICONDATAA_sizeof() 0
#endif

#ifndef NO_NOTIFYICONDATAW
void cacheNOTIFYICONDATAWFields(JNIEnv *env, jobject lpObject);
NOTIFYICONDATAW *getNOTIFYICONDATAWFields(JNIEnv *env, jobject lpObject, NOTIFYICONDATAW *lpStruct);
void setNOTIFYICONDATAWFields(JNIEnv *env, jobject lpObject, NOTIFYICONDATAW *lpStruct);
#define NOTIFYICONDATAW_sizeof() sizeof(NOTIFYICONDATAW)
#else
#define cacheNOTIFYICONDATAWFields(a,b)
#define getNOTIFYICONDATAWFields(a,b,c) NULL
#define setNOTIFYICONDATAWFields(a,b,c)
#define NOTIFYICONDATAW_sizeof() 0
#endif

#ifndef NO_OFNOTIFY
void cacheOFNOTIFYFields(JNIEnv *env, jobject lpObject);
OFNOTIFY *getOFNOTIFYFields(JNIEnv *env, jobject lpObject, OFNOTIFY *lpStruct);
void setOFNOTIFYFields(JNIEnv *env, jobject lpObject, OFNOTIFY *lpStruct);
#define OFNOTIFY_sizeof() sizeof(OFNOTIFY)
#else
#define cacheOFNOTIFYFields(a,b)
#define getOFNOTIFYFields(a,b,c) NULL
#define setOFNOTIFYFields(a,b,c)
#define OFNOTIFY_sizeof() 0
#endif

#ifndef NO_OPENFILENAME
void cacheOPENFILENAMEFields(JNIEnv *env, jobject lpObject);
OPENFILENAME *getOPENFILENAMEFields(JNIEnv *env, jobject lpObject, OPENFILENAME *lpStruct);
void setOPENFILENAMEFields(JNIEnv *env, jobject lpObject, OPENFILENAME *lpStruct);
#define OPENFILENAME_sizeof() sizeof(OPENFILENAME)
#else
#define cacheOPENFILENAMEFields(a,b)
#define getOPENFILENAMEFields(a,b,c) NULL
#define setOPENFILENAMEFields(a,b,c)
#define OPENFILENAME_sizeof() 0
#endif

#ifndef NO_OSVERSIONINFO
void cacheOSVERSIONINFOFields(JNIEnv *env, jobject lpObject);
OSVERSIONINFO *getOSVERSIONINFOFields(JNIEnv *env, jobject lpObject, OSVERSIONINFO *lpStruct);
void setOSVERSIONINFOFields(JNIEnv *env, jobject lpObject, OSVERSIONINFO *lpStruct);
#define OSVERSIONINFO_sizeof() sizeof(OSVERSIONINFO)
#else
#define cacheOSVERSIONINFOFields(a,b)
#define getOSVERSIONINFOFields(a,b,c) NULL
#define setOSVERSIONINFOFields(a,b,c)
#define OSVERSIONINFO_sizeof() 0
#endif

#ifndef NO_OSVERSIONINFOA
void cacheOSVERSIONINFOAFields(JNIEnv *env, jobject lpObject);
OSVERSIONINFOA *getOSVERSIONINFOAFields(JNIEnv *env, jobject lpObject, OSVERSIONINFOA *lpStruct);
void setOSVERSIONINFOAFields(JNIEnv *env, jobject lpObject, OSVERSIONINFOA *lpStruct);
#define OSVERSIONINFOA_sizeof() sizeof(OSVERSIONINFOA)
#else
#define cacheOSVERSIONINFOAFields(a,b)
#define getOSVERSIONINFOAFields(a,b,c) NULL
#define setOSVERSIONINFOAFields(a,b,c)
#define OSVERSIONINFOA_sizeof() 0
#endif

#ifndef NO_OSVERSIONINFOEX
void cacheOSVERSIONINFOEXFields(JNIEnv *env, jobject lpObject);
OSVERSIONINFOEX *getOSVERSIONINFOEXFields(JNIEnv *env, jobject lpObject, OSVERSIONINFOEX *lpStruct);
void setOSVERSIONINFOEXFields(JNIEnv *env, jobject lpObject, OSVERSIONINFOEX *lpStruct);
#define OSVERSIONINFOEX_sizeof() sizeof(OSVERSIONINFOEX)
#else
#define cacheOSVERSIONINFOEXFields(a,b)
#define getOSVERSIONINFOEXFields(a,b,c) NULL
#define setOSVERSIONINFOEXFields(a,b,c)
#define OSVERSIONINFOEX_sizeof() 0
#endif

#ifndef NO_OSVERSIONINFOEXA
void cacheOSVERSIONINFOEXAFields(JNIEnv *env, jobject lpObject);
OSVERSIONINFOEXA *getOSVERSIONINFOEXAFields(JNIEnv *env, jobject lpObject, OSVERSIONINFOEXA *lpStruct);
void setOSVERSIONINFOEXAFields(JNIEnv *env, jobject lpObject, OSVERSIONINFOEXA *lpStruct);
#define OSVERSIONINFOEXA_sizeof() sizeof(OSVERSIONINFOEXA)
#else
#define cacheOSVERSIONINFOEXAFields(a,b)
#define getOSVERSIONINFOEXAFields(a,b,c) NULL
#define setOSVERSIONINFOEXAFields(a,b,c)
#define OSVERSIONINFOEXA_sizeof() 0
#endif

#ifndef NO_OSVERSIONINFOEXW
void cacheOSVERSIONINFOEXWFields(JNIEnv *env, jobject lpObject);
OSVERSIONINFOEXW *getOSVERSIONINFOEXWFields(JNIEnv *env, jobject lpObject, OSVERSIONINFOEXW *lpStruct);
void setOSVERSIONINFOEXWFields(JNIEnv *env, jobject lpObject, OSVERSIONINFOEXW *lpStruct);
#define OSVERSIONINFOEXW_sizeof() sizeof(OSVERSIONINFOEXW)
#else
#define cacheOSVERSIONINFOEXWFields(a,b)
#define getOSVERSIONINFOEXWFields(a,b,c) NULL
#define setOSVERSIONINFOEXWFields(a,b,c)
#define OSVERSIONINFOEXW_sizeof() 0
#endif

#ifndef NO_OSVERSIONINFOW
void cacheOSVERSIONINFOWFields(JNIEnv *env, jobject lpObject);
OSVERSIONINFOW *getOSVERSIONINFOWFields(JNIEnv *env, jobject lpObject, OSVERSIONINFOW *lpStruct);
void setOSVERSIONINFOWFields(JNIEnv *env, jobject lpObject, OSVERSIONINFOW *lpStruct);
#define OSVERSIONINFOW_sizeof() sizeof(OSVERSIONINFOW)
#else
#define cacheOSVERSIONINFOWFields(a,b)
#define getOSVERSIONINFOWFields(a,b,c) NULL
#define setOSVERSIONINFOWFields(a,b,c)
#define OSVERSIONINFOW_sizeof() 0
#endif

#ifndef NO_OUTLINETEXTMETRIC
void cacheOUTLINETEXTMETRICFields(JNIEnv *env, jobject lpObject);
OUTLINETEXTMETRIC *getOUTLINETEXTMETRICFields(JNIEnv *env, jobject lpObject, OUTLINETEXTMETRIC *lpStruct);
void setOUTLINETEXTMETRICFields(JNIEnv *env, jobject lpObject, OUTLINETEXTMETRIC *lpStruct);
#define OUTLINETEXTMETRIC_sizeof() sizeof(OUTLINETEXTMETRIC)
#else
#define cacheOUTLINETEXTMETRICFields(a,b)
#define getOUTLINETEXTMETRICFields(a,b,c) NULL
#define setOUTLINETEXTMETRICFields(a,b,c)
#define OUTLINETEXTMETRIC_sizeof() 0
#endif

#ifndef NO_OUTLINETEXTMETRICA
void cacheOUTLINETEXTMETRICAFields(JNIEnv *env, jobject lpObject);
OUTLINETEXTMETRICA *getOUTLINETEXTMETRICAFields(JNIEnv *env, jobject lpObject, OUTLINETEXTMETRICA *lpStruct);
void setOUTLINETEXTMETRICAFields(JNIEnv *env, jobject lpObject, OUTLINETEXTMETRICA *lpStruct);
#define OUTLINETEXTMETRICA_sizeof() sizeof(OUTLINETEXTMETRICA)
#else
#define cacheOUTLINETEXTMETRICAFields(a,b)
#define getOUTLINETEXTMETRICAFields(a,b,c) NULL
#define setOUTLINETEXTMETRICAFields(a,b,c)
#define OUTLINETEXTMETRICA_sizeof() 0
#endif

#ifndef NO_OUTLINETEXTMETRICW
void cacheOUTLINETEXTMETRICWFields(JNIEnv *env, jobject lpObject);
OUTLINETEXTMETRICW *getOUTLINETEXTMETRICWFields(JNIEnv *env, jobject lpObject, OUTLINETEXTMETRICW *lpStruct);
void setOUTLINETEXTMETRICWFields(JNIEnv *env, jobject lpObject, OUTLINETEXTMETRICW *lpStruct);
#define OUTLINETEXTMETRICW_sizeof() sizeof(OUTLINETEXTMETRICW)
#else
#define cacheOUTLINETEXTMETRICWFields(a,b)
#define getOUTLINETEXTMETRICWFields(a,b,c) NULL
#define setOUTLINETEXTMETRICWFields(a,b,c)
#define OUTLINETEXTMETRICW_sizeof() 0
#endif

#ifndef NO_PAINTSTRUCT
void cachePAINTSTRUCTFields(JNIEnv *env, jobject lpObject);
PAINTSTRUCT *getPAINTSTRUCTFields(JNIEnv *env, jobject lpObject, PAINTSTRUCT *lpStruct);
void setPAINTSTRUCTFields(JNIEnv *env, jobject lpObject, PAINTSTRUCT *lpStruct);
#define PAINTSTRUCT_sizeof() sizeof(PAINTSTRUCT)
#else
#define cachePAINTSTRUCTFields(a,b)
#define getPAINTSTRUCTFields(a,b,c) NULL
#define setPAINTSTRUCTFields(a,b,c)
#define PAINTSTRUCT_sizeof() 0
#endif

#ifndef NO_PANOSE
void cachePANOSEFields(JNIEnv *env, jobject lpObject);
PANOSE *getPANOSEFields(JNIEnv *env, jobject lpObject, PANOSE *lpStruct);
void setPANOSEFields(JNIEnv *env, jobject lpObject, PANOSE *lpStruct);
#define PANOSE_sizeof() sizeof(PANOSE)
#else
#define cachePANOSEFields(a,b)
#define getPANOSEFields(a,b,c) NULL
#define setPANOSEFields(a,b,c)
#define PANOSE_sizeof() 0
#endif

#ifndef NO_POINT
void cachePOINTFields(JNIEnv *env, jobject lpObject);
POINT *getPOINTFields(JNIEnv *env, jobject lpObject, POINT *lpStruct);
void setPOINTFields(JNIEnv *env, jobject lpObject, POINT *lpStruct);
#define POINT_sizeof() sizeof(POINT)
#else
#define cachePOINTFields(a,b)
#define getPOINTFields(a,b,c) NULL
#define setPOINTFields(a,b,c)
#define POINT_sizeof() 0
#endif

#ifndef NO_PRINTDLG
void cachePRINTDLGFields(JNIEnv *env, jobject lpObject);
PRINTDLG *getPRINTDLGFields(JNIEnv *env, jobject lpObject, PRINTDLG *lpStruct);
void setPRINTDLGFields(JNIEnv *env, jobject lpObject, PRINTDLG *lpStruct);
#define PRINTDLG_sizeof() sizeof(PRINTDLG)
#else
#define cachePRINTDLGFields(a,b)
#define getPRINTDLGFields(a,b,c) NULL
#define setPRINTDLGFields(a,b,c)
#define PRINTDLG_sizeof() 0
#endif

#ifndef NO_PROCESS_INFORMATION
void cachePROCESS_INFORMATIONFields(JNIEnv *env, jobject lpObject);
PROCESS_INFORMATION *getPROCESS_INFORMATIONFields(JNIEnv *env, jobject lpObject, PROCESS_INFORMATION *lpStruct);
void setPROCESS_INFORMATIONFields(JNIEnv *env, jobject lpObject, PROCESS_INFORMATION *lpStruct);
#define PROCESS_INFORMATION_sizeof() sizeof(PROCESS_INFORMATION)
#else
#define cachePROCESS_INFORMATIONFields(a,b)
#define getPROCESS_INFORMATIONFields(a,b,c) NULL
#define setPROCESS_INFORMATIONFields(a,b,c)
#define PROCESS_INFORMATION_sizeof() 0
#endif

#ifndef NO_PROPERTYKEY
void cachePROPERTYKEYFields(JNIEnv *env, jobject lpObject);
PROPERTYKEY *getPROPERTYKEYFields(JNIEnv *env, jobject lpObject, PROPERTYKEY *lpStruct);
void setPROPERTYKEYFields(JNIEnv *env, jobject lpObject, PROPERTYKEY *lpStruct);
#define PROPERTYKEY_sizeof() sizeof(PROPERTYKEY)
#else
#define cachePROPERTYKEYFields(a,b)
#define getPROPERTYKEYFields(a,b,c) NULL
#define setPROPERTYKEYFields(a,b,c)
#define PROPERTYKEY_sizeof() 0
#endif

#ifndef NO_REBARBANDINFO
void cacheREBARBANDINFOFields(JNIEnv *env, jobject lpObject);
REBARBANDINFO *getREBARBANDINFOFields(JNIEnv *env, jobject lpObject, REBARBANDINFO *lpStruct);
void setREBARBANDINFOFields(JNIEnv *env, jobject lpObject, REBARBANDINFO *lpStruct);
#define REBARBANDINFO_sizeof() sizeof(REBARBANDINFO)
#else
#define cacheREBARBANDINFOFields(a,b)
#define getREBARBANDINFOFields(a,b,c) NULL
#define setREBARBANDINFOFields(a,b,c)
#define REBARBANDINFO_sizeof() 0
#endif

#ifndef NO_RECT
void cacheRECTFields(JNIEnv *env, jobject lpObject);
RECT *getRECTFields(JNIEnv *env, jobject lpObject, RECT *lpStruct);
void setRECTFields(JNIEnv *env, jobject lpObject, RECT *lpStruct);
#define RECT_sizeof() sizeof(RECT)
#else
#define cacheRECTFields(a,b)
#define getRECTFields(a,b,c) NULL
#define setRECTFields(a,b,c)
#define RECT_sizeof() 0
#endif

#ifndef NO_SAFEARRAY
void cacheSAFEARRAYFields(JNIEnv *env, jobject lpObject);
SAFEARRAY *getSAFEARRAYFields(JNIEnv *env, jobject lpObject, SAFEARRAY *lpStruct);
void setSAFEARRAYFields(JNIEnv *env, jobject lpObject, SAFEARRAY *lpStruct);
#define SAFEARRAY_sizeof() sizeof(SAFEARRAY)
#else
#define cacheSAFEARRAYFields(a,b)
#define getSAFEARRAYFields(a,b,c) NULL
#define setSAFEARRAYFields(a,b,c)
#define SAFEARRAY_sizeof() 0
#endif

#ifndef NO_SAFEARRAYBOUND
void cacheSAFEARRAYBOUNDFields(JNIEnv *env, jobject lpObject);
SAFEARRAYBOUND *getSAFEARRAYBOUNDFields(JNIEnv *env, jobject lpObject, SAFEARRAYBOUND *lpStruct);
void setSAFEARRAYBOUNDFields(JNIEnv *env, jobject lpObject, SAFEARRAYBOUND *lpStruct);
#define SAFEARRAYBOUND_sizeof() sizeof(SAFEARRAYBOUND)
#else
#define cacheSAFEARRAYBOUNDFields(a,b)
#define getSAFEARRAYBOUNDFields(a,b,c) NULL
#define setSAFEARRAYBOUNDFields(a,b,c)
#define SAFEARRAYBOUND_sizeof() 0
#endif

#ifndef NO_SCRIPT_ANALYSIS
void cacheSCRIPT_ANALYSISFields(JNIEnv *env, jobject lpObject);
SCRIPT_ANALYSIS *getSCRIPT_ANALYSISFields(JNIEnv *env, jobject lpObject, SCRIPT_ANALYSIS *lpStruct);
void setSCRIPT_ANALYSISFields(JNIEnv *env, jobject lpObject, SCRIPT_ANALYSIS *lpStruct);
#define SCRIPT_ANALYSIS_sizeof() sizeof(SCRIPT_ANALYSIS)
#else
#define cacheSCRIPT_ANALYSISFields(a,b)
#define getSCRIPT_ANALYSISFields(a,b,c) NULL
#define setSCRIPT_ANALYSISFields(a,b,c)
#define SCRIPT_ANALYSIS_sizeof() 0
#endif

#ifndef NO_SCRIPT_CONTROL
void cacheSCRIPT_CONTROLFields(JNIEnv *env, jobject lpObject);
SCRIPT_CONTROL *getSCRIPT_CONTROLFields(JNIEnv *env, jobject lpObject, SCRIPT_CONTROL *lpStruct);
void setSCRIPT_CONTROLFields(JNIEnv *env, jobject lpObject, SCRIPT_CONTROL *lpStruct);
#define SCRIPT_CONTROL_sizeof() sizeof(SCRIPT_CONTROL)
#else
#define cacheSCRIPT_CONTROLFields(a,b)
#define getSCRIPT_CONTROLFields(a,b,c) NULL
#define setSCRIPT_CONTROLFields(a,b,c)
#define SCRIPT_CONTROL_sizeof() 0
#endif

#ifndef NO_SCRIPT_DIGITSUBSTITUTE
void cacheSCRIPT_DIGITSUBSTITUTEFields(JNIEnv *env, jobject lpObject);
SCRIPT_DIGITSUBSTITUTE *getSCRIPT_DIGITSUBSTITUTEFields(JNIEnv *env, jobject lpObject, SCRIPT_DIGITSUBSTITUTE *lpStruct);
void setSCRIPT_DIGITSUBSTITUTEFields(JNIEnv *env, jobject lpObject, SCRIPT_DIGITSUBSTITUTE *lpStruct);
#define SCRIPT_DIGITSUBSTITUTE_sizeof() sizeof(SCRIPT_DIGITSUBSTITUTE)
#else
#define cacheSCRIPT_DIGITSUBSTITUTEFields(a,b)
#define getSCRIPT_DIGITSUBSTITUTEFields(a,b,c) NULL
#define setSCRIPT_DIGITSUBSTITUTEFields(a,b,c)
#define SCRIPT_DIGITSUBSTITUTE_sizeof() 0
#endif

#ifndef NO_SCRIPT_FONTPROPERTIES
void cacheSCRIPT_FONTPROPERTIESFields(JNIEnv *env, jobject lpObject);
SCRIPT_FONTPROPERTIES *getSCRIPT_FONTPROPERTIESFields(JNIEnv *env, jobject lpObject, SCRIPT_FONTPROPERTIES *lpStruct);
void setSCRIPT_FONTPROPERTIESFields(JNIEnv *env, jobject lpObject, SCRIPT_FONTPROPERTIES *lpStruct);
#define SCRIPT_FONTPROPERTIES_sizeof() sizeof(SCRIPT_FONTPROPERTIES)
#else
#define cacheSCRIPT_FONTPROPERTIESFields(a,b)
#define getSCRIPT_FONTPROPERTIESFields(a,b,c) NULL
#define setSCRIPT_FONTPROPERTIESFields(a,b,c)
#define SCRIPT_FONTPROPERTIES_sizeof() 0
#endif

#ifndef NO_SCRIPT_ITEM
void cacheSCRIPT_ITEMFields(JNIEnv *env, jobject lpObject);
SCRIPT_ITEM *getSCRIPT_ITEMFields(JNIEnv *env, jobject lpObject, SCRIPT_ITEM *lpStruct);
void setSCRIPT_ITEMFields(JNIEnv *env, jobject lpObject, SCRIPT_ITEM *lpStruct);
#define SCRIPT_ITEM_sizeof() sizeof(SCRIPT_ITEM)
#else
#define cacheSCRIPT_ITEMFields(a,b)
#define getSCRIPT_ITEMFields(a,b,c) NULL
#define setSCRIPT_ITEMFields(a,b,c)
#define SCRIPT_ITEM_sizeof() 0
#endif

#ifndef NO_SCRIPT_LOGATTR
void cacheSCRIPT_LOGATTRFields(JNIEnv *env, jobject lpObject);
SCRIPT_LOGATTR *getSCRIPT_LOGATTRFields(JNIEnv *env, jobject lpObject, SCRIPT_LOGATTR *lpStruct);
void setSCRIPT_LOGATTRFields(JNIEnv *env, jobject lpObject, SCRIPT_LOGATTR *lpStruct);
#define SCRIPT_LOGATTR_sizeof() sizeof(SCRIPT_LOGATTR)
#else
#define cacheSCRIPT_LOGATTRFields(a,b)
#define getSCRIPT_LOGATTRFields(a,b,c) NULL
#define setSCRIPT_LOGATTRFields(a,b,c)
#define SCRIPT_LOGATTR_sizeof() 0
#endif

#ifndef NO_SCRIPT_PROPERTIES
void cacheSCRIPT_PROPERTIESFields(JNIEnv *env, jobject lpObject);
SCRIPT_PROPERTIES *getSCRIPT_PROPERTIESFields(JNIEnv *env, jobject lpObject, SCRIPT_PROPERTIES *lpStruct);
void setSCRIPT_PROPERTIESFields(JNIEnv *env, jobject lpObject, SCRIPT_PROPERTIES *lpStruct);
#define SCRIPT_PROPERTIES_sizeof() sizeof(SCRIPT_PROPERTIES)
#else
#define cacheSCRIPT_PROPERTIESFields(a,b)
#define getSCRIPT_PROPERTIESFields(a,b,c) NULL
#define setSCRIPT_PROPERTIESFields(a,b,c)
#define SCRIPT_PROPERTIES_sizeof() 0
#endif

#ifndef NO_SCRIPT_STATE
void cacheSCRIPT_STATEFields(JNIEnv *env, jobject lpObject);
SCRIPT_STATE *getSCRIPT_STATEFields(JNIEnv *env, jobject lpObject, SCRIPT_STATE *lpStruct);
void setSCRIPT_STATEFields(JNIEnv *env, jobject lpObject, SCRIPT_STATE *lpStruct);
#define SCRIPT_STATE_sizeof() sizeof(SCRIPT_STATE)
#else
#define cacheSCRIPT_STATEFields(a,b)
#define getSCRIPT_STATEFields(a,b,c) NULL
#define setSCRIPT_STATEFields(a,b,c)
#define SCRIPT_STATE_sizeof() 0
#endif

#ifndef NO_SCROLLBARINFO
void cacheSCROLLBARINFOFields(JNIEnv *env, jobject lpObject);
SCROLLBARINFO *getSCROLLBARINFOFields(JNIEnv *env, jobject lpObject, SCROLLBARINFO *lpStruct);
void setSCROLLBARINFOFields(JNIEnv *env, jobject lpObject, SCROLLBARINFO *lpStruct);
#define SCROLLBARINFO_sizeof() sizeof(SCROLLBARINFO)
#else
#define cacheSCROLLBARINFOFields(a,b)
#define getSCROLLBARINFOFields(a,b,c) NULL
#define setSCROLLBARINFOFields(a,b,c)
#define SCROLLBARINFO_sizeof() 0
#endif

#ifndef NO_SCROLLINFO
void cacheSCROLLINFOFields(JNIEnv *env, jobject lpObject);
SCROLLINFO *getSCROLLINFOFields(JNIEnv *env, jobject lpObject, SCROLLINFO *lpStruct);
void setSCROLLINFOFields(JNIEnv *env, jobject lpObject, SCROLLINFO *lpStruct);
#define SCROLLINFO_sizeof() sizeof(SCROLLINFO)
#else
#define cacheSCROLLINFOFields(a,b)
#define getSCROLLINFOFields(a,b,c) NULL
#define setSCROLLINFOFields(a,b,c)
#define SCROLLINFO_sizeof() 0
#endif

#ifndef NO_SHACTIVATEINFO
void cacheSHACTIVATEINFOFields(JNIEnv *env, jobject lpObject);
SHACTIVATEINFO *getSHACTIVATEINFOFields(JNIEnv *env, jobject lpObject, SHACTIVATEINFO *lpStruct);
void setSHACTIVATEINFOFields(JNIEnv *env, jobject lpObject, SHACTIVATEINFO *lpStruct);
#define SHACTIVATEINFO_sizeof() sizeof(SHACTIVATEINFO)
#else
#define cacheSHACTIVATEINFOFields(a,b)
#define getSHACTIVATEINFOFields(a,b,c) NULL
#define setSHACTIVATEINFOFields(a,b,c)
#define SHACTIVATEINFO_sizeof() 0
#endif

#ifndef NO_SHDRAGIMAGE
void cacheSHDRAGIMAGEFields(JNIEnv *env, jobject lpObject);
SHDRAGIMAGE *getSHDRAGIMAGEFields(JNIEnv *env, jobject lpObject, SHDRAGIMAGE *lpStruct);
void setSHDRAGIMAGEFields(JNIEnv *env, jobject lpObject, SHDRAGIMAGE *lpStruct);
#define SHDRAGIMAGE_sizeof() sizeof(SHDRAGIMAGE)
#else
#define cacheSHDRAGIMAGEFields(a,b)
#define getSHDRAGIMAGEFields(a,b,c) NULL
#define setSHDRAGIMAGEFields(a,b,c)
#define SHDRAGIMAGE_sizeof() 0
#endif

#ifndef NO_SHELLEXECUTEINFO
void cacheSHELLEXECUTEINFOFields(JNIEnv *env, jobject lpObject);
SHELLEXECUTEINFO *getSHELLEXECUTEINFOFields(JNIEnv *env, jobject lpObject, SHELLEXECUTEINFO *lpStruct);
void setSHELLEXECUTEINFOFields(JNIEnv *env, jobject lpObject, SHELLEXECUTEINFO *lpStruct);
#define SHELLEXECUTEINFO_sizeof() sizeof(SHELLEXECUTEINFO)
#else
#define cacheSHELLEXECUTEINFOFields(a,b)
#define getSHELLEXECUTEINFOFields(a,b,c) NULL
#define setSHELLEXECUTEINFOFields(a,b,c)
#define SHELLEXECUTEINFO_sizeof() 0
#endif

#ifndef NO_SHFILEINFO
void cacheSHFILEINFOFields(JNIEnv *env, jobject lpObject);
SHFILEINFO *getSHFILEINFOFields(JNIEnv *env, jobject lpObject, SHFILEINFO *lpStruct);
void setSHFILEINFOFields(JNIEnv *env, jobject lpObject, SHFILEINFO *lpStruct);
#define SHFILEINFO_sizeof() sizeof(SHFILEINFO)
#else
#define cacheSHFILEINFOFields(a,b)
#define getSHFILEINFOFields(a,b,c) NULL
#define setSHFILEINFOFields(a,b,c)
#define SHFILEINFO_sizeof() 0
#endif

#ifndef NO_SHFILEINFOA
void cacheSHFILEINFOAFields(JNIEnv *env, jobject lpObject);
SHFILEINFOA *getSHFILEINFOAFields(JNIEnv *env, jobject lpObject, SHFILEINFOA *lpStruct);
void setSHFILEINFOAFields(JNIEnv *env, jobject lpObject, SHFILEINFOA *lpStruct);
#define SHFILEINFOA_sizeof() sizeof(SHFILEINFOA)
#else
#define cacheSHFILEINFOAFields(a,b)
#define getSHFILEINFOAFields(a,b,c) NULL
#define setSHFILEINFOAFields(a,b,c)
#define SHFILEINFOA_sizeof() 0
#endif

#ifndef NO_SHFILEINFOW
void cacheSHFILEINFOWFields(JNIEnv *env, jobject lpObject);
SHFILEINFOW *getSHFILEINFOWFields(JNIEnv *env, jobject lpObject, SHFILEINFOW *lpStruct);
void setSHFILEINFOWFields(JNIEnv *env, jobject lpObject, SHFILEINFOW *lpStruct);
#define SHFILEINFOW_sizeof() sizeof(SHFILEINFOW)
#else
#define cacheSHFILEINFOWFields(a,b)
#define getSHFILEINFOWFields(a,b,c) NULL
#define setSHFILEINFOWFields(a,b,c)
#define SHFILEINFOW_sizeof() 0
#endif

#ifndef NO_SHMENUBARINFO
void cacheSHMENUBARINFOFields(JNIEnv *env, jobject lpObject);
SHMENUBARINFO *getSHMENUBARINFOFields(JNIEnv *env, jobject lpObject, SHMENUBARINFO *lpStruct);
void setSHMENUBARINFOFields(JNIEnv *env, jobject lpObject, SHMENUBARINFO *lpStruct);
#define SHMENUBARINFO_sizeof() sizeof(SHMENUBARINFO)
#else
#define cacheSHMENUBARINFOFields(a,b)
#define getSHMENUBARINFOFields(a,b,c) NULL
#define setSHMENUBARINFOFields(a,b,c)
#define SHMENUBARINFO_sizeof() 0
#endif

#ifndef NO_SHRGINFO
void cacheSHRGINFOFields(JNIEnv *env, jobject lpObject);
SHRGINFO *getSHRGINFOFields(JNIEnv *env, jobject lpObject, SHRGINFO *lpStruct);
void setSHRGINFOFields(JNIEnv *env, jobject lpObject, SHRGINFO *lpStruct);
#define SHRGINFO_sizeof() sizeof(SHRGINFO)
#else
#define cacheSHRGINFOFields(a,b)
#define getSHRGINFOFields(a,b,c) NULL
#define setSHRGINFOFields(a,b,c)
#define SHRGINFO_sizeof() 0
#endif

#ifndef NO_SIPINFO
void cacheSIPINFOFields(JNIEnv *env, jobject lpObject);
SIPINFO *getSIPINFOFields(JNIEnv *env, jobject lpObject, SIPINFO *lpStruct);
void setSIPINFOFields(JNIEnv *env, jobject lpObject, SIPINFO *lpStruct);
#define SIPINFO_sizeof() sizeof(SIPINFO)
#else
#define cacheSIPINFOFields(a,b)
#define getSIPINFOFields(a,b,c) NULL
#define setSIPINFOFields(a,b,c)
#define SIPINFO_sizeof() 0
#endif

#ifndef NO_SIZE
void cacheSIZEFields(JNIEnv *env, jobject lpObject);
SIZE *getSIZEFields(JNIEnv *env, jobject lpObject, SIZE *lpStruct);
void setSIZEFields(JNIEnv *env, jobject lpObject, SIZE *lpStruct);
#define SIZE_sizeof() sizeof(SIZE)
#else
#define cacheSIZEFields(a,b)
#define getSIZEFields(a,b,c) NULL
#define setSIZEFields(a,b,c)
#define SIZE_sizeof() 0
#endif

#ifndef NO_STARTUPINFO
void cacheSTARTUPINFOFields(JNIEnv *env, jobject lpObject);
STARTUPINFO *getSTARTUPINFOFields(JNIEnv *env, jobject lpObject, STARTUPINFO *lpStruct);
void setSTARTUPINFOFields(JNIEnv *env, jobject lpObject, STARTUPINFO *lpStruct);
#define STARTUPINFO_sizeof() sizeof(STARTUPINFO)
#else
#define cacheSTARTUPINFOFields(a,b)
#define getSTARTUPINFOFields(a,b,c) NULL
#define setSTARTUPINFOFields(a,b,c)
#define STARTUPINFO_sizeof() 0
#endif

#ifndef NO_SYSTEMTIME
void cacheSYSTEMTIMEFields(JNIEnv *env, jobject lpObject);
SYSTEMTIME *getSYSTEMTIMEFields(JNIEnv *env, jobject lpObject, SYSTEMTIME *lpStruct);
void setSYSTEMTIMEFields(JNIEnv *env, jobject lpObject, SYSTEMTIME *lpStruct);
#define SYSTEMTIME_sizeof() sizeof(SYSTEMTIME)
#else
#define cacheSYSTEMTIMEFields(a,b)
#define getSYSTEMTIMEFields(a,b,c) NULL
#define setSYSTEMTIMEFields(a,b,c)
#define SYSTEMTIME_sizeof() 0
#endif

#ifndef NO_TBBUTTON
void cacheTBBUTTONFields(JNIEnv *env, jobject lpObject);
TBBUTTON *getTBBUTTONFields(JNIEnv *env, jobject lpObject, TBBUTTON *lpStruct);
void setTBBUTTONFields(JNIEnv *env, jobject lpObject, TBBUTTON *lpStruct);
#define TBBUTTON_sizeof() sizeof(TBBUTTON)
#else
#define cacheTBBUTTONFields(a,b)
#define getTBBUTTONFields(a,b,c) NULL
#define setTBBUTTONFields(a,b,c)
#define TBBUTTON_sizeof() 0
#endif

#ifndef NO_TBBUTTONINFO
void cacheTBBUTTONINFOFields(JNIEnv *env, jobject lpObject);
TBBUTTONINFO *getTBBUTTONINFOFields(JNIEnv *env, jobject lpObject, TBBUTTONINFO *lpStruct);
void setTBBUTTONINFOFields(JNIEnv *env, jobject lpObject, TBBUTTONINFO *lpStruct);
#define TBBUTTONINFO_sizeof() sizeof(TBBUTTONINFO)
#else
#define cacheTBBUTTONINFOFields(a,b)
#define getTBBUTTONINFOFields(a,b,c) NULL
#define setTBBUTTONINFOFields(a,b,c)
#define TBBUTTONINFO_sizeof() 0
#endif

#ifndef NO_TCHITTESTINFO
void cacheTCHITTESTINFOFields(JNIEnv *env, jobject lpObject);
TCHITTESTINFO *getTCHITTESTINFOFields(JNIEnv *env, jobject lpObject, TCHITTESTINFO *lpStruct);
void setTCHITTESTINFOFields(JNIEnv *env, jobject lpObject, TCHITTESTINFO *lpStruct);
#define TCHITTESTINFO_sizeof() sizeof(TCHITTESTINFO)
#else
#define cacheTCHITTESTINFOFields(a,b)
#define getTCHITTESTINFOFields(a,b,c) NULL
#define setTCHITTESTINFOFields(a,b,c)
#define TCHITTESTINFO_sizeof() 0
#endif

#ifndef NO_TCITEM
void cacheTCITEMFields(JNIEnv *env, jobject lpObject);
TCITEM *getTCITEMFields(JNIEnv *env, jobject lpObject, TCITEM *lpStruct);
void setTCITEMFields(JNIEnv *env, jobject lpObject, TCITEM *lpStruct);
#define TCITEM_sizeof() sizeof(TCITEM)
#else
#define cacheTCITEMFields(a,b)
#define getTCITEMFields(a,b,c) NULL
#define setTCITEMFields(a,b,c)
#define TCITEM_sizeof() 0
#endif

#ifndef NO_TEXTMETRIC
void cacheTEXTMETRICFields(JNIEnv *env, jobject lpObject);
TEXTMETRIC *getTEXTMETRICFields(JNIEnv *env, jobject lpObject, TEXTMETRIC *lpStruct);
void setTEXTMETRICFields(JNIEnv *env, jobject lpObject, TEXTMETRIC *lpStruct);
#define TEXTMETRIC_sizeof() sizeof(TEXTMETRIC)
#else
#define cacheTEXTMETRICFields(a,b)
#define getTEXTMETRICFields(a,b,c) NULL
#define setTEXTMETRICFields(a,b,c)
#define TEXTMETRIC_sizeof() 0
#endif

#ifndef NO_TEXTMETRICA
void cacheTEXTMETRICAFields(JNIEnv *env, jobject lpObject);
TEXTMETRICA *getTEXTMETRICAFields(JNIEnv *env, jobject lpObject, TEXTMETRICA *lpStruct);
void setTEXTMETRICAFields(JNIEnv *env, jobject lpObject, TEXTMETRICA *lpStruct);
#define TEXTMETRICA_sizeof() sizeof(TEXTMETRICA)
#else
#define cacheTEXTMETRICAFields(a,b)
#define getTEXTMETRICAFields(a,b,c) NULL
#define setTEXTMETRICAFields(a,b,c)
#define TEXTMETRICA_sizeof() 0
#endif

#ifndef NO_TEXTMETRICW
void cacheTEXTMETRICWFields(JNIEnv *env, jobject lpObject);
TEXTMETRICW *getTEXTMETRICWFields(JNIEnv *env, jobject lpObject, TEXTMETRICW *lpStruct);
void setTEXTMETRICWFields(JNIEnv *env, jobject lpObject, TEXTMETRICW *lpStruct);
#define TEXTMETRICW_sizeof() sizeof(TEXTMETRICW)
#else
#define cacheTEXTMETRICWFields(a,b)
#define getTEXTMETRICWFields(a,b,c) NULL
#define setTEXTMETRICWFields(a,b,c)
#define TEXTMETRICW_sizeof() 0
#endif

#ifndef NO_TF_DA_COLOR
void cacheTF_DA_COLORFields(JNIEnv *env, jobject lpObject);
TF_DA_COLOR *getTF_DA_COLORFields(JNIEnv *env, jobject lpObject, TF_DA_COLOR *lpStruct);
void setTF_DA_COLORFields(JNIEnv *env, jobject lpObject, TF_DA_COLOR *lpStruct);
#define TF_DA_COLOR_sizeof() sizeof(TF_DA_COLOR)
#else
#define cacheTF_DA_COLORFields(a,b)
#define getTF_DA_COLORFields(a,b,c) NULL
#define setTF_DA_COLORFields(a,b,c)
#define TF_DA_COLOR_sizeof() 0
#endif

#ifndef NO_TF_DISPLAYATTRIBUTE
void cacheTF_DISPLAYATTRIBUTEFields(JNIEnv *env, jobject lpObject);
TF_DISPLAYATTRIBUTE *getTF_DISPLAYATTRIBUTEFields(JNIEnv *env, jobject lpObject, TF_DISPLAYATTRIBUTE *lpStruct);
void setTF_DISPLAYATTRIBUTEFields(JNIEnv *env, jobject lpObject, TF_DISPLAYATTRIBUTE *lpStruct);
#define TF_DISPLAYATTRIBUTE_sizeof() sizeof(TF_DISPLAYATTRIBUTE)
#else
#define cacheTF_DISPLAYATTRIBUTEFields(a,b)
#define getTF_DISPLAYATTRIBUTEFields(a,b,c) NULL
#define setTF_DISPLAYATTRIBUTEFields(a,b,c)
#define TF_DISPLAYATTRIBUTE_sizeof() 0
#endif

#ifndef NO_TOOLINFO
void cacheTOOLINFOFields(JNIEnv *env, jobject lpObject);
TOOLINFO *getTOOLINFOFields(JNIEnv *env, jobject lpObject, TOOLINFO *lpStruct);
void setTOOLINFOFields(JNIEnv *env, jobject lpObject, TOOLINFO *lpStruct);
#define TOOLINFO_sizeof() sizeof(TOOLINFO)
#else
#define cacheTOOLINFOFields(a,b)
#define getTOOLINFOFields(a,b,c) NULL
#define setTOOLINFOFields(a,b,c)
#define TOOLINFO_sizeof() 0
#endif

#ifndef NO_TOUCHINPUT
void cacheTOUCHINPUTFields(JNIEnv *env, jobject lpObject);
TOUCHINPUT *getTOUCHINPUTFields(JNIEnv *env, jobject lpObject, TOUCHINPUT *lpStruct);
void setTOUCHINPUTFields(JNIEnv *env, jobject lpObject, TOUCHINPUT *lpStruct);
#define TOUCHINPUT_sizeof() sizeof(TOUCHINPUT)
#else
#define cacheTOUCHINPUTFields(a,b)
#define getTOUCHINPUTFields(a,b,c) NULL
#define setTOUCHINPUTFields(a,b,c)
#define TOUCHINPUT_sizeof() 0
#endif

#ifndef NO_TRACKMOUSEEVENT
void cacheTRACKMOUSEEVENTFields(JNIEnv *env, jobject lpObject);
TRACKMOUSEEVENT *getTRACKMOUSEEVENTFields(JNIEnv *env, jobject lpObject, TRACKMOUSEEVENT *lpStruct);
void setTRACKMOUSEEVENTFields(JNIEnv *env, jobject lpObject, TRACKMOUSEEVENT *lpStruct);
#define TRACKMOUSEEVENT_sizeof() sizeof(TRACKMOUSEEVENT)
#else
#define cacheTRACKMOUSEEVENTFields(a,b)
#define getTRACKMOUSEEVENTFields(a,b,c) NULL
#define setTRACKMOUSEEVENTFields(a,b,c)
#define TRACKMOUSEEVENT_sizeof() 0
#endif

#ifndef NO_TRIVERTEX
void cacheTRIVERTEXFields(JNIEnv *env, jobject lpObject);
TRIVERTEX *getTRIVERTEXFields(JNIEnv *env, jobject lpObject, TRIVERTEX *lpStruct);
void setTRIVERTEXFields(JNIEnv *env, jobject lpObject, TRIVERTEX *lpStruct);
#define TRIVERTEX_sizeof() sizeof(TRIVERTEX)
#else
#define cacheTRIVERTEXFields(a,b)
#define getTRIVERTEXFields(a,b,c) NULL
#define setTRIVERTEXFields(a,b,c)
#define TRIVERTEX_sizeof() 0
#endif

#ifndef NO_TVHITTESTINFO
void cacheTVHITTESTINFOFields(JNIEnv *env, jobject lpObject);
TVHITTESTINFO *getTVHITTESTINFOFields(JNIEnv *env, jobject lpObject, TVHITTESTINFO *lpStruct);
void setTVHITTESTINFOFields(JNIEnv *env, jobject lpObject, TVHITTESTINFO *lpStruct);
#define TVHITTESTINFO_sizeof() sizeof(TVHITTESTINFO)
#else
#define cacheTVHITTESTINFOFields(a,b)
#define getTVHITTESTINFOFields(a,b,c) NULL
#define setTVHITTESTINFOFields(a,b,c)
#define TVHITTESTINFO_sizeof() 0
#endif

#ifndef NO_TVINSERTSTRUCT
void cacheTVINSERTSTRUCTFields(JNIEnv *env, jobject lpObject);
TVINSERTSTRUCT *getTVINSERTSTRUCTFields(JNIEnv *env, jobject lpObject, TVINSERTSTRUCT *lpStruct);
void setTVINSERTSTRUCTFields(JNIEnv *env, jobject lpObject, TVINSERTSTRUCT *lpStruct);
#define TVINSERTSTRUCT_sizeof() sizeof(TVINSERTSTRUCT)
#else
#define cacheTVINSERTSTRUCTFields(a,b)
#define getTVINSERTSTRUCTFields(a,b,c) NULL
#define setTVINSERTSTRUCTFields(a,b,c)
#define TVINSERTSTRUCT_sizeof() 0
#endif

#ifndef NO_TVITEM
void cacheTVITEMFields(JNIEnv *env, jobject lpObject);
TVITEM *getTVITEMFields(JNIEnv *env, jobject lpObject, TVITEM *lpStruct);
void setTVITEMFields(JNIEnv *env, jobject lpObject, TVITEM *lpStruct);
#define TVITEM_sizeof() sizeof(TVITEM)
#else
#define cacheTVITEMFields(a,b)
#define getTVITEMFields(a,b,c) NULL
#define setTVITEMFields(a,b,c)
#define TVITEM_sizeof() 0
#endif

#ifndef NO_TVITEMEX
void cacheTVITEMEXFields(JNIEnv *env, jobject lpObject);
TVITEMEX *getTVITEMEXFields(JNIEnv *env, jobject lpObject, TVITEMEX *lpStruct);
void setTVITEMEXFields(JNIEnv *env, jobject lpObject, TVITEMEX *lpStruct);
#define TVITEMEX_sizeof() sizeof(TVITEMEX)
#else
#define cacheTVITEMEXFields(a,b)
#define getTVITEMEXFields(a,b,c) NULL
#define setTVITEMEXFields(a,b,c)
#define TVITEMEX_sizeof() 0
#endif

#ifndef NO_TVSORTCB
void cacheTVSORTCBFields(JNIEnv *env, jobject lpObject);
TVSORTCB *getTVSORTCBFields(JNIEnv *env, jobject lpObject, TVSORTCB *lpStruct);
void setTVSORTCBFields(JNIEnv *env, jobject lpObject, TVSORTCB *lpStruct);
#define TVSORTCB_sizeof() sizeof(TVSORTCB)
#else
#define cacheTVSORTCBFields(a,b)
#define getTVSORTCBFields(a,b,c) NULL
#define setTVSORTCBFields(a,b,c)
#define TVSORTCB_sizeof() 0
#endif

#ifndef NO_UDACCEL
void cacheUDACCELFields(JNIEnv *env, jobject lpObject);
UDACCEL *getUDACCELFields(JNIEnv *env, jobject lpObject, UDACCEL *lpStruct);
void setUDACCELFields(JNIEnv *env, jobject lpObject, UDACCEL *lpStruct);
#define UDACCEL_sizeof() sizeof(UDACCEL)
#else
#define cacheUDACCELFields(a,b)
#define getUDACCELFields(a,b,c) NULL
#define setUDACCELFields(a,b,c)
#define UDACCEL_sizeof() 0
#endif

#ifndef NO_WINDOWPLACEMENT
void cacheWINDOWPLACEMENTFields(JNIEnv *env, jobject lpObject);
WINDOWPLACEMENT *getWINDOWPLACEMENTFields(JNIEnv *env, jobject lpObject, WINDOWPLACEMENT *lpStruct);
void setWINDOWPLACEMENTFields(JNIEnv *env, jobject lpObject, WINDOWPLACEMENT *lpStruct);
#define WINDOWPLACEMENT_sizeof() sizeof(WINDOWPLACEMENT)
#else
#define cacheWINDOWPLACEMENTFields(a,b)
#define getWINDOWPLACEMENTFields(a,b,c) NULL
#define setWINDOWPLACEMENTFields(a,b,c)
#define WINDOWPLACEMENT_sizeof() 0
#endif

#ifndef NO_WINDOWPOS
void cacheWINDOWPOSFields(JNIEnv *env, jobject lpObject);
WINDOWPOS *getWINDOWPOSFields(JNIEnv *env, jobject lpObject, WINDOWPOS *lpStruct);
void setWINDOWPOSFields(JNIEnv *env, jobject lpObject, WINDOWPOS *lpStruct);
#define WINDOWPOS_sizeof() sizeof(WINDOWPOS)
#else
#define cacheWINDOWPOSFields(a,b)
#define getWINDOWPOSFields(a,b,c) NULL
#define setWINDOWPOSFields(a,b,c)
#define WINDOWPOS_sizeof() 0
#endif

#ifndef NO_WNDCLASS
void cacheWNDCLASSFields(JNIEnv *env, jobject lpObject);
WNDCLASS *getWNDCLASSFields(JNIEnv *env, jobject lpObject, WNDCLASS *lpStruct);
void setWNDCLASSFields(JNIEnv *env, jobject lpObject, WNDCLASS *lpStruct);
#define WNDCLASS_sizeof() sizeof(WNDCLASS)
#else
#define cacheWNDCLASSFields(a,b)
#define getWNDCLASSFields(a,b,c) NULL
#define setWNDCLASSFields(a,b,c)
#define WNDCLASS_sizeof() 0
#endif

