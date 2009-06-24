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
extern int XPCOMInit_nativeFunctionCount;
extern int XPCOMInit_nativeFunctionCallCount[];
extern char* XPCOMInit_nativeFunctionNames[];
#define XPCOMInit_NATIVE_ENTER(env, that, func) XPCOMInit_nativeFunctionCallCount[func]++;
#define XPCOMInit_NATIVE_EXIT(env, that, func) 
#else
#ifndef XPCOMInit_NATIVE_ENTER
#define XPCOMInit_NATIVE_ENTER(env, that, func) 
#endif
#ifndef XPCOMInit_NATIVE_EXIT
#define XPCOMInit_NATIVE_EXIT(env, that, func) 
#endif
#endif

typedef enum {
	GREVersionRange_1sizeof_FUNC,
	_1GRE_1GetGREPathWithProperties_FUNC,
	_1XPCOMGlueShutdown_FUNC,
	_1XPCOMGlueStartup_FUNC,
} XPCOMInit_FUNCS;
