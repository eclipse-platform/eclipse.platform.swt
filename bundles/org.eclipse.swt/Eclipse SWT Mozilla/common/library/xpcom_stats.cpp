/* ***** BEGIN LICENSE BLOCK *****
* Version: MPL 1.1
*
* The contents of this file are subject to the Mozilla Public License Version
* 1.1 (the "License"); you may not use this file except in compliance with
* the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/
*
* Software distributed under the License is distributed on an "AS IS" basis,
* WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
* for the specific language governing rights and limitations under the
* License.
*
* The Original Code is Mozilla Communicator client code, released March 31, 1998.
*
* The Initial Developer of the Original Code is
* Netscape Communications Corporation.
* Portions created by Netscape are Copyright (C) 1998-1999
* Netscape Communications Corporation.  All Rights Reserved.
*
* Contributor(s):
*
* IBM
* -  Binding to permit interfacing between Mozilla and SWT
* -  Copyright (C) 2003, 2004 IBM Corp.  All Rights Reserved.
*
* ***** END LICENSE BLOCK ***** */

#include "swt.h"
#include "xpcom_stats.h"

#ifdef NATIVE_STATS

int XPCOM_nativeFunctionCount = 164;
int XPCOM_nativeFunctionCallCount[164];
char * XPCOM_nativeFunctionNames[] = {
	"Call",
	"NS_1GetComponentManager",
	"NS_1GetServiceManager",
	"NS_1InitEmbedding",
	"NS_1NewLocalFile",
	"NS_1TermEmbedding",
	"PR_1Free",
	"PR_1GetEnv",
	"PR_1Malloc",
	"VtblCall__II",
	"VtblCall__IID",
	"VtblCall__IIF",
	"VtblCall__III",
	"VtblCall__IIII",
	"VtblCall__IIIII",
	"VtblCall__IIIIII",
	"VtblCall__IIIIIII",
	"VtblCall__IIIIIIII",
	"VtblCall__IIIIIIIII",
	"VtblCall__IIIIIIIII_3Z",
	"VtblCall__IIIIIIZ",
	"VtblCall__IIIIII_3I",
	"VtblCall__IIIIIZ",
	"VtblCall__IIIIIZ_3I",
	"VtblCall__IIIIIZ_3Z",
	"VtblCall__IIIII_3C",
	"VtblCall__IIIII_3I",
	"VtblCall__IIIII_3Z",
	"VtblCall__IIIIZ",
	"VtblCall__IIIIZI",
	"VtblCall__IIIIZ_3I",
	"VtblCall__IIII_3B_3I",
	"VtblCall__IIII_3C",
	"VtblCall__IIII_3I",
	"VtblCall__IIII_3S",
	"VtblCall__IIII_3Z",
	"VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2",
	"VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2Z",
	"VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2_3I",
	"VtblCall__IIISSS_3Z",
	"VtblCall__IIIZ",
	"VtblCall__IIIZI_3Z",
	"VtblCall__IIIZZ",
	"VtblCall__IIIZZII",
	"VtblCall__IIIZZIIIIIIZZZZSI",
	"VtblCall__IIIZZIIIIS",
	"VtblCall__IIIZZIZZZZII",
	"VtblCall__IIIZZZZZZ_3Z",
	"VtblCall__IIIZ_3C",
	"VtblCall__IIIZ_3I",
	"VtblCall__IIIZ_3Z",
	"VtblCall__III_3B",
	"VtblCall__III_3BI",
	"VtblCall__III_3BI_3I",
	"VtblCall__III_3BZ",
	"VtblCall__III_3B_3C",
	"VtblCall__III_3C",
	"VtblCall__III_3CI_3C_3I",
	"VtblCall__III_3CZ",
	"VtblCall__III_3C_3C",
	"VtblCall__III_3C_3C_3I",
	"VtblCall__III_3C_3I",
	"VtblCall__III_3C_3Z",
	"VtblCall__III_3I",
	"VtblCall__III_3I_3I_3I_3I",
	"VtblCall__III_3S",
	"VtblCall__III_3Z",
	"VtblCall__IIJ",
	"VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2",
	"VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2ILorg_eclipse_swt_internal_mozilla_nsID_2_3I",
	"VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3I",
	"VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3Z",
	"VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI",
	"VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3I",
	"VtblCall__IISF",
	"VtblCall__IISI",
	"VtblCall__IIS_3F",
	"VtblCall__IIZ",
	"VtblCall__IIZI_3Z",
	"VtblCall__IIZSSS_3Z",
	"VtblCall__IIZZ",
	"VtblCall__IIZZZ",
	"VtblCall__IIZ_3I",
	"VtblCall__IIZ_3I_3I",
	"VtblCall__II_3B",
	"VtblCall__II_3BI",
	"VtblCall__II_3BII",
	"VtblCall__II_3BII_3BII_3I_3I",
	"VtblCall__II_3BILorg_eclipse_swt_internal_mozilla_nsID_2_3I",
	"VtblCall__II_3BIZ",
	"VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2I",
	"VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3I",
	"VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3Z",
	"VtblCall__II_3BZ",
	"VtblCall__II_3B_3B",
	"VtblCall__II_3B_3B_3I",
	"VtblCall__II_3B_3C",
	"VtblCall__II_3B_3I",
	"VtblCall__II_3B_3Z",
	"VtblCall__II_3C",
	"VtblCall__II_3CI",
	"VtblCall__II_3CII",
	"VtblCall__II_3CIIII",
	"VtblCall__II_3CIZ",
	"VtblCall__II_3CI_3C",
	"VtblCall__II_3CI_3I",
	"VtblCall__II_3CLorg_eclipse_swt_internal_mozilla_nsID_2I",
	"VtblCall__II_3CLorg_eclipse_swt_internal_mozilla_nsID_2_3I",
	"VtblCall__II_3CZ",
	"VtblCall__II_3C_3C",
	"VtblCall__II_3C_3CI_3C_3C_3C_3C_3Z_3I",
	"VtblCall__II_3C_3CI_3C_3I_3Z",
	"VtblCall__II_3C_3C_3C",
	"VtblCall__II_3C_3C_3CI_3I",
	"VtblCall__II_3C_3C_3CZ",
	"VtblCall__II_3C_3C_3C_3C",
	"VtblCall__II_3C_3C_3C_3I",
	"VtblCall__II_3C_3C_3C_3Z",
	"VtblCall__II_3C_3C_3C_3Z_3Z",
	"VtblCall__II_3C_3C_3I",
	"VtblCall__II_3C_3C_3Z",
	"VtblCall__II_3C_3I",
	"VtblCall__II_3C_3I_3I",
	"VtblCall__II_3C_3Z",
	"VtblCall__II_3C_3Z_3I",
	"VtblCall__II_3D",
	"VtblCall__II_3F",
	"VtblCall__II_3I",
	"VtblCall__II_3II",
	"VtblCall__II_3I_3I",
	"VtblCall__II_3I_3I_3I_3I",
	"VtblCall__II_3J",
	"VtblCall__II_3S",
	"VtblCall__II_3Z",
	"VtblCall__II_3Z_3Z_3Z",
	"VtblCallNoRet__III",
	"VtblCallNoRet__IIII",
	"VtblCallNoRet__II_3I_3I",
	"memmove__ILorg_eclipse_swt_internal_mozilla_nsID_2I",
	"memmove__I_3BI",
	"memmove__I_3CI",
	"memmove__I_3II",
	"memmove__Lorg_eclipse_swt_internal_mozilla_nsID_2II",
	"memmove___3BII",
	"memmove___3B_3CI",
	"memmove___3CII",
	"memmove___3III",
	"nsEmbedCString_1Length",
	"nsEmbedCString_1delete",
	"nsEmbedCString_1get",
	"nsEmbedCString_1new__",
	"nsEmbedCString_1new___3BI",
	"nsEmbedString_1Equals",
	"nsEmbedString_1Length",
	"nsEmbedString_1delete",
	"nsEmbedString_1get",
	"nsEmbedString_1new__",
	"nsEmbedString_1new___3C",
	"nsID_1Equals",
	"nsID_1Parse",
	"nsID_1delete",
	"nsID_1new",
	"strlen",
	"strlen_1PRUnichar",
};

#define STATS_NATIVE(func) Java_org_eclipse_swt_tools_internal_NativeStats_##func

JNIEXPORT jint JNICALL STATS_NATIVE(XPCOM_1GetFunctionCount)
	(JNIEnv *env, jclass that)
{
	return XPCOM_nativeFunctionCount;
}

JNIEXPORT jstring JNICALL STATS_NATIVE(XPCOM_1GetFunctionName)
	(JNIEnv *env, jclass that, jint index)
{
	return env->NewStringUTF(XPCOM_nativeFunctionNames[index]);
}

JNIEXPORT jint JNICALL STATS_NATIVE(XPCOM_1GetFunctionCallCount)
	(JNIEnv *env, jclass that, jint index)
{
	return XPCOM_nativeFunctionCallCount[index];
}

#endif
