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

#include "swt.h"
#include "structs_ole.h"

#ifndef NO_CAUUID
typedef struct CAUUID_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cElems, pElems;
} CAUUID_FID_CACHE;

CAUUID_FID_CACHE CAUUIDFc;

void cacheCAUUIDFids(JNIEnv *env, jobject lpObject)
{
	if (CAUUIDFc.cached) return;
	CAUUIDFc.clazz = (*env)->GetObjectClass(env, lpObject);
	CAUUIDFc.cElems = (*env)->GetFieldID(env, CAUUIDFc.clazz, "cElems", "I");
	CAUUIDFc.pElems = (*env)->GetFieldID(env, CAUUIDFc.clazz, "pElems", "I");
	CAUUIDFc.cached = 1;
}

CAUUID *getCAUUIDFields(JNIEnv *env, jobject lpObject, CAUUID *lpStruct)
{
	if (!CAUUIDFc.cached) cacheCAUUIDFids(env, lpObject);
	lpStruct->cElems = (*env)->GetIntField(env, lpObject, CAUUIDFc.cElems);
	lpStruct->pElems = (GUID FAR *)(*env)->GetIntField(env, lpObject, CAUUIDFc.pElems);
	return lpStruct;
}

void setCAUUIDFields(JNIEnv *env, jobject lpObject, CAUUID *lpStruct)
{
	if (!CAUUIDFc.cached) cacheCAUUIDFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, CAUUIDFc.cElems, (jint)lpStruct->cElems);
	(*env)->SetIntField(env, lpObject, CAUUIDFc.pElems, (jint)lpStruct->pElems);
}
#endif /* NO_CAUUID */

#ifndef NO_CONTROLINFO
typedef struct CONTROLINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cb, hAccel, cAccel, dwFlags;
} CONTROLINFO_FID_CACHE;

CONTROLINFO_FID_CACHE CONTROLINFOFc;

void cacheCONTROLINFOFids(JNIEnv *env, jobject lpObject)
{
	if (CONTROLINFOFc.cached) return;
	CONTROLINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	CONTROLINFOFc.cb = (*env)->GetFieldID(env, CONTROLINFOFc.clazz, "cb", "I");
	CONTROLINFOFc.hAccel = (*env)->GetFieldID(env, CONTROLINFOFc.clazz, "hAccel", "I");
	CONTROLINFOFc.cAccel = (*env)->GetFieldID(env, CONTROLINFOFc.clazz, "cAccel", "S");
	CONTROLINFOFc.dwFlags = (*env)->GetFieldID(env, CONTROLINFOFc.clazz, "dwFlags", "I");
	CONTROLINFOFc.cached = 1;
}

CONTROLINFO *getCONTROLINFOFields(JNIEnv *env, jobject lpObject, CONTROLINFO *lpStruct)
{
	if (!CONTROLINFOFc.cached) cacheCONTROLINFOFids(env, lpObject);
	lpStruct->cb = (*env)->GetIntField(env, lpObject, CONTROLINFOFc.cb);
	lpStruct->hAccel = (HACCEL)(*env)->GetIntField(env, lpObject, CONTROLINFOFc.hAccel);
	lpStruct->cAccel = (*env)->GetShortField(env, lpObject, CONTROLINFOFc.cAccel);
	lpStruct->dwFlags = (*env)->GetIntField(env, lpObject, CONTROLINFOFc.dwFlags);
	return lpStruct;
}

void setCONTROLINFOFields(JNIEnv *env, jobject lpObject, CONTROLINFO *lpStruct)
{
	if (!CONTROLINFOFc.cached) cacheCONTROLINFOFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, CONTROLINFOFc.cb, (jint)lpStruct->cb);
	(*env)->SetIntField(env, lpObject, CONTROLINFOFc.hAccel, (jint)lpStruct->hAccel);
	(*env)->SetShortField(env, lpObject, CONTROLINFOFc.cAccel, (jshort)lpStruct->cAccel);
	(*env)->SetIntField(env, lpObject, CONTROLINFOFc.dwFlags, (jint)lpStruct->dwFlags);
}
#endif /* NO_CONTROLINFO */

#ifndef NO_COSERVERINFO
typedef struct COSERVERINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID dwReserved1, pwszName, pAuthInfo, dwReserved2;
} COSERVERINFO_FID_CACHE;

COSERVERINFO_FID_CACHE COSERVERINFOFc;

void cacheCOSERVERINFOFids(JNIEnv *env, jobject lpObject)
{
	if (COSERVERINFOFc.cached) return;
	COSERVERINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	COSERVERINFOFc.dwReserved1 = (*env)->GetFieldID(env, COSERVERINFOFc.clazz, "dwReserved1", "I");
	COSERVERINFOFc.pwszName = (*env)->GetFieldID(env, COSERVERINFOFc.clazz, "pwszName", "I");
	COSERVERINFOFc.pAuthInfo = (*env)->GetFieldID(env, COSERVERINFOFc.clazz, "pAuthInfo", "I");
	COSERVERINFOFc.dwReserved2 = (*env)->GetFieldID(env, COSERVERINFOFc.clazz, "dwReserved2", "I");
	COSERVERINFOFc.cached = 1;
}

COSERVERINFO *getCOSERVERINFOFields(JNIEnv *env, jobject lpObject, COSERVERINFO *lpStruct)
{
	if (!COSERVERINFOFc.cached) cacheCOSERVERINFOFids(env, lpObject);
	lpStruct->dwReserved1 = (*env)->GetIntField(env, lpObject, COSERVERINFOFc.dwReserved1);
	lpStruct->pwszName = (LPWSTR)(*env)->GetIntField(env, lpObject, COSERVERINFOFc.pwszName);
	lpStruct->pAuthInfo = (COAUTHINFO *)(*env)->GetIntField(env, lpObject, COSERVERINFOFc.pAuthInfo);
	lpStruct->dwReserved2 = (*env)->GetIntField(env, lpObject, COSERVERINFOFc.dwReserved2);
	return lpStruct;
}

void setCOSERVERINFOFields(JNIEnv *env, jobject lpObject, COSERVERINFO *lpStruct)
{
	if (!COSERVERINFOFc.cached) cacheCOSERVERINFOFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, COSERVERINFOFc.dwReserved1, (jint)lpStruct->dwReserved1);
	(*env)->SetIntField(env, lpObject, COSERVERINFOFc.pwszName, (jint)lpStruct->pwszName);
	(*env)->SetIntField(env, lpObject, COSERVERINFOFc.pAuthInfo, (jint)lpStruct->pAuthInfo);
	(*env)->SetIntField(env, lpObject, COSERVERINFOFc.dwReserved2, (jint)lpStruct->dwReserved2);
}
#endif /* NO_COSERVERINFO */

#ifndef NO_DISPPARAMS
typedef struct DISPPARAMS_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID rgvarg, rgdispidNamedArgs, cArgs, cNamedArgs;
} DISPPARAMS_FID_CACHE;

DISPPARAMS_FID_CACHE DISPPARAMSFc;

void cacheDISPPARAMSFids(JNIEnv *env, jobject lpObject)
{
	if (DISPPARAMSFc.cached) return;
	DISPPARAMSFc.clazz = (*env)->GetObjectClass(env, lpObject);
	DISPPARAMSFc.rgvarg = (*env)->GetFieldID(env, DISPPARAMSFc.clazz, "rgvarg", "I");
	DISPPARAMSFc.rgdispidNamedArgs = (*env)->GetFieldID(env, DISPPARAMSFc.clazz, "rgdispidNamedArgs", "I");
	DISPPARAMSFc.cArgs = (*env)->GetFieldID(env, DISPPARAMSFc.clazz, "cArgs", "I");
	DISPPARAMSFc.cNamedArgs = (*env)->GetFieldID(env, DISPPARAMSFc.clazz, "cNamedArgs", "I");
	DISPPARAMSFc.cached = 1;
}

DISPPARAMS *getDISPPARAMSFields(JNIEnv *env, jobject lpObject, DISPPARAMS *lpStruct)
{
	if (!DISPPARAMSFc.cached) cacheDISPPARAMSFids(env, lpObject);
	lpStruct->rgvarg = (VARIANTARG FAR *)(*env)->GetIntField(env, lpObject, DISPPARAMSFc.rgvarg);
	lpStruct->rgdispidNamedArgs = (DISPID FAR *)(*env)->GetIntField(env, lpObject, DISPPARAMSFc.rgdispidNamedArgs);
	lpStruct->cArgs = (*env)->GetIntField(env, lpObject, DISPPARAMSFc.cArgs);
	lpStruct->cNamedArgs = (*env)->GetIntField(env, lpObject, DISPPARAMSFc.cNamedArgs);
	return lpStruct;
}

void setDISPPARAMSFields(JNIEnv *env, jobject lpObject, DISPPARAMS *lpStruct)
{
	if (!DISPPARAMSFc.cached) cacheDISPPARAMSFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, DISPPARAMSFc.rgvarg, (jint)lpStruct->rgvarg);
	(*env)->SetIntField(env, lpObject, DISPPARAMSFc.rgdispidNamedArgs, (jint)lpStruct->rgdispidNamedArgs);
	(*env)->SetIntField(env, lpObject, DISPPARAMSFc.cArgs, (jint)lpStruct->cArgs);
	(*env)->SetIntField(env, lpObject, DISPPARAMSFc.cNamedArgs, (jint)lpStruct->cNamedArgs);
}
#endif /* NO_DISPPARAMS */

#ifndef NO_DVTARGETDEVICE
typedef struct DVTARGETDEVICE_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID tdSize, tdDriverNameOffset, tdDeviceNameOffset, tdPortNameOffset, tdExtDevmodeOffset, tdData;
} DVTARGETDEVICE_FID_CACHE;

DVTARGETDEVICE_FID_CACHE DVTARGETDEVICEFc;

void cacheDVTARGETDEVICEFids(JNIEnv *env, jobject lpObject)
{
	if (DVTARGETDEVICEFc.cached) return;
	DVTARGETDEVICEFc.clazz = (*env)->GetObjectClass(env, lpObject);
	DVTARGETDEVICEFc.tdSize = (*env)->GetFieldID(env, DVTARGETDEVICEFc.clazz, "tdSize", "I");
	DVTARGETDEVICEFc.tdDriverNameOffset = (*env)->GetFieldID(env, DVTARGETDEVICEFc.clazz, "tdDriverNameOffset", "S");
	DVTARGETDEVICEFc.tdDeviceNameOffset = (*env)->GetFieldID(env, DVTARGETDEVICEFc.clazz, "tdDeviceNameOffset", "S");
	DVTARGETDEVICEFc.tdPortNameOffset = (*env)->GetFieldID(env, DVTARGETDEVICEFc.clazz, "tdPortNameOffset", "S");
	DVTARGETDEVICEFc.tdExtDevmodeOffset = (*env)->GetFieldID(env, DVTARGETDEVICEFc.clazz, "tdExtDevmodeOffset", "S");
	DVTARGETDEVICEFc.tdData = (*env)->GetFieldID(env, DVTARGETDEVICEFc.clazz, "tdData", "B");
	DVTARGETDEVICEFc.cached = 1;
}

DVTARGETDEVICE *getDVTARGETDEVICEFields(JNIEnv *env, jobject lpObject, DVTARGETDEVICE *lpStruct)
{
	if (!DVTARGETDEVICEFc.cached) cacheDVTARGETDEVICEFids(env, lpObject);
	lpStruct->tdSize = (*env)->GetIntField(env, lpObject, DVTARGETDEVICEFc.tdSize);
	lpStruct->tdDriverNameOffset = (*env)->GetShortField(env, lpObject, DVTARGETDEVICEFc.tdDriverNameOffset);
	lpStruct->tdDeviceNameOffset = (*env)->GetShortField(env, lpObject, DVTARGETDEVICEFc.tdDeviceNameOffset);
	lpStruct->tdPortNameOffset = (*env)->GetShortField(env, lpObject, DVTARGETDEVICEFc.tdPortNameOffset);
	lpStruct->tdExtDevmodeOffset = (*env)->GetShortField(env, lpObject, DVTARGETDEVICEFc.tdExtDevmodeOffset);
	*lpStruct->tdData = (*env)->GetByteField(env, lpObject, DVTARGETDEVICEFc.tdData);
	return lpStruct;
}

void setDVTARGETDEVICEFields(JNIEnv *env, jobject lpObject, DVTARGETDEVICE *lpStruct)
{
	if (!DVTARGETDEVICEFc.cached) cacheDVTARGETDEVICEFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, DVTARGETDEVICEFc.tdSize, (jint)lpStruct->tdSize);
	(*env)->SetShortField(env, lpObject, DVTARGETDEVICEFc.tdDriverNameOffset, (jshort)lpStruct->tdDriverNameOffset);
	(*env)->SetShortField(env, lpObject, DVTARGETDEVICEFc.tdDeviceNameOffset, (jshort)lpStruct->tdDeviceNameOffset);
	(*env)->SetShortField(env, lpObject, DVTARGETDEVICEFc.tdPortNameOffset, (jshort)lpStruct->tdPortNameOffset);
	(*env)->SetShortField(env, lpObject, DVTARGETDEVICEFc.tdExtDevmodeOffset, (jshort)lpStruct->tdExtDevmodeOffset);
	(*env)->SetByteField(env, lpObject, DVTARGETDEVICEFc.tdData, (jbyte)*lpStruct->tdData);
}
#endif /* NO_DVTARGETDEVICE */

#ifndef NO_EXCEPINFO
typedef struct EXCEPINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID wCode, wReserved, bstrSource, bstrDescription, bstrHelpFile, dwHelpContext, pvReserved, pfnDeferredFillIn, scode;
} EXCEPINFO_FID_CACHE;

EXCEPINFO_FID_CACHE EXCEPINFOFc;

void cacheEXCEPINFOFids(JNIEnv *env, jobject lpObject)
{
	if (EXCEPINFOFc.cached) return;
	EXCEPINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	EXCEPINFOFc.wCode = (*env)->GetFieldID(env, EXCEPINFOFc.clazz, "wCode", "S");
	EXCEPINFOFc.wReserved = (*env)->GetFieldID(env, EXCEPINFOFc.clazz, "wReserved", "S");
	EXCEPINFOFc.bstrSource = (*env)->GetFieldID(env, EXCEPINFOFc.clazz, "bstrSource", "I");
	EXCEPINFOFc.bstrDescription = (*env)->GetFieldID(env, EXCEPINFOFc.clazz, "bstrDescription", "I");
	EXCEPINFOFc.bstrHelpFile = (*env)->GetFieldID(env, EXCEPINFOFc.clazz, "bstrHelpFile", "I");
	EXCEPINFOFc.dwHelpContext = (*env)->GetFieldID(env, EXCEPINFOFc.clazz, "dwHelpContext", "I");
	EXCEPINFOFc.pvReserved = (*env)->GetFieldID(env, EXCEPINFOFc.clazz, "pvReserved", "I");
	EXCEPINFOFc.pfnDeferredFillIn = (*env)->GetFieldID(env, EXCEPINFOFc.clazz, "pfnDeferredFillIn", "I");
	EXCEPINFOFc.scode = (*env)->GetFieldID(env, EXCEPINFOFc.clazz, "scode", "I");
	EXCEPINFOFc.cached = 1;
}

EXCEPINFO *getEXCEPINFOFields(JNIEnv *env, jobject lpObject, EXCEPINFO *lpStruct)
{
	if (!EXCEPINFOFc.cached) cacheEXCEPINFOFids(env, lpObject);
	lpStruct->wCode = (*env)->GetShortField(env, lpObject, EXCEPINFOFc.wCode);
	lpStruct->wReserved = (*env)->GetShortField(env, lpObject, EXCEPINFOFc.wReserved);
	lpStruct->bstrSource = (BSTR)(*env)->GetIntField(env, lpObject, EXCEPINFOFc.bstrSource);
	lpStruct->bstrDescription = (BSTR)(*env)->GetIntField(env, lpObject, EXCEPINFOFc.bstrDescription);
	lpStruct->bstrHelpFile = (BSTR)(*env)->GetIntField(env, lpObject, EXCEPINFOFc.bstrHelpFile);
	lpStruct->dwHelpContext = (*env)->GetIntField(env, lpObject, EXCEPINFOFc.dwHelpContext);
	lpStruct->pvReserved = (void FAR *)(*env)->GetIntField(env, lpObject, EXCEPINFOFc.pvReserved);
	lpStruct->pfnDeferredFillIn = (HRESULT (STDAPICALLTYPE FAR* )(struct tagEXCEPINFO FAR*))(*env)->GetIntField(env, lpObject, EXCEPINFOFc.pfnDeferredFillIn);
	lpStruct->scode = (*env)->GetIntField(env, lpObject, EXCEPINFOFc.scode);
	return lpStruct;
}

void setEXCEPINFOFields(JNIEnv *env, jobject lpObject, EXCEPINFO *lpStruct)
{
	if (!EXCEPINFOFc.cached) cacheEXCEPINFOFids(env, lpObject);
	(*env)->SetShortField(env, lpObject, EXCEPINFOFc.wCode, (jshort)lpStruct->wCode);
	(*env)->SetShortField(env, lpObject, EXCEPINFOFc.wReserved, (jshort)lpStruct->wReserved);
	(*env)->SetIntField(env, lpObject, EXCEPINFOFc.bstrSource, (jint)lpStruct->bstrSource);
	(*env)->SetIntField(env, lpObject, EXCEPINFOFc.bstrDescription, (jint)lpStruct->bstrDescription);
	(*env)->SetIntField(env, lpObject, EXCEPINFOFc.bstrHelpFile, (jint)lpStruct->bstrHelpFile);
	(*env)->SetIntField(env, lpObject, EXCEPINFOFc.dwHelpContext, (jint)lpStruct->dwHelpContext);
	(*env)->SetIntField(env, lpObject, EXCEPINFOFc.pvReserved, (jint)lpStruct->pvReserved);
	(*env)->SetIntField(env, lpObject, EXCEPINFOFc.pfnDeferredFillIn, (jint)lpStruct->pfnDeferredFillIn);
	(*env)->SetIntField(env, lpObject, EXCEPINFOFc.scode, (jint)lpStruct->scode);
}
#endif /* NO_EXCEPINFO */

#ifndef NO_FORMATETC
typedef struct FORMATETC_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cfFormat, ptd, dwAspect, lindex, tymed;
} FORMATETC_FID_CACHE;

FORMATETC_FID_CACHE FORMATETCFc;

void cacheFORMATETCFids(JNIEnv *env, jobject lpObject)
{
	if (FORMATETCFc.cached) return;
	FORMATETCFc.clazz = (*env)->GetObjectClass(env, lpObject);
	FORMATETCFc.cfFormat = (*env)->GetFieldID(env, FORMATETCFc.clazz, "cfFormat", "I");
	FORMATETCFc.ptd = (*env)->GetFieldID(env, FORMATETCFc.clazz, "ptd", "I");
	FORMATETCFc.dwAspect = (*env)->GetFieldID(env, FORMATETCFc.clazz, "dwAspect", "I");
	FORMATETCFc.lindex = (*env)->GetFieldID(env, FORMATETCFc.clazz, "lindex", "I");
	FORMATETCFc.tymed = (*env)->GetFieldID(env, FORMATETCFc.clazz, "tymed", "I");
	FORMATETCFc.cached = 1;
}

FORMATETC *getFORMATETCFields(JNIEnv *env, jobject lpObject, FORMATETC *lpStruct)
{
	if (!FORMATETCFc.cached) cacheFORMATETCFids(env, lpObject);
	lpStruct->cfFormat = (CLIPFORMAT)(*env)->GetIntField(env, lpObject, FORMATETCFc.cfFormat);
	lpStruct->ptd = (DVTARGETDEVICE *)(*env)->GetIntField(env, lpObject, FORMATETCFc.ptd);
	lpStruct->dwAspect = (*env)->GetIntField(env, lpObject, FORMATETCFc.dwAspect);
	lpStruct->lindex = (*env)->GetIntField(env, lpObject, FORMATETCFc.lindex);
	lpStruct->tymed = (*env)->GetIntField(env, lpObject, FORMATETCFc.tymed);
	return lpStruct;
}

void setFORMATETCFields(JNIEnv *env, jobject lpObject, FORMATETC *lpStruct)
{
	if (!FORMATETCFc.cached) cacheFORMATETCFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, FORMATETCFc.cfFormat, (jint)lpStruct->cfFormat);
	(*env)->SetIntField(env, lpObject, FORMATETCFc.ptd, (jint)lpStruct->ptd);
	(*env)->SetIntField(env, lpObject, FORMATETCFc.dwAspect, (jint)lpStruct->dwAspect);
	(*env)->SetIntField(env, lpObject, FORMATETCFc.lindex, (jint)lpStruct->lindex);
	(*env)->SetIntField(env, lpObject, FORMATETCFc.tymed, (jint)lpStruct->tymed);
}
#endif /* NO_FORMATETC */

#ifndef NO_FUNCDESC1
typedef struct FUNCDESC1_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID memid, lprgscode, lprgelemdescParam, funckind, invkind, callconv, cParams, cParamsOpt, oVft, cScodes, elemdescFunc_tdesc_union, elemdescFunc_tdesc_vt, elemdescFunc_paramdesc_pparamdescex, elemdescFunc_paramdesc_wParamFlags, wFuncFlags;
} FUNCDESC1_FID_CACHE;

FUNCDESC1_FID_CACHE FUNCDESC1Fc;

void cacheFUNCDESC1Fids(JNIEnv *env, jobject lpObject)
{
	if (FUNCDESC1Fc.cached) return;
	FUNCDESC1Fc.clazz = (*env)->GetObjectClass(env, lpObject);
	FUNCDESC1Fc.memid = (*env)->GetFieldID(env, FUNCDESC1Fc.clazz, "memid", "I");
	FUNCDESC1Fc.lprgscode = (*env)->GetFieldID(env, FUNCDESC1Fc.clazz, "lprgscode", "I");
	FUNCDESC1Fc.lprgelemdescParam = (*env)->GetFieldID(env, FUNCDESC1Fc.clazz, "lprgelemdescParam", "I");
	FUNCDESC1Fc.funckind = (*env)->GetFieldID(env, FUNCDESC1Fc.clazz, "funckind", "I");
	FUNCDESC1Fc.invkind = (*env)->GetFieldID(env, FUNCDESC1Fc.clazz, "invkind", "I");
	FUNCDESC1Fc.callconv = (*env)->GetFieldID(env, FUNCDESC1Fc.clazz, "callconv", "I");
	FUNCDESC1Fc.cParams = (*env)->GetFieldID(env, FUNCDESC1Fc.clazz, "cParams", "S");
	FUNCDESC1Fc.cParamsOpt = (*env)->GetFieldID(env, FUNCDESC1Fc.clazz, "cParamsOpt", "S");
	FUNCDESC1Fc.oVft = (*env)->GetFieldID(env, FUNCDESC1Fc.clazz, "oVft", "S");
	FUNCDESC1Fc.cScodes = (*env)->GetFieldID(env, FUNCDESC1Fc.clazz, "cScodes", "S");
	FUNCDESC1Fc.elemdescFunc_tdesc_union = (*env)->GetFieldID(env, FUNCDESC1Fc.clazz, "elemdescFunc_tdesc_union", "I");
	FUNCDESC1Fc.elemdescFunc_tdesc_vt = (*env)->GetFieldID(env, FUNCDESC1Fc.clazz, "elemdescFunc_tdesc_vt", "S");
	FUNCDESC1Fc.elemdescFunc_paramdesc_pparamdescex = (*env)->GetFieldID(env, FUNCDESC1Fc.clazz, "elemdescFunc_paramdesc_pparamdescex", "I");
	FUNCDESC1Fc.elemdescFunc_paramdesc_wParamFlags = (*env)->GetFieldID(env, FUNCDESC1Fc.clazz, "elemdescFunc_paramdesc_wParamFlags", "S");
	FUNCDESC1Fc.wFuncFlags = (*env)->GetFieldID(env, FUNCDESC1Fc.clazz, "wFuncFlags", "S");
	FUNCDESC1Fc.cached = 1;
}

FUNCDESC *getFUNCDESC1Fields(JNIEnv *env, jobject lpObject, FUNCDESC *lpStruct)
{
	if (!FUNCDESC1Fc.cached) cacheFUNCDESC1Fids(env, lpObject);
	lpStruct->memid = (*env)->GetIntField(env, lpObject, FUNCDESC1Fc.memid);
	lpStruct->lprgscode = (SCODE FAR *)(*env)->GetIntField(env, lpObject, FUNCDESC1Fc.lprgscode);
	lpStruct->lprgelemdescParam = (ELEMDESC FAR *)(*env)->GetIntField(env, lpObject, FUNCDESC1Fc.lprgelemdescParam);
	lpStruct->funckind = (*env)->GetIntField(env, lpObject, FUNCDESC1Fc.funckind);
	lpStruct->invkind = (*env)->GetIntField(env, lpObject, FUNCDESC1Fc.invkind);
	lpStruct->callconv = (*env)->GetIntField(env, lpObject, FUNCDESC1Fc.callconv);
	lpStruct->cParams = (*env)->GetShortField(env, lpObject, FUNCDESC1Fc.cParams);
	lpStruct->cParamsOpt = (*env)->GetShortField(env, lpObject, FUNCDESC1Fc.cParamsOpt);
	lpStruct->oVft = (*env)->GetShortField(env, lpObject, FUNCDESC1Fc.oVft);
	lpStruct->cScodes = (*env)->GetShortField(env, lpObject, FUNCDESC1Fc.cScodes);
	lpStruct->elemdescFunc.tdesc.lptdesc = (struct FARSTRUCT tagTYPEDESC FAR* )(*env)->GetIntField(env, lpObject, FUNCDESC1Fc.elemdescFunc_tdesc_union);
	lpStruct->elemdescFunc.tdesc.vt = (*env)->GetShortField(env, lpObject, FUNCDESC1Fc.elemdescFunc_tdesc_vt);
	lpStruct->elemdescFunc.paramdesc.pparamdescex = (LPPARAMDESCEX)(*env)->GetIntField(env, lpObject, FUNCDESC1Fc.elemdescFunc_paramdesc_pparamdescex);
	lpStruct->elemdescFunc.paramdesc.wParamFlags = (*env)->GetShortField(env, lpObject, FUNCDESC1Fc.elemdescFunc_paramdesc_wParamFlags);
	lpStruct->wFuncFlags = (*env)->GetShortField(env, lpObject, FUNCDESC1Fc.wFuncFlags);
	return lpStruct;
}

void setFUNCDESC1Fields(JNIEnv *env, jobject lpObject, FUNCDESC *lpStruct)
{
	if (!FUNCDESC1Fc.cached) cacheFUNCDESC1Fids(env, lpObject);
	(*env)->SetIntField(env, lpObject, FUNCDESC1Fc.memid, (jint)lpStruct->memid);
	(*env)->SetIntField(env, lpObject, FUNCDESC1Fc.lprgscode, (jint)lpStruct->lprgscode);
	(*env)->SetIntField(env, lpObject, FUNCDESC1Fc.lprgelemdescParam, (jint)lpStruct->lprgelemdescParam);
	(*env)->SetIntField(env, lpObject, FUNCDESC1Fc.funckind, (jint)lpStruct->funckind);
	(*env)->SetIntField(env, lpObject, FUNCDESC1Fc.invkind, (jint)lpStruct->invkind);
	(*env)->SetIntField(env, lpObject, FUNCDESC1Fc.callconv, (jint)lpStruct->callconv);
	(*env)->SetShortField(env, lpObject, FUNCDESC1Fc.cParams, (jshort)lpStruct->cParams);
	(*env)->SetShortField(env, lpObject, FUNCDESC1Fc.cParamsOpt, (jshort)lpStruct->cParamsOpt);
	(*env)->SetShortField(env, lpObject, FUNCDESC1Fc.oVft, (jshort)lpStruct->oVft);
	(*env)->SetShortField(env, lpObject, FUNCDESC1Fc.cScodes, (jshort)lpStruct->cScodes);
	(*env)->SetIntField(env, lpObject, FUNCDESC1Fc.elemdescFunc_tdesc_union, (jint)lpStruct->elemdescFunc.tdesc.lptdesc);
	(*env)->SetShortField(env, lpObject, FUNCDESC1Fc.elemdescFunc_tdesc_vt, (jshort)lpStruct->elemdescFunc.tdesc.vt);
	(*env)->SetIntField(env, lpObject, FUNCDESC1Fc.elemdescFunc_paramdesc_pparamdescex, (jint)lpStruct->elemdescFunc.paramdesc.pparamdescex);
	(*env)->SetShortField(env, lpObject, FUNCDESC1Fc.elemdescFunc_paramdesc_wParamFlags, (jshort)lpStruct->elemdescFunc.paramdesc.wParamFlags);
	(*env)->SetShortField(env, lpObject, FUNCDESC1Fc.wFuncFlags, (jshort)lpStruct->wFuncFlags);
}
#endif /* NO_FUNCDESC1 */

#ifndef NO_FUNCDESC2
typedef struct FUNCDESC2_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID memid, lprgscode, lprgelemdescParam, funckind, invkind, callconv, cParams, cParamsOpt, oVft, cScodes, elemdescFunc_tdesc_union, elemdescFunc_tdesc_vt, elemdescFunc_idldesc_dwReserved, elemdescFunc_idldesc_wIDLFlags, wFuncFlags;
} FUNCDESC2_FID_CACHE;

FUNCDESC2_FID_CACHE FUNCDESC2Fc;

void cacheFUNCDESC2Fids(JNIEnv *env, jobject lpObject)
{
	if (FUNCDESC2Fc.cached) return;
	FUNCDESC2Fc.clazz = (*env)->GetObjectClass(env, lpObject);
	FUNCDESC2Fc.memid = (*env)->GetFieldID(env, FUNCDESC2Fc.clazz, "memid", "I");
	FUNCDESC2Fc.lprgscode = (*env)->GetFieldID(env, FUNCDESC2Fc.clazz, "lprgscode", "I");
	FUNCDESC2Fc.lprgelemdescParam = (*env)->GetFieldID(env, FUNCDESC2Fc.clazz, "lprgelemdescParam", "I");
	FUNCDESC2Fc.funckind = (*env)->GetFieldID(env, FUNCDESC2Fc.clazz, "funckind", "I");
	FUNCDESC2Fc.invkind = (*env)->GetFieldID(env, FUNCDESC2Fc.clazz, "invkind", "I");
	FUNCDESC2Fc.callconv = (*env)->GetFieldID(env, FUNCDESC2Fc.clazz, "callconv", "I");
	FUNCDESC2Fc.cParams = (*env)->GetFieldID(env, FUNCDESC2Fc.clazz, "cParams", "S");
	FUNCDESC2Fc.cParamsOpt = (*env)->GetFieldID(env, FUNCDESC2Fc.clazz, "cParamsOpt", "S");
	FUNCDESC2Fc.oVft = (*env)->GetFieldID(env, FUNCDESC2Fc.clazz, "oVft", "S");
	FUNCDESC2Fc.cScodes = (*env)->GetFieldID(env, FUNCDESC2Fc.clazz, "cScodes", "S");
	FUNCDESC2Fc.elemdescFunc_tdesc_union = (*env)->GetFieldID(env, FUNCDESC2Fc.clazz, "elemdescFunc_tdesc_union", "I");
	FUNCDESC2Fc.elemdescFunc_tdesc_vt = (*env)->GetFieldID(env, FUNCDESC2Fc.clazz, "elemdescFunc_tdesc_vt", "S");
	FUNCDESC2Fc.elemdescFunc_idldesc_dwReserved = (*env)->GetFieldID(env, FUNCDESC2Fc.clazz, "elemdescFunc_idldesc_dwReserved", "I");
	FUNCDESC2Fc.elemdescFunc_idldesc_wIDLFlags = (*env)->GetFieldID(env, FUNCDESC2Fc.clazz, "elemdescFunc_idldesc_wIDLFlags", "S");
	FUNCDESC2Fc.wFuncFlags = (*env)->GetFieldID(env, FUNCDESC2Fc.clazz, "wFuncFlags", "S");
	FUNCDESC2Fc.cached = 1;
}

FUNCDESC *getFUNCDESC2Fields(JNIEnv *env, jobject lpObject, FUNCDESC *lpStruct)
{
	if (!FUNCDESC2Fc.cached) cacheFUNCDESC2Fids(env, lpObject);
	lpStruct->memid = (*env)->GetIntField(env, lpObject, FUNCDESC2Fc.memid);
	lpStruct->lprgscode = (SCODE FAR *)(*env)->GetIntField(env, lpObject, FUNCDESC2Fc.lprgscode);
	lpStruct->lprgelemdescParam = (ELEMDESC FAR *)(*env)->GetIntField(env, lpObject, FUNCDESC2Fc.lprgelemdescParam);
	lpStruct->funckind = (*env)->GetIntField(env, lpObject, FUNCDESC2Fc.funckind);
	lpStruct->invkind = (*env)->GetIntField(env, lpObject, FUNCDESC2Fc.invkind);
	lpStruct->callconv = (*env)->GetIntField(env, lpObject, FUNCDESC2Fc.callconv);
	lpStruct->cParams = (*env)->GetShortField(env, lpObject, FUNCDESC2Fc.cParams);
	lpStruct->cParamsOpt = (*env)->GetShortField(env, lpObject, FUNCDESC2Fc.cParamsOpt);
	lpStruct->oVft = (*env)->GetShortField(env, lpObject, FUNCDESC2Fc.oVft);
	lpStruct->cScodes = (*env)->GetShortField(env, lpObject, FUNCDESC2Fc.cScodes);
	lpStruct->elemdescFunc.tdesc.lptdesc = (struct FARSTRUCT tagTYPEDESC FAR* )(*env)->GetIntField(env, lpObject, FUNCDESC2Fc.elemdescFunc_tdesc_union);
	lpStruct->elemdescFunc.tdesc.vt = (*env)->GetShortField(env, lpObject, FUNCDESC2Fc.elemdescFunc_tdesc_vt);
	lpStruct->elemdescFunc.idldesc.dwReserved = (*env)->GetIntField(env, lpObject, FUNCDESC2Fc.elemdescFunc_idldesc_dwReserved);
	lpStruct->elemdescFunc.idldesc.wIDLFlags = (*env)->GetShortField(env, lpObject, FUNCDESC2Fc.elemdescFunc_idldesc_wIDLFlags);
	lpStruct->wFuncFlags = (*env)->GetShortField(env, lpObject, FUNCDESC2Fc.wFuncFlags);
	return lpStruct;
}

void setFUNCDESC2Fields(JNIEnv *env, jobject lpObject, FUNCDESC *lpStruct)
{
	if (!FUNCDESC2Fc.cached) cacheFUNCDESC2Fids(env, lpObject);
	(*env)->SetIntField(env, lpObject, FUNCDESC2Fc.memid, (jint)lpStruct->memid);
	(*env)->SetIntField(env, lpObject, FUNCDESC2Fc.lprgscode, (jint)lpStruct->lprgscode);
	(*env)->SetIntField(env, lpObject, FUNCDESC2Fc.lprgelemdescParam, (jint)lpStruct->lprgelemdescParam);
	(*env)->SetIntField(env, lpObject, FUNCDESC2Fc.funckind, (jint)lpStruct->funckind);
	(*env)->SetIntField(env, lpObject, FUNCDESC2Fc.invkind, (jint)lpStruct->invkind);
	(*env)->SetIntField(env, lpObject, FUNCDESC2Fc.callconv, (jint)lpStruct->callconv);
	(*env)->SetShortField(env, lpObject, FUNCDESC2Fc.cParams, (jshort)lpStruct->cParams);
	(*env)->SetShortField(env, lpObject, FUNCDESC2Fc.cParamsOpt, (jshort)lpStruct->cParamsOpt);
	(*env)->SetShortField(env, lpObject, FUNCDESC2Fc.oVft, (jshort)lpStruct->oVft);
	(*env)->SetShortField(env, lpObject, FUNCDESC2Fc.cScodes, (jshort)lpStruct->cScodes);
	(*env)->SetIntField(env, lpObject, FUNCDESC2Fc.elemdescFunc_tdesc_union, (jint)lpStruct->elemdescFunc.tdesc.lptdesc);
	(*env)->SetShortField(env, lpObject, FUNCDESC2Fc.elemdescFunc_tdesc_vt, (jshort)lpStruct->elemdescFunc.tdesc.vt);
	(*env)->SetIntField(env, lpObject, FUNCDESC2Fc.elemdescFunc_idldesc_dwReserved, (jint)lpStruct->elemdescFunc.idldesc.dwReserved);
	(*env)->SetShortField(env, lpObject, FUNCDESC2Fc.elemdescFunc_idldesc_wIDLFlags, (jshort)lpStruct->elemdescFunc.idldesc.wIDLFlags);
	(*env)->SetShortField(env, lpObject, FUNCDESC2Fc.wFuncFlags, (jshort)lpStruct->wFuncFlags);
}
#endif /* NO_FUNCDESC2 */

#ifndef NO_GUID
typedef struct GUID_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID data1, data2, data3, b0, b1, b2, b3, b4, b5, b6, b7;
} GUID_FID_CACHE;

GUID_FID_CACHE GUIDFc;

void cacheGUIDFids(JNIEnv *env, jobject lpObject)
{
	if (GUIDFc.cached) return;
	GUIDFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GUIDFc.data1 = (*env)->GetFieldID(env, GUIDFc.clazz, "data1", "I");
	GUIDFc.data2 = (*env)->GetFieldID(env, GUIDFc.clazz, "data2", "S");
	GUIDFc.data3 = (*env)->GetFieldID(env, GUIDFc.clazz, "data3", "S");
	GUIDFc.b0 = (*env)->GetFieldID(env, GUIDFc.clazz, "b0", "B");
	GUIDFc.b1 = (*env)->GetFieldID(env, GUIDFc.clazz, "b1", "B");
	GUIDFc.b2 = (*env)->GetFieldID(env, GUIDFc.clazz, "b2", "B");
	GUIDFc.b3 = (*env)->GetFieldID(env, GUIDFc.clazz, "b3", "B");
	GUIDFc.b4 = (*env)->GetFieldID(env, GUIDFc.clazz, "b4", "B");
	GUIDFc.b5 = (*env)->GetFieldID(env, GUIDFc.clazz, "b5", "B");
	GUIDFc.b6 = (*env)->GetFieldID(env, GUIDFc.clazz, "b6", "B");
	GUIDFc.b7 = (*env)->GetFieldID(env, GUIDFc.clazz, "b7", "B");
	GUIDFc.cached = 1;
}

GUID *getGUIDFields(JNIEnv *env, jobject lpObject, GUID *lpStruct)
{
	if (!GUIDFc.cached) cacheGUIDFids(env, lpObject);
	lpStruct->Data4[7] = (*env)->GetByteField(env, lpObject, GUIDFc.b7);
	lpStruct->Data4[6] = (*env)->GetByteField(env, lpObject, GUIDFc.b6);
	lpStruct->Data4[5] = (*env)->GetByteField(env, lpObject, GUIDFc.b5);
	lpStruct->Data4[4] = (*env)->GetByteField(env, lpObject, GUIDFc.b4);
	lpStruct->Data4[3] = (*env)->GetByteField(env, lpObject, GUIDFc.b3);
	lpStruct->Data4[2] = (*env)->GetByteField(env, lpObject, GUIDFc.b2);
	lpStruct->Data4[1] = (*env)->GetByteField(env, lpObject, GUIDFc.b1);
	lpStruct->Data4[0] = (*env)->GetByteField(env, lpObject, GUIDFc.b0);
	lpStruct->Data3 = (*env)->GetShortField(env, lpObject, GUIDFc.data3);
	lpStruct->Data2 = (*env)->GetShortField(env, lpObject, GUIDFc.data2);
	lpStruct->Data1 = (*env)->GetIntField(env, lpObject, GUIDFc.data1);
	return lpStruct;
}

void setGUIDFields(JNIEnv *env, jobject lpObject, GUID *lpStruct)
{
	if (!GUIDFc.cached) cacheGUIDFids(env, lpObject);
	(*env)->SetByteField(env, lpObject, GUIDFc.b7, lpStruct->Data4[7]);
	(*env)->SetByteField(env, lpObject, GUIDFc.b6, lpStruct->Data4[6]);
	(*env)->SetByteField(env, lpObject, GUIDFc.b5, lpStruct->Data4[5]);
	(*env)->SetByteField(env, lpObject, GUIDFc.b4, lpStruct->Data4[4]);
	(*env)->SetByteField(env, lpObject, GUIDFc.b3, lpStruct->Data4[3]);
	(*env)->SetByteField(env, lpObject, GUIDFc.b2, lpStruct->Data4[2]);
	(*env)->SetByteField(env, lpObject, GUIDFc.b1, lpStruct->Data4[1]);
	(*env)->SetByteField(env, lpObject, GUIDFc.b0, lpStruct->Data4[0]);
	(*env)->SetShortField(env, lpObject, GUIDFc.data3, lpStruct->Data3);
	(*env)->SetShortField(env, lpObject, GUIDFc.data2, lpStruct->Data2);
	(*env)->SetIntField(env, lpObject, GUIDFc.data1, lpStruct->Data1);
}

#endif /* NO_GUID */

#ifndef NO_LICINFO
typedef struct LICINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbLicInfo, fRuntimeKeyAvail, fLicVerified;
} LICINFO_FID_CACHE;

LICINFO_FID_CACHE LICINFOFc;

void cacheLICINFOFids(JNIEnv *env, jobject lpObject)
{
	if (LICINFOFc.cached) return;
	LICINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	LICINFOFc.cbLicInfo = (*env)->GetFieldID(env, LICINFOFc.clazz, "cbLicInfo", "I");
	LICINFOFc.fRuntimeKeyAvail = (*env)->GetFieldID(env, LICINFOFc.clazz, "fRuntimeKeyAvail", "I");
	LICINFOFc.fLicVerified = (*env)->GetFieldID(env, LICINFOFc.clazz, "fLicVerified", "I");
	LICINFOFc.cached = 1;
}

LICINFO *getLICINFOFields(JNIEnv *env, jobject lpObject, LICINFO *lpStruct)
{
	if (!LICINFOFc.cached) cacheLICINFOFids(env, lpObject);
	lpStruct->cbLicInfo = (*env)->GetIntField(env, lpObject, LICINFOFc.cbLicInfo);
	lpStruct->fRuntimeKeyAvail = (*env)->GetIntField(env, lpObject, LICINFOFc.fRuntimeKeyAvail);
	lpStruct->fLicVerified = (*env)->GetIntField(env, lpObject, LICINFOFc.fLicVerified);
	return lpStruct;
}

void setLICINFOFields(JNIEnv *env, jobject lpObject, LICINFO *lpStruct)
{
	if (!LICINFOFc.cached) cacheLICINFOFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, LICINFOFc.cbLicInfo, (jint)lpStruct->cbLicInfo);
	(*env)->SetIntField(env, lpObject, LICINFOFc.fRuntimeKeyAvail, (jint)lpStruct->fRuntimeKeyAvail);
	(*env)->SetIntField(env, lpObject, LICINFOFc.fLicVerified, (jint)lpStruct->fLicVerified);
}
#endif /* NO_LICINFO */

#ifndef NO_OLECMD
typedef struct OLECMD_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cmdID, cmdf;
} OLECMD_FID_CACHE;

OLECMD_FID_CACHE OLECMDFc;

void cacheOLECMDFids(JNIEnv *env, jobject lpObject)
{
	if (OLECMDFc.cached) return;
	OLECMDFc.clazz = (*env)->GetObjectClass(env, lpObject);
	OLECMDFc.cmdID = (*env)->GetFieldID(env, OLECMDFc.clazz, "cmdID", "I");
	OLECMDFc.cmdf = (*env)->GetFieldID(env, OLECMDFc.clazz, "cmdf", "I");
	OLECMDFc.cached = 1;
}

OLECMD *getOLECMDFields(JNIEnv *env, jobject lpObject, OLECMD *lpStruct)
{
	if (!OLECMDFc.cached) cacheOLECMDFids(env, lpObject);
	lpStruct->cmdID = (*env)->GetIntField(env, lpObject, OLECMDFc.cmdID);
	lpStruct->cmdf = (*env)->GetIntField(env, lpObject, OLECMDFc.cmdf);
	return lpStruct;
}

void setOLECMDFields(JNIEnv *env, jobject lpObject, OLECMD *lpStruct)
{
	if (!OLECMDFc.cached) cacheOLECMDFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, OLECMDFc.cmdID, (jint)lpStruct->cmdID);
	(*env)->SetIntField(env, lpObject, OLECMDFc.cmdf, (jint)lpStruct->cmdf);
}
#endif /* NO_OLECMD */

#ifndef NO_OLECMDTEXT
typedef struct OLECMDTEXT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cmdtextf, cwActual, cwBuf, rgwz;
} OLECMDTEXT_FID_CACHE;

OLECMDTEXT_FID_CACHE OLECMDTEXTFc;

void cacheOLECMDTEXTFids(JNIEnv *env, jobject lpObject)
{
	if (OLECMDTEXTFc.cached) return;
	OLECMDTEXTFc.clazz = (*env)->GetObjectClass(env, lpObject);
	OLECMDTEXTFc.cmdtextf = (*env)->GetFieldID(env, OLECMDTEXTFc.clazz, "cmdtextf", "I");
	OLECMDTEXTFc.cwActual = (*env)->GetFieldID(env, OLECMDTEXTFc.clazz, "cwActual", "I");
	OLECMDTEXTFc.cwBuf = (*env)->GetFieldID(env, OLECMDTEXTFc.clazz, "cwBuf", "I");
	OLECMDTEXTFc.rgwz = (*env)->GetFieldID(env, OLECMDTEXTFc.clazz, "rgwz", "S");
	OLECMDTEXTFc.cached = 1;
}

OLECMDTEXT *getOLECMDTEXTFields(JNIEnv *env, jobject lpObject, OLECMDTEXT *lpStruct)
{
	if (!OLECMDTEXTFc.cached) cacheOLECMDTEXTFids(env, lpObject);
	lpStruct->cmdtextf = (*env)->GetIntField(env, lpObject, OLECMDTEXTFc.cmdtextf);
	lpStruct->cwActual = (*env)->GetIntField(env, lpObject, OLECMDTEXTFc.cwActual);
	lpStruct->cwBuf = (*env)->GetIntField(env, lpObject, OLECMDTEXTFc.cwBuf);
	lpStruct->rgwz[0] = (*env)->GetShortField(env, lpObject, OLECMDTEXTFc.rgwz); /* SPECIAL */
	return lpStruct;
}

void setOLECMDTEXTFields(JNIEnv *env, jobject lpObject, OLECMDTEXT *lpStruct)
{
	if (!OLECMDTEXTFc.cached) cacheOLECMDTEXTFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, OLECMDTEXTFc.cmdtextf, (jint)lpStruct->cmdtextf);
	(*env)->SetIntField(env, lpObject, OLECMDTEXTFc.cwActual, (jint)lpStruct->cwActual);
	(*env)->SetIntField(env, lpObject, OLECMDTEXTFc.cwBuf, (jint)lpStruct->cwBuf);
	(*env)->SetShortField(env, lpObject, OLECMDTEXTFc.rgwz, (jshort)lpStruct->rgwz[0]); /* SPECIAL */
}
#endif /* NO_OLECMDTEXT */

#ifndef NO_OLEINPLACEFRAMEINFO
typedef struct OLEINPLACEFRAMEINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cb, fMDIApp, hwndFrame, haccel, cAccelEntries;
} OLEINPLACEFRAMEINFO_FID_CACHE;

OLEINPLACEFRAMEINFO_FID_CACHE OLEINPLACEFRAMEINFOFc;

void cacheOLEINPLACEFRAMEINFOFids(JNIEnv *env, jobject lpObject)
{
	if (OLEINPLACEFRAMEINFOFc.cached) return;
	OLEINPLACEFRAMEINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	OLEINPLACEFRAMEINFOFc.cb = (*env)->GetFieldID(env, OLEINPLACEFRAMEINFOFc.clazz, "cb", "I");
	OLEINPLACEFRAMEINFOFc.fMDIApp = (*env)->GetFieldID(env, OLEINPLACEFRAMEINFOFc.clazz, "fMDIApp", "I");
	OLEINPLACEFRAMEINFOFc.hwndFrame = (*env)->GetFieldID(env, OLEINPLACEFRAMEINFOFc.clazz, "hwndFrame", "I");
	OLEINPLACEFRAMEINFOFc.haccel = (*env)->GetFieldID(env, OLEINPLACEFRAMEINFOFc.clazz, "haccel", "I");
	OLEINPLACEFRAMEINFOFc.cAccelEntries = (*env)->GetFieldID(env, OLEINPLACEFRAMEINFOFc.clazz, "cAccelEntries", "I");
	OLEINPLACEFRAMEINFOFc.cached = 1;
}

OLEINPLACEFRAMEINFO *getOLEINPLACEFRAMEINFOFields(JNIEnv *env, jobject lpObject, OLEINPLACEFRAMEINFO *lpStruct)
{
	if (!OLEINPLACEFRAMEINFOFc.cached) cacheOLEINPLACEFRAMEINFOFids(env, lpObject);
	lpStruct->cb = (*env)->GetIntField(env, lpObject, OLEINPLACEFRAMEINFOFc.cb);
	lpStruct->fMDIApp = (*env)->GetIntField(env, lpObject, OLEINPLACEFRAMEINFOFc.fMDIApp);
	lpStruct->hwndFrame = (HWND)(*env)->GetIntField(env, lpObject, OLEINPLACEFRAMEINFOFc.hwndFrame);
	lpStruct->haccel = (HACCEL)(*env)->GetIntField(env, lpObject, OLEINPLACEFRAMEINFOFc.haccel);
	lpStruct->cAccelEntries = (*env)->GetIntField(env, lpObject, OLEINPLACEFRAMEINFOFc.cAccelEntries);
	return lpStruct;
}

void setOLEINPLACEFRAMEINFOFields(JNIEnv *env, jobject lpObject, OLEINPLACEFRAMEINFO *lpStruct)
{
	if (!OLEINPLACEFRAMEINFOFc.cached) cacheOLEINPLACEFRAMEINFOFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, OLEINPLACEFRAMEINFOFc.cb, (jint)lpStruct->cb);
	(*env)->SetIntField(env, lpObject, OLEINPLACEFRAMEINFOFc.fMDIApp, (jint)lpStruct->fMDIApp);
	(*env)->SetIntField(env, lpObject, OLEINPLACEFRAMEINFOFc.hwndFrame, (jint)lpStruct->hwndFrame);
	(*env)->SetIntField(env, lpObject, OLEINPLACEFRAMEINFOFc.haccel, (jint)lpStruct->haccel);
	(*env)->SetIntField(env, lpObject, OLEINPLACEFRAMEINFOFc.cAccelEntries, (jint)lpStruct->cAccelEntries);
}
#endif /* NO_OLEINPLACEFRAMEINFO */

#ifndef NO_STATSTG
typedef struct STATSTG_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID pwcsName, type, cbSize, mtime_dwLowDateTime, mtime_dwHighDateTime, ctime_dwLowDateTime, ctime_dwHighDateTime, atime_dwLowDateTime, atime_dwHighDateTime, grfMode, grfLocksSupported, clsid_data1, clsid_data2, clsid_data3, clsid_b0, clsid_b1, clsid_b2, clsid_b3, clsid_b4, clsid_b5, clsid_b6, clsid_b7, grfStateBits, reserved;
} STATSTG_FID_CACHE;

STATSTG_FID_CACHE STATSTGFc;

void cacheSTATSTGFids(JNIEnv *env, jobject lpObject)
{
	if (STATSTGFc.cached) return;
	STATSTGFc.clazz = (*env)->GetObjectClass(env, lpObject);
	STATSTGFc.pwcsName = (*env)->GetFieldID(env, STATSTGFc.clazz, "pwcsName", "I");
	STATSTGFc.type = (*env)->GetFieldID(env, STATSTGFc.clazz, "type", "I");
	STATSTGFc.cbSize = (*env)->GetFieldID(env, STATSTGFc.clazz, "cbSize", "J");
	STATSTGFc.mtime_dwLowDateTime = (*env)->GetFieldID(env, STATSTGFc.clazz, "mtime_dwLowDateTime", "I");
	STATSTGFc.mtime_dwHighDateTime = (*env)->GetFieldID(env, STATSTGFc.clazz, "mtime_dwHighDateTime", "I");
	STATSTGFc.ctime_dwLowDateTime = (*env)->GetFieldID(env, STATSTGFc.clazz, "ctime_dwLowDateTime", "I");
	STATSTGFc.ctime_dwHighDateTime = (*env)->GetFieldID(env, STATSTGFc.clazz, "ctime_dwHighDateTime", "I");
	STATSTGFc.atime_dwLowDateTime = (*env)->GetFieldID(env, STATSTGFc.clazz, "atime_dwLowDateTime", "I");
	STATSTGFc.atime_dwHighDateTime = (*env)->GetFieldID(env, STATSTGFc.clazz, "atime_dwHighDateTime", "I");
	STATSTGFc.grfMode = (*env)->GetFieldID(env, STATSTGFc.clazz, "grfMode", "I");
	STATSTGFc.grfLocksSupported = (*env)->GetFieldID(env, STATSTGFc.clazz, "grfLocksSupported", "I");
	STATSTGFc.clsid_data1 = (*env)->GetFieldID(env, STATSTGFc.clazz, "clsid_data1", "I");
	STATSTGFc.clsid_data2 = (*env)->GetFieldID(env, STATSTGFc.clazz, "clsid_data2", "S");
	STATSTGFc.clsid_data3 = (*env)->GetFieldID(env, STATSTGFc.clazz, "clsid_data3", "S");
	STATSTGFc.clsid_b0 = (*env)->GetFieldID(env, STATSTGFc.clazz, "clsid_b0", "B");
	STATSTGFc.clsid_b1 = (*env)->GetFieldID(env, STATSTGFc.clazz, "clsid_b1", "B");
	STATSTGFc.clsid_b2 = (*env)->GetFieldID(env, STATSTGFc.clazz, "clsid_b2", "B");
	STATSTGFc.clsid_b3 = (*env)->GetFieldID(env, STATSTGFc.clazz, "clsid_b3", "B");
	STATSTGFc.clsid_b4 = (*env)->GetFieldID(env, STATSTGFc.clazz, "clsid_b4", "B");
	STATSTGFc.clsid_b5 = (*env)->GetFieldID(env, STATSTGFc.clazz, "clsid_b5", "B");
	STATSTGFc.clsid_b6 = (*env)->GetFieldID(env, STATSTGFc.clazz, "clsid_b6", "B");
	STATSTGFc.clsid_b7 = (*env)->GetFieldID(env, STATSTGFc.clazz, "clsid_b7", "B");
	STATSTGFc.grfStateBits = (*env)->GetFieldID(env, STATSTGFc.clazz, "grfStateBits", "I");
	STATSTGFc.reserved = (*env)->GetFieldID(env, STATSTGFc.clazz, "reserved", "I");
	STATSTGFc.cached = 1;
}

STATSTG *getSTATSTGFields(JNIEnv *env, jobject lpObject, STATSTG *lpStruct)
{
	if (!STATSTGFc.cached) cacheSTATSTGFids(env, lpObject);
	lpStruct->pwcsName = (LPWSTR)(*env)->GetIntField(env, lpObject, STATSTGFc.pwcsName);
	lpStruct->type = (*env)->GetIntField(env, lpObject, STATSTGFc.type);
	lpStruct->cbSize.QuadPart = (*env)->GetLongField(env, lpObject, STATSTGFc.cbSize);
	lpStruct->mtime.dwLowDateTime = (*env)->GetIntField(env, lpObject, STATSTGFc.mtime_dwLowDateTime);
	lpStruct->mtime.dwHighDateTime = (*env)->GetIntField(env, lpObject, STATSTGFc.mtime_dwHighDateTime);
	lpStruct->ctime.dwLowDateTime = (*env)->GetIntField(env, lpObject, STATSTGFc.ctime_dwLowDateTime);
	lpStruct->ctime.dwHighDateTime = (*env)->GetIntField(env, lpObject, STATSTGFc.ctime_dwHighDateTime);
	lpStruct->atime.dwLowDateTime = (*env)->GetIntField(env, lpObject, STATSTGFc.atime_dwLowDateTime);
	lpStruct->atime.dwHighDateTime = (*env)->GetIntField(env, lpObject, STATSTGFc.atime_dwHighDateTime);
	lpStruct->grfMode = (*env)->GetIntField(env, lpObject, STATSTGFc.grfMode);
	lpStruct->grfLocksSupported = (*env)->GetIntField(env, lpObject, STATSTGFc.grfLocksSupported);
	lpStruct->clsid.Data4[7] = (*env)->GetByteField(env, lpObject, STATSTGFc.clsid_b7);
	lpStruct->clsid.Data4[6] = (*env)->GetByteField(env, lpObject, STATSTGFc.clsid_b6);
	lpStruct->clsid.Data4[5] = (*env)->GetByteField(env, lpObject, STATSTGFc.clsid_b5);
	lpStruct->clsid.Data4[4] = (*env)->GetByteField(env, lpObject, STATSTGFc.clsid_b4);
	lpStruct->clsid.Data4[3] = (*env)->GetByteField(env, lpObject, STATSTGFc.clsid_b3);
	lpStruct->clsid.Data4[2] = (*env)->GetByteField(env, lpObject, STATSTGFc.clsid_b2);
	lpStruct->clsid.Data4[1] = (*env)->GetByteField(env, lpObject, STATSTGFc.clsid_b1);
	lpStruct->clsid.Data4[0] = (*env)->GetByteField(env, lpObject, STATSTGFc.clsid_b0);
	lpStruct->clsid.Data3 = (*env)->GetShortField(env, lpObject, STATSTGFc.clsid_data3);
	lpStruct->clsid.Data2 = (*env)->GetShortField(env, lpObject, STATSTGFc.clsid_data2);
	lpStruct->clsid.Data1 = (*env)->GetIntField(env, lpObject, STATSTGFc.clsid_data1);
	lpStruct->grfStateBits = (*env)->GetIntField(env, lpObject, STATSTGFc.grfStateBits);
	lpStruct->reserved = (*env)->GetIntField(env, lpObject, STATSTGFc.reserved);
	return lpStruct;
}

void setSTATSTGFields(JNIEnv *env, jobject lpObject, STATSTG *lpStruct)
{
	if (!STATSTGFc.cached) cacheSTATSTGFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, STATSTGFc.pwcsName, (jint)lpStruct->pwcsName);
	(*env)->SetIntField(env, lpObject, STATSTGFc.type, (jint)lpStruct->type);
	(*env)->SetLongField(env, lpObject, STATSTGFc.cbSize, (jlong)lpStruct->cbSize.QuadPart);
	(*env)->SetIntField(env, lpObject, STATSTGFc.mtime_dwLowDateTime, (jint)lpStruct->mtime.dwLowDateTime);
	(*env)->SetIntField(env, lpObject, STATSTGFc.mtime_dwHighDateTime, (jint)lpStruct->mtime.dwHighDateTime);
	(*env)->SetIntField(env, lpObject, STATSTGFc.ctime_dwLowDateTime, (jint)lpStruct->ctime.dwLowDateTime);
	(*env)->SetIntField(env, lpObject, STATSTGFc.ctime_dwHighDateTime, (jint)lpStruct->ctime.dwHighDateTime);
	(*env)->SetIntField(env, lpObject, STATSTGFc.atime_dwLowDateTime, (jint)lpStruct->atime.dwLowDateTime);
	(*env)->SetIntField(env, lpObject, STATSTGFc.atime_dwHighDateTime, (jint)lpStruct->atime.dwHighDateTime);
	(*env)->SetIntField(env, lpObject, STATSTGFc.grfMode, (jint)lpStruct->grfMode);
	(*env)->SetIntField(env, lpObject, STATSTGFc.grfLocksSupported, (jint)lpStruct->grfLocksSupported);
	(*env)->SetByteField(env, lpObject, STATSTGFc.clsid_b7, lpStruct->clsid.Data4[7]);
	(*env)->SetByteField(env, lpObject, STATSTGFc.clsid_b6, lpStruct->clsid.Data4[6]);
	(*env)->SetByteField(env, lpObject, STATSTGFc.clsid_b5, lpStruct->clsid.Data4[5]);
	(*env)->SetByteField(env, lpObject, STATSTGFc.clsid_b4, lpStruct->clsid.Data4[4]);
	(*env)->SetByteField(env, lpObject, STATSTGFc.clsid_b3, lpStruct->clsid.Data4[3]);
	(*env)->SetByteField(env, lpObject, STATSTGFc.clsid_b2, lpStruct->clsid.Data4[2]);
	(*env)->SetByteField(env, lpObject, STATSTGFc.clsid_b1, lpStruct->clsid.Data4[1]);
	(*env)->SetByteField(env, lpObject, STATSTGFc.clsid_b0, lpStruct->clsid.Data4[0]);
	(*env)->SetShortField(env, lpObject, STATSTGFc.clsid_data3, lpStruct->clsid.Data3);
	(*env)->SetShortField(env, lpObject, STATSTGFc.clsid_data2, lpStruct->clsid.Data2);
	(*env)->SetIntField(env, lpObject, STATSTGFc.clsid_data1, lpStruct->clsid.Data1);
	(*env)->SetIntField(env, lpObject, STATSTGFc.grfStateBits, (jint)lpStruct->grfStateBits);
	(*env)->SetIntField(env, lpObject, STATSTGFc.reserved, (jint)lpStruct->reserved);
}
#endif /* NO_STATSTG */

#ifndef NO_STGMEDIUM
typedef struct STGMEDIUM_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID tymed, unionField, pUnkForRelease;
} STGMEDIUM_FID_CACHE;

STGMEDIUM_FID_CACHE STGMEDIUMFc;

void cacheSTGMEDIUMFids(JNIEnv *env, jobject lpObject)
{
	if (STGMEDIUMFc.cached) return;
	STGMEDIUMFc.clazz = (*env)->GetObjectClass(env, lpObject);
	STGMEDIUMFc.tymed = (*env)->GetFieldID(env, STGMEDIUMFc.clazz, "tymed", "I");
	STGMEDIUMFc.unionField = (*env)->GetFieldID(env, STGMEDIUMFc.clazz, "unionField", "I");
	STGMEDIUMFc.pUnkForRelease = (*env)->GetFieldID(env, STGMEDIUMFc.clazz, "pUnkForRelease", "I");
	STGMEDIUMFc.cached = 1;
}

STGMEDIUM *getSTGMEDIUMFields(JNIEnv *env, jobject lpObject, STGMEDIUM *lpStruct)
{
	if (!STGMEDIUMFc.cached) cacheSTGMEDIUMFids(env, lpObject);
	lpStruct->tymed = (*env)->GetIntField(env, lpObject, STGMEDIUMFc.tymed);
	lpStruct->hGlobal = (HGLOBAL)(*env)->GetIntField(env, lpObject, STGMEDIUMFc.unionField);
	lpStruct->pUnkForRelease = (IUnknown *)(*env)->GetIntField(env, lpObject, STGMEDIUMFc.pUnkForRelease);
	return lpStruct;
}

void setSTGMEDIUMFields(JNIEnv *env, jobject lpObject, STGMEDIUM *lpStruct)
{
	if (!STGMEDIUMFc.cached) cacheSTGMEDIUMFids(env, lpObject);
	(*env)->SetIntField(env, lpObject, STGMEDIUMFc.tymed, (jint)lpStruct->tymed);
	(*env)->SetIntField(env, lpObject, STGMEDIUMFc.unionField, (jint)lpStruct->hGlobal);
	(*env)->SetIntField(env, lpObject, STGMEDIUMFc.pUnkForRelease, (jint)lpStruct->pUnkForRelease);
}
#endif /* NO_STGMEDIUM */

#ifndef NO_TYPEATTR
typedef struct TYPEATTR_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID guid_data1, guid_data2, guid_data3, guid_b0, guid_b1, guid_b2, guid_b3, guid_b4, guid_b5, guid_b6, guid_b7, lcid, dwReserved, memidConstructor, memidDestructor, lpstrSchema, cbSizeInstance, typekind, cFuncs, cVars, cImplTypes, cbSizeVft, cbAlignment, wTypeFlags, wMajorVerNum, wMinorVerNum, tdescAlias_unionField, tdescAlias_vt, idldescType_dwReserved, idldescType_wIDLFlags;
} TYPEATTR_FID_CACHE;

TYPEATTR_FID_CACHE TYPEATTRFc;

void cacheTYPEATTRFids(JNIEnv *env, jobject lpObject)
{
	if (TYPEATTRFc.cached) return;
	TYPEATTRFc.clazz = (*env)->GetObjectClass(env, lpObject);
	TYPEATTRFc.guid_data1 = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "guid_data1", "I");
	TYPEATTRFc.guid_data2 = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "guid_data2", "S");
	TYPEATTRFc.guid_data3 = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "guid_data3", "S");
	TYPEATTRFc.guid_b0 = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "guid_b0", "B");
	TYPEATTRFc.guid_b1 = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "guid_b1", "B");
	TYPEATTRFc.guid_b2 = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "guid_b2", "B");
	TYPEATTRFc.guid_b3 = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "guid_b3", "B");
	TYPEATTRFc.guid_b4 = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "guid_b4", "B");
	TYPEATTRFc.guid_b5 = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "guid_b5", "B");
	TYPEATTRFc.guid_b6 = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "guid_b6", "B");
	TYPEATTRFc.guid_b7 = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "guid_b7", "B");
	TYPEATTRFc.lcid = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "lcid", "I");
	TYPEATTRFc.dwReserved = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "dwReserved", "I");
	TYPEATTRFc.memidConstructor = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "memidConstructor", "I");
	TYPEATTRFc.memidDestructor = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "memidDestructor", "I");
	TYPEATTRFc.lpstrSchema = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "lpstrSchema", "I");
	TYPEATTRFc.cbSizeInstance = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "cbSizeInstance", "I");
	TYPEATTRFc.typekind = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "typekind", "I");
	TYPEATTRFc.cFuncs = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "cFuncs", "S");
	TYPEATTRFc.cVars = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "cVars", "S");
	TYPEATTRFc.cImplTypes = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "cImplTypes", "S");
	TYPEATTRFc.cbSizeVft = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "cbSizeVft", "S");
	TYPEATTRFc.cbAlignment = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "cbAlignment", "S");
	TYPEATTRFc.wTypeFlags = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "wTypeFlags", "S");
	TYPEATTRFc.wMajorVerNum = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "wMajorVerNum", "S");
	TYPEATTRFc.wMinorVerNum = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "wMinorVerNum", "S");
	TYPEATTRFc.tdescAlias_unionField = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "tdescAlias_unionField", "I");
	TYPEATTRFc.tdescAlias_vt = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "tdescAlias_vt", "S");
	TYPEATTRFc.idldescType_dwReserved = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "idldescType_dwReserved", "I");
	TYPEATTRFc.idldescType_wIDLFlags = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "idldescType_wIDLFlags", "S");
	TYPEATTRFc.cached = 1;
}

TYPEATTR *getTYPEATTRFields(JNIEnv *env, jobject lpObject, TYPEATTR *lpStruct)
{
	if (!TYPEATTRFc.cached) cacheTYPEATTRFids(env, lpObject);
	lpStruct->idldescType.wIDLFlags = (*env)->GetShortField(env, lpObject, TYPEATTRFc.idldescType_wIDLFlags);
	lpStruct->idldescType.dwReserved = (*env)->GetIntField(env, lpObject, TYPEATTRFc.idldescType_dwReserved);
	lpStruct->tdescAlias.vt = (*env)->GetShortField(env, lpObject, TYPEATTRFc.tdescAlias_vt);
	lpStruct->tdescAlias.lptdesc = (struct FARSTRUCT tagTYPEDESC FAR *)(*env)->GetIntField(env, lpObject, TYPEATTRFc.tdescAlias_unionField);
	lpStruct->wMinorVerNum = (*env)->GetShortField(env, lpObject, TYPEATTRFc.wMinorVerNum);
	lpStruct->wMajorVerNum = (*env)->GetShortField(env, lpObject, TYPEATTRFc.wMajorVerNum);
	lpStruct->wTypeFlags = (*env)->GetShortField(env, lpObject, TYPEATTRFc.wTypeFlags);
	lpStruct->cbAlignment = (*env)->GetShortField(env, lpObject, TYPEATTRFc.cbAlignment);
	lpStruct->cbSizeVft = (*env)->GetShortField(env, lpObject, TYPEATTRFc.cbSizeVft);
	lpStruct->cImplTypes = (*env)->GetShortField(env, lpObject, TYPEATTRFc.cImplTypes);
	lpStruct->cVars = (*env)->GetShortField(env, lpObject, TYPEATTRFc.cVars);
	lpStruct->cFuncs = (*env)->GetShortField(env, lpObject, TYPEATTRFc.cFuncs);
	lpStruct->typekind = (*env)->GetIntField(env, lpObject, TYPEATTRFc.typekind);
	lpStruct->cbSizeInstance = (*env)->GetIntField(env, lpObject, TYPEATTRFc.cbSizeInstance);
	lpStruct->lpstrSchema = (OLECHAR FAR *)(*env)->GetIntField(env, lpObject, TYPEATTRFc.lpstrSchema);
	lpStruct->memidDestructor = (*env)->GetIntField(env, lpObject, TYPEATTRFc.memidDestructor);
	lpStruct->memidConstructor = (*env)->GetIntField(env, lpObject, TYPEATTRFc.memidConstructor);
	lpStruct->dwReserved = (*env)->GetIntField(env, lpObject, TYPEATTRFc.dwReserved);
	lpStruct->lcid = (*env)->GetIntField(env, lpObject, TYPEATTRFc.lcid);
	lpStruct->guid.Data4[7] = (*env)->GetByteField(env, lpObject, TYPEATTRFc.guid_b7);
	lpStruct->guid.Data4[6] = (*env)->GetByteField(env, lpObject, TYPEATTRFc.guid_b6);
	lpStruct->guid.Data4[5] = (*env)->GetByteField(env, lpObject, TYPEATTRFc.guid_b5);
	lpStruct->guid.Data4[4] = (*env)->GetByteField(env, lpObject, TYPEATTRFc.guid_b4);
	lpStruct->guid.Data4[3] = (*env)->GetByteField(env, lpObject, TYPEATTRFc.guid_b3);
	lpStruct->guid.Data4[2] = (*env)->GetByteField(env, lpObject, TYPEATTRFc.guid_b2);
	lpStruct->guid.Data4[1] = (*env)->GetByteField(env, lpObject, TYPEATTRFc.guid_b1);
	lpStruct->guid.Data4[0] = (*env)->GetByteField(env, lpObject, TYPEATTRFc.guid_b0);
	lpStruct->guid.Data3 = (*env)->GetShortField(env, lpObject, TYPEATTRFc.guid_data3);
	lpStruct->guid.Data2 = (*env)->GetShortField(env, lpObject, TYPEATTRFc.guid_data2);
	lpStruct->guid.Data1 = (*env)->GetIntField(env, lpObject, TYPEATTRFc.guid_data1);
	return lpStruct;
}

void setTYPEATTRFields(JNIEnv *env, jobject lpObject, TYPEATTR *lpStruct)
{
	if (!TYPEATTRFc.cached) cacheTYPEATTRFids(env, lpObject);
	(*env)->SetShortField(env, lpObject, TYPEATTRFc.idldescType_wIDLFlags, lpStruct->idldescType.wIDLFlags);
	(*env)->SetIntField(env, lpObject, TYPEATTRFc.idldescType_dwReserved, lpStruct->idldescType.dwReserved);
	(*env)->SetShortField(env, lpObject, TYPEATTRFc.tdescAlias_vt, lpStruct->tdescAlias.vt);
	(*env)->SetIntField(env, lpObject, TYPEATTRFc.tdescAlias_unionField, (jint)lpStruct->tdescAlias.lptdesc);
	(*env)->SetShortField(env, lpObject, TYPEATTRFc.wMinorVerNum, lpStruct->wMinorVerNum);
	(*env)->SetShortField(env, lpObject, TYPEATTRFc.wMajorVerNum, lpStruct->wMajorVerNum);
	(*env)->SetShortField(env, lpObject, TYPEATTRFc.wTypeFlags, lpStruct->wTypeFlags);
	(*env)->SetShortField(env, lpObject, TYPEATTRFc.cbAlignment, lpStruct->cbAlignment);
	(*env)->SetShortField(env, lpObject, TYPEATTRFc.cbSizeVft, lpStruct->cbSizeVft);
	(*env)->SetShortField(env, lpObject, TYPEATTRFc.cImplTypes, lpStruct->cImplTypes);
	(*env)->SetShortField(env, lpObject, TYPEATTRFc.cVars, lpStruct->cVars);
	(*env)->SetShortField(env, lpObject, TYPEATTRFc.cFuncs, lpStruct->cFuncs);
	(*env)->SetIntField(env, lpObject, TYPEATTRFc.typekind, lpStruct->typekind);
	(*env)->SetIntField(env, lpObject, TYPEATTRFc.cbSizeInstance, lpStruct->cbSizeInstance);
	(*env)->SetIntField(env, lpObject, TYPEATTRFc.lpstrSchema, (jint)lpStruct->lpstrSchema);
	(*env)->SetIntField(env, lpObject, TYPEATTRFc.memidDestructor, lpStruct->memidDestructor);
	(*env)->SetIntField(env, lpObject, TYPEATTRFc.memidConstructor, lpStruct->memidConstructor);
	(*env)->SetIntField(env, lpObject, TYPEATTRFc.dwReserved, lpStruct->dwReserved);
	(*env)->SetIntField(env, lpObject, TYPEATTRFc.lcid, lpStruct->lcid);
	(*env)->SetByteField(env, lpObject, TYPEATTRFc.guid_b7, lpStruct->guid.Data4[7]);
	(*env)->SetByteField(env, lpObject, TYPEATTRFc.guid_b6, lpStruct->guid.Data4[6]);
	(*env)->SetByteField(env, lpObject, TYPEATTRFc.guid_b5, lpStruct->guid.Data4[5]);
	(*env)->SetByteField(env, lpObject, TYPEATTRFc.guid_b4, lpStruct->guid.Data4[4]);
	(*env)->SetByteField(env, lpObject, TYPEATTRFc.guid_b3, lpStruct->guid.Data4[3]);
	(*env)->SetByteField(env, lpObject, TYPEATTRFc.guid_b2, lpStruct->guid.Data4[2]);
	(*env)->SetByteField(env, lpObject, TYPEATTRFc.guid_b1, lpStruct->guid.Data4[1]);
	(*env)->SetByteField(env, lpObject, TYPEATTRFc.guid_b0, lpStruct->guid.Data4[0]);
	(*env)->SetShortField(env, lpObject, TYPEATTRFc.guid_data3, lpStruct->guid.Data3);
	(*env)->SetShortField(env, lpObject, TYPEATTRFc.guid_data2, lpStruct->guid.Data2);
	(*env)->SetIntField(env, lpObject, TYPEATTRFc.guid_data1, lpStruct->guid.Data1);
}
#endif /* NO_TYPEATTR */

#ifndef NO_VARDESC1
typedef struct VARDESC1_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID memid, lpstrSchema, unionField, elemdescVar_tdesc_union, elemdescVar_tdesc_vt, elemdescVar_paramdesc_pparamdescex, elemdescVar_paramdesc_wParamFlags, wVarFlags, varkind;
} VARDESC1_FID_CACHE;

VARDESC1_FID_CACHE VARDESC1Fc;

void cacheVARDESC1Fids(JNIEnv *env, jobject lpObject)
{
	if (VARDESC1Fc.cached) return;
	VARDESC1Fc.clazz = (*env)->GetObjectClass(env, lpObject);
	VARDESC1Fc.memid = (*env)->GetFieldID(env, VARDESC1Fc.clazz, "memid", "I");
	VARDESC1Fc.lpstrSchema = (*env)->GetFieldID(env, VARDESC1Fc.clazz, "lpstrSchema", "I");
	VARDESC1Fc.unionField = (*env)->GetFieldID(env, VARDESC1Fc.clazz, "unionField", "I");
	VARDESC1Fc.elemdescVar_tdesc_union = (*env)->GetFieldID(env, VARDESC1Fc.clazz, "elemdescVar_tdesc_union", "I");
	VARDESC1Fc.elemdescVar_tdesc_vt = (*env)->GetFieldID(env, VARDESC1Fc.clazz, "elemdescVar_tdesc_vt", "S");
	VARDESC1Fc.elemdescVar_paramdesc_pparamdescex = (*env)->GetFieldID(env, VARDESC1Fc.clazz, "elemdescVar_paramdesc_pparamdescex", "I");
	VARDESC1Fc.elemdescVar_paramdesc_wParamFlags = (*env)->GetFieldID(env, VARDESC1Fc.clazz, "elemdescVar_paramdesc_wParamFlags", "S");
	VARDESC1Fc.wVarFlags = (*env)->GetFieldID(env, VARDESC1Fc.clazz, "wVarFlags", "S");
	VARDESC1Fc.varkind = (*env)->GetFieldID(env, VARDESC1Fc.clazz, "varkind", "I");
	VARDESC1Fc.cached = 1;
}

VARDESC *getVARDESC1Fields(JNIEnv *env, jobject lpObject, VARDESC *lpStruct)
{
	if (!VARDESC1Fc.cached) cacheVARDESC1Fids(env, lpObject);
	lpStruct->varkind = (*env)->GetIntField(env, lpObject, VARDESC1Fc.varkind);
	lpStruct->wVarFlags = (*env)->GetShortField(env, lpObject, VARDESC1Fc.wVarFlags);
	lpStruct->elemdescVar.paramdesc.wParamFlags = (*env)->GetShortField(env, lpObject, VARDESC1Fc.elemdescVar_paramdesc_wParamFlags);
	lpStruct->elemdescVar.paramdesc.pparamdescex = (LPPARAMDESCEX)(*env)->GetIntField(env, lpObject, VARDESC1Fc.elemdescVar_paramdesc_pparamdescex);
	lpStruct->elemdescVar.tdesc.vt = (*env)->GetShortField(env, lpObject, VARDESC1Fc.elemdescVar_tdesc_vt);
	lpStruct->elemdescVar.tdesc.lptdesc = (struct FARSTRUCT tagTYPEDESC FAR *)(*env)->GetIntField(env, lpObject, VARDESC1Fc.elemdescVar_tdesc_union);
	lpStruct->oInst = (*env)->GetIntField(env, lpObject, VARDESC1Fc.unionField);
	lpStruct->lpstrSchema = (OLECHAR FAR *)(*env)->GetIntField(env, lpObject, VARDESC1Fc.lpstrSchema);
	lpStruct->memid = (*env)->GetIntField(env, lpObject, VARDESC1Fc.memid);
	return lpStruct;
}

void setVARDESC1Fields(JNIEnv *env, jobject lpObject, VARDESC *lpStruct)
{
	if (!VARDESC1Fc.cached) cacheVARDESC1Fids(env, lpObject);
	(*env)->SetIntField(env, lpObject, VARDESC1Fc.varkind, lpStruct->varkind);
	(*env)->SetShortField(env, lpObject, VARDESC1Fc.wVarFlags, lpStruct->wVarFlags);
	(*env)->SetShortField(env, lpObject, VARDESC1Fc.elemdescVar_paramdesc_wParamFlags, lpStruct->elemdescVar.paramdesc.wParamFlags);
	(*env)->SetIntField(env, lpObject, VARDESC1Fc.elemdescVar_paramdesc_pparamdescex, (jint)lpStruct->elemdescVar.paramdesc.pparamdescex);
	(*env)->SetShortField(env, lpObject, VARDESC1Fc.elemdescVar_tdesc_vt, lpStruct->elemdescVar.tdesc.vt);
	(*env)->SetIntField(env, lpObject, VARDESC1Fc.elemdescVar_tdesc_union, (jint)lpStruct->elemdescVar.tdesc.lptdesc);
	(*env)->SetIntField(env, lpObject, VARDESC1Fc.unionField, lpStruct->oInst);
	(*env)->SetIntField(env, lpObject, VARDESC1Fc.lpstrSchema, (jint)lpStruct->lpstrSchema);
	(*env)->SetIntField(env, lpObject, VARDESC1Fc.memid, lpStruct->memid);
}
#endif /* NO_VARDESC1 */

#ifndef NO_VARDESC2
typedef struct VARDESC2_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID memid, lpstrSchema, unionField, elemdescVar_tdesc_union, elemdescVar_tdesc_vt, elemdescFunc_idldesc_dwReserved, elemdescFunc_idldesc_wIDLFlags, wVarFlags, varkind;
} VARDESC2_FID_CACHE;

VARDESC2_FID_CACHE VARDESC2Fc;

void cacheVARDESC2Fids(JNIEnv *env, jobject lpObject)
{
	if (VARDESC2Fc.cached) return;
	VARDESC2Fc.clazz = (*env)->GetObjectClass(env, lpObject);
	VARDESC2Fc.memid = (*env)->GetFieldID(env, VARDESC2Fc.clazz, "memid", "I");
	VARDESC2Fc.lpstrSchema = (*env)->GetFieldID(env, VARDESC2Fc.clazz, "lpstrSchema", "I");
	VARDESC2Fc.unionField = (*env)->GetFieldID(env, VARDESC2Fc.clazz, "unionField", "I");
	VARDESC2Fc.elemdescVar_tdesc_union = (*env)->GetFieldID(env, VARDESC2Fc.clazz, "elemdescVar_tdesc_union", "I");
	VARDESC2Fc.elemdescVar_tdesc_vt = (*env)->GetFieldID(env, VARDESC2Fc.clazz, "elemdescVar_tdesc_vt", "S");
	VARDESC2Fc.elemdescFunc_idldesc_dwReserved = (*env)->GetFieldID(env, VARDESC2Fc.clazz, "elemdescFunc_idldesc_dwReserved", "I");
	VARDESC2Fc.elemdescFunc_idldesc_wIDLFlags = (*env)->GetFieldID(env, VARDESC2Fc.clazz, "elemdescFunc_idldesc_wIDLFlags", "S");
	VARDESC2Fc.wVarFlags = (*env)->GetFieldID(env, VARDESC2Fc.clazz, "wVarFlags", "S");
	VARDESC2Fc.varkind = (*env)->GetFieldID(env, VARDESC2Fc.clazz, "varkind", "I");
	VARDESC2Fc.cached = 1;
}

VARDESC *getVARDESC2Fields(JNIEnv *env, jobject lpObject, VARDESC *lpStruct)
{
	if (!VARDESC2Fc.cached) cacheVARDESC2Fids(env, lpObject);
	lpStruct->varkind = (*env)->GetIntField(env, lpObject, VARDESC2Fc.varkind);
	lpStruct->wVarFlags = (*env)->GetShortField(env, lpObject, VARDESC2Fc.wVarFlags);
	lpStruct->elemdescVar.idldesc.wIDLFlags = (*env)->GetShortField(env, lpObject, VARDESC2Fc.elemdescFunc_idldesc_wIDLFlags);
	lpStruct->elemdescVar.idldesc.dwReserved = (*env)->GetIntField(env, lpObject, VARDESC2Fc.elemdescFunc_idldesc_dwReserved);
	lpStruct->elemdescVar.tdesc.vt = (*env)->GetShortField(env, lpObject, VARDESC2Fc.elemdescVar_tdesc_vt);
	lpStruct->elemdescVar.tdesc.lptdesc = (struct FARSTRUCT tagTYPEDESC FAR *)(*env)->GetIntField(env, lpObject, VARDESC2Fc.elemdescVar_tdesc_union);
	lpStruct->oInst = (*env)->GetIntField(env, lpObject, VARDESC2Fc.unionField);
	lpStruct->lpstrSchema = (OLECHAR FAR *)(*env)->GetIntField(env, lpObject, VARDESC2Fc.lpstrSchema);
	lpStruct->memid = (*env)->GetIntField(env, lpObject, VARDESC2Fc.memid);
	return lpStruct;
}

void setVARDESC2Fields(JNIEnv *env, jobject lpObject, VARDESC *lpStruct)
{
	if (!VARDESC2Fc.cached) cacheVARDESC2Fids(env, lpObject);
	(*env)->SetIntField(env, lpObject, VARDESC2Fc.varkind, lpStruct->varkind);
	(*env)->SetShortField(env, lpObject, VARDESC2Fc.wVarFlags, lpStruct->wVarFlags);
	(*env)->SetShortField(env, lpObject, VARDESC2Fc.elemdescFunc_idldesc_wIDLFlags, lpStruct->elemdescVar.idldesc.wIDLFlags);
	(*env)->SetIntField(env, lpObject, VARDESC2Fc.elemdescFunc_idldesc_dwReserved, lpStruct->elemdescVar.idldesc.dwReserved);
	(*env)->SetShortField(env, lpObject, VARDESC2Fc.elemdescVar_tdesc_vt, lpStruct->elemdescVar.tdesc.vt);
	(*env)->SetIntField(env, lpObject, VARDESC2Fc.elemdescVar_tdesc_union, (jint)lpStruct->elemdescVar.tdesc.lptdesc);
	(*env)->SetIntField(env, lpObject, VARDESC2Fc.unionField, lpStruct->oInst);
	(*env)->SetIntField(env, lpObject, VARDESC2Fc.lpstrSchema, (jint)lpStruct->lpstrSchema);
	(*env)->SetIntField(env, lpObject, VARDESC2Fc.memid, lpStruct->memid);
}
#endif /* NO_VARDESC2 */
