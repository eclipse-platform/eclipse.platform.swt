/*
 * (c) Copyright IBM Corp., 2000, 2001
 * All Rights Reserved.
 */

#include "globals.h"
#include "structs.h"

#define cacheGuidFids cacheGUIDFids
#define getGuidFields getGUIDFields
#define setGuidFields setGUIDFields
#define GuidFc GUIDFc
#define cacheDropfilesFids cacheDROPFILESFids
#define getDropfilesFields getDROPFILESFields
#define setDropfilesFields setDROPFILESFields
#define DropfilesFc DROPFILESFc
#define cacheFormatetcFids cacheFORMATETCFids
#define getFormatetcFields getFORMATETCFields
#define setFormatetcFields setFORMATETCFields
#define FormatetcFc FORMATETCFc
#define cacheOleinplaceframeinfoFids cacheOLEINPLACEFRAMEINFOFids
#define getOleinplaceframeinfoFields getOLEINPLACEFRAMEINFOFields
#define setOleinplaceframeinfoFields setOLEINPLACEFRAMEINFOFields
#define OleinplaceframeinfoFc OLEINPLACEFRAMEINFOFc
#define cacheStatstgFids cacheSTATSTGFids
#define getStatstgFields getSTATSTGFields
#define setStatstgFields setSTATSTGFields
#define StatstgFc STATSTGFc
#define cacheStgmediumFids cacheSTGMEDIUMFids
#define getStgmediumFields getSTGMEDIUMFields
#define setStgmediumFields setSTGMEDIUMFields
#define StgmediumFc STGMEDIUMFc
#define cacheDispparamsFids cacheDISPPARAMSFids
#define getDispparamsFields getDISPPARAMSFields
#define setDispparamsFields setDISPPARAMSFields
#define DispparamsFc DISPPARAMSFc
#define cacheTypeattrFids cacheTYPEATTRFids
#define getTypeattrFields getTYPEATTRFields
#define setTypeattrFields setTYPEATTRFields
#define TypeattrFc TYPEATTRFc
#define cacheRectFids cacheRECTFids
#define getRectFields getRECTFields
#define setRectFields setRECTFields
#define RectFc RECTFc
#define cacheFuncdesc1Fids cacheFUNCDESC1Fids
#define getFuncdesc1Fields getFUNCDESC1Fields
#define setFuncdesc1Fields setFUNCDESC1Fields
#define Funcdesc1Fc FUNCDESC1Fc
#define cacheFuncdesc2Fids cacheFUNCDESC2Fids
#define getFuncdesc2Fields getFUNCDESC2Fields
#define setFuncdesc2Fields setFUNCDESC2Fields
#define Funcdesc2Fc FUNCDESC2Fc
#define cacheVardesc1Fids cacheVARDESC1Fids
#define getVardesc1Fields getVARDESC1Fields
#define setVardesc1Fields setVARDESC1Fields
#define Vardesc1Fc VARDESC1Fc
#define cacheVardesc2Fids cacheVARDESC2Fids
#define getVardesc2Fields getVARDESC2Fields
#define setVardesc2Fields setVARDESC2Fields
#define Vardesc2Fc VARDESC2Fc
#define cacheDvtargetdeviceFids cacheDVTARGETDEVICEFids
#define getDvtargetdeviceFields getDVTARGETDEVICEFields
#define setDvtargetdeviceFields setDVTARGETDEVICEFields
#define DvtargetdeviceFc DVTARGETDEVICEFc
#define cacheSizeFids cacheSIZEFids
#define getSizeFields getSIZEFields
#define setSizeFields setSIZEFields
#define SizeFc SIZEFc
#define cacheExcepinfoFids cacheEXCEPINFOFids
#define getExcepinfoFields getEXCEPINFOFields
#define setExcepinfoFields setEXCEPINFOFields
#define ExcepinfoFc EXCEPINFOFc
#define cacheMsgFids cacheMSGFids
#define getMsgFields getMSGFields
#define setMsgFields setMSGFields
#define MsgFc MSGFc
#define cacheCauuidFids cacheCAUUIDFids
#define getCauuidFields getCAUUIDFields
#define setCauuidFields setCAUUIDFields
#define CauuidFc CAUUIDFc
#define cacheControlinfoFids cacheCONTROLINFOFids
#define getControlinfoFields getCONTROLINFOFields
#define setControlinfoFields setCONTROLINFOFields
#define ControlinfoFc CONTROLINFOFc
#define cacheOlecmdFids cacheOLECMDFids
#define getOlecmdFields getOLECMDFields
#define setOlecmdFields setOLECMDFields
#define OlecmdFc OLECMDFc
#define cacheOlecmdtextFids cacheOLECMDTEXTFids
#define getOlecmdtextFields getOLECMDTEXTFields
#define setOlecmdtextFields setOLECMDTEXTFields
#define OlecmdtextFc OLECMDTEXTFc
#define cacheLicinfoFids cacheLICINFOFids
#define getLicinfoFields getLICINFOFields
#define setLicinfoFields setLICINFOFields
#define LicinfoFc LICINFOFc

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    CLSIDFromProgID
 * Signature: ([CLorg/eclipse/swt/internal/ole/win32/GUID;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_CLSIDFromProgID
  (JNIEnv *env, jclass that, jcharArray lpszProgID, jobject pclsid)
{
	DECL_GLOB(pGlob)
    LPCOLESTR lpszProgID1=NULL;
    GUID guid, *pclsid1=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "CLSIDFromProgID\n");
#endif

    if (pclsid) {
        pclsid1=&guid;
        cacheGuidFids(env, pclsid, &PGLOB(GuidFc));
        getGuidFields(env, pclsid, pclsid1, &PGLOB(GuidFc));
    }

    if (lpszProgID)
        lpszProgID1 = (LPCOLESTR)(*env)->GetCharArrayElements(env, lpszProgID, NULL);

    rc = (jint) CLSIDFromProgID(lpszProgID1, pclsid1);

    if (lpszProgID)
        (*env)->ReleaseCharArrayElements(env, lpszProgID, (jchar *)lpszProgID1, 0);
    
    if (pclsid) {
        setGuidFields(env, pclsid, pclsid1, &PGLOB(GuidFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    CLSIDFromString
 * Signature: ([CLorg/eclipse/swt/internal/ole/win32/GUID;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_CLSIDFromString
  (JNIEnv *env, jclass that, jcharArray lpsz, jobject pclsid)
{
	DECL_GLOB(pGlob)
    LPOLESTR lpsz1=NULL;
    GUID guid, *pclsid1=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "CLSIDFromString\n");
#endif
    if (pclsid) {
        pclsid1=&guid;
        cacheGuidFids(env, pclsid, &PGLOB(GuidFc));
        getGuidFields(env, pclsid, pclsid1, &PGLOB(GuidFc));
    }
    
    if (lpsz)
        lpsz1 = (LPOLESTR)(*env)->GetCharArrayElements(env, lpsz, NULL);

    rc = CLSIDFromString(lpsz1, pclsid1);

    if (lpsz)
        (*env)->ReleaseCharArrayElements(env, lpsz, lpsz1, 0);

    if (pclsid) {
        setGuidFields(env, pclsid, pclsid1, &PGLOB(GuidFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    CoCreateInstance
 * Signature: (Lorg/eclipse/swt/internal/ole/win32/GUID;IILorg/eclipse/swt/internal/ole/win32/GUID;[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_CoCreateInstance
  (JNIEnv *env, jclass that, jobject rclsid, jint pUnkOuter, jint dwClsContext, jobject riid, jintArray ppv)
{
	DECL_GLOB(pGlob)
    LPVOID *ppv1=NULL;
    GUID guid1, guid2, *riid1=NULL, *rclsid1=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "CoCreateInstance\n");
#endif

    if (rclsid) {
        rclsid1=&guid2;
        cacheGuidFids(env, rclsid, &PGLOB(GuidFc));
        getGuidFields(env, rclsid, rclsid1, &PGLOB(GuidFc));
    }
    if (riid) {
        riid1=&guid1;
        cacheGuidFids(env, riid, &PGLOB(GuidFc));
        getGuidFields(env, riid, riid1, &PGLOB(GuidFc));
    }
    
    if (ppv)
        ppv1 = (LPVOID *)(*env)->GetIntArrayElements(env, ppv, NULL);

    rc = (jint) CoCreateInstance(rclsid1, (LPUNKNOWN)pUnkOuter, dwClsContext, riid1, ppv1);

    if (ppv)
        (*env)->ReleaseIntArrayElements(env, ppv, (jint *)ppv1, 0);

    if (rclsid) {
        setGuidFields(env, rclsid, rclsid1, &PGLOB(GuidFc));
    }
    if (riid) {
        setGuidFields(env, riid, riid1, &PGLOB(GuidFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    CoFreeUnusedLibraries
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_CoFreeUnusedLibraries
  (JNIEnv *env, jclass that)
{
    CoFreeUnusedLibraries();
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    CoGetClassObject
 * Signature: (Lorg/eclipse/swt/internal/ole/win32/GUID;IILorg/eclipse/swt/internal/ole/win32/GUID;[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_CoGetClassObject
  (JNIEnv *env, jclass that, jobject rclsid, jint dwClsContext, jint pServerInfo, 
                    jobject riid, jintArray ppv)
{
	DECL_GLOB(pGlob)
    LPVOID *ppv1=NULL;
    GUID guid1, *rclsid1=NULL;
    GUID guid2, *riid1=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "CoGetClassObject\n");
#endif

    if (rclsid) {
        rclsid1=&guid1;
        cacheGuidFids(env, rclsid, &PGLOB(GuidFc));
        getGuidFields(env, rclsid, rclsid1, &PGLOB(GuidFc));
    }
    if (riid) {
        riid1=&guid2;
        cacheGuidFids(env, riid, &PGLOB(GuidFc));
        getGuidFields(env, riid, riid1, &PGLOB(GuidFc));
    }
    if (ppv)
        ppv1 = (LPVOID *)(*env)->GetIntArrayElements(env, ppv, NULL);

    rc = (jint) CoGetClassObject(rclsid1, dwClsContext, (COSERVERINFO *)pServerInfo, riid1, ppv1);
    if (ppv)
        (*env)->ReleaseIntArrayElements(env, ppv, (jint *)ppv1, 0);

    if (rclsid) {
        setGuidFields(env, rclsid, rclsid1, &PGLOB(GuidFc));
    }
    if (riid) {
        setGuidFields(env, riid, riid1, &PGLOB(GuidFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    CoLockObjectExternal
 * Signature: (IZZ)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_CoLockObjectExternal
  (JNIEnv *env, jclass that, jint pUnk, jboolean fLock, jboolean fLastUnlockReleases)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "CoLockObjectExternal\n");
#endif
	return (jint) CoLockObjectExternal((IUnknown *)pUnk, (BOOL)fLock, (BOOL)fLastUnlockReleases);
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    CoTaskMemAlloc
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_CoTaskMemAlloc
  (JNIEnv *env, jclass that, jint cb)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "CoTaskMemAlloc\n");
#endif
    return (jint) CoTaskMemAlloc((ULONG)cb);
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    CoTaskMemFree
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_CoTaskMemFree
  (JNIEnv *env, jclass that, jint pv)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "CoTaskMemFree\n");
#endif
    CoTaskMemFree((LPVOID)pv);
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    DoDragDrop
 * Signature: (III[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_DoDragDrop
  (JNIEnv *env, jclass that, jint pDataObject, jint pDropSource, jint dwOKEffect, jintArray pdwEffect)
{
    jint *pdwEffect1=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "DoDragDrop\n");
#endif

    if (pdwEffect)
        pdwEffect1 = (*env)->GetIntArrayElements(env, pdwEffect, NULL);

    rc = DoDragDrop((IDataObject *)pDataObject, (IDropSource *)pDropSource, dwOKEffect, pdwEffect1);

    if (pdwEffect)
        (*env)->ReleaseIntArrayElements(env, pdwEffect, pdwEffect1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    DragFinish
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_DragFinish
  (JNIEnv *env, jclass that, jint hDrop)
{
    DragFinish((HDROP)hDrop);
}


/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    DragQueryFile
 * Signature: (II[BI)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_DragQueryFile
  (JNIEnv *env, jclass that, jint hDrop, jint iFile, jbyteArray lpszFile, jint cch)
{
    LPTSTR lpszFile1=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "DragQueryFile\n");
#endif

    if (lpszFile)
        lpszFile1 = (*env)->GetByteArrayElements(env, lpszFile, NULL);

    rc = (jint) DragQueryFile((HDROP)hDrop, iFile, lpszFile1, cch);

    if (lpszFile)
        (*env)->ReleaseByteArrayElements(env, lpszFile, (jbyte *)lpszFile1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    GetClassFile
 * Signature: ([CLorg/eclipse/swt/internal/ole/win32/GUID;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_GetClassFile
  (JNIEnv *env, jclass that, jcharArray szFileName, jobject clsid)
{
	DECL_GLOB(pGlob)
    LPCWSTR szFileName1=NULL;
    GUID guid, *clsid1=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "GetClassFile\n");
#endif
    if (clsid) {
        clsid1=&guid;
        cacheGuidFids(env, clsid, &PGLOB(GuidFc));
        getGuidFields(env, clsid, clsid1, &PGLOB(GuidFc));
    }
    
    if (szFileName)
        szFileName1 = (LPCWSTR)(*env)->GetCharArrayElements(env, szFileName, NULL);

    rc = GetClassFile(szFileName1, clsid1);

    if (szFileName)
        (*env)->ReleaseCharArrayElements(env, szFileName, (jchar *)szFileName1, 0);

    if (clsid) {
        setGuidFields(env, clsid, clsid1, &PGLOB(GuidFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    GetClipboardFormatName
 * Signature: (I[BI)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_GetClipboardFormatName
  (JNIEnv *env, jclass that, jint format, jbyteArray lpszFormatName, jint cchMaxCount)
{
    LPTSTR lpszFormatName1=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "GetClipboardFormatName\n");
#endif

    if (lpszFormatName)
        lpszFormatName1 = (*env)->GetByteArrayElements(env, lpszFormatName, NULL);

    rc = (jint) GetClipboardFormatName(format, lpszFormatName1, cchMaxCount);

    if (lpszFormatName)
        (*env)->ReleaseByteArrayElements(env, lpszFormatName, (jbyte *)lpszFormatName1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    IIDFromString
 * Signature: ([CLorg/eclipse/swt/internal/ole/win32/GUID;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_IIDFromString
  (JNIEnv *env, jclass that, jcharArray lpsz, jobject lpiid)
{
	DECL_GLOB(pGlob)
    LPOLESTR lpsz1=NULL;
    GUID guid, *lpiid1=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "IIDFromString\n");
#endif

    if (lpsz)
        lpsz1 = (LPOLESTR)(*env)->GetCharArrayElements(env, lpsz, NULL);
        
    if (lpiid) {
        lpiid1=&guid;
        cacheGuidFids(env, lpiid, &PGLOB(GuidFc));
        getGuidFields(env, lpiid, lpiid1, &PGLOB(GuidFc));
    }

    rc = (jint) IIDFromString(lpsz1, lpiid1);

    if (lpiid) {
        setGuidFields(env, lpiid, lpiid1, &PGLOB(GuidFc));
    }

    if (lpsz)
        (*env)->ReleaseCharArrayElements(env, lpsz, (jchar *)lpsz1, 0);

    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    IsEqualGUID
 * Signature: (Lorg/eclipse/swt/internal/ole/win32/GUID;Lorg/eclipse/swt/internal/ole/win32/GUID;)Z
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_IsEqualGUID
  (JNIEnv *env, jclass that, jobject rguid1, jobject rguid2)
{
	DECL_GLOB(pGlob)
    GUID guid1, *rguid11=NULL;
    GUID guid2, *rguid21=NULL;
    jboolean rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "IsEqualGUID\n");
#endif

    if (rguid1) {
        rguid11=&guid1;
        cacheGuidFids(env, rguid1, &PGLOB(GuidFc));
        getGuidFields(env, rguid1, rguid11, &PGLOB(GuidFc));
    }

    if (rguid2) {
        rguid21=&guid2;
        cacheGuidFids(env, rguid2, &PGLOB(GuidFc));
        getGuidFields(env, rguid2, rguid21, &PGLOB(GuidFc));
    }

    rc = (jboolean) IsEqualGUID(rguid11, rguid21);

    if (rguid1) {
        setGuidFields(env, rguid1, rguid11, &PGLOB(GuidFc));
    }
    if (rguid2) {
        setGuidFields(env, rguid2, rguid21, &PGLOB(GuidFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    MoveMemory
 * Signature: ([DII)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_MoveMemory___3DII
  (JNIEnv *env, jclass that, jdoubleArray Destination, jint SourcePtr, jint Length)
{
    jdouble *Destination1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_MoveMemory___3DII\n");
#endif

    if (Destination)
        Destination1 = (*env)->GetDoubleArrayElements(env, Destination, NULL);

    MoveMemory(Destination1, (CONST VOID *)SourcePtr, Length);

    if (Destination)
        (*env)->ReleaseDoubleArrayElements(env, Destination, Destination1, 0);
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    MoveMemory
 * Signature: ([FII)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_MoveMemory___3FII
  (JNIEnv *env, jclass that, jfloatArray Destination, jint SourcePtr, jint Length)
{
    jfloat *Destination1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_MoveMemory___3FII\n");
#endif

    if (Destination)
        Destination1 = (*env)->GetFloatArrayElements(env, Destination, NULL);

    MoveMemory(Destination1, (CONST VOID *)SourcePtr, Length);

    if (Destination)
        (*env)->ReleaseFloatArrayElements(env, Destination, Destination1, 0);
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    MoveMemory
 * Signature: ([SII)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_MoveMemory___3SII
  (JNIEnv *env, jclass that, jshortArray Destination, jint SourcePtr, jint Length)
{
    jshort *Destination1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_MoveMemory___3SII\n");
#endif

    if (Destination)
        Destination1 = (*env)->GetShortArrayElements(env, Destination, NULL);

    MoveMemory(Destination1, (CONST VOID *)SourcePtr, Length);

    if (Destination)
        (*env)->ReleaseShortArrayElements(env, Destination, Destination1, 0);
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    MoveMemory
 * Signature: (I[FI)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_MoveMemory__I_3FI
  (JNIEnv *env, jclass that, jint Destination, jfloatArray SourcePtr, jint Length)
{
    jfloat *SourcePtr1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_MoveMemory___I_3FI\n");
#endif

    if (SourcePtr)
        SourcePtr1 = (*env)->GetFloatArrayElements(env, SourcePtr, NULL);

    MoveMemory((LPVOID)Destination, (CONST VOID *)SourcePtr1, Length);

    if (SourcePtr)
        (*env)->ReleaseFloatArrayElements(env, SourcePtr, SourcePtr1, 0);
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    MoveMemory
 * Signature: (I[DI)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_MoveMemory__I_3DI
  (JNIEnv *env, jclass that, jint DestinationPtr, jdoubleArray Source, jint Length)
{
    jdouble *Source1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_MoveMemory__I_3DI\n");
#endif

    if (Source)
        Source1 = (jdouble *)(*env)->GetDoubleArrayElements(env, Source, NULL);

    MoveMemory((PVOID)DestinationPtr, (CONST VOID *)Source1, Length);

    if (Source)
        (*env)->ReleaseDoubleArrayElements(env, Source, Source1, 0);
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    MoveMemory
 * Signature: (I[II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_MoveMemory__I_3II
  (JNIEnv *env, jclass that, jint DestinationPtr, jintArray Source, jint Length)
{
    jint *Source1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_MoveMemory___I_3II\n");
#endif

    if (Source)
        Source1 = (*env)->GetIntArrayElements(env, Source, NULL);

    MoveMemory((PVOID)DestinationPtr, Source1, Length);

    if (Source)
        (*env)->ReleaseIntArrayElements(env, Source, Source1, 0);
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    MoveMemory
 * Signature: (I[SI)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_MoveMemory__I_3SI
  (JNIEnv *env, jclass that, jint DestinationPtr, jshortArray Source, jint Length)
{
    short *Source1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_MoveMemory___I_3SI\n");
#endif

    if (Source)
        Source1 = (*env)->GetShortArrayElements(env, Source, NULL);

    MoveMemory((PVOID)DestinationPtr, Source1, Length);

    if (Source)
        (*env)->ReleaseShortArrayElements(env, Source, Source1, 0);
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    MoveMemory
 * Signature: ([BLorg/eclipse/swt/internal/ole/win32/DROPFILES;I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_MoveMemory___3BLorg_eclipse_swt_internal_ole_win32_DROPFILES_2I
  (JNIEnv *env, jclass that, jbyteArray Destination, jobject Source, jint Length)
{
	DECL_GLOB(pGlob)
    DROPFILES dropfiles, *Source1=NULL;
    VOID *Destination1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_MoveMemory___3BLorg_eclipse_swt_internal_ole_win32_DROPFILES_2I\n");
#endif

    if (Source) {
        Source1=&dropfiles;
        cacheDropfilesFids(env, Source, &PGLOB(DropfilesFc));
        getDropfilesFields(env, Source, Source1, &PGLOB(DropfilesFc));
    }

    if (Destination)
        Destination1 = (*env)->GetByteArrayElements(env, Destination, NULL);

    MoveMemory((PVOID)Destination1, Source1, Length);

    if (Destination)
        (*env)->ReleaseByteArrayElements(env, Destination, (jbyte *)Destination1, 0);
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    MoveMemory
 * Signature: (ILorg/eclipse/swt/internal/ole/win32/DROPFILES;I)V
 */
/*
/*JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_MoveMemory__ILorg_eclipse_swt_internal_ole_win32_DROPFILES_2I
  (JNIEnv *env, jclass that, jint Destination, jobject Source, jint Length)
{
    DROPFILES dropfiles, *Source1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_MoveMemory__ILorg_eclipse_swt_internal_ole_win32_DROPFILES_2I\n");
#endif

    if (Source) {
        Source1=&dropfiles;
        cacheDropfilesFids(env, Source, &DropfilesFc);
        getDropfilesFields(env, Source, Source1, &DropfilesFc);
    }
    MoveMemory((PVOID)Destination, Source1, Length);
}
*/
/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    MoveMemory
 * Signature: (ILorg/eclipse/swt/internal/ole/win32/FORMATETC;I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_MoveMemory__ILorg_eclipse_swt_internal_ole_win32_FORMATETC_2I
  (JNIEnv *env, jclass that, jint Destination, jobject Source, jint Length)
{
	DECL_GLOB(pGlob)
    FORMATETC formatetc, *Source1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_MoveMemory__ILorg_eclipse_swt_internal_ole_win32_FORMATETC_2I\n");
#endif

    if (Source) {
        Source1=&formatetc;
        cacheFormatetcFids(env, Source, &PGLOB(FormatetcFc));
        getFormatetcFields(env, Source, Source1, &PGLOB(FormatetcFc));
    }
    MoveMemory((PVOID)Destination, Source1, Length);
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    MoveMemory
 * Signature: (ILorg/eclipse/swt/internal/ole/win32/GUID;I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_MoveMemory__ILorg_eclipse_swt_internal_ole_win32_GUID_2I
  (JNIEnv *env, jclass that, jint DestinationPtr, jobject Source, jint Length)
{
	DECL_GLOB(pGlob)
    GUID guid, *Source1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "MoveMemory__ILorg_eclipse_swt_internal_ole_win32_GUID_2I\n");
#endif

    if (Source) {
        Source1=&guid;
        cacheGuidFids(env, Source, &PGLOB(GuidFc));
        getGuidFields(env, Source, Source1, &PGLOB(GuidFc));
    }
    MoveMemory((PVOID)DestinationPtr, Source1, Length);
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    MoveMemory
 * Signature: (ILorg/eclipse/swt/internal/ole/win32/OLEINPLACEFRAMEINFO;I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_MoveMemory__ILorg_eclipse_swt_internal_ole_win32_OLEINPLACEFRAMEINFO_2I
  (JNIEnv *env, jclass that, jint DestinationPtr, jobject Source, jint Length)
{
	DECL_GLOB(pGlob)
    OLEINPLACEFRAMEINFO oleinplaceframeinfo, *Source1=&oleinplaceframeinfo;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_MoveMemory__ILorg_eclipse_swt_internal_ole_win32_OLEINPLACEFRAMEINFO_2I\n");
#endif

    if (Source) {
        cacheOleinplaceframeinfoFids(env, Source, &PGLOB(OleinplaceframeinfoFc));
        getOleinplaceframeinfoFields(env, Source, Source1, &PGLOB(OleinplaceframeinfoFc));
    }

    MoveMemory((PVOID)DestinationPtr, Source1, Length);
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    MoveMemory
 * Signature: (ILorg/eclipse/swt/internal/ole/win32/STATSTG;I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_MoveMemory__ILorg_eclipse_swt_internal_ole_win32_STATSTG_2I
  (JNIEnv *env, jclass that, jint Destination, jobject Source, jint Length)
{
	DECL_GLOB(pGlob)
    STATSTG statstg, *Source1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_MoveMemory__ILorg_eclipse_swt_internal_ole_win32_STATSTG_2I\n");
#endif

    if (Source) {
        Source1=&statstg;
        cacheStatstgFids(env, Source, &PGLOB(StatstgFc));
        getStatstgFields(env, Source, Source1, &PGLOB(StatstgFc));
    }
    MoveMemory((PVOID)Destination, Source1, Length);
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    MoveMemory
 * Signature: (ILorg/eclipse/swt/internal/ole/win32/STGMEDIUM;I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_MoveMemory__ILorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2I
  (JNIEnv *env, jclass that, jint Destination, jobject Source, jint Length)
{
	DECL_GLOB(pGlob)
    STGMEDIUM stgmedium, *Source1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_MoveMemory__ILorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2I\n");
#endif

    if (Source) {
        Source1=&stgmedium;
        cacheStgmediumFids(env, Source, &PGLOB(StgmediumFc));
        getStgmediumFields(env, Source, Source1, &PGLOB(StgmediumFc));
    }
    MoveMemory((PVOID)Destination, Source1, Length);
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    MoveMemory
 * Signature: (Lorg/eclipse/swt/internal/ole/win32/STGMEDIUM;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2II
  (JNIEnv *env, jclass that, jobject Destination, jint SourcePtr, jint Length)
{
	DECL_GLOB(pGlob)
    STGMEDIUM stgmedium, *Destination1=&stgmedium;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2II\n");
#endif

	MoveMemory((PVOID)Destination1, (CONST VOID *)SourcePtr, Length);

    if (Destination) {
        cacheStgmediumFids(env, Destination, &PGLOB(StgmediumFc));
        setStgmediumFields(env, Destination, Destination1, &PGLOB(StgmediumFc));
    }
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    MoveMemory
 * Signature: (Lorg/eclipse/swt/internal/ole/win32/DISPPARAMS;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2II
  (JNIEnv *env, jclass that, jobject Destination, jint SourcePtr, jint Length)
{
	DECL_GLOB(pGlob)
    DISPPARAMS dispparams, *Destination1=&dispparams;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_MoveMemory__ILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2II\n");
#endif

    MoveMemory((PVOID)Destination1, (CONST VOID *)SourcePtr, Length);

    if (Destination) {
        cacheDispparamsFids(env, Destination, &PGLOB(DispparamsFc));
        setDispparamsFields(env, Destination, Destination1, &PGLOB(DispparamsFc));
    }
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    MoveMemory
 * Signature: (Lorg/eclipse/swt/internal/ole/win32/FORMATETC;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FORMATETC_2II
  (JNIEnv *env, jclass that, jobject Destination, jint Source, jint Length)
{
	DECL_GLOB(pGlob)
    FORMATETC formatetc, *Destination1=&formatetc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FORMATETC_2II\n");
#endif

    MoveMemory((PVOID)Destination1, (CONST VOID *)Source, Length);

    if (Destination) {
        cacheFormatetcFids(env, Destination, &PGLOB(FormatetcFc));
        setFormatetcFields(env, Destination, Destination1, &PGLOB(FormatetcFc));
    }
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    MoveMemory
 * Signature: (Lorg/eclipse/swt/internal/ole/win32/GUID;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_GUID_2II
  (JNIEnv *env, jclass that, jobject Destination, jint SourcePtr, jint Length)
{
	DECL_GLOB(pGlob)
    GUID guid, *Destination1=&guid;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_MoveMemory__ILorg_eclipse_swt_internal_ole_win32_GUID_2II\n");
#endif

    MoveMemory((PVOID)Destination1, (CONST VOID *)SourcePtr, Length);

    if (Destination) {
        cacheGuidFids(env, Destination, &PGLOB(GuidFc));
        setGuidFields(env, Destination, Destination1, &PGLOB(GuidFc));
    }
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    MoveMemory
 * Signature: (Lorg/eclipse/swt/internal/ole/win32/STATSTG;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_STATSTG_2II
  (JNIEnv *env, jclass that, jobject Destination, jint Source, jint Length)
{
	DECL_GLOB(pGlob)
    STATSTG statstg, *Destination1=&statstg;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_STATSTG_2II\n");
#endif

    MoveMemory((PVOID)Destination1, (CONST VOID *)Source, Length);

    if (Destination) {
        cacheStatstgFids(env, Destination, &PGLOB(StatstgFc));
        setStatstgFields(env, Destination, Destination1, &PGLOB(StatstgFc));
    }
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    MoveMemory
 * Signature: (Lorg/eclipse/swt/internal/ole/win32/TYPEATTR;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_TYPEATTR_2II
  (JNIEnv *env, jclass that, jobject Destination, jint SourcePtr, jint Length)
{
	DECL_GLOB(pGlob)
    TYPEATTR typeattr, *Destination1=&typeattr;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_MoveMemory__ILorg_eclipse_swt_internal_ole_win32_TYPEATTR_2II\n");
#endif
    MoveMemory((PVOID)Destination1, (CONST VOID *)SourcePtr, Length);
    if (Destination) {
        cacheTypeattrFids(env, Destination, &PGLOB(TypeattrFc));
        setTypeattrFields(env, Destination, Destination1, &PGLOB(TypeattrFc));
    }
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    MoveMemory
 * Signature: (Lorg/eclipse/swt/internal/win32/RECT;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_MoveMemory__Lorg_eclipse_swt_internal_win32_RECT_2II
  (JNIEnv *env, jclass that, jobject Destination, jint Source, jint Length)
{
	DECL_GLOB(pGlob)
    RECT rect, *lpDestination1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_MoveMemory__Lorg_eclipse_swt_internal_win32_RECT_2II\n");
#endif

       MoveMemory((PVOID)&rect, (CONST VOID *)Source, Length);

    if (Destination) {
        lpDestination1 = &rect;
        cacheRectFids(env, Destination, &PGLOB(RectFc));
        setRectFields(env, Destination, lpDestination1, &PGLOB(RectFc));
    }
}
/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    MoveMemory
 * Signature: (Lorg/eclipse/swt/internal/ole/win32/FUNCDESC1;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FUNCDESC1_2II
  (JNIEnv *env, jclass that, jobject Destination, jint Source, jint Length)
{
	DECL_GLOB(pGlob)
    FUNCDESC funcdesc, *lpDestination1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FUNCDESC1_2II\n");
#endif

    MoveMemory((PVOID)&funcdesc, (CONST VOID *)Source, Length);

    if (Destination) {
        lpDestination1 = &funcdesc;
        cacheFuncdesc1Fids(env, Destination, &PGLOB(Funcdesc1Fc));
        setFuncdesc1Fields(env, Destination, lpDestination1, &PGLOB(Funcdesc1Fc));
    }
}
/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    MoveMemory
 * Signature: (Lorg/eclipse/swt/internal/ole/win32/FUNCDESC2;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FUNCDESC2_2II
  (JNIEnv *env, jclass that, jobject Destination, jint Source, jint Length)
{
	DECL_GLOB(pGlob)
    FUNCDESC funcdesc, *lpDestination1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FUNCDESC2_2II\n");
#endif

    MoveMemory((PVOID)&funcdesc, (CONST VOID *)Source, Length);

    if (Destination) {
        lpDestination1 = &funcdesc;
        cacheFuncdesc2Fids(env, Destination, &PGLOB(Funcdesc2Fc));
        setFuncdesc2Fields(env, Destination, lpDestination1, &PGLOB(Funcdesc2Fc));
    }
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    MoveMemory
 * Signature: (Lorg/eclipse/swt/internal/ole/win32/VARDESC1;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_VARDESC1_2II
  (JNIEnv *env, jclass that, jobject Destination, jint Source, jint Length)
{
	DECL_GLOB(pGlob)
    VARDESC vardesc, *lpDestination1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_VARDESC1_2II\n");
#endif

       MoveMemory((PVOID)&vardesc, (CONST VOID *)Source, Length);

    if (Destination) {
        lpDestination1 = &vardesc;
        cacheVardesc1Fids(env, Destination, &PGLOB(Vardesc1Fc));
        setVardesc1Fields(env, Destination, lpDestination1, &PGLOB(Vardesc1Fc));
    }
}
/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    MoveMemory
 * Signature: (Lorg/eclipse/swt/internal/ole/win32/VARDESC2;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_VARDESC2_2II
  (JNIEnv *env, jclass that, jobject Destination, jint Source, jint Length)
{
	DECL_GLOB(pGlob)
    VARDESC vardesc, *lpDestination1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_VARDESC2_2II\n");
#endif

       MoveMemory((PVOID)&vardesc, (CONST VOID *)Source, Length);

    if (Destination) {
        lpDestination1 = &vardesc;
        cacheVardesc2Fids(env, Destination, &PGLOB(Vardesc2Fc));
        setVardesc2Fields(env, Destination, lpDestination1, &PGLOB(Vardesc2Fc));
    }
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    OleCreate
 * Signature: (Lorg/eclipse/swt/internal/ole/win32/GUID;Lorg/eclipse/swt/internal/ole/win32/GUID;ILorg/eclipse/swt/internal/ole/win32/FORMATETC;II[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_OleCreate
  (JNIEnv *env, jclass that, jobject rclsid, jobject riid, jint renderopt, 
                             jobject pFormatEtc, jint pClientSite, jint pStg, jintArray ppvObject)
{
	DECL_GLOB(pGlob)
    LPVOID *ppvObject1=NULL;
    GUID guid1, *rclsid1=NULL;
    GUID guid2, *riid1=NULL;
    FORMATETC formatec, *pFormatEtc1=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "OleCreate\n");
#endif

    if (rclsid) {
        rclsid1=&guid1;
        cacheGuidFids(env, rclsid, &PGLOB(GuidFc));
        getGuidFields(env, rclsid, rclsid1, &PGLOB(GuidFc));
    }
    if (riid) {
        riid1=&guid2;
        cacheGuidFids(env, riid, &PGLOB(GuidFc));
        getGuidFields(env, riid, riid1, &PGLOB(GuidFc));
    }
    if (pFormatEtc) {
        pFormatEtc1 = &formatec;
        cacheFormatetcFids(env, pFormatEtc, &PGLOB(FormatetcFc));
        getFormatetcFields(env, pFormatEtc, pFormatEtc1, &PGLOB(FormatetcFc));
    }

    if (ppvObject)
        ppvObject1 = (LPVOID *)(*env)->GetIntArrayElements(env, ppvObject, NULL);

    rc = (jint) OleCreate(rclsid1, riid1, renderopt, pFormatEtc1, (IOleClientSite * )pClientSite, (IStorage * )pStg, ppvObject1);

    if (ppvObject)
        (*env)->ReleaseIntArrayElements(env, ppvObject, (jint *)ppvObject1, 0);
    
/*  all of these objects are inputs so we don't not need to set the
    fields back
*/
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    OleCreateFromFile
 * Signature: (Lorg/eclipse/swt/internal/ole/win32/GUID;[CLorg/eclipse/swt/internal/ole/win32/GUID;ILorg/eclipse/swt/internal/ole/win32/FORMATETC;II[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_OleCreateFromFile
  (JNIEnv *env, jclass that, jobject rclsid, jcharArray lpszFileName, jobject riid, jint renderopt, 
			jobject pFormatEtc, jint pClientSite, jint pStg, jintArray ppvObj)
{
	DECL_GLOB(pGlob)
    LPVOID *ppvObj1=NULL;
    LPCOLESTR lpszFileName1=NULL;
    GUID guid1, *rclsid1=NULL;
    GUID guid2, *riid1=NULL;
    FORMATETC formatec, *pFormatEtc1=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "OleCreateFromFile\n");
#endif
    
    if (rclsid) {
        rclsid1=&guid1;
        cacheGuidFids(env, rclsid, &PGLOB(GuidFc));
        getGuidFields(env, rclsid, rclsid1, &PGLOB(GuidFc));
    }

    if (riid) {
        riid1=&guid2;
        cacheGuidFids(env, riid, &PGLOB(GuidFc));
        getGuidFields(env, riid, riid1, &PGLOB(GuidFc));
    }
    if (pFormatEtc) {
        pFormatEtc1 = &formatec;
        cacheFormatetcFids(env, pFormatEtc, &PGLOB(FormatetcFc));
        getFormatetcFields(env, pFormatEtc, pFormatEtc1, &PGLOB(FormatetcFc));
    }

    if (lpszFileName)
        lpszFileName1 = (LPCOLESTR)(*env)->GetCharArrayElements(env, lpszFileName, NULL);

    if (ppvObj)
        ppvObj1 = (LPVOID *)(*env)->GetIntArrayElements(env, ppvObj, NULL);

    rc = (jint) OleCreateFromFile(rclsid1, lpszFileName1, riid1, renderopt, pFormatEtc1, (LPOLECLIENTSITE)pClientSite, (LPSTORAGE)pStg, ppvObj1);

    if (ppvObj)
        (*env)->ReleaseIntArrayElements(env, ppvObj, (jint *)ppvObj1, 0);
    
    if (lpszFileName)
        (*env)->ReleaseCharArrayElements(env, lpszFileName, (jchar *)lpszFileName1, 0);

/*  all of these objects are inputs so we don't not need to set the
    fields back
*/
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    OleCreatePropertyFrame
 * Signature: (III[CI[IIIIII)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_OleCreatePropertyFrame
  (JNIEnv *env, jclass that, jint hwndOwner, jint x, jint y, jcharArray lpszCaption, jint cObjects, jintArray lplpUnk, jint cPages, jint lpPageClsID, jint lcid, jint dwReserved, jint lpvReserved)
{
    LPCOLESTR lpszCaption1=NULL;
    jint *lplpUnk1=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "OleCreatePropertyFrame\n");
#endif

    if (lpszCaption)
        lpszCaption1 = (LPCOLESTR)(*env)->GetCharArrayElements(env, lpszCaption, NULL);

    if (lplpUnk)
        lplpUnk1 = (*env)->GetIntArrayElements(env, lplpUnk, NULL);

    rc = (jint) OleCreatePropertyFrame((HWND)hwndOwner, x, y, lpszCaption1, cObjects, (LPUNKNOWN FAR*)lplpUnk1, cPages, (LPCLSID)lpPageClsID, (LCID)lcid, dwReserved, (LPVOID)lpvReserved);

    if (lplpUnk)
        (*env)->ReleaseIntArrayElements(env, lplpUnk, (jint *)lplpUnk1, 0);

    if (lpszCaption)
        (*env)->ReleaseCharArrayElements(env, lpszCaption, (jchar *)lpszCaption1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    OleIsRunning
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_OleIsRunning
  (JNIEnv *env, jclass that, jint pObject)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "OleIsRunning\n");
#endif
    return (jboolean) OleIsRunning((LPOLEOBJECT)pObject);
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    OleLoad
 * Signature: (ILorg/eclipse/swt/internal/ole/win32/GUID;I[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_OleLoad
  (JNIEnv *env, jclass that, jint pStg, jobject riid, jint pClientSite, jintArray ppvObj)
{
	DECL_GLOB(pGlob)
    LPVOID *ppvObj1=NULL;
    GUID guid, *riid1=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "OleLoad\n");
#endif
    if (riid) {
        riid1=&guid;
        cacheGuidFids(env, riid, &PGLOB(GuidFc));
        getGuidFields(env, riid, riid1, &PGLOB(GuidFc));
    }
    
    if (ppvObj)
        ppvObj1 = (LPVOID *)(*env)->GetIntArrayElements(env, ppvObj, NULL);

    rc = (jint) OleLoad((IStorage *)pStg, riid1, (IOleClientSite *)pClientSite, ppvObj1); /* cast it to an OLE function returning int */

    if (ppvObj)
        (*env)->ReleaseIntArrayElements(env, ppvObj, (jint *)ppvObj1, 0);

    if (riid) {
        setGuidFields(env, riid, riid1, &PGLOB(GuidFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    OleRun
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_OleRun
  (JNIEnv *env, jclass that, jint pUnknown)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "OleRun\n");
#endif
    return (jint) OleRun((LPUNKNOWN)pUnknown);
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    OleSave
 * Signature: (IIZ)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_OleSave
  (JNIEnv *env, jclass that, jint pPS, jint pStg, jboolean fSameAsLoad)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "OleSave\n");
#endif
    return (jint) OleSave((IPersistStorage *)pPS, (IStorage *)pStg, fSameAsLoad);
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    OleSetContainedObject
 * Signature: (IZ)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_OleSetContainedObject
  (JNIEnv *env, jclass that, jint pUnk, jboolean fContained)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "OleSetContainedObject\n");
#endif
    return (jint) OleSetContainedObject((LPUNKNOWN)pUnk, fContained);
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    OleSetMenuDescriptor
 * Signature: (IIIII)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_OleSetMenuDescriptor
  (JNIEnv *env, jclass that, jint holemenu, jint hwndFrame, jint hwndActiveObject, jint lpFrame, jint lpActiveObj)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "OleSetMenuDescriptor\n");
#endif
    return (jint) OleSetMenuDescriptor((HOLEMENU)holemenu, (HWND)hwndFrame, (HWND)hwndActiveObject, (LPOLEINPLACEFRAME)lpFrame, (LPOLEINPLACEACTIVEOBJECT)lpActiveObj);
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    OleTranslateColor
 * Signature: (II[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_OleTranslateColor
  (JNIEnv *env, jclass that, jint clr, jint hpal, jintArray pcolorref)
{
    jint *pcolorref1=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "OleTranslateColor\n");
#endif

    if (pcolorref)
        pcolorref1 = (*env)->GetIntArrayElements(env, pcolorref, NULL);

    rc = (jint) OleTranslateColor((OLE_COLOR)clr, (HPALETTE)hpal, (COLORREF *)pcolorref1);

    if (pcolorref)
        (*env)->ReleaseIntArrayElements(env, pcolorref, pcolorref1, 0);

    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    RegisterClipboardFormat
 * Signature: ([B)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_RegisterClipboardFormat
  (JNIEnv *env, jclass that, jbyteArray lpszFormat)
{
    LPCTSTR lpszFormat1=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "RegisterClipboardFormat\n");
#endif

    if (lpszFormat)
        lpszFormat1 = (*env)->GetByteArrayElements(env, lpszFormat, NULL);

    rc = (jint) RegisterClipboardFormat(lpszFormat1);

    if (lpszFormat)
        (*env)->ReleaseByteArrayElements(env, lpszFormat, (jbyte *)lpszFormat1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    RegisterDragDrop
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_RegisterDragDrop
  (JNIEnv *env, jclass that, jint hwnd, jint pDropTarget)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "RegisterDragDrop\n");
#endif
    return (jint) RegisterDragDrop((HWND)hwnd, (IDropTarget *)pDropTarget);
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    RevokeDragDrop
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_RevokeDragDrop
  (JNIEnv *env, jclass that, jint hwnd)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "RevokeDragDrop\n");
#endif
    return (jint) RevokeDragDrop((HWND)hwnd);
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    StgCreateDocfile
 * Signature: ([CII[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_StgCreateDocfile
  (JNIEnv *env, jclass that, jcharArray pwcsName, jint grfMode, jint reserved, jintArray ppstgOpen)
{
    jchar *pwcsName1=NULL;
    IStorage **ppstgOpen1=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "StgCreateDocfile\n");
#endif

    if (pwcsName)
        pwcsName1 = (*env)->GetCharArrayElements(env, pwcsName, NULL);

    if (ppstgOpen)
        ppstgOpen1 = (IStorage **)(*env)->GetIntArrayElements(env, ppstgOpen, NULL);

    rc = (jint) StgCreateDocfile(pwcsName1, grfMode, reserved, ppstgOpen1);

    if (ppstgOpen)
        (*env)->ReleaseIntArrayElements(env, ppstgOpen, (jint *)ppstgOpen1, 0);

    if (pwcsName)
        (*env)->ReleaseCharArrayElements(env, pwcsName, pwcsName1, 0);

    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    StgIsStorageFile
 * Signature: ([C)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_StgIsStorageFile
  (JNIEnv *env, jclass that, jcharArray pwcsName)
{
    const WCHAR *pwcsName1=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "StgIsStorageFile\n");
#endif

    if (pwcsName)
        pwcsName1 = (const WCHAR *)(*env)->GetCharArrayElements(env, pwcsName, NULL);

    rc = (jint)StgIsStorageFile(pwcsName1);

    if (pwcsName)
        (*env)->ReleaseCharArrayElements(env, pwcsName, (jchar *)pwcsName1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    StgOpenStorage
 * Signature: ([CIIII[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_StgOpenStorage
  (JNIEnv *env, jclass that, jcharArray pwcsName, jint pstgPriority, jint grfMode, jint snbExclude, jint reserved, jintArray ppstgOpen)
{
    const WCHAR *pwcsName1=NULL;
    IStorage **ppstgOpen1=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "StgOpenStorage\n");
#endif

    if (pwcsName)
        pwcsName1 = (const WCHAR *)(*env)->GetCharArrayElements(env, pwcsName, NULL);

    if (ppstgOpen)
        ppstgOpen1 = (IStorage **)(*env)->GetIntArrayElements(env, ppstgOpen, NULL);

    rc = (jint) StgOpenStorage(pwcsName1, (IStorage *)pstgPriority, grfMode, (SNB)snbExclude, reserved, ppstgOpen1);

    if (ppstgOpen)
        (*env)->ReleaseIntArrayElements(env, ppstgOpen, (jint *)ppstgOpen1, 0);

    if (pwcsName)
        (*env)->ReleaseCharArrayElements(env, pwcsName, (jchar *)pwcsName1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    SysAllocString
 * Signature: ([C)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_SysAllocString
   (JNIEnv *env, jclass that, jcharArray sz0)
{
	jchar *sz01=NULL;
	jint rc;
	
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "SysAllocString\n");
#endif
	if (sz0)
		sz01 = (*env)->GetCharArrayElements(env, sz0, NULL);
	rc = (jint) SysAllocString( (OLECHAR *) sz01);
	if (sz0)
		(*env)->ReleaseCharArrayElements(env, sz0, sz01, 0);
	return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    SysFreeString
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_SysFreeString
   (JNIEnv *env, jclass that, jint bstr)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "SysFreeString\n");
#endif
	SysFreeString((BSTR)bstr);
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    SysStringByteLen
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_SysStringByteLen
  (JNIEnv *env, jclass that, jint bstr)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "SysStringByteLen\n");
#endif
    return (jint) SysStringByteLen((BSTR)bstr);
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VariantChangeType
 * Signature: (IISS)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VariantChangeType
  (JNIEnv *env, jclass that, jint pvargDest, jint pvarSrc, jshort wFlags, jshort vt)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "VariantChangeType\n");
#endif
    return (jint) VariantChangeType((VARIANTARG FAR* )pvargDest, (VARIANTARG FAR* )pvarSrc, wFlags, (VARTYPE) vt);
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VariantClear
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VariantClear
  (JNIEnv *env, jclass that, jint pvarg)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "VariantClear\n");
#endif
    return (jint) VariantClear((VARIANTARG FAR* ) pvarg);
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VariantInit
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VariantInit
  (JNIEnv *env, jclass that, jint pvarg)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "VariantInit\n");
#endif
    VariantInit((VARIANTARG FAR*)pvarg);
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__II
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl)
{
    P_OLE_FN_1 fn; /* this is a function that returns int */

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_VtblCall__II\n");
#endif
    fn = (P_OLE_FN_1)(*(int **)ppVtbl)[fnNumber];
    return fn(ppVtbl); /* cast it to an OLE function returning int */
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (II[C)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__II_3C
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jcharArray arg0)
{
    P_OLE_FN_2 fn;
	jchar *arg01=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_VtblCall__II_3C\n");
#endif

    fn = (P_OLE_FN_2)(*(int **)ppVtbl)[fnNumber];

	if (arg0)
		arg01 = (*env)->GetCharArrayElements(env, arg0, NULL);

	rc = fn(ppVtbl, (jint)arg01);

	if (arg0)
		(*env)->ReleaseCharArrayElements(env, arg0, arg01, 0);

    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (II[C[C)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__II_3C_3C
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jcharArray arg0, jcharArray arg1)
{
    P_OLE_FN_3 fn; /* this is a function that returns int */
    jchar *arg01=NULL, *arg11=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_VtblCall__II_3C_3C\n");
#endif

    fn = (P_OLE_FN_3)(*(int **)ppVtbl)[fnNumber];

    if (arg0)
        arg01 = (*env)->GetCharArrayElements(env, arg0, NULL);

    if (arg1)
        arg11 = (*env)->GetCharArrayElements(env, arg1, NULL);

    rc = fn(ppVtbl, (jint)arg01, (jint)arg11); /* cast it to an OLE function returning int */

    if (arg1)
        (*env)->ReleaseCharArrayElements(env, arg1, arg11, 0);

    if (arg0)
        (*env)->ReleaseCharArrayElements(env, arg0, arg01, 0);

    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (II[CI)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__II_3CI
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jcharArray arg0, jint arg1)
{
    P_OLE_FN_3 fn; /* this is a function that returns int */
    jchar *arg01=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_VtblCall__II_3CI\n");
#endif

    fn = (P_OLE_FN_3)(*(int **)ppVtbl)[fnNumber];

    if (arg0)
        arg01 = (*env)->GetCharArrayElements(env, arg0, NULL);
    
    rc = fn(ppVtbl, (jint)arg01, arg1);

    if (arg0)
        (*env)->ReleaseCharArrayElements(env, arg0, arg01, 0);

    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (II[CIII[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__II_3CIII_3I
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jcharArray arg0, jint arg1, jint arg2, jint arg3, jintArray arg4)
{
    P_OLE_FN_6 fn; /* this is a function that returns int */
    jchar *arg01=NULL;
    jint *arg41=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_VtblCall__II_3CIII_3I\n");
#endif

    fn = (P_OLE_FN_6)(*(int **)ppVtbl)[fnNumber];

    if (arg0)
        arg01 = (*env)->GetCharArrayElements(env, arg0, NULL);
    if (arg4)
        arg41 = (*env)->GetIntArrayElements(env, arg4, NULL);

    rc = fn(ppVtbl, (jint)arg01, arg1, arg2, arg3, (jint)arg41); /* cast it to an OLE function returning int */

    if (arg4)
        (*env)->ReleaseIntArrayElements(env, arg4, arg41, 0);
    if (arg0)
        (*env)->ReleaseCharArrayElements(env, arg0, arg01, 0);

    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (II[CIIII[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__II_3CIIII_3I
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jcharArray arg0, jint arg1, jint arg2, jint arg3, jint arg4, jintArray arg5)
{
    P_OLE_FN_7 fn; /* this is a function that returns int */    jchar *arg01=NULL;
    jint *arg51=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_VtblCall__II_3CIIII_3I\n");
#endif

    fn = (P_OLE_FN_7)(*(int **)ppVtbl)[fnNumber];

    if (arg0)
        arg01 = (*env)->GetCharArrayElements(env, arg0, NULL);
    if (arg5)
        arg51 = (*env)->GetIntArrayElements(env, arg5, NULL);

    rc = fn(ppVtbl, (jint)arg01, arg1, arg2, arg3, arg4, (jint)arg51); /* cast it to an OLE function returning int */

    if (arg5)
        (*env)->ReleaseIntArrayElements(env, arg5, arg51, 0);
    if (arg0)
        (*env)->ReleaseCharArrayElements(env, arg0, arg01, 0);

    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (II[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__II_3I
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jintArray arg0)
{
    P_OLE_FN_2 fn; /* this is a function that returns int */
    jint *arg01=NULL;
    jint rc;
    
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_VtblCall__II_3I\n");
#endif

    fn = (P_OLE_FN_2)(*(int **)ppVtbl)[fnNumber];

    if (arg0)
        arg01 = (*env)->GetIntArrayElements(env, arg0, NULL);

    rc = fn(ppVtbl, (jint)arg01); /* cast it to an OLE function returning int */

    if (arg0)
        (*env)->ReleaseIntArrayElements(env, arg0, arg01, 0);

    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__III
   (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jint arg0)
{
	P_OLE_FN_2 fn; /* this is a function that returns int */
	jint rc;

#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "COM_VtblCall__III\n");
#endif

	fn = (P_OLE_FN_2)(*(int **)ppVtbl)[fnNumber];

	rc = fn(ppVtbl, arg0); /* cast it to an OLE function returning int */
	return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (IIII)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__IIII
   (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jint arg0, jint arg1)
{
	P_OLE_FN_3 fn; /* this is a function that returns int */
	jint rc;

#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "COM_VtblCall__IIII\n");
#endif

	fn = (P_OLE_FN_3)(*(int **)ppVtbl)[fnNumber];

	rc = fn(ppVtbl, arg0, arg1); /* cast it to an OLE function returning int */
	return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (III[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__III_3I
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jint arg0, jintArray arg1)
{
    P_OLE_FN_3 fn;    

    jint *arg11=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_VtblCall__III_3I\n");
#endif

    fn = (P_OLE_FN_3)(*((jint **)ppVtbl))[fnNumber];

    if (arg1)
        arg11 = (*env)->GetIntArrayElements(env, arg1, NULL);

    rc = fn(ppVtbl, arg0, (jint)arg11); 

    if (arg1)
        (*env)->ReleaseIntArrayElements(env, arg1, arg11, 0);

    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (IIII[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__IIII_3I
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jint arg0, jint arg1, jintArray arg2)
{
    P_OLE_FN_4 fn; /* this is a function that returns int */
    jint *arg21=NULL;
    jint rc;
    
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_VtblCall__IIII_3I\n");
#endif

    fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];

    if (arg2)
        arg21 = (*env)->GetIntArrayElements(env, arg2, NULL);

    rc = fn(ppVtbl, arg0, arg1, (jint)arg21); /* cast it to an OLE function returning int */

    if (arg2)
        (*env)->ReleaseIntArrayElements(env, arg2, arg21, 0);

    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (IIIII)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__IIIII
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jint arg0, jint arg1, jint arg2)
{
    P_OLE_FN_4 fn; /* this is a function that returns int */

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_VtblCall__IIIII\n");
#endif

    fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];
    return fn(ppVtbl, arg0, arg1, arg2);;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (IIIII[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__IIIII_3I
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
    P_OLE_FN_5 fn; /* this is a function that returns int */
    jint *arg31=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "VtblCall__IIIII_3I\n");
#endif

    fn = (P_OLE_FN_5)(*(int **)ppVtbl)[fnNumber];

    if (arg3)
        arg31 = (*env)->GetIntArrayElements(env, arg3, NULL);
    
    rc = fn(ppVtbl, arg0, arg1, arg2, (jint)arg31);

    if (arg3)
        (*env)->ReleaseIntArrayElements(env, arg3, arg31, 0);

    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (IIIIII[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__IIIIII_3I
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jint arg0, jint arg1, jint arg2, jint arg3, jintArray arg4)
{
    P_OLE_FN_6 fn;
    jint *arg41=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_VtblCall__II_3CIIII_3I\n");
#endif

    fn = (P_OLE_FN_6)(*(int **)ppVtbl)[fnNumber];
    if (arg4)
        arg41 = (*env)->GetIntArrayElements(env, arg4, NULL);
    
    rc = fn(ppVtbl, arg0, arg1, arg2, arg3, (jint)arg41); 

    if (arg4)
        (*env)->ReleaseIntArrayElements(env, arg4, arg41, 0);

    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (IIIIIII[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__IIIIIII_3I
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jintArray arg5)
{
    P_OLE_FN_7 fn;    

    jint *arg51=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_VtblCall__IIIIIII_3I\n");
#endif

    fn = (P_OLE_FN_7)(*((jint **)ppVtbl))[fnNumber];

    if (arg5)
        arg51 = (*env)->GetIntArrayElements(env, arg5, NULL);

    rc = fn(ppVtbl, arg0, arg1, arg2, arg3, arg4, (jint)arg51); 

    if (arg5)
        (*env)->ReleaseIntArrayElements(env, arg5, arg51, 0);

    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (IIIILorg/eclipse/swt/internal/ole/win32/DVTARGETDEVICE;Lorg/eclipse/swt/internal/win32/SIZE;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__IIIILorg_eclipse_swt_internal_ole_win32_DVTARGETDEVICE_2Lorg_eclipse_swt_internal_win32_SIZE_2
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jint arg0, jint arg1, jobject arg2, jobject arg3)
{
	DECL_GLOB(pGlob)
    DVTARGETDEVICE dvtargetdevice, *arg21=NULL;
    SIZE size, *arg31=NULL;
    P_OLE_FN_5 fn; /* this is a function that returns int */
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_VtblCall__IIIILorg_eclipse_swt_internal_ole_win32_DVTARGETDEVICE_2Lorg_eclipse_swt_internal_win32_SIZE_2\n");
#endif

    fn = (P_OLE_FN_5)(*(int **)ppVtbl)[fnNumber];
    if (arg2) {
        arg21=&dvtargetdevice;
        cacheDvtargetdeviceFids(env, arg2, &PGLOB(DvtargetdeviceFc));
        getDvtargetdeviceFields(env, arg2, arg21, &PGLOB(DvtargetdeviceFc));
    }
    if (arg3) {
        arg31=&size;
        cacheSizeFids(env, arg3, &PGLOB(SizeFc));
        getSizeFields(env, arg3, arg31, &PGLOB(SizeFc));
    }
    
    rc = fn(ppVtbl, arg0, arg1, (jint)arg21, (jint)arg31); /* cast it to an OLE function returning int */

    if (arg2) {
        setDvtargetdeviceFields(env, arg2, arg21, &PGLOB(DvtargetdeviceFc));
    }
    if (arg3) {
        setSizeFields(env, arg3, arg31, &PGLOB(SizeFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (IIIILorg/eclipse/swt/internal/ole/win32/GUID;I[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__IIIILorg_eclipse_swt_internal_ole_win32_GUID_2I_3I
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jint arg0, jint arg1, jobject arg2, jint arg3, jintArray arg4)
{
	DECL_GLOB(pGlob)
    jint *arg41=NULL;
    GUID guid, *arg21=NULL;
    P_OLE_FN_6 fn; /* this is a function that returns int */
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_VtblCall__IIIILorg_eclipse_swt_internal_ole_win32_GUID_2I_3I\n");
#endif
    fn = (P_OLE_FN_6)(*(int **)ppVtbl)[fnNumber];
    if (arg2) {
        arg21=&guid;
        cacheGuidFids(env, arg2, &PGLOB(GuidFc));
        getGuidFields(env, arg2, arg21, &PGLOB(GuidFc));
    }
    
    if (arg4)
        arg41 = (*env)->GetIntArrayElements(env, arg4, NULL);

    rc = fn(ppVtbl, arg0, arg1, (jint)arg21, arg3, (jint)arg41); /* cast it to an OLE function returning int */

    if (arg4)
        (*env)->ReleaseIntArrayElements(env, arg4, arg41, 0);

    if (arg2) {
        setGuidFields(env, arg2, arg21, &PGLOB(GuidFc));
    }
    return rc;
}


/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (IIILorg/eclipse/swt/internal/ole/win32/FORMATETC;[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__IIILorg_eclipse_swt_internal_ole_win32_FORMATETC_2_3I
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jint arg0, jobject arg1, jintArray arg2)
{
	DECL_GLOB(pGlob)
    P_OLE_FN_4 fn;
    FORMATETC formatetc, *arg11=NULL;
    jint *arg21=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_VtblCall__IIILorg_eclipse_swt_internal_ole_win32_FORMATETC_2_3I\n");
#endif

    fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];

    if (arg1) {
        arg11 = &formatetc;
        cacheFormatetcFids(env, arg1, &PGLOB(FormatetcFc));
        getFormatetcFields(env, arg1, arg11, &PGLOB(FormatetcFc));
    }
    if (arg2)
        arg21 = (*env)->GetIntArrayElements(env, arg2, NULL);

    rc = fn(ppVtbl, arg0, (jint)arg11, (jint)arg21);

    if (arg2)
        (*env)->ReleaseIntArrayElements(env, arg2, arg21, 0);

    if (arg1) {
        setFormatetcFields(env, arg1, arg11, &PGLOB(FormatetcFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (IIILorg/eclipse/swt/internal/ole/win32/GUID;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jint arg0, jobject arg1)
{
	DECL_GLOB(pGlob)
    GUID guid, *arg11=NULL;
    P_OLE_FN_3 fn; /* this is a function that returns int */
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_VtblCall__IIIILorg_eclipse_swt_internal_ole_win32_GUID_2n");
#endif
    fn = (P_OLE_FN_3)(*(int **)ppVtbl)[fnNumber];
    if (arg1) {
        arg11=&guid;
        cacheGuidFids(env, arg1, &PGLOB(GuidFc));
        getGuidFields(env, arg1, arg11, &PGLOB(GuidFc));
    }

    rc = fn(ppVtbl, arg0, (jint)arg11); /* cast it to an COM function returning int */

    if (arg1) {
        setGuidFields(env, arg1, arg11, &PGLOB(GuidFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (IIILorg/eclipse/swt/internal/ole/win32/GUID;II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2II
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jint arg0, jobject arg1, jint arg2, jint arg3)
{
	DECL_GLOB(pGlob)
    GUID guid, *arg11=NULL;
    P_OLE_FN_5 fn; /* this is a function that returns int */
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2II\n");
#endif
    fn = (P_OLE_FN_5)(*(int **)ppVtbl)[fnNumber];
    if (arg1) {
        arg11=&guid;
        cacheGuidFids(env, arg1, &PGLOB(GuidFc));
        getGuidFields(env, arg1, arg11, &PGLOB(GuidFc));
    }
    
    rc = fn(ppVtbl, arg0, (jint)arg11, arg2, arg3); /* cast it to an OLE function returning int */

    if (arg1) {
        setGuidFields(env, arg1, arg11, &PGLOB(GuidFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (IIILorg/eclipse/swt/internal/ole/win32/GUID;IILorg/eclipse/swt/internal/ole/win32/DISPPARAMS;ILorg/eclipse/swt/internal/ole/win32/EXCEPINFO;[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2IILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2ILorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2_3I
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jint arg0, jobject arg1, jint arg2, jint arg3, jobject arg4, jint arg5, jobject arg6, jintArray arg7)
{
	DECL_GLOB(pGlob)
    jint *arg71=NULL;
    GUID guid, *arg11=NULL;
    EXCEPINFO excepinfo, *arg61=NULL;
    DISPPARAMS dispparams, *arg41=NULL;
    P_OLE_FN_9 fn; /* this is a function that returns int */
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2IILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2ILorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2_3I\n");
#endif

    fn = (P_OLE_FN_9)(*(int **)ppVtbl)[fnNumber];
    if (arg1) {
        arg11=&guid;
        cacheGuidFids(env, arg1, &PGLOB(GuidFc));
        getGuidFields(env, arg1, arg11, &PGLOB(GuidFc));
    }
    if (arg4) {
        arg41=&dispparams;
        cacheDispparamsFids(env, arg4, &PGLOB(DispparamsFc));
        getDispparamsFields(env, arg4, arg41, &PGLOB(DispparamsFc));
    }

    if (arg6) {
        arg61=&excepinfo;
        cacheExcepinfoFids(env, arg6, &PGLOB(ExcepinfoFc));
        getExcepinfoFields(env, arg6, arg61, &PGLOB(ExcepinfoFc));
    }

    if (arg7)
        arg71 = (*env)->GetIntArrayElements(env, arg7, NULL);

    rc = fn(ppVtbl, arg0, (jint)arg11, arg2, arg3, (jint)arg41, arg5, (jint)arg61, (jint)arg71); /* cast it to an OLE function returning int */

    if (arg7)
        (*env)->ReleaseIntArrayElements(env, arg7, arg71, 0);

    if (arg6) {
        setExcepinfoFields(env, arg6, arg61, &PGLOB(ExcepinfoFc));
    }

    if (arg4) {
        setDispparamsFields(env, arg4, arg41, &PGLOB(DispparamsFc));
    }

    if (arg1) {
        setGuidFields(env, arg1, arg11, &PGLOB(GuidFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (IIILorg/eclipse/swt/internal/ole/win32/STATSTG;[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__IIILorg_eclipse_swt_internal_ole_win32_STATSTG_2_3I
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jint arg0, jobject arg1, jintArray arg2)
{
	DECL_GLOB(pGlob)
    jint *arg21=NULL;
    STATSTG statstg, *arg11=NULL;
    P_OLE_FN_4 fn; /* this is a function that returns int */
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "VtblCall__IIILorg_eclipse_swt_internal_ole_win32_STATSTG_2_3I\n");
#endif
    fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];
    if (arg1) {
        arg11=&statstg;
        cacheStatstgFids(env, arg1, &PGLOB(StatstgFc));
        getStatstgFields(env, arg1, arg11, &PGLOB(StatstgFc));
    }
    
    if (arg2)
        arg21 = (*env)->GetIntArrayElements(env, arg2, NULL);

    rc = fn(ppVtbl, arg0, (jint)arg11, (jint)arg21); /* cast it to an OLE function returning int */

    if (arg2)
        (*env)->ReleaseIntArrayElements(env, arg2, arg21, 0);

    if (arg1) {
        setStatstgFields(env, arg1, arg11, &PGLOB(StatstgFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (IIILorg/eclipse/swt/internal/win32/MSG;IIILorg/eclipse/swt/internal/win32/RECT;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__IIILorg_eclipse_swt_internal_win32_MSG_2IIILorg_eclipse_swt_internal_win32_RECT_2
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jint arg0, jobject arg1, jint arg2, jint arg3, jint arg4, jobject arg5)
{
	DECL_GLOB(pGlob)
    MSG msg, *arg11=NULL;
    RECT rect, *arg51=NULL;
    P_OLE_FN_7 fn; /* this is a function that returns int */
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_VtblCall__IIILorg_eclipse_swt_internal_win32_MSG_2IIILorg_eclipse_swt_internal_win32_RECT_2\n");
#endif

    fn = (P_OLE_FN_7)(*(int **)ppVtbl)[fnNumber];
    if (arg1) {
        arg11=&msg;
        cacheMsgFids(env, arg1, &PGLOB(MsgFc));
        getMsgFields(env, arg1, arg11, &PGLOB(MsgFc));
    }
    if (arg5) {
        arg51=&rect;
        cacheRectFids(env, arg5, &PGLOB(RectFc));
        getRectFields(env, arg5, arg51, &PGLOB(RectFc));
    }
    
    rc = fn(ppVtbl, arg0, (jint)arg11, arg2, arg3, arg4, (jint)arg51); /* cast it to an OLE function returning int */

    if (arg1) {
        setMsgFields(env, arg1, arg11, &PGLOB(MsgFc));
    }
    if (arg5) {
        setRectFields(env, arg5, arg51, &PGLOB(RectFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (IILorg/eclipse/swt/internal/win32/MSG;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__IILorg_eclipse_swt_internal_win32_MSG_2
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jobject arg0)
{
	DECL_GLOB(pGlob)
    MSG msg, *arg01=NULL;
    P_OLE_FN_2 fn; /* this is a function that returns int */
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_VtblCall__IILorg_eclipse_swt_internal_win32_MSG_2\n");
#endif

    fn = (P_OLE_FN_2)(*(int **)ppVtbl)[fnNumber];
    if (arg0) {
        arg01=&msg;
        cacheMsgFids(env, arg0, &PGLOB(MsgFc));
        getMsgFields(env, arg0, arg01, &PGLOB(MsgFc));
    }
    
    rc = fn(ppVtbl,  (jint)arg01); /* cast it to an OLE function returning int */

    if (arg0) {
        setMsgFields(env, arg0, arg01, &PGLOB(MsgFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (IIILorg/eclipse/swt/internal/win32/SIZE;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__IIILorg_eclipse_swt_internal_win32_SIZE_2
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jint arg0, jobject arg1)
{
	DECL_GLOB(pGlob)
    SIZE size, *arg11=NULL;
    P_OLE_FN_3 fn; /* this is a function that returns int */
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_VtblCall__IIILorg_eclipse_swt_internal_win32_SIZE_2\n");
#endif

    fn = (P_OLE_FN_3)(*(int **)ppVtbl)[fnNumber];
    if (arg1) {
        arg11=&size;
        cacheSizeFids(env, arg1, &PGLOB(SizeFc));
        getSizeFields(env, arg1, arg11, &PGLOB(SizeFc));
    }
    
    rc = fn(ppVtbl, arg0, (jint)arg11); /* cast it to an COM function returning int */

    if (arg1) {
        setSizeFields(env, arg1, arg11, &PGLOB(SizeFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (IIIZ)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__IIIZ
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jint arg0, jboolean arg1)
{
    P_OLE_FN_3 fn; /* this is a function that returns int */
    jint rc;
    
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_VtblCall__IIIZ\n");
#endif
    fn = (P_OLE_FN_3)(*(int **)ppVtbl)[fnNumber];

    rc = fn(ppVtbl, arg0, arg1); /* cast it to an OLE function returning int */
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (IILorg/eclipse/swt/internal/ole/win32/CAUUID;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__IILorg_eclipse_swt_internal_ole_win32_CAUUID_2
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jobject arg0)
{
	DECL_GLOB(pGlob)
    P_OLE_FN_2 fn; /* this is a function that returns int */
    CAUUID cauuid, *arg01=&cauuid;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_VtblCall__II_3C\n");
#endif

    fn = (P_OLE_FN_2)(*(int **)ppVtbl)[fnNumber];

    if (arg0) {
        cacheCauuidFids(env, arg0, &PGLOB(CauuidFc));
        getCauuidFields(env, arg0, arg01, &PGLOB(CauuidFc));
    }
    
    rc = fn(ppVtbl, (jint)arg01); /* cast it to an OLE function returning int */

    if (arg0) {
        setCauuidFields(env, arg0, arg01, &PGLOB(CauuidFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (IILorg/eclipse/swt/internal/ole/win32/CONTROLINFO;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__IILorg_eclipse_swt_internal_ole_win32_CONTROLINFO_2
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jobject arg0)
{
	DECL_GLOB(pGlob)
    CONTROLINFO controlinfo, *arg01=NULL;
    P_OLE_FN_2 fn; /* this is a function that returns int */
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_VtblCall__IILorg_eclipse_swt_internal_ole_win32_CONTROLINFO_2\n");
#endif
    fn = (P_OLE_FN_2)(*(int **)ppVtbl)[fnNumber];
    if (arg0) {
        arg01=&controlinfo;
        cacheControlinfoFids(env, arg0, &PGLOB(ControlinfoFc));
        getControlinfoFields(env, arg0, arg01, &PGLOB(ControlinfoFc));
    }
    
    rc = fn(ppVtbl, (jint)arg01); /* cast it to an OLE function returning int */

    if (arg0) {
        setControlinfoFields(env, arg0, arg01, &PGLOB(ControlinfoFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (IILorg/eclipse/swt/internal/ole/win32/FORMATETC;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jobject arg0)
{
	DECL_GLOB(pGlob)
    P_OLE_FN_2 fn;
    FORMATETC formatetc, *arg01=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2\n");
#endif

    fn = (P_OLE_FN_2)(*(int **)ppVtbl)[fnNumber];

    if (arg0) {
        arg01 = &formatetc;
        cacheFormatetcFids(env, arg0, &PGLOB(FormatetcFc));
        getFormatetcFields(env, arg0, arg01, &PGLOB(FormatetcFc));
    }
    rc = fn(ppVtbl, (jint)arg01);

    if (arg0) {
        setFormatetcFields(env, arg0, arg01, &PGLOB(FormatetcFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (IILorg/eclipse/swt/internal/ole/win32/FORMATETC;Lorg/eclipse/swt/internal/ole/win32/STGMEDIUM;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jobject arg0, jobject arg1)
{
	DECL_GLOB(pGlob)
    P_OLE_FN_3 fn;
    FORMATETC formatetc, *arg01=NULL;
    STGMEDIUM stgmedium, *arg11=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2\n");
#endif

    fn = (P_OLE_FN_3)(*(int **)ppVtbl)[fnNumber];

    if (arg0) {
        arg01 = &formatetc;
        cacheFormatetcFids(env, arg0, &PGLOB(FormatetcFc));
        getFormatetcFields(env, arg0, arg01, &PGLOB(FormatetcFc));
    }
    if (arg1) {
        arg11 = &stgmedium;
        cacheStgmediumFids(env, arg1, &PGLOB(StgmediumFc));
        getStgmediumFields(env, arg1, arg11, &PGLOB(StgmediumFc));
    }
    rc = fn(ppVtbl, (jint)arg01, (jint)arg11);

    if (arg0) {
        setFormatetcFields(env, arg0, arg01, &PGLOB(FormatetcFc));
    }
    if (arg1) {
        setStgmediumFields(env, arg1, arg11, &PGLOB(StgmediumFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (IILorg/eclipse/swt/internal/ole/win32/FORMATETC;Lorg/eclipse/swt/internal/ole/win32/STGMEDIUM;Z)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2Z
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jobject arg0, jobject arg1, jboolean arg2)
{
	DECL_GLOB(pGlob)
    P_OLE_FN_4 fn;
    FORMATETC formatetc, *arg01=NULL;
    STGMEDIUM stgmedium, *arg11=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2Z\n");
#endif

    fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];

    if (arg0) {
        arg01 = &formatetc;
        cacheFormatetcFids(env, arg0, &PGLOB(FormatetcFc));
        getFormatetcFields(env, arg0, arg01, &PGLOB(FormatetcFc));
    }
    if (arg1) {
        arg11 = &stgmedium;
        cacheStgmediumFids(env, arg1, &PGLOB(StgmediumFc));
        getStgmediumFields(env, arg1, arg11, &PGLOB(StgmediumFc));
    }
    rc = fn(ppVtbl, (jint)arg01, (jint)arg11, (jint)arg2);

    if (arg0) {
        setFormatetcFields(env, arg0, arg01, &PGLOB(FormatetcFc));
    }
    if (arg1) {
        setStgmediumFields(env, arg1, arg11, &PGLOB(StgmediumFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (IILorg/eclipse/swt/internal/ole/win32/GUID;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jobject arg0)
{
	DECL_GLOB(pGlob)
    GUID guid, *arg01=NULL;
    P_OLE_FN_2 fn; /* this is a function that returns int */
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2\n");
#endif
    fn = (P_OLE_FN_2)(*(int **)ppVtbl)[fnNumber];
    if (arg0) {
        arg01=&guid;
        cacheGuidFids(env, arg0, &PGLOB(GuidFc));
        getGuidFields(env, arg0, arg01, &PGLOB(GuidFc));
    }

    rc = fn(ppVtbl, (jint)arg01); /* cast it to an OLE function returning int */

    if (arg0) {
        setGuidFields(env, arg0, arg01, &PGLOB(GuidFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (IILorg/eclipse/swt/internal/ole/win32/GUID;[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2_3I
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jobject arg0, jintArray arg1)
{
	DECL_GLOB(pGlob)
    jint *arg11=NULL;
    GUID guid, *arg01=NULL;
    P_OLE_FN_3 fn; /* this is a function that returns int */
    jint rc;
    
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2_3I\n");
#endif
    fn = (P_OLE_FN_3)(*(int **)ppVtbl)[fnNumber];

    if (arg0) {
        arg01=&guid;
        cacheGuidFids(env, arg0, &PGLOB(GuidFc));
        getGuidFields(env, arg0, arg01, &PGLOB(GuidFc));
    }
    
    if (arg1)
        arg11 = (*env)->GetIntArrayElements(env, arg1, NULL);

    rc = fn(ppVtbl, (jint)arg01, (jint)arg11); /* cast it to an OLE function returning int */

    if (arg1)
        (*env)->ReleaseIntArrayElements(env, arg1, arg11, 0);

    if (arg0) {
        setGuidFields(env, arg0, arg01, &PGLOB(GuidFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (IILorg/eclipse/swt/internal/ole/win32/GUID;III[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2III_3I
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jobject arg0, jint arg1, jint arg2, jint arg3, jintArray arg4)
{
	DECL_GLOB(pGlob)
    jint *arg41=NULL;
    GUID guid, *arg01=NULL;
    P_OLE_FN_6 fn; /* this is a function that returns int */
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2III_3I\n");
#endif
    fn = (P_OLE_FN_6)(*(int **)ppVtbl)[fnNumber];
    if (arg0) {
        arg01=&guid;
        cacheGuidFids(env, arg0, &PGLOB(GuidFc));
        getGuidFields(env, arg0, arg01, &PGLOB(GuidFc));
    }
    
    if (arg4)
        arg41 = (*env)->GetIntArrayElements(env, arg4, NULL);

    rc = fn(ppVtbl, (jint)arg01, arg1, arg2, arg3, (jint)arg41); /* cast it to an OLE function returning int */

    if (arg4)
        (*env)->ReleaseIntArrayElements(env, arg4, arg41, 0);

    if (arg0) {
        setGuidFields(env, arg0, arg01, &PGLOB(GuidFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (IILorg/eclipse/swt/internal/ole/win32/GUID;IIII)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2IIII
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jobject arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	DECL_GLOB(pGlob)
    GUID guid, *arg01=NULL;
    P_OLE_FN_6 fn; /* this is a function that returns int */
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2IIII\n");
#endif
    fn = (P_OLE_FN_6)(*(int **)ppVtbl)[fnNumber];
    if (arg0) {
        arg01=&guid;
        cacheGuidFids(env, arg0, &PGLOB(GuidFc));
        getGuidFields(env, arg0, arg01, &PGLOB(GuidFc));
    }
    
    rc = fn(ppVtbl, (jint)arg01, arg1, arg2, arg3, arg4);

    if (arg0) {
        setGuidFields(env, arg0, arg01, &PGLOB(GuidFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (IILorg/eclipse/swt/internal/ole/win32/GUID;ILorg/eclipse/swt/internal/ole/win32/OLECMD;Lorg/eclipse/swt/internal/ole/win32/OLECMDTEXT;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2ILorg_eclipse_swt_internal_ole_win32_OLECMD_2Lorg_eclipse_swt_internal_ole_win32_OLECMDTEXT_2
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jobject arg0, jint arg1, jobject arg2, jobject arg3)
{
	DECL_GLOB(pGlob)
    GUID guid, *arg01=NULL;
    OLECMD olecmd, *arg21=NULL;
    OLECMDTEXT olecmdtext, *arg31=NULL;

    P_OLE_FN_5 fn; /* this is a function that returns int */
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2ILorg_eclipse_swt_internal_ole_win32_OLECMD_2Lorg_eclipse_swt_internal_ole_win32_OLECMDTEXT_2\n");
#endif
    fn = (P_OLE_FN_5)(*(int **)ppVtbl)[fnNumber];
    if (arg0) {
        arg01=&guid;
        cacheGuidFids(env, arg0, &PGLOB(GuidFc));
        getGuidFields(env, arg0, arg01, &PGLOB(GuidFc));
    }
    if (arg2) {
        arg21=&olecmd;
        cacheOlecmdFids(env, arg2, &PGLOB(OlecmdFc));
        getOlecmdFields(env, arg2, arg21, &PGLOB(OlecmdFc));
    }
    if (arg3) {
        arg31=&olecmdtext;
        cacheOlecmdtextFids(env, arg3, &PGLOB(OlecmdtextFc));
        getOlecmdtextFields(env, arg3, arg31, &PGLOB(OlecmdtextFc));
    }
    
    rc = fn(ppVtbl, (jint)arg01, arg1, (jint)arg21, (jint)arg31);

    if (arg0) {
        setGuidFields(env, arg0, arg01, &PGLOB(GuidFc));
    }
    if (arg2) {
        setOlecmdFields(env, arg2, arg21, &PGLOB(OlecmdFc));
    }
    if (arg3) {
        setOlecmdtextFields(env, arg3, arg31, &PGLOB(OlecmdtextFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (IILorg/eclipse/swt/internal/ole/win32/LICINFO;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__IILorg_eclipse_swt_internal_ole_win32_LICINFO_2
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jobject arg0)
{
	DECL_GLOB(pGlob)
    LICINFO licinfo, *arg01=NULL;
    P_OLE_FN_2 fn; /* this is a function that returns int */
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_VtblCall__IILorg_eclipse_swt_internal_ole_win32_LICINFO_2\n");
#endif
    fn = (P_OLE_FN_2)(*(int **)ppVtbl)[fnNumber];
    if (arg0) {
        arg01=&licinfo;
        cacheLicinfoFids(env, arg0, &PGLOB(LicinfoFc));
        getLicinfoFields(env, arg0, arg01, &PGLOB(LicinfoFc));
    }

    rc = fn(ppVtbl, (jint)arg01); /* cast it to an OLE function returning int */

    if (arg0) {
        setLicinfoFields(env, arg0, arg01, &PGLOB(LicinfoFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (IILorg/eclipse/swt/internal/win32/RECT;IZ)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2IZ
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jobject arg0, jint arg1, jboolean arg2)
{
	DECL_GLOB(pGlob)
    RECT rect, *arg01=NULL;
    P_OLE_FN_4 fn; /* this is a function that returns int */
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2IZ\n");
#endif

    fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];
    if (arg0) {
        arg01=&rect;
        cacheRectFids(env, arg0, &PGLOB(RectFc));
        getRectFields(env, arg0, arg01, &PGLOB(RectFc));
    }
    
    rc = fn(ppVtbl, (jint)arg01, arg1, arg2); /* cast it to an OLE function returning int */

    if (arg0) {
        setRectFields(env, arg0, arg01, &PGLOB(RectFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (IILorg/eclipse/swt/internal/win32/RECT;Lorg/eclipse/swt/internal/win32/RECT;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2Lorg_eclipse_swt_internal_win32_RECT_2
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jobject arg0, jobject arg1)
{
	DECL_GLOB(pGlob)
    RECT rect0, *arg01=NULL;
    RECT rect1, *arg11=NULL;
    P_OLE_FN_3 fn; /* this is a function that returns int */
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2Lorg_eclipse_swt_internal_win32_RECT_2\n");
#endif

    fn = (P_OLE_FN_3)(*(int **)ppVtbl)[fnNumber];
    if (arg0) {
        arg01=&rect0;
        cacheRectFids(env, arg0, &PGLOB(RectFc));
        getRectFields(env, arg0, arg01, &PGLOB(RectFc));
    }
    if (arg1) {
        arg11=&rect1;
        cacheRectFids(env, arg1, &PGLOB(RectFc));
        getRectFields(env, arg1, arg11, &PGLOB(RectFc));
    }
    rc = fn(ppVtbl, (jint)arg01, (jint)arg11); /* cast it to an OLE function returning int */

    if (arg1) {
        setRectFields(env, arg1, arg11, &PGLOB(RectFc));
    }
    if (arg0) {
        setRectFields(env, arg0, arg01, &PGLOB(RectFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (III[I[I[I[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__III_3I_3I_3I_3I
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jint arg0, jintArray arg1, jintArray arg2, jintArray arg3, jintArray arg4)
{
	DECL_GLOB(pGlob)
    jint *arg11=NULL;
    jint *arg21=NULL;
    jint *arg31=NULL;
    jint *arg41=NULL;
    P_OLE_FN_6 fn; /* this is a function that returns int */
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_VtblCall__III_3I_3I_3I_31\n");
#endif
    fn = (P_OLE_FN_6)(*(int **)ppVtbl)[fnNumber];
    if (arg1)
        arg11 = (*env)->GetIntArrayElements(env, arg1, NULL);
        
    if (arg2)
        arg21 = (*env)->GetIntArrayElements(env, arg2, NULL);
        
    if (arg3)
        arg31 = (*env)->GetIntArrayElements(env, arg3, NULL);
        
    if (arg4)
        arg41 = (*env)->GetIntArrayElements(env, arg4, NULL);

    rc = fn(ppVtbl, arg0, (jint)arg11, (jint)arg21, (jint)arg31, (jint)arg41); /* cast it to an OLE function returning int */

    if (arg1)
        (*env)->ReleaseIntArrayElements(env, arg1, arg11, 0);
    
    if (arg2)
        (*env)->ReleaseIntArrayElements(env, arg2, arg21, 0);
        
    if (arg3)
        (*env)->ReleaseIntArrayElements(env, arg3, arg31, 0);
    
    if (arg4)
        (*env)->ReleaseIntArrayElements(env, arg4, arg41, 0);


    return rc;
}
/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (III[II[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__III_3II_3I
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jint arg0, jintArray arg1, jint arg2, jintArray arg3)
{
	DECL_GLOB(pGlob)
    jint *arg11=NULL;
    jint *arg31=NULL;
    P_OLE_FN_5 fn; /* this is a function that returns int */
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_VtblCall__III_3II_3I\n");
#endif
    fn = (P_OLE_FN_5)(*(int **)ppVtbl)[fnNumber];
    if (arg1)
        arg11 = (*env)->GetIntArrayElements(env, arg1, NULL);
        
    if (arg3)
        arg31 = (*env)->GetIntArrayElements(env, arg3, NULL);
        
    rc = fn(ppVtbl, arg0, (jint)arg11, arg2, (jint)arg31); /* cast it to an OLE function returning int */

    if (arg1)
        (*env)->ReleaseIntArrayElements(env, arg1, arg11, 0);
            
    if (arg3)
        (*env)->ReleaseIntArrayElements(env, arg3, arg31, 0);
    
    return rc;
}      
/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    WriteClassStg
 * Signature: (ILorg/eclipse/swt/internal/ole/win32/GUID;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_WriteClassStg
  (JNIEnv *env, jclass that, jint pStg, jobject rclsid)
{
	DECL_GLOB(pGlob)
    GUID guid, *rclsid1=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "WriteClassStg\n");
#endif
    if (rclsid) {
        rclsid1=&guid;
        cacheGuidFids(env, rclsid, &PGLOB(GuidFc));
        getGuidFields(env, rclsid, rclsid1, &PGLOB(GuidFc));
    }
    rc = (jint) WriteClassStg((IStorage *)pStg, rclsid1);

    if (rclsid) {
        setGuidFields(env, rclsid, rclsid1, &PGLOB(GuidFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    OleGetClipboard
 * Signature: ([I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_OleGetClipboard
  (JNIEnv *env, jclass that, jintArray ppDataObj)
{
    IDataObject **ppDataObj1=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "OleGetClipboard\n");
#endif
    if (ppDataObj)
        ppDataObj1 = (IDataObject **)(*env)->GetIntArrayElements(env, ppDataObj, NULL);

    rc = (jint) OleGetClipboard(ppDataObj1);

    if (ppDataObj)
        (*env)->ReleaseIntArrayElements(env, ppDataObj, (jint *)ppDataObj1, 0);

    return rc;
}

     /*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    OleIsCurrentClipboard
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_OleIsCurrentClipboard
  (JNIEnv *env, jclass that, jint pDataObj)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "OleIsCurrentClipboard\n");
#endif
    return (jint) OleIsCurrentClipboard((IDataObject *)pDataObj);
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    OleSetClipboard
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_OleSetClipboard
  (JNIEnv *env, jclass that, jint pDataObj)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "OleSetClipboard\n");
#endif
    return (jint) OleSetClipboard((IDataObject *)pDataObj);
}
    
/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    OleFlushClipboard
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_OleFlushClipboard
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "OleFlushClipboard\n");
#endif
    return (jint) OleFlushClipboard();
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    OleDraw
 * Signature: (IIII)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_OleDraw
  (JNIEnv *env, jclass that, jint pUnk, jint dwAspect, jint hdcDraw, jint lprcBounds)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "OleDraw\n");
#endif
    return (jint) OleDraw((LPUNKNOWN)pUnk, (DWORD)dwAspect, (HDC)hdcDraw, (LPRECT)lprcBounds);
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    ReleaseStgMedium
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_ReleaseStgMedium
  (JNIEnv *env, jclass that, jint pmedium)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "ReleaseStgMedium\n");
#endif
    ReleaseStgMedium((STGMEDIUM *)pmedium);
}

/* ------------- obsolete OLE calls ------------- */

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    DispGetParam
 * Signature: (Lorg/eclipse/swt/internal/ole/win32/DISPPARAMS;III[I)I
 */
/*JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_DispGetParam
  (JNIEnv *env, jclass that, jobject pdispparams, jint position, jint vtTarg, jint pvarResult, jintArray puArgErr)
{
    unsigned int FAR *puArgErr1=NULL;
    DISPPARAMS dispparams, *pdispparams1=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "DispGetParam\n");
#endif

    if (pdispparams) {
        pdispparams1=&dispparams;
        cacheDispparamsFids(env, pdispparams, &DispparamsFc);
        getDispparamsFields(env, pdispparams, pdispparams1, &DispparamsFc);
    }
    if (puArgErr)
        puArgErr1 = (*env)->GetIntArrayElements(env, puArgErr, NULL);

    rc = (jint) DispGetParam(pdispparams1, position, (VARTYPE)vtTarg, (VARIANT FAR *)pvarResult, puArgErr1);

    if (puArgErr)
        (*env)->ReleaseIntArrayElements(env, puArgErr, puArgErr1, 0);

    if (pdispparams) {
        setDispparamsFields(env, pdispparams, pdispparams1, &DispparamsFc);
    }
    return rc;
}
*/
/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    MoveMemory
 * Signature: (ILorg/eclipse/swt/internal/win32/SIZE;I)V
 */
/*JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_MoveMemory__ILorg_eclipse_swt_internal_win32_SIZE_2I
  (JNIEnv *env, jclass that, jint DestinationPtr, jobject Source, jint Length)
{
    SIZE size, *Source1=&size;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_MoveMemory__ILorg_eclipse_swt_internal_win32_SIZE_2I\n");
#endif

    if (Source) {
        cacheSizeFids(env, Source, &SizeFc);
        getSizeFields(env, Source, Source1, &SizeFc);
    }

    MoveMemory((PVOID)DestinationPtr, Source1, Length);
}
*/
/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (II[CI[CI)I
 */
/*JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__II_3CI_3CI
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jcharArray arg0, jint arg1, jcharArray arg2, jint arg3)
{
    P_OLE_FN_5 fn;
    jchar *arg01=NULL, *arg21=NULL;
    jint rc;
    
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_VtblCall__II_3CI_3CI\n");
#endif

    fn = (P_OLE_FN_5)(*(int **)ppVtbl)[fnNumber];

    if (arg0)
        arg01 = (*env)->GetCharArrayElements(env, arg0, NULL);

    if (arg2)
        arg21 = (*env)->GetCharArrayElements(env, arg2, NULL);

    rc = fn(ppVtbl, (jint)arg01, arg1, (jint)arg21, arg3);

    if (arg2)
        (*env)->ReleaseCharArrayElements(env, arg2, arg21, 0);

    if (arg0)
        (*env)->ReleaseCharArrayElements(env, arg0, arg01, 0);

    return rc;
}
*/
/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (IIILorg/eclipse/swt/internal/ole/win32/GUID;[I)I
 */
/*JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2_3I
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jint arg0, jobject arg1, jintArray arg2)
{
    jint *arg21=NULL;
    GUID guid, *arg11=NULL;
    P_OLE_FN_4 fn;
    jint rc;
    
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2I_3I\n");
#endif
    
    fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];
    if (arg1) {
        arg11=&guid;
        cacheGuidFids(env, arg1, &GuidFc);
        getGuidFields(env, arg1, arg11, &GuidFc);
    }
    
    if (arg2)
        arg21 = (*env)->GetIntArrayElements(env, arg2, NULL);

    rc = fn(ppVtbl, arg0, (jint)arg11, (jint)arg21);

    if (arg2)
        (*env)->ReleaseIntArrayElements(env, arg2, arg21, 0);

    if (arg1) {
        setGuidFields(env, arg1, arg11, &GuidFc);
    }
    return rc;
}
*/
/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (IIII)I
 */
/*JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__IIII
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jint arg0, jint arg1)
{
    P_OLE_FN_3 fn;
    jint rc;
    
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_VtblCall__IIII\n");
#endif

    fn = (P_OLE_FN_3)(*(int **)ppVtbl)[fnNumber];
    rc = fn(ppVtbl, arg0, arg1);
    return rc;
}
*/

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (IILorg/eclipse/swt/internal/win32/MSG;)I
 */
/*
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__IILorg_eclipse_swt_internal_win32_MSG_2
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jobject arg0)
{
    MSG msg, *arg01=NULL;
    P_OLE_FN_2 fn;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_VtblCall__IILorg_eclipse_swt_internal_ole_win32_MSG_2\n");
#endif
    fn = (P_OLE_FN_2)(*(int **)ppVtbl)[fnNumber];
    if (arg0) {
        arg01=&msg;
        cacheMsgFids(env, arg0, &MsgFc);
        getMsgFields(env, arg0, arg01, &MsgFc);
    }

    rc = fn(ppVtbl, (jint)arg01);

    if (arg0) {
        setMsgFields(env, arg0, arg01, &MsgFc);
    }
    return rc;
}
*/
/* ------------- end obsolote OLE calls ------------- */

/* ------------- deleted OLE calls ------------- */

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VtblCall
 * Signature: (IILorg/eclipse/swt/internal/ole/win32/TYPEATTR;)I
 */
/*
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VtblCall__IILorg_eclipse_swt_internal_ole_win32_TYPEATTR_2
  (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jobject arg0)
{
    TYPEATTR typeattr, *arg01=NULL;
    P_OLE_FN_2 fn;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_VtblCall__IILorg_eclipse_swt_internal_ole_win32_TYPEATTR_2\n");
#endif
    fn = (P_OLE_FN_2)(*(int **)ppVtbl)[fnNumber];
    if (arg0) {
        arg01=&typeattr;
        cacheTypeattrFids(env, arg0, &TypeattrFc);
        getTypeattrFields(env, arg0, arg01, &TypeattrFc);
    }

    rc = fn(ppVtbl, (jint)arg01);
    if (arg0) {
        setTypeattrFields(env, arg0, arg01, &TypeattrFc);
    }
    return rc;
}
*/

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    MoveMemory
 * Signature: (ILorg/eclipse/swt/internal/ole/win32/EXCEPINFO;I)V
 */
/* JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_MoveMemory__ILorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2I
  (JNIEnv *env, jclass that, jint Destination, jobject SourcePtr, jint Length)
{
    EXCEPINFO excepinfo, *SourcePtr1=&excepinfo;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_MoveMemory__ILorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2I\n");
#endif

    if (SourcePtr) {
        cacheExcepinfoFids(env, SourcePtr, &ExcepinfoFc);
        getExcepinfoFields(env, SourcePtr, SourcePtr1, &ExcepinfoFc);
    }

    MoveMemory((PVOID)Destination, SourcePtr1, Length);
}
*/
/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    ProgIDFromCLSID
 * Signature: (Lorg/eclipse/swt/internal/ole/win32/GUID;[C)I
 */
/* JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_ProgIDFromCLSID__Lorg_eclipse_swt_internal_ole_win32_GUID_2_3C
  (JNIEnv *env, jclass that, jobject pclsid, jcharArray lplpszProgID)
{
    LPOLESTR *lplpszProgID1=NULL;
    GUID guid, *pclsid1=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_ProgIDFromCLSID__Lorg_eclipse_swt_internal_ole_win32_GUID_2_3C\n");
#endif

    if (pclsid) {
        pclsid1=&guid;
        cacheGuidFids(env, pclsid, &GuidFc);
        getGuidFields(env, pclsid, pclsid1, &GuidFc);
    }

    if (lplpszProgID)
        lplpszProgID1 = (LPOLESTR *)(*env)->GetCharArrayElements(env, lplpszProgID, NULL);

    rc = (jint) ProgIDFromCLSID(pclsid1, lplpszProgID1);

    if (lplpszProgID)
        (*env)->ReleaseCharArrayElements(env, lplpszProgID, (jchar *)lplpszProgID1, 0);
    
    if (pclsid) {
        setGuidFields(env, pclsid, pclsid1, &GuidFc);
    }
    return rc;
}
*/

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    ProgIDFromCLSID
 * Signature: (Lorg/eclipse/swt/internal/ole/win32/GUID;[I)I
 */
 JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_ProgIDFromCLSID__Lorg_eclipse_swt_internal_ole_win32_GUID_2_3I
  (JNIEnv *env, jclass that, jobject pclsid, jintArray lplpszProgID)
{
	DECL_GLOB(pGlob)
    LPOLESTR *lplpszProgID1=NULL;
    GUID guid, *pclsid1=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "COM_ProgIDFromCLSID__Lorg_eclipse_swt_internal_ole_win32_GUID_2_3I\n");
#endif

    if (pclsid) {
        pclsid1=&guid;
        cacheGuidFids(env, pclsid, &PGLOB(GuidFc));
        getGuidFields(env, pclsid, pclsid1, &PGLOB(GuidFc));
    }

    if (lplpszProgID)
        lplpszProgID1 = (LPOLESTR *)(*env)->GetIntArrayElements(env, lplpszProgID, NULL);

    rc = (jint) ProgIDFromCLSID(pclsid1, lplpszProgID1);

    if (lplpszProgID)
        (*env)->ReleaseIntArrayElements(env, lplpszProgID, (jint *)lplpszProgID1, 0);
    
    if (pclsid) {
        setGuidFields(env, pclsid, pclsid1, &PGLOB(GuidFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    VariantCopyInd
 * Signature: (II)I
 */
/* JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_VariantCopyInd
  (JNIEnv *env, jclass that, jint pvarDest, jint pvarSrc)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "VariantCopyInd\n");
#endif
    return (jint) VariantCopyInd((VARIANT FAR*)pvarDest, (VARIANTARG FAR*)pvarSrc);
}
*/


/* MoveMemory__Lorg_eclipse_swt_internal_win32_NMHEADER_2II removed after OS version 0.80 */
/* 
 * Class:     org_eclipse_swt_internal_win32_OS
 * Method:    MoveMemory
 * Signature: (Lorg/eclipse/swt/internal/win32/NMHEADER;II)V
 */
/* JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_win32_OS_MoveMemory__Lorg_eclipse_swt_internal_win32_NMHEADER_2II
  (JNIEnv *env, jclass that, jobject Destination, jint Source, jint Length)
{
    NMHEADER nmheader, *lpDestination1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "MoveMemory__Lorg_eclipse_swt_internal_win32_NMHEADER_2II\n");
#endif

       MoveMemory((PVOID)&nmheader, (CONST VOID *)Source, Length);

    if (Destination) {
        lpDestination1 = &nmheader;
        cacheNmheaderFids(env, Destination, &NmheaderFc);
        setNmheaderFields(env, Destination, lpDestination1, &NmheaderFc);
    }
}
*/

/* SendMessage__IIILorg_eclipse_swt_internal_win32_HDITEM_2 removed after OS version 0.80 */
/*
 * Class:     org_eclipse_swt_internal_win32_OS
 * Method:    SendMessage
 * Signature: (IIILorg/eclipse/swt/internal/win32/HDITEM;)I
 */
/* JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SendMessage__IIILorg_eclipse_swt_internal_win32_HDITEM_2
  (JNIEnv *env, jclass that, jint hWnd, jint Msg, jint wParam, jobject lParam)
{
    HDITEM hditem, *lpParam1=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "SendMessage__IIILorg_eclipse_swt_internal_win32_HDITEM_2\n");
#endif

    if (lParam) {
        lpParam1 = &hditem;
        cacheHditemFids(env, lParam, &HditemFc);
        getHditemFields(env, lParam, lpParam1, &HditemFc);
    }
    rc = (jint) SendMessage((HWND)hWnd, Msg, wParam, (LPARAM)lpParam1);
    if (lParam) {
        setHditemFields(env, lParam, lpParam1, &HditemFc);
    }
    return rc;
}
*/

/* SendMessage__IIILorg_eclipse_swt_internal_win32_HDLAYOUT_2 removed after OS version 0.80 */
/*
 * Class:     org_eclipse_swt_internal_win32_OS
 * Method:    SendMessage
 * Signature: (IIILorg/eclipse/swt/internal/win32/HDLAYOUT;)I
 */
/* JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_win32_OS_SendMessage__IIILorg_eclipse_swt_internal_win32_HDLAYOUT_2
  (JNIEnv *env, jclass that, jint hWnd, jint Msg, jint wParam, jobject lParam)
{
    HDLAYOUT hdlayout, *lpParam1=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "SendMessage__IIILorg_eclipse_swt_internal_win32_HDLAYOUT_2\n");
#endif

    if (lParam) {
        lpParam1 = &hdlayout;
        cacheHdlayoutFids(env, lParam, &HdlayoutFc);
        getHdlayoutFields(env, lParam, lpParam1, &HdlayoutFc);
    }
    rc = (jint) SendMessage((HWND)hWnd, Msg, wParam, (LPARAM)lpParam1);
    if (lParam) {
        setHdlayoutFields(env, lParam, lpParam1, &HdlayoutFc);
    }
    return rc;
}
*/
/*
 * Class:     org_eclipse_swt_internal_ole_win32_COM
 * Method:    CoGetInstanceFromIStorage
 * Signature: (Lorg/eclipse/swt/internal/ole/win32/COSERVERINFO;Lorg/eclipse/swt/internal/ole/win32/GUID;IIIILorg/eclipse/swt/internal/ole/win32/MULTI_QI;)I
 */
/*
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_ole_win32_COM_CoGetInstanceFromIStorage
  (JNIEnv *env, jclass that, jobject pServerInfo, jobject pclsid, jint punkOuter, jint dwClsCtx,
				 jint pstg, jint cmq, jobject rgmqResults)
{
    GUID guid, *pclsid1=NULL;
    COSERVERINFO coserverinfo, *pServerInfo1=NULL;
    MULTI_QI multi_qi, *rgmqResults1=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "CoGetInstanceFromIStorage\n");
#endif

    if (pclsid) {
        pclsid1=&guid;
        cacheGuidFids(env, pclsid, &GuidFc);
        getGuidFields(env, pclsid, pclsid1, &GuidFc);
    }
    if (pServerInfo) {
        pServerInfo1=&coserverinfo;
        cacheCoserverinfoFids(env, pServerInfo, &CoserverinfoFc);
        getCoserverinfoFields(env, pServerInfo, pServerInfo1, &CoserverinfoFc);
    }
    if (rgmqResults) {
        rgmqResults1=&multi_qi;
        cacheMulti_qiFids(env, rgmqResults, &MultiqiFc);
        getMulti_qiFields(env, rgmqResults, rgmqResults1, &MultiqiFc);
    }

    rc = CoGetInstanceFromIStorage(pServerInfo1, pclsid1, (IUnknown *)punkOuter, dwClsCtx, (IStorage *)pstg, cmq, rgmqResults1);

    if (pclsid) {
        setGuidFields(env, pclsid, pclsid1, &GuidFc);
    }
    if (pServerInfo) {
        setCoserverinfoFields(env, pServerInfo, pServerInfo1, &CoserverinfoFc);
    }
    if (rgmqResults) {
        setMulti_qiFields(env, rgmqResults, rgmqResults1, &MultiqiFc);
    }
    return rc;
}
*/

