/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
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
#ifndef JNI64
	_1Call__I_FUNC,
#else
	_1Call__J_FUNC,
#endif
#ifndef JNI64
	_1Call__IIIIII_FUNC,
#else
	_1Call__JJJJJI_FUNC,
#endif
#ifndef JNI64
	_1Call__III_3BII_3I_FUNC,
#else
	_1Call__JJJ_3BII_3I_FUNC,
#endif
	_1JS_1EvaluateUCScriptForPrincipals_FUNC,
	_1NS_1Free_FUNC,
	_1NS_1GetComponentManager_FUNC,
	_1NS_1GetServiceManager_FUNC,
	_1NS_1InitXPCOM2_FUNC,
	_1NS_1NewLocalFile_FUNC,
#ifndef JNI64
	_1VtblCall__II_FUNC,
#else
	_1VtblCall__IJ_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IID_FUNC,
#else
	_1VtblCall__IJD_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIF_FUNC,
#else
	_1VtblCall__IJF_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__III_FUNC,
#else
	_1VtblCall__IJI_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIII_FUNC,
#else
	_1VtblCall__IJII_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIIII_FUNC,
#else
	_1VtblCall__IJIII_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIIIII_FUNC,
#else
	_1VtblCall__IJIIII_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIIIIII_FUNC,
#else
	_1VtblCall__IJIIIII_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIIIIIII_FUNC,
#else
	_1VtblCall__IJIIIIII_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIIIIIIIIIII_FUNC,
#else
	_1VtblCall__IJIIIIIIIIII_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIIIIIIIIIIIIIISI_FUNC,
#else
	_1VtblCall__IJIIIIIIIIIIIIISI_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIIIIIIII_3I_FUNC,
#else
	_1VtblCall__IJIIIIIII_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIIIIIII_3C_3I_3I_FUNC,
#else
	_1VtblCall__IJIIIIII_3C_3I_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIIIIII_3I_FUNC,
#else
	_1VtblCall__IJIIIII_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIIIIIJII_FUNC,
#else
	_1VtblCall__IJIIIIJII_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIIIII_3CIIIII_3I_3I_FUNC,
#else
	_1VtblCall__IJIIII_3CIIIII_3I_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIIIII_3C_3BIIIII_3I_3I_FUNC,
#else
	_1VtblCall__IJIIII_3C_3BIIIII_3I_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIIIII_3C_3I_3I_FUNC,
#else
	_1VtblCall__IJIIII_3C_3I_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIIIII_3I_3I_FUNC,
#else
	_1VtblCall__IJIIII_3I_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIIIII_3I_3I_3I_FUNC,
#else
	_1VtblCall__IJIIII_3I_3I_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIIII_3B_3BI_FUNC,
#else
	_1VtblCall__IJIII_3B_3BI_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIIII_3C_FUNC,
#else
	_1VtblCall__IJIII_3C_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIIII_3I_FUNC,
#else
	_1VtblCall__IJIII_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIIII_3I_3I_3I_FUNC,
#else
	_1VtblCall__IJIII_3I_3I_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIIIJJJJ_FUNC,
#else
	_1VtblCall__IJIIJJJJ_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIII_3B_FUNC,
#else
	_1VtblCall__IJII_3B_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIII_3BI_FUNC,
#else
	_1VtblCall__IJII_3BI_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIII_3BII_FUNC,
#else
	_1VtblCall__IJII_3BII_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIII_3B_3B_FUNC,
#else
	_1VtblCall__IJII_3B_3B_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIII_3C_FUNC,
#else
	_1VtblCall__IJII_3C_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIII_3CIJI_FUNC,
#else
	_1VtblCall__IJII_3CIJI_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIII_3CJJJ_FUNC,
#else
	_1VtblCall__IJII_3CJJJ_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIII_3C_3CI_3I_FUNC,
#else
	_1VtblCall__IJII_3C_3CI_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIII_3I_FUNC,
#else
	_1VtblCall__IJII_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIII_3I_3I_FUNC,
#else
	_1VtblCall__IJII_3I_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIII_3J_FUNC,
#else
	_1VtblCall__IJII_3J_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIIJJ_FUNC,
#else
	_1VtblCall__IJIJJ_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIIJJJJJJ_3J_FUNC,
#else
	_1VtblCall__IJIJJJJJJ_3J_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2_FUNC,
#else
	_1VtblCall__IJILorg_eclipse_swt_internal_mozilla_nsID_2_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2II_3I_FUNC,
#else
	_1VtblCall__IJILorg_eclipse_swt_internal_mozilla_nsID_2II_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC,
#else
	_1VtblCall__IJILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__III_3B_FUNC,
#else
	_1VtblCall__IJI_3B_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__III_3BI_FUNC,
#else
	_1VtblCall__IJI_3BI_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__III_3BI_3I_FUNC,
#else
	_1VtblCall__IJI_3BI_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__III_3BS_FUNC,
#else
	_1VtblCall__IJI_3BS_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__III_3B_3B_3BI_3I_FUNC,
#else
	_1VtblCall__IJI_3B_3B_3BI_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__III_3B_3C_FUNC,
#else
	_1VtblCall__IJI_3B_3C_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__III_3B_3I_FUNC,
#else
	_1VtblCall__IJI_3B_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__III_3C_FUNC,
#else
	_1VtblCall__IJI_3C_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__III_3CI_FUNC,
#else
	_1VtblCall__IJI_3CI_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__III_3C_3C_FUNC,
#else
	_1VtblCall__IJI_3C_3C_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__III_3C_3CI_3C_3C_3C_3C_3I_3I_FUNC,
#else
	_1VtblCall__IJI_3C_3CI_3C_3C_3C_3C_3I_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__III_3C_3CI_3I_3I_3I_FUNC,
#else
	_1VtblCall__IJI_3C_3CI_3I_3I_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__III_3C_3C_3C_3I_FUNC,
#else
	_1VtblCall__IJI_3C_3C_3C_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__III_3C_3C_3C_3I_3I_FUNC,
#else
	_1VtblCall__IJI_3C_3C_3C_3I_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__III_3C_3C_3I_FUNC,
#else
	_1VtblCall__IJI_3C_3C_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__III_3C_3C_3I_3C_3I_3I_FUNC,
#else
	_1VtblCall__IJI_3C_3C_3I_3C_3I_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__III_3C_3C_3I_3I_3C_3I_3I_FUNC,
#else
	_1VtblCall__IJI_3C_3C_3I_3I_3C_3I_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__III_3C_3I_FUNC,
#else
	_1VtblCall__IJI_3C_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__III_3I_FUNC,
#else
	_1VtblCall__IJI_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__III_3II_FUNC,
#else
	_1VtblCall__IJI_3II_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__III_3IJ_FUNC,
#else
	_1VtblCall__IJI_3IJ_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__III_3I_3I_3I_FUNC,
#else
	_1VtblCall__IJI_3I_3I_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__III_3I_3I_3I_3I_FUNC,
#else
	_1VtblCall__IJI_3I_3I_3I_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__III_3I_3I_3J_FUNC,
#else
	_1VtblCall__IJI_3I_3I_3J_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJ_FUNC,
#else
	_1VtblCall__IJJ_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJI_FUNC,
#else
	_1VtblCall__IJJI_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJIIJIIIIII_FUNC,
#else
	_1VtblCall__IJJIIJIIIIII_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJIIJIIIIIIIIISJ_FUNC,
#else
	_1VtblCall__IJJIIJIIIIIIIIISJ_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJIIJ_3I_3J_FUNC,
#else
	_1VtblCall__IJJIIJ_3I_3J_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJII_3I_FUNC,
#else
	_1VtblCall__IJJII_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJIJII_FUNC,
#else
	_1VtblCall__IJJIJII_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJIJJ_3I_3I_3I_FUNC,
#else
	_1VtblCall__IJJIJJ_3I_3I_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJIJ_3I_3I_3I_FUNC,
#else
	_1VtblCall__IJJIJ_3I_3I_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJI_3J_FUNC,
#else
	_1VtblCall__IJJI_3J_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJJ_FUNC,
#else
	_1VtblCall__IJJJ_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJJI_FUNC,
#else
	_1VtblCall__IJJJI_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJJII_FUNC,
#else
	_1VtblCall__IJJJII_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJJIIII_FUNC,
#else
	_1VtblCall__IJJJIIII_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJJIJ_3C_3I_3I_FUNC,
#else
	_1VtblCall__IJJJIJ_3C_3I_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJJI_3C_FUNC,
#else
	_1VtblCall__IJJJI_3C_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJJI_3I_FUNC,
#else
	_1VtblCall__IJJJI_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJJJ_FUNC,
#else
	_1VtblCall__IJJJJ_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJJJI_FUNC,
#else
	_1VtblCall__IJJJJI_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJJJI_3CJJIJI_3J_3J_FUNC,
#else
	_1VtblCall__IJJJJI_3CJJIJI_3J_3J_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJJJI_3C_3BJJIJI_3J_3J_FUNC,
#else
	_1VtblCall__IJJJJI_3C_3BJJIJI_3J_3J_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJJJJIJ_3C_3I_3J_FUNC,
#else
	_1VtblCall__IJJJJJIJ_3C_3I_3J_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJJJJJ_FUNC,
#else
	_1VtblCall__IJJJJJJ_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJJJJJJ_FUNC,
#else
	_1VtblCall__IJJJJJJJ_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJJJJJJJ_FUNC,
#else
	_1VtblCall__IJJJJJJJJ_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJJJJJ_3J_FUNC,
#else
	_1VtblCall__IJJJJJJ_3J_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJJJ_3B_3BJ_FUNC,
#else
	_1VtblCall__IJJJJ_3B_3BJ_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJJJ_3I_FUNC,
#else
	_1VtblCall__IJJJJ_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJJJ_3J_FUNC,
#else
	_1VtblCall__IJJJJ_3J_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJJ_3B_FUNC,
#else
	_1VtblCall__IJJJ_3B_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJJ_3BJ_FUNC,
#else
	_1VtblCall__IJJJ_3BJ_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJJ_3BJI_FUNC,
#else
	_1VtblCall__IJJJ_3BJI_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJJ_3B_3B_FUNC,
#else
	_1VtblCall__IJJJ_3B_3B_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJJ_3CIJI_FUNC,
#else
	_1VtblCall__IJJJ_3CIJI_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJJ_3CJJJ_FUNC,
#else
	_1VtblCall__IJJJ_3CJJJ_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJJ_3C_3CI_3J_FUNC,
#else
	_1VtblCall__IJJJ_3C_3CI_3J_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJJ_3I_FUNC,
#else
	_1VtblCall__IJJJ_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJJ_3I_3I_FUNC,
#else
	_1VtblCall__IJJJ_3I_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJJ_3J_FUNC,
#else
	_1VtblCall__IJJJ_3J_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJLorg_eclipse_swt_internal_mozilla_nsID_2_FUNC,
#else
	_1VtblCall__IJJLorg_eclipse_swt_internal_mozilla_nsID_2_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJLorg_eclipse_swt_internal_mozilla_nsID_2JJ_3J_FUNC,
#else
	_1VtblCall__IJJLorg_eclipse_swt_internal_mozilla_nsID_2JJ_3J_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC,
#else
	_1VtblCall__IJJLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJ_3B_FUNC,
#else
	_1VtblCall__IJJ_3B_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJ_3BI_FUNC,
#else
	_1VtblCall__IJJ_3BI_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJ_3BJ_FUNC,
#else
	_1VtblCall__IJJ_3BJ_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJ_3BJ_3J_FUNC,
#else
	_1VtblCall__IJJ_3BJ_3J_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJ_3BS_FUNC,
#else
	_1VtblCall__IJJ_3BS_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJ_3B_3B_3BJ_3J_FUNC,
#else
	_1VtblCall__IJJ_3B_3B_3BJ_3J_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJ_3B_3C_FUNC,
#else
	_1VtblCall__IJJ_3B_3C_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJ_3CI_FUNC,
#else
	_1VtblCall__IJJ_3CI_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJ_3C_3C_FUNC,
#else
	_1VtblCall__IJJ_3C_3C_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJ_3C_3CI_3C_3C_3C_3C_3I_3I_FUNC,
#else
	_1VtblCall__IJJ_3C_3CI_3C_3C_3C_3C_3I_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJ_3C_3CI_3J_3I_3I_FUNC,
#else
	_1VtblCall__IJJ_3C_3CI_3J_3I_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJ_3C_3C_3C_3I_FUNC,
#else
	_1VtblCall__IJJ_3C_3C_3C_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJ_3C_3C_3C_3I_3I_FUNC,
#else
	_1VtblCall__IJJ_3C_3C_3C_3I_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJ_3C_3C_3I_FUNC,
#else
	_1VtblCall__IJJ_3C_3C_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJ_3C_3C_3J_FUNC,
#else
	_1VtblCall__IJJ_3C_3C_3J_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJ_3C_3C_3J_3C_3I_3I_FUNC,
#else
	_1VtblCall__IJJ_3C_3C_3J_3C_3I_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJ_3C_3C_3J_3J_3C_3I_3I_FUNC,
#else
	_1VtblCall__IJJ_3C_3C_3J_3J_3C_3I_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJ_3C_3J_FUNC,
#else
	_1VtblCall__IJJ_3C_3J_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJ_3I_FUNC,
#else
	_1VtblCall__IJJ_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IIJ_3J_FUNC,
#else
	_1VtblCall__IJJ_3J_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2I_FUNC,
#else
	_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2ILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC,
#else
	_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2ILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2J_FUNC,
#else
	_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2J_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2JLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC,
#else
	_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2JLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC,
#else
	_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC,
#else
	_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_FUNC,
#else
	_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_3B_3B_FUNC,
#else
	_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI_3B_3B_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ_FUNC,
#else
	_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ_3B_3B_FUNC,
#else
	_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BJ_3B_3B_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC,
#else
	_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC,
#else
	_1VtblCall__IJLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IISIII_FUNC,
#else
	_1VtblCall__IJSIII_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__IISJIJ_FUNC,
#else
	_1VtblCall__IJSJIJ_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3B_FUNC,
#else
	_1VtblCall__IJ_3B_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3BI_FUNC,
#else
	_1VtblCall__IJ_3BI_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3BII_3I_3I_FUNC,
#else
	_1VtblCall__IJ_3BII_3I_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3BIJ_3J_3I_FUNC,
#else
	_1VtblCall__IJ_3BIJ_3J_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3BILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC,
#else
	_1VtblCall__IJ_3BILorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3BI_3I_FUNC,
#else
	_1VtblCall__IJ_3BI_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3BI_3I_3I_FUNC,
#else
	_1VtblCall__IJ_3BI_3I_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3BI_3J_3I_FUNC,
#else
	_1VtblCall__IJ_3BI_3J_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3BJ_FUNC,
#else
	_1VtblCall__IJ_3BJ_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3BJLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC,
#else
	_1VtblCall__IJ_3BJLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3BJ_3I_FUNC,
#else
	_1VtblCall__IJ_3BJ_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2I_FUNC,
#else
	_1VtblCall__IJ_3BLorg_eclipse_swt_internal_mozilla_nsID_2I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2J_FUNC,
#else
	_1VtblCall__IJ_3BLorg_eclipse_swt_internal_mozilla_nsID_2J_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC,
#else
	_1VtblCall__IJ_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC,
#else
	_1VtblCall__IJ_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3J_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3B_3B_FUNC,
#else
	_1VtblCall__IJ_3B_3B_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3B_3BI_FUNC,
#else
	_1VtblCall__IJ_3B_3BI_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3B_3B_3BII_3I_FUNC,
#else
	_1VtblCall__IJ_3B_3B_3BII_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3B_3B_3BII_3J_FUNC,
#else
	_1VtblCall__IJ_3B_3B_3BII_3J_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3B_3B_3I_FUNC,
#else
	_1VtblCall__IJ_3B_3B_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3B_3B_3J_FUNC,
#else
	_1VtblCall__IJ_3B_3B_3J_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3B_3I_FUNC,
#else
	_1VtblCall__IJ_3B_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3B_3I_3I_FUNC,
#else
	_1VtblCall__IJ_3B_3I_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3B_3I_3J_FUNC,
#else
	_1VtblCall__IJ_3B_3I_3J_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3B_3J_FUNC,
#else
	_1VtblCall__IJ_3B_3J_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3B_3J_3I_FUNC,
#else
	_1VtblCall__IJ_3B_3J_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3C_FUNC,
#else
	_1VtblCall__IJ_3C_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3CIIII_FUNC,
#else
	_1VtblCall__IJ_3CIIII_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3CIJJJ_FUNC,
#else
	_1VtblCall__IJ_3CIJJJ_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3CI_3I_FUNC,
#else
	_1VtblCall__IJ_3CI_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3CJ_3J_FUNC,
#else
	_1VtblCall__IJ_3CJ_3J_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3C_3C_FUNC,
#else
	_1VtblCall__IJ_3C_3C_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3F_FUNC,
#else
	_1VtblCall__IJ_3F_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3I_FUNC,
#else
	_1VtblCall__IJ_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3I_3I_FUNC,
#else
	_1VtblCall__IJ_3I_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3I_3I_3I_FUNC,
#else
	_1VtblCall__IJ_3I_3I_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3I_3I_3I_3I_FUNC,
#else
	_1VtblCall__IJ_3I_3I_3I_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3I_3I_3I_3I_3I_3I_FUNC,
#else
	_1VtblCall__IJ_3I_3I_3I_3I_3I_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3I_3J_FUNC,
#else
	_1VtblCall__IJ_3I_3J_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3I_3J_3I_FUNC,
#else
	_1VtblCall__IJ_3I_3J_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3I_3J_3J_FUNC,
#else
	_1VtblCall__IJ_3I_3J_3J_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3J_FUNC,
#else
	_1VtblCall__IJ_3J_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3J_3J_FUNC,
#else
	_1VtblCall__IJ_3J_3J_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3J_3J_3J_FUNC,
#else
	_1VtblCall__IJ_3J_3J_3J_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3J_3J_3J_3J_3J_3I_FUNC,
#else
	_1VtblCall__IJ_3J_3J_3J_3J_3J_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3S_FUNC,
#else
	_1VtblCall__IJ_3S_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3SI_3I_3I_FUNC,
#else
	_1VtblCall__IJ_3SI_3I_3I_FUNC,
#endif
#ifndef JNI64
	_1VtblCall__II_3SJ_3I_3J_FUNC,
#else
	_1VtblCall__IJ_3SJ_3I_3J_FUNC,
#endif
	_1XPCOMGlueLoadXULFunctions_FUNC,
	_1XPCOMGlueShutdown_FUNC,
	_1XPCOMGlueStartup_FUNC,
	_1nsEmbedCString_1Length_FUNC,
	_1nsEmbedCString_1delete_FUNC,
	_1nsEmbedCString_1get_FUNC,
	_1nsEmbedCString_1new___FUNC,
#ifndef JNI64
	_1nsEmbedCString_1new__II_FUNC,
#else
	_1nsEmbedCString_1new__JI_FUNC,
#endif
	_1nsEmbedCString_1new___3BI_FUNC,
	_1nsEmbedString_1Length_FUNC,
	_1nsEmbedString_1delete_FUNC,
	_1nsEmbedString_1get_FUNC,
	_1nsEmbedString_1new___FUNC,
	_1nsEmbedString_1new___3C_FUNC,
	_1nsID_1Equals_FUNC,
	_1nsID_1delete_FUNC,
	_1nsID_1new_FUNC,
	_1nsIMemory_1Alloc_FUNC,
	_1nsIMemory_1Realloc_FUNC,
	_1nsIScriptContext_1GetNativeContext_FUNC,
	_1nsIScriptGlobalObject_1EnsureScriptEnvironment_FUNC,
	_1nsIScriptGlobalObject_1GetScriptContext_FUNC,
	_1nsIScriptGlobalObject_1GetScriptGlobal_FUNC,
#ifndef JNI64
	memmove__ILorg_eclipse_swt_internal_mozilla_nsDynamicFunctionLoad_2I_FUNC,
#else
	memmove__JLorg_eclipse_swt_internal_mozilla_nsDynamicFunctionLoad_2J_FUNC,
#endif
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
	nsDynamicFunctionLoad_1sizeof_FUNC,
	strlen_1PRUnichar_FUNC,
} XPCOM_FUNCS;
