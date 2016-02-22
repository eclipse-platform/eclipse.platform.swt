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
extern int C_nativeFunctionCount;
extern int C_nativeFunctionCallCount[];
extern char* C_nativeFunctionNames[];
#define C_NATIVE_ENTER(env, that, func) C_nativeFunctionCallCount[func]++;
#define C_NATIVE_EXIT(env, that, func) 
#else
#ifndef C_NATIVE_ENTER
#define C_NATIVE_ENTER(env, that, func) 
#endif
#ifndef C_NATIVE_EXIT
#define C_NATIVE_EXIT(env, that, func) 
#endif
#endif

typedef enum {
	PTR_1sizeof_FUNC,
	free_FUNC,
	getenv_FUNC,
	malloc_FUNC,
#ifndef JNI64
	memmove__III_FUNC,
#else
	memmove__JJJ_FUNC,
#endif
#ifndef JNI64
	memmove__I_3BI_FUNC,
#else
	memmove__J_3BJ_FUNC,
#endif
#ifndef JNI64
	memmove__I_3CI_FUNC,
#else
	memmove__J_3CJ_FUNC,
#endif
#ifndef JNI64
	memmove__I_3DI_FUNC,
#else
	memmove__J_3DJ_FUNC,
#endif
#ifndef JNI64
	memmove__I_3FI_FUNC,
#else
	memmove__J_3FJ_FUNC,
#endif
#ifndef JNI64
	memmove__I_3II_FUNC,
#else
	memmove__J_3IJ_FUNC,
#endif
#ifndef JNI64
	memmove__I_3JI_FUNC,
#else
	memmove__J_3JJ_FUNC,
#endif
#ifndef JNI64
	memmove__I_3SI_FUNC,
#else
	memmove__J_3SJ_FUNC,
#endif
#ifndef JNI64
	memmove___3BII_FUNC,
#else
	memmove___3BJJ_FUNC,
#endif
#ifndef JNI64
	memmove___3B_3CI_FUNC,
#else
	memmove___3B_3CJ_FUNC,
#endif
#ifndef JNI64
	memmove___3CII_FUNC,
#else
	memmove___3CJJ_FUNC,
#endif
#ifndef JNI64
	memmove___3DII_FUNC,
#else
	memmove___3DJJ_FUNC,
#endif
#ifndef JNI64
	memmove___3FII_FUNC,
#else
	memmove___3FJJ_FUNC,
#endif
#ifndef JNI64
	memmove___3III_FUNC,
#else
	memmove___3IJJ_FUNC,
#endif
#ifndef JNI64
	memmove___3I_3BI_FUNC,
#else
	memmove___3I_3BJ_FUNC,
#endif
#ifndef JNI64
	memmove___3JII_FUNC,
#else
	memmove___3JJJ_FUNC,
#endif
#ifndef JNI64
	memmove___3SII_FUNC,
#else
	memmove___3SJJ_FUNC,
#endif
	memset_FUNC,
	strlen_FUNC,
} C_FUNCS;
