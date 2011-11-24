/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
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

int XPCOM_nativeFunctionCount = 231;
int XPCOM_nativeFunctionCallCount[231];
char * XPCOM_nativeFunctionNames[] = {
#ifndef JNI64
	"_1Call__I",
#else
	"_1Call__J",
#endif
#ifndef JNI64
	"_1Call__IIIIII",
#else
	"_1Call__JJJJJI",
#endif
#ifndef JNI64
	"_1Call__III_3BII_3I",
#else
	"_1Call__JJJ_3BII_3I",
#endif
	"_1JS_1EvaluateUCScriptForPrincipals",
	"_1NS_1Free",
	"_1NS_1GetComponentManager",
	"_1NS_1GetServiceManager",
	"_1NS_1InitXPCOM2",
	"_1NS_1NewLocalFile",
#ifndef JNI64
	"_1VtblCall__II",
#else
	"_1VtblCall__IJ",
#endif
#ifndef JNI64
	"_1VtblCall__IID",
#else
	"_1VtblCall__IJD",
#endif
#ifndef JNI64
	"_1VtblCall__IIF",
#else
	"_1VtblCall__IJF",
#endif
#ifndef JNI64
	"_1VtblCall__III",
#else
	"_1VtblCall__IJI",
#endif
#ifndef JNI64
	"_1VtblCall__IIII",
#else
	"_1VtblCall__IJII",
#endif
#ifndef JNI64
	"_1VtblCall__IIIII",
#else
	"_1VtblCall__IJIII",
#endif
#ifndef JNI64
	"_1VtblCall__IIIIII",
#else
	"_1VtblCall__IJIIII",
#endif
#ifndef JNI64
	"_1VtblCall__IIIIIII",
#else
	"_1VtblCall__IJIIIII",
#endif
#ifndef JNI64
	"_1VtblCall__IIIIIIII",
#else
	"_1VtblCall__IJIIIIII",
#endif
#ifndef JNI64
	"_1VtblCall__IIIIIIIIIIII",
#else
	"_1VtblCall__IJIIIIIIIIII",
#endif
#ifndef JNI64
	"_1VtblCall__IIIIIIIIIIIIIIISI",
#else
	"_1VtblCall__IJIIIIIIIIIIIIISI",
#endif
#ifndef JNI64
	"_1VtblCall__IIIIIIIII_3I",
#else
	"_1VtblCall__IJIIIIIII_3I",
#endif
#ifndef JNI64
	"_1VtblCall__IIIIIIII_3C_3I_3I",
#else
	"_1VtblCall__IJIIIIII_3C_3I_3I",
#endif
#ifndef JNI64
	"_1VtblCall__IIIIIII_3I",
#else
	"_1VtblCall__IJIIIII_3I",
#endif
#ifndef JNI64
	"_1VtblCall__IIIIIIJII",
#else
	"_1VtblCall__IJIIIIJII",
#endif
#ifndef JNI64
	"_1VtblCall__IIIIII_3CIIIII_3I_3I",
#else
	"_1VtblCall__IJIIII_3CIIIII_3I_3I",
#endif
#ifndef JNI64
	"_1VtblCall__IIIIII_3C_3BIIIII_3I_3I",
#else
	"_1VtblCall__IJIIII_3C_3BIIIII_3I_3I",
#endif
#ifndef JNI64
	"_1VtblCall__IIIIII_3C_3I_3I",
#else
	"_1VtblCall__IJIIII_3C_3I_3I",
#endif
#ifndef JNI64
	"_1VtblCall__IIIIII_3I",
#else
	"_1VtblCall__IJIIII_3I",
#endif
#ifndef JNI64
	"_1VtblCall__IIIIII_3I_3I",
#else
	"_1VtblCall__IJIIII_3I_3I",
#endif
#ifndef JNI64
	"_1VtblCall__IIIIII_3I_3I_3I",
#else
	"_1VtblCall__IJIIII_3I_3I_3I",
#endif
#ifndef JNI64
	"_1VtblCall__IIIII_3B_3BI",
#else
	"_1VtblCall__IJIII_3B_3BI",
#endif
#ifndef JNI64
	"_1VtblCall__IIIII_3C",
#else
	"_1VtblCall__IJIII_3C",
#endif
#ifndef JNI64
	"_1VtblCall__IIIII_3I",
#else
	"_1VtblCall__IJIII_3I",
#endif
#ifndef JNI64
	"_1VtblCall__IIIII_3I_3I_3I",
#else
	"_1VtblCall__IJIII_3I_3I_3I",
#endif
#ifndef JNI64
	"_1VtblCall__IIIIJJJJ",
#else
	"_1VtblCall__IJIIJJJJ",
#endif
#ifndef JNI64
	"_1VtblCall__IIII_3B",
#else
	"_1VtblCall__IJII_3B",
#endif
#ifndef JNI64
	"_1VtblCall__IIII_3BI",
#else
	"_1VtblCall__IJII_3BI",
#endif
#ifndef JNI64
	"_1VtblCall__IIII_3BII",
#else
	"_1VtblCall__IJII_3BII",
#endif
#ifndef JNI64
	"_1VtblCall__IIII_3B_3B",
#else
	"_1VtblCall__IJII_3B_3B",
#endif
#ifndef JNI64
	"_1VtblCall__IIII_3C",
#else
	"_1VtblCall__IJII_3C",
#endif
#ifndef JNI64
	"_1VtblCall__IIII_3CIJI",
#else
	"_1VtblCall__IJII_3CIJI",
#endif
#ifndef JNI64
	"_1VtblCall__IIII_3CJJJ",
#else
	"_1VtblCall__IJII_3CJJJ",
#endif
#ifndef JNI64
	"_1VtblCall__IIII_3C_3CI_3I",
#else
	"_1VtblCall__IJII_3C_3CI_3I",
#endif
#ifndef JNI64
	"_1VtblCall__IIII_3I",
#else
	"_1VtblCall__IJII_3I",
#endif
#ifndef JNI64
	"_1VtblCall__IIII_3I_3I",
#else
	"_1VtblCall__IJII_3I_3I",
#endif
#ifndef JNI64
	"_1VtblCall__IIII_3J",
#else
	"_1VtblCall__IJII_3J",
#endif
#ifndef JNI64
	"_1VtblCall__IIIJ",
#else
	"_1VtblCall__IJIJ",
#endif
#ifndef JNI64
	"_1VtblCall__IIIJJ",
#else
	"_1VtblCall__IJIJJ",
#endif
#ifndef JNI64
	"_1VtblCall__IIIJJJJJJ_3J",
#else
	"_1VtblCall__IJIJJJJJJ_3J",
#endif
#ifndef JNI64
	"_1VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2",
#else
	"_1VtblCall__IJILorg_eclipse_swt_internal_mozilla_nsID_2",
#endif
#ifndef JNI64
	"_1VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2II_3I",
#else
	"_1VtblCall__IJILorg_eclipse_swt_internal_mozilla_nsID_2II_3I",
#endif
#ifndef JNI64
	"_1VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2_3I",
#else
	"_1VtblCall__IJILorg_eclipse_swt_internal_mozilla_nsID_2_3I",
#endif
#ifndef JNI64
	"_1VtblCall__III_3B",
#else
	"_1VtblCall__IJI_3B",
#endif
#ifndef JNI64
	"_1VtblCall__III_3BI",
#else
	"_1VtblCall__IJI_3BI",
#endif
#ifndef JNI64
	"_1VtblCall__III_3BI_3I",
#else
	"_1VtblCall__IJI_3BI_3I",
#endif
#ifndef JNI64
	"_1VtblCall__III_3BS",
#else
	"_1VtblCall__IJI_3BS",
#endif
#ifndef JNI64
	"_1VtblCall__III_3B_3B_3BI_3I",
#else
	"_1VtblCall__IJI_3B_3B_3BI_3I",
#endif
#ifndef JNI64
	"_1VtblCall__III_3B_3C",
#else
	"_1VtblCall__IJI_3B_3C",
#endif
#ifndef JNI64
	"_1VtblCall__III_3B_3I",
#else
	"_1VtblCall__IJI_3B_3I",
#endif
#ifndef JNI64
	"_1VtblCall__III_3C",
#else
	"_1VtblCall__IJI_3C",
#endif
#ifndef JNI64
	"_1VtblCall__III_3CI",
#else
	"_1VtblCall__IJI_3CI",
#endif
#ifndef JNI64
	"_1VtblCall__III_3C_3C",
#else
	"_1VtblCall__IJI_3C_3C",
#endif
#ifndef JNI64
	"_1VtblCall__III_3C_3CI_3C_3C_3C_3C_3I_3I",
#else
	"_1VtblCall__IJI_3C_3CI_3C_3C_3C_3C_3I_3I",
#endif
#ifndef JNI64
	"_1VtblCall__III_3C_3CI_3I_3I_3I",
#else
	"_1VtblCall__IJI_3C_3CI_3I_3I_3I",
#endif
#ifndef JNI64
	"_1VtblCall__III_3C_3C_3C_3I",
#else
	"_1VtblCall__IJI_3C_3C_3C_3I",
#endif
#ifndef JNI64
	"_1VtblCall__III_3C_3C_3C_3I_3I",
#else
	"_1VtblCall__IJI_3C_3C_3C_3I_3I",
#endif
#ifndef JNI64
	"_1VtblCall__III_3C_3C_3I",
#else
	"_1VtblCall__IJI_3C_3C_3I",
#endif
#ifndef JNI64
	"_1VtblCall__III_3C_3C_3I_3C_3I_3I",
#else
	"_1VtblCall__IJI_3C_3C_3I_3C_3I_3I",
#endif
#ifndef JNI64
	"_1VtblCall__III_3C_3C_3I_3I_3C_3I_3I",
#else
	"_1VtblCall__IJI_3C_3C_3I_3I_3C_3I_3I",
#endif
#ifndef JNI64
	"_1VtblCall__III_3C_3I",
#else
	"_1VtblCall__IJI_3C_3I",
#endif
#ifndef JNI64
	"_1VtblCall__III_3I",
#else
	"_1VtblCall__IJI_3I",
#endif
#ifndef JNI64
	"_1VtblCall__III_3II",
#else
	"_1VtblCall__IJI_3II",
#endif
#ifndef JNI64
	"_1VtblCall__III_3IJ",
#else
	"_1VtblCall__IJI_3IJ",
#endif
#ifndef JNI64
	"_1VtblCall__III_3I_3I_3I",
#else
	"_1VtblCall__IJI_3I_3I_3I",
#endif
#ifndef JNI64
	"_1VtblCall__III_3I_3I_3I_3I",
#else
	"_1VtblCall__IJI_3I_3I_3I_3I",
#endif
#ifndef JNI64
	"_1VtblCall__III_3I_3I_3J",
#else
	"_1VtblCall__IJI_3I_3I_3J",
#endif
#ifndef JNI64
	"_1VtblCall__III_3J",
#else
	"_1VtblCall__IJI_3J",
#endif
#ifndef JNI64
	"_1VtblCall__IIJ",
#else
	"_1VtblCall__IJJ",
#endif
#ifndef JNI64
	"_1VtblCall__IIJI",
#else
	"_1VtblCall__IJJI",
#endif
#ifndef JNI64
	"_1VtblCall__IIJIIJIIIIII",
#else
	"_1VtblCall__IJJIIJIIIIII",
#endif
#ifndef JNI64
	"_1VtblCall__IIJIIJIIIIIIIIISJ",
#else
	"_1VtblCall__IJJIIJIIIIIIIIISJ",
#endif
#ifndef JNI64
	"_1VtblCall__IIJIIJ_3I_3J",
#else
	"_1VtblCall__IJJIIJ_3I_3J",
#endif
#ifndef JNI64
	"_1VtblCall__IIJII_3I",
#else
	"_1VtblCall__IJJII_3I",
#endif
#ifndef JNI64
	"_1VtblCall__IIJIJII",
#else
	"_1VtblCall__IJJIJII",
#endif
#ifndef JNI64
	"_1VtblCall__IIJIJJ_3I_3I_3I",
#else
	"_1VtblCall__IJJIJJ_3I_3I_3I",
#endif
#ifndef JNI64
	"_1VtblCall__IIJIJ_3I_3I_3I",
#else
	"_1VtblCall__IJJIJ_3I_3I_3I",
#endif
#ifndef JNI64
	"_1VtblCall__IIJI_3J",
#else
	"_1VtblCall__IJJI_3J",
#endif
#ifndef JNI64
	"_1VtblCall__IIJI_3J_3J",
#else
	"_1VtblCall__IJJI_3J_3J",
#endif
#ifndef JNI64
	"_1VtblCall__IIJJ",
#else
	"_1VtblCall__IJJJ",
#endif
#ifndef JNI64
	"_1VtblCall__IIJJI",
#else
	"_1VtblCall__IJJJI",
#endif
#ifndef JNI64
	"_1VtblCall__IIJJII",
#else
	"_1VtblCall__IJJJII",
#endif
#ifndef JNI64
	"_1VtblCall__IIJJIIII",
#else
	"_1VtblCall__IJJJIIII",
#endif
#ifndef JNI64
	"_1VtblCall__IIJJII_3J",
#else
	"_1VtblCall__IJJJII_3J",
#endif
#ifndef JNI64
	"_1VtblCall__IIJJIJ_3C_3I_3I",
#else
	"_1VtblCall__IJJJIJ_3C_3I_3I",
#endif
#ifndef JNI64
	"_1VtblCall__IIJJI_3C",
#else
	"_1VtblCall__IJJJI_3C",
#endif
#ifndef JNI64
	"_1VtblCall__IIJJI_3I",
#else
	"_1VtblCall__IJJJI_3I",
#endif
#ifndef JNI64
	"_1VtblCall__IIJJJ",
#else
	"_1VtblCall__IJJJJ",
#endif
#ifndef JNI64
	"_1VtblCall__IIJJJI",
#else
	"_1VtblCall__IJJJJI",
#endif
#ifndef JNI64
	"_1VtblCall__IIJJJI_3CJJIJI_3J_3J",
#else
	"_1VtblCall__IJJJJI_3CJJIJI_3J_3J",
#endif
#ifndef JNI64
	"_1VtblCall__IIJJJI_3C_3BJJIJI_3J_3J",
#else
	"_1VtblCall__IJJJJI_3C_3BJJIJI_3J_3J",
#endif
#ifndef JNI64
	"_1VtblCall__IIJJJJIJ_3C_3I_3J",
#else
	"_1VtblCall__IJJJJJIJ_3C_3I_3J",
#endif
#ifndef JNI64
	"_1VtblCall__IIJJJJJ",
#else
	"_1VtblCall__IJJJJJJ",
#endif
#ifndef JNI64
	"_1VtblCall__IIJJJJJJ",
#else
	"_1VtblCall__IJJJJJJJ",
#endif
#ifndef JNI64
	"_1VtblCall__IIJJJJJJJ",
#else
	"_1VtblCall__IJJJJJJJJ",
#endif
#ifndef JNI64
	"_1VtblCall__IIJJJJJ_3J",
#else
	"_1VtblCall__IJJJJJJ_3J",
#endif
#ifndef JNI64
	"_1VtblCall__IIJJJ_3B_3BJ",
#else
	"_1VtblCall__IJJJJ_3B_3BJ",
#endif
#ifndef JNI64
	"_1VtblCall__IIJJJ_3I",
#else
	"_1VtblCall__IJJJJ_3I",
#endif
#ifndef JNI64
	"_1VtblCall__IIJJJ_3J",
#else
	"_1VtblCall__IJJJJ_3J",
#endif
#ifndef JNI64
	"_1VtblCall__IIJJ_3B",
#else
	"_1VtblCall__IJJJ_3B",
#endif
#ifndef JNI64
	"_1VtblCall__IIJJ_3BJ",
#else
	"_1VtblCall__IJJJ_3BJ",
#endif
#ifndef JNI64
	"_1VtblCall__IIJJ_3BJI",
#else
	"_1VtblCall__IJJJ_3BJI",
#endif
#ifndef JNI64
	"_1VtblCall__IIJJ_3B_3B",
#else
	"_1VtblCall__IJJJ_3B_3B",
#endif
#ifndef JNI64
	"_1VtblCall__IIJJ_3CIJI",
#else
	"_1VtblCall__IJJJ_3CIJI",
#endif
#ifndef JNI64
	"_1VtblCall__IIJJ_3CJJJ",
#else
	"_1VtblCall__IJJJ_3CJJJ",
#endif
#ifndef JNI64
	"_1VtblCall__IIJJ_3C_3CI_3J",
#else
	"_1VtblCall__IJJJ_3C_3CI_3J",
#endif
#ifndef JNI64
	"_1VtblCall__IIJJ_3I",
#else
	"_1VtblCall__IJJJ_3I",
#endif
#ifndef JNI64
	"_1VtblCall__IIJJ_3I_3I",
#else
	"_1VtblCall__IJJJ_3I_3I",
#endif
#ifndef JNI64
	"_1VtblCall__IIJJ_3J",
#else
	"_1VtblCall__IJJJ_3J",
#endif
#ifndef JNI64
	"_1VtblCall__IIJLorg_eclipse_swt_internal_mozilla_nsID_2",
#else
	"_1VtblCall__IJJLorg_eclipse_swt_internal_mozilla_nsID_2",
#endif
#ifndef JNI64
	"_1VtblCall__IIJLorg_eclipse_swt_internal_mozilla_nsID_2JJ_3J",
#else
	"_1VtblCall__IJJLorg_eclipse_swt_internal_mozilla_nsID_2JJ_3J",
#endif
#ifndef JNI64
	"_1VtblCall__IIJLorg_eclipse_swt_internal_mozilla_nsID_2_3J",
#else
	"_1VtblCall__IJJLorg_eclipse_swt_internal_mozilla_nsID_2_3J",
#endif
#ifndef JNI64
	"_1VtblCall__IIJ_3B",
#else
	"_1VtblCall__IJJ_3B",
#endif
#ifndef JNI64
	"_1VtblCall__IIJ_3BI",
#else
	"_1VtblCall__IJJ_3BI",
#endif
#ifndef JNI64
	"_1VtblCall__IIJ_3BJ",
#else
	"_1VtblCall__IJJ_3BJ",
#endif
#ifndef JNI64
	"_1VtblCall__IIJ_3BJ_3J",
#else
	"_1VtblCall__IJJ_3BJ_3J",
#endif
#ifndef JNI64
	"_1VtblCall__IIJ_3BS",
#else
	"_1VtblCall__IJJ_3BS",
#endif
#ifndef JNI64
	"_1VtblCall__IIJ_3B_3B_3BJ_3J",
#else
	"_1VtblCall__IJJ_3B_3B_3BJ_3J",
#endif
#ifndef JNI64
	"_1VtblCall__IIJ_3B_3C",
#else
	"_1VtblCall__IJJ_3B_3C",
#endif
#ifndef JNI64
	"_1VtblCall__IIJ_3CI",
#else
	"_1VtblCall__IJJ_3CI",
#endif
#ifndef JNI64
	"_1VtblCall__IIJ_3C_3C",
#else
	"_1VtblCall__IJJ_3C_3C",
#endif
#ifndef JNI64
	"_1VtblCall__IIJ_3C_3CI_3C_3C_3C_3C_3I_3I",
#else
	"_1VtblCall__IJJ_3C_3CI_3C_3C_3C_3C_3I_3I",
#endif
#ifndef JNI64
	"_1VtblCall__IIJ_3C_3CI_3J_3I_3I",
#else
	"_1VtblCall__IJJ_3C_3CI_3J_3I_3I",
#endif
#ifndef JNI64
	"_1VtblCall__IIJ_3C_3C_3C_3I",
#else
	"_1VtblCall__IJJ_3C_3C_3C_3I",
#endif
#ifndef JNI64
	"_1VtblCall__IIJ_3C_3C_3C_3I_3I",
#else
	"_1VtblCall__IJJ_3C_3C_3C_3I_3I",
#endif
#ifndef JNI64
	"_1VtblCall__IIJ_3C_3C_3I",
#else
	"_1VtblCall__IJJ_3C_3C_3I",
#endif
#ifndef JNI64
	"_1VtblCall__IIJ_3C_3C_3J",
#else
	"_1VtblCall__IJJ_3C_3C_3J",
#endif
#ifndef JNI64
	"_1VtblCall__IIJ_3C_3C_3J_3C_3I_3I",
#else
	"_1VtblCall__IJJ_3C_3C_3J_3C_3I_3I",
#endif
#ifndef JNI64
	"_1VtblCall__IIJ_3C_3C_3J_3J_3C_3I_3I",
#else
	"_1VtblCall__IJJ_3C_3C_3J_3J_3C_3I_3I",
#endif
#ifndef JNI64
	"_1VtblCall__IIJ_3C_3J",
#else
	"_1VtblCall__IJJ_3C_3J",
#endif
#ifndef JNI64
	"_1VtblCall__IIJ_3I",
#else
	"_1VtblCall__IJJ_3I",
#endif
#ifndef JNI64
	"_1VtblCall__IIJ_3J",
#else
	"_1VtblCall__IJJ_3J",
#endif
#ifndef JNI64
	"_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2I",
#else
	"_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2I",
#endif
#ifndef JNI64
	"_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2ILorg_eclipse_swt_internal_mozilla_nsID_2_3I",
#else
	"_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2ILorg_eclipse_swt_internal_mozilla_nsID_2_3I",
#endif
#ifndef JNI64
	"_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2J",
#else
	"_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2J",
#endif
#ifndef JNI64
	"_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2JLorg_eclipse_swt_internal_mozilla_nsID_2_3J",
#else
	"_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2JLorg_eclipse_swt_internal_mozilla_nsID_2_3J",
#endif
#ifndef JNI64
	"_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3I",
#else
	"_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3I",
#endif
#ifndef JNI64
	"_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3J",
#else
	"_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3J",
#endif
#ifndef JNI64
	"_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI",
#else
	"_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI",
#endif
#ifndef JNI64
	"_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_3B_3B",
#else
	"_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_3B_3B",
#endif
#ifndef JNI64
	"_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ",
#else
	"_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ",
#endif
#ifndef JNI64
	"_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ_3B_3B",
#else
	"_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ_3B_3B",
#endif
#ifndef JNI64
	"_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3I",
#else
	"_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3I",
#endif
#ifndef JNI64
	"_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3J",
#else
	"_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3J",
#endif
#ifndef JNI64
	"_1VtblCall__IISIII",
#else
	"_1VtblCall__IJSIII",
#endif
#ifndef JNI64
	"_1VtblCall__IISJIJ",
#else
	"_1VtblCall__IJSJIJ",
#endif
#ifndef JNI64
	"_1VtblCall__II_3B",
#else
	"_1VtblCall__IJ_3B",
#endif
#ifndef JNI64
	"_1VtblCall__II_3BI",
#else
	"_1VtblCall__IJ_3BI",
#endif
#ifndef JNI64
	"_1VtblCall__II_3BII_3I_3I",
#else
	"_1VtblCall__IJ_3BII_3I_3I",
#endif
#ifndef JNI64
	"_1VtblCall__II_3BIJ_3J_3I",
#else
	"_1VtblCall__IJ_3BIJ_3J_3I",
#endif
#ifndef JNI64
	"_1VtblCall__II_3BILorg_eclipse_swt_internal_mozilla_nsID_2_3I",
#else
	"_1VtblCall__IJ_3BILorg_eclipse_swt_internal_mozilla_nsID_2_3I",
#endif
#ifndef JNI64
	"_1VtblCall__II_3BI_3I",
#else
	"_1VtblCall__IJ_3BI_3I",
#endif
#ifndef JNI64
	"_1VtblCall__II_3BI_3I_3I",
#else
	"_1VtblCall__IJ_3BI_3I_3I",
#endif
#ifndef JNI64
	"_1VtblCall__II_3BI_3J_3I",
#else
	"_1VtblCall__IJ_3BI_3J_3I",
#endif
#ifndef JNI64
	"_1VtblCall__II_3BJ",
#else
	"_1VtblCall__IJ_3BJ",
#endif
#ifndef JNI64
	"_1VtblCall__II_3BJLorg_eclipse_swt_internal_mozilla_nsID_2_3J",
#else
	"_1VtblCall__IJ_3BJLorg_eclipse_swt_internal_mozilla_nsID_2_3J",
#endif
#ifndef JNI64
	"_1VtblCall__II_3BJ_3I",
#else
	"_1VtblCall__IJ_3BJ_3I",
#endif
#ifndef JNI64
	"_1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2I",
#else
	"_1VtblCall__IJ_3BLorg_eclipse_swt_internal_mozilla_nsID_2I",
#endif
#ifndef JNI64
	"_1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2J",
#else
	"_1VtblCall__IJ_3BLorg_eclipse_swt_internal_mozilla_nsID_2J",
#endif
#ifndef JNI64
	"_1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3I",
#else
	"_1VtblCall__IJ_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3I",
#endif
#ifndef JNI64
	"_1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3J",
#else
	"_1VtblCall__IJ_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3J",
#endif
#ifndef JNI64
	"_1VtblCall__II_3B_3B",
#else
	"_1VtblCall__IJ_3B_3B",
#endif
#ifndef JNI64
	"_1VtblCall__II_3B_3BI",
#else
	"_1VtblCall__IJ_3B_3BI",
#endif
#ifndef JNI64
	"_1VtblCall__II_3B_3B_3BII_3I",
#else
	"_1VtblCall__IJ_3B_3B_3BII_3I",
#endif
#ifndef JNI64
	"_1VtblCall__II_3B_3B_3BII_3J",
#else
	"_1VtblCall__IJ_3B_3B_3BII_3J",
#endif
#ifndef JNI64
	"_1VtblCall__II_3B_3B_3I",
#else
	"_1VtblCall__IJ_3B_3B_3I",
#endif
#ifndef JNI64
	"_1VtblCall__II_3B_3B_3J",
#else
	"_1VtblCall__IJ_3B_3B_3J",
#endif
#ifndef JNI64
	"_1VtblCall__II_3B_3I",
#else
	"_1VtblCall__IJ_3B_3I",
#endif
#ifndef JNI64
	"_1VtblCall__II_3B_3I_3I",
#else
	"_1VtblCall__IJ_3B_3I_3I",
#endif
#ifndef JNI64
	"_1VtblCall__II_3B_3I_3J",
#else
	"_1VtblCall__IJ_3B_3I_3J",
#endif
#ifndef JNI64
	"_1VtblCall__II_3B_3J",
#else
	"_1VtblCall__IJ_3B_3J",
#endif
#ifndef JNI64
	"_1VtblCall__II_3B_3J_3I",
#else
	"_1VtblCall__IJ_3B_3J_3I",
#endif
#ifndef JNI64
	"_1VtblCall__II_3C",
#else
	"_1VtblCall__IJ_3C",
#endif
#ifndef JNI64
	"_1VtblCall__II_3CIIII",
#else
	"_1VtblCall__IJ_3CIIII",
#endif
#ifndef JNI64
	"_1VtblCall__II_3CIJJJ",
#else
	"_1VtblCall__IJ_3CIJJJ",
#endif
#ifndef JNI64
	"_1VtblCall__II_3CI_3I",
#else
	"_1VtblCall__IJ_3CI_3I",
#endif
#ifndef JNI64
	"_1VtblCall__II_3CJ_3J",
#else
	"_1VtblCall__IJ_3CJ_3J",
#endif
#ifndef JNI64
	"_1VtblCall__II_3C_3C",
#else
	"_1VtblCall__IJ_3C_3C",
#endif
#ifndef JNI64
	"_1VtblCall__II_3F",
#else
	"_1VtblCall__IJ_3F",
#endif
#ifndef JNI64
	"_1VtblCall__II_3I",
#else
	"_1VtblCall__IJ_3I",
#endif
#ifndef JNI64
	"_1VtblCall__II_3I_3I",
#else
	"_1VtblCall__IJ_3I_3I",
#endif
#ifndef JNI64
	"_1VtblCall__II_3I_3I_3I",
#else
	"_1VtblCall__IJ_3I_3I_3I",
#endif
#ifndef JNI64
	"_1VtblCall__II_3I_3I_3I_3I",
#else
	"_1VtblCall__IJ_3I_3I_3I_3I",
#endif
#ifndef JNI64
	"_1VtblCall__II_3I_3I_3I_3I_3I_3I",
#else
	"_1VtblCall__IJ_3I_3I_3I_3I_3I_3I",
#endif
#ifndef JNI64
	"_1VtblCall__II_3I_3J",
#else
	"_1VtblCall__IJ_3I_3J",
#endif
#ifndef JNI64
	"_1VtblCall__II_3I_3J_3I",
#else
	"_1VtblCall__IJ_3I_3J_3I",
#endif
#ifndef JNI64
	"_1VtblCall__II_3I_3J_3J",
#else
	"_1VtblCall__IJ_3I_3J_3J",
#endif
#ifndef JNI64
	"_1VtblCall__II_3J",
#else
	"_1VtblCall__IJ_3J",
#endif
#ifndef JNI64
	"_1VtblCall__II_3J_3J",
#else
	"_1VtblCall__IJ_3J_3J",
#endif
#ifndef JNI64
	"_1VtblCall__II_3J_3J_3J",
#else
	"_1VtblCall__IJ_3J_3J_3J",
#endif
#ifndef JNI64
	"_1VtblCall__II_3J_3J_3J_3J_3J_3I",
#else
	"_1VtblCall__IJ_3J_3J_3J_3J_3J_3I",
#endif
#ifndef JNI64
	"_1VtblCall__II_3S",
#else
	"_1VtblCall__IJ_3S",
#endif
#ifndef JNI64
	"_1VtblCall__II_3SI_3I_3I",
#else
	"_1VtblCall__IJ_3SI_3I_3I",
#endif
#ifndef JNI64
	"_1VtblCall__II_3SJ_3I_3J",
#else
	"_1VtblCall__IJ_3SJ_3I_3J",
#endif
	"_1XPCOMGlueLoadXULFunctions",
	"_1XPCOMGlueShutdown",
	"_1XPCOMGlueStartup",
	"_1nsEmbedCString_1Length",
	"_1nsEmbedCString_1delete",
	"_1nsEmbedCString_1get",
	"_1nsEmbedCString_1new__",
#ifndef JNI64
	"_1nsEmbedCString_1new__II",
#else
	"_1nsEmbedCString_1new__JI",
#endif
	"_1nsEmbedCString_1new___3BI",
	"_1nsEmbedString_1Length",
	"_1nsEmbedString_1delete",
	"_1nsEmbedString_1get",
	"_1nsEmbedString_1new__",
	"_1nsEmbedString_1new___3C",
	"_1nsID_1Equals",
	"_1nsID_1delete",
	"_1nsID_1new",
	"_1nsIMemory_1Alloc",
	"_1nsIMemory_1Realloc",
	"_1nsIScriptContext_1GetNativeContext",
	"_1nsIScriptGlobalObject_1EnsureScriptEnvironment",
	"_1nsIScriptGlobalObject_1GetScriptContext",
	"_1nsIScriptGlobalObject_1GetScriptGlobal",
#ifndef JNI64
	"memmove__ILorg_eclipse_swt_internal_mozilla_nsDynamicFunctionLoad_2I",
#else
	"memmove__JLorg_eclipse_swt_internal_mozilla_nsDynamicFunctionLoad_2J",
#endif
#ifndef JNI64
	"memmove__ILorg_eclipse_swt_internal_mozilla_nsID_2I",
#else
	"memmove__JLorg_eclipse_swt_internal_mozilla_nsID_2I",
#endif
#ifndef JNI64
	"memmove__Lorg_eclipse_swt_internal_mozilla_nsID_2II",
#else
	"memmove__Lorg_eclipse_swt_internal_mozilla_nsID_2JI",
#endif
	"nsDynamicFunctionLoad_1sizeof",
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
