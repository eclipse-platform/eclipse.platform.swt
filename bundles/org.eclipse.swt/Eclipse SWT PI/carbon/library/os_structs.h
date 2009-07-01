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

#ifndef NO_AEDesc
void cacheAEDescFields(JNIEnv *env, jobject lpObject);
AEDesc *getAEDescFields(JNIEnv *env, jobject lpObject, AEDesc *lpStruct);
void setAEDescFields(JNIEnv *env, jobject lpObject, AEDesc *lpStruct);
#define AEDesc_sizeof() sizeof(AEDesc)
#else
#define cacheAEDescFields(a,b)
#define getAEDescFields(a,b,c) NULL
#define setAEDescFields(a,b,c)
#define AEDesc_sizeof() 0
#endif

#ifndef NO_ATSFontMetrics
void cacheATSFontMetricsFields(JNIEnv *env, jobject lpObject);
ATSFontMetrics *getATSFontMetricsFields(JNIEnv *env, jobject lpObject, ATSFontMetrics *lpStruct);
void setATSFontMetricsFields(JNIEnv *env, jobject lpObject, ATSFontMetrics *lpStruct);
#define ATSFontMetrics_sizeof() sizeof(ATSFontMetrics)
#else
#define cacheATSFontMetricsFields(a,b)
#define getATSFontMetricsFields(a,b,c) NULL
#define setATSFontMetricsFields(a,b,c)
#define ATSFontMetrics_sizeof() 0
#endif

#ifndef NO_ATSLayoutRecord
void cacheATSLayoutRecordFields(JNIEnv *env, jobject lpObject);
ATSLayoutRecord *getATSLayoutRecordFields(JNIEnv *env, jobject lpObject, ATSLayoutRecord *lpStruct);
void setATSLayoutRecordFields(JNIEnv *env, jobject lpObject, ATSLayoutRecord *lpStruct);
#define ATSLayoutRecord_sizeof() sizeof(ATSLayoutRecord)
#else
#define cacheATSLayoutRecordFields(a,b)
#define getATSLayoutRecordFields(a,b,c) NULL
#define setATSLayoutRecordFields(a,b,c)
#define ATSLayoutRecord_sizeof() 0
#endif

#ifndef NO_ATSTrapezoid
void cacheATSTrapezoidFields(JNIEnv *env, jobject lpObject);
ATSTrapezoid *getATSTrapezoidFields(JNIEnv *env, jobject lpObject, ATSTrapezoid *lpStruct);
void setATSTrapezoidFields(JNIEnv *env, jobject lpObject, ATSTrapezoid *lpStruct);
#define ATSTrapezoid_sizeof() sizeof(ATSTrapezoid)
#else
#define cacheATSTrapezoidFields(a,b)
#define getATSTrapezoidFields(a,b,c) NULL
#define setATSTrapezoidFields(a,b,c)
#define ATSTrapezoid_sizeof() 0
#endif

#ifndef NO_ATSUCaret
void cacheATSUCaretFields(JNIEnv *env, jobject lpObject);
ATSUCaret *getATSUCaretFields(JNIEnv *env, jobject lpObject, ATSUCaret *lpStruct);
void setATSUCaretFields(JNIEnv *env, jobject lpObject, ATSUCaret *lpStruct);
#define ATSUCaret_sizeof() sizeof(ATSUCaret)
#else
#define cacheATSUCaretFields(a,b)
#define getATSUCaretFields(a,b,c) NULL
#define setATSUCaretFields(a,b,c)
#define ATSUCaret_sizeof() 0
#endif

#ifndef NO_ATSUTab
void cacheATSUTabFields(JNIEnv *env, jobject lpObject);
ATSUTab *getATSUTabFields(JNIEnv *env, jobject lpObject, ATSUTab *lpStruct);
void setATSUTabFields(JNIEnv *env, jobject lpObject, ATSUTab *lpStruct);
#define ATSUTab_sizeof() sizeof(ATSUTab)
#else
#define cacheATSUTabFields(a,b)
#define getATSUTabFields(a,b,c) NULL
#define setATSUTabFields(a,b,c)
#define ATSUTab_sizeof() 0
#endif

#ifndef NO_ATSUUnhighlightData
void cacheATSUUnhighlightDataFields(JNIEnv *env, jobject lpObject);
ATSUUnhighlightData *getATSUUnhighlightDataFields(JNIEnv *env, jobject lpObject, ATSUUnhighlightData *lpStruct);
void setATSUUnhighlightDataFields(JNIEnv *env, jobject lpObject, ATSUUnhighlightData *lpStruct);
#define ATSUUnhighlightData_sizeof() sizeof(ATSUUnhighlightData)
#else
#define cacheATSUUnhighlightDataFields(a,b)
#define getATSUUnhighlightDataFields(a,b,c) NULL
#define setATSUUnhighlightDataFields(a,b,c)
#define ATSUUnhighlightData_sizeof() 0
#endif

#ifndef NO_AlertStdCFStringAlertParamRec
void cacheAlertStdCFStringAlertParamRecFields(JNIEnv *env, jobject lpObject);
AlertStdCFStringAlertParamRec *getAlertStdCFStringAlertParamRecFields(JNIEnv *env, jobject lpObject, AlertStdCFStringAlertParamRec *lpStruct);
void setAlertStdCFStringAlertParamRecFields(JNIEnv *env, jobject lpObject, AlertStdCFStringAlertParamRec *lpStruct);
#define AlertStdCFStringAlertParamRec_sizeof() sizeof(AlertStdCFStringAlertParamRec)
#else
#define cacheAlertStdCFStringAlertParamRecFields(a,b)
#define getAlertStdCFStringAlertParamRecFields(a,b,c) NULL
#define setAlertStdCFStringAlertParamRecFields(a,b,c)
#define AlertStdCFStringAlertParamRec_sizeof() 0
#endif

#ifndef NO_BitMap
void cacheBitMapFields(JNIEnv *env, jobject lpObject);
BitMap *getBitMapFields(JNIEnv *env, jobject lpObject, BitMap *lpStruct);
void setBitMapFields(JNIEnv *env, jobject lpObject, BitMap *lpStruct);
#define BitMap_sizeof() sizeof(BitMap)
#else
#define cacheBitMapFields(a,b)
#define getBitMapFields(a,b,c) NULL
#define setBitMapFields(a,b,c)
#define BitMap_sizeof() 0
#endif

#ifndef NO_CFRange
void cacheCFRangeFields(JNIEnv *env, jobject lpObject);
CFRange *getCFRangeFields(JNIEnv *env, jobject lpObject, CFRange *lpStruct);
void setCFRangeFields(JNIEnv *env, jobject lpObject, CFRange *lpStruct);
#define CFRange_sizeof() sizeof(CFRange)
#else
#define cacheCFRangeFields(a,b)
#define getCFRangeFields(a,b,c) NULL
#define setCFRangeFields(a,b,c)
#define CFRange_sizeof() 0
#endif

#ifndef NO_CFRunLoopSourceContext
void cacheCFRunLoopSourceContextFields(JNIEnv *env, jobject lpObject);
CFRunLoopSourceContext *getCFRunLoopSourceContextFields(JNIEnv *env, jobject lpObject, CFRunLoopSourceContext *lpStruct);
void setCFRunLoopSourceContextFields(JNIEnv *env, jobject lpObject, CFRunLoopSourceContext *lpStruct);
#define CFRunLoopSourceContext_sizeof() sizeof(CFRunLoopSourceContext)
#else
#define cacheCFRunLoopSourceContextFields(a,b)
#define getCFRunLoopSourceContextFields(a,b,c) NULL
#define setCFRunLoopSourceContextFields(a,b,c)
#define CFRunLoopSourceContext_sizeof() 0
#endif

#ifndef NO_CGFunctionCallbacks
void cacheCGFunctionCallbacksFields(JNIEnv *env, jobject lpObject);
CGFunctionCallbacks *getCGFunctionCallbacksFields(JNIEnv *env, jobject lpObject, CGFunctionCallbacks *lpStruct);
void setCGFunctionCallbacksFields(JNIEnv *env, jobject lpObject, CGFunctionCallbacks *lpStruct);
#define CGFunctionCallbacks_sizeof() sizeof(CGFunctionCallbacks)
#else
#define cacheCGFunctionCallbacksFields(a,b)
#define getCGFunctionCallbacksFields(a,b,c) NULL
#define setCGFunctionCallbacksFields(a,b,c)
#define CGFunctionCallbacks_sizeof() 0
#endif

#ifndef NO_CGPathElement
void cacheCGPathElementFields(JNIEnv *env, jobject lpObject);
CGPathElement *getCGPathElementFields(JNIEnv *env, jobject lpObject, CGPathElement *lpStruct);
void setCGPathElementFields(JNIEnv *env, jobject lpObject, CGPathElement *lpStruct);
#define CGPathElement_sizeof() sizeof(CGPathElement)
#else
#define cacheCGPathElementFields(a,b)
#define getCGPathElementFields(a,b,c) NULL
#define setCGPathElementFields(a,b,c)
#define CGPathElement_sizeof() 0
#endif

#ifndef NO_CGPatternCallbacks
void cacheCGPatternCallbacksFields(JNIEnv *env, jobject lpObject);
CGPatternCallbacks *getCGPatternCallbacksFields(JNIEnv *env, jobject lpObject, CGPatternCallbacks *lpStruct);
void setCGPatternCallbacksFields(JNIEnv *env, jobject lpObject, CGPatternCallbacks *lpStruct);
#define CGPatternCallbacks_sizeof() sizeof(CGPatternCallbacks)
#else
#define cacheCGPatternCallbacksFields(a,b)
#define getCGPatternCallbacksFields(a,b,c) NULL
#define setCGPatternCallbacksFields(a,b,c)
#define CGPatternCallbacks_sizeof() 0
#endif

#ifndef NO_CGPoint
void cacheCGPointFields(JNIEnv *env, jobject lpObject);
CGPoint *getCGPointFields(JNIEnv *env, jobject lpObject, CGPoint *lpStruct);
void setCGPointFields(JNIEnv *env, jobject lpObject, CGPoint *lpStruct);
#define CGPoint_sizeof() sizeof(CGPoint)
#else
#define cacheCGPointFields(a,b)
#define getCGPointFields(a,b,c) NULL
#define setCGPointFields(a,b,c)
#define CGPoint_sizeof() 0
#endif

#ifndef NO_CGRect
void cacheCGRectFields(JNIEnv *env, jobject lpObject);
CGRect *getCGRectFields(JNIEnv *env, jobject lpObject, CGRect *lpStruct);
void setCGRectFields(JNIEnv *env, jobject lpObject, CGRect *lpStruct);
#define CGRect_sizeof() sizeof(CGRect)
#else
#define cacheCGRectFields(a,b)
#define getCGRectFields(a,b,c) NULL
#define setCGRectFields(a,b,c)
#define CGRect_sizeof() 0
#endif

#ifndef NO_CGSize
void cacheCGSizeFields(JNIEnv *env, jobject lpObject);
CGSize *getCGSizeFields(JNIEnv *env, jobject lpObject, CGSize *lpStruct);
void setCGSizeFields(JNIEnv *env, jobject lpObject, CGSize *lpStruct);
#define CGSize_sizeof() sizeof(CGSize)
#else
#define cacheCGSizeFields(a,b)
#define getCGSizeFields(a,b,c) NULL
#define setCGSizeFields(a,b,c)
#define CGSize_sizeof() 0
#endif

#ifndef NO_ColorPickerInfo
void cacheColorPickerInfoFields(JNIEnv *env, jobject lpObject);
ColorPickerInfo *getColorPickerInfoFields(JNIEnv *env, jobject lpObject, ColorPickerInfo *lpStruct);
void setColorPickerInfoFields(JNIEnv *env, jobject lpObject, ColorPickerInfo *lpStruct);
#define ColorPickerInfo_sizeof() sizeof(ColorPickerInfo)
#else
#define cacheColorPickerInfoFields(a,b)
#define getColorPickerInfoFields(a,b,c) NULL
#define setColorPickerInfoFields(a,b,c)
#define ColorPickerInfo_sizeof() 0
#endif

#ifndef NO_ControlButtonContentInfo
void cacheControlButtonContentInfoFields(JNIEnv *env, jobject lpObject);
ControlButtonContentInfo *getControlButtonContentInfoFields(JNIEnv *env, jobject lpObject, ControlButtonContentInfo *lpStruct);
void setControlButtonContentInfoFields(JNIEnv *env, jobject lpObject, ControlButtonContentInfo *lpStruct);
#define ControlButtonContentInfo_sizeof() sizeof(ControlButtonContentInfo)
#else
#define cacheControlButtonContentInfoFields(a,b)
#define getControlButtonContentInfoFields(a,b,c) NULL
#define setControlButtonContentInfoFields(a,b,c)
#define ControlButtonContentInfo_sizeof() 0
#endif

#ifndef NO_ControlEditTextSelectionRec
void cacheControlEditTextSelectionRecFields(JNIEnv *env, jobject lpObject);
ControlEditTextSelectionRec *getControlEditTextSelectionRecFields(JNIEnv *env, jobject lpObject, ControlEditTextSelectionRec *lpStruct);
void setControlEditTextSelectionRecFields(JNIEnv *env, jobject lpObject, ControlEditTextSelectionRec *lpStruct);
#define ControlEditTextSelectionRec_sizeof() sizeof(ControlEditTextSelectionRec)
#else
#define cacheControlEditTextSelectionRecFields(a,b)
#define getControlEditTextSelectionRecFields(a,b,c) NULL
#define setControlEditTextSelectionRecFields(a,b,c)
#define ControlEditTextSelectionRec_sizeof() 0
#endif

#ifndef NO_ControlFontStyleRec
void cacheControlFontStyleRecFields(JNIEnv *env, jobject lpObject);
ControlFontStyleRec *getControlFontStyleRecFields(JNIEnv *env, jobject lpObject, ControlFontStyleRec *lpStruct);
void setControlFontStyleRecFields(JNIEnv *env, jobject lpObject, ControlFontStyleRec *lpStruct);
#define ControlFontStyleRec_sizeof() sizeof(ControlFontStyleRec)
#else
#define cacheControlFontStyleRecFields(a,b)
#define getControlFontStyleRecFields(a,b,c) NULL
#define setControlFontStyleRecFields(a,b,c)
#define ControlFontStyleRec_sizeof() 0
#endif

#ifndef NO_ControlKind
void cacheControlKindFields(JNIEnv *env, jobject lpObject);
ControlKind *getControlKindFields(JNIEnv *env, jobject lpObject, ControlKind *lpStruct);
void setControlKindFields(JNIEnv *env, jobject lpObject, ControlKind *lpStruct);
#define ControlKind_sizeof() sizeof(ControlKind)
#else
#define cacheControlKindFields(a,b)
#define getControlKindFields(a,b,c) NULL
#define setControlKindFields(a,b,c)
#define ControlKind_sizeof() 0
#endif

#ifndef NO_ControlTabEntry
void cacheControlTabEntryFields(JNIEnv *env, jobject lpObject);
ControlTabEntry *getControlTabEntryFields(JNIEnv *env, jobject lpObject, ControlTabEntry *lpStruct);
void setControlTabEntryFields(JNIEnv *env, jobject lpObject, ControlTabEntry *lpStruct);
#define ControlTabEntry_sizeof() sizeof(ControlTabEntry)
#else
#define cacheControlTabEntryFields(a,b)
#define getControlTabEntryFields(a,b,c) NULL
#define setControlTabEntryFields(a,b,c)
#define ControlTabEntry_sizeof() 0
#endif

#ifndef NO_ControlTabInfoRecV1
void cacheControlTabInfoRecV1Fields(JNIEnv *env, jobject lpObject);
ControlTabInfoRecV1 *getControlTabInfoRecV1Fields(JNIEnv *env, jobject lpObject, ControlTabInfoRecV1 *lpStruct);
void setControlTabInfoRecV1Fields(JNIEnv *env, jobject lpObject, ControlTabInfoRecV1 *lpStruct);
#define ControlTabInfoRecV1_sizeof() sizeof(ControlTabInfoRecV1)
#else
#define cacheControlTabInfoRecV1Fields(a,b)
#define getControlTabInfoRecV1Fields(a,b,c) NULL
#define setControlTabInfoRecV1Fields(a,b,c)
#define ControlTabInfoRecV1_sizeof() 0
#endif

#ifndef NO_Cursor
void cacheCursorFields(JNIEnv *env, jobject lpObject);
Cursor *getCursorFields(JNIEnv *env, jobject lpObject, Cursor *lpStruct);
void setCursorFields(JNIEnv *env, jobject lpObject, Cursor *lpStruct);
#define Cursor_sizeof() sizeof(Cursor)
#else
#define cacheCursorFields(a,b)
#define getCursorFields(a,b,c) NULL
#define setCursorFields(a,b,c)
#define Cursor_sizeof() 0
#endif

#ifndef NO_DataBrowserAccessibilityItemInfo
void cacheDataBrowserAccessibilityItemInfoFields(JNIEnv *env, jobject lpObject);
DataBrowserAccessibilityItemInfo *getDataBrowserAccessibilityItemInfoFields(JNIEnv *env, jobject lpObject, DataBrowserAccessibilityItemInfo *lpStruct);
void setDataBrowserAccessibilityItemInfoFields(JNIEnv *env, jobject lpObject, DataBrowserAccessibilityItemInfo *lpStruct);
#define DataBrowserAccessibilityItemInfo_sizeof() sizeof(DataBrowserAccessibilityItemInfo)
#else
#define cacheDataBrowserAccessibilityItemInfoFields(a,b)
#define getDataBrowserAccessibilityItemInfoFields(a,b,c) NULL
#define setDataBrowserAccessibilityItemInfoFields(a,b,c)
#define DataBrowserAccessibilityItemInfo_sizeof() 0
#endif

#ifndef NO_DataBrowserCallbacks
void cacheDataBrowserCallbacksFields(JNIEnv *env, jobject lpObject);
DataBrowserCallbacks *getDataBrowserCallbacksFields(JNIEnv *env, jobject lpObject, DataBrowserCallbacks *lpStruct);
void setDataBrowserCallbacksFields(JNIEnv *env, jobject lpObject, DataBrowserCallbacks *lpStruct);
#define DataBrowserCallbacks_sizeof() sizeof(DataBrowserCallbacks)
#else
#define cacheDataBrowserCallbacksFields(a,b)
#define getDataBrowserCallbacksFields(a,b,c) NULL
#define setDataBrowserCallbacksFields(a,b,c)
#define DataBrowserCallbacks_sizeof() 0
#endif

#ifndef NO_DataBrowserCustomCallbacks
void cacheDataBrowserCustomCallbacksFields(JNIEnv *env, jobject lpObject);
DataBrowserCustomCallbacks *getDataBrowserCustomCallbacksFields(JNIEnv *env, jobject lpObject, DataBrowserCustomCallbacks *lpStruct);
void setDataBrowserCustomCallbacksFields(JNIEnv *env, jobject lpObject, DataBrowserCustomCallbacks *lpStruct);
#define DataBrowserCustomCallbacks_sizeof() sizeof(DataBrowserCustomCallbacks)
#else
#define cacheDataBrowserCustomCallbacksFields(a,b)
#define getDataBrowserCustomCallbacksFields(a,b,c) NULL
#define setDataBrowserCustomCallbacksFields(a,b,c)
#define DataBrowserCustomCallbacks_sizeof() 0
#endif

#ifndef NO_DataBrowserListViewColumnDesc
void cacheDataBrowserListViewColumnDescFields(JNIEnv *env, jobject lpObject);
DataBrowserListViewColumnDesc *getDataBrowserListViewColumnDescFields(JNIEnv *env, jobject lpObject, DataBrowserListViewColumnDesc *lpStruct);
void setDataBrowserListViewColumnDescFields(JNIEnv *env, jobject lpObject, DataBrowserListViewColumnDesc *lpStruct);
#define DataBrowserListViewColumnDesc_sizeof() sizeof(DataBrowserListViewColumnDesc)
#else
#define cacheDataBrowserListViewColumnDescFields(a,b)
#define getDataBrowserListViewColumnDescFields(a,b,c) NULL
#define setDataBrowserListViewColumnDescFields(a,b,c)
#define DataBrowserListViewColumnDesc_sizeof() 0
#endif

#ifndef NO_DataBrowserListViewHeaderDesc
void cacheDataBrowserListViewHeaderDescFields(JNIEnv *env, jobject lpObject);
DataBrowserListViewHeaderDesc *getDataBrowserListViewHeaderDescFields(JNIEnv *env, jobject lpObject, DataBrowserListViewHeaderDesc *lpStruct);
void setDataBrowserListViewHeaderDescFields(JNIEnv *env, jobject lpObject, DataBrowserListViewHeaderDesc *lpStruct);
#define DataBrowserListViewHeaderDesc_sizeof() sizeof(DataBrowserListViewHeaderDesc)
#else
#define cacheDataBrowserListViewHeaderDescFields(a,b)
#define getDataBrowserListViewHeaderDescFields(a,b,c) NULL
#define setDataBrowserListViewHeaderDescFields(a,b,c)
#define DataBrowserListViewHeaderDesc_sizeof() 0
#endif

#ifndef NO_EventRecord
void cacheEventRecordFields(JNIEnv *env, jobject lpObject);
EventRecord *getEventRecordFields(JNIEnv *env, jobject lpObject, EventRecord *lpStruct);
void setEventRecordFields(JNIEnv *env, jobject lpObject, EventRecord *lpStruct);
#define EventRecord_sizeof() sizeof(EventRecord)
#else
#define cacheEventRecordFields(a,b)
#define getEventRecordFields(a,b,c) NULL
#define setEventRecordFields(a,b,c)
#define EventRecord_sizeof() 0
#endif

#ifndef NO_GDevice
void cacheGDeviceFields(JNIEnv *env, jobject lpObject);
GDevice *getGDeviceFields(JNIEnv *env, jobject lpObject, GDevice *lpStruct);
void setGDeviceFields(JNIEnv *env, jobject lpObject, GDevice *lpStruct);
#define GDevice_sizeof() sizeof(GDevice)
#else
#define cacheGDeviceFields(a,b)
#define getGDeviceFields(a,b,c) NULL
#define setGDeviceFields(a,b,c)
#define GDevice_sizeof() 0
#endif

#ifndef NO_HIAxisPosition
void cacheHIAxisPositionFields(JNIEnv *env, jobject lpObject);
HIAxisPosition *getHIAxisPositionFields(JNIEnv *env, jobject lpObject, HIAxisPosition *lpStruct);
void setHIAxisPositionFields(JNIEnv *env, jobject lpObject, HIAxisPosition *lpStruct);
#define HIAxisPosition_sizeof() sizeof(HIAxisPosition)
#else
#define cacheHIAxisPositionFields(a,b)
#define getHIAxisPositionFields(a,b,c) NULL
#define setHIAxisPositionFields(a,b,c)
#define HIAxisPosition_sizeof() 0
#endif

#ifndef NO_HIAxisScale
void cacheHIAxisScaleFields(JNIEnv *env, jobject lpObject);
HIAxisScale *getHIAxisScaleFields(JNIEnv *env, jobject lpObject, HIAxisScale *lpStruct);
void setHIAxisScaleFields(JNIEnv *env, jobject lpObject, HIAxisScale *lpStruct);
#define HIAxisScale_sizeof() sizeof(HIAxisScale)
#else
#define cacheHIAxisScaleFields(a,b)
#define getHIAxisScaleFields(a,b,c) NULL
#define setHIAxisScaleFields(a,b,c)
#define HIAxisScale_sizeof() 0
#endif

#ifndef NO_HIBinding
void cacheHIBindingFields(JNIEnv *env, jobject lpObject);
HIBinding *getHIBindingFields(JNIEnv *env, jobject lpObject, HIBinding *lpStruct);
void setHIBindingFields(JNIEnv *env, jobject lpObject, HIBinding *lpStruct);
#define HIBinding_sizeof() sizeof(HIBinding)
#else
#define cacheHIBindingFields(a,b)
#define getHIBindingFields(a,b,c) NULL
#define setHIBindingFields(a,b,c)
#define HIBinding_sizeof() 0
#endif

#ifndef NO_HICommand
void cacheHICommandFields(JNIEnv *env, jobject lpObject);
HICommand *getHICommandFields(JNIEnv *env, jobject lpObject, HICommand *lpStruct);
void setHICommandFields(JNIEnv *env, jobject lpObject, HICommand *lpStruct);
#define HICommand_sizeof() sizeof(HICommand)
#else
#define cacheHICommandFields(a,b)
#define getHICommandFields(a,b,c) NULL
#define setHICommandFields(a,b,c)
#define HICommand_sizeof() 0
#endif

#ifndef NO_HILayoutInfo
void cacheHILayoutInfoFields(JNIEnv *env, jobject lpObject);
HILayoutInfo *getHILayoutInfoFields(JNIEnv *env, jobject lpObject, HILayoutInfo *lpStruct);
void setHILayoutInfoFields(JNIEnv *env, jobject lpObject, HILayoutInfo *lpStruct);
#define HILayoutInfo_sizeof() sizeof(HILayoutInfo)
#else
#define cacheHILayoutInfoFields(a,b)
#define getHILayoutInfoFields(a,b,c) NULL
#define setHILayoutInfoFields(a,b,c)
#define HILayoutInfo_sizeof() 0
#endif

#ifndef NO_HIPositioning
void cacheHIPositioningFields(JNIEnv *env, jobject lpObject);
HIPositioning *getHIPositioningFields(JNIEnv *env, jobject lpObject, HIPositioning *lpStruct);
void setHIPositioningFields(JNIEnv *env, jobject lpObject, HIPositioning *lpStruct);
#define HIPositioning_sizeof() sizeof(HIPositioning)
#else
#define cacheHIPositioningFields(a,b)
#define getHIPositioningFields(a,b,c) NULL
#define setHIPositioningFields(a,b,c)
#define HIPositioning_sizeof() 0
#endif

#ifndef NO_HIScaling
void cacheHIScalingFields(JNIEnv *env, jobject lpObject);
HIScaling *getHIScalingFields(JNIEnv *env, jobject lpObject, HIScaling *lpStruct);
void setHIScalingFields(JNIEnv *env, jobject lpObject, HIScaling *lpStruct);
#define HIScaling_sizeof() sizeof(HIScaling)
#else
#define cacheHIScalingFields(a,b)
#define getHIScalingFields(a,b,c) NULL
#define setHIScalingFields(a,b,c)
#define HIScaling_sizeof() 0
#endif

#ifndef NO_HIScrollBarTrackInfo
void cacheHIScrollBarTrackInfoFields(JNIEnv *env, jobject lpObject);
HIScrollBarTrackInfo *getHIScrollBarTrackInfoFields(JNIEnv *env, jobject lpObject, HIScrollBarTrackInfo *lpStruct);
void setHIScrollBarTrackInfoFields(JNIEnv *env, jobject lpObject, HIScrollBarTrackInfo *lpStruct);
#define HIScrollBarTrackInfo_sizeof() sizeof(HIScrollBarTrackInfo)
#else
#define cacheHIScrollBarTrackInfoFields(a,b)
#define getHIScrollBarTrackInfoFields(a,b,c) NULL
#define setHIScrollBarTrackInfoFields(a,b,c)
#define HIScrollBarTrackInfo_sizeof() 0
#endif

#ifndef NO_HISideBinding
void cacheHISideBindingFields(JNIEnv *env, jobject lpObject);
HISideBinding *getHISideBindingFields(JNIEnv *env, jobject lpObject, HISideBinding *lpStruct);
void setHISideBindingFields(JNIEnv *env, jobject lpObject, HISideBinding *lpStruct);
#define HISideBinding_sizeof() sizeof(HISideBinding)
#else
#define cacheHISideBindingFields(a,b)
#define getHISideBindingFields(a,b,c) NULL
#define setHISideBindingFields(a,b,c)
#define HISideBinding_sizeof() 0
#endif

#ifndef NO_HIThemeAnimationFrameInfo
void cacheHIThemeAnimationFrameInfoFields(JNIEnv *env, jobject lpObject);
HIThemeAnimationFrameInfo *getHIThemeAnimationFrameInfoFields(JNIEnv *env, jobject lpObject, HIThemeAnimationFrameInfo *lpStruct);
void setHIThemeAnimationFrameInfoFields(JNIEnv *env, jobject lpObject, HIThemeAnimationFrameInfo *lpStruct);
#define HIThemeAnimationFrameInfo_sizeof() sizeof(HIThemeAnimationFrameInfo)
#else
#define cacheHIThemeAnimationFrameInfoFields(a,b)
#define getHIThemeAnimationFrameInfoFields(a,b,c) NULL
#define setHIThemeAnimationFrameInfoFields(a,b,c)
#define HIThemeAnimationFrameInfo_sizeof() 0
#endif

#ifndef NO_HIThemeAnimationTimeInfo
void cacheHIThemeAnimationTimeInfoFields(JNIEnv *env, jobject lpObject);
HIThemeAnimationTimeInfo *getHIThemeAnimationTimeInfoFields(JNIEnv *env, jobject lpObject, HIThemeAnimationTimeInfo *lpStruct);
void setHIThemeAnimationTimeInfoFields(JNIEnv *env, jobject lpObject, HIThemeAnimationTimeInfo *lpStruct);
#define HIThemeAnimationTimeInfo_sizeof() sizeof(HIThemeAnimationTimeInfo)
#else
#define cacheHIThemeAnimationTimeInfoFields(a,b)
#define getHIThemeAnimationTimeInfoFields(a,b,c) NULL
#define setHIThemeAnimationTimeInfoFields(a,b,c)
#define HIThemeAnimationTimeInfo_sizeof() 0
#endif

#ifndef NO_HIThemeBackgroundDrawInfo
void cacheHIThemeBackgroundDrawInfoFields(JNIEnv *env, jobject lpObject);
HIThemeBackgroundDrawInfo *getHIThemeBackgroundDrawInfoFields(JNIEnv *env, jobject lpObject, HIThemeBackgroundDrawInfo *lpStruct);
void setHIThemeBackgroundDrawInfoFields(JNIEnv *env, jobject lpObject, HIThemeBackgroundDrawInfo *lpStruct);
#define HIThemeBackgroundDrawInfo_sizeof() sizeof(HIThemeBackgroundDrawInfo)
#else
#define cacheHIThemeBackgroundDrawInfoFields(a,b)
#define getHIThemeBackgroundDrawInfoFields(a,b,c) NULL
#define setHIThemeBackgroundDrawInfoFields(a,b,c)
#define HIThemeBackgroundDrawInfo_sizeof() 0
#endif

#ifndef NO_HIThemeButtonDrawInfo
void cacheHIThemeButtonDrawInfoFields(JNIEnv *env, jobject lpObject);
HIThemeButtonDrawInfo *getHIThemeButtonDrawInfoFields(JNIEnv *env, jobject lpObject, HIThemeButtonDrawInfo *lpStruct);
void setHIThemeButtonDrawInfoFields(JNIEnv *env, jobject lpObject, HIThemeButtonDrawInfo *lpStruct);
#define HIThemeButtonDrawInfo_sizeof() sizeof(HIThemeButtonDrawInfo)
#else
#define cacheHIThemeButtonDrawInfoFields(a,b)
#define getHIThemeButtonDrawInfoFields(a,b,c) NULL
#define setHIThemeButtonDrawInfoFields(a,b,c)
#define HIThemeButtonDrawInfo_sizeof() 0
#endif

#ifndef NO_HIThemeFrameDrawInfo
void cacheHIThemeFrameDrawInfoFields(JNIEnv *env, jobject lpObject);
HIThemeFrameDrawInfo *getHIThemeFrameDrawInfoFields(JNIEnv *env, jobject lpObject, HIThemeFrameDrawInfo *lpStruct);
void setHIThemeFrameDrawInfoFields(JNIEnv *env, jobject lpObject, HIThemeFrameDrawInfo *lpStruct);
#define HIThemeFrameDrawInfo_sizeof() sizeof(HIThemeFrameDrawInfo)
#else
#define cacheHIThemeFrameDrawInfoFields(a,b)
#define getHIThemeFrameDrawInfoFields(a,b,c) NULL
#define setHIThemeFrameDrawInfoFields(a,b,c)
#define HIThemeFrameDrawInfo_sizeof() 0
#endif

#ifndef NO_HIThemeGroupBoxDrawInfo
void cacheHIThemeGroupBoxDrawInfoFields(JNIEnv *env, jobject lpObject);
HIThemeGroupBoxDrawInfo *getHIThemeGroupBoxDrawInfoFields(JNIEnv *env, jobject lpObject, HIThemeGroupBoxDrawInfo *lpStruct);
void setHIThemeGroupBoxDrawInfoFields(JNIEnv *env, jobject lpObject, HIThemeGroupBoxDrawInfo *lpStruct);
#define HIThemeGroupBoxDrawInfo_sizeof() sizeof(HIThemeGroupBoxDrawInfo)
#else
#define cacheHIThemeGroupBoxDrawInfoFields(a,b)
#define getHIThemeGroupBoxDrawInfoFields(a,b,c) NULL
#define setHIThemeGroupBoxDrawInfoFields(a,b,c)
#define HIThemeGroupBoxDrawInfo_sizeof() 0
#endif

#ifndef NO_HIThemeGrowBoxDrawInfo
void cacheHIThemeGrowBoxDrawInfoFields(JNIEnv *env, jobject lpObject);
HIThemeGrowBoxDrawInfo *getHIThemeGrowBoxDrawInfoFields(JNIEnv *env, jobject lpObject, HIThemeGrowBoxDrawInfo *lpStruct);
void setHIThemeGrowBoxDrawInfoFields(JNIEnv *env, jobject lpObject, HIThemeGrowBoxDrawInfo *lpStruct);
#define HIThemeGrowBoxDrawInfo_sizeof() sizeof(HIThemeGrowBoxDrawInfo)
#else
#define cacheHIThemeGrowBoxDrawInfoFields(a,b)
#define getHIThemeGrowBoxDrawInfoFields(a,b,c) NULL
#define setHIThemeGrowBoxDrawInfoFields(a,b,c)
#define HIThemeGrowBoxDrawInfo_sizeof() 0
#endif

#ifndef NO_HIThemePopupArrowDrawInfo
void cacheHIThemePopupArrowDrawInfoFields(JNIEnv *env, jobject lpObject);
HIThemePopupArrowDrawInfo *getHIThemePopupArrowDrawInfoFields(JNIEnv *env, jobject lpObject, HIThemePopupArrowDrawInfo *lpStruct);
void setHIThemePopupArrowDrawInfoFields(JNIEnv *env, jobject lpObject, HIThemePopupArrowDrawInfo *lpStruct);
#define HIThemePopupArrowDrawInfo_sizeof() sizeof(HIThemePopupArrowDrawInfo)
#else
#define cacheHIThemePopupArrowDrawInfoFields(a,b)
#define getHIThemePopupArrowDrawInfoFields(a,b,c) NULL
#define setHIThemePopupArrowDrawInfoFields(a,b,c)
#define HIThemePopupArrowDrawInfo_sizeof() 0
#endif

#ifndef NO_HIThemeSeparatorDrawInfo
void cacheHIThemeSeparatorDrawInfoFields(JNIEnv *env, jobject lpObject);
HIThemeSeparatorDrawInfo *getHIThemeSeparatorDrawInfoFields(JNIEnv *env, jobject lpObject, HIThemeSeparatorDrawInfo *lpStruct);
void setHIThemeSeparatorDrawInfoFields(JNIEnv *env, jobject lpObject, HIThemeSeparatorDrawInfo *lpStruct);
#define HIThemeSeparatorDrawInfo_sizeof() sizeof(HIThemeSeparatorDrawInfo)
#else
#define cacheHIThemeSeparatorDrawInfoFields(a,b)
#define getHIThemeSeparatorDrawInfoFields(a,b,c) NULL
#define setHIThemeSeparatorDrawInfoFields(a,b,c)
#define HIThemeSeparatorDrawInfo_sizeof() 0
#endif

#ifndef NO_HIThemeTabDrawInfo
void cacheHIThemeTabDrawInfoFields(JNIEnv *env, jobject lpObject);
HIThemeTabDrawInfo *getHIThemeTabDrawInfoFields(JNIEnv *env, jobject lpObject, HIThemeTabDrawInfo *lpStruct);
void setHIThemeTabDrawInfoFields(JNIEnv *env, jobject lpObject, HIThemeTabDrawInfo *lpStruct);
#define HIThemeTabDrawInfo_sizeof() sizeof(HIThemeTabDrawInfo)
#else
#define cacheHIThemeTabDrawInfoFields(a,b)
#define getHIThemeTabDrawInfoFields(a,b,c) NULL
#define setHIThemeTabDrawInfoFields(a,b,c)
#define HIThemeTabDrawInfo_sizeof() 0
#endif

#ifndef NO_HIThemeTabPaneDrawInfo
void cacheHIThemeTabPaneDrawInfoFields(JNIEnv *env, jobject lpObject);
HIThemeTabPaneDrawInfo *getHIThemeTabPaneDrawInfoFields(JNIEnv *env, jobject lpObject, HIThemeTabPaneDrawInfo *lpStruct);
void setHIThemeTabPaneDrawInfoFields(JNIEnv *env, jobject lpObject, HIThemeTabPaneDrawInfo *lpStruct);
#define HIThemeTabPaneDrawInfo_sizeof() sizeof(HIThemeTabPaneDrawInfo)
#else
#define cacheHIThemeTabPaneDrawInfoFields(a,b)
#define getHIThemeTabPaneDrawInfoFields(a,b,c) NULL
#define setHIThemeTabPaneDrawInfoFields(a,b,c)
#define HIThemeTabPaneDrawInfo_sizeof() 0
#endif

#ifndef NO_HIThemeTextInfo
void cacheHIThemeTextInfoFields(JNIEnv *env, jobject lpObject);
HIThemeTextInfo *getHIThemeTextInfoFields(JNIEnv *env, jobject lpObject, HIThemeTextInfo *lpStruct);
void setHIThemeTextInfoFields(JNIEnv *env, jobject lpObject, HIThemeTextInfo *lpStruct);
#define HIThemeTextInfo_sizeof() sizeof(HIThemeTextInfo)
#else
#define cacheHIThemeTextInfoFields(a,b)
#define getHIThemeTextInfoFields(a,b,c) NULL
#define setHIThemeTextInfoFields(a,b,c)
#define HIThemeTextInfo_sizeof() 0
#endif

#ifndef NO_HIThemeTrackDrawInfo
void cacheHIThemeTrackDrawInfoFields(JNIEnv *env, jobject lpObject);
HIThemeTrackDrawInfo *getHIThemeTrackDrawInfoFields(JNIEnv *env, jobject lpObject, HIThemeTrackDrawInfo *lpStruct);
void setHIThemeTrackDrawInfoFields(JNIEnv *env, jobject lpObject, HIThemeTrackDrawInfo *lpStruct);
#define HIThemeTrackDrawInfo_sizeof() sizeof(HIThemeTrackDrawInfo)
#else
#define cacheHIThemeTrackDrawInfoFields(a,b)
#define getHIThemeTrackDrawInfoFields(a,b,c) NULL
#define setHIThemeTrackDrawInfoFields(a,b,c)
#define HIThemeTrackDrawInfo_sizeof() 0
#endif

#ifndef NO_HMHelpContentRec
void cacheHMHelpContentRecFields(JNIEnv *env, jobject lpObject);
HMHelpContentRec *getHMHelpContentRecFields(JNIEnv *env, jobject lpObject, HMHelpContentRec *lpStruct);
void setHMHelpContentRecFields(JNIEnv *env, jobject lpObject, HMHelpContentRec *lpStruct);
#define HMHelpContentRec_sizeof() sizeof(HMHelpContentRec)
#else
#define cacheHMHelpContentRecFields(a,b)
#define getHMHelpContentRecFields(a,b,c) NULL
#define setHMHelpContentRecFields(a,b,c)
#define HMHelpContentRec_sizeof() 0
#endif

#ifndef NO_LSApplicationParameters
void cacheLSApplicationParametersFields(JNIEnv *env, jobject lpObject);
LSApplicationParameters *getLSApplicationParametersFields(JNIEnv *env, jobject lpObject, LSApplicationParameters *lpStruct);
void setLSApplicationParametersFields(JNIEnv *env, jobject lpObject, LSApplicationParameters *lpStruct);
#define LSApplicationParameters_sizeof() sizeof(LSApplicationParameters)
#else
#define cacheLSApplicationParametersFields(a,b)
#define getLSApplicationParametersFields(a,b,c) NULL
#define setLSApplicationParametersFields(a,b,c)
#define LSApplicationParameters_sizeof() 0
#endif

#ifndef NO_LongDateRec
void cacheLongDateRecFields(JNIEnv *env, jobject lpObject);
LongDateRec *getLongDateRecFields(JNIEnv *env, jobject lpObject, LongDateRec *lpStruct);
void setLongDateRecFields(JNIEnv *env, jobject lpObject, LongDateRec *lpStruct);
#define LongDateRec_sizeof() sizeof(LongDateRec)
#else
#define cacheLongDateRecFields(a,b)
#define getLongDateRecFields(a,b,c) NULL
#define setLongDateRecFields(a,b,c)
#define LongDateRec_sizeof() 0
#endif

#ifndef NO_MenuTrackingData
void cacheMenuTrackingDataFields(JNIEnv *env, jobject lpObject);
MenuTrackingData *getMenuTrackingDataFields(JNIEnv *env, jobject lpObject, MenuTrackingData *lpStruct);
void setMenuTrackingDataFields(JNIEnv *env, jobject lpObject, MenuTrackingData *lpStruct);
#define MenuTrackingData_sizeof() sizeof(MenuTrackingData)
#else
#define cacheMenuTrackingDataFields(a,b)
#define getMenuTrackingDataFields(a,b,c) NULL
#define setMenuTrackingDataFields(a,b,c)
#define MenuTrackingData_sizeof() 0
#endif

#ifndef NO_NavCBRec
void cacheNavCBRecFields(JNIEnv *env, jobject lpObject);
NavCBRec *getNavCBRecFields(JNIEnv *env, jobject lpObject, NavCBRec *lpStruct);
void setNavCBRecFields(JNIEnv *env, jobject lpObject, NavCBRec *lpStruct);
#define NavCBRec_sizeof() sizeof(NavCBRec)
#else
#define cacheNavCBRecFields(a,b)
#define getNavCBRecFields(a,b,c) NULL
#define setNavCBRecFields(a,b,c)
#define NavCBRec_sizeof() 0
#endif

#ifndef NO_NavDialogCreationOptions
void cacheNavDialogCreationOptionsFields(JNIEnv *env, jobject lpObject);
NavDialogCreationOptions *getNavDialogCreationOptionsFields(JNIEnv *env, jobject lpObject, NavDialogCreationOptions *lpStruct);
void setNavDialogCreationOptionsFields(JNIEnv *env, jobject lpObject, NavDialogCreationOptions *lpStruct);
#define NavDialogCreationOptions_sizeof() sizeof(NavDialogCreationOptions)
#else
#define cacheNavDialogCreationOptionsFields(a,b)
#define getNavDialogCreationOptionsFields(a,b,c) NULL
#define setNavDialogCreationOptionsFields(a,b,c)
#define NavDialogCreationOptions_sizeof() 0
#endif

#ifndef NO_NavEventData
void cacheNavEventDataFields(JNIEnv *env, jobject lpObject);
NavEventData *getNavEventDataFields(JNIEnv *env, jobject lpObject, NavEventData *lpStruct);
void setNavEventDataFields(JNIEnv *env, jobject lpObject, NavEventData *lpStruct);
#define NavEventData_sizeof() sizeof(NavEventData)
#else
#define cacheNavEventDataFields(a,b)
#define getNavEventDataFields(a,b,c) NULL
#define setNavEventDataFields(a,b,c)
#define NavEventData_sizeof() 0
#endif

#ifndef NO_NavEventDataInfo
void cacheNavEventDataInfoFields(JNIEnv *env, jobject lpObject);
NavEventDataInfo *getNavEventDataInfoFields(JNIEnv *env, jobject lpObject, NavEventDataInfo *lpStruct);
void setNavEventDataInfoFields(JNIEnv *env, jobject lpObject, NavEventDataInfo *lpStruct);
#define NavEventDataInfo_sizeof() sizeof(NavEventDataInfo)
#else
#define cacheNavEventDataInfoFields(a,b)
#define getNavEventDataInfoFields(a,b,c) NULL
#define setNavEventDataInfoFields(a,b,c)
#define NavEventDataInfo_sizeof() 0
#endif

#ifndef NO_NavFileOrFolderInfo
void cacheNavFileOrFolderInfoFields(JNIEnv *env, jobject lpObject);
NavFileOrFolderInfo *getNavFileOrFolderInfoFields(JNIEnv *env, jobject lpObject, NavFileOrFolderInfo *lpStruct);
void setNavFileOrFolderInfoFields(JNIEnv *env, jobject lpObject, NavFileOrFolderInfo *lpStruct);
#define NavFileOrFolderInfo_sizeof() sizeof(NavFileOrFolderInfo)
#else
#define cacheNavFileOrFolderInfoFields(a,b)
#define getNavFileOrFolderInfoFields(a,b,c) NULL
#define setNavFileOrFolderInfoFields(a,b,c)
#define NavFileOrFolderInfo_sizeof() 0
#endif

#ifndef NO_NavMenuItemSpec
void cacheNavMenuItemSpecFields(JNIEnv *env, jobject lpObject);
NavMenuItemSpec *getNavMenuItemSpecFields(JNIEnv *env, jobject lpObject, NavMenuItemSpec *lpStruct);
void setNavMenuItemSpecFields(JNIEnv *env, jobject lpObject, NavMenuItemSpec *lpStruct);
#define NavMenuItemSpec_sizeof() sizeof(NavMenuItemSpec)
#else
#define cacheNavMenuItemSpecFields(a,b)
#define getNavMenuItemSpecFields(a,b,c) NULL
#define setNavMenuItemSpecFields(a,b,c)
#define NavMenuItemSpec_sizeof() 0
#endif

#ifndef NO_NavReplyRecord
void cacheNavReplyRecordFields(JNIEnv *env, jobject lpObject);
NavReplyRecord *getNavReplyRecordFields(JNIEnv *env, jobject lpObject, NavReplyRecord *lpStruct);
void setNavReplyRecordFields(JNIEnv *env, jobject lpObject, NavReplyRecord *lpStruct);
#define NavReplyRecord_sizeof() sizeof(NavReplyRecord)
#else
#define cacheNavReplyRecordFields(a,b)
#define getNavReplyRecordFields(a,b,c) NULL
#define setNavReplyRecordFields(a,b,c)
#define NavReplyRecord_sizeof() 0
#endif

#ifndef NO_PMRect
void cachePMRectFields(JNIEnv *env, jobject lpObject);
PMRect *getPMRectFields(JNIEnv *env, jobject lpObject, PMRect *lpStruct);
void setPMRectFields(JNIEnv *env, jobject lpObject, PMRect *lpStruct);
#define PMRect_sizeof() sizeof(PMRect)
#else
#define cachePMRectFields(a,b)
#define getPMRectFields(a,b,c) NULL
#define setPMRectFields(a,b,c)
#define PMRect_sizeof() 0
#endif

#ifndef NO_PMResolution
void cachePMResolutionFields(JNIEnv *env, jobject lpObject);
PMResolution *getPMResolutionFields(JNIEnv *env, jobject lpObject, PMResolution *lpStruct);
void setPMResolutionFields(JNIEnv *env, jobject lpObject, PMResolution *lpStruct);
#define PMResolution_sizeof() sizeof(PMResolution)
#else
#define cachePMResolutionFields(a,b)
#define getPMResolutionFields(a,b,c) NULL
#define setPMResolutionFields(a,b,c)
#define PMResolution_sizeof() 0
#endif

#ifndef NO_PixMap
void cachePixMapFields(JNIEnv *env, jobject lpObject);
PixMap *getPixMapFields(JNIEnv *env, jobject lpObject, PixMap *lpStruct);
void setPixMapFields(JNIEnv *env, jobject lpObject, PixMap *lpStruct);
#define PixMap_sizeof() sizeof(PixMap)
#else
#define cachePixMapFields(a,b)
#define getPixMapFields(a,b,c) NULL
#define setPixMapFields(a,b,c)
#define PixMap_sizeof() 0
#endif

#ifndef NO_Point
void cachePointFields(JNIEnv *env, jobject lpObject);
Point *getPointFields(JNIEnv *env, jobject lpObject, Point *lpStruct);
void setPointFields(JNIEnv *env, jobject lpObject, Point *lpStruct);
#define Point_sizeof() sizeof(Point)
#else
#define cachePointFields(a,b)
#define getPointFields(a,b,c) NULL
#define setPointFields(a,b,c)
#define Point_sizeof() 0
#endif

#ifndef NO_ProgressTrackInfo
void cacheProgressTrackInfoFields(JNIEnv *env, jobject lpObject);
ProgressTrackInfo *getProgressTrackInfoFields(JNIEnv *env, jobject lpObject, ProgressTrackInfo *lpStruct);
void setProgressTrackInfoFields(JNIEnv *env, jobject lpObject, ProgressTrackInfo *lpStruct);
#define ProgressTrackInfo_sizeof() sizeof(ProgressTrackInfo)
#else
#define cacheProgressTrackInfoFields(a,b)
#define getProgressTrackInfoFields(a,b,c) NULL
#define setProgressTrackInfoFields(a,b,c)
#define ProgressTrackInfo_sizeof() 0
#endif

#ifndef NO_RGBColor
void cacheRGBColorFields(JNIEnv *env, jobject lpObject);
RGBColor *getRGBColorFields(JNIEnv *env, jobject lpObject, RGBColor *lpStruct);
void setRGBColorFields(JNIEnv *env, jobject lpObject, RGBColor *lpStruct);
#define RGBColor_sizeof() sizeof(RGBColor)
#else
#define cacheRGBColorFields(a,b)
#define getRGBColorFields(a,b,c) NULL
#define setRGBColorFields(a,b,c)
#define RGBColor_sizeof() 0
#endif

#ifndef NO_Rect
void cacheRectFields(JNIEnv *env, jobject lpObject);
Rect *getRectFields(JNIEnv *env, jobject lpObject, Rect *lpStruct);
void setRectFields(JNIEnv *env, jobject lpObject, Rect *lpStruct);
#define Rect_sizeof() sizeof(Rect)
#else
#define cacheRectFields(a,b)
#define getRectFields(a,b,c) NULL
#define setRectFields(a,b,c)
#define Rect_sizeof() 0
#endif

#ifndef NO_ScrollBarTrackInfo
void cacheScrollBarTrackInfoFields(JNIEnv *env, jobject lpObject);
ScrollBarTrackInfo *getScrollBarTrackInfoFields(JNIEnv *env, jobject lpObject, ScrollBarTrackInfo *lpStruct);
void setScrollBarTrackInfoFields(JNIEnv *env, jobject lpObject, ScrollBarTrackInfo *lpStruct);
#define ScrollBarTrackInfo_sizeof() sizeof(ScrollBarTrackInfo)
#else
#define cacheScrollBarTrackInfoFields(a,b)
#define getScrollBarTrackInfoFields(a,b,c) NULL
#define setScrollBarTrackInfoFields(a,b,c)
#define ScrollBarTrackInfo_sizeof() 0
#endif

#ifndef NO_SliderTrackInfo
void cacheSliderTrackInfoFields(JNIEnv *env, jobject lpObject);
SliderTrackInfo *getSliderTrackInfoFields(JNIEnv *env, jobject lpObject, SliderTrackInfo *lpStruct);
void setSliderTrackInfoFields(JNIEnv *env, jobject lpObject, SliderTrackInfo *lpStruct);
#define SliderTrackInfo_sizeof() sizeof(SliderTrackInfo)
#else
#define cacheSliderTrackInfoFields(a,b)
#define getSliderTrackInfoFields(a,b,c) NULL
#define setSliderTrackInfoFields(a,b,c)
#define SliderTrackInfo_sizeof() 0
#endif

#ifndef NO_TXNBackground
void cacheTXNBackgroundFields(JNIEnv *env, jobject lpObject);
TXNBackground *getTXNBackgroundFields(JNIEnv *env, jobject lpObject, TXNBackground *lpStruct);
void setTXNBackgroundFields(JNIEnv *env, jobject lpObject, TXNBackground *lpStruct);
#define TXNBackground_sizeof() sizeof(TXNBackground)
#else
#define cacheTXNBackgroundFields(a,b)
#define getTXNBackgroundFields(a,b,c) NULL
#define setTXNBackgroundFields(a,b,c)
#define TXNBackground_sizeof() 0
#endif

#ifndef NO_TXNTab
void cacheTXNTabFields(JNIEnv *env, jobject lpObject);
TXNTab *getTXNTabFields(JNIEnv *env, jobject lpObject, TXNTab *lpStruct);
void setTXNTabFields(JNIEnv *env, jobject lpObject, TXNTab *lpStruct);
#define TXNTab_sizeof() sizeof(TXNTab)
#else
#define cacheTXNTabFields(a,b)
#define getTXNTabFields(a,b,c) NULL
#define setTXNTabFields(a,b,c)
#define TXNTab_sizeof() 0
#endif

#ifndef NO_TextRange
void cacheTextRangeFields(JNIEnv *env, jobject lpObject);
TextRange *getTextRangeFields(JNIEnv *env, jobject lpObject, TextRange *lpStruct);
void setTextRangeFields(JNIEnv *env, jobject lpObject, TextRange *lpStruct);
#define TextRange_sizeof() sizeof(TextRange)
#else
#define cacheTextRangeFields(a,b)
#define getTextRangeFields(a,b,c) NULL
#define setTextRangeFields(a,b,c)
#define TextRange_sizeof() 0
#endif

#ifndef NO_ThemeButtonDrawInfo
void cacheThemeButtonDrawInfoFields(JNIEnv *env, jobject lpObject);
ThemeButtonDrawInfo *getThemeButtonDrawInfoFields(JNIEnv *env, jobject lpObject, ThemeButtonDrawInfo *lpStruct);
void setThemeButtonDrawInfoFields(JNIEnv *env, jobject lpObject, ThemeButtonDrawInfo *lpStruct);
#define ThemeButtonDrawInfo_sizeof() sizeof(ThemeButtonDrawInfo)
#else
#define cacheThemeButtonDrawInfoFields(a,b)
#define getThemeButtonDrawInfoFields(a,b,c) NULL
#define setThemeButtonDrawInfoFields(a,b,c)
#define ThemeButtonDrawInfo_sizeof() 0
#endif

