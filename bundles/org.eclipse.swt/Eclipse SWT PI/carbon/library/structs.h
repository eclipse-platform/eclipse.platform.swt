/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

/**
 * JNI SWT object field getters and setters declarations for Mac/Carbon structs.
 */

#include <Carbon/Carbon.h>

#ifndef NO_AEDesc
AEDesc *getAEDescFields(JNIEnv *env, jobject lpObject, AEDesc *lpStruct);
void setAEDescFields(JNIEnv *env, jobject lpObject, AEDesc *lpStruct);
#else
#define getAEDescFields(a,b,c) NULL
#define setAEDescFields(a,b,c)
#endif /* NO_AEDesc */

#ifndef NO_CFRange
CFRange *getCFRangeFields(JNIEnv *env, jobject lpObject, CFRange *lpStruct);
void setCFRangeFields(JNIEnv *env, jobject lpObject, CFRange *lpStruct);
#else
#define getCFRangeFields(a,b,c) NULL
#define setCFRangeFields(a,b,c)
#endif /* NO_CFRange */

#ifndef NO_CGPoint
CGPoint *getCGPointFields(JNIEnv *env, jobject lpObject, CGPoint *lpStruct);
void setCGPointFields(JNIEnv *env, jobject lpObject, CGPoint *lpStruct);
#else
#define getCGPointFields(a,b,c) NULL
#define setCGPointFields(a,b,c)
#endif /* NO_CGPoint */

#ifndef NO_CGRect
CGRect *getCGRectFields(JNIEnv *env, jobject lpObject, CGRect *lpStruct);
void setCGRectFields(JNIEnv *env, jobject lpObject, CGRect *lpStruct);
#else
#define getCGRectFields(a,b,c) NULL
#define setCGRectFields(a,b,c)
#endif /* NO_CGRect */

#ifndef NO_ColorPickerInfo
ColorPickerInfo *getColorPickerInfoFields(JNIEnv *env, jobject lpObject, ColorPickerInfo *lpStruct);
void setColorPickerInfoFields(JNIEnv *env, jobject lpObject, ColorPickerInfo *lpStruct);
#else
#define getColorPickerInfoFields(a,b,c) NULL
#define setColorPickerInfoFields(a,b,c)
#endif /* NO_ColorPickerInfo */

#ifndef NO_ControlButtonContentInfo
ControlButtonContentInfo *getControlButtonContentInfoFields(JNIEnv *env, jobject lpObject, ControlButtonContentInfo *lpStruct);
void setControlButtonContentInfoFields(JNIEnv *env, jobject lpObject, ControlButtonContentInfo *lpStruct);
#else
#define getControlButtonContentInfoFields(a,b,c) NULL
#define setControlButtonContentInfoFields(a,b,c)
#endif /* NO_ControlButtonContentInfo */

#ifndef NO_ControlFontStyleRec
ControlFontStyleRec *getControlFontStyleRecFields(JNIEnv *env, jobject lpObject, ControlFontStyleRec *lpStruct);
void setControlFontStyleRecFields(JNIEnv *env, jobject lpObject, ControlFontStyleRec *lpStruct);
#else
#define getControlFontStyleRecFields(a,b,c) NULL
#define setControlFontStyleRecFields(a,b,c)
#endif /* NO_ControlFontStyleRec */

#ifndef NO_ControlTabInfoRecV1
ControlTabInfoRecV1 *getControlTabInfoRecV1Fields(JNIEnv *env, jobject lpObject, ControlTabInfoRecV1 *lpStruct);
void setControlTabInfoRecV1Fields(JNIEnv *env, jobject lpObject, ControlTabInfoRecV1 *lpStruct);
#else
#define getControlTabInfoRecV1Fields(a,b,c) NULL
#define setControlTabInfoRecV1Fields(a,b,c)
#endif /* NO_ControlTabInfoRecV1 */

#ifndef NO_DataBrowserCallbacks
DataBrowserCallbacks *getDataBrowserCallbacksFields(JNIEnv *env, jobject lpObject, DataBrowserCallbacks *lpStruct);
void setDataBrowserCallbacksFields(JNIEnv *env, jobject lpObject, DataBrowserCallbacks *lpStruct);
#else
#define getDataBrowserCallbacksFields(a,b,c) NULL
#define setDataBrowserCallbacksFields(a,b,c)
#endif /* NO_DataBrowserCallbacks */

#ifndef NO_DataBrowserListViewColumnDesc
DataBrowserListViewColumnDesc *getDataBrowserListViewColumnDescFields(JNIEnv *env, jobject lpObject, DataBrowserListViewColumnDesc *lpStruct);
void setDataBrowserListViewColumnDescFields(JNIEnv *env, jobject lpObject, DataBrowserListViewColumnDesc *lpStruct);
#else
#define getDataBrowserListViewColumnDescFields(a,b,c) NULL
#define setDataBrowserListViewColumnDescFields(a,b,c)
#endif /* NO_DataBrowserListViewColumnDesc */

#ifndef NO_EventRecord
EventRecord *getEventRecordFields(JNIEnv *env, jobject lpObject, EventRecord *lpStruct);
void setEventRecordFields(JNIEnv *env, jobject lpObject, EventRecord *lpStruct);
#else
#define getEventRecordFields(a,b,c) NULL
#define setEventRecordFields(a,b,c)
#endif /* NO_EventRecord */

#ifndef NO_NavDialogCreationOptions
NavDialogCreationOptions *getNavDialogCreationOptionsFields(JNIEnv *env, jobject lpObject, NavDialogCreationOptions *lpStruct);
void setNavDialogCreationOptionsFields(JNIEnv *env, jobject lpObject, NavDialogCreationOptions *lpStruct);
#else
#define getNavDialogCreationOptionsFields(a,b,c) NULL
#define setNavDialogCreationOptionsFields(a,b,c)
#endif /* NO_NavDialogCreationOptions */

#ifndef NO_NavReplyRecord
NavReplyRecord *getNavReplyRecordFields(JNIEnv *env, jobject lpObject, NavReplyRecord *lpStruct);
void setNavReplyRecordFields(JNIEnv *env, jobject lpObject, NavReplyRecord *lpStruct);
#else
#define getNavReplyRecordFields(a,b,c) NULL
#define setNavReplyRecordFields(a,b,c)
#endif /* NO_NavReplyRecord */

#ifndef NO_Point
Point *getPointFields(JNIEnv *env, jobject lpObject, Point *lpStruct);
void setPointFields(JNIEnv *env, jobject lpObject, Point *lpStruct);
#else
#define getPointFields(a,b,c) NULL
#define setPointFields(a,b,c)
#endif /* NO_Point */

#ifndef NO_RGBColor
RGBColor *getRGBColorFields(JNIEnv *env, jobject lpObject, RGBColor *lpStruct);
void setRGBColorFields(JNIEnv *env, jobject lpObject, RGBColor *lpStruct);
#else
#define getRGBColorFields(a,b,c) NULL
#define setRGBColorFields(a,b,c)
#endif /* NO_RGBColor */

#ifndef NO_Rect
Rect *getRectFields(JNIEnv *env, jobject lpObject, Rect *lpStruct);
void setRectFields(JNIEnv *env, jobject lpObject, Rect *lpStruct);
#else
#define getRectFields(a,b,c) NULL
#define setRectFields(a,b,c)
#endif /* NO_Rect */

#ifndef NO_ThemeButtonDrawInfo
ThemeButtonDrawInfo *getThemeButtonDrawInfoFields(JNIEnv *env, jobject lpObject, ThemeButtonDrawInfo *lpStruct);
void setThemeButtonDrawInfoFields(JNIEnv *env, jobject lpObject, ThemeButtonDrawInfo *lpStruct);
#else
#define getThemeButtonDrawInfoFields(a,b,c) NULL
#define setThemeButtonDrawInfoFields(a,b,c)
#endif /* NO_ThemeButtonDrawInfo */
