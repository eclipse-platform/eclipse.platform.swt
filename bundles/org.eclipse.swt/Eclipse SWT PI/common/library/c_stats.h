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
	memmove__III_FUNC,
	memmove__I_3BI_FUNC,
	memmove__I_3CI_FUNC,
	memmove__I_3DI_FUNC,
	memmove__I_3FI_FUNC,
	memmove__I_3II_FUNC,
	memmove__I_3JI_FUNC,
	memmove__I_3SI_FUNC,
	memmove___3BII_FUNC,
	memmove___3B_3CI_FUNC,
	memmove___3CII_FUNC,
	memmove___3DII_FUNC,
	memmove___3FII_FUNC,
	memmove___3III_FUNC,
	memmove___3I_3BI_FUNC,
	memmove___3JII_FUNC,
	memmove___3SII_FUNC,
	memset_FUNC,
	strlen_FUNC,
} C_FUNCS;
