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

#include "swt.h"
#include "os_structs.h"

#ifndef NO_AEDesc
typedef struct AEDesc_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID descriptorType, dataHandle;
} AEDesc_FID_CACHE;

AEDesc_FID_CACHE AEDescFc;

void cacheAEDescFields(JNIEnv *env, jobject lpObject)
{
	if (AEDescFc.cached) return;
	AEDescFc.clazz = (*env)->GetObjectClass(env, lpObject);
	AEDescFc.descriptorType = (*env)->GetFieldID(env, AEDescFc.clazz, "descriptorType", "I");
	AEDescFc.dataHandle = (*env)->GetFieldID(env, AEDescFc.clazz, "dataHandle", "I");
	AEDescFc.cached = 1;
}

AEDesc *getAEDescFields(JNIEnv *env, jobject lpObject, AEDesc *lpStruct)
{
	if (!AEDescFc.cached) cacheAEDescFields(env, lpObject);
	lpStruct->descriptorType = (DescType)(*env)->GetIntField(env, lpObject, AEDescFc.descriptorType);
	lpStruct->dataHandle = (AEDataStorage)(*env)->GetIntField(env, lpObject, AEDescFc.dataHandle);
	return lpStruct;
}

void setAEDescFields(JNIEnv *env, jobject lpObject, AEDesc *lpStruct)
{
	if (!AEDescFc.cached) cacheAEDescFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, AEDescFc.descriptorType, (jint)lpStruct->descriptorType);
	(*env)->SetIntField(env, lpObject, AEDescFc.dataHandle, (jint)lpStruct->dataHandle);
}
#endif

#ifndef NO_ATSFontMetrics
typedef struct ATSFontMetrics_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID version, ascent, descent, leading, avgAdvanceWidth, maxAdvanceWidth, minLeftSideBearing, minRightSideBearing, stemWidth, stemHeight, capHeight, xHeight, italicAngle, underlinePosition, underlineThickness;
} ATSFontMetrics_FID_CACHE;

ATSFontMetrics_FID_CACHE ATSFontMetricsFc;

void cacheATSFontMetricsFields(JNIEnv *env, jobject lpObject)
{
	if (ATSFontMetricsFc.cached) return;
	ATSFontMetricsFc.clazz = (*env)->GetObjectClass(env, lpObject);
	ATSFontMetricsFc.version = (*env)->GetFieldID(env, ATSFontMetricsFc.clazz, "version", "I");
	ATSFontMetricsFc.ascent = (*env)->GetFieldID(env, ATSFontMetricsFc.clazz, "ascent", "F");
	ATSFontMetricsFc.descent = (*env)->GetFieldID(env, ATSFontMetricsFc.clazz, "descent", "F");
	ATSFontMetricsFc.leading = (*env)->GetFieldID(env, ATSFontMetricsFc.clazz, "leading", "F");
	ATSFontMetricsFc.avgAdvanceWidth = (*env)->GetFieldID(env, ATSFontMetricsFc.clazz, "avgAdvanceWidth", "F");
	ATSFontMetricsFc.maxAdvanceWidth = (*env)->GetFieldID(env, ATSFontMetricsFc.clazz, "maxAdvanceWidth", "F");
	ATSFontMetricsFc.minLeftSideBearing = (*env)->GetFieldID(env, ATSFontMetricsFc.clazz, "minLeftSideBearing", "F");
	ATSFontMetricsFc.minRightSideBearing = (*env)->GetFieldID(env, ATSFontMetricsFc.clazz, "minRightSideBearing", "F");
	ATSFontMetricsFc.stemWidth = (*env)->GetFieldID(env, ATSFontMetricsFc.clazz, "stemWidth", "F");
	ATSFontMetricsFc.stemHeight = (*env)->GetFieldID(env, ATSFontMetricsFc.clazz, "stemHeight", "F");
	ATSFontMetricsFc.capHeight = (*env)->GetFieldID(env, ATSFontMetricsFc.clazz, "capHeight", "F");
	ATSFontMetricsFc.xHeight = (*env)->GetFieldID(env, ATSFontMetricsFc.clazz, "xHeight", "F");
	ATSFontMetricsFc.italicAngle = (*env)->GetFieldID(env, ATSFontMetricsFc.clazz, "italicAngle", "F");
	ATSFontMetricsFc.underlinePosition = (*env)->GetFieldID(env, ATSFontMetricsFc.clazz, "underlinePosition", "F");
	ATSFontMetricsFc.underlineThickness = (*env)->GetFieldID(env, ATSFontMetricsFc.clazz, "underlineThickness", "F");
	ATSFontMetricsFc.cached = 1;
}

ATSFontMetrics *getATSFontMetricsFields(JNIEnv *env, jobject lpObject, ATSFontMetrics *lpStruct)
{
	if (!ATSFontMetricsFc.cached) cacheATSFontMetricsFields(env, lpObject);
	lpStruct->version = (*env)->GetIntField(env, lpObject, ATSFontMetricsFc.version);
	lpStruct->ascent = (*env)->GetFloatField(env, lpObject, ATSFontMetricsFc.ascent);
	lpStruct->descent = (*env)->GetFloatField(env, lpObject, ATSFontMetricsFc.descent);
	lpStruct->leading = (*env)->GetFloatField(env, lpObject, ATSFontMetricsFc.leading);
	lpStruct->avgAdvanceWidth = (*env)->GetFloatField(env, lpObject, ATSFontMetricsFc.avgAdvanceWidth);
	lpStruct->maxAdvanceWidth = (*env)->GetFloatField(env, lpObject, ATSFontMetricsFc.maxAdvanceWidth);
	lpStruct->minLeftSideBearing = (*env)->GetFloatField(env, lpObject, ATSFontMetricsFc.minLeftSideBearing);
	lpStruct->minRightSideBearing = (*env)->GetFloatField(env, lpObject, ATSFontMetricsFc.minRightSideBearing);
	lpStruct->stemWidth = (*env)->GetFloatField(env, lpObject, ATSFontMetricsFc.stemWidth);
	lpStruct->stemHeight = (*env)->GetFloatField(env, lpObject, ATSFontMetricsFc.stemHeight);
	lpStruct->capHeight = (*env)->GetFloatField(env, lpObject, ATSFontMetricsFc.capHeight);
	lpStruct->xHeight = (*env)->GetFloatField(env, lpObject, ATSFontMetricsFc.xHeight);
	lpStruct->italicAngle = (*env)->GetFloatField(env, lpObject, ATSFontMetricsFc.italicAngle);
	lpStruct->underlinePosition = (*env)->GetFloatField(env, lpObject, ATSFontMetricsFc.underlinePosition);
	lpStruct->underlineThickness = (*env)->GetFloatField(env, lpObject, ATSFontMetricsFc.underlineThickness);
	return lpStruct;
}

void setATSFontMetricsFields(JNIEnv *env, jobject lpObject, ATSFontMetrics *lpStruct)
{
	if (!ATSFontMetricsFc.cached) cacheATSFontMetricsFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, ATSFontMetricsFc.version, (jint)lpStruct->version);
	(*env)->SetFloatField(env, lpObject, ATSFontMetricsFc.ascent, (jfloat)lpStruct->ascent);
	(*env)->SetFloatField(env, lpObject, ATSFontMetricsFc.descent, (jfloat)lpStruct->descent);
	(*env)->SetFloatField(env, lpObject, ATSFontMetricsFc.leading, (jfloat)lpStruct->leading);
	(*env)->SetFloatField(env, lpObject, ATSFontMetricsFc.avgAdvanceWidth, (jfloat)lpStruct->avgAdvanceWidth);
	(*env)->SetFloatField(env, lpObject, ATSFontMetricsFc.maxAdvanceWidth, (jfloat)lpStruct->maxAdvanceWidth);
	(*env)->SetFloatField(env, lpObject, ATSFontMetricsFc.minLeftSideBearing, (jfloat)lpStruct->minLeftSideBearing);
	(*env)->SetFloatField(env, lpObject, ATSFontMetricsFc.minRightSideBearing, (jfloat)lpStruct->minRightSideBearing);
	(*env)->SetFloatField(env, lpObject, ATSFontMetricsFc.stemWidth, (jfloat)lpStruct->stemWidth);
	(*env)->SetFloatField(env, lpObject, ATSFontMetricsFc.stemHeight, (jfloat)lpStruct->stemHeight);
	(*env)->SetFloatField(env, lpObject, ATSFontMetricsFc.capHeight, (jfloat)lpStruct->capHeight);
	(*env)->SetFloatField(env, lpObject, ATSFontMetricsFc.xHeight, (jfloat)lpStruct->xHeight);
	(*env)->SetFloatField(env, lpObject, ATSFontMetricsFc.italicAngle, (jfloat)lpStruct->italicAngle);
	(*env)->SetFloatField(env, lpObject, ATSFontMetricsFc.underlinePosition, (jfloat)lpStruct->underlinePosition);
	(*env)->SetFloatField(env, lpObject, ATSFontMetricsFc.underlineThickness, (jfloat)lpStruct->underlineThickness);
}
#endif

#ifndef NO_ATSLayoutRecord
typedef struct ATSLayoutRecord_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID glyphID, flags, originalOffset, realPos;
} ATSLayoutRecord_FID_CACHE;

ATSLayoutRecord_FID_CACHE ATSLayoutRecordFc;

void cacheATSLayoutRecordFields(JNIEnv *env, jobject lpObject)
{
	if (ATSLayoutRecordFc.cached) return;
	ATSLayoutRecordFc.clazz = (*env)->GetObjectClass(env, lpObject);
	ATSLayoutRecordFc.glyphID = (*env)->GetFieldID(env, ATSLayoutRecordFc.clazz, "glyphID", "S");
	ATSLayoutRecordFc.flags = (*env)->GetFieldID(env, ATSLayoutRecordFc.clazz, "flags", "I");
	ATSLayoutRecordFc.originalOffset = (*env)->GetFieldID(env, ATSLayoutRecordFc.clazz, "originalOffset", "I");
	ATSLayoutRecordFc.realPos = (*env)->GetFieldID(env, ATSLayoutRecordFc.clazz, "realPos", "I");
	ATSLayoutRecordFc.cached = 1;
}

ATSLayoutRecord *getATSLayoutRecordFields(JNIEnv *env, jobject lpObject, ATSLayoutRecord *lpStruct)
{
	if (!ATSLayoutRecordFc.cached) cacheATSLayoutRecordFields(env, lpObject);
	lpStruct->glyphID = (*env)->GetShortField(env, lpObject, ATSLayoutRecordFc.glyphID);
	lpStruct->flags = (*env)->GetIntField(env, lpObject, ATSLayoutRecordFc.flags);
	lpStruct->originalOffset = (*env)->GetIntField(env, lpObject, ATSLayoutRecordFc.originalOffset);
	lpStruct->realPos = (*env)->GetIntField(env, lpObject, ATSLayoutRecordFc.realPos);
	return lpStruct;
}

void setATSLayoutRecordFields(JNIEnv *env, jobject lpObject, ATSLayoutRecord *lpStruct)
{
	if (!ATSLayoutRecordFc.cached) cacheATSLayoutRecordFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, ATSLayoutRecordFc.glyphID, (jshort)lpStruct->glyphID);
	(*env)->SetIntField(env, lpObject, ATSLayoutRecordFc.flags, (jint)lpStruct->flags);
	(*env)->SetIntField(env, lpObject, ATSLayoutRecordFc.originalOffset, (jint)lpStruct->originalOffset);
	(*env)->SetIntField(env, lpObject, ATSLayoutRecordFc.realPos, (jint)lpStruct->realPos);
}
#endif

#ifndef NO_ATSTrapezoid
typedef struct ATSTrapezoid_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID upperLeft_x, upperLeft_y, upperRight_x, upperRight_y, lowerRight_x, lowerRight_y, lowerLeft_x, lowerLeft_y;
} ATSTrapezoid_FID_CACHE;

ATSTrapezoid_FID_CACHE ATSTrapezoidFc;

void cacheATSTrapezoidFields(JNIEnv *env, jobject lpObject)
{
	if (ATSTrapezoidFc.cached) return;
	ATSTrapezoidFc.clazz = (*env)->GetObjectClass(env, lpObject);
	ATSTrapezoidFc.upperLeft_x = (*env)->GetFieldID(env, ATSTrapezoidFc.clazz, "upperLeft_x", "I");
	ATSTrapezoidFc.upperLeft_y = (*env)->GetFieldID(env, ATSTrapezoidFc.clazz, "upperLeft_y", "I");
	ATSTrapezoidFc.upperRight_x = (*env)->GetFieldID(env, ATSTrapezoidFc.clazz, "upperRight_x", "I");
	ATSTrapezoidFc.upperRight_y = (*env)->GetFieldID(env, ATSTrapezoidFc.clazz, "upperRight_y", "I");
	ATSTrapezoidFc.lowerRight_x = (*env)->GetFieldID(env, ATSTrapezoidFc.clazz, "lowerRight_x", "I");
	ATSTrapezoidFc.lowerRight_y = (*env)->GetFieldID(env, ATSTrapezoidFc.clazz, "lowerRight_y", "I");
	ATSTrapezoidFc.lowerLeft_x = (*env)->GetFieldID(env, ATSTrapezoidFc.clazz, "lowerLeft_x", "I");
	ATSTrapezoidFc.lowerLeft_y = (*env)->GetFieldID(env, ATSTrapezoidFc.clazz, "lowerLeft_y", "I");
	ATSTrapezoidFc.cached = 1;
}

ATSTrapezoid *getATSTrapezoidFields(JNIEnv *env, jobject lpObject, ATSTrapezoid *lpStruct)
{
	if (!ATSTrapezoidFc.cached) cacheATSTrapezoidFields(env, lpObject);
	lpStruct->upperLeft.x = (*env)->GetIntField(env, lpObject, ATSTrapezoidFc.upperLeft_x);
	lpStruct->upperLeft.y = (*env)->GetIntField(env, lpObject, ATSTrapezoidFc.upperLeft_y);
	lpStruct->upperRight.x = (*env)->GetIntField(env, lpObject, ATSTrapezoidFc.upperRight_x);
	lpStruct->upperRight.y = (*env)->GetIntField(env, lpObject, ATSTrapezoidFc.upperRight_y);
	lpStruct->lowerRight.x = (*env)->GetIntField(env, lpObject, ATSTrapezoidFc.lowerRight_x);
	lpStruct->lowerRight.y = (*env)->GetIntField(env, lpObject, ATSTrapezoidFc.lowerRight_y);
	lpStruct->lowerLeft.x = (*env)->GetIntField(env, lpObject, ATSTrapezoidFc.lowerLeft_x);
	lpStruct->lowerLeft.y = (*env)->GetIntField(env, lpObject, ATSTrapezoidFc.lowerLeft_y);
	return lpStruct;
}

void setATSTrapezoidFields(JNIEnv *env, jobject lpObject, ATSTrapezoid *lpStruct)
{
	if (!ATSTrapezoidFc.cached) cacheATSTrapezoidFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, ATSTrapezoidFc.upperLeft_x, (jint)lpStruct->upperLeft.x);
	(*env)->SetIntField(env, lpObject, ATSTrapezoidFc.upperLeft_y, (jint)lpStruct->upperLeft.y);
	(*env)->SetIntField(env, lpObject, ATSTrapezoidFc.upperRight_x, (jint)lpStruct->upperRight.x);
	(*env)->SetIntField(env, lpObject, ATSTrapezoidFc.upperRight_y, (jint)lpStruct->upperRight.y);
	(*env)->SetIntField(env, lpObject, ATSTrapezoidFc.lowerRight_x, (jint)lpStruct->lowerRight.x);
	(*env)->SetIntField(env, lpObject, ATSTrapezoidFc.lowerRight_y, (jint)lpStruct->lowerRight.y);
	(*env)->SetIntField(env, lpObject, ATSTrapezoidFc.lowerLeft_x, (jint)lpStruct->lowerLeft.x);
	(*env)->SetIntField(env, lpObject, ATSTrapezoidFc.lowerLeft_y, (jint)lpStruct->lowerLeft.y);
}
#endif

#ifndef NO_ATSUCaret
typedef struct ATSUCaret_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID fX, fY, fDeltaX, fDeltaY;
} ATSUCaret_FID_CACHE;

ATSUCaret_FID_CACHE ATSUCaretFc;

void cacheATSUCaretFields(JNIEnv *env, jobject lpObject)
{
	if (ATSUCaretFc.cached) return;
	ATSUCaretFc.clazz = (*env)->GetObjectClass(env, lpObject);
	ATSUCaretFc.fX = (*env)->GetFieldID(env, ATSUCaretFc.clazz, "fX", "I");
	ATSUCaretFc.fY = (*env)->GetFieldID(env, ATSUCaretFc.clazz, "fY", "I");
	ATSUCaretFc.fDeltaX = (*env)->GetFieldID(env, ATSUCaretFc.clazz, "fDeltaX", "I");
	ATSUCaretFc.fDeltaY = (*env)->GetFieldID(env, ATSUCaretFc.clazz, "fDeltaY", "I");
	ATSUCaretFc.cached = 1;
}

ATSUCaret *getATSUCaretFields(JNIEnv *env, jobject lpObject, ATSUCaret *lpStruct)
{
	if (!ATSUCaretFc.cached) cacheATSUCaretFields(env, lpObject);
	lpStruct->fX = (*env)->GetIntField(env, lpObject, ATSUCaretFc.fX);
	lpStruct->fY = (*env)->GetIntField(env, lpObject, ATSUCaretFc.fY);
	lpStruct->fDeltaX = (*env)->GetIntField(env, lpObject, ATSUCaretFc.fDeltaX);
	lpStruct->fDeltaY = (*env)->GetIntField(env, lpObject, ATSUCaretFc.fDeltaY);
	return lpStruct;
}

void setATSUCaretFields(JNIEnv *env, jobject lpObject, ATSUCaret *lpStruct)
{
	if (!ATSUCaretFc.cached) cacheATSUCaretFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, ATSUCaretFc.fX, (jint)lpStruct->fX);
	(*env)->SetIntField(env, lpObject, ATSUCaretFc.fY, (jint)lpStruct->fY);
	(*env)->SetIntField(env, lpObject, ATSUCaretFc.fDeltaX, (jint)lpStruct->fDeltaX);
	(*env)->SetIntField(env, lpObject, ATSUCaretFc.fDeltaY, (jint)lpStruct->fDeltaY);
}
#endif

#ifndef NO_ATSUTab
typedef struct ATSUTab_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID tabPosition, tabType;
} ATSUTab_FID_CACHE;

ATSUTab_FID_CACHE ATSUTabFc;

void cacheATSUTabFields(JNIEnv *env, jobject lpObject)
{
	if (ATSUTabFc.cached) return;
	ATSUTabFc.clazz = (*env)->GetObjectClass(env, lpObject);
	ATSUTabFc.tabPosition = (*env)->GetFieldID(env, ATSUTabFc.clazz, "tabPosition", "I");
	ATSUTabFc.tabType = (*env)->GetFieldID(env, ATSUTabFc.clazz, "tabType", "S");
	ATSUTabFc.cached = 1;
}

ATSUTab *getATSUTabFields(JNIEnv *env, jobject lpObject, ATSUTab *lpStruct)
{
	if (!ATSUTabFc.cached) cacheATSUTabFields(env, lpObject);
	lpStruct->tabPosition = (*env)->GetIntField(env, lpObject, ATSUTabFc.tabPosition);
	lpStruct->tabType = (*env)->GetShortField(env, lpObject, ATSUTabFc.tabType);
	return lpStruct;
}

void setATSUTabFields(JNIEnv *env, jobject lpObject, ATSUTab *lpStruct)
{
	if (!ATSUTabFc.cached) cacheATSUTabFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, ATSUTabFc.tabPosition, (jint)lpStruct->tabPosition);
	(*env)->SetShortField(env, lpObject, ATSUTabFc.tabType, (jshort)lpStruct->tabType);
}
#endif

#ifndef NO_ATSUUnhighlightData
typedef struct ATSUUnhighlightData_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID dataType, red, green, blue, alpha;
} ATSUUnhighlightData_FID_CACHE;

ATSUUnhighlightData_FID_CACHE ATSUUnhighlightDataFc;

void cacheATSUUnhighlightDataFields(JNIEnv *env, jobject lpObject)
{
	if (ATSUUnhighlightDataFc.cached) return;
	ATSUUnhighlightDataFc.clazz = (*env)->GetObjectClass(env, lpObject);
	ATSUUnhighlightDataFc.dataType = (*env)->GetFieldID(env, ATSUUnhighlightDataFc.clazz, "dataType", "I");
	ATSUUnhighlightDataFc.red = (*env)->GetFieldID(env, ATSUUnhighlightDataFc.clazz, "red", "F");
	ATSUUnhighlightDataFc.green = (*env)->GetFieldID(env, ATSUUnhighlightDataFc.clazz, "green", "F");
	ATSUUnhighlightDataFc.blue = (*env)->GetFieldID(env, ATSUUnhighlightDataFc.clazz, "blue", "F");
	ATSUUnhighlightDataFc.alpha = (*env)->GetFieldID(env, ATSUUnhighlightDataFc.clazz, "alpha", "F");
	ATSUUnhighlightDataFc.cached = 1;
}

ATSUUnhighlightData *getATSUUnhighlightDataFields(JNIEnv *env, jobject lpObject, ATSUUnhighlightData *lpStruct)
{
	if (!ATSUUnhighlightDataFc.cached) cacheATSUUnhighlightDataFields(env, lpObject);
	lpStruct->dataType = (*env)->GetIntField(env, lpObject, ATSUUnhighlightDataFc.dataType);
	lpStruct->unhighlightData.backgroundColor.red = (*env)->GetFloatField(env, lpObject, ATSUUnhighlightDataFc.red);
	lpStruct->unhighlightData.backgroundColor.green = (*env)->GetFloatField(env, lpObject, ATSUUnhighlightDataFc.green);
	lpStruct->unhighlightData.backgroundColor.blue = (*env)->GetFloatField(env, lpObject, ATSUUnhighlightDataFc.blue);
	lpStruct->unhighlightData.backgroundColor.alpha = (*env)->GetFloatField(env, lpObject, ATSUUnhighlightDataFc.alpha);
	return lpStruct;
}

void setATSUUnhighlightDataFields(JNIEnv *env, jobject lpObject, ATSUUnhighlightData *lpStruct)
{
	if (!ATSUUnhighlightDataFc.cached) cacheATSUUnhighlightDataFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, ATSUUnhighlightDataFc.dataType, (jint)lpStruct->dataType);
	(*env)->SetFloatField(env, lpObject, ATSUUnhighlightDataFc.red, (jfloat)lpStruct->unhighlightData.backgroundColor.red);
	(*env)->SetFloatField(env, lpObject, ATSUUnhighlightDataFc.green, (jfloat)lpStruct->unhighlightData.backgroundColor.green);
	(*env)->SetFloatField(env, lpObject, ATSUUnhighlightDataFc.blue, (jfloat)lpStruct->unhighlightData.backgroundColor.blue);
	(*env)->SetFloatField(env, lpObject, ATSUUnhighlightDataFc.alpha, (jfloat)lpStruct->unhighlightData.backgroundColor.alpha);
}
#endif

#ifndef NO_AlertStdCFStringAlertParamRec
typedef struct AlertStdCFStringAlertParamRec_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID version, movable, helpButton, defaultText, cancelText, otherText, defaultButton, cancelButton, position, flags;
} AlertStdCFStringAlertParamRec_FID_CACHE;

AlertStdCFStringAlertParamRec_FID_CACHE AlertStdCFStringAlertParamRecFc;

void cacheAlertStdCFStringAlertParamRecFields(JNIEnv *env, jobject lpObject)
{
	if (AlertStdCFStringAlertParamRecFc.cached) return;
	AlertStdCFStringAlertParamRecFc.clazz = (*env)->GetObjectClass(env, lpObject);
	AlertStdCFStringAlertParamRecFc.version = (*env)->GetFieldID(env, AlertStdCFStringAlertParamRecFc.clazz, "version", "I");
	AlertStdCFStringAlertParamRecFc.movable = (*env)->GetFieldID(env, AlertStdCFStringAlertParamRecFc.clazz, "movable", "Z");
	AlertStdCFStringAlertParamRecFc.helpButton = (*env)->GetFieldID(env, AlertStdCFStringAlertParamRecFc.clazz, "helpButton", "Z");
	AlertStdCFStringAlertParamRecFc.defaultText = (*env)->GetFieldID(env, AlertStdCFStringAlertParamRecFc.clazz, "defaultText", "I");
	AlertStdCFStringAlertParamRecFc.cancelText = (*env)->GetFieldID(env, AlertStdCFStringAlertParamRecFc.clazz, "cancelText", "I");
	AlertStdCFStringAlertParamRecFc.otherText = (*env)->GetFieldID(env, AlertStdCFStringAlertParamRecFc.clazz, "otherText", "I");
	AlertStdCFStringAlertParamRecFc.defaultButton = (*env)->GetFieldID(env, AlertStdCFStringAlertParamRecFc.clazz, "defaultButton", "S");
	AlertStdCFStringAlertParamRecFc.cancelButton = (*env)->GetFieldID(env, AlertStdCFStringAlertParamRecFc.clazz, "cancelButton", "S");
	AlertStdCFStringAlertParamRecFc.position = (*env)->GetFieldID(env, AlertStdCFStringAlertParamRecFc.clazz, "position", "S");
	AlertStdCFStringAlertParamRecFc.flags = (*env)->GetFieldID(env, AlertStdCFStringAlertParamRecFc.clazz, "flags", "I");
	AlertStdCFStringAlertParamRecFc.cached = 1;
}

AlertStdCFStringAlertParamRec *getAlertStdCFStringAlertParamRecFields(JNIEnv *env, jobject lpObject, AlertStdCFStringAlertParamRec *lpStruct)
{
	if (!AlertStdCFStringAlertParamRecFc.cached) cacheAlertStdCFStringAlertParamRecFields(env, lpObject);
	lpStruct->version = (*env)->GetIntField(env, lpObject, AlertStdCFStringAlertParamRecFc.version);
	lpStruct->movable = (*env)->GetBooleanField(env, lpObject, AlertStdCFStringAlertParamRecFc.movable);
	lpStruct->helpButton = (*env)->GetBooleanField(env, lpObject, AlertStdCFStringAlertParamRecFc.helpButton);
	lpStruct->defaultText = (CFStringRef)(*env)->GetIntField(env, lpObject, AlertStdCFStringAlertParamRecFc.defaultText);
	lpStruct->cancelText = (CFStringRef)(*env)->GetIntField(env, lpObject, AlertStdCFStringAlertParamRecFc.cancelText);
	lpStruct->otherText = (CFStringRef)(*env)->GetIntField(env, lpObject, AlertStdCFStringAlertParamRecFc.otherText);
	lpStruct->defaultButton = (*env)->GetShortField(env, lpObject, AlertStdCFStringAlertParamRecFc.defaultButton);
	lpStruct->cancelButton = (*env)->GetShortField(env, lpObject, AlertStdCFStringAlertParamRecFc.cancelButton);
	lpStruct->position = (*env)->GetShortField(env, lpObject, AlertStdCFStringAlertParamRecFc.position);
	lpStruct->flags = (*env)->GetIntField(env, lpObject, AlertStdCFStringAlertParamRecFc.flags);
	return lpStruct;
}

void setAlertStdCFStringAlertParamRecFields(JNIEnv *env, jobject lpObject, AlertStdCFStringAlertParamRec *lpStruct)
{
	if (!AlertStdCFStringAlertParamRecFc.cached) cacheAlertStdCFStringAlertParamRecFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, AlertStdCFStringAlertParamRecFc.version, (jint)lpStruct->version);
	(*env)->SetBooleanField(env, lpObject, AlertStdCFStringAlertParamRecFc.movable, (jboolean)lpStruct->movable);
	(*env)->SetBooleanField(env, lpObject, AlertStdCFStringAlertParamRecFc.helpButton, (jboolean)lpStruct->helpButton);
	(*env)->SetIntField(env, lpObject, AlertStdCFStringAlertParamRecFc.defaultText, (jint)lpStruct->defaultText);
	(*env)->SetIntField(env, lpObject, AlertStdCFStringAlertParamRecFc.cancelText, (jint)lpStruct->cancelText);
	(*env)->SetIntField(env, lpObject, AlertStdCFStringAlertParamRecFc.otherText, (jint)lpStruct->otherText);
	(*env)->SetShortField(env, lpObject, AlertStdCFStringAlertParamRecFc.defaultButton, (jshort)lpStruct->defaultButton);
	(*env)->SetShortField(env, lpObject, AlertStdCFStringAlertParamRecFc.cancelButton, (jshort)lpStruct->cancelButton);
	(*env)->SetShortField(env, lpObject, AlertStdCFStringAlertParamRecFc.position, (jshort)lpStruct->position);
	(*env)->SetIntField(env, lpObject, AlertStdCFStringAlertParamRecFc.flags, (jint)lpStruct->flags);
}
#endif

#ifndef NO_BitMap
typedef struct BitMap_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID baseAddr, rowBytes, top, left, bottom, right;
} BitMap_FID_CACHE;

BitMap_FID_CACHE BitMapFc;

void cacheBitMapFields(JNIEnv *env, jobject lpObject)
{
	if (BitMapFc.cached) return;
	BitMapFc.clazz = (*env)->GetObjectClass(env, lpObject);
	BitMapFc.baseAddr = (*env)->GetFieldID(env, BitMapFc.clazz, "baseAddr", "I");
	BitMapFc.rowBytes = (*env)->GetFieldID(env, BitMapFc.clazz, "rowBytes", "S");
	BitMapFc.top = (*env)->GetFieldID(env, BitMapFc.clazz, "top", "S");
	BitMapFc.left = (*env)->GetFieldID(env, BitMapFc.clazz, "left", "S");
	BitMapFc.bottom = (*env)->GetFieldID(env, BitMapFc.clazz, "bottom", "S");
	BitMapFc.right = (*env)->GetFieldID(env, BitMapFc.clazz, "right", "S");
	BitMapFc.cached = 1;
}

BitMap *getBitMapFields(JNIEnv *env, jobject lpObject, BitMap *lpStruct)
{
	if (!BitMapFc.cached) cacheBitMapFields(env, lpObject);
	lpStruct->baseAddr = (void *)(*env)->GetIntField(env, lpObject, BitMapFc.baseAddr);
	lpStruct->rowBytes = (*env)->GetShortField(env, lpObject, BitMapFc.rowBytes);
	lpStruct->bounds.top = (*env)->GetShortField(env, lpObject, BitMapFc.top);
	lpStruct->bounds.left = (*env)->GetShortField(env, lpObject, BitMapFc.left);
	lpStruct->bounds.bottom = (*env)->GetShortField(env, lpObject, BitMapFc.bottom);
	lpStruct->bounds.right = (*env)->GetShortField(env, lpObject, BitMapFc.right);
	return lpStruct;
}

void setBitMapFields(JNIEnv *env, jobject lpObject, BitMap *lpStruct)
{
	if (!BitMapFc.cached) cacheBitMapFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, BitMapFc.baseAddr, (jint)lpStruct->baseAddr);
	(*env)->SetShortField(env, lpObject, BitMapFc.rowBytes, (jshort)lpStruct->rowBytes);
	(*env)->SetShortField(env, lpObject, BitMapFc.top, (jshort)lpStruct->bounds.top);
	(*env)->SetShortField(env, lpObject, BitMapFc.left, (jshort)lpStruct->bounds.left);
	(*env)->SetShortField(env, lpObject, BitMapFc.bottom, (jshort)lpStruct->bounds.bottom);
	(*env)->SetShortField(env, lpObject, BitMapFc.right, (jshort)lpStruct->bounds.right);
}
#endif

#ifndef NO_CFRange
typedef struct CFRange_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID location, length;
} CFRange_FID_CACHE;

CFRange_FID_CACHE CFRangeFc;

void cacheCFRangeFields(JNIEnv *env, jobject lpObject)
{
	if (CFRangeFc.cached) return;
	CFRangeFc.clazz = (*env)->GetObjectClass(env, lpObject);
	CFRangeFc.location = (*env)->GetFieldID(env, CFRangeFc.clazz, "location", "I");
	CFRangeFc.length = (*env)->GetFieldID(env, CFRangeFc.clazz, "length", "I");
	CFRangeFc.cached = 1;
}

CFRange *getCFRangeFields(JNIEnv *env, jobject lpObject, CFRange *lpStruct)
{
	if (!CFRangeFc.cached) cacheCFRangeFields(env, lpObject);
	lpStruct->location = (CFIndex)(*env)->GetIntField(env, lpObject, CFRangeFc.location);
	lpStruct->length = (CFIndex)(*env)->GetIntField(env, lpObject, CFRangeFc.length);
	return lpStruct;
}

void setCFRangeFields(JNIEnv *env, jobject lpObject, CFRange *lpStruct)
{
	if (!CFRangeFc.cached) cacheCFRangeFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, CFRangeFc.location, (jint)lpStruct->location);
	(*env)->SetIntField(env, lpObject, CFRangeFc.length, (jint)lpStruct->length);
}
#endif

#ifndef NO_CFRunLoopSourceContext
typedef struct CFRunLoopSourceContext_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID version, info, retain, release, copyDescription, equal, hash, schedule, cancel, perform;
} CFRunLoopSourceContext_FID_CACHE;

CFRunLoopSourceContext_FID_CACHE CFRunLoopSourceContextFc;

void cacheCFRunLoopSourceContextFields(JNIEnv *env, jobject lpObject)
{
	if (CFRunLoopSourceContextFc.cached) return;
	CFRunLoopSourceContextFc.clazz = (*env)->GetObjectClass(env, lpObject);
	CFRunLoopSourceContextFc.version = (*env)->GetFieldID(env, CFRunLoopSourceContextFc.clazz, "version", "I");
	CFRunLoopSourceContextFc.info = (*env)->GetFieldID(env, CFRunLoopSourceContextFc.clazz, "info", "I");
	CFRunLoopSourceContextFc.retain = (*env)->GetFieldID(env, CFRunLoopSourceContextFc.clazz, "retain", "I");
	CFRunLoopSourceContextFc.release = (*env)->GetFieldID(env, CFRunLoopSourceContextFc.clazz, "release", "I");
	CFRunLoopSourceContextFc.copyDescription = (*env)->GetFieldID(env, CFRunLoopSourceContextFc.clazz, "copyDescription", "I");
	CFRunLoopSourceContextFc.equal = (*env)->GetFieldID(env, CFRunLoopSourceContextFc.clazz, "equal", "I");
	CFRunLoopSourceContextFc.hash = (*env)->GetFieldID(env, CFRunLoopSourceContextFc.clazz, "hash", "I");
	CFRunLoopSourceContextFc.schedule = (*env)->GetFieldID(env, CFRunLoopSourceContextFc.clazz, "schedule", "I");
	CFRunLoopSourceContextFc.cancel = (*env)->GetFieldID(env, CFRunLoopSourceContextFc.clazz, "cancel", "I");
	CFRunLoopSourceContextFc.perform = (*env)->GetFieldID(env, CFRunLoopSourceContextFc.clazz, "perform", "I");
	CFRunLoopSourceContextFc.cached = 1;
}

CFRunLoopSourceContext *getCFRunLoopSourceContextFields(JNIEnv *env, jobject lpObject, CFRunLoopSourceContext *lpStruct)
{
	if (!CFRunLoopSourceContextFc.cached) cacheCFRunLoopSourceContextFields(env, lpObject);
	lpStruct->version = (CFIndex)(*env)->GetIntField(env, lpObject, CFRunLoopSourceContextFc.version);
	lpStruct->info = (void *)(*env)->GetIntField(env, lpObject, CFRunLoopSourceContextFc.info);
	lpStruct->retain = (void *)(*env)->GetIntField(env, lpObject, CFRunLoopSourceContextFc.retain);
	lpStruct->release = (void *)(*env)->GetIntField(env, lpObject, CFRunLoopSourceContextFc.release);
	lpStruct->copyDescription = (void *)(*env)->GetIntField(env, lpObject, CFRunLoopSourceContextFc.copyDescription);
	lpStruct->equal = (void *)(*env)->GetIntField(env, lpObject, CFRunLoopSourceContextFc.equal);
	lpStruct->hash = (void *)(*env)->GetIntField(env, lpObject, CFRunLoopSourceContextFc.hash);
	lpStruct->schedule = (void *)(*env)->GetIntField(env, lpObject, CFRunLoopSourceContextFc.schedule);
	lpStruct->cancel = (void *)(*env)->GetIntField(env, lpObject, CFRunLoopSourceContextFc.cancel);
	lpStruct->perform = (void *)(*env)->GetIntField(env, lpObject, CFRunLoopSourceContextFc.perform);
	return lpStruct;
}

void setCFRunLoopSourceContextFields(JNIEnv *env, jobject lpObject, CFRunLoopSourceContext *lpStruct)
{
	if (!CFRunLoopSourceContextFc.cached) cacheCFRunLoopSourceContextFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, CFRunLoopSourceContextFc.version, (jint)lpStruct->version);
	(*env)->SetIntField(env, lpObject, CFRunLoopSourceContextFc.info, (jint)lpStruct->info);
	(*env)->SetIntField(env, lpObject, CFRunLoopSourceContextFc.retain, (jint)lpStruct->retain);
	(*env)->SetIntField(env, lpObject, CFRunLoopSourceContextFc.release, (jint)lpStruct->release);
	(*env)->SetIntField(env, lpObject, CFRunLoopSourceContextFc.copyDescription, (jint)lpStruct->copyDescription);
	(*env)->SetIntField(env, lpObject, CFRunLoopSourceContextFc.equal, (jint)lpStruct->equal);
	(*env)->SetIntField(env, lpObject, CFRunLoopSourceContextFc.hash, (jint)lpStruct->hash);
	(*env)->SetIntField(env, lpObject, CFRunLoopSourceContextFc.schedule, (jint)lpStruct->schedule);
	(*env)->SetIntField(env, lpObject, CFRunLoopSourceContextFc.cancel, (jint)lpStruct->cancel);
	(*env)->SetIntField(env, lpObject, CFRunLoopSourceContextFc.perform, (jint)lpStruct->perform);
}
#endif

#ifndef NO_CGFunctionCallbacks
typedef struct CGFunctionCallbacks_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID version, evaluate, releaseInfo;
} CGFunctionCallbacks_FID_CACHE;

CGFunctionCallbacks_FID_CACHE CGFunctionCallbacksFc;

void cacheCGFunctionCallbacksFields(JNIEnv *env, jobject lpObject)
{
	if (CGFunctionCallbacksFc.cached) return;
	CGFunctionCallbacksFc.clazz = (*env)->GetObjectClass(env, lpObject);
	CGFunctionCallbacksFc.version = (*env)->GetFieldID(env, CGFunctionCallbacksFc.clazz, "version", "I");
	CGFunctionCallbacksFc.evaluate = (*env)->GetFieldID(env, CGFunctionCallbacksFc.clazz, "evaluate", "I");
	CGFunctionCallbacksFc.releaseInfo = (*env)->GetFieldID(env, CGFunctionCallbacksFc.clazz, "releaseInfo", "I");
	CGFunctionCallbacksFc.cached = 1;
}

CGFunctionCallbacks *getCGFunctionCallbacksFields(JNIEnv *env, jobject lpObject, CGFunctionCallbacks *lpStruct)
{
	if (!CGFunctionCallbacksFc.cached) cacheCGFunctionCallbacksFields(env, lpObject);
	lpStruct->version = (*env)->GetIntField(env, lpObject, CGFunctionCallbacksFc.version);
	lpStruct->evaluate = (CGFunctionEvaluateCallback)(*env)->GetIntField(env, lpObject, CGFunctionCallbacksFc.evaluate);
	lpStruct->releaseInfo = (CGFunctionReleaseInfoCallback)(*env)->GetIntField(env, lpObject, CGFunctionCallbacksFc.releaseInfo);
	return lpStruct;
}

void setCGFunctionCallbacksFields(JNIEnv *env, jobject lpObject, CGFunctionCallbacks *lpStruct)
{
	if (!CGFunctionCallbacksFc.cached) cacheCGFunctionCallbacksFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, CGFunctionCallbacksFc.version, (jint)lpStruct->version);
	(*env)->SetIntField(env, lpObject, CGFunctionCallbacksFc.evaluate, (jint)lpStruct->evaluate);
	(*env)->SetIntField(env, lpObject, CGFunctionCallbacksFc.releaseInfo, (jint)lpStruct->releaseInfo);
}
#endif

#ifndef NO_CGPathElement
typedef struct CGPathElement_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID type, points;
} CGPathElement_FID_CACHE;

CGPathElement_FID_CACHE CGPathElementFc;

void cacheCGPathElementFields(JNIEnv *env, jobject lpObject)
{
	if (CGPathElementFc.cached) return;
	CGPathElementFc.clazz = (*env)->GetObjectClass(env, lpObject);
	CGPathElementFc.type = (*env)->GetFieldID(env, CGPathElementFc.clazz, "type", "I");
	CGPathElementFc.points = (*env)->GetFieldID(env, CGPathElementFc.clazz, "points", "I");
	CGPathElementFc.cached = 1;
}

CGPathElement *getCGPathElementFields(JNIEnv *env, jobject lpObject, CGPathElement *lpStruct)
{
	if (!CGPathElementFc.cached) cacheCGPathElementFields(env, lpObject);
	lpStruct->type = (CGPathElementType)(*env)->GetIntField(env, lpObject, CGPathElementFc.type);
	lpStruct->points = (CGPoint *)(*env)->GetIntField(env, lpObject, CGPathElementFc.points);
	return lpStruct;
}

void setCGPathElementFields(JNIEnv *env, jobject lpObject, CGPathElement *lpStruct)
{
	if (!CGPathElementFc.cached) cacheCGPathElementFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, CGPathElementFc.type, (jint)lpStruct->type);
	(*env)->SetIntField(env, lpObject, CGPathElementFc.points, (jint)lpStruct->points);
}
#endif

#ifndef NO_CGPatternCallbacks
typedef struct CGPatternCallbacks_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID version, drawPattern, releaseInfo;
} CGPatternCallbacks_FID_CACHE;

CGPatternCallbacks_FID_CACHE CGPatternCallbacksFc;

void cacheCGPatternCallbacksFields(JNIEnv *env, jobject lpObject)
{
	if (CGPatternCallbacksFc.cached) return;
	CGPatternCallbacksFc.clazz = (*env)->GetObjectClass(env, lpObject);
	CGPatternCallbacksFc.version = (*env)->GetFieldID(env, CGPatternCallbacksFc.clazz, "version", "I");
	CGPatternCallbacksFc.drawPattern = (*env)->GetFieldID(env, CGPatternCallbacksFc.clazz, "drawPattern", "I");
	CGPatternCallbacksFc.releaseInfo = (*env)->GetFieldID(env, CGPatternCallbacksFc.clazz, "releaseInfo", "I");
	CGPatternCallbacksFc.cached = 1;
}

CGPatternCallbacks *getCGPatternCallbacksFields(JNIEnv *env, jobject lpObject, CGPatternCallbacks *lpStruct)
{
	if (!CGPatternCallbacksFc.cached) cacheCGPatternCallbacksFields(env, lpObject);
	lpStruct->version = (*env)->GetIntField(env, lpObject, CGPatternCallbacksFc.version);
	lpStruct->drawPattern = (CGPatternDrawPatternCallback)(*env)->GetIntField(env, lpObject, CGPatternCallbacksFc.drawPattern);
	lpStruct->releaseInfo = (CGPatternReleaseInfoCallback)(*env)->GetIntField(env, lpObject, CGPatternCallbacksFc.releaseInfo);
	return lpStruct;
}

void setCGPatternCallbacksFields(JNIEnv *env, jobject lpObject, CGPatternCallbacks *lpStruct)
{
	if (!CGPatternCallbacksFc.cached) cacheCGPatternCallbacksFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, CGPatternCallbacksFc.version, (jint)lpStruct->version);
	(*env)->SetIntField(env, lpObject, CGPatternCallbacksFc.drawPattern, (jint)lpStruct->drawPattern);
	(*env)->SetIntField(env, lpObject, CGPatternCallbacksFc.releaseInfo, (jint)lpStruct->releaseInfo);
}
#endif

#ifndef NO_CGPoint
typedef struct CGPoint_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID x, y;
} CGPoint_FID_CACHE;

CGPoint_FID_CACHE CGPointFc;

void cacheCGPointFields(JNIEnv *env, jobject lpObject)
{
	if (CGPointFc.cached) return;
	CGPointFc.clazz = (*env)->GetObjectClass(env, lpObject);
	CGPointFc.x = (*env)->GetFieldID(env, CGPointFc.clazz, "x", "F");
	CGPointFc.y = (*env)->GetFieldID(env, CGPointFc.clazz, "y", "F");
	CGPointFc.cached = 1;
}

CGPoint *getCGPointFields(JNIEnv *env, jobject lpObject, CGPoint *lpStruct)
{
	if (!CGPointFc.cached) cacheCGPointFields(env, lpObject);
	lpStruct->x = (float)(*env)->GetFloatField(env, lpObject, CGPointFc.x);
	lpStruct->y = (float)(*env)->GetFloatField(env, lpObject, CGPointFc.y);
	return lpStruct;
}

void setCGPointFields(JNIEnv *env, jobject lpObject, CGPoint *lpStruct)
{
	if (!CGPointFc.cached) cacheCGPointFields(env, lpObject);
	(*env)->SetFloatField(env, lpObject, CGPointFc.x, (jfloat)lpStruct->x);
	(*env)->SetFloatField(env, lpObject, CGPointFc.y, (jfloat)lpStruct->y);
}
#endif

#ifndef NO_CGRect
typedef struct CGRect_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID x, y, width, height;
} CGRect_FID_CACHE;

CGRect_FID_CACHE CGRectFc;

void cacheCGRectFields(JNIEnv *env, jobject lpObject)
{
	if (CGRectFc.cached) return;
	CGRectFc.clazz = (*env)->GetObjectClass(env, lpObject);
	CGRectFc.x = (*env)->GetFieldID(env, CGRectFc.clazz, "x", "F");
	CGRectFc.y = (*env)->GetFieldID(env, CGRectFc.clazz, "y", "F");
	CGRectFc.width = (*env)->GetFieldID(env, CGRectFc.clazz, "width", "F");
	CGRectFc.height = (*env)->GetFieldID(env, CGRectFc.clazz, "height", "F");
	CGRectFc.cached = 1;
}

CGRect *getCGRectFields(JNIEnv *env, jobject lpObject, CGRect *lpStruct)
{
	if (!CGRectFc.cached) cacheCGRectFields(env, lpObject);
	lpStruct->origin.x = (float)(*env)->GetFloatField(env, lpObject, CGRectFc.x);
	lpStruct->origin.y = (float)(*env)->GetFloatField(env, lpObject, CGRectFc.y);
	lpStruct->size.width = (float)(*env)->GetFloatField(env, lpObject, CGRectFc.width);
	lpStruct->size.height = (float)(*env)->GetFloatField(env, lpObject, CGRectFc.height);
	return lpStruct;
}

void setCGRectFields(JNIEnv *env, jobject lpObject, CGRect *lpStruct)
{
	if (!CGRectFc.cached) cacheCGRectFields(env, lpObject);
	(*env)->SetFloatField(env, lpObject, CGRectFc.x, (jfloat)lpStruct->origin.x);
	(*env)->SetFloatField(env, lpObject, CGRectFc.y, (jfloat)lpStruct->origin.y);
	(*env)->SetFloatField(env, lpObject, CGRectFc.width, (jfloat)lpStruct->size.width);
	(*env)->SetFloatField(env, lpObject, CGRectFc.height, (jfloat)lpStruct->size.height);
}
#endif

#ifndef NO_CGSize
typedef struct CGSize_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID width, height;
} CGSize_FID_CACHE;

CGSize_FID_CACHE CGSizeFc;

void cacheCGSizeFields(JNIEnv *env, jobject lpObject)
{
	if (CGSizeFc.cached) return;
	CGSizeFc.clazz = (*env)->GetObjectClass(env, lpObject);
	CGSizeFc.width = (*env)->GetFieldID(env, CGSizeFc.clazz, "width", "F");
	CGSizeFc.height = (*env)->GetFieldID(env, CGSizeFc.clazz, "height", "F");
	CGSizeFc.cached = 1;
}

CGSize *getCGSizeFields(JNIEnv *env, jobject lpObject, CGSize *lpStruct)
{
	if (!CGSizeFc.cached) cacheCGSizeFields(env, lpObject);
	lpStruct->width = (*env)->GetFloatField(env, lpObject, CGSizeFc.width);
	lpStruct->height = (*env)->GetFloatField(env, lpObject, CGSizeFc.height);
	return lpStruct;
}

void setCGSizeFields(JNIEnv *env, jobject lpObject, CGSize *lpStruct)
{
	if (!CGSizeFc.cached) cacheCGSizeFields(env, lpObject);
	(*env)->SetFloatField(env, lpObject, CGSizeFc.width, (jfloat)lpStruct->width);
	(*env)->SetFloatField(env, lpObject, CGSizeFc.height, (jfloat)lpStruct->height);
}
#endif

#ifndef NO_ColorPickerInfo
typedef struct ColorPickerInfo_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID profile, red, green, blue, dstProfile, flags, placeWhere, h, v, pickerType, eventProc, colorProc, colorProcData, prompt, editMenuID, cutItem, copyItem, pasteItem, clearItem, undoItem, newColorChosen;
} ColorPickerInfo_FID_CACHE;

ColorPickerInfo_FID_CACHE ColorPickerInfoFc;

void cacheColorPickerInfoFields(JNIEnv *env, jobject lpObject)
{
	if (ColorPickerInfoFc.cached) return;
	ColorPickerInfoFc.clazz = (*env)->GetObjectClass(env, lpObject);
	ColorPickerInfoFc.profile = (*env)->GetFieldID(env, ColorPickerInfoFc.clazz, "profile", "I");
	ColorPickerInfoFc.red = (*env)->GetFieldID(env, ColorPickerInfoFc.clazz, "red", "S");
	ColorPickerInfoFc.green = (*env)->GetFieldID(env, ColorPickerInfoFc.clazz, "green", "S");
	ColorPickerInfoFc.blue = (*env)->GetFieldID(env, ColorPickerInfoFc.clazz, "blue", "S");
	ColorPickerInfoFc.dstProfile = (*env)->GetFieldID(env, ColorPickerInfoFc.clazz, "dstProfile", "I");
	ColorPickerInfoFc.flags = (*env)->GetFieldID(env, ColorPickerInfoFc.clazz, "flags", "I");
	ColorPickerInfoFc.placeWhere = (*env)->GetFieldID(env, ColorPickerInfoFc.clazz, "placeWhere", "S");
	ColorPickerInfoFc.h = (*env)->GetFieldID(env, ColorPickerInfoFc.clazz, "h", "S");
	ColorPickerInfoFc.v = (*env)->GetFieldID(env, ColorPickerInfoFc.clazz, "v", "S");
	ColorPickerInfoFc.pickerType = (*env)->GetFieldID(env, ColorPickerInfoFc.clazz, "pickerType", "I");
	ColorPickerInfoFc.eventProc = (*env)->GetFieldID(env, ColorPickerInfoFc.clazz, "eventProc", "I");
	ColorPickerInfoFc.colorProc = (*env)->GetFieldID(env, ColorPickerInfoFc.clazz, "colorProc", "I");
	ColorPickerInfoFc.colorProcData = (*env)->GetFieldID(env, ColorPickerInfoFc.clazz, "colorProcData", "I");
	ColorPickerInfoFc.prompt = (*env)->GetFieldID(env, ColorPickerInfoFc.clazz, "prompt", "[B");
	ColorPickerInfoFc.editMenuID = (*env)->GetFieldID(env, ColorPickerInfoFc.clazz, "editMenuID", "S");
	ColorPickerInfoFc.cutItem = (*env)->GetFieldID(env, ColorPickerInfoFc.clazz, "cutItem", "S");
	ColorPickerInfoFc.copyItem = (*env)->GetFieldID(env, ColorPickerInfoFc.clazz, "copyItem", "S");
	ColorPickerInfoFc.pasteItem = (*env)->GetFieldID(env, ColorPickerInfoFc.clazz, "pasteItem", "S");
	ColorPickerInfoFc.clearItem = (*env)->GetFieldID(env, ColorPickerInfoFc.clazz, "clearItem", "S");
	ColorPickerInfoFc.undoItem = (*env)->GetFieldID(env, ColorPickerInfoFc.clazz, "undoItem", "S");
	ColorPickerInfoFc.newColorChosen = (*env)->GetFieldID(env, ColorPickerInfoFc.clazz, "newColorChosen", "Z");
	ColorPickerInfoFc.cached = 1;
}

ColorPickerInfo *getColorPickerInfoFields(JNIEnv *env, jobject lpObject, ColorPickerInfo *lpStruct)
{
	if (!ColorPickerInfoFc.cached) cacheColorPickerInfoFields(env, lpObject);
	lpStruct->theColor.profile = (CMProfileHandle)(*env)->GetIntField(env, lpObject, ColorPickerInfoFc.profile);
	lpStruct->theColor.color.rgb.red = (UInt16)(*env)->GetShortField(env, lpObject, ColorPickerInfoFc.red);
	lpStruct->theColor.color.rgb.green = (UInt16)(*env)->GetShortField(env, lpObject, ColorPickerInfoFc.green);
	lpStruct->theColor.color.rgb.blue = (UInt16)(*env)->GetShortField(env, lpObject, ColorPickerInfoFc.blue);
	lpStruct->dstProfile = (CMProfileHandle)(*env)->GetIntField(env, lpObject, ColorPickerInfoFc.dstProfile);
	lpStruct->flags = (UInt32)(*env)->GetIntField(env, lpObject, ColorPickerInfoFc.flags);
	lpStruct->placeWhere = (DialogPlacementSpec)(*env)->GetShortField(env, lpObject, ColorPickerInfoFc.placeWhere);
	lpStruct->dialogOrigin.h = (short)(*env)->GetShortField(env, lpObject, ColorPickerInfoFc.h);
	lpStruct->dialogOrigin.v = (short)(*env)->GetShortField(env, lpObject, ColorPickerInfoFc.v);
	lpStruct->pickerType = (OSType)(*env)->GetIntField(env, lpObject, ColorPickerInfoFc.pickerType);
	lpStruct->eventProc = (UserEventUPP)(*env)->GetIntField(env, lpObject, ColorPickerInfoFc.eventProc);
	lpStruct->colorProc = (ColorChangedUPP)(*env)->GetIntField(env, lpObject, ColorPickerInfoFc.colorProc);
	lpStruct->colorProcData = (UInt32)(*env)->GetIntField(env, lpObject, ColorPickerInfoFc.colorProcData);
	{
	jbyteArray lpObject1 = (jbyteArray)(*env)->GetObjectField(env, lpObject, ColorPickerInfoFc.prompt);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->prompt), (jbyte *)lpStruct->prompt);
	}
	lpStruct->mInfo.editMenuID = (SInt16)(*env)->GetShortField(env, lpObject, ColorPickerInfoFc.editMenuID);
	lpStruct->mInfo.cutItem = (SInt16)(*env)->GetShortField(env, lpObject, ColorPickerInfoFc.cutItem);
	lpStruct->mInfo.copyItem = (SInt16)(*env)->GetShortField(env, lpObject, ColorPickerInfoFc.copyItem);
	lpStruct->mInfo.pasteItem = (SInt16)(*env)->GetShortField(env, lpObject, ColorPickerInfoFc.pasteItem);
	lpStruct->mInfo.clearItem = (SInt16)(*env)->GetShortField(env, lpObject, ColorPickerInfoFc.clearItem);
	lpStruct->mInfo.undoItem = (SInt16)(*env)->GetShortField(env, lpObject, ColorPickerInfoFc.undoItem);
	lpStruct->newColorChosen = (Boolean)(*env)->GetBooleanField(env, lpObject, ColorPickerInfoFc.newColorChosen);
	return lpStruct;
}

void setColorPickerInfoFields(JNIEnv *env, jobject lpObject, ColorPickerInfo *lpStruct)
{
	if (!ColorPickerInfoFc.cached) cacheColorPickerInfoFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, ColorPickerInfoFc.profile, (jint)lpStruct->theColor.profile);
	(*env)->SetShortField(env, lpObject, ColorPickerInfoFc.red, (jshort)lpStruct->theColor.color.rgb.red);
	(*env)->SetShortField(env, lpObject, ColorPickerInfoFc.green, (jshort)lpStruct->theColor.color.rgb.green);
	(*env)->SetShortField(env, lpObject, ColorPickerInfoFc.blue, (jshort)lpStruct->theColor.color.rgb.blue);
	(*env)->SetIntField(env, lpObject, ColorPickerInfoFc.dstProfile, (jint)lpStruct->dstProfile);
	(*env)->SetIntField(env, lpObject, ColorPickerInfoFc.flags, (jint)lpStruct->flags);
	(*env)->SetShortField(env, lpObject, ColorPickerInfoFc.placeWhere, (jshort)lpStruct->placeWhere);
	(*env)->SetShortField(env, lpObject, ColorPickerInfoFc.h, (jshort)lpStruct->dialogOrigin.h);
	(*env)->SetShortField(env, lpObject, ColorPickerInfoFc.v, (jshort)lpStruct->dialogOrigin.v);
	(*env)->SetIntField(env, lpObject, ColorPickerInfoFc.pickerType, (jint)lpStruct->pickerType);
	(*env)->SetIntField(env, lpObject, ColorPickerInfoFc.eventProc, (jint)lpStruct->eventProc);
	(*env)->SetIntField(env, lpObject, ColorPickerInfoFc.colorProc, (jint)lpStruct->colorProc);
	(*env)->SetIntField(env, lpObject, ColorPickerInfoFc.colorProcData, (jint)lpStruct->colorProcData);
	{
	jbyteArray lpObject1 = (jbyteArray)(*env)->GetObjectField(env, lpObject, ColorPickerInfoFc.prompt);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->prompt), (jbyte *)lpStruct->prompt);
	}
	(*env)->SetShortField(env, lpObject, ColorPickerInfoFc.editMenuID, (jshort)lpStruct->mInfo.editMenuID);
	(*env)->SetShortField(env, lpObject, ColorPickerInfoFc.cutItem, (jshort)lpStruct->mInfo.cutItem);
	(*env)->SetShortField(env, lpObject, ColorPickerInfoFc.copyItem, (jshort)lpStruct->mInfo.copyItem);
	(*env)->SetShortField(env, lpObject, ColorPickerInfoFc.pasteItem, (jshort)lpStruct->mInfo.pasteItem);
	(*env)->SetShortField(env, lpObject, ColorPickerInfoFc.clearItem, (jshort)lpStruct->mInfo.clearItem);
	(*env)->SetShortField(env, lpObject, ColorPickerInfoFc.undoItem, (jshort)lpStruct->mInfo.undoItem);
	(*env)->SetBooleanField(env, lpObject, ColorPickerInfoFc.newColorChosen, (jboolean)lpStruct->newColorChosen);
}
#endif

#ifndef NO_ControlButtonContentInfo
typedef struct ControlButtonContentInfo_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID contentType, iconRef;
} ControlButtonContentInfo_FID_CACHE;

ControlButtonContentInfo_FID_CACHE ControlButtonContentInfoFc;

void cacheControlButtonContentInfoFields(JNIEnv *env, jobject lpObject)
{
	if (ControlButtonContentInfoFc.cached) return;
	ControlButtonContentInfoFc.clazz = (*env)->GetObjectClass(env, lpObject);
	ControlButtonContentInfoFc.contentType = (*env)->GetFieldID(env, ControlButtonContentInfoFc.clazz, "contentType", "S");
	ControlButtonContentInfoFc.iconRef = (*env)->GetFieldID(env, ControlButtonContentInfoFc.clazz, "iconRef", "I");
	ControlButtonContentInfoFc.cached = 1;
}

ControlButtonContentInfo *getControlButtonContentInfoFields(JNIEnv *env, jobject lpObject, ControlButtonContentInfo *lpStruct)
{
	if (!ControlButtonContentInfoFc.cached) cacheControlButtonContentInfoFields(env, lpObject);
	lpStruct->contentType = (ControlContentType)(*env)->GetShortField(env, lpObject, ControlButtonContentInfoFc.contentType);
	lpStruct->u.iconRef = (void *)(*env)->GetIntField(env, lpObject, ControlButtonContentInfoFc.iconRef);
	return lpStruct;
}

void setControlButtonContentInfoFields(JNIEnv *env, jobject lpObject, ControlButtonContentInfo *lpStruct)
{
	if (!ControlButtonContentInfoFc.cached) cacheControlButtonContentInfoFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, ControlButtonContentInfoFc.contentType, (jshort)lpStruct->contentType);
	(*env)->SetIntField(env, lpObject, ControlButtonContentInfoFc.iconRef, (jint)lpStruct->u.iconRef);
}
#endif

#ifndef NO_ControlEditTextSelectionRec
typedef struct ControlEditTextSelectionRec_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID selStart, selEnd;
} ControlEditTextSelectionRec_FID_CACHE;

ControlEditTextSelectionRec_FID_CACHE ControlEditTextSelectionRecFc;

void cacheControlEditTextSelectionRecFields(JNIEnv *env, jobject lpObject)
{
	if (ControlEditTextSelectionRecFc.cached) return;
	ControlEditTextSelectionRecFc.clazz = (*env)->GetObjectClass(env, lpObject);
	ControlEditTextSelectionRecFc.selStart = (*env)->GetFieldID(env, ControlEditTextSelectionRecFc.clazz, "selStart", "S");
	ControlEditTextSelectionRecFc.selEnd = (*env)->GetFieldID(env, ControlEditTextSelectionRecFc.clazz, "selEnd", "S");
	ControlEditTextSelectionRecFc.cached = 1;
}

ControlEditTextSelectionRec *getControlEditTextSelectionRecFields(JNIEnv *env, jobject lpObject, ControlEditTextSelectionRec *lpStruct)
{
	if (!ControlEditTextSelectionRecFc.cached) cacheControlEditTextSelectionRecFields(env, lpObject);
	lpStruct->selStart = (*env)->GetShortField(env, lpObject, ControlEditTextSelectionRecFc.selStart);
	lpStruct->selEnd = (*env)->GetShortField(env, lpObject, ControlEditTextSelectionRecFc.selEnd);
	return lpStruct;
}

void setControlEditTextSelectionRecFields(JNIEnv *env, jobject lpObject, ControlEditTextSelectionRec *lpStruct)
{
	if (!ControlEditTextSelectionRecFc.cached) cacheControlEditTextSelectionRecFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, ControlEditTextSelectionRecFc.selStart, (jshort)lpStruct->selStart);
	(*env)->SetShortField(env, lpObject, ControlEditTextSelectionRecFc.selEnd, (jshort)lpStruct->selEnd);
}
#endif

#ifndef NO_ControlFontStyleRec
typedef struct ControlFontStyleRec_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID flags, font, size, style, mode, just, foreColor_red, foreColor_green, foreColor_blue, backColor_red, backColor_green, backColor_blue;
} ControlFontStyleRec_FID_CACHE;

ControlFontStyleRec_FID_CACHE ControlFontStyleRecFc;

void cacheControlFontStyleRecFields(JNIEnv *env, jobject lpObject)
{
	if (ControlFontStyleRecFc.cached) return;
	ControlFontStyleRecFc.clazz = (*env)->GetObjectClass(env, lpObject);
	ControlFontStyleRecFc.flags = (*env)->GetFieldID(env, ControlFontStyleRecFc.clazz, "flags", "S");
	ControlFontStyleRecFc.font = (*env)->GetFieldID(env, ControlFontStyleRecFc.clazz, "font", "S");
	ControlFontStyleRecFc.size = (*env)->GetFieldID(env, ControlFontStyleRecFc.clazz, "size", "S");
	ControlFontStyleRecFc.style = (*env)->GetFieldID(env, ControlFontStyleRecFc.clazz, "style", "S");
	ControlFontStyleRecFc.mode = (*env)->GetFieldID(env, ControlFontStyleRecFc.clazz, "mode", "S");
	ControlFontStyleRecFc.just = (*env)->GetFieldID(env, ControlFontStyleRecFc.clazz, "just", "S");
	ControlFontStyleRecFc.foreColor_red = (*env)->GetFieldID(env, ControlFontStyleRecFc.clazz, "foreColor_red", "S");
	ControlFontStyleRecFc.foreColor_green = (*env)->GetFieldID(env, ControlFontStyleRecFc.clazz, "foreColor_green", "S");
	ControlFontStyleRecFc.foreColor_blue = (*env)->GetFieldID(env, ControlFontStyleRecFc.clazz, "foreColor_blue", "S");
	ControlFontStyleRecFc.backColor_red = (*env)->GetFieldID(env, ControlFontStyleRecFc.clazz, "backColor_red", "S");
	ControlFontStyleRecFc.backColor_green = (*env)->GetFieldID(env, ControlFontStyleRecFc.clazz, "backColor_green", "S");
	ControlFontStyleRecFc.backColor_blue = (*env)->GetFieldID(env, ControlFontStyleRecFc.clazz, "backColor_blue", "S");
	ControlFontStyleRecFc.cached = 1;
}

ControlFontStyleRec *getControlFontStyleRecFields(JNIEnv *env, jobject lpObject, ControlFontStyleRec *lpStruct)
{
	if (!ControlFontStyleRecFc.cached) cacheControlFontStyleRecFields(env, lpObject);
	lpStruct->flags = (*env)->GetShortField(env, lpObject, ControlFontStyleRecFc.flags);
	lpStruct->font = (*env)->GetShortField(env, lpObject, ControlFontStyleRecFc.font);
	lpStruct->size = (*env)->GetShortField(env, lpObject, ControlFontStyleRecFc.size);
	lpStruct->style = (*env)->GetShortField(env, lpObject, ControlFontStyleRecFc.style);
	lpStruct->mode = (*env)->GetShortField(env, lpObject, ControlFontStyleRecFc.mode);
	lpStruct->just = (*env)->GetShortField(env, lpObject, ControlFontStyleRecFc.just);
	lpStruct->foreColor.red = (*env)->GetShortField(env, lpObject, ControlFontStyleRecFc.foreColor_red);
	lpStruct->foreColor.green = (*env)->GetShortField(env, lpObject, ControlFontStyleRecFc.foreColor_green);
	lpStruct->foreColor.blue = (*env)->GetShortField(env, lpObject, ControlFontStyleRecFc.foreColor_blue);
	lpStruct->backColor.red = (*env)->GetShortField(env, lpObject, ControlFontStyleRecFc.backColor_red);
	lpStruct->backColor.green = (*env)->GetShortField(env, lpObject, ControlFontStyleRecFc.backColor_green);
	lpStruct->backColor.blue = (*env)->GetShortField(env, lpObject, ControlFontStyleRecFc.backColor_blue);
	return lpStruct;
}

void setControlFontStyleRecFields(JNIEnv *env, jobject lpObject, ControlFontStyleRec *lpStruct)
{
	if (!ControlFontStyleRecFc.cached) cacheControlFontStyleRecFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, ControlFontStyleRecFc.flags, (jshort)lpStruct->flags);
	(*env)->SetShortField(env, lpObject, ControlFontStyleRecFc.font, (jshort)lpStruct->font);
	(*env)->SetShortField(env, lpObject, ControlFontStyleRecFc.size, (jshort)lpStruct->size);
	(*env)->SetShortField(env, lpObject, ControlFontStyleRecFc.style, (jshort)lpStruct->style);
	(*env)->SetShortField(env, lpObject, ControlFontStyleRecFc.mode, (jshort)lpStruct->mode);
	(*env)->SetShortField(env, lpObject, ControlFontStyleRecFc.just, (jshort)lpStruct->just);
	(*env)->SetShortField(env, lpObject, ControlFontStyleRecFc.foreColor_red, (jshort)lpStruct->foreColor.red);
	(*env)->SetShortField(env, lpObject, ControlFontStyleRecFc.foreColor_green, (jshort)lpStruct->foreColor.green);
	(*env)->SetShortField(env, lpObject, ControlFontStyleRecFc.foreColor_blue, (jshort)lpStruct->foreColor.blue);
	(*env)->SetShortField(env, lpObject, ControlFontStyleRecFc.backColor_red, (jshort)lpStruct->backColor.red);
	(*env)->SetShortField(env, lpObject, ControlFontStyleRecFc.backColor_green, (jshort)lpStruct->backColor.green);
	(*env)->SetShortField(env, lpObject, ControlFontStyleRecFc.backColor_blue, (jshort)lpStruct->backColor.blue);
}
#endif

#ifndef NO_ControlKind
typedef struct ControlKind_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID signature, kind;
} ControlKind_FID_CACHE;

ControlKind_FID_CACHE ControlKindFc;

void cacheControlKindFields(JNIEnv *env, jobject lpObject)
{
	if (ControlKindFc.cached) return;
	ControlKindFc.clazz = (*env)->GetObjectClass(env, lpObject);
	ControlKindFc.signature = (*env)->GetFieldID(env, ControlKindFc.clazz, "signature", "I");
	ControlKindFc.kind = (*env)->GetFieldID(env, ControlKindFc.clazz, "kind", "I");
	ControlKindFc.cached = 1;
}

ControlKind *getControlKindFields(JNIEnv *env, jobject lpObject, ControlKind *lpStruct)
{
	if (!ControlKindFc.cached) cacheControlKindFields(env, lpObject);
	lpStruct->signature = (*env)->GetIntField(env, lpObject, ControlKindFc.signature);
	lpStruct->kind = (*env)->GetIntField(env, lpObject, ControlKindFc.kind);
	return lpStruct;
}

void setControlKindFields(JNIEnv *env, jobject lpObject, ControlKind *lpStruct)
{
	if (!ControlKindFc.cached) cacheControlKindFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, ControlKindFc.signature, (jint)lpStruct->signature);
	(*env)->SetIntField(env, lpObject, ControlKindFc.kind, (jint)lpStruct->kind);
}
#endif

#ifndef NO_ControlTabEntry
typedef struct ControlTabEntry_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID icon, name, enabled;
} ControlTabEntry_FID_CACHE;

ControlTabEntry_FID_CACHE ControlTabEntryFc;

void cacheControlTabEntryFields(JNIEnv *env, jobject lpObject)
{
	if (ControlTabEntryFc.cached) return;
	ControlTabEntryFc.clazz = (*env)->GetObjectClass(env, lpObject);
	ControlTabEntryFc.icon = (*env)->GetFieldID(env, ControlTabEntryFc.clazz, "icon", "I");
	ControlTabEntryFc.name = (*env)->GetFieldID(env, ControlTabEntryFc.clazz, "name", "I");
	ControlTabEntryFc.enabled = (*env)->GetFieldID(env, ControlTabEntryFc.clazz, "enabled", "Z");
	ControlTabEntryFc.cached = 1;
}

ControlTabEntry *getControlTabEntryFields(JNIEnv *env, jobject lpObject, ControlTabEntry *lpStruct)
{
	if (!ControlTabEntryFc.cached) cacheControlTabEntryFields(env, lpObject);
	lpStruct->icon = (ControlButtonContentInfo *)(*env)->GetIntField(env, lpObject, ControlTabEntryFc.icon);
	lpStruct->name = (CFStringRef)(*env)->GetIntField(env, lpObject, ControlTabEntryFc.name);
	lpStruct->enabled = (Boolean)(*env)->GetBooleanField(env, lpObject, ControlTabEntryFc.enabled);
	return lpStruct;
}

void setControlTabEntryFields(JNIEnv *env, jobject lpObject, ControlTabEntry *lpStruct)
{
	if (!ControlTabEntryFc.cached) cacheControlTabEntryFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, ControlTabEntryFc.icon, (jint)lpStruct->icon);
	(*env)->SetIntField(env, lpObject, ControlTabEntryFc.name, (jint)lpStruct->name);
	(*env)->SetBooleanField(env, lpObject, ControlTabEntryFc.enabled, (jboolean)lpStruct->enabled);
}
#endif

#ifndef NO_ControlTabInfoRecV1
typedef struct ControlTabInfoRecV1_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID version, iconSuiteID, name;
} ControlTabInfoRecV1_FID_CACHE;

ControlTabInfoRecV1_FID_CACHE ControlTabInfoRecV1Fc;

void cacheControlTabInfoRecV1Fields(JNIEnv *env, jobject lpObject)
{
	if (ControlTabInfoRecV1Fc.cached) return;
	ControlTabInfoRecV1Fc.clazz = (*env)->GetObjectClass(env, lpObject);
	ControlTabInfoRecV1Fc.version = (*env)->GetFieldID(env, ControlTabInfoRecV1Fc.clazz, "version", "S");
	ControlTabInfoRecV1Fc.iconSuiteID = (*env)->GetFieldID(env, ControlTabInfoRecV1Fc.clazz, "iconSuiteID", "S");
	ControlTabInfoRecV1Fc.name = (*env)->GetFieldID(env, ControlTabInfoRecV1Fc.clazz, "name", "I");
	ControlTabInfoRecV1Fc.cached = 1;
}

ControlTabInfoRecV1 *getControlTabInfoRecV1Fields(JNIEnv *env, jobject lpObject, ControlTabInfoRecV1 *lpStruct)
{
	if (!ControlTabInfoRecV1Fc.cached) cacheControlTabInfoRecV1Fields(env, lpObject);
	lpStruct->version = (SInt16)(*env)->GetShortField(env, lpObject, ControlTabInfoRecV1Fc.version);
	lpStruct->iconSuiteID = (SInt16)(*env)->GetShortField(env, lpObject, ControlTabInfoRecV1Fc.iconSuiteID);
	lpStruct->name = (CFStringRef)(*env)->GetIntField(env, lpObject, ControlTabInfoRecV1Fc.name);
	return lpStruct;
}

void setControlTabInfoRecV1Fields(JNIEnv *env, jobject lpObject, ControlTabInfoRecV1 *lpStruct)
{
	if (!ControlTabInfoRecV1Fc.cached) cacheControlTabInfoRecV1Fields(env, lpObject);
	(*env)->SetShortField(env, lpObject, ControlTabInfoRecV1Fc.version, (jshort)lpStruct->version);
	(*env)->SetShortField(env, lpObject, ControlTabInfoRecV1Fc.iconSuiteID, (jshort)lpStruct->iconSuiteID);
	(*env)->SetIntField(env, lpObject, ControlTabInfoRecV1Fc.name, (jint)lpStruct->name);
}
#endif

#ifndef NO_Cursor
typedef struct Cursor_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID data, mask, hotSpot_v, hotSpot_h;
} Cursor_FID_CACHE;

Cursor_FID_CACHE CursorFc;

void cacheCursorFields(JNIEnv *env, jobject lpObject)
{
	if (CursorFc.cached) return;
	CursorFc.clazz = (*env)->GetObjectClass(env, lpObject);
	CursorFc.data = (*env)->GetFieldID(env, CursorFc.clazz, "data", "[B");
	CursorFc.mask = (*env)->GetFieldID(env, CursorFc.clazz, "mask", "[B");
	CursorFc.hotSpot_v = (*env)->GetFieldID(env, CursorFc.clazz, "hotSpot_v", "S");
	CursorFc.hotSpot_h = (*env)->GetFieldID(env, CursorFc.clazz, "hotSpot_h", "S");
	CursorFc.cached = 1;
}

Cursor *getCursorFields(JNIEnv *env, jobject lpObject, Cursor *lpStruct)
{
	if (!CursorFc.cached) cacheCursorFields(env, lpObject);
	{
	jbyteArray lpObject1 = (jbyteArray)(*env)->GetObjectField(env, lpObject, CursorFc.data);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->data), (jbyte *)lpStruct->data);
	}
	{
	jbyteArray lpObject1 = (jbyteArray)(*env)->GetObjectField(env, lpObject, CursorFc.mask);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->mask), (jbyte *)lpStruct->mask);
	}
	lpStruct->hotSpot.v = (*env)->GetShortField(env, lpObject, CursorFc.hotSpot_v);
	lpStruct->hotSpot.h = (*env)->GetShortField(env, lpObject, CursorFc.hotSpot_h);
	return lpStruct;
}

void setCursorFields(JNIEnv *env, jobject lpObject, Cursor *lpStruct)
{
	if (!CursorFc.cached) cacheCursorFields(env, lpObject);
	{
	jbyteArray lpObject1 = (jbyteArray)(*env)->GetObjectField(env, lpObject, CursorFc.data);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->data), (jbyte *)lpStruct->data);
	}
	{
	jbyteArray lpObject1 = (jbyteArray)(*env)->GetObjectField(env, lpObject, CursorFc.mask);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->mask), (jbyte *)lpStruct->mask);
	}
	(*env)->SetShortField(env, lpObject, CursorFc.hotSpot_v, (jshort)lpStruct->hotSpot.v);
	(*env)->SetShortField(env, lpObject, CursorFc.hotSpot_h, (jshort)lpStruct->hotSpot.h);
}
#endif

#ifndef NO_DataBrowserAccessibilityItemInfo
typedef struct DataBrowserAccessibilityItemInfo_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID version, v0_container, v0_item, v0_columnProperty, v0_propertyPart;
} DataBrowserAccessibilityItemInfo_FID_CACHE;

DataBrowserAccessibilityItemInfo_FID_CACHE DataBrowserAccessibilityItemInfoFc;

void cacheDataBrowserAccessibilityItemInfoFields(JNIEnv *env, jobject lpObject)
{
	if (DataBrowserAccessibilityItemInfoFc.cached) return;
	DataBrowserAccessibilityItemInfoFc.clazz = (*env)->GetObjectClass(env, lpObject);
	DataBrowserAccessibilityItemInfoFc.version = (*env)->GetFieldID(env, DataBrowserAccessibilityItemInfoFc.clazz, "version", "I");
	DataBrowserAccessibilityItemInfoFc.v0_container = (*env)->GetFieldID(env, DataBrowserAccessibilityItemInfoFc.clazz, "v0_container", "I");
	DataBrowserAccessibilityItemInfoFc.v0_item = (*env)->GetFieldID(env, DataBrowserAccessibilityItemInfoFc.clazz, "v0_item", "I");
	DataBrowserAccessibilityItemInfoFc.v0_columnProperty = (*env)->GetFieldID(env, DataBrowserAccessibilityItemInfoFc.clazz, "v0_columnProperty", "I");
	DataBrowserAccessibilityItemInfoFc.v0_propertyPart = (*env)->GetFieldID(env, DataBrowserAccessibilityItemInfoFc.clazz, "v0_propertyPart", "I");
	DataBrowserAccessibilityItemInfoFc.cached = 1;
}

DataBrowserAccessibilityItemInfo *getDataBrowserAccessibilityItemInfoFields(JNIEnv *env, jobject lpObject, DataBrowserAccessibilityItemInfo *lpStruct)
{
	if (!DataBrowserAccessibilityItemInfoFc.cached) cacheDataBrowserAccessibilityItemInfoFields(env, lpObject);
	lpStruct->version = (UInt32)(*env)->GetIntField(env, lpObject, DataBrowserAccessibilityItemInfoFc.version);
	lpStruct->u.v0.container = (DataBrowserItemID)(*env)->GetIntField(env, lpObject, DataBrowserAccessibilityItemInfoFc.v0_container);
	lpStruct->u.v0.item = (DataBrowserItemID)(*env)->GetIntField(env, lpObject, DataBrowserAccessibilityItemInfoFc.v0_item);
	lpStruct->u.v0.columnProperty = (DataBrowserPropertyID)(*env)->GetIntField(env, lpObject, DataBrowserAccessibilityItemInfoFc.v0_columnProperty);
	lpStruct->u.v0.propertyPart = (DataBrowserPropertyPart)(*env)->GetIntField(env, lpObject, DataBrowserAccessibilityItemInfoFc.v0_propertyPart);
	return lpStruct;
}

void setDataBrowserAccessibilityItemInfoFields(JNIEnv *env, jobject lpObject, DataBrowserAccessibilityItemInfo *lpStruct)
{
	if (!DataBrowserAccessibilityItemInfoFc.cached) cacheDataBrowserAccessibilityItemInfoFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, DataBrowserAccessibilityItemInfoFc.version, (jint)lpStruct->version);
	(*env)->SetIntField(env, lpObject, DataBrowserAccessibilityItemInfoFc.v0_container, (jint)lpStruct->u.v0.container);
	(*env)->SetIntField(env, lpObject, DataBrowserAccessibilityItemInfoFc.v0_item, (jint)lpStruct->u.v0.item);
	(*env)->SetIntField(env, lpObject, DataBrowserAccessibilityItemInfoFc.v0_columnProperty, (jint)lpStruct->u.v0.columnProperty);
	(*env)->SetIntField(env, lpObject, DataBrowserAccessibilityItemInfoFc.v0_propertyPart, (jint)lpStruct->u.v0.propertyPart);
}
#endif

#ifndef NO_DataBrowserCallbacks
typedef struct DataBrowserCallbacks_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID version, v1_itemDataCallback, v1_itemCompareCallback, v1_itemNotificationCallback, v1_addDragItemCallback, v1_acceptDragCallback, v1_receiveDragCallback, v1_postProcessDragCallback, v1_itemHelpContentCallback, v1_getContextualMenuCallback, v1_selectContextualMenuCallback;
} DataBrowserCallbacks_FID_CACHE;

DataBrowserCallbacks_FID_CACHE DataBrowserCallbacksFc;

void cacheDataBrowserCallbacksFields(JNIEnv *env, jobject lpObject)
{
	if (DataBrowserCallbacksFc.cached) return;
	DataBrowserCallbacksFc.clazz = (*env)->GetObjectClass(env, lpObject);
	DataBrowserCallbacksFc.version = (*env)->GetFieldID(env, DataBrowserCallbacksFc.clazz, "version", "I");
	DataBrowserCallbacksFc.v1_itemDataCallback = (*env)->GetFieldID(env, DataBrowserCallbacksFc.clazz, "v1_itemDataCallback", "I");
	DataBrowserCallbacksFc.v1_itemCompareCallback = (*env)->GetFieldID(env, DataBrowserCallbacksFc.clazz, "v1_itemCompareCallback", "I");
	DataBrowserCallbacksFc.v1_itemNotificationCallback = (*env)->GetFieldID(env, DataBrowserCallbacksFc.clazz, "v1_itemNotificationCallback", "I");
	DataBrowserCallbacksFc.v1_addDragItemCallback = (*env)->GetFieldID(env, DataBrowserCallbacksFc.clazz, "v1_addDragItemCallback", "I");
	DataBrowserCallbacksFc.v1_acceptDragCallback = (*env)->GetFieldID(env, DataBrowserCallbacksFc.clazz, "v1_acceptDragCallback", "I");
	DataBrowserCallbacksFc.v1_receiveDragCallback = (*env)->GetFieldID(env, DataBrowserCallbacksFc.clazz, "v1_receiveDragCallback", "I");
	DataBrowserCallbacksFc.v1_postProcessDragCallback = (*env)->GetFieldID(env, DataBrowserCallbacksFc.clazz, "v1_postProcessDragCallback", "I");
	DataBrowserCallbacksFc.v1_itemHelpContentCallback = (*env)->GetFieldID(env, DataBrowserCallbacksFc.clazz, "v1_itemHelpContentCallback", "I");
	DataBrowserCallbacksFc.v1_getContextualMenuCallback = (*env)->GetFieldID(env, DataBrowserCallbacksFc.clazz, "v1_getContextualMenuCallback", "I");
	DataBrowserCallbacksFc.v1_selectContextualMenuCallback = (*env)->GetFieldID(env, DataBrowserCallbacksFc.clazz, "v1_selectContextualMenuCallback", "I");
	DataBrowserCallbacksFc.cached = 1;
}

DataBrowserCallbacks *getDataBrowserCallbacksFields(JNIEnv *env, jobject lpObject, DataBrowserCallbacks *lpStruct)
{
	if (!DataBrowserCallbacksFc.cached) cacheDataBrowserCallbacksFields(env, lpObject);
	lpStruct->version = (UInt32)(*env)->GetIntField(env, lpObject, DataBrowserCallbacksFc.version);
	lpStruct->u.v1.itemDataCallback = (DataBrowserItemDataUPP)(*env)->GetIntField(env, lpObject, DataBrowserCallbacksFc.v1_itemDataCallback);
	lpStruct->u.v1.itemCompareCallback = (DataBrowserItemCompareUPP)(*env)->GetIntField(env, lpObject, DataBrowserCallbacksFc.v1_itemCompareCallback);
	lpStruct->u.v1.itemNotificationCallback = (DataBrowserItemNotificationUPP)(*env)->GetIntField(env, lpObject, DataBrowserCallbacksFc.v1_itemNotificationCallback);
	lpStruct->u.v1.addDragItemCallback = (DataBrowserAddDragItemUPP)(*env)->GetIntField(env, lpObject, DataBrowserCallbacksFc.v1_addDragItemCallback);
	lpStruct->u.v1.acceptDragCallback = (DataBrowserAcceptDragUPP)(*env)->GetIntField(env, lpObject, DataBrowserCallbacksFc.v1_acceptDragCallback);
	lpStruct->u.v1.receiveDragCallback = (DataBrowserReceiveDragUPP)(*env)->GetIntField(env, lpObject, DataBrowserCallbacksFc.v1_receiveDragCallback);
	lpStruct->u.v1.postProcessDragCallback = (DataBrowserPostProcessDragUPP)(*env)->GetIntField(env, lpObject, DataBrowserCallbacksFc.v1_postProcessDragCallback);
	lpStruct->u.v1.itemHelpContentCallback = (DataBrowserItemHelpContentUPP)(*env)->GetIntField(env, lpObject, DataBrowserCallbacksFc.v1_itemHelpContentCallback);
	lpStruct->u.v1.getContextualMenuCallback = (DataBrowserGetContextualMenuUPP)(*env)->GetIntField(env, lpObject, DataBrowserCallbacksFc.v1_getContextualMenuCallback);
	lpStruct->u.v1.selectContextualMenuCallback = (DataBrowserSelectContextualMenuUPP)(*env)->GetIntField(env, lpObject, DataBrowserCallbacksFc.v1_selectContextualMenuCallback);
	return lpStruct;
}

void setDataBrowserCallbacksFields(JNIEnv *env, jobject lpObject, DataBrowserCallbacks *lpStruct)
{
	if (!DataBrowserCallbacksFc.cached) cacheDataBrowserCallbacksFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, DataBrowserCallbacksFc.version, (jint)lpStruct->version);
	(*env)->SetIntField(env, lpObject, DataBrowserCallbacksFc.v1_itemDataCallback, (jint)lpStruct->u.v1.itemDataCallback);
	(*env)->SetIntField(env, lpObject, DataBrowserCallbacksFc.v1_itemCompareCallback, (jint)lpStruct->u.v1.itemCompareCallback);
	(*env)->SetIntField(env, lpObject, DataBrowserCallbacksFc.v1_itemNotificationCallback, (jint)lpStruct->u.v1.itemNotificationCallback);
	(*env)->SetIntField(env, lpObject, DataBrowserCallbacksFc.v1_addDragItemCallback, (jint)lpStruct->u.v1.addDragItemCallback);
	(*env)->SetIntField(env, lpObject, DataBrowserCallbacksFc.v1_acceptDragCallback, (jint)lpStruct->u.v1.acceptDragCallback);
	(*env)->SetIntField(env, lpObject, DataBrowserCallbacksFc.v1_receiveDragCallback, (jint)lpStruct->u.v1.receiveDragCallback);
	(*env)->SetIntField(env, lpObject, DataBrowserCallbacksFc.v1_postProcessDragCallback, (jint)lpStruct->u.v1.postProcessDragCallback);
	(*env)->SetIntField(env, lpObject, DataBrowserCallbacksFc.v1_itemHelpContentCallback, (jint)lpStruct->u.v1.itemHelpContentCallback);
	(*env)->SetIntField(env, lpObject, DataBrowserCallbacksFc.v1_getContextualMenuCallback, (jint)lpStruct->u.v1.getContextualMenuCallback);
	(*env)->SetIntField(env, lpObject, DataBrowserCallbacksFc.v1_selectContextualMenuCallback, (jint)lpStruct->u.v1.selectContextualMenuCallback);
}
#endif

#ifndef NO_DataBrowserCustomCallbacks
typedef struct DataBrowserCustomCallbacks_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID version, v1_drawItemCallback, v1_editTextCallback, v1_hitTestCallback, v1_trackingCallback, v1_dragRegionCallback, v1_acceptDragCallback, v1_receiveDragCallback;
} DataBrowserCustomCallbacks_FID_CACHE;

DataBrowserCustomCallbacks_FID_CACHE DataBrowserCustomCallbacksFc;

void cacheDataBrowserCustomCallbacksFields(JNIEnv *env, jobject lpObject)
{
	if (DataBrowserCustomCallbacksFc.cached) return;
	DataBrowserCustomCallbacksFc.clazz = (*env)->GetObjectClass(env, lpObject);
	DataBrowserCustomCallbacksFc.version = (*env)->GetFieldID(env, DataBrowserCustomCallbacksFc.clazz, "version", "I");
	DataBrowserCustomCallbacksFc.v1_drawItemCallback = (*env)->GetFieldID(env, DataBrowserCustomCallbacksFc.clazz, "v1_drawItemCallback", "I");
	DataBrowserCustomCallbacksFc.v1_editTextCallback = (*env)->GetFieldID(env, DataBrowserCustomCallbacksFc.clazz, "v1_editTextCallback", "I");
	DataBrowserCustomCallbacksFc.v1_hitTestCallback = (*env)->GetFieldID(env, DataBrowserCustomCallbacksFc.clazz, "v1_hitTestCallback", "I");
	DataBrowserCustomCallbacksFc.v1_trackingCallback = (*env)->GetFieldID(env, DataBrowserCustomCallbacksFc.clazz, "v1_trackingCallback", "I");
	DataBrowserCustomCallbacksFc.v1_dragRegionCallback = (*env)->GetFieldID(env, DataBrowserCustomCallbacksFc.clazz, "v1_dragRegionCallback", "I");
	DataBrowserCustomCallbacksFc.v1_acceptDragCallback = (*env)->GetFieldID(env, DataBrowserCustomCallbacksFc.clazz, "v1_acceptDragCallback", "I");
	DataBrowserCustomCallbacksFc.v1_receiveDragCallback = (*env)->GetFieldID(env, DataBrowserCustomCallbacksFc.clazz, "v1_receiveDragCallback", "I");
	DataBrowserCustomCallbacksFc.cached = 1;
}

DataBrowserCustomCallbacks *getDataBrowserCustomCallbacksFields(JNIEnv *env, jobject lpObject, DataBrowserCustomCallbacks *lpStruct)
{
	if (!DataBrowserCustomCallbacksFc.cached) cacheDataBrowserCustomCallbacksFields(env, lpObject);
	lpStruct->version = (*env)->GetIntField(env, lpObject, DataBrowserCustomCallbacksFc.version);
	lpStruct->u.v1.drawItemCallback = (DataBrowserDrawItemUPP)(*env)->GetIntField(env, lpObject, DataBrowserCustomCallbacksFc.v1_drawItemCallback);
	lpStruct->u.v1.editTextCallback = (DataBrowserEditItemUPP)(*env)->GetIntField(env, lpObject, DataBrowserCustomCallbacksFc.v1_editTextCallback);
	lpStruct->u.v1.hitTestCallback = (DataBrowserHitTestUPP)(*env)->GetIntField(env, lpObject, DataBrowserCustomCallbacksFc.v1_hitTestCallback);
	lpStruct->u.v1.trackingCallback = (DataBrowserTrackingUPP)(*env)->GetIntField(env, lpObject, DataBrowserCustomCallbacksFc.v1_trackingCallback);
	lpStruct->u.v1.dragRegionCallback = (DataBrowserItemDragRgnUPP)(*env)->GetIntField(env, lpObject, DataBrowserCustomCallbacksFc.v1_dragRegionCallback);
	lpStruct->u.v1.acceptDragCallback = (DataBrowserItemAcceptDragUPP)(*env)->GetIntField(env, lpObject, DataBrowserCustomCallbacksFc.v1_acceptDragCallback);
	lpStruct->u.v1.receiveDragCallback = (DataBrowserItemReceiveDragUPP)(*env)->GetIntField(env, lpObject, DataBrowserCustomCallbacksFc.v1_receiveDragCallback);
	return lpStruct;
}

void setDataBrowserCustomCallbacksFields(JNIEnv *env, jobject lpObject, DataBrowserCustomCallbacks *lpStruct)
{
	if (!DataBrowserCustomCallbacksFc.cached) cacheDataBrowserCustomCallbacksFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, DataBrowserCustomCallbacksFc.version, (jint)lpStruct->version);
	(*env)->SetIntField(env, lpObject, DataBrowserCustomCallbacksFc.v1_drawItemCallback, (jint)lpStruct->u.v1.drawItemCallback);
	(*env)->SetIntField(env, lpObject, DataBrowserCustomCallbacksFc.v1_editTextCallback, (jint)lpStruct->u.v1.editTextCallback);
	(*env)->SetIntField(env, lpObject, DataBrowserCustomCallbacksFc.v1_hitTestCallback, (jint)lpStruct->u.v1.hitTestCallback);
	(*env)->SetIntField(env, lpObject, DataBrowserCustomCallbacksFc.v1_trackingCallback, (jint)lpStruct->u.v1.trackingCallback);
	(*env)->SetIntField(env, lpObject, DataBrowserCustomCallbacksFc.v1_dragRegionCallback, (jint)lpStruct->u.v1.dragRegionCallback);
	(*env)->SetIntField(env, lpObject, DataBrowserCustomCallbacksFc.v1_acceptDragCallback, (jint)lpStruct->u.v1.acceptDragCallback);
	(*env)->SetIntField(env, lpObject, DataBrowserCustomCallbacksFc.v1_receiveDragCallback, (jint)lpStruct->u.v1.receiveDragCallback);
}
#endif

#ifndef NO_DataBrowserListViewColumnDesc
typedef struct DataBrowserListViewColumnDesc_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID propertyDesc_propertyID, propertyDesc_propertyType, propertyDesc_propertyFlags, headerBtnDesc_version, headerBtnDesc_minimumWidth, headerBtnDesc_maximumWidth, headerBtnDesc_titleOffset, headerBtnDesc_titleString, headerBtnDesc_initialOrder, headerBtnDesc_btnFontStyle_flags, headerBtnDesc_btnFontStyle_font, headerBtnDesc_btnFontStyle_size, headerBtnDesc_btnFontStyle_style, headerBtnDesc_btnFontStyle_mode, headerBtnDesc_btnFontStyle_just, headerBtnDesc_btnFontStyle_foreColor_red, headerBtnDesc_btnFontStyle_foreColor_green, headerBtnDesc_btnFontStyle_foreColor_blue, headerBtnDesc_btnFontStyle_backColor_red, headerBtnDesc_btnFontStyle_backColor_green, headerBtnDesc_btnFontStyle_backColor_blue, headerBtnDesc_btnContentInfo_contentType, headerBtnDesc_btnContentInfo_iconRef;
} DataBrowserListViewColumnDesc_FID_CACHE;

DataBrowserListViewColumnDesc_FID_CACHE DataBrowserListViewColumnDescFc;

void cacheDataBrowserListViewColumnDescFields(JNIEnv *env, jobject lpObject)
{
	if (DataBrowserListViewColumnDescFc.cached) return;
	DataBrowserListViewColumnDescFc.clazz = (*env)->GetObjectClass(env, lpObject);
	DataBrowserListViewColumnDescFc.propertyDesc_propertyID = (*env)->GetFieldID(env, DataBrowserListViewColumnDescFc.clazz, "propertyDesc_propertyID", "I");
	DataBrowserListViewColumnDescFc.propertyDesc_propertyType = (*env)->GetFieldID(env, DataBrowserListViewColumnDescFc.clazz, "propertyDesc_propertyType", "I");
	DataBrowserListViewColumnDescFc.propertyDesc_propertyFlags = (*env)->GetFieldID(env, DataBrowserListViewColumnDescFc.clazz, "propertyDesc_propertyFlags", "I");
	DataBrowserListViewColumnDescFc.headerBtnDesc_version = (*env)->GetFieldID(env, DataBrowserListViewColumnDescFc.clazz, "headerBtnDesc_version", "I");
	DataBrowserListViewColumnDescFc.headerBtnDesc_minimumWidth = (*env)->GetFieldID(env, DataBrowserListViewColumnDescFc.clazz, "headerBtnDesc_minimumWidth", "S");
	DataBrowserListViewColumnDescFc.headerBtnDesc_maximumWidth = (*env)->GetFieldID(env, DataBrowserListViewColumnDescFc.clazz, "headerBtnDesc_maximumWidth", "S");
	DataBrowserListViewColumnDescFc.headerBtnDesc_titleOffset = (*env)->GetFieldID(env, DataBrowserListViewColumnDescFc.clazz, "headerBtnDesc_titleOffset", "S");
	DataBrowserListViewColumnDescFc.headerBtnDesc_titleString = (*env)->GetFieldID(env, DataBrowserListViewColumnDescFc.clazz, "headerBtnDesc_titleString", "I");
	DataBrowserListViewColumnDescFc.headerBtnDesc_initialOrder = (*env)->GetFieldID(env, DataBrowserListViewColumnDescFc.clazz, "headerBtnDesc_initialOrder", "S");
	DataBrowserListViewColumnDescFc.headerBtnDesc_btnFontStyle_flags = (*env)->GetFieldID(env, DataBrowserListViewColumnDescFc.clazz, "headerBtnDesc_btnFontStyle_flags", "S");
	DataBrowserListViewColumnDescFc.headerBtnDesc_btnFontStyle_font = (*env)->GetFieldID(env, DataBrowserListViewColumnDescFc.clazz, "headerBtnDesc_btnFontStyle_font", "S");
	DataBrowserListViewColumnDescFc.headerBtnDesc_btnFontStyle_size = (*env)->GetFieldID(env, DataBrowserListViewColumnDescFc.clazz, "headerBtnDesc_btnFontStyle_size", "S");
	DataBrowserListViewColumnDescFc.headerBtnDesc_btnFontStyle_style = (*env)->GetFieldID(env, DataBrowserListViewColumnDescFc.clazz, "headerBtnDesc_btnFontStyle_style", "S");
	DataBrowserListViewColumnDescFc.headerBtnDesc_btnFontStyle_mode = (*env)->GetFieldID(env, DataBrowserListViewColumnDescFc.clazz, "headerBtnDesc_btnFontStyle_mode", "S");
	DataBrowserListViewColumnDescFc.headerBtnDesc_btnFontStyle_just = (*env)->GetFieldID(env, DataBrowserListViewColumnDescFc.clazz, "headerBtnDesc_btnFontStyle_just", "S");
	DataBrowserListViewColumnDescFc.headerBtnDesc_btnFontStyle_foreColor_red = (*env)->GetFieldID(env, DataBrowserListViewColumnDescFc.clazz, "headerBtnDesc_btnFontStyle_foreColor_red", "S");
	DataBrowserListViewColumnDescFc.headerBtnDesc_btnFontStyle_foreColor_green = (*env)->GetFieldID(env, DataBrowserListViewColumnDescFc.clazz, "headerBtnDesc_btnFontStyle_foreColor_green", "S");
	DataBrowserListViewColumnDescFc.headerBtnDesc_btnFontStyle_foreColor_blue = (*env)->GetFieldID(env, DataBrowserListViewColumnDescFc.clazz, "headerBtnDesc_btnFontStyle_foreColor_blue", "S");
	DataBrowserListViewColumnDescFc.headerBtnDesc_btnFontStyle_backColor_red = (*env)->GetFieldID(env, DataBrowserListViewColumnDescFc.clazz, "headerBtnDesc_btnFontStyle_backColor_red", "S");
	DataBrowserListViewColumnDescFc.headerBtnDesc_btnFontStyle_backColor_green = (*env)->GetFieldID(env, DataBrowserListViewColumnDescFc.clazz, "headerBtnDesc_btnFontStyle_backColor_green", "S");
	DataBrowserListViewColumnDescFc.headerBtnDesc_btnFontStyle_backColor_blue = (*env)->GetFieldID(env, DataBrowserListViewColumnDescFc.clazz, "headerBtnDesc_btnFontStyle_backColor_blue", "S");
	DataBrowserListViewColumnDescFc.headerBtnDesc_btnContentInfo_contentType = (*env)->GetFieldID(env, DataBrowserListViewColumnDescFc.clazz, "headerBtnDesc_btnContentInfo_contentType", "S");
	DataBrowserListViewColumnDescFc.headerBtnDesc_btnContentInfo_iconRef = (*env)->GetFieldID(env, DataBrowserListViewColumnDescFc.clazz, "headerBtnDesc_btnContentInfo_iconRef", "I");
	DataBrowserListViewColumnDescFc.cached = 1;
}

DataBrowserListViewColumnDesc *getDataBrowserListViewColumnDescFields(JNIEnv *env, jobject lpObject, DataBrowserListViewColumnDesc *lpStruct)
{
	if (!DataBrowserListViewColumnDescFc.cached) cacheDataBrowserListViewColumnDescFields(env, lpObject);
	lpStruct->propertyDesc.propertyID = (DataBrowserPropertyID)(*env)->GetIntField(env, lpObject, DataBrowserListViewColumnDescFc.propertyDesc_propertyID);
	lpStruct->propertyDesc.propertyType = (OSType)(*env)->GetIntField(env, lpObject, DataBrowserListViewColumnDescFc.propertyDesc_propertyType);
	lpStruct->propertyDesc.propertyFlags = (DataBrowserPropertyFlags)(*env)->GetIntField(env, lpObject, DataBrowserListViewColumnDescFc.propertyDesc_propertyFlags);
	lpStruct->headerBtnDesc.version = (UInt32)(*env)->GetIntField(env, lpObject, DataBrowserListViewColumnDescFc.headerBtnDesc_version);
	lpStruct->headerBtnDesc.minimumWidth = (UInt16)(*env)->GetShortField(env, lpObject, DataBrowserListViewColumnDescFc.headerBtnDesc_minimumWidth);
	lpStruct->headerBtnDesc.maximumWidth = (UInt16)(*env)->GetShortField(env, lpObject, DataBrowserListViewColumnDescFc.headerBtnDesc_maximumWidth);
	lpStruct->headerBtnDesc.titleOffset = (SInt16)(*env)->GetShortField(env, lpObject, DataBrowserListViewColumnDescFc.headerBtnDesc_titleOffset);
	lpStruct->headerBtnDesc.titleString = (CFStringRef)(*env)->GetIntField(env, lpObject, DataBrowserListViewColumnDescFc.headerBtnDesc_titleString);
	lpStruct->headerBtnDesc.initialOrder = (DataBrowserSortOrder)(*env)->GetShortField(env, lpObject, DataBrowserListViewColumnDescFc.headerBtnDesc_initialOrder);
	lpStruct->headerBtnDesc.btnFontStyle.flags = (SInt16)(*env)->GetShortField(env, lpObject, DataBrowserListViewColumnDescFc.headerBtnDesc_btnFontStyle_flags);
	lpStruct->headerBtnDesc.btnFontStyle.font = (SInt16)(*env)->GetShortField(env, lpObject, DataBrowserListViewColumnDescFc.headerBtnDesc_btnFontStyle_font);
	lpStruct->headerBtnDesc.btnFontStyle.size = (SInt16)(*env)->GetShortField(env, lpObject, DataBrowserListViewColumnDescFc.headerBtnDesc_btnFontStyle_size);
	lpStruct->headerBtnDesc.btnFontStyle.style = (SInt16)(*env)->GetShortField(env, lpObject, DataBrowserListViewColumnDescFc.headerBtnDesc_btnFontStyle_style);
	lpStruct->headerBtnDesc.btnFontStyle.mode = (SInt16)(*env)->GetShortField(env, lpObject, DataBrowserListViewColumnDescFc.headerBtnDesc_btnFontStyle_mode);
	lpStruct->headerBtnDesc.btnFontStyle.just = (SInt16)(*env)->GetShortField(env, lpObject, DataBrowserListViewColumnDescFc.headerBtnDesc_btnFontStyle_just);
	lpStruct->headerBtnDesc.btnFontStyle.foreColor.red = (unsigned short)(*env)->GetShortField(env, lpObject, DataBrowserListViewColumnDescFc.headerBtnDesc_btnFontStyle_foreColor_red);
	lpStruct->headerBtnDesc.btnFontStyle.foreColor.green = (unsigned short)(*env)->GetShortField(env, lpObject, DataBrowserListViewColumnDescFc.headerBtnDesc_btnFontStyle_foreColor_green);
	lpStruct->headerBtnDesc.btnFontStyle.foreColor.blue = (unsigned short)(*env)->GetShortField(env, lpObject, DataBrowserListViewColumnDescFc.headerBtnDesc_btnFontStyle_foreColor_blue);
	lpStruct->headerBtnDesc.btnFontStyle.backColor.red = (unsigned short)(*env)->GetShortField(env, lpObject, DataBrowserListViewColumnDescFc.headerBtnDesc_btnFontStyle_backColor_red);
	lpStruct->headerBtnDesc.btnFontStyle.backColor.green = (unsigned short)(*env)->GetShortField(env, lpObject, DataBrowserListViewColumnDescFc.headerBtnDesc_btnFontStyle_backColor_green);
	lpStruct->headerBtnDesc.btnFontStyle.backColor.blue = (unsigned short)(*env)->GetShortField(env, lpObject, DataBrowserListViewColumnDescFc.headerBtnDesc_btnFontStyle_backColor_blue);
	lpStruct->headerBtnDesc.btnContentInfo.contentType = (ControlContentType)(*env)->GetShortField(env, lpObject, DataBrowserListViewColumnDescFc.headerBtnDesc_btnContentInfo_contentType);
	lpStruct->headerBtnDesc.btnContentInfo.u.iconRef = (IconRef)(*env)->GetIntField(env, lpObject, DataBrowserListViewColumnDescFc.headerBtnDesc_btnContentInfo_iconRef);
	return lpStruct;
}

void setDataBrowserListViewColumnDescFields(JNIEnv *env, jobject lpObject, DataBrowserListViewColumnDesc *lpStruct)
{
	if (!DataBrowserListViewColumnDescFc.cached) cacheDataBrowserListViewColumnDescFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, DataBrowserListViewColumnDescFc.propertyDesc_propertyID, (jint)lpStruct->propertyDesc.propertyID);
	(*env)->SetIntField(env, lpObject, DataBrowserListViewColumnDescFc.propertyDesc_propertyType, (jint)lpStruct->propertyDesc.propertyType);
	(*env)->SetIntField(env, lpObject, DataBrowserListViewColumnDescFc.propertyDesc_propertyFlags, (jint)lpStruct->propertyDesc.propertyFlags);
	(*env)->SetIntField(env, lpObject, DataBrowserListViewColumnDescFc.headerBtnDesc_version, (jint)lpStruct->headerBtnDesc.version);
	(*env)->SetShortField(env, lpObject, DataBrowserListViewColumnDescFc.headerBtnDesc_minimumWidth, (jshort)lpStruct->headerBtnDesc.minimumWidth);
	(*env)->SetShortField(env, lpObject, DataBrowserListViewColumnDescFc.headerBtnDesc_maximumWidth, (jshort)lpStruct->headerBtnDesc.maximumWidth);
	(*env)->SetShortField(env, lpObject, DataBrowserListViewColumnDescFc.headerBtnDesc_titleOffset, (jshort)lpStruct->headerBtnDesc.titleOffset);
	(*env)->SetIntField(env, lpObject, DataBrowserListViewColumnDescFc.headerBtnDesc_titleString, (jint)lpStruct->headerBtnDesc.titleString);
	(*env)->SetShortField(env, lpObject, DataBrowserListViewColumnDescFc.headerBtnDesc_initialOrder, (jshort)lpStruct->headerBtnDesc.initialOrder);
	(*env)->SetShortField(env, lpObject, DataBrowserListViewColumnDescFc.headerBtnDesc_btnFontStyle_flags, (jshort)lpStruct->headerBtnDesc.btnFontStyle.flags);
	(*env)->SetShortField(env, lpObject, DataBrowserListViewColumnDescFc.headerBtnDesc_btnFontStyle_font, (jshort)lpStruct->headerBtnDesc.btnFontStyle.font);
	(*env)->SetShortField(env, lpObject, DataBrowserListViewColumnDescFc.headerBtnDesc_btnFontStyle_size, (jshort)lpStruct->headerBtnDesc.btnFontStyle.size);
	(*env)->SetShortField(env, lpObject, DataBrowserListViewColumnDescFc.headerBtnDesc_btnFontStyle_style, (jshort)lpStruct->headerBtnDesc.btnFontStyle.style);
	(*env)->SetShortField(env, lpObject, DataBrowserListViewColumnDescFc.headerBtnDesc_btnFontStyle_mode, (jshort)lpStruct->headerBtnDesc.btnFontStyle.mode);
	(*env)->SetShortField(env, lpObject, DataBrowserListViewColumnDescFc.headerBtnDesc_btnFontStyle_just, (jshort)lpStruct->headerBtnDesc.btnFontStyle.just);
	(*env)->SetShortField(env, lpObject, DataBrowserListViewColumnDescFc.headerBtnDesc_btnFontStyle_foreColor_red, (jshort)lpStruct->headerBtnDesc.btnFontStyle.foreColor.red);
	(*env)->SetShortField(env, lpObject, DataBrowserListViewColumnDescFc.headerBtnDesc_btnFontStyle_foreColor_green, (jshort)lpStruct->headerBtnDesc.btnFontStyle.foreColor.green);
	(*env)->SetShortField(env, lpObject, DataBrowserListViewColumnDescFc.headerBtnDesc_btnFontStyle_foreColor_blue, (jshort)lpStruct->headerBtnDesc.btnFontStyle.foreColor.blue);
	(*env)->SetShortField(env, lpObject, DataBrowserListViewColumnDescFc.headerBtnDesc_btnFontStyle_backColor_red, (jshort)lpStruct->headerBtnDesc.btnFontStyle.backColor.red);
	(*env)->SetShortField(env, lpObject, DataBrowserListViewColumnDescFc.headerBtnDesc_btnFontStyle_backColor_green, (jshort)lpStruct->headerBtnDesc.btnFontStyle.backColor.green);
	(*env)->SetShortField(env, lpObject, DataBrowserListViewColumnDescFc.headerBtnDesc_btnFontStyle_backColor_blue, (jshort)lpStruct->headerBtnDesc.btnFontStyle.backColor.blue);
	(*env)->SetShortField(env, lpObject, DataBrowserListViewColumnDescFc.headerBtnDesc_btnContentInfo_contentType, (jshort)lpStruct->headerBtnDesc.btnContentInfo.contentType);
	(*env)->SetIntField(env, lpObject, DataBrowserListViewColumnDescFc.headerBtnDesc_btnContentInfo_iconRef, (jint)lpStruct->headerBtnDesc.btnContentInfo.u.iconRef);
}
#endif

#ifndef NO_DataBrowserListViewHeaderDesc
typedef struct DataBrowserListViewHeaderDesc_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID version, minimumWidth, maximumWidth, titleOffset, titleString, initialOrder, btnFontStyle_flags, btnFontStyle_font, btnFontStyle_size, btnFontStyle_style, btnFontStyle_mode, btnFontStyle_just, btnFontStyle_foreColor_red, btnFontStyle_foreColor_green, btnFontStyle_foreColor_blue, btnFontStyle_backColor_red, btnFontStyle_backColor_green, btnFontStyle_backColor_blue, btnContentInfo_contentType, btnContentInfo_iconRef;
} DataBrowserListViewHeaderDesc_FID_CACHE;

DataBrowserListViewHeaderDesc_FID_CACHE DataBrowserListViewHeaderDescFc;

void cacheDataBrowserListViewHeaderDescFields(JNIEnv *env, jobject lpObject)
{
	if (DataBrowserListViewHeaderDescFc.cached) return;
	DataBrowserListViewHeaderDescFc.clazz = (*env)->GetObjectClass(env, lpObject);
	DataBrowserListViewHeaderDescFc.version = (*env)->GetFieldID(env, DataBrowserListViewHeaderDescFc.clazz, "version", "I");
	DataBrowserListViewHeaderDescFc.minimumWidth = (*env)->GetFieldID(env, DataBrowserListViewHeaderDescFc.clazz, "minimumWidth", "S");
	DataBrowserListViewHeaderDescFc.maximumWidth = (*env)->GetFieldID(env, DataBrowserListViewHeaderDescFc.clazz, "maximumWidth", "S");
	DataBrowserListViewHeaderDescFc.titleOffset = (*env)->GetFieldID(env, DataBrowserListViewHeaderDescFc.clazz, "titleOffset", "S");
	DataBrowserListViewHeaderDescFc.titleString = (*env)->GetFieldID(env, DataBrowserListViewHeaderDescFc.clazz, "titleString", "I");
	DataBrowserListViewHeaderDescFc.initialOrder = (*env)->GetFieldID(env, DataBrowserListViewHeaderDescFc.clazz, "initialOrder", "S");
	DataBrowserListViewHeaderDescFc.btnFontStyle_flags = (*env)->GetFieldID(env, DataBrowserListViewHeaderDescFc.clazz, "btnFontStyle_flags", "S");
	DataBrowserListViewHeaderDescFc.btnFontStyle_font = (*env)->GetFieldID(env, DataBrowserListViewHeaderDescFc.clazz, "btnFontStyle_font", "S");
	DataBrowserListViewHeaderDescFc.btnFontStyle_size = (*env)->GetFieldID(env, DataBrowserListViewHeaderDescFc.clazz, "btnFontStyle_size", "S");
	DataBrowserListViewHeaderDescFc.btnFontStyle_style = (*env)->GetFieldID(env, DataBrowserListViewHeaderDescFc.clazz, "btnFontStyle_style", "S");
	DataBrowserListViewHeaderDescFc.btnFontStyle_mode = (*env)->GetFieldID(env, DataBrowserListViewHeaderDescFc.clazz, "btnFontStyle_mode", "S");
	DataBrowserListViewHeaderDescFc.btnFontStyle_just = (*env)->GetFieldID(env, DataBrowserListViewHeaderDescFc.clazz, "btnFontStyle_just", "S");
	DataBrowserListViewHeaderDescFc.btnFontStyle_foreColor_red = (*env)->GetFieldID(env, DataBrowserListViewHeaderDescFc.clazz, "btnFontStyle_foreColor_red", "S");
	DataBrowserListViewHeaderDescFc.btnFontStyle_foreColor_green = (*env)->GetFieldID(env, DataBrowserListViewHeaderDescFc.clazz, "btnFontStyle_foreColor_green", "S");
	DataBrowserListViewHeaderDescFc.btnFontStyle_foreColor_blue = (*env)->GetFieldID(env, DataBrowserListViewHeaderDescFc.clazz, "btnFontStyle_foreColor_blue", "S");
	DataBrowserListViewHeaderDescFc.btnFontStyle_backColor_red = (*env)->GetFieldID(env, DataBrowserListViewHeaderDescFc.clazz, "btnFontStyle_backColor_red", "S");
	DataBrowserListViewHeaderDescFc.btnFontStyle_backColor_green = (*env)->GetFieldID(env, DataBrowserListViewHeaderDescFc.clazz, "btnFontStyle_backColor_green", "S");
	DataBrowserListViewHeaderDescFc.btnFontStyle_backColor_blue = (*env)->GetFieldID(env, DataBrowserListViewHeaderDescFc.clazz, "btnFontStyle_backColor_blue", "S");
	DataBrowserListViewHeaderDescFc.btnContentInfo_contentType = (*env)->GetFieldID(env, DataBrowserListViewHeaderDescFc.clazz, "btnContentInfo_contentType", "S");
	DataBrowserListViewHeaderDescFc.btnContentInfo_iconRef = (*env)->GetFieldID(env, DataBrowserListViewHeaderDescFc.clazz, "btnContentInfo_iconRef", "I");
	DataBrowserListViewHeaderDescFc.cached = 1;
}

DataBrowserListViewHeaderDesc *getDataBrowserListViewHeaderDescFields(JNIEnv *env, jobject lpObject, DataBrowserListViewHeaderDesc *lpStruct)
{
	if (!DataBrowserListViewHeaderDescFc.cached) cacheDataBrowserListViewHeaderDescFields(env, lpObject);
	lpStruct->version = (*env)->GetIntField(env, lpObject, DataBrowserListViewHeaderDescFc.version);
	lpStruct->minimumWidth = (*env)->GetShortField(env, lpObject, DataBrowserListViewHeaderDescFc.minimumWidth);
	lpStruct->maximumWidth = (*env)->GetShortField(env, lpObject, DataBrowserListViewHeaderDescFc.maximumWidth);
	lpStruct->titleOffset = (*env)->GetShortField(env, lpObject, DataBrowserListViewHeaderDescFc.titleOffset);
	lpStruct->titleString = (CFStringRef)(*env)->GetIntField(env, lpObject, DataBrowserListViewHeaderDescFc.titleString);
	lpStruct->initialOrder = (*env)->GetShortField(env, lpObject, DataBrowserListViewHeaderDescFc.initialOrder);
	lpStruct->btnFontStyle.flags = (*env)->GetShortField(env, lpObject, DataBrowserListViewHeaderDescFc.btnFontStyle_flags);
	lpStruct->btnFontStyle.font = (*env)->GetShortField(env, lpObject, DataBrowserListViewHeaderDescFc.btnFontStyle_font);
	lpStruct->btnFontStyle.size = (*env)->GetShortField(env, lpObject, DataBrowserListViewHeaderDescFc.btnFontStyle_size);
	lpStruct->btnFontStyle.style = (*env)->GetShortField(env, lpObject, DataBrowserListViewHeaderDescFc.btnFontStyle_style);
	lpStruct->btnFontStyle.mode = (*env)->GetShortField(env, lpObject, DataBrowserListViewHeaderDescFc.btnFontStyle_mode);
	lpStruct->btnFontStyle.just = (*env)->GetShortField(env, lpObject, DataBrowserListViewHeaderDescFc.btnFontStyle_just);
	lpStruct->btnFontStyle.foreColor.red = (*env)->GetShortField(env, lpObject, DataBrowserListViewHeaderDescFc.btnFontStyle_foreColor_red);
	lpStruct->btnFontStyle.foreColor.green = (*env)->GetShortField(env, lpObject, DataBrowserListViewHeaderDescFc.btnFontStyle_foreColor_green);
	lpStruct->btnFontStyle.foreColor.blue = (*env)->GetShortField(env, lpObject, DataBrowserListViewHeaderDescFc.btnFontStyle_foreColor_blue);
	lpStruct->btnFontStyle.backColor.red = (*env)->GetShortField(env, lpObject, DataBrowserListViewHeaderDescFc.btnFontStyle_backColor_red);
	lpStruct->btnFontStyle.backColor.green = (*env)->GetShortField(env, lpObject, DataBrowserListViewHeaderDescFc.btnFontStyle_backColor_green);
	lpStruct->btnFontStyle.backColor.blue = (*env)->GetShortField(env, lpObject, DataBrowserListViewHeaderDescFc.btnFontStyle_backColor_blue);
	lpStruct->btnContentInfo.contentType = (*env)->GetShortField(env, lpObject, DataBrowserListViewHeaderDescFc.btnContentInfo_contentType);
	lpStruct->btnContentInfo.u.iconRef = (IconRef)(*env)->GetIntField(env, lpObject, DataBrowserListViewHeaderDescFc.btnContentInfo_iconRef);
	return lpStruct;
}

void setDataBrowserListViewHeaderDescFields(JNIEnv *env, jobject lpObject, DataBrowserListViewHeaderDesc *lpStruct)
{
	if (!DataBrowserListViewHeaderDescFc.cached) cacheDataBrowserListViewHeaderDescFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, DataBrowserListViewHeaderDescFc.version, (jint)lpStruct->version);
	(*env)->SetShortField(env, lpObject, DataBrowserListViewHeaderDescFc.minimumWidth, (jshort)lpStruct->minimumWidth);
	(*env)->SetShortField(env, lpObject, DataBrowserListViewHeaderDescFc.maximumWidth, (jshort)lpStruct->maximumWidth);
	(*env)->SetShortField(env, lpObject, DataBrowserListViewHeaderDescFc.titleOffset, (jshort)lpStruct->titleOffset);
	(*env)->SetIntField(env, lpObject, DataBrowserListViewHeaderDescFc.titleString, (jint)lpStruct->titleString);
	(*env)->SetShortField(env, lpObject, DataBrowserListViewHeaderDescFc.initialOrder, (jshort)lpStruct->initialOrder);
	(*env)->SetShortField(env, lpObject, DataBrowserListViewHeaderDescFc.btnFontStyle_flags, (jshort)lpStruct->btnFontStyle.flags);
	(*env)->SetShortField(env, lpObject, DataBrowserListViewHeaderDescFc.btnFontStyle_font, (jshort)lpStruct->btnFontStyle.font);
	(*env)->SetShortField(env, lpObject, DataBrowserListViewHeaderDescFc.btnFontStyle_size, (jshort)lpStruct->btnFontStyle.size);
	(*env)->SetShortField(env, lpObject, DataBrowserListViewHeaderDescFc.btnFontStyle_style, (jshort)lpStruct->btnFontStyle.style);
	(*env)->SetShortField(env, lpObject, DataBrowserListViewHeaderDescFc.btnFontStyle_mode, (jshort)lpStruct->btnFontStyle.mode);
	(*env)->SetShortField(env, lpObject, DataBrowserListViewHeaderDescFc.btnFontStyle_just, (jshort)lpStruct->btnFontStyle.just);
	(*env)->SetShortField(env, lpObject, DataBrowserListViewHeaderDescFc.btnFontStyle_foreColor_red, (jshort)lpStruct->btnFontStyle.foreColor.red);
	(*env)->SetShortField(env, lpObject, DataBrowserListViewHeaderDescFc.btnFontStyle_foreColor_green, (jshort)lpStruct->btnFontStyle.foreColor.green);
	(*env)->SetShortField(env, lpObject, DataBrowserListViewHeaderDescFc.btnFontStyle_foreColor_blue, (jshort)lpStruct->btnFontStyle.foreColor.blue);
	(*env)->SetShortField(env, lpObject, DataBrowserListViewHeaderDescFc.btnFontStyle_backColor_red, (jshort)lpStruct->btnFontStyle.backColor.red);
	(*env)->SetShortField(env, lpObject, DataBrowserListViewHeaderDescFc.btnFontStyle_backColor_green, (jshort)lpStruct->btnFontStyle.backColor.green);
	(*env)->SetShortField(env, lpObject, DataBrowserListViewHeaderDescFc.btnFontStyle_backColor_blue, (jshort)lpStruct->btnFontStyle.backColor.blue);
	(*env)->SetShortField(env, lpObject, DataBrowserListViewHeaderDescFc.btnContentInfo_contentType, (jshort)lpStruct->btnContentInfo.contentType);
	(*env)->SetIntField(env, lpObject, DataBrowserListViewHeaderDescFc.btnContentInfo_iconRef, (jint)lpStruct->btnContentInfo.u.iconRef);
}
#endif

#ifndef NO_EventRecord
typedef struct EventRecord_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID what, message, when, where_v, where_h, modifiers;
} EventRecord_FID_CACHE;

EventRecord_FID_CACHE EventRecordFc;

void cacheEventRecordFields(JNIEnv *env, jobject lpObject)
{
	if (EventRecordFc.cached) return;
	EventRecordFc.clazz = (*env)->GetObjectClass(env, lpObject);
	EventRecordFc.what = (*env)->GetFieldID(env, EventRecordFc.clazz, "what", "S");
	EventRecordFc.message = (*env)->GetFieldID(env, EventRecordFc.clazz, "message", "I");
	EventRecordFc.when = (*env)->GetFieldID(env, EventRecordFc.clazz, "when", "I");
	EventRecordFc.where_v = (*env)->GetFieldID(env, EventRecordFc.clazz, "where_v", "S");
	EventRecordFc.where_h = (*env)->GetFieldID(env, EventRecordFc.clazz, "where_h", "S");
	EventRecordFc.modifiers = (*env)->GetFieldID(env, EventRecordFc.clazz, "modifiers", "S");
	EventRecordFc.cached = 1;
}

EventRecord *getEventRecordFields(JNIEnv *env, jobject lpObject, EventRecord *lpStruct)
{
	if (!EventRecordFc.cached) cacheEventRecordFields(env, lpObject);
	lpStruct->what = (EventKind)(*env)->GetShortField(env, lpObject, EventRecordFc.what);
	lpStruct->message = (*env)->GetIntField(env, lpObject, EventRecordFc.message);
	lpStruct->when = (*env)->GetIntField(env, lpObject, EventRecordFc.when);
	lpStruct->where.v = (*env)->GetShortField(env, lpObject, EventRecordFc.where_v);
	lpStruct->where.h = (*env)->GetShortField(env, lpObject, EventRecordFc.where_h);
	lpStruct->modifiers = (EventModifiers)(*env)->GetShortField(env, lpObject, EventRecordFc.modifiers);
	return lpStruct;
}

void setEventRecordFields(JNIEnv *env, jobject lpObject, EventRecord *lpStruct)
{
	if (!EventRecordFc.cached) cacheEventRecordFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, EventRecordFc.what, (jshort)lpStruct->what);
	(*env)->SetIntField(env, lpObject, EventRecordFc.message, (jint)lpStruct->message);
	(*env)->SetIntField(env, lpObject, EventRecordFc.when, (jint)lpStruct->when);
	(*env)->SetShortField(env, lpObject, EventRecordFc.where_v, (jshort)lpStruct->where.v);
	(*env)->SetShortField(env, lpObject, EventRecordFc.where_h, (jshort)lpStruct->where.h);
	(*env)->SetShortField(env, lpObject, EventRecordFc.modifiers, (jshort)lpStruct->modifiers);
}
#endif

#ifndef NO_GDevice
typedef struct GDevice_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID gdRefNum, gdID, gdType, gdITable, gdResPref, gdSearchProc, gdCompProc, gdFlags, gdPMap, gdRefCon, gdNextGD, left, top, right, bottom, gdMode, gdCCBytes, gdCCDepth, gdCCXData, gdCCXMask, gdExt;
} GDevice_FID_CACHE;

GDevice_FID_CACHE GDeviceFc;

void cacheGDeviceFields(JNIEnv *env, jobject lpObject)
{
	if (GDeviceFc.cached) return;
	GDeviceFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GDeviceFc.gdRefNum = (*env)->GetFieldID(env, GDeviceFc.clazz, "gdRefNum", "S");
	GDeviceFc.gdID = (*env)->GetFieldID(env, GDeviceFc.clazz, "gdID", "S");
	GDeviceFc.gdType = (*env)->GetFieldID(env, GDeviceFc.clazz, "gdType", "S");
	GDeviceFc.gdITable = (*env)->GetFieldID(env, GDeviceFc.clazz, "gdITable", "I");
	GDeviceFc.gdResPref = (*env)->GetFieldID(env, GDeviceFc.clazz, "gdResPref", "S");
	GDeviceFc.gdSearchProc = (*env)->GetFieldID(env, GDeviceFc.clazz, "gdSearchProc", "I");
	GDeviceFc.gdCompProc = (*env)->GetFieldID(env, GDeviceFc.clazz, "gdCompProc", "I");
	GDeviceFc.gdFlags = (*env)->GetFieldID(env, GDeviceFc.clazz, "gdFlags", "S");
	GDeviceFc.gdPMap = (*env)->GetFieldID(env, GDeviceFc.clazz, "gdPMap", "I");
	GDeviceFc.gdRefCon = (*env)->GetFieldID(env, GDeviceFc.clazz, "gdRefCon", "I");
	GDeviceFc.gdNextGD = (*env)->GetFieldID(env, GDeviceFc.clazz, "gdNextGD", "I");
	GDeviceFc.left = (*env)->GetFieldID(env, GDeviceFc.clazz, "left", "S");
	GDeviceFc.top = (*env)->GetFieldID(env, GDeviceFc.clazz, "top", "S");
	GDeviceFc.right = (*env)->GetFieldID(env, GDeviceFc.clazz, "right", "S");
	GDeviceFc.bottom = (*env)->GetFieldID(env, GDeviceFc.clazz, "bottom", "S");
	GDeviceFc.gdMode = (*env)->GetFieldID(env, GDeviceFc.clazz, "gdMode", "I");
	GDeviceFc.gdCCBytes = (*env)->GetFieldID(env, GDeviceFc.clazz, "gdCCBytes", "S");
	GDeviceFc.gdCCDepth = (*env)->GetFieldID(env, GDeviceFc.clazz, "gdCCDepth", "S");
	GDeviceFc.gdCCXData = (*env)->GetFieldID(env, GDeviceFc.clazz, "gdCCXData", "I");
	GDeviceFc.gdCCXMask = (*env)->GetFieldID(env, GDeviceFc.clazz, "gdCCXMask", "I");
	GDeviceFc.gdExt = (*env)->GetFieldID(env, GDeviceFc.clazz, "gdExt", "I");
	GDeviceFc.cached = 1;
}

GDevice *getGDeviceFields(JNIEnv *env, jobject lpObject, GDevice *lpStruct)
{
	if (!GDeviceFc.cached) cacheGDeviceFields(env, lpObject);
	lpStruct->gdRefNum = (*env)->GetShortField(env, lpObject, GDeviceFc.gdRefNum);
	lpStruct->gdID = (*env)->GetShortField(env, lpObject, GDeviceFc.gdID);
	lpStruct->gdType = (*env)->GetShortField(env, lpObject, GDeviceFc.gdType);
	lpStruct->gdITable = (ITabHandle)(*env)->GetIntField(env, lpObject, GDeviceFc.gdITable);
	lpStruct->gdResPref = (*env)->GetShortField(env, lpObject, GDeviceFc.gdResPref);
	lpStruct->gdSearchProc = (SProcHndl)(*env)->GetIntField(env, lpObject, GDeviceFc.gdSearchProc);
	lpStruct->gdCompProc = (CProcHndl)(*env)->GetIntField(env, lpObject, GDeviceFc.gdCompProc);
	lpStruct->gdFlags = (*env)->GetShortField(env, lpObject, GDeviceFc.gdFlags);
	lpStruct->gdPMap = (PixMapHandle)(*env)->GetIntField(env, lpObject, GDeviceFc.gdPMap);
	lpStruct->gdRefCon = (*env)->GetIntField(env, lpObject, GDeviceFc.gdRefCon);
	lpStruct->gdNextGD = (GDHandle)(*env)->GetIntField(env, lpObject, GDeviceFc.gdNextGD);
	lpStruct->gdRect.left = (*env)->GetShortField(env, lpObject, GDeviceFc.left);
	lpStruct->gdRect.top = (*env)->GetShortField(env, lpObject, GDeviceFc.top);
	lpStruct->gdRect.right = (*env)->GetShortField(env, lpObject, GDeviceFc.right);
	lpStruct->gdRect.bottom = (*env)->GetShortField(env, lpObject, GDeviceFc.bottom);
	lpStruct->gdMode = (*env)->GetIntField(env, lpObject, GDeviceFc.gdMode);
	lpStruct->gdCCBytes = (*env)->GetShortField(env, lpObject, GDeviceFc.gdCCBytes);
	lpStruct->gdCCDepth = (*env)->GetShortField(env, lpObject, GDeviceFc.gdCCDepth);
	lpStruct->gdCCXData = (Handle)(*env)->GetIntField(env, lpObject, GDeviceFc.gdCCXData);
	lpStruct->gdCCXMask = (Handle)(*env)->GetIntField(env, lpObject, GDeviceFc.gdCCXMask);
	lpStruct->gdExt = (Handle)(*env)->GetIntField(env, lpObject, GDeviceFc.gdExt);
	return lpStruct;
}

void setGDeviceFields(JNIEnv *env, jobject lpObject, GDevice *lpStruct)
{
	if (!GDeviceFc.cached) cacheGDeviceFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, GDeviceFc.gdRefNum, (jshort)lpStruct->gdRefNum);
	(*env)->SetShortField(env, lpObject, GDeviceFc.gdID, (jshort)lpStruct->gdID);
	(*env)->SetShortField(env, lpObject, GDeviceFc.gdType, (jshort)lpStruct->gdType);
	(*env)->SetIntField(env, lpObject, GDeviceFc.gdITable, (jint)lpStruct->gdITable);
	(*env)->SetShortField(env, lpObject, GDeviceFc.gdResPref, (jshort)lpStruct->gdResPref);
	(*env)->SetIntField(env, lpObject, GDeviceFc.gdSearchProc, (jint)lpStruct->gdSearchProc);
	(*env)->SetIntField(env, lpObject, GDeviceFc.gdCompProc, (jint)lpStruct->gdCompProc);
	(*env)->SetShortField(env, lpObject, GDeviceFc.gdFlags, (jshort)lpStruct->gdFlags);
	(*env)->SetIntField(env, lpObject, GDeviceFc.gdPMap, (jint)lpStruct->gdPMap);
	(*env)->SetIntField(env, lpObject, GDeviceFc.gdRefCon, (jint)lpStruct->gdRefCon);
	(*env)->SetIntField(env, lpObject, GDeviceFc.gdNextGD, (jint)lpStruct->gdNextGD);
	(*env)->SetShortField(env, lpObject, GDeviceFc.left, (jshort)lpStruct->gdRect.left);
	(*env)->SetShortField(env, lpObject, GDeviceFc.top, (jshort)lpStruct->gdRect.top);
	(*env)->SetShortField(env, lpObject, GDeviceFc.right, (jshort)lpStruct->gdRect.right);
	(*env)->SetShortField(env, lpObject, GDeviceFc.bottom, (jshort)lpStruct->gdRect.bottom);
	(*env)->SetIntField(env, lpObject, GDeviceFc.gdMode, (jint)lpStruct->gdMode);
	(*env)->SetShortField(env, lpObject, GDeviceFc.gdCCBytes, (jshort)lpStruct->gdCCBytes);
	(*env)->SetShortField(env, lpObject, GDeviceFc.gdCCDepth, (jshort)lpStruct->gdCCDepth);
	(*env)->SetIntField(env, lpObject, GDeviceFc.gdCCXData, (jint)lpStruct->gdCCXData);
	(*env)->SetIntField(env, lpObject, GDeviceFc.gdCCXMask, (jint)lpStruct->gdCCXMask);
	(*env)->SetIntField(env, lpObject, GDeviceFc.gdExt, (jint)lpStruct->gdExt);
}
#endif

#ifndef NO_HIAxisPosition
typedef struct HIAxisPosition_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID toView, kind, offset;
} HIAxisPosition_FID_CACHE;

HIAxisPosition_FID_CACHE HIAxisPositionFc;

void cacheHIAxisPositionFields(JNIEnv *env, jobject lpObject)
{
	if (HIAxisPositionFc.cached) return;
	HIAxisPositionFc.clazz = (*env)->GetObjectClass(env, lpObject);
	HIAxisPositionFc.toView = (*env)->GetFieldID(env, HIAxisPositionFc.clazz, "toView", "I");
	HIAxisPositionFc.kind = (*env)->GetFieldID(env, HIAxisPositionFc.clazz, "kind", "S");
	HIAxisPositionFc.offset = (*env)->GetFieldID(env, HIAxisPositionFc.clazz, "offset", "F");
	HIAxisPositionFc.cached = 1;
}

HIAxisPosition *getHIAxisPositionFields(JNIEnv *env, jobject lpObject, HIAxisPosition *lpStruct)
{
	if (!HIAxisPositionFc.cached) cacheHIAxisPositionFields(env, lpObject);
	lpStruct->toView = (HIViewRef)(*env)->GetIntField(env, lpObject, HIAxisPositionFc.toView);
	lpStruct->kind = (*env)->GetShortField(env, lpObject, HIAxisPositionFc.kind);
	lpStruct->offset = (*env)->GetFloatField(env, lpObject, HIAxisPositionFc.offset);
	return lpStruct;
}

void setHIAxisPositionFields(JNIEnv *env, jobject lpObject, HIAxisPosition *lpStruct)
{
	if (!HIAxisPositionFc.cached) cacheHIAxisPositionFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, HIAxisPositionFc.toView, (jint)lpStruct->toView);
	(*env)->SetShortField(env, lpObject, HIAxisPositionFc.kind, (jshort)lpStruct->kind);
	(*env)->SetFloatField(env, lpObject, HIAxisPositionFc.offset, (jfloat)lpStruct->offset);
}
#endif

#ifndef NO_HIAxisScale
typedef struct HIAxisScale_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID toView, kind, ratio;
} HIAxisScale_FID_CACHE;

HIAxisScale_FID_CACHE HIAxisScaleFc;

void cacheHIAxisScaleFields(JNIEnv *env, jobject lpObject)
{
	if (HIAxisScaleFc.cached) return;
	HIAxisScaleFc.clazz = (*env)->GetObjectClass(env, lpObject);
	HIAxisScaleFc.toView = (*env)->GetFieldID(env, HIAxisScaleFc.clazz, "toView", "I");
	HIAxisScaleFc.kind = (*env)->GetFieldID(env, HIAxisScaleFc.clazz, "kind", "S");
	HIAxisScaleFc.ratio = (*env)->GetFieldID(env, HIAxisScaleFc.clazz, "ratio", "F");
	HIAxisScaleFc.cached = 1;
}

HIAxisScale *getHIAxisScaleFields(JNIEnv *env, jobject lpObject, HIAxisScale *lpStruct)
{
	if (!HIAxisScaleFc.cached) cacheHIAxisScaleFields(env, lpObject);
	lpStruct->toView = (HIViewRef)(*env)->GetIntField(env, lpObject, HIAxisScaleFc.toView);
	lpStruct->kind = (*env)->GetShortField(env, lpObject, HIAxisScaleFc.kind);
	lpStruct->ratio = (*env)->GetFloatField(env, lpObject, HIAxisScaleFc.ratio);
	return lpStruct;
}

void setHIAxisScaleFields(JNIEnv *env, jobject lpObject, HIAxisScale *lpStruct)
{
	if (!HIAxisScaleFc.cached) cacheHIAxisScaleFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, HIAxisScaleFc.toView, (jint)lpStruct->toView);
	(*env)->SetShortField(env, lpObject, HIAxisScaleFc.kind, (jshort)lpStruct->kind);
	(*env)->SetFloatField(env, lpObject, HIAxisScaleFc.ratio, (jfloat)lpStruct->ratio);
}
#endif

#ifndef NO_HIBinding
typedef struct HIBinding_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID top, left, bottom, right;
} HIBinding_FID_CACHE;

HIBinding_FID_CACHE HIBindingFc;

void cacheHIBindingFields(JNIEnv *env, jobject lpObject)
{
	if (HIBindingFc.cached) return;
	HIBindingFc.clazz = (*env)->GetObjectClass(env, lpObject);
	HIBindingFc.top = (*env)->GetFieldID(env, HIBindingFc.clazz, "top", "Lorg/eclipse/swt/internal/carbon/HISideBinding;");
	HIBindingFc.left = (*env)->GetFieldID(env, HIBindingFc.clazz, "left", "Lorg/eclipse/swt/internal/carbon/HISideBinding;");
	HIBindingFc.bottom = (*env)->GetFieldID(env, HIBindingFc.clazz, "bottom", "Lorg/eclipse/swt/internal/carbon/HISideBinding;");
	HIBindingFc.right = (*env)->GetFieldID(env, HIBindingFc.clazz, "right", "Lorg/eclipse/swt/internal/carbon/HISideBinding;");
	HIBindingFc.cached = 1;
}

HIBinding *getHIBindingFields(JNIEnv *env, jobject lpObject, HIBinding *lpStruct)
{
	if (!HIBindingFc.cached) cacheHIBindingFields(env, lpObject);
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, HIBindingFc.top);
	if (lpObject1 != NULL) getHISideBindingFields(env, lpObject1, &lpStruct->top);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, HIBindingFc.left);
	if (lpObject1 != NULL) getHISideBindingFields(env, lpObject1, &lpStruct->left);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, HIBindingFc.bottom);
	if (lpObject1 != NULL) getHISideBindingFields(env, lpObject1, &lpStruct->bottom);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, HIBindingFc.right);
	if (lpObject1 != NULL) getHISideBindingFields(env, lpObject1, &lpStruct->right);
	}
	return lpStruct;
}

void setHIBindingFields(JNIEnv *env, jobject lpObject, HIBinding *lpStruct)
{
	if (!HIBindingFc.cached) cacheHIBindingFields(env, lpObject);
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, HIBindingFc.top);
	if (lpObject1 != NULL) setHISideBindingFields(env, lpObject1, &lpStruct->top);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, HIBindingFc.left);
	if (lpObject1 != NULL) setHISideBindingFields(env, lpObject1, &lpStruct->left);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, HIBindingFc.bottom);
	if (lpObject1 != NULL) setHISideBindingFields(env, lpObject1, &lpStruct->bottom);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, HIBindingFc.right);
	if (lpObject1 != NULL) setHISideBindingFields(env, lpObject1, &lpStruct->right);
	}
}
#endif

#ifndef NO_HICommand
typedef struct HICommand_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID attributes, commandID, menu_menuRef, menu_menuItemIndex;
} HICommand_FID_CACHE;

HICommand_FID_CACHE HICommandFc;

void cacheHICommandFields(JNIEnv *env, jobject lpObject)
{
	if (HICommandFc.cached) return;
	HICommandFc.clazz = (*env)->GetObjectClass(env, lpObject);
	HICommandFc.attributes = (*env)->GetFieldID(env, HICommandFc.clazz, "attributes", "I");
	HICommandFc.commandID = (*env)->GetFieldID(env, HICommandFc.clazz, "commandID", "I");
	HICommandFc.menu_menuRef = (*env)->GetFieldID(env, HICommandFc.clazz, "menu_menuRef", "I");
	HICommandFc.menu_menuItemIndex = (*env)->GetFieldID(env, HICommandFc.clazz, "menu_menuItemIndex", "S");
	HICommandFc.cached = 1;
}

HICommand *getHICommandFields(JNIEnv *env, jobject lpObject, HICommand *lpStruct)
{
	if (!HICommandFc.cached) cacheHICommandFields(env, lpObject);
	lpStruct->attributes = (*env)->GetIntField(env, lpObject, HICommandFc.attributes);
	lpStruct->commandID = (*env)->GetIntField(env, lpObject, HICommandFc.commandID);
	lpStruct->menu.menuRef = (MenuRef)(*env)->GetIntField(env, lpObject, HICommandFc.menu_menuRef);
	lpStruct->menu.menuItemIndex = (MenuItemIndex)(*env)->GetShortField(env, lpObject, HICommandFc.menu_menuItemIndex);
	return lpStruct;
}

void setHICommandFields(JNIEnv *env, jobject lpObject, HICommand *lpStruct)
{
	if (!HICommandFc.cached) cacheHICommandFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, HICommandFc.attributes, (jint)lpStruct->attributes);
	(*env)->SetIntField(env, lpObject, HICommandFc.commandID, (jint)lpStruct->commandID);
	(*env)->SetIntField(env, lpObject, HICommandFc.menu_menuRef, (jint)lpStruct->menu.menuRef);
	(*env)->SetShortField(env, lpObject, HICommandFc.menu_menuItemIndex, (jshort)lpStruct->menu.menuItemIndex);
}
#endif

#ifndef NO_HILayoutInfo
typedef struct HILayoutInfo_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID version, binding, scale, position;
} HILayoutInfo_FID_CACHE;

HILayoutInfo_FID_CACHE HILayoutInfoFc;

void cacheHILayoutInfoFields(JNIEnv *env, jobject lpObject)
{
	if (HILayoutInfoFc.cached) return;
	HILayoutInfoFc.clazz = (*env)->GetObjectClass(env, lpObject);
	HILayoutInfoFc.version = (*env)->GetFieldID(env, HILayoutInfoFc.clazz, "version", "I");
	HILayoutInfoFc.binding = (*env)->GetFieldID(env, HILayoutInfoFc.clazz, "binding", "Lorg/eclipse/swt/internal/carbon/HIBinding;");
	HILayoutInfoFc.scale = (*env)->GetFieldID(env, HILayoutInfoFc.clazz, "scale", "Lorg/eclipse/swt/internal/carbon/HIScaling;");
	HILayoutInfoFc.position = (*env)->GetFieldID(env, HILayoutInfoFc.clazz, "position", "Lorg/eclipse/swt/internal/carbon/HIPositioning;");
	HILayoutInfoFc.cached = 1;
}

HILayoutInfo *getHILayoutInfoFields(JNIEnv *env, jobject lpObject, HILayoutInfo *lpStruct)
{
	if (!HILayoutInfoFc.cached) cacheHILayoutInfoFields(env, lpObject);
	lpStruct->version = (*env)->GetIntField(env, lpObject, HILayoutInfoFc.version);
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, HILayoutInfoFc.binding);
	if (lpObject1 != NULL) getHIBindingFields(env, lpObject1, &lpStruct->binding);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, HILayoutInfoFc.scale);
	if (lpObject1 != NULL) getHIScalingFields(env, lpObject1, &lpStruct->scale);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, HILayoutInfoFc.position);
	if (lpObject1 != NULL) getHIPositioningFields(env, lpObject1, &lpStruct->position);
	}
	return lpStruct;
}

void setHILayoutInfoFields(JNIEnv *env, jobject lpObject, HILayoutInfo *lpStruct)
{
	if (!HILayoutInfoFc.cached) cacheHILayoutInfoFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, HILayoutInfoFc.version, (jint)lpStruct->version);
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, HILayoutInfoFc.binding);
	if (lpObject1 != NULL) setHIBindingFields(env, lpObject1, &lpStruct->binding);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, HILayoutInfoFc.scale);
	if (lpObject1 != NULL) setHIScalingFields(env, lpObject1, &lpStruct->scale);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, HILayoutInfoFc.position);
	if (lpObject1 != NULL) setHIPositioningFields(env, lpObject1, &lpStruct->position);
	}
}
#endif

#ifndef NO_HIPositioning
typedef struct HIPositioning_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID x, y;
} HIPositioning_FID_CACHE;

HIPositioning_FID_CACHE HIPositioningFc;

void cacheHIPositioningFields(JNIEnv *env, jobject lpObject)
{
	if (HIPositioningFc.cached) return;
	HIPositioningFc.clazz = (*env)->GetObjectClass(env, lpObject);
	HIPositioningFc.x = (*env)->GetFieldID(env, HIPositioningFc.clazz, "x", "Lorg/eclipse/swt/internal/carbon/HIAxisPosition;");
	HIPositioningFc.y = (*env)->GetFieldID(env, HIPositioningFc.clazz, "y", "Lorg/eclipse/swt/internal/carbon/HIAxisPosition;");
	HIPositioningFc.cached = 1;
}

HIPositioning *getHIPositioningFields(JNIEnv *env, jobject lpObject, HIPositioning *lpStruct)
{
	if (!HIPositioningFc.cached) cacheHIPositioningFields(env, lpObject);
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, HIPositioningFc.x);
	if (lpObject1 != NULL) getHIAxisPositionFields(env, lpObject1, &lpStruct->x);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, HIPositioningFc.y);
	if (lpObject1 != NULL) getHIAxisPositionFields(env, lpObject1, &lpStruct->y);
	}
	return lpStruct;
}

void setHIPositioningFields(JNIEnv *env, jobject lpObject, HIPositioning *lpStruct)
{
	if (!HIPositioningFc.cached) cacheHIPositioningFields(env, lpObject);
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, HIPositioningFc.x);
	if (lpObject1 != NULL) setHIAxisPositionFields(env, lpObject1, &lpStruct->x);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, HIPositioningFc.y);
	if (lpObject1 != NULL) setHIAxisPositionFields(env, lpObject1, &lpStruct->y);
	}
}
#endif

#ifndef NO_HIScaling
typedef struct HIScaling_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID x, y;
} HIScaling_FID_CACHE;

HIScaling_FID_CACHE HIScalingFc;

void cacheHIScalingFields(JNIEnv *env, jobject lpObject)
{
	if (HIScalingFc.cached) return;
	HIScalingFc.clazz = (*env)->GetObjectClass(env, lpObject);
	HIScalingFc.x = (*env)->GetFieldID(env, HIScalingFc.clazz, "x", "Lorg/eclipse/swt/internal/carbon/HIAxisScale;");
	HIScalingFc.y = (*env)->GetFieldID(env, HIScalingFc.clazz, "y", "Lorg/eclipse/swt/internal/carbon/HIAxisScale;");
	HIScalingFc.cached = 1;
}

HIScaling *getHIScalingFields(JNIEnv *env, jobject lpObject, HIScaling *lpStruct)
{
	if (!HIScalingFc.cached) cacheHIScalingFields(env, lpObject);
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, HIScalingFc.x);
	if (lpObject1 != NULL) getHIAxisScaleFields(env, lpObject1, &lpStruct->x);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, HIScalingFc.y);
	if (lpObject1 != NULL) getHIAxisScaleFields(env, lpObject1, &lpStruct->y);
	}
	return lpStruct;
}

void setHIScalingFields(JNIEnv *env, jobject lpObject, HIScaling *lpStruct)
{
	if (!HIScalingFc.cached) cacheHIScalingFields(env, lpObject);
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, HIScalingFc.x);
	if (lpObject1 != NULL) setHIAxisScaleFields(env, lpObject1, &lpStruct->x);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, HIScalingFc.y);
	if (lpObject1 != NULL) setHIAxisScaleFields(env, lpObject1, &lpStruct->y);
	}
}
#endif

#ifndef NO_HIScrollBarTrackInfo
typedef struct HIScrollBarTrackInfo_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID version, enableState, pressState, viewsize;
} HIScrollBarTrackInfo_FID_CACHE;

HIScrollBarTrackInfo_FID_CACHE HIScrollBarTrackInfoFc;

void cacheHIScrollBarTrackInfoFields(JNIEnv *env, jobject lpObject)
{
	if (HIScrollBarTrackInfoFc.cached) return;
	HIScrollBarTrackInfoFc.clazz = (*env)->GetObjectClass(env, lpObject);
	HIScrollBarTrackInfoFc.version = (*env)->GetFieldID(env, HIScrollBarTrackInfoFc.clazz, "version", "I");
	HIScrollBarTrackInfoFc.enableState = (*env)->GetFieldID(env, HIScrollBarTrackInfoFc.clazz, "enableState", "B");
	HIScrollBarTrackInfoFc.pressState = (*env)->GetFieldID(env, HIScrollBarTrackInfoFc.clazz, "pressState", "B");
	HIScrollBarTrackInfoFc.viewsize = (*env)->GetFieldID(env, HIScrollBarTrackInfoFc.clazz, "viewsize", "F");
	HIScrollBarTrackInfoFc.cached = 1;
}

HIScrollBarTrackInfo *getHIScrollBarTrackInfoFields(JNIEnv *env, jobject lpObject, HIScrollBarTrackInfo *lpStruct)
{
	if (!HIScrollBarTrackInfoFc.cached) cacheHIScrollBarTrackInfoFields(env, lpObject);
	lpStruct->version = (*env)->GetIntField(env, lpObject, HIScrollBarTrackInfoFc.version);
	lpStruct->enableState = (*env)->GetByteField(env, lpObject, HIScrollBarTrackInfoFc.enableState);
	lpStruct->pressState = (*env)->GetByteField(env, lpObject, HIScrollBarTrackInfoFc.pressState);
	lpStruct->viewsize = (*env)->GetFloatField(env, lpObject, HIScrollBarTrackInfoFc.viewsize);
	return lpStruct;
}

void setHIScrollBarTrackInfoFields(JNIEnv *env, jobject lpObject, HIScrollBarTrackInfo *lpStruct)
{
	if (!HIScrollBarTrackInfoFc.cached) cacheHIScrollBarTrackInfoFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, HIScrollBarTrackInfoFc.version, (jint)lpStruct->version);
	(*env)->SetByteField(env, lpObject, HIScrollBarTrackInfoFc.enableState, (jbyte)lpStruct->enableState);
	(*env)->SetByteField(env, lpObject, HIScrollBarTrackInfoFc.pressState, (jbyte)lpStruct->pressState);
	(*env)->SetFloatField(env, lpObject, HIScrollBarTrackInfoFc.viewsize, (jfloat)lpStruct->viewsize);
}
#endif

#ifndef NO_HISideBinding
typedef struct HISideBinding_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID toView, kind, offset;
} HISideBinding_FID_CACHE;

HISideBinding_FID_CACHE HISideBindingFc;

void cacheHISideBindingFields(JNIEnv *env, jobject lpObject)
{
	if (HISideBindingFc.cached) return;
	HISideBindingFc.clazz = (*env)->GetObjectClass(env, lpObject);
	HISideBindingFc.toView = (*env)->GetFieldID(env, HISideBindingFc.clazz, "toView", "I");
	HISideBindingFc.kind = (*env)->GetFieldID(env, HISideBindingFc.clazz, "kind", "S");
	HISideBindingFc.offset = (*env)->GetFieldID(env, HISideBindingFc.clazz, "offset", "F");
	HISideBindingFc.cached = 1;
}

HISideBinding *getHISideBindingFields(JNIEnv *env, jobject lpObject, HISideBinding *lpStruct)
{
	if (!HISideBindingFc.cached) cacheHISideBindingFields(env, lpObject);
	lpStruct->toView = (HIViewRef)(*env)->GetIntField(env, lpObject, HISideBindingFc.toView);
	lpStruct->kind = (*env)->GetShortField(env, lpObject, HISideBindingFc.kind);
	lpStruct->offset = (*env)->GetFloatField(env, lpObject, HISideBindingFc.offset);
	return lpStruct;
}

void setHISideBindingFields(JNIEnv *env, jobject lpObject, HISideBinding *lpStruct)
{
	if (!HISideBindingFc.cached) cacheHISideBindingFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, HISideBindingFc.toView, (jint)lpStruct->toView);
	(*env)->SetShortField(env, lpObject, HISideBindingFc.kind, (jshort)lpStruct->kind);
	(*env)->SetFloatField(env, lpObject, HISideBindingFc.offset, (jfloat)lpStruct->offset);
}
#endif

#ifndef NO_HIThemeAnimationFrameInfo
typedef struct HIThemeAnimationFrameInfo_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID index;
} HIThemeAnimationFrameInfo_FID_CACHE;

HIThemeAnimationFrameInfo_FID_CACHE HIThemeAnimationFrameInfoFc;

void cacheHIThemeAnimationFrameInfoFields(JNIEnv *env, jobject lpObject)
{
	if (HIThemeAnimationFrameInfoFc.cached) return;
	HIThemeAnimationFrameInfoFc.clazz = (*env)->GetObjectClass(env, lpObject);
	HIThemeAnimationFrameInfoFc.index = (*env)->GetFieldID(env, HIThemeAnimationFrameInfoFc.clazz, "index", "I");
	HIThemeAnimationFrameInfoFc.cached = 1;
}

HIThemeAnimationFrameInfo *getHIThemeAnimationFrameInfoFields(JNIEnv *env, jobject lpObject, HIThemeAnimationFrameInfo *lpStruct)
{
	if (!HIThemeAnimationFrameInfoFc.cached) cacheHIThemeAnimationFrameInfoFields(env, lpObject);
	lpStruct->index = (*env)->GetIntField(env, lpObject, HIThemeAnimationFrameInfoFc.index);
	return lpStruct;
}

void setHIThemeAnimationFrameInfoFields(JNIEnv *env, jobject lpObject, HIThemeAnimationFrameInfo *lpStruct)
{
	if (!HIThemeAnimationFrameInfoFc.cached) cacheHIThemeAnimationFrameInfoFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, HIThemeAnimationFrameInfoFc.index, (jint)lpStruct->index);
}
#endif

#ifndef NO_HIThemeAnimationTimeInfo
typedef struct HIThemeAnimationTimeInfo_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID start, current;
} HIThemeAnimationTimeInfo_FID_CACHE;

HIThemeAnimationTimeInfo_FID_CACHE HIThemeAnimationTimeInfoFc;

void cacheHIThemeAnimationTimeInfoFields(JNIEnv *env, jobject lpObject)
{
	if (HIThemeAnimationTimeInfoFc.cached) return;
	HIThemeAnimationTimeInfoFc.clazz = (*env)->GetObjectClass(env, lpObject);
	HIThemeAnimationTimeInfoFc.start = (*env)->GetFieldID(env, HIThemeAnimationTimeInfoFc.clazz, "start", "J");
	HIThemeAnimationTimeInfoFc.current = (*env)->GetFieldID(env, HIThemeAnimationTimeInfoFc.clazz, "current", "J");
	HIThemeAnimationTimeInfoFc.cached = 1;
}

HIThemeAnimationTimeInfo *getHIThemeAnimationTimeInfoFields(JNIEnv *env, jobject lpObject, HIThemeAnimationTimeInfo *lpStruct)
{
	if (!HIThemeAnimationTimeInfoFc.cached) cacheHIThemeAnimationTimeInfoFields(env, lpObject);
	lpStruct->start = (*env)->GetLongField(env, lpObject, HIThemeAnimationTimeInfoFc.start);
	lpStruct->current = (*env)->GetLongField(env, lpObject, HIThemeAnimationTimeInfoFc.current);
	return lpStruct;
}

void setHIThemeAnimationTimeInfoFields(JNIEnv *env, jobject lpObject, HIThemeAnimationTimeInfo *lpStruct)
{
	if (!HIThemeAnimationTimeInfoFc.cached) cacheHIThemeAnimationTimeInfoFields(env, lpObject);
	(*env)->SetLongField(env, lpObject, HIThemeAnimationTimeInfoFc.start, (jlong)lpStruct->start);
	(*env)->SetLongField(env, lpObject, HIThemeAnimationTimeInfoFc.current, (jlong)lpStruct->current);
}
#endif

#ifndef NO_HIThemeBackgroundDrawInfo
typedef struct HIThemeBackgroundDrawInfo_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID version, state, kind;
} HIThemeBackgroundDrawInfo_FID_CACHE;

HIThemeBackgroundDrawInfo_FID_CACHE HIThemeBackgroundDrawInfoFc;

void cacheHIThemeBackgroundDrawInfoFields(JNIEnv *env, jobject lpObject)
{
	if (HIThemeBackgroundDrawInfoFc.cached) return;
	HIThemeBackgroundDrawInfoFc.clazz = (*env)->GetObjectClass(env, lpObject);
	HIThemeBackgroundDrawInfoFc.version = (*env)->GetFieldID(env, HIThemeBackgroundDrawInfoFc.clazz, "version", "I");
	HIThemeBackgroundDrawInfoFc.state = (*env)->GetFieldID(env, HIThemeBackgroundDrawInfoFc.clazz, "state", "I");
	HIThemeBackgroundDrawInfoFc.kind = (*env)->GetFieldID(env, HIThemeBackgroundDrawInfoFc.clazz, "kind", "I");
	HIThemeBackgroundDrawInfoFc.cached = 1;
}

HIThemeBackgroundDrawInfo *getHIThemeBackgroundDrawInfoFields(JNIEnv *env, jobject lpObject, HIThemeBackgroundDrawInfo *lpStruct)
{
	if (!HIThemeBackgroundDrawInfoFc.cached) cacheHIThemeBackgroundDrawInfoFields(env, lpObject);
	lpStruct->version = (*env)->GetIntField(env, lpObject, HIThemeBackgroundDrawInfoFc.version);
	lpStruct->state = (*env)->GetIntField(env, lpObject, HIThemeBackgroundDrawInfoFc.state);
	lpStruct->kind = (*env)->GetIntField(env, lpObject, HIThemeBackgroundDrawInfoFc.kind);
	return lpStruct;
}

void setHIThemeBackgroundDrawInfoFields(JNIEnv *env, jobject lpObject, HIThemeBackgroundDrawInfo *lpStruct)
{
	if (!HIThemeBackgroundDrawInfoFc.cached) cacheHIThemeBackgroundDrawInfoFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, HIThemeBackgroundDrawInfoFc.version, (jint)lpStruct->version);
	(*env)->SetIntField(env, lpObject, HIThemeBackgroundDrawInfoFc.state, (jint)lpStruct->state);
	(*env)->SetIntField(env, lpObject, HIThemeBackgroundDrawInfoFc.kind, (jint)lpStruct->kind);
}
#endif

#ifndef NO_HIThemeButtonDrawInfo
typedef struct HIThemeButtonDrawInfo_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID version, state, kind, value, adornment, time, frame;
} HIThemeButtonDrawInfo_FID_CACHE;

HIThemeButtonDrawInfo_FID_CACHE HIThemeButtonDrawInfoFc;

void cacheHIThemeButtonDrawInfoFields(JNIEnv *env, jobject lpObject)
{
	if (HIThemeButtonDrawInfoFc.cached) return;
	HIThemeButtonDrawInfoFc.clazz = (*env)->GetObjectClass(env, lpObject);
	HIThemeButtonDrawInfoFc.version = (*env)->GetFieldID(env, HIThemeButtonDrawInfoFc.clazz, "version", "I");
	HIThemeButtonDrawInfoFc.state = (*env)->GetFieldID(env, HIThemeButtonDrawInfoFc.clazz, "state", "I");
	HIThemeButtonDrawInfoFc.kind = (*env)->GetFieldID(env, HIThemeButtonDrawInfoFc.clazz, "kind", "I");
	HIThemeButtonDrawInfoFc.value = (*env)->GetFieldID(env, HIThemeButtonDrawInfoFc.clazz, "value", "I");
	HIThemeButtonDrawInfoFc.adornment = (*env)->GetFieldID(env, HIThemeButtonDrawInfoFc.clazz, "adornment", "I");
	HIThemeButtonDrawInfoFc.time = (*env)->GetFieldID(env, HIThemeButtonDrawInfoFc.clazz, "time", "Lorg/eclipse/swt/internal/carbon/HIThemeAnimationTimeInfo;");
	HIThemeButtonDrawInfoFc.frame = (*env)->GetFieldID(env, HIThemeButtonDrawInfoFc.clazz, "frame", "Lorg/eclipse/swt/internal/carbon/HIThemeAnimationFrameInfo;");
	HIThemeButtonDrawInfoFc.cached = 1;
}

HIThemeButtonDrawInfo *getHIThemeButtonDrawInfoFields(JNIEnv *env, jobject lpObject, HIThemeButtonDrawInfo *lpStruct)
{
	if (!HIThemeButtonDrawInfoFc.cached) cacheHIThemeButtonDrawInfoFields(env, lpObject);
	lpStruct->version = (*env)->GetIntField(env, lpObject, HIThemeButtonDrawInfoFc.version);
	lpStruct->state = (*env)->GetIntField(env, lpObject, HIThemeButtonDrawInfoFc.state);
	lpStruct->kind = (*env)->GetIntField(env, lpObject, HIThemeButtonDrawInfoFc.kind);
	lpStruct->value = (*env)->GetIntField(env, lpObject, HIThemeButtonDrawInfoFc.value);
	lpStruct->adornment = (*env)->GetIntField(env, lpObject, HIThemeButtonDrawInfoFc.adornment);
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, HIThemeButtonDrawInfoFc.time);
	if (lpObject1 != NULL) getHIThemeAnimationTimeInfoFields(env, lpObject1, &lpStruct->animation.time);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, HIThemeButtonDrawInfoFc.frame);
	if (lpObject1 != NULL) getHIThemeAnimationFrameInfoFields(env, lpObject1, &lpStruct->animation.frame);
	}
	return lpStruct;
}

void setHIThemeButtonDrawInfoFields(JNIEnv *env, jobject lpObject, HIThemeButtonDrawInfo *lpStruct)
{
	if (!HIThemeButtonDrawInfoFc.cached) cacheHIThemeButtonDrawInfoFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, HIThemeButtonDrawInfoFc.version, (jint)lpStruct->version);
	(*env)->SetIntField(env, lpObject, HIThemeButtonDrawInfoFc.state, (jint)lpStruct->state);
	(*env)->SetIntField(env, lpObject, HIThemeButtonDrawInfoFc.kind, (jint)lpStruct->kind);
	(*env)->SetIntField(env, lpObject, HIThemeButtonDrawInfoFc.value, (jint)lpStruct->value);
	(*env)->SetIntField(env, lpObject, HIThemeButtonDrawInfoFc.adornment, (jint)lpStruct->adornment);
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, HIThemeButtonDrawInfoFc.time);
	if (lpObject1 != NULL) setHIThemeAnimationTimeInfoFields(env, lpObject1, &lpStruct->animation.time);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, HIThemeButtonDrawInfoFc.frame);
	if (lpObject1 != NULL) setHIThemeAnimationFrameInfoFields(env, lpObject1, &lpStruct->animation.frame);
	}
}
#endif

#ifndef NO_HIThemeFrameDrawInfo
typedef struct HIThemeFrameDrawInfo_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID version, state, kind, isFocused;
} HIThemeFrameDrawInfo_FID_CACHE;

HIThemeFrameDrawInfo_FID_CACHE HIThemeFrameDrawInfoFc;

void cacheHIThemeFrameDrawInfoFields(JNIEnv *env, jobject lpObject)
{
	if (HIThemeFrameDrawInfoFc.cached) return;
	HIThemeFrameDrawInfoFc.clazz = (*env)->GetObjectClass(env, lpObject);
	HIThemeFrameDrawInfoFc.version = (*env)->GetFieldID(env, HIThemeFrameDrawInfoFc.clazz, "version", "I");
	HIThemeFrameDrawInfoFc.state = (*env)->GetFieldID(env, HIThemeFrameDrawInfoFc.clazz, "state", "I");
	HIThemeFrameDrawInfoFc.kind = (*env)->GetFieldID(env, HIThemeFrameDrawInfoFc.clazz, "kind", "I");
	HIThemeFrameDrawInfoFc.isFocused = (*env)->GetFieldID(env, HIThemeFrameDrawInfoFc.clazz, "isFocused", "Z");
	HIThemeFrameDrawInfoFc.cached = 1;
}

HIThemeFrameDrawInfo *getHIThemeFrameDrawInfoFields(JNIEnv *env, jobject lpObject, HIThemeFrameDrawInfo *lpStruct)
{
	if (!HIThemeFrameDrawInfoFc.cached) cacheHIThemeFrameDrawInfoFields(env, lpObject);
	lpStruct->version = (*env)->GetIntField(env, lpObject, HIThemeFrameDrawInfoFc.version);
	lpStruct->state = (*env)->GetIntField(env, lpObject, HIThemeFrameDrawInfoFc.state);
	lpStruct->kind = (*env)->GetIntField(env, lpObject, HIThemeFrameDrawInfoFc.kind);
	lpStruct->isFocused = (*env)->GetBooleanField(env, lpObject, HIThemeFrameDrawInfoFc.isFocused);
	return lpStruct;
}

void setHIThemeFrameDrawInfoFields(JNIEnv *env, jobject lpObject, HIThemeFrameDrawInfo *lpStruct)
{
	if (!HIThemeFrameDrawInfoFc.cached) cacheHIThemeFrameDrawInfoFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, HIThemeFrameDrawInfoFc.version, (jint)lpStruct->version);
	(*env)->SetIntField(env, lpObject, HIThemeFrameDrawInfoFc.state, (jint)lpStruct->state);
	(*env)->SetIntField(env, lpObject, HIThemeFrameDrawInfoFc.kind, (jint)lpStruct->kind);
	(*env)->SetBooleanField(env, lpObject, HIThemeFrameDrawInfoFc.isFocused, (jboolean)lpStruct->isFocused);
}
#endif

#ifndef NO_HIThemeGroupBoxDrawInfo
typedef struct HIThemeGroupBoxDrawInfo_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID version, state, kind;
} HIThemeGroupBoxDrawInfo_FID_CACHE;

HIThemeGroupBoxDrawInfo_FID_CACHE HIThemeGroupBoxDrawInfoFc;

void cacheHIThemeGroupBoxDrawInfoFields(JNIEnv *env, jobject lpObject)
{
	if (HIThemeGroupBoxDrawInfoFc.cached) return;
	HIThemeGroupBoxDrawInfoFc.clazz = (*env)->GetObjectClass(env, lpObject);
	HIThemeGroupBoxDrawInfoFc.version = (*env)->GetFieldID(env, HIThemeGroupBoxDrawInfoFc.clazz, "version", "I");
	HIThemeGroupBoxDrawInfoFc.state = (*env)->GetFieldID(env, HIThemeGroupBoxDrawInfoFc.clazz, "state", "I");
	HIThemeGroupBoxDrawInfoFc.kind = (*env)->GetFieldID(env, HIThemeGroupBoxDrawInfoFc.clazz, "kind", "I");
	HIThemeGroupBoxDrawInfoFc.cached = 1;
}

HIThemeGroupBoxDrawInfo *getHIThemeGroupBoxDrawInfoFields(JNIEnv *env, jobject lpObject, HIThemeGroupBoxDrawInfo *lpStruct)
{
	if (!HIThemeGroupBoxDrawInfoFc.cached) cacheHIThemeGroupBoxDrawInfoFields(env, lpObject);
	lpStruct->version = (*env)->GetIntField(env, lpObject, HIThemeGroupBoxDrawInfoFc.version);
	lpStruct->state = (*env)->GetIntField(env, lpObject, HIThemeGroupBoxDrawInfoFc.state);
	lpStruct->kind = (*env)->GetIntField(env, lpObject, HIThemeGroupBoxDrawInfoFc.kind);
	return lpStruct;
}

void setHIThemeGroupBoxDrawInfoFields(JNIEnv *env, jobject lpObject, HIThemeGroupBoxDrawInfo *lpStruct)
{
	if (!HIThemeGroupBoxDrawInfoFc.cached) cacheHIThemeGroupBoxDrawInfoFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, HIThemeGroupBoxDrawInfoFc.version, (jint)lpStruct->version);
	(*env)->SetIntField(env, lpObject, HIThemeGroupBoxDrawInfoFc.state, (jint)lpStruct->state);
	(*env)->SetIntField(env, lpObject, HIThemeGroupBoxDrawInfoFc.kind, (jint)lpStruct->kind);
}
#endif

#ifndef NO_HIThemeGrowBoxDrawInfo
typedef struct HIThemeGrowBoxDrawInfo_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID version, kind, state, direction, size;
} HIThemeGrowBoxDrawInfo_FID_CACHE;

HIThemeGrowBoxDrawInfo_FID_CACHE HIThemeGrowBoxDrawInfoFc;

void cacheHIThemeGrowBoxDrawInfoFields(JNIEnv *env, jobject lpObject)
{
	if (HIThemeGrowBoxDrawInfoFc.cached) return;
	HIThemeGrowBoxDrawInfoFc.clazz = (*env)->GetObjectClass(env, lpObject);
	HIThemeGrowBoxDrawInfoFc.version = (*env)->GetFieldID(env, HIThemeGrowBoxDrawInfoFc.clazz, "version", "I");
	HIThemeGrowBoxDrawInfoFc.kind = (*env)->GetFieldID(env, HIThemeGrowBoxDrawInfoFc.clazz, "kind", "I");
	HIThemeGrowBoxDrawInfoFc.state = (*env)->GetFieldID(env, HIThemeGrowBoxDrawInfoFc.clazz, "state", "I");
	HIThemeGrowBoxDrawInfoFc.direction = (*env)->GetFieldID(env, HIThemeGrowBoxDrawInfoFc.clazz, "direction", "S");
	HIThemeGrowBoxDrawInfoFc.size = (*env)->GetFieldID(env, HIThemeGrowBoxDrawInfoFc.clazz, "size", "I");
	HIThemeGrowBoxDrawInfoFc.cached = 1;
}

HIThemeGrowBoxDrawInfo *getHIThemeGrowBoxDrawInfoFields(JNIEnv *env, jobject lpObject, HIThemeGrowBoxDrawInfo *lpStruct)
{
	if (!HIThemeGrowBoxDrawInfoFc.cached) cacheHIThemeGrowBoxDrawInfoFields(env, lpObject);
	lpStruct->version = (*env)->GetIntField(env, lpObject, HIThemeGrowBoxDrawInfoFc.version);
	lpStruct->kind = (*env)->GetIntField(env, lpObject, HIThemeGrowBoxDrawInfoFc.kind);
	lpStruct->state = (*env)->GetIntField(env, lpObject, HIThemeGrowBoxDrawInfoFc.state);
	lpStruct->direction = (*env)->GetShortField(env, lpObject, HIThemeGrowBoxDrawInfoFc.direction);
	lpStruct->size = (*env)->GetIntField(env, lpObject, HIThemeGrowBoxDrawInfoFc.size);
	return lpStruct;
}

void setHIThemeGrowBoxDrawInfoFields(JNIEnv *env, jobject lpObject, HIThemeGrowBoxDrawInfo *lpStruct)
{
	if (!HIThemeGrowBoxDrawInfoFc.cached) cacheHIThemeGrowBoxDrawInfoFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, HIThemeGrowBoxDrawInfoFc.version, (jint)lpStruct->version);
	(*env)->SetIntField(env, lpObject, HIThemeGrowBoxDrawInfoFc.kind, (jint)lpStruct->kind);
	(*env)->SetIntField(env, lpObject, HIThemeGrowBoxDrawInfoFc.state, (jint)lpStruct->state);
	(*env)->SetShortField(env, lpObject, HIThemeGrowBoxDrawInfoFc.direction, (jshort)lpStruct->direction);
	(*env)->SetIntField(env, lpObject, HIThemeGrowBoxDrawInfoFc.size, (jint)lpStruct->size);
}
#endif

#ifndef NO_HIThemePopupArrowDrawInfo
typedef struct HIThemePopupArrowDrawInfo_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID version, state, orientation, size;
} HIThemePopupArrowDrawInfo_FID_CACHE;

HIThemePopupArrowDrawInfo_FID_CACHE HIThemePopupArrowDrawInfoFc;

void cacheHIThemePopupArrowDrawInfoFields(JNIEnv *env, jobject lpObject)
{
	if (HIThemePopupArrowDrawInfoFc.cached) return;
	HIThemePopupArrowDrawInfoFc.clazz = (*env)->GetObjectClass(env, lpObject);
	HIThemePopupArrowDrawInfoFc.version = (*env)->GetFieldID(env, HIThemePopupArrowDrawInfoFc.clazz, "version", "I");
	HIThemePopupArrowDrawInfoFc.state = (*env)->GetFieldID(env, HIThemePopupArrowDrawInfoFc.clazz, "state", "I");
	HIThemePopupArrowDrawInfoFc.orientation = (*env)->GetFieldID(env, HIThemePopupArrowDrawInfoFc.clazz, "orientation", "S");
	HIThemePopupArrowDrawInfoFc.size = (*env)->GetFieldID(env, HIThemePopupArrowDrawInfoFc.clazz, "size", "S");
	HIThemePopupArrowDrawInfoFc.cached = 1;
}

HIThemePopupArrowDrawInfo *getHIThemePopupArrowDrawInfoFields(JNIEnv *env, jobject lpObject, HIThemePopupArrowDrawInfo *lpStruct)
{
	if (!HIThemePopupArrowDrawInfoFc.cached) cacheHIThemePopupArrowDrawInfoFields(env, lpObject);
	lpStruct->version = (*env)->GetIntField(env, lpObject, HIThemePopupArrowDrawInfoFc.version);
	lpStruct->state = (*env)->GetIntField(env, lpObject, HIThemePopupArrowDrawInfoFc.state);
	lpStruct->orientation = (*env)->GetShortField(env, lpObject, HIThemePopupArrowDrawInfoFc.orientation);
	lpStruct->size = (*env)->GetShortField(env, lpObject, HIThemePopupArrowDrawInfoFc.size);
	return lpStruct;
}

void setHIThemePopupArrowDrawInfoFields(JNIEnv *env, jobject lpObject, HIThemePopupArrowDrawInfo *lpStruct)
{
	if (!HIThemePopupArrowDrawInfoFc.cached) cacheHIThemePopupArrowDrawInfoFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, HIThemePopupArrowDrawInfoFc.version, (jint)lpStruct->version);
	(*env)->SetIntField(env, lpObject, HIThemePopupArrowDrawInfoFc.state, (jint)lpStruct->state);
	(*env)->SetShortField(env, lpObject, HIThemePopupArrowDrawInfoFc.orientation, (jshort)lpStruct->orientation);
	(*env)->SetShortField(env, lpObject, HIThemePopupArrowDrawInfoFc.size, (jshort)lpStruct->size);
}
#endif

#ifndef NO_HIThemeSeparatorDrawInfo
typedef struct HIThemeSeparatorDrawInfo_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID version, state;
} HIThemeSeparatorDrawInfo_FID_CACHE;

HIThemeSeparatorDrawInfo_FID_CACHE HIThemeSeparatorDrawInfoFc;

void cacheHIThemeSeparatorDrawInfoFields(JNIEnv *env, jobject lpObject)
{
	if (HIThemeSeparatorDrawInfoFc.cached) return;
	HIThemeSeparatorDrawInfoFc.clazz = (*env)->GetObjectClass(env, lpObject);
	HIThemeSeparatorDrawInfoFc.version = (*env)->GetFieldID(env, HIThemeSeparatorDrawInfoFc.clazz, "version", "I");
	HIThemeSeparatorDrawInfoFc.state = (*env)->GetFieldID(env, HIThemeSeparatorDrawInfoFc.clazz, "state", "I");
	HIThemeSeparatorDrawInfoFc.cached = 1;
}

HIThemeSeparatorDrawInfo *getHIThemeSeparatorDrawInfoFields(JNIEnv *env, jobject lpObject, HIThemeSeparatorDrawInfo *lpStruct)
{
	if (!HIThemeSeparatorDrawInfoFc.cached) cacheHIThemeSeparatorDrawInfoFields(env, lpObject);
	lpStruct->version = (*env)->GetIntField(env, lpObject, HIThemeSeparatorDrawInfoFc.version);
	lpStruct->state = (*env)->GetIntField(env, lpObject, HIThemeSeparatorDrawInfoFc.state);
	return lpStruct;
}

void setHIThemeSeparatorDrawInfoFields(JNIEnv *env, jobject lpObject, HIThemeSeparatorDrawInfo *lpStruct)
{
	if (!HIThemeSeparatorDrawInfoFc.cached) cacheHIThemeSeparatorDrawInfoFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, HIThemeSeparatorDrawInfoFc.version, (jint)lpStruct->version);
	(*env)->SetIntField(env, lpObject, HIThemeSeparatorDrawInfoFc.state, (jint)lpStruct->state);
}
#endif

#ifndef NO_HIThemeTabDrawInfo
typedef struct HIThemeTabDrawInfo_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID version, style, direction, size, adornment, kind, position;
} HIThemeTabDrawInfo_FID_CACHE;

HIThemeTabDrawInfo_FID_CACHE HIThemeTabDrawInfoFc;

void cacheHIThemeTabDrawInfoFields(JNIEnv *env, jobject lpObject)
{
	if (HIThemeTabDrawInfoFc.cached) return;
	HIThemeTabDrawInfoFc.clazz = (*env)->GetObjectClass(env, lpObject);
	HIThemeTabDrawInfoFc.version = (*env)->GetFieldID(env, HIThemeTabDrawInfoFc.clazz, "version", "I");
	HIThemeTabDrawInfoFc.style = (*env)->GetFieldID(env, HIThemeTabDrawInfoFc.clazz, "style", "S");
	HIThemeTabDrawInfoFc.direction = (*env)->GetFieldID(env, HIThemeTabDrawInfoFc.clazz, "direction", "S");
	HIThemeTabDrawInfoFc.size = (*env)->GetFieldID(env, HIThemeTabDrawInfoFc.clazz, "size", "I");
	HIThemeTabDrawInfoFc.adornment = (*env)->GetFieldID(env, HIThemeTabDrawInfoFc.clazz, "adornment", "I");
	HIThemeTabDrawInfoFc.kind = (*env)->GetFieldID(env, HIThemeTabDrawInfoFc.clazz, "kind", "I");
	HIThemeTabDrawInfoFc.position = (*env)->GetFieldID(env, HIThemeTabDrawInfoFc.clazz, "position", "I");
	HIThemeTabDrawInfoFc.cached = 1;
}

HIThemeTabDrawInfo *getHIThemeTabDrawInfoFields(JNIEnv *env, jobject lpObject, HIThemeTabDrawInfo *lpStruct)
{
	if (!HIThemeTabDrawInfoFc.cached) cacheHIThemeTabDrawInfoFields(env, lpObject);
	lpStruct->version = (*env)->GetIntField(env, lpObject, HIThemeTabDrawInfoFc.version);
	lpStruct->style = (*env)->GetShortField(env, lpObject, HIThemeTabDrawInfoFc.style);
	lpStruct->direction = (*env)->GetShortField(env, lpObject, HIThemeTabDrawInfoFc.direction);
	lpStruct->size = (*env)->GetIntField(env, lpObject, HIThemeTabDrawInfoFc.size);
	lpStruct->adornment = (*env)->GetIntField(env, lpObject, HIThemeTabDrawInfoFc.adornment);
#if MAC_OS_X_VERSION_MAX_ALLOWED >= MAC_OS_X_VERSION_10_4
	lpStruct->kind = (*env)->GetIntField(env, lpObject, HIThemeTabDrawInfoFc.kind);
#endif
#if MAC_OS_X_VERSION_MAX_ALLOWED >= MAC_OS_X_VERSION_10_4
	lpStruct->position = (*env)->GetIntField(env, lpObject, HIThemeTabDrawInfoFc.position);
#endif
	return lpStruct;
}

void setHIThemeTabDrawInfoFields(JNIEnv *env, jobject lpObject, HIThemeTabDrawInfo *lpStruct)
{
	if (!HIThemeTabDrawInfoFc.cached) cacheHIThemeTabDrawInfoFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, HIThemeTabDrawInfoFc.version, (jint)lpStruct->version);
	(*env)->SetShortField(env, lpObject, HIThemeTabDrawInfoFc.style, (jshort)lpStruct->style);
	(*env)->SetShortField(env, lpObject, HIThemeTabDrawInfoFc.direction, (jshort)lpStruct->direction);
	(*env)->SetIntField(env, lpObject, HIThemeTabDrawInfoFc.size, (jint)lpStruct->size);
	(*env)->SetIntField(env, lpObject, HIThemeTabDrawInfoFc.adornment, (jint)lpStruct->adornment);
#if MAC_OS_X_VERSION_MAX_ALLOWED >= MAC_OS_X_VERSION_10_4
	(*env)->SetIntField(env, lpObject, HIThemeTabDrawInfoFc.kind, (jint)lpStruct->kind);
#endif
#if MAC_OS_X_VERSION_MAX_ALLOWED >= MAC_OS_X_VERSION_10_4
	(*env)->SetIntField(env, lpObject, HIThemeTabDrawInfoFc.position, (jint)lpStruct->position);
#endif
}
#endif

#ifndef NO_HIThemeTabPaneDrawInfo
typedef struct HIThemeTabPaneDrawInfo_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID version, state, direction, size, kind, adornment;
} HIThemeTabPaneDrawInfo_FID_CACHE;

HIThemeTabPaneDrawInfo_FID_CACHE HIThemeTabPaneDrawInfoFc;

void cacheHIThemeTabPaneDrawInfoFields(JNIEnv *env, jobject lpObject)
{
	if (HIThemeTabPaneDrawInfoFc.cached) return;
	HIThemeTabPaneDrawInfoFc.clazz = (*env)->GetObjectClass(env, lpObject);
	HIThemeTabPaneDrawInfoFc.version = (*env)->GetFieldID(env, HIThemeTabPaneDrawInfoFc.clazz, "version", "I");
	HIThemeTabPaneDrawInfoFc.state = (*env)->GetFieldID(env, HIThemeTabPaneDrawInfoFc.clazz, "state", "I");
	HIThemeTabPaneDrawInfoFc.direction = (*env)->GetFieldID(env, HIThemeTabPaneDrawInfoFc.clazz, "direction", "S");
	HIThemeTabPaneDrawInfoFc.size = (*env)->GetFieldID(env, HIThemeTabPaneDrawInfoFc.clazz, "size", "I");
	HIThemeTabPaneDrawInfoFc.kind = (*env)->GetFieldID(env, HIThemeTabPaneDrawInfoFc.clazz, "kind", "I");
	HIThemeTabPaneDrawInfoFc.adornment = (*env)->GetFieldID(env, HIThemeTabPaneDrawInfoFc.clazz, "adornment", "I");
	HIThemeTabPaneDrawInfoFc.cached = 1;
}

HIThemeTabPaneDrawInfo *getHIThemeTabPaneDrawInfoFields(JNIEnv *env, jobject lpObject, HIThemeTabPaneDrawInfo *lpStruct)
{
	if (!HIThemeTabPaneDrawInfoFc.cached) cacheHIThemeTabPaneDrawInfoFields(env, lpObject);
	lpStruct->version = (*env)->GetIntField(env, lpObject, HIThemeTabPaneDrawInfoFc.version);
	lpStruct->state = (*env)->GetIntField(env, lpObject, HIThemeTabPaneDrawInfoFc.state);
	lpStruct->direction = (*env)->GetShortField(env, lpObject, HIThemeTabPaneDrawInfoFc.direction);
	lpStruct->size = (*env)->GetIntField(env, lpObject, HIThemeTabPaneDrawInfoFc.size);
#if MAC_OS_X_VERSION_MAX_ALLOWED >= MAC_OS_X_VERSION_10_4
	lpStruct->kind = (*env)->GetIntField(env, lpObject, HIThemeTabPaneDrawInfoFc.kind);
#endif
#if MAC_OS_X_VERSION_MAX_ALLOWED >= MAC_OS_X_VERSION_10_4
	lpStruct->adornment = (*env)->GetIntField(env, lpObject, HIThemeTabPaneDrawInfoFc.adornment);
#endif
	return lpStruct;
}

void setHIThemeTabPaneDrawInfoFields(JNIEnv *env, jobject lpObject, HIThemeTabPaneDrawInfo *lpStruct)
{
	if (!HIThemeTabPaneDrawInfoFc.cached) cacheHIThemeTabPaneDrawInfoFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, HIThemeTabPaneDrawInfoFc.version, (jint)lpStruct->version);
	(*env)->SetIntField(env, lpObject, HIThemeTabPaneDrawInfoFc.state, (jint)lpStruct->state);
	(*env)->SetShortField(env, lpObject, HIThemeTabPaneDrawInfoFc.direction, (jshort)lpStruct->direction);
	(*env)->SetIntField(env, lpObject, HIThemeTabPaneDrawInfoFc.size, (jint)lpStruct->size);
#if MAC_OS_X_VERSION_MAX_ALLOWED >= MAC_OS_X_VERSION_10_4
	(*env)->SetIntField(env, lpObject, HIThemeTabPaneDrawInfoFc.kind, (jint)lpStruct->kind);
#endif
#if MAC_OS_X_VERSION_MAX_ALLOWED >= MAC_OS_X_VERSION_10_4
	(*env)->SetIntField(env, lpObject, HIThemeTabPaneDrawInfoFc.adornment, (jint)lpStruct->adornment);
#endif
}
#endif

#ifndef NO_HIThemeTextInfo
typedef struct HIThemeTextInfo_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID version, state, fontID, horizontalFlushness, verticalFlushness, options, truncationPosition, truncationMaxLines, truncationHappened;
} HIThemeTextInfo_FID_CACHE;

HIThemeTextInfo_FID_CACHE HIThemeTextInfoFc;

void cacheHIThemeTextInfoFields(JNIEnv *env, jobject lpObject)
{
	if (HIThemeTextInfoFc.cached) return;
	HIThemeTextInfoFc.clazz = (*env)->GetObjectClass(env, lpObject);
	HIThemeTextInfoFc.version = (*env)->GetFieldID(env, HIThemeTextInfoFc.clazz, "version", "I");
	HIThemeTextInfoFc.state = (*env)->GetFieldID(env, HIThemeTextInfoFc.clazz, "state", "I");
	HIThemeTextInfoFc.fontID = (*env)->GetFieldID(env, HIThemeTextInfoFc.clazz, "fontID", "S");
	HIThemeTextInfoFc.horizontalFlushness = (*env)->GetFieldID(env, HIThemeTextInfoFc.clazz, "horizontalFlushness", "I");
	HIThemeTextInfoFc.verticalFlushness = (*env)->GetFieldID(env, HIThemeTextInfoFc.clazz, "verticalFlushness", "I");
	HIThemeTextInfoFc.options = (*env)->GetFieldID(env, HIThemeTextInfoFc.clazz, "options", "I");
	HIThemeTextInfoFc.truncationPosition = (*env)->GetFieldID(env, HIThemeTextInfoFc.clazz, "truncationPosition", "I");
	HIThemeTextInfoFc.truncationMaxLines = (*env)->GetFieldID(env, HIThemeTextInfoFc.clazz, "truncationMaxLines", "I");
	HIThemeTextInfoFc.truncationHappened = (*env)->GetFieldID(env, HIThemeTextInfoFc.clazz, "truncationHappened", "Z");
	HIThemeTextInfoFc.cached = 1;
}

HIThemeTextInfo *getHIThemeTextInfoFields(JNIEnv *env, jobject lpObject, HIThemeTextInfo *lpStruct)
{
	if (!HIThemeTextInfoFc.cached) cacheHIThemeTextInfoFields(env, lpObject);
	lpStruct->version = (*env)->GetIntField(env, lpObject, HIThemeTextInfoFc.version);
	lpStruct->state = (*env)->GetIntField(env, lpObject, HIThemeTextInfoFc.state);
	lpStruct->fontID = (*env)->GetShortField(env, lpObject, HIThemeTextInfoFc.fontID);
	lpStruct->horizontalFlushness = (*env)->GetIntField(env, lpObject, HIThemeTextInfoFc.horizontalFlushness);
	lpStruct->verticalFlushness = (*env)->GetIntField(env, lpObject, HIThemeTextInfoFc.verticalFlushness);
	lpStruct->options = (*env)->GetIntField(env, lpObject, HIThemeTextInfoFc.options);
	lpStruct->truncationPosition = (*env)->GetIntField(env, lpObject, HIThemeTextInfoFc.truncationPosition);
	lpStruct->truncationMaxLines = (*env)->GetIntField(env, lpObject, HIThemeTextInfoFc.truncationMaxLines);
	lpStruct->truncationHappened = (*env)->GetBooleanField(env, lpObject, HIThemeTextInfoFc.truncationHappened);
	return lpStruct;
}

void setHIThemeTextInfoFields(JNIEnv *env, jobject lpObject, HIThemeTextInfo *lpStruct)
{
	if (!HIThemeTextInfoFc.cached) cacheHIThemeTextInfoFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, HIThemeTextInfoFc.version, (jint)lpStruct->version);
	(*env)->SetIntField(env, lpObject, HIThemeTextInfoFc.state, (jint)lpStruct->state);
	(*env)->SetShortField(env, lpObject, HIThemeTextInfoFc.fontID, (jshort)lpStruct->fontID);
	(*env)->SetIntField(env, lpObject, HIThemeTextInfoFc.horizontalFlushness, (jint)lpStruct->horizontalFlushness);
	(*env)->SetIntField(env, lpObject, HIThemeTextInfoFc.verticalFlushness, (jint)lpStruct->verticalFlushness);
	(*env)->SetIntField(env, lpObject, HIThemeTextInfoFc.options, (jint)lpStruct->options);
	(*env)->SetIntField(env, lpObject, HIThemeTextInfoFc.truncationPosition, (jint)lpStruct->truncationPosition);
	(*env)->SetIntField(env, lpObject, HIThemeTextInfoFc.truncationMaxLines, (jint)lpStruct->truncationMaxLines);
	(*env)->SetBooleanField(env, lpObject, HIThemeTextInfoFc.truncationHappened, (jboolean)lpStruct->truncationHappened);
}
#endif

#ifndef NO_HIThemeTrackDrawInfo
typedef struct HIThemeTrackDrawInfo_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID version, kind, bounds_x, bounds_y, bounds_width, bounds_height, min, max, value, reserved, attributes, enableState, filler1, scrollbar, slider, progress;
} HIThemeTrackDrawInfo_FID_CACHE;

HIThemeTrackDrawInfo_FID_CACHE HIThemeTrackDrawInfoFc;

void cacheHIThemeTrackDrawInfoFields(JNIEnv *env, jobject lpObject)
{
	if (HIThemeTrackDrawInfoFc.cached) return;
	HIThemeTrackDrawInfoFc.clazz = (*env)->GetObjectClass(env, lpObject);
	HIThemeTrackDrawInfoFc.version = (*env)->GetFieldID(env, HIThemeTrackDrawInfoFc.clazz, "version", "I");
	HIThemeTrackDrawInfoFc.kind = (*env)->GetFieldID(env, HIThemeTrackDrawInfoFc.clazz, "kind", "S");
	HIThemeTrackDrawInfoFc.bounds_x = (*env)->GetFieldID(env, HIThemeTrackDrawInfoFc.clazz, "bounds_x", "F");
	HIThemeTrackDrawInfoFc.bounds_y = (*env)->GetFieldID(env, HIThemeTrackDrawInfoFc.clazz, "bounds_y", "F");
	HIThemeTrackDrawInfoFc.bounds_width = (*env)->GetFieldID(env, HIThemeTrackDrawInfoFc.clazz, "bounds_width", "F");
	HIThemeTrackDrawInfoFc.bounds_height = (*env)->GetFieldID(env, HIThemeTrackDrawInfoFc.clazz, "bounds_height", "F");
	HIThemeTrackDrawInfoFc.min = (*env)->GetFieldID(env, HIThemeTrackDrawInfoFc.clazz, "min", "I");
	HIThemeTrackDrawInfoFc.max = (*env)->GetFieldID(env, HIThemeTrackDrawInfoFc.clazz, "max", "I");
	HIThemeTrackDrawInfoFc.value = (*env)->GetFieldID(env, HIThemeTrackDrawInfoFc.clazz, "value", "I");
	HIThemeTrackDrawInfoFc.reserved = (*env)->GetFieldID(env, HIThemeTrackDrawInfoFc.clazz, "reserved", "I");
	HIThemeTrackDrawInfoFc.attributes = (*env)->GetFieldID(env, HIThemeTrackDrawInfoFc.clazz, "attributes", "S");
	HIThemeTrackDrawInfoFc.enableState = (*env)->GetFieldID(env, HIThemeTrackDrawInfoFc.clazz, "enableState", "B");
	HIThemeTrackDrawInfoFc.filler1 = (*env)->GetFieldID(env, HIThemeTrackDrawInfoFc.clazz, "filler1", "B");
	HIThemeTrackDrawInfoFc.scrollbar = (*env)->GetFieldID(env, HIThemeTrackDrawInfoFc.clazz, "scrollbar", "Lorg/eclipse/swt/internal/carbon/ScrollBarTrackInfo;");
	HIThemeTrackDrawInfoFc.slider = (*env)->GetFieldID(env, HIThemeTrackDrawInfoFc.clazz, "slider", "Lorg/eclipse/swt/internal/carbon/SliderTrackInfo;");
	HIThemeTrackDrawInfoFc.progress = (*env)->GetFieldID(env, HIThemeTrackDrawInfoFc.clazz, "progress", "Lorg/eclipse/swt/internal/carbon/ProgressTrackInfo;");
	HIThemeTrackDrawInfoFc.cached = 1;
}

HIThemeTrackDrawInfo *getHIThemeTrackDrawInfoFields(JNIEnv *env, jobject lpObject, HIThemeTrackDrawInfo *lpStruct)
{
	if (!HIThemeTrackDrawInfoFc.cached) cacheHIThemeTrackDrawInfoFields(env, lpObject);
	lpStruct->version = (*env)->GetIntField(env, lpObject, HIThemeTrackDrawInfoFc.version);
	lpStruct->kind = (ThemeTrackKind)(*env)->GetShortField(env, lpObject, HIThemeTrackDrawInfoFc.kind);
	lpStruct->bounds.origin.x = (*env)->GetFloatField(env, lpObject, HIThemeTrackDrawInfoFc.bounds_x);
	lpStruct->bounds.origin.y = (*env)->GetFloatField(env, lpObject, HIThemeTrackDrawInfoFc.bounds_y);
	lpStruct->bounds.size.width = (*env)->GetFloatField(env, lpObject, HIThemeTrackDrawInfoFc.bounds_width);
	lpStruct->bounds.size.height = (*env)->GetFloatField(env, lpObject, HIThemeTrackDrawInfoFc.bounds_height);
	lpStruct->min = (*env)->GetIntField(env, lpObject, HIThemeTrackDrawInfoFc.min);
	lpStruct->max = (*env)->GetIntField(env, lpObject, HIThemeTrackDrawInfoFc.max);
	lpStruct->value = (*env)->GetIntField(env, lpObject, HIThemeTrackDrawInfoFc.value);
	lpStruct->reserved = (*env)->GetIntField(env, lpObject, HIThemeTrackDrawInfoFc.reserved);
	lpStruct->attributes = (ThemeTrackAttributes)(*env)->GetShortField(env, lpObject, HIThemeTrackDrawInfoFc.attributes);
	lpStruct->enableState = (ThemeTrackEnableState)(*env)->GetByteField(env, lpObject, HIThemeTrackDrawInfoFc.enableState);
	lpStruct->filler1 = (*env)->GetByteField(env, lpObject, HIThemeTrackDrawInfoFc.filler1);
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, HIThemeTrackDrawInfoFc.scrollbar);
	if (lpObject1 != NULL) getScrollBarTrackInfoFields(env, lpObject1, &lpStruct->trackInfo.scrollbar);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, HIThemeTrackDrawInfoFc.slider);
	if (lpObject1 != NULL) getSliderTrackInfoFields(env, lpObject1, &lpStruct->trackInfo.slider);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, HIThemeTrackDrawInfoFc.progress);
	if (lpObject1 != NULL) getProgressTrackInfoFields(env, lpObject1, &lpStruct->trackInfo.progress);
	}
	return lpStruct;
}

void setHIThemeTrackDrawInfoFields(JNIEnv *env, jobject lpObject, HIThemeTrackDrawInfo *lpStruct)
{
	if (!HIThemeTrackDrawInfoFc.cached) cacheHIThemeTrackDrawInfoFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, HIThemeTrackDrawInfoFc.version, (jint)lpStruct->version);
	(*env)->SetShortField(env, lpObject, HIThemeTrackDrawInfoFc.kind, (jshort)lpStruct->kind);
	(*env)->SetFloatField(env, lpObject, HIThemeTrackDrawInfoFc.bounds_x, (jfloat)lpStruct->bounds.origin.x);
	(*env)->SetFloatField(env, lpObject, HIThemeTrackDrawInfoFc.bounds_y, (jfloat)lpStruct->bounds.origin.y);
	(*env)->SetFloatField(env, lpObject, HIThemeTrackDrawInfoFc.bounds_width, (jfloat)lpStruct->bounds.size.width);
	(*env)->SetFloatField(env, lpObject, HIThemeTrackDrawInfoFc.bounds_height, (jfloat)lpStruct->bounds.size.height);
	(*env)->SetIntField(env, lpObject, HIThemeTrackDrawInfoFc.min, (jint)lpStruct->min);
	(*env)->SetIntField(env, lpObject, HIThemeTrackDrawInfoFc.max, (jint)lpStruct->max);
	(*env)->SetIntField(env, lpObject, HIThemeTrackDrawInfoFc.value, (jint)lpStruct->value);
	(*env)->SetIntField(env, lpObject, HIThemeTrackDrawInfoFc.reserved, (jint)lpStruct->reserved);
	(*env)->SetShortField(env, lpObject, HIThemeTrackDrawInfoFc.attributes, (jshort)lpStruct->attributes);
	(*env)->SetByteField(env, lpObject, HIThemeTrackDrawInfoFc.enableState, (jbyte)lpStruct->enableState);
	(*env)->SetByteField(env, lpObject, HIThemeTrackDrawInfoFc.filler1, (jbyte)lpStruct->filler1);
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, HIThemeTrackDrawInfoFc.scrollbar);
	if (lpObject1 != NULL) setScrollBarTrackInfoFields(env, lpObject1, &lpStruct->trackInfo.scrollbar);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, HIThemeTrackDrawInfoFc.slider);
	if (lpObject1 != NULL) setSliderTrackInfoFields(env, lpObject1, &lpStruct->trackInfo.slider);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, HIThemeTrackDrawInfoFc.progress);
	if (lpObject1 != NULL) setProgressTrackInfoFields(env, lpObject1, &lpStruct->trackInfo.progress);
	}
}
#endif

#ifndef NO_HMHelpContentRec
typedef struct HMHelpContentRec_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID version, absHotRect_top, absHotRect_left, absHotRect_bottom, absHotRect_right, tagSide, content0_contentType, content0_tagCFString, content1_contentType, content1_tagCFString;
} HMHelpContentRec_FID_CACHE;

HMHelpContentRec_FID_CACHE HMHelpContentRecFc;

void cacheHMHelpContentRecFields(JNIEnv *env, jobject lpObject)
{
	if (HMHelpContentRecFc.cached) return;
	HMHelpContentRecFc.clazz = (*env)->GetObjectClass(env, lpObject);
	HMHelpContentRecFc.version = (*env)->GetFieldID(env, HMHelpContentRecFc.clazz, "version", "I");
	HMHelpContentRecFc.absHotRect_top = (*env)->GetFieldID(env, HMHelpContentRecFc.clazz, "absHotRect_top", "S");
	HMHelpContentRecFc.absHotRect_left = (*env)->GetFieldID(env, HMHelpContentRecFc.clazz, "absHotRect_left", "S");
	HMHelpContentRecFc.absHotRect_bottom = (*env)->GetFieldID(env, HMHelpContentRecFc.clazz, "absHotRect_bottom", "S");
	HMHelpContentRecFc.absHotRect_right = (*env)->GetFieldID(env, HMHelpContentRecFc.clazz, "absHotRect_right", "S");
	HMHelpContentRecFc.tagSide = (*env)->GetFieldID(env, HMHelpContentRecFc.clazz, "tagSide", "S");
	HMHelpContentRecFc.content0_contentType = (*env)->GetFieldID(env, HMHelpContentRecFc.clazz, "content0_contentType", "I");
	HMHelpContentRecFc.content0_tagCFString = (*env)->GetFieldID(env, HMHelpContentRecFc.clazz, "content0_tagCFString", "I");
	HMHelpContentRecFc.content1_contentType = (*env)->GetFieldID(env, HMHelpContentRecFc.clazz, "content1_contentType", "I");
	HMHelpContentRecFc.content1_tagCFString = (*env)->GetFieldID(env, HMHelpContentRecFc.clazz, "content1_tagCFString", "I");
	HMHelpContentRecFc.cached = 1;
}

HMHelpContentRec *getHMHelpContentRecFields(JNIEnv *env, jobject lpObject, HMHelpContentRec *lpStruct)
{
	if (!HMHelpContentRecFc.cached) cacheHMHelpContentRecFields(env, lpObject);
	lpStruct->version = (*env)->GetIntField(env, lpObject, HMHelpContentRecFc.version);
	lpStruct->absHotRect.top = (*env)->GetShortField(env, lpObject, HMHelpContentRecFc.absHotRect_top);
	lpStruct->absHotRect.left = (*env)->GetShortField(env, lpObject, HMHelpContentRecFc.absHotRect_left);
	lpStruct->absHotRect.bottom = (*env)->GetShortField(env, lpObject, HMHelpContentRecFc.absHotRect_bottom);
	lpStruct->absHotRect.right = (*env)->GetShortField(env, lpObject, HMHelpContentRecFc.absHotRect_right);
	lpStruct->tagSide = (*env)->GetShortField(env, lpObject, HMHelpContentRecFc.tagSide);
	lpStruct->content[0].contentType = (*env)->GetIntField(env, lpObject, HMHelpContentRecFc.content0_contentType);
	lpStruct->content[0].u.tagCFString = (CFStringRef)(*env)->GetIntField(env, lpObject, HMHelpContentRecFc.content0_tagCFString);
	lpStruct->content[1].contentType = (*env)->GetIntField(env, lpObject, HMHelpContentRecFc.content1_contentType);
	lpStruct->content[1].u.tagCFString = (CFStringRef)(*env)->GetIntField(env, lpObject, HMHelpContentRecFc.content1_tagCFString);
	return lpStruct;
}

void setHMHelpContentRecFields(JNIEnv *env, jobject lpObject, HMHelpContentRec *lpStruct)
{
	if (!HMHelpContentRecFc.cached) cacheHMHelpContentRecFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, HMHelpContentRecFc.version, (jint)lpStruct->version);
	(*env)->SetShortField(env, lpObject, HMHelpContentRecFc.absHotRect_top, (jshort)lpStruct->absHotRect.top);
	(*env)->SetShortField(env, lpObject, HMHelpContentRecFc.absHotRect_left, (jshort)lpStruct->absHotRect.left);
	(*env)->SetShortField(env, lpObject, HMHelpContentRecFc.absHotRect_bottom, (jshort)lpStruct->absHotRect.bottom);
	(*env)->SetShortField(env, lpObject, HMHelpContentRecFc.absHotRect_right, (jshort)lpStruct->absHotRect.right);
	(*env)->SetShortField(env, lpObject, HMHelpContentRecFc.tagSide, (jshort)lpStruct->tagSide);
	(*env)->SetIntField(env, lpObject, HMHelpContentRecFc.content0_contentType, (jint)lpStruct->content[0].contentType);
	(*env)->SetIntField(env, lpObject, HMHelpContentRecFc.content0_tagCFString, (jint)lpStruct->content[0].u.tagCFString);
	(*env)->SetIntField(env, lpObject, HMHelpContentRecFc.content1_contentType, (jint)lpStruct->content[1].contentType);
	(*env)->SetIntField(env, lpObject, HMHelpContentRecFc.content1_tagCFString, (jint)lpStruct->content[1].u.tagCFString);
}
#endif

#ifndef NO_LSApplicationParameters
typedef struct LSApplicationParameters_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID version, flags, application, asyncLaunchRefCon, environment, argv, initialEvent;
} LSApplicationParameters_FID_CACHE;

LSApplicationParameters_FID_CACHE LSApplicationParametersFc;

void cacheLSApplicationParametersFields(JNIEnv *env, jobject lpObject)
{
	if (LSApplicationParametersFc.cached) return;
	LSApplicationParametersFc.clazz = (*env)->GetObjectClass(env, lpObject);
	LSApplicationParametersFc.version = (*env)->GetFieldID(env, LSApplicationParametersFc.clazz, "version", "I");
	LSApplicationParametersFc.flags = (*env)->GetFieldID(env, LSApplicationParametersFc.clazz, "flags", "I");
	LSApplicationParametersFc.application = (*env)->GetFieldID(env, LSApplicationParametersFc.clazz, "application", "I");
	LSApplicationParametersFc.asyncLaunchRefCon = (*env)->GetFieldID(env, LSApplicationParametersFc.clazz, "asyncLaunchRefCon", "I");
	LSApplicationParametersFc.environment = (*env)->GetFieldID(env, LSApplicationParametersFc.clazz, "environment", "I");
	LSApplicationParametersFc.argv = (*env)->GetFieldID(env, LSApplicationParametersFc.clazz, "argv", "I");
	LSApplicationParametersFc.initialEvent = (*env)->GetFieldID(env, LSApplicationParametersFc.clazz, "initialEvent", "I");
	LSApplicationParametersFc.cached = 1;
}

LSApplicationParameters *getLSApplicationParametersFields(JNIEnv *env, jobject lpObject, LSApplicationParameters *lpStruct)
{
	if (!LSApplicationParametersFc.cached) cacheLSApplicationParametersFields(env, lpObject);
	lpStruct->version = (*env)->GetIntField(env, lpObject, LSApplicationParametersFc.version);
	lpStruct->flags = (*env)->GetIntField(env, lpObject, LSApplicationParametersFc.flags);
	lpStruct->application = (const FSRef *)(*env)->GetIntField(env, lpObject, LSApplicationParametersFc.application);
	lpStruct->asyncLaunchRefCon = (void *)(*env)->GetIntField(env, lpObject, LSApplicationParametersFc.asyncLaunchRefCon);
	lpStruct->environment = (CFDictionaryRef)(*env)->GetIntField(env, lpObject, LSApplicationParametersFc.environment);
	lpStruct->argv = (CFArrayRef)(*env)->GetIntField(env, lpObject, LSApplicationParametersFc.argv);
	lpStruct->initialEvent = (AppleEvent *)(*env)->GetIntField(env, lpObject, LSApplicationParametersFc.initialEvent);
	return lpStruct;
}

void setLSApplicationParametersFields(JNIEnv *env, jobject lpObject, LSApplicationParameters *lpStruct)
{
	if (!LSApplicationParametersFc.cached) cacheLSApplicationParametersFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, LSApplicationParametersFc.version, (jint)lpStruct->version);
	(*env)->SetIntField(env, lpObject, LSApplicationParametersFc.flags, (jint)lpStruct->flags);
	(*env)->SetIntField(env, lpObject, LSApplicationParametersFc.application, (jint)lpStruct->application);
	(*env)->SetIntField(env, lpObject, LSApplicationParametersFc.asyncLaunchRefCon, (jint)lpStruct->asyncLaunchRefCon);
	(*env)->SetIntField(env, lpObject, LSApplicationParametersFc.environment, (jint)lpStruct->environment);
	(*env)->SetIntField(env, lpObject, LSApplicationParametersFc.argv, (jint)lpStruct->argv);
	(*env)->SetIntField(env, lpObject, LSApplicationParametersFc.initialEvent, (jint)lpStruct->initialEvent);
}
#endif

#ifndef NO_LongDateRec
typedef struct LongDateRec_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID era, year, month, day, hour, minute, second, dayOfWeek, dayOfYear, weekOfYear, pm, res1, res2, res3;
} LongDateRec_FID_CACHE;

LongDateRec_FID_CACHE LongDateRecFc;

void cacheLongDateRecFields(JNIEnv *env, jobject lpObject)
{
	if (LongDateRecFc.cached) return;
	LongDateRecFc.clazz = (*env)->GetObjectClass(env, lpObject);
	LongDateRecFc.era = (*env)->GetFieldID(env, LongDateRecFc.clazz, "era", "S");
	LongDateRecFc.year = (*env)->GetFieldID(env, LongDateRecFc.clazz, "year", "S");
	LongDateRecFc.month = (*env)->GetFieldID(env, LongDateRecFc.clazz, "month", "S");
	LongDateRecFc.day = (*env)->GetFieldID(env, LongDateRecFc.clazz, "day", "S");
	LongDateRecFc.hour = (*env)->GetFieldID(env, LongDateRecFc.clazz, "hour", "S");
	LongDateRecFc.minute = (*env)->GetFieldID(env, LongDateRecFc.clazz, "minute", "S");
	LongDateRecFc.second = (*env)->GetFieldID(env, LongDateRecFc.clazz, "second", "S");
	LongDateRecFc.dayOfWeek = (*env)->GetFieldID(env, LongDateRecFc.clazz, "dayOfWeek", "S");
	LongDateRecFc.dayOfYear = (*env)->GetFieldID(env, LongDateRecFc.clazz, "dayOfYear", "S");
	LongDateRecFc.weekOfYear = (*env)->GetFieldID(env, LongDateRecFc.clazz, "weekOfYear", "S");
	LongDateRecFc.pm = (*env)->GetFieldID(env, LongDateRecFc.clazz, "pm", "S");
	LongDateRecFc.res1 = (*env)->GetFieldID(env, LongDateRecFc.clazz, "res1", "S");
	LongDateRecFc.res2 = (*env)->GetFieldID(env, LongDateRecFc.clazz, "res2", "S");
	LongDateRecFc.res3 = (*env)->GetFieldID(env, LongDateRecFc.clazz, "res3", "S");
	LongDateRecFc.cached = 1;
}

LongDateRec *getLongDateRecFields(JNIEnv *env, jobject lpObject, LongDateRec *lpStruct)
{
	if (!LongDateRecFc.cached) cacheLongDateRecFields(env, lpObject);
	lpStruct->ld.era = (*env)->GetShortField(env, lpObject, LongDateRecFc.era);
	lpStruct->ld.year = (*env)->GetShortField(env, lpObject, LongDateRecFc.year);
	lpStruct->ld.month = (*env)->GetShortField(env, lpObject, LongDateRecFc.month);
	lpStruct->ld.day = (*env)->GetShortField(env, lpObject, LongDateRecFc.day);
	lpStruct->ld.hour = (*env)->GetShortField(env, lpObject, LongDateRecFc.hour);
	lpStruct->ld.minute = (*env)->GetShortField(env, lpObject, LongDateRecFc.minute);
	lpStruct->ld.second = (*env)->GetShortField(env, lpObject, LongDateRecFc.second);
	lpStruct->ld.dayOfWeek = (*env)->GetShortField(env, lpObject, LongDateRecFc.dayOfWeek);
	lpStruct->ld.dayOfYear = (*env)->GetShortField(env, lpObject, LongDateRecFc.dayOfYear);
	lpStruct->ld.weekOfYear = (*env)->GetShortField(env, lpObject, LongDateRecFc.weekOfYear);
	lpStruct->ld.pm = (*env)->GetShortField(env, lpObject, LongDateRecFc.pm);
	lpStruct->ld.res1 = (*env)->GetShortField(env, lpObject, LongDateRecFc.res1);
	lpStruct->ld.res2 = (*env)->GetShortField(env, lpObject, LongDateRecFc.res2);
	lpStruct->ld.res3 = (*env)->GetShortField(env, lpObject, LongDateRecFc.res3);
	return lpStruct;
}

void setLongDateRecFields(JNIEnv *env, jobject lpObject, LongDateRec *lpStruct)
{
	if (!LongDateRecFc.cached) cacheLongDateRecFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, LongDateRecFc.era, (jshort)lpStruct->ld.era);
	(*env)->SetShortField(env, lpObject, LongDateRecFc.year, (jshort)lpStruct->ld.year);
	(*env)->SetShortField(env, lpObject, LongDateRecFc.month, (jshort)lpStruct->ld.month);
	(*env)->SetShortField(env, lpObject, LongDateRecFc.day, (jshort)lpStruct->ld.day);
	(*env)->SetShortField(env, lpObject, LongDateRecFc.hour, (jshort)lpStruct->ld.hour);
	(*env)->SetShortField(env, lpObject, LongDateRecFc.minute, (jshort)lpStruct->ld.minute);
	(*env)->SetShortField(env, lpObject, LongDateRecFc.second, (jshort)lpStruct->ld.second);
	(*env)->SetShortField(env, lpObject, LongDateRecFc.dayOfWeek, (jshort)lpStruct->ld.dayOfWeek);
	(*env)->SetShortField(env, lpObject, LongDateRecFc.dayOfYear, (jshort)lpStruct->ld.dayOfYear);
	(*env)->SetShortField(env, lpObject, LongDateRecFc.weekOfYear, (jshort)lpStruct->ld.weekOfYear);
	(*env)->SetShortField(env, lpObject, LongDateRecFc.pm, (jshort)lpStruct->ld.pm);
	(*env)->SetShortField(env, lpObject, LongDateRecFc.res1, (jshort)lpStruct->ld.res1);
	(*env)->SetShortField(env, lpObject, LongDateRecFc.res2, (jshort)lpStruct->ld.res2);
	(*env)->SetShortField(env, lpObject, LongDateRecFc.res3, (jshort)lpStruct->ld.res3);
}
#endif

#ifndef NO_MenuTrackingData
typedef struct MenuTrackingData_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID menu, itemSelected, itemUnderMouse, top, left, bottom, right, virtualMenuTop, virtualMenuBottom;
} MenuTrackingData_FID_CACHE;

MenuTrackingData_FID_CACHE MenuTrackingDataFc;

void cacheMenuTrackingDataFields(JNIEnv *env, jobject lpObject)
{
	if (MenuTrackingDataFc.cached) return;
	MenuTrackingDataFc.clazz = (*env)->GetObjectClass(env, lpObject);
	MenuTrackingDataFc.menu = (*env)->GetFieldID(env, MenuTrackingDataFc.clazz, "menu", "I");
	MenuTrackingDataFc.itemSelected = (*env)->GetFieldID(env, MenuTrackingDataFc.clazz, "itemSelected", "S");
	MenuTrackingDataFc.itemUnderMouse = (*env)->GetFieldID(env, MenuTrackingDataFc.clazz, "itemUnderMouse", "S");
	MenuTrackingDataFc.top = (*env)->GetFieldID(env, MenuTrackingDataFc.clazz, "top", "S");
	MenuTrackingDataFc.left = (*env)->GetFieldID(env, MenuTrackingDataFc.clazz, "left", "S");
	MenuTrackingDataFc.bottom = (*env)->GetFieldID(env, MenuTrackingDataFc.clazz, "bottom", "S");
	MenuTrackingDataFc.right = (*env)->GetFieldID(env, MenuTrackingDataFc.clazz, "right", "S");
	MenuTrackingDataFc.virtualMenuTop = (*env)->GetFieldID(env, MenuTrackingDataFc.clazz, "virtualMenuTop", "I");
	MenuTrackingDataFc.virtualMenuBottom = (*env)->GetFieldID(env, MenuTrackingDataFc.clazz, "virtualMenuBottom", "I");
	MenuTrackingDataFc.cached = 1;
}

MenuTrackingData *getMenuTrackingDataFields(JNIEnv *env, jobject lpObject, MenuTrackingData *lpStruct)
{
	if (!MenuTrackingDataFc.cached) cacheMenuTrackingDataFields(env, lpObject);
	lpStruct->menu = (MenuRef)(*env)->GetIntField(env, lpObject, MenuTrackingDataFc.menu);
	lpStruct->itemSelected = (*env)->GetShortField(env, lpObject, MenuTrackingDataFc.itemSelected);
	lpStruct->itemUnderMouse = (*env)->GetShortField(env, lpObject, MenuTrackingDataFc.itemUnderMouse);
	lpStruct->itemRect.top = (*env)->GetShortField(env, lpObject, MenuTrackingDataFc.top);
	lpStruct->itemRect.left = (*env)->GetShortField(env, lpObject, MenuTrackingDataFc.left);
	lpStruct->itemRect.bottom = (*env)->GetShortField(env, lpObject, MenuTrackingDataFc.bottom);
	lpStruct->itemRect.right = (*env)->GetShortField(env, lpObject, MenuTrackingDataFc.right);
	lpStruct->virtualMenuTop = (*env)->GetIntField(env, lpObject, MenuTrackingDataFc.virtualMenuTop);
	lpStruct->virtualMenuBottom = (*env)->GetIntField(env, lpObject, MenuTrackingDataFc.virtualMenuBottom);
	return lpStruct;
}

void setMenuTrackingDataFields(JNIEnv *env, jobject lpObject, MenuTrackingData *lpStruct)
{
	if (!MenuTrackingDataFc.cached) cacheMenuTrackingDataFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, MenuTrackingDataFc.menu, (jint)lpStruct->menu);
	(*env)->SetShortField(env, lpObject, MenuTrackingDataFc.itemSelected, (jshort)lpStruct->itemSelected);
	(*env)->SetShortField(env, lpObject, MenuTrackingDataFc.itemUnderMouse, (jshort)lpStruct->itemUnderMouse);
	(*env)->SetShortField(env, lpObject, MenuTrackingDataFc.top, (jshort)lpStruct->itemRect.top);
	(*env)->SetShortField(env, lpObject, MenuTrackingDataFc.left, (jshort)lpStruct->itemRect.left);
	(*env)->SetShortField(env, lpObject, MenuTrackingDataFc.bottom, (jshort)lpStruct->itemRect.bottom);
	(*env)->SetShortField(env, lpObject, MenuTrackingDataFc.right, (jshort)lpStruct->itemRect.right);
	(*env)->SetIntField(env, lpObject, MenuTrackingDataFc.virtualMenuTop, (jint)lpStruct->virtualMenuTop);
	(*env)->SetIntField(env, lpObject, MenuTrackingDataFc.virtualMenuBottom, (jint)lpStruct->virtualMenuBottom);
}
#endif

#ifndef NO_NavCBRec
typedef struct NavCBRec_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID version, context, window, customRect, previewRect, eventData, userAction, reserved;
} NavCBRec_FID_CACHE;

NavCBRec_FID_CACHE NavCBRecFc;

void cacheNavCBRecFields(JNIEnv *env, jobject lpObject)
{
	if (NavCBRecFc.cached) return;
	NavCBRecFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NavCBRecFc.version = (*env)->GetFieldID(env, NavCBRecFc.clazz, "version", "S");
	NavCBRecFc.context = (*env)->GetFieldID(env, NavCBRecFc.clazz, "context", "I");
	NavCBRecFc.window = (*env)->GetFieldID(env, NavCBRecFc.clazz, "window", "I");
	NavCBRecFc.customRect = (*env)->GetFieldID(env, NavCBRecFc.clazz, "customRect", "Lorg/eclipse/swt/internal/carbon/Rect;");
	NavCBRecFc.previewRect = (*env)->GetFieldID(env, NavCBRecFc.clazz, "previewRect", "Lorg/eclipse/swt/internal/carbon/Rect;");
	NavCBRecFc.eventData = (*env)->GetFieldID(env, NavCBRecFc.clazz, "eventData", "Lorg/eclipse/swt/internal/carbon/NavEventData;");
	NavCBRecFc.userAction = (*env)->GetFieldID(env, NavCBRecFc.clazz, "userAction", "I");
	NavCBRecFc.reserved = (*env)->GetFieldID(env, NavCBRecFc.clazz, "reserved", "[B");
	NavCBRecFc.cached = 1;
}

NavCBRec *getNavCBRecFields(JNIEnv *env, jobject lpObject, NavCBRec *lpStruct)
{
	if (!NavCBRecFc.cached) cacheNavCBRecFields(env, lpObject);
	lpStruct->version = (*env)->GetShortField(env, lpObject, NavCBRecFc.version);
	lpStruct->context = (NavDialogRef)(*env)->GetIntField(env, lpObject, NavCBRecFc.context);
	lpStruct->window = (WindowRef)(*env)->GetIntField(env, lpObject, NavCBRecFc.window);
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NavCBRecFc.customRect);
	if (lpObject1 != NULL) getRectFields(env, lpObject1, &lpStruct->customRect);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NavCBRecFc.previewRect);
	if (lpObject1 != NULL) getRectFields(env, lpObject1, &lpStruct->previewRect);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NavCBRecFc.eventData);
	if (lpObject1 != NULL) getNavEventDataFields(env, lpObject1, &lpStruct->eventData);
	}
	lpStruct->userAction = (NavUserAction)(*env)->GetIntField(env, lpObject, NavCBRecFc.userAction);
	{
	jbyteArray lpObject1 = (jbyteArray)(*env)->GetObjectField(env, lpObject, NavCBRecFc.reserved);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->reserved), (jbyte *)lpStruct->reserved);
	}
	return lpStruct;
}

void setNavCBRecFields(JNIEnv *env, jobject lpObject, NavCBRec *lpStruct)
{
	if (!NavCBRecFc.cached) cacheNavCBRecFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, NavCBRecFc.version, (jshort)lpStruct->version);
	(*env)->SetIntField(env, lpObject, NavCBRecFc.context, (jint)lpStruct->context);
	(*env)->SetIntField(env, lpObject, NavCBRecFc.window, (jint)lpStruct->window);
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NavCBRecFc.customRect);
	if (lpObject1 != NULL) setRectFields(env, lpObject1, &lpStruct->customRect);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NavCBRecFc.previewRect);
	if (lpObject1 != NULL) setRectFields(env, lpObject1, &lpStruct->previewRect);
	}
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NavCBRecFc.eventData);
	if (lpObject1 != NULL) setNavEventDataFields(env, lpObject1, &lpStruct->eventData);
	}
	(*env)->SetIntField(env, lpObject, NavCBRecFc.userAction, (jint)lpStruct->userAction);
	{
	jbyteArray lpObject1 = (jbyteArray)(*env)->GetObjectField(env, lpObject, NavCBRecFc.reserved);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->reserved), (jbyte *)lpStruct->reserved);
	}
}
#endif

#ifndef NO_NavDialogCreationOptions
typedef struct NavDialogCreationOptions_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID version, optionFlags, location_h, location_v, clientName, windowTitle, actionButtonLabel, cancelButtonLabel, saveFileName, message, preferenceKey, popupExtension, modality, parentWindow;
} NavDialogCreationOptions_FID_CACHE;

NavDialogCreationOptions_FID_CACHE NavDialogCreationOptionsFc;

void cacheNavDialogCreationOptionsFields(JNIEnv *env, jobject lpObject)
{
	if (NavDialogCreationOptionsFc.cached) return;
	NavDialogCreationOptionsFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NavDialogCreationOptionsFc.version = (*env)->GetFieldID(env, NavDialogCreationOptionsFc.clazz, "version", "S");
	NavDialogCreationOptionsFc.optionFlags = (*env)->GetFieldID(env, NavDialogCreationOptionsFc.clazz, "optionFlags", "I");
	NavDialogCreationOptionsFc.location_h = (*env)->GetFieldID(env, NavDialogCreationOptionsFc.clazz, "location_h", "S");
	NavDialogCreationOptionsFc.location_v = (*env)->GetFieldID(env, NavDialogCreationOptionsFc.clazz, "location_v", "S");
	NavDialogCreationOptionsFc.clientName = (*env)->GetFieldID(env, NavDialogCreationOptionsFc.clazz, "clientName", "I");
	NavDialogCreationOptionsFc.windowTitle = (*env)->GetFieldID(env, NavDialogCreationOptionsFc.clazz, "windowTitle", "I");
	NavDialogCreationOptionsFc.actionButtonLabel = (*env)->GetFieldID(env, NavDialogCreationOptionsFc.clazz, "actionButtonLabel", "I");
	NavDialogCreationOptionsFc.cancelButtonLabel = (*env)->GetFieldID(env, NavDialogCreationOptionsFc.clazz, "cancelButtonLabel", "I");
	NavDialogCreationOptionsFc.saveFileName = (*env)->GetFieldID(env, NavDialogCreationOptionsFc.clazz, "saveFileName", "I");
	NavDialogCreationOptionsFc.message = (*env)->GetFieldID(env, NavDialogCreationOptionsFc.clazz, "message", "I");
	NavDialogCreationOptionsFc.preferenceKey = (*env)->GetFieldID(env, NavDialogCreationOptionsFc.clazz, "preferenceKey", "I");
	NavDialogCreationOptionsFc.popupExtension = (*env)->GetFieldID(env, NavDialogCreationOptionsFc.clazz, "popupExtension", "I");
	NavDialogCreationOptionsFc.modality = (*env)->GetFieldID(env, NavDialogCreationOptionsFc.clazz, "modality", "I");
	NavDialogCreationOptionsFc.parentWindow = (*env)->GetFieldID(env, NavDialogCreationOptionsFc.clazz, "parentWindow", "I");
	NavDialogCreationOptionsFc.cached = 1;
}

NavDialogCreationOptions *getNavDialogCreationOptionsFields(JNIEnv *env, jobject lpObject, NavDialogCreationOptions *lpStruct)
{
	if (!NavDialogCreationOptionsFc.cached) cacheNavDialogCreationOptionsFields(env, lpObject);
	lpStruct->version = (*env)->GetShortField(env, lpObject, NavDialogCreationOptionsFc.version);
	lpStruct->optionFlags = (NavDialogOptionFlags)(*env)->GetIntField(env, lpObject, NavDialogCreationOptionsFc.optionFlags);
	lpStruct->location.h = (*env)->GetShortField(env, lpObject, NavDialogCreationOptionsFc.location_h);
	lpStruct->location.v = (*env)->GetShortField(env, lpObject, NavDialogCreationOptionsFc.location_v);
	lpStruct->clientName = (CFStringRef)(*env)->GetIntField(env, lpObject, NavDialogCreationOptionsFc.clientName);
	lpStruct->windowTitle = (CFStringRef)(*env)->GetIntField(env, lpObject, NavDialogCreationOptionsFc.windowTitle);
	lpStruct->actionButtonLabel = (CFStringRef)(*env)->GetIntField(env, lpObject, NavDialogCreationOptionsFc.actionButtonLabel);
	lpStruct->cancelButtonLabel = (CFStringRef)(*env)->GetIntField(env, lpObject, NavDialogCreationOptionsFc.cancelButtonLabel);
	lpStruct->saveFileName = (CFStringRef)(*env)->GetIntField(env, lpObject, NavDialogCreationOptionsFc.saveFileName);
	lpStruct->message = (CFStringRef)(*env)->GetIntField(env, lpObject, NavDialogCreationOptionsFc.message);
	lpStruct->preferenceKey = (*env)->GetIntField(env, lpObject, NavDialogCreationOptionsFc.preferenceKey);
	lpStruct->popupExtension = (CFArrayRef)(*env)->GetIntField(env, lpObject, NavDialogCreationOptionsFc.popupExtension);
	lpStruct->modality = (WindowModality)(*env)->GetIntField(env, lpObject, NavDialogCreationOptionsFc.modality);
	lpStruct->parentWindow = (WindowRef)(*env)->GetIntField(env, lpObject, NavDialogCreationOptionsFc.parentWindow);
	return lpStruct;
}

void setNavDialogCreationOptionsFields(JNIEnv *env, jobject lpObject, NavDialogCreationOptions *lpStruct)
{
	if (!NavDialogCreationOptionsFc.cached) cacheNavDialogCreationOptionsFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, NavDialogCreationOptionsFc.version, (jshort)lpStruct->version);
	(*env)->SetIntField(env, lpObject, NavDialogCreationOptionsFc.optionFlags, (jint)lpStruct->optionFlags);
	(*env)->SetShortField(env, lpObject, NavDialogCreationOptionsFc.location_h, (jshort)lpStruct->location.h);
	(*env)->SetShortField(env, lpObject, NavDialogCreationOptionsFc.location_v, (jshort)lpStruct->location.v);
	(*env)->SetIntField(env, lpObject, NavDialogCreationOptionsFc.clientName, (jint)lpStruct->clientName);
	(*env)->SetIntField(env, lpObject, NavDialogCreationOptionsFc.windowTitle, (jint)lpStruct->windowTitle);
	(*env)->SetIntField(env, lpObject, NavDialogCreationOptionsFc.actionButtonLabel, (jint)lpStruct->actionButtonLabel);
	(*env)->SetIntField(env, lpObject, NavDialogCreationOptionsFc.cancelButtonLabel, (jint)lpStruct->cancelButtonLabel);
	(*env)->SetIntField(env, lpObject, NavDialogCreationOptionsFc.saveFileName, (jint)lpStruct->saveFileName);
	(*env)->SetIntField(env, lpObject, NavDialogCreationOptionsFc.message, (jint)lpStruct->message);
	(*env)->SetIntField(env, lpObject, NavDialogCreationOptionsFc.preferenceKey, (jint)lpStruct->preferenceKey);
	(*env)->SetIntField(env, lpObject, NavDialogCreationOptionsFc.popupExtension, (jint)lpStruct->popupExtension);
	(*env)->SetIntField(env, lpObject, NavDialogCreationOptionsFc.modality, (jint)lpStruct->modality);
	(*env)->SetIntField(env, lpObject, NavDialogCreationOptionsFc.parentWindow, (jint)lpStruct->parentWindow);
}
#endif

#ifndef NO_NavEventData
typedef struct NavEventData_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID eventDataParms, itemHit;
} NavEventData_FID_CACHE;

NavEventData_FID_CACHE NavEventDataFc;

void cacheNavEventDataFields(JNIEnv *env, jobject lpObject)
{
	if (NavEventDataFc.cached) return;
	NavEventDataFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NavEventDataFc.eventDataParms = (*env)->GetFieldID(env, NavEventDataFc.clazz, "eventDataParms", "Lorg/eclipse/swt/internal/carbon/NavEventDataInfo;");
	NavEventDataFc.itemHit = (*env)->GetFieldID(env, NavEventDataFc.clazz, "itemHit", "S");
	NavEventDataFc.cached = 1;
}

NavEventData *getNavEventDataFields(JNIEnv *env, jobject lpObject, NavEventData *lpStruct)
{
	if (!NavEventDataFc.cached) cacheNavEventDataFields(env, lpObject);
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NavEventDataFc.eventDataParms);
	if (lpObject1 != NULL) getNavEventDataInfoFields(env, lpObject1, &lpStruct->eventDataParms);
	}
	lpStruct->itemHit = (*env)->GetShortField(env, lpObject, NavEventDataFc.itemHit);
	return lpStruct;
}

void setNavEventDataFields(JNIEnv *env, jobject lpObject, NavEventData *lpStruct)
{
	if (!NavEventDataFc.cached) cacheNavEventDataFields(env, lpObject);
	{
	jobject lpObject1 = (*env)->GetObjectField(env, lpObject, NavEventDataFc.eventDataParms);
	if (lpObject1 != NULL) setNavEventDataInfoFields(env, lpObject1, &lpStruct->eventDataParms);
	}
	(*env)->SetShortField(env, lpObject, NavEventDataFc.itemHit, (jshort)lpStruct->itemHit);
}
#endif

#ifndef NO_NavEventDataInfo
typedef struct NavEventDataInfo_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID event, param;
} NavEventDataInfo_FID_CACHE;

NavEventDataInfo_FID_CACHE NavEventDataInfoFc;

void cacheNavEventDataInfoFields(JNIEnv *env, jobject lpObject)
{
	if (NavEventDataInfoFc.cached) return;
	NavEventDataInfoFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NavEventDataInfoFc.event = (*env)->GetFieldID(env, NavEventDataInfoFc.clazz, "event", "I");
	NavEventDataInfoFc.param = (*env)->GetFieldID(env, NavEventDataInfoFc.clazz, "param", "I");
	NavEventDataInfoFc.cached = 1;
}

NavEventDataInfo *getNavEventDataInfoFields(JNIEnv *env, jobject lpObject, NavEventDataInfo *lpStruct)
{
	if (!NavEventDataInfoFc.cached) cacheNavEventDataInfoFields(env, lpObject);
	lpStruct->event = (EventRecord *)(*env)->GetIntField(env, lpObject, NavEventDataInfoFc.event);
	lpStruct->param = (void *)(*env)->GetIntField(env, lpObject, NavEventDataInfoFc.param);
	return lpStruct;
}

void setNavEventDataInfoFields(JNIEnv *env, jobject lpObject, NavEventDataInfo *lpStruct)
{
	if (!NavEventDataInfoFc.cached) cacheNavEventDataInfoFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, NavEventDataInfoFc.event, (jint)lpStruct->event);
	(*env)->SetIntField(env, lpObject, NavEventDataInfoFc.param, (jint)lpStruct->param);
}
#endif

#ifndef NO_NavFileOrFolderInfo
typedef struct NavFileOrFolderInfo_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID version, isFolder, visible, creationDate, modificationDate;
} NavFileOrFolderInfo_FID_CACHE;

NavFileOrFolderInfo_FID_CACHE NavFileOrFolderInfoFc;

void cacheNavFileOrFolderInfoFields(JNIEnv *env, jobject lpObject)
{
	if (NavFileOrFolderInfoFc.cached) return;
	NavFileOrFolderInfoFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NavFileOrFolderInfoFc.version = (*env)->GetFieldID(env, NavFileOrFolderInfoFc.clazz, "version", "S");
	NavFileOrFolderInfoFc.isFolder = (*env)->GetFieldID(env, NavFileOrFolderInfoFc.clazz, "isFolder", "Z");
	NavFileOrFolderInfoFc.visible = (*env)->GetFieldID(env, NavFileOrFolderInfoFc.clazz, "visible", "Z");
	NavFileOrFolderInfoFc.creationDate = (*env)->GetFieldID(env, NavFileOrFolderInfoFc.clazz, "creationDate", "I");
	NavFileOrFolderInfoFc.modificationDate = (*env)->GetFieldID(env, NavFileOrFolderInfoFc.clazz, "modificationDate", "I");
	NavFileOrFolderInfoFc.cached = 1;
}

NavFileOrFolderInfo *getNavFileOrFolderInfoFields(JNIEnv *env, jobject lpObject, NavFileOrFolderInfo *lpStruct)
{
	if (!NavFileOrFolderInfoFc.cached) cacheNavFileOrFolderInfoFields(env, lpObject);
	lpStruct->version = (*env)->GetShortField(env, lpObject, NavFileOrFolderInfoFc.version);
	lpStruct->isFolder = (*env)->GetBooleanField(env, lpObject, NavFileOrFolderInfoFc.isFolder);
	lpStruct->visible = (*env)->GetBooleanField(env, lpObject, NavFileOrFolderInfoFc.visible);
	lpStruct->creationDate = (*env)->GetIntField(env, lpObject, NavFileOrFolderInfoFc.creationDate);
	lpStruct->modificationDate = (*env)->GetIntField(env, lpObject, NavFileOrFolderInfoFc.modificationDate);
	return lpStruct;
}

void setNavFileOrFolderInfoFields(JNIEnv *env, jobject lpObject, NavFileOrFolderInfo *lpStruct)
{
	if (!NavFileOrFolderInfoFc.cached) cacheNavFileOrFolderInfoFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, NavFileOrFolderInfoFc.version, (jshort)lpStruct->version);
	(*env)->SetBooleanField(env, lpObject, NavFileOrFolderInfoFc.isFolder, (jboolean)lpStruct->isFolder);
	(*env)->SetBooleanField(env, lpObject, NavFileOrFolderInfoFc.visible, (jboolean)lpStruct->visible);
	(*env)->SetIntField(env, lpObject, NavFileOrFolderInfoFc.creationDate, (jint)lpStruct->creationDate);
	(*env)->SetIntField(env, lpObject, NavFileOrFolderInfoFc.modificationDate, (jint)lpStruct->modificationDate);
}
#endif

#ifndef NO_NavMenuItemSpec
typedef struct NavMenuItemSpec_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID version, menuCreator, menuType, menuItemName, reserved;
} NavMenuItemSpec_FID_CACHE;

NavMenuItemSpec_FID_CACHE NavMenuItemSpecFc;

void cacheNavMenuItemSpecFields(JNIEnv *env, jobject lpObject)
{
	if (NavMenuItemSpecFc.cached) return;
	NavMenuItemSpecFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NavMenuItemSpecFc.version = (*env)->GetFieldID(env, NavMenuItemSpecFc.clazz, "version", "S");
	NavMenuItemSpecFc.menuCreator = (*env)->GetFieldID(env, NavMenuItemSpecFc.clazz, "menuCreator", "I");
	NavMenuItemSpecFc.menuType = (*env)->GetFieldID(env, NavMenuItemSpecFc.clazz, "menuType", "I");
	NavMenuItemSpecFc.menuItemName = (*env)->GetFieldID(env, NavMenuItemSpecFc.clazz, "menuItemName", "[B");
	NavMenuItemSpecFc.reserved = (*env)->GetFieldID(env, NavMenuItemSpecFc.clazz, "reserved", "[B");
	NavMenuItemSpecFc.cached = 1;
}

NavMenuItemSpec *getNavMenuItemSpecFields(JNIEnv *env, jobject lpObject, NavMenuItemSpec *lpStruct)
{
	if (!NavMenuItemSpecFc.cached) cacheNavMenuItemSpecFields(env, lpObject);
	lpStruct->version = (*env)->GetShortField(env, lpObject, NavMenuItemSpecFc.version);
	lpStruct->menuCreator = (*env)->GetIntField(env, lpObject, NavMenuItemSpecFc.menuCreator);
	lpStruct->menuType = (*env)->GetIntField(env, lpObject, NavMenuItemSpecFc.menuType);
	{
	jbyteArray lpObject1 = (jbyteArray)(*env)->GetObjectField(env, lpObject, NavMenuItemSpecFc.menuItemName);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->menuItemName), (jbyte *)lpStruct->menuItemName);
	}
	{
	jbyteArray lpObject1 = (jbyteArray)(*env)->GetObjectField(env, lpObject, NavMenuItemSpecFc.reserved);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->reserved), (jbyte *)lpStruct->reserved);
	}
	return lpStruct;
}

void setNavMenuItemSpecFields(JNIEnv *env, jobject lpObject, NavMenuItemSpec *lpStruct)
{
	if (!NavMenuItemSpecFc.cached) cacheNavMenuItemSpecFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, NavMenuItemSpecFc.version, (jshort)lpStruct->version);
	(*env)->SetIntField(env, lpObject, NavMenuItemSpecFc.menuCreator, (jint)lpStruct->menuCreator);
	(*env)->SetIntField(env, lpObject, NavMenuItemSpecFc.menuType, (jint)lpStruct->menuType);
	{
	jbyteArray lpObject1 = (jbyteArray)(*env)->GetObjectField(env, lpObject, NavMenuItemSpecFc.menuItemName);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->menuItemName), (jbyte *)lpStruct->menuItemName);
	}
	{
	jbyteArray lpObject1 = (jbyteArray)(*env)->GetObjectField(env, lpObject, NavMenuItemSpecFc.reserved);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->reserved), (jbyte *)lpStruct->reserved);
	}
}
#endif

#ifndef NO_NavReplyRecord
typedef struct NavReplyRecord_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID version, validRecord, replacing, isStationery, translationNeeded, selection_descriptorType, selection_dataHandle, keyScript, fileTranslation, reserved1, saveFileName, saveFileExtensionHidden, reserved2, reserved;
} NavReplyRecord_FID_CACHE;

NavReplyRecord_FID_CACHE NavReplyRecordFc;

void cacheNavReplyRecordFields(JNIEnv *env, jobject lpObject)
{
	if (NavReplyRecordFc.cached) return;
	NavReplyRecordFc.clazz = (*env)->GetObjectClass(env, lpObject);
	NavReplyRecordFc.version = (*env)->GetFieldID(env, NavReplyRecordFc.clazz, "version", "S");
	NavReplyRecordFc.validRecord = (*env)->GetFieldID(env, NavReplyRecordFc.clazz, "validRecord", "Z");
	NavReplyRecordFc.replacing = (*env)->GetFieldID(env, NavReplyRecordFc.clazz, "replacing", "Z");
	NavReplyRecordFc.isStationery = (*env)->GetFieldID(env, NavReplyRecordFc.clazz, "isStationery", "Z");
	NavReplyRecordFc.translationNeeded = (*env)->GetFieldID(env, NavReplyRecordFc.clazz, "translationNeeded", "Z");
	NavReplyRecordFc.selection_descriptorType = (*env)->GetFieldID(env, NavReplyRecordFc.clazz, "selection_descriptorType", "I");
	NavReplyRecordFc.selection_dataHandle = (*env)->GetFieldID(env, NavReplyRecordFc.clazz, "selection_dataHandle", "I");
	NavReplyRecordFc.keyScript = (*env)->GetFieldID(env, NavReplyRecordFc.clazz, "keyScript", "S");
	NavReplyRecordFc.fileTranslation = (*env)->GetFieldID(env, NavReplyRecordFc.clazz, "fileTranslation", "I");
	NavReplyRecordFc.reserved1 = (*env)->GetFieldID(env, NavReplyRecordFc.clazz, "reserved1", "I");
	NavReplyRecordFc.saveFileName = (*env)->GetFieldID(env, NavReplyRecordFc.clazz, "saveFileName", "I");
	NavReplyRecordFc.saveFileExtensionHidden = (*env)->GetFieldID(env, NavReplyRecordFc.clazz, "saveFileExtensionHidden", "Z");
	NavReplyRecordFc.reserved2 = (*env)->GetFieldID(env, NavReplyRecordFc.clazz, "reserved2", "B");
	NavReplyRecordFc.reserved = (*env)->GetFieldID(env, NavReplyRecordFc.clazz, "reserved", "[B");
	NavReplyRecordFc.cached = 1;
}

NavReplyRecord *getNavReplyRecordFields(JNIEnv *env, jobject lpObject, NavReplyRecord *lpStruct)
{
	if (!NavReplyRecordFc.cached) cacheNavReplyRecordFields(env, lpObject);
	lpStruct->version = (UInt16)(*env)->GetShortField(env, lpObject, NavReplyRecordFc.version);
	lpStruct->validRecord = (Boolean)(*env)->GetBooleanField(env, lpObject, NavReplyRecordFc.validRecord);
	lpStruct->replacing = (Boolean)(*env)->GetBooleanField(env, lpObject, NavReplyRecordFc.replacing);
	lpStruct->isStationery = (Boolean)(*env)->GetBooleanField(env, lpObject, NavReplyRecordFc.isStationery);
	lpStruct->translationNeeded = (Boolean)(*env)->GetBooleanField(env, lpObject, NavReplyRecordFc.translationNeeded);
	lpStruct->selection.descriptorType = (DescType)(*env)->GetIntField(env, lpObject, NavReplyRecordFc.selection_descriptorType);
	lpStruct->selection.dataHandle = (AEDataStorage)(*env)->GetIntField(env, lpObject, NavReplyRecordFc.selection_dataHandle);
	lpStruct->keyScript = (ScriptCode)(*env)->GetShortField(env, lpObject, NavReplyRecordFc.keyScript);
	lpStruct->fileTranslation = (FileTranslationSpecArrayHandle)(*env)->GetIntField(env, lpObject, NavReplyRecordFc.fileTranslation);
	lpStruct->reserved1 = (UInt32)(*env)->GetIntField(env, lpObject, NavReplyRecordFc.reserved1);
	lpStruct->saveFileName = (CFStringRef)(*env)->GetIntField(env, lpObject, NavReplyRecordFc.saveFileName);
	lpStruct->saveFileExtensionHidden = (Boolean)(*env)->GetBooleanField(env, lpObject, NavReplyRecordFc.saveFileExtensionHidden);
	lpStruct->reserved2 = (UInt8)(*env)->GetByteField(env, lpObject, NavReplyRecordFc.reserved2);
	{
	jbyteArray lpObject1 = (jbyteArray)(*env)->GetObjectField(env, lpObject, NavReplyRecordFc.reserved);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->reserved), (jbyte *)lpStruct->reserved);
	}
	return lpStruct;
}

void setNavReplyRecordFields(JNIEnv *env, jobject lpObject, NavReplyRecord *lpStruct)
{
	if (!NavReplyRecordFc.cached) cacheNavReplyRecordFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, NavReplyRecordFc.version, (jshort)lpStruct->version);
	(*env)->SetBooleanField(env, lpObject, NavReplyRecordFc.validRecord, (jboolean)lpStruct->validRecord);
	(*env)->SetBooleanField(env, lpObject, NavReplyRecordFc.replacing, (jboolean)lpStruct->replacing);
	(*env)->SetBooleanField(env, lpObject, NavReplyRecordFc.isStationery, (jboolean)lpStruct->isStationery);
	(*env)->SetBooleanField(env, lpObject, NavReplyRecordFc.translationNeeded, (jboolean)lpStruct->translationNeeded);
	(*env)->SetIntField(env, lpObject, NavReplyRecordFc.selection_descriptorType, (jint)lpStruct->selection.descriptorType);
	(*env)->SetIntField(env, lpObject, NavReplyRecordFc.selection_dataHandle, (jint)lpStruct->selection.dataHandle);
	(*env)->SetShortField(env, lpObject, NavReplyRecordFc.keyScript, (jshort)lpStruct->keyScript);
	(*env)->SetIntField(env, lpObject, NavReplyRecordFc.fileTranslation, (jint)lpStruct->fileTranslation);
	(*env)->SetIntField(env, lpObject, NavReplyRecordFc.reserved1, (jint)lpStruct->reserved1);
	(*env)->SetIntField(env, lpObject, NavReplyRecordFc.saveFileName, (jint)lpStruct->saveFileName);
	(*env)->SetBooleanField(env, lpObject, NavReplyRecordFc.saveFileExtensionHidden, (jboolean)lpStruct->saveFileExtensionHidden);
	(*env)->SetByteField(env, lpObject, NavReplyRecordFc.reserved2, (jbyte)lpStruct->reserved2);
	{
	jbyteArray lpObject1 = (jbyteArray)(*env)->GetObjectField(env, lpObject, NavReplyRecordFc.reserved);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->reserved), (jbyte *)lpStruct->reserved);
	}
}
#endif

#ifndef NO_PMRect
typedef struct PMRect_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID top, left, bottom, right;
} PMRect_FID_CACHE;

PMRect_FID_CACHE PMRectFc;

void cachePMRectFields(JNIEnv *env, jobject lpObject)
{
	if (PMRectFc.cached) return;
	PMRectFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PMRectFc.top = (*env)->GetFieldID(env, PMRectFc.clazz, "top", "D");
	PMRectFc.left = (*env)->GetFieldID(env, PMRectFc.clazz, "left", "D");
	PMRectFc.bottom = (*env)->GetFieldID(env, PMRectFc.clazz, "bottom", "D");
	PMRectFc.right = (*env)->GetFieldID(env, PMRectFc.clazz, "right", "D");
	PMRectFc.cached = 1;
}

PMRect *getPMRectFields(JNIEnv *env, jobject lpObject, PMRect *lpStruct)
{
	if (!PMRectFc.cached) cachePMRectFields(env, lpObject);
	lpStruct->top = (double)(*env)->GetDoubleField(env, lpObject, PMRectFc.top);
	lpStruct->left = (double)(*env)->GetDoubleField(env, lpObject, PMRectFc.left);
	lpStruct->bottom = (double)(*env)->GetDoubleField(env, lpObject, PMRectFc.bottom);
	lpStruct->right = (double)(*env)->GetDoubleField(env, lpObject, PMRectFc.right);
	return lpStruct;
}

void setPMRectFields(JNIEnv *env, jobject lpObject, PMRect *lpStruct)
{
	if (!PMRectFc.cached) cachePMRectFields(env, lpObject);
	(*env)->SetDoubleField(env, lpObject, PMRectFc.top, (jdouble)lpStruct->top);
	(*env)->SetDoubleField(env, lpObject, PMRectFc.left, (jdouble)lpStruct->left);
	(*env)->SetDoubleField(env, lpObject, PMRectFc.bottom, (jdouble)lpStruct->bottom);
	(*env)->SetDoubleField(env, lpObject, PMRectFc.right, (jdouble)lpStruct->right);
}
#endif

#ifndef NO_PMResolution
typedef struct PMResolution_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID hRes, vRes;
} PMResolution_FID_CACHE;

PMResolution_FID_CACHE PMResolutionFc;

void cachePMResolutionFields(JNIEnv *env, jobject lpObject)
{
	if (PMResolutionFc.cached) return;
	PMResolutionFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PMResolutionFc.hRes = (*env)->GetFieldID(env, PMResolutionFc.clazz, "hRes", "D");
	PMResolutionFc.vRes = (*env)->GetFieldID(env, PMResolutionFc.clazz, "vRes", "D");
	PMResolutionFc.cached = 1;
}

PMResolution *getPMResolutionFields(JNIEnv *env, jobject lpObject, PMResolution *lpStruct)
{
	if (!PMResolutionFc.cached) cachePMResolutionFields(env, lpObject);
	lpStruct->hRes = (*env)->GetDoubleField(env, lpObject, PMResolutionFc.hRes);
	lpStruct->vRes = (*env)->GetDoubleField(env, lpObject, PMResolutionFc.vRes);
	return lpStruct;
}

void setPMResolutionFields(JNIEnv *env, jobject lpObject, PMResolution *lpStruct)
{
	if (!PMResolutionFc.cached) cachePMResolutionFields(env, lpObject);
	(*env)->SetDoubleField(env, lpObject, PMResolutionFc.hRes, (jdouble)lpStruct->hRes);
	(*env)->SetDoubleField(env, lpObject, PMResolutionFc.vRes, (jdouble)lpStruct->vRes);
}
#endif

#ifndef NO_PixMap
typedef struct PixMap_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID pmVersion, packType, packSize, hRes, vRes, pixelType, pixelSize, cmpCount, cmpSize, pixelFormat, pmTable, pmExt;
} PixMap_FID_CACHE;

PixMap_FID_CACHE PixMapFc;

void cachePixMapFields(JNIEnv *env, jobject lpObject)
{
	if (PixMapFc.cached) return;
	cacheBitMapFields(env, lpObject);
	PixMapFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PixMapFc.pmVersion = (*env)->GetFieldID(env, PixMapFc.clazz, "pmVersion", "S");
	PixMapFc.packType = (*env)->GetFieldID(env, PixMapFc.clazz, "packType", "S");
	PixMapFc.packSize = (*env)->GetFieldID(env, PixMapFc.clazz, "packSize", "I");
	PixMapFc.hRes = (*env)->GetFieldID(env, PixMapFc.clazz, "hRes", "I");
	PixMapFc.vRes = (*env)->GetFieldID(env, PixMapFc.clazz, "vRes", "I");
	PixMapFc.pixelType = (*env)->GetFieldID(env, PixMapFc.clazz, "pixelType", "S");
	PixMapFc.pixelSize = (*env)->GetFieldID(env, PixMapFc.clazz, "pixelSize", "S");
	PixMapFc.cmpCount = (*env)->GetFieldID(env, PixMapFc.clazz, "cmpCount", "S");
	PixMapFc.cmpSize = (*env)->GetFieldID(env, PixMapFc.clazz, "cmpSize", "S");
	PixMapFc.pixelFormat = (*env)->GetFieldID(env, PixMapFc.clazz, "pixelFormat", "I");
	PixMapFc.pmTable = (*env)->GetFieldID(env, PixMapFc.clazz, "pmTable", "I");
	PixMapFc.pmExt = (*env)->GetFieldID(env, PixMapFc.clazz, "pmExt", "I");
	PixMapFc.cached = 1;
}

PixMap *getPixMapFields(JNIEnv *env, jobject lpObject, PixMap *lpStruct)
{
	if (!PixMapFc.cached) cachePixMapFields(env, lpObject);
	getBitMapFields(env, lpObject, (BitMap *)lpStruct);
	lpStruct->pmVersion = (*env)->GetShortField(env, lpObject, PixMapFc.pmVersion);
	lpStruct->packType = (*env)->GetShortField(env, lpObject, PixMapFc.packType);
	lpStruct->packSize = (*env)->GetIntField(env, lpObject, PixMapFc.packSize);
	lpStruct->hRes = (*env)->GetIntField(env, lpObject, PixMapFc.hRes);
	lpStruct->vRes = (*env)->GetIntField(env, lpObject, PixMapFc.vRes);
	lpStruct->pixelType = (*env)->GetShortField(env, lpObject, PixMapFc.pixelType);
	lpStruct->pixelSize = (*env)->GetShortField(env, lpObject, PixMapFc.pixelSize);
	lpStruct->cmpCount = (*env)->GetShortField(env, lpObject, PixMapFc.cmpCount);
	lpStruct->cmpSize = (*env)->GetShortField(env, lpObject, PixMapFc.cmpSize);
	lpStruct->pixelFormat = (*env)->GetIntField(env, lpObject, PixMapFc.pixelFormat);
	lpStruct->pmTable = (CTabHandle)(*env)->GetIntField(env, lpObject, PixMapFc.pmTable);
	lpStruct->pmExt = (void *)(*env)->GetIntField(env, lpObject, PixMapFc.pmExt);
	return lpStruct;
}

void setPixMapFields(JNIEnv *env, jobject lpObject, PixMap *lpStruct)
{
	if (!PixMapFc.cached) cachePixMapFields(env, lpObject);
	setBitMapFields(env, lpObject, (BitMap *)lpStruct);
	(*env)->SetShortField(env, lpObject, PixMapFc.pmVersion, (jshort)lpStruct->pmVersion);
	(*env)->SetShortField(env, lpObject, PixMapFc.packType, (jshort)lpStruct->packType);
	(*env)->SetIntField(env, lpObject, PixMapFc.packSize, (jint)lpStruct->packSize);
	(*env)->SetIntField(env, lpObject, PixMapFc.hRes, (jint)lpStruct->hRes);
	(*env)->SetIntField(env, lpObject, PixMapFc.vRes, (jint)lpStruct->vRes);
	(*env)->SetShortField(env, lpObject, PixMapFc.pixelType, (jshort)lpStruct->pixelType);
	(*env)->SetShortField(env, lpObject, PixMapFc.pixelSize, (jshort)lpStruct->pixelSize);
	(*env)->SetShortField(env, lpObject, PixMapFc.cmpCount, (jshort)lpStruct->cmpCount);
	(*env)->SetShortField(env, lpObject, PixMapFc.cmpSize, (jshort)lpStruct->cmpSize);
	(*env)->SetIntField(env, lpObject, PixMapFc.pixelFormat, (jint)lpStruct->pixelFormat);
	(*env)->SetIntField(env, lpObject, PixMapFc.pmTable, (jint)lpStruct->pmTable);
	(*env)->SetIntField(env, lpObject, PixMapFc.pmExt, (jint)lpStruct->pmExt);
}
#endif

#ifndef NO_Point
typedef struct Point_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID v, h;
} Point_FID_CACHE;

Point_FID_CACHE PointFc;

void cachePointFields(JNIEnv *env, jobject lpObject)
{
	if (PointFc.cached) return;
	PointFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PointFc.v = (*env)->GetFieldID(env, PointFc.clazz, "v", "S");
	PointFc.h = (*env)->GetFieldID(env, PointFc.clazz, "h", "S");
	PointFc.cached = 1;
}

Point *getPointFields(JNIEnv *env, jobject lpObject, Point *lpStruct)
{
	if (!PointFc.cached) cachePointFields(env, lpObject);
	lpStruct->v = (*env)->GetShortField(env, lpObject, PointFc.v);
	lpStruct->h = (*env)->GetShortField(env, lpObject, PointFc.h);
	return lpStruct;
}

void setPointFields(JNIEnv *env, jobject lpObject, Point *lpStruct)
{
	if (!PointFc.cached) cachePointFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, PointFc.v, (jshort)lpStruct->v);
	(*env)->SetShortField(env, lpObject, PointFc.h, (jshort)lpStruct->h);
}
#endif

#ifndef NO_ProgressTrackInfo
typedef struct ProgressTrackInfo_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID phase;
} ProgressTrackInfo_FID_CACHE;

ProgressTrackInfo_FID_CACHE ProgressTrackInfoFc;

void cacheProgressTrackInfoFields(JNIEnv *env, jobject lpObject)
{
	if (ProgressTrackInfoFc.cached) return;
	ProgressTrackInfoFc.clazz = (*env)->GetObjectClass(env, lpObject);
	ProgressTrackInfoFc.phase = (*env)->GetFieldID(env, ProgressTrackInfoFc.clazz, "phase", "B");
	ProgressTrackInfoFc.cached = 1;
}

ProgressTrackInfo *getProgressTrackInfoFields(JNIEnv *env, jobject lpObject, ProgressTrackInfo *lpStruct)
{
	if (!ProgressTrackInfoFc.cached) cacheProgressTrackInfoFields(env, lpObject);
	lpStruct->phase = (*env)->GetByteField(env, lpObject, ProgressTrackInfoFc.phase);
	return lpStruct;
}

void setProgressTrackInfoFields(JNIEnv *env, jobject lpObject, ProgressTrackInfo *lpStruct)
{
	if (!ProgressTrackInfoFc.cached) cacheProgressTrackInfoFields(env, lpObject);
	(*env)->SetByteField(env, lpObject, ProgressTrackInfoFc.phase, (jbyte)lpStruct->phase);
}
#endif

#ifndef NO_RGBColor
typedef struct RGBColor_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID red, green, blue;
} RGBColor_FID_CACHE;

RGBColor_FID_CACHE RGBColorFc;

void cacheRGBColorFields(JNIEnv *env, jobject lpObject)
{
	if (RGBColorFc.cached) return;
	RGBColorFc.clazz = (*env)->GetObjectClass(env, lpObject);
	RGBColorFc.red = (*env)->GetFieldID(env, RGBColorFc.clazz, "red", "S");
	RGBColorFc.green = (*env)->GetFieldID(env, RGBColorFc.clazz, "green", "S");
	RGBColorFc.blue = (*env)->GetFieldID(env, RGBColorFc.clazz, "blue", "S");
	RGBColorFc.cached = 1;
}

RGBColor *getRGBColorFields(JNIEnv *env, jobject lpObject, RGBColor *lpStruct)
{
	if (!RGBColorFc.cached) cacheRGBColorFields(env, lpObject);
	lpStruct->red = (*env)->GetShortField(env, lpObject, RGBColorFc.red);
	lpStruct->green = (*env)->GetShortField(env, lpObject, RGBColorFc.green);
	lpStruct->blue = (*env)->GetShortField(env, lpObject, RGBColorFc.blue);
	return lpStruct;
}

void setRGBColorFields(JNIEnv *env, jobject lpObject, RGBColor *lpStruct)
{
	if (!RGBColorFc.cached) cacheRGBColorFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, RGBColorFc.red, (jshort)lpStruct->red);
	(*env)->SetShortField(env, lpObject, RGBColorFc.green, (jshort)lpStruct->green);
	(*env)->SetShortField(env, lpObject, RGBColorFc.blue, (jshort)lpStruct->blue);
}
#endif

#ifndef NO_Rect
typedef struct Rect_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID top, left, bottom, right;
} Rect_FID_CACHE;

Rect_FID_CACHE RectFc;

void cacheRectFields(JNIEnv *env, jobject lpObject)
{
	if (RectFc.cached) return;
	RectFc.clazz = (*env)->GetObjectClass(env, lpObject);
	RectFc.top = (*env)->GetFieldID(env, RectFc.clazz, "top", "S");
	RectFc.left = (*env)->GetFieldID(env, RectFc.clazz, "left", "S");
	RectFc.bottom = (*env)->GetFieldID(env, RectFc.clazz, "bottom", "S");
	RectFc.right = (*env)->GetFieldID(env, RectFc.clazz, "right", "S");
	RectFc.cached = 1;
}

Rect *getRectFields(JNIEnv *env, jobject lpObject, Rect *lpStruct)
{
	if (!RectFc.cached) cacheRectFields(env, lpObject);
	lpStruct->top = (*env)->GetShortField(env, lpObject, RectFc.top);
	lpStruct->left = (*env)->GetShortField(env, lpObject, RectFc.left);
	lpStruct->bottom = (*env)->GetShortField(env, lpObject, RectFc.bottom);
	lpStruct->right = (*env)->GetShortField(env, lpObject, RectFc.right);
	return lpStruct;
}

void setRectFields(JNIEnv *env, jobject lpObject, Rect *lpStruct)
{
	if (!RectFc.cached) cacheRectFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, RectFc.top, (jshort)lpStruct->top);
	(*env)->SetShortField(env, lpObject, RectFc.left, (jshort)lpStruct->left);
	(*env)->SetShortField(env, lpObject, RectFc.bottom, (jshort)lpStruct->bottom);
	(*env)->SetShortField(env, lpObject, RectFc.right, (jshort)lpStruct->right);
}
#endif

#ifndef NO_ScrollBarTrackInfo
typedef struct ScrollBarTrackInfo_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID viewsize, pressState;
} ScrollBarTrackInfo_FID_CACHE;

ScrollBarTrackInfo_FID_CACHE ScrollBarTrackInfoFc;

void cacheScrollBarTrackInfoFields(JNIEnv *env, jobject lpObject)
{
	if (ScrollBarTrackInfoFc.cached) return;
	ScrollBarTrackInfoFc.clazz = (*env)->GetObjectClass(env, lpObject);
	ScrollBarTrackInfoFc.viewsize = (*env)->GetFieldID(env, ScrollBarTrackInfoFc.clazz, "viewsize", "I");
	ScrollBarTrackInfoFc.pressState = (*env)->GetFieldID(env, ScrollBarTrackInfoFc.clazz, "pressState", "B");
	ScrollBarTrackInfoFc.cached = 1;
}

ScrollBarTrackInfo *getScrollBarTrackInfoFields(JNIEnv *env, jobject lpObject, ScrollBarTrackInfo *lpStruct)
{
	if (!ScrollBarTrackInfoFc.cached) cacheScrollBarTrackInfoFields(env, lpObject);
	lpStruct->viewsize = (*env)->GetIntField(env, lpObject, ScrollBarTrackInfoFc.viewsize);
	lpStruct->pressState = (*env)->GetByteField(env, lpObject, ScrollBarTrackInfoFc.pressState);
	return lpStruct;
}

void setScrollBarTrackInfoFields(JNIEnv *env, jobject lpObject, ScrollBarTrackInfo *lpStruct)
{
	if (!ScrollBarTrackInfoFc.cached) cacheScrollBarTrackInfoFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, ScrollBarTrackInfoFc.viewsize, (jint)lpStruct->viewsize);
	(*env)->SetByteField(env, lpObject, ScrollBarTrackInfoFc.pressState, (jbyte)lpStruct->pressState);
}
#endif

#ifndef NO_SliderTrackInfo
typedef struct SliderTrackInfo_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID thumbDir, pressState;
} SliderTrackInfo_FID_CACHE;

SliderTrackInfo_FID_CACHE SliderTrackInfoFc;

void cacheSliderTrackInfoFields(JNIEnv *env, jobject lpObject)
{
	if (SliderTrackInfoFc.cached) return;
	SliderTrackInfoFc.clazz = (*env)->GetObjectClass(env, lpObject);
	SliderTrackInfoFc.thumbDir = (*env)->GetFieldID(env, SliderTrackInfoFc.clazz, "thumbDir", "B");
	SliderTrackInfoFc.pressState = (*env)->GetFieldID(env, SliderTrackInfoFc.clazz, "pressState", "B");
	SliderTrackInfoFc.cached = 1;
}

SliderTrackInfo *getSliderTrackInfoFields(JNIEnv *env, jobject lpObject, SliderTrackInfo *lpStruct)
{
	if (!SliderTrackInfoFc.cached) cacheSliderTrackInfoFields(env, lpObject);
	lpStruct->thumbDir = (*env)->GetByteField(env, lpObject, SliderTrackInfoFc.thumbDir);
	lpStruct->pressState = (*env)->GetByteField(env, lpObject, SliderTrackInfoFc.pressState);
	return lpStruct;
}

void setSliderTrackInfoFields(JNIEnv *env, jobject lpObject, SliderTrackInfo *lpStruct)
{
	if (!SliderTrackInfoFc.cached) cacheSliderTrackInfoFields(env, lpObject);
	(*env)->SetByteField(env, lpObject, SliderTrackInfoFc.thumbDir, (jbyte)lpStruct->thumbDir);
	(*env)->SetByteField(env, lpObject, SliderTrackInfoFc.pressState, (jbyte)lpStruct->pressState);
}
#endif

#ifndef NO_TXNBackground
typedef struct TXNBackground_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID bgType, bg_red, bg_green, bg_blue;
} TXNBackground_FID_CACHE;

TXNBackground_FID_CACHE TXNBackgroundFc;

void cacheTXNBackgroundFields(JNIEnv *env, jobject lpObject)
{
	if (TXNBackgroundFc.cached) return;
	TXNBackgroundFc.clazz = (*env)->GetObjectClass(env, lpObject);
	TXNBackgroundFc.bgType = (*env)->GetFieldID(env, TXNBackgroundFc.clazz, "bgType", "I");
	TXNBackgroundFc.bg_red = (*env)->GetFieldID(env, TXNBackgroundFc.clazz, "bg_red", "S");
	TXNBackgroundFc.bg_green = (*env)->GetFieldID(env, TXNBackgroundFc.clazz, "bg_green", "S");
	TXNBackgroundFc.bg_blue = (*env)->GetFieldID(env, TXNBackgroundFc.clazz, "bg_blue", "S");
	TXNBackgroundFc.cached = 1;
}

TXNBackground *getTXNBackgroundFields(JNIEnv *env, jobject lpObject, TXNBackground *lpStruct)
{
	if (!TXNBackgroundFc.cached) cacheTXNBackgroundFields(env, lpObject);
	lpStruct->bgType = (*env)->GetIntField(env, lpObject, TXNBackgroundFc.bgType);
	lpStruct->bg.color.red = (*env)->GetShortField(env, lpObject, TXNBackgroundFc.bg_red);
	lpStruct->bg.color.green = (*env)->GetShortField(env, lpObject, TXNBackgroundFc.bg_green);
	lpStruct->bg.color.blue = (*env)->GetShortField(env, lpObject, TXNBackgroundFc.bg_blue);
	return lpStruct;
}

void setTXNBackgroundFields(JNIEnv *env, jobject lpObject, TXNBackground *lpStruct)
{
	if (!TXNBackgroundFc.cached) cacheTXNBackgroundFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, TXNBackgroundFc.bgType, (jint)lpStruct->bgType);
	(*env)->SetShortField(env, lpObject, TXNBackgroundFc.bg_red, (jshort)lpStruct->bg.color.red);
	(*env)->SetShortField(env, lpObject, TXNBackgroundFc.bg_green, (jshort)lpStruct->bg.color.green);
	(*env)->SetShortField(env, lpObject, TXNBackgroundFc.bg_blue, (jshort)lpStruct->bg.color.blue);
}
#endif

#ifndef NO_TXNTab
typedef struct TXNTab_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID value, tabType, filler;
} TXNTab_FID_CACHE;

TXNTab_FID_CACHE TXNTabFc;

void cacheTXNTabFields(JNIEnv *env, jobject lpObject)
{
	if (TXNTabFc.cached) return;
	TXNTabFc.clazz = (*env)->GetObjectClass(env, lpObject);
	TXNTabFc.value = (*env)->GetFieldID(env, TXNTabFc.clazz, "value", "S");
	TXNTabFc.tabType = (*env)->GetFieldID(env, TXNTabFc.clazz, "tabType", "B");
	TXNTabFc.filler = (*env)->GetFieldID(env, TXNTabFc.clazz, "filler", "B");
	TXNTabFc.cached = 1;
}

TXNTab *getTXNTabFields(JNIEnv *env, jobject lpObject, TXNTab *lpStruct)
{
	if (!TXNTabFc.cached) cacheTXNTabFields(env, lpObject);
	lpStruct->value = (*env)->GetShortField(env, lpObject, TXNTabFc.value);
	lpStruct->tabType = (*env)->GetByteField(env, lpObject, TXNTabFc.tabType);
	lpStruct->filler = (*env)->GetByteField(env, lpObject, TXNTabFc.filler);
	return lpStruct;
}

void setTXNTabFields(JNIEnv *env, jobject lpObject, TXNTab *lpStruct)
{
	if (!TXNTabFc.cached) cacheTXNTabFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, TXNTabFc.value, (jshort)lpStruct->value);
	(*env)->SetByteField(env, lpObject, TXNTabFc.tabType, (jbyte)lpStruct->tabType);
	(*env)->SetByteField(env, lpObject, TXNTabFc.filler, (jbyte)lpStruct->filler);
}
#endif

#ifndef NO_TextRange
typedef struct TextRange_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID fStart, fEnd, fHiliteStyle;
} TextRange_FID_CACHE;

TextRange_FID_CACHE TextRangeFc;

void cacheTextRangeFields(JNIEnv *env, jobject lpObject)
{
	if (TextRangeFc.cached) return;
	TextRangeFc.clazz = (*env)->GetObjectClass(env, lpObject);
	TextRangeFc.fStart = (*env)->GetFieldID(env, TextRangeFc.clazz, "fStart", "I");
	TextRangeFc.fEnd = (*env)->GetFieldID(env, TextRangeFc.clazz, "fEnd", "I");
	TextRangeFc.fHiliteStyle = (*env)->GetFieldID(env, TextRangeFc.clazz, "fHiliteStyle", "S");
	TextRangeFc.cached = 1;
}

TextRange *getTextRangeFields(JNIEnv *env, jobject lpObject, TextRange *lpStruct)
{
	if (!TextRangeFc.cached) cacheTextRangeFields(env, lpObject);
	lpStruct->fStart = (*env)->GetIntField(env, lpObject, TextRangeFc.fStart);
	lpStruct->fEnd = (*env)->GetIntField(env, lpObject, TextRangeFc.fEnd);
	lpStruct->fHiliteStyle = (*env)->GetShortField(env, lpObject, TextRangeFc.fHiliteStyle);
	return lpStruct;
}

void setTextRangeFields(JNIEnv *env, jobject lpObject, TextRange *lpStruct)
{
	if (!TextRangeFc.cached) cacheTextRangeFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, TextRangeFc.fStart, (jint)lpStruct->fStart);
	(*env)->SetIntField(env, lpObject, TextRangeFc.fEnd, (jint)lpStruct->fEnd);
	(*env)->SetShortField(env, lpObject, TextRangeFc.fHiliteStyle, (jshort)lpStruct->fHiliteStyle);
}
#endif

#ifndef NO_ThemeButtonDrawInfo
typedef struct ThemeButtonDrawInfo_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID state, value, adornment;
} ThemeButtonDrawInfo_FID_CACHE;

ThemeButtonDrawInfo_FID_CACHE ThemeButtonDrawInfoFc;

void cacheThemeButtonDrawInfoFields(JNIEnv *env, jobject lpObject)
{
	if (ThemeButtonDrawInfoFc.cached) return;
	ThemeButtonDrawInfoFc.clazz = (*env)->GetObjectClass(env, lpObject);
	ThemeButtonDrawInfoFc.state = (*env)->GetFieldID(env, ThemeButtonDrawInfoFc.clazz, "state", "I");
	ThemeButtonDrawInfoFc.value = (*env)->GetFieldID(env, ThemeButtonDrawInfoFc.clazz, "value", "S");
	ThemeButtonDrawInfoFc.adornment = (*env)->GetFieldID(env, ThemeButtonDrawInfoFc.clazz, "adornment", "S");
	ThemeButtonDrawInfoFc.cached = 1;
}

ThemeButtonDrawInfo *getThemeButtonDrawInfoFields(JNIEnv *env, jobject lpObject, ThemeButtonDrawInfo *lpStruct)
{
	if (!ThemeButtonDrawInfoFc.cached) cacheThemeButtonDrawInfoFields(env, lpObject);
	lpStruct->state = (ThemeDrawState)(*env)->GetIntField(env, lpObject, ThemeButtonDrawInfoFc.state);
	lpStruct->value = (ThemeButtonValue)(*env)->GetShortField(env, lpObject, ThemeButtonDrawInfoFc.value);
	lpStruct->adornment = (ThemeButtonAdornment)(*env)->GetShortField(env, lpObject, ThemeButtonDrawInfoFc.adornment);
	return lpStruct;
}

void setThemeButtonDrawInfoFields(JNIEnv *env, jobject lpObject, ThemeButtonDrawInfo *lpStruct)
{
	if (!ThemeButtonDrawInfoFc.cached) cacheThemeButtonDrawInfoFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, ThemeButtonDrawInfoFc.state, (jint)lpStruct->state);
	(*env)->SetShortField(env, lpObject, ThemeButtonDrawInfoFc.value, (jshort)lpStruct->value);
	(*env)->SetShortField(env, lpObject, ThemeButtonDrawInfoFc.adornment, (jshort)lpStruct->adornment);
}
#endif

