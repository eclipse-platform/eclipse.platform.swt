/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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
#define C_NATIVE_ENTER(env, that, func) 
#define C_NATIVE_EXIT(env, that, func) 
#endif

typedef enum {
	PTR_1sizeof_FUNC,
	free_FUNC,
	malloc_FUNC,
	strlen_FUNC,
} C_FUNCS;
