/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

/**
 * JNI SWT object field getters and setters declarations for Mac/Carbon structs.
 */

#include "swt.h"
#include "structs.h"

#ifndef NO_AEDesc
AEDesc *getAEDescFields(JNIEnv *env, jobject lpObject, AEDesc *lpStruct);
void setAEDescFields(JNIEnv *env, jobject lpObject, AEDesc *lpStruct);
#else
#define getAEDescFields(a,b,c) NULL
#define setAEDescFields(a,b,c)
#endif /* NO_AEDesc */

#ifndef NO_AEDesc
typedef struct AEDesc_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID descriptorType, dataHandle;
} AEDesc_FID_CACHE;

AEDesc_FID_CACHE AEDescFc;

void cacheAEDescFids(JNIEnv *env, jobject lpObject)
{
	if (AEDescFc.cached) return;
	AEDescFc.clazz = (*env)->GetObjectClass(env, lpObject);
	AEDescFc.descriptorType = (*env)->GetFieldID(env, AEDescFc.clazz, "descriptorType", "I");
	AEDescFc.dataHandle = (*env)->GetFieldID(env, AEDescFc.clazz, "dataHandle", "I");
	AEDescFc.cached = 1;
}

AEDesc *getAEDescFields(JNIEnv *env, jobject lpObject, AEDesc *lpStruct)
{
	if (!AEDescFc.cached) cacheAEDescFids(env, lpObject);
	lpStruct->descriptorType = (DescType)(*env)->GetIntField(env, lpObject, AEDescFc.descriptorType);
	lpStruct->dataHandle = (AEDataStorage)(*env)->GetIntField(env, lpObject, AEDescFc.dataHandle);
	return lpStruct;
}

void setAEDescFields(JNIEnv *env, jobject lpObject, AEDesc *lpStruct)
{
	if (!AEDescFc.cached) cacheAEDescFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, AEDescFc.descriptorType, (jint)lpStruct->descriptorType);
	(*env)->SetIntField(env, lpObject, AEDescFc.dataHandle, (jint)lpStruct->dataHandle);
}
#endif /* NO_AEDesc */

#ifndef NO_CFRange
typedef struct CFRange_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID location, length;
} CFRange_FID_CACHE;

CFRange_FID_CACHE CFRangeFc;

void cacheCFRangeFids(JNIEnv *env, jobject lpObject)
{
	if (CFRangeFc.cached) return;
	CFRangeFc.clazz = (*env)->GetObjectClass(env, lpObject);
	CFRangeFc.location = (*env)->GetFieldID(env, CFRangeFc.clazz, "location", "I");
	CFRangeFc.length = (*env)->GetFieldID(env, CFRangeFc.clazz, "length", "I");
	CFRangeFc.cached = 1;
}

CFRange *getCFRangeFields(JNIEnv *env, jobject lpObject, CFRange *lpStruct)
{
	if (!CFRangeFc.cached) cacheCFRangeFids(env, lpObject);
	lpStruct->location = (CFIndex)(*env)->GetIntField(env, lpObject, CFRangeFc.location);
	lpStruct->length = (CFIndex)(*env)->GetIntField(env, lpObject, CFRangeFc.length);
	return lpStruct;
}

void setCFRangeFields(JNIEnv *env, jobject lpObject, CFRange *lpStruct)
{
	if (!CFRangeFc.cached) cacheCFRangeFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, CFRangeFc.location, (jint)lpStruct->location);
	(*env)->SetIntField(env, lpObject, CFRangeFc.length, (jint)lpStruct->length);
}
#endif /* NO_CFRange */

#ifndef NO_CGPoint
typedef struct CGPoint_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID x, y;
} CGPoint_FID_CACHE;

CGPoint_FID_CACHE CGPointFc;

void cacheCGPointFids(JNIEnv *env, jobject lpObject)
{
	if (CGPointFc.cached) return;
	CGPointFc.clazz = (*env)->GetObjectClass(env, lpObject);
	CGPointFc.x = (*env)->GetFieldID(env, CGPointFc.clazz, "x", "F");
	CGPointFc.y = (*env)->GetFieldID(env, CGPointFc.clazz, "y", "F");
	CGPointFc.cached = 1;
}

CGPoint *getCGPointFields(JNIEnv *env, jobject lpObject, CGPoint *lpStruct)
{
	if (!CGPointFc.cached) cacheCGPointFids(env, lpObject);
	lpStruct->x = (float)(*env)->GetFloatField(env, lpObject, CGPointFc.x);
	lpStruct->y = (float)(*env)->GetFloatField(env, lpObject, CGPointFc.y);
	return lpStruct;
}

void setCGPointFields(JNIEnv *env, jobject lpObject, CGPoint *lpStruct)
{
	if (!CGPointFc.cached) cacheCGPointFids(env, lpObject);
	(*env)->SetFloatField(env, lpObject, CGPointFc.x, (jfloat)lpStruct->x);
	(*env)->SetFloatField(env, lpObject, CGPointFc.y, (jfloat)lpStruct->y);
}
#endif /* NO_CGPoint */

#ifndef NO_CGRect
typedef struct CGRect_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID x, y, width, height;
} CGRect_FID_CACHE;

CGRect_FID_CACHE CGRectFc;

void cacheCGRectFids(JNIEnv *env, jobject lpObject)
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
	if (!CGRectFc.cached) cacheCGRectFids(env, lpObject);
	lpStruct->origin.x = (float)(*env)->GetFloatField(env, lpObject, CGRectFc.x);
	lpStruct->origin.y = (float)(*env)->GetFloatField(env, lpObject, CGRectFc.y);
	lpStruct->size.width = (float)(*env)->GetFloatField(env, lpObject, CGRectFc.width);
	lpStruct->size.height = (float)(*env)->GetFloatField(env, lpObject, CGRectFc.height);
	return lpStruct;
}

void setCGRectFields(JNIEnv *env, jobject lpObject, CGRect *lpStruct)
{
	if (!CGRectFc.cached) cacheCGRectFids(env, lpObject);
	(*env)->SetFloatField(env, lpObject, CGRectFc.x, (jfloat)lpStruct->origin.x);
	(*env)->SetFloatField(env, lpObject, CGRectFc.y, (jfloat)lpStruct->origin.y);
	(*env)->SetFloatField(env, lpObject, CGRectFc.width, (jfloat)lpStruct->size.width);
	(*env)->SetFloatField(env, lpObject, CGRectFc.height, (jfloat)lpStruct->size.height);
}
#endif /* NO_CGRect */

#ifndef NO_ColorPickerInfo
typedef struct ColorPickerInfo_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID profile, red, green, blue, dstProfile, flags, placeWhere, h, v, pickerType, eventProc, colorProc, colorProcData, prompt, editMenuID, cutItem, copyItem, pasteItem, clearItem, undoItem, newColorChosen;
} ColorPickerInfo_FID_CACHE;

ColorPickerInfo_FID_CACHE ColorPickerInfoFc;

void cacheColorPickerInfoFids(JNIEnv *env, jobject lpObject)
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
	if (!ColorPickerInfoFc.cached) cacheColorPickerInfoFids(env, lpObject);
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
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, ColorPickerInfoFc.prompt);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->prompt), lpStruct->prompt);
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
	if (!ColorPickerInfoFc.cached) cacheColorPickerInfoFids(env, lpObject);
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
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, ColorPickerInfoFc.prompt);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->prompt), lpStruct->prompt);
	}
	(*env)->SetShortField(env, lpObject, ColorPickerInfoFc.editMenuID, (jshort)lpStruct->mInfo.editMenuID);
	(*env)->SetShortField(env, lpObject, ColorPickerInfoFc.cutItem, (jshort)lpStruct->mInfo.cutItem);
	(*env)->SetShortField(env, lpObject, ColorPickerInfoFc.copyItem, (jshort)lpStruct->mInfo.copyItem);
	(*env)->SetShortField(env, lpObject, ColorPickerInfoFc.pasteItem, (jshort)lpStruct->mInfo.pasteItem);
	(*env)->SetShortField(env, lpObject, ColorPickerInfoFc.clearItem, (jshort)lpStruct->mInfo.clearItem);
	(*env)->SetShortField(env, lpObject, ColorPickerInfoFc.undoItem, (jshort)lpStruct->mInfo.undoItem);
	(*env)->SetBooleanField(env, lpObject, ColorPickerInfoFc.newColorChosen, (jboolean)lpStruct->newColorChosen);
}
#endif /* NO_ColorPickerInfo */

#ifndef NO_ControlButtonContentInfo
typedef struct ControlButtonContentInfo_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID contentType, iconRef;
} ControlButtonContentInfo_FID_CACHE;

ControlButtonContentInfo_FID_CACHE ControlButtonContentInfoFc;

void cacheControlButtonContentInfoFids(JNIEnv *env, jobject lpObject)
{
	if (ControlButtonContentInfoFc.cached) return;
	ControlButtonContentInfoFc.clazz = (*env)->GetObjectClass(env, lpObject);
	ControlButtonContentInfoFc.contentType = (*env)->GetFieldID(env, ControlButtonContentInfoFc.clazz, "contentType", "S");
	ControlButtonContentInfoFc.iconRef = (*env)->GetFieldID(env, ControlButtonContentInfoFc.clazz, "iconRef", "I");
	ControlButtonContentInfoFc.cached = 1;
}

ControlButtonContentInfo *getControlButtonContentInfoFields(JNIEnv *env, jobject lpObject, ControlButtonContentInfo *lpStruct)
{
	if (!ControlButtonContentInfoFc.cached) cacheControlButtonContentInfoFids(env, lpObject);
	lpStruct->contentType = (ControlContentType)(*env)->GetShortField(env, lpObject, ControlButtonContentInfoFc.contentType);
	lpStruct->u.iconRef = (void *)(*env)->GetIntField(env, lpObject, ControlButtonContentInfoFc.iconRef);
	return lpStruct;
}

void setControlButtonContentInfoFields(JNIEnv *env, jobject lpObject, ControlButtonContentInfo *lpStruct)
{
	if (!ControlButtonContentInfoFc.cached) cacheControlButtonContentInfoFids(env, lpObject);
	(*env)->SetShortField(env, lpObject, ControlButtonContentInfoFc.contentType, (jshort)lpStruct->contentType);
	(*env)->SetIntField(env, lpObject, ControlButtonContentInfoFc.iconRef, (jint)lpStruct->u.iconRef);
}
#endif /* NO_ControlButtonContentInfo */

#ifndef NO_ControlFontStyleRec
typedef struct ControlFontStyleRec_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID flags, font, size, style, mode, just, foreColor_red, foreColor_green, foreColor_blue, backColor_red, backColor_green, backColor_blue;
} ControlFontStyleRec_FID_CACHE;

ControlFontStyleRec_FID_CACHE ControlFontStyleRecFc;

void cacheControlFontStyleRecFids(JNIEnv *env, jobject lpObject)
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
	if (!ControlFontStyleRecFc.cached) cacheControlFontStyleRecFids(env, lpObject);
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
	if (!ControlFontStyleRecFc.cached) cacheControlFontStyleRecFids(env, lpObject);
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
#endif /* NO_ControlFontStyleRec */

#ifndef NO_ControlTabInfoRecV1
typedef struct ControlTabInfoRecV1_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID version, iconSuiteID, name;
} ControlTabInfoRecV1_FID_CACHE;

ControlTabInfoRecV1_FID_CACHE ControlTabInfoRecV1Fc;

void cacheControlTabInfoRecV1Fids(JNIEnv *env, jobject lpObject)
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
	if (!ControlTabInfoRecV1Fc.cached) cacheControlTabInfoRecV1Fids(env, lpObject);
	lpStruct->version = (SInt16)(*env)->GetShortField(env, lpObject, ControlTabInfoRecV1Fc.version);
	lpStruct->iconSuiteID = (SInt16)(*env)->GetShortField(env, lpObject, ControlTabInfoRecV1Fc.iconSuiteID);
	lpStruct->name = (CFStringRef)(*env)->GetIntField(env, lpObject, ControlTabInfoRecV1Fc.name);
	return lpStruct;
}

void setControlTabInfoRecV1Fields(JNIEnv *env, jobject lpObject, ControlTabInfoRecV1 *lpStruct)
{
	if (!ControlTabInfoRecV1Fc.cached) cacheControlTabInfoRecV1Fids(env, lpObject);
	(*env)->SetShortField(env, lpObject, ControlTabInfoRecV1Fc.version, (jshort)lpStruct->version);
	(*env)->SetShortField(env, lpObject, ControlTabInfoRecV1Fc.iconSuiteID, (jshort)lpStruct->iconSuiteID);
	(*env)->SetIntField(env, lpObject, ControlTabInfoRecV1Fc.name, (jint)lpStruct->name);
}
#endif /* NO_ControlTabInfoRecV1 */

#ifndef NO_NavDialogCreationOptions
typedef struct NavDialogCreationOptions_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID version, optionFlags, location_h, location_v, clientName, windowTitle, actionButtonLabel, cancelButtonLabel, saveFileName, message, preferenceKey, popupExtension, modality, parentWindow;
} NavDialogCreationOptions_FID_CACHE;

NavDialogCreationOptions_FID_CACHE NavDialogCreationOptionsFc;

void cacheNavDialogCreationOptionsFids(JNIEnv *env, jobject lpObject)
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
	if (!NavDialogCreationOptionsFc.cached) cacheNavDialogCreationOptionsFids(env, lpObject);
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
	if (!NavDialogCreationOptionsFc.cached) cacheNavDialogCreationOptionsFids(env, lpObject);
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
#endif /* NO_NavDialogCreationOptions */

#ifndef NO_NavReplyRecord
NavReplyRecord *getNavReplyRecordFields(JNIEnv *env, jobject lpObject, NavReplyRecord *lpStruct);
void setNavReplyRecordFields(JNIEnv *env, jobject lpObject, NavReplyRecord *lpStruct);
#else
#define getNavReplyRecordFields(a,b,c) NULL
#define setNavReplyRecordFields(a,b,c)
#endif /* NO_NavReplyRecord */

#ifndef NO_NavReplyRecord
typedef struct NavReplyRecord_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID version, validRecord, replacing, isStationery, translationNeeded, selection_descriptorType, selection_dataHandle, keyScript, fileTranslation, reserved1, saveFileName, saveFileExtensionHidden, reserved2, reserved;
} NavReplyRecord_FID_CACHE;

NavReplyRecord_FID_CACHE NavReplyRecordFc;

void cacheNavReplyRecordFids(JNIEnv *env, jobject lpObject)
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
	if (!NavReplyRecordFc.cached) cacheNavReplyRecordFids(env, lpObject);
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
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, NavReplyRecordFc.reserved);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->reserved), lpStruct->reserved);
	}
	return lpStruct;
}

void setNavReplyRecordFields(JNIEnv *env, jobject lpObject, NavReplyRecord *lpStruct)
{
	if (!NavReplyRecordFc.cached) cacheNavReplyRecordFids(env, lpObject);
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
	jbyteArray lpObject1 = (*env)->GetObjectField(env, lpObject, NavReplyRecordFc.reserved);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->reserved), lpStruct->reserved);
	}
}
#endif /* NO_NavReplyRecord */

#ifndef NO_Point
typedef struct Point_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID v, h;
} Point_FID_CACHE;

Point_FID_CACHE PointFc;

void cachePointFids(JNIEnv *env, jobject lpObject)
{
	if (PointFc.cached) return;
	PointFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PointFc.v = (*env)->GetFieldID(env, PointFc.clazz, "v", "S");
	PointFc.h = (*env)->GetFieldID(env, PointFc.clazz, "h", "S");
	PointFc.cached = 1;
}

Point *getPointFields(JNIEnv *env, jobject lpObject, Point *lpStruct)
{
	if (!PointFc.cached) cachePointFids(env, lpObject);
	lpStruct->v = (*env)->GetShortField(env, lpObject, PointFc.v);
	lpStruct->h = (*env)->GetShortField(env, lpObject, PointFc.h);
	return lpStruct;
}

void setPointFields(JNIEnv *env, jobject lpObject, Point *lpStruct)
{
	if (!PointFc.cached) cachePointFids(env, lpObject);
	(*env)->SetShortField(env, lpObject, PointFc.v, (jshort)lpStruct->v);
	(*env)->SetShortField(env, lpObject, PointFc.h, (jshort)lpStruct->h);
}
#endif /* NO_Point */

#ifndef NO_RGBColor
typedef struct RGBColor_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID red, green, blue;
} RGBColor_FID_CACHE;

RGBColor_FID_CACHE RGBColorFc;

void cacheRGBColorFids(JNIEnv *env, jobject lpObject)
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
	if (!RGBColorFc.cached) cacheRGBColorFids(env, lpObject);
	lpStruct->red = (*env)->GetShortField(env, lpObject, RGBColorFc.red);
	lpStruct->green = (*env)->GetShortField(env, lpObject, RGBColorFc.green);
	lpStruct->blue = (*env)->GetShortField(env, lpObject, RGBColorFc.blue);
	return lpStruct;
}

void setRGBColorFields(JNIEnv *env, jobject lpObject, RGBColor *lpStruct)
{
	if (!RGBColorFc.cached) cacheRGBColorFids(env, lpObject);
	(*env)->SetShortField(env, lpObject, RGBColorFc.red, (jshort)lpStruct->red);
	(*env)->SetShortField(env, lpObject, RGBColorFc.green, (jshort)lpStruct->green);
	(*env)->SetShortField(env, lpObject, RGBColorFc.blue, (jshort)lpStruct->blue);
}
#endif /* NO_RGBColor */

#ifndef NO_Rect
typedef struct Rect_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID top, left, bottom, right;
} Rect_FID_CACHE;

Rect_FID_CACHE RectFc;

void cacheRectFids(JNIEnv *env, jobject lpObject)
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
	if (!RectFc.cached) cacheRectFids(env, lpObject);
	lpStruct->top = (*env)->GetShortField(env, lpObject, RectFc.top);
	lpStruct->left = (*env)->GetShortField(env, lpObject, RectFc.left);
	lpStruct->bottom = (*env)->GetShortField(env, lpObject, RectFc.bottom);
	lpStruct->right = (*env)->GetShortField(env, lpObject, RectFc.right);
	return lpStruct;
}

void setRectFields(JNIEnv *env, jobject lpObject, Rect *lpStruct)
{
	if (!RectFc.cached) cacheRectFids(env, lpObject);
	(*env)->SetShortField(env, lpObject, RectFc.top, (jshort)lpStruct->top);
	(*env)->SetShortField(env, lpObject, RectFc.left, (jshort)lpStruct->left);
	(*env)->SetShortField(env, lpObject, RectFc.bottom, (jshort)lpStruct->bottom);
	(*env)->SetShortField(env, lpObject, RectFc.right, (jshort)lpStruct->right);
}
#endif /* NO_Rect */

