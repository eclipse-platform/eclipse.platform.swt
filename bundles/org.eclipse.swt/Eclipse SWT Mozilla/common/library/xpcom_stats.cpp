/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

#include "swt.h"
#include "xpcom_stats.h"

#ifdef NATIVE_STATS

int XPCOM_nativeFunctionCount = 168;
int XPCOM_nativeFunctionCallCount[168];
char * XPCOM_nativeFunctionNames[] = {
	"Call",
	"NS_1GetComponentManager",
	"NS_1GetServiceManager",
	"NS_1InitXPCOM2",
	"NS_1NewLocalFile",
	"VtblCall__II",
	"VtblCall__IIF",
	"VtblCall__III",
	"VtblCall__IIII",
	"VtblCall__IIIII",
	"VtblCall__IIIIII",
	"VtblCall__IIIIIII",
	"VtblCall__IIIIIIII",
	"VtblCall__IIIIIIIIIIII",
	"VtblCall__IIIIIIIIIIIIIIISI",
	"VtblCall__IIIIIIII_3C_3I_3I",
	"VtblCall__IIIIIIJII",
	"VtblCall__IIIIII_3CIIIII_3I_3I",
	"VtblCall__IIIIII_3C_3BIIIII_3I_3I",
	"VtblCall__IIIIII_3C_3I_3I",
	"VtblCall__IIIIII_3I_3I",
	"VtblCall__IIIII_3C",
	"VtblCall__IIIII_3I",
	"VtblCall__IIIIJJJJ",
	"VtblCall__IIII_3B",
	"VtblCall__IIII_3C",
	"VtblCall__IIII_3CIJI",
	"VtblCall__IIII_3CJJJ",
	"VtblCall__IIII_3I",
	"VtblCall__IIII_3J",
	"VtblCall__IIIJJ",
	"VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2",
	"VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2_3I",
	"VtblCall__III_3B",
	"VtblCall__III_3BI",
	"VtblCall__III_3BI_3I",
	"VtblCall__III_3B_3B_3BI_3I",
	"VtblCall__III_3B_3C",
	"VtblCall__III_3B_3I",
	"VtblCall__III_3C",
	"VtblCall__III_3CI",
	"VtblCall__III_3C_3C",
	"VtblCall__III_3C_3CI_3C_3C_3C_3C_3I_3I",
	"VtblCall__III_3C_3CI_3I_3I_3I",
	"VtblCall__III_3C_3C_3C_3I",
	"VtblCall__III_3C_3C_3C_3I_3I",
	"VtblCall__III_3C_3C_3I",
	"VtblCall__III_3C_3C_3I_3C_3I_3I",
	"VtblCall__III_3C_3C_3I_3I_3C_3I_3I",
	"VtblCall__III_3I",
	"VtblCall__III_3I_3I_3I_3I",
	"VtblCall__IIJ",
	"VtblCall__IIJI",
	"VtblCall__IIJIIJIIIIII",
	"VtblCall__IIJIIJIIIIIIIIISJ",
	"VtblCall__IIJIIJ_3I_3J",
	"VtblCall__IIJI_3J",
	"VtblCall__IIJJ",
	"VtblCall__IIJJI",
	"VtblCall__IIJJII",
	"VtblCall__IIJJIIII",
	"VtblCall__IIJJIJ_3C_3I_3I",
	"VtblCall__IIJJI_3C",
	"VtblCall__IIJJI_3I",
	"VtblCall__IIJJJ",
	"VtblCall__IIJJJI",
	"VtblCall__IIJJJI_3CJJIJI_3J_3J",
	"VtblCall__IIJJJI_3C_3BJJIJI_3J_3J",
	"VtblCall__IIJJJJIJ_3C_3I_3J",
	"VtblCall__IIJJJJJ",
	"VtblCall__IIJJJJJJ",
	"VtblCall__IIJJJJJJJ",
	"VtblCall__IIJJ_3B",
	"VtblCall__IIJJ_3CIJI",
	"VtblCall__IIJJ_3CJJJ",
	"VtblCall__IIJJ_3I",
	"VtblCall__IIJLorg_eclipse_swt_internal_mozilla_nsID_2",
	"VtblCall__IIJLorg_eclipse_swt_internal_mozilla_nsID_2_3J",
	"VtblCall__IIJ_3B",
	"VtblCall__IIJ_3BI",
	"VtblCall__IIJ_3BJ_3J",
	"VtblCall__IIJ_3B_3B_3BJ_3J",
	"VtblCall__IIJ_3B_3C",
	"VtblCall__IIJ_3CI",
	"VtblCall__IIJ_3C_3C",
	"VtblCall__IIJ_3C_3CI_3C_3C_3C_3C_3I_3I",
	"VtblCall__IIJ_3C_3CI_3J_3I_3I",
	"VtblCall__IIJ_3C_3C_3C_3I",
	"VtblCall__IIJ_3C_3C_3C_3I_3I",
	"VtblCall__IIJ_3C_3C_3I",
	"VtblCall__IIJ_3C_3C_3J",
	"VtblCall__IIJ_3C_3C_3J_3C_3I_3I",
	"VtblCall__IIJ_3C_3C_3J_3J_3C_3I_3I",
	"VtblCall__IIJ_3I",
	"VtblCall__IIJ_3J",
	"VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2I",
	"VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2ILorg_eclipse_swt_internal_mozilla_nsID_2_3I",
	"VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2J",
	"VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2JLorg_eclipse_swt_internal_mozilla_nsID_2_3J",
	"VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3I",
	"VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3J",
	"VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI",
	"VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_3B_3B",
	"VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ",
	"VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ_3B_3B",
	"VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3I",
	"VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3J",
	"VtblCall__II_3B",
	"VtblCall__II_3BI",
	"VtblCall__II_3BII_3I_3I",
	"VtblCall__II_3BIJ_3J_3I",
	"VtblCall__II_3BILorg_eclipse_swt_internal_mozilla_nsID_2_3I",
	"VtblCall__II_3BI_3I",
	"VtblCall__II_3BI_3I_3I",
	"VtblCall__II_3BI_3J_3I",
	"VtblCall__II_3BJ",
	"VtblCall__II_3BJLorg_eclipse_swt_internal_mozilla_nsID_2_3J",
	"VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2I",
	"VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2J",
	"VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3I",
	"VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3J",
	"VtblCall__II_3B_3B",
	"VtblCall__II_3B_3BI",
	"VtblCall__II_3B_3B_3BII_3I",
	"VtblCall__II_3B_3B_3BII_3J",
	"VtblCall__II_3B_3B_3I",
	"VtblCall__II_3B_3B_3J",
	"VtblCall__II_3B_3I",
	"VtblCall__II_3B_3I_3I",
	"VtblCall__II_3B_3I_3J",
	"VtblCall__II_3B_3J",
	"VtblCall__II_3B_3J_3I",
	"VtblCall__II_3C",
	"VtblCall__II_3CIIII",
	"VtblCall__II_3CIJJJ",
	"VtblCall__II_3CI_3I",
	"VtblCall__II_3CJ_3J",
	"VtblCall__II_3C_3C",
	"VtblCall__II_3F",
	"VtblCall__II_3I",
	"VtblCall__II_3I_3I",
	"VtblCall__II_3I_3I_3I",
	"VtblCall__II_3I_3I_3I_3I",
	"VtblCall__II_3I_3J",
	"VtblCall__II_3I_3J_3I",
	"VtblCall__II_3I_3J_3J",
	"VtblCall__II_3J",
	"VtblCall__II_3J_3J_3J",
	"VtblCall__II_3S",
	"XPCOMGlueShutdown",
	"XPCOMGlueStartup",
	"memmove__ILorg_eclipse_swt_internal_mozilla_nsID_2I",
	"memmove__Lorg_eclipse_swt_internal_mozilla_nsID_2II",
	"nsEmbedCString_1Length",
	"nsEmbedCString_1delete",
	"nsEmbedCString_1get",
	"nsEmbedCString_1new__",
	"nsEmbedCString_1new__II",
	"nsEmbedCString_1new___3BI",
	"nsEmbedString_1Length",
	"nsEmbedString_1delete",
	"nsEmbedString_1get",
	"nsEmbedString_1new__",
	"nsEmbedString_1new___3C",
	"nsID_1Equals",
	"nsID_1delete",
	"nsID_1new",
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
