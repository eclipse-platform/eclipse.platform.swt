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

#include "swt.h"
#include "xpcom_stats.h"

#ifdef NATIVE_STATS

int XPCOM_nativeFunctionCount = 170;
int XPCOM_nativeFunctionCallCount[170];
char * XPCOM_nativeFunctionNames[] = {
	"Call",
	"NS_1GetComponentManager",
	"NS_1GetServiceManager",
	"NS_1InitXPCOM2",
	"NS_1NewLocalFile",
#ifndef JNI64
	"VtblCall__II",
#else
	"VtblCall__IJ",
#endif
#ifndef JNI64
	"VtblCall__IIF",
#else
	"VtblCall__IJF",
#endif
#ifndef JNI64
	"VtblCall__III",
#else
	"VtblCall__IJI",
#endif
#ifndef JNI64
	"VtblCall__IIII",
#else
	"VtblCall__IJII",
#endif
#ifndef JNI64
	"VtblCall__IIIII",
#else
	"VtblCall__IJIII",
#endif
#ifndef JNI64
	"VtblCall__IIIIII",
#else
	"VtblCall__IJIIII",
#endif
#ifndef JNI64
	"VtblCall__IIIIIII",
#else
	"VtblCall__IJIIIII",
#endif
#ifndef JNI64
	"VtblCall__IIIIIIII",
#else
	"VtblCall__IJIIIIII",
#endif
#ifndef JNI64
	"VtblCall__IIIIIIIIIIII",
#else
	"VtblCall__IJIIIIIIIIII",
#endif
#ifndef JNI64
	"VtblCall__IIIIIIIIIIIIIIISI",
#else
	"VtblCall__IJIIIIIIIIIIIIISI",
#endif
#ifndef JNI64
	"VtblCall__IIIIIIII_3C_3I_3I",
#else
	"VtblCall__IJIIIIII_3C_3I_3I",
#endif
#ifndef JNI64
	"VtblCall__IIIIIIJII",
#else
	"VtblCall__IJIIIIJII",
#endif
#ifndef JNI64
	"VtblCall__IIIIII_3CIIIII_3I_3I",
#else
	"VtblCall__IJIIII_3CIIIII_3I_3I",
#endif
#ifndef JNI64
	"VtblCall__IIIIII_3C_3BIIIII_3I_3I",
#else
	"VtblCall__IJIIII_3C_3BIIIII_3I_3I",
#endif
#ifndef JNI64
	"VtblCall__IIIIII_3C_3I_3I",
#else
	"VtblCall__IJIIII_3C_3I_3I",
#endif
#ifndef JNI64
	"VtblCall__IIIIII_3I_3I",
#else
	"VtblCall__IJIIII_3I_3I",
#endif
#ifndef JNI64
	"VtblCall__IIIII_3C",
#else
	"VtblCall__IJIII_3C",
#endif
#ifndef JNI64
	"VtblCall__IIIII_3I",
#else
	"VtblCall__IJIII_3I",
#endif
#ifndef JNI64
	"VtblCall__IIIIJJJJ",
#else
	"VtblCall__IJIIJJJJ",
#endif
#ifndef JNI64
	"VtblCall__IIII_3B",
#else
	"VtblCall__IJII_3B",
#endif
#ifndef JNI64
	"VtblCall__IIII_3C",
#else
	"VtblCall__IJII_3C",
#endif
#ifndef JNI64
	"VtblCall__IIII_3CIJI",
#else
	"VtblCall__IJII_3CIJI",
#endif
#ifndef JNI64
	"VtblCall__IIII_3CJJJ",
#else
	"VtblCall__IJII_3CJJJ",
#endif
#ifndef JNI64
	"VtblCall__IIII_3C_3CI_3I",
#else
	"VtblCall__IJII_3C_3CI_3I",
#endif
#ifndef JNI64
	"VtblCall__IIII_3I",
#else
	"VtblCall__IJII_3I",
#endif
#ifndef JNI64
	"VtblCall__IIII_3J",
#else
	"VtblCall__IJII_3J",
#endif
#ifndef JNI64
	"VtblCall__IIIJJ",
#else
	"VtblCall__IJIJJ",
#endif
#ifndef JNI64
	"VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2",
#else
	"VtblCall__IJILorg_eclipse_swt_internal_mozilla_nsID_2",
#endif
#ifndef JNI64
	"VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2_3I",
#else
	"VtblCall__IJILorg_eclipse_swt_internal_mozilla_nsID_2_3I",
#endif
#ifndef JNI64
	"VtblCall__III_3B",
#else
	"VtblCall__IJI_3B",
#endif
#ifndef JNI64
	"VtblCall__III_3BI",
#else
	"VtblCall__IJI_3BI",
#endif
#ifndef JNI64
	"VtblCall__III_3BI_3I",
#else
	"VtblCall__IJI_3BI_3I",
#endif
#ifndef JNI64
	"VtblCall__III_3B_3B_3BI_3I",
#else
	"VtblCall__IJI_3B_3B_3BI_3I",
#endif
#ifndef JNI64
	"VtblCall__III_3B_3C",
#else
	"VtblCall__IJI_3B_3C",
#endif
#ifndef JNI64
	"VtblCall__III_3B_3I",
#else
	"VtblCall__IJI_3B_3I",
#endif
#ifndef JNI64
	"VtblCall__III_3C",
#else
	"VtblCall__IJI_3C",
#endif
#ifndef JNI64
	"VtblCall__III_3CI",
#else
	"VtblCall__IJI_3CI",
#endif
#ifndef JNI64
	"VtblCall__III_3C_3C",
#else
	"VtblCall__IJI_3C_3C",
#endif
#ifndef JNI64
	"VtblCall__III_3C_3CI_3C_3C_3C_3C_3I_3I",
#else
	"VtblCall__IJI_3C_3CI_3C_3C_3C_3C_3I_3I",
#endif
#ifndef JNI64
	"VtblCall__III_3C_3CI_3I_3I_3I",
#else
	"VtblCall__IJI_3C_3CI_3I_3I_3I",
#endif
#ifndef JNI64
	"VtblCall__III_3C_3C_3C_3I",
#else
	"VtblCall__IJI_3C_3C_3C_3I",
#endif
#ifndef JNI64
	"VtblCall__III_3C_3C_3C_3I_3I",
#else
	"VtblCall__IJI_3C_3C_3C_3I_3I",
#endif
#ifndef JNI64
	"VtblCall__III_3C_3C_3I",
#else
	"VtblCall__IJI_3C_3C_3I",
#endif
#ifndef JNI64
	"VtblCall__III_3C_3C_3I_3C_3I_3I",
#else
	"VtblCall__IJI_3C_3C_3I_3C_3I_3I",
#endif
#ifndef JNI64
	"VtblCall__III_3C_3C_3I_3I_3C_3I_3I",
#else
	"VtblCall__IJI_3C_3C_3I_3I_3C_3I_3I",
#endif
#ifndef JNI64
	"VtblCall__III_3I",
#else
	"VtblCall__IJI_3I",
#endif
#ifndef JNI64
	"VtblCall__III_3I_3I_3I_3I",
#else
	"VtblCall__IJI_3I_3I_3I_3I",
#endif
#ifndef JNI64
	"VtblCall__IIJ",
#else
	"VtblCall__IJJ",
#endif
#ifndef JNI64
	"VtblCall__IIJI",
#else
	"VtblCall__IJJI",
#endif
#ifndef JNI64
	"VtblCall__IIJIIJIIIIII",
#else
	"VtblCall__IJJIIJIIIIII",
#endif
#ifndef JNI64
	"VtblCall__IIJIIJIIIIIIIIISJ",
#else
	"VtblCall__IJJIIJIIIIIIIIISJ",
#endif
#ifndef JNI64
	"VtblCall__IIJIIJ_3I_3J",
#else
	"VtblCall__IJJIIJ_3I_3J",
#endif
#ifndef JNI64
	"VtblCall__IIJI_3J",
#else
	"VtblCall__IJJI_3J",
#endif
#ifndef JNI64
	"VtblCall__IIJJ",
#else
	"VtblCall__IJJJ",
#endif
#ifndef JNI64
	"VtblCall__IIJJI",
#else
	"VtblCall__IJJJI",
#endif
#ifndef JNI64
	"VtblCall__IIJJII",
#else
	"VtblCall__IJJJII",
#endif
#ifndef JNI64
	"VtblCall__IIJJIIII",
#else
	"VtblCall__IJJJIIII",
#endif
#ifndef JNI64
	"VtblCall__IIJJIJ_3C_3I_3I",
#else
	"VtblCall__IJJJIJ_3C_3I_3I",
#endif
#ifndef JNI64
	"VtblCall__IIJJI_3C",
#else
	"VtblCall__IJJJI_3C",
#endif
#ifndef JNI64
	"VtblCall__IIJJI_3I",
#else
	"VtblCall__IJJJI_3I",
#endif
#ifndef JNI64
	"VtblCall__IIJJJ",
#else
	"VtblCall__IJJJJ",
#endif
#ifndef JNI64
	"VtblCall__IIJJJI",
#else
	"VtblCall__IJJJJI",
#endif
#ifndef JNI64
	"VtblCall__IIJJJI_3CJJIJI_3J_3J",
#else
	"VtblCall__IJJJJI_3CJJIJI_3J_3J",
#endif
#ifndef JNI64
	"VtblCall__IIJJJI_3C_3BJJIJI_3J_3J",
#else
	"VtblCall__IJJJJI_3C_3BJJIJI_3J_3J",
#endif
#ifndef JNI64
	"VtblCall__IIJJJJIJ_3C_3I_3J",
#else
	"VtblCall__IJJJJJIJ_3C_3I_3J",
#endif
#ifndef JNI64
	"VtblCall__IIJJJJJ",
#else
	"VtblCall__IJJJJJJ",
#endif
#ifndef JNI64
	"VtblCall__IIJJJJJJ",
#else
	"VtblCall__IJJJJJJJ",
#endif
#ifndef JNI64
	"VtblCall__IIJJJJJJJ",
#else
	"VtblCall__IJJJJJJJJ",
#endif
#ifndef JNI64
	"VtblCall__IIJJ_3B",
#else
	"VtblCall__IJJJ_3B",
#endif
#ifndef JNI64
	"VtblCall__IIJJ_3CIJI",
#else
	"VtblCall__IJJJ_3CIJI",
#endif
#ifndef JNI64
	"VtblCall__IIJJ_3CJJJ",
#else
	"VtblCall__IJJJ_3CJJJ",
#endif
#ifndef JNI64
	"VtblCall__IIJJ_3C_3CI_3J",
#else
	"VtblCall__IJJJ_3C_3CI_3J",
#endif
#ifndef JNI64
	"VtblCall__IIJJ_3I",
#else
	"VtblCall__IJJJ_3I",
#endif
#ifndef JNI64
	"VtblCall__IIJLorg_eclipse_swt_internal_mozilla_nsID_2",
#else
	"VtblCall__IJJLorg_eclipse_swt_internal_mozilla_nsID_2",
#endif
#ifndef JNI64
	"VtblCall__IIJLorg_eclipse_swt_internal_mozilla_nsID_2_3J",
#else
	"VtblCall__IJJLorg_eclipse_swt_internal_mozilla_nsID_2_3J",
#endif
#ifndef JNI64
	"VtblCall__IIJ_3B",
#else
	"VtblCall__IJJ_3B",
#endif
#ifndef JNI64
	"VtblCall__IIJ_3BI",
#else
	"VtblCall__IJJ_3BI",
#endif
#ifndef JNI64
	"VtblCall__IIJ_3BJ_3J",
#else
	"VtblCall__IJJ_3BJ_3J",
#endif
#ifndef JNI64
	"VtblCall__IIJ_3B_3B_3BJ_3J",
#else
	"VtblCall__IJJ_3B_3B_3BJ_3J",
#endif
#ifndef JNI64
	"VtblCall__IIJ_3B_3C",
#else
	"VtblCall__IJJ_3B_3C",
#endif
#ifndef JNI64
	"VtblCall__IIJ_3CI",
#else
	"VtblCall__IJJ_3CI",
#endif
#ifndef JNI64
	"VtblCall__IIJ_3C_3C",
#else
	"VtblCall__IJJ_3C_3C",
#endif
#ifndef JNI64
	"VtblCall__IIJ_3C_3CI_3C_3C_3C_3C_3I_3I",
#else
	"VtblCall__IJJ_3C_3CI_3C_3C_3C_3C_3I_3I",
#endif
#ifndef JNI64
	"VtblCall__IIJ_3C_3CI_3J_3I_3I",
#else
	"VtblCall__IJJ_3C_3CI_3J_3I_3I",
#endif
#ifndef JNI64
	"VtblCall__IIJ_3C_3C_3C_3I",
#else
	"VtblCall__IJJ_3C_3C_3C_3I",
#endif
#ifndef JNI64
	"VtblCall__IIJ_3C_3C_3C_3I_3I",
#else
	"VtblCall__IJJ_3C_3C_3C_3I_3I",
#endif
#ifndef JNI64
	"VtblCall__IIJ_3C_3C_3I",
#else
	"VtblCall__IJJ_3C_3C_3I",
#endif
#ifndef JNI64
	"VtblCall__IIJ_3C_3C_3J",
#else
	"VtblCall__IJJ_3C_3C_3J",
#endif
#ifndef JNI64
	"VtblCall__IIJ_3C_3C_3J_3C_3I_3I",
#else
	"VtblCall__IJJ_3C_3C_3J_3C_3I_3I",
#endif
#ifndef JNI64
	"VtblCall__IIJ_3C_3C_3J_3J_3C_3I_3I",
#else
	"VtblCall__IJJ_3C_3C_3J_3J_3C_3I_3I",
#endif
#ifndef JNI64
	"VtblCall__IIJ_3I",
#else
	"VtblCall__IJJ_3I",
#endif
#ifndef JNI64
	"VtblCall__IIJ_3J",
#else
	"VtblCall__IJJ_3J",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2I",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2I",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2ILorg_eclipse_swt_internal_mozilla_nsID_2_3I",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2ILorg_eclipse_swt_internal_mozilla_nsID_2_3I",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2J",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2J",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2JLorg_eclipse_swt_internal_mozilla_nsID_2_3J",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2JLorg_eclipse_swt_internal_mozilla_nsID_2_3J",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3I",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3I",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3J",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3J",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_3B_3B",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_3B_3B",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ_3B_3B",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ_3B_3B",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3I",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3I",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3J",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3J",
#endif
#ifndef JNI64
	"VtblCall__II_3B",
#else
	"VtblCall__IJ_3B",
#endif
#ifndef JNI64
	"VtblCall__II_3BI",
#else
	"VtblCall__IJ_3BI",
#endif
#ifndef JNI64
	"VtblCall__II_3BII_3I_3I",
#else
	"VtblCall__IJ_3BII_3I_3I",
#endif
#ifndef JNI64
	"VtblCall__II_3BIJ_3J_3I",
#else
	"VtblCall__IJ_3BIJ_3J_3I",
#endif
#ifndef JNI64
	"VtblCall__II_3BILorg_eclipse_swt_internal_mozilla_nsID_2_3I",
#else
	"VtblCall__IJ_3BILorg_eclipse_swt_internal_mozilla_nsID_2_3I",
#endif
#ifndef JNI64
	"VtblCall__II_3BI_3I",
#else
	"VtblCall__IJ_3BI_3I",
#endif
#ifndef JNI64
	"VtblCall__II_3BI_3I_3I",
#else
	"VtblCall__IJ_3BI_3I_3I",
#endif
#ifndef JNI64
	"VtblCall__II_3BI_3J_3I",
#else
	"VtblCall__IJ_3BI_3J_3I",
#endif
#ifndef JNI64
	"VtblCall__II_3BJ",
#else
	"VtblCall__IJ_3BJ",
#endif
#ifndef JNI64
	"VtblCall__II_3BJLorg_eclipse_swt_internal_mozilla_nsID_2_3J",
#else
	"VtblCall__IJ_3BJLorg_eclipse_swt_internal_mozilla_nsID_2_3J",
#endif
#ifndef JNI64
	"VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2I",
#else
	"VtblCall__IJ_3BLorg_eclipse_swt_internal_mozilla_nsID_2I",
#endif
#ifndef JNI64
	"VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2J",
#else
	"VtblCall__IJ_3BLorg_eclipse_swt_internal_mozilla_nsID_2J",
#endif
#ifndef JNI64
	"VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3I",
#else
	"VtblCall__IJ_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3I",
#endif
#ifndef JNI64
	"VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3J",
#else
	"VtblCall__IJ_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3J",
#endif
#ifndef JNI64
	"VtblCall__II_3B_3B",
#else
	"VtblCall__IJ_3B_3B",
#endif
#ifndef JNI64
	"VtblCall__II_3B_3BI",
#else
	"VtblCall__IJ_3B_3BI",
#endif
#ifndef JNI64
	"VtblCall__II_3B_3B_3BII_3I",
#else
	"VtblCall__IJ_3B_3B_3BII_3I",
#endif
#ifndef JNI64
	"VtblCall__II_3B_3B_3BII_3J",
#else
	"VtblCall__IJ_3B_3B_3BII_3J",
#endif
#ifndef JNI64
	"VtblCall__II_3B_3B_3I",
#else
	"VtblCall__IJ_3B_3B_3I",
#endif
#ifndef JNI64
	"VtblCall__II_3B_3B_3J",
#else
	"VtblCall__IJ_3B_3B_3J",
#endif
#ifndef JNI64
	"VtblCall__II_3B_3I",
#else
	"VtblCall__IJ_3B_3I",
#endif
#ifndef JNI64
	"VtblCall__II_3B_3I_3I",
#else
	"VtblCall__IJ_3B_3I_3I",
#endif
#ifndef JNI64
	"VtblCall__II_3B_3I_3J",
#else
	"VtblCall__IJ_3B_3I_3J",
#endif
#ifndef JNI64
	"VtblCall__II_3B_3J",
#else
	"VtblCall__IJ_3B_3J",
#endif
#ifndef JNI64
	"VtblCall__II_3B_3J_3I",
#else
	"VtblCall__IJ_3B_3J_3I",
#endif
#ifndef JNI64
	"VtblCall__II_3C",
#else
	"VtblCall__IJ_3C",
#endif
#ifndef JNI64
	"VtblCall__II_3CIIII",
#else
	"VtblCall__IJ_3CIIII",
#endif
#ifndef JNI64
	"VtblCall__II_3CIJJJ",
#else
	"VtblCall__IJ_3CIJJJ",
#endif
#ifndef JNI64
	"VtblCall__II_3CI_3I",
#else
	"VtblCall__IJ_3CI_3I",
#endif
#ifndef JNI64
	"VtblCall__II_3CJ_3J",
#else
	"VtblCall__IJ_3CJ_3J",
#endif
#ifndef JNI64
	"VtblCall__II_3C_3C",
#else
	"VtblCall__IJ_3C_3C",
#endif
#ifndef JNI64
	"VtblCall__II_3F",
#else
	"VtblCall__IJ_3F",
#endif
#ifndef JNI64
	"VtblCall__II_3I",
#else
	"VtblCall__IJ_3I",
#endif
#ifndef JNI64
	"VtblCall__II_3I_3I",
#else
	"VtblCall__IJ_3I_3I",
#endif
#ifndef JNI64
	"VtblCall__II_3I_3I_3I",
#else
	"VtblCall__IJ_3I_3I_3I",
#endif
#ifndef JNI64
	"VtblCall__II_3I_3I_3I_3I",
#else
	"VtblCall__IJ_3I_3I_3I_3I",
#endif
#ifndef JNI64
	"VtblCall__II_3I_3J",
#else
	"VtblCall__IJ_3I_3J",
#endif
#ifndef JNI64
	"VtblCall__II_3I_3J_3I",
#else
	"VtblCall__IJ_3I_3J_3I",
#endif
#ifndef JNI64
	"VtblCall__II_3I_3J_3J",
#else
	"VtblCall__IJ_3I_3J_3J",
#endif
#ifndef JNI64
	"VtblCall__II_3J",
#else
	"VtblCall__IJ_3J",
#endif
#ifndef JNI64
	"VtblCall__II_3J_3J_3J",
#else
	"VtblCall__IJ_3J_3J_3J",
#endif
#ifndef JNI64
	"VtblCall__II_3S",
#else
	"VtblCall__IJ_3S",
#endif
	"XPCOMGlueShutdown",
	"XPCOMGlueStartup",
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
	"nsEmbedCString_1Length",
	"nsEmbedCString_1delete",
	"nsEmbedCString_1get",
	"nsEmbedCString_1new__",
#ifndef JNI64
	"nsEmbedCString_1new__II",
#else
	"nsEmbedCString_1new__JI",
#endif
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
