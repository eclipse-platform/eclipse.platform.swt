/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

#include "os.h"

#ifndef NO_FontDetails
void cacheFontDetailsFields(JNIEnv *env, jobject lpObject);
FontDetails *getFontDetailsFields(JNIEnv *env, jobject lpObject, FontDetails *lpStruct);
void setFontDetailsFields(JNIEnv *env, jobject lpObject, FontDetails *lpStruct);
#define FontDetails_sizeof() sizeof(FontDetails)
#else
#define cacheFontDetailsFields(a,b)
#define getFontDetailsFields(a,b,c) NULL
#define setFontDetailsFields(a,b,c)
#define FontDetails_sizeof() 0
#endif

#ifndef NO_FontQueryInfo
void cacheFontQueryInfoFields(JNIEnv *env, jobject lpObject);
FontQueryInfo *getFontQueryInfoFields(JNIEnv *env, jobject lpObject, FontQueryInfo *lpStruct);
void setFontQueryInfoFields(JNIEnv *env, jobject lpObject, FontQueryInfo *lpStruct);
#define FontQueryInfo_sizeof() sizeof(FontQueryInfo)
#else
#define cacheFontQueryInfoFields(a,b)
#define getFontQueryInfoFields(a,b,c) NULL
#define setFontQueryInfoFields(a,b,c)
#define FontQueryInfo_sizeof() 0
#endif

#ifndef NO_PgAlpha_t
void cachePgAlpha_tFields(JNIEnv *env, jobject lpObject);
PgAlpha_t *getPgAlpha_tFields(JNIEnv *env, jobject lpObject, PgAlpha_t *lpStruct);
void setPgAlpha_tFields(JNIEnv *env, jobject lpObject, PgAlpha_t *lpStruct);
#define PgAlpha_t_sizeof() sizeof(PgAlpha_t)
#else
#define cachePgAlpha_tFields(a,b)
#define getPgAlpha_tFields(a,b,c) NULL
#define setPgAlpha_tFields(a,b,c)
#define PgAlpha_t_sizeof() 0
#endif

#ifndef NO_PgDisplaySettings_t
void cachePgDisplaySettings_tFields(JNIEnv *env, jobject lpObject);
PgDisplaySettings_t *getPgDisplaySettings_tFields(JNIEnv *env, jobject lpObject, PgDisplaySettings_t *lpStruct);
void setPgDisplaySettings_tFields(JNIEnv *env, jobject lpObject, PgDisplaySettings_t *lpStruct);
#define PgDisplaySettings_t_sizeof() sizeof(PgDisplaySettings_t)
#else
#define cachePgDisplaySettings_tFields(a,b)
#define getPgDisplaySettings_tFields(a,b,c) NULL
#define setPgDisplaySettings_tFields(a,b,c)
#define PgDisplaySettings_t_sizeof() 0
#endif

#ifndef NO_PgMap_t
void cachePgMap_tFields(JNIEnv *env, jobject lpObject);
PgMap_t *getPgMap_tFields(JNIEnv *env, jobject lpObject, PgMap_t *lpStruct);
void setPgMap_tFields(JNIEnv *env, jobject lpObject, PgMap_t *lpStruct);
#define PgMap_t_sizeof() sizeof(PgMap_t)
#else
#define cachePgMap_tFields(a,b)
#define getPgMap_tFields(a,b,c) NULL
#define setPgMap_tFields(a,b,c)
#define PgMap_t_sizeof() 0
#endif

#ifndef NO_PgVideoModeInfo_t
void cachePgVideoModeInfo_tFields(JNIEnv *env, jobject lpObject);
PgVideoModeInfo_t *getPgVideoModeInfo_tFields(JNIEnv *env, jobject lpObject, PgVideoModeInfo_t *lpStruct);
void setPgVideoModeInfo_tFields(JNIEnv *env, jobject lpObject, PgVideoModeInfo_t *lpStruct);
#define PgVideoModeInfo_t_sizeof() sizeof(PgVideoModeInfo_t)
#else
#define cachePgVideoModeInfo_tFields(a,b)
#define getPgVideoModeInfo_tFields(a,b,c) NULL
#define setPgVideoModeInfo_tFields(a,b,c)
#define PgVideoModeInfo_t_sizeof() 0
#endif

#ifndef NO_PhArea_t
void cachePhArea_tFields(JNIEnv *env, jobject lpObject);
PhArea_t *getPhArea_tFields(JNIEnv *env, jobject lpObject, PhArea_t *lpStruct);
void setPhArea_tFields(JNIEnv *env, jobject lpObject, PhArea_t *lpStruct);
#define PhArea_t_sizeof() sizeof(PhArea_t)
#else
#define cachePhArea_tFields(a,b)
#define getPhArea_tFields(a,b,c) NULL
#define setPhArea_tFields(a,b,c)
#define PhArea_t_sizeof() 0
#endif

#ifndef NO_PhClipHeader
void cachePhClipHeaderFields(JNIEnv *env, jobject lpObject);
PhClipHeader *getPhClipHeaderFields(JNIEnv *env, jobject lpObject, PhClipHeader *lpStruct);
void setPhClipHeaderFields(JNIEnv *env, jobject lpObject, PhClipHeader *lpStruct);
#define PhClipHeader_sizeof() sizeof(PhClipHeader)
#else
#define cachePhClipHeaderFields(a,b)
#define getPhClipHeaderFields(a,b,c) NULL
#define setPhClipHeaderFields(a,b,c)
#define PhClipHeader_sizeof() 0
#endif

#ifndef NO_PhCursorDef_t
void cachePhCursorDef_tFields(JNIEnv *env, jobject lpObject);
PhCursorDef_t *getPhCursorDef_tFields(JNIEnv *env, jobject lpObject, PhCursorDef_t *lpStruct);
void setPhCursorDef_tFields(JNIEnv *env, jobject lpObject, PhCursorDef_t *lpStruct);
#define PhCursorDef_t_sizeof() sizeof(PhCursorDef_t)
#else
#define cachePhCursorDef_tFields(a,b)
#define getPhCursorDef_tFields(a,b,c) NULL
#define setPhCursorDef_tFields(a,b,c)
#define PhCursorDef_t_sizeof() 0
#endif

#ifndef NO_PhCursorInfo_t
void cachePhCursorInfo_tFields(JNIEnv *env, jobject lpObject);
PhCursorInfo_t *getPhCursorInfo_tFields(JNIEnv *env, jobject lpObject, PhCursorInfo_t *lpStruct);
void setPhCursorInfo_tFields(JNIEnv *env, jobject lpObject, PhCursorInfo_t *lpStruct);
#define PhCursorInfo_t_sizeof() sizeof(PhCursorInfo_t)
#else
#define cachePhCursorInfo_tFields(a,b)
#define getPhCursorInfo_tFields(a,b,c) NULL
#define setPhCursorInfo_tFields(a,b,c)
#define PhCursorInfo_t_sizeof() 0
#endif

#ifndef NO_PhDim_t
void cachePhDim_tFields(JNIEnv *env, jobject lpObject);
PhDim_t *getPhDim_tFields(JNIEnv *env, jobject lpObject, PhDim_t *lpStruct);
void setPhDim_tFields(JNIEnv *env, jobject lpObject, PhDim_t *lpStruct);
#define PhDim_t_sizeof() sizeof(PhDim_t)
#else
#define cachePhDim_tFields(a,b)
#define getPhDim_tFields(a,b,c) NULL
#define setPhDim_tFields(a,b,c)
#define PhDim_t_sizeof() 0
#endif

#ifndef NO_PhEvent_t
void cachePhEvent_tFields(JNIEnv *env, jobject lpObject);
PhEvent_t *getPhEvent_tFields(JNIEnv *env, jobject lpObject, PhEvent_t *lpStruct);
void setPhEvent_tFields(JNIEnv *env, jobject lpObject, PhEvent_t *lpStruct);
#define PhEvent_t_sizeof() sizeof(PhEvent_t)
#else
#define cachePhEvent_tFields(a,b)
#define getPhEvent_tFields(a,b,c) NULL
#define setPhEvent_tFields(a,b,c)
#define PhEvent_t_sizeof() 0
#endif

#ifndef NO_PhImage_t
void cachePhImage_tFields(JNIEnv *env, jobject lpObject);
PhImage_t *getPhImage_tFields(JNIEnv *env, jobject lpObject, PhImage_t *lpStruct);
void setPhImage_tFields(JNIEnv *env, jobject lpObject, PhImage_t *lpStruct);
#define PhImage_t_sizeof() sizeof(PhImage_t)
#else
#define cachePhImage_tFields(a,b)
#define getPhImage_tFields(a,b,c) NULL
#define setPhImage_tFields(a,b,c)
#define PhImage_t_sizeof() 0
#endif

#ifndef NO_PhKeyEvent_t
void cachePhKeyEvent_tFields(JNIEnv *env, jobject lpObject);
PhKeyEvent_t *getPhKeyEvent_tFields(JNIEnv *env, jobject lpObject, PhKeyEvent_t *lpStruct);
void setPhKeyEvent_tFields(JNIEnv *env, jobject lpObject, PhKeyEvent_t *lpStruct);
#define PhKeyEvent_t_sizeof() sizeof(PhKeyEvent_t)
#else
#define cachePhKeyEvent_tFields(a,b)
#define getPhKeyEvent_tFields(a,b,c) NULL
#define setPhKeyEvent_tFields(a,b,c)
#define PhKeyEvent_t_sizeof() 0
#endif

#ifndef NO_PhPoint_t
void cachePhPoint_tFields(JNIEnv *env, jobject lpObject);
PhPoint_t *getPhPoint_tFields(JNIEnv *env, jobject lpObject, PhPoint_t *lpStruct);
void setPhPoint_tFields(JNIEnv *env, jobject lpObject, PhPoint_t *lpStruct);
#define PhPoint_t_sizeof() sizeof(PhPoint_t)
#else
#define cachePhPoint_tFields(a,b)
#define getPhPoint_tFields(a,b,c) NULL
#define setPhPoint_tFields(a,b,c)
#define PhPoint_t_sizeof() 0
#endif

#ifndef NO_PhPointerEvent_t
void cachePhPointerEvent_tFields(JNIEnv *env, jobject lpObject);
PhPointerEvent_t *getPhPointerEvent_tFields(JNIEnv *env, jobject lpObject, PhPointerEvent_t *lpStruct);
void setPhPointerEvent_tFields(JNIEnv *env, jobject lpObject, PhPointerEvent_t *lpStruct);
#define PhPointerEvent_t_sizeof() sizeof(PhPointerEvent_t)
#else
#define cachePhPointerEvent_tFields(a,b)
#define getPhPointerEvent_tFields(a,b,c) NULL
#define setPhPointerEvent_tFields(a,b,c)
#define PhPointerEvent_t_sizeof() 0
#endif

#ifndef NO_PhRect_t
void cachePhRect_tFields(JNIEnv *env, jobject lpObject);
PhRect_t *getPhRect_tFields(JNIEnv *env, jobject lpObject, PhRect_t *lpStruct);
void setPhRect_tFields(JNIEnv *env, jobject lpObject, PhRect_t *lpStruct);
#define PhRect_t_sizeof() sizeof(PhRect_t)
#else
#define cachePhRect_tFields(a,b)
#define getPhRect_tFields(a,b,c) NULL
#define setPhRect_tFields(a,b,c)
#define PhRect_t_sizeof() 0
#endif

#ifndef NO_PhRegion_t
void cachePhRegion_tFields(JNIEnv *env, jobject lpObject);
PhRegion_t *getPhRegion_tFields(JNIEnv *env, jobject lpObject, PhRegion_t *lpStruct);
void setPhRegion_tFields(JNIEnv *env, jobject lpObject, PhRegion_t *lpStruct);
#define PhRegion_t_sizeof() sizeof(PhRegion_t)
#else
#define cachePhRegion_tFields(a,b)
#define getPhRegion_tFields(a,b,c) NULL
#define setPhRegion_tFields(a,b,c)
#define PhRegion_t_sizeof() 0
#endif

#ifndef NO_PhTile_t
void cachePhTile_tFields(JNIEnv *env, jobject lpObject);
PhTile_t *getPhTile_tFields(JNIEnv *env, jobject lpObject, PhTile_t *lpStruct);
void setPhTile_tFields(JNIEnv *env, jobject lpObject, PhTile_t *lpStruct);
#define PhTile_t_sizeof() sizeof(PhTile_t)
#else
#define cachePhTile_tFields(a,b)
#define getPhTile_tFields(a,b,c) NULL
#define setPhTile_tFields(a,b,c)
#define PhTile_t_sizeof() 0
#endif

#ifndef NO_PhWindowEvent_t
void cachePhWindowEvent_tFields(JNIEnv *env, jobject lpObject);
PhWindowEvent_t *getPhWindowEvent_tFields(JNIEnv *env, jobject lpObject, PhWindowEvent_t *lpStruct);
void setPhWindowEvent_tFields(JNIEnv *env, jobject lpObject, PhWindowEvent_t *lpStruct);
#define PhWindowEvent_t_sizeof() sizeof(PhWindowEvent_t)
#else
#define cachePhWindowEvent_tFields(a,b)
#define getPhWindowEvent_tFields(a,b,c) NULL
#define setPhWindowEvent_tFields(a,b,c)
#define PhWindowEvent_t_sizeof() 0
#endif

#ifndef NO_PtCallbackInfo_t
void cachePtCallbackInfo_tFields(JNIEnv *env, jobject lpObject);
PtCallbackInfo_t *getPtCallbackInfo_tFields(JNIEnv *env, jobject lpObject, PtCallbackInfo_t *lpStruct);
void setPtCallbackInfo_tFields(JNIEnv *env, jobject lpObject, PtCallbackInfo_t *lpStruct);
#define PtCallbackInfo_t_sizeof() sizeof(PtCallbackInfo_t)
#else
#define cachePtCallbackInfo_tFields(a,b)
#define getPtCallbackInfo_tFields(a,b,c) NULL
#define setPtCallbackInfo_tFields(a,b,c)
#define PtCallbackInfo_t_sizeof() 0
#endif

#ifndef NO_PtColorSelectInfo_t
void cachePtColorSelectInfo_tFields(JNIEnv *env, jobject lpObject);
PtColorSelectInfo_t *getPtColorSelectInfo_tFields(JNIEnv *env, jobject lpObject, PtColorSelectInfo_t *lpStruct);
void setPtColorSelectInfo_tFields(JNIEnv *env, jobject lpObject, PtColorSelectInfo_t *lpStruct);
#define PtColorSelectInfo_t_sizeof() sizeof(PtColorSelectInfo_t)
#else
#define cachePtColorSelectInfo_tFields(a,b)
#define getPtColorSelectInfo_tFields(a,b,c) NULL
#define setPtColorSelectInfo_tFields(a,b,c)
#define PtColorSelectInfo_t_sizeof() 0
#endif

#ifndef NO_PtFileSelectionInfo_t
void cachePtFileSelectionInfo_tFields(JNIEnv *env, jobject lpObject);
PtFileSelectionInfo_t *getPtFileSelectionInfo_tFields(JNIEnv *env, jobject lpObject, PtFileSelectionInfo_t *lpStruct);
void setPtFileSelectionInfo_tFields(JNIEnv *env, jobject lpObject, PtFileSelectionInfo_t *lpStruct);
#define PtFileSelectionInfo_t_sizeof() sizeof(PtFileSelectionInfo_t)
#else
#define cachePtFileSelectionInfo_tFields(a,b)
#define getPtFileSelectionInfo_tFields(a,b,c) NULL
#define setPtFileSelectionInfo_tFields(a,b,c)
#define PtFileSelectionInfo_t_sizeof() 0
#endif

#ifndef NO_PtScrollbarCallback_t
void cachePtScrollbarCallback_tFields(JNIEnv *env, jobject lpObject);
PtScrollbarCallback_t *getPtScrollbarCallback_tFields(JNIEnv *env, jobject lpObject, PtScrollbarCallback_t *lpStruct);
void setPtScrollbarCallback_tFields(JNIEnv *env, jobject lpObject, PtScrollbarCallback_t *lpStruct);
#define PtScrollbarCallback_t_sizeof() sizeof(PtScrollbarCallback_t)
#else
#define cachePtScrollbarCallback_tFields(a,b)
#define getPtScrollbarCallback_tFields(a,b,c) NULL
#define setPtScrollbarCallback_tFields(a,b,c)
#define PtScrollbarCallback_t_sizeof() 0
#endif

#ifndef NO_PtTextCallback_t
void cachePtTextCallback_tFields(JNIEnv *env, jobject lpObject);
PtTextCallback_t *getPtTextCallback_tFields(JNIEnv *env, jobject lpObject, PtTextCallback_t *lpStruct);
void setPtTextCallback_tFields(JNIEnv *env, jobject lpObject, PtTextCallback_t *lpStruct);
#define PtTextCallback_t_sizeof() sizeof(PtTextCallback_t)
#else
#define cachePtTextCallback_tFields(a,b)
#define getPtTextCallback_tFields(a,b,c) NULL
#define setPtTextCallback_tFields(a,b,c)
#define PtTextCallback_t_sizeof() 0
#endif

#ifndef NO_PtWebClient2Data_t
void cachePtWebClient2Data_tFields(JNIEnv *env, jobject lpObject);
PtWebClient2Data_t *getPtWebClient2Data_tFields(JNIEnv *env, jobject lpObject, PtWebClient2Data_t *lpStruct);
void setPtWebClient2Data_tFields(JNIEnv *env, jobject lpObject, PtWebClient2Data_t *lpStruct);
#define PtWebClient2Data_t_sizeof() sizeof(PtWebClient2Data_t)
#else
#define cachePtWebClient2Data_tFields(a,b)
#define getPtWebClient2Data_tFields(a,b,c) NULL
#define setPtWebClient2Data_tFields(a,b,c)
#define PtWebClient2Data_t_sizeof() 0
#endif

#ifndef NO_PtWebDataReqCallback_t
void cachePtWebDataReqCallback_tFields(JNIEnv *env, jobject lpObject);
PtWebDataReqCallback_t *getPtWebDataReqCallback_tFields(JNIEnv *env, jobject lpObject, PtWebDataReqCallback_t *lpStruct);
void setPtWebDataReqCallback_tFields(JNIEnv *env, jobject lpObject, PtWebDataReqCallback_t *lpStruct);
#define PtWebDataReqCallback_t_sizeof() sizeof(PtWebDataReqCallback_t)
#else
#define cachePtWebDataReqCallback_tFields(a,b)
#define getPtWebDataReqCallback_tFields(a,b,c) NULL
#define setPtWebDataReqCallback_tFields(a,b,c)
#define PtWebDataReqCallback_t_sizeof() 0
#endif

#ifndef NO_PtWebMetaDataCallback_t
void cachePtWebMetaDataCallback_tFields(JNIEnv *env, jobject lpObject);
PtWebMetaDataCallback_t *getPtWebMetaDataCallback_tFields(JNIEnv *env, jobject lpObject, PtWebMetaDataCallback_t *lpStruct);
void setPtWebMetaDataCallback_tFields(JNIEnv *env, jobject lpObject, PtWebMetaDataCallback_t *lpStruct);
#define PtWebMetaDataCallback_t_sizeof() sizeof(PtWebMetaDataCallback_t)
#else
#define cachePtWebMetaDataCallback_tFields(a,b)
#define getPtWebMetaDataCallback_tFields(a,b,c) NULL
#define setPtWebMetaDataCallback_tFields(a,b,c)
#define PtWebMetaDataCallback_t_sizeof() 0
#endif

#ifndef NO_PtWebStatusCallback_t
void cachePtWebStatusCallback_tFields(JNIEnv *env, jobject lpObject);
PtWebStatusCallback_t *getPtWebStatusCallback_tFields(JNIEnv *env, jobject lpObject, PtWebStatusCallback_t *lpStruct);
void setPtWebStatusCallback_tFields(JNIEnv *env, jobject lpObject, PtWebStatusCallback_t *lpStruct);
#define PtWebStatusCallback_t_sizeof() sizeof(PtWebStatusCallback_t)
#else
#define cachePtWebStatusCallback_tFields(a,b)
#define getPtWebStatusCallback_tFields(a,b,c) NULL
#define setPtWebStatusCallback_tFields(a,b,c)
#define PtWebStatusCallback_t_sizeof() 0
#endif

#ifndef NO_PtWebWindowCallback_t
void cachePtWebWindowCallback_tFields(JNIEnv *env, jobject lpObject);
PtWebWindowCallback_t *getPtWebWindowCallback_tFields(JNIEnv *env, jobject lpObject, PtWebWindowCallback_t *lpStruct);
void setPtWebWindowCallback_tFields(JNIEnv *env, jobject lpObject, PtWebWindowCallback_t *lpStruct);
#define PtWebWindowCallback_t_sizeof() sizeof(PtWebWindowCallback_t)
#else
#define cachePtWebWindowCallback_tFields(a,b)
#define getPtWebWindowCallback_tFields(a,b,c) NULL
#define setPtWebWindowCallback_tFields(a,b,c)
#define PtWebWindowCallback_t_sizeof() 0
#endif

#ifndef NO_utsname
void cacheutsnameFields(JNIEnv *env, jobject lpObject);
utsname *getutsnameFields(JNIEnv *env, jobject lpObject, utsname *lpStruct);
void setutsnameFields(JNIEnv *env, jobject lpObject, utsname *lpStruct);
#define utsname_sizeof() sizeof(utsname)
#else
#define cacheutsnameFields(a,b)
#define getutsnameFields(a,b,c) NULL
#define setutsnameFields(a,b,c)
#define utsname_sizeof() 0
#endif

