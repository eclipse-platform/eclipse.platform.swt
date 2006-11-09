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
extern int XPCOM_PROFILE_nativeFunctionCount;
extern int XPCOM_PROFILE_nativeFunctionCallCount[];
extern char* XPCOM_PROFILE_nativeFunctionNames[];
#define XPCOM_PROFILE_NATIVE_ENTER(env, that, func) XPCOM_PROFILE_nativeFunctionCallCount[func]++;
#define XPCOM_PROFILE_NATIVE_EXIT(env, that, func) 
#else
#define XPCOM_PROFILE_NATIVE_ENTER(env, that, func) 
#define XPCOM_PROFILE_NATIVE_EXIT(env, that, func) 
#endif

typedef enum {
	NS_1NewProfileDirServiceProvider_FUNC,
	ProfileDirServiceProvider_1Register_FUNC,
	ProfileDirServiceProvider_1SetProfileDir_FUNC,
	ProfileDirServiceProvider_1Shutdown_FUNC,
} XPCOM_PROFILE_FUNCS;
