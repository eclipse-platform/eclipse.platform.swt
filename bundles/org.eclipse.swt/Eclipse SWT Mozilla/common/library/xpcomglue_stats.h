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
extern int XPCOMGlue_nativeFunctionCount;
extern int XPCOMGlue_nativeFunctionCallCount[];
extern char* XPCOMGlue_nativeFunctionNames[];
#define XPCOMGlue_NATIVE_ENTER(env, that, func) XPCOMGlue_nativeFunctionCallCount[func]++;
#define XPCOMGlue_NATIVE_EXIT(env, that, func) 
#else
#define XPCOMGlue_NATIVE_ENTER(env, that, func) 
#define XPCOMGlue_NATIVE_EXIT(env, that, func) 
#endif

typedef enum {
	XPCOMGlueShutdown_FUNC,
	XPCOMGlueStartup_FUNC,
} XPCOMGlue_FUNCS;
