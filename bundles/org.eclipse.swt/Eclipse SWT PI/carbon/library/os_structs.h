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

#ifndef NO_AEDesc
AEDesc *getAEDescFields(JNIEnv *env, jobject lpObject, AEDesc *lpStruct);
void setAEDescFields(JNIEnv *env, jobject lpObject, AEDesc *lpStruct);
#define AEDesc_sizeof() sizeof(AEDesc)
#else
#define getAEDescFields(a,b,c) NULL
#define setAEDescFields(a,b,c)
#define AEDesc_sizeof() 0
#endif

#ifndef NO_ATSTrapezoid
ATSTrapezoid *getATSTrapezoidFields(JNIEnv *env, jobject lpObject, ATSTrapezoid *lpStruct);
void setATSTrapezoidFields(JNIEnv *env, jobject lpObject, ATSTrapezoid *lpStruct);
#define ATSTrapezoid_sizeof() sizeof(ATSTrapezoid)
#else
#define getATSTrapezoidFields(a,b,c) NULL
#define setATSTrapezoidFields(a,b,c)
#define ATSTrapezoid_sizeof() 0
#endif

#ifndef NO_ATSUCaret
ATSUCaret *getATSUCaretFields(JNIEnv *env, jobject lpObject, ATSUCaret *lpStruct);
void setATSUCaretFields(JNIEnv *env, jobject lpObject, ATSUCaret *lpStruct);
#define ATSUCaret_sizeof() sizeof(ATSUCaret)
#else
#define getATSUCaretFields(a,b,c) NULL
#define setATSUCaretFields(a,b,c)
#define ATSUCaret_sizeof() 0
#endif

#ifndef NO_ATSUTab
ATSUTab *getATSUTabFields(JNIEnv *env, jobject lpObject, ATSUTab *lpStruct);
void setATSUTabFields(JNIEnv *env, jobject lpObject, ATSUTab *lpStruct);
#define ATSUTab_sizeof() sizeof(ATSUTab)
#else
#define getATSUTabFields(a,b,c) NULL
#define setATSUTabFields(a,b,c)
#define ATSUTab_sizeof() 0
#endif

#ifndef NO_ATSUUnhighlightData
ATSUUnhighlightData *getATSUUnhighlightDataFields(JNIEnv *env, jobject lpObject, ATSUUnhighlightData *lpStruct);
void setATSUUnhighlightDataFields(JNIEnv *env, jobject lpObject, ATSUUnhighlightData *lpStruct);
#define ATSUUnhighlightData_sizeof() sizeof(ATSUUnhighlightData)
#else
#define getATSUUnhighlightDataFields(a,b,c) NULL
#define setATSUUnhighlightDataFields(a,b,c)
#define ATSUUnhighlightData_sizeof() 0
#endif

#ifndef NO_AlertStdCFStringAlertParamRec
AlertStdCFStringAlertParamRec *getAlertStdCFStringAlertParamRecFields(JNIEnv *env, jobject lpObject, AlertStdCFStringAlertParamRec *lpStruct);
void setAlertStdCFStringAlertParamRecFields(JNIEnv *env, jobject lpObject, AlertStdCFStringAlertParamRec *lpStruct);
#define AlertStdCFStringAlertParamRec_sizeof() sizeof(AlertStdCFStringAlertParamRec)
#else
#define getAlertStdCFStringAlertParamRecFields(a,b,c) NULL
#define setAlertStdCFStringAlertParamRecFields(a,b,c)
#define AlertStdCFStringAlertParamRec_sizeof() 0
#endif

#ifndef NO_BitMap
BitMap *getBitMapFields(JNIEnv *env, jobject lpObject, BitMap *lpStruct);
void setBitMapFields(JNIEnv *env, jobject lpObject, BitMap *lpStruct);
#define BitMap_sizeof() sizeof(BitMap)
#else
#define getBitMapFields(a,b,c) NULL
#define setBitMapFields(a,b,c)
#define BitMap_sizeof() 0
#endif

#ifndef NO_CFRange
CFRange *getCFRangeFields(JNIEnv *env, jobject lpObject, CFRange *lpStruct);
void setCFRangeFields(JNIEnv *env, jobject lpObject, CFRange *lpStruct);
#define CFRange_sizeof() sizeof(CFRange)
#else
#define getCFRangeFields(a,b,c) NULL
#define setCFRangeFields(a,b,c)
#define CFRange_sizeof() 0
#endif

#ifndef NO_CGPoint
CGPoint *getCGPointFields(JNIEnv *env, jobject lpObject, CGPoint *lpStruct);
void setCGPointFields(JNIEnv *env, jobject lpObject, CGPoint *lpStruct);
#define CGPoint_sizeof() sizeof(CGPoint)
#else
#define getCGPointFields(a,b,c) NULL
#define setCGPointFields(a,b,c)
#define CGPoint_sizeof() 0
#endif

#ifndef NO_CGRect
CGRect *getCGRectFields(JNIEnv *env, jobject lpObject, CGRect *lpStruct);
void setCGRectFields(JNIEnv *env, jobject lpObject, CGRect *lpStruct);
#define CGRect_sizeof() sizeof(CGRect)
#else
#define getCGRectFields(a,b,c) NULL
#define setCGRectFields(a,b,c)
#define CGRect_sizeof() 0
#endif

#ifndef NO_ColorPickerInfo
ColorPickerInfo *getColorPickerInfoFields(JNIEnv *env, jobject lpObject, ColorPickerInfo *lpStruct);
void setColorPickerInfoFields(JNIEnv *env, jobject lpObject, ColorPickerInfo *lpStruct);
#define ColorPickerInfo_sizeof() sizeof(ColorPickerInfo)
#else
#define getColorPickerInfoFields(a,b,c) NULL
#define setColorPickerInfoFields(a,b,c)
#define ColorPickerInfo_sizeof() 0
#endif

#ifndef NO_ControlButtonContentInfo
ControlButtonContentInfo *getControlButtonContentInfoFields(JNIEnv *env, jobject lpObject, ControlButtonContentInfo *lpStruct);
void setControlButtonContentInfoFields(JNIEnv *env, jobject lpObject, ControlButtonContentInfo *lpStruct);
#define ControlButtonContentInfo_sizeof() sizeof(ControlButtonContentInfo)
#else
#define getControlButtonContentInfoFields(a,b,c) NULL
#define setControlButtonContentInfoFields(a,b,c)
#define ControlButtonContentInfo_sizeof() 0
#endif

#ifndef NO_ControlFontStyleRec
ControlFontStyleRec *getControlFontStyleRecFields(JNIEnv *env, jobject lpObject, ControlFontStyleRec *lpStruct);
void setControlFontStyleRecFields(JNIEnv *env, jobject lpObject, ControlFontStyleRec *lpStruct);
#define ControlFontStyleRec_sizeof() sizeof(ControlFontStyleRec)
#else
#define getControlFontStyleRecFields(a,b,c) NULL
#define setControlFontStyleRecFields(a,b,c)
#define ControlFontStyleRec_sizeof() 0
#endif

#ifndef NO_ControlTabEntry
ControlTabEntry *getControlTabEntryFields(JNIEnv *env, jobject lpObject, ControlTabEntry *lpStruct);
void setControlTabEntryFields(JNIEnv *env, jobject lpObject, ControlTabEntry *lpStruct);
#define ControlTabEntry_sizeof() sizeof(ControlTabEntry)
#else
#define getControlTabEntryFields(a,b,c) NULL
#define setControlTabEntryFields(a,b,c)
#define ControlTabEntry_sizeof() 0
#endif

#ifndef NO_ControlTabInfoRecV1
ControlTabInfoRecV1 *getControlTabInfoRecV1Fields(JNIEnv *env, jobject lpObject, ControlTabInfoRecV1 *lpStruct);
void setControlTabInfoRecV1Fields(JNIEnv *env, jobject lpObject, ControlTabInfoRecV1 *lpStruct);
#define ControlTabInfoRecV1_sizeof() sizeof(ControlTabInfoRecV1)
#else
#define getControlTabInfoRecV1Fields(a,b,c) NULL
#define setControlTabInfoRecV1Fields(a,b,c)
#define ControlTabInfoRecV1_sizeof() 0
#endif

#ifndef NO_Cursor
Cursor *getCursorFields(JNIEnv *env, jobject lpObject, Cursor *lpStruct);
void setCursorFields(JNIEnv *env, jobject lpObject, Cursor *lpStruct);
#define Cursor_sizeof() sizeof(Cursor)
#else
#define getCursorFields(a,b,c) NULL
#define setCursorFields(a,b,c)
#define Cursor_sizeof() 0
#endif

#ifndef NO_DataBrowserCallbacks
DataBrowserCallbacks *getDataBrowserCallbacksFields(JNIEnv *env, jobject lpObject, DataBrowserCallbacks *lpStruct);
void setDataBrowserCallbacksFields(JNIEnv *env, jobject lpObject, DataBrowserCallbacks *lpStruct);
#define DataBrowserCallbacks_sizeof() sizeof(DataBrowserCallbacks)
#else
#define getDataBrowserCallbacksFields(a,b,c) NULL
#define setDataBrowserCallbacksFields(a,b,c)
#define DataBrowserCallbacks_sizeof() 0
#endif

#ifndef NO_DataBrowserCustomCallbacks
DataBrowserCustomCallbacks *getDataBrowserCustomCallbacksFields(JNIEnv *env, jobject lpObject, DataBrowserCustomCallbacks *lpStruct);
void setDataBrowserCustomCallbacksFields(JNIEnv *env, jobject lpObject, DataBrowserCustomCallbacks *lpStruct);
#define DataBrowserCustomCallbacks_sizeof() sizeof(DataBrowserCustomCallbacks)
#else
#define getDataBrowserCustomCallbacksFields(a,b,c) NULL
#define setDataBrowserCustomCallbacksFields(a,b,c)
#define DataBrowserCustomCallbacks_sizeof() 0
#endif

#ifndef NO_DataBrowserListViewColumnDesc
DataBrowserListViewColumnDesc *getDataBrowserListViewColumnDescFields(JNIEnv *env, jobject lpObject, DataBrowserListViewColumnDesc *lpStruct);
void setDataBrowserListViewColumnDescFields(JNIEnv *env, jobject lpObject, DataBrowserListViewColumnDesc *lpStruct);
#define DataBrowserListViewColumnDesc_sizeof() sizeof(DataBrowserListViewColumnDesc)
#else
#define getDataBrowserListViewColumnDescFields(a,b,c) NULL
#define setDataBrowserListViewColumnDescFields(a,b,c)
#define DataBrowserListViewColumnDesc_sizeof() 0
#endif

#ifndef NO_DataBrowserListViewHeaderDesc
DataBrowserListViewHeaderDesc *getDataBrowserListViewHeaderDescFields(JNIEnv *env, jobject lpObject, DataBrowserListViewHeaderDesc *lpStruct);
void setDataBrowserListViewHeaderDescFields(JNIEnv *env, jobject lpObject, DataBrowserListViewHeaderDesc *lpStruct);
#define DataBrowserListViewHeaderDesc_sizeof() sizeof(DataBrowserListViewHeaderDesc)
#else
#define getDataBrowserListViewHeaderDescFields(a,b,c) NULL
#define setDataBrowserListViewHeaderDescFields(a,b,c)
#define DataBrowserListViewHeaderDesc_sizeof() 0
#endif

#ifndef NO_EventRecord
EventRecord *getEventRecordFields(JNIEnv *env, jobject lpObject, EventRecord *lpStruct);
void setEventRecordFields(JNIEnv *env, jobject lpObject, EventRecord *lpStruct);
#define EventRecord_sizeof() sizeof(EventRecord)
#else
#define getEventRecordFields(a,b,c) NULL
#define setEventRecordFields(a,b,c)
#define EventRecord_sizeof() 0
#endif

#ifndef NO_FontInfo
FontInfo *getFontInfoFields(JNIEnv *env, jobject lpObject, FontInfo *lpStruct);
void setFontInfoFields(JNIEnv *env, jobject lpObject, FontInfo *lpStruct);
#define FontInfo_sizeof() sizeof(FontInfo)
#else
#define getFontInfoFields(a,b,c) NULL
#define setFontInfoFields(a,b,c)
#define FontInfo_sizeof() 0
#endif

#ifndef NO_FontSelectionQDStyle
FontSelectionQDStyle *getFontSelectionQDStyleFields(JNIEnv *env, jobject lpObject, FontSelectionQDStyle *lpStruct);
void setFontSelectionQDStyleFields(JNIEnv *env, jobject lpObject, FontSelectionQDStyle *lpStruct);
#define FontSelectionQDStyle_sizeof() sizeof(FontSelectionQDStyle)
#else
#define getFontSelectionQDStyleFields(a,b,c) NULL
#define setFontSelectionQDStyleFields(a,b,c)
#define FontSelectionQDStyle_sizeof() 0
#endif

#ifndef NO_GDevice
GDevice *getGDeviceFields(JNIEnv *env, jobject lpObject, GDevice *lpStruct);
void setGDeviceFields(JNIEnv *env, jobject lpObject, GDevice *lpStruct);
#define GDevice_sizeof() sizeof(GDevice)
#else
#define getGDeviceFields(a,b,c) NULL
#define setGDeviceFields(a,b,c)
#define GDevice_sizeof() 0
#endif

#ifndef NO_HICommand
HICommand *getHICommandFields(JNIEnv *env, jobject lpObject, HICommand *lpStruct);
void setHICommandFields(JNIEnv *env, jobject lpObject, HICommand *lpStruct);
#define HICommand_sizeof() sizeof(HICommand)
#else
#define getHICommandFields(a,b,c) NULL
#define setHICommandFields(a,b,c)
#define HICommand_sizeof() 0
#endif

#ifndef NO_HMHelpContentRec
HMHelpContentRec *getHMHelpContentRecFields(JNIEnv *env, jobject lpObject, HMHelpContentRec *lpStruct);
void setHMHelpContentRecFields(JNIEnv *env, jobject lpObject, HMHelpContentRec *lpStruct);
#define HMHelpContentRec_sizeof() sizeof(HMHelpContentRec)
#else
#define getHMHelpContentRecFields(a,b,c) NULL
#define setHMHelpContentRecFields(a,b,c)
#define HMHelpContentRec_sizeof() 0
#endif

#ifndef NO_MenuTrackingData
MenuTrackingData *getMenuTrackingDataFields(JNIEnv *env, jobject lpObject, MenuTrackingData *lpStruct);
void setMenuTrackingDataFields(JNIEnv *env, jobject lpObject, MenuTrackingData *lpStruct);
#define MenuTrackingData_sizeof() sizeof(MenuTrackingData)
#else
#define getMenuTrackingDataFields(a,b,c) NULL
#define setMenuTrackingDataFields(a,b,c)
#define MenuTrackingData_sizeof() 0
#endif

#ifndef NO_NavDialogCreationOptions
NavDialogCreationOptions *getNavDialogCreationOptionsFields(JNIEnv *env, jobject lpObject, NavDialogCreationOptions *lpStruct);
void setNavDialogCreationOptionsFields(JNIEnv *env, jobject lpObject, NavDialogCreationOptions *lpStruct);
#define NavDialogCreationOptions_sizeof() sizeof(NavDialogCreationOptions)
#else
#define getNavDialogCreationOptionsFields(a,b,c) NULL
#define setNavDialogCreationOptionsFields(a,b,c)
#define NavDialogCreationOptions_sizeof() 0
#endif

#ifndef NO_NavReplyRecord
NavReplyRecord *getNavReplyRecordFields(JNIEnv *env, jobject lpObject, NavReplyRecord *lpStruct);
void setNavReplyRecordFields(JNIEnv *env, jobject lpObject, NavReplyRecord *lpStruct);
#define NavReplyRecord_sizeof() sizeof(NavReplyRecord)
#else
#define getNavReplyRecordFields(a,b,c) NULL
#define setNavReplyRecordFields(a,b,c)
#define NavReplyRecord_sizeof() 0
#endif

#ifndef NO_PMRect
PMRect *getPMRectFields(JNIEnv *env, jobject lpObject, PMRect *lpStruct);
void setPMRectFields(JNIEnv *env, jobject lpObject, PMRect *lpStruct);
#define PMRect_sizeof() sizeof(PMRect)
#else
#define getPMRectFields(a,b,c) NULL
#define setPMRectFields(a,b,c)
#define PMRect_sizeof() 0
#endif

#ifndef NO_PMResolution
PMResolution *getPMResolutionFields(JNIEnv *env, jobject lpObject, PMResolution *lpStruct);
void setPMResolutionFields(JNIEnv *env, jobject lpObject, PMResolution *lpStruct);
#define PMResolution_sizeof() sizeof(PMResolution)
#else
#define getPMResolutionFields(a,b,c) NULL
#define setPMResolutionFields(a,b,c)
#define PMResolution_sizeof() 0
#endif

#ifndef NO_PixMap
PixMap *getPixMapFields(JNIEnv *env, jobject lpObject, PixMap *lpStruct);
void setPixMapFields(JNIEnv *env, jobject lpObject, PixMap *lpStruct);
#define PixMap_sizeof() sizeof(PixMap)
#else
#define getPixMapFields(a,b,c) NULL
#define setPixMapFields(a,b,c)
#define PixMap_sizeof() 0
#endif

#ifndef NO_Point
Point *getPointFields(JNIEnv *env, jobject lpObject, Point *lpStruct);
void setPointFields(JNIEnv *env, jobject lpObject, Point *lpStruct);
#define Point_sizeof() sizeof(Point)
#else
#define getPointFields(a,b,c) NULL
#define setPointFields(a,b,c)
#define Point_sizeof() 0
#endif

#ifndef NO_RGBColor
RGBColor *getRGBColorFields(JNIEnv *env, jobject lpObject, RGBColor *lpStruct);
void setRGBColorFields(JNIEnv *env, jobject lpObject, RGBColor *lpStruct);
#define RGBColor_sizeof() sizeof(RGBColor)
#else
#define getRGBColorFields(a,b,c) NULL
#define setRGBColorFields(a,b,c)
#define RGBColor_sizeof() 0
#endif

#ifndef NO_Rect
Rect *getRectFields(JNIEnv *env, jobject lpObject, Rect *lpStruct);
void setRectFields(JNIEnv *env, jobject lpObject, Rect *lpStruct);
#define Rect_sizeof() sizeof(Rect)
#else
#define getRectFields(a,b,c) NULL
#define setRectFields(a,b,c)
#define Rect_sizeof() 0
#endif

#ifndef NO_TXNBackground
TXNBackground *getTXNBackgroundFields(JNIEnv *env, jobject lpObject, TXNBackground *lpStruct);
void setTXNBackgroundFields(JNIEnv *env, jobject lpObject, TXNBackground *lpStruct);
#define TXNBackground_sizeof() sizeof(TXNBackground)
#else
#define getTXNBackgroundFields(a,b,c) NULL
#define setTXNBackgroundFields(a,b,c)
#define TXNBackground_sizeof() 0
#endif

#ifndef NO_TXNLongRect
TXNLongRect *getTXNLongRectFields(JNIEnv *env, jobject lpObject, TXNLongRect *lpStruct);
void setTXNLongRectFields(JNIEnv *env, jobject lpObject, TXNLongRect *lpStruct);
#define TXNLongRect_sizeof() sizeof(TXNLongRect)
#else
#define getTXNLongRectFields(a,b,c) NULL
#define setTXNLongRectFields(a,b,c)
#define TXNLongRect_sizeof() 0
#endif

#ifndef NO_ThemeButtonDrawInfo
ThemeButtonDrawInfo *getThemeButtonDrawInfoFields(JNIEnv *env, jobject lpObject, ThemeButtonDrawInfo *lpStruct);
void setThemeButtonDrawInfoFields(JNIEnv *env, jobject lpObject, ThemeButtonDrawInfo *lpStruct);
#define ThemeButtonDrawInfo_sizeof() sizeof(ThemeButtonDrawInfo)
#else
#define getThemeButtonDrawInfoFields(a,b,c) NULL
#define setThemeButtonDrawInfoFields(a,b,c)
#define ThemeButtonDrawInfo_sizeof() 0
#endif

