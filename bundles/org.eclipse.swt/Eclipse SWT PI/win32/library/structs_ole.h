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

#include "structs.h"

/* used to cast Vtabl entries */

/* P_OLE_FN_x typedef for an OLE function returning int with x params*/
typedef jint (STDMETHODCALLTYPE *P_OLE_FN_9)(jint, jint, jint, jint, jint, jint, jint, jint, jint);
typedef jint (STDMETHODCALLTYPE *P_OLE_FN_8)(jint, jint, jint, jint, jint, jint, jint, jint);
typedef jint (STDMETHODCALLTYPE *P_OLE_FN_7)(jint, jint, jint, jint, jint, jint, jint);
typedef jint (STDMETHODCALLTYPE *P_OLE_FN_6)(jint, jint, jint, jint, jint, jint);
typedef jint (STDMETHODCALLTYPE *P_OLE_FN_5)(jint, jint, jint, jint, jint);
typedef jint (STDMETHODCALLTYPE *P_OLE_FN_4)(jint, jint, jint, jint);
typedef jint (STDMETHODCALLTYPE *P_OLE_FN_3)(jint, jint, jint);
typedef jint (STDMETHODCALLTYPE *P_OLE_FN_2)(jint, jint);
typedef jint (STDMETHODCALLTYPE *P_OLE_FN_1)(jint);
typedef jint (STDMETHODCALLTYPE *P_OLE_FN_0)(void);

#ifndef NO_CAUUID
CAUUID *getCAUUIDFields(JNIEnv *env, jobject lpObject, CAUUID *lpStruct);
void setCAUUIDFields(JNIEnv *env, jobject lpObject, CAUUID *lpStruct);
#endif /* NO_CAUUID */

#ifndef NO_CONTROLINFO
CONTROLINFO *getCONTROLINFOFields(JNIEnv *env, jobject lpObject, CONTROLINFO *lpStruct);
void setCONTROLINFOFields(JNIEnv *env, jobject lpObject, CONTROLINFO *lpStruct);
#endif /* NO_CONTROLINFO */

#ifndef NO_COSERVERINFO
COSERVERINFO *getCOSERVERINFOFields(JNIEnv *env, jobject lpObject, COSERVERINFO *lpStruct);
void setCOSERVERINFOFields(JNIEnv *env, jobject lpObject, COSERVERINFO *lpStruct);
#endif /* NO_COSERVERINFO */

#ifndef NO_DISPPARAMS
DISPPARAMS *getDISPPARAMSFields(JNIEnv *env, jobject lpObject, DISPPARAMS *lpStruct);
void setDISPPARAMSFields(JNIEnv *env, jobject lpObject, DISPPARAMS *lpStruct);
#endif /* NO_DISPPARAMS */

#ifndef NO_DVTARGETDEVICE
DVTARGETDEVICE *getDVTARGETDEVICEFields(JNIEnv *env, jobject lpObject, DVTARGETDEVICE *lpStruct);
void setDVTARGETDEVICEFields(JNIEnv *env, jobject lpObject, DVTARGETDEVICE *lpStruct);
#endif /* NO_DVTARGETDEVICE */

#ifndef NO_EXCEPINFO
EXCEPINFO *getEXCEPINFOFields(JNIEnv *env, jobject lpObject, EXCEPINFO *lpStruct);
void setEXCEPINFOFields(JNIEnv *env, jobject lpObject, EXCEPINFO *lpStruct);
#endif /* NO_EXCEPINFO */

#ifndef NO_FORMATETC
FORMATETC *getFORMATETCFields(JNIEnv *env, jobject lpObject, FORMATETC *lpStruct);
void setFORMATETCFields(JNIEnv *env, jobject lpObject, FORMATETC *lpStruct);
#endif /* NO_FORMATETC */

#ifndef NO_FUNCDESC1
FUNCDESC *getFUNCDESC1Fields(JNIEnv *env, jobject lpObject, FUNCDESC *lpStruct);
void setFUNCDESC1Fields(JNIEnv *env, jobject lpObject, FUNCDESC *lpStruct);
#endif /* NO_FUNCDESC1 */

#ifndef NO_FUNCDESC2
FUNCDESC *getFUNCDESC2Fields(JNIEnv *env, jobject lpObject, FUNCDESC *lpStruct);
void setFUNCDESC2Fields(JNIEnv *env, jobject lpObject, FUNCDESC *lpStruct);
#endif /* NO_FUNCDESC2 */

#ifndef NO_GUID
GUID *getGUIDFields(JNIEnv *env, jobject lpObject, GUID *lpStruct);
void setGUIDFields(JNIEnv *env, jobject lpObject, GUID *lpStruct);
#endif /* NO_GUID */

#ifndef NO_LICINFO
LICINFO *getLICINFOFields(JNIEnv *env, jobject lpObject, LICINFO *lpStruct);
void setLICINFOFields(JNIEnv *env, jobject lpObject, LICINFO *lpStruct);
#endif /* NO_LICINFO */

#ifndef NO_OLECMD
OLECMD *getOLECMDFields(JNIEnv *env, jobject lpObject, OLECMD *lpStruct);
void setOLECMDFields(JNIEnv *env, jobject lpObject, OLECMD *lpStruct);
#endif /* NO_OLECMD */

#ifndef NO_OLECMDTEXT
OLECMDTEXT *getOLECMDTEXTFields(JNIEnv *env, jobject lpObject, OLECMDTEXT *lpStruct);
void setOLECMDTEXTFields(JNIEnv *env, jobject lpObject, OLECMDTEXT *lpStruct);
#endif /* NO_OLECMDTEXT */

#ifndef NO_OLEINPLACEFRAMEINFO
OLEINPLACEFRAMEINFO *getOLEINPLACEFRAMEINFOFields(JNIEnv *env, jobject lpObject, OLEINPLACEFRAMEINFO *lpStruct);
void setOLEINPLACEFRAMEINFOFields(JNIEnv *env, jobject lpObject, OLEINPLACEFRAMEINFO *lpStruct);
#endif /* NO_OLEINPLACEFRAMEINFO */

#ifndef NO_STATSTG
STATSTG *getSTATSTGFields(JNIEnv *env, jobject lpObject, STATSTG *lpStruct);
void setSTATSTGFields(JNIEnv *env, jobject lpObject, STATSTG *lpStruct);
#endif /* NO_STATSTG */

#ifndef NO_STGMEDIUM
STGMEDIUM *getSTGMEDIUMFields(JNIEnv *env, jobject lpObject, STGMEDIUM *lpStruct);
void setSTGMEDIUMFields(JNIEnv *env, jobject lpObject, STGMEDIUM *lpStruct);
#endif /* NO_STGMEDIUM */

#ifndef NO_TYPEATTR
TYPEATTR *getTYPEATTRFields(JNIEnv *env, jobject lpObject, TYPEATTR *lpStruct);
void setTYPEATTRFields(JNIEnv *env, jobject lpObject, TYPEATTR *lpStruct);
#endif /* NO_TYPEATTR */

#ifndef NO_VARDESC1
VARDESC *getVARDESC1Fields(JNIEnv *env, jobject lpObject, VARDESC *lpStruct);
void setVARDESC1Fields(JNIEnv *env, jobject lpObject, VARDESC *lpStruct);
#endif /* NO_VARDESC1 */

#ifndef NO_VARDESC2
VARDESC *getVARDESC2Fields(JNIEnv *env, jobject lpObject, VARDESC *lpStruct);
void setVARDESC2Fields(JNIEnv *env, jobject lpObject, VARDESC *lpStruct);
#endif /* NO_VARDESC2 */
