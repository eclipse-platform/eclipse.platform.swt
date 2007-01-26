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
extern int COM_nativeFunctionCount;
extern int COM_nativeFunctionCallCount[];
extern char* COM_nativeFunctionNames[];
#define COM_NATIVE_ENTER(env, that, func) COM_nativeFunctionCallCount[func]++;
#define COM_NATIVE_EXIT(env, that, func) 
#else
#ifndef COM_NATIVE_ENTER
#define COM_NATIVE_ENTER(env, that, func) 
#endif
#ifndef COM_NATIVE_EXIT
#define COM_NATIVE_EXIT(env, that, func) 
#endif
#endif

typedef enum {
	OleInitialize_FUNC,
	OleUninitialize_FUNC,
} COM_FUNCS;
