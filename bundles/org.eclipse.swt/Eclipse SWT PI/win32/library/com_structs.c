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
#include "com_structs.h"

#ifndef NO_CAUUID
typedef struct CAUUID_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cElems, pElems;
} CAUUID_FID_CACHE;

CAUUID_FID_CACHE CAUUIDFc;

void cacheCAUUIDFields(JNIEnv *env, jobject lpObject)
{
	if (CAUUIDFc.cached) return;
	CAUUIDFc.clazz = (*env)->GetObjectClass(env, lpObject);
	CAUUIDFc.cElems = (*env)->GetFieldID(env, CAUUIDFc.clazz, "cElems", "I");
	CAUUIDFc.pElems = (*env)->GetFieldID(env, CAUUIDFc.clazz, "pElems", "I");
	CAUUIDFc.cached = 1;
}

CAUUID *getCAUUIDFields(JNIEnv *env, jobject lpObject, CAUUID *lpStruct)
{
	if (!CAUUIDFc.cached) cacheCAUUIDFields(env, lpObject);
	lpStruct->cElems = (*env)->GetIntField(env, lpObject, CAUUIDFc.cElems);
	lpStruct->pElems = (GUID FAR *)(*env)->GetIntField(env, lpObject, CAUUIDFc.pElems);
	return lpStruct;
}

void setCAUUIDFields(JNIEnv *env, jobject lpObject, CAUUID *lpStruct)
{
	if (!CAUUIDFc.cached) cacheCAUUIDFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, CAUUIDFc.cElems, (jint)lpStruct->cElems);
	(*env)->SetIntField(env, lpObject, CAUUIDFc.pElems, (jint)lpStruct->pElems);
}
#endif

#ifndef NO_CONTROLINFO
typedef struct CONTROLINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cb, hAccel, cAccel, dwFlags;
} CONTROLINFO_FID_CACHE;

CONTROLINFO_FID_CACHE CONTROLINFOFc;

void cacheCONTROLINFOFields(JNIEnv *env, jobject lpObject)
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
	if (!CONTROLINFOFc.cached) cacheCONTROLINFOFields(env, lpObject);
	lpStruct->cb = (*env)->GetIntField(env, lpObject, CONTROLINFOFc.cb);
	lpStruct->hAccel = (HACCEL)(*env)->GetIntField(env, lpObject, CONTROLINFOFc.hAccel);
	lpStruct->cAccel = (*env)->GetShortField(env, lpObject, CONTROLINFOFc.cAccel);
	lpStruct->dwFlags = (*env)->GetIntField(env, lpObject, CONTROLINFOFc.dwFlags);
	return lpStruct;
}

void setCONTROLINFOFields(JNIEnv *env, jobject lpObject, CONTROLINFO *lpStruct)
{
	if (!CONTROLINFOFc.cached) cacheCONTROLINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, CONTROLINFOFc.cb, (jint)lpStruct->cb);
	(*env)->SetIntField(env, lpObject, CONTROLINFOFc.hAccel, (jint)lpStruct->hAccel);
	(*env)->SetShortField(env, lpObject, CONTROLINFOFc.cAccel, (jshort)lpStruct->cAccel);
	(*env)->SetIntField(env, lpObject, CONTROLINFOFc.dwFlags, (jint)lpStruct->dwFlags);
}
#endif

#ifndef NO_COSERVERINFO
typedef struct COSERVERINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID dwReserved1, pwszName, pAuthInfo, dwReserved2;
} COSERVERINFO_FID_CACHE;

COSERVERINFO_FID_CACHE COSERVERINFOFc;

void cacheCOSERVERINFOFields(JNIEnv *env, jobject lpObject)
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
	if (!COSERVERINFOFc.cached) cacheCOSERVERINFOFields(env, lpObject);
	lpStruct->dwReserved1 = (*env)->GetIntField(env, lpObject, COSERVERINFOFc.dwReserved1);
	lpStruct->pwszName = (LPWSTR)(*env)->GetIntField(env, lpObject, COSERVERINFOFc.pwszName);
	lpStruct->pAuthInfo = (COAUTHINFO *)(*env)->GetIntField(env, lpObject, COSERVERINFOFc.pAuthInfo);
	lpStruct->dwReserved2 = (*env)->GetIntField(env, lpObject, COSERVERINFOFc.dwReserved2);
	return lpStruct;
}

void setCOSERVERINFOFields(JNIEnv *env, jobject lpObject, COSERVERINFO *lpStruct)
{
	if (!COSERVERINFOFc.cached) cacheCOSERVERINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, COSERVERINFOFc.dwReserved1, (jint)lpStruct->dwReserved1);
	(*env)->SetIntField(env, lpObject, COSERVERINFOFc.pwszName, (jint)lpStruct->pwszName);
	(*env)->SetIntField(env, lpObject, COSERVERINFOFc.pAuthInfo, (jint)lpStruct->pAuthInfo);
	(*env)->SetIntField(env, lpObject, COSERVERINFOFc.dwReserved2, (jint)lpStruct->dwReserved2);
}
#endif

#ifndef NO_DISPPARAMS
typedef struct DISPPARAMS_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID rgvarg, rgdispidNamedArgs, cArgs, cNamedArgs;
} DISPPARAMS_FID_CACHE;

DISPPARAMS_FID_CACHE DISPPARAMSFc;

void cacheDISPPARAMSFields(JNIEnv *env, jobject lpObject)
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
	if (!DISPPARAMSFc.cached) cacheDISPPARAMSFields(env, lpObject);
	lpStruct->rgvarg = (VARIANTARG FAR *)(*env)->GetIntField(env, lpObject, DISPPARAMSFc.rgvarg);
	lpStruct->rgdispidNamedArgs = (DISPID FAR *)(*env)->GetIntField(env, lpObject, DISPPARAMSFc.rgdispidNamedArgs);
	lpStruct->cArgs = (*env)->GetIntField(env, lpObject, DISPPARAMSFc.cArgs);
	lpStruct->cNamedArgs = (*env)->GetIntField(env, lpObject, DISPPARAMSFc.cNamedArgs);
	return lpStruct;
}

void setDISPPARAMSFields(JNIEnv *env, jobject lpObject, DISPPARAMS *lpStruct)
{
	if (!DISPPARAMSFc.cached) cacheDISPPARAMSFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, DISPPARAMSFc.rgvarg, (jint)lpStruct->rgvarg);
	(*env)->SetIntField(env, lpObject, DISPPARAMSFc.rgdispidNamedArgs, (jint)lpStruct->rgdispidNamedArgs);
	(*env)->SetIntField(env, lpObject, DISPPARAMSFc.cArgs, (jint)lpStruct->cArgs);
	(*env)->SetIntField(env, lpObject, DISPPARAMSFc.cNamedArgs, (jint)lpStruct->cNamedArgs);
}
#endif

#ifndef NO_DVTARGETDEVICE
typedef struct DVTARGETDEVICE_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID tdSize, tdDriverNameOffset, tdDeviceNameOffset, tdPortNameOffset, tdExtDevmodeOffset, tdData;
} DVTARGETDEVICE_FID_CACHE;

DVTARGETDEVICE_FID_CACHE DVTARGETDEVICEFc;

void cacheDVTARGETDEVICEFields(JNIEnv *env, jobject lpObject)
{
	if (DVTARGETDEVICEFc.cached) return;
	DVTARGETDEVICEFc.clazz = (*env)->GetObjectClass(env, lpObject);
	DVTARGETDEVICEFc.tdSize = (*env)->GetFieldID(env, DVTARGETDEVICEFc.clazz, "tdSize", "I");
	DVTARGETDEVICEFc.tdDriverNameOffset = (*env)->GetFieldID(env, DVTARGETDEVICEFc.clazz, "tdDriverNameOffset", "S");
	DVTARGETDEVICEFc.tdDeviceNameOffset = (*env)->GetFieldID(env, DVTARGETDEVICEFc.clazz, "tdDeviceNameOffset", "S");
	DVTARGETDEVICEFc.tdPortNameOffset = (*env)->GetFieldID(env, DVTARGETDEVICEFc.clazz, "tdPortNameOffset", "S");
	DVTARGETDEVICEFc.tdExtDevmodeOffset = (*env)->GetFieldID(env, DVTARGETDEVICEFc.clazz, "tdExtDevmodeOffset", "S");
	DVTARGETDEVICEFc.tdData = (*env)->GetFieldID(env, DVTARGETDEVICEFc.clazz, "tdData", "[B");
	DVTARGETDEVICEFc.cached = 1;
}

DVTARGETDEVICE *getDVTARGETDEVICEFields(JNIEnv *env, jobject lpObject, DVTARGETDEVICE *lpStruct)
{
	if (!DVTARGETDEVICEFc.cached) cacheDVTARGETDEVICEFields(env, lpObject);
	lpStruct->tdSize = (*env)->GetIntField(env, lpObject, DVTARGETDEVICEFc.tdSize);
	lpStruct->tdDriverNameOffset = (*env)->GetShortField(env, lpObject, DVTARGETDEVICEFc.tdDriverNameOffset);
	lpStruct->tdDeviceNameOffset = (*env)->GetShortField(env, lpObject, DVTARGETDEVICEFc.tdDeviceNameOffset);
	lpStruct->tdPortNameOffset = (*env)->GetShortField(env, lpObject, DVTARGETDEVICEFc.tdPortNameOffset);
	lpStruct->tdExtDevmodeOffset = (*env)->GetShortField(env, lpObject, DVTARGETDEVICEFc.tdExtDevmodeOffset);
	{
	jbyteArray lpObject1 = (jbyteArray)(*env)->GetObjectField(env, lpObject, DVTARGETDEVICEFc.tdData);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->tdData[0]), (jbyte *)lpStruct->tdData[0]);
	}
	return lpStruct;
}

void setDVTARGETDEVICEFields(JNIEnv *env, jobject lpObject, DVTARGETDEVICE *lpStruct)
{
	if (!DVTARGETDEVICEFc.cached) cacheDVTARGETDEVICEFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, DVTARGETDEVICEFc.tdSize, (jint)lpStruct->tdSize);
	(*env)->SetShortField(env, lpObject, DVTARGETDEVICEFc.tdDriverNameOffset, (jshort)lpStruct->tdDriverNameOffset);
	(*env)->SetShortField(env, lpObject, DVTARGETDEVICEFc.tdDeviceNameOffset, (jshort)lpStruct->tdDeviceNameOffset);
	(*env)->SetShortField(env, lpObject, DVTARGETDEVICEFc.tdPortNameOffset, (jshort)lpStruct->tdPortNameOffset);
	(*env)->SetShortField(env, lpObject, DVTARGETDEVICEFc.tdExtDevmodeOffset, (jshort)lpStruct->tdExtDevmodeOffset);
	{
	jbyteArray lpObject1 = (jbyteArray)(*env)->GetObjectField(env, lpObject, DVTARGETDEVICEFc.tdData);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->tdData[0]), (jbyte *)lpStruct->tdData[0]);
	}
}
#endif

#ifndef NO_EXCEPINFO
typedef struct EXCEPINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID wCode, wReserved, bstrSource, bstrDescription, bstrHelpFile, dwHelpContext, pvReserved, pfnDeferredFillIn, scode;
} EXCEPINFO_FID_CACHE;

EXCEPINFO_FID_CACHE EXCEPINFOFc;

void cacheEXCEPINFOFields(JNIEnv *env, jobject lpObject)
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
	if (!EXCEPINFOFc.cached) cacheEXCEPINFOFields(env, lpObject);
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
	if (!EXCEPINFOFc.cached) cacheEXCEPINFOFields(env, lpObject);
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
#endif

#ifndef NO_FORMATETC
typedef struct FORMATETC_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cfFormat, ptd, dwAspect, lindex, tymed;
} FORMATETC_FID_CACHE;

FORMATETC_FID_CACHE FORMATETCFc;

void cacheFORMATETCFields(JNIEnv *env, jobject lpObject)
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
	if (!FORMATETCFc.cached) cacheFORMATETCFields(env, lpObject);
	lpStruct->cfFormat = (CLIPFORMAT)(*env)->GetIntField(env, lpObject, FORMATETCFc.cfFormat);
	lpStruct->ptd = (DVTARGETDEVICE *)(*env)->GetIntField(env, lpObject, FORMATETCFc.ptd);
	lpStruct->dwAspect = (*env)->GetIntField(env, lpObject, FORMATETCFc.dwAspect);
	lpStruct->lindex = (*env)->GetIntField(env, lpObject, FORMATETCFc.lindex);
	lpStruct->tymed = (*env)->GetIntField(env, lpObject, FORMATETCFc.tymed);
	return lpStruct;
}

void setFORMATETCFields(JNIEnv *env, jobject lpObject, FORMATETC *lpStruct)
{
	if (!FORMATETCFc.cached) cacheFORMATETCFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, FORMATETCFc.cfFormat, (jint)lpStruct->cfFormat);
	(*env)->SetIntField(env, lpObject, FORMATETCFc.ptd, (jint)lpStruct->ptd);
	(*env)->SetIntField(env, lpObject, FORMATETCFc.dwAspect, (jint)lpStruct->dwAspect);
	(*env)->SetIntField(env, lpObject, FORMATETCFc.lindex, (jint)lpStruct->lindex);
	(*env)->SetIntField(env, lpObject, FORMATETCFc.tymed, (jint)lpStruct->tymed);
}
#endif

#ifndef NO_FUNCDESC
typedef struct FUNCDESC_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID memid, lprgscode, lprgelemdescParam, funckind, invkind, callconv, cParams, cParamsOpt, oVft, cScodes, elemdescFunc_tdesc_union, elemdescFunc_tdesc_vt, elemdescFunc_paramdesc_pparamdescex, elemdescFunc_paramdesc_wParamFlags, wFuncFlags;
} FUNCDESC_FID_CACHE;

FUNCDESC_FID_CACHE FUNCDESCFc;

void cacheFUNCDESCFields(JNIEnv *env, jobject lpObject)
{
	if (FUNCDESCFc.cached) return;
	FUNCDESCFc.clazz = (*env)->GetObjectClass(env, lpObject);
	FUNCDESCFc.memid = (*env)->GetFieldID(env, FUNCDESCFc.clazz, "memid", "I");
	FUNCDESCFc.lprgscode = (*env)->GetFieldID(env, FUNCDESCFc.clazz, "lprgscode", "I");
	FUNCDESCFc.lprgelemdescParam = (*env)->GetFieldID(env, FUNCDESCFc.clazz, "lprgelemdescParam", "I");
	FUNCDESCFc.funckind = (*env)->GetFieldID(env, FUNCDESCFc.clazz, "funckind", "I");
	FUNCDESCFc.invkind = (*env)->GetFieldID(env, FUNCDESCFc.clazz, "invkind", "I");
	FUNCDESCFc.callconv = (*env)->GetFieldID(env, FUNCDESCFc.clazz, "callconv", "I");
	FUNCDESCFc.cParams = (*env)->GetFieldID(env, FUNCDESCFc.clazz, "cParams", "S");
	FUNCDESCFc.cParamsOpt = (*env)->GetFieldID(env, FUNCDESCFc.clazz, "cParamsOpt", "S");
	FUNCDESCFc.oVft = (*env)->GetFieldID(env, FUNCDESCFc.clazz, "oVft", "S");
	FUNCDESCFc.cScodes = (*env)->GetFieldID(env, FUNCDESCFc.clazz, "cScodes", "S");
	FUNCDESCFc.elemdescFunc_tdesc_union = (*env)->GetFieldID(env, FUNCDESCFc.clazz, "elemdescFunc_tdesc_union", "I");
	FUNCDESCFc.elemdescFunc_tdesc_vt = (*env)->GetFieldID(env, FUNCDESCFc.clazz, "elemdescFunc_tdesc_vt", "S");
	FUNCDESCFc.elemdescFunc_paramdesc_pparamdescex = (*env)->GetFieldID(env, FUNCDESCFc.clazz, "elemdescFunc_paramdesc_pparamdescex", "I");
	FUNCDESCFc.elemdescFunc_paramdesc_wParamFlags = (*env)->GetFieldID(env, FUNCDESCFc.clazz, "elemdescFunc_paramdesc_wParamFlags", "S");
	FUNCDESCFc.wFuncFlags = (*env)->GetFieldID(env, FUNCDESCFc.clazz, "wFuncFlags", "S");
	FUNCDESCFc.cached = 1;
}

FUNCDESC *getFUNCDESCFields(JNIEnv *env, jobject lpObject, FUNCDESC *lpStruct)
{
	if (!FUNCDESCFc.cached) cacheFUNCDESCFields(env, lpObject);
	lpStruct->memid = (MEMBERID)(*env)->GetIntField(env, lpObject, FUNCDESCFc.memid);
	lpStruct->lprgscode = (SCODE FAR *)(*env)->GetIntField(env, lpObject, FUNCDESCFc.lprgscode);
	lpStruct->lprgelemdescParam = (ELEMDESC FAR *)(*env)->GetIntField(env, lpObject, FUNCDESCFc.lprgelemdescParam);
	lpStruct->funckind = (FUNCKIND)(*env)->GetIntField(env, lpObject, FUNCDESCFc.funckind);
	lpStruct->invkind = (INVOKEKIND)(*env)->GetIntField(env, lpObject, FUNCDESCFc.invkind);
	lpStruct->callconv = (CALLCONV)(*env)->GetIntField(env, lpObject, FUNCDESCFc.callconv);
	lpStruct->cParams = (*env)->GetShortField(env, lpObject, FUNCDESCFc.cParams);
	lpStruct->cParamsOpt = (*env)->GetShortField(env, lpObject, FUNCDESCFc.cParamsOpt);
	lpStruct->oVft = (*env)->GetShortField(env, lpObject, FUNCDESCFc.oVft);
	lpStruct->cScodes = (*env)->GetShortField(env, lpObject, FUNCDESCFc.cScodes);
	lpStruct->elemdescFunc.tdesc.lptdesc = (struct FARSTRUCT tagTYPEDESC FAR* )(*env)->GetIntField(env, lpObject, FUNCDESCFc.elemdescFunc_tdesc_union);
	lpStruct->elemdescFunc.tdesc.vt = (*env)->GetShortField(env, lpObject, FUNCDESCFc.elemdescFunc_tdesc_vt);
	lpStruct->elemdescFunc.paramdesc.pparamdescex = (LPPARAMDESCEX)(*env)->GetIntField(env, lpObject, FUNCDESCFc.elemdescFunc_paramdesc_pparamdescex);
	lpStruct->elemdescFunc.paramdesc.wParamFlags = (*env)->GetShortField(env, lpObject, FUNCDESCFc.elemdescFunc_paramdesc_wParamFlags);
	lpStruct->wFuncFlags = (*env)->GetShortField(env, lpObject, FUNCDESCFc.wFuncFlags);
	return lpStruct;
}

void setFUNCDESCFields(JNIEnv *env, jobject lpObject, FUNCDESC *lpStruct)
{
	if (!FUNCDESCFc.cached) cacheFUNCDESCFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, FUNCDESCFc.memid, (jint)lpStruct->memid);
	(*env)->SetIntField(env, lpObject, FUNCDESCFc.lprgscode, (jint)lpStruct->lprgscode);
	(*env)->SetIntField(env, lpObject, FUNCDESCFc.lprgelemdescParam, (jint)lpStruct->lprgelemdescParam);
	(*env)->SetIntField(env, lpObject, FUNCDESCFc.funckind, (jint)lpStruct->funckind);
	(*env)->SetIntField(env, lpObject, FUNCDESCFc.invkind, (jint)lpStruct->invkind);
	(*env)->SetIntField(env, lpObject, FUNCDESCFc.callconv, (jint)lpStruct->callconv);
	(*env)->SetShortField(env, lpObject, FUNCDESCFc.cParams, (jshort)lpStruct->cParams);
	(*env)->SetShortField(env, lpObject, FUNCDESCFc.cParamsOpt, (jshort)lpStruct->cParamsOpt);
	(*env)->SetShortField(env, lpObject, FUNCDESCFc.oVft, (jshort)lpStruct->oVft);
	(*env)->SetShortField(env, lpObject, FUNCDESCFc.cScodes, (jshort)lpStruct->cScodes);
	(*env)->SetIntField(env, lpObject, FUNCDESCFc.elemdescFunc_tdesc_union, (jint)lpStruct->elemdescFunc.tdesc.lptdesc);
	(*env)->SetShortField(env, lpObject, FUNCDESCFc.elemdescFunc_tdesc_vt, (jshort)lpStruct->elemdescFunc.tdesc.vt);
	(*env)->SetIntField(env, lpObject, FUNCDESCFc.elemdescFunc_paramdesc_pparamdescex, (jint)lpStruct->elemdescFunc.paramdesc.pparamdescex);
	(*env)->SetShortField(env, lpObject, FUNCDESCFc.elemdescFunc_paramdesc_wParamFlags, (jshort)lpStruct->elemdescFunc.paramdesc.wParamFlags);
	(*env)->SetShortField(env, lpObject, FUNCDESCFc.wFuncFlags, (jshort)lpStruct->wFuncFlags);
}
#endif

#ifndef NO_GUID
typedef struct GUID_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID Data1, Data2, Data3, Data4;
} GUID_FID_CACHE;

GUID_FID_CACHE GUIDFc;

void cacheGUIDFields(JNIEnv *env, jobject lpObject)
{
	if (GUIDFc.cached) return;
	GUIDFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GUIDFc.Data1 = (*env)->GetFieldID(env, GUIDFc.clazz, "Data1", "I");
	GUIDFc.Data2 = (*env)->GetFieldID(env, GUIDFc.clazz, "Data2", "S");
	GUIDFc.Data3 = (*env)->GetFieldID(env, GUIDFc.clazz, "Data3", "S");
	GUIDFc.Data4 = (*env)->GetFieldID(env, GUIDFc.clazz, "Data4", "[B");
	GUIDFc.cached = 1;
}

GUID *getGUIDFields(JNIEnv *env, jobject lpObject, GUID *lpStruct)
{
	if (!GUIDFc.cached) cacheGUIDFields(env, lpObject);
	lpStruct->Data1 = (*env)->GetIntField(env, lpObject, GUIDFc.Data1);
	lpStruct->Data2 = (*env)->GetShortField(env, lpObject, GUIDFc.Data2);
	lpStruct->Data3 = (*env)->GetShortField(env, lpObject, GUIDFc.Data3);
	{
	jbyteArray lpObject1 = (jbyteArray)(*env)->GetObjectField(env, lpObject, GUIDFc.Data4);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->Data4), (jbyte *)lpStruct->Data4);
	}
	return lpStruct;
}

void setGUIDFields(JNIEnv *env, jobject lpObject, GUID *lpStruct)
{
	if (!GUIDFc.cached) cacheGUIDFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GUIDFc.Data1, (jint)lpStruct->Data1);
	(*env)->SetShortField(env, lpObject, GUIDFc.Data2, (jshort)lpStruct->Data2);
	(*env)->SetShortField(env, lpObject, GUIDFc.Data3, (jshort)lpStruct->Data3);
	{
	jbyteArray lpObject1 = (jbyteArray)(*env)->GetObjectField(env, lpObject, GUIDFc.Data4);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->Data4), (jbyte *)lpStruct->Data4);
	}
}
#endif

#ifndef NO_LICINFO
typedef struct LICINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cbLicInfo, fRuntimeKeyAvail, fLicVerified;
} LICINFO_FID_CACHE;

LICINFO_FID_CACHE LICINFOFc;

void cacheLICINFOFields(JNIEnv *env, jobject lpObject)
{
	if (LICINFOFc.cached) return;
	LICINFOFc.clazz = (*env)->GetObjectClass(env, lpObject);
	LICINFOFc.cbLicInfo = (*env)->GetFieldID(env, LICINFOFc.clazz, "cbLicInfo", "I");
	LICINFOFc.fRuntimeKeyAvail = (*env)->GetFieldID(env, LICINFOFc.clazz, "fRuntimeKeyAvail", "Z");
	LICINFOFc.fLicVerified = (*env)->GetFieldID(env, LICINFOFc.clazz, "fLicVerified", "Z");
	LICINFOFc.cached = 1;
}

LICINFO *getLICINFOFields(JNIEnv *env, jobject lpObject, LICINFO *lpStruct)
{
	if (!LICINFOFc.cached) cacheLICINFOFields(env, lpObject);
	lpStruct->cbLicInfo = (*env)->GetIntField(env, lpObject, LICINFOFc.cbLicInfo);
	lpStruct->fRuntimeKeyAvail = (*env)->GetBooleanField(env, lpObject, LICINFOFc.fRuntimeKeyAvail);
	lpStruct->fLicVerified = (*env)->GetBooleanField(env, lpObject, LICINFOFc.fLicVerified);
	return lpStruct;
}

void setLICINFOFields(JNIEnv *env, jobject lpObject, LICINFO *lpStruct)
{
	if (!LICINFOFc.cached) cacheLICINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, LICINFOFc.cbLicInfo, (jint)lpStruct->cbLicInfo);
	(*env)->SetBooleanField(env, lpObject, LICINFOFc.fRuntimeKeyAvail, (jboolean)lpStruct->fRuntimeKeyAvail);
	(*env)->SetBooleanField(env, lpObject, LICINFOFc.fLicVerified, (jboolean)lpStruct->fLicVerified);
}
#endif

#ifndef NO_OLECMD
typedef struct OLECMD_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cmdID, cmdf;
} OLECMD_FID_CACHE;

OLECMD_FID_CACHE OLECMDFc;

void cacheOLECMDFields(JNIEnv *env, jobject lpObject)
{
	if (OLECMDFc.cached) return;
	OLECMDFc.clazz = (*env)->GetObjectClass(env, lpObject);
	OLECMDFc.cmdID = (*env)->GetFieldID(env, OLECMDFc.clazz, "cmdID", "I");
	OLECMDFc.cmdf = (*env)->GetFieldID(env, OLECMDFc.clazz, "cmdf", "I");
	OLECMDFc.cached = 1;
}

OLECMD *getOLECMDFields(JNIEnv *env, jobject lpObject, OLECMD *lpStruct)
{
	if (!OLECMDFc.cached) cacheOLECMDFields(env, lpObject);
	lpStruct->cmdID = (*env)->GetIntField(env, lpObject, OLECMDFc.cmdID);
	lpStruct->cmdf = (*env)->GetIntField(env, lpObject, OLECMDFc.cmdf);
	return lpStruct;
}

void setOLECMDFields(JNIEnv *env, jobject lpObject, OLECMD *lpStruct)
{
	if (!OLECMDFc.cached) cacheOLECMDFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, OLECMDFc.cmdID, (jint)lpStruct->cmdID);
	(*env)->SetIntField(env, lpObject, OLECMDFc.cmdf, (jint)lpStruct->cmdf);
}
#endif

#ifndef NO_OLECMDTEXT
typedef struct OLECMDTEXT_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cmdtextf, cwActual, cwBuf, rgwz;
} OLECMDTEXT_FID_CACHE;

OLECMDTEXT_FID_CACHE OLECMDTEXTFc;

void cacheOLECMDTEXTFields(JNIEnv *env, jobject lpObject)
{
	if (OLECMDTEXTFc.cached) return;
	OLECMDTEXTFc.clazz = (*env)->GetObjectClass(env, lpObject);
	OLECMDTEXTFc.cmdtextf = (*env)->GetFieldID(env, OLECMDTEXTFc.clazz, "cmdtextf", "I");
	OLECMDTEXTFc.cwActual = (*env)->GetFieldID(env, OLECMDTEXTFc.clazz, "cwActual", "I");
	OLECMDTEXTFc.cwBuf = (*env)->GetFieldID(env, OLECMDTEXTFc.clazz, "cwBuf", "I");
	OLECMDTEXTFc.rgwz = (*env)->GetFieldID(env, OLECMDTEXTFc.clazz, "rgwz", "[S");
	OLECMDTEXTFc.cached = 1;
}

OLECMDTEXT *getOLECMDTEXTFields(JNIEnv *env, jobject lpObject, OLECMDTEXT *lpStruct)
{
	if (!OLECMDTEXTFc.cached) cacheOLECMDTEXTFields(env, lpObject);
	lpStruct->cmdtextf = (*env)->GetIntField(env, lpObject, OLECMDTEXTFc.cmdtextf);
	lpStruct->cwActual = (*env)->GetIntField(env, lpObject, OLECMDTEXTFc.cwActual);
	lpStruct->cwBuf = (*env)->GetIntField(env, lpObject, OLECMDTEXTFc.cwBuf);
	{
	jshortArray lpObject1 = (jshortArray)(*env)->GetObjectField(env, lpObject, OLECMDTEXTFc.rgwz);
	(*env)->GetShortArrayRegion(env, lpObject1, 0, sizeof(lpStruct->rgwz[0]) / 2, (jshort *)lpStruct->rgwz[0]);
	}
	return lpStruct;
}

void setOLECMDTEXTFields(JNIEnv *env, jobject lpObject, OLECMDTEXT *lpStruct)
{
	if (!OLECMDTEXTFc.cached) cacheOLECMDTEXTFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, OLECMDTEXTFc.cmdtextf, (jint)lpStruct->cmdtextf);
	(*env)->SetIntField(env, lpObject, OLECMDTEXTFc.cwActual, (jint)lpStruct->cwActual);
	(*env)->SetIntField(env, lpObject, OLECMDTEXTFc.cwBuf, (jint)lpStruct->cwBuf);
	{
	jshortArray lpObject1 = (jshortArray)(*env)->GetObjectField(env, lpObject, OLECMDTEXTFc.rgwz);
	(*env)->SetShortArrayRegion(env, lpObject1, 0, sizeof(lpStruct->rgwz[0]) / 2, (jshort *)lpStruct->rgwz[0]);
	}
}
#endif

#ifndef NO_OLEINPLACEFRAMEINFO
typedef struct OLEINPLACEFRAMEINFO_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID cb, fMDIApp, hwndFrame, haccel, cAccelEntries;
} OLEINPLACEFRAMEINFO_FID_CACHE;

OLEINPLACEFRAMEINFO_FID_CACHE OLEINPLACEFRAMEINFOFc;

void cacheOLEINPLACEFRAMEINFOFields(JNIEnv *env, jobject lpObject)
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
	if (!OLEINPLACEFRAMEINFOFc.cached) cacheOLEINPLACEFRAMEINFOFields(env, lpObject);
	lpStruct->cb = (*env)->GetIntField(env, lpObject, OLEINPLACEFRAMEINFOFc.cb);
	lpStruct->fMDIApp = (*env)->GetIntField(env, lpObject, OLEINPLACEFRAMEINFOFc.fMDIApp);
	lpStruct->hwndFrame = (HWND)(*env)->GetIntField(env, lpObject, OLEINPLACEFRAMEINFOFc.hwndFrame);
	lpStruct->haccel = (HACCEL)(*env)->GetIntField(env, lpObject, OLEINPLACEFRAMEINFOFc.haccel);
	lpStruct->cAccelEntries = (*env)->GetIntField(env, lpObject, OLEINPLACEFRAMEINFOFc.cAccelEntries);
	return lpStruct;
}

void setOLEINPLACEFRAMEINFOFields(JNIEnv *env, jobject lpObject, OLEINPLACEFRAMEINFO *lpStruct)
{
	if (!OLEINPLACEFRAMEINFOFc.cached) cacheOLEINPLACEFRAMEINFOFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, OLEINPLACEFRAMEINFOFc.cb, (jint)lpStruct->cb);
	(*env)->SetIntField(env, lpObject, OLEINPLACEFRAMEINFOFc.fMDIApp, (jint)lpStruct->fMDIApp);
	(*env)->SetIntField(env, lpObject, OLEINPLACEFRAMEINFOFc.hwndFrame, (jint)lpStruct->hwndFrame);
	(*env)->SetIntField(env, lpObject, OLEINPLACEFRAMEINFOFc.haccel, (jint)lpStruct->haccel);
	(*env)->SetIntField(env, lpObject, OLEINPLACEFRAMEINFOFc.cAccelEntries, (jint)lpStruct->cAccelEntries);
}
#endif

#ifndef NO_STATSTG
typedef struct STATSTG_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID pwcsName, type, cbSize, mtime_dwLowDateTime, mtime_dwHighDateTime, ctime_dwLowDateTime, ctime_dwHighDateTime, atime_dwLowDateTime, atime_dwHighDateTime, grfMode, grfLocksSupported, clsid_Data1, clsid_Data2, clsid_Data3, clsid_Data4, grfStateBits, reserved;
} STATSTG_FID_CACHE;

STATSTG_FID_CACHE STATSTGFc;

void cacheSTATSTGFields(JNIEnv *env, jobject lpObject)
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
	STATSTGFc.clsid_Data1 = (*env)->GetFieldID(env, STATSTGFc.clazz, "clsid_Data1", "I");
	STATSTGFc.clsid_Data2 = (*env)->GetFieldID(env, STATSTGFc.clazz, "clsid_Data2", "S");
	STATSTGFc.clsid_Data3 = (*env)->GetFieldID(env, STATSTGFc.clazz, "clsid_Data3", "S");
	STATSTGFc.clsid_Data4 = (*env)->GetFieldID(env, STATSTGFc.clazz, "clsid_Data4", "[B");
	STATSTGFc.grfStateBits = (*env)->GetFieldID(env, STATSTGFc.clazz, "grfStateBits", "I");
	STATSTGFc.reserved = (*env)->GetFieldID(env, STATSTGFc.clazz, "reserved", "I");
	STATSTGFc.cached = 1;
}

STATSTG *getSTATSTGFields(JNIEnv *env, jobject lpObject, STATSTG *lpStruct)
{
	if (!STATSTGFc.cached) cacheSTATSTGFields(env, lpObject);
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
	lpStruct->clsid.Data1 = (*env)->GetIntField(env, lpObject, STATSTGFc.clsid_Data1);
	lpStruct->clsid.Data2 = (*env)->GetShortField(env, lpObject, STATSTGFc.clsid_Data2);
	lpStruct->clsid.Data3 = (*env)->GetShortField(env, lpObject, STATSTGFc.clsid_Data3);
	{
	jbyteArray lpObject1 = (jbyteArray)(*env)->GetObjectField(env, lpObject, STATSTGFc.clsid_Data4);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->clsid.Data4), (jbyte *)lpStruct->clsid.Data4);
	}
	lpStruct->grfStateBits = (*env)->GetIntField(env, lpObject, STATSTGFc.grfStateBits);
	lpStruct->reserved = (*env)->GetIntField(env, lpObject, STATSTGFc.reserved);
	return lpStruct;
}

void setSTATSTGFields(JNIEnv *env, jobject lpObject, STATSTG *lpStruct)
{
	if (!STATSTGFc.cached) cacheSTATSTGFields(env, lpObject);
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
	(*env)->SetIntField(env, lpObject, STATSTGFc.clsid_Data1, (jint)lpStruct->clsid.Data1);
	(*env)->SetShortField(env, lpObject, STATSTGFc.clsid_Data2, (jshort)lpStruct->clsid.Data2);
	(*env)->SetShortField(env, lpObject, STATSTGFc.clsid_Data3, (jshort)lpStruct->clsid.Data3);
	{
	jbyteArray lpObject1 = (jbyteArray)(*env)->GetObjectField(env, lpObject, STATSTGFc.clsid_Data4);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->clsid.Data4), (jbyte *)lpStruct->clsid.Data4);
	}
	(*env)->SetIntField(env, lpObject, STATSTGFc.grfStateBits, (jint)lpStruct->grfStateBits);
	(*env)->SetIntField(env, lpObject, STATSTGFc.reserved, (jint)lpStruct->reserved);
}
#endif

#ifndef NO_STGMEDIUM
typedef struct STGMEDIUM_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID tymed, unionField, pUnkForRelease;
} STGMEDIUM_FID_CACHE;

STGMEDIUM_FID_CACHE STGMEDIUMFc;

void cacheSTGMEDIUMFields(JNIEnv *env, jobject lpObject)
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
	if (!STGMEDIUMFc.cached) cacheSTGMEDIUMFields(env, lpObject);
	lpStruct->tymed = (*env)->GetIntField(env, lpObject, STGMEDIUMFc.tymed);
	lpStruct->hGlobal = (HGLOBAL)(*env)->GetIntField(env, lpObject, STGMEDIUMFc.unionField);
	lpStruct->pUnkForRelease = (IUnknown *)(*env)->GetIntField(env, lpObject, STGMEDIUMFc.pUnkForRelease);
	return lpStruct;
}

void setSTGMEDIUMFields(JNIEnv *env, jobject lpObject, STGMEDIUM *lpStruct)
{
	if (!STGMEDIUMFc.cached) cacheSTGMEDIUMFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, STGMEDIUMFc.tymed, (jint)lpStruct->tymed);
	(*env)->SetIntField(env, lpObject, STGMEDIUMFc.unionField, (jint)lpStruct->hGlobal);
	(*env)->SetIntField(env, lpObject, STGMEDIUMFc.pUnkForRelease, (jint)lpStruct->pUnkForRelease);
}
#endif

#ifndef NO_TYPEATTR
typedef struct TYPEATTR_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID guid_Data1, guid_Data2, guid_Data3, guid_Data4, lcid, dwReserved, memidConstructor, memidDestructor, lpstrSchema, cbSizeInstance, typekind, cFuncs, cVars, cImplTypes, cbSizeVft, cbAlignment, wTypeFlags, wMajorVerNum, wMinorVerNum, tdescAlias_unionField, tdescAlias_vt, idldescType_dwReserved, idldescType_wIDLFlags;
} TYPEATTR_FID_CACHE;

TYPEATTR_FID_CACHE TYPEATTRFc;

void cacheTYPEATTRFields(JNIEnv *env, jobject lpObject)
{
	if (TYPEATTRFc.cached) return;
	TYPEATTRFc.clazz = (*env)->GetObjectClass(env, lpObject);
	TYPEATTRFc.guid_Data1 = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "guid_Data1", "I");
	TYPEATTRFc.guid_Data2 = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "guid_Data2", "S");
	TYPEATTRFc.guid_Data3 = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "guid_Data3", "S");
	TYPEATTRFc.guid_Data4 = (*env)->GetFieldID(env, TYPEATTRFc.clazz, "guid_Data4", "[B");
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
	if (!TYPEATTRFc.cached) cacheTYPEATTRFields(env, lpObject);
	lpStruct->guid.Data1 = (*env)->GetIntField(env, lpObject, TYPEATTRFc.guid_Data1);
	lpStruct->guid.Data2 = (*env)->GetShortField(env, lpObject, TYPEATTRFc.guid_Data2);
	lpStruct->guid.Data3 = (*env)->GetShortField(env, lpObject, TYPEATTRFc.guid_Data3);
	{
	jbyteArray lpObject1 = (jbyteArray)(*env)->GetObjectField(env, lpObject, TYPEATTRFc.guid_Data4);
	(*env)->GetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->guid.Data4), (jbyte *)lpStruct->guid.Data4);
	}
	lpStruct->lcid = (*env)->GetIntField(env, lpObject, TYPEATTRFc.lcid);
	lpStruct->dwReserved = (*env)->GetIntField(env, lpObject, TYPEATTRFc.dwReserved);
	lpStruct->memidConstructor = (*env)->GetIntField(env, lpObject, TYPEATTRFc.memidConstructor);
	lpStruct->memidDestructor = (*env)->GetIntField(env, lpObject, TYPEATTRFc.memidDestructor);
	lpStruct->lpstrSchema = (OLECHAR FAR *)(*env)->GetIntField(env, lpObject, TYPEATTRFc.lpstrSchema);
	lpStruct->cbSizeInstance = (*env)->GetIntField(env, lpObject, TYPEATTRFc.cbSizeInstance);
	lpStruct->typekind = (*env)->GetIntField(env, lpObject, TYPEATTRFc.typekind);
	lpStruct->cFuncs = (*env)->GetShortField(env, lpObject, TYPEATTRFc.cFuncs);
	lpStruct->cVars = (*env)->GetShortField(env, lpObject, TYPEATTRFc.cVars);
	lpStruct->cImplTypes = (*env)->GetShortField(env, lpObject, TYPEATTRFc.cImplTypes);
	lpStruct->cbSizeVft = (*env)->GetShortField(env, lpObject, TYPEATTRFc.cbSizeVft);
	lpStruct->cbAlignment = (*env)->GetShortField(env, lpObject, TYPEATTRFc.cbAlignment);
	lpStruct->wTypeFlags = (*env)->GetShortField(env, lpObject, TYPEATTRFc.wTypeFlags);
	lpStruct->wMajorVerNum = (*env)->GetShortField(env, lpObject, TYPEATTRFc.wMajorVerNum);
	lpStruct->wMinorVerNum = (*env)->GetShortField(env, lpObject, TYPEATTRFc.wMinorVerNum);
	lpStruct->tdescAlias.lptdesc = (struct FARSTRUCT tagTYPEDESC FAR *)(*env)->GetIntField(env, lpObject, TYPEATTRFc.tdescAlias_unionField);
	lpStruct->tdescAlias.vt = (*env)->GetShortField(env, lpObject, TYPEATTRFc.tdescAlias_vt);
	lpStruct->idldescType.dwReserved = (*env)->GetIntField(env, lpObject, TYPEATTRFc.idldescType_dwReserved);
	lpStruct->idldescType.wIDLFlags = (*env)->GetShortField(env, lpObject, TYPEATTRFc.idldescType_wIDLFlags);
	return lpStruct;
}

void setTYPEATTRFields(JNIEnv *env, jobject lpObject, TYPEATTR *lpStruct)
{
	if (!TYPEATTRFc.cached) cacheTYPEATTRFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, TYPEATTRFc.guid_Data1, (jint)lpStruct->guid.Data1);
	(*env)->SetShortField(env, lpObject, TYPEATTRFc.guid_Data2, (jshort)lpStruct->guid.Data2);
	(*env)->SetShortField(env, lpObject, TYPEATTRFc.guid_Data3, (jshort)lpStruct->guid.Data3);
	{
	jbyteArray lpObject1 = (jbyteArray)(*env)->GetObjectField(env, lpObject, TYPEATTRFc.guid_Data4);
	(*env)->SetByteArrayRegion(env, lpObject1, 0, sizeof(lpStruct->guid.Data4), (jbyte *)lpStruct->guid.Data4);
	}
	(*env)->SetIntField(env, lpObject, TYPEATTRFc.lcid, (jint)lpStruct->lcid);
	(*env)->SetIntField(env, lpObject, TYPEATTRFc.dwReserved, (jint)lpStruct->dwReserved);
	(*env)->SetIntField(env, lpObject, TYPEATTRFc.memidConstructor, (jint)lpStruct->memidConstructor);
	(*env)->SetIntField(env, lpObject, TYPEATTRFc.memidDestructor, (jint)lpStruct->memidDestructor);
	(*env)->SetIntField(env, lpObject, TYPEATTRFc.lpstrSchema, (jint)lpStruct->lpstrSchema);
	(*env)->SetIntField(env, lpObject, TYPEATTRFc.cbSizeInstance, (jint)lpStruct->cbSizeInstance);
	(*env)->SetIntField(env, lpObject, TYPEATTRFc.typekind, (jint)lpStruct->typekind);
	(*env)->SetShortField(env, lpObject, TYPEATTRFc.cFuncs, (jshort)lpStruct->cFuncs);
	(*env)->SetShortField(env, lpObject, TYPEATTRFc.cVars, (jshort)lpStruct->cVars);
	(*env)->SetShortField(env, lpObject, TYPEATTRFc.cImplTypes, (jshort)lpStruct->cImplTypes);
	(*env)->SetShortField(env, lpObject, TYPEATTRFc.cbSizeVft, (jshort)lpStruct->cbSizeVft);
	(*env)->SetShortField(env, lpObject, TYPEATTRFc.cbAlignment, (jshort)lpStruct->cbAlignment);
	(*env)->SetShortField(env, lpObject, TYPEATTRFc.wTypeFlags, (jshort)lpStruct->wTypeFlags);
	(*env)->SetShortField(env, lpObject, TYPEATTRFc.wMajorVerNum, (jshort)lpStruct->wMajorVerNum);
	(*env)->SetShortField(env, lpObject, TYPEATTRFc.wMinorVerNum, (jshort)lpStruct->wMinorVerNum);
	(*env)->SetIntField(env, lpObject, TYPEATTRFc.tdescAlias_unionField, (jint)lpStruct->tdescAlias.lptdesc);
	(*env)->SetShortField(env, lpObject, TYPEATTRFc.tdescAlias_vt, (jshort)lpStruct->tdescAlias.vt);
	(*env)->SetIntField(env, lpObject, TYPEATTRFc.idldescType_dwReserved, (jint)lpStruct->idldescType.dwReserved);
	(*env)->SetShortField(env, lpObject, TYPEATTRFc.idldescType_wIDLFlags, (jshort)lpStruct->idldescType.wIDLFlags);
}
#endif

#ifndef NO_VARDESC
typedef struct VARDESC_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID memid, lpstrSchema, oInst, elemdescVar_tdesc_union, elemdescVar_tdesc_vt, elemdescVar_paramdesc_pparamdescex, elemdescVar_paramdesc_wParamFlags, wVarFlags, varkind;
} VARDESC_FID_CACHE;

VARDESC_FID_CACHE VARDESCFc;

void cacheVARDESCFields(JNIEnv *env, jobject lpObject)
{
	if (VARDESCFc.cached) return;
	VARDESCFc.clazz = (*env)->GetObjectClass(env, lpObject);
	VARDESCFc.memid = (*env)->GetFieldID(env, VARDESCFc.clazz, "memid", "I");
	VARDESCFc.lpstrSchema = (*env)->GetFieldID(env, VARDESCFc.clazz, "lpstrSchema", "I");
	VARDESCFc.oInst = (*env)->GetFieldID(env, VARDESCFc.clazz, "oInst", "I");
	VARDESCFc.elemdescVar_tdesc_union = (*env)->GetFieldID(env, VARDESCFc.clazz, "elemdescVar_tdesc_union", "I");
	VARDESCFc.elemdescVar_tdesc_vt = (*env)->GetFieldID(env, VARDESCFc.clazz, "elemdescVar_tdesc_vt", "S");
	VARDESCFc.elemdescVar_paramdesc_pparamdescex = (*env)->GetFieldID(env, VARDESCFc.clazz, "elemdescVar_paramdesc_pparamdescex", "I");
	VARDESCFc.elemdescVar_paramdesc_wParamFlags = (*env)->GetFieldID(env, VARDESCFc.clazz, "elemdescVar_paramdesc_wParamFlags", "S");
	VARDESCFc.wVarFlags = (*env)->GetFieldID(env, VARDESCFc.clazz, "wVarFlags", "S");
	VARDESCFc.varkind = (*env)->GetFieldID(env, VARDESCFc.clazz, "varkind", "I");
	VARDESCFc.cached = 1;
}

VARDESC *getVARDESCFields(JNIEnv *env, jobject lpObject, VARDESC *lpStruct)
{
	if (!VARDESCFc.cached) cacheVARDESCFields(env, lpObject);
	lpStruct->memid = (*env)->GetIntField(env, lpObject, VARDESCFc.memid);
	lpStruct->lpstrSchema = (OLECHAR FAR *)(*env)->GetIntField(env, lpObject, VARDESCFc.lpstrSchema);
	lpStruct->oInst = (*env)->GetIntField(env, lpObject, VARDESCFc.oInst);
	lpStruct->elemdescVar.tdesc.lptdesc = (struct FARSTRUCT tagTYPEDESC FAR *)(*env)->GetIntField(env, lpObject, VARDESCFc.elemdescVar_tdesc_union);
	lpStruct->elemdescVar.tdesc.vt = (*env)->GetShortField(env, lpObject, VARDESCFc.elemdescVar_tdesc_vt);
	lpStruct->elemdescVar.paramdesc.pparamdescex = (LPPARAMDESCEX)(*env)->GetIntField(env, lpObject, VARDESCFc.elemdescVar_paramdesc_pparamdescex);
	lpStruct->elemdescVar.paramdesc.wParamFlags = (*env)->GetShortField(env, lpObject, VARDESCFc.elemdescVar_paramdesc_wParamFlags);
	lpStruct->wVarFlags = (*env)->GetShortField(env, lpObject, VARDESCFc.wVarFlags);
	lpStruct->varkind = (*env)->GetIntField(env, lpObject, VARDESCFc.varkind);
	return lpStruct;
}

void setVARDESCFields(JNIEnv *env, jobject lpObject, VARDESC *lpStruct)
{
	if (!VARDESCFc.cached) cacheVARDESCFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, VARDESCFc.memid, (jint)lpStruct->memid);
	(*env)->SetIntField(env, lpObject, VARDESCFc.lpstrSchema, (jint)lpStruct->lpstrSchema);
	(*env)->SetIntField(env, lpObject, VARDESCFc.oInst, (jint)lpStruct->oInst);
	(*env)->SetIntField(env, lpObject, VARDESCFc.elemdescVar_tdesc_union, (jint)lpStruct->elemdescVar.tdesc.lptdesc);
	(*env)->SetShortField(env, lpObject, VARDESCFc.elemdescVar_tdesc_vt, (jshort)lpStruct->elemdescVar.tdesc.vt);
	(*env)->SetIntField(env, lpObject, VARDESCFc.elemdescVar_paramdesc_pparamdescex, (jint)lpStruct->elemdescVar.paramdesc.pparamdescex);
	(*env)->SetShortField(env, lpObject, VARDESCFc.elemdescVar_paramdesc_wParamFlags, (jshort)lpStruct->elemdescVar.paramdesc.wParamFlags);
	(*env)->SetShortField(env, lpObject, VARDESCFc.wVarFlags, (jshort)lpStruct->wVarFlags);
	(*env)->SetIntField(env, lpObject, VARDESCFc.varkind, (jint)lpStruct->varkind);
}
#endif

