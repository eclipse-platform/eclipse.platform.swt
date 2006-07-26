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
 * -  Copyright (C) 2004, 2006 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */

#include "swt.h"
#include "xpcom_stats.h"

#ifdef NATIVE_STATS

int XPCOM_nativeFunctionCount = 132;
int XPCOM_nativeFunctionCallCount[132];
char * XPCOM_nativeFunctionNames[] = {
	"Call",
	"NS_1GetComponentManager",
	"NS_1GetServiceManager",
	"NS_1InitEmbedding",
	"NS_1NewLocalFile",
	"NS_1TermEmbedding",
	"PR_1Free",
	"PR_1Malloc",
	"VtblCall__II",
	"VtblCall__IIF",
	"VtblCall__III",
	"VtblCall__IIII",
	"VtblCall__IIIII",
	"VtblCall__IIIIII",
	"VtblCall__IIIIIII",
	"VtblCall__IIIIIIII",
	"VtblCall__IIIIIIZ",
	"VtblCall__IIIIIZ",
	"VtblCall__IIIIIZ_3CIIIIZ_3I_3I",
	"VtblCall__IIIII_3C",
	"VtblCall__IIIII_3I",
	"VtblCall__IIIIJZ",
	"VtblCall__IIIIZ",
	"VtblCall__IIII_3C",
	"VtblCall__IIII_3CIJI",
	"VtblCall__IIII_3I",
	"VtblCall__IIII_3J",
	"VtblCall__IIIJJ",
	"VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2",
	"VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2_3I",
	"VtblCall__IIIZ",
	"VtblCall__IIIZZ",
	"VtblCall__IIIZZII",
	"VtblCall__IIIZZIIIIIIZZZZSI",
	"VtblCall__IIIZ_3Z",
	"VtblCall__III_3BI_3I",
	"VtblCall__III_3B_3B_3BI_3I",
	"VtblCall__III_3B_3Z",
	"VtblCall__III_3C",
	"VtblCall__III_3CI",
	"VtblCall__III_3C_3C",
	"VtblCall__III_3C_3CI_3C_3C_3C_3C_3Z_3I",
	"VtblCall__III_3C_3CI_3I_3I_3Z",
	"VtblCall__III_3C_3C_3C_3Z",
	"VtblCall__III_3C_3C_3C_3Z_3Z",
	"VtblCall__III_3C_3C_3I",
	"VtblCall__III_3C_3C_3I_3C_3Z_3Z",
	"VtblCall__III_3C_3C_3I_3I_3C_3Z_3Z",
	"VtblCall__III_3C_3C_3Z",
	"VtblCall__III_3I",
	"VtblCall__III_3I_3I_3I_3I",
	"VtblCall__III_3Z",
	"VtblCall__IIJ",
	"VtblCall__IIJI",
	"VtblCall__IIJJ",
	"VtblCall__IIJJI",
	"VtblCall__IIJJJJJ",
	"VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2I",
	"VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2ILorg_eclipse_swt_internal_mozilla_nsID_2_3I",
	"VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3I",
	"VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3Z",
	"VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI",
	"VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_3B_3B",
	"VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3I",
	"VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3Z",
	"VtblCall__IIZ",
	"VtblCall__IIZI",
	"VtblCall__IIZ_3Z",
	"VtblCall__II_3B",
	"VtblCall__II_3BI",
	"VtblCall__II_3BILorg_eclipse_swt_internal_mozilla_nsID_2_3I",
	"VtblCall__II_3BI_3I",
	"VtblCall__II_3BJ",
	"VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2I",
	"VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3I",
	"VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3Z",
	"VtblCall__II_3BZI_3I_3Z",
	"VtblCall__II_3BZ_3I_3Z",
	"VtblCall__II_3B_3B",
	"VtblCall__II_3B_3BZ",
	"VtblCall__II_3B_3B_3BZZ_3I",
	"VtblCall__II_3B_3B_3I",
	"VtblCall__II_3B_3I",
	"VtblCall__II_3B_3I_3I",
	"VtblCall__II_3B_3I_3Z",
	"VtblCall__II_3B_3J",
	"VtblCall__II_3B_3Z",
	"VtblCall__II_3B_3Z_3I",
	"VtblCall__II_3C",
	"VtblCall__II_3CIIII",
	"VtblCall__II_3CI_3I",
	"VtblCall__II_3CZ",
	"VtblCall__II_3C_3C",
	"VtblCall__II_3C_3C_3CZ",
	"VtblCall__II_3C_3Z",
	"VtblCall__II_3F",
	"VtblCall__II_3I",
	"VtblCall__II_3I_3I",
	"VtblCall__II_3I_3I_3I",
	"VtblCall__II_3I_3I_3I_3I",
	"VtblCall__II_3I_3J",
	"VtblCall__II_3I_3J_3I",
	"VtblCall__II_3J",
	"VtblCall__II_3S",
	"VtblCall__II_3Z",
	"memmove__ILorg_eclipse_swt_internal_mozilla_nsID_2I",
	"memmove__I_3BI",
	"memmove__I_3CI",
	"memmove__I_3II",
	"memmove__I_3JI",
	"memmove__Lorg_eclipse_swt_internal_mozilla_nsID_2II",
	"memmove___3BII",
	"memmove___3B_3CI",
	"memmove___3CII",
	"memmove___3III",
	"memmove___3JII",
	"nsEmbedCString_1Length",
	"nsEmbedCString_1delete",
	"nsEmbedCString_1get",
	"nsEmbedCString_1new__",
	"nsEmbedCString_1new___3BI",
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
