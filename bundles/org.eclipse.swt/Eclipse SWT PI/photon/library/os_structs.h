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

#ifndef NO_FontDetails
FontDetails *getFontDetailsFields(JNIEnv *env, jobject lpObject, FontDetails *lpStruct);
void setFontDetailsFields(JNIEnv *env, jobject lpObject, FontDetails *lpStruct);
#else
#define getFontDetailsFields(a,b,c) NULL
#define setFontDetailsFields(a,b,c)
#endif

#ifndef NO_FontQueryInfo
FontQueryInfo *getFontQueryInfoFields(JNIEnv *env, jobject lpObject, FontQueryInfo *lpStruct);
void setFontQueryInfoFields(JNIEnv *env, jobject lpObject, FontQueryInfo *lpStruct);
#else
#define getFontQueryInfoFields(a,b,c) NULL
#define setFontQueryInfoFields(a,b,c)
#endif

#ifndef NO_PgAlpha_t
PgAlpha_t *getPgAlpha_tFields(JNIEnv *env, jobject lpObject, PgAlpha_t *lpStruct);
void setPgAlpha_tFields(JNIEnv *env, jobject lpObject, PgAlpha_t *lpStruct);
#else
#define getPgAlpha_tFields(a,b,c) NULL
#define setPgAlpha_tFields(a,b,c)
#endif

#ifndef NO_PgDisplaySettings_t
PgDisplaySettings_t *getPgDisplaySettings_tFields(JNIEnv *env, jobject lpObject, PgDisplaySettings_t *lpStruct);
void setPgDisplaySettings_tFields(JNIEnv *env, jobject lpObject, PgDisplaySettings_t *lpStruct);
#else
#define getPgDisplaySettings_tFields(a,b,c) NULL
#define setPgDisplaySettings_tFields(a,b,c)
#endif

#ifndef NO_PgMap_t
PgMap_t *getPgMap_tFields(JNIEnv *env, jobject lpObject, PgMap_t *lpStruct);
void setPgMap_tFields(JNIEnv *env, jobject lpObject, PgMap_t *lpStruct);
#else
#define getPgMap_tFields(a,b,c) NULL
#define setPgMap_tFields(a,b,c)
#endif

#ifndef NO_PgVideoModeInfo_t
PgVideoModeInfo_t *getPgVideoModeInfo_tFields(JNIEnv *env, jobject lpObject, PgVideoModeInfo_t *lpStruct);
void setPgVideoModeInfo_tFields(JNIEnv *env, jobject lpObject, PgVideoModeInfo_t *lpStruct);
#else
#define getPgVideoModeInfo_tFields(a,b,c) NULL
#define setPgVideoModeInfo_tFields(a,b,c)
#endif

#ifndef NO_PhArea_t
PhArea_t *getPhArea_tFields(JNIEnv *env, jobject lpObject, PhArea_t *lpStruct);
void setPhArea_tFields(JNIEnv *env, jobject lpObject, PhArea_t *lpStruct);
#else
#define getPhArea_tFields(a,b,c) NULL
#define setPhArea_tFields(a,b,c)
#endif

#ifndef NO_PhClipHeader
PhClipHeader *getPhClipHeaderFields(JNIEnv *env, jobject lpObject, PhClipHeader *lpStruct);
void setPhClipHeaderFields(JNIEnv *env, jobject lpObject, PhClipHeader *lpStruct);
#else
#define getPhClipHeaderFields(a,b,c) NULL
#define setPhClipHeaderFields(a,b,c)
#endif

#ifndef NO_PhCursorDef_t
PhCursorDef_t *getPhCursorDef_tFields(JNIEnv *env, jobject lpObject, PhCursorDef_t *lpStruct);
void setPhCursorDef_tFields(JNIEnv *env, jobject lpObject, PhCursorDef_t *lpStruct);
#else
#define getPhCursorDef_tFields(a,b,c) NULL
#define setPhCursorDef_tFields(a,b,c)
#endif

#ifndef NO_PhCursorInfo_t
PhCursorInfo_t *getPhCursorInfo_tFields(JNIEnv *env, jobject lpObject, PhCursorInfo_t *lpStruct);
void setPhCursorInfo_tFields(JNIEnv *env, jobject lpObject, PhCursorInfo_t *lpStruct);
#else
#define getPhCursorInfo_tFields(a,b,c) NULL
#define setPhCursorInfo_tFields(a,b,c)
#endif

#ifndef NO_PhDim_t
PhDim_t *getPhDim_tFields(JNIEnv *env, jobject lpObject, PhDim_t *lpStruct);
void setPhDim_tFields(JNIEnv *env, jobject lpObject, PhDim_t *lpStruct);
#else
#define getPhDim_tFields(a,b,c) NULL
#define setPhDim_tFields(a,b,c)
#endif

#ifndef NO_PhEvent_t
PhEvent_t *getPhEvent_tFields(JNIEnv *env, jobject lpObject, PhEvent_t *lpStruct);
void setPhEvent_tFields(JNIEnv *env, jobject lpObject, PhEvent_t *lpStruct);
#else
#define getPhEvent_tFields(a,b,c) NULL
#define setPhEvent_tFields(a,b,c)
#endif

#ifndef NO_PhImage_t
PhImage_t *getPhImage_tFields(JNIEnv *env, jobject lpObject, PhImage_t *lpStruct);
void setPhImage_tFields(JNIEnv *env, jobject lpObject, PhImage_t *lpStruct);
#else
#define getPhImage_tFields(a,b,c) NULL
#define setPhImage_tFields(a,b,c)
#endif

#ifndef NO_PhKeyEvent_t
PhKeyEvent_t *getPhKeyEvent_tFields(JNIEnv *env, jobject lpObject, PhKeyEvent_t *lpStruct);
void setPhKeyEvent_tFields(JNIEnv *env, jobject lpObject, PhKeyEvent_t *lpStruct);
#else
#define getPhKeyEvent_tFields(a,b,c) NULL
#define setPhKeyEvent_tFields(a,b,c)
#endif

#ifndef NO_PhPoint_t
PhPoint_t *getPhPoint_tFields(JNIEnv *env, jobject lpObject, PhPoint_t *lpStruct);
void setPhPoint_tFields(JNIEnv *env, jobject lpObject, PhPoint_t *lpStruct);
#else
#define getPhPoint_tFields(a,b,c) NULL
#define setPhPoint_tFields(a,b,c)
#endif

#ifndef NO_PhPointerEvent_t
PhPointerEvent_t *getPhPointerEvent_tFields(JNIEnv *env, jobject lpObject, PhPointerEvent_t *lpStruct);
void setPhPointerEvent_tFields(JNIEnv *env, jobject lpObject, PhPointerEvent_t *lpStruct);
#else
#define getPhPointerEvent_tFields(a,b,c) NULL
#define setPhPointerEvent_tFields(a,b,c)
#endif

#ifndef NO_PhRect_t
PhRect_t *getPhRect_tFields(JNIEnv *env, jobject lpObject, PhRect_t *lpStruct);
void setPhRect_tFields(JNIEnv *env, jobject lpObject, PhRect_t *lpStruct);
#else
#define getPhRect_tFields(a,b,c) NULL
#define setPhRect_tFields(a,b,c)
#endif

#ifndef NO_PhRegion_t
PhRegion_t *getPhRegion_tFields(JNIEnv *env, jobject lpObject, PhRegion_t *lpStruct);
void setPhRegion_tFields(JNIEnv *env, jobject lpObject, PhRegion_t *lpStruct);
#else
#define getPhRegion_tFields(a,b,c) NULL
#define setPhRegion_tFields(a,b,c)
#endif

#ifndef NO_PhTile_t
PhTile_t *getPhTile_tFields(JNIEnv *env, jobject lpObject, PhTile_t *lpStruct);
void setPhTile_tFields(JNIEnv *env, jobject lpObject, PhTile_t *lpStruct);
#else
#define getPhTile_tFields(a,b,c) NULL
#define setPhTile_tFields(a,b,c)
#endif

#ifndef NO_PhWindowEvent_t
PhWindowEvent_t *getPhWindowEvent_tFields(JNIEnv *env, jobject lpObject, PhWindowEvent_t *lpStruct);
void setPhWindowEvent_tFields(JNIEnv *env, jobject lpObject, PhWindowEvent_t *lpStruct);
#else
#define getPhWindowEvent_tFields(a,b,c) NULL
#define setPhWindowEvent_tFields(a,b,c)
#endif

#ifndef NO_PtCallbackInfo_t
PtCallbackInfo_t *getPtCallbackInfo_tFields(JNIEnv *env, jobject lpObject, PtCallbackInfo_t *lpStruct);
void setPtCallbackInfo_tFields(JNIEnv *env, jobject lpObject, PtCallbackInfo_t *lpStruct);
#else
#define getPtCallbackInfo_tFields(a,b,c) NULL
#define setPtCallbackInfo_tFields(a,b,c)
#endif

#ifndef NO_PtColorSelectInfo_t
PtColorSelectInfo_t *getPtColorSelectInfo_tFields(JNIEnv *env, jobject lpObject, PtColorSelectInfo_t *lpStruct);
void setPtColorSelectInfo_tFields(JNIEnv *env, jobject lpObject, PtColorSelectInfo_t *lpStruct);
#else
#define getPtColorSelectInfo_tFields(a,b,c) NULL
#define setPtColorSelectInfo_tFields(a,b,c)
#endif

#ifndef NO_PtFileSelectionInfo_t
PtFileSelectionInfo_t *getPtFileSelectionInfo_tFields(JNIEnv *env, jobject lpObject, PtFileSelectionInfo_t *lpStruct);
void setPtFileSelectionInfo_tFields(JNIEnv *env, jobject lpObject, PtFileSelectionInfo_t *lpStruct);
#else
#define getPtFileSelectionInfo_tFields(a,b,c) NULL
#define setPtFileSelectionInfo_tFields(a,b,c)
#endif

#ifndef NO_PtScrollbarCallback_t
PtScrollbarCallback_t *getPtScrollbarCallback_tFields(JNIEnv *env, jobject lpObject, PtScrollbarCallback_t *lpStruct);
void setPtScrollbarCallback_tFields(JNIEnv *env, jobject lpObject, PtScrollbarCallback_t *lpStruct);
#else
#define getPtScrollbarCallback_tFields(a,b,c) NULL
#define setPtScrollbarCallback_tFields(a,b,c)
#endif

#ifndef NO_PtTextCallback_t
PtTextCallback_t *getPtTextCallback_tFields(JNIEnv *env, jobject lpObject, PtTextCallback_t *lpStruct);
void setPtTextCallback_tFields(JNIEnv *env, jobject lpObject, PtTextCallback_t *lpStruct);
#else
#define getPtTextCallback_tFields(a,b,c) NULL
#define setPtTextCallback_tFields(a,b,c)
#endif

#ifndef NO_PtWebClientData_t
PtWebClientData_t *getPtWebClientData_tFields(JNIEnv *env, jobject lpObject, PtWebClientData_t *lpStruct);
void setPtWebClientData_tFields(JNIEnv *env, jobject lpObject, PtWebClientData_t *lpStruct);
#else
#define getPtWebClientData_tFields(a,b,c) NULL
#define setPtWebClientData_tFields(a,b,c)
#endif

#ifndef NO_PtWebDataReqCallback_t
PtWebDataReqCallback_t *getPtWebDataReqCallback_tFields(JNIEnv *env, jobject lpObject, PtWebDataReqCallback_t *lpStruct);
void setPtWebDataReqCallback_tFields(JNIEnv *env, jobject lpObject, PtWebDataReqCallback_t *lpStruct);
#else
#define getPtWebDataReqCallback_tFields(a,b,c) NULL
#define setPtWebDataReqCallback_tFields(a,b,c)
#endif

#ifndef NO_PtWebStatusCallback_t
PtWebStatusCallback_t *getPtWebStatusCallback_tFields(JNIEnv *env, jobject lpObject, PtWebStatusCallback_t *lpStruct);
void setPtWebStatusCallback_tFields(JNIEnv *env, jobject lpObject, PtWebStatusCallback_t *lpStruct);
#else
#define getPtWebStatusCallback_tFields(a,b,c) NULL
#define setPtWebStatusCallback_tFields(a,b,c)
#endif

#ifndef NO_utsname
utsname *getutsnameFields(JNIEnv *env, jobject lpObject, utsname *lpStruct);
void setutsnameFields(JNIEnv *env, jobject lpObject, utsname *lpStruct);
#else
#define getutsnameFields(a,b,c) NULL
#define setutsnameFields(a,b,c)
#endif

