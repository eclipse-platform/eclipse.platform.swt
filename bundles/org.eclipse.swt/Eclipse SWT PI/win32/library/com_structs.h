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

#include "com.h"

#ifndef NO_CAUUID
void cacheCAUUIDFields(JNIEnv *env, jobject lpObject);
CAUUID *getCAUUIDFields(JNIEnv *env, jobject lpObject, CAUUID *lpStruct);
void setCAUUIDFields(JNIEnv *env, jobject lpObject, CAUUID *lpStruct);
#define CAUUID_sizeof() sizeof(CAUUID)
#else
#define cacheCAUUIDFields(a,b)
#define getCAUUIDFields(a,b,c) NULL
#define setCAUUIDFields(a,b,c)
#define CAUUID_sizeof() 0
#endif

#ifndef NO_CONTROLINFO
void cacheCONTROLINFOFields(JNIEnv *env, jobject lpObject);
CONTROLINFO *getCONTROLINFOFields(JNIEnv *env, jobject lpObject, CONTROLINFO *lpStruct);
void setCONTROLINFOFields(JNIEnv *env, jobject lpObject, CONTROLINFO *lpStruct);
#define CONTROLINFO_sizeof() sizeof(CONTROLINFO)
#else
#define cacheCONTROLINFOFields(a,b)
#define getCONTROLINFOFields(a,b,c) NULL
#define setCONTROLINFOFields(a,b,c)
#define CONTROLINFO_sizeof() 0
#endif

#ifndef NO_COSERVERINFO
void cacheCOSERVERINFOFields(JNIEnv *env, jobject lpObject);
COSERVERINFO *getCOSERVERINFOFields(JNIEnv *env, jobject lpObject, COSERVERINFO *lpStruct);
void setCOSERVERINFOFields(JNIEnv *env, jobject lpObject, COSERVERINFO *lpStruct);
#define COSERVERINFO_sizeof() sizeof(COSERVERINFO)
#else
#define cacheCOSERVERINFOFields(a,b)
#define getCOSERVERINFOFields(a,b,c) NULL
#define setCOSERVERINFOFields(a,b,c)
#define COSERVERINFO_sizeof() 0
#endif

#ifndef NO_DISPPARAMS
void cacheDISPPARAMSFields(JNIEnv *env, jobject lpObject);
DISPPARAMS *getDISPPARAMSFields(JNIEnv *env, jobject lpObject, DISPPARAMS *lpStruct);
void setDISPPARAMSFields(JNIEnv *env, jobject lpObject, DISPPARAMS *lpStruct);
#define DISPPARAMS_sizeof() sizeof(DISPPARAMS)
#else
#define cacheDISPPARAMSFields(a,b)
#define getDISPPARAMSFields(a,b,c) NULL
#define setDISPPARAMSFields(a,b,c)
#define DISPPARAMS_sizeof() 0
#endif

#ifndef NO_DVTARGETDEVICE
void cacheDVTARGETDEVICEFields(JNIEnv *env, jobject lpObject);
DVTARGETDEVICE *getDVTARGETDEVICEFields(JNIEnv *env, jobject lpObject, DVTARGETDEVICE *lpStruct);
void setDVTARGETDEVICEFields(JNIEnv *env, jobject lpObject, DVTARGETDEVICE *lpStruct);
#define DVTARGETDEVICE_sizeof() sizeof(DVTARGETDEVICE)
#else
#define cacheDVTARGETDEVICEFields(a,b)
#define getDVTARGETDEVICEFields(a,b,c) NULL
#define setDVTARGETDEVICEFields(a,b,c)
#define DVTARGETDEVICE_sizeof() 0
#endif

#ifndef NO_EXCEPINFO
void cacheEXCEPINFOFields(JNIEnv *env, jobject lpObject);
EXCEPINFO *getEXCEPINFOFields(JNIEnv *env, jobject lpObject, EXCEPINFO *lpStruct);
void setEXCEPINFOFields(JNIEnv *env, jobject lpObject, EXCEPINFO *lpStruct);
#define EXCEPINFO_sizeof() sizeof(EXCEPINFO)
#else
#define cacheEXCEPINFOFields(a,b)
#define getEXCEPINFOFields(a,b,c) NULL
#define setEXCEPINFOFields(a,b,c)
#define EXCEPINFO_sizeof() 0
#endif

#ifndef NO_FORMATETC
void cacheFORMATETCFields(JNIEnv *env, jobject lpObject);
FORMATETC *getFORMATETCFields(JNIEnv *env, jobject lpObject, FORMATETC *lpStruct);
void setFORMATETCFields(JNIEnv *env, jobject lpObject, FORMATETC *lpStruct);
#define FORMATETC_sizeof() sizeof(FORMATETC)
#else
#define cacheFORMATETCFields(a,b)
#define getFORMATETCFields(a,b,c) NULL
#define setFORMATETCFields(a,b,c)
#define FORMATETC_sizeof() 0
#endif

#ifndef NO_FUNCDESC
void cacheFUNCDESCFields(JNIEnv *env, jobject lpObject);
FUNCDESC *getFUNCDESCFields(JNIEnv *env, jobject lpObject, FUNCDESC *lpStruct);
void setFUNCDESCFields(JNIEnv *env, jobject lpObject, FUNCDESC *lpStruct);
#define FUNCDESC_sizeof() sizeof(FUNCDESC)
#else
#define cacheFUNCDESCFields(a,b)
#define getFUNCDESCFields(a,b,c) NULL
#define setFUNCDESCFields(a,b,c)
#define FUNCDESC_sizeof() 0
#endif

#ifndef NO_GUID
void cacheGUIDFields(JNIEnv *env, jobject lpObject);
GUID *getGUIDFields(JNIEnv *env, jobject lpObject, GUID *lpStruct);
void setGUIDFields(JNIEnv *env, jobject lpObject, GUID *lpStruct);
#define GUID_sizeof() sizeof(GUID)
#else
#define cacheGUIDFields(a,b)
#define getGUIDFields(a,b,c) NULL
#define setGUIDFields(a,b,c)
#define GUID_sizeof() 0
#endif

#ifndef NO_LICINFO
void cacheLICINFOFields(JNIEnv *env, jobject lpObject);
LICINFO *getLICINFOFields(JNIEnv *env, jobject lpObject, LICINFO *lpStruct);
void setLICINFOFields(JNIEnv *env, jobject lpObject, LICINFO *lpStruct);
#define LICINFO_sizeof() sizeof(LICINFO)
#else
#define cacheLICINFOFields(a,b)
#define getLICINFOFields(a,b,c) NULL
#define setLICINFOFields(a,b,c)
#define LICINFO_sizeof() 0
#endif

#ifndef NO_OLECMD
void cacheOLECMDFields(JNIEnv *env, jobject lpObject);
OLECMD *getOLECMDFields(JNIEnv *env, jobject lpObject, OLECMD *lpStruct);
void setOLECMDFields(JNIEnv *env, jobject lpObject, OLECMD *lpStruct);
#define OLECMD_sizeof() sizeof(OLECMD)
#else
#define cacheOLECMDFields(a,b)
#define getOLECMDFields(a,b,c) NULL
#define setOLECMDFields(a,b,c)
#define OLECMD_sizeof() 0
#endif

#ifndef NO_OLECMDTEXT
void cacheOLECMDTEXTFields(JNIEnv *env, jobject lpObject);
OLECMDTEXT *getOLECMDTEXTFields(JNIEnv *env, jobject lpObject, OLECMDTEXT *lpStruct);
void setOLECMDTEXTFields(JNIEnv *env, jobject lpObject, OLECMDTEXT *lpStruct);
#define OLECMDTEXT_sizeof() sizeof(OLECMDTEXT)
#else
#define cacheOLECMDTEXTFields(a,b)
#define getOLECMDTEXTFields(a,b,c) NULL
#define setOLECMDTEXTFields(a,b,c)
#define OLECMDTEXT_sizeof() 0
#endif

#ifndef NO_OLEINPLACEFRAMEINFO
void cacheOLEINPLACEFRAMEINFOFields(JNIEnv *env, jobject lpObject);
OLEINPLACEFRAMEINFO *getOLEINPLACEFRAMEINFOFields(JNIEnv *env, jobject lpObject, OLEINPLACEFRAMEINFO *lpStruct);
void setOLEINPLACEFRAMEINFOFields(JNIEnv *env, jobject lpObject, OLEINPLACEFRAMEINFO *lpStruct);
#define OLEINPLACEFRAMEINFO_sizeof() sizeof(OLEINPLACEFRAMEINFO)
#else
#define cacheOLEINPLACEFRAMEINFOFields(a,b)
#define getOLEINPLACEFRAMEINFOFields(a,b,c) NULL
#define setOLEINPLACEFRAMEINFOFields(a,b,c)
#define OLEINPLACEFRAMEINFO_sizeof() 0
#endif

#ifndef NO_STATSTG
void cacheSTATSTGFields(JNIEnv *env, jobject lpObject);
STATSTG *getSTATSTGFields(JNIEnv *env, jobject lpObject, STATSTG *lpStruct);
void setSTATSTGFields(JNIEnv *env, jobject lpObject, STATSTG *lpStruct);
#define STATSTG_sizeof() sizeof(STATSTG)
#else
#define cacheSTATSTGFields(a,b)
#define getSTATSTGFields(a,b,c) NULL
#define setSTATSTGFields(a,b,c)
#define STATSTG_sizeof() 0
#endif

#ifndef NO_STGMEDIUM
void cacheSTGMEDIUMFields(JNIEnv *env, jobject lpObject);
STGMEDIUM *getSTGMEDIUMFields(JNIEnv *env, jobject lpObject, STGMEDIUM *lpStruct);
void setSTGMEDIUMFields(JNIEnv *env, jobject lpObject, STGMEDIUM *lpStruct);
#define STGMEDIUM_sizeof() sizeof(STGMEDIUM)
#else
#define cacheSTGMEDIUMFields(a,b)
#define getSTGMEDIUMFields(a,b,c) NULL
#define setSTGMEDIUMFields(a,b,c)
#define STGMEDIUM_sizeof() 0
#endif

#ifndef NO_TYPEATTR
void cacheTYPEATTRFields(JNIEnv *env, jobject lpObject);
TYPEATTR *getTYPEATTRFields(JNIEnv *env, jobject lpObject, TYPEATTR *lpStruct);
void setTYPEATTRFields(JNIEnv *env, jobject lpObject, TYPEATTR *lpStruct);
#define TYPEATTR_sizeof() sizeof(TYPEATTR)
#else
#define cacheTYPEATTRFields(a,b)
#define getTYPEATTRFields(a,b,c) NULL
#define setTYPEATTRFields(a,b,c)
#define TYPEATTR_sizeof() 0
#endif

#ifndef NO_VARDESC
void cacheVARDESCFields(JNIEnv *env, jobject lpObject);
VARDESC *getVARDESCFields(JNIEnv *env, jobject lpObject, VARDESC *lpStruct);
void setVARDESCFields(JNIEnv *env, jobject lpObject, VARDESC *lpStruct);
#define VARDESC_sizeof() sizeof(VARDESC)
#else
#define cacheVARDESCFields(a,b)
#define getVARDESCFields(a,b,c) NULL
#define setVARDESCFields(a,b,c)
#define VARDESC_sizeof() 0
#endif

#ifndef NO_VARIANT
void cacheVARIANTFields(JNIEnv *env, jobject lpObject);
VARIANT *getVARIANTFields(JNIEnv *env, jobject lpObject, VARIANT *lpStruct);
void setVARIANTFields(JNIEnv *env, jobject lpObject, VARIANT *lpStruct);
#define VARIANT_sizeof() sizeof(VARIANT)
#else
#define cacheVARIANTFields(a,b)
#define getVARIANTFields(a,b,c) NULL
#define setVARIANTFields(a,b,c)
#define VARIANT_sizeof() 0
#endif

