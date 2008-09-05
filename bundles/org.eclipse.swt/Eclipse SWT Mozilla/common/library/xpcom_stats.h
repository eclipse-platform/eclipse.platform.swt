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

#ifdef NATIVE_STATS
extern int XPCOM_nativeFunctionCount;
extern int XPCOM_nativeFunctionCallCount[];
extern char* XPCOM_nativeFunctionNames[];
#define XPCOM_NATIVE_ENTER(env, that, func) XPCOM_nativeFunctionCallCount[func]++;
#define XPCOM_NATIVE_EXIT(env, that, func) 
#else
#ifndef XPCOM_NATIVE_ENTER
#define XPCOM_NATIVE_ENTER(env, that, func) 
#endif
#ifndef XPCOM_NATIVE_EXIT
#define XPCOM_NATIVE_EXIT(env, that, func) 
#endif
#endif

typedef enum {
	Call_FUNC,
	NS_1GetComponentManager_FUNC,
	NS_1GetServiceManager_FUNC,
	NS_1InitXPCOM2_FUNC,
	NS_1NewLocalFile_FUNC,
#ifndef JNI64
	VtblCall__II_FUNC,
#else
	VtblCall__IJ_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIF_FUNC,
#else
	VtblCall__IJF_FUNC,
#endif
#ifndef JNI64
	VtblCall__III_FUNC,
#else
	VtblCall__IJI_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIII_FUNC,
#else
	VtblCall__IJII_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIIII_FUNC,
#else
	VtblCall__IJIII_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIIIII_FUNC,
#else
	VtblCall__IJIIII_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIIIIII_FUNC,
#else
	VtblCall__IJIIIII_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIIIIIII_FUNC,
#else
	VtblCall__IJIIIIII_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIIIIIIIIIII_FUNC,
#else
	VtblCall__IJIIIIIIIIII_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIIIIIIIIIIIIIISI_FUNC,
#else
	VtblCall__IJIIIIIIIIIIIIISI_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIIIIIII_3C_3I_3I_FUNC,
#else
	VtblCall__IJIIIIII_3C_3I_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIIIIIJII_FUNC,
#else
	VtblCall__IJIIIIJII_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIIIII_3CIIIII_3I_3I_FUNC,
#else
	VtblCall__IJIIII_3CIIIII_3I_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIIIII_3C_3BIIIII_3I_3I_FUNC,
#else
	VtblCall__IJIIII_3C_3BIIIII_3I_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIIIII_3C_3I_3I_FUNC,
#else
	VtblCall__IJIIII_3C_3I_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIIIII_3I_3I_FUNC,
#else
	VtblCall__IJIIII_3I_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIIII_3C_FUNC,
#else
	VtblCall__IJIII_3C_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIIII_3I_FUNC,
#else
	VtblCall__IJIII_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIIIJJJJ_FUNC,
#else
	VtblCall__IJIIJJJJ_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIII_3B_FUNC,
#else
	VtblCall__IJII_3B_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIII_3C_FUNC,
#else
	VtblCall__IJII_3C_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIII_3CIJI_FUNC,
#else
	VtblCall__IJII_3CIJI_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIII_3CJJJ_FUNC,
#else
	VtblCall__IJII_3CJJJ_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIII_3C_3CI_3I_FUNC,
#else
	VtblCall__IJII_3C_3CI_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIII_3I_FUNC,
#else
	VtblCall__IJII_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIII_3J_FUNC,
#else
	VtblCall__IJII_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIIJJ_FUNC,
#else
	VtblCall__IJIJJ_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2_FUNC,
#else
	VtblCall__IJILorg_eclipse_swt_internal_mozilla_nsID_2_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC,
#else
	VtblCall__IJILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__III_3B_FUNC,
#else
	VtblCall__IJI_3B_FUNC,
#endif
#ifndef JNI64
	VtblCall__III_3BI_FUNC,
#else
	VtblCall__IJI_3BI_FUNC,
#endif
#ifndef JNI64
	VtblCall__III_3BI_3I_FUNC,
#else
	VtblCall__IJI_3BI_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__III_3B_3B_3BI_3I_FUNC,
#else
	VtblCall__IJI_3B_3B_3BI_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__III_3B_3C_FUNC,
#else
	VtblCall__IJI_3B_3C_FUNC,
#endif
#ifndef JNI64
	VtblCall__III_3B_3I_FUNC,
#else
	VtblCall__IJI_3B_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__III_3C_FUNC,
#else
	VtblCall__IJI_3C_FUNC,
#endif
#ifndef JNI64
	VtblCall__III_3CI_FUNC,
#else
	VtblCall__IJI_3CI_FUNC,
#endif
#ifndef JNI64
	VtblCall__III_3C_3C_FUNC,
#else
	VtblCall__IJI_3C_3C_FUNC,
#endif
#ifndef JNI64
	VtblCall__III_3C_3CI_3C_3C_3C_3C_3I_3I_FUNC,
#else
	VtblCall__IJI_3C_3CI_3C_3C_3C_3C_3I_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__III_3C_3CI_3I_3I_3I_FUNC,
#else
	VtblCall__IJI_3C_3CI_3I_3I_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__III_3C_3C_3C_3I_FUNC,
#else
	VtblCall__IJI_3C_3C_3C_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__III_3C_3C_3C_3I_3I_FUNC,
#else
	VtblCall__IJI_3C_3C_3C_3I_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__III_3C_3C_3I_FUNC,
#else
	VtblCall__IJI_3C_3C_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__III_3C_3C_3I_3C_3I_3I_FUNC,
#else
	VtblCall__IJI_3C_3C_3I_3C_3I_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__III_3C_3C_3I_3I_3C_3I_3I_FUNC,
#else
	VtblCall__IJI_3C_3C_3I_3I_3C_3I_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__III_3I_FUNC,
#else
	VtblCall__IJI_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__III_3I_3I_3I_3I_FUNC,
#else
	VtblCall__IJI_3I_3I_3I_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJ_FUNC,
#else
	VtblCall__IJJ_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJI_FUNC,
#else
	VtblCall__IJJI_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJIIJIIIIII_FUNC,
#else
	VtblCall__IJJIIJIIIIII_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJIIJIIIIIIIIISJ_FUNC,
#else
	VtblCall__IJJIIJIIIIIIIIISJ_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJIIJ_3I_3J_FUNC,
#else
	VtblCall__IJJIIJ_3I_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJI_3J_FUNC,
#else
	VtblCall__IJJI_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJJ_FUNC,
#else
	VtblCall__IJJJ_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJJI_FUNC,
#else
	VtblCall__IJJJI_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJJII_FUNC,
#else
	VtblCall__IJJJII_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJJIIII_FUNC,
#else
	VtblCall__IJJJIIII_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJJIJ_3C_3I_3I_FUNC,
#else
	VtblCall__IJJJIJ_3C_3I_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJJI_3C_FUNC,
#else
	VtblCall__IJJJI_3C_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJJI_3I_FUNC,
#else
	VtblCall__IJJJI_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJJJ_FUNC,
#else
	VtblCall__IJJJJ_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJJJI_FUNC,
#else
	VtblCall__IJJJJI_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJJJI_3CJJIJI_3J_3J_FUNC,
#else
	VtblCall__IJJJJI_3CJJIJI_3J_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJJJI_3C_3BJJIJI_3J_3J_FUNC,
#else
	VtblCall__IJJJJI_3C_3BJJIJI_3J_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJJJJIJ_3C_3I_3J_FUNC,
#else
	VtblCall__IJJJJJIJ_3C_3I_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJJJJJ_FUNC,
#else
	VtblCall__IJJJJJJ_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJJJJJJ_FUNC,
#else
	VtblCall__IJJJJJJJ_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJJJJJJJ_FUNC,
#else
	VtblCall__IJJJJJJJJ_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJJ_3B_FUNC,
#else
	VtblCall__IJJJ_3B_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJJ_3CIJI_FUNC,
#else
	VtblCall__IJJJ_3CIJI_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJJ_3CJJJ_FUNC,
#else
	VtblCall__IJJJ_3CJJJ_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJJ_3C_3CI_3J_FUNC,
#else
	VtblCall__IJJJ_3C_3CI_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJJ_3I_FUNC,
#else
	VtblCall__IJJJ_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJLorg_eclipse_swt_internal_mozilla_nsID_2_FUNC,
#else
	VtblCall__IJJLorg_eclipse_swt_internal_mozilla_nsID_2_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC,
#else
	VtblCall__IJJLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJ_3B_FUNC,
#else
	VtblCall__IJJ_3B_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJ_3BI_FUNC,
#else
	VtblCall__IJJ_3BI_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJ_3BJ_3J_FUNC,
#else
	VtblCall__IJJ_3BJ_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJ_3B_3B_3BJ_3J_FUNC,
#else
	VtblCall__IJJ_3B_3B_3BJ_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJ_3B_3C_FUNC,
#else
	VtblCall__IJJ_3B_3C_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJ_3CI_FUNC,
#else
	VtblCall__IJJ_3CI_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJ_3C_3C_FUNC,
#else
	VtblCall__IJJ_3C_3C_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJ_3C_3CI_3C_3C_3C_3C_3I_3I_FUNC,
#else
	VtblCall__IJJ_3C_3CI_3C_3C_3C_3C_3I_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJ_3C_3CI_3J_3I_3I_FUNC,
#else
	VtblCall__IJJ_3C_3CI_3J_3I_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJ_3C_3C_3C_3I_FUNC,
#else
	VtblCall__IJJ_3C_3C_3C_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJ_3C_3C_3C_3I_3I_FUNC,
#else
	VtblCall__IJJ_3C_3C_3C_3I_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJ_3C_3C_3I_FUNC,
#else
	VtblCall__IJJ_3C_3C_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJ_3C_3C_3J_FUNC,
#else
	VtblCall__IJJ_3C_3C_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJ_3C_3C_3J_3C_3I_3I_FUNC,
#else
	VtblCall__IJJ_3C_3C_3J_3C_3I_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJ_3C_3C_3J_3J_3C_3I_3I_FUNC,
#else
	VtblCall__IJJ_3C_3C_3J_3J_3C_3I_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJ_3I_FUNC,
#else
	VtblCall__IJJ_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJ_3J_FUNC,
#else
	VtblCall__IJJ_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2I_FUNC,
#else
	VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2ILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC,
#else
	VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2ILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2J_FUNC,
#else
	VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2J_FUNC,
#endif
#ifndef JNI64
	VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2JLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC,
#else
	VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2JLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC,
#else
	VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC,
#else
	VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_FUNC,
#else
	VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_FUNC,
#endif
#ifndef JNI64
	VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_3B_3B_FUNC,
#else
	VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_3B_3B_FUNC,
#endif
#ifndef JNI64
	VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ_FUNC,
#else
	VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ_FUNC,
#endif
#ifndef JNI64
	VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ_3B_3B_FUNC,
#else
	VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ_3B_3B_FUNC,
#endif
#ifndef JNI64
	VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC,
#else
	VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC,
#else
	VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3B_FUNC,
#else
	VtblCall__IJ_3B_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3BI_FUNC,
#else
	VtblCall__IJ_3BI_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3BII_3I_3I_FUNC,
#else
	VtblCall__IJ_3BII_3I_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3BIJ_3J_3I_FUNC,
#else
	VtblCall__IJ_3BIJ_3J_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3BILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC,
#else
	VtblCall__IJ_3BILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3BI_3I_FUNC,
#else
	VtblCall__IJ_3BI_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3BI_3I_3I_FUNC,
#else
	VtblCall__IJ_3BI_3I_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3BI_3J_3I_FUNC,
#else
	VtblCall__IJ_3BI_3J_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3BJ_FUNC,
#else
	VtblCall__IJ_3BJ_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3BJLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC,
#else
	VtblCall__IJ_3BJLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2I_FUNC,
#else
	VtblCall__IJ_3BLorg_eclipse_swt_internal_mozilla_nsID_2I_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2J_FUNC,
#else
	VtblCall__IJ_3BLorg_eclipse_swt_internal_mozilla_nsID_2J_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC,
#else
	VtblCall__IJ_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC,
#else
	VtblCall__IJ_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3B_3B_FUNC,
#else
	VtblCall__IJ_3B_3B_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3B_3BI_FUNC,
#else
	VtblCall__IJ_3B_3BI_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3B_3B_3BII_3I_FUNC,
#else
	VtblCall__IJ_3B_3B_3BII_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3B_3B_3BII_3J_FUNC,
#else
	VtblCall__IJ_3B_3B_3BII_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3B_3B_3I_FUNC,
#else
	VtblCall__IJ_3B_3B_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3B_3B_3J_FUNC,
#else
	VtblCall__IJ_3B_3B_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3B_3I_FUNC,
#else
	VtblCall__IJ_3B_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3B_3I_3I_FUNC,
#else
	VtblCall__IJ_3B_3I_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3B_3I_3J_FUNC,
#else
	VtblCall__IJ_3B_3I_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3B_3J_FUNC,
#else
	VtblCall__IJ_3B_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3B_3J_3I_FUNC,
#else
	VtblCall__IJ_3B_3J_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3C_FUNC,
#else
	VtblCall__IJ_3C_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3CIIII_FUNC,
#else
	VtblCall__IJ_3CIIII_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3CIJJJ_FUNC,
#else
	VtblCall__IJ_3CIJJJ_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3CI_3I_FUNC,
#else
	VtblCall__IJ_3CI_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3CJ_3J_FUNC,
#else
	VtblCall__IJ_3CJ_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3C_3C_FUNC,
#else
	VtblCall__IJ_3C_3C_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3F_FUNC,
#else
	VtblCall__IJ_3F_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3I_FUNC,
#else
	VtblCall__IJ_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3I_3I_FUNC,
#else
	VtblCall__IJ_3I_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3I_3I_3I_FUNC,
#else
	VtblCall__IJ_3I_3I_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3I_3I_3I_3I_FUNC,
#else
	VtblCall__IJ_3I_3I_3I_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3I_3J_FUNC,
#else
	VtblCall__IJ_3I_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3I_3J_3I_FUNC,
#else
	VtblCall__IJ_3I_3J_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3I_3J_3J_FUNC,
#else
	VtblCall__IJ_3I_3J_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3J_FUNC,
#else
	VtblCall__IJ_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3J_3J_3J_FUNC,
#else
	VtblCall__IJ_3J_3J_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3S_FUNC,
#else
	VtblCall__IJ_3S_FUNC,
#endif
	XPCOMGlueShutdown_FUNC,
	XPCOMGlueStartup_FUNC,
#ifndef JNI64
	memmove__ILorg_eclipse_swt_internal_mozilla_nsID_2I_FUNC,
#else
	memmove__JLorg_eclipse_swt_internal_mozilla_nsID_2I_FUNC,
#endif
#ifndef JNI64
	memmove__Lorg_eclipse_swt_internal_mozilla_nsID_2II_FUNC,
#else
	memmove__Lorg_eclipse_swt_internal_mozilla_nsID_2JI_FUNC,
#endif
	nsEmbedCString_1Length_FUNC,
	nsEmbedCString_1delete_FUNC,
	nsEmbedCString_1get_FUNC,
	nsEmbedCString_1new___FUNC,
	nsEmbedCString_1new__II_FUNC,
	nsEmbedCString_1new___3BI_FUNC,
	nsEmbedString_1Length_FUNC,
	nsEmbedString_1delete_FUNC,
	nsEmbedString_1get_FUNC,
	nsEmbedString_1new___FUNC,
	nsEmbedString_1new___3C_FUNC,
	nsID_1Equals_FUNC,
	nsID_1delete_FUNC,
	nsID_1new_FUNC,
	strlen_1PRUnichar_FUNC,
} XPCOM_FUNCS;
