/*******************************************************************************
* Copyright (c) 2000, 2005 IBM Corporation and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*     IBM Corporation - initial API and implementation
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
	CursorFc.data = (*env)->GetFieldID(env, CursorFc.clazz, "data", "[S");
	CursorFc.mask = (*env)->GetFieldID(env, CursorFc.clazz, "mask", "[S");
	CursorFc.hotSpot_v = (*env)->GetFieldID(env, CursorFc.clazz, "hotSpot_v", "S");
	CursorFc.hotSpot_h = (*env)->GetFieldID(env, CursorFc.clazz, "hotSpot_h", "S");
	CursorFc.cached = 1;
}

Cursor *getCursorFields(JNIEnv *env, jobject lpObject, Cursor *lpStruct)
{
	if (!CursorFc.cached) cacheCursorFields(env, lpObject);
	{
	jshortArray lpObject1 = (jshortArray)(*env)->GetObjectField(env, lpObject, CursorFc.data);
	(*env)->GetShortArrayRegion(env, lpObject1, 0, sizeof(lpStruct->data) / 2, (jshort *)lpStruct->data);
	}
	{
	jshortArray lpObject1 = (jshortArray)(*env)->GetObjectField(env, lpObject, CursorFc.mask);
	(*env)->GetShortArrayRegion(env, lpObject1, 0, sizeof(lpStruct->mask) / 2, (jshort *)lpStruct->mask);
	}
	lpStruct->hotSpot.v = (*env)->GetShortField(env, lpObject, CursorFc.hotSpot_v);
	lpStruct->hotSpot.h = (*env)->GetShortField(env, lpObject, CursorFc.hotSpot_h);
	return lpStruct;
}

void setCursorFields(JNIEnv *env, jobject lpObject, Cursor *lpStruct)
{
	if (!CursorFc.cached) cacheCursorFields(env, lpObject);
	{
	jshortArray lpObject1 = (jshortArray)(*env)->GetObjectField(env, lpObject, CursorFc.data);
	(*env)->SetShortArrayRegion(env, lpObject1, 0, sizeof(lpStruct->data) / 2, (jshort *)lpStruct->data);
	}
	{
	jshortArray lpObject1 = (jshortArray)(*env)->GetObjectField(env, lpObject, CursorFc.mask);
	(*env)->SetShortArrayRegion(env, lpObject1, 0, sizeof(lpStruct->mask) / 2, (jshort *)lpStruct->mask);
	}
	(*env)->SetShortField(env, lpObject, CursorFc.hotSpot_v, (jshort)lpStruct->hotSpot.v);
	(*env)->SetShortField(env, lpObject, CursorFc.hotSpot_h, (jshort)lpStruct->hotSpot.h);
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

#ifndef NO_FontInfo
typedef struct FontInfo_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID ascent, descent, widMax, leading;
} FontInfo_FID_CACHE;

FontInfo_FID_CACHE FontInfoFc;

void cacheFontInfoFields(JNIEnv *env, jobject lpObject)
{
	if (FontInfoFc.cached) return;
	FontInfoFc.clazz = (*env)->GetObjectClass(env, lpObject);
	FontInfoFc.ascent = (*env)->GetFieldID(env, FontInfoFc.clazz, "ascent", "S");
	FontInfoFc.descent = (*env)->GetFieldID(env, FontInfoFc.clazz, "descent", "S");
	FontInfoFc.widMax = (*env)->GetFieldID(env, FontInfoFc.clazz, "widMax", "S");
	FontInfoFc.leading = (*env)->GetFieldID(env, FontInfoFc.clazz, "leading", "S");
	FontInfoFc.cached = 1;
}

FontInfo *getFontInfoFields(JNIEnv *env, jobject lpObject, FontInfo *lpStruct)
{
	if (!FontInfoFc.cached) cacheFontInfoFields(env, lpObject);
	lpStruct->ascent = (*env)->GetShortField(env, lpObject, FontInfoFc.ascent);
	lpStruct->descent = (*env)->GetShortField(env, lpObject, FontInfoFc.descent);
	lpStruct->widMax = (*env)->GetShortField(env, lpObject, FontInfoFc.widMax);
	lpStruct->leading = (*env)->GetShortField(env, lpObject, FontInfoFc.leading);
	return lpStruct;
}

void setFontInfoFields(JNIEnv *env, jobject lpObject, FontInfo *lpStruct)
{
	if (!FontInfoFc.cached) cacheFontInfoFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, FontInfoFc.ascent, (jshort)lpStruct->ascent);
	(*env)->SetShortField(env, lpObject, FontInfoFc.descent, (jshort)lpStruct->descent);
	(*env)->SetShortField(env, lpObject, FontInfoFc.widMax, (jshort)lpStruct->widMax);
	(*env)->SetShortField(env, lpObject, FontInfoFc.leading, (jshort)lpStruct->leading);
}
#endif

#ifndef NO_FontSelectionQDStyle
typedef struct FontSelectionQDStyle_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID version, instance_fontFamily, instance_fontStyle, size, hasColor, reserved, color_red, color_green, color_blue;
} FontSelectionQDStyle_FID_CACHE;

FontSelectionQDStyle_FID_CACHE FontSelectionQDStyleFc;

void cacheFontSelectionQDStyleFields(JNIEnv *env, jobject lpObject)
{
	if (FontSelectionQDStyleFc.cached) return;
	FontSelectionQDStyleFc.clazz = (*env)->GetObjectClass(env, lpObject);
	FontSelectionQDStyleFc.version = (*env)->GetFieldID(env, FontSelectionQDStyleFc.clazz, "version", "I");
	FontSelectionQDStyleFc.instance_fontFamily = (*env)->GetFieldID(env, FontSelectionQDStyleFc.clazz, "instance_fontFamily", "S");
	FontSelectionQDStyleFc.instance_fontStyle = (*env)->GetFieldID(env, FontSelectionQDStyleFc.clazz, "instance_fontStyle", "S");
	FontSelectionQDStyleFc.size = (*env)->GetFieldID(env, FontSelectionQDStyleFc.clazz, "size", "S");
	FontSelectionQDStyleFc.hasColor = (*env)->GetFieldID(env, FontSelectionQDStyleFc.clazz, "hasColor", "Z");
	FontSelectionQDStyleFc.reserved = (*env)->GetFieldID(env, FontSelectionQDStyleFc.clazz, "reserved", "B");
	FontSelectionQDStyleFc.color_red = (*env)->GetFieldID(env, FontSelectionQDStyleFc.clazz, "color_red", "S");
	FontSelectionQDStyleFc.color_green = (*env)->GetFieldID(env, FontSelectionQDStyleFc.clazz, "color_green", "S");
	FontSelectionQDStyleFc.color_blue = (*env)->GetFieldID(env, FontSelectionQDStyleFc.clazz, "color_blue", "S");
	FontSelectionQDStyleFc.cached = 1;
}

FontSelectionQDStyle *getFontSelectionQDStyleFields(JNIEnv *env, jobject lpObject, FontSelectionQDStyle *lpStruct)
{
	if (!FontSelectionQDStyleFc.cached) cacheFontSelectionQDStyleFields(env, lpObject);
	lpStruct->version = (*env)->GetIntField(env, lpObject, FontSelectionQDStyleFc.version);
	lpStruct->instance.fontFamily = (*env)->GetShortField(env, lpObject, FontSelectionQDStyleFc.instance_fontFamily);
	lpStruct->instance.fontStyle = (*env)->GetShortField(env, lpObject, FontSelectionQDStyleFc.instance_fontStyle);
	lpStruct->size = (*env)->GetShortField(env, lpObject, FontSelectionQDStyleFc.size);
	lpStruct->hasColor = (*env)->GetBooleanField(env, lpObject, FontSelectionQDStyleFc.hasColor);
	lpStruct->reserved = (*env)->GetByteField(env, lpObject, FontSelectionQDStyleFc.reserved);
	lpStruct->color.red = (*env)->GetShortField(env, lpObject, FontSelectionQDStyleFc.color_red);
	lpStruct->color.green = (*env)->GetShortField(env, lpObject, FontSelectionQDStyleFc.color_green);
	lpStruct->color.blue = (*env)->GetShortField(env, lpObject, FontSelectionQDStyleFc.color_blue);
	return lpStruct;
}

void setFontSelectionQDStyleFields(JNIEnv *env, jobject lpObject, FontSelectionQDStyle *lpStruct)
{
	if (!FontSelectionQDStyleFc.cached) cacheFontSelectionQDStyleFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, FontSelectionQDStyleFc.version, (jint)lpStruct->version);
	(*env)->SetShortField(env, lpObject, FontSelectionQDStyleFc.instance_fontFamily, (jshort)lpStruct->instance.fontFamily);
	(*env)->SetShortField(env, lpObject, FontSelectionQDStyleFc.instance_fontStyle, (jshort)lpStruct->instance.fontStyle);
	(*env)->SetShortField(env, lpObject, FontSelectionQDStyleFc.size, (jshort)lpStruct->size);
	(*env)->SetBooleanField(env, lpObject, FontSelectionQDStyleFc.hasColor, (jboolean)lpStruct->hasColor);
	(*env)->SetByteField(env, lpObject, FontSelectionQDStyleFc.reserved, (jbyte)lpStruct->reserved);
	(*env)->SetShortField(env, lpObject, FontSelectionQDStyleFc.color_red, (jshort)lpStruct->color.red);
	(*env)->SetShortField(env, lpObject, FontSelectionQDStyleFc.color_green, (jshort)lpStruct->color.green);
	(*env)->SetShortField(env, lpObject, FontSelectionQDStyleFc.color_blue, (jshort)lpStruct->color.blue);
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

#ifndef NO_TXNLongRect
typedef struct TXNLongRect_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID top, left, bottom, right;
} TXNLongRect_FID_CACHE;

TXNLongRect_FID_CACHE TXNLongRectFc;

void cacheTXNLongRectFields(JNIEnv *env, jobject lpObject)
{
	if (TXNLongRectFc.cached) return;
	TXNLongRectFc.clazz = (*env)->GetObjectClass(env, lpObject);
	TXNLongRectFc.top = (*env)->GetFieldID(env, TXNLongRectFc.clazz, "top", "I");
	TXNLongRectFc.left = (*env)->GetFieldID(env, TXNLongRectFc.clazz, "left", "I");
	TXNLongRectFc.bottom = (*env)->GetFieldID(env, TXNLongRectFc.clazz, "bottom", "I");
	TXNLongRectFc.right = (*env)->GetFieldID(env, TXNLongRectFc.clazz, "right", "I");
	TXNLongRectFc.cached = 1;
}

TXNLongRect *getTXNLongRectFields(JNIEnv *env, jobject lpObject, TXNLongRect *lpStruct)
{
	if (!TXNLongRectFc.cached) cacheTXNLongRectFields(env, lpObject);
	lpStruct->top = (*env)->GetIntField(env, lpObject, TXNLongRectFc.top);
	lpStruct->left = (*env)->GetIntField(env, lpObject, TXNLongRectFc.left);
	lpStruct->bottom = (*env)->GetIntField(env, lpObject, TXNLongRectFc.bottom);
	lpStruct->right = (*env)->GetIntField(env, lpObject, TXNLongRectFc.right);
	return lpStruct;
}

void setTXNLongRectFields(JNIEnv *env, jobject lpObject, TXNLongRect *lpStruct)
{
	if (!TXNLongRectFc.cached) cacheTXNLongRectFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, TXNLongRectFc.top, (jint)lpStruct->top);
	(*env)->SetIntField(env, lpObject, TXNLongRectFc.left, (jint)lpStruct->left);
	(*env)->SetIntField(env, lpObject, TXNLongRectFc.bottom, (jint)lpStruct->bottom);
	(*env)->SetIntField(env, lpObject, TXNLongRectFc.right, (jint)lpStruct->right);
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

